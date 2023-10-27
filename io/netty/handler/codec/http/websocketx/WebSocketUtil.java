/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WebSocketUtil
/*     */ {
/*  33 */   private static final FastThreadLocal<MessageDigest> MD5 = new FastThreadLocal()
/*     */   {
/*     */     protected MessageDigest initialValue() throws Exception
/*     */     {
/*     */       try {
/*  38 */         return MessageDigest.getInstance("MD5");
/*     */       }
/*     */       catch (NoSuchAlgorithmException e) {
/*  41 */         throw new InternalError("MD5 not supported on this platform - Outdated?");
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*  46 */   private static final FastThreadLocal<MessageDigest> SHA1 = new FastThreadLocal()
/*     */   {
/*     */     protected MessageDigest initialValue() throws Exception
/*     */     {
/*     */       try {
/*  51 */         return MessageDigest.getInstance("SHA1");
/*     */       }
/*     */       catch (NoSuchAlgorithmException e) {
/*  54 */         throw new InternalError("SHA-1 not supported on this platform - Outdated?");
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] md5(byte[] data)
/*     */   {
/*  67 */     return digest(MD5, data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] sha1(byte[] data)
/*     */   {
/*  78 */     return digest(SHA1, data);
/*     */   }
/*     */   
/*     */   private static byte[] digest(FastThreadLocal<MessageDigest> digestFastThreadLocal, byte[] data) {
/*  82 */     MessageDigest digest = (MessageDigest)digestFastThreadLocal.get();
/*  83 */     digest.reset();
/*  84 */     return digest.digest(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String base64(byte[] data)
/*     */   {
/*  94 */     ByteBuf encodedData = Unpooled.wrappedBuffer(data);
/*  95 */     ByteBuf encoded = Base64.encode(encodedData);
/*  96 */     String encodedString = encoded.toString(CharsetUtil.UTF_8);
/*  97 */     encoded.release();
/*  98 */     return encodedString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] randomBytes(int size)
/*     */   {
/* 108 */     byte[] bytes = new byte[size];
/* 109 */     PlatformDependent.threadLocalRandom().nextBytes(bytes);
/* 110 */     return bytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int randomNumber(int minimum, int maximum)
/*     */   {
/* 121 */     assert (minimum < maximum);
/* 122 */     double fraction = PlatformDependent.threadLocalRandom().nextDouble();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */     return (int)(minimum + fraction * (maximum - minimum));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */