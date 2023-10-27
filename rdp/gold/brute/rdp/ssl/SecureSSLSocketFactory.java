/*     */ package rdp.gold.brute.rdp.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import org.apache.log4j.Logger;
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
/*     */ public class SecureSSLSocketFactory
/*     */   extends SSLSocketFactory
/*     */ {
/*  35 */   public static final Logger s_logger = Logger.getLogger(SecureSSLSocketFactory.class);
/*     */   private SSLContext _sslContext;
/*     */   
/*     */   public SecureSSLSocketFactory() throws NoSuchAlgorithmException {
/*  39 */     this._sslContext = SSLUtils.getSSLContext();
/*     */   }
/*     */   
/*     */   public SecureSSLSocketFactory(SSLContext sslContext) throws NoSuchAlgorithmException {
/*  43 */     if (sslContext != null) {
/*  44 */       this._sslContext = sslContext;
/*     */     } else {
/*  46 */       this._sslContext = SSLUtils.getSSLContext();
/*     */     }
/*     */   }
/*     */   
/*     */   public SecureSSLSocketFactory(KeyManager[] km, TrustManager[] tm, SecureRandom random) throws NoSuchAlgorithmException, KeyManagementException, IOException {
/*  51 */     this._sslContext = SSLUtils.getSSLContext();
/*  52 */     this._sslContext.init(km, tm, random);
/*     */   }
/*     */   
/*     */   public String[] getDefaultCipherSuites()
/*     */   {
/*  57 */     return getSupportedCipherSuites();
/*     */   }
/*     */   
/*     */   public String[] getSupportedCipherSuites()
/*     */   {
/*  62 */     String[] ciphers = null;
/*     */     try {
/*  64 */       ciphers = SSLUtils.getSupportedCiphers();
/*     */     } catch (NoSuchAlgorithmException e) {
/*  66 */       s_logger.error("SecureSSLSocketFactory::getDefaultCipherSuites found no cipher suites");
/*     */     }
/*  68 */     return ciphers;
/*     */   }
/*     */   
/*     */   public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
/*     */   {
/*  73 */     SSLSocketFactory factory = this._sslContext.getSocketFactory();
/*  74 */     Socket socket = factory.createSocket(s, host, port, autoClose);
/*  75 */     if ((socket instanceof SSLSocket)) {
/*  76 */       ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
/*     */     }
/*  78 */     return socket;
/*     */   }
/*     */   
/*     */   public Socket createSocket(String host, int port) throws IOException, UnknownHostException
/*     */   {
/*  83 */     SSLSocketFactory factory = this._sslContext.getSocketFactory();
/*  84 */     Socket socket = factory.createSocket(host, port);
/*  85 */     if ((socket instanceof SSLSocket)) {
/*  86 */       ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
/*     */     }
/*  88 */     return socket;
/*     */   }
/*     */   
/*     */   public Socket createSocket(String host, int port, InetAddress inetAddress, int localPort) throws IOException, UnknownHostException
/*     */   {
/*  93 */     SSLSocketFactory factory = this._sslContext.getSocketFactory();
/*  94 */     Socket socket = factory.createSocket(host, port, inetAddress, localPort);
/*  95 */     if ((socket instanceof SSLSocket)) {
/*  96 */       ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
/*     */     }
/*  98 */     return socket;
/*     */   }
/*     */   
/*     */   public Socket createSocket(InetAddress inetAddress, int localPort) throws IOException
/*     */   {
/* 103 */     SSLSocketFactory factory = this._sslContext.getSocketFactory();
/* 104 */     Socket socket = factory.createSocket(inetAddress, localPort);
/* 105 */     if ((socket instanceof SSLSocket)) {
/* 106 */       ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
/*     */     }
/* 108 */     return socket;
/*     */   }
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
/*     */   {
/* 113 */     SSLSocketFactory factory = this._sslContext.getSocketFactory();
/* 114 */     Socket socket = factory.createSocket(address, port, localAddress, localPort);
/* 115 */     if ((socket instanceof SSLSocket)) {
/* 116 */       ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
/*     */     }
/* 118 */     return socket;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\ssl\SecureSSLSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */