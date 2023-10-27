/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider.Listener;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*     */ public class OutputStreamContentProvider
/*     */   implements AsyncContentProvider, Callback, Closeable
/*     */ {
/*  79 */   private final DeferredContentProvider deferred = new DeferredContentProvider(new ByteBuffer[0]);
/*  80 */   private final OutputStream output = new DeferredOutputStream(null);
/*     */   
/*     */ 
/*     */   public Invocable.InvocationType getInvocationType()
/*     */   {
/*  85 */     return this.deferred.getInvocationType();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLength()
/*     */   {
/*  91 */     return this.deferred.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<ByteBuffer> iterator()
/*     */   {
/*  97 */     return this.deferred.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setListener(AsyncContentProvider.Listener listener)
/*     */   {
/* 103 */     this.deferred.setListener(listener);
/*     */   }
/*     */   
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 108 */     return this.output;
/*     */   }
/*     */   
/*     */   protected void write(ByteBuffer buffer)
/*     */   {
/* 113 */     this.deferred.offer(buffer);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/* 119 */     this.deferred.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public void succeeded()
/*     */   {
/* 125 */     this.deferred.succeeded();
/*     */   }
/*     */   
/*     */ 
/*     */   public void failed(Throwable failure)
/*     */   {
/* 131 */     this.deferred.failed(failure);
/*     */   }
/*     */   
/*     */   private class DeferredOutputStream extends OutputStream
/*     */   {
/*     */     private DeferredOutputStream() {}
/*     */     
/*     */     public void write(int b) throws IOException {
/* 139 */       write(new byte[] { (byte)b }, 0, 1);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len)
/*     */       throws IOException
/*     */     {
/* 145 */       OutputStreamContentProvider.this.write(ByteBuffer.wrap(b, off, len));
/* 146 */       flush();
/*     */     }
/*     */     
/*     */     public void flush()
/*     */       throws IOException
/*     */     {
/* 152 */       OutputStreamContentProvider.this.deferred.flush();
/*     */     }
/*     */     
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 158 */       OutputStreamContentProvider.this.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\OutputStreamContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */