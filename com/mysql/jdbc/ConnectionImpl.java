/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.LogFactory;
/*      */ import com.mysql.jdbc.log.LogUtils;
/*      */ import com.mysql.jdbc.log.NullLogger;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import com.mysql.jdbc.util.LRUCache;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLPermission;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Savepoint;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
/*      */ import java.util.TreeMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConnectionImpl
/*      */   extends ConnectionPropertiesImpl
/*      */   implements MySQLConnection
/*      */ {
/*      */   private static final long serialVersionUID = 2877471301981509474L;
/*   88 */   private static final SQLPermission SET_NETWORK_TIMEOUT_PERM = new SQLPermission("setNetworkTimeout");
/*      */   
/*   90 */   private static final SQLPermission ABORT_PERM = new SQLPermission("abort");
/*      */   private static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
/*      */   
/*      */   public String getHost()
/*      */   {
/*   95 */     return this.host;
/*      */   }
/*      */   
/*   98 */   private MySQLConnection proxy = null;
/*      */   
/*  100 */   private InvocationHandler realProxy = null;
/*      */   
/*      */   public boolean isProxySet() {
/*  103 */     return this.proxy != null;
/*      */   }
/*      */   
/*      */   public void setProxy(MySQLConnection proxy) {
/*  107 */     this.proxy = proxy;
/*      */   }
/*      */   
/*      */   public void setRealProxy(InvocationHandler proxy) {
/*  111 */     this.realProxy = proxy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private MySQLConnection getProxy()
/*      */   {
/*  118 */     return this.proxy != null ? this.proxy : this;
/*      */   }
/*      */   
/*      */   public MySQLConnection getLoadBalanceSafeProxy() {
/*  122 */     return getProxy();
/*      */   }
/*      */   
/*      */   public Object getConnectionMutex() {
/*  126 */     return this.realProxy != null ? this.realProxy : this;
/*      */   }
/*      */   
/*      */   class ExceptionInterceptorChain implements ExceptionInterceptor {
/*      */     List<Extension> interceptors;
/*      */     
/*      */     ExceptionInterceptorChain(String interceptorClasses) throws SQLException {
/*  133 */       this.interceptors = Util.loadExtensions(ConnectionImpl.this, ConnectionImpl.this.props, interceptorClasses, "Connection.BadExceptionInterceptor", this);
/*      */     }
/*      */     
/*      */     void addRingZero(ExceptionInterceptor interceptor) throws SQLException {
/*  137 */       this.interceptors.add(0, interceptor);
/*      */     }
/*      */     
/*      */     public SQLException interceptException(SQLException sqlEx, Connection conn) {
/*  141 */       if (this.interceptors != null) {
/*  142 */         Iterator<Extension> iter = this.interceptors.iterator();
/*      */         
/*  144 */         while (iter.hasNext()) {
/*  145 */           sqlEx = ((ExceptionInterceptor)iter.next()).interceptException(sqlEx, ConnectionImpl.this);
/*      */         }
/*      */       }
/*      */       
/*  149 */       return sqlEx;
/*      */     }
/*      */     
/*      */     public void destroy() {
/*  153 */       if (this.interceptors != null) {
/*  154 */         Iterator<Extension> iter = this.interceptors.iterator();
/*      */         
/*  156 */         while (iter.hasNext()) {
/*  157 */           ((ExceptionInterceptor)iter.next()).destroy();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void init(Connection conn, Properties properties) throws SQLException
/*      */     {
/*  164 */       if (this.interceptors != null) {
/*  165 */         Iterator<Extension> iter = this.interceptors.iterator();
/*      */         
/*  167 */         while (iter.hasNext()) {
/*  168 */           ((ExceptionInterceptor)iter.next()).init(conn, properties);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static class CompoundCacheKey
/*      */   {
/*      */     String componentOne;
/*      */     
/*      */     String componentTwo;
/*      */     
/*      */     int hashCode;
/*      */     
/*      */ 
/*      */     CompoundCacheKey(String partOne, String partTwo)
/*      */     {
/*  187 */       this.componentOne = partOne;
/*  188 */       this.componentTwo = partTwo;
/*      */       
/*      */ 
/*      */ 
/*  192 */       this.hashCode = ((this.componentOne != null ? this.componentOne : "") + this.componentTwo).hashCode();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/*  202 */       if ((obj instanceof CompoundCacheKey)) {
/*  203 */         CompoundCacheKey another = (CompoundCacheKey)obj;
/*      */         
/*  205 */         boolean firstPartEqual = false;
/*      */         
/*  207 */         if (this.componentOne == null) {
/*  208 */           firstPartEqual = another.componentOne == null;
/*      */         } else {
/*  210 */           firstPartEqual = this.componentOne.equals(another.componentOne);
/*      */         }
/*      */         
/*      */ 
/*  214 */         return (firstPartEqual) && (this.componentTwo.equals(another.componentTwo));
/*      */       }
/*      */       
/*      */ 
/*  218 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  227 */       return this.hashCode;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */   private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */   public static Map<?, ?> charsetMap;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int HISTOGRAM_BUCKETS = 20;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String LOGGER_INSTANCE_NAME = "MySQL";
/*      */   
/*      */ 
/*      */ 
/*  255 */   private static Map<String, Integer> mapTransIsolationNameToValue = null;
/*      */   
/*      */ 
/*  258 */   private static final Log NULL_LOGGER = new NullLogger("MySQL");
/*      */   
/*      */   protected static Map<?, ?> roundRobinStatsMap;
/*      */   
/*  262 */   private static final Map<String, Map<Long, String>> serverCollationByUrl = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  268 */   private static final Map<String, Map<Integer, String>> serverJavaCharsetByUrl = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  273 */   private static final Map<String, Map<Integer, String>> serverCustomCharsetByUrl = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  278 */   private static final Map<String, Map<String, Integer>> serverCustomMblenByUrl = new HashMap();
/*      */   
/*      */   private CacheAdapter<String, Map<String, String>> serverConfigCache;
/*      */   
/*      */   private long queryTimeCount;
/*      */   
/*      */   private double queryTimeSum;
/*      */   
/*      */   private double queryTimeSumSquares;
/*      */   
/*      */   private double queryTimeMean;
/*      */   
/*      */   private transient Timer cancelTimer;
/*      */   private List<Extension> connectionLifecycleInterceptors;
/*      */   private static final Constructor<?> JDBC_4_CONNECTION_CTOR;
/*      */   private static final int DEFAULT_RESULT_SET_TYPE = 1003;
/*      */   private static final int DEFAULT_RESULT_SET_CONCURRENCY = 1007;
/*      */   
/*      */   static
/*      */   {
/*  298 */     mapTransIsolationNameToValue = new HashMap(8);
/*  299 */     mapTransIsolationNameToValue.put("READ-UNCOMMITED", Integer.valueOf(1));
/*  300 */     mapTransIsolationNameToValue.put("READ-UNCOMMITTED", Integer.valueOf(1));
/*  301 */     mapTransIsolationNameToValue.put("READ-COMMITTED", Integer.valueOf(2));
/*  302 */     mapTransIsolationNameToValue.put("REPEATABLE-READ", Integer.valueOf(4));
/*  303 */     mapTransIsolationNameToValue.put("SERIALIZABLE", Integer.valueOf(8));
/*      */     
/*  305 */     if (Util.isJdbc4()) {
/*      */       try {
/*  307 */         JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4Connection").getConstructor(new Class[] { String.class, Integer.TYPE, Properties.class, String.class, String.class });
/*      */ 
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*  312 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  314 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  316 */         throw new RuntimeException(e);
/*      */       }
/*      */     } else {
/*  319 */       JDBC_4_CONNECTION_CTOR = null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend, ExceptionInterceptor interceptor)
/*      */   {
/*  325 */     String origMessage = sqlEx.getMessage();
/*  326 */     String sqlState = sqlEx.getSQLState();
/*  327 */     int vendorErrorCode = sqlEx.getErrorCode();
/*      */     
/*  329 */     StringBuffer messageBuf = new StringBuffer(origMessage.length() + messageToAppend.length());
/*      */     
/*  331 */     messageBuf.append(origMessage);
/*  332 */     messageBuf.append(messageToAppend);
/*      */     
/*  334 */     SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf.toString(), sqlState, vendorErrorCode, interceptor);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  344 */       Method getStackTraceMethod = null;
/*  345 */       Method setStackTraceMethod = null;
/*  346 */       Object theStackTraceAsObject = null;
/*      */       
/*  348 */       Class<?> stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
/*  349 */       Class<?> stackTraceElementArrayClass = Array.newInstance(stackTraceElementClass, new int[] { 0 }).getClass();
/*      */       
/*      */ 
/*  352 */       getStackTraceMethod = Throwable.class.getMethod("getStackTrace", new Class[0]);
/*      */       
/*      */ 
/*  355 */       setStackTraceMethod = Throwable.class.getMethod("setStackTrace", new Class[] { stackTraceElementArrayClass });
/*      */       
/*      */ 
/*  358 */       if ((getStackTraceMethod != null) && (setStackTraceMethod != null)) {
/*  359 */         theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx, new Object[0]);
/*      */         
/*  361 */         setStackTraceMethod.invoke(sqlExceptionWithNewMessage, new Object[] { theStackTraceAsObject });
/*      */       }
/*      */     }
/*      */     catch (NoClassDefFoundError noClassDefFound) {}catch (NoSuchMethodException noSuchMethodEx) {}catch (Throwable catchAll) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  372 */     return sqlExceptionWithNewMessage;
/*      */   }
/*      */   
/*      */   public Timer getCancelTimer() {
/*  376 */     synchronized (getConnectionMutex()) {
/*  377 */       if (this.cancelTimer == null) {
/*  378 */         boolean createdNamedTimer = false;
/*      */         
/*      */ 
/*      */         try
/*      */         {
/*  383 */           Constructor<Timer> ctr = Timer.class.getConstructor(new Class[] { String.class, Boolean.TYPE });
/*      */           
/*  385 */           this.cancelTimer = ((Timer)ctr.newInstance(new Object[] { "MySQL Statement Cancellation Timer", Boolean.TRUE }));
/*  386 */           createdNamedTimer = true;
/*      */         } catch (Throwable t) {
/*  388 */           createdNamedTimer = false;
/*      */         }
/*      */         
/*  391 */         if (!createdNamedTimer) {
/*  392 */           this.cancelTimer = new Timer(true);
/*      */         }
/*      */       }
/*      */       
/*  396 */       return this.cancelTimer;
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
/*      */   protected static Connection getInstance(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url)
/*      */     throws SQLException
/*      */   {
/*  411 */     if (!Util.isJdbc4()) {
/*  412 */       return new ConnectionImpl(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
/*      */     }
/*      */     
/*      */ 
/*  416 */     return (Connection)Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[] { hostToConnectTo, Integer.valueOf(portToConnectTo), info, databaseToConnectTo, url }, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  422 */   private static final Random random = new Random();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static synchronized int getNextRoundRobinHostIndex(String url, List<?> hostList)
/*      */   {
/*  435 */     int indexRange = hostList.size();
/*      */     
/*  437 */     int index = random.nextInt(indexRange);
/*      */     
/*  439 */     return index;
/*      */   }
/*      */   
/*      */   private static boolean nullSafeCompare(String s1, String s2) {
/*  443 */     if ((s1 == null) && (s2 == null)) {
/*  444 */       return true;
/*      */     }
/*      */     
/*  447 */     if ((s1 == null) && (s2 != null)) {
/*  448 */       return false;
/*      */     }
/*      */     
/*  451 */     return (s1 != null) && (s1.equals(s2));
/*      */   }
/*      */   
/*      */ 
/*  455 */   private boolean autoCommit = true;
/*      */   
/*      */ 
/*      */ 
/*      */   private CacheAdapter<String, PreparedStatement.ParseInfo> cachedPreparedStatementParams;
/*      */   
/*      */ 
/*      */ 
/*  463 */   private String characterSetMetadata = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  469 */   private String characterSetResultsOnServer = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  476 */   private Map<String, Object> charsetConverterMap = new HashMap(CharsetMapping.getNumberOfCharsetsConfigured());
/*      */   
/*      */ 
/*      */ 
/*  480 */   private long connectionCreationTimeMillis = 0L;
/*      */   
/*      */ 
/*      */   private long connectionId;
/*      */   
/*      */ 
/*  486 */   private String database = null;
/*      */   
/*      */ 
/*  489 */   private java.sql.DatabaseMetaData dbmd = null;
/*      */   
/*      */ 
/*      */   private TimeZone defaultTimeZone;
/*      */   
/*      */ 
/*      */   private ProfilerEventHandler eventSink;
/*      */   
/*      */ 
/*      */   private Throwable forceClosedReason;
/*      */   
/*  500 */   private boolean hasIsolationLevels = false;
/*      */   
/*      */ 
/*  503 */   private boolean hasQuotedIdentifiers = false;
/*      */   
/*      */ 
/*  506 */   private String host = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  512 */   public Map<Integer, String> indexToJavaCharset = new HashMap();
/*      */   
/*  514 */   public Map<Integer, String> indexToCustomMysqlCharset = null;
/*      */   
/*  516 */   private Map<String, Integer> mysqlCharsetToCustomMblen = null;
/*      */   
/*      */ 
/*  519 */   private transient MysqlIO io = null;
/*      */   
/*  521 */   private boolean isClientTzUTC = false;
/*      */   
/*      */ 
/*  524 */   private boolean isClosed = true;
/*      */   
/*      */ 
/*  527 */   private boolean isInGlobalTx = false;
/*      */   
/*      */ 
/*  530 */   private boolean isRunningOnJDK13 = false;
/*      */   
/*      */ 
/*  533 */   private int isolationLevel = 2;
/*      */   
/*  535 */   private boolean isServerTzUTC = false;
/*      */   
/*      */ 
/*  538 */   private long lastQueryFinishedTime = 0L;
/*      */   
/*      */ 
/*  541 */   private transient Log log = NULL_LOGGER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  547 */   private long longestQueryTimeMs = 0L;
/*      */   
/*      */ 
/*  550 */   private boolean lowerCaseTableNames = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  555 */   private long maximumNumberTablesAccessed = 0L;
/*      */   
/*      */ 
/*  558 */   private boolean maxRowsChanged = false;
/*      */   
/*      */ 
/*      */   private long metricsLastReportedMs;
/*      */   
/*  563 */   private long minimumNumberTablesAccessed = Long.MAX_VALUE;
/*      */   
/*      */ 
/*  566 */   private String myURL = null;
/*      */   
/*      */ 
/*  569 */   private boolean needsPing = false;
/*      */   
/*  571 */   private int netBufferLength = 16384;
/*      */   
/*  573 */   private boolean noBackslashEscapes = false;
/*      */   
/*  575 */   private long numberOfPreparedExecutes = 0L;
/*      */   
/*  577 */   private long numberOfPrepares = 0L;
/*      */   
/*  579 */   private long numberOfQueriesIssued = 0L;
/*      */   
/*  581 */   private long numberOfResultSetsCreated = 0L;
/*      */   
/*      */   private long[] numTablesMetricsHistBreakpoints;
/*      */   
/*      */   private int[] numTablesMetricsHistCounts;
/*      */   
/*  587 */   private long[] oldHistBreakpoints = null;
/*      */   
/*  589 */   private int[] oldHistCounts = null;
/*      */   
/*      */ 
/*      */   private Map<Statement, Statement> openStatements;
/*      */   
/*      */   private LRUCache parsedCallableStatementCache;
/*      */   
/*  596 */   private boolean parserKnowsUnicode = false;
/*      */   
/*      */ 
/*  599 */   private String password = null;
/*      */   
/*      */ 
/*      */   private long[] perfMetricsHistBreakpoints;
/*      */   
/*      */ 
/*      */   private int[] perfMetricsHistCounts;
/*      */   
/*      */   private String pointOfOrigin;
/*      */   
/*  609 */   private int port = 3306;
/*      */   
/*      */ 
/*  612 */   protected Properties props = null;
/*      */   
/*      */ 
/*  615 */   private boolean readInfoMsg = false;
/*      */   
/*      */ 
/*  618 */   private boolean readOnly = false;
/*      */   
/*      */ 
/*      */   protected LRUCache resultSetMetadataCache;
/*      */   
/*      */ 
/*  624 */   private TimeZone serverTimezoneTZ = null;
/*      */   
/*      */ 
/*  627 */   private Map<String, String> serverVariables = null;
/*      */   
/*  629 */   private long shortestQueryTimeMs = Long.MAX_VALUE;
/*      */   
/*      */ 
/*      */   private Map<Statement, Statement> statementsUsingMaxRows;
/*      */   
/*  634 */   private double totalQueryTimeMs = 0.0D;
/*      */   
/*      */ 
/*  637 */   private boolean transactionsSupported = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map<String, Class<?>> typeMap;
/*      */   
/*      */ 
/*      */ 
/*  646 */   private boolean useAnsiQuotes = false;
/*      */   
/*      */ 
/*  649 */   private String user = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  655 */   private boolean useServerPreparedStmts = false;
/*      */   
/*      */   private LRUCache serverSideStatementCheckCache;
/*      */   
/*      */   private LRUCache serverSideStatementCache;
/*      */   
/*      */   private Calendar sessionCalendar;
/*      */   
/*      */   private Calendar utcCalendar;
/*      */   
/*      */   private String origHostToConnectTo;
/*      */   
/*      */   private int origPortToConnectTo;
/*      */   
/*      */   private String origDatabaseToConnectTo;
/*      */   
/*  671 */   private String errorMessageEncoding = "Cp1252";
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean usePlatformCharsetConverters;
/*      */   
/*      */ 
/*  678 */   private boolean hasTriedMasterFlag = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  684 */   private String statementComment = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean storesLowerCaseTableName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<StatementInterceptorV2> statementInterceptors;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean requiresEscapingEncoder;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String hostPortPair;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ConnectionImpl(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url)
/*      */     throws SQLException
/*      */   {
/*  726 */     this.connectionCreationTimeMillis = System.currentTimeMillis();
/*      */     
/*  728 */     if (databaseToConnectTo == null) {
/*  729 */       databaseToConnectTo = "";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  736 */     this.origHostToConnectTo = hostToConnectTo;
/*  737 */     this.origPortToConnectTo = portToConnectTo;
/*  738 */     this.origDatabaseToConnectTo = databaseToConnectTo;
/*      */     try
/*      */     {
/*  741 */       Blob.class.getMethod("truncate", new Class[] { Long.TYPE });
/*      */       
/*  743 */       this.isRunningOnJDK13 = false;
/*      */     } catch (NoSuchMethodException nsme) {
/*  745 */       this.isRunningOnJDK13 = true;
/*      */     }
/*      */     
/*  748 */     this.sessionCalendar = new GregorianCalendar();
/*  749 */     this.utcCalendar = new GregorianCalendar();
/*  750 */     this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  762 */     this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
/*      */     
/*      */ 
/*      */ 
/*  766 */     this.defaultTimeZone = Util.getDefaultTimeZone();
/*      */     
/*  768 */     if ("GMT".equalsIgnoreCase(this.defaultTimeZone.getID())) {
/*  769 */       this.isClientTzUTC = true;
/*      */     } else {
/*  771 */       this.isClientTzUTC = false;
/*      */     }
/*      */     
/*  774 */     this.openStatements = new HashMap();
/*      */     
/*  776 */     if (NonRegisteringDriver.isHostPropertiesList(hostToConnectTo)) {
/*  777 */       Properties hostSpecificProps = NonRegisteringDriver.expandHostKeyValues(hostToConnectTo);
/*      */       
/*  779 */       Enumeration<?> propertyNames = hostSpecificProps.propertyNames();
/*      */       
/*  781 */       while (propertyNames.hasMoreElements()) {
/*  782 */         String propertyName = propertyNames.nextElement().toString();
/*  783 */         String propertyValue = hostSpecificProps.getProperty(propertyName);
/*      */         
/*  785 */         info.setProperty(propertyName, propertyValue);
/*      */       }
/*      */       
/*      */     }
/*  789 */     else if (hostToConnectTo == null) {
/*  790 */       this.host = "localhost";
/*  791 */       this.hostPortPair = (this.host + ":" + portToConnectTo);
/*      */     } else {
/*  793 */       this.host = hostToConnectTo;
/*      */       
/*  795 */       if (hostToConnectTo.indexOf(":") == -1) {
/*  796 */         this.hostPortPair = (this.host + ":" + portToConnectTo);
/*      */       } else {
/*  798 */         this.hostPortPair = this.host;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  803 */     this.port = portToConnectTo;
/*      */     
/*  805 */     this.database = databaseToConnectTo;
/*  806 */     this.myURL = url;
/*  807 */     this.user = info.getProperty("user");
/*  808 */     this.password = info.getProperty("password");
/*      */     
/*      */ 
/*  811 */     if ((this.user == null) || (this.user.equals(""))) {
/*  812 */       this.user = "";
/*      */     }
/*      */     
/*  815 */     if (this.password == null) {
/*  816 */       this.password = "";
/*      */     }
/*      */     
/*  819 */     this.props = info;
/*      */     
/*      */ 
/*      */ 
/*  823 */     initializeDriverProperties(info);
/*      */     
/*  825 */     if (getUseUsageAdvisor()) {
/*  826 */       this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
/*      */     } else {
/*  828 */       this.pointOfOrigin = "";
/*      */     }
/*      */     try
/*      */     {
/*  832 */       this.dbmd = getMetaData(false, false);
/*  833 */       initializeSafeStatementInterceptors();
/*  834 */       createNewIO(false);
/*  835 */       unSafeStatementInterceptors();
/*      */     } catch (SQLException ex) {
/*  837 */       cleanup(ex);
/*      */       
/*      */ 
/*  840 */       throw ex;
/*      */     } catch (Exception ex) {
/*  842 */       cleanup(ex);
/*      */       
/*  844 */       StringBuffer mesg = new StringBuffer(128);
/*      */       
/*  846 */       if (!getParanoid()) {
/*  847 */         mesg.append("Cannot connect to MySQL server on ");
/*  848 */         mesg.append(this.host);
/*  849 */         mesg.append(":");
/*  850 */         mesg.append(this.port);
/*  851 */         mesg.append(".\n\n");
/*  852 */         mesg.append("Make sure that there is a MySQL server ");
/*  853 */         mesg.append("running on the machine/port you are trying ");
/*  854 */         mesg.append("to connect to and that the machine this software is running on ");
/*      */         
/*      */ 
/*  857 */         mesg.append("is able to connect to this host/port (i.e. not firewalled). ");
/*      */         
/*  859 */         mesg.append("Also make sure that the server has not been started with the --skip-networking ");
/*      */         
/*      */ 
/*  862 */         mesg.append("flag.\n\n");
/*      */       } else {
/*  864 */         mesg.append("Unable to connect to database.");
/*      */       }
/*      */       
/*  867 */       SQLException sqlEx = SQLError.createSQLException(mesg.toString(), "08S01", getExceptionInterceptor());
/*      */       
/*      */ 
/*  870 */       sqlEx.initCause(ex);
/*      */       
/*  872 */       throw sqlEx;
/*      */     }
/*      */     
/*  875 */     NonRegisteringDriver.trackConnection(this);
/*      */   }
/*      */   
/*      */   public void unSafeStatementInterceptors() throws SQLException
/*      */   {
/*  880 */     ArrayList<StatementInterceptorV2> unSafedStatementInterceptors = new ArrayList(this.statementInterceptors.size());
/*      */     
/*  882 */     for (int i = 0; i < this.statementInterceptors.size(); i++) {
/*  883 */       NoSubInterceptorWrapper wrappedInterceptor = (NoSubInterceptorWrapper)this.statementInterceptors.get(i);
/*      */       
/*  885 */       unSafedStatementInterceptors.add(wrappedInterceptor.getUnderlyingInterceptor());
/*      */     }
/*      */     
/*  888 */     this.statementInterceptors = unSafedStatementInterceptors;
/*      */     
/*  890 */     if (this.io != null) {
/*  891 */       this.io.setStatementInterceptors(this.statementInterceptors);
/*      */     }
/*      */   }
/*      */   
/*      */   public void initializeSafeStatementInterceptors() throws SQLException {
/*  896 */     this.isClosed = false;
/*      */     
/*  898 */     List<Extension> unwrappedInterceptors = Util.loadExtensions(this, this.props, getStatementInterceptors(), "MysqlIo.BadStatementInterceptor", getExceptionInterceptor());
/*      */     
/*      */ 
/*      */ 
/*  902 */     this.statementInterceptors = new ArrayList(unwrappedInterceptors.size());
/*      */     
/*  904 */     for (int i = 0; i < unwrappedInterceptors.size(); i++) {
/*  905 */       Extension interceptor = (Extension)unwrappedInterceptors.get(i);
/*      */       
/*      */ 
/*      */ 
/*  909 */       if ((interceptor instanceof StatementInterceptor)) {
/*  910 */         if (ReflectiveStatementInterceptorAdapter.getV2PostProcessMethod(interceptor.getClass()) != null) {
/*  911 */           this.statementInterceptors.add(new NoSubInterceptorWrapper(new ReflectiveStatementInterceptorAdapter((StatementInterceptor)interceptor)));
/*      */         } else {
/*  913 */           this.statementInterceptors.add(new NoSubInterceptorWrapper(new V1toV2StatementInterceptorAdapter((StatementInterceptor)interceptor)));
/*      */         }
/*      */       } else {
/*  916 */         this.statementInterceptors.add(new NoSubInterceptorWrapper((StatementInterceptorV2)interceptor));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public List<StatementInterceptorV2> getStatementInterceptorsInstances()
/*      */   {
/*  924 */     return this.statementInterceptors;
/*      */   }
/*      */   
/*      */ 
/*      */   private void addToHistogram(int[] histogramCounts, long[] histogramBreakpoints, long value, int numberOfTimes, long currentLowerBound, long currentUpperBound)
/*      */   {
/*  930 */     if (histogramCounts == null) {
/*  931 */       createInitialHistogram(histogramBreakpoints, currentLowerBound, currentUpperBound);
/*      */     }
/*      */     else {
/*  934 */       for (int i = 0; i < 20; i++) {
/*  935 */         if (histogramBreakpoints[i] >= value) {
/*  936 */           histogramCounts[i] += numberOfTimes;
/*      */           
/*  938 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToPerformanceHistogram(long value, int numberOfTimes) {
/*  945 */     checkAndCreatePerformanceHistogram();
/*      */     
/*  947 */     addToHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, value, numberOfTimes, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addToTablesAccessedHistogram(long value, int numberOfTimes)
/*      */   {
/*  954 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/*  956 */     addToHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, value, numberOfTimes, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void buildCollationMapping()
/*      */     throws SQLException
/*      */   {
/*  972 */     Map<Integer, String> javaCharset = null;
/*  973 */     Map<Long, String> sortedCollationMap = null;
/*  974 */     Map<Integer, String> customCharset = null;
/*  975 */     Map<String, Integer> customMblen = null;
/*      */     
/*  977 */     if (getCacheServerConfiguration()) {
/*  978 */       synchronized (serverJavaCharsetByUrl) {
/*  979 */         javaCharset = (Map)serverJavaCharsetByUrl.get(getURL());
/*  980 */         sortedCollationMap = (Map)serverCollationByUrl.get(getURL());
/*  981 */         customCharset = (Map)serverCustomCharsetByUrl.get(getURL());
/*  982 */         customMblen = (Map)serverCustomMblenByUrl.get(getURL());
/*      */       }
/*      */     }
/*      */     
/*  986 */     if (javaCharset == null) {
/*  987 */       javaCharset = new HashMap();
/*      */       
/*  989 */       if ((versionMeetsMinimum(4, 1, 0)) && (getDetectCustomCollations()))
/*      */       {
/*  991 */         java.sql.Statement stmt = null;
/*  992 */         ResultSet results = null;
/*      */         try
/*      */         {
/*  995 */           sortedCollationMap = new TreeMap();
/*  996 */           customCharset = new HashMap();
/*  997 */           customMblen = new HashMap();
/*      */           
/*  999 */           stmt = getMetadataSafeStatement();
/*      */           try
/*      */           {
/* 1002 */             results = stmt.executeQuery("SHOW COLLATION");
/* 1003 */             if (versionMeetsMinimum(5, 0, 0)) {
/* 1004 */               Util.resultSetToMap(sortedCollationMap, results, 3, 2);
/*      */             } else {
/* 1006 */               while (results.next()) {
/* 1007 */                 sortedCollationMap.put(Long.valueOf(results.getLong(3)), results.getString(2));
/*      */               }
/*      */             }
/*      */           } catch (SQLException ex) {
/* 1011 */             if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 1012 */               throw ex;
/*      */             }
/*      */           }
/*      */           
/* 1016 */           for (Iterator<Map.Entry<Long, String>> indexIter = sortedCollationMap.entrySet().iterator(); indexIter.hasNext();) {
/* 1017 */             Map.Entry<Long, String> indexEntry = (Map.Entry)indexIter.next();
/*      */             
/* 1019 */             int collationIndex = ((Long)indexEntry.getKey()).intValue();
/* 1020 */             String charsetName = (String)indexEntry.getValue();
/*      */             
/* 1022 */             javaCharset.put(Integer.valueOf(collationIndex), getJavaEncodingForMysqlEncoding(charsetName));
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1027 */             if ((collationIndex >= 255) || (!charsetName.equals(CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(Integer.valueOf(collationIndex)))))
/*      */             {
/* 1029 */               customCharset.put(Integer.valueOf(collationIndex), charsetName);
/*      */             }
/*      */             
/*      */ 
/* 1033 */             if ((!CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP.containsKey(charsetName)) && (!CharsetMapping.STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP.containsKey(charsetName)))
/*      */             {
/* 1035 */               customMblen.put(charsetName, null);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1040 */           if (customMblen.size() > 0) {
/*      */             try {
/* 1042 */               results = stmt.executeQuery("SHOW CHARACTER SET");
/* 1043 */               while (results.next()) {
/* 1044 */                 String charsetName = results.getString("Charset");
/* 1045 */                 if (customMblen.containsKey(charsetName)) {
/* 1046 */                   customMblen.put(charsetName, Integer.valueOf(results.getInt("Maxlen")));
/*      */                 }
/*      */               }
/*      */             } catch (SQLException ex) {
/* 1050 */               if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 1051 */                 throw ex;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 1056 */           if (getCacheServerConfiguration()) {
/* 1057 */             synchronized (serverJavaCharsetByUrl) {
/* 1058 */               serverJavaCharsetByUrl.put(getURL(), javaCharset);
/* 1059 */               serverCollationByUrl.put(getURL(), sortedCollationMap);
/* 1060 */               serverCustomCharsetByUrl.put(getURL(), customCharset);
/* 1061 */               serverCustomMblenByUrl.put(getURL(), customMblen);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (SQLException ex)
/*      */         {
/* 1067 */           throw ex;
/*      */         } catch (RuntimeException ex) {
/* 1069 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 1070 */           sqlEx.initCause(ex);
/* 1071 */           throw sqlEx;
/*      */         } finally {
/* 1073 */           if (results != null) {
/*      */             try {
/* 1075 */               results.close();
/*      */             }
/*      */             catch (SQLException sqlE) {}
/*      */           }
/*      */           
/*      */ 
/* 1081 */           if (stmt != null) {
/*      */             try {
/* 1083 */               stmt.close();
/*      */             }
/*      */             catch (SQLException sqlE) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 1090 */         for (int i = 0; i < CharsetMapping.INDEX_TO_CHARSET.length; i++) {
/* 1091 */           javaCharset.put(Integer.valueOf(i), getJavaEncodingForMysqlEncoding((String)CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(Integer.valueOf(i))));
/*      */         }
/* 1093 */         if (getCacheServerConfiguration()) {
/* 1094 */           synchronized (serverJavaCharsetByUrl) {
/* 1095 */             serverJavaCharsetByUrl.put(getURL(), javaCharset);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1103 */     this.indexToJavaCharset = Collections.unmodifiableMap(javaCharset);
/* 1104 */     if (customCharset != null) {
/* 1105 */       this.indexToCustomMysqlCharset = Collections.unmodifiableMap(customCharset);
/*      */     }
/* 1107 */     if (customMblen != null) {
/* 1108 */       this.mysqlCharsetToCustomMblen = Collections.unmodifiableMap(customMblen);
/*      */     }
/*      */   }
/*      */   
/*      */   public String getJavaEncodingForMysqlEncoding(String mysqlEncoding) throws SQLException
/*      */   {
/* 1114 */     if ((versionMeetsMinimum(4, 1, 0)) && ("latin1".equalsIgnoreCase(mysqlEncoding))) {
/* 1115 */       return "Cp1252";
/*      */     }
/*      */     
/* 1118 */     return (String)CharsetMapping.MYSQL_TO_JAVA_CHARSET_MAP.get(mysqlEncoding);
/*      */   }
/*      */   
/*      */   private boolean canHandleAsServerPreparedStatement(String sql) throws SQLException
/*      */   {
/* 1123 */     if ((sql == null) || (sql.length() == 0)) {
/* 1124 */       return true;
/*      */     }
/*      */     
/* 1127 */     if (!this.useServerPreparedStmts) {
/* 1128 */       return false;
/*      */     }
/*      */     
/* 1131 */     if (getCachePreparedStatements()) {
/* 1132 */       synchronized (this.serverSideStatementCheckCache) {
/* 1133 */         Boolean flag = (Boolean)this.serverSideStatementCheckCache.get(sql);
/*      */         
/* 1135 */         if (flag != null) {
/* 1136 */           return flag.booleanValue();
/*      */         }
/*      */         
/* 1139 */         boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);
/*      */         
/* 1141 */         if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 1142 */           this.serverSideStatementCheckCache.put(sql, canHandle ? Boolean.TRUE : Boolean.FALSE);
/*      */         }
/*      */         
/*      */ 
/* 1146 */         return canHandle;
/*      */       }
/*      */     }
/*      */     
/* 1150 */     return canHandleAsServerPreparedStatementNoCache(sql);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean canHandleAsServerPreparedStatementNoCache(String sql)
/*      */     throws SQLException
/*      */   {
/* 1157 */     if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
/* 1158 */       return false;
/*      */     }
/*      */     
/* 1161 */     boolean canHandleAsStatement = true;
/*      */     
/* 1163 */     if ((!versionMeetsMinimum(5, 0, 7)) && ((StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "DELETE")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "INSERT")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "UPDATE")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "REPLACE"))))
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1181 */       int currentPos = 0;
/* 1182 */       int statementLength = sql.length();
/* 1183 */       int lastPosToLook = statementLength - 7;
/* 1184 */       boolean allowBackslashEscapes = !this.noBackslashEscapes;
/* 1185 */       char quoteChar = this.useAnsiQuotes ? '"' : '\'';
/* 1186 */       boolean foundLimitWithPlaceholder = false;
/*      */       
/* 1188 */       while (currentPos < lastPosToLook) {
/* 1189 */         int limitStart = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, sql, "LIMIT ", quoteChar, allowBackslashEscapes);
/*      */         
/*      */ 
/*      */ 
/* 1193 */         if (limitStart == -1) {
/*      */           break;
/*      */         }
/*      */         
/* 1197 */         currentPos = limitStart + 7;
/*      */         
/* 1199 */         while (currentPos < statementLength) {
/* 1200 */           char c = sql.charAt(currentPos);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1207 */           if ((!Character.isDigit(c)) && (!Character.isWhitespace(c)) && (c != ',') && (c != '?')) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/* 1212 */           if (c == '?') {
/* 1213 */             foundLimitWithPlaceholder = true;
/* 1214 */             break;
/*      */           }
/*      */           
/* 1217 */           currentPos++;
/*      */         }
/*      */       }
/*      */       
/* 1221 */       canHandleAsStatement = !foundLimitWithPlaceholder;
/* 1222 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
/* 1223 */       canHandleAsStatement = false;
/* 1224 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "DO")) {
/* 1225 */       canHandleAsStatement = false;
/* 1226 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
/* 1227 */       canHandleAsStatement = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1232 */     return canHandleAsStatement;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void changeUser(String userName, String newPassword)
/*      */     throws SQLException
/*      */   {
/* 1250 */     synchronized (getConnectionMutex()) {
/* 1251 */       checkClosed();
/*      */       
/* 1253 */       if ((userName == null) || (userName.equals(""))) {
/* 1254 */         userName = "";
/*      */       }
/*      */       
/* 1257 */       if (newPassword == null) {
/* 1258 */         newPassword = "";
/*      */       }
/*      */       try
/*      */       {
/* 1262 */         this.io.changeUser(userName, newPassword, this.database);
/*      */       } catch (SQLException ex) {
/* 1264 */         if ((versionMeetsMinimum(5, 6, 13)) && ("28000".equals(ex.getSQLState()))) {
/* 1265 */           cleanup(ex);
/*      */         }
/* 1267 */         throw ex;
/*      */       }
/* 1269 */       this.user = userName;
/* 1270 */       this.password = newPassword;
/*      */       
/* 1272 */       if (versionMeetsMinimum(4, 1, 0)) {
/* 1273 */         configureClientCharacterSet(true);
/*      */       }
/*      */       
/* 1276 */       setSessionVariables();
/*      */       
/* 1278 */       setupServerForTruncationChecks();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean characterSetNamesMatches(String mysqlEncodingName)
/*      */   {
/* 1285 */     return (mysqlEncodingName != null) && (mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_client"))) && (mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_connection")));
/*      */   }
/*      */   
/*      */ 
/*      */   private void checkAndCreatePerformanceHistogram()
/*      */   {
/* 1291 */     if (this.perfMetricsHistCounts == null) {
/* 1292 */       this.perfMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1295 */     if (this.perfMetricsHistBreakpoints == null) {
/* 1296 */       this.perfMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkAndCreateTablesAccessedHistogram() {
/* 1301 */     if (this.numTablesMetricsHistCounts == null) {
/* 1302 */       this.numTablesMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1305 */     if (this.numTablesMetricsHistBreakpoints == null) {
/* 1306 */       this.numTablesMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkClosed() throws SQLException {
/* 1311 */     if (this.isClosed) {
/* 1312 */       throwConnectionClosedException();
/*      */     }
/*      */   }
/*      */   
/*      */   public void throwConnectionClosedException() throws SQLException {
/* 1317 */     StringBuffer messageBuf = new StringBuffer("No operations allowed after connection closed.");
/*      */     
/*      */ 
/* 1320 */     SQLException ex = SQLError.createSQLException(messageBuf.toString(), "08003", getExceptionInterceptor());
/*      */     
/*      */ 
/* 1323 */     if (this.forceClosedReason != null) {
/* 1324 */       ex.initCause(this.forceClosedReason);
/*      */     }
/*      */     
/* 1327 */     throw ex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkServerEncoding()
/*      */     throws SQLException
/*      */   {
/* 1338 */     if ((getUseUnicode()) && (getEncoding() != null))
/*      */     {
/* 1340 */       return;
/*      */     }
/*      */     
/* 1343 */     String serverEncoding = (String)this.serverVariables.get("character_set");
/*      */     
/* 1345 */     if (serverEncoding == null)
/*      */     {
/* 1347 */       serverEncoding = (String)this.serverVariables.get("character_set_server");
/*      */     }
/*      */     
/* 1350 */     String mappedServerEncoding = null;
/*      */     
/* 1352 */     if (serverEncoding != null) {
/*      */       try {
/* 1354 */         mappedServerEncoding = getJavaEncodingForMysqlEncoding(serverEncoding.toUpperCase(Locale.ENGLISH));
/*      */       }
/*      */       catch (SQLException ex) {
/* 1357 */         throw ex;
/*      */       } catch (RuntimeException ex) {
/* 1359 */         SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 1360 */         sqlEx.initCause(ex);
/* 1361 */         throw sqlEx;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1368 */     if ((!getUseUnicode()) && (mappedServerEncoding != null)) {
/* 1369 */       SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);
/*      */       
/* 1371 */       if (converter != null) {
/* 1372 */         setUseUnicode(true);
/* 1373 */         setEncoding(mappedServerEncoding);
/*      */         
/* 1375 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1383 */     if (serverEncoding != null) {
/* 1384 */       if (mappedServerEncoding == null)
/*      */       {
/*      */ 
/* 1387 */         if (Character.isLowerCase(serverEncoding.charAt(0))) {
/* 1388 */           char[] ach = serverEncoding.toCharArray();
/* 1389 */           ach[0] = Character.toUpperCase(serverEncoding.charAt(0));
/* 1390 */           setEncoding(new String(ach));
/*      */         }
/*      */       }
/*      */       
/* 1394 */       if (mappedServerEncoding == null) {
/* 1395 */         throw SQLError.createSQLException("Unknown character encoding on server '" + serverEncoding + "', use 'characterEncoding=' property " + " to provide correct mapping", "01S00", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1407 */         StringUtils.getBytes("abc", mappedServerEncoding);
/* 1408 */         setEncoding(mappedServerEncoding);
/* 1409 */         setUseUnicode(true);
/*      */       } catch (UnsupportedEncodingException UE) {
/* 1411 */         throw SQLError.createSQLException("The driver can not map the character encoding '" + getEncoding() + "' that your server is using " + "to a character encoding your JVM understands. You " + "can specify this mapping manually by adding \"useUnicode=true\" " + "as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" " + "to your JDBC URL.", "0S100", getExceptionInterceptor());
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
/*      */   private void checkTransactionIsolationLevel()
/*      */     throws SQLException
/*      */   {
/* 1431 */     String txIsolationName = null;
/*      */     
/* 1433 */     if (versionMeetsMinimum(4, 0, 3)) {
/* 1434 */       txIsolationName = "tx_isolation";
/*      */     } else {
/* 1436 */       txIsolationName = "transaction_isolation";
/*      */     }
/*      */     
/* 1439 */     String s = (String)this.serverVariables.get(txIsolationName);
/*      */     
/* 1441 */     if (s != null) {
/* 1442 */       Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */       
/* 1444 */       if (intTI != null) {
/* 1445 */         this.isolationLevel = intTI.intValue();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void abortInternal()
/*      */     throws SQLException
/*      */   {
/* 1457 */     if (this.io != null) {
/*      */       try {
/* 1459 */         this.io.forceClose();
/*      */       }
/*      */       catch (Throwable t) {}
/*      */       
/* 1463 */       this.io.releaseResources();
/* 1464 */       this.io = null;
/*      */     }
/*      */     
/* 1467 */     this.isClosed = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void cleanup(Throwable whyCleanedUp)
/*      */   {
/*      */     try
/*      */     {
/* 1480 */       if (this.io != null) {
/* 1481 */         if (isClosed()) {
/* 1482 */           this.io.forceClose();
/*      */         } else {
/* 1484 */           realClose(false, false, false, whyCleanedUp);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx) {}
/*      */     
/*      */ 
/*      */ 
/* 1492 */     this.isClosed = true;
/*      */   }
/*      */   
/*      */   public void clearHasTriedMaster() {
/* 1496 */     this.hasTriedMasterFlag = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 1521 */     return clientPrepareStatement(sql, 1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 1531 */     java.sql.PreparedStatement pStmt = clientPrepareStatement(sql);
/*      */     
/* 1533 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
/*      */     
/*      */ 
/* 1536 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 1554 */     return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, boolean processEscapeCodesIfNeeded)
/*      */     throws SQLException
/*      */   {
/* 1562 */     checkClosed();
/*      */     
/* 1564 */     String nativeSql = (processEscapeCodesIfNeeded) && (getProcessEscapeCodesForPrepStmts()) ? nativeSQL(sql) : sql;
/*      */     
/* 1566 */     PreparedStatement pStmt = null;
/*      */     
/* 1568 */     if (getCachePreparedStatements()) {
/* 1569 */       PreparedStatement.ParseInfo pStmtInfo = (PreparedStatement.ParseInfo)this.cachedPreparedStatementParams.get(nativeSql);
/*      */       
/* 1571 */       if (pStmtInfo == null) {
/* 1572 */         pStmt = PreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database);
/*      */         
/*      */ 
/* 1575 */         this.cachedPreparedStatementParams.put(nativeSql, pStmt.getParseInfo());
/*      */       }
/*      */       else {
/* 1578 */         pStmt = new PreparedStatement(getLoadBalanceSafeProxy(), nativeSql, this.database, pStmtInfo);
/*      */       }
/*      */     }
/*      */     else {
/* 1582 */       pStmt = PreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database);
/*      */     }
/*      */     
/*      */ 
/* 1586 */     pStmt.setResultSetType(resultSetType);
/* 1587 */     pStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 1589 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 1598 */     PreparedStatement pStmt = (PreparedStatement)clientPrepareStatement(sql);
/*      */     
/* 1600 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null) && (autoGenKeyIndexes.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 1604 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 1612 */     PreparedStatement pStmt = (PreparedStatement)clientPrepareStatement(sql);
/*      */     
/* 1614 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null) && (autoGenKeyColNames.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 1618 */     return pStmt;
/*      */   }
/*      */   
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 1624 */     return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
/*      */   }
/*      */   
/*      */ 
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
/* 1640 */     synchronized (getConnectionMutex()) {
/* 1641 */       if (this.connectionLifecycleInterceptors != null) {
/* 1642 */         new IterateBlock(this.connectionLifecycleInterceptors.iterator()) {
/*      */           void forEach(Extension each) throws SQLException {
/* 1644 */             ((ConnectionLifecycleInterceptor)each).close();
/*      */           }
/*      */         }.doForAll();
/*      */       }
/*      */       
/* 1649 */       realClose(true, true, false, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void closeAllOpenStatements()
/*      */     throws SQLException
/*      */   {
/* 1660 */     SQLException postponedException = null;
/*      */     
/* 1662 */     if (this.openStatements != null) {
/* 1663 */       List<Statement> currentlyOpenStatements = new ArrayList();
/*      */       
/*      */ 
/*      */ 
/* 1667 */       for (Iterator<Statement> iter = this.openStatements.keySet().iterator(); iter.hasNext();) {
/* 1668 */         currentlyOpenStatements.add(iter.next());
/*      */       }
/*      */       
/* 1671 */       int numStmts = currentlyOpenStatements.size();
/*      */       
/* 1673 */       for (int i = 0; i < numStmts; i++) {
/* 1674 */         StatementImpl stmt = (StatementImpl)currentlyOpenStatements.get(i);
/*      */         try
/*      */         {
/* 1677 */           stmt.realClose(false, true);
/*      */         } catch (SQLException sqlEx) {
/* 1679 */           postponedException = sqlEx;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1684 */       if (postponedException != null) {
/* 1685 */         throw postponedException;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void closeStatement(java.sql.Statement stmt) {
/* 1691 */     if (stmt != null) {
/*      */       try {
/* 1693 */         stmt.close();
/*      */       }
/*      */       catch (SQLException sqlEx) {}
/*      */       
/*      */ 
/* 1698 */       stmt = null;
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
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/* 1717 */     synchronized (getConnectionMutex()) {
/* 1718 */       checkClosed();
/*      */       try
/*      */       {
/* 1721 */         if (this.connectionLifecycleInterceptors != null) {
/* 1722 */           IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */           {
/*      */             void forEach(Extension each) throws SQLException {
/* 1725 */               if (!((ConnectionLifecycleInterceptor)each).commit()) {
/* 1726 */                 this.stopIterating = true;
/*      */               }
/*      */               
/*      */             }
/* 1730 */           };
/* 1731 */           iter.doForAll();
/*      */           
/* 1733 */           if (!iter.fullIteration()) {
/* 1734 */             jsr 136;return;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1739 */         if ((this.autoCommit) && (!getRelaxAutoCommit()))
/* 1740 */           throw SQLError.createSQLException("Can't call commit when autocommit=true", getExceptionInterceptor());
/* 1741 */         if (this.transactionsSupported) {
/* 1742 */           if ((getUseLocalTransactionState()) && (versionMeetsMinimum(5, 0, 0)) && 
/* 1743 */             (!this.io.inTransactionOnServer())) {
/* 1744 */             jsr 71;return;
/*      */           }
/*      */           
/*      */ 
/* 1748 */           execSQL(null, "commit", -1, null, 1003, 1007, false, this.database, null, false);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       catch (SQLException sqlException)
/*      */       {
/* 1755 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 1757 */           throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1764 */         throw sqlException;
/*      */       } finally {
/* 1766 */         jsr 5; } localObject2 = returnAddress;this.needsPing = getReconnectAtTxEnd();ret;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureCharsetProperties()
/*      */     throws SQLException
/*      */   {
/* 1779 */     if (getEncoding() != null)
/*      */     {
/*      */       try
/*      */       {
/* 1783 */         String testString = "abc";
/* 1784 */         StringUtils.getBytes(testString, getEncoding());
/*      */       }
/*      */       catch (UnsupportedEncodingException UE) {
/* 1787 */         String oldEncoding = getEncoding();
/*      */         try
/*      */         {
/* 1790 */           setEncoding(getJavaEncodingForMysqlEncoding(oldEncoding));
/*      */         } catch (SQLException ex) {
/* 1792 */           throw ex;
/*      */         } catch (RuntimeException ex) {
/* 1794 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 1795 */           sqlEx.initCause(ex);
/* 1796 */           throw sqlEx;
/*      */         }
/*      */         
/* 1799 */         if (getEncoding() == null) {
/* 1800 */           throw SQLError.createSQLException("Java does not support the MySQL character encoding  encoding '" + oldEncoding + "'.", "01S00", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1807 */           String testString = "abc";
/* 1808 */           StringUtils.getBytes(testString, getEncoding());
/*      */         } catch (UnsupportedEncodingException encodingEx) {
/* 1810 */           throw SQLError.createSQLException("Unsupported character encoding '" + getEncoding() + "'.", "01S00", getExceptionInterceptor());
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
/*      */   private boolean configureClientCharacterSet(boolean dontCheckServerMatch)
/*      */     throws SQLException
/*      */   {
/* 1832 */     String realJavaEncoding = getEncoding();
/* 1833 */     boolean characterSetAlreadyConfigured = false;
/*      */     try
/*      */     {
/* 1836 */       if (versionMeetsMinimum(4, 1, 0)) {
/* 1837 */         characterSetAlreadyConfigured = true;
/*      */         
/* 1839 */         setUseUnicode(true);
/*      */         
/* 1841 */         configureCharsetProperties();
/* 1842 */         realJavaEncoding = getEncoding();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1850 */           if ((this.props != null) && (this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null)) {
/* 1851 */             this.io.serverCharsetIndex = Integer.parseInt(this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex"));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1856 */           String serverEncodingToSet = CharsetMapping.INDEX_TO_CHARSET[this.io.serverCharsetIndex];
/*      */           
/*      */ 
/* 1859 */           if ((serverEncodingToSet == null) || (serverEncodingToSet.length() == 0)) {
/* 1860 */             if (realJavaEncoding != null)
/*      */             {
/* 1862 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 1864 */               throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1873 */           if ((versionMeetsMinimum(4, 1, 0)) && ("ISO8859_1".equalsIgnoreCase(serverEncodingToSet)))
/*      */           {
/* 1875 */             serverEncodingToSet = "Cp1252";
/*      */           }
/* 1877 */           if (("UnicodeBig".equalsIgnoreCase(serverEncodingToSet)) || ("UTF-16".equalsIgnoreCase(serverEncodingToSet)) || ("UTF-16LE".equalsIgnoreCase(serverEncodingToSet)) || ("UTF-32".equalsIgnoreCase(serverEncodingToSet)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1882 */             serverEncodingToSet = "UTF-8";
/*      */           }
/*      */           
/* 1885 */           setEncoding(serverEncodingToSet);
/*      */         }
/*      */         catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
/* 1888 */           if (realJavaEncoding != null)
/*      */           {
/* 1890 */             setEncoding(realJavaEncoding);
/*      */           } else {
/* 1892 */             throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         catch (SQLException ex)
/*      */         {
/* 1899 */           throw ex;
/*      */         } catch (RuntimeException ex) {
/* 1901 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 1902 */           sqlEx.initCause(ex);
/* 1903 */           throw sqlEx;
/*      */         }
/*      */         
/* 1906 */         if (getEncoding() == null)
/*      */         {
/* 1908 */           setEncoding("ISO8859_1");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1915 */         if (getUseUnicode()) {
/* 1916 */           if (realJavaEncoding != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1922 */             if ((realJavaEncoding.equalsIgnoreCase("UTF-8")) || (realJavaEncoding.equalsIgnoreCase("UTF8")))
/*      */             {
/*      */ 
/*      */ 
/* 1926 */               boolean utf8mb4Supported = versionMeetsMinimum(5, 5, 2);
/* 1927 */               boolean useutf8mb4 = false;
/*      */               
/* 1929 */               if (utf8mb4Supported) {
/* 1930 */                 useutf8mb4 = this.io.serverCharsetIndex == 45;
/*      */               }
/*      */               
/* 1933 */               if (!getUseOldUTF8Behavior()) {
/* 1934 */                 if ((dontCheckServerMatch) || (!characterSetNamesMatches("utf8")) || ((utf8mb4Supported) && (!characterSetNamesMatches("utf8mb4"))))
/*      */                 {
/* 1936 */                   execSQL(null, "SET NAMES " + (useutf8mb4 ? "utf8mb4" : "utf8"), -1, null, 1003, 1007, false, this.database, null, false);
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*      */               else {
/* 1942 */                 execSQL(null, "SET NAMES latin1", -1, null, 1003, 1007, false, this.database, null, false);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1948 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 1950 */               String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), this);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1965 */               if (mysqlEncodingName != null)
/*      */               {
/* 1967 */                 if ((dontCheckServerMatch) || (!characterSetNamesMatches(mysqlEncodingName))) {
/* 1968 */                   execSQL(null, "SET NAMES " + mysqlEncodingName, -1, null, 1003, 1007, false, this.database, null, false);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1979 */               setEncoding(realJavaEncoding);
/*      */             }
/* 1981 */           } else if (getEncoding() != null)
/*      */           {
/*      */ 
/*      */ 
/* 1985 */             String mysqlEncodingName = getServerCharacterEncoding();
/*      */             
/* 1987 */             if (getUseOldUTF8Behavior()) {
/* 1988 */               mysqlEncodingName = "latin1";
/*      */             }
/*      */             
/* 1991 */             boolean ucs2 = false;
/* 1992 */             if (("ucs2".equalsIgnoreCase(mysqlEncodingName)) || ("utf16".equalsIgnoreCase(mysqlEncodingName)) || ("utf16le".equalsIgnoreCase(mysqlEncodingName)) || ("utf32".equalsIgnoreCase(mysqlEncodingName)))
/*      */             {
/*      */ 
/*      */ 
/* 1996 */               mysqlEncodingName = "utf8";
/* 1997 */               ucs2 = true;
/* 1998 */               if (getCharacterSetResults() == null) {
/* 1999 */                 setCharacterSetResults("UTF-8");
/*      */               }
/*      */             }
/*      */             
/* 2003 */             if ((dontCheckServerMatch) || (!characterSetNamesMatches(mysqlEncodingName)) || (ucs2)) {
/*      */               try {
/* 2005 */                 execSQL(null, "SET NAMES " + mysqlEncodingName, -1, null, 1003, 1007, false, this.database, null, false);
/*      */ 
/*      */               }
/*      */               catch (SQLException ex)
/*      */               {
/* 2010 */                 if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 2011 */                   throw ex;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 2016 */             realJavaEncoding = getEncoding();
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
/* 2027 */         String onServer = null;
/* 2028 */         boolean isNullOnServer = false;
/*      */         
/* 2030 */         if (this.serverVariables != null) {
/* 2031 */           onServer = (String)this.serverVariables.get("character_set_results");
/*      */           
/* 2033 */           isNullOnServer = (onServer == null) || ("NULL".equalsIgnoreCase(onServer)) || (onServer.length() == 0);
/*      */         }
/*      */         
/* 2036 */         if (getCharacterSetResults() == null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2043 */           if (!isNullOnServer) {
/*      */             try {
/* 2045 */               execSQL(null, "SET character_set_results = NULL", -1, null, 1003, 1007, false, this.database, null, false);
/*      */ 
/*      */             }
/*      */             catch (SQLException ex)
/*      */             {
/*      */ 
/* 2051 */               if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 2052 */                 throw ex;
/*      */               }
/*      */             }
/* 2055 */             if (!this.usingCachedConfig) {
/* 2056 */               this.serverVariables.put("jdbc.local.character_set_results", null);
/*      */             }
/*      */           }
/* 2059 */           else if (!this.usingCachedConfig) {
/* 2060 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2065 */           if (getUseOldUTF8Behavior()) {
/*      */             try {
/* 2067 */               execSQL(null, "SET NAMES latin1", -1, null, 1003, 1007, false, this.database, null, false);
/*      */ 
/*      */             }
/*      */             catch (SQLException ex)
/*      */             {
/* 2072 */               if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 2073 */                 throw ex;
/*      */               }
/*      */             }
/*      */           }
/* 2077 */           String charsetResults = getCharacterSetResults();
/* 2078 */           String mysqlEncodingName = null;
/*      */           
/* 2080 */           if (("UTF-8".equalsIgnoreCase(charsetResults)) || ("UTF8".equalsIgnoreCase(charsetResults)))
/*      */           {
/* 2082 */             mysqlEncodingName = "utf8";
/* 2083 */           } else if ("null".equalsIgnoreCase(charsetResults)) {
/* 2084 */             mysqlEncodingName = "NULL";
/*      */           } else {
/* 2086 */             mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), this);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2095 */           if (mysqlEncodingName == null) {
/* 2096 */             throw SQLError.createSQLException("Can't map " + charsetResults + " given for characterSetResults to a supported MySQL encoding.", "S1009", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2101 */           if (!mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_results")))
/*      */           {
/* 2103 */             StringBuffer setBuf = new StringBuffer("SET character_set_results = ".length() + mysqlEncodingName.length());
/*      */             
/*      */ 
/* 2106 */             setBuf.append("SET character_set_results = ").append(mysqlEncodingName);
/*      */             
/*      */             try
/*      */             {
/* 2110 */               execSQL(null, setBuf.toString(), -1, null, 1003, 1007, false, this.database, null, false);
/*      */ 
/*      */             }
/*      */             catch (SQLException ex)
/*      */             {
/* 2115 */               if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 2116 */                 throw ex;
/*      */               }
/*      */             }
/*      */             
/* 2120 */             if (!this.usingCachedConfig) {
/* 2121 */               this.serverVariables.put("jdbc.local.character_set_results", mysqlEncodingName);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2127 */             if (versionMeetsMinimum(5, 5, 0)) {
/* 2128 */               this.errorMessageEncoding = charsetResults;
/*      */             }
/*      */             
/*      */           }
/* 2132 */           else if (!this.usingCachedConfig) {
/* 2133 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2138 */         if (getConnectionCollation() != null) {
/* 2139 */           StringBuffer setBuf = new StringBuffer("SET collation_connection = ".length() + getConnectionCollation().length());
/*      */           
/*      */ 
/* 2142 */           setBuf.append("SET collation_connection = ").append(getConnectionCollation());
/*      */           
/*      */           try
/*      */           {
/* 2146 */             execSQL(null, setBuf.toString(), -1, null, 1003, 1007, false, this.database, null, false);
/*      */ 
/*      */           }
/*      */           catch (SQLException ex)
/*      */           {
/* 2151 */             if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 2152 */               throw ex;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 2158 */         realJavaEncoding = getEncoding();
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/* 2166 */       setEncoding(realJavaEncoding);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 2174 */       CharsetEncoder enc = Charset.forName(getEncoding()).newEncoder();
/* 2175 */       CharBuffer cbuf = CharBuffer.allocate(1);
/* 2176 */       ByteBuffer bbuf = ByteBuffer.allocate(1);
/*      */       
/* 2178 */       cbuf.put("¥");
/* 2179 */       cbuf.position(0);
/* 2180 */       enc.encode(cbuf, bbuf, true);
/* 2181 */       if (bbuf.get(0) == 92) {
/* 2182 */         this.requiresEscapingEncoder = true;
/*      */       } else {
/* 2184 */         cbuf.clear();
/* 2185 */         bbuf.clear();
/*      */         
/* 2187 */         cbuf.put("₩");
/* 2188 */         cbuf.position(0);
/* 2189 */         enc.encode(cbuf, bbuf, true);
/* 2190 */         if (bbuf.get(0) == 92) {
/* 2191 */           this.requiresEscapingEncoder = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (UnsupportedCharsetException ucex) {
/*      */       try {
/* 2197 */         byte[] bbuf = StringUtils.getBytes("¥", getEncoding());
/* 2198 */         if (bbuf[0] == 92) {
/* 2199 */           this.requiresEscapingEncoder = true;
/*      */         } else {
/* 2201 */           bbuf = StringUtils.getBytes("₩", getEncoding());
/* 2202 */           if (bbuf[0] == 92) {
/* 2203 */             this.requiresEscapingEncoder = true;
/*      */           }
/*      */         }
/*      */       } catch (UnsupportedEncodingException ueex) {
/* 2207 */         throw SQLError.createSQLException("Unable to use encoding: " + getEncoding(), "S1000", ueex, getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2213 */     return characterSetAlreadyConfigured;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureTimezone()
/*      */     throws SQLException
/*      */   {
/* 2224 */     String configuredTimeZoneOnServer = (String)this.serverVariables.get("timezone");
/*      */     
/* 2226 */     if (configuredTimeZoneOnServer == null) {
/* 2227 */       configuredTimeZoneOnServer = (String)this.serverVariables.get("time_zone");
/*      */       
/* 2229 */       if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
/* 2230 */         configuredTimeZoneOnServer = (String)this.serverVariables.get("system_time_zone");
/*      */       }
/*      */     }
/*      */     
/* 2234 */     String canoncicalTimezone = getServerTimezone();
/*      */     
/* 2236 */     if (((getUseTimezone()) || (!getUseLegacyDatetimeCode())) && (configuredTimeZoneOnServer != null))
/*      */     {
/* 2238 */       if ((canoncicalTimezone == null) || (StringUtils.isEmptyOrWhitespaceOnly(canoncicalTimezone))) {
/*      */         try {
/* 2240 */           canoncicalTimezone = TimeUtil.getCanoncialTimezone(configuredTimeZoneOnServer, getExceptionInterceptor());
/*      */           
/*      */ 
/* 2243 */           if (canoncicalTimezone == null) {
/* 2244 */             throw SQLError.createSQLException("Can't map timezone '" + configuredTimeZoneOnServer + "' to " + " canonical timezone.", "S1009", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */         }
/*      */         catch (IllegalArgumentException iae)
/*      */         {
/* 2250 */           throw SQLError.createSQLException(iae.getMessage(), "S1000", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 2255 */       canoncicalTimezone = getServerTimezone();
/*      */     }
/*      */     
/* 2258 */     if ((canoncicalTimezone != null) && (canoncicalTimezone.length() > 0)) {
/* 2259 */       this.serverTimezoneTZ = TimeZone.getTimeZone(canoncicalTimezone);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2266 */       if ((!canoncicalTimezone.equalsIgnoreCase("GMT")) && (this.serverTimezoneTZ.getID().equals("GMT")))
/*      */       {
/* 2268 */         throw SQLError.createSQLException("No timezone mapping entry for '" + canoncicalTimezone + "'", "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2273 */       if ("GMT".equalsIgnoreCase(this.serverTimezoneTZ.getID())) {
/* 2274 */         this.isServerTzUTC = true;
/*      */       } else {
/* 2276 */         this.isServerTzUTC = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void createInitialHistogram(long[] breakpoints, long lowerBound, long upperBound)
/*      */   {
/* 2284 */     double bucketSize = (upperBound - lowerBound) / 20.0D * 1.25D;
/*      */     
/* 2286 */     if (bucketSize < 1.0D) {
/* 2287 */       bucketSize = 1.0D;
/*      */     }
/*      */     
/* 2290 */     for (int i = 0; i < 20; i++) {
/* 2291 */       breakpoints[i] = lowerBound;
/* 2292 */       lowerBound = (lowerBound + bucketSize);
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
/*      */   public void createNewIO(boolean isForReconnect)
/*      */     throws SQLException
/*      */   {
/* 2309 */     synchronized (getConnectionMutex())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2317 */       Properties mergedProps = exposeAsProperties(this.props);
/*      */       
/* 2319 */       if (!getHighAvailability()) {
/* 2320 */         connectOneTryOnly(isForReconnect, mergedProps);
/*      */         
/* 2322 */         return;
/*      */       }
/*      */       
/* 2325 */       connectWithRetries(isForReconnect, mergedProps);
/*      */     }
/*      */   }
/*      */   
/*      */   private void connectWithRetries(boolean isForReconnect, Properties mergedProps) throws SQLException
/*      */   {
/* 2331 */     double timeout = getInitialTimeout();
/* 2332 */     boolean connectionGood = false;
/*      */     
/* 2334 */     Exception connectionException = null;
/*      */     
/* 2336 */     for (int attemptCount = 0; 
/* 2337 */         (attemptCount < getMaxReconnects()) && (!connectionGood); attemptCount++) {
/*      */       try {
/* 2339 */         if (this.io != null) {
/* 2340 */           this.io.forceClose();
/*      */         }
/*      */         
/* 2343 */         coreConnect(mergedProps);
/* 2344 */         pingInternal(false, 0);
/*      */         
/*      */         boolean oldAutoCommit;
/*      */         
/*      */         int oldIsolationLevel;
/*      */         boolean oldReadOnly;
/*      */         String oldCatalog;
/* 2351 */         synchronized (getConnectionMutex()) {
/* 2352 */           this.connectionId = this.io.getThreadId();
/* 2353 */           this.isClosed = false;
/*      */           
/*      */ 
/* 2356 */           oldAutoCommit = getAutoCommit();
/* 2357 */           oldIsolationLevel = this.isolationLevel;
/* 2358 */           oldReadOnly = isReadOnly(false);
/* 2359 */           oldCatalog = getCatalog();
/*      */           
/* 2361 */           this.io.setStatementInterceptors(this.statementInterceptors);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2367 */         initializePropsFromServer();
/*      */         
/* 2369 */         if (isForReconnect)
/*      */         {
/* 2371 */           setAutoCommit(oldAutoCommit);
/*      */           
/* 2373 */           if (this.hasIsolationLevels) {
/* 2374 */             setTransactionIsolation(oldIsolationLevel);
/*      */           }
/*      */           
/* 2377 */           setCatalog(oldCatalog);
/* 2378 */           setReadOnly(oldReadOnly);
/*      */         }
/*      */         
/* 2381 */         connectionGood = true;
/*      */       }
/*      */       catch (Exception EEE)
/*      */       {
/* 2385 */         connectionException = EEE;
/* 2386 */         connectionGood = false;
/*      */         
/*      */ 
/* 2389 */         if (!connectionGood) break label190; }
/* 2390 */       break;
/*      */       
/*      */       label190:
/* 2393 */       if (attemptCount > 0) {
/*      */         try {
/* 2395 */           Thread.sleep(timeout * 1000L);
/*      */         }
/*      */         catch (InterruptedException IE) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2402 */     if (!connectionGood)
/*      */     {
/* 2404 */       SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnectWithRetries", new Object[] { Integer.valueOf(getMaxReconnects()) }), "08001", getExceptionInterceptor());
/*      */       
/*      */ 
/*      */ 
/* 2408 */       chainedEx.initCause(connectionException);
/*      */       
/* 2410 */       throw chainedEx;
/*      */     }
/*      */     
/* 2413 */     if ((getParanoid()) && (!getHighAvailability())) {
/* 2414 */       this.password = null;
/* 2415 */       this.user = null;
/*      */     }
/*      */     
/* 2418 */     if (isForReconnect)
/*      */     {
/*      */ 
/*      */ 
/* 2422 */       Iterator<Statement> statementIter = this.openStatements.values().iterator();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2433 */       Stack<Statement> serverPreparedStatements = null;
/*      */       
/* 2435 */       while (statementIter.hasNext()) {
/* 2436 */         Statement statementObj = (Statement)statementIter.next();
/*      */         
/* 2438 */         if ((statementObj instanceof ServerPreparedStatement)) {
/* 2439 */           if (serverPreparedStatements == null) {
/* 2440 */             serverPreparedStatements = new Stack();
/*      */           }
/*      */           
/* 2443 */           serverPreparedStatements.add(statementObj);
/*      */         }
/*      */       }
/*      */       
/* 2447 */       if (serverPreparedStatements != null) {
/* 2448 */         while (!serverPreparedStatements.isEmpty()) {
/* 2449 */           ((ServerPreparedStatement)serverPreparedStatements.pop()).rePrepare();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void coreConnect(Properties mergedProps)
/*      */     throws SQLException, IOException
/*      */   {
/* 2458 */     int newPort = 3306;
/* 2459 */     String newHost = "localhost";
/*      */     
/* 2461 */     String protocol = mergedProps.getProperty("PROTOCOL");
/*      */     
/* 2463 */     if (protocol != null)
/*      */     {
/*      */ 
/* 2466 */       if ("tcp".equalsIgnoreCase(protocol)) {
/* 2467 */         newHost = normalizeHost(mergedProps.getProperty("HOST"));
/* 2468 */         newPort = parsePortNumber(mergedProps.getProperty("PORT", "3306"));
/* 2469 */       } else if ("pipe".equalsIgnoreCase(protocol)) {
/* 2470 */         setSocketFactoryClassName(NamedPipeSocketFactory.class.getName());
/*      */         
/* 2472 */         String path = mergedProps.getProperty("PATH");
/*      */         
/* 2474 */         if (path != null) {
/* 2475 */           mergedProps.setProperty("namedPipePath", path);
/*      */         }
/*      */       }
/*      */       else {
/* 2479 */         newHost = normalizeHost(mergedProps.getProperty("HOST"));
/* 2480 */         newPort = parsePortNumber(mergedProps.getProperty("PORT", "3306"));
/*      */       }
/*      */     }
/*      */     else {
/* 2484 */       String[] parsedHostPortPair = NonRegisteringDriver.parseHostPortPair(this.hostPortPair);
/*      */       
/* 2486 */       newHost = parsedHostPortPair[0];
/*      */       
/* 2488 */       newHost = normalizeHost(newHost);
/*      */       
/* 2490 */       if (parsedHostPortPair[1] != null) {
/* 2491 */         newPort = parsePortNumber(parsedHostPortPair[1]);
/*      */       }
/*      */     }
/*      */     
/* 2495 */     this.port = newPort;
/* 2496 */     this.host = newHost;
/*      */     
/* 2498 */     this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), getProxy(), getSocketTimeout(), this.largeRowSizeThreshold.getValueAsInt());
/*      */     
/*      */ 
/*      */ 
/* 2502 */     this.io.doHandshake(this.user, this.password, this.database);
/*      */   }
/*      */   
/*      */   private String normalizeHost(String hostname)
/*      */   {
/* 2507 */     if ((hostname == null) || (StringUtils.isEmptyOrWhitespaceOnly(hostname))) {
/* 2508 */       return "localhost";
/*      */     }
/*      */     
/* 2511 */     return hostname;
/*      */   }
/*      */   
/*      */   private int parsePortNumber(String portAsString) throws SQLException {
/* 2515 */     int portNumber = 3306;
/*      */     try {
/* 2517 */       portNumber = Integer.parseInt(portAsString);
/*      */     }
/*      */     catch (NumberFormatException nfe) {
/* 2520 */       throw SQLError.createSQLException("Illegal connection port value '" + portAsString + "'", "01S00", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2526 */     return portNumber;
/*      */   }
/*      */   
/*      */   private void connectOneTryOnly(boolean isForReconnect, Properties mergedProps) throws SQLException
/*      */   {
/* 2531 */     Exception connectionNotEstablishedBecause = null;
/*      */     
/*      */     try
/*      */     {
/* 2535 */       coreConnect(mergedProps);
/* 2536 */       this.connectionId = this.io.getThreadId();
/* 2537 */       this.isClosed = false;
/*      */       
/*      */ 
/* 2540 */       boolean oldAutoCommit = getAutoCommit();
/* 2541 */       int oldIsolationLevel = this.isolationLevel;
/* 2542 */       boolean oldReadOnly = isReadOnly(false);
/* 2543 */       String oldCatalog = getCatalog();
/*      */       
/* 2545 */       this.io.setStatementInterceptors(this.statementInterceptors);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2550 */       initializePropsFromServer();
/*      */       
/* 2552 */       if (isForReconnect)
/*      */       {
/* 2554 */         setAutoCommit(oldAutoCommit);
/*      */         
/* 2556 */         if (this.hasIsolationLevels) {
/* 2557 */           setTransactionIsolation(oldIsolationLevel);
/*      */         }
/*      */         
/* 2560 */         setCatalog(oldCatalog);
/*      */         
/* 2562 */         setReadOnly(oldReadOnly);
/*      */       }
/* 2564 */       return;
/*      */     }
/*      */     catch (Exception EEE)
/*      */     {
/* 2568 */       if (((EEE instanceof SQLException)) && (((SQLException)EEE).getErrorCode() == 1820) && (!getDisconnectOnExpiredPasswords()))
/*      */       {
/*      */ 
/* 2571 */         return;
/*      */       }
/*      */       
/* 2574 */       if (this.io != null) {
/* 2575 */         this.io.forceClose();
/*      */       }
/*      */       
/* 2578 */       connectionNotEstablishedBecause = EEE;
/*      */       
/* 2580 */       if ((EEE instanceof SQLException)) {
/* 2581 */         throw ((SQLException)EEE);
/*      */       }
/*      */       
/* 2584 */       SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnect"), "08001", getExceptionInterceptor());
/*      */       
/*      */ 
/* 2587 */       chainedEx.initCause(connectionNotEstablishedBecause);
/*      */       
/* 2589 */       throw chainedEx;
/*      */     }
/*      */   }
/*      */   
/*      */   private void createPreparedStatementCaches() throws SQLException {
/* 2594 */     synchronized (getConnectionMutex()) {
/* 2595 */       int cacheSize = getPreparedStatementCacheSize();
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 2600 */         Class<?> factoryClass = Class.forName(getParseInfoCacheFactory());
/*      */         
/*      */ 
/* 2603 */         CacheAdapterFactory<String, PreparedStatement.ParseInfo> cacheFactory = (CacheAdapterFactory)factoryClass.newInstance();
/*      */         
/* 2605 */         this.cachedPreparedStatementParams = cacheFactory.getInstance(this, this.myURL, getPreparedStatementCacheSize(), getPreparedStatementCacheSqlLimit(), this.props);
/*      */       }
/*      */       catch (ClassNotFoundException e) {
/* 2608 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 2611 */         sqlEx.initCause(e);
/*      */         
/* 2613 */         throw sqlEx;
/*      */       } catch (InstantiationException e) {
/* 2615 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 2618 */         sqlEx.initCause(e);
/*      */         
/* 2620 */         throw sqlEx;
/*      */       } catch (IllegalAccessException e) {
/* 2622 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 2625 */         sqlEx.initCause(e);
/*      */         
/* 2627 */         throw sqlEx;
/*      */       }
/*      */       
/* 2630 */       if (getUseServerPreparedStmts()) {
/* 2631 */         this.serverSideStatementCheckCache = new LRUCache(cacheSize);
/*      */         
/* 2633 */         this.serverSideStatementCache = new LRUCache(cacheSize)
/*      */         {
/*      */           private static final long serialVersionUID = 7692318650375988114L;
/*      */           
/*      */           protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
/* 2638 */             if (this.maxElements <= 1) {
/* 2639 */               return false;
/*      */             }
/*      */             
/* 2642 */             boolean removeIt = super.removeEldestEntry(eldest);
/*      */             
/* 2644 */             if (removeIt) {
/* 2645 */               ServerPreparedStatement ps = (ServerPreparedStatement)eldest.getValue();
/*      */               
/* 2647 */               ps.isCached = false;
/* 2648 */               ps.setClosed(false);
/*      */               try
/*      */               {
/* 2651 */                 ps.close();
/*      */               }
/*      */               catch (SQLException sqlEx) {}
/*      */             }
/*      */             
/*      */ 
/* 2657 */             return removeIt;
/*      */           }
/*      */         };
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
/*      */   public java.sql.Statement createStatement()
/*      */     throws SQLException
/*      */   {
/* 2674 */     return createStatement(1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 2692 */     checkClosed();
/*      */     
/* 2694 */     StatementImpl stmt = new StatementImpl(getLoadBalanceSafeProxy(), this.database);
/* 2695 */     stmt.setResultSetType(resultSetType);
/* 2696 */     stmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 2698 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 2707 */     if ((getPedantic()) && 
/* 2708 */       (resultSetHoldability != 1)) {
/* 2709 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2715 */     return createStatement(resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   public void dumpTestcaseQuery(String query) {
/* 2719 */     System.err.println(query);
/*      */   }
/*      */   
/*      */   public Connection duplicate() throws SQLException {
/* 2723 */     return new ConnectionImpl(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata)
/*      */     throws SQLException
/*      */   {
/* 2777 */     return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/* 2787 */     synchronized (getConnectionMutex())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2794 */       long queryStartTime = 0L;
/*      */       
/* 2796 */       int endOfQueryPacketPosition = 0;
/*      */       
/* 2798 */       if (packet != null) {
/* 2799 */         endOfQueryPacketPosition = packet.getPosition();
/*      */       }
/*      */       
/* 2802 */       if (getGatherPerformanceMetrics()) {
/* 2803 */         queryStartTime = System.currentTimeMillis();
/*      */       }
/*      */       
/* 2806 */       this.lastQueryFinishedTime = 0L;
/*      */       
/* 2808 */       if ((getHighAvailability()) && ((this.autoCommit) || (getAutoReconnectForPools())) && (this.needsPing) && (!isBatch))
/*      */       {
/*      */         try
/*      */         {
/* 2812 */           pingInternal(false, 0);
/*      */           
/* 2814 */           this.needsPing = false;
/*      */         } catch (Exception Ex) {
/* 2816 */           createNewIO(true);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 2821 */         if (packet == null) {
/* 2822 */           encoding = null;
/*      */           
/* 2824 */           if (getUseUnicode()) {
/* 2825 */             encoding = getEncoding();
/*      */           }
/*      */           
/* 2828 */           ResultSetInternalMethods localResultSetInternalMethods = this.io.sqlQueryDirect(callingStatement, sql, encoding, null, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);jsr 236;return localResultSetInternalMethods;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2834 */         String encoding = this.io.sqlQueryDirect(callingStatement, null, null, packet, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);jsr 202;return encoding;
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (SQLException sqlE)
/*      */       {
/*      */ 
/* 2841 */         if (getDumpQueriesOnException()) {
/* 2842 */           String extractedSql = extractSqlFromPacket(sql, packet, endOfQueryPacketPosition);
/*      */           
/* 2844 */           StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);
/*      */           
/* 2846 */           messageBuf.append("\n\nQuery being executed when exception was thrown:\n");
/*      */           
/* 2848 */           messageBuf.append(extractedSql);
/* 2849 */           messageBuf.append("\n\n");
/*      */           
/* 2851 */           sqlE = appendMessageToException(sqlE, messageBuf.toString(), getExceptionInterceptor());
/*      */         }
/*      */         
/* 2854 */         if (getHighAvailability()) {
/* 2855 */           this.needsPing = true;
/*      */         } else {
/* 2857 */           String sqlState = sqlE.getSQLState();
/*      */           
/* 2859 */           if ((sqlState != null) && (sqlState.equals("08S01")))
/*      */           {
/*      */ 
/* 2862 */             cleanup(sqlE);
/*      */           }
/*      */         }
/*      */         
/* 2866 */         throw sqlE;
/*      */       } catch (Exception ex) {
/* 2868 */         if (getHighAvailability()) {
/* 2869 */           this.needsPing = true;
/* 2870 */         } else if ((ex instanceof IOException)) {
/* 2871 */           cleanup(ex);
/*      */         }
/*      */         
/* 2874 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnexpectedException"), "S1000", getExceptionInterceptor());
/*      */         
/*      */ 
/* 2877 */         sqlEx.initCause(ex);
/*      */         
/* 2879 */         throw sqlEx;
/*      */       } finally {
/* 2881 */         jsr 6; } localObject2 = returnAddress; if (getMaintainTimeStats()) {
/* 2882 */         this.lastQueryFinishedTime = System.currentTimeMillis();
/*      */       }
/*      */       
/*      */ 
/* 2886 */       if (getGatherPerformanceMetrics()) {
/* 2887 */         long queryTime = System.currentTimeMillis() - queryStartTime;
/*      */         
/*      */ 
/* 2890 */         registerQueryExecutionTime(queryTime); }
/* 2891 */       ret;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition)
/*      */     throws SQLException
/*      */   {
/* 2900 */     String extractedSql = null;
/*      */     
/* 2902 */     if (possibleSqlQuery != null) {
/* 2903 */       if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
/* 2904 */         StringBuffer truncatedQueryBuf = new StringBuffer(possibleSqlQuery.substring(0, getMaxQuerySizeToLog()));
/*      */         
/* 2906 */         truncatedQueryBuf.append(Messages.getString("MysqlIO.25"));
/* 2907 */         extractedSql = truncatedQueryBuf.toString();
/*      */       } else {
/* 2909 */         extractedSql = possibleSqlQuery;
/*      */       }
/*      */     }
/*      */     
/* 2913 */     if (extractedSql == null)
/*      */     {
/*      */ 
/*      */ 
/* 2917 */       int extractPosition = endOfQueryPacketPosition;
/*      */       
/* 2919 */       boolean truncated = false;
/*      */       
/* 2921 */       if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
/* 2922 */         extractPosition = getMaxQuerySizeToLog();
/* 2923 */         truncated = true;
/*      */       }
/*      */       
/* 2926 */       extractedSql = StringUtils.toString(queryPacket.getByteBuffer(), 5, extractPosition - 5);
/*      */       
/*      */ 
/* 2929 */       if (truncated) {
/* 2930 */         extractedSql = extractedSql + Messages.getString("MysqlIO.25");
/*      */       }
/*      */     }
/*      */     
/* 2934 */     return extractedSql;
/*      */   }
/*      */   
/*      */   public StringBuffer generateConnectionCommentBlock(StringBuffer buf)
/*      */   {
/* 2939 */     buf.append("/* conn id ");
/* 2940 */     buf.append(getId());
/* 2941 */     buf.append(" clock: ");
/* 2942 */     buf.append(System.currentTimeMillis());
/* 2943 */     buf.append(" */ ");
/*      */     
/* 2945 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 2979 */     if (getDynamicCalendars()) {
/* 2980 */       return Calendar.getInstance();
/*      */     }
/*      */     
/* 2983 */     return getSessionLockedCalendar();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName)
/*      */     throws SQLException
/*      */   {
/* 3022 */     if (javaEncodingName == null) {
/* 3023 */       return null;
/*      */     }
/*      */     
/* 3026 */     if (this.usePlatformCharsetConverters) {
/* 3027 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 3031 */     SingleByteCharsetConverter converter = null;
/*      */     
/* 3033 */     synchronized (this.charsetConverterMap) {
/* 3034 */       Object asObject = this.charsetConverterMap.get(javaEncodingName);
/*      */       
/*      */ 
/* 3037 */       if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
/* 3038 */         return null;
/*      */       }
/*      */       
/* 3041 */       converter = (SingleByteCharsetConverter)asObject;
/*      */       
/* 3043 */       if (converter == null) {
/*      */         try {
/* 3045 */           converter = SingleByteCharsetConverter.getInstance(javaEncodingName, this);
/*      */           
/*      */ 
/* 3048 */           if (converter == null) {
/* 3049 */             this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */           }
/*      */           else {
/* 3052 */             this.charsetConverterMap.put(javaEncodingName, converter);
/*      */           }
/*      */         } catch (UnsupportedEncodingException unsupEncEx) {
/* 3055 */           this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */           
/*      */ 
/* 3058 */           converter = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3063 */     return converter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharsetNameForIndex(int charsetIndex)
/*      */     throws SQLException
/*      */   {
/* 3078 */     String charsetName = null;
/*      */     
/* 3080 */     if (getUseOldUTF8Behavior()) {
/* 3081 */       return getEncoding();
/*      */     }
/*      */     
/* 3084 */     if (charsetIndex != -1) {
/*      */       try {
/* 3086 */         charsetName = (String)this.indexToJavaCharset.get(Integer.valueOf(charsetIndex));
/*      */         
/* 3088 */         if (charsetName == null) { charsetName = CharsetMapping.INDEX_TO_CHARSET[charsetIndex];
/*      */         }
/* 3090 */         if ((this.characterEncodingIsAliasForSjis) && (
/* 3091 */           ("sjis".equalsIgnoreCase(charsetName)) || ("MS932".equalsIgnoreCase(charsetName))))
/*      */         {
/*      */ 
/* 3094 */           charsetName = getEncoding();
/*      */         }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
/* 3098 */         throw SQLError.createSQLException("Unknown character set index for field '" + charsetIndex + "' received from server.", "S1000", getExceptionInterceptor());
/*      */ 
/*      */       }
/*      */       catch (RuntimeException ex)
/*      */       {
/* 3103 */         SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 3104 */         sqlEx.initCause(ex);
/* 3105 */         throw sqlEx;
/*      */       }
/*      */       
/*      */ 
/* 3109 */       if (charsetName == null) {
/* 3110 */         charsetName = getEncoding();
/*      */       }
/*      */     } else {
/* 3113 */       charsetName = getEncoding();
/*      */     }
/*      */     
/* 3116 */     return charsetName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getDefaultTimeZone()
/*      */   {
/* 3125 */     return this.defaultTimeZone;
/*      */   }
/*      */   
/*      */   public String getErrorMessageEncoding() {
/* 3129 */     return this.errorMessageEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getHoldability()
/*      */     throws SQLException
/*      */   {
/* 3136 */     return 2;
/*      */   }
/*      */   
/*      */   public long getId() {
/* 3140 */     return this.connectionId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIdleFor()
/*      */   {
/* 3152 */     synchronized (getConnectionMutex()) {
/* 3153 */       if (this.lastQueryFinishedTime == 0L) {
/* 3154 */         return 0L;
/*      */       }
/*      */       
/* 3157 */       long now = System.currentTimeMillis();
/* 3158 */       long idleTime = now - this.lastQueryFinishedTime;
/*      */       
/* 3160 */       return idleTime;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MysqlIO getIO()
/*      */     throws SQLException
/*      */   {
/* 3172 */     if ((this.io == null) || (this.isClosed)) {
/* 3173 */       throw SQLError.createSQLException("Operation not allowed on closed connection", "08003", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3178 */     return this.io;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Log getLog()
/*      */     throws SQLException
/*      */   {
/* 3190 */     return this.log;
/*      */   }
/*      */   
/*      */   public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
/* 3194 */     return getMaxBytesPerChar(null, javaCharsetName);
/*      */   }
/*      */   
/*      */   public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException
/*      */   {
/* 3199 */     String charset = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 3207 */       if (this.indexToCustomMysqlCharset != null) {
/* 3208 */         charset = (String)this.indexToCustomMysqlCharset.get(charsetIndex);
/*      */       }
/*      */       
/* 3211 */       if (charset == null) { charset = (String)CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(charsetIndex);
/*      */       }
/*      */       
/* 3214 */       if (charset == null) {
/* 3215 */         charset = CharsetMapping.getMysqlEncodingForJavaEncoding(javaCharsetName, this);
/* 3216 */         if ((this.io.serverCharsetIndex == 33) && (versionMeetsMinimum(5, 5, 3)) && (javaCharsetName.equalsIgnoreCase("UTF-8")))
/*      */         {
/* 3218 */           charset = "utf8";
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3223 */       Integer mblen = null;
/* 3224 */       if (this.mysqlCharsetToCustomMblen != null) {
/* 3225 */         mblen = (Integer)this.mysqlCharsetToCustomMblen.get(charset);
/*      */       }
/*      */       
/*      */ 
/* 3229 */       if (mblen == null) mblen = (Integer)CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP.get(charset);
/* 3230 */       if (mblen == null) { mblen = (Integer)CharsetMapping.STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP.get(charset);
/*      */       }
/* 3232 */       if (mblen != null) return mblen.intValue();
/*      */     } catch (SQLException ex) {
/* 3234 */       throw ex;
/*      */     } catch (RuntimeException ex) {
/* 3236 */       SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 3237 */       sqlEx.initCause(ex);
/* 3238 */       throw sqlEx;
/*      */     }
/*      */     
/* 3241 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.DatabaseMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 3255 */     return getMetaData(true, true);
/*      */   }
/*      */   
/*      */   private java.sql.DatabaseMetaData getMetaData(boolean checkClosed, boolean checkForInfoSchema) throws SQLException {
/* 3259 */     if (checkClosed) {
/* 3260 */       checkClosed();
/*      */     }
/*      */     
/* 3263 */     return DatabaseMetaData.getInstance(getLoadBalanceSafeProxy(), this.database, checkForInfoSchema);
/*      */   }
/*      */   
/*      */   public java.sql.Statement getMetadataSafeStatement() throws SQLException {
/* 3267 */     java.sql.Statement stmt = createStatement();
/*      */     
/* 3269 */     if (stmt.getMaxRows() != 0) {
/* 3270 */       stmt.setMaxRows(0);
/*      */     }
/*      */     
/* 3273 */     stmt.setEscapeProcessing(false);
/*      */     
/* 3275 */     if (stmt.getFetchSize() != 0) {
/* 3276 */       stmt.setFetchSize(0);
/*      */     }
/*      */     
/* 3279 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNetBufferLength()
/*      */   {
/* 3288 */     return this.netBufferLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServerCharacterEncoding()
/*      */   {
/* 3297 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 3298 */       String charset = null;
/* 3299 */       if (this.indexToCustomMysqlCharset != null) {
/* 3300 */         charset = (String)this.indexToCustomMysqlCharset.get(Integer.valueOf(this.io.serverCharsetIndex));
/*      */       }
/* 3302 */       if (charset == null) charset = (String)CharsetMapping.STATIC_INDEX_TO_MYSQL_CHARSET_MAP.get(Integer.valueOf(this.io.serverCharsetIndex));
/* 3303 */       return charset != null ? charset : (String)this.serverVariables.get("character_set_server");
/*      */     }
/* 3305 */     return (String)this.serverVariables.get("character_set");
/*      */   }
/*      */   
/*      */   public int getServerMajorVersion() {
/* 3309 */     return this.io.getServerMajorVersion();
/*      */   }
/*      */   
/*      */   public int getServerMinorVersion() {
/* 3313 */     return this.io.getServerMinorVersion();
/*      */   }
/*      */   
/*      */   public int getServerSubMinorVersion() {
/* 3317 */     return this.io.getServerSubMinorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getServerTimezoneTZ()
/*      */   {
/* 3326 */     return this.serverTimezoneTZ;
/*      */   }
/*      */   
/*      */   public String getServerVariable(String variableName)
/*      */   {
/* 3331 */     if (this.serverVariables != null) {
/* 3332 */       return (String)this.serverVariables.get(variableName);
/*      */     }
/*      */     
/* 3335 */     return null;
/*      */   }
/*      */   
/*      */   public String getServerVersion() {
/* 3339 */     return this.io.getServerVersion();
/*      */   }
/*      */   
/*      */   public Calendar getSessionLockedCalendar()
/*      */   {
/* 3344 */     return this.sessionCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 3356 */     synchronized (getConnectionMutex()) {
/* 3357 */       if ((this.hasIsolationLevels) && (!getUseLocalSessionState())) {
/* 3358 */         java.sql.Statement stmt = null;
/* 3359 */         ResultSet rs = null;
/*      */         try
/*      */         {
/* 3362 */           stmt = getMetadataSafeStatement();
/*      */           
/* 3364 */           String query = null;
/*      */           
/* 3366 */           int offset = 0;
/*      */           
/* 3368 */           if (versionMeetsMinimum(4, 0, 3)) {
/* 3369 */             query = "SELECT @@session.tx_isolation";
/* 3370 */             offset = 1;
/*      */           } else {
/* 3372 */             query = "SHOW VARIABLES LIKE 'transaction_isolation'";
/* 3373 */             offset = 2;
/*      */           }
/*      */           
/* 3376 */           rs = stmt.executeQuery(query);
/*      */           
/* 3378 */           if (rs.next()) {
/* 3379 */             String s = rs.getString(offset);
/*      */             
/* 3381 */             if (s != null) {
/* 3382 */               Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */               
/* 3384 */               if (intTI != null) {
/* 3385 */                 int i = intTI.intValue();jsr 68;return i;
/*      */               }
/*      */             }
/*      */             
/* 3389 */             throw SQLError.createSQLException("Could not map transaction isolation '" + s + " to a valid JDBC level.", "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 3395 */           throw SQLError.createSQLException("Could not retrieve transaction isolation level from server", "S1000", getExceptionInterceptor());
/*      */ 
/*      */         }
/*      */         finally
/*      */         {
/* 3400 */           jsr 6; } localObject2 = returnAddress; if (rs != null) {
/*      */           try {
/* 3402 */             rs.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */           
/*      */ 
/*      */ 
/* 3408 */           rs = null;
/*      */         }
/*      */         
/* 3411 */         if (stmt != null) {
/*      */           try {
/* 3413 */             stmt.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */           
/*      */ 
/*      */ 
/* 3419 */           stmt = null; } ret;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3424 */       return this.isolationLevel;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, Class<?>> getTypeMap()
/*      */     throws SQLException
/*      */   {
/* 3437 */     synchronized (getConnectionMutex()) {
/* 3438 */       if (this.typeMap == null) {
/* 3439 */         this.typeMap = new HashMap();
/*      */       }
/*      */       
/* 3442 */       return this.typeMap;
/*      */     }
/*      */   }
/*      */   
/*      */   public String getURL() {
/* 3447 */     return this.myURL;
/*      */   }
/*      */   
/*      */   public String getUser() {
/* 3451 */     return this.user;
/*      */   }
/*      */   
/*      */   public Calendar getUtcCalendar() {
/* 3455 */     return this.utcCalendar;
/*      */   }
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
/* 3468 */     return null;
/*      */   }
/*      */   
/*      */   public boolean hasSameProperties(Connection c) {
/* 3472 */     return this.props.equals(c.getProperties());
/*      */   }
/*      */   
/*      */   public Properties getProperties() {
/* 3476 */     return this.props;
/*      */   }
/*      */   
/*      */   public boolean hasTriedMaster() {
/* 3480 */     return this.hasTriedMasterFlag;
/*      */   }
/*      */   
/*      */   public void incrementNumberOfPreparedExecutes() {
/* 3484 */     if (getGatherPerformanceMetrics()) {
/* 3485 */       this.numberOfPreparedExecutes += 1L;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3490 */       this.numberOfQueriesIssued += 1L;
/*      */     }
/*      */   }
/*      */   
/*      */   public void incrementNumberOfPrepares() {
/* 3495 */     if (getGatherPerformanceMetrics()) {
/* 3496 */       this.numberOfPrepares += 1L;
/*      */     }
/*      */   }
/*      */   
/*      */   public void incrementNumberOfResultSetsCreated() {
/* 3501 */     if (getGatherPerformanceMetrics()) {
/* 3502 */       this.numberOfResultSetsCreated += 1L;
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
/*      */   private void initializeDriverProperties(Properties info)
/*      */     throws SQLException
/*      */   {
/* 3517 */     initializeProperties(info);
/*      */     
/* 3519 */     String exceptionInterceptorClasses = getExceptionInterceptors();
/*      */     
/* 3521 */     if ((exceptionInterceptorClasses != null) && (!"".equals(exceptionInterceptorClasses))) {
/* 3522 */       this.exceptionInterceptor = new ExceptionInterceptorChain(exceptionInterceptorClasses);
/* 3523 */       this.exceptionInterceptor.init(this, info);
/*      */     }
/*      */     
/* 3526 */     this.usePlatformCharsetConverters = getUseJvmCharsetConverters();
/*      */     
/* 3528 */     this.log = LogFactory.getLogger(getLogger(), "MySQL", getExceptionInterceptor());
/*      */     
/* 3530 */     if ((getProfileSql()) || (getUseUsageAdvisor())) {
/* 3531 */       this.eventSink = ProfilerEventHandlerFactory.getInstance(getLoadBalanceSafeProxy());
/*      */     }
/*      */     
/* 3534 */     if (getCachePreparedStatements()) {
/* 3535 */       createPreparedStatementCaches();
/*      */     }
/*      */     
/* 3538 */     if ((getNoDatetimeStringSync()) && (getUseTimezone())) {
/* 3539 */       throw SQLError.createSQLException("Can't enable noDatetimeStringSync and useTimezone configuration properties at the same time", "01S00", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3545 */     if (getCacheCallableStatements()) {
/* 3546 */       this.parsedCallableStatementCache = new LRUCache(getCallableStatementCacheSize());
/*      */     }
/*      */     
/*      */ 
/* 3550 */     if (getAllowMultiQueries()) {
/* 3551 */       setCacheResultSetMetadata(false);
/*      */     }
/*      */     
/* 3554 */     if (getCacheResultSetMetadata()) {
/* 3555 */       this.resultSetMetadataCache = new LRUCache(getMetadataCacheSize());
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
/*      */   private void initializePropsFromServer()
/*      */     throws SQLException
/*      */   {
/* 3570 */     String connectionInterceptorClasses = getConnectionLifecycleInterceptors();
/*      */     
/* 3572 */     this.connectionLifecycleInterceptors = null;
/*      */     
/* 3574 */     if (connectionInterceptorClasses != null) {
/* 3575 */       this.connectionLifecycleInterceptors = Util.loadExtensions(this, this.props, connectionInterceptorClasses, "Connection.badLifecycleInterceptor", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3580 */     setSessionVariables();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3586 */     if (!versionMeetsMinimum(4, 1, 0)) {
/* 3587 */       setTransformedBitIsBoolean(false);
/*      */     }
/*      */     
/* 3590 */     this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3595 */     if ((getUseServerPreparedStmts()) && (versionMeetsMinimum(4, 1, 0))) {
/* 3596 */       this.useServerPreparedStmts = true;
/*      */       
/* 3598 */       if ((versionMeetsMinimum(5, 0, 0)) && (!versionMeetsMinimum(5, 0, 3))) {
/* 3599 */         this.useServerPreparedStmts = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3608 */     if (versionMeetsMinimum(3, 21, 22)) {
/* 3609 */       loadServerVariables();
/*      */       
/* 3611 */       if (versionMeetsMinimum(5, 0, 2)) {
/* 3612 */         this.autoIncrementIncrement = getServerVariableAsInt("auto_increment_increment", 1);
/*      */       } else {
/* 3614 */         this.autoIncrementIncrement = 1;
/*      */       }
/*      */       
/* 3617 */       buildCollationMapping();
/*      */       
/* 3619 */       LicenseConfiguration.checkLicenseType(this.serverVariables);
/*      */       
/* 3621 */       String lowerCaseTables = (String)this.serverVariables.get("lower_case_table_names");
/*      */       
/* 3623 */       this.lowerCaseTableNames = (("on".equalsIgnoreCase(lowerCaseTables)) || ("1".equalsIgnoreCase(lowerCaseTables)) || ("2".equalsIgnoreCase(lowerCaseTables)));
/*      */       
/*      */ 
/*      */ 
/* 3627 */       this.storesLowerCaseTableName = (("1".equalsIgnoreCase(lowerCaseTables)) || ("on".equalsIgnoreCase(lowerCaseTables)));
/*      */       
/*      */ 
/* 3630 */       configureTimezone();
/*      */       
/* 3632 */       if (this.serverVariables.containsKey("max_allowed_packet")) {
/* 3633 */         int serverMaxAllowedPacket = getServerVariableAsInt("max_allowed_packet", -1);
/*      */         
/* 3635 */         if ((serverMaxAllowedPacket != -1) && ((serverMaxAllowedPacket < getMaxAllowedPacket()) || (getMaxAllowedPacket() <= 0)))
/*      */         {
/* 3637 */           setMaxAllowedPacket(serverMaxAllowedPacket);
/* 3638 */         } else if ((serverMaxAllowedPacket == -1) && (getMaxAllowedPacket() == -1)) {
/* 3639 */           setMaxAllowedPacket(65535);
/*      */         }
/* 3641 */         if (getUseServerPrepStmts()) {
/* 3642 */           int preferredBlobSendChunkSize = getBlobSendChunkSize();
/*      */           
/*      */ 
/* 3645 */           int packetHeaderSize = 8203;
/* 3646 */           int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, getMaxAllowedPacket()) - packetHeaderSize;
/*      */           
/*      */ 
/* 3649 */           if (allowedBlobSendChunkSize <= 0) {
/* 3650 */             throw SQLError.createSQLException("Connection setting too low for 'maxAllowedPacket'. When 'useServerPrepStmts=true', 'maxAllowedPacket' must be higher than " + packetHeaderSize + ". Check also 'max_allowed_packet' in MySQL configuration files.", "01S00", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 3656 */           setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
/*      */         }
/*      */       }
/*      */       
/* 3660 */       if (this.serverVariables.containsKey("net_buffer_length")) {
/* 3661 */         this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384);
/*      */       }
/*      */       
/* 3664 */       checkTransactionIsolationLevel();
/*      */       
/* 3666 */       if (!versionMeetsMinimum(4, 1, 0)) {
/* 3667 */         checkServerEncoding();
/*      */       }
/*      */       
/* 3670 */       this.io.checkForCharsetMismatch();
/*      */       
/* 3672 */       if (this.serverVariables.containsKey("sql_mode")) {
/* 3673 */         int sqlMode = 0;
/*      */         
/* 3675 */         String sqlModeAsString = (String)this.serverVariables.get("sql_mode");
/*      */         try {
/* 3677 */           sqlMode = Integer.parseInt(sqlModeAsString);
/*      */         }
/*      */         catch (NumberFormatException nfe)
/*      */         {
/* 3681 */           sqlMode = 0;
/*      */           
/* 3683 */           if (sqlModeAsString != null) {
/* 3684 */             if (sqlModeAsString.indexOf("ANSI_QUOTES") != -1) {
/* 3685 */               sqlMode |= 0x4;
/*      */             }
/*      */             
/* 3688 */             if (sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
/* 3689 */               this.noBackslashEscapes = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 3694 */         if ((sqlMode & 0x4) > 0) {
/* 3695 */           this.useAnsiQuotes = true;
/*      */         } else {
/* 3697 */           this.useAnsiQuotes = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 3703 */       this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
/*      */     }
/*      */     catch (SQLException ex) {
/* 3706 */       throw ex;
/*      */     } catch (RuntimeException ex) {
/* 3708 */       SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 3709 */       sqlEx.initCause(ex);
/* 3710 */       throw sqlEx;
/*      */     }
/*      */     
/*      */ 
/* 3714 */     boolean overrideDefaultAutocommit = isAutoCommitNonDefaultOnServer();
/*      */     
/* 3716 */     configureClientCharacterSet(false);
/*      */     
/* 3718 */     if (versionMeetsMinimum(3, 23, 15)) {
/* 3719 */       this.transactionsSupported = true;
/*      */       
/* 3721 */       if (!overrideDefaultAutocommit) {
/*      */         try {
/* 3723 */           setAutoCommit(true);
/*      */         }
/*      */         catch (SQLException ex)
/*      */         {
/* 3727 */           if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 3728 */             throw ex;
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/* 3733 */       this.transactionsSupported = false;
/*      */     }
/*      */     
/*      */ 
/* 3737 */     if (versionMeetsMinimum(3, 23, 36)) {
/* 3738 */       this.hasIsolationLevels = true;
/*      */     } else {
/* 3740 */       this.hasIsolationLevels = false;
/*      */     }
/*      */     
/* 3743 */     this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);
/*      */     
/* 3745 */     this.io.resetMaxBuf();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3755 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 3756 */       String characterSetResultsOnServerMysql = (String)this.serverVariables.get("jdbc.local.character_set_results");
/*      */       
/* 3758 */       if ((characterSetResultsOnServerMysql == null) || (StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, "NULL")) || (characterSetResultsOnServerMysql.length() == 0))
/*      */       {
/*      */ 
/*      */ 
/* 3762 */         String defaultMetadataCharsetMysql = (String)this.serverVariables.get("character_set_system");
/* 3763 */         String defaultMetadataCharset = null;
/*      */         
/* 3765 */         if (defaultMetadataCharsetMysql != null) {
/* 3766 */           defaultMetadataCharset = getJavaEncodingForMysqlEncoding(defaultMetadataCharsetMysql);
/*      */         } else {
/* 3768 */           defaultMetadataCharset = "UTF-8";
/*      */         }
/*      */         
/* 3771 */         this.characterSetMetadata = defaultMetadataCharset;
/*      */       } else {
/* 3773 */         this.characterSetResultsOnServer = getJavaEncodingForMysqlEncoding(characterSetResultsOnServerMysql);
/* 3774 */         this.characterSetMetadata = this.characterSetResultsOnServer;
/*      */       }
/*      */     } else {
/* 3777 */       this.characterSetMetadata = getEncoding();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3784 */     if ((versionMeetsMinimum(4, 1, 0)) && (!versionMeetsMinimum(4, 1, 10)) && (getAllowMultiQueries()))
/*      */     {
/*      */ 
/* 3787 */       if (isQueryCacheEnabled()) {
/* 3788 */         setAllowMultiQueries(false);
/*      */       }
/*      */     }
/*      */     
/* 3792 */     if ((versionMeetsMinimum(5, 0, 0)) && ((getUseLocalTransactionState()) || (getElideSetAutoCommits())) && (isQueryCacheEnabled()) && (!versionMeetsMinimum(6, 0, 10)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 3797 */       setUseLocalTransactionState(false);
/* 3798 */       setElideSetAutoCommits(false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3805 */     setupServerForTruncationChecks();
/*      */   }
/*      */   
/*      */   private boolean isQueryCacheEnabled() {
/* 3809 */     return ("ON".equalsIgnoreCase((String)this.serverVariables.get("query_cache_type"))) && (!"0".equalsIgnoreCase((String)this.serverVariables.get("query_cache_size")));
/*      */   }
/*      */   
/*      */   private int getServerVariableAsInt(String variableName, int fallbackValue) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3816 */       return Integer.parseInt((String)this.serverVariables.get(variableName));
/*      */     } catch (NumberFormatException nfe) {
/* 3818 */       getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[] { variableName, this.serverVariables.get(variableName), Integer.valueOf(fallbackValue) }));
/*      */     }
/*      */     
/* 3821 */     return fallbackValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isAutoCommitNonDefaultOnServer()
/*      */     throws SQLException
/*      */   {
/* 3834 */     boolean overrideDefaultAutocommit = false;
/*      */     
/* 3836 */     String initConnectValue = (String)this.serverVariables.get("init_connect");
/*      */     
/* 3838 */     if ((versionMeetsMinimum(4, 1, 2)) && (initConnectValue != null) && (initConnectValue.length() > 0))
/*      */     {
/* 3840 */       if (!getElideSetAutoCommits())
/*      */       {
/* 3842 */         ResultSet rs = null;
/* 3843 */         java.sql.Statement stmt = null;
/*      */         try
/*      */         {
/* 3846 */           stmt = getMetadataSafeStatement();
/*      */           
/* 3848 */           rs = stmt.executeQuery("SELECT @@session.autocommit");
/*      */           
/* 3850 */           if (rs.next()) {
/* 3851 */             this.autoCommit = rs.getBoolean(1);
/* 3852 */             if (this.autoCommit != true) {
/* 3853 */               overrideDefaultAutocommit = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         finally {
/* 3858 */           if (rs != null) {
/*      */             try {
/* 3860 */               rs.close();
/*      */             }
/*      */             catch (SQLException sqlEx) {}
/*      */           }
/*      */           
/*      */ 
/* 3866 */           if (stmt != null) {
/*      */             try {
/* 3868 */               stmt.close();
/*      */ 
/*      */             }
/*      */             catch (SQLException sqlEx) {}
/*      */           }
/*      */         }
/*      */       }
/* 3875 */       else if (getIO().isSetNeededForAutoCommitMode(true))
/*      */       {
/* 3877 */         this.autoCommit = false;
/* 3878 */         overrideDefaultAutocommit = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3883 */     return overrideDefaultAutocommit;
/*      */   }
/*      */   
/*      */   public boolean isClientTzUTC() {
/* 3887 */     return this.isClientTzUTC;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/* 3896 */     return this.isClosed;
/*      */   }
/*      */   
/*      */   public boolean isCursorFetchEnabled() throws SQLException {
/* 3900 */     return (versionMeetsMinimum(5, 0, 2)) && (getUseCursorFetch());
/*      */   }
/*      */   
/*      */   public boolean isInGlobalTx() {
/* 3904 */     return this.isInGlobalTx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNoBackslashEscapesSet()
/*      */   {
/* 3927 */     return this.noBackslashEscapes;
/*      */   }
/*      */   
/*      */   public boolean isReadInfoMsgEnabled() {
/* 3931 */     return this.readInfoMsg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/* 3943 */     return isReadOnly(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRunningOnJDK13()
/*      */   {
/* 4005 */     return this.isRunningOnJDK13;
/*      */   }
/*      */   
/*      */   public boolean isSameResource(Connection otherConnection) {
/* 4009 */     synchronized (getConnectionMutex()) {
/* 4010 */       if (otherConnection == null) {
/* 4011 */         return false;
/*      */       }
/*      */       
/* 4014 */       boolean directCompare = true;
/*      */       
/* 4016 */       String otherHost = ((ConnectionImpl)otherConnection).origHostToConnectTo;
/* 4017 */       String otherOrigDatabase = ((ConnectionImpl)otherConnection).origDatabaseToConnectTo;
/* 4018 */       String otherCurrentCatalog = ((ConnectionImpl)otherConnection).database;
/*      */       
/* 4020 */       if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
/* 4021 */         directCompare = false;
/* 4022 */       } else if ((otherHost != null) && (otherHost.indexOf(',') == -1) && (otherHost.indexOf(':') == -1))
/*      */       {
/*      */ 
/* 4025 */         directCompare = ((ConnectionImpl)otherConnection).origPortToConnectTo == this.origPortToConnectTo;
/*      */       }
/*      */       
/*      */ 
/* 4029 */       if (directCompare) {
/* 4030 */         if (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo)) { directCompare = false;
/* 4031 */           directCompare = false;
/* 4032 */         } else if (!nullSafeCompare(otherCurrentCatalog, this.database)) {
/* 4033 */           directCompare = false;
/*      */         }
/*      */       }
/*      */       
/* 4037 */       if (directCompare) {
/* 4038 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 4042 */       String otherResourceId = ((ConnectionImpl)otherConnection).getResourceId();
/* 4043 */       String myResourceId = getResourceId();
/*      */       
/* 4045 */       if ((otherResourceId != null) || (myResourceId != null)) {
/* 4046 */         directCompare = nullSafeCompare(otherResourceId, myResourceId);
/*      */         
/* 4048 */         if (directCompare) {
/* 4049 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 4053 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isServerTzUTC() {
/* 4058 */     return this.isServerTzUTC;
/*      */   }
/*      */   
/*      */ 
/* 4062 */   private boolean usingCachedConfig = false;
/*      */   private static final String SERVER_VERSION_STRING_VAR_NAME = "server_version_string";
/*      */   
/* 4065 */   private void createConfigCacheIfNeeded() throws SQLException { synchronized (getConnectionMutex()) {
/* 4066 */       if (this.serverConfigCache != null) {
/* 4067 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 4073 */         Class<?> factoryClass = Class.forName(getServerConfigCacheFactory());
/*      */         
/*      */ 
/* 4076 */         CacheAdapterFactory<String, Map<String, String>> cacheFactory = (CacheAdapterFactory)factoryClass.newInstance();
/*      */         
/* 4078 */         this.serverConfigCache = cacheFactory.getInstance(this, this.myURL, Integer.MAX_VALUE, Integer.MAX_VALUE, this.props);
/*      */         
/* 4080 */         ExceptionInterceptor evictOnCommsError = new ExceptionInterceptor()
/*      */         {
/*      */           public void init(Connection conn, Properties config)
/*      */             throws SQLException
/*      */           {}
/*      */           
/*      */ 
/*      */           public void destroy() {}
/*      */           
/*      */ 
/*      */           public SQLException interceptException(SQLException sqlEx, Connection conn)
/*      */           {
/* 4092 */             if ((sqlEx.getSQLState() != null) && (sqlEx.getSQLState().startsWith("08"))) {
/* 4093 */               ConnectionImpl.this.serverConfigCache.invalidate(ConnectionImpl.this.getURL());
/*      */             }
/* 4095 */             return null;
/*      */           }
/*      */         };
/* 4098 */         if (this.exceptionInterceptor == null) {
/* 4099 */           this.exceptionInterceptor = evictOnCommsError;
/*      */         } else {
/* 4101 */           ((ExceptionInterceptorChain)this.exceptionInterceptor).addRingZero(evictOnCommsError);
/*      */         }
/*      */       } catch (ClassNotFoundException e) {
/* 4104 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 4107 */         sqlEx.initCause(e);
/*      */         
/* 4109 */         throw sqlEx;
/*      */       } catch (InstantiationException e) {
/* 4111 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 4114 */         sqlEx.initCause(e);
/*      */         
/* 4116 */         throw sqlEx;
/*      */       } catch (IllegalAccessException e) {
/* 4118 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getParseInfoCacheFactory(), "parseInfoCacheFactory" }), getExceptionInterceptor());
/*      */         
/*      */ 
/* 4121 */         sqlEx.initCause(e);
/*      */         
/* 4123 */         throw sqlEx;
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
/*      */   private void loadServerVariables()
/*      */     throws SQLException
/*      */   {
/* 4139 */     if (getCacheServerConfiguration()) {
/* 4140 */       createConfigCacheIfNeeded();
/*      */       
/* 4142 */       Map<String, String> cachedVariableMap = (Map)this.serverConfigCache.get(getURL());
/*      */       
/* 4144 */       if (cachedVariableMap != null) {
/* 4145 */         String cachedServerVersion = (String)cachedVariableMap.get("server_version_string");
/*      */         
/* 4147 */         if ((cachedServerVersion != null) && (this.io.getServerVersion() != null) && (cachedServerVersion.equals(this.io.getServerVersion())))
/*      */         {
/* 4149 */           this.serverVariables = cachedVariableMap;
/* 4150 */           this.usingCachedConfig = true;
/*      */           
/* 4152 */           return;
/*      */         }
/*      */         
/* 4155 */         this.serverConfigCache.invalidate(getURL());
/*      */       }
/*      */     }
/*      */     
/* 4159 */     java.sql.Statement stmt = null;
/* 4160 */     ResultSet results = null;
/*      */     try
/*      */     {
/* 4163 */       stmt = getMetadataSafeStatement();
/*      */       
/* 4165 */       String version = this.dbmd.getDriverVersion();
/*      */       
/* 4167 */       if ((version != null) && (version.indexOf('*') != -1)) {
/* 4168 */         StringBuffer buf = new StringBuffer(version.length() + 10);
/*      */         
/* 4170 */         for (int i = 0; i < version.length(); i++) {
/* 4171 */           char c = version.charAt(i);
/*      */           
/* 4173 */           if (c == '*') {
/* 4174 */             buf.append("[star]");
/*      */           } else {
/* 4176 */             buf.append(c);
/*      */           }
/*      */         }
/*      */         
/* 4180 */         version = buf.toString();
/*      */       }
/*      */       
/* 4183 */       String versionComment = "/* " + version + " */";
/*      */       
/*      */ 
/* 4186 */       String query = versionComment + "SHOW VARIABLES";
/*      */       
/* 4188 */       if (versionMeetsMinimum(5, 0, 3)) {
/* 4189 */         query = versionComment + "SHOW VARIABLES WHERE Variable_name ='language'" + " OR Variable_name = 'net_write_timeout'" + " OR Variable_name = 'interactive_timeout'" + " OR Variable_name = 'wait_timeout'" + " OR Variable_name = 'character_set_client'" + " OR Variable_name = 'character_set_connection'" + " OR Variable_name = 'character_set'" + " OR Variable_name = 'character_set_server'" + " OR Variable_name = 'tx_isolation'" + " OR Variable_name = 'transaction_isolation'" + " OR Variable_name = 'character_set_results'" + " OR Variable_name = 'timezone'" + " OR Variable_name = 'time_zone'" + " OR Variable_name = 'system_time_zone'" + " OR Variable_name = 'lower_case_table_names'" + " OR Variable_name = 'max_allowed_packet'" + " OR Variable_name = 'net_buffer_length'" + " OR Variable_name = 'sql_mode'" + " OR Variable_name = 'query_cache_type'" + " OR Variable_name = 'query_cache_size'" + " OR Variable_name = 'init_connect'";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4212 */       this.serverVariables = new HashMap();
/*      */       try
/*      */       {
/* 4215 */         results = stmt.executeQuery(query);
/*      */         
/* 4217 */         while (results.next()) {
/* 4218 */           this.serverVariables.put(results.getString(1), results.getString(2));
/*      */         }
/*      */         
/*      */ 
/* 4222 */         results.close();
/* 4223 */         results = null;
/*      */       } catch (SQLException ex) {
/* 4225 */         if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 4226 */           throw ex;
/*      */         }
/*      */       }
/*      */       
/* 4230 */       if (versionMeetsMinimum(5, 0, 2)) {
/*      */         try {
/* 4232 */           results = stmt.executeQuery(versionComment + "SELECT @@session.auto_increment_increment");
/*      */           
/* 4234 */           if (results.next()) {
/* 4235 */             this.serverVariables.put("auto_increment_increment", results.getString(1));
/*      */           }
/*      */         } catch (SQLException ex) {
/* 4238 */           if ((ex.getErrorCode() != 1820) || (getDisconnectOnExpiredPasswords())) {
/* 4239 */             throw ex;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4244 */       if (getCacheServerConfiguration()) {
/* 4245 */         this.serverVariables.put("server_version_string", this.io.getServerVersion());
/*      */         
/* 4247 */         this.serverConfigCache.put(getURL(), this.serverVariables);
/*      */         
/* 4249 */         this.usingCachedConfig = true;
/*      */       }
/*      */     } catch (SQLException e) {
/* 4252 */       throw e;
/*      */     } finally {
/* 4254 */       if (results != null) {
/*      */         try {
/* 4256 */           results.close();
/*      */         }
/*      */         catch (SQLException sqlE) {}
/*      */       }
/*      */       
/*      */ 
/* 4262 */       if (stmt != null) {
/*      */         try {
/* 4264 */           stmt.close();
/*      */         }
/*      */         catch (SQLException sqlE) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/* 4272 */   private int autoIncrementIncrement = 0;
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */   
/* 4275 */   public int getAutoIncrementIncrement() { return this.autoIncrementIncrement; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean lowerCaseTableNames()
/*      */   {
/* 4284 */     return this.lowerCaseTableNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void maxRowsChanged(Statement stmt)
/*      */   {
/* 4294 */     synchronized (getConnectionMutex()) {
/* 4295 */       if (this.statementsUsingMaxRows == null) {
/* 4296 */         this.statementsUsingMaxRows = new HashMap();
/*      */       }
/*      */       
/* 4299 */       this.statementsUsingMaxRows.put(stmt, stmt);
/*      */       
/* 4301 */       this.maxRowsChanged = true;
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
/*      */   public String nativeSQL(String sql)
/*      */     throws SQLException
/*      */   {
/* 4318 */     if (sql == null) {
/* 4319 */       return null;
/*      */     }
/*      */     
/* 4322 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getLoadBalanceSafeProxy());
/*      */     
/*      */ 
/*      */ 
/* 4326 */     if ((escapedSqlResult instanceof String)) {
/* 4327 */       return (String)escapedSqlResult;
/*      */     }
/*      */     
/* 4330 */     return ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */   }
/*      */   
/*      */   private CallableStatement parseCallableStatement(String sql) throws SQLException
/*      */   {
/* 4335 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getLoadBalanceSafeProxy());
/*      */     
/*      */ 
/* 4338 */     boolean isFunctionCall = false;
/* 4339 */     String parsedSql = null;
/*      */     
/* 4341 */     if ((escapedSqlResult instanceof EscapeProcessorResult)) {
/* 4342 */       parsedSql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/* 4343 */       isFunctionCall = ((EscapeProcessorResult)escapedSqlResult).callingStoredFunction;
/*      */     } else {
/* 4345 */       parsedSql = (String)escapedSqlResult;
/* 4346 */       isFunctionCall = false;
/*      */     }
/*      */     
/* 4349 */     return CallableStatement.getInstance(getLoadBalanceSafeProxy(), parsedSql, this.database, isFunctionCall);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean parserKnowsUnicode()
/*      */   {
/* 4359 */     return this.parserKnowsUnicode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ping()
/*      */     throws SQLException
/*      */   {
/* 4369 */     pingInternal(true, 0);
/*      */   }
/*      */   
/*      */   public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException
/*      */   {
/* 4374 */     if (checkForClosedConnection) {
/* 4375 */       checkClosed();
/*      */     }
/*      */     
/* 4378 */     long pingMillisLifetime = getSelfDestructOnPingSecondsLifetime();
/* 4379 */     int pingMaxOperations = getSelfDestructOnPingMaxOperations();
/*      */     
/* 4381 */     if (((pingMillisLifetime > 0L) && (System.currentTimeMillis() - this.connectionCreationTimeMillis > pingMillisLifetime)) || ((pingMaxOperations > 0) && (pingMaxOperations <= this.io.getCommandCount())))
/*      */     {
/*      */ 
/*      */ 
/* 4385 */       close();
/*      */       
/* 4387 */       throw SQLError.createSQLException(Messages.getString("Connection.exceededConnectionLifetime"), "08S01", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4392 */     this.io.sendCommand(14, null, null, false, null, timeoutMillis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.CallableStatement prepareCall(String sql)
/*      */     throws SQLException
/*      */   {
/* 4407 */     return prepareCall(sql, 1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 4428 */     if (versionMeetsMinimum(5, 0, 0)) {
/* 4429 */       CallableStatement cStmt = null;
/*      */       
/* 4431 */       if (!getCacheCallableStatements())
/*      */       {
/* 4433 */         cStmt = parseCallableStatement(sql);
/*      */       } else {
/* 4435 */         synchronized (this.parsedCallableStatementCache) {
/* 4436 */           CompoundCacheKey key = new CompoundCacheKey(getCatalog(), sql);
/*      */           
/* 4438 */           CallableStatement.CallableStatementParamInfo cachedParamInfo = (CallableStatement.CallableStatementParamInfo)this.parsedCallableStatementCache.get(key);
/*      */           
/*      */ 
/* 4441 */           if (cachedParamInfo != null) {
/* 4442 */             cStmt = CallableStatement.getInstance(getLoadBalanceSafeProxy(), cachedParamInfo);
/*      */           } else {
/* 4444 */             cStmt = parseCallableStatement(sql);
/*      */             
/* 4446 */             synchronized (cStmt) {
/* 4447 */               cachedParamInfo = cStmt.paramInfo;
/*      */             }
/*      */             
/* 4450 */             this.parsedCallableStatementCache.put(key, cachedParamInfo);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4455 */       cStmt.setResultSetType(resultSetType);
/* 4456 */       cStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */       
/* 4458 */       return cStmt;
/*      */     }
/*      */     
/* 4461 */     throw SQLError.createSQLException("Callable statements not supported.", "S1C00", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 4471 */     if ((getPedantic()) && 
/* 4472 */       (resultSetHoldability != 1)) {
/* 4473 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4479 */     CallableStatement cStmt = (CallableStatement)prepareCall(sql, resultSetType, resultSetConcurrency);
/*      */     
/*      */ 
/* 4482 */     return cStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 4512 */     return prepareStatement(sql, 1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 4521 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4523 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
/*      */     
/*      */ 
/* 4526 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 4546 */     synchronized (getConnectionMutex()) {
/* 4547 */       checkClosed();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4553 */       PreparedStatement pStmt = null;
/*      */       
/* 4555 */       boolean canServerPrepare = true;
/*      */       
/* 4557 */       String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */       
/* 4559 */       if ((this.useServerPreparedStmts) && (getEmulateUnsupportedPstmts())) {
/* 4560 */         canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
/*      */       }
/*      */       
/* 4563 */       if ((this.useServerPreparedStmts) && (canServerPrepare)) {
/* 4564 */         if (getCachePreparedStatements()) {
/* 4565 */           synchronized (this.serverSideStatementCache) {
/* 4566 */             pStmt = (ServerPreparedStatement)this.serverSideStatementCache.remove(sql);
/*      */             
/* 4568 */             if (pStmt != null) {
/* 4569 */               ((ServerPreparedStatement)pStmt).setClosed(false);
/* 4570 */               pStmt.clearParameters();
/*      */             }
/*      */             
/* 4573 */             if (pStmt == null) {
/*      */               try {
/* 4575 */                 pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */                 
/* 4577 */                 if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4578 */                   ((ServerPreparedStatement)pStmt).isCached = true;
/*      */                 }
/*      */                 
/* 4581 */                 pStmt.setResultSetType(resultSetType);
/* 4582 */                 pStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */               }
/*      */               catch (SQLException sqlEx) {
/* 4585 */                 if (getEmulateUnsupportedPstmts()) {
/* 4586 */                   pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */                   
/* 4588 */                   if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4589 */                     this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
/*      */                   }
/*      */                 } else {
/* 4592 */                   throw sqlEx;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         } else {
/*      */           try {
/* 4599 */             pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */             
/*      */ 
/* 4602 */             pStmt.setResultSetType(resultSetType);
/* 4603 */             pStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */           }
/*      */           catch (SQLException sqlEx) {
/* 4606 */             if (getEmulateUnsupportedPstmts()) {
/* 4607 */               pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */             } else {
/* 4609 */               throw sqlEx;
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 4614 */         pStmt = (PreparedStatement)clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */       }
/*      */       
/* 4617 */       return pStmt;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 4627 */     if ((getPedantic()) && 
/* 4628 */       (resultSetHoldability != 1)) {
/* 4629 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4635 */     return prepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 4643 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4645 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyIndexes != null) && (autoGenKeyIndexes.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 4649 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 4657 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4659 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyColNames != null) && (autoGenKeyColNames.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 4663 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason)
/*      */     throws SQLException
/*      */   {
/* 4678 */     SQLException sqlEx = null;
/*      */     
/* 4680 */     if (isClosed()) {
/* 4681 */       return;
/*      */     }
/*      */     
/* 4684 */     this.forceClosedReason = reason;
/*      */     try
/*      */     {
/* 4687 */       if (!skipLocalTeardown) {
/* 4688 */         if ((!getAutoCommit()) && (issueRollback)) {
/*      */           try {
/* 4690 */             rollback();
/*      */           } catch (SQLException ex) {
/* 4692 */             sqlEx = ex;
/*      */           }
/*      */         }
/*      */         
/* 4696 */         reportMetrics();
/*      */         
/* 4698 */         if (getUseUsageAdvisor()) {
/* 4699 */           if (!calledExplicitly) {
/* 4700 */             String message = "Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.";
/*      */             
/* 4702 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4710 */           long connectionLifeTime = System.currentTimeMillis() - this.connectionCreationTimeMillis;
/*      */           
/*      */ 
/* 4713 */           if (connectionLifeTime < 500L) {
/* 4714 */             String message = "Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.";
/*      */             
/* 4716 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 4726 */           closeAllOpenStatements();
/*      */         } catch (SQLException ex) {
/* 4728 */           sqlEx = ex;
/*      */         }
/*      */         
/* 4731 */         if (this.io != null) {
/*      */           try {
/* 4733 */             this.io.quit();
/*      */           }
/*      */           catch (Exception e) {}
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 4740 */         this.io.forceClose();
/*      */       }
/*      */       
/* 4743 */       if (this.statementInterceptors != null) {
/* 4744 */         for (int i = 0; i < this.statementInterceptors.size(); i++) {
/* 4745 */           ((StatementInterceptorV2)this.statementInterceptors.get(i)).destroy();
/*      */         }
/*      */       }
/*      */       
/* 4749 */       if (this.exceptionInterceptor != null) {
/* 4750 */         this.exceptionInterceptor.destroy();
/*      */       }
/*      */     } finally {
/* 4753 */       this.openStatements = null;
/* 4754 */       if (this.io != null) {
/* 4755 */         this.io.releaseResources();
/* 4756 */         this.io = null;
/*      */       }
/* 4758 */       this.statementInterceptors = null;
/* 4759 */       this.exceptionInterceptor = null;
/* 4760 */       ProfilerEventHandlerFactory.removeInstance(this);
/*      */       
/* 4762 */       synchronized (getConnectionMutex()) {
/* 4763 */         if (this.cancelTimer != null) {
/* 4764 */           this.cancelTimer.cancel();
/*      */         }
/*      */       }
/*      */       
/* 4768 */       this.isClosed = true;
/*      */     }
/*      */     
/* 4771 */     if (sqlEx != null) {
/* 4772 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException
/*      */   {
/* 4778 */     synchronized (getConnectionMutex()) {
/* 4779 */       if (pstmt.isPoolable()) {
/* 4780 */         synchronized (this.serverSideStatementCache) {
/* 4781 */           this.serverSideStatementCache.put(pstmt.originalSql, pstmt);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerQueryExecutionTime(long queryTimeMs)
/*      */   {
/* 4793 */     if (queryTimeMs > this.longestQueryTimeMs) {
/* 4794 */       this.longestQueryTimeMs = queryTimeMs;
/*      */       
/* 4796 */       repartitionPerformanceHistogram();
/*      */     }
/*      */     
/* 4799 */     addToPerformanceHistogram(queryTimeMs, 1);
/*      */     
/* 4801 */     if (queryTimeMs < this.shortestQueryTimeMs) {
/* 4802 */       this.shortestQueryTimeMs = (queryTimeMs == 0L ? 1L : queryTimeMs);
/*      */     }
/*      */     
/* 4805 */     this.numberOfQueriesIssued += 1L;
/*      */     
/* 4807 */     this.totalQueryTimeMs += queryTimeMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerStatement(Statement stmt)
/*      */   {
/* 4817 */     synchronized (this.openStatements) {
/* 4818 */       this.openStatements.put(stmt, stmt);
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
/*      */   private void repartitionHistogram(int[] histCounts, long[] histBreakpoints, long currentLowerBound, long currentUpperBound)
/*      */   {
/* 4832 */     if (this.oldHistCounts == null) {
/* 4833 */       this.oldHistCounts = new int[histCounts.length];
/* 4834 */       this.oldHistBreakpoints = new long[histBreakpoints.length];
/*      */     }
/*      */     
/* 4837 */     System.arraycopy(histCounts, 0, this.oldHistCounts, 0, histCounts.length);
/*      */     
/* 4839 */     System.arraycopy(histBreakpoints, 0, this.oldHistBreakpoints, 0, histBreakpoints.length);
/*      */     
/*      */ 
/* 4842 */     createInitialHistogram(histBreakpoints, currentLowerBound, currentUpperBound);
/*      */     
/*      */ 
/* 4845 */     for (int i = 0; i < 20; i++) {
/* 4846 */       addToHistogram(histCounts, histBreakpoints, this.oldHistBreakpoints[i], this.oldHistCounts[i], currentLowerBound, currentUpperBound);
/*      */     }
/*      */   }
/*      */   
/*      */   private void repartitionPerformanceHistogram()
/*      */   {
/* 4852 */     checkAndCreatePerformanceHistogram();
/*      */     
/* 4854 */     repartitionHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void repartitionTablesAccessedHistogram()
/*      */   {
/* 4861 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/* 4863 */     repartitionHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void reportMetrics()
/*      */   {
/* 4871 */     if (getGatherPerformanceMetrics()) {
/* 4872 */       StringBuffer logMessage = new StringBuffer(256);
/*      */       
/* 4874 */       logMessage.append("** Performance Metrics Report **\n");
/* 4875 */       logMessage.append("\nLongest reported query: " + this.longestQueryTimeMs + " ms");
/*      */       
/* 4877 */       logMessage.append("\nShortest reported query: " + this.shortestQueryTimeMs + " ms");
/*      */       
/* 4879 */       logMessage.append("\nAverage query execution time: " + this.totalQueryTimeMs / this.numberOfQueriesIssued + " ms");
/*      */       
/*      */ 
/*      */ 
/* 4883 */       logMessage.append("\nNumber of statements executed: " + this.numberOfQueriesIssued);
/*      */       
/* 4885 */       logMessage.append("\nNumber of result sets created: " + this.numberOfResultSetsCreated);
/*      */       
/* 4887 */       logMessage.append("\nNumber of statements prepared: " + this.numberOfPrepares);
/*      */       
/* 4889 */       logMessage.append("\nNumber of prepared statement executions: " + this.numberOfPreparedExecutes);
/*      */       
/*      */ 
/* 4892 */       if (this.perfMetricsHistBreakpoints != null) {
/* 4893 */         logMessage.append("\n\n\tTiming Histogram:\n");
/* 4894 */         int maxNumPoints = 20;
/* 4895 */         int highestCount = Integer.MIN_VALUE;
/*      */         
/* 4897 */         for (int i = 0; i < 20; i++) {
/* 4898 */           if (this.perfMetricsHistCounts[i] > highestCount) {
/* 4899 */             highestCount = this.perfMetricsHistCounts[i];
/*      */           }
/*      */         }
/*      */         
/* 4903 */         if (highestCount == 0) {
/* 4904 */           highestCount = 1;
/*      */         }
/*      */         
/* 4907 */         for (int i = 0; i < 19; i++)
/*      */         {
/* 4909 */           if (i == 0) {
/* 4910 */             logMessage.append("\n\tless than " + this.perfMetricsHistBreakpoints[(i + 1)] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           }
/*      */           else
/*      */           {
/* 4914 */             logMessage.append("\n\tbetween " + this.perfMetricsHistBreakpoints[i] + " and " + this.perfMetricsHistBreakpoints[(i + 1)] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 4920 */           logMessage.append("\t");
/*      */           
/* 4922 */           int numPointsToGraph = (int)(maxNumPoints * (this.perfMetricsHistCounts[i] / highestCount));
/*      */           
/* 4924 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 4925 */             logMessage.append("*");
/*      */           }
/*      */           
/* 4928 */           if (this.longestQueryTimeMs < this.perfMetricsHistCounts[(i + 1)]) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/* 4933 */         if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
/* 4934 */           logMessage.append("\n\tbetween ");
/* 4935 */           logMessage.append(this.perfMetricsHistBreakpoints[18]);
/*      */           
/* 4937 */           logMessage.append(" and ");
/* 4938 */           logMessage.append(this.perfMetricsHistBreakpoints[19]);
/*      */           
/* 4940 */           logMessage.append(" ms: \t");
/* 4941 */           logMessage.append(this.perfMetricsHistCounts[19]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 4946 */       if (this.numTablesMetricsHistBreakpoints != null) {
/* 4947 */         logMessage.append("\n\n\tTable Join Histogram:\n");
/* 4948 */         int maxNumPoints = 20;
/* 4949 */         int highestCount = Integer.MIN_VALUE;
/*      */         
/* 4951 */         for (int i = 0; i < 20; i++) {
/* 4952 */           if (this.numTablesMetricsHistCounts[i] > highestCount) {
/* 4953 */             highestCount = this.numTablesMetricsHistCounts[i];
/*      */           }
/*      */         }
/*      */         
/* 4957 */         if (highestCount == 0) {
/* 4958 */           highestCount = 1;
/*      */         }
/*      */         
/* 4961 */         for (int i = 0; i < 19; i++)
/*      */         {
/* 4963 */           if (i == 0) {
/* 4964 */             logMessage.append("\n\t" + this.numTablesMetricsHistBreakpoints[(i + 1)] + " tables or less: \t\t" + this.numTablesMetricsHistCounts[i]);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 4969 */             logMessage.append("\n\tbetween " + this.numTablesMetricsHistBreakpoints[i] + " and " + this.numTablesMetricsHistBreakpoints[(i + 1)] + " tables: \t" + this.numTablesMetricsHistCounts[i]);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4977 */           logMessage.append("\t");
/*      */           
/* 4979 */           int numPointsToGraph = (int)(maxNumPoints * (this.numTablesMetricsHistCounts[i] / highestCount));
/*      */           
/* 4981 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 4982 */             logMessage.append("*");
/*      */           }
/*      */           
/* 4985 */           if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[(i + 1)]) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/* 4990 */         if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
/* 4991 */           logMessage.append("\n\tbetween ");
/* 4992 */           logMessage.append(this.numTablesMetricsHistBreakpoints[18]);
/*      */           
/* 4994 */           logMessage.append(" and ");
/* 4995 */           logMessage.append(this.numTablesMetricsHistBreakpoints[19]);
/*      */           
/* 4997 */           logMessage.append(" tables: ");
/* 4998 */           logMessage.append(this.numTablesMetricsHistCounts[19]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5003 */       this.log.logInfo(logMessage);
/*      */       
/* 5005 */       this.metricsLastReportedMs = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reportMetricsIfNeeded()
/*      */   {
/* 5014 */     if ((getGatherPerformanceMetrics()) && 
/* 5015 */       (System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis())) {
/* 5016 */       reportMetrics();
/*      */     }
/*      */   }
/*      */   
/*      */   public void reportNumberOfTablesAccessed(int numTablesAccessed)
/*      */   {
/* 5022 */     if (numTablesAccessed < this.minimumNumberTablesAccessed) {
/* 5023 */       this.minimumNumberTablesAccessed = numTablesAccessed;
/*      */     }
/*      */     
/* 5026 */     if (numTablesAccessed > this.maximumNumberTablesAccessed) {
/* 5027 */       this.maximumNumberTablesAccessed = numTablesAccessed;
/*      */       
/* 5029 */       repartitionTablesAccessedHistogram();
/*      */     }
/*      */     
/* 5032 */     addToTablesAccessedHistogram(numTablesAccessed, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetServerState()
/*      */     throws SQLException
/*      */   {
/* 5044 */     if ((!getParanoid()) && (this.io != null) && (versionMeetsMinimum(4, 0, 6)))
/*      */     {
/* 5046 */       changeUser(this.user, this.password);
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
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/* 5060 */     synchronized (getConnectionMutex()) {
/* 5061 */       checkClosed();
/*      */       try
/*      */       {
/* 5064 */         if (this.connectionLifecycleInterceptors != null) {
/* 5065 */           IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */           {
/*      */             void forEach(Extension each) throws SQLException {
/* 5068 */               if (!((ConnectionLifecycleInterceptor)each).rollback()) {
/* 5069 */                 this.stopIterating = true;
/*      */               }
/*      */               
/*      */             }
/* 5073 */           };
/* 5074 */           iter.doForAll();
/*      */           
/* 5076 */           if (!iter.fullIteration()) {
/* 5077 */             jsr 115;return;
/*      */           }
/*      */         }
/*      */         
/* 5081 */         if ((this.autoCommit) && (!getRelaxAutoCommit())) {
/* 5082 */           throw SQLError.createSQLException("Can't call rollback when autocommit=true", "08003", getExceptionInterceptor());
/*      */         }
/*      */         
/* 5085 */         if (this.transactionsSupported) {
/*      */           try {
/* 5087 */             rollbackNoChecks();
/*      */           }
/*      */           catch (SQLException sqlEx) {
/* 5090 */             if ((getIgnoreNonTxTables()) && (sqlEx.getErrorCode() == 1196))
/*      */             {
/* 5092 */               return;
/*      */             }
/* 5094 */             throw sqlEx;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (SQLException sqlException) {
/* 5099 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 5101 */           throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5106 */         throw sqlException;
/*      */       } finally {
/* 5108 */         jsr 5; } localObject2 = returnAddress;this.needsPing = getReconnectAtTxEnd();ret;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rollback(final Savepoint savepoint)
/*      */     throws SQLException
/*      */   {
/* 5118 */     synchronized (getConnectionMutex()) {
/* 5119 */       if ((versionMeetsMinimum(4, 0, 14)) || (versionMeetsMinimum(4, 1, 1))) {
/* 5120 */         checkClosed();
/*      */         try
/*      */         {
/* 5123 */           if (this.connectionLifecycleInterceptors != null) {
/* 5124 */             IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */             {
/*      */               void forEach(Extension each) throws SQLException {
/* 5127 */                 if (!((ConnectionLifecycleInterceptor)each).rollback(savepoint)) {
/* 5128 */                   this.stopIterating = true;
/*      */                 }
/*      */                 
/*      */               }
/* 5132 */             };
/* 5133 */             iter.doForAll();
/*      */             
/* 5135 */             if (!iter.fullIteration()) {
/* 5136 */               jsr 241;return;
/*      */             }
/*      */           }
/*      */           
/* 5140 */           StringBuffer rollbackQuery = new StringBuffer("ROLLBACK TO SAVEPOINT ");
/*      */           
/* 5142 */           rollbackQuery.append('`');
/* 5143 */           rollbackQuery.append(savepoint.getSavepointName());
/* 5144 */           rollbackQuery.append('`');
/*      */           
/* 5146 */           java.sql.Statement stmt = null;
/*      */           try
/*      */           {
/* 5149 */             stmt = getMetadataSafeStatement();
/*      */             
/* 5151 */             stmt.executeUpdate(rollbackQuery.toString());
/*      */           } catch (SQLException sqlEx) {
/* 5153 */             int errno = sqlEx.getErrorCode();
/*      */             
/* 5155 */             if (errno == 1181) {
/* 5156 */               String msg = sqlEx.getMessage();
/*      */               
/* 5158 */               if (msg != null) {
/* 5159 */                 int indexOfError153 = msg.indexOf("153");
/*      */                 
/* 5161 */                 if (indexOfError153 != -1) {
/* 5162 */                   throw SQLError.createSQLException("Savepoint '" + savepoint.getSavepointName() + "' does not exist", "S1009", errno, getExceptionInterceptor());
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5172 */             if ((getIgnoreNonTxTables()) && (sqlEx.getErrorCode() != 1196))
/*      */             {
/* 5174 */               throw sqlEx;
/*      */             }
/*      */             
/* 5177 */             if ("08S01".equals(sqlEx.getSQLState()))
/*      */             {
/* 5179 */               throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007", getExceptionInterceptor());
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 5184 */             throw sqlEx;
/*      */           } finally {
/* 5186 */             closeStatement(stmt);
/*      */           }
/*      */         } finally {
/* 5189 */           jsr 6; } localObject4 = returnAddress;this.needsPing = getReconnectAtTxEnd();ret;
/*      */       }
/*      */       else {
/* 5192 */         throw SQLError.notImplemented();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void rollbackNoChecks() throws SQLException {
/* 5198 */     if ((getUseLocalTransactionState()) && (versionMeetsMinimum(5, 0, 0)) && 
/* 5199 */       (!this.io.inTransactionOnServer())) {
/* 5200 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5204 */     execSQL(null, "rollback", -1, null, 1003, 1007, false, this.database, null, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 5216 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 5218 */     return ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), 1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 5228 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 5230 */     PreparedStatement pStmt = ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), 1003, 1007);
/*      */     
/*      */ 
/*      */ 
/* 5234 */     pStmt.setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
/*      */     
/*      */ 
/* 5237 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 5245 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 5247 */     return ServerPreparedStatement.getInstance(getLoadBalanceSafeProxy(), nativeSql, getCatalog(), resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 5258 */     if ((getPedantic()) && 
/* 5259 */       (resultSetHoldability != 1)) {
/* 5260 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5266 */     return serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 5275 */     PreparedStatement pStmt = (PreparedStatement)serverPrepareStatement(sql);
/*      */     
/* 5277 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyIndexes != null) && (autoGenKeyIndexes.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 5281 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 5289 */     PreparedStatement pStmt = (PreparedStatement)serverPrepareStatement(sql);
/*      */     
/* 5291 */     pStmt.setRetrieveGeneratedKeys((autoGenKeyColNames != null) && (autoGenKeyColNames.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 5295 */     return pStmt;
/*      */   }
/*      */   
/*      */   public boolean serverSupportsConvertFn() throws SQLException {
/* 5299 */     return versionMeetsMinimum(4, 0, 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoCommit(final boolean autoCommitFlag)
/*      */     throws SQLException
/*      */   {
/* 5325 */     synchronized (getConnectionMutex()) {
/* 5326 */       checkClosed();
/*      */       
/* 5328 */       if (this.connectionLifecycleInterceptors != null) {
/* 5329 */         IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */         {
/*      */           void forEach(Extension each) throws SQLException {
/* 5332 */             if (!((ConnectionLifecycleInterceptor)each).setAutoCommit(autoCommitFlag)) {
/* 5333 */               this.stopIterating = true;
/*      */             }
/*      */             
/*      */           }
/* 5337 */         };
/* 5338 */         iter.doForAll();
/*      */         
/* 5340 */         if (!iter.fullIteration()) {
/* 5341 */           return;
/*      */         }
/*      */       }
/*      */       
/* 5345 */       if (getAutoReconnectForPools()) {
/* 5346 */         setHighAvailability(true);
/*      */       }
/*      */       try
/*      */       {
/* 5350 */         if (this.transactionsSupported)
/*      */         {
/* 5352 */           boolean needsSetOnServer = true;
/*      */           
/* 5354 */           if ((getUseLocalSessionState()) && (this.autoCommit == autoCommitFlag))
/*      */           {
/* 5356 */             needsSetOnServer = false;
/* 5357 */           } else if (!getHighAvailability()) {
/* 5358 */             needsSetOnServer = getIO().isSetNeededForAutoCommitMode(autoCommitFlag);
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
/* 5369 */           this.autoCommit = autoCommitFlag;
/*      */           
/* 5371 */           if (needsSetOnServer) {
/* 5372 */             execSQL(null, autoCommitFlag ? "SET autocommit=1" : "SET autocommit=0", -1, null, 1003, 1007, false, this.database, null, false);
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 5380 */           if ((!autoCommitFlag) && (!getRelaxAutoCommit())) {
/* 5381 */             throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 do not support transactions", "08003", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 5386 */           this.autoCommit = autoCommitFlag;
/*      */         }
/*      */       } finally {
/* 5389 */         if (getAutoReconnectForPools()) {
/* 5390 */           setHighAvailability(false);
/*      */         }
/*      */       }
/*      */       
/* 5394 */       return;
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
/*      */   public void setCatalog(final String catalog)
/*      */     throws SQLException
/*      */   {
/* 5412 */     synchronized (getConnectionMutex()) {
/* 5413 */       checkClosed();
/*      */       
/* 5415 */       if (catalog == null) {
/* 5416 */         throw SQLError.createSQLException("Catalog can not be null", "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 5420 */       if (this.connectionLifecycleInterceptors != null) {
/* 5421 */         IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */         {
/*      */           void forEach(Extension each) throws SQLException {
/* 5424 */             if (!((ConnectionLifecycleInterceptor)each).setCatalog(catalog)) {
/* 5425 */               this.stopIterating = true;
/*      */             }
/*      */             
/*      */           }
/* 5429 */         };
/* 5430 */         iter.doForAll();
/*      */         
/* 5432 */         if (!iter.fullIteration()) {
/* 5433 */           return;
/*      */         }
/*      */       }
/*      */       
/* 5437 */       if (getUseLocalSessionState()) {
/* 5438 */         if (this.lowerCaseTableNames) {
/* 5439 */           if (!this.database.equalsIgnoreCase(catalog)) {}
/*      */ 
/*      */ 
/*      */         }
/* 5443 */         else if (this.database.equals(catalog)) {
/* 5444 */           return;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5449 */       String quotedId = this.dbmd.getIdentifierQuoteString();
/*      */       
/* 5451 */       if ((quotedId == null) || (quotedId.equals(" "))) {
/* 5452 */         quotedId = "";
/*      */       }
/*      */       
/* 5455 */       StringBuffer query = new StringBuffer("USE ");
/* 5456 */       query.append(quotedId);
/* 5457 */       query.append(catalog);
/* 5458 */       query.append(quotedId);
/*      */       
/* 5460 */       execSQL(null, query.toString(), -1, null, 1003, 1007, false, this.database, null, false);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5465 */       this.database = catalog;
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
/*      */   public void setInGlobalTx(boolean flag)
/*      */   {
/* 5487 */     this.isInGlobalTx = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadInfoMsgEnabled(boolean flag)
/*      */   {
/* 5500 */     this.readInfoMsg = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadOnly(boolean readOnlyFlag)
/*      */     throws SQLException
/*      */   {
/* 5514 */     checkClosed();
/*      */     
/* 5516 */     setReadOnlyInternal(readOnlyFlag);
/*      */   }
/*      */   
/*      */   public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException
/*      */   {
/* 5521 */     if ((versionMeetsMinimum(5, 6, 5)) && (
/* 5522 */       (!getUseLocalSessionState()) || (readOnlyFlag != this.readOnly))) {
/* 5523 */       execSQL(null, "set session transaction " + (readOnlyFlag ? "read only" : "read write"), -1, null, 1003, 1007, false, this.database, null, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5531 */     this.readOnly = readOnlyFlag;
/*      */   }
/*      */   
/*      */ 
/*      */   public Savepoint setSavepoint()
/*      */     throws SQLException
/*      */   {
/* 5538 */     MysqlSavepoint savepoint = new MysqlSavepoint(getExceptionInterceptor());
/*      */     
/* 5540 */     setSavepoint(savepoint);
/*      */     
/* 5542 */     return savepoint;
/*      */   }
/*      */   
/*      */   private void setSavepoint(MysqlSavepoint savepoint) throws SQLException
/*      */   {
/* 5547 */     synchronized (getConnectionMutex()) {
/* 5548 */       if ((versionMeetsMinimum(4, 0, 14)) || (versionMeetsMinimum(4, 1, 1))) {
/* 5549 */         checkClosed();
/*      */         
/* 5551 */         StringBuffer savePointQuery = new StringBuffer("SAVEPOINT ");
/* 5552 */         savePointQuery.append('`');
/* 5553 */         savePointQuery.append(savepoint.getSavepointName());
/* 5554 */         savePointQuery.append('`');
/*      */         
/* 5556 */         java.sql.Statement stmt = null;
/*      */         try
/*      */         {
/* 5559 */           stmt = getMetadataSafeStatement();
/*      */           
/* 5561 */           stmt.executeUpdate(savePointQuery.toString());
/*      */         } finally {
/* 5563 */           closeStatement(stmt);
/*      */         }
/*      */       } else {
/* 5566 */         throw SQLError.notImplemented();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public Savepoint setSavepoint(String name)
/*      */     throws SQLException
/*      */   {
/* 5575 */     synchronized (getConnectionMutex()) {
/* 5576 */       MysqlSavepoint savepoint = new MysqlSavepoint(name, getExceptionInterceptor());
/*      */       
/* 5578 */       setSavepoint(savepoint);
/*      */       
/* 5580 */       return savepoint;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void setSessionVariables()
/*      */     throws SQLException
/*      */   {
/* 5588 */     if ((versionMeetsMinimum(4, 0, 0)) && (getSessionVariables() != null)) {
/* 5589 */       List<String> variablesToSet = StringUtils.split(getSessionVariables(), ",", "\"'", "\"'", false);
/*      */       
/*      */ 
/* 5592 */       int numVariablesToSet = variablesToSet.size();
/*      */       
/* 5594 */       java.sql.Statement stmt = null;
/*      */       try
/*      */       {
/* 5597 */         stmt = getMetadataSafeStatement();
/*      */         
/* 5599 */         for (int i = 0; i < numVariablesToSet; i++) {
/* 5600 */           String variableValuePair = (String)variablesToSet.get(i);
/*      */           
/* 5602 */           if (variableValuePair.startsWith("@")) {
/* 5603 */             stmt.executeUpdate("SET " + variableValuePair);
/*      */           } else {
/* 5605 */             stmt.executeUpdate("SET SESSION " + variableValuePair);
/*      */           }
/*      */         }
/*      */       } finally {
/* 5609 */         if (stmt != null) {
/* 5610 */           stmt.close();
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
/*      */   public void setTransactionIsolation(int level)
/*      */     throws SQLException
/*      */   {
/* 5626 */     synchronized (getConnectionMutex()) {
/* 5627 */       checkClosed();
/*      */       
/* 5629 */       if (this.hasIsolationLevels) {
/* 5630 */         String sql = null;
/*      */         
/* 5632 */         boolean shouldSendSet = false;
/*      */         
/* 5634 */         if (getAlwaysSendSetIsolation()) {
/* 5635 */           shouldSendSet = true;
/*      */         }
/* 5637 */         else if (level != this.isolationLevel) {
/* 5638 */           shouldSendSet = true;
/*      */         }
/*      */         
/*      */ 
/* 5642 */         if (getUseLocalSessionState()) {
/* 5643 */           shouldSendSet = this.isolationLevel != level;
/*      */         }
/*      */         
/* 5646 */         if (shouldSendSet) {
/* 5647 */           switch (level) {
/*      */           case 0: 
/* 5649 */             throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL", getExceptionInterceptor());
/*      */           
/*      */ 
/*      */           case 2: 
/* 5653 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
/*      */             
/* 5655 */             break;
/*      */           
/*      */           case 1: 
/* 5658 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
/*      */             
/* 5660 */             break;
/*      */           
/*      */           case 4: 
/* 5663 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
/*      */             
/* 5665 */             break;
/*      */           
/*      */           case 8: 
/* 5668 */             sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
/*      */             
/* 5670 */             break;
/*      */           case 3: case 5: case 6: 
/*      */           case 7: default: 
/* 5673 */             throw SQLError.createSQLException("Unsupported transaction isolation level '" + level + "'", "S1C00", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */           
/*      */ 
/* 5678 */           execSQL(null, sql, -1, null, 1003, 1007, false, this.database, null, false);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5683 */           this.isolationLevel = level;
/*      */         }
/*      */       } else {
/* 5686 */         throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", "S1C00", getExceptionInterceptor());
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
/*      */   public void setTypeMap(Map<String, Class<?>> map)
/*      */     throws SQLException
/*      */   {
/* 5703 */     synchronized (getConnectionMutex()) {
/* 5704 */       this.typeMap = map;
/*      */     }
/*      */   }
/*      */   
/*      */   private void setupServerForTruncationChecks() throws SQLException {
/* 5709 */     if ((getJdbcCompliantTruncation()) && 
/* 5710 */       (versionMeetsMinimum(5, 0, 2))) {
/* 5711 */       String currentSqlMode = (String)this.serverVariables.get("sql_mode");
/*      */       
/*      */ 
/* 5714 */       boolean strictTransTablesIsSet = StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1;
/*      */       
/* 5716 */       if ((currentSqlMode == null) || (currentSqlMode.length() == 0) || (!strictTransTablesIsSet))
/*      */       {
/* 5718 */         StringBuffer commandBuf = new StringBuffer("SET sql_mode='");
/*      */         
/* 5720 */         if ((currentSqlMode != null) && (currentSqlMode.length() > 0)) {
/* 5721 */           commandBuf.append(currentSqlMode);
/* 5722 */           commandBuf.append(",");
/*      */         }
/*      */         
/* 5725 */         commandBuf.append("STRICT_TRANS_TABLES'");
/*      */         
/* 5727 */         execSQL(null, commandBuf.toString(), -1, null, 1003, 1007, false, this.database, null, false);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 5732 */         setJdbcCompliantTruncation(false);
/* 5733 */       } else if (strictTransTablesIsSet)
/*      */       {
/* 5735 */         setJdbcCompliantTruncation(false);
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
/*      */   public void shutdownServer()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 5751 */       this.io.sendCommand(8, null, null, false, null, 0);
/*      */     } catch (Exception ex) {
/* 5753 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnhandledExceptionDuringShutdown"), "S1000", getExceptionInterceptor());
/*      */       
/*      */ 
/*      */ 
/* 5757 */       sqlEx.initCause(ex);
/*      */       
/* 5759 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsIsolationLevel()
/*      */   {
/* 5769 */     return this.hasIsolationLevels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsQuotedIdentifiers()
/*      */   {
/* 5778 */     return this.hasQuotedIdentifiers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTransactions()
/*      */   {
/* 5787 */     return this.transactionsSupported;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unregisterStatement(Statement stmt)
/*      */   {
/* 5797 */     if (this.openStatements != null) {
/* 5798 */       synchronized (this.openStatements) {
/* 5799 */         this.openStatements.remove(stmt);
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
/*      */   public void unsetMaxRows(Statement stmt)
/*      */     throws SQLException
/*      */   {
/* 5815 */     synchronized (getConnectionMutex()) {
/* 5816 */       if (this.statementsUsingMaxRows != null) {
/* 5817 */         Object found = this.statementsUsingMaxRows.remove(stmt);
/*      */         
/* 5819 */         if ((found != null) && (this.statementsUsingMaxRows.size() == 0))
/*      */         {
/* 5821 */           execSQL(null, "SET SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.database, null, false);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5826 */           this.maxRowsChanged = false;
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
/*      */   public boolean versionMeetsMinimum(int major, int minor, int subminor)
/*      */     throws SQLException
/*      */   {
/* 5851 */     checkClosed();
/*      */     
/* 5853 */     return this.io.versionMeetsMinimum(major, minor, subminor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet)
/*      */     throws SQLException
/*      */   {
/* 5899 */     if (cachedMetaData == null)
/*      */     {
/*      */ 
/* 5902 */       cachedMetaData = new CachedResultSetMetaData();
/*      */       
/*      */ 
/*      */ 
/* 5906 */       resultSet.buildIndexMapping();
/* 5907 */       resultSet.initializeWithMetadata();
/*      */       
/* 5909 */       if ((resultSet instanceof UpdatableResultSet)) {
/* 5910 */         ((UpdatableResultSet)resultSet).checkUpdatability();
/*      */       }
/*      */       
/* 5913 */       resultSet.populateCachedMetaData(cachedMetaData);
/*      */       
/* 5915 */       this.resultSetMetadataCache.put(sql, cachedMetaData);
/*      */     } else {
/* 5917 */       resultSet.initializeFromCachedMetaData(cachedMetaData);
/* 5918 */       resultSet.initializeWithMetadata();
/*      */       
/* 5920 */       if ((resultSet instanceof UpdatableResultSet)) {
/* 5921 */         ((UpdatableResultSet)resultSet).checkUpdatability();
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
/*      */   public String getStatementComment()
/*      */   {
/* 5934 */     return this.statementComment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStatementComment(String comment)
/*      */   {
/* 5946 */     this.statementComment = comment;
/*      */   }
/*      */   
/*      */   public void reportQueryTime(long millisOrNanos) {
/* 5950 */     synchronized (getConnectionMutex()) {
/* 5951 */       this.queryTimeCount += 1L;
/* 5952 */       this.queryTimeSum += millisOrNanos;
/* 5953 */       this.queryTimeSumSquares += millisOrNanos * millisOrNanos;
/* 5954 */       this.queryTimeMean = ((this.queryTimeMean * (this.queryTimeCount - 1L) + millisOrNanos) / this.queryTimeCount);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isAbonormallyLongQuery(long millisOrNanos)
/*      */   {
/* 5960 */     synchronized (getConnectionMutex()) {
/* 5961 */       if (this.queryTimeCount < 15L) {
/* 5962 */         return false;
/*      */       }
/*      */       
/* 5965 */       double stddev = Math.sqrt((this.queryTimeSumSquares - this.queryTimeSum * this.queryTimeSum / this.queryTimeCount) / (this.queryTimeCount - 1L));
/*      */       
/* 5967 */       return millisOrNanos > this.queryTimeMean + 5.0D * stddev;
/*      */     }
/*      */   }
/*      */   
/*      */   public void initializeExtension(Extension ex) throws SQLException {
/* 5972 */     ex.init(this, this.props);
/*      */   }
/*      */   
/*      */   public void transactionBegun() throws SQLException {
/* 5976 */     synchronized (getConnectionMutex()) {
/* 5977 */       if (this.connectionLifecycleInterceptors != null) {
/* 5978 */         IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */         {
/*      */           void forEach(Extension each) throws SQLException {
/* 5981 */             ((ConnectionLifecycleInterceptor)each).transactionBegun();
/*      */           }
/*      */           
/* 5984 */         };
/* 5985 */         iter.doForAll();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void transactionCompleted() throws SQLException {
/* 5991 */     synchronized (getConnectionMutex()) {
/* 5992 */       if (this.connectionLifecycleInterceptors != null) {
/* 5993 */         IterateBlock<Extension> iter = new IterateBlock(this.connectionLifecycleInterceptors.iterator())
/*      */         {
/*      */           void forEach(Extension each) throws SQLException {
/* 5996 */             ((ConnectionLifecycleInterceptor)each).transactionCompleted();
/*      */           }
/*      */           
/* 5999 */         };
/* 6000 */         iter.doForAll();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean storesLowerCaseTableName() {
/* 6006 */     return this.storesLowerCaseTableName;
/*      */   }
/*      */   
/*      */ 
/*      */   public ExceptionInterceptor getExceptionInterceptor()
/*      */   {
/* 6012 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   public boolean getRequiresEscapingEncoder() {
/* 6016 */     return this.requiresEscapingEncoder;
/*      */   }
/*      */   
/*      */   public boolean isServerLocal() throws SQLException {
/* 6020 */     synchronized (getConnectionMutex()) {
/* 6021 */       SocketFactory factory = getIO().socketFactory;
/*      */       
/* 6023 */       if ((factory instanceof SocketMetadata)) {
/* 6024 */         return ((SocketMetadata)factory).isLocallyConnected(this);
/*      */       }
/* 6026 */       getLog().logWarn(Messages.getString("Connection.NoMetadataOnSocketFactory"));
/* 6027 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSchema(String schema)
/*      */     throws SQLException
/*      */   {
/* 6034 */     synchronized (getConnectionMutex()) {
/* 6035 */       checkClosed();
/*      */     }
/*      */   }
/*      */   
/*      */   public String getSchema() throws SQLException
/*      */   {
/* 6041 */     synchronized (getConnectionMutex()) {
/* 6042 */       checkClosed();
/*      */       
/* 6044 */       return null;
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
/*      */   public void abort(Executor executor)
/*      */     throws SQLException
/*      */   {
/* 6085 */     SecurityManager sec = System.getSecurityManager();
/*      */     
/* 6087 */     if (sec != null) {
/* 6088 */       sec.checkPermission(ABORT_PERM);
/*      */     }
/*      */     
/* 6091 */     if (executor == null) {
/* 6092 */       throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/* 6095 */     executor.execute(new Runnable()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 6099 */           ConnectionImpl.this.abortInternal();
/*      */         } catch (SQLException e) {
/* 6101 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public void setNetworkTimeout(Executor executor, final int milliseconds) throws SQLException
/*      */   {
/* 6109 */     synchronized (getConnectionMutex()) {
/* 6110 */       SecurityManager sec = System.getSecurityManager();
/*      */       
/* 6112 */       if (sec != null) {
/* 6113 */         sec.checkPermission(SET_NETWORK_TIMEOUT_PERM);
/*      */       }
/*      */       
/* 6116 */       if (executor == null) {
/* 6117 */         throw SQLError.createSQLException("Executor can not be null", "S1009", getExceptionInterceptor());
/*      */       }
/*      */       
/* 6120 */       checkClosed();
/* 6121 */       final MysqlIO mysqlIo = this.io;
/*      */       
/* 6123 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*      */           try {
/* 6127 */             ConnectionImpl.this.setSocketTimeout(milliseconds);
/* 6128 */             mysqlIo.setSocketTimeout(milliseconds);
/*      */           } catch (SQLException e) {
/* 6130 */             throw new RuntimeException(e);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   public int getNetworkTimeout() throws SQLException {
/* 6138 */     synchronized (getConnectionMutex()) {
/* 6139 */       checkClosed();
/* 6140 */       return getSocketTimeout();
/*      */     }
/*      */   }
/*      */   
/*      */   protected ConnectionImpl() {}
/*      */   
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   /* Error */
/*      */   public int getActiveStatementCount()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 136	com/mysql/jdbc/ConnectionImpl:openStatements	Ljava/util/Map;
/*      */     //   4: ifnull +27 -> 31
/*      */     //   7: aload_0
/*      */     //   8: getfield 136	com/mysql/jdbc/ConnectionImpl:openStatements	Ljava/util/Map;
/*      */     //   11: dup
/*      */     //   12: astore_1
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 136	com/mysql/jdbc/ConnectionImpl:openStatements	Ljava/util/Map;
/*      */     //   18: invokeinterface 248 1 0
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: ireturn
/*      */     //   26: astore_2
/*      */     //   27: aload_1
/*      */     //   28: monitorexit
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     //   31: iconst_0
/*      */     //   32: ireturn
/*      */     // Line number table:
/*      */     //   Java source line #2951	-> byte code offset #0
/*      */     //   Java source line #2952	-> byte code offset #7
/*      */     //   Java source line #2953	-> byte code offset #14
/*      */     //   Java source line #2954	-> byte code offset #26
/*      */     //   Java source line #2957	-> byte code offset #31
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	33	0	this	ConnectionImpl
/*      */     //   12	16	1	Ljava/lang/Object;	Object
/*      */     //   26	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	25	26	finally
/*      */     //   26	29	26	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean getAutoCommit()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 50	com/mysql/jdbc/ConnectionImpl:autoCommit	Z
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: ireturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2969	-> byte code offset #0
/*      */     //   Java source line #2970	-> byte code offset #7
/*      */     //   Java source line #2971	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	ConnectionImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String getCatalog()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 58	com/mysql/jdbc/ConnectionImpl:database	Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #2998	-> byte code offset #0
/*      */     //   Java source line #2999	-> byte code offset #7
/*      */     //   Java source line #3000	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	ConnectionImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String getCharacterSetMetadata()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 51	com/mysql/jdbc/ConnectionImpl:characterSetMetadata	Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3007	-> byte code offset #0
/*      */     //   Java source line #3008	-> byte code offset #7
/*      */     //   Java source line #3009	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	ConnectionImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isMasterConnection()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: iconst_0
/*      */     //   8: aload_1
/*      */     //   9: monitorexit
/*      */     //   10: ireturn
/*      */     //   11: astore_2
/*      */     //   12: aload_1
/*      */     //   13: monitorexit
/*      */     //   14: aload_2
/*      */     //   15: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3915	-> byte code offset #0
/*      */     //   Java source line #3916	-> byte code offset #7
/*      */     //   Java source line #3917	-> byte code offset #11
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	16	0	this	ConnectionImpl
/*      */     //   5	8	1	Ljava/lang/Object;	Object
/*      */     //   11	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	10	11	finally
/*      */     //   11	14	11	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean isReadOnly(boolean useSessionStatus)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iload_1
/*      */     //   1: ifeq +170 -> 171
/*      */     //   4: aload_0
/*      */     //   5: getfield 68	com/mysql/jdbc/ConnectionImpl:isClosed	Z
/*      */     //   8: ifne +163 -> 171
/*      */     //   11: aload_0
/*      */     //   12: iconst_5
/*      */     //   13: bipush 6
/*      */     //   15: iconst_5
/*      */     //   16: invokevirtual 220	com/mysql/jdbc/ConnectionImpl:versionMeetsMinimum	(III)Z
/*      */     //   19: ifeq +152 -> 171
/*      */     //   22: aload_0
/*      */     //   23: invokevirtual 590	com/mysql/jdbc/ConnectionImpl:getUseLocalSessionState	()Z
/*      */     //   26: ifne +145 -> 171
/*      */     //   29: aconst_null
/*      */     //   30: astore_2
/*      */     //   31: aconst_null
/*      */     //   32: astore_3
/*      */     //   33: aload_0
/*      */     //   34: invokevirtual 224	com/mysql/jdbc/ConnectionImpl:getMetadataSafeStatement	()Ljava/sql/Statement;
/*      */     //   37: astore_2
/*      */     //   38: aload_2
/*      */     //   39: ldc_w 674
/*      */     //   42: invokeinterface 226 2 0
/*      */     //   47: astore_3
/*      */     //   48: aload_3
/*      */     //   49: invokeinterface 228 1 0
/*      */     //   54: ifeq +26 -> 80
/*      */     //   57: aload_3
/*      */     //   58: iconst_1
/*      */     //   59: invokeinterface 675 2 0
/*      */     //   64: ifeq +7 -> 71
/*      */     //   67: iconst_1
/*      */     //   68: goto +4 -> 72
/*      */     //   71: iconst_0
/*      */     //   72: istore 4
/*      */     //   74: jsr +59 -> 133
/*      */     //   77: iload 4
/*      */     //   79: ireturn
/*      */     //   80: goto +39 -> 119
/*      */     //   83: astore 4
/*      */     //   85: aload 4
/*      */     //   87: invokevirtual 8	java/sql/SQLException:getErrorCode	()I
/*      */     //   90: sipush 1820
/*      */     //   93: if_icmpne +10 -> 103
/*      */     //   96: aload_0
/*      */     //   97: invokevirtual 233	com/mysql/jdbc/ConnectionImpl:getDisconnectOnExpiredPasswords	()Z
/*      */     //   100: ifeq +19 -> 119
/*      */     //   103: ldc_w 676
/*      */     //   106: ldc_w 386
/*      */     //   109: aload 4
/*      */     //   111: aload_0
/*      */     //   112: invokevirtual 130	com/mysql/jdbc/ConnectionImpl:getExceptionInterceptor	()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   115: invokestatic 434	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   118: athrow
/*      */     //   119: jsr +14 -> 133
/*      */     //   122: goto +49 -> 171
/*      */     //   125: astore 5
/*      */     //   127: jsr +6 -> 133
/*      */     //   130: aload 5
/*      */     //   132: athrow
/*      */     //   133: astore 6
/*      */     //   135: aload_3
/*      */     //   136: ifnull +16 -> 152
/*      */     //   139: aload_3
/*      */     //   140: invokeinterface 257 1 0
/*      */     //   145: goto +5 -> 150
/*      */     //   148: astore 7
/*      */     //   150: aconst_null
/*      */     //   151: astore_3
/*      */     //   152: aload_2
/*      */     //   153: ifnull +16 -> 169
/*      */     //   156: aload_2
/*      */     //   157: invokeinterface 258 1 0
/*      */     //   162: goto +5 -> 167
/*      */     //   165: astore 7
/*      */     //   167: aconst_null
/*      */     //   168: astore_2
/*      */     //   169: ret 6
/*      */     //   171: aload_0
/*      */     //   172: getfield 98	com/mysql/jdbc/ConnectionImpl:readOnly	Z
/*      */     //   175: ireturn
/*      */     // Line number table:
/*      */     //   Java source line #3958	-> byte code offset #0
/*      */     //   Java source line #3959	-> byte code offset #29
/*      */     //   Java source line #3960	-> byte code offset #31
/*      */     //   Java source line #3964	-> byte code offset #33
/*      */     //   Java source line #3966	-> byte code offset #38
/*      */     //   Java source line #3967	-> byte code offset #48
/*      */     //   Java source line #3968	-> byte code offset #57
/*      */     //   Java source line #3976	-> byte code offset #80
/*      */     //   Java source line #3970	-> byte code offset #83
/*      */     //   Java source line #3971	-> byte code offset #85
/*      */     //   Java source line #3972	-> byte code offset #103
/*      */     //   Java source line #3978	-> byte code offset #119
/*      */     //   Java source line #3998	-> byte code offset #122
/*      */     //   Java source line #3979	-> byte code offset #125
/*      */     //   Java source line #3981	-> byte code offset #139
/*      */     //   Java source line #3984	-> byte code offset #145
/*      */     //   Java source line #3982	-> byte code offset #148
/*      */     //   Java source line #3986	-> byte code offset #150
/*      */     //   Java source line #3989	-> byte code offset #152
/*      */     //   Java source line #3991	-> byte code offset #156
/*      */     //   Java source line #3994	-> byte code offset #162
/*      */     //   Java source line #3992	-> byte code offset #165
/*      */     //   Java source line #3996	-> byte code offset #167
/*      */     //   Java source line #4001	-> byte code offset #171
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	176	0	this	ConnectionImpl
/*      */     //   0	176	1	useSessionStatus	boolean
/*      */     //   30	139	2	stmt	java.sql.Statement
/*      */     //   32	120	3	rs	ResultSet
/*      */     //   72	6	4	bool	boolean
/*      */     //   83	27	4	ex1	SQLException
/*      */     //   125	6	5	localObject1	Object
/*      */     //   133	1	6	localObject2	Object
/*      */     //   148	3	7	ex	Exception
/*      */     //   165	3	7	ex	Exception
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   33	74	83	java/sql/SQLException
/*      */     //   33	77	125	finally
/*      */     //   80	122	125	finally
/*      */     //   125	130	125	finally
/*      */     //   139	145	148	java/lang/Exception
/*      */     //   156	162	165	java/lang/Exception
/*      */   }
/*      */   
/*      */   public void releaseSavepoint(Savepoint arg0)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   /* Error */
/*      */   public void setFailedOver(boolean flag)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_2
/*      */     //   8: monitorexit
/*      */     //   9: goto +8 -> 17
/*      */     //   12: astore_3
/*      */     //   13: aload_2
/*      */     //   14: monitorexit
/*      */     //   15: aload_3
/*      */     //   16: athrow
/*      */     //   17: return
/*      */     // Line number table:
/*      */     //   Java source line #5474	-> byte code offset #0
/*      */     //   Java source line #5476	-> byte code offset #7
/*      */     //   Java source line #5477	-> byte code offset #17
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	18	0	this	ConnectionImpl
/*      */     //   0	18	1	flag	boolean
/*      */     //   5	9	2	Ljava/lang/Object;	Object
/*      */     //   12	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	9	12	finally
/*      */     //   12	15	12	finally
/*      */   }
/*      */   
/*      */   public void setHoldability(int arg0)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   public void setPreferSlaveDuringFailover(boolean flag) {}
/*      */   
/*      */   /* Error */
/*      */   public boolean useAnsiQuotedIdentifiers()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 104	com/mysql/jdbc/ConnectionImpl:useAnsiQuotes	Z
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: ireturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5833	-> byte code offset #0
/*      */     //   Java source line #5834	-> byte code offset #7
/*      */     //   Java source line #5835	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	ConnectionImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean useMaxRows()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 28	com/mysql/jdbc/ConnectionImpl:getConnectionMutex	()Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 79	com/mysql/jdbc/ConnectionImpl:maxRowsChanged	Z
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: ireturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5844	-> byte code offset #0
/*      */     //   Java source line #5845	-> byte code offset #7
/*      */     //   Java source line #5846	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	ConnectionImpl
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public CachedResultSetMetaData getCachedMetaData(String sql)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 619	com/mysql/jdbc/ConnectionImpl:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   4: ifnull +29 -> 33
/*      */     //   7: aload_0
/*      */     //   8: getfield 619	com/mysql/jdbc/ConnectionImpl:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   11: dup
/*      */     //   12: astore_2
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 619	com/mysql/jdbc/ConnectionImpl:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   18: aload_1
/*      */     //   19: invokevirtual 265	com/mysql/jdbc/util/LRUCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   22: checkcast 868	com/mysql/jdbc/CachedResultSetMetaData
/*      */     //   25: aload_2
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: astore_3
/*      */     //   29: aload_2
/*      */     //   30: monitorexit
/*      */     //   31: aload_3
/*      */     //   32: athrow
/*      */     //   33: aconst_null
/*      */     //   34: areturn
/*      */     // Line number table:
/*      */     //   Java source line #5871	-> byte code offset #0
/*      */     //   Java source line #5872	-> byte code offset #7
/*      */     //   Java source line #5873	-> byte code offset #14
/*      */     //   Java source line #5875	-> byte code offset #28
/*      */     //   Java source line #5878	-> byte code offset #33
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	ConnectionImpl
/*      */     //   0	35	1	sql	String
/*      */     //   12	18	2	Ljava/lang/Object;	Object
/*      */     //   28	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	27	28	finally
/*      */     //   28	31	28	finally
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */