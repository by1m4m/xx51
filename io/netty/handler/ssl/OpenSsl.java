/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.handler.ssl.util.SelfSignedCertificate;
/*     */ import io.netty.internal.tcnative.Buffer;
/*     */ import io.netty.internal.tcnative.Library;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.NativeLibraryLoader;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OpenSsl
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final Throwable UNAVAILABILITY_CAUSE;
/*     */   static final List<String> DEFAULT_CIPHERS;
/*     */   static final Set<String> AVAILABLE_CIPHER_SUITES;
/*     */   private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
/*     */   private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
/*     */   private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
/*     */   private static final boolean SUPPORTS_HOSTNAME_VALIDATION;
/*     */   private static final boolean USE_KEYMANAGER_FACTORY;
/*     */   private static final boolean SUPPORTS_OCSP;
/*     */   static final Set<String> SUPPORTED_PROTOCOLS_SET;
/*     */   
/*     */   static
/*     */   {
/*  58 */     logger = InternalLoggerFactory.getInstance(OpenSsl.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     Throwable cause = null;
/*     */     
/*  75 */     if (SystemPropertyUtil.getBoolean("io.netty.handler.ssl.noOpenSsl", false)) {
/*  76 */       cause = new UnsupportedOperationException("OpenSSL was explicit disabled with -Dio.netty.handler.ssl.noOpenSsl=true");
/*     */       
/*     */ 
/*  79 */       logger.debug("netty-tcnative explicit disabled; " + OpenSslEngine.class
/*     */       
/*  81 */         .getSimpleName() + " will be unavailable.", cause);
/*     */     }
/*     */     else {
/*     */       try {
/*  85 */         Class.forName("io.netty.internal.tcnative.SSL", false, OpenSsl.class.getClassLoader());
/*     */       } catch (ClassNotFoundException t) {
/*  87 */         cause = t;
/*  88 */         logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class
/*     */         
/*  90 */           .getSimpleName() + " will be unavailable.");
/*     */       }
/*     */       
/*     */ 
/*  94 */       if (cause == null)
/*     */       {
/*     */         try {
/*  97 */           loadTcNative();
/*     */         } catch (Throwable t) {
/*  99 */           cause = t;
/* 100 */           logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class
/*     */           
/* 102 */             .getSimpleName() + " will be unavailable, unless the application has already loaded the symbols by some other means. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t);
/*     */         }
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 108 */           String engine = SystemPropertyUtil.get("io.netty.handler.ssl.openssl.engine", null);
/* 109 */           if (engine == null) {
/* 110 */             logger.debug("Initialize netty-tcnative using engine: 'default'");
/*     */           } else {
/* 112 */             logger.debug("Initialize netty-tcnative using engine: '{}'", engine);
/*     */           }
/* 114 */           initializeTcNative(engine);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 119 */           cause = null;
/*     */         } catch (Throwable t) {
/* 121 */           if (cause == null) {
/* 122 */             cause = t;
/*     */           }
/* 124 */           logger.debug("Failed to initialize netty-tcnative; " + OpenSslEngine.class
/*     */           
/* 126 */             .getSimpleName() + " will be unavailable. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 132 */     UNAVAILABILITY_CAUSE = cause;
/*     */     
/* 134 */     if (cause == null) {
/* 135 */       logger.debug("netty-tcnative using native library: {}", SSL.versionString());
/*     */       
/* 137 */       List<String> defaultCiphers = new ArrayList();
/* 138 */       Set<String> availableOpenSslCipherSuites = new LinkedHashSet(128);
/* 139 */       boolean supportsKeyManagerFactory = false;
/* 140 */       boolean useKeyManagerFactory = false;
/* 141 */       boolean supportsHostNameValidation = false;
/*     */       try {
/* 143 */         long sslCtx = SSLContext.make(31, 1);
/* 144 */         long certBio = 0L;
/* 145 */         SelfSignedCertificate cert = null;
/*     */         try {
/* 147 */           SSLContext.setCipherSuite(sslCtx, "ALL");
/* 148 */           long ssl = SSL.newSSL(sslCtx, true);
/*     */           try {
/* 150 */             for (String c : SSL.getCiphers(ssl))
/*     */             {
/* 152 */               if ((c != null) && (!c.isEmpty()) && (!availableOpenSslCipherSuites.contains(c)))
/*     */               {
/*     */ 
/* 155 */                 availableOpenSslCipherSuites.add(c);
/*     */               }
/*     */             }
/*     */             try {
/* 159 */               SSL.setHostNameValidation(ssl, 0, "netty.io");
/* 160 */               supportsHostNameValidation = true;
/*     */             } catch (Throwable ignore) {
/* 162 */               logger.debug("Hostname Verification not supported.");
/*     */             }
/*     */             try {
/* 165 */               cert = new SelfSignedCertificate();
/* 166 */               certBio = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, new X509Certificate[] { cert.cert() });
/* 167 */               SSL.setCertificateChainBio(ssl, certBio, false);
/* 168 */               supportsKeyManagerFactory = true;
/*     */               try {
/* 170 */                 useKeyManagerFactory = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */                 {
/*     */                   public Boolean run() {
/* 173 */                     return Boolean.valueOf(SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useKeyManagerFactory", true));
/*     */                   }
/*     */                 })).booleanValue();
/*     */               }
/*     */               catch (Throwable ignore) {
/* 178 */                 logger.debug("Failed to get useKeyManagerFactory system property.");
/*     */               }
/*     */             } catch (Throwable ignore) {
/* 181 */               logger.debug("KeyManagerFactory not supported.");
/*     */             }
/*     */           } finally {
/* 184 */             SSL.freeSSL(ssl);
/* 185 */             if (certBio != 0L) {
/* 186 */               SSL.freeBIO(certBio);
/*     */             }
/* 188 */             if (cert != null) {
/* 189 */               cert.delete();
/*     */             }
/*     */           }
/*     */         } finally {
/* 193 */           SSLContext.free(sslCtx);
/*     */         }
/*     */       } catch (Exception e) {
/* 196 */         logger.warn("Failed to get the list of available OpenSSL cipher suites.", e);
/*     */       }
/* 198 */       AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.unmodifiableSet(availableOpenSslCipherSuites);
/*     */       
/* 200 */       Set<String> availableJavaCipherSuites = new LinkedHashSet(AVAILABLE_OPENSSL_CIPHER_SUITES.size() * 2);
/* 201 */       for (String cipher : AVAILABLE_OPENSSL_CIPHER_SUITES)
/*     */       {
/* 203 */         availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "TLS"));
/* 204 */         availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "SSL"));
/*     */       }
/*     */       
/* 207 */       SslUtils.addIfSupported(availableJavaCipherSuites, defaultCiphers, SslUtils.DEFAULT_CIPHER_SUITES);
/* 208 */       SslUtils.useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, availableJavaCipherSuites);
/* 209 */       DEFAULT_CIPHERS = Collections.unmodifiableList(defaultCiphers);
/*     */       
/* 211 */       AVAILABLE_JAVA_CIPHER_SUITES = Collections.unmodifiableSet(availableJavaCipherSuites);
/*     */       
/*     */ 
/* 214 */       Object availableCipherSuites = new LinkedHashSet(AVAILABLE_OPENSSL_CIPHER_SUITES.size() + AVAILABLE_JAVA_CIPHER_SUITES.size());
/* 215 */       ((Set)availableCipherSuites).addAll(AVAILABLE_OPENSSL_CIPHER_SUITES);
/* 216 */       ((Set)availableCipherSuites).addAll(AVAILABLE_JAVA_CIPHER_SUITES);
/*     */       
/* 218 */       AVAILABLE_CIPHER_SUITES = (Set)availableCipherSuites;
/* 219 */       SUPPORTS_KEYMANAGER_FACTORY = supportsKeyManagerFactory;
/* 220 */       SUPPORTS_HOSTNAME_VALIDATION = supportsHostNameValidation;
/* 221 */       USE_KEYMANAGER_FACTORY = useKeyManagerFactory;
/*     */       
/* 223 */       Set<String> protocols = new LinkedHashSet(6);
/*     */       
/* 225 */       protocols.add("SSLv2Hello");
/* 226 */       if (doesSupportProtocol(1, SSL.SSL_OP_NO_SSLv2)) {
/* 227 */         protocols.add("SSLv2");
/*     */       }
/* 229 */       if (doesSupportProtocol(2, SSL.SSL_OP_NO_SSLv3)) {
/* 230 */         protocols.add("SSLv3");
/*     */       }
/* 232 */       if (doesSupportProtocol(4, SSL.SSL_OP_NO_TLSv1)) {
/* 233 */         protocols.add("TLSv1");
/*     */       }
/* 235 */       if (doesSupportProtocol(8, SSL.SSL_OP_NO_TLSv1_1)) {
/* 236 */         protocols.add("TLSv1.1");
/*     */       }
/* 238 */       if (doesSupportProtocol(16, SSL.SSL_OP_NO_TLSv1_2)) {
/* 239 */         protocols.add("TLSv1.2");
/*     */       }
/*     */       
/* 242 */       SUPPORTED_PROTOCOLS_SET = Collections.unmodifiableSet(protocols);
/* 243 */       SUPPORTS_OCSP = doesSupportOcsp();
/*     */       
/* 245 */       if (logger.isDebugEnabled()) {
/* 246 */         logger.debug("Supported protocols (OpenSSL): {} ", SUPPORTED_PROTOCOLS_SET);
/* 247 */         logger.debug("Default cipher suites (OpenSSL): {}", DEFAULT_CIPHERS);
/*     */       }
/*     */     } else {
/* 250 */       DEFAULT_CIPHERS = Collections.emptyList();
/* 251 */       AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.emptySet();
/* 252 */       AVAILABLE_JAVA_CIPHER_SUITES = Collections.emptySet();
/* 253 */       AVAILABLE_CIPHER_SUITES = Collections.emptySet();
/* 254 */       SUPPORTS_KEYMANAGER_FACTORY = false;
/* 255 */       SUPPORTS_HOSTNAME_VALIDATION = false;
/* 256 */       USE_KEYMANAGER_FACTORY = false;
/* 257 */       SUPPORTED_PROTOCOLS_SET = Collections.emptySet();
/* 258 */       SUPPORTS_OCSP = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean doesSupportOcsp() {
/* 263 */     boolean supportsOcsp = false;
/* 264 */     if (version() >= 268443648L) {
/* 265 */       long sslCtx = -1L;
/*     */       try {
/* 267 */         sslCtx = SSLContext.make(16, 1);
/* 268 */         SSLContext.enableOcsp(sslCtx, false);
/* 269 */         supportsOcsp = true;
/*     */       }
/*     */       catch (Exception localException) {}finally
/*     */       {
/* 273 */         if (sslCtx != -1L) {
/* 274 */           SSLContext.free(sslCtx);
/*     */         }
/*     */       }
/*     */     }
/* 278 */     return supportsOcsp;
/*     */   }
/*     */   
/* 281 */   private static boolean doesSupportProtocol(int protocol, int opt) { if (opt == 0)
/*     */     {
/* 283 */       return false;
/*     */     }
/* 285 */     long sslCtx = -1L;
/*     */     try {
/* 287 */       sslCtx = SSLContext.make(protocol, 2);
/* 288 */       return true;
/*     */     } catch (Exception ignore) {
/* 290 */       return false;
/*     */     } finally {
/* 292 */       if (sslCtx != -1L) {
/* 293 */         SSLContext.free(sslCtx);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAvailable()
/*     */   {
/* 304 */     return UNAVAILABILITY_CAUSE == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAlpnSupported()
/*     */   {
/* 312 */     return version() >= 268443648L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isOcspSupported()
/*     */   {
/* 319 */     return SUPPORTS_OCSP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int version()
/*     */   {
/* 327 */     return isAvailable() ? SSL.version() : -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String versionString()
/*     */   {
/* 335 */     return isAvailable() ? SSL.versionString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void ensureAvailability()
/*     */   {
/* 345 */     if (UNAVAILABILITY_CAUSE != null)
/*     */     {
/* 347 */       throw ((Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Throwable unavailabilityCause()
/*     */   {
/* 358 */     return UNAVAILABILITY_CAUSE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static Set<String> availableCipherSuites()
/*     */   {
/* 366 */     return availableOpenSslCipherSuites();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> availableOpenSslCipherSuites()
/*     */   {
/* 374 */     return AVAILABLE_OPENSSL_CIPHER_SUITES;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> availableJavaCipherSuites()
/*     */   {
/* 382 */     return AVAILABLE_JAVA_CIPHER_SUITES;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isCipherSuiteAvailable(String cipherSuite)
/*     */   {
/* 390 */     String converted = CipherSuiteConverter.toOpenSsl(cipherSuite);
/* 391 */     if (converted != null) {
/* 392 */       cipherSuite = converted;
/*     */     }
/* 394 */     return AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean supportsKeyManagerFactory()
/*     */   {
/* 401 */     return SUPPORTS_KEYMANAGER_FACTORY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean supportsHostnameValidation()
/*     */   {
/* 409 */     return SUPPORTS_HOSTNAME_VALIDATION;
/*     */   }
/*     */   
/*     */   static boolean useKeyManagerFactory() {
/* 413 */     return USE_KEYMANAGER_FACTORY;
/*     */   }
/*     */   
/*     */   static long memoryAddress(ByteBuf buf) {
/* 417 */     assert (buf.isDirect());
/* 418 */     return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.nioBuffer());
/*     */   }
/*     */   
/*     */   private static void loadTcNative()
/*     */     throws Exception
/*     */   {
/* 424 */     String os = PlatformDependent.normalizedOs();
/* 425 */     String arch = PlatformDependent.normalizedArch();
/*     */     
/* 427 */     Set<String> libNames = new LinkedHashSet(4);
/* 428 */     String staticLibName = "netty_tcnative";
/*     */     
/*     */ 
/*     */ 
/* 432 */     libNames.add(staticLibName + "_" + os + '_' + arch);
/* 433 */     if ("linux".equalsIgnoreCase(os))
/*     */     {
/* 435 */       libNames.add(staticLibName + "_" + os + '_' + arch + "_fedora");
/*     */     }
/* 437 */     libNames.add(staticLibName + "_" + arch);
/* 438 */     libNames.add(staticLibName);
/*     */     
/* 440 */     NativeLibraryLoader.loadFirstAvailable(SSL.class.getClassLoader(), 
/* 441 */       (String[])libNames.toArray(new String[0]));
/*     */   }
/*     */   
/*     */   private static boolean initializeTcNative(String engine) throws Exception {
/* 445 */     return Library.initialize("provided", engine);
/*     */   }
/*     */   
/*     */   static void releaseIfNeeded(ReferenceCounted counted) {
/* 449 */     if (counted.refCnt() > 0) {
/* 450 */       ReferenceCountUtil.safeRelease(counted);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSsl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */