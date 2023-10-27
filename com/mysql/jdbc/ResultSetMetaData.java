/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ public class ResultSetMetaData
/*     */   implements java.sql.ResultSetMetaData
/*     */ {
/*     */   Field[] fields;
/*     */   
/*     */   private static int clampedGetLength(Field f)
/*     */   {
/*  39 */     long fieldLength = f.getLength();
/*     */     
/*  41 */     if (fieldLength > 2147483647L) {
/*  42 */       fieldLength = 2147483647L;
/*     */     }
/*     */     
/*  45 */     return (int)fieldLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean isDecimalType(int type)
/*     */   {
/*  57 */     switch (type) {
/*     */     case -7: 
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 2: 
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*  68 */       return true;
/*     */     }
/*     */     
/*  71 */     return false;
/*     */   }
/*     */   
/*     */ 
/*  75 */   boolean useOldAliasBehavior = false;
/*  76 */   boolean treatYearAsDate = true;
/*     */   
/*     */ 
/*     */ 
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSetMetaData(Field[] fields, boolean useOldAliasBehavior, boolean treatYearAsDate, ExceptionInterceptor exceptionInterceptor)
/*     */   {
/*  87 */     this.fields = fields;
/*  88 */     this.useOldAliasBehavior = useOldAliasBehavior;
/*  89 */     this.treatYearAsDate = treatYearAsDate;
/*  90 */     this.exceptionInterceptor = exceptionInterceptor;
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
/*     */   public String getCatalogName(int column)
/*     */     throws SQLException
/*     */   {
/* 105 */     Field f = getField(column);
/*     */     
/* 107 */     String database = f.getDatabaseName();
/*     */     
/* 109 */     return database == null ? "" : database;
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
/*     */   public String getColumnCharacterEncoding(int column)
/*     */     throws SQLException
/*     */   {
/* 126 */     String mysqlName = getColumnCharacterSet(column);
/*     */     
/* 128 */     String javaName = null;
/*     */     
/* 130 */     if (mysqlName != null) {
/*     */       try {
/* 132 */         javaName = (String)CharsetMapping.MYSQL_TO_JAVA_CHARSET_MAP.get(mysqlName);
/*     */       } catch (RuntimeException ex) {
/* 134 */         SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", null);
/* 135 */         sqlEx.initCause(ex);
/* 136 */         throw sqlEx;
/*     */       }
/*     */     }
/*     */     
/* 140 */     return javaName;
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
/*     */   public String getColumnCharacterSet(int column)
/*     */     throws SQLException
/*     */   {
/* 155 */     return getField(column).getCharacterSet();
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
/*     */   public String getColumnClassName(int column)
/*     */     throws SQLException
/*     */   {
/* 181 */     Field f = getField(column);
/*     */     
/* 183 */     return getClassNameForJavaType(f.getSQLType(), f.isUnsigned(), f.getMysqlType(), (f.isBinary()) || (f.isBlob()), f.isOpaqueBinary(), this.treatYearAsDate);
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
/*     */   public int getColumnCount()
/*     */     throws SQLException
/*     */   {
/* 199 */     return this.fields.length;
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
/*     */   public int getColumnDisplaySize(int column)
/*     */     throws SQLException
/*     */   {
/* 214 */     Field f = getField(column);
/*     */     
/* 216 */     int lengthInBytes = clampedGetLength(f);
/*     */     
/* 218 */     return lengthInBytes / f.getMaxBytesPerCharacter();
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
/*     */   public String getColumnLabel(int column)
/*     */     throws SQLException
/*     */   {
/* 233 */     if (this.useOldAliasBehavior) {
/* 234 */       return getColumnName(column);
/*     */     }
/*     */     
/* 237 */     return getField(column).getColumnLabel();
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
/*     */   public String getColumnName(int column)
/*     */     throws SQLException
/*     */   {
/* 252 */     if (this.useOldAliasBehavior) {
/* 253 */       return getField(column).getName();
/*     */     }
/*     */     
/* 256 */     String name = getField(column).getNameNoAliases();
/*     */     
/* 258 */     if ((name != null) && (name.length() == 0)) {
/* 259 */       return getField(column).getName();
/*     */     }
/*     */     
/* 262 */     return name;
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
/*     */   public int getColumnType(int column)
/*     */     throws SQLException
/*     */   {
/* 279 */     return getField(column).getSQLType();
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
/*     */   public String getColumnTypeName(int column)
/*     */     throws SQLException
/*     */   {
/* 294 */     Field field = getField(column);
/*     */     
/* 296 */     int mysqlType = field.getMysqlType();
/* 297 */     int jdbcType = field.getSQLType();
/*     */     
/* 299 */     switch (mysqlType) {
/*     */     case 16: 
/* 301 */       return "BIT";
/*     */     case 0: 
/*     */     case 246: 
/* 304 */       return field.isUnsigned() ? "DECIMAL UNSIGNED" : "DECIMAL";
/*     */     
/*     */     case 1: 
/* 307 */       return field.isUnsigned() ? "TINYINT UNSIGNED" : "TINYINT";
/*     */     
/*     */     case 2: 
/* 310 */       return field.isUnsigned() ? "SMALLINT UNSIGNED" : "SMALLINT";
/*     */     
/*     */     case 3: 
/* 313 */       return field.isUnsigned() ? "INT UNSIGNED" : "INT";
/*     */     
/*     */     case 4: 
/* 316 */       return field.isUnsigned() ? "FLOAT UNSIGNED" : "FLOAT";
/*     */     
/*     */     case 5: 
/* 319 */       return field.isUnsigned() ? "DOUBLE UNSIGNED" : "DOUBLE";
/*     */     
/*     */     case 6: 
/* 322 */       return "NULL";
/*     */     
/*     */     case 7: 
/* 325 */       return "TIMESTAMP";
/*     */     
/*     */     case 8: 
/* 328 */       return field.isUnsigned() ? "BIGINT UNSIGNED" : "BIGINT";
/*     */     
/*     */     case 9: 
/* 331 */       return field.isUnsigned() ? "MEDIUMINT UNSIGNED" : "MEDIUMINT";
/*     */     
/*     */     case 10: 
/* 334 */       return "DATE";
/*     */     
/*     */     case 11: 
/* 337 */       return "TIME";
/*     */     
/*     */     case 12: 
/* 340 */       return "DATETIME";
/*     */     
/*     */     case 249: 
/* 343 */       return "TINYBLOB";
/*     */     
/*     */     case 250: 
/* 346 */       return "MEDIUMBLOB";
/*     */     
/*     */     case 251: 
/* 349 */       return "LONGBLOB";
/*     */     
/*     */     case 252: 
/* 352 */       if (getField(column).isBinary()) {
/* 353 */         return "BLOB";
/*     */       }
/*     */       
/* 356 */       return "TEXT";
/*     */     
/*     */     case 15: 
/* 359 */       return "VARCHAR";
/*     */     
/*     */     case 253: 
/* 362 */       if (jdbcType == -3) {
/* 363 */         return "VARBINARY";
/*     */       }
/*     */       
/* 366 */       return "VARCHAR";
/*     */     
/*     */     case 254: 
/* 369 */       if (jdbcType == -2) {
/* 370 */         return "BINARY";
/*     */       }
/*     */       
/* 373 */       return "CHAR";
/*     */     
/*     */     case 247: 
/* 376 */       return "ENUM";
/*     */     
/*     */     case 13: 
/* 379 */       return "YEAR";
/*     */     
/*     */     case 248: 
/* 382 */       return "SET";
/*     */     
/*     */     case 255: 
/* 385 */       return "GEOMETRY";
/*     */     }
/*     */     
/* 388 */     return "UNKNOWN";
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
/*     */   protected Field getField(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 404 */     if ((columnIndex < 1) || (columnIndex > this.fields.length)) {
/* 405 */       throw SQLError.createSQLException(Messages.getString("ResultSetMetaData.46"), "S1002", this.exceptionInterceptor);
/*     */     }
/*     */     
/*     */ 
/* 409 */     return this.fields[(columnIndex - 1)];
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
/*     */   public int getPrecision(int column)
/*     */     throws SQLException
/*     */   {
/* 424 */     Field f = getField(column);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */     if (isDecimalType(f.getSQLType())) {
/* 431 */       if (f.getDecimals() > 0) {
/* 432 */         return clampedGetLength(f) - 1 + f.getPrecisionAdjustFactor();
/*     */       }
/*     */       
/* 435 */       return clampedGetLength(f) + f.getPrecisionAdjustFactor();
/*     */     }
/*     */     
/* 438 */     switch (f.getMysqlType()) {
/*     */     case 249: 
/*     */     case 250: 
/*     */     case 251: 
/*     */     case 252: 
/* 443 */       return clampedGetLength(f);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 450 */     return clampedGetLength(f) / f.getMaxBytesPerCharacter();
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
/*     */   public int getScale(int column)
/*     */     throws SQLException
/*     */   {
/* 467 */     Field f = getField(column);
/*     */     
/* 469 */     if (isDecimalType(f.getSQLType())) {
/* 470 */       return f.getDecimals();
/*     */     }
/*     */     
/* 473 */     return 0;
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
/*     */   public String getSchemaName(int column)
/*     */     throws SQLException
/*     */   {
/* 490 */     return "";
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
/*     */   public String getTableName(int column)
/*     */     throws SQLException
/*     */   {
/* 505 */     if (this.useOldAliasBehavior) {
/* 506 */       return getField(column).getTableName();
/*     */     }
/*     */     
/* 509 */     return getField(column).getTableNameNoAliases();
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
/*     */   public boolean isAutoIncrement(int column)
/*     */     throws SQLException
/*     */   {
/* 524 */     Field f = getField(column);
/*     */     
/* 526 */     return f.isAutoIncrement();
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
/*     */   public boolean isCaseSensitive(int column)
/*     */     throws SQLException
/*     */   {
/* 541 */     Field field = getField(column);
/*     */     
/* 543 */     int sqlType = field.getSQLType();
/*     */     
/* 545 */     switch (sqlType) {
/*     */     case -7: 
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 91: 
/*     */     case 92: 
/*     */     case 93: 
/* 557 */       return false;
/*     */     
/*     */ 
/*     */     case -1: 
/*     */     case 1: 
/*     */     case 12: 
/* 563 */       if (field.isBinary()) {
/* 564 */         return true;
/*     */       }
/*     */       
/* 567 */       String collationName = field.getCollation();
/*     */       
/* 569 */       return (collationName != null) && (!collationName.endsWith("_ci"));
/*     */     }
/*     */     
/* 572 */     return true;
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
/*     */   public boolean isCurrency(int column)
/*     */     throws SQLException
/*     */   {
/* 588 */     return false;
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
/*     */   public boolean isDefinitelyWritable(int column)
/*     */     throws SQLException
/*     */   {
/* 603 */     return isWritable(column);
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
/*     */   public int isNullable(int column)
/*     */     throws SQLException
/*     */   {
/* 618 */     if (!getField(column).isNotNull()) {
/* 619 */       return 1;
/*     */     }
/*     */     
/* 622 */     return 0;
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
/*     */   public boolean isReadOnly(int column)
/*     */     throws SQLException
/*     */   {
/* 637 */     return getField(column).isReadOnly();
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
/*     */   public boolean isSearchable(int column)
/*     */     throws SQLException
/*     */   {
/* 656 */     return true;
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
/*     */   public boolean isSigned(int column)
/*     */     throws SQLException
/*     */   {
/* 671 */     Field f = getField(column);
/* 672 */     int sqlType = f.getSQLType();
/*     */     
/* 674 */     switch (sqlType) {
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 2: 
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/* 684 */       return !f.isUnsigned();
/*     */     
/*     */     case 91: 
/*     */     case 92: 
/*     */     case 93: 
/* 689 */       return false;
/*     */     }
/*     */     
/* 692 */     return false;
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
/*     */   public boolean isWritable(int column)
/*     */     throws SQLException
/*     */   {
/* 708 */     return !isReadOnly(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 717 */     StringBuffer toStringBuf = new StringBuffer();
/* 718 */     toStringBuf.append(super.toString());
/* 719 */     toStringBuf.append(" - Field level information: ");
/*     */     
/* 721 */     for (int i = 0; i < this.fields.length; i++) {
/* 722 */       toStringBuf.append("\n\t");
/* 723 */       toStringBuf.append(this.fields[i].toString());
/*     */     }
/*     */     
/* 726 */     return toStringBuf.toString();
/*     */   }
/*     */   
/*     */   static String getClassNameForJavaType(int javaType, boolean isUnsigned, int mysqlTypeIfKnown, boolean isBinaryOrBlob, boolean isOpaqueBinary, boolean treatYearAsDate)
/*     */   {
/* 731 */     switch (javaType) {
/*     */     case -7: 
/*     */     case 16: 
/* 734 */       return "java.lang.Boolean";
/*     */     
/*     */ 
/*     */     case -6: 
/* 738 */       if (isUnsigned) {
/* 739 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 742 */       return "java.lang.Integer";
/*     */     
/*     */ 
/*     */     case 5: 
/* 746 */       if (isUnsigned) {
/* 747 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 750 */       return "java.lang.Integer";
/*     */     
/*     */ 
/*     */     case 4: 
/* 754 */       if ((!isUnsigned) || (mysqlTypeIfKnown == 9))
/*     */       {
/* 756 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 759 */       return "java.lang.Long";
/*     */     
/*     */ 
/*     */     case -5: 
/* 763 */       if (!isUnsigned) {
/* 764 */         return "java.lang.Long";
/*     */       }
/*     */       
/* 767 */       return "java.math.BigInteger";
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 771 */       return "java.math.BigDecimal";
/*     */     
/*     */     case 7: 
/* 774 */       return "java.lang.Float";
/*     */     
/*     */     case 6: 
/*     */     case 8: 
/* 778 */       return "java.lang.Double";
/*     */     
/*     */     case -1: 
/*     */     case 1: 
/*     */     case 12: 
/* 783 */       if (!isOpaqueBinary) {
/* 784 */         return "java.lang.String";
/*     */       }
/*     */       
/* 787 */       return "[B";
/*     */     
/*     */ 
/*     */     case -4: 
/*     */     case -3: 
/*     */     case -2: 
/* 793 */       if (mysqlTypeIfKnown == 255)
/* 794 */         return "[B";
/* 795 */       if (isBinaryOrBlob) {
/* 796 */         return "[B";
/*     */       }
/* 798 */       return "java.lang.String";
/*     */     
/*     */ 
/*     */     case 91: 
/* 802 */       return (treatYearAsDate) || (mysqlTypeIfKnown != 13) ? "java.sql.Date" : "java.lang.Short";
/*     */     
/*     */ 
/*     */     case 92: 
/* 806 */       return "java.sql.Time";
/*     */     
/*     */     case 93: 
/* 809 */       return "java.sql.Timestamp";
/*     */     }
/*     */     
/* 812 */     return "java.lang.Object";
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/* 834 */     return iface.isInstance(this);
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
/*     */   public Object unwrap(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 855 */       return Util.cast(iface, this);
/*     */     } catch (ClassCastException cce) {
/* 857 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ResultSetMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */