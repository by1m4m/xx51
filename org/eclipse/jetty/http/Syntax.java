/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Syntax
/*     */ {
/*     */   public static void requireValidRFC2616Token(String value, String msg)
/*     */   {
/*  51 */     Objects.requireNonNull(msg, "msg cannot be null");
/*     */     
/*  53 */     if (value == null)
/*     */     {
/*  55 */       return;
/*     */     }
/*     */     
/*  58 */     int valueLen = value.length();
/*  59 */     if (valueLen == 0)
/*     */     {
/*  61 */       return;
/*     */     }
/*     */     
/*  64 */     for (int i = 0; i < valueLen; i++)
/*     */     {
/*  66 */       char c = value.charAt(i);
/*     */       
/*     */ 
/*     */ 
/*  70 */       if ((c <= '\037') || (c == ''))
/*  71 */         throw new IllegalArgumentException(msg + ": RFC2616 tokens may not contain control characters");
/*  72 */       if ((c == '(') || (c == ')') || (c == '<') || (c == '>') || (c == '@') || (c == ',') || (c == ';') || (c == ':') || (c == '\\') || (c == '"') || (c == '/') || (c == '[') || (c == ']') || (c == '?') || (c == '=') || (c == '{') || (c == '}') || (c == ' '))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  77 */         throw new IllegalArgumentException(msg + ": RFC2616 tokens may not contain separator character: [" + c + "]");
/*     */       }
/*  79 */       if (c >= '') {
/*  80 */         throw new IllegalArgumentException(msg + ": RFC2616 tokens characters restricted to US-ASCII: 0x" + Integer.toHexString(c));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void requireValidRFC6265CookieValue(String value)
/*     */   {
/*  99 */     if (value == null)
/*     */     {
/* 101 */       return;
/*     */     }
/*     */     
/* 104 */     int valueLen = value.length();
/* 105 */     if (valueLen == 0)
/*     */     {
/* 107 */       return;
/*     */     }
/*     */     
/* 110 */     int i = 0;
/* 111 */     if (value.charAt(0) == '"')
/*     */     {
/*     */ 
/* 114 */       if ((valueLen <= 1) || (value.charAt(valueLen - 1) != '"'))
/*     */       {
/* 116 */         throw new IllegalArgumentException("RFC6265 Cookie values must have balanced DQUOTES (if used)");
/*     */       }
/*     */       
/*     */ 
/* 120 */       i++;
/* 121 */       valueLen--;
/*     */     }
/* 123 */     for (; i < valueLen; i++)
/*     */     {
/* 125 */       char c = value.charAt(i);
/*     */       
/*     */ 
/*     */ 
/* 129 */       if ((c <= '\037') || (c == ''))
/* 130 */         throw new IllegalArgumentException("RFC6265 Cookie values may not contain control characters");
/* 131 */       if ((c == ' ') || (c == '"') || (c == ';') || (c == '\\'))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 136 */         throw new IllegalArgumentException("RFC6265 Cookie values may not contain character: [" + c + "]");
/*     */       }
/* 138 */       if (c >= '') {
/* 139 */         throw new IllegalArgumentException("RFC6265 Cookie values characters restricted to US-ASCII: 0x" + Integer.toHexString(c));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\Syntax.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */