/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.internal.tcnative.SniHostNameMatcher;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
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
/*     */ public final class ReferenceCountedOpenSslServerContext
/*     */   extends ReferenceCountedOpenSslContext
/*     */ {
/*  48 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslServerContext.class);
/*  49 */   private static final byte[] ID = { 110, 101, 116, 116, 121 };
/*     */   
/*     */   private final OpenSslServerSessionContext sessionContext;
/*     */   
/*     */   private final OpenSslKeyMaterialManager keyMaterialManager;
/*     */   
/*     */ 
/*     */   ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp)
/*     */     throws SSLException
/*     */   {
/*  59 */     this(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, 
/*  60 */       toNegotiator(apn), sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp)
/*     */     throws SSLException
/*     */   {
/*  70 */     super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 1, keyCertChain, clientAuth, protocols, startTls, enableOcsp, true);
/*     */     
/*     */ 
/*  73 */     boolean success = false;
/*     */     try {
/*  75 */       ServerContext context = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
/*     */       
/*  77 */       this.sessionContext = context.sessionContext;
/*  78 */       this.keyMaterialManager = context.keyMaterialManager;
/*  79 */       success = true;
/*     */     } finally {
/*  81 */       if (!success) {
/*  82 */         release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public OpenSslServerSessionContext sessionContext()
/*     */   {
/*  89 */     return this.sessionContext;
/*     */   }
/*     */   
/*     */   OpenSslKeyMaterialManager keyMaterialManager()
/*     */   {
/*  94 */     return this.keyMaterialManager;
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
/*     */   static ServerContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory)
/*     */     throws SSLException
/*     */   {
/* 108 */     ServerContext result = new ServerContext();
/* 109 */     OpenSslKeyMaterialProvider keyMaterialProvider = null;
/*     */     try {
/*     */       try {
/* 112 */         SSLContext.setVerify(ctx, 0, 10);
/* 113 */         if (!OpenSsl.useKeyManagerFactory()) {
/* 114 */           if (keyManagerFactory != null) {
/* 115 */             throw new IllegalArgumentException("KeyManagerFactory not supported");
/*     */           }
/*     */           
/* 118 */           ObjectUtil.checkNotNull(keyCertChain, "keyCertChain");
/*     */           
/* 120 */           setKeyMaterial(ctx, keyCertChain, key, keyPassword);
/*     */         }
/*     */         else
/*     */         {
/* 124 */           if (keyManagerFactory == null) {
/* 125 */             char[] keyPasswordChars = keyStorePassword(keyPassword);
/* 126 */             KeyStore ks = buildKeyStore(keyCertChain, key, keyPasswordChars);
/* 127 */             if (ks.aliases().hasMoreElements()) {
/* 128 */               keyManagerFactory = new OpenSslX509KeyManagerFactory();
/*     */             }
/*     */             else {
/* 131 */               keyManagerFactory = new OpenSslCachingX509KeyManagerFactory(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()));
/*     */             }
/* 133 */             keyManagerFactory.init(ks, keyPasswordChars);
/*     */           }
/* 135 */           keyMaterialProvider = providerFor(keyManagerFactory, keyPassword);
/*     */           
/* 137 */           result.keyMaterialManager = new OpenSslKeyMaterialManager(keyMaterialProvider);
/*     */         }
/*     */       } catch (Exception e) {
/* 140 */         throw new SSLException("failed to set certificate and key", e);
/*     */       }
/*     */       try {
/* 143 */         if (trustCertCollection != null) {
/* 144 */           trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory);
/* 145 */         } else if (trustManagerFactory == null)
/*     */         {
/* 147 */           trustManagerFactory = TrustManagerFactory.getInstance(
/* 148 */             TrustManagerFactory.getDefaultAlgorithm());
/* 149 */           trustManagerFactory.init((KeyStore)null);
/*     */         }
/*     */         
/* 152 */         X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */         if (useExtendedTrustManager(manager)) {
/* 162 */           SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager)manager));
/*     */         }
/*     */         else {
/* 165 */           SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
/*     */         }
/*     */         
/* 168 */         X509Certificate[] issuers = manager.getAcceptedIssuers();
/* 169 */         if ((issuers != null) && (issuers.length > 0)) {
/* 170 */           long bio = 0L;
/*     */           try {
/* 172 */             bio = toBIO(ByteBufAllocator.DEFAULT, issuers);
/* 173 */             if (!SSLContext.setCACertificateBio(ctx, bio)) {
/* 174 */               throw new SSLException("unable to setup accepted issuers for trustmanager " + manager);
/*     */             }
/*     */           } finally {
/* 177 */             freeBio(bio);
/*     */           }
/*     */         }
/*     */         
/* 181 */         if (PlatformDependent.javaVersion() >= 8)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 186 */           SSLContext.setSniHostnameMatcher(ctx, new OpenSslSniHostnameMatcher(engineMap));
/*     */         }
/*     */       } catch (SSLException e) {
/* 189 */         throw e;
/*     */       } catch (Exception e) {
/* 191 */         throw new SSLException("unable to setup trustmanager", e);
/*     */       }
/*     */       
/* 194 */       result.sessionContext = new OpenSslServerSessionContext(thiz, keyMaterialProvider);
/* 195 */       result.sessionContext.setSessionIdContext(ID);
/*     */       
/* 197 */       keyMaterialProvider = null;
/*     */       
/* 199 */       return result;
/*     */     } finally {
/* 201 */       if (keyMaterialProvider != null)
/* 202 */         keyMaterialProvider.destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ServerContext { OpenSslServerSessionContext sessionContext;
/*     */     OpenSslKeyMaterialManager keyMaterialManager; }
/*     */   
/*     */   private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier { private final X509TrustManager manager;
/*     */     
/* 211 */     TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) { super();
/* 212 */       this.manager = manager;
/*     */     }
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth)
/*     */       throws Exception
/*     */     {
/* 218 */       this.manager.checkClientTrusted(peerCerts, auth);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509ExtendedTrustManager manager;
/*     */     
/*     */     ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
/* 226 */       super();
/* 227 */       this.manager = manager;
/*     */     }
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth)
/*     */       throws Exception
/*     */     {
/* 233 */       this.manager.checkClientTrusted(peerCerts, auth, engine);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OpenSslSniHostnameMatcher implements SniHostNameMatcher {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     
/*     */     OpenSslSniHostnameMatcher(OpenSslEngineMap engineMap) {
/* 241 */       this.engineMap = engineMap;
/*     */     }
/*     */     
/*     */     public boolean match(long ssl, String hostname)
/*     */     {
/* 246 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
/* 247 */       if (engine != null)
/*     */       {
/* 249 */         return engine.checkSniHostnameMatch(hostname.getBytes(CharsetUtil.UTF_8));
/*     */       }
/* 251 */       ReferenceCountedOpenSslServerContext.logger.warn("No ReferenceCountedOpenSslEngine found for SSL pointer: {}", Long.valueOf(ssl));
/* 252 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslServerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */