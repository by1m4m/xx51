/*     */ package com.google.api.client.http.apache;
/*     */ 
/*     */ import com.google.api.client.http.HttpTransport;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.SecurityUtils;
/*     */ import com.google.api.client.util.SslUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.ProxySelector;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpDelete;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.params.ConnManagerParams;
/*     */ import org.apache.http.conn.params.ConnPerRouteBean;
/*     */ import org.apache.http.conn.params.ConnRouteParams;
/*     */ import org.apache.http.conn.scheme.PlainSocketFactory;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.conn.ssl.SSLSocketFactory;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
/*     */ import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
/*     */ import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ApacheHttpTransport
/*     */   extends HttpTransport
/*     */ {
/*     */   private final HttpClient httpClient;
/*     */   
/*     */   public ApacheHttpTransport()
/*     */   {
/* 100 */     this(newDefaultHttpClient());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApacheHttpTransport(HttpClient httpClient)
/*     */   {
/* 128 */     this.httpClient = httpClient;
/* 129 */     HttpParams params = httpClient.getParams();
/* 130 */     HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
/* 131 */     params.setBooleanParameter("http.protocol.handle-redirects", false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DefaultHttpClient newDefaultHttpClient()
/*     */   {
/* 157 */     return newDefaultHttpClient(SSLSocketFactory.getSocketFactory(), newDefaultHttpParams(), ProxySelector.getDefault());
/*     */   }
/*     */   
/*     */ 
/*     */   static HttpParams newDefaultHttpParams()
/*     */   {
/* 163 */     HttpParams params = new BasicHttpParams();
/*     */     
/*     */ 
/* 166 */     HttpConnectionParams.setStaleCheckingEnabled(params, false);
/* 167 */     HttpConnectionParams.setSocketBufferSize(params, 8192);
/* 168 */     ConnManagerParams.setMaxTotalConnections(params, 200);
/* 169 */     ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(20));
/* 170 */     return params;
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
/*     */   static DefaultHttpClient newDefaultHttpClient(SSLSocketFactory socketFactory, HttpParams params, ProxySelector proxySelector)
/*     */   {
/* 186 */     SchemeRegistry registry = new SchemeRegistry();
/* 187 */     registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
/* 188 */     registry.register(new Scheme("https", socketFactory, 443));
/* 189 */     ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
/* 190 */     DefaultHttpClient defaultHttpClient = new DefaultHttpClient(connectionManager, params);
/* 191 */     defaultHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
/* 192 */     if (proxySelector != null) {
/* 193 */       defaultHttpClient.setRoutePlanner(new ProxySelectorRoutePlanner(registry, proxySelector));
/*     */     }
/* 195 */     return defaultHttpClient;
/*     */   }
/*     */   
/*     */   public boolean supportsMethod(String method)
/*     */   {
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   protected ApacheHttpRequest buildRequest(String method, String url) {
/*     */     HttpRequestBase requestBase;
/*     */     HttpRequestBase requestBase;
/* 206 */     if (method.equals("DELETE")) {
/* 207 */       requestBase = new HttpDelete(url); } else { HttpRequestBase requestBase;
/* 208 */       if (method.equals("GET")) {
/* 209 */         requestBase = new HttpGet(url); } else { HttpRequestBase requestBase;
/* 210 */         if (method.equals("HEAD")) {
/* 211 */           requestBase = new HttpHead(url); } else { HttpRequestBase requestBase;
/* 212 */           if (method.equals("POST")) {
/* 213 */             requestBase = new HttpPost(url); } else { HttpRequestBase requestBase;
/* 214 */             if (method.equals("PUT")) {
/* 215 */               requestBase = new HttpPut(url); } else { HttpRequestBase requestBase;
/* 216 */               if (method.equals("TRACE")) {
/* 217 */                 requestBase = new HttpTrace(url); } else { HttpRequestBase requestBase;
/* 218 */                 if (method.equals("OPTIONS")) {
/* 219 */                   requestBase = new HttpOptions(url);
/*     */                 } else
/* 221 */                   requestBase = new HttpExtensionMethod(method, url);
/*     */               } } } } } }
/* 223 */     return new ApacheHttpRequest(this.httpClient, requestBase);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 234 */     this.httpClient.getConnectionManager().shutdown();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpClient getHttpClient()
/*     */   {
/* 243 */     return this.httpClient;
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
/*     */   public static final class Builder
/*     */   {
/* 258 */     private SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
/*     */     
/*     */ 
/* 261 */     private HttpParams params = ApacheHttpTransport.newDefaultHttpParams();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 267 */     private ProxySelector proxySelector = ProxySelector.getDefault();
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
/*     */     public Builder setProxy(HttpHost proxy)
/*     */     {
/* 288 */       ConnRouteParams.setDefaultProxy(this.params, proxy);
/* 289 */       if (proxy != null) {
/* 290 */         this.proxySelector = null;
/*     */       }
/* 292 */       return this;
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
/*     */     public Builder setProxySelector(ProxySelector proxySelector)
/*     */     {
/* 306 */       this.proxySelector = proxySelector;
/* 307 */       if (proxySelector != null) {
/* 308 */         ConnRouteParams.setDefaultProxy(this.params, null);
/*     */       }
/* 310 */       return this;
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
/*     */     public Builder trustCertificatesFromJavaKeyStore(InputStream keyStoreStream, String storePass)
/*     */       throws GeneralSecurityException, IOException
/*     */     {
/* 330 */       KeyStore trustStore = SecurityUtils.getJavaKeyStore();
/* 331 */       SecurityUtils.loadKeyStore(trustStore, keyStoreStream, storePass);
/* 332 */       return trustCertificates(trustStore);
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
/* 352 */       KeyStore trustStore = SecurityUtils.getJavaKeyStore();
/* 353 */       trustStore.load(null, null);
/* 354 */       SecurityUtils.loadKeyStoreFromCertificates(trustStore, SecurityUtils.getX509CertificateFactory(), certificateStream);
/*     */       
/* 356 */       return trustCertificates(trustStore);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder trustCertificates(KeyStore trustStore)
/*     */       throws GeneralSecurityException
/*     */     {
/* 368 */       SSLContext sslContext = SslUtils.getTlsSslContext();
/* 369 */       SslUtils.initSslContext(sslContext, trustStore, SslUtils.getPkixTrustManagerFactory());
/* 370 */       return setSocketFactory(new SSLSocketFactoryExtension(sslContext));
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
/* 386 */       this.socketFactory = new SSLSocketFactoryExtension(SslUtils.trustAllSSLContext());
/* 387 */       this.socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
/* 388 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSocketFactory(SSLSocketFactory socketFactory)
/*     */     {
/* 393 */       this.socketFactory = ((SSLSocketFactory)Preconditions.checkNotNull(socketFactory));
/* 394 */       return this;
/*     */     }
/*     */     
/*     */     public SSLSocketFactory getSSLSocketFactory()
/*     */     {
/* 399 */       return this.socketFactory;
/*     */     }
/*     */     
/*     */     public HttpParams getHttpParams()
/*     */     {
/* 404 */       return this.params;
/*     */     }
/*     */     
/*     */     public ApacheHttpTransport build()
/*     */     {
/* 409 */       return new ApacheHttpTransport(ApacheHttpTransport.newDefaultHttpClient(this.socketFactory, this.params, this.proxySelector));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\ApacheHttpTransport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */