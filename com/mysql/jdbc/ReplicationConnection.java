/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Savepoint;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TimeZone;
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
/*      */ public class ReplicationConnection
/*      */   implements Connection, PingTarget
/*      */ {
/*      */   protected Connection currentConnection;
/*      */   protected LoadBalancedConnection masterConnection;
/*      */   protected LoadBalancedConnection slavesConnection;
/*      */   private Properties slaveProperties;
/*      */   private Properties masterProperties;
/*      */   private NonRegisteringDriver driver;
/*   61 */   private long connectionGroupID = -1L;
/*      */   
/*      */   private ReplicationConnectionGroup connectionGroup;
/*      */   
/*      */   private List<String> slaveHosts;
/*      */   
/*      */   private List<String> masterHosts;
/*      */   
/*   69 */   private boolean allowMasterDownConnections = false;
/*      */   
/*   71 */   private boolean enableJMX = false;
/*      */   
/*      */   protected ReplicationConnection() {}
/*      */   
/*      */   public ReplicationConnection(Properties masterProperties, Properties slaveProperties, List<String> masterHostList, List<String> slaveHostList) throws SQLException
/*      */   {
/*   77 */     String enableJMXAsString = masterProperties.getProperty("replicationEnableJMX", "false");
/*      */     try
/*      */     {
/*   80 */       this.enableJMX = Boolean.parseBoolean(enableJMXAsString);
/*      */     } catch (Exception e) {
/*   82 */       throw SQLError.createSQLException(Messages.getString("ReplicationConnection.badValueForReplicationEnableJMX", new Object[] { enableJMXAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   88 */     String allowMasterDownConnectionsAsString = masterProperties.getProperty("allowMasterDownConnections", "false");
/*      */     try
/*      */     {
/*   91 */       this.allowMasterDownConnections = Boolean.parseBoolean(allowMasterDownConnectionsAsString);
/*      */     } catch (Exception e) {
/*   93 */       throw SQLError.createSQLException(Messages.getString("ReplicationConnection.badValueForAllowMasterDownConnections", new Object[] { enableJMXAsString }), "S1009", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  100 */     String group = masterProperties.getProperty("replicationConnectionGroup", null);
/*      */     
/*      */ 
/*  103 */     if (group != null) {
/*  104 */       this.connectionGroup = ReplicationConnectionGroupManager.getConnectionGroupInstance(group);
/*  105 */       if (this.enableJMX) {
/*  106 */         ReplicationConnectionGroupManager.registerJmx();
/*      */       }
/*  108 */       this.connectionGroupID = this.connectionGroup.registerReplicationConnection(this, masterHostList, slaveHostList);
/*      */       
/*  110 */       slaveHostList = new ArrayList(this.connectionGroup.getSlaveHosts());
/*  111 */       masterHostList = new ArrayList(this.connectionGroup.getMasterHosts());
/*      */     }
/*      */     
/*  114 */     this.driver = new NonRegisteringDriver();
/*  115 */     this.slaveProperties = slaveProperties;
/*  116 */     this.masterProperties = masterProperties;
/*  117 */     this.slaveHosts = slaveHostList;
/*  118 */     this.masterHosts = masterHostList;
/*      */     
/*  120 */     boolean createdMaster = initializeMasterConnection();
/*  121 */     initializeSlaveConnection();
/*  122 */     if (!createdMaster) {
/*  123 */       this.currentConnection = this.slavesConnection;
/*  124 */       return;
/*      */     }
/*      */     
/*  127 */     this.currentConnection = this.masterConnection;
/*      */   }
/*      */   
/*      */   private boolean initializeMasterConnection() throws SQLException {
/*  131 */     return initializeMasterConnection(this.allowMasterDownConnections);
/*      */   }
/*      */   
/*      */   public long getConnectionGroupId() {
/*  135 */     return this.connectionGroupID;
/*      */   }
/*      */   
/*      */   private boolean initializeMasterConnection(boolean allowMasterDown) throws SQLException
/*      */   {
/*  140 */     boolean isMaster = isMasterConnection();
/*      */     
/*  142 */     StringBuffer masterUrl = new StringBuffer("jdbc:mysql:loadbalance://");
/*      */     
/*      */ 
/*  145 */     boolean firstHost = true;
/*  146 */     for (String host : this.masterHosts) {
/*  147 */       if (!firstHost) {
/*  148 */         masterUrl.append(',');
/*      */       }
/*  150 */       masterUrl.append(host);
/*  151 */       firstHost = false;
/*      */     }
/*      */     
/*  154 */     String masterDb = this.masterProperties.getProperty("DBNAME");
/*      */     
/*      */ 
/*      */ 
/*  158 */     masterUrl.append("/");
/*      */     
/*  160 */     if (masterDb != null) {
/*  161 */       masterUrl.append(masterDb);
/*      */     }
/*      */     
/*  164 */     LoadBalancedConnection newMasterConn = null;
/*      */     try {
/*  166 */       newMasterConn = (LoadBalancedConnection)this.driver.connect(masterUrl.toString(), this.masterProperties);
/*      */     }
/*      */     catch (SQLException ex) {
/*  169 */       if (allowMasterDown) {
/*  170 */         this.currentConnection = this.slavesConnection;
/*  171 */         this.masterConnection = null;
/*  172 */         setReadOnly(true);
/*  173 */         return false;
/*      */       }
/*  175 */       throw ex;
/*      */     }
/*      */     
/*      */ 
/*  179 */     if ((isMaster) && (this.currentConnection != null)) {
/*  180 */       swapConnections(newMasterConn, this.currentConnection, true);
/*      */     }
/*      */     
/*  183 */     if (this.masterConnection != null) {
/*      */       try {
/*  185 */         this.masterConnection.close();
/*  186 */         this.masterConnection = null;
/*      */       }
/*      */       catch (SQLException e) {}
/*      */     }
/*      */     
/*  191 */     this.masterConnection = newMasterConn;
/*  192 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private void initializeSlaveConnection()
/*      */     throws SQLException
/*      */   {
/*  199 */     StringBuffer slaveUrl = new StringBuffer("jdbc:mysql:loadbalance://");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  215 */     boolean firstHost = true;
/*  216 */     for (String host : this.slaveHosts) {
/*  217 */       if (!firstHost) {
/*  218 */         slaveUrl.append(',');
/*      */       }
/*  220 */       slaveUrl.append(host);
/*  221 */       firstHost = false;
/*      */     }
/*      */     
/*      */ 
/*  225 */     String slaveDb = this.slaveProperties.getProperty("DBNAME");
/*      */     
/*      */ 
/*  228 */     slaveUrl.append("/");
/*      */     
/*  230 */     if (slaveDb != null) {
/*  231 */       slaveUrl.append(slaveDb);
/*      */     }
/*      */     
/*      */ 
/*  235 */     this.slavesConnection = ((LoadBalancedConnection)this.driver.connect(slaveUrl.toString(), this.slaveProperties));
/*      */     
/*  237 */     this.slavesConnection.setReadOnly(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/*  247 */     getCurrentConnection().clearWarnings();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/*  256 */     if (this.masterConnection != null) {
/*  257 */       this.masterConnection.close();
/*      */     }
/*  259 */     if (this.slavesConnection != null) {
/*  260 */       this.slavesConnection.close();
/*      */     }
/*      */     
/*  263 */     if (this.connectionGroup != null) {
/*  264 */       this.connectionGroup.handleCloseConnection(this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/*  275 */     getCurrentConnection().commit();
/*      */   }
/*      */   
/*      */   public boolean isHostMaster(String host) {
/*  279 */     if (host == null) {
/*  280 */       return false;
/*      */     }
/*  282 */     for (String test : this.masterHosts) {
/*  283 */       if (test.equalsIgnoreCase(host)) {
/*  284 */         return true;
/*      */       }
/*      */     }
/*  287 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isHostSlave(String host)
/*      */   {
/*  292 */     if (host == null) {
/*  293 */       return false;
/*      */     }
/*  295 */     for (String test : this.slaveHosts) {
/*  296 */       if (test.equalsIgnoreCase(host)) {
/*  297 */         return true;
/*      */       }
/*      */     }
/*  300 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void removeSlave(String host)
/*      */     throws SQLException
/*      */   {
/*  308 */     removeSlave(host, true);
/*      */   }
/*      */   
/*      */   public synchronized void removeSlave(String host, boolean closeGently) throws SQLException
/*      */   {
/*  313 */     this.slaveHosts.remove(host);
/*  314 */     if (this.slavesConnection == null) {
/*  315 */       return;
/*      */     }
/*      */     
/*  318 */     if (closeGently) {
/*  319 */       this.slavesConnection.removeHostWhenNotInUse(host);
/*      */     } else {
/*  321 */       this.slavesConnection.removeHost(host);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void addSlaveHost(String host) throws SQLException {
/*  326 */     if (isHostSlave(host))
/*      */     {
/*  328 */       return;
/*      */     }
/*  330 */     this.slaveHosts.add(host);
/*  331 */     this.slavesConnection.addHost(host);
/*      */   }
/*      */   
/*      */   public synchronized void promoteSlaveToMaster(String host)
/*      */     throws SQLException
/*      */   {
/*  337 */     if (!isHostSlave(host)) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  344 */     this.masterHosts.add(host);
/*  345 */     removeSlave(host);
/*  346 */     if (this.masterConnection != null) {
/*  347 */       this.masterConnection.addHost(host);
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void removeMasterHost(String host) throws SQLException
/*      */   {
/*  353 */     removeMasterHost(host, true);
/*      */   }
/*      */   
/*      */   public synchronized void removeMasterHost(String host, boolean waitUntilNotInUse) throws SQLException {
/*  357 */     removeMasterHost(host, waitUntilNotInUse, false);
/*      */   }
/*      */   
/*      */   public synchronized void removeMasterHost(String host, boolean waitUntilNotInUse, boolean isNowSlave) throws SQLException {
/*  361 */     if (isNowSlave) {
/*  362 */       this.slaveHosts.add(host);
/*      */     }
/*  364 */     this.masterHosts.remove(host);
/*      */     
/*  366 */     if (this.masterConnection == null) {
/*  367 */       return;
/*      */     }
/*      */     
/*  370 */     if (waitUntilNotInUse) {
/*  371 */       this.masterConnection.removeHostWhenNotInUse(host);
/*      */     } else {
/*  373 */       this.masterConnection.removeHost(host);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement()
/*      */     throws SQLException
/*      */   {
/*  384 */     java.sql.Statement stmt = getCurrentConnection().createStatement();
/*  385 */     ((Statement)stmt).setPingTarget(this);
/*      */     
/*  387 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*  397 */     java.sql.Statement stmt = getCurrentConnection().createStatement(resultSetType, resultSetConcurrency);
/*      */     
/*      */ 
/*  400 */     ((Statement)stmt).setPingTarget(this);
/*      */     
/*  402 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*  413 */     java.sql.Statement stmt = getCurrentConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */     
/*      */ 
/*  416 */     ((Statement)stmt).setPingTarget(this);
/*      */     
/*  418 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutoCommit()
/*      */     throws SQLException
/*      */   {
/*  427 */     return getCurrentConnection().getAutoCommit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCatalog()
/*      */     throws SQLException
/*      */   {
/*  436 */     return getCurrentConnection().getCatalog();
/*      */   }
/*      */   
/*      */   public synchronized Connection getCurrentConnection() {
/*  440 */     return this.currentConnection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHoldability()
/*      */     throws SQLException
/*      */   {
/*  449 */     return getCurrentConnection().getHoldability();
/*      */   }
/*      */   
/*      */   public synchronized Connection getMasterConnection() {
/*  453 */     return this.masterConnection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DatabaseMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/*  462 */     return getCurrentConnection().getMetaData();
/*      */   }
/*      */   
/*      */   public synchronized Connection getSlavesConnection() {
/*  466 */     return this.slavesConnection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/*  475 */     return getCurrentConnection().getTransactionIsolation();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, Class<?>> getTypeMap()
/*      */     throws SQLException
/*      */   {
/*  484 */     return getCurrentConnection().getTypeMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/*  493 */     return getCurrentConnection().getWarnings();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/*  502 */     return getCurrentConnection().isClosed();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/*  511 */     return this.currentConnection == this.slavesConnection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String nativeSQL(String sql)
/*      */     throws SQLException
/*      */   {
/*  520 */     return getCurrentConnection().nativeSQL(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CallableStatement prepareCall(String sql)
/*      */     throws SQLException
/*      */   {
/*  529 */     return getCurrentConnection().prepareCall(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*  539 */     return getCurrentConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*  551 */     return getCurrentConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/*  561 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql);
/*      */     
/*  563 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  565 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/*  575 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql, autoGeneratedKeys);
/*      */     
/*  577 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  579 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*  589 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */     
/*      */ 
/*  592 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  594 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*  606 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*      */     
/*      */ 
/*  609 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  611 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
/*      */     throws SQLException
/*      */   {
/*  621 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql, columnIndexes);
/*      */     
/*  623 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  625 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PreparedStatement prepareStatement(String sql, String[] columnNames)
/*      */     throws SQLException
/*      */   {
/*  636 */     PreparedStatement pstmt = getCurrentConnection().prepareStatement(sql, columnNames);
/*      */     
/*  638 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  640 */     return pstmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void releaseSavepoint(Savepoint savepoint)
/*      */     throws SQLException
/*      */   {
/*  650 */     getCurrentConnection().releaseSavepoint(savepoint);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/*  659 */     getCurrentConnection().rollback();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rollback(Savepoint savepoint)
/*      */     throws SQLException
/*      */   {
/*  668 */     getCurrentConnection().rollback(savepoint);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoCommit(boolean autoCommit)
/*      */     throws SQLException
/*      */   {
/*  678 */     getCurrentConnection().setAutoCommit(autoCommit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCatalog(String catalog)
/*      */     throws SQLException
/*      */   {
/*  687 */     getCurrentConnection().setCatalog(catalog);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHoldability(int holdability)
/*      */     throws SQLException
/*      */   {
/*  697 */     getCurrentConnection().setHoldability(holdability);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void setReadOnly(boolean readOnly)
/*      */     throws SQLException
/*      */   {
/*  706 */     if (readOnly) {
/*  707 */       if (this.currentConnection != this.slavesConnection) {
/*  708 */         switchToSlavesConnection();
/*      */       }
/*      */     }
/*  711 */     else if (this.currentConnection != this.masterConnection) {
/*  712 */       switchToMasterConnection();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Savepoint setSavepoint()
/*      */     throws SQLException
/*      */   {
/*  723 */     return getCurrentConnection().setSavepoint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Savepoint setSavepoint(String name)
/*      */     throws SQLException
/*      */   {
/*  732 */     return getCurrentConnection().setSavepoint(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTransactionIsolation(int level)
/*      */     throws SQLException
/*      */   {
/*  742 */     getCurrentConnection().setTransactionIsolation(level);
/*      */   }
/*      */   
/*      */   private synchronized void switchToMasterConnection()
/*      */     throws SQLException
/*      */   {
/*  748 */     if ((this.masterConnection == null) || (this.masterConnection.isClosed())) {
/*  749 */       initializeMasterConnection();
/*      */     }
/*  751 */     swapConnections(this.masterConnection, this.slavesConnection);
/*  752 */     this.masterConnection.setReadOnly(false);
/*      */   }
/*      */   
/*      */   private synchronized void switchToSlavesConnection() throws SQLException {
/*  756 */     if ((this.slavesConnection == null) || (this.slavesConnection.isClosed())) {
/*  757 */       initializeSlaveConnection();
/*      */     }
/*  759 */     swapConnections(this.slavesConnection, this.masterConnection);
/*  760 */     this.slavesConnection.setReadOnly(true);
/*      */   }
/*      */   
/*      */   private synchronized void swapConnections(Connection switchToConnection, Connection switchFromConnection) throws SQLException
/*      */   {
/*  765 */     swapConnections(switchToConnection, switchFromConnection, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized void swapConnections(Connection switchToConnection, Connection switchFromConnection, boolean skipReconfigure)
/*      */     throws SQLException
/*      */   {
/*  782 */     String switchFromCatalog = switchFromConnection.getCatalog();
/*  783 */     String switchToCatalog = switchToConnection.getCatalog();
/*      */     
/*  785 */     if ((switchToCatalog != null) && (!switchToCatalog.equals(switchFromCatalog))) {
/*  786 */       switchToConnection.setCatalog(switchFromCatalog);
/*  787 */     } else if (switchFromCatalog != null) {
/*  788 */       switchToConnection.setCatalog(switchFromCatalog);
/*      */     }
/*      */     
/*  791 */     boolean switchToAutoCommit = switchToConnection.getAutoCommit();
/*  792 */     boolean switchFromConnectionAutoCommit = switchFromConnection.getAutoCommit();
/*      */     
/*  794 */     if (switchFromConnectionAutoCommit != switchToAutoCommit) {
/*  795 */       switchToConnection.setAutoCommit(switchFromConnectionAutoCommit);
/*      */     }
/*      */     
/*  798 */     int switchToIsolation = switchToConnection.getTransactionIsolation();
/*      */     
/*      */ 
/*  801 */     int switchFromIsolation = switchFromConnection.getTransactionIsolation();
/*      */     
/*  803 */     if (switchFromIsolation != switchToIsolation) {
/*  804 */       switchToConnection.setTransactionIsolation(switchFromIsolation);
/*      */     }
/*      */     
/*      */ 
/*  808 */     this.currentConnection = switchToConnection;
/*      */   }
/*      */   
/*      */   public synchronized void doPing() throws SQLException {
/*  812 */     boolean isMasterConn = isMasterConnection();
/*  813 */     if (this.masterConnection != null) {
/*      */       try {
/*  815 */         this.masterConnection.ping();
/*      */       } catch (SQLException e) {
/*  817 */         if (isMasterConn)
/*      */         {
/*  819 */           this.currentConnection = this.slavesConnection;
/*  820 */           this.masterConnection = null;
/*      */           
/*  822 */           throw e;
/*      */         }
/*      */       }
/*      */     } else {
/*  826 */       initializeMasterConnection();
/*      */     }
/*      */     
/*  829 */     if (this.slavesConnection != null) {
/*      */       try {
/*  831 */         this.slavesConnection.ping();
/*      */       } catch (SQLException e) {
/*  833 */         if (!isMasterConn)
/*      */         {
/*  835 */           this.currentConnection = this.masterConnection;
/*  836 */           this.slavesConnection = null;
/*      */           
/*  838 */           throw e;
/*      */         }
/*      */       }
/*      */     } else {
/*  842 */       initializeSlaveConnection();
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized void changeUser(String userName, String newPassword) throws SQLException
/*      */   {
/*  848 */     this.masterConnection.changeUser(userName, newPassword);
/*  849 */     this.slavesConnection.changeUser(userName, newPassword);
/*      */   }
/*      */   
/*      */   public synchronized void clearHasTriedMaster() {
/*  853 */     this.masterConnection.clearHasTriedMaster();
/*  854 */     this.slavesConnection.clearHasTriedMaster();
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/*  860 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql);
/*  861 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  863 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException
/*      */   {
/*  868 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql, autoGenKeyIndex);
/*  869 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  871 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
/*      */   {
/*  876 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
/*  877 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  879 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException
/*      */   {
/*  884 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
/*  885 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  887 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/*  893 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*  894 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  896 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException
/*      */   {
/*  901 */     PreparedStatement pstmt = getCurrentConnection().clientPrepareStatement(sql, autoGenKeyColNames);
/*  902 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  904 */     return pstmt;
/*      */   }
/*      */   
/*      */   public int getActiveStatementCount() {
/*  908 */     return getCurrentConnection().getActiveStatementCount();
/*      */   }
/*      */   
/*      */   public long getIdleFor() {
/*  912 */     return getCurrentConnection().getIdleFor();
/*      */   }
/*      */   
/*      */   public Log getLog() throws SQLException {
/*  916 */     return getCurrentConnection().getLog();
/*      */   }
/*      */   
/*      */   public String getServerCharacterEncoding() {
/*  920 */     return getCurrentConnection().getServerCharacterEncoding();
/*      */   }
/*      */   
/*      */   public TimeZone getServerTimezoneTZ() {
/*  924 */     return getCurrentConnection().getServerTimezoneTZ();
/*      */   }
/*      */   
/*      */   public String getStatementComment() {
/*  928 */     return getCurrentConnection().getStatementComment();
/*      */   }
/*      */   
/*      */   public boolean hasTriedMaster() {
/*  932 */     return getCurrentConnection().hasTriedMaster();
/*      */   }
/*      */   
/*      */   public void initializeExtension(Extension ex) throws SQLException {
/*  936 */     getCurrentConnection().initializeExtension(ex);
/*      */   }
/*      */   
/*      */   public boolean isAbonormallyLongQuery(long millisOrNanos) {
/*  940 */     return getCurrentConnection().isAbonormallyLongQuery(millisOrNanos);
/*      */   }
/*      */   
/*      */   public boolean isInGlobalTx() {
/*  944 */     return getCurrentConnection().isInGlobalTx();
/*      */   }
/*      */   
/*      */   public boolean isMasterConnection() {
/*  948 */     if (this.currentConnection == null) {
/*  949 */       return true;
/*      */     }
/*  951 */     return this.currentConnection == this.masterConnection;
/*      */   }
/*      */   
/*      */   public boolean isNoBackslashEscapesSet() {
/*  955 */     return getCurrentConnection().isNoBackslashEscapesSet();
/*      */   }
/*      */   
/*      */   public boolean lowerCaseTableNames() {
/*  959 */     return getCurrentConnection().lowerCaseTableNames();
/*      */   }
/*      */   
/*      */   public boolean parserKnowsUnicode() {
/*  963 */     return getCurrentConnection().parserKnowsUnicode();
/*      */   }
/*      */   
/*      */   public synchronized void ping() throws SQLException {
/*      */     try {
/*  968 */       this.masterConnection.ping();
/*      */     } catch (SQLException e) {
/*  970 */       if (isMasterConnection()) {
/*  971 */         throw e;
/*      */       }
/*      */     }
/*      */     try {
/*  975 */       this.slavesConnection.ping();
/*      */     } catch (SQLException e) {
/*  977 */       if (!isMasterConnection()) {
/*  978 */         throw e;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void reportQueryTime(long millisOrNanos) {
/*  984 */     getCurrentConnection().reportQueryTime(millisOrNanos);
/*      */   }
/*      */   
/*      */   public void resetServerState() throws SQLException {
/*  988 */     getCurrentConnection().resetServerState();
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql) throws SQLException
/*      */   {
/*  993 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql);
/*  994 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/*  996 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException
/*      */   {
/* 1001 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql, autoGenKeyIndex);
/* 1002 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/* 1004 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
/*      */   {
/* 1009 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
/* 1010 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/* 1012 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 1018 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/* 1019 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/* 1021 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException
/*      */   {
/* 1026 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
/* 1027 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/* 1029 */     return pstmt;
/*      */   }
/*      */   
/*      */   public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException
/*      */   {
/* 1034 */     PreparedStatement pstmt = getCurrentConnection().serverPrepareStatement(sql, autoGenKeyColNames);
/* 1035 */     ((Statement)pstmt).setPingTarget(this);
/*      */     
/* 1037 */     return pstmt;
/*      */   }
/*      */   
/*      */   public void setFailedOver(boolean flag) {
/* 1041 */     getCurrentConnection().setFailedOver(flag);
/*      */   }
/*      */   
/*      */   public void setPreferSlaveDuringFailover(boolean flag) {
/* 1045 */     getCurrentConnection().setPreferSlaveDuringFailover(flag);
/*      */   }
/*      */   
/*      */   public synchronized void setStatementComment(String comment) {
/* 1049 */     this.masterConnection.setStatementComment(comment);
/* 1050 */     this.slavesConnection.setStatementComment(comment);
/*      */   }
/*      */   
/*      */   public void shutdownServer() throws SQLException {
/* 1054 */     getCurrentConnection().shutdownServer();
/*      */   }
/*      */   
/*      */   public boolean supportsIsolationLevel() {
/* 1058 */     return getCurrentConnection().supportsIsolationLevel();
/*      */   }
/*      */   
/*      */   public boolean supportsQuotedIdentifiers() {
/* 1062 */     return getCurrentConnection().supportsQuotedIdentifiers();
/*      */   }
/*      */   
/*      */   public boolean supportsTransactions() {
/* 1066 */     return getCurrentConnection().supportsTransactions();
/*      */   }
/*      */   
/*      */   public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException
/*      */   {
/* 1071 */     return getCurrentConnection().versionMeetsMinimum(major, minor, subminor);
/*      */   }
/*      */   
/*      */   public String exposeAsXml() throws SQLException {
/* 1075 */     return getCurrentConnection().exposeAsXml();
/*      */   }
/*      */   
/*      */   public boolean getAllowLoadLocalInfile() {
/* 1079 */     return getCurrentConnection().getAllowLoadLocalInfile();
/*      */   }
/*      */   
/*      */   public boolean getAllowMultiQueries() {
/* 1083 */     return getCurrentConnection().getAllowMultiQueries();
/*      */   }
/*      */   
/*      */   public boolean getAllowNanAndInf() {
/* 1087 */     return getCurrentConnection().getAllowNanAndInf();
/*      */   }
/*      */   
/*      */   public boolean getAllowUrlInLocalInfile() {
/* 1091 */     return getCurrentConnection().getAllowUrlInLocalInfile();
/*      */   }
/*      */   
/*      */   public boolean getAlwaysSendSetIsolation() {
/* 1095 */     return getCurrentConnection().getAlwaysSendSetIsolation();
/*      */   }
/*      */   
/*      */   public boolean getAutoClosePStmtStreams() {
/* 1099 */     return getCurrentConnection().getAutoClosePStmtStreams();
/*      */   }
/*      */   
/*      */   public boolean getAutoDeserialize() {
/* 1103 */     return getCurrentConnection().getAutoDeserialize();
/*      */   }
/*      */   
/*      */   public boolean getAutoGenerateTestcaseScript() {
/* 1107 */     return getCurrentConnection().getAutoGenerateTestcaseScript();
/*      */   }
/*      */   
/*      */   public boolean getAutoReconnectForPools() {
/* 1111 */     return getCurrentConnection().getAutoReconnectForPools();
/*      */   }
/*      */   
/*      */   public boolean getAutoSlowLog() {
/* 1115 */     return getCurrentConnection().getAutoSlowLog();
/*      */   }
/*      */   
/*      */   public int getBlobSendChunkSize() {
/* 1119 */     return getCurrentConnection().getBlobSendChunkSize();
/*      */   }
/*      */   
/*      */   public boolean getBlobsAreStrings() {
/* 1123 */     return getCurrentConnection().getBlobsAreStrings();
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStatements() {
/* 1127 */     return getCurrentConnection().getCacheCallableStatements();
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStmts() {
/* 1131 */     return getCurrentConnection().getCacheCallableStmts();
/*      */   }
/*      */   
/*      */   public boolean getCachePrepStmts() {
/* 1135 */     return getCurrentConnection().getCachePrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getCachePreparedStatements() {
/* 1139 */     return getCurrentConnection().getCachePreparedStatements();
/*      */   }
/*      */   
/*      */   public boolean getCacheResultSetMetadata() {
/* 1143 */     return getCurrentConnection().getCacheResultSetMetadata();
/*      */   }
/*      */   
/*      */   public boolean getCacheServerConfiguration() {
/* 1147 */     return getCurrentConnection().getCacheServerConfiguration();
/*      */   }
/*      */   
/*      */   public int getCallableStatementCacheSize() {
/* 1151 */     return getCurrentConnection().getCallableStatementCacheSize();
/*      */   }
/*      */   
/*      */   public int getCallableStmtCacheSize() {
/* 1155 */     return getCurrentConnection().getCallableStmtCacheSize();
/*      */   }
/*      */   
/*      */   public boolean getCapitalizeTypeNames() {
/* 1159 */     return getCurrentConnection().getCapitalizeTypeNames();
/*      */   }
/*      */   
/*      */   public String getCharacterSetResults() {
/* 1163 */     return getCurrentConnection().getCharacterSetResults();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStorePassword() {
/* 1167 */     return getCurrentConnection().getClientCertificateKeyStorePassword();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStoreType() {
/* 1171 */     return getCurrentConnection().getClientCertificateKeyStoreType();
/*      */   }
/*      */   
/*      */   public String getClientCertificateKeyStoreUrl() {
/* 1175 */     return getCurrentConnection().getClientCertificateKeyStoreUrl();
/*      */   }
/*      */   
/*      */   public String getClientInfoProvider() {
/* 1179 */     return getCurrentConnection().getClientInfoProvider();
/*      */   }
/*      */   
/*      */   public String getClobCharacterEncoding() {
/* 1183 */     return getCurrentConnection().getClobCharacterEncoding();
/*      */   }
/*      */   
/*      */   public boolean getClobberStreamingResults() {
/* 1187 */     return getCurrentConnection().getClobberStreamingResults();
/*      */   }
/*      */   
/*      */   public int getConnectTimeout() {
/* 1191 */     return getCurrentConnection().getConnectTimeout();
/*      */   }
/*      */   
/*      */   public String getConnectionCollation() {
/* 1195 */     return getCurrentConnection().getConnectionCollation();
/*      */   }
/*      */   
/*      */   public String getConnectionLifecycleInterceptors() {
/* 1199 */     return getCurrentConnection().getConnectionLifecycleInterceptors();
/*      */   }
/*      */   
/*      */   public boolean getContinueBatchOnError() {
/* 1203 */     return getCurrentConnection().getContinueBatchOnError();
/*      */   }
/*      */   
/*      */   public boolean getCreateDatabaseIfNotExist() {
/* 1207 */     return getCurrentConnection().getCreateDatabaseIfNotExist();
/*      */   }
/*      */   
/*      */   public int getDefaultFetchSize() {
/* 1211 */     return getCurrentConnection().getDefaultFetchSize();
/*      */   }
/*      */   
/*      */   public boolean getDontTrackOpenResources() {
/* 1215 */     return getCurrentConnection().getDontTrackOpenResources();
/*      */   }
/*      */   
/*      */   public boolean getDumpMetadataOnColumnNotFound() {
/* 1219 */     return getCurrentConnection().getDumpMetadataOnColumnNotFound();
/*      */   }
/*      */   
/*      */   public boolean getDumpQueriesOnException() {
/* 1223 */     return getCurrentConnection().getDumpQueriesOnException();
/*      */   }
/*      */   
/*      */   public boolean getDynamicCalendars() {
/* 1227 */     return getCurrentConnection().getDynamicCalendars();
/*      */   }
/*      */   
/*      */   public boolean getElideSetAutoCommits() {
/* 1231 */     return getCurrentConnection().getElideSetAutoCommits();
/*      */   }
/*      */   
/*      */   public boolean getEmptyStringsConvertToZero() {
/* 1235 */     return getCurrentConnection().getEmptyStringsConvertToZero();
/*      */   }
/*      */   
/*      */   public boolean getEmulateLocators() {
/* 1239 */     return getCurrentConnection().getEmulateLocators();
/*      */   }
/*      */   
/*      */   public boolean getEmulateUnsupportedPstmts() {
/* 1243 */     return getCurrentConnection().getEmulateUnsupportedPstmts();
/*      */   }
/*      */   
/*      */   public boolean getEnablePacketDebug() {
/* 1247 */     return getCurrentConnection().getEnablePacketDebug();
/*      */   }
/*      */   
/*      */   public boolean getEnableQueryTimeouts() {
/* 1251 */     return getCurrentConnection().getEnableQueryTimeouts();
/*      */   }
/*      */   
/*      */   public String getEncoding() {
/* 1255 */     return getCurrentConnection().getEncoding();
/*      */   }
/*      */   
/*      */   public boolean getExplainSlowQueries() {
/* 1259 */     return getCurrentConnection().getExplainSlowQueries();
/*      */   }
/*      */   
/*      */   public boolean getFailOverReadOnly() {
/* 1263 */     return getCurrentConnection().getFailOverReadOnly();
/*      */   }
/*      */   
/*      */   public boolean getFunctionsNeverReturnBlobs() {
/* 1267 */     return getCurrentConnection().getFunctionsNeverReturnBlobs();
/*      */   }
/*      */   
/*      */   public boolean getGatherPerfMetrics() {
/* 1271 */     return getCurrentConnection().getGatherPerfMetrics();
/*      */   }
/*      */   
/*      */   public boolean getGatherPerformanceMetrics() {
/* 1275 */     return getCurrentConnection().getGatherPerformanceMetrics();
/*      */   }
/*      */   
/*      */   public boolean getGenerateSimpleParameterMetadata() {
/* 1279 */     return getCurrentConnection().getGenerateSimpleParameterMetadata();
/*      */   }
/*      */   
/*      */   public boolean getHoldResultsOpenOverStatementClose() {
/* 1283 */     return getCurrentConnection().getHoldResultsOpenOverStatementClose();
/*      */   }
/*      */   
/*      */   public boolean getIgnoreNonTxTables() {
/* 1287 */     return getCurrentConnection().getIgnoreNonTxTables();
/*      */   }
/*      */   
/*      */   public boolean getIncludeInnodbStatusInDeadlockExceptions() {
/* 1291 */     return getCurrentConnection().getIncludeInnodbStatusInDeadlockExceptions();
/*      */   }
/*      */   
/*      */   public int getInitialTimeout() {
/* 1295 */     return getCurrentConnection().getInitialTimeout();
/*      */   }
/*      */   
/*      */   public boolean getInteractiveClient() {
/* 1299 */     return getCurrentConnection().getInteractiveClient();
/*      */   }
/*      */   
/*      */   public boolean getIsInteractiveClient() {
/* 1303 */     return getCurrentConnection().getIsInteractiveClient();
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncation() {
/* 1307 */     return getCurrentConnection().getJdbcCompliantTruncation();
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncationForReads() {
/* 1311 */     return getCurrentConnection().getJdbcCompliantTruncationForReads();
/*      */   }
/*      */   
/*      */   public String getLargeRowSizeThreshold() {
/* 1315 */     return getCurrentConnection().getLargeRowSizeThreshold();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceStrategy() {
/* 1319 */     return getCurrentConnection().getLoadBalanceStrategy();
/*      */   }
/*      */   
/*      */   public String getLocalSocketAddress() {
/* 1323 */     return getCurrentConnection().getLocalSocketAddress();
/*      */   }
/*      */   
/*      */   public int getLocatorFetchBufferSize() {
/* 1327 */     return getCurrentConnection().getLocatorFetchBufferSize();
/*      */   }
/*      */   
/*      */   public boolean getLogSlowQueries() {
/* 1331 */     return getCurrentConnection().getLogSlowQueries();
/*      */   }
/*      */   
/*      */   public boolean getLogXaCommands() {
/* 1335 */     return getCurrentConnection().getLogXaCommands();
/*      */   }
/*      */   
/*      */   public String getLogger() {
/* 1339 */     return getCurrentConnection().getLogger();
/*      */   }
/*      */   
/*      */   public String getLoggerClassName() {
/* 1343 */     return getCurrentConnection().getLoggerClassName();
/*      */   }
/*      */   
/*      */   public boolean getMaintainTimeStats() {
/* 1347 */     return getCurrentConnection().getMaintainTimeStats();
/*      */   }
/*      */   
/*      */   public int getMaxQuerySizeToLog() {
/* 1351 */     return getCurrentConnection().getMaxQuerySizeToLog();
/*      */   }
/*      */   
/*      */   public int getMaxReconnects() {
/* 1355 */     return getCurrentConnection().getMaxReconnects();
/*      */   }
/*      */   
/*      */   public int getMaxRows() {
/* 1359 */     return getCurrentConnection().getMaxRows();
/*      */   }
/*      */   
/*      */   public int getMetadataCacheSize() {
/* 1363 */     return getCurrentConnection().getMetadataCacheSize();
/*      */   }
/*      */   
/*      */   public int getNetTimeoutForStreamingResults() {
/* 1367 */     return getCurrentConnection().getNetTimeoutForStreamingResults();
/*      */   }
/*      */   
/*      */   public boolean getNoAccessToProcedureBodies() {
/* 1371 */     return getCurrentConnection().getNoAccessToProcedureBodies();
/*      */   }
/*      */   
/*      */   public boolean getNoDatetimeStringSync() {
/* 1375 */     return getCurrentConnection().getNoDatetimeStringSync();
/*      */   }
/*      */   
/*      */   public boolean getNoTimezoneConversionForTimeType() {
/* 1379 */     return getCurrentConnection().getNoTimezoneConversionForTimeType();
/*      */   }
/*      */   
/*      */   public boolean getNullCatalogMeansCurrent() {
/* 1383 */     return getCurrentConnection().getNullCatalogMeansCurrent();
/*      */   }
/*      */   
/*      */   public boolean getNullNamePatternMatchesAll() {
/* 1387 */     return getCurrentConnection().getNullNamePatternMatchesAll();
/*      */   }
/*      */   
/*      */   public boolean getOverrideSupportsIntegrityEnhancementFacility() {
/* 1391 */     return getCurrentConnection().getOverrideSupportsIntegrityEnhancementFacility();
/*      */   }
/*      */   
/*      */   public int getPacketDebugBufferSize() {
/* 1395 */     return getCurrentConnection().getPacketDebugBufferSize();
/*      */   }
/*      */   
/*      */   public boolean getPadCharsWithSpace() {
/* 1399 */     return getCurrentConnection().getPadCharsWithSpace();
/*      */   }
/*      */   
/*      */   public boolean getParanoid() {
/* 1403 */     return getCurrentConnection().getParanoid();
/*      */   }
/*      */   
/*      */   public boolean getPedantic() {
/* 1407 */     return getCurrentConnection().getPedantic();
/*      */   }
/*      */   
/*      */   public boolean getPinGlobalTxToPhysicalConnection() {
/* 1411 */     return getCurrentConnection().getPinGlobalTxToPhysicalConnection();
/*      */   }
/*      */   
/*      */   public boolean getPopulateInsertRowWithDefaultValues() {
/* 1415 */     return getCurrentConnection().getPopulateInsertRowWithDefaultValues();
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSize() {
/* 1419 */     return getCurrentConnection().getPrepStmtCacheSize();
/*      */   }
/*      */   
/*      */   public int getPrepStmtCacheSqlLimit() {
/* 1423 */     return getCurrentConnection().getPrepStmtCacheSqlLimit();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSize() {
/* 1427 */     return getCurrentConnection().getPreparedStatementCacheSize();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSqlLimit() {
/* 1431 */     return getCurrentConnection().getPreparedStatementCacheSqlLimit();
/*      */   }
/*      */   
/*      */   public boolean getProcessEscapeCodesForPrepStmts() {
/* 1435 */     return getCurrentConnection().getProcessEscapeCodesForPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getProfileSQL() {
/* 1439 */     return getCurrentConnection().getProfileSQL();
/*      */   }
/*      */   
/*      */   public boolean getProfileSql() {
/* 1443 */     return getCurrentConnection().getProfileSql();
/*      */   }
/*      */   
/*      */   public String getProfilerEventHandler() {
/* 1447 */     return getCurrentConnection().getProfilerEventHandler();
/*      */   }
/*      */   
/*      */   public String getPropertiesTransform() {
/* 1451 */     return getCurrentConnection().getPropertiesTransform();
/*      */   }
/*      */   
/*      */   public int getQueriesBeforeRetryMaster() {
/* 1455 */     return getCurrentConnection().getQueriesBeforeRetryMaster();
/*      */   }
/*      */   
/*      */   public boolean getReconnectAtTxEnd() {
/* 1459 */     return getCurrentConnection().getReconnectAtTxEnd();
/*      */   }
/*      */   
/*      */   public boolean getRelaxAutoCommit() {
/* 1463 */     return getCurrentConnection().getRelaxAutoCommit();
/*      */   }
/*      */   
/*      */   public int getReportMetricsIntervalMillis() {
/* 1467 */     return getCurrentConnection().getReportMetricsIntervalMillis();
/*      */   }
/*      */   
/*      */   public boolean getRequireSSL() {
/* 1471 */     return getCurrentConnection().getRequireSSL();
/*      */   }
/*      */   
/*      */   public String getResourceId() {
/* 1475 */     return getCurrentConnection().getResourceId();
/*      */   }
/*      */   
/*      */   public int getResultSetSizeThreshold() {
/* 1479 */     return getCurrentConnection().getResultSetSizeThreshold();
/*      */   }
/*      */   
/*      */   public boolean getRewriteBatchedStatements() {
/* 1483 */     return getCurrentConnection().getRewriteBatchedStatements();
/*      */   }
/*      */   
/*      */   public boolean getRollbackOnPooledClose() {
/* 1487 */     return getCurrentConnection().getRollbackOnPooledClose();
/*      */   }
/*      */   
/*      */   public boolean getRoundRobinLoadBalance() {
/* 1491 */     return getCurrentConnection().getRoundRobinLoadBalance();
/*      */   }
/*      */   
/*      */   public boolean getRunningCTS13() {
/* 1495 */     return getCurrentConnection().getRunningCTS13();
/*      */   }
/*      */   
/*      */   public int getSecondsBeforeRetryMaster() {
/* 1499 */     return getCurrentConnection().getSecondsBeforeRetryMaster();
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingMaxOperations() {
/* 1503 */     return getCurrentConnection().getSelfDestructOnPingMaxOperations();
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingSecondsLifetime() {
/* 1507 */     return getCurrentConnection().getSelfDestructOnPingSecondsLifetime();
/*      */   }
/*      */   
/*      */   public String getServerTimezone() {
/* 1511 */     return getCurrentConnection().getServerTimezone();
/*      */   }
/*      */   
/*      */   public String getSessionVariables() {
/* 1515 */     return getCurrentConnection().getSessionVariables();
/*      */   }
/*      */   
/*      */   public int getSlowQueryThresholdMillis() {
/* 1519 */     return getCurrentConnection().getSlowQueryThresholdMillis();
/*      */   }
/*      */   
/*      */   public long getSlowQueryThresholdNanos() {
/* 1523 */     return getCurrentConnection().getSlowQueryThresholdNanos();
/*      */   }
/*      */   
/*      */   public String getSocketFactory() {
/* 1527 */     return getCurrentConnection().getSocketFactory();
/*      */   }
/*      */   
/*      */   public String getSocketFactoryClassName() {
/* 1531 */     return getCurrentConnection().getSocketFactoryClassName();
/*      */   }
/*      */   
/*      */   public int getSocketTimeout() {
/* 1535 */     return getCurrentConnection().getSocketTimeout();
/*      */   }
/*      */   
/*      */   public String getStatementInterceptors() {
/* 1539 */     return getCurrentConnection().getStatementInterceptors();
/*      */   }
/*      */   
/*      */   public boolean getStrictFloatingPoint() {
/* 1543 */     return getCurrentConnection().getStrictFloatingPoint();
/*      */   }
/*      */   
/*      */   public boolean getStrictUpdates() {
/* 1547 */     return getCurrentConnection().getStrictUpdates();
/*      */   }
/*      */   
/*      */   public boolean getTcpKeepAlive() {
/* 1551 */     return getCurrentConnection().getTcpKeepAlive();
/*      */   }
/*      */   
/*      */   public boolean getTcpNoDelay() {
/* 1555 */     return getCurrentConnection().getTcpNoDelay();
/*      */   }
/*      */   
/*      */   public int getTcpRcvBuf() {
/* 1559 */     return getCurrentConnection().getTcpRcvBuf();
/*      */   }
/*      */   
/*      */   public int getTcpSndBuf() {
/* 1563 */     return getCurrentConnection().getTcpSndBuf();
/*      */   }
/*      */   
/*      */   public int getTcpTrafficClass() {
/* 1567 */     return getCurrentConnection().getTcpTrafficClass();
/*      */   }
/*      */   
/*      */   public boolean getTinyInt1isBit() {
/* 1571 */     return getCurrentConnection().getTinyInt1isBit();
/*      */   }
/*      */   
/*      */   public boolean getTraceProtocol() {
/* 1575 */     return getCurrentConnection().getTraceProtocol();
/*      */   }
/*      */   
/*      */   public boolean getTransformedBitIsBoolean() {
/* 1579 */     return getCurrentConnection().getTransformedBitIsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTreatUtilDateAsTimestamp() {
/* 1583 */     return getCurrentConnection().getTreatUtilDateAsTimestamp();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStorePassword() {
/* 1587 */     return getCurrentConnection().getTrustCertificateKeyStorePassword();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStoreType() {
/* 1591 */     return getCurrentConnection().getTrustCertificateKeyStoreType();
/*      */   }
/*      */   
/*      */   public String getTrustCertificateKeyStoreUrl() {
/* 1595 */     return getCurrentConnection().getTrustCertificateKeyStoreUrl();
/*      */   }
/*      */   
/*      */   public boolean getUltraDevHack() {
/* 1599 */     return getCurrentConnection().getUltraDevHack();
/*      */   }
/*      */   
/*      */   public boolean getUseBlobToStoreUTF8OutsideBMP() {
/* 1603 */     return getCurrentConnection().getUseBlobToStoreUTF8OutsideBMP();
/*      */   }
/*      */   
/*      */   public boolean getUseCompression() {
/* 1607 */     return getCurrentConnection().getUseCompression();
/*      */   }
/*      */   
/*      */   public String getUseConfigs() {
/* 1611 */     return getCurrentConnection().getUseConfigs();
/*      */   }
/*      */   
/*      */   public boolean getUseCursorFetch() {
/* 1615 */     return getCurrentConnection().getUseCursorFetch();
/*      */   }
/*      */   
/*      */   public boolean getUseDirectRowUnpack() {
/* 1619 */     return getCurrentConnection().getUseDirectRowUnpack();
/*      */   }
/*      */   
/*      */   public boolean getUseDynamicCharsetInfo() {
/* 1623 */     return getCurrentConnection().getUseDynamicCharsetInfo();
/*      */   }
/*      */   
/*      */   public boolean getUseFastDateParsing() {
/* 1627 */     return getCurrentConnection().getUseFastDateParsing();
/*      */   }
/*      */   
/*      */   public boolean getUseFastIntParsing() {
/* 1631 */     return getCurrentConnection().getUseFastIntParsing();
/*      */   }
/*      */   
/*      */   public boolean getUseGmtMillisForDatetimes() {
/* 1635 */     return getCurrentConnection().getUseGmtMillisForDatetimes();
/*      */   }
/*      */   
/*      */   public boolean getUseHostsInPrivileges() {
/* 1639 */     return getCurrentConnection().getUseHostsInPrivileges();
/*      */   }
/*      */   
/*      */   public boolean getUseInformationSchema() {
/* 1643 */     return getCurrentConnection().getUseInformationSchema();
/*      */   }
/*      */   
/*      */   public boolean getUseJDBCCompliantTimezoneShift() {
/* 1647 */     return getCurrentConnection().getUseJDBCCompliantTimezoneShift();
/*      */   }
/*      */   
/*      */   public boolean getUseJvmCharsetConverters() {
/* 1651 */     return getCurrentConnection().getUseJvmCharsetConverters();
/*      */   }
/*      */   
/*      */   public boolean getUseLegacyDatetimeCode() {
/* 1655 */     return getCurrentConnection().getUseLegacyDatetimeCode();
/*      */   }
/*      */   
/*      */   public boolean getUseLocalSessionState() {
/* 1659 */     return getCurrentConnection().getUseLocalSessionState();
/*      */   }
/*      */   
/*      */   public boolean getUseNanosForElapsedTime() {
/* 1663 */     return getCurrentConnection().getUseNanosForElapsedTime();
/*      */   }
/*      */   
/*      */   public boolean getUseOldAliasMetadataBehavior() {
/* 1667 */     return getCurrentConnection().getUseOldAliasMetadataBehavior();
/*      */   }
/*      */   
/*      */   public boolean getUseOldUTF8Behavior() {
/* 1671 */     return getCurrentConnection().getUseOldUTF8Behavior();
/*      */   }
/*      */   
/*      */   public boolean getUseOnlyServerErrorMessages() {
/* 1675 */     return getCurrentConnection().getUseOnlyServerErrorMessages();
/*      */   }
/*      */   
/*      */   public boolean getUseReadAheadInput() {
/* 1679 */     return getCurrentConnection().getUseReadAheadInput();
/*      */   }
/*      */   
/*      */   public boolean getUseSSL() {
/* 1683 */     return getCurrentConnection().getUseSSL();
/*      */   }
/*      */   
/*      */   public boolean getUseSSPSCompatibleTimezoneShift() {
/* 1687 */     return getCurrentConnection().getUseSSPSCompatibleTimezoneShift();
/*      */   }
/*      */   
/*      */   public boolean getUseServerPrepStmts() {
/* 1691 */     return getCurrentConnection().getUseServerPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseServerPreparedStmts() {
/* 1695 */     return getCurrentConnection().getUseServerPreparedStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseSqlStateCodes() {
/* 1699 */     return getCurrentConnection().getUseSqlStateCodes();
/*      */   }
/*      */   
/*      */   public boolean getUseStreamLengthsInPrepStmts() {
/* 1703 */     return getCurrentConnection().getUseStreamLengthsInPrepStmts();
/*      */   }
/*      */   
/*      */   public boolean getUseTimezone() {
/* 1707 */     return getCurrentConnection().getUseTimezone();
/*      */   }
/*      */   
/*      */   public boolean getUseUltraDevWorkAround() {
/* 1711 */     return getCurrentConnection().getUseUltraDevWorkAround();
/*      */   }
/*      */   
/*      */   public boolean getUseUnbufferedInput() {
/* 1715 */     return getCurrentConnection().getUseUnbufferedInput();
/*      */   }
/*      */   
/*      */   public boolean getUseUnicode() {
/* 1719 */     return getCurrentConnection().getUseUnicode();
/*      */   }
/*      */   
/*      */   public boolean getUseUsageAdvisor() {
/* 1723 */     return getCurrentConnection().getUseUsageAdvisor();
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpExcludedColumnNamePattern() {
/* 1727 */     return getCurrentConnection().getUtf8OutsideBmpExcludedColumnNamePattern();
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpIncludedColumnNamePattern() {
/* 1731 */     return getCurrentConnection().getUtf8OutsideBmpIncludedColumnNamePattern();
/*      */   }
/*      */   
/*      */   public boolean getVerifyServerCertificate() {
/* 1735 */     return getCurrentConnection().getVerifyServerCertificate();
/*      */   }
/*      */   
/*      */   public boolean getYearIsDateType() {
/* 1739 */     return getCurrentConnection().getYearIsDateType();
/*      */   }
/*      */   
/*      */   public String getZeroDateTimeBehavior() {
/* 1743 */     return getCurrentConnection().getZeroDateTimeBehavior();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowLoadLocalInfile(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowMultiQueries(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowNanAndInf(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowUrlInLocalInfile(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAlwaysSendSetIsolation(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoClosePStmtStreams(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoDeserialize(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoGenerateTestcaseScript(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnect(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForConnectionPools(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForPools(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoSlowLog(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBlobSendChunkSize(String value)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBlobsAreStrings(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheCallableStatements(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheCallableStmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCachePrepStmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCachePreparedStatements(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheResultSetMetadata(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheServerConfiguration(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCallableStatementCacheSize(int size) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCallableStmtCacheSize(int cacheSize) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCapitalizeDBMDTypes(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCapitalizeTypeNames(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterEncoding(String encoding) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterSetResults(String characterSet) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStorePassword(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStoreType(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStoreUrl(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientInfoProvider(String classname) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClobCharacterEncoding(String encoding) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClobberStreamingResults(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectTimeout(int timeoutMs) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionCollation(String collation) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionLifecycleInterceptors(String interceptors) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContinueBatchOnError(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCreateDatabaseIfNotExist(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultFetchSize(int n) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectServerPreparedStmts(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDontTrackOpenResources(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDumpMetadataOnColumnNotFound(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDumpQueriesOnException(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDynamicCalendars(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setElideSetAutoCommits(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEmptyStringsConvertToZero(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEmulateLocators(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEmulateUnsupportedPstmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnablePacketDebug(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnableQueryTimeouts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEncoding(String property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExplainSlowQueries(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFailOverReadOnly(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFunctionsNeverReturnBlobs(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGatherPerfMetrics(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGatherPerformanceMetrics(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGenerateSimpleParameterMetadata(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHoldResultsOpenOverStatementClose(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIgnoreNonTxTables(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInitialTimeout(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInteractiveClient(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIsInteractiveClient(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJdbcCompliantTruncation(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLargeRowSizeThreshold(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceStrategy(String strategy) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocalSocketAddress(String address) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocatorFetchBufferSize(String value)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogSlowQueries(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogXaCommands(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogger(String property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLoggerClassName(String className) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaintainTimeStats(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxQuerySizeToLog(int sizeInBytes) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxReconnects(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxRows(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMetadataCacheSize(int value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNetTimeoutForStreamingResults(int value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNoAccessToProcedureBodies(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNoDatetimeStringSync(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNoTimezoneConversionForTimeType(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNullCatalogMeansCurrent(boolean value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNullNamePatternMatchesAll(boolean value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPacketDebugBufferSize(int size) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPadCharsWithSpace(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParanoid(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPedantic(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPinGlobalTxToPhysicalConnection(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPopulateInsertRowWithDefaultValues(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPrepStmtCacheSize(int cacheSize) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPrepStmtCacheSqlLimit(int sqlLimit) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreparedStatementCacheSize(int cacheSize) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProcessEscapeCodesForPrepStmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProfileSQL(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProfileSql(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProfilerEventHandler(String handler) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPropertiesTransform(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setQueriesBeforeRetryMaster(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReconnectAtTxEnd(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRelaxAutoCommit(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReportMetricsIntervalMillis(int millis) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequireSSL(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResourceId(String resourceId) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResultSetSizeThreshold(int threshold) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRetainStatementAfterResultSetClose(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRewriteBatchedStatements(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRollbackOnPooledClose(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoundRobinLoadBalance(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRunningCTS13(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecondsBeforeRetryMaster(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelfDestructOnPingMaxOperations(int maxOperations) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelfDestructOnPingSecondsLifetime(int seconds) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerTimezone(String property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionVariables(String variables) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSlowQueryThresholdMillis(int millis) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSlowQueryThresholdNanos(long nanos) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketFactory(String name) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketFactoryClassName(String property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketTimeout(int property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStatementInterceptors(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrictFloatingPoint(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrictUpdates(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTcpKeepAlive(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTcpNoDelay(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTcpRcvBuf(int bufSize) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTcpSndBuf(int bufSize) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTcpTrafficClass(int classFlags) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTinyInt1isBit(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTraceProtocol(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTransformedBitIsBoolean(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTreatUtilDateAsTimestamp(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStorePassword(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStoreType(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStoreUrl(String value) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUltraDevHack(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseCompression(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseConfigs(String configs) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseCursorFetch(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseDirectRowUnpack(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseDynamicCharsetInfo(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseFastDateParsing(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseFastIntParsing(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseGmtMillisForDatetimes(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseHostsInPrivileges(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseInformationSchema(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseJDBCCompliantTimezoneShift(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseJvmCharsetConverters(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseLegacyDatetimeCode(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseLocalSessionState(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseNanosForElapsedTime(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOldAliasMetadataBehavior(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOldUTF8Behavior(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOnlyServerErrorMessages(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseReadAheadInput(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseSSL(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseSSPSCompatibleTimezoneShift(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseServerPrepStmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseServerPreparedStmts(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseSqlStateCodes(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseStreamLengthsInPrepStmts(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseTimezone(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUltraDevWorkAround(boolean property) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUnbufferedInput(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseUnicode(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setVerifyServerCertificate(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setYearIsDateType(boolean flag) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void setZeroDateTimeBehavior(String behavior) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean useUnbufferedInput()
/*      */   {
/* 2613 */     return getCurrentConnection().useUnbufferedInput();
/*      */   }
/*      */   
/*      */   public boolean isSameResource(Connection c) {
/* 2617 */     return getCurrentConnection().isSameResource(c);
/*      */   }
/*      */   
/*      */   public void setInGlobalTx(boolean flag) {
/* 2621 */     getCurrentConnection().setInGlobalTx(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseColumnNamesInFindColumn() {
/* 2625 */     return getCurrentConnection().getUseColumnNamesInFindColumn();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUseColumnNamesInFindColumn(boolean flag) {}
/*      */   
/*      */   public boolean getUseLocalTransactionState()
/*      */   {
/* 2633 */     return getCurrentConnection().getUseLocalTransactionState();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUseLocalTransactionState(boolean flag) {}
/*      */   
/*      */ 
/*      */   public boolean getCompensateOnDuplicateKeyUpdateCounts()
/*      */   {
/* 2642 */     return getCurrentConnection().getCompensateOnDuplicateKeyUpdateCounts();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {}
/*      */   
/*      */ 
/*      */   public boolean getUseAffectedRows()
/*      */   {
/* 2651 */     return getCurrentConnection().getUseAffectedRows();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUseAffectedRows(boolean flag) {}
/*      */   
/*      */ 
/*      */   public String getPasswordCharacterEncoding()
/*      */   {
/* 2660 */     return getCurrentConnection().getPasswordCharacterEncoding();
/*      */   }
/*      */   
/*      */   public void setPasswordCharacterEncoding(String characterSet) {
/* 2664 */     getCurrentConnection().setPasswordCharacterEncoding(characterSet);
/*      */   }
/*      */   
/*      */   public int getAutoIncrementIncrement() {
/* 2668 */     return getCurrentConnection().getAutoIncrementIncrement();
/*      */   }
/*      */   
/*      */   public int getLoadBalanceBlacklistTimeout() {
/* 2672 */     return getCurrentConnection().getLoadBalanceBlacklistTimeout();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
/* 2676 */     getCurrentConnection().setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
/*      */   }
/*      */   
/*      */   public int getLoadBalancePingTimeout() {
/* 2680 */     return getCurrentConnection().getLoadBalancePingTimeout();
/*      */   }
/*      */   
/*      */   public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException {
/* 2684 */     getCurrentConnection().setLoadBalancePingTimeout(loadBalancePingTimeout);
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceValidateConnectionOnSwapServer() {
/* 2688 */     return getCurrentConnection().getLoadBalanceValidateConnectionOnSwapServer();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
/* 2692 */     getCurrentConnection().setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
/*      */   }
/*      */   
/*      */   public int getRetriesAllDown() {
/* 2696 */     return getCurrentConnection().getRetriesAllDown();
/*      */   }
/*      */   
/*      */   public void setRetriesAllDown(int retriesAllDown) throws SQLException {
/* 2700 */     getCurrentConnection().setRetriesAllDown(retriesAllDown);
/*      */   }
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor() {
/* 2704 */     return getCurrentConnection().getExceptionInterceptor();
/*      */   }
/*      */   
/*      */   public String getExceptionInterceptors() {
/* 2708 */     return getCurrentConnection().getExceptionInterceptors();
/*      */   }
/*      */   
/*      */   public void setExceptionInterceptors(String exceptionInterceptors) {
/* 2712 */     getCurrentConnection().setExceptionInterceptors(exceptionInterceptors);
/*      */   }
/*      */   
/*      */   public boolean getQueryTimeoutKillsConnection() {
/* 2716 */     return getCurrentConnection().getQueryTimeoutKillsConnection();
/*      */   }
/*      */   
/*      */   public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection)
/*      */   {
/* 2721 */     getCurrentConnection().setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
/*      */   }
/*      */   
/*      */   public boolean hasSameProperties(Connection c) {
/* 2725 */     return (this.masterConnection.hasSameProperties(c)) && (this.slavesConnection.hasSameProperties(c));
/*      */   }
/*      */   
/*      */   public Properties getProperties()
/*      */   {
/* 2730 */     Properties props = new Properties();
/* 2731 */     props.putAll(this.masterConnection.getProperties());
/* 2732 */     props.putAll(this.slavesConnection.getProperties());
/*      */     
/* 2734 */     return props;
/*      */   }
/*      */   
/*      */   public String getHost() {
/* 2738 */     return getCurrentConnection().getHost();
/*      */   }
/*      */   
/*      */   public void setProxy(MySQLConnection proxy) {
/* 2742 */     getCurrentConnection().setProxy(proxy);
/*      */   }
/*      */   
/*      */   public boolean getRetainStatementAfterResultSetClose() {
/* 2746 */     return getCurrentConnection().getRetainStatementAfterResultSetClose();
/*      */   }
/*      */   
/*      */   public int getMaxAllowedPacket() {
/* 2750 */     return getCurrentConnection().getMaxAllowedPacket();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceConnectionGroup() {
/* 2754 */     return getCurrentConnection().getLoadBalanceConnectionGroup();
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceEnableJMX() {
/* 2758 */     return getCurrentConnection().getLoadBalanceEnableJMX();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceExceptionChecker() {
/* 2762 */     return this.currentConnection.getLoadBalanceExceptionChecker();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLExceptionSubclassFailover()
/*      */   {
/* 2767 */     return this.currentConnection.getLoadBalanceSQLExceptionSubclassFailover();
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLStateFailover()
/*      */   {
/* 2772 */     return this.currentConnection.getLoadBalanceSQLStateFailover();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup)
/*      */   {
/* 2777 */     this.currentConnection.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX)
/*      */   {
/* 2783 */     this.currentConnection.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker)
/*      */   {
/* 2790 */     this.currentConnection.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover)
/*      */   {
/* 2797 */     this.currentConnection.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover)
/*      */   {
/* 2804 */     this.currentConnection.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getLoadBalanceAutoCommitStatementRegex()
/*      */   {
/* 2810 */     return getCurrentConnection().getLoadBalanceAutoCommitStatementRegex();
/*      */   }
/*      */   
/*      */   public int getLoadBalanceAutoCommitStatementThreshold() {
/* 2814 */     return getCurrentConnection().getLoadBalanceAutoCommitStatementThreshold();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex)
/*      */   {
/* 2819 */     getCurrentConnection().setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
/*      */   }
/*      */   
/*      */   public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold)
/*      */     throws SQLException
/*      */   {
/* 2825 */     getCurrentConnection().setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setTypeMap(Map<String, Class<?>> map)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   public boolean getIncludeThreadDumpInDeadlockExceptions()
/*      */   {
/* 2835 */     return getCurrentConnection().getIncludeThreadDumpInDeadlockExceptions();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
/* 2839 */     getCurrentConnection().setIncludeThreadDumpInDeadlockExceptions(flag);
/*      */   }
/*      */   
/*      */   public boolean getIncludeThreadNamesAsStatementComment()
/*      */   {
/* 2844 */     return getCurrentConnection().getIncludeThreadNamesAsStatementComment();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadNamesAsStatementComment(boolean flag) {
/* 2848 */     getCurrentConnection().setIncludeThreadNamesAsStatementComment(flag);
/*      */   }
/*      */   
/*      */   public boolean isServerLocal() throws SQLException {
/* 2852 */     return getCurrentConnection().isServerLocal();
/*      */   }
/*      */   
/*      */   public void setAuthenticationPlugins(String authenticationPlugins) {
/* 2856 */     getCurrentConnection().setAuthenticationPlugins(authenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getAuthenticationPlugins() {
/* 2860 */     return getCurrentConnection().getAuthenticationPlugins();
/*      */   }
/*      */   
/*      */   public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins)
/*      */   {
/* 2865 */     getCurrentConnection().setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getDisabledAuthenticationPlugins() {
/* 2869 */     return getCurrentConnection().getDisabledAuthenticationPlugins();
/*      */   }
/*      */   
/*      */   public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin)
/*      */   {
/* 2874 */     getCurrentConnection().setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
/*      */   }
/*      */   
/*      */   public String getDefaultAuthenticationPlugin() {
/* 2878 */     return getCurrentConnection().getDefaultAuthenticationPlugin();
/*      */   }
/*      */   
/*      */   public void setParseInfoCacheFactory(String factoryClassname) {
/* 2882 */     getCurrentConnection().setParseInfoCacheFactory(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getParseInfoCacheFactory() {
/* 2886 */     return getCurrentConnection().getParseInfoCacheFactory();
/*      */   }
/*      */   
/*      */   public void setSchema(String schema) throws SQLException {
/* 2890 */     getCurrentConnection().setSchema(schema);
/*      */   }
/*      */   
/*      */   public String getSchema() throws SQLException {
/* 2894 */     return getCurrentConnection().getSchema();
/*      */   }
/*      */   
/*      */   public void abort(Executor executor) throws SQLException {
/* 2898 */     getCurrentConnection().abort(executor);
/* 2899 */     if (this.connectionGroup != null) {
/* 2900 */       this.connectionGroup.handleCloseConnection(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
/*      */   {
/* 2906 */     getCurrentConnection().setNetworkTimeout(executor, milliseconds);
/*      */   }
/*      */   
/*      */   public int getNetworkTimeout() throws SQLException {
/* 2910 */     return getCurrentConnection().getNetworkTimeout();
/*      */   }
/*      */   
/*      */   public void setServerConfigCacheFactory(String factoryClassname) {
/* 2914 */     getCurrentConnection().setServerConfigCacheFactory(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getServerConfigCacheFactory() {
/* 2918 */     return getCurrentConnection().getServerConfigCacheFactory();
/*      */   }
/*      */   
/*      */   public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
/* 2922 */     getCurrentConnection().setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
/*      */   }
/*      */   
/*      */   public boolean getDisconnectOnExpiredPasswords() {
/* 2926 */     return getCurrentConnection().getDisconnectOnExpiredPasswords();
/*      */   }
/*      */   
/*      */   public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions) {
/* 2930 */     getCurrentConnection().setGetProceduresReturnsFunctions(getProcedureReturnsFunctions);
/*      */   }
/*      */   
/*      */   public boolean getGetProceduresReturnsFunctions() {
/* 2934 */     return getCurrentConnection().getGetProceduresReturnsFunctions();
/*      */   }
/*      */   
/*      */   public void abortInternal() throws SQLException {
/* 2938 */     getCurrentConnection().abortInternal();
/* 2939 */     if (this.connectionGroup != null) {
/* 2940 */       this.connectionGroup.handleCloseConnection(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkClosed() throws SQLException {
/* 2945 */     getCurrentConnection().checkClosed();
/*      */   }
/*      */   
/*      */   public Object getConnectionMutex() {
/* 2949 */     return getCurrentConnection().getConnectionMutex();
/*      */   }
/*      */   
/*      */   public boolean getAllowMasterDownConnections() {
/* 2953 */     return this.allowMasterDownConnections;
/*      */   }
/*      */   
/*      */   public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
/* 2957 */     this.allowMasterDownConnections = connectIfMasterDown;
/*      */   }
/*      */   
/*      */   public boolean getReplicationEnableJMX() {
/* 2961 */     return this.enableJMX;
/*      */   }
/*      */   
/*      */   public void setReplicationEnableJMX(boolean replicationEnableJMX) {
/* 2965 */     this.enableJMX = replicationEnableJMX;
/*      */   }
/*      */   
/*      */   public String getConnectionAttributes()
/*      */     throws SQLException
/*      */   {
/* 2971 */     return getCurrentConnection().getConnectionAttributes();
/*      */   }
/*      */   
/*      */   public void setDetectCustomCollations(boolean detectCustomCollations) {
/* 2975 */     getCurrentConnection().setDetectCustomCollations(detectCustomCollations);
/*      */   }
/*      */   
/*      */   public boolean getDetectCustomCollations() {
/* 2979 */     return getCurrentConnection().getDetectCustomCollations();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ReplicationConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */