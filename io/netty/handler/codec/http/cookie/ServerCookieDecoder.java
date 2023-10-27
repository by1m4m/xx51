/*     */ package io.netty.handler.codec.http.cookie;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ServerCookieDecoder
/*     */   extends CookieDecoder
/*     */ {
/*     */   private static final String RFC2965_VERSION = "$Version";
/*     */   private static final String RFC2965_PATH = "$Path";
/*     */   private static final String RFC2965_DOMAIN = "$Domain";
/*     */   private static final String RFC2965_PORT = "$Port";
/*  48 */   public static final ServerCookieDecoder STRICT = new ServerCookieDecoder(true);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   public static final ServerCookieDecoder LAX = new ServerCookieDecoder(false);
/*     */   
/*     */   private ServerCookieDecoder(boolean strict) {
/*  56 */     super(strict);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<Cookie> decode(String header)
/*     */   {
/*  65 */     int headerLen = ((String)ObjectUtil.checkNotNull(header, "header")).length();
/*     */     
/*  67 */     if (headerLen == 0) {
/*  68 */       return Collections.emptySet();
/*     */     }
/*     */     
/*  71 */     Set<Cookie> cookies = new TreeSet();
/*     */     
/*  73 */     int i = 0;
/*     */     
/*  75 */     boolean rfc2965Style = false;
/*  76 */     if (header.regionMatches(true, 0, "$Version", 0, "$Version".length()))
/*     */     {
/*  78 */       i = header.indexOf(';') + 1;
/*  79 */       rfc2965Style = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */     while (i != headerLen)
/*     */     {
/*     */ 
/*  89 */       char c = header.charAt(i);
/*  90 */       if ((c == '\t') || (c == '\n') || (c == '\013') || (c == '\f') || (c == '\r') || (c == ' ') || (c == ',') || (c == ';'))
/*     */       {
/*  92 */         i++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  98 */         int nameBegin = i;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         for (;;)
/*     */         {
/* 105 */           char curChar = header.charAt(i);
/* 106 */           if (curChar == ';')
/*     */           {
/* 108 */             int nameEnd = i;
/* 109 */             int valueEnd; int valueBegin = valueEnd = -1;
/* 110 */             break;
/*     */           }
/* 112 */           if (curChar == '=')
/*     */           {
/* 114 */             int nameEnd = i;
/* 115 */             i++;
/* 116 */             if (i == headerLen) {
/*     */               int valueEnd;
/* 118 */               int valueBegin = valueEnd = 0;
/* 119 */               break;
/*     */             }
/*     */             
/* 122 */             int valueBegin = i;
/*     */             
/* 124 */             int semiPos = header.indexOf(';', i);
/* 125 */             int valueEnd = i = semiPos > 0 ? semiPos : headerLen;
/* 126 */             break;
/*     */           }
/* 128 */           i++;
/*     */           
/*     */ 
/* 131 */           if (i == headerLen)
/*     */           {
/* 133 */             int nameEnd = headerLen;
/* 134 */             int valueEnd; int valueBegin = valueEnd = -1;
/* 135 */             break; } }
/*     */         int valueEnd;
/*     */         int valueBegin;
/*     */         int nameEnd;
/* 139 */         if ((!rfc2965Style) || ((!header.regionMatches(nameBegin, "$Path", 0, "$Path".length())) && 
/* 140 */           (!header.regionMatches(nameBegin, "$Domain", 0, "$Domain".length())) && 
/* 141 */           (!header.regionMatches(nameBegin, "$Port", 0, "$Port".length()))))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */           DefaultCookie cookie = initCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
/* 148 */           if (cookie != null)
/* 149 */             cookies.add(cookie);
/*     */         }
/*     */       }
/*     */     }
/* 153 */     return cookies;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\cookie\ServerCookieDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */