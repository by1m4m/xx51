/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.NClob;
/*     */ import java.sql.SQLClientInfoException;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Struct;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class JDBC4ConnectionWrapper
/*     */   extends ConnectionWrapper
/*     */ {
/*     */   public JDBC4ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, com.mysql.jdbc.Connection mysqlConnection, boolean forXa)
/*     */     throws SQLException
/*     */   {
/*  67 */     super(mysqlPooledConnection, mysqlConnection, forXa);
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/*     */     try {
/*  72 */       super.close();
/*     */     } finally {
/*  74 */       this.unwrappedInterfaces = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public SQLXML createSQLXML() throws SQLException {
/*  79 */     checkClosed();
/*     */     try
/*     */     {
/*  82 */       return this.mc.createSQLXML();
/*     */     } catch (SQLException sqlException) {
/*  84 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public Array createArrayOf(String typeName, Object[] elements) throws SQLException
/*     */   {
/*  92 */     checkClosed();
/*     */     try
/*     */     {
/*  95 */       return this.mc.createArrayOf(typeName, elements);
/*     */     }
/*     */     catch (SQLException sqlException) {
/*  98 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public Struct createStruct(String typeName, Object[] attributes) throws SQLException
/*     */   {
/* 106 */     checkClosed();
/*     */     try
/*     */     {
/* 109 */       return this.mc.createStruct(typeName, attributes);
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 112 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public Properties getClientInfo() throws SQLException {
/* 119 */     checkClosed();
/*     */     try
/*     */     {
/* 122 */       return this.mc.getClientInfo();
/*     */     } catch (SQLException sqlException) {
/* 124 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 127 */     return null;
/*     */   }
/*     */   
/*     */   public String getClientInfo(String name) throws SQLException {
/* 131 */     checkClosed();
/*     */     try
/*     */     {
/* 134 */       return this.mc.getClientInfo(name);
/*     */     } catch (SQLException sqlException) {
/* 136 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 139 */     return null;
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
/*     */   public synchronized boolean isValid(int timeout)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 166 */       return this.mc.isValid(timeout);
/*     */     } catch (SQLException sqlException) {
/* 168 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public void setClientInfo(Properties properties) throws SQLClientInfoException
/*     */   {
/*     */     try {
/* 177 */       checkClosed();
/*     */       
/* 179 */       this.mc.setClientInfo(properties);
/*     */     } catch (SQLException sqlException) {
/*     */       try {
/* 182 */         checkAndFireConnectionError(sqlException);
/*     */       } catch (SQLException sqlEx2) {
/* 184 */         SQLClientInfoException clientEx = new SQLClientInfoException();
/* 185 */         clientEx.initCause(sqlEx2);
/*     */         
/* 187 */         throw clientEx;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClientInfo(String name, String value) throws SQLClientInfoException
/*     */   {
/*     */     try {
/* 195 */       checkClosed();
/*     */       
/* 197 */       this.mc.setClientInfo(name, value);
/*     */     } catch (SQLException sqlException) {
/*     */       try {
/* 200 */         checkAndFireConnectionError(sqlException);
/*     */       } catch (SQLException sqlEx2) {
/* 202 */         SQLClientInfoException clientEx = new SQLClientInfoException();
/* 203 */         clientEx.initCause(sqlEx2);
/*     */         
/* 205 */         throw clientEx;
/*     */       }
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 233 */     checkClosed();
/*     */     
/* 235 */     boolean isInstance = iface.isInstance(this);
/*     */     
/* 237 */     if (isInstance) {
/* 238 */       return true;
/*     */     }
/*     */     
/* 241 */     return (iface.getName().equals("com.mysql.jdbc.Connection")) || (iface.getName().equals("com.mysql.jdbc.ConnectionProperties"));
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
/*     */   public synchronized <T> T unwrap(Class<T> iface)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 267 */       if (("java.sql.Connection".equals(iface.getName())) || ("java.sql.Wrapper.class".equals(iface.getName())))
/*     */       {
/* 269 */         return (T)iface.cast(this);
/*     */       }
/*     */       
/* 272 */       if (this.unwrappedInterfaces == null) {
/* 273 */         this.unwrappedInterfaces = new HashMap();
/*     */       }
/*     */       
/* 276 */       Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
/*     */       
/* 278 */       if (cachedUnwrapped == null) {
/* 279 */         cachedUnwrapped = Proxy.newProxyInstance(this.mc.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.mc));
/*     */         
/*     */ 
/* 282 */         this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */       }
/*     */       
/* 285 */       return (T)iface.cast(cachedUnwrapped);
/*     */     } catch (ClassCastException cce) {
/* 287 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Blob createBlob()
/*     */     throws SQLException
/*     */   {
/* 296 */     checkClosed();
/*     */     try
/*     */     {
/* 299 */       return this.mc.createBlob();
/*     */     } catch (SQLException sqlException) {
/* 301 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 304 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Clob createClob()
/*     */     throws SQLException
/*     */   {
/* 311 */     checkClosed();
/*     */     try
/*     */     {
/* 314 */       return this.mc.createClob();
/*     */     } catch (SQLException sqlException) {
/* 316 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 319 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public NClob createNClob()
/*     */     throws SQLException
/*     */   {
/* 326 */     checkClosed();
/*     */     try
/*     */     {
/* 329 */       return this.mc.createNClob();
/*     */     } catch (SQLException sqlException) {
/* 331 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 334 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\JDBC4ConnectionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */