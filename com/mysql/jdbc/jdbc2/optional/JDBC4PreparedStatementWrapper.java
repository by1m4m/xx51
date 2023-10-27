/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.StatementEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBC4PreparedStatementWrapper
/*     */   extends PreparedStatementWrapper
/*     */ {
/*     */   public JDBC4PreparedStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, PreparedStatement toWrap)
/*     */   {
/*  60 */     super(c, conn, toWrap);
/*     */   }
/*     */   
/*     */   public synchronized void close() throws SQLException {
/*  64 */     if (this.pooledConnection == null)
/*     */     {
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     MysqlPooledConnection con = this.pooledConnection;
/*     */     
/*     */     try
/*     */     {
/*  73 */       super.close();
/*     */     } finally {
/*     */       try { StatementEvent e;
/*  76 */         StatementEvent e = new StatementEvent(con, this);
/*     */         
/*  78 */         if ((con instanceof JDBC4MysqlPooledConnection)) {
/*  79 */           ((JDBC4MysqlPooledConnection)con).fireStatementEvent(e);
/*  80 */         } else if ((con instanceof JDBC4MysqlXAConnection)) {
/*  81 */           ((JDBC4MysqlXAConnection)con).fireStatementEvent(e);
/*  82 */         } else if ((con instanceof JDBC4SuspendableXAConnection)) {
/*  83 */           ((JDBC4SuspendableXAConnection)con).fireStatementEvent(e);
/*     */         }
/*     */       } finally {
/*  86 */         this.unwrappedInterfaces = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*     */     try {
/*  93 */       if (this.wrappedStmt != null) {
/*  94 */         return this.wrappedStmt.isClosed();
/*     */       }
/*  96 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 100 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public void setPoolable(boolean poolable) throws SQLException {
/*     */     try {
/* 108 */       if (this.wrappedStmt != null) {
/* 109 */         this.wrappedStmt.setPoolable(poolable);
/*     */       } else {
/* 111 */         throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 115 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isPoolable() throws SQLException {
/*     */     try {
/* 121 */       if (this.wrappedStmt != null) {
/* 122 */         return this.wrappedStmt.isPoolable();
/*     */       }
/* 124 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 128 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/*     */     try {
/* 136 */       if (this.wrappedStmt != null) {
/* 137 */         ((PreparedStatement)this.wrappedStmt).setRowId(parameterIndex, x);
/*     */       }
/*     */       else {
/* 140 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 145 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/*     */     try {
/* 151 */       if (this.wrappedStmt != null) {
/* 152 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, value);
/*     */       }
/*     */       else {
/* 155 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 160 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
/*     */   {
/*     */     try {
/* 167 */       if (this.wrappedStmt != null) {
/* 168 */         ((PreparedStatement)this.wrappedStmt).setSQLXML(parameterIndex, xmlObject);
/*     */       }
/*     */       else {
/* 171 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 176 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNString(int parameterIndex, String value)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 185 */       if (this.wrappedStmt != null) {
/* 186 */         ((PreparedStatement)this.wrappedStmt).setNString(parameterIndex, value);
/*     */       }
/*     */       else {
/* 189 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 194 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 203 */       if (this.wrappedStmt != null) {
/* 204 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value, length);
/*     */       }
/*     */       else {
/* 207 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 212 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 221 */       if (this.wrappedStmt != null) {
/* 222 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader, length);
/*     */       }
/*     */       else {
/* 225 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 230 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 239 */       if (this.wrappedStmt != null) {
/* 240 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream, length);
/*     */       }
/*     */       else {
/* 243 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 248 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 257 */       if (this.wrappedStmt != null) {
/* 258 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader, length);
/*     */       }
/*     */       else {
/* 261 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 266 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 275 */       if (this.wrappedStmt != null) {
/* 276 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 279 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 284 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 293 */       if (this.wrappedStmt != null) {
/* 294 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 297 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 302 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader, long length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 311 */       if (this.wrappedStmt != null) {
/* 312 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
/*     */       }
/*     */       else {
/* 315 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 320 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 328 */       if (this.wrappedStmt != null) {
/* 329 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x);
/*     */       }
/*     */       else {
/* 332 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 337 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 345 */       if (this.wrappedStmt != null) {
/* 346 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x);
/*     */       }
/*     */       else {
/* 349 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 354 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 362 */       if (this.wrappedStmt != null) {
/* 363 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader);
/*     */       }
/*     */       else {
/* 366 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 371 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 380 */       if (this.wrappedStmt != null) {
/* 381 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value);
/*     */       }
/*     */       else {
/* 384 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 389 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 398 */       if (this.wrappedStmt != null) {
/* 399 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader);
/*     */       }
/*     */       else {
/* 402 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 407 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 416 */       if (this.wrappedStmt != null) {
/* 417 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream);
/*     */       }
/*     */       else {
/* 420 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 425 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader) throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 433 */       if (this.wrappedStmt != null) {
/* 434 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader);
/*     */       }
/*     */       else {
/* 437 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 442 */       checkAndFireConnectionError(sqlEx);
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 470 */     boolean isInstance = iface.isInstance(this);
/*     */     
/* 472 */     if (isInstance) {
/* 473 */       return true;
/*     */     }
/*     */     
/* 476 */     String interfaceClassName = iface.getName();
/*     */     
/* 478 */     return (interfaceClassName.equals("com.mysql.jdbc.Statement")) || (interfaceClassName.equals("java.sql.Statement")) || (interfaceClassName.equals("java.sql.PreparedStatement")) || (interfaceClassName.equals("java.sql.Wrapper"));
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
/*     */   public synchronized <T> T unwrap(Class<T> iface)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 506 */       if (("java.sql.Statement".equals(iface.getName())) || ("java.sql.PreparedStatement".equals(iface.getName())) || ("java.sql.Wrapper.class".equals(iface.getName())))
/*     */       {
/*     */ 
/* 509 */         return (T)iface.cast(this);
/*     */       }
/*     */       
/* 512 */       if (this.unwrappedInterfaces == null) {
/* 513 */         this.unwrappedInterfaces = new HashMap();
/*     */       }
/*     */       
/* 516 */       Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
/*     */       
/* 518 */       if (cachedUnwrapped == null) {
/* 519 */         if (cachedUnwrapped == null) {
/* 520 */           cachedUnwrapped = Proxy.newProxyInstance(this.wrappedStmt.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.wrappedStmt));
/*     */           
/*     */ 
/*     */ 
/* 524 */           this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */         }
/* 526 */         this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */       }
/*     */       
/* 529 */       return (T)iface.cast(cachedUnwrapped);
/*     */     } catch (ClassCastException cce) {
/* 531 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\JDBC4PreparedStatementWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */