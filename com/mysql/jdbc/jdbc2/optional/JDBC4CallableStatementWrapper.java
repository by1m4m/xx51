/*      */ package com.mysql.jdbc.jdbc2.optional;
/*      */ 
/*      */ import com.mysql.jdbc.SQLError;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.NClob;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Statement;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JDBC4CallableStatementWrapper
/*      */   extends CallableStatementWrapper
/*      */ {
/*      */   public JDBC4CallableStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, CallableStatement toWrap)
/*      */   {
/*   60 */     super(c, conn, toWrap);
/*      */   }
/*      */   
/*      */   public void close() throws SQLException {
/*      */     try {
/*   65 */       super.close();
/*      */     } finally {
/*   67 */       this.unwrappedInterfaces = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isClosed() throws SQLException {
/*      */     try {
/*   73 */       if (this.wrappedStmt != null) {
/*   74 */         return this.wrappedStmt.isClosed();
/*      */       }
/*   76 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*   80 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*   83 */     return false;
/*      */   }
/*      */   
/*      */   public void setPoolable(boolean poolable) throws SQLException {
/*      */     try {
/*   88 */       if (this.wrappedStmt != null) {
/*   89 */         this.wrappedStmt.setPoolable(poolable);
/*      */       } else {
/*   91 */         throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx) {
/*   95 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isPoolable() throws SQLException {
/*      */     try {
/*  101 */       if (this.wrappedStmt != null) {
/*  102 */         return this.wrappedStmt.isPoolable();
/*      */       }
/*  104 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  108 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  111 */     return false;
/*      */   }
/*      */   
/*      */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/*      */     try {
/*  116 */       if (this.wrappedStmt != null) {
/*  117 */         ((PreparedStatement)this.wrappedStmt).setRowId(parameterIndex, x);
/*      */       }
/*      */       else {
/*  120 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  125 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/*      */     try {
/*  131 */       if (this.wrappedStmt != null) {
/*  132 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, value);
/*      */       }
/*      */       else {
/*  135 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  140 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
/*      */   {
/*      */     try {
/*  147 */       if (this.wrappedStmt != null) {
/*  148 */         ((PreparedStatement)this.wrappedStmt).setSQLXML(parameterIndex, xmlObject);
/*      */       }
/*      */       else {
/*  151 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  156 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNString(int parameterIndex, String value)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  165 */       if (this.wrappedStmt != null) {
/*  166 */         ((PreparedStatement)this.wrappedStmt).setNString(parameterIndex, value);
/*      */       }
/*      */       else {
/*  169 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  174 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  183 */       if (this.wrappedStmt != null) {
/*  184 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value, length);
/*      */       }
/*      */       else {
/*  187 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  192 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  201 */       if (this.wrappedStmt != null) {
/*  202 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader, length);
/*      */       }
/*      */       else {
/*  205 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  210 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  219 */       if (this.wrappedStmt != null) {
/*  220 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream, length);
/*      */       }
/*      */       else {
/*  223 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  228 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  237 */       if (this.wrappedStmt != null) {
/*  238 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader, length);
/*      */       }
/*      */       else {
/*  241 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  246 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  255 */       if (this.wrappedStmt != null) {
/*  256 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
/*      */       }
/*      */       else {
/*  259 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  264 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  273 */       if (this.wrappedStmt != null) {
/*  274 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
/*      */       }
/*      */       else {
/*  277 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  282 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, long length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  291 */       if (this.wrappedStmt != null) {
/*  292 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
/*      */       }
/*      */       else {
/*  295 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  300 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  308 */       if (this.wrappedStmt != null) {
/*  309 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x);
/*      */       }
/*      */       else {
/*  312 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  317 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  325 */       if (this.wrappedStmt != null) {
/*  326 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x);
/*      */       }
/*      */       else {
/*  329 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  334 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  342 */       if (this.wrappedStmt != null) {
/*  343 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader);
/*      */       }
/*      */       else {
/*  346 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  351 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  360 */       if (this.wrappedStmt != null) {
/*  361 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value);
/*      */       }
/*      */       else {
/*  364 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  369 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  378 */       if (this.wrappedStmt != null) {
/*  379 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader);
/*      */       }
/*      */       else {
/*  382 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  387 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  396 */       if (this.wrappedStmt != null) {
/*  397 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream);
/*      */       }
/*      */       else {
/*  400 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  405 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  413 */       if (this.wrappedStmt != null) {
/*  414 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader);
/*      */       }
/*      */       else {
/*  417 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  422 */       checkAndFireConnectionError(sqlEx);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> iface)
/*      */     throws SQLException
/*      */   {
/*  450 */     boolean isInstance = iface.isInstance(this);
/*      */     
/*  452 */     if (isInstance) {
/*  453 */       return true;
/*      */     }
/*      */     
/*  456 */     String interfaceClassName = iface.getName();
/*      */     
/*  458 */     return (interfaceClassName.equals("com.mysql.jdbc.Statement")) || (interfaceClassName.equals("java.sql.Statement")) || (interfaceClassName.equals("java.sql.PreparedStatement")) || (interfaceClassName.equals("java.sql.Wrapper"));
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
/*      */   public synchronized <T> T unwrap(Class<T> iface)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  486 */       if (("java.sql.Statement".equals(iface.getName())) || ("java.sql.PreparedStatement".equals(iface.getName())) || ("java.sql.Wrapper.class".equals(iface.getName())))
/*      */       {
/*      */ 
/*  489 */         return (T)iface.cast(this);
/*      */       }
/*      */       
/*  492 */       if (this.unwrappedInterfaces == null) {
/*  493 */         this.unwrappedInterfaces = new HashMap();
/*      */       }
/*      */       
/*  496 */       Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
/*      */       
/*  498 */       if (cachedUnwrapped == null) {
/*  499 */         if (cachedUnwrapped == null) {
/*  500 */           cachedUnwrapped = Proxy.newProxyInstance(this.wrappedStmt.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.wrappedStmt));
/*      */           
/*      */ 
/*      */ 
/*  504 */           this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*      */         }
/*  506 */         this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*      */       }
/*      */       
/*  509 */       return (T)iface.cast(cachedUnwrapped);
/*      */     } catch (ClassCastException cce) {
/*  511 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setRowId(String parameterName, RowId x) throws SQLException
/*      */   {
/*      */     try {
/*  518 */       if (this.wrappedStmt != null) {
/*  519 */         ((CallableStatement)this.wrappedStmt).setRowId(parameterName, x);
/*      */       } else {
/*  521 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  526 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
/*      */     try {
/*  532 */       if (this.wrappedStmt != null) {
/*  533 */         ((CallableStatement)this.wrappedStmt).setSQLXML(parameterName, xmlObject);
/*      */       } else {
/*  535 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  540 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public SQLXML getSQLXML(int parameterIndex) throws SQLException {
/*      */     try {
/*  546 */       if (this.wrappedStmt != null) {
/*  547 */         return ((CallableStatement)this.wrappedStmt).getSQLXML(parameterIndex);
/*      */       }
/*  549 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  554 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  557 */     return null;
/*      */   }
/*      */   
/*      */   public SQLXML getSQLXML(String parameterName) throws SQLException
/*      */   {
/*      */     try {
/*  563 */       if (this.wrappedStmt != null) {
/*  564 */         return ((CallableStatement)this.wrappedStmt).getSQLXML(parameterName);
/*      */       }
/*  566 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  571 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  574 */     return null;
/*      */   }
/*      */   
/*      */   public RowId getRowId(String parameterName) throws SQLException {
/*      */     try {
/*  579 */       if (this.wrappedStmt != null) {
/*  580 */         return ((CallableStatement)this.wrappedStmt).getRowId(parameterName);
/*      */       }
/*  582 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  587 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  590 */     return null;
/*      */   }
/*      */   
/*      */   public void setNClob(String parameterName, NClob value) throws SQLException {
/*      */     try {
/*  595 */       if (this.wrappedStmt != null) {
/*  596 */         ((CallableStatement)this.wrappedStmt).setNClob(parameterName, value);
/*      */       } else {
/*  598 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  603 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(String parameterName, Reader reader) throws SQLException {
/*      */     try {
/*  609 */       if (this.wrappedStmt != null) {
/*  610 */         ((CallableStatement)this.wrappedStmt).setNClob(parameterName, reader);
/*      */       } else {
/*  612 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  617 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
/*      */     try {
/*  623 */       if (this.wrappedStmt != null) {
/*  624 */         ((CallableStatement)this.wrappedStmt).setNClob(parameterName, reader, length);
/*      */       } else {
/*  626 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  631 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNString(String parameterName, String value) throws SQLException {
/*      */     try {
/*  637 */       if (this.wrappedStmt != null) {
/*  638 */         ((CallableStatement)this.wrappedStmt).setNString(parameterName, value);
/*      */       } else {
/*  640 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  645 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public Reader getCharacterStream(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  654 */       if (this.wrappedStmt != null) {
/*  655 */         return ((CallableStatement)this.wrappedStmt).getCharacterStream(parameterIndex);
/*      */       }
/*  657 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  662 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  665 */     return null;
/*      */   }
/*      */   
/*      */   public Reader getCharacterStream(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  673 */       if (this.wrappedStmt != null) {
/*  674 */         return ((CallableStatement)this.wrappedStmt).getCharacterStream(parameterName);
/*      */       }
/*  676 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  681 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  684 */     return null;
/*      */   }
/*      */   
/*      */   public Reader getNCharacterStream(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  692 */       if (this.wrappedStmt != null) {
/*  693 */         return ((CallableStatement)this.wrappedStmt).getNCharacterStream(parameterIndex);
/*      */       }
/*  695 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  700 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  703 */     return null;
/*      */   }
/*      */   
/*      */   public Reader getNCharacterStream(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  711 */       if (this.wrappedStmt != null) {
/*  712 */         return ((CallableStatement)this.wrappedStmt).getNCharacterStream(parameterName);
/*      */       }
/*  714 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  719 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  722 */     return null;
/*      */   }
/*      */   
/*      */   public NClob getNClob(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  730 */       if (this.wrappedStmt != null) {
/*  731 */         return ((CallableStatement)this.wrappedStmt).getNClob(parameterName);
/*      */       }
/*  733 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  738 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  741 */     return null;
/*      */   }
/*      */   
/*      */   public String getNString(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  749 */       if (this.wrappedStmt != null) {
/*  750 */         return ((CallableStatement)this.wrappedStmt).getNString(parameterName);
/*      */       }
/*  752 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  757 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  760 */     return null;
/*      */   }
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
/*      */     try {
/*  765 */       if (this.wrappedStmt != null) {
/*  766 */         ((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x);
/*      */       } else {
/*  768 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  773 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
/*      */     try {
/*  779 */       if (this.wrappedStmt != null) {
/*  780 */         ((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x, length);
/*      */       } else {
/*  782 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  787 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
/*      */     try {
/*  793 */       if (this.wrappedStmt != null) {
/*  794 */         ((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x);
/*      */       } else {
/*  796 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  801 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
/*      */     try {
/*  807 */       if (this.wrappedStmt != null) {
/*  808 */         ((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x, length);
/*      */       } else {
/*  810 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  815 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(String parameterName, InputStream x) throws SQLException {
/*      */     try {
/*  821 */       if (this.wrappedStmt != null) {
/*  822 */         ((CallableStatement)this.wrappedStmt).setBlob(parameterName, x);
/*      */       } else {
/*  824 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  829 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(String parameterName, InputStream x, long length) throws SQLException {
/*      */     try {
/*  835 */       if (this.wrappedStmt != null) {
/*  836 */         ((CallableStatement)this.wrappedStmt).setBlob(parameterName, x, length);
/*      */       } else {
/*  838 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  843 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setBlob(String parameterName, Blob x) throws SQLException {
/*      */     try {
/*  849 */       if (this.wrappedStmt != null) {
/*  850 */         ((CallableStatement)this.wrappedStmt).setBlob(parameterName, x);
/*      */       } else {
/*  852 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  857 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
/*      */     try {
/*  863 */       if (this.wrappedStmt != null) {
/*  864 */         ((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader);
/*      */       } else {
/*  866 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  871 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
/*      */     try {
/*  877 */       if (this.wrappedStmt != null) {
/*  878 */         ((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader, length);
/*      */       } else {
/*  880 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  885 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setClob(String parameterName, Clob x) throws SQLException {
/*      */     try {
/*  891 */       if (this.wrappedStmt != null) {
/*  892 */         ((CallableStatement)this.wrappedStmt).setClob(parameterName, x);
/*      */       } else {
/*  894 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  899 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setClob(String parameterName, Reader reader) throws SQLException {
/*      */     try {
/*  905 */       if (this.wrappedStmt != null) {
/*  906 */         ((CallableStatement)this.wrappedStmt).setClob(parameterName, reader);
/*      */       } else {
/*  908 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  913 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setClob(String parameterName, Reader reader, long length) throws SQLException {
/*      */     try {
/*  919 */       if (this.wrappedStmt != null) {
/*  920 */         ((CallableStatement)this.wrappedStmt).setClob(parameterName, reader, length);
/*      */       } else {
/*  922 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  927 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader reader) throws SQLException {
/*      */     try {
/*  933 */       if (this.wrappedStmt != null) {
/*  934 */         ((CallableStatement)this.wrappedStmt).setNCharacterStream(parameterName, reader);
/*      */       } else {
/*  936 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  941 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
/*      */     try {
/*  947 */       if (this.wrappedStmt != null) {
/*  948 */         ((CallableStatement)this.wrappedStmt).setNCharacterStream(parameterName, reader, length);
/*      */       } else {
/*  950 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  955 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */   public NClob getNClob(int parameterIndex) throws SQLException {
/*      */     try {
/*  961 */       if (this.wrappedStmt != null) {
/*  962 */         return ((CallableStatement)this.wrappedStmt).getNClob(parameterIndex);
/*      */       }
/*  964 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  969 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  972 */     return null;
/*      */   }
/*      */   
/*      */   public String getNString(int parameterIndex) throws SQLException {
/*      */     try {
/*  977 */       if (this.wrappedStmt != null) {
/*  978 */         return ((CallableStatement)this.wrappedStmt).getNString(parameterIndex);
/*      */       }
/*  980 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  985 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  988 */     return null;
/*      */   }
/*      */   
/*      */   public RowId getRowId(int parameterIndex) throws SQLException {
/*      */     try {
/*  993 */       if (this.wrappedStmt != null) {
/*  994 */         return ((CallableStatement)this.wrappedStmt).getRowId(parameterIndex);
/*      */       }
/*  996 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1001 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1004 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\JDBC4CallableStatementWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */