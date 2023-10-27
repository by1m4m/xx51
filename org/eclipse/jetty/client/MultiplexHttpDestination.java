/*    */ package org.eclipse.jetty.client;
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
/*    */ public abstract class MultiplexHttpDestination
/*    */   extends HttpDestination
/*    */ {
/*    */   protected MultiplexHttpDestination(HttpClient client, Origin origin)
/*    */   {
/* 25 */     super(client, origin);
/*    */   }
/*    */   
/*    */   public int getMaxRequestsPerConnection()
/*    */   {
/* 30 */     ConnectionPool connectionPool = getConnectionPool();
/* 31 */     if ((connectionPool instanceof MultiplexConnectionPool))
/* 32 */       return ((MultiplexConnectionPool)connectionPool).getMaxMultiplex();
/* 33 */     return 1;
/*    */   }
/*    */   
/*    */   public void setMaxRequestsPerConnection(int maxRequestsPerConnection)
/*    */   {
/* 38 */     ConnectionPool connectionPool = getConnectionPool();
/* 39 */     if ((connectionPool instanceof MultiplexConnectionPool)) {
/* 40 */       ((MultiplexConnectionPool)connectionPool).setMaxMultiplex(maxRequestsPerConnection);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\MultiplexHttpDestination.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */