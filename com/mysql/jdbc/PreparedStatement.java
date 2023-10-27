/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PreparedStatement
/*      */   extends StatementImpl
/*      */   implements java.sql.PreparedStatement
/*      */ {
/*      */   private static final Constructor<?> JDBC_4_PSTMT_2_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_PSTMT_3_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_PSTMT_4_ARG_CTOR;
/*      */   
/*      */   static
/*      */   {
/*   96 */     if (Util.isJdbc4()) {
/*      */       try {
/*   98 */         JDBC_4_PSTMT_2_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class });
/*      */         
/*      */ 
/*      */ 
/*  102 */         JDBC_4_PSTMT_3_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class });
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  107 */         JDBC_4_PSTMT_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, ParseInfo.class });
/*      */ 
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*      */ 
/*  113 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  115 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  117 */         throw new RuntimeException(e);
/*      */       }
/*      */     } else {
/*  120 */       JDBC_4_PSTMT_2_ARG_CTOR = null;
/*  121 */       JDBC_4_PSTMT_3_ARG_CTOR = null;
/*  122 */       JDBC_4_PSTMT_4_ARG_CTOR = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public class BatchParams {
/*  127 */     public boolean[] isNull = null;
/*      */     
/*  129 */     public boolean[] isStream = null;
/*      */     
/*  131 */     public InputStream[] parameterStreams = null;
/*      */     
/*  133 */     public byte[][] parameterStrings = (byte[][])null;
/*      */     
/*  135 */     public int[] streamLengths = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags)
/*      */     {
/*  142 */       this.parameterStrings = new byte[strings.length][];
/*  143 */       this.parameterStreams = new InputStream[streams.length];
/*  144 */       this.isStream = new boolean[isStreamFlags.length];
/*  145 */       this.streamLengths = new int[lengths.length];
/*  146 */       this.isNull = new boolean[isNullFlags.length];
/*  147 */       System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
/*      */       
/*  149 */       System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
/*      */       
/*  151 */       System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
/*      */       
/*  153 */       System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
/*  154 */       System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   class EndPoint
/*      */   {
/*      */     int begin;
/*      */     int end;
/*      */     
/*      */     EndPoint(int b, int e)
/*      */     {
/*  166 */       this.begin = b;
/*  167 */       this.end = e;
/*      */     }
/*      */   }
/*      */   
/*      */   class ParseInfo {
/*  172 */     char firstStmtChar = '\000';
/*      */     
/*  174 */     boolean foundLimitClause = false;
/*      */     
/*  176 */     boolean foundLoadData = false;
/*      */     
/*  178 */     long lastUsed = 0L;
/*      */     
/*  180 */     int statementLength = 0;
/*      */     
/*  182 */     int statementStartPos = 0;
/*      */     
/*  184 */     boolean canRewriteAsMultiValueInsert = false;
/*      */     
/*  186 */     byte[][] staticSql = (byte[][])null;
/*      */     
/*  188 */     boolean isOnDuplicateKeyUpdate = false;
/*      */     
/*  190 */     int locationOfOnDuplicateKeyUpdate = -1;
/*      */     
/*      */     String valuesClause;
/*      */     
/*  194 */     boolean parametersInDuplicateKeyClause = false;
/*      */     
/*      */     private ParseInfo batchHead;
/*      */     
/*      */     private ParseInfo batchValues;
/*      */     
/*      */     private ParseInfo batchODKUClause;
/*      */     
/*      */     ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter)
/*      */       throws SQLException
/*      */     {
/*  205 */       this(sql, conn, dbmd, encoding, converter, true);
/*      */     }
/*      */     
/*      */     public ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo) throws SQLException
/*      */     {
/*      */       try
/*      */       {
/*  212 */         if (sql == null) {
/*  213 */           throw SQLError.createSQLException(Messages.getString("PreparedStatement.61"), "S1009", PreparedStatement.this.getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  218 */         this.locationOfOnDuplicateKeyUpdate = PreparedStatement.this.getOnDuplicateKeyLocation(sql);
/*  219 */         this.isOnDuplicateKeyUpdate = (this.locationOfOnDuplicateKeyUpdate != -1);
/*      */         
/*  221 */         this.lastUsed = System.currentTimeMillis();
/*      */         
/*  223 */         quotedIdentifierString = dbmd.getIdentifierQuoteString();
/*      */         
/*  225 */         char quotedIdentifierChar = '\000';
/*      */         
/*  227 */         if ((quotedIdentifierString != null) && (!quotedIdentifierString.equals(" ")) && (quotedIdentifierString.length() > 0))
/*      */         {
/*      */ 
/*  230 */           quotedIdentifierChar = quotedIdentifierString.charAt(0);
/*      */         }
/*      */         
/*  233 */         this.statementLength = sql.length();
/*      */         
/*  235 */         ArrayList<int[]> endpointList = new ArrayList();
/*  236 */         boolean inQuotes = false;
/*  237 */         char quoteChar = '\000';
/*  238 */         boolean inQuotedId = false;
/*  239 */         int lastParmEnd = 0;
/*      */         
/*      */ 
/*  242 */         int stopLookingForLimitClause = this.statementLength - 5;
/*      */         
/*  244 */         this.foundLimitClause = false;
/*      */         
/*  246 */         boolean noBackslashEscapes = PreparedStatement.this.connection.isNoBackslashEscapesSet();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  252 */         this.statementStartPos = PreparedStatement.this.findStartOfStatement(sql);
/*      */         
/*  254 */         for (int i = this.statementStartPos; i < this.statementLength; i++) {
/*  255 */           char c = sql.charAt(i);
/*      */           
/*  257 */           if ((this.firstStmtChar == 0) && (Character.isLetter(c)))
/*      */           {
/*      */ 
/*  260 */             this.firstStmtChar = Character.toUpperCase(c);
/*      */           }
/*      */           
/*  263 */           if ((!noBackslashEscapes) && (c == '\\') && (i < this.statementLength - 1))
/*      */           {
/*  265 */             i++;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  271 */             if ((!inQuotes) && (quotedIdentifierChar != 0) && (c == quotedIdentifierChar))
/*      */             {
/*  273 */               inQuotedId = !inQuotedId;
/*  274 */             } else if (!inQuotedId)
/*      */             {
/*      */ 
/*  277 */               if (inQuotes) {
/*  278 */                 if (((c == '\'') || (c == '"')) && (c == quoteChar)) {
/*  279 */                   if ((i < this.statementLength - 1) && (sql.charAt(i + 1) == quoteChar)) {
/*  280 */                     i++;
/*  281 */                     continue;
/*      */                   }
/*      */                   
/*  284 */                   inQuotes = !inQuotes;
/*  285 */                   quoteChar = '\000';
/*  286 */                 } else if (((c == '\'') || (c == '"')) && (c == quoteChar)) {
/*  287 */                   inQuotes = !inQuotes;
/*  288 */                   quoteChar = '\000';
/*      */                 }
/*      */               } else {
/*  291 */                 if ((c == '#') || ((c == '-') && (i + 1 < this.statementLength) && (sql.charAt(i + 1) == '-')))
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*  296 */                   int endOfStmt = this.statementLength - 1;
/*  298 */                   for (; 
/*  298 */                       i < endOfStmt; i++) {
/*  299 */                     c = sql.charAt(i);
/*      */                     
/*  301 */                     if ((c == '\r') || (c == '\n')) {
/*      */                       break;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/*  307 */                 if ((c == '/') && (i + 1 < this.statementLength))
/*      */                 {
/*  309 */                   char cNext = sql.charAt(i + 1);
/*      */                   
/*  311 */                   if (cNext == '*') {
/*  312 */                     i += 2;
/*      */                     
/*  314 */                     for (int j = i; j < this.statementLength; j++) {
/*  315 */                       i++;
/*  316 */                       cNext = sql.charAt(j);
/*      */                       
/*  318 */                       if ((cNext == '*') && (j + 1 < this.statementLength) && 
/*  319 */                         (sql.charAt(j + 1) == '/')) {
/*  320 */                         i++;
/*      */                         
/*  322 */                         if (i >= this.statementLength) break;
/*  323 */                         c = sql.charAt(i); break;
/*      */                       }
/*      */                       
/*      */                     }
/*      */                     
/*      */                   }
/*      */                   
/*      */                 }
/*  331 */                 else if ((c == '\'') || (c == '"')) {
/*  332 */                   inQuotes = true;
/*  333 */                   quoteChar = c;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  338 */             if ((c == '?') && (!inQuotes) && (!inQuotedId)) {
/*  339 */               endpointList.add(new int[] { lastParmEnd, i });
/*  340 */               lastParmEnd = i + 1;
/*      */               
/*  342 */               if ((this.isOnDuplicateKeyUpdate) && (i > this.locationOfOnDuplicateKeyUpdate)) {
/*  343 */                 this.parametersInDuplicateKeyClause = true;
/*      */               }
/*      */             }
/*      */             
/*  347 */             if ((!inQuotes) && (!inQuotedId) && (i < stopLookingForLimitClause) && (
/*  348 */               (c == 'L') || (c == 'l'))) {
/*  349 */               char posI1 = sql.charAt(i + 1);
/*      */               
/*  351 */               if ((posI1 == 'I') || (posI1 == 'i')) {
/*  352 */                 char posM = sql.charAt(i + 2);
/*      */                 
/*  354 */                 if ((posM == 'M') || (posM == 'm')) {
/*  355 */                   char posI2 = sql.charAt(i + 3);
/*      */                   
/*  357 */                   if ((posI2 == 'I') || (posI2 == 'i')) {
/*  358 */                     char posT = sql.charAt(i + 4);
/*      */                     
/*  360 */                     if ((posT == 'T') || (posT == 't'))
/*      */                     {
/*  362 */                       boolean hasPreviosIdChar = false;
/*  363 */                       boolean hasFollowingIdChar = false;
/*  364 */                       if ((i > this.statementStartPos) && (StringUtils.isValidIdChar(sql.charAt(i - 1)))) {
/*  365 */                         hasPreviosIdChar = true;
/*      */                       }
/*  367 */                       if ((i + 5 < this.statementLength) && (StringUtils.isValidIdChar(sql.charAt(i + 5)))) {
/*  368 */                         hasFollowingIdChar = true;
/*      */                       }
/*  370 */                       if ((!hasPreviosIdChar) && (!hasFollowingIdChar)) {
/*  371 */                         this.foundLimitClause = true;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  381 */         if (this.firstStmtChar == 'L') {
/*  382 */           if (StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA")) {
/*  383 */             this.foundLoadData = true;
/*      */           } else {
/*  385 */             this.foundLoadData = false;
/*      */           }
/*      */         } else {
/*  388 */           this.foundLoadData = false;
/*      */         }
/*      */         
/*  391 */         endpointList.add(new int[] { lastParmEnd, this.statementLength });
/*  392 */         this.staticSql = new byte[endpointList.size()][];
/*  393 */         char[] asCharArray = sql.toCharArray();
/*      */         
/*  395 */         for (i = 0; i < this.staticSql.length; i++) {
/*  396 */           int[] ep = (int[])endpointList.get(i);
/*  397 */           int end = ep[1];
/*  398 */           int begin = ep[0];
/*  399 */           int len = end - begin;
/*      */           
/*  401 */           if (this.foundLoadData) {
/*  402 */             String temp = new String(asCharArray, begin, len);
/*  403 */             this.staticSql[i] = StringUtils.getBytes(temp);
/*  404 */           } else if (encoding == null) {
/*  405 */             byte[] buf = new byte[len];
/*      */             
/*  407 */             for (int j = 0; j < len; j++) {
/*  408 */               buf[j] = ((byte)sql.charAt(begin + j));
/*      */             }
/*      */             
/*  411 */             this.staticSql[i] = buf;
/*      */           }
/*  413 */           else if (converter != null) {
/*  414 */             this.staticSql[i] = StringUtils.getBytes(sql, converter, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), begin, len, PreparedStatement.this.connection.parserKnowsUnicode(), PreparedStatement.this.getExceptionInterceptor());
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  419 */             String temp = new String(asCharArray, begin, len);
/*      */             
/*  421 */             this.staticSql[i] = StringUtils.getBytes(temp, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), PreparedStatement.this.connection.parserKnowsUnicode(), conn, PreparedStatement.this.getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (StringIndexOutOfBoundsException oobEx)
/*      */       {
/*      */         String quotedIdentifierString;
/*      */         
/*  429 */         SQLException sqlEx = new SQLException("Parse error for " + sql);
/*  430 */         sqlEx.initCause(oobEx);
/*      */         
/*  432 */         throw sqlEx;
/*      */       }
/*      */       
/*      */ 
/*  436 */       if (buildRewriteInfo) {
/*  437 */         this.canRewriteAsMultiValueInsert = ((PreparedStatement.canRewrite(sql, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementStartPos)) && (!this.parametersInDuplicateKeyClause));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  442 */         if ((this.canRewriteAsMultiValueInsert) && (conn.getRewriteBatchedStatements()))
/*      */         {
/*  444 */           buildRewriteBatchedParams(sql, conn, dbmd, encoding, converter);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void buildRewriteBatchedParams(String sql, MySQLConnection conn, DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter)
/*      */       throws SQLException
/*      */     {
/*  459 */       this.valuesClause = extractValuesClause(sql);
/*  460 */       String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
/*      */       
/*      */ 
/*  463 */       String headSql = null;
/*      */       
/*  465 */       if (this.isOnDuplicateKeyUpdate) {
/*  466 */         headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
/*      */       } else {
/*  468 */         headSql = sql;
/*      */       }
/*      */       
/*  471 */       this.batchHead = new ParseInfo(PreparedStatement.this, headSql, conn, metadata, encoding, converter, false);
/*      */       
/*  473 */       this.batchValues = new ParseInfo(PreparedStatement.this, "," + this.valuesClause, conn, metadata, encoding, converter, false);
/*      */       
/*  475 */       this.batchODKUClause = null;
/*      */       
/*  477 */       if ((odkuClause != null) && (odkuClause.length() > 0)) {
/*  478 */         this.batchODKUClause = new ParseInfo(PreparedStatement.this, "," + this.valuesClause + " " + odkuClause, conn, metadata, encoding, converter, false);
/*      */       }
/*      */     }
/*      */     
/*      */     private String extractValuesClause(String sql)
/*      */       throws SQLException
/*      */     {
/*  485 */       String quoteCharStr = PreparedStatement.this.connection.getMetaData().getIdentifierQuoteString();
/*      */       
/*      */ 
/*  488 */       int indexOfValues = -1;
/*  489 */       int valuesSearchStart = this.statementStartPos;
/*      */       
/*  491 */       while (indexOfValues == -1) {
/*  492 */         if (quoteCharStr.length() > 0) {
/*  493 */           indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES", quoteCharStr.charAt(0), false);
/*      */         }
/*      */         else
/*      */         {
/*  497 */           indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  502 */         if (indexOfValues <= 0)
/*      */           break;
/*  504 */         char c = PreparedStatement.this.originalSql.charAt(indexOfValues - 1);
/*  505 */         if ((!Character.isWhitespace(c)) && (c != ')') && (c != '`')) {
/*  506 */           valuesSearchStart = indexOfValues + 6;
/*  507 */           indexOfValues = -1;
/*      */         }
/*      */         else {
/*  510 */           c = PreparedStatement.this.originalSql.charAt(indexOfValues + 6);
/*  511 */           if ((!Character.isWhitespace(c)) && (c != '(')) {
/*  512 */             valuesSearchStart = indexOfValues + 6;
/*  513 */             indexOfValues = -1;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  521 */       if (indexOfValues == -1) {
/*  522 */         return null;
/*      */       }
/*      */       
/*  525 */       int indexOfFirstParen = sql.indexOf('(', indexOfValues + 6);
/*      */       
/*  527 */       if (indexOfFirstParen == -1) {
/*  528 */         return null;
/*      */       }
/*      */       
/*  531 */       int endOfValuesClause = sql.lastIndexOf(')');
/*      */       
/*  533 */       if (endOfValuesClause == -1) {
/*  534 */         return null;
/*      */       }
/*      */       
/*  537 */       if (this.isOnDuplicateKeyUpdate) {
/*  538 */         endOfValuesClause = this.locationOfOnDuplicateKeyUpdate - 1;
/*      */       }
/*      */       
/*  541 */       return sql.substring(indexOfFirstParen, endOfValuesClause + 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     synchronized ParseInfo getParseInfoForBatch(int numBatch)
/*      */     {
/*  548 */       PreparedStatement.AppendingBatchVisitor apv = new PreparedStatement.AppendingBatchVisitor(PreparedStatement.this);
/*  549 */       buildInfoForBatch(numBatch, apv);
/*      */       
/*  551 */       ParseInfo batchParseInfo = new ParseInfo(PreparedStatement.this, apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLimitClause, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  557 */       return batchParseInfo;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     String getSqlForBatch(int numBatch)
/*      */       throws UnsupportedEncodingException
/*      */     {
/*  566 */       ParseInfo batchInfo = getParseInfoForBatch(numBatch);
/*      */       
/*  568 */       return getSqlForBatch(batchInfo);
/*      */     }
/*      */     
/*      */ 
/*      */     String getSqlForBatch(ParseInfo batchInfo)
/*      */       throws UnsupportedEncodingException
/*      */     {
/*  575 */       int size = 0;
/*  576 */       byte[][] sqlStrings = batchInfo.staticSql;
/*  577 */       int sqlStringsLength = sqlStrings.length;
/*      */       
/*  579 */       for (int i = 0; i < sqlStringsLength; i++) {
/*  580 */         size += sqlStrings[i].length;
/*  581 */         size++;
/*      */       }
/*      */       
/*  584 */       StringBuffer buf = new StringBuffer(size);
/*      */       
/*  586 */       for (int i = 0; i < sqlStringsLength - 1; i++) {
/*  587 */         buf.append(StringUtils.toString(sqlStrings[i], PreparedStatement.this.charEncoding));
/*  588 */         buf.append("?");
/*      */       }
/*      */       
/*  591 */       buf.append(StringUtils.toString(sqlStrings[(sqlStringsLength - 1)]));
/*      */       
/*  593 */       return buf.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void buildInfoForBatch(int numBatch, PreparedStatement.BatchVisitor visitor)
/*      */     {
/*  605 */       byte[][] headStaticSql = this.batchHead.staticSql;
/*  606 */       int headStaticSqlLength = headStaticSql.length;
/*      */       
/*  608 */       if (headStaticSqlLength > 1) {
/*  609 */         for (int i = 0; i < headStaticSqlLength - 1; i++) {
/*  610 */           visitor.append(headStaticSql[i]).increment();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  615 */       byte[] endOfHead = headStaticSql[(headStaticSqlLength - 1)];
/*  616 */       byte[][] valuesStaticSql = this.batchValues.staticSql;
/*  617 */       byte[] beginOfValues = valuesStaticSql[0];
/*      */       
/*  619 */       visitor.merge(endOfHead, beginOfValues).increment();
/*      */       
/*  621 */       int numValueRepeats = numBatch - 1;
/*      */       
/*  623 */       if (this.batchODKUClause != null) {
/*  624 */         numValueRepeats--;
/*      */       }
/*      */       
/*  627 */       int valuesStaticSqlLength = valuesStaticSql.length;
/*  628 */       byte[] endOfValues = valuesStaticSql[(valuesStaticSqlLength - 1)];
/*      */       
/*  630 */       for (int i = 0; i < numValueRepeats; i++) {
/*  631 */         for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
/*  632 */           visitor.append(valuesStaticSql[j]).increment();
/*      */         }
/*  634 */         visitor.merge(endOfValues, beginOfValues).increment();
/*      */       }
/*      */       
/*  637 */       if (this.batchODKUClause != null) {
/*  638 */         byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
/*  639 */         byte[] beginOfOdku = batchOdkuStaticSql[0];
/*  640 */         visitor.decrement().merge(endOfValues, beginOfOdku).increment();
/*      */         
/*  642 */         int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
/*      */         
/*  644 */         if (numBatch > 1) {
/*  645 */           for (int i = 1; i < batchOdkuStaticSqlLength; i++) {
/*  646 */             visitor.append(batchOdkuStaticSql[i]).increment();
/*      */           }
/*      */           
/*      */         } else {
/*  650 */           visitor.decrement().append(batchOdkuStaticSql[(batchOdkuStaticSqlLength - 1)]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  655 */         visitor.decrement().append(this.staticSql[(this.staticSql.length - 1)]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLimitClause, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos)
/*      */     {
/*  664 */       this.firstStmtChar = firstStmtChar;
/*  665 */       this.foundLimitClause = foundLimitClause;
/*  666 */       this.foundLoadData = foundLoadData;
/*  667 */       this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
/*  668 */       this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
/*  669 */       this.statementLength = statementLength;
/*  670 */       this.statementStartPos = statementStartPos;
/*  671 */       this.staticSql = staticSql;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract interface BatchVisitor { public abstract BatchVisitor increment();
/*      */     
/*      */     public abstract BatchVisitor decrement();
/*      */     
/*      */     public abstract BatchVisitor append(byte[] paramArrayOfByte);
/*      */     
/*      */     public abstract BatchVisitor merge(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */   }
/*      */   
/*      */   class AppendingBatchVisitor implements PreparedStatement.BatchVisitor { AppendingBatchVisitor() {}
/*      */     
/*  686 */     LinkedList<byte[]> statementComponents = new LinkedList();
/*      */     
/*      */     public PreparedStatement.BatchVisitor append(byte[] values) {
/*  689 */       this.statementComponents.addLast(values);
/*      */       
/*  691 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor increment()
/*      */     {
/*  696 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor decrement() {
/*  700 */       this.statementComponents.removeLast();
/*      */       
/*  702 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor merge(byte[] front, byte[] back) {
/*  706 */       int mergedLength = front.length + back.length;
/*  707 */       byte[] merged = new byte[mergedLength];
/*  708 */       System.arraycopy(front, 0, merged, 0, front.length);
/*  709 */       System.arraycopy(back, 0, merged, front.length, back.length);
/*  710 */       this.statementComponents.addLast(merged);
/*  711 */       return this;
/*      */     }
/*      */     
/*      */     public byte[][] getStaticSqlStrings() {
/*  715 */       byte[][] asBytes = new byte[this.statementComponents.size()][];
/*  716 */       this.statementComponents.toArray(asBytes);
/*      */       
/*  718 */       return asBytes;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  722 */       StringBuffer buf = new StringBuffer();
/*  723 */       Iterator<byte[]> iter = this.statementComponents.iterator();
/*  724 */       while (iter.hasNext()) {
/*  725 */         buf.append(StringUtils.toString((byte[])iter.next()));
/*      */       }
/*      */       
/*  728 */       return buf.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  733 */   private static final byte[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static int readFully(Reader reader, char[] buf, int length)
/*      */     throws IOException
/*      */   {
/*  756 */     int numCharsRead = 0;
/*      */     
/*  758 */     while (numCharsRead < length) {
/*  759 */       int count = reader.read(buf, numCharsRead, length - numCharsRead);
/*      */       
/*  761 */       if (count < 0) {
/*      */         break;
/*      */       }
/*      */       
/*  765 */       numCharsRead += count;
/*      */     }
/*      */     
/*  768 */     return numCharsRead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  777 */   protected boolean batchHasPlainStatements = false;
/*      */   
/*  779 */   private DatabaseMetaData dbmd = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  785 */   protected char firstCharOfStmt = '\000';
/*      */   
/*      */ 
/*  788 */   protected boolean hasLimitClause = false;
/*      */   
/*      */ 
/*  791 */   protected boolean isLoadDataQuery = false;
/*      */   
/*  793 */   protected boolean[] isNull = null;
/*      */   
/*  795 */   private boolean[] isStream = null;
/*      */   
/*  797 */   protected int numberOfExecutions = 0;
/*      */   
/*      */ 
/*  800 */   protected String originalSql = null;
/*      */   
/*      */ 
/*      */   protected int parameterCount;
/*      */   
/*      */   protected MysqlParameterMetadata parameterMetaData;
/*      */   
/*  807 */   private InputStream[] parameterStreams = null;
/*      */   
/*  809 */   private byte[][] parameterValues = (byte[][])null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  815 */   protected int[] parameterTypes = null;
/*      */   
/*      */   protected ParseInfo parseInfo;
/*      */   
/*      */   private java.sql.ResultSetMetaData pstmtResultMetaData;
/*      */   
/*  821 */   private byte[][] staticSqlStrings = (byte[][])null;
/*      */   
/*  823 */   private byte[] streamConvertBuf = null;
/*      */   
/*  825 */   private int[] streamLengths = null;
/*      */   
/*  827 */   private SimpleDateFormat tsdf = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  832 */   protected boolean useTrueBoolean = false;
/*      */   
/*      */   protected boolean usingAnsiMode;
/*      */   
/*      */   protected String batchedValuesClause;
/*      */   
/*      */   private boolean doPingInstead;
/*      */   
/*      */   private SimpleDateFormat ddf;
/*      */   private SimpleDateFormat tdf;
/*  842 */   private boolean compensateForOnDuplicateKeyUpdate = false;
/*      */   
/*      */ 
/*      */   private CharsetEncoder charsetEncoder;
/*      */   
/*      */ 
/*  848 */   protected int batchCommandIndex = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean serverSupportsFracSecs;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String catalog)
/*      */     throws SQLException
/*      */   {
/*  861 */     if (!Util.isJdbc4()) {
/*  862 */       return new PreparedStatement(conn, catalog);
/*      */     }
/*      */     
/*  865 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[] { conn, catalog }, conn.getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog)
/*      */     throws SQLException
/*      */   {
/*  878 */     if (!Util.isJdbc4()) {
/*  879 */       return new PreparedStatement(conn, sql, catalog);
/*      */     }
/*      */     
/*  882 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[] { conn, sql, catalog }, conn.getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo)
/*      */     throws SQLException
/*      */   {
/*  895 */     if (!Util.isJdbc4()) {
/*  896 */       return new PreparedStatement(conn, sql, catalog, cachedParseInfo);
/*      */     }
/*      */     
/*  899 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[] { conn, sql, catalog, cachedParseInfo }, conn.getExceptionInterceptor());
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
/*      */   public PreparedStatement(MySQLConnection conn, String catalog)
/*      */     throws SQLException
/*      */   {
/*  917 */     super(conn, catalog);
/*      */     
/*  919 */     detectFractionalSecondsSupport();
/*  920 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */   }
/*      */   
/*      */   protected void detectFractionalSecondsSupport() throws SQLException {
/*  924 */     this.serverSupportsFracSecs = ((this.connection != null) && (this.connection.versionMeetsMinimum(5, 6, 4)));
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
/*      */   public PreparedStatement(MySQLConnection conn, String sql, String catalog)
/*      */     throws SQLException
/*      */   {
/*  943 */     super(conn, catalog);
/*      */     
/*  945 */     if (sql == null) {
/*  946 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  950 */     detectFractionalSecondsSupport();
/*  951 */     this.originalSql = sql;
/*      */     
/*  953 */     if (this.originalSql.startsWith("/* ping */")) {
/*  954 */       this.doPingInstead = true;
/*      */     } else {
/*  956 */       this.doPingInstead = false;
/*      */     }
/*      */     
/*  959 */     this.dbmd = this.connection.getMetaData();
/*      */     
/*  961 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/*  963 */     this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
/*      */     
/*      */ 
/*  966 */     initializeFromParseInfo();
/*      */     
/*  968 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */     
/*  970 */     if (conn.getRequiresEscapingEncoder()) {
/*  971 */       this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
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
/*      */   public PreparedStatement(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo)
/*      */     throws SQLException
/*      */   {
/*  991 */     super(conn, catalog);
/*      */     
/*  993 */     if (sql == null) {
/*  994 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  998 */     detectFractionalSecondsSupport();
/*  999 */     this.originalSql = sql;
/*      */     
/* 1001 */     this.dbmd = this.connection.getMetaData();
/*      */     
/* 1003 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/* 1005 */     this.parseInfo = cachedParseInfo;
/*      */     
/* 1007 */     this.usingAnsiMode = (!this.connection.useAnsiQuotedIdentifiers());
/*      */     
/* 1009 */     initializeFromParseInfo();
/*      */     
/* 1011 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */     
/* 1013 */     if (conn.getRequiresEscapingEncoder()) {
/* 1014 */       this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBatch()
/*      */     throws SQLException
/*      */   {
/* 1026 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1027 */       if (this.batchedArgs == null) {
/* 1028 */         this.batchedArgs = new ArrayList();
/*      */       }
/*      */       
/* 1031 */       for (int i = 0; i < this.parameterValues.length; i++) {
/* 1032 */         checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
/*      */       }
/*      */       
/*      */ 
/* 1036 */       this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
/*      */     }
/*      */   }
/*      */   
/*      */   public void addBatch(String sql)
/*      */     throws SQLException
/*      */   {
/* 1043 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1044 */       this.batchHasPlainStatements = true;
/*      */       
/* 1046 */       super.addBatch(sql);
/*      */     }
/*      */   }
/*      */   
/*      */   public String asSql() throws SQLException {
/* 1051 */     return asSql(false);
/*      */   }
/*      */   
/*      */   public String asSql(boolean quoteStreamsAndUnknowns) throws SQLException {
/* 1055 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1057 */       StringBuffer buf = new StringBuffer();
/*      */       try
/*      */       {
/* 1060 */         int realParameterCount = this.parameterCount + getParameterIndexOffset();
/* 1061 */         Object batchArg = null;
/* 1062 */         if (this.batchCommandIndex != -1) {
/* 1063 */           batchArg = this.batchedArgs.get(this.batchCommandIndex);
/*      */         }
/* 1065 */         for (int i = 0; i < realParameterCount; i++) {
/* 1066 */           if (this.charEncoding != null) {
/* 1067 */             buf.append(StringUtils.toString(this.staticSqlStrings[i], this.charEncoding));
/*      */           }
/*      */           else {
/* 1070 */             buf.append(StringUtils.toString(this.staticSqlStrings[i]));
/*      */           }
/*      */           
/* 1073 */           byte[] val = null;
/* 1074 */           if ((batchArg != null) && ((batchArg instanceof String))) {
/* 1075 */             buf.append((String)batchArg);
/*      */           }
/*      */           else {
/* 1078 */             if (this.batchCommandIndex == -1) {
/* 1079 */               val = this.parameterValues[i];
/*      */             } else {
/* 1081 */               val = ((BatchParams)batchArg).parameterStrings[i];
/*      */             }
/* 1083 */             boolean isStreamParam = false;
/* 1084 */             if (this.batchCommandIndex == -1) {
/* 1085 */               isStreamParam = this.isStream[i];
/*      */             } else {
/* 1087 */               isStreamParam = ((BatchParams)batchArg).isStream[i];
/*      */             }
/* 1089 */             if ((val == null) && (!isStreamParam)) {
/* 1090 */               if (quoteStreamsAndUnknowns) {
/* 1091 */                 buf.append("'");
/*      */               }
/*      */               
/* 1094 */               buf.append("** NOT SPECIFIED **");
/*      */               
/* 1096 */               if (quoteStreamsAndUnknowns) {
/* 1097 */                 buf.append("'");
/*      */               }
/* 1099 */             } else if (isStreamParam) {
/* 1100 */               if (quoteStreamsAndUnknowns) {
/* 1101 */                 buf.append("'");
/*      */               }
/*      */               
/* 1104 */               buf.append("** STREAM DATA **");
/*      */               
/* 1106 */               if (quoteStreamsAndUnknowns) {
/* 1107 */                 buf.append("'");
/*      */               }
/*      */             }
/* 1110 */             else if (this.charConverter != null) {
/* 1111 */               buf.append(this.charConverter.toString(val));
/*      */             }
/* 1113 */             else if (this.charEncoding != null) {
/* 1114 */               buf.append(new String(val, this.charEncoding));
/*      */             } else {
/* 1116 */               buf.append(StringUtils.toAsciiString(val));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1122 */         if (this.charEncoding != null) {
/* 1123 */           buf.append(StringUtils.toString(this.staticSqlStrings[(this.parameterCount + getParameterIndexOffset())], this.charEncoding));
/*      */         }
/*      */         else
/*      */         {
/* 1127 */           buf.append(StringUtils.toAsciiString(this.staticSqlStrings[(this.parameterCount + getParameterIndexOffset())]));
/*      */         }
/*      */       }
/*      */       catch (UnsupportedEncodingException uue)
/*      */       {
/* 1132 */         throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1138 */       return buf.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearBatch() throws SQLException {
/* 1143 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1144 */       this.batchHasPlainStatements = false;
/*      */       
/* 1146 */       super.clearBatch();
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
/*      */   public void clearParameters()
/*      */     throws SQLException
/*      */   {
/* 1161 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1163 */       for (int i = 0; i < this.parameterValues.length; i++) {
/* 1164 */         this.parameterValues[i] = null;
/* 1165 */         this.parameterStreams[i] = null;
/* 1166 */         this.isStream[i] = false;
/* 1167 */         this.isNull[i] = false;
/* 1168 */         this.parameterTypes[i] = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException
/*      */   {
/* 1175 */     int lastwritten = 0;
/*      */     
/* 1177 */     for (int i = 0; i < size; i++) {
/* 1178 */       byte b = buf[i];
/*      */       
/* 1180 */       if (b == 0)
/*      */       {
/* 1182 */         if (i > lastwritten) {
/* 1183 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/* 1187 */         packet.writeByte((byte)92);
/* 1188 */         packet.writeByte((byte)48);
/* 1189 */         lastwritten = i + 1;
/*      */       }
/* 1191 */       else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
/*      */       {
/*      */ 
/* 1194 */         if (i > lastwritten) {
/* 1195 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1200 */         packet.writeByte((byte)92);
/* 1201 */         lastwritten = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1207 */     if (lastwritten < size) {
/* 1208 */       packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size)
/*      */   {
/* 1214 */     int lastwritten = 0;
/*      */     
/* 1216 */     for (int i = 0; i < size; i++) {
/* 1217 */       byte b = buf[i];
/*      */       
/* 1219 */       if (b == 0)
/*      */       {
/* 1221 */         if (i > lastwritten) {
/* 1222 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/* 1226 */         bytesOut.write(92);
/* 1227 */         bytesOut.write(48);
/* 1228 */         lastwritten = i + 1;
/*      */       }
/* 1230 */       else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
/*      */       {
/*      */ 
/* 1233 */         if (i > lastwritten) {
/* 1234 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/* 1238 */         bytesOut.write(92);
/* 1239 */         lastwritten = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1245 */     if (lastwritten < size) {
/* 1246 */       bytesOut.write(buf, lastwritten, size - lastwritten);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkReadOnlySafeStatement()
/*      */     throws SQLException
/*      */   {
/* 1257 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1258 */       return (!this.connection.isReadOnly()) || (this.firstCharOfStmt == 'S');
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
/*      */   public boolean execute()
/*      */     throws SQLException
/*      */   {
/* 1274 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1276 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1278 */       if (!checkReadOnlySafeStatement()) {
/* 1279 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1284 */       ResultSetInternalMethods rs = null;
/*      */       
/* 1286 */       CachedResultSetMetaData cachedMetadata = null;
/*      */       
/* 1288 */       this.lastQueryIsOnDupKeyUpdate = false;
/*      */       
/* 1290 */       if (this.retrieveGeneratedKeys) {
/* 1291 */         this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL();
/*      */       }
/*      */       
/* 1294 */       boolean doStreaming = createStreamingResultSet();
/*      */       
/* 1296 */       clearWarnings();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1306 */       if ((doStreaming) && (this.connection.getNetTimeoutForStreamingResults() > 0))
/*      */       {
/* 1308 */         executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1314 */       this.batchedGeneratedKeys = null;
/*      */       
/* 1316 */       Buffer sendPacket = fillSendPacket();
/*      */       
/* 1318 */       String oldCatalog = null;
/*      */       
/* 1320 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1321 */         oldCatalog = locallyScopedConn.getCatalog();
/* 1322 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1328 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1329 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/* 1332 */       Field[] metadataFromCache = null;
/*      */       
/* 1334 */       if (cachedMetadata != null) {
/* 1335 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/* 1338 */       boolean oldInfoMsgState = false;
/*      */       
/* 1340 */       if (this.retrieveGeneratedKeys) {
/* 1341 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/* 1342 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1354 */       if (locallyScopedConn.useMaxRows()) {
/* 1355 */         int rowLimit = -1;
/*      */         
/* 1357 */         if (this.firstCharOfStmt == 'S') {
/* 1358 */           if (this.hasLimitClause) {
/* 1359 */             rowLimit = this.maxRows;
/*      */           }
/* 1361 */           else if (this.maxRows <= 0) {
/* 1362 */             executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */           }
/*      */           else {
/* 1365 */             executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/* 1370 */           executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1375 */         rs = executeInternal(rowLimit, sendPacket, doStreaming, this.firstCharOfStmt == 'S', metadataFromCache, false);
/*      */       }
/*      */       else
/*      */       {
/* 1379 */         rs = executeInternal(-1, sendPacket, doStreaming, this.firstCharOfStmt == 'S', metadataFromCache, false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1384 */       if (cachedMetadata != null) {
/* 1385 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, rs);
/*      */ 
/*      */       }
/* 1388 */       else if ((rs.reallyResult()) && (locallyScopedConn.getCacheResultSetMetadata())) {
/* 1389 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, rs);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1394 */       if (this.retrieveGeneratedKeys) {
/* 1395 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/* 1396 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       }
/*      */       
/* 1399 */       if (oldCatalog != null) {
/* 1400 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 1403 */       if (rs != null) {
/* 1404 */         this.lastInsertId = rs.getUpdateID();
/*      */         
/* 1406 */         this.results = rs;
/*      */       }
/*      */       
/* 1409 */       return (rs != null) && (rs.reallyResult());
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
/*      */   public int[] executeBatch()
/*      */     throws SQLException
/*      */   {
/* 1428 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1430 */       if (this.connection.isReadOnly()) {
/* 1431 */         throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1436 */       if ((this.batchedArgs == null) || (this.batchedArgs.size() == 0)) {
/* 1437 */         return new int[0];
/*      */       }
/*      */       
/*      */ 
/* 1441 */       int batchTimeout = this.timeoutInMillis;
/* 1442 */       this.timeoutInMillis = 0;
/*      */       
/* 1444 */       resetCancelledState();
/*      */       try
/*      */       {
/* 1447 */         statementBegins();
/*      */         
/* 1449 */         clearWarnings();
/*      */         
/* 1451 */         if ((!this.batchHasPlainStatements) && (this.connection.getRewriteBatchedStatements()))
/*      */         {
/*      */ 
/*      */ 
/* 1455 */           if (canRewriteAsMultiValueInsertAtSqlLevel()) {
/* 1456 */             arrayOfInt = executeBatchedInserts(batchTimeout);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1469 */             this.statementExecuting.set(false);
/*      */             
/* 1471 */             clearBatch();return arrayOfInt;
/*      */           }
/* 1459 */           if ((this.connection.versionMeetsMinimum(4, 1, 0)) && (!this.batchHasPlainStatements) && (this.batchedArgs != null) && (this.batchedArgs.size() > 3))
/*      */           {
/*      */ 
/*      */ 
/* 1463 */             arrayOfInt = executePreparedBatchAsMultiStatement(batchTimeout);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1469 */             this.statementExecuting.set(false);
/*      */             
/* 1471 */             clearBatch();return arrayOfInt;
/*      */           }
/*      */         }
/* 1467 */         int[] arrayOfInt = executeBatchSerially(batchTimeout);
/*      */         
/* 1469 */         this.statementExecuting.set(false);
/*      */         
/* 1471 */         clearBatch();return arrayOfInt;
/*      */       }
/*      */       finally
/*      */       {
/* 1469 */         this.statementExecuting.set(false);
/*      */         
/* 1471 */         clearBatch();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
/* 1477 */     return this.parseInfo.canRewriteAsMultiValueInsert;
/*      */   }
/*      */   
/*      */   protected int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
/* 1481 */     return this.parseInfo.locationOfOnDuplicateKeyUpdate;
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
/*      */   protected int[] executePreparedBatchAsMultiStatement(int batchTimeout)
/*      */     throws SQLException
/*      */   {
/* 1495 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1497 */       if (this.batchedValuesClause == null) {
/* 1498 */         this.batchedValuesClause = (this.originalSql + ";");
/*      */       }
/*      */       
/* 1501 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1503 */       boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries();
/* 1504 */       StatementImpl.CancelTask timeoutTask = null;
/*      */       try
/*      */       {
/* 1507 */         clearWarnings();
/*      */         
/* 1509 */         int numBatchedArgs = this.batchedArgs.size();
/*      */         
/* 1511 */         if (this.retrieveGeneratedKeys) {
/* 1512 */           this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
/*      */         }
/*      */         
/* 1515 */         int numValuesPerBatch = computeBatchSize(numBatchedArgs);
/*      */         
/* 1517 */         if (numBatchedArgs < numValuesPerBatch) {
/* 1518 */           numValuesPerBatch = numBatchedArgs;
/*      */         }
/*      */         
/* 1521 */         java.sql.PreparedStatement batchedStatement = null;
/*      */         
/* 1523 */         int batchedParamIndex = 1;
/* 1524 */         int numberToExecuteAsMultiValue = 0;
/* 1525 */         int batchCounter = 0;
/* 1526 */         int updateCountCounter = 0;
/* 1527 */         int[] updateCounts = new int[numBatchedArgs];
/* 1528 */         SQLException sqlEx = null;
/*      */         try
/*      */         {
/* 1531 */           if (!multiQueriesEnabled) {
/* 1532 */             locallyScopedConn.getIO().enableMultiQueries();
/*      */           }
/*      */           
/* 1535 */           if (this.retrieveGeneratedKeys) {
/* 1536 */             batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch), 1);
/*      */           }
/*      */           else
/*      */           {
/* 1540 */             batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch));
/*      */           }
/*      */           
/*      */ 
/* 1544 */           if ((locallyScopedConn.getEnableQueryTimeouts()) && (batchTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/* 1547 */             timeoutTask = new StatementImpl.CancelTask(this, (StatementImpl)batchedStatement);
/* 1548 */             locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */           }
/*      */           
/*      */ 
/* 1552 */           if (numBatchedArgs < numValuesPerBatch) {
/* 1553 */             numberToExecuteAsMultiValue = numBatchedArgs;
/*      */           } else {
/* 1555 */             numberToExecuteAsMultiValue = numBatchedArgs / numValuesPerBatch;
/*      */           }
/*      */           
/* 1558 */           int numberArgsToExecute = numberToExecuteAsMultiValue * numValuesPerBatch;
/*      */           
/* 1560 */           for (int i = 0; i < numberArgsToExecute; i++) {
/* 1561 */             if ((i != 0) && (i % numValuesPerBatch == 0)) {
/*      */               try {
/* 1563 */                 batchedStatement.execute();
/*      */               } catch (SQLException ex) {
/* 1565 */                 sqlEx = handleExceptionForBatch(batchCounter, numValuesPerBatch, updateCounts, ex);
/*      */               }
/*      */               
/*      */ 
/* 1569 */               updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */               
/*      */ 
/*      */ 
/* 1573 */               batchedStatement.clearParameters();
/* 1574 */               batchedParamIndex = 1;
/*      */             }
/*      */             
/* 1577 */             batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */           }
/*      */           
/*      */ 
/*      */           try
/*      */           {
/* 1583 */             batchedStatement.execute();
/*      */           } catch (SQLException ex) {
/* 1585 */             sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */           }
/*      */           
/*      */ 
/* 1589 */           updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */           
/*      */ 
/*      */ 
/* 1593 */           batchedStatement.clearParameters();
/*      */           
/* 1595 */           numValuesPerBatch = numBatchedArgs - batchCounter;
/*      */         } finally {
/* 1597 */           if (batchedStatement != null) {
/* 1598 */             batchedStatement.close();
/* 1599 */             batchedStatement = null;
/*      */           }
/*      */         }
/*      */         try
/*      */         {
/* 1604 */           if (numValuesPerBatch > 0)
/*      */           {
/* 1606 */             if (this.retrieveGeneratedKeys) {
/* 1607 */               batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch), 1);
/*      */             }
/*      */             else
/*      */             {
/* 1611 */               batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch));
/*      */             }
/*      */             
/*      */ 
/* 1615 */             if (timeoutTask != null) {
/* 1616 */               timeoutTask.toCancel = ((StatementImpl)batchedStatement);
/*      */             }
/*      */             
/* 1619 */             batchedParamIndex = 1;
/*      */             
/* 1621 */             while (batchCounter < numBatchedArgs) {
/* 1622 */               batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */             }
/*      */             
/*      */ 
/*      */             try
/*      */             {
/* 1628 */               batchedStatement.execute();
/*      */             } catch (SQLException ex) {
/* 1630 */               sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */             }
/*      */             
/*      */ 
/* 1634 */             updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */             
/*      */ 
/*      */ 
/* 1638 */             batchedStatement.clearParameters();
/*      */           }
/*      */           
/* 1641 */           if (timeoutTask != null) {
/* 1642 */             if (timeoutTask.caughtWhileCancelling != null) {
/* 1643 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/* 1646 */             timeoutTask.cancel();
/*      */             
/* 1648 */             locallyScopedConn.getCancelTimer().purge();
/*      */             
/* 1650 */             timeoutTask = null;
/*      */           }
/*      */           
/* 1653 */           if (sqlEx != null) {
/* 1654 */             batchUpdateException = new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */             
/*      */ 
/* 1657 */             batchUpdateException.initCause(sqlEx);
/* 1658 */             throw batchUpdateException;
/*      */           }
/*      */           
/* 1661 */           SQLException batchUpdateException = updateCounts;
/*      */           
/* 1663 */           if (batchedStatement != null) {
/* 1664 */             batchedStatement.close();
/*      */           }
/*      */           
/*      */ 
/* 1668 */           if (timeoutTask != null) {
/* 1669 */             timeoutTask.cancel();
/* 1670 */             locallyScopedConn.getCancelTimer().purge();
/*      */           }
/*      */           
/* 1673 */           resetCancelledState();
/*      */           
/* 1675 */           if (!multiQueriesEnabled) {
/* 1676 */             locallyScopedConn.getIO().disableMultiQueries();
/*      */           }
/*      */           
/* 1679 */           clearBatch();return batchUpdateException;
/*      */         }
/*      */         finally
/*      */         {
/* 1663 */           if (batchedStatement != null) {
/* 1664 */             batchedStatement.close();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1681 */         localObject4 = finally;
/*      */       }
/*      */       finally
/*      */       {
/* 1668 */         if (timeoutTask != null) {
/* 1669 */           timeoutTask.cancel();
/* 1670 */           locallyScopedConn.getCancelTimer().purge();
/*      */         }
/*      */         
/* 1673 */         resetCancelledState();
/*      */         
/* 1675 */         if (!multiQueriesEnabled) {
/* 1676 */           locallyScopedConn.getIO().disableMultiQueries();
/*      */         }
/*      */         
/* 1679 */         clearBatch();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String generateMultiStatementForBatch(int numBatches) throws SQLException {
/* 1685 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1686 */       StringBuffer newStatementSql = new StringBuffer((this.originalSql.length() + 1) * numBatches);
/*      */       
/*      */ 
/* 1689 */       newStatementSql.append(this.originalSql);
/*      */       
/* 1691 */       for (int i = 0; i < numBatches - 1; i++) {
/* 1692 */         newStatementSql.append(';');
/* 1693 */         newStatementSql.append(this.originalSql);
/*      */       }
/*      */       
/* 1696 */       return newStatementSql.toString();
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
/*      */   protected int[] executeBatchedInserts(int batchTimeout)
/*      */     throws SQLException
/*      */   {
/* 1710 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1711 */       String valuesClause = getValuesClause();
/*      */       
/* 1713 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1715 */       if (valuesClause == null) {
/* 1716 */         return executeBatchSerially(batchTimeout);
/*      */       }
/*      */       
/* 1719 */       int numBatchedArgs = this.batchedArgs.size();
/*      */       
/* 1721 */       if (this.retrieveGeneratedKeys) {
/* 1722 */         this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
/*      */       }
/*      */       
/* 1725 */       int numValuesPerBatch = computeBatchSize(numBatchedArgs);
/*      */       
/* 1727 */       if (numBatchedArgs < numValuesPerBatch) {
/* 1728 */         numValuesPerBatch = numBatchedArgs;
/*      */       }
/*      */       
/* 1731 */       java.sql.PreparedStatement batchedStatement = null;
/*      */       
/* 1733 */       int batchedParamIndex = 1;
/* 1734 */       int updateCountRunningTotal = 0;
/* 1735 */       int numberToExecuteAsMultiValue = 0;
/* 1736 */       int batchCounter = 0;
/* 1737 */       StatementImpl.CancelTask timeoutTask = null;
/* 1738 */       SQLException sqlEx = null;
/*      */       
/* 1740 */       int[] updateCounts = new int[numBatchedArgs];
/*      */       try
/*      */       {
/*      */         try {
/* 1744 */           batchedStatement = prepareBatchedInsertSQL(locallyScopedConn, numValuesPerBatch);
/*      */           
/*      */ 
/* 1747 */           if ((locallyScopedConn.getEnableQueryTimeouts()) && (batchTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/* 1750 */             timeoutTask = new StatementImpl.CancelTask(this, (StatementImpl)batchedStatement);
/*      */             
/* 1752 */             locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */           }
/*      */           
/*      */ 
/* 1756 */           if (numBatchedArgs < numValuesPerBatch) {
/* 1757 */             numberToExecuteAsMultiValue = numBatchedArgs;
/*      */           } else {
/* 1759 */             numberToExecuteAsMultiValue = numBatchedArgs / numValuesPerBatch;
/*      */           }
/*      */           
/*      */ 
/* 1763 */           int numberArgsToExecute = numberToExecuteAsMultiValue * numValuesPerBatch;
/*      */           
/*      */ 
/* 1766 */           for (int i = 0; i < numberArgsToExecute; i++) {
/* 1767 */             if ((i != 0) && (i % numValuesPerBatch == 0)) {
/*      */               try {
/* 1769 */                 updateCountRunningTotal += batchedStatement.executeUpdate();
/*      */               }
/*      */               catch (SQLException ex) {
/* 1772 */                 sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */               }
/*      */               
/*      */ 
/* 1776 */               getBatchedGeneratedKeys(batchedStatement);
/* 1777 */               batchedStatement.clearParameters();
/* 1778 */               batchedParamIndex = 1;
/*      */             }
/*      */             
/*      */ 
/* 1782 */             batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */           }
/*      */           
/*      */ 
/*      */           try
/*      */           {
/* 1788 */             updateCountRunningTotal += batchedStatement.executeUpdate();
/*      */           }
/*      */           catch (SQLException ex) {
/* 1791 */             sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */           }
/*      */           
/*      */ 
/* 1795 */           getBatchedGeneratedKeys(batchedStatement);
/*      */           
/* 1797 */           numValuesPerBatch = numBatchedArgs - batchCounter;
/*      */         } finally {
/* 1799 */           if (batchedStatement != null) {
/* 1800 */             batchedStatement.close();
/* 1801 */             batchedStatement = null;
/*      */           }
/*      */         }
/*      */         try
/*      */         {
/* 1806 */           if (numValuesPerBatch > 0) {
/* 1807 */             batchedStatement = prepareBatchedInsertSQL(locallyScopedConn, numValuesPerBatch);
/*      */             
/*      */ 
/*      */ 
/* 1811 */             if (timeoutTask != null) {
/* 1812 */               timeoutTask.toCancel = ((StatementImpl)batchedStatement);
/*      */             }
/*      */             
/* 1815 */             batchedParamIndex = 1;
/*      */             
/* 1817 */             while (batchCounter < numBatchedArgs) {
/* 1818 */               batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */             }
/*      */             
/*      */ 
/*      */             try
/*      */             {
/* 1824 */               updateCountRunningTotal += batchedStatement.executeUpdate();
/*      */             } catch (SQLException ex) {
/* 1826 */               sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */             }
/*      */             
/*      */ 
/* 1830 */             getBatchedGeneratedKeys(batchedStatement);
/*      */           }
/*      */           
/* 1833 */           if (sqlEx != null) {
/* 1834 */             SQLException batchUpdateException = new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */             
/*      */ 
/* 1837 */             batchUpdateException.initCause(sqlEx);
/* 1838 */             throw batchUpdateException;
/*      */           }
/*      */           
/* 1841 */           for (int j = 0; j < this.batchedArgs.size(); j++) {
/* 1842 */             updateCounts[j] = updateCountRunningTotal;
/*      */           }
/* 1844 */           j = updateCounts;
/*      */           
/* 1846 */           if (batchedStatement != null) {
/* 1847 */             batchedStatement.close();
/*      */           }
/*      */           
/*      */ 
/* 1851 */           if (timeoutTask != null) {
/* 1852 */             timeoutTask.cancel();
/* 1853 */             locallyScopedConn.getCancelTimer().purge();
/*      */           }
/*      */           
/* 1856 */           resetCancelledState();return j;
/*      */         }
/*      */         finally
/*      */         {
/* 1846 */           if (batchedStatement != null) {
/* 1847 */             batchedStatement.close();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1858 */         localObject4 = finally;
/*      */       }
/*      */       finally
/*      */       {
/* 1851 */         if (timeoutTask != null) {
/* 1852 */           timeoutTask.cancel();
/* 1853 */           locallyScopedConn.getCancelTimer().purge();
/*      */         }
/*      */         
/* 1856 */         resetCancelledState();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected String getValuesClause() throws SQLException {
/* 1862 */     return this.parseInfo.valuesClause;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int computeBatchSize(int numBatchedArgs)
/*      */     throws SQLException
/*      */   {
/* 1874 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1875 */       long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
/*      */       
/* 1877 */       long maxSizeOfParameterSet = combinedValues[0];
/* 1878 */       long sizeOfEntireBatch = combinedValues[1];
/*      */       
/* 1880 */       int maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */       
/* 1882 */       if (sizeOfEntireBatch < maxAllowedPacket - this.originalSql.length()) {
/* 1883 */         return numBatchedArgs;
/*      */       }
/*      */       
/* 1886 */       return (int)Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs)
/*      */     throws SQLException
/*      */   {
/* 1896 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1897 */       long sizeOfEntireBatch = 0L;
/* 1898 */       long maxSizeOfParameterSet = 0L;
/*      */       
/* 1900 */       for (int i = 0; i < numBatchedArgs; i++) {
/* 1901 */         BatchParams paramArg = (BatchParams)this.batchedArgs.get(i);
/*      */         
/*      */ 
/* 1904 */         boolean[] isNullBatch = paramArg.isNull;
/* 1905 */         boolean[] isStreamBatch = paramArg.isStream;
/*      */         
/* 1907 */         long sizeOfParameterSet = 0L;
/*      */         
/* 1909 */         for (int j = 0; j < isNullBatch.length; j++) {
/* 1910 */           if (isNullBatch[j] == 0)
/*      */           {
/* 1912 */             if (isStreamBatch[j] != 0) {
/* 1913 */               int streamLength = paramArg.streamLengths[j];
/*      */               
/* 1915 */               if (streamLength != -1) {
/* 1916 */                 sizeOfParameterSet += streamLength * 2;
/*      */               } else {
/* 1918 */                 int paramLength = paramArg.parameterStrings[j].length;
/* 1919 */                 sizeOfParameterSet += paramLength;
/*      */               }
/*      */             } else {
/* 1922 */               sizeOfParameterSet += paramArg.parameterStrings[j].length;
/*      */             }
/*      */           } else {
/* 1925 */             sizeOfParameterSet += 4L;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1937 */         if (getValuesClause() != null) {
/* 1938 */           sizeOfParameterSet += getValuesClause().length() + 1;
/*      */         } else {
/* 1940 */           sizeOfParameterSet += this.originalSql.length() + 1;
/*      */         }
/*      */         
/* 1943 */         sizeOfEntireBatch += sizeOfParameterSet;
/*      */         
/* 1945 */         if (sizeOfParameterSet > maxSizeOfParameterSet) {
/* 1946 */           maxSizeOfParameterSet = sizeOfParameterSet;
/*      */         }
/*      */       }
/*      */       
/* 1950 */       return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
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
/*      */   protected int[] executeBatchSerially(int batchTimeout)
/*      */     throws SQLException
/*      */   {
/* 1964 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1965 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1967 */       if (locallyScopedConn == null) {
/* 1968 */         checkClosed();
/*      */       }
/*      */       
/* 1971 */       int[] updateCounts = null;
/*      */       
/* 1973 */       if (this.batchedArgs != null) {
/* 1974 */         int nbrCommands = this.batchedArgs.size();
/* 1975 */         updateCounts = new int[nbrCommands];
/*      */         
/* 1977 */         for (int i = 0; i < nbrCommands; i++) {
/* 1978 */           updateCounts[i] = -3;
/*      */         }
/*      */         
/* 1981 */         SQLException sqlEx = null;
/*      */         
/* 1983 */         StatementImpl.CancelTask timeoutTask = null;
/*      */         try
/*      */         {
/* 1986 */           if ((locallyScopedConn.getEnableQueryTimeouts()) && (batchTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/* 1989 */             timeoutTask = new StatementImpl.CancelTask(this, this);
/* 1990 */             locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */           }
/*      */           
/*      */ 
/* 1994 */           if (this.retrieveGeneratedKeys) {
/* 1995 */             this.batchedGeneratedKeys = new ArrayList(nbrCommands);
/*      */           }
/*      */           
/* 1998 */           for (this.batchCommandIndex = 0; this.batchCommandIndex < nbrCommands; this.batchCommandIndex += 1) {
/* 1999 */             Object arg = this.batchedArgs.get(this.batchCommandIndex);
/*      */             
/* 2001 */             if ((arg instanceof String)) {
/* 2002 */               updateCounts[this.batchCommandIndex] = executeUpdate((String)arg);
/*      */             } else {
/* 2004 */               BatchParams paramArg = (BatchParams)arg;
/*      */               try
/*      */               {
/* 2007 */                 updateCounts[this.batchCommandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 2012 */                 if (this.retrieveGeneratedKeys) {
/* 2013 */                   ResultSet rs = null;
/*      */                   try
/*      */                   {
/* 2016 */                     if (containsOnDuplicateKeyUpdateInSQL()) {
/* 2017 */                       rs = getGeneratedKeysInternal(1);
/*      */                     } else {
/* 2019 */                       rs = getGeneratedKeysInternal();
/*      */                     }
/* 2021 */                     while (rs.next()) {
/* 2022 */                       this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor()));
/*      */                     }
/*      */                   }
/*      */                   finally {
/* 2026 */                     if (rs != null) {
/* 2027 */                       rs.close();
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } catch (SQLException ex) {
/* 2032 */                 updateCounts[this.batchCommandIndex] = -3;
/*      */                 
/* 2034 */                 if ((this.continueBatchOnError) && (!(ex instanceof MySQLTimeoutException)) && (!(ex instanceof MySQLStatementCancelledException)) && (!hasDeadlockOrTimeoutRolledBackTx(ex)))
/*      */                 {
/*      */ 
/*      */ 
/* 2038 */                   sqlEx = ex;
/*      */                 } else {
/* 2040 */                   int[] newUpdateCounts = new int[this.batchCommandIndex];
/* 2041 */                   System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
/*      */                   
/*      */ 
/* 2044 */                   SQLException batchUpdateException = new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */                   
/*      */ 
/* 2047 */                   batchUpdateException.initCause(ex);
/* 2048 */                   throw batchUpdateException;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 2054 */           if (sqlEx != null) {
/* 2055 */             SQLException batchUpdateException = new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */             
/* 2057 */             batchUpdateException.initCause(sqlEx);
/* 2058 */             throw batchUpdateException;
/*      */           }
/*      */         } catch (NullPointerException npe) {
/*      */           try {
/* 2062 */             checkClosed();
/*      */           } catch (SQLException connectionClosedEx) {
/* 2064 */             updateCounts[this.batchCommandIndex] = -3;
/*      */             
/* 2066 */             int[] newUpdateCounts = new int[this.batchCommandIndex];
/*      */             
/* 2068 */             System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
/*      */             
/*      */ 
/* 2071 */             throw new BatchUpdateException(connectionClosedEx.getMessage(), connectionClosedEx.getSQLState(), connectionClosedEx.getErrorCode(), newUpdateCounts);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2076 */           throw npe;
/*      */         } finally {
/* 2078 */           this.batchCommandIndex = -1;
/*      */           
/* 2080 */           if (timeoutTask != null) {
/* 2081 */             timeoutTask.cancel();
/* 2082 */             locallyScopedConn.getCancelTimer().purge();
/*      */           }
/*      */           
/* 2085 */           resetCancelledState();
/*      */         }
/*      */       }
/*      */       
/* 2089 */       return updateCounts != null ? updateCounts : new int[0];
/*      */     }
/*      */   }
/*      */   
/*      */   public String getDateTime(String pattern)
/*      */   {
/* 2095 */     SimpleDateFormat sdf = new SimpleDateFormat(pattern);
/* 2096 */     return sdf.format(new java.util.Date());
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
/*      */   protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/* 2124 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*      */       try {
/* 2127 */         resetCancelledState();
/*      */         
/* 2129 */         MySQLConnection locallyScopedConnection = this.connection;
/*      */         
/* 2131 */         this.numberOfExecutions += 1;
/*      */         
/* 2133 */         if (this.doPingInstead) {
/* 2134 */           doPingInstead();
/*      */           
/* 2136 */           return this.results;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2141 */         StatementImpl.CancelTask timeoutTask = null;
/*      */         ResultSetInternalMethods rs;
/*      */         try {
/* 2144 */           if ((locallyScopedConnection.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConnection.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/* 2147 */             timeoutTask = new StatementImpl.CancelTask(this, this);
/* 2148 */             locallyScopedConnection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */           }
/*      */           
/*      */ 
/* 2152 */           if (!isBatch) {
/* 2153 */             statementBegins();
/*      */           }
/*      */           
/* 2156 */           rs = locallyScopedConnection.execSQL(this, null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, metadataFromCache, isBatch);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2161 */           if (timeoutTask != null) {
/* 2162 */             timeoutTask.cancel();
/*      */             
/* 2164 */             locallyScopedConnection.getCancelTimer().purge();
/*      */             
/* 2166 */             if (timeoutTask.caughtWhileCancelling != null) {
/* 2167 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/* 2170 */             timeoutTask = null;
/*      */           }
/*      */           
/* 2173 */           synchronized (this.cancelTimeoutMutex) {
/* 2174 */             if (this.wasCancelled) {
/* 2175 */               SQLException cause = null;
/*      */               
/* 2177 */               if (this.wasCancelledByTimeout) {
/* 2178 */                 cause = new MySQLTimeoutException();
/*      */               } else {
/* 2180 */                 cause = new MySQLStatementCancelledException();
/*      */               }
/*      */               
/* 2183 */               resetCancelledState();
/*      */               
/* 2185 */               throw cause;
/*      */             }
/*      */           }
/*      */         } finally {
/* 2189 */           if (!isBatch) {
/* 2190 */             this.statementExecuting.set(false);
/*      */           }
/*      */           
/* 2193 */           if (timeoutTask != null) {
/* 2194 */             timeoutTask.cancel();
/* 2195 */             locallyScopedConnection.getCancelTimer().purge();
/*      */           }
/*      */         }
/*      */         
/* 2199 */         return rs;
/*      */       } catch (NullPointerException npe) {
/* 2201 */         checkClosed();
/*      */         
/*      */ 
/*      */ 
/* 2205 */         throw npe;
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
/*      */   public ResultSet executeQuery()
/*      */     throws SQLException
/*      */   {
/* 2220 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 2222 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 2224 */       checkForDml(this.originalSql, this.firstCharOfStmt);
/*      */       
/* 2226 */       CachedResultSetMetaData cachedMetadata = null;
/*      */       
/*      */ 
/* 2229 */       clearWarnings();
/*      */       
/* 2231 */       boolean doStreaming = createStreamingResultSet();
/*      */       
/* 2233 */       this.batchedGeneratedKeys = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2243 */       if ((doStreaming) && (this.connection.getNetTimeoutForStreamingResults() > 0))
/*      */       {
/*      */ 
/* 2246 */         Statement stmt = null;
/*      */         try
/*      */         {
/* 2249 */           stmt = this.connection.createStatement();
/*      */           
/* 2251 */           ((StatementImpl)stmt).executeSimpleNonQuery(this.connection, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
/*      */         }
/*      */         finally {
/* 2254 */           if (stmt != null) {
/* 2255 */             stmt.close();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2260 */       Buffer sendPacket = fillSendPacket();
/*      */       
/* 2262 */       implicitlyCloseAllOpenResults();
/*      */       
/* 2264 */       String oldCatalog = null;
/*      */       
/* 2266 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 2267 */         oldCatalog = locallyScopedConn.getCatalog();
/* 2268 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2274 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 2275 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/* 2278 */       Field[] metadataFromCache = null;
/*      */       
/* 2280 */       if (cachedMetadata != null) {
/* 2281 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/* 2284 */       if (locallyScopedConn.useMaxRows())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2291 */         if (this.hasLimitClause) {
/* 2292 */           this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false);
/*      */         }
/*      */         else
/*      */         {
/* 2296 */           if (this.maxRows <= 0) {
/* 2297 */             executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */           }
/*      */           else {
/* 2300 */             executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
/*      */           }
/*      */           
/*      */ 
/* 2304 */           this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
/*      */           
/*      */ 
/*      */ 
/* 2308 */           if (oldCatalog != null) {
/* 2309 */             this.connection.setCatalog(oldCatalog);
/*      */           }
/*      */         }
/*      */       } else {
/* 2313 */         this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2318 */       if (oldCatalog != null) {
/* 2319 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 2322 */       if (cachedMetadata != null) {
/* 2323 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
/*      */ 
/*      */       }
/* 2326 */       else if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 2327 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, this.results);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2332 */       this.lastInsertId = this.results.getUpdateID();
/*      */       
/* 2334 */       return this.results;
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
/*      */   public int executeUpdate()
/*      */     throws SQLException
/*      */   {
/* 2350 */     return executeUpdate(true, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/* 2360 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2361 */       if (clearBatchedGeneratedKeysAndWarnings) {
/* 2362 */         clearWarnings();
/* 2363 */         this.batchedGeneratedKeys = null;
/*      */       }
/*      */       
/* 2366 */       return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
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
/*      */   protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch)
/*      */     throws SQLException
/*      */   {
/* 2395 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 2397 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 2399 */       if (locallyScopedConn.isReadOnly()) {
/* 2400 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2405 */       if ((this.firstCharOfStmt == 'S') && (isSelectQuery()))
/*      */       {
/* 2407 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 2411 */       implicitlyCloseAllOpenResults();
/*      */       
/* 2413 */       ResultSetInternalMethods rs = null;
/*      */       
/* 2415 */       Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
/*      */       
/*      */ 
/*      */ 
/* 2419 */       String oldCatalog = null;
/*      */       
/* 2421 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 2422 */         oldCatalog = locallyScopedConn.getCatalog();
/* 2423 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2429 */       if (locallyScopedConn.useMaxRows()) {
/* 2430 */         executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */       }
/*      */       
/*      */ 
/* 2434 */       boolean oldInfoMsgState = false;
/*      */       
/* 2436 */       if (this.retrieveGeneratedKeys) {
/* 2437 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/* 2438 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */       }
/*      */       
/* 2441 */       rs = executeInternal(-1, sendPacket, false, false, null, isReallyBatch);
/*      */       
/*      */ 
/* 2444 */       if (this.retrieveGeneratedKeys) {
/* 2445 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/* 2446 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       }
/*      */       
/* 2449 */       if (oldCatalog != null) {
/* 2450 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 2453 */       this.results = rs;
/*      */       
/* 2455 */       this.updateCount = rs.getUpdateCount();
/*      */       
/* 2457 */       if ((containsOnDuplicateKeyUpdateInSQL()) && (this.compensateForOnDuplicateKeyUpdate))
/*      */       {
/* 2459 */         if ((this.updateCount == 2L) || (this.updateCount == 0L)) {
/* 2460 */           this.updateCount = 1L;
/*      */         }
/*      */       }
/*      */       
/* 2464 */       int truncatedUpdateCount = 0;
/*      */       
/* 2466 */       if (this.updateCount > 2147483647L) {
/* 2467 */         truncatedUpdateCount = Integer.MAX_VALUE;
/*      */       } else {
/* 2469 */         truncatedUpdateCount = (int)this.updateCount;
/*      */       }
/*      */       
/* 2472 */       this.lastInsertId = rs.getUpdateID();
/*      */       
/* 2474 */       return truncatedUpdateCount;
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean containsOnDuplicateKeyUpdateInSQL() {
/* 2479 */     return this.parseInfo.isOnDuplicateKeyUpdate;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths)
/*      */     throws SQLException
/*      */   {
/* 2518 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2519 */       Buffer sendPacket = this.connection.getIO().getSharedSendPacket();
/*      */       
/* 2521 */       sendPacket.clear();
/*      */       
/* 2523 */       sendPacket.writeByte((byte)3);
/*      */       
/* 2525 */       boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2532 */       int ensurePacketSize = 0;
/*      */       
/* 2534 */       String statementComment = this.connection.getStatementComment();
/*      */       
/* 2536 */       byte[] commentAsBytes = null;
/*      */       
/* 2538 */       if (statementComment != null) {
/* 2539 */         if (this.charConverter != null) {
/* 2540 */           commentAsBytes = this.charConverter.toBytes(statementComment);
/*      */         } else {
/* 2542 */           commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2548 */         ensurePacketSize += commentAsBytes.length;
/* 2549 */         ensurePacketSize += 6;
/*      */       }
/*      */       
/* 2552 */       for (int i = 0; i < batchedParameterStrings.length; i++) {
/* 2553 */         if ((batchedIsStream[i] != 0) && (useStreamLengths)) {
/* 2554 */           ensurePacketSize += batchedStreamLengths[i];
/*      */         }
/*      */       }
/*      */       
/* 2558 */       if (ensurePacketSize != 0) {
/* 2559 */         sendPacket.ensureCapacity(ensurePacketSize);
/*      */       }
/*      */       
/* 2562 */       if (commentAsBytes != null) {
/* 2563 */         sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
/* 2564 */         sendPacket.writeBytesNoNull(commentAsBytes);
/* 2565 */         sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
/*      */       }
/*      */       
/* 2568 */       for (int i = 0; i < batchedParameterStrings.length; i++) {
/* 2569 */         checkAllParametersSet(batchedParameterStrings[i], batchedParameterStreams[i], i);
/*      */         
/*      */ 
/* 2572 */         sendPacket.writeBytesNoNull(this.staticSqlStrings[i]);
/*      */         
/* 2574 */         if (batchedIsStream[i] != 0) {
/* 2575 */           streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths);
/*      */         }
/*      */         else {
/* 2578 */           sendPacket.writeBytesNoNull(batchedParameterStrings[i]);
/*      */         }
/*      */       }
/*      */       
/* 2582 */       sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
/*      */       
/*      */ 
/* 2585 */       return sendPacket;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex) throws SQLException
/*      */   {
/* 2591 */     if ((parameterString == null) && (parameterStream == null))
/*      */     {
/*      */ 
/* 2594 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches)
/*      */     throws SQLException
/*      */   {
/* 2604 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2605 */       PreparedStatement pstmt = new PreparedStatement(localConn, "Rewritten batch of: " + this.originalSql, this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches));
/* 2606 */       pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
/* 2607 */       pstmt.rewrittenBatchSize = numBatches;
/*      */       
/* 2609 */       return pstmt;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setRetrieveGeneratedKeys(boolean flag) throws SQLException {
/* 2614 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2615 */       this.retrieveGeneratedKeys = flag;
/*      */     }
/*      */   }
/*      */   
/* 2619 */   protected int rewrittenBatchSize = 0;
/*      */   
/*      */   public int getRewrittenBatchSize() {
/* 2622 */     return this.rewrittenBatchSize;
/*      */   }
/*      */   
/*      */   public String getNonRewrittenSql() throws SQLException {
/* 2626 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2627 */       int indexOfBatch = this.originalSql.indexOf(" of: ");
/*      */       
/* 2629 */       if (indexOfBatch != -1) {
/* 2630 */         return this.originalSql.substring(indexOfBatch + 5);
/*      */       }
/*      */       
/* 2633 */       return this.originalSql;
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
/*      */   public byte[] getBytesRepresentation(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 2651 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2652 */       if (this.isStream[parameterIndex] != 0) {
/* 2653 */         return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2658 */       byte[] parameterVal = this.parameterValues[parameterIndex];
/*      */       
/* 2660 */       if (parameterVal == null) {
/* 2661 */         return null;
/*      */       }
/*      */       
/* 2664 */       if ((parameterVal[0] == 39) && (parameterVal[(parameterVal.length - 1)] == 39))
/*      */       {
/* 2666 */         byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2667 */         System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */         
/*      */ 
/* 2670 */         return valNoQuotes;
/*      */       }
/*      */       
/* 2673 */       return parameterVal;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex)
/*      */     throws SQLException
/*      */   {
/* 2686 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2687 */       Object batchedArg = this.batchedArgs.get(commandIndex);
/* 2688 */       if ((batchedArg instanceof String)) {
/*      */         try {
/* 2690 */           return StringUtils.getBytes((String)batchedArg, this.charEncoding);
/*      */         }
/*      */         catch (UnsupportedEncodingException uue) {
/* 2693 */           throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2700 */       BatchParams params = (BatchParams)batchedArg;
/* 2701 */       if (params.isStream[parameterIndex] != 0) {
/* 2702 */         return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */       }
/*      */       
/* 2705 */       byte[] parameterVal = params.parameterStrings[parameterIndex];
/* 2706 */       if (parameterVal == null) {
/* 2707 */         return null;
/*      */       }
/* 2709 */       if ((parameterVal[0] == 39) && (parameterVal[(parameterVal.length - 1)] == 39))
/*      */       {
/* 2711 */         byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2712 */         System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */         
/*      */ 
/* 2715 */         return valNoQuotes;
/*      */       }
/*      */       
/* 2718 */       return parameterVal;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String getDateTimePattern(String dt, boolean toTime)
/*      */     throws Exception
/*      */   {
/* 2729 */     int dtLength = dt != null ? dt.length() : 0;
/*      */     
/* 2731 */     if ((dtLength >= 8) && (dtLength <= 10)) {
/* 2732 */       int dashCount = 0;
/* 2733 */       boolean isDateOnly = true;
/*      */       
/* 2735 */       for (int i = 0; i < dtLength; i++) {
/* 2736 */         char c = dt.charAt(i);
/*      */         
/* 2738 */         if ((!Character.isDigit(c)) && (c != '-')) {
/* 2739 */           isDateOnly = false;
/*      */           
/* 2741 */           break;
/*      */         }
/*      */         
/* 2744 */         if (c == '-') {
/* 2745 */           dashCount++;
/*      */         }
/*      */       }
/*      */       
/* 2749 */       if ((isDateOnly) && (dashCount == 2)) {
/* 2750 */         return "yyyy-MM-dd";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2757 */     boolean colonsOnly = true;
/*      */     
/* 2759 */     for (int i = 0; i < dtLength; i++) {
/* 2760 */       char c = dt.charAt(i);
/*      */       
/* 2762 */       if ((!Character.isDigit(c)) && (c != ':')) {
/* 2763 */         colonsOnly = false;
/*      */         
/* 2765 */         break;
/*      */       }
/*      */     }
/*      */     
/* 2769 */     if (colonsOnly) {
/* 2770 */       return "HH:mm:ss";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2779 */     StringReader reader = new StringReader(dt + " ");
/* 2780 */     ArrayList<Object[]> vec = new ArrayList();
/* 2781 */     ArrayList<Object[]> vecRemovelist = new ArrayList();
/* 2782 */     Object[] nv = new Object[3];
/*      */     
/* 2784 */     nv[0] = Character.valueOf('y');
/* 2785 */     nv[1] = new StringBuffer();
/* 2786 */     nv[2] = Integer.valueOf(0);
/* 2787 */     vec.add(nv);
/*      */     
/* 2789 */     if (toTime) {
/* 2790 */       nv = new Object[3];
/* 2791 */       nv[0] = Character.valueOf('h');
/* 2792 */       nv[1] = new StringBuffer();
/* 2793 */       nv[2] = Integer.valueOf(0);
/* 2794 */       vec.add(nv);
/*      */     }
/*      */     int z;
/* 2797 */     while ((z = reader.read()) != -1) {
/* 2798 */       char separator = (char)z;
/* 2799 */       int maxvecs = vec.size();
/*      */       
/* 2801 */       for (int count = 0; count < maxvecs; count++) {
/* 2802 */         Object[] v = (Object[])vec.get(count);
/* 2803 */         int n = ((Integer)v[2]).intValue();
/* 2804 */         char c = getSuccessor(((Character)v[0]).charValue(), n);
/*      */         
/* 2806 */         if (!Character.isLetterOrDigit(separator)) {
/* 2807 */           if ((c == ((Character)v[0]).charValue()) && (c != 'S')) {
/* 2808 */             vecRemovelist.add(v);
/*      */           } else {
/* 2810 */             ((StringBuffer)v[1]).append(separator);
/*      */             
/* 2812 */             if ((c == 'X') || (c == 'Y')) {
/* 2813 */               v[2] = Integer.valueOf(4);
/*      */             }
/*      */           }
/*      */         } else {
/* 2817 */           if (c == 'X') {
/* 2818 */             c = 'y';
/* 2819 */             nv = new Object[3];
/* 2820 */             nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('M');
/*      */             
/* 2822 */             nv[0] = Character.valueOf('M');
/* 2823 */             nv[2] = Integer.valueOf(1);
/* 2824 */             vec.add(nv);
/* 2825 */           } else if (c == 'Y') {
/* 2826 */             c = 'M';
/* 2827 */             nv = new Object[3];
/* 2828 */             nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('d');
/*      */             
/* 2830 */             nv[0] = Character.valueOf('d');
/* 2831 */             nv[2] = Integer.valueOf(1);
/* 2832 */             vec.add(nv);
/*      */           }
/*      */           
/* 2835 */           ((StringBuffer)v[1]).append(c);
/*      */           
/* 2837 */           if (c == ((Character)v[0]).charValue()) {
/* 2838 */             v[2] = Integer.valueOf(n + 1);
/*      */           } else {
/* 2840 */             v[0] = Character.valueOf(c);
/* 2841 */             v[2] = Integer.valueOf(1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2846 */       int size = vecRemovelist.size();
/*      */       
/* 2848 */       for (int i = 0; i < size; i++) {
/* 2849 */         Object[] v = (Object[])vecRemovelist.get(i);
/* 2850 */         vec.remove(v);
/*      */       }
/*      */       
/* 2853 */       vecRemovelist.clear();
/*      */     }
/*      */     
/* 2856 */     int size = vec.size();
/*      */     
/* 2858 */     for (int i = 0; i < size; i++) {
/* 2859 */       Object[] v = (Object[])vec.get(i);
/* 2860 */       char c = ((Character)v[0]).charValue();
/* 2861 */       int n = ((Integer)v[2]).intValue();
/*      */       
/* 2863 */       boolean bk = getSuccessor(c, n) != c;
/* 2864 */       boolean atEnd = ((c == 's') || (c == 'm') || ((c == 'h') && (toTime))) && (bk);
/* 2865 */       boolean finishesAtDate = (bk) && (c == 'd') && (!toTime);
/* 2866 */       boolean containsEnd = ((StringBuffer)v[1]).toString().indexOf('W') != -1;
/*      */       
/*      */ 
/* 2869 */       if (((!atEnd) && (!finishesAtDate)) || (containsEnd)) {
/* 2870 */         vecRemovelist.add(v);
/*      */       }
/*      */     }
/*      */     
/* 2874 */     size = vecRemovelist.size();
/*      */     
/* 2876 */     for (int i = 0; i < size; i++) {
/* 2877 */       vec.remove(vecRemovelist.get(i));
/*      */     }
/*      */     
/* 2880 */     vecRemovelist.clear();
/* 2881 */     Object[] v = (Object[])vec.get(0);
/*      */     
/* 2883 */     StringBuffer format = (StringBuffer)v[1];
/* 2884 */     format.setLength(format.length() - 1);
/*      */     
/* 2886 */     return format.toString();
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
/*      */   public java.sql.ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 2901 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2913 */       if (!isSelectQuery()) {
/* 2914 */         return null;
/*      */       }
/*      */       
/* 2917 */       PreparedStatement mdStmt = null;
/* 2918 */       ResultSet mdRs = null;
/*      */       
/* 2920 */       if (this.pstmtResultMetaData == null) {
/*      */         try {
/* 2922 */           mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
/*      */           
/*      */ 
/* 2925 */           mdStmt.setMaxRows(1);
/*      */           
/* 2927 */           int paramCount = this.parameterValues.length;
/*      */           
/* 2929 */           for (int i = 1; i <= paramCount; i++) {
/* 2930 */             mdStmt.setString(i, "");
/*      */           }
/*      */           
/* 2933 */           boolean hadResults = mdStmt.execute();
/*      */           
/* 2935 */           if (hadResults) {
/* 2936 */             mdRs = mdStmt.getResultSet();
/*      */             
/* 2938 */             this.pstmtResultMetaData = mdRs.getMetaData();
/*      */           } else {
/* 2940 */             this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */         finally {
/*      */           SQLException sqlExRethrow;
/* 2945 */           SQLException sqlExRethrow = null;
/*      */           
/* 2947 */           if (mdRs != null) {
/*      */             try {
/* 2949 */               mdRs.close();
/*      */             } catch (SQLException sqlEx) {
/* 2951 */               sqlExRethrow = sqlEx;
/*      */             }
/*      */             
/* 2954 */             mdRs = null;
/*      */           }
/*      */           
/* 2957 */           if (mdStmt != null) {
/*      */             try {
/* 2959 */               mdStmt.close();
/*      */             } catch (SQLException sqlEx) {
/* 2961 */               sqlExRethrow = sqlEx;
/*      */             }
/*      */             
/* 2964 */             mdStmt = null;
/*      */           }
/*      */           
/* 2967 */           if (sqlExRethrow != null) {
/* 2968 */             throw sqlExRethrow;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2973 */       return this.pstmtResultMetaData;
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
/*      */   public ParameterMetaData getParameterMetaData()
/*      */     throws SQLException
/*      */   {
/* 2991 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2992 */       if (this.parameterMetaData == null) {
/* 2993 */         if (this.connection.getGenerateSimpleParameterMetadata()) {
/* 2994 */           this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
/*      */         } else {
/* 2996 */           this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3001 */       return this.parameterMetaData;
/*      */     }
/*      */   }
/*      */   
/*      */   ParseInfo getParseInfo() {
/* 3006 */     return this.parseInfo;
/*      */   }
/*      */   
/*      */   private final char getSuccessor(char c, int n) {
/* 3010 */     return (c == 's') && (n < 2) ? 's' : c == 'm' ? 's' : (c == 'm') && (n < 2) ? 'm' : c == 'H' ? 'm' : (c == 'H') && (n < 2) ? 'H' : c == 'd' ? 'H' : (c == 'd') && (n < 2) ? 'd' : c == 'M' ? 'd' : (c == 'M') && (n < 3) ? 'M' : (c == 'M') && (n == 2) ? 'Y' : c == 'y' ? 'M' : (c == 'y') && (n < 4) ? 'y' : (c == 'y') && (n == 2) ? 'X' : 'W';
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
/*      */   private final void hexEscapeBlock(byte[] buf, Buffer packet, int size)
/*      */     throws SQLException
/*      */   {
/* 3036 */     for (int i = 0; i < size; i++) {
/* 3037 */       byte b = buf[i];
/* 3038 */       int lowBits = (b & 0xFF) / 16;
/* 3039 */       int highBits = (b & 0xFF) % 16;
/*      */       
/* 3041 */       packet.writeByte(HEX_DIGITS[lowBits]);
/* 3042 */       packet.writeByte(HEX_DIGITS[highBits]);
/*      */     }
/*      */   }
/*      */   
/*      */   private void initializeFromParseInfo() throws SQLException {
/* 3047 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3048 */       this.staticSqlStrings = this.parseInfo.staticSql;
/* 3049 */       this.hasLimitClause = this.parseInfo.foundLimitClause;
/* 3050 */       this.isLoadDataQuery = this.parseInfo.foundLoadData;
/* 3051 */       this.firstCharOfStmt = this.parseInfo.firstStmtChar;
/*      */       
/* 3053 */       this.parameterCount = (this.staticSqlStrings.length - 1);
/*      */       
/* 3055 */       this.parameterValues = new byte[this.parameterCount][];
/* 3056 */       this.parameterStreams = new InputStream[this.parameterCount];
/* 3057 */       this.isStream = new boolean[this.parameterCount];
/* 3058 */       this.streamLengths = new int[this.parameterCount];
/* 3059 */       this.isNull = new boolean[this.parameterCount];
/* 3060 */       this.parameterTypes = new int[this.parameterCount];
/*      */       
/* 3062 */       clearParameters();
/*      */       
/* 3064 */       for (int j = 0; j < this.parameterCount; j++) {
/* 3065 */         this.isStream[j] = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int readblock(InputStream i, byte[] b)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3078 */       return i.read(b);
/*      */     } catch (Throwable ex) {
/* 3080 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 3082 */       sqlEx.initCause(ex);
/*      */       
/* 3084 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b, int length) throws SQLException
/*      */   {
/*      */     try {
/* 3091 */       int lengthToRead = length;
/*      */       
/* 3093 */       if (lengthToRead > b.length) {
/* 3094 */         lengthToRead = b.length;
/*      */       }
/*      */       
/* 3097 */       return i.read(b, 0, lengthToRead);
/*      */     } catch (Throwable ex) {
/* 3099 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 3101 */       sqlEx.initCause(ex);
/*      */       
/* 3103 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults)
/*      */     throws SQLException
/*      */   {
/*      */     MySQLConnection locallyScopedConn;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 3121 */       locallyScopedConn = checkClosed();
/*      */     } catch (SQLException sqlEx) {
/* 3123 */       return;
/*      */     }
/*      */     
/* 3126 */     synchronized (locallyScopedConn.getConnectionMutex())
/*      */     {
/* 3128 */       if ((this.useUsageAdvisor) && 
/* 3129 */         (this.numberOfExecutions <= 1)) {
/* 3130 */         String message = Messages.getString("PreparedStatement.43");
/*      */         
/* 3132 */         this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3141 */       super.realClose(calledExplicitly, closeOpenResults);
/*      */       
/* 3143 */       this.dbmd = null;
/* 3144 */       this.originalSql = null;
/* 3145 */       this.staticSqlStrings = ((byte[][])null);
/* 3146 */       this.parameterValues = ((byte[][])null);
/* 3147 */       this.parameterStreams = null;
/* 3148 */       this.isStream = null;
/* 3149 */       this.streamLengths = null;
/* 3150 */       this.isNull = null;
/* 3151 */       this.streamConvertBuf = null;
/* 3152 */       this.parameterTypes = null;
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
/*      */   public void setArray(int i, Array x)
/*      */     throws SQLException
/*      */   {
/* 3170 */     throw SQLError.notImplemented();
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
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 3197 */     if (x == null) {
/* 3198 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 3200 */       setBinaryStream(parameterIndex, x, length);
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
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 3218 */     if (x == null) {
/* 3219 */       setNull(parameterIndex, 3);
/*      */     } else {
/* 3221 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
/*      */       
/*      */ 
/* 3224 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 3;
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
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 3250 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3251 */       if (x == null) {
/* 3252 */         setNull(parameterIndex, -2);
/*      */       } else {
/* 3254 */         int parameterIndexOffset = getParameterIndexOffset();
/*      */         
/* 3256 */         if ((parameterIndex < 1) || (parameterIndex > this.staticSqlStrings.length))
/*      */         {
/* 3258 */           throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 3263 */         if ((parameterIndexOffset == -1) && (parameterIndex == 1)) {
/* 3264 */           throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 3269 */         this.parameterStreams[(parameterIndex - 1 + parameterIndexOffset)] = x;
/* 3270 */         this.isStream[(parameterIndex - 1 + parameterIndexOffset)] = true;
/* 3271 */         this.streamLengths[(parameterIndex - 1 + parameterIndexOffset)] = length;
/* 3272 */         this.isNull[(parameterIndex - 1 + parameterIndexOffset)] = false;
/* 3273 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2004;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
/*      */   {
/* 3280 */     setBinaryStream(parameterIndex, inputStream, (int)length);
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
/*      */   public void setBlob(int i, Blob x)
/*      */     throws SQLException
/*      */   {
/* 3295 */     if (x == null) {
/* 3296 */       setNull(i, 2004);
/*      */     } else {
/* 3298 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 3300 */       bytesOut.write(39);
/* 3301 */       escapeblockFast(x.getBytes(1L, (int)x.length()), bytesOut, (int)x.length());
/*      */       
/* 3303 */       bytesOut.write(39);
/*      */       
/* 3305 */       setInternal(i, bytesOut.toByteArray());
/*      */       
/* 3307 */       this.parameterTypes[(i - 1 + getParameterIndexOffset())] = 2004;
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
/*      */   public void setBoolean(int parameterIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 3324 */     if (this.useTrueBoolean) {
/* 3325 */       setInternal(parameterIndex, x ? "1" : "0");
/*      */     } else {
/* 3327 */       setInternal(parameterIndex, x ? "'t'" : "'f'");
/*      */       
/* 3329 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 16;
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
/*      */   public void setByte(int parameterIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 3346 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3348 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -6;
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
/*      */   public void setBytes(int parameterIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 3365 */     setBytes(parameterIndex, x, true, true);
/*      */     
/* 3367 */     if (x != null) {
/* 3368 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -2;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars)
/*      */     throws SQLException
/*      */   {
/* 3375 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3376 */       if (x == null) {
/* 3377 */         setNull(parameterIndex, -2);
/*      */       } else {
/* 3379 */         String connectionEncoding = this.connection.getEncoding();
/*      */         try
/*      */         {
/* 3382 */           if ((this.connection.isNoBackslashEscapesSet()) || ((escapeForMBChars) && (this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding))))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3390 */             ByteArrayOutputStream bOut = new ByteArrayOutputStream(x.length * 2 + 3);
/*      */             
/* 3392 */             bOut.write(120);
/* 3393 */             bOut.write(39);
/*      */             
/* 3395 */             for (int i = 0; i < x.length; i++) {
/* 3396 */               int lowBits = (x[i] & 0xFF) / 16;
/* 3397 */               int highBits = (x[i] & 0xFF) % 16;
/*      */               
/* 3399 */               bOut.write(HEX_DIGITS[lowBits]);
/* 3400 */               bOut.write(HEX_DIGITS[highBits]);
/*      */             }
/*      */             
/* 3403 */             bOut.write(39);
/*      */             
/* 3405 */             setInternal(parameterIndex, bOut.toByteArray());
/*      */             
/* 3407 */             return;
/*      */           }
/*      */         } catch (SQLException ex) {
/* 3410 */           throw ex;
/*      */         } catch (RuntimeException ex) {
/* 3412 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 3413 */           sqlEx.initCause(ex);
/* 3414 */           throw sqlEx;
/*      */         }
/*      */         
/*      */ 
/* 3418 */         int numBytes = x.length;
/*      */         
/* 3420 */         int pad = 2;
/*      */         
/* 3422 */         boolean needsIntroducer = (checkForIntroducer) && (this.connection.versionMeetsMinimum(4, 1, 0));
/*      */         
/*      */ 
/* 3425 */         if (needsIntroducer) {
/* 3426 */           pad += 7;
/*      */         }
/*      */         
/* 3429 */         ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
/*      */         
/*      */ 
/* 3432 */         if (needsIntroducer) {
/* 3433 */           bOut.write(95);
/* 3434 */           bOut.write(98);
/* 3435 */           bOut.write(105);
/* 3436 */           bOut.write(110);
/* 3437 */           bOut.write(97);
/* 3438 */           bOut.write(114);
/* 3439 */           bOut.write(121);
/*      */         }
/* 3441 */         bOut.write(39);
/*      */         
/* 3443 */         for (int i = 0; i < numBytes; i++) {
/* 3444 */           byte b = x[i];
/*      */           
/* 3446 */           switch (b) {
/*      */           case 0: 
/* 3448 */             bOut.write(92);
/* 3449 */             bOut.write(48);
/*      */             
/* 3451 */             break;
/*      */           
/*      */           case 10: 
/* 3454 */             bOut.write(92);
/* 3455 */             bOut.write(110);
/*      */             
/* 3457 */             break;
/*      */           
/*      */           case 13: 
/* 3460 */             bOut.write(92);
/* 3461 */             bOut.write(114);
/*      */             
/* 3463 */             break;
/*      */           
/*      */           case 92: 
/* 3466 */             bOut.write(92);
/* 3467 */             bOut.write(92);
/*      */             
/* 3469 */             break;
/*      */           
/*      */           case 39: 
/* 3472 */             bOut.write(92);
/* 3473 */             bOut.write(39);
/*      */             
/* 3475 */             break;
/*      */           
/*      */           case 34: 
/* 3478 */             bOut.write(92);
/* 3479 */             bOut.write(34);
/*      */             
/* 3481 */             break;
/*      */           
/*      */           case 26: 
/* 3484 */             bOut.write(92);
/* 3485 */             bOut.write(90);
/*      */             
/* 3487 */             break;
/*      */           
/*      */           default: 
/* 3490 */             bOut.write(b);
/*      */           }
/*      */           
/*      */         }
/* 3494 */         bOut.write(39);
/*      */         
/* 3496 */         setInternal(parameterIndex, bOut.toByteArray());
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
/*      */ 
/*      */ 
/*      */   protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes)
/*      */     throws SQLException
/*      */   {
/* 3515 */     byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
/* 3516 */     parameterWithQuotes[0] = 39;
/* 3517 */     System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
/*      */     
/* 3519 */     parameterWithQuotes[(parameterAsBytes.length + 1)] = 39;
/*      */     
/* 3521 */     setInternal(parameterIndex, parameterWithQuotes);
/*      */   }
/*      */   
/*      */   protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException
/*      */   {
/* 3526 */     setInternal(parameterIndex, parameterAsBytes);
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
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 3553 */     synchronized (checkClosed().getConnectionMutex()) {
/*      */       try {
/* 3555 */         if (reader == null) {
/* 3556 */           setNull(parameterIndex, -1);
/*      */         } else {
/* 3558 */           char[] c = null;
/* 3559 */           int len = 0;
/*      */           
/* 3561 */           boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */           
/*      */ 
/* 3564 */           String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */           
/* 3566 */           if ((useLength) && (length != -1)) {
/* 3567 */             c = new char[length];
/*      */             
/* 3569 */             int numCharsRead = readFully(reader, c, length);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 3574 */             if (forcedEncoding == null) {
/* 3575 */               setString(parameterIndex, new String(c, 0, numCharsRead));
/*      */             } else {
/*      */               try {
/* 3578 */                 setBytes(parameterIndex, StringUtils.getBytes(new String(c, 0, numCharsRead), forcedEncoding));
/*      */               }
/*      */               catch (UnsupportedEncodingException uee)
/*      */               {
/* 3582 */                 throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/* 3587 */             c = new char['က'];
/*      */             
/* 3589 */             StringBuffer buf = new StringBuffer();
/*      */             
/* 3591 */             while ((len = reader.read(c)) != -1) {
/* 3592 */               buf.append(c, 0, len);
/*      */             }
/*      */             
/* 3595 */             if (forcedEncoding == null) {
/* 3596 */               setString(parameterIndex, buf.toString());
/*      */             } else {
/*      */               try {
/* 3599 */                 setBytes(parameterIndex, StringUtils.getBytes(buf.toString(), forcedEncoding));
/*      */               }
/*      */               catch (UnsupportedEncodingException uee) {
/* 3602 */                 throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 3608 */           this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
/*      */         }
/*      */       } catch (IOException ioEx) {
/* 3611 */         throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
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
/*      */ 
/*      */   public void setClob(int i, Clob x)
/*      */     throws SQLException
/*      */   {
/* 3629 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3630 */       if (x == null) {
/* 3631 */         setNull(i, 2005);
/*      */       }
/*      */       else {
/* 3634 */         String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */         
/* 3636 */         if (forcedEncoding == null) {
/* 3637 */           setString(i, x.getSubString(1L, (int)x.length()));
/*      */         } else {
/*      */           try {
/* 3640 */             setBytes(i, StringUtils.getBytes(x.getSubString(1L, (int)x.length()), forcedEncoding));
/*      */           }
/*      */           catch (UnsupportedEncodingException uee) {
/* 3643 */             throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 3648 */         this.parameterTypes[(i - 1 + getParameterIndexOffset())] = 2005;
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
/*      */ 
/*      */ 
/*      */   public void setDate(int parameterIndex, java.sql.Date x)
/*      */     throws SQLException
/*      */   {
/* 3667 */     setDate(parameterIndex, x, null);
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
/*      */   public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 3686 */     if (x == null) {
/* 3687 */       setNull(parameterIndex, 91);
/*      */     } else {
/* 3689 */       checkClosed();
/*      */       
/* 3691 */       if (!this.useLegacyDatetimeCode) {
/* 3692 */         newSetDateInternal(parameterIndex, x, cal);
/*      */       }
/*      */       else
/*      */       {
/* 3696 */         SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */         
/* 3698 */         setInternal(parameterIndex, dateFormatter.format(x));
/*      */         
/* 3700 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 91;
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
/*      */ 
/*      */   public void setDouble(int parameterIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 3718 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3719 */       if ((!this.connection.getAllowNanAndInf()) && ((x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) || (Double.isNaN(x))))
/*      */       {
/*      */ 
/* 3722 */         throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3728 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */       
/*      */ 
/* 3731 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 8;
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
/*      */   public void setFloat(int parameterIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 3748 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */     
/*      */ 
/* 3751 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 6;
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
/*      */   public void setInt(int parameterIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 3767 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3769 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 4;
/*      */   }
/*      */   
/*      */   protected final void setInternal(int paramIndex, byte[] val) throws SQLException
/*      */   {
/* 3774 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 3776 */       int parameterIndexOffset = getParameterIndexOffset();
/*      */       
/* 3778 */       checkBounds(paramIndex, parameterIndexOffset);
/*      */       
/* 3780 */       this.isStream[(paramIndex - 1 + parameterIndexOffset)] = false;
/* 3781 */       this.isNull[(paramIndex - 1 + parameterIndexOffset)] = false;
/* 3782 */       this.parameterStreams[(paramIndex - 1 + parameterIndexOffset)] = null;
/* 3783 */       this.parameterValues[(paramIndex - 1 + parameterIndexOffset)] = val;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException
/*      */   {
/* 3789 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3790 */       if (paramIndex < 1) {
/* 3791 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 3795 */       if (paramIndex > this.parameterCount) {
/* 3796 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3801 */       if ((parameterIndexOffset == -1) && (paramIndex == 1)) {
/* 3802 */         throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void setInternal(int paramIndex, String val)
/*      */     throws SQLException
/*      */   {
/* 3810 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 3812 */       byte[] parameterAsBytes = null;
/*      */       
/* 3814 */       if (this.charConverter != null) {
/* 3815 */         parameterAsBytes = this.charConverter.toBytes(val);
/*      */       } else {
/* 3817 */         parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3823 */       setInternal(paramIndex, parameterAsBytes);
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
/*      */   public void setLong(int parameterIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 3840 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3842 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -5;
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
/*      */   public void setNull(int parameterIndex, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 3862 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3863 */       setInternal(parameterIndex, "null");
/* 3864 */       this.isNull[(parameterIndex - 1 + getParameterIndexOffset())] = true;
/*      */       
/* 3866 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 0;
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
/*      */   public void setNull(int parameterIndex, int sqlType, String arg)
/*      */     throws SQLException
/*      */   {
/* 3889 */     setNull(parameterIndex, sqlType);
/*      */     
/* 3891 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 0;
/*      */   }
/*      */   
/*      */   private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/*      */     Number parameterAsNum;
/*      */     Number parameterAsNum;
/* 3897 */     if ((parameterObj instanceof Boolean)) {
/* 3898 */       parameterAsNum = ((Boolean)parameterObj).booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);
/*      */ 
/*      */     }
/* 3901 */     else if ((parameterObj instanceof String)) { Number parameterAsNum;
/* 3902 */       switch (targetSqlType) {
/*      */       case -7:  Number parameterAsNum;
/* 3904 */         if (("1".equals(parameterObj)) || ("0".equals(parameterObj)))
/*      */         {
/* 3906 */           parameterAsNum = Integer.valueOf((String)parameterObj);
/*      */         } else {
/* 3908 */           boolean parameterAsBoolean = "true".equalsIgnoreCase((String)parameterObj);
/*      */           
/*      */ 
/* 3911 */           parameterAsNum = parameterAsBoolean ? Integer.valueOf(1) : Integer.valueOf(0);
/*      */         }
/*      */         
/*      */ 
/* 3915 */         break;
/*      */       
/*      */       case -6: 
/*      */       case 4: 
/*      */       case 5: 
/* 3920 */         parameterAsNum = Integer.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 3923 */         break;
/*      */       
/*      */       case -5: 
/* 3926 */         parameterAsNum = Long.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 3929 */         break;
/*      */       
/*      */       case 7: 
/* 3932 */         parameterAsNum = Float.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 3935 */         break;
/*      */       
/*      */       case 6: 
/*      */       case 8: 
/* 3939 */         parameterAsNum = Double.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 3942 */         break;
/*      */       case -4: case -3: case -2: 
/*      */       case -1: case 0: 
/*      */       case 1: case 2: 
/*      */       case 3: default: 
/* 3947 */         parameterAsNum = new BigDecimal((String)parameterObj);break;
/*      */       }
/*      */     }
/*      */     else {
/* 3951 */       parameterAsNum = (Number)parameterObj;
/*      */     }
/*      */     
/* 3954 */     switch (targetSqlType) {
/*      */     case -7: 
/*      */     case -6: 
/*      */     case 4: 
/*      */     case 5: 
/* 3959 */       setInt(parameterIndex, parameterAsNum.intValue());
/*      */       
/* 3961 */       break;
/*      */     
/*      */     case -5: 
/* 3964 */       setLong(parameterIndex, parameterAsNum.longValue());
/*      */       
/* 3966 */       break;
/*      */     
/*      */     case 7: 
/* 3969 */       setFloat(parameterIndex, parameterAsNum.floatValue());
/*      */       
/* 3971 */       break;
/*      */     
/*      */     case 6: 
/*      */     case 8: 
/* 3975 */       setDouble(parameterIndex, parameterAsNum.doubleValue());
/*      */       
/* 3977 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 3982 */       if ((parameterAsNum instanceof BigDecimal)) {
/* 3983 */         BigDecimal scaledBigDecimal = null;
/*      */         try
/*      */         {
/* 3986 */           scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale);
/*      */         }
/*      */         catch (ArithmeticException ex) {
/*      */           try {
/* 3990 */             scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale, 4);
/*      */           }
/*      */           catch (ArithmeticException arEx)
/*      */           {
/* 3994 */             throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009", getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4003 */         setBigDecimal(parameterIndex, scaledBigDecimal);
/* 4004 */       } else if ((parameterAsNum instanceof BigInteger)) {
/* 4005 */         setBigDecimal(parameterIndex, new BigDecimal((BigInteger)parameterAsNum, scale));
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 4011 */         setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   public void setObject(int parameterIndex, Object parameterObj)
/*      */     throws SQLException
/*      */   {
/* 4022 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4023 */       if (parameterObj == null) {
/* 4024 */         setNull(parameterIndex, 1111);
/*      */       }
/* 4026 */       else if ((parameterObj instanceof Byte)) {
/* 4027 */         setInt(parameterIndex, ((Byte)parameterObj).intValue());
/* 4028 */       } else if ((parameterObj instanceof String)) {
/* 4029 */         setString(parameterIndex, (String)parameterObj);
/* 4030 */       } else if ((parameterObj instanceof BigDecimal)) {
/* 4031 */         setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
/* 4032 */       } else if ((parameterObj instanceof Short)) {
/* 4033 */         setShort(parameterIndex, ((Short)parameterObj).shortValue());
/* 4034 */       } else if ((parameterObj instanceof Integer)) {
/* 4035 */         setInt(parameterIndex, ((Integer)parameterObj).intValue());
/* 4036 */       } else if ((parameterObj instanceof Long)) {
/* 4037 */         setLong(parameterIndex, ((Long)parameterObj).longValue());
/* 4038 */       } else if ((parameterObj instanceof Float)) {
/* 4039 */         setFloat(parameterIndex, ((Float)parameterObj).floatValue());
/* 4040 */       } else if ((parameterObj instanceof Double)) {
/* 4041 */         setDouble(parameterIndex, ((Double)parameterObj).doubleValue());
/* 4042 */       } else if ((parameterObj instanceof byte[])) {
/* 4043 */         setBytes(parameterIndex, (byte[])parameterObj);
/* 4044 */       } else if ((parameterObj instanceof java.sql.Date)) {
/* 4045 */         setDate(parameterIndex, (java.sql.Date)parameterObj);
/* 4046 */       } else if ((parameterObj instanceof Time)) {
/* 4047 */         setTime(parameterIndex, (Time)parameterObj);
/* 4048 */       } else if ((parameterObj instanceof Timestamp)) {
/* 4049 */         setTimestamp(parameterIndex, (Timestamp)parameterObj);
/* 4050 */       } else if ((parameterObj instanceof Boolean)) {
/* 4051 */         setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */       }
/* 4053 */       else if ((parameterObj instanceof InputStream)) {
/* 4054 */         setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
/* 4055 */       } else if ((parameterObj instanceof Blob)) {
/* 4056 */         setBlob(parameterIndex, (Blob)parameterObj);
/* 4057 */       } else if ((parameterObj instanceof Clob)) {
/* 4058 */         setClob(parameterIndex, (Clob)parameterObj);
/* 4059 */       } else if ((this.connection.getTreatUtilDateAsTimestamp()) && ((parameterObj instanceof java.util.Date)))
/*      */       {
/* 4061 */         setTimestamp(parameterIndex, new Timestamp(((java.util.Date)parameterObj).getTime()));
/*      */       }
/* 4063 */       else if ((parameterObj instanceof BigInteger)) {
/* 4064 */         setString(parameterIndex, parameterObj.toString());
/*      */       } else {
/* 4066 */         setSerializableObject(parameterIndex, parameterObj);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType)
/*      */     throws SQLException
/*      */   {
/* 4088 */     if (!(parameterObj instanceof BigDecimal)) {
/* 4089 */       setObject(parameterIndex, parameterObj, targetSqlType, 0);
/*      */     } else {
/* 4091 */       setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal)parameterObj).scale());
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
/*      */ 
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale)
/*      */     throws SQLException
/*      */   {
/* 4127 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4128 */       if (parameterObj == null) {
/* 4129 */         setNull(parameterIndex, 1111);
/*      */       } else {
/*      */         try {
/* 4132 */           switch (targetSqlType)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           case 16: 
/* 4152 */             if ((parameterObj instanceof Boolean)) {
/* 4153 */               setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */ 
/*      */             }
/* 4156 */             else if ((parameterObj instanceof String)) {
/* 4157 */               setBoolean(parameterIndex, ("true".equalsIgnoreCase((String)parameterObj)) || (!"0".equalsIgnoreCase((String)parameterObj)));
/*      */ 
/*      */ 
/*      */             }
/* 4161 */             else if ((parameterObj instanceof Number)) {
/* 4162 */               int intValue = ((Number)parameterObj).intValue();
/*      */               
/* 4164 */               setBoolean(parameterIndex, intValue != 0);
/*      */             }
/*      */             else
/*      */             {
/* 4168 */               throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009", getExceptionInterceptor());
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */             break;
/*      */           case -7: 
/*      */           case -6: 
/*      */           case -5: 
/*      */           case 2: 
/*      */           case 3: 
/*      */           case 4: 
/*      */           case 5: 
/*      */           case 6: 
/*      */           case 7: 
/*      */           case 8: 
/* 4184 */             setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
/*      */             
/* 4186 */             break;
/*      */           
/*      */           case -1: 
/*      */           case 1: 
/*      */           case 12: 
/* 4191 */             if ((parameterObj instanceof BigDecimal)) {
/* 4192 */               setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal)parameterObj)));
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/* 4198 */               setString(parameterIndex, parameterObj.toString());
/*      */             }
/*      */             
/* 4201 */             break;
/*      */           
/*      */ 
/*      */           case 2005: 
/* 4205 */             if ((parameterObj instanceof Clob)) {
/* 4206 */               setClob(parameterIndex, (Clob)parameterObj);
/*      */             } else {
/* 4208 */               setString(parameterIndex, parameterObj.toString());
/*      */             }
/*      */             
/* 4211 */             break;
/*      */           
/*      */ 
/*      */           case -4: 
/*      */           case -3: 
/*      */           case -2: 
/*      */           case 2004: 
/* 4218 */             if ((parameterObj instanceof byte[])) {
/* 4219 */               setBytes(parameterIndex, (byte[])parameterObj);
/* 4220 */             } else if ((parameterObj instanceof Blob)) {
/* 4221 */               setBlob(parameterIndex, (Blob)parameterObj);
/*      */             } else {
/* 4223 */               setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4230 */             break;
/*      */           case 91: 
/*      */           case 93: 
/*      */             java.util.Date parameterAsDate;
/*      */             
/*      */             java.util.Date parameterAsDate;
/*      */             
/* 4237 */             if ((parameterObj instanceof String)) {
/* 4238 */               ParsePosition pp = new ParsePosition(0);
/* 4239 */               DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, false), Locale.US);
/*      */               
/* 4241 */               parameterAsDate = sdf.parse((String)parameterObj, pp);
/*      */             } else {
/* 4243 */               parameterAsDate = (java.util.Date)parameterObj;
/*      */             }
/*      */             
/* 4246 */             switch (targetSqlType)
/*      */             {
/*      */             case 91: 
/* 4249 */               if ((parameterAsDate instanceof java.sql.Date)) {
/* 4250 */                 setDate(parameterIndex, (java.sql.Date)parameterAsDate);
/*      */               }
/*      */               else {
/* 4253 */                 setDate(parameterIndex, new java.sql.Date(parameterAsDate.getTime()));
/*      */               }
/*      */               
/*      */ 
/* 4257 */               break;
/*      */             
/*      */ 
/*      */             case 93: 
/* 4261 */               if ((parameterAsDate instanceof Timestamp)) {
/* 4262 */                 setTimestamp(parameterIndex, (Timestamp)parameterAsDate);
/*      */               }
/*      */               else {
/* 4265 */                 setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
/*      */               }
/*      */               
/*      */ 
/*      */               break;
/*      */             }
/*      */             
/*      */             
/* 4273 */             break;
/*      */           
/*      */ 
/*      */           case 92: 
/* 4277 */             if ((parameterObj instanceof String)) {
/* 4278 */               DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, true), Locale.US);
/*      */               
/* 4280 */               setTime(parameterIndex, new Time(sdf.parse((String)parameterObj).getTime()));
/*      */             }
/* 4282 */             else if ((parameterObj instanceof Timestamp)) {
/* 4283 */               Timestamp xT = (Timestamp)parameterObj;
/* 4284 */               setTime(parameterIndex, new Time(xT.getTime()));
/*      */             } else {
/* 4286 */               setTime(parameterIndex, (Time)parameterObj);
/*      */             }
/*      */             
/* 4289 */             break;
/*      */           
/*      */           case 1111: 
/* 4292 */             setSerializableObject(parameterIndex, parameterObj);
/*      */             
/* 4294 */             break;
/*      */           
/*      */           default: 
/* 4297 */             throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */         }
/*      */         catch (Exception ex) {
/* 4302 */           if ((ex instanceof SQLException)) {
/* 4303 */             throw ((SQLException)ex);
/*      */           }
/*      */           
/* 4306 */           SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000", getExceptionInterceptor());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4314 */           sqlEx.initCause(ex);
/*      */           
/* 4316 */           throw sqlEx;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet)
/*      */     throws SQLException
/*      */   {
/* 4325 */     BatchParams paramArg = (BatchParams)paramSet;
/*      */     
/* 4327 */     boolean[] isNullBatch = paramArg.isNull;
/* 4328 */     boolean[] isStreamBatch = paramArg.isStream;
/*      */     
/* 4330 */     for (int j = 0; j < isNullBatch.length; j++) {
/* 4331 */       if (isNullBatch[j] != 0) {
/* 4332 */         batchedStatement.setNull(batchedParamIndex++, 0);
/*      */       }
/* 4334 */       else if (isStreamBatch[j] != 0) {
/* 4335 */         batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
/*      */       }
/*      */       else
/*      */       {
/* 4339 */         ((PreparedStatement)batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4346 */     return batchedParamIndex;
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
/*      */   public void setRef(int i, Ref x)
/*      */     throws SQLException
/*      */   {
/* 4363 */     throw SQLError.notImplemented();
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
/*      */   private final void setSerializableObject(int parameterIndex, Object parameterObj)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 4382 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 4383 */       ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
/* 4384 */       objectOut.writeObject(parameterObj);
/* 4385 */       objectOut.flush();
/* 4386 */       objectOut.close();
/* 4387 */       bytesOut.flush();
/* 4388 */       bytesOut.close();
/*      */       
/* 4390 */       byte[] buf = bytesOut.toByteArray();
/* 4391 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
/* 4392 */       setBinaryStream(parameterIndex, bytesIn, buf.length);
/* 4393 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -2;
/*      */     } catch (Exception ex) {
/* 4395 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009", getExceptionInterceptor());
/*      */       
/*      */ 
/* 4398 */       sqlEx.initCause(ex);
/*      */       
/* 4400 */       throw sqlEx;
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
/*      */   public void setShort(int parameterIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 4417 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 4419 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 5;
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
/*      */   public void setString(int parameterIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 4436 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 4438 */       if (x == null) {
/* 4439 */         setNull(parameterIndex, 1);
/*      */       } else {
/* 4441 */         checkClosed();
/*      */         
/* 4443 */         int stringLength = x.length();
/*      */         
/* 4445 */         if (this.connection.isNoBackslashEscapesSet())
/*      */         {
/*      */ 
/* 4448 */           boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
/*      */           
/*      */ 
/* 4451 */           if (!needsHexEscape) {
/* 4452 */             byte[] parameterAsBytes = null;
/*      */             
/* 4454 */             StringBuffer quotedString = new StringBuffer(x.length() + 2);
/* 4455 */             quotedString.append('\'');
/* 4456 */             quotedString.append(x);
/* 4457 */             quotedString.append('\'');
/*      */             
/* 4459 */             if (!this.isLoadDataQuery) {
/* 4460 */               parameterAsBytes = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/* 4466 */               parameterAsBytes = StringUtils.getBytes(quotedString.toString());
/*      */             }
/*      */             
/* 4469 */             setInternal(parameterIndex, parameterAsBytes);
/*      */           } else {
/* 4471 */             byte[] parameterAsBytes = null;
/*      */             
/* 4473 */             if (!this.isLoadDataQuery) {
/* 4474 */               parameterAsBytes = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/* 4480 */               parameterAsBytes = StringUtils.getBytes(x);
/*      */             }
/*      */             
/* 4483 */             setBytes(parameterIndex, parameterAsBytes);
/*      */           }
/*      */           
/* 4486 */           return;
/*      */         }
/*      */         
/* 4489 */         String parameterAsString = x;
/* 4490 */         boolean needsQuoted = true;
/*      */         
/* 4492 */         if ((this.isLoadDataQuery) || (isEscapeNeededForString(x, stringLength))) {
/* 4493 */           needsQuoted = false;
/*      */           
/* 4495 */           StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
/*      */           
/* 4497 */           buf.append('\'');
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4506 */           for (int i = 0; i < stringLength; i++) {
/* 4507 */             char c = x.charAt(i);
/*      */             
/* 4509 */             switch (c) {
/*      */             case '\000': 
/* 4511 */               buf.append('\\');
/* 4512 */               buf.append('0');
/*      */               
/* 4514 */               break;
/*      */             
/*      */             case '\n': 
/* 4517 */               buf.append('\\');
/* 4518 */               buf.append('n');
/*      */               
/* 4520 */               break;
/*      */             
/*      */             case '\r': 
/* 4523 */               buf.append('\\');
/* 4524 */               buf.append('r');
/*      */               
/* 4526 */               break;
/*      */             
/*      */             case '\\': 
/* 4529 */               buf.append('\\');
/* 4530 */               buf.append('\\');
/*      */               
/* 4532 */               break;
/*      */             
/*      */             case '\'': 
/* 4535 */               buf.append('\\');
/* 4536 */               buf.append('\'');
/*      */               
/* 4538 */               break;
/*      */             
/*      */             case '"': 
/* 4541 */               if (this.usingAnsiMode) {
/* 4542 */                 buf.append('\\');
/*      */               }
/*      */               
/* 4545 */               buf.append('"');
/*      */               
/* 4547 */               break;
/*      */             
/*      */             case '\032': 
/* 4550 */               buf.append('\\');
/* 4551 */               buf.append('Z');
/*      */               
/* 4553 */               break;
/*      */             
/*      */ 
/*      */             case '¥': 
/*      */             case '₩': 
/* 4558 */               if (this.charsetEncoder != null) {
/* 4559 */                 CharBuffer cbuf = CharBuffer.allocate(1);
/* 4560 */                 ByteBuffer bbuf = ByteBuffer.allocate(1);
/* 4561 */                 cbuf.put(c);
/* 4562 */                 cbuf.position(0);
/* 4563 */                 this.charsetEncoder.encode(cbuf, bbuf, true);
/* 4564 */                 if (bbuf.get(0) == 92) {
/* 4565 */                   buf.append('\\');
/*      */                 }
/*      */               }
/*      */               break;
/*      */             }
/*      */             
/* 4571 */             buf.append(c);
/*      */           }
/*      */           
/*      */ 
/* 4575 */           buf.append('\'');
/*      */           
/* 4577 */           parameterAsString = buf.toString();
/*      */         }
/*      */         
/* 4580 */         byte[] parameterAsBytes = null;
/*      */         
/* 4582 */         if (!this.isLoadDataQuery) {
/* 4583 */           if (needsQuoted) {
/* 4584 */             parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 4589 */             parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 4596 */           parameterAsBytes = StringUtils.getBytes(parameterAsString);
/*      */         }
/*      */         
/* 4599 */         setInternal(parameterIndex, parameterAsBytes);
/*      */         
/* 4601 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 12;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isEscapeNeededForString(String x, int stringLength) {
/* 4607 */     boolean needsHexEscape = false;
/*      */     
/* 4609 */     for (int i = 0; i < stringLength; i++) {
/* 4610 */       char c = x.charAt(i);
/*      */       
/* 4612 */       switch (c)
/*      */       {
/*      */       case '\000': 
/* 4615 */         needsHexEscape = true;
/* 4616 */         break;
/*      */       
/*      */       case '\n': 
/* 4619 */         needsHexEscape = true;
/*      */         
/* 4621 */         break;
/*      */       
/*      */       case '\r': 
/* 4624 */         needsHexEscape = true;
/* 4625 */         break;
/*      */       
/*      */       case '\\': 
/* 4628 */         needsHexEscape = true;
/*      */         
/* 4630 */         break;
/*      */       
/*      */       case '\'': 
/* 4633 */         needsHexEscape = true;
/*      */         
/* 4635 */         break;
/*      */       
/*      */       case '"': 
/* 4638 */         needsHexEscape = true;
/*      */         
/* 4640 */         break;
/*      */       
/*      */       case '\032': 
/* 4643 */         needsHexEscape = true;
/*      */       }
/*      */       
/*      */       
/* 4647 */       if (needsHexEscape) {
/*      */         break;
/*      */       }
/*      */     }
/* 4651 */     return needsHexEscape;
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
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 4670 */     setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTime(int parameterIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 4687 */     setTimeInternal(parameterIndex, x, null, Util.getDefaultTimeZone(), false);
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
/*      */   private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4708 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4709 */       if (x == null) {
/* 4710 */         setNull(parameterIndex, 92);
/*      */       } else {
/* 4712 */         checkClosed();
/*      */         
/* 4714 */         if (!this.useLegacyDatetimeCode) {
/* 4715 */           newSetTimeInternal(parameterIndex, x, targetCalendar);
/*      */         } else {
/* 4717 */           Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */           
/* 4719 */           synchronized (sessionCalendar) {
/* 4720 */             x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4727 */           setInternal(parameterIndex, "'" + x.toString() + "'");
/*      */         }
/*      */         
/* 4730 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 92;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 4751 */     setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 4768 */     setTimestampInternal(parameterIndex, x, null, Util.getDefaultTimeZone(), false);
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
/*      */   private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4788 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4789 */       if (x == null) {
/* 4790 */         setNull(parameterIndex, 93);
/*      */       } else {
/* 4792 */         checkClosed();
/*      */         
/* 4794 */         if (!this.useLegacyDatetimeCode) {
/* 4795 */           newSetTimestampInternal(parameterIndex, x, targetCalendar);
/*      */         } else {
/* 4797 */           Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */           
/*      */ 
/*      */ 
/* 4801 */           synchronized (sessionCalendar) {
/* 4802 */             x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4809 */           if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
/* 4810 */             doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
/*      */           } else {
/* 4812 */             synchronized (this) {
/* 4813 */               if (this.tsdf == null) {
/* 4814 */                 this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */               }
/*      */               
/* 4817 */               StringBuffer buf = new StringBuffer();
/* 4818 */               buf.append(this.tsdf.format(x));
/*      */               
/* 4820 */               if (this.serverSupportsFracSecs) {
/* 4821 */                 int nanos = x.getNanos();
/*      */                 
/* 4823 */                 if (nanos != 0) {
/* 4824 */                   buf.append('.');
/* 4825 */                   buf.append(TimeUtil.formatNanos(nanos, this.serverSupportsFracSecs));
/*      */                 }
/*      */               }
/*      */               
/* 4829 */               buf.append('\'');
/*      */               
/* 4831 */               setInternal(parameterIndex, buf.toString());
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 4837 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 93;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar) throws SQLException
/*      */   {
/* 4844 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4845 */       if (this.tsdf == null) {
/* 4846 */         this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */       }
/*      */       
/* 4849 */       String timestampString = null;
/*      */       
/* 4851 */       if (targetCalendar != null) {
/* 4852 */         targetCalendar.setTime(x);
/* 4853 */         this.tsdf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4855 */         timestampString = this.tsdf.format(x);
/*      */       } else {
/* 4857 */         this.tsdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4858 */         timestampString = this.tsdf.format(x);
/*      */       }
/*      */       
/* 4861 */       StringBuffer buf = new StringBuffer();
/* 4862 */       buf.append(timestampString);
/* 4863 */       buf.append('.');
/* 4864 */       buf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
/* 4865 */       buf.append('\'');
/*      */       
/* 4867 */       setInternal(parameterIndex, buf.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   private void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar) throws SQLException
/*      */   {
/* 4873 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4874 */       if (this.tdf == null) {
/* 4875 */         this.tdf = new SimpleDateFormat("''HH:mm:ss''", Locale.US);
/*      */       }
/*      */       
/*      */ 
/* 4879 */       String timeString = null;
/*      */       
/* 4881 */       if (targetCalendar != null) {
/* 4882 */         targetCalendar.setTime(x);
/* 4883 */         this.tdf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4885 */         timeString = this.tdf.format(x);
/*      */       } else {
/* 4887 */         this.tdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4888 */         timeString = this.tdf.format(x);
/*      */       }
/*      */       
/* 4891 */       setInternal(parameterIndex, timeString);
/*      */     }
/*      */   }
/*      */   
/*      */   private void newSetDateInternal(int parameterIndex, java.sql.Date x, Calendar targetCalendar) throws SQLException
/*      */   {
/* 4897 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4898 */       if (this.ddf == null) {
/* 4899 */         this.ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */       }
/*      */       
/* 4902 */       String timeString = null;
/*      */       
/* 4904 */       if (targetCalendar != null) {
/* 4905 */         targetCalendar.setTime(x);
/* 4906 */         this.ddf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4908 */         timeString = this.ddf.format(x);
/*      */       } else {
/* 4910 */         this.ddf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4911 */         timeString = this.ddf.format(x);
/*      */       }
/*      */       
/* 4914 */       setInternal(parameterIndex, timeString);
/*      */     }
/*      */   }
/*      */   
/*      */   private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar) throws SQLException {
/* 4919 */     synchronized (checkClosed().getConnectionMutex()) {
/* 4920 */       Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 4925 */       synchronized (sessionCalendar2) {
/* 4926 */         java.util.Date oldTime = sessionCalendar2.getTime();
/*      */         try
/*      */         {
/* 4929 */           sessionCalendar2.setTime(x);
/*      */           
/* 4931 */           int year = sessionCalendar2.get(1);
/* 4932 */           int month = sessionCalendar2.get(2) + 1;
/* 4933 */           int date = sessionCalendar2.get(5);
/*      */           
/* 4935 */           int hour = sessionCalendar2.get(11);
/* 4936 */           int minute = sessionCalendar2.get(12);
/* 4937 */           int seconds = sessionCalendar2.get(13);
/*      */           
/* 4939 */           StringBuffer tsBuf = new StringBuffer();
/*      */           
/* 4941 */           tsBuf.append('\'');
/* 4942 */           tsBuf.append(year);
/*      */           
/* 4944 */           tsBuf.append("-");
/*      */           
/* 4946 */           if (month < 10) {
/* 4947 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4950 */           tsBuf.append(month);
/*      */           
/* 4952 */           tsBuf.append('-');
/*      */           
/* 4954 */           if (date < 10) {
/* 4955 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4958 */           tsBuf.append(date);
/*      */           
/* 4960 */           tsBuf.append(' ');
/*      */           
/* 4962 */           if (hour < 10) {
/* 4963 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4966 */           tsBuf.append(hour);
/*      */           
/* 4968 */           tsBuf.append(':');
/*      */           
/* 4970 */           if (minute < 10) {
/* 4971 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4974 */           tsBuf.append(minute);
/*      */           
/* 4976 */           tsBuf.append(':');
/*      */           
/* 4978 */           if (seconds < 10) {
/* 4979 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4982 */           tsBuf.append(seconds);
/*      */           
/* 4984 */           tsBuf.append('.');
/* 4985 */           tsBuf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
/* 4986 */           tsBuf.append('\'');
/*      */           
/* 4988 */           setInternal(parameterIndex, tsBuf.toString());
/*      */         }
/*      */         finally {
/* 4991 */           sessionCalendar.setTime(oldTime);
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 5023 */     if (x == null) {
/* 5024 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 5026 */       setBinaryStream(parameterIndex, x, length);
/*      */       
/* 5028 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setURL(int parameterIndex, URL arg)
/*      */     throws SQLException
/*      */   {
/* 5036 */     if (arg != null) {
/* 5037 */       setString(parameterIndex, arg.toString());
/*      */       
/* 5039 */       this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 70;
/*      */     } else {
/* 5041 */       setNull(parameterIndex, 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength)
/*      */     throws SQLException
/*      */   {
/* 5048 */     synchronized (checkClosed().getConnectionMutex()) {
/*      */       try {
/* 5050 */         if (this.streamConvertBuf == null) {
/* 5051 */           this.streamConvertBuf = new byte['က'];
/*      */         }
/*      */         
/* 5054 */         String connectionEncoding = this.connection.getEncoding();
/*      */         
/* 5056 */         boolean hexEscape = false;
/*      */         try
/*      */         {
/* 5059 */           if ((this.connection.isNoBackslashEscapesSet()) || ((this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding)) && (!this.connection.parserKnowsUnicode())))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 5064 */             hexEscape = true;
/*      */           }
/*      */         } catch (RuntimeException ex) {
/* 5067 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 5068 */           sqlEx.initCause(ex);
/* 5069 */           throw sqlEx;
/*      */         }
/*      */         
/* 5072 */         if (streamLength == -1) {
/* 5073 */           useLength = false;
/*      */         }
/*      */         
/* 5076 */         int bc = -1;
/*      */         
/* 5078 */         if (useLength) {
/* 5079 */           bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */         } else {
/* 5081 */           bc = readblock(in, this.streamConvertBuf);
/*      */         }
/*      */         
/* 5084 */         int lengthLeftToRead = streamLength - bc;
/*      */         
/* 5086 */         if (hexEscape) {
/* 5087 */           packet.writeStringNoNull("x");
/* 5088 */         } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
/* 5089 */           packet.writeStringNoNull("_binary");
/*      */         }
/*      */         
/* 5092 */         if (escape) {
/* 5093 */           packet.writeByte((byte)39);
/*      */         }
/*      */         
/* 5096 */         while (bc > 0) {
/* 5097 */           if (hexEscape) {
/* 5098 */             hexEscapeBlock(this.streamConvertBuf, packet, bc);
/* 5099 */           } else if (escape) {
/* 5100 */             escapeblockFast(this.streamConvertBuf, packet, bc);
/*      */           } else {
/* 5102 */             packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
/*      */           }
/*      */           
/* 5105 */           if (useLength) {
/* 5106 */             bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */             
/* 5108 */             if (bc > 0) {
/* 5109 */               lengthLeftToRead -= bc;
/*      */             }
/*      */           } else {
/* 5112 */             bc = readblock(in, this.streamConvertBuf);
/*      */           }
/*      */         }
/*      */         
/* 5116 */         if (escape) {
/* 5117 */           packet.writeByte((byte)39);
/*      */         }
/*      */       } finally {
/* 5120 */         if (this.connection.getAutoClosePStmtStreams()) {
/*      */           try {
/* 5122 */             in.close();
/*      */           }
/*      */           catch (IOException ioEx) {}
/*      */           
/*      */ 
/* 5127 */           in = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException
/*      */   {
/* 5135 */     synchronized (checkClosed().getConnectionMutex()) {
/*      */       try {
/* 5137 */         if (this.streamConvertBuf == null) {
/* 5138 */           this.streamConvertBuf = new byte['က'];
/*      */         }
/* 5140 */         if (streamLength == -1) {
/* 5141 */           useLength = false;
/*      */         }
/*      */         
/* 5144 */         ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */         
/* 5146 */         int bc = -1;
/*      */         
/* 5148 */         if (useLength) {
/* 5149 */           bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */         } else {
/* 5151 */           bc = readblock(in, this.streamConvertBuf);
/*      */         }
/*      */         
/* 5154 */         int lengthLeftToRead = streamLength - bc;
/*      */         
/* 5156 */         if (escape) {
/* 5157 */           if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 5158 */             bytesOut.write(95);
/* 5159 */             bytesOut.write(98);
/* 5160 */             bytesOut.write(105);
/* 5161 */             bytesOut.write(110);
/* 5162 */             bytesOut.write(97);
/* 5163 */             bytesOut.write(114);
/* 5164 */             bytesOut.write(121);
/*      */           }
/*      */           
/* 5167 */           bytesOut.write(39);
/*      */         }
/*      */         
/* 5170 */         while (bc > 0) {
/* 5171 */           if (escape) {
/* 5172 */             escapeblockFast(this.streamConvertBuf, bytesOut, bc);
/*      */           } else {
/* 5174 */             bytesOut.write(this.streamConvertBuf, 0, bc);
/*      */           }
/*      */           
/* 5177 */           if (useLength) {
/* 5178 */             bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */             
/* 5180 */             if (bc > 0) {
/* 5181 */               lengthLeftToRead -= bc;
/*      */             }
/*      */           } else {
/* 5184 */             bc = readblock(in, this.streamConvertBuf);
/*      */           }
/*      */         }
/*      */         
/* 5188 */         if (escape) {
/* 5189 */           bytesOut.write(39);
/*      */         }
/*      */         
/* 5192 */         byte[] arrayOfByte = bytesOut.toByteArray();
/*      */         
/* 5194 */         if (this.connection.getAutoClosePStmtStreams()) {
/*      */           try {
/* 5196 */             in.close();
/*      */           }
/*      */           catch (IOException ioEx) {}
/*      */           
/*      */ 
/* 5201 */           in = null; } return arrayOfByte;
/*      */       }
/*      */       finally
/*      */       {
/* 5194 */         if (this.connection.getAutoClosePStmtStreams()) {
/*      */           try {
/* 5196 */             in.close();
/*      */           }
/*      */           catch (IOException ioEx) {}
/*      */           
/*      */ 
/* 5201 */           in = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 5213 */     StringBuffer buf = new StringBuffer();
/* 5214 */     buf.append(super.toString());
/* 5215 */     buf.append(": ");
/*      */     try
/*      */     {
/* 5218 */       buf.append(asSql());
/*      */     } catch (SQLException sqlEx) {
/* 5220 */       buf.append("EXCEPTION: " + sqlEx.toString());
/*      */     }
/*      */     
/* 5223 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getParameterIndexOffset()
/*      */   {
/* 5235 */     return 0;
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/* 5239 */     setAsciiStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5243 */     setAsciiStream(parameterIndex, x, (int)length);
/* 5244 */     this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/* 5248 */     setBinaryStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5252 */     setBinaryStream(parameterIndex, x, (int)length);
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/* 5256 */     setBinaryStream(parameterIndex, inputStream);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/* 5260 */     setCharacterStream(parameterIndex, reader, -1);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5264 */     setCharacterStream(parameterIndex, reader, (int)length);
/*      */   }
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader) throws SQLException
/*      */   {
/* 5269 */     setCharacterStream(parameterIndex, reader);
/*      */   }
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/* 5275 */     setCharacterStream(parameterIndex, reader, length);
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/* 5279 */     setNCharacterStream(parameterIndex, value, -1L);
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
/*      */   public void setNString(int parameterIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 5297 */     synchronized (checkClosed().getConnectionMutex()) {
/* 5298 */       if ((this.charEncoding.equalsIgnoreCase("UTF-8")) || (this.charEncoding.equalsIgnoreCase("utf8")))
/*      */       {
/* 5300 */         setString(parameterIndex, x);
/* 5301 */         return;
/*      */       }
/*      */       
/*      */ 
/* 5305 */       if (x == null) {
/* 5306 */         setNull(parameterIndex, 1);
/*      */       } else {
/* 5308 */         int stringLength = x.length();
/*      */         
/*      */ 
/*      */ 
/* 5312 */         StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D + 4.0D));
/* 5313 */         buf.append("_utf8");
/* 5314 */         buf.append('\'');
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5323 */         for (int i = 0; i < stringLength; i++) {
/* 5324 */           char c = x.charAt(i);
/*      */           
/* 5326 */           switch (c) {
/*      */           case '\000': 
/* 5328 */             buf.append('\\');
/* 5329 */             buf.append('0');
/*      */             
/* 5331 */             break;
/*      */           
/*      */           case '\n': 
/* 5334 */             buf.append('\\');
/* 5335 */             buf.append('n');
/*      */             
/* 5337 */             break;
/*      */           
/*      */           case '\r': 
/* 5340 */             buf.append('\\');
/* 5341 */             buf.append('r');
/*      */             
/* 5343 */             break;
/*      */           
/*      */           case '\\': 
/* 5346 */             buf.append('\\');
/* 5347 */             buf.append('\\');
/*      */             
/* 5349 */             break;
/*      */           
/*      */           case '\'': 
/* 5352 */             buf.append('\\');
/* 5353 */             buf.append('\'');
/*      */             
/* 5355 */             break;
/*      */           
/*      */           case '"': 
/* 5358 */             if (this.usingAnsiMode) {
/* 5359 */               buf.append('\\');
/*      */             }
/*      */             
/* 5362 */             buf.append('"');
/*      */             
/* 5364 */             break;
/*      */           
/*      */           case '\032': 
/* 5367 */             buf.append('\\');
/* 5368 */             buf.append('Z');
/*      */             
/* 5370 */             break;
/*      */           
/*      */           default: 
/* 5373 */             buf.append(c);
/*      */           }
/*      */           
/*      */         }
/* 5377 */         buf.append('\'');
/*      */         
/* 5379 */         String parameterAsString = buf.toString();
/*      */         
/* 5381 */         byte[] parameterAsBytes = null;
/*      */         
/* 5383 */         if (!this.isLoadDataQuery) {
/* 5384 */           parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 5390 */           parameterAsBytes = StringUtils.getBytes(parameterAsString);
/*      */         }
/*      */         
/* 5393 */         setInternal(parameterIndex, parameterAsBytes);
/*      */         
/* 5395 */         this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -9;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNCharacterStream(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/* 5424 */     synchronized (checkClosed().getConnectionMutex()) {
/*      */       try {
/* 5426 */         if (reader == null) {
/* 5427 */           setNull(parameterIndex, -1);
/*      */         }
/*      */         else {
/* 5430 */           char[] c = null;
/* 5431 */           int len = 0;
/*      */           
/* 5433 */           boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5438 */           if ((useLength) && (length != -1L)) {
/* 5439 */             c = new char[(int)length];
/*      */             
/* 5441 */             int numCharsRead = readFully(reader, c, (int)length);
/*      */             
/*      */ 
/*      */ 
/* 5445 */             setNString(parameterIndex, new String(c, 0, numCharsRead));
/*      */           }
/*      */           else {
/* 5448 */             c = new char['က'];
/*      */             
/* 5450 */             StringBuffer buf = new StringBuffer();
/*      */             
/* 5452 */             while ((len = reader.read(c)) != -1) {
/* 5453 */               buf.append(c, 0, len);
/*      */             }
/*      */             
/* 5456 */             setNString(parameterIndex, buf.toString());
/*      */           }
/*      */           
/* 5459 */           this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2011;
/*      */         }
/*      */       } catch (IOException ioEx) {
/* 5462 */         throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader) throws SQLException
/*      */   {
/* 5469 */     setNCharacterStream(parameterIndex, reader);
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
/*      */   public void setNClob(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/* 5487 */     if (reader == null) {
/* 5488 */       setNull(parameterIndex, -1);
/*      */     } else {
/* 5490 */       setNCharacterStream(parameterIndex, reader, length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   class EmulatedPreparedStatementBindings
/*      */     implements ParameterBindings
/*      */   {
/*      */     private ResultSetImpl bindingsAsRs;
/*      */     
/*      */     private boolean[] parameterIsNull;
/*      */     
/*      */ 
/*      */     EmulatedPreparedStatementBindings()
/*      */       throws SQLException
/*      */     {
/* 5506 */       List<ResultSetRow> rows = new ArrayList();
/* 5507 */       this.parameterIsNull = new boolean[PreparedStatement.this.parameterCount];
/* 5508 */       System.arraycopy(PreparedStatement.this.isNull, 0, this.parameterIsNull, 0, PreparedStatement.this.parameterCount);
/*      */       
/*      */ 
/* 5511 */       byte[][] rowData = new byte[PreparedStatement.this.parameterCount][];
/* 5512 */       Field[] typeMetadata = new Field[PreparedStatement.this.parameterCount];
/*      */       
/* 5514 */       for (int i = 0; i < PreparedStatement.this.parameterCount; i++) {
/* 5515 */         if (PreparedStatement.this.batchCommandIndex == -1) {
/* 5516 */           rowData[i] = PreparedStatement.this.getBytesRepresentation(i);
/*      */         } else {
/* 5518 */           rowData[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, PreparedStatement.this.batchCommandIndex);
/*      */         }
/* 5520 */         int charsetIndex = 0;
/*      */         
/* 5522 */         if ((PreparedStatement.this.parameterTypes[i] == -2) || (PreparedStatement.this.parameterTypes[i] == 2004))
/*      */         {
/* 5524 */           charsetIndex = 63;
/*      */         } else {
/*      */           try {
/* 5527 */             String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);
/*      */             
/*      */ 
/* 5530 */             charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(mysqlEncodingName);
/*      */           }
/*      */           catch (SQLException ex) {
/* 5533 */             throw ex;
/*      */           } catch (RuntimeException ex) {
/* 5535 */             SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 5536 */             sqlEx.initCause(ex);
/* 5537 */             throw sqlEx;
/*      */           }
/*      */         }
/*      */         
/* 5541 */         Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, PreparedStatement.this.parameterTypes[i], rowData[i].length);
/*      */         
/*      */ 
/* 5544 */         parameterMetadata.setConnection(PreparedStatement.this.connection);
/* 5545 */         typeMetadata[i] = parameterMetadata;
/*      */       }
/*      */       
/* 5548 */       rows.add(new ByteArrayRow(rowData, PreparedStatement.this.getExceptionInterceptor()));
/*      */       
/* 5550 */       this.bindingsAsRs = new ResultSetImpl(PreparedStatement.this.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), PreparedStatement.this.connection, null);
/*      */       
/* 5552 */       this.bindingsAsRs.next();
/*      */     }
/*      */     
/*      */     public Array getArray(int parameterIndex) throws SQLException {
/* 5556 */       return this.bindingsAsRs.getArray(parameterIndex);
/*      */     }
/*      */     
/*      */     public InputStream getAsciiStream(int parameterIndex) throws SQLException
/*      */     {
/* 5561 */       return this.bindingsAsRs.getAsciiStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/* 5565 */       return this.bindingsAsRs.getBigDecimal(parameterIndex);
/*      */     }
/*      */     
/*      */     public InputStream getBinaryStream(int parameterIndex) throws SQLException
/*      */     {
/* 5570 */       return this.bindingsAsRs.getBinaryStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Blob getBlob(int parameterIndex) throws SQLException {
/* 5574 */       return this.bindingsAsRs.getBlob(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean getBoolean(int parameterIndex) throws SQLException {
/* 5578 */       return this.bindingsAsRs.getBoolean(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte getByte(int parameterIndex) throws SQLException {
/* 5582 */       return this.bindingsAsRs.getByte(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte[] getBytes(int parameterIndex) throws SQLException {
/* 5586 */       return this.bindingsAsRs.getBytes(parameterIndex);
/*      */     }
/*      */     
/*      */     public Reader getCharacterStream(int parameterIndex) throws SQLException
/*      */     {
/* 5591 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Clob getClob(int parameterIndex) throws SQLException {
/* 5595 */       return this.bindingsAsRs.getClob(parameterIndex);
/*      */     }
/*      */     
/*      */     public java.sql.Date getDate(int parameterIndex) throws SQLException {
/* 5599 */       return this.bindingsAsRs.getDate(parameterIndex);
/*      */     }
/*      */     
/*      */     public double getDouble(int parameterIndex) throws SQLException {
/* 5603 */       return this.bindingsAsRs.getDouble(parameterIndex);
/*      */     }
/*      */     
/*      */     public float getFloat(int parameterIndex) throws SQLException {
/* 5607 */       return this.bindingsAsRs.getFloat(parameterIndex);
/*      */     }
/*      */     
/*      */     public int getInt(int parameterIndex) throws SQLException {
/* 5611 */       return this.bindingsAsRs.getInt(parameterIndex);
/*      */     }
/*      */     
/*      */     public long getLong(int parameterIndex) throws SQLException {
/* 5615 */       return this.bindingsAsRs.getLong(parameterIndex);
/*      */     }
/*      */     
/*      */     public Reader getNCharacterStream(int parameterIndex) throws SQLException
/*      */     {
/* 5620 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Reader getNClob(int parameterIndex) throws SQLException {
/* 5624 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Object getObject(int parameterIndex) throws SQLException {
/* 5628 */       PreparedStatement.this.checkBounds(parameterIndex, 0);
/*      */       
/* 5630 */       if (this.parameterIsNull[(parameterIndex - 1)] != 0) {
/* 5631 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5638 */       switch (PreparedStatement.this.parameterTypes[(parameterIndex - 1)]) {
/*      */       case -6: 
/* 5640 */         return Byte.valueOf(getByte(parameterIndex));
/*      */       case 5: 
/* 5642 */         return Short.valueOf(getShort(parameterIndex));
/*      */       case 4: 
/* 5644 */         return Integer.valueOf(getInt(parameterIndex));
/*      */       case -5: 
/* 5646 */         return Long.valueOf(getLong(parameterIndex));
/*      */       case 6: 
/* 5648 */         return Float.valueOf(getFloat(parameterIndex));
/*      */       case 8: 
/* 5650 */         return Double.valueOf(getDouble(parameterIndex));
/*      */       }
/* 5652 */       return this.bindingsAsRs.getObject(parameterIndex);
/*      */     }
/*      */     
/*      */     public Ref getRef(int parameterIndex) throws SQLException
/*      */     {
/* 5657 */       return this.bindingsAsRs.getRef(parameterIndex);
/*      */     }
/*      */     
/*      */     public short getShort(int parameterIndex) throws SQLException {
/* 5661 */       return this.bindingsAsRs.getShort(parameterIndex);
/*      */     }
/*      */     
/*      */     public String getString(int parameterIndex) throws SQLException {
/* 5665 */       return this.bindingsAsRs.getString(parameterIndex);
/*      */     }
/*      */     
/*      */     public Time getTime(int parameterIndex) throws SQLException {
/* 5669 */       return this.bindingsAsRs.getTime(parameterIndex);
/*      */     }
/*      */     
/*      */     public Timestamp getTimestamp(int parameterIndex) throws SQLException {
/* 5673 */       return this.bindingsAsRs.getTimestamp(parameterIndex);
/*      */     }
/*      */     
/*      */     public URL getURL(int parameterIndex) throws SQLException {
/* 5677 */       return this.bindingsAsRs.getURL(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean isNull(int parameterIndex) throws SQLException {
/* 5681 */       PreparedStatement.this.checkBounds(parameterIndex, 0);
/*      */       
/* 5683 */       return this.parameterIsNull[(parameterIndex - 1)];
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
/*      */   public int getUpdateCount()
/*      */     throws SQLException
/*      */   {
/* 5706 */     int count = super.getUpdateCount();
/*      */     
/* 5708 */     if ((containsOnDuplicateKeyUpdateInSQL()) && (this.compensateForOnDuplicateKeyUpdate))
/*      */     {
/* 5710 */       if ((count == 2) || (count == 0)) {
/* 5711 */         count = 1;
/*      */       }
/*      */     }
/*      */     
/* 5715 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos)
/*      */   {
/* 5722 */     boolean rewritableOdku = true;
/*      */     
/* 5724 */     if (isOnDuplicateKeyUpdate) {
/* 5725 */       int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");
/*      */       
/*      */ 
/* 5728 */       if (updateClausePos != -1) {
/* 5729 */         rewritableOdku = StringUtils.indexOfIgnoreCaseRespectMarker(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", false) == -1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5736 */     return (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos)) && (StringUtils.indexOfIgnoreCaseRespectMarker(statementStartPos, sql, "SELECT", "\"'`", "\"'`", false) == -1) && (rewritableOdku);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected Buffer fillSendPacket()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 63	com/mysql/jdbc/PreparedStatement:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 64 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: aload_0
/*      */     //   14: getfield 25	com/mysql/jdbc/PreparedStatement:parameterValues	[[B
/*      */     //   17: aload_0
/*      */     //   18: getfield 23	com/mysql/jdbc/PreparedStatement:parameterStreams	[Ljava/io/InputStream;
/*      */     //   21: aload_0
/*      */     //   22: getfield 20	com/mysql/jdbc/PreparedStatement:isStream	[Z
/*      */     //   25: aload_0
/*      */     //   26: getfield 29	com/mysql/jdbc/PreparedStatement:streamLengths	[I
/*      */     //   29: invokevirtual 250	com/mysql/jdbc/PreparedStatement:fillSendPacket	([[B[Ljava/io/InputStream;[Z[I)Lcom/mysql/jdbc/Buffer;
/*      */     //   32: aload_1
/*      */     //   33: monitorexit
/*      */     //   34: areturn
/*      */     //   35: astore_2
/*      */     //   36: aload_1
/*      */     //   37: monitorexit
/*      */     //   38: aload_2
/*      */     //   39: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2492	-> byte code offset #0
/*      */     //   Java source line #2493	-> byte code offset #12
/*      */     //   Java source line #2495	-> byte code offset #35
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	40	0	this	PreparedStatement
/*      */     //   10	27	1	Ljava/lang/Object;	Object
/*      */     //   35	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	34	35	finally
/*      */     //   35	38	35	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected boolean isSelectQuery()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 63	com/mysql/jdbc/PreparedStatement:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 64 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 22	com/mysql/jdbc/PreparedStatement:originalSql	Ljava/lang/String;
/*      */     //   16: ldc_w 320
/*      */     //   19: ldc_w 320
/*      */     //   22: iconst_1
/*      */     //   23: iconst_0
/*      */     //   24: iconst_1
/*      */     //   25: iconst_1
/*      */     //   26: invokestatic 321	com/mysql/jdbc/StringUtils:stripComments	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZ)Ljava/lang/String;
/*      */     //   29: ldc_w 322
/*      */     //   32: invokestatic 323	com/mysql/jdbc/StringUtils:startsWithIgnoreCaseAndWs	(Ljava/lang/String;Ljava/lang/String;)Z
/*      */     //   35: aload_1
/*      */     //   36: monitorexit
/*      */     //   37: ireturn
/*      */     //   38: astore_2
/*      */     //   39: aload_1
/*      */     //   40: monitorexit
/*      */     //   41: aload_2
/*      */     //   42: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2978	-> byte code offset #0
/*      */     //   Java source line #2979	-> byte code offset #12
/*      */     //   Java source line #2983	-> byte code offset #38
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	43	0	this	PreparedStatement
/*      */     //   10	30	1	Ljava/lang/Object;	Object
/*      */     //   38	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	37	38	finally
/*      */     //   38	41	38	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   boolean isNull(int paramIndex)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 63	com/mysql/jdbc/PreparedStatement:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 64 1 0
/*      */     //   9: dup
/*      */     //   10: astore_2
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 19	com/mysql/jdbc/PreparedStatement:isNull	[Z
/*      */     //   16: iload_1
/*      */     //   17: baload
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: ireturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3071	-> byte code offset #0
/*      */     //   Java source line #3072	-> byte code offset #12
/*      */     //   Java source line #3073	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	PreparedStatement
/*      */     //   0	26	1	paramIndex	int
/*      */     //   10	13	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ParameterBindings getParameterBindings()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 63	com/mysql/jdbc/PreparedStatement:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 64 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: new 576	com/mysql/jdbc/PreparedStatement$EmulatedPreparedStatementBindings
/*      */     //   15: dup
/*      */     //   16: aload_0
/*      */     //   17: invokespecial 577	com/mysql/jdbc/PreparedStatement$EmulatedPreparedStatementBindings:<init>	(Lcom/mysql/jdbc/PreparedStatement;)V
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: areturn
/*      */     //   23: astore_2
/*      */     //   24: aload_1
/*      */     //   25: monitorexit
/*      */     //   26: aload_2
/*      */     //   27: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5495	-> byte code offset #0
/*      */     //   Java source line #5496	-> byte code offset #12
/*      */     //   Java source line #5497	-> byte code offset #23
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	28	0	this	PreparedStatement
/*      */     //   10	15	1	Ljava/lang/Object;	Object
/*      */     //   23	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	22	23	finally
/*      */     //   23	26	23	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String getPreparedSql()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 63	com/mysql/jdbc/PreparedStatement:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 64 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 34	com/mysql/jdbc/PreparedStatement:rewrittenBatchSize	I
/*      */     //   16: ifne +10 -> 26
/*      */     //   19: aload_0
/*      */     //   20: getfield 22	com/mysql/jdbc/PreparedStatement:originalSql	Ljava/lang/String;
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: areturn
/*      */     //   26: aload_0
/*      */     //   27: getfield 53	com/mysql/jdbc/PreparedStatement:parseInfo	Lcom/mysql/jdbc/PreparedStatement$ParseInfo;
/*      */     //   30: aload_0
/*      */     //   31: getfield 53	com/mysql/jdbc/PreparedStatement:parseInfo	Lcom/mysql/jdbc/PreparedStatement$ParseInfo;
/*      */     //   34: invokevirtual 578	com/mysql/jdbc/PreparedStatement$ParseInfo:getSqlForBatch	(Lcom/mysql/jdbc/PreparedStatement$ParseInfo;)Ljava/lang/String;
/*      */     //   37: aload_1
/*      */     //   38: monitorexit
/*      */     //   39: areturn
/*      */     //   40: astore_2
/*      */     //   41: new 92	java/lang/RuntimeException
/*      */     //   44: dup
/*      */     //   45: aload_2
/*      */     //   46: invokespecial 579	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   49: athrow
/*      */     //   50: astore_3
/*      */     //   51: aload_1
/*      */     //   52: monitorexit
/*      */     //   53: aload_3
/*      */     //   54: athrow
/*      */     //   55: astore_1
/*      */     //   56: new 92	java/lang/RuntimeException
/*      */     //   59: dup
/*      */     //   60: aload_1
/*      */     //   61: invokespecial 579	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   64: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5689	-> byte code offset #0
/*      */     //   Java source line #5690	-> byte code offset #12
/*      */     //   Java source line #5691	-> byte code offset #19
/*      */     //   Java source line #5695	-> byte code offset #26
/*      */     //   Java source line #5696	-> byte code offset #40
/*      */     //   Java source line #5697	-> byte code offset #41
/*      */     //   Java source line #5699	-> byte code offset #50
/*      */     //   Java source line #5700	-> byte code offset #55
/*      */     //   Java source line #5701	-> byte code offset #56
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	65	0	this	PreparedStatement
/*      */     //   55	6	1	e	SQLException
/*      */     //   40	6	2	e	UnsupportedEncodingException
/*      */     //   50	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   26	37	40	java/io/UnsupportedEncodingException
/*      */     //   12	25	50	finally
/*      */     //   26	39	50	finally
/*      */     //   40	53	50	finally
/*      */     //   0	25	55	java/sql/SQLException
/*      */     //   26	39	55	java/sql/SQLException
/*      */     //   40	55	55	java/sql/SQLException
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\PreparedStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */