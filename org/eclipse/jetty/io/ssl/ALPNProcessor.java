/*    */ package org.eclipse.jetty.io.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import org.eclipse.jetty.io.Connection;
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
/*    */ public abstract interface ALPNProcessor
/*    */ {
/*    */   public void init() {}
/*    */   
/*    */   public boolean appliesTo(SSLEngine sslEngine)
/*    */   {
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   public void configure(SSLEngine sslEngine, Connection connection) {}
/*    */   
/*    */   public static abstract interface Client
/*    */     extends ALPNProcessor
/*    */   {}
/*    */   
/*    */   public static abstract interface Server
/*    */     extends ALPNProcessor
/*    */   {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ssl\ALPNProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */