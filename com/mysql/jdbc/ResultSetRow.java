/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.sql.Date;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
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
/*      */ public abstract class ResultSetRow
/*      */ {
/*      */   protected ExceptionInterceptor exceptionInterceptor;
/*      */   protected Field[] metadata;
/*      */   
/*      */   protected ResultSetRow(ExceptionInterceptor exceptionInterceptor)
/*      */   {
/*   53 */     this.exceptionInterceptor = exceptionInterceptor;
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
/*      */   public abstract void closeOpenStreams();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract InputStream getBinaryInputStream(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract byte[] getColumnValue(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Date getDateFast(int columnIndex, byte[] dateAsBytes, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar)
/*      */     throws SQLException
/*      */   {
/*   97 */     int year = 0;
/*   98 */     int month = 0;
/*   99 */     int day = 0;
/*      */     try
/*      */     {
/*  102 */       if (dateAsBytes == null) {
/*  103 */         return null;
/*      */       }
/*      */       
/*  106 */       boolean allZeroDate = true;
/*      */       
/*  108 */       boolean onlyTimePresent = false;
/*      */       
/*  110 */       for (int i = 0; i < length; i++) {
/*  111 */         if (dateAsBytes[(offset + i)] == 58) {
/*  112 */           onlyTimePresent = true;
/*  113 */           break;
/*      */         }
/*      */       }
/*      */       
/*  117 */       for (int i = 0; i < length; i++) {
/*  118 */         byte b = dateAsBytes[(offset + i)];
/*      */         
/*  120 */         if ((b == 32) || (b == 45) || (b == 47)) {
/*  121 */           onlyTimePresent = false;
/*      */         }
/*      */         
/*  124 */         if ((b != 48) && (b != 32) && (b != 58) && (b != 45) && (b != 47) && (b != 46))
/*      */         {
/*  126 */           allZeroDate = false;
/*      */           
/*  128 */           break;
/*      */         }
/*      */       }
/*      */       
/*  132 */       if ((!onlyTimePresent) && (allZeroDate))
/*      */       {
/*  134 */         if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*      */ 
/*  137 */           return null; }
/*  138 */         if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */         {
/*  140 */           throw SQLError.createSQLException("Value '" + StringUtils.toString(dateAsBytes) + "' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  148 */         return rs.fastDateCreate(targetCalendar, 1, 1, 1);
/*      */       }
/*  150 */       if (this.metadata[columnIndex].getMysqlType() == 7)
/*      */       {
/*  152 */         switch (length) {
/*      */         case 19: 
/*      */         case 21: 
/*      */         case 29: 
/*  156 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */           
/*  158 */           month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
/*      */           
/*  160 */           day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
/*      */           
/*      */ 
/*  163 */           return rs.fastDateCreate(targetCalendar, year, month, day);
/*      */         
/*      */ 
/*      */         case 8: 
/*      */         case 14: 
/*  168 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */           
/*  170 */           month = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
/*      */           
/*  172 */           day = StringUtils.getInt(dateAsBytes, offset + 6, offset + 8);
/*      */           
/*      */ 
/*  175 */           return rs.fastDateCreate(targetCalendar, year, month, day);
/*      */         
/*      */ 
/*      */         case 6: 
/*      */         case 10: 
/*      */         case 12: 
/*  181 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
/*      */           
/*      */ 
/*  184 */           if (year <= 69) {
/*  185 */             year += 100;
/*      */           }
/*      */           
/*  188 */           month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
/*      */           
/*  190 */           day = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
/*      */           
/*      */ 
/*  193 */           return rs.fastDateCreate(targetCalendar, year + 1900, month, day);
/*      */         
/*      */ 
/*      */         case 4: 
/*  197 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */           
/*      */ 
/*  200 */           if (year <= 69) {
/*  201 */             year += 100;
/*      */           }
/*      */           
/*  204 */           month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
/*      */           
/*      */ 
/*  207 */           return rs.fastDateCreate(targetCalendar, year + 1900, month, 1);
/*      */         
/*      */ 
/*      */         case 2: 
/*  211 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
/*      */           
/*      */ 
/*  214 */           if (year <= 69) {
/*  215 */             year += 100;
/*      */           }
/*      */           
/*  218 */           return rs.fastDateCreate(targetCalendar, year + 1900, 1, 1);
/*      */         }
/*      */         
/*      */         
/*  222 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */       if (this.metadata[columnIndex].getMysqlType() == 13)
/*      */       {
/*  235 */         if ((length == 2) || (length == 1)) {
/*  236 */           year = StringUtils.getInt(dateAsBytes, offset, offset + length);
/*      */           
/*      */ 
/*  239 */           if (year <= 69) {
/*  240 */             year += 100;
/*      */           }
/*      */           
/*  243 */           year += 1900;
/*      */         } else {
/*  245 */           year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */         }
/*      */         
/*      */ 
/*  249 */         return rs.fastDateCreate(targetCalendar, year, 1, 1); }
/*  250 */       if (this.metadata[columnIndex].getMysqlType() == 11) {
/*  251 */         return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */       }
/*  253 */       if (length < 10) {
/*  254 */         if (length == 8) {
/*  255 */           return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  260 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */       }
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
/*  272 */       if (length != 18) {
/*  273 */         year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
/*      */         
/*  275 */         month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
/*      */         
/*  277 */         day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  282 */         StringTokenizer st = new StringTokenizer(StringUtils.toString(dateAsBytes, offset, length, "ISO8859_1"), "- ");
/*      */         
/*      */ 
/*  285 */         year = Integer.parseInt(st.nextToken());
/*  286 */         month = Integer.parseInt(st.nextToken());
/*  287 */         day = Integer.parseInt(st.nextToken());
/*      */       }
/*      */       
/*      */ 
/*  291 */       return rs.fastDateCreate(targetCalendar, year, month, day);
/*      */     } catch (SQLException sqlEx) {
/*  293 */       throw sqlEx;
/*      */     } catch (Exception e) {
/*  295 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  300 */       sqlEx.initCause(e);
/*      */       
/*  302 */       throw sqlEx;
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
/*      */   public abstract Date getDateFast(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getInt(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract long getLong(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date getNativeDate(int columnIndex, byte[] bits, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*  349 */     int year = 0;
/*  350 */     int month = 0;
/*  351 */     int day = 0;
/*      */     
/*  353 */     if (length != 0) {
/*  354 */       year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
/*      */       
/*  356 */       month = bits[(offset + 2)];
/*  357 */       day = bits[(offset + 3)];
/*      */     }
/*      */     
/*  360 */     if ((length == 0) || ((year == 0) && (month == 0) && (day == 0))) {
/*  361 */       if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  363 */         return null; }
/*  364 */       if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  366 */         throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  372 */       year = 1;
/*  373 */       month = 1;
/*  374 */       day = 1;
/*      */     }
/*      */     
/*  377 */     if (!rs.useLegacyDatetimeCode) {
/*  378 */       return TimeUtil.fastDateCreate(year, month, day, cal);
/*      */     }
/*      */     
/*  381 */     return rs.fastDateCreate(cal == null ? rs.getCalendarInstanceForSessionOrNew() : cal, year, month, day);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract Date getNativeDate(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */   protected Object getNativeDateTimeValue(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*      */     throws SQLException
/*      */   {
/*  393 */     int year = 0;
/*  394 */     int month = 0;
/*  395 */     int day = 0;
/*      */     
/*  397 */     int hour = 0;
/*  398 */     int minute = 0;
/*  399 */     int seconds = 0;
/*      */     
/*  401 */     int nanos = 0;
/*      */     
/*  403 */     if (bits == null)
/*      */     {
/*  405 */       return null;
/*      */     }
/*      */     
/*  408 */     Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
/*      */     
/*      */ 
/*      */ 
/*  412 */     boolean populatedFromDateTimeValue = false;
/*      */     
/*  414 */     switch (mysqlType) {
/*      */     case 7: 
/*      */     case 12: 
/*  417 */       populatedFromDateTimeValue = true;
/*      */       
/*  419 */       if (length != 0) {
/*  420 */         year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
/*      */         
/*  422 */         month = bits[(offset + 2)];
/*  423 */         day = bits[(offset + 3)];
/*      */         
/*  425 */         if (length > 4) {
/*  426 */           hour = bits[(offset + 4)];
/*  427 */           minute = bits[(offset + 5)];
/*  428 */           seconds = bits[(offset + 6)];
/*      */         }
/*      */         
/*  431 */         if (length > 7)
/*      */         {
/*  433 */           nanos = (bits[(offset + 7)] & 0xFF | (bits[(offset + 8)] & 0xFF) << 8 | (bits[(offset + 9)] & 0xFF) << 16 | (bits[(offset + 10)] & 0xFF) << 24) * 1000;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 10: 
/*  441 */       populatedFromDateTimeValue = true;
/*      */       
/*  443 */       if (bits.length != 0) {
/*  444 */         year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
/*      */         
/*  446 */         month = bits[(offset + 2)];
/*  447 */         day = bits[(offset + 3)];
/*      */       }
/*      */       
/*      */       break;
/*      */     case 11: 
/*  452 */       populatedFromDateTimeValue = true;
/*      */       
/*  454 */       if (bits.length != 0)
/*      */       {
/*      */ 
/*  457 */         hour = bits[(offset + 5)];
/*  458 */         minute = bits[(offset + 6)];
/*  459 */         seconds = bits[(offset + 7)];
/*      */       }
/*      */       
/*  462 */       year = 1970;
/*  463 */       month = 1;
/*  464 */       day = 1;
/*      */       
/*  466 */       break;
/*      */     case 8: case 9: default: 
/*  468 */       populatedFromDateTimeValue = false;
/*      */     }
/*      */     
/*  471 */     switch (jdbcType) {
/*      */     case 92: 
/*  473 */       if (populatedFromDateTimeValue) {
/*  474 */         if (!rs.useLegacyDatetimeCode) {
/*  475 */           return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
/*      */         }
/*      */         
/*  478 */         Time time = TimeUtil.fastTimeCreate(rs.getCalendarInstanceForSessionOrNew(), hour, minute, seconds, this.exceptionInterceptor);
/*      */         
/*      */ 
/*      */ 
/*  482 */         Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*  486 */         return adjustedTime;
/*      */       }
/*      */       
/*  489 */       return rs.getNativeTimeViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
/*      */     
/*      */ 
/*      */     case 91: 
/*  493 */       if (populatedFromDateTimeValue) {
/*  494 */         if ((year == 0) && (month == 0) && (day == 0)) {
/*  495 */           if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/*      */ 
/*  498 */             return null; }
/*  499 */           if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/*  501 */             throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  506 */           year = 1;
/*  507 */           month = 1;
/*  508 */           day = 1;
/*      */         }
/*      */         
/*  511 */         if (!rs.useLegacyDatetimeCode) {
/*  512 */           return TimeUtil.fastDateCreate(year, month, day, targetCalendar);
/*      */         }
/*      */         
/*  515 */         return rs.fastDateCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  521 */       return rs.getNativeDateViaParseConversion(columnIndex + 1);
/*      */     case 93: 
/*  523 */       if (populatedFromDateTimeValue) {
/*  524 */         if ((year == 0) && (month == 0) && (day == 0)) {
/*  525 */           if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/*      */ 
/*  528 */             return null; }
/*  529 */           if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */           {
/*  531 */             throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  536 */           year = 1;
/*  537 */           month = 1;
/*  538 */           day = 1;
/*      */         }
/*      */         
/*  541 */         if (!rs.useLegacyDatetimeCode) {
/*  542 */           return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
/*      */         }
/*      */         
/*      */ 
/*  546 */         Timestamp ts = rs.fastTimestampCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day, hour, minute, seconds, nanos);
/*      */         
/*      */ 
/*      */ 
/*  550 */         Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*  554 */         return adjustedTs;
/*      */       }
/*      */       
/*  557 */       return rs.getNativeTimestampViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/*      */     
/*  561 */     throw new SQLException("Internal error - conversion method doesn't support this type", "S1000");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */   protected double getNativeDouble(byte[] bits, int offset)
/*      */   {
/*  573 */     long valueAsLong = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24 | (bits[(offset + 4)] & 0xFF) << 32 | (bits[(offset + 5)] & 0xFF) << 40 | (bits[(offset + 6)] & 0xFF) << 48 | (bits[(offset + 7)] & 0xFF) << 56;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  582 */     return Double.longBitsToDouble(valueAsLong);
/*      */   }
/*      */   
/*      */   public abstract double getNativeDouble(int paramInt) throws SQLException;
/*      */   
/*      */   protected float getNativeFloat(byte[] bits, int offset) {
/*  588 */     int asInt = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  593 */     return Float.intBitsToFloat(asInt);
/*      */   }
/*      */   
/*      */   public abstract float getNativeFloat(int paramInt) throws SQLException;
/*      */   
/*      */   protected int getNativeInt(byte[] bits, int offset)
/*      */   {
/*  600 */     int valueAsInt = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  605 */     return valueAsInt;
/*      */   }
/*      */   
/*      */   public abstract int getNativeInt(int paramInt) throws SQLException;
/*      */   
/*      */   protected long getNativeLong(byte[] bits, int offset) {
/*  611 */     long valueAsLong = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24 | (bits[(offset + 4)] & 0xFF) << 32 | (bits[(offset + 5)] & 0xFF) << 40 | (bits[(offset + 6)] & 0xFF) << 48 | (bits[(offset + 7)] & 0xFF) << 56;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  620 */     return valueAsLong;
/*      */   }
/*      */   
/*      */   public abstract long getNativeLong(int paramInt) throws SQLException;
/*      */   
/*      */   protected short getNativeShort(byte[] bits, int offset) {
/*  626 */     short asShort = (short)(bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8);
/*      */     
/*  628 */     return asShort;
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
/*      */   public abstract short getNativeShort(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Time getNativeTime(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*      */     throws SQLException
/*      */   {
/*  652 */     int hour = 0;
/*  653 */     int minute = 0;
/*  654 */     int seconds = 0;
/*      */     
/*  656 */     if (length != 0)
/*      */     {
/*      */ 
/*  659 */       hour = bits[(offset + 5)];
/*  660 */       minute = bits[(offset + 6)];
/*  661 */       seconds = bits[(offset + 7)];
/*      */     }
/*      */     
/*  664 */     if (!rs.useLegacyDatetimeCode) {
/*  665 */       return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
/*      */     }
/*      */     
/*  668 */     Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
/*      */     
/*  670 */     synchronized (sessionCalendar) {
/*  671 */       Time time = TimeUtil.fastTimeCreate(sessionCalendar, hour, minute, seconds, this.exceptionInterceptor);
/*      */       
/*      */ 
/*  674 */       Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */       
/*      */ 
/*      */ 
/*  678 */       return adjustedTime;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public abstract Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
/*      */     throws SQLException;
/*      */   
/*      */   protected Timestamp getNativeTimestamp(byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*      */     throws SQLException
/*      */   {
/*  689 */     int year = 0;
/*  690 */     int month = 0;
/*  691 */     int day = 0;
/*      */     
/*  693 */     int hour = 0;
/*  694 */     int minute = 0;
/*  695 */     int seconds = 0;
/*      */     
/*  697 */     int nanos = 0;
/*      */     
/*  699 */     if (length != 0) {
/*  700 */       year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
/*  701 */       month = bits[(offset + 2)];
/*  702 */       day = bits[(offset + 3)];
/*      */       
/*  704 */       if (length > 4) {
/*  705 */         hour = bits[(offset + 4)];
/*  706 */         minute = bits[(offset + 5)];
/*  707 */         seconds = bits[(offset + 6)];
/*      */       }
/*      */       
/*  710 */       if (length > 7)
/*      */       {
/*  712 */         nanos = (bits[(offset + 7)] & 0xFF | (bits[(offset + 8)] & 0xFF) << 8 | (bits[(offset + 9)] & 0xFF) << 16 | (bits[(offset + 10)] & 0xFF) << 24) * 1000;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  718 */     if ((length == 0) || ((year == 0) && (month == 0) && (day == 0))) {
/*  719 */       if ("convertToNull".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*      */ 
/*  722 */         return null; }
/*  723 */       if ("exception".equals(conn.getZeroDateTimeBehavior()))
/*      */       {
/*  725 */         throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", this.exceptionInterceptor);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  731 */       year = 1;
/*  732 */       month = 1;
/*  733 */       day = 1;
/*      */     }
/*      */     
/*  736 */     if (!rs.useLegacyDatetimeCode) {
/*  737 */       return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
/*      */     }
/*      */     
/*      */ 
/*  741 */     Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
/*      */     
/*      */ 
/*      */ 
/*  745 */     synchronized (sessionCalendar) {
/*  746 */       Timestamp ts = rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minute, seconds, nanos);
/*      */       
/*      */ 
/*  749 */       Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
/*      */       
/*      */ 
/*      */ 
/*  753 */       return adjustedTs;
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
/*      */   public abstract Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
/*      */     throws SQLException;
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
/*      */   public abstract Reader getReader(int paramInt)
/*      */     throws SQLException;
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
/*      */   public abstract String getString(int paramInt, String paramString, MySQLConnection paramMySQLConnection)
/*      */     throws SQLException;
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
/*      */   protected String getString(String encoding, MySQLConnection conn, byte[] value, int offset, int length)
/*      */     throws SQLException
/*      */   {
/*  807 */     String stringVal = null;
/*      */     
/*  809 */     if ((conn != null) && (conn.getUseUnicode())) {
/*      */       try {
/*  811 */         if (encoding == null) {
/*  812 */           stringVal = StringUtils.toString(value);
/*      */         } else {
/*  814 */           SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
/*      */           
/*      */ 
/*  817 */           if (converter != null) {
/*  818 */             stringVal = converter.toString(value, offset, length);
/*      */           } else {
/*  820 */             stringVal = StringUtils.toString(value, offset, length, encoding);
/*      */           }
/*      */         }
/*      */       } catch (UnsupportedEncodingException E) {
/*  824 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____101") + encoding + "'.", "0S100", this.exceptionInterceptor);
/*      */ 
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/*  831 */       stringVal = StringUtils.toAsciiString(value, offset, length);
/*      */     }
/*      */     
/*  834 */     return stringVal;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected Time getTimeFast(int columnIndex, byte[] timeAsBytes, int offset, int fullLength, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore 10
/*      */     //   3: iconst_0
/*      */     //   4: istore 11
/*      */     //   6: iconst_0
/*      */     //   7: istore 12
/*      */     //   9: iconst_0
/*      */     //   10: istore 13
/*      */     //   12: iconst_m1
/*      */     //   13: istore 14
/*      */     //   15: aload_2
/*      */     //   16: ifnonnull +5 -> 21
/*      */     //   19: aconst_null
/*      */     //   20: areturn
/*      */     //   21: iconst_1
/*      */     //   22: istore 15
/*      */     //   24: iconst_0
/*      */     //   25: istore 16
/*      */     //   27: iconst_0
/*      */     //   28: istore 17
/*      */     //   30: iload 17
/*      */     //   32: iload 4
/*      */     //   34: if_icmpge +26 -> 60
/*      */     //   37: aload_2
/*      */     //   38: iload_3
/*      */     //   39: iload 17
/*      */     //   41: iadd
/*      */     //   42: baload
/*      */     //   43: bipush 58
/*      */     //   45: if_icmpne +9 -> 54
/*      */     //   48: iconst_1
/*      */     //   49: istore 16
/*      */     //   51: goto +9 -> 60
/*      */     //   54: iinc 17 1
/*      */     //   57: goto -27 -> 30
/*      */     //   60: iconst_0
/*      */     //   61: istore 17
/*      */     //   63: iload 17
/*      */     //   65: iload 4
/*      */     //   67: if_icmpge +27 -> 94
/*      */     //   70: aload_2
/*      */     //   71: iload_3
/*      */     //   72: iload 17
/*      */     //   74: iadd
/*      */     //   75: baload
/*      */     //   76: bipush 46
/*      */     //   78: if_icmpne +10 -> 88
/*      */     //   81: iload 17
/*      */     //   83: istore 14
/*      */     //   85: goto +9 -> 94
/*      */     //   88: iinc 17 1
/*      */     //   91: goto -28 -> 63
/*      */     //   94: iconst_0
/*      */     //   95: istore 17
/*      */     //   97: iload 17
/*      */     //   99: iload 4
/*      */     //   101: if_icmpge +89 -> 190
/*      */     //   104: aload_2
/*      */     //   105: iload_3
/*      */     //   106: iload 17
/*      */     //   108: iadd
/*      */     //   109: baload
/*      */     //   110: istore 18
/*      */     //   112: iload 18
/*      */     //   114: bipush 32
/*      */     //   116: if_icmpeq +17 -> 133
/*      */     //   119: iload 18
/*      */     //   121: bipush 45
/*      */     //   123: if_icmpeq +10 -> 133
/*      */     //   126: iload 18
/*      */     //   128: bipush 47
/*      */     //   130: if_icmpne +6 -> 136
/*      */     //   133: iconst_0
/*      */     //   134: istore 16
/*      */     //   136: iload 18
/*      */     //   138: bipush 48
/*      */     //   140: if_icmpeq +44 -> 184
/*      */     //   143: iload 18
/*      */     //   145: bipush 32
/*      */     //   147: if_icmpeq +37 -> 184
/*      */     //   150: iload 18
/*      */     //   152: bipush 58
/*      */     //   154: if_icmpeq +30 -> 184
/*      */     //   157: iload 18
/*      */     //   159: bipush 45
/*      */     //   161: if_icmpeq +23 -> 184
/*      */     //   164: iload 18
/*      */     //   166: bipush 47
/*      */     //   168: if_icmpeq +16 -> 184
/*      */     //   171: iload 18
/*      */     //   173: bipush 46
/*      */     //   175: if_icmpeq +9 -> 184
/*      */     //   178: iconst_0
/*      */     //   179: istore 15
/*      */     //   181: goto +9 -> 190
/*      */     //   184: iinc 17 1
/*      */     //   187: goto -90 -> 97
/*      */     //   190: iload 16
/*      */     //   192: ifne +88 -> 280
/*      */     //   195: iload 15
/*      */     //   197: ifeq +83 -> 280
/*      */     //   200: ldc 3
/*      */     //   202: aload 8
/*      */     //   204: invokeinterface 4 1 0
/*      */     //   209: invokevirtual 5	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   212: ifeq +5 -> 217
/*      */     //   215: aconst_null
/*      */     //   216: areturn
/*      */     //   217: ldc 6
/*      */     //   219: aload 8
/*      */     //   221: invokeinterface 4 1 0
/*      */     //   226: invokevirtual 5	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   229: ifeq +40 -> 269
/*      */     //   232: new 7	java/lang/StringBuilder
/*      */     //   235: dup
/*      */     //   236: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   239: ldc 9
/*      */     //   241: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   244: aload_2
/*      */     //   245: invokestatic 11	com/mysql/jdbc/StringUtils:toString	([B)Ljava/lang/String;
/*      */     //   248: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   251: ldc 65
/*      */     //   253: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   256: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   259: ldc 14
/*      */     //   261: aload_0
/*      */     //   262: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   265: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   268: athrow
/*      */     //   269: aload 9
/*      */     //   271: aload 5
/*      */     //   273: iconst_0
/*      */     //   274: iconst_0
/*      */     //   275: iconst_0
/*      */     //   276: invokevirtual 66	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   279: areturn
/*      */     //   280: aload_0
/*      */     //   281: getfield 17	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
/*      */     //   284: iload_1
/*      */     //   285: aaload
/*      */     //   286: astore 17
/*      */     //   288: iload 4
/*      */     //   290: istore 18
/*      */     //   292: iload 14
/*      */     //   294: iconst_m1
/*      */     //   295: if_icmpeq +81 -> 376
/*      */     //   298: iload 14
/*      */     //   300: istore 18
/*      */     //   302: iload 14
/*      */     //   304: iconst_2
/*      */     //   305: iadd
/*      */     //   306: iload 4
/*      */     //   308: if_icmpgt +60 -> 368
/*      */     //   311: aload_2
/*      */     //   312: iload_3
/*      */     //   313: iload 14
/*      */     //   315: iadd
/*      */     //   316: iconst_1
/*      */     //   317: iadd
/*      */     //   318: iload_3
/*      */     //   319: iload 4
/*      */     //   321: iadd
/*      */     //   322: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   325: istore 13
/*      */     //   327: iload 4
/*      */     //   329: iload 14
/*      */     //   331: iconst_1
/*      */     //   332: iadd
/*      */     //   333: isub
/*      */     //   334: istore 19
/*      */     //   336: iload 19
/*      */     //   338: bipush 9
/*      */     //   340: if_icmpge +25 -> 365
/*      */     //   343: ldc2_w 67
/*      */     //   346: bipush 9
/*      */     //   348: iload 19
/*      */     //   350: isub
/*      */     //   351: i2d
/*      */     //   352: invokestatic 69	java/lang/Math:pow	(DD)D
/*      */     //   355: d2i
/*      */     //   356: istore 20
/*      */     //   358: iload 13
/*      */     //   360: iload 20
/*      */     //   362: imul
/*      */     //   363: istore 13
/*      */     //   365: goto +11 -> 376
/*      */     //   368: new 70	java/lang/IllegalArgumentException
/*      */     //   371: dup
/*      */     //   372: invokespecial 71	java/lang/IllegalArgumentException:<init>	()V
/*      */     //   375: athrow
/*      */     //   376: aload 17
/*      */     //   378: invokevirtual 18	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   381: bipush 7
/*      */     //   383: if_icmpne +306 -> 689
/*      */     //   386: iload 18
/*      */     //   388: tableswitch	default:+203->591, 10:+169->557, 11:+203->591, 12:+113->501, 13:+203->591, 14:+113->501, 15:+203->591, 16:+203->591, 17:+203->591, 18:+203->591, 19:+56->444
/*      */     //   444: aload_2
/*      */     //   445: iload_3
/*      */     //   446: iload 18
/*      */     //   448: iadd
/*      */     //   449: bipush 8
/*      */     //   451: isub
/*      */     //   452: iload_3
/*      */     //   453: iload 18
/*      */     //   455: iadd
/*      */     //   456: bipush 6
/*      */     //   458: isub
/*      */     //   459: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   462: istore 10
/*      */     //   464: aload_2
/*      */     //   465: iload_3
/*      */     //   466: iload 18
/*      */     //   468: iadd
/*      */     //   469: iconst_5
/*      */     //   470: isub
/*      */     //   471: iload_3
/*      */     //   472: iload 18
/*      */     //   474: iadd
/*      */     //   475: iconst_3
/*      */     //   476: isub
/*      */     //   477: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   480: istore 11
/*      */     //   482: aload_2
/*      */     //   483: iload_3
/*      */     //   484: iload 18
/*      */     //   486: iadd
/*      */     //   487: iconst_2
/*      */     //   488: isub
/*      */     //   489: iload_3
/*      */     //   490: iload 18
/*      */     //   492: iadd
/*      */     //   493: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   496: istore 12
/*      */     //   498: goto +142 -> 640
/*      */     //   501: aload_2
/*      */     //   502: iload_3
/*      */     //   503: iload 18
/*      */     //   505: iadd
/*      */     //   506: bipush 6
/*      */     //   508: isub
/*      */     //   509: iload_3
/*      */     //   510: iload 18
/*      */     //   512: iadd
/*      */     //   513: iconst_4
/*      */     //   514: isub
/*      */     //   515: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   518: istore 10
/*      */     //   520: aload_2
/*      */     //   521: iload_3
/*      */     //   522: iload 18
/*      */     //   524: iadd
/*      */     //   525: iconst_4
/*      */     //   526: isub
/*      */     //   527: iload_3
/*      */     //   528: iload 18
/*      */     //   530: iadd
/*      */     //   531: iconst_2
/*      */     //   532: isub
/*      */     //   533: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   536: istore 11
/*      */     //   538: aload_2
/*      */     //   539: iload_3
/*      */     //   540: iload 18
/*      */     //   542: iadd
/*      */     //   543: iconst_2
/*      */     //   544: isub
/*      */     //   545: iload_3
/*      */     //   546: iload 18
/*      */     //   548: iadd
/*      */     //   549: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   552: istore 12
/*      */     //   554: goto +86 -> 640
/*      */     //   557: aload_2
/*      */     //   558: iload_3
/*      */     //   559: bipush 6
/*      */     //   561: iadd
/*      */     //   562: iload_3
/*      */     //   563: bipush 8
/*      */     //   565: iadd
/*      */     //   566: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   569: istore 10
/*      */     //   571: aload_2
/*      */     //   572: iload_3
/*      */     //   573: bipush 8
/*      */     //   575: iadd
/*      */     //   576: iload_3
/*      */     //   577: bipush 10
/*      */     //   579: iadd
/*      */     //   580: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   583: istore 11
/*      */     //   585: iconst_0
/*      */     //   586: istore 12
/*      */     //   588: goto +52 -> 640
/*      */     //   591: new 7	java/lang/StringBuilder
/*      */     //   594: dup
/*      */     //   595: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   598: ldc 72
/*      */     //   600: invokestatic 61	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   603: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   606: iload_1
/*      */     //   607: iconst_1
/*      */     //   608: iadd
/*      */     //   609: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   612: ldc 74
/*      */     //   614: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   617: aload 17
/*      */     //   619: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   622: ldc 76
/*      */     //   624: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   627: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   630: ldc 14
/*      */     //   632: aload_0
/*      */     //   633: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   636: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   639: athrow
/*      */     //   640: new 77	java/sql/SQLWarning
/*      */     //   643: dup
/*      */     //   644: new 7	java/lang/StringBuilder
/*      */     //   647: dup
/*      */     //   648: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   651: ldc 78
/*      */     //   653: invokestatic 61	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   656: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   659: iload_1
/*      */     //   660: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   663: ldc 74
/*      */     //   665: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   668: aload 17
/*      */     //   670: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   673: ldc 76
/*      */     //   675: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   678: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   681: invokespecial 79	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   684: astore 19
/*      */     //   686: goto +236 -> 922
/*      */     //   689: aload 17
/*      */     //   691: invokevirtual 18	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   694: bipush 12
/*      */     //   696: if_icmpne +96 -> 792
/*      */     //   699: aload_2
/*      */     //   700: iload_3
/*      */     //   701: bipush 11
/*      */     //   703: iadd
/*      */     //   704: iload_3
/*      */     //   705: bipush 13
/*      */     //   707: iadd
/*      */     //   708: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   711: istore 10
/*      */     //   713: aload_2
/*      */     //   714: iload_3
/*      */     //   715: bipush 14
/*      */     //   717: iadd
/*      */     //   718: iload_3
/*      */     //   719: bipush 16
/*      */     //   721: iadd
/*      */     //   722: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   725: istore 11
/*      */     //   727: aload_2
/*      */     //   728: iload_3
/*      */     //   729: bipush 17
/*      */     //   731: iadd
/*      */     //   732: iload_3
/*      */     //   733: bipush 19
/*      */     //   735: iadd
/*      */     //   736: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   739: istore 12
/*      */     //   741: new 77	java/sql/SQLWarning
/*      */     //   744: dup
/*      */     //   745: new 7	java/lang/StringBuilder
/*      */     //   748: dup
/*      */     //   749: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   752: ldc 80
/*      */     //   754: invokestatic 61	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   757: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   760: iload_1
/*      */     //   761: iconst_1
/*      */     //   762: iadd
/*      */     //   763: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   766: ldc 74
/*      */     //   768: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   771: aload 17
/*      */     //   773: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   776: ldc 76
/*      */     //   778: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   781: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   784: invokespecial 79	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   787: astore 19
/*      */     //   789: goto +133 -> 922
/*      */     //   792: aload 17
/*      */     //   794: invokevirtual 18	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   797: bipush 10
/*      */     //   799: if_icmpne +13 -> 812
/*      */     //   802: aload 9
/*      */     //   804: aconst_null
/*      */     //   805: iconst_0
/*      */     //   806: iconst_0
/*      */     //   807: iconst_0
/*      */     //   808: invokevirtual 66	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   811: areturn
/*      */     //   812: iload 18
/*      */     //   814: iconst_5
/*      */     //   815: if_icmpeq +59 -> 874
/*      */     //   818: iload 18
/*      */     //   820: bipush 8
/*      */     //   822: if_icmpeq +52 -> 874
/*      */     //   825: new 7	java/lang/StringBuilder
/*      */     //   828: dup
/*      */     //   829: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   832: ldc 81
/*      */     //   834: invokestatic 61	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   837: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   840: aload_2
/*      */     //   841: invokestatic 11	com/mysql/jdbc/StringUtils:toString	([B)Ljava/lang/String;
/*      */     //   844: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   847: ldc 82
/*      */     //   849: invokestatic 61	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   852: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   855: iload_1
/*      */     //   856: iconst_1
/*      */     //   857: iadd
/*      */     //   858: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   861: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   864: ldc 14
/*      */     //   866: aload_0
/*      */     //   867: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   870: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   873: athrow
/*      */     //   874: aload_2
/*      */     //   875: iload_3
/*      */     //   876: iconst_0
/*      */     //   877: iadd
/*      */     //   878: iload_3
/*      */     //   879: iconst_2
/*      */     //   880: iadd
/*      */     //   881: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   884: istore 10
/*      */     //   886: aload_2
/*      */     //   887: iload_3
/*      */     //   888: iconst_3
/*      */     //   889: iadd
/*      */     //   890: iload_3
/*      */     //   891: iconst_5
/*      */     //   892: iadd
/*      */     //   893: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   896: istore 11
/*      */     //   898: iload 18
/*      */     //   900: iconst_5
/*      */     //   901: if_icmpne +7 -> 908
/*      */     //   904: iconst_0
/*      */     //   905: goto +15 -> 920
/*      */     //   908: aload_2
/*      */     //   909: iload_3
/*      */     //   910: bipush 6
/*      */     //   912: iadd
/*      */     //   913: iload_3
/*      */     //   914: bipush 8
/*      */     //   916: iadd
/*      */     //   917: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   920: istore 12
/*      */     //   922: aload 9
/*      */     //   924: invokevirtual 37	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   927: astore 19
/*      */     //   929: aload 9
/*      */     //   931: getfield 35	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   934: ifne +17 -> 951
/*      */     //   937: aload 9
/*      */     //   939: aload 5
/*      */     //   941: iload 10
/*      */     //   943: iload 11
/*      */     //   945: iload 12
/*      */     //   947: invokevirtual 66	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   950: areturn
/*      */     //   951: aload 19
/*      */     //   953: dup
/*      */     //   954: astore 20
/*      */     //   956: monitorenter
/*      */     //   957: aload 8
/*      */     //   959: aload 19
/*      */     //   961: aload 5
/*      */     //   963: aload 9
/*      */     //   965: aload 19
/*      */     //   967: iload 10
/*      */     //   969: iload 11
/*      */     //   971: iload 12
/*      */     //   973: invokevirtual 66	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   976: aload 8
/*      */     //   978: invokeinterface 42 1 0
/*      */     //   983: aload 6
/*      */     //   985: iload 7
/*      */     //   987: invokestatic 43	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
/*      */     //   990: aload 20
/*      */     //   992: monitorexit
/*      */     //   993: areturn
/*      */     //   994: astore 21
/*      */     //   996: aload 20
/*      */     //   998: monitorexit
/*      */     //   999: aload 21
/*      */     //   1001: athrow
/*      */     //   1002: astore 15
/*      */     //   1004: aload 15
/*      */     //   1006: invokevirtual 84	java/lang/RuntimeException:toString	()Ljava/lang/String;
/*      */     //   1009: ldc 14
/*      */     //   1011: aload_0
/*      */     //   1012: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   1015: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   1018: astore 16
/*      */     //   1020: aload 16
/*      */     //   1022: aload 15
/*      */     //   1024: invokevirtual 33	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */     //   1027: pop
/*      */     //   1028: aload 16
/*      */     //   1030: athrow
/*      */     // Line number table:
/*      */     //   Java source line #842	-> byte code offset #0
/*      */     //   Java source line #843	-> byte code offset #3
/*      */     //   Java source line #844	-> byte code offset #6
/*      */     //   Java source line #845	-> byte code offset #9
/*      */     //   Java source line #847	-> byte code offset #12
/*      */     //   Java source line #851	-> byte code offset #15
/*      */     //   Java source line #852	-> byte code offset #19
/*      */     //   Java source line #855	-> byte code offset #21
/*      */     //   Java source line #856	-> byte code offset #24
/*      */     //   Java source line #858	-> byte code offset #27
/*      */     //   Java source line #859	-> byte code offset #37
/*      */     //   Java source line #860	-> byte code offset #48
/*      */     //   Java source line #861	-> byte code offset #51
/*      */     //   Java source line #858	-> byte code offset #54
/*      */     //   Java source line #865	-> byte code offset #60
/*      */     //   Java source line #866	-> byte code offset #70
/*      */     //   Java source line #867	-> byte code offset #81
/*      */     //   Java source line #868	-> byte code offset #85
/*      */     //   Java source line #865	-> byte code offset #88
/*      */     //   Java source line #872	-> byte code offset #94
/*      */     //   Java source line #873	-> byte code offset #104
/*      */     //   Java source line #875	-> byte code offset #112
/*      */     //   Java source line #876	-> byte code offset #133
/*      */     //   Java source line #879	-> byte code offset #136
/*      */     //   Java source line #881	-> byte code offset #178
/*      */     //   Java source line #883	-> byte code offset #181
/*      */     //   Java source line #872	-> byte code offset #184
/*      */     //   Java source line #887	-> byte code offset #190
/*      */     //   Java source line #888	-> byte code offset #200
/*      */     //   Java source line #890	-> byte code offset #215
/*      */     //   Java source line #891	-> byte code offset #217
/*      */     //   Java source line #893	-> byte code offset #232
/*      */     //   Java source line #901	-> byte code offset #269
/*      */     //   Java source line #904	-> byte code offset #280
/*      */     //   Java source line #906	-> byte code offset #288
/*      */     //   Java source line #908	-> byte code offset #292
/*      */     //   Java source line #910	-> byte code offset #298
/*      */     //   Java source line #912	-> byte code offset #302
/*      */     //   Java source line #913	-> byte code offset #311
/*      */     //   Java source line #915	-> byte code offset #327
/*      */     //   Java source line #917	-> byte code offset #336
/*      */     //   Java source line #918	-> byte code offset #343
/*      */     //   Java source line #919	-> byte code offset #358
/*      */     //   Java source line #921	-> byte code offset #365
/*      */     //   Java source line #922	-> byte code offset #368
/*      */     //   Java source line #931	-> byte code offset #376
/*      */     //   Java source line #933	-> byte code offset #386
/*      */     //   Java source line #936	-> byte code offset #444
/*      */     //   Java source line #938	-> byte code offset #464
/*      */     //   Java source line #940	-> byte code offset #482
/*      */     //   Java source line #944	-> byte code offset #498
/*      */     //   Java source line #947	-> byte code offset #501
/*      */     //   Java source line #949	-> byte code offset #520
/*      */     //   Java source line #951	-> byte code offset #538
/*      */     //   Java source line #955	-> byte code offset #554
/*      */     //   Java source line #958	-> byte code offset #557
/*      */     //   Java source line #960	-> byte code offset #571
/*      */     //   Java source line #962	-> byte code offset #585
/*      */     //   Java source line #965	-> byte code offset #588
/*      */     //   Java source line #968	-> byte code offset #591
/*      */     //   Java source line #979	-> byte code offset #640
/*      */     //   Java source line #988	-> byte code offset #686
/*      */     //   Java source line #989	-> byte code offset #699
/*      */     //   Java source line #990	-> byte code offset #713
/*      */     //   Java source line #991	-> byte code offset #727
/*      */     //   Java source line #994	-> byte code offset #741
/*      */     //   Java source line #1004	-> byte code offset #789
/*      */     //   Java source line #1005	-> byte code offset #802
/*      */     //   Java source line #1010	-> byte code offset #812
/*      */     //   Java source line #1011	-> byte code offset #825
/*      */     //   Java source line #1019	-> byte code offset #874
/*      */     //   Java source line #1020	-> byte code offset #886
/*      */     //   Java source line #1021	-> byte code offset #898
/*      */     //   Java source line #1025	-> byte code offset #922
/*      */     //   Java source line #1027	-> byte code offset #929
/*      */     //   Java source line #1031	-> byte code offset #937
/*      */     //   Java source line #1034	-> byte code offset #951
/*      */     //   Java source line #1035	-> byte code offset #957
/*      */     //   Java source line #1042	-> byte code offset #994
/*      */     //   Java source line #1043	-> byte code offset #1002
/*      */     //   Java source line #1044	-> byte code offset #1004
/*      */     //   Java source line #1046	-> byte code offset #1020
/*      */     //   Java source line #1048	-> byte code offset #1028
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1031	0	this	ResultSetRow
/*      */     //   0	1031	1	columnIndex	int
/*      */     //   0	1031	2	timeAsBytes	byte[]
/*      */     //   0	1031	3	offset	int
/*      */     //   0	1031	4	fullLength	int
/*      */     //   0	1031	5	targetCalendar	Calendar
/*      */     //   0	1031	6	tz	TimeZone
/*      */     //   0	1031	7	rollForward	boolean
/*      */     //   0	1031	8	conn	MySQLConnection
/*      */     //   0	1031	9	rs	ResultSetImpl
/*      */     //   1	967	10	hr	int
/*      */     //   4	966	11	min	int
/*      */     //   7	965	12	sec	int
/*      */     //   10	354	13	nanos	int
/*      */     //   13	317	14	decimalIndex	int
/*      */     //   22	174	15	allZeroTime	boolean
/*      */     //   1002	21	15	ex	RuntimeException
/*      */     //   25	166	16	onlyTimePresent	boolean
/*      */     //   1018	11	16	sqlEx	SQLException
/*      */     //   28	27	17	i	int
/*      */     //   61	28	17	i	int
/*      */     //   95	90	17	i	int
/*      */     //   286	507	17	timeColField	Field
/*      */     //   110	62	18	b	byte
/*      */     //   290	609	18	length	int
/*      */     //   334	15	19	numDigits	int
/*      */     //   684	3	19	precisionLost	java.sql.SQLWarning
/*      */     //   787	3	19	precisionLost	java.sql.SQLWarning
/*      */     //   927	39	19	sessionCalendar	Calendar
/*      */     //   356	5	20	factor	int
/*      */     //   954	43	20	Ljava/lang/Object;	Object
/*      */     //   994	6	21	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   957	993	994	finally
/*      */     //   994	999	994	finally
/*      */     //   15	20	1002	java/lang/RuntimeException
/*      */     //   21	216	1002	java/lang/RuntimeException
/*      */     //   217	279	1002	java/lang/RuntimeException
/*      */     //   280	811	1002	java/lang/RuntimeException
/*      */     //   812	950	1002	java/lang/RuntimeException
/*      */     //   951	993	1002	java/lang/RuntimeException
/*      */     //   994	1002	1002	java/lang/RuntimeException
/*      */   }
/*      */   
/*      */   public abstract Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
/*      */     throws SQLException;
/*      */   
/*      */   /* Error */
/*      */   protected Timestamp getTimestampFast(int columnIndex, byte[] timestampAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload 8
/*      */     //   2: invokeinterface 38 1 0
/*      */     //   7: ifeq +13 -> 20
/*      */     //   10: aload 8
/*      */     //   12: invokeinterface 39 1 0
/*      */     //   17: goto +8 -> 25
/*      */     //   20: aload 9
/*      */     //   22: invokevirtual 37	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   25: astore 10
/*      */     //   27: aload 10
/*      */     //   29: dup
/*      */     //   30: astore 11
/*      */     //   32: monitorenter
/*      */     //   33: iconst_1
/*      */     //   34: istore 12
/*      */     //   36: iconst_0
/*      */     //   37: istore 13
/*      */     //   39: iconst_0
/*      */     //   40: istore 14
/*      */     //   42: iload 14
/*      */     //   44: iload 4
/*      */     //   46: if_icmpge +26 -> 72
/*      */     //   49: aload_2
/*      */     //   50: iload_3
/*      */     //   51: iload 14
/*      */     //   53: iadd
/*      */     //   54: baload
/*      */     //   55: bipush 58
/*      */     //   57: if_icmpne +9 -> 66
/*      */     //   60: iconst_1
/*      */     //   61: istore 13
/*      */     //   63: goto +9 -> 72
/*      */     //   66: iinc 14 1
/*      */     //   69: goto -27 -> 42
/*      */     //   72: iconst_0
/*      */     //   73: istore 14
/*      */     //   75: iload 14
/*      */     //   77: iload 4
/*      */     //   79: if_icmpge +89 -> 168
/*      */     //   82: aload_2
/*      */     //   83: iload_3
/*      */     //   84: iload 14
/*      */     //   86: iadd
/*      */     //   87: baload
/*      */     //   88: istore 15
/*      */     //   90: iload 15
/*      */     //   92: bipush 32
/*      */     //   94: if_icmpeq +17 -> 111
/*      */     //   97: iload 15
/*      */     //   99: bipush 45
/*      */     //   101: if_icmpeq +10 -> 111
/*      */     //   104: iload 15
/*      */     //   106: bipush 47
/*      */     //   108: if_icmpne +6 -> 114
/*      */     //   111: iconst_0
/*      */     //   112: istore 13
/*      */     //   114: iload 15
/*      */     //   116: bipush 48
/*      */     //   118: if_icmpeq +44 -> 162
/*      */     //   121: iload 15
/*      */     //   123: bipush 32
/*      */     //   125: if_icmpeq +37 -> 162
/*      */     //   128: iload 15
/*      */     //   130: bipush 58
/*      */     //   132: if_icmpeq +30 -> 162
/*      */     //   135: iload 15
/*      */     //   137: bipush 45
/*      */     //   139: if_icmpeq +23 -> 162
/*      */     //   142: iload 15
/*      */     //   144: bipush 47
/*      */     //   146: if_icmpeq +16 -> 162
/*      */     //   149: iload 15
/*      */     //   151: bipush 46
/*      */     //   153: if_icmpeq +9 -> 162
/*      */     //   156: iconst_0
/*      */     //   157: istore 12
/*      */     //   159: goto +9 -> 168
/*      */     //   162: iinc 14 1
/*      */     //   165: goto -90 -> 75
/*      */     //   168: iload 13
/*      */     //   170: ifne +121 -> 291
/*      */     //   173: iload 12
/*      */     //   175: ifeq +116 -> 291
/*      */     //   178: ldc 3
/*      */     //   180: aload 8
/*      */     //   182: invokeinterface 4 1 0
/*      */     //   187: invokevirtual 5	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   190: ifeq +8 -> 198
/*      */     //   193: aconst_null
/*      */     //   194: aload 11
/*      */     //   196: monitorexit
/*      */     //   197: areturn
/*      */     //   198: ldc 6
/*      */     //   200: aload 8
/*      */     //   202: invokeinterface 4 1 0
/*      */     //   207: invokevirtual 5	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   210: ifeq +40 -> 250
/*      */     //   213: new 7	java/lang/StringBuilder
/*      */     //   216: dup
/*      */     //   217: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   220: ldc 9
/*      */     //   222: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   225: aload_2
/*      */     //   226: invokestatic 11	com/mysql/jdbc/StringUtils:toString	([B)Ljava/lang/String;
/*      */     //   229: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   232: ldc 85
/*      */     //   234: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   237: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   240: ldc 14
/*      */     //   242: aload_0
/*      */     //   243: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   246: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   249: athrow
/*      */     //   250: aload 9
/*      */     //   252: getfield 35	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   255: ifne +19 -> 274
/*      */     //   258: aload 6
/*      */     //   260: iconst_1
/*      */     //   261: iconst_1
/*      */     //   262: iconst_1
/*      */     //   263: iconst_0
/*      */     //   264: iconst_0
/*      */     //   265: iconst_0
/*      */     //   266: iconst_0
/*      */     //   267: invokestatic 48	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   270: aload 11
/*      */     //   272: monitorexit
/*      */     //   273: areturn
/*      */     //   274: aload 9
/*      */     //   276: aconst_null
/*      */     //   277: iconst_1
/*      */     //   278: iconst_1
/*      */     //   279: iconst_1
/*      */     //   280: iconst_0
/*      */     //   281: iconst_0
/*      */     //   282: iconst_0
/*      */     //   283: iconst_0
/*      */     //   284: invokevirtual 49	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   287: aload 11
/*      */     //   289: monitorexit
/*      */     //   290: areturn
/*      */     //   291: aload_0
/*      */     //   292: getfield 17	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
/*      */     //   295: iload_1
/*      */     //   296: aaload
/*      */     //   297: invokevirtual 18	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   300: bipush 13
/*      */     //   302: if_icmpne +75 -> 377
/*      */     //   305: aload 9
/*      */     //   307: getfield 35	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   310: ifne +24 -> 334
/*      */     //   313: aload 6
/*      */     //   315: aload_2
/*      */     //   316: iload_3
/*      */     //   317: iconst_4
/*      */     //   318: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   321: iconst_1
/*      */     //   322: iconst_1
/*      */     //   323: iconst_0
/*      */     //   324: iconst_0
/*      */     //   325: iconst_0
/*      */     //   326: iconst_0
/*      */     //   327: invokestatic 48	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   330: aload 11
/*      */     //   332: monitorexit
/*      */     //   333: areturn
/*      */     //   334: aload 8
/*      */     //   336: aload 10
/*      */     //   338: aload 5
/*      */     //   340: aload 9
/*      */     //   342: aload 10
/*      */     //   344: aload_2
/*      */     //   345: iload_3
/*      */     //   346: iconst_4
/*      */     //   347: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   350: iconst_1
/*      */     //   351: iconst_1
/*      */     //   352: iconst_0
/*      */     //   353: iconst_0
/*      */     //   354: iconst_0
/*      */     //   355: iconst_0
/*      */     //   356: invokevirtual 49	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   359: aload 8
/*      */     //   361: invokeinterface 42 1 0
/*      */     //   366: aload 6
/*      */     //   368: iload 7
/*      */     //   370: invokestatic 50	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   373: aload 11
/*      */     //   375: monitorexit
/*      */     //   376: areturn
/*      */     //   377: aload_2
/*      */     //   378: iload_3
/*      */     //   379: iload 4
/*      */     //   381: iadd
/*      */     //   382: iconst_1
/*      */     //   383: isub
/*      */     //   384: baload
/*      */     //   385: bipush 46
/*      */     //   387: if_icmpne +6 -> 393
/*      */     //   390: iinc 4 -1
/*      */     //   393: iconst_0
/*      */     //   394: istore 14
/*      */     //   396: iconst_0
/*      */     //   397: istore 15
/*      */     //   399: iconst_0
/*      */     //   400: istore 16
/*      */     //   402: iconst_0
/*      */     //   403: istore 17
/*      */     //   405: iconst_0
/*      */     //   406: istore 18
/*      */     //   408: iconst_0
/*      */     //   409: istore 19
/*      */     //   411: iconst_0
/*      */     //   412: istore 20
/*      */     //   414: iload 4
/*      */     //   416: tableswitch	default:+1002->1418, 2:+961->1377, 3:+1002->1418, 4:+917->1333, 5:+1002->1418, 6:+857->1273, 7:+1002->1418, 8:+713->1129, 9:+1002->1418, 10:+522->938, 11:+1002->1418, 12:+420->836, 13:+1002->1418, 14:+336->752, 15:+1002->1418, 16:+1002->1418, 17:+1002->1418, 18:+1002->1418, 19:+128->544, 20:+128->544, 21:+128->544, 22:+128->544, 23:+128->544, 24:+128->544, 25:+128->544, 26:+128->544, 27:+1002->1418, 28:+1002->1418, 29:+128->544
/*      */     //   544: aload_2
/*      */     //   545: iload_3
/*      */     //   546: iconst_0
/*      */     //   547: iadd
/*      */     //   548: iload_3
/*      */     //   549: iconst_4
/*      */     //   550: iadd
/*      */     //   551: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   554: istore 14
/*      */     //   556: aload_2
/*      */     //   557: iload_3
/*      */     //   558: iconst_5
/*      */     //   559: iadd
/*      */     //   560: iload_3
/*      */     //   561: bipush 7
/*      */     //   563: iadd
/*      */     //   564: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   567: istore 15
/*      */     //   569: aload_2
/*      */     //   570: iload_3
/*      */     //   571: bipush 8
/*      */     //   573: iadd
/*      */     //   574: iload_3
/*      */     //   575: bipush 10
/*      */     //   577: iadd
/*      */     //   578: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   581: istore 16
/*      */     //   583: aload_2
/*      */     //   584: iload_3
/*      */     //   585: bipush 11
/*      */     //   587: iadd
/*      */     //   588: iload_3
/*      */     //   589: bipush 13
/*      */     //   591: iadd
/*      */     //   592: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   595: istore 17
/*      */     //   597: aload_2
/*      */     //   598: iload_3
/*      */     //   599: bipush 14
/*      */     //   601: iadd
/*      */     //   602: iload_3
/*      */     //   603: bipush 16
/*      */     //   605: iadd
/*      */     //   606: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   609: istore 18
/*      */     //   611: aload_2
/*      */     //   612: iload_3
/*      */     //   613: bipush 17
/*      */     //   615: iadd
/*      */     //   616: iload_3
/*      */     //   617: bipush 19
/*      */     //   619: iadd
/*      */     //   620: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   623: istore 19
/*      */     //   625: iconst_0
/*      */     //   626: istore 20
/*      */     //   628: iload 4
/*      */     //   630: bipush 19
/*      */     //   632: if_icmple +834 -> 1466
/*      */     //   635: iconst_m1
/*      */     //   636: istore 21
/*      */     //   638: iconst_0
/*      */     //   639: istore 22
/*      */     //   641: iload 22
/*      */     //   643: iload 4
/*      */     //   645: if_icmpge +24 -> 669
/*      */     //   648: aload_2
/*      */     //   649: iload_3
/*      */     //   650: iload 22
/*      */     //   652: iadd
/*      */     //   653: baload
/*      */     //   654: bipush 46
/*      */     //   656: if_icmpne +7 -> 663
/*      */     //   659: iload 22
/*      */     //   661: istore 21
/*      */     //   663: iinc 22 1
/*      */     //   666: goto -25 -> 641
/*      */     //   669: iload 21
/*      */     //   671: iconst_m1
/*      */     //   672: if_icmpeq +77 -> 749
/*      */     //   675: iload 21
/*      */     //   677: iconst_2
/*      */     //   678: iadd
/*      */     //   679: iload 4
/*      */     //   681: if_icmpgt +60 -> 741
/*      */     //   684: aload_2
/*      */     //   685: iload_3
/*      */     //   686: iload 21
/*      */     //   688: iadd
/*      */     //   689: iconst_1
/*      */     //   690: iadd
/*      */     //   691: iload_3
/*      */     //   692: iload 4
/*      */     //   694: iadd
/*      */     //   695: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   698: istore 20
/*      */     //   700: iload 4
/*      */     //   702: iload 21
/*      */     //   704: iconst_1
/*      */     //   705: iadd
/*      */     //   706: isub
/*      */     //   707: istore 22
/*      */     //   709: iload 22
/*      */     //   711: bipush 9
/*      */     //   713: if_icmpge +25 -> 738
/*      */     //   716: ldc2_w 67
/*      */     //   719: bipush 9
/*      */     //   721: iload 22
/*      */     //   723: isub
/*      */     //   724: i2d
/*      */     //   725: invokestatic 69	java/lang/Math:pow	(DD)D
/*      */     //   728: d2i
/*      */     //   729: istore 23
/*      */     //   731: iload 20
/*      */     //   733: iload 23
/*      */     //   735: imul
/*      */     //   736: istore 20
/*      */     //   738: goto +11 -> 749
/*      */     //   741: new 70	java/lang/IllegalArgumentException
/*      */     //   744: dup
/*      */     //   745: invokespecial 71	java/lang/IllegalArgumentException:<init>	()V
/*      */     //   748: athrow
/*      */     //   749: goto +717 -> 1466
/*      */     //   752: aload_2
/*      */     //   753: iload_3
/*      */     //   754: iconst_0
/*      */     //   755: iadd
/*      */     //   756: iload_3
/*      */     //   757: iconst_4
/*      */     //   758: iadd
/*      */     //   759: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   762: istore 14
/*      */     //   764: aload_2
/*      */     //   765: iload_3
/*      */     //   766: iconst_4
/*      */     //   767: iadd
/*      */     //   768: iload_3
/*      */     //   769: bipush 6
/*      */     //   771: iadd
/*      */     //   772: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   775: istore 15
/*      */     //   777: aload_2
/*      */     //   778: iload_3
/*      */     //   779: bipush 6
/*      */     //   781: iadd
/*      */     //   782: iload_3
/*      */     //   783: bipush 8
/*      */     //   785: iadd
/*      */     //   786: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   789: istore 16
/*      */     //   791: aload_2
/*      */     //   792: iload_3
/*      */     //   793: bipush 8
/*      */     //   795: iadd
/*      */     //   796: iload_3
/*      */     //   797: bipush 10
/*      */     //   799: iadd
/*      */     //   800: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   803: istore 17
/*      */     //   805: aload_2
/*      */     //   806: iload_3
/*      */     //   807: bipush 10
/*      */     //   809: iadd
/*      */     //   810: iload_3
/*      */     //   811: bipush 12
/*      */     //   813: iadd
/*      */     //   814: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   817: istore 18
/*      */     //   819: aload_2
/*      */     //   820: iload_3
/*      */     //   821: bipush 12
/*      */     //   823: iadd
/*      */     //   824: iload_3
/*      */     //   825: bipush 14
/*      */     //   827: iadd
/*      */     //   828: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   831: istore 19
/*      */     //   833: goto +633 -> 1466
/*      */     //   836: aload_2
/*      */     //   837: iload_3
/*      */     //   838: iconst_0
/*      */     //   839: iadd
/*      */     //   840: iload_3
/*      */     //   841: iconst_2
/*      */     //   842: iadd
/*      */     //   843: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   846: istore 14
/*      */     //   848: iload 14
/*      */     //   850: bipush 69
/*      */     //   852: if_icmpgt +10 -> 862
/*      */     //   855: iload 14
/*      */     //   857: bipush 100
/*      */     //   859: iadd
/*      */     //   860: istore 14
/*      */     //   862: wide
/*      */     //   868: aload_2
/*      */     //   869: iload_3
/*      */     //   870: iconst_2
/*      */     //   871: iadd
/*      */     //   872: iload_3
/*      */     //   873: iconst_4
/*      */     //   874: iadd
/*      */     //   875: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   878: istore 15
/*      */     //   880: aload_2
/*      */     //   881: iload_3
/*      */     //   882: iconst_4
/*      */     //   883: iadd
/*      */     //   884: iload_3
/*      */     //   885: bipush 6
/*      */     //   887: iadd
/*      */     //   888: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   891: istore 16
/*      */     //   893: aload_2
/*      */     //   894: iload_3
/*      */     //   895: bipush 6
/*      */     //   897: iadd
/*      */     //   898: iload_3
/*      */     //   899: bipush 8
/*      */     //   901: iadd
/*      */     //   902: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   905: istore 17
/*      */     //   907: aload_2
/*      */     //   908: iload_3
/*      */     //   909: bipush 8
/*      */     //   911: iadd
/*      */     //   912: iload_3
/*      */     //   913: bipush 10
/*      */     //   915: iadd
/*      */     //   916: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   919: istore 18
/*      */     //   921: aload_2
/*      */     //   922: iload_3
/*      */     //   923: bipush 10
/*      */     //   925: iadd
/*      */     //   926: iload_3
/*      */     //   927: bipush 12
/*      */     //   929: iadd
/*      */     //   930: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   933: istore 19
/*      */     //   935: goto +531 -> 1466
/*      */     //   938: iconst_0
/*      */     //   939: istore 21
/*      */     //   941: iconst_0
/*      */     //   942: istore 22
/*      */     //   944: iload 22
/*      */     //   946: iload 4
/*      */     //   948: if_icmpge +26 -> 974
/*      */     //   951: aload_2
/*      */     //   952: iload_3
/*      */     //   953: iload 22
/*      */     //   955: iadd
/*      */     //   956: baload
/*      */     //   957: bipush 45
/*      */     //   959: if_icmpne +9 -> 968
/*      */     //   962: iconst_1
/*      */     //   963: istore 21
/*      */     //   965: goto +9 -> 974
/*      */     //   968: iinc 22 1
/*      */     //   971: goto -27 -> 944
/*      */     //   974: aload_0
/*      */     //   975: getfield 17	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
/*      */     //   978: iload_1
/*      */     //   979: aaload
/*      */     //   980: invokevirtual 18	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   983: bipush 10
/*      */     //   985: if_icmpeq +8 -> 993
/*      */     //   988: iload 21
/*      */     //   990: ifeq +51 -> 1041
/*      */     //   993: aload_2
/*      */     //   994: iload_3
/*      */     //   995: iconst_0
/*      */     //   996: iadd
/*      */     //   997: iload_3
/*      */     //   998: iconst_4
/*      */     //   999: iadd
/*      */     //   1000: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1003: istore 14
/*      */     //   1005: aload_2
/*      */     //   1006: iload_3
/*      */     //   1007: iconst_5
/*      */     //   1008: iadd
/*      */     //   1009: iload_3
/*      */     //   1010: bipush 7
/*      */     //   1012: iadd
/*      */     //   1013: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1016: istore 15
/*      */     //   1018: aload_2
/*      */     //   1019: iload_3
/*      */     //   1020: bipush 8
/*      */     //   1022: iadd
/*      */     //   1023: iload_3
/*      */     //   1024: bipush 10
/*      */     //   1026: iadd
/*      */     //   1027: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1030: istore 16
/*      */     //   1032: iconst_0
/*      */     //   1033: istore 17
/*      */     //   1035: iconst_0
/*      */     //   1036: istore 18
/*      */     //   1038: goto +428 -> 1466
/*      */     //   1041: aload_2
/*      */     //   1042: iload_3
/*      */     //   1043: iconst_0
/*      */     //   1044: iadd
/*      */     //   1045: iload_3
/*      */     //   1046: iconst_2
/*      */     //   1047: iadd
/*      */     //   1048: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1051: istore 14
/*      */     //   1053: iload 14
/*      */     //   1055: bipush 69
/*      */     //   1057: if_icmpgt +10 -> 1067
/*      */     //   1060: iload 14
/*      */     //   1062: bipush 100
/*      */     //   1064: iadd
/*      */     //   1065: istore 14
/*      */     //   1067: aload_2
/*      */     //   1068: iload_3
/*      */     //   1069: iconst_2
/*      */     //   1070: iadd
/*      */     //   1071: iload_3
/*      */     //   1072: iconst_4
/*      */     //   1073: iadd
/*      */     //   1074: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1077: istore 15
/*      */     //   1079: aload_2
/*      */     //   1080: iload_3
/*      */     //   1081: iconst_4
/*      */     //   1082: iadd
/*      */     //   1083: iload_3
/*      */     //   1084: bipush 6
/*      */     //   1086: iadd
/*      */     //   1087: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1090: istore 16
/*      */     //   1092: aload_2
/*      */     //   1093: iload_3
/*      */     //   1094: bipush 6
/*      */     //   1096: iadd
/*      */     //   1097: iload_3
/*      */     //   1098: bipush 8
/*      */     //   1100: iadd
/*      */     //   1101: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1104: istore 17
/*      */     //   1106: aload_2
/*      */     //   1107: iload_3
/*      */     //   1108: bipush 8
/*      */     //   1110: iadd
/*      */     //   1111: iload_3
/*      */     //   1112: bipush 10
/*      */     //   1114: iadd
/*      */     //   1115: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1118: istore 18
/*      */     //   1120: wide
/*      */     //   1126: goto +340 -> 1466
/*      */     //   1129: iconst_0
/*      */     //   1130: istore 21
/*      */     //   1132: iconst_0
/*      */     //   1133: istore 22
/*      */     //   1135: iload 22
/*      */     //   1137: iload 4
/*      */     //   1139: if_icmpge +26 -> 1165
/*      */     //   1142: aload_2
/*      */     //   1143: iload_3
/*      */     //   1144: iload 22
/*      */     //   1146: iadd
/*      */     //   1147: baload
/*      */     //   1148: bipush 58
/*      */     //   1150: if_icmpne +9 -> 1159
/*      */     //   1153: iconst_1
/*      */     //   1154: istore 21
/*      */     //   1156: goto +9 -> 1165
/*      */     //   1159: iinc 22 1
/*      */     //   1162: goto -27 -> 1135
/*      */     //   1165: iload 21
/*      */     //   1167: ifeq +55 -> 1222
/*      */     //   1170: aload_2
/*      */     //   1171: iload_3
/*      */     //   1172: iconst_0
/*      */     //   1173: iadd
/*      */     //   1174: iload_3
/*      */     //   1175: iconst_2
/*      */     //   1176: iadd
/*      */     //   1177: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1180: istore 17
/*      */     //   1182: aload_2
/*      */     //   1183: iload_3
/*      */     //   1184: iconst_3
/*      */     //   1185: iadd
/*      */     //   1186: iload_3
/*      */     //   1187: iconst_5
/*      */     //   1188: iadd
/*      */     //   1189: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1192: istore 18
/*      */     //   1194: aload_2
/*      */     //   1195: iload_3
/*      */     //   1196: bipush 6
/*      */     //   1198: iadd
/*      */     //   1199: iload_3
/*      */     //   1200: bipush 8
/*      */     //   1202: iadd
/*      */     //   1203: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1206: istore 19
/*      */     //   1208: sipush 1970
/*      */     //   1211: istore 14
/*      */     //   1213: iconst_1
/*      */     //   1214: istore 15
/*      */     //   1216: iconst_1
/*      */     //   1217: istore 16
/*      */     //   1219: goto +247 -> 1466
/*      */     //   1222: aload_2
/*      */     //   1223: iload_3
/*      */     //   1224: iconst_0
/*      */     //   1225: iadd
/*      */     //   1226: iload_3
/*      */     //   1227: iconst_4
/*      */     //   1228: iadd
/*      */     //   1229: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1232: istore 14
/*      */     //   1234: aload_2
/*      */     //   1235: iload_3
/*      */     //   1236: iconst_4
/*      */     //   1237: iadd
/*      */     //   1238: iload_3
/*      */     //   1239: bipush 6
/*      */     //   1241: iadd
/*      */     //   1242: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1245: istore 15
/*      */     //   1247: aload_2
/*      */     //   1248: iload_3
/*      */     //   1249: bipush 6
/*      */     //   1251: iadd
/*      */     //   1252: iload_3
/*      */     //   1253: bipush 8
/*      */     //   1255: iadd
/*      */     //   1256: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1259: istore 16
/*      */     //   1261: wide
/*      */     //   1267: iinc 15 -1
/*      */     //   1270: goto +196 -> 1466
/*      */     //   1273: aload_2
/*      */     //   1274: iload_3
/*      */     //   1275: iconst_0
/*      */     //   1276: iadd
/*      */     //   1277: iload_3
/*      */     //   1278: iconst_2
/*      */     //   1279: iadd
/*      */     //   1280: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1283: istore 14
/*      */     //   1285: iload 14
/*      */     //   1287: bipush 69
/*      */     //   1289: if_icmpgt +10 -> 1299
/*      */     //   1292: iload 14
/*      */     //   1294: bipush 100
/*      */     //   1296: iadd
/*      */     //   1297: istore 14
/*      */     //   1299: wide
/*      */     //   1305: aload_2
/*      */     //   1306: iload_3
/*      */     //   1307: iconst_2
/*      */     //   1308: iadd
/*      */     //   1309: iload_3
/*      */     //   1310: iconst_4
/*      */     //   1311: iadd
/*      */     //   1312: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1315: istore 15
/*      */     //   1317: aload_2
/*      */     //   1318: iload_3
/*      */     //   1319: iconst_4
/*      */     //   1320: iadd
/*      */     //   1321: iload_3
/*      */     //   1322: bipush 6
/*      */     //   1324: iadd
/*      */     //   1325: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1328: istore 16
/*      */     //   1330: goto +136 -> 1466
/*      */     //   1333: aload_2
/*      */     //   1334: iload_3
/*      */     //   1335: iconst_0
/*      */     //   1336: iadd
/*      */     //   1337: iload_3
/*      */     //   1338: iconst_2
/*      */     //   1339: iadd
/*      */     //   1340: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1343: istore 14
/*      */     //   1345: iload 14
/*      */     //   1347: bipush 69
/*      */     //   1349: if_icmpgt +10 -> 1359
/*      */     //   1352: iload 14
/*      */     //   1354: bipush 100
/*      */     //   1356: iadd
/*      */     //   1357: istore 14
/*      */     //   1359: aload_2
/*      */     //   1360: iload_3
/*      */     //   1361: iconst_2
/*      */     //   1362: iadd
/*      */     //   1363: iload_3
/*      */     //   1364: iconst_4
/*      */     //   1365: iadd
/*      */     //   1366: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1369: istore 15
/*      */     //   1371: iconst_1
/*      */     //   1372: istore 16
/*      */     //   1374: goto +92 -> 1466
/*      */     //   1377: aload_2
/*      */     //   1378: iload_3
/*      */     //   1379: iconst_0
/*      */     //   1380: iadd
/*      */     //   1381: iload_3
/*      */     //   1382: iconst_2
/*      */     //   1383: iadd
/*      */     //   1384: invokestatic 19	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1387: istore 14
/*      */     //   1389: iload 14
/*      */     //   1391: bipush 69
/*      */     //   1393: if_icmpgt +10 -> 1403
/*      */     //   1396: iload 14
/*      */     //   1398: bipush 100
/*      */     //   1400: iadd
/*      */     //   1401: istore 14
/*      */     //   1403: wide
/*      */     //   1409: iconst_1
/*      */     //   1410: istore 15
/*      */     //   1412: iconst_1
/*      */     //   1413: istore 16
/*      */     //   1415: goto +51 -> 1466
/*      */     //   1418: new 31	java/sql/SQLException
/*      */     //   1421: dup
/*      */     //   1422: new 7	java/lang/StringBuilder
/*      */     //   1425: dup
/*      */     //   1426: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   1429: ldc 86
/*      */     //   1431: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1434: aload_2
/*      */     //   1435: invokestatic 11	com/mysql/jdbc/StringUtils:toString	([B)Ljava/lang/String;
/*      */     //   1438: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1441: ldc 87
/*      */     //   1443: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1446: iload_1
/*      */     //   1447: iconst_1
/*      */     //   1448: iadd
/*      */     //   1449: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   1452: ldc 88
/*      */     //   1454: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1457: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   1460: ldc 14
/*      */     //   1462: invokespecial 45	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1465: athrow
/*      */     //   1466: aload 9
/*      */     //   1468: getfield 35	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
/*      */     //   1471: ifne +26 -> 1497
/*      */     //   1474: aload 6
/*      */     //   1476: iload 14
/*      */     //   1478: iload 15
/*      */     //   1480: iload 16
/*      */     //   1482: iload 17
/*      */     //   1484: iload 18
/*      */     //   1486: iload 19
/*      */     //   1488: iload 20
/*      */     //   1490: invokestatic 48	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1493: aload 11
/*      */     //   1495: monitorexit
/*      */     //   1496: areturn
/*      */     //   1497: aload 8
/*      */     //   1499: aload 10
/*      */     //   1501: aload 5
/*      */     //   1503: aload 9
/*      */     //   1505: aload 10
/*      */     //   1507: iload 14
/*      */     //   1509: iload 15
/*      */     //   1511: iload 16
/*      */     //   1513: iload 17
/*      */     //   1515: iload 18
/*      */     //   1517: iload 19
/*      */     //   1519: iload 20
/*      */     //   1521: invokevirtual 49	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1524: aload 8
/*      */     //   1526: invokeinterface 42 1 0
/*      */     //   1531: aload 6
/*      */     //   1533: iload 7
/*      */     //   1535: invokestatic 50	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1538: aload 11
/*      */     //   1540: monitorexit
/*      */     //   1541: areturn
/*      */     //   1542: astore 24
/*      */     //   1544: aload 11
/*      */     //   1546: monitorexit
/*      */     //   1547: aload 24
/*      */     //   1549: athrow
/*      */     //   1550: astore 10
/*      */     //   1552: new 7	java/lang/StringBuilder
/*      */     //   1555: dup
/*      */     //   1556: invokespecial 8	java/lang/StringBuilder:<init>	()V
/*      */     //   1559: ldc 89
/*      */     //   1561: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1564: aload_0
/*      */     //   1565: iload_1
/*      */     //   1566: ldc 25
/*      */     //   1568: aload 8
/*      */     //   1570: invokevirtual 90	com/mysql/jdbc/ResultSetRow:getString	(ILjava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)Ljava/lang/String;
/*      */     //   1573: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1576: ldc 91
/*      */     //   1578: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1581: iload_1
/*      */     //   1582: iconst_1
/*      */     //   1583: iadd
/*      */     //   1584: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   1587: ldc 92
/*      */     //   1589: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   1592: invokevirtual 13	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   1595: ldc 14
/*      */     //   1597: aload_0
/*      */     //   1598: getfield 2	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */     //   1601: invokestatic 15	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */     //   1604: astore 11
/*      */     //   1606: aload 11
/*      */     //   1608: aload 10
/*      */     //   1610: invokevirtual 33	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */     //   1613: pop
/*      */     //   1614: aload 11
/*      */     //   1616: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1062	-> byte code offset #0
/*      */     //   Java source line #1066	-> byte code offset #27
/*      */     //   Java source line #1067	-> byte code offset #33
/*      */     //   Java source line #1069	-> byte code offset #36
/*      */     //   Java source line #1071	-> byte code offset #39
/*      */     //   Java source line #1072	-> byte code offset #49
/*      */     //   Java source line #1073	-> byte code offset #60
/*      */     //   Java source line #1074	-> byte code offset #63
/*      */     //   Java source line #1071	-> byte code offset #66
/*      */     //   Java source line #1078	-> byte code offset #72
/*      */     //   Java source line #1079	-> byte code offset #82
/*      */     //   Java source line #1081	-> byte code offset #90
/*      */     //   Java source line #1082	-> byte code offset #111
/*      */     //   Java source line #1085	-> byte code offset #114
/*      */     //   Java source line #1087	-> byte code offset #156
/*      */     //   Java source line #1089	-> byte code offset #159
/*      */     //   Java source line #1078	-> byte code offset #162
/*      */     //   Java source line #1093	-> byte code offset #168
/*      */     //   Java source line #1095	-> byte code offset #178
/*      */     //   Java source line #1098	-> byte code offset #193
/*      */     //   Java source line #1099	-> byte code offset #198
/*      */     //   Java source line #1101	-> byte code offset #213
/*      */     //   Java source line #1109	-> byte code offset #250
/*      */     //   Java source line #1110	-> byte code offset #258
/*      */     //   Java source line #1114	-> byte code offset #274
/*      */     //   Java source line #1116	-> byte code offset #291
/*      */     //   Java source line #1118	-> byte code offset #305
/*      */     //   Java source line #1119	-> byte code offset #313
/*      */     //   Java source line #1124	-> byte code offset #334
/*      */     //   Java source line #1131	-> byte code offset #377
/*      */     //   Java source line #1132	-> byte code offset #390
/*      */     //   Java source line #1137	-> byte code offset #393
/*      */     //   Java source line #1138	-> byte code offset #396
/*      */     //   Java source line #1139	-> byte code offset #399
/*      */     //   Java source line #1140	-> byte code offset #402
/*      */     //   Java source line #1141	-> byte code offset #405
/*      */     //   Java source line #1142	-> byte code offset #408
/*      */     //   Java source line #1143	-> byte code offset #411
/*      */     //   Java source line #1145	-> byte code offset #414
/*      */     //   Java source line #1155	-> byte code offset #544
/*      */     //   Java source line #1157	-> byte code offset #556
/*      */     //   Java source line #1159	-> byte code offset #569
/*      */     //   Java source line #1161	-> byte code offset #583
/*      */     //   Java source line #1163	-> byte code offset #597
/*      */     //   Java source line #1165	-> byte code offset #611
/*      */     //   Java source line #1168	-> byte code offset #625
/*      */     //   Java source line #1170	-> byte code offset #628
/*      */     //   Java source line #1171	-> byte code offset #635
/*      */     //   Java source line #1173	-> byte code offset #638
/*      */     //   Java source line #1174	-> byte code offset #648
/*      */     //   Java source line #1175	-> byte code offset #659
/*      */     //   Java source line #1173	-> byte code offset #663
/*      */     //   Java source line #1179	-> byte code offset #669
/*      */     //   Java source line #1180	-> byte code offset #675
/*      */     //   Java source line #1181	-> byte code offset #684
/*      */     //   Java source line #1185	-> byte code offset #700
/*      */     //   Java source line #1187	-> byte code offset #709
/*      */     //   Java source line #1188	-> byte code offset #716
/*      */     //   Java source line #1189	-> byte code offset #731
/*      */     //   Java source line #1191	-> byte code offset #738
/*      */     //   Java source line #1192	-> byte code offset #741
/*      */     //   Java source line #1200	-> byte code offset #749
/*      */     //   Java source line #1206	-> byte code offset #752
/*      */     //   Java source line #1208	-> byte code offset #764
/*      */     //   Java source line #1210	-> byte code offset #777
/*      */     //   Java source line #1212	-> byte code offset #791
/*      */     //   Java source line #1214	-> byte code offset #805
/*      */     //   Java source line #1216	-> byte code offset #819
/*      */     //   Java source line #1219	-> byte code offset #833
/*      */     //   Java source line #1223	-> byte code offset #836
/*      */     //   Java source line #1226	-> byte code offset #848
/*      */     //   Java source line #1227	-> byte code offset #855
/*      */     //   Java source line #1230	-> byte code offset #862
/*      */     //   Java source line #1232	-> byte code offset #868
/*      */     //   Java source line #1234	-> byte code offset #880
/*      */     //   Java source line #1236	-> byte code offset #893
/*      */     //   Java source line #1238	-> byte code offset #907
/*      */     //   Java source line #1240	-> byte code offset #921
/*      */     //   Java source line #1243	-> byte code offset #935
/*      */     //   Java source line #1247	-> byte code offset #938
/*      */     //   Java source line #1249	-> byte code offset #941
/*      */     //   Java source line #1250	-> byte code offset #951
/*      */     //   Java source line #1251	-> byte code offset #962
/*      */     //   Java source line #1252	-> byte code offset #965
/*      */     //   Java source line #1249	-> byte code offset #968
/*      */     //   Java source line #1256	-> byte code offset #974
/*      */     //   Java source line #1258	-> byte code offset #993
/*      */     //   Java source line #1260	-> byte code offset #1005
/*      */     //   Java source line #1262	-> byte code offset #1018
/*      */     //   Java source line #1264	-> byte code offset #1032
/*      */     //   Java source line #1265	-> byte code offset #1035
/*      */     //   Java source line #1267	-> byte code offset #1041
/*      */     //   Java source line #1270	-> byte code offset #1053
/*      */     //   Java source line #1271	-> byte code offset #1060
/*      */     //   Java source line #1274	-> byte code offset #1067
/*      */     //   Java source line #1276	-> byte code offset #1079
/*      */     //   Java source line #1278	-> byte code offset #1092
/*      */     //   Java source line #1280	-> byte code offset #1106
/*      */     //   Java source line #1283	-> byte code offset #1120
/*      */     //   Java source line #1286	-> byte code offset #1126
/*      */     //   Java source line #1290	-> byte code offset #1129
/*      */     //   Java source line #1292	-> byte code offset #1132
/*      */     //   Java source line #1293	-> byte code offset #1142
/*      */     //   Java source line #1294	-> byte code offset #1153
/*      */     //   Java source line #1295	-> byte code offset #1156
/*      */     //   Java source line #1292	-> byte code offset #1159
/*      */     //   Java source line #1299	-> byte code offset #1165
/*      */     //   Java source line #1300	-> byte code offset #1170
/*      */     //   Java source line #1302	-> byte code offset #1182
/*      */     //   Java source line #1304	-> byte code offset #1194
/*      */     //   Java source line #1307	-> byte code offset #1208
/*      */     //   Java source line #1308	-> byte code offset #1213
/*      */     //   Java source line #1309	-> byte code offset #1216
/*      */     //   Java source line #1311	-> byte code offset #1219
/*      */     //   Java source line #1314	-> byte code offset #1222
/*      */     //   Java source line #1316	-> byte code offset #1234
/*      */     //   Java source line #1318	-> byte code offset #1247
/*      */     //   Java source line #1321	-> byte code offset #1261
/*      */     //   Java source line #1322	-> byte code offset #1267
/*      */     //   Java source line #1324	-> byte code offset #1270
/*      */     //   Java source line #1328	-> byte code offset #1273
/*      */     //   Java source line #1331	-> byte code offset #1285
/*      */     //   Java source line #1332	-> byte code offset #1292
/*      */     //   Java source line #1335	-> byte code offset #1299
/*      */     //   Java source line #1337	-> byte code offset #1305
/*      */     //   Java source line #1339	-> byte code offset #1317
/*      */     //   Java source line #1342	-> byte code offset #1330
/*      */     //   Java source line #1346	-> byte code offset #1333
/*      */     //   Java source line #1349	-> byte code offset #1345
/*      */     //   Java source line #1350	-> byte code offset #1352
/*      */     //   Java source line #1353	-> byte code offset #1359
/*      */     //   Java source line #1356	-> byte code offset #1371
/*      */     //   Java source line #1358	-> byte code offset #1374
/*      */     //   Java source line #1362	-> byte code offset #1377
/*      */     //   Java source line #1365	-> byte code offset #1389
/*      */     //   Java source line #1366	-> byte code offset #1396
/*      */     //   Java source line #1369	-> byte code offset #1403
/*      */     //   Java source line #1370	-> byte code offset #1409
/*      */     //   Java source line #1371	-> byte code offset #1412
/*      */     //   Java source line #1373	-> byte code offset #1415
/*      */     //   Java source line #1377	-> byte code offset #1418
/*      */     //   Java source line #1385	-> byte code offset #1466
/*      */     //   Java source line #1386	-> byte code offset #1474
/*      */     //   Java source line #1392	-> byte code offset #1497
/*      */     //   Java source line #1401	-> byte code offset #1542
/*      */     //   Java source line #1402	-> byte code offset #1550
/*      */     //   Java source line #1403	-> byte code offset #1552
/*      */     //   Java source line #1407	-> byte code offset #1606
/*      */     //   Java source line #1409	-> byte code offset #1614
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1617	0	this	ResultSetRow
/*      */     //   0	1617	1	columnIndex	int
/*      */     //   0	1617	2	timestampAsBytes	byte[]
/*      */     //   0	1617	3	offset	int
/*      */     //   0	1617	4	length	int
/*      */     //   0	1617	5	targetCalendar	Calendar
/*      */     //   0	1617	6	tz	TimeZone
/*      */     //   0	1617	7	rollForward	boolean
/*      */     //   0	1617	8	conn	MySQLConnection
/*      */     //   0	1617	9	rs	ResultSetImpl
/*      */     //   25	1481	10	sessionCalendar	Calendar
/*      */     //   1550	59	10	e	RuntimeException
/*      */     //   1604	11	11	sqlEx	SQLException
/*      */     //   34	140	12	allZeroTimestamp	boolean
/*      */     //   37	132	13	onlyTimePresent	boolean
/*      */     //   40	27	14	i	int
/*      */     //   73	90	14	i	int
/*      */     //   394	1114	14	year	int
/*      */     //   88	62	15	b	byte
/*      */     //   397	1113	15	month	int
/*      */     //   400	1112	16	day	int
/*      */     //   403	1111	17	hour	int
/*      */     //   406	1110	18	minutes	int
/*      */     //   409	1109	19	seconds	int
/*      */     //   412	1108	20	nanos	int
/*      */     //   636	67	21	decimalIndex	int
/*      */     //   939	50	21	hasDash	boolean
/*      */     //   1130	36	21	hasColon	boolean
/*      */     //   639	25	22	i	int
/*      */     //   707	15	22	numDigits	int
/*      */     //   942	27	22	i	int
/*      */     //   1133	27	22	i	int
/*      */     //   729	5	23	factor	int
/*      */     //   1542	6	24	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   33	197	1542	finally
/*      */     //   198	273	1542	finally
/*      */     //   274	290	1542	finally
/*      */     //   291	333	1542	finally
/*      */     //   334	376	1542	finally
/*      */     //   377	1496	1542	finally
/*      */     //   1497	1541	1542	finally
/*      */     //   1542	1547	1542	finally
/*      */     //   0	197	1550	java/lang/RuntimeException
/*      */     //   198	273	1550	java/lang/RuntimeException
/*      */     //   274	290	1550	java/lang/RuntimeException
/*      */     //   291	333	1550	java/lang/RuntimeException
/*      */     //   334	376	1550	java/lang/RuntimeException
/*      */     //   377	1496	1550	java/lang/RuntimeException
/*      */     //   1497	1541	1550	java/lang/RuntimeException
/*      */     //   1542	1550	1550	java/lang/RuntimeException
/*      */   }
/*      */   
/*      */   public abstract Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
/*      */     throws SQLException;
/*      */   
/*      */   public abstract boolean isFloatingPointNumber(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */   public abstract boolean isNull(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */   public abstract long length(int paramInt)
/*      */     throws SQLException;
/*      */   
/*      */   public abstract void setColumnValue(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException;
/*      */   
/*      */   public ResultSetRow setMetadata(Field[] f)
/*      */     throws SQLException
/*      */   {
/* 1477 */     this.metadata = f;
/*      */     
/* 1479 */     return this;
/*      */   }
/*      */   
/*      */   public abstract int getBytesSize();
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ResultSetRow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */