/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlEncoded
/*     */   extends MultiMap<String>
/*     */   implements Cloneable
/*     */ {
/*  61 */   static final Logger LOG = Log.getLogger(UrlEncoded.class);
/*     */   public static final Charset ENCODING;
/*     */   
/*     */   static
/*     */   {
/*     */     Charset encoding;
/*     */     try
/*     */     {
/*  69 */       String charset = System.getProperty("org.eclipse.jetty.util.UrlEncoding.charset");
/*  70 */       encoding = charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset);
/*     */     }
/*     */     catch (Exception e) {
/*     */       Charset encoding;
/*  74 */       LOG.warn(e);
/*  75 */       encoding = StandardCharsets.UTF_8;
/*     */     }
/*  77 */     ENCODING = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */   public UrlEncoded(UrlEncoded url)
/*     */   {
/*  83 */     super(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlEncoded(String query)
/*     */   {
/*  93 */     decodeTo(query, this, ENCODING);
/*     */   }
/*     */   
/*     */ 
/*     */   public void decode(String query)
/*     */   {
/*  99 */     decodeTo(query, this, ENCODING);
/*     */   }
/*     */   
/*     */ 
/*     */   public void decode(String query, Charset charset)
/*     */   {
/* 105 */     decodeTo(query, this, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encode()
/*     */   {
/* 114 */     return encode(ENCODING, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encode(Charset charset)
/*     */   {
/* 124 */     return encode(charset, false);
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
/*     */   public synchronized String encode(Charset charset, boolean equalsForNullValue)
/*     */   {
/* 137 */     return encode(this, charset, equalsForNullValue);
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
/*     */   public static String encode(MultiMap<String> map, Charset charset, boolean equalsForNullValue)
/*     */   {
/* 150 */     if (charset == null) {
/* 151 */       charset = ENCODING;
/*     */     }
/* 153 */     StringBuilder result = new StringBuilder(128);
/*     */     
/* 155 */     boolean delim = false;
/* 156 */     for (Map.Entry<String, List<String>> entry : map.entrySet())
/*     */     {
/* 158 */       String key = ((String)entry.getKey()).toString();
/* 159 */       List<String> list = (List)entry.getValue();
/* 160 */       int s = list.size();
/*     */       
/* 162 */       if (delim)
/*     */       {
/* 164 */         result.append('&');
/*     */       }
/*     */       
/* 167 */       if (s == 0)
/*     */       {
/* 169 */         result.append(encodeString(key, charset));
/* 170 */         if (equalsForNullValue) {
/* 171 */           result.append('=');
/*     */         }
/*     */       }
/*     */       else {
/* 175 */         for (int i = 0; i < s; i++)
/*     */         {
/* 177 */           if (i > 0)
/* 178 */             result.append('&');
/* 179 */           String val = (String)list.get(i);
/* 180 */           result.append(encodeString(key, charset));
/*     */           
/* 182 */           if (val != null)
/*     */           {
/* 184 */             String str = val.toString();
/* 185 */             if (str.length() > 0)
/*     */             {
/* 187 */               result.append('=');
/* 188 */               result.append(encodeString(str, charset));
/*     */             }
/* 190 */             else if (equalsForNullValue) {
/* 191 */               result.append('=');
/*     */             }
/* 193 */           } else if (equalsForNullValue) {
/* 194 */             result.append('=');
/*     */           }
/*     */         } }
/* 197 */       delim = true;
/*     */     }
/* 199 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void decodeTo(String content, MultiMap<String> map, String charset)
/*     */   {
/* 210 */     decodeTo(content, map, charset == null ? null : Charset.forName(charset));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void decodeTo(String content, MultiMap<String> map, Charset charset)
/*     */   {
/* 221 */     if (charset == null) {
/* 222 */       charset = ENCODING;
/*     */     }
/* 224 */     if (StandardCharsets.UTF_8.equals(charset))
/*     */     {
/* 226 */       decodeUtf8To(content, 0, content.length(), map);
/* 227 */       return;
/*     */     }
/*     */     
/* 230 */     synchronized (map)
/*     */     {
/* 232 */       String key = null;
/* 233 */       String value = null;
/* 234 */       int mark = -1;
/* 235 */       boolean encoded = false;
/* 236 */       for (int i = 0; i < content.length(); i++)
/*     */       {
/* 238 */         char c = content.charAt(i);
/* 239 */         switch (c)
/*     */         {
/*     */         case '&': 
/* 242 */           int l = i - mark - 1;
/*     */           
/* 244 */           value = encoded ? decodeString(content, mark + 1, l, charset) : l == 0 ? "" : content.substring(mark + 1, i);
/* 245 */           mark = i;
/* 246 */           encoded = false;
/* 247 */           if (key != null)
/*     */           {
/* 249 */             map.add(key, value);
/*     */           }
/* 251 */           else if ((value != null) && (value.length() > 0))
/*     */           {
/* 253 */             map.add(value, "");
/*     */           }
/* 255 */           key = null;
/* 256 */           value = null;
/* 257 */           break;
/*     */         case '=': 
/* 259 */           if (key == null)
/*     */           {
/* 261 */             key = encoded ? decodeString(content, mark + 1, i - mark - 1, charset) : content.substring(mark + 1, i);
/* 262 */             mark = i;
/* 263 */             encoded = false; }
/* 264 */           break;
/*     */         case '+': 
/* 266 */           encoded = true;
/* 267 */           break;
/*     */         case '%': 
/* 269 */           encoded = true;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 274 */       if (key != null)
/*     */       {
/* 276 */         int l = content.length() - mark - 1;
/* 277 */         value = encoded ? decodeString(content, mark + 1, l, charset) : l == 0 ? "" : content.substring(mark + 1);
/* 278 */         map.add(key, value);
/*     */       }
/* 280 */       else if (mark < content.length())
/*     */       {
/*     */ 
/*     */ 
/* 284 */         key = encoded ? decodeString(content, mark + 1, content.length() - mark - 1, charset) : content.substring(mark + 1);
/* 285 */         if ((key != null) && (key.length() > 0))
/*     */         {
/* 287 */           map.add(key, "");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void decodeUtf8To(String query, MultiMap<String> map)
/*     */   {
/* 296 */     decodeUtf8To(query, 0, query.length(), map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void decodeUtf8To(String query, int offset, int length, MultiMap<String> map)
/*     */   {
/* 308 */     Utf8StringBuilder buffer = new Utf8StringBuilder();
/* 309 */     synchronized (map)
/*     */     {
/* 311 */       String key = null;
/* 312 */       String value = null;
/*     */       
/* 314 */       int end = offset + length;
/* 315 */       for (int i = offset; i < end; i++)
/*     */       {
/* 317 */         char c = query.charAt(i);
/* 318 */         switch (c)
/*     */         {
/*     */         case '&': 
/* 321 */           value = buffer.toReplacedString();
/* 322 */           buffer.reset();
/* 323 */           if (key != null)
/*     */           {
/* 325 */             map.add(key, value);
/*     */           }
/* 327 */           else if ((value != null) && (value.length() > 0))
/*     */           {
/* 329 */             map.add(value, "");
/*     */           }
/* 331 */           key = null;
/* 332 */           value = null;
/* 333 */           break;
/*     */         
/*     */         case '=': 
/* 336 */           if (key != null)
/*     */           {
/* 338 */             buffer.append(c);
/*     */           }
/*     */           else {
/* 341 */             key = buffer.toReplacedString();
/* 342 */             buffer.reset(); }
/* 343 */           break;
/*     */         
/*     */         case '+': 
/* 346 */           buffer.append((byte)32);
/* 347 */           break;
/*     */         
/*     */         case '%': 
/* 350 */           if (i + 2 < end)
/*     */           {
/* 352 */             char hi = query.charAt(++i);
/* 353 */             char lo = query.charAt(++i);
/* 354 */             buffer.append(decodeHexByte(hi, lo));
/*     */           }
/*     */           else
/*     */           {
/* 358 */             throw new Utf8Appendable.NotUtf8Exception("Incomplete % encoding");
/*     */           }
/*     */           
/*     */           break;
/*     */         default: 
/* 363 */           buffer.append(c);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 368 */       if (key != null)
/*     */       {
/* 370 */         value = buffer.toReplacedString();
/* 371 */         buffer.reset();
/* 372 */         map.add(key, value);
/*     */       }
/* 374 */       else if (buffer.length() > 0)
/*     */       {
/* 376 */         map.add(buffer.toReplacedString(), "");
/*     */       }
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
/*     */   public static void decode88591To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
/*     */     throws IOException
/*     */   {
/* 393 */     synchronized (map)
/*     */     {
/* 395 */       StringBuffer buffer = new StringBuffer();
/* 396 */       String key = null;
/* 397 */       String value = null;
/*     */       
/*     */ 
/*     */ 
/* 401 */       int totalLength = 0;
/* 402 */       int b; while ((b = in.read()) >= 0)
/*     */       {
/* 404 */         switch ((char)b)
/*     */         {
/*     */         case '&': 
/* 407 */           value = buffer.length() == 0 ? "" : buffer.toString();
/* 408 */           buffer.setLength(0);
/* 409 */           if (key != null)
/*     */           {
/* 411 */             map.add(key, value);
/*     */           }
/* 413 */           else if ((value != null) && (value.length() > 0))
/*     */           {
/* 415 */             map.add(value, "");
/*     */           }
/* 417 */           key = null;
/* 418 */           value = null;
/* 419 */           if ((maxKeys > 0) && (map.size() > maxKeys)) {
/* 420 */             throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
/*     */           }
/*     */           break;
/*     */         case '=': 
/* 424 */           if (key != null)
/*     */           {
/* 426 */             buffer.append((char)b);
/*     */           }
/*     */           else {
/* 429 */             key = buffer.toString();
/* 430 */             buffer.setLength(0); }
/* 431 */           break;
/*     */         
/*     */         case '+': 
/* 434 */           buffer.append(' ');
/* 435 */           break;
/*     */         
/*     */         case '%': 
/* 438 */           int code0 = in.read();
/* 439 */           int code1 = in.read();
/* 440 */           buffer.append(decodeHexChar(code0, code1));
/* 441 */           break;
/*     */         
/*     */         default: 
/* 444 */           buffer.append((char)b);
/*     */         }
/*     */         
/* 447 */         if (maxLength >= 0) { totalLength++; if (totalLength > maxLength)
/* 448 */             throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
/*     */         }
/*     */       }
/* 451 */       if (key != null)
/*     */       {
/* 453 */         value = buffer.length() == 0 ? "" : buffer.toString();
/* 454 */         buffer.setLength(0);
/* 455 */         map.add(key, value);
/*     */       }
/* 457 */       else if (buffer.length() > 0)
/*     */       {
/* 459 */         map.add(buffer.toString(), "");
/*     */       }
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
/*     */   public static void decodeUtf8To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
/*     */     throws IOException
/*     */   {
/* 475 */     synchronized (map)
/*     */     {
/* 477 */       Utf8StringBuilder buffer = new Utf8StringBuilder();
/* 478 */       String key = null;
/* 479 */       String value = null;
/*     */       
/*     */ 
/*     */ 
/* 483 */       int totalLength = 0;
/* 484 */       int b; while ((b = in.read()) >= 0)
/*     */       {
/* 486 */         switch ((char)b)
/*     */         {
/*     */         case '&': 
/* 489 */           value = buffer.toReplacedString();
/* 490 */           buffer.reset();
/* 491 */           if (key != null)
/*     */           {
/* 493 */             map.add(key, value);
/*     */           }
/* 495 */           else if ((value != null) && (value.length() > 0))
/*     */           {
/* 497 */             map.add(value, "");
/*     */           }
/* 499 */           key = null;
/* 500 */           value = null;
/* 501 */           if ((maxKeys > 0) && (map.size() > maxKeys)) {
/* 502 */             throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
/*     */           }
/*     */           break;
/*     */         case '=': 
/* 506 */           if (key != null)
/*     */           {
/* 508 */             buffer.append((byte)b);
/*     */           }
/*     */           else {
/* 511 */             key = buffer.toReplacedString();
/* 512 */             buffer.reset(); }
/* 513 */           break;
/*     */         
/*     */         case '+': 
/* 516 */           buffer.append((byte)32);
/* 517 */           break;
/*     */         
/*     */         case '%': 
/* 520 */           char code0 = (char)in.read();
/* 521 */           char code1 = (char)in.read();
/* 522 */           buffer.append(decodeHexByte(code0, code1));
/* 523 */           break;
/*     */         
/*     */         default: 
/* 526 */           buffer.append((byte)b);
/*     */         }
/*     */         
/* 529 */         if (maxLength >= 0) { totalLength++; if (totalLength > maxLength)
/* 530 */             throw new IllegalStateException("Form is too large");
/*     */         }
/*     */       }
/* 533 */       if (key != null)
/*     */       {
/* 535 */         value = buffer.toReplacedString();
/* 536 */         buffer.reset();
/* 537 */         map.add(key, value);
/*     */       }
/* 539 */       else if (buffer.length() > 0)
/*     */       {
/* 541 */         map.add(buffer.toReplacedString(), "");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void decodeUtf16To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
/*     */     throws IOException
/*     */   {
/* 549 */     InputStreamReader input = new InputStreamReader(in, StandardCharsets.UTF_16);
/* 550 */     StringWriter buf = new StringWriter(8192);
/* 551 */     IO.copy(input, buf, maxLength);
/*     */     
/*     */ 
/* 554 */     decodeTo(buf.getBuffer().toString(), map, StandardCharsets.UTF_16);
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
/*     */   public static void decodeTo(InputStream in, MultiMap<String> map, String charset, int maxLength, int maxKeys)
/*     */     throws IOException
/*     */   {
/* 569 */     if (charset == null)
/*     */     {
/* 571 */       if (ENCODING.equals(StandardCharsets.UTF_8)) {
/* 572 */         decodeUtf8To(in, map, maxLength, maxKeys);
/*     */       } else {
/* 574 */         decodeTo(in, map, ENCODING, maxLength, maxKeys);
/*     */       }
/* 576 */     } else if ("utf-8".equalsIgnoreCase(charset)) {
/* 577 */       decodeUtf8To(in, map, maxLength, maxKeys);
/* 578 */     } else if ("iso-8859-1".equalsIgnoreCase(charset)) {
/* 579 */       decode88591To(in, map, maxLength, maxKeys);
/* 580 */     } else if ("utf-16".equalsIgnoreCase(charset)) {
/* 581 */       decodeUtf16To(in, map, maxLength, maxKeys);
/*     */     } else {
/* 583 */       decodeTo(in, map, Charset.forName(charset), maxLength, maxKeys);
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
/*     */   public static void decodeTo(InputStream in, MultiMap<String> map, Charset charset, int maxLength, int maxKeys)
/*     */     throws IOException
/*     */   {
/* 599 */     if (charset == null) {
/* 600 */       charset = ENCODING;
/*     */     }
/* 602 */     if (StandardCharsets.UTF_8.equals(charset))
/*     */     {
/* 604 */       decodeUtf8To(in, map, maxLength, maxKeys);
/* 605 */       return;
/*     */     }
/*     */     
/* 608 */     if (StandardCharsets.ISO_8859_1.equals(charset))
/*     */     {
/* 610 */       decode88591To(in, map, maxLength, maxKeys);
/* 611 */       return;
/*     */     }
/*     */     
/* 614 */     if (StandardCharsets.UTF_16.equals(charset))
/*     */     {
/* 616 */       decodeUtf16To(in, map, maxLength, maxKeys);
/* 617 */       return;
/*     */     }
/*     */     
/* 620 */     synchronized (map)
/*     */     {
/* 622 */       String key = null;
/* 623 */       String value = null;
/*     */       
/*     */ 
/*     */ 
/* 627 */       int totalLength = 0;
/*     */       
/* 629 */       ByteArrayOutputStream2 output = new ByteArrayOutputStream2();Throwable localThrowable3 = null;
/*     */       try {
/* 631 */         int size = 0;
/*     */         int c;
/* 633 */         while ((c = in.read()) > 0)
/*     */         {
/* 635 */           switch ((char)c)
/*     */           {
/*     */           case '&': 
/* 638 */             size = output.size();
/* 639 */             value = size == 0 ? "" : output.toString(charset);
/* 640 */             output.setCount(0);
/* 641 */             if (key != null)
/*     */             {
/* 643 */               map.add(key, value);
/*     */             }
/* 645 */             else if ((value != null) && (value.length() > 0))
/*     */             {
/* 647 */               map.add(value, "");
/*     */             }
/* 649 */             key = null;
/* 650 */             value = null;
/* 651 */             if ((maxKeys > 0) && (map.size() > maxKeys))
/* 652 */               throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
/*     */             break;
/*     */           case '=': 
/* 655 */             if (key != null)
/*     */             {
/* 657 */               output.write(c);
/*     */             }
/*     */             else {
/* 660 */               size = output.size();
/* 661 */               key = size == 0 ? "" : output.toString(charset);
/* 662 */               output.setCount(0); }
/* 663 */             break;
/*     */           case '+': 
/* 665 */             output.write(32);
/* 666 */             break;
/*     */           case '%': 
/* 668 */             int code0 = in.read();
/* 669 */             int code1 = in.read();
/* 670 */             output.write(decodeHexChar(code0, code1));
/* 671 */             break;
/*     */           default: 
/* 673 */             output.write(c);
/*     */           }
/*     */           
/*     */           
/* 677 */           totalLength++;
/* 678 */           if ((maxLength >= 0) && (totalLength > maxLength)) {
/* 679 */             throw new IllegalStateException("Form is too large");
/*     */           }
/*     */         }
/* 682 */         size = output.size();
/* 683 */         if (key != null)
/*     */         {
/* 685 */           value = size == 0 ? "" : output.toString(charset);
/* 686 */           output.setCount(0);
/* 687 */           map.add(key, value);
/*     */         }
/* 689 */         else if (size > 0) {
/* 690 */           map.add(output.toString(charset), "");
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 629 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 691 */         if (localThrowable3 != null) try { output.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { output.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String decodeString(String encoded)
/*     */   {
/* 704 */     return decodeString(encoded, 0, encoded.length(), ENCODING);
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
/*     */   public static String decodeString(String encoded, int offset, int length, Charset charset)
/*     */   {
/* 719 */     if ((charset == null) || (StandardCharsets.UTF_8.equals(charset)))
/*     */     {
/* 721 */       Utf8StringBuffer buffer = null;
/*     */       
/* 723 */       for (int i = 0; i < length; i++)
/*     */       {
/* 725 */         char c = encoded.charAt(offset + i);
/* 726 */         if ((c < 0) || (c > 'ÿ'))
/*     */         {
/* 728 */           if (buffer == null)
/*     */           {
/* 730 */             buffer = new Utf8StringBuffer(length);
/* 731 */             buffer.getStringBuffer().append(encoded, offset, offset + i + 1);
/*     */           }
/*     */           else {
/* 734 */             buffer.getStringBuffer().append(c);
/*     */           }
/* 736 */         } else if (c == '+')
/*     */         {
/* 738 */           if (buffer == null)
/*     */           {
/* 740 */             buffer = new Utf8StringBuffer(length);
/* 741 */             buffer.getStringBuffer().append(encoded, offset, offset + i);
/*     */           }
/*     */           
/* 744 */           buffer.getStringBuffer().append(' ');
/*     */         }
/* 746 */         else if (c == '%')
/*     */         {
/* 748 */           if (buffer == null)
/*     */           {
/* 750 */             buffer = new Utf8StringBuffer(length);
/* 751 */             buffer.getStringBuffer().append(encoded, offset, offset + i);
/*     */           }
/*     */           
/* 754 */           if (i + 2 < length)
/*     */           {
/* 756 */             int o = offset + i + 1;
/* 757 */             i += 2;
/* 758 */             byte b = (byte)TypeUtil.parseInt(encoded, o, 2, 16);
/* 759 */             buffer.append(b);
/*     */           }
/*     */           else
/*     */           {
/* 763 */             buffer.getStringBuffer().append(65533);
/* 764 */             i = length;
/*     */           }
/*     */         }
/* 767 */         else if (buffer != null) {
/* 768 */           buffer.getStringBuffer().append(c);
/*     */         }
/*     */       }
/* 771 */       if (buffer == null)
/*     */       {
/* 773 */         if ((offset == 0) && (encoded.length() == length))
/* 774 */           return encoded;
/* 775 */         return encoded.substring(offset, offset + length);
/*     */       }
/*     */       
/* 778 */       return buffer.toReplacedString();
/*     */     }
/*     */     
/*     */ 
/* 782 */     StringBuffer buffer = null;
/*     */     
/* 784 */     for (int i = 0; i < length; i++)
/*     */     {
/* 786 */       char c = encoded.charAt(offset + i);
/* 787 */       if ((c < 0) || (c > 'ÿ'))
/*     */       {
/* 789 */         if (buffer == null)
/*     */         {
/* 791 */           buffer = new StringBuffer(length);
/* 792 */           buffer.append(encoded, offset, offset + i + 1);
/*     */         }
/*     */         else {
/* 795 */           buffer.append(c);
/*     */         }
/* 797 */       } else if (c == '+')
/*     */       {
/* 799 */         if (buffer == null)
/*     */         {
/* 801 */           buffer = new StringBuffer(length);
/* 802 */           buffer.append(encoded, offset, offset + i);
/*     */         }
/*     */         
/* 805 */         buffer.append(' ');
/*     */       }
/* 807 */       else if (c == '%')
/*     */       {
/* 809 */         if (buffer == null)
/*     */         {
/* 811 */           buffer = new StringBuffer(length);
/* 812 */           buffer.append(encoded, offset, offset + i);
/*     */         }
/*     */         
/* 815 */         byte[] ba = new byte[length];
/* 816 */         int n = 0;
/* 817 */         while ((c >= 0) && (c <= 'ÿ'))
/*     */         {
/* 819 */           if (c == '%')
/*     */           {
/* 821 */             if (i + 2 < length)
/*     */             {
/* 823 */               int o = offset + i + 1;
/* 824 */               i += 3;
/* 825 */               ba[n] = ((byte)TypeUtil.parseInt(encoded, o, 2, 16));
/* 826 */               n++;
/*     */             }
/*     */             else
/*     */             {
/* 830 */               ba[(n++)] = 63;
/* 831 */               i = length;
/*     */             }
/*     */           }
/* 834 */           else if (c == '+')
/*     */           {
/* 836 */             ba[(n++)] = 32;
/* 837 */             i++;
/*     */           }
/*     */           else
/*     */           {
/* 841 */             ba[(n++)] = ((byte)c);
/* 842 */             i++;
/*     */           }
/*     */           
/* 845 */           if (i >= length)
/*     */             break;
/* 847 */           c = encoded.charAt(offset + i);
/*     */         }
/*     */         
/* 850 */         i--;
/* 851 */         buffer.append(new String(ba, 0, n, charset));
/*     */ 
/*     */       }
/* 854 */       else if (buffer != null) {
/* 855 */         buffer.append(c);
/*     */       }
/*     */     }
/* 858 */     if (buffer == null)
/*     */     {
/* 860 */       if ((offset == 0) && (encoded.length() == length))
/* 861 */         return encoded;
/* 862 */       return encoded.substring(offset, offset + length);
/*     */     }
/*     */     
/* 865 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private static char decodeHexChar(int hi, int lo)
/*     */   {
/*     */     try
/*     */     {
/* 873 */       return (char)((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo));
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/* 877 */       throw new IllegalArgumentException("Not valid encoding '%" + (char)hi + (char)lo + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte decodeHexByte(char hi, char lo)
/*     */   {
/*     */     try
/*     */     {
/* 885 */       return (byte)((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo));
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/* 889 */       throw new IllegalArgumentException("Not valid encoding '%" + hi + lo + "'");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encodeString(String string)
/*     */   {
/* 900 */     return encodeString(string, ENCODING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encodeString(String string, Charset charset)
/*     */   {
/* 911 */     if (charset == null)
/* 912 */       charset = ENCODING;
/* 913 */     byte[] bytes = null;
/* 914 */     bytes = string.getBytes(charset);
/*     */     
/* 916 */     int len = bytes.length;
/* 917 */     byte[] encoded = new byte[bytes.length * 3];
/* 918 */     int n = 0;
/* 919 */     boolean noEncode = true;
/*     */     
/* 921 */     for (int i = 0; i < len; i++)
/*     */     {
/* 923 */       byte b = bytes[i];
/*     */       
/* 925 */       if (b == 32)
/*     */       {
/* 927 */         noEncode = false;
/* 928 */         encoded[(n++)] = 43;
/*     */       }
/* 930 */       else if (((b >= 97) && (b <= 122)) || ((b >= 65) && (b <= 90)) || ((b >= 48) && (b <= 57)))
/*     */       {
/*     */ 
/*     */ 
/* 934 */         encoded[(n++)] = b;
/*     */       }
/*     */       else
/*     */       {
/* 938 */         noEncode = false;
/* 939 */         encoded[(n++)] = 37;
/* 940 */         byte nibble = (byte)((b & 0xF0) >> 4);
/* 941 */         if (nibble >= 10) {
/* 942 */           encoded[(n++)] = ((byte)(65 + nibble - 10));
/*     */         } else
/* 944 */           encoded[(n++)] = ((byte)(48 + nibble));
/* 945 */         nibble = (byte)(b & 0xF);
/* 946 */         if (nibble >= 10) {
/* 947 */           encoded[(n++)] = ((byte)(65 + nibble - 10));
/*     */         } else {
/* 949 */           encoded[(n++)] = ((byte)(48 + nibble));
/*     */         }
/*     */       }
/*     */     }
/* 953 */     if (noEncode) {
/* 954 */       return string;
/*     */     }
/* 956 */     return new String(encoded, 0, n, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 966 */     return new UrlEncoded(this);
/*     */   }
/*     */   
/*     */   public UrlEncoded() {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\UrlEncoded.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */