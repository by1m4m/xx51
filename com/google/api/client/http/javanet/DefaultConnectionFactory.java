/*    */ package com.google.api.client.http.javanet;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultConnectionFactory
/*    */   implements ConnectionFactory
/*    */ {
/*    */   private final Proxy proxy;
/*    */   
/*    */   public DefaultConnectionFactory()
/*    */   {
/* 17 */     this(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultConnectionFactory(Proxy proxy)
/*    */   {
/* 26 */     this.proxy = proxy;
/*    */   }
/*    */   
/*    */   public HttpURLConnection openConnection(URL url) throws IOException
/*    */   {
/* 31 */     return (HttpURLConnection)(this.proxy == null ? url.openConnection() : url.openConnection(this.proxy));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\javanet\DefaultConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */