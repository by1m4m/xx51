/*    */ package org.eclipse.jetty.websocket.client;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import org.eclipse.jetty.client.HttpClient;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
/*    */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
/*    */ import org.eclipse.jetty.xml.XmlConfiguration;
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
/*    */ class XmlBasedHttpClientProvider
/*    */ {
/*    */   public static HttpClient get(WebSocketContainerScope scope)
/*    */   {
/* 33 */     URL resource = Thread.currentThread().getContextClassLoader().getResource("jetty-websocket-httpclient.xml");
/* 34 */     if (resource == null)
/*    */     {
/* 36 */       return null;
/*    */     }
/*    */     try {
/* 39 */       InputStream in = resource.openStream();Throwable localThrowable4 = null;
/*    */       try {
/* 41 */         XmlConfiguration configuration = new XmlConfiguration(in);
/* 42 */         return (HttpClient)configuration.configure();
/*    */       }
/*    */       catch (Throwable localThrowable2)
/*    */       {
/* 39 */         localThrowable4 = localThrowable2;throw localThrowable2;
/*    */       }
/*    */       finally
/*    */       {
/* 43 */         if (in != null) { if (localThrowable4 != null) try { in.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else { in.close();
/*    */           }
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 49 */       return null;
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/* 46 */       Log.getLogger(XmlBasedHttpClientProvider.class).warn("Unable to load: " + resource, t);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\XmlBasedHttpClientProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */