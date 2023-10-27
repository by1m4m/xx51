/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.StatusLine;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.MalformedChallengeException;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DefaultTargetAuthenticationHandler
/*    */   extends AbstractAuthenticationHandler
/*    */ {
/*    */   public boolean isAuthenticationRequested(HttpResponse response, HttpContext context)
/*    */   {
/* 58 */     if (response == null) {
/* 59 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 61 */     int status = response.getStatusLine().getStatusCode();
/* 62 */     return status == 401;
/*    */   }
/*    */   
/*    */   public Map<String, Header> getChallenges(HttpResponse response, HttpContext context)
/*    */     throws MalformedChallengeException
/*    */   {
/* 68 */     if (response == null) {
/* 69 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 71 */     Header[] headers = response.getHeaders("WWW-Authenticate");
/* 72 */     return parseChallenges(headers);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\client\DefaultTargetAuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */