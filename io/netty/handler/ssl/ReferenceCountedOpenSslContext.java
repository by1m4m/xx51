/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.internal.tcnative.CertificateVerifier;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.ResourceLeakDetectorFactory;
/*     */ import io.netty.util.ResourceLeakTracker;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.CertPathValidatorException.Reason;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.CertificateRevokedException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
/*     */ import javax.net.ssl.X509KeyManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ReferenceCountedOpenSslContext
/*     */   extends SslContext
/*     */   implements ReferenceCounted
/*     */ {
/*  77 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslContext.class);
/*     */   
/*     */ 
/*  80 */   private static final int DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE = ((Integer)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Integer run()
/*     */     {
/*  83 */       return Integer.valueOf(Math.max(1, 
/*  84 */         SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize", 2048)));
/*     */     }
/*  80 */   })).intValue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final Integer DH_KEY_LENGTH;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private static final ResourceLeakDetector<ReferenceCountedOpenSslContext> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslContext.class);
/*     */   
/*     */ 
/*     */   protected static final int VERIFY_DEPTH = 10;
/*     */   
/*     */   protected long ctx;
/*     */   
/*     */   private final List<String> unmodifiableCiphers;
/*     */   
/*     */   private final long sessionCacheSize;
/*     */   
/*     */   private final long sessionTimeout;
/*     */   
/*     */   private final OpenSslApplicationProtocolNegotiator apn;
/*     */   
/*     */   private final int mode;
/*     */   
/*     */   private final ResourceLeakTracker<ReferenceCountedOpenSslContext> leak;
/*     */   
/* 110 */   private final AbstractReferenceCounted refCnt = new AbstractReferenceCounted()
/*     */   {
/*     */     public ReferenceCounted touch(Object hint) {
/* 113 */       if (ReferenceCountedOpenSslContext.this.leak != null) {
/* 114 */         ReferenceCountedOpenSslContext.this.leak.record(hint);
/*     */       }
/*     */       
/* 117 */       return ReferenceCountedOpenSslContext.this;
/*     */     }
/*     */     
/*     */     protected void deallocate()
/*     */     {
/* 122 */       ReferenceCountedOpenSslContext.this.destroy();
/* 123 */       if (ReferenceCountedOpenSslContext.this.leak != null) {
/* 124 */         boolean closed = ReferenceCountedOpenSslContext.this.leak.close(ReferenceCountedOpenSslContext.this);
/* 125 */         assert (closed);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   final Certificate[] keyCertChain;
/*     */   final ClientAuth clientAuth;
/*     */   final String[] protocols;
/*     */   final boolean enableOcsp;
/* 134 */   final OpenSslEngineMap engineMap = new DefaultOpenSslEngineMap(null);
/* 135 */   final ReadWriteLock ctxLock = new ReentrantReadWriteLock();
/*     */   
/* 137 */   private volatile int bioNonApplicationBufferSize = DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE;
/*     */   
/*     */ 
/* 140 */   static final OpenSslApplicationProtocolNegotiator NONE_PROTOCOL_NEGOTIATOR = new OpenSslApplicationProtocolNegotiator()
/*     */   {
/*     */     public ApplicationProtocolConfig.Protocol protocol()
/*     */     {
/* 144 */       return ApplicationProtocolConfig.Protocol.NONE;
/*     */     }
/*     */     
/*     */     public List<String> protocols()
/*     */     {
/* 149 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */     public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior()
/*     */     {
/* 154 */       return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
/*     */     }
/*     */     
/*     */     public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior()
/*     */     {
/* 159 */       return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
/*     */     }
/*     */   };
/*     */   
/*     */   static {
/* 164 */     Integer dhLen = null;
/*     */     try
/*     */     {
/* 167 */       String dhKeySize = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run() {
/* 170 */           return SystemPropertyUtil.get("jdk.tls.ephemeralDHKeySize");
/*     */         }
/*     */       });
/* 173 */       if (dhKeySize != null) {
/*     */         try {
/* 175 */           dhLen = Integer.valueOf(dhKeySize);
/*     */         } catch (NumberFormatException e) {
/* 177 */           logger.debug("ReferenceCountedOpenSslContext supports -Djdk.tls.ephemeralDHKeySize={int}, but got: " + dhKeySize);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/* 184 */     DH_KEY_LENGTH = dhLen;
/*     */   }
/*     */   
/*     */ 
/*     */   ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apnCfg, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection)
/*     */     throws SSLException
/*     */   {
/* 191 */     this(ciphers, cipherFilter, toNegotiator(apnCfg), sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, leakDetection);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection)
/*     */     throws SSLException
/*     */   {
/* 200 */     super(startTls);
/*     */     
/* 202 */     OpenSsl.ensureAvailability();
/*     */     
/* 204 */     if ((enableOcsp) && (!OpenSsl.isOcspSupported())) {
/* 205 */       throw new IllegalStateException("OCSP is not supported.");
/*     */     }
/*     */     
/* 208 */     if ((mode != 1) && (mode != 0)) {
/* 209 */       throw new IllegalArgumentException("mode most be either SSL.SSL_MODE_SERVER or SSL.SSL_MODE_CLIENT");
/*     */     }
/* 211 */     this.leak = (leakDetection ? leakDetector.track(this) : null);
/* 212 */     this.mode = mode;
/* 213 */     this.clientAuth = (isServer() ? (ClientAuth)ObjectUtil.checkNotNull(clientAuth, "clientAuth") : ClientAuth.NONE);
/* 214 */     this.protocols = protocols;
/* 215 */     this.enableOcsp = enableOcsp;
/*     */     
/* 217 */     this.keyCertChain = (keyCertChain == null ? null : (Certificate[])keyCertChain.clone());
/*     */     
/* 219 */     this.unmodifiableCiphers = Arrays.asList(((CipherSuiteFilter)ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, OpenSsl.DEFAULT_CIPHERS, 
/* 220 */       OpenSsl.availableJavaCipherSuites()));
/*     */     
/* 222 */     this.apn = ((OpenSslApplicationProtocolNegotiator)ObjectUtil.checkNotNull(apn, "apn"));
/*     */     
/*     */ 
/* 225 */     boolean success = false;
/*     */     try {
/*     */       try {
/* 228 */         this.ctx = SSLContext.make(31, mode);
/*     */       } catch (Exception e) {
/* 230 */         throw new SSLException("failed to create an SSL_CTX", e);
/*     */       }
/*     */       
/* 233 */       SSLContext.setOptions(this.ctx, SSLContext.getOptions(this.ctx) | SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_CIPHER_SERVER_PREFERENCE | SSL.SSL_OP_NO_COMPRESSION | SSL.SSL_OP_NO_TICKET);
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
/* 250 */       SSLContext.setMode(this.ctx, SSLContext.getMode(this.ctx) | SSL.SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER);
/*     */       
/* 252 */       if (DH_KEY_LENGTH != null) {
/* 253 */         SSLContext.setTmpDHLength(this.ctx, DH_KEY_LENGTH.intValue());
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 258 */         SSLContext.setCipherSuite(this.ctx, CipherSuiteConverter.toOpenSsl(this.unmodifiableCiphers));
/*     */       } catch (SSLException e) {
/* 260 */         throw e;
/*     */       } catch (Exception e) {
/* 262 */         throw new SSLException("failed to set cipher suite: " + this.unmodifiableCiphers, e);
/*     */       }
/*     */       
/* 265 */       List<String> nextProtoList = apn.protocols();
/*     */       
/* 267 */       if (!nextProtoList.isEmpty()) {
/* 268 */         String[] appProtocols = (String[])nextProtoList.toArray(new String[0]);
/* 269 */         int selectorBehavior = opensslSelectorFailureBehavior(apn.selectorFailureBehavior());
/*     */         
/* 271 */         switch (apn.protocol()) {
/*     */         case NPN: 
/* 273 */           SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
/* 274 */           break;
/*     */         case ALPN: 
/* 276 */           SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
/* 277 */           break;
/*     */         case NPN_AND_ALPN: 
/* 279 */           SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
/* 280 */           SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
/* 281 */           break;
/*     */         default: 
/* 283 */           throw new Error();
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 288 */       if (sessionCacheSize <= 0L)
/*     */       {
/* 290 */         sessionCacheSize = SSLContext.setSessionCacheSize(this.ctx, 20480L);
/*     */       }
/* 292 */       this.sessionCacheSize = sessionCacheSize;
/* 293 */       SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
/*     */       
/*     */ 
/* 296 */       if (sessionTimeout <= 0L)
/*     */       {
/* 298 */         sessionTimeout = SSLContext.setSessionCacheTimeout(this.ctx, 300L);
/*     */       }
/* 300 */       this.sessionTimeout = sessionTimeout;
/* 301 */       SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
/*     */       
/* 303 */       if (enableOcsp) {
/* 304 */         SSLContext.enableOcsp(this.ctx, isClient());
/*     */       }
/* 306 */       success = true;
/*     */     } finally {
/* 308 */       if (!success) {
/* 309 */         release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int opensslSelectorFailureBehavior(ApplicationProtocolConfig.SelectorFailureBehavior behavior) {
/* 315 */     switch (behavior) {
/*     */     case NO_ADVERTISE: 
/* 317 */       return 0;
/*     */     case CHOOSE_MY_LAST_PROTOCOL: 
/* 319 */       return 1;
/*     */     }
/* 321 */     throw new Error();
/*     */   }
/*     */   
/*     */ 
/*     */   public final List<String> cipherSuites()
/*     */   {
/* 327 */     return this.unmodifiableCiphers;
/*     */   }
/*     */   
/*     */   public final long sessionCacheSize()
/*     */   {
/* 332 */     return this.sessionCacheSize;
/*     */   }
/*     */   
/*     */   public final long sessionTimeout()
/*     */   {
/* 337 */     return this.sessionTimeout;
/*     */   }
/*     */   
/*     */   public ApplicationProtocolNegotiator applicationProtocolNegotiator()
/*     */   {
/* 342 */     return this.apn;
/*     */   }
/*     */   
/*     */   public final boolean isClient()
/*     */   {
/* 347 */     return this.mode == 0;
/*     */   }
/*     */   
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort)
/*     */   {
/* 352 */     return newEngine0(alloc, peerHost, peerPort, true);
/*     */   }
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls)
/*     */   {
/* 357 */     return new SslHandler(newEngine0(alloc, null, -1, false), startTls);
/*     */   }
/*     */   
/*     */   protected final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls)
/*     */   {
/* 362 */     return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), startTls);
/*     */   }
/*     */   
/*     */   SSLEngine newEngine0(ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
/* 366 */     return new ReferenceCountedOpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SSLEngine newEngine(ByteBufAllocator alloc)
/*     */   {
/* 376 */     return newEngine(alloc, null, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final long context()
/*     */   {
/* 388 */     Lock readerLock = this.ctxLock.readLock();
/* 389 */     readerLock.lock();
/*     */     try {
/* 391 */       return this.ctx;
/*     */     } finally {
/* 393 */       readerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final OpenSslSessionStats stats()
/*     */   {
/* 404 */     return sessionContext().stats();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setRejectRemoteInitiatedRenegotiation(boolean rejectRemoteInitiatedRenegotiation)
/*     */   {
/* 414 */     if (!rejectRemoteInitiatedRenegotiation) {
/* 415 */       throw new UnsupportedOperationException("Renegotiation is not supported");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean getRejectRemoteInitiatedRenegotiation()
/*     */   {
/* 425 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBioNonApplicationBufferSize(int bioNonApplicationBufferSize)
/*     */   {
/* 434 */     this.bioNonApplicationBufferSize = ObjectUtil.checkPositiveOrZero(bioNonApplicationBufferSize, "bioNonApplicationBufferSize");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getBioNonApplicationBufferSize()
/*     */   {
/* 441 */     return this.bioNonApplicationBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setTicketKeys(byte[] keys)
/*     */   {
/* 451 */     sessionContext().setTicketKeys(keys);
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
/*     */   @Deprecated
/*     */   public final long sslCtxPointer()
/*     */   {
/* 466 */     Lock readerLock = this.ctxLock.readLock();
/* 467 */     readerLock.lock();
/*     */     try {
/* 469 */       return this.ctx;
/*     */     } finally {
/* 471 */       readerLock.unlock();
/*     */     }
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
/*     */ 
/*     */   protected static X509Certificate[] certificates(byte[][] chain)
/*     */   {
/* 501 */     X509Certificate[] peerCerts = new X509Certificate[chain.length];
/* 502 */     for (int i = 0; i < peerCerts.length; i++) {
/* 503 */       peerCerts[i] = new OpenSslX509Certificate(chain[i]);
/*     */     }
/* 505 */     return peerCerts;
/*     */   }
/*     */   
/*     */   protected static X509TrustManager chooseTrustManager(TrustManager[] managers) {
/* 509 */     for (TrustManager m : managers) {
/* 510 */       if ((m instanceof X509TrustManager)) {
/* 511 */         return (X509TrustManager)m;
/*     */       }
/*     */     }
/* 514 */     throw new IllegalStateException("no X509TrustManager found");
/*     */   }
/*     */   
/*     */   protected static X509KeyManager chooseX509KeyManager(KeyManager[] kms) {
/* 518 */     for (KeyManager km : kms) {
/* 519 */       if ((km instanceof X509KeyManager)) {
/* 520 */         return (X509KeyManager)km;
/*     */       }
/*     */     }
/* 523 */     throw new IllegalStateException("no X509KeyManager found");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static OpenSslApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config)
/*     */   {
/* 535 */     if (config == null) {
/* 536 */       return NONE_PROTOCOL_NEGOTIATOR;
/*     */     }
/*     */     
/* 539 */     switch (config.protocol()) {
/*     */     case NONE: 
/* 541 */       return NONE_PROTOCOL_NEGOTIATOR;
/*     */     case NPN: 
/*     */     case ALPN: 
/*     */     case NPN_AND_ALPN: 
/* 545 */       switch (config.selectedListenerFailureBehavior()) {
/*     */       case CHOOSE_MY_LAST_PROTOCOL: 
/*     */       case ACCEPT: 
/* 548 */         switch (config.selectorFailureBehavior()) {
/*     */         case NO_ADVERTISE: 
/*     */         case CHOOSE_MY_LAST_PROTOCOL: 
/* 551 */           return new OpenSslDefaultApplicationProtocolNegotiator(config);
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/* 557 */         throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectorFailureBehavior() + " behavior");
/*     */       }
/*     */       
/*     */       
/*     */ 
/*     */ 
/* 563 */       throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectedListenerFailureBehavior() + " behavior");
/*     */     }
/*     */     
/* 566 */     throw new Error();
/*     */   }
/*     */   
/*     */   static boolean useExtendedTrustManager(X509TrustManager trustManager)
/*     */   {
/* 571 */     return (PlatformDependent.javaVersion() >= 7) && ((trustManager instanceof X509ExtendedTrustManager));
/*     */   }
/*     */   
/*     */   public final int refCnt()
/*     */   {
/* 576 */     return this.refCnt.refCnt();
/*     */   }
/*     */   
/*     */   public final ReferenceCounted retain()
/*     */   {
/* 581 */     this.refCnt.retain();
/* 582 */     return this;
/*     */   }
/*     */   
/*     */   public final ReferenceCounted retain(int increment)
/*     */   {
/* 587 */     this.refCnt.retain(increment);
/* 588 */     return this;
/*     */   }
/*     */   
/*     */   public final ReferenceCounted touch()
/*     */   {
/* 593 */     this.refCnt.touch();
/* 594 */     return this;
/*     */   }
/*     */   
/*     */   public final ReferenceCounted touch(Object hint)
/*     */   {
/* 599 */     this.refCnt.touch(hint);
/* 600 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean release()
/*     */   {
/* 605 */     return this.refCnt.release();
/*     */   }
/*     */   
/*     */   public final boolean release(int decrement)
/*     */   {
/* 610 */     return this.refCnt.release(decrement);
/*     */   }
/*     */   
/*     */   static abstract class AbstractCertificateVerifier extends CertificateVerifier {
/*     */     private final OpenSslEngineMap engineMap;
/*     */     
/*     */     AbstractCertificateVerifier(OpenSslEngineMap engineMap) {
/* 617 */       this.engineMap = engineMap;
/*     */     }
/*     */     
/*     */     public final int verify(long ssl, byte[][] chain, String auth)
/*     */     {
/* 622 */       X509Certificate[] peerCerts = ReferenceCountedOpenSslContext.certificates(chain);
/* 623 */       ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
/*     */       try {
/* 625 */         verify(engine, peerCerts, auth);
/* 626 */         return CertificateVerifier.X509_V_OK;
/*     */       } catch (Throwable cause) {
/* 628 */         ReferenceCountedOpenSslContext.logger.debug("verification of certificate failed", cause);
/* 629 */         SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
/* 630 */         e.initCause(cause);
/* 631 */         engine.handshakeException = e;
/*     */         
/*     */ 
/* 634 */         if ((cause instanceof OpenSslCertificateException))
/*     */         {
/*     */ 
/* 637 */           return ((OpenSslCertificateException)cause).errorCode();
/*     */         }
/* 639 */         if ((cause instanceof CertificateExpiredException)) {
/* 640 */           return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
/*     */         }
/* 642 */         if ((cause instanceof CertificateNotYetValidException)) {
/* 643 */           return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
/*     */         }
/* 645 */         if (PlatformDependent.javaVersion() >= 7) {
/* 646 */           if ((cause instanceof CertificateRevokedException)) {
/* 647 */             return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 653 */           Throwable wrapped = cause.getCause();
/* 654 */           while (wrapped != null) {
/* 655 */             if ((wrapped instanceof CertPathValidatorException)) {
/* 656 */               CertPathValidatorException ex = (CertPathValidatorException)wrapped;
/* 657 */               CertPathValidatorException.Reason reason = ex.getReason();
/* 658 */               if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
/* 659 */                 return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
/*     */               }
/* 661 */               if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
/* 662 */                 return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
/*     */               }
/* 664 */               if (reason == CertPathValidatorException.BasicReason.REVOKED) {
/* 665 */                 return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
/*     */               }
/*     */             }
/* 668 */             wrapped = wrapped.getCause();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 673 */       return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
/*     */     }
/*     */     
/*     */     abstract void verify(ReferenceCountedOpenSslEngine paramReferenceCountedOpenSslEngine, X509Certificate[] paramArrayOfX509Certificate, String paramString)
/*     */       throws Exception;
/*     */   }
/*     */   
/*     */   private static final class DefaultOpenSslEngineMap implements OpenSslEngineMap
/*     */   {
/* 682 */     private final Map<Long, ReferenceCountedOpenSslEngine> engines = PlatformDependent.newConcurrentHashMap();
/*     */     
/*     */     public ReferenceCountedOpenSslEngine remove(long ssl)
/*     */     {
/* 686 */       return (ReferenceCountedOpenSslEngine)this.engines.remove(Long.valueOf(ssl));
/*     */     }
/*     */     
/*     */     public void add(ReferenceCountedOpenSslEngine engine)
/*     */     {
/* 691 */       this.engines.put(Long.valueOf(engine.sslPointer()), engine);
/*     */     }
/*     */     
/*     */     public ReferenceCountedOpenSslEngine get(long ssl)
/*     */     {
/* 696 */       return (ReferenceCountedOpenSslEngine)this.engines.get(Long.valueOf(ssl));
/*     */     }
/*     */   }
/*     */   
/*     */   static void setKeyMaterial(long ctx, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword)
/*     */     throws SSLException
/*     */   {
/* 703 */     long keyBio = 0L;
/* 704 */     long keyCertChainBio = 0L;
/* 705 */     long keyCertChainBio2 = 0L;
/* 706 */     PemEncoded encoded = null;
/*     */     try
/*     */     {
/* 709 */       encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, keyCertChain);
/* 710 */       keyCertChainBio = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/* 711 */       keyCertChainBio2 = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/*     */       
/* 713 */       if (key != null) {
/* 714 */         keyBio = toBIO(ByteBufAllocator.DEFAULT, key);
/*     */       }
/*     */       
/* 717 */       SSLContext.setCertificateBio(ctx, keyCertChainBio, keyBio, keyPassword == null ? "" : keyPassword);
/*     */       
/*     */ 
/*     */ 
/* 721 */       SSLContext.setCertificateChainBio(ctx, keyCertChainBio2, true);
/*     */     } catch (SSLException e) {
/* 723 */       throw e;
/*     */     } catch (Exception e) {
/* 725 */       throw new SSLException("failed to set certificate and key", e);
/*     */     } finally {
/* 727 */       freeBio(keyBio);
/* 728 */       freeBio(keyCertChainBio);
/* 729 */       freeBio(keyCertChainBio2);
/* 730 */       if (encoded != null) {
/* 731 */         encoded.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void freeBio(long bio) {
/* 737 */     if (bio != 0L) {
/* 738 */       SSL.freeBIO(bio);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static long toBIO(ByteBufAllocator allocator, PrivateKey key)
/*     */     throws Exception
/*     */   {
/* 747 */     if (key == null) {
/* 748 */       return 0L;
/*     */     }
/*     */     
/* 751 */     PemEncoded pem = PemPrivateKey.toPEM(allocator, true, key);
/*     */     try {
/* 753 */       return toBIO(allocator, pem.retain());
/*     */     } finally {
/* 755 */       pem.release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static long toBIO(ByteBufAllocator allocator, X509Certificate... certChain)
/*     */     throws Exception
/*     */   {
/* 764 */     if (certChain == null) {
/* 765 */       return 0L;
/*     */     }
/*     */     
/* 768 */     if (certChain.length == 0) {
/* 769 */       throw new IllegalArgumentException("certChain can't be empty");
/*     */     }
/*     */     
/* 772 */     PemEncoded pem = PemX509Certificate.toPEM(allocator, true, certChain);
/*     */     try {
/* 774 */       return toBIO(allocator, pem.retain());
/*     */     } finally {
/* 776 */       pem.release();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long newBIO(ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 812 */       long bio = SSL.newMemBIO();
/* 813 */       int readable = buffer.readableBytes();
/* 814 */       if (SSL.bioWrite(bio, OpenSsl.memoryAddress(buffer) + buffer.readerIndex(), readable) != readable) {
/* 815 */         SSL.freeBIO(bio);
/* 816 */         throw new IllegalStateException("Could not write data to memory BIO");
/*     */       }
/* 818 */       return bio;
/*     */     } finally {
/* 820 */       buffer.release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static OpenSslKeyMaterialProvider providerFor(KeyManagerFactory factory, String password)
/*     */   {
/* 830 */     if ((factory instanceof OpenSslX509KeyManagerFactory)) {
/* 831 */       return ((OpenSslX509KeyManagerFactory)factory).newProvider();
/*     */     }
/*     */     
/* 834 */     X509KeyManager keyManager = chooseX509KeyManager(factory.getKeyManagers());
/* 835 */     if ((factory instanceof OpenSslCachingX509KeyManagerFactory))
/*     */     {
/* 837 */       return new OpenSslCachingKeyMaterialProvider(keyManager, password);
/*     */     }
/*     */     
/* 840 */     return new OpenSslKeyMaterialProvider(keyManager, password);
/*     */   }
/*     */   
/*     */   abstract OpenSslKeyMaterialManager keyMaterialManager();
/*     */   
/*     */   public abstract OpenSslSessionContext sessionContext();
/*     */   
/*     */   /* Error */
/*     */   private void destroy()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 15	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctxLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   4: invokeinterface 122 1 0
/*     */     //   9: astore_1
/*     */     //   10: aload_1
/*     */     //   11: invokeinterface 112 1 0
/*     */     //   16: aload_0
/*     */     //   17: getfield 52	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   20: lconst_0
/*     */     //   21: lcmp
/*     */     //   22: ifeq +43 -> 65
/*     */     //   25: aload_0
/*     */     //   26: getfield 37	io/netty/handler/ssl/ReferenceCountedOpenSslContext:enableOcsp	Z
/*     */     //   29: ifeq +10 -> 39
/*     */     //   32: aload_0
/*     */     //   33: getfield 52	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   36: invokestatic 123	io/netty/internal/tcnative/SSLContext:disableOcsp	(J)V
/*     */     //   39: aload_0
/*     */     //   40: getfield 52	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   43: invokestatic 124	io/netty/internal/tcnative/SSLContext:free	(J)I
/*     */     //   46: pop
/*     */     //   47: aload_0
/*     */     //   48: lconst_0
/*     */     //   49: putfield 52	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   52: aload_0
/*     */     //   53: invokevirtual 114	io/netty/handler/ssl/ReferenceCountedOpenSslContext:sessionContext	()Lio/netty/handler/ssl/OpenSslSessionContext;
/*     */     //   56: astore_2
/*     */     //   57: aload_2
/*     */     //   58: ifnull +7 -> 65
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual 125	io/netty/handler/ssl/OpenSslSessionContext:destroy	()V
/*     */     //   65: aload_1
/*     */     //   66: invokeinterface 113 1 0
/*     */     //   71: goto +12 -> 83
/*     */     //   74: astore_3
/*     */     //   75: aload_1
/*     */     //   76: invokeinterface 113 1 0
/*     */     //   81: aload_3
/*     */     //   82: athrow
/*     */     //   83: return
/*     */     // Line number table:
/*     */     //   Java source line #479	-> byte code offset #0
/*     */     //   Java source line #480	-> byte code offset #10
/*     */     //   Java source line #482	-> byte code offset #16
/*     */     //   Java source line #483	-> byte code offset #25
/*     */     //   Java source line #484	-> byte code offset #32
/*     */     //   Java source line #487	-> byte code offset #39
/*     */     //   Java source line #488	-> byte code offset #47
/*     */     //   Java source line #490	-> byte code offset #52
/*     */     //   Java source line #491	-> byte code offset #57
/*     */     //   Java source line #492	-> byte code offset #61
/*     */     //   Java source line #496	-> byte code offset #65
/*     */     //   Java source line #497	-> byte code offset #71
/*     */     //   Java source line #496	-> byte code offset #74
/*     */     //   Java source line #497	-> byte code offset #81
/*     */     //   Java source line #498	-> byte code offset #83
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	84	0	this	ReferenceCountedOpenSslContext
/*     */     //   9	67	1	writerLock	Lock
/*     */     //   56	6	2	context	OpenSslSessionContext
/*     */     //   74	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	65	74	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   static long toBIO(ByteBufAllocator allocator, PemEncoded pem)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 168 1 0
/*     */     //   6: astore_2
/*     */     //   7: aload_2
/*     */     //   8: invokevirtual 169	io/netty/buffer/ByteBuf:isDirect	()Z
/*     */     //   11: ifeq +20 -> 31
/*     */     //   14: aload_2
/*     */     //   15: invokevirtual 170	io/netty/buffer/ByteBuf:retainedSlice	()Lio/netty/buffer/ByteBuf;
/*     */     //   18: invokestatic 171	io/netty/handler/ssl/ReferenceCountedOpenSslContext:newBIO	(Lio/netty/buffer/ByteBuf;)J
/*     */     //   21: lstore_3
/*     */     //   22: aload_1
/*     */     //   23: invokeinterface 163 1 0
/*     */     //   28: pop
/*     */     //   29: lload_3
/*     */     //   30: lreturn
/*     */     //   31: aload_0
/*     */     //   32: aload_2
/*     */     //   33: invokevirtual 172	io/netty/buffer/ByteBuf:readableBytes	()I
/*     */     //   36: invokeinterface 173 2 0
/*     */     //   41: astore_3
/*     */     //   42: aload_3
/*     */     //   43: aload_2
/*     */     //   44: aload_2
/*     */     //   45: invokevirtual 174	io/netty/buffer/ByteBuf:readerIndex	()I
/*     */     //   48: aload_2
/*     */     //   49: invokevirtual 172	io/netty/buffer/ByteBuf:readableBytes	()I
/*     */     //   52: invokevirtual 175	io/netty/buffer/ByteBuf:writeBytes	(Lio/netty/buffer/ByteBuf;II)Lio/netty/buffer/ByteBuf;
/*     */     //   55: pop
/*     */     //   56: aload_3
/*     */     //   57: invokevirtual 170	io/netty/buffer/ByteBuf:retainedSlice	()Lio/netty/buffer/ByteBuf;
/*     */     //   60: invokestatic 171	io/netty/handler/ssl/ReferenceCountedOpenSslContext:newBIO	(Lio/netty/buffer/ByteBuf;)J
/*     */     //   63: lstore 4
/*     */     //   65: aload_1
/*     */     //   66: invokeinterface 176 1 0
/*     */     //   71: ifeq +7 -> 78
/*     */     //   74: aload_3
/*     */     //   75: invokestatic 177	io/netty/handler/ssl/SslUtils:zeroout	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   78: aload_3
/*     */     //   79: invokevirtual 178	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   82: pop
/*     */     //   83: goto +13 -> 96
/*     */     //   86: astore 6
/*     */     //   88: aload_3
/*     */     //   89: invokevirtual 178	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   92: pop
/*     */     //   93: aload 6
/*     */     //   95: athrow
/*     */     //   96: aload_1
/*     */     //   97: invokeinterface 163 1 0
/*     */     //   102: pop
/*     */     //   103: lload 4
/*     */     //   105: lreturn
/*     */     //   106: astore 7
/*     */     //   108: aload_1
/*     */     //   109: invokeinterface 176 1 0
/*     */     //   114: ifeq +7 -> 121
/*     */     //   117: aload_3
/*     */     //   118: invokestatic 177	io/netty/handler/ssl/SslUtils:zeroout	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   121: aload_3
/*     */     //   122: invokevirtual 178	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   125: pop
/*     */     //   126: goto +13 -> 139
/*     */     //   129: astore 8
/*     */     //   131: aload_3
/*     */     //   132: invokevirtual 178	io/netty/buffer/ByteBuf:release	()Z
/*     */     //   135: pop
/*     */     //   136: aload 8
/*     */     //   138: athrow
/*     */     //   139: aload 7
/*     */     //   141: athrow
/*     */     //   142: astore 9
/*     */     //   144: aload_1
/*     */     //   145: invokeinterface 163 1 0
/*     */     //   150: pop
/*     */     //   151: aload 9
/*     */     //   153: athrow
/*     */     // Line number table:
/*     */     //   Java source line #784	-> byte code offset #0
/*     */     //   Java source line #786	-> byte code offset #7
/*     */     //   Java source line #787	-> byte code offset #14
/*     */     //   Java source line #806	-> byte code offset #22
/*     */     //   Java source line #787	-> byte code offset #29
/*     */     //   Java source line #790	-> byte code offset #31
/*     */     //   Java source line #792	-> byte code offset #42
/*     */     //   Java source line #793	-> byte code offset #56
/*     */     //   Java source line #798	-> byte code offset #65
/*     */     //   Java source line #799	-> byte code offset #74
/*     */     //   Java source line #802	-> byte code offset #78
/*     */     //   Java source line #803	-> byte code offset #83
/*     */     //   Java source line #802	-> byte code offset #86
/*     */     //   Java source line #803	-> byte code offset #93
/*     */     //   Java source line #806	-> byte code offset #96
/*     */     //   Java source line #793	-> byte code offset #103
/*     */     //   Java source line #795	-> byte code offset #106
/*     */     //   Java source line #798	-> byte code offset #108
/*     */     //   Java source line #799	-> byte code offset #117
/*     */     //   Java source line #802	-> byte code offset #121
/*     */     //   Java source line #803	-> byte code offset #126
/*     */     //   Java source line #802	-> byte code offset #129
/*     */     //   Java source line #803	-> byte code offset #136
/*     */     //   Java source line #804	-> byte code offset #139
/*     */     //   Java source line #806	-> byte code offset #142
/*     */     //   Java source line #807	-> byte code offset #151
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	154	0	allocator	ByteBufAllocator
/*     */     //   0	154	1	pem	PemEncoded
/*     */     //   6	43	2	content	ByteBuf
/*     */     //   21	9	3	l1	long
/*     */     //   41	91	3	buffer	ByteBuf
/*     */     //   63	41	4	l2	long
/*     */     //   86	8	6	localObject1	Object
/*     */     //   106	34	7	localObject2	Object
/*     */     //   129	8	8	localObject3	Object
/*     */     //   142	10	9	localObject4	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   65	78	86	finally
/*     */     //   86	88	86	finally
/*     */     //   42	65	106	finally
/*     */     //   106	108	106	finally
/*     */     //   108	121	129	finally
/*     */     //   129	131	129	finally
/*     */     //   0	22	142	finally
/*     */     //   31	96	142	finally
/*     */     //   106	144	142	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */