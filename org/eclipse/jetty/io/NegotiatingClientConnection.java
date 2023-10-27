/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.net.ssl.SSLEngine;
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
/*     */ public abstract class NegotiatingClientConnection
/*     */   extends AbstractConnection
/*     */ {
/*  33 */   private static final Logger LOG = Log.getLogger(NegotiatingClientConnection.class);
/*     */   
/*     */   private final SSLEngine engine;
/*     */   private final ClientConnectionFactory connectionFactory;
/*     */   private final Map<String, Object> context;
/*     */   private volatile boolean completed;
/*     */   
/*     */   protected NegotiatingClientConnection(EndPoint endp, Executor executor, SSLEngine sslEngine, ClientConnectionFactory connectionFactory, Map<String, Object> context)
/*     */   {
/*  42 */     super(endp, executor);
/*  43 */     this.engine = sslEngine;
/*  44 */     this.connectionFactory = connectionFactory;
/*  45 */     this.context = context;
/*     */   }
/*     */   
/*     */   public SSLEngine getSSLEngine()
/*     */   {
/*  50 */     return this.engine;
/*     */   }
/*     */   
/*     */   protected void completed()
/*     */   {
/*  55 */     this.completed = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onOpen()
/*     */   {
/*  61 */     super.onOpen();
/*     */     try
/*     */     {
/*  64 */       getEndPoint().flush(new ByteBuffer[] { BufferUtil.EMPTY_BUFFER });
/*  65 */       if (this.completed) {
/*  66 */         replaceConnection();
/*     */       } else {
/*  68 */         fillInterested();
/*     */       }
/*     */     }
/*     */     catch (IOException x) {
/*  72 */       close();
/*  73 */       throw new RuntimeIOException(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onFillable()
/*     */   {
/*     */     for (;;)
/*     */     {
/*  82 */       int filled = fill();
/*  83 */       if ((this.completed) || (filled < 0))
/*     */       {
/*  85 */         replaceConnection();
/*  86 */         break;
/*     */       }
/*  88 */       if (filled == 0)
/*     */       {
/*  90 */         fillInterested();
/*  91 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int fill()
/*     */   {
/*     */     try
/*     */     {
/* 100 */       return getEndPoint().fill(BufferUtil.EMPTY_BUFFER);
/*     */     }
/*     */     catch (IOException x)
/*     */     {
/* 104 */       LOG.debug(x);
/* 105 */       close(); }
/* 106 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   private void replaceConnection()
/*     */   {
/* 112 */     EndPoint endPoint = getEndPoint();
/*     */     try
/*     */     {
/* 115 */       endPoint.upgrade(this.connectionFactory.newConnection(endPoint, this.context));
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 119 */       LOG.debug(x);
/* 120 */       close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 128 */     getEndPoint().shutdownOutput();
/* 129 */     super.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\NegotiatingClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */