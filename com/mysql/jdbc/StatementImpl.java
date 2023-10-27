/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.log.LogUtils;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.DriverManager;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StatementImpl
/*      */   implements Statement
/*      */ {
/*      */   protected static final String PING_MARKER = "/* ping */";
/*      */   
/*      */   class CancelTask
/*      */     extends TimerTask
/*      */   {
/*   78 */     long connectionId = 0L;
/*   79 */     String origHost = "";
/*   80 */     SQLException caughtWhileCancelling = null;
/*      */     StatementImpl toCancel;
/*   82 */     Properties origConnProps = null;
/*   83 */     String origConnURL = "";
/*      */     
/*      */     CancelTask(StatementImpl cancellee) throws SQLException {
/*   86 */       this.connectionId = cancellee.connectionId;
/*   87 */       this.origHost = StatementImpl.this.connection.getHost();
/*   88 */       this.toCancel = cancellee;
/*   89 */       this.origConnProps = new Properties();
/*      */       
/*   91 */       Properties props = StatementImpl.this.connection.getProperties();
/*      */       
/*   93 */       Enumeration<?> keys = props.propertyNames();
/*      */       
/*   95 */       while (keys.hasMoreElements()) {
/*   96 */         String key = keys.nextElement().toString();
/*   97 */         this.origConnProps.setProperty(key, props.getProperty(key));
/*      */       }
/*      */       
/*  100 */       this.origConnURL = StatementImpl.this.connection.getURL();
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*  105 */       Thread cancelThread = new Thread()
/*      */       {
/*      */         public void run()
/*      */         {
/*  109 */           Connection cancelConn = null;
/*  110 */           java.sql.Statement cancelStmt = null;
/*      */           try
/*      */           {
/*  113 */             if (StatementImpl.this.connection.getQueryTimeoutKillsConnection()) {
/*  114 */               StatementImpl.CancelTask.this.toCancel.wasCancelled = true;
/*  115 */               StatementImpl.CancelTask.this.toCancel.wasCancelledByTimeout = true;
/*  116 */               StatementImpl.this.connection.realClose(false, false, true, new MySQLStatementCancelledException(Messages.getString("Statement.ConnectionKilledDueToTimeout")));
/*      */             }
/*      */             else {
/*  119 */               synchronized (StatementImpl.this.cancelTimeoutMutex) {
/*  120 */                 if (StatementImpl.CancelTask.this.origConnURL.equals(StatementImpl.this.connection.getURL()))
/*      */                 {
/*  122 */                   cancelConn = StatementImpl.this.connection.duplicate();
/*  123 */                   cancelStmt = cancelConn.createStatement();
/*  124 */                   cancelStmt.execute("KILL QUERY " + StatementImpl.CancelTask.this.connectionId);
/*      */                 } else {
/*      */                   try {
/*  127 */                     cancelConn = (Connection)DriverManager.getConnection(StatementImpl.CancelTask.this.origConnURL, StatementImpl.CancelTask.this.origConnProps);
/*  128 */                     cancelStmt = cancelConn.createStatement();
/*  129 */                     cancelStmt.execute("KILL QUERY " + StatementImpl.CancelTask.this.connectionId);
/*      */                   }
/*      */                   catch (NullPointerException npe) {}
/*      */                 }
/*      */                 
/*  134 */                 StatementImpl.CancelTask.this.toCancel.wasCancelled = true;
/*  135 */                 StatementImpl.CancelTask.this.toCancel.wasCancelledByTimeout = true;
/*      */               }
/*      */             }
/*      */           } catch (SQLException sqlEx) {
/*  139 */             StatementImpl.CancelTask.this.caughtWhileCancelling = sqlEx;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           catch (NullPointerException npe) {}finally
/*      */           {
/*      */ 
/*      */ 
/*  148 */             if (cancelStmt != null) {
/*      */               try {
/*  150 */                 cancelStmt.close();
/*      */               } catch (SQLException sqlEx) {
/*  152 */                 throw new RuntimeException(sqlEx.toString());
/*      */               }
/*      */             }
/*      */             
/*  156 */             if (cancelConn != null) {
/*      */               try {
/*  158 */                 cancelConn.close();
/*      */               } catch (SQLException sqlEx) {
/*  160 */                 throw new RuntimeException(sqlEx.toString());
/*      */               }
/*      */             }
/*      */             
/*  164 */             StatementImpl.CancelTask.this.toCancel = null;
/*  165 */             StatementImpl.CancelTask.this.origConnProps = null;
/*  166 */             StatementImpl.CancelTask.this.origConnURL = null;
/*      */           }
/*      */           
/*      */         }
/*  170 */       };
/*  171 */       cancelThread.start();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected Object cancelTimeoutMutex = new Object();
/*      */   
/*      */ 
/*  181 */   static int statementCounter = 1;
/*      */   
/*      */   public static final byte USES_VARIABLES_FALSE = 0;
/*      */   
/*      */   public static final byte USES_VARIABLES_TRUE = 1;
/*      */   
/*      */   public static final byte USES_VARIABLES_UNKNOWN = -1;
/*      */   
/*  189 */   protected boolean wasCancelled = false;
/*  190 */   protected boolean wasCancelledByTimeout = false;
/*      */   
/*      */ 
/*      */   protected List<Object> batchedArgs;
/*      */   
/*      */ 
/*  196 */   protected SingleByteCharsetConverter charConverter = null;
/*      */   
/*      */ 
/*  199 */   protected String charEncoding = null;
/*      */   
/*      */ 
/*  202 */   protected volatile MySQLConnection connection = null;
/*      */   
/*  204 */   protected long connectionId = 0L;
/*      */   
/*      */ 
/*  207 */   protected String currentCatalog = null;
/*      */   
/*      */ 
/*  210 */   protected boolean doEscapeProcessing = true;
/*      */   
/*      */ 
/*  213 */   protected ProfilerEventHandler eventSink = null;
/*      */   
/*      */ 
/*  216 */   private int fetchSize = 0;
/*      */   
/*      */ 
/*  219 */   protected boolean isClosed = false;
/*      */   
/*      */ 
/*  222 */   protected long lastInsertId = -1L;
/*      */   
/*      */ 
/*  225 */   protected int maxFieldSize = MysqlIO.getMaxBuf();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  231 */   protected int maxRows = -1;
/*      */   
/*      */ 
/*  234 */   protected boolean maxRowsChanged = false;
/*      */   
/*      */ 
/*  237 */   protected Set<ResultSetInternalMethods> openResults = new HashSet();
/*      */   
/*      */ 
/*  240 */   protected boolean pedantic = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String pointOfOrigin;
/*      */   
/*      */ 
/*      */ 
/*  249 */   protected boolean profileSQL = false;
/*      */   
/*      */ 
/*  252 */   protected ResultSetInternalMethods results = null;
/*      */   
/*  254 */   protected ResultSetInternalMethods generatedKeysResults = null;
/*      */   
/*      */ 
/*  257 */   protected int resultSetConcurrency = 0;
/*      */   
/*      */ 
/*  260 */   protected int resultSetType = 0;
/*      */   
/*      */ 
/*      */   protected int statementId;
/*      */   
/*      */ 
/*  266 */   protected int timeoutInMillis = 0;
/*      */   
/*      */ 
/*  269 */   protected long updateCount = -1L;
/*      */   
/*      */ 
/*  272 */   protected boolean useUsageAdvisor = false;
/*      */   
/*      */ 
/*  275 */   protected SQLWarning warningChain = null;
/*      */   
/*      */ 
/*  278 */   protected boolean clearWarningsCalled = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  284 */   protected boolean holdResultsOpenOverClose = false;
/*      */   
/*  286 */   protected ArrayList<ResultSetRow> batchedGeneratedKeys = null;
/*      */   
/*  288 */   protected boolean retrieveGeneratedKeys = false;
/*      */   
/*  290 */   protected boolean continueBatchOnError = false;
/*      */   
/*  292 */   protected PingTarget pingTarget = null;
/*      */   
/*      */ 
/*      */   protected boolean useLegacyDatetimeCode;
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */   
/*  299 */   protected boolean lastQueryIsOnDupKeyUpdate = false;
/*      */   
/*      */ 
/*  302 */   protected final AtomicBoolean statementExecuting = new AtomicBoolean(false);
/*      */   
/*      */ 
/*  305 */   private boolean isImplicitlyClosingResults = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StatementImpl(MySQLConnection c, String catalog)
/*      */     throws SQLException
/*      */   {
/*  319 */     if ((c == null) || (c.isClosed())) {
/*  320 */       throw SQLError.createSQLException(Messages.getString("Statement.0"), "08003", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  325 */     this.connection = c;
/*  326 */     this.connectionId = this.connection.getId();
/*  327 */     this.exceptionInterceptor = this.connection.getExceptionInterceptor();
/*      */     
/*      */ 
/*  330 */     this.currentCatalog = catalog;
/*  331 */     this.pedantic = this.connection.getPedantic();
/*  332 */     this.continueBatchOnError = this.connection.getContinueBatchOnError();
/*  333 */     this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
/*      */     
/*  335 */     if (!this.connection.getDontTrackOpenResources()) {
/*  336 */       this.connection.registerStatement(this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  343 */     if (this.connection != null) {
/*  344 */       this.maxFieldSize = this.connection.getMaxAllowedPacket();
/*      */       
/*  346 */       int defaultFetchSize = this.connection.getDefaultFetchSize();
/*      */       
/*  348 */       if (defaultFetchSize != 0) {
/*  349 */         setFetchSize(defaultFetchSize);
/*      */       }
/*      */       
/*  352 */       if (this.connection.getUseUnicode()) {
/*  353 */         this.charEncoding = this.connection.getEncoding();
/*      */         
/*  355 */         this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  360 */       boolean profiling = (this.connection.getProfileSql()) || (this.connection.getUseUsageAdvisor()) || (this.connection.getLogSlowQueries());
/*      */       
/*      */ 
/*  363 */       if ((this.connection.getAutoGenerateTestcaseScript()) || (profiling)) {
/*  364 */         this.statementId = (statementCounter++);
/*      */       }
/*      */       
/*  367 */       if (profiling) {
/*  368 */         this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
/*  369 */         this.profileSQL = this.connection.getProfileSql();
/*  370 */         this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
/*  371 */         this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */       }
/*      */       
/*  374 */       int maxRowsConn = this.connection.getMaxRows();
/*      */       
/*  376 */       if (maxRowsConn != -1) {
/*  377 */         setMaxRows(maxRowsConn);
/*      */       }
/*      */       
/*  380 */       this.holdResultsOpenOverClose = this.connection.getHoldResultsOpenOverStatementClose();
/*      */     }
/*      */     
/*  383 */     this.version5013OrNewer = this.connection.versionMeetsMinimum(5, 0, 13);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBatch(String sql)
/*      */     throws SQLException
/*      */   {
/*  396 */     synchronized (checkClosed().getConnectionMutex()) {
/*  397 */       if (this.batchedArgs == null) {
/*  398 */         this.batchedArgs = new ArrayList();
/*      */       }
/*      */       
/*  401 */       if (sql != null) {
/*  402 */         this.batchedArgs.add(sql);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Object> getBatchedArgs()
/*      */   {
/*  414 */     return this.batchedArgs == null ? null : Collections.unmodifiableList(this.batchedArgs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cancel()
/*      */     throws SQLException
/*      */   {
/*  423 */     if (!this.statementExecuting.get()) {
/*  424 */       return;
/*      */     }
/*      */     
/*  427 */     if ((!this.isClosed) && (this.connection != null) && (this.connection.versionMeetsMinimum(5, 0, 0)))
/*      */     {
/*      */ 
/*  430 */       Connection cancelConn = null;
/*  431 */       java.sql.Statement cancelStmt = null;
/*      */       try
/*      */       {
/*  434 */         cancelConn = this.connection.duplicate();
/*  435 */         cancelStmt = cancelConn.createStatement();
/*  436 */         cancelStmt.execute("KILL QUERY " + this.connection.getIO().getThreadId());
/*      */         
/*  438 */         this.wasCancelled = true;
/*      */       } finally {
/*  440 */         if (cancelStmt != null) {
/*  441 */           cancelStmt.close();
/*      */         }
/*      */         
/*  444 */         if (cancelConn != null) {
/*  445 */           cancelConn.close();
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
/*      */   protected MySQLConnection checkClosed()
/*      */     throws SQLException
/*      */   {
/*  461 */     MySQLConnection c = this.connection;
/*      */     
/*  463 */     if (c == null) {
/*  464 */       throw SQLError.createSQLException(Messages.getString("Statement.49"), "08003", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  469 */     return c;
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
/*      */   protected void checkForDml(String sql, char firstStatementChar)
/*      */     throws SQLException
/*      */   {
/*  486 */     if ((firstStatementChar == 'I') || (firstStatementChar == 'U') || (firstStatementChar == 'D') || (firstStatementChar == 'A') || (firstStatementChar == 'C') || (firstStatementChar == 'T') || (firstStatementChar == 'R'))
/*      */     {
/*      */ 
/*      */ 
/*  490 */       String noCommentSql = StringUtils.stripComments(sql, "'\"", "'\"", true, false, true, true);
/*      */       
/*      */ 
/*  493 */       if ((StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "INSERT")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "UPDATE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DELETE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DROP")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "CREATE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "ALTER")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "TRUNCATE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "RENAME")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  502 */         throw SQLError.createSQLException(Messages.getString("Statement.57"), "S1009", getExceptionInterceptor());
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
/*      */   protected void checkNullOrEmptyQuery(String sql)
/*      */     throws SQLException
/*      */   {
/*  519 */     if (sql == null) {
/*  520 */       throw SQLError.createSQLException(Messages.getString("Statement.59"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  525 */     if (sql.length() == 0) {
/*  526 */       throw SQLError.createSQLException(Messages.getString("Statement.61"), "S1009", getExceptionInterceptor());
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
/*      */   public void clearBatch()
/*      */     throws SQLException
/*      */   {
/*  541 */     synchronized (checkClosed().getConnectionMutex()) {
/*  542 */       if (this.batchedArgs != null) {
/*  543 */         this.batchedArgs.clear();
/*      */       }
/*      */     }
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
/*  556 */     synchronized (checkClosed().getConnectionMutex()) {
/*  557 */       this.clearWarningsCalled = true;
/*  558 */       this.warningChain = null;
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
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  579 */       synchronized (checkClosed().getConnectionMutex()) {
/*  580 */         realClose(true, true);
/*      */       }
/*      */     } catch (SQLException sqlEx) {
/*  583 */       if ("08003".equals(sqlEx.getSQLState())) {
/*  584 */         return;
/*      */       }
/*      */       
/*  587 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void closeAllOpenResults()
/*      */     throws SQLException
/*      */   {
/*  595 */     synchronized (checkClosed().getConnectionMutex()) {
/*  596 */       if (this.openResults != null) {
/*  597 */         for (ResultSetInternalMethods element : this.openResults) {
/*      */           try {
/*  599 */             element.realClose(false);
/*      */           } catch (SQLException sqlEx) {
/*  601 */             AssertionFailedException.shouldNotHappen(sqlEx);
/*      */           }
/*      */         }
/*      */         
/*  605 */         this.openResults.clear();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void implicitlyCloseAllOpenResults()
/*      */     throws SQLException
/*      */   {
/*  614 */     this.isImplicitlyClosingResults = true;
/*      */     try {
/*  616 */       if ((!this.connection.getHoldResultsOpenOverStatementClose()) && (!this.connection.getDontTrackOpenResources()) && (!this.holdResultsOpenOverClose))
/*      */       {
/*      */ 
/*  619 */         if (this.results != null) {
/*  620 */           this.results.realClose(false);
/*      */         }
/*  622 */         if (this.generatedKeysResults != null) {
/*  623 */           this.generatedKeysResults.realClose(false);
/*      */         }
/*  625 */         closeAllOpenResults();
/*      */       }
/*      */     } finally {
/*  628 */       this.isImplicitlyClosingResults = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeOpenResultSet(ResultSetInternalMethods rs) {
/*      */     try {
/*  634 */       synchronized (checkClosed().getConnectionMutex()) {
/*  635 */         if (this.openResults != null) {
/*  636 */           this.openResults.remove(rs);
/*      */         }
/*      */         
/*  639 */         boolean hasMoreResults = rs.getNextResultSet() != null;
/*      */         
/*      */ 
/*  642 */         if ((this.results == rs) && (!hasMoreResults)) {
/*  643 */           this.results = null;
/*      */         }
/*  645 */         if (this.generatedKeysResults == rs) {
/*  646 */           this.generatedKeysResults = null;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  652 */         if ((!this.isImplicitlyClosingResults) && (!hasMoreResults)) {
/*  653 */           checkAndPerformCloseOnCompletionAction();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {}
/*      */   }
/*      */   
/*      */   public int getOpenResultSetCount()
/*      */   {
/*      */     try {
/*  663 */       synchronized (checkClosed().getConnectionMutex()) {
/*  664 */         if (this.openResults != null) {
/*  665 */           return this.openResults.size();
/*      */         }
/*      */         
/*  668 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  673 */       return 0;
/*      */     }
/*      */     catch (SQLException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */   private void checkAndPerformCloseOnCompletionAction()
/*      */   {
/*      */     try
/*      */     {
/*  683 */       synchronized (checkClosed().getConnectionMutex()) {
/*  684 */         if ((isCloseOnCompletion()) && (!this.connection.getDontTrackOpenResources()) && (getOpenResultSetCount() == 0) && ((this.results == null) || (!this.results.reallyResult()) || (this.results.isClosed())) && ((this.generatedKeysResults == null) || (!this.generatedKeysResults.reallyResult()) || (this.generatedKeysResults.isClosed())))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  691 */           realClose(false, false);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ResultSetInternalMethods createResultSetUsingServerFetch(String sql)
/*      */     throws SQLException
/*      */   {
/*  704 */     synchronized (checkClosed().getConnectionMutex()) {
/*  705 */       java.sql.PreparedStatement pStmt = this.connection.prepareStatement(sql, this.resultSetType, this.resultSetConcurrency);
/*      */       
/*      */ 
/*  708 */       pStmt.setFetchSize(this.fetchSize);
/*      */       
/*  710 */       if (this.maxRows > -1) {
/*  711 */         pStmt.setMaxRows(this.maxRows);
/*      */       }
/*      */       
/*  714 */       statementBegins();
/*      */       
/*  716 */       pStmt.execute();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  722 */       ResultSetInternalMethods rs = ((StatementImpl)pStmt).getResultSetInternal();
/*      */       
/*      */ 
/*  725 */       rs.setStatementUsedForFetchingRows((PreparedStatement)pStmt);
/*      */       
/*      */ 
/*  728 */       this.results = rs;
/*      */       
/*  730 */       return rs;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean createStreamingResultSet()
/*      */   {
/*      */     try
/*      */     {
/*  743 */       synchronized (checkClosed().getConnectionMutex()) {
/*  744 */         return (this.resultSetType == 1003) && (this.resultSetConcurrency == 1007) && (this.fetchSize == Integer.MIN_VALUE);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  750 */       return false;
/*      */     } catch (SQLException e) {}
/*      */   }
/*      */   
/*  754 */   private int originalResultSetType = 0;
/*  755 */   private int originalFetchSize = 0;
/*      */   
/*      */ 
/*      */   public void enableStreamingResults()
/*      */     throws SQLException
/*      */   {
/*  761 */     synchronized (checkClosed().getConnectionMutex()) {
/*  762 */       this.originalResultSetType = this.resultSetType;
/*  763 */       this.originalFetchSize = this.fetchSize;
/*      */       
/*  765 */       setFetchSize(Integer.MIN_VALUE);
/*  766 */       setResultSetType(1003);
/*      */     }
/*      */   }
/*      */   
/*      */   public void disableStreamingResults() throws SQLException {
/*  771 */     synchronized (checkClosed().getConnectionMutex()) {
/*  772 */       if ((this.fetchSize == Integer.MIN_VALUE) && (this.resultSetType == 1003))
/*      */       {
/*  774 */         setFetchSize(this.originalFetchSize);
/*  775 */         setResultSetType(this.originalResultSetType);
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
/*      */   public boolean execute(String sql)
/*      */     throws SQLException
/*      */   {
/*  795 */     return execute(sql, false);
/*      */   }
/*      */   
/*      */   private boolean execute(String sql, boolean returnGeneratedKeys) throws SQLException {
/*  799 */     MySQLConnection locallyScopedConn = checkClosed();
/*      */     char firstNonWsChar;
/*  801 */     boolean isSelect; boolean doStreaming; synchronized (locallyScopedConn.getConnectionMutex()) {
/*  802 */       this.retrieveGeneratedKeys = returnGeneratedKeys;
/*  803 */       this.lastQueryIsOnDupKeyUpdate = false;
/*  804 */       if (returnGeneratedKeys) {
/*  805 */         this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyInString(sql);
/*      */       }
/*  807 */       resetCancelledState();
/*      */       
/*  809 */       checkNullOrEmptyQuery(sql);
/*      */       
/*  811 */       checkClosed();
/*      */       
/*  813 */       firstNonWsChar = StringUtils.firstAlphaCharUc(sql, findStartOfStatement(sql));
/*      */       
/*  815 */       isSelect = true;
/*      */       
/*  817 */       if (firstNonWsChar != 'S') {
/*  818 */         isSelect = false;
/*      */         
/*  820 */         if (locallyScopedConn.isReadOnly()) {
/*  821 */           throw SQLError.createSQLException(Messages.getString("Statement.27") + Messages.getString("Statement.28"), "S1009", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  828 */       doStreaming = createStreamingResultSet();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  839 */       if ((doStreaming) && (locallyScopedConn.getNetTimeoutForStreamingResults() > 0))
/*      */       {
/*  841 */         executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + locallyScopedConn.getNetTimeoutForStreamingResults());
/*      */       }
/*      */       
/*      */       Object escapedSqlResult;
/*  845 */       if (this.doEscapeProcessing) {
/*  846 */         escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), locallyScopedConn);
/*      */         
/*      */ 
/*  849 */         if ((escapedSqlResult instanceof String)) {
/*  850 */           sql = (String)escapedSqlResult;
/*      */         } else {
/*  852 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/*  856 */       implicitlyCloseAllOpenResults();
/*      */       
/*  858 */       if ((sql.charAt(0) == '/') && 
/*  859 */         (sql.startsWith("/* ping */"))) {
/*  860 */         doPingInstead();
/*      */         
/*  862 */         escapedSqlResult = 1;jsr 595;return (boolean)escapedSqlResult;
/*      */       }
/*      */       
/*      */ 
/*  866 */       CachedResultSetMetaData cachedMetaData = null;
/*      */       
/*  868 */       ResultSetInternalMethods rs = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  877 */       this.batchedGeneratedKeys = null;
/*      */       
/*  879 */       if (useServerFetch()) {
/*  880 */         rs = createResultSetUsingServerFetch(sql);
/*      */       } else {
/*  882 */         timeoutTask = null;
/*      */         
/*  884 */         String oldCatalog = null;
/*      */         try
/*      */         {
/*  887 */           if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/*  890 */             timeoutTask = new CancelTask(this);
/*  891 */             locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */           }
/*      */           
/*      */ 
/*  895 */           if (!locallyScopedConn.getCatalog().equals(this.currentCatalog))
/*      */           {
/*  897 */             oldCatalog = locallyScopedConn.getCatalog();
/*  898 */             locallyScopedConn.setCatalog(this.currentCatalog);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  905 */           Field[] cachedFields = null;
/*      */           
/*  907 */           if (locallyScopedConn.getCacheResultSetMetadata()) {
/*  908 */             cachedMetaData = locallyScopedConn.getCachedMetaData(sql);
/*      */             
/*  910 */             if (cachedMetaData != null) {
/*  911 */               cachedFields = cachedMetaData.fields;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  918 */           if (locallyScopedConn.useMaxRows()) {
/*  919 */             int rowLimit = -1;
/*      */             
/*  921 */             if (isSelect) {
/*  922 */               if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) {
/*  923 */                 rowLimit = this.maxRows;
/*      */               }
/*  925 */               else if (this.maxRows <= 0) {
/*  926 */                 executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */               }
/*      */               else {
/*  929 */                 executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
/*      */               }
/*      */               
/*      */             }
/*      */             else {
/*  934 */               executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  939 */             statementBegins();
/*      */             
/*      */ 
/*  942 */             rs = locallyScopedConn.execSQL(this, sql, rowLimit, null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  947 */             statementBegins();
/*      */             
/*  949 */             rs = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  955 */           if (timeoutTask != null) {
/*  956 */             if (timeoutTask.caughtWhileCancelling != null) {
/*  957 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/*  960 */             timeoutTask.cancel();
/*  961 */             timeoutTask = null;
/*      */           }
/*      */           
/*  964 */           synchronized (this.cancelTimeoutMutex) {
/*  965 */             if (this.wasCancelled) {
/*  966 */               SQLException cause = null;
/*      */               
/*  968 */               if (this.wasCancelledByTimeout) {
/*  969 */                 cause = new MySQLTimeoutException();
/*      */               } else {
/*  971 */                 cause = new MySQLStatementCancelledException();
/*      */               }
/*      */               
/*  974 */               resetCancelledState();
/*      */               
/*  976 */               throw cause;
/*      */             }
/*      */           }
/*      */         } finally {
/*  980 */           if (timeoutTask != null) {
/*  981 */             timeoutTask.cancel();
/*  982 */             locallyScopedConn.getCancelTimer().purge();
/*      */           }
/*      */           
/*  985 */           if (oldCatalog != null) {
/*  986 */             locallyScopedConn.setCatalog(oldCatalog);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  991 */       if (rs != null) {
/*  992 */         this.lastInsertId = rs.getUpdateID();
/*      */         
/*  994 */         this.results = rs;
/*      */         
/*  996 */         rs.setFirstCharOfQuery(firstNonWsChar);
/*      */         
/*  998 */         if (rs.reallyResult()) {
/*  999 */           if (cachedMetaData != null) {
/* 1000 */             locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results);
/*      */ 
/*      */           }
/* 1003 */           else if (this.connection.getCacheResultSetMetadata()) {
/* 1004 */             locallyScopedConn.initializeResultsMetadataFromCache(sql, null, this.results);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1011 */       CancelTask timeoutTask = (rs != null) && (rs.reallyResult()) ? 1 : 0;jsr 17;return timeoutTask;
/*      */     } finally {
/* 1013 */       jsr 6; } localObject5 = returnAddress;this.statementExecuting.set(false);ret;
/*      */     
/* 1015 */     localObject6 = finally;throw ((Throwable)localObject6);
/*      */   }
/*      */   
/*      */   protected void statementBegins() {
/* 1019 */     this.clearWarningsCalled = false;
/* 1020 */     this.statementExecuting.set(true);
/*      */   }
/*      */   
/*      */   protected void resetCancelledState() throws SQLException {
/* 1024 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1025 */       if (this.cancelTimeoutMutex == null) {
/* 1026 */         return;
/*      */       }
/*      */       
/* 1029 */       synchronized (this.cancelTimeoutMutex) {
/* 1030 */         this.wasCancelled = false;
/* 1031 */         this.wasCancelledByTimeout = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, int returnGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/* 1043 */     if (returnGeneratedKeys == 1) {
/* 1044 */       checkClosed();
/*      */       
/* 1046 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1048 */       synchronized (locallyScopedConn.getConnectionMutex())
/*      */       {
/*      */ 
/*      */ 
/* 1052 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/* 1054 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1057 */           boolean bool1 = execute(sql, true);jsr 17;return bool1;
/*      */         } finally {
/* 1059 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1064 */     return execute(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, int[] generatedKeyIndices)
/*      */     throws SQLException
/*      */   {
/* 1072 */     MySQLConnection locallyScopedConn = checkClosed();
/*      */     
/* 1074 */     synchronized (locallyScopedConn.getConnectionMutex()) {
/* 1075 */       if ((generatedKeyIndices != null) && (generatedKeyIndices.length > 0))
/*      */       {
/* 1077 */         this.retrieveGeneratedKeys = true;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1082 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/* 1084 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1087 */           boolean bool1 = execute(sql, true);jsr 17;return bool1;
/*      */         } finally {
/* 1089 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */       
/*      */ 
/* 1093 */       return execute(sql);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, String[] generatedKeyNames)
/*      */     throws SQLException
/*      */   {
/* 1102 */     MySQLConnection locallyScopedConn = checkClosed();
/*      */     
/* 1104 */     synchronized (locallyScopedConn.getConnectionMutex()) {
/* 1105 */       if ((generatedKeyNames != null) && (generatedKeyNames.length > 0))
/*      */       {
/* 1107 */         this.retrieveGeneratedKeys = true;
/*      */         
/*      */ 
/*      */ 
/* 1111 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/* 1113 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1116 */           boolean bool1 = execute(sql, true);jsr 17;return bool1;
/*      */         } finally {
/* 1118 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */       
/*      */ 
/* 1122 */       return execute(sql);
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
/* 1141 */     MySQLConnection locallyScopedConn = checkClosed();
/*      */     
/* 1143 */     synchronized (locallyScopedConn.getConnectionMutex()) {
/* 1144 */       if (locallyScopedConn.isReadOnly()) {
/* 1145 */         throw SQLError.createSQLException(Messages.getString("Statement.34") + Messages.getString("Statement.35"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1151 */       implicitlyCloseAllOpenResults();
/*      */       
/* 1153 */       if ((this.batchedArgs == null) || (this.batchedArgs.size() == 0)) {
/* 1154 */         return new int[0];
/*      */       }
/*      */       
/*      */ 
/* 1158 */       int individualStatementTimeout = this.timeoutInMillis;
/* 1159 */       this.timeoutInMillis = 0;
/*      */       
/* 1161 */       CancelTask timeoutTask = null;
/*      */       try
/*      */       {
/* 1164 */         resetCancelledState();
/*      */         
/* 1166 */         statementBegins();
/*      */         try
/*      */         {
/* 1169 */           this.retrieveGeneratedKeys = true;
/*      */           
/* 1171 */           int[] updateCounts = null;
/*      */           
/*      */ 
/* 1174 */           if (this.batchedArgs != null) {
/* 1175 */             nbrCommands = this.batchedArgs.size();
/*      */             
/* 1177 */             this.batchedGeneratedKeys = new ArrayList(this.batchedArgs.size());
/*      */             
/* 1179 */             boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries();
/*      */             
/* 1181 */             if ((locallyScopedConn.versionMeetsMinimum(4, 1, 1)) && ((multiQueriesEnabled) || ((locallyScopedConn.getRewriteBatchedStatements()) && (nbrCommands > 4))))
/*      */             {
/*      */ 
/*      */ 
/* 1185 */               int[] arrayOfInt1 = executeBatchUsingMultiQueries(multiQueriesEnabled, nbrCommands, individualStatementTimeout);return arrayOfInt1;
/*      */             }
/*      */             
/* 1188 */             if ((locallyScopedConn.getEnableQueryTimeouts()) && (individualStatementTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */             {
/*      */ 
/* 1191 */               timeoutTask = new CancelTask(this);
/* 1192 */               locallyScopedConn.getCancelTimer().schedule(timeoutTask, individualStatementTimeout);
/*      */             }
/*      */             
/*      */ 
/* 1196 */             updateCounts = new int[nbrCommands];
/*      */             
/* 1198 */             for (int i = 0; i < nbrCommands; i++) {
/* 1199 */               updateCounts[i] = -3;
/*      */             }
/*      */             
/* 1202 */             SQLException sqlEx = null;
/*      */             
/* 1204 */             int commandIndex = 0;
/*      */             
/* 1206 */             for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/*      */               try {
/* 1208 */                 String sql = (String)this.batchedArgs.get(commandIndex);
/* 1209 */                 updateCounts[commandIndex] = executeUpdate(sql, true, true);
/*      */                 
/* 1211 */                 getBatchedGeneratedKeys(containsOnDuplicateKeyInString(sql) ? 1 : 0);
/*      */               } catch (SQLException ex) {
/* 1213 */                 updateCounts[commandIndex] = -3;
/*      */                 
/* 1215 */                 if ((this.continueBatchOnError) && (!(ex instanceof MySQLTimeoutException)) && (!(ex instanceof MySQLStatementCancelledException)) && (!hasDeadlockOrTimeoutRolledBackTx(ex)))
/*      */                 {
/*      */ 
/*      */ 
/* 1219 */                   sqlEx = ex;
/*      */                 } else {
/* 1221 */                   int[] newUpdateCounts = new int[commandIndex];
/*      */                   
/* 1223 */                   if (hasDeadlockOrTimeoutRolledBackTx(ex)) {
/* 1224 */                     for (int i = 0; i < newUpdateCounts.length; i++) {
/* 1225 */                       newUpdateCounts[i] = -3;
/*      */                     }
/*      */                   } else {
/* 1228 */                     System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex);
/*      */                   }
/*      */                   
/*      */ 
/* 1232 */                   throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 1239 */             if (sqlEx != null) {
/* 1240 */               throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1246 */           if (timeoutTask != null) {
/* 1247 */             if (timeoutTask.caughtWhileCancelling != null) {
/* 1248 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/* 1251 */             timeoutTask.cancel();
/*      */             
/* 1253 */             locallyScopedConn.getCancelTimer().purge();
/* 1254 */             timeoutTask = null;
/*      */           }
/*      */           
/* 1257 */           int nbrCommands = updateCounts != null ? updateCounts : new int[0];return nbrCommands;
/*      */         } finally {
/* 1259 */           this.statementExecuting.set(false);
/*      */         }
/*      */         
/*      */ 
/* 1263 */         localObject4 = returnAddress; } finally { jsr 6; } if (timeoutTask != null) {
/* 1264 */         timeoutTask.cancel();
/*      */         
/* 1266 */         locallyScopedConn.getCancelTimer().purge();
/*      */       }
/*      */       
/* 1269 */       resetCancelledState();
/*      */       
/* 1271 */       this.timeoutInMillis = individualStatementTimeout;
/*      */       
/* 1273 */       clearBatch();ret;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final boolean hasDeadlockOrTimeoutRolledBackTx(SQLException ex)
/*      */   {
/* 1279 */     int vendorCode = ex.getErrorCode();
/*      */     
/* 1281 */     switch (vendorCode) {
/*      */     case 1206: 
/*      */     case 1213: 
/* 1284 */       return true;
/*      */     case 1205: 
/* 1286 */       return !this.version5013OrNewer;
/*      */     }
/* 1288 */     return false;
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
/*      */   private int[] executeBatchUsingMultiQueries(boolean multiQueriesEnabled, int nbrCommands, int individualStatementTimeout)
/*      */     throws SQLException
/*      */   {
/* 1303 */     MySQLConnection locallyScopedConn = checkClosed();
/*      */     
/* 1305 */     synchronized (locallyScopedConn.getConnectionMutex()) {
/* 1306 */       if (!multiQueriesEnabled) {
/* 1307 */         locallyScopedConn.getIO().enableMultiQueries();
/*      */       }
/*      */       
/* 1310 */       java.sql.Statement batchStmt = null;
/*      */       
/* 1312 */       CancelTask timeoutTask = null;
/*      */       try
/*      */       {
/* 1315 */         int[] updateCounts = new int[nbrCommands];
/*      */         
/* 1317 */         for (int i = 0; i < nbrCommands; i++) {
/* 1318 */           updateCounts[i] = -3;
/*      */         }
/*      */         
/* 1321 */         int commandIndex = 0;
/*      */         
/* 1323 */         StringBuffer queryBuf = new StringBuffer();
/*      */         
/* 1325 */         batchStmt = locallyScopedConn.createStatement();
/*      */         
/* 1327 */         if ((locallyScopedConn.getEnableQueryTimeouts()) && (individualStatementTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1330 */           timeoutTask = new CancelTask((StatementImpl)batchStmt);
/* 1331 */           locallyScopedConn.getCancelTimer().schedule(timeoutTask, individualStatementTimeout);
/*      */         }
/*      */         
/*      */ 
/* 1335 */         int counter = 0;
/*      */         
/* 1337 */         int numberOfBytesPerChar = 1;
/*      */         
/* 1339 */         String connectionEncoding = locallyScopedConn.getEncoding();
/*      */         
/* 1341 */         if (StringUtils.startsWithIgnoreCase(connectionEncoding, "utf")) {
/* 1342 */           numberOfBytesPerChar = 3;
/* 1343 */         } else if (CharsetMapping.isMultibyteCharset(connectionEncoding)) {
/* 1344 */           numberOfBytesPerChar = 2;
/*      */         }
/*      */         
/* 1347 */         int escapeAdjust = 1;
/*      */         
/* 1349 */         batchStmt.setEscapeProcessing(this.doEscapeProcessing);
/*      */         
/* 1351 */         if (this.doEscapeProcessing)
/*      */         {
/* 1353 */           escapeAdjust = 2;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1358 */         SQLException sqlEx = null;
/*      */         
/* 1360 */         int argumentSetsInBatchSoFar = 0;
/*      */         
/* 1362 */         for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/* 1363 */           String nextQuery = (String)this.batchedArgs.get(commandIndex);
/*      */           
/* 1365 */           if (((queryBuf.length() + nextQuery.length()) * numberOfBytesPerChar + 1 + 4) * escapeAdjust + 32 > this.connection.getMaxAllowedPacket())
/*      */           {
/*      */ 
/*      */             try
/*      */             {
/* 1370 */               batchStmt.execute(queryBuf.toString(), 1);
/*      */             } catch (SQLException ex) {
/* 1372 */               sqlEx = handleExceptionForBatch(commandIndex, argumentSetsInBatchSoFar, updateCounts, ex);
/*      */             }
/*      */             
/*      */ 
/* 1376 */             counter = processMultiCountsAndKeys((StatementImpl)batchStmt, counter, updateCounts);
/*      */             
/*      */ 
/* 1379 */             queryBuf = new StringBuffer();
/* 1380 */             argumentSetsInBatchSoFar = 0;
/*      */           }
/*      */           
/* 1383 */           queryBuf.append(nextQuery);
/* 1384 */           queryBuf.append(";");
/* 1385 */           argumentSetsInBatchSoFar++;
/*      */         }
/*      */         
/* 1388 */         if (queryBuf.length() > 0) {
/*      */           try {
/* 1390 */             batchStmt.execute(queryBuf.toString(), 1);
/*      */           } catch (SQLException ex) {
/* 1392 */             sqlEx = handleExceptionForBatch(commandIndex - 1, argumentSetsInBatchSoFar, updateCounts, ex);
/*      */           }
/*      */           
/*      */ 
/* 1396 */           counter = processMultiCountsAndKeys((StatementImpl)batchStmt, counter, updateCounts);
/*      */         }
/*      */         
/*      */ 
/* 1400 */         if (timeoutTask != null) {
/* 1401 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1402 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1405 */           timeoutTask.cancel();
/*      */           
/* 1407 */           locallyScopedConn.getCancelTimer().purge();
/*      */           
/* 1409 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1412 */         if (sqlEx != null) {
/* 1413 */           throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1418 */         ex = updateCounts != null ? updateCounts : new int[0];jsr 17;return ex;
/*      */       } finally {
/* 1420 */         jsr 6; } localObject2 = returnAddress; if (timeoutTask != null) {
/* 1421 */         timeoutTask.cancel();
/*      */         
/* 1423 */         locallyScopedConn.getCancelTimer().purge();
/*      */       }
/*      */       
/* 1426 */       resetCancelledState();
/*      */       try
/*      */       {
/* 1429 */         if (batchStmt != null) {
/* 1430 */           batchStmt.close();
/*      */         }
/*      */       } finally {
/* 1433 */         if (!multiQueriesEnabled)
/* 1434 */           locallyScopedConn.getIO().disableMultiQueries(); } } ret;
/*      */     
/*      */ 
/*      */ 
/* 1438 */     localObject5 = finally;throw ((Throwable)localObject5);
/*      */   }
/*      */   
/*      */   protected int processMultiCountsAndKeys(StatementImpl batchedStatement, int updateCountCounter, int[] updateCounts)
/*      */     throws SQLException
/*      */   {
/* 1444 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1445 */       updateCounts[(updateCountCounter++)] = batchedStatement.getUpdateCount();
/*      */       
/* 1447 */       boolean doGenKeys = this.batchedGeneratedKeys != null;
/*      */       
/* 1449 */       byte[][] row = (byte[][])null;
/*      */       
/* 1451 */       if (doGenKeys) {
/* 1452 */         long generatedKey = batchedStatement.getLastInsertID();
/*      */         
/* 1454 */         row = new byte[1][];
/* 1455 */         row[0] = StringUtils.getBytes(Long.toString(generatedKey));
/* 1456 */         this.batchedGeneratedKeys.add(new ByteArrayRow(row, getExceptionInterceptor()));
/*      */       }
/*      */       
/*      */ 
/* 1460 */       while ((batchedStatement.getMoreResults()) || (batchedStatement.getUpdateCount() != -1)) {
/* 1461 */         updateCounts[(updateCountCounter++)] = batchedStatement.getUpdateCount();
/*      */         
/* 1463 */         if (doGenKeys) {
/* 1464 */           long generatedKey = batchedStatement.getLastInsertID();
/*      */           
/* 1466 */           row = new byte[1][];
/* 1467 */           row[0] = StringUtils.getBytes(Long.toString(generatedKey));
/* 1468 */           this.batchedGeneratedKeys.add(new ByteArrayRow(row, getExceptionInterceptor()));
/*      */         }
/*      */       }
/*      */       
/* 1472 */       return updateCountCounter;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SQLException handleExceptionForBatch(int endOfBatchIndex, int numValuesPerBatch, int[] updateCounts, SQLException ex)
/*      */     throws BatchUpdateException
/*      */   {
/* 1481 */     for (int j = endOfBatchIndex; j > endOfBatchIndex - numValuesPerBatch; j--) {
/* 1482 */       updateCounts[j] = -3;
/*      */     }
/*      */     SQLException sqlEx;
/* 1485 */     if ((this.continueBatchOnError) && (!(ex instanceof MySQLTimeoutException)) && (!(ex instanceof MySQLStatementCancelledException)) && (!hasDeadlockOrTimeoutRolledBackTx(ex)))
/*      */     {
/*      */ 
/*      */ 
/* 1489 */       sqlEx = ex;
/*      */     } else {
/* 1491 */       int[] newUpdateCounts = new int[endOfBatchIndex];
/* 1492 */       System.arraycopy(updateCounts, 0, newUpdateCounts, 0, endOfBatchIndex);
/*      */       
/*      */ 
/* 1495 */       BatchUpdateException batchException = new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */       
/*      */ 
/* 1498 */       batchException.initCause(ex);
/* 1499 */       throw batchException;
/*      */     }
/*      */     SQLException sqlEx;
/* 1502 */     return sqlEx;
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
/*      */   public ResultSet executeQuery(String sql)
/*      */     throws SQLException
/*      */   {
/* 1518 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1519 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1521 */       this.retrieveGeneratedKeys = false;
/*      */       
/* 1523 */       resetCancelledState();
/*      */       
/* 1525 */       checkNullOrEmptyQuery(sql);
/*      */       
/* 1527 */       boolean doStreaming = createStreamingResultSet();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1537 */       if ((doStreaming) && (this.connection.getNetTimeoutForStreamingResults() > 0))
/*      */       {
/* 1539 */         executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
/*      */       }
/*      */       
/*      */ 
/* 1543 */       if (this.doEscapeProcessing) {
/* 1544 */         Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), this.connection);
/*      */         
/*      */ 
/* 1547 */         if ((escapedSqlResult instanceof String)) {
/* 1548 */           sql = (String)escapedSqlResult;
/*      */         } else {
/* 1550 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/* 1554 */       char firstStatementChar = StringUtils.firstNonWsCharUc(sql, findStartOfStatement(sql));
/*      */       
/*      */ 
/* 1557 */       if ((sql.charAt(0) == '/') && 
/* 1558 */         (sql.startsWith("/* ping */"))) {
/* 1559 */         doPingInstead();
/*      */         
/* 1561 */         return this.results;
/*      */       }
/*      */       
/*      */ 
/* 1565 */       checkForDml(sql, firstStatementChar);
/*      */       
/* 1567 */       implicitlyCloseAllOpenResults();
/*      */       
/* 1569 */       CachedResultSetMetaData cachedMetaData = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1578 */       if (useServerFetch()) {
/* 1579 */         this.results = createResultSetUsingServerFetch(sql);
/*      */         
/* 1581 */         return this.results;
/*      */       }
/*      */       
/* 1584 */       CancelTask timeoutTask = null;
/*      */       
/* 1586 */       String oldCatalog = null;
/*      */       try
/*      */       {
/* 1589 */         if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1592 */           timeoutTask = new CancelTask(this);
/* 1593 */           locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1597 */         if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1598 */           oldCatalog = locallyScopedConn.getCatalog();
/* 1599 */           locallyScopedConn.setCatalog(this.currentCatalog);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1606 */         Field[] cachedFields = null;
/*      */         
/* 1608 */         if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1609 */           cachedMetaData = locallyScopedConn.getCachedMetaData(sql);
/*      */           
/* 1611 */           if (cachedMetaData != null) {
/* 1612 */             cachedFields = cachedMetaData.fields;
/*      */           }
/*      */         }
/*      */         
/* 1616 */         if (locallyScopedConn.useMaxRows())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1621 */           if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) {
/* 1622 */             this.results = locallyScopedConn.execSQL(this, sql, this.maxRows, null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1628 */             if (this.maxRows <= 0) {
/* 1629 */               executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */             }
/*      */             else {
/* 1632 */               executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows);
/*      */             }
/*      */             
/*      */ 
/* 1636 */             statementBegins();
/*      */             
/* 1638 */             this.results = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1644 */             if (oldCatalog != null) {
/* 1645 */               locallyScopedConn.setCatalog(oldCatalog);
/*      */             }
/*      */           }
/*      */         } else {
/* 1649 */           statementBegins();
/*      */           
/* 1651 */           this.results = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1657 */         if (timeoutTask != null) {
/* 1658 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1659 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1662 */           timeoutTask.cancel();
/*      */           
/* 1664 */           locallyScopedConn.getCancelTimer().purge();
/*      */           
/* 1666 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1669 */         synchronized (this.cancelTimeoutMutex) {
/* 1670 */           if (this.wasCancelled) {
/* 1671 */             SQLException cause = null;
/*      */             
/* 1673 */             if (this.wasCancelledByTimeout) {
/* 1674 */               cause = new MySQLTimeoutException();
/*      */             } else {
/* 1676 */               cause = new MySQLStatementCancelledException();
/*      */             }
/*      */             
/* 1679 */             resetCancelledState();
/*      */             
/* 1681 */             throw cause;
/*      */           }
/*      */         }
/*      */       } finally {
/* 1685 */         this.statementExecuting.set(false);
/*      */         
/* 1687 */         if (timeoutTask != null) {
/* 1688 */           timeoutTask.cancel();
/*      */           
/* 1690 */           locallyScopedConn.getCancelTimer().purge();
/*      */         }
/*      */         
/* 1693 */         if (oldCatalog != null) {
/* 1694 */           locallyScopedConn.setCatalog(oldCatalog);
/*      */         }
/*      */       }
/*      */       
/* 1698 */       this.lastInsertId = this.results.getUpdateID();
/*      */       
/* 1700 */       if (cachedMetaData != null) {
/* 1701 */         locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results);
/*      */ 
/*      */       }
/* 1704 */       else if (this.connection.getCacheResultSetMetadata()) {
/* 1705 */         locallyScopedConn.initializeResultsMetadataFromCache(sql, null, this.results);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1710 */       return this.results;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doPingInstead() throws SQLException {
/* 1715 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1716 */       if (this.pingTarget != null) {
/* 1717 */         this.pingTarget.doPing();
/*      */       } else {
/* 1719 */         this.connection.ping();
/*      */       }
/*      */       
/* 1722 */       ResultSetInternalMethods fakeSelectOneResultSet = generatePingResultSet();
/* 1723 */       this.results = fakeSelectOneResultSet;
/*      */     }
/*      */   }
/*      */   
/*      */   protected ResultSetInternalMethods generatePingResultSet() throws SQLException {
/* 1728 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1729 */       Field[] fields = { new Field(null, "1", -5, 1) };
/* 1730 */       ArrayList<ResultSetRow> rows = new ArrayList();
/* 1731 */       byte[] colVal = { 49 };
/*      */       
/* 1733 */       rows.add(new ByteArrayRow(new byte[][] { colVal }, getExceptionInterceptor()));
/*      */       
/* 1735 */       return (ResultSetInternalMethods)DatabaseMetaData.buildResultSet(fields, rows, this.connection);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void executeSimpleNonQuery(MySQLConnection c, String nonQuery)
/*      */     throws SQLException
/*      */   {
/* 1742 */     c.execSQL(this, nonQuery, -1, null, 1003, 1007, false, this.currentCatalog, null, false).close();
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
/*      */   public int executeUpdate(String sql)
/*      */     throws SQLException
/*      */   {
/* 1764 */     return executeUpdate(sql, false, false);
/*      */   }
/*      */   
/*      */   protected int executeUpdate(String sql, boolean isBatch, boolean returnGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/* 1770 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1771 */       MySQLConnection locallyScopedConn = this.connection;
/*      */       
/* 1773 */       char firstStatementChar = StringUtils.firstAlphaCharUc(sql, findStartOfStatement(sql));
/*      */       
/*      */ 
/* 1776 */       ResultSetInternalMethods rs = null;
/*      */       
/* 1778 */       this.retrieveGeneratedKeys = returnGeneratedKeys;
/*      */       
/* 1780 */       resetCancelledState();
/*      */       
/* 1782 */       checkNullOrEmptyQuery(sql);
/*      */       
/* 1784 */       if (this.doEscapeProcessing) {
/* 1785 */         Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, this.connection.serverSupportsConvertFn(), this.connection);
/*      */         
/*      */ 
/* 1788 */         if ((escapedSqlResult instanceof String)) {
/* 1789 */           sql = (String)escapedSqlResult;
/*      */         } else {
/* 1791 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/* 1795 */       if (locallyScopedConn.isReadOnly(false)) {
/* 1796 */         throw SQLError.createSQLException(Messages.getString("Statement.42") + Messages.getString("Statement.43"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1802 */       if (StringUtils.startsWithIgnoreCaseAndWs(sql, "select")) {
/* 1803 */         throw SQLError.createSQLException(Messages.getString("Statement.46"), "01S03", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1808 */       implicitlyCloseAllOpenResults();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1814 */       CancelTask timeoutTask = null;
/*      */       
/* 1816 */       String oldCatalog = null;
/*      */       try
/*      */       {
/* 1819 */         if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1822 */           timeoutTask = new CancelTask(this);
/* 1823 */           locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1827 */         if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1828 */           oldCatalog = locallyScopedConn.getCatalog();
/* 1829 */           locallyScopedConn.setCatalog(this.currentCatalog);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1835 */         if (locallyScopedConn.useMaxRows()) {
/* 1836 */           executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");
/*      */         }
/*      */         
/*      */ 
/* 1840 */         statementBegins();
/*      */         
/* 1842 */         rs = locallyScopedConn.execSQL(this, sql, -1, null, 1003, 1007, false, this.currentCatalog, null, isBatch);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1849 */         if (timeoutTask != null) {
/* 1850 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1851 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1854 */           timeoutTask.cancel();
/*      */           
/* 1856 */           locallyScopedConn.getCancelTimer().purge();
/*      */           
/* 1858 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1861 */         synchronized (this.cancelTimeoutMutex) {
/* 1862 */           if (this.wasCancelled) {
/* 1863 */             SQLException cause = null;
/*      */             
/* 1865 */             if (this.wasCancelledByTimeout) {
/* 1866 */               cause = new MySQLTimeoutException();
/*      */             } else {
/* 1868 */               cause = new MySQLStatementCancelledException();
/*      */             }
/*      */             
/* 1871 */             resetCancelledState();
/*      */             
/* 1873 */             throw cause;
/*      */           }
/*      */         }
/*      */       } finally {
/* 1877 */         if (timeoutTask != null) {
/* 1878 */           timeoutTask.cancel();
/*      */           
/* 1880 */           locallyScopedConn.getCancelTimer().purge();
/*      */         }
/*      */         
/* 1883 */         if (oldCatalog != null) {
/* 1884 */           locallyScopedConn.setCatalog(oldCatalog);
/*      */         }
/*      */         
/* 1887 */         if (!isBatch) {
/* 1888 */           this.statementExecuting.set(false);
/*      */         }
/*      */       }
/*      */       
/* 1892 */       this.results = rs;
/*      */       
/* 1894 */       rs.setFirstCharOfQuery(firstStatementChar);
/*      */       
/* 1896 */       this.updateCount = rs.getUpdateCount();
/*      */       
/* 1898 */       int truncatedUpdateCount = 0;
/*      */       
/* 1900 */       if (this.updateCount > 2147483647L) {
/* 1901 */         truncatedUpdateCount = Integer.MAX_VALUE;
/*      */       } else {
/* 1903 */         truncatedUpdateCount = (int)this.updateCount;
/*      */       }
/*      */       
/* 1906 */       this.lastInsertId = rs.getUpdateID();
/*      */       
/* 1908 */       return truncatedUpdateCount;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, int returnGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/* 1918 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1919 */       if (returnGeneratedKeys == 1) {
/* 1920 */         MySQLConnection locallyScopedConn = this.connection;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1925 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/* 1927 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1930 */           int i = executeUpdate(sql, false, true);jsr 16;return i;
/*      */         } finally {
/* 1932 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */       
/*      */ 
/* 1936 */       return executeUpdate(sql);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, int[] generatedKeyIndices)
/*      */     throws SQLException
/*      */   {
/* 1945 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1946 */       if ((generatedKeyIndices != null) && (generatedKeyIndices.length > 0)) {
/* 1947 */         checkClosed();
/*      */         
/* 1949 */         MySQLConnection locallyScopedConn = this.connection;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1954 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/* 1956 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1959 */           int i = executeUpdate(sql, false, true);jsr 16;return i;
/*      */         } finally {
/* 1961 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */       
/*      */ 
/* 1965 */       return executeUpdate(sql);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, String[] generatedKeyNames)
/*      */     throws SQLException
/*      */   {
/* 1974 */     synchronized (checkClosed().getConnectionMutex()) {
/* 1975 */       if ((generatedKeyNames != null) && (generatedKeyNames.length > 0)) {
/* 1976 */         MySQLConnection locallyScopedConn = this.connection;
/*      */         
/*      */ 
/*      */ 
/* 1980 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/* 1982 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1985 */           int i = executeUpdate(sql, false, true);jsr 16;return i;
/*      */         } finally {
/* 1987 */           jsr 6; } localObject2 = returnAddress;locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);ret;
/*      */       }
/*      */       
/*      */ 
/* 1991 */       return executeUpdate(sql);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Calendar getCalendarInstanceForSessionOrNew()
/*      */     throws SQLException
/*      */   {
/* 2000 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2001 */       if (this.connection != null) {
/* 2002 */         return this.connection.getCalendarInstanceForSessionOrNew();
/*      */       }
/*      */       
/* 2005 */       return new GregorianCalendar();
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public java.sql.Connection getConnection()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 8	com/mysql/jdbc/StatementImpl:connection	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2018	-> byte code offset #0
/*      */     //   Java source line #2019	-> byte code offset #12
/*      */     //   Java source line #2020	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/* 2032 */     return 1000;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 13	com/mysql/jdbc/StatementImpl:fetchSize	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2044	-> byte code offset #0
/*      */     //   Java source line #2045	-> byte code offset #12
/*      */     //   Java source line #2046	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   public ResultSet getGeneratedKeys()
/*      */     throws SQLException
/*      */   {
/* 2059 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2060 */       if (!this.retrieveGeneratedKeys) {
/* 2061 */         throw SQLError.createSQLException(Messages.getString("Statement.GeneratedKeysNotRequested"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/* 2064 */       if (this.batchedGeneratedKeys == null) {
/* 2065 */         if (this.lastQueryIsOnDupKeyUpdate) {
/* 2066 */           return this.generatedKeysResults = getGeneratedKeysInternal(1);
/*      */         }
/* 2068 */         return this.generatedKeysResults = getGeneratedKeysInternal();
/*      */       }
/*      */       
/* 2071 */       Field[] fields = new Field[1];
/* 2072 */       fields[0] = new Field("", "GENERATED_KEY", -5, 17);
/* 2073 */       fields[0].setConnection(this.connection);
/*      */       
/* 2075 */       this.generatedKeysResults = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(this.batchedGeneratedKeys), this.connection, this, false);
/*      */       
/*      */ 
/*      */ 
/* 2079 */       return this.generatedKeysResults;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSetInternalMethods getGeneratedKeysInternal()
/*      */     throws SQLException
/*      */   {
/* 2090 */     int numKeys = getUpdateCount();
/* 2091 */     return getGeneratedKeysInternal(numKeys);
/*      */   }
/*      */   
/*      */   protected ResultSetInternalMethods getGeneratedKeysInternal(int numKeys) throws SQLException
/*      */   {
/* 2096 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2097 */       Field[] fields = new Field[1];
/* 2098 */       fields[0] = new Field("", "GENERATED_KEY", -5, 17);
/* 2099 */       fields[0].setConnection(this.connection);
/* 2100 */       fields[0].setUseOldNameMetadata(true);
/*      */       
/* 2102 */       ArrayList<ResultSetRow> rowSet = new ArrayList();
/*      */       
/* 2104 */       long beginAt = getLastInsertID();
/*      */       
/* 2106 */       if (beginAt < 0L) {
/* 2107 */         fields[0].setUnsigned();
/*      */       }
/*      */       
/* 2110 */       if (this.results != null) {
/* 2111 */         String serverInfo = this.results.getServerInfo();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2117 */         if ((numKeys > 0) && (this.results.getFirstCharOfQuery() == 'R') && (serverInfo != null) && (serverInfo.length() > 0))
/*      */         {
/* 2119 */           numKeys = getRecordCountFromInfo(serverInfo);
/*      */         }
/*      */         
/* 2122 */         if ((beginAt != 0L) && (numKeys > 0)) {
/* 2123 */           for (int i = 0; i < numKeys; i++) {
/* 2124 */             byte[][] row = new byte[1][];
/* 2125 */             if (beginAt > 0L) {
/* 2126 */               row[0] = StringUtils.getBytes(Long.toString(beginAt));
/*      */             } else {
/* 2128 */               byte[] asBytes = new byte[8];
/* 2129 */               asBytes[7] = ((byte)(int)(beginAt & 0xFF));
/* 2130 */               asBytes[6] = ((byte)(int)(beginAt >>> 8));
/* 2131 */               asBytes[5] = ((byte)(int)(beginAt >>> 16));
/* 2132 */               asBytes[4] = ((byte)(int)(beginAt >>> 24));
/* 2133 */               asBytes[3] = ((byte)(int)(beginAt >>> 32));
/* 2134 */               asBytes[2] = ((byte)(int)(beginAt >>> 40));
/* 2135 */               asBytes[1] = ((byte)(int)(beginAt >>> 48));
/* 2136 */               asBytes[0] = ((byte)(int)(beginAt >>> 56));
/*      */               
/* 2138 */               BigInteger val = new BigInteger(1, asBytes);
/*      */               
/* 2140 */               row[0] = val.toString().getBytes();
/*      */             }
/* 2142 */             rowSet.add(new ByteArrayRow(row, getExceptionInterceptor()));
/* 2143 */             beginAt += this.connection.getAutoIncrementIncrement();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2148 */       ResultSetImpl gkRs = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(rowSet), this.connection, this, false);
/*      */       
/*      */ 
/* 2151 */       return gkRs;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getId()
/*      */   {
/* 2161 */     return this.statementId;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public long getLastInsertID()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 17	com/mysql/jdbc/StatementImpl:lastInsertId	J
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: lreturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     //   24: astore_1
/*      */     //   25: new 303	java/lang/RuntimeException
/*      */     //   28: dup
/*      */     //   29: aload_1
/*      */     //   30: invokespecial 304	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   33: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2179	-> byte code offset #0
/*      */     //   Java source line #2180	-> byte code offset #12
/*      */     //   Java source line #2181	-> byte code offset #19
/*      */     //   Java source line #2182	-> byte code offset #24
/*      */     //   Java source line #2183	-> byte code offset #25
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	34	0	this	StatementImpl
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
/*      */   public long getLongUpdateCount()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 27	com/mysql/jdbc/StatementImpl:results	Lcom/mysql/jdbc/ResultSetInternalMethods;
/*      */     //   16: ifnonnull +9 -> 25
/*      */     //   19: ldc2_w 15
/*      */     //   22: aload_1
/*      */     //   23: monitorexit
/*      */     //   24: lreturn
/*      */     //   25: aload_0
/*      */     //   26: getfield 27	com/mysql/jdbc/StatementImpl:results	Lcom/mysql/jdbc/ResultSetInternalMethods;
/*      */     //   29: invokeinterface 144 1 0
/*      */     //   34: ifeq +9 -> 43
/*      */     //   37: ldc2_w 15
/*      */     //   40: aload_1
/*      */     //   41: monitorexit
/*      */     //   42: lreturn
/*      */     //   43: aload_0
/*      */     //   44: getfield 32	com/mysql/jdbc/StatementImpl:updateCount	J
/*      */     //   47: aload_1
/*      */     //   48: monitorexit
/*      */     //   49: lreturn
/*      */     //   50: astore_2
/*      */     //   51: aload_1
/*      */     //   52: monitorexit
/*      */     //   53: aload_2
/*      */     //   54: athrow
/*      */     //   55: astore_1
/*      */     //   56: new 303	java/lang/RuntimeException
/*      */     //   59: dup
/*      */     //   60: aload_1
/*      */     //   61: invokespecial 304	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*      */     //   64: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2201	-> byte code offset #0
/*      */     //   Java source line #2202	-> byte code offset #12
/*      */     //   Java source line #2203	-> byte code offset #19
/*      */     //   Java source line #2206	-> byte code offset #25
/*      */     //   Java source line #2207	-> byte code offset #37
/*      */     //   Java source line #2210	-> byte code offset #43
/*      */     //   Java source line #2211	-> byte code offset #50
/*      */     //   Java source line #2212	-> byte code offset #55
/*      */     //   Java source line #2213	-> byte code offset #56
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	65	0	this	StatementImpl
/*      */     //   55	6	1	e	SQLException
/*      */     //   50	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	24	50	finally
/*      */     //   25	42	50	finally
/*      */     //   43	49	50	finally
/*      */     //   50	53	50	finally
/*      */     //   0	24	55	java/sql/SQLException
/*      */     //   25	42	55	java/sql/SQLException
/*      */     //   43	49	55	java/sql/SQLException
/*      */     //   50	55	55	java/sql/SQLException
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getMaxFieldSize()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 19	com/mysql/jdbc/StatementImpl:maxFieldSize	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2229	-> byte code offset #0
/*      */     //   Java source line #2230	-> byte code offset #12
/*      */     //   Java source line #2231	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   public int getMaxRows()
/*      */     throws SQLException
/*      */   {
/* 2245 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2246 */       if (this.maxRows <= 0) {
/* 2247 */         return 0;
/*      */       }
/*      */       
/* 2250 */       return this.maxRows;
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
/*      */   public boolean getMoreResults()
/*      */     throws SQLException
/*      */   {
/* 2264 */     return getMoreResults(1);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getMoreResults(int current)
/*      */     throws SQLException
/*      */   {
/* 2271 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2272 */       if (this.results == null) {
/* 2273 */         return false;
/*      */       }
/*      */       
/* 2276 */       boolean streamingMode = createStreamingResultSet();
/*      */       
/* 2278 */       while ((streamingMode) && 
/* 2279 */         (this.results.reallyResult()) && 
/* 2280 */         (this.results.next())) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2285 */       ResultSetInternalMethods nextResultSet = this.results.getNextResultSet();
/*      */       
/* 2287 */       switch (current)
/*      */       {
/*      */       case 1: 
/* 2290 */         if (this.results != null) {
/* 2291 */           if ((!streamingMode) && (!this.connection.getDontTrackOpenResources())) {
/* 2292 */             this.results.realClose(false);
/*      */           }
/*      */           
/* 2295 */           this.results.clearNextResult();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         break;
/*      */       case 3: 
/* 2302 */         if (this.results != null) {
/* 2303 */           if ((!streamingMode) && (!this.connection.getDontTrackOpenResources())) {
/* 2304 */             this.results.realClose(false);
/*      */           }
/*      */           
/* 2307 */           this.results.clearNextResult();
/*      */         }
/*      */         
/* 2310 */         closeAllOpenResults();
/*      */         
/* 2312 */         break;
/*      */       
/*      */       case 2: 
/* 2315 */         if (!this.connection.getDontTrackOpenResources()) {
/* 2316 */           this.openResults.add(this.results);
/*      */         }
/*      */         
/* 2319 */         this.results.clearNextResult();
/*      */         
/* 2321 */         break;
/*      */       
/*      */       default: 
/* 2324 */         throw SQLError.createSQLException(Messages.getString("Statement.19"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 2329 */       this.results = nextResultSet;
/*      */       
/* 2331 */       if (this.results == null) {
/* 2332 */         this.updateCount = -1L;
/* 2333 */         this.lastInsertId = -1L;
/* 2334 */       } else if (this.results.reallyResult()) {
/* 2335 */         this.updateCount = -1L;
/* 2336 */         this.lastInsertId = -1L;
/*      */       } else {
/* 2338 */         this.updateCount = this.results.getUpdateCount();
/* 2339 */         this.lastInsertId = this.results.getUpdateID();
/*      */       }
/*      */       
/* 2342 */       boolean moreResults = (this.results != null) && (this.results.reallyResult());
/* 2343 */       if (!moreResults)
/* 2344 */         checkAndPerformCloseOnCompletionAction();
/* 2345 */       return moreResults;
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getQueryTimeout()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 31	com/mysql/jdbc/StatementImpl:timeoutInMillis	I
/*      */     //   16: sipush 1000
/*      */     //   19: idiv
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: ireturn
/*      */     //   23: astore_2
/*      */     //   24: aload_1
/*      */     //   25: monitorexit
/*      */     //   26: aload_2
/*      */     //   27: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2360	-> byte code offset #0
/*      */     //   Java source line #2361	-> byte code offset #12
/*      */     //   Java source line #2362	-> byte code offset #23
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	28	0	this	StatementImpl
/*      */     //   10	15	1	Ljava/lang/Object;	Object
/*      */     //   23	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	22	23	finally
/*      */     //   23	26	23	finally
/*      */   }
/*      */   
/*      */   private int getRecordCountFromInfo(String serverInfo)
/*      */   {
/* 2374 */     StringBuffer recordsBuf = new StringBuffer();
/* 2375 */     int recordsCount = 0;
/* 2376 */     int duplicatesCount = 0;
/*      */     
/* 2378 */     char c = '\000';
/*      */     
/* 2380 */     int length = serverInfo.length();
/* 2381 */     for (int i = 0; 
/*      */         
/* 2383 */         i < length; i++) {
/* 2384 */       c = serverInfo.charAt(i);
/*      */       
/* 2386 */       if (Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 2391 */     recordsBuf.append(c);
/* 2392 */     i++;
/* 2394 */     for (; 
/* 2394 */         i < length; i++) {
/* 2395 */       c = serverInfo.charAt(i);
/*      */       
/* 2397 */       if (!Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */       
/* 2401 */       recordsBuf.append(c);
/*      */     }
/*      */     
/* 2404 */     recordsCount = Integer.parseInt(recordsBuf.toString());
/*      */     
/* 2406 */     StringBuffer duplicatesBuf = new StringBuffer();
/* 2408 */     for (; 
/* 2408 */         i < length; i++) {
/* 2409 */       c = serverInfo.charAt(i);
/*      */       
/* 2411 */       if (Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 2416 */     duplicatesBuf.append(c);
/* 2417 */     i++;
/* 2419 */     for (; 
/* 2419 */         i < length; i++) {
/* 2420 */       c = serverInfo.charAt(i);
/*      */       
/* 2422 */       if (!Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */       
/* 2426 */       duplicatesBuf.append(c);
/*      */     }
/*      */     
/* 2429 */     duplicatesCount = Integer.parseInt(duplicatesBuf.toString());
/*      */     
/* 2431 */     return recordsCount - duplicatesCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getResultSet()
/*      */     throws SQLException
/*      */   {
/* 2444 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2445 */       return (this.results != null) && (this.results.reallyResult()) ? this.results : null;
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public int getResultSetConcurrency()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 29	com/mysql/jdbc/StatementImpl:resultSetConcurrency	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2459	-> byte code offset #0
/*      */     //   Java source line #2460	-> byte code offset #12
/*      */     //   Java source line #2461	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   public int getResultSetHoldability()
/*      */     throws SQLException
/*      */   {
/* 2468 */     return 1;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected ResultSetInternalMethods getResultSetInternal()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 27	com/mysql/jdbc/StatementImpl:results	Lcom/mysql/jdbc/ResultSetInternalMethods;
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     //   24: astore_1
/*      */     //   25: aload_0
/*      */     //   26: getfield 27	com/mysql/jdbc/StatementImpl:results	Lcom/mysql/jdbc/ResultSetInternalMethods;
/*      */     //   29: areturn
/*      */     // Line number table:
/*      */     //   Java source line #2473	-> byte code offset #0
/*      */     //   Java source line #2474	-> byte code offset #12
/*      */     //   Java source line #2475	-> byte code offset #19
/*      */     //   Java source line #2476	-> byte code offset #24
/*      */     //   Java source line #2477	-> byte code offset #25
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	30	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   24	2	1	e	SQLException
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
/*      */   public int getResultSetType()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 30	com/mysql/jdbc/StatementImpl:resultSetType	I
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2490	-> byte code offset #0
/*      */     //   Java source line #2491	-> byte code offset #12
/*      */     //   Java source line #2492	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   public int getUpdateCount()
/*      */     throws SQLException
/*      */   {
/* 2506 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2507 */       if (this.results == null) {
/* 2508 */         return -1;
/*      */       }
/*      */       
/* 2511 */       if (this.results.reallyResult()) {
/* 2512 */         return -1;
/*      */       }
/*      */       
/* 2515 */       int truncatedUpdateCount = 0;
/*      */       
/* 2517 */       if (this.results.getUpdateCount() > 2147483647L) {
/* 2518 */         truncatedUpdateCount = Integer.MAX_VALUE;
/*      */       } else {
/* 2520 */         truncatedUpdateCount = (int)this.results.getUpdateCount();
/*      */       }
/*      */       
/* 2523 */       return truncatedUpdateCount;
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
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 2549 */     synchronized (checkClosed().getConnectionMutex())
/*      */     {
/* 2551 */       if (this.clearWarningsCalled) {
/* 2552 */         return null;
/*      */       }
/*      */       
/* 2555 */       if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 2556 */         SQLWarning pendingWarningsFromServer = SQLError.convertShowWarningsToSQLWarnings(this.connection);
/*      */         
/*      */ 
/* 2559 */         if (this.warningChain != null) {
/* 2560 */           this.warningChain.setNextWarning(pendingWarningsFromServer);
/*      */         } else {
/* 2562 */           this.warningChain = pendingWarningsFromServer;
/*      */         }
/*      */         
/* 2565 */         return this.warningChain;
/*      */       }
/*      */       
/* 2568 */       return this.warningChain;
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
/* 2586 */       locallyScopedConn = checkClosed();
/*      */     } catch (SQLException sqlEx) {
/* 2588 */       return;
/*      */     }
/*      */     
/* 2591 */     synchronized (locallyScopedConn.getConnectionMutex())
/*      */     {
/* 2593 */       if ((this.useUsageAdvisor) && 
/* 2594 */         (!calledExplicitly)) {
/* 2595 */         String message = Messages.getString("Statement.63") + Messages.getString("Statement.64");
/*      */         
/*      */ 
/* 2598 */         this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2608 */       if (closeOpenResults) {
/* 2609 */         closeOpenResults = (!this.holdResultsOpenOverClose) && (!this.connection.getDontTrackOpenResources());
/*      */       }
/*      */       
/*      */ 
/* 2613 */       if (closeOpenResults) {
/* 2614 */         if (this.results != null) {
/*      */           try
/*      */           {
/* 2617 */             this.results.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */         }
/*      */         
/*      */ 
/* 2623 */         if (this.generatedKeysResults != null) {
/*      */           try
/*      */           {
/* 2626 */             this.generatedKeysResults.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */         }
/*      */         
/*      */ 
/* 2632 */         closeAllOpenResults();
/*      */       }
/*      */       
/* 2635 */       if (this.connection != null) {
/* 2636 */         if (this.maxRowsChanged) {
/* 2637 */           this.connection.unsetMaxRows(this);
/*      */         }
/*      */         
/* 2640 */         if (!this.connection.getDontTrackOpenResources()) {
/* 2641 */           this.connection.unregisterStatement(this);
/*      */         }
/*      */       }
/*      */       
/* 2645 */       this.isClosed = true;
/*      */       
/* 2647 */       this.results = null;
/* 2648 */       this.generatedKeysResults = null;
/* 2649 */       this.connection = null;
/* 2650 */       this.warningChain = null;
/* 2651 */       this.openResults = null;
/* 2652 */       this.batchedGeneratedKeys = null;
/* 2653 */       this.localInfileInputStream = null;
/* 2654 */       this.pingTarget = null;
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
/*      */   public void setCursorName(String name)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEscapeProcessing(boolean enable)
/*      */     throws SQLException
/*      */   {
/* 2691 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2692 */       this.doEscapeProcessing = enable;
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
/*      */   public void setFetchDirection(int direction)
/*      */     throws SQLException
/*      */   {
/* 2710 */     switch (direction)
/*      */     {
/*      */     case 1000: 
/*      */     case 1001: 
/*      */     case 1002: 
/*      */       break;
/*      */     default: 
/* 2717 */       throw SQLError.createSQLException(Messages.getString("Statement.5"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
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
/*      */   public void setFetchSize(int rows)
/*      */     throws SQLException
/*      */   {
/* 2738 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2739 */       if (((rows < 0) && (rows != Integer.MIN_VALUE)) || ((this.maxRows != 0) && (this.maxRows != -1) && (rows > getMaxRows())))
/*      */       {
/*      */ 
/* 2742 */         throw SQLError.createSQLException(Messages.getString("Statement.7"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2747 */       this.fetchSize = rows;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setHoldResultsOpenOverClose(boolean holdResultsOpenOverClose) {
/*      */     try {
/* 2753 */       synchronized (checkClosed().getConnectionMutex()) {
/* 2754 */         this.holdResultsOpenOverClose = holdResultsOpenOverClose;
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {}
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
/*      */   public void setMaxFieldSize(int max)
/*      */     throws SQLException
/*      */   {
/* 2771 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2772 */       if (max < 0) {
/* 2773 */         throw SQLError.createSQLException(Messages.getString("Statement.11"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2778 */       int maxBuf = this.connection != null ? this.connection.getMaxAllowedPacket() : MysqlIO.getMaxBuf();
/*      */       
/*      */ 
/* 2781 */       if (max > maxBuf) {
/* 2782 */         throw SQLError.createSQLException(Messages.getString("Statement.13", new Object[] { Long.valueOf(maxBuf) }), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2788 */       this.maxFieldSize = max;
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
/*      */   public void setMaxRows(int max)
/*      */     throws SQLException
/*      */   {
/* 2804 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2805 */       if ((max > 50000000) || (max < 0)) {
/* 2806 */         throw SQLError.createSQLException(Messages.getString("Statement.15") + max + " > " + 50000000 + ".", "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2813 */       if (max == 0) {
/* 2814 */         max = -1;
/*      */       }
/*      */       
/* 2817 */       this.maxRows = max;
/* 2818 */       this.maxRowsChanged = true;
/*      */       
/* 2820 */       if (this.maxRows == -1) {
/* 2821 */         this.connection.unsetMaxRows(this);
/* 2822 */         this.maxRowsChanged = false;
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 2829 */         this.connection.maxRowsChanged(this);
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
/*      */   public void setQueryTimeout(int seconds)
/*      */     throws SQLException
/*      */   {
/* 2844 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2845 */       if (seconds < 0) {
/* 2846 */         throw SQLError.createSQLException(Messages.getString("Statement.21"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2851 */       this.timeoutInMillis = (seconds * 1000);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/*      */     try
/*      */     {
/* 2863 */       synchronized (checkClosed().getConnectionMutex()) {
/* 2864 */         this.resultSetConcurrency = concurrencyFlag;
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetType(int typeFlag)
/*      */   {
/*      */     try
/*      */     {
/* 2880 */       synchronized (checkClosed().getConnectionMutex()) {
/* 2881 */         this.resultSetType = typeFlag;
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {}
/*      */   }
/*      */   
/*      */   protected void getBatchedGeneratedKeys(java.sql.Statement batchedStatement)
/*      */     throws SQLException
/*      */   {
/* 2890 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2891 */       if (this.retrieveGeneratedKeys) {
/* 2892 */         ResultSet rs = null;
/*      */         try
/*      */         {
/* 2895 */           rs = batchedStatement.getGeneratedKeys();
/*      */           
/* 2897 */           while (rs.next()) {
/* 2898 */             this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor()));
/*      */           }
/*      */         }
/*      */         finally {
/* 2902 */           if (rs != null) {
/* 2903 */             rs.close();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void getBatchedGeneratedKeys(int maxKeys) throws SQLException {
/* 2911 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2912 */       if (this.retrieveGeneratedKeys) {
/* 2913 */         ResultSet rs = null;
/*      */         try
/*      */         {
/* 2916 */           if (maxKeys == 0) {
/* 2917 */             rs = getGeneratedKeysInternal();
/*      */           } else {
/* 2919 */             rs = getGeneratedKeysInternal(maxKeys);
/*      */           }
/* 2921 */           while (rs.next()) {
/* 2922 */             this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor()));
/*      */           }
/*      */         }
/*      */         finally {
/* 2926 */           this.isImplicitlyClosingResults = true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2932 */         ret;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean useServerFetch()
/*      */     throws SQLException
/*      */   {
/* 2943 */     synchronized (checkClosed().getConnectionMutex()) {
/* 2944 */       return (this.connection.isCursorFetchEnabled()) && (this.fetchSize > 0) && (this.resultSetConcurrency == 1007) && (this.resultSetType == 1003);
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
/* 2964 */   private boolean isPoolable = true;
/*      */   private InputStream localInfileInputStream;
/*      */   protected final boolean version5013OrNewer;
/*      */   
/*      */   /* Error */
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 14	com/mysql/jdbc/StatementImpl:isClosed	Z
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     //   24: astore_1
/*      */     //   25: ldc 53
/*      */     //   27: aload_1
/*      */     //   28: invokevirtual 128	java/sql/SQLException:getSQLState	()Ljava/lang/String;
/*      */     //   31: invokevirtual 129	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   34: ifeq +5 -> 39
/*      */     //   37: iconst_1
/*      */     //   38: ireturn
/*      */     //   39: aload_1
/*      */     //   40: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2952	-> byte code offset #0
/*      */     //   Java source line #2953	-> byte code offset #12
/*      */     //   Java source line #2954	-> byte code offset #19
/*      */     //   Java source line #2955	-> byte code offset #24
/*      */     //   Java source line #2956	-> byte code offset #25
/*      */     //   Java source line #2957	-> byte code offset #37
/*      */     //   Java source line #2960	-> byte code offset #39
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	41	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   24	16	1	sqlEx	SQLException
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */     //   0	18	24	java/sql/SQLException
/*      */     //   19	24	24	java/sql/SQLException
/*      */   }
/*      */   
/*      */   public boolean isPoolable()
/*      */     throws SQLException
/*      */   {
/* 2967 */     return this.isPoolable;
/*      */   }
/*      */   
/*      */   public void setPoolable(boolean poolable) throws SQLException {
/* 2971 */     this.isPoolable = poolable;
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
/*      */   public boolean isWrapperFor(Class<?> iface)
/*      */     throws SQLException
/*      */   {
/* 2990 */     checkClosed();
/*      */     
/*      */ 
/*      */ 
/* 2994 */     return iface.isInstance(this);
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
/*      */   public Object unwrap(Class<?> iface)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3015 */       return Util.cast(iface, this);
/*      */     } catch (ClassCastException cce) {
/* 3017 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   protected int findStartOfStatement(String sql)
/*      */   {
/* 3023 */     int statementStartPos = 0;
/*      */     
/* 3025 */     if (StringUtils.startsWithIgnoreCaseAndWs(sql, "/*")) {
/* 3026 */       statementStartPos = sql.indexOf("*/");
/*      */       
/* 3028 */       if (statementStartPos == -1) {
/* 3029 */         statementStartPos = 0;
/*      */       } else {
/* 3031 */         statementStartPos += 2;
/*      */       }
/* 3033 */     } else if ((StringUtils.startsWithIgnoreCaseAndWs(sql, "--")) || (StringUtils.startsWithIgnoreCaseAndWs(sql, "#")))
/*      */     {
/* 3035 */       statementStartPos = sql.indexOf('\n');
/*      */       
/* 3037 */       if (statementStartPos == -1) {
/* 3038 */         statementStartPos = sql.indexOf('\r');
/*      */         
/* 3040 */         if (statementStartPos == -1) {
/* 3041 */           statementStartPos = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3046 */     return statementStartPos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public InputStream getLocalInfileInputStream()
/*      */   {
/* 3054 */     return this.localInfileInputStream;
/*      */   }
/*      */   
/*      */   public void setLocalInfileInputStream(InputStream stream) {
/* 3058 */     this.localInfileInputStream = stream;
/*      */   }
/*      */   
/*      */   public void setPingTarget(PingTarget pingTarget) {
/* 3062 */     this.pingTarget = pingTarget;
/*      */   }
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor() {
/* 3066 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   protected boolean containsOnDuplicateKeyInString(String sql) {
/* 3070 */     return getOnDuplicateKeyLocation(sql) != -1;
/*      */   }
/*      */   
/*      */   protected int getOnDuplicateKeyLocation(String sql) {
/* 3074 */     return StringUtils.indexOfIgnoreCaseRespectMarker(0, sql, "ON DUPLICATE KEY UPDATE ", "\"'`", "\"'`", !this.connection.isNoBackslashEscapesSet());
/*      */   }
/*      */   
/*      */ 
/* 3078 */   private boolean closeOnCompletion = false;
/*      */   
/*      */   public void closeOnCompletion() throws SQLException {
/* 3081 */     synchronized (checkClosed().getConnectionMutex()) {
/* 3082 */       this.closeOnCompletion = true;
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isCloseOnCompletion()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 86	com/mysql/jdbc/StatementImpl:checkClosed	()Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   4: invokeinterface 87 1 0
/*      */     //   9: dup
/*      */     //   10: astore_1
/*      */     //   11: monitorenter
/*      */     //   12: aload_0
/*      */     //   13: getfield 49	com/mysql/jdbc/StatementImpl:closeOnCompletion	Z
/*      */     //   16: aload_1
/*      */     //   17: monitorexit
/*      */     //   18: ireturn
/*      */     //   19: astore_2
/*      */     //   20: aload_1
/*      */     //   21: monitorexit
/*      */     //   22: aload_2
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3087	-> byte code offset #0
/*      */     //   Java source line #3088	-> byte code offset #12
/*      */     //   Java source line #3089	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	StatementImpl
/*      */     //   10	11	1	Ljava/lang/Object;	Object
/*      */     //   19	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   12	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\StatementImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */