/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.LogUtils;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ResultSetImpl
/*      */   implements ResultSetInternalMethods
/*      */ {
/*      */   private static final Constructor<?> JDBC_4_RS_4_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_RS_6_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_UPD_RS_6_ARG_CTOR;
/*      */   protected static final double MIN_DIFF_PREC;
/*      */   protected static final double MAX_DIFF_PREC;
/*      */   static int resultCounter;
/*      */   
/*      */   protected static BigInteger convertLongToUlong(long longVal)
/*      */   {
/*  180 */     byte[] asBytes = new byte[8];
/*  181 */     asBytes[7] = ((byte)(int)(longVal & 0xFF));
/*  182 */     asBytes[6] = ((byte)(int)(longVal >>> 8));
/*  183 */     asBytes[5] = ((byte)(int)(longVal >>> 16));
/*  184 */     asBytes[4] = ((byte)(int)(longVal >>> 24));
/*  185 */     asBytes[3] = ((byte)(int)(longVal >>> 32));
/*  186 */     asBytes[2] = ((byte)(int)(longVal >>> 40));
/*  187 */     asBytes[1] = ((byte)(int)(longVal >>> 48));
/*  188 */     asBytes[0] = ((byte)(int)(longVal >>> 56));
/*      */     
/*  190 */     return new BigInteger(1, asBytes);
/*      */   }
/*      */   
/*      */ 
/*  194 */   protected String catalog = null;
/*      */   
/*      */ 
/*  197 */   protected Map<String, Integer> columnLabelToIndex = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  203 */   protected Map<String, Integer> columnToIndexCache = null;
/*      */   
/*      */ 
/*  206 */   protected boolean[] columnUsed = null;
/*      */   
/*      */ 
/*      */   protected volatile MySQLConnection connection;
/*      */   
/*      */ 
/*  212 */   protected long connectionId = 0L;
/*      */   
/*      */ 
/*  215 */   protected int currentRow = -1;
/*      */   
/*      */ 
/*      */   TimeZone defaultTimeZone;
/*      */   
/*  220 */   protected boolean doingUpdates = false;
/*      */   
/*  222 */   protected ProfilerEventHandler eventSink = null;
/*      */   
/*  224 */   Calendar fastDateCal = null;
/*      */   
/*      */ 
/*  227 */   protected int fetchDirection = 1000;
/*      */   
/*      */ 
/*  230 */   protected int fetchSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Field[] fields;
/*      */   
/*      */ 
/*      */ 
/*      */   protected char firstCharOfQuery;
/*      */   
/*      */ 
/*      */ 
/*  243 */   protected Map<String, Integer> fullColumnNameToIndex = null;
/*      */   
/*  245 */   protected Map<String, Integer> columnNameToIndex = null;
/*      */   
/*  247 */   protected boolean hasBuiltIndexMapping = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  253 */   protected boolean isBinaryEncoded = false;
/*      */   
/*      */ 
/*  256 */   protected boolean isClosed = false;
/*      */   
/*  258 */   protected ResultSetInternalMethods nextResultSet = null;
/*      */   
/*      */ 
/*  261 */   protected boolean onInsertRow = false;
/*      */   
/*      */ 
/*      */ 
/*      */   protected StatementImpl owningStatement;
/*      */   
/*      */ 
/*      */ 
/*      */   protected String pointOfOrigin;
/*      */   
/*      */ 
/*  272 */   protected boolean profileSql = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  278 */   protected boolean reallyResult = false;
/*      */   
/*      */ 
/*      */   protected int resultId;
/*      */   
/*      */ 
/*  284 */   protected int resultSetConcurrency = 0;
/*      */   
/*      */ 
/*  287 */   protected int resultSetType = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected RowData rowData;
/*      */   
/*      */ 
/*      */ 
/*  296 */   protected String serverInfo = null;
/*      */   
/*      */ 
/*      */   PreparedStatement statementUsedForFetchingRows;
/*      */   
/*  301 */   protected ResultSetRow thisRow = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long updateCount;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  315 */   protected long updateId = -1L;
/*      */   
/*  317 */   private boolean useStrictFloatingPoint = false;
/*      */   
/*  319 */   protected boolean useUsageAdvisor = false;
/*      */   
/*      */ 
/*  322 */   protected SQLWarning warningChain = null;
/*      */   
/*      */ 
/*  325 */   protected boolean wasNullFlag = false;
/*      */   
/*      */   protected Statement wrapperStatement;
/*      */   
/*      */   protected boolean retainOwningStatement;
/*      */   
/*  331 */   protected Calendar gmtCalendar = null;
/*      */   
/*  333 */   protected boolean useFastDateParsing = false;
/*      */   
/*  335 */   private boolean padCharsWithSpace = false;
/*      */   
/*      */   private boolean jdbcCompliantTruncationForReads;
/*      */   
/*  339 */   private boolean useFastIntParsing = true;
/*      */   private boolean useColumnNamesInFindColumn;
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */   static final char[] EMPTY_SPACE;
/*      */   
/*      */   static
/*      */   {
/*  126 */     if (Util.isJdbc4()) {
/*      */       try {
/*  128 */         JDBC_4_RS_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4ResultSet").getConstructor(new Class[] { Long.TYPE, Long.TYPE, MySQLConnection.class, StatementImpl.class });
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  133 */         JDBC_4_RS_6_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4ResultSet").getConstructor(new Class[] { String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class });
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */         JDBC_4_UPD_RS_6_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4UpdatableResultSet").getConstructor(new Class[] { String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class });
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*      */ 
/*      */ 
/*  147 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  149 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  151 */         throw new RuntimeException(e);
/*      */       }
/*      */     } else {
/*  154 */       JDBC_4_RS_4_ARG_CTOR = null;
/*  155 */       JDBC_4_RS_6_ARG_CTOR = null;
/*  156 */       JDBC_4_UPD_RS_6_ARG_CTOR = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  163 */     MIN_DIFF_PREC = Float.parseFloat(Float.toString(Float.MIN_VALUE)) - Double.parseDouble(Float.toString(Float.MIN_VALUE));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */     MAX_DIFF_PREC = Float.parseFloat(Float.toString(Float.MAX_VALUE)) - Double.parseDouble(Float.toString(Float.MAX_VALUE));
/*      */     
/*      */ 
/*      */ 
/*  173 */     resultCounter = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  344 */     EMPTY_SPACE = new char['ÿ'];
/*      */     
/*      */ 
/*  347 */     for (int i = 0; i < EMPTY_SPACE.length; i++) {
/*  348 */       EMPTY_SPACE[i] = ' ';
/*      */     }
/*      */   }
/*      */   
/*      */   protected static ResultSetImpl getInstance(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException
/*      */   {
/*  354 */     if (!Util.isJdbc4()) {
/*  355 */       return new ResultSetImpl(updateCount, updateID, conn, creatorStmt);
/*      */     }
/*      */     
/*  358 */     return (ResultSetImpl)Util.handleNewInstance(JDBC_4_RS_4_ARG_CTOR, new Object[] { Long.valueOf(updateCount), Long.valueOf(updateID), conn, creatorStmt }, conn.getExceptionInterceptor());
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
/*      */   protected static ResultSetImpl getInstance(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt, boolean isUpdatable)
/*      */     throws SQLException
/*      */   {
/*  374 */     if (!Util.isJdbc4()) {
/*  375 */       if (!isUpdatable) {
/*  376 */         return new ResultSetImpl(catalog, fields, tuples, conn, creatorStmt);
/*      */       }
/*      */       
/*  379 */       return new UpdatableResultSet(catalog, fields, tuples, conn, creatorStmt);
/*      */     }
/*      */     
/*      */ 
/*  383 */     if (!isUpdatable) {
/*  384 */       return (ResultSetImpl)Util.handleNewInstance(JDBC_4_RS_6_ARG_CTOR, new Object[] { catalog, fields, tuples, conn, creatorStmt }, conn.getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  389 */     return (ResultSetImpl)Util.handleNewInstance(JDBC_4_UPD_RS_6_ARG_CTOR, new Object[] { catalog, fields, tuples, conn, creatorStmt }, conn.getExceptionInterceptor());
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
/*      */   public ResultSetImpl(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt)
/*      */   {
/*  407 */     this.updateCount = updateCount;
/*  408 */     this.updateId = updateID;
/*  409 */     this.reallyResult = false;
/*  410 */     this.fields = new Field[0];
/*      */     
/*  412 */     this.connection = conn;
/*  413 */     this.owningStatement = creatorStmt;
/*      */     
/*  415 */     this.retainOwningStatement = false;
/*      */     
/*  417 */     if (this.connection != null) {
/*  418 */       this.exceptionInterceptor = this.connection.getExceptionInterceptor();
/*      */       
/*  420 */       this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
/*      */       
/*      */ 
/*  423 */       this.connectionId = this.connection.getId();
/*  424 */       this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
/*  425 */       this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
/*      */       
/*  427 */       this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
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
/*      */   public ResultSetImpl(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt)
/*      */     throws SQLException
/*      */   {
/*  450 */     this.connection = conn;
/*      */     
/*  452 */     this.retainOwningStatement = false;
/*      */     
/*  454 */     if (this.connection != null) {
/*  455 */       this.useStrictFloatingPoint = this.connection.getStrictFloatingPoint();
/*      */       
/*  457 */       setDefaultTimeZone(this.connection.getDefaultTimeZone());
/*  458 */       this.connectionId = this.connection.getId();
/*  459 */       this.useFastDateParsing = this.connection.getUseFastDateParsing();
/*  460 */       this.profileSql = this.connection.getProfileSql();
/*  461 */       this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
/*      */       
/*  463 */       this.jdbcCompliantTruncationForReads = this.connection.getJdbcCompliantTruncationForReads();
/*  464 */       this.useFastIntParsing = this.connection.getUseFastIntParsing();
/*  465 */       this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
/*  466 */       this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
/*      */     }
/*      */     
/*  469 */     this.owningStatement = creatorStmt;
/*      */     
/*  471 */     this.catalog = catalog;
/*      */     
/*  473 */     this.fields = fields;
/*  474 */     this.rowData = tuples;
/*  475 */     this.updateCount = this.rowData.size();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  482 */     this.reallyResult = true;
/*      */     
/*      */ 
/*  485 */     if (this.rowData.size() > 0) {
/*  486 */       if ((this.updateCount == 1L) && 
/*  487 */         (this.thisRow == null)) {
/*  488 */         this.rowData.close();
/*  489 */         this.updateCount = -1L;
/*      */       }
/*      */     }
/*      */     else {
/*  493 */       this.thisRow = null;
/*      */     }
/*      */     
/*  496 */     this.rowData.setOwner(this);
/*      */     
/*  498 */     if (this.fields != null) {
/*  499 */       initializeWithMetadata();
/*      */     }
/*  501 */     this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
/*      */     
/*  503 */     this.useColumnNamesInFindColumn = this.connection.getUseColumnNamesInFindColumn();
/*      */     
/*  505 */     setRowPositionValidity();
/*      */   }
/*      */   
/*      */   public void initializeWithMetadata() throws SQLException {
/*  509 */     synchronized (checkClosed().getConnectionMutex()) {
/*  510 */       this.rowData.setMetadata(this.fields);
/*      */       
/*  512 */       this.columnToIndexCache = new HashMap();
/*      */       
/*  514 */       if ((this.profileSql) || (this.connection.getUseUsageAdvisor())) {
/*  515 */         this.columnUsed = new boolean[this.fields.length];
/*  516 */         this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
/*  517 */         this.resultId = (resultCounter++);
/*  518 */         this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
/*  519 */         this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */       }
/*      */       
/*  522 */       if (this.connection.getGatherPerformanceMetrics()) {
/*  523 */         this.connection.incrementNumberOfResultSetsCreated();
/*      */         
/*  525 */         Set<String> tableNamesSet = new HashSet();
/*      */         
/*  527 */         for (int i = 0; i < this.fields.length; i++) {
/*  528 */           Field f = this.fields[i];
/*      */           
/*  530 */           String tableName = f.getOriginalTableName();
/*      */           
/*  532 */           if (tableName == null) {
/*  533 */             tableName = f.getTableName();
/*      */           }
/*      */           
/*  536 */           if (tableName != null) {
/*  537 */             if (this.connection.lowerCaseTableNames()) {
/*  538 */               tableName = tableName.toLowerCase();
/*      */             }
/*      */             
/*      */ 
/*  542 */             tableNamesSet.add(tableName);
/*      */           }
/*      */         }
/*      */         
/*  546 */         this.connection.reportNumberOfTablesAccessed(tableNamesSet.size());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized void createCalendarIfNeeded() {
/*  552 */     if (this.fastDateCal == null) {
/*  553 */       this.fastDateCal = new GregorianCalendar(Locale.US);
/*  554 */       this.fastDateCal.setTimeZone(getDefaultTimeZone());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean absolute(int row)
/*      */     throws SQLException
/*      */   {
/*  597 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*      */       boolean b;
/*      */       boolean b;
/*  601 */       if (this.rowData.size() == 0) {
/*  602 */         b = false;
/*      */       } else {
/*  604 */         if (this.onInsertRow) {
/*  605 */           this.onInsertRow = false;
/*      */         }
/*      */         
/*  608 */         if (this.doingUpdates) {
/*  609 */           this.doingUpdates = false;
/*      */         }
/*      */         
/*  612 */         if (this.thisRow != null) {
/*  613 */           this.thisRow.closeOpenStreams();
/*      */         }
/*      */         boolean b;
/*  616 */         if (row == 0) {
/*  617 */           beforeFirst();
/*  618 */           b = false; } else { boolean b;
/*  619 */           if (row == 1) {
/*  620 */             b = first(); } else { boolean b;
/*  621 */             if (row == -1) {
/*  622 */               b = last(); } else { boolean b;
/*  623 */               if (row > this.rowData.size()) {
/*  624 */                 afterLast();
/*  625 */                 b = false;
/*      */               } else { boolean b;
/*  627 */                 if (row < 0)
/*      */                 {
/*  629 */                   int newRowPosition = this.rowData.size() + row + 1;
/*      */                   boolean b;
/*  631 */                   if (newRowPosition <= 0) {
/*  632 */                     beforeFirst();
/*  633 */                     b = false;
/*      */                   } else {
/*  635 */                     b = absolute(newRowPosition);
/*      */                   }
/*      */                 } else {
/*  638 */                   row--;
/*  639 */                   this.rowData.setCurrentRow(row);
/*  640 */                   this.thisRow = this.rowData.getAt(row);
/*  641 */                   b = true;
/*      */                 }
/*      */               }
/*      */             }
/*      */           } } }
/*  646 */       setRowPositionValidity();
/*      */       
/*  648 */       return b;
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
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/*  665 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*  667 */       if (this.onInsertRow) {
/*  668 */         this.onInsertRow = false;
/*      */       }
/*      */       
/*  671 */       if (this.doingUpdates) {
/*  672 */         this.doingUpdates = false;
/*      */       }
/*      */       
/*  675 */       if (this.thisRow != null) {
/*  676 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/*  679 */       if (this.rowData.size() != 0) {
/*  680 */         this.rowData.afterLast();
/*  681 */         this.thisRow = null;
/*      */       }
/*      */       
/*  684 */       setRowPositionValidity();
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
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/*  701 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*  703 */       if (this.onInsertRow) {
/*  704 */         this.onInsertRow = false;
/*      */       }
/*      */       
/*  707 */       if (this.doingUpdates) {
/*  708 */         this.doingUpdates = false;
/*      */       }
/*      */       
/*  711 */       if (this.rowData.size() == 0) {
/*  712 */         return;
/*      */       }
/*      */       
/*  715 */       if (this.thisRow != null) {
/*  716 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/*  719 */       this.rowData.beforeFirst();
/*  720 */       this.thisRow = null;
/*      */       
/*  722 */       setRowPositionValidity();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void buildIndexMapping()
/*      */     throws SQLException
/*      */   {
/*  734 */     int numFields = this.fields.length;
/*  735 */     this.columnLabelToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  736 */     this.fullColumnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  737 */     this.columnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  751 */     for (int i = numFields - 1; i >= 0; i--) {
/*  752 */       Integer index = Integer.valueOf(i);
/*  753 */       String columnName = this.fields[i].getOriginalName();
/*  754 */       String columnLabel = this.fields[i].getName();
/*  755 */       String fullColumnName = this.fields[i].getFullName();
/*      */       
/*  757 */       if (columnLabel != null) {
/*  758 */         this.columnLabelToIndex.put(columnLabel, index);
/*      */       }
/*      */       
/*  761 */       if (fullColumnName != null) {
/*  762 */         this.fullColumnNameToIndex.put(fullColumnName, index);
/*      */       }
/*      */       
/*  765 */       if (columnName != null) {
/*  766 */         this.columnNameToIndex.put(columnName, index);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  771 */     this.hasBuiltIndexMapping = true;
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
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/*  787 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final MySQLConnection checkClosed()
/*      */     throws SQLException
/*      */   {
/*  797 */     MySQLConnection c = this.connection;
/*      */     
/*  799 */     if (c == null) {
/*  800 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  806 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void checkColumnBounds(int columnIndex)
/*      */     throws SQLException
/*      */   {
/*  819 */     synchronized (checkClosed().getConnectionMutex()) {
/*  820 */       if (columnIndex < 1) {
/*  821 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_low", new Object[] { Integer.valueOf(columnIndex), Integer.valueOf(this.fields.length) }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  826 */       if (columnIndex > this.fields.length) {
/*  827 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_high", new Object[] { Integer.valueOf(columnIndex), Integer.valueOf(this.fields.length) }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  834 */       if ((this.profileSql) || (this.useUsageAdvisor)) {
/*  835 */         this.columnUsed[(columnIndex - 1)] = true;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkRowPos()
/*      */     throws SQLException
/*      */   {
/*  848 */     checkClosed();
/*      */     
/*  850 */     if (!this.onValidRow) {
/*  851 */       throw SQLError.createSQLException(this.invalidRowReason, "S1000", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  856 */   private boolean onValidRow = false;
/*  857 */   private String invalidRowReason = null;
/*      */   protected boolean useLegacyDatetimeCode;
/*      */   private TimeZone serverTimeZoneTz;
/*      */   
/*      */   private void setRowPositionValidity() throws SQLException {
/*  862 */     if ((!this.rowData.isDynamic()) && (this.rowData.size() == 0)) {
/*  863 */       this.invalidRowReason = Messages.getString("ResultSet.Illegal_operation_on_empty_result_set");
/*      */       
/*  865 */       this.onValidRow = false;
/*  866 */     } else if (this.rowData.isBeforeFirst()) {
/*  867 */       this.invalidRowReason = Messages.getString("ResultSet.Before_start_of_result_set_146");
/*      */       
/*  869 */       this.onValidRow = false;
/*  870 */     } else if (this.rowData.isAfterLast()) {
/*  871 */       this.invalidRowReason = Messages.getString("ResultSet.After_end_of_result_set_148");
/*      */       
/*  873 */       this.onValidRow = false;
/*      */     } else {
/*  875 */       this.onValidRow = true;
/*  876 */       this.invalidRowReason = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void clearNextResult()
/*      */   {
/*  885 */     this.nextResultSet = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/*  896 */     synchronized (checkClosed().getConnectionMutex()) {
/*  897 */       this.warningChain = null;
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
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  919 */     realClose(true);
/*      */   }
/*      */   
/*      */ 
/*      */   private int convertToZeroWithEmptyCheck()
/*      */     throws SQLException
/*      */   {
/*  926 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  927 */       return 0;
/*      */     }
/*      */     
/*  930 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */   private String convertToZeroLiteralStringWithEmptyCheck()
/*      */     throws SQLException
/*      */   {
/*  937 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  938 */       return "0";
/*      */     }
/*      */     
/*  941 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ResultSetInternalMethods copy()
/*      */     throws SQLException
/*      */   {
/*  949 */     synchronized (checkClosed().getConnectionMutex()) {
/*  950 */       ResultSetInternalMethods rs = getInstance(this.catalog, this.fields, this.rowData, this.connection, this.owningStatement, false);
/*      */       
/*      */ 
/*  953 */       return rs;
/*      */     }
/*      */   }
/*      */   
/*      */   public void redefineFieldsForDBMD(Field[] f) {
/*  958 */     this.fields = f;
/*      */     
/*  960 */     for (int i = 0; i < this.fields.length; i++) {
/*  961 */       this.fields[i].setUseOldNameMetadata(true);
/*  962 */       this.fields[i].setConnection(this.connection);
/*      */     }
/*      */   }
/*      */   
/*      */   public void populateCachedMetaData(CachedResultSetMetaData cachedMetaData) throws SQLException
/*      */   {
/*  968 */     cachedMetaData.fields = this.fields;
/*  969 */     cachedMetaData.columnNameToIndex = this.columnLabelToIndex;
/*  970 */     cachedMetaData.fullColumnNameToIndex = this.fullColumnNameToIndex;
/*  971 */     cachedMetaData.metadata = getMetaData();
/*      */   }
/*      */   
/*      */   public void initializeFromCachedMetaData(CachedResultSetMetaData cachedMetaData) {
/*  975 */     this.fields = cachedMetaData.fields;
/*  976 */     this.columnLabelToIndex = cachedMetaData.columnNameToIndex;
/*  977 */     this.fullColumnNameToIndex = cachedMetaData.fullColumnNameToIndex;
/*  978 */     this.hasBuiltIndexMapping = true;
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
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/*  993 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String extractStringFromNativeColumn(int columnIndex, int mysqlType)
/*      */     throws SQLException
/*      */   {
/* 1005 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 1007 */     this.wasNullFlag = false;
/*      */     
/* 1009 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1010 */       this.wasNullFlag = true;
/*      */       
/* 1012 */       return null;
/*      */     }
/*      */     
/* 1015 */     this.wasNullFlag = false;
/*      */     
/* 1017 */     String encoding = this.fields[columnIndexMinusOne].getCharacterSet();
/*      */     
/*      */ 
/* 1020 */     return this.thisRow.getString(columnIndex - 1, encoding, this.connection);
/*      */   }
/*      */   
/*      */   protected Date fastDateCreate(Calendar cal, int year, int month, int day) throws SQLException
/*      */   {
/* 1025 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1026 */       if (this.useLegacyDatetimeCode) {
/* 1027 */         return TimeUtil.fastDateCreate(year, month, day, cal);
/*      */       }
/*      */       
/* 1030 */       if (cal == null) {
/* 1031 */         createCalendarIfNeeded();
/* 1032 */         cal = this.fastDateCal;
/*      */       }
/*      */       
/* 1035 */       boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */       
/* 1037 */       return TimeUtil.fastDateCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : cal, cal, year, month, day);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected Time fastTimeCreate(Calendar cal, int hour, int minute, int second)
/*      */     throws SQLException
/*      */   {
/* 1045 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1046 */       if (!this.useLegacyDatetimeCode) {
/* 1047 */         return TimeUtil.fastTimeCreate(hour, minute, second, cal, getExceptionInterceptor());
/*      */       }
/*      */       
/* 1050 */       if (cal == null) {
/* 1051 */         createCalendarIfNeeded();
/* 1052 */         cal = this.fastDateCal;
/*      */       }
/*      */       
/* 1055 */       return TimeUtil.fastTimeCreate(cal, hour, minute, second, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   protected Timestamp fastTimestampCreate(Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart)
/*      */     throws SQLException
/*      */   {
/* 1062 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1063 */       if (!this.useLegacyDatetimeCode) {
/* 1064 */         return TimeUtil.fastTimestampCreate(cal.getTimeZone(), year, month, day, hour, minute, seconds, secondsPart);
/*      */       }
/*      */       
/*      */ 
/* 1068 */       if (cal == null) {
/* 1069 */         createCalendarIfNeeded();
/* 1070 */         cal = this.fastDateCal;
/*      */       }
/*      */       
/* 1073 */       boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */       
/* 1075 */       return TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : null, cal, year, month, day, hour, minute, seconds, secondsPart);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int findColumn(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1123 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/*      */ 
/* 1126 */       if (!this.hasBuiltIndexMapping) {
/* 1127 */         buildIndexMapping();
/*      */       }
/*      */       
/* 1130 */       Integer index = (Integer)this.columnToIndexCache.get(columnName);
/*      */       
/* 1132 */       if (index != null) {
/* 1133 */         return index.intValue() + 1;
/*      */       }
/*      */       
/* 1136 */       index = (Integer)this.columnLabelToIndex.get(columnName);
/*      */       
/* 1138 */       if ((index == null) && (this.useColumnNamesInFindColumn)) {
/* 1139 */         index = (Integer)this.columnNameToIndex.get(columnName);
/*      */       }
/*      */       
/* 1142 */       if (index == null) {
/* 1143 */         index = (Integer)this.fullColumnNameToIndex.get(columnName);
/*      */       }
/*      */       
/* 1146 */       if (index != null) {
/* 1147 */         this.columnToIndexCache.put(columnName, index);
/*      */         
/* 1149 */         return index.intValue() + 1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1154 */       for (int i = 0; i < this.fields.length; i++) {
/* 1155 */         if (this.fields[i].getName().equalsIgnoreCase(columnName))
/* 1156 */           return i + 1;
/* 1157 */         if (this.fields[i].getFullName().equalsIgnoreCase(columnName))
/*      */         {
/* 1159 */           return i + 1;
/*      */         }
/*      */       }
/*      */       
/* 1163 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Column____112") + columnName + Messages.getString("ResultSet.___not_found._113"), "S0022", getExceptionInterceptor());
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
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 1184 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 1186 */       boolean b = true;
/*      */       
/* 1188 */       if (this.rowData.isEmpty()) {
/* 1189 */         b = false;
/*      */       }
/*      */       else {
/* 1192 */         if (this.onInsertRow) {
/* 1193 */           this.onInsertRow = false;
/*      */         }
/*      */         
/* 1196 */         if (this.doingUpdates) {
/* 1197 */           this.doingUpdates = false;
/*      */         }
/*      */         
/* 1200 */         this.rowData.beforeFirst();
/* 1201 */         this.thisRow = this.rowData.next();
/*      */       }
/*      */       
/* 1204 */       setRowPositionValidity();
/*      */       
/* 1206 */       return b;
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
/*      */   public Array getArray(int i)
/*      */     throws SQLException
/*      */   {
/* 1224 */     checkColumnBounds(i);
/*      */     
/* 1226 */     throw SQLError.notImplemented();
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
/*      */   public Array getArray(String colName)
/*      */     throws SQLException
/*      */   {
/* 1243 */     return getArray(findColumn(colName));
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
/*      */   public InputStream getAsciiStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1272 */     checkRowPos();
/*      */     
/* 1274 */     if (!this.isBinaryEncoded) {
/* 1275 */       return getBinaryStream(columnIndex);
/*      */     }
/*      */     
/* 1278 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getAsciiStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1293 */     return getAsciiStream(findColumn(columnName));
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
/*      */   public BigDecimal getBigDecimal(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1310 */     if (!this.isBinaryEncoded) {
/* 1311 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 1314 */       if (stringVal != null) {
/* 1315 */         if (stringVal.length() == 0)
/*      */         {
/* 1317 */           BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */           
/*      */ 
/* 1320 */           return val;
/*      */         }
/*      */         try
/*      */         {
/* 1324 */           return new BigDecimal(stringVal);
/*      */         }
/*      */         catch (NumberFormatException ex)
/*      */         {
/* 1328 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1336 */       return null;
/*      */     }
/*      */     
/* 1339 */     return getNativeBigDecimal(columnIndex);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public BigDecimal getBigDecimal(int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 1360 */     if (!this.isBinaryEncoded) {
/* 1361 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 1364 */       if (stringVal != null) {
/* 1365 */         if (stringVal.length() == 0) {
/* 1366 */           BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */           
/*      */           try
/*      */           {
/* 1370 */             return val.setScale(scale);
/*      */           } catch (ArithmeticException ex) {
/*      */             try {
/* 1373 */               return val.setScale(scale, 4);
/*      */             }
/*      */             catch (ArithmeticException arEx) {
/* 1376 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1386 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) { BigDecimal val;
/* 1388 */           if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 1389 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 1391 */             val = new BigDecimal(valueAsLong);
/*      */           } else {
/* 1393 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Integer.valueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1402 */           return val.setScale(scale);
/*      */         } catch (ArithmeticException ex) {
/*      */           try { BigDecimal val;
/* 1405 */             return val.setScale(scale, 4);
/*      */           } catch (ArithmeticException arithEx) {
/* 1407 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { Integer.valueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1416 */       return null;
/*      */     }
/*      */     
/* 1419 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   public BigDecimal getBigDecimal(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1435 */     return getBigDecimal(findColumn(columnName));
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public BigDecimal getBigDecimal(String columnName, int scale)
/*      */     throws SQLException
/*      */   {
/* 1455 */     return getBigDecimal(findColumn(columnName), scale);
/*      */   }
/*      */   
/*      */ 
/*      */   private final BigDecimal getBigDecimalFromString(String stringVal, int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 1462 */     if (stringVal != null) {
/* 1463 */       if (stringVal.length() == 0) {
/* 1464 */         BigDecimal bdVal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */         try
/*      */         {
/* 1467 */           return bdVal.setScale(scale);
/*      */         } catch (ArithmeticException ex) {
/*      */           try {
/* 1470 */             return bdVal.setScale(scale, 4);
/*      */           } catch (ArithmeticException arEx) {
/* 1472 */             throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1483 */         return new BigDecimal(stringVal).setScale(scale);
/*      */       } catch (ArithmeticException ex) {
/*      */         try {
/* 1486 */           return new BigDecimal(stringVal).setScale(scale, 4);
/*      */         }
/*      */         catch (ArithmeticException arEx) {
/* 1489 */           throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException ex)
/*      */       {
/*      */ 
/* 1497 */         if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 1498 */           long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */           try
/*      */           {
/* 1501 */             return new BigDecimal(valueAsLong).setScale(scale);
/*      */           } catch (ArithmeticException arEx1) {
/*      */             try {
/* 1504 */               return new BigDecimal(valueAsLong).setScale(scale, 4);
/*      */             }
/*      */             catch (ArithmeticException arEx2) {
/* 1507 */               throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1516 */         if ((this.fields[(columnIndex - 1)].getMysqlType() == 1) && (this.connection.getTinyInt1isBit()) && (this.fields[(columnIndex - 1)].getLength() == 1L))
/*      */         {
/* 1518 */           return new BigDecimal(stringVal.equalsIgnoreCase("true") ? 1 : 0).setScale(scale);
/*      */         }
/*      */         
/* 1521 */         throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1529 */     return null;
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
/*      */   public InputStream getBinaryStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1550 */     checkRowPos();
/*      */     
/* 1552 */     if (!this.isBinaryEncoded) {
/* 1553 */       checkColumnBounds(columnIndex);
/*      */       
/* 1555 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1557 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1558 */         this.wasNullFlag = true;
/*      */         
/* 1560 */         return null;
/*      */       }
/*      */       
/* 1563 */       this.wasNullFlag = false;
/*      */       
/* 1565 */       return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1568 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getBinaryStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1583 */     return getBinaryStream(findColumn(columnName));
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
/*      */   public java.sql.Blob getBlob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1598 */     if (!this.isBinaryEncoded) {
/* 1599 */       checkRowPos();
/*      */       
/* 1601 */       checkColumnBounds(columnIndex);
/*      */       
/* 1603 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1605 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1606 */         this.wasNullFlag = true;
/*      */       } else {
/* 1608 */         this.wasNullFlag = false;
/*      */       }
/*      */       
/* 1611 */       if (this.wasNullFlag) {
/* 1612 */         return null;
/*      */       }
/*      */       
/* 1615 */       if (!this.connection.getEmulateLocators()) {
/* 1616 */         return new Blob(this.thisRow.getColumnValue(columnIndexMinusOne), getExceptionInterceptor());
/*      */       }
/*      */       
/* 1619 */       return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
/*      */     }
/*      */     
/* 1622 */     return getNativeBlob(columnIndex);
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
/*      */   public java.sql.Blob getBlob(String colName)
/*      */     throws SQLException
/*      */   {
/* 1637 */     return getBlob(findColumn(colName));
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
/*      */   public boolean getBoolean(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1653 */     checkColumnBounds(columnIndex);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1660 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 1662 */     Field field = this.fields[columnIndexMinusOne];
/*      */     
/* 1664 */     if (field.getMysqlType() == 16) {
/* 1665 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1668 */     this.wasNullFlag = false;
/*      */     
/* 1670 */     int sqlType = field.getSQLType();
/*      */     long boolVal;
/* 1672 */     switch (sqlType) {
/*      */     case 16: 
/* 1674 */       if (field.getMysqlType() == -1) {
/* 1675 */         String stringVal = getString(columnIndex);
/*      */         
/* 1677 */         return getBooleanFromString(stringVal);
/*      */       }
/*      */       
/* 1680 */       boolVal = getLong(columnIndex, false);
/*      */       
/* 1682 */       return (boolVal == -1L) || (boolVal > 0L);
/*      */     case -7: 
/*      */     case -6: 
/*      */     case -5: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/* 1693 */       boolVal = getLong(columnIndex, false);
/*      */       
/* 1695 */       return (boolVal == -1L) || (boolVal > 0L);
/*      */     }
/* 1697 */     if (this.connection.getPedantic())
/*      */     {
/* 1699 */       switch (sqlType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case 70: 
/*      */       case 91: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 2000: 
/*      */       case 2002: 
/*      */       case 2003: 
/*      */       case 2004: 
/*      */       case 2005: 
/*      */       case 2006: 
/* 1713 */         throw SQLError.createSQLException("Required type conversion not allowed", "22018", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 1718 */     if ((sqlType == -2) || (sqlType == -3) || (sqlType == -4) || (sqlType == 2004))
/*      */     {
/*      */ 
/*      */ 
/* 1722 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1725 */     if (this.useUsageAdvisor) {
/* 1726 */       issueConversionViaParsingWarning("getBoolean()", columnIndex, this.thisRow.getColumnValue(columnIndexMinusOne), this.fields[columnIndex], new int[] { 16, 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1738 */     String stringVal = getString(columnIndex);
/*      */     
/* 1740 */     return getBooleanFromString(stringVal);
/*      */   }
/*      */   
/*      */   private boolean byteArrayToBoolean(int columnIndexMinusOne) throws SQLException
/*      */   {
/* 1745 */     Object value = this.thisRow.getColumnValue(columnIndexMinusOne);
/*      */     
/* 1747 */     if (value == null) {
/* 1748 */       this.wasNullFlag = true;
/*      */       
/* 1750 */       return false;
/*      */     }
/*      */     
/* 1753 */     this.wasNullFlag = false;
/*      */     
/* 1755 */     if (((byte[])value).length == 0) {
/* 1756 */       return false;
/*      */     }
/*      */     
/* 1759 */     byte boolVal = ((byte[])(byte[])value)[0];
/*      */     
/* 1761 */     if (boolVal == 49)
/* 1762 */       return true;
/* 1763 */     if (boolVal == 48) {
/* 1764 */       return false;
/*      */     }
/*      */     
/* 1767 */     return (boolVal == -1) || (boolVal > 0);
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
/*      */   public boolean getBoolean(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1782 */     return getBoolean(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final boolean getBooleanFromString(String stringVal) throws SQLException
/*      */   {
/* 1787 */     if ((stringVal != null) && (stringVal.length() > 0)) {
/* 1788 */       int c = Character.toLowerCase(stringVal.charAt(0));
/*      */       
/* 1790 */       return (c == 116) || (c == 121) || (c == 49) || (stringVal.equals("-1"));
/*      */     }
/*      */     
/*      */ 
/* 1794 */     return false;
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
/*      */   public byte getByte(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1809 */     if (!this.isBinaryEncoded) {
/* 1810 */       String stringVal = getString(columnIndex);
/*      */       
/* 1812 */       if ((this.wasNullFlag) || (stringVal == null)) {
/* 1813 */         return 0;
/*      */       }
/*      */       
/* 1816 */       return getByteFromString(stringVal, columnIndex);
/*      */     }
/*      */     
/* 1819 */     return getNativeByte(columnIndex);
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
/*      */   public byte getByte(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1834 */     return getByte(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final byte getByteFromString(String stringVal, int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1840 */     if ((stringVal != null) && (stringVal.length() == 0)) {
/* 1841 */       return (byte)convertToZeroWithEmptyCheck();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1852 */     if (stringVal == null) {
/* 1853 */       return 0;
/*      */     }
/*      */     
/* 1856 */     stringVal = stringVal.trim();
/*      */     try
/*      */     {
/* 1859 */       int decimalIndex = stringVal.indexOf(".");
/*      */       
/*      */ 
/* 1862 */       if (decimalIndex != -1) {
/* 1863 */         double valueAsDouble = Double.parseDouble(stringVal);
/*      */         
/* 1865 */         if ((this.jdbcCompliantTruncationForReads) && (
/* 1866 */           (valueAsDouble < -128.0D) || (valueAsDouble > 127.0D)))
/*      */         {
/* 1868 */           throwRangeException(stringVal, columnIndex, -6);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1873 */         return (byte)(int)valueAsDouble;
/*      */       }
/*      */       
/* 1876 */       long valueAsLong = Long.parseLong(stringVal);
/*      */       
/* 1878 */       if ((this.jdbcCompliantTruncationForReads) && (
/* 1879 */         (valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/* 1881 */         throwRangeException(String.valueOf(valueAsLong), columnIndex, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1886 */       return (byte)(int)valueAsLong;
/*      */     } catch (NumberFormatException NFE) {
/* 1888 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Value____173") + stringVal + Messages.getString("ResultSet.___is_out_of_range_[-127,127]_174"), "S1009", getExceptionInterceptor());
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
/*      */   public byte[] getBytes(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1913 */     return getBytes(columnIndex, false);
/*      */   }
/*      */   
/*      */   protected byte[] getBytes(int columnIndex, boolean noConversion) throws SQLException
/*      */   {
/* 1918 */     if (!this.isBinaryEncoded) {
/* 1919 */       checkRowPos();
/*      */       
/* 1921 */       checkColumnBounds(columnIndex);
/*      */       
/* 1923 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 1925 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 1926 */         this.wasNullFlag = true;
/*      */       } else {
/* 1928 */         this.wasNullFlag = false;
/*      */       }
/*      */       
/* 1931 */       if (this.wasNullFlag) {
/* 1932 */         return null;
/*      */       }
/*      */       
/* 1935 */       return this.thisRow.getColumnValue(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1938 */     return getNativeBytes(columnIndex, noConversion);
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
/*      */   public byte[] getBytes(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1953 */     return getBytes(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final byte[] getBytesFromString(String stringVal) throws SQLException
/*      */   {
/* 1958 */     if (stringVal != null) {
/* 1959 */       return StringUtils.getBytes(stringVal, this.connection.getEncoding(), this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection, getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1966 */     return null;
/*      */   }
/*      */   
/*      */   public int getBytesSize() throws SQLException {
/* 1970 */     RowData localRowData = this.rowData;
/*      */     
/* 1972 */     checkClosed();
/*      */     
/* 1974 */     if ((localRowData instanceof RowDataStatic)) {
/* 1975 */       int bytesSize = 0;
/*      */       
/* 1977 */       int numRows = localRowData.size();
/*      */       
/* 1979 */       for (int i = 0; i < numRows; i++) {
/* 1980 */         bytesSize += localRowData.getAt(i).getBytesSize();
/*      */       }
/*      */       
/* 1983 */       return bytesSize;
/*      */     }
/*      */     
/* 1986 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Calendar getCalendarInstanceForSessionOrNew()
/*      */     throws SQLException
/*      */   {
/* 1994 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1995 */       if (this.connection != null) {
/* 1996 */         return this.connection.getCalendarInstanceForSessionOrNew();
/*      */       }
/*      */       
/*      */ 
/* 2000 */       return new GregorianCalendar();
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
/*      */   public Reader getCharacterStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2021 */     if (!this.isBinaryEncoded) {
/* 2022 */       checkColumnBounds(columnIndex);
/*      */       
/* 2024 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 2026 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2027 */         this.wasNullFlag = true;
/*      */         
/* 2029 */         return null;
/*      */       }
/*      */       
/* 2032 */       this.wasNullFlag = false;
/*      */       
/* 2034 */       return this.thisRow.getReader(columnIndexMinusOne);
/*      */     }
/*      */     
/* 2037 */     return getNativeCharacterStream(columnIndex);
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
/*      */   public Reader getCharacterStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2057 */     return getCharacterStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final Reader getCharacterStreamFromString(String stringVal) throws SQLException
/*      */   {
/* 2062 */     if (stringVal != null) {
/* 2063 */       return new StringReader(stringVal);
/*      */     }
/*      */     
/* 2066 */     return null;
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
/*      */   public java.sql.Clob getClob(int i)
/*      */     throws SQLException
/*      */   {
/* 2081 */     if (!this.isBinaryEncoded) {
/* 2082 */       String asString = getStringForClob(i);
/*      */       
/* 2084 */       if (asString == null) {
/* 2085 */         return null;
/*      */       }
/*      */       
/* 2088 */       return new Clob(asString, getExceptionInterceptor());
/*      */     }
/*      */     
/* 2091 */     return getNativeClob(i);
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
/*      */   public java.sql.Clob getClob(String colName)
/*      */     throws SQLException
/*      */   {
/* 2106 */     return getClob(findColumn(colName));
/*      */   }
/*      */   
/*      */   private final java.sql.Clob getClobFromString(String stringVal) throws SQLException {
/* 2110 */     return new Clob(stringVal, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getConcurrency()
/*      */     throws SQLException
/*      */   {
/* 2123 */     return 1007;
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
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 2152 */     throw SQLError.createSQLException(Messages.getString("ResultSet.Positioned_Update_not_supported"), "S1C00", getExceptionInterceptor());
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
/*      */   public Date getDate(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2169 */     return getDate(columnIndex, null);
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
/*      */   public Date getDate(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2190 */     if (this.isBinaryEncoded) {
/* 2191 */       return getNativeDate(columnIndex, cal);
/*      */     }
/*      */     
/* 2194 */     if (!this.useFastDateParsing) {
/* 2195 */       String stringVal = getStringInternal(columnIndex, false);
/*      */       
/* 2197 */       if (stringVal == null) {
/* 2198 */         return null;
/*      */       }
/*      */       
/* 2201 */       return getDateFromString(stringVal, columnIndex, cal);
/*      */     }
/*      */     
/* 2204 */     checkColumnBounds(columnIndex);
/*      */     
/* 2206 */     int columnIndexMinusOne = columnIndex - 1;
/* 2207 */     Date tmpDate = this.thisRow.getDateFast(columnIndexMinusOne, this.connection, this, cal);
/* 2208 */     if ((this.thisRow.isNull(columnIndexMinusOne)) || (tmpDate == null))
/*      */     {
/*      */ 
/* 2211 */       this.wasNullFlag = true;
/*      */       
/* 2213 */       return null;
/*      */     }
/*      */     
/* 2216 */     this.wasNullFlag = false;
/*      */     
/* 2218 */     return tmpDate;
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
/*      */   public Date getDate(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2234 */     return getDate(findColumn(columnName));
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
/*      */   public Date getDate(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2254 */     return getDate(findColumn(columnName), cal);
/*      */   }
/*      */   
/*      */   private final Date getDateFromString(String stringVal, int columnIndex, Calendar targetCalendar) throws SQLException
/*      */   {
/* 2259 */     int year = 0;
/* 2260 */     int month = 0;
/* 2261 */     int day = 0;
/*      */     try
/*      */     {
/* 2264 */       this.wasNullFlag = false;
/*      */       
/* 2266 */       if (stringVal == null) {
/* 2267 */         this.wasNullFlag = true;
/*      */         
/* 2269 */         return null;
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
/* 2280 */       stringVal = stringVal.trim();
/*      */       
/* 2282 */       if ((stringVal.equals("0")) || (stringVal.equals("0000-00-00")) || (stringVal.equals("0000-00-00 00:00:00")) || (stringVal.equals("00000000000000")) || (stringVal.equals("0")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2287 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2289 */           this.wasNullFlag = true;
/*      */           
/* 2291 */           return null; }
/* 2292 */         if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2294 */           throw SQLError.createSQLException("Value '" + stringVal + "' can not be represented as java.sql.Date", "S1009", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2301 */         return fastDateCreate(targetCalendar, 1, 1, 1);
/*      */       }
/* 2303 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 7)
/*      */       {
/* 2305 */         switch (stringVal.length()) {
/*      */         case 19: 
/*      */         case 21: 
/* 2308 */           year = Integer.parseInt(stringVal.substring(0, 4));
/* 2309 */           month = Integer.parseInt(stringVal.substring(5, 7));
/* 2310 */           day = Integer.parseInt(stringVal.substring(8, 10));
/*      */           
/* 2312 */           return fastDateCreate(targetCalendar, year, month, day);
/*      */         
/*      */ 
/*      */         case 8: 
/*      */         case 14: 
/* 2317 */           year = Integer.parseInt(stringVal.substring(0, 4));
/* 2318 */           month = Integer.parseInt(stringVal.substring(4, 6));
/* 2319 */           day = Integer.parseInt(stringVal.substring(6, 8));
/*      */           
/* 2321 */           return fastDateCreate(targetCalendar, year, month, day);
/*      */         
/*      */ 
/*      */         case 6: 
/*      */         case 10: 
/*      */         case 12: 
/* 2327 */           year = Integer.parseInt(stringVal.substring(0, 2));
/*      */           
/* 2329 */           if (year <= 69) {
/* 2330 */             year += 100;
/*      */           }
/*      */           
/* 2333 */           month = Integer.parseInt(stringVal.substring(2, 4));
/* 2334 */           day = Integer.parseInt(stringVal.substring(4, 6));
/*      */           
/* 2336 */           return fastDateCreate(targetCalendar, year + 1900, month, day);
/*      */         
/*      */ 
/*      */         case 4: 
/* 2340 */           year = Integer.parseInt(stringVal.substring(0, 4));
/*      */           
/* 2342 */           if (year <= 69) {
/* 2343 */             year += 100;
/*      */           }
/*      */           
/* 2346 */           month = Integer.parseInt(stringVal.substring(2, 4));
/*      */           
/* 2348 */           return fastDateCreate(targetCalendar, year + 1900, month, 1);
/*      */         
/*      */ 
/*      */         case 2: 
/* 2352 */           year = Integer.parseInt(stringVal.substring(0, 2));
/*      */           
/* 2354 */           if (year <= 69) {
/* 2355 */             year += 100;
/*      */           }
/*      */           
/* 2358 */           return fastDateCreate(targetCalendar, year + 1900, 1, 1);
/*      */         }
/*      */         
/*      */         
/* 2362 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2367 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 13)
/*      */       {
/* 2369 */         if ((stringVal.length() == 2) || (stringVal.length() == 1)) {
/* 2370 */           year = Integer.parseInt(stringVal);
/*      */           
/* 2372 */           if (year <= 69) {
/* 2373 */             year += 100;
/*      */           }
/*      */           
/* 2376 */           year += 1900;
/*      */         } else {
/* 2378 */           year = Integer.parseInt(stringVal.substring(0, 4));
/*      */         }
/*      */         
/* 2381 */         return fastDateCreate(targetCalendar, year, 1, 1); }
/* 2382 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 11) {
/* 2383 */         return fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */       }
/* 2385 */       if (stringVal.length() < 10) {
/* 2386 */         if (stringVal.length() == 8) {
/* 2387 */           return fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */         }
/*      */         
/* 2390 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2396 */       if (stringVal.length() != 18) {
/* 2397 */         year = Integer.parseInt(stringVal.substring(0, 4));
/* 2398 */         month = Integer.parseInt(stringVal.substring(5, 7));
/* 2399 */         day = Integer.parseInt(stringVal.substring(8, 10));
/*      */       }
/*      */       else {
/* 2402 */         StringTokenizer st = new StringTokenizer(stringVal, "- ");
/*      */         
/* 2404 */         year = Integer.parseInt(st.nextToken());
/* 2405 */         month = Integer.parseInt(st.nextToken());
/* 2406 */         day = Integer.parseInt(st.nextToken());
/*      */       }
/*      */       
/*      */ 
/* 2410 */       return fastDateCreate(targetCalendar, year, month, day);
/*      */     } catch (SQLException sqlEx) {
/* 2412 */       throw sqlEx;
/*      */     } catch (Exception e) {
/* 2414 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2419 */       sqlEx.initCause(e);
/*      */       
/* 2421 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   private TimeZone getDefaultTimeZone() {
/* 2426 */     if ((!this.useLegacyDatetimeCode) && (this.connection != null)) {
/* 2427 */       return this.serverTimeZoneTz;
/*      */     }
/*      */     
/* 2430 */     return this.connection.getDefaultTimeZone();
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
/*      */   public double getDouble(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2445 */     if (!this.isBinaryEncoded) {
/* 2446 */       return getDoubleInternal(columnIndex);
/*      */     }
/*      */     
/* 2449 */     return getNativeDouble(columnIndex);
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
/*      */   public double getDouble(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2464 */     return getDouble(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final double getDoubleFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 2469 */     return getDoubleInternal(stringVal, columnIndex);
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
/*      */   protected double getDoubleInternal(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 2485 */     return getDoubleInternal(getString(colIndex), colIndex);
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
/*      */   protected double getDoubleInternal(String stringVal, int colIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2505 */       if (stringVal == null) {
/* 2506 */         return 0.0D;
/*      */       }
/*      */       
/* 2509 */       if (stringVal.length() == 0) {
/* 2510 */         return convertToZeroWithEmptyCheck();
/*      */       }
/*      */       
/* 2513 */       double d = Double.parseDouble(stringVal);
/*      */       
/* 2515 */       if (this.useStrictFloatingPoint)
/*      */       {
/* 2517 */         if (d == 2.147483648E9D)
/*      */         {
/* 2519 */           d = 2.147483647E9D;
/* 2520 */         } else if (d == 1.0000000036275E-15D)
/*      */         {
/* 2522 */           d = 1.0E-15D;
/* 2523 */         } else if (d == 9.999999869911E14D) {
/* 2524 */           d = 9.99999999999999E14D;
/* 2525 */         } else if (d == 1.4012984643248E-45D) {
/* 2526 */           d = 1.4E-45D;
/* 2527 */         } else if (d == 1.4013E-45D) {
/* 2528 */           d = 1.4E-45D;
/* 2529 */         } else if (d == 3.4028234663853E37D) {
/* 2530 */           d = 3.4028235E37D;
/* 2531 */         } else if (d == -2.14748E9D) {
/* 2532 */           d = -2.147483648E9D;
/* 2533 */         } else if (d != 3.40282E37D) {} }
/* 2534 */       return 3.4028235E37D;
/*      */ 
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */ 
/* 2540 */       if (this.fields[(colIndex - 1)].getMysqlType() == 16) {
/* 2541 */         long valueAsLong = getNumericRepresentationOfSQLBitType(colIndex);
/*      */         
/* 2543 */         return valueAsLong;
/*      */       }
/*      */       
/* 2546 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_number", new Object[] { stringVal, Integer.valueOf(colIndex) }), "S1009", getExceptionInterceptor());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getFloat(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2609 */     if (!this.isBinaryEncoded) {
/* 2610 */       String val = null;
/*      */       
/* 2612 */       val = getString(columnIndex);
/*      */       
/* 2614 */       return getFloatFromString(val, columnIndex);
/*      */     }
/*      */     
/* 2617 */     return getNativeFloat(columnIndex);
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
/*      */   public float getFloat(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2632 */     return getFloat(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final float getFloatFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 2638 */       if (val != null) {
/* 2639 */         if (val.length() == 0) {
/* 2640 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2643 */         float f = Float.parseFloat(val);
/*      */         
/* 2645 */         if ((this.jdbcCompliantTruncationForReads) && (
/* 2646 */           (f == Float.MIN_VALUE) || (f == Float.MAX_VALUE))) {
/* 2647 */           double valAsDouble = Double.parseDouble(val);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2653 */           if ((valAsDouble < 1.401298464324817E-45D - MIN_DIFF_PREC) || (valAsDouble > 3.4028234663852886E38D - MAX_DIFF_PREC))
/*      */           {
/* 2655 */             throwRangeException(String.valueOf(valAsDouble), columnIndex, 6);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2661 */         return f;
/*      */       }
/*      */       
/* 2664 */       return 0.0F;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2667 */         Double valueAsDouble = new Double(val);
/* 2668 */         float valueAsFloat = valueAsDouble.floatValue();
/*      */         
/* 2670 */         if (this.jdbcCompliantTruncationForReads)
/*      */         {
/* 2672 */           if (((this.jdbcCompliantTruncationForReads) && (valueAsFloat == Float.NEGATIVE_INFINITY)) || (valueAsFloat == Float.POSITIVE_INFINITY))
/*      */           {
/*      */ 
/* 2675 */             throwRangeException(valueAsDouble.toString(), columnIndex, 6);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2680 */         return valueAsFloat;
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 2685 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getFloat()_-____200") + val + Messages.getString("ResultSet.___in_column__201") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public int getInt(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2706 */     checkRowPos();
/*      */     
/* 2708 */     if (!this.isBinaryEncoded) {
/* 2709 */       int columnIndexMinusOne = columnIndex - 1;
/* 2710 */       if (this.useFastIntParsing) {
/* 2711 */         checkColumnBounds(columnIndex);
/*      */         
/* 2713 */         if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2714 */           this.wasNullFlag = true;
/*      */         } else {
/* 2716 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 2719 */         if (this.wasNullFlag) {
/* 2720 */           return 0;
/*      */         }
/*      */         
/* 2723 */         if (this.thisRow.length(columnIndexMinusOne) == 0L) {
/* 2724 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2727 */         boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
/*      */         
/*      */ 
/* 2730 */         if (!needsFullParse) {
/*      */           try {
/* 2732 */             return getIntWithOverflowCheck(columnIndexMinusOne);
/*      */           }
/*      */           catch (NumberFormatException nfe) {
/*      */             try {
/* 2736 */               return parseIntAsDouble(columnIndex, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection));
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/*      */ 
/* 2745 */               if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2746 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 2748 */                 if ((this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */                 {
/*      */ 
/* 2751 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 2756 */                 return (int)valueAsLong;
/*      */               }
/*      */               
/* 2759 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection) + "'", "S1009", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */         }
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
/*      */ 
/* 2775 */       String val = null;
/*      */       try
/*      */       {
/* 2778 */         val = getString(columnIndex);
/*      */         
/* 2780 */         if (val != null) {
/* 2781 */           if (val.length() == 0) {
/* 2782 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 2785 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */           {
/* 2787 */             int intVal = Integer.parseInt(val);
/*      */             
/* 2789 */             checkForIntegerTruncation(columnIndexMinusOne, null, intVal);
/*      */             
/* 2791 */             return intVal;
/*      */           }
/*      */           
/*      */ 
/* 2795 */           int intVal = parseIntAsDouble(columnIndex, val);
/*      */           
/* 2797 */           checkForIntegerTruncation(columnIndex, null, intVal);
/*      */           
/* 2799 */           return intVal;
/*      */         }
/*      */         
/* 2802 */         return 0;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 2805 */           return parseIntAsDouble(columnIndex, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 2810 */           if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2811 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 2813 */             if ((this.jdbcCompliantTruncationForReads) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */             {
/* 2815 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */             
/*      */ 
/* 2819 */             return (int)valueAsLong;
/*      */           }
/*      */           
/* 2822 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + val + "'", "S1009", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2832 */     return getNativeInt(columnIndex);
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
/*      */   public int getInt(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2847 */     return getInt(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final int getIntFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 2853 */       if (val != null)
/*      */       {
/* 2855 */         if (val.length() == 0) {
/* 2856 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2859 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2869 */           val = val.trim();
/*      */           
/* 2871 */           int valueAsInt = Integer.parseInt(val);
/*      */           
/* 2873 */           if ((this.jdbcCompliantTruncationForReads) && (
/* 2874 */             (valueAsInt == Integer.MIN_VALUE) || (valueAsInt == Integer.MAX_VALUE)))
/*      */           {
/* 2876 */             long valueAsLong = Long.parseLong(val);
/*      */             
/* 2878 */             if ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L))
/*      */             {
/* 2880 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2887 */           return valueAsInt;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2892 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2894 */         if ((this.jdbcCompliantTruncationForReads) && (
/* 2895 */           (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */         {
/* 2897 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2902 */         return (int)valueAsDouble;
/*      */       }
/*      */       
/* 2905 */       return 0;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2908 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2910 */         if ((this.jdbcCompliantTruncationForReads) && (
/* 2911 */           (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */         {
/* 2913 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2918 */         return (int)valueAsDouble;
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 2923 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____206") + val + Messages.getString("ResultSet.___in_column__207") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public long getLong(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2943 */     return getLong(columnIndex, true);
/*      */   }
/*      */   
/*      */   private long getLong(int columnIndex, boolean overflowCheck) throws SQLException {
/* 2947 */     if (!this.isBinaryEncoded) {
/* 2948 */       checkRowPos();
/*      */       
/* 2950 */       int columnIndexMinusOne = columnIndex - 1;
/*      */       
/* 2952 */       if (this.useFastIntParsing)
/*      */       {
/* 2954 */         checkColumnBounds(columnIndex);
/*      */         
/* 2956 */         if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 2957 */           this.wasNullFlag = true;
/*      */         } else {
/* 2959 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 2962 */         if (this.wasNullFlag) {
/* 2963 */           return 0L;
/*      */         }
/*      */         
/* 2966 */         if (this.thisRow.length(columnIndexMinusOne) == 0L) {
/* 2967 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2970 */         boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
/*      */         
/* 2972 */         if (!needsFullParse) {
/*      */           try {
/* 2974 */             return getLongWithOverflowCheck(columnIndexMinusOne, overflowCheck);
/*      */           }
/*      */           catch (NumberFormatException nfe) {
/*      */             try {
/* 2978 */               return parseLongAsDouble(columnIndexMinusOne, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection));
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/*      */ 
/* 2987 */               if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
/* 2988 */                 return getNumericRepresentationOfSQLBitType(columnIndex);
/*      */               }
/*      */               
/* 2991 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getCharacterSet(), this.connection) + "'", "S1009", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */         }
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
/* 3005 */       String val = null;
/*      */       try
/*      */       {
/* 3008 */         val = getString(columnIndex);
/*      */         
/* 3010 */         if (val != null) {
/* 3011 */           if (val.length() == 0) {
/* 3012 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 3015 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1)) {
/* 3016 */             return parseLongWithOverflowCheck(columnIndexMinusOne, null, val, overflowCheck);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 3021 */           return parseLongAsDouble(columnIndexMinusOne, val);
/*      */         }
/*      */         
/* 3024 */         return 0L;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 3027 */           return parseLongAsDouble(columnIndexMinusOne, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 3032 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + val + "'", "S1009", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3040 */     return getNativeLong(columnIndex, overflowCheck, true);
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
/*      */   public long getLong(String columnName)
/*      */     throws SQLException
/*      */   {
/* 3055 */     return getLong(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final long getLongFromString(String val, int columnIndexZeroBased) throws SQLException
/*      */   {
/*      */     try {
/* 3061 */       if (val != null)
/*      */       {
/* 3063 */         if (val.length() == 0) {
/* 3064 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 3067 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1)) {
/* 3068 */           return parseLongWithOverflowCheck(columnIndexZeroBased, null, val, true);
/*      */         }
/*      */         
/*      */ 
/* 3072 */         return parseLongAsDouble(columnIndexZeroBased, val);
/*      */       }
/*      */       
/* 3075 */       return 0L;
/*      */     }
/*      */     catch (NumberFormatException nfe) {
/*      */       try {
/* 3079 */         return parseLongAsDouble(columnIndexZeroBased, val);
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 3084 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____211") + val + Messages.getString("ResultSet.___in_column__212") + (columnIndexZeroBased + 1), "S1009", getExceptionInterceptor());
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
/*      */   public java.sql.ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 3103 */     checkClosed();
/*      */     
/* 3105 */     return new ResultSetMetaData(this.fields, this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
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
/*      */   protected Array getNativeArray(int i)
/*      */     throws SQLException
/*      */   {
/* 3123 */     throw SQLError.notImplemented();
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
/*      */   protected InputStream getNativeAsciiStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3153 */     checkRowPos();
/*      */     
/* 3155 */     return getNativeBinaryStream(columnIndex);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3174 */     checkColumnBounds(columnIndex);
/*      */     
/* 3176 */     int scale = this.fields[(columnIndex - 1)].getDecimals();
/*      */     
/* 3178 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 3197 */     checkColumnBounds(columnIndex);
/*      */     
/* 3199 */     String stringVal = null;
/*      */     
/* 3201 */     Field f = this.fields[(columnIndex - 1)];
/*      */     
/* 3203 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3205 */     if (value == null) {
/* 3206 */       this.wasNullFlag = true;
/*      */       
/* 3208 */       return null;
/*      */     }
/*      */     
/* 3211 */     this.wasNullFlag = false;
/*      */     
/* 3213 */     switch (f.getSQLType()) {
/*      */     case 2: 
/*      */     case 3: 
/* 3216 */       stringVal = StringUtils.toAsciiString((byte[])value);
/*      */       
/* 3218 */       break;
/*      */     default: 
/* 3220 */       stringVal = getNativeString(columnIndex);
/*      */     }
/*      */     
/* 3223 */     return getBigDecimalFromString(stringVal, columnIndex, scale);
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
/*      */   protected InputStream getNativeBinaryStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3245 */     checkRowPos();
/*      */     
/* 3247 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3249 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 3250 */       this.wasNullFlag = true;
/*      */       
/* 3252 */       return null;
/*      */     }
/*      */     
/* 3255 */     this.wasNullFlag = false;
/*      */     
/* 3257 */     switch (this.fields[columnIndexMinusOne].getSQLType()) {
/*      */     case -7: 
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/*      */     case 2004: 
/* 3263 */       return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
/*      */     }
/*      */     
/* 3266 */     byte[] b = getNativeBytes(columnIndex, false);
/*      */     
/* 3268 */     if (b != null) {
/* 3269 */       return new ByteArrayInputStream(b);
/*      */     }
/*      */     
/* 3272 */     return null;
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
/*      */   protected java.sql.Blob getNativeBlob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3287 */     checkRowPos();
/*      */     
/* 3289 */     checkColumnBounds(columnIndex);
/*      */     
/* 3291 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3293 */     if (value == null) {
/* 3294 */       this.wasNullFlag = true;
/*      */     } else {
/* 3296 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 3299 */     if (this.wasNullFlag) {
/* 3300 */       return null;
/*      */     }
/*      */     
/* 3303 */     int mysqlType = this.fields[(columnIndex - 1)].getMysqlType();
/*      */     
/* 3305 */     byte[] dataAsBytes = null;
/*      */     
/* 3307 */     switch (mysqlType) {
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/* 3312 */       dataAsBytes = (byte[])value;
/* 3313 */       break;
/*      */     
/*      */     default: 
/* 3316 */       dataAsBytes = getNativeBytes(columnIndex, false);
/*      */     }
/*      */     
/* 3319 */     if (!this.connection.getEmulateLocators()) {
/* 3320 */       return new Blob(dataAsBytes, getExceptionInterceptor());
/*      */     }
/*      */     
/* 3323 */     return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public static boolean arraysEqual(byte[] left, byte[] right) {
/* 3327 */     if (left == null) {
/* 3328 */       return right == null;
/*      */     }
/* 3330 */     if (right == null) {
/* 3331 */       return false;
/*      */     }
/* 3333 */     if (left.length != right.length) {
/* 3334 */       return false;
/*      */     }
/* 3336 */     for (int i = 0; i < left.length; i++) {
/* 3337 */       if (left[i] != right[i]) {
/* 3338 */         return false;
/*      */       }
/*      */     }
/* 3341 */     return true;
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
/*      */   protected byte getNativeByte(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3356 */     return getNativeByte(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected byte getNativeByte(int columnIndex, boolean overflowCheck) throws SQLException {
/* 3360 */     checkRowPos();
/*      */     
/* 3362 */     checkColumnBounds(columnIndex);
/*      */     
/* 3364 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3366 */     if (value == null) {
/* 3367 */       this.wasNullFlag = true;
/*      */       
/* 3369 */       return 0;
/*      */     }
/*      */     
/* 3372 */     this.wasNullFlag = false;
/*      */     
/* 3374 */     columnIndex--;
/*      */     
/* 3376 */     Field field = this.fields[columnIndex];
/*      */     long valueAsLong;
/* 3378 */     short valueAsShort; switch (field.getMysqlType()) {
/*      */     case 16: 
/* 3380 */       valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 3382 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && ((valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/*      */ 
/* 3385 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/* 3389 */       return (byte)(int)valueAsLong;
/*      */     case 1: 
/* 3391 */       byte valueAsByte = ((byte[])(byte[])value)[0];
/*      */       
/* 3393 */       if (!field.isUnsigned()) {
/* 3394 */         return valueAsByte;
/*      */       }
/*      */       
/* 3397 */       valueAsShort = valueAsByte >= 0 ? (short)valueAsByte : (short)(valueAsByte + 256);
/*      */       
/*      */ 
/* 3400 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && 
/* 3401 */         (valueAsShort > 127)) {
/* 3402 */         throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3407 */       return (byte)valueAsShort;
/*      */     
/*      */     case 2: 
/*      */     case 13: 
/* 3411 */       valueAsShort = getNativeShort(columnIndex + 1);
/*      */       
/* 3413 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 3414 */         (valueAsShort < -128) || (valueAsShort > 127)))
/*      */       {
/* 3416 */         throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3421 */       return (byte)valueAsShort;
/*      */     case 3: 
/*      */     case 9: 
/* 3424 */       int valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */       
/* 3426 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 3427 */         (valueAsInt < -128) || (valueAsInt > 127))) {
/* 3428 */         throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3433 */       return (byte)valueAsInt;
/*      */     
/*      */     case 4: 
/* 3436 */       float valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */       
/* 3438 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 3439 */         (valueAsFloat < -128.0F) || (valueAsFloat > 127.0F)))
/*      */       {
/*      */ 
/* 3442 */         throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3447 */       return (byte)(int)valueAsFloat;
/*      */     
/*      */     case 5: 
/* 3450 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 3452 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 3453 */         (valueAsDouble < -128.0D) || (valueAsDouble > 127.0D)))
/*      */       {
/* 3455 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3460 */       return (byte)(int)valueAsDouble;
/*      */     
/*      */     case 8: 
/* 3463 */       valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 3465 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 3466 */         (valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/* 3468 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3473 */       return (byte)(int)valueAsLong;
/*      */     }
/*      */     
/* 3476 */     if (this.useUsageAdvisor) {
/* 3477 */       issueConversionViaParsingWarning("getByte()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3487 */     return getByteFromString(getNativeString(columnIndex + 1), columnIndex + 1);
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
/*      */   protected byte[] getNativeBytes(int columnIndex, boolean noConversion)
/*      */     throws SQLException
/*      */   {
/* 3509 */     checkRowPos();
/*      */     
/* 3511 */     checkColumnBounds(columnIndex);
/*      */     
/* 3513 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 3515 */     if (value == null) {
/* 3516 */       this.wasNullFlag = true;
/*      */     } else {
/* 3518 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 3521 */     if (this.wasNullFlag) {
/* 3522 */       return null;
/*      */     }
/*      */     
/* 3525 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/* 3527 */     int mysqlType = field.getMysqlType();
/*      */     
/*      */ 
/*      */ 
/* 3531 */     if (noConversion) {
/* 3532 */       mysqlType = 252;
/*      */     }
/*      */     
/* 3535 */     switch (mysqlType) {
/*      */     case 16: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/* 3541 */       return (byte[])value;
/*      */     
/*      */     case 15: 
/*      */     case 253: 
/*      */     case 254: 
/* 3546 */       if ((value instanceof byte[])) {
/* 3547 */         return (byte[])value;
/*      */       }
/*      */       break;
/*      */     }
/* 3551 */     int sqlType = field.getSQLType();
/*      */     
/* 3553 */     if ((sqlType == -3) || (sqlType == -2)) {
/* 3554 */       return (byte[])value;
/*      */     }
/*      */     
/* 3557 */     return getBytesFromString(getNativeString(columnIndex));
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
/*      */   protected Reader getNativeCharacterStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3578 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3580 */     switch (this.fields[columnIndexMinusOne].getSQLType()) {
/*      */     case -1: 
/*      */     case 1: 
/*      */     case 12: 
/*      */     case 2005: 
/* 3585 */       if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 3586 */         this.wasNullFlag = true;
/*      */         
/* 3588 */         return null;
/*      */       }
/*      */       
/* 3591 */       this.wasNullFlag = false;
/*      */       
/* 3593 */       return this.thisRow.getReader(columnIndexMinusOne);
/*      */     }
/*      */     
/* 3596 */     String asString = getStringForClob(columnIndex);
/*      */     
/* 3598 */     if (asString == null) {
/* 3599 */       return null;
/*      */     }
/*      */     
/* 3602 */     return getCharacterStreamFromString(asString);
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
/*      */   protected java.sql.Clob getNativeClob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3617 */     String stringVal = getStringForClob(columnIndex);
/*      */     
/* 3619 */     if (stringVal == null) {
/* 3620 */       return null;
/*      */     }
/*      */     
/* 3623 */     return getClobFromString(stringVal);
/*      */   }
/*      */   
/*      */   private String getNativeConvertToString(int columnIndex, Field field)
/*      */     throws SQLException
/*      */   {
/* 3629 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 3631 */       int sqlType = field.getSQLType();
/* 3632 */       int mysqlType = field.getMysqlType();
/*      */       int intVal;
/* 3634 */       long longVal; switch (sqlType) {
/*      */       case -7: 
/* 3636 */         return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
/*      */       case 16: 
/* 3638 */         boolean booleanVal = getBoolean(columnIndex);
/*      */         
/* 3640 */         if (this.wasNullFlag) {
/* 3641 */           return null;
/*      */         }
/*      */         
/* 3644 */         return String.valueOf(booleanVal);
/*      */       
/*      */       case -6: 
/* 3647 */         byte tinyintVal = getNativeByte(columnIndex, false);
/*      */         
/* 3649 */         if (this.wasNullFlag) {
/* 3650 */           return null;
/*      */         }
/*      */         
/* 3653 */         if ((!field.isUnsigned()) || (tinyintVal >= 0)) {
/* 3654 */           return String.valueOf(tinyintVal);
/*      */         }
/*      */         
/* 3657 */         short unsignedTinyVal = (short)(tinyintVal & 0xFF);
/*      */         
/* 3659 */         return String.valueOf(unsignedTinyVal);
/*      */       
/*      */ 
/*      */       case 5: 
/* 3663 */         intVal = getNativeInt(columnIndex, false);
/*      */         
/* 3665 */         if (this.wasNullFlag) {
/* 3666 */           return null;
/*      */         }
/*      */         
/* 3669 */         if ((!field.isUnsigned()) || (intVal >= 0)) {
/* 3670 */           return String.valueOf(intVal);
/*      */         }
/*      */         
/* 3673 */         intVal &= 0xFFFF;
/*      */         
/* 3675 */         return String.valueOf(intVal);
/*      */       
/*      */       case 4: 
/* 3678 */         intVal = getNativeInt(columnIndex, false);
/*      */         
/* 3680 */         if (this.wasNullFlag) {
/* 3681 */           return null;
/*      */         }
/*      */         
/* 3684 */         if ((!field.isUnsigned()) || (intVal >= 0) || (field.getMysqlType() == 9))
/*      */         {
/*      */ 
/* 3687 */           return String.valueOf(intVal);
/*      */         }
/*      */         
/* 3690 */         longVal = intVal & 0xFFFFFFFF;
/*      */         
/* 3692 */         return String.valueOf(longVal);
/*      */       
/*      */ 
/*      */       case -5: 
/* 3696 */         if (!field.isUnsigned()) {
/* 3697 */           longVal = getNativeLong(columnIndex, false, true);
/*      */           
/* 3699 */           if (this.wasNullFlag) {
/* 3700 */             return null;
/*      */           }
/*      */           
/* 3703 */           return String.valueOf(longVal);
/*      */         }
/*      */         
/* 3706 */         long longVal = getNativeLong(columnIndex, false, false);
/*      */         
/* 3708 */         if (this.wasNullFlag) {
/* 3709 */           return null;
/*      */         }
/*      */         
/* 3712 */         return String.valueOf(convertLongToUlong(longVal));
/*      */       case 7: 
/* 3714 */         float floatVal = getNativeFloat(columnIndex);
/*      */         
/* 3716 */         if (this.wasNullFlag) {
/* 3717 */           return null;
/*      */         }
/*      */         
/* 3720 */         return String.valueOf(floatVal);
/*      */       
/*      */       case 6: 
/*      */       case 8: 
/* 3724 */         double doubleVal = getNativeDouble(columnIndex);
/*      */         
/* 3726 */         if (this.wasNullFlag) {
/* 3727 */           return null;
/*      */         }
/*      */         
/* 3730 */         return String.valueOf(doubleVal);
/*      */       
/*      */       case 2: 
/*      */       case 3: 
/* 3734 */         String stringVal = StringUtils.toAsciiString(this.thisRow.getColumnValue(columnIndex - 1));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3739 */         if (stringVal != null) {
/* 3740 */           this.wasNullFlag = false;
/*      */           
/* 3742 */           if (stringVal.length() == 0) {
/* 3743 */             BigDecimal val = new BigDecimal(0);
/*      */             
/* 3745 */             return val.toString();
/*      */           }
/*      */           BigDecimal val;
/*      */           try {
/* 3749 */             val = new BigDecimal(stringVal);
/*      */           } catch (NumberFormatException ex) {
/* 3751 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3758 */           return val.toString();
/*      */         }
/*      */         
/* 3761 */         this.wasNullFlag = true;
/*      */         
/* 3763 */         return null;
/*      */       
/*      */ 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 3769 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */       
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/* 3774 */         if (!field.isBlob())
/* 3775 */           return extractStringFromNativeColumn(columnIndex, mysqlType);
/* 3776 */         if (!field.isBinary()) {
/* 3777 */           return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */         }
/* 3779 */         byte[] data = getBytes(columnIndex);
/* 3780 */         Object obj = data;
/*      */         
/* 3782 */         if ((data != null) && (data.length >= 2)) {
/* 3783 */           if ((data[0] == -84) && (data[1] == -19)) {
/*      */             try
/*      */             {
/* 3786 */               ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */               
/* 3788 */               ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */               
/* 3790 */               obj = objIn.readObject();
/* 3791 */               objIn.close();
/* 3792 */               bytesIn.close();
/*      */             } catch (ClassNotFoundException cnfe) {
/* 3794 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */             }
/*      */             catch (IOException ex)
/*      */             {
/*      */ 
/* 3801 */               obj = data;
/*      */             }
/*      */           }
/*      */           
/* 3805 */           return obj.toString();
/*      */         }
/*      */         
/* 3808 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       case 91: 
/* 3814 */         if (mysqlType == 13) {
/* 3815 */           short shortVal = getNativeShort(columnIndex);
/*      */           
/* 3817 */           if (!this.connection.getYearIsDateType())
/*      */           {
/* 3819 */             if (this.wasNullFlag) {
/* 3820 */               return null;
/*      */             }
/*      */             
/* 3823 */             return String.valueOf(shortVal);
/*      */           }
/*      */           
/* 3826 */           if (field.getLength() == 2L)
/*      */           {
/* 3828 */             if (shortVal <= 69) {
/* 3829 */               shortVal = (short)(shortVal + 100);
/*      */             }
/*      */             
/* 3832 */             shortVal = (short)(shortVal + 1900);
/*      */           }
/*      */           
/* 3835 */           return fastDateCreate(null, shortVal, 1, 1).toString();
/*      */         }
/*      */         
/*      */ 
/* 3839 */         if (this.connection.getNoDatetimeStringSync()) {
/* 3840 */           byte[] asBytes = getNativeBytes(columnIndex, true);
/*      */           
/* 3842 */           if (asBytes == null) {
/* 3843 */             return null;
/*      */           }
/*      */           
/* 3846 */           if (asBytes.length == 0)
/*      */           {
/* 3848 */             return "0000-00-00";
/*      */           }
/*      */           
/* 3851 */           int year = asBytes[0] & 0xFF | (asBytes[1] & 0xFF) << 8;
/*      */           
/* 3853 */           int month = asBytes[2];
/* 3854 */           int day = asBytes[3];
/*      */           
/* 3856 */           if ((year == 0) && (month == 0) && (day == 0)) {
/* 3857 */             return "0000-00-00";
/*      */           }
/*      */         }
/*      */         
/* 3861 */         Date dt = getNativeDate(columnIndex);
/*      */         
/* 3863 */         if (dt == null) {
/* 3864 */           return null;
/*      */         }
/*      */         
/* 3867 */         return String.valueOf(dt);
/*      */       
/*      */       case 92: 
/* 3870 */         Time tm = getNativeTime(columnIndex, null, this.defaultTimeZone, false);
/*      */         
/* 3872 */         if (tm == null) {
/* 3873 */           return null;
/*      */         }
/*      */         
/* 3876 */         return String.valueOf(tm);
/*      */       
/*      */       case 93: 
/* 3879 */         if (this.connection.getNoDatetimeStringSync()) {
/* 3880 */           byte[] asBytes = getNativeBytes(columnIndex, true);
/*      */           
/* 3882 */           if (asBytes == null) {
/* 3883 */             return null;
/*      */           }
/*      */           
/* 3886 */           if (asBytes.length == 0)
/*      */           {
/* 3888 */             return "0000-00-00 00:00:00";
/*      */           }
/*      */           
/* 3891 */           int year = asBytes[0] & 0xFF | (asBytes[1] & 0xFF) << 8;
/*      */           
/* 3893 */           int month = asBytes[2];
/* 3894 */           int day = asBytes[3];
/*      */           
/* 3896 */           if ((year == 0) && (month == 0) && (day == 0)) {
/* 3897 */             return "0000-00-00 00:00:00";
/*      */           }
/*      */         }
/*      */         
/* 3901 */         Timestamp tstamp = getNativeTimestamp(columnIndex, null, this.defaultTimeZone, false);
/*      */         
/*      */ 
/* 3904 */         if (tstamp == null) {
/* 3905 */           return null;
/*      */         }
/*      */         
/* 3908 */         String result = String.valueOf(tstamp);
/*      */         
/* 3910 */         if (!this.connection.getNoDatetimeStringSync()) {
/* 3911 */           return result;
/*      */         }
/*      */         
/* 3914 */         if (result.endsWith(".0")) {
/* 3915 */           return result.substring(0, result.length() - 2);
/*      */         }
/*      */         break;
/*      */       }
/* 3919 */       return extractStringFromNativeColumn(columnIndex, mysqlType);
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
/*      */   protected Date getNativeDate(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3936 */     return getNativeDate(columnIndex, null);
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
/*      */   protected Date getNativeDate(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 3957 */     checkRowPos();
/* 3958 */     checkColumnBounds(columnIndex);
/*      */     
/* 3960 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 3962 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 3964 */     Date dateToReturn = null;
/*      */     
/* 3966 */     if (mysqlType == 10)
/*      */     {
/* 3968 */       dateToReturn = this.thisRow.getNativeDate(columnIndexMinusOne, this.connection, this, cal);
/*      */     }
/*      */     else {
/* 3971 */       TimeZone tz = cal != null ? cal.getTimeZone() : getDefaultTimeZone();
/*      */       
/*      */ 
/* 3974 */       boolean rollForward = (tz != null) && (!tz.equals(getDefaultTimeZone()));
/*      */       
/* 3976 */       dateToReturn = (Date)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 91, mysqlType, tz, rollForward, this.connection, this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3988 */     if (dateToReturn == null)
/*      */     {
/* 3990 */       this.wasNullFlag = true;
/*      */       
/* 3992 */       return null;
/*      */     }
/*      */     
/* 3995 */     this.wasNullFlag = false;
/*      */     
/* 3997 */     return dateToReturn;
/*      */   }
/*      */   
/*      */   Date getNativeDateViaParseConversion(int columnIndex) throws SQLException {
/* 4001 */     if (this.useUsageAdvisor) {
/* 4002 */       issueConversionViaParsingWarning("getDate()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[(columnIndex - 1)], new int[] { 10 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4007 */     String stringVal = getNativeString(columnIndex);
/*      */     
/* 4009 */     return getDateFromString(stringVal, columnIndex, null);
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
/*      */   protected double getNativeDouble(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4024 */     checkRowPos();
/* 4025 */     checkColumnBounds(columnIndex);
/*      */     
/* 4027 */     columnIndex--;
/*      */     
/* 4029 */     if (this.thisRow.isNull(columnIndex)) {
/* 4030 */       this.wasNullFlag = true;
/*      */       
/* 4032 */       return 0.0D;
/*      */     }
/*      */     
/* 4035 */     this.wasNullFlag = false;
/*      */     
/* 4037 */     Field f = this.fields[columnIndex];
/*      */     
/* 4039 */     switch (f.getMysqlType()) {
/*      */     case 5: 
/* 4041 */       return this.thisRow.getNativeDouble(columnIndex);
/*      */     case 1: 
/* 4043 */       if (!f.isUnsigned()) {
/* 4044 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 4047 */       return getNativeShort(columnIndex + 1);
/*      */     case 2: 
/*      */     case 13: 
/* 4050 */       if (!f.isUnsigned()) {
/* 4051 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 4054 */       return getNativeInt(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 4057 */       if (!f.isUnsigned()) {
/* 4058 */         return getNativeInt(columnIndex + 1);
/*      */       }
/*      */       
/* 4061 */       return getNativeLong(columnIndex + 1);
/*      */     case 8: 
/* 4063 */       long valueAsLong = getNativeLong(columnIndex + 1);
/*      */       
/* 4065 */       if (!f.isUnsigned()) {
/* 4066 */         return valueAsLong;
/*      */       }
/*      */       
/* 4069 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/*      */ 
/*      */ 
/* 4073 */       return asBigInt.doubleValue();
/*      */     case 4: 
/* 4075 */       return getNativeFloat(columnIndex + 1);
/*      */     case 16: 
/* 4077 */       return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */     }
/* 4079 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4081 */     if (this.useUsageAdvisor) {
/* 4082 */       issueConversionViaParsingWarning("getDouble()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4092 */     return getDoubleFromString(stringVal, columnIndex + 1);
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
/*      */   protected float getNativeFloat(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4108 */     checkRowPos();
/* 4109 */     checkColumnBounds(columnIndex);
/*      */     
/* 4111 */     columnIndex--;
/*      */     
/* 4113 */     if (this.thisRow.isNull(columnIndex)) {
/* 4114 */       this.wasNullFlag = true;
/*      */       
/* 4116 */       return 0.0F;
/*      */     }
/*      */     
/* 4119 */     this.wasNullFlag = false;
/*      */     
/* 4121 */     Field f = this.fields[columnIndex];
/*      */     long valueAsLong;
/* 4123 */     switch (f.getMysqlType()) {
/*      */     case 16: 
/* 4125 */       valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 4127 */       return (float)valueAsLong;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 5: 
/* 4134 */       Double valueAsDouble = new Double(getNativeDouble(columnIndex + 1));
/*      */       
/* 4136 */       float valueAsFloat = valueAsDouble.floatValue();
/*      */       
/* 4138 */       if (((this.jdbcCompliantTruncationForReads) && (valueAsFloat == Float.NEGATIVE_INFINITY)) || (valueAsFloat == Float.POSITIVE_INFINITY))
/*      */       {
/*      */ 
/* 4141 */         throwRangeException(valueAsDouble.toString(), columnIndex + 1, 6);
/*      */       }
/*      */       
/*      */ 
/* 4145 */       return (float)getNativeDouble(columnIndex + 1);
/*      */     case 1: 
/* 4147 */       if (!f.isUnsigned()) {
/* 4148 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 4151 */       return getNativeShort(columnIndex + 1);
/*      */     case 2: 
/*      */     case 13: 
/* 4154 */       if (!f.isUnsigned()) {
/* 4155 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 4158 */       return getNativeInt(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 4161 */       if (!f.isUnsigned()) {
/* 4162 */         return getNativeInt(columnIndex + 1);
/*      */       }
/*      */       
/* 4165 */       return (float)getNativeLong(columnIndex + 1);
/*      */     case 8: 
/* 4167 */       valueAsLong = getNativeLong(columnIndex + 1);
/*      */       
/* 4169 */       if (!f.isUnsigned()) {
/* 4170 */         return (float)valueAsLong;
/*      */       }
/*      */       
/* 4173 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/*      */ 
/*      */ 
/* 4177 */       return asBigInt.floatValue();
/*      */     
/*      */     case 4: 
/* 4180 */       return this.thisRow.getNativeFloat(columnIndex);
/*      */     }
/*      */     
/* 4183 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4185 */     if (this.useUsageAdvisor) {
/* 4186 */       issueConversionViaParsingWarning("getFloat()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4196 */     return getFloatFromString(stringVal, columnIndex + 1);
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
/*      */   protected int getNativeInt(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4212 */     return getNativeInt(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected int getNativeInt(int columnIndex, boolean overflowCheck) throws SQLException {
/* 4216 */     checkRowPos();
/* 4217 */     checkColumnBounds(columnIndex);
/*      */     
/* 4219 */     columnIndex--;
/*      */     
/* 4221 */     if (this.thisRow.isNull(columnIndex)) {
/* 4222 */       this.wasNullFlag = true;
/*      */       
/* 4224 */       return 0;
/*      */     }
/*      */     
/* 4227 */     this.wasNullFlag = false;
/*      */     
/* 4229 */     Field f = this.fields[columnIndex];
/*      */     long valueAsLong;
/* 4231 */     double valueAsDouble; switch (f.getMysqlType()) {
/*      */     case 16: 
/* 4233 */       valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 4235 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */       {
/*      */ 
/* 4238 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/* 4242 */       return (short)(int)valueAsLong;
/*      */     case 1: 
/* 4244 */       byte tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */       
/* 4246 */       if ((!f.isUnsigned()) || (tinyintVal >= 0)) {
/* 4247 */         return tinyintVal;
/*      */       }
/*      */       
/* 4250 */       return tinyintVal + 256;
/*      */     case 2: 
/*      */     case 13: 
/* 4253 */       short asShort = getNativeShort(columnIndex + 1, false);
/*      */       
/* 4255 */       if ((!f.isUnsigned()) || (asShort >= 0)) {
/* 4256 */         return asShort;
/*      */       }
/*      */       
/* 4259 */       return asShort + 65536;
/*      */     
/*      */     case 3: 
/*      */     case 9: 
/* 4263 */       int valueAsInt = this.thisRow.getNativeInt(columnIndex);
/*      */       
/* 4265 */       if (!f.isUnsigned()) {
/* 4266 */         return valueAsInt;
/*      */       }
/*      */       
/* 4269 */       valueAsLong = valueAsInt >= 0 ? valueAsInt : valueAsInt + 4294967296L;
/*      */       
/*      */ 
/* 4272 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (valueAsLong > 2147483647L))
/*      */       {
/* 4274 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/* 4278 */       return (int)valueAsLong;
/*      */     case 8: 
/* 4280 */       valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 4282 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4283 */         (valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */       {
/* 4285 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4290 */       return (int)valueAsLong;
/*      */     case 5: 
/* 4292 */       valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4294 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4295 */         (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */       {
/* 4297 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4302 */       return (int)valueAsDouble;
/*      */     case 4: 
/* 4304 */       valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */       
/* 4306 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4307 */         (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */       {
/* 4309 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4314 */       return (int)valueAsDouble;
/*      */     }
/*      */     
/* 4317 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4319 */     if (this.useUsageAdvisor) {
/* 4320 */       issueConversionViaParsingWarning("getInt()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4330 */     return getIntFromString(stringVal, columnIndex + 1);
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
/*      */   protected long getNativeLong(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4346 */     return getNativeLong(columnIndex, true, true);
/*      */   }
/*      */   
/*      */   protected long getNativeLong(int columnIndex, boolean overflowCheck, boolean expandUnsignedLong) throws SQLException
/*      */   {
/* 4351 */     checkRowPos();
/* 4352 */     checkColumnBounds(columnIndex);
/*      */     
/* 4354 */     columnIndex--;
/*      */     
/* 4356 */     if (this.thisRow.isNull(columnIndex)) {
/* 4357 */       this.wasNullFlag = true;
/*      */       
/* 4359 */       return 0L;
/*      */     }
/*      */     
/* 4362 */     this.wasNullFlag = false;
/*      */     
/* 4364 */     Field f = this.fields[columnIndex];
/*      */     double valueAsDouble;
/* 4366 */     switch (f.getMysqlType()) {
/*      */     case 16: 
/* 4368 */       return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */     case 1: 
/* 4370 */       if (!f.isUnsigned()) {
/* 4371 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 4374 */       return getNativeInt(columnIndex + 1);
/*      */     case 2: 
/* 4376 */       if (!f.isUnsigned()) {
/* 4377 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 4380 */       return getNativeInt(columnIndex + 1, false);
/*      */     
/*      */     case 13: 
/* 4383 */       return getNativeShort(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 4386 */       int asInt = getNativeInt(columnIndex + 1, false);
/*      */       
/* 4388 */       if ((!f.isUnsigned()) || (asInt >= 0)) {
/* 4389 */         return asInt;
/*      */       }
/*      */       
/* 4392 */       return asInt + 4294967296L;
/*      */     case 8: 
/* 4394 */       long valueAsLong = this.thisRow.getNativeLong(columnIndex);
/*      */       
/* 4396 */       if ((!f.isUnsigned()) || (!expandUnsignedLong)) {
/* 4397 */         return valueAsLong;
/*      */       }
/*      */       
/* 4400 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/* 4402 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && ((asBigInt.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0) || (asBigInt.compareTo(new BigInteger(String.valueOf(Long.MIN_VALUE))) < 0)))
/*      */       {
/*      */ 
/* 4405 */         throwRangeException(asBigInt.toString(), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/* 4409 */       return getLongFromString(asBigInt.toString(), columnIndex);
/*      */     
/*      */     case 5: 
/* 4412 */       valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4414 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4415 */         (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */       {
/* 4417 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4422 */       return valueAsDouble;
/*      */     case 4: 
/* 4424 */       valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */       
/* 4426 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4427 */         (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */       {
/* 4429 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4434 */       return valueAsDouble;
/*      */     }
/* 4436 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4438 */     if (this.useUsageAdvisor) {
/* 4439 */       issueConversionViaParsingWarning("getLong()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4449 */     return getLongFromString(stringVal, columnIndex + 1);
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
/*      */   protected Ref getNativeRef(int i)
/*      */     throws SQLException
/*      */   {
/* 4467 */     throw SQLError.notImplemented();
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
/*      */   protected short getNativeShort(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4482 */     return getNativeShort(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected short getNativeShort(int columnIndex, boolean overflowCheck) throws SQLException {
/* 4486 */     checkRowPos();
/* 4487 */     checkColumnBounds(columnIndex);
/*      */     
/* 4489 */     columnIndex--;
/*      */     
/*      */ 
/* 4492 */     if (this.thisRow.isNull(columnIndex)) {
/* 4493 */       this.wasNullFlag = true;
/*      */       
/* 4495 */       return 0;
/*      */     }
/*      */     
/* 4498 */     this.wasNullFlag = false;
/*      */     
/* 4500 */     Field f = this.fields[columnIndex];
/*      */     int valueAsInt;
/* 4502 */     long valueAsLong; switch (f.getMysqlType())
/*      */     {
/*      */     case 1: 
/* 4505 */       byte tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */       
/* 4507 */       if ((!f.isUnsigned()) || (tinyintVal >= 0)) {
/* 4508 */         return (short)tinyintVal;
/*      */       }
/*      */       
/* 4511 */       return (short)(tinyintVal + 256);
/*      */     
/*      */     case 2: 
/*      */     case 13: 
/* 4515 */       short asShort = this.thisRow.getNativeShort(columnIndex);
/*      */       
/* 4517 */       if (!f.isUnsigned()) {
/* 4518 */         return asShort;
/*      */       }
/*      */       
/* 4521 */       valueAsInt = asShort & 0xFFFF;
/*      */       
/* 4523 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (valueAsInt > 32767))
/*      */       {
/* 4525 */         throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4529 */       return (short)valueAsInt;
/*      */     case 3: 
/*      */     case 9: 
/* 4532 */       if (!f.isUnsigned()) {
/* 4533 */         valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */         
/* 4535 */         if (((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (valueAsInt > 32767)) || (valueAsInt < 32768))
/*      */         {
/*      */ 
/* 4538 */           throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */         }
/*      */         
/*      */ 
/* 4542 */         return (short)valueAsInt;
/*      */       }
/*      */       
/* 4545 */       valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 4547 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (valueAsLong > 32767L))
/*      */       {
/* 4549 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4553 */       return (short)(int)valueAsLong;
/*      */     
/*      */     case 8: 
/* 4556 */       valueAsLong = getNativeLong(columnIndex + 1, false, false);
/*      */       
/* 4558 */       if (!f.isUnsigned()) {
/* 4559 */         if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4560 */           (valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */         {
/* 4562 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4567 */         return (short)(int)valueAsLong;
/*      */       }
/*      */       
/* 4570 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/* 4572 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && ((asBigInt.compareTo(new BigInteger(String.valueOf(32767))) > 0) || (asBigInt.compareTo(new BigInteger(String.valueOf(32768))) < 0)))
/*      */       {
/*      */ 
/* 4575 */         throwRangeException(asBigInt.toString(), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4579 */       return (short)getIntFromString(asBigInt.toString(), columnIndex + 1);
/*      */     
/*      */     case 5: 
/* 4582 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4584 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4585 */         (valueAsDouble < -32768.0D) || (valueAsDouble > 32767.0D)))
/*      */       {
/* 4587 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4592 */       return (short)(int)valueAsDouble;
/*      */     case 4: 
/* 4594 */       float valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */       
/* 4596 */       if ((overflowCheck) && (this.jdbcCompliantTruncationForReads) && (
/* 4597 */         (valueAsFloat < -32768.0F) || (valueAsFloat > 32767.0F)))
/*      */       {
/* 4599 */         throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4604 */       return (short)(int)valueAsFloat;
/*      */     }
/* 4606 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4608 */     if (this.useUsageAdvisor) {
/* 4609 */       issueConversionViaParsingWarning("getShort()", columnIndex, stringVal, this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4619 */     return getShortFromString(stringVal, columnIndex + 1);
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
/*      */   protected String getNativeString(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4635 */     checkRowPos();
/* 4636 */     checkColumnBounds(columnIndex);
/*      */     
/* 4638 */     if (this.fields == null) {
/* 4639 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_133"), "S1002", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4645 */     if (this.thisRow.isNull(columnIndex - 1)) {
/* 4646 */       this.wasNullFlag = true;
/*      */       
/* 4648 */       return null;
/*      */     }
/*      */     
/* 4651 */     this.wasNullFlag = false;
/*      */     
/* 4653 */     String stringVal = null;
/*      */     
/* 4655 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/*      */ 
/* 4658 */     stringVal = getNativeConvertToString(columnIndex, field);
/* 4659 */     int mysqlType = field.getMysqlType();
/*      */     
/* 4661 */     if ((mysqlType != 7) && (mysqlType != 10) && (field.isZeroFill()) && (stringVal != null))
/*      */     {
/*      */ 
/* 4664 */       int origLength = stringVal.length();
/*      */       
/* 4666 */       StringBuffer zeroFillBuf = new StringBuffer(origLength);
/*      */       
/* 4668 */       long numZeros = field.getLength() - origLength;
/*      */       
/* 4670 */       for (long i = 0L; i < numZeros; i += 1L) {
/* 4671 */         zeroFillBuf.append('0');
/*      */       }
/*      */       
/* 4674 */       zeroFillBuf.append(stringVal);
/*      */       
/* 4676 */       stringVal = zeroFillBuf.toString();
/*      */     }
/*      */     
/* 4679 */     return stringVal;
/*      */   }
/*      */   
/*      */   private Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4685 */     checkRowPos();
/* 4686 */     checkColumnBounds(columnIndex);
/*      */     
/* 4688 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4690 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 4692 */     Time timeVal = null;
/*      */     
/* 4694 */     if (mysqlType == 11) {
/* 4695 */       timeVal = this.thisRow.getNativeTime(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
/*      */     }
/*      */     else
/*      */     {
/* 4699 */       timeVal = (Time)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 92, mysqlType, tz, rollForward, this.connection, this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4711 */     if (timeVal == null)
/*      */     {
/* 4713 */       this.wasNullFlag = true;
/*      */       
/* 4715 */       return null;
/*      */     }
/*      */     
/* 4718 */     this.wasNullFlag = false;
/*      */     
/* 4720 */     return timeVal;
/*      */   }
/*      */   
/*      */   Time getNativeTimeViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException
/*      */   {
/* 4725 */     if (this.useUsageAdvisor) {
/* 4726 */       issueConversionViaParsingWarning("getTime()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[(columnIndex - 1)], new int[] { 11 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4731 */     String strTime = getNativeString(columnIndex);
/*      */     
/* 4733 */     return getTimeFromString(strTime, targetCalendar, columnIndex, tz, rollForward);
/*      */   }
/*      */   
/*      */ 
/*      */   private Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4740 */     checkRowPos();
/* 4741 */     checkColumnBounds(columnIndex);
/*      */     
/* 4743 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4745 */     Timestamp tsVal = null;
/*      */     
/* 4747 */     int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
/*      */     
/* 4749 */     switch (mysqlType) {
/*      */     case 7: 
/*      */     case 12: 
/* 4752 */       tsVal = this.thisRow.getNativeTimestamp(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
/*      */       
/* 4754 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     default: 
/* 4759 */       tsVal = (Timestamp)this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 93, mysqlType, tz, rollForward, this.connection, this);
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4771 */     if (tsVal == null)
/*      */     {
/* 4773 */       this.wasNullFlag = true;
/*      */       
/* 4775 */       return null;
/*      */     }
/*      */     
/* 4778 */     this.wasNullFlag = false;
/*      */     
/* 4780 */     return tsVal;
/*      */   }
/*      */   
/*      */   Timestamp getNativeTimestampViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException
/*      */   {
/* 4785 */     if (this.useUsageAdvisor) {
/* 4786 */       issueConversionViaParsingWarning("getTimestamp()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[(columnIndex - 1)], new int[] { 7, 12 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4792 */     String strTimestamp = getNativeString(columnIndex);
/*      */     
/* 4794 */     return getTimestampFromString(columnIndex, targetCalendar, strTimestamp, tz, rollForward);
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
/*      */   protected InputStream getNativeUnicodeStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4821 */     checkRowPos();
/*      */     
/* 4823 */     return getBinaryStream(columnIndex);
/*      */   }
/*      */   
/*      */ 
/*      */   protected URL getNativeURL(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 4830 */     String val = getString(colIndex);
/*      */     
/* 4832 */     if (val == null) {
/* 4833 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 4837 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 4839 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____141") + val + "'", "S1009", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized ResultSetInternalMethods getNextResultSet()
/*      */   {
/* 4851 */     return this.nextResultSet;
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
/*      */   public Object getObject(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4878 */     checkRowPos();
/* 4879 */     checkColumnBounds(columnIndex);
/*      */     
/* 4881 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 4883 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 4884 */       this.wasNullFlag = true;
/*      */       
/* 4886 */       return null;
/*      */     }
/*      */     
/* 4889 */     this.wasNullFlag = false;
/*      */     
/*      */ 
/* 4892 */     Field field = this.fields[columnIndexMinusOne];
/*      */     String stringVal;
/* 4894 */     switch (field.getSQLType()) {
/*      */     case -7: 
/*      */     case 16: 
/* 4897 */       if ((field.getMysqlType() == 16) && (!field.isSingleBit()))
/*      */       {
/* 4899 */         return getBytes(columnIndex);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 4905 */       return Boolean.valueOf(getBoolean(columnIndex));
/*      */     
/*      */     case -6: 
/* 4908 */       if (!field.isUnsigned()) {
/* 4909 */         return Integer.valueOf(getByte(columnIndex));
/*      */       }
/*      */       
/* 4912 */       return Integer.valueOf(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 5: 
/* 4916 */       return Integer.valueOf(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 4: 
/* 4920 */       if ((!field.isUnsigned()) || (field.getMysqlType() == 9))
/*      */       {
/* 4922 */         return Integer.valueOf(getInt(columnIndex));
/*      */       }
/*      */       
/* 4925 */       return Long.valueOf(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case -5: 
/* 4929 */       if (!field.isUnsigned()) {
/* 4930 */         return Long.valueOf(getLong(columnIndex));
/*      */       }
/*      */       
/* 4933 */       stringVal = getString(columnIndex);
/*      */       
/* 4935 */       if (stringVal == null) {
/* 4936 */         return null;
/*      */       }
/*      */       try
/*      */       {
/* 4940 */         return new BigInteger(stringVal);
/*      */       } catch (NumberFormatException nfe) {
/* 4942 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigInteger", new Object[] { Integer.valueOf(columnIndex), stringVal }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 4950 */       stringVal = getString(columnIndex);
/*      */       
/*      */ 
/*      */ 
/* 4954 */       if (stringVal != null) {
/* 4955 */         if (stringVal.length() == 0) {
/* 4956 */           BigDecimal val = new BigDecimal(0);
/*      */           
/* 4958 */           return val;
/*      */         }
/*      */         BigDecimal val;
/*      */         try {
/* 4962 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) {
/* 4964 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4971 */         return val;
/*      */       }
/*      */       
/* 4974 */       return null;
/*      */     
/*      */     case 7: 
/* 4977 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */     case 6: 
/*      */     case 8: 
/* 4981 */       return new Double(getDouble(columnIndex));
/*      */     
/*      */     case 1: 
/*      */     case 12: 
/* 4985 */       if (!field.isOpaqueBinary()) {
/* 4986 */         return getString(columnIndex);
/*      */       }
/*      */       
/* 4989 */       return getBytes(columnIndex);
/*      */     case -1: 
/* 4991 */       if (!field.isOpaqueBinary()) {
/* 4992 */         return getStringForClob(columnIndex);
/*      */       }
/*      */       
/* 4995 */       return getBytes(columnIndex);
/*      */     
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/* 5000 */       if (field.getMysqlType() == 255)
/* 5001 */         return getBytes(columnIndex);
/* 5002 */       if ((field.isBinary()) || (field.isBlob())) {
/* 5003 */         byte[] data = getBytes(columnIndex);
/*      */         
/* 5005 */         if (this.connection.getAutoDeserialize()) {
/* 5006 */           Object obj = data;
/*      */           
/* 5008 */           if ((data != null) && (data.length >= 2)) {
/* 5009 */             if ((data[0] == -84) && (data[1] == -19)) {
/*      */               try
/*      */               {
/* 5012 */                 ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */                 
/* 5014 */                 ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */                 
/* 5016 */                 obj = objIn.readObject();
/* 5017 */                 objIn.close();
/* 5018 */                 bytesIn.close();
/*      */               } catch (ClassNotFoundException cnfe) {
/* 5020 */                 throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */               }
/*      */               catch (IOException ex)
/*      */               {
/*      */ 
/* 5027 */                 obj = data;
/*      */               }
/*      */             } else {
/* 5030 */               return getString(columnIndex);
/*      */             }
/*      */           }
/*      */           
/* 5034 */           return obj;
/*      */         }
/*      */         
/* 5037 */         return data;
/*      */       }
/*      */       
/* 5040 */       return getBytes(columnIndex);
/*      */     
/*      */     case 91: 
/* 5043 */       if ((field.getMysqlType() == 13) && (!this.connection.getYearIsDateType()))
/*      */       {
/* 5045 */         return Short.valueOf(getShort(columnIndex));
/*      */       }
/*      */       
/* 5048 */       return getDate(columnIndex);
/*      */     
/*      */     case 92: 
/* 5051 */       return getTime(columnIndex);
/*      */     
/*      */     case 93: 
/* 5054 */       return getTimestamp(columnIndex);
/*      */     }
/*      */     
/* 5057 */     return getString(columnIndex);
/*      */   }
/*      */   
/*      */   public <T> T getObject(int columnIndex, Class<T> type)
/*      */     throws SQLException
/*      */   {
/* 5063 */     if (type == null) {
/* 5064 */       throw SQLError.createSQLException("Type parameter can not be null", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 5068 */     if (type.equals(String.class))
/* 5069 */       return getString(columnIndex);
/* 5070 */     if (type.equals(BigDecimal.class))
/* 5071 */       return getBigDecimal(columnIndex);
/* 5072 */     if ((type.equals(Boolean.class)) || (type.equals(Boolean.TYPE)))
/* 5073 */       return Boolean.valueOf(getBoolean(columnIndex));
/* 5074 */     if ((type.equals(Integer.class)) || (type.equals(Integer.TYPE)))
/* 5075 */       return Integer.valueOf(getInt(columnIndex));
/* 5076 */     if ((type.equals(Long.class)) || (type.equals(Long.TYPE)))
/* 5077 */       return Long.valueOf(getLong(columnIndex));
/* 5078 */     if ((type.equals(Float.class)) || (type.equals(Float.TYPE)))
/* 5079 */       return Float.valueOf(getFloat(columnIndex));
/* 5080 */     if ((type.equals(Double.class)) || (type.equals(Double.TYPE)))
/* 5081 */       return Double.valueOf(getDouble(columnIndex));
/* 5082 */     if (type.equals(byte[].class))
/* 5083 */       return getBytes(columnIndex);
/* 5084 */     if (type.equals(Date.class))
/* 5085 */       return getDate(columnIndex);
/* 5086 */     if (type.equals(Time.class))
/* 5087 */       return getTime(columnIndex);
/* 5088 */     if (type.equals(Timestamp.class))
/* 5089 */       return getTimestamp(columnIndex);
/* 5090 */     if (type.equals(Clob.class))
/* 5091 */       return getClob(columnIndex);
/* 5092 */     if (type.equals(Blob.class))
/* 5093 */       return getBlob(columnIndex);
/* 5094 */     if (type.equals(Array.class))
/* 5095 */       return getArray(columnIndex);
/* 5096 */     if (type.equals(Ref.class))
/* 5097 */       return getRef(columnIndex);
/* 5098 */     if (type.equals(URL.class)) {
/* 5099 */       return getURL(columnIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5110 */     if (this.connection.getAutoDeserialize()) {
/*      */       try {
/* 5112 */         return (T)getObject(columnIndex);
/*      */       } catch (ClassCastException cce) {
/* 5114 */         SQLException sqlEx = SQLError.createSQLException("Conversion not supported for type " + type.getName(), "S1009", getExceptionInterceptor());
/*      */         
/* 5116 */         sqlEx.initCause(cce);
/*      */         
/* 5118 */         throw sqlEx;
/*      */       }
/*      */     }
/*      */     
/* 5122 */     throw SQLError.createSQLException("Conversion not supported for type " + type.getName(), "S1009", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T getObject(String columnLabel, Class<T> type)
/*      */     throws SQLException
/*      */   {
/* 5129 */     return (T)getObject(findColumn(columnLabel), type);
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
/*      */   public Object getObject(int i, Map<String, Class<?>> map)
/*      */     throws SQLException
/*      */   {
/* 5148 */     return getObject(i);
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
/*      */   public Object getObject(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5175 */     return getObject(findColumn(columnName));
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
/*      */   public Object getObject(String colName, Map<String, Class<?>> map)
/*      */     throws SQLException
/*      */   {
/* 5195 */     return getObject(findColumn(colName), map);
/*      */   }
/*      */   
/*      */   public Object getObjectStoredProc(int columnIndex, int desiredSqlType) throws SQLException
/*      */   {
/* 5200 */     checkRowPos();
/* 5201 */     checkColumnBounds(columnIndex);
/*      */     
/* 5203 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 5205 */     if (value == null) {
/* 5206 */       this.wasNullFlag = true;
/*      */       
/* 5208 */       return null;
/*      */     }
/*      */     
/* 5211 */     this.wasNullFlag = false;
/*      */     
/*      */ 
/* 5214 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/* 5216 */     switch (desiredSqlType)
/*      */     {
/*      */ 
/*      */ 
/*      */     case -7: 
/*      */     case 16: 
/* 5222 */       return Boolean.valueOf(getBoolean(columnIndex));
/*      */     
/*      */     case -6: 
/* 5225 */       return Integer.valueOf(getInt(columnIndex));
/*      */     
/*      */     case 5: 
/* 5228 */       return Integer.valueOf(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 4: 
/* 5232 */       if ((!field.isUnsigned()) || (field.getMysqlType() == 9))
/*      */       {
/* 5234 */         return Integer.valueOf(getInt(columnIndex));
/*      */       }
/*      */       
/* 5237 */       return Long.valueOf(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case -5: 
/* 5241 */       if (field.isUnsigned()) {
/* 5242 */         return getBigDecimal(columnIndex);
/*      */       }
/*      */       
/* 5245 */       return Long.valueOf(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 5250 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 5253 */       if (stringVal != null) {
/* 5254 */         if (stringVal.length() == 0) {
/* 5255 */           BigDecimal val = new BigDecimal(0);
/*      */           
/* 5257 */           return val;
/*      */         }
/*      */         BigDecimal val;
/*      */         try {
/* 5261 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) {
/* 5263 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, Integer.valueOf(columnIndex) }), "S1009", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5270 */         return val;
/*      */       }
/*      */       
/* 5273 */       return null;
/*      */     
/*      */     case 7: 
/* 5276 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */ 
/*      */     case 6: 
/* 5280 */       if (!this.connection.getRunningCTS13()) {
/* 5281 */         return new Double(getFloat(columnIndex));
/*      */       }
/* 5283 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 8: 
/* 5290 */       return new Double(getDouble(columnIndex));
/*      */     
/*      */     case 1: 
/*      */     case 12: 
/* 5294 */       return getString(columnIndex);
/*      */     case -1: 
/* 5296 */       return getStringForClob(columnIndex);
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/* 5300 */       return getBytes(columnIndex);
/*      */     
/*      */     case 91: 
/* 5303 */       if ((field.getMysqlType() == 13) && (!this.connection.getYearIsDateType()))
/*      */       {
/* 5305 */         return Short.valueOf(getShort(columnIndex));
/*      */       }
/*      */       
/* 5308 */       return getDate(columnIndex);
/*      */     
/*      */     case 92: 
/* 5311 */       return getTime(columnIndex);
/*      */     
/*      */     case 93: 
/* 5314 */       return getTimestamp(columnIndex);
/*      */     }
/*      */     
/* 5317 */     return getString(columnIndex);
/*      */   }
/*      */   
/*      */   public Object getObjectStoredProc(int i, Map<Object, Object> map, int desiredSqlType)
/*      */     throws SQLException
/*      */   {
/* 5323 */     return getObjectStoredProc(i, desiredSqlType);
/*      */   }
/*      */   
/*      */   public Object getObjectStoredProc(String columnName, int desiredSqlType) throws SQLException
/*      */   {
/* 5328 */     return getObjectStoredProc(findColumn(columnName), desiredSqlType);
/*      */   }
/*      */   
/*      */   public Object getObjectStoredProc(String colName, Map<Object, Object> map, int desiredSqlType) throws SQLException
/*      */   {
/* 5333 */     return getObjectStoredProc(findColumn(colName), map, desiredSqlType);
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
/*      */   public Ref getRef(int i)
/*      */     throws SQLException
/*      */   {
/* 5350 */     checkColumnBounds(i);
/* 5351 */     throw SQLError.notImplemented();
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
/*      */   public Ref getRef(String colName)
/*      */     throws SQLException
/*      */   {
/* 5368 */     return getRef(findColumn(colName));
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
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 5385 */     checkClosed();
/*      */     
/* 5387 */     int currentRowNumber = this.rowData.getCurrentRowNumber();
/* 5388 */     int row = 0;
/*      */     
/*      */ 
/*      */ 
/* 5392 */     if (!this.rowData.isDynamic()) {
/* 5393 */       if ((currentRowNumber < 0) || (this.rowData.isAfterLast()) || (this.rowData.isEmpty()))
/*      */       {
/* 5395 */         row = 0;
/*      */       } else {
/* 5397 */         row = currentRowNumber + 1;
/*      */       }
/*      */     }
/*      */     else {
/* 5401 */       row = currentRowNumber + 1;
/*      */     }
/*      */     
/* 5404 */     return row;
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
/*      */   private long getNumericRepresentationOfSQLBitType(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5424 */     Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */     
/* 5426 */     if ((this.fields[(columnIndex - 1)].isSingleBit()) || (((byte[])value).length == 1))
/*      */     {
/* 5428 */       return ((byte[])(byte[])value)[0];
/*      */     }
/*      */     
/*      */ 
/* 5432 */     byte[] asBytes = (byte[])value;
/*      */     
/*      */ 
/* 5435 */     int shift = 0;
/*      */     
/* 5437 */     long[] steps = new long[asBytes.length];
/*      */     
/* 5439 */     for (int i = asBytes.length - 1; i >= 0; i--) {
/* 5440 */       steps[i] = ((asBytes[i] & 0xFF) << shift);
/* 5441 */       shift += 8;
/*      */     }
/*      */     
/* 5444 */     long valueAsLong = 0L;
/*      */     
/* 5446 */     for (int i = 0; i < asBytes.length; i++) {
/* 5447 */       valueAsLong |= steps[i];
/*      */     }
/*      */     
/* 5450 */     return valueAsLong;
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
/*      */   public short getShort(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5465 */     if (!this.isBinaryEncoded) {
/* 5466 */       checkRowPos();
/*      */       
/* 5468 */       if (this.useFastIntParsing)
/*      */       {
/* 5470 */         checkColumnBounds(columnIndex);
/*      */         
/* 5472 */         Object value = this.thisRow.getColumnValue(columnIndex - 1);
/*      */         
/* 5474 */         if (value == null) {
/* 5475 */           this.wasNullFlag = true;
/*      */         } else {
/* 5477 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 5480 */         if (this.wasNullFlag) {
/* 5481 */           return 0;
/*      */         }
/*      */         
/* 5484 */         byte[] shortAsBytes = (byte[])value;
/*      */         
/* 5486 */         if (shortAsBytes.length == 0) {
/* 5487 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5490 */         boolean needsFullParse = false;
/*      */         
/* 5492 */         for (int i = 0; i < shortAsBytes.length; i++) {
/* 5493 */           if (((char)shortAsBytes[i] == 'e') || ((char)shortAsBytes[i] == 'E'))
/*      */           {
/* 5495 */             needsFullParse = true;
/*      */             
/* 5497 */             break;
/*      */           }
/*      */         }
/*      */         
/* 5501 */         if (!needsFullParse) {
/*      */           try {
/* 5503 */             return parseShortWithOverflowCheck(columnIndex, shortAsBytes, null);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */             try {
/* 5508 */               return parseShortAsDouble(columnIndex, StringUtils.toString(shortAsBytes));
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/* 5514 */               if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 5515 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 5517 */                 if ((this.jdbcCompliantTruncationForReads) && ((valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */                 {
/*      */ 
/* 5520 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */                 }
/*      */                 
/*      */ 
/* 5524 */                 return (short)(int)valueAsLong;
/*      */               }
/*      */               
/* 5527 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + StringUtils.toString(shortAsBytes) + "'", "S1009", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5537 */       String val = null;
/*      */       try
/*      */       {
/* 5540 */         val = getString(columnIndex);
/*      */         
/* 5542 */         if (val != null)
/*      */         {
/* 5544 */           if (val.length() == 0) {
/* 5545 */             return (short)convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 5548 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */           {
/* 5550 */             return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 5555 */           return parseShortAsDouble(columnIndex, val);
/*      */         }
/*      */         
/* 5558 */         return 0;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 5561 */           return parseShortAsDouble(columnIndex, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 5566 */           if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 5567 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 5569 */             if ((this.jdbcCompliantTruncationForReads) && ((valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */             {
/*      */ 
/* 5572 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */             }
/*      */             
/*      */ 
/* 5576 */             return (short)(int)valueAsLong;
/*      */           }
/*      */           
/* 5579 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + val + "'", "S1009", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5587 */     return getNativeShort(columnIndex);
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
/*      */   public short getShort(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5602 */     return getShort(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final short getShortFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 5608 */       if (val != null)
/*      */       {
/* 5610 */         if (val.length() == 0) {
/* 5611 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5614 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */         {
/* 5616 */           return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */         }
/*      */         
/*      */ 
/* 5620 */         return parseShortAsDouble(columnIndex, val);
/*      */       }
/*      */       
/* 5623 */       return 0;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 5626 */         return parseShortAsDouble(columnIndex, val);
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 5631 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____217") + val + Messages.getString("ResultSet.___in_column__218") + columnIndex, "S1009", getExceptionInterceptor());
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
/*      */   public Statement getStatement()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 5651 */       synchronized (checkClosed().getConnectionMutex()) {
/* 5652 */         if (this.wrapperStatement != null) {
/* 5653 */           return this.wrapperStatement;
/*      */         }
/*      */         
/* 5656 */         return this.owningStatement;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5672 */       return this.owningStatement;
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 5660 */       if (!this.retainOwningStatement) {
/* 5661 */         throw SQLError.createSQLException("Operation not allowed on closed ResultSet. Statements can be retained over result set closure by setting the connection property \"retainStatementAfterResultSetClose\" to \"true\".", "S1000", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5668 */       if (this.wrapperStatement != null) {
/* 5669 */         return this.wrapperStatement;
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
/*      */   public String getString(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5689 */     String stringVal = getStringInternal(columnIndex, true);
/*      */     
/* 5691 */     if ((this.padCharsWithSpace) && (stringVal != null)) {
/* 5692 */       Field f = this.fields[(columnIndex - 1)];
/*      */       
/* 5694 */       if (f.getMysqlType() == 254) {
/* 5695 */         int fieldLength = (int)f.getLength() / f.getMaxBytesPerCharacter();
/*      */         
/*      */ 
/* 5698 */         int currentLength = stringVal.length();
/*      */         
/* 5700 */         if (currentLength < fieldLength) {
/* 5701 */           StringBuffer paddedBuf = new StringBuffer(fieldLength);
/* 5702 */           paddedBuf.append(stringVal);
/*      */           
/* 5704 */           int difference = fieldLength - currentLength;
/*      */           
/* 5706 */           paddedBuf.append(EMPTY_SPACE, 0, difference);
/*      */           
/* 5708 */           stringVal = paddedBuf.toString();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5713 */     return stringVal;
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
/*      */   public String getString(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5729 */     return getString(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private String getStringForClob(int columnIndex) throws SQLException {
/* 5733 */     String asString = null;
/*      */     
/* 5735 */     String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */     
/*      */ 
/* 5738 */     if (forcedEncoding == null) {
/* 5739 */       if (!this.isBinaryEncoded) {
/* 5740 */         asString = getString(columnIndex);
/*      */       } else {
/* 5742 */         asString = getNativeString(columnIndex);
/*      */       }
/*      */     } else {
/*      */       try {
/* 5746 */         byte[] asBytes = null;
/*      */         
/* 5748 */         if (!this.isBinaryEncoded) {
/* 5749 */           asBytes = getBytes(columnIndex);
/*      */         } else {
/* 5751 */           asBytes = getNativeBytes(columnIndex, true);
/*      */         }
/*      */         
/* 5754 */         if (asBytes != null) {
/* 5755 */           asString = StringUtils.toString(asBytes, forcedEncoding);
/*      */         }
/*      */       } catch (UnsupportedEncodingException uee) {
/* 5758 */         throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5763 */     return asString;
/*      */   }
/*      */   
/*      */   protected String getStringInternal(int columnIndex, boolean checkDateTypes) throws SQLException
/*      */   {
/* 5768 */     if (!this.isBinaryEncoded) {
/* 5769 */       checkRowPos();
/* 5770 */       checkColumnBounds(columnIndex);
/*      */       
/* 5772 */       if (this.fields == null) {
/* 5773 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_99"), "S1002", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5781 */       int internalColumnIndex = columnIndex - 1;
/*      */       
/* 5783 */       if (this.thisRow.isNull(internalColumnIndex)) {
/* 5784 */         this.wasNullFlag = true;
/*      */         
/* 5786 */         return null;
/*      */       }
/*      */       
/* 5789 */       this.wasNullFlag = false;
/*      */       
/*      */ 
/* 5792 */       Field metadata = this.fields[internalColumnIndex];
/*      */       
/* 5794 */       String stringVal = null;
/*      */       
/* 5796 */       if (metadata.getMysqlType() == 16) {
/* 5797 */         if (metadata.isSingleBit()) {
/* 5798 */           byte[] value = this.thisRow.getColumnValue(internalColumnIndex);
/*      */           
/* 5800 */           if (value.length == 0) {
/* 5801 */             return String.valueOf(convertToZeroWithEmptyCheck());
/*      */           }
/*      */           
/* 5804 */           return String.valueOf(value[0]);
/*      */         }
/*      */         
/* 5807 */         return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
/*      */       }
/*      */       
/* 5810 */       String encoding = metadata.getCharacterSet();
/*      */       
/* 5812 */       stringVal = this.thisRow.getString(internalColumnIndex, encoding, this.connection);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5819 */       if (metadata.getMysqlType() == 13) {
/* 5820 */         if (!this.connection.getYearIsDateType()) {
/* 5821 */           return stringVal;
/*      */         }
/*      */         
/* 5824 */         Date dt = getDateFromString(stringVal, columnIndex, null);
/*      */         
/* 5826 */         if (dt == null) {
/* 5827 */           this.wasNullFlag = true;
/*      */           
/* 5829 */           return null;
/*      */         }
/*      */         
/* 5832 */         this.wasNullFlag = false;
/*      */         
/* 5834 */         return dt.toString();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5839 */       if ((checkDateTypes) && (!this.connection.getNoDatetimeStringSync())) {
/* 5840 */         switch (metadata.getSQLType()) {
/*      */         case 92: 
/* 5842 */           Time tm = getTimeFromString(stringVal, null, columnIndex, getDefaultTimeZone(), false);
/*      */           
/*      */ 
/* 5845 */           if (tm == null) {
/* 5846 */             this.wasNullFlag = true;
/*      */             
/* 5848 */             return null;
/*      */           }
/*      */           
/* 5851 */           this.wasNullFlag = false;
/*      */           
/* 5853 */           return tm.toString();
/*      */         
/*      */         case 91: 
/* 5856 */           Date dt = getDateFromString(stringVal, columnIndex, null);
/*      */           
/* 5858 */           if (dt == null) {
/* 5859 */             this.wasNullFlag = true;
/*      */             
/* 5861 */             return null;
/*      */           }
/*      */           
/* 5864 */           this.wasNullFlag = false;
/*      */           
/* 5866 */           return dt.toString();
/*      */         case 93: 
/* 5868 */           Timestamp ts = getTimestampFromString(columnIndex, null, stringVal, getDefaultTimeZone(), false);
/*      */           
/*      */ 
/* 5871 */           if (ts == null) {
/* 5872 */             this.wasNullFlag = true;
/*      */             
/* 5874 */             return null;
/*      */           }
/*      */           
/* 5877 */           this.wasNullFlag = false;
/*      */           
/* 5879 */           return ts.toString();
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/* 5885 */       return stringVal;
/*      */     }
/*      */     
/* 5888 */     return getNativeString(columnIndex);
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
/*      */   public Time getTime(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5903 */     return getTimeInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Time getTime(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 5923 */     return getTimeInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Time getTime(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5938 */     return getTime(findColumn(columnName));
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
/*      */   public Time getTime(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 5958 */     return getTime(findColumn(columnName), cal);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Time getTimeInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 6146 */     checkRowPos();
/*      */     
/* 6148 */     if (this.isBinaryEncoded) {
/* 6149 */       return getNativeTime(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 6152 */     if (!this.useFastDateParsing) {
/* 6153 */       String timeAsString = getStringInternal(columnIndex, false);
/*      */       
/* 6155 */       return getTimeFromString(timeAsString, targetCalendar, columnIndex, tz, rollForward);
/*      */     }
/*      */     
/*      */ 
/* 6159 */     checkColumnBounds(columnIndex);
/*      */     
/* 6161 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 6163 */     if (this.thisRow.isNull(columnIndexMinusOne)) {
/* 6164 */       this.wasNullFlag = true;
/*      */       
/* 6166 */       return null;
/*      */     }
/*      */     
/* 6169 */     this.wasNullFlag = false;
/*      */     
/* 6171 */     return this.thisRow.getTimeFast(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
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
/*      */   public Timestamp getTimestamp(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 6188 */     return getTimestampInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Timestamp getTimestamp(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 6210 */     return getTimestampInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Timestamp getTimestamp(String columnName)
/*      */     throws SQLException
/*      */   {
/* 6226 */     return getTimestamp(findColumn(columnName));
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
/*      */   public Timestamp getTimestamp(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 6247 */     return getTimestamp(findColumn(columnName), cal);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Timestamp getTimestampInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 6571 */     if (this.isBinaryEncoded) {
/* 6572 */       return getNativeTimestamp(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 6575 */     Timestamp tsVal = null;
/*      */     
/* 6577 */     if (!this.useFastDateParsing) {
/* 6578 */       String timestampValue = getStringInternal(columnIndex, false);
/*      */       
/* 6580 */       tsVal = getTimestampFromString(columnIndex, targetCalendar, timestampValue, tz, rollForward);
/*      */     }
/*      */     else
/*      */     {
/* 6584 */       checkClosed();
/* 6585 */       checkRowPos();
/* 6586 */       checkColumnBounds(columnIndex);
/*      */       
/* 6588 */       tsVal = this.thisRow.getTimestampFast(columnIndex - 1, targetCalendar, tz, rollForward, this.connection, this);
/*      */     }
/*      */     
/*      */ 
/* 6592 */     if (tsVal == null) {
/* 6593 */       this.wasNullFlag = true;
/*      */     } else {
/* 6595 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 6598 */     return tsVal;
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
/*      */   public int getType()
/*      */     throws SQLException
/*      */   {
/* 6612 */     return this.resultSetType;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public InputStream getUnicodeStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 6634 */     if (!this.isBinaryEncoded) {
/* 6635 */       checkRowPos();
/*      */       
/* 6637 */       return getBinaryStream(columnIndex);
/*      */     }
/*      */     
/* 6640 */     return getNativeBinaryStream(columnIndex);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public InputStream getUnicodeStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 6657 */     return getUnicodeStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public long getUpdateCount() {
/* 6661 */     return this.updateCount;
/*      */   }
/*      */   
/*      */   public long getUpdateID() {
/* 6665 */     return this.updateId;
/*      */   }
/*      */   
/*      */ 
/*      */   public URL getURL(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 6672 */     String val = getString(colIndex);
/*      */     
/* 6674 */     if (val == null) {
/* 6675 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 6679 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 6681 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____104") + val + "'", "S1009", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public URL getURL(String colName)
/*      */     throws SQLException
/*      */   {
/* 6691 */     String val = getString(colName);
/*      */     
/* 6693 */     if (val == null) {
/* 6694 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 6698 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 6700 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____107") + val + "'", "S1009", getExceptionInterceptor());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 6744 */     throw new NotUpdatable();
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
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 6761 */     synchronized (checkClosed().getConnectionMutex()) {
/* 6762 */       boolean b = this.rowData.isAfterLast();
/*      */       
/* 6764 */       return b;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void issueConversionViaParsingWarning(String methodName, int columnIndex, Object value, Field fieldInfo, int[] typesWithNoParseConversion)
/*      */     throws SQLException
/*      */   {
/* 6834 */     synchronized (checkClosed().getConnectionMutex()) {
/* 6835 */       StringBuffer originalQueryBuf = new StringBuffer();
/*      */       
/* 6837 */       if ((this.owningStatement != null) && ((this.owningStatement instanceof PreparedStatement)))
/*      */       {
/* 6839 */         originalQueryBuf.append(Messages.getString("ResultSet.CostlyConversionCreatedFromQuery"));
/* 6840 */         originalQueryBuf.append(((PreparedStatement)this.owningStatement).originalSql);
/*      */         
/* 6842 */         originalQueryBuf.append("\n\n");
/*      */       } else {
/* 6844 */         originalQueryBuf.append(".");
/*      */       }
/*      */       
/* 6847 */       StringBuffer convertibleTypesBuf = new StringBuffer();
/*      */       
/* 6849 */       for (int i = 0; i < typesWithNoParseConversion.length; i++) {
/* 6850 */         convertibleTypesBuf.append(MysqlDefs.typeToName(typesWithNoParseConversion[i]));
/* 6851 */         convertibleTypesBuf.append("\n");
/*      */       }
/*      */       
/* 6854 */       String message = Messages.getString("ResultSet.CostlyConversion", new Object[] { methodName, Integer.valueOf(columnIndex + 1), fieldInfo.getOriginalName(), fieldInfo.getOriginalTableName(), originalQueryBuf.toString(), value != null ? value.getClass().getName() : ResultSetMetaData.getClassNameForJavaType(fieldInfo.getSQLType(), fieldInfo.isUnsigned(), fieldInfo.getMysqlType(), (fieldInfo.isBinary()) || (fieldInfo.isBlob()) ? 1 : false, fieldInfo.isOpaqueBinary(), this.connection.getYearIsDateType()), MysqlDefs.typeToName(fieldInfo.getMysqlType()), convertibleTypesBuf.toString() });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6870 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
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
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 6894 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 6896 */       boolean b = true;
/*      */       
/* 6898 */       if (this.rowData.size() == 0) {
/* 6899 */         b = false;
/*      */       }
/*      */       else {
/* 6902 */         if (this.onInsertRow) {
/* 6903 */           this.onInsertRow = false;
/*      */         }
/*      */         
/* 6906 */         if (this.doingUpdates) {
/* 6907 */           this.doingUpdates = false;
/*      */         }
/*      */         
/* 6910 */         if (this.thisRow != null) {
/* 6911 */           this.thisRow.closeOpenStreams();
/*      */         }
/*      */         
/* 6914 */         this.rowData.beforeLast();
/* 6915 */         this.thisRow = this.rowData.next();
/*      */       }
/*      */       
/* 6918 */       setRowPositionValidity();
/*      */       
/* 6920 */       return b;
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
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 6943 */     throw new NotUpdatable();
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
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 6964 */     throw new NotUpdatable();
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
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/* 6983 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 6985 */       if (this.onInsertRow) {
/* 6986 */         this.onInsertRow = false;
/*      */       }
/*      */       
/* 6989 */       if (this.doingUpdates) {
/* 6990 */         this.doingUpdates = false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 6995 */       if (!reallyResult()) {
/* 6996 */         throw SQLError.createSQLException(Messages.getString("ResultSet.ResultSet_is_from_UPDATE._No_Data_115"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 7002 */       if (this.thisRow != null)
/* 7003 */         this.thisRow.closeOpenStreams();
/*      */       boolean b;
/*      */       boolean b;
/* 7006 */       if (this.rowData.size() == 0) {
/* 7007 */         b = false;
/*      */       } else {
/* 7009 */         this.thisRow = this.rowData.next();
/*      */         boolean b;
/* 7011 */         if (this.thisRow == null) {
/* 7012 */           b = false;
/*      */         } else {
/* 7014 */           clearWarnings();
/*      */           
/* 7016 */           b = true;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 7021 */       setRowPositionValidity();
/*      */       
/* 7023 */       return b;
/*      */     }
/*      */   }
/*      */   
/*      */   private int parseIntAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException
/*      */   {
/* 7029 */     if (val == null) {
/* 7030 */       return 0;
/*      */     }
/*      */     
/* 7033 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7035 */     if ((this.jdbcCompliantTruncationForReads) && (
/* 7036 */       (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */     {
/* 7038 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7043 */     return (int)valueAsDouble;
/*      */   }
/*      */   
/*      */   private int getIntWithOverflowCheck(int columnIndex) throws SQLException {
/* 7047 */     int intValue = this.thisRow.getInt(columnIndex);
/*      */     
/* 7049 */     checkForIntegerTruncation(columnIndex, null, intValue);
/*      */     
/*      */ 
/* 7052 */     return intValue;
/*      */   }
/*      */   
/*      */   private void checkForIntegerTruncation(int columnIndex, byte[] valueAsBytes, int intValue)
/*      */     throws SQLException
/*      */   {
/* 7058 */     if ((this.jdbcCompliantTruncationForReads) && (
/* 7059 */       (intValue == Integer.MIN_VALUE) || (intValue == Integer.MAX_VALUE))) {
/* 7060 */       String valueAsString = null;
/*      */       
/* 7062 */       if (valueAsBytes == null) {
/* 7063 */         valueAsString = this.thisRow.getString(columnIndex, this.fields[columnIndex].getCharacterSet(), this.connection);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 7068 */       long valueAsLong = Long.parseLong(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7072 */       if ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L))
/*      */       {
/* 7074 */         throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndex + 1, 4);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private long parseLongAsDouble(int columnIndexZeroBased, String val)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7084 */     if (val == null) {
/* 7085 */       return 0L;
/*      */     }
/*      */     
/* 7088 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7090 */     if ((this.jdbcCompliantTruncationForReads) && (
/* 7091 */       (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */     {
/* 7093 */       throwRangeException(val, columnIndexZeroBased + 1, -5);
/*      */     }
/*      */     
/*      */ 
/* 7097 */     return valueAsDouble;
/*      */   }
/*      */   
/*      */   private long getLongWithOverflowCheck(int columnIndexZeroBased, boolean doOverflowCheck) throws SQLException {
/* 7101 */     long longValue = this.thisRow.getLong(columnIndexZeroBased);
/*      */     
/* 7103 */     if (doOverflowCheck) {
/* 7104 */       checkForLongTruncation(columnIndexZeroBased, null, longValue);
/*      */     }
/*      */     
/* 7107 */     return longValue;
/*      */   }
/*      */   
/*      */ 
/*      */   private long parseLongWithOverflowCheck(int columnIndexZeroBased, byte[] valueAsBytes, String valueAsString, boolean doCheck)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7114 */     long longValue = 0L;
/*      */     
/* 7116 */     if ((valueAsBytes == null) && (valueAsString == null)) {
/* 7117 */       return 0L;
/*      */     }
/*      */     
/* 7120 */     if (valueAsBytes != null) {
/* 7121 */       longValue = StringUtils.getLong(valueAsBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 7131 */       valueAsString = valueAsString.trim();
/*      */       
/* 7133 */       longValue = Long.parseLong(valueAsString);
/*      */     }
/*      */     
/* 7136 */     if ((doCheck) && (this.jdbcCompliantTruncationForReads)) {
/* 7137 */       checkForLongTruncation(columnIndexZeroBased, valueAsBytes, longValue);
/*      */     }
/*      */     
/* 7140 */     return longValue;
/*      */   }
/*      */   
/*      */   private void checkForLongTruncation(int columnIndexZeroBased, byte[] valueAsBytes, long longValue) throws SQLException {
/* 7144 */     if ((longValue == Long.MIN_VALUE) || (longValue == Long.MAX_VALUE))
/*      */     {
/* 7146 */       String valueAsString = null;
/*      */       
/* 7148 */       if (valueAsBytes == null) {
/* 7149 */         valueAsString = this.thisRow.getString(columnIndexZeroBased, this.fields[columnIndexZeroBased].getCharacterSet(), this.connection);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 7154 */       double valueAsDouble = Double.parseDouble(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7158 */       if ((valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D))
/*      */       {
/* 7160 */         throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndexZeroBased + 1, -5);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private short parseShortAsDouble(int columnIndex, String val)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7169 */     if (val == null) {
/* 7170 */       return 0;
/*      */     }
/*      */     
/* 7173 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7175 */     if ((this.jdbcCompliantTruncationForReads) && (
/* 7176 */       (valueAsDouble < -32768.0D) || (valueAsDouble > 32767.0D)))
/*      */     {
/* 7178 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 5);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7183 */     return (short)(int)valueAsDouble;
/*      */   }
/*      */   
/*      */ 
/*      */   private short parseShortWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7190 */     short shortValue = 0;
/*      */     
/* 7192 */     if ((valueAsBytes == null) && (valueAsString == null)) {
/* 7193 */       return 0;
/*      */     }
/*      */     
/* 7196 */     if (valueAsBytes != null) {
/* 7197 */       shortValue = StringUtils.getShort(valueAsBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 7207 */       valueAsString = valueAsString.trim();
/*      */       
/* 7209 */       shortValue = Short.parseShort(valueAsString);
/*      */     }
/*      */     
/* 7212 */     if ((this.jdbcCompliantTruncationForReads) && (
/* 7213 */       (shortValue == Short.MIN_VALUE) || (shortValue == Short.MAX_VALUE))) {
/* 7214 */       long valueAsLong = Long.parseLong(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7218 */       if ((valueAsLong < -32768L) || (valueAsLong > 32767L))
/*      */       {
/* 7220 */         throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndex, 5);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7227 */     return shortValue;
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
/*      */   public boolean prev()
/*      */     throws SQLException
/*      */   {
/* 7251 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 7253 */       int rowIndex = this.rowData.getCurrentRowNumber();
/*      */       
/* 7255 */       if (this.thisRow != null) {
/* 7256 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/* 7259 */       boolean b = true;
/*      */       
/* 7261 */       if (rowIndex - 1 >= 0) {
/* 7262 */         rowIndex--;
/* 7263 */         this.rowData.setCurrentRow(rowIndex);
/* 7264 */         this.thisRow = this.rowData.getAt(rowIndex);
/*      */         
/* 7266 */         b = true;
/* 7267 */       } else if (rowIndex - 1 == -1) {
/* 7268 */         rowIndex--;
/* 7269 */         this.rowData.setCurrentRow(rowIndex);
/* 7270 */         this.thisRow = null;
/*      */         
/* 7272 */         b = false;
/*      */       } else {
/* 7274 */         b = false;
/*      */       }
/*      */       
/* 7277 */       setRowPositionValidity();
/*      */       
/* 7279 */       return b;
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
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 7302 */     synchronized (checkClosed().getConnectionMutex()) {
/* 7303 */       if (this.onInsertRow) {
/* 7304 */         this.onInsertRow = false;
/*      */       }
/*      */       
/* 7307 */       if (this.doingUpdates) {
/* 7308 */         this.doingUpdates = false;
/*      */       }
/*      */       
/* 7311 */       return prev();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void realClose(boolean calledExplicitly)
/*      */     throws SQLException
/*      */   {
/*      */     MySQLConnection locallyScopedConn;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 7329 */       locallyScopedConn = checkClosed();
/*      */     } catch (SQLException sqlEx) {
/* 7331 */       return;
/*      */     }
/*      */     
/* 7334 */     synchronized (locallyScopedConn.getConnectionMutex())
/*      */     {
/*      */       try {
/* 7337 */         if (this.useUsageAdvisor)
/*      */         {
/*      */ 
/*      */ 
/* 7341 */           if (!calledExplicitly) {
/* 7342 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.ResultSet_implicitly_closed_by_driver")));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7361 */           if ((this.rowData instanceof RowDataStatic))
/*      */           {
/*      */ 
/*      */ 
/* 7365 */             if (this.rowData.size() > this.connection.getResultSetSizeThreshold())
/*      */             {
/* 7367 */               this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Too_Large_Result_Set", new Object[] { Integer.valueOf(this.rowData.size()), Integer.valueOf(this.connection.getResultSetSizeThreshold()) })));
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7395 */             if ((!isLast()) && (!isAfterLast()) && (this.rowData.size() != 0))
/*      */             {
/* 7397 */               this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Possible_incomplete_traversal_of_result_set", new Object[] { Integer.valueOf(getRow()), Integer.valueOf(this.rowData.size()) })));
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7430 */           if ((this.columnUsed.length > 0) && (!this.rowData.wasEmpty())) {
/* 7431 */             StringBuffer buf = new StringBuffer(Messages.getString("ResultSet.The_following_columns_were_never_referenced"));
/*      */             
/*      */ 
/*      */ 
/* 7435 */             boolean issueWarn = false;
/*      */             
/* 7437 */             for (int i = 0; i < this.columnUsed.length; i++) {
/* 7438 */               if (this.columnUsed[i] == 0) {
/* 7439 */                 if (!issueWarn) {
/* 7440 */                   issueWarn = true;
/*      */                 } else {
/* 7442 */                   buf.append(", ");
/*      */                 }
/*      */                 
/* 7445 */                 buf.append(this.fields[i].getFullName());
/*      */               }
/*      */             }
/*      */             
/* 7449 */             if (issueWarn) {
/* 7450 */               this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), 0, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, buf.toString()));
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/* 7464 */         if ((this.owningStatement != null) && (calledExplicitly)) {
/* 7465 */           this.owningStatement.removeOpenResultSet(this);
/*      */         }
/*      */         
/* 7468 */         SQLException exceptionDuringClose = null;
/*      */         
/* 7470 */         if (this.rowData != null) {
/*      */           try {
/* 7472 */             this.rowData.close();
/*      */           } catch (SQLException sqlEx) {
/* 7474 */             exceptionDuringClose = sqlEx;
/*      */           }
/*      */         }
/*      */         
/* 7478 */         if (this.statementUsedForFetchingRows != null) {
/*      */           try {
/* 7480 */             this.statementUsedForFetchingRows.realClose(true, false);
/*      */           } catch (SQLException sqlEx) {
/* 7482 */             if (exceptionDuringClose != null) {
/* 7483 */               exceptionDuringClose.setNextException(sqlEx);
/*      */             } else {
/* 7485 */               exceptionDuringClose = sqlEx;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 7490 */         this.rowData = null;
/* 7491 */         this.defaultTimeZone = null;
/* 7492 */         this.fields = null;
/* 7493 */         this.columnLabelToIndex = null;
/* 7494 */         this.fullColumnNameToIndex = null;
/* 7495 */         this.columnToIndexCache = null;
/* 7496 */         this.eventSink = null;
/* 7497 */         this.warningChain = null;
/*      */         
/* 7499 */         if (!this.retainOwningStatement) {
/* 7500 */           this.owningStatement = null;
/*      */         }
/*      */         
/* 7503 */         this.catalog = null;
/* 7504 */         this.serverInfo = null;
/* 7505 */         this.thisRow = null;
/* 7506 */         this.fastDateCal = null;
/* 7507 */         this.connection = null;
/*      */         
/* 7509 */         this.isClosed = true;
/*      */         
/* 7511 */         if (exceptionDuringClose != null) {
/* 7512 */           throw exceptionDuringClose;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 7522 */     return this.isClosed;
/*      */   }
/*      */   
/*      */   public boolean reallyResult() {
/* 7526 */     if (this.rowData != null) {
/* 7527 */       return true;
/*      */     }
/*      */     
/* 7530 */     return this.reallyResult;
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
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 7554 */     throw new NotUpdatable();
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
/*      */   public boolean relative(int rows)
/*      */     throws SQLException
/*      */   {
/* 7584 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 7586 */       if (this.rowData.size() == 0) {
/* 7587 */         setRowPositionValidity();
/*      */         
/* 7589 */         return false;
/*      */       }
/*      */       
/* 7592 */       if (this.thisRow != null) {
/* 7593 */         this.thisRow.closeOpenStreams();
/*      */       }
/*      */       
/* 7596 */       this.rowData.moveRowRelative(rows);
/* 7597 */       this.thisRow = this.rowData.getAt(this.rowData.getCurrentRowNumber());
/*      */       
/* 7599 */       setRowPositionValidity();
/*      */       
/* 7601 */       return (!this.rowData.isAfterLast()) && (!this.rowData.isBeforeFirst());
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
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 7621 */     throw SQLError.notImplemented();
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
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 7639 */     throw SQLError.notImplemented();
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
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 7657 */     throw SQLError.notImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setBinaryEncoded()
/*      */   {
/* 7665 */     this.isBinaryEncoded = true;
/*      */   }
/*      */   
/*      */   private void setDefaultTimeZone(TimeZone defaultTimeZone) throws SQLException {
/* 7669 */     synchronized (checkClosed().getConnectionMutex()) {
/* 7670 */       this.defaultTimeZone = defaultTimeZone;
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
/*      */   public void setFetchDirection(int direction)
/*      */     throws SQLException
/*      */   {
/* 7690 */     synchronized (checkClosed().getConnectionMutex()) {
/* 7691 */       if ((direction != 1000) && (direction != 1001) && (direction != 1002))
/*      */       {
/* 7693 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Illegal_value_for_fetch_direction_64"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 7699 */       this.fetchDirection = direction;
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
/*      */   public void setFetchSize(int rows)
/*      */     throws SQLException
/*      */   {
/* 7720 */     synchronized (checkClosed().getConnectionMutex()) {
/* 7721 */       if (rows < 0) {
/* 7722 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Value_must_be_between_0_and_getMaxRows()_66"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 7728 */       this.fetchSize = rows;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFirstCharOfQuery(char c)
/*      */   {
/*      */     try
/*      */     {
/* 7741 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7742 */         this.firstCharOfQuery = c;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7745 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void setNextResultSet(ResultSetInternalMethods nextResultSet)
/*      */   {
/* 7757 */     this.nextResultSet = nextResultSet;
/*      */   }
/*      */   
/*      */   public void setOwningStatement(StatementImpl owningStatement) {
/*      */     try {
/* 7762 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7763 */         this.owningStatement = owningStatement;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7766 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/*      */     try
/*      */     {
/* 7778 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7779 */         this.resultSetConcurrency = concurrencyFlag;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7782 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void setResultSetType(int typeFlag)
/*      */   {
/*      */     try
/*      */     {
/* 7795 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7796 */         this.resultSetType = typeFlag;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7799 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void setServerInfo(String info)
/*      */   {
/*      */     try
/*      */     {
/* 7811 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7812 */         this.serverInfo = info;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7815 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void setStatementUsedForFetchingRows(PreparedStatement stmt) {
/*      */     try {
/* 7821 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7822 */         this.statementUsedForFetchingRows = stmt;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7825 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void setWrapperStatement(Statement wrapperStatement)
/*      */   {
/*      */     try
/*      */     {
/* 7835 */       synchronized (checkClosed().getConnectionMutex()) {
/* 7836 */         this.wrapperStatement = wrapperStatement;
/*      */       }
/*      */     } catch (SQLException e) {
/* 7839 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void throwRangeException(String valueAsString, int columnIndex, int jdbcType) throws SQLException
/*      */   {
/* 7845 */     String datatype = null;
/*      */     
/* 7847 */     switch (jdbcType) {
/*      */     case -6: 
/* 7849 */       datatype = "TINYINT";
/* 7850 */       break;
/*      */     case 5: 
/* 7852 */       datatype = "SMALLINT";
/* 7853 */       break;
/*      */     case 4: 
/* 7855 */       datatype = "INTEGER";
/* 7856 */       break;
/*      */     case -5: 
/* 7858 */       datatype = "BIGINT";
/* 7859 */       break;
/*      */     case 7: 
/* 7861 */       datatype = "REAL";
/* 7862 */       break;
/*      */     case 6: 
/* 7864 */       datatype = "FLOAT";
/* 7865 */       break;
/*      */     case 8: 
/* 7867 */       datatype = "DOUBLE";
/* 7868 */       break;
/*      */     case 3: 
/* 7870 */       datatype = "DECIMAL";
/* 7871 */       break;
/*      */     case -4: case -3: case -2: case -1: case 0: case 1: case 2: default: 
/* 7873 */       datatype = " (JDBC type '" + jdbcType + "')";
/*      */     }
/*      */     
/* 7876 */     throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 7887 */     if (this.reallyResult) {
/* 7888 */       return super.toString();
/*      */     }
/*      */     
/* 7891 */     return "Result set representing update count of " + this.updateCount;
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateArray(int arg0, Array arg1)
/*      */     throws SQLException
/*      */   {
/* 7898 */     throw SQLError.notImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateArray(String arg0, Array arg1)
/*      */     throws SQLException
/*      */   {
/* 7905 */     throw SQLError.notImplemented();
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
/*      */   public void updateAsciiStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 7929 */     throw new NotUpdatable();
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
/*      */   public void updateAsciiStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 7951 */     updateAsciiStream(findColumn(columnName), x, length);
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
/*      */   public void updateBigDecimal(int columnIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 7972 */     throw new NotUpdatable();
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
/*      */   public void updateBigDecimal(String columnName, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 7991 */     updateBigDecimal(findColumn(columnName), x);
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
/*      */   public void updateBinaryStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8015 */     throw new NotUpdatable();
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
/*      */   public void updateBinaryStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8037 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateBlob(int arg0, java.sql.Blob arg1)
/*      */     throws SQLException
/*      */   {
/* 8044 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateBlob(String arg0, java.sql.Blob arg1)
/*      */     throws SQLException
/*      */   {
/* 8051 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(int columnIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 8071 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(String columnName, boolean x)
/*      */     throws SQLException
/*      */   {
/* 8089 */     updateBoolean(findColumn(columnName), x);
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
/*      */   public void updateByte(int columnIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 8109 */     throw new NotUpdatable();
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
/*      */   public void updateByte(String columnName, byte x)
/*      */     throws SQLException
/*      */   {
/* 8127 */     updateByte(findColumn(columnName), x);
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
/*      */   public void updateBytes(int columnIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 8147 */     throw new NotUpdatable();
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
/*      */   public void updateBytes(String columnName, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 8165 */     updateBytes(findColumn(columnName), x);
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
/*      */   public void updateCharacterStream(int columnIndex, Reader x, int length)
/*      */     throws SQLException
/*      */   {
/* 8189 */     throw new NotUpdatable();
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
/*      */   public void updateCharacterStream(String columnName, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 8211 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateClob(int arg0, java.sql.Clob arg1)
/*      */     throws SQLException
/*      */   {
/* 8218 */     throw SQLError.notImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateClob(String columnName, java.sql.Clob clob)
/*      */     throws SQLException
/*      */   {
/* 8226 */     updateClob(findColumn(columnName), clob);
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
/*      */   public void updateDate(int columnIndex, Date x)
/*      */     throws SQLException
/*      */   {
/* 8247 */     throw new NotUpdatable();
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
/*      */   public void updateDate(String columnName, Date x)
/*      */     throws SQLException
/*      */   {
/* 8266 */     updateDate(findColumn(columnName), x);
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
/*      */   public void updateDouble(int columnIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 8286 */     throw new NotUpdatable();
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
/*      */   public void updateDouble(String columnName, double x)
/*      */     throws SQLException
/*      */   {
/* 8304 */     updateDouble(findColumn(columnName), x);
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
/*      */   public void updateFloat(int columnIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 8324 */     throw new NotUpdatable();
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
/*      */   public void updateFloat(String columnName, float x)
/*      */     throws SQLException
/*      */   {
/* 8342 */     updateFloat(findColumn(columnName), x);
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
/*      */   public void updateInt(int columnIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 8362 */     throw new NotUpdatable();
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
/*      */   public void updateInt(String columnName, int x)
/*      */     throws SQLException
/*      */   {
/* 8380 */     updateInt(findColumn(columnName), x);
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
/*      */   public void updateLong(int columnIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 8400 */     throw new NotUpdatable();
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
/*      */   public void updateLong(String columnName, long x)
/*      */     throws SQLException
/*      */   {
/* 8418 */     updateLong(findColumn(columnName), x);
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
/*      */   public void updateNull(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 8436 */     throw new NotUpdatable();
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
/*      */   public void updateNull(String columnName)
/*      */     throws SQLException
/*      */   {
/* 8452 */     updateNull(findColumn(columnName));
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
/*      */   public void updateObject(int columnIndex, Object x)
/*      */     throws SQLException
/*      */   {
/* 8472 */     throw new NotUpdatable();
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
/*      */   public void updateObject(int columnIndex, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 8497 */     throw new NotUpdatable();
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
/*      */   public void updateObject(String columnName, Object x)
/*      */     throws SQLException
/*      */   {
/* 8515 */     updateObject(findColumn(columnName), x);
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
/*      */   public void updateObject(String columnName, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 8538 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateRef(int arg0, Ref arg1)
/*      */     throws SQLException
/*      */   {
/* 8545 */     throw SQLError.notImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateRef(String arg0, Ref arg1)
/*      */     throws SQLException
/*      */   {
/* 8552 */     throw SQLError.notImplemented();
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
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 8566 */     throw new NotUpdatable();
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
/*      */   public void updateShort(int columnIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 8586 */     throw new NotUpdatable();
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
/*      */   public void updateShort(String columnName, short x)
/*      */     throws SQLException
/*      */   {
/* 8604 */     updateShort(findColumn(columnName), x);
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
/*      */   public void updateString(int columnIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 8624 */     throw new NotUpdatable();
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
/*      */   public void updateString(String columnName, String x)
/*      */     throws SQLException
/*      */   {
/* 8642 */     updateString(findColumn(columnName), x);
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
/*      */   public void updateTime(int columnIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 8663 */     throw new NotUpdatable();
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
/*      */   public void updateTime(String columnName, Time x)
/*      */     throws SQLException
/*      */   {
/* 8682 */     updateTime(findColumn(columnName), x);
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
/*      */   public void updateTimestamp(int columnIndex, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 8704 */     throw new NotUpdatable();
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
/*      */   public void updateTimestamp(String columnName, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 8723 */     updateTimestamp(findColumn(columnName), x);
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
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/* 8738 */     return this.wasNullFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Calendar getGmtCalendar()
/*      */   {
/* 8745 */     if (this.gmtCalendar == null) {
/* 8746 */       this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */     
/* 8749 */     return this.gmtCalendar;
/*      */   }
/*      */   
/*      */   protected ExceptionInterceptor getExceptionInterceptor() {
/* 8753 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 28	com/mysql/jdbc/ResultSetImpl:fetchDirection	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2562	-> byte code offset #0
/*      */     //   Java source line #2563	-> byte code offset #12
/*      */     //   Java source line #2564	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	ResultSetImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 29	com/mysql/jdbc/ResultSetImpl:fetchSize	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2576	-> byte code offset #0
/*      */     //   Java source line #2577	-> byte code offset #12
/*      */     //   Java source line #2578	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	ResultSetImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public char getFirstCharOfQuery()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 345	com/mysql/jdbc/ResultSetImpl:firstCharOfQuery	C
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     //   24: astore_1
/*      */     //   25: new 346	java/lang/RuntimeException
/*      */     //   28: dup
/*      */     //   29: aload_1
/*      */     //   30: invokespecial 347	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   33: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2589	-> byte code offset #0
/*      */     //   Java source line #2590	-> byte code offset #12
/*      */     //   Java source line #2591	-> byte code offset #19
/*      */     //   Java source line #2592	-> byte code offset #24
/*      */     //   Java source line #2593	-> byte code offset #25
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	34	0	this	ResultSetImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   24	6	1	e	SQLException
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */     //   0	18	24	java/sql/SQLException
/*      */     //   19	24	24	java/sql/SQLException
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String getServerInfo()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 41	com/mysql/jdbc/ResultSetImpl:serverInfo	Ljava/lang/String;
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     //   24: astore_1
/*      */     //   25: new 346	java/lang/RuntimeException
/*      */     //   28: dup
/*      */     //   29: aload_1
/*      */     //   30: invokespecial 347	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   33: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5414	-> byte code offset #0
/*      */     //   Java source line #5415	-> byte code offset #12
/*      */     //   Java source line #5416	-> byte code offset #19
/*      */     //   Java source line #5417	-> byte code offset #24
/*      */     //   Java source line #5418	-> byte code offset #25
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	34	0	this	ResultSetImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   24	6	1	e	SQLException
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */     //   0	18	24	java/sql/SQLException
/*      */     //   19	24	24	java/sql/SQLException
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Time getTimeFromString(String timeAsString, Calendar targetCalendar, int columnIndex, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore 6
/*      */     //   12: monitorenter
/*      */     //   13: iconst_0
/*      */     //   14: istore 7
/*      */     //   16: iconst_0
/*      */     //   17: istore 8
/*      */     //   19: iconst_0
/*      */     //   20: istore 9
/*      */     //   22: aload_1
/*      */     //   23: ifnonnull +13 -> 36
/*      */     //   26: aload_0
/*      */     //   27: iconst_1
/*      */     //   28: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   31: aconst_null
/*      */     //   32: aload 6
/*      */     //   34: monitorexit
/*      */     //   35: areturn
/*      */     //   36: aload_1
/*      */     //   37: invokevirtual 248	java/lang/String:trim	()Ljava/lang/String;
/*      */     //   40: astore_1
/*      */     //   41: aload_1
/*      */     //   42: ldc -102
/*      */     //   44: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   47: ifne +33 -> 80
/*      */     //   50: aload_1
/*      */     //   51: ldc_w 294
/*      */     //   54: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   57: ifne +23 -> 80
/*      */     //   60: aload_1
/*      */     //   61: ldc_w 295
/*      */     //   64: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   67: ifne +13 -> 80
/*      */     //   70: aload_1
/*      */     //   71: ldc_w 296
/*      */     //   74: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   77: ifeq +97 -> 174
/*      */     //   80: ldc_w 297
/*      */     //   83: aload_0
/*      */     //   84: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   87: invokeinterface 298 1 0
/*      */     //   92: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   95: ifeq +13 -> 108
/*      */     //   98: aload_0
/*      */     //   99: iconst_1
/*      */     //   100: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   103: aconst_null
/*      */     //   104: aload 6
/*      */     //   106: monitorexit
/*      */     //   107: areturn
/*      */     //   108: ldc_w 299
/*      */     //   111: aload_0
/*      */     //   112: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   115: invokeinterface 298 1 0
/*      */     //   120: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   123: ifeq +39 -> 162
/*      */     //   126: new 181	java/lang/StringBuilder
/*      */     //   129: dup
/*      */     //   130: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   133: ldc_w 300
/*      */     //   136: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   139: aload_1
/*      */     //   140: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   143: ldc_w 576
/*      */     //   146: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   149: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   152: ldc -114
/*      */     //   154: aload_0
/*      */     //   155: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   158: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   161: athrow
/*      */     //   162: aload_0
/*      */     //   163: aload_2
/*      */     //   164: iconst_0
/*      */     //   165: iconst_0
/*      */     //   166: iconst_0
/*      */     //   167: invokevirtual 577	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   170: aload 6
/*      */     //   172: monitorexit
/*      */     //   173: areturn
/*      */     //   174: aload_0
/*      */     //   175: iconst_0
/*      */     //   176: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   179: aload_0
/*      */     //   180: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   183: iload_3
/*      */     //   184: iconst_1
/*      */     //   185: isub
/*      */     //   186: aaload
/*      */     //   187: astore 10
/*      */     //   189: aload 10
/*      */     //   191: invokevirtual 209	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   194: bipush 7
/*      */     //   196: if_icmpne +342 -> 538
/*      */     //   199: aload_1
/*      */     //   200: invokevirtual 199	java/lang/String:length	()I
/*      */     //   203: istore 11
/*      */     //   205: iload 11
/*      */     //   207: tableswitch	default:+192->399, 10:+160->367, 11:+192->399, 12:+107->314, 13:+192->399, 14:+107->314, 15:+192->399, 16:+192->399, 17:+192->399, 18:+192->399, 19:+53->260
/*      */     //   260: aload_1
/*      */     //   261: iload 11
/*      */     //   263: bipush 8
/*      */     //   265: isub
/*      */     //   266: iload 11
/*      */     //   268: bipush 6
/*      */     //   270: isub
/*      */     //   271: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   274: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   277: istore 7
/*      */     //   279: aload_1
/*      */     //   280: iload 11
/*      */     //   282: iconst_5
/*      */     //   283: isub
/*      */     //   284: iload 11
/*      */     //   286: iconst_3
/*      */     //   287: isub
/*      */     //   288: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   291: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   294: istore 8
/*      */     //   296: aload_1
/*      */     //   297: iload 11
/*      */     //   299: iconst_2
/*      */     //   300: isub
/*      */     //   301: iload 11
/*      */     //   303: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   306: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   309: istore 9
/*      */     //   311: goto +144 -> 455
/*      */     //   314: aload_1
/*      */     //   315: iload 11
/*      */     //   317: bipush 6
/*      */     //   319: isub
/*      */     //   320: iload 11
/*      */     //   322: iconst_4
/*      */     //   323: isub
/*      */     //   324: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   327: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   330: istore 7
/*      */     //   332: aload_1
/*      */     //   333: iload 11
/*      */     //   335: iconst_4
/*      */     //   336: isub
/*      */     //   337: iload 11
/*      */     //   339: iconst_2
/*      */     //   340: isub
/*      */     //   341: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   344: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   347: istore 8
/*      */     //   349: aload_1
/*      */     //   350: iload 11
/*      */     //   352: iconst_2
/*      */     //   353: isub
/*      */     //   354: iload 11
/*      */     //   356: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   359: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   362: istore 9
/*      */     //   364: goto +91 -> 455
/*      */     //   367: aload_1
/*      */     //   368: bipush 6
/*      */     //   370: bipush 8
/*      */     //   372: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   375: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   378: istore 7
/*      */     //   380: aload_1
/*      */     //   381: bipush 8
/*      */     //   383: bipush 10
/*      */     //   385: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   388: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   391: istore 8
/*      */     //   393: iconst_0
/*      */     //   394: istore 9
/*      */     //   396: goto +59 -> 455
/*      */     //   399: new 181	java/lang/StringBuilder
/*      */     //   402: dup
/*      */     //   403: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   406: ldc_w 578
/*      */     //   409: invokestatic 136	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   412: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   415: iload_3
/*      */     //   416: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   419: ldc_w 579
/*      */     //   422: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   425: aload_0
/*      */     //   426: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   429: iload_3
/*      */     //   430: iconst_1
/*      */     //   431: isub
/*      */     //   432: aaload
/*      */     //   433: invokevirtual 580	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   436: ldc_w 581
/*      */     //   439: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   442: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   445: ldc -114
/*      */     //   447: aload_0
/*      */     //   448: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   451: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   454: athrow
/*      */     //   455: new 582	java/sql/SQLWarning
/*      */     //   458: dup
/*      */     //   459: new 181	java/lang/StringBuilder
/*      */     //   462: dup
/*      */     //   463: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   466: ldc_w 583
/*      */     //   469: invokestatic 136	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   472: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   475: iload_3
/*      */     //   476: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   479: ldc_w 579
/*      */     //   482: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   485: aload_0
/*      */     //   486: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   489: iload_3
/*      */     //   490: iconst_1
/*      */     //   491: isub
/*      */     //   492: aaload
/*      */     //   493: invokevirtual 580	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   496: ldc_w 581
/*      */     //   499: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   502: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   505: invokespecial 584	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   508: astore 12
/*      */     //   510: aload_0
/*      */     //   511: getfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   514: ifnonnull +12 -> 526
/*      */     //   517: aload_0
/*      */     //   518: aload 12
/*      */     //   520: putfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   523: goto +12 -> 535
/*      */     //   526: aload_0
/*      */     //   527: getfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   530: aload 12
/*      */     //   532: invokevirtual 585	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   535: goto +265 -> 800
/*      */     //   538: aload 10
/*      */     //   540: invokevirtual 209	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   543: bipush 12
/*      */     //   545: if_icmpne +125 -> 670
/*      */     //   548: aload_1
/*      */     //   549: bipush 11
/*      */     //   551: bipush 13
/*      */     //   553: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   556: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   559: istore 7
/*      */     //   561: aload_1
/*      */     //   562: bipush 14
/*      */     //   564: bipush 16
/*      */     //   566: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   569: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   572: istore 8
/*      */     //   574: aload_1
/*      */     //   575: bipush 17
/*      */     //   577: bipush 19
/*      */     //   579: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   582: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   585: istore 9
/*      */     //   587: new 582	java/sql/SQLWarning
/*      */     //   590: dup
/*      */     //   591: new 181	java/lang/StringBuilder
/*      */     //   594: dup
/*      */     //   595: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   598: ldc_w 586
/*      */     //   601: invokestatic 136	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   604: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   607: iload_3
/*      */     //   608: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   611: ldc_w 579
/*      */     //   614: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   617: aload_0
/*      */     //   618: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   621: iload_3
/*      */     //   622: iconst_1
/*      */     //   623: isub
/*      */     //   624: aaload
/*      */     //   625: invokevirtual 580	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   628: ldc_w 581
/*      */     //   631: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   634: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   637: invokespecial 584	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   640: astore 11
/*      */     //   642: aload_0
/*      */     //   643: getfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   646: ifnonnull +12 -> 658
/*      */     //   649: aload_0
/*      */     //   650: aload 11
/*      */     //   652: putfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   655: goto +12 -> 667
/*      */     //   658: aload_0
/*      */     //   659: getfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   662: aload 11
/*      */     //   664: invokevirtual 585	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   667: goto +133 -> 800
/*      */     //   670: aload 10
/*      */     //   672: invokevirtual 209	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   675: bipush 10
/*      */     //   677: if_icmpne +15 -> 692
/*      */     //   680: aload_0
/*      */     //   681: aload_2
/*      */     //   682: iconst_0
/*      */     //   683: iconst_0
/*      */     //   684: iconst_0
/*      */     //   685: invokevirtual 577	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   688: aload 6
/*      */     //   690: monitorexit
/*      */     //   691: areturn
/*      */     //   692: aload_1
/*      */     //   693: invokevirtual 199	java/lang/String:length	()I
/*      */     //   696: iconst_5
/*      */     //   697: if_icmpeq +58 -> 755
/*      */     //   700: aload_1
/*      */     //   701: invokevirtual 199	java/lang/String:length	()I
/*      */     //   704: bipush 8
/*      */     //   706: if_icmpeq +49 -> 755
/*      */     //   709: new 181	java/lang/StringBuilder
/*      */     //   712: dup
/*      */     //   713: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   716: ldc_w 587
/*      */     //   719: invokestatic 136	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   722: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   725: aload_1
/*      */     //   726: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   729: ldc_w 588
/*      */     //   732: invokestatic 136	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   735: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   738: iload_3
/*      */     //   739: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   742: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   745: ldc -114
/*      */     //   747: aload_0
/*      */     //   748: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   751: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   754: athrow
/*      */     //   755: aload_1
/*      */     //   756: iconst_0
/*      */     //   757: iconst_2
/*      */     //   758: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   761: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   764: istore 7
/*      */     //   766: aload_1
/*      */     //   767: iconst_3
/*      */     //   768: iconst_5
/*      */     //   769: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   772: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   775: istore 8
/*      */     //   777: aload_1
/*      */     //   778: invokevirtual 199	java/lang/String:length	()I
/*      */     //   781: iconst_5
/*      */     //   782: if_icmpne +7 -> 789
/*      */     //   785: iconst_0
/*      */     //   786: goto +12 -> 798
/*      */     //   789: aload_1
/*      */     //   790: bipush 6
/*      */     //   792: invokevirtual 589	java/lang/String:substring	(I)Ljava/lang/String;
/*      */     //   795: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   798: istore 9
/*      */     //   800: aload_0
/*      */     //   801: invokevirtual 590	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   804: astore 11
/*      */     //   806: aload 11
/*      */     //   808: dup
/*      */     //   809: astore 12
/*      */     //   811: monitorenter
/*      */     //   812: aload_0
/*      */     //   813: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   816: aload 11
/*      */     //   818: aload_2
/*      */     //   819: aload_0
/*      */     //   820: aload 11
/*      */     //   822: iload 7
/*      */     //   824: iload 8
/*      */     //   826: iload 9
/*      */     //   828: invokevirtual 577	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   831: aload_0
/*      */     //   832: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   835: invokeinterface 65 1 0
/*      */     //   840: aload 4
/*      */     //   842: iload 5
/*      */     //   844: invokestatic 591	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
/*      */     //   847: aload 12
/*      */     //   849: monitorexit
/*      */     //   850: aload 6
/*      */     //   852: monitorexit
/*      */     //   853: areturn
/*      */     //   854: astore 13
/*      */     //   856: aload 12
/*      */     //   858: monitorexit
/*      */     //   859: aload 13
/*      */     //   861: athrow
/*      */     //   862: astore 10
/*      */     //   864: aload 10
/*      */     //   866: invokevirtual 592	java/lang/RuntimeException:toString	()Ljava/lang/String;
/*      */     //   869: ldc -114
/*      */     //   871: aload_0
/*      */     //   872: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   875: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   878: astore 11
/*      */     //   880: aload 11
/*      */     //   882: aload 10
/*      */     //   884: invokevirtual 311	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */     //   887: pop
/*      */     //   888: aload 11
/*      */     //   890: athrow
/*      */     //   891: astore 14
/*      */     //   893: aload 6
/*      */     //   895: monitorexit
/*      */     //   896: aload 14
/*      */     //   898: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5965	-> byte code offset #0
/*      */     //   Java source line #5966	-> byte code offset #13
/*      */     //   Java source line #5967	-> byte code offset #16
/*      */     //   Java source line #5968	-> byte code offset #19
/*      */     //   Java source line #5972	-> byte code offset #22
/*      */     //   Java source line #5973	-> byte code offset #26
/*      */     //   Java source line #5975	-> byte code offset #31
/*      */     //   Java source line #5986	-> byte code offset #36
/*      */     //   Java source line #5988	-> byte code offset #41
/*      */     //   Java source line #5992	-> byte code offset #80
/*      */     //   Java source line #5994	-> byte code offset #98
/*      */     //   Java source line #5996	-> byte code offset #103
/*      */     //   Java source line #5997	-> byte code offset #108
/*      */     //   Java source line #5999	-> byte code offset #126
/*      */     //   Java source line #6006	-> byte code offset #162
/*      */     //   Java source line #6009	-> byte code offset #174
/*      */     //   Java source line #6011	-> byte code offset #179
/*      */     //   Java source line #6013	-> byte code offset #189
/*      */     //   Java source line #6015	-> byte code offset #199
/*      */     //   Java source line #6017	-> byte code offset #205
/*      */     //   Java source line #6020	-> byte code offset #260
/*      */     //   Java source line #6022	-> byte code offset #279
/*      */     //   Java source line #6024	-> byte code offset #296
/*      */     //   Java source line #6028	-> byte code offset #311
/*      */     //   Java source line #6031	-> byte code offset #314
/*      */     //   Java source line #6033	-> byte code offset #332
/*      */     //   Java source line #6035	-> byte code offset #349
/*      */     //   Java source line #6039	-> byte code offset #364
/*      */     //   Java source line #6042	-> byte code offset #367
/*      */     //   Java source line #6043	-> byte code offset #380
/*      */     //   Java source line #6044	-> byte code offset #393
/*      */     //   Java source line #6047	-> byte code offset #396
/*      */     //   Java source line #6050	-> byte code offset #399
/*      */     //   Java source line #6059	-> byte code offset #455
/*      */     //   Java source line #6066	-> byte code offset #510
/*      */     //   Java source line #6067	-> byte code offset #517
/*      */     //   Java source line #6069	-> byte code offset #526
/*      */     //   Java source line #6071	-> byte code offset #535
/*      */     //   Java source line #6072	-> byte code offset #548
/*      */     //   Java source line #6073	-> byte code offset #561
/*      */     //   Java source line #6074	-> byte code offset #574
/*      */     //   Java source line #6076	-> byte code offset #587
/*      */     //   Java source line #6083	-> byte code offset #642
/*      */     //   Java source line #6084	-> byte code offset #649
/*      */     //   Java source line #6086	-> byte code offset #658
/*      */     //   Java source line #6088	-> byte code offset #667
/*      */     //   Java source line #6089	-> byte code offset #680
/*      */     //   Java source line #6093	-> byte code offset #692
/*      */     //   Java source line #6095	-> byte code offset #709
/*      */     //   Java source line #6102	-> byte code offset #755
/*      */     //   Java source line #6103	-> byte code offset #766
/*      */     //   Java source line #6104	-> byte code offset #777
/*      */     //   Java source line #6108	-> byte code offset #800
/*      */     //   Java source line #6110	-> byte code offset #806
/*      */     //   Java source line #6111	-> byte code offset #812
/*      */     //   Java source line #6118	-> byte code offset #854
/*      */     //   Java source line #6119	-> byte code offset #862
/*      */     //   Java source line #6120	-> byte code offset #864
/*      */     //   Java source line #6122	-> byte code offset #880
/*      */     //   Java source line #6124	-> byte code offset #888
/*      */     //   Java source line #6126	-> byte code offset #891
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	899	0	this	ResultSetImpl
/*      */     //   0	899	1	timeAsString	String
/*      */     //   0	899	2	targetCalendar	Calendar
/*      */     //   0	899	3	columnIndex	int
/*      */     //   0	899	4	tz	TimeZone
/*      */     //   0	899	5	rollForward	boolean
/*      */     //   10	884	6	Ljava/lang/Object;	Object
/*      */     //   14	809	7	hr	int
/*      */     //   17	808	8	min	int
/*      */     //   20	807	9	sec	int
/*      */     //   187	484	10	timeColField	Field
/*      */     //   862	21	10	ex	RuntimeException
/*      */     //   203	152	11	length	int
/*      */     //   640	23	11	precisionLost	SQLWarning
/*      */     //   804	17	11	sessionCalendar	Calendar
/*      */     //   878	11	11	sqlEx	SQLException
/*      */     //   508	23	12	precisionLost	SQLWarning
/*      */     //   809	48	12	Ljava/lang/Object;	Object
/*      */     //   854	6	13	localObject1	Object
/*      */     //   891	6	14	localObject2	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   812	850	854	finally
/*      */     //   854	859	854	finally
/*      */     //   22	32	862	java/lang/RuntimeException
/*      */     //   36	104	862	java/lang/RuntimeException
/*      */     //   108	170	862	java/lang/RuntimeException
/*      */     //   174	688	862	java/lang/RuntimeException
/*      */     //   692	850	862	java/lang/RuntimeException
/*      */     //   854	862	862	java/lang/RuntimeException
/*      */     //   13	35	891	finally
/*      */     //   36	107	891	finally
/*      */     //   108	173	891	finally
/*      */     //   174	691	891	finally
/*      */     //   692	853	891	finally
/*      */     //   854	896	891	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Timestamp getTimestampFromString(int columnIndex, Calendar targetCalendar, String timestampValue, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iconst_0
/*      */     //   2: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   5: aload_3
/*      */     //   6: ifnonnull +10 -> 16
/*      */     //   9: aload_0
/*      */     //   10: iconst_1
/*      */     //   11: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   14: aconst_null
/*      */     //   15: areturn
/*      */     //   16: aload_3
/*      */     //   17: invokevirtual 248	java/lang/String:trim	()Ljava/lang/String;
/*      */     //   20: astore_3
/*      */     //   21: aload_3
/*      */     //   22: invokevirtual 199	java/lang/String:length	()I
/*      */     //   25: istore 6
/*      */     //   27: aload_0
/*      */     //   28: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   31: invokeinterface 596 1 0
/*      */     //   36: ifeq +15 -> 51
/*      */     //   39: aload_0
/*      */     //   40: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   43: invokeinterface 597 1 0
/*      */     //   48: goto +7 -> 55
/*      */     //   51: aload_0
/*      */     //   52: invokevirtual 590	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   55: astore 7
/*      */     //   57: aload 7
/*      */     //   59: dup
/*      */     //   60: astore 8
/*      */     //   62: monitorenter
/*      */     //   63: iload 6
/*      */     //   65: ifle +150 -> 215
/*      */     //   68: aload_3
/*      */     //   69: iconst_0
/*      */     //   70: invokevirtual 240	java/lang/String:charAt	(I)C
/*      */     //   73: bipush 48
/*      */     //   75: if_icmpne +140 -> 215
/*      */     //   78: aload_3
/*      */     //   79: ldc_w 294
/*      */     //   82: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   85: ifne +32 -> 117
/*      */     //   88: aload_3
/*      */     //   89: ldc_w 295
/*      */     //   92: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   95: ifne +22 -> 117
/*      */     //   98: aload_3
/*      */     //   99: ldc_w 296
/*      */     //   102: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   105: ifne +12 -> 117
/*      */     //   108: aload_3
/*      */     //   109: ldc -102
/*      */     //   111: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   114: ifeq +101 -> 215
/*      */     //   117: ldc_w 297
/*      */     //   120: aload_0
/*      */     //   121: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   124: invokeinterface 298 1 0
/*      */     //   129: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   132: ifeq +13 -> 145
/*      */     //   135: aload_0
/*      */     //   136: iconst_1
/*      */     //   137: putfield 49	com/mysql/jdbc/ResultSetImpl:wasNullFlag	Z
/*      */     //   140: aconst_null
/*      */     //   141: aload 8
/*      */     //   143: monitorexit
/*      */     //   144: areturn
/*      */     //   145: ldc_w 299
/*      */     //   148: aload_0
/*      */     //   149: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   152: invokeinterface 298 1 0
/*      */     //   157: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   160: ifeq +39 -> 199
/*      */     //   163: new 181	java/lang/StringBuilder
/*      */     //   166: dup
/*      */     //   167: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   170: ldc_w 300
/*      */     //   173: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   176: aload_3
/*      */     //   177: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   180: ldc_w 598
/*      */     //   183: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   186: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   189: ldc -114
/*      */     //   191: aload_0
/*      */     //   192: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   195: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   198: athrow
/*      */     //   199: aload_0
/*      */     //   200: aconst_null
/*      */     //   201: iconst_1
/*      */     //   202: iconst_1
/*      */     //   203: iconst_1
/*      */     //   204: iconst_0
/*      */     //   205: iconst_0
/*      */     //   206: iconst_0
/*      */     //   207: iconst_0
/*      */     //   208: invokevirtual 599	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   211: aload 8
/*      */     //   213: monitorexit
/*      */     //   214: areturn
/*      */     //   215: aload_0
/*      */     //   216: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   219: iload_1
/*      */     //   220: iconst_1
/*      */     //   221: isub
/*      */     //   222: aaload
/*      */     //   223: invokevirtual 209	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   226: bipush 13
/*      */     //   228: if_icmpne +82 -> 310
/*      */     //   231: aload_0
/*      */     //   232: getfield 69	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   235: ifne +27 -> 262
/*      */     //   238: aload 4
/*      */     //   240: aload_3
/*      */     //   241: iconst_0
/*      */     //   242: iconst_4
/*      */     //   243: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   246: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   249: iconst_1
/*      */     //   250: iconst_1
/*      */     //   251: iconst_0
/*      */     //   252: iconst_0
/*      */     //   253: iconst_0
/*      */     //   254: iconst_0
/*      */     //   255: invokestatic 174	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   258: aload 8
/*      */     //   260: monitorexit
/*      */     //   261: areturn
/*      */     //   262: aload_0
/*      */     //   263: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   266: aload 7
/*      */     //   268: aload_2
/*      */     //   269: aload_0
/*      */     //   270: aload 7
/*      */     //   272: aload_3
/*      */     //   273: iconst_0
/*      */     //   274: iconst_4
/*      */     //   275: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   278: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   281: iconst_1
/*      */     //   282: iconst_1
/*      */     //   283: iconst_0
/*      */     //   284: iconst_0
/*      */     //   285: iconst_0
/*      */     //   286: iconst_0
/*      */     //   287: invokevirtual 599	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   290: aload_0
/*      */     //   291: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   294: invokeinterface 65 1 0
/*      */     //   299: aload 4
/*      */     //   301: iload 5
/*      */     //   303: invokestatic 600	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   306: aload 8
/*      */     //   308: monitorexit
/*      */     //   309: areturn
/*      */     //   310: aload_3
/*      */     //   311: ldc -7
/*      */     //   313: invokevirtual 450	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   316: ifeq +15 -> 331
/*      */     //   319: aload_3
/*      */     //   320: iconst_0
/*      */     //   321: aload_3
/*      */     //   322: invokevirtual 199	java/lang/String:length	()I
/*      */     //   325: iconst_1
/*      */     //   326: isub
/*      */     //   327: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   330: astore_3
/*      */     //   331: iconst_0
/*      */     //   332: istore 9
/*      */     //   334: iconst_0
/*      */     //   335: istore 10
/*      */     //   337: iconst_0
/*      */     //   338: istore 11
/*      */     //   340: iconst_0
/*      */     //   341: istore 12
/*      */     //   343: iconst_0
/*      */     //   344: istore 13
/*      */     //   346: iconst_0
/*      */     //   347: istore 14
/*      */     //   349: iconst_0
/*      */     //   350: istore 15
/*      */     //   352: iload 6
/*      */     //   354: tableswitch	default:+869->1223, 2:+829->1183, 3:+869->1223, 4:+781->1135, 5:+869->1223, 6:+724->1078, 7:+869->1223, 8:+616->970, 9:+869->1223, 10:+461->815, 11:+869->1223, 12:+365->719, 13:+869->1223, 14:+287->641, 15:+869->1223, 16:+869->1223, 17:+869->1223, 18:+869->1223, 19:+114->468, 20:+114->468, 21:+114->468, 22:+114->468, 23:+114->468, 24:+114->468, 25:+114->468, 26:+114->468
/*      */     //   468: aload_3
/*      */     //   469: iconst_0
/*      */     //   470: iconst_4
/*      */     //   471: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   474: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   477: istore 9
/*      */     //   479: aload_3
/*      */     //   480: iconst_5
/*      */     //   481: bipush 7
/*      */     //   483: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   486: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   489: istore 10
/*      */     //   491: aload_3
/*      */     //   492: bipush 8
/*      */     //   494: bipush 10
/*      */     //   496: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   499: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   502: istore 11
/*      */     //   504: aload_3
/*      */     //   505: bipush 11
/*      */     //   507: bipush 13
/*      */     //   509: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   512: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   515: istore 12
/*      */     //   517: aload_3
/*      */     //   518: bipush 14
/*      */     //   520: bipush 16
/*      */     //   522: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   525: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   528: istore 13
/*      */     //   530: aload_3
/*      */     //   531: bipush 17
/*      */     //   533: bipush 19
/*      */     //   535: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   538: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   541: istore 14
/*      */     //   543: iconst_0
/*      */     //   544: istore 15
/*      */     //   546: iload 6
/*      */     //   548: bipush 19
/*      */     //   550: if_icmple +718 -> 1268
/*      */     //   553: aload_3
/*      */     //   554: bipush 46
/*      */     //   556: invokevirtual 601	java/lang/String:lastIndexOf	(I)I
/*      */     //   559: istore 16
/*      */     //   561: iload 16
/*      */     //   563: iconst_m1
/*      */     //   564: if_icmpeq +74 -> 638
/*      */     //   567: iload 16
/*      */     //   569: iconst_2
/*      */     //   570: iadd
/*      */     //   571: iload 6
/*      */     //   573: if_icmpgt +57 -> 630
/*      */     //   576: aload_3
/*      */     //   577: iload 16
/*      */     //   579: iconst_1
/*      */     //   580: iadd
/*      */     //   581: invokevirtual 589	java/lang/String:substring	(I)Ljava/lang/String;
/*      */     //   584: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   587: istore 15
/*      */     //   589: iload 6
/*      */     //   591: iload 16
/*      */     //   593: iconst_1
/*      */     //   594: iadd
/*      */     //   595: isub
/*      */     //   596: istore 17
/*      */     //   598: iload 17
/*      */     //   600: bipush 9
/*      */     //   602: if_icmpge +25 -> 627
/*      */     //   605: ldc2_w 602
/*      */     //   608: bipush 9
/*      */     //   610: iload 17
/*      */     //   612: isub
/*      */     //   613: i2d
/*      */     //   614: invokestatic 604	java/lang/Math:pow	(DD)D
/*      */     //   617: d2i
/*      */     //   618: istore 18
/*      */     //   620: iload 15
/*      */     //   622: iload 18
/*      */     //   624: imul
/*      */     //   625: istore 15
/*      */     //   627: goto +11 -> 638
/*      */     //   630: new 605	java/lang/IllegalArgumentException
/*      */     //   633: dup
/*      */     //   634: invokespecial 606	java/lang/IllegalArgumentException:<init>	()V
/*      */     //   637: athrow
/*      */     //   638: goto +630 -> 1268
/*      */     //   641: aload_3
/*      */     //   642: iconst_0
/*      */     //   643: iconst_4
/*      */     //   644: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   647: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   650: istore 9
/*      */     //   652: aload_3
/*      */     //   653: iconst_4
/*      */     //   654: bipush 6
/*      */     //   656: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   659: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   662: istore 10
/*      */     //   664: aload_3
/*      */     //   665: bipush 6
/*      */     //   667: bipush 8
/*      */     //   669: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   672: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   675: istore 11
/*      */     //   677: aload_3
/*      */     //   678: bipush 8
/*      */     //   680: bipush 10
/*      */     //   682: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   685: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   688: istore 12
/*      */     //   690: aload_3
/*      */     //   691: bipush 10
/*      */     //   693: bipush 12
/*      */     //   695: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   698: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   701: istore 13
/*      */     //   703: aload_3
/*      */     //   704: bipush 12
/*      */     //   706: bipush 14
/*      */     //   708: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   711: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   714: istore 14
/*      */     //   716: goto +552 -> 1268
/*      */     //   719: aload_3
/*      */     //   720: iconst_0
/*      */     //   721: iconst_2
/*      */     //   722: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   725: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   728: istore 9
/*      */     //   730: iload 9
/*      */     //   732: bipush 69
/*      */     //   734: if_icmpgt +10 -> 744
/*      */     //   737: iload 9
/*      */     //   739: bipush 100
/*      */     //   741: iadd
/*      */     //   742: istore 9
/*      */     //   744: wide
/*      */     //   750: aload_3
/*      */     //   751: iconst_2
/*      */     //   752: iconst_4
/*      */     //   753: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   756: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   759: istore 10
/*      */     //   761: aload_3
/*      */     //   762: iconst_4
/*      */     //   763: bipush 6
/*      */     //   765: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   768: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   771: istore 11
/*      */     //   773: aload_3
/*      */     //   774: bipush 6
/*      */     //   776: bipush 8
/*      */     //   778: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   781: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   784: istore 12
/*      */     //   786: aload_3
/*      */     //   787: bipush 8
/*      */     //   789: bipush 10
/*      */     //   791: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   794: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   797: istore 13
/*      */     //   799: aload_3
/*      */     //   800: bipush 10
/*      */     //   802: bipush 12
/*      */     //   804: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   807: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   810: istore 14
/*      */     //   812: goto +456 -> 1268
/*      */     //   815: aload_0
/*      */     //   816: getfield 58	com/mysql/jdbc/ResultSetImpl:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   819: iload_1
/*      */     //   820: iconst_1
/*      */     //   821: isub
/*      */     //   822: aaload
/*      */     //   823: invokevirtual 209	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   826: bipush 10
/*      */     //   828: if_icmpeq +14 -> 842
/*      */     //   831: aload_3
/*      */     //   832: ldc_w 607
/*      */     //   835: invokevirtual 250	java/lang/String:indexOf	(Ljava/lang/String;)I
/*      */     //   838: iconst_m1
/*      */     //   839: if_icmpeq +48 -> 887
/*      */     //   842: aload_3
/*      */     //   843: iconst_0
/*      */     //   844: iconst_4
/*      */     //   845: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   848: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   851: istore 9
/*      */     //   853: aload_3
/*      */     //   854: iconst_5
/*      */     //   855: bipush 7
/*      */     //   857: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   860: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   863: istore 10
/*      */     //   865: aload_3
/*      */     //   866: bipush 8
/*      */     //   868: bipush 10
/*      */     //   870: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   873: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   876: istore 11
/*      */     //   878: iconst_0
/*      */     //   879: istore 12
/*      */     //   881: iconst_0
/*      */     //   882: istore 13
/*      */     //   884: goto +384 -> 1268
/*      */     //   887: aload_3
/*      */     //   888: iconst_0
/*      */     //   889: iconst_2
/*      */     //   890: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   893: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   896: istore 9
/*      */     //   898: iload 9
/*      */     //   900: bipush 69
/*      */     //   902: if_icmpgt +10 -> 912
/*      */     //   905: iload 9
/*      */     //   907: bipush 100
/*      */     //   909: iadd
/*      */     //   910: istore 9
/*      */     //   912: aload_3
/*      */     //   913: iconst_2
/*      */     //   914: iconst_4
/*      */     //   915: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   918: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   921: istore 10
/*      */     //   923: aload_3
/*      */     //   924: iconst_4
/*      */     //   925: bipush 6
/*      */     //   927: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   930: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   933: istore 11
/*      */     //   935: aload_3
/*      */     //   936: bipush 6
/*      */     //   938: bipush 8
/*      */     //   940: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   943: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   946: istore 12
/*      */     //   948: aload_3
/*      */     //   949: bipush 8
/*      */     //   951: bipush 10
/*      */     //   953: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   956: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   959: istore 13
/*      */     //   961: wide
/*      */     //   967: goto +301 -> 1268
/*      */     //   970: aload_3
/*      */     //   971: ldc_w 608
/*      */     //   974: invokevirtual 250	java/lang/String:indexOf	(Ljava/lang/String;)I
/*      */     //   977: iconst_m1
/*      */     //   978: if_icmpeq +52 -> 1030
/*      */     //   981: aload_3
/*      */     //   982: iconst_0
/*      */     //   983: iconst_2
/*      */     //   984: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   987: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   990: istore 12
/*      */     //   992: aload_3
/*      */     //   993: iconst_3
/*      */     //   994: iconst_5
/*      */     //   995: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   998: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1001: istore 13
/*      */     //   1003: aload_3
/*      */     //   1004: bipush 6
/*      */     //   1006: bipush 8
/*      */     //   1008: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1011: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1014: istore 14
/*      */     //   1016: sipush 1970
/*      */     //   1019: istore 9
/*      */     //   1021: iconst_1
/*      */     //   1022: istore 10
/*      */     //   1024: iconst_1
/*      */     //   1025: istore 11
/*      */     //   1027: goto +241 -> 1268
/*      */     //   1030: aload_3
/*      */     //   1031: iconst_0
/*      */     //   1032: iconst_4
/*      */     //   1033: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1036: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1039: istore 9
/*      */     //   1041: aload_3
/*      */     //   1042: iconst_4
/*      */     //   1043: bipush 6
/*      */     //   1045: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1048: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1051: istore 10
/*      */     //   1053: aload_3
/*      */     //   1054: bipush 6
/*      */     //   1056: bipush 8
/*      */     //   1058: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1061: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1064: istore 11
/*      */     //   1066: wide
/*      */     //   1072: iinc 10 -1
/*      */     //   1075: goto +193 -> 1268
/*      */     //   1078: aload_3
/*      */     //   1079: iconst_0
/*      */     //   1080: iconst_2
/*      */     //   1081: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1084: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1087: istore 9
/*      */     //   1089: iload 9
/*      */     //   1091: bipush 69
/*      */     //   1093: if_icmpgt +10 -> 1103
/*      */     //   1096: iload 9
/*      */     //   1098: bipush 100
/*      */     //   1100: iadd
/*      */     //   1101: istore 9
/*      */     //   1103: wide
/*      */     //   1109: aload_3
/*      */     //   1110: iconst_2
/*      */     //   1111: iconst_4
/*      */     //   1112: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1115: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1118: istore 10
/*      */     //   1120: aload_3
/*      */     //   1121: iconst_4
/*      */     //   1122: bipush 6
/*      */     //   1124: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1127: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1130: istore 11
/*      */     //   1132: goto +136 -> 1268
/*      */     //   1135: aload_3
/*      */     //   1136: iconst_0
/*      */     //   1137: iconst_2
/*      */     //   1138: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1141: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1144: istore 9
/*      */     //   1146: iload 9
/*      */     //   1148: bipush 69
/*      */     //   1150: if_icmpgt +10 -> 1160
/*      */     //   1153: iload 9
/*      */     //   1155: bipush 100
/*      */     //   1157: iadd
/*      */     //   1158: istore 9
/*      */     //   1160: wide
/*      */     //   1166: aload_3
/*      */     //   1167: iconst_2
/*      */     //   1168: iconst_4
/*      */     //   1169: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1172: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1175: istore 10
/*      */     //   1177: iconst_1
/*      */     //   1178: istore 11
/*      */     //   1180: goto +88 -> 1268
/*      */     //   1183: aload_3
/*      */     //   1184: iconst_0
/*      */     //   1185: iconst_2
/*      */     //   1186: invokevirtual 303	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1189: invokestatic 304	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1192: istore 9
/*      */     //   1194: iload 9
/*      */     //   1196: bipush 69
/*      */     //   1198: if_icmpgt +10 -> 1208
/*      */     //   1201: iload 9
/*      */     //   1203: bipush 100
/*      */     //   1205: iadd
/*      */     //   1206: istore 9
/*      */     //   1208: wide
/*      */     //   1214: iconst_1
/*      */     //   1215: istore 10
/*      */     //   1217: iconst_1
/*      */     //   1218: istore 11
/*      */     //   1220: goto +48 -> 1268
/*      */     //   1223: new 215	java/sql/SQLException
/*      */     //   1226: dup
/*      */     //   1227: new 181	java/lang/StringBuilder
/*      */     //   1230: dup
/*      */     //   1231: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   1234: ldc_w 609
/*      */     //   1237: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1240: aload_3
/*      */     //   1241: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1244: ldc_w 610
/*      */     //   1247: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1250: iload_1
/*      */     //   1251: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   1254: ldc -7
/*      */     //   1256: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1259: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   1262: ldc -114
/*      */     //   1264: invokespecial 216	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1267: athrow
/*      */     //   1268: aload_0
/*      */     //   1269: getfield 69	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   1272: ifne +26 -> 1298
/*      */     //   1275: aload 4
/*      */     //   1277: iload 9
/*      */     //   1279: iload 10
/*      */     //   1281: iload 11
/*      */     //   1283: iload 12
/*      */     //   1285: iload 13
/*      */     //   1287: iload 14
/*      */     //   1289: iload 15
/*      */     //   1291: invokestatic 174	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1294: aload 8
/*      */     //   1296: monitorexit
/*      */     //   1297: areturn
/*      */     //   1298: aload_0
/*      */     //   1299: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   1302: aload 7
/*      */     //   1304: aload_2
/*      */     //   1305: aload_0
/*      */     //   1306: aload 7
/*      */     //   1308: iload 9
/*      */     //   1310: iload 10
/*      */     //   1312: iload 11
/*      */     //   1314: iload 12
/*      */     //   1316: iload 13
/*      */     //   1318: iload 14
/*      */     //   1320: iload 15
/*      */     //   1322: invokevirtual 599	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1325: aload_0
/*      */     //   1326: getfield 59	com/mysql/jdbc/ResultSetImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   1329: invokeinterface 65 1 0
/*      */     //   1334: aload 4
/*      */     //   1336: iload 5
/*      */     //   1338: invokestatic 600	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1341: aload 8
/*      */     //   1343: monitorexit
/*      */     //   1344: areturn
/*      */     //   1345: astore 19
/*      */     //   1347: aload 8
/*      */     //   1349: monitorexit
/*      */     //   1350: aload 19
/*      */     //   1352: athrow
/*      */     //   1353: astore 6
/*      */     //   1355: new 181	java/lang/StringBuilder
/*      */     //   1358: dup
/*      */     //   1359: invokespecial 182	java/lang/StringBuilder:<init>	()V
/*      */     //   1362: ldc_w 611
/*      */     //   1365: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1368: aload_3
/*      */     //   1369: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1372: ldc_w 612
/*      */     //   1375: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1378: iload_1
/*      */     //   1379: invokevirtual 369	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   1382: ldc_w 613
/*      */     //   1385: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1388: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   1391: ldc -114
/*      */     //   1393: aload_0
/*      */     //   1394: invokevirtual 138	com/mysql/jdbc/ResultSetImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   1397: invokestatic 139	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   1400: astore 7
/*      */     //   1402: aload 7
/*      */     //   1404: aload 6
/*      */     //   1406: invokevirtual 311	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */     //   1409: pop
/*      */     //   1410: aload 7
/*      */     //   1412: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6255	-> byte code offset #0
/*      */     //   Java source line #6257	-> byte code offset #5
/*      */     //   Java source line #6258	-> byte code offset #9
/*      */     //   Java source line #6260	-> byte code offset #14
/*      */     //   Java source line #6271	-> byte code offset #16
/*      */     //   Java source line #6273	-> byte code offset #21
/*      */     //   Java source line #6275	-> byte code offset #27
/*      */     //   Java source line #6279	-> byte code offset #57
/*      */     //   Java source line #6280	-> byte code offset #63
/*      */     //   Java source line #6287	-> byte code offset #117
/*      */     //   Java source line #6289	-> byte code offset #135
/*      */     //   Java source line #6291	-> byte code offset #140
/*      */     //   Java source line #6292	-> byte code offset #145
/*      */     //   Java source line #6294	-> byte code offset #163
/*      */     //   Java source line #6301	-> byte code offset #199
/*      */     //   Java source line #6303	-> byte code offset #215
/*      */     //   Java source line #6305	-> byte code offset #231
/*      */     //   Java source line #6306	-> byte code offset #238
/*      */     //   Java source line #6311	-> byte code offset #262
/*      */     //   Java source line #6321	-> byte code offset #310
/*      */     //   Java source line #6322	-> byte code offset #319
/*      */     //   Java source line #6328	-> byte code offset #331
/*      */     //   Java source line #6329	-> byte code offset #334
/*      */     //   Java source line #6330	-> byte code offset #337
/*      */     //   Java source line #6331	-> byte code offset #340
/*      */     //   Java source line #6332	-> byte code offset #343
/*      */     //   Java source line #6333	-> byte code offset #346
/*      */     //   Java source line #6334	-> byte code offset #349
/*      */     //   Java source line #6336	-> byte code offset #352
/*      */     //   Java source line #6345	-> byte code offset #468
/*      */     //   Java source line #6346	-> byte code offset #479
/*      */     //   Java source line #6348	-> byte code offset #491
/*      */     //   Java source line #6349	-> byte code offset #504
/*      */     //   Java source line #6351	-> byte code offset #517
/*      */     //   Java source line #6353	-> byte code offset #530
/*      */     //   Java source line #6356	-> byte code offset #543
/*      */     //   Java source line #6358	-> byte code offset #546
/*      */     //   Java source line #6359	-> byte code offset #553
/*      */     //   Java source line #6361	-> byte code offset #561
/*      */     //   Java source line #6362	-> byte code offset #567
/*      */     //   Java source line #6363	-> byte code offset #576
/*      */     //   Java source line #6366	-> byte code offset #589
/*      */     //   Java source line #6368	-> byte code offset #598
/*      */     //   Java source line #6369	-> byte code offset #605
/*      */     //   Java source line #6370	-> byte code offset #620
/*      */     //   Java source line #6372	-> byte code offset #627
/*      */     //   Java source line #6373	-> byte code offset #630
/*      */     //   Java source line #6381	-> byte code offset #638
/*      */     //   Java source line #6387	-> byte code offset #641
/*      */     //   Java source line #6388	-> byte code offset #652
/*      */     //   Java source line #6390	-> byte code offset #664
/*      */     //   Java source line #6391	-> byte code offset #677
/*      */     //   Java source line #6393	-> byte code offset #690
/*      */     //   Java source line #6395	-> byte code offset #703
/*      */     //   Java source line #6398	-> byte code offset #716
/*      */     //   Java source line #6402	-> byte code offset #719
/*      */     //   Java source line #6404	-> byte code offset #730
/*      */     //   Java source line #6405	-> byte code offset #737
/*      */     //   Java source line #6408	-> byte code offset #744
/*      */     //   Java source line #6410	-> byte code offset #750
/*      */     //   Java source line #6412	-> byte code offset #761
/*      */     //   Java source line #6413	-> byte code offset #773
/*      */     //   Java source line #6414	-> byte code offset #786
/*      */     //   Java source line #6416	-> byte code offset #799
/*      */     //   Java source line #6419	-> byte code offset #812
/*      */     //   Java source line #6423	-> byte code offset #815
/*      */     //   Java source line #6425	-> byte code offset #842
/*      */     //   Java source line #6426	-> byte code offset #853
/*      */     //   Java source line #6428	-> byte code offset #865
/*      */     //   Java source line #6429	-> byte code offset #878
/*      */     //   Java source line #6430	-> byte code offset #881
/*      */     //   Java source line #6432	-> byte code offset #887
/*      */     //   Java source line #6434	-> byte code offset #898
/*      */     //   Java source line #6435	-> byte code offset #905
/*      */     //   Java source line #6438	-> byte code offset #912
/*      */     //   Java source line #6440	-> byte code offset #923
/*      */     //   Java source line #6441	-> byte code offset #935
/*      */     //   Java source line #6442	-> byte code offset #948
/*      */     //   Java source line #6445	-> byte code offset #961
/*      */     //   Java source line #6448	-> byte code offset #967
/*      */     //   Java source line #6452	-> byte code offset #970
/*      */     //   Java source line #6453	-> byte code offset #981
/*      */     //   Java source line #6455	-> byte code offset #992
/*      */     //   Java source line #6457	-> byte code offset #1003
/*      */     //   Java source line #6459	-> byte code offset #1016
/*      */     //   Java source line #6460	-> byte code offset #1021
/*      */     //   Java source line #6461	-> byte code offset #1024
/*      */     //   Java source line #6462	-> byte code offset #1027
/*      */     //   Java source line #6465	-> byte code offset #1030
/*      */     //   Java source line #6466	-> byte code offset #1041
/*      */     //   Java source line #6468	-> byte code offset #1053
/*      */     //   Java source line #6470	-> byte code offset #1066
/*      */     //   Java source line #6471	-> byte code offset #1072
/*      */     //   Java source line #6473	-> byte code offset #1075
/*      */     //   Java source line #6477	-> byte code offset #1078
/*      */     //   Java source line #6479	-> byte code offset #1089
/*      */     //   Java source line #6480	-> byte code offset #1096
/*      */     //   Java source line #6483	-> byte code offset #1103
/*      */     //   Java source line #6485	-> byte code offset #1109
/*      */     //   Java source line #6487	-> byte code offset #1120
/*      */     //   Java source line #6489	-> byte code offset #1132
/*      */     //   Java source line #6493	-> byte code offset #1135
/*      */     //   Java source line #6495	-> byte code offset #1146
/*      */     //   Java source line #6496	-> byte code offset #1153
/*      */     //   Java source line #6499	-> byte code offset #1160
/*      */     //   Java source line #6501	-> byte code offset #1166
/*      */     //   Java source line #6504	-> byte code offset #1177
/*      */     //   Java source line #6506	-> byte code offset #1180
/*      */     //   Java source line #6510	-> byte code offset #1183
/*      */     //   Java source line #6512	-> byte code offset #1194
/*      */     //   Java source line #6513	-> byte code offset #1201
/*      */     //   Java source line #6516	-> byte code offset #1208
/*      */     //   Java source line #6517	-> byte code offset #1214
/*      */     //   Java source line #6518	-> byte code offset #1217
/*      */     //   Java source line #6520	-> byte code offset #1220
/*      */     //   Java source line #6524	-> byte code offset #1223
/*      */     //   Java source line #6530	-> byte code offset #1268
/*      */     //   Java source line #6531	-> byte code offset #1275
/*      */     //   Java source line #6535	-> byte code offset #1298
/*      */     //   Java source line #6542	-> byte code offset #1345
/*      */     //   Java source line #6543	-> byte code offset #1353
/*      */     //   Java source line #6544	-> byte code offset #1355
/*      */     //   Java source line #6547	-> byte code offset #1402
/*      */     //   Java source line #6549	-> byte code offset #1410
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1413	0	this	ResultSetImpl
/*      */     //   0	1413	1	columnIndex	int
/*      */     //   0	1413	2	targetCalendar	Calendar
/*      */     //   0	1413	3	timestampValue	String
/*      */     //   0	1413	4	tz	TimeZone
/*      */     //   0	1413	5	rollForward	boolean
/*      */     //   25	565	6	length	int
/*      */     //   1353	52	6	e	RuntimeException
/*      */     //   55	1252	7	sessionCalendar	Calendar
/*      */     //   1400	11	7	sqlEx	SQLException
/*      */     //   332	977	9	year	int
/*      */     //   335	976	10	month	int
/*      */     //   338	975	11	day	int
/*      */     //   341	974	12	hour	int
/*      */     //   344	973	13	minutes	int
/*      */     //   347	972	14	seconds	int
/*      */     //   350	971	15	nanos	int
/*      */     //   559	33	16	decimalIndex	int
/*      */     //   596	15	17	numDigits	int
/*      */     //   618	5	18	factor	int
/*      */     //   1345	6	19	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   63	144	1345	finally
/*      */     //   145	214	1345	finally
/*      */     //   215	261	1345	finally
/*      */     //   262	309	1345	finally
/*      */     //   310	1297	1345	finally
/*      */     //   1298	1344	1345	finally
/*      */     //   1345	1350	1345	finally
/*      */     //   0	15	1353	java/lang/RuntimeException
/*      */     //   16	144	1353	java/lang/RuntimeException
/*      */     //   145	214	1353	java/lang/RuntimeException
/*      */     //   215	261	1353	java/lang/RuntimeException
/*      */     //   262	309	1353	java/lang/RuntimeException
/*      */     //   310	1297	1353	java/lang/RuntimeException
/*      */     //   1298	1344	1353	java/lang/RuntimeException
/*      */     //   1345	1353	1353	java/lang/RuntimeException
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 48	com/mysql/jdbc/ResultSetImpl:warningChain	Ljava/sql/SQLWarning;
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6727	-> byte code offset #0
/*      */     //   Java source line #6728	-> byte code offset #12
/*      */     //   Java source line #6729	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	ResultSetImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 78	com/mysql/jdbc/ResultSetImpl:rowData	Lcom/mysql/jdbc/RowData;
/*      */     //   16: invokeinterface 146 1 0
/*      */     //   21: aload_1
/*      */     //   22: monitorexit
/*      */     //   23: ireturn
/*      */     //   24: astore_2
/*      */     //   25: aload_1
/*      */     //   26: monitorexit
/*      */     //   27: aload_2
/*      */     //   28: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6782	-> byte code offset #0
/*      */     //   Java source line #6783	-> byte code offset #12
/*      */     //   Java source line #6784	-> byte code offset #24
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	29	0	this	ResultSetImpl
/*      */     //   10	16	1	Ljava/lang/Object;	Object
/*      */     //   24	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	23	24	finally
/*      */     //   24	27	24	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 78	com/mysql/jdbc/ResultSetImpl:rowData	Lcom/mysql/jdbc/RowData;
/*      */     //   16: invokeinterface 619 1 0
/*      */     //   21: aload_1
/*      */     //   22: monitorexit
/*      */     //   23: ireturn
/*      */     //   24: astore_2
/*      */     //   25: aload_1
/*      */     //   26: monitorexit
/*      */     //   27: aload_2
/*      */     //   28: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6800	-> byte code offset #0
/*      */     //   Java source line #6801	-> byte code offset #12
/*      */     //   Java source line #6802	-> byte code offset #24
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	29	0	this	ResultSetImpl
/*      */     //   10	16	1	Ljava/lang/Object;	Object
/*      */     //   24	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	23	24	finally
/*      */     //   24	27	24	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/ResultSetImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 78	com/mysql/jdbc/ResultSetImpl:rowData	Lcom/mysql/jdbc/RowData;
/*      */     //   16: invokeinterface 620 1 0
/*      */     //   21: aload_1
/*      */     //   22: monitorexit
/*      */     //   23: ireturn
/*      */     //   24: astore_2
/*      */     //   25: aload_1
/*      */     //   26: monitorexit
/*      */     //   27: aload_2
/*      */     //   28: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6821	-> byte code offset #0
/*      */     //   Java source line #6822	-> byte code offset #12
/*      */     //   Java source line #6823	-> byte code offset #24
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	29	0	this	ResultSetImpl
/*      */     //   10	16	1	Ljava/lang/Object;	Object
/*      */     //   24	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	23	24	finally
/*      */     //   24	27	24	finally
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ResultSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */