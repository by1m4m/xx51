/*    */ package org.eclipse.jetty.websocket.api;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebSocketAdapter
/*    */   implements WebSocketListener
/*    */ {
/*    */   private volatile Session session;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private RemoteEndpoint remote;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RemoteEndpoint getRemote()
/*    */   {
/* 33 */     return this.remote;
/*    */   }
/*    */   
/*    */   public Session getSession()
/*    */   {
/* 38 */     return this.session;
/*    */   }
/*    */   
/*    */   public boolean isConnected()
/*    */   {
/* 43 */     Session sess = this.session;
/* 44 */     return (sess != null) && (sess.isOpen());
/*    */   }
/*    */   
/*    */   public boolean isNotConnected()
/*    */   {
/* 49 */     Session sess = this.session;
/* 50 */     return (sess == null) || (!sess.isOpen());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onWebSocketBinary(byte[] payload, int offset, int len) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void onWebSocketClose(int statusCode, String reason)
/*    */   {
/* 62 */     this.session = null;
/* 63 */     this.remote = null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void onWebSocketConnect(Session sess)
/*    */   {
/* 69 */     this.session = sess;
/* 70 */     this.remote = sess.getRemote();
/*    */   }
/*    */   
/*    */   public void onWebSocketError(Throwable cause) {}
/*    */   
/*    */   public void onWebSocketText(String message) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */