/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*    */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ @ManagedObject
/*    */ public abstract class AbstractHttpClientTransport
/*    */   extends ContainerLifeCycle
/*    */   implements HttpClientTransport
/*    */ {
/* 29 */   protected static final Logger LOG = Log.getLogger(HttpClientTransport.class);
/*    */   
/*    */   private HttpClient client;
/*    */   private ConnectionPool.Factory factory;
/*    */   
/*    */   protected HttpClient getHttpClient()
/*    */   {
/* 36 */     return this.client;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setHttpClient(HttpClient client)
/*    */   {
/* 42 */     this.client = client;
/*    */   }
/*    */   
/*    */ 
/*    */   public ConnectionPool.Factory getConnectionPoolFactory()
/*    */   {
/* 48 */     return this.factory;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setConnectionPoolFactory(ConnectionPool.Factory factory)
/*    */   {
/* 54 */     this.factory = factory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\AbstractHttpClientTransport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */