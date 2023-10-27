/*     */ package io.netty.buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolChunk<T>
/*     */   implements PoolChunkMetric
/*     */ {
/*     */   private static final int INTEGER_SIZE_MINUS_ONE = 31;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final PoolArena<T> arena;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final T memory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean unpooled;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int offset;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] memoryMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] depthMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final PoolSubpage<T>[] subpages;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int subpageOverflowMask;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int pageSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int pageShifts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxOrder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int chunkSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int log2ChunkSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxSubpageAllocs;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte unusable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int freeBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunkList<T> parent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk<T> prev;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk<T> next;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize, int offset)
/*     */   {
/* 135 */     this.unpooled = false;
/* 136 */     this.arena = arena;
/* 137 */     this.memory = memory;
/* 138 */     this.pageSize = pageSize;
/* 139 */     this.pageShifts = pageShifts;
/* 140 */     this.maxOrder = maxOrder;
/* 141 */     this.chunkSize = chunkSize;
/* 142 */     this.offset = offset;
/* 143 */     this.unusable = ((byte)(maxOrder + 1));
/* 144 */     this.log2ChunkSize = log2(chunkSize);
/* 145 */     this.subpageOverflowMask = (pageSize - 1 ^ 0xFFFFFFFF);
/* 146 */     this.freeBytes = chunkSize;
/*     */     
/* 148 */     assert (maxOrder < 30) : ("maxOrder should be < 30, but is: " + maxOrder);
/* 149 */     this.maxSubpageAllocs = (1 << maxOrder);
/*     */     
/*     */ 
/* 152 */     this.memoryMap = new byte[this.maxSubpageAllocs << 1];
/* 153 */     this.depthMap = new byte[this.memoryMap.length];
/* 154 */     int memoryMapIndex = 1;
/* 155 */     for (int d = 0; d <= maxOrder; d++) {
/* 156 */       int depth = 1 << d;
/* 157 */       for (int p = 0; p < depth; p++)
/*     */       {
/* 159 */         this.memoryMap[memoryMapIndex] = ((byte)d);
/* 160 */         this.depthMap[memoryMapIndex] = ((byte)d);
/* 161 */         memoryMapIndex++;
/*     */       }
/*     */     }
/*     */     
/* 165 */     this.subpages = newSubpageArray(this.maxSubpageAllocs);
/*     */   }
/*     */   
/*     */   PoolChunk(PoolArena<T> arena, T memory, int size, int offset)
/*     */   {
/* 170 */     this.unpooled = true;
/* 171 */     this.arena = arena;
/* 172 */     this.memory = memory;
/* 173 */     this.offset = offset;
/* 174 */     this.memoryMap = null;
/* 175 */     this.depthMap = null;
/* 176 */     this.subpages = null;
/* 177 */     this.subpageOverflowMask = 0;
/* 178 */     this.pageSize = 0;
/* 179 */     this.pageShifts = 0;
/* 180 */     this.maxOrder = 0;
/* 181 */     this.unusable = ((byte)(this.maxOrder + 1));
/* 182 */     this.chunkSize = size;
/* 183 */     this.log2ChunkSize = log2(this.chunkSize);
/* 184 */     this.maxSubpageAllocs = 0;
/*     */   }
/*     */   
/*     */   private PoolSubpage<T>[] newSubpageArray(int size)
/*     */   {
/* 189 */     return new PoolSubpage[size];
/*     */   }
/*     */   
/*     */   public int usage()
/*     */   {
/*     */     int freeBytes;
/* 195 */     synchronized (this.arena) {
/* 196 */       freeBytes = this.freeBytes; }
/*     */     int freeBytes;
/* 198 */     return usage(freeBytes);
/*     */   }
/*     */   
/*     */   private int usage(int freeBytes) {
/* 202 */     if (freeBytes == 0) {
/* 203 */       return 100;
/*     */     }
/*     */     
/* 206 */     int freePercentage = (int)(freeBytes * 100L / this.chunkSize);
/* 207 */     if (freePercentage == 0) {
/* 208 */       return 99;
/*     */     }
/* 210 */     return 100 - freePercentage;
/*     */   }
/*     */   
/*     */   long allocate(int normCapacity) {
/* 214 */     if ((normCapacity & this.subpageOverflowMask) != 0) {
/* 215 */       return allocateRun(normCapacity);
/*     */     }
/* 217 */     return allocateSubpage(normCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateParentsAlloc(int id)
/*     */   {
/* 230 */     while (id > 1) {
/* 231 */       int parentId = id >>> 1;
/* 232 */       byte val1 = value(id);
/* 233 */       byte val2 = value(id ^ 0x1);
/* 234 */       byte val = val1 < val2 ? val1 : val2;
/* 235 */       setValue(parentId, val);
/* 236 */       id = parentId;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateParentsFree(int id)
/*     */   {
/* 248 */     int logChild = depth(id) + 1;
/* 249 */     while (id > 1) {
/* 250 */       int parentId = id >>> 1;
/* 251 */       byte val1 = value(id);
/* 252 */       byte val2 = value(id ^ 0x1);
/* 253 */       logChild--;
/*     */       
/* 255 */       if ((val1 == logChild) && (val2 == logChild)) {
/* 256 */         setValue(parentId, (byte)(logChild - 1));
/*     */       } else {
/* 258 */         byte val = val1 < val2 ? val1 : val2;
/* 259 */         setValue(parentId, val);
/*     */       }
/*     */       
/* 262 */       id = parentId;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int allocateNode(int d)
/*     */   {
/* 274 */     int id = 1;
/* 275 */     int initial = -(1 << d);
/* 276 */     byte val = value(id);
/* 277 */     if (val > d) {
/* 278 */       return -1;
/*     */     }
/* 280 */     while ((val < d) || ((id & initial) == 0)) {
/* 281 */       id <<= 1;
/* 282 */       val = value(id);
/* 283 */       if (val > d) {
/* 284 */         id ^= 0x1;
/* 285 */         val = value(id);
/*     */       }
/*     */     }
/* 288 */     byte value = value(id);
/* 289 */     if ((!$assertionsDisabled) && ((value != d) || ((id & initial) != 1 << d))) throw new AssertionError(String.format("val = %d, id & initial = %d, d = %d", new Object[] {
/* 290 */         Byte.valueOf(value), Integer.valueOf(id & initial), Integer.valueOf(d) }));
/* 291 */     setValue(id, this.unusable);
/* 292 */     updateParentsAlloc(id);
/* 293 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long allocateRun(int normCapacity)
/*     */   {
/* 303 */     int d = this.maxOrder - (log2(normCapacity) - this.pageShifts);
/* 304 */     int id = allocateNode(d);
/* 305 */     if (id < 0) {
/* 306 */       return id;
/*     */     }
/* 308 */     this.freeBytes -= runLength(id);
/* 309 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long allocateSubpage(int normCapacity)
/*     */   {
/* 322 */     PoolSubpage<T> head = this.arena.findSubpagePoolHead(normCapacity);
/* 323 */     synchronized (head) {
/* 324 */       int d = this.maxOrder;
/* 325 */       int id = allocateNode(d);
/* 326 */       if (id < 0) {
/* 327 */         return id;
/*     */       }
/*     */       
/* 330 */       PoolSubpage<T>[] subpages = this.subpages;
/* 331 */       int pageSize = this.pageSize;
/*     */       
/* 333 */       this.freeBytes -= pageSize;
/*     */       
/* 335 */       int subpageIdx = subpageIdx(id);
/* 336 */       PoolSubpage<T> subpage = subpages[subpageIdx];
/* 337 */       if (subpage == null) {
/* 338 */         subpage = new PoolSubpage(head, this, id, runOffset(id), pageSize, normCapacity);
/* 339 */         subpages[subpageIdx] = subpage;
/*     */       } else {
/* 341 */         subpage.init(head, normCapacity);
/*     */       }
/* 343 */       return subpage.allocate();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void free(long handle)
/*     */   {
/* 356 */     int memoryMapIdx = memoryMapIdx(handle);
/* 357 */     int bitmapIdx = bitmapIdx(handle);
/*     */     
/* 359 */     if (bitmapIdx != 0) {
/* 360 */       PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 361 */       assert ((subpage != null) && (subpage.doNotDestroy));
/*     */       
/*     */ 
/*     */ 
/* 365 */       PoolSubpage<T> head = this.arena.findSubpagePoolHead(subpage.elemSize);
/* 366 */       synchronized (head) {
/* 367 */         if (subpage.free(head, bitmapIdx & 0x3FFFFFFF)) {
/* 368 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 372 */     this.freeBytes += runLength(memoryMapIdx);
/* 373 */     setValue(memoryMapIdx, depth(memoryMapIdx));
/* 374 */     updateParentsFree(memoryMapIdx);
/*     */   }
/*     */   
/*     */   void initBuf(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 378 */     int memoryMapIdx = memoryMapIdx(handle);
/* 379 */     int bitmapIdx = bitmapIdx(handle);
/* 380 */     if (bitmapIdx == 0) {
/* 381 */       byte val = value(memoryMapIdx);
/* 382 */       assert (val == this.unusable) : String.valueOf(val);
/* 383 */       buf.init(this, handle, runOffset(memoryMapIdx) + this.offset, reqCapacity, runLength(memoryMapIdx), this.arena.parent
/* 384 */         .threadCache());
/*     */     } else {
/* 386 */       initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 391 */     initBufWithSubpage(buf, handle, bitmapIdx(handle), reqCapacity);
/*     */   }
/*     */   
/*     */   private void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int bitmapIdx, int reqCapacity) {
/* 395 */     assert (bitmapIdx != 0);
/*     */     
/* 397 */     int memoryMapIdx = memoryMapIdx(handle);
/*     */     
/* 399 */     PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 400 */     assert (subpage.doNotDestroy);
/* 401 */     assert (reqCapacity <= subpage.elemSize);
/*     */     
/* 403 */     buf.init(this, handle, 
/*     */     
/* 405 */       runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize + this.offset, reqCapacity, subpage.elemSize, this.arena.parent
/* 406 */       .threadCache());
/*     */   }
/*     */   
/*     */   private byte value(int id) {
/* 410 */     return this.memoryMap[id];
/*     */   }
/*     */   
/*     */   private void setValue(int id, byte val) {
/* 414 */     this.memoryMap[id] = val;
/*     */   }
/*     */   
/*     */   private byte depth(int id) {
/* 418 */     return this.depthMap[id];
/*     */   }
/*     */   
/*     */   private static int log2(int val)
/*     */   {
/* 423 */     return 31 - Integer.numberOfLeadingZeros(val);
/*     */   }
/*     */   
/*     */   private int runLength(int id)
/*     */   {
/* 428 */     return 1 << this.log2ChunkSize - depth(id);
/*     */   }
/*     */   
/*     */   private int runOffset(int id)
/*     */   {
/* 433 */     int shift = id ^ 1 << depth(id);
/* 434 */     return shift * runLength(id);
/*     */   }
/*     */   
/*     */   private int subpageIdx(int memoryMapIdx) {
/* 438 */     return memoryMapIdx ^ this.maxSubpageAllocs;
/*     */   }
/*     */   
/*     */   private static int memoryMapIdx(long handle) {
/* 442 */     return (int)handle;
/*     */   }
/*     */   
/*     */   private static int bitmapIdx(long handle) {
/* 446 */     return (int)(handle >>> 32);
/*     */   }
/*     */   
/*     */   public int chunkSize()
/*     */   {
/* 451 */     return this.chunkSize;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int freeBytes()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	io/netty/buffer/PoolChunk:arena	Lio/netty/buffer/PoolArena;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 15	io/netty/buffer/PoolChunk:freeBytes	I
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: ireturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #456	-> byte code offset #0
/*     */     //   Java source line #457	-> byte code offset #7
/*     */     //   Java source line #458	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	PoolChunk<T>
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*     */     int freeBytes;
/* 464 */     synchronized (this.arena) {
/* 465 */       freeBytes = this.freeBytes;
/*     */     }
/*     */     int freeBytes;
/* 468 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 477 */       "Chunk(" + Integer.toHexString(System.identityHashCode(this)) + ": " + usage(freeBytes) + "%, " + (this.chunkSize - freeBytes) + '/' + this.chunkSize + ')';
/*     */   }
/*     */   
/*     */   void destroy()
/*     */   {
/* 482 */     this.arena.destroyChunk(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\PoolChunk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */