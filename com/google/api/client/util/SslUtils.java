/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ public final class SslUtils
/*     */ {
/*     */   public static SSLContext getSslContext()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  45 */     return SSLContext.getInstance("SSL");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SSLContext getTlsSslContext()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  54 */     return SSLContext.getInstance("TLS");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TrustManagerFactory getDefaultTrustManagerFactory()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  64 */     return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TrustManagerFactory getPkixTrustManagerFactory()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  73 */     return TrustManagerFactory.getInstance("PKIX");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static KeyManagerFactory getDefaultKeyManagerFactory()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  82 */     return KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static KeyManagerFactory getPkixKeyManagerFactory()
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  91 */     return KeyManagerFactory.getInstance("PKIX");
/*     */   }
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
/*     */   public static SSLContext initSslContext(SSLContext sslContext, KeyStore trustStore, TrustManagerFactory trustManagerFactory)
/*     */     throws GeneralSecurityException
/*     */   {
/* 109 */     trustManagerFactory.init(trustStore);
/* 110 */     sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
/* 111 */     return sslContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static SSLContext trustAllSSLContext()
/*     */     throws GeneralSecurityException
/*     */   {
/* 125 */     TrustManager[] trustAllCerts = { new X509TrustManager()
/*     */     {
/*     */       public void checkClientTrusted(X509Certificate[] chain, String authType)
/*     */         throws CertificateException
/*     */       {}
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
/*     */       {}
/*     */       
/*     */       public X509Certificate[] getAcceptedIssuers()
/*     */       {
/* 136 */         return null;
/*     */       }
/* 138 */     } };
/* 139 */     SSLContext context = getTlsSslContext();
/* 140 */     context.init(null, trustAllCerts, null);
/* 141 */     return context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static HostnameVerifier trustAllHostnameVerifier()
/*     */   {
/* 155 */     new HostnameVerifier()
/*     */     {
/*     */       public boolean verify(String arg0, SSLSession arg1) {
/* 158 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\SslUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */