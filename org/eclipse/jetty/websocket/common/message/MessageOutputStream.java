/*     */ package org.eclipse.jetty.websocket.common.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
/*     */ import org.eclipse.jetty.websocket.common.BlockingWriteCallback;
/*     */ import org.eclipse.jetty.websocket.common.BlockingWriteCallback.WriteBlocker;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageOutputStream
/*     */   extends OutputStream
/*     */ {
/*  42 */   private static final Logger LOG = Log.getLogger(MessageOutputStream.class);
/*     */   
/*     */   private final OutgoingFrames outgoing;
/*     */   private final ByteBufferPool bufferPool;
/*     */   private final BlockingWriteCallback blocker;
/*     */   private long frameCount;
/*     */   private BinaryFrame frame;
/*     */   private ByteBuffer buffer;
/*     */   private WriteCallback callback;
/*     */   private boolean closed;
/*     */   
/*     */   public MessageOutputStream(WebSocketSession session)
/*     */   {
/*  55 */     this(session.getOutgoingHandler(), session.getPolicy().getMaxBinaryMessageBufferSize(), session.getBufferPool());
/*     */   }
/*     */   
/*     */   public MessageOutputStream(OutgoingFrames outgoing, int bufferSize, ByteBufferPool bufferPool)
/*     */   {
/*  60 */     this.outgoing = outgoing;
/*  61 */     this.bufferPool = bufferPool;
/*  62 */     this.blocker = new BlockingWriteCallback();
/*  63 */     this.buffer = bufferPool.acquire(bufferSize, true);
/*  64 */     BufferUtil.flipToFill(this.buffer);
/*  65 */     this.frame = new BinaryFrame();
/*     */   }
/*     */   
/*     */   public void write(byte[] bytes, int off, int len)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  73 */       send(bytes, off, len);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  78 */       notifyFailure(x);
/*  79 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  88 */       send(new byte[] { (byte)b }, 0, 1);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  93 */       notifyFailure(x);
/*  94 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 103 */       flush(false);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 108 */       notifyFailure(x);
/* 109 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 118 */       flush(true);
/* 119 */       this.bufferPool.release(this.buffer);
/* 120 */       if (LOG.isDebugEnabled()) {
/* 121 */         LOG.debug("Stream closed, {} frames sent", this.frameCount);
/*     */       }
/* 123 */       notifySuccess();
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 128 */       notifyFailure(x);
/* 129 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   private void flush(boolean fin) throws IOException
/*     */   {
/* 135 */     synchronized (this)
/*     */     {
/* 137 */       if (this.closed) {
/* 138 */         throw new IOException("Stream is closed");
/*     */       }
/* 140 */       this.closed = fin;
/*     */       
/* 142 */       BufferUtil.flipToFlush(this.buffer, 0);
/* 143 */       this.frame.setPayload(this.buffer);
/* 144 */       this.frame.setFin(fin);
/*     */       
/* 146 */       BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker();Throwable localThrowable3 = null;
/*     */       try {
/* 148 */         this.outgoing.outgoingFrame(this.frame, b, BatchMode.OFF);
/* 149 */         b.block();
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 146 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally
/*     */       {
/* 150 */         if (b != null) if (localThrowable3 != null) try { b.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else b.close();
/*     */       }
/* 152 */       this.frameCount += 1L;
/*     */       
/* 154 */       this.frame.setIsContinuation();
/*     */       
/* 156 */       BufferUtil.flipToFill(this.buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private void send(byte[] bytes, int offset, int length) throws IOException
/*     */   {
/* 162 */     synchronized (this)
/*     */     {
/* 164 */       if (this.closed) {
/* 165 */         throw new IOException("Stream is closed");
/*     */       }
/* 167 */       while (length > 0)
/*     */       {
/*     */ 
/*     */ 
/* 171 */         int space = this.buffer.remaining();
/* 172 */         int size = Math.min(space, length);
/* 173 */         this.buffer.put(bytes, offset, size);
/* 174 */         offset += size;
/* 175 */         length -= size;
/* 176 */         if (length > 0)
/*     */         {
/*     */ 
/*     */ 
/* 180 */           flush(false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCallback(WriteCallback callback)
/*     */   {
/* 188 */     synchronized (this)
/*     */     {
/* 190 */       this.callback = callback;
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifySuccess()
/*     */   {
/*     */     WriteCallback callback;
/* 197 */     synchronized (this)
/*     */     {
/* 199 */       callback = this.callback; }
/*     */     WriteCallback callback;
/* 201 */     if (callback != null)
/*     */     {
/* 203 */       callback.writeSuccess();
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyFailure(Throwable failure)
/*     */   {
/*     */     WriteCallback callback;
/* 210 */     synchronized (this)
/*     */     {
/* 212 */       callback = this.callback; }
/*     */     WriteCallback callback;
/* 214 */     if (callback != null)
/*     */     {
/* 216 */       callback.writeFailed(failure);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\MessageOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */