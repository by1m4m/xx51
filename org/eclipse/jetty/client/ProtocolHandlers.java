/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.eclipse.jetty.client.api.Request;
/*    */ import org.eclipse.jetty.client.api.Response;
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
/*    */ public class ProtocolHandlers
/*    */ {
/* 32 */   private final Map<String, ProtocolHandler> handlers = new LinkedHashMap();
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
/*    */   public ProtocolHandler put(ProtocolHandler protocolHandler)
/*    */   {
/* 50 */     return (ProtocolHandler)this.handlers.put(protocolHandler.getName(), protocolHandler);
/*    */   }
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
/*    */   public ProtocolHandler remove(String name)
/*    */   {
/* 64 */     return (ProtocolHandler)this.handlers.remove(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void clear()
/*    */   {
/* 72 */     this.handlers.clear();
/*    */   }
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
/*    */   public ProtocolHandler find(Request request, Response response)
/*    */   {
/* 87 */     for (ProtocolHandler handler : this.handlers.values())
/*    */     {
/* 89 */       if (handler.accept(request, response))
/* 90 */         return handler;
/*    */     }
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ProtocolHandlers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */