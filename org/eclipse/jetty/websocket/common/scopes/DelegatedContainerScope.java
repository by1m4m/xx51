/*    */ package org.eclipse.jetty.websocket.common.scopes;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.eclipse.jetty.io.ByteBufferPool;
/*    */ import org.eclipse.jetty.util.DecoratedObjectFactory;
/*    */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*    */ import org.eclipse.jetty.websocket.common.WebSocketSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelegatedContainerScope
/*    */   implements WebSocketContainerScope
/*    */ {
/*    */   private final WebSocketPolicy policy;
/*    */   private final WebSocketContainerScope delegate;
/*    */   
/*    */   public DelegatedContainerScope(WebSocketPolicy policy, WebSocketContainerScope parentScope)
/*    */   {
/* 36 */     this.policy = policy;
/* 37 */     this.delegate = parentScope;
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBufferPool getBufferPool()
/*    */   {
/* 43 */     return this.delegate.getBufferPool();
/*    */   }
/*    */   
/*    */ 
/*    */   public Executor getExecutor()
/*    */   {
/* 49 */     return this.delegate.getExecutor();
/*    */   }
/*    */   
/*    */ 
/*    */   public DecoratedObjectFactory getObjectFactory()
/*    */   {
/* 55 */     return this.delegate.getObjectFactory();
/*    */   }
/*    */   
/*    */ 
/*    */   public WebSocketPolicy getPolicy()
/*    */   {
/* 61 */     return this.policy;
/*    */   }
/*    */   
/*    */ 
/*    */   public SslContextFactory getSslContextFactory()
/*    */   {
/* 67 */     return this.delegate.getSslContextFactory();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isRunning()
/*    */   {
/* 73 */     return this.delegate.isRunning();
/*    */   }
/*    */   
/*    */ 
/*    */   public void onSessionOpened(WebSocketSession session)
/*    */   {
/* 79 */     this.delegate.onSessionOpened(session);
/*    */   }
/*    */   
/*    */ 
/*    */   public void onSessionClosed(WebSocketSession session)
/*    */   {
/* 85 */     this.delegate.onSessionClosed(session);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\scopes\DelegatedContainerScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */