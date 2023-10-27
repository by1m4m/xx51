/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.Array;
/*     */ import java.sql.NClob;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Struct;
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
/*     */ public class JDBC4Connection
/*     */   extends ConnectionImpl
/*     */ {
/*     */   private JDBC4ClientInfoProvider infoProvider;
/*     */   
/*     */   public JDBC4Connection(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url)
/*     */     throws SQLException
/*     */   {
/*  46 */     super(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
/*     */   }
/*     */   
/*     */   public SQLXML createSQLXML() throws SQLException
/*     */   {
/*  51 */     return new JDBC4MysqlSQLXML(getExceptionInterceptor());
/*     */   }
/*     */   
/*     */   public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
/*  55 */     throw SQLError.notImplemented();
/*     */   }
/*     */   
/*     */   public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
/*  59 */     throw SQLError.notImplemented();
/*     */   }
/*     */   
/*     */   public Properties getClientInfo() throws SQLException {
/*  63 */     return getClientInfoProviderImpl().getClientInfo(this);
/*     */   }
/*     */   
/*     */   public String getClientInfo(String name) throws SQLException {
/*  67 */     return getClientInfoProviderImpl().getClientInfo(this, name);
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
/*     */   public boolean isValid(int timeout)
/*     */     throws SQLException
/*     */   {
/*  92 */     synchronized (getConnectionMutex()) {
/*  93 */       if (isClosed()) {
/*  94 */         return false;
/*     */       }
/*     */       try
/*     */       {
/*     */         try {
/*  99 */           pingInternal(false, timeout * 1000);
/*     */         } catch (Throwable t) {
/*     */           try {
/* 102 */             abortInternal();
/*     */           }
/*     */           catch (Throwable ignoreThrown) {}
/*     */           
/*     */ 
/* 107 */           return false;
/*     */         }
/*     */       }
/*     */       catch (Throwable t) {
/* 111 */         return false;
/*     */       }
/*     */       
/* 114 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClientInfo(Properties properties) throws SQLClientInfoException {
/*     */     try {
/* 120 */       getClientInfoProviderImpl().setClientInfo(this, properties);
/*     */     } catch (SQLClientInfoException ciEx) {
/* 122 */       throw ciEx;
/*     */     } catch (SQLException sqlEx) {
/* 124 */       SQLClientInfoException clientInfoEx = new SQLClientInfoException();
/* 125 */       clientInfoEx.initCause(sqlEx);
/*     */       
/* 127 */       throw clientInfoEx;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClientInfo(String name, String value) throws SQLClientInfoException {
/*     */     try {
/* 133 */       getClientInfoProviderImpl().setClientInfo(this, name, value);
/*     */     } catch (SQLClientInfoException ciEx) {
/* 135 */       throw ciEx;
/*     */     } catch (SQLException sqlEx) {
/* 137 */       SQLClientInfoException clientInfoEx = new SQLClientInfoException();
/* 138 */       clientInfoEx.initCause(sqlEx);
/*     */       
/* 140 */       throw clientInfoEx;
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 160 */     checkClosed();
/*     */     
/*     */ 
/*     */ 
/* 164 */     return iface.isInstance(this);
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
/*     */   public <T> T unwrap(Class<T> iface)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 185 */       return (T)iface.cast(this);
/*     */     } catch (ClassCastException cce) {
/* 187 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", getExceptionInterceptor());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.Blob createBlob()
/*     */   {
/* 196 */     return new Blob(getExceptionInterceptor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public java.sql.Clob createClob()
/*     */   {
/* 203 */     return new Clob(getExceptionInterceptor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NClob createNClob()
/*     */   {
/* 210 */     return new JDBC4NClob(getExceptionInterceptor());
/*     */   }
/*     */   
/*     */   protected JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
/* 214 */     synchronized (getConnectionMutex()) {
/* 215 */       if (this.infoProvider == null) {
/*     */         try {
/*     */           try {
/* 218 */             this.infoProvider = ((JDBC4ClientInfoProvider)Util.getInstance(getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor()));
/*     */           }
/*     */           catch (SQLException sqlEx) {
/* 221 */             if ((sqlEx.getCause() instanceof ClassCastException))
/*     */             {
/* 223 */               this.infoProvider = ((JDBC4ClientInfoProvider)Util.getInstance("com.mysql.jdbc." + getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor()));
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (ClassCastException cce)
/*     */         {
/* 229 */           throw SQLError.createSQLException(Messages.getString("JDBC4Connection.ClientInfoNotImplemented", new Object[] { getClientInfoProvider() }), "S1009", getExceptionInterceptor());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 234 */         this.infoProvider.initialize(this, this.props);
/*     */       }
/*     */       
/* 237 */       return this.infoProvider;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */