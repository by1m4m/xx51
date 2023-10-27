/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class ArrayByteBufferPool
/*     */   implements ByteBufferPool
/*     */ {
/*     */   private final int _min;
/*     */   private final int _maxQueue;
/*     */   private final ByteBufferPool.Bucket[] _direct;
/*     */   private final ByteBufferPool.Bucket[] _indirect;
/*     */   private final int _inc;
/*     */   
/*     */   public ArrayByteBufferPool()
/*     */   {
/*  33 */     this(-1, -1, -1, -1);
/*     */   }
/*     */   
/*     */   public ArrayByteBufferPool(int minSize, int increment, int maxSize)
/*     */   {
/*  38 */     this(minSize, increment, maxSize, -1);
/*     */   }
/*     */   
/*     */   public ArrayByteBufferPool(int minSize, int increment, int maxSize, int maxQueue)
/*     */   {
/*  43 */     if (minSize <= 0)
/*  44 */       minSize = 0;
/*  45 */     if (increment <= 0)
/*  46 */       increment = 1024;
/*  47 */     if (maxSize <= 0)
/*  48 */       maxSize = 65536;
/*  49 */     if (minSize >= increment)
/*  50 */       throw new IllegalArgumentException("minSize >= increment");
/*  51 */     if ((maxSize % increment != 0) || (increment >= maxSize))
/*  52 */       throw new IllegalArgumentException("increment must be a divisor of maxSize");
/*  53 */     this._min = minSize;
/*  54 */     this._inc = increment;
/*     */     
/*  56 */     this._direct = new ByteBufferPool.Bucket[maxSize / increment];
/*  57 */     this._indirect = new ByteBufferPool.Bucket[maxSize / increment];
/*  58 */     this._maxQueue = maxQueue;
/*     */     
/*  60 */     int size = 0;
/*  61 */     for (int i = 0; i < this._direct.length; i++)
/*     */     {
/*  63 */       size += this._inc;
/*  64 */       this._direct[i] = new ByteBufferPool.Bucket(this, size, this._maxQueue);
/*  65 */       this._indirect[i] = new ByteBufferPool.Bucket(this, size, this._maxQueue);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer acquire(int size, boolean direct)
/*     */   {
/*  72 */     ByteBufferPool.Bucket bucket = bucketFor(size, direct);
/*  73 */     if (bucket == null) {
/*  74 */       return newByteBuffer(size, direct);
/*     */     }
/*  76 */     return bucket.acquire(direct);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void release(ByteBuffer buffer)
/*     */   {
/*  83 */     if (buffer != null)
/*     */     {
/*  85 */       ByteBufferPool.Bucket bucket = bucketFor(buffer.capacity(), buffer.isDirect());
/*  86 */       if (bucket != null) {
/*  87 */         bucket.release(buffer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/*  93 */     for (int i = 0; i < this._direct.length; i++)
/*     */     {
/*  95 */       this._direct[i].clear();
/*  96 */       this._indirect[i].clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private ByteBufferPool.Bucket bucketFor(int size, boolean direct)
/*     */   {
/* 102 */     if (size <= this._min)
/* 103 */       return null;
/* 104 */     int b = (size - 1) / this._inc;
/* 105 */     if (b >= this._direct.length)
/* 106 */       return null;
/* 107 */     ByteBufferPool.Bucket bucket = direct ? this._direct[b] : this._indirect[b];
/*     */     
/* 109 */     return bucket;
/*     */   }
/*     */   
/*     */ 
/*     */   ByteBufferPool.Bucket[] bucketsFor(boolean direct)
/*     */   {
/* 115 */     return direct ? this._direct : this._indirect;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ArrayByteBufferPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */