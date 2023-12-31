/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ResultSetInternalMethods;
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import com.mysql.jdbc.Util;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PreparedStatementWrapper
/*     */   extends StatementWrapper
/*     */   implements java.sql.PreparedStatement
/*     */ {
/*     */   private static final Constructor<?> JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR;
/*     */   
/*     */   static
/*     */   {
/*  64 */     if (Util.isJdbc4()) {
/*     */       try {
/*  66 */         JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4PreparedStatementWrapper").getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, java.sql.PreparedStatement.class });
/*     */ 
/*     */       }
/*     */       catch (SecurityException e)
/*     */       {
/*     */ 
/*  72 */         throw new RuntimeException(e);
/*     */       } catch (NoSuchMethodException e) {
/*  74 */         throw new RuntimeException(e);
/*     */       } catch (ClassNotFoundException e) {
/*  76 */         throw new RuntimeException(e);
/*     */       }
/*     */     } else {
/*  79 */       JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static PreparedStatementWrapper getInstance(ConnectionWrapper c, MysqlPooledConnection conn, java.sql.PreparedStatement toWrap)
/*     */     throws SQLException
/*     */   {
/*  86 */     if (!Util.isJdbc4()) {
/*  87 */       return new PreparedStatementWrapper(c, conn, toWrap);
/*     */     }
/*     */     
/*     */ 
/*  91 */     return (PreparedStatementWrapper)Util.handleNewInstance(JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR, new Object[] { c, conn, toWrap }, conn.getExceptionInterceptor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   PreparedStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, java.sql.PreparedStatement toWrap)
/*     */   {
/*  99 */     super(c, conn, toWrap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setArray(int parameterIndex, Array x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       if (this.wrappedStmt != null) {
/* 110 */         ((java.sql.PreparedStatement)this.wrappedStmt).setArray(parameterIndex, x);
/*     */       }
/*     */       else {
/* 113 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 118 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsciiStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 131 */       if (this.wrappedStmt != null) {
/* 132 */         ((java.sql.PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 135 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 140 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBigDecimal(int parameterIndex, BigDecimal x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 152 */       if (this.wrappedStmt != null) {
/* 153 */         ((java.sql.PreparedStatement)this.wrappedStmt).setBigDecimal(parameterIndex, x);
/*     */       }
/*     */       else {
/* 156 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 161 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBinaryStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 174 */       if (this.wrappedStmt != null) {
/* 175 */         ((java.sql.PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 178 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 183 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBlob(int parameterIndex, Blob x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 194 */       if (this.wrappedStmt != null) {
/* 195 */         ((java.sql.PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, x);
/*     */       }
/*     */       else {
/* 198 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 203 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBoolean(int parameterIndex, boolean x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 214 */       if (this.wrappedStmt != null) {
/* 215 */         ((java.sql.PreparedStatement)this.wrappedStmt).setBoolean(parameterIndex, x);
/*     */       }
/*     */       else {
/* 218 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 223 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setByte(int parameterIndex, byte x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 234 */       if (this.wrappedStmt != null) {
/* 235 */         ((java.sql.PreparedStatement)this.wrappedStmt).setByte(parameterIndex, x);
/*     */       }
/*     */       else {
/* 238 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 243 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBytes(int parameterIndex, byte[] x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 254 */       if (this.wrappedStmt != null) {
/* 255 */         ((java.sql.PreparedStatement)this.wrappedStmt).setBytes(parameterIndex, x);
/*     */       }
/*     */       else {
/* 258 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 263 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterStream(int parameterIndex, Reader reader, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 276 */       if (this.wrappedStmt != null) {
/* 277 */         ((java.sql.PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
/*     */       }
/*     */       else {
/* 280 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 285 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setClob(int parameterIndex, Clob x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 296 */       if (this.wrappedStmt != null) {
/* 297 */         ((java.sql.PreparedStatement)this.wrappedStmt).setClob(parameterIndex, x);
/*     */       }
/*     */       else {
/* 300 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 305 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDate(int parameterIndex, Date x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 316 */       if (this.wrappedStmt != null) {
/* 317 */         ((java.sql.PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x);
/*     */       }
/*     */       else {
/* 320 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 325 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDate(int parameterIndex, Date x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 338 */       if (this.wrappedStmt != null) {
/* 339 */         ((java.sql.PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 342 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 347 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDouble(int parameterIndex, double x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 358 */       if (this.wrappedStmt != null) {
/* 359 */         ((java.sql.PreparedStatement)this.wrappedStmt).setDouble(parameterIndex, x);
/*     */       }
/*     */       else {
/* 362 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 367 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFloat(int parameterIndex, float x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 378 */       if (this.wrappedStmt != null) {
/* 379 */         ((java.sql.PreparedStatement)this.wrappedStmt).setFloat(parameterIndex, x);
/*     */       }
/*     */       else {
/* 382 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 387 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setInt(int parameterIndex, int x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 398 */       if (this.wrappedStmt != null) {
/* 399 */         ((java.sql.PreparedStatement)this.wrappedStmt).setInt(parameterIndex, x);
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
/*     */ 
/*     */ 
/*     */   public void setLong(int parameterIndex, long x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 418 */       if (this.wrappedStmt != null) {
/* 419 */         ((java.sql.PreparedStatement)this.wrappedStmt).setLong(parameterIndex, x);
/*     */       }
/*     */       else {
/* 422 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 427 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSetMetaData getMetaData()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 438 */       if (this.wrappedStmt != null) {
/* 439 */         return ((java.sql.PreparedStatement)this.wrappedStmt).getMetaData();
/*     */       }
/*     */       
/* 442 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 446 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 449 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setNull(int parameterIndex, int sqlType)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 459 */       if (this.wrappedStmt != null) {
/* 460 */         ((java.sql.PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType);
/*     */       }
/*     */       else {
/* 463 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 468 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNull(int parameterIndex, int sqlType, String typeName)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 480 */       if (this.wrappedStmt != null) {
/* 481 */         ((java.sql.PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType, typeName);
/*     */       }
/*     */       else {
/* 484 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 489 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 500 */       if (this.wrappedStmt != null) {
/* 501 */         ((java.sql.PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x);
/*     */       }
/*     */       else {
/* 504 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 509 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x, int targetSqlType)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 521 */       if (this.wrappedStmt != null) {
/* 522 */         ((java.sql.PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType);
/*     */       }
/*     */       else {
/* 525 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 530 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 543 */       if (this.wrappedStmt != null) {
/* 544 */         ((java.sql.PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType, scale);
/*     */       }
/*     */       else {
/* 547 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 552 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ParameterMetaData getParameterMetaData()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 563 */       if (this.wrappedStmt != null) {
/* 564 */         return ((java.sql.PreparedStatement)this.wrappedStmt).getParameterMetaData();
/*     */       }
/*     */       
/*     */ 
/* 568 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 572 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 575 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRef(int parameterIndex, Ref x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 585 */       if (this.wrappedStmt != null) {
/* 586 */         ((java.sql.PreparedStatement)this.wrappedStmt).setRef(parameterIndex, x);
/*     */       }
/*     */       else {
/* 589 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 594 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setShort(int parameterIndex, short x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 605 */       if (this.wrappedStmt != null) {
/* 606 */         ((java.sql.PreparedStatement)this.wrappedStmt).setShort(parameterIndex, x);
/*     */       }
/*     */       else {
/* 609 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 614 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setString(int parameterIndex, String x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 625 */       if (this.wrappedStmt != null) {
/* 626 */         ((java.sql.PreparedStatement)this.wrappedStmt).setString(parameterIndex, x);
/*     */       }
/*     */       else {
/* 629 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 634 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTime(int parameterIndex, Time x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 645 */       if (this.wrappedStmt != null) {
/* 646 */         ((java.sql.PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x);
/*     */       }
/*     */       else {
/* 649 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 654 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTime(int parameterIndex, Time x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 667 */       if (this.wrappedStmt != null) {
/* 668 */         ((java.sql.PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 671 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 676 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestamp(int parameterIndex, Timestamp x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 688 */       if (this.wrappedStmt != null) {
/* 689 */         ((java.sql.PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x);
/*     */       }
/*     */       else {
/* 692 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 697 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 710 */       if (this.wrappedStmt != null) {
/* 711 */         ((java.sql.PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 714 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 719 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setURL(int parameterIndex, URL x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 730 */       if (this.wrappedStmt != null) {
/* 731 */         ((java.sql.PreparedStatement)this.wrappedStmt).setURL(parameterIndex, x);
/*     */       }
/*     */       else {
/* 734 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 739 */       checkAndFireConnectionError(sqlEx);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void setUnicodeStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 763 */       if (this.wrappedStmt != null) {
/* 764 */         ((java.sql.PreparedStatement)this.wrappedStmt).setUnicodeStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 767 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 772 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBatch()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 783 */       if (this.wrappedStmt != null) {
/* 784 */         ((java.sql.PreparedStatement)this.wrappedStmt).addBatch();
/*     */       } else {
/* 786 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 791 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearParameters()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 802 */       if (this.wrappedStmt != null) {
/* 803 */         ((java.sql.PreparedStatement)this.wrappedStmt).clearParameters();
/*     */       } else {
/* 805 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 810 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean execute()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 821 */       if (this.wrappedStmt != null) {
/* 822 */         return ((java.sql.PreparedStatement)this.wrappedStmt).execute();
/*     */       }
/*     */       
/* 825 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 829 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 832 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSet executeQuery()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 845 */       if (this.wrappedStmt != null) {
/* 846 */         ResultSet rs = ((java.sql.PreparedStatement)this.wrappedStmt).executeQuery();
/*     */         
/*     */ 
/* 849 */         ((ResultSetInternalMethods)rs).setWrapperStatement(this);
/*     */         
/* 851 */         return rs;
/*     */       }
/*     */       
/* 854 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 858 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 861 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 874 */       if (this.wrappedStmt != null) {
/* 875 */         return ((java.sql.PreparedStatement)this.wrappedStmt).executeUpdate();
/*     */       }
/*     */       
/* 878 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 882 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 885 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 891 */     StringBuffer buf = new StringBuffer();
/* 892 */     buf.append(super.toString());
/*     */     
/* 894 */     if (this.wrappedStmt != null) {
/* 895 */       buf.append(": ");
/*     */       try {
/* 897 */         buf.append(((com.mysql.jdbc.PreparedStatement)this.wrappedStmt).asSql());
/*     */       } catch (SQLException sqlEx) {
/* 899 */         buf.append("EXCEPTION: " + sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/* 903 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\PreparedStatementWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */