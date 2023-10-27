/*    */ package org.eclipse.jetty.websocket.common.events;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*    */ import org.eclipse.jetty.websocket.api.annotations.WebSocket;
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
/*    */ public class JettyAnnotatedImpl
/*    */   implements EventDriverImpl
/*    */ {
/* 28 */   private ConcurrentHashMap<Class<?>, JettyAnnotatedMetadata> cache = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */   public EventDriver create(Object websocket, WebSocketPolicy policy)
/*    */   {
/* 33 */     Class<?> websocketClass = websocket.getClass();
/* 34 */     synchronized (this)
/*    */     {
/* 36 */       JettyAnnotatedMetadata metadata = (JettyAnnotatedMetadata)this.cache.get(websocketClass);
/* 37 */       if (metadata == null)
/*    */       {
/* 39 */         JettyAnnotatedScanner scanner = new JettyAnnotatedScanner();
/* 40 */         metadata = scanner.scan(websocketClass);
/* 41 */         this.cache.put(websocketClass, metadata);
/*    */       }
/* 43 */       return new JettyAnnotatedEventDriver(policy, websocket, metadata);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String describeRule()
/*    */   {
/* 50 */     return "class is annotated with @" + WebSocket.class.getName();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supports(Object websocket)
/*    */   {
/* 56 */     WebSocket anno = (WebSocket)websocket.getClass().getAnnotation(WebSocket.class);
/* 57 */     return anno != null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     return String.format("%s [cache.count=%d]", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.cache.size()) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyAnnotatedImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */