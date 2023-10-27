/*    */ package org.eclipse.jetty.websocket.common.events;
/*    */ 
/*    */ import org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketListener;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
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
/*    */ public class JettyListenerImpl
/*    */   implements EventDriverImpl
/*    */ {
/*    */   public EventDriver create(Object websocket, WebSocketPolicy policy)
/*    */   {
/* 30 */     WebSocketConnectionListener listener = (WebSocketConnectionListener)websocket;
/* 31 */     return new JettyListenerEventDriver(policy, listener);
/*    */   }
/*    */   
/*    */ 
/*    */   public String describeRule()
/*    */   {
/* 37 */     return "class implements " + WebSocketListener.class.getName();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supports(Object websocket)
/*    */   {
/* 43 */     return websocket instanceof WebSocketConnectionListener;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */