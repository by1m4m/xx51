/*    */ package org.apache.commons.net.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.GeneralSecurityException;
/*    */ import javax.net.ssl.KeyManager;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.TrustManager;
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
/*    */ public class SSLContextUtils
/*    */ {
/*    */   public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager)
/*    */     throws IOException
/*    */   {
/* 47 */     return createSSLContext(protocol, new KeyManager[] { keyManager == null ? null : keyManager }, new TrustManager[] { trustManager == null ? null : trustManager });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers)
/*    */     throws IOException
/*    */   {
/*    */     SSLContext ctx;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 64 */       ctx = SSLContext.getInstance(protocol);
/* 65 */       ctx.init(keyManagers, trustManagers, null);
/*    */     } catch (GeneralSecurityException e) {
/* 67 */       IOException ioe = new IOException("Could not initialize SSL context");
/* 68 */       ioe.initCause(e);
/* 69 */       throw ioe;
/*    */     }
/* 71 */     return ctx;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\util\SSLContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */