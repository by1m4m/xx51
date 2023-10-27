/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.client.methods.HttpRequestBase;
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
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ class HttpRedirect
/*    */   extends HttpRequestBase
/*    */ {
/*    */   private String method;
/*    */   
/*    */   public HttpRedirect(String method, URI uri)
/*    */   {
/* 50 */     if (method.equalsIgnoreCase("HEAD")) {
/* 51 */       this.method = "HEAD";
/*    */     } else {
/* 53 */       this.method = "GET";
/*    */     }
/* 55 */     setURI(uri);
/*    */   }
/*    */   
/*    */   public String getMethod()
/*    */   {
/* 60 */     return this.method;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\client\HttpRedirect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */