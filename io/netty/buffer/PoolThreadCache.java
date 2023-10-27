/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolThreadCache
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
/*     */   
/*     */   final PoolArena<byte[]> heapArena;
/*     */   
/*     */   final PoolArena<ByteBuffer> directArena;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
/*     */   private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
/*     */   private final MemoryRegionCache<byte[]>[] normalHeapCaches;
/*     */   private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
/*     */   private final int numShiftsNormalDirect;
/*     */   private final int numShiftsNormalHeap;
/*     */   private final int freeSweepAllocationThreshold;
/*  58 */   private final AtomicBoolean freed = new AtomicBoolean();
/*     */   
/*     */ 
/*     */ 
/*     */   private int allocations;
/*     */   
/*     */ 
/*     */ 
/*     */   PoolThreadCache(PoolArena<byte[]> heapArena, PoolArena<ByteBuffer> directArena, int tinyCacheSize, int smallCacheSize, int normalCacheSize, int maxCachedBufferCapacity, int freeSweepAllocationThreshold)
/*     */   {
/*  68 */     if (maxCachedBufferCapacity < 0) {
/*  69 */       throw new IllegalArgumentException("maxCachedBufferCapacity: " + maxCachedBufferCapacity + " (expected: >= 0)");
/*     */     }
/*     */     
/*  72 */     this.freeSweepAllocationThreshold = freeSweepAllocationThreshold;
/*  73 */     this.heapArena = heapArena;
/*  74 */     this.directArena = directArena;
/*  75 */     if (directArena != null) {
/*  76 */       this.tinySubPageDirectCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
/*     */       
/*  78 */       this.smallSubPageDirectCaches = createSubPageCaches(smallCacheSize, directArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
/*     */       
/*     */ 
/*  81 */       this.numShiftsNormalDirect = log2(directArena.pageSize);
/*  82 */       this.normalDirectCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, directArena);
/*     */       
/*     */ 
/*  85 */       directArena.numThreadCaches.getAndIncrement();
/*     */     }
/*     */     else {
/*  88 */       this.tinySubPageDirectCaches = null;
/*  89 */       this.smallSubPageDirectCaches = null;
/*  90 */       this.normalDirectCaches = null;
/*  91 */       this.numShiftsNormalDirect = -1;
/*     */     }
/*  93 */     if (heapArena != null)
/*     */     {
/*  95 */       this.tinySubPageHeapCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
/*     */       
/*  97 */       this.smallSubPageHeapCaches = createSubPageCaches(smallCacheSize, heapArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
/*     */       
/*     */ 
/* 100 */       this.numShiftsNormalHeap = log2(heapArena.pageSize);
/* 101 */       this.normalHeapCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, heapArena);
/*     */       
/*     */ 
/* 104 */       heapArena.numThreadCaches.getAndIncrement();
/*     */     }
/*     */     else {
/* 107 */       this.tinySubPageHeapCaches = null;
/* 108 */       this.smallSubPageHeapCaches = null;
/* 109 */       this.normalHeapCaches = null;
/* 110 */       this.numShiftsNormalHeap = -1;
/*     */     }
/*     */     
/*     */ 
/* 114 */     if (((this.tinySubPageDirectCaches != null) || (this.smallSubPageDirectCaches != null) || (this.normalDirectCaches != null) || (this.tinySubPageHeapCaches != null) || (this.smallSubPageHeapCaches != null) || (this.normalHeapCaches != null)) && (freeSweepAllocationThreshold < 1))
/*     */     {
/*     */ 
/* 117 */       throw new IllegalArgumentException("freeSweepAllocationThreshold: " + freeSweepAllocationThreshold + " (expected: > 0)");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static <T> MemoryRegionCache<T>[] createSubPageCaches(int cacheSize, int numCaches, PoolArena.SizeClass sizeClass)
/*     */   {
/* 124 */     if ((cacheSize > 0) && (numCaches > 0))
/*     */     {
/* 126 */       MemoryRegionCache<T>[] cache = new MemoryRegionCache[numCaches];
/* 127 */       for (int i = 0; i < cache.length; i++)
/*     */       {
/* 129 */         cache[i] = new SubPageMemoryRegionCache(cacheSize, sizeClass);
/*     */       }
/* 131 */       return cache;
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static <T> MemoryRegionCache<T>[] createNormalCaches(int cacheSize, int maxCachedBufferCapacity, PoolArena<T> area)
/*     */   {
/* 139 */     if ((cacheSize > 0) && (maxCachedBufferCapacity > 0)) {
/* 140 */       int max = Math.min(area.chunkSize, maxCachedBufferCapacity);
/* 141 */       int arraySize = Math.max(1, log2(max / area.pageSize) + 1);
/*     */       
/*     */ 
/* 144 */       MemoryRegionCache<T>[] cache = new MemoryRegionCache[arraySize];
/* 145 */       for (int i = 0; i < cache.length; i++) {
/* 146 */         cache[i] = new NormalMemoryRegionCache(cacheSize);
/*     */       }
/* 148 */       return cache;
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   private static int log2(int val)
/*     */   {
/* 155 */     int res = 0;
/* 156 */     while (val > 1) {
/* 157 */       val >>= 1;
/* 158 */       res++;
/*     */     }
/* 160 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateTiny(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 167 */     return allocate(cacheForTiny(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateSmall(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 174 */     return allocate(cacheForSmall(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   boolean allocateNormal(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity)
/*     */   {
/* 181 */     return allocate(cacheForNormal(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */   
/*     */   private boolean allocate(MemoryRegionCache<?> cache, PooledByteBuf buf, int reqCapacity)
/*     */   {
/* 186 */     if (cache == null)
/*     */     {
/* 188 */       return false;
/*     */     }
/* 190 */     boolean allocated = cache.allocate(buf, reqCapacity);
/* 191 */     if (++this.allocations >= this.freeSweepAllocationThreshold) {
/* 192 */       this.allocations = 0;
/* 193 */       trim();
/*     */     }
/* 195 */     return allocated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(PoolArena<?> area, PoolChunk chunk, long handle, int normCapacity, PoolArena.SizeClass sizeClass)
/*     */   {
/* 204 */     MemoryRegionCache<?> cache = cache(area, normCapacity, sizeClass);
/* 205 */     if (cache == null) {
/* 206 */       return false;
/*     */     }
/* 208 */     return cache.add(chunk, handle);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cache(PoolArena<?> area, int normCapacity, PoolArena.SizeClass sizeClass) {
/* 212 */     switch (sizeClass) {
/*     */     case Normal: 
/* 214 */       return cacheForNormal(area, normCapacity);
/*     */     case Small: 
/* 216 */       return cacheForSmall(area, normCapacity);
/*     */     case Tiny: 
/* 218 */       return cacheForTiny(area, normCapacity);
/*     */     }
/* 220 */     throw new Error();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void finalize()
/*     */     throws java.lang.Throwable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 58	java/lang/Object:finalize	()V
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual 59	io/netty/buffer/PoolThreadCache:free	()V
/*     */     //   8: goto +10 -> 18
/*     */     //   11: astore_1
/*     */     //   12: aload_0
/*     */     //   13: invokevirtual 59	io/netty/buffer/PoolThreadCache:free	()V
/*     */     //   16: aload_1
/*     */     //   17: athrow
/*     */     //   18: return
/*     */     // Line number table:
/*     */     //   Java source line #228	-> byte code offset #0
/*     */     //   Java source line #230	-> byte code offset #4
/*     */     //   Java source line #231	-> byte code offset #8
/*     */     //   Java source line #230	-> byte code offset #11
/*     */     //   Java source line #231	-> byte code offset #16
/*     */     //   Java source line #232	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	PoolThreadCache
/*     */     //   11	6	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	11	finally
/*     */   }
/*     */   
/*     */   void free()
/*     */   {
/* 240 */     if (this.freed.compareAndSet(false, true))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */       int numFreed = free(this.tinySubPageDirectCaches) + free(this.smallSubPageDirectCaches) + free(this.normalDirectCaches) + free(this.tinySubPageHeapCaches) + free(this.smallSubPageHeapCaches) + free(this.normalHeapCaches);
/*     */       
/* 248 */       if ((numFreed > 0) && (logger.isDebugEnabled())) {
/* 249 */         logger.debug("Freed {} thread-local buffer(s) from thread: {}", Integer.valueOf(numFreed), 
/* 250 */           Thread.currentThread().getName());
/*     */       }
/*     */       
/* 253 */       if (this.directArena != null) {
/* 254 */         this.directArena.numThreadCaches.getAndDecrement();
/*     */       }
/*     */       
/* 257 */       if (this.heapArena != null) {
/* 258 */         this.heapArena.numThreadCaches.getAndDecrement();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?>[] caches) {
/* 264 */     if (caches == null) {
/* 265 */       return 0;
/*     */     }
/*     */     
/* 268 */     int numFreed = 0;
/* 269 */     for (MemoryRegionCache<?> c : caches) {
/* 270 */       numFreed += free(c);
/*     */     }
/* 272 */     return numFreed;
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?> cache) {
/* 276 */     if (cache == null) {
/* 277 */       return 0;
/*     */     }
/* 279 */     return cache.free();
/*     */   }
/*     */   
/*     */   void trim() {
/* 283 */     trim(this.tinySubPageDirectCaches);
/* 284 */     trim(this.smallSubPageDirectCaches);
/* 285 */     trim(this.normalDirectCaches);
/* 286 */     trim(this.tinySubPageHeapCaches);
/* 287 */     trim(this.smallSubPageHeapCaches);
/* 288 */     trim(this.normalHeapCaches);
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?>[] caches) {
/* 292 */     if (caches == null) {
/* 293 */       return;
/*     */     }
/* 295 */     for (MemoryRegionCache<?> c : caches) {
/* 296 */       trim(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?> cache) {
/* 301 */     if (cache == null) {
/* 302 */       return;
/*     */     }
/* 304 */     cache.trim();
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForTiny(PoolArena<?> area, int normCapacity) {
/* 308 */     int idx = PoolArena.tinyIdx(normCapacity);
/* 309 */     if (area.isDirect()) {
/* 310 */       return cache(this.tinySubPageDirectCaches, idx);
/*     */     }
/* 312 */     return cache(this.tinySubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForSmall(PoolArena<?> area, int normCapacity) {
/* 316 */     int idx = PoolArena.smallIdx(normCapacity);
/* 317 */     if (area.isDirect()) {
/* 318 */       return cache(this.smallSubPageDirectCaches, idx);
/*     */     }
/* 320 */     return cache(this.smallSubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForNormal(PoolArena<?> area, int normCapacity) {
/* 324 */     if (area.isDirect()) {
/* 325 */       int idx = log2(normCapacity >> this.numShiftsNormalDirect);
/* 326 */       return cache(this.normalDirectCaches, idx);
/*     */     }
/* 328 */     int idx = log2(normCapacity >> this.numShiftsNormalHeap);
/* 329 */     return cache(this.normalHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private static <T> MemoryRegionCache<T> cache(MemoryRegionCache<T>[] cache, int idx) {
/* 333 */     if ((cache == null) || (idx > cache.length - 1)) {
/* 334 */       return null;
/*     */     }
/* 336 */     return cache[idx];
/*     */   }
/*     */   
/*     */   private static final class SubPageMemoryRegionCache<T>
/*     */     extends PoolThreadCache.MemoryRegionCache<T>
/*     */   {
/*     */     SubPageMemoryRegionCache(int size, PoolArena.SizeClass sizeClass)
/*     */     {
/* 344 */       super(sizeClass);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 350 */       chunk.initBufWithSubpage(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NormalMemoryRegionCache<T>
/*     */     extends PoolThreadCache.MemoryRegionCache<T>
/*     */   {
/*     */     NormalMemoryRegionCache(int size)
/*     */     {
/* 359 */       super(PoolArena.SizeClass.Normal);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 365 */       chunk.initBuf(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class MemoryRegionCache<T> {
/*     */     private final int size;
/*     */     private final Queue<Entry<T>> queue;
/*     */     private final PoolArena.SizeClass sizeClass;
/*     */     private int allocations;
/*     */     
/*     */     MemoryRegionCache(int size, PoolArena.SizeClass sizeClass) {
/* 376 */       this.size = MathUtil.safeFindNextPositivePowerOfTwo(size);
/* 377 */       this.queue = PlatformDependent.newFixedMpscQueue(this.size);
/* 378 */       this.sizeClass = sizeClass;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected abstract void initBuf(PoolChunk<T> paramPoolChunk, long paramLong, PooledByteBuf<T> paramPooledByteBuf, int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final boolean add(PoolChunk<T> chunk, long handle)
/*     */     {
/* 392 */       Entry<T> entry = newEntry(chunk, handle);
/* 393 */       boolean queued = this.queue.offer(entry);
/* 394 */       if (!queued)
/*     */       {
/* 396 */         entry.recycle();
/*     */       }
/*     */       
/* 399 */       return queued;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final boolean allocate(PooledByteBuf<T> buf, int reqCapacity)
/*     */     {
/* 406 */       Entry<T> entry = (Entry)this.queue.poll();
/* 407 */       if (entry == null) {
/* 408 */         return false;
/*     */       }
/* 410 */       initBuf(entry.chunk, entry.handle, buf, reqCapacity);
/* 411 */       entry.recycle();
/*     */       
/*     */ 
/* 414 */       this.allocations += 1;
/* 415 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final int free()
/*     */     {
/* 422 */       return free(Integer.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private int free(int max) {
/* 426 */       for (int numFreed = 0; 
/* 427 */           numFreed < max; numFreed++) {
/* 428 */         Entry<T> entry = (Entry)this.queue.poll();
/* 429 */         if (entry != null) {
/* 430 */           freeEntry(entry);
/*     */         }
/*     */         else {
/* 433 */           return numFreed;
/*     */         }
/*     */       }
/* 436 */       return numFreed;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final void trim()
/*     */     {
/* 443 */       int free = this.size - this.allocations;
/* 444 */       this.allocations = 0;
/*     */       
/*     */ 
/* 447 */       if (free > 0) {
/* 448 */         free(free);
/*     */       }
/*     */     }
/*     */     
/*     */     private void freeEntry(Entry entry)
/*     */     {
/* 454 */       PoolChunk chunk = entry.chunk;
/* 455 */       long handle = entry.handle;
/*     */       
/*     */ 
/* 458 */       entry.recycle();
/*     */       
/* 460 */       chunk.arena.freeChunk(chunk, handle, this.sizeClass);
/*     */     }
/*     */     
/*     */     static final class Entry<T> {
/*     */       final Recycler.Handle<Entry<?>> recyclerHandle;
/*     */       PoolChunk<T> chunk;
/* 466 */       long handle = -1L;
/*     */       
/*     */       Entry(Recycler.Handle<Entry<?>> recyclerHandle) {
/* 469 */         this.recyclerHandle = recyclerHandle;
/*     */       }
/*     */       
/*     */       void recycle() {
/* 473 */         this.chunk = null;
/* 474 */         this.handle = -1L;
/* 475 */         this.recyclerHandle.recycle(this);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Entry newEntry(PoolChunk<?> chunk, long handle)
/*     */     {
/* 481 */       Entry entry = (Entry)RECYCLER.get();
/* 482 */       entry.chunk = chunk;
/* 483 */       entry.handle = handle;
/* 484 */       return entry;
/*     */     }
/*     */     
/*     */ 
/* 488 */     private static final Recycler<Entry> RECYCLER = new Recycler()
/*     */     {
/*     */       protected PoolThreadCache.MemoryRegionCache.Entry newObject(Recycler.Handle<PoolThreadCache.MemoryRegionCache.Entry> handle)
/*     */       {
/* 492 */         return new PoolThreadCache.MemoryRegionCache.Entry(handle);
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\PoolThreadCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */