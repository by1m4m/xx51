/*    */ package org.eclipse.jetty.io;
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
/*    */ public abstract class NegotiatingClientConnectionFactory
/*    */   implements ClientConnectionFactory
/*    */ {
/*    */   private final ClientConnectionFactory connectionFactory;
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
/*    */   protected NegotiatingClientConnectionFactory(ClientConnectionFactory connectionFactory)
/*    */   {
/* 28 */     this.connectionFactory = connectionFactory;
/*    */   }
/*    */   
/*    */   public ClientConnectionFactory getClientConnectionFactory()
/*    */   {
/* 33 */     return this.connectionFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\NegotiatingClientConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */