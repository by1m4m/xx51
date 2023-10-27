/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.eclipse.jetty.util.BufferUtil;
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
/*     */ public abstract interface ByteBufferPool
/*     */ {
/*     */   public abstract ByteBuffer acquire(int paramInt, boolean paramBoolean);
/*     */   
/*     */   public abstract void release(ByteBuffer paramByteBuffer);
/*     */   
/*     */   public ByteBuffer newByteBuffer(int capacity, boolean direct)
/*     */   {
/*  61 */     return direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity);
/*     */   }
/*     */   
/*     */   public static class Lease
/*     */   {
/*     */     private final ByteBufferPool byteBufferPool;
/*     */     private final List<ByteBuffer> buffers;
/*     */     private final List<Boolean> recycles;
/*     */     
/*     */     public Lease(ByteBufferPool byteBufferPool)
/*     */     {
/*  72 */       this.byteBufferPool = byteBufferPool;
/*  73 */       this.buffers = new ArrayList();
/*  74 */       this.recycles = new ArrayList();
/*     */     }
/*     */     
/*     */     public ByteBuffer acquire(int capacity, boolean direct)
/*     */     {
/*  79 */       ByteBuffer buffer = this.byteBufferPool.acquire(capacity, direct);
/*  80 */       BufferUtil.clearToFill(buffer);
/*  81 */       return buffer;
/*     */     }
/*     */     
/*     */     public void append(ByteBuffer buffer, boolean recycle)
/*     */     {
/*  86 */       this.buffers.add(buffer);
/*  87 */       this.recycles.add(Boolean.valueOf(recycle));
/*     */     }
/*     */     
/*     */     public void insert(int index, ByteBuffer buffer, boolean recycle)
/*     */     {
/*  92 */       this.buffers.add(index, buffer);
/*  93 */       this.recycles.add(index, Boolean.valueOf(recycle));
/*     */     }
/*     */     
/*     */     public List<ByteBuffer> getByteBuffers()
/*     */     {
/*  98 */       return this.buffers;
/*     */     }
/*     */     
/*     */     public long getTotalLength()
/*     */     {
/* 103 */       long length = 0L;
/* 104 */       for (int i = 0; i < this.buffers.size(); i++)
/* 105 */         length += ((ByteBuffer)this.buffers.get(i)).remaining();
/* 106 */       return length;
/*     */     }
/*     */     
/*     */     public int getSize()
/*     */     {
/* 111 */       return this.buffers.size();
/*     */     }
/*     */     
/*     */     public void recycle()
/*     */     {
/* 116 */       for (int i = 0; i < this.buffers.size(); i++)
/*     */       {
/* 118 */         ByteBuffer buffer = (ByteBuffer)this.buffers.get(i);
/* 119 */         if (((Boolean)this.recycles.get(i)).booleanValue())
/* 120 */           this.byteBufferPool.release(buffer);
/*     */       }
/* 122 */       this.buffers.clear();
/* 123 */       this.recycles.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Bucket
/*     */   {
/* 129 */     private final Deque<ByteBuffer> _queue = new ConcurrentLinkedDeque();
/*     */     private final ByteBufferPool _pool;
/*     */     private final int _capacity;
/*     */     private final AtomicInteger _space;
/*     */     
/*     */     public Bucket(ByteBufferPool pool, int bufferSize, int maxSize)
/*     */     {
/* 136 */       this._pool = pool;
/* 137 */       this._capacity = bufferSize;
/* 138 */       this._space = (maxSize > 0 ? new AtomicInteger(maxSize) : null);
/*     */     }
/*     */     
/*     */     public ByteBuffer acquire(boolean direct)
/*     */     {
/* 143 */       ByteBuffer buffer = queuePoll();
/* 144 */       if (buffer == null)
/* 145 */         return this._pool.newByteBuffer(this._capacity, direct);
/* 146 */       if (this._space != null)
/* 147 */         this._space.incrementAndGet();
/* 148 */       return buffer;
/*     */     }
/*     */     
/*     */     public void release(ByteBuffer buffer)
/*     */     {
/* 153 */       BufferUtil.clear(buffer);
/* 154 */       if (this._space == null) {
/* 155 */         queueOffer(buffer);
/* 156 */       } else if (this._space.decrementAndGet() >= 0) {
/* 157 */         queueOffer(buffer);
/*     */       } else {
/* 159 */         this._space.incrementAndGet();
/*     */       }
/*     */     }
/*     */     
/*     */     public void clear() {
/* 164 */       if (this._space == null)
/*     */       {
/* 166 */         queueClear();
/*     */       }
/*     */       else
/*     */       {
/* 170 */         int s = this._space.getAndSet(0);
/* 171 */         while (s-- > 0)
/*     */         {
/* 173 */           if (queuePoll() == null) {
/* 174 */             this._space.incrementAndGet();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void queueOffer(ByteBuffer buffer) {
/* 181 */       this._queue.offerFirst(buffer);
/*     */     }
/*     */     
/*     */     private ByteBuffer queuePoll()
/*     */     {
/* 186 */       return (ByteBuffer)this._queue.poll();
/*     */     }
/*     */     
/*     */     private void queueClear()
/*     */     {
/* 191 */       this._queue.clear();
/*     */     }
/*     */     
/*     */     boolean isEmpty()
/*     */     {
/* 196 */       return this._queue.isEmpty();
/*     */     }
/*     */     
/*     */     int size()
/*     */     {
/* 201 */       return this._queue.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 207 */       return String.format("Bucket@%x{%d/%d}", new Object[] { Integer.valueOf(hashCode()), Integer.valueOf(size()), Integer.valueOf(this._capacity) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ByteBufferPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */