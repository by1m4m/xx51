/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ public final class SslContextBuilder
/*     */ {
/*     */   private final boolean forServer;
/*     */   private SslProvider provider;
/*     */   private Provider sslContextProvider;
/*     */   private X509Certificate[] trustCertCollection;
/*     */   private TrustManagerFactory trustManagerFactory;
/*     */   private X509Certificate[] keyCertChain;
/*     */   private PrivateKey key;
/*     */   private String keyPassword;
/*     */   private KeyManagerFactory keyManagerFactory;
/*     */   private Iterable<String> ciphers;
/*     */   
/*     */   public static SslContextBuilder forClient()
/*     */   {
/*  43 */     return new SslContextBuilder(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContextBuilder forServer(File keyCertChainFile, File keyFile)
/*     */   {
/*  54 */     return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream)
/*     */   {
/*  65 */     return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SslContextBuilder forServer(PrivateKey key, X509Certificate... keyCertChain)
/*     */   {
/*  76 */     return new SslContextBuilder(true).keyManager(key, keyCertChain);
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
/*     */   public static SslContextBuilder forServer(File keyCertChainFile, File keyFile, String keyPassword)
/*     */   {
/*  90 */     return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile, keyPassword);
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
/*     */   public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword)
/*     */   {
/* 104 */     return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream, keyPassword);
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
/*     */   public static SslContextBuilder forServer(PrivateKey key, String keyPassword, X509Certificate... keyCertChain)
/*     */   {
/* 118 */     return new SslContextBuilder(true).keyManager(key, keyPassword, keyCertChain);
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
/*     */   public static SslContextBuilder forServer(KeyManagerFactory keyManagerFactory)
/*     */   {
/* 131 */     return new SslContextBuilder(true).keyManager(keyManagerFactory);
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
/* 144 */   private CipherSuiteFilter cipherFilter = IdentityCipherSuiteFilter.INSTANCE;
/*     */   private ApplicationProtocolConfig apn;
/*     */   private long sessionCacheSize;
/*     */   private long sessionTimeout;
/* 148 */   private ClientAuth clientAuth = ClientAuth.NONE;
/*     */   private String[] protocols;
/*     */   private boolean startTls;
/*     */   private boolean enableOcsp;
/*     */   
/*     */   private SslContextBuilder(boolean forServer) {
/* 154 */     this.forServer = forServer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder sslProvider(SslProvider provider)
/*     */   {
/* 161 */     this.provider = provider;
/* 162 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder sslContextProvider(Provider sslContextProvider)
/*     */   {
/* 170 */     this.sslContextProvider = sslContextProvider;
/* 171 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder trustManager(File trustCertCollectionFile)
/*     */   {
/*     */     try
/*     */     {
/* 180 */       return trustManager(SslContext.toX509Certificates(trustCertCollectionFile));
/*     */     } catch (Exception e) {
/* 182 */       throw new IllegalArgumentException("File does not contain valid certificates: " + trustCertCollectionFile, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder trustManager(InputStream trustCertCollectionInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       return trustManager(SslContext.toX509Certificates(trustCertCollectionInputStream));
/*     */     } catch (Exception e) {
/* 195 */       throw new IllegalArgumentException("Input stream does not contain valid certificates.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder trustManager(X509Certificate... trustCertCollection)
/*     */   {
/* 203 */     this.trustCertCollection = (trustCertCollection != null ? (X509Certificate[])trustCertCollection.clone() : null);
/* 204 */     this.trustManagerFactory = null;
/* 205 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder trustManager(TrustManagerFactory trustManagerFactory)
/*     */   {
/* 212 */     this.trustCertCollection = null;
/* 213 */     this.trustManagerFactory = trustManagerFactory;
/* 214 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder keyManager(File keyCertChainFile, File keyFile)
/*     */   {
/* 225 */     return keyManager(keyCertChainFile, keyFile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream)
/*     */   {
/* 236 */     return keyManager(keyCertChainInputStream, keyInputStream, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder keyManager(PrivateKey key, X509Certificate... keyCertChain)
/*     */   {
/* 247 */     return keyManager(key, null, keyCertChain);
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
/*     */   public SslContextBuilder keyManager(File keyCertChainFile, File keyFile, String keyPassword)
/*     */   {
/*     */     try
/*     */     {
/* 263 */       keyCertChain = SslContext.toX509Certificates(keyCertChainFile);
/*     */     } catch (Exception e) { X509Certificate[] keyCertChain;
/* 265 */       throw new IllegalArgumentException("File does not contain valid certificates: " + keyCertChainFile, e);
/*     */     }
/*     */     X509Certificate[] keyCertChain;
/* 268 */     try { key = SslContext.toPrivateKey(keyFile, keyPassword);
/*     */     } catch (Exception e) { PrivateKey key;
/* 270 */       throw new IllegalArgumentException("File does not contain valid private key: " + keyFile, e); }
/*     */     PrivateKey key;
/* 272 */     return keyManager(key, keyPassword, keyCertChain);
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
/*     */   public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword)
/*     */   {
/*     */     try
/*     */     {
/* 289 */       keyCertChain = SslContext.toX509Certificates(keyCertChainInputStream);
/*     */     } catch (Exception e) { X509Certificate[] keyCertChain;
/* 291 */       throw new IllegalArgumentException("Input stream not contain valid certificates.", e);
/*     */     }
/*     */     X509Certificate[] keyCertChain;
/* 294 */     try { key = SslContext.toPrivateKey(keyInputStream, keyPassword);
/*     */     } catch (Exception e) { PrivateKey key;
/* 296 */       throw new IllegalArgumentException("Input stream does not contain valid private key.", e); }
/*     */     PrivateKey key;
/* 298 */     return keyManager(key, keyPassword, keyCertChain);
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
/*     */   public SslContextBuilder keyManager(PrivateKey key, String keyPassword, X509Certificate... keyCertChain)
/*     */   {
/* 311 */     if (this.forServer) {
/* 312 */       ObjectUtil.checkNotNull(keyCertChain, "keyCertChain required for servers");
/* 313 */       if (keyCertChain.length == 0) {
/* 314 */         throw new IllegalArgumentException("keyCertChain must be non-empty");
/*     */       }
/* 316 */       ObjectUtil.checkNotNull(key, "key required for servers");
/*     */     }
/* 318 */     if ((keyCertChain == null) || (keyCertChain.length == 0)) {
/* 319 */       this.keyCertChain = null;
/*     */     } else {
/* 321 */       for (X509Certificate cert : keyCertChain) {
/* 322 */         if (cert == null) {
/* 323 */           throw new IllegalArgumentException("keyCertChain contains null entry");
/*     */         }
/*     */       }
/* 326 */       this.keyCertChain = ((X509Certificate[])keyCertChain.clone());
/*     */     }
/* 328 */     this.key = key;
/* 329 */     this.keyPassword = keyPassword;
/* 330 */     this.keyManagerFactory = null;
/* 331 */     return this;
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
/*     */   public SslContextBuilder keyManager(KeyManagerFactory keyManagerFactory)
/*     */   {
/* 346 */     if (this.forServer) {
/* 347 */       ObjectUtil.checkNotNull(keyManagerFactory, "keyManagerFactory required for servers");
/*     */     }
/* 349 */     this.keyCertChain = null;
/* 350 */     this.key = null;
/* 351 */     this.keyPassword = null;
/* 352 */     this.keyManagerFactory = keyManagerFactory;
/* 353 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder ciphers(Iterable<String> ciphers)
/*     */   {
/* 361 */     return ciphers(ciphers, IdentityCipherSuiteFilter.INSTANCE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder ciphers(Iterable<String> ciphers, CipherSuiteFilter cipherFilter)
/*     */   {
/* 370 */     ObjectUtil.checkNotNull(cipherFilter, "cipherFilter");
/* 371 */     this.ciphers = ciphers;
/* 372 */     this.cipherFilter = cipherFilter;
/* 373 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder applicationProtocolConfig(ApplicationProtocolConfig apn)
/*     */   {
/* 380 */     this.apn = apn;
/* 381 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder sessionCacheSize(long sessionCacheSize)
/*     */   {
/* 389 */     this.sessionCacheSize = sessionCacheSize;
/* 390 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder sessionTimeout(long sessionTimeout)
/*     */   {
/* 398 */     this.sessionTimeout = sessionTimeout;
/* 399 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder clientAuth(ClientAuth clientAuth)
/*     */   {
/* 406 */     this.clientAuth = ((ClientAuth)ObjectUtil.checkNotNull(clientAuth, "clientAuth"));
/* 407 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder protocols(String... protocols)
/*     */   {
/* 416 */     this.protocols = (protocols == null ? null : (String[])protocols.clone());
/* 417 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SslContextBuilder startTls(boolean startTls)
/*     */   {
/* 424 */     this.startTls = startTls;
/* 425 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextBuilder enableOcsp(boolean enableOcsp)
/*     */   {
/* 436 */     this.enableOcsp = enableOcsp;
/* 437 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContext build()
/*     */     throws SSLException
/*     */   {
/* 446 */     if (this.forServer) {
/* 447 */       return SslContext.newServerContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.sessionCacheSize, this.sessionTimeout, this.clientAuth, this.protocols, this.startTls, this.enableOcsp);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 452 */     return SslContext.newClientContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.protocols, this.sessionCacheSize, this.sessionTimeout, this.enableOcsp);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\SslContextBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */