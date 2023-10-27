/*     */ package org.eclipse.jetty.websocket.api.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuoteUtil
/*     */ {
/*     */   public static final String ABNF_REQUIRED_QUOTING = "\"'\\\n\r\t\f\b%+ ;=";
/*     */   private static final char UNICODE_TAG = '￿';
/*     */   
/*     */   private static class DeQuotingStringIterator
/*     */     implements Iterator<String>
/*     */   {
/*     */     private final String input;
/*     */     private final String delims;
/*     */     private StringBuilder token;
/*     */     
/*     */     private static enum State
/*     */     {
/*  44 */       START, 
/*  45 */       TOKEN, 
/*  46 */       QUOTE_SINGLE, 
/*  47 */       QUOTE_DOUBLE;
/*     */       
/*     */ 
/*     */       private State() {}
/*     */     }
/*     */     
/*  53 */     private boolean hasToken = false;
/*  54 */     private int i = 0;
/*     */     
/*     */     public DeQuotingStringIterator(String input, String delims)
/*     */     {
/*  58 */       this.input = input;
/*  59 */       this.delims = delims;
/*  60 */       int len = input.length();
/*  61 */       this.token = new StringBuilder(len > 1024 ? 512 : len / 2);
/*     */     }
/*     */     
/*     */     private void appendToken(char c)
/*     */     {
/*  66 */       if (this.hasToken)
/*     */       {
/*  68 */         this.token.append(c);
/*     */       }
/*     */       else
/*     */       {
/*  72 */         if (Character.isWhitespace(c))
/*     */         {
/*  74 */           return;
/*     */         }
/*     */         
/*     */ 
/*  78 */         this.token.append(c);
/*  79 */         this.hasToken = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/*  88 */       if (this.hasToken)
/*     */       {
/*  90 */         return true;
/*     */       }
/*     */       
/*  93 */       State state = State.START;
/*  94 */       boolean escape = false;
/*  95 */       int inputLen = this.input.length();
/*     */       
/*  97 */       while (this.i < inputLen)
/*     */       {
/*  99 */         char c = this.input.charAt(this.i++);
/*     */         
/* 101 */         switch (QuoteUtil.1.$SwitchMap$org$eclipse$jetty$websocket$api$util$QuoteUtil$DeQuotingStringIterator$State[state.ordinal()])
/*     */         {
/*     */ 
/*     */         case 1: 
/* 105 */           if (c == '\'')
/*     */           {
/* 107 */             state = State.QUOTE_SINGLE;
/* 108 */             appendToken(c);
/*     */           }
/* 110 */           else if (c == '"')
/*     */           {
/* 112 */             state = State.QUOTE_DOUBLE;
/* 113 */             appendToken(c);
/*     */           }
/*     */           else
/*     */           {
/* 117 */             appendToken(c);
/* 118 */             state = State.TOKEN;
/*     */           }
/* 120 */           break;
/*     */         
/*     */ 
/*     */         case 2: 
/* 124 */           if (this.delims.indexOf(c) >= 0)
/*     */           {
/*     */ 
/* 127 */             return this.hasToken;
/*     */           }
/* 129 */           if (c == '\'')
/*     */           {
/* 131 */             state = State.QUOTE_SINGLE;
/*     */           }
/* 133 */           else if (c == '"')
/*     */           {
/* 135 */             state = State.QUOTE_DOUBLE;
/*     */           }
/* 137 */           appendToken(c);
/* 138 */           break;
/*     */         
/*     */ 
/*     */         case 3: 
/* 142 */           if (escape)
/*     */           {
/* 144 */             escape = false;
/* 145 */             appendToken(c);
/*     */           }
/* 147 */           else if (c == '\'')
/*     */           {
/* 149 */             appendToken(c);
/* 150 */             state = State.TOKEN;
/*     */           }
/* 152 */           else if (c == '\\')
/*     */           {
/* 154 */             escape = true;
/*     */           }
/*     */           else
/*     */           {
/* 158 */             appendToken(c);
/*     */           }
/* 160 */           break;
/*     */         
/*     */ 
/*     */         case 4: 
/* 164 */           if (escape)
/*     */           {
/* 166 */             escape = false;
/* 167 */             appendToken(c);
/*     */           }
/* 169 */           else if (c == '"')
/*     */           {
/* 171 */             appendToken(c);
/* 172 */             state = State.TOKEN;
/*     */           }
/* 174 */           else if (c == '\\')
/*     */           {
/* 176 */             escape = true;
/*     */           }
/*     */           else
/*     */           {
/* 180 */             appendToken(c);
/*     */           }
/*     */           
/*     */           break;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 188 */       return this.hasToken;
/*     */     }
/*     */     
/*     */ 
/*     */     public String next()
/*     */     {
/* 194 */       if (!hasNext())
/*     */       {
/* 196 */         throw new NoSuchElementException();
/*     */       }
/* 198 */       String ret = this.token.toString();
/* 199 */       this.token.setLength(0);
/* 200 */       this.hasToken = false;
/* 201 */       return QuoteUtil.dequote(ret.trim());
/*     */     }
/*     */     
/*     */ 
/*     */     public void remove()
/*     */     {
/* 207 */       throw new UnsupportedOperationException("Remove not supported with this iterator");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */   private static final char[] escapes = new char[32];
/*     */   
/*     */   static
/*     */   {
/* 221 */     Arrays.fill(escapes, 65535);
/*     */     
/* 223 */     escapes[8] = 'b';
/* 224 */     escapes[9] = 't';
/* 225 */     escapes[10] = 'n';
/* 226 */     escapes[12] = 'f';
/* 227 */     escapes[13] = 'r';
/*     */   }
/*     */   
/*     */   private static int dehex(byte b)
/*     */   {
/* 232 */     if ((b >= 48) && (b <= 57))
/*     */     {
/* 234 */       return (byte)(b - 48);
/*     */     }
/* 236 */     if ((b >= 97) && (b <= 102))
/*     */     {
/* 238 */       return (byte)(b - 97 + 10);
/*     */     }
/* 240 */     if ((b >= 65) && (b <= 70))
/*     */     {
/* 242 */       return (byte)(b - 65 + 10);
/*     */     }
/* 244 */     throw new IllegalArgumentException("!hex:" + Integer.toHexString(0xFF & b));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String dequote(String str)
/*     */   {
/* 256 */     char start = str.charAt(0);
/* 257 */     if ((start == '\'') || (start == '"'))
/*     */     {
/*     */ 
/* 260 */       char end = str.charAt(str.length() - 1);
/* 261 */       if (start == end)
/*     */       {
/*     */ 
/* 264 */         return str.substring(1, str.length() - 1);
/*     */       }
/*     */     }
/* 267 */     return str;
/*     */   }
/*     */   
/*     */   public static void escape(StringBuilder buf, String str)
/*     */   {
/* 272 */     for (char c : str.toCharArray())
/*     */     {
/* 274 */       if (c >= ' ')
/*     */       {
/*     */ 
/* 277 */         if ((c == '"') || (c == '\\'))
/*     */         {
/* 279 */           buf.append('\\');
/*     */         }
/* 281 */         buf.append(c);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 286 */         char escaped = escapes[c];
/*     */         
/*     */ 
/* 289 */         if (escaped == 65535)
/*     */         {
/* 291 */           buf.append("\\u00");
/* 292 */           if (c < '\020')
/*     */           {
/* 294 */             buf.append('0');
/*     */           }
/* 296 */           buf.append(Integer.toString(c, 16));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 301 */           buf.append('\\').append(escaped);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void quote(StringBuilder buf, String str)
/*     */   {
/* 317 */     buf.append('"');
/* 318 */     escape(buf, str);
/* 319 */     buf.append('"');
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
/*     */   public static void quoteIfNeeded(StringBuilder buf, String str, String delim)
/*     */   {
/* 336 */     if (str == null)
/*     */     {
/* 338 */       return;
/*     */     }
/*     */     
/* 341 */     int len = str.length();
/* 342 */     if (len == 0)
/*     */     {
/* 344 */       return;
/*     */     }
/*     */     
/* 347 */     for (int i = 0; i < len; i++)
/*     */     {
/* 349 */       int ch = str.codePointAt(i);
/* 350 */       if (delim.indexOf(ch) >= 0)
/*     */       {
/*     */ 
/* 353 */         quote(buf, str);
/* 354 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 359 */     buf.append(str);
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
/*     */   public static Iterator<String> splitAt(String str, String delims)
/*     */   {
/* 374 */     return new DeQuotingStringIterator(str.trim(), delims);
/*     */   }
/*     */   
/*     */   public static String unescape(String str)
/*     */   {
/* 379 */     if (str == null)
/*     */     {
/*     */ 
/* 382 */       return null;
/*     */     }
/*     */     
/* 385 */     int len = str.length();
/* 386 */     if (len <= 1)
/*     */     {
/*     */ 
/* 389 */       return str;
/*     */     }
/*     */     
/* 392 */     StringBuilder ret = new StringBuilder(len - 2);
/* 393 */     boolean escaped = false;
/*     */     
/* 395 */     for (int i = 0; i < len; i++)
/*     */     {
/* 397 */       char c = str.charAt(i);
/* 398 */       if (escaped)
/*     */       {
/* 400 */         escaped = false;
/* 401 */         switch (c)
/*     */         {
/*     */         case 'n': 
/* 404 */           ret.append('\n');
/* 405 */           break;
/*     */         case 'r': 
/* 407 */           ret.append('\r');
/* 408 */           break;
/*     */         case 't': 
/* 410 */           ret.append('\t');
/* 411 */           break;
/*     */         case 'f': 
/* 413 */           ret.append('\f');
/* 414 */           break;
/*     */         case 'b': 
/* 416 */           ret.append('\b');
/* 417 */           break;
/*     */         case '\\': 
/* 419 */           ret.append('\\');
/* 420 */           break;
/*     */         case '/': 
/* 422 */           ret.append('/');
/* 423 */           break;
/*     */         case '"': 
/* 425 */           ret.append('"');
/* 426 */           break;
/*     */         case 'u': 
/* 428 */           ret.append((char)((dehex((byte)str.charAt(i++)) << 24) + (dehex((byte)str.charAt(i++)) << 16) + (dehex((byte)str.charAt(i++)) << 8) + dehex(
/* 429 */             (byte)str.charAt(i++))));
/* 430 */           break;
/*     */         default: 
/* 432 */           ret.append(c);break;
/*     */         }
/*     */       }
/* 435 */       else if (c == '\\')
/*     */       {
/* 437 */         escaped = true;
/*     */       }
/*     */       else
/*     */       {
/* 441 */         ret.append(c);
/*     */       }
/*     */     }
/* 444 */     return ret.toString();
/*     */   }
/*     */   
/*     */   public static String join(Object[] objs, String delim)
/*     */   {
/* 449 */     if (objs == null)
/*     */     {
/* 451 */       return "";
/*     */     }
/* 453 */     StringBuilder ret = new StringBuilder();
/* 454 */     int len = objs.length;
/* 455 */     for (int i = 0; i < len; i++)
/*     */     {
/* 457 */       if (i > 0)
/*     */       {
/* 459 */         ret.append(delim);
/*     */       }
/* 461 */       if ((objs[i] instanceof String))
/*     */       {
/* 463 */         ret.append('"').append(objs[i]).append('"');
/*     */       }
/*     */       else
/*     */       {
/* 467 */         ret.append(objs[i]);
/*     */       }
/*     */     }
/* 470 */     return ret.toString();
/*     */   }
/*     */   
/*     */   public static String join(Collection<?> objs, String delim)
/*     */   {
/* 475 */     if (objs == null)
/*     */     {
/* 477 */       return "";
/*     */     }
/* 479 */     StringBuilder ret = new StringBuilder();
/* 480 */     boolean needDelim = false;
/* 481 */     for (Object obj : objs)
/*     */     {
/* 483 */       if (needDelim)
/*     */       {
/* 485 */         ret.append(delim);
/*     */       }
/* 487 */       if ((obj instanceof String))
/*     */       {
/* 489 */         ret.append('"').append(obj).append('"');
/*     */       }
/*     */       else
/*     */       {
/* 493 */         ret.append(obj);
/*     */       }
/* 495 */       needDelim = true;
/*     */     }
/* 497 */     return ret.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\util\QuoteUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */