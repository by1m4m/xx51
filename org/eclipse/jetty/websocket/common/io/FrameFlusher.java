/*     */ package org.eclipse.jetty.websocket.common.io;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.common.Generator;
/*     */ import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
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
/*     */ public class FrameFlusher
/*     */   extends IteratingCallback
/*     */ {
/*  44 */   public static final BinaryFrame FLUSH_FRAME = new BinaryFrame();
/*  45 */   private static final Logger LOG = Log.getLogger(FrameFlusher.class);
/*     */   
/*     */   private final ByteBufferPool bufferPool;
/*     */   private final EndPoint endPoint;
/*     */   private final int bufferSize;
/*     */   private final Generator generator;
/*     */   private final int maxGather;
/*  52 */   private final Deque<FrameEntry> queue = new ArrayDeque();
/*     */   private final List<FrameEntry> entries;
/*     */   private final List<ByteBuffer> buffers;
/*     */   private boolean closed;
/*     */   private Throwable terminated;
/*     */   private ByteBuffer aggregate;
/*     */   private BatchMode batchMode;
/*     */   
/*     */   public FrameFlusher(ByteBufferPool bufferPool, Generator generator, EndPoint endPoint, int bufferSize, int maxGather)
/*     */   {
/*  62 */     this.bufferPool = bufferPool;
/*  63 */     this.endPoint = endPoint;
/*  64 */     this.bufferSize = bufferSize;
/*  65 */     this.generator = ((Generator)Objects.requireNonNull(generator));
/*  66 */     this.maxGather = maxGather;
/*  67 */     this.entries = new ArrayList(maxGather);
/*  68 */     this.buffers = new ArrayList(maxGather * 2 + 1);
/*     */   }
/*     */   
/*     */   public void enqueue(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/*  73 */     FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
/*     */     
/*     */ 
/*  76 */     synchronized (this)
/*     */     {
/*  78 */       Throwable closed = this.terminated;
/*  79 */       if (closed == null)
/*     */       {
/*  81 */         byte opCode = frame.getOpCode();
/*  82 */         if ((opCode == 9) || (opCode == 10)) {
/*  83 */           this.queue.offerFirst(entry);
/*     */         } else
/*  85 */           this.queue.offerLast(entry);
/*     */       }
/*     */     }
/*     */     Throwable closed;
/*  89 */     if (closed == null) {
/*  90 */       iterate();
/*     */     } else {
/*  92 */       notifyCallbackFailure(callback, closed);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IteratingCallback.Action process() throws Throwable
/*     */   {
/*  98 */     if (LOG.isDebugEnabled()) {
/*  99 */       LOG.debug("Flushing {}", new Object[] { this });
/*     */     }
/* 101 */     int space = this.aggregate == null ? this.bufferSize : BufferUtil.space(this.aggregate);
/* 102 */     BatchMode currentBatchMode = BatchMode.AUTO;
/* 103 */     synchronized (this)
/*     */     {
/* 105 */       if (this.closed) {
/* 106 */         return IteratingCallback.Action.SUCCEEDED;
/*     */       }
/* 108 */       if (this.terminated != null) {
/* 109 */         throw this.terminated;
/*     */       }
/* 111 */       while ((!this.queue.isEmpty()) && (this.entries.size() <= this.maxGather))
/*     */       {
/* 113 */         FrameEntry entry = (FrameEntry)this.queue.poll();
/* 114 */         currentBatchMode = BatchMode.max(currentBatchMode, entry.batchMode);
/*     */         
/*     */ 
/* 117 */         if (entry.frame == FLUSH_FRAME) {
/* 118 */           currentBatchMode = BatchMode.OFF;
/*     */         }
/* 120 */         int payloadLength = BufferUtil.length(entry.frame.getPayload());
/* 121 */         int approxFrameLength = 28 + payloadLength;
/*     */         
/*     */ 
/* 124 */         if (approxFrameLength > this.bufferSize >> 2) {
/* 125 */           currentBatchMode = BatchMode.OFF;
/*     */         }
/*     */         
/* 128 */         space -= approxFrameLength;
/* 129 */         if (space <= 0) {
/* 130 */           currentBatchMode = BatchMode.OFF;
/*     */         }
/* 132 */         this.entries.add(entry);
/*     */       }
/*     */     }
/*     */     
/* 136 */     if (LOG.isDebugEnabled()) {
/* 137 */       LOG.debug("{} processing {} entries: {}", new Object[] { this, Integer.valueOf(this.entries.size()), this.entries });
/*     */     }
/* 139 */     if (this.entries.isEmpty())
/*     */     {
/* 141 */       if (this.batchMode != BatchMode.AUTO)
/*     */       {
/*     */ 
/*     */ 
/* 145 */         releaseAggregate();
/* 146 */         return IteratingCallback.Action.IDLE;
/*     */       }
/*     */       
/* 149 */       if (LOG.isDebugEnabled()) {
/* 150 */         LOG.debug("{} auto flushing", new Object[] { this });
/*     */       }
/* 152 */       return flush();
/*     */     }
/*     */     
/* 155 */     this.batchMode = currentBatchMode;
/*     */     
/* 157 */     return currentBatchMode == BatchMode.OFF ? flush() : batch();
/*     */   }
/*     */   
/*     */   private IteratingCallback.Action batch()
/*     */   {
/* 162 */     if (this.aggregate == null)
/*     */     {
/* 164 */       this.aggregate = this.bufferPool.acquire(this.bufferSize, true);
/* 165 */       if (LOG.isDebugEnabled()) {
/* 166 */         LOG.debug("{} acquired aggregate buffer {}", new Object[] { this, this.aggregate });
/*     */       }
/*     */     }
/* 169 */     for (FrameEntry entry : this.entries)
/*     */     {
/* 171 */       entry.generateHeaderBytes(this.aggregate);
/*     */       
/* 173 */       ByteBuffer payload = entry.frame.getPayload();
/* 174 */       if (BufferUtil.hasContent(payload))
/* 175 */         BufferUtil.append(this.aggregate, payload);
/*     */     }
/* 177 */     if (LOG.isDebugEnabled()) {
/* 178 */       LOG.debug("{} aggregated {} frames: {}", new Object[] { this, Integer.valueOf(this.entries.size()), this.entries });
/*     */     }
/*     */     
/* 181 */     succeeded();
/*     */     
/* 183 */     return IteratingCallback.Action.SCHEDULED;
/*     */   }
/*     */   
/*     */   private IteratingCallback.Action flush()
/*     */   {
/* 188 */     if (!BufferUtil.isEmpty(this.aggregate))
/*     */     {
/* 190 */       this.buffers.add(this.aggregate);
/* 191 */       if (LOG.isDebugEnabled()) {
/* 192 */         LOG.debug("{} flushing aggregate {}", new Object[] { this, this.aggregate });
/*     */       }
/*     */     }
/* 195 */     for (FrameEntry entry : this.entries)
/*     */     {
/*     */ 
/* 198 */       if (entry.frame != FLUSH_FRAME)
/*     */       {
/*     */ 
/* 201 */         this.buffers.add(entry.generateHeaderBytes());
/* 202 */         ByteBuffer payload = entry.frame.getPayload();
/* 203 */         if (BufferUtil.hasContent(payload))
/* 204 */           this.buffers.add(payload);
/*     */       }
/*     */     }
/* 207 */     if (LOG.isDebugEnabled()) {
/* 208 */       LOG.debug("{} flushing {} frames: {}", new Object[] { this, Integer.valueOf(this.entries.size()), this.entries });
/*     */     }
/* 210 */     if (this.buffers.isEmpty())
/*     */     {
/* 212 */       releaseAggregate();
/*     */       
/* 214 */       succeedEntries();
/* 215 */       return IteratingCallback.Action.IDLE;
/*     */     }
/*     */     
/* 218 */     this.endPoint.write(this, (ByteBuffer[])this.buffers.toArray(new ByteBuffer[this.buffers.size()]));
/* 219 */     this.buffers.clear();
/* 220 */     return IteratingCallback.Action.SCHEDULED;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private int getQueueSize()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 6	org/eclipse/jetty/websocket/common/io/FrameFlusher:queue	Ljava/util/Deque;
/*     */     //   8: invokeinterface 76 1 0
/*     */     //   13: aload_1
/*     */     //   14: monitorexit
/*     */     //   15: ireturn
/*     */     //   16: astore_2
/*     */     //   17: aload_1
/*     */     //   18: monitorexit
/*     */     //   19: aload_2
/*     */     //   20: athrow
/*     */     // Line number table:
/*     */     //   Java source line #225	-> byte code offset #0
/*     */     //   Java source line #227	-> byte code offset #4
/*     */     //   Java source line #228	-> byte code offset #16
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	21	0	this	FrameFlusher
/*     */     //   2	16	1	Ljava/lang/Object;	Object
/*     */     //   16	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	15	16	finally
/*     */     //   16	19	16	finally
/*     */   }
/*     */   
/*     */   public void succeeded()
/*     */   {
/* 234 */     succeedEntries();
/* 235 */     super.succeeded();
/*     */   }
/*     */   
/*     */   private void succeedEntries()
/*     */   {
/* 240 */     for (FrameEntry entry : this.entries)
/*     */     {
/* 242 */       notifyCallbackSuccess(entry.callback);
/* 243 */       entry.release();
/* 244 */       if (entry.frame.getOpCode() == 8)
/*     */       {
/* 246 */         terminate(new ClosedChannelException(), true);
/* 247 */         this.endPoint.shutdownOutput();
/*     */       }
/*     */     }
/* 250 */     this.entries.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onCompleteFailure(Throwable failure)
/*     */   {
/* 256 */     releaseAggregate();
/*     */     
/*     */ 
/* 259 */     synchronized (this)
/*     */     {
/* 261 */       Throwable closed = this.terminated;
/* 262 */       if (closed == null)
/* 263 */         this.terminated = failure;
/* 264 */       this.entries.addAll(this.queue);
/* 265 */       this.queue.clear();
/*     */     }
/*     */     Throwable closed;
/* 268 */     for (??? = this.entries.iterator(); ((Iterator)???).hasNext();) { FrameEntry entry = (FrameEntry)((Iterator)???).next();
/*     */       
/* 270 */       notifyCallbackFailure(entry.callback, failure);
/* 271 */       entry.release();
/*     */     }
/* 273 */     this.entries.clear();
/*     */   }
/*     */   
/*     */   private void releaseAggregate()
/*     */   {
/* 278 */     if (BufferUtil.isEmpty(this.aggregate))
/*     */     {
/* 280 */       this.bufferPool.release(this.aggregate);
/* 281 */       this.aggregate = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void terminate(Throwable cause, boolean close)
/*     */   {
/* 288 */     synchronized (this)
/*     */     {
/* 290 */       this.closed = close;
/* 291 */       Throwable reason = this.terminated;
/* 292 */       if (reason == null)
/* 293 */         this.terminated = cause; }
/*     */     Throwable reason;
/* 295 */     if (LOG.isDebugEnabled())
/* 296 */       LOG.debug("{} {}", new Object[] { reason == null ? "Terminating" : "Terminated", this });
/* 297 */     if ((reason == null) && (!close)) {
/* 298 */       iterate();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void notifyCallbackSuccess(WriteCallback callback)
/*     */   {
/*     */     try {
/* 305 */       if (callback != null)
/*     */       {
/* 307 */         callback.writeSuccess();
/*     */       }
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 312 */       if (LOG.isDebugEnabled()) {
/* 313 */         LOG.debug("Exception while notifying success of callback " + callback, x);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void notifyCallbackFailure(WriteCallback callback, Throwable failure)
/*     */   {
/*     */     try {
/* 321 */       if (callback != null)
/*     */       {
/* 323 */         callback.writeFailed(failure);
/*     */       }
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 328 */       if (LOG.isDebugEnabled()) {
/* 329 */         LOG.debug("Exception while notifying failure of callback " + callback, x);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 336 */     return String.format("%s@%x[queueSize=%d,aggregateSize=%d,terminated=%s]", new Object[] {
/* 337 */       getClass().getSimpleName(), 
/* 338 */       Integer.valueOf(hashCode()), 
/* 339 */       Integer.valueOf(getQueueSize()), 
/* 340 */       Integer.valueOf(this.aggregate == null ? 0 : this.aggregate.position()), this.terminated });
/*     */   }
/*     */   
/*     */ 
/*     */   private class FrameEntry
/*     */   {
/*     */     private final Frame frame;
/*     */     private final WriteCallback callback;
/*     */     private final BatchMode batchMode;
/*     */     private ByteBuffer headerBuffer;
/*     */     
/*     */     private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */     {
/* 353 */       this.frame = ((Frame)Objects.requireNonNull(frame));
/* 354 */       this.callback = callback;
/* 355 */       this.batchMode = batchMode;
/*     */     }
/*     */     
/*     */     private ByteBuffer generateHeaderBytes()
/*     */     {
/* 360 */       return this.headerBuffer = FrameFlusher.this.generator.generateHeaderBytes(this.frame);
/*     */     }
/*     */     
/*     */     private void generateHeaderBytes(ByteBuffer buffer)
/*     */     {
/* 365 */       FrameFlusher.this.generator.generateHeaderBytes(this.frame, buffer);
/*     */     }
/*     */     
/*     */     private void release()
/*     */     {
/* 370 */       if (this.headerBuffer != null)
/*     */       {
/* 372 */         FrameFlusher.this.generator.getBufferPool().release(this.headerBuffer);
/* 373 */         this.headerBuffer = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 380 */       return String.format("%s[%s,%s,%s,%s]", new Object[] { getClass().getSimpleName(), this.frame, this.callback, this.batchMode, FrameFlusher.this.terminated });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\FrameFlusher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */