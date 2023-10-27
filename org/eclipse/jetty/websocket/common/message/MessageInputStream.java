/*     */ package org.eclipse.jetty.websocket.common.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public class MessageInputStream
/*     */   extends InputStream
/*     */   implements MessageAppender
/*     */ {
/*  40 */   private static final Logger LOG = Log.getLogger(MessageInputStream.class);
/*  41 */   private static final ByteBuffer EOF = ByteBuffer.allocate(0).asReadOnlyBuffer();
/*     */   
/*  43 */   private final BlockingDeque<ByteBuffer> buffers = new LinkedBlockingDeque();
/*  44 */   private AtomicBoolean closed = new AtomicBoolean(false);
/*     */   private final long timeoutMs;
/*  46 */   private ByteBuffer activeBuffer = null;
/*     */   
/*     */ 
/*     */   private static boolean isTheEofBuffer(ByteBuffer buf)
/*     */   {
/*  51 */     boolean isTheEofBuffer = buf == EOF;
/*  52 */     return isTheEofBuffer;
/*     */   }
/*     */   
/*     */   public MessageInputStream()
/*     */   {
/*  57 */     this(-1);
/*     */   }
/*     */   
/*     */   public MessageInputStream(int timeoutMs)
/*     */   {
/*  62 */     this.timeoutMs = timeoutMs;
/*     */   }
/*     */   
/*     */   public void appendFrame(ByteBuffer framePayload, boolean fin)
/*     */     throws IOException
/*     */   {
/*  68 */     if (LOG.isDebugEnabled())
/*     */     {
/*  70 */       LOG.debug("Appending {} chunk: {}", new Object[] { fin ? "final" : "non-final", BufferUtil.toDetailString(framePayload) });
/*     */     }
/*     */     
/*     */ 
/*  74 */     if (this.closed.get())
/*     */     {
/*  76 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  84 */       if (framePayload == null)
/*     */       {
/*     */ 
/*  87 */         return;
/*     */       }
/*     */       
/*  90 */       int capacity = framePayload.remaining();
/*  91 */       if (capacity <= 0)
/*     */       {
/*     */ 
/*  94 */         return;
/*     */       }
/*     */       
/*  97 */       ByteBuffer copy = framePayload.isDirect() ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
/*  98 */       copy.put(framePayload).flip();
/*  99 */       this.buffers.put(copy);
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 103 */       throw new IOException(e);
/*     */     }
/*     */     finally
/*     */     {
/* 107 */       if (fin)
/*     */       {
/* 109 */         this.buffers.offer(EOF);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 117 */     if (this.closed.compareAndSet(false, true))
/*     */     {
/* 119 */       this.buffers.offer(EOF);
/* 120 */       super.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mark(int readlimit) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 133 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void messageComplete()
/*     */   {
/* 139 */     if (LOG.isDebugEnabled())
/* 140 */       LOG.debug("Message completed", new Object[0]);
/* 141 */     this.buffers.offer(EOF);
/*     */   }
/*     */   
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 149 */       if (this.closed.get())
/*     */       {
/* 151 */         if (LOG.isDebugEnabled())
/* 152 */           LOG.debug("Stream closed", new Object[0]);
/* 153 */         return -1;
/*     */       }
/*     */       
/*     */ 
/* 157 */       while ((this.activeBuffer == null) || (!this.activeBuffer.hasRemaining()))
/*     */       {
/* 159 */         if (LOG.isDebugEnabled())
/* 160 */           LOG.debug("Waiting {} ms to read", this.timeoutMs);
/* 161 */         if (this.timeoutMs < 0L)
/*     */         {
/*     */ 
/* 164 */           this.activeBuffer = ((ByteBuffer)this.buffers.take());
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 169 */           this.activeBuffer = ((ByteBuffer)this.buffers.poll(this.timeoutMs, TimeUnit.MILLISECONDS));
/* 170 */           if (this.activeBuffer == null)
/*     */           {
/* 172 */             throw new IOException(String.format("Read timeout: %,dms expired", new Object[] { Long.valueOf(this.timeoutMs) }));
/*     */           }
/*     */         }
/*     */         
/* 176 */         if (isTheEofBuffer(this.activeBuffer))
/*     */         {
/* 178 */           if (LOG.isDebugEnabled()) {
/* 179 */             LOG.debug("Reached EOF", new Object[0]);
/*     */           }
/* 181 */           this.closed.set(true);
/*     */           
/* 183 */           this.buffers.clear();
/* 184 */           return -1;
/*     */         }
/*     */       }
/*     */       
/* 188 */       return this.activeBuffer.get() & 0xFF;
/*     */     }
/*     */     catch (InterruptedException x)
/*     */     {
/* 192 */       if (LOG.isDebugEnabled())
/* 193 */         LOG.debug("Interrupted while waiting to read", x);
/* 194 */       this.closed.set(true); }
/* 195 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 202 */     throw new IOException("reset() not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\MessageInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */