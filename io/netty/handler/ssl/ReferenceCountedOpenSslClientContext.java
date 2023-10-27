/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.CertificateRequestedCallback;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ public final class ReferenceCountedOpenSslClientContext
/*     */   extends ReferenceCountedOpenSslContext
/*     */ {
/*  48 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslClientContext.class);
/*     */   
/*     */ 
/*     */   private final OpenSslSessionContext sessionContext;
/*     */   
/*     */ 
/*     */   ReferenceCountedOpenSslClientContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, boolean enableOcsp)
/*     */     throws SSLException
/*     */   {
/*  57 */     super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 0, keyCertChain, ClientAuth.NONE, protocols, false, enableOcsp, true);
/*     */     
/*  59 */     boolean success = false;
/*     */     try {
/*  61 */       this.sessionContext = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory);
/*     */       
/*  63 */       success = true;
/*     */     } finally {
/*  65 */       if (!success) {
/*  66 */         release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   OpenSslKeyMaterialManager keyMaterialManager()
/*     */   {
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public OpenSslSessionContext sessionContext()
/*     */   {
/*  78 */     return this.sessionContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static OpenSslSessionContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory)
/*     */     throws SSLException
/*     */   {
/*  87 */     if (((key == null) && (keyCertChain != null)) || ((key != null) && (keyCertChain == null))) {
/*  88 */       throw new IllegalArgumentException("Either both keyCertChain and key needs to be null or none of them");
/*     */     }
/*     */     
/*  91 */     OpenSslKeyMaterialProvider keyMaterialProvider = null;
/*     */     try {
/*     */       KeyStore ks;
/*  94 */       try { if (!OpenSsl.useKeyManagerFactory()) {
/*  95 */           if (keyManagerFactory != null) {
/*  96 */             throw new IllegalArgumentException("KeyManagerFactory not supported");
/*     */           }
/*     */           
/*  99 */           if (keyCertChain != null) {
/* 100 */             setKeyMaterial(ctx, keyCertChain, key, keyPassword);
/*     */           }
/*     */         }
/*     */         else {
/* 104 */           if ((keyManagerFactory == null) && (keyCertChain != null)) {
/* 105 */             char[] keyPasswordChars = keyStorePassword(keyPassword);
/* 106 */             ks = buildKeyStore(keyCertChain, key, keyPasswordChars);
/* 107 */             if (ks.aliases().hasMoreElements()) {
/* 108 */               keyManagerFactory = new OpenSslX509KeyManagerFactory();
/*     */             }
/*     */             else {
/* 111 */               keyManagerFactory = new OpenSslCachingX509KeyManagerFactory(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()));
/*     */             }
/* 113 */             keyManagerFactory.init(ks, keyPasswordChars);
/* 114 */             keyMaterialProvider = providerFor(keyManagerFactory, keyPassword);
/* 115 */           } else if (keyManagerFactory != null) {
/* 116 */             keyMaterialProvider = providerFor(keyManagerFactory, keyPassword);
/*     */           }
/*     */           
/* 119 */           if (keyMaterialProvider != null) {
/* 120 */             OpenSslKeyMaterialManager materialManager = new OpenSslKeyMaterialManager(keyMaterialProvider);
/* 121 */             SSLContext.setCertRequestedCallback(ctx, new OpenSslCertificateRequestedCallback(engineMap, materialManager));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 126 */         throw new SSLException("failed to set certificate and key", e);
/*     */       }
/*     */       
/* 129 */       SSLContext.setVerify(ctx, 0, 10);
/*     */       try
/*     */       {
/* 132 */         if (trustCertCollection != null) {
/* 133 */           trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory);
/* 134 */         } else if (trustManagerFactory == null) {
/* 135 */           trustManagerFactory = TrustManagerFactory.getInstance(
/* 136 */             TrustManagerFactory.getDefaultAlgorithm());
/* 137 */           trustManagerFactory.init((KeyStore)null);
/*     */         }
/* 139 */         X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */         if (useExtendedTrustManager(manager)) {
/* 149 */           SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager)manager));
/*     */         }
/*     */         else {
/* 152 */           SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
/*     */         }
/*     */         
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 158 */         throw new SSLException("unable to setup trustmanager", e);
/*     */       }
/* 160 */       OpenSslClientSessionContext context = new OpenSslClientSessionContext(thiz, keyMaterialProvider);
/* 161 */       keyMaterialProvider = null;
/* 162 */       return context;
/*     */     } finally {
/* 164 */       if (keyMaterialProvider != null) {
/* 165 */         keyMaterialProvider.destroy();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OpenSslClientSessionContext extends OpenSslSessionContext
/*     */   {
/*     */     OpenSslClientSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
/* 173 */       super(provider);
/*     */     }
/*     */     
/*     */     public void setSessionTimeout(int seconds)
/*     */     {
/* 178 */       if (seconds < 0) {
/* 179 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */     
/*     */     public int getSessionTimeout()
/*     */     {
/* 185 */       return 0;
/*     */     }
/*     */     
/*     */     public void setSessionCacheSize(int size)
/*     */     {
/* 190 */       if (size < 0) {
/* 191 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */     
/*     */     public int getSessionCacheSize()
/*     */     {
/* 197 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void setSessionCacheEnabled(boolean enabled) {}
/*     */     
/*     */ 
/*     */     public boolean isSessionCacheEnabled()
/*     */     {
/* 207 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509TrustManager manager;
/*     */     
/*     */     TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
/* 215 */       super();
/* 216 */       this.manager = manager;
/*     */     }
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth)
/*     */       throws Exception
/*     */     {
/* 222 */       this.manager.checkServerTrusted(peerCerts, auth);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
/*     */     private final X509ExtendedTrustManager manager;
/*     */     
/*     */     ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
/* 230 */       super();
/* 231 */       this.manager = manager;
/*     */     }
/*     */     
/*     */     void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth)
/*     */       throws Exception
/*     */     {
/* 237 */       this.manager.checkServerTrusted(peerCerts, auth, engine);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OpenSslCertificateRequestedCallback implements CertificateRequestedCallback {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     private final OpenSslKeyMaterialManager keyManagerHolder;
/*     */     
/*     */     OpenSslCertificateRequestedCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
/* 246 */       this.engineMap = engineMap;
/* 247 */       this.keyManagerHolder = keyManagerHolder;
/*     */     }
/*     */     
/*     */ 
/*     */     public void requested(long ssl, long certOut, long keyOut, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals)
/*     */     {
/* 253 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
/*     */       try {
/* 255 */         Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
/* 256 */         String[] keyTypes = (String[])keyTypesSet.toArray(new String[0]);
/*     */         X500Principal[] issuers;
/* 258 */         X500Principal[] issuers; if (asn1DerEncodedPrincipals == null) {
/* 259 */           issuers = null;
/*     */         } else {
/* 261 */           issuers = new X500Principal[asn1DerEncodedPrincipals.length];
/* 262 */           for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
/* 263 */             issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
/*     */           }
/*     */         }
/* 266 */         this.keyManagerHolder.setKeyMaterialClientSide(engine, certOut, keyOut, keyTypes, issuers);
/*     */       } catch (Throwable cause) {
/* 268 */         ReferenceCountedOpenSslClientContext.logger.debug("request of key failed", cause);
/* 269 */         SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
/* 270 */         e.initCause(cause);
/* 271 */         engine.handshakeException = e;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static Set<String> supportedClientKeyTypes(byte[] clientCertificateTypes)
/*     */     {
/* 284 */       Set<String> result = new HashSet(clientCertificateTypes.length);
/* 285 */       for (byte keyTypeCode : clientCertificateTypes) {
/* 286 */         String keyType = clientKeyType(keyTypeCode);
/* 287 */         if (keyType != null)
/*     */         {
/*     */ 
/*     */ 
/* 291 */           result.add(keyType); }
/*     */       }
/* 293 */       return result;
/*     */     }
/*     */     
/*     */     private static String clientKeyType(byte clientCertificateType)
/*     */     {
/* 298 */       switch (clientCertificateType) {
/*     */       case 1: 
/* 300 */         return "RSA";
/*     */       case 3: 
/* 302 */         return "DH_RSA";
/*     */       case 64: 
/* 304 */         return "EC";
/*     */       case 65: 
/* 306 */         return "EC_RSA";
/*     */       case 66: 
/* 308 */         return "EC_EC";
/*     */       }
/* 310 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslClientContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */