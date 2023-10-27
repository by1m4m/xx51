/*     */ package com.google.api.client.http.javanet;
/*     */ 
/*     */ import com.google.api.client.http.HttpTransport;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.SecurityUtils;
/*     */ import com.google.api.client.util.SslUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.util.Arrays;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public final class NetHttpTransport
/*     */   extends HttpTransport
/*     */ {
/*  68 */   private static final String[] SUPPORTED_METHODS = { "DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE" };
/*     */   
/*     */   private final ConnectionFactory connectionFactory;
/*     */   private final SSLSocketFactory sslSocketFactory;
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   
/*     */   static
/*     */   {
/*  76 */     Arrays.sort(SUPPORTED_METHODS);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetHttpTransport()
/*     */   {
/*  96 */     this((ConnectionFactory)null, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NetHttpTransport(Proxy proxy, SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier)
/*     */   {
/* 108 */     this(new DefaultConnectionFactory(proxy), sslSocketFactory, hostnameVerifier);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NetHttpTransport(ConnectionFactory connectionFactory, SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier)
/*     */   {
/* 120 */     this.connectionFactory = (connectionFactory == null ? new DefaultConnectionFactory() : connectionFactory);
/*     */     
/* 122 */     this.sslSocketFactory = sslSocketFactory;
/* 123 */     this.hostnameVerifier = hostnameVerifier;
/*     */   }
/*     */   
/*     */   public boolean supportsMethod(String method)
/*     */   {
/* 128 */     return Arrays.binarySearch(SUPPORTED_METHODS, method) >= 0;
/*     */   }
/*     */   
/*     */   protected NetHttpRequest buildRequest(String method, String url) throws IOException
/*     */   {
/* 133 */     Preconditions.checkArgument(supportsMethod(method), "HTTP method %s not supported", new Object[] { method });
/*     */     
/* 135 */     URL connUrl = new URL(url);
/* 136 */     HttpURLConnection connection = this.connectionFactory.openConnection(connUrl);
/* 137 */     connection.setRequestMethod(method);
/*     */     
/* 139 */     if ((connection instanceof HttpsURLConnection)) {
/* 140 */       HttpsURLConnection secureConnection = (HttpsURLConnection)connection;
/* 141 */       if (this.hostnameVerifier != null) {
/* 142 */         secureConnection.setHostnameVerifier(this.hostnameVerifier);
/*     */       }
/* 144 */       if (this.sslSocketFactory != null) {
/* 145 */         secureConnection.setSSLSocketFactory(this.sslSocketFactory);
/*     */       }
/*     */     }
/* 148 */     return new NetHttpRequest(connection);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     private SSLSocketFactory sslSocketFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private HostnameVerifier hostnameVerifier;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Proxy proxy;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ConnectionFactory connectionFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setProxy(Proxy proxy)
/*     */     {
/* 195 */       this.proxy = proxy;
/* 196 */       return this;
/*     */     }
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
/*     */     public Builder setConnectionFactory(ConnectionFactory connectionFactory)
/*     */     {
/* 211 */       this.connectionFactory = connectionFactory;
/* 212 */       return this;
/*     */     }
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
/*     */     public Builder trustCertificatesFromJavaKeyStore(InputStream keyStoreStream, String storePass)
/*     */       throws GeneralSecurityException, IOException
/*     */     {
/* 233 */       KeyStore trustStore = SecurityUtils.getJavaKeyStore();
/* 234 */       SecurityUtils.loadKeyStore(trustStore, keyStoreStream, storePass);
/* 235 */       return trustCertificates(trustStore);
/*     */     }
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
/*     */     public Builder trustCertificatesFromStream(InputStream certificateStream)
/*     */       throws GeneralSecurityException, IOException
/*     */     {
/* 255 */       KeyStore trustStore = SecurityUtils.getJavaKeyStore();
/* 256 */       trustStore.load(null, null);
/* 257 */       SecurityUtils.loadKeyStoreFromCertificates(trustStore, SecurityUtils.getX509CertificateFactory(), certificateStream);
/*     */       
/* 259 */       return trustCertificates(trustStore);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder trustCertificates(KeyStore trustStore)
/*     */       throws GeneralSecurityException
/*     */     {
/* 270 */       SSLContext sslContext = SslUtils.getTlsSslContext();
/* 271 */       SslUtils.initSslContext(sslContext, trustStore, SslUtils.getPkixTrustManagerFactory());
/* 272 */       return setSslSocketFactory(sslContext.getSocketFactory());
/*     */     }
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
/*     */     @Beta
/*     */     public Builder doNotValidateCertificate()
/*     */       throws GeneralSecurityException
/*     */     {
/* 288 */       this.hostnameVerifier = SslUtils.trustAllHostnameVerifier();
/* 289 */       this.sslSocketFactory = SslUtils.trustAllSSLContext().getSocketFactory();
/* 290 */       return this;
/*     */     }
/*     */     
/*     */     public SSLSocketFactory getSslSocketFactory()
/*     */     {
/* 295 */       return this.sslSocketFactory;
/*     */     }
/*     */     
/*     */     public Builder setSslSocketFactory(SSLSocketFactory sslSocketFactory)
/*     */     {
/* 300 */       this.sslSocketFactory = sslSocketFactory;
/* 301 */       return this;
/*     */     }
/*     */     
/*     */     public HostnameVerifier getHostnameVerifier()
/*     */     {
/* 306 */       return this.hostnameVerifier;
/*     */     }
/*     */     
/*     */     public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier)
/*     */     {
/* 311 */       this.hostnameVerifier = hostnameVerifier;
/* 312 */       return this;
/*     */     }
/*     */     
/*     */     public NetHttpTransport build()
/*     */     {
/* 317 */       return this.proxy == null ? new NetHttpTransport(this.connectionFactory, this.sslSocketFactory, this.hostnameVerifier) : new NetHttpTransport(this.proxy, this.sslSocketFactory, this.hostnameVerifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\javanet\NetHttpTransport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */