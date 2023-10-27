/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.sql.NClob;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Struct;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBC4ResultSet
/*     */   extends ResultSetImpl
/*     */ {
/*     */   public JDBC4ResultSet(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt)
/*     */   {
/*  49 */     super(updateCount, updateID, conn, creatorStmt);
/*     */   }
/*     */   
/*     */   public JDBC4ResultSet(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException
/*     */   {
/*  54 */     super(catalog, fields, tuples, conn, creatorStmt);
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
/*     */   public Reader getNCharacterStream(int columnIndex)
/*     */     throws SQLException
/*     */   {
/*  73 */     checkColumnBounds(columnIndex);
/*     */     
/*  75 */     String fieldEncoding = this.fields[(columnIndex - 1)].getCharacterSet();
/*  76 */     if ((fieldEncoding == null) || (!fieldEncoding.equals("UTF-8"))) {
/*  77 */       throw new SQLException("Can not call getNCharacterStream() when field's charset isn't UTF-8");
/*     */     }
/*     */     
/*  80 */     return getCharacterStream(columnIndex);
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
/*     */   public Reader getNCharacterStream(String columnName)
/*     */     throws SQLException
/*     */   {
/*  99 */     return getNCharacterStream(findColumn(columnName));
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
/*     */   public NClob getNClob(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 114 */     checkColumnBounds(columnIndex);
/*     */     
/* 116 */     String fieldEncoding = this.fields[(columnIndex - 1)].getCharacterSet();
/* 117 */     if ((fieldEncoding == null) || (!fieldEncoding.equals("UTF-8"))) {
/* 118 */       throw new SQLException("Can not call getNClob() when field's charset isn't UTF-8");
/*     */     }
/*     */     
/* 121 */     if (!this.isBinaryEncoded) {
/* 122 */       String asString = getStringForNClob(columnIndex);
/*     */       
/* 124 */       if (asString == null) {
/* 125 */         return null;
/*     */       }
/*     */       
/* 128 */       return new JDBC4NClob(asString, getExceptionInterceptor());
/*     */     }
/*     */     
/* 131 */     return getNativeNClob(columnIndex);
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
/*     */   public NClob getNClob(String columnName)
/*     */     throws SQLException
/*     */   {
/* 146 */     return getNClob(findColumn(columnName));
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
/*     */   protected NClob getNativeNClob(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 162 */     String stringVal = getStringForNClob(columnIndex);
/*     */     
/* 164 */     if (stringVal == null) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     return getNClobFromString(stringVal, columnIndex);
/*     */   }
/*     */   
/*     */   private String getStringForNClob(int columnIndex) throws SQLException {
/* 172 */     String asString = null;
/*     */     
/* 174 */     String forcedEncoding = "UTF-8";
/*     */     try
/*     */     {
/* 177 */       byte[] asBytes = null;
/*     */       
/* 179 */       if (!this.isBinaryEncoded) {
/* 180 */         asBytes = getBytes(columnIndex);
/*     */       } else {
/* 182 */         asBytes = getNativeBytes(columnIndex, true);
/*     */       }
/*     */       
/* 185 */       if (asBytes != null) {
/* 186 */         asString = new String(asBytes, forcedEncoding);
/*     */       }
/*     */     } catch (UnsupportedEncodingException uee) {
/* 189 */       throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*     */     }
/*     */     
/*     */ 
/* 193 */     return asString;
/*     */   }
/*     */   
/*     */   private final NClob getNClobFromString(String stringVal, int columnIndex) throws SQLException
/*     */   {
/* 198 */     return new JDBC4NClob(stringVal, getExceptionInterceptor());
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
/*     */   public String getNString(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 215 */     checkColumnBounds(columnIndex);
/*     */     
/* 217 */     String fieldEncoding = this.fields[(columnIndex - 1)].getCharacterSet();
/* 218 */     if ((fieldEncoding == null) || (!fieldEncoding.equals("UTF-8"))) {
/* 219 */       throw new SQLException("Can not call getNString() when field's charset isn't UTF-8");
/*     */     }
/*     */     
/* 222 */     return getString(columnIndex);
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
/*     */   public String getNString(String columnName)
/*     */     throws SQLException
/*     */   {
/* 240 */     return getNString(findColumn(columnName));
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
/*     */   public void updateNCharacterStream(int columnIndex, Reader x, int length)
/*     */     throws SQLException
/*     */   {
/* 264 */     throw new NotUpdatable();
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
/*     */   public void updateNCharacterStream(String columnName, Reader reader, int length)
/*     */     throws SQLException
/*     */   {
/* 286 */     updateNCharacterStream(findColumn(columnName), reader, length);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateNClob(String columnName, NClob nClob)
/*     */     throws SQLException
/*     */   {
/* 293 */     updateNClob(findColumn(columnName), nClob);
/*     */   }
/*     */   
/*     */   public void updateRowId(int columnIndex, RowId x) throws SQLException {
/* 297 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateRowId(String columnName, RowId x) throws SQLException {
/* 301 */     updateRowId(findColumn(columnName), x);
/*     */   }
/*     */   
/*     */   public int getHoldability() throws SQLException {
/* 305 */     throw SQLError.notImplemented();
/*     */   }
/*     */   
/*     */   public RowId getRowId(int columnIndex) throws SQLException {
/* 309 */     throw SQLError.notImplemented();
/*     */   }
/*     */   
/*     */   public RowId getRowId(String columnLabel) throws SQLException {
/* 313 */     return getRowId(findColumn(columnLabel));
/*     */   }
/*     */   
/*     */   public SQLXML getSQLXML(int columnIndex) throws SQLException {
/* 317 */     checkColumnBounds(columnIndex);
/*     */     
/* 319 */     return new JDBC4MysqlSQLXML(this, columnIndex, getExceptionInterceptor());
/*     */   }
/*     */   
/*     */   public SQLXML getSQLXML(String columnLabel) throws SQLException {
/* 323 */     return getSQLXML(findColumn(columnLabel));
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
/* 327 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException
/*     */   {
/* 332 */     updateAsciiStream(findColumn(columnLabel), x);
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException
/*     */   {
/* 337 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException
/*     */   {
/* 342 */     updateAsciiStream(findColumn(columnLabel), x, length);
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
/* 346 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException
/*     */   {
/* 351 */     updateBinaryStream(findColumn(columnLabel), x);
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
/* 355 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException
/*     */   {
/* 360 */     updateBinaryStream(findColumn(columnLabel), x, length);
/*     */   }
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
/* 364 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
/* 368 */     updateBlob(findColumn(columnLabel), inputStream);
/*     */   }
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 372 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException
/*     */   {
/* 377 */     updateBlob(findColumn(columnLabel), inputStream, length);
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 381 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException
/*     */   {
/* 386 */     updateCharacterStream(findColumn(columnLabel), reader);
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 390 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
/*     */   {
/* 395 */     updateCharacterStream(findColumn(columnLabel), reader, length);
/*     */   }
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader) throws SQLException {
/* 399 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader) throws SQLException
/*     */   {
/* 404 */     updateClob(findColumn(columnLabel), reader);
/*     */   }
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 408 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader, long length) throws SQLException
/*     */   {
/* 413 */     updateClob(findColumn(columnLabel), reader, length);
/*     */   }
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 417 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException
/*     */   {
/* 422 */     updateNCharacterStream(findColumn(columnLabel), reader);
/*     */   }
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException
/*     */   {
/* 427 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
/*     */   {
/* 432 */     updateNCharacterStream(findColumn(columnLabel), reader, length);
/*     */   }
/*     */   
/*     */   public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
/* 436 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader) throws SQLException
/*     */   {
/* 441 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader) throws SQLException
/*     */   {
/* 446 */     updateNClob(findColumn(columnLabel), reader);
/*     */   }
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException
/*     */   {
/* 451 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
/* 455 */     updateNClob(findColumn(columnLabel), reader, length);
/*     */   }
/*     */   
/*     */   public void updateNString(int columnIndex, String nString) throws SQLException {
/* 459 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateNString(String columnLabel, String nString) throws SQLException
/*     */   {
/* 464 */     updateNString(findColumn(columnLabel), nString);
/*     */   }
/*     */   
/*     */   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
/* 468 */     throw new NotUpdatable();
/*     */   }
/*     */   
/*     */   public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException
/*     */   {
/* 473 */     updateSQLXML(findColumn(columnLabel), xmlObject);
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 493 */     checkClosed();
/*     */     
/*     */ 
/*     */ 
/* 497 */     return iface.isInstance(this);
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
/* 518 */       return (T)iface.cast(this);
/*     */     } catch (ClassCastException cce) {
/* 520 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", getExceptionInterceptor());
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T getObject(int columnIndex, Class<T> type)
/*     */     throws SQLException
/*     */   {
/* 527 */     if (type == null) {
/* 528 */       throw SQLError.createSQLException("Type parameter can not be null", "S1009", getExceptionInterceptor());
/*     */     }
/*     */     
/*     */ 
/* 532 */     if (type.equals(Struct.class))
/* 533 */       throw new SQLFeatureNotSupportedException();
/* 534 */     if (type.equals(RowId.class))
/* 535 */       return getRowId(columnIndex);
/* 536 */     if (type.equals(NClob.class))
/* 537 */       return getNClob(columnIndex);
/* 538 */     if (type.equals(SQLXML.class)) {
/* 539 */       return getSQLXML(columnIndex);
/*     */     }
/*     */     
/* 542 */     return (T)super.getObject(columnIndex, type);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4ResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */