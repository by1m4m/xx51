/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Savepoint;
/*      */ import java.util.Calendar;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
/*      */ import java.util.concurrent.Executor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class LoadBalancedMySQLConnection
/*      */   implements LoadBalancedConnection
/*      */ {
/*      */   protected LoadBalancingConnectionProxy proxy;
/*      */   
/*      */   public LoadBalancingConnectionProxy getProxy()
/*      */   {
/*   47 */     return this.proxy;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected MySQLConnection getActiveMySQLConnection()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 1	com/mysql/jdbc/LoadBalancedMySQLConnection:proxy	Lcom/mysql/jdbc/LoadBalancingConnectionProxy;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 1	com/mysql/jdbc/LoadBalancedMySQLConnection:proxy	Lcom/mysql/jdbc/LoadBalancingConnectionProxy;
/*      */     //   11: getfield 2	com/mysql/jdbc/LoadBalancingConnectionProxy:currentConn	Lcom/mysql/jdbc/MySQLConnection;
/*      */     //   14: aload_1
/*      */     //   15: monitorexit
/*      */     //   16: areturn
/*      */     //   17: astore_2
/*      */     //   18: aload_1
/*      */     //   19: monitorexit
/*      */     //   20: aload_2
/*      */     //   21: athrow
/*      */     // Line number table:
/*      */     //   Java source line #51	-> byte code offset #0
/*      */     //   Java source line #52	-> byte code offset #7
/*      */     //   Java source line #53	-> byte code offset #17
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	22	0	this	LoadBalancedMySQLConnection
/*      */     //   5	14	1	Ljava/lang/Object;	Object
/*      */     //   17	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	16	17	finally
/*      */     //   17	20	17	finally
/*      */   }
/*      */   
/*      */   public LoadBalancedMySQLConnection(LoadBalancingConnectionProxy proxy)
/*      */   {
/*   57 */     this.proxy = proxy;
/*      */   }
/*      */   
/*      */   public void abortInternal() throws SQLException {
/*   61 */     getActiveMySQLConnection().abortInternal();
/*      */   }
/*      */   
/*      */   public void changeUser(String userName, String newPassword) throws SQLException
/*      */   {
/*   66 */     getActiveMySQLConnection().changeUser(userName, newPassword);
/*      */   }
/*      */   
/*      */   public void checkClosed() throws SQLException {
/*   70 */     getActiveMySQLConnection().checkClosed();
/*      */   }
/*      */   
/*      */   public void clearHasTriedMaster() {
/*   74 */     getActiveMySQLConnection().clearHasTriedMaster();
/*      */   }
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*   78 */     getActiveMySQLConnection().clearWarnings();
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*   84 */     return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*   90 */     return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/*   96 */     return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndex);
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/*  102 */     return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/*  108 */     return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyColNames);
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/*  114 */     return getActiveMySQLConnection().clientPrepareStatement(sql);
/*      */   }
/*      */   
/*      */   public void close() throws SQLException {
/*  118 */     getActiveMySQLConnection().close();
/*      */   }
/*      */   
/*      */   public void commit() throws SQLException {
/*  122 */     getActiveMySQLConnection().commit();
/*      */   }
/*      */   
/*      */   public void createNewIO(boolean isForReconnect) throws SQLException {
/*  126 */     getActiveMySQLConnection().createNewIO(isForReconnect);
/*      */   }
/*      */   
/*      */   public java.sql.Statement createStatement() throws SQLException {
/*  130 */     return getActiveMySQLConnection().createStatement();
/*      */   }
/*      */   
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*  136 */     return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*  142 */     return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   public void dumpTestcaseQuery(String query)
/*      */   {
/*  147 */     getActiveMySQLConnection().dumpTestcaseQuery(query);
/*      */   }
/*      */   
/*      */   public Connection duplicate() throws SQLException {
/*  151 */     return getActiveMySQLConnection().duplicate();
/*      */   }
/*      */   
/*      */ 
/*      */   public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/*  158 */     return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, isBatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata)
/*      */     throws SQLException
/*      */   {
/*  167 */     return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition)
/*      */     throws SQLException
/*      */   {
/*  175 */     return getActiveMySQLConnection().extractSqlFromPacket(possibleSqlQuery, queryPacket, endOfQueryPacketPosition);
/*      */   }
/*      */   
/*      */   public String exposeAsXml() throws SQLException
/*      */   {
/*  180 */     return getActiveMySQLConnection().exposeAsXml();
/*      */   }
/*      */   
/*      */   public boolean getAllowLoadLocalInfile() {
/*  184 */     return getActiveMySQLConnection().getAllowLoadLocalInfile();
/*      */   }
/*      */   
/*      */   public boolean getAllowMultiQueries() {
/*  188 */     return getActiveMySQLConnection().getAllowMultiQueries();
/*      */   }
/*      */   
/*      */   public boolean getAllowNanAndInf() {
/*  192 */     return getActiveMySQLConnection().getAllowNanAndInf();
/*      */   }
/*      */   
/*      */   public boolean getAllowUrlInLocalInfile() {
/*  196 */     return getActiveMySQLConnection().getAllowUrlInLocalInfile();
/*      */   }
/*      */   
/*      */   public boolean getAlwaysSendSetIsolation() {
/*  200 */     return getActiveMySQLConnection().getAlwaysSendSetIsolation();
/*      */   }
/*      */   
/*      */   public boolean getAutoClosePStmtStreams() {
/*  204 */     return getActiveMySQLConnection().getAutoClosePStmtStreams();
/*      */   }
/*      */   
/*      */   public boolean getAutoDeserialize() {
/*  208 */     return getActiveMySQLConnection().getAutoDeserialize();
/*      */   }
/*      */   
/*      */   public boolean getAutoGenerateTestcaseScript() {
/*  212 */     return getActiveMySQLConnection().getAutoGenerateTestcaseScript();
/*      */   }
/*      */   
/*      */   public boolean getAutoReconnectForPools() {
/*  216 */     return getActiveMySQLConnection().getAutoReconnectForPools();
/*      */   }
/*      */   
/*      */   public boolean getAutoSlowLog() {
/*  220 */     return getActiveMySQLConnection().getAutoSlowLog();
/*      */   }
/*      */   
/*      */   public int getBlobSendChunkSize() {
/*  224 */     return getActiveMySQLConnection().getBlobSendChunkSize();
/*      */   }
/*      */   
/*      */   public boolean getBlobsAreStrings() {
/*  228 */     return getActiveMySQLConnection().getBlobsAreStrings();
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStatements() {
/*  232 */     return getActiveMySQLConnection().getCacheCallableStatements();
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStmts() {
/*  236 */     return getActiveMySQLConnection().getCacheCallableStmts();
/*      */   }
/*      */   
/*      */   public boolean getCachePrepStmts() {
/*  240 */     return getActiveMySQLConnection().getCachePrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getCachePreparedStatements() {
/*  244 */     return getActiveMySQLConnection().getCachePreparedStatements();
/*      */   }
/*      */   
/*      */   public boolean getCacheResultSetMetadata() {
/*  248 */     return getActiveMySQLConnection().getCacheResultSetMetadata();
/*      */   }
/*      */   
/*      */   public boolean getCacheServerConfiguration() {
/*  252 */     return getActiveMySQLConnection().getCacheServerConfiguration();
/*      */   }
/*      */   
/*      */   public int getCallableStatementCacheSize() {
/*  256 */     return getActiveMySQLConnection().getCallableStatementCacheSize();
/*      */   }
/*      */   
/*      */   public int getCallableStmtCacheSize() {
/*  260 */     return getActiveMySQLConnection().getCallableStmtCacheSize();
/*      */   }
/*      */   
/*      */   public boolean getCapitalizeTypeNames() {
/*  264 */     return getActiveMySQLConnection().getCapitalizeTypeNames();
/*      */   }
/*      */   
/*      */   public String getCharacterSetResults() {
/*  268 */     return getActiveMySQLConnection().getCharacterSetResults();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStorePassword() {
/*  272 */     return getActiveMySQLConnection().getClientCertificateKeyStorePassword();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStoreType()
/*      */   {
/*  277 */     return getActiveMySQLConnection().getClientCertificateKeyStoreType();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStoreUrl() {
/*  281 */     return getActiveMySQLConnection().getClientCertificateKeyStoreUrl();
/*      */   }
/*      */   
/*      */   public String getClientInfoProvider() {
/*  285 */     return getActiveMySQLConnection().getClientInfoProvider();
/*      */   }
/*      */   
/*      */   public String getClobCharacterEncoding() {
/*  289 */     return getActiveMySQLConnection().getClobCharacterEncoding();
/*      */   }
/*      */   
/*      */   public boolean getClobberStreamingResults() {
/*  293 */     return getActiveMySQLConnection().getClobberStreamingResults();
/*      */   }
/*      */   
/*      */   public boolean getCompensateOnDuplicateKeyUpdateCounts() {
/*  297 */     return getActiveMySQLConnection().getCompensateOnDuplicateKeyUpdateCounts();
/*      */   }
/*      */   
/*      */   public int getConnectTimeout()
/*      */   {
/*  302 */     return getActiveMySQLConnection().getConnectTimeout();
/*      */   }
/*      */   
/*      */   public String getConnectionCollation() {
/*  306 */     return getActiveMySQLConnection().getConnectionCollation();
/*      */   }
/*      */   
/*      */   public String getConnectionLifecycleInterceptors() {
/*  310 */     return getActiveMySQLConnection().getConnectionLifecycleInterceptors();
/*      */   }
/*      */   
/*      */   public boolean getContinueBatchOnError() {
/*  314 */     return getActiveMySQLConnection().getContinueBatchOnError();
/*      */   }
/*      */   
/*      */   public boolean getCreateDatabaseIfNotExist() {
/*  318 */     return getActiveMySQLConnection().getCreateDatabaseIfNotExist();
/*      */   }
/*      */   
/*      */   public int getDefaultFetchSize() {
/*  322 */     return getActiveMySQLConnection().getDefaultFetchSize();
/*      */   }
/*      */   
/*      */   public boolean getDontTrackOpenResources() {
/*  326 */     return getActiveMySQLConnection().getDontTrackOpenResources();
/*      */   }
/*      */   
/*      */   public boolean getDumpMetadataOnColumnNotFound() {
/*  330 */     return getActiveMySQLConnection().getDumpMetadataOnColumnNotFound();
/*      */   }
/*      */   
/*      */   public boolean getDumpQueriesOnException() {
/*  334 */     return getActiveMySQLConnection().getDumpQueriesOnException();
/*      */   }
/*      */   
/*      */   public boolean getDynamicCalendars() {
/*  338 */     return getActiveMySQLConnection().getDynamicCalendars();
/*      */   }
/*      */   
/*      */   public boolean getElideSetAutoCommits() {
/*  342 */     return getActiveMySQLConnection().getElideSetAutoCommits();
/*      */   }
/*      */   
/*      */   public boolean getEmptyStringsConvertToZero() {
/*  346 */     return getActiveMySQLConnection().getEmptyStringsConvertToZero();
/*      */   }
/*      */   
/*      */   public boolean getEmulateLocators() {
/*  350 */     return getActiveMySQLConnection().getEmulateLocators();
/*      */   }
/*      */   
/*      */   public boolean getEmulateUnsupportedPstmts() {
/*  354 */     return getActiveMySQLConnection().getEmulateUnsupportedPstmts();
/*      */   }
/*      */   
/*      */   public boolean getEnablePacketDebug() {
/*  358 */     return getActiveMySQLConnection().getEnablePacketDebug();
/*      */   }
/*      */   
/*      */   public boolean getEnableQueryTimeouts() {
/*  362 */     return getActiveMySQLConnection().getEnableQueryTimeouts();
/*      */   }
/*      */   
/*      */   public String getEncoding() {
/*  366 */     return getActiveMySQLConnection().getEncoding();
/*      */   }
/*      */   
/*      */   public String getExceptionInterceptors() {
/*  370 */     return getActiveMySQLConnection().getExceptionInterceptors();
/*      */   }
/*      */   
/*      */   public boolean getExplainSlowQueries() {
/*  374 */     return getActiveMySQLConnection().getExplainSlowQueries();
/*      */   }
/*      */   
/*      */   public boolean getFailOverReadOnly() {
/*  378 */     return getActiveMySQLConnection().getFailOverReadOnly();
/*      */   }
/*      */   
/*      */   public boolean getFunctionsNeverReturnBlobs() {
/*  382 */     return getActiveMySQLConnection().getFunctionsNeverReturnBlobs();
/*      */   }
/*      */   
/*      */   public boolean getGatherPerfMetrics() {
/*  386 */     return getActiveMySQLConnection().getGatherPerfMetrics();
/*      */   }
/*      */   
/*      */   public boolean getGatherPerformanceMetrics() {
/*  390 */     return getActiveMySQLConnection().getGatherPerformanceMetrics();
/*      */   }
/*      */   
/*      */   public boolean getGenerateSimpleParameterMetadata() {
/*  394 */     return getActiveMySQLConnection().getGenerateSimpleParameterMetadata();
/*      */   }
/*      */   
/*      */   public boolean getIgnoreNonTxTables() {
/*  398 */     return getActiveMySQLConnection().getIgnoreNonTxTables();
/*      */   }
/*      */   
/*      */   public boolean getIncludeInnodbStatusInDeadlockExceptions() {
/*  402 */     return getActiveMySQLConnection().getIncludeInnodbStatusInDeadlockExceptions();
/*      */   }
/*      */   
/*      */   public int getInitialTimeout()
/*      */   {
/*  407 */     return getActiveMySQLConnection().getInitialTimeout();
/*      */   }
/*      */   
/*      */   public boolean getInteractiveClient() {
/*  411 */     return getActiveMySQLConnection().getInteractiveClient();
/*      */   }
/*      */   
/*      */   public boolean getIsInteractiveClient() {
/*  415 */     return getActiveMySQLConnection().getIsInteractiveClient();
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncation() {
/*  419 */     return getActiveMySQLConnection().getJdbcCompliantTruncation();
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncationForReads() {
/*  423 */     return getActiveMySQLConnection().getJdbcCompliantTruncationForReads();
/*      */   }
/*      */   
/*      */   public String getLargeRowSizeThreshold() {
/*  427 */     return getActiveMySQLConnection().getLargeRowSizeThreshold();
/*      */   }
/*      */   
/*      */   public int getLoadBalanceBlacklistTimeout() {
/*  431 */     return getActiveMySQLConnection().getLoadBalanceBlacklistTimeout();
/*      */   }
/*      */   
/*      */   public int getLoadBalancePingTimeout() {
/*  435 */     return getActiveMySQLConnection().getLoadBalancePingTimeout();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceStrategy() {
/*  439 */     return getActiveMySQLConnection().getLoadBalanceStrategy();
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceValidateConnectionOnSwapServer() {
/*  443 */     return getActiveMySQLConnection().getLoadBalanceValidateConnectionOnSwapServer();
/*      */   }
/*      */   
/*      */   public String getLocalSocketAddress()
/*      */   {
/*  448 */     return getActiveMySQLConnection().getLocalSocketAddress();
/*      */   }
/*      */   
/*      */   public int getLocatorFetchBufferSize() {
/*  452 */     return getActiveMySQLConnection().getLocatorFetchBufferSize();
/*      */   }
/*      */   
/*      */   public boolean getLogSlowQueries() {
/*  456 */     return getActiveMySQLConnection().getLogSlowQueries();
/*      */   }
/*      */   
/*      */   public boolean getLogXaCommands() {
/*  460 */     return getActiveMySQLConnection().getLogXaCommands();
/*      */   }
/*      */   
/*      */   public String getLogger() {
/*  464 */     return getActiveMySQLConnection().getLogger();
/*      */   }
/*      */   
/*      */   public String getLoggerClassName() {
/*  468 */     return getActiveMySQLConnection().getLoggerClassName();
/*      */   }
/*      */   
/*      */   public boolean getMaintainTimeStats() {
/*  472 */     return getActiveMySQLConnection().getMaintainTimeStats();
/*      */   }
/*      */   
/*      */   public int getMaxAllowedPacket() {
/*  476 */     return getActiveMySQLConnection().getMaxAllowedPacket();
/*      */   }
/*      */   
/*      */   public int getMaxQuerySizeToLog() {
/*  480 */     return getActiveMySQLConnection().getMaxQuerySizeToLog();
/*      */   }
/*      */   
/*      */   public int getMaxReconnects() {
/*  484 */     return getActiveMySQLConnection().getMaxReconnects();
/*      */   }
/*      */   
/*      */   public int getMaxRows() {
/*  488 */     return getActiveMySQLConnection().getMaxRows();
/*      */   }
/*      */   
/*      */   public int getMetadataCacheSize() {
/*  492 */     return getActiveMySQLConnection().getMetadataCacheSize();
/*      */   }
/*      */   
/*      */   public int getNetTimeoutForStreamingResults() {
/*  496 */     return getActiveMySQLConnection().getNetTimeoutForStreamingResults();
/*      */   }
/*      */   
/*      */   public boolean getNoAccessToProcedureBodies() {
/*  500 */     return getActiveMySQLConnection().getNoAccessToProcedureBodies();
/*      */   }
/*      */   
/*      */   public boolean getNoDatetimeStringSync() {
/*  504 */     return getActiveMySQLConnection().getNoDatetimeStringSync();
/*      */   }
/*      */   
/*      */   public boolean getNoTimezoneConversionForTimeType() {
/*  508 */     return getActiveMySQLConnection().getNoTimezoneConversionForTimeType();
/*      */   }
/*      */   
/*      */   public boolean getNullCatalogMeansCurrent() {
/*  512 */     return getActiveMySQLConnection().getNullCatalogMeansCurrent();
/*      */   }
/*      */   
/*      */   public boolean getNullNamePatternMatchesAll() {
/*  516 */     return getActiveMySQLConnection().getNullNamePatternMatchesAll();
/*      */   }
/*      */   
/*      */   public boolean getOverrideSupportsIntegrityEnhancementFacility() {
/*  520 */     return getActiveMySQLConnection().getOverrideSupportsIntegrityEnhancementFacility();
/*      */   }
/*      */   
/*      */   public int getPacketDebugBufferSize()
/*      */   {
/*  525 */     return getActiveMySQLConnection().getPacketDebugBufferSize();
/*      */   }
/*      */   
/*      */   public boolean getPadCharsWithSpace() {
/*  529 */     return getActiveMySQLConnection().getPadCharsWithSpace();
/*      */   }
/*      */   
/*      */   public boolean getParanoid() {
/*  533 */     return getActiveMySQLConnection().getParanoid();
/*      */   }
/*      */   
/*      */   public String getPasswordCharacterEncoding() {
/*  537 */     return getActiveMySQLConnection().getPasswordCharacterEncoding();
/*      */   }
/*      */   
/*      */   public boolean getPedantic() {
/*  541 */     return getActiveMySQLConnection().getPedantic();
/*      */   }
/*      */   
/*      */   public boolean getPinGlobalTxToPhysicalConnection() {
/*  545 */     return getActiveMySQLConnection().getPinGlobalTxToPhysicalConnection();
/*      */   }
/*      */   
/*      */   public boolean getPopulateInsertRowWithDefaultValues() {
/*  549 */     return getActiveMySQLConnection().getPopulateInsertRowWithDefaultValues();
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSize()
/*      */   {
/*  554 */     return getActiveMySQLConnection().getPrepStmtCacheSize();
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSqlLimit() {
/*  558 */     return getActiveMySQLConnection().getPrepStmtCacheSqlLimit();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSize() {
/*  562 */     return getActiveMySQLConnection().getPreparedStatementCacheSize();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSqlLimit() {
/*  566 */     return getActiveMySQLConnection().getPreparedStatementCacheSqlLimit();
/*      */   }
/*      */   
/*      */   public boolean getProcessEscapeCodesForPrepStmts() {
/*  570 */     return getActiveMySQLConnection().getProcessEscapeCodesForPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getProfileSQL() {
/*  574 */     return getActiveMySQLConnection().getProfileSQL();
/*      */   }
/*      */   
/*      */   public boolean getProfileSql() {
/*  578 */     return getActiveMySQLConnection().getProfileSql();
/*      */   }
/*      */   
/*      */   public String getProfilerEventHandler() {
/*  582 */     return getActiveMySQLConnection().getProfilerEventHandler();
/*      */   }
/*      */   
/*      */   public String getPropertiesTransform() {
/*  586 */     return getActiveMySQLConnection().getPropertiesTransform();
/*      */   }
/*      */   
/*      */   public int getQueriesBeforeRetryMaster() {
/*  590 */     return getActiveMySQLConnection().getQueriesBeforeRetryMaster();
/*      */   }
/*      */   
/*      */   public boolean getQueryTimeoutKillsConnection() {
/*  594 */     return getActiveMySQLConnection().getQueryTimeoutKillsConnection();
/*      */   }
/*      */   
/*      */   public boolean getReconnectAtTxEnd() {
/*  598 */     return getActiveMySQLConnection().getReconnectAtTxEnd();
/*      */   }
/*      */   
/*      */   public boolean getRelaxAutoCommit() {
/*  602 */     return getActiveMySQLConnection().getRelaxAutoCommit();
/*      */   }
/*      */   
/*      */   public int getReportMetricsIntervalMillis() {
/*  606 */     return getActiveMySQLConnection().getReportMetricsIntervalMillis();
/*      */   }
/*      */   
/*      */   public boolean getRequireSSL() {
/*  610 */     return getActiveMySQLConnection().getRequireSSL();
/*      */   }
/*      */   
/*      */   public String getResourceId() {
/*  614 */     return getActiveMySQLConnection().getResourceId();
/*      */   }
/*      */   
/*      */   public int getResultSetSizeThreshold() {
/*  618 */     return getActiveMySQLConnection().getResultSetSizeThreshold();
/*      */   }
/*      */   
/*      */   public boolean getRetainStatementAfterResultSetClose() {
/*  622 */     return getActiveMySQLConnection().getRetainStatementAfterResultSetClose();
/*      */   }
/*      */   
/*      */   public int getRetriesAllDown()
/*      */   {
/*  627 */     return getActiveMySQLConnection().getRetriesAllDown();
/*      */   }
/*      */   
/*      */   public boolean getRewriteBatchedStatements() {
/*  631 */     return getActiveMySQLConnection().getRewriteBatchedStatements();
/*      */   }
/*      */   
/*      */   public boolean getRollbackOnPooledClose() {
/*  635 */     return getActiveMySQLConnection().getRollbackOnPooledClose();
/*      */   }
/*      */   
/*      */   public boolean getRoundRobinLoadBalance() {
/*  639 */     return getActiveMySQLConnection().getRoundRobinLoadBalance();
/*      */   }
/*      */   
/*      */   public boolean getRunningCTS13() {
/*  643 */     return getActiveMySQLConnection().getRunningCTS13();
/*      */   }
/*      */   
/*      */   public int getSecondsBeforeRetryMaster() {
/*  647 */     return getActiveMySQLConnection().getSecondsBeforeRetryMaster();
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingMaxOperations() {
/*  651 */     return getActiveMySQLConnection().getSelfDestructOnPingMaxOperations();
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingSecondsLifetime() {
/*  655 */     return getActiveMySQLConnection().getSelfDestructOnPingSecondsLifetime();
/*      */   }
/*      */   
/*      */   public String getServerTimezone()
/*      */   {
/*  660 */     return getActiveMySQLConnection().getServerTimezone();
/*      */   }
/*      */   
/*      */   public String getSessionVariables() {
/*  664 */     return getActiveMySQLConnection().getSessionVariables();
/*      */   }
/*      */   
/*      */   public int getSlowQueryThresholdMillis() {
/*  668 */     return getActiveMySQLConnection().getSlowQueryThresholdMillis();
/*      */   }
/*      */   
/*      */   public long getSlowQueryThresholdNanos() {
/*  672 */     return getActiveMySQLConnection().getSlowQueryThresholdNanos();
/*      */   }
/*      */   
/*      */   public String getSocketFactory() {
/*  676 */     return getActiveMySQLConnection().getSocketFactory();
/*      */   }
/*      */   
/*      */   public String getSocketFactoryClassName() {
/*  680 */     return getActiveMySQLConnection().getSocketFactoryClassName();
/*      */   }
/*      */   
/*      */   public int getSocketTimeout() {
/*  684 */     return getActiveMySQLConnection().getSocketTimeout();
/*      */   }
/*      */   
/*      */   public String getStatementInterceptors() {
/*  688 */     return getActiveMySQLConnection().getStatementInterceptors();
/*      */   }
/*      */   
/*      */   public boolean getStrictFloatingPoint() {
/*  692 */     return getActiveMySQLConnection().getStrictFloatingPoint();
/*      */   }
/*      */   
/*      */   public boolean getStrictUpdates() {
/*  696 */     return getActiveMySQLConnection().getStrictUpdates();
/*      */   }
/*      */   
/*      */   public boolean getTcpKeepAlive() {
/*  700 */     return getActiveMySQLConnection().getTcpKeepAlive();
/*      */   }
/*      */   
/*      */   public boolean getTcpNoDelay() {
/*  704 */     return getActiveMySQLConnection().getTcpNoDelay();
/*      */   }
/*      */   
/*      */   public int getTcpRcvBuf() {
/*  708 */     return getActiveMySQLConnection().getTcpRcvBuf();
/*      */   }
/*      */   
/*      */   public int getTcpSndBuf() {
/*  712 */     return getActiveMySQLConnection().getTcpSndBuf();
/*      */   }
/*      */   
/*      */   public int getTcpTrafficClass() {
/*  716 */     return getActiveMySQLConnection().getTcpTrafficClass();
/*      */   }
/*      */   
/*      */   public boolean getTinyInt1isBit() {
/*  720 */     return getActiveMySQLConnection().getTinyInt1isBit();
/*      */   }
/*      */   
/*      */   public boolean getTraceProtocol() {
/*  724 */     return getActiveMySQLConnection().getTraceProtocol();
/*      */   }
/*      */   
/*      */   public boolean getTransformedBitIsBoolean() {
/*  728 */     return getActiveMySQLConnection().getTransformedBitIsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTreatUtilDateAsTimestamp() {
/*  732 */     return getActiveMySQLConnection().getTreatUtilDateAsTimestamp();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStorePassword() {
/*  736 */     return getActiveMySQLConnection().getTrustCertificateKeyStorePassword();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStoreType() {
/*  740 */     return getActiveMySQLConnection().getTrustCertificateKeyStoreType();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStoreUrl() {
/*  744 */     return getActiveMySQLConnection().getTrustCertificateKeyStoreUrl();
/*      */   }
/*      */   
/*      */   public boolean getUltraDevHack() {
/*  748 */     return getActiveMySQLConnection().getUltraDevHack();
/*      */   }
/*      */   
/*      */   public boolean getUseAffectedRows() {
/*  752 */     return getActiveMySQLConnection().getUseAffectedRows();
/*      */   }
/*      */   
/*      */   public boolean getUseBlobToStoreUTF8OutsideBMP() {
/*  756 */     return getActiveMySQLConnection().getUseBlobToStoreUTF8OutsideBMP();
/*      */   }
/*      */   
/*      */   public boolean getUseColumnNamesInFindColumn() {
/*  760 */     return getActiveMySQLConnection().getUseColumnNamesInFindColumn();
/*      */   }
/*      */   
/*      */   public boolean getUseCompression() {
/*  764 */     return getActiveMySQLConnection().getUseCompression();
/*      */   }
/*      */   
/*      */   public String getUseConfigs() {
/*  768 */     return getActiveMySQLConnection().getUseConfigs();
/*      */   }
/*      */   
/*      */   public boolean getUseCursorFetch() {
/*  772 */     return getActiveMySQLConnection().getUseCursorFetch();
/*      */   }
/*      */   
/*      */   public boolean getUseDirectRowUnpack() {
/*  776 */     return getActiveMySQLConnection().getUseDirectRowUnpack();
/*      */   }
/*      */   
/*      */   public boolean getUseDynamicCharsetInfo() {
/*  780 */     return getActiveMySQLConnection().getUseDynamicCharsetInfo();
/*      */   }
/*      */   
/*      */   public boolean getUseFastDateParsing() {
/*  784 */     return getActiveMySQLConnection().getUseFastDateParsing();
/*      */   }
/*      */   
/*      */   public boolean getUseFastIntParsing() {
/*  788 */     return getActiveMySQLConnection().getUseFastIntParsing();
/*      */   }
/*      */   
/*      */   public boolean getUseGmtMillisForDatetimes() {
/*  792 */     return getActiveMySQLConnection().getUseGmtMillisForDatetimes();
/*      */   }
/*      */   
/*      */   public boolean getUseHostsInPrivileges() {
/*  796 */     return getActiveMySQLConnection().getUseHostsInPrivileges();
/*      */   }
/*      */   
/*      */   public boolean getUseInformationSchema() {
/*  800 */     return getActiveMySQLConnection().getUseInformationSchema();
/*      */   }
/*      */   
/*      */   public boolean getUseJDBCCompliantTimezoneShift() {
/*  804 */     return getActiveMySQLConnection().getUseJDBCCompliantTimezoneShift();
/*      */   }
/*      */   
/*      */   public boolean getUseJvmCharsetConverters() {
/*  808 */     return getActiveMySQLConnection().getUseJvmCharsetConverters();
/*      */   }
/*      */   
/*      */   public boolean getUseLegacyDatetimeCode() {
/*  812 */     return getActiveMySQLConnection().getUseLegacyDatetimeCode();
/*      */   }
/*      */   
/*      */   public boolean getUseLocalSessionState() {
/*  816 */     return getActiveMySQLConnection().getUseLocalSessionState();
/*      */   }
/*      */   
/*      */   public boolean getUseLocalTransactionState() {
/*  820 */     return getActiveMySQLConnection().getUseLocalTransactionState();
/*      */   }
/*      */   
/*      */   public boolean getUseNanosForElapsedTime() {
/*  824 */     return getActiveMySQLConnection().getUseNanosForElapsedTime();
/*      */   }
/*      */   
/*      */   public boolean getUseOldAliasMetadataBehavior() {
/*  828 */     return getActiveMySQLConnection().getUseOldAliasMetadataBehavior();
/*      */   }
/*      */   
/*      */   public boolean getUseOldUTF8Behavior() {
/*  832 */     return getActiveMySQLConnection().getUseOldUTF8Behavior();
/*      */   }
/*      */   
/*      */   public boolean getUseOnlyServerErrorMessages() {
/*  836 */     return getActiveMySQLConnection().getUseOnlyServerErrorMessages();
/*      */   }
/*      */   
/*      */   public boolean getUseReadAheadInput() {
/*  840 */     return getActiveMySQLConnection().getUseReadAheadInput();
/*      */   }
/*      */   
/*      */   public boolean getUseSSL() {
/*  844 */     return getActiveMySQLConnection().getUseSSL();
/*      */   }
/*      */   
/*      */   public boolean getUseSSPSCompatibleTimezoneShift() {
/*  848 */     return getActiveMySQLConnection().getUseSSPSCompatibleTimezoneShift();
/*      */   }
/*      */   
/*      */   public boolean getUseServerPrepStmts() {
/*  852 */     return getActiveMySQLConnection().getUseServerPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseServerPreparedStmts() {
/*  856 */     return getActiveMySQLConnection().getUseServerPreparedStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseSqlStateCodes() {
/*  860 */     return getActiveMySQLConnection().getUseSqlStateCodes();
/*      */   }
/*      */   
/*      */   public boolean getUseStreamLengthsInPrepStmts() {
/*  864 */     return getActiveMySQLConnection().getUseStreamLengthsInPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseTimezone() {
/*  868 */     return getActiveMySQLConnection().getUseTimezone();
/*      */   }
/*      */   
/*      */   public boolean getUseUltraDevWorkAround() {
/*  872 */     return getActiveMySQLConnection().getUseUltraDevWorkAround();
/*      */   }
/*      */   
/*      */   public boolean getUseUnbufferedInput() {
/*  876 */     return getActiveMySQLConnection().getUseUnbufferedInput();
/*      */   }
/*      */   
/*      */   public boolean getUseUnicode() {
/*  880 */     return getActiveMySQLConnection().getUseUnicode();
/*      */   }
/*      */   
/*      */   public boolean getUseUsageAdvisor() {
/*  884 */     return getActiveMySQLConnection().getUseUsageAdvisor();
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpExcludedColumnNamePattern() {
/*  888 */     return getActiveMySQLConnection().getUtf8OutsideBmpExcludedColumnNamePattern();
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpIncludedColumnNamePattern()
/*      */   {
/*  893 */     return getActiveMySQLConnection().getUtf8OutsideBmpIncludedColumnNamePattern();
/*      */   }
/*      */   
/*      */   public boolean getVerifyServerCertificate()
/*      */   {
/*  898 */     return getActiveMySQLConnection().getVerifyServerCertificate();
/*      */   }
/*      */   
/*      */   public boolean getYearIsDateType() {
/*  902 */     return getActiveMySQLConnection().getYearIsDateType();
/*      */   }
/*      */   
/*      */   public String getZeroDateTimeBehavior() {
/*  906 */     return getActiveMySQLConnection().getZeroDateTimeBehavior();
/*      */   }
/*      */   
/*      */   public void setAllowLoadLocalInfile(boolean property) {
/*  910 */     getActiveMySQLConnection().setAllowLoadLocalInfile(property);
/*      */   }
/*      */   
/*      */   public void setAllowMultiQueries(boolean property) {
/*  914 */     getActiveMySQLConnection().setAllowMultiQueries(property);
/*      */   }
/*      */   
/*      */   public void setAllowNanAndInf(boolean flag) {
/*  918 */     getActiveMySQLConnection().setAllowNanAndInf(flag);
/*      */   }
/*      */   
/*      */   public void setAllowUrlInLocalInfile(boolean flag) {
/*  922 */     getActiveMySQLConnection().setAllowUrlInLocalInfile(flag);
/*      */   }
/*      */   
/*      */   public void setAlwaysSendSetIsolation(boolean flag) {
/*  926 */     getActiveMySQLConnection().setAlwaysSendSetIsolation(flag);
/*      */   }
/*      */   
/*      */   public void setAutoClosePStmtStreams(boolean flag) {
/*  930 */     getActiveMySQLConnection().setAutoClosePStmtStreams(flag);
/*      */   }
/*      */   
/*      */   public void setAutoDeserialize(boolean flag) {
/*  934 */     getActiveMySQLConnection().setAutoDeserialize(flag);
/*      */   }
/*      */   
/*      */   public void setAutoGenerateTestcaseScript(boolean flag) {
/*  938 */     getActiveMySQLConnection().setAutoGenerateTestcaseScript(flag);
/*      */   }
/*      */   
/*      */   public void setAutoReconnect(boolean flag) {
/*  942 */     getActiveMySQLConnection().setAutoReconnect(flag);
/*      */   }
/*      */   
/*      */   public void setAutoReconnectForConnectionPools(boolean property) {
/*  946 */     getActiveMySQLConnection().setAutoReconnectForConnectionPools(property);
/*      */   }
/*      */   
/*      */   public void setAutoReconnectForPools(boolean flag) {
/*  950 */     getActiveMySQLConnection().setAutoReconnectForPools(flag);
/*      */   }
/*      */   
/*      */   public void setAutoSlowLog(boolean flag) {
/*  954 */     getActiveMySQLConnection().setAutoSlowLog(flag);
/*      */   }
/*      */   
/*      */   public void setBlobSendChunkSize(String value) throws SQLException {
/*  958 */     getActiveMySQLConnection().setBlobSendChunkSize(value);
/*      */   }
/*      */   
/*      */   public void setBlobsAreStrings(boolean flag) {
/*  962 */     getActiveMySQLConnection().setBlobsAreStrings(flag);
/*      */   }
/*      */   
/*      */   public void setCacheCallableStatements(boolean flag) {
/*  966 */     getActiveMySQLConnection().setCacheCallableStatements(flag);
/*      */   }
/*      */   
/*      */   public void setCacheCallableStmts(boolean flag) {
/*  970 */     getActiveMySQLConnection().setCacheCallableStmts(flag);
/*      */   }
/*      */   
/*      */   public void setCachePrepStmts(boolean flag) {
/*  974 */     getActiveMySQLConnection().setCachePrepStmts(flag);
/*      */   }
/*      */   
/*      */   public void setCachePreparedStatements(boolean flag) {
/*  978 */     getActiveMySQLConnection().setCachePreparedStatements(flag);
/*      */   }
/*      */   
/*      */   public void setCacheResultSetMetadata(boolean property) {
/*  982 */     getActiveMySQLConnection().setCacheResultSetMetadata(property);
/*      */   }
/*      */   
/*      */   public void setCacheServerConfiguration(boolean flag) {
/*  986 */     getActiveMySQLConnection().setCacheServerConfiguration(flag);
/*      */   }
/*      */   
/*      */   public void setCallableStatementCacheSize(int size) throws SQLException {
/*  990 */     getActiveMySQLConnection().setCallableStatementCacheSize(size);
/*      */   }
/*      */   
/*      */   public void setCallableStmtCacheSize(int cacheSize) throws SQLException {
/*  994 */     getActiveMySQLConnection().setCallableStmtCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */   public void setCapitalizeDBMDTypes(boolean property) {
/*  998 */     getActiveMySQLConnection().setCapitalizeDBMDTypes(property);
/*      */   }
/*      */   
/*      */   public void setCapitalizeTypeNames(boolean flag) {
/* 1002 */     getActiveMySQLConnection().setCapitalizeTypeNames(flag);
/*      */   }
/*      */   
/*      */   public void setCharacterEncoding(String encoding) {
/* 1006 */     getActiveMySQLConnection().setCharacterEncoding(encoding);
/*      */   }
/*      */   
/*      */   public void setCharacterSetResults(String characterSet) {
/* 1010 */     getActiveMySQLConnection().setCharacterSetResults(characterSet);
/*      */   }
/*      */   
/*      */   public void setClientCertificateKeyStorePassword(String value) {
/* 1014 */     getActiveMySQLConnection().setClientCertificateKeyStorePassword(value);
/*      */   }
/*      */   
/*      */   public void setClientCertificateKeyStoreType(String value) {
/* 1018 */     getActiveMySQLConnection().setClientCertificateKeyStoreType(value);
/*      */   }
/*      */   
/*      */   public void setClientCertificateKeyStoreUrl(String value) {
/* 1022 */     getActiveMySQLConnection().setClientCertificateKeyStoreUrl(value);
/*      */   }
/*      */   
/*      */   public void setClientInfoProvider(String classname) {
/* 1026 */     getActiveMySQLConnection().setClientInfoProvider(classname);
/*      */   }
/*      */   
/*      */   public void setClobCharacterEncoding(String encoding) {
/* 1030 */     getActiveMySQLConnection().setClobCharacterEncoding(encoding);
/*      */   }
/*      */   
/*      */   public void setClobberStreamingResults(boolean flag) {
/* 1034 */     getActiveMySQLConnection().setClobberStreamingResults(flag);
/*      */   }
/*      */   
/*      */   public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
/* 1038 */     getActiveMySQLConnection().setCompensateOnDuplicateKeyUpdateCounts(flag);
/*      */   }
/*      */   
/*      */   public void setConnectTimeout(int timeoutMs) throws SQLException
/*      */   {
/* 1043 */     getActiveMySQLConnection().setConnectTimeout(timeoutMs);
/*      */   }
/*      */   
/*      */   public void setConnectionCollation(String collation) {
/* 1047 */     getActiveMySQLConnection().setConnectionCollation(collation);
/*      */   }
/*      */   
/*      */   public void setConnectionLifecycleInterceptors(String interceptors) {
/* 1051 */     getActiveMySQLConnection().setConnectionLifecycleInterceptors(interceptors);
/*      */   }
/*      */   
/*      */   public void setContinueBatchOnError(boolean property)
/*      */   {
/* 1056 */     getActiveMySQLConnection().setContinueBatchOnError(property);
/*      */   }
/*      */   
/*      */   public void setCreateDatabaseIfNotExist(boolean flag) {
/* 1060 */     getActiveMySQLConnection().setCreateDatabaseIfNotExist(flag);
/*      */   }
/*      */   
/*      */   public void setDefaultFetchSize(int n) throws SQLException {
/* 1064 */     getActiveMySQLConnection().setDefaultFetchSize(n);
/*      */   }
/*      */   
/*      */   public void setDetectServerPreparedStmts(boolean property) {
/* 1068 */     getActiveMySQLConnection().setDetectServerPreparedStmts(property);
/*      */   }
/*      */   
/*      */   public void setDontTrackOpenResources(boolean flag) {
/* 1072 */     getActiveMySQLConnection().setDontTrackOpenResources(flag);
/*      */   }
/*      */   
/*      */   public void setDumpMetadataOnColumnNotFound(boolean flag) {
/* 1076 */     getActiveMySQLConnection().setDumpMetadataOnColumnNotFound(flag);
/*      */   }
/*      */   
/*      */   public void setDumpQueriesOnException(boolean flag) {
/* 1080 */     getActiveMySQLConnection().setDumpQueriesOnException(flag);
/*      */   }
/*      */   
/*      */   public void setDynamicCalendars(boolean flag) {
/* 1084 */     getActiveMySQLConnection().setDynamicCalendars(flag);
/*      */   }
/*      */   
/*      */   public void setElideSetAutoCommits(boolean flag) {
/* 1088 */     getActiveMySQLConnection().setElideSetAutoCommits(flag);
/*      */   }
/*      */   
/*      */   public void setEmptyStringsConvertToZero(boolean flag) {
/* 1092 */     getActiveMySQLConnection().setEmptyStringsConvertToZero(flag);
/*      */   }
/*      */   
/*      */   public void setEmulateLocators(boolean property) {
/* 1096 */     getActiveMySQLConnection().setEmulateLocators(property);
/*      */   }
/*      */   
/*      */   public void setEmulateUnsupportedPstmts(boolean flag) {
/* 1100 */     getActiveMySQLConnection().setEmulateUnsupportedPstmts(flag);
/*      */   }
/*      */   
/*      */   public void setEnablePacketDebug(boolean flag) {
/* 1104 */     getActiveMySQLConnection().setEnablePacketDebug(flag);
/*      */   }
/*      */   
/*      */   public void setEnableQueryTimeouts(boolean flag) {
/* 1108 */     getActiveMySQLConnection().setEnableQueryTimeouts(flag);
/*      */   }
/*      */   
/*      */   public void setEncoding(String property) {
/* 1112 */     getActiveMySQLConnection().setEncoding(property);
/*      */   }
/*      */   
/*      */   public void setExceptionInterceptors(String exceptionInterceptors) {
/* 1116 */     getActiveMySQLConnection().setExceptionInterceptors(exceptionInterceptors);
/*      */   }
/*      */   
/*      */   public void setExplainSlowQueries(boolean flag)
/*      */   {
/* 1121 */     getActiveMySQLConnection().setExplainSlowQueries(flag);
/*      */   }
/*      */   
/*      */   public void setFailOverReadOnly(boolean flag) {
/* 1125 */     getActiveMySQLConnection().setFailOverReadOnly(flag);
/*      */   }
/*      */   
/*      */   public void setFunctionsNeverReturnBlobs(boolean flag) {
/* 1129 */     getActiveMySQLConnection().setFunctionsNeverReturnBlobs(flag);
/*      */   }
/*      */   
/*      */   public void setGatherPerfMetrics(boolean flag) {
/* 1133 */     getActiveMySQLConnection().setGatherPerfMetrics(flag);
/*      */   }
/*      */   
/*      */   public void setGatherPerformanceMetrics(boolean flag) {
/* 1137 */     getActiveMySQLConnection().setGatherPerformanceMetrics(flag);
/*      */   }
/*      */   
/*      */   public void setGenerateSimpleParameterMetadata(boolean flag) {
/* 1141 */     getActiveMySQLConnection().setGenerateSimpleParameterMetadata(flag);
/*      */   }
/*      */   
/*      */   public void setHoldResultsOpenOverStatementClose(boolean flag) {
/* 1145 */     getActiveMySQLConnection().setHoldResultsOpenOverStatementClose(flag);
/*      */   }
/*      */   
/*      */   public void setIgnoreNonTxTables(boolean property) {
/* 1149 */     getActiveMySQLConnection().setIgnoreNonTxTables(property);
/*      */   }
/*      */   
/*      */   public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
/* 1153 */     getActiveMySQLConnection().setIncludeInnodbStatusInDeadlockExceptions(flag);
/*      */   }
/*      */   
/*      */   public void setInitialTimeout(int property) throws SQLException
/*      */   {
/* 1158 */     getActiveMySQLConnection().setInitialTimeout(property);
/*      */   }
/*      */   
/*      */   public void setInteractiveClient(boolean property) {
/* 1162 */     getActiveMySQLConnection().setInteractiveClient(property);
/*      */   }
/*      */   
/*      */   public void setIsInteractiveClient(boolean property) {
/* 1166 */     getActiveMySQLConnection().setIsInteractiveClient(property);
/*      */   }
/*      */   
/*      */   public void setJdbcCompliantTruncation(boolean flag) {
/* 1170 */     getActiveMySQLConnection().setJdbcCompliantTruncation(flag);
/*      */   }
/*      */   
/*      */   public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads)
/*      */   {
/* 1175 */     getActiveMySQLConnection().setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
/*      */   }
/*      */   
/*      */   public void setLargeRowSizeThreshold(String value) throws SQLException
/*      */   {
/* 1180 */     getActiveMySQLConnection().setLargeRowSizeThreshold(value);
/*      */   }
/*      */   
/*      */   public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
/* 1184 */     getActiveMySQLConnection().setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
/*      */   }
/*      */   
/*      */   public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException
/*      */   {
/* 1189 */     getActiveMySQLConnection().setLoadBalancePingTimeout(loadBalancePingTimeout);
/*      */   }
/*      */   
/*      */   public void setLoadBalanceStrategy(String strategy)
/*      */   {
/* 1194 */     getActiveMySQLConnection().setLoadBalanceStrategy(strategy);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer)
/*      */   {
/* 1200 */     getActiveMySQLConnection().setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLocalSocketAddress(String address)
/*      */   {
/* 1207 */     getActiveMySQLConnection().setLocalSocketAddress(address);
/*      */   }
/*      */   
/*      */   public void setLocatorFetchBufferSize(String value) throws SQLException
/*      */   {
/* 1212 */     getActiveMySQLConnection().setLocatorFetchBufferSize(value);
/*      */   }
/*      */   
/*      */   public void setLogSlowQueries(boolean flag)
/*      */   {
/* 1217 */     getActiveMySQLConnection().setLogSlowQueries(flag);
/*      */   }
/*      */   
/*      */   public void setLogXaCommands(boolean flag)
/*      */   {
/* 1222 */     getActiveMySQLConnection().setLogXaCommands(flag);
/*      */   }
/*      */   
/*      */   public void setLogger(String property)
/*      */   {
/* 1227 */     getActiveMySQLConnection().setLogger(property);
/*      */   }
/*      */   
/*      */   public void setLoggerClassName(String className)
/*      */   {
/* 1232 */     getActiveMySQLConnection().setLoggerClassName(className);
/*      */   }
/*      */   
/*      */   public void setMaintainTimeStats(boolean flag)
/*      */   {
/* 1237 */     getActiveMySQLConnection().setMaintainTimeStats(flag);
/*      */   }
/*      */   
/*      */   public void setMaxQuerySizeToLog(int sizeInBytes) throws SQLException
/*      */   {
/* 1242 */     getActiveMySQLConnection().setMaxQuerySizeToLog(sizeInBytes);
/*      */   }
/*      */   
/*      */   public void setMaxReconnects(int property) throws SQLException
/*      */   {
/* 1247 */     getActiveMySQLConnection().setMaxReconnects(property);
/*      */   }
/*      */   
/*      */   public void setMaxRows(int property) throws SQLException
/*      */   {
/* 1252 */     getActiveMySQLConnection().setMaxRows(property);
/*      */   }
/*      */   
/*      */   public void setMetadataCacheSize(int value) throws SQLException
/*      */   {
/* 1257 */     getActiveMySQLConnection().setMetadataCacheSize(value);
/*      */   }
/*      */   
/*      */   public void setNetTimeoutForStreamingResults(int value) throws SQLException
/*      */   {
/* 1262 */     getActiveMySQLConnection().setNetTimeoutForStreamingResults(value);
/*      */   }
/*      */   
/*      */   public void setNoAccessToProcedureBodies(boolean flag)
/*      */   {
/* 1267 */     getActiveMySQLConnection().setNoAccessToProcedureBodies(flag);
/*      */   }
/*      */   
/*      */   public void setNoDatetimeStringSync(boolean flag)
/*      */   {
/* 1272 */     getActiveMySQLConnection().setNoDatetimeStringSync(flag);
/*      */   }
/*      */   
/*      */   public void setNoTimezoneConversionForTimeType(boolean flag)
/*      */   {
/* 1277 */     getActiveMySQLConnection().setNoTimezoneConversionForTimeType(flag);
/*      */   }
/*      */   
/*      */   public void setNullCatalogMeansCurrent(boolean value)
/*      */   {
/* 1282 */     getActiveMySQLConnection().setNullCatalogMeansCurrent(value);
/*      */   }
/*      */   
/*      */   public void setNullNamePatternMatchesAll(boolean value)
/*      */   {
/* 1287 */     getActiveMySQLConnection().setNullNamePatternMatchesAll(value);
/*      */   }
/*      */   
/*      */   public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag)
/*      */   {
/* 1292 */     getActiveMySQLConnection().setOverrideSupportsIntegrityEnhancementFacility(flag);
/*      */   }
/*      */   
/*      */   public void setPacketDebugBufferSize(int size)
/*      */     throws SQLException
/*      */   {
/* 1298 */     getActiveMySQLConnection().setPacketDebugBufferSize(size);
/*      */   }
/*      */   
/*      */   public void setPadCharsWithSpace(boolean flag)
/*      */   {
/* 1303 */     getActiveMySQLConnection().setPadCharsWithSpace(flag);
/*      */   }
/*      */   
/*      */   public void setParanoid(boolean property)
/*      */   {
/* 1308 */     getActiveMySQLConnection().setParanoid(property);
/*      */   }
/*      */   
/*      */   public void setPasswordCharacterEncoding(String characterSet)
/*      */   {
/* 1313 */     getActiveMySQLConnection().setPasswordCharacterEncoding(characterSet);
/*      */   }
/*      */   
/*      */   public void setPedantic(boolean property)
/*      */   {
/* 1318 */     getActiveMySQLConnection().setPedantic(property);
/*      */   }
/*      */   
/*      */   public void setPinGlobalTxToPhysicalConnection(boolean flag)
/*      */   {
/* 1323 */     getActiveMySQLConnection().setPinGlobalTxToPhysicalConnection(flag);
/*      */   }
/*      */   
/*      */   public void setPopulateInsertRowWithDefaultValues(boolean flag)
/*      */   {
/* 1328 */     getActiveMySQLConnection().setPopulateInsertRowWithDefaultValues(flag);
/*      */   }
/*      */   
/*      */   public void setPrepStmtCacheSize(int cacheSize) throws SQLException
/*      */   {
/* 1333 */     getActiveMySQLConnection().setPrepStmtCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */   public void setPrepStmtCacheSqlLimit(int sqlLimit) throws SQLException
/*      */   {
/* 1338 */     getActiveMySQLConnection().setPrepStmtCacheSqlLimit(sqlLimit);
/*      */   }
/*      */   
/*      */   public void setPreparedStatementCacheSize(int cacheSize) throws SQLException
/*      */   {
/* 1343 */     getActiveMySQLConnection().setPreparedStatementCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */   public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) throws SQLException
/*      */   {
/* 1348 */     getActiveMySQLConnection().setPreparedStatementCacheSqlLimit(cacheSqlLimit);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setProcessEscapeCodesForPrepStmts(boolean flag)
/*      */   {
/* 1354 */     getActiveMySQLConnection().setProcessEscapeCodesForPrepStmts(flag);
/*      */   }
/*      */   
/*      */   public void setProfileSQL(boolean flag)
/*      */   {
/* 1359 */     getActiveMySQLConnection().setProfileSQL(flag);
/*      */   }
/*      */   
/*      */   public void setProfileSql(boolean property)
/*      */   {
/* 1364 */     getActiveMySQLConnection().setProfileSql(property);
/*      */   }
/*      */   
/*      */   public void setProfilerEventHandler(String handler)
/*      */   {
/* 1369 */     getActiveMySQLConnection().setProfilerEventHandler(handler);
/*      */   }
/*      */   
/*      */   public void setPropertiesTransform(String value)
/*      */   {
/* 1374 */     getActiveMySQLConnection().setPropertiesTransform(value);
/*      */   }
/*      */   
/*      */   public void setQueriesBeforeRetryMaster(int property) throws SQLException
/*      */   {
/* 1379 */     getActiveMySQLConnection().setQueriesBeforeRetryMaster(property);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection)
/*      */   {
/* 1385 */     getActiveMySQLConnection().setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setReconnectAtTxEnd(boolean property)
/*      */   {
/* 1391 */     getActiveMySQLConnection().setReconnectAtTxEnd(property);
/*      */   }
/*      */   
/*      */   public void setRelaxAutoCommit(boolean property)
/*      */   {
/* 1396 */     getActiveMySQLConnection().setRelaxAutoCommit(property);
/*      */   }
/*      */   
/*      */   public void setReportMetricsIntervalMillis(int millis) throws SQLException
/*      */   {
/* 1401 */     getActiveMySQLConnection().setReportMetricsIntervalMillis(millis);
/*      */   }
/*      */   
/*      */   public void setRequireSSL(boolean property)
/*      */   {
/* 1406 */     getActiveMySQLConnection().setRequireSSL(property);
/*      */   }
/*      */   
/*      */   public void setResourceId(String resourceId)
/*      */   {
/* 1411 */     getActiveMySQLConnection().setResourceId(resourceId);
/*      */   }
/*      */   
/*      */   public void setResultSetSizeThreshold(int threshold) throws SQLException
/*      */   {
/* 1416 */     getActiveMySQLConnection().setResultSetSizeThreshold(threshold);
/*      */   }
/*      */   
/*      */   public void setRetainStatementAfterResultSetClose(boolean flag)
/*      */   {
/* 1421 */     getActiveMySQLConnection().setRetainStatementAfterResultSetClose(flag);
/*      */   }
/*      */   
/*      */   public void setRetriesAllDown(int retriesAllDown) throws SQLException
/*      */   {
/* 1426 */     getActiveMySQLConnection().setRetriesAllDown(retriesAllDown);
/*      */   }
/*      */   
/*      */   public void setRewriteBatchedStatements(boolean flag)
/*      */   {
/* 1431 */     getActiveMySQLConnection().setRewriteBatchedStatements(flag);
/*      */   }
/*      */   
/*      */   public void setRollbackOnPooledClose(boolean flag)
/*      */   {
/* 1436 */     getActiveMySQLConnection().setRollbackOnPooledClose(flag);
/*      */   }
/*      */   
/*      */   public void setRoundRobinLoadBalance(boolean flag)
/*      */   {
/* 1441 */     getActiveMySQLConnection().setRoundRobinLoadBalance(flag);
/*      */   }
/*      */   
/*      */   public void setRunningCTS13(boolean flag)
/*      */   {
/* 1446 */     getActiveMySQLConnection().setRunningCTS13(flag);
/*      */   }
/*      */   
/*      */   public void setSecondsBeforeRetryMaster(int property) throws SQLException
/*      */   {
/* 1451 */     getActiveMySQLConnection().setSecondsBeforeRetryMaster(property);
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingMaxOperations(int maxOperations) throws SQLException
/*      */   {
/* 1456 */     getActiveMySQLConnection().setSelfDestructOnPingMaxOperations(maxOperations);
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingSecondsLifetime(int seconds)
/*      */     throws SQLException
/*      */   {
/* 1462 */     getActiveMySQLConnection().setSelfDestructOnPingSecondsLifetime(seconds);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setServerTimezone(String property)
/*      */   {
/* 1468 */     getActiveMySQLConnection().setServerTimezone(property);
/*      */   }
/*      */   
/*      */   public void setSessionVariables(String variables)
/*      */   {
/* 1473 */     getActiveMySQLConnection().setSessionVariables(variables);
/*      */   }
/*      */   
/*      */   public void setSlowQueryThresholdMillis(int millis) throws SQLException
/*      */   {
/* 1478 */     getActiveMySQLConnection().setSlowQueryThresholdMillis(millis);
/*      */   }
/*      */   
/*      */   public void setSlowQueryThresholdNanos(long nanos) throws SQLException
/*      */   {
/* 1483 */     getActiveMySQLConnection().setSlowQueryThresholdNanos(nanos);
/*      */   }
/*      */   
/*      */   public void setSocketFactory(String name)
/*      */   {
/* 1488 */     getActiveMySQLConnection().setSocketFactory(name);
/*      */   }
/*      */   
/*      */   public void setSocketFactoryClassName(String property)
/*      */   {
/* 1493 */     getActiveMySQLConnection().setSocketFactoryClassName(property);
/*      */   }
/*      */   
/*      */   public void setSocketTimeout(int property) throws SQLException
/*      */   {
/* 1498 */     getActiveMySQLConnection().setSocketTimeout(property);
/*      */   }
/*      */   
/*      */   public void setStatementInterceptors(String value)
/*      */   {
/* 1503 */     getActiveMySQLConnection().setStatementInterceptors(value);
/*      */   }
/*      */   
/*      */   public void setStrictFloatingPoint(boolean property)
/*      */   {
/* 1508 */     getActiveMySQLConnection().setStrictFloatingPoint(property);
/*      */   }
/*      */   
/*      */   public void setStrictUpdates(boolean property)
/*      */   {
/* 1513 */     getActiveMySQLConnection().setStrictUpdates(property);
/*      */   }
/*      */   
/*      */   public void setTcpKeepAlive(boolean flag)
/*      */   {
/* 1518 */     getActiveMySQLConnection().setTcpKeepAlive(flag);
/*      */   }
/*      */   
/*      */   public void setTcpNoDelay(boolean flag)
/*      */   {
/* 1523 */     getActiveMySQLConnection().setTcpNoDelay(flag);
/*      */   }
/*      */   
/*      */   public void setTcpRcvBuf(int bufSize) throws SQLException
/*      */   {
/* 1528 */     getActiveMySQLConnection().setTcpRcvBuf(bufSize);
/*      */   }
/*      */   
/*      */   public void setTcpSndBuf(int bufSize) throws SQLException
/*      */   {
/* 1533 */     getActiveMySQLConnection().setTcpSndBuf(bufSize);
/*      */   }
/*      */   
/*      */   public void setTcpTrafficClass(int classFlags) throws SQLException
/*      */   {
/* 1538 */     getActiveMySQLConnection().setTcpTrafficClass(classFlags);
/*      */   }
/*      */   
/*      */   public void setTinyInt1isBit(boolean flag)
/*      */   {
/* 1543 */     getActiveMySQLConnection().setTinyInt1isBit(flag);
/*      */   }
/*      */   
/*      */   public void setTraceProtocol(boolean flag)
/*      */   {
/* 1548 */     getActiveMySQLConnection().setTraceProtocol(flag);
/*      */   }
/*      */   
/*      */   public void setTransformedBitIsBoolean(boolean flag)
/*      */   {
/* 1553 */     getActiveMySQLConnection().setTransformedBitIsBoolean(flag);
/*      */   }
/*      */   
/*      */   public void setTreatUtilDateAsTimestamp(boolean flag)
/*      */   {
/* 1558 */     getActiveMySQLConnection().setTreatUtilDateAsTimestamp(flag);
/*      */   }
/*      */   
/*      */   public void setTrustCertificateKeyStorePassword(String value)
/*      */   {
/* 1563 */     getActiveMySQLConnection().setTrustCertificateKeyStorePassword(value);
/*      */   }
/*      */   
/*      */   public void setTrustCertificateKeyStoreType(String value)
/*      */   {
/* 1568 */     getActiveMySQLConnection().setTrustCertificateKeyStoreType(value);
/*      */   }
/*      */   
/*      */   public void setTrustCertificateKeyStoreUrl(String value)
/*      */   {
/* 1573 */     getActiveMySQLConnection().setTrustCertificateKeyStoreUrl(value);
/*      */   }
/*      */   
/*      */   public void setUltraDevHack(boolean flag)
/*      */   {
/* 1578 */     getActiveMySQLConnection().setUltraDevHack(flag);
/*      */   }
/*      */   
/*      */   public void setUseAffectedRows(boolean flag)
/*      */   {
/* 1583 */     getActiveMySQLConnection().setUseAffectedRows(flag);
/*      */   }
/*      */   
/*      */   public void setUseBlobToStoreUTF8OutsideBMP(boolean flag)
/*      */   {
/* 1588 */     getActiveMySQLConnection().setUseBlobToStoreUTF8OutsideBMP(flag);
/*      */   }
/*      */   
/*      */   public void setUseColumnNamesInFindColumn(boolean flag)
/*      */   {
/* 1593 */     getActiveMySQLConnection().setUseColumnNamesInFindColumn(flag);
/*      */   }
/*      */   
/*      */   public void setUseCompression(boolean property)
/*      */   {
/* 1598 */     getActiveMySQLConnection().setUseCompression(property);
/*      */   }
/*      */   
/*      */   public void setUseConfigs(String configs)
/*      */   {
/* 1603 */     getActiveMySQLConnection().setUseConfigs(configs);
/*      */   }
/*      */   
/*      */   public void setUseCursorFetch(boolean flag)
/*      */   {
/* 1608 */     getActiveMySQLConnection().setUseCursorFetch(flag);
/*      */   }
/*      */   
/*      */   public void setUseDirectRowUnpack(boolean flag)
/*      */   {
/* 1613 */     getActiveMySQLConnection().setUseDirectRowUnpack(flag);
/*      */   }
/*      */   
/*      */   public void setUseDynamicCharsetInfo(boolean flag)
/*      */   {
/* 1618 */     getActiveMySQLConnection().setUseDynamicCharsetInfo(flag);
/*      */   }
/*      */   
/*      */   public void setUseFastDateParsing(boolean flag)
/*      */   {
/* 1623 */     getActiveMySQLConnection().setUseFastDateParsing(flag);
/*      */   }
/*      */   
/*      */   public void setUseFastIntParsing(boolean flag)
/*      */   {
/* 1628 */     getActiveMySQLConnection().setUseFastIntParsing(flag);
/*      */   }
/*      */   
/*      */   public void setUseGmtMillisForDatetimes(boolean flag)
/*      */   {
/* 1633 */     getActiveMySQLConnection().setUseGmtMillisForDatetimes(flag);
/*      */   }
/*      */   
/*      */   public void setUseHostsInPrivileges(boolean property)
/*      */   {
/* 1638 */     getActiveMySQLConnection().setUseHostsInPrivileges(property);
/*      */   }
/*      */   
/*      */   public void setUseInformationSchema(boolean flag)
/*      */   {
/* 1643 */     getActiveMySQLConnection().setUseInformationSchema(flag);
/*      */   }
/*      */   
/*      */   public void setUseJDBCCompliantTimezoneShift(boolean flag)
/*      */   {
/* 1648 */     getActiveMySQLConnection().setUseJDBCCompliantTimezoneShift(flag);
/*      */   }
/*      */   
/*      */   public void setUseJvmCharsetConverters(boolean flag)
/*      */   {
/* 1653 */     getActiveMySQLConnection().setUseJvmCharsetConverters(flag);
/*      */   }
/*      */   
/*      */   public void setUseLegacyDatetimeCode(boolean flag)
/*      */   {
/* 1658 */     getActiveMySQLConnection().setUseLegacyDatetimeCode(flag);
/*      */   }
/*      */   
/*      */   public void setUseLocalSessionState(boolean flag)
/*      */   {
/* 1663 */     getActiveMySQLConnection().setUseLocalSessionState(flag);
/*      */   }
/*      */   
/*      */   public void setUseLocalTransactionState(boolean flag)
/*      */   {
/* 1668 */     getActiveMySQLConnection().setUseLocalTransactionState(flag);
/*      */   }
/*      */   
/*      */   public void setUseNanosForElapsedTime(boolean flag)
/*      */   {
/* 1673 */     getActiveMySQLConnection().setUseNanosForElapsedTime(flag);
/*      */   }
/*      */   
/*      */   public void setUseOldAliasMetadataBehavior(boolean flag)
/*      */   {
/* 1678 */     getActiveMySQLConnection().setUseOldAliasMetadataBehavior(flag);
/*      */   }
/*      */   
/*      */   public void setUseOldUTF8Behavior(boolean flag)
/*      */   {
/* 1683 */     getActiveMySQLConnection().setUseOldUTF8Behavior(flag);
/*      */   }
/*      */   
/*      */   public void setUseOnlyServerErrorMessages(boolean flag)
/*      */   {
/* 1688 */     getActiveMySQLConnection().setUseOnlyServerErrorMessages(flag);
/*      */   }
/*      */   
/*      */   public void setUseReadAheadInput(boolean flag)
/*      */   {
/* 1693 */     getActiveMySQLConnection().setUseReadAheadInput(flag);
/*      */   }
/*      */   
/*      */   public void setUseSSL(boolean property)
/*      */   {
/* 1698 */     getActiveMySQLConnection().setUseSSL(property);
/*      */   }
/*      */   
/*      */   public void setUseSSPSCompatibleTimezoneShift(boolean flag)
/*      */   {
/* 1703 */     getActiveMySQLConnection().setUseSSPSCompatibleTimezoneShift(flag);
/*      */   }
/*      */   
/*      */   public void setUseServerPrepStmts(boolean flag)
/*      */   {
/* 1708 */     getActiveMySQLConnection().setUseServerPrepStmts(flag);
/*      */   }
/*      */   
/*      */   public void setUseServerPreparedStmts(boolean flag)
/*      */   {
/* 1713 */     getActiveMySQLConnection().setUseServerPreparedStmts(flag);
/*      */   }
/*      */   
/*      */   public void setUseSqlStateCodes(boolean flag)
/*      */   {
/* 1718 */     getActiveMySQLConnection().setUseSqlStateCodes(flag);
/*      */   }
/*      */   
/*      */   public void setUseStreamLengthsInPrepStmts(boolean property)
/*      */   {
/* 1723 */     getActiveMySQLConnection().setUseStreamLengthsInPrepStmts(property);
/*      */   }
/*      */   
/*      */   public void setUseTimezone(boolean property)
/*      */   {
/* 1728 */     getActiveMySQLConnection().setUseTimezone(property);
/*      */   }
/*      */   
/*      */   public void setUseUltraDevWorkAround(boolean property)
/*      */   {
/* 1733 */     getActiveMySQLConnection().setUseUltraDevWorkAround(property);
/*      */   }
/*      */   
/*      */   public void setUseUnbufferedInput(boolean flag)
/*      */   {
/* 1738 */     getActiveMySQLConnection().setUseUnbufferedInput(flag);
/*      */   }
/*      */   
/*      */   public void setUseUnicode(boolean flag)
/*      */   {
/* 1743 */     getActiveMySQLConnection().setUseUnicode(flag);
/*      */   }
/*      */   
/*      */   public void setUseUsageAdvisor(boolean useUsageAdvisorFlag)
/*      */   {
/* 1748 */     getActiveMySQLConnection().setUseUsageAdvisor(useUsageAdvisorFlag);
/*      */   }
/*      */   
/*      */   public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern)
/*      */   {
/* 1753 */     getActiveMySQLConnection().setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern)
/*      */   {
/* 1759 */     getActiveMySQLConnection().setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setVerifyServerCertificate(boolean flag)
/*      */   {
/* 1765 */     getActiveMySQLConnection().setVerifyServerCertificate(flag);
/*      */   }
/*      */   
/*      */   public void setYearIsDateType(boolean flag)
/*      */   {
/* 1770 */     getActiveMySQLConnection().setYearIsDateType(flag);
/*      */   }
/*      */   
/*      */   public void setZeroDateTimeBehavior(String behavior)
/*      */   {
/* 1775 */     getActiveMySQLConnection().setZeroDateTimeBehavior(behavior);
/*      */   }
/*      */   
/*      */   public boolean useUnbufferedInput()
/*      */   {
/* 1780 */     return getActiveMySQLConnection().useUnbufferedInput();
/*      */   }
/*      */   
/*      */   public StringBuffer generateConnectionCommentBlock(StringBuffer buf)
/*      */   {
/* 1785 */     return getActiveMySQLConnection().generateConnectionCommentBlock(buf);
/*      */   }
/*      */   
/*      */   public int getActiveStatementCount()
/*      */   {
/* 1790 */     return getActiveMySQLConnection().getActiveStatementCount();
/*      */   }
/*      */   
/*      */   public boolean getAutoCommit() throws SQLException
/*      */   {
/* 1795 */     return getActiveMySQLConnection().getAutoCommit();
/*      */   }
/*      */   
/*      */   public int getAutoIncrementIncrement()
/*      */   {
/* 1800 */     return getActiveMySQLConnection().getAutoIncrementIncrement();
/*      */   }
/*      */   
/*      */   public CachedResultSetMetaData getCachedMetaData(String sql)
/*      */   {
/* 1805 */     return getActiveMySQLConnection().getCachedMetaData(sql);
/*      */   }
/*      */   
/*      */   public Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 1810 */     return getActiveMySQLConnection().getCalendarInstanceForSessionOrNew();
/*      */   }
/*      */   
/*      */   public Timer getCancelTimer()
/*      */   {
/* 1815 */     return getActiveMySQLConnection().getCancelTimer();
/*      */   }
/*      */   
/*      */   public String getCatalog() throws SQLException
/*      */   {
/* 1820 */     return getActiveMySQLConnection().getCatalog();
/*      */   }
/*      */   
/*      */   public String getCharacterSetMetadata()
/*      */   {
/* 1825 */     return getActiveMySQLConnection().getCharacterSetMetadata();
/*      */   }
/*      */   
/*      */   public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName)
/*      */     throws SQLException
/*      */   {
/* 1831 */     return getActiveMySQLConnection().getCharsetConverter(javaEncodingName);
/*      */   }
/*      */   
/*      */   public String getCharsetNameForIndex(int charsetIndex) throws SQLException
/*      */   {
/* 1836 */     return getActiveMySQLConnection().getCharsetNameForIndex(charsetIndex);
/*      */   }
/*      */   
/*      */   public TimeZone getDefaultTimeZone()
/*      */   {
/* 1841 */     return getActiveMySQLConnection().getDefaultTimeZone();
/*      */   }
/*      */   
/*      */   public String getErrorMessageEncoding()
/*      */   {
/* 1846 */     return getActiveMySQLConnection().getErrorMessageEncoding();
/*      */   }
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor()
/*      */   {
/* 1851 */     return getActiveMySQLConnection().getExceptionInterceptor();
/*      */   }
/*      */   
/*      */   public int getHoldability() throws SQLException
/*      */   {
/* 1856 */     return getActiveMySQLConnection().getHoldability();
/*      */   }
/*      */   
/*      */   public String getHost()
/*      */   {
/* 1861 */     return getActiveMySQLConnection().getHost();
/*      */   }
/*      */   
/*      */   public long getId()
/*      */   {
/* 1866 */     return getActiveMySQLConnection().getId();
/*      */   }
/*      */   
/*      */   public long getIdleFor()
/*      */   {
/* 1871 */     return getActiveMySQLConnection().getIdleFor();
/*      */   }
/*      */   
/*      */   public MysqlIO getIO() throws SQLException
/*      */   {
/* 1876 */     return getActiveMySQLConnection().getIO();
/*      */   }
/*      */   
/*      */   public MySQLConnection getLoadBalanceSafeProxy()
/*      */   {
/* 1881 */     return getActiveMySQLConnection().getLoadBalanceSafeProxy();
/*      */   }
/*      */   
/*      */   public Log getLog() throws SQLException
/*      */   {
/* 1886 */     return getActiveMySQLConnection().getLog();
/*      */   }
/*      */   
/*      */   public int getMaxBytesPerChar(String javaCharsetName) throws SQLException
/*      */   {
/* 1891 */     return getActiveMySQLConnection().getMaxBytesPerChar(javaCharsetName);
/*      */   }
/*      */   
/*      */   public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException
/*      */   {
/* 1896 */     return getActiveMySQLConnection().getMaxBytesPerChar(charsetIndex, javaCharsetName);
/*      */   }
/*      */   
/*      */   public DatabaseMetaData getMetaData() throws SQLException
/*      */   {
/* 1901 */     return getActiveMySQLConnection().getMetaData();
/*      */   }
/*      */   
/*      */   public java.sql.Statement getMetadataSafeStatement() throws SQLException
/*      */   {
/* 1906 */     return getActiveMySQLConnection().getMetadataSafeStatement();
/*      */   }
/*      */   
/*      */   public int getNetBufferLength()
/*      */   {
/* 1911 */     return getActiveMySQLConnection().getNetBufferLength();
/*      */   }
/*      */   
/*      */   public Properties getProperties()
/*      */   {
/* 1916 */     return getActiveMySQLConnection().getProperties();
/*      */   }
/*      */   
/*      */   public boolean getRequiresEscapingEncoder()
/*      */   {
/* 1921 */     return getActiveMySQLConnection().getRequiresEscapingEncoder();
/*      */   }
/*      */   
/*      */   public String getServerCharacterEncoding()
/*      */   {
/* 1926 */     return getActiveMySQLConnection().getServerCharacterEncoding();
/*      */   }
/*      */   
/*      */   public int getServerMajorVersion()
/*      */   {
/* 1931 */     return getActiveMySQLConnection().getServerMajorVersion();
/*      */   }
/*      */   
/*      */   public int getServerMinorVersion()
/*      */   {
/* 1936 */     return getActiveMySQLConnection().getServerMinorVersion();
/*      */   }
/*      */   
/*      */   public int getServerSubMinorVersion()
/*      */   {
/* 1941 */     return getActiveMySQLConnection().getServerSubMinorVersion();
/*      */   }
/*      */   
/*      */   public TimeZone getServerTimezoneTZ()
/*      */   {
/* 1946 */     return getActiveMySQLConnection().getServerTimezoneTZ();
/*      */   }
/*      */   
/*      */   public String getServerVariable(String variableName)
/*      */   {
/* 1951 */     return getActiveMySQLConnection().getServerVariable(variableName);
/*      */   }
/*      */   
/*      */   public String getServerVersion()
/*      */   {
/* 1956 */     return getActiveMySQLConnection().getServerVersion();
/*      */   }
/*      */   
/*      */   public Calendar getSessionLockedCalendar()
/*      */   {
/* 1961 */     return getActiveMySQLConnection().getSessionLockedCalendar();
/*      */   }
/*      */   
/*      */   public String getStatementComment()
/*      */   {
/* 1966 */     return getActiveMySQLConnection().getStatementComment();
/*      */   }
/*      */   
/*      */   public List<StatementInterceptorV2> getStatementInterceptorsInstances()
/*      */   {
/* 1971 */     return getActiveMySQLConnection().getStatementInterceptorsInstances();
/*      */   }
/*      */   
/*      */   public int getTransactionIsolation() throws SQLException
/*      */   {
/* 1976 */     return getActiveMySQLConnection().getTransactionIsolation();
/*      */   }
/*      */   
/*      */   public Map<String, Class<?>> getTypeMap() throws SQLException
/*      */   {
/* 1981 */     return getActiveMySQLConnection().getTypeMap();
/*      */   }
/*      */   
/*      */   public String getURL()
/*      */   {
/* 1986 */     return getActiveMySQLConnection().getURL();
/*      */   }
/*      */   
/*      */   public String getUser()
/*      */   {
/* 1991 */     return getActiveMySQLConnection().getUser();
/*      */   }
/*      */   
/*      */   public Calendar getUtcCalendar()
/*      */   {
/* 1996 */     return getActiveMySQLConnection().getUtcCalendar();
/*      */   }
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException
/*      */   {
/* 2001 */     return getActiveMySQLConnection().getWarnings();
/*      */   }
/*      */   
/*      */   public boolean hasSameProperties(Connection c)
/*      */   {
/* 2006 */     return getActiveMySQLConnection().hasSameProperties(c);
/*      */   }
/*      */   
/*      */   public boolean hasTriedMaster()
/*      */   {
/* 2011 */     return getActiveMySQLConnection().hasTriedMaster();
/*      */   }
/*      */   
/*      */   public void incrementNumberOfPreparedExecutes()
/*      */   {
/* 2016 */     getActiveMySQLConnection().incrementNumberOfPreparedExecutes();
/*      */   }
/*      */   
/*      */   public void incrementNumberOfPrepares()
/*      */   {
/* 2021 */     getActiveMySQLConnection().incrementNumberOfPrepares();
/*      */   }
/*      */   
/*      */   public void incrementNumberOfResultSetsCreated()
/*      */   {
/* 2026 */     getActiveMySQLConnection().incrementNumberOfResultSetsCreated();
/*      */   }
/*      */   
/*      */   public void initializeExtension(Extension ex) throws SQLException
/*      */   {
/* 2031 */     getActiveMySQLConnection().initializeExtension(ex);
/*      */   }
/*      */   
/*      */ 
/*      */   public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet)
/*      */     throws SQLException
/*      */   {
/* 2038 */     getActiveMySQLConnection().initializeResultsMetadataFromCache(sql, cachedMetaData, resultSet);
/*      */   }
/*      */   
/*      */   public void initializeSafeStatementInterceptors()
/*      */     throws SQLException
/*      */   {
/* 2044 */     getActiveMySQLConnection().initializeSafeStatementInterceptors();
/*      */   }
/*      */   
/*      */   public boolean isAbonormallyLongQuery(long millisOrNanos)
/*      */   {
/* 2049 */     return getActiveMySQLConnection().isAbonormallyLongQuery(millisOrNanos);
/*      */   }
/*      */   
/*      */   public boolean isClientTzUTC()
/*      */   {
/* 2054 */     return getActiveMySQLConnection().isClientTzUTC();
/*      */   }
/*      */   
/*      */   public boolean isCursorFetchEnabled() throws SQLException
/*      */   {
/* 2059 */     return getActiveMySQLConnection().isCursorFetchEnabled();
/*      */   }
/*      */   
/*      */   public boolean isInGlobalTx()
/*      */   {
/* 2064 */     return getActiveMySQLConnection().isInGlobalTx();
/*      */   }
/*      */   
/*      */   public boolean isMasterConnection()
/*      */   {
/* 2069 */     return getActiveMySQLConnection().isMasterConnection();
/*      */   }
/*      */   
/*      */   public boolean isNoBackslashEscapesSet()
/*      */   {
/* 2074 */     return getActiveMySQLConnection().isNoBackslashEscapesSet();
/*      */   }
/*      */   
/*      */   public boolean isReadInfoMsgEnabled()
/*      */   {
/* 2079 */     return getActiveMySQLConnection().isReadInfoMsgEnabled();
/*      */   }
/*      */   
/*      */   public boolean isReadOnly() throws SQLException
/*      */   {
/* 2084 */     return getActiveMySQLConnection().isReadOnly();
/*      */   }
/*      */   
/*      */   public boolean isReadOnly(boolean useSessionStatus) throws SQLException
/*      */   {
/* 2089 */     return getActiveMySQLConnection().isReadOnly(useSessionStatus);
/*      */   }
/*      */   
/*      */   public boolean isRunningOnJDK13()
/*      */   {
/* 2094 */     return getActiveMySQLConnection().isRunningOnJDK13();
/*      */   }
/*      */   
/*      */   public boolean isSameResource(Connection otherConnection)
/*      */   {
/* 2099 */     return getActiveMySQLConnection().isSameResource(otherConnection);
/*      */   }
/*      */   
/*      */   public boolean isServerTzUTC()
/*      */   {
/* 2104 */     return getActiveMySQLConnection().isServerTzUTC();
/*      */   }
/*      */   
/*      */   public boolean lowerCaseTableNames()
/*      */   {
/* 2109 */     return getActiveMySQLConnection().lowerCaseTableNames();
/*      */   }
/*      */   
/*      */   public void maxRowsChanged(Statement stmt)
/*      */   {
/* 2114 */     getActiveMySQLConnection().maxRowsChanged(stmt);
/*      */   }
/*      */   
/*      */   public String nativeSQL(String sql) throws SQLException
/*      */   {
/* 2119 */     return getActiveMySQLConnection().nativeSQL(sql);
/*      */   }
/*      */   
/*      */   public boolean parserKnowsUnicode()
/*      */   {
/* 2124 */     return getActiveMySQLConnection().parserKnowsUnicode();
/*      */   }
/*      */   
/*      */   public void ping() throws SQLException {
/* 2128 */     ping(true);
/*      */   }
/*      */   
/*      */   public void ping(boolean allConnections) throws SQLException {
/* 2132 */     if (allConnections) {
/* 2133 */       this.proxy.doPing();
/*      */     } else {
/* 2135 */       getActiveMySQLConnection().ping();
/*      */     }
/*      */   }
/*      */   
/*      */   public void pingInternal(boolean checkForClosedConnection, int timeoutMillis)
/*      */     throws SQLException
/*      */   {
/* 2142 */     getActiveMySQLConnection().pingInternal(checkForClosedConnection, timeoutMillis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 2150 */     return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */ 
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 2157 */     return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   public CallableStatement prepareCall(String sql)
/*      */     throws SQLException
/*      */   {
/* 2163 */     return getActiveMySQLConnection().prepareCall(sql);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 2170 */     return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 2177 */     return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 2184 */     return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndex);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 2191 */     return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndexes);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 2198 */     return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyColNames);
/*      */   }
/*      */   
/*      */   public PreparedStatement prepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 2204 */     return getActiveMySQLConnection().prepareStatement(sql);
/*      */   }
/*      */   
/*      */   public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason)
/*      */     throws SQLException
/*      */   {
/* 2210 */     getActiveMySQLConnection().realClose(calledExplicitly, issueRollback, skipLocalTeardown, reason);
/*      */   }
/*      */   
/*      */ 
/*      */   public void recachePreparedStatement(ServerPreparedStatement pstmt)
/*      */     throws SQLException
/*      */   {
/* 2217 */     getActiveMySQLConnection().recachePreparedStatement(pstmt);
/*      */   }
/*      */   
/*      */   public void registerQueryExecutionTime(long queryTimeMs)
/*      */   {
/* 2222 */     getActiveMySQLConnection().registerQueryExecutionTime(queryTimeMs);
/*      */   }
/*      */   
/*      */   public void registerStatement(Statement stmt)
/*      */   {
/* 2227 */     getActiveMySQLConnection().registerStatement(stmt);
/*      */   }
/*      */   
/*      */   public void releaseSavepoint(Savepoint arg0) throws SQLException
/*      */   {
/* 2232 */     getActiveMySQLConnection().releaseSavepoint(arg0);
/*      */   }
/*      */   
/*      */   public void reportNumberOfTablesAccessed(int numTablesAccessed)
/*      */   {
/* 2237 */     getActiveMySQLConnection().reportNumberOfTablesAccessed(numTablesAccessed);
/*      */   }
/*      */   
/*      */ 
/*      */   public void reportQueryTime(long millisOrNanos)
/*      */   {
/* 2243 */     getActiveMySQLConnection().reportQueryTime(millisOrNanos);
/*      */   }
/*      */   
/*      */   public void resetServerState() throws SQLException
/*      */   {
/* 2248 */     getActiveMySQLConnection().resetServerState();
/*      */   }
/*      */   
/*      */   public void rollback() throws SQLException
/*      */   {
/* 2253 */     getActiveMySQLConnection().rollback();
/*      */   }
/*      */   
/*      */   public void rollback(Savepoint savepoint) throws SQLException
/*      */   {
/* 2258 */     getActiveMySQLConnection().rollback(savepoint);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 2265 */     return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 2272 */     return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 2279 */     return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndex);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 2286 */     return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 2293 */     return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyColNames);
/*      */   }
/*      */   
/*      */ 
/*      */   public PreparedStatement serverPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 2300 */     return getActiveMySQLConnection().serverPrepareStatement(sql);
/*      */   }
/*      */   
/*      */   public boolean serverSupportsConvertFn() throws SQLException
/*      */   {
/* 2305 */     return getActiveMySQLConnection().serverSupportsConvertFn();
/*      */   }
/*      */   
/*      */   public void setAutoCommit(boolean autoCommitFlag) throws SQLException
/*      */   {
/* 2310 */     getActiveMySQLConnection().setAutoCommit(autoCommitFlag);
/*      */   }
/*      */   
/*      */   public void setCatalog(String catalog) throws SQLException
/*      */   {
/* 2315 */     getActiveMySQLConnection().setCatalog(catalog);
/*      */   }
/*      */   
/*      */   public void setFailedOver(boolean flag)
/*      */   {
/* 2320 */     getActiveMySQLConnection().setFailedOver(flag);
/*      */   }
/*      */   
/*      */   public void setHoldability(int arg0) throws SQLException
/*      */   {
/* 2325 */     getActiveMySQLConnection().setHoldability(arg0);
/*      */   }
/*      */   
/*      */   public void setInGlobalTx(boolean flag)
/*      */   {
/* 2330 */     getActiveMySQLConnection().setInGlobalTx(flag);
/*      */   }
/*      */   
/*      */   public void setPreferSlaveDuringFailover(boolean flag)
/*      */   {
/* 2335 */     getActiveMySQLConnection().setPreferSlaveDuringFailover(flag);
/*      */   }
/*      */   
/*      */   public void setProxy(MySQLConnection proxy)
/*      */   {
/* 2340 */     getActiveMySQLConnection().setProxy(proxy);
/*      */   }
/*      */   
/*      */   public void setReadInfoMsgEnabled(boolean flag)
/*      */   {
/* 2345 */     getActiveMySQLConnection().setReadInfoMsgEnabled(flag);
/*      */   }
/*      */   
/*      */   public void setReadOnly(boolean readOnlyFlag) throws SQLException
/*      */   {
/* 2350 */     getActiveMySQLConnection().setReadOnly(readOnlyFlag);
/*      */   }
/*      */   
/*      */   public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
/* 2354 */     getActiveMySQLConnection().setReadOnlyInternal(readOnlyFlag);
/*      */   }
/*      */   
/*      */   public Savepoint setSavepoint() throws SQLException {
/* 2358 */     return getActiveMySQLConnection().setSavepoint();
/*      */   }
/*      */   
/*      */   public Savepoint setSavepoint(String name) throws SQLException {
/* 2362 */     return getActiveMySQLConnection().setSavepoint(name);
/*      */   }
/*      */   
/*      */   public void setStatementComment(String comment) {
/* 2366 */     getActiveMySQLConnection().setStatementComment(comment);
/*      */   }
/*      */   
/*      */   public void setTransactionIsolation(int level) throws SQLException
/*      */   {
/* 2371 */     getActiveMySQLConnection().setTransactionIsolation(level);
/*      */   }
/*      */   
/*      */   public void shutdownServer() throws SQLException
/*      */   {
/* 2376 */     getActiveMySQLConnection().shutdownServer();
/*      */   }
/*      */   
/*      */   public boolean storesLowerCaseTableName() {
/* 2380 */     return getActiveMySQLConnection().storesLowerCaseTableName();
/*      */   }
/*      */   
/*      */   public boolean supportsIsolationLevel() {
/* 2384 */     return getActiveMySQLConnection().supportsIsolationLevel();
/*      */   }
/*      */   
/*      */   public boolean supportsQuotedIdentifiers() {
/* 2388 */     return getActiveMySQLConnection().supportsQuotedIdentifiers();
/*      */   }
/*      */   
/*      */   public boolean supportsTransactions() {
/* 2392 */     return getActiveMySQLConnection().supportsTransactions();
/*      */   }
/*      */   
/*      */   public void throwConnectionClosedException() throws SQLException {
/* 2396 */     getActiveMySQLConnection().throwConnectionClosedException();
/*      */   }
/*      */   
/*      */   public void transactionBegun() throws SQLException {
/* 2400 */     getActiveMySQLConnection().transactionBegun();
/*      */   }
/*      */   
/*      */   public void transactionCompleted() throws SQLException {
/* 2404 */     getActiveMySQLConnection().transactionCompleted();
/*      */   }
/*      */   
/*      */   public void unregisterStatement(Statement stmt) {
/* 2408 */     getActiveMySQLConnection().unregisterStatement(stmt);
/*      */   }
/*      */   
/*      */   public void unSafeStatementInterceptors() throws SQLException {
/* 2412 */     getActiveMySQLConnection().unSafeStatementInterceptors();
/*      */   }
/*      */   
/*      */   public void unsetMaxRows(Statement stmt) throws SQLException {
/* 2416 */     getActiveMySQLConnection().unsetMaxRows(stmt);
/*      */   }
/*      */   
/*      */   public boolean useAnsiQuotedIdentifiers() {
/* 2420 */     return getActiveMySQLConnection().useAnsiQuotedIdentifiers();
/*      */   }
/*      */   
/*      */   public boolean useMaxRows() {
/* 2424 */     return getActiveMySQLConnection().useMaxRows();
/*      */   }
/*      */   
/*      */   public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException
/*      */   {
/* 2429 */     return getActiveMySQLConnection().versionMeetsMinimum(major, minor, subminor);
/*      */   }
/*      */   
/*      */   public boolean isClosed() throws SQLException
/*      */   {
/* 2434 */     return getActiveMySQLConnection().isClosed();
/*      */   }
/*      */   
/*      */   public boolean getHoldResultsOpenOverStatementClose() {
/* 2438 */     return getActiveMySQLConnection().getHoldResultsOpenOverStatementClose();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceConnectionGroup()
/*      */   {
/* 2443 */     return getActiveMySQLConnection().getLoadBalanceConnectionGroup();
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceEnableJMX() {
/* 2447 */     return getActiveMySQLConnection().getLoadBalanceEnableJMX();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceExceptionChecker() {
/* 2451 */     return getActiveMySQLConnection().getLoadBalanceExceptionChecker();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLExceptionSubclassFailover()
/*      */   {
/* 2456 */     return getActiveMySQLConnection().getLoadBalanceSQLExceptionSubclassFailover();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLStateFailover()
/*      */   {
/* 2461 */     return getActiveMySQLConnection().getLoadBalanceSQLStateFailover();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup)
/*      */   {
/* 2466 */     getActiveMySQLConnection().setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX)
/*      */   {
/* 2472 */     getActiveMySQLConnection().setLoadBalanceEnableJMX(loadBalanceEnableJMX);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker)
/*      */   {
/* 2479 */     getActiveMySQLConnection().setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover)
/*      */   {
/* 2486 */     getActiveMySQLConnection().setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover)
/*      */   {
/* 2493 */     getActiveMySQLConnection().setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean shouldExecutionTriggerServerSwapAfter(String SQL)
/*      */   {
/* 2504 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isProxySet() {
/* 2508 */     return getActiveMySQLConnection().isProxySet();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceAutoCommitStatementRegex()
/*      */   {
/* 2513 */     return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementRegex();
/*      */   }
/*      */   
/*      */   public int getLoadBalanceAutoCommitStatementThreshold()
/*      */   {
/* 2518 */     return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementThreshold();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex)
/*      */   {
/* 2524 */     getActiveMySQLConnection().setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold)
/*      */     throws SQLException
/*      */   {
/* 2531 */     getActiveMySQLConnection().setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getIncludeThreadDumpInDeadlockExceptions()
/*      */   {
/* 2537 */     return getActiveMySQLConnection().getIncludeThreadDumpInDeadlockExceptions();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
/* 2541 */     getActiveMySQLConnection().setIncludeThreadDumpInDeadlockExceptions(flag);
/*      */   }
/*      */   
/*      */   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
/* 2545 */     getActiveMySQLConnection().setTypeMap(map);
/*      */   }
/*      */   
/*      */   public boolean getIncludeThreadNamesAsStatementComment() {
/* 2549 */     return getActiveMySQLConnection().getIncludeThreadNamesAsStatementComment();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadNamesAsStatementComment(boolean flag) {
/* 2553 */     getActiveMySQLConnection().setIncludeThreadNamesAsStatementComment(flag);
/*      */   }
/*      */   
/*      */   public boolean isServerLocal() throws SQLException {
/* 2557 */     return getActiveMySQLConnection().isServerLocal();
/*      */   }
/*      */   
/*      */   public void setAuthenticationPlugins(String authenticationPlugins) {
/* 2561 */     getActiveMySQLConnection().setAuthenticationPlugins(authenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getAuthenticationPlugins() {
/* 2565 */     return getActiveMySQLConnection().getAuthenticationPlugins();
/*      */   }
/*      */   
/*      */   public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins)
/*      */   {
/* 2570 */     getActiveMySQLConnection().setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getDisabledAuthenticationPlugins() {
/* 2574 */     return getActiveMySQLConnection().getDisabledAuthenticationPlugins();
/*      */   }
/*      */   
/*      */   public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin)
/*      */   {
/* 2579 */     getActiveMySQLConnection().setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
/*      */   }
/*      */   
/*      */   public String getDefaultAuthenticationPlugin() {
/* 2583 */     return getActiveMySQLConnection().getDefaultAuthenticationPlugin();
/*      */   }
/*      */   
/*      */   public void setParseInfoCacheFactory(String factoryClassname) {
/* 2587 */     getActiveMySQLConnection().setParseInfoCacheFactory(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getParseInfoCacheFactory() {
/* 2591 */     return getActiveMySQLConnection().getParseInfoCacheFactory();
/*      */   }
/*      */   
/*      */   public void setSchema(String schema) throws SQLException {
/* 2595 */     getActiveMySQLConnection().setSchema(schema);
/*      */   }
/*      */   
/*      */   public String getSchema() throws SQLException {
/* 2599 */     return getActiveMySQLConnection().getSchema();
/*      */   }
/*      */   
/*      */   public void abort(Executor executor) throws SQLException {
/* 2603 */     getActiveMySQLConnection().abort(executor);
/*      */   }
/*      */   
/*      */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
/*      */   {
/* 2608 */     getActiveMySQLConnection().setNetworkTimeout(executor, milliseconds);
/*      */   }
/*      */   
/*      */   public int getNetworkTimeout() throws SQLException {
/* 2612 */     return getActiveMySQLConnection().getNetworkTimeout();
/*      */   }
/*      */   
/*      */   public void setServerConfigCacheFactory(String factoryClassname) {
/* 2616 */     getActiveMySQLConnection().setServerConfigCacheFactory(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getServerConfigCacheFactory() {
/* 2620 */     return getActiveMySQLConnection().getServerConfigCacheFactory();
/*      */   }
/*      */   
/*      */   public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
/* 2624 */     getActiveMySQLConnection().setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
/*      */   }
/*      */   
/*      */   public boolean getDisconnectOnExpiredPasswords() {
/* 2628 */     return getActiveMySQLConnection().getDisconnectOnExpiredPasswords();
/*      */   }
/*      */   
/*      */   public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions) {
/* 2632 */     getActiveMySQLConnection().setGetProceduresReturnsFunctions(getProcedureReturnsFunctions);
/*      */   }
/*      */   
/*      */   public boolean getGetProceduresReturnsFunctions() {
/* 2636 */     return getActiveMySQLConnection().getGetProceduresReturnsFunctions();
/*      */   }
/*      */   
/*      */   public Object getConnectionMutex() {
/* 2640 */     return getActiveMySQLConnection().getConnectionMutex();
/*      */   }
/*      */   
/*      */   public String getConnectionAttributes() throws SQLException {
/* 2644 */     return getActiveMySQLConnection().getConnectionAttributes();
/*      */   }
/*      */   
/*      */   public boolean addHost(String host) throws SQLException {
/* 2648 */     return this.proxy.addHost(host);
/*      */   }
/*      */   
/*      */   public void removeHost(String host) throws SQLException {
/* 2652 */     this.proxy.removeHost(host);
/*      */   }
/*      */   
/*      */   public void removeHostWhenNotInUse(String host) throws SQLException {
/* 2656 */     this.proxy.removeHostWhenNotInUse(host);
/*      */   }
/*      */   
/*      */   public boolean getAllowMasterDownConnections()
/*      */   {
/* 2661 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setAllowMasterDownConnections(boolean connectIfMasterDown) {}
/*      */   
/*      */   public boolean getReplicationEnableJMX()
/*      */   {
/* 2669 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setReplicationEnableJMX(boolean replicationEnableJMX) {}
/*      */   
/*      */ 
/*      */   public void setDetectCustomCollations(boolean detectCustomCollations)
/*      */   {
/* 2678 */     getActiveMySQLConnection().setDetectCustomCollations(detectCustomCollations);
/*      */   }
/*      */   
/*      */   public boolean getDetectCustomCollations() {
/* 2682 */     return getActiveMySQLConnection().getDetectCustomCollations();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\LoadBalancedMySQLConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */