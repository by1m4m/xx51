/*    */ package com.google.api.client.testing.http.apache;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.io.IOException;
/*    */ import org.apache.http.ConnectionReuseStrategy;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.client.AuthenticationHandler;
/*    */ import org.apache.http.client.HttpRequestRetryHandler;
/*    */ import org.apache.http.client.RedirectHandler;
/*    */ import org.apache.http.client.RequestDirector;
/*    */ import org.apache.http.client.UserTokenHandler;
/*    */ import org.apache.http.conn.ClientConnectionManager;
/*    */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*    */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*    */ import org.apache.http.impl.client.DefaultHttpClient;
/*    */ import org.apache.http.message.BasicHttpResponse;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ import org.apache.http.protocol.HttpProcessor;
/*    */ import org.apache.http.protocol.HttpRequestExecutor;
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
/*    */ @Beta
/*    */ public class MockHttpClient
/*    */   extends DefaultHttpClient
/*    */ {
/*    */   int responseCode;
/*    */   
/*    */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler stateHandler, HttpParams params)
/*    */   {
/* 68 */     new RequestDirector()
/*    */     {
/*    */       @Beta
/*    */       public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 72 */         return new BasicHttpResponse(HttpVersion.HTTP_1_1, MockHttpClient.this.responseCode, null);
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   public final int getResponseCode()
/*    */   {
/* 79 */     return this.responseCode;
/*    */   }
/*    */   
/*    */   public MockHttpClient setResponseCode(int responseCode)
/*    */   {
/* 84 */     Preconditions.checkArgument(responseCode >= 0);
/* 85 */     this.responseCode = responseCode;
/* 86 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\apache\MockHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */