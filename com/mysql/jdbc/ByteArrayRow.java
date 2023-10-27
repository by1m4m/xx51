/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.sql.Date;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayRow
/*     */   extends ResultSetRow
/*     */ {
/*     */   byte[][] internalRowData;
/*     */   
/*     */   public ByteArrayRow(byte[][] internalRowData, ExceptionInterceptor exceptionInterceptor)
/*     */   {
/*  48 */     super(exceptionInterceptor);
/*     */     
/*  50 */     this.internalRowData = internalRowData;
/*     */   }
/*     */   
/*     */   public byte[] getColumnValue(int index) throws SQLException {
/*  54 */     return this.internalRowData[index];
/*     */   }
/*     */   
/*     */   public void setColumnValue(int index, byte[] value) throws SQLException {
/*  58 */     this.internalRowData[index] = value;
/*     */   }
/*     */   
/*     */   public String getString(int index, String encoding, MySQLConnection conn) throws SQLException
/*     */   {
/*  63 */     byte[] columnData = this.internalRowData[index];
/*     */     
/*  65 */     if (columnData == null) {
/*  66 */       return null;
/*     */     }
/*     */     
/*  69 */     return getString(encoding, conn, columnData, 0, columnData.length);
/*     */   }
/*     */   
/*     */   public boolean isNull(int index) throws SQLException {
/*  73 */     return this.internalRowData[index] == null;
/*     */   }
/*     */   
/*     */   public boolean isFloatingPointNumber(int index) throws SQLException {
/*  77 */     byte[] numAsBytes = this.internalRowData[index];
/*     */     
/*  79 */     if ((this.internalRowData[index] == null) || (this.internalRowData[index].length == 0))
/*     */     {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     for (int i = 0; i < numAsBytes.length; i++) {
/*  85 */       if (((char)numAsBytes[i] == 'e') || ((char)numAsBytes[i] == 'E')) {
/*  86 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public long length(int index) throws SQLException {
/*  94 */     if (this.internalRowData[index] == null) {
/*  95 */       return 0L;
/*     */     }
/*     */     
/*  98 */     return this.internalRowData[index].length;
/*     */   }
/*     */   
/*     */   public int getInt(int columnIndex) {
/* 102 */     if (this.internalRowData[columnIndex] == null) {
/* 103 */       return 0;
/*     */     }
/*     */     
/* 106 */     return StringUtils.getInt(this.internalRowData[columnIndex]);
/*     */   }
/*     */   
/*     */   public long getLong(int columnIndex) {
/* 110 */     if (this.internalRowData[columnIndex] == null) {
/* 111 */       return 0L;
/*     */     }
/*     */     
/* 114 */     return StringUtils.getLong(this.internalRowData[columnIndex]);
/*     */   }
/*     */   
/*     */   public Timestamp getTimestampFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 120 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 122 */     if (columnValue == null) {
/* 123 */       return null;
/*     */     }
/*     */     
/* 126 */     return getTimestampFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public double getNativeDouble(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 132 */     if (this.internalRowData[columnIndex] == null) {
/* 133 */       return 0.0D;
/*     */     }
/*     */     
/* 136 */     return getNativeDouble(this.internalRowData[columnIndex], 0);
/*     */   }
/*     */   
/*     */   public float getNativeFloat(int columnIndex) throws SQLException {
/* 140 */     if (this.internalRowData[columnIndex] == null) {
/* 141 */       return 0.0F;
/*     */     }
/*     */     
/* 144 */     return getNativeFloat(this.internalRowData[columnIndex], 0);
/*     */   }
/*     */   
/*     */   public int getNativeInt(int columnIndex) throws SQLException {
/* 148 */     if (this.internalRowData[columnIndex] == null) {
/* 149 */       return 0;
/*     */     }
/*     */     
/* 152 */     return getNativeInt(this.internalRowData[columnIndex], 0);
/*     */   }
/*     */   
/*     */   public long getNativeLong(int columnIndex) throws SQLException {
/* 156 */     if (this.internalRowData[columnIndex] == null) {
/* 157 */       return 0L;
/*     */     }
/*     */     
/* 160 */     return getNativeLong(this.internalRowData[columnIndex], 0);
/*     */   }
/*     */   
/*     */   public short getNativeShort(int columnIndex) throws SQLException {
/* 164 */     if (this.internalRowData[columnIndex] == null) {
/* 165 */       return 0;
/*     */     }
/*     */     
/* 168 */     return getNativeShort(this.internalRowData[columnIndex], 0);
/*     */   }
/*     */   
/*     */   public Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 174 */     byte[] bits = this.internalRowData[columnIndex];
/*     */     
/* 176 */     if (bits == null) {
/* 177 */       return null;
/*     */     }
/*     */     
/* 180 */     return getNativeTimestamp(bits, 0, bits.length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */ 
/*     */   public void closeOpenStreams() {}
/*     */   
/*     */ 
/*     */   public InputStream getBinaryInputStream(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 190 */     if (this.internalRowData[columnIndex] == null) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     return new ByteArrayInputStream(this.internalRowData[columnIndex]);
/*     */   }
/*     */   
/*     */   public Reader getReader(int columnIndex) throws SQLException {
/* 198 */     InputStream stream = getBinaryInputStream(columnIndex);
/*     */     
/* 200 */     if (stream == null) {
/* 201 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 205 */       return new InputStreamReader(stream, this.metadata[columnIndex].getCharacterSet());
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 208 */       SQLException sqlEx = SQLError.createSQLException("", this.exceptionInterceptor);
/*     */       
/* 210 */       sqlEx.initCause(e);
/*     */       
/* 212 */       throw sqlEx;
/*     */     }
/*     */   }
/*     */   
/*     */   public Time getTimeFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 219 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 221 */     if (columnValue == null) {
/* 222 */       return null;
/*     */     }
/*     */     
/* 225 */     return getTimeFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public Date getDateFast(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar)
/*     */     throws SQLException
/*     */   {
/* 231 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 233 */     if (columnValue == null) {
/* 234 */       return null;
/*     */     }
/*     */     
/* 237 */     return getDateFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, conn, rs, targetCalendar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getNativeDateTimeValue(int columnIndex, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 245 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 247 */     if (columnValue == null) {
/* 248 */       return null;
/*     */     }
/*     */     
/* 251 */     return getNativeDateTimeValue(columnIndex, columnValue, 0, columnValue.length, targetCalendar, jdbcType, mysqlType, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */ 
/*     */   public Date getNativeDate(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar cal)
/*     */     throws SQLException
/*     */   {
/* 258 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 260 */     if (columnValue == null) {
/* 261 */       return null;
/*     */     }
/*     */     
/* 264 */     return getNativeDate(columnIndex, columnValue, 0, columnValue.length, conn, rs, cal);
/*     */   }
/*     */   
/*     */ 
/*     */   public Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 271 */     byte[] columnValue = this.internalRowData[columnIndex];
/*     */     
/* 273 */     if (columnValue == null) {
/* 274 */       return null;
/*     */     }
/*     */     
/* 277 */     return getNativeTime(columnIndex, columnValue, 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public int getBytesSize()
/*     */   {
/* 282 */     if (this.internalRowData == null) {
/* 283 */       return 0;
/*     */     }
/*     */     
/* 286 */     int bytesSize = 0;
/*     */     
/* 288 */     for (int i = 0; i < this.internalRowData.length; i++) {
/* 289 */       if (this.internalRowData[i] != null) {
/* 290 */         bytesSize += this.internalRowData[i].length;
/*     */       }
/*     */     }
/*     */     
/* 294 */     return bytesSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ByteArrayRow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */