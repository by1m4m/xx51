/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.internal.tcnative.Buffer;
/*      */ import io.netty.internal.tcnative.SSL;
/*      */ import io.netty.util.AbstractReferenceCounted;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.ResourceLeakDetectorFactory;
/*      */ import io.netty.util.ResourceLeakTracker;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ReadOnlyBufferException;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLHandshakeException;
/*      */ import javax.net.ssl.SSLParameters;
/*      */ import javax.net.ssl.SSLPeerUnverifiedException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSessionBindingEvent;
/*      */ import javax.net.ssl.SSLSessionBindingListener;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ReferenceCountedOpenSslEngine
/*      */   extends SSLEngine
/*      */   implements ReferenceCounted, ApplicationProtocolAccessor
/*      */ {
/*   96 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslEngine.class);
/*      */   
/*   98 */   private static final SSLException BEGIN_HANDSHAKE_ENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
/*      */   
/*  100 */   private static final SSLException HANDSHAKE_ENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "handshake()");
/*      */   
/*  102 */   private static final SSLException RENEGOTIATION_UNSUPPORTED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("renegotiation unsupported"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
/*      */   
/*      */ 
/*  105 */   private static final ResourceLeakDetector<ReferenceCountedOpenSslEngine> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslEngine.class);
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV2 = 0;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV3 = 1;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1 = 2;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_1 = 3;
/*      */   private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_2 = 4;
/*  111 */   private static final int[] OPENSSL_OP_NO_PROTOCOLS = { SSL.SSL_OP_NO_SSLv2, SSL.SSL_OP_NO_SSLv3, SSL.SSL_OP_NO_TLSv1, SSL.SSL_OP_NO_TLSv1_1, SSL.SSL_OP_NO_TLSv1_2 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int DEFAULT_HOSTNAME_VALIDATION_FLAGS = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */   static final int MAX_PLAINTEXT_LENGTH = SSL.SSL_MAX_PLAINTEXT_LENGTH;
/*      */   
/*      */ 
/*      */ 
/*  130 */   private static final int MAX_RECORD_SIZE = SSL.SSL_MAX_RECORD_LENGTH;
/*      */   
/*      */ 
/*  133 */   private static final AtomicIntegerFieldUpdater<ReferenceCountedOpenSslEngine> DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ReferenceCountedOpenSslEngine.class, "destroyed");
/*      */   
/*      */   private static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
/*  136 */   private static final SSLEngineResult NEED_UNWRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*  137 */   private static final SSLEngineResult NEED_UNWRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*  138 */   private static final SSLEngineResult NEED_WRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
/*  139 */   private static final SSLEngineResult NEED_WRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
/*  140 */   private static final SSLEngineResult CLOSED_NOT_HANDSHAKING = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */   
/*      */   private long ssl;
/*      */   
/*      */   private long networkBIO;
/*      */   
/*      */   private boolean certificateSet;
/*      */   
/*      */ 
/*      */   private static enum HandshakeState
/*      */   {
/*  151 */     NOT_STARTED, 
/*      */     
/*      */ 
/*      */ 
/*  155 */     STARTED_IMPLICITLY, 
/*      */     
/*      */ 
/*      */ 
/*  159 */     STARTED_EXPLICITLY, 
/*      */     
/*      */ 
/*      */ 
/*  163 */     FINISHED;
/*      */     
/*      */     private HandshakeState() {} }
/*  166 */   private HandshakeState handshakeState = HandshakeState.NOT_STARTED;
/*      */   
/*      */   private boolean receivedShutdown;
/*      */   
/*      */   private volatile int destroyed;
/*      */   private volatile String applicationProtocol;
/*      */   private final ResourceLeakTracker<ReferenceCountedOpenSslEngine> leak;
/*  173 */   private final AbstractReferenceCounted refCnt = new AbstractReferenceCounted()
/*      */   {
/*      */     public ReferenceCounted touch(Object hint) {
/*  176 */       if (ReferenceCountedOpenSslEngine.this.leak != null) {
/*  177 */         ReferenceCountedOpenSslEngine.this.leak.record(hint);
/*      */       }
/*      */       
/*  180 */       return ReferenceCountedOpenSslEngine.this;
/*      */     }
/*      */     
/*      */     protected void deallocate()
/*      */     {
/*  185 */       ReferenceCountedOpenSslEngine.this.shutdown();
/*  186 */       if (ReferenceCountedOpenSslEngine.this.leak != null) {
/*  187 */         boolean closed = ReferenceCountedOpenSslEngine.this.leak.close(ReferenceCountedOpenSslEngine.this);
/*  188 */         assert (closed);
/*      */       }
/*      */     }
/*      */   };
/*      */   
/*  193 */   private volatile ClientAuth clientAuth = ClientAuth.NONE;
/*      */   
/*      */ 
/*  196 */   private volatile long lastAccessed = -1L;
/*      */   
/*      */   private String endPointIdentificationAlgorithm;
/*      */   
/*      */   private Object algorithmConstraints;
/*      */   
/*      */   private List<String> sniHostNames;
/*      */   
/*      */   private volatile Collection<?> matchers;
/*      */   
/*      */   private boolean isInboundDone;
/*      */   
/*      */   private boolean outboundClosed;
/*      */   
/*      */   final boolean jdkCompatibilityMode;
/*      */   
/*      */   private final boolean clientMode;
/*      */   final ByteBufAllocator alloc;
/*      */   private final OpenSslEngineMap engineMap;
/*      */   private final OpenSslApplicationProtocolNegotiator apn;
/*      */   private final OpenSslSession session;
/*      */   private final Certificate[] localCerts;
/*  218 */   private final ByteBuffer[] singleSrcBuffer = new ByteBuffer[1];
/*  219 */   private final ByteBuffer[] singleDstBuffer = new ByteBuffer[1];
/*      */   
/*      */ 
/*      */ 
/*      */   private final OpenSslKeyMaterialManager keyMaterialManager;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean enableOcsp;
/*      */   
/*      */ 
/*      */ 
/*      */   private int maxWrapOverhead;
/*      */   
/*      */ 
/*      */   private int maxWrapBufferSize;
/*      */   
/*      */ 
/*      */   SSLHandshakeException handshakeException;
/*      */   
/*      */ 
/*      */ 
/*      */   ReferenceCountedOpenSslEngine(ReferenceCountedOpenSslContext context, ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode, boolean leakDetection)
/*      */   {
/*  243 */     super(peerHost, peerPort);
/*  244 */     OpenSsl.ensureAvailability();
/*  245 */     this.alloc = ((ByteBufAllocator)ObjectUtil.checkNotNull(alloc, "alloc"));
/*  246 */     this.apn = ((OpenSslApplicationProtocolNegotiator)context.applicationProtocolNegotiator());
/*  247 */     this.clientMode = context.isClient();
/*  248 */     if ((PlatformDependent.javaVersion() >= 7) && (context.isClient())) {
/*  249 */       this.session = new ExtendedOpenSslSession(new DefaultOpenSslSession(context.sessionContext()))
/*      */       {
/*      */         public List getRequestedServerNames() {
/*  252 */           return Java8SslUtils.getSniHostNames(ReferenceCountedOpenSslEngine.this.sniHostNames);
/*      */         }
/*      */         
/*      */         public List<byte[]> getStatusResponses()
/*      */         {
/*  257 */           byte[] ocspResponse = null;
/*  258 */           if ((ReferenceCountedOpenSslEngine.this.enableOcsp) && (ReferenceCountedOpenSslEngine.this.clientMode)) {
/*  259 */             synchronized (ReferenceCountedOpenSslEngine.this) {
/*  260 */               if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/*  261 */                 ocspResponse = SSL.getOcspResponse(ReferenceCountedOpenSslEngine.this.ssl);
/*      */               }
/*      */             }
/*      */           }
/*  265 */           return ocspResponse == null ? 
/*  266 */             Collections.emptyList() : Collections.singletonList(ocspResponse);
/*      */         }
/*      */       };
/*      */     } else {
/*  270 */       this.session = new DefaultOpenSslSession(context.sessionContext());
/*      */     }
/*  272 */     this.engineMap = context.engineMap;
/*  273 */     this.localCerts = context.keyCertChain;
/*  274 */     this.keyMaterialManager = context.keyMaterialManager();
/*  275 */     this.enableOcsp = context.enableOcsp;
/*  276 */     this.jdkCompatibilityMode = jdkCompatibilityMode;
/*  277 */     Lock readerLock = context.ctxLock.readLock();
/*  278 */     readerLock.lock();
/*      */     try
/*      */     {
/*  281 */       finalSsl = SSL.newSSL(context.ctx, !context.isClient());
/*      */     } finally { long finalSsl;
/*  283 */       readerLock.unlock();
/*      */     }
/*  285 */     synchronized (this) { long finalSsl;
/*  286 */       this.ssl = finalSsl;
/*      */       try {
/*  288 */         this.networkBIO = SSL.bioNewByteBuffer(this.ssl, context.getBioNonApplicationBufferSize());
/*      */         
/*      */ 
/*      */ 
/*  292 */         setClientAuth(this.clientMode ? ClientAuth.NONE : context.clientAuth);
/*      */         
/*  294 */         if (context.protocols != null) {
/*  295 */           setEnabledProtocols(context.protocols);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  300 */         if ((this.clientMode) && (SslUtils.isValidHostNameForSNI(peerHost))) {
/*  301 */           SSL.setTlsExtHostName(this.ssl, peerHost);
/*  302 */           this.sniHostNames = Collections.singletonList(peerHost);
/*      */         }
/*      */         
/*  305 */         if (this.enableOcsp) {
/*  306 */           SSL.enableOcsp(this.ssl);
/*      */         }
/*      */         
/*  309 */         if (!jdkCompatibilityMode) {
/*  310 */           SSL.setMode(this.ssl, SSL.getMode(this.ssl) | SSL.SSL_MODE_ENABLE_PARTIAL_WRITE);
/*      */         }
/*      */         
/*      */ 
/*  314 */         calculateMaxWrapOverhead();
/*      */       } catch (Throwable cause) {
/*  316 */         SSL.freeSSL(this.ssl);
/*  317 */         PlatformDependent.throwException(cause);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  323 */     this.leak = (leakDetection ? leakDetector.track(this) : null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOcspResponse(byte[] response)
/*      */   {
/*  331 */     if (!this.enableOcsp) {
/*  332 */       throw new IllegalStateException("OCSP stapling is not enabled");
/*      */     }
/*      */     
/*  335 */     if (this.clientMode) {
/*  336 */       throw new IllegalStateException("Not a server SSLEngine");
/*      */     }
/*      */     
/*  339 */     synchronized (this) {
/*  340 */       SSL.setOcspResponse(this.ssl, response);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public byte[] getOcspResponse()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 17	io/netty/handler/ssl/ReferenceCountedOpenSslEngine:enableOcsp	Z
/*      */     //   4: ifne +13 -> 17
/*      */     //   7: new 78	java/lang/IllegalStateException
/*      */     //   10: dup
/*      */     //   11: ldc 79
/*      */     //   13: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*      */     //   16: athrow
/*      */     //   17: aload_0
/*      */     //   18: getfield 16	io/netty/handler/ssl/ReferenceCountedOpenSslEngine:clientMode	Z
/*      */     //   21: ifne +13 -> 34
/*      */     //   24: new 78	java/lang/IllegalStateException
/*      */     //   27: dup
/*      */     //   28: ldc 83
/*      */     //   30: invokespecial 80	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*      */     //   33: athrow
/*      */     //   34: aload_0
/*      */     //   35: dup
/*      */     //   36: astore_1
/*      */     //   37: monitorenter
/*      */     //   38: aload_0
/*      */     //   39: getfield 14	io/netty/handler/ssl/ReferenceCountedOpenSslEngine:ssl	J
/*      */     //   42: invokestatic 84	io/netty/internal/tcnative/SSL:getOcspResponse	(J)[B
/*      */     //   45: aload_1
/*      */     //   46: monitorexit
/*      */     //   47: areturn
/*      */     //   48: astore_2
/*      */     //   49: aload_1
/*      */     //   50: monitorexit
/*      */     //   51: aload_2
/*      */     //   52: athrow
/*      */     // Line number table:
/*      */     //   Java source line #349	-> byte code offset #0
/*      */     //   Java source line #350	-> byte code offset #7
/*      */     //   Java source line #353	-> byte code offset #17
/*      */     //   Java source line #354	-> byte code offset #24
/*      */     //   Java source line #357	-> byte code offset #34
/*      */     //   Java source line #358	-> byte code offset #38
/*      */     //   Java source line #359	-> byte code offset #48
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	53	0	this	ReferenceCountedOpenSslEngine
/*      */     //   36	14	1	Ljava/lang/Object;	Object
/*      */     //   48	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   38	47	48	finally
/*      */     //   48	51	48	finally
/*      */   }
/*      */   
/*      */   public final int refCnt()
/*      */   {
/*  364 */     return this.refCnt.refCnt();
/*      */   }
/*      */   
/*      */   public final ReferenceCounted retain()
/*      */   {
/*  369 */     this.refCnt.retain();
/*  370 */     return this;
/*      */   }
/*      */   
/*      */   public final ReferenceCounted retain(int increment)
/*      */   {
/*  375 */     this.refCnt.retain(increment);
/*  376 */     return this;
/*      */   }
/*      */   
/*      */   public final ReferenceCounted touch()
/*      */   {
/*  381 */     this.refCnt.touch();
/*  382 */     return this;
/*      */   }
/*      */   
/*      */   public final ReferenceCounted touch(Object hint)
/*      */   {
/*  387 */     this.refCnt.touch(hint);
/*  388 */     return this;
/*      */   }
/*      */   
/*      */   public final boolean release()
/*      */   {
/*  393 */     return this.refCnt.release();
/*      */   }
/*      */   
/*      */   public final boolean release(int decrement)
/*      */   {
/*  398 */     return this.refCnt.release(decrement);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final synchronized SSLSession getHandshakeSession()
/*      */   {
/*  407 */     switch (this.handshakeState) {
/*      */     case NOT_STARTED: 
/*      */     case FINISHED: 
/*  410 */       return null;
/*      */     }
/*  412 */     return this.session;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final synchronized long sslPointer()
/*      */   {
/*  422 */     return this.ssl;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final synchronized void shutdown()
/*      */   {
/*  429 */     if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
/*  430 */       this.engineMap.remove(this.ssl);
/*  431 */       SSL.freeSSL(this.ssl);
/*  432 */       this.ssl = (this.networkBIO = 0L);
/*      */       
/*  434 */       this.isInboundDone = (this.outboundClosed = 1);
/*      */     }
/*      */     
/*      */ 
/*  438 */     SSL.clearError();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writePlaintextData(ByteBuffer src, int len)
/*      */   {
/*  447 */     int pos = src.position();
/*  448 */     int limit = src.limit();
/*      */     
/*      */ 
/*  451 */     if (src.isDirect()) {
/*  452 */       int sslWrote = SSL.writeToSSL(this.ssl, bufferAddress(src) + pos, len);
/*  453 */       if (sslWrote > 0) {
/*  454 */         src.position(pos + sslWrote);
/*      */       }
/*      */     } else {
/*  457 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  459 */         src.limit(pos + len);
/*      */         
/*  461 */         buf.setBytes(0, src);
/*  462 */         src.limit(limit);
/*      */         
/*  464 */         int sslWrote = SSL.writeToSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
/*  465 */         if (sslWrote > 0) {
/*  466 */           src.position(pos + sslWrote);
/*      */         } else {
/*  468 */           src.position(pos);
/*      */         }
/*      */       } finally {
/*  471 */         buf.release();
/*      */       } }
/*      */     int sslWrote;
/*  474 */     return sslWrote;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ByteBuf writeEncryptedData(ByteBuffer src, int len)
/*      */   {
/*  481 */     int pos = src.position();
/*  482 */     if (src.isDirect()) {
/*  483 */       SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(src) + pos, len, false);
/*      */     } else {
/*  485 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  487 */         int limit = src.limit();
/*  488 */         src.limit(pos + len);
/*  489 */         buf.writeBytes(src);
/*      */         
/*  491 */         src.position(pos);
/*  492 */         src.limit(limit);
/*      */         
/*  494 */         SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(buf), len, false);
/*  495 */         return buf;
/*      */       } catch (Throwable cause) {
/*  497 */         buf.release();
/*  498 */         PlatformDependent.throwException(cause);
/*      */       }
/*      */     }
/*  501 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int readPlaintextData(ByteBuffer dst)
/*      */   {
/*  509 */     int pos = dst.position();
/*  510 */     if (dst.isDirect()) {
/*  511 */       int sslRead = SSL.readFromSSL(this.ssl, bufferAddress(dst) + pos, dst.limit() - pos);
/*  512 */       if (sslRead > 0) {
/*  513 */         dst.position(pos + sslRead);
/*      */       }
/*      */     } else {
/*  516 */       int limit = dst.limit();
/*  517 */       int len = Math.min(maxEncryptedPacketLength0(), limit - pos);
/*  518 */       ByteBuf buf = this.alloc.directBuffer(len);
/*      */       try {
/*  520 */         int sslRead = SSL.readFromSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
/*  521 */         if (sslRead > 0) {
/*  522 */           dst.limit(pos + sslRead);
/*  523 */           buf.getBytes(buf.readerIndex(), dst);
/*  524 */           dst.limit(limit);
/*      */         }
/*      */       } finally {
/*  527 */         buf.release();
/*      */       }
/*      */     }
/*      */     int sslRead;
/*  531 */     return sslRead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final synchronized int maxWrapOverhead()
/*      */   {
/*  538 */     return this.maxWrapOverhead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final synchronized int maxEncryptedPacketLength()
/*      */   {
/*  545 */     return maxEncryptedPacketLength0();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final int maxEncryptedPacketLength0()
/*      */   {
/*  553 */     return this.maxWrapOverhead + MAX_PLAINTEXT_LENGTH;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int calculateMaxLengthForWrap(int plaintextLength, int numComponents)
/*      */   {
/*  562 */     return (int)Math.min(this.maxWrapBufferSize, plaintextLength + this.maxWrapOverhead * numComponents);
/*      */   }
/*      */   
/*      */   final synchronized int sslPending() {
/*  566 */     return sslPending0();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void calculateMaxWrapOverhead()
/*      */   {
/*  573 */     this.maxWrapOverhead = SSL.getMaxWrapOverhead(this.ssl);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  578 */     this.maxWrapBufferSize = (this.jdkCompatibilityMode ? maxEncryptedPacketLength0() : maxEncryptedPacketLength0() << 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int sslPending0()
/*      */   {
/*  586 */     return this.handshakeState != HandshakeState.FINISHED ? 0 : SSL.sslPending(this.ssl);
/*      */   }
/*      */   
/*      */   private boolean isBytesAvailableEnoughForWrap(int bytesAvailable, int plaintextLength, int numComponents) {
/*  590 */     return bytesAvailable - this.maxWrapOverhead * numComponents >= plaintextLength;
/*      */   }
/*      */   
/*      */ 
/*      */   public final SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int length, ByteBuffer dst)
/*      */     throws SSLException
/*      */   {
/*  597 */     if (srcs == null) {
/*  598 */       throw new IllegalArgumentException("srcs is null");
/*      */     }
/*  600 */     if (dst == null) {
/*  601 */       throw new IllegalArgumentException("dst is null");
/*      */     }
/*      */     
/*  604 */     if ((offset >= srcs.length) || (offset + length > srcs.length)) {
/*  605 */       throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  610 */     if (dst.isReadOnly()) {
/*  611 */       throw new ReadOnlyBufferException();
/*      */     }
/*      */     
/*  614 */     synchronized (this) {
/*  615 */       if (isOutboundDone())
/*      */       {
/*  617 */         return (isInboundDone()) || (isDestroyed()) ? CLOSED_NOT_HANDSHAKING : NEED_UNWRAP_CLOSED;
/*      */       }
/*      */       
/*  620 */       int bytesProduced = 0;
/*  621 */       ByteBuf bioReadCopyBuf = null;
/*      */       try
/*      */       {
/*  624 */         if (dst.isDirect()) {
/*  625 */           SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(dst) + dst.position(), dst.remaining(), true);
/*      */         }
/*      */         else {
/*  628 */           bioReadCopyBuf = this.alloc.directBuffer(dst.remaining());
/*  629 */           SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(bioReadCopyBuf), bioReadCopyBuf.writableBytes(), true);
/*      */         }
/*      */         
/*      */ 
/*  633 */         int bioLengthBefore = SSL.bioLengthByteBuffer(this.networkBIO);
/*      */         
/*      */ 
/*  636 */         if (this.outboundClosed)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  642 */           if (!isBytesAvailableEnoughForWrap(dst.remaining(), 2, 1)) {
/*  643 */             localSSLEngineResult1 = new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), 0, 0);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  643 */             return localSSLEngineResult1;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  648 */           bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  649 */           if (bytesProduced <= 0) {
/*  650 */             localSSLEngineResult1 = newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  650 */             return localSSLEngineResult1;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  655 */           if (!doSSLShutdown()) {
/*  656 */             localSSLEngineResult1 = newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, bytesProduced);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  656 */             return localSSLEngineResult1;
/*      */           }
/*  658 */           bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*  659 */           SSLEngineResult localSSLEngineResult1 = newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */           SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */           if (bioReadCopyBuf == null) {
/*  832 */             dst.position(dst.position() + bytesProduced);
/*      */           } else {
/*  834 */             assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */             
/*  836 */             dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */             bioReadCopyBuf.release();
/*      */           }
/*  659 */           return localSSLEngineResult1;
/*      */         }
/*      */         
/*      */ 
/*  663 */         SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */         
/*  665 */         if (this.handshakeState != HandshakeState.FINISHED) {
/*  666 */           if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY)
/*      */           {
/*  668 */             this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
/*      */           }
/*      */           
/*      */ 
/*  672 */           bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*      */           SSLEngineResult localSSLEngineResult2;
/*  674 */           if ((bytesProduced > 0) && (this.handshakeException != null))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  683 */             localSSLEngineResult2 = newResult(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  683 */             return localSSLEngineResult2;
/*      */           }
/*      */           
/*  686 */           status = handshake();
/*      */           
/*      */ 
/*      */ 
/*  690 */           bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*      */           
/*  692 */           if (bytesProduced > 0)
/*      */           {
/*      */ 
/*      */ 
/*  696 */             localSSLEngineResult2 = newResult(mayFinishHandshake(status != SSLEngineResult.HandshakeStatus.FINISHED ? 
/*      */             
/*  698 */               getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO)) : bytesProduced == bioLengthBefore ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.FINISHED), 0, bytesProduced);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  696 */             return localSSLEngineResult2;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  702 */           if (status == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
/*      */           {
/*  704 */             localSSLEngineResult2 = isOutboundDone() ? NEED_UNWRAP_CLOSED : NEED_UNWRAP_OK;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  704 */             return localSSLEngineResult2;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  709 */           if (this.outboundClosed) {
/*  710 */             bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  711 */             localSSLEngineResult2 = newResultMayFinishHandshake(status, 0, bytesProduced);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  711 */             return localSSLEngineResult2;
/*      */           }
/*      */         }
/*      */         
/*  715 */         int endOffset = offset + length;
/*  716 */         if (this.jdkCompatibilityMode) {
/*  717 */           int srcsLen = 0;
/*  718 */           for (int i = offset; i < endOffset; i++) {
/*  719 */             ByteBuffer src = srcs[i];
/*  720 */             if (src == null) {
/*  721 */               throw new IllegalArgumentException("srcs[" + i + "] is null");
/*      */             }
/*  723 */             if (srcsLen != MAX_PLAINTEXT_LENGTH)
/*      */             {
/*      */ 
/*      */ 
/*  727 */               srcsLen += src.remaining();
/*  728 */               if ((srcsLen > MAX_PLAINTEXT_LENGTH) || (srcsLen < 0))
/*      */               {
/*      */ 
/*      */ 
/*  732 */                 srcsLen = MAX_PLAINTEXT_LENGTH;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  738 */           if (!isBytesAvailableEnoughForWrap(dst.remaining(), srcsLen, 1)) {
/*  739 */             i = new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), 0, 0);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */             SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */             if (bioReadCopyBuf == null) {
/*  832 */               dst.position(dst.position() + bytesProduced);
/*      */             } else {
/*  834 */               assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */               
/*  836 */               dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */               bioReadCopyBuf.release();
/*      */             }
/*  739 */             return i;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  744 */         int bytesConsumed = 0;
/*      */         
/*  746 */         bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
/*  747 */         for (; offset < endOffset; offset++) {
/*  748 */           src = srcs[offset];
/*  749 */           int remaining = src.remaining();
/*  750 */           if (remaining != 0)
/*      */           {
/*      */             int bytesWritten;
/*      */             SSLEngineResult localSSLEngineResult3;
/*      */             int bytesWritten;
/*  755 */             if (this.jdkCompatibilityMode)
/*      */             {
/*      */ 
/*      */ 
/*  759 */               bytesWritten = writePlaintextData(src, Math.min(remaining, MAX_PLAINTEXT_LENGTH - bytesConsumed));
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  764 */               int availableCapacityForWrap = dst.remaining() - bytesProduced - this.maxWrapOverhead;
/*  765 */               if (availableCapacityForWrap <= 0) {
/*  766 */                 localSSLEngineResult3 = new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                 SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                 if (bioReadCopyBuf == null) {
/*  832 */                   dst.position(dst.position() + bytesProduced);
/*      */                 } else {
/*  834 */                   assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                   
/*  836 */                   dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                   bioReadCopyBuf.release();
/*      */                 }
/*  766 */                 return localSSLEngineResult3;
/*      */               }
/*      */               
/*  769 */               bytesWritten = writePlaintextData(src, Math.min(remaining, availableCapacityForWrap));
/*      */             }
/*      */             
/*  772 */             if (bytesWritten > 0) {
/*  773 */               bytesConsumed += bytesWritten;
/*      */               
/*      */ 
/*  776 */               int pendingNow = SSL.bioLengthByteBuffer(this.networkBIO);
/*  777 */               bytesProduced += bioLengthBefore - pendingNow;
/*  778 */               bioLengthBefore = pendingNow;
/*      */               
/*  780 */               if ((this.jdkCompatibilityMode) || (bytesProduced == dst.remaining())) {
/*  781 */                 localSSLEngineResult3 = newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                 SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                 if (bioReadCopyBuf == null) {
/*  832 */                   dst.position(dst.position() + bytesProduced);
/*      */                 } else {
/*  834 */                   assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                   
/*  836 */                   dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                   bioReadCopyBuf.release();
/*      */                 }
/*  781 */                 return localSSLEngineResult3;
/*      */               }
/*      */             } else {
/*  784 */               int sslError = SSL.getError(this.ssl, bytesWritten);
/*  785 */               Object hs; if (sslError == SSL.SSL_ERROR_ZERO_RETURN)
/*      */               {
/*  787 */                 if (!this.receivedShutdown) {
/*  788 */                   closeAll();
/*      */                   
/*  790 */                   bytesProduced += bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*  795 */                   hs = mayFinishHandshake(status != SSLEngineResult.HandshakeStatus.FINISHED ? 
/*      */                   
/*  797 */                     getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO)) : bytesProduced == dst.remaining() ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.FINISHED);
/*      */                   
/*  799 */                   SSLEngineResult localSSLEngineResult4 = newResult((SSLEngineResult.HandshakeStatus)hs, bytesConsumed, bytesProduced);
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                   SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                   if (bioReadCopyBuf == null) {
/*  832 */                     dst.position(dst.position() + bytesProduced);
/*      */                   } else {
/*  834 */                     assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                     
/*  836 */                     dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                     bioReadCopyBuf.release();
/*      */                   }
/*  799 */                   return localSSLEngineResult4;
/*      */                 }
/*      */                 
/*  802 */                 hs = newResult(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, bytesConsumed, bytesProduced);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                 SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                 if (bioReadCopyBuf == null) {
/*  832 */                   dst.position(dst.position() + bytesProduced);
/*      */                 } else {
/*  834 */                   assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                   
/*  836 */                   dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                   bioReadCopyBuf.release();
/*      */                 }
/*  802 */                 return (SSLEngineResult)hs; }
/*  803 */               if (sslError == SSL.SSL_ERROR_WANT_READ)
/*      */               {
/*      */ 
/*      */ 
/*  807 */                 hs = newResult(SSLEngineResult.HandshakeStatus.NEED_UNWRAP, bytesConsumed, bytesProduced);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                 SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                 if (bioReadCopyBuf == null) {
/*  832 */                   dst.position(dst.position() + bytesProduced);
/*      */                 } else {
/*  834 */                   assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                   
/*  836 */                   dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                   bioReadCopyBuf.release();
/*      */                 }
/*  807 */                 return (SSLEngineResult)hs; }
/*  808 */               if (sslError == SSL.SSL_ERROR_WANT_WRITE)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  821 */                 hs = newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */                 SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */                 if (bioReadCopyBuf == null) {
/*  832 */                   dst.position(dst.position() + bytesProduced);
/*      */                 } else {
/*  834 */                   assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */                   
/*  836 */                   dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */                   bioReadCopyBuf.release();
/*      */                 }
/*  821 */                 return (SSLEngineResult)hs;
/*      */               }
/*      */               
/*  824 */               throw shutdownWithError("SSL_write", sslError);
/*      */             }
/*      */           }
/*      */         }
/*  828 */         ByteBuffer src = newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
/*      */         
/*  830 */         SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */         if (bioReadCopyBuf == null) {
/*  832 */           dst.position(dst.position() + bytesProduced);
/*      */         } else {
/*  834 */           assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */           
/*  836 */           dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */           bioReadCopyBuf.release();
/*      */         }
/*  828 */         return src;
/*      */       } finally {
/*  830 */         SSL.bioClearByteBuffer(this.networkBIO);
/*  831 */         if (bioReadCopyBuf == null) {
/*  832 */           dst.position(dst.position() + bytesProduced);
/*      */         } else {
/*  834 */           assert (bioReadCopyBuf.readableBytes() <= dst.remaining()) : ("The destination buffer " + dst + " didn't have enough remaining space to hold the encrypted content in " + bioReadCopyBuf);
/*      */           
/*  836 */           dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
/*  837 */           bioReadCopyBuf.release();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private SSLEngineResult newResult(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) {
/*  844 */     return newResult(SSLEngineResult.Status.OK, hs, bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SSLEngineResult newResult(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced)
/*      */   {
/*  852 */     if (isOutboundDone()) {
/*  853 */       if (isInboundDone())
/*      */       {
/*  855 */         hs = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */         
/*      */ 
/*  858 */         shutdown();
/*      */       }
/*  860 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, hs, bytesConsumed, bytesProduced);
/*      */     }
/*  862 */     return new SSLEngineResult(status, hs, bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */   private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException
/*      */   {
/*  867 */     return newResult(mayFinishHandshake(hs != SSLEngineResult.HandshakeStatus.FINISHED ? getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */ 
/*      */   private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced)
/*      */     throws SSLException
/*      */   {
/*  874 */     return newResult(status, mayFinishHandshake(hs != SSLEngineResult.HandshakeStatus.FINISHED ? getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SSLException shutdownWithError(String operations, int sslError)
/*      */   {
/*  882 */     return shutdownWithError(operations, sslError, SSL.getLastErrorNumber());
/*      */   }
/*      */   
/*      */   private SSLException shutdownWithError(String operation, int sslError, int error) {
/*  886 */     String errorString = SSL.getErrorString(error);
/*  887 */     if (logger.isDebugEnabled()) {
/*  888 */       logger.debug("{} failed with {}: OpenSSL error: {} {}", new Object[] { operation, 
/*  889 */         Integer.valueOf(sslError), Integer.valueOf(error), errorString });
/*      */     }
/*      */     
/*      */ 
/*  893 */     shutdown();
/*  894 */     if (this.handshakeState == HandshakeState.FINISHED) {
/*  895 */       return new SSLException(errorString);
/*      */     }
/*  897 */     return new SSLHandshakeException(errorString);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final SSLEngineResult unwrap(ByteBuffer[] srcs, int srcsOffset, int srcsLength, ByteBuffer[] dsts, int dstsOffset, int dstsLength)
/*      */     throws SSLException
/*      */   {
/*  905 */     if (srcs == null) {
/*  906 */       throw new NullPointerException("srcs");
/*      */     }
/*  908 */     if ((srcsOffset >= srcs.length) || (srcsOffset + srcsLength > srcs.length))
/*      */     {
/*  910 */       throw new IndexOutOfBoundsException("offset: " + srcsOffset + ", length: " + srcsLength + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))");
/*      */     }
/*      */     
/*      */ 
/*  914 */     if (dsts == null) {
/*  915 */       throw new IllegalArgumentException("dsts is null");
/*      */     }
/*  917 */     if ((dstsOffset >= dsts.length) || (dstsOffset + dstsLength > dsts.length)) {
/*  918 */       throw new IndexOutOfBoundsException("offset: " + dstsOffset + ", length: " + dstsLength + " (expected: offset <= offset + length <= dsts.length (" + dsts.length + "))");
/*      */     }
/*      */     
/*      */ 
/*  922 */     long capacity = 0L;
/*  923 */     int dstsEndOffset = dstsOffset + dstsLength;
/*  924 */     for (int i = dstsOffset; i < dstsEndOffset; i++) {
/*  925 */       ByteBuffer dst = dsts[i];
/*  926 */       if (dst == null) {
/*  927 */         throw new IllegalArgumentException("dsts[" + i + "] is null");
/*      */       }
/*  929 */       if (dst.isReadOnly()) {
/*  930 */         throw new ReadOnlyBufferException();
/*      */       }
/*  932 */       capacity += dst.remaining();
/*      */     }
/*      */     
/*  935 */     int srcsEndOffset = srcsOffset + srcsLength;
/*  936 */     long len = 0L;
/*  937 */     for (int i = srcsOffset; i < srcsEndOffset; i++) {
/*  938 */       ByteBuffer src = srcs[i];
/*  939 */       if (src == null) {
/*  940 */         throw new IllegalArgumentException("srcs[" + i + "] is null");
/*      */       }
/*  942 */       len += src.remaining();
/*      */     }
/*      */     
/*  945 */     synchronized (this) {
/*  946 */       if (isInboundDone()) {
/*  947 */         return (isOutboundDone()) || (isDestroyed()) ? CLOSED_NOT_HANDSHAKING : NEED_WRAP_CLOSED;
/*      */       }
/*      */       
/*  950 */       SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */       
/*  952 */       if (this.handshakeState != HandshakeState.FINISHED) {
/*  953 */         if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY)
/*      */         {
/*  955 */           this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
/*      */         }
/*      */         
/*  958 */         status = handshake();
/*  959 */         if (status == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
/*  960 */           return NEED_WRAP_OK;
/*      */         }
/*      */         
/*  963 */         if (this.isInboundDone) {
/*  964 */           return NEED_WRAP_CLOSED;
/*      */         }
/*      */       }
/*      */       
/*  968 */       int sslPending = sslPending0();
/*      */       
/*      */ 
/*      */       int packetLength;
/*      */       
/*      */ 
/*  974 */       if (this.jdkCompatibilityMode) {
/*  975 */         if (len < 5L) {
/*  976 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
/*      */         }
/*      */         
/*  979 */         int packetLength = SslUtils.getEncryptedPacketLength(srcs, srcsOffset);
/*  980 */         if (packetLength == -2) {
/*  981 */           throw new NotSslRecordException("not an SSL/TLS record");
/*      */         }
/*      */         
/*  984 */         int packetLengthDataOnly = packetLength - 5;
/*  985 */         if (packetLengthDataOnly > capacity)
/*      */         {
/*      */ 
/*  988 */           if (packetLengthDataOnly > MAX_RECORD_SIZE)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  995 */             throw new SSLException("Illegal packet length: " + packetLengthDataOnly + " > " + this.session.getApplicationBufferSize());
/*      */           }
/*  997 */           this.session.tryExpandApplicationBufferSize(packetLengthDataOnly);
/*      */           
/*  999 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
/*      */         }
/*      */         
/* 1002 */         if (len < packetLength)
/*      */         {
/*      */ 
/* 1005 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0); }
/*      */       } else {
/* 1007 */         if ((len == 0L) && (sslPending <= 0))
/* 1008 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
/* 1009 */         if (capacity == 0L) {
/* 1010 */           return newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
/*      */         }
/* 1012 */         packetLength = (int)Math.min(2147483647L, len);
/*      */       }
/*      */       
/*      */ 
/* 1016 */       assert (srcsOffset < srcsEndOffset);
/*      */       
/*      */ 
/* 1019 */       assert (capacity > 0L);
/*      */       
/*      */ 
/* 1022 */       int bytesProduced = 0;
/* 1023 */       int bytesConsumed = 0;
/*      */       try
/*      */       {
/*      */         for (;;) {
/* 1027 */           ByteBuffer src = srcs[srcsOffset];
/* 1028 */           int remaining = src.remaining();
/*      */           int pendingEncryptedBytes;
/*      */           ByteBuf bioWriteCopyBuf;
/* 1031 */           if (remaining == 0) {
/* 1032 */             if (sslPending <= 0)
/*      */             {
/*      */ 
/* 1035 */               srcsOffset++; if (srcsOffset < srcsEndOffset) continue;
/* 1036 */               break;
/*      */             }
/*      */             
/*      */ 
/* 1040 */             ByteBuf bioWriteCopyBuf = null;
/* 1041 */             int pendingEncryptedBytes = SSL.bioLengthByteBuffer(this.networkBIO);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1046 */             pendingEncryptedBytes = Math.min(packetLength, remaining);
/* 1047 */             bioWriteCopyBuf = writeEncryptedData(src, pendingEncryptedBytes);
/*      */           }
/*      */           try {
/*      */             for (;;) {
/* 1051 */               ByteBuffer dst = dsts[dstsOffset];
/* 1052 */               if (!dst.hasRemaining())
/*      */               {
/* 1054 */                 dstsOffset++; if (dstsOffset >= dstsEndOffset)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */                   if (bioWriteCopyBuf == null) break label1248;
/* 1112 */                   bioWriteCopyBuf.release();
/*      */                   break label1248;
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 1060 */                 int bytesRead = readPlaintextData(dst);
/*      */                 
/*      */ 
/*      */ 
/* 1064 */                 int localBytesConsumed = pendingEncryptedBytes - SSL.bioLengthByteBuffer(this.networkBIO);
/* 1065 */                 bytesConsumed += localBytesConsumed;
/* 1066 */                 packetLength -= localBytesConsumed;
/* 1067 */                 pendingEncryptedBytes -= localBytesConsumed;
/* 1068 */                 src.position(src.position() + localBytesConsumed);
/*      */                 
/* 1070 */                 if (bytesRead > 0) {
/* 1071 */                   bytesProduced += bytesRead;
/*      */                   
/* 1073 */                   if (!dst.hasRemaining()) {
/* 1074 */                     sslPending = sslPending0();
/*      */                     
/* 1076 */                     dstsOffset++; if (dstsOffset >= dstsEndOffset)
/*      */                     {
/*      */ 
/* 1079 */                       SSLEngineResult localSSLEngineResult1 = sslPending > 0 ? newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced) : newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */                       if (bioWriteCopyBuf != null) {
/* 1112 */                         bioWriteCopyBuf.release();
/*      */                       }
/*      */                       
/*      */ 
/*      */ 
/* 1117 */                       SSL.bioClearByteBuffer(this.networkBIO);
/* 1118 */                       rejectRemoteInitiatedRenegotiation();return localSSLEngineResult1;
/*      */                     }
/*      */                   }
/* 1082 */                   else if ((packetLength == 0) || (this.jdkCompatibilityMode))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */                     if (bioWriteCopyBuf == null) break label1248;
/* 1112 */                     bioWriteCopyBuf.release();
/*      */                     break label1248;
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/* 1088 */                   int sslError = SSL.getError(this.ssl, bytesRead);
/* 1089 */                   if ((sslError == SSL.SSL_ERROR_WANT_READ) || (sslError == SSL.SSL_ERROR_WANT_WRITE)) {
/*      */                     break;
/*      */                   }
/*      */                   
/* 1093 */                   if (sslError == SSL.SSL_ERROR_ZERO_RETURN)
/*      */                   {
/* 1095 */                     if (!this.receivedShutdown) {
/* 1096 */                       closeAll();
/*      */                     }
/* 1098 */                     localSSLEngineResult2 = newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */                     if (bioWriteCopyBuf != null) {
/* 1112 */                       bioWriteCopyBuf.release();
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/* 1117 */                     SSL.bioClearByteBuffer(this.networkBIO);
/* 1118 */                     rejectRemoteInitiatedRenegotiation();return localSSLEngineResult2;
/*      */                   }
/* 1101 */                   SSLEngineResult localSSLEngineResult2 = sslReadErrorResult(sslError, SSL.getLastErrorNumber(), bytesConsumed, bytesProduced);
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */                   if (bioWriteCopyBuf != null) {
/* 1112 */                     bioWriteCopyBuf.release();
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/* 1117 */                   SSL.bioClearByteBuffer(this.networkBIO);
/* 1118 */                   rejectRemoteInitiatedRenegotiation();return localSSLEngineResult2;
/*      */                 }
/*      */               }
/*      */             }
/* 1107 */             srcsOffset++; if (srcsOffset >= srcsEndOffset)
/*      */             {
/*      */ 
/*      */ 
/* 1111 */               if (bioWriteCopyBuf == null) break;
/* 1112 */               bioWriteCopyBuf.release(); break;
/*      */             }
/*      */           }
/*      */           finally
/*      */           {
/* 1111 */             if (bioWriteCopyBuf != null)
/* 1112 */               bioWriteCopyBuf.release();
/*      */           }
/*      */         }
/*      */       } finally {
/*      */         label1248:
/* 1117 */         SSL.bioClearByteBuffer(this.networkBIO);
/* 1118 */         rejectRemoteInitiatedRenegotiation();
/*      */       }
/*      */       
/*      */ 
/* 1122 */       if ((!this.receivedShutdown) && ((SSL.getShutdown(this.ssl) & SSL.SSL_RECEIVED_SHUTDOWN) == SSL.SSL_RECEIVED_SHUTDOWN)) {
/* 1123 */         closeAll();
/*      */       }
/*      */       
/* 1126 */       return newResultMayFinishHandshake(isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SSLEngineResult sslReadErrorResult(int error, int stackError, int bytesConsumed, int bytesProduced)
/*      */     throws SSLException
/*      */   {
/* 1136 */     if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
/* 1137 */       if ((this.handshakeException == null) && (this.handshakeState != HandshakeState.FINISHED))
/*      */       {
/*      */ 
/* 1140 */         this.handshakeException = new SSLHandshakeException(SSL.getErrorString(stackError));
/*      */       }
/*      */       
/*      */ 
/* 1144 */       SSL.clearError();
/* 1145 */       return new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, bytesConsumed, bytesProduced);
/*      */     }
/* 1147 */     throw shutdownWithError("SSL_read", error, stackError);
/*      */   }
/*      */   
/*      */   private void closeAll() throws SSLException {
/* 1151 */     this.receivedShutdown = true;
/* 1152 */     closeOutbound();
/* 1153 */     closeInbound();
/*      */   }
/*      */   
/*      */ 
/*      */   private void rejectRemoteInitiatedRenegotiation()
/*      */     throws SSLHandshakeException
/*      */   {
/* 1160 */     if ((!isDestroyed()) && (SSL.getHandshakeCount(this.ssl) > 1))
/*      */     {
/*      */ 
/* 1163 */       shutdown();
/* 1164 */       throw new SSLHandshakeException("remote-initiated renegotiation not allowed");
/*      */     }
/*      */   }
/*      */   
/*      */   public final SSLEngineResult unwrap(ByteBuffer[] srcs, ByteBuffer[] dsts) throws SSLException {
/* 1169 */     return unwrap(srcs, 0, srcs.length, dsts, 0, dsts.length);
/*      */   }
/*      */   
/*      */   private ByteBuffer[] singleSrcBuffer(ByteBuffer src) {
/* 1173 */     this.singleSrcBuffer[0] = src;
/* 1174 */     return this.singleSrcBuffer;
/*      */   }
/*      */   
/*      */   private void resetSingleSrcBuffer() {
/* 1178 */     this.singleSrcBuffer[0] = null;
/*      */   }
/*      */   
/*      */   private ByteBuffer[] singleDstBuffer(ByteBuffer src) {
/* 1182 */     this.singleDstBuffer[0] = src;
/* 1183 */     return this.singleDstBuffer;
/*      */   }
/*      */   
/*      */   private void resetSingleDstBuffer() {
/* 1187 */     this.singleDstBuffer[0] = null;
/*      */   }
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length) throws SSLException
/*      */   {
/*      */     try
/*      */     {
/* 1194 */       return unwrap(singleSrcBuffer(src), 0, 1, dsts, offset, length);
/*      */     } finally {
/* 1196 */       resetSingleSrcBuffer();
/*      */     }
/*      */   }
/*      */   
/*      */   public final synchronized SSLEngineResult wrap(ByteBuffer src, ByteBuffer dst) throws SSLException
/*      */   {
/*      */     try {
/* 1203 */       return wrap(singleSrcBuffer(src), dst);
/*      */     } finally {
/* 1205 */       resetSingleSrcBuffer();
/*      */     }
/*      */   }
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer dst) throws SSLException
/*      */   {
/*      */     try {
/* 1212 */       return unwrap(singleSrcBuffer(src), singleDstBuffer(dst));
/*      */     } finally {
/* 1214 */       resetSingleSrcBuffer();
/* 1215 */       resetSingleDstBuffer();
/*      */     }
/*      */   }
/*      */   
/*      */   public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts) throws SSLException
/*      */   {
/*      */     try {
/* 1222 */       return unwrap(singleSrcBuffer(src), dsts);
/*      */     } finally {
/* 1224 */       resetSingleSrcBuffer();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final Runnable getDelegatedTask()
/*      */   {
/* 1232 */     return null;
/*      */   }
/*      */   
/*      */   public final synchronized void closeInbound() throws SSLException
/*      */   {
/* 1237 */     if (this.isInboundDone) {
/* 1238 */       return;
/*      */     }
/*      */     
/* 1241 */     this.isInboundDone = true;
/*      */     
/* 1243 */     if (isOutboundDone())
/*      */     {
/*      */ 
/* 1246 */       shutdown();
/*      */     }
/*      */     
/* 1249 */     if ((this.handshakeState != HandshakeState.NOT_STARTED) && (!this.receivedShutdown)) {
/* 1250 */       throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public final synchronized boolean isInboundDone()
/*      */   {
/* 1257 */     return this.isInboundDone;
/*      */   }
/*      */   
/*      */   public final synchronized void closeOutbound()
/*      */   {
/* 1262 */     if (this.outboundClosed) {
/* 1263 */       return;
/*      */     }
/*      */     
/* 1266 */     this.outboundClosed = true;
/*      */     
/* 1268 */     if ((this.handshakeState != HandshakeState.NOT_STARTED) && (!isDestroyed())) {
/* 1269 */       int mode = SSL.getShutdown(this.ssl);
/* 1270 */       if ((mode & SSL.SSL_SENT_SHUTDOWN) != SSL.SSL_SENT_SHUTDOWN) {
/* 1271 */         doSSLShutdown();
/*      */       }
/*      */     }
/*      */     else {
/* 1275 */       shutdown();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean doSSLShutdown()
/*      */   {
/* 1284 */     if (SSL.isInInit(this.ssl) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1289 */       return false;
/*      */     }
/* 1291 */     int err = SSL.shutdownSSL(this.ssl);
/* 1292 */     if (err < 0) {
/* 1293 */       int sslErr = SSL.getError(this.ssl, err);
/* 1294 */       if ((sslErr == SSL.SSL_ERROR_SYSCALL) || (sslErr == SSL.SSL_ERROR_SSL)) {
/* 1295 */         if (logger.isDebugEnabled()) {
/* 1296 */           int error = SSL.getLastErrorNumber();
/* 1297 */           logger.debug("SSL_shutdown failed: OpenSSL error: {} {}", Integer.valueOf(error), SSL.getErrorString(error));
/*      */         }
/*      */         
/* 1300 */         shutdown();
/* 1301 */         return false;
/*      */       }
/* 1303 */       SSL.clearError();
/*      */     }
/* 1305 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final synchronized boolean isOutboundDone()
/*      */   {
/* 1312 */     return (this.outboundClosed) && ((this.networkBIO == 0L) || (SSL.bioLengthNonApplication(this.networkBIO) == 0));
/*      */   }
/*      */   
/*      */   public final String[] getSupportedCipherSuites()
/*      */   {
/* 1317 */     return (String[])OpenSsl.AVAILABLE_CIPHER_SUITES.toArray(new String[0]);
/*      */   }
/*      */   
/*      */   public final String[] getEnabledCipherSuites()
/*      */   {
/*      */     String[] enabled;
/* 1323 */     synchronized (this) { String[] enabled;
/* 1324 */       if (!isDestroyed()) {
/* 1325 */         enabled = SSL.getCiphers(this.ssl);
/*      */       } else
/* 1327 */         return EmptyArrays.EMPTY_STRINGS;
/*      */     }
/*      */     String[] enabled;
/* 1330 */     if (enabled == null) {
/* 1331 */       return EmptyArrays.EMPTY_STRINGS;
/*      */     }
/* 1333 */     synchronized (this) {
/* 1334 */       for (int i = 0; i < enabled.length; i++) {
/* 1335 */         String mapped = toJavaCipherSuite(enabled[i]);
/* 1336 */         if (mapped != null) {
/* 1337 */           enabled[i] = mapped;
/*      */         }
/*      */       }
/*      */     }
/* 1341 */     return enabled;
/*      */   }
/*      */   
/*      */ 
/*      */   public final void setEnabledCipherSuites(String[] cipherSuites)
/*      */   {
/* 1347 */     ObjectUtil.checkNotNull(cipherSuites, "cipherSuites");
/*      */     
/* 1349 */     StringBuilder buf = new StringBuilder();
/* 1350 */     for (String c : cipherSuites) {
/* 1351 */       if (c == null) {
/*      */         break;
/*      */       }
/*      */       
/* 1355 */       String converted = CipherSuiteConverter.toOpenSsl(c);
/* 1356 */       if (converted == null) {
/* 1357 */         converted = c;
/*      */       }
/*      */       
/* 1360 */       if (!OpenSsl.isCipherSuiteAvailable(converted)) {
/* 1361 */         throw new IllegalArgumentException("unsupported cipher suite: " + c + '(' + converted + ')');
/*      */       }
/*      */       
/* 1364 */       buf.append(converted);
/* 1365 */       buf.append(':');
/*      */     }
/*      */     
/* 1368 */     if (buf.length() == 0) {
/* 1369 */       throw new IllegalArgumentException("empty cipher suites");
/*      */     }
/* 1371 */     buf.setLength(buf.length() - 1);
/*      */     
/* 1373 */     String cipherSuiteSpec = buf.toString();
/*      */     
/* 1375 */     synchronized (this) {
/* 1376 */       if (!isDestroyed()) {
/*      */         try {
/* 1378 */           SSL.setCipherSuites(this.ssl, cipherSuiteSpec);
/*      */         } catch (Exception e) {
/* 1380 */           throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec, e);
/*      */         }
/*      */       } else {
/* 1383 */         throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final String[] getSupportedProtocols()
/*      */   {
/* 1390 */     return (String[])OpenSsl.SUPPORTED_PROTOCOLS_SET.toArray(new String[0]);
/*      */   }
/*      */   
/*      */   public final String[] getEnabledProtocols()
/*      */   {
/* 1395 */     List<String> enabled = new ArrayList(6);
/*      */     
/* 1397 */     enabled.add("SSLv2Hello");
/*      */     
/*      */     int opts;
/* 1400 */     synchronized (this) { int opts;
/* 1401 */       if (!isDestroyed()) {
/* 1402 */         opts = SSL.getOptions(this.ssl);
/*      */       } else
/* 1404 */         return (String[])enabled.toArray(new String[0]);
/*      */     }
/*      */     int opts;
/* 1407 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1, "TLSv1")) {
/* 1408 */       enabled.add("TLSv1");
/*      */     }
/* 1410 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_1, "TLSv1.1")) {
/* 1411 */       enabled.add("TLSv1.1");
/*      */     }
/* 1413 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_2, "TLSv1.2")) {
/* 1414 */       enabled.add("TLSv1.2");
/*      */     }
/* 1416 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv2, "SSLv2")) {
/* 1417 */       enabled.add("SSLv2");
/*      */     }
/* 1419 */     if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv3, "SSLv3")) {
/* 1420 */       enabled.add("SSLv3");
/*      */     }
/* 1422 */     return (String[])enabled.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean isProtocolEnabled(int opts, int disableMask, String protocolString)
/*      */   {
/* 1428 */     return ((opts & disableMask) == 0) && (OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(protocolString));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setEnabledProtocols(String[] protocols)
/*      */   {
/* 1442 */     if (protocols == null)
/*      */     {
/* 1444 */       throw new IllegalArgumentException();
/*      */     }
/* 1446 */     int minProtocolIndex = OPENSSL_OP_NO_PROTOCOLS.length;
/* 1447 */     int maxProtocolIndex = 0;
/* 1448 */     for (String p : protocols) {
/* 1449 */       if (!OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(p)) {
/* 1450 */         throw new IllegalArgumentException("Protocol " + p + " is not supported.");
/*      */       }
/* 1452 */       if (p.equals("SSLv2")) {
/* 1453 */         if (minProtocolIndex > 0) {
/* 1454 */           minProtocolIndex = 0;
/*      */         }
/* 1456 */         if (maxProtocolIndex < 0) {
/* 1457 */           maxProtocolIndex = 0;
/*      */         }
/* 1459 */       } else if (p.equals("SSLv3")) {
/* 1460 */         if (minProtocolIndex > 1) {
/* 1461 */           minProtocolIndex = 1;
/*      */         }
/* 1463 */         if (maxProtocolIndex < 1) {
/* 1464 */           maxProtocolIndex = 1;
/*      */         }
/* 1466 */       } else if (p.equals("TLSv1")) {
/* 1467 */         if (minProtocolIndex > 2) {
/* 1468 */           minProtocolIndex = 2;
/*      */         }
/* 1470 */         if (maxProtocolIndex < 2) {
/* 1471 */           maxProtocolIndex = 2;
/*      */         }
/* 1473 */       } else if (p.equals("TLSv1.1")) {
/* 1474 */         if (minProtocolIndex > 3) {
/* 1475 */           minProtocolIndex = 3;
/*      */         }
/* 1477 */         if (maxProtocolIndex < 3) {
/* 1478 */           maxProtocolIndex = 3;
/*      */         }
/* 1480 */       } else if (p.equals("TLSv1.2")) {
/* 1481 */         if (minProtocolIndex > 4) {
/* 1482 */           minProtocolIndex = 4;
/*      */         }
/* 1484 */         if (maxProtocolIndex < 4) {
/* 1485 */           maxProtocolIndex = 4;
/*      */         }
/*      */       }
/*      */     }
/* 1489 */     synchronized (this) {
/* 1490 */       if (!isDestroyed())
/*      */       {
/* 1492 */         SSL.clearOptions(this.ssl, SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1 | SSL.SSL_OP_NO_TLSv1_1 | SSL.SSL_OP_NO_TLSv1_2);
/*      */         
/*      */ 
/* 1495 */         int opts = 0;
/* 1496 */         for (int i = 0; i < minProtocolIndex; i++) {
/* 1497 */           opts |= OPENSSL_OP_NO_PROTOCOLS[i];
/*      */         }
/* 1499 */         assert (maxProtocolIndex != Integer.MAX_VALUE);
/* 1500 */         for (int i = maxProtocolIndex + 1; i < OPENSSL_OP_NO_PROTOCOLS.length; i++) {
/* 1501 */           opts |= OPENSSL_OP_NO_PROTOCOLS[i];
/*      */         }
/*      */         
/*      */ 
/* 1505 */         SSL.setOptions(this.ssl, opts);
/*      */       } else {
/* 1507 */         throw new IllegalStateException("failed to enable protocols: " + Arrays.asList(protocols));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final SSLSession getSession()
/*      */   {
/* 1514 */     return this.session;
/*      */   }
/*      */   
/*      */   public final synchronized void beginHandshake() throws SSLException
/*      */   {
/* 1519 */     switch (this.handshakeState) {
/*      */     case STARTED_IMPLICITLY: 
/* 1521 */       checkEngineClosed(BEGIN_HANDSHAKE_ENGINE_CLOSED);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1529 */       this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
/* 1530 */       calculateMaxWrapOverhead();
/*      */       
/* 1532 */       break;
/*      */     case STARTED_EXPLICITLY: 
/*      */       break;
/*      */     
/*      */     case FINISHED: 
/* 1537 */       throw RENEGOTIATION_UNSUPPORTED;
/*      */     case NOT_STARTED: 
/* 1539 */       this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
/* 1540 */       handshake();
/* 1541 */       calculateMaxWrapOverhead();
/* 1542 */       break;
/*      */     default: 
/* 1544 */       throw new Error();
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkEngineClosed(SSLException cause) throws SSLException {
/* 1549 */     if (isDestroyed()) {
/* 1550 */       throw cause;
/*      */     }
/*      */   }
/*      */   
/*      */   private static SSLEngineResult.HandshakeStatus pendingStatus(int pendingStatus)
/*      */   {
/* 1556 */     return pendingStatus > 0 ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */   }
/*      */   
/*      */   private static boolean isEmpty(Object[] arr) {
/* 1560 */     return (arr == null) || (arr.length == 0);
/*      */   }
/*      */   
/*      */   private static boolean isEmpty(byte[] cert) {
/* 1564 */     return (cert == null) || (cert.length == 0);
/*      */   }
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus handshake() throws SSLException {
/* 1568 */     if (this.handshakeState == HandshakeState.FINISHED) {
/* 1569 */       return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */     }
/* 1571 */     checkEngineClosed(HANDSHAKE_ENGINE_CLOSED);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1577 */     SSLHandshakeException exception = this.handshakeException;
/* 1578 */     if (exception != null) {
/* 1579 */       if (SSL.bioLengthNonApplication(this.networkBIO) > 0)
/*      */       {
/* 1581 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */       
/*      */ 
/* 1585 */       this.handshakeException = null;
/* 1586 */       shutdown();
/* 1587 */       throw exception;
/*      */     }
/*      */     
/*      */ 
/* 1591 */     this.engineMap.add(this);
/* 1592 */     if (this.lastAccessed == -1L) {
/* 1593 */       this.lastAccessed = System.currentTimeMillis();
/*      */     }
/*      */     
/* 1596 */     if ((!this.certificateSet) && (this.keyMaterialManager != null)) {
/* 1597 */       this.certificateSet = true;
/* 1598 */       this.keyMaterialManager.setKeyMaterialServerSide(this);
/*      */     }
/*      */     
/* 1601 */     int code = SSL.doHandshake(this.ssl);
/* 1602 */     if (code <= 0)
/*      */     {
/*      */ 
/* 1605 */       if (this.handshakeException != null) {
/* 1606 */         exception = this.handshakeException;
/* 1607 */         this.handshakeException = null;
/* 1608 */         shutdown();
/* 1609 */         throw exception;
/*      */       }
/*      */       
/* 1612 */       int sslError = SSL.getError(this.ssl, code);
/* 1613 */       if ((sslError == SSL.SSL_ERROR_WANT_READ) || (sslError == SSL.SSL_ERROR_WANT_WRITE)) {
/* 1614 */         return pendingStatus(SSL.bioLengthNonApplication(this.networkBIO));
/*      */       }
/*      */       
/* 1617 */       throw shutdownWithError("SSL_do_handshake", sslError);
/*      */     }
/*      */     
/*      */ 
/* 1621 */     this.session.handshakeFinished();
/* 1622 */     this.engineMap.remove(this.ssl);
/* 1623 */     return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */   }
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus mayFinishHandshake(SSLEngineResult.HandshakeStatus status) throws SSLException
/*      */   {
/* 1628 */     if ((status == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) && (this.handshakeState != HandshakeState.FINISHED))
/*      */     {
/*      */ 
/* 1631 */       return handshake();
/*      */     }
/* 1633 */     return status;
/*      */   }
/*      */   
/*      */ 
/*      */   public final synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus()
/*      */   {
/* 1639 */     return needPendingStatus() ? pendingStatus(SSL.bioLengthNonApplication(this.networkBIO)) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */   
/*      */   private SSLEngineResult.HandshakeStatus getHandshakeStatus(int pending)
/*      */   {
/* 1644 */     return needPendingStatus() ? pendingStatus(pending) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */   
/*      */   private boolean needPendingStatus() {
/* 1648 */     return (this.handshakeState != HandshakeState.NOT_STARTED) && (!isDestroyed()) && ((this.handshakeState != HandshakeState.FINISHED) || 
/* 1649 */       (isInboundDone()) || (isOutboundDone()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String toJavaCipherSuite(String openSslCipherSuite)
/*      */   {
/* 1656 */     if (openSslCipherSuite == null) {
/* 1657 */       return null;
/*      */     }
/*      */     
/* 1660 */     String prefix = toJavaCipherSuitePrefix(SSL.getVersion(this.ssl));
/* 1661 */     return CipherSuiteConverter.toJava(openSslCipherSuite, prefix);
/*      */   }
/*      */   
/*      */ 
/*      */   private static String toJavaCipherSuitePrefix(String protocolVersion)
/*      */   {
/*      */     char c;
/*      */     char c;
/* 1669 */     if ((protocolVersion == null) || (protocolVersion.isEmpty())) {
/* 1670 */       c = '\000';
/*      */     } else {
/* 1672 */       c = protocolVersion.charAt(0);
/*      */     }
/*      */     
/* 1675 */     switch (c) {
/*      */     case 'T': 
/* 1677 */       return "TLS";
/*      */     case 'S': 
/* 1679 */       return "SSL";
/*      */     }
/* 1681 */     return "UNKNOWN";
/*      */   }
/*      */   
/*      */ 
/*      */   public final void setUseClientMode(boolean clientMode)
/*      */   {
/* 1687 */     if (clientMode != this.clientMode) {
/* 1688 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean getUseClientMode()
/*      */   {
/* 1694 */     return this.clientMode;
/*      */   }
/*      */   
/*      */   public final void setNeedClientAuth(boolean b)
/*      */   {
/* 1699 */     setClientAuth(b ? ClientAuth.REQUIRE : ClientAuth.NONE);
/*      */   }
/*      */   
/*      */   public final boolean getNeedClientAuth()
/*      */   {
/* 1704 */     return this.clientAuth == ClientAuth.REQUIRE;
/*      */   }
/*      */   
/*      */   public final void setWantClientAuth(boolean b)
/*      */   {
/* 1709 */     setClientAuth(b ? ClientAuth.OPTIONAL : ClientAuth.NONE);
/*      */   }
/*      */   
/*      */   public final boolean getWantClientAuth()
/*      */   {
/* 1714 */     return this.clientAuth == ClientAuth.OPTIONAL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final synchronized void setVerify(int verifyMode, int depth)
/*      */   {
/* 1723 */     SSL.setVerify(this.ssl, verifyMode, depth);
/*      */   }
/*      */   
/*      */   private void setClientAuth(ClientAuth mode) {
/* 1727 */     if (this.clientMode) {
/* 1728 */       return;
/*      */     }
/* 1730 */     synchronized (this) {
/* 1731 */       if (this.clientAuth == mode)
/*      */       {
/* 1733 */         return;
/*      */       }
/* 1735 */       switch (mode) {
/*      */       case NONE: 
/* 1737 */         SSL.setVerify(this.ssl, 0, 10);
/* 1738 */         break;
/*      */       case REQUIRE: 
/* 1740 */         SSL.setVerify(this.ssl, 2, 10);
/* 1741 */         break;
/*      */       case OPTIONAL: 
/* 1743 */         SSL.setVerify(this.ssl, 1, 10);
/* 1744 */         break;
/*      */       default: 
/* 1746 */         throw new Error(mode.toString());
/*      */       }
/* 1748 */       this.clientAuth = mode;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void setEnableSessionCreation(boolean b)
/*      */   {
/* 1754 */     if (b) {
/* 1755 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public final boolean getEnableSessionCreation()
/*      */   {
/* 1761 */     return false;
/*      */   }
/*      */   
/*      */   public final synchronized SSLParameters getSSLParameters()
/*      */   {
/* 1766 */     SSLParameters sslParameters = super.getSSLParameters();
/*      */     
/* 1768 */     int version = PlatformDependent.javaVersion();
/* 1769 */     if (version >= 7) {
/* 1770 */       sslParameters.setEndpointIdentificationAlgorithm(this.endPointIdentificationAlgorithm);
/* 1771 */       Java7SslParametersUtils.setAlgorithmConstraints(sslParameters, this.algorithmConstraints);
/* 1772 */       if (version >= 8) {
/* 1773 */         if (this.sniHostNames != null) {
/* 1774 */           Java8SslUtils.setSniHostNames(sslParameters, this.sniHostNames);
/*      */         }
/* 1776 */         if (!isDestroyed()) {
/* 1777 */           Java8SslUtils.setUseCipherSuitesOrder(sslParameters, 
/* 1778 */             (SSL.getOptions(this.ssl) & SSL.SSL_OP_CIPHER_SERVER_PREFERENCE) != 0);
/*      */         }
/*      */         
/* 1781 */         Java8SslUtils.setSNIMatchers(sslParameters, this.matchers);
/*      */       }
/*      */     }
/* 1784 */     return sslParameters;
/*      */   }
/*      */   
/*      */   public final synchronized void setSSLParameters(SSLParameters sslParameters)
/*      */   {
/* 1789 */     int version = PlatformDependent.javaVersion();
/* 1790 */     if (version >= 7) {
/* 1791 */       if (sslParameters.getAlgorithmConstraints() != null) {
/* 1792 */         throw new IllegalArgumentException("AlgorithmConstraints are not supported.");
/*      */       }
/*      */       
/* 1795 */       if (version >= 8) {
/* 1796 */         if (!isDestroyed()) {
/* 1797 */           if (this.clientMode) {
/* 1798 */             List<String> sniHostNames = Java8SslUtils.getSniHostNames(sslParameters);
/* 1799 */             for (String name : sniHostNames) {
/* 1800 */               SSL.setTlsExtHostName(this.ssl, name);
/*      */             }
/* 1802 */             this.sniHostNames = sniHostNames;
/*      */           }
/* 1804 */           if (Java8SslUtils.getUseCipherSuitesOrder(sslParameters)) {
/* 1805 */             SSL.setOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
/*      */           } else {
/* 1807 */             SSL.clearOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
/*      */           }
/*      */         }
/* 1810 */         this.matchers = sslParameters.getSNIMatchers();
/*      */       }
/*      */       
/* 1813 */       String endPointIdentificationAlgorithm = sslParameters.getEndpointIdentificationAlgorithm();
/* 1814 */       boolean endPointVerificationEnabled = isEndPointVerificationEnabled(endPointIdentificationAlgorithm);
/*      */       
/*      */ 
/* 1817 */       boolean wasEndPointVerificationEnabled = isEndPointVerificationEnabled(this.endPointIdentificationAlgorithm);
/*      */       
/* 1819 */       if ((wasEndPointVerificationEnabled) && (!endPointVerificationEnabled))
/*      */       {
/* 1821 */         SSL.setHostNameValidation(this.ssl, 0, null);
/*      */       } else {
/* 1823 */         String host = endPointVerificationEnabled ? getPeerHost() : null;
/* 1824 */         if ((host != null) && (!host.isEmpty())) {
/* 1825 */           SSL.setHostNameValidation(this.ssl, 0, host);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1830 */       if ((this.clientMode) && (endPointVerificationEnabled)) {
/* 1831 */         SSL.setVerify(this.ssl, 2, -1);
/*      */       }
/*      */       
/* 1834 */       this.endPointIdentificationAlgorithm = endPointIdentificationAlgorithm;
/* 1835 */       this.algorithmConstraints = sslParameters.getAlgorithmConstraints();
/*      */     }
/* 1837 */     super.setSSLParameters(sslParameters);
/*      */   }
/*      */   
/*      */   private static boolean isEndPointVerificationEnabled(String endPointIdentificationAlgorithm) {
/* 1841 */     return (endPointIdentificationAlgorithm != null) && (!endPointIdentificationAlgorithm.isEmpty());
/*      */   }
/*      */   
/*      */   private boolean isDestroyed() {
/* 1845 */     return this.destroyed != 0;
/*      */   }
/*      */   
/*      */   final boolean checkSniHostnameMatch(byte[] hostname) {
/* 1849 */     return Java8SslUtils.checkSniHostnameMatch(this.matchers, hostname);
/*      */   }
/*      */   
/*      */   public String getNegotiatedApplicationProtocol()
/*      */   {
/* 1854 */     return this.applicationProtocol;
/*      */   }
/*      */   
/*      */   private static long bufferAddress(ByteBuffer b) {
/* 1858 */     assert (b.isDirect());
/* 1859 */     if (PlatformDependent.hasUnsafe()) {
/* 1860 */       return PlatformDependent.directBufferAddress(b);
/*      */     }
/* 1862 */     return Buffer.address(b);
/*      */   }
/*      */   
/*      */ 
/*      */   private final class DefaultOpenSslSession
/*      */     implements OpenSslSession
/*      */   {
/*      */     private final OpenSslSessionContext sessionContext;
/*      */     private javax.security.cert.X509Certificate[] x509PeerCerts;
/*      */     private Certificate[] peerCerts;
/*      */     private String protocol;
/*      */     private String cipher;
/*      */     private byte[] id;
/*      */     private long creationTime;
/* 1876 */     private volatile int applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
/*      */     
/*      */     private Map<String, Object> values;
/*      */     
/*      */     DefaultOpenSslSession(OpenSslSessionContext sessionContext)
/*      */     {
/* 1882 */       this.sessionContext = sessionContext;
/*      */     }
/*      */     
/*      */     private SSLSessionBindingEvent newSSLSessionBindingEvent(String name) {
/* 1886 */       return new SSLSessionBindingEvent(ReferenceCountedOpenSslEngine.this.session, name);
/*      */     }
/*      */     
/*      */     public byte[] getId()
/*      */     {
/* 1891 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1892 */         if (this.id == null) {
/* 1893 */           return EmptyArrays.EMPTY_BYTES;
/*      */         }
/* 1895 */         return (byte[])this.id.clone();
/*      */       }
/*      */     }
/*      */     
/*      */     public SSLSessionContext getSessionContext()
/*      */     {
/* 1901 */       return this.sessionContext;
/*      */     }
/*      */     
/*      */     public long getCreationTime()
/*      */     {
/* 1906 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1907 */         if ((this.creationTime == 0L) && (!ReferenceCountedOpenSslEngine.this.isDestroyed())) {
/* 1908 */           this.creationTime = (SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L);
/*      */         }
/*      */       }
/* 1911 */       return this.creationTime;
/*      */     }
/*      */     
/*      */     public long getLastAccessedTime()
/*      */     {
/* 1916 */       long lastAccessed = ReferenceCountedOpenSslEngine.this.lastAccessed;
/*      */       
/* 1918 */       return lastAccessed == -1L ? getCreationTime() : lastAccessed;
/*      */     }
/*      */     
/*      */     public void invalidate()
/*      */     {
/* 1923 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1924 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1925 */           SSL.setTimeout(ReferenceCountedOpenSslEngine.this.ssl, 0L);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean isValid()
/*      */     {
/* 1932 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 1933 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 1934 */           return System.currentTimeMillis() - SSL.getTimeout(ReferenceCountedOpenSslEngine.this.ssl) * 1000L < SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L;
/*      */         }
/*      */       }
/* 1937 */       return false;
/*      */     }
/*      */     
/*      */     public void putValue(String name, Object value)
/*      */     {
/* 1942 */       if (name == null) {
/* 1943 */         throw new NullPointerException("name");
/*      */       }
/* 1945 */       if (value == null) {
/* 1946 */         throw new NullPointerException("value");
/*      */       }
/* 1948 */       Map<String, Object> values = this.values;
/* 1949 */       if (values == null)
/*      */       {
/* 1951 */         values = this.values = new HashMap(2);
/*      */       }
/* 1953 */       Object old = values.put(name, value);
/* 1954 */       if ((value instanceof SSLSessionBindingListener))
/*      */       {
/* 1956 */         ((SSLSessionBindingListener)value).valueBound(newSSLSessionBindingEvent(name));
/*      */       }
/* 1958 */       notifyUnbound(old, name);
/*      */     }
/*      */     
/*      */     public Object getValue(String name)
/*      */     {
/* 1963 */       if (name == null) {
/* 1964 */         throw new NullPointerException("name");
/*      */       }
/* 1966 */       if (this.values == null) {
/* 1967 */         return null;
/*      */       }
/* 1969 */       return this.values.get(name);
/*      */     }
/*      */     
/*      */     public void removeValue(String name)
/*      */     {
/* 1974 */       if (name == null) {
/* 1975 */         throw new NullPointerException("name");
/*      */       }
/* 1977 */       Map<String, Object> values = this.values;
/* 1978 */       if (values == null) {
/* 1979 */         return;
/*      */       }
/* 1981 */       Object old = values.remove(name);
/* 1982 */       notifyUnbound(old, name);
/*      */     }
/*      */     
/*      */     public String[] getValueNames()
/*      */     {
/* 1987 */       Map<String, Object> values = this.values;
/* 1988 */       if ((values == null) || (values.isEmpty())) {
/* 1989 */         return EmptyArrays.EMPTY_STRINGS;
/*      */       }
/* 1991 */       return (String[])values.keySet().toArray(new String[0]);
/*      */     }
/*      */     
/*      */     private void notifyUnbound(Object value, String name) {
/* 1995 */       if ((value instanceof SSLSessionBindingListener))
/*      */       {
/* 1997 */         ((SSLSessionBindingListener)value).valueUnbound(newSSLSessionBindingEvent(name));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void handshakeFinished()
/*      */       throws SSLException
/*      */     {
/* 2007 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2008 */         if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 2009 */           this.id = SSL.getSessionId(ReferenceCountedOpenSslEngine.this.ssl);
/* 2010 */           this.cipher = ReferenceCountedOpenSslEngine.this.toJavaCipherSuite(SSL.getCipherForSSL(ReferenceCountedOpenSslEngine.this.ssl));
/* 2011 */           this.protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
/*      */           
/* 2013 */           initPeerCerts();
/* 2014 */           selectApplicationProtocol();
/* 2015 */           ReferenceCountedOpenSslEngine.this.calculateMaxWrapOverhead();
/*      */           
/* 2017 */           ReferenceCountedOpenSslEngine.this.handshakeState = ReferenceCountedOpenSslEngine.HandshakeState.FINISHED;
/*      */         } else {
/* 2019 */           throw new SSLException("Already closed");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void initPeerCerts()
/*      */     {
/* 2030 */       byte[][] chain = SSL.getPeerCertChain(ReferenceCountedOpenSslEngine.this.ssl);
/* 2031 */       if (ReferenceCountedOpenSslEngine.this.clientMode) {
/* 2032 */         if (ReferenceCountedOpenSslEngine.isEmpty(chain)) {
/* 2033 */           this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
/* 2034 */           this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
/*      */         } else {
/* 2036 */           this.peerCerts = new Certificate[chain.length];
/* 2037 */           this.x509PeerCerts = new javax.security.cert.X509Certificate[chain.length];
/* 2038 */           initCerts(chain, 0);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 2046 */         byte[] clientCert = SSL.getPeerCertificate(ReferenceCountedOpenSslEngine.this.ssl);
/* 2047 */         if (ReferenceCountedOpenSslEngine.isEmpty(clientCert)) {
/* 2048 */           this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
/* 2049 */           this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
/*      */         }
/* 2051 */         else if (ReferenceCountedOpenSslEngine.isEmpty(chain)) {
/* 2052 */           this.peerCerts = new Certificate[] { new OpenSslX509Certificate(clientCert) };
/* 2053 */           this.x509PeerCerts = new javax.security.cert.X509Certificate[] { new OpenSslJavaxX509Certificate(clientCert) };
/*      */         } else {
/* 2055 */           this.peerCerts = new Certificate[chain.length + 1];
/* 2056 */           this.x509PeerCerts = new javax.security.cert.X509Certificate[chain.length + 1];
/* 2057 */           this.peerCerts[0] = new OpenSslX509Certificate(clientCert);
/* 2058 */           this.x509PeerCerts[0] = new OpenSslJavaxX509Certificate(clientCert);
/* 2059 */           initCerts(chain, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void initCerts(byte[][] chain, int startPos)
/*      */     {
/* 2066 */       for (int i = 0; i < chain.length; i++) {
/* 2067 */         int certPos = startPos + i;
/* 2068 */         this.peerCerts[certPos] = new OpenSslX509Certificate(chain[i]);
/* 2069 */         this.x509PeerCerts[certPos] = new OpenSslJavaxX509Certificate(chain[i]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void selectApplicationProtocol()
/*      */       throws SSLException
/*      */     {
/* 2077 */       ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior = ReferenceCountedOpenSslEngine.this.apn.selectedListenerFailureBehavior();
/* 2078 */       List<String> protocols = ReferenceCountedOpenSslEngine.this.apn.protocols();
/*      */       
/* 2080 */       switch (ReferenceCountedOpenSslEngine.3.$SwitchMap$io$netty$handler$ssl$ApplicationProtocolConfig$Protocol[ReferenceCountedOpenSslEngine.this.apn.protocol().ordinal()])
/*      */       {
/*      */       case 1: 
/*      */         break;
/*      */       
/*      */       case 2: 
/* 2086 */         String applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
/* 2087 */         if (applicationProtocol != null) {
/* 2088 */           ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */         }
/*      */         
/*      */         break;
/*      */       case 3: 
/* 2093 */         String applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
/* 2094 */         if (applicationProtocol != null) {
/* 2095 */           ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */         }
/*      */         
/*      */         break;
/*      */       case 4: 
/* 2100 */         String applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
/* 2101 */         if (applicationProtocol == null) {
/* 2102 */           applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
/*      */         }
/* 2104 */         if (applicationProtocol != null) {
/* 2105 */           ReferenceCountedOpenSslEngine.this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
/*      */         }
/*      */         
/*      */         break;
/*      */       default: 
/* 2110 */         throw new Error();
/*      */       }
/*      */     }
/*      */     
/*      */     private String selectApplicationProtocol(List<String> protocols, ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior, String applicationProtocol)
/*      */       throws SSLException
/*      */     {
/* 2117 */       if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT) {
/* 2118 */         return applicationProtocol;
/*      */       }
/* 2120 */       int size = protocols.size();
/* 2121 */       assert (size > 0);
/* 2122 */       if (protocols.contains(applicationProtocol)) {
/* 2123 */         return applicationProtocol;
/*      */       }
/* 2125 */       if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL) {
/* 2126 */         return (String)protocols.get(size - 1);
/*      */       }
/* 2128 */       throw new SSLException("unknown protocol " + applicationProtocol);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Certificate[] getPeerCertificates()
/*      */       throws SSLPeerUnverifiedException
/*      */     {
/* 2136 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2137 */         if (ReferenceCountedOpenSslEngine.isEmpty(this.peerCerts)) {
/* 2138 */           throw new SSLPeerUnverifiedException("peer not verified");
/*      */         }
/* 2140 */         return (Certificate[])this.peerCerts.clone();
/*      */       }
/*      */     }
/*      */     
/*      */     public Certificate[] getLocalCertificates()
/*      */     {
/* 2146 */       if (ReferenceCountedOpenSslEngine.this.localCerts == null) {
/* 2147 */         return null;
/*      */       }
/* 2149 */       return (Certificate[])ReferenceCountedOpenSslEngine.this.localCerts.clone();
/*      */     }
/*      */     
/*      */     public javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException
/*      */     {
/* 2154 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2155 */         if (ReferenceCountedOpenSslEngine.isEmpty(this.x509PeerCerts)) {
/* 2156 */           throw new SSLPeerUnverifiedException("peer not verified");
/*      */         }
/* 2158 */         return (javax.security.cert.X509Certificate[])this.x509PeerCerts.clone();
/*      */       }
/*      */     }
/*      */     
/*      */     public Principal getPeerPrincipal() throws SSLPeerUnverifiedException
/*      */     {
/* 2164 */       Certificate[] peer = getPeerCertificates();
/*      */       
/*      */ 
/* 2167 */       return ((java.security.cert.X509Certificate)peer[0]).getSubjectX500Principal();
/*      */     }
/*      */     
/*      */     public Principal getLocalPrincipal()
/*      */     {
/* 2172 */       Certificate[] local = ReferenceCountedOpenSslEngine.this.localCerts;
/* 2173 */       if ((local == null) || (local.length == 0)) {
/* 2174 */         return null;
/*      */       }
/* 2176 */       return ((java.security.cert.X509Certificate)local[0]).getIssuerX500Principal();
/*      */     }
/*      */     
/*      */     public String getCipherSuite()
/*      */     {
/* 2181 */       synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2182 */         if (this.cipher == null) {
/* 2183 */           return "SSL_NULL_WITH_NULL_NULL";
/*      */         }
/* 2185 */         return this.cipher;
/*      */       }
/*      */     }
/*      */     
/*      */     public String getProtocol()
/*      */     {
/* 2191 */       String protocol = this.protocol;
/* 2192 */       if (protocol == null) {
/* 2193 */         synchronized (ReferenceCountedOpenSslEngine.this) {
/* 2194 */           if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
/* 2195 */             protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
/*      */           } else {
/* 2197 */             protocol = "";
/*      */           }
/*      */         }
/*      */       }
/* 2201 */       return protocol;
/*      */     }
/*      */     
/*      */     public String getPeerHost()
/*      */     {
/* 2206 */       return ReferenceCountedOpenSslEngine.this.getPeerHost();
/*      */     }
/*      */     
/*      */     public int getPeerPort()
/*      */     {
/* 2211 */       return ReferenceCountedOpenSslEngine.this.getPeerPort();
/*      */     }
/*      */     
/*      */     public int getPacketBufferSize()
/*      */     {
/* 2216 */       return ReferenceCountedOpenSslEngine.this.maxEncryptedPacketLength();
/*      */     }
/*      */     
/*      */     public int getApplicationBufferSize()
/*      */     {
/* 2221 */       return this.applicationBufferSize;
/*      */     }
/*      */     
/*      */     public void tryExpandApplicationBufferSize(int packetLengthDataOnly)
/*      */     {
/* 2226 */       if ((packetLengthDataOnly > ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH) && (this.applicationBufferSize != ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE)) {
/* 2227 */         this.applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE;
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\ReferenceCountedOpenSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */