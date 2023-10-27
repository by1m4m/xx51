/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SelfSignedCertificate
/*     */ {
/*  61 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
/*     */   
/*     */ 
/*  64 */   private static final Date DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", 
/*  65 */     System.currentTimeMillis() - 31536000000L));
/*     */   
/*  67 */   private static final Date DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
/*     */   
/*     */   private final File certificate;
/*     */   
/*     */   private final File privateKey;
/*     */   
/*     */   private final X509Certificate cert;
/*     */   private final PrivateKey key;
/*     */   
/*     */   public SelfSignedCertificate()
/*     */     throws CertificateException
/*     */   {
/*  79 */     this(DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(Date notBefore, Date notAfter)
/*     */     throws CertificateException
/*     */   {
/*  88 */     this("example.com", notBefore, notAfter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(String fqdn)
/*     */     throws CertificateException
/*     */   {
/*  97 */     this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter)
/*     */     throws CertificateException
/*     */   {
/* 110 */     this(fqdn, ThreadLocalInsecureRandom.current(), 1024, notBefore, notAfter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(String fqdn, SecureRandom random, int bits)
/*     */     throws CertificateException
/*     */   {
/* 121 */     this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
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
/*     */   public SelfSignedCertificate(String fqdn, SecureRandom random, int bits, Date notBefore, Date notAfter)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 138 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
/* 139 */       keyGen.initialize(bits, random);
/* 140 */       keypair = keyGen.generateKeyPair();
/*     */     } catch (NoSuchAlgorithmException e) {
/*     */       KeyPair keypair;
/* 143 */       throw new Error(e);
/*     */     }
/*     */     
/*     */     KeyPair keypair;
/*     */     try
/*     */     {
/* 149 */       paths = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
/*     */     } catch (Throwable t) { String[] paths;
/* 151 */       logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t);
/*     */       try
/*     */       {
/* 154 */         paths = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
/*     */       } catch (Throwable t2) { String[] paths;
/* 156 */         logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t2);
/* 157 */         throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.", t2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     String[] paths;
/*     */     
/* 164 */     this.certificate = new File(paths[0]);
/* 165 */     this.privateKey = new File(paths[1]);
/* 166 */     this.key = keypair.getPrivate();
/* 167 */     FileInputStream certificateInput = null;
/*     */     try {
/* 169 */       certificateInput = new FileInputStream(this.certificate);
/* 170 */       this.cert = ((X509Certificate)CertificateFactory.getInstance("X509").generateCertificate(certificateInput)); return;
/*     */     } catch (Exception e) {
/* 172 */       throw new CertificateEncodingException(e);
/*     */     } finally {
/* 174 */       if (certificateInput != null) {
/*     */         try {
/* 176 */           certificateInput.close();
/*     */         } catch (IOException e) {
/* 178 */           if (logger.isWarnEnabled()) {
/* 179 */             logger.warn("Failed to close a file: " + this.certificate, e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File certificate()
/*     */   {
/* 190 */     return this.certificate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File privateKey()
/*     */   {
/* 197 */     return this.privateKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public X509Certificate cert()
/*     */   {
/* 204 */     return this.cert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PrivateKey key()
/*     */   {
/* 211 */     return this.key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 218 */     safeDelete(this.certificate);
/* 219 */     safeDelete(this.privateKey);
/*     */   }
/*     */   
/*     */   static String[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert)
/*     */     throws IOException, CertificateEncodingException
/*     */   {
/* 225 */     ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
/*     */     
/*     */     try
/*     */     {
/* 229 */       ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
/*     */       String keyText;
/*     */       try {
/* 232 */         keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     finally {
/*     */       String keyText;
/* 238 */       wrappedBuf.release(); }
/*     */     String keyText;
/*     */     ByteBuf encodedBuf;
/* 241 */     File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
/* 242 */     keyFile.deleteOnExit();
/*     */     
/* 244 */     Object keyOut = new FileOutputStream(keyFile);
/*     */     try {
/* 246 */       ((OutputStream)keyOut).write(keyText.getBytes(CharsetUtil.US_ASCII));
/* 247 */       ((OutputStream)keyOut).close();
/* 248 */       keyOut = null;
/*     */     } finally {
/* 250 */       if (keyOut != null) {
/* 251 */         safeClose(keyFile, (OutputStream)keyOut);
/* 252 */         safeDelete(keyFile);
/*     */       }
/*     */     }
/*     */     
/* 256 */     wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
/*     */     try
/*     */     {
/* 259 */       encodedBuf = Base64.encode(wrappedBuf, true);
/*     */       String certText;
/*     */       try
/*     */       {
/* 263 */         certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     finally {
/*     */       String certText;
/* 269 */       wrappedBuf.release();
/*     */     }
/*     */     String certText;
/* 272 */     File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
/* 273 */     certFile.deleteOnExit();
/*     */     
/* 275 */     Object certOut = new FileOutputStream(certFile);
/*     */     try {
/* 277 */       ((OutputStream)certOut).write(certText.getBytes(CharsetUtil.US_ASCII));
/* 278 */       ((OutputStream)certOut).close();
/* 279 */       certOut = null;
/*     */     } finally {
/* 281 */       if (certOut != null) {
/* 282 */         safeClose(certFile, (OutputStream)certOut);
/* 283 */         safeDelete(certFile);
/* 284 */         safeDelete(keyFile);
/*     */       }
/*     */     }
/*     */     
/* 288 */     return new String[] { certFile.getPath(), keyFile.getPath() };
/*     */   }
/*     */   
/*     */   private static void safeDelete(File certFile) {
/* 292 */     if ((!certFile.delete()) && 
/* 293 */       (logger.isWarnEnabled())) {
/* 294 */       logger.warn("Failed to delete a file: " + certFile);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(File keyFile, OutputStream keyOut)
/*     */   {
/*     */     try {
/* 301 */       keyOut.close();
/*     */     } catch (IOException e) {
/* 303 */       if (logger.isWarnEnabled()) {
/* 304 */         logger.warn("Failed to close a file: " + keyFile, e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\util\SelfSignedCertificate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */