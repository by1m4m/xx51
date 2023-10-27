/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.ref.PhantomReference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.URLDecoder;
/*      */ import java.sql.Driver;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NonRegisteringDriver
/*      */   implements Driver
/*      */ {
/*      */   private static final String ALLOWED_QUOTES = "\"'";
/*      */   private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
/*      */   private static final String URL_PREFIX = "jdbc:mysql://";
/*      */   private static final String MXJ_URL_PREFIX = "jdbc:mysql:mxj://";
/*      */   public static final String LOADBALANCE_URL_PREFIX = "jdbc:mysql:loadbalance://";
/*   84 */   protected static final ConcurrentHashMap<ConnectionPhantomReference, ConnectionPhantomReference> connectionPhantomRefs = new ConcurrentHashMap();
/*      */   
/*   86 */   protected static final ReferenceQueue<ConnectionImpl> refQueue = new ReferenceQueue();
/*      */   
/*   88 */   public static final String OS = getOSName();
/*   89 */   public static final String PLATFORM = getPlatform();
/*      */   public static final String LICENSE = "GPL";
/*   91 */   public static final String RUNTIME_VENDOR = System.getProperty("java.vendor");
/*   92 */   public static final String RUNTIME_VERSION = System.getProperty("java.version");
/*      */   public static final String VERSION = "5.1.29";
/*      */   public static final String NAME = "MySQL Connector Java";
/*      */   public static final String DBNAME_PROPERTY_KEY = "DBNAME";
/*      */   public static final boolean DEBUG = false;
/*      */   public static final int HOST_NAME_INDEX = 0;
/*      */   public static final String HOST_PROPERTY_KEY = "HOST";
/*      */   public static final String NUM_HOSTS_PROPERTY_KEY = "NUM_HOSTS";
/*      */   public static final String PASSWORD_PROPERTY_KEY = "password";
/*      */   
/*      */   public static String getOSName() {
/*  103 */     return System.getProperty("os.name");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getPlatform()
/*      */   {
/*  113 */     return System.getProperty("os.arch");
/*      */   }
/*      */   
/*      */   static
/*      */   {
/*  118 */     AbandonedConnectionCleanupThread referenceThread = new AbandonedConnectionCleanupThread();
/*  119 */     referenceThread.setDaemon(true);
/*  120 */     referenceThread.start();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int PORT_NUMBER_INDEX = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String PORT_PROPERTY_KEY = "PORT";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String PROPERTIES_TRANSFORM_KEY = "propertiesTransform";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean TRACE = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String USE_CONFIG_PROPERTY_KEY = "useConfigs";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String USER_PROPERTY_KEY = "user";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String PROTOCOL_PROPERTY_KEY = "PROTOCOL";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String PATH_PROPERTY_KEY = "PATH";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int getMajorVersionInternal()
/*      */   {
/*  180 */     return safeIntParse("5");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int getMinorVersionInternal()
/*      */   {
/*  189 */     return safeIntParse("1");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static String[] parseHostPortPair(String hostPortPair)
/*      */     throws SQLException
/*      */   {
/*  210 */     String[] splitValues = new String[2];
/*      */     
/*  212 */     if (StringUtils.startsWithIgnoreCaseAndWs(hostPortPair, "address")) {
/*  213 */       splitValues[0] = hostPortPair.trim();
/*  214 */       splitValues[1] = null;
/*      */       
/*  216 */       return splitValues;
/*      */     }
/*      */     
/*  219 */     int portIndex = hostPortPair.indexOf(":");
/*      */     
/*  221 */     String hostname = null;
/*      */     
/*  223 */     if (portIndex != -1) {
/*  224 */       if (portIndex + 1 < hostPortPair.length()) {
/*  225 */         String portAsString = hostPortPair.substring(portIndex + 1);
/*  226 */         hostname = hostPortPair.substring(0, portIndex);
/*      */         
/*  228 */         splitValues[0] = hostname;
/*      */         
/*  230 */         splitValues[1] = portAsString;
/*      */       } else {
/*  232 */         throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.37"), "01S00", null);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  237 */       splitValues[0] = hostPortPair;
/*  238 */       splitValues[1] = null;
/*      */     }
/*      */     
/*  241 */     return splitValues;
/*      */   }
/*      */   
/*      */   private static int safeIntParse(String intAsString) {
/*      */     try {
/*  246 */       return Integer.parseInt(intAsString);
/*      */     } catch (NumberFormatException nfe) {}
/*  248 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean acceptsURL(String url)
/*      */     throws SQLException
/*      */   {
/*  278 */     return parseURL(url, null) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Connection connect(String url, Properties info)
/*      */     throws SQLException
/*      */   {
/*  327 */     if (url != null) {
/*  328 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://"))
/*  329 */         return connectLoadBalanced(url, info);
/*  330 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://"))
/*      */       {
/*  332 */         return connectReplicationConnection(url, info);
/*      */       }
/*      */     }
/*      */     
/*  336 */     Properties props = null;
/*      */     
/*  338 */     if ((props = parseURL(url, info)) == null) {
/*  339 */       return null;
/*      */     }
/*      */     
/*  342 */     if (!"1".equals(props.getProperty("NUM_HOSTS"))) {
/*  343 */       return connectFailover(url, info);
/*      */     }
/*      */     try
/*      */     {
/*  347 */       return ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*      */ 
/*  354 */       throw sqlEx;
/*      */     } catch (Exception ex) {
/*  356 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("NonRegisteringDriver.17") + ex.toString() + Messages.getString("NonRegisteringDriver.18"), "08001", null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  362 */       sqlEx.initCause(ex);
/*      */       
/*  364 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static void trackConnection(Connection newConn)
/*      */   {
/*  370 */     ConnectionPhantomReference phantomRef = new ConnectionPhantomReference((ConnectionImpl)newConn, refQueue);
/*  371 */     connectionPhantomRefs.put(phantomRef, phantomRef);
/*      */   }
/*      */   
/*      */   private java.sql.Connection connectLoadBalanced(String url, Properties info) throws SQLException
/*      */   {
/*  376 */     Properties parsedProps = parseURL(url, info);
/*      */     
/*  378 */     if (parsedProps == null) {
/*  379 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  383 */     parsedProps.remove("roundRobinLoadBalance");
/*      */     
/*  385 */     int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
/*      */     
/*  387 */     List<String> hostList = new ArrayList();
/*      */     
/*  389 */     for (int i = 0; i < numHosts; i++) {
/*  390 */       int index = i + 1;
/*      */       
/*  392 */       hostList.add(parsedProps.getProperty(new StringBuilder().append("HOST.").append(index).toString()) + ":" + parsedProps.getProperty(new StringBuilder().append("PORT.").append(index).toString()));
/*      */     }
/*      */     
/*      */ 
/*  396 */     LoadBalancingConnectionProxy proxyBal = new LoadBalancingConnectionProxy(hostList, parsedProps);
/*      */     
/*      */ 
/*  399 */     return (java.sql.Connection)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { LoadBalancedConnection.class }, proxyBal);
/*      */   }
/*      */   
/*      */ 
/*      */   private java.sql.Connection connectFailover(String url, Properties info)
/*      */     throws SQLException
/*      */   {
/*  406 */     Properties parsedProps = parseURL(url, info);
/*      */     
/*  408 */     if (parsedProps == null) {
/*  409 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  413 */     parsedProps.remove("roundRobinLoadBalance");
/*  414 */     parsedProps.setProperty("autoReconnect", "false");
/*      */     
/*  416 */     int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
/*      */     
/*      */ 
/*  419 */     List<String> hostList = new ArrayList();
/*      */     
/*  421 */     for (int i = 0; i < numHosts; i++) {
/*  422 */       int index = i + 1;
/*      */       
/*  424 */       hostList.add(parsedProps.getProperty(new StringBuilder().append("HOST.").append(index).toString()) + ":" + parsedProps.getProperty(new StringBuilder().append("PORT.").append(index).toString()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  430 */     FailoverConnectionProxy connProxy = new FailoverConnectionProxy(hostList, parsedProps);
/*      */     
/*      */ 
/*  433 */     return (java.sql.Connection)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Connection.class }, connProxy);
/*      */   }
/*      */   
/*      */ 
/*      */   protected java.sql.Connection connectReplicationConnection(String url, Properties info)
/*      */     throws SQLException
/*      */   {
/*  440 */     Properties parsedProps = parseURL(url, info);
/*      */     
/*  442 */     if (parsedProps == null) {
/*  443 */       return null;
/*      */     }
/*      */     
/*  446 */     Properties masterProps = (Properties)parsedProps.clone();
/*  447 */     Properties slavesProps = (Properties)parsedProps.clone();
/*      */     
/*      */ 
/*      */ 
/*  451 */     slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
/*      */     
/*      */ 
/*  454 */     int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
/*      */     
/*  456 */     if (numHosts < 2) {
/*  457 */       throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00", null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  462 */     List<String> slaveHostList = new ArrayList();
/*  463 */     List<String> masterHostList = new ArrayList();
/*      */     
/*      */ 
/*  466 */     String firstHost = masterProps.getProperty("HOST.1") + ":" + masterProps.getProperty("PORT.1");
/*      */     
/*      */ 
/*  469 */     boolean usesExplicitServerType = isHostPropertiesList(firstHost);
/*      */     
/*  471 */     for (int i = 0; i < numHosts; i++) {
/*  472 */       int index = i + 1;
/*      */       
/*  474 */       masterProps.remove("HOST." + index);
/*  475 */       masterProps.remove("PORT." + index);
/*  476 */       slavesProps.remove("HOST." + index);
/*  477 */       slavesProps.remove("PORT." + index);
/*      */       
/*  479 */       String host = parsedProps.getProperty("HOST." + index);
/*  480 */       String port = parsedProps.getProperty("PORT." + index);
/*  481 */       if (usesExplicitServerType) {
/*  482 */         if (isHostMaster(host)) {
/*  483 */           masterHostList.add(host);
/*      */         } else {
/*  485 */           slaveHostList.add(host);
/*      */         }
/*      */       }
/*  488 */       else if (i == 0) {
/*  489 */         masterHostList.add(host + ":" + port);
/*      */       } else {
/*  491 */         slaveHostList.add(host + ":" + port);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  496 */     slavesProps.remove("NUM_HOSTS");
/*  497 */     masterProps.remove("NUM_HOSTS");
/*  498 */     masterProps.remove("HOST");
/*  499 */     masterProps.remove("PORT");
/*  500 */     slavesProps.remove("HOST");
/*  501 */     slavesProps.remove("PORT");
/*      */     
/*      */ 
/*  504 */     return new ReplicationConnection(masterProps, slavesProps, masterHostList, slaveHostList);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isHostMaster(String host)
/*      */   {
/*  510 */     if (isHostPropertiesList(host)) {
/*  511 */       Properties hostSpecificProps = expandHostKeyValues(host);
/*  512 */       if ((hostSpecificProps.containsKey("type")) && ("master".equalsIgnoreCase(hostSpecificProps.get("type").toString())))
/*      */       {
/*  514 */         return true;
/*      */       }
/*      */     }
/*  517 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String database(Properties props)
/*      */   {
/*  529 */     return props.getProperty("DBNAME");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMajorVersion()
/*      */   {
/*  538 */     return getMajorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMinorVersion()
/*      */   {
/*  547 */     return getMinorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
/*      */     throws SQLException
/*      */   {
/*  578 */     if (info == null) {
/*  579 */       info = new Properties();
/*      */     }
/*      */     
/*  582 */     if ((url != null) && (url.startsWith("jdbc:mysql://"))) {
/*  583 */       info = parseURL(url, info);
/*      */     }
/*      */     
/*  586 */     DriverPropertyInfo hostProp = new DriverPropertyInfo("HOST", info.getProperty("HOST"));
/*      */     
/*  588 */     hostProp.required = true;
/*  589 */     hostProp.description = Messages.getString("NonRegisteringDriver.3");
/*      */     
/*  591 */     DriverPropertyInfo portProp = new DriverPropertyInfo("PORT", info.getProperty("PORT", "3306"));
/*      */     
/*  593 */     portProp.required = false;
/*  594 */     portProp.description = Messages.getString("NonRegisteringDriver.7");
/*      */     
/*  596 */     DriverPropertyInfo dbProp = new DriverPropertyInfo("DBNAME", info.getProperty("DBNAME"));
/*      */     
/*  598 */     dbProp.required = false;
/*  599 */     dbProp.description = "Database name";
/*      */     
/*  601 */     DriverPropertyInfo userProp = new DriverPropertyInfo("user", info.getProperty("user"));
/*      */     
/*  603 */     userProp.required = true;
/*  604 */     userProp.description = Messages.getString("NonRegisteringDriver.13");
/*      */     
/*  606 */     DriverPropertyInfo passwordProp = new DriverPropertyInfo("password", info.getProperty("password"));
/*      */     
/*      */ 
/*  609 */     passwordProp.required = true;
/*  610 */     passwordProp.description = Messages.getString("NonRegisteringDriver.16");
/*      */     
/*      */ 
/*  613 */     DriverPropertyInfo[] dpi = ConnectionPropertiesImpl.exposeAsDriverPropertyInfo(info, 5);
/*      */     
/*      */ 
/*  616 */     dpi[0] = hostProp;
/*  617 */     dpi[1] = portProp;
/*  618 */     dpi[2] = dbProp;
/*  619 */     dpi[3] = userProp;
/*  620 */     dpi[4] = passwordProp;
/*      */     
/*  622 */     return dpi;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String host(Properties props)
/*      */   {
/*  639 */     return props.getProperty("HOST", "localhost");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean jdbcCompliant()
/*      */   {
/*  655 */     return false;
/*      */   }
/*      */   
/*      */   public Properties parseURL(String url, Properties defaults) throws SQLException
/*      */   {
/*  660 */     Properties urlProps = defaults != null ? new Properties(defaults) : new Properties();
/*      */     
/*      */ 
/*  663 */     if (url == null) {
/*  664 */       return null;
/*      */     }
/*      */     
/*  667 */     if ((!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://")))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  674 */       return null;
/*      */     }
/*      */     
/*  677 */     int beginningOfSlashes = url.indexOf("//");
/*      */     
/*  679 */     if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://"))
/*      */     {
/*  681 */       urlProps.setProperty("socketFactory", "com.mysql.management.driverlaunched.ServerLauncherSocketFactory");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  690 */     int index = url.indexOf("?");
/*      */     
/*  692 */     if (index != -1) {
/*  693 */       String paramString = url.substring(index + 1, url.length());
/*  694 */       url = url.substring(0, index);
/*      */       
/*  696 */       StringTokenizer queryParams = new StringTokenizer(paramString, "&");
/*      */       
/*  698 */       while (queryParams.hasMoreTokens()) {
/*  699 */         String parameterValuePair = queryParams.nextToken();
/*      */         
/*  701 */         int indexOfEquals = StringUtils.indexOfIgnoreCase(0, parameterValuePair, "=");
/*      */         
/*      */ 
/*  704 */         String parameter = null;
/*  705 */         String value = null;
/*      */         
/*  707 */         if (indexOfEquals != -1) {
/*  708 */           parameter = parameterValuePair.substring(0, indexOfEquals);
/*      */           
/*  710 */           if (indexOfEquals + 1 < parameterValuePair.length()) {
/*  711 */             value = parameterValuePair.substring(indexOfEquals + 1);
/*      */           }
/*      */         }
/*      */         
/*  715 */         if ((value != null) && (value.length() > 0) && (parameter != null) && (parameter.length() > 0)) {
/*      */           try
/*      */           {
/*  718 */             urlProps.put(parameter, URLDecoder.decode(value, "UTF-8"));
/*      */           }
/*      */           catch (UnsupportedEncodingException badEncoding)
/*      */           {
/*  722 */             urlProps.put(parameter, URLDecoder.decode(value));
/*      */           }
/*      */           catch (NoSuchMethodError nsme) {
/*  725 */             urlProps.put(parameter, URLDecoder.decode(value));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  731 */     url = url.substring(beginningOfSlashes + 2);
/*      */     
/*  733 */     String hostStuff = null;
/*      */     
/*  735 */     int slashIndex = StringUtils.indexOfIgnoreCaseRespectMarker(0, url, "/", "\"'", "\"'", true);
/*      */     
/*  737 */     if (slashIndex != -1) {
/*  738 */       hostStuff = url.substring(0, slashIndex);
/*      */       
/*  740 */       if (slashIndex + 1 < url.length()) {
/*  741 */         urlProps.put("DBNAME", url.substring(slashIndex + 1, url.length()));
/*      */       }
/*      */     }
/*      */     else {
/*  745 */       hostStuff = url;
/*      */     }
/*      */     
/*  748 */     int numHosts = 0;
/*      */     
/*  750 */     if ((hostStuff != null) && (hostStuff.trim().length() > 0)) {
/*  751 */       List<String> hosts = StringUtils.split(hostStuff, ",", "\"'", "\"'", false);
/*      */       
/*      */ 
/*  754 */       for (String hostAndPort : hosts) {
/*  755 */         numHosts++;
/*      */         
/*  757 */         String[] hostPortPair = parseHostPortPair(hostAndPort);
/*      */         
/*  759 */         if ((hostPortPair[0] != null) && (hostPortPair[0].trim().length() > 0)) {
/*  760 */           urlProps.setProperty("HOST." + numHosts, hostPortPair[0]);
/*      */         } else {
/*  762 */           urlProps.setProperty("HOST." + numHosts, "localhost");
/*      */         }
/*      */         
/*  765 */         if (hostPortPair[1] != null) {
/*  766 */           urlProps.setProperty("PORT." + numHosts, hostPortPair[1]);
/*      */         } else {
/*  768 */           urlProps.setProperty("PORT." + numHosts, "3306");
/*      */         }
/*      */       }
/*      */     } else {
/*  772 */       numHosts = 1;
/*  773 */       urlProps.setProperty("HOST.1", "localhost");
/*  774 */       urlProps.setProperty("PORT.1", "3306");
/*      */     }
/*      */     
/*  777 */     urlProps.setProperty("NUM_HOSTS", String.valueOf(numHosts));
/*  778 */     urlProps.setProperty("HOST", urlProps.getProperty("HOST.1"));
/*  779 */     urlProps.setProperty("PORT", urlProps.getProperty("PORT.1"));
/*      */     
/*  781 */     String propertiesTransformClassName = urlProps.getProperty("propertiesTransform");
/*      */     
/*      */ 
/*  784 */     if (propertiesTransformClassName != null) {
/*      */       try {
/*  786 */         ConnectionPropertiesTransform propTransformer = (ConnectionPropertiesTransform)Class.forName(propertiesTransformClassName).newInstance();
/*      */         
/*      */ 
/*  789 */         urlProps = propTransformer.transformProperties(urlProps);
/*      */       } catch (InstantiationException e) {
/*  791 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (IllegalAccessException e)
/*      */       {
/*      */ 
/*  798 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (ClassNotFoundException e)
/*      */       {
/*      */ 
/*  805 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  814 */     if ((Util.isColdFusion()) && (urlProps.getProperty("autoConfigureForColdFusion", "true").equalsIgnoreCase("true")))
/*      */     {
/*  816 */       String configs = urlProps.getProperty("useConfigs");
/*      */       
/*  818 */       StringBuffer newConfigs = new StringBuffer();
/*      */       
/*  820 */       if (configs != null) {
/*  821 */         newConfigs.append(configs);
/*  822 */         newConfigs.append(",");
/*      */       }
/*      */       
/*  825 */       newConfigs.append("coldFusion");
/*      */       
/*  827 */       urlProps.setProperty("useConfigs", newConfigs.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  833 */     String configNames = null;
/*      */     
/*  835 */     if (defaults != null) {
/*  836 */       configNames = defaults.getProperty("useConfigs");
/*      */     }
/*      */     
/*  839 */     if (configNames == null) {
/*  840 */       configNames = urlProps.getProperty("useConfigs");
/*      */     }
/*      */     
/*  843 */     if (configNames != null) {
/*  844 */       List<String> splitNames = StringUtils.split(configNames, ",", true);
/*      */       
/*  846 */       Properties configProps = new Properties();
/*      */       
/*  848 */       Iterator<String> namesIter = splitNames.iterator();
/*      */       
/*  850 */       while (namesIter.hasNext()) {
/*  851 */         String configName = (String)namesIter.next();
/*      */         try
/*      */         {
/*  854 */           InputStream configAsStream = getClass().getResourceAsStream("configs/" + configName + ".properties");
/*      */           
/*      */ 
/*      */ 
/*  858 */           if (configAsStream == null) {
/*  859 */             throw SQLError.createSQLException("Can't find configuration template named '" + configName + "'", "01S00", null);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  865 */           configProps.load(configAsStream);
/*      */         } catch (IOException ioEx) {
/*  867 */           SQLException sqlEx = SQLError.createSQLException("Unable to load configuration template '" + configName + "' due to underlying IOException: " + ioEx, "01S00", null);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  873 */           sqlEx.initCause(ioEx);
/*      */           
/*  875 */           throw sqlEx;
/*      */         }
/*      */       }
/*      */       
/*  879 */       Iterator<Object> propsIter = urlProps.keySet().iterator();
/*      */       
/*  881 */       while (propsIter.hasNext()) {
/*  882 */         String key = propsIter.next().toString();
/*  883 */         String property = urlProps.getProperty(key);
/*  884 */         configProps.setProperty(key, property);
/*      */       }
/*      */       
/*  887 */       urlProps = configProps;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  892 */     if (defaults != null) {
/*  893 */       Iterator<Object> propsIter = defaults.keySet().iterator();
/*      */       
/*  895 */       while (propsIter.hasNext()) {
/*  896 */         String key = propsIter.next().toString();
/*  897 */         if (!key.equals("NUM_HOSTS")) {
/*  898 */           String property = defaults.getProperty(key);
/*  899 */           urlProps.setProperty(key, property);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  904 */     return urlProps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int port(Properties props)
/*      */   {
/*  916 */     return Integer.parseInt(props.getProperty("PORT", "3306"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String property(String name, Properties props)
/*      */   {
/*  930 */     return props.getProperty(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Properties expandHostKeyValues(String host)
/*      */   {
/*  940 */     Properties hostProps = new Properties();
/*      */     
/*  942 */     if (isHostPropertiesList(host)) {
/*  943 */       host = host.substring("address=".length() + 1);
/*  944 */       List<String> hostPropsList = StringUtils.split(host, ")", "'\"", "'\"", true);
/*      */       
/*  946 */       for (String propDef : hostPropsList) {
/*  947 */         if (propDef.startsWith("(")) {
/*  948 */           propDef = propDef.substring(1);
/*      */         }
/*      */         
/*  951 */         List<String> kvp = StringUtils.split(propDef, "=", "'\"", "'\"", true);
/*      */         
/*  953 */         String key = (String)kvp.get(0);
/*  954 */         String value = kvp.size() > 1 ? (String)kvp.get(1) : null;
/*      */         
/*  956 */         if ((value != null) && (((value.startsWith("\"")) && (value.endsWith("\""))) || ((value.startsWith("'")) && (value.endsWith("'"))))) {
/*  957 */           value = value.substring(1, value.length() - 1);
/*      */         }
/*      */         
/*  960 */         if (value != null) {
/*  961 */           if (("HOST".equalsIgnoreCase(key)) || ("DBNAME".equalsIgnoreCase(key)) || ("PORT".equalsIgnoreCase(key)) || ("PROTOCOL".equalsIgnoreCase(key)) || ("PATH".equalsIgnoreCase(key)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  966 */             key = key.toUpperCase(Locale.ENGLISH);
/*  967 */           } else if (("user".equalsIgnoreCase(key)) || ("password".equalsIgnoreCase(key)))
/*      */           {
/*  969 */             key = key.toLowerCase(Locale.ENGLISH);
/*      */           }
/*      */           
/*  972 */           hostProps.setProperty(key, value);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  977 */     return hostProps;
/*      */   }
/*      */   
/*      */ 
/*  981 */   public static boolean isHostPropertiesList(String host) { return (host != null) && (StringUtils.startsWithIgnoreCase(host, "address=")); }
/*      */   
/*      */   public NonRegisteringDriver() throws SQLException
/*      */   {}
/*      */   
/*      */   static class ConnectionPhantomReference extends PhantomReference<ConnectionImpl> { private NetworkResources io;
/*      */     
/*  988 */     ConnectionPhantomReference(ConnectionImpl connectionImpl, ReferenceQueue<ConnectionImpl> q) { super(q);
/*      */       try
/*      */       {
/*  991 */         this.io = connectionImpl.getIO().getNetworkResources();
/*      */       }
/*      */       catch (SQLException e) {}
/*      */     }
/*      */     
/*      */     void cleanup()
/*      */     {
/*  998 */       if (this.io != null) {
/*      */         try {
/* 1000 */           this.io.forceClose();
/*      */         } finally {
/* 1002 */           this.io = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\NonRegisteringDriver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */