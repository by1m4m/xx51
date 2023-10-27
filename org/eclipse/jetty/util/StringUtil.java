/*      */ package org.eclipse.jetty.util;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
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
/*      */ public class StringUtil
/*      */ {
/*   40 */   private static final Logger LOG = Log.getLogger(StringUtil.class);
/*      */   
/*      */ 
/*   43 */   private static final Trie<String> CHARSETS = new ArrayTrie(256);
/*      */   
/*      */   public static final String ALL_INTERFACES = "0.0.0.0";
/*      */   
/*      */   public static final String CRLF = "\r\n";
/*      */   
/*      */   @Deprecated
/*   50 */   public static final String __LINE_SEPARATOR = System.lineSeparator();
/*      */   
/*      */   public static final String __ISO_8859_1 = "iso-8859-1";
/*      */   public static final String __UTF8 = "utf-8";
/*      */   public static final String __UTF16 = "utf-16";
/*      */   
/*      */   static
/*      */   {
/*   58 */     CHARSETS.put("utf-8", "utf-8");
/*   59 */     CHARSETS.put("utf8", "utf-8");
/*   60 */     CHARSETS.put("utf-16", "utf-16");
/*   61 */     CHARSETS.put("utf16", "utf-16");
/*   62 */     CHARSETS.put("iso-8859-1", "iso-8859-1");
/*   63 */     CHARSETS.put("iso_8859_1", "iso-8859-1");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String normalizeCharset(String s)
/*      */   {
/*   74 */     String n = (String)CHARSETS.get(s);
/*   75 */     return n == null ? s : n;
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
/*      */   public static String normalizeCharset(String s, int offset, int length)
/*      */   {
/*   88 */     String n = (String)CHARSETS.get(s, offset, length);
/*   89 */     return n == null ? s.substring(offset, offset + length) : n;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*   94 */   public static final char[] lowercases = { '\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '' };
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
/*      */   public static String asciiToLowerCase(String s)
/*      */   {
/*  120 */     if (s == null) {
/*  121 */       return null;
/*      */     }
/*  123 */     char[] c = null;
/*  124 */     int i = s.length();
/*      */     
/*      */ 
/*  127 */     while (i-- > 0)
/*      */     {
/*  129 */       char c1 = s.charAt(i);
/*  130 */       if (c1 <= '')
/*      */       {
/*  132 */         char c2 = lowercases[c1];
/*  133 */         if (c1 != c2)
/*      */         {
/*  135 */           c = s.toCharArray();
/*  136 */           c[i] = c2;
/*  137 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  142 */     while (i-- > 0)
/*      */     {
/*  144 */       if (c[i] <= '') {
/*  145 */         c[i] = lowercases[c[i]];
/*      */       }
/*      */     }
/*  148 */     return c == null ? s : new String(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCase(String s, String w)
/*      */   {
/*  155 */     if (w == null) {
/*  156 */       return true;
/*      */     }
/*  158 */     if ((s == null) || (s.length() < w.length())) {
/*  159 */       return false;
/*      */     }
/*  161 */     for (int i = 0; i < w.length(); i++)
/*      */     {
/*  163 */       char c1 = s.charAt(i);
/*  164 */       char c2 = w.charAt(i);
/*  165 */       if (c1 != c2)
/*      */       {
/*  167 */         if (c1 <= '')
/*  168 */           c1 = lowercases[c1];
/*  169 */         if (c2 <= '')
/*  170 */           c2 = lowercases[c2];
/*  171 */         if (c1 != c2)
/*  172 */           return false;
/*      */       }
/*      */     }
/*  175 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean endsWithIgnoreCase(String s, String w)
/*      */   {
/*  181 */     if (w == null) {
/*  182 */       return true;
/*      */     }
/*  184 */     if (s == null) {
/*  185 */       return false;
/*      */     }
/*  187 */     int sl = s.length();
/*  188 */     int wl = w.length();
/*      */     
/*  190 */     if (sl < wl) {
/*  191 */       return false;
/*      */     }
/*  193 */     for (int i = wl; i-- > 0;)
/*      */     {
/*  195 */       char c1 = s.charAt(--sl);
/*  196 */       char c2 = w.charAt(i);
/*  197 */       if (c1 != c2)
/*      */       {
/*  199 */         if (c1 <= '')
/*  200 */           c1 = lowercases[c1];
/*  201 */         if (c2 <= '')
/*  202 */           c2 = lowercases[c2];
/*  203 */         if (c1 != c2)
/*  204 */           return false;
/*      */       }
/*      */     }
/*  207 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int indexFrom(String s, String chars)
/*      */   {
/*  219 */     for (int i = 0; i < s.length(); i++)
/*  220 */       if (chars.indexOf(s.charAt(i)) >= 0)
/*  221 */         return i;
/*  222 */     return -1;
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
/*      */   public static String replace(String s, String sub, String with)
/*      */   {
/*  235 */     int c = 0;
/*  236 */     int i = s.indexOf(sub, c);
/*  237 */     if (i == -1) {
/*  238 */       return s;
/*      */     }
/*  240 */     StringBuilder buf = new StringBuilder(s.length() + with.length());
/*      */     
/*      */     do
/*      */     {
/*  244 */       buf.append(s.substring(c, i));
/*  245 */       buf.append(with);
/*  246 */       c = i + sub.length();
/*  247 */     } while ((i = s.indexOf(sub, c)) != -1);
/*      */     
/*  249 */     if (c < s.length()) {
/*  250 */       buf.append(s.substring(c, s.length()));
/*      */     }
/*  252 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static String unquote(String s)
/*      */   {
/*  263 */     return QuotedStringTokenizer.unquote(s);
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
/*      */   public static void append(StringBuilder buf, String s, int offset, int length)
/*      */   {
/*  279 */     synchronized (buf)
/*      */     {
/*  281 */       int end = offset + length;
/*  282 */       for (int i = offset; i < end; i++)
/*      */       {
/*  284 */         if (i >= s.length())
/*      */           break;
/*  286 */         buf.append(s.charAt(i));
/*      */       }
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
/*      */ 
/*      */ 
/*      */   public static void append(StringBuilder buf, byte b, int base)
/*      */   {
/*  302 */     int bi = 0xFF & b;
/*  303 */     int c = 48 + bi / base % base;
/*  304 */     if (c > 57)
/*  305 */       c = 97 + (c - 48 - 10);
/*  306 */     buf.append((char)c);
/*  307 */     c = 48 + bi % base;
/*  308 */     if (c > 57)
/*  309 */       c = 97 + (c - 48 - 10);
/*  310 */     buf.append((char)c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void append2digits(StringBuffer buf, int i)
/*      */   {
/*  322 */     if (i < 100)
/*      */     {
/*  324 */       buf.append((char)(i / 10 + 48));
/*  325 */       buf.append((char)(i % 10 + 48));
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
/*      */   public static void append2digits(StringBuilder buf, int i)
/*      */   {
/*  338 */     if (i < 100)
/*      */     {
/*  340 */       buf.append((char)(i / 10 + 48));
/*  341 */       buf.append((char)(i % 10 + 48));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String nonNull(String s)
/*      */   {
/*  352 */     if (s == null)
/*  353 */       return "";
/*  354 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean equals(String s, char[] buf, int offset, int length)
/*      */   {
/*  360 */     if (s.length() != length)
/*  361 */       return false;
/*  362 */     for (int i = 0; i < length; i++)
/*  363 */       if (buf[(offset + i)] != s.charAt(i))
/*  364 */         return false;
/*  365 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String toUTF8String(byte[] b, int offset, int length)
/*      */   {
/*  371 */     return new String(b, offset, length, StandardCharsets.UTF_8);
/*      */   }
/*      */   
/*      */ 
/*      */   public static String toString(byte[] b, int offset, int length, String charset)
/*      */   {
/*      */     try
/*      */     {
/*  379 */       return new String(b, offset, length, charset);
/*      */     }
/*      */     catch (UnsupportedEncodingException e)
/*      */     {
/*  383 */       throw new IllegalArgumentException(e);
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
/*      */   public static int indexOfControlChars(String str)
/*      */   {
/*  417 */     if (str == null)
/*      */     {
/*  419 */       return -1;
/*      */     }
/*  421 */     int len = str.length();
/*  422 */     for (int i = 0; i < len; i++)
/*      */     {
/*  424 */       if (Character.isISOControl(str.codePointAt(i)))
/*      */       {
/*      */ 
/*  427 */         return i;
/*      */       }
/*      */     }
/*      */     
/*  431 */     return -1;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isBlank(String str)
/*      */   {
/*  457 */     if (str == null)
/*      */     {
/*  459 */       return true;
/*      */     }
/*  461 */     int len = str.length();
/*  462 */     for (int i = 0; i < len; i++)
/*      */     {
/*  464 */       if (!Character.isWhitespace(str.codePointAt(i)))
/*      */       {
/*      */ 
/*  467 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  471 */     return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isNotBlank(String str)
/*      */   {
/*  497 */     if (str == null)
/*      */     {
/*  499 */       return false;
/*      */     }
/*  501 */     int len = str.length();
/*  502 */     for (int i = 0; i < len; i++)
/*      */     {
/*  504 */       if (!Character.isWhitespace(str.codePointAt(i)))
/*      */       {
/*      */ 
/*  507 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  511 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean isUTF8(String charset)
/*      */   {
/*  517 */     return ("utf-8".equalsIgnoreCase(charset)) || ("utf-8".equalsIgnoreCase(normalizeCharset(charset)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String printable(String name)
/*      */   {
/*  524 */     if (name == null)
/*  525 */       return null;
/*  526 */     StringBuilder buf = new StringBuilder(name.length());
/*  527 */     for (int i = 0; i < name.length(); i++)
/*      */     {
/*  529 */       char c = name.charAt(i);
/*  530 */       if (!Character.isISOControl(c))
/*  531 */         buf.append(c);
/*      */     }
/*  533 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   public static String printable(byte[] b)
/*      */   {
/*  539 */     StringBuilder buf = new StringBuilder();
/*  540 */     for (int i = 0; i < b.length; i++)
/*      */     {
/*  542 */       char c = (char)b[i];
/*  543 */       if ((Character.isWhitespace(c)) || ((c > ' ') && (c < ''))) {
/*  544 */         buf.append(c);
/*      */       }
/*      */       else {
/*  547 */         buf.append("0x");
/*  548 */         TypeUtil.toHex(b[i], buf);
/*      */       }
/*      */     }
/*  551 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public static byte[] getBytes(String s)
/*      */   {
/*  556 */     return s.getBytes(StandardCharsets.ISO_8859_1);
/*      */   }
/*      */   
/*      */   public static byte[] getUtf8Bytes(String s)
/*      */   {
/*  561 */     return s.getBytes(StandardCharsets.UTF_8);
/*      */   }
/*      */   
/*      */   public static byte[] getBytes(String s, String charset)
/*      */   {
/*      */     try
/*      */     {
/*  568 */       return s.getBytes(charset);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  572 */       LOG.warn(e); }
/*  573 */     return s.getBytes();
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
/*      */   @Deprecated
/*      */   public static String sidBytesToString(byte[] sidBytes)
/*      */   {
/*  591 */     StringBuilder sidString = new StringBuilder();
/*      */     
/*      */ 
/*  594 */     sidString.append("S-");
/*      */     
/*      */ 
/*  597 */     sidString.append(Byte.toString(sidBytes[0])).append('-');
/*      */     
/*  599 */     StringBuilder tmpBuilder = new StringBuilder();
/*      */     
/*      */ 
/*  602 */     for (int i = 2; i <= 7; i++)
/*      */     {
/*  604 */       tmpBuilder.append(Integer.toHexString(sidBytes[i] & 0xFF));
/*      */     }
/*      */     
/*  607 */     sidString.append(Long.parseLong(tmpBuilder.toString(), 16));
/*      */     
/*      */ 
/*  610 */     int subAuthorityCount = sidBytes[1];
/*      */     
/*      */ 
/*  613 */     for (int i = 0; i < subAuthorityCount; i++)
/*      */     {
/*  615 */       int offset = i * 4;
/*  616 */       tmpBuilder.setLength(0);
/*      */       
/*  618 */       tmpBuilder.append(String.format("%02X%02X%02X%02X", new Object[] {
/*  619 */         Integer.valueOf(sidBytes[(11 + offset)] & 0xFF), 
/*  620 */         Integer.valueOf(sidBytes[(10 + offset)] & 0xFF), 
/*  621 */         Integer.valueOf(sidBytes[(9 + offset)] & 0xFF), 
/*  622 */         Integer.valueOf(sidBytes[(8 + offset)] & 0xFF) }));
/*  623 */       sidString.append('-').append(Long.parseLong(tmpBuilder.toString(), 16));
/*      */     }
/*      */     
/*  626 */     return sidString.toString();
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
/*      */   @Deprecated
/*      */   public static byte[] sidStringToBytes(String sidString)
/*      */   {
/*  641 */     String[] sidTokens = sidString.split("-");
/*      */     
/*  643 */     int subAuthorityCount = sidTokens.length - 3;
/*      */     
/*  645 */     int byteCount = 0;
/*  646 */     byte[] sidBytes = new byte[8 + 4 * subAuthorityCount];
/*      */     
/*      */ 
/*  649 */     sidBytes[(byteCount++)] = ((byte)Integer.parseInt(sidTokens[1]));
/*      */     
/*      */ 
/*  652 */     sidBytes[(byteCount++)] = ((byte)subAuthorityCount);
/*      */     
/*      */ 
/*  655 */     String hexStr = Long.toHexString(Long.parseLong(sidTokens[2]));
/*      */     
/*  657 */     while (hexStr.length() < 12)
/*      */     {
/*  659 */       hexStr = "0" + hexStr;
/*      */     }
/*      */     
/*      */ 
/*  663 */     for (int i = 0; i < hexStr.length(); i += 2)
/*      */     {
/*  665 */       sidBytes[(byteCount++)] = ((byte)Integer.parseInt(hexStr.substring(i, i + 2), 16));
/*      */     }
/*      */     
/*      */ 
/*  669 */     for (int i = 3; i < sidTokens.length; i++)
/*      */     {
/*  671 */       hexStr = Long.toHexString(Long.parseLong(sidTokens[i]));
/*      */       
/*  673 */       while (hexStr.length() < 8)
/*      */       {
/*  675 */         hexStr = "0" + hexStr;
/*      */       }
/*      */       
/*      */ 
/*  679 */       for (int j = hexStr.length(); j > 0; j -= 2)
/*      */       {
/*  681 */         sidBytes[(byteCount++)] = ((byte)Integer.parseInt(hexStr.substring(j - 2, j), 16));
/*      */       }
/*      */     }
/*      */     
/*  685 */     return sidBytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int toInt(String string, int from)
/*      */   {
/*  697 */     int val = 0;
/*  698 */     boolean started = false;
/*  699 */     boolean minus = false;
/*      */     
/*  701 */     for (int i = from; i < string.length(); i++)
/*      */     {
/*  703 */       char b = string.charAt(i);
/*  704 */       if (b <= ' ')
/*      */       {
/*  706 */         if (started) {
/*      */           break;
/*      */         }
/*  709 */       } else if ((b >= '0') && (b <= '9'))
/*      */       {
/*  711 */         val = val * 10 + (b - '0');
/*  712 */         started = true;
/*      */       } else {
/*  714 */         if ((b != '-') || (started))
/*      */           break;
/*  716 */         minus = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  722 */     if (started)
/*  723 */       return minus ? -val : val;
/*  724 */     throw new NumberFormatException(string);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long toLong(String string)
/*      */   {
/*  736 */     long val = 0L;
/*  737 */     boolean started = false;
/*  738 */     boolean minus = false;
/*      */     
/*  740 */     for (int i = 0; i < string.length(); i++)
/*      */     {
/*  742 */       char b = string.charAt(i);
/*  743 */       if (b <= ' ')
/*      */       {
/*  745 */         if (started) {
/*      */           break;
/*      */         }
/*  748 */       } else if ((b >= '0') && (b <= '9'))
/*      */       {
/*  750 */         val = val * 10L + (b - '0');
/*  751 */         started = true;
/*      */       } else {
/*  753 */         if ((b != '-') || (started))
/*      */           break;
/*  755 */         minus = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  761 */     if (started)
/*  762 */       return minus ? -val : val;
/*  763 */     throw new NumberFormatException(string);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String truncate(String str, int maxSize)
/*      */   {
/*  775 */     if (str == null)
/*      */     {
/*  777 */       return null;
/*      */     }
/*      */     
/*  780 */     if (str.length() <= maxSize)
/*      */     {
/*  782 */       return str;
/*      */     }
/*      */     
/*  785 */     return str.substring(0, maxSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] arrayFromString(String s)
/*      */   {
/*  795 */     if (s == null) {
/*  796 */       return new String[0];
/*      */     }
/*  798 */     if ((!s.startsWith("[")) || (!s.endsWith("]")))
/*  799 */       throw new IllegalArgumentException();
/*  800 */     if (s.length() == 2) {
/*  801 */       return new String[0];
/*      */     }
/*  803 */     return csvSplit(s, 1, s.length() - 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] csvSplit(String s)
/*      */   {
/*  813 */     if (s == null)
/*  814 */       return null;
/*  815 */     return csvSplit(s, 0, s.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] csvSplit(String s, int off, int len)
/*      */   {
/*  827 */     if (s == null)
/*  828 */       return null;
/*  829 */     if ((off < 0) || (len < 0) || (off > s.length())) {
/*  830 */       throw new IllegalArgumentException();
/*      */     }
/*  832 */     List<String> list = new ArrayList();
/*  833 */     csvSplit(list, s, off, len);
/*  834 */     return (String[])list.toArray(new String[list.size()]);
/*      */   }
/*      */   
/*  837 */   static enum CsvSplitState { PRE_DATA,  QUOTE,  SLOSH,  DATA,  WHITE,  POST_DATA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private CsvSplitState() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<String> csvSplit(List<String> list, String s, int off, int len)
/*      */   {
/*  855 */     if (list == null)
/*  856 */       list = new ArrayList();
/*  857 */     CsvSplitState state = CsvSplitState.PRE_DATA;
/*  858 */     StringBuilder out = new StringBuilder();
/*  859 */     int last = -1;
/*  860 */     while (len > 0)
/*      */     {
/*  862 */       char ch = s.charAt(off++);
/*  863 */       len--;
/*      */       
/*  865 */       switch (state)
/*      */       {
/*      */       case PRE_DATA: 
/*  868 */         if (!Character.isWhitespace(ch))
/*      */         {
/*      */ 
/*  871 */           if ('"' == ch)
/*      */           {
/*  873 */             state = CsvSplitState.QUOTE;
/*      */ 
/*      */ 
/*      */           }
/*  877 */           else if (',' == ch)
/*      */           {
/*  879 */             list.add("");
/*      */           }
/*      */           else
/*      */           {
/*  883 */             state = CsvSplitState.DATA;
/*  884 */             out.append(ch); } }
/*  885 */         break;
/*      */       
/*      */       case DATA: 
/*  888 */         if (Character.isWhitespace(ch))
/*      */         {
/*  890 */           last = out.length();
/*  891 */           out.append(ch);
/*  892 */           state = CsvSplitState.WHITE;
/*      */ 
/*      */ 
/*      */         }
/*  896 */         else if (',' == ch)
/*      */         {
/*  898 */           list.add(out.toString());
/*  899 */           out.setLength(0);
/*  900 */           state = CsvSplitState.PRE_DATA;
/*      */         }
/*      */         else
/*      */         {
/*  904 */           out.append(ch); }
/*  905 */         break;
/*      */       
/*      */       case WHITE: 
/*  908 */         if (Character.isWhitespace(ch))
/*      */         {
/*  910 */           out.append(ch);
/*      */ 
/*      */ 
/*      */         }
/*  914 */         else if (',' == ch)
/*      */         {
/*  916 */           out.setLength(last);
/*  917 */           list.add(out.toString());
/*  918 */           out.setLength(0);
/*  919 */           state = CsvSplitState.PRE_DATA;
/*      */         }
/*      */         else
/*      */         {
/*  923 */           state = CsvSplitState.DATA;
/*  924 */           out.append(ch);
/*  925 */           last = -1; }
/*  926 */         break;
/*      */       
/*      */       case QUOTE: 
/*  929 */         if ('\\' == ch)
/*      */         {
/*  931 */           state = CsvSplitState.SLOSH;
/*      */ 
/*      */         }
/*  934 */         else if ('"' == ch)
/*      */         {
/*  936 */           list.add(out.toString());
/*  937 */           out.setLength(0);
/*  938 */           state = CsvSplitState.POST_DATA;
/*      */         }
/*      */         else {
/*  941 */           out.append(ch); }
/*  942 */         break;
/*      */       
/*      */       case SLOSH: 
/*  945 */         out.append(ch);
/*  946 */         state = CsvSplitState.QUOTE;
/*  947 */         break;
/*      */       
/*      */       case POST_DATA: 
/*  950 */         if (',' == ch)
/*      */         {
/*  952 */           state = CsvSplitState.PRE_DATA;
/*      */         }
/*      */         
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*  959 */     switch (state)
/*      */     {
/*      */     case PRE_DATA: 
/*      */     case POST_DATA: 
/*      */       break;
/*      */     
/*      */     case DATA: 
/*      */     case QUOTE: 
/*      */     case SLOSH: 
/*  968 */       list.add(out.toString());
/*  969 */       break;
/*      */     
/*      */     case WHITE: 
/*  972 */       out.setLength(last);
/*  973 */       list.add(out.toString());
/*      */     }
/*      */     
/*      */     
/*  977 */     return list;
/*      */   }
/*      */   
/*      */   public static String sanitizeXmlString(String html)
/*      */   {
/*  982 */     if (html == null) {
/*  983 */       return null;
/*      */     }
/*  985 */     for (int i = 0; 
/*      */         
/*      */ 
/*  988 */         i < html.length(); i++)
/*      */     {
/*  990 */       char c = html.charAt(i);
/*      */       
/*  992 */       switch (c)
/*      */       {
/*      */       case '"': 
/*      */       case '&': 
/*      */       case '\'': 
/*      */       case '<': 
/*      */       case '>': 
/*      */         break;
/*      */       
/*      */       default: 
/* 1002 */         if ((Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */     }
/* 1008 */     if (i == html.length()) {
/* 1009 */       return html;
/*      */     }
/*      */     
/* 1012 */     StringBuilder out = new StringBuilder(html.length() * 4 / 3);
/* 1013 */     out.append(html, 0, i);
/* 1016 */     for (; 
/*      */         
/* 1016 */         i < html.length(); i++)
/*      */     {
/* 1018 */       char c = html.charAt(i);
/*      */       
/* 1020 */       switch (c)
/*      */       {
/*      */       case '&': 
/* 1023 */         out.append("&amp;");
/* 1024 */         break;
/*      */       case '<': 
/* 1026 */         out.append("&lt;");
/* 1027 */         break;
/*      */       case '>': 
/* 1029 */         out.append("&gt;");
/* 1030 */         break;
/*      */       case '\'': 
/* 1032 */         out.append("&apos;");
/* 1033 */         break;
/*      */       case '"': 
/* 1035 */         out.append("&quot;");
/* 1036 */         break;
/*      */       
/*      */       default: 
/* 1039 */         if ((Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/* 1040 */           out.append('?');
/*      */         } else
/* 1042 */           out.append(c);
/*      */         break; }
/*      */     }
/* 1045 */     return out.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String valueOf(Object object)
/*      */   {
/* 1057 */     return object == null ? null : String.valueOf(object);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */