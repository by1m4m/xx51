/*    */ package org.eclipse.jetty.websocket.client;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.eclipse.jetty.client.HttpClient;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ public final class HttpClientProvider
/*    */ {
/*    */   public static HttpClient get(WebSocketContainerScope scope)
/*    */   {
/*    */     try
/*    */     {
/* 33 */       if (Class.forName("org.eclipse.jetty.xml.XmlConfiguration") != null)
/*    */       {
/* 35 */         Class<?> xmlClazz = Class.forName("org.eclipse.jetty.websocket.client.XmlBasedHttpClientProvider");
/* 36 */         Method getMethod = xmlClazz.getMethod("get", new Class[] { WebSocketContainerScope.class });
/* 37 */         Object ret = getMethod.invoke(null, new Object[] { scope });
/* 38 */         if ((ret != null) && ((ret instanceof HttpClient)))
/*    */         {
/* 40 */           return (HttpClient)ret;
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Throwable ignore)
/*    */     {
/* 46 */       Log.getLogger(HttpClientProvider.class).ignore(ignore);
/*    */     }
/*    */     
/* 49 */     return DefaultHttpClientProvider.newHttpClient(scope);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\HttpClientProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */