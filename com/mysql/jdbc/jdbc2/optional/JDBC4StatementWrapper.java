/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBC4StatementWrapper
/*     */   extends StatementWrapper
/*     */ {
/*     */   public JDBC4StatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, Statement toWrap)
/*     */   {
/*  58 */     super(c, conn, toWrap);
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/*     */     try {
/*  63 */       super.close();
/*     */     } finally {
/*  65 */       this.unwrappedInterfaces = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*     */     try {
/*  71 */       if (this.wrappedStmt != null) {
/*  72 */         return this.wrappedStmt.isClosed();
/*     */       }
/*  74 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/*  78 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public void setPoolable(boolean poolable) throws SQLException {
/*     */     try {
/*  86 */       if (this.wrappedStmt != null) {
/*  87 */         this.wrappedStmt.setPoolable(poolable);
/*     */       } else {
/*  89 */         throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/*  93 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isPoolable() throws SQLException {
/*     */     try {
/*  99 */       if (this.wrappedStmt != null) {
/* 100 */         return this.wrappedStmt.isPoolable();
/*     */       }
/* 102 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 106 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 109 */     return false;
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 136 */     boolean isInstance = iface.isInstance(this);
/*     */     
/* 138 */     if (isInstance) {
/* 139 */       return true;
/*     */     }
/*     */     
/* 142 */     String interfaceClassName = iface.getName();
/*     */     
/* 144 */     return (interfaceClassName.equals("com.mysql.jdbc.Statement")) || (interfaceClassName.equals("java.sql.Statement")) || (interfaceClassName.equals("java.sql.Wrapper"));
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
/*     */   public synchronized <T> T unwrap(Class<T> iface)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 171 */       if (("java.sql.Statement".equals(iface.getName())) || ("java.sql.Wrapper.class".equals(iface.getName())))
/*     */       {
/* 173 */         return (T)iface.cast(this);
/*     */       }
/*     */       
/* 176 */       if (this.unwrappedInterfaces == null) {
/* 177 */         this.unwrappedInterfaces = new HashMap();
/*     */       }
/*     */       
/* 180 */       Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
/*     */       
/* 182 */       if (cachedUnwrapped == null) {
/* 183 */         cachedUnwrapped = Proxy.newProxyInstance(this.wrappedStmt.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.wrappedStmt));
/*     */         
/*     */ 
/*     */ 
/* 187 */         this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */       }
/*     */       
/* 190 */       return (T)iface.cast(cachedUnwrapped);
/*     */     } catch (ClassCastException cce) {
/* 192 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\JDBC4StatementWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */