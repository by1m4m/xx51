/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.KeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PemReader
/*     */ {
/*  46 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PemReader.class);
/*     */   
/*  48 */   private static final Pattern CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private static final Pattern KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);
/*     */   
/*     */   /* Error */
/*     */   static ByteBuf[] readCertificates(java.io.File file)
/*     */     throws CertificateException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 1	java/io/FileInputStream
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 2	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   8: astore_1
/*     */     //   9: aload_1
/*     */     //   10: invokestatic 3	io/netty/handler/ssl/PemReader:readCertificates	(Ljava/io/InputStream;)[Lio/netty/buffer/ByteBuf;
/*     */     //   13: astore_2
/*     */     //   14: aload_1
/*     */     //   15: invokestatic 4	io/netty/handler/ssl/PemReader:safeClose	(Ljava/io/InputStream;)V
/*     */     //   18: aload_2
/*     */     //   19: areturn
/*     */     //   20: astore_3
/*     */     //   21: aload_1
/*     */     //   22: invokestatic 4	io/netty/handler/ssl/PemReader:safeClose	(Ljava/io/InputStream;)V
/*     */     //   25: aload_3
/*     */     //   26: athrow
/*     */     //   27: astore_1
/*     */     //   28: new 6	java/security/cert/CertificateException
/*     */     //   31: dup
/*     */     //   32: new 7	java/lang/StringBuilder
/*     */     //   35: dup
/*     */     //   36: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*     */     //   39: ldc 9
/*     */     //   41: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   44: aload_0
/*     */     //   45: invokevirtual 11	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   48: invokevirtual 12	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   51: invokespecial 13	java/security/cert/CertificateException:<init>	(Ljava/lang/String;)V
/*     */     //   54: athrow
/*     */     // Line number table:
/*     */     //   Java source line #61	-> byte code offset #0
/*     */     //   Java source line #64	-> byte code offset #9
/*     */     //   Java source line #66	-> byte code offset #14
/*     */     //   Java source line #64	-> byte code offset #18
/*     */     //   Java source line #66	-> byte code offset #20
/*     */     //   Java source line #67	-> byte code offset #25
/*     */     //   Java source line #68	-> byte code offset #27
/*     */     //   Java source line #69	-> byte code offset #28
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	55	0	file	java.io.File
/*     */     //   8	14	1	in	InputStream
/*     */     //   27	2	1	e	java.io.FileNotFoundException
/*     */     //   20	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   9	14	20	finally
/*     */     //   0	18	27	java/io/FileNotFoundException
/*     */     //   20	27	27	java/io/FileNotFoundException
/*     */   }
/*     */   
/*     */   static ByteBuf[] readCertificates(InputStream in)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/*  76 */       content = readContent(in);
/*     */     } catch (IOException e) { String content;
/*  78 */       throw new CertificateException("failed to read certificate input stream", e);
/*     */     }
/*     */     String content;
/*  81 */     List<ByteBuf> certs = new ArrayList();
/*  82 */     Matcher m = CERT_PATTERN.matcher(content);
/*  83 */     int start = 0;
/*     */     
/*  85 */     while (m.find(start))
/*     */     {
/*     */ 
/*     */ 
/*  89 */       ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
/*  90 */       ByteBuf der = Base64.decode(base64);
/*  91 */       base64.release();
/*  92 */       certs.add(der);
/*     */       
/*  94 */       start = m.end();
/*     */     }
/*     */     
/*  97 */     if (certs.isEmpty()) {
/*  98 */       throw new CertificateException("found no certificates in input stream");
/*     */     }
/*     */     
/* 101 */     return (ByteBuf[])certs.toArray(new ByteBuf[0]);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   static ByteBuf readPrivateKey(java.io.File file)
/*     */     throws KeyException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 1	java/io/FileInputStream
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 2	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   8: astore_1
/*     */     //   9: aload_1
/*     */     //   10: invokestatic 35	io/netty/handler/ssl/PemReader:readPrivateKey	(Ljava/io/InputStream;)Lio/netty/buffer/ByteBuf;
/*     */     //   13: astore_2
/*     */     //   14: aload_1
/*     */     //   15: invokestatic 4	io/netty/handler/ssl/PemReader:safeClose	(Ljava/io/InputStream;)V
/*     */     //   18: aload_2
/*     */     //   19: areturn
/*     */     //   20: astore_3
/*     */     //   21: aload_1
/*     */     //   22: invokestatic 4	io/netty/handler/ssl/PemReader:safeClose	(Ljava/io/InputStream;)V
/*     */     //   25: aload_3
/*     */     //   26: athrow
/*     */     //   27: astore_1
/*     */     //   28: new 36	java/security/KeyException
/*     */     //   31: dup
/*     */     //   32: new 7	java/lang/StringBuilder
/*     */     //   35: dup
/*     */     //   36: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*     */     //   39: ldc 37
/*     */     //   41: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   44: aload_0
/*     */     //   45: invokevirtual 11	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   48: invokevirtual 12	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   51: invokespecial 38	java/security/KeyException:<init>	(Ljava/lang/String;)V
/*     */     //   54: athrow
/*     */     // Line number table:
/*     */     //   Java source line #106	-> byte code offset #0
/*     */     //   Java source line #109	-> byte code offset #9
/*     */     //   Java source line #111	-> byte code offset #14
/*     */     //   Java source line #109	-> byte code offset #18
/*     */     //   Java source line #111	-> byte code offset #20
/*     */     //   Java source line #112	-> byte code offset #25
/*     */     //   Java source line #113	-> byte code offset #27
/*     */     //   Java source line #114	-> byte code offset #28
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	55	0	file	java.io.File
/*     */     //   8	14	1	in	InputStream
/*     */     //   27	2	1	e	java.io.FileNotFoundException
/*     */     //   20	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   9	14	20	finally
/*     */     //   0	18	27	java/io/FileNotFoundException
/*     */     //   20	27	27	java/io/FileNotFoundException
/*     */   }
/*     */   
/*     */   static ByteBuf readPrivateKey(InputStream in)
/*     */     throws KeyException
/*     */   {
/*     */     try
/*     */     {
/* 121 */       content = readContent(in);
/*     */     } catch (IOException e) { String content;
/* 123 */       throw new KeyException("failed to read key input stream", e);
/*     */     }
/*     */     String content;
/* 126 */     Matcher m = KEY_PATTERN.matcher(content);
/* 127 */     if (!m.find()) {
/* 128 */       throw new KeyException("could not find a PKCS #8 private key in input stream (see http://netty.io/wiki/sslcontextbuilder-and-private-key.html for more information)");
/*     */     }
/*     */     
/*     */ 
/* 132 */     ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
/* 133 */     ByteBuf der = Base64.decode(base64);
/* 134 */     base64.release();
/* 135 */     return der;
/*     */   }
/*     */   
/*     */   private static String readContent(InputStream in) throws IOException {
/* 139 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 141 */       byte[] buf = new byte[' '];
/*     */       int ret;
/* 143 */       for (;;) { ret = in.read(buf);
/* 144 */         if (ret < 0) {
/*     */           break;
/*     */         }
/* 147 */         out.write(buf, 0, ret);
/*     */       }
/* 149 */       return out.toString(CharsetUtil.US_ASCII.name());
/*     */     } finally {
/* 151 */       safeClose(out);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(InputStream in) {
/*     */     try {
/* 157 */       in.close();
/*     */     } catch (IOException e) {
/* 159 */       logger.warn("Failed to close a stream.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(OutputStream out) {
/*     */     try {
/* 165 */       out.close();
/*     */     } catch (IOException e) {
/* 167 */       logger.warn("Failed to close a stream.", e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\PemReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */