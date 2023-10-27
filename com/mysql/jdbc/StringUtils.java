/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public class StringUtils
/*      */ {
/*      */   private static final int BYTE_RANGE = 256;
/*   55 */   private static byte[] allBytes = new byte['Ā'];
/*      */   
/*   57 */   private static char[] byteToChars = new char['Ā'];
/*      */   
/*      */   private static Method toPlainStringMethod;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_NO_WILD = 0;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_WITH_WILD = 1;
/*      */   
/*      */   static final int WILD_COMPARE_NO_MATCH = -1;
/*      */   
/*   67 */   private static final ConcurrentHashMap<String, Charset> charsetsByAlias = new ConcurrentHashMap();
/*      */   
/*      */ 
/*   70 */   private static final String platformEncoding = System.getProperty("file.encoding");
/*      */   private static final String VALID_ID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@";
/*      */   
/*      */   static Charset findCharset(String alias) throws UnsupportedEncodingException
/*      */   {
/*      */     try {
/*   76 */       Charset cs = (Charset)charsetsByAlias.get(alias);
/*      */       
/*   78 */       if (cs == null) {
/*   79 */         cs = Charset.forName(alias);
/*   80 */         charsetsByAlias.putIfAbsent(alias, cs);
/*      */       }
/*      */       
/*   83 */       return cs;
/*      */     }
/*      */     catch (UnsupportedCharsetException uce)
/*      */     {
/*   87 */       throw new UnsupportedEncodingException(alias);
/*      */     } catch (IllegalCharsetNameException icne) {
/*   89 */       throw new UnsupportedEncodingException(alias);
/*      */     } catch (IllegalArgumentException iae) {
/*   91 */       throw new UnsupportedEncodingException(alias);
/*      */     }
/*      */   }
/*      */   
/*      */   static {
/*   96 */     for (int i = -128; i <= 127; i++) {
/*   97 */       allBytes[(i - -128)] = ((byte)i);
/*      */     }
/*      */     
/*  100 */     String allBytesString = new String(allBytes, 0, 255);
/*      */     
/*      */ 
/*  103 */     int allBytesStringLen = allBytesString.length();
/*      */     
/*  105 */     for (int i = 0; 
/*  106 */         (i < 255) && (i < allBytesStringLen); i++) {
/*  107 */       byteToChars[i] = allBytesString.charAt(i);
/*      */     }
/*      */     try
/*      */     {
/*  111 */       toPlainStringMethod = BigDecimal.class.getMethod("toPlainString", new Class[0]);
/*      */     }
/*      */     catch (NoSuchMethodException nsme) {}
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
/*      */   public static String consistentToString(BigDecimal decimal)
/*      */   {
/*  128 */     if (decimal == null) {
/*  129 */       return null;
/*      */     }
/*      */     
/*  132 */     if (toPlainStringMethod != null) {
/*      */       try {
/*  134 */         return (String)toPlainStringMethod.invoke(decimal, (Object[])null);
/*      */       }
/*      */       catch (InvocationTargetException invokeEx) {}catch (IllegalAccessException accessEx) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  142 */     return decimal.toString();
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
/*      */   public static final String dumpAsHex(byte[] byteBuffer, int length)
/*      */   {
/*  156 */     StringBuffer outputBuf = new StringBuffer(length * 4);
/*      */     
/*  158 */     int p = 0;
/*  159 */     int rows = length / 8;
/*      */     
/*  161 */     for (int i = 0; (i < rows) && (p < length); i++) {
/*  162 */       int ptemp = p;
/*      */       
/*  164 */       for (int j = 0; j < 8; j++) {
/*  165 */         String hexVal = Integer.toHexString(byteBuffer[ptemp] & 0xFF);
/*      */         
/*  167 */         if (hexVal.length() == 1) {
/*  168 */           hexVal = "0" + hexVal;
/*      */         }
/*      */         
/*  171 */         outputBuf.append(hexVal + " ");
/*  172 */         ptemp++;
/*      */       }
/*      */       
/*  175 */       outputBuf.append("    ");
/*      */       
/*  177 */       for (int j = 0; j < 8; j++) {
/*  178 */         int b = 0xFF & byteBuffer[p];
/*      */         
/*  180 */         if ((b > 32) && (b < 127)) {
/*  181 */           outputBuf.append((char)b + " ");
/*      */         } else {
/*  183 */           outputBuf.append(". ");
/*      */         }
/*      */         
/*  186 */         p++;
/*      */       }
/*      */       
/*  189 */       outputBuf.append("\n");
/*      */     }
/*      */     
/*  192 */     int n = 0;
/*      */     
/*  194 */     for (int i = p; i < length; i++) {
/*  195 */       String hexVal = Integer.toHexString(byteBuffer[i] & 0xFF);
/*      */       
/*  197 */       if (hexVal.length() == 1) {
/*  198 */         hexVal = "0" + hexVal;
/*      */       }
/*      */       
/*  201 */       outputBuf.append(hexVal + " ");
/*  202 */       n++;
/*      */     }
/*      */     
/*  205 */     for (int i = n; i < 8; i++) {
/*  206 */       outputBuf.append("   ");
/*      */     }
/*      */     
/*  209 */     outputBuf.append("    ");
/*      */     
/*  211 */     for (int i = p; i < length; i++) {
/*  212 */       int b = 0xFF & byteBuffer[i];
/*      */       
/*  214 */       if ((b > 32) && (b < 127)) {
/*  215 */         outputBuf.append((char)b + " ");
/*      */       } else {
/*  217 */         outputBuf.append(". ");
/*      */       }
/*      */     }
/*      */     
/*  221 */     outputBuf.append("\n");
/*      */     
/*  223 */     return outputBuf.toString();
/*      */   }
/*      */   
/*      */   private static boolean endsWith(byte[] dataFrom, String suffix) {
/*  227 */     for (int i = 1; i <= suffix.length(); i++) {
/*  228 */       int dfOffset = dataFrom.length - i;
/*  229 */       int suffixOffset = suffix.length() - i;
/*  230 */       if (dataFrom[dfOffset] != suffix.charAt(suffixOffset)) {
/*  231 */         return false;
/*      */       }
/*      */     }
/*  234 */     return true;
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
/*      */   public static byte[] escapeEasternUnicodeByteStream(byte[] origBytes, String origString, int offset, int length)
/*      */   {
/*  254 */     if ((origBytes == null) || (origBytes.length == 0)) {
/*  255 */       return origBytes;
/*      */     }
/*      */     
/*  258 */     int bytesLen = origBytes.length;
/*  259 */     int bufIndex = 0;
/*  260 */     int strIndex = 0;
/*      */     
/*  262 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(bytesLen);
/*      */     for (;;)
/*      */     {
/*  265 */       if (origString.charAt(strIndex) == '\\')
/*      */       {
/*  267 */         bytesOut.write(origBytes[(bufIndex++)]);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  272 */         int loByte = origBytes[bufIndex];
/*      */         
/*  274 */         if (loByte < 0) {
/*  275 */           loByte += 256;
/*      */         }
/*      */         
/*      */ 
/*  279 */         bytesOut.write(loByte);
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
/*  297 */         if (loByte >= 128) {
/*  298 */           if (bufIndex < bytesLen - 1) {
/*  299 */             int hiByte = origBytes[(bufIndex + 1)];
/*      */             
/*  301 */             if (hiByte < 0) {
/*  302 */               hiByte += 256;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  307 */             bytesOut.write(hiByte);
/*  308 */             bufIndex++;
/*      */             
/*      */ 
/*  311 */             if (hiByte == 92) {
/*  312 */               bytesOut.write(hiByte);
/*      */             }
/*      */           }
/*  315 */         } else if ((loByte == 92) && 
/*  316 */           (bufIndex < bytesLen - 1)) {
/*  317 */           int hiByte = origBytes[(bufIndex + 1)];
/*      */           
/*  319 */           if (hiByte < 0) {
/*  320 */             hiByte += 256;
/*      */           }
/*      */           
/*  323 */           if (hiByte == 98)
/*      */           {
/*  325 */             bytesOut.write(92);
/*  326 */             bytesOut.write(98);
/*  327 */             bufIndex++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  332 */         bufIndex++;
/*      */       }
/*      */       
/*  335 */       if (bufIndex >= bytesLen) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  340 */       strIndex++;
/*      */     }
/*      */     
/*  343 */     return bytesOut.toByteArray();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static char firstNonWsCharUc(String searchIn)
/*      */   {
/*  355 */     return firstNonWsCharUc(searchIn, 0);
/*      */   }
/*      */   
/*      */   public static char firstNonWsCharUc(String searchIn, int startAt) {
/*  359 */     if (searchIn == null) {
/*  360 */       return '\000';
/*      */     }
/*      */     
/*  363 */     int length = searchIn.length();
/*      */     
/*  365 */     for (int i = startAt; i < length; i++) {
/*  366 */       char c = searchIn.charAt(i);
/*      */       
/*  368 */       if (!Character.isWhitespace(c)) {
/*  369 */         return Character.toUpperCase(c);
/*      */       }
/*      */     }
/*      */     
/*  373 */     return '\000';
/*      */   }
/*      */   
/*      */   public static char firstAlphaCharUc(String searchIn, int startAt) {
/*  377 */     if (searchIn == null) {
/*  378 */       return '\000';
/*      */     }
/*      */     
/*  381 */     int length = searchIn.length();
/*      */     
/*  383 */     for (int i = startAt; i < length; i++) {
/*  384 */       char c = searchIn.charAt(i);
/*      */       
/*  386 */       if (Character.isLetter(c)) {
/*  387 */         return Character.toUpperCase(c);
/*      */       }
/*      */     }
/*      */     
/*  391 */     return '\000';
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
/*      */   public static final String fixDecimalExponent(String dString)
/*      */   {
/*  404 */     int ePos = dString.indexOf("E");
/*      */     
/*  406 */     if (ePos == -1) {
/*  407 */       ePos = dString.indexOf("e");
/*      */     }
/*      */     
/*  410 */     if ((ePos != -1) && 
/*  411 */       (dString.length() > ePos + 1)) {
/*  412 */       char maybeMinusChar = dString.charAt(ePos + 1);
/*      */       
/*  414 */       if ((maybeMinusChar != '-') && (maybeMinusChar != '+')) {
/*  415 */         StringBuffer buf = new StringBuffer(dString.length() + 1);
/*  416 */         buf.append(dString.substring(0, ePos + 1));
/*  417 */         buf.append('+');
/*  418 */         buf.append(dString.substring(ePos + 1, dString.length()));
/*  419 */         dString = buf.toString();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  424 */     return dString;
/*      */   }
/*      */   
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  432 */       byte[] b = null;
/*      */       String s;
/*  434 */       if (converter != null) {
/*  435 */         b = converter.toBytes(c);
/*  436 */       } else if (encoding == null) {
/*  437 */         b = new String(c).getBytes();
/*      */       } else {
/*  439 */         s = new String(c);
/*      */         
/*  441 */         b = s.getBytes(encoding);
/*      */         
/*  443 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  447 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  448 */       return escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  455 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  466 */       byte[] b = null;
/*      */       String s;
/*  468 */       if (converter != null) {
/*  469 */         b = converter.toBytes(c, offset, length);
/*  470 */       } else if (encoding == null) {
/*  471 */         byte[] temp = new String(c, offset, length).getBytes();
/*      */         
/*  473 */         length = temp.length;
/*      */         
/*  475 */         b = new byte[length];
/*  476 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       } else {
/*  478 */         s = new String(c, offset, length);
/*      */         
/*  480 */         byte[] temp = s.getBytes(encoding);
/*      */         
/*  482 */         length = temp.length;
/*      */         
/*  484 */         b = new byte[length];
/*  485 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  487 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  491 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  492 */       return escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  499 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009", exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(char[] c, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  511 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  513 */       if (conn != null) {
/*  514 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  516 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       }
/*      */       
/*  519 */       return getBytes(c, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
/*      */     }
/*      */     catch (UnsupportedEncodingException uee) {
/*  522 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009", exceptionInterceptor);
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
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  553 */       byte[] b = null;
/*      */       
/*  555 */       if (converter != null) {
/*  556 */         b = converter.toBytes(s);
/*  557 */       } else if (encoding == null) {
/*  558 */         b = s.getBytes();
/*      */       } else {
/*  560 */         b = s.getBytes(encoding);
/*      */         
/*  562 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  566 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  567 */       return escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  574 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytesWrapped(String s, char beginWrap, char endWrap, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  585 */       byte[] b = null;
/*      */       
/*  587 */       if (converter != null) {
/*  588 */         b = converter.toBytesWrapped(s, beginWrap, endWrap);
/*  589 */       } else if (encoding == null) {
/*  590 */         StringBuffer buf = new StringBuffer(s.length() + 2);
/*  591 */         buf.append(beginWrap);
/*  592 */         buf.append(s);
/*  593 */         buf.append(endWrap);
/*      */         
/*  595 */         b = buf.toString().getBytes();
/*      */       } else {
/*  597 */         StringBuffer buf = new StringBuffer(s.length() + 2);
/*  598 */         buf.append(beginWrap);
/*  599 */         buf.append(s);
/*  600 */         buf.append(endWrap);
/*      */         
/*  602 */         s = buf.toString();
/*  603 */         b = s.getBytes(encoding);
/*      */         
/*  605 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  609 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  610 */       return escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  617 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009", exceptionInterceptor);
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
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  651 */       byte[] b = null;
/*      */       
/*  653 */       if (converter != null) {
/*  654 */         b = converter.toBytes(s, offset, length);
/*  655 */       } else if (encoding == null) {
/*  656 */         byte[] temp = s.substring(offset, offset + length).getBytes();
/*      */         
/*  658 */         length = temp.length;
/*      */         
/*  660 */         b = new byte[length];
/*  661 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       }
/*      */       else {
/*  664 */         byte[] temp = s.substring(offset, offset + length).getBytes(encoding);
/*      */         
/*      */ 
/*  667 */         length = temp.length;
/*      */         
/*  669 */         b = new byte[length];
/*  670 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  672 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  676 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  677 */       return escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  684 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009", exceptionInterceptor);
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
/*      */   public static final byte[] getBytes(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn, ExceptionInterceptor exceptionInterceptor)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  711 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  713 */       if (conn != null) {
/*  714 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  716 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       }
/*      */       
/*  719 */       return getBytes(s, converter, encoding, serverEncoding, parserKnowsUnicode, exceptionInterceptor);
/*      */     }
/*      */     catch (UnsupportedEncodingException uee) {
/*  722 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009", exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] buf, int offset, int endPos)
/*      */     throws NumberFormatException
/*      */   {
/*  729 */     int base = 10;
/*      */     
/*  731 */     int s = offset;
/*      */     
/*      */ 
/*  734 */     while ((Character.isWhitespace((char)buf[s])) && (s < endPos)) {
/*  735 */       s++;
/*      */     }
/*      */     
/*  738 */     if (s == endPos) {
/*  739 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  743 */     boolean negative = false;
/*      */     
/*  745 */     if ((char)buf[s] == '-') {
/*  746 */       negative = true;
/*  747 */       s++;
/*  748 */     } else if ((char)buf[s] == '+') {
/*  749 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  753 */     int save = s;
/*      */     
/*  755 */     int cutoff = Integer.MAX_VALUE / base;
/*  756 */     int cutlim = Integer.MAX_VALUE % base;
/*      */     
/*  758 */     if (negative) {
/*  759 */       cutlim++;
/*      */     }
/*      */     
/*  762 */     boolean overflow = false;
/*      */     
/*  764 */     int i = 0;
/*  766 */     for (; 
/*  766 */         s < endPos; s++) {
/*  767 */       char c = (char)buf[s];
/*      */       
/*  769 */       if (Character.isDigit(c)) {
/*  770 */         c = (char)(c - '0');
/*  771 */       } else { if (!Character.isLetter(c)) break;
/*  772 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  777 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  782 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  783 */         overflow = true;
/*      */       } else {
/*  785 */         i *= base;
/*  786 */         i += c;
/*      */       }
/*      */     }
/*      */     
/*  790 */     if (s == save) {
/*  791 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*  794 */     if (overflow) {
/*  795 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  799 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] buf) throws NumberFormatException {
/*  803 */     return getInt(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] buf) throws NumberFormatException {
/*  807 */     return getLong(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] buf, int offset, int endpos) throws NumberFormatException {
/*  811 */     int base = 10;
/*      */     
/*  813 */     int s = offset;
/*      */     
/*      */ 
/*  816 */     while ((Character.isWhitespace((char)buf[s])) && (s < endpos)) {
/*  817 */       s++;
/*      */     }
/*      */     
/*  820 */     if (s == endpos) {
/*  821 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  825 */     boolean negative = false;
/*      */     
/*  827 */     if ((char)buf[s] == '-') {
/*  828 */       negative = true;
/*  829 */       s++;
/*  830 */     } else if ((char)buf[s] == '+') {
/*  831 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  835 */     int save = s;
/*      */     
/*  837 */     long cutoff = Long.MAX_VALUE / base;
/*  838 */     long cutlim = (int)(Long.MAX_VALUE % base);
/*      */     
/*  840 */     if (negative) {
/*  841 */       cutlim += 1L;
/*      */     }
/*      */     
/*  844 */     boolean overflow = false;
/*  845 */     long i = 0L;
/*  847 */     for (; 
/*  847 */         s < endpos; s++) {
/*  848 */       char c = (char)buf[s];
/*      */       
/*  850 */       if (Character.isDigit(c)) {
/*  851 */         c = (char)(c - '0');
/*  852 */       } else { if (!Character.isLetter(c)) break;
/*  853 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  858 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  863 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  864 */         overflow = true;
/*      */       } else {
/*  866 */         i *= base;
/*  867 */         i += c;
/*      */       }
/*      */     }
/*      */     
/*  871 */     if (s == save) {
/*  872 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*  875 */     if (overflow) {
/*  876 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  880 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static short getShort(byte[] buf) throws NumberFormatException {
/*  884 */     short base = 10;
/*      */     
/*  886 */     int s = 0;
/*      */     
/*      */ 
/*  889 */     while ((Character.isWhitespace((char)buf[s])) && (s < buf.length)) {
/*  890 */       s++;
/*      */     }
/*      */     
/*  893 */     if (s == buf.length) {
/*  894 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  898 */     boolean negative = false;
/*      */     
/*  900 */     if ((char)buf[s] == '-') {
/*  901 */       negative = true;
/*  902 */       s++;
/*  903 */     } else if ((char)buf[s] == '+') {
/*  904 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  908 */     int save = s;
/*      */     
/*  910 */     short cutoff = (short)(Short.MAX_VALUE / base);
/*  911 */     short cutlim = (short)(Short.MAX_VALUE % base);
/*      */     
/*  913 */     if (negative) {
/*  914 */       cutlim = (short)(cutlim + 1);
/*      */     }
/*      */     
/*  917 */     boolean overflow = false;
/*  918 */     short i = 0;
/*  920 */     for (; 
/*  920 */         s < buf.length; s++) {
/*  921 */       char c = (char)buf[s];
/*      */       
/*  923 */       if (Character.isDigit(c)) {
/*  924 */         c = (char)(c - '0');
/*  925 */       } else { if (!Character.isLetter(c)) break;
/*  926 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  931 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  936 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  937 */         overflow = true;
/*      */       } else {
/*  939 */         i = (short)(i * base);
/*  940 */         i = (short)(i + c);
/*      */       }
/*      */     }
/*      */     
/*  944 */     if (s == save) {
/*  945 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*  948 */     if (overflow) {
/*  949 */       throw new NumberFormatException(toString(buf));
/*      */     }
/*      */     
/*      */ 
/*  953 */     return negative ? (short)-i : i;
/*      */   }
/*      */   
/*      */   public static final int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor)
/*      */   {
/*  958 */     if ((searchIn == null) || (searchFor == null) || (startingPosition > searchIn.length()))
/*      */     {
/*  960 */       return -1;
/*      */     }
/*      */     
/*  963 */     int patternLength = searchFor.length();
/*  964 */     int stringLength = searchIn.length();
/*  965 */     int stopSearchingAt = stringLength - patternLength;
/*      */     
/*  967 */     if (patternLength == 0) {
/*  968 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  973 */     char firstCharOfPatternUc = Character.toUpperCase(searchFor.charAt(0));
/*  974 */     char firstCharOfPatternLc = Character.toLowerCase(searchFor.charAt(0));
/*      */     
/*      */ 
/*  977 */     for (int i = startingPosition; i <= stopSearchingAt; i++) {
/*  978 */       if (isNotEqualIgnoreCharCase(searchIn, firstCharOfPatternUc, firstCharOfPatternLc, i)) {
/*      */         do
/*      */         {
/*  981 */           i++; } while ((i <= stopSearchingAt) && (isNotEqualIgnoreCharCase(searchIn, firstCharOfPatternUc, firstCharOfPatternLc, i)));
/*      */       }
/*      */       
/*      */ 
/*  985 */       if (i <= stopSearchingAt)
/*      */       {
/*      */ 
/*  988 */         int j = i + 1;
/*  989 */         int end = j + patternLength - 1;
/*  990 */         for (int k = 1; (j < end) && ((Character.toLowerCase(searchIn.charAt(j)) == Character.toLowerCase(searchFor.charAt(k))) || (Character.toUpperCase(searchIn.charAt(j)) == Character.toUpperCase(searchFor.charAt(k)))); 
/*      */             
/*  992 */             k++) { j++;
/*      */         }
/*  994 */         if (j == end) {
/*  995 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1000 */     return -1;
/*      */   }
/*      */   
/*      */   private static final boolean isNotEqualIgnoreCharCase(String searchIn, char firstCharOfPatternUc, char firstCharOfPatternLc, int i)
/*      */   {
/* 1005 */     return (Character.toLowerCase(searchIn.charAt(i)) != firstCharOfPatternLc) && (Character.toUpperCase(searchIn.charAt(i)) != firstCharOfPatternUc);
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
/*      */   public static final int indexOfIgnoreCase(String searchIn, String searchFor)
/*      */   {
/* 1020 */     return indexOfIgnoreCase(0, searchIn, searchFor);
/*      */   }
/*      */   
/*      */ 
/*      */   public static int indexOfIgnoreCaseRespectMarker(int startAt, String src, String target, String marker, String markerCloses, boolean allowBackslashEscapes)
/*      */   {
/* 1026 */     char contextMarker = '\000';
/* 1027 */     boolean escaped = false;
/* 1028 */     int markerTypeFound = 0;
/* 1029 */     int srcLength = src.length();
/* 1030 */     int ind = 0;
/*      */     
/* 1032 */     for (int i = startAt; i < srcLength; i++) {
/* 1033 */       char c = src.charAt(i);
/*      */       
/* 1035 */       if ((allowBackslashEscapes) && (c == '\\')) {
/* 1036 */         escaped = !escaped;
/* 1037 */       } else if ((contextMarker != 0) && (c == markerCloses.charAt(markerTypeFound)) && (!escaped)) {
/* 1038 */         contextMarker = '\000';
/* 1039 */       } else if (((ind = marker.indexOf(c)) != -1) && (!escaped) && (contextMarker == 0))
/*      */       {
/* 1041 */         markerTypeFound = ind;
/* 1042 */         contextMarker = c;
/* 1043 */       } else if (((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0))) || (Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0)))) && (!escaped) && (contextMarker == 0))
/*      */       {
/*      */ 
/* 1046 */         if (startsWithIgnoreCase(src, i, target)) {
/* 1047 */           return i;
/*      */         }
/*      */       }
/*      */     }
/* 1051 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int indexOfIgnoreCaseRespectQuotes(int startAt, String src, String target, char quoteChar, boolean allowBackslashEscapes)
/*      */   {
/* 1057 */     char contextMarker = '\000';
/* 1058 */     boolean escaped = false;
/*      */     
/* 1060 */     int srcLength = src.length();
/*      */     
/* 1062 */     for (int i = startAt; i < srcLength; i++) {
/* 1063 */       char c = src.charAt(i);
/*      */       
/* 1065 */       if ((allowBackslashEscapes) && (c == '\\')) {
/* 1066 */         escaped = !escaped;
/* 1067 */       } else if ((c == contextMarker) && (!escaped)) {
/* 1068 */         contextMarker = '\000';
/* 1069 */       } else if ((c == quoteChar) && (!escaped) && (contextMarker == 0))
/*      */       {
/* 1071 */         contextMarker = c;
/*      */ 
/*      */       }
/* 1074 */       else if (((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0))) || (Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0)))) && (!escaped) && (contextMarker == 0))
/*      */       {
/*      */ 
/* 1077 */         if (startsWithIgnoreCase(src, i, target)) {
/* 1078 */           return i;
/*      */         }
/*      */       }
/*      */     }
/* 1082 */     return -1;
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
/*      */   public static final List<String> split(String stringToSplit, String delimitter, boolean trim)
/*      */   {
/* 1103 */     if (stringToSplit == null) {
/* 1104 */       return new ArrayList();
/*      */     }
/*      */     
/* 1107 */     if (delimitter == null) {
/* 1108 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1111 */     StringTokenizer tokenizer = new StringTokenizer(stringToSplit, delimitter, false);
/*      */     
/*      */ 
/* 1114 */     List<String> splitTokens = new ArrayList(tokenizer.countTokens());
/*      */     
/* 1116 */     while (tokenizer.hasMoreTokens()) {
/* 1117 */       String token = tokenizer.nextToken();
/*      */       
/* 1119 */       if (trim) {
/* 1120 */         token = token.trim();
/*      */       }
/*      */       
/* 1123 */       splitTokens.add(token);
/*      */     }
/*      */     
/* 1126 */     return splitTokens;
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
/*      */   public static final List<String> split(String stringToSplit, String delimiter, String markers, String markerCloses, boolean trim)
/*      */   {
/* 1146 */     if (stringToSplit == null) {
/* 1147 */       return new ArrayList();
/*      */     }
/*      */     
/* 1150 */     if (delimiter == null) {
/* 1151 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1154 */     int delimPos = 0;
/* 1155 */     int currentPos = 0;
/*      */     
/* 1157 */     List<String> splitTokens = new ArrayList();
/*      */     
/*      */ 
/* 1160 */     while ((delimPos = indexOfIgnoreCaseRespectMarker(currentPos, stringToSplit, delimiter, markers, markerCloses, false)) != -1) {
/* 1161 */       String token = stringToSplit.substring(currentPos, delimPos);
/*      */       
/* 1163 */       if (trim) {
/* 1164 */         token = token.trim();
/*      */       }
/*      */       
/* 1167 */       splitTokens.add(token);
/* 1168 */       currentPos = delimPos + 1;
/*      */     }
/*      */     
/* 1171 */     if (currentPos < stringToSplit.length()) {
/* 1172 */       String token = stringToSplit.substring(currentPos);
/*      */       
/* 1174 */       if (trim) {
/* 1175 */         token = token.trim();
/*      */       }
/*      */       
/* 1178 */       splitTokens.add(token);
/*      */     }
/*      */     
/* 1181 */     return splitTokens;
/*      */   }
/*      */   
/*      */   private static boolean startsWith(byte[] dataFrom, String chars) {
/* 1185 */     for (int i = 0; i < chars.length(); i++) {
/* 1186 */       if (dataFrom[i] != chars.charAt(i)) {
/* 1187 */         return false;
/*      */       }
/*      */     }
/* 1190 */     return true;
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
/*      */   public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor)
/*      */   {
/* 1209 */     return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
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
/*      */   public static boolean startsWithIgnoreCase(String searchIn, String searchFor)
/*      */   {
/* 1225 */     return startsWithIgnoreCase(searchIn, 0, searchFor);
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
/*      */   public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String searchIn, String searchFor)
/*      */   {
/* 1242 */     if (searchIn == null) {
/* 1243 */       return searchFor == null;
/*      */     }
/*      */     
/* 1246 */     int beginPos = 0;
/*      */     
/* 1248 */     int inLength = searchIn.length();
/*      */     
/* 1250 */     for (beginPos = 0; beginPos < inLength; beginPos++) {
/* 1251 */       char c = searchIn.charAt(beginPos);
/*      */       
/* 1253 */       if (Character.isLetterOrDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1258 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
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
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor)
/*      */   {
/* 1274 */     return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
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
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos)
/*      */   {
/* 1293 */     if (searchIn == null) {
/* 1294 */       return searchFor == null;
/*      */     }
/*      */     
/* 1297 */     int inLength = searchIn.length();
/* 1299 */     for (; 
/* 1299 */         beginPos < inLength; beginPos++) {
/* 1300 */       if (!Character.isWhitespace(searchIn.charAt(beginPos))) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1305 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
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
/*      */   public static int startsWithIgnoreCaseAndWs(String searchIn, String[] searchFor)
/*      */   {
/* 1320 */     for (int i = 0; i < searchFor.length; i++) {
/* 1321 */       if (startsWithIgnoreCaseAndWs(searchIn, searchFor[i], 0)) {
/* 1322 */         return i;
/*      */       }
/*      */     }
/* 1325 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] stripEnclosure(byte[] source, String prefix, String suffix)
/*      */   {
/* 1336 */     if ((source.length >= prefix.length() + suffix.length()) && (startsWith(source, prefix)) && (endsWith(source, suffix)))
/*      */     {
/*      */ 
/* 1339 */       int totalToStrip = prefix.length() + suffix.length();
/* 1340 */       int enclosedLength = source.length - totalToStrip;
/* 1341 */       byte[] enclosed = new byte[enclosedLength];
/*      */       
/* 1343 */       int startPos = prefix.length();
/* 1344 */       int numToCopy = enclosed.length;
/* 1345 */       System.arraycopy(source, startPos, enclosed, 0, numToCopy);
/*      */       
/* 1347 */       return enclosed;
/*      */     }
/* 1349 */     return source;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String toAsciiString(byte[] buffer)
/*      */   {
/* 1361 */     return toAsciiString(buffer, 0, buffer.length);
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
/*      */   public static final String toAsciiString(byte[] buffer, int startPos, int length)
/*      */   {
/* 1378 */     char[] charArray = new char[length];
/* 1379 */     int readpoint = startPos;
/*      */     
/* 1381 */     for (int i = 0; i < length; i++) {
/* 1382 */       charArray[i] = ((char)buffer[readpoint]);
/* 1383 */       readpoint++;
/*      */     }
/*      */     
/* 1386 */     return new String(charArray);
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
/*      */   public static int wildCompare(String searchIn, String searchForWildcard)
/*      */   {
/* 1404 */     if ((searchIn == null) || (searchForWildcard == null)) {
/* 1405 */       return -1;
/*      */     }
/*      */     
/* 1408 */     if (searchForWildcard.equals("%"))
/*      */     {
/* 1410 */       return 1;
/*      */     }
/*      */     
/* 1413 */     int result = -1;
/*      */     
/* 1415 */     char wildcardMany = '%';
/* 1416 */     char wildcardOne = '_';
/* 1417 */     char wildcardEscape = '\\';
/*      */     
/* 1419 */     int searchForPos = 0;
/* 1420 */     int searchForEnd = searchForWildcard.length();
/*      */     
/* 1422 */     int searchInPos = 0;
/* 1423 */     int searchInEnd = searchIn.length();
/*      */     
/* 1425 */     while (searchForPos != searchForEnd) {
/* 1426 */       char wildstrChar = searchForWildcard.charAt(searchForPos);
/*      */       
/*      */ 
/* 1429 */       while ((searchForWildcard.charAt(searchForPos) != wildcardMany) && (wildstrChar != wildcardOne)) {
/* 1430 */         if ((searchForWildcard.charAt(searchForPos) == wildcardEscape) && (searchForPos + 1 != searchForEnd))
/*      */         {
/* 1432 */           searchForPos++;
/*      */         }
/*      */         
/* 1435 */         if ((searchInPos == searchInEnd) || (Character.toUpperCase(searchForWildcard.charAt(searchForPos++)) != Character.toUpperCase(searchIn.charAt(searchInPos++))))
/*      */         {
/*      */ 
/*      */ 
/* 1439 */           return 1;
/*      */         }
/*      */         
/* 1442 */         if (searchForPos == searchForEnd) {
/* 1443 */           return searchInPos != searchInEnd ? 1 : 0;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1450 */         result = 1;
/*      */       }
/*      */       
/* 1453 */       if (searchForWildcard.charAt(searchForPos) == wildcardOne) {
/*      */         do {
/* 1455 */           if (searchInPos == searchInEnd)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1460 */             return result;
/*      */           }
/*      */           
/* 1463 */           searchInPos++;
/*      */           
/* 1465 */           searchForPos++; } while ((searchForPos < searchForEnd) && (searchForWildcard.charAt(searchForPos) == wildcardOne));
/*      */         
/* 1467 */         if (searchForPos == searchForEnd) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/* 1472 */       if (searchForWildcard.charAt(searchForPos) == wildcardMany)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1479 */         searchForPos++;
/* 1482 */         for (; 
/*      */             
/* 1482 */             searchForPos != searchForEnd; searchForPos++) {
/* 1483 */           if (searchForWildcard.charAt(searchForPos) != wildcardMany)
/*      */           {
/*      */ 
/*      */ 
/* 1487 */             if (searchForWildcard.charAt(searchForPos) != wildcardOne) break;
/* 1488 */             if (searchInPos == searchInEnd) {
/* 1489 */               return -1;
/*      */             }
/*      */             
/* 1492 */             searchInPos++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1500 */         if (searchForPos == searchForEnd) {
/* 1501 */           return 0;
/*      */         }
/*      */         
/* 1504 */         if (searchInPos == searchInEnd) {
/* 1505 */           return -1;
/*      */         }
/*      */         char cmp;
/* 1508 */         if (((cmp = searchForWildcard.charAt(searchForPos)) == wildcardEscape) && (searchForPos + 1 != searchForEnd))
/*      */         {
/* 1510 */           cmp = searchForWildcard.charAt(++searchForPos);
/*      */         }
/*      */         
/* 1513 */         searchForPos++;
/*      */         
/*      */         do
/*      */         {
/* 1517 */           while ((searchInPos != searchInEnd) && (Character.toUpperCase(searchIn.charAt(searchInPos)) != Character.toUpperCase(cmp)))
/*      */           {
/*      */ 
/* 1520 */             searchInPos++;
/*      */           }
/* 1522 */           if (searchInPos++ == searchInEnd) {
/* 1523 */             return -1;
/*      */           }
/*      */           
/*      */ 
/* 1527 */           int tmp = wildCompare(searchIn, searchForWildcard);
/*      */           
/* 1529 */           if (tmp <= 0) {
/* 1530 */             return tmp;
/*      */           }
/*      */           
/*      */         }
/* 1534 */         while ((searchInPos != searchInEnd) && (searchForWildcard.charAt(0) != wildcardMany));
/*      */         
/* 1536 */         return -1;
/*      */       }
/*      */     }
/*      */     
/* 1540 */     return searchInPos != searchInEnd ? 1 : 0;
/*      */   }
/*      */   
/*      */   static byte[] s2b(String s, MySQLConnection conn) throws SQLException
/*      */   {
/* 1545 */     if (s == null) {
/* 1546 */       return null;
/*      */     }
/*      */     
/* 1549 */     if ((conn != null) && (conn.getUseUnicode())) {
/*      */       try {
/* 1551 */         String encoding = conn.getEncoding();
/*      */         
/* 1553 */         if (encoding == null) {
/* 1554 */           return s.getBytes();
/*      */         }
/*      */         
/* 1557 */         SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
/*      */         
/*      */ 
/* 1560 */         if (converter != null) {
/* 1561 */           return converter.toBytes(s);
/*      */         }
/*      */         
/* 1564 */         return s.getBytes(encoding);
/*      */       } catch (UnsupportedEncodingException E) {
/* 1566 */         return s.getBytes();
/*      */       }
/*      */     }
/*      */     
/* 1570 */     return s.getBytes();
/*      */   }
/*      */   
/*      */   public static int lastIndexOf(byte[] s, char c) {
/* 1574 */     if (s == null) {
/* 1575 */       return -1;
/*      */     }
/*      */     
/* 1578 */     for (int i = s.length - 1; i >= 0; i--) {
/* 1579 */       if (s[i] == c) {
/* 1580 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 1584 */     return -1;
/*      */   }
/*      */   
/*      */   public static int indexOf(byte[] s, char c) {
/* 1588 */     if (s == null) {
/* 1589 */       return -1;
/*      */     }
/*      */     
/* 1592 */     int length = s.length;
/*      */     
/* 1594 */     for (int i = 0; i < length; i++) {
/* 1595 */       if (s[i] == c) {
/* 1596 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 1600 */     return -1;
/*      */   }
/*      */   
/*      */   public static boolean isNullOrEmpty(String toTest) {
/* 1604 */     return (toTest == null) || (toTest.length() == 0);
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
/*      */   public static String stripComments(String src, String stringOpens, String stringCloses, boolean slashStarComments, boolean slashSlashComments, boolean hashComments, boolean dashDashComments)
/*      */   {
/* 1631 */     if (src == null) {
/* 1632 */       return null;
/*      */     }
/*      */     
/* 1635 */     StringBuffer buf = new StringBuffer(src.length());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1644 */     StringReader sourceReader = new StringReader(src);
/*      */     
/* 1646 */     int contextMarker = 0;
/* 1647 */     boolean escaped = false;
/* 1648 */     int markerTypeFound = -1;
/*      */     
/* 1650 */     int ind = 0;
/*      */     
/* 1652 */     int currentChar = 0;
/*      */     try
/*      */     {
/* 1655 */       while ((currentChar = sourceReader.read()) != -1)
/*      */       {
/*      */ 
/*      */ 
/* 1659 */         if ((markerTypeFound != -1) && (currentChar == stringCloses.charAt(markerTypeFound)) && (!escaped))
/*      */         {
/* 1661 */           contextMarker = 0;
/* 1662 */           markerTypeFound = -1;
/* 1663 */         } else if (((ind = stringOpens.indexOf(currentChar)) != -1) && (!escaped) && (contextMarker == 0))
/*      */         {
/* 1665 */           markerTypeFound = ind;
/* 1666 */           contextMarker = currentChar;
/*      */         }
/*      */         
/* 1669 */         if ((contextMarker == 0) && (currentChar == 47) && ((slashSlashComments) || (slashStarComments)))
/*      */         {
/* 1671 */           currentChar = sourceReader.read();
/* 1672 */           if ((currentChar == 42) && (slashStarComments)) {
/* 1673 */             int prevChar = 0;
/*      */             
/* 1675 */             while (((currentChar = sourceReader.read()) != 47) || (prevChar != 42)) {
/* 1676 */               if (currentChar == 13)
/*      */               {
/* 1678 */                 currentChar = sourceReader.read();
/* 1679 */                 if (currentChar == 10) {
/* 1680 */                   currentChar = sourceReader.read();
/*      */                 }
/*      */               }
/* 1683 */               else if (currentChar == 10)
/*      */               {
/* 1685 */                 currentChar = sourceReader.read();
/*      */               }
/*      */               
/* 1688 */               if (currentChar < 0)
/*      */                 break;
/* 1690 */               prevChar = currentChar;
/*      */             }
/*      */           }
/* 1693 */           if ((currentChar != 47) || (!slashSlashComments)) {}
/*      */         } else {
/* 1695 */           while (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0))
/*      */           {
/*      */             continue;
/* 1698 */             if ((contextMarker == 0) && (currentChar == 35) && (hashComments)) {}
/*      */             
/*      */             for (;;)
/*      */             {
/* 1702 */               if (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0)) {
/*      */                 continue;
/* 1704 */                 if ((contextMarker == 0) && (currentChar == 45) && (dashDashComments))
/*      */                 {
/* 1706 */                   currentChar = sourceReader.read();
/*      */                   
/* 1708 */                   if ((currentChar == -1) || (currentChar != 45)) {
/* 1709 */                     buf.append('-');
/*      */                     
/* 1711 */                     if (currentChar == -1) break;
/* 1712 */                     buf.append(currentChar); break;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1721 */                   while (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0)) {}
/*      */                 }
/*      */               }
/*      */             } } }
/* 1725 */         if (currentChar != -1) {
/* 1726 */           buf.append((char)currentChar);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {}
/*      */     
/*      */ 
/* 1733 */     return buf.toString();
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
/*      */   public static String sanitizeProcOrFuncName(String src)
/*      */   {
/* 1749 */     if ((src == null) || (src.equals("%"))) {
/* 1750 */       return null;
/*      */     }
/*      */     
/* 1753 */     return src;
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
/*      */   public static List<String> splitDBdotName(String src, String cat, String quotId, boolean isNoBslashEscSet)
/*      */   {
/* 1773 */     if ((src == null) || (src.equals("%"))) {
/* 1774 */       return new ArrayList();
/*      */     }
/*      */     
/* 1777 */     boolean isQuoted = indexOfIgnoreCase(0, src, quotId) > -1;
/*      */     
/*      */ 
/* 1780 */     String retval = src;
/* 1781 */     String tmpCat = cat;
/*      */     
/* 1783 */     int trueDotIndex = -1;
/* 1784 */     if (!" ".equals(quotId))
/*      */     {
/*      */ 
/* 1787 */       if (isQuoted) {
/* 1788 */         trueDotIndex = indexOfIgnoreCase(0, retval, quotId + "." + quotId);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1793 */         trueDotIndex = indexOfIgnoreCase(0, retval, ".");
/*      */       }
/*      */     }
/*      */     else {
/* 1797 */       trueDotIndex = retval.indexOf(".");
/*      */     }
/*      */     
/* 1800 */     List<String> retTokens = new ArrayList(2);
/*      */     
/* 1802 */     if (trueDotIndex != -1)
/*      */     {
/* 1804 */       if (isQuoted) {
/* 1805 */         tmpCat = toString(stripEnclosure(retval.substring(0, trueDotIndex + 1).getBytes(), quotId, quotId));
/*      */         
/* 1807 */         if (startsWithIgnoreCaseAndWs(tmpCat, quotId)) {
/* 1808 */           tmpCat = tmpCat.substring(1, tmpCat.length() - 1);
/*      */         }
/*      */         
/* 1811 */         retval = retval.substring(trueDotIndex + 2);
/* 1812 */         retval = toString(stripEnclosure(retval.getBytes(), quotId, quotId));
/*      */       }
/*      */       else
/*      */       {
/* 1816 */         tmpCat = retval.substring(0, trueDotIndex);
/* 1817 */         retval = retval.substring(trueDotIndex + 1);
/*      */       }
/*      */     }
/*      */     else {
/* 1821 */       retval = toString(stripEnclosure(retval.getBytes(), quotId, quotId));
/*      */     }
/*      */     
/*      */ 
/* 1825 */     retTokens.add(tmpCat);
/* 1826 */     retTokens.add(retval);
/* 1827 */     return retTokens;
/*      */   }
/*      */   
/*      */   public static final boolean isEmptyOrWhitespaceOnly(String str) {
/* 1831 */     if ((str == null) || (str.length() == 0)) {
/* 1832 */       return true;
/*      */     }
/*      */     
/* 1835 */     int length = str.length();
/*      */     
/* 1837 */     for (int i = 0; i < length; i++) {
/* 1838 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 1839 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1843 */     return true;
/*      */   }
/*      */   
/*      */   public static String escapeQuote(String src, String quotChar) {
/* 1847 */     if (src == null) {
/* 1848 */       return null;
/*      */     }
/*      */     
/* 1851 */     src = toString(stripEnclosure(src.getBytes(), quotChar, quotChar));
/*      */     
/* 1853 */     int lastNdx = src.indexOf(quotChar);
/*      */     
/*      */ 
/*      */ 
/* 1857 */     String tmpSrc = src.substring(0, lastNdx);
/* 1858 */     tmpSrc = tmpSrc + quotChar + quotChar;
/*      */     
/* 1860 */     String tmpRest = src.substring(lastNdx + 1, src.length());
/*      */     
/* 1862 */     lastNdx = tmpRest.indexOf(quotChar);
/* 1863 */     while (lastNdx > -1)
/*      */     {
/* 1865 */       tmpSrc = tmpSrc + tmpRest.substring(0, lastNdx);
/* 1866 */       tmpSrc = tmpSrc + quotChar + quotChar;
/* 1867 */       tmpRest = tmpRest.substring(lastNdx + 1, tmpRest.length());
/*      */       
/* 1869 */       lastNdx = tmpRest.indexOf(quotChar);
/*      */     }
/*      */     
/* 1872 */     tmpSrc = tmpSrc + tmpRest;
/* 1873 */     src = tmpSrc;
/*      */     
/* 1875 */     return src;
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
/*      */   public static String quoteIdentifier(String identifier, String quoteChar, boolean isPedantic)
/*      */   {
/* 1902 */     if (identifier == null) {
/* 1903 */       return null;
/*      */     }
/*      */     
/* 1906 */     if ((!isPedantic) && (identifier.startsWith(quoteChar)) && (identifier.endsWith(quoteChar))) {
/* 1907 */       return identifier;
/*      */     }
/*      */     
/* 1910 */     return quoteChar + identifier.replaceAll(quoteChar, new StringBuilder().append(quoteChar).append(quoteChar).toString()) + quoteChar;
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
/*      */   public static String quoteIdentifier(String identifier, boolean isPedantic)
/*      */   {
/* 1929 */     return quoteIdentifier(identifier, "`", isPedantic);
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
/*      */   public static String unQuoteIdentifier(String identifier, boolean useAnsiQuotedIdentifiers)
/*      */   {
/* 1951 */     if (identifier == null) {
/* 1952 */       return null;
/*      */     }
/*      */     
/* 1955 */     identifier = identifier.trim();
/*      */     
/* 1957 */     String quoteChar = null;
/*      */     
/*      */ 
/* 1960 */     if ((identifier.startsWith("`")) && (identifier.endsWith("`"))) {
/* 1961 */       quoteChar = "`";
/*      */     }
/*      */     
/* 1964 */     if ((quoteChar == null) && (useAnsiQuotedIdentifiers) && 
/* 1965 */       (identifier.startsWith("\"")) && (identifier.endsWith("\""))) {
/* 1966 */       quoteChar = "\"";
/*      */     }
/*      */     
/*      */ 
/* 1970 */     if (quoteChar != null) {
/* 1971 */       identifier = identifier.substring(1, identifier.length() - 1);
/* 1972 */       return identifier.replaceAll(quoteChar + quoteChar, quoteChar);
/*      */     }
/*      */     
/* 1975 */     return identifier;
/*      */   }
/*      */   
/*      */   public static int indexOfQuoteDoubleAware(String line, String quoteChar, int startFrom) {
/* 1979 */     int lastIndex = line.length() - 1;
/*      */     
/* 1981 */     int beginPos = startFrom;
/* 1982 */     int pos = -1;
/*      */     
/* 1984 */     boolean next = true;
/* 1985 */     while (next) {
/* 1986 */       pos = line.indexOf(quoteChar, beginPos);
/* 1987 */       if ((pos == -1) || (pos == lastIndex) || (!line.substring(pos + 1).startsWith(quoteChar))) {
/* 1988 */         next = false;
/*      */       } else {
/* 1990 */         beginPos = pos + 2;
/*      */       }
/*      */     }
/*      */     
/* 1994 */     return pos;
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
/*      */   public static String toString(byte[] value, int offset, int length, String encoding)
/*      */     throws UnsupportedEncodingException
/*      */   {
/* 2008 */     Charset cs = findCharset(encoding);
/*      */     
/* 2010 */     return cs.decode(ByteBuffer.wrap(value, offset, length)).toString();
/*      */   }
/*      */   
/*      */   public static String toString(byte[] value, String encoding) throws UnsupportedEncodingException
/*      */   {
/* 2015 */     Charset cs = findCharset(encoding);
/*      */     
/* 2017 */     return cs.decode(ByteBuffer.wrap(value)).toString();
/*      */   }
/*      */   
/*      */   public static String toString(byte[] value, int offset, int length) {
/*      */     try {
/* 2022 */       Charset cs = findCharset(platformEncoding);
/*      */       
/* 2024 */       return cs.decode(ByteBuffer.wrap(value, offset, length)).toString();
/*      */     }
/*      */     catch (UnsupportedEncodingException e) {}
/*      */     
/*      */ 
/* 2029 */     return null;
/*      */   }
/*      */   
/*      */   public static String toString(byte[] value) {
/*      */     try {
/* 2034 */       Charset cs = findCharset(platformEncoding);
/*      */       
/* 2036 */       return cs.decode(ByteBuffer.wrap(value)).toString();
/*      */     }
/*      */     catch (UnsupportedEncodingException e) {}
/*      */     
/*      */ 
/* 2041 */     return null;
/*      */   }
/*      */   
/*      */   public static byte[] getBytes(String value, String encoding) throws UnsupportedEncodingException
/*      */   {
/* 2046 */     Charset cs = findCharset(encoding);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2051 */     ByteBuffer buf = cs.encode(value);
/*      */     
/* 2053 */     int encodedLen = buf.limit();
/* 2054 */     byte[] asBytes = new byte[encodedLen];
/* 2055 */     buf.get(asBytes, 0, encodedLen);
/*      */     
/* 2057 */     return asBytes;
/*      */   }
/*      */   
/*      */   public static byte[] getBytes(String value) {
/*      */     try {
/* 2062 */       Charset cs = findCharset(platformEncoding);
/*      */       
/* 2064 */       ByteBuffer buf = cs.encode(value);
/*      */       
/* 2066 */       int encodedLen = buf.limit();
/* 2067 */       byte[] asBytes = new byte[encodedLen];
/* 2068 */       buf.get(asBytes, 0, encodedLen);
/*      */       
/* 2070 */       return asBytes;
/*      */     }
/*      */     catch (UnsupportedEncodingException e) {}
/*      */     
/*      */ 
/* 2075 */     return null;
/*      */   }
/*      */   
/*      */   public static final boolean isValidIdChar(char c) {
/* 2079 */     return "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@".indexOf(c) != -1;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\StringUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */