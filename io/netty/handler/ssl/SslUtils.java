/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SslUtils
/*     */ {
/*     */   static final String PROTOCOL_SSL_V2_HELLO = "SSLv2Hello";
/*     */   static final String PROTOCOL_SSL_V2 = "SSLv2";
/*     */   static final String PROTOCOL_SSL_V3 = "SSLv3";
/*     */   static final String PROTOCOL_TLS_V1 = "TLSv1";
/*     */   static final String PROTOCOL_TLS_V1_1 = "TLSv1.1";
/*     */   static final String PROTOCOL_TLS_V1_2 = "TLSv1.2";
/*     */   static final int SSL_CONTENT_TYPE_CHANGE_CIPHER_SPEC = 20;
/*     */   static final int SSL_CONTENT_TYPE_ALERT = 21;
/*     */   static final int SSL_CONTENT_TYPE_HANDSHAKE = 22;
/*     */   static final int SSL_CONTENT_TYPE_APPLICATION_DATA = 23;
/*     */   static final int SSL_CONTENT_TYPE_EXTENSION_HEARTBEAT = 24;
/*     */   static final int SSL_RECORD_HEADER_LENGTH = 5;
/*     */   static final int NOT_ENOUGH_DATA = -1;
/*     */   static final int NOT_ENCRYPTED = -2;
/*  88 */   static final String[] DEFAULT_CIPHER_SUITES = { "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void addIfSupported(Set<String> supported, List<String> enabled, String... names)
/*     */   {
/* 107 */     for (String n : names) {
/* 108 */       if (supported.contains(n)) {
/* 109 */         enabled.add(n);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void useFallbackCiphersIfDefaultIsEmpty(List<String> defaultCiphers, Iterable<String> fallbackCiphers) {
/* 115 */     if (defaultCiphers.isEmpty()) {
/* 116 */       for (String cipher : fallbackCiphers) {
/* 117 */         if ((!cipher.startsWith("SSL_")) && (!cipher.contains("_RC4_")))
/*     */         {
/*     */ 
/* 120 */           defaultCiphers.add(cipher); }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void useFallbackCiphersIfDefaultIsEmpty(List<String> defaultCiphers, String... fallbackCiphers) {
/* 126 */     useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, Arrays.asList(fallbackCiphers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static SSLHandshakeException toSSLHandshakeException(Throwable e)
/*     */   {
/* 133 */     if ((e instanceof SSLHandshakeException)) {
/* 134 */       return (SSLHandshakeException)e;
/*     */     }
/*     */     
/* 137 */     return (SSLHandshakeException)new SSLHandshakeException(e.getMessage()).initCause(e);
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
/*     */   static int getEncryptedPacketLength(ByteBuf buffer, int offset)
/*     */   {
/* 158 */     int packetLength = 0;
/*     */     
/*     */     boolean tls;
/*     */     boolean tls;
/* 162 */     switch (buffer.getUnsignedByte(offset)) {
/*     */     case 20: 
/*     */     case 21: 
/*     */     case 22: 
/*     */     case 23: 
/*     */     case 24: 
/* 168 */       tls = true;
/* 169 */       break;
/*     */     
/*     */     default: 
/* 172 */       tls = false;
/*     */     }
/*     */     
/* 175 */     if (tls)
/*     */     {
/* 177 */       int majorVersion = buffer.getUnsignedByte(offset + 1);
/* 178 */       if (majorVersion == 3)
/*     */       {
/* 180 */         packetLength = unsignedShortBE(buffer, offset + 3) + 5;
/* 181 */         if (packetLength <= 5)
/*     */         {
/* 183 */           tls = false;
/*     */         }
/*     */       }
/*     */       else {
/* 187 */         tls = false;
/*     */       }
/*     */     }
/*     */     
/* 191 */     if (!tls)
/*     */     {
/* 193 */       int headerLength = (buffer.getUnsignedByte(offset) & 0x80) != 0 ? 2 : 3;
/* 194 */       int majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
/* 195 */       if ((majorVersion == 2) || (majorVersion == 3))
/*     */       {
/*     */ 
/* 198 */         packetLength = headerLength == 2 ? (shortBE(buffer, offset) & 0x7FFF) + 2 : (shortBE(buffer, offset) & 0x3FFF) + 3;
/* 199 */         if (packetLength <= headerLength) {
/* 200 */           return -1;
/*     */         }
/*     */       } else {
/* 203 */         return -2;
/*     */       }
/*     */     }
/* 206 */     return packetLength;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int unsignedShortBE(ByteBuf buffer, int offset)
/*     */   {
/* 212 */     return buffer.order() == ByteOrder.BIG_ENDIAN ? buffer
/* 213 */       .getUnsignedShort(offset) : buffer.getUnsignedShortLE(offset);
/*     */   }
/*     */   
/*     */ 
/*     */   private static short shortBE(ByteBuf buffer, int offset)
/*     */   {
/* 219 */     return buffer.order() == ByteOrder.BIG_ENDIAN ? buffer
/* 220 */       .getShort(offset) : buffer.getShortLE(offset);
/*     */   }
/*     */   
/*     */   private static short unsignedByte(byte b) {
/* 224 */     return (short)(b & 0xFF);
/*     */   }
/*     */   
/*     */   private static int unsignedShortBE(ByteBuffer buffer, int offset)
/*     */   {
/* 229 */     return shortBE(buffer, offset) & 0xFFFF;
/*     */   }
/*     */   
/*     */   private static short shortBE(ByteBuffer buffer, int offset)
/*     */   {
/* 234 */     return buffer.order() == ByteOrder.BIG_ENDIAN ? buffer
/* 235 */       .getShort(offset) : ByteBufUtil.swapShort(buffer.getShort(offset));
/*     */   }
/*     */   
/*     */   static int getEncryptedPacketLength(ByteBuffer[] buffers, int offset) {
/* 239 */     ByteBuffer buffer = buffers[offset];
/*     */     
/*     */ 
/* 242 */     if (buffer.remaining() >= 5) {
/* 243 */       return getEncryptedPacketLength(buffer);
/*     */     }
/*     */     
/*     */ 
/* 247 */     ByteBuffer tmp = ByteBuffer.allocate(5);
/*     */     do
/*     */     {
/* 250 */       buffer = buffers[(offset++)].duplicate();
/* 251 */       if (buffer.remaining() > tmp.remaining()) {
/* 252 */         buffer.limit(buffer.position() + tmp.remaining());
/*     */       }
/* 254 */       tmp.put(buffer);
/* 255 */     } while (tmp.hasRemaining());
/*     */     
/*     */ 
/* 258 */     tmp.flip();
/* 259 */     return getEncryptedPacketLength(tmp);
/*     */   }
/*     */   
/*     */   private static int getEncryptedPacketLength(ByteBuffer buffer) {
/* 263 */     int packetLength = 0;
/* 264 */     int pos = buffer.position();
/*     */     boolean tls;
/*     */     boolean tls;
/* 267 */     switch (unsignedByte(buffer.get(pos))) {
/*     */     case 20: 
/*     */     case 21: 
/*     */     case 22: 
/*     */     case 23: 
/*     */     case 24: 
/* 273 */       tls = true;
/* 274 */       break;
/*     */     
/*     */     default: 
/* 277 */       tls = false;
/*     */     }
/*     */     
/* 280 */     if (tls)
/*     */     {
/* 282 */       int majorVersion = unsignedByte(buffer.get(pos + 1));
/* 283 */       if (majorVersion == 3)
/*     */       {
/* 285 */         packetLength = unsignedShortBE(buffer, pos + 3) + 5;
/* 286 */         if (packetLength <= 5)
/*     */         {
/* 288 */           tls = false;
/*     */         }
/*     */       }
/*     */       else {
/* 292 */         tls = false;
/*     */       }
/*     */     }
/*     */     
/* 296 */     if (!tls)
/*     */     {
/* 298 */       int headerLength = (unsignedByte(buffer.get(pos)) & 0x80) != 0 ? 2 : 3;
/* 299 */       int majorVersion = unsignedByte(buffer.get(pos + headerLength + 1));
/* 300 */       if ((majorVersion == 2) || (majorVersion == 3))
/*     */       {
/*     */ 
/* 303 */         packetLength = headerLength == 2 ? (shortBE(buffer, pos) & 0x7FFF) + 2 : (shortBE(buffer, pos) & 0x3FFF) + 3;
/* 304 */         if (packetLength <= headerLength) {
/* 305 */           return -1;
/*     */         }
/*     */       } else {
/* 308 */         return -2;
/*     */       }
/*     */     }
/* 311 */     return packetLength;
/*     */   }
/*     */   
/*     */ 
/*     */   static void handleHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean notify)
/*     */   {
/* 317 */     ctx.flush();
/* 318 */     if (notify) {
/* 319 */       ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
/*     */     }
/* 321 */     ctx.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void zeroout(ByteBuf buffer)
/*     */   {
/* 328 */     if (!buffer.isReadOnly()) {
/* 329 */       buffer.setZero(0, buffer.capacity());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void zerooutAndRelease(ByteBuf buffer)
/*     */   {
/* 337 */     zeroout(buffer);
/* 338 */     buffer.release();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static ByteBuf toBase64(ByteBufAllocator allocator, ByteBuf src)
/*     */   {
/* 347 */     ByteBuf dst = Base64.encode(src, src.readerIndex(), src
/* 348 */       .readableBytes(), true, Base64Dialect.STANDARD, allocator);
/* 349 */     src.readerIndex(src.writerIndex());
/* 350 */     return dst;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isValidHostNameForSNI(String hostname)
/*     */   {
/* 357 */     return (hostname != null) && 
/* 358 */       (hostname.indexOf('.') > 0) && 
/* 359 */       (!hostname.endsWith(".")) && 
/* 360 */       (!NetUtil.isValidIpV4Address(hostname)) && 
/* 361 */       (!NetUtil.isValidIpV6Address(hostname));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\SslUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */