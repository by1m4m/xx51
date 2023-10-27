/*    */ package org.eclipse.jetty.io.ssl;
/*    */ 
/*    */ import java.util.EventListener;
/*    */ import java.util.EventObject;
/*    */ import javax.net.ssl.SSLEngine;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface SslHandshakeListener
/*    */   extends EventListener
/*    */ {
/*    */   public void handshakeSucceeded(Event event) {}
/*    */   
/*    */   public void handshakeFailed(Event event, Throwable failure) {}
/*    */   
/*    */   public static class Event
/*    */     extends EventObject
/*    */   {
/*    */     public Event(Object source)
/*    */     {
/* 60 */       super();
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public SSLEngine getSSLEngine()
/*    */     {
/* 68 */       return (SSLEngine)getSource();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ssl\SslHandshakeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */