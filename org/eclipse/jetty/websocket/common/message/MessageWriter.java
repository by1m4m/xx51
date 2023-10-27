/*     */ package org.eclipse.jetty.websocket.common.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ import org.eclipse.jetty.websocket.common.frames.TextFrame;
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
/*     */ public class MessageWriter
/*     */   extends Writer
/*     */ {
/*  44 */   private static final Logger LOG = Log.getLogger(MessageWriter.class);
/*     */   
/*     */   private final OutgoingFrames outgoing;
/*     */   private final ByteBufferPool bufferPool;
/*     */   private final BlockingWriteCallback blocker;
/*     */   private long frameCount;
/*     */   private TextFrame frame;
/*     */   private ByteBuffer buffer;
/*     */   private Utf8CharBuffer utf;
/*     */   private WriteCallback callback;
/*     */   private boolean closed;
/*     */   
/*     */   public MessageWriter(WebSocketSession session)
/*     */   {
/*  58 */     this(session.getOutgoingHandler(), session.getPolicy().getMaxTextMessageBufferSize(), session.getBufferPool());
/*     */   }
/*     */   
/*     */   public MessageWriter(OutgoingFrames outgoing, int bufferSize, ByteBufferPool bufferPool)
/*     */   {
/*  63 */     this.outgoing = outgoing;
/*  64 */     this.bufferPool = bufferPool;
/*  65 */     this.blocker = new BlockingWriteCallback();
/*  66 */     this.buffer = bufferPool.acquire(bufferSize, true);
/*  67 */     BufferUtil.flipToFill(this.buffer);
/*  68 */     this.frame = new TextFrame();
/*  69 */     this.utf = Utf8CharBuffer.wrap(this.buffer);
/*     */   }
/*     */   
/*     */   public void write(char[] chars, int off, int len)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  77 */       send(chars, off, len);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  82 */       notifyFailure(x);
/*  83 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int c)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  92 */       send(new char[] { (char)c }, 0, 1);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  97 */       notifyFailure(x);
/*  98 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       flush(false);
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 112 */       notifyFailure(x);
/* 113 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 122 */       flush(true);
/* 123 */       this.bufferPool.release(this.buffer);
/* 124 */       if (LOG.isDebugEnabled()) {
/* 125 */         LOG.debug("Stream closed, {} frames sent", this.frameCount);
/*     */       }
/* 127 */       notifySuccess();
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 132 */       notifyFailure(x);
/* 133 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */   private void flush(boolean fin) throws IOException
/*     */   {
/* 139 */     synchronized (this)
/*     */     {
/* 141 */       if (this.closed) {
/* 142 */         throw new IOException("Stream is closed");
/*     */       }
/* 144 */       this.closed = fin;
/*     */       
/* 146 */       ByteBuffer data = this.utf.getByteBuffer();
/* 147 */       if (LOG.isDebugEnabled())
/* 148 */         LOG.debug("flush({}): {}", new Object[] { Boolean.valueOf(fin), BufferUtil.toDetailString(this.buffer) });
/* 149 */       this.frame.setPayload(data);
/* 150 */       this.frame.setFin(fin);
/*     */       
/* 152 */       BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker();Throwable localThrowable3 = null;
/*     */       try {
/* 154 */         this.outgoing.outgoingFrame(this.frame, b, BatchMode.OFF);
/* 155 */         b.block();
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 152 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally
/*     */       {
/* 156 */         if (b != null) if (localThrowable3 != null) try { b.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else b.close();
/*     */       }
/* 158 */       this.frameCount += 1L;
/*     */       
/* 160 */       this.frame.setIsContinuation();
/*     */       
/* 162 */       this.utf.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private void send(char[] chars, int offset, int length) throws IOException
/*     */   {
/* 168 */     synchronized (this)
/*     */     {
/* 170 */       if (this.closed) {
/* 171 */         throw new IOException("Stream is closed");
/*     */       }
/* 173 */       while (length > 0)
/*     */       {
/*     */ 
/*     */ 
/* 177 */         int space = this.utf.remaining();
/* 178 */         int size = Math.min(space, length);
/* 179 */         this.utf.append(chars, offset, size);
/* 180 */         offset += size;
/* 181 */         length -= size;
/* 182 */         if (length > 0)
/*     */         {
/*     */ 
/*     */ 
/* 186 */           flush(false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCallback(WriteCallback callback)
/*     */   {
/* 194 */     synchronized (this)
/*     */     {
/* 196 */       this.callback = callback;
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifySuccess()
/*     */   {
/*     */     WriteCallback callback;
/* 203 */     synchronized (this)
/*     */     {
/* 205 */       callback = this.callback; }
/*     */     WriteCallback callback;
/* 207 */     if (callback != null)
/*     */     {
/* 209 */       callback.writeSuccess();
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyFailure(Throwable failure)
/*     */   {
/*     */     WriteCallback callback;
/* 216 */     synchronized (this)
/*     */     {
/* 218 */       callback = this.callback; }
/*     */     WriteCallback callback;
/* 220 */     if (callback != null)
/*     */     {
/* 222 */       callback.writeFailed(failure);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\MessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */