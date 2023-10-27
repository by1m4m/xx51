/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class RequestTargetAuthentication
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  55 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void process(HttpRequest request, HttpContext context)
/*     */     throws HttpException, IOException
/*     */   {
/*  63 */     if (request == null) {
/*  64 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  66 */     if (context == null) {
/*  67 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  70 */     String method = request.getRequestLine().getMethod();
/*  71 */     if (method.equalsIgnoreCase("CONNECT")) {
/*  72 */       return;
/*     */     }
/*     */     
/*  75 */     if (request.containsHeader("Authorization")) {
/*  76 */       return;
/*     */     }
/*     */     
/*     */ 
/*  80 */     AuthState authState = (AuthState)context.getAttribute("http.auth.target-scope");
/*     */     
/*  82 */     if (authState == null) {
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     AuthScheme authScheme = authState.getAuthScheme();
/*  87 */     if (authScheme == null) {
/*  88 */       return;
/*     */     }
/*     */     
/*  91 */     Credentials creds = authState.getCredentials();
/*  92 */     if (creds == null) {
/*  93 */       this.log.debug("User credentials not available");
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     if ((authState.getAuthScope() != null) || (!authScheme.isConnectionBased())) {
/*     */       try {
/*  99 */         request.addHeader(authScheme.authenticate(creds, request));
/*     */       } catch (AuthenticationException ex) {
/* 101 */         if (this.log.isErrorEnabled()) {
/* 102 */           this.log.error("Authentication error: " + ex.getMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\protocol\RequestTargetAuthentication.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */