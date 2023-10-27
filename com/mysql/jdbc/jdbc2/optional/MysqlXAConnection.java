/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ConnectionImpl;
/*     */ import com.mysql.jdbc.Util;
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class MysqlXAConnection
/*     */   extends MysqlPooledConnection
/*     */   implements XAConnection, XAResource
/*     */ {
/*     */   private ConnectionImpl underlyingConnection;
/*     */   private static final Map<Integer, Integer> MYSQL_ERROR_CODES_TO_XA_ERROR_CODES;
/*     */   private Log log;
/*     */   protected boolean logXaCommands;
/*     */   private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;
/*     */   
/*     */   static
/*     */   {
/*  77 */     HashMap<Integer, Integer> temp = new HashMap();
/*     */     
/*  79 */     temp.put(Integer.valueOf(1397), Integer.valueOf(-4));
/*  80 */     temp.put(Integer.valueOf(1398), Integer.valueOf(-5));
/*  81 */     temp.put(Integer.valueOf(1399), Integer.valueOf(-7));
/*  82 */     temp.put(Integer.valueOf(1400), Integer.valueOf(-9));
/*  83 */     temp.put(Integer.valueOf(1401), Integer.valueOf(-3));
/*  84 */     temp.put(Integer.valueOf(1402), Integer.valueOf(100));
/*  85 */     temp.put(Integer.valueOf(1440), Integer.valueOf(-8));
/*     */     
/*  87 */     MYSQL_ERROR_CODES_TO_XA_ERROR_CODES = Collections.unmodifiableMap(temp);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */     if (Util.isJdbc4()) {
/*     */       try {
/*  95 */         JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlXAConnection").getConstructor(new Class[] { ConnectionImpl.class, Boolean.TYPE });
/*     */ 
/*     */       }
/*     */       catch (SecurityException e)
/*     */       {
/* 100 */         throw new RuntimeException(e);
/*     */       } catch (NoSuchMethodException e) {
/* 102 */         throw new RuntimeException(e);
/*     */       } catch (ClassNotFoundException e) {
/* 104 */         throw new RuntimeException(e);
/*     */       }
/*     */     } else {
/* 107 */       JDBC_4_XA_CONNECTION_WRAPPER_CTOR = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static MysqlXAConnection getInstance(ConnectionImpl mysqlConnection, boolean logXaCommands) throws SQLException
/*     */   {
/* 113 */     if (!Util.isJdbc4()) {
/* 114 */       return new MysqlXAConnection(mysqlConnection, logXaCommands);
/*     */     }
/*     */     
/* 117 */     return (MysqlXAConnection)Util.handleNewInstance(JDBC_4_XA_CONNECTION_WRAPPER_CTOR, new Object[] { mysqlConnection, Boolean.valueOf(logXaCommands) }, mysqlConnection.getExceptionInterceptor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MysqlXAConnection(ConnectionImpl connection, boolean logXaCommands)
/*     */     throws SQLException
/*     */   {
/* 128 */     super(connection);
/* 129 */     this.underlyingConnection = connection;
/* 130 */     this.log = connection.getLog();
/* 131 */     this.logXaCommands = logXaCommands;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XAResource getXAResource()
/*     */     throws SQLException
/*     */   {
/* 144 */     return this;
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
/*     */ 
/*     */ 
/*     */   public int getTransactionTimeout()
/*     */     throws XAException
/*     */   {
/* 161 */     return 0;
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
/*     */   public boolean setTransactionTimeout(int arg0)
/*     */     throws XAException
/*     */   {
/* 186 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSameRM(XAResource xares)
/*     */     throws XAException
/*     */   {
/* 206 */     if ((xares instanceof MysqlXAConnection)) {
/* 207 */       return this.underlyingConnection.isSameResource(((MysqlXAConnection)xares).underlyingConnection);
/*     */     }
/*     */     
/*     */ 
/* 211 */     return false;
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
/*     */   public Xid[] recover(int flag)
/*     */     throws XAException
/*     */   {
/* 252 */     return recover(this.underlyingConnection, flag);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Xid[] recover(Connection c, int flag)
/*     */     throws XAException
/*     */   {
/* 276 */     boolean startRscan = (flag & 0x1000000) > 0;
/* 277 */     boolean endRscan = (flag & 0x800000) > 0;
/*     */     
/* 279 */     if ((!startRscan) && (!endRscan) && (flag != 0)) {
/* 280 */       throw new MysqlXAException(-5, "Invalid flag, must use TMNOFLAGS, or any combination of TMSTARTRSCAN and TMENDRSCAN", null);
/*     */     }
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
/* 293 */     if (!startRscan) {
/* 294 */       return new Xid[0];
/*     */     }
/*     */     
/* 297 */     ResultSet rs = null;
/* 298 */     Statement stmt = null;
/*     */     
/* 300 */     List<MysqlXid> recoveredXidList = new ArrayList();
/*     */     
/*     */     try
/*     */     {
/* 304 */       stmt = c.createStatement();
/*     */       
/* 306 */       rs = stmt.executeQuery("XA RECOVER");
/*     */       
/* 308 */       while (rs.next()) {
/* 309 */         int formatId = rs.getInt(1);
/* 310 */         int gtridLength = rs.getInt(2);
/* 311 */         int bqualLength = rs.getInt(3);
/* 312 */         byte[] gtridAndBqual = rs.getBytes(4);
/*     */         
/* 314 */         byte[] gtrid = new byte[gtridLength];
/* 315 */         byte[] bqual = new byte[bqualLength];
/*     */         
/* 317 */         if (gtridAndBqual.length != gtridLength + bqualLength) {
/* 318 */           throw new MysqlXAException(105, "Error while recovering XIDs from RM. GTRID and BQUAL are wrong sizes", null);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 323 */         System.arraycopy(gtridAndBqual, 0, gtrid, 0, gtridLength);
/*     */         
/* 325 */         System.arraycopy(gtridAndBqual, gtridLength, bqual, 0, bqualLength);
/*     */         
/*     */ 
/* 328 */         recoveredXidList.add(new MysqlXid(gtrid, bqual, formatId));
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 332 */       throw mapXAExceptionFromSQLException(sqlEx);
/*     */     } finally {
/* 334 */       if (rs != null) {
/*     */         try {
/* 336 */           rs.close();
/*     */         } catch (SQLException sqlEx) {
/* 338 */           throw mapXAExceptionFromSQLException(sqlEx);
/*     */         }
/*     */       }
/*     */       
/* 342 */       if (stmt != null) {
/*     */         try {
/* 344 */           stmt.close();
/*     */         } catch (SQLException sqlEx) {
/* 346 */           throw mapXAExceptionFromSQLException(sqlEx);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 351 */     int numXids = recoveredXidList.size();
/*     */     
/* 353 */     Xid[] asXids = new Xid[numXids];
/* 354 */     Object[] asObjects = recoveredXidList.toArray();
/*     */     
/* 356 */     for (int i = 0; i < numXids; i++) {
/* 357 */       asXids[i] = ((Xid)asObjects[i]);
/*     */     }
/*     */     
/* 360 */     return asXids;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int prepare(Xid xid)
/*     */     throws XAException
/*     */   {
/* 382 */     StringBuffer commandBuf = new StringBuffer();
/* 383 */     commandBuf.append("XA PREPARE ");
/* 384 */     commandBuf.append(xidToString(xid));
/*     */     
/* 386 */     dispatchCommand(commandBuf.toString());
/*     */     
/* 388 */     return 0;
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
/*     */   public void rollback(Xid xid)
/*     */     throws XAException
/*     */   {
/* 424 */     StringBuffer commandBuf = new StringBuffer();
/* 425 */     commandBuf.append("XA ROLLBACK ");
/* 426 */     commandBuf.append(xidToString(xid));
/*     */     try
/*     */     {
/* 429 */       dispatchCommand(commandBuf.toString());
/*     */     } finally {
/* 431 */       this.underlyingConnection.setInGlobalTx(false);
/*     */     }
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
/*     */   public void end(Xid xid, int flags)
/*     */     throws XAException
/*     */   {
/* 463 */     StringBuffer commandBuf = new StringBuffer();
/* 464 */     commandBuf.append("XA END ");
/* 465 */     commandBuf.append(xidToString(xid));
/*     */     
/* 467 */     switch (flags) {
/*     */     case 67108864: 
/*     */       break;
/*     */     case 33554432: 
/* 471 */       commandBuf.append(" SUSPEND");
/* 472 */       break;
/*     */     case 536870912: 
/*     */       break;
/*     */     default: 
/* 476 */       throw new XAException(-5);
/*     */     }
/*     */     
/* 479 */     dispatchCommand(commandBuf.toString());
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
/*     */   public void start(Xid xid, int flags)
/*     */     throws XAException
/*     */   {
/* 506 */     StringBuffer commandBuf = new StringBuffer();
/* 507 */     commandBuf.append("XA START ");
/* 508 */     commandBuf.append(xidToString(xid));
/*     */     
/* 510 */     switch (flags) {
/*     */     case 2097152: 
/* 512 */       commandBuf.append(" JOIN");
/* 513 */       break;
/*     */     case 134217728: 
/* 515 */       commandBuf.append(" RESUME");
/* 516 */       break;
/*     */     case 0: 
/*     */       break;
/*     */     
/*     */     default: 
/* 521 */       throw new XAException(-5);
/*     */     }
/*     */     
/* 524 */     dispatchCommand(commandBuf.toString());
/*     */     
/* 526 */     this.underlyingConnection.setInGlobalTx(true);
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
/*     */   public void commit(Xid xid, boolean onePhase)
/*     */     throws XAException
/*     */   {
/* 551 */     StringBuffer commandBuf = new StringBuffer();
/* 552 */     commandBuf.append("XA COMMIT ");
/* 553 */     commandBuf.append(xidToString(xid));
/*     */     
/* 555 */     if (onePhase) {
/* 556 */       commandBuf.append(" ONE PHASE");
/*     */     }
/*     */     try
/*     */     {
/* 560 */       dispatchCommand(commandBuf.toString());
/*     */     } finally {
/* 562 */       this.underlyingConnection.setInGlobalTx(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private ResultSet dispatchCommand(String command) throws XAException {
/* 567 */     Statement stmt = null;
/*     */     try
/*     */     {
/* 570 */       if (this.logXaCommands) {
/* 571 */         this.log.logDebug("Executing XA statement: " + command);
/*     */       }
/*     */       
/*     */ 
/* 575 */       stmt = this.underlyingConnection.createStatement();
/*     */       
/*     */ 
/* 578 */       stmt.execute(command);
/*     */       
/* 580 */       ResultSet rs = stmt.getResultSet();
/*     */       
/* 582 */       return rs;
/*     */     } catch (SQLException sqlEx) {
/* 584 */       throw mapXAExceptionFromSQLException(sqlEx);
/*     */     } finally {
/* 586 */       if (stmt != null) {
/*     */         try {
/* 588 */           stmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static XAException mapXAExceptionFromSQLException(SQLException sqlEx)
/*     */   {
/* 597 */     Integer xaCode = (Integer)MYSQL_ERROR_CODES_TO_XA_ERROR_CODES.get(Integer.valueOf(sqlEx.getErrorCode()));
/*     */     
/*     */ 
/* 600 */     if (xaCode != null) {
/* 601 */       return new MysqlXAException(xaCode.intValue(), sqlEx.getMessage(), null);
/*     */     }
/*     */     
/*     */ 
/* 605 */     return new MysqlXAException(sqlEx.getMessage(), null);
/*     */   }
/*     */   
/*     */   private static String xidToString(Xid xid) {
/* 609 */     byte[] gtrid = xid.getGlobalTransactionId();
/*     */     
/* 611 */     byte[] btrid = xid.getBranchQualifier();
/*     */     
/* 613 */     int lengthAsString = 6;
/*     */     
/* 615 */     if (gtrid != null) {
/* 616 */       lengthAsString += 2 * gtrid.length;
/*     */     }
/*     */     
/* 619 */     if (btrid != null) {
/* 620 */       lengthAsString += 2 * btrid.length;
/*     */     }
/*     */     
/* 623 */     String formatIdInHex = Integer.toHexString(xid.getFormatId());
/*     */     
/* 625 */     lengthAsString += formatIdInHex.length();
/* 626 */     lengthAsString += 3;
/*     */     
/* 628 */     StringBuffer asString = new StringBuffer(lengthAsString);
/*     */     
/* 630 */     asString.append("0x");
/*     */     
/* 632 */     if (gtrid != null) {
/* 633 */       for (int i = 0; i < gtrid.length; i++) {
/* 634 */         String asHex = Integer.toHexString(gtrid[i] & 0xFF);
/*     */         
/* 636 */         if (asHex.length() == 1) {
/* 637 */           asString.append("0");
/*     */         }
/*     */         
/* 640 */         asString.append(asHex);
/*     */       }
/*     */     }
/*     */     
/* 644 */     asString.append(",");
/*     */     
/* 646 */     if (btrid != null) {
/* 647 */       asString.append("0x");
/*     */       
/* 649 */       for (int i = 0; i < btrid.length; i++) {
/* 650 */         String asHex = Integer.toHexString(btrid[i] & 0xFF);
/*     */         
/* 652 */         if (asHex.length() == 1) {
/* 653 */           asString.append("0");
/*     */         }
/*     */         
/* 656 */         asString.append(asHex);
/*     */       }
/*     */     }
/*     */     
/* 660 */     asString.append(",0x");
/* 661 */     asString.append(formatIdInHex);
/*     */     
/* 663 */     return asString.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 672 */     Connection connToWrap = getConnection(false, true);
/*     */     
/* 674 */     return connToWrap;
/*     */   }
/*     */   
/*     */   public void forget(Xid xid)
/*     */     throws XAException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXAConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */