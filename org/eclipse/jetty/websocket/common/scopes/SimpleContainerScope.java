/*     */ package org.eclipse.jetty.websocket.common.scopes;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.MappedByteBufferPool;
/*     */ import org.eclipse.jetty.util.DecoratedObjectFactory;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*     */ import org.eclipse.jetty.util.thread.QueuedThreadPool;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSession;
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
/*     */ public class SimpleContainerScope
/*     */   extends ContainerLifeCycle
/*     */   implements WebSocketContainerScope
/*     */ {
/*     */   private final ByteBufferPool bufferPool;
/*     */   private final DecoratedObjectFactory objectFactory;
/*     */   private final WebSocketPolicy policy;
/*     */   private final Executor executor;
/*     */   private SslContextFactory sslContextFactory;
/*     */   
/*     */   public SimpleContainerScope(WebSocketPolicy policy)
/*     */   {
/*  42 */     this(policy, new MappedByteBufferPool(), new DecoratedObjectFactory());
/*  43 */     this.sslContextFactory = new SslContextFactory();
/*     */   }
/*     */   
/*     */   public SimpleContainerScope(WebSocketPolicy policy, ByteBufferPool bufferPool)
/*     */   {
/*  48 */     this(policy, bufferPool, new DecoratedObjectFactory());
/*     */   }
/*     */   
/*     */   public SimpleContainerScope(WebSocketPolicy policy, ByteBufferPool bufferPool, DecoratedObjectFactory objectFactory)
/*     */   {
/*  53 */     this(policy, bufferPool, (Executor)null, objectFactory);
/*     */   }
/*     */   
/*     */   public SimpleContainerScope(WebSocketPolicy policy, ByteBufferPool bufferPool, Executor executor, DecoratedObjectFactory objectFactory)
/*     */   {
/*  58 */     this.policy = policy;
/*  59 */     this.bufferPool = bufferPool;
/*     */     
/*  61 */     if (objectFactory == null)
/*     */     {
/*  63 */       this.objectFactory = new DecoratedObjectFactory();
/*     */     }
/*     */     else
/*     */     {
/*  67 */       this.objectFactory = objectFactory;
/*     */     }
/*     */     
/*  70 */     if (executor == null)
/*     */     {
/*  72 */       QueuedThreadPool threadPool = new QueuedThreadPool();
/*  73 */       String name = "WebSocketContainer@" + hashCode();
/*  74 */       threadPool.setName(name);
/*  75 */       threadPool.setDaemon(true);
/*  76 */       this.executor = threadPool;
/*  77 */       addBean(this.executor);
/*     */     }
/*     */     else
/*     */     {
/*  81 */       this.executor = executor;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*  88 */     super.doStart();
/*     */   }
/*     */   
/*     */   protected void doStop()
/*     */     throws Exception
/*     */   {
/*  94 */     super.doStop();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/* 100 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 106 */     return this.executor;
/*     */   }
/*     */   
/*     */ 
/*     */   public DecoratedObjectFactory getObjectFactory()
/*     */   {
/* 112 */     return this.objectFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 118 */     return this.policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public SslContextFactory getSslContextFactory()
/*     */   {
/* 124 */     return this.sslContextFactory;
/*     */   }
/*     */   
/*     */   public void setSslContextFactory(SslContextFactory sslContextFactory)
/*     */   {
/* 129 */     this.sslContextFactory = sslContextFactory;
/*     */   }
/*     */   
/*     */   public void onSessionOpened(WebSocketSession session) {}
/*     */   
/*     */   public void onSessionClosed(WebSocketSession session) {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\scopes\SimpleContainerScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */