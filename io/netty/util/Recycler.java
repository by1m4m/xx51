/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ public abstract class Recycler<T>
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Recycler.class);
/*     */   
/*     */ 
/*  44 */   private static final Handle NOOP_HANDLE = new Handle()
/*     */   {
/*     */     public void recycle(Object object) {}
/*     */   };
/*     */   
/*     */ 
/*  50 */   private static final AtomicInteger ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
/*  51 */   private static final int OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD = 4096;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_MAX_CAPACITY_PER_THREAD;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  64 */     int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread", 
/*  65 */       SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 4096));
/*  66 */     if (maxCapacityPerThread < 0) {
/*  67 */       maxCapacityPerThread = 4096;
/*     */     }
/*     */     
/*  70 */     DEFAULT_MAX_CAPACITY_PER_THREAD = maxCapacityPerThread;
/*     */     
/*  72 */     MAX_SHARED_CAPACITY_FACTOR = Math.max(2, 
/*  73 */       SystemPropertyUtil.getInt("io.netty.recycler.maxSharedCapacityFactor", 2));
/*     */     
/*     */ 
/*  76 */     MAX_DELAYED_QUEUES_PER_THREAD = Math.max(0, 
/*  77 */       SystemPropertyUtil.getInt("io.netty.recycler.maxDelayedQueuesPerThread", 
/*     */       
/*  79 */       NettyRuntime.availableProcessors() * 2));
/*     */     
/*  81 */     LINK_CAPACITY = MathUtil.safeFindNextPositivePowerOfTwo(
/*  82 */       Math.max(SystemPropertyUtil.getInt("io.netty.recycler.linkCapacity", 16), 16));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  87 */     RATIO = MathUtil.safeFindNextPositivePowerOfTwo(SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
/*     */     
/*  89 */     if (logger.isDebugEnabled())
/*  90 */       if (DEFAULT_MAX_CAPACITY_PER_THREAD == 0) {
/*  91 */         logger.debug("-Dio.netty.recycler.maxCapacityPerThread: disabled");
/*  92 */         logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: disabled");
/*  93 */         logger.debug("-Dio.netty.recycler.linkCapacity: disabled");
/*  94 */         logger.debug("-Dio.netty.recycler.ratio: disabled");
/*     */       } else {
/*  96 */         logger.debug("-Dio.netty.recycler.maxCapacityPerThread: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY_PER_THREAD));
/*  97 */         logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: {}", Integer.valueOf(MAX_SHARED_CAPACITY_FACTOR));
/*  98 */         logger.debug("-Dio.netty.recycler.linkCapacity: {}", Integer.valueOf(LINK_CAPACITY));
/*  99 */         logger.debug("-Dio.netty.recycler.ratio: {}", Integer.valueOf(RATIO));
/*     */       }
/*     */   }
/*     */   
/* 103 */   private static final int INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY_PER_THREAD, 256);
/*     */   private static final int MAX_SHARED_CAPACITY_FACTOR;
/*     */   private static final int MAX_DELAYED_QUEUES_PER_THREAD;
/*     */   private static final int LINK_CAPACITY;
/*     */   private static final int RATIO;
/*     */   private final int maxCapacityPerThread;
/*     */   private final int maxSharedCapacityFactor;
/*     */   private final int ratioMask;
/* 111 */   private final int maxDelayedQueuesPerThread; private final FastThreadLocal<Stack<T>> threadLocal = new FastThreadLocal()
/*     */   {
/*     */     protected Recycler.Stack<T> initialValue() {
/* 114 */       return new Recycler.Stack(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacityPerThread, Recycler.this.maxSharedCapacityFactor, 
/* 115 */         Recycler.this.ratioMask, Recycler.this.maxDelayedQueuesPerThread);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void onRemoval(Recycler.Stack<T> value)
/*     */     {
/* 121 */       if ((value.threadRef.get() == Thread.currentThread()) && 
/* 122 */         (Recycler.DELAYED_RECYCLED.isSet())) {
/* 123 */         ((Map)Recycler.DELAYED_RECYCLED.get()).remove(value);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   protected Recycler()
/*     */   {
/* 130 */     this(DEFAULT_MAX_CAPACITY_PER_THREAD);
/*     */   }
/*     */   
/*     */   protected Recycler(int maxCapacityPerThread) {
/* 134 */     this(maxCapacityPerThread, MAX_SHARED_CAPACITY_FACTOR);
/*     */   }
/*     */   
/*     */   protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor) {
/* 138 */     this(maxCapacityPerThread, maxSharedCapacityFactor, RATIO, MAX_DELAYED_QUEUES_PER_THREAD);
/*     */   }
/*     */   
/*     */   protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread)
/*     */   {
/* 143 */     this.ratioMask = (MathUtil.safeFindNextPositivePowerOfTwo(ratio) - 1);
/* 144 */     if (maxCapacityPerThread <= 0) {
/* 145 */       this.maxCapacityPerThread = 0;
/* 146 */       this.maxSharedCapacityFactor = 1;
/* 147 */       this.maxDelayedQueuesPerThread = 0;
/*     */     } else {
/* 149 */       this.maxCapacityPerThread = maxCapacityPerThread;
/* 150 */       this.maxSharedCapacityFactor = Math.max(1, maxSharedCapacityFactor);
/* 151 */       this.maxDelayedQueuesPerThread = Math.max(0, maxDelayedQueuesPerThread);
/*     */     }
/*     */   }
/*     */   
/*     */   public final T get()
/*     */   {
/* 157 */     if (this.maxCapacityPerThread == 0) {
/* 158 */       return (T)newObject(NOOP_HANDLE);
/*     */     }
/* 160 */     Stack<T> stack = (Stack)this.threadLocal.get();
/* 161 */     DefaultHandle<T> handle = stack.pop();
/* 162 */     if (handle == null) {
/* 163 */       handle = stack.newHandle();
/* 164 */       handle.value = newObject(handle);
/*     */     }
/* 166 */     return (T)handle.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean recycle(T o, Handle<T> handle)
/*     */   {
/* 174 */     if (handle == NOOP_HANDLE) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     DefaultHandle<T> h = (DefaultHandle)handle;
/* 179 */     if (h.stack.parent != this) {
/* 180 */       return false;
/*     */     }
/*     */     
/* 183 */     h.recycle(o);
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   final int threadLocalCapacity() {
/* 188 */     return ((Stack)this.threadLocal.get()).elements.length;
/*     */   }
/*     */   
/*     */   final int threadLocalSize() {
/* 192 */     return ((Stack)this.threadLocal.get()).size;
/*     */   }
/*     */   
/*     */   protected abstract T newObject(Handle<T> paramHandle);
/*     */   
/*     */   public static abstract interface Handle<T>
/*     */   {
/*     */     public abstract void recycle(T paramT);
/*     */   }
/*     */   
/*     */   static final class DefaultHandle<T> implements Recycler.Handle<T>
/*     */   {
/*     */     private int lastRecycledId;
/*     */     private int recycleId;
/*     */     boolean hasBeenRecycled;
/*     */     private Recycler.Stack<?> stack;
/*     */     private Object value;
/*     */     
/*     */     DefaultHandle(Recycler.Stack<?> stack) {
/* 211 */       this.stack = stack;
/*     */     }
/*     */     
/*     */     public void recycle(Object object)
/*     */     {
/* 216 */       if (object != this.value) {
/* 217 */         throw new IllegalArgumentException("object does not belong to handle");
/*     */       }
/*     */       
/* 220 */       Recycler.Stack<?> stack = this.stack;
/* 221 */       if ((this.lastRecycledId != this.recycleId) || (stack == null)) {
/* 222 */         throw new IllegalStateException("recycled already");
/*     */       }
/*     */       
/* 225 */       stack.push(this);
/*     */     }
/*     */   }
/*     */   
/* 229 */   private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED = new FastThreadLocal()
/*     */   {
/*     */     protected Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> initialValue()
/*     */     {
/* 233 */       return new WeakHashMap();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class WeakOrderQueue
/*     */   {
/* 241 */     static final WeakOrderQueue DUMMY = new WeakOrderQueue();
/*     */     private final Head head;
/*     */     private Link tail;
/*     */     
/*     */     static final class Link extends AtomicInteger {
/* 246 */       private final Recycler.DefaultHandle<?>[] elements = new Recycler.DefaultHandle[Recycler.LINK_CAPACITY];
/*     */       
/*     */       private int readIndex;
/*     */       
/*     */       Link next;
/*     */     }
/*     */     
/*     */     static final class Head
/*     */     {
/*     */       private final AtomicInteger availableSharedCapacity;
/*     */       Recycler.WeakOrderQueue.Link link;
/*     */       
/*     */       Head(AtomicInteger availableSharedCapacity)
/*     */       {
/* 260 */         this.availableSharedCapacity = availableSharedCapacity;
/*     */       }
/*     */       
/*     */       /* Error */
/*     */       protected void finalize()
/*     */         throws java.lang.Throwable
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: invokespecial 34	java/lang/Object:finalize	()V
/*     */         //   4: aload_0
/*     */         //   5: getfield 36	io/netty/util/Recycler$WeakOrderQueue$Head:link	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   8: astore_1
/*     */         //   9: aload_0
/*     */         //   10: aconst_null
/*     */         //   11: putfield 36	io/netty/util/Recycler$WeakOrderQueue$Head:link	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   14: aload_1
/*     */         //   15: ifnull +25 -> 40
/*     */         //   18: aload_0
/*     */         //   19: invokestatic 40	io/netty/util/Recycler:access$900	()I
/*     */         //   22: invokevirtual 44	io/netty/util/Recycler$WeakOrderQueue$Head:reclaimSpace	(I)V
/*     */         //   25: aload_1
/*     */         //   26: getfield 47	io/netty/util/Recycler$WeakOrderQueue$Link:next	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   29: astore_2
/*     */         //   30: aload_1
/*     */         //   31: aconst_null
/*     */         //   32: putfield 47	io/netty/util/Recycler$WeakOrderQueue$Link:next	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   35: aload_2
/*     */         //   36: astore_1
/*     */         //   37: goto -23 -> 14
/*     */         //   40: goto +49 -> 89
/*     */         //   43: astore_3
/*     */         //   44: aload_0
/*     */         //   45: getfield 36	io/netty/util/Recycler$WeakOrderQueue$Head:link	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   48: astore 4
/*     */         //   50: aload_0
/*     */         //   51: aconst_null
/*     */         //   52: putfield 36	io/netty/util/Recycler$WeakOrderQueue$Head:link	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   55: aload 4
/*     */         //   57: ifnull +30 -> 87
/*     */         //   60: aload_0
/*     */         //   61: invokestatic 40	io/netty/util/Recycler:access$900	()I
/*     */         //   64: invokevirtual 44	io/netty/util/Recycler$WeakOrderQueue$Head:reclaimSpace	(I)V
/*     */         //   67: aload 4
/*     */         //   69: getfield 47	io/netty/util/Recycler$WeakOrderQueue$Link:next	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   72: astore 5
/*     */         //   74: aload 4
/*     */         //   76: aconst_null
/*     */         //   77: putfield 47	io/netty/util/Recycler$WeakOrderQueue$Link:next	Lio/netty/util/Recycler$WeakOrderQueue$Link;
/*     */         //   80: aload 5
/*     */         //   82: astore 4
/*     */         //   84: goto -29 -> 55
/*     */         //   87: aload_3
/*     */         //   88: athrow
/*     */         //   89: return
/*     */         // Line number table:
/*     */         //   Java source line #267	-> byte code offset #0
/*     */         //   Java source line #269	-> byte code offset #4
/*     */         //   Java source line #270	-> byte code offset #9
/*     */         //   Java source line #271	-> byte code offset #14
/*     */         //   Java source line #272	-> byte code offset #18
/*     */         //   Java source line #273	-> byte code offset #25
/*     */         //   Java source line #275	-> byte code offset #30
/*     */         //   Java source line #276	-> byte code offset #35
/*     */         //   Java source line #277	-> byte code offset #37
/*     */         //   Java source line #278	-> byte code offset #40
/*     */         //   Java source line #269	-> byte code offset #43
/*     */         //   Java source line #270	-> byte code offset #50
/*     */         //   Java source line #271	-> byte code offset #55
/*     */         //   Java source line #272	-> byte code offset #60
/*     */         //   Java source line #273	-> byte code offset #67
/*     */         //   Java source line #275	-> byte code offset #74
/*     */         //   Java source line #276	-> byte code offset #80
/*     */         //   Java source line #277	-> byte code offset #84
/*     */         //   Java source line #278	-> byte code offset #87
/*     */         //   Java source line #279	-> byte code offset #89
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	90	0	this	Head
/*     */         //   8	29	1	head	Recycler.WeakOrderQueue.Link
/*     */         //   29	7	2	next	Recycler.WeakOrderQueue.Link
/*     */         //   43	45	3	localObject	Object
/*     */         //   48	35	4	head	Recycler.WeakOrderQueue.Link
/*     */         //   72	9	5	next	Recycler.WeakOrderQueue.Link
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   0	4	43	finally
/*     */       }
/*     */       
/*     */       void reclaimSpace(int space)
/*     */       {
/* 282 */         assert (space >= 0);
/* 283 */         this.availableSharedCapacity.addAndGet(space);
/*     */       }
/*     */       
/*     */       boolean reserveSpace(int space) {
/* 287 */         return reserveSpace(this.availableSharedCapacity, space);
/*     */       }
/*     */       
/*     */       static boolean reserveSpace(AtomicInteger availableSharedCapacity, int space) {
/* 291 */         assert (space >= 0);
/*     */         for (;;) {
/* 293 */           int available = availableSharedCapacity.get();
/* 294 */           if (available < space) {
/* 295 */             return false;
/*     */           }
/* 297 */           if (availableSharedCapacity.compareAndSet(available, available - space)) {
/* 298 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private WeakOrderQueue next;
/*     */     
/*     */     private final WeakReference<Thread> owner;
/*     */     
/* 310 */     private final int id = Recycler.ID_GENERATOR.getAndIncrement();
/*     */     
/*     */     private WeakOrderQueue() {
/* 313 */       this.owner = null;
/* 314 */       this.head = new Head(null);
/*     */     }
/*     */     
/*     */     private WeakOrderQueue(Recycler.Stack<?> stack, Thread thread) {
/* 318 */       this.tail = new Link();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 323 */       this.head = new Head(stack.availableSharedCapacity);
/* 324 */       this.head.link = this.tail;
/* 325 */       this.owner = new WeakReference(thread);
/*     */     }
/*     */     
/*     */     static WeakOrderQueue newQueue(Recycler.Stack<?> stack, Thread thread) {
/* 329 */       WeakOrderQueue queue = new WeakOrderQueue(stack, thread);
/*     */       
/*     */ 
/* 332 */       stack.setHead(queue);
/*     */       
/* 334 */       return queue;
/*     */     }
/*     */     
/*     */     private void setNext(WeakOrderQueue next) {
/* 338 */       assert (next != this);
/* 339 */       this.next = next;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     static WeakOrderQueue allocate(Recycler.Stack<?> stack, Thread thread)
/*     */     {
/* 347 */       return Head.reserveSpace(stack.availableSharedCapacity, Recycler.LINK_CAPACITY) ? 
/* 348 */         newQueue(stack, thread) : null;
/*     */     }
/*     */     
/*     */     void add(Recycler.DefaultHandle<?> handle) {
/* 352 */       Recycler.DefaultHandle.access$1102(handle, this.id);
/*     */       
/* 354 */       Link tail = this.tail;
/*     */       int writeIndex;
/* 356 */       if ((writeIndex = tail.get()) == Recycler.LINK_CAPACITY) {
/* 357 */         if (!this.head.reserveSpace(Recycler.LINK_CAPACITY))
/*     */         {
/* 359 */           return;
/*     */         }
/*     */         
/* 362 */         this.tail = (tail = tail.next = new Link());
/*     */         
/* 364 */         writeIndex = tail.get();
/*     */       }
/* 366 */       tail.elements[writeIndex] = handle;
/* 367 */       Recycler.DefaultHandle.access$602(handle, null);
/*     */       
/*     */ 
/* 370 */       tail.lazySet(writeIndex + 1);
/*     */     }
/*     */     
/*     */     boolean hasFinalData() {
/* 374 */       return this.tail.readIndex != this.tail.get();
/*     */     }
/*     */     
/*     */ 
/*     */     boolean transfer(Recycler.Stack<?> dst)
/*     */     {
/* 380 */       Link head = this.head.link;
/* 381 */       if (head == null) {
/* 382 */         return false;
/*     */       }
/*     */       
/* 385 */       if (head.readIndex == Recycler.LINK_CAPACITY) {
/* 386 */         if (head.next == null) {
/* 387 */           return false;
/*     */         }
/* 389 */         this.head.link = (head = head.next);
/*     */       }
/*     */       
/* 392 */       int srcStart = head.readIndex;
/* 393 */       int srcEnd = head.get();
/* 394 */       int srcSize = srcEnd - srcStart;
/* 395 */       if (srcSize == 0) {
/* 396 */         return false;
/*     */       }
/*     */       
/* 399 */       int dstSize = dst.size;
/* 400 */       int expectedCapacity = dstSize + srcSize;
/*     */       
/* 402 */       if (expectedCapacity > dst.elements.length) {
/* 403 */         int actualCapacity = dst.increaseCapacity(expectedCapacity);
/* 404 */         srcEnd = Math.min(srcStart + actualCapacity - dstSize, srcEnd);
/*     */       }
/*     */       
/* 407 */       if (srcStart != srcEnd) {
/* 408 */         Recycler.DefaultHandle[] srcElems = head.elements;
/* 409 */         Recycler.DefaultHandle[] dstElems = dst.elements;
/* 410 */         int newDstSize = dstSize;
/* 411 */         for (int i = srcStart; i < srcEnd; i++) {
/* 412 */           Recycler.DefaultHandle element = srcElems[i];
/* 413 */           if (Recycler.DefaultHandle.access$1400(element) == 0) {
/* 414 */             Recycler.DefaultHandle.access$1402(element, Recycler.DefaultHandle.access$1100(element));
/* 415 */           } else if (Recycler.DefaultHandle.access$1400(element) != Recycler.DefaultHandle.access$1100(element)) {
/* 416 */             throw new IllegalStateException("recycled already");
/*     */           }
/* 418 */           srcElems[i] = null;
/*     */           
/* 420 */           if (!dst.dropHandle(element))
/*     */           {
/*     */ 
/*     */ 
/* 424 */             Recycler.DefaultHandle.access$602(element, dst);
/* 425 */             dstElems[(newDstSize++)] = element;
/*     */           }
/*     */         }
/* 428 */         if ((srcEnd == Recycler.LINK_CAPACITY) && (head.next != null))
/*     */         {
/* 430 */           this.head.reclaimSpace(Recycler.LINK_CAPACITY);
/* 431 */           this.head.link = head.next;
/*     */         }
/*     */         
/* 434 */         head.readIndex = srcEnd;
/* 435 */         if (dst.size == newDstSize) {
/* 436 */           return false;
/*     */         }
/* 438 */         dst.size = newDstSize;
/* 439 */         return true;
/*     */       }
/*     */       
/* 442 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final class Stack<T>
/*     */   {
/*     */     final Recycler<T> parent;
/*     */     
/*     */ 
/*     */     final WeakReference<Thread> threadRef;
/*     */     
/*     */ 
/*     */     final AtomicInteger availableSharedCapacity;
/*     */     
/*     */ 
/*     */     final int maxDelayedQueues;
/*     */     
/*     */     private final int maxCapacity;
/*     */     
/*     */     private final int ratioMask;
/*     */     
/*     */     private Recycler.DefaultHandle<?>[] elements;
/*     */     
/*     */     private int size;
/*     */     
/* 469 */     private int handleRecycleCount = -1;
/*     */     private Recycler.WeakOrderQueue cursor;
/*     */     private Recycler.WeakOrderQueue prev;
/*     */     private volatile Recycler.WeakOrderQueue head;
/*     */     
/*     */     Stack(Recycler<T> parent, Thread thread, int maxCapacity, int maxSharedCapacityFactor, int ratioMask, int maxDelayedQueues) {
/* 475 */       this.parent = parent;
/* 476 */       this.threadRef = new WeakReference(thread);
/* 477 */       this.maxCapacity = maxCapacity;
/* 478 */       this.availableSharedCapacity = new AtomicInteger(Math.max(maxCapacity / maxSharedCapacityFactor, Recycler.LINK_CAPACITY));
/* 479 */       this.elements = new Recycler.DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
/* 480 */       this.ratioMask = ratioMask;
/* 481 */       this.maxDelayedQueues = maxDelayedQueues;
/*     */     }
/*     */     
/*     */     synchronized void setHead(Recycler.WeakOrderQueue queue)
/*     */     {
/* 486 */       Recycler.WeakOrderQueue.access$1600(queue, this.head);
/* 487 */       this.head = queue;
/*     */     }
/*     */     
/*     */     int increaseCapacity(int expectedCapacity) {
/* 491 */       int newCapacity = this.elements.length;
/* 492 */       int maxCapacity = this.maxCapacity;
/*     */       do {
/* 494 */         newCapacity <<= 1;
/* 495 */       } while ((newCapacity < expectedCapacity) && (newCapacity < maxCapacity));
/*     */       
/* 497 */       newCapacity = Math.min(newCapacity, maxCapacity);
/* 498 */       if (newCapacity != this.elements.length) {
/* 499 */         this.elements = ((Recycler.DefaultHandle[])Arrays.copyOf(this.elements, newCapacity));
/*     */       }
/*     */       
/* 502 */       return newCapacity;
/*     */     }
/*     */     
/*     */     Recycler.DefaultHandle<T> pop()
/*     */     {
/* 507 */       int size = this.size;
/* 508 */       if (size == 0) {
/* 509 */         if (!scavenge()) {
/* 510 */           return null;
/*     */         }
/* 512 */         size = this.size;
/*     */       }
/* 514 */       size--;
/* 515 */       Recycler.DefaultHandle ret = this.elements[size];
/* 516 */       this.elements[size] = null;
/* 517 */       if (Recycler.DefaultHandle.access$1100(ret) != Recycler.DefaultHandle.access$1400(ret)) {
/* 518 */         throw new IllegalStateException("recycled multiple times");
/*     */       }
/* 520 */       Recycler.DefaultHandle.access$1402(ret, 0);
/* 521 */       Recycler.DefaultHandle.access$1102(ret, 0);
/* 522 */       this.size = size;
/* 523 */       return ret;
/*     */     }
/*     */     
/*     */     boolean scavenge()
/*     */     {
/* 528 */       if (scavengeSome()) {
/* 529 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 533 */       this.prev = null;
/* 534 */       this.cursor = this.head;
/* 535 */       return false;
/*     */     }
/*     */     
/*     */     boolean scavengeSome()
/*     */     {
/* 540 */       Recycler.WeakOrderQueue cursor = this.cursor;
/* 541 */       Recycler.WeakOrderQueue prev; if (cursor == null) {
/* 542 */         Recycler.WeakOrderQueue prev = null;
/* 543 */         cursor = this.head;
/* 544 */         if (cursor == null) {
/* 545 */           return false;
/*     */         }
/*     */       } else {
/* 548 */         prev = this.prev;
/*     */       }
/*     */       
/* 551 */       boolean success = false;
/*     */       do {
/* 553 */         if (cursor.transfer(this)) {
/* 554 */           success = true;
/* 555 */           break;
/*     */         }
/* 557 */         Recycler.WeakOrderQueue next = Recycler.WeakOrderQueue.access$1700(cursor);
/* 558 */         if (Recycler.WeakOrderQueue.access$1800(cursor).get() == null)
/*     */         {
/*     */ 
/*     */ 
/* 562 */           if (cursor.hasFinalData())
/*     */           {
/* 564 */             while (cursor.transfer(this)) {
/* 565 */               success = true;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 572 */           if (prev != null) {
/* 573 */             Recycler.WeakOrderQueue.access$1600(prev, next);
/*     */           }
/*     */         } else {
/* 576 */           prev = cursor;
/*     */         }
/*     */         
/* 579 */         cursor = next;
/*     */       }
/* 581 */       while ((cursor != null) && (!success));
/*     */       
/* 583 */       this.prev = prev;
/* 584 */       this.cursor = cursor;
/* 585 */       return success;
/*     */     }
/*     */     
/*     */     void push(Recycler.DefaultHandle<?> item) {
/* 589 */       Thread currentThread = Thread.currentThread();
/* 590 */       if (this.threadRef.get() == currentThread)
/*     */       {
/* 592 */         pushNow(item);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 597 */         pushLater(item, currentThread);
/*     */       }
/*     */     }
/*     */     
/*     */     private void pushNow(Recycler.DefaultHandle<?> item) {
/* 602 */       if ((Recycler.DefaultHandle.access$1400(item) | Recycler.DefaultHandle.access$1100(item)) != 0) {
/* 603 */         throw new IllegalStateException("recycled already");
/*     */       }
/* 605 */       Recycler.DefaultHandle.access$1402(item, Recycler.DefaultHandle.access$1102(item, Recycler.OWN_THREAD_ID));
/*     */       
/* 607 */       int size = this.size;
/* 608 */       if ((size >= this.maxCapacity) || (dropHandle(item)))
/*     */       {
/* 610 */         return;
/*     */       }
/* 612 */       if (size == this.elements.length) {
/* 613 */         this.elements = ((Recycler.DefaultHandle[])Arrays.copyOf(this.elements, Math.min(size << 1, this.maxCapacity)));
/*     */       }
/*     */       
/* 616 */       this.elements[size] = item;
/* 617 */       this.size = (size + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void pushLater(Recycler.DefaultHandle<?> item, Thread thread)
/*     */     {
/* 624 */       Map<Stack<?>, Recycler.WeakOrderQueue> delayedRecycled = (Map)Recycler.DELAYED_RECYCLED.get();
/* 625 */       Recycler.WeakOrderQueue queue = (Recycler.WeakOrderQueue)delayedRecycled.get(this);
/* 626 */       if (queue == null) {
/* 627 */         if (delayedRecycled.size() >= this.maxDelayedQueues)
/*     */         {
/* 629 */           delayedRecycled.put(this, Recycler.WeakOrderQueue.DUMMY);
/* 630 */           return;
/*     */         }
/*     */         
/* 633 */         if ((queue = Recycler.WeakOrderQueue.allocate(this, thread)) == null)
/*     */         {
/* 635 */           return;
/*     */         }
/* 637 */         delayedRecycled.put(this, queue);
/* 638 */       } else if (queue == Recycler.WeakOrderQueue.DUMMY)
/*     */       {
/* 640 */         return;
/*     */       }
/*     */       
/* 643 */       queue.add(item);
/*     */     }
/*     */     
/*     */     boolean dropHandle(Recycler.DefaultHandle<?> handle) {
/* 647 */       if (!handle.hasBeenRecycled) {
/* 648 */         if ((++this.handleRecycleCount & this.ratioMask) != 0)
/*     */         {
/* 650 */           return true;
/*     */         }
/* 652 */         handle.hasBeenRecycled = true;
/*     */       }
/* 654 */       return false;
/*     */     }
/*     */     
/*     */     Recycler.DefaultHandle<T> newHandle() {
/* 658 */       return new Recycler.DefaultHandle(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\Recycler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */