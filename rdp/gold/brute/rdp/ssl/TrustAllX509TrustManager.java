/*    */ package rdp.gold.brute.rdp.ssl;
/*    */ 
/*    */ import java.security.PublicKey;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.X509TrustManager;
/*    */ import org.apache.log4j.Logger;
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
/*    */ public class TrustAllX509TrustManager
/*    */   implements X509TrustManager
/*    */ {
/* 25 */   private static final Logger logger = Logger.getLogger(TrustAllX509TrustManager.class);
/*    */   private SSLState sslState;
/*    */   
/*    */   public TrustAllX509TrustManager(SSLState sslState) {
/* 29 */     this.sslState = sslState;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void checkClientTrusted(X509Certificate[] chain, String authType) {}
/*    */   
/*    */ 
/*    */   public void checkServerTrusted(X509Certificate[] chain, String authType)
/*    */   {
/* 39 */     if (this.sslState != null) {
/* 40 */       this.sslState.serverCertificateSubjectPublicKeyInfo = chain[0].getPublicKey().getEncoded();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public X509Certificate[] getAcceptedIssuers()
/*    */   {
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\ssl\TrustAllX509TrustManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */