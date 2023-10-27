/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.security.Principal;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.auth.Credentials;
/*    */ import org.apache.http.client.UserTokenHandler;
/*    */ import org.apache.http.conn.ManagedClientConnection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DefaultUserTokenHandler
/*    */   implements UserTokenHandler
/*    */ {
/*    */   public Object getUserToken(HttpContext context)
/*    */   {
/* 63 */     Principal userPrincipal = null;
/*    */     
/* 65 */     AuthState targetAuthState = (AuthState)context.getAttribute("http.auth.target-scope");
/*    */     
/* 67 */     if (targetAuthState != null) {
/* 68 */       userPrincipal = getAuthPrincipal(targetAuthState);
/* 69 */       if (userPrincipal == null) {
/* 70 */         AuthState proxyAuthState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/*    */         
/* 72 */         userPrincipal = getAuthPrincipal(proxyAuthState);
/*    */       }
/*    */     }
/*    */     
/* 76 */     if (userPrincipal == null) {
/* 77 */       ManagedClientConnection conn = (ManagedClientConnection)context.getAttribute("http.connection");
/*    */       
/* 79 */       if (conn.isOpen()) {
/* 80 */         SSLSession sslsession = conn.getSSLSession();
/* 81 */         if (sslsession != null) {
/* 82 */           userPrincipal = sslsession.getLocalPrincipal();
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 87 */     return userPrincipal;
/*    */   }
/*    */   
/*    */   private static Principal getAuthPrincipal(AuthState authState) {
/* 91 */     AuthScheme scheme = authState.getAuthScheme();
/* 92 */     if ((scheme != null) && (scheme.isComplete()) && (scheme.isConnectionBased())) {
/* 93 */       Credentials creds = authState.getCredentials();
/* 94 */       if (creds != null) {
/* 95 */         return creds.getUserPrincipal();
/*    */       }
/*    */     }
/* 98 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\client\DefaultUserTokenHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */