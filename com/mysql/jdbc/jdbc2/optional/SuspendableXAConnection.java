/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ConnectionImpl;
/*     */ import com.mysql.jdbc.Util;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
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
/*     */ public class SuspendableXAConnection
/*     */   extends MysqlPooledConnection
/*     */   implements XAConnection, XAResource
/*     */ {
/*     */   private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
/*     */   
/*     */   static
/*     */   {
/*  46 */     if (Util.isJdbc4()) {
/*     */       try {
/*  48 */         JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4SuspendableXAConnection").getConstructor(new Class[] { ConnectionImpl.class });
/*     */ 
/*     */       }
/*     */       catch (SecurityException e)
/*     */       {
/*  53 */         throw new RuntimeException(e);
/*     */       } catch (NoSuchMethodException e) {
/*  55 */         throw new RuntimeException(e);
/*     */       } catch (ClassNotFoundException e) {
/*  57 */         throw new RuntimeException(e);
/*     */       }
/*     */     } else {
/*  60 */       JDBC_4_XA_CONNECTION_WRAPPER_CTOR = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static SuspendableXAConnection getInstance(ConnectionImpl mysqlConnection) throws SQLException {
/*  65 */     if (!Util.isJdbc4()) {
/*  66 */       return new SuspendableXAConnection(mysqlConnection);
/*     */     }
/*     */     
/*  69 */     return (SuspendableXAConnection)Util.handleNewInstance(JDBC_4_XA_CONNECTION_WRAPPER_CTOR, new Object[] { mysqlConnection }, mysqlConnection.getExceptionInterceptor());
/*     */   }
/*     */   
/*     */ 
/*     */   public SuspendableXAConnection(ConnectionImpl connection)
/*     */   {
/*  75 */     super(connection);
/*  76 */     this.underlyingConnection = connection;
/*     */   }
/*     */   
/*  79 */   private static final Map<Xid, XAConnection> XIDS_TO_PHYSICAL_CONNECTIONS = new HashMap();
/*     */   
/*     */ 
/*     */   private Xid currentXid;
/*     */   
/*     */ 
/*     */   private XAConnection currentXAConnection;
/*     */   
/*     */ 
/*     */   private XAResource currentXAResource;
/*     */   
/*     */   private ConnectionImpl underlyingConnection;
/*     */   
/*     */ 
/*     */   private static synchronized XAConnection findConnectionForXid(ConnectionImpl connectionToWrap, Xid xid)
/*     */     throws SQLException
/*     */   {
/*  96 */     XAConnection conn = (XAConnection)XIDS_TO_PHYSICAL_CONNECTIONS.get(xid);
/*     */     
/*  98 */     if (conn == null) {
/*  99 */       conn = new MysqlXAConnection(connectionToWrap, connectionToWrap.getLogXaCommands());
/*     */       
/* 101 */       XIDS_TO_PHYSICAL_CONNECTIONS.put(xid, conn);
/*     */     }
/*     */     
/* 104 */     return conn;
/*     */   }
/*     */   
/*     */   private static synchronized void removeXAConnectionMapping(Xid xid) {
/* 108 */     XIDS_TO_PHYSICAL_CONNECTIONS.remove(xid);
/*     */   }
/*     */   
/*     */   private synchronized void switchToXid(Xid xid) throws XAException {
/* 112 */     if (xid == null) {
/* 113 */       throw new XAException();
/*     */     }
/*     */     try
/*     */     {
/* 117 */       if (!xid.equals(this.currentXid)) {
/* 118 */         XAConnection toSwitchTo = findConnectionForXid(this.underlyingConnection, xid);
/* 119 */         this.currentXAConnection = toSwitchTo;
/* 120 */         this.currentXid = xid;
/* 121 */         this.currentXAResource = toSwitchTo.getXAResource();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 124 */       throw new XAException();
/*     */     }
/*     */   }
/*     */   
/*     */   public XAResource getXAResource() throws SQLException {
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public void commit(Xid xid, boolean arg1) throws XAException {
/* 133 */     switchToXid(xid);
/* 134 */     this.currentXAResource.commit(xid, arg1);
/* 135 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public void end(Xid xid, int arg1) throws XAException {
/* 139 */     switchToXid(xid);
/* 140 */     this.currentXAResource.end(xid, arg1);
/*     */   }
/*     */   
/*     */   public void forget(Xid xid) throws XAException {
/* 144 */     switchToXid(xid);
/* 145 */     this.currentXAResource.forget(xid);
/*     */     
/* 147 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public int getTransactionTimeout() throws XAException {
/* 151 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isSameRM(XAResource xaRes) throws XAException {
/* 155 */     return xaRes == this;
/*     */   }
/*     */   
/*     */   public int prepare(Xid xid) throws XAException {
/* 159 */     switchToXid(xid);
/* 160 */     return this.currentXAResource.prepare(xid);
/*     */   }
/*     */   
/*     */   public Xid[] recover(int flag) throws XAException {
/* 164 */     return MysqlXAConnection.recover(this.underlyingConnection, flag);
/*     */   }
/*     */   
/*     */   public void rollback(Xid xid) throws XAException {
/* 168 */     switchToXid(xid);
/* 169 */     this.currentXAResource.rollback(xid);
/* 170 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public boolean setTransactionTimeout(int arg0) throws XAException {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   public void start(Xid xid, int arg1) throws XAException {
/* 178 */     switchToXid(xid);
/*     */     
/* 180 */     if (arg1 != 2097152) {
/* 181 */       this.currentXAResource.start(xid, arg1);
/*     */       
/* 183 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */     this.currentXAResource.start(xid, 134217728);
/*     */   }
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/* 194 */     if (this.currentXAConnection == null) {
/* 195 */       return getConnection(false, true);
/*     */     }
/*     */     
/* 198 */     return this.currentXAConnection.getConnection();
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/* 202 */     if (this.currentXAConnection == null) {
/* 203 */       super.close();
/*     */     } else {
/* 205 */       removeXAConnectionMapping(this.currentXid);
/* 206 */       this.currentXAConnection.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\SuspendableXAConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */