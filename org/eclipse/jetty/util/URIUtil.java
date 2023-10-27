/*      */ package org.eclipse.jetty.util;
/*      */ 
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class URIUtil
/*      */   implements Cloneable
/*      */ {
/*   44 */   private static final Logger LOG = Log.getLogger(URIUtil.class);
/*      */   
/*      */   public static final String SLASH = "/";
/*      */   
/*      */   public static final String HTTP = "http";
/*      */   public static final String HTTPS = "https";
/*   50 */   public static final Charset __CHARSET = StandardCharsets.UTF_8;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String encodePath(String path)
/*      */   {
/*   64 */     if ((path == null) || (path.length() == 0)) {
/*   65 */       return path;
/*      */     }
/*   67 */     StringBuilder buf = encodePath(null, path, 0);
/*   68 */     return buf == null ? path : buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static StringBuilder encodePath(StringBuilder buf, String path)
/*      */   {
/*   79 */     return encodePath(buf, path, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static StringBuilder encodePath(StringBuilder buf, String path, int offset)
/*      */   {
/*   90 */     byte[] bytes = null;
/*   91 */     if (buf == null)
/*      */     {
/*   93 */       for (int i = offset; i < path.length(); i++)
/*      */       {
/*   95 */         char c = path.charAt(i);
/*   96 */         switch (c)
/*      */         {
/*      */         case ' ': 
/*      */         case '"': 
/*      */         case '#': 
/*      */         case '%': 
/*      */         case '\'': 
/*      */         case ';': 
/*      */         case '<': 
/*      */         case '>': 
/*      */         case '?': 
/*      */         case '[': 
/*      */         case '\\': 
/*      */         case ']': 
/*      */         case '^': 
/*      */         case '`': 
/*      */         case '{': 
/*      */         case '|': 
/*      */         case '}': 
/*  115 */           buf = new StringBuilder(path.length() * 2);
/*  116 */           break;
/*      */         default: 
/*  118 */           if (c > '')
/*      */           {
/*  120 */             bytes = path.getBytes(__CHARSET);
/*  121 */             buf = new StringBuilder(path.length() * 2);
/*  122 */             break;
/*      */           }
/*      */           break; }
/*      */       }
/*  126 */       if (buf == null) {
/*  127 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  132 */     for (int i = offset; i < path.length(); i++)
/*      */     {
/*  134 */       char c = path.charAt(i);
/*  135 */       switch (c)
/*      */       {
/*      */       case '%': 
/*  138 */         buf.append("%25");
/*  139 */         break;
/*      */       case '?': 
/*  141 */         buf.append("%3F");
/*  142 */         break;
/*      */       case ';': 
/*  144 */         buf.append("%3B");
/*  145 */         break;
/*      */       case '#': 
/*  147 */         buf.append("%23");
/*  148 */         break;
/*      */       case '"': 
/*  150 */         buf.append("%22");
/*  151 */         break;
/*      */       case '\'': 
/*  153 */         buf.append("%27");
/*  154 */         break;
/*      */       case '<': 
/*  156 */         buf.append("%3C");
/*  157 */         break;
/*      */       case '>': 
/*  159 */         buf.append("%3E");
/*  160 */         break;
/*      */       case ' ': 
/*  162 */         buf.append("%20");
/*  163 */         break;
/*      */       case '[': 
/*  165 */         buf.append("%5B");
/*  166 */         break;
/*      */       case '\\': 
/*  168 */         buf.append("%5C");
/*  169 */         break;
/*      */       case ']': 
/*  171 */         buf.append("%5D");
/*  172 */         break;
/*      */       case '^': 
/*  174 */         buf.append("%5E");
/*  175 */         break;
/*      */       case '`': 
/*  177 */         buf.append("%60");
/*  178 */         break;
/*      */       case '{': 
/*  180 */         buf.append("%7B");
/*  181 */         break;
/*      */       case '|': 
/*  183 */         buf.append("%7C");
/*  184 */         break;
/*      */       case '}': 
/*  186 */         buf.append("%7D");
/*  187 */         break;
/*      */       
/*      */       default: 
/*  190 */         if (c > '')
/*      */         {
/*  192 */           bytes = path.getBytes(__CHARSET);
/*      */           break label605;
/*      */         }
/*  195 */         buf.append(c);
/*      */       }
/*      */     }
/*      */     label605:
/*  199 */     if (bytes != null)
/*  201 */       for (; 
/*  201 */           i < bytes.length; i++)
/*      */       {
/*  203 */         byte c = bytes[i];
/*  204 */         switch (c)
/*      */         {
/*      */         case 37: 
/*  207 */           buf.append("%25");
/*  208 */           break;
/*      */         case 63: 
/*  210 */           buf.append("%3F");
/*  211 */           break;
/*      */         case 59: 
/*  213 */           buf.append("%3B");
/*  214 */           break;
/*      */         case 35: 
/*  216 */           buf.append("%23");
/*  217 */           break;
/*      */         case 34: 
/*  219 */           buf.append("%22");
/*  220 */           break;
/*      */         case 39: 
/*  222 */           buf.append("%27");
/*  223 */           break;
/*      */         case 60: 
/*  225 */           buf.append("%3C");
/*  226 */           break;
/*      */         case 62: 
/*  228 */           buf.append("%3E");
/*  229 */           break;
/*      */         case 32: 
/*  231 */           buf.append("%20");
/*  232 */           break;
/*      */         case 91: 
/*  234 */           buf.append("%5B");
/*  235 */           break;
/*      */         case 92: 
/*  237 */           buf.append("%5C");
/*  238 */           break;
/*      */         case 93: 
/*  240 */           buf.append("%5D");
/*  241 */           break;
/*      */         case 94: 
/*  243 */           buf.append("%5E");
/*  244 */           break;
/*      */         case 96: 
/*  246 */           buf.append("%60");
/*  247 */           break;
/*      */         case 123: 
/*  249 */           buf.append("%7B");
/*  250 */           break;
/*      */         case 124: 
/*  252 */           buf.append("%7C");
/*  253 */           break;
/*      */         case 125: 
/*  255 */           buf.append("%7D");
/*  256 */           break;
/*      */         default: 
/*  258 */           if (c < 0)
/*      */           {
/*  260 */             buf.append('%');
/*  261 */             TypeUtil.toHex(c, buf);
/*      */           }
/*      */           else {
/*  264 */             buf.append((char)c);
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*  269 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static StringBuilder encodeString(StringBuilder buf, String path, String encode)
/*      */   {
/*  283 */     if (buf == null)
/*      */     {
/*  285 */       for (int i = 0; i < path.length(); i++)
/*      */       {
/*  287 */         char c = path.charAt(i);
/*  288 */         if ((c == '%') || (encode.indexOf(c) >= 0))
/*      */         {
/*  290 */           buf = new StringBuilder(path.length() << 1);
/*  291 */           break;
/*      */         }
/*      */       }
/*  294 */       if (buf == null) {
/*  295 */         return null;
/*      */       }
/*      */     }
/*  298 */     for (int i = 0; i < path.length(); i++)
/*      */     {
/*  300 */       char c = path.charAt(i);
/*  301 */       if ((c == '%') || (encode.indexOf(c) >= 0))
/*      */       {
/*  303 */         buf.append('%');
/*  304 */         StringUtil.append(buf, (byte)(0xFF & c), 16);
/*      */       }
/*      */       else {
/*  307 */         buf.append(c);
/*      */       }
/*      */     }
/*  310 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String decodePath(String path)
/*      */   {
/*  318 */     return decodePath(path, 0, path.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String decodePath(String path, int offset, int length)
/*      */   {
/*      */     try
/*      */     {
/*  328 */       Utf8StringBuilder builder = null;
/*  329 */       int end = offset + length;
/*  330 */       for (int i = offset; i < end; i++)
/*      */       {
/*  332 */         char c = path.charAt(i);
/*  333 */         switch (c)
/*      */         {
/*      */         case '%': 
/*  336 */           if (builder == null)
/*      */           {
/*  338 */             builder = new Utf8StringBuilder(path.length());
/*  339 */             builder.append(path, offset, i - offset);
/*      */           }
/*  341 */           if (i + 2 < end)
/*      */           {
/*  343 */             char u = path.charAt(i + 1);
/*  344 */             if (u == 'u')
/*      */             {
/*      */ 
/*  347 */               builder.append((char)(0xFFFF & TypeUtil.parseInt(path, i + 2, 4, 16)));
/*  348 */               i += 5;
/*      */             }
/*      */             else
/*      */             {
/*  352 */               builder.append((byte)(0xFF & TypeUtil.convertHexDigit(u) * 16 + TypeUtil.convertHexDigit(path.charAt(i + 2))));
/*  353 */               i += 2;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  358 */             throw new IllegalArgumentException("Bad URI % encoding");
/*      */           }
/*      */           
/*      */ 
/*      */           break;
/*      */         case ';': 
/*  364 */           if (builder == null)
/*      */           {
/*  366 */             builder = new Utf8StringBuilder(path.length());
/*  367 */             builder.append(path, offset, i - offset);
/*      */           }
/*      */           do {
/*  370 */             i++; if (i >= end)
/*      */               break;
/*  372 */           } while (path.charAt(i) != '/');
/*      */           
/*  374 */           builder.append('/');
/*  375 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         default: 
/*  382 */           if (builder != null) {
/*  383 */             builder.append(c);
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*  388 */       if (builder != null)
/*  389 */         return builder.toString();
/*  390 */       if ((offset == 0) && (length == path.length()))
/*  391 */         return path;
/*  392 */       return path.substring(offset, end);
/*      */     }
/*      */     catch (Utf8Appendable.NotUtf8Exception e)
/*      */     {
/*  396 */       LOG.warn(path.substring(offset, offset + length) + " " + e, new Object[0]);
/*  397 */       LOG.debug(e); }
/*  398 */     return decodeISO88591Path(path, offset, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String decodeISO88591Path(String path, int offset, int length)
/*      */   {
/*  408 */     StringBuilder builder = null;
/*  409 */     int end = offset + length;
/*  410 */     for (int i = offset; i < end; i++)
/*      */     {
/*  412 */       char c = path.charAt(i);
/*  413 */       switch (c)
/*      */       {
/*      */       case '%': 
/*  416 */         if (builder == null)
/*      */         {
/*  418 */           builder = new StringBuilder(path.length());
/*  419 */           builder.append(path, offset, i - offset);
/*      */         }
/*  421 */         if (i + 2 < end)
/*      */         {
/*  423 */           char u = path.charAt(i + 1);
/*  424 */           if (u == 'u')
/*      */           {
/*      */ 
/*  427 */             builder.append((char)(0xFFFF & TypeUtil.parseInt(path, i + 2, 4, 16)));
/*  428 */             i += 5;
/*      */           }
/*      */           else
/*      */           {
/*  432 */             builder.append((byte)(0xFF & TypeUtil.convertHexDigit(u) * 16 + TypeUtil.convertHexDigit(path.charAt(i + 2))));
/*  433 */             i += 2;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  438 */           throw new IllegalArgumentException();
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       case ';': 
/*  444 */         if (builder == null)
/*      */         {
/*  446 */           builder = new StringBuilder(path.length());
/*  447 */           builder.append(path, offset, i - offset);
/*      */         }
/*  449 */         do { i++; if (i >= end)
/*      */             break;
/*  451 */         } while (path.charAt(i) != '/');
/*      */         
/*  453 */         builder.append('/');
/*  454 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       default: 
/*  461 */         if (builder != null) {
/*  462 */           builder.append(c);
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*  467 */     if (builder != null)
/*  468 */       return builder.toString();
/*  469 */     if ((offset == 0) && (length == path.length()))
/*  470 */       return path;
/*  471 */     return path.substring(offset, end);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String addEncodedPaths(String p1, String p2)
/*      */   {
/*  485 */     if ((p1 == null) || (p1.length() == 0))
/*      */     {
/*  487 */       if ((p1 != null) && (p2 == null))
/*  488 */         return p1;
/*  489 */       return p2;
/*      */     }
/*  491 */     if ((p2 == null) || (p2.length() == 0)) {
/*  492 */       return p1;
/*      */     }
/*  494 */     int split = p1.indexOf(';');
/*  495 */     if (split < 0)
/*  496 */       split = p1.indexOf('?');
/*  497 */     if (split == 0)
/*  498 */       return p2 + p1;
/*  499 */     if (split < 0) {
/*  500 */       split = p1.length();
/*      */     }
/*  502 */     StringBuilder buf = new StringBuilder(p1.length() + p2.length() + 2);
/*  503 */     buf.append(p1);
/*      */     
/*  505 */     if (buf.charAt(split - 1) == '/')
/*      */     {
/*  507 */       if (p2.startsWith("/"))
/*      */       {
/*  509 */         buf.deleteCharAt(split - 1);
/*  510 */         buf.insert(split - 1, p2);
/*      */       }
/*      */       else {
/*  513 */         buf.insert(split, p2);
/*      */       }
/*      */       
/*      */     }
/*  517 */     else if (p2.startsWith("/")) {
/*  518 */       buf.insert(split, p2);
/*      */     }
/*      */     else {
/*  521 */       buf.insert(split, '/');
/*  522 */       buf.insert(split + 1, p2);
/*      */     }
/*      */     
/*      */ 
/*  526 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String addPaths(String p1, String p2)
/*      */   {
/*  539 */     if ((p1 == null) || (p1.length() == 0))
/*      */     {
/*  541 */       if ((p1 != null) && (p2 == null))
/*  542 */         return p1;
/*  543 */       return p2;
/*      */     }
/*  545 */     if ((p2 == null) || (p2.length() == 0)) {
/*  546 */       return p1;
/*      */     }
/*  548 */     boolean p1EndsWithSlash = p1.endsWith("/");
/*  549 */     boolean p2StartsWithSlash = p2.startsWith("/");
/*      */     
/*  551 */     if ((p1EndsWithSlash) && (p2StartsWithSlash))
/*      */     {
/*  553 */       if (p2.length() == 1)
/*  554 */         return p1;
/*  555 */       if (p1.length() == 1) {
/*  556 */         return p2;
/*      */       }
/*      */     }
/*  559 */     StringBuilder buf = new StringBuilder(p1.length() + p2.length() + 2);
/*  560 */     buf.append(p1);
/*      */     
/*  562 */     if (p1.endsWith("/"))
/*      */     {
/*  564 */       if (p2.startsWith("/")) {
/*  565 */         buf.setLength(buf.length() - 1);
/*      */       }
/*      */       
/*      */     }
/*  569 */     else if (!p2.startsWith("/")) {
/*  570 */       buf.append("/");
/*      */     }
/*  572 */     buf.append(p2);
/*      */     
/*  574 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String parentPath(String p)
/*      */   {
/*  585 */     if ((p == null) || ("/".equals(p)))
/*  586 */       return null;
/*  587 */     int slash = p.lastIndexOf('/', p.length() - 2);
/*  588 */     if (slash >= 0)
/*  589 */       return p.substring(0, slash + 1);
/*  590 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String canonicalPath(String path)
/*      */   {
/*  607 */     if ((path == null) || (path.isEmpty())) {
/*  608 */       return path;
/*      */     }
/*  610 */     boolean slash = true;
/*  611 */     int end = path.length();
/*  612 */     int i = 0;
/*      */     
/*      */ 
/*  615 */     while (i < end)
/*      */     {
/*  617 */       char c = path.charAt(i);
/*  618 */       switch (c)
/*      */       {
/*      */       case '/': 
/*  621 */         slash = true;
/*  622 */         break;
/*      */       
/*      */       case '.': 
/*  625 */         if (slash)
/*      */           break label89;
/*  627 */         slash = false;
/*  628 */         break;
/*      */       
/*      */       default: 
/*  631 */         slash = false;
/*      */       }
/*      */       
/*  634 */       i++;
/*      */     }
/*      */     label89:
/*  637 */     if (i == end) {
/*  638 */       return path;
/*      */     }
/*  640 */     StringBuilder canonical = new StringBuilder(path.length());
/*  641 */     canonical.append(path, 0, i);
/*      */     
/*  643 */     int dots = 1;
/*  644 */     i++;
/*  645 */     while (i <= end)
/*      */     {
/*  647 */       char c = i < end ? path.charAt(i) : '\000';
/*  648 */       switch (c)
/*      */       {
/*      */       case '\000': 
/*      */       case '/': 
/*  652 */         switch (dots)
/*      */         {
/*      */         case 0: 
/*  655 */           if (c != 0) {
/*  656 */             canonical.append(c);
/*      */           }
/*      */           
/*      */           break;
/*      */         case 1: 
/*      */           break;
/*      */         case 2: 
/*  663 */           if (canonical.length() < 2)
/*  664 */             return null;
/*  665 */           canonical.setLength(canonical.length() - 1);
/*  666 */           canonical.setLength(canonical.lastIndexOf("/") + 1);
/*  667 */           break;
/*      */         
/*      */         default: 
/*  670 */           while (dots-- > 0)
/*  671 */             canonical.append('.');
/*  672 */           if (c != 0)
/*  673 */             canonical.append(c);
/*      */           break;
/*      */         }
/*  676 */         slash = true;
/*  677 */         dots = 0;
/*  678 */         break;
/*      */       
/*      */       case '.': 
/*  681 */         if (dots > 0) {
/*  682 */           dots++;
/*  683 */         } else if (slash) {
/*  684 */           dots = 1;
/*      */         } else
/*  686 */           canonical.append('.');
/*  687 */         slash = false;
/*  688 */         break;
/*      */       
/*      */       default: 
/*  691 */         while (dots-- > 0)
/*  692 */           canonical.append('.');
/*  693 */         canonical.append(c);
/*  694 */         dots = 0;
/*  695 */         slash = false;
/*      */       }
/*      */       
/*  698 */       i++;
/*      */     }
/*  700 */     return canonical.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String canonicalEncodedPath(String path)
/*      */   {
/*  718 */     if ((path == null) || (path.isEmpty())) {
/*  719 */       return path;
/*      */     }
/*  721 */     boolean slash = true;
/*  722 */     int end = path.length();
/*  723 */     int i = 0;
/*      */     
/*      */ 
/*  726 */     while (i < end)
/*      */     {
/*  728 */       char c = path.charAt(i);
/*  729 */       switch (c)
/*      */       {
/*      */       case '/': 
/*  732 */         slash = true;
/*  733 */         break;
/*      */       
/*      */       case '.': 
/*  736 */         if (slash)
/*      */           break label99;
/*  738 */         slash = false;
/*  739 */         break;
/*      */       
/*      */       case '?': 
/*  742 */         return path;
/*      */       
/*      */       default: 
/*  745 */         slash = false;
/*      */       }
/*      */       
/*  748 */       i++;
/*      */     }
/*      */     label99:
/*  751 */     if (i == end) {
/*  752 */       return path;
/*      */     }
/*  754 */     StringBuilder canonical = new StringBuilder(path.length());
/*  755 */     canonical.append(path, 0, i);
/*      */     
/*  757 */     int dots = 1;
/*  758 */     i++;
/*  759 */     while (i <= end)
/*      */     {
/*  761 */       char c = i < end ? path.charAt(i) : '\000';
/*  762 */       switch (c)
/*      */       {
/*      */       case '\000': 
/*      */       case '/': 
/*      */       case '?': 
/*  767 */         switch (dots)
/*      */         {
/*      */         case 0: 
/*  770 */           if (c != 0) {
/*  771 */             canonical.append(c);
/*      */           }
/*      */           break;
/*      */         case 1: 
/*  775 */           if (c == '?') {
/*  776 */             canonical.append(c);
/*      */           }
/*      */           break;
/*      */         case 2: 
/*  780 */           if (canonical.length() < 2)
/*  781 */             return null;
/*  782 */           canonical.setLength(canonical.length() - 1);
/*  783 */           canonical.setLength(canonical.lastIndexOf("/") + 1);
/*  784 */           if (c == '?')
/*  785 */             canonical.append(c);
/*      */           break;
/*      */         default: 
/*  788 */           while (dots-- > 0)
/*  789 */             canonical.append('.');
/*  790 */           if (c != 0)
/*  791 */             canonical.append(c);
/*      */           break;
/*      */         }
/*  794 */         slash = true;
/*  795 */         dots = 0;
/*  796 */         break;
/*      */       
/*      */       case '.': 
/*  799 */         if (dots > 0) {
/*  800 */           dots++;
/*  801 */         } else if (slash) {
/*  802 */           dots = 1;
/*      */         } else
/*  804 */           canonical.append('.');
/*  805 */         slash = false;
/*  806 */         break;
/*      */       
/*      */       default: 
/*  809 */         while (dots-- > 0)
/*  810 */           canonical.append('.');
/*  811 */         canonical.append(c);
/*  812 */         dots = 0;
/*  813 */         slash = false;
/*      */       }
/*      */       
/*  816 */       i++;
/*      */     }
/*  818 */     return canonical.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String compactPath(String path)
/*      */   {
/*  831 */     if ((path == null) || (path.length() == 0)) {
/*  832 */       return path;
/*      */     }
/*  834 */     int state = 0;
/*  835 */     int end = path.length();
/*  836 */     int i = 0;
/*      */     
/*      */ 
/*  839 */     while (i < end)
/*      */     {
/*  841 */       char c = path.charAt(i);
/*  842 */       switch (c)
/*      */       {
/*      */       case '?': 
/*  845 */         return path;
/*      */       case '/': 
/*  847 */         state++;
/*  848 */         if (state != 2) break;
/*  849 */         break;
/*      */       
/*      */       default: 
/*  852 */         state = 0;
/*      */       }
/*  854 */       i++;
/*      */     }
/*      */     
/*  857 */     if (state < 2) {
/*  858 */       return path;
/*      */     }
/*  860 */     StringBuilder buf = new StringBuilder(path.length());
/*  861 */     buf.append(path, 0, i);
/*      */     
/*      */ 
/*  864 */     while (i < end)
/*      */     {
/*  866 */       char c = path.charAt(i);
/*  867 */       switch (c)
/*      */       {
/*      */       case '?': 
/*  870 */         buf.append(path, i, end);
/*  871 */         break;
/*      */       case '/': 
/*  873 */         if (state++ == 0)
/*  874 */           buf.append(c);
/*      */         break;
/*      */       default: 
/*  877 */         state = 0;
/*  878 */         buf.append(c);
/*      */       }
/*  880 */       i++;
/*      */     }
/*      */     
/*  883 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasScheme(String uri)
/*      */   {
/*  893 */     for (int i = 0; i < uri.length(); i++)
/*      */     {
/*  895 */       char c = uri.charAt(i);
/*  896 */       if (c == ':')
/*  897 */         return true;
/*  898 */       if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && ((i <= 0) || (((c < '0') || (c > '9')) && (c != '.') && (c != '+') && (c != '-')))) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  907 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String newURI(String scheme, String server, int port, String path, String query)
/*      */   {
/*  922 */     StringBuilder builder = newURIBuilder(scheme, server, port);
/*  923 */     builder.append(path);
/*  924 */     if ((query != null) && (query.length() > 0))
/*  925 */       builder.append('?').append(query);
/*  926 */     return builder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static StringBuilder newURIBuilder(String scheme, String server, int port)
/*      */   {
/*  939 */     StringBuilder builder = new StringBuilder();
/*  940 */     appendSchemeHostPort(builder, scheme, server, port);
/*  941 */     return builder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void appendSchemeHostPort(StringBuilder url, String scheme, String server, int port)
/*      */   {
/*  954 */     url.append(scheme).append("://").append(HostPort.normalizeHost(server));
/*      */     
/*  956 */     if (port > 0)
/*      */     {
/*  958 */       switch (scheme)
/*      */       {
/*      */       case "http": 
/*  961 */         if (port != 80) {
/*  962 */           url.append(':').append(port);
/*      */         }
/*      */         break;
/*      */       case "https": 
/*  966 */         if (port != 443) {
/*  967 */           url.append(':').append(port);
/*      */         }
/*      */         break;
/*      */       default: 
/*  971 */         url.append(':').append(port);
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void appendSchemeHostPort(StringBuffer url, String scheme, String server, int port)
/*      */   {
/*  986 */     synchronized (url)
/*      */     {
/*  988 */       url.append(scheme).append("://").append(HostPort.normalizeHost(server));
/*      */       
/*  990 */       if (port > 0)
/*      */       {
/*  992 */         switch (scheme)
/*      */         {
/*      */         case "http": 
/*  995 */           if (port != 80) {
/*  996 */             url.append(':').append(port);
/*      */           }
/*      */           break;
/*      */         case "https": 
/* 1000 */           if (port != 443) {
/* 1001 */             url.append(':').append(port);
/*      */           }
/*      */           break;
/*      */         default: 
/* 1005 */           url.append(':').append(port);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean equalsIgnoreEncodings(String uriA, String uriB)
/*      */   {
/* 1013 */     int lenA = uriA.length();
/* 1014 */     int lenB = uriB.length();
/* 1015 */     int a = 0;
/* 1016 */     int b = 0;
/*      */     
/* 1018 */     while ((a < lenA) && (b < lenB))
/*      */     {
/* 1020 */       int oa = uriA.charAt(a++);
/* 1021 */       int ca = oa;
/* 1022 */       if (ca == 37) {
/* 1023 */         ca = TypeUtil.convertHexDigit(uriA.charAt(a++)) * 16 + TypeUtil.convertHexDigit(uriA.charAt(a++));
/*      */       }
/* 1025 */       int ob = uriB.charAt(b++);
/* 1026 */       int cb = ob;
/* 1027 */       if (cb == 37) {
/* 1028 */         cb = TypeUtil.convertHexDigit(uriB.charAt(b++)) * 16 + TypeUtil.convertHexDigit(uriB.charAt(b++));
/*      */       }
/* 1030 */       if ((ca == 47) && (oa != ob)) {
/* 1031 */         return false;
/*      */       }
/* 1033 */       if (ca != cb)
/* 1034 */         return decodePath(uriA).equals(decodePath(uriB));
/*      */     }
/* 1036 */     return (a == lenA) && (b == lenB);
/*      */   }
/*      */   
/*      */   public static boolean equalsIgnoreEncodings(URI uriA, URI uriB)
/*      */   {
/* 1041 */     if (uriA.equals(uriB)) {
/* 1042 */       return true;
/*      */     }
/* 1044 */     if (uriA.getScheme() == null)
/*      */     {
/* 1046 */       if (uriB.getScheme() != null) {
/* 1047 */         return false;
/*      */       }
/* 1049 */     } else if (!uriA.getScheme().equals(uriB.getScheme())) {
/* 1050 */       return false;
/*      */     }
/* 1052 */     if (uriA.getAuthority() == null)
/*      */     {
/* 1054 */       if (uriB.getAuthority() != null) {
/* 1055 */         return false;
/*      */       }
/* 1057 */     } else if (!uriA.getAuthority().equals(uriB.getAuthority())) {
/* 1058 */       return false;
/*      */     }
/* 1060 */     return equalsIgnoreEncodings(uriA.getPath(), uriB.getPath());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static URI addPath(URI uri, String path)
/*      */   {
/* 1070 */     String base = uri.toASCIIString();
/* 1071 */     StringBuilder buf = new StringBuilder(base.length() + path.length() * 3);
/* 1072 */     buf.append(base);
/* 1073 */     if (buf.charAt(base.length() - 1) != '/') {
/* 1074 */       buf.append('/');
/*      */     }
/* 1076 */     int offset = path.charAt(0) == '/' ? 1 : 0;
/* 1077 */     encodePath(buf, path, offset);
/*      */     
/* 1079 */     return URI.create(buf.toString());
/*      */   }
/*      */   
/*      */   public static URI getJarSource(URI uri)
/*      */   {
/*      */     try
/*      */     {
/* 1086 */       if (!"jar".equals(uri.getScheme())) {
/* 1087 */         return uri;
/*      */       }
/* 1089 */       String s = uri.getRawSchemeSpecificPart();
/* 1090 */       int bang_slash = s.indexOf("!/");
/* 1091 */       if (bang_slash >= 0)
/* 1092 */         s = s.substring(0, bang_slash);
/* 1093 */       return new URI(s);
/*      */     }
/*      */     catch (URISyntaxException e)
/*      */     {
/* 1097 */       throw new IllegalArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String getJarSource(String uri)
/*      */   {
/* 1103 */     if (!uri.startsWith("jar:"))
/* 1104 */       return uri;
/* 1105 */     int bang_slash = uri.indexOf("!/");
/* 1106 */     return bang_slash >= 0 ? uri.substring(4, bang_slash) : uri.substring(4);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\URIUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */