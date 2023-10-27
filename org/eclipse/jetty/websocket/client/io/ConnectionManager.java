/*    */ package org.eclipse.jetty.websocket.client.io;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.URI;
/*    */ import java.util.Locale;
/*    */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*    */ import org.eclipse.jetty.websocket.client.WebSocketClient;
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
/*    */ @Deprecated
/*    */ public class ConnectionManager
/*    */   extends ContainerLifeCycle
/*    */ {
/*    */   public static InetSocketAddress toSocketAddress(URI uri)
/*    */   {
/* 38 */     if (!uri.isAbsolute())
/*    */     {
/* 40 */       throw new IllegalArgumentException("Cannot get InetSocketAddress of non-absolute URIs");
/*    */     }
/*    */     
/* 43 */     int port = uri.getPort();
/* 44 */     String scheme = uri.getScheme().toLowerCase(Locale.ENGLISH);
/* 45 */     if ("ws".equals(scheme))
/*    */     {
/* 47 */       if (port == -1)
/*    */       {
/* 49 */         port = 80;
/*    */       }
/*    */     }
/* 52 */     else if ("wss".equals(scheme))
/*    */     {
/* 54 */       if (port == -1)
/*    */       {
/* 56 */         port = 443;
/*    */       }
/*    */       
/*    */     }
/*    */     else {
/* 61 */       throw new IllegalArgumentException("Only support ws:// and wss:// URIs");
/*    */     }
/*    */     
/* 64 */     return new InetSocketAddress(uri.getHost(), port);
/*    */   }
/*    */   
/*    */   public ConnectionManager(WebSocketClient client) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\io\ConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */