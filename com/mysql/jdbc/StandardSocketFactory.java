/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardSocketFactory
/*     */   implements SocketFactory, SocketMetadata
/*     */ {
/*     */   public static final String TCP_NO_DELAY_PROPERTY_NAME = "tcpNoDelay";
/*     */   public static final String TCP_KEEP_ALIVE_DEFAULT_VALUE = "true";
/*     */   public static final String TCP_KEEP_ALIVE_PROPERTY_NAME = "tcpKeepAlive";
/*     */   public static final String TCP_RCV_BUF_PROPERTY_NAME = "tcpRcvBuf";
/*     */   public static final String TCP_SND_BUF_PROPERTY_NAME = "tcpSndBuf";
/*     */   public static final String TCP_TRAFFIC_CLASS_PROPERTY_NAME = "tcpTrafficClass";
/*     */   public static final String TCP_RCV_BUF_DEFAULT_VALUE = "0";
/*     */   public static final String TCP_SND_BUF_DEFAULT_VALUE = "0";
/*     */   public static final String TCP_TRAFFIC_CLASS_DEFAULT_VALUE = "0";
/*     */   public static final String TCP_NO_DELAY_DEFAULT_VALUE = "true";
/*     */   private static Method setTraficClassMethod;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  72 */       setTraficClassMethod = Socket.class.getMethod("setTrafficClass", new Class[] { Integer.TYPE });
/*     */     }
/*     */     catch (SecurityException e) {
/*  75 */       setTraficClassMethod = null;
/*     */     } catch (NoSuchMethodException e) {
/*  77 */       setTraficClassMethod = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  82 */   protected String host = null;
/*     */   
/*     */ 
/*  85 */   protected int port = 3306;
/*     */   
/*     */ 
/*  88 */   protected Socket rawSocket = null;
/*     */   
/*     */ 
/*  91 */   protected int loginTimeoutCountdown = DriverManager.getLoginTimeout() * 1000;
/*     */   
/*     */ 
/*  94 */   protected long loginTimeoutCheckTimestamp = System.currentTimeMillis();
/*     */   
/*     */ 
/*  97 */   protected int socketTimeoutBackup = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME = "com.mysql.jdbc.test.isLocalHostnameReplacement";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket afterHandshake()
/*     */     throws SocketException, IOException
/*     */   {
/* 111 */     resetLoginTimeCountdown();
/* 112 */     this.rawSocket.setSoTimeout(this.socketTimeoutBackup);
/* 113 */     return this.rawSocket;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Socket beforeHandshake()
/*     */     throws SocketException, IOException
/*     */   {
/* 128 */     resetLoginTimeCountdown();
/* 129 */     this.socketTimeoutBackup = this.rawSocket.getSoTimeout();
/* 130 */     this.rawSocket.setSoTimeout(getRealTimeout(this.socketTimeoutBackup));
/* 131 */     return this.rawSocket;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void configureSocket(Socket sock, Properties props)
/*     */     throws SocketException, IOException
/*     */   {
/*     */     try
/*     */     {
/* 145 */       sock.setTcpNoDelay(Boolean.valueOf(props.getProperty("tcpNoDelay", "true")).booleanValue());
/*     */       
/*     */ 
/*     */ 
/* 149 */       String keepAlive = props.getProperty("tcpKeepAlive", "true");
/*     */       
/*     */ 
/* 152 */       if ((keepAlive != null) && (keepAlive.length() > 0)) {
/* 153 */         sock.setKeepAlive(Boolean.valueOf(keepAlive).booleanValue());
/*     */       }
/*     */       
/*     */ 
/* 157 */       int receiveBufferSize = Integer.parseInt(props.getProperty("tcpRcvBuf", "0"));
/*     */       
/*     */ 
/* 160 */       if (receiveBufferSize > 0) {
/* 161 */         sock.setReceiveBufferSize(receiveBufferSize);
/*     */       }
/*     */       
/* 164 */       int sendBufferSize = Integer.parseInt(props.getProperty("tcpSndBuf", "0"));
/*     */       
/*     */ 
/* 167 */       if (sendBufferSize > 0) {
/* 168 */         sock.setSendBufferSize(sendBufferSize);
/*     */       }
/*     */       
/* 171 */       int trafficClass = Integer.parseInt(props.getProperty("tcpTrafficClass", "0"));
/*     */       
/*     */ 
/*     */ 
/* 175 */       if ((trafficClass > 0) && (setTraficClassMethod != null)) {
/* 176 */         setTraficClassMethod.invoke(sock, new Object[] { Integer.valueOf(trafficClass) });
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {
/* 180 */       unwrapExceptionToProperClassAndThrowIt(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Socket connect(String hostname, int portNumber, Properties props)
/*     */     throws SocketException, IOException
/*     */   {
/* 189 */     if (props != null) {
/* 190 */       this.host = hostname;
/*     */       
/* 192 */       this.port = portNumber;
/*     */       
/* 194 */       Method connectWithTimeoutMethod = null;
/* 195 */       Method socketBindMethod = null;
/* 196 */       Class<?> socketAddressClass = null;
/*     */       
/* 198 */       String localSocketHostname = props.getProperty("localSocketAddress");
/*     */       
/* 200 */       String connectTimeoutStr = props.getProperty("connectTimeout");
/*     */       
/* 202 */       int connectTimeout = 0;
/*     */       
/* 204 */       boolean wantsTimeout = ((connectTimeoutStr != null) && (connectTimeoutStr.length() > 0) && (!connectTimeoutStr.equals("0"))) || (this.loginTimeoutCountdown > 0);
/*     */       
/*     */ 
/* 207 */       boolean wantsLocalBind = (localSocketHostname != null) && (localSocketHostname.length() > 0);
/*     */       
/* 209 */       boolean needsConfigurationBeforeConnect = socketNeedsConfigurationBeforeConnect(props);
/*     */       
/* 211 */       if ((wantsTimeout) || (wantsLocalBind) || (needsConfigurationBeforeConnect)) {
/* 212 */         if (connectTimeoutStr != null) {
/*     */           try {
/* 214 */             connectTimeout = Integer.parseInt(connectTimeoutStr);
/*     */           } catch (NumberFormatException nfe) {
/* 216 */             throw new SocketException("Illegal value '" + connectTimeoutStr + "' for connectTimeout");
/*     */           }
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 222 */           socketAddressClass = Class.forName("java.net.SocketAddress");
/*     */           
/* 224 */           connectWithTimeoutMethod = Socket.class.getMethod("connect", new Class[] { socketAddressClass, Integer.TYPE });
/*     */           
/*     */ 
/* 227 */           socketBindMethod = Socket.class.getMethod("bind", new Class[] { socketAddressClass });
/*     */         }
/*     */         catch (NoClassDefFoundError noClassDefFound) {}catch (NoSuchMethodException noSuchMethodEx) {}catch (Throwable catchAll) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */         if ((wantsLocalBind) && (socketBindMethod == null)) {
/* 238 */           throw new SocketException("Can't specify \"localSocketAddress\" on JVMs older than 1.4");
/*     */         }
/*     */         
/* 241 */         if ((wantsTimeout) && (connectWithTimeoutMethod == null)) {
/* 242 */           throw new SocketException("Can't specify \"connectTimeout\" on JVMs older than 1.4");
/*     */         }
/*     */       }
/*     */       
/* 246 */       if (this.host != null) {
/* 247 */         if ((!wantsLocalBind) && (!wantsTimeout) && (!needsConfigurationBeforeConnect)) {
/* 248 */           InetAddress[] possibleAddresses = InetAddress.getAllByName(this.host);
/*     */           
/* 250 */           Throwable caughtWhileConnecting = null;
/*     */           
/*     */ 
/*     */ 
/* 254 */           for (int i = 0; i < possibleAddresses.length; i++) {
/*     */             try {
/* 256 */               this.rawSocket = new Socket(possibleAddresses[i], this.port);
/*     */               
/* 258 */               configureSocket(this.rawSocket, props);
/*     */             }
/*     */             catch (Exception ex)
/*     */             {
/* 262 */               resetLoginTimeCountdown();
/* 263 */               caughtWhileConnecting = ex;
/*     */             }
/*     */           }
/*     */           
/* 267 */           if (this.rawSocket == null) {
/* 268 */             unwrapExceptionToProperClassAndThrowIt(caughtWhileConnecting);
/*     */           }
/*     */         }
/*     */         else {
/*     */           try {
/* 273 */             InetAddress[] possibleAddresses = InetAddress.getAllByName(this.host);
/*     */             
/* 275 */             Throwable caughtWhileConnecting = null;
/*     */             
/* 277 */             Object localSockAddr = null;
/*     */             
/* 279 */             Class<?> inetSocketAddressClass = null;
/*     */             
/* 281 */             Constructor<?> addrConstructor = null;
/*     */             try
/*     */             {
/* 284 */               inetSocketAddressClass = Class.forName("java.net.InetSocketAddress");
/*     */               
/* 286 */               addrConstructor = inetSocketAddressClass.getConstructor(new Class[] { InetAddress.class, Integer.TYPE });
/*     */               
/*     */ 
/* 289 */               if (wantsLocalBind) {
/* 290 */                 localSockAddr = addrConstructor.newInstance(new Object[] { InetAddress.getByName(localSocketHostname), new Integer(0) });
/*     */               }
/*     */             }
/*     */             catch (Throwable ex)
/*     */             {
/* 295 */               unwrapExceptionToProperClassAndThrowIt(ex);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 300 */             for (int i = 0; i < possibleAddresses.length; i++) {
/*     */               try {
/* 302 */                 this.rawSocket = new Socket();
/*     */                 
/* 304 */                 configureSocket(this.rawSocket, props);
/*     */                 
/* 306 */                 Object sockAddr = addrConstructor.newInstance(new Object[] { possibleAddresses[i], Integer.valueOf(this.port) });
/*     */                 
/*     */ 
/* 309 */                 if (localSockAddr != null) {
/* 310 */                   socketBindMethod.invoke(this.rawSocket, new Object[] { localSockAddr });
/*     */                 }
/*     */                 
/* 313 */                 connectWithTimeoutMethod.invoke(this.rawSocket, new Object[] { sockAddr, Integer.valueOf(getRealTimeout(connectTimeout)) });
/*     */ 
/*     */               }
/*     */               catch (Exception ex)
/*     */               {
/* 318 */                 resetLoginTimeCountdown();
/* 319 */                 this.rawSocket = null;
/*     */                 
/* 321 */                 caughtWhileConnecting = ex;
/*     */               }
/*     */             }
/*     */             
/* 325 */             if (this.rawSocket == null) {
/* 326 */               unwrapExceptionToProperClassAndThrowIt(caughtWhileConnecting);
/*     */             }
/*     */           }
/*     */           catch (Throwable t) {
/* 330 */             unwrapExceptionToProperClassAndThrowIt(t);
/*     */           }
/*     */         }
/* 333 */         resetLoginTimeCountdown();
/*     */         
/* 335 */         return this.rawSocket;
/*     */       }
/*     */     }
/*     */     
/* 339 */     throw new SocketException("Unable to create socket");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean socketNeedsConfigurationBeforeConnect(Properties props)
/*     */   {
/* 347 */     int receiveBufferSize = Integer.parseInt(props.getProperty("tcpRcvBuf", "0"));
/*     */     
/*     */ 
/* 350 */     if (receiveBufferSize > 0) {
/* 351 */       return true;
/*     */     }
/*     */     
/* 354 */     int sendBufferSize = Integer.parseInt(props.getProperty("tcpSndBuf", "0"));
/*     */     
/*     */ 
/* 357 */     if (sendBufferSize > 0) {
/* 358 */       return true;
/*     */     }
/*     */     
/* 361 */     int trafficClass = Integer.parseInt(props.getProperty("tcpTrafficClass", "0"));
/*     */     
/*     */ 
/*     */ 
/* 365 */     if ((trafficClass > 0) && (setTraficClassMethod != null)) {
/* 366 */       return true;
/*     */     }
/*     */     
/* 369 */     return false;
/*     */   }
/*     */   
/*     */   private void unwrapExceptionToProperClassAndThrowIt(Throwable caughtWhileConnecting)
/*     */     throws SocketException, IOException
/*     */   {
/* 375 */     if ((caughtWhileConnecting instanceof InvocationTargetException))
/*     */     {
/*     */ 
/*     */ 
/* 379 */       caughtWhileConnecting = ((InvocationTargetException)caughtWhileConnecting).getTargetException();
/*     */     }
/*     */     
/*     */ 
/* 383 */     if ((caughtWhileConnecting instanceof SocketException)) {
/* 384 */       throw ((SocketException)caughtWhileConnecting);
/*     */     }
/*     */     
/* 387 */     if ((caughtWhileConnecting instanceof IOException)) {
/* 388 */       throw ((IOException)caughtWhileConnecting);
/*     */     }
/*     */     
/* 391 */     throw new SocketException(caughtWhileConnecting.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isLocallyConnected(ConnectionImpl conn)
/*     */     throws SQLException
/*     */   {
/* 398 */     long threadId = conn.getId();
/* 399 */     Statement processListStmt = conn.getMetadataSafeStatement();
/* 400 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 403 */       String processHost = null;
/*     */       
/* 405 */       rs = processListStmt.executeQuery("SHOW PROCESSLIST");
/*     */       
/* 407 */       while (rs.next()) {
/* 408 */         long id = rs.getLong(1);
/*     */         
/* 410 */         if (threadId == id)
/*     */         {
/* 412 */           processHost = rs.getString(3);
/*     */           
/* 414 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 419 */       if (System.getProperty("com.mysql.jdbc.test.isLocalHostnameReplacement") != null) {
/* 420 */         processHost = System.getProperty("com.mysql.jdbc.test.isLocalHostnameReplacement");
/*     */       }
/* 422 */       else if (conn.getProperties().getProperty("com.mysql.jdbc.test.isLocalHostnameReplacement") != null) {
/* 423 */         processHost = conn.getProperties().getProperty("com.mysql.jdbc.test.isLocalHostnameReplacement");
/*     */       }
/*     */       
/* 426 */       if ((processHost != null) && 
/* 427 */         (processHost.indexOf(":") != -1)) {
/* 428 */         processHost = processHost.split(":")[0];
/*     */         try
/*     */         {
/* 431 */           boolean isLocal = false;
/*     */           
/* 433 */           whereMysqlThinksIConnectedFrom = InetAddress.getByName(processHost);
/* 434 */           SocketAddress remoteSocketAddr = this.rawSocket.getRemoteSocketAddress();
/*     */           InetAddress whereIConnectedTo;
/* 436 */           if ((remoteSocketAddr instanceof InetSocketAddress)) {
/* 437 */             whereIConnectedTo = ((InetSocketAddress)remoteSocketAddr).getAddress();
/*     */             
/* 439 */             isLocal = whereMysqlThinksIConnectedFrom.equals(whereIConnectedTo);
/*     */           }
/*     */           
/* 442 */           return isLocal;
/*     */         } catch (UnknownHostException e) { InetAddress whereMysqlThinksIConnectedFrom;
/* 444 */           conn.getLog().logWarn(Messages.getString("Connection.CantDetectLocalConnect", new Object[] { this.host }), e);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 449 */           return 0;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 454 */       return 0;
/*     */     } finally {
/* 456 */       processListStmt.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void resetLoginTimeCountdown()
/*     */     throws SocketException
/*     */   {
/* 467 */     if (this.loginTimeoutCountdown > 0) {
/* 468 */       long now = System.currentTimeMillis();
/* 469 */       this.loginTimeoutCountdown = ((int)(this.loginTimeoutCountdown - (now - this.loginTimeoutCheckTimestamp)));
/* 470 */       if (this.loginTimeoutCountdown <= 0) {
/* 471 */         throw new SocketException(Messages.getString("Connection.LoginTimeout"));
/*     */       }
/* 473 */       this.loginTimeoutCheckTimestamp = now;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getRealTimeout(int expectedTimeout)
/*     */   {
/* 485 */     if ((this.loginTimeoutCountdown > 0) && ((expectedTimeout == 0) || (expectedTimeout > this.loginTimeoutCountdown))) {
/* 486 */       return this.loginTimeoutCountdown;
/*     */     }
/* 488 */     return expectedTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\StandardSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */