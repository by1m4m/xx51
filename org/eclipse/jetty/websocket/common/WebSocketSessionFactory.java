/*    */ package org.eclipse.jetty.websocket.common;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.eclipse.jetty.websocket.common.events.EventDriver;
/*    */ import org.eclipse.jetty.websocket.common.events.JettyAnnotatedEventDriver;
/*    */ import org.eclipse.jetty.websocket.common.events.JettyListenerEventDriver;
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
/*    */ 
/*    */ 
/*    */ public class WebSocketSessionFactory
/*    */   implements SessionFactory
/*    */ {
/*    */   private final WebSocketContainerScope containerScope;
/*    */   
/*    */   public WebSocketSessionFactory(WebSocketContainerScope containerScope)
/*    */   {
/* 37 */     this.containerScope = containerScope;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supports(EventDriver websocket)
/*    */   {
/* 43 */     return ((websocket instanceof JettyAnnotatedEventDriver)) || ((websocket instanceof JettyListenerEventDriver));
/*    */   }
/*    */   
/*    */ 
/*    */   public WebSocketSession createSession(URI requestURI, EventDriver websocket, LogicalConnection connection)
/*    */   {
/* 49 */     return new WebSocketSession(this.containerScope, requestURI, websocket, connection);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\WebSocketSessionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */