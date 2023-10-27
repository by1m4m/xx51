/*    */ package rdp.gold.brute.rdp.ssl;
/*    */ 
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.NoSuchProviderException;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.SSLSocketFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SSLUtils
/*    */ {
/* 32 */   public static final Logger s_logger = Logger.getLogger(SSLUtils.class);
/*    */   
/*    */   public static String[] getSupportedProtocols(String[] protocols) {
/* 35 */     Set<String> set = new HashSet();
/* 36 */     for (String s : protocols) {
/* 37 */       if ((!s.equals("SSLv3")) && (!s.equals("SSLv2Hello")))
/*    */       {
/*    */ 
/* 40 */         set.add(s); }
/*    */     }
/* 42 */     return (String[])set.toArray(new String[set.size()]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static String[] getRecommendedProtocols()
/*    */   {
/* 49 */     return new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static String[] getRecommendedCiphers()
/*    */   {
/* 56 */     return new String[] { "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384" };
/*    */   }
/*    */   
/*    */ 
/*    */   public static String[] getSupportedCiphers()
/*    */     throws NoSuchAlgorithmException
/*    */   {
/* 63 */     String[] availableCiphers = getSSLContext().getSocketFactory().getSupportedCipherSuites();
/* 64 */     Arrays.sort(availableCiphers);
/* 65 */     return availableCiphers;
/*    */   }
/*    */   
/*    */   public static SSLContext getSSLContext() throws NoSuchAlgorithmException {
/* 69 */     return SSLContext.getInstance("TLSv1.2");
/*    */   }
/*    */   
/*    */   public static SSLContext getSSLContext(String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
/* 73 */     return SSLContext.getInstance("TLSv1.2", provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\ssl\SSLUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */