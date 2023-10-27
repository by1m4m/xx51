/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public class B64Code
/*     */ {
/*     */   private static final char __pad = '=';
/*  38 */   private static final char[] __rfc1421alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
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
/*  49 */   private static final byte[] __rfc1421nibbles = new byte['Ā'];
/*  50 */   static { for (int i = 0; i < 256; i++)
/*  51 */       __rfc1421nibbles[i] = -1;
/*  52 */     for (byte b = 0; b < 64; b = (byte)(b + 1))
/*  53 */       __rfc1421nibbles[((byte)__rfc1421alphabet[b])] = b;
/*  54 */     __rfc1421nibbles[61] = 0;
/*     */     
/*     */ 
/*  57 */     __rfc4648urlAlphabet = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };
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
/*  68 */     __rfc4648urlNibbles = new byte['Ā'];
/*  69 */     for (int i = 0; i < 256; i++)
/*  70 */       __rfc4648urlNibbles[i] = -1;
/*  71 */     for (byte b = 0; b < 64; b = (byte)(b + 1))
/*  72 */       __rfc4648urlNibbles[((byte)__rfc4648urlAlphabet[b])] = b;
/*  73 */     __rfc4648urlNibbles[61] = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final char[] __rfc4648urlAlphabet;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(String s)
/*     */   {
/*  88 */     return encode(s, (Charset)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(String s, String charEncoding)
/*     */   {
/*     */     byte[] bytes;
/*     */     
/*     */ 
/*     */     byte[] bytes;
/*     */     
/*     */ 
/* 102 */     if (charEncoding == null) {
/* 103 */       bytes = s.getBytes(StandardCharsets.ISO_8859_1);
/*     */     } else
/* 105 */       bytes = s.getBytes(Charset.forName(charEncoding));
/* 106 */     return new String(encode(bytes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(String s, Charset charEncoding)
/*     */   {
/* 118 */     byte[] bytes = s.getBytes(charEncoding == null ? StandardCharsets.ISO_8859_1 : charEncoding);
/* 119 */     return new String(encode(bytes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char[] encode(byte[] b)
/*     */   {
/* 131 */     if (b == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     int bLen = b.length;
/* 135 */     int cLen = (bLen + 2) / 3 * 4;
/* 136 */     char[] c = new char[cLen];
/* 137 */     int ci = 0;
/* 138 */     int bi = 0;
/*     */     
/* 140 */     int stop = bLen / 3 * 3;
/* 141 */     while (bi < stop)
/*     */     {
/* 143 */       byte b0 = b[(bi++)];
/* 144 */       byte b1 = b[(bi++)];
/* 145 */       byte b2 = b[(bi++)];
/* 146 */       c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 147 */       c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F | b1 >>> 4 & 0xF)];
/* 148 */       c[(ci++)] = __rfc1421alphabet[(b1 << 2 & 0x3F | b2 >>> 6 & 0x3)];
/* 149 */       c[(ci++)] = __rfc1421alphabet[(b2 & 0x3F)];
/*     */     }
/*     */     
/* 152 */     if (bLen != bi)
/*     */     {
/* 154 */       switch (bLen % 3)
/*     */       {
/*     */       case 2: 
/* 157 */         byte b0 = b[(bi++)];
/* 158 */         byte b1 = b[(bi++)];
/* 159 */         c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 160 */         c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F | b1 >>> 4 & 0xF)];
/* 161 */         c[(ci++)] = __rfc1421alphabet[(b1 << 2 & 0x3F)];
/* 162 */         c[(ci++)] = '=';
/* 163 */         break;
/*     */       
/*     */       case 1: 
/* 166 */         byte b0 = b[(bi++)];
/* 167 */         c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 168 */         c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F)];
/* 169 */         c[(ci++)] = '=';
/* 170 */         c[(ci++)] = '=';
/* 171 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 178 */     return c;
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
/*     */   public static char[] encode(byte[] b, boolean rfc2045)
/*     */   {
/* 191 */     if (b == null)
/* 192 */       return null;
/* 193 */     if (!rfc2045) {
/* 194 */       return encode(b);
/*     */     }
/* 196 */     int bLen = b.length;
/* 197 */     int cLen = (bLen + 2) / 3 * 4;
/* 198 */     cLen += 2 + 2 * (cLen / 76);
/* 199 */     char[] c = new char[cLen];
/* 200 */     int ci = 0;
/* 201 */     int bi = 0;
/*     */     
/* 203 */     int stop = bLen / 3 * 3;
/* 204 */     int l = 0;
/* 205 */     while (bi < stop)
/*     */     {
/* 207 */       byte b0 = b[(bi++)];
/* 208 */       byte b1 = b[(bi++)];
/* 209 */       byte b2 = b[(bi++)];
/* 210 */       c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 211 */       c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F | b1 >>> 4 & 0xF)];
/* 212 */       c[(ci++)] = __rfc1421alphabet[(b1 << 2 & 0x3F | b2 >>> 6 & 0x3)];
/* 213 */       c[(ci++)] = __rfc1421alphabet[(b2 & 0x3F)];
/* 214 */       l += 4;
/* 215 */       if (l % 76 == 0)
/*     */       {
/* 217 */         c[(ci++)] = '\r';
/* 218 */         c[(ci++)] = '\n';
/*     */       }
/*     */     }
/*     */     
/* 222 */     if (bLen != bi)
/*     */     {
/* 224 */       switch (bLen % 3)
/*     */       {
/*     */       case 2: 
/* 227 */         byte b0 = b[(bi++)];
/* 228 */         byte b1 = b[(bi++)];
/* 229 */         c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 230 */         c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F | b1 >>> 4 & 0xF)];
/* 231 */         c[(ci++)] = __rfc1421alphabet[(b1 << 2 & 0x3F)];
/* 232 */         c[(ci++)] = '=';
/* 233 */         break;
/*     */       
/*     */       case 1: 
/* 236 */         byte b0 = b[(bi++)];
/* 237 */         c[(ci++)] = __rfc1421alphabet[(b0 >>> 2 & 0x3F)];
/* 238 */         c[(ci++)] = __rfc1421alphabet[(b0 << 4 & 0x3F)];
/* 239 */         c[(ci++)] = '=';
/* 240 */         c[(ci++)] = '=';
/* 241 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 248 */     c[(ci++)] = '\r';
/* 249 */     c[(ci++)] = '\n';
/* 250 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte[] __rfc4648urlNibbles;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String decode(String encoded, String charEncoding)
/*     */   {
/* 268 */     byte[] decoded = decode(encoded);
/* 269 */     if (charEncoding == null)
/* 270 */       return new String(decoded);
/* 271 */     return new String(decoded, Charset.forName(charEncoding));
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
/*     */   public static String decode(String encoded, Charset charEncoding)
/*     */   {
/* 288 */     byte[] decoded = decode(encoded);
/* 289 */     if (charEncoding == null)
/* 290 */       return new String(decoded);
/* 291 */     return new String(decoded, charEncoding);
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
/*     */   public static byte[] decode(char[] b)
/*     */   {
/* 308 */     if (b == null) {
/* 309 */       return null;
/*     */     }
/* 311 */     int bLen = b.length;
/* 312 */     if (bLen % 4 != 0) {
/* 313 */       throw new IllegalArgumentException("Input block size is not 4");
/*     */     }
/* 315 */     int li = bLen - 1;
/* 316 */     while ((li >= 0) && (b[li] == '=')) {
/* 317 */       li--;
/*     */     }
/* 319 */     if (li < 0) {
/* 320 */       return new byte[0];
/*     */     }
/*     */     
/* 323 */     int rLen = (li + 1) * 3 / 4;
/* 324 */     byte[] r = new byte[rLen];
/* 325 */     int ri = 0;
/* 326 */     int bi = 0;
/* 327 */     int stop = rLen / 3 * 3;
/*     */     
/*     */     try
/*     */     {
/* 331 */       while (ri < stop)
/*     */       {
/* 333 */         byte b0 = __rfc1421nibbles[b[(bi++)]];
/* 334 */         byte b1 = __rfc1421nibbles[b[(bi++)]];
/* 335 */         byte b2 = __rfc1421nibbles[b[(bi++)]];
/* 336 */         byte b3 = __rfc1421nibbles[b[(bi++)]];
/* 337 */         if ((b0 < 0) || (b1 < 0) || (b2 < 0) || (b3 < 0)) {
/* 338 */           throw new IllegalArgumentException("Not B64 encoded");
/*     */         }
/* 340 */         r[(ri++)] = ((byte)(b0 << 2 | b1 >>> 4));
/* 341 */         r[(ri++)] = ((byte)(b1 << 4 | b2 >>> 2));
/* 342 */         r[(ri++)] = ((byte)(b2 << 6 | b3));
/*     */       }
/*     */       
/* 345 */       if (rLen != ri)
/*     */       {
/* 347 */         switch (rLen % 3)
/*     */         {
/*     */         case 2: 
/* 350 */           byte b0 = __rfc1421nibbles[b[(bi++)]];
/* 351 */           byte b1 = __rfc1421nibbles[b[(bi++)]];
/* 352 */           byte b2 = __rfc1421nibbles[b[(bi++)]];
/* 353 */           if ((b0 < 0) || (b1 < 0) || (b2 < 0))
/* 354 */             throw new IllegalArgumentException("Not B64 encoded");
/* 355 */           r[(ri++)] = ((byte)(b0 << 2 | b1 >>> 4));
/* 356 */           r[(ri++)] = ((byte)(b1 << 4 | b2 >>> 2));
/* 357 */           break;
/*     */         
/*     */         case 1: 
/* 360 */           byte b0 = __rfc1421nibbles[b[(bi++)]];
/* 361 */           byte b1 = __rfc1421nibbles[b[(bi++)]];
/* 362 */           if ((b0 < 0) || (b1 < 0))
/* 363 */             throw new IllegalArgumentException("Not B64 encoded");
/* 364 */           r[(ri++)] = ((byte)(b0 << 2 | b1 >>> 4));
/*     */         
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 374 */       throw new IllegalArgumentException("char " + bi + " was not B64 encoded");
/*     */     }
/*     */     
/*     */ 
/* 378 */     return r;
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
/*     */   public static byte[] decode(String encoded)
/*     */   {
/* 391 */     if (encoded == null) {
/* 392 */       return null;
/*     */     }
/* 394 */     ByteArrayOutputStream bout = new ByteArrayOutputStream(4 * encoded.length() / 3);
/* 395 */     decode(encoded, bout);
/* 396 */     return bout.toByteArray();
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
/*     */   public static void decode(String encoded, ByteArrayOutputStream bout)
/*     */   {
/* 410 */     if (encoded == null) {
/* 411 */       return;
/*     */     }
/* 413 */     if (bout == null) {
/* 414 */       throw new IllegalArgumentException("No outputstream for decoded bytes");
/*     */     }
/* 416 */     int ci = 0;
/* 417 */     byte[] nibbles = new byte[4];
/* 418 */     int s = 0;
/*     */     
/* 420 */     while (ci < encoded.length())
/*     */     {
/* 422 */       char c = encoded.charAt(ci++);
/*     */       
/* 424 */       if (c == '=') {
/*     */         break;
/*     */       }
/* 427 */       if (!Character.isWhitespace(c))
/*     */       {
/*     */ 
/* 430 */         byte nibble = __rfc1421nibbles[c];
/* 431 */         if (nibble < 0) {
/* 432 */           throw new IllegalArgumentException("Not B64 encoded");
/*     */         }
/* 434 */         nibbles[(s++)] = __rfc1421nibbles[c];
/*     */         
/* 436 */         switch (s)
/*     */         {
/*     */         case 1: 
/*     */           break;
/*     */         case 2: 
/* 441 */           bout.write(nibbles[0] << 2 | nibbles[1] >>> 4);
/* 442 */           break;
/*     */         case 3: 
/* 444 */           bout.write(nibbles[1] << 4 | nibbles[2] >>> 2);
/* 445 */           break;
/*     */         case 4: 
/* 447 */           bout.write(nibbles[2] << 6 | nibbles[3]);
/* 448 */           s = 0;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] decodeRFC4648URL(String encoded)
/*     */   {
/* 460 */     if (encoded == null) {
/* 461 */       return null;
/*     */     }
/* 463 */     ByteArrayOutputStream bout = new ByteArrayOutputStream(4 * encoded.length() / 3);
/* 464 */     decodeRFC4648URL(encoded, bout);
/* 465 */     return bout.toByteArray();
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
/*     */   public static void decodeRFC4648URL(String encoded, ByteArrayOutputStream bout)
/*     */   {
/* 479 */     if (encoded == null) {
/* 480 */       return;
/*     */     }
/* 482 */     if (bout == null) {
/* 483 */       throw new IllegalArgumentException("No outputstream for decoded bytes");
/*     */     }
/* 485 */     int ci = 0;
/* 486 */     byte[] nibbles = new byte[4];
/* 487 */     int s = 0;
/*     */     
/* 489 */     while (ci < encoded.length())
/*     */     {
/* 491 */       char c = encoded.charAt(ci++);
/*     */       
/* 493 */       if (c == '=') {
/*     */         break;
/*     */       }
/* 496 */       if (!Character.isWhitespace(c))
/*     */       {
/*     */ 
/* 499 */         byte nibble = __rfc4648urlNibbles[c];
/* 500 */         if (nibble < 0) {
/* 501 */           throw new IllegalArgumentException("Not B64 encoded");
/*     */         }
/* 503 */         nibbles[(s++)] = __rfc4648urlNibbles[c];
/*     */         
/* 505 */         switch (s)
/*     */         {
/*     */         case 1: 
/*     */           break;
/*     */         case 2: 
/* 510 */           bout.write(nibbles[0] << 2 | nibbles[1] >>> 4);
/* 511 */           break;
/*     */         case 3: 
/* 513 */           bout.write(nibbles[1] << 4 | nibbles[2] >>> 2);
/* 514 */           break;
/*     */         case 4: 
/* 516 */           bout.write(nibbles[2] << 6 | nibbles[3]);
/* 517 */           s = 0;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void encode(int value, Appendable buf)
/*     */     throws IOException
/*     */   {
/* 529 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC000000 & value) >> 26)]);
/* 530 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F00000 & value) >> 20)]);
/* 531 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC000 & value) >> 14)]);
/* 532 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F00 & value) >> 8)]);
/* 533 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC & value) >> 2)]);
/* 534 */     buf.append(__rfc1421alphabet[(0x3F & (0x3 & value) << 4)]);
/* 535 */     buf.append('=');
/*     */   }
/*     */   
/*     */   public static void encode(long lvalue, Appendable buf) throws IOException
/*     */   {
/* 540 */     int value = (int)(0xFFFFFFFFFFFFFFFC & lvalue >> 32);
/* 541 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC000000 & value) >> 26)]);
/* 542 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F00000 & value) >> 20)]);
/* 543 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC000 & value) >> 14)]);
/* 544 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F00 & value) >> 8)]);
/* 545 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC & value) >> 2)]);
/*     */     
/* 547 */     buf.append(__rfc1421alphabet[(0x3F & ((0x3 & value) << 4) + (0xF & (int)(lvalue >> 28)))]);
/*     */     
/* 549 */     value = 0xFFFFFFF & (int)lvalue;
/* 550 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC00000 & value) >> 22)]);
/* 551 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F0000 & value) >> 16)]);
/* 552 */     buf.append(__rfc1421alphabet[(0x3F & (0xFC00 & value) >> 10)]);
/* 553 */     buf.append(__rfc1421alphabet[(0x3F & (0x3F0 & value) >> 4)]);
/* 554 */     buf.append(__rfc1421alphabet[(0x3F & (0xF & value) << 2)]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\B64Code.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */