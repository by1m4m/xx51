/*    */ package org.eclipse.jetty.websocket.client;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.eclipse.jetty.client.HttpClient;
/*    */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*    */ import org.eclipse.jetty.util.thread.QueuedThreadPool;
/*    */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
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
/*    */ 
/*    */ class DefaultHttpClientProvider
/*    */ {
/*    */   public static HttpClient newHttpClient(WebSocketContainerScope scope)
/*    */   {
/* 32 */     SslContextFactory sslContextFactory = null;
/* 33 */     Executor executor = null;
/*    */     
/* 35 */     if (scope != null)
/*    */     {
/* 37 */       sslContextFactory = scope.getSslContextFactory();
/* 38 */       executor = scope.getExecutor();
/*    */     }
/*    */     
/* 41 */     if (sslContextFactory == null)
/*    */     {
/* 43 */       sslContextFactory = new SslContextFactory();
/*    */     }
/*    */     
/* 46 */     HttpClient client = new HttpClient(sslContextFactory);
/* 47 */     if (executor == null)
/*    */     {
/* 49 */       QueuedThreadPool threadPool = new QueuedThreadPool();
/* 50 */       String name = "WebSocketClient@" + client.hashCode();
/* 51 */       threadPool.setName(name);
/* 52 */       threadPool.setDaemon(true);
/* 53 */       executor = threadPool;
/*    */     }
/* 55 */     client.setExecutor(executor);
/* 56 */     return client;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\DefaultHttpClientProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */