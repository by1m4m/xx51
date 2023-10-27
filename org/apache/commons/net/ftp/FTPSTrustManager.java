/*    */ package org.apache.commons.net.ftp;
/*    */ 
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.X509TrustManager;
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
/*    */ @Deprecated
/*    */ public class FTPSTrustManager
/*    */   implements X509TrustManager
/*    */ {
/* 35 */   private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void checkClientTrusted(X509Certificate[] certificates, String authType) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void checkServerTrusted(X509Certificate[] certificates, String authType)
/*    */     throws CertificateException
/*    */   {
/* 49 */     for (X509Certificate certificate : certificates)
/*    */     {
/* 51 */       certificate.checkValidity();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public X509Certificate[] getAcceptedIssuers()
/*    */   {
/* 58 */     return EMPTY_X509CERTIFICATE_ARRAY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\FTPSTrustManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */