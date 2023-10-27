/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class LoadBalancingConnectionProxy
/*      */   implements InvocationHandler, PingTarget
/*      */ {
/*      */   private static Method getLocalTimeMethod;
/*   68 */   private long totalPhysicalConnections = 0L;
/*   69 */   private long activePhysicalConnections = 0L;
/*   70 */   private String hostToRemove = null;
/*   71 */   private long lastUsed = 0L;
/*   72 */   private long transactionCount = 0L;
/*   73 */   private ConnectionGroup connectionGroup = null;
/*   74 */   protected String closedReason = null;
/*   75 */   protected boolean closedExplicitly = false;
/*   76 */   protected boolean autoReconnect = false;
/*      */   
/*      */   public static final String BLACKLIST_TIMEOUT_PROPERTY_KEY = "loadBalanceBlacklistTimeout";
/*      */   
/*      */   protected MySQLConnection currentConn;
/*      */   
/*      */   protected List<String> hostList;
/*      */   
/*      */   protected Map<String, ConnectionImpl> liveConnections;
/*      */   
/*      */   private Map<ConnectionImpl, String> connectionsToHostsMap;
/*      */   
/*      */   private long[] responseTimes;
/*      */   
/*      */   private Map<String, Integer> hostsToListIndexMap;
/*      */   
/*      */   protected class ConnectionErrorFiringInvocationHandler
/*      */     implements InvocationHandler
/*      */   {
/*   95 */     Object invokeOn = null;
/*      */     
/*      */     public ConnectionErrorFiringInvocationHandler(Object toInvokeOn) {
/*   98 */       this.invokeOn = toInvokeOn;
/*      */     }
/*      */     
/*      */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*      */     {
/*  103 */       Object result = null;
/*      */       try
/*      */       {
/*  106 */         result = method.invoke(this.invokeOn, args);
/*      */         
/*  108 */         if (result != null) {
/*  109 */           result = LoadBalancingConnectionProxy.this.proxyIfInterfaceIsJdbc(result, result.getClass());
/*      */         }
/*      */       } catch (InvocationTargetException e) {
/*  112 */         LoadBalancingConnectionProxy.this.dealWithInvocationException(e);
/*      */       }
/*      */       
/*  115 */       return result;
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
/*  131 */   private boolean inTransaction = false;
/*      */   
/*  133 */   private long transactionStartTime = 0L;
/*      */   
/*      */   private Properties localProps;
/*      */   
/*  137 */   protected boolean isClosed = false;
/*      */   
/*      */   private BalanceStrategy balancer;
/*      */   
/*      */   private int retriesAllDown;
/*      */   
/*      */   private static Map<String, Long> globalBlacklist;
/*      */   
/*  145 */   private int globalBlacklistTimeout = 0;
/*      */   
/*  147 */   private long connectionGroupProxyID = 0L;
/*      */   
/*      */   private LoadBalanceExceptionChecker exceptionChecker;
/*      */   
/*  151 */   private Map<Class<?>, Boolean> jdbcInterfacesForProxyCache = new HashMap();
/*      */   
/*  153 */   private MySQLConnection thisAsConnection = null;
/*      */   
/*  155 */   private int autoCommitSwapThreshold = 0;
/*      */   private static Constructor<?> JDBC_4_LB_CONNECTION_CTOR;
/*      */   
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*   82 */       getLocalTimeMethod = System.class.getMethod("nanoTime", new Class[0]);
/*      */     }
/*      */     catch (SecurityException e) {}catch (NoSuchMethodException e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */     globalBlacklist = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */     if (Util.isJdbc4()) {
/*      */       try {
/*  162 */         JDBC_4_LB_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4LoadBalancedMySQLConnection").getConstructor(new Class[] { LoadBalancingConnectionProxy.class });
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*  166 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  168 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  170 */         throw new RuntimeException(e);
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
/*      */   LoadBalancingConnectionProxy(List<String> hosts, Properties props)
/*      */     throws SQLException
/*      */   {
/*  192 */     String group = props.getProperty("loadBalanceConnectionGroup", null);
/*      */     
/*  194 */     boolean enableJMX = false;
/*  195 */     String enableJMXAsString = props.getProperty("loadBalanceEnableJMX", "false");
/*      */     try
/*      */     {
/*  198 */       enableJMX = Boolean.parseBoolean(enableJMXAsString);
/*      */     } catch (Exception e) {
/*  200 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceEnableJMX", new Object[] { enableJMXAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  206 */     if (group != null) {
/*  207 */       this.connectionGroup = ConnectionGroupManager.getConnectionGroupInstance(group);
/*  208 */       if (enableJMX) {
/*  209 */         ConnectionGroupManager.registerJmx();
/*      */       }
/*  211 */       this.connectionGroupProxyID = this.connectionGroup.registerConnectionProxy(this, hosts);
/*  212 */       hosts = new ArrayList(this.connectionGroup.getInitialHosts());
/*      */     }
/*      */     
/*  215 */     this.autoReconnect = (("true".equalsIgnoreCase(props.getProperty("autoReconnect"))) || ("true".equalsIgnoreCase(props.getProperty("autoReconnectForPools"))));
/*      */     
/*      */ 
/*  218 */     this.hostList = hosts;
/*      */     
/*  220 */     int numHosts = this.hostList.size();
/*      */     
/*  222 */     this.liveConnections = new HashMap(numHosts);
/*  223 */     this.connectionsToHostsMap = new HashMap(numHosts);
/*  224 */     this.responseTimes = new long[numHosts];
/*  225 */     this.hostsToListIndexMap = new HashMap(numHosts);
/*      */     
/*  227 */     this.localProps = ((Properties)props.clone());
/*  228 */     this.localProps.remove("HOST");
/*  229 */     this.localProps.remove("PORT");
/*      */     
/*  231 */     for (int i = 0; i < numHosts; i++) {
/*  232 */       this.hostsToListIndexMap.put(this.hostList.get(i), Integer.valueOf(i));
/*  233 */       this.localProps.remove("HOST." + (i + 1));
/*      */       
/*  235 */       this.localProps.remove("PORT." + (i + 1));
/*      */     }
/*      */     
/*      */ 
/*  239 */     this.localProps.remove("NUM_HOSTS");
/*  240 */     this.localProps.setProperty("useLocalSessionState", "true");
/*      */     
/*  242 */     String strategy = this.localProps.getProperty("loadBalanceStrategy", "random");
/*      */     
/*      */ 
/*  245 */     String lbExceptionChecker = this.localProps.getProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker");
/*      */     
/*      */ 
/*      */ 
/*  249 */     String retriesAllDownAsString = this.localProps.getProperty("retriesAllDown", "120");
/*      */     
/*      */     try
/*      */     {
/*  253 */       this.retriesAllDown = Integer.parseInt(retriesAllDownAsString);
/*      */     } catch (NumberFormatException nfe) {
/*  255 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForRetriesAllDown", new Object[] { retriesAllDownAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  260 */     String blacklistTimeoutAsString = this.localProps.getProperty("loadBalanceBlacklistTimeout", "0");
/*      */     
/*      */     try
/*      */     {
/*  264 */       this.globalBlacklistTimeout = Integer.parseInt(blacklistTimeoutAsString);
/*      */     }
/*      */     catch (NumberFormatException nfe) {
/*  267 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceBlacklistTimeout", new Object[] { retriesAllDownAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */     if ("random".equals(strategy)) {
/*  277 */       this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, "com.mysql.jdbc.RandomBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0));
/*      */ 
/*      */     }
/*  280 */     else if ("bestResponseTime".equals(strategy)) {
/*  281 */       this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, "com.mysql.jdbc.BestResponseTimeBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0));
/*      */     }
/*      */     else
/*      */     {
/*  285 */       this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, strategy, "InvalidLoadBalanceStrategy", null).get(0));
/*      */     }
/*      */     
/*      */ 
/*  289 */     String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
/*      */     try
/*      */     {
/*  292 */       this.autoCommitSwapThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
/*      */     } catch (NumberFormatException nfe) {
/*  294 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceAutoCommitStatementThreshold", new Object[] { autoCommitSwapThresholdAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  300 */     String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
/*  301 */     if (!"".equals(autoCommitSwapRegex)) {
/*      */       try {
/*  303 */         "".matches(autoCommitSwapRegex);
/*      */       } catch (Exception e) {
/*  305 */         throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceAutoCommitStatementRegex", new Object[] { autoCommitSwapRegex }), "S1009", null);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  312 */     if (this.autoCommitSwapThreshold > 0) {
/*  313 */       String statementInterceptors = this.localProps.getProperty("statementInterceptors");
/*  314 */       if (statementInterceptors == null) {
/*  315 */         this.localProps.setProperty("statementInterceptors", "com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
/*  316 */       } else if (statementInterceptors.length() > 0) {
/*  317 */         this.localProps.setProperty("statementInterceptors", statementInterceptors + ",com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
/*      */       }
/*  319 */       props.setProperty("statementInterceptors", this.localProps.getProperty("statementInterceptors"));
/*      */     }
/*      */     
/*  322 */     this.balancer.init(null, props);
/*      */     
/*      */ 
/*  325 */     this.exceptionChecker = ((LoadBalanceExceptionChecker)Util.loadExtensions(null, props, lbExceptionChecker, "InvalidLoadBalanceExceptionChecker", null).get(0));
/*      */     
/*  327 */     this.exceptionChecker.init(null, props);
/*      */     
/*  329 */     if ((Util.isJdbc4()) || (JDBC_4_LB_CONNECTION_CTOR != null)) {
/*  330 */       this.thisAsConnection = ((MySQLConnection)Util.handleNewInstance(JDBC_4_LB_CONNECTION_CTOR, new Object[] { this }, null));
/*      */     }
/*      */     else {
/*  333 */       this.thisAsConnection = new LoadBalancedMySQLConnection(this);
/*      */     }
/*  335 */     pickNewConnection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized ConnectionImpl createConnectionForHost(String hostPortSpec)
/*      */     throws SQLException
/*      */   {
/*  350 */     Properties connProps = (Properties)this.localProps.clone();
/*      */     
/*  352 */     String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(hostPortSpec);
/*      */     
/*  354 */     String hostName = hostPortPair[0];
/*  355 */     String portNumber = hostPortPair[1];
/*  356 */     String dbName = connProps.getProperty("DBNAME");
/*      */     
/*      */ 
/*  359 */     if (hostName == null) {
/*  360 */       throw new SQLException("Could not find a hostname to start a connection to");
/*      */     }
/*      */     
/*  363 */     if (portNumber == null) {
/*  364 */       portNumber = "3306";
/*      */     }
/*      */     
/*  367 */     connProps.setProperty("HOST", hostName);
/*  368 */     connProps.setProperty("PORT", portNumber);
/*      */     
/*  370 */     connProps.setProperty("HOST.1", hostName);
/*      */     
/*  372 */     connProps.setProperty("PORT.1", portNumber);
/*      */     
/*  374 */     connProps.setProperty("NUM_HOSTS", "1");
/*  375 */     connProps.setProperty("roundRobinLoadBalance", "false");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  382 */     ConnectionImpl conn = (ConnectionImpl)ConnectionImpl.getInstance(hostName, Integer.parseInt(portNumber), connProps, dbName, "jdbc:mysql://" + hostName + ":" + portNumber + "/");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  388 */     this.liveConnections.put(hostPortSpec, conn);
/*  389 */     this.connectionsToHostsMap.put(conn, hostPortSpec);
/*      */     
/*      */ 
/*  392 */     this.activePhysicalConnections += 1L;
/*  393 */     this.totalPhysicalConnections += 1L;
/*      */     
/*  395 */     conn.setProxy(this.thisAsConnection);
/*  396 */     conn.setRealProxy(this);
/*      */     
/*  398 */     return conn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void dealWithInvocationException(InvocationTargetException e)
/*      */     throws SQLException, Throwable, InvocationTargetException
/*      */   {
/*  409 */     Throwable t = e.getTargetException();
/*      */     
/*  411 */     if (t != null) {
/*  412 */       if (((t instanceof SQLException)) && (shouldExceptionTriggerFailover((SQLException)t))) {
/*  413 */         invalidateCurrentConnection();
/*  414 */         pickNewConnection();
/*      */       }
/*      */       
/*  417 */       throw t;
/*      */     }
/*      */     
/*  420 */     throw e;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   synchronized void invalidateCurrentConnection()
/*      */     throws SQLException
/*      */   {
/*  429 */     invalidateConnection(this.currentConn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   synchronized void invalidateConnection(MySQLConnection conn)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  440 */       if (!conn.isClosed()) {
/*  441 */         conn.close();
/*      */       }
/*      */       
/*      */     }
/*      */     catch (SQLException e) {}finally
/*      */     {
/*  447 */       if (isGlobalBlacklistEnabled()) {
/*  448 */         addToGlobalBlacklist((String)this.connectionsToHostsMap.get(conn));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  453 */       this.liveConnections.remove(this.connectionsToHostsMap.get(conn));
/*      */       
/*  455 */       Object mappedHost = this.connectionsToHostsMap.remove(conn);
/*      */       
/*  457 */       if ((mappedHost != null) && (this.hostsToListIndexMap.containsKey(mappedHost)))
/*      */       {
/*  459 */         int hostIndex = ((Integer)this.hostsToListIndexMap.get(mappedHost)).intValue();
/*      */         
/*      */ 
/*  462 */         synchronized (this.responseTimes) {
/*  463 */           this.responseTimes[hostIndex] = 0L;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void closeAllConnections()
/*      */   {
/*  471 */     synchronized (this)
/*      */     {
/*  473 */       Iterator<ConnectionImpl> allConnections = this.liveConnections.values().iterator();
/*      */       
/*  475 */       while (allConnections.hasNext()) {
/*      */         try {
/*  477 */           this.activePhysicalConnections -= 1L;
/*  478 */           ((ConnectionImpl)allConnections.next()).close();
/*      */         }
/*      */         catch (SQLException e) {}
/*      */       }
/*      */       
/*  483 */       if (!this.isClosed) {
/*  484 */         this.balancer.destroy();
/*  485 */         if (this.connectionGroup != null) {
/*  486 */           this.connectionGroup.closeConnectionProxy(this);
/*      */         }
/*      */       }
/*      */       
/*  490 */       this.liveConnections.clear();
/*  491 */       this.connectionsToHostsMap.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private void abortAllConnectionsInternal()
/*      */   {
/*  497 */     synchronized (this)
/*      */     {
/*  499 */       Iterator<ConnectionImpl> allConnections = this.liveConnections.values().iterator();
/*      */       
/*  501 */       while (allConnections.hasNext()) {
/*      */         try {
/*  503 */           this.activePhysicalConnections -= 1L;
/*  504 */           ((ConnectionImpl)allConnections.next()).abortInternal();
/*      */         }
/*      */         catch (SQLException e) {}
/*      */       }
/*      */       
/*  509 */       if (!this.isClosed) {
/*  510 */         this.balancer.destroy();
/*  511 */         if (this.connectionGroup != null) {
/*  512 */           this.connectionGroup.closeConnectionProxy(this);
/*      */         }
/*      */       }
/*      */       
/*  516 */       this.liveConnections.clear();
/*  517 */       this.connectionsToHostsMap.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private void abortAllConnections(Executor executor) {
/*  522 */     synchronized (this)
/*      */     {
/*  524 */       Iterator<ConnectionImpl> allConnections = this.liveConnections.values().iterator();
/*      */       
/*  526 */       while (allConnections.hasNext()) {
/*      */         try {
/*  528 */           this.activePhysicalConnections -= 1L;
/*  529 */           ((ConnectionImpl)allConnections.next()).abort(executor);
/*      */         }
/*      */         catch (SQLException e) {}
/*      */       }
/*      */       
/*  534 */       if (!this.isClosed) {
/*  535 */         this.balancer.destroy();
/*  536 */         if (this.connectionGroup != null) {
/*  537 */           this.connectionGroup.closeConnectionProxy(this);
/*      */         }
/*      */       }
/*      */       
/*  541 */       this.liveConnections.clear();
/*  542 */       this.connectionsToHostsMap.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*      */   {
/*  548 */     return invoke(proxy, method, args, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Object invoke(Object proxy, Method method, Object[] args, boolean swapAtTransactionBoundary)
/*      */     throws Throwable
/*      */   {
/*  566 */     String methodName = method.getName();
/*      */     
/*  568 */     if ("getLoadBalanceSafeProxy".equals(methodName)) {
/*  569 */       return this.currentConn;
/*      */     }
/*      */     
/*  572 */     if (("equals".equals(methodName)) && (args.length == 1)) {
/*  573 */       if ((args[0] instanceof Proxy)) {
/*  574 */         return Boolean.valueOf(((Proxy)args[0]).equals(this));
/*      */       }
/*  576 */       return Boolean.valueOf(equals(args[0]));
/*      */     }
/*      */     
/*  579 */     if ("hashCode".equals(methodName)) {
/*  580 */       return Integer.valueOf(hashCode());
/*      */     }
/*      */     
/*  583 */     if ("close".equals(methodName)) {
/*  584 */       closeAllConnections();
/*      */       
/*  586 */       this.isClosed = true;
/*  587 */       this.closedReason = "Connection explicitly closed.";
/*  588 */       this.closedExplicitly = true;
/*      */       
/*  590 */       return null;
/*      */     }
/*      */     
/*  593 */     if ("abortInternal".equals(methodName)) {
/*  594 */       abortAllConnectionsInternal();
/*  595 */       this.isClosed = true;
/*  596 */       this.closedReason = "Connection explicitly closed.";
/*  597 */       return null;
/*      */     }
/*      */     
/*  600 */     if (("abort".equals(methodName)) && (args.length == 1)) {
/*  601 */       abortAllConnections((Executor)args[0]);
/*  602 */       this.isClosed = true;
/*  603 */       this.closedReason = "Connection explicitly closed.";
/*  604 */       return null;
/*      */     }
/*      */     
/*  607 */     if ("isClosed".equals(methodName)) {
/*  608 */       return Boolean.valueOf(this.isClosed);
/*      */     }
/*      */     
/*  611 */     if (this.isClosed) {
/*  612 */       if ((this.autoReconnect) && (!this.closedExplicitly))
/*      */       {
/*  614 */         this.currentConn = null;
/*  615 */         pickNewConnection();
/*  616 */         this.isClosed = false;
/*  617 */         this.closedReason = null;
/*      */       } else {
/*  619 */         String reason = "No operations allowed after connection closed.";
/*  620 */         if (this.closedReason != null) {
/*  621 */           reason = reason + "  " + this.closedReason;
/*      */         }
/*  623 */         throw SQLError.createSQLException(reason, "08003", null);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  633 */     if (!this.inTransaction) {
/*  634 */       this.inTransaction = true;
/*  635 */       this.transactionStartTime = getLocalTimeBestResolution();
/*  636 */       this.transactionCount += 1L;
/*      */     }
/*      */     
/*  639 */     Object result = null;
/*      */     try
/*      */     {
/*  642 */       this.lastUsed = System.currentTimeMillis();
/*  643 */       result = method.invoke(this.thisAsConnection, args);
/*      */       
/*  645 */       if (result != null) {
/*  646 */         if ((result instanceof Statement)) {
/*  647 */           ((Statement)result).setPingTarget(this);
/*      */         }
/*      */         
/*  650 */         result = proxyIfInterfaceIsJdbc(result, result.getClass());
/*      */       }
/*      */     } catch (InvocationTargetException e) {
/*  653 */       dealWithInvocationException(e);
/*      */     } finally {
/*  655 */       if ((swapAtTransactionBoundary) && (("commit".equals(methodName)) || ("rollback".equals(methodName)))) {
/*  656 */         this.inTransaction = false;
/*      */         
/*      */ 
/*  659 */         String host = (String)this.connectionsToHostsMap.get(this.currentConn);
/*      */         
/*      */ 
/*      */ 
/*  663 */         if (host != null) {
/*  664 */           synchronized (this.responseTimes) {
/*  665 */             Integer hostIndex = (Integer)this.hostsToListIndexMap.get(host);
/*      */             
/*      */ 
/*  668 */             if ((hostIndex != null) && (hostIndex.intValue() < this.responseTimes.length)) {
/*  669 */               this.responseTimes[hostIndex.intValue()] = (getLocalTimeBestResolution() - this.transactionStartTime);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  674 */         pickNewConnection();
/*      */       }
/*      */     }
/*      */     
/*  678 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void pickNewConnection()
/*      */     throws SQLException
/*      */   {
/*  688 */     if ((this.isClosed) && (this.closedExplicitly)) {
/*  689 */       return;
/*      */     }
/*      */     
/*  692 */     if (this.currentConn == null) {
/*  693 */       this.currentConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
/*      */       
/*      */ 
/*      */ 
/*  697 */       return;
/*      */     }
/*      */     
/*  700 */     if (this.currentConn.isClosed()) {
/*  701 */       invalidateCurrentConnection();
/*      */     }
/*      */     
/*  704 */     int pingTimeout = this.currentConn.getLoadBalancePingTimeout();
/*  705 */     boolean pingBeforeReturn = this.currentConn.getLoadBalanceValidateConnectionOnSwapServer();
/*      */     
/*  707 */     int hostsTried = 0; for (int hostsToTry = this.hostList.size(); hostsTried <= hostsToTry; hostsTried++) {
/*  708 */       ConnectionImpl newConn = null;
/*      */       try {
/*  710 */         newConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  716 */         if (this.currentConn != null) {
/*  717 */           if (pingBeforeReturn) {
/*  718 */             if (pingTimeout == 0) {
/*  719 */               newConn.ping();
/*      */             } else {
/*  721 */               newConn.pingInternal(true, pingTimeout);
/*      */             }
/*      */           }
/*      */           
/*  725 */           syncSessionState(this.currentConn, newConn);
/*      */         }
/*      */         
/*  728 */         this.currentConn = newConn;
/*  729 */         return;
/*      */       }
/*      */       catch (SQLException e) {
/*  732 */         if ((shouldExceptionTriggerFailover(e)) && (newConn != null))
/*      */         {
/*      */ 
/*  735 */           invalidateConnection(newConn);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  741 */     this.isClosed = true;
/*  742 */     this.closedReason = "Connection closed after inability to pick valid new connection during fail-over.";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Object proxyIfInterfaceIsJdbc(Object toProxy, Class<?> clazz)
/*      */   {
/*  757 */     if (isInterfaceJdbc(clazz))
/*      */     {
/*  759 */       Class<?>[] interfacesToProxy = getAllInterfacesToProxy(clazz);
/*      */       
/*  761 */       return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfacesToProxy, createConnectionProxy(toProxy));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  766 */     return toProxy;
/*      */   }
/*      */   
/*  769 */   private Map<Class<?>, Class<?>[]> allInterfacesToProxy = new HashMap();
/*      */   
/*      */   private Class<?>[] getAllInterfacesToProxy(Class<?> clazz) {
/*  772 */     Class<?>[] interfacesToProxy = (Class[])this.allInterfacesToProxy.get(clazz);
/*      */     
/*  774 */     if (interfacesToProxy != null) {
/*  775 */       return interfacesToProxy;
/*      */     }
/*      */     
/*  778 */     List<Class<?>> interfaces = new LinkedList();
/*      */     
/*  780 */     Class<?> superClass = clazz;
/*      */     
/*  782 */     while (!superClass.equals(Object.class)) {
/*  783 */       Class<?>[] declared = superClass.getInterfaces();
/*      */       
/*  785 */       for (int i = 0; i < declared.length; i++) {
/*  786 */         interfaces.add(declared[i]);
/*      */       }
/*      */       
/*  789 */       superClass = superClass.getSuperclass();
/*      */     }
/*      */     
/*  792 */     interfacesToProxy = new Class[interfaces.size()];
/*  793 */     interfaces.toArray(interfacesToProxy);
/*      */     
/*  795 */     this.allInterfacesToProxy.put(clazz, interfacesToProxy);
/*      */     
/*  797 */     return interfacesToProxy;
/*      */   }
/*      */   
/*      */   private boolean isInterfaceJdbc(Class<?> clazz)
/*      */   {
/*  802 */     if (this.jdbcInterfacesForProxyCache.containsKey(clazz)) {
/*  803 */       return ((Boolean)this.jdbcInterfacesForProxyCache.get(clazz)).booleanValue();
/*      */     }
/*      */     
/*  806 */     Class<?>[] interfaces = clazz.getInterfaces();
/*      */     
/*  808 */     for (int i = 0; i < interfaces.length; i++) {
/*  809 */       String packageName = interfaces[i].getPackage().getName();
/*      */       
/*  811 */       if (("java.sql".equals(packageName)) || ("javax.sql".equals(packageName)) || ("com.mysql.jdbc".equals(packageName)))
/*      */       {
/*      */ 
/*  814 */         this.jdbcInterfacesForProxyCache.put(clazz, Boolean.valueOf(true));
/*      */         
/*  816 */         return true;
/*      */       }
/*      */       
/*  819 */       if (isInterfaceJdbc(interfaces[i])) {
/*  820 */         this.jdbcInterfacesForProxyCache.put(clazz, Boolean.valueOf(true));
/*      */         
/*  822 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  826 */     this.jdbcInterfacesForProxyCache.put(clazz, Boolean.valueOf(false));
/*  827 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected ConnectionErrorFiringInvocationHandler createConnectionProxy(Object toProxy)
/*      */   {
/*  833 */     return new ConnectionErrorFiringInvocationHandler(toProxy);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long getLocalTimeBestResolution()
/*      */   {
/*  841 */     if (getLocalTimeMethod != null) {
/*      */       try {
/*  843 */         return ((Long)getLocalTimeMethod.invoke(null, (Object[])null)).longValue();
/*      */       }
/*      */       catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  854 */     return System.currentTimeMillis();
/*      */   }
/*      */   
/*      */   public synchronized void doPing() throws SQLException {
/*  858 */     SQLException se = null;
/*  859 */     boolean foundHost = false;
/*  860 */     int pingTimeout = this.currentConn.getLoadBalancePingTimeout();
/*  861 */     Iterator<String> i; synchronized (this) {
/*  862 */       for (i = this.hostList.iterator(); i.hasNext();) {
/*  863 */         String host = (String)i.next();
/*  864 */         ConnectionImpl conn = (ConnectionImpl)this.liveConnections.get(host);
/*  865 */         if (conn != null)
/*      */         {
/*      */           try
/*      */           {
/*  869 */             if (pingTimeout == 0) {
/*  870 */               conn.ping();
/*      */             } else {
/*  872 */               conn.pingInternal(true, pingTimeout);
/*      */             }
/*  874 */             foundHost = true;
/*      */           } catch (SQLException e) {
/*  876 */             this.activePhysicalConnections -= 1L;
/*      */             
/*      */ 
/*  879 */             if (host.equals(this.connectionsToHostsMap.get(this.currentConn)))
/*      */             {
/*      */ 
/*      */ 
/*  883 */               closeAllConnections();
/*  884 */               this.isClosed = true;
/*  885 */               this.closedReason = "Connection closed because ping of current connection failed.";
/*  886 */               throw e;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  891 */             if (e.getMessage().equals(Messages.getString("Connection.exceededConnectionLifetime")))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  897 */               if (se == null) {
/*  898 */                 se = e;
/*      */               }
/*      */             }
/*      */             else {
/*  902 */               se = e;
/*  903 */               if (isGlobalBlacklistEnabled()) {
/*  904 */                 addToGlobalBlacklist(host);
/*      */               }
/*      */             }
/*      */             
/*  908 */             this.liveConnections.remove(this.connectionsToHostsMap.get(conn));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  914 */     if (!foundHost) {
/*  915 */       closeAllConnections();
/*  916 */       this.isClosed = true;
/*  917 */       this.closedReason = "Connection closed due to inability to ping any active connections.";
/*      */       
/*  919 */       if (se != null) {
/*  920 */         throw se;
/*      */       }
/*      */       
/*      */ 
/*  924 */       ((ConnectionImpl)this.currentConn).throwConnectionClosedException();
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToGlobalBlacklist(String host, long timeout)
/*      */   {
/*  930 */     if (isGlobalBlacklistEnabled()) {
/*  931 */       synchronized (globalBlacklist) {
/*  932 */         globalBlacklist.put(host, Long.valueOf(timeout));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void addToGlobalBlacklist(String host) {
/*  938 */     addToGlobalBlacklist(host, System.currentTimeMillis() + this.globalBlacklistTimeout);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isGlobalBlacklistEnabled()
/*      */   {
/*  944 */     return this.globalBlacklistTimeout > 0;
/*      */   }
/*      */   
/*      */   public synchronized Map<String, Long> getGlobalBlacklist() {
/*  948 */     if (!isGlobalBlacklistEnabled()) {
/*  949 */       String localHostToRemove = this.hostToRemove;
/*      */       
/*  951 */       if (this.hostToRemove != null) {
/*  952 */         HashMap<String, Long> fakedBlacklist = new HashMap();
/*  953 */         fakedBlacklist.put(localHostToRemove, Long.valueOf(System.currentTimeMillis() + 5000L));
/*  954 */         return fakedBlacklist;
/*      */       }
/*      */       
/*  957 */       return new HashMap(1);
/*      */     }
/*      */     
/*      */ 
/*  961 */     Map<String, Long> blacklistClone = new HashMap(globalBlacklist.size());
/*      */     
/*      */ 
/*  964 */     synchronized (globalBlacklist) {
/*  965 */       blacklistClone.putAll(globalBlacklist);
/*      */     }
/*  967 */     Set<String> keys = blacklistClone.keySet();
/*      */     
/*      */ 
/*  970 */     keys.retainAll(this.hostList);
/*      */     
/*      */ 
/*  973 */     for (Object i = keys.iterator(); ((Iterator)i).hasNext();) {
/*  974 */       String host = (String)((Iterator)i).next();
/*      */       
/*      */ 
/*  977 */       Long timeout = (Long)globalBlacklist.get(host);
/*  978 */       if ((timeout != null) && (timeout.longValue() < System.currentTimeMillis()))
/*      */       {
/*      */ 
/*  981 */         synchronized (globalBlacklist) {
/*  982 */           globalBlacklist.remove(host);
/*      */         }
/*  984 */         ((Iterator)i).remove();
/*      */       }
/*      */     }
/*      */     
/*  988 */     if (keys.size() == this.hostList.size())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  994 */       return new HashMap(1);
/*      */     }
/*      */     
/*  997 */     return blacklistClone;
/*      */   }
/*      */   
/*      */   public boolean shouldExceptionTriggerFailover(SQLException ex) {
/* 1001 */     return this.exceptionChecker.shouldExceptionTriggerFailover(ex);
/*      */   }
/*      */   
/*      */   public void removeHostWhenNotInUse(String host)
/*      */     throws SQLException
/*      */   {
/* 1007 */     int timeBetweenChecks = 1000;
/* 1008 */     long timeBeforeHardFail = 15000L;
/*      */     
/* 1010 */     synchronized (this) {
/* 1011 */       addToGlobalBlacklist(host, timeBeforeHardFail + 1000L);
/*      */       
/* 1013 */       long cur = System.currentTimeMillis();
/*      */       
/* 1015 */       while (System.currentTimeMillis() - timeBeforeHardFail < cur)
/*      */       {
/* 1017 */         this.hostToRemove = host;
/*      */         
/* 1019 */         if (!host.equals(this.currentConn.getHost())) {
/* 1020 */           removeHost(host);
/* 1021 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1027 */       Thread.sleep(timeBetweenChecks);
/*      */     }
/*      */     catch (InterruptedException e) {}
/*      */     
/*      */ 
/* 1032 */     removeHost(host);
/*      */   }
/*      */   
/*      */   public synchronized void removeHost(String host) throws SQLException
/*      */   {
/* 1037 */     if (this.connectionGroup != null) {
/* 1038 */       if ((this.connectionGroup.getInitialHosts().size() == 1) && (this.connectionGroup.getInitialHosts().contains(host)))
/*      */       {
/* 1040 */         throw SQLError.createSQLException("Cannot remove only configured host.", null);
/*      */       }
/*      */       
/*      */ 
/* 1044 */       this.hostToRemove = host;
/*      */       
/* 1046 */       if (host.equals(this.currentConn.getHost())) {
/* 1047 */         closeAllConnections();
/*      */       } else {
/* 1049 */         this.connectionsToHostsMap.remove(this.liveConnections.remove(host));
/*      */         
/* 1051 */         Integer idx = (Integer)this.hostsToListIndexMap.remove(host);
/* 1052 */         long[] newResponseTimes = new long[this.responseTimes.length - 1];
/* 1053 */         int newIdx = 0;
/* 1054 */         for (Iterator<String> i = this.hostList.iterator(); i.hasNext(); newIdx++) {
/* 1055 */           String copyHost = (String)i.next();
/* 1056 */           if ((idx != null) && (idx.intValue() < this.responseTimes.length))
/*      */           {
/* 1058 */             newResponseTimes[newIdx] = this.responseTimes[idx.intValue()];
/*      */             
/* 1060 */             this.hostsToListIndexMap.put(copyHost, Integer.valueOf(newIdx));
/*      */           }
/*      */         }
/*      */         
/* 1064 */         this.responseTimes = newResponseTimes;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized boolean addHost(String host)
/*      */   {
/* 1072 */     if (this.hostsToListIndexMap.containsKey(host)) {
/* 1073 */       return false;
/*      */     }
/*      */     
/* 1076 */     long[] newResponseTimes = new long[this.responseTimes.length + 1];
/*      */     
/* 1078 */     for (int i = 0; i < this.responseTimes.length; i++) {
/* 1079 */       newResponseTimes[i] = this.responseTimes[i];
/*      */     }
/*      */     
/* 1082 */     this.responseTimes = newResponseTimes;
/* 1083 */     this.hostList.add(host);
/* 1084 */     this.hostsToListIndexMap.put(host, Integer.valueOf(this.responseTimes.length - 1));
/*      */     
/*      */ 
/* 1087 */     return true;
/*      */   }
/*      */   
/*      */   public synchronized long getLastUsed() {
/* 1091 */     return this.lastUsed;
/*      */   }
/*      */   
/*      */   public synchronized boolean inTransaction() {
/* 1095 */     return this.inTransaction;
/*      */   }
/*      */   
/*      */   public synchronized long getTransactionCount() {
/* 1099 */     return this.transactionCount;
/*      */   }
/*      */   
/*      */   public synchronized long getActivePhysicalConnectionCount() {
/* 1103 */     return this.activePhysicalConnections;
/*      */   }
/*      */   
/*      */   public synchronized long getTotalPhysicalConnectionCount() {
/* 1107 */     return this.totalPhysicalConnections;
/*      */   }
/*      */   
/*      */   public synchronized long getConnectionGroupProxyID() {
/* 1111 */     return this.connectionGroupProxyID;
/*      */   }
/*      */   
/*      */   public synchronized String getCurrentActiveHost() {
/* 1115 */     MySQLConnection c = this.currentConn;
/* 1116 */     if (c != null) {
/* 1117 */       Object o = this.connectionsToHostsMap.get(c);
/* 1118 */       if (o != null) {
/* 1119 */         return o.toString();
/*      */       }
/*      */     }
/* 1122 */     return null;
/*      */   }
/*      */   
/*      */   public synchronized long getCurrentTransactionDuration()
/*      */   {
/* 1127 */     if ((this.inTransaction) && (this.transactionStartTime > 0L)) {
/* 1128 */       return getLocalTimeBestResolution() - this.transactionStartTime;
/*      */     }
/*      */     
/* 1131 */     return 0L;
/*      */   }
/*      */   
/*      */   protected void syncSessionState(Connection initial, Connection target) throws SQLException
/*      */   {
/* 1136 */     if ((initial == null) || (target == null)) {
/* 1137 */       return;
/*      */     }
/* 1139 */     target.setAutoCommit(initial.getAutoCommit());
/* 1140 */     target.setCatalog(initial.getCatalog());
/* 1141 */     target.setTransactionIsolation(initial.getTransactionIsolation());
/* 1142 */     target.setReadOnly(initial.isReadOnly());
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\LoadBalancingConnectionProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */