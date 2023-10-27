/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
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
/*      */ public class DatabaseMetaData
/*      */   implements java.sql.DatabaseMetaData
/*      */ {
/*      */   protected static final int MAX_IDENTIFIER_LENGTH = 64;
/*      */   private static final int DEFERRABILITY = 13;
/*      */   private static final int DELETE_RULE = 10;
/*      */   private static final int FK_NAME = 11;
/*      */   private static final int FKCOLUMN_NAME = 7;
/*      */   private static final int FKTABLE_CAT = 4;
/*      */   private static final int FKTABLE_NAME = 6;
/*      */   private static final int FKTABLE_SCHEM = 5;
/*      */   private static final int KEY_SEQ = 8;
/*      */   private static final int PK_NAME = 12;
/*      */   private static final int PKCOLUMN_NAME = 3;
/*      */   private static final int PKTABLE_CAT = 0;
/*      */   private static final int PKTABLE_NAME = 2;
/*      */   private static final int PKTABLE_SCHEM = 1;
/*      */   private static final String SUPPORTS_FK = "SUPPORTS_FK";
/*      */   
/*      */   protected abstract class IteratorWithCleanup<T>
/*      */   {
/*      */     protected IteratorWithCleanup() {}
/*      */     
/*      */     abstract void close()
/*      */       throws SQLException;
/*      */     
/*      */     abstract boolean hasNext()
/*      */       throws SQLException;
/*      */     
/*      */     abstract T next()
/*      */       throws SQLException;
/*      */   }
/*      */   
/*      */   class LocalAndReferencedColumns
/*      */   {
/*      */     String constraintName;
/*      */     List<String> localColumnsList;
/*      */     String referencedCatalog;
/*      */     List<String> referencedColumnsList;
/*      */     String referencedTable;
/*      */     
/*      */     LocalAndReferencedColumns(List<String> localColumns, String refColumns, String constName, String refCatalog)
/*      */     {
/*   91 */       this.localColumnsList = localColumns;
/*   92 */       this.referencedColumnsList = refColumns;
/*   93 */       this.constraintName = constName;
/*   94 */       this.referencedTable = refTable;
/*   95 */       this.referencedCatalog = refCatalog;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ResultSetIterator extends DatabaseMetaData.IteratorWithCleanup<String> {
/*      */     int colIndex;
/*      */     ResultSet resultSet;
/*      */     
/*      */     ResultSetIterator(ResultSet rs, int index) {
/*  104 */       super();
/*  105 */       this.resultSet = rs;
/*  106 */       this.colIndex = index;
/*      */     }
/*      */     
/*      */     void close() throws SQLException {
/*  110 */       this.resultSet.close();
/*      */     }
/*      */     
/*      */     boolean hasNext() throws SQLException {
/*  114 */       return this.resultSet.next();
/*      */     }
/*      */     
/*      */     String next() throws SQLException {
/*  118 */       return this.resultSet.getObject(this.colIndex).toString();
/*      */     }
/*      */   }
/*      */   
/*      */   protected class SingleStringIterator extends DatabaseMetaData.IteratorWithCleanup<String> {
/*  123 */     boolean onFirst = true;
/*      */     String value;
/*      */     
/*      */     SingleStringIterator(String s) {
/*  127 */       super();
/*  128 */       this.value = s;
/*      */     }
/*      */     
/*      */     void close()
/*      */       throws SQLException
/*      */     {}
/*      */     
/*      */     boolean hasNext() throws SQLException
/*      */     {
/*  137 */       return this.onFirst;
/*      */     }
/*      */     
/*      */     String next() throws SQLException {
/*  141 */       this.onFirst = false;
/*  142 */       return this.value;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class TypeDescriptor
/*      */   {
/*      */     int bufferLength;
/*      */     
/*      */ 
/*      */     int charOctetLength;
/*      */     
/*      */     Integer columnSize;
/*      */     
/*      */     short dataType;
/*      */     
/*      */     Integer decimalDigits;
/*      */     
/*      */     String isNullable;
/*      */     
/*      */     int nullability;
/*      */     
/*  165 */     int numPrecRadix = 10;
/*      */     String typeName;
/*      */     
/*      */     TypeDescriptor(String typeInfo, String nullabilityInfo)
/*      */       throws SQLException
/*      */     {
/*  171 */       if (typeInfo == null) {
/*  172 */         throw SQLError.createSQLException("NULL typeinfo not supported.", "S1009", DatabaseMetaData.this.getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*  176 */       String mysqlType = "";
/*  177 */       String fullMysqlType = null;
/*      */       
/*  179 */       if (typeInfo.indexOf("(") != -1) {
/*  180 */         mysqlType = typeInfo.substring(0, typeInfo.indexOf("("));
/*      */       } else {
/*  182 */         mysqlType = typeInfo;
/*      */       }
/*      */       
/*  185 */       int indexOfUnsignedInMysqlType = StringUtils.indexOfIgnoreCase(mysqlType, "unsigned");
/*      */       
/*      */ 
/*  188 */       if (indexOfUnsignedInMysqlType != -1) {
/*  189 */         mysqlType = mysqlType.substring(0, indexOfUnsignedInMysqlType - 1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  196 */       boolean isUnsigned = false;
/*      */       
/*  198 */       if ((StringUtils.indexOfIgnoreCase(typeInfo, "unsigned") != -1) && (StringUtils.indexOfIgnoreCase(typeInfo, "set") != 0) && (StringUtils.indexOfIgnoreCase(typeInfo, "enum") != 0))
/*      */       {
/*      */ 
/*  201 */         fullMysqlType = mysqlType + " unsigned";
/*  202 */         isUnsigned = true;
/*      */       } else {
/*  204 */         fullMysqlType = mysqlType;
/*      */       }
/*      */       
/*  207 */       if (DatabaseMetaData.this.conn.getCapitalizeTypeNames()) {
/*  208 */         fullMysqlType = fullMysqlType.toUpperCase(Locale.ENGLISH);
/*      */       }
/*      */       
/*  211 */       this.dataType = ((short)MysqlDefs.mysqlToJavaType(mysqlType));
/*      */       
/*  213 */       this.typeName = fullMysqlType;
/*      */       
/*      */ 
/*      */ 
/*  217 */       if (StringUtils.startsWithIgnoreCase(typeInfo, "enum")) {
/*  218 */         String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
/*      */         
/*  220 */         StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */         
/*  222 */         int maxLength = 0;
/*      */         
/*  224 */         while (tokenizer.hasMoreTokens()) {
/*  225 */           maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */         }
/*      */         
/*      */ 
/*  229 */         this.columnSize = Integer.valueOf(maxLength);
/*  230 */         this.decimalDigits = null;
/*  231 */       } else if (StringUtils.startsWithIgnoreCase(typeInfo, "set")) {
/*  232 */         String temp = typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
/*      */         
/*  234 */         StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */         
/*  236 */         int maxLength = 0;
/*      */         
/*  238 */         int numElements = tokenizer.countTokens();
/*      */         
/*  240 */         if (numElements > 0) {
/*  241 */           maxLength += numElements - 1;
/*      */         }
/*      */         
/*  244 */         while (tokenizer.hasMoreTokens()) {
/*  245 */           String setMember = tokenizer.nextToken().trim();
/*      */           
/*  247 */           if ((setMember.startsWith("'")) && (setMember.endsWith("'")))
/*      */           {
/*  249 */             maxLength += setMember.length() - 2;
/*      */           } else {
/*  251 */             maxLength += setMember.length();
/*      */           }
/*      */         }
/*      */         
/*  255 */         this.columnSize = Integer.valueOf(maxLength);
/*  256 */         this.decimalDigits = null;
/*  257 */       } else if (typeInfo.indexOf(",") != -1)
/*      */       {
/*  259 */         this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim());
/*      */         
/*  261 */         this.decimalDigits = Integer.valueOf(typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim());
/*      */       }
/*      */       else
/*      */       {
/*  265 */         this.columnSize = null;
/*  266 */         this.decimalDigits = null;
/*      */         
/*      */ 
/*  269 */         if (((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "text") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "bit") != -1)) && (typeInfo.indexOf("(") != -1))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */           int endParenIndex = typeInfo.indexOf(")");
/*      */           
/*  278 */           if (endParenIndex == -1) {
/*  279 */             endParenIndex = typeInfo.length();
/*      */           }
/*      */           
/*  282 */           this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, endParenIndex).trim());
/*      */           
/*      */ 
/*      */ 
/*  286 */           if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (this.columnSize.intValue() == 1) && (StringUtils.startsWithIgnoreCase(typeInfo, 0, "tinyint")))
/*      */           {
/*      */ 
/*      */ 
/*  290 */             if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
/*  291 */               this.dataType = 16;
/*  292 */               this.typeName = "BOOLEAN";
/*      */             } else {
/*  294 */               this.dataType = -7;
/*  295 */               this.typeName = "BIT";
/*      */             }
/*      */           }
/*  298 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint"))
/*      */         {
/*  300 */           if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (typeInfo.indexOf("(1)") != -1)) {
/*  301 */             if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
/*  302 */               this.dataType = 16;
/*  303 */               this.typeName = "BOOLEAN";
/*      */             } else {
/*  305 */               this.dataType = -7;
/*  306 */               this.typeName = "BIT";
/*      */             }
/*      */           } else {
/*  309 */             this.columnSize = Integer.valueOf(3);
/*  310 */             this.decimalDigits = Integer.valueOf(0);
/*      */           }
/*  312 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint"))
/*      */         {
/*  314 */           this.columnSize = Integer.valueOf(5);
/*  315 */           this.decimalDigits = Integer.valueOf(0);
/*  316 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint"))
/*      */         {
/*  318 */           this.columnSize = Integer.valueOf(isUnsigned ? 8 : 7);
/*  319 */           this.decimalDigits = Integer.valueOf(0);
/*  320 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int"))
/*      */         {
/*  322 */           this.columnSize = Integer.valueOf(10);
/*  323 */           this.decimalDigits = Integer.valueOf(0);
/*  324 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer"))
/*      */         {
/*  326 */           this.columnSize = Integer.valueOf(10);
/*  327 */           this.decimalDigits = Integer.valueOf(0);
/*  328 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint"))
/*      */         {
/*  330 */           this.columnSize = Integer.valueOf(isUnsigned ? 20 : 19);
/*  331 */           this.decimalDigits = Integer.valueOf(0);
/*  332 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24"))
/*      */         {
/*  334 */           this.columnSize = Integer.valueOf(19);
/*  335 */           this.decimalDigits = Integer.valueOf(0);
/*  336 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real"))
/*      */         {
/*  338 */           this.columnSize = Integer.valueOf(12);
/*  339 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "float"))
/*      */         {
/*  341 */           this.columnSize = Integer.valueOf(12);
/*  342 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "decimal"))
/*      */         {
/*  344 */           this.columnSize = Integer.valueOf(12);
/*  345 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric"))
/*      */         {
/*  347 */           this.columnSize = Integer.valueOf(12);
/*  348 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "double"))
/*      */         {
/*  350 */           this.columnSize = Integer.valueOf(22);
/*  351 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char"))
/*      */         {
/*  353 */           this.columnSize = Integer.valueOf(1);
/*  354 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar"))
/*      */         {
/*  356 */           this.columnSize = Integer.valueOf(255);
/*  357 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp"))
/*      */         {
/*  359 */           this.columnSize = Integer.valueOf(19);
/*  360 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime"))
/*      */         {
/*  362 */           this.columnSize = Integer.valueOf(19);
/*  363 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date"))
/*      */         {
/*  365 */           this.columnSize = Integer.valueOf(10);
/*  366 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "time"))
/*      */         {
/*  368 */           this.columnSize = Integer.valueOf(8);
/*      */         }
/*  370 */         else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob"))
/*      */         {
/*  372 */           this.columnSize = Integer.valueOf(255);
/*  373 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob"))
/*      */         {
/*  375 */           this.columnSize = Integer.valueOf(65535);
/*  376 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob"))
/*      */         {
/*  378 */           this.columnSize = Integer.valueOf(16777215);
/*  379 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob"))
/*      */         {
/*  381 */           this.columnSize = Integer.valueOf(Integer.MAX_VALUE);
/*  382 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext"))
/*      */         {
/*  384 */           this.columnSize = Integer.valueOf(255);
/*  385 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "text"))
/*      */         {
/*  387 */           this.columnSize = Integer.valueOf(65535);
/*  388 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext"))
/*      */         {
/*  390 */           this.columnSize = Integer.valueOf(16777215);
/*  391 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext"))
/*      */         {
/*  393 */           this.columnSize = Integer.valueOf(Integer.MAX_VALUE);
/*  394 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum"))
/*      */         {
/*  396 */           this.columnSize = Integer.valueOf(255);
/*  397 */         } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set"))
/*      */         {
/*  399 */           this.columnSize = Integer.valueOf(255);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  405 */       this.bufferLength = MysqlIO.getMaxBuf();
/*      */       
/*      */ 
/*  408 */       this.numPrecRadix = 10;
/*      */       
/*      */ 
/*  411 */       if (nullabilityInfo != null) {
/*  412 */         if (nullabilityInfo.equals("YES")) {
/*  413 */           this.nullability = 1;
/*  414 */           this.isNullable = "YES";
/*      */         }
/*  416 */         else if (nullabilityInfo.equals("UNKNOWN")) {
/*  417 */           this.nullability = 2;
/*  418 */           this.isNullable = "";
/*      */         }
/*      */         else
/*      */         {
/*  422 */           this.nullability = 0;
/*  423 */           this.isNullable = "NO";
/*      */         }
/*      */       } else {
/*  426 */         this.nullability = 0;
/*  427 */         this.isNullable = "NO";
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected class IndexMetaDataKey
/*      */     implements Comparable<IndexMetaDataKey>
/*      */   {
/*      */     Boolean columnNonUnique;
/*      */     Short columnType;
/*      */     String columnIndexName;
/*      */     Short columnOrdinalPosition;
/*      */     
/*      */     IndexMetaDataKey(boolean columnNonUnique, short columnType, String columnIndexName, short columnOrdinalPosition)
/*      */     {
/*  443 */       this.columnNonUnique = Boolean.valueOf(columnNonUnique);
/*  444 */       this.columnType = Short.valueOf(columnType);
/*  445 */       this.columnIndexName = columnIndexName;
/*  446 */       this.columnOrdinalPosition = Short.valueOf(columnOrdinalPosition);
/*      */     }
/*      */     
/*      */     public int compareTo(IndexMetaDataKey indexInfoKey)
/*      */     {
/*      */       int compareResult;
/*  452 */       if ((compareResult = this.columnNonUnique.compareTo(indexInfoKey.columnNonUnique)) != 0) {
/*  453 */         return compareResult;
/*      */       }
/*  455 */       if ((compareResult = this.columnType.compareTo(indexInfoKey.columnType)) != 0) {
/*  456 */         return compareResult;
/*      */       }
/*  458 */       if ((compareResult = this.columnIndexName.compareTo(indexInfoKey.columnIndexName)) != 0) {
/*  459 */         return compareResult;
/*      */       }
/*  461 */       return this.columnOrdinalPosition.compareTo(indexInfoKey.columnOrdinalPosition);
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/*  466 */       if (obj == null) {
/*  467 */         return false;
/*      */       }
/*      */       
/*  470 */       if (obj == this) {
/*  471 */         return true;
/*      */       }
/*      */       
/*  474 */       if (!(obj instanceof IndexMetaDataKey)) {
/*  475 */         return false;
/*      */       }
/*  477 */       return compareTo((IndexMetaDataKey)obj) == 0;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class TableMetaDataKey
/*      */     implements Comparable<TableMetaDataKey>
/*      */   {
/*      */     String tableType;
/*      */     String tableCat;
/*      */     String tableSchem;
/*      */     String tableName;
/*      */     
/*      */     TableMetaDataKey(String tableType, String tableCat, String tableSchem, String tableName)
/*      */     {
/*  491 */       this.tableType = (tableType == null ? "" : tableType);
/*  492 */       this.tableCat = (tableCat == null ? "" : tableCat);
/*  493 */       this.tableSchem = (tableSchem == null ? "" : tableSchem);
/*  494 */       this.tableName = (tableName == null ? "" : tableName);
/*      */     }
/*      */     
/*      */     public int compareTo(TableMetaDataKey tablesKey)
/*      */     {
/*      */       int compareResult;
/*  500 */       if ((compareResult = this.tableType.compareTo(tablesKey.tableType)) != 0) {
/*  501 */         return compareResult;
/*      */       }
/*  503 */       if ((compareResult = this.tableCat.compareTo(tablesKey.tableCat)) != 0) {
/*  504 */         return compareResult;
/*      */       }
/*  506 */       if ((compareResult = this.tableSchem.compareTo(tablesKey.tableSchem)) != 0) {
/*  507 */         return compareResult;
/*      */       }
/*  509 */       return this.tableName.compareTo(tablesKey.tableName);
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/*  514 */       if (obj == null) {
/*  515 */         return false;
/*      */       }
/*      */       
/*  518 */       if (obj == this) {
/*  519 */         return true;
/*      */       }
/*      */       
/*  522 */       if (!(obj instanceof TableMetaDataKey)) {
/*  523 */         return false;
/*      */       }
/*  525 */       return compareTo((TableMetaDataKey)obj) == 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected class ComparableWrapper<K,  extends Comparable<? super K>, V>
/*      */     implements Comparable<ComparableWrapper<K, V>>
/*      */   {
/*      */     K key;
/*      */     V value;
/*      */     
/*      */     public ComparableWrapper(V key)
/*      */     {
/*  538 */       this.key = key;
/*  539 */       this.value = value;
/*      */     }
/*      */     
/*      */     public K getKey() {
/*  543 */       return (K)this.key;
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  547 */       return (V)this.value;
/*      */     }
/*      */     
/*      */     public int compareTo(ComparableWrapper<K, V> other) {
/*  551 */       return ((Comparable)getKey()).compareTo(other.getKey());
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/*  556 */       if (obj == null) {
/*  557 */         return false;
/*      */       }
/*      */       
/*  560 */       if (obj == this) {
/*  561 */         return true;
/*      */       }
/*      */       
/*  564 */       if (!(obj instanceof ComparableWrapper)) {
/*  565 */         return false;
/*      */       }
/*      */       
/*  568 */       Object otherKey = ((ComparableWrapper)obj).getKey();
/*  569 */       return this.key.equals(otherKey);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  574 */       return "{KEY:" + this.key + "; VALUE:" + this.value + "}";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static enum TableType
/*      */   {
/*  582 */     LOCAL_TEMPORARY("LOCAL TEMPORARY"),  SYSTEM_TABLE("SYSTEM TABLE"),  SYSTEM_VIEW("SYSTEM VIEW"),  TABLE("TABLE", new String[] { "BASE TABLE" }), 
/*  583 */     VIEW("VIEW"),  UNKNOWN("UNKNOWN");
/*      */     
/*      */     private String name;
/*      */     private byte[] nameAsBytes;
/*      */     private String[] synonyms;
/*      */     
/*      */     private TableType(String tableTypeName) {
/*  590 */       this(tableTypeName, null);
/*      */     }
/*      */     
/*      */     private TableType(String tableTypeName, String[] tableTypeSynonyms) {
/*  594 */       this.name = tableTypeName;
/*  595 */       this.nameAsBytes = tableTypeName.getBytes();
/*  596 */       this.synonyms = tableTypeSynonyms;
/*      */     }
/*      */     
/*      */     String getName() {
/*  600 */       return this.name;
/*      */     }
/*      */     
/*      */     byte[] asBytes() {
/*  604 */       return this.nameAsBytes;
/*      */     }
/*      */     
/*      */     boolean equalsTo(String tableTypeName) {
/*  608 */       return this.name.equalsIgnoreCase(tableTypeName);
/*      */     }
/*      */     
/*      */     static TableType getTableTypeEqualTo(String tableTypeName) {
/*  612 */       for (TableType tableType : ) {
/*  613 */         if (tableType.equalsTo(tableTypeName)) {
/*  614 */           return tableType;
/*      */         }
/*      */       }
/*  617 */       return UNKNOWN;
/*      */     }
/*      */     
/*      */     boolean compliesWith(String tableTypeName) {
/*  621 */       if (equalsTo(tableTypeName)) {
/*  622 */         return true;
/*      */       }
/*  624 */       if (this.synonyms != null) {
/*  625 */         for (String synonym : this.synonyms) {
/*  626 */           if (synonym.equalsIgnoreCase(tableTypeName)) {
/*  627 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*  631 */       return false;
/*      */     }
/*      */     
/*      */     static TableType getTableTypeCompliantWith(String tableTypeName) {
/*  635 */       for (TableType tableType : ) {
/*  636 */         if (tableType.compliesWith(tableTypeName)) {
/*  637 */           return tableType;
/*      */         }
/*      */       }
/*  640 */       return UNKNOWN;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static enum ProcedureType
/*      */   {
/*  648 */     PROCEDURE,  FUNCTION;
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
/*      */     private ProcedureType() {}
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
/*  686 */   protected static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
/*      */   
/*  688 */   protected static final byte[] SYSTEM_TABLE_AS_BYTES = "SYSTEM TABLE".getBytes();
/*      */   
/*      */   private static final int UPDATE_RULE = 9;
/*      */   
/*  692 */   protected static final byte[] VIEW_AS_BYTES = "VIEW".getBytes();
/*      */   
/*      */   private static final Constructor<?> JDBC_4_DBMD_SHOW_CTOR;
/*      */   private static final Constructor<?> JDBC_4_DBMD_IS_CTOR;
/*      */   
/*      */   static
/*      */   {
/*  699 */     if (Util.isJdbc4()) {
/*      */       try {
/*  701 */         JDBC_4_DBMD_SHOW_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaData").getConstructor(new Class[] { MySQLConnection.class, String.class });
/*      */         
/*      */ 
/*      */ 
/*  705 */         JDBC_4_DBMD_IS_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema").getConstructor(new Class[] { MySQLConnection.class, String.class });
/*      */ 
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*      */ 
/*  711 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  713 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  715 */         throw new RuntimeException(e);
/*      */       }
/*      */     } else {
/*  718 */       JDBC_4_DBMD_IS_CTOR = null;
/*  719 */       JDBC_4_DBMD_SHOW_CTOR = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  724 */   private static final String[] MYSQL_KEYWORDS = { "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GET", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NONBLOCKING", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PARTITION", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL" };
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
/*  752 */   private static final String[] SQL92_KEYWORDS = { "ABSOLUTE", "ACTION", "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIT", "BIT_LENGTH", "BOTH", "BY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOSE", "COALESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONTINUE", "CONVERT", "CORRESPONDING", "CREATE", "CROSS", "CURRENT", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DESCRIBE", "DESCRIPTOR", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DOMAIN", "DOUBLE", "DROP", "ELSE", "END", "END-EXEC", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FULL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HAVING", "HOUR", "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INPUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LAST", "LEADING", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOWER", "MATCH", "MAX", "MIN", "MINUTE", "MODULE", "MONTH", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NOT", "NULL", "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARTIAL", "POSITION", "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES", "RELATIVE", "RESTRICT", "REVOKE", "RIGHT", "ROLLBACK", "ROWS", "SCHEMA", "SCROLL", "SECOND", "SECTION", "SELECT", "SESSION", "SESSION_USER", "SET", "SIZE", "SMALLINT", "SOME", "SPACE", "SQL", "SQLCODE", "SQLERROR", "SQLSTATE", "SUBSTRING", "SUM", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIM", "TRUE", "UNION", "UNIQUE", "UNKNOWN", "UPDATE", "UPPER", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN", "WHENEVER", "WHERE", "WITH", "WORK", "WRITE", "YEAR", "ZONE" };
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
/*  778 */   private static final String[] SQL2003_KEYWORDS = { "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL", "CALLED", "CASCADED", "CASE", "CAST", "CHAR", "CHARACTER", "CHECK", "CLOB", "CLOSE", "COLLATE", "COLUMN", "COMMIT", "CONNECT", "CONSTRAINT", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", "DISTINCT", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC", "ESCAPE", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXTERNAL", "FALSE", "FETCH", "FILTER", "FLOAT", "FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD", "HOUR", "IDENTITY", "IMMEDIATE", "IN", "INDICATOR", "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEFT", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "MATCH", "MEMBER", "MERGE", "METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NO", "NONE", "NOT", "NULL", "NUMERIC", "OF", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OUTPUT", "OVER", "OVERLAPS", "PARAMETER", "PARTITION", "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "RELEASE", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "SAVEPOINT", "SCROLL", "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATIC", "SUBMULTISET", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATION", "TREAT", "TRIGGER", "TRUE", "UESCAPE", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "UPPER", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "YEAR" };
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
/*  805 */   private static volatile String mysqlKeywords = null;
/*      */   
/*      */ 
/*      */   protected MySQLConnection conn;
/*      */   
/*      */ 
/*  811 */   protected String database = null;
/*      */   
/*      */ 
/*  814 */   protected String quotedId = null;
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */   
/*      */ 
/*      */   protected static DatabaseMetaData getInstance(MySQLConnection connToSet, String databaseToSet, boolean checkForInfoSchema)
/*      */     throws SQLException
/*      */   {
/*  822 */     if (!Util.isJdbc4()) {
/*  823 */       if ((checkForInfoSchema) && (connToSet != null) && (connToSet.getUseInformationSchema()) && (connToSet.versionMeetsMinimum(5, 0, 7)))
/*      */       {
/*      */ 
/*  826 */         return new DatabaseMetaDataUsingInfoSchema(connToSet, databaseToSet);
/*      */       }
/*      */       
/*      */ 
/*  830 */       return new DatabaseMetaData(connToSet, databaseToSet);
/*      */     }
/*      */     
/*  833 */     if ((checkForInfoSchema) && (connToSet != null) && (connToSet.getUseInformationSchema()) && (connToSet.versionMeetsMinimum(5, 0, 7)))
/*      */     {
/*      */ 
/*      */ 
/*  837 */       return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_IS_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  842 */     return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_SHOW_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
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
/*      */   protected DatabaseMetaData(MySQLConnection connToSet, String databaseToSet)
/*      */   {
/*  855 */     this.conn = connToSet;
/*  856 */     this.database = databaseToSet;
/*  857 */     this.exceptionInterceptor = this.conn.getExceptionInterceptor();
/*      */     try
/*      */     {
/*  860 */       this.quotedId = (this.conn.supportsQuotedIdentifiers() ? getIdentifierQuoteString() : "");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*      */ 
/*  866 */       AssertionFailedException.shouldNotHappen(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allProceduresAreCallable()
/*      */     throws SQLException
/*      */   {
/*  879 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allTablesAreSelectable()
/*      */     throws SQLException
/*      */   {
/*  890 */     return false;
/*      */   }
/*      */   
/*      */   private ResultSet buildResultSet(Field[] fields, ArrayList<ResultSetRow> rows) throws SQLException
/*      */   {
/*  895 */     return buildResultSet(fields, rows, this.conn);
/*      */   }
/*      */   
/*      */   static ResultSet buildResultSet(Field[] fields, ArrayList<ResultSetRow> rows, MySQLConnection c) throws SQLException
/*      */   {
/*  900 */     int fieldsLength = fields.length;
/*      */     
/*  902 */     for (int i = 0; i < fieldsLength; i++) {
/*  903 */       int jdbcType = fields[i].getSQLType();
/*      */       
/*  905 */       switch (jdbcType) {
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/*  909 */         fields[i].setCharacterSet(c.getCharacterSetMetadata());
/*  910 */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*  915 */       fields[i].setConnection(c);
/*  916 */       fields[i].setUseOldNameMetadata(true);
/*      */     }
/*      */     
/*  919 */     return ResultSetImpl.getInstance(c.getCatalog(), fields, new RowDataStatic(rows), c, null, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void convertToJdbcFunctionList(String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, List<ComparableWrapper<String, ResultSetRow>> procedureRows, int nameIndex, Field[] fields)
/*      */     throws SQLException
/*      */   {
/*  927 */     while (proceduresRs.next()) {
/*  928 */       boolean shouldAdd = true;
/*      */       
/*  930 */       if (needsClientFiltering) {
/*  931 */         shouldAdd = false;
/*      */         
/*  933 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  935 */         if ((db == null) && (procDb == null)) {
/*  936 */           shouldAdd = true;
/*  937 */         } else if ((db != null) && (db.equals(procDb))) {
/*  938 */           shouldAdd = true;
/*      */         }
/*      */       }
/*      */       
/*  942 */       if (shouldAdd) {
/*  943 */         String functionName = proceduresRs.getString(nameIndex);
/*      */         
/*  945 */         byte[][] rowData = (byte[][])null;
/*      */         
/*  947 */         if ((fields != null) && (fields.length == 9))
/*      */         {
/*  949 */           rowData = new byte[9][];
/*  950 */           rowData[0] = (catalog == null ? null : s2b(catalog));
/*  951 */           rowData[1] = null;
/*  952 */           rowData[2] = s2b(functionName);
/*  953 */           rowData[3] = null;
/*  954 */           rowData[4] = null;
/*  955 */           rowData[5] = null;
/*  956 */           rowData[6] = s2b(proceduresRs.getString("comment"));
/*  957 */           rowData[7] = s2b(Integer.toString(2));
/*  958 */           rowData[8] = s2b(functionName);
/*      */         }
/*      */         else {
/*  961 */           rowData = new byte[6][];
/*      */           
/*  963 */           rowData[0] = (catalog == null ? null : s2b(catalog));
/*  964 */           rowData[1] = null;
/*  965 */           rowData[2] = s2b(functionName);
/*  966 */           rowData[3] = s2b(proceduresRs.getString("comment"));
/*  967 */           rowData[4] = s2b(Integer.toString(getJDBC4FunctionNoTableConstant()));
/*  968 */           rowData[5] = s2b(functionName);
/*      */         }
/*      */         
/*  971 */         procedureRows.add(new ComparableWrapper(functionName, new ByteArrayRow(rowData, getExceptionInterceptor())));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getJDBC4FunctionNoTableConstant()
/*      */   {
/*  984 */     return 0;
/*      */   }
/*      */   
/*      */   protected void convertToJdbcProcedureList(boolean fromSelect, String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, List<ComparableWrapper<String, ResultSetRow>> procedureRows, int nameIndex)
/*      */     throws SQLException
/*      */   {
/*  990 */     while (proceduresRs.next()) {
/*  991 */       boolean shouldAdd = true;
/*      */       
/*  993 */       if (needsClientFiltering) {
/*  994 */         shouldAdd = false;
/*      */         
/*  996 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  998 */         if ((db == null) && (procDb == null)) {
/*  999 */           shouldAdd = true;
/* 1000 */         } else if ((db != null) && (db.equals(procDb))) {
/* 1001 */           shouldAdd = true;
/*      */         }
/*      */       }
/*      */       
/* 1005 */       if (shouldAdd) {
/* 1006 */         String procedureName = proceduresRs.getString(nameIndex);
/* 1007 */         byte[][] rowData = new byte[9][];
/* 1008 */         rowData[0] = (catalog == null ? null : s2b(catalog));
/* 1009 */         rowData[1] = null;
/* 1010 */         rowData[2] = s2b(procedureName);
/* 1011 */         rowData[3] = null;
/* 1012 */         rowData[4] = null;
/* 1013 */         rowData[5] = null;
/* 1014 */         rowData[6] = s2b(proceduresRs.getString("comment"));
/*      */         
/* 1016 */         boolean isFunction = fromSelect ? "FUNCTION".equalsIgnoreCase(proceduresRs.getString("type")) : false;
/*      */         
/*      */ 
/* 1019 */         rowData[7] = s2b(isFunction ? Integer.toString(2) : Integer.toString(1));
/*      */         
/*      */ 
/*      */ 
/* 1023 */         rowData[8] = s2b(procedureName);
/*      */         
/* 1025 */         procedureRows.add(new ComparableWrapper(procedureName, new ByteArrayRow(rowData, getExceptionInterceptor())));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ResultSetRow convertTypeDescriptorToProcedureRow(byte[] procNameAsBytes, byte[] procCatAsBytes, String paramName, boolean isOutParam, boolean isInParam, boolean isReturnParam, TypeDescriptor typeDesc, boolean forGetFunctionColumns, int ordinal)
/*      */     throws SQLException
/*      */   {
/* 1037 */     byte[][] row = forGetFunctionColumns ? new byte[17][] : new byte[20][];
/* 1038 */     row[0] = procCatAsBytes;
/* 1039 */     row[1] = null;
/* 1040 */     row[2] = procNameAsBytes;
/* 1041 */     row[3] = s2b(paramName);
/* 1042 */     row[4] = s2b(String.valueOf(getColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns)));
/* 1043 */     row[5] = s2b(Short.toString(typeDesc.dataType));
/* 1044 */     row[6] = s2b(typeDesc.typeName);
/* 1045 */     row[7] = (typeDesc.columnSize == null ? null : s2b(typeDesc.columnSize.toString()));
/* 1046 */     row[8] = row[7];
/* 1047 */     row[9] = (typeDesc.decimalDigits == null ? null : s2b(typeDesc.decimalDigits.toString()));
/* 1048 */     row[10] = s2b(Integer.toString(typeDesc.numPrecRadix));
/*      */     
/* 1050 */     switch (typeDesc.nullability) {
/*      */     case 0: 
/* 1052 */       row[11] = s2b(String.valueOf(0));
/* 1053 */       break;
/*      */     
/*      */     case 1: 
/* 1056 */       row[11] = s2b(String.valueOf(1));
/* 1057 */       break;
/*      */     
/*      */     case 2: 
/* 1060 */       row[11] = s2b(String.valueOf(2));
/* 1061 */       break;
/*      */     
/*      */     default: 
/* 1064 */       throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 1069 */     row[12] = null;
/*      */     
/* 1071 */     if (forGetFunctionColumns)
/*      */     {
/* 1073 */       row[13] = null;
/*      */       
/*      */ 
/* 1076 */       row[14] = s2b(String.valueOf(ordinal));
/*      */       
/*      */ 
/* 1079 */       row[15] = s2b(typeDesc.isNullable);
/*      */       
/*      */ 
/* 1082 */       row[16] = procNameAsBytes;
/*      */     }
/*      */     else {
/* 1085 */       row[13] = null;
/*      */       
/*      */ 
/* 1088 */       row[14] = null;
/*      */       
/*      */ 
/* 1091 */       row[15] = null;
/*      */       
/*      */ 
/* 1094 */       row[16] = null;
/*      */       
/*      */ 
/* 1097 */       row[17] = s2b(String.valueOf(ordinal));
/*      */       
/*      */ 
/* 1100 */       row[18] = s2b(typeDesc.isNullable);
/*      */       
/*      */ 
/* 1103 */       row[19] = procNameAsBytes;
/*      */     }
/*      */     
/* 1106 */     return new ByteArrayRow(row, getExceptionInterceptor());
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
/*      */   protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns)
/*      */   {
/* 1126 */     if ((isInParam) && (isOutParam))
/* 1127 */       return 2;
/* 1128 */     if (isInParam)
/* 1129 */       return 1;
/* 1130 */     if (isOutParam)
/* 1131 */       return 4;
/* 1132 */     if (isReturnParam) {
/* 1133 */       return 5;
/*      */     }
/* 1135 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected ExceptionInterceptor getExceptionInterceptor()
/*      */   {
/* 1142 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataDefinitionCausesTransactionCommit()
/*      */     throws SQLException
/*      */   {
/* 1154 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataDefinitionIgnoredInTransactions()
/*      */     throws SQLException
/*      */   {
/* 1165 */     return false;
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
/*      */   public boolean deletesAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/* 1180 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean doesMaxRowSizeIncludeBlobs()
/*      */     throws SQLException
/*      */   {
/* 1193 */     return true;
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
/*      */   public List<ResultSetRow> extractForeignKeyForTable(ArrayList<ResultSetRow> rows, ResultSet rs, String catalog)
/*      */     throws SQLException
/*      */   {
/* 1211 */     byte[][] row = new byte[3][];
/* 1212 */     row[0] = rs.getBytes(1);
/* 1213 */     row[1] = s2b("SUPPORTS_FK");
/*      */     
/* 1215 */     String createTableString = rs.getString(2);
/* 1216 */     StringTokenizer lineTokenizer = new StringTokenizer(createTableString, "\n");
/*      */     
/* 1218 */     StringBuffer commentBuf = new StringBuffer("comment; ");
/* 1219 */     boolean firstTime = true;
/*      */     
/* 1221 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1223 */     if (quoteChar == null) {
/* 1224 */       quoteChar = "`";
/*      */     }
/*      */     
/* 1227 */     while (lineTokenizer.hasMoreTokens()) {
/* 1228 */       String line = lineTokenizer.nextToken().trim();
/*      */       
/* 1230 */       String constraintName = null;
/*      */       
/* 1232 */       if (StringUtils.startsWithIgnoreCase(line, "CONSTRAINT")) {
/* 1233 */         boolean usingBackTicks = true;
/* 1234 */         int beginPos = StringUtils.indexOfQuoteDoubleAware(line, quoteChar, 0);
/*      */         
/* 1236 */         if (beginPos == -1) {
/* 1237 */           beginPos = line.indexOf("\"");
/* 1238 */           usingBackTicks = false;
/*      */         }
/*      */         
/* 1241 */         if (beginPos != -1) {
/* 1242 */           int endPos = -1;
/*      */           
/* 1244 */           if (usingBackTicks) {
/* 1245 */             endPos = StringUtils.indexOfQuoteDoubleAware(line, quoteChar, beginPos + 1);
/*      */           } else {
/* 1247 */             endPos = StringUtils.indexOfQuoteDoubleAware(line, "\"", beginPos + 1);
/*      */           }
/*      */           
/* 1250 */           if (endPos != -1) {
/* 1251 */             constraintName = line.substring(beginPos + 1, endPos);
/* 1252 */             line = line.substring(endPos + 1, line.length()).trim();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1258 */       if (line.startsWith("FOREIGN KEY")) {
/* 1259 */         if (line.endsWith(",")) {
/* 1260 */           line = line.substring(0, line.length() - 1);
/*      */         }
/*      */         
/* 1263 */         char quote = this.quotedId.charAt(0);
/*      */         
/* 1265 */         int indexOfFK = line.indexOf("FOREIGN KEY");
/*      */         
/* 1267 */         String localColumnName = null;
/* 1268 */         String referencedCatalogName = StringUtils.quoteIdentifier(catalog, this.conn.getPedantic());
/* 1269 */         String referencedTableName = null;
/* 1270 */         String referencedColumnName = null;
/*      */         
/*      */ 
/* 1273 */         if (indexOfFK != -1) {
/* 1274 */           int afterFk = indexOfFK + "FOREIGN KEY".length();
/*      */           
/* 1276 */           int indexOfRef = StringUtils.indexOfIgnoreCaseRespectQuotes(afterFk, line, "REFERENCES", quote, true);
/*      */           
/* 1278 */           if (indexOfRef != -1)
/*      */           {
/* 1280 */             int indexOfParenOpen = line.indexOf('(', afterFk);
/* 1281 */             int indexOfParenClose = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfParenOpen, line, ")", quote, true);
/*      */             
/* 1283 */             if ((indexOfParenOpen != -1) && (indexOfParenClose == -1)) {}
/*      */             
/*      */ 
/*      */ 
/* 1287 */             localColumnName = line.substring(indexOfParenOpen + 1, indexOfParenClose);
/*      */             
/* 1289 */             int afterRef = indexOfRef + "REFERENCES".length();
/*      */             
/* 1291 */             int referencedColumnBegin = StringUtils.indexOfIgnoreCaseRespectQuotes(afterRef, line, "(", quote, true);
/*      */             
/* 1293 */             if (referencedColumnBegin != -1) {
/* 1294 */               referencedTableName = line.substring(afterRef, referencedColumnBegin);
/*      */               
/* 1296 */               int referencedColumnEnd = StringUtils.indexOfIgnoreCaseRespectQuotes(referencedColumnBegin + 1, line, ")", quote, true);
/*      */               
/* 1298 */               if (referencedColumnEnd != -1) {
/* 1299 */                 referencedColumnName = line.substring(referencedColumnBegin + 1, referencedColumnEnd);
/*      */               }
/*      */               
/* 1302 */               int indexOfCatalogSep = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referencedTableName, ".", quote, true);
/*      */               
/* 1304 */               if (indexOfCatalogSep != -1) {
/* 1305 */                 referencedCatalogName = referencedTableName.substring(0, indexOfCatalogSep);
/* 1306 */                 referencedTableName = referencedTableName.substring(indexOfCatalogSep + 1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1313 */         if (!firstTime) {
/* 1314 */           commentBuf.append("; ");
/*      */         } else {
/* 1316 */           firstTime = false;
/*      */         }
/*      */         
/* 1319 */         if (constraintName != null) {
/* 1320 */           commentBuf.append(constraintName);
/*      */         } else {
/* 1322 */           commentBuf.append("not_available");
/*      */         }
/*      */         
/* 1325 */         commentBuf.append("(");
/* 1326 */         commentBuf.append(localColumnName);
/* 1327 */         commentBuf.append(") REFER ");
/* 1328 */         commentBuf.append(referencedCatalogName);
/* 1329 */         commentBuf.append("/");
/* 1330 */         commentBuf.append(referencedTableName);
/* 1331 */         commentBuf.append("(");
/* 1332 */         commentBuf.append(referencedColumnName);
/* 1333 */         commentBuf.append(")");
/*      */         
/* 1335 */         int lastParenIndex = line.lastIndexOf(")");
/*      */         
/* 1337 */         if (lastParenIndex != line.length() - 1) {
/* 1338 */           String cascadeOptions = line.substring(lastParenIndex + 1);
/*      */           
/* 1340 */           commentBuf.append(" ");
/* 1341 */           commentBuf.append(cascadeOptions);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1346 */     row[2] = s2b(commentBuf.toString());
/* 1347 */     rows.add(new ByteArrayRow(row, getExceptionInterceptor()));
/*      */     
/* 1349 */     return rows;
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
/*      */   public ResultSet extractForeignKeyFromCreateTable(String catalog, String tableName)
/*      */     throws SQLException
/*      */   {
/* 1370 */     ArrayList<String> tableList = new ArrayList();
/* 1371 */     ResultSet rs = null;
/* 1372 */     java.sql.Statement stmt = null;
/*      */     
/* 1374 */     if (tableName != null) {
/* 1375 */       tableList.add(tableName);
/*      */     } else {
/*      */       try {
/* 1378 */         rs = getTables(catalog, "", "%", new String[] { "TABLE" });
/*      */         
/* 1380 */         while (rs.next()) {
/* 1381 */           tableList.add(rs.getString("TABLE_NAME"));
/*      */         }
/*      */       } finally {
/* 1384 */         if (rs != null) {
/* 1385 */           rs.close();
/*      */         }
/*      */         
/* 1388 */         rs = null;
/*      */       }
/*      */     }
/*      */     
/* 1392 */     Object rows = new ArrayList();
/* 1393 */     Field[] fields = new Field[3];
/* 1394 */     fields[0] = new Field("", "Name", 1, Integer.MAX_VALUE);
/* 1395 */     fields[1] = new Field("", "Type", 1, 255);
/* 1396 */     fields[2] = new Field("", "Comment", 1, Integer.MAX_VALUE);
/*      */     
/* 1398 */     int numTables = tableList.size();
/* 1399 */     stmt = this.conn.getMetadataSafeStatement();
/*      */     
/* 1401 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1403 */     if (quoteChar == null) {
/* 1404 */       quoteChar = "`";
/*      */     }
/*      */     try
/*      */     {
/* 1408 */       for (int i = 0; i < numTables; i++) {
/* 1409 */         String tableToExtract = (String)tableList.get(i);
/*      */         
/* 1411 */         String query = "SHOW CREATE TABLE " + StringUtils.quoteIdentifier(catalog, this.conn.getPedantic()) + "." + StringUtils.quoteIdentifier(tableToExtract, this.conn.getPedantic());
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1416 */           rs = stmt.executeQuery(query);
/*      */         }
/*      */         catch (SQLException sqlEx) {
/* 1419 */           String sqlState = sqlEx.getSQLState();
/*      */           
/* 1421 */           if ((!"42S02".equals(sqlState)) && (sqlEx.getErrorCode() != 1146))
/*      */           {
/* 1423 */             throw sqlEx;
/*      */           }
/*      */           
/* 1426 */           continue;
/*      */         }
/*      */         
/* 1429 */         while (rs.next()) {
/* 1430 */           extractForeignKeyForTable((ArrayList)rows, rs, catalog);
/*      */         }
/*      */       }
/*      */     } finally {
/* 1434 */       if (rs != null) {
/* 1435 */         rs.close();
/*      */       }
/*      */       
/* 1438 */       rs = null;
/*      */       
/* 1440 */       if (stmt != null) {
/* 1441 */         stmt.close();
/*      */       }
/*      */       
/* 1444 */       stmt = null;
/*      */     }
/*      */     
/* 1447 */     return buildResultSet(fields, (ArrayList)rows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3)
/*      */     throws SQLException
/*      */   {
/* 1455 */     Field[] fields = new Field[21];
/* 1456 */     fields[0] = new Field("", "TYPE_CAT", 1, 32);
/* 1457 */     fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
/* 1458 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 1459 */     fields[3] = new Field("", "ATTR_NAME", 1, 32);
/* 1460 */     fields[4] = new Field("", "DATA_TYPE", 5, 32);
/* 1461 */     fields[5] = new Field("", "ATTR_TYPE_NAME", 1, 32);
/* 1462 */     fields[6] = new Field("", "ATTR_SIZE", 4, 32);
/* 1463 */     fields[7] = new Field("", "DECIMAL_DIGITS", 4, 32);
/* 1464 */     fields[8] = new Field("", "NUM_PREC_RADIX", 4, 32);
/* 1465 */     fields[9] = new Field("", "NULLABLE ", 4, 32);
/* 1466 */     fields[10] = new Field("", "REMARKS", 1, 32);
/* 1467 */     fields[11] = new Field("", "ATTR_DEF", 1, 32);
/* 1468 */     fields[12] = new Field("", "SQL_DATA_TYPE", 4, 32);
/* 1469 */     fields[13] = new Field("", "SQL_DATETIME_SUB", 4, 32);
/* 1470 */     fields[14] = new Field("", "CHAR_OCTET_LENGTH", 4, 32);
/* 1471 */     fields[15] = new Field("", "ORDINAL_POSITION", 4, 32);
/* 1472 */     fields[16] = new Field("", "IS_NULLABLE", 1, 32);
/* 1473 */     fields[17] = new Field("", "SCOPE_CATALOG", 1, 32);
/* 1474 */     fields[18] = new Field("", "SCOPE_SCHEMA", 1, 32);
/* 1475 */     fields[19] = new Field("", "SCOPE_TABLE", 1, 32);
/* 1476 */     fields[20] = new Field("", "SOURCE_DATA_TYPE", 5, 32);
/*      */     
/* 1478 */     return buildResultSet(fields, new ArrayList());
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
/*      */   public ResultSet getBestRowIdentifier(String catalog, String schema, final String table, int scope, boolean nullable)
/*      */     throws SQLException
/*      */   {
/* 1529 */     if (table == null) {
/* 1530 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 1534 */     Field[] fields = new Field[8];
/* 1535 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 1536 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 1537 */     fields[2] = new Field("", "DATA_TYPE", 4, 32);
/* 1538 */     fields[3] = new Field("", "TYPE_NAME", 1, 32);
/* 1539 */     fields[4] = new Field("", "COLUMN_SIZE", 4, 10);
/* 1540 */     fields[5] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 1541 */     fields[6] = new Field("", "DECIMAL_DIGITS", 5, 10);
/* 1542 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 1544 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/* 1545 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 1549 */       new IterateBlock(getCatalogIterator(catalog)) {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 1551 */           ResultSet results = null;
/*      */           try
/*      */           {
/* 1554 */             StringBuffer queryBuf = new StringBuffer("SHOW COLUMNS FROM ");
/*      */             
/* 1556 */             queryBuf.append(StringUtils.quoteIdentifier(table, DatabaseMetaData.this.conn.getPedantic()));
/* 1557 */             queryBuf.append(" FROM ");
/* 1558 */             queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */             
/* 1560 */             results = stmt.executeQuery(queryBuf.toString());
/*      */             
/* 1562 */             while (results.next()) {
/* 1563 */               String keyType = results.getString("Key");
/*      */               
/* 1565 */               if ((keyType != null) && 
/* 1566 */                 (StringUtils.startsWithIgnoreCase(keyType, "PRI")))
/*      */               {
/* 1568 */                 byte[][] rowVal = new byte[8][];
/* 1569 */                 rowVal[0] = Integer.toString(2).getBytes();
/*      */                 
/*      */ 
/*      */ 
/* 1573 */                 rowVal[1] = results.getBytes("Field");
/*      */                 
/* 1575 */                 String type = results.getString("Type");
/* 1576 */                 int size = MysqlIO.getMaxBuf();
/* 1577 */                 int decimals = 0;
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1582 */                 if (type.indexOf("enum") != -1) {
/* 1583 */                   String temp = type.substring(type.indexOf("("), type.indexOf(")"));
/*      */                   
/*      */ 
/* 1586 */                   StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */                   
/* 1588 */                   int maxLength = 0;
/*      */                   
/* 1590 */                   while (tokenizer.hasMoreTokens()) {
/* 1591 */                     maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/* 1596 */                   size = maxLength;
/* 1597 */                   decimals = 0;
/* 1598 */                   type = "enum";
/* 1599 */                 } else if (type.indexOf("(") != -1) {
/* 1600 */                   if (type.indexOf(",") != -1) {
/* 1601 */                     size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(",")));
/*      */                     
/*      */ 
/*      */ 
/* 1605 */                     decimals = Integer.parseInt(type.substring(type.indexOf(",") + 1, type.indexOf(")")));
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/* 1610 */                     size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/* 1616 */                   type = type.substring(0, type.indexOf("("));
/*      */                 }
/*      */                 
/*      */ 
/* 1620 */                 rowVal[2] = DatabaseMetaData.this.s2b(String.valueOf(MysqlDefs.mysqlToJavaType(type)));
/*      */                 
/* 1622 */                 rowVal[3] = DatabaseMetaData.this.s2b(type);
/* 1623 */                 rowVal[4] = Integer.toString(size + decimals).getBytes();
/*      */                 
/* 1625 */                 rowVal[5] = Integer.toString(size + decimals).getBytes();
/*      */                 
/* 1627 */                 rowVal[6] = Integer.toString(decimals).getBytes();
/*      */                 
/* 1629 */                 rowVal[7] = Integer.toString(1).getBytes();
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1634 */                 rows.add(new ByteArrayRow(rowVal, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (SQLException sqlEx) {
/* 1639 */             if (!"42S02".equals(sqlEx.getSQLState())) {
/* 1640 */               throw sqlEx;
/*      */             }
/*      */           } finally {
/* 1643 */             if (results != null) {
/*      */               try {
/* 1645 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 1650 */               results = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 1656 */       if (stmt != null) {
/* 1657 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 1661 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 1663 */     return results;
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
/*      */   protected void getCallStmtParameterTypes(String catalog, String procName, ProcedureType procType, String parameterNamePattern, List<ResultSetRow> resultRows)
/*      */     throws SQLException
/*      */   {
/* 1701 */     getCallStmtParameterTypes(catalog, procName, procType, parameterNamePattern, resultRows, false);
/*      */   }
/*      */   
/*      */ 
/*      */   private void getCallStmtParameterTypes(String catalog, String procName, ProcedureType procType, String parameterNamePattern, List<ResultSetRow> resultRows, boolean forGetFunctionColumns)
/*      */     throws SQLException
/*      */   {
/* 1708 */     java.sql.Statement paramRetrievalStmt = null;
/* 1709 */     ResultSet paramRetrievalRs = null;
/*      */     
/* 1711 */     if (parameterNamePattern == null) {
/* 1712 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1713 */         parameterNamePattern = "%";
/*      */       } else {
/* 1715 */         throw SQLError.createSQLException("Parameter/Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1721 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1723 */     String parameterDef = null;
/*      */     
/* 1725 */     byte[] procNameAsBytes = null;
/* 1726 */     byte[] procCatAsBytes = null;
/*      */     
/* 1728 */     boolean isProcedureInAnsiMode = false;
/* 1729 */     String storageDefnDelims = null;
/* 1730 */     String storageDefnClosures = null;
/*      */     try
/*      */     {
/* 1733 */       paramRetrievalStmt = this.conn.getMetadataSafeStatement();
/*      */       
/* 1735 */       String oldCatalog = this.conn.getCatalog();
/* 1736 */       if ((this.conn.lowerCaseTableNames()) && (catalog != null) && (catalog.length() != 0) && (oldCatalog != null) && (oldCatalog.length() != 0))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1743 */         ResultSet rs = null;
/*      */         
/*      */         try
/*      */         {
/* 1747 */           this.conn.setCatalog(catalog.replaceAll(quoteChar, ""));
/* 1748 */           rs = paramRetrievalStmt.executeQuery("SELECT DATABASE()");
/* 1749 */           rs.next();
/*      */           
/* 1751 */           catalog = rs.getString(1);
/*      */         }
/*      */         finally
/*      */         {
/* 1755 */           this.conn.setCatalog(oldCatalog);
/*      */           
/* 1757 */           if (rs != null) {
/* 1758 */             rs.close();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1763 */       if (paramRetrievalStmt.getMaxRows() != 0) {
/* 1764 */         paramRetrievalStmt.setMaxRows(0);
/*      */       }
/*      */       
/* 1767 */       int dotIndex = -1;
/*      */       
/* 1769 */       if (!" ".equals(quoteChar)) {
/* 1770 */         dotIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       }
/*      */       else
/*      */       {
/* 1774 */         dotIndex = procName.indexOf(".");
/*      */       }
/*      */       
/* 1777 */       String dbName = null;
/*      */       
/* 1779 */       if ((dotIndex != -1) && (dotIndex + 1 < procName.length())) {
/* 1780 */         dbName = procName.substring(0, dotIndex);
/* 1781 */         procName = procName.substring(dotIndex + 1);
/*      */       } else {
/* 1783 */         dbName = catalog;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1789 */       String tmpProcName = procName;
/* 1790 */       tmpProcName = tmpProcName.replaceAll(quoteChar, "");
/*      */       try {
/* 1792 */         procNameAsBytes = StringUtils.getBytes(tmpProcName, "UTF-8");
/*      */       } catch (UnsupportedEncodingException ueEx) {
/* 1794 */         procNameAsBytes = s2b(tmpProcName);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1799 */       tmpProcName = dbName;
/* 1800 */       tmpProcName = tmpProcName.replaceAll(quoteChar, "");
/*      */       try {
/* 1802 */         procCatAsBytes = StringUtils.getBytes(tmpProcName, "UTF-8");
/*      */       } catch (UnsupportedEncodingException ueEx) {
/* 1804 */         procCatAsBytes = s2b(tmpProcName);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1809 */       StringBuffer procNameBuf = new StringBuffer();
/*      */       
/* 1811 */       if (dbName != null) {
/* 1812 */         if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
/* 1813 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1816 */         procNameBuf.append(dbName);
/*      */         
/* 1818 */         if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
/* 1819 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1822 */         procNameBuf.append(".");
/*      */       }
/*      */       
/* 1825 */       boolean procNameIsNotQuoted = !procName.startsWith(quoteChar);
/*      */       
/* 1827 */       if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
/* 1828 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1831 */       procNameBuf.append(procName);
/*      */       
/* 1833 */       if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
/* 1834 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1837 */       String fieldName = null;
/* 1838 */       if (procType == ProcedureType.PROCEDURE) {
/* 1839 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE PROCEDURE " + procNameBuf.toString());
/* 1840 */         fieldName = "Create Procedure";
/*      */       } else {
/* 1842 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE FUNCTION " + procNameBuf.toString());
/* 1843 */         fieldName = "Create Function";
/*      */       }
/*      */       
/* 1846 */       if (paramRetrievalRs.next()) {
/* 1847 */         String procedureDef = paramRetrievalRs.getString(fieldName);
/*      */         
/* 1849 */         if ((!this.conn.getNoAccessToProcedureBodies()) && ((procedureDef == null) || (procedureDef.length() == 0)))
/*      */         {
/* 1851 */           throw SQLError.createSQLException("User does not have access to metadata required to determine stored procedure parameter types. If rights can not be granted, configure connection with \"noAccessToProcedureBodies=true\" to have driver generate parameters that represent INOUT strings irregardless of actual parameter types.", "S1000", getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1858 */           String sqlMode = paramRetrievalRs.getString("sql_mode");
/*      */           
/* 1860 */           if (StringUtils.indexOfIgnoreCase(sqlMode, "ANSI") != -1) {
/* 1861 */             isProcedureInAnsiMode = true;
/*      */           }
/*      */         }
/*      */         catch (SQLException sqlEx) {}
/*      */         
/*      */ 
/* 1867 */         String identifierMarkers = isProcedureInAnsiMode ? "`\"" : "`";
/* 1868 */         String identifierAndStringMarkers = "'" + identifierMarkers;
/* 1869 */         storageDefnDelims = "(" + identifierMarkers;
/* 1870 */         storageDefnClosures = ")" + identifierMarkers;
/*      */         
/* 1872 */         if ((procedureDef != null) && (procedureDef.length() != 0))
/*      */         {
/* 1874 */           procedureDef = StringUtils.stripComments(procedureDef, identifierAndStringMarkers, identifierAndStringMarkers, true, false, true, true);
/*      */           
/*      */ 
/* 1877 */           int openParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */           
/*      */ 
/*      */ 
/* 1881 */           int endOfParamDeclarationIndex = 0;
/*      */           
/* 1883 */           endOfParamDeclarationIndex = endPositionOfParameterDeclaration(openParenIndex, procedureDef, quoteChar);
/*      */           
/*      */ 
/* 1886 */           if (procType == ProcedureType.FUNCTION)
/*      */           {
/*      */ 
/*      */ 
/* 1890 */             int returnsIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, " RETURNS ", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1895 */             int endReturnsDef = findEndOfReturnsClause(procedureDef, quoteChar, returnsIndex);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1900 */             int declarationStart = returnsIndex + "RETURNS ".length();
/*      */             
/* 1902 */             while ((declarationStart < procedureDef.length()) && 
/* 1903 */               (Character.isWhitespace(procedureDef.charAt(declarationStart)))) {
/* 1904 */               declarationStart++;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1910 */             String returnsDefn = procedureDef.substring(declarationStart, endReturnsDef).trim();
/* 1911 */             TypeDescriptor returnDescriptor = new TypeDescriptor(returnsDefn, "YES");
/*      */             
/*      */ 
/* 1914 */             resultRows.add(convertTypeDescriptorToProcedureRow(procNameAsBytes, procCatAsBytes, "", false, false, true, returnDescriptor, forGetFunctionColumns, 0));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1919 */           if ((openParenIndex == -1) || (endOfParamDeclarationIndex == -1))
/*      */           {
/*      */ 
/* 1922 */             throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1928 */           parameterDef = procedureDef.substring(openParenIndex + 1, endOfParamDeclarationIndex);
/*      */         }
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1934 */       SQLException sqlExRethrow = null;
/*      */       
/* 1936 */       if (paramRetrievalRs != null) {
/*      */         try {
/* 1938 */           paramRetrievalRs.close();
/*      */         } catch (SQLException sqlEx) {
/* 1940 */           sqlExRethrow = sqlEx;
/*      */         }
/*      */         
/* 1943 */         paramRetrievalRs = null;
/*      */       }
/*      */       
/* 1946 */       if (paramRetrievalStmt != null) {
/*      */         try {
/* 1948 */           paramRetrievalStmt.close();
/*      */         } catch (SQLException sqlEx) {
/* 1950 */           sqlExRethrow = sqlEx;
/*      */         }
/*      */         
/* 1953 */         paramRetrievalStmt = null;
/*      */       }
/*      */       
/* 1956 */       if (sqlExRethrow != null) {
/* 1957 */         throw sqlExRethrow;
/*      */       }
/*      */     }
/*      */     
/* 1961 */     if (parameterDef != null) {
/* 1962 */       int ordinal = 1;
/*      */       
/* 1964 */       List<String> parseList = StringUtils.split(parameterDef, ",", storageDefnDelims, storageDefnClosures, true);
/*      */       
/*      */ 
/* 1967 */       int parseListLen = parseList.size();
/*      */       
/* 1969 */       for (int i = 0; i < parseListLen; i++) {
/* 1970 */         String declaration = (String)parseList.get(i);
/*      */         
/* 1972 */         if (declaration.trim().length() == 0) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1978 */         declaration = declaration.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
/* 1979 */         StringTokenizer declarationTok = new StringTokenizer(declaration, " \t");
/*      */         
/*      */ 
/* 1982 */         String paramName = null;
/* 1983 */         boolean isOutParam = false;
/* 1984 */         boolean isInParam = false;
/*      */         
/* 1986 */         if (declarationTok.hasMoreTokens()) {
/* 1987 */           String possibleParamName = declarationTok.nextToken();
/*      */           
/* 1989 */           if (possibleParamName.equalsIgnoreCase("OUT")) {
/* 1990 */             isOutParam = true;
/*      */             
/* 1992 */             if (declarationTok.hasMoreTokens()) {
/* 1993 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1995 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */             
/*      */           }
/* 1999 */           else if (possibleParamName.equalsIgnoreCase("INOUT")) {
/* 2000 */             isOutParam = true;
/* 2001 */             isInParam = true;
/*      */             
/* 2003 */             if (declarationTok.hasMoreTokens()) {
/* 2004 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 2006 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */             
/*      */           }
/* 2010 */           else if (possibleParamName.equalsIgnoreCase("IN")) {
/* 2011 */             isOutParam = false;
/* 2012 */             isInParam = true;
/*      */             
/* 2014 */             if (declarationTok.hasMoreTokens()) {
/* 2015 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 2017 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2022 */             isOutParam = false;
/* 2023 */             isInParam = true;
/*      */             
/* 2025 */             paramName = possibleParamName;
/*      */           }
/*      */           
/* 2028 */           TypeDescriptor typeDesc = null;
/*      */           
/* 2030 */           if (declarationTok.hasMoreTokens()) {
/* 2031 */             StringBuffer typeInfoBuf = new StringBuffer(declarationTok.nextToken());
/*      */             
/*      */ 
/* 2034 */             while (declarationTok.hasMoreTokens()) {
/* 2035 */               typeInfoBuf.append(" ");
/* 2036 */               typeInfoBuf.append(declarationTok.nextToken());
/*      */             }
/*      */             
/* 2039 */             String typeInfo = typeInfoBuf.toString();
/*      */             
/* 2041 */             typeDesc = new TypeDescriptor(typeInfo, "YES");
/*      */           } else {
/* 2043 */             throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter type)", "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2048 */           if (((paramName.startsWith("`")) && (paramName.endsWith("`"))) || ((isProcedureInAnsiMode) && (paramName.startsWith("\"")) && (paramName.endsWith("\""))))
/*      */           {
/* 2050 */             paramName = paramName.substring(1, paramName.length() - 1);
/*      */           }
/*      */           
/* 2053 */           int wildCompareRes = StringUtils.wildCompare(paramName, parameterNamePattern);
/*      */           
/*      */ 
/* 2056 */           if (wildCompareRes != -1) {
/* 2057 */             ResultSetRow row = convertTypeDescriptorToProcedureRow(procNameAsBytes, procCatAsBytes, paramName, isOutParam, isInParam, false, typeDesc, forGetFunctionColumns, ordinal++);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2062 */             resultRows.add(row);
/*      */           }
/*      */         } else {
/* 2065 */           throw SQLError.createSQLException("Internal error when parsing callable statement metadata (unknown output from 'SHOW CREATE PROCEDURE')", "S1000", getExceptionInterceptor());
/*      */         }
/*      */       }
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
/*      */   private int endPositionOfParameterDeclaration(int beginIndex, String procedureDef, String quoteChar)
/*      */     throws SQLException
/*      */   {
/* 2094 */     int currentPos = beginIndex + 1;
/* 2095 */     int parenDepth = 1;
/*      */     
/* 2097 */     while ((parenDepth > 0) && (currentPos < procedureDef.length())) {
/* 2098 */       int closedParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, ")", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       
/*      */ 
/*      */ 
/* 2102 */       if (closedParenIndex != -1) {
/* 2103 */         int nextOpenParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2108 */         if ((nextOpenParenIndex != -1) && (nextOpenParenIndex < closedParenIndex))
/*      */         {
/* 2110 */           parenDepth++;
/* 2111 */           currentPos = closedParenIndex + 1;
/*      */         }
/*      */         else
/*      */         {
/* 2115 */           parenDepth--;
/* 2116 */           currentPos = closedParenIndex;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2121 */         throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2128 */     return currentPos;
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
/*      */   private int findEndOfReturnsClause(String procedureDefn, String quoteChar, int positionOfReturnKeyword)
/*      */     throws SQLException
/*      */   {
/* 2153 */     String[] tokens = { "LANGUAGE", "NOT", "DETERMINISTIC", "CONTAINS", "NO", "READ", "MODIFIES", "SQL", "COMMENT", "BEGIN", "RETURN" };
/*      */     
/*      */ 
/*      */ 
/* 2157 */     int startLookingAt = positionOfReturnKeyword + "RETURNS".length() + 1;
/*      */     
/* 2159 */     int endOfReturn = -1;
/*      */     
/* 2161 */     for (int i = 0; i < tokens.length; i++) {
/* 2162 */       int nextEndOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, tokens[i], quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       
/*      */ 
/*      */ 
/* 2166 */       if ((nextEndOfReturn != -1) && (
/* 2167 */         (endOfReturn == -1) || (nextEndOfReturn < endOfReturn))) {
/* 2168 */         endOfReturn = nextEndOfReturn;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2173 */     if (endOfReturn != -1) {
/* 2174 */       return endOfReturn;
/*      */     }
/*      */     
/*      */ 
/* 2178 */     endOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, ":", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */     
/*      */ 
/*      */ 
/* 2182 */     if (endOfReturn != -1)
/*      */     {
/* 2184 */       for (int i = endOfReturn; i > 0; i--) {
/* 2185 */         if (Character.isWhitespace(procedureDefn.charAt(i))) {
/* 2186 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2193 */     throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
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
/*      */   private int getCascadeDeleteOption(String cascadeOptions)
/*      */   {
/* 2207 */     int onDeletePos = cascadeOptions.indexOf("ON DELETE");
/*      */     
/* 2209 */     if (onDeletePos != -1) {
/* 2210 */       String deleteOptions = cascadeOptions.substring(onDeletePos, cascadeOptions.length());
/*      */       
/*      */ 
/* 2213 */       if (deleteOptions.startsWith("ON DELETE CASCADE"))
/* 2214 */         return 0;
/* 2215 */       if (deleteOptions.startsWith("ON DELETE SET NULL"))
/* 2216 */         return 2;
/* 2217 */       if (deleteOptions.startsWith("ON DELETE RESTRICT"))
/* 2218 */         return 1;
/* 2219 */       if (deleteOptions.startsWith("ON DELETE NO ACTION")) {
/* 2220 */         return 3;
/*      */       }
/*      */     }
/*      */     
/* 2224 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCascadeUpdateOption(String cascadeOptions)
/*      */   {
/* 2236 */     int onUpdatePos = cascadeOptions.indexOf("ON UPDATE");
/*      */     
/* 2238 */     if (onUpdatePos != -1) {
/* 2239 */       String updateOptions = cascadeOptions.substring(onUpdatePos, cascadeOptions.length());
/*      */       
/*      */ 
/* 2242 */       if (updateOptions.startsWith("ON UPDATE CASCADE"))
/* 2243 */         return 0;
/* 2244 */       if (updateOptions.startsWith("ON UPDATE SET NULL"))
/* 2245 */         return 2;
/* 2246 */       if (updateOptions.startsWith("ON UPDATE RESTRICT"))
/* 2247 */         return 1;
/* 2248 */       if (updateOptions.startsWith("ON UPDATE NO ACTION")) {
/* 2249 */         return 3;
/*      */       }
/*      */     }
/*      */     
/* 2253 */     return 3;
/*      */   }
/*      */   
/*      */   protected IteratorWithCleanup<String> getCatalogIterator(String catalogSpec) throws SQLException {
/*      */     IteratorWithCleanup<String> allCatalogsIter;
/*      */     IteratorWithCleanup<String> allCatalogsIter;
/* 2259 */     if (catalogSpec != null) { IteratorWithCleanup<String> allCatalogsIter;
/* 2260 */       if (!catalogSpec.equals("")) { IteratorWithCleanup<String> allCatalogsIter;
/* 2261 */         if (this.conn.getPedantic()) {
/* 2262 */           allCatalogsIter = new SingleStringIterator(catalogSpec);
/*      */         } else {
/* 2264 */           allCatalogsIter = new SingleStringIterator(StringUtils.unQuoteIdentifier(catalogSpec, this.conn.useAnsiQuotedIdentifiers()));
/*      */         }
/*      */       }
/*      */       else {
/* 2268 */         allCatalogsIter = new SingleStringIterator(this.database);
/*      */       } } else { IteratorWithCleanup<String> allCatalogsIter;
/* 2270 */       if (this.conn.getNullCatalogMeansCurrent())
/*      */       {
/* 2272 */         allCatalogsIter = new SingleStringIterator(this.database);
/*      */       } else {
/* 2274 */         allCatalogsIter = new ResultSetIterator(getCatalogs(), 1);
/*      */       }
/*      */     }
/* 2277 */     return allCatalogsIter;
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
/*      */   public ResultSet getCatalogs()
/*      */     throws SQLException
/*      */   {
/* 2296 */     ResultSet results = null;
/* 2297 */     java.sql.Statement stmt = null;
/*      */     try
/*      */     {
/* 2300 */       stmt = this.conn.createStatement();
/* 2301 */       stmt.setEscapeProcessing(false);
/* 2302 */       results = stmt.executeQuery("SHOW DATABASES");
/*      */       
/* 2304 */       ResultSetMetaData resultsMD = results.getMetaData();
/* 2305 */       Field[] fields = new Field[1];
/* 2306 */       fields[0] = new Field("", "TABLE_CAT", 12, resultsMD.getColumnDisplaySize(1));
/*      */       
/*      */ 
/* 2309 */       ArrayList<ResultSetRow> tuples = new ArrayList();
/*      */       byte[][] rowVal;
/* 2311 */       while (results.next()) {
/* 2312 */         rowVal = new byte[1][];
/* 2313 */         rowVal[0] = results.getBytes(1);
/* 2314 */         tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */       }
/*      */       
/* 2317 */       return buildResultSet(fields, tuples);
/*      */     } finally {
/* 2319 */       if (results != null) {
/*      */         try {
/* 2321 */           results.close();
/*      */         } catch (SQLException sqlEx) {
/* 2323 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         }
/*      */         
/* 2326 */         results = null;
/*      */       }
/*      */       
/* 2329 */       if (stmt != null) {
/*      */         try {
/* 2331 */           stmt.close();
/*      */         } catch (SQLException sqlEx) {
/* 2333 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         }
/*      */         
/* 2336 */         stmt = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCatalogSeparator()
/*      */     throws SQLException
/*      */   {
/* 2349 */     return ".";
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
/*      */   public String getCatalogTerm()
/*      */     throws SQLException
/*      */   {
/* 2366 */     return "database";
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
/*      */   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 2407 */     Field[] fields = new Field[8];
/* 2408 */     fields[0] = new Field("", "TABLE_CAT", 1, 64);
/* 2409 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
/* 2410 */     fields[2] = new Field("", "TABLE_NAME", 1, 64);
/* 2411 */     fields[3] = new Field("", "COLUMN_NAME", 1, 64);
/* 2412 */     fields[4] = new Field("", "GRANTOR", 1, 77);
/* 2413 */     fields[5] = new Field("", "GRANTEE", 1, 77);
/* 2414 */     fields[6] = new Field("", "PRIVILEGE", 1, 64);
/* 2415 */     fields[7] = new Field("", "IS_GRANTABLE", 1, 3);
/*      */     
/* 2417 */     StringBuffer grantQuery = new StringBuffer("SELECT c.host, c.db, t.grantor, c.user, c.table_name, c.column_name, c.column_priv from mysql.columns_priv c, mysql.tables_priv t where c.host = t.host and c.db = t.db and c.table_name = t.table_name ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2424 */     if ((catalog != null) && (catalog.length() != 0)) {
/* 2425 */       grantQuery.append(" AND c.db='");
/* 2426 */       grantQuery.append(catalog);
/* 2427 */       grantQuery.append("' ");
/*      */     }
/*      */     
/*      */ 
/* 2431 */     grantQuery.append(" AND c.table_name ='");
/* 2432 */     grantQuery.append(table);
/* 2433 */     grantQuery.append("' AND c.column_name like '");
/* 2434 */     grantQuery.append(columnNamePattern);
/* 2435 */     grantQuery.append("'");
/*      */     
/* 2437 */     java.sql.Statement stmt = null;
/* 2438 */     ResultSet results = null;
/* 2439 */     ArrayList<ResultSetRow> grantRows = new ArrayList();
/*      */     try
/*      */     {
/* 2442 */       stmt = this.conn.createStatement();
/* 2443 */       stmt.setEscapeProcessing(false);
/* 2444 */       results = stmt.executeQuery(grantQuery.toString());
/*      */       
/* 2446 */       while (results.next()) {
/* 2447 */         String host = results.getString(1);
/* 2448 */         String db = results.getString(2);
/* 2449 */         String grantor = results.getString(3);
/* 2450 */         String user = results.getString(4);
/*      */         
/* 2452 */         if ((user == null) || (user.length() == 0)) {
/* 2453 */           user = "%";
/*      */         }
/*      */         
/* 2456 */         StringBuffer fullUser = new StringBuffer(user);
/*      */         
/* 2458 */         if ((host != null) && (this.conn.getUseHostsInPrivileges())) {
/* 2459 */           fullUser.append("@");
/* 2460 */           fullUser.append(host);
/*      */         }
/*      */         
/* 2463 */         String columnName = results.getString(6);
/* 2464 */         String allPrivileges = results.getString(7);
/*      */         
/* 2466 */         if (allPrivileges != null) {
/* 2467 */           allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
/*      */           
/* 2469 */           StringTokenizer st = new StringTokenizer(allPrivileges, ",");
/*      */           
/* 2471 */           while (st.hasMoreTokens()) {
/* 2472 */             String privilege = st.nextToken().trim();
/* 2473 */             byte[][] tuple = new byte[8][];
/* 2474 */             tuple[0] = s2b(db);
/* 2475 */             tuple[1] = null;
/* 2476 */             tuple[2] = s2b(table);
/* 2477 */             tuple[3] = s2b(columnName);
/*      */             
/* 2479 */             if (grantor != null) {
/* 2480 */               tuple[4] = s2b(grantor);
/*      */             } else {
/* 2482 */               tuple[4] = null;
/*      */             }
/*      */             
/* 2485 */             tuple[5] = s2b(fullUser.toString());
/* 2486 */             tuple[6] = s2b(privilege);
/* 2487 */             tuple[7] = null;
/* 2488 */             grantRows.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
/*      */           }
/*      */         }
/*      */       }
/*      */     } finally {
/* 2493 */       if (results != null) {
/*      */         try {
/* 2495 */           results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 2500 */         results = null;
/*      */       }
/*      */       
/* 2503 */       if (stmt != null) {
/*      */         try {
/* 2505 */           stmt.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 2510 */         stmt = null;
/*      */       }
/*      */     }
/*      */     
/* 2514 */     return buildResultSet(fields, grantRows);
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
/*      */   public ResultSet getColumns(String catalog, final String schemaPattern, final String tableNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 2578 */     if (columnNamePattern == null) {
/* 2579 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 2580 */         columnNamePattern = "%";
/*      */       } else {
/* 2582 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2588 */     final String colPattern = columnNamePattern;
/*      */     
/* 2590 */     Field[] fields = createColumnsFields();
/*      */     
/* 2592 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/* 2593 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 2597 */       new IterateBlock(getCatalogIterator(catalog))
/*      */       {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 2600 */           ArrayList<String> tableNameList = new ArrayList();
/*      */           
/* 2602 */           if (tableNamePattern == null)
/*      */           {
/* 2604 */             ResultSet tables = null;
/*      */             try
/*      */             {
/* 2607 */               tables = DatabaseMetaData.this.getTables(catalogStr, schemaPattern, "%", new String[0]);
/*      */               
/*      */ 
/* 2610 */               while (tables.next()) {
/* 2611 */                 String tableNameFromList = tables.getString("TABLE_NAME");
/*      */                 
/* 2613 */                 tableNameList.add(tableNameFromList);
/*      */               }
/*      */             } finally {
/* 2616 */               if (tables != null) {
/*      */                 try {
/* 2618 */                   tables.close();
/*      */                 } catch (Exception sqlEx) {
/* 2620 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 2624 */                 tables = null;
/*      */               }
/*      */             }
/*      */           } else {
/* 2628 */             ResultSet tables = null;
/*      */             try
/*      */             {
/* 2631 */               tables = DatabaseMetaData.this.getTables(catalogStr, schemaPattern, tableNamePattern, new String[0]);
/*      */               
/*      */ 
/* 2634 */               while (tables.next()) {
/* 2635 */                 String tableNameFromList = tables.getString("TABLE_NAME");
/*      */                 
/* 2637 */                 tableNameList.add(tableNameFromList);
/*      */               }
/*      */             } finally {
/* 2640 */               if (tables != null) {
/*      */                 try {
/* 2642 */                   tables.close();
/*      */                 } catch (SQLException sqlEx) {
/* 2644 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 2648 */                 tables = null;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 2653 */           for (String tableName : tableNameList)
/*      */           {
/* 2655 */             ResultSet results = null;
/*      */             try
/*      */             {
/* 2658 */               StringBuffer queryBuf = new StringBuffer("SHOW ");
/*      */               
/* 2660 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2661 */                 queryBuf.append("FULL ");
/*      */               }
/*      */               
/* 2664 */               queryBuf.append("COLUMNS FROM ");
/* 2665 */               queryBuf.append(StringUtils.quoteIdentifier(tableName, DatabaseMetaData.this.conn.getPedantic()));
/* 2666 */               queryBuf.append(" FROM ");
/* 2667 */               queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/* 2668 */               queryBuf.append(" LIKE '");
/* 2669 */               queryBuf.append(colPattern);
/* 2670 */               queryBuf.append("'");
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2677 */               boolean fixUpOrdinalsRequired = false;
/* 2678 */               Object ordinalFixUpMap = null;
/*      */               
/* 2680 */               if (!colPattern.equals("%")) {
/* 2681 */                 fixUpOrdinalsRequired = true;
/*      */                 
/* 2683 */                 StringBuffer fullColumnQueryBuf = new StringBuffer("SHOW ");
/*      */                 
/*      */ 
/* 2686 */                 if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2687 */                   fullColumnQueryBuf.append("FULL ");
/*      */                 }
/*      */                 
/* 2690 */                 fullColumnQueryBuf.append("COLUMNS FROM ");
/* 2691 */                 fullColumnQueryBuf.append(StringUtils.quoteIdentifier(tableName, DatabaseMetaData.this.conn.getPedantic()));
/* 2692 */                 fullColumnQueryBuf.append(" FROM ");
/* 2693 */                 fullColumnQueryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */                 
/* 2695 */                 results = stmt.executeQuery(fullColumnQueryBuf.toString());
/*      */                 
/*      */ 
/* 2698 */                 ordinalFixUpMap = new HashMap();
/*      */                 
/* 2700 */                 int fullOrdinalPos = 1;
/*      */                 
/* 2702 */                 while (results.next()) {
/* 2703 */                   String fullOrdColName = results.getString("Field");
/*      */                   
/*      */ 
/* 2706 */                   ((Map)ordinalFixUpMap).put(fullOrdColName, Integer.valueOf(fullOrdinalPos++));
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2711 */               results = stmt.executeQuery(queryBuf.toString());
/*      */               
/* 2713 */               int ordPos = 1;
/*      */               
/* 2715 */               while (results.next()) {
/* 2716 */                 byte[][] rowVal = new byte[24][];
/* 2717 */                 rowVal[0] = DatabaseMetaData.this.s2b(catalogStr);
/* 2718 */                 rowVal[1] = null;
/*      */                 
/*      */ 
/* 2721 */                 rowVal[2] = DatabaseMetaData.this.s2b(tableName);
/* 2722 */                 rowVal[3] = results.getBytes("Field");
/*      */                 
/* 2724 */                 DatabaseMetaData.TypeDescriptor typeDesc = new DatabaseMetaData.TypeDescriptor(DatabaseMetaData.this, results.getString("Type"), results.getString("Null"));
/*      */                 
/*      */ 
/*      */ 
/* 2728 */                 rowVal[4] = Short.toString(typeDesc.dataType).getBytes();
/*      */                 
/*      */ 
/*      */ 
/* 2732 */                 rowVal[5] = DatabaseMetaData.this.s2b(typeDesc.typeName);
/*      */                 
/* 2734 */                 if (typeDesc.columnSize == null) {
/* 2735 */                   rowVal[6] = null;
/*      */                 } else {
/* 2737 */                   String collation = results.getString("Collation");
/* 2738 */                   int mbminlen = 1;
/* 2739 */                   if ((collation != null) && (("TEXT".equals(typeDesc.typeName)) || ("TINYTEXT".equals(typeDesc.typeName)) || ("MEDIUMTEXT".equals(typeDesc.typeName))))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/* 2744 */                     if ((collation.indexOf("ucs2") > -1) || (collation.indexOf("utf16") > -1))
/*      */                     {
/* 2746 */                       mbminlen = 2;
/* 2747 */                     } else if (collation.indexOf("utf32") > -1) {
/* 2748 */                       mbminlen = 4;
/*      */                     }
/*      */                   }
/* 2751 */                   rowVal[6] = (mbminlen == 1 ? DatabaseMetaData.this.s2b(typeDesc.columnSize.toString()) : DatabaseMetaData.this.s2b(Integer.valueOf(typeDesc.columnSize.intValue() / mbminlen).toString()));
/*      */                 }
/*      */                 
/*      */ 
/* 2755 */                 rowVal[7] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.bufferLength));
/* 2756 */                 rowVal[8] = (typeDesc.decimalDigits == null ? null : DatabaseMetaData.this.s2b(typeDesc.decimalDigits.toString()));
/* 2757 */                 rowVal[9] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.numPrecRadix));
/*      */                 
/* 2759 */                 rowVal[10] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.nullability));
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 try
/*      */                 {
/* 2770 */                   if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2771 */                     rowVal[11] = results.getBytes("Comment");
/*      */                   }
/*      */                   else {
/* 2774 */                     rowVal[11] = results.getBytes("Extra");
/*      */                   }
/*      */                 } catch (Exception E) {
/* 2777 */                   rowVal[11] = new byte[0];
/*      */                 }
/*      */                 
/*      */ 
/* 2781 */                 rowVal[12] = results.getBytes("Default");
/*      */                 
/* 2783 */                 rowVal[13] = { 48 };
/* 2784 */                 rowVal[14] = { 48 };
/*      */                 
/* 2786 */                 if ((StringUtils.indexOfIgnoreCase(typeDesc.typeName, "CHAR") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BLOB") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "TEXT") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BINARY") != -1))
/*      */                 {
/*      */ 
/*      */ 
/* 2790 */                   rowVal[15] = rowVal[6];
/*      */                 } else {
/* 2792 */                   rowVal[15] = null;
/*      */                 }
/*      */                 
/*      */ 
/* 2796 */                 if (!fixUpOrdinalsRequired) {
/* 2797 */                   rowVal[16] = Integer.toString(ordPos++).getBytes();
/*      */                 }
/*      */                 else {
/* 2800 */                   String origColName = results.getString("Field");
/*      */                   
/* 2802 */                   Integer realOrdinal = (Integer)((Map)ordinalFixUpMap).get(origColName);
/*      */                   
/*      */ 
/* 2805 */                   if (realOrdinal != null) {
/* 2806 */                     rowVal[16] = realOrdinal.toString().getBytes();
/*      */                   }
/*      */                   else {
/* 2809 */                     throw SQLError.createSQLException("Can not find column in full column list to determine true ordinal position.", "S1000", DatabaseMetaData.this.getExceptionInterceptor());
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 2815 */                 rowVal[17] = DatabaseMetaData.this.s2b(typeDesc.isNullable);
/*      */                 
/*      */ 
/* 2818 */                 rowVal[18] = null;
/* 2819 */                 rowVal[19] = null;
/* 2820 */                 rowVal[20] = null;
/* 2821 */                 rowVal[21] = null;
/*      */                 
/* 2823 */                 rowVal[22] = DatabaseMetaData.this.s2b("");
/*      */                 
/* 2825 */                 String extra = results.getString("Extra");
/*      */                 
/* 2827 */                 if (extra != null) {
/* 2828 */                   rowVal[22] = DatabaseMetaData.this.s2b(StringUtils.indexOfIgnoreCase(extra, "auto_increment") != -1 ? "YES" : "NO");
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 2833 */                 rowVal[23] = DatabaseMetaData.this.s2b("");
/*      */                 
/* 2835 */                 rows.add(new ByteArrayRow(rowVal, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */               }
/*      */             } finally {
/* 2838 */               if (results != null) {
/*      */                 try {
/* 2840 */                   results.close();
/*      */                 }
/*      */                 catch (Exception ex) {}
/*      */                 
/*      */ 
/* 2845 */                 results = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 2852 */       if (stmt != null) {
/* 2853 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 2857 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 2859 */     return results;
/*      */   }
/*      */   
/*      */   protected Field[] createColumnsFields() {
/* 2863 */     Field[] fields = new Field[24];
/* 2864 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 2865 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 2866 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 2867 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 2868 */     fields[4] = new Field("", "DATA_TYPE", 4, 5);
/* 2869 */     fields[5] = new Field("", "TYPE_NAME", 1, 16);
/* 2870 */     fields[6] = new Field("", "COLUMN_SIZE", 4, Integer.toString(Integer.MAX_VALUE).length());
/*      */     
/* 2872 */     fields[7] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 2873 */     fields[8] = new Field("", "DECIMAL_DIGITS", 4, 10);
/* 2874 */     fields[9] = new Field("", "NUM_PREC_RADIX", 4, 10);
/* 2875 */     fields[10] = new Field("", "NULLABLE", 4, 10);
/* 2876 */     fields[11] = new Field("", "REMARKS", 1, 0);
/* 2877 */     fields[12] = new Field("", "COLUMN_DEF", 1, 0);
/* 2878 */     fields[13] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 2879 */     fields[14] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 2880 */     fields[15] = new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(Integer.MAX_VALUE).length());
/*      */     
/* 2882 */     fields[16] = new Field("", "ORDINAL_POSITION", 4, 10);
/* 2883 */     fields[17] = new Field("", "IS_NULLABLE", 1, 3);
/* 2884 */     fields[18] = new Field("", "SCOPE_CATALOG", 1, 255);
/* 2885 */     fields[19] = new Field("", "SCOPE_SCHEMA", 1, 255);
/* 2886 */     fields[20] = new Field("", "SCOPE_TABLE", 1, 255);
/* 2887 */     fields[21] = new Field("", "SOURCE_DATA_TYPE", 5, 10);
/* 2888 */     fields[22] = new Field("", "IS_AUTOINCREMENT", 1, 3);
/* 2889 */     fields[23] = new Field("", "IS_GENERATEDCOLUMN", 1, 3);
/* 2890 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 2901 */     return this.conn;
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
/*      */   public ResultSet getCrossReference(final String primaryCatalog, final String primarySchema, final String primaryTable, final String foreignCatalog, final String foreignSchema, final String foreignTable)
/*      */     throws SQLException
/*      */   {
/* 2975 */     if (primaryTable == null) {
/* 2976 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 2980 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 2982 */     final ArrayList<ResultSetRow> tuples = new ArrayList();
/*      */     
/* 2984 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 2986 */       final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 2990 */         new IterateBlock(getCatalogIterator(foreignCatalog))
/*      */         {
/*      */           void forEach(String catalogStr) throws SQLException {
/* 2993 */             ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 3000 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50)) {
/* 3001 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr, null);
/*      */               }
/*      */               else {
/* 3004 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                 
/* 3006 */                 queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */                 
/* 3008 */                 fkresults = stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/* 3012 */               String foreignTableWithCase = DatabaseMetaData.this.getTableNameWithCase(foreignTable);
/* 3013 */               String primaryTableWithCase = DatabaseMetaData.this.getTableNameWithCase(primaryTable);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3021 */               while (fkresults.next()) {
/* 3022 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 3024 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 3028 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 3031 */                   if (comment != null) {
/* 3032 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */ 
/* 3035 */                     if (commentTokens.hasMoreTokens()) {
/* 3036 */                       String str1 = commentTokens.nextToken();
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/* 3041 */                     while (commentTokens.hasMoreTokens()) {
/* 3042 */                       String keys = commentTokens.nextToken();
/*      */                       
/* 3044 */                       DatabaseMetaData.LocalAndReferencedColumns parsedInfo = DatabaseMetaData.this.parseTableStatusIntoLocalAndReferencedColumns(keys);
/*      */                       
/* 3046 */                       int keySeq = 0;
/*      */                       
/* 3048 */                       Iterator<String> referencingColumns = parsedInfo.localColumnsList.iterator();
/*      */                       
/* 3050 */                       Iterator<String> referencedColumns = parsedInfo.referencedColumnsList.iterator();
/*      */                       
/*      */ 
/* 3053 */                       while (referencingColumns.hasNext()) {
/* 3054 */                         String referencingColumn = StringUtils.unQuoteIdentifier((String)referencingColumns.next(), DatabaseMetaData.this.conn.useAnsiQuotedIdentifiers());
/*      */                         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3061 */                         byte[][] tuple = new byte[14][];
/* 3062 */                         tuple[4] = (foreignCatalog == null ? null : DatabaseMetaData.this.s2b(foreignCatalog));
/*      */                         
/* 3064 */                         tuple[5] = (foreignSchema == null ? null : DatabaseMetaData.this.s2b(foreignSchema));
/*      */                         
/* 3066 */                         String dummy = fkresults.getString("Name");
/*      */                         
/*      */ 
/* 3069 */                         if (dummy.compareTo(foreignTableWithCase) == 0)
/*      */                         {
/*      */ 
/*      */ 
/*      */ 
/* 3074 */                           tuple[6] = DatabaseMetaData.this.s2b(dummy);
/*      */                           
/* 3076 */                           tuple[7] = DatabaseMetaData.this.s2b(referencingColumn);
/* 3077 */                           tuple[0] = (primaryCatalog == null ? null : DatabaseMetaData.this.s2b(primaryCatalog));
/*      */                           
/* 3079 */                           tuple[1] = (primarySchema == null ? null : DatabaseMetaData.this.s2b(primarySchema));
/*      */                           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3085 */                           if (parsedInfo.referencedTable.compareTo(primaryTableWithCase) == 0)
/*      */                           {
/*      */ 
/*      */ 
/*      */ 
/* 3090 */                             tuple[2] = DatabaseMetaData.this.s2b(parsedInfo.referencedTable);
/* 3091 */                             tuple[3] = DatabaseMetaData.this.s2b(StringUtils.unQuoteIdentifier((String)referencedColumns.next(), DatabaseMetaData.this.conn.useAnsiQuotedIdentifiers()));
/*      */                             
/*      */ 
/* 3094 */                             tuple[8] = Integer.toString(keySeq).getBytes();
/*      */                             
/*      */ 
/* 3097 */                             int[] actions = DatabaseMetaData.this.getForeignKeyActions(keys);
/*      */                             
/* 3099 */                             tuple[9] = Integer.toString(actions[1]).getBytes();
/*      */                             
/* 3101 */                             tuple[10] = Integer.toString(actions[0]).getBytes();
/*      */                             
/* 3103 */                             tuple[11] = null;
/* 3104 */                             tuple[12] = null;
/* 3105 */                             tuple[13] = Integer.toString(7).getBytes();
/*      */                             
/*      */ 
/*      */ 
/* 3109 */                             tuples.add(new ByteArrayRow(tuple, DatabaseMetaData.this.getExceptionInterceptor()));
/* 3110 */                             keySeq++;
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/* 3118 */             } finally { if (fkresults != null) {
/*      */                 try {
/* 3120 */                   fkresults.close();
/*      */                 } catch (Exception sqlEx) {
/* 3122 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 3126 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 3132 */         if (stmt != null) {
/* 3133 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3138 */     ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 3140 */     return results;
/*      */   }
/*      */   
/*      */   protected Field[] createFkMetadataFields() {
/* 3144 */     Field[] fields = new Field[14];
/* 3145 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/* 3146 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/* 3147 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/* 3148 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/* 3149 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/* 3150 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/* 3151 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/* 3152 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/* 3153 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/* 3154 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/* 3155 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/* 3156 */     fields[11] = new Field("", "FK_NAME", 1, 0);
/* 3157 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/* 3158 */     fields[13] = new Field("", "DEFERRABILITY", 5, 2);
/* 3159 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDatabaseMajorVersion()
/*      */     throws SQLException
/*      */   {
/* 3166 */     return this.conn.getServerMajorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDatabaseMinorVersion()
/*      */     throws SQLException
/*      */   {
/* 3173 */     return this.conn.getServerMinorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDatabaseProductName()
/*      */     throws SQLException
/*      */   {
/* 3184 */     return "MySQL";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDatabaseProductVersion()
/*      */     throws SQLException
/*      */   {
/* 3195 */     return this.conn.getServerVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDefaultTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 3208 */     if (this.conn.supportsIsolationLevel()) {
/* 3209 */       return 2;
/*      */     }
/*      */     
/* 3212 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDriverMajorVersion()
/*      */   {
/* 3221 */     return NonRegisteringDriver.getMajorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDriverMinorVersion()
/*      */   {
/* 3230 */     return NonRegisteringDriver.getMinorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDriverName()
/*      */     throws SQLException
/*      */   {
/* 3241 */     return "MySQL Connector Java";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDriverVersion()
/*      */     throws SQLException
/*      */   {
/* 3252 */     return "mysql-connector-java-5.1.29 ( Revision: alexander.soklakov@oracle.com-20140120140810-s44574olh90i6i4l )";
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
/*      */   public ResultSet getExportedKeys(String catalog, String schema, final String table)
/*      */     throws SQLException
/*      */   {
/* 3316 */     if (table == null) {
/* 3317 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 3321 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 3323 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/*      */     
/* 3325 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 3327 */       final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 3331 */         new IterateBlock(getCatalogIterator(catalog)) {
/*      */           void forEach(String catalogStr) throws SQLException {
/* 3333 */             ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 3340 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
/*      */               {
/*      */ 
/* 3343 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr, null);
/*      */               }
/*      */               else {
/* 3346 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                 
/* 3348 */                 queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */                 
/* 3350 */                 fkresults = stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 3355 */               String tableNameWithCase = DatabaseMetaData.this.getTableNameWithCase(table);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3361 */               while (fkresults.next()) {
/* 3362 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 3364 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 3368 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 3371 */                   if (comment != null) {
/* 3372 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */ 
/* 3375 */                     if (commentTokens.hasMoreTokens()) {
/* 3376 */                       commentTokens.nextToken();
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/* 3381 */                       while (commentTokens.hasMoreTokens()) {
/* 3382 */                         String keys = commentTokens.nextToken();
/*      */                         
/* 3384 */                         DatabaseMetaData.this.getExportKeyResults(catalogStr, tableNameWithCase, keys, rows, fkresults.getString("Name"));
/*      */                       }
/*      */                       
/*      */                     }
/*      */                     
/*      */                   }
/*      */                   
/*      */                 }
/*      */                 
/*      */               }
/*      */               
/*      */             }
/*      */             finally
/*      */             {
/* 3398 */               if (fkresults != null) {
/*      */                 try {
/* 3400 */                   fkresults.close();
/*      */                 } catch (SQLException sqlEx) {
/* 3402 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 3406 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 3412 */         if (stmt != null) {
/* 3413 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3418 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3420 */     return results;
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
/*      */   protected void getExportKeyResults(String catalog, String exportingTable, String keysComment, List<ResultSetRow> tuples, String fkTableName)
/*      */     throws SQLException
/*      */   {
/* 3444 */     getResultsImpl(catalog, exportingTable, keysComment, tuples, fkTableName, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getExtraNameCharacters()
/*      */     throws SQLException
/*      */   {
/* 3457 */     return "#@";
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
/*      */   protected int[] getForeignKeyActions(String commentString)
/*      */   {
/* 3470 */     int[] actions = { 3, 3 };
/*      */     
/*      */ 
/*      */ 
/* 3474 */     int lastParenIndex = commentString.lastIndexOf(")");
/*      */     
/* 3476 */     if (lastParenIndex != commentString.length() - 1) {
/* 3477 */       String cascadeOptions = commentString.substring(lastParenIndex + 1).trim().toUpperCase(Locale.ENGLISH);
/*      */       
/*      */ 
/* 3480 */       actions[0] = getCascadeDeleteOption(cascadeOptions);
/* 3481 */       actions[1] = getCascadeUpdateOption(cascadeOptions);
/*      */     }
/*      */     
/* 3484 */     return actions;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getIdentifierQuoteString()
/*      */     throws SQLException
/*      */   {
/* 3497 */     if (this.conn.supportsQuotedIdentifiers()) {
/* 3498 */       if (!this.conn.useAnsiQuotedIdentifiers()) {
/* 3499 */         return "`";
/*      */       }
/*      */       
/* 3502 */       return "\"";
/*      */     }
/*      */     
/* 3505 */     return " ";
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
/*      */   public ResultSet getImportedKeys(String catalog, String schema, final String table)
/*      */     throws SQLException
/*      */   {
/* 3569 */     if (table == null) {
/* 3570 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 3574 */     Field[] fields = createFkMetadataFields();
/*      */     
/* 3576 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/*      */     
/* 3578 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 3580 */       final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 3584 */         new IterateBlock(getCatalogIterator(catalog)) {
/*      */           void forEach(String catalogStr) throws SQLException {
/* 3586 */             ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 3593 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
/*      */               {
/*      */ 
/* 3596 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr, table);
/*      */               }
/*      */               else {
/* 3599 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS ");
/*      */                 
/* 3601 */                 queryBuf.append(" FROM ");
/* 3602 */                 queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/* 3603 */                 queryBuf.append(" LIKE '");
/* 3604 */                 queryBuf.append(table);
/* 3605 */                 queryBuf.append("'");
/*      */                 
/* 3607 */                 fkresults = stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3615 */               while (fkresults.next()) {
/* 3616 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 3618 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 3622 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 3625 */                   if (comment != null) {
/* 3626 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */ 
/* 3629 */                     if (commentTokens.hasMoreTokens()) {
/* 3630 */                       commentTokens.nextToken();
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/* 3635 */                       while (commentTokens.hasMoreTokens()) {
/* 3636 */                         String keys = commentTokens.nextToken();
/*      */                         
/* 3638 */                         DatabaseMetaData.this.getImportKeyResults(catalogStr, table, keys, rows);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             finally
/*      */             {
/* 3647 */               if (fkresults != null) {
/*      */                 try {
/* 3649 */                   fkresults.close();
/*      */                 } catch (SQLException sqlEx) {
/* 3651 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 3655 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 3661 */         if (stmt != null) {
/* 3662 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3667 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3669 */     return results;
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
/*      */   protected void getImportKeyResults(String catalog, String importingTable, String keysComment, List<ResultSetRow> tuples)
/*      */     throws SQLException
/*      */   {
/* 3691 */     getResultsImpl(catalog, importingTable, keysComment, tuples, null, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getIndexInfo(String catalog, String schema, final String table, final boolean unique, boolean approximate)
/*      */     throws SQLException
/*      */   {
/* 3762 */     Field[] fields = createIndexInfoFields();
/*      */     
/* 3764 */     final SortedMap<IndexMetaDataKey, ResultSetRow> sortedRows = new TreeMap();
/* 3765 */     ArrayList<ResultSetRow> rows = new ArrayList();
/* 3766 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 3770 */       new IterateBlock(getCatalogIterator(catalog))
/*      */       {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 3773 */           ResultSet results = null;
/*      */           try
/*      */           {
/* 3776 */             StringBuffer queryBuf = new StringBuffer("SHOW INDEX FROM ");
/*      */             
/* 3778 */             queryBuf.append(StringUtils.quoteIdentifier(table, DatabaseMetaData.this.conn.getPedantic()));
/* 3779 */             queryBuf.append(" FROM ");
/* 3780 */             queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */             try
/*      */             {
/* 3783 */               results = stmt.executeQuery(queryBuf.toString());
/*      */             } catch (SQLException sqlEx) {
/* 3785 */               int errorCode = sqlEx.getErrorCode();
/*      */               
/*      */ 
/*      */ 
/* 3789 */               if (!"42S02".equals(sqlEx.getSQLState()))
/*      */               {
/*      */ 
/* 3792 */                 if (errorCode != 1146) {
/* 3793 */                   throw sqlEx;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 3798 */             while ((results != null) && (results.next())) {
/* 3799 */               byte[][] row = new byte[14][];
/* 3800 */               row[0] = (catalogStr == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr));
/*      */               
/* 3802 */               row[1] = null;
/* 3803 */               row[2] = results.getBytes("Table");
/*      */               
/* 3805 */               boolean indexIsUnique = results.getInt("Non_unique") == 0;
/*      */               
/*      */ 
/* 3808 */               row[3] = (!indexIsUnique ? DatabaseMetaData.this.s2b("true") : DatabaseMetaData.this.s2b("false"));
/*      */               
/* 3810 */               row[4] = new byte[0];
/* 3811 */               row[5] = results.getBytes("Key_name");
/* 3812 */               short indexType = 3;
/* 3813 */               row[6] = Integer.toString(indexType).getBytes();
/*      */               
/* 3815 */               row[7] = results.getBytes("Seq_in_index");
/* 3816 */               row[8] = results.getBytes("Column_name");
/* 3817 */               row[9] = results.getBytes("Collation");
/*      */               
/*      */ 
/*      */ 
/* 3821 */               long cardinality = results.getLong("Cardinality");
/*      */               
/* 3823 */               if (cardinality > 2147483647L) {
/* 3824 */                 cardinality = 2147483647L;
/*      */               }
/*      */               
/* 3827 */               row[10] = DatabaseMetaData.this.s2b(String.valueOf(cardinality));
/* 3828 */               row[11] = DatabaseMetaData.this.s2b("0");
/* 3829 */               row[12] = null;
/*      */               
/* 3831 */               DatabaseMetaData.IndexMetaDataKey indexInfoKey = new DatabaseMetaData.IndexMetaDataKey(DatabaseMetaData.this, !indexIsUnique, indexType, results.getString("Key_name").toLowerCase(), results.getShort("Seq_in_index"));
/*      */               
/*      */ 
/* 3834 */               if (unique) {
/* 3835 */                 if (indexIsUnique) {
/* 3836 */                   sortedRows.put(indexInfoKey, new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                 }
/*      */               }
/*      */               else {
/* 3840 */                 sortedRows.put(indexInfoKey, new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */               }
/*      */             }
/*      */           } finally {
/* 3844 */             if (results != null) {
/*      */               try {
/* 3846 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 3851 */               results = null;
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 3856 */       }.doForAll();
/* 3857 */       Iterator<ResultSetRow> sortedRowsIterator = sortedRows.values().iterator();
/* 3858 */       while (sortedRowsIterator.hasNext()) {
/* 3859 */         rows.add(sortedRowsIterator.next());
/*      */       }
/*      */       
/* 3862 */       ResultSet indexInfo = buildResultSet(fields, rows);
/*      */       
/* 3864 */       return indexInfo;
/*      */     } finally {
/* 3866 */       if (stmt != null) {
/* 3867 */         stmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected Field[] createIndexInfoFields() {
/* 3873 */     Field[] fields = new Field[13];
/* 3874 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 3875 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 3876 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 3877 */     fields[3] = new Field("", "NON_UNIQUE", 16, 4);
/* 3878 */     fields[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
/* 3879 */     fields[5] = new Field("", "INDEX_NAME", 1, 32);
/* 3880 */     fields[6] = new Field("", "TYPE", 5, 32);
/* 3881 */     fields[7] = new Field("", "ORDINAL_POSITION", 5, 5);
/* 3882 */     fields[8] = new Field("", "COLUMN_NAME", 1, 32);
/* 3883 */     fields[9] = new Field("", "ASC_OR_DESC", 1, 1);
/* 3884 */     fields[10] = new Field("", "CARDINALITY", 4, 20);
/* 3885 */     fields[11] = new Field("", "PAGES", 4, 10);
/* 3886 */     fields[12] = new Field("", "FILTER_CONDITION", 1, 32);
/* 3887 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getJDBCMajorVersion()
/*      */     throws SQLException
/*      */   {
/* 3894 */     return 4;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getJDBCMinorVersion()
/*      */     throws SQLException
/*      */   {
/* 3901 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxBinaryLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 3912 */     return 16777208;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCatalogNameLength()
/*      */     throws SQLException
/*      */   {
/* 3923 */     return 32;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCharLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 3934 */     return 16777208;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnNameLength()
/*      */     throws SQLException
/*      */   {
/* 3945 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInGroupBy()
/*      */     throws SQLException
/*      */   {
/* 3956 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInIndex()
/*      */     throws SQLException
/*      */   {
/* 3967 */     return 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInOrderBy()
/*      */     throws SQLException
/*      */   {
/* 3978 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInSelect()
/*      */     throws SQLException
/*      */   {
/* 3989 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInTable()
/*      */     throws SQLException
/*      */   {
/* 4000 */     return 512;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxConnections()
/*      */     throws SQLException
/*      */   {
/* 4011 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCursorNameLength()
/*      */     throws SQLException
/*      */   {
/* 4022 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxIndexLength()
/*      */     throws SQLException
/*      */   {
/* 4033 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxProcedureNameLength()
/*      */     throws SQLException
/*      */   {
/* 4044 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxRowSize()
/*      */     throws SQLException
/*      */   {
/* 4055 */     return 2147483639;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSchemaNameLength()
/*      */     throws SQLException
/*      */   {
/* 4066 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStatementLength()
/*      */     throws SQLException
/*      */   {
/* 4077 */     return MysqlIO.getMaxBuf() - 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStatements()
/*      */     throws SQLException
/*      */   {
/* 4088 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxTableNameLength()
/*      */     throws SQLException
/*      */   {
/* 4099 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxTablesInSelect()
/*      */     throws SQLException
/*      */   {
/* 4110 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxUserNameLength()
/*      */     throws SQLException
/*      */   {
/* 4121 */     return 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNumericFunctions()
/*      */     throws SQLException
/*      */   {
/* 4132 */     return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getPrimaryKeys(String catalog, String schema, final String table)
/*      */     throws SQLException
/*      */   {
/* 4164 */     Field[] fields = new Field[6];
/* 4165 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 4166 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 4167 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 4168 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 4169 */     fields[4] = new Field("", "KEY_SEQ", 5, 5);
/* 4170 */     fields[5] = new Field("", "PK_NAME", 1, 32);
/*      */     
/* 4172 */     if (table == null) {
/* 4173 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 4177 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/* 4178 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 4182 */       new IterateBlock(getCatalogIterator(catalog)) {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 4184 */           ResultSet rs = null;
/*      */           
/*      */           try
/*      */           {
/* 4188 */             StringBuffer queryBuf = new StringBuffer("SHOW KEYS FROM ");
/*      */             
/* 4190 */             queryBuf.append(StringUtils.quoteIdentifier(table, DatabaseMetaData.this.conn.getPedantic()));
/* 4191 */             queryBuf.append(" FROM ");
/* 4192 */             queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/*      */             
/* 4194 */             rs = stmt.executeQuery(queryBuf.toString());
/*      */             
/* 4196 */             TreeMap<String, byte[][]> sortMap = new TreeMap();
/*      */             
/* 4198 */             while (rs.next()) {
/* 4199 */               String keyType = rs.getString("Key_name");
/*      */               
/* 4201 */               if ((keyType != null) && (
/* 4202 */                 (keyType.equalsIgnoreCase("PRIMARY")) || (keyType.equalsIgnoreCase("PRI"))))
/*      */               {
/* 4204 */                 byte[][] tuple = new byte[6][];
/* 4205 */                 tuple[0] = (catalogStr == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr));
/*      */                 
/* 4207 */                 tuple[1] = null;
/* 4208 */                 tuple[2] = DatabaseMetaData.this.s2b(table);
/*      */                 
/* 4210 */                 String columnName = rs.getString("Column_name");
/*      */                 
/* 4212 */                 tuple[3] = DatabaseMetaData.this.s2b(columnName);
/* 4213 */                 tuple[4] = DatabaseMetaData.this.s2b(rs.getString("Seq_in_index"));
/* 4214 */                 tuple[5] = DatabaseMetaData.this.s2b(keyType);
/* 4215 */                 sortMap.put(columnName, tuple);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4221 */             Iterator<byte[][]> sortedIterator = sortMap.values().iterator();
/*      */             
/* 4223 */             while (sortedIterator.hasNext()) {
/* 4224 */               rows.add(new ByteArrayRow((byte[][])sortedIterator.next(), DatabaseMetaData.this.getExceptionInterceptor()));
/*      */             }
/*      */           }
/*      */           finally {
/* 4228 */             if (rs != null) {
/*      */               try {
/* 4230 */                 rs.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 4235 */               rs = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 4241 */       if (stmt != null) {
/* 4242 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 4246 */     ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 4248 */     return results;
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
/*      */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 4331 */     Field[] fields = createProcedureColumnsFields();
/*      */     
/* 4333 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Field[] createProcedureColumnsFields()
/*      */   {
/* 4340 */     Field[] fields = new Field[20];
/*      */     
/* 4342 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 512);
/* 4343 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 512);
/* 4344 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 512);
/* 4345 */     fields[3] = new Field("", "COLUMN_NAME", 1, 512);
/* 4346 */     fields[4] = new Field("", "COLUMN_TYPE", 1, 64);
/* 4347 */     fields[5] = new Field("", "DATA_TYPE", 5, 6);
/* 4348 */     fields[6] = new Field("", "TYPE_NAME", 1, 64);
/* 4349 */     fields[7] = new Field("", "PRECISION", 4, 12);
/* 4350 */     fields[8] = new Field("", "LENGTH", 4, 12);
/* 4351 */     fields[9] = new Field("", "SCALE", 5, 12);
/* 4352 */     fields[10] = new Field("", "RADIX", 5, 6);
/* 4353 */     fields[11] = new Field("", "NULLABLE", 5, 6);
/* 4354 */     fields[12] = new Field("", "REMARKS", 1, 512);
/* 4355 */     fields[13] = new Field("", "COLUMN_DEF", 1, 512);
/* 4356 */     fields[14] = new Field("", "SQL_DATA_TYPE", 4, 12);
/* 4357 */     fields[15] = new Field("", "SQL_DATETIME_SUB", 4, 12);
/* 4358 */     fields[16] = new Field("", "CHAR_OCTET_LENGTH", 4, 12);
/* 4359 */     fields[17] = new Field("", "ORDINAL_POSITION", 4, 12);
/* 4360 */     fields[18] = new Field("", "IS_NULLABLE", 1, 512);
/* 4361 */     fields[19] = new Field("", "SPECIFIC_NAME", 1, 512);
/* 4362 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSet getProcedureOrFunctionColumns(Field[] fields, String catalog, String schemaPattern, String procedureOrFunctionNamePattern, String columnNamePattern, boolean returnProcedures, boolean returnFunctions)
/*      */     throws SQLException
/*      */   {
/* 4371 */     List<ComparableWrapper<String, ProcedureType>> procsOrFuncsToExtractList = new ArrayList();
/*      */     
/* 4373 */     ResultSet procsAndOrFuncsRs = null;
/*      */     
/* 4375 */     if (supportsStoredProcedures())
/*      */     {
/*      */       try
/*      */       {
/* 4379 */         String tmpProcedureOrFunctionNamePattern = null;
/*      */         
/* 4381 */         if ((procedureOrFunctionNamePattern != null) && (!procedureOrFunctionNamePattern.equals("%"))) {
/* 4382 */           tmpProcedureOrFunctionNamePattern = StringUtils.sanitizeProcOrFuncName(procedureOrFunctionNamePattern);
/*      */         }
/*      */         
/*      */ 
/* 4386 */         if (tmpProcedureOrFunctionNamePattern == null) {
/* 4387 */           tmpProcedureOrFunctionNamePattern = procedureOrFunctionNamePattern;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 4392 */           String tmpCatalog = catalog;
/* 4393 */           List<String> parseList = StringUtils.splitDBdotName(tmpProcedureOrFunctionNamePattern, tmpCatalog, this.quotedId, this.conn.isNoBackslashEscapesSet());
/*      */           
/*      */ 
/*      */ 
/* 4397 */           if (parseList.size() == 2) {
/* 4398 */             tmpCatalog = (String)parseList.get(0);
/* 4399 */             tmpProcedureOrFunctionNamePattern = (String)parseList.get(1);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4405 */         procsAndOrFuncsRs = getProceduresAndOrFunctions(createFieldMetadataForGetProcedures(), catalog, schemaPattern, tmpProcedureOrFunctionNamePattern, returnProcedures, returnFunctions);
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
/* 4417 */         String tmpstrPNameRs = null;
/* 4418 */         String tmpstrCatNameRs = null;
/*      */         
/* 4420 */         boolean hasResults = false;
/* 4421 */         while (procsAndOrFuncsRs.next()) {
/* 4422 */           tmpstrCatNameRs = procsAndOrFuncsRs.getString(1);
/* 4423 */           tmpstrPNameRs = procsAndOrFuncsRs.getString(3);
/*      */           
/* 4425 */           if (((!tmpstrCatNameRs.startsWith(this.quotedId)) || (!tmpstrCatNameRs.endsWith(this.quotedId))) && ((!tmpstrCatNameRs.startsWith("\"")) || (!tmpstrCatNameRs.endsWith("\""))))
/*      */           {
/* 4427 */             tmpstrCatNameRs = this.quotedId + tmpstrCatNameRs + this.quotedId;
/*      */           }
/* 4429 */           if (((!tmpstrPNameRs.startsWith(this.quotedId)) || (!tmpstrPNameRs.endsWith(this.quotedId))) && ((!tmpstrPNameRs.startsWith("\"")) || (!tmpstrPNameRs.endsWith("\""))))
/*      */           {
/* 4431 */             tmpstrPNameRs = this.quotedId + tmpstrPNameRs + this.quotedId;
/*      */           }
/*      */           
/* 4434 */           procsOrFuncsToExtractList.add(new ComparableWrapper(tmpstrCatNameRs + "." + tmpstrPNameRs, procsAndOrFuncsRs.getShort(8) == 1 ? ProcedureType.PROCEDURE : ProcedureType.FUNCTION));
/*      */           
/*      */ 
/*      */ 
/* 4438 */           hasResults = true;
/*      */         }
/*      */         
/*      */ 
/* 4442 */         if (hasResults)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4449 */           Collections.sort(procsOrFuncsToExtractList);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/* 4458 */         SQLException rethrowSqlEx = null;
/*      */         
/* 4460 */         if (procsAndOrFuncsRs != null) {
/*      */           try {
/* 4462 */             procsAndOrFuncsRs.close();
/*      */           } catch (SQLException sqlEx) {
/* 4464 */             rethrowSqlEx = sqlEx;
/*      */           }
/*      */         }
/*      */         
/* 4468 */         if (rethrowSqlEx != null) {
/* 4469 */           throw rethrowSqlEx;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4474 */     ArrayList<ResultSetRow> resultRows = new ArrayList();
/* 4475 */     int idx = 0;
/* 4476 */     String procNameToCall = "";
/*      */     
/* 4478 */     for (Object procOrFunc : procsOrFuncsToExtractList) {
/* 4479 */       String procName = (String)((ComparableWrapper)procOrFunc).getKey();
/* 4480 */       ProcedureType procType = (ProcedureType)((ComparableWrapper)procOrFunc).getValue();
/*      */       
/*      */ 
/* 4483 */       if (!" ".equals(this.quotedId)) {
/* 4484 */         idx = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", this.quotedId.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       }
/*      */       else
/*      */       {
/* 4488 */         idx = procName.indexOf(".");
/*      */       }
/*      */       
/* 4491 */       if (idx > 0) {
/* 4492 */         catalog = procName.substring(0, idx);
/* 4493 */         if ((this.quotedId != " ") && (catalog.startsWith(this.quotedId)) && (catalog.endsWith(this.quotedId))) {
/* 4494 */           catalog = procName.substring(1, catalog.length() - 1);
/*      */         }
/* 4496 */         procNameToCall = procName;
/*      */       }
/*      */       else {
/* 4499 */         procNameToCall = procName;
/*      */       }
/* 4501 */       getCallStmtParameterTypes(catalog, procNameToCall, procType, columnNamePattern, resultRows, fields.length == 17);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4506 */     return buildResultSet(fields, resultRows);
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
/*      */   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
/*      */     throws SQLException
/*      */   {
/* 4552 */     Field[] fields = createFieldMetadataForGetProcedures();
/*      */     
/* 4554 */     return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, true);
/*      */   }
/*      */   
/*      */   protected Field[] createFieldMetadataForGetProcedures()
/*      */   {
/* 4559 */     Field[] fields = new Field[9];
/* 4560 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 255);
/* 4561 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 255);
/* 4562 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 255);
/* 4563 */     fields[3] = new Field("", "reserved1", 1, 0);
/* 4564 */     fields[4] = new Field("", "reserved2", 1, 0);
/* 4565 */     fields[5] = new Field("", "reserved3", 1, 0);
/* 4566 */     fields[6] = new Field("", "REMARKS", 1, 255);
/* 4567 */     fields[7] = new Field("", "PROCEDURE_TYPE", 5, 6);
/* 4568 */     fields[8] = new Field("", "SPECIFIC_NAME", 1, 255);
/*      */     
/* 4570 */     return fields;
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
/*      */   protected ResultSet getProceduresAndOrFunctions(final Field[] fields, String catalog, String schemaPattern, String procedureNamePattern, final boolean returnProcedures, final boolean returnFunctions)
/*      */     throws SQLException
/*      */   {
/* 4591 */     if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0))
/*      */     {
/* 4593 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4594 */         procedureNamePattern = "%";
/*      */       } else {
/* 4596 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4602 */     final ArrayList<ResultSetRow> procedureRows = new ArrayList();
/*      */     
/* 4604 */     if (supportsStoredProcedures()) {
/* 4605 */       final String procNamePattern = procedureNamePattern;
/*      */       
/* 4607 */       final List<ComparableWrapper<String, ResultSetRow>> procedureRowsToSort = new ArrayList();
/*      */       
/* 4609 */       new IterateBlock(getCatalogIterator(catalog)) {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 4611 */           String db = catalogStr;
/*      */           
/* 4613 */           boolean fromSelect = false;
/* 4614 */           ResultSet proceduresRs = null;
/* 4615 */           boolean needsClientFiltering = true;
/*      */           
/* 4617 */           StringBuffer selectFromMySQLProcSQL = new StringBuffer();
/*      */           
/* 4619 */           selectFromMySQLProcSQL.append("SELECT name, type, comment FROM mysql.proc WHERE ");
/* 4620 */           if ((returnProcedures) && (!returnFunctions)) {
/* 4621 */             selectFromMySQLProcSQL.append("type = 'PROCEDURE' and ");
/* 4622 */           } else if ((!returnProcedures) && (returnFunctions)) {
/* 4623 */             selectFromMySQLProcSQL.append("type = 'FUNCTION' and ");
/*      */           }
/* 4625 */           selectFromMySQLProcSQL.append("name like ? and db <=> ? ORDER BY name, type");
/*      */           
/* 4627 */           PreparedStatement proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement(selectFromMySQLProcSQL.toString());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           try
/*      */           {
/* 4636 */             boolean hasTypeColumn = false;
/*      */             
/* 4638 */             if (db != null) {
/* 4639 */               if (DatabaseMetaData.this.conn.lowerCaseTableNames()) {
/* 4640 */                 db = db.toLowerCase();
/*      */               }
/* 4642 */               proceduresStmt.setString(2, db);
/*      */             } else {
/* 4644 */               proceduresStmt.setNull(2, 12);
/*      */             }
/*      */             
/* 4647 */             int nameIndex = 1;
/*      */             
/* 4649 */             if (proceduresStmt.getMaxRows() != 0) {
/* 4650 */               proceduresStmt.setMaxRows(0);
/*      */             }
/*      */             
/* 4653 */             proceduresStmt.setString(1, procNamePattern);
/*      */             try
/*      */             {
/* 4656 */               proceduresRs = proceduresStmt.executeQuery();
/* 4657 */               fromSelect = true;
/* 4658 */               needsClientFiltering = false;
/* 4659 */               hasTypeColumn = true;
/*      */ 
/*      */ 
/*      */             }
/*      */             catch (SQLException sqlEx)
/*      */             {
/*      */ 
/*      */ 
/* 4667 */               proceduresStmt.close();
/*      */               
/* 4669 */               fromSelect = false;
/*      */               
/* 4671 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 1)) {
/* 4672 */                 nameIndex = 2;
/*      */               } else {
/* 4674 */                 nameIndex = 1;
/*      */               }
/*      */               
/* 4677 */               proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW PROCEDURE STATUS LIKE ?");
/*      */               
/*      */ 
/* 4680 */               if (proceduresStmt.getMaxRows() != 0) {
/* 4681 */                 proceduresStmt.setMaxRows(0);
/*      */               }
/*      */               
/* 4684 */               proceduresStmt.setString(1, procNamePattern);
/*      */               
/* 4686 */               proceduresRs = proceduresStmt.executeQuery();
/*      */             }
/*      */             
/* 4689 */             if (returnProcedures) {
/* 4690 */               DatabaseMetaData.this.convertToJdbcProcedureList(fromSelect, db, proceduresRs, needsClientFiltering, db, procedureRowsToSort, nameIndex);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4695 */             if (!hasTypeColumn)
/*      */             {
/* 4697 */               if (proceduresStmt != null) {
/* 4698 */                 proceduresStmt.close();
/*      */               }
/*      */               
/* 4701 */               proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW FUNCTION STATUS LIKE ?");
/*      */               
/*      */ 
/* 4704 */               if (proceduresStmt.getMaxRows() != 0) {
/* 4705 */                 proceduresStmt.setMaxRows(0);
/*      */               }
/*      */               
/* 4708 */               proceduresStmt.setString(1, procNamePattern);
/*      */               
/* 4710 */               proceduresRs = proceduresStmt.executeQuery();
/*      */             }
/*      */             
/*      */ 
/* 4714 */             if (returnFunctions) {
/* 4715 */               DatabaseMetaData.this.convertToJdbcFunctionList(db, proceduresRs, needsClientFiltering, db, procedureRowsToSort, nameIndex, fields);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4722 */             Collections.sort(procedureRowsToSort);
/* 4723 */             for (DatabaseMetaData.ComparableWrapper<String, ResultSetRow> procRow : procedureRowsToSort) {
/* 4724 */               procedureRows.add(procRow.getValue());
/*      */             }
/*      */           } finally {
/* 4727 */             SQLException rethrowSqlEx = null;
/*      */             
/* 4729 */             if (proceduresRs != null) {
/*      */               try {
/* 4731 */                 proceduresRs.close();
/*      */               } catch (SQLException sqlEx) {
/* 4733 */                 rethrowSqlEx = sqlEx;
/*      */               }
/*      */             }
/*      */             
/* 4737 */             if (proceduresStmt != null) {
/*      */               try {
/* 4739 */                 proceduresStmt.close();
/*      */               } catch (SQLException sqlEx) {
/* 4741 */                 rethrowSqlEx = sqlEx;
/*      */               }
/*      */             }
/*      */             
/* 4745 */             if (rethrowSqlEx != null) {
/* 4746 */               throw rethrowSqlEx;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     }
/*      */     
/* 4753 */     return buildResultSet(fields, procedureRows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProcedureTerm()
/*      */     throws SQLException
/*      */   {
/* 4765 */     return "PROCEDURE";
/*      */   }
/*      */   
/*      */ 
/*      */   public int getResultSetHoldability()
/*      */     throws SQLException
/*      */   {
/* 4772 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private void getResultsImpl(String catalog, String table, String keysComment, List<ResultSetRow> tuples, String fkTableName, boolean isExport)
/*      */     throws SQLException
/*      */   {
/* 4779 */     LocalAndReferencedColumns parsedInfo = parseTableStatusIntoLocalAndReferencedColumns(keysComment);
/*      */     
/* 4781 */     if ((isExport) && (!parsedInfo.referencedTable.equals(table))) {
/* 4782 */       return;
/*      */     }
/*      */     
/* 4785 */     if (parsedInfo.localColumnsList.size() != parsedInfo.referencedColumnsList.size())
/*      */     {
/* 4787 */       throw SQLError.createSQLException("Error parsing foreign keys definition,number of local and referenced columns is not the same.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4793 */     Iterator<String> localColumnNames = parsedInfo.localColumnsList.iterator();
/* 4794 */     Iterator<String> referColumnNames = parsedInfo.referencedColumnsList.iterator();
/*      */     
/* 4796 */     int keySeqIndex = 1;
/*      */     
/* 4798 */     while (localColumnNames.hasNext()) {
/* 4799 */       byte[][] tuple = new byte[14][];
/* 4800 */       String lColumnName = StringUtils.unQuoteIdentifier((String)localColumnNames.next(), this.conn.useAnsiQuotedIdentifiers());
/* 4801 */       String rColumnName = StringUtils.unQuoteIdentifier((String)referColumnNames.next(), this.conn.useAnsiQuotedIdentifiers());
/* 4802 */       tuple[4] = (catalog == null ? new byte[0] : s2b(catalog));
/*      */       
/* 4804 */       tuple[5] = null;
/* 4805 */       tuple[6] = s2b(isExport ? fkTableName : table);
/* 4806 */       tuple[7] = s2b(lColumnName);
/* 4807 */       tuple[0] = s2b(parsedInfo.referencedCatalog);
/* 4808 */       tuple[1] = null;
/* 4809 */       tuple[2] = s2b(isExport ? table : parsedInfo.referencedTable);
/*      */       
/* 4811 */       tuple[3] = s2b(rColumnName);
/* 4812 */       tuple[8] = s2b(Integer.toString(keySeqIndex++));
/*      */       
/* 4814 */       int[] actions = getForeignKeyActions(keysComment);
/*      */       
/* 4816 */       tuple[9] = s2b(Integer.toString(actions[1]));
/* 4817 */       tuple[10] = s2b(Integer.toString(actions[0]));
/* 4818 */       tuple[11] = s2b(parsedInfo.constraintName);
/* 4819 */       tuple[12] = null;
/* 4820 */       tuple[13] = s2b(Integer.toString(7));
/*      */       
/* 4822 */       tuples.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
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
/*      */   public ResultSet getSchemas()
/*      */     throws SQLException
/*      */   {
/* 4842 */     Field[] fields = new Field[2];
/* 4843 */     fields[0] = new Field("", "TABLE_SCHEM", 1, 0);
/* 4844 */     fields[1] = new Field("", "TABLE_CATALOG", 1, 0);
/*      */     
/* 4846 */     ArrayList<ResultSetRow> tuples = new ArrayList();
/* 4847 */     ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 4849 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSchemaTerm()
/*      */     throws SQLException
/*      */   {
/* 4860 */     return "";
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
/*      */   public String getSearchStringEscape()
/*      */     throws SQLException
/*      */   {
/* 4878 */     return "\\";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSQLKeywords()
/*      */     throws SQLException
/*      */   {
/* 4889 */     if (mysqlKeywords != null) {
/* 4890 */       return mysqlKeywords;
/*      */     }
/*      */     
/* 4893 */     synchronized (DatabaseMetaData.class)
/*      */     {
/* 4895 */       if (mysqlKeywords != null) {
/* 4896 */         return mysqlKeywords;
/*      */       }
/*      */       
/* 4899 */       Set<String> mysqlKeywordSet = new TreeSet();
/* 4900 */       StringBuffer mysqlKeywordsBuffer = new StringBuffer();
/*      */       
/* 4902 */       Collections.addAll(mysqlKeywordSet, MYSQL_KEYWORDS);
/* 4903 */       mysqlKeywordSet.removeAll(Arrays.asList(Util.isJdbc4() ? SQL2003_KEYWORDS : SQL92_KEYWORDS));
/*      */       
/* 4905 */       for (String keyword : mysqlKeywordSet) {
/* 4906 */         mysqlKeywordsBuffer.append(",").append(keyword);
/*      */       }
/*      */       
/* 4909 */       mysqlKeywords = mysqlKeywordsBuffer.substring(1);
/* 4910 */       return mysqlKeywords;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int getSQLStateType()
/*      */     throws SQLException
/*      */   {
/* 4918 */     if (this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 4919 */       return 2;
/*      */     }
/*      */     
/* 4922 */     if (this.conn.getUseSqlStateCodes()) {
/* 4923 */       return 2;
/*      */     }
/*      */     
/* 4926 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringFunctions()
/*      */     throws SQLException
/*      */   {
/* 4937 */     return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
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
/*      */   public ResultSet getSuperTables(String arg0, String arg1, String arg2)
/*      */     throws SQLException
/*      */   {
/* 4951 */     Field[] fields = new Field[4];
/* 4952 */     fields[0] = new Field("", "TABLE_CAT", 1, 32);
/* 4953 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 32);
/* 4954 */     fields[2] = new Field("", "TABLE_NAME", 1, 32);
/* 4955 */     fields[3] = new Field("", "SUPERTABLE_NAME", 1, 32);
/*      */     
/* 4957 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ResultSet getSuperTypes(String arg0, String arg1, String arg2)
/*      */     throws SQLException
/*      */   {
/* 4965 */     Field[] fields = new Field[6];
/* 4966 */     fields[0] = new Field("", "TYPE_CAT", 1, 32);
/* 4967 */     fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
/* 4968 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 4969 */     fields[3] = new Field("", "SUPERTYPE_CAT", 1, 32);
/* 4970 */     fields[4] = new Field("", "SUPERTYPE_SCHEM", 1, 32);
/* 4971 */     fields[5] = new Field("", "SUPERTYPE_NAME", 1, 32);
/*      */     
/* 4973 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSystemFunctions()
/*      */     throws SQLException
/*      */   {
/* 4984 */     return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
/*      */   }
/*      */   
/*      */   protected String getTableNameWithCase(String table) {
/* 4988 */     String tableNameWithCase = this.conn.lowerCaseTableNames() ? table.toLowerCase() : table;
/*      */     
/*      */ 
/* 4991 */     return tableNameWithCase;
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
/*      */   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
/*      */     throws SQLException
/*      */   {
/* 5031 */     if (tableNamePattern == null) {
/* 5032 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 5033 */         tableNamePattern = "%";
/*      */       } else {
/* 5035 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5041 */     Field[] fields = new Field[7];
/* 5042 */     fields[0] = new Field("", "TABLE_CAT", 1, 64);
/* 5043 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
/* 5044 */     fields[2] = new Field("", "TABLE_NAME", 1, 64);
/* 5045 */     fields[3] = new Field("", "GRANTOR", 1, 77);
/* 5046 */     fields[4] = new Field("", "GRANTEE", 1, 77);
/* 5047 */     fields[5] = new Field("", "PRIVILEGE", 1, 64);
/* 5048 */     fields[6] = new Field("", "IS_GRANTABLE", 1, 3);
/*      */     
/* 5050 */     StringBuffer grantQuery = new StringBuffer("SELECT host,db,table_name,grantor,user,table_priv from mysql.tables_priv ");
/*      */     
/* 5052 */     grantQuery.append(" WHERE ");
/*      */     
/* 5054 */     if ((catalog != null) && (catalog.length() != 0)) {
/* 5055 */       grantQuery.append(" db='");
/* 5056 */       grantQuery.append(catalog);
/* 5057 */       grantQuery.append("' AND ");
/*      */     }
/*      */     
/* 5060 */     grantQuery.append("table_name like '");
/* 5061 */     grantQuery.append(tableNamePattern);
/* 5062 */     grantQuery.append("'");
/*      */     
/* 5064 */     ResultSet results = null;
/* 5065 */     ArrayList<ResultSetRow> grantRows = new ArrayList();
/* 5066 */     java.sql.Statement stmt = null;
/*      */     try
/*      */     {
/* 5069 */       stmt = this.conn.createStatement();
/* 5070 */       stmt.setEscapeProcessing(false);
/*      */       
/* 5072 */       results = stmt.executeQuery(grantQuery.toString());
/*      */       
/* 5074 */       while (results.next()) {
/* 5075 */         String host = results.getString(1);
/* 5076 */         String db = results.getString(2);
/* 5077 */         String table = results.getString(3);
/* 5078 */         String grantor = results.getString(4);
/* 5079 */         String user = results.getString(5);
/*      */         
/* 5081 */         if ((user == null) || (user.length() == 0)) {
/* 5082 */           user = "%";
/*      */         }
/*      */         
/* 5085 */         StringBuffer fullUser = new StringBuffer(user);
/*      */         
/* 5087 */         if ((host != null) && (this.conn.getUseHostsInPrivileges())) {
/* 5088 */           fullUser.append("@");
/* 5089 */           fullUser.append(host);
/*      */         }
/*      */         
/* 5092 */         String allPrivileges = results.getString(6);
/*      */         
/* 5094 */         if (allPrivileges != null) {
/* 5095 */           allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
/*      */           
/* 5097 */           StringTokenizer st = new StringTokenizer(allPrivileges, ",");
/*      */           
/* 5099 */           while (st.hasMoreTokens()) {
/* 5100 */             String privilege = st.nextToken().trim();
/*      */             
/*      */ 
/* 5103 */             ResultSet columnResults = null;
/*      */             try
/*      */             {
/* 5106 */               columnResults = getColumns(catalog, schemaPattern, table, "%");
/*      */               
/*      */ 
/* 5109 */               while (columnResults.next()) {
/* 5110 */                 byte[][] tuple = new byte[8][];
/* 5111 */                 tuple[0] = s2b(db);
/* 5112 */                 tuple[1] = null;
/* 5113 */                 tuple[2] = s2b(table);
/*      */                 
/* 5115 */                 if (grantor != null) {
/* 5116 */                   tuple[3] = s2b(grantor);
/*      */                 } else {
/* 5118 */                   tuple[3] = null;
/*      */                 }
/*      */                 
/* 5121 */                 tuple[4] = s2b(fullUser.toString());
/* 5122 */                 tuple[5] = s2b(privilege);
/* 5123 */                 tuple[6] = null;
/* 5124 */                 grantRows.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
/*      */               }
/*      */             } finally {
/* 5127 */               if (columnResults != null) {
/*      */                 try {
/* 5129 */                   columnResults.close();
/*      */                 }
/*      */                 catch (Exception ex) {}
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     finally {
/* 5139 */       if (results != null) {
/*      */         try {
/* 5141 */           results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 5146 */         results = null;
/*      */       }
/*      */       
/* 5149 */       if (stmt != null) {
/*      */         try {
/* 5151 */           stmt.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 5156 */         stmt = null;
/*      */       }
/*      */     }
/*      */     
/* 5160 */     return buildResultSet(fields, grantRows);
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
/*      */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, final String[] types)
/*      */     throws SQLException
/*      */   {
/* 5202 */     if (tableNamePattern == null) {
/* 5203 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 5204 */         tableNamePattern = "%";
/*      */       } else {
/* 5206 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5212 */     final SortedMap<TableMetaDataKey, ResultSetRow> sortedRows = new TreeMap();
/* 5213 */     ArrayList<ResultSetRow> tuples = new ArrayList();
/*      */     
/* 5215 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */ 
/* 5218 */     String tmpCat = "";
/*      */     
/* 5220 */     if ((catalog == null) || (catalog.length() == 0)) {
/* 5221 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 5222 */         tmpCat = this.database;
/*      */       }
/*      */     } else {
/* 5225 */       tmpCat = catalog;
/*      */     }
/*      */     
/* 5228 */     List<String> parseList = StringUtils.splitDBdotName(tableNamePattern, tmpCat, this.quotedId, this.conn.isNoBackslashEscapesSet());
/*      */     String tableNamePat;
/*      */     final String tableNamePat;
/* 5231 */     if (parseList.size() == 2) {
/* 5232 */       tableNamePat = (String)parseList.get(1);
/*      */     } else {
/* 5234 */       tableNamePat = tableNamePattern;
/*      */     }
/*      */     try
/*      */     {
/* 5238 */       new IterateBlock(getCatalogIterator(catalog)) {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 5240 */           boolean operatingOnSystemDB = ("information_schema".equalsIgnoreCase(catalogStr)) || ("mysql".equalsIgnoreCase(catalogStr)) || ("performance_schema".equalsIgnoreCase(catalogStr));
/*      */           
/*      */ 
/*      */ 
/* 5244 */           ResultSet results = null;
/*      */           try
/*      */           {
/*      */             try
/*      */             {
/* 5249 */               results = stmt.executeQuery((!DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2) ? "SHOW TABLES FROM " : "SHOW FULL TABLES FROM ") + StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()) + " LIKE '" + tableNamePat + "'");
/*      */ 
/*      */             }
/*      */             catch (SQLException sqlEx)
/*      */             {
/*      */ 
/* 5255 */               if ("08S01".equals(sqlEx.getSQLState())) {
/* 5256 */                 throw sqlEx;
/*      */               }
/*      */               
/* 5259 */               return;
/*      */             }
/*      */             
/* 5262 */             boolean shouldReportTables = false;
/* 5263 */             boolean shouldReportViews = false;
/* 5264 */             boolean shouldReportSystemTables = false;
/* 5265 */             boolean shouldReportSystemViews = false;
/* 5266 */             boolean shouldReportLocalTemporaries = false;
/*      */             
/* 5268 */             if ((types == null) || (types.length == 0)) {
/* 5269 */               shouldReportTables = true;
/* 5270 */               shouldReportViews = true;
/* 5271 */               shouldReportSystemTables = true;
/* 5272 */               shouldReportSystemViews = true;
/* 5273 */               shouldReportLocalTemporaries = true;
/*      */             } else {
/* 5275 */               for (int i = 0; i < types.length; i++) {
/* 5276 */                 if (DatabaseMetaData.TableType.TABLE.equalsTo(types[i])) {
/* 5277 */                   shouldReportTables = true;
/*      */                 }
/* 5279 */                 else if (DatabaseMetaData.TableType.VIEW.equalsTo(types[i])) {
/* 5280 */                   shouldReportViews = true;
/*      */                 }
/* 5282 */                 else if (DatabaseMetaData.TableType.SYSTEM_TABLE.equalsTo(types[i])) {
/* 5283 */                   shouldReportSystemTables = true;
/*      */                 }
/* 5285 */                 else if (DatabaseMetaData.TableType.SYSTEM_VIEW.equalsTo(types[i])) {
/* 5286 */                   shouldReportSystemViews = true;
/*      */                 }
/* 5288 */                 else if (DatabaseMetaData.TableType.LOCAL_TEMPORARY.equalsTo(types[i])) {
/* 5289 */                   shouldReportLocalTemporaries = true;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 5294 */             int typeColumnIndex = 0;
/* 5295 */             boolean hasTableTypes = false;
/*      */             
/* 5297 */             if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2))
/*      */             {
/*      */               try
/*      */               {
/*      */ 
/* 5302 */                 typeColumnIndex = results.findColumn("table_type");
/*      */                 
/* 5304 */                 hasTableTypes = true;
/*      */ 
/*      */ 
/*      */               }
/*      */               catch (SQLException sqlEx)
/*      */               {
/*      */ 
/*      */ 
/*      */                 try
/*      */                 {
/*      */ 
/*      */ 
/* 5316 */                   typeColumnIndex = results.findColumn("Type");
/*      */                   
/* 5318 */                   hasTableTypes = true;
/*      */                 } catch (SQLException sqlEx2) {
/* 5320 */                   hasTableTypes = false;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 5325 */             while (results.next()) {
/* 5326 */               byte[][] row = new byte[10][];
/* 5327 */               row[0] = (catalogStr == null ? null : DatabaseMetaData.this.s2b(catalogStr));
/*      */               
/* 5329 */               row[1] = null;
/* 5330 */               row[2] = results.getBytes(1);
/* 5331 */               row[4] = new byte[0];
/* 5332 */               row[5] = null;
/* 5333 */               row[6] = null;
/* 5334 */               row[7] = null;
/* 5335 */               row[8] = null;
/* 5336 */               row[9] = null;
/*      */               
/* 5338 */               if (hasTableTypes) {
/* 5339 */                 String tableType = results.getString(typeColumnIndex);
/*      */                 
/*      */ 
/* 5342 */                 switch (DatabaseMetaData.11.$SwitchMap$com$mysql$jdbc$DatabaseMetaData$TableType[DatabaseMetaData.TableType.getTableTypeCompliantWith(tableType).ordinal()]) {
/*      */                 case 1: 
/* 5344 */                   boolean reportTable = false;
/* 5345 */                   DatabaseMetaData.TableMetaDataKey tablesKey = null;
/*      */                   
/* 5347 */                   if ((operatingOnSystemDB) && (shouldReportSystemTables)) {
/* 5348 */                     row[3] = DatabaseMetaData.TableType.SYSTEM_TABLE.asBytes();
/* 5349 */                     tablesKey = new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.SYSTEM_TABLE.getName(), catalogStr, null, results.getString(1));
/*      */                     
/* 5351 */                     reportTable = true;
/*      */                   }
/* 5353 */                   else if ((!operatingOnSystemDB) && (shouldReportTables)) {
/* 5354 */                     row[3] = DatabaseMetaData.TableType.TABLE.asBytes();
/* 5355 */                     tablesKey = new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.TABLE.getName(), catalogStr, null, results.getString(1));
/*      */                     
/* 5357 */                     reportTable = true;
/*      */                   }
/*      */                   
/* 5360 */                   if (reportTable) {
/* 5361 */                     sortedRows.put(tablesKey, new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                   }
/*      */                   
/*      */                   break;
/*      */                 case 2: 
/* 5366 */                   if (shouldReportViews) {
/* 5367 */                     row[3] = DatabaseMetaData.TableType.VIEW.asBytes();
/* 5368 */                     sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.VIEW.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */                   break;
/*      */                 case 3: 
/* 5375 */                   if (shouldReportSystemTables) {
/* 5376 */                     row[3] = DatabaseMetaData.TableType.SYSTEM_TABLE.asBytes();
/* 5377 */                     sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.SYSTEM_TABLE.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */                   break;
/*      */                 case 4: 
/* 5384 */                   if (shouldReportSystemViews) {
/* 5385 */                     row[3] = DatabaseMetaData.TableType.SYSTEM_VIEW.asBytes();
/* 5386 */                     sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.SYSTEM_VIEW.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */                   break;
/*      */                 case 5: 
/* 5393 */                   if (shouldReportLocalTemporaries) {
/* 5394 */                     row[3] = DatabaseMetaData.TableType.LOCAL_TEMPORARY.asBytes();
/* 5395 */                     sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.LOCAL_TEMPORARY.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */                   break;
/*      */                 default: 
/* 5402 */                   row[3] = DatabaseMetaData.TableType.TABLE.asBytes();
/* 5403 */                   sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.TABLE.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                 
/*      */ 
/*      */                 }
/*      */                 
/*      */               }
/* 5409 */               else if (shouldReportTables)
/*      */               {
/* 5411 */                 row[3] = DatabaseMetaData.TableType.TABLE.asBytes();
/* 5412 */                 sortedRows.put(new DatabaseMetaData.TableMetaDataKey(DatabaseMetaData.this, DatabaseMetaData.TableType.TABLE.getName(), catalogStr, null, results.getString(1)), new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */               }
/*      */               
/*      */             }
/*      */           }
/*      */           finally
/*      */           {
/* 5419 */             if (results != null) {
/*      */               try {
/* 5421 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/* 5425 */               results = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 5431 */       if (stmt != null) {
/* 5432 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 5436 */     tuples.addAll(sortedRows.values());
/* 5437 */     ResultSet tables = buildResultSet(createTablesFields(), tuples);
/*      */     
/* 5439 */     return tables;
/*      */   }
/*      */   
/*      */   protected Field[] createTablesFields() {
/* 5443 */     Field[] fields = new Field[10];
/* 5444 */     fields[0] = new Field("", "TABLE_CAT", 12, 255);
/* 5445 */     fields[1] = new Field("", "TABLE_SCHEM", 12, 0);
/* 5446 */     fields[2] = new Field("", "TABLE_NAME", 12, 255);
/* 5447 */     fields[3] = new Field("", "TABLE_TYPE", 12, 5);
/* 5448 */     fields[4] = new Field("", "REMARKS", 12, 0);
/* 5449 */     fields[5] = new Field("", "TYPE_CAT", 12, 0);
/* 5450 */     fields[6] = new Field("", "TYPE_SCHEM", 12, 0);
/* 5451 */     fields[7] = new Field("", "TYPE_NAME", 12, 0);
/* 5452 */     fields[8] = new Field("", "SELF_REFERENCING_COL_NAME", 12, 0);
/* 5453 */     fields[9] = new Field("", "REF_GENERATION", 12, 0);
/* 5454 */     return fields;
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
/*      */   public ResultSet getTableTypes()
/*      */     throws SQLException
/*      */   {
/* 5475 */     ArrayList<ResultSetRow> tuples = new ArrayList();
/* 5476 */     Field[] fields = { new Field("", "TABLE_TYPE", 12, 256) };
/*      */     
/* 5478 */     boolean minVersion5_0_1 = this.conn.versionMeetsMinimum(5, 0, 1);
/*      */     
/* 5480 */     tuples.add(new ByteArrayRow(new byte[][] { TableType.LOCAL_TEMPORARY.asBytes() }, getExceptionInterceptor()));
/* 5481 */     tuples.add(new ByteArrayRow(new byte[][] { TableType.SYSTEM_TABLE.asBytes() }, getExceptionInterceptor()));
/* 5482 */     if (minVersion5_0_1) {
/* 5483 */       tuples.add(new ByteArrayRow(new byte[][] { TableType.SYSTEM_VIEW.asBytes() }, getExceptionInterceptor()));
/*      */     }
/* 5485 */     tuples.add(new ByteArrayRow(new byte[][] { TableType.TABLE.asBytes() }, getExceptionInterceptor()));
/* 5486 */     if (minVersion5_0_1) {
/* 5487 */       tuples.add(new ByteArrayRow(new byte[][] { TableType.VIEW.asBytes() }, getExceptionInterceptor()));
/*      */     }
/*      */     
/* 5490 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTimeDateFunctions()
/*      */     throws SQLException
/*      */   {
/* 5501 */     return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getTypeInfo()
/*      */     throws SQLException
/*      */   {
/* 5610 */     Field[] fields = new Field[18];
/* 5611 */     fields[0] = new Field("", "TYPE_NAME", 1, 32);
/* 5612 */     fields[1] = new Field("", "DATA_TYPE", 4, 5);
/* 5613 */     fields[2] = new Field("", "PRECISION", 4, 10);
/* 5614 */     fields[3] = new Field("", "LITERAL_PREFIX", 1, 4);
/* 5615 */     fields[4] = new Field("", "LITERAL_SUFFIX", 1, 4);
/* 5616 */     fields[5] = new Field("", "CREATE_PARAMS", 1, 32);
/* 5617 */     fields[6] = new Field("", "NULLABLE", 5, 5);
/* 5618 */     fields[7] = new Field("", "CASE_SENSITIVE", 16, 3);
/* 5619 */     fields[8] = new Field("", "SEARCHABLE", 5, 3);
/* 5620 */     fields[9] = new Field("", "UNSIGNED_ATTRIBUTE", 16, 3);
/* 5621 */     fields[10] = new Field("", "FIXED_PREC_SCALE", 16, 3);
/* 5622 */     fields[11] = new Field("", "AUTO_INCREMENT", 16, 3);
/* 5623 */     fields[12] = new Field("", "LOCAL_TYPE_NAME", 1, 32);
/* 5624 */     fields[13] = new Field("", "MINIMUM_SCALE", 5, 5);
/* 5625 */     fields[14] = new Field("", "MAXIMUM_SCALE", 5, 5);
/* 5626 */     fields[15] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 5627 */     fields[16] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 5628 */     fields[17] = new Field("", "NUM_PREC_RADIX", 4, 10);
/*      */     
/* 5630 */     byte[][] rowVal = (byte[][])null;
/* 5631 */     ArrayList<ResultSetRow> tuples = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5640 */     rowVal = new byte[18][];
/* 5641 */     rowVal[0] = s2b("BIT");
/* 5642 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */     
/*      */ 
/* 5645 */     rowVal[2] = s2b("1");
/* 5646 */     rowVal[3] = s2b("");
/* 5647 */     rowVal[4] = s2b("");
/* 5648 */     rowVal[5] = s2b("");
/* 5649 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5653 */     rowVal[7] = s2b("true");
/* 5654 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5658 */     rowVal[9] = s2b("false");
/* 5659 */     rowVal[10] = s2b("false");
/* 5660 */     rowVal[11] = s2b("false");
/* 5661 */     rowVal[12] = s2b("BIT");
/* 5662 */     rowVal[13] = s2b("0");
/* 5663 */     rowVal[14] = s2b("0");
/* 5664 */     rowVal[15] = s2b("0");
/* 5665 */     rowVal[16] = s2b("0");
/* 5666 */     rowVal[17] = s2b("10");
/* 5667 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5672 */     rowVal = new byte[18][];
/* 5673 */     rowVal[0] = s2b("BOOL");
/* 5674 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */     
/*      */ 
/* 5677 */     rowVal[2] = s2b("1");
/* 5678 */     rowVal[3] = s2b("");
/* 5679 */     rowVal[4] = s2b("");
/* 5680 */     rowVal[5] = s2b("");
/* 5681 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5685 */     rowVal[7] = s2b("true");
/* 5686 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5690 */     rowVal[9] = s2b("false");
/* 5691 */     rowVal[10] = s2b("false");
/* 5692 */     rowVal[11] = s2b("false");
/* 5693 */     rowVal[12] = s2b("BOOL");
/* 5694 */     rowVal[13] = s2b("0");
/* 5695 */     rowVal[14] = s2b("0");
/* 5696 */     rowVal[15] = s2b("0");
/* 5697 */     rowVal[16] = s2b("0");
/* 5698 */     rowVal[17] = s2b("10");
/* 5699 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5704 */     rowVal = new byte[18][];
/* 5705 */     rowVal[0] = s2b("TINYINT");
/* 5706 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */     
/*      */ 
/* 5709 */     rowVal[2] = s2b("3");
/* 5710 */     rowVal[3] = s2b("");
/* 5711 */     rowVal[4] = s2b("");
/* 5712 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5713 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5717 */     rowVal[7] = s2b("false");
/* 5718 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5722 */     rowVal[9] = s2b("true");
/* 5723 */     rowVal[10] = s2b("false");
/* 5724 */     rowVal[11] = s2b("true");
/* 5725 */     rowVal[12] = s2b("TINYINT");
/* 5726 */     rowVal[13] = s2b("0");
/* 5727 */     rowVal[14] = s2b("0");
/* 5728 */     rowVal[15] = s2b("0");
/* 5729 */     rowVal[16] = s2b("0");
/* 5730 */     rowVal[17] = s2b("10");
/* 5731 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 5733 */     rowVal = new byte[18][];
/* 5734 */     rowVal[0] = s2b("TINYINT UNSIGNED");
/* 5735 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */     
/*      */ 
/* 5738 */     rowVal[2] = s2b("3");
/* 5739 */     rowVal[3] = s2b("");
/* 5740 */     rowVal[4] = s2b("");
/* 5741 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5742 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5746 */     rowVal[7] = s2b("false");
/* 5747 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5751 */     rowVal[9] = s2b("true");
/* 5752 */     rowVal[10] = s2b("false");
/* 5753 */     rowVal[11] = s2b("true");
/* 5754 */     rowVal[12] = s2b("TINYINT UNSIGNED");
/* 5755 */     rowVal[13] = s2b("0");
/* 5756 */     rowVal[14] = s2b("0");
/* 5757 */     rowVal[15] = s2b("0");
/* 5758 */     rowVal[16] = s2b("0");
/* 5759 */     rowVal[17] = s2b("10");
/* 5760 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5765 */     rowVal = new byte[18][];
/* 5766 */     rowVal[0] = s2b("BIGINT");
/* 5767 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */     
/*      */ 
/* 5770 */     rowVal[2] = s2b("19");
/* 5771 */     rowVal[3] = s2b("");
/* 5772 */     rowVal[4] = s2b("");
/* 5773 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5774 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5778 */     rowVal[7] = s2b("false");
/* 5779 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5783 */     rowVal[9] = s2b("true");
/* 5784 */     rowVal[10] = s2b("false");
/* 5785 */     rowVal[11] = s2b("true");
/* 5786 */     rowVal[12] = s2b("BIGINT");
/* 5787 */     rowVal[13] = s2b("0");
/* 5788 */     rowVal[14] = s2b("0");
/* 5789 */     rowVal[15] = s2b("0");
/* 5790 */     rowVal[16] = s2b("0");
/* 5791 */     rowVal[17] = s2b("10");
/* 5792 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 5794 */     rowVal = new byte[18][];
/* 5795 */     rowVal[0] = s2b("BIGINT UNSIGNED");
/* 5796 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */     
/*      */ 
/* 5799 */     rowVal[2] = s2b("20");
/* 5800 */     rowVal[3] = s2b("");
/* 5801 */     rowVal[4] = s2b("");
/* 5802 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5803 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5807 */     rowVal[7] = s2b("false");
/* 5808 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5812 */     rowVal[9] = s2b("true");
/* 5813 */     rowVal[10] = s2b("false");
/* 5814 */     rowVal[11] = s2b("true");
/* 5815 */     rowVal[12] = s2b("BIGINT UNSIGNED");
/* 5816 */     rowVal[13] = s2b("0");
/* 5817 */     rowVal[14] = s2b("0");
/* 5818 */     rowVal[15] = s2b("0");
/* 5819 */     rowVal[16] = s2b("0");
/* 5820 */     rowVal[17] = s2b("10");
/* 5821 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5826 */     rowVal = new byte[18][];
/* 5827 */     rowVal[0] = s2b("LONG VARBINARY");
/* 5828 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5831 */     rowVal[2] = s2b("16777215");
/* 5832 */     rowVal[3] = s2b("'");
/* 5833 */     rowVal[4] = s2b("'");
/* 5834 */     rowVal[5] = s2b("");
/* 5835 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5839 */     rowVal[7] = s2b("true");
/* 5840 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5844 */     rowVal[9] = s2b("false");
/* 5845 */     rowVal[10] = s2b("false");
/* 5846 */     rowVal[11] = s2b("false");
/* 5847 */     rowVal[12] = s2b("LONG VARBINARY");
/* 5848 */     rowVal[13] = s2b("0");
/* 5849 */     rowVal[14] = s2b("0");
/* 5850 */     rowVal[15] = s2b("0");
/* 5851 */     rowVal[16] = s2b("0");
/* 5852 */     rowVal[17] = s2b("10");
/* 5853 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5858 */     rowVal = new byte[18][];
/* 5859 */     rowVal[0] = s2b("MEDIUMBLOB");
/* 5860 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5863 */     rowVal[2] = s2b("16777215");
/* 5864 */     rowVal[3] = s2b("'");
/* 5865 */     rowVal[4] = s2b("'");
/* 5866 */     rowVal[5] = s2b("");
/* 5867 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5871 */     rowVal[7] = s2b("true");
/* 5872 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5876 */     rowVal[9] = s2b("false");
/* 5877 */     rowVal[10] = s2b("false");
/* 5878 */     rowVal[11] = s2b("false");
/* 5879 */     rowVal[12] = s2b("MEDIUMBLOB");
/* 5880 */     rowVal[13] = s2b("0");
/* 5881 */     rowVal[14] = s2b("0");
/* 5882 */     rowVal[15] = s2b("0");
/* 5883 */     rowVal[16] = s2b("0");
/* 5884 */     rowVal[17] = s2b("10");
/* 5885 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5890 */     rowVal = new byte[18][];
/* 5891 */     rowVal[0] = s2b("LONGBLOB");
/* 5892 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5895 */     rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
/*      */     
/*      */ 
/* 5898 */     rowVal[3] = s2b("'");
/* 5899 */     rowVal[4] = s2b("'");
/* 5900 */     rowVal[5] = s2b("");
/* 5901 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5905 */     rowVal[7] = s2b("true");
/* 5906 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5910 */     rowVal[9] = s2b("false");
/* 5911 */     rowVal[10] = s2b("false");
/* 5912 */     rowVal[11] = s2b("false");
/* 5913 */     rowVal[12] = s2b("LONGBLOB");
/* 5914 */     rowVal[13] = s2b("0");
/* 5915 */     rowVal[14] = s2b("0");
/* 5916 */     rowVal[15] = s2b("0");
/* 5917 */     rowVal[16] = s2b("0");
/* 5918 */     rowVal[17] = s2b("10");
/* 5919 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5924 */     rowVal = new byte[18][];
/* 5925 */     rowVal[0] = s2b("BLOB");
/* 5926 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5929 */     rowVal[2] = s2b("65535");
/* 5930 */     rowVal[3] = s2b("'");
/* 5931 */     rowVal[4] = s2b("'");
/* 5932 */     rowVal[5] = s2b("");
/* 5933 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5937 */     rowVal[7] = s2b("true");
/* 5938 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5942 */     rowVal[9] = s2b("false");
/* 5943 */     rowVal[10] = s2b("false");
/* 5944 */     rowVal[11] = s2b("false");
/* 5945 */     rowVal[12] = s2b("BLOB");
/* 5946 */     rowVal[13] = s2b("0");
/* 5947 */     rowVal[14] = s2b("0");
/* 5948 */     rowVal[15] = s2b("0");
/* 5949 */     rowVal[16] = s2b("0");
/* 5950 */     rowVal[17] = s2b("10");
/* 5951 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5956 */     rowVal = new byte[18][];
/* 5957 */     rowVal[0] = s2b("TINYBLOB");
/* 5958 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5961 */     rowVal[2] = s2b("255");
/* 5962 */     rowVal[3] = s2b("'");
/* 5963 */     rowVal[4] = s2b("'");
/* 5964 */     rowVal[5] = s2b("");
/* 5965 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5969 */     rowVal[7] = s2b("true");
/* 5970 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5974 */     rowVal[9] = s2b("false");
/* 5975 */     rowVal[10] = s2b("false");
/* 5976 */     rowVal[11] = s2b("false");
/* 5977 */     rowVal[12] = s2b("TINYBLOB");
/* 5978 */     rowVal[13] = s2b("0");
/* 5979 */     rowVal[14] = s2b("0");
/* 5980 */     rowVal[15] = s2b("0");
/* 5981 */     rowVal[16] = s2b("0");
/* 5982 */     rowVal[17] = s2b("10");
/* 5983 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5989 */     rowVal = new byte[18][];
/* 5990 */     rowVal[0] = s2b("VARBINARY");
/* 5991 */     rowVal[1] = Integer.toString(-3).getBytes();
/*      */     
/*      */ 
/* 5994 */     rowVal[2] = s2b("255");
/* 5995 */     rowVal[3] = s2b("'");
/* 5996 */     rowVal[4] = s2b("'");
/* 5997 */     rowVal[5] = s2b("(M)");
/* 5998 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6002 */     rowVal[7] = s2b("true");
/* 6003 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6007 */     rowVal[9] = s2b("false");
/* 6008 */     rowVal[10] = s2b("false");
/* 6009 */     rowVal[11] = s2b("false");
/* 6010 */     rowVal[12] = s2b("VARBINARY");
/* 6011 */     rowVal[13] = s2b("0");
/* 6012 */     rowVal[14] = s2b("0");
/* 6013 */     rowVal[15] = s2b("0");
/* 6014 */     rowVal[16] = s2b("0");
/* 6015 */     rowVal[17] = s2b("10");
/* 6016 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6022 */     rowVal = new byte[18][];
/* 6023 */     rowVal[0] = s2b("BINARY");
/* 6024 */     rowVal[1] = Integer.toString(-2).getBytes();
/*      */     
/*      */ 
/* 6027 */     rowVal[2] = s2b("255");
/* 6028 */     rowVal[3] = s2b("'");
/* 6029 */     rowVal[4] = s2b("'");
/* 6030 */     rowVal[5] = s2b("(M)");
/* 6031 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6035 */     rowVal[7] = s2b("true");
/* 6036 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6040 */     rowVal[9] = s2b("false");
/* 6041 */     rowVal[10] = s2b("false");
/* 6042 */     rowVal[11] = s2b("false");
/* 6043 */     rowVal[12] = s2b("BINARY");
/* 6044 */     rowVal[13] = s2b("0");
/* 6045 */     rowVal[14] = s2b("0");
/* 6046 */     rowVal[15] = s2b("0");
/* 6047 */     rowVal[16] = s2b("0");
/* 6048 */     rowVal[17] = s2b("10");
/* 6049 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6054 */     rowVal = new byte[18][];
/* 6055 */     rowVal[0] = s2b("LONG VARCHAR");
/* 6056 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 6059 */     rowVal[2] = s2b("16777215");
/* 6060 */     rowVal[3] = s2b("'");
/* 6061 */     rowVal[4] = s2b("'");
/* 6062 */     rowVal[5] = s2b("");
/* 6063 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6067 */     rowVal[7] = s2b("false");
/* 6068 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6072 */     rowVal[9] = s2b("false");
/* 6073 */     rowVal[10] = s2b("false");
/* 6074 */     rowVal[11] = s2b("false");
/* 6075 */     rowVal[12] = s2b("LONG VARCHAR");
/* 6076 */     rowVal[13] = s2b("0");
/* 6077 */     rowVal[14] = s2b("0");
/* 6078 */     rowVal[15] = s2b("0");
/* 6079 */     rowVal[16] = s2b("0");
/* 6080 */     rowVal[17] = s2b("10");
/* 6081 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6086 */     rowVal = new byte[18][];
/* 6087 */     rowVal[0] = s2b("MEDIUMTEXT");
/* 6088 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 6091 */     rowVal[2] = s2b("16777215");
/* 6092 */     rowVal[3] = s2b("'");
/* 6093 */     rowVal[4] = s2b("'");
/* 6094 */     rowVal[5] = s2b("");
/* 6095 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6099 */     rowVal[7] = s2b("false");
/* 6100 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6104 */     rowVal[9] = s2b("false");
/* 6105 */     rowVal[10] = s2b("false");
/* 6106 */     rowVal[11] = s2b("false");
/* 6107 */     rowVal[12] = s2b("MEDIUMTEXT");
/* 6108 */     rowVal[13] = s2b("0");
/* 6109 */     rowVal[14] = s2b("0");
/* 6110 */     rowVal[15] = s2b("0");
/* 6111 */     rowVal[16] = s2b("0");
/* 6112 */     rowVal[17] = s2b("10");
/* 6113 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6118 */     rowVal = new byte[18][];
/* 6119 */     rowVal[0] = s2b("LONGTEXT");
/* 6120 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 6123 */     rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
/*      */     
/*      */ 
/* 6126 */     rowVal[3] = s2b("'");
/* 6127 */     rowVal[4] = s2b("'");
/* 6128 */     rowVal[5] = s2b("");
/* 6129 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6133 */     rowVal[7] = s2b("false");
/* 6134 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6138 */     rowVal[9] = s2b("false");
/* 6139 */     rowVal[10] = s2b("false");
/* 6140 */     rowVal[11] = s2b("false");
/* 6141 */     rowVal[12] = s2b("LONGTEXT");
/* 6142 */     rowVal[13] = s2b("0");
/* 6143 */     rowVal[14] = s2b("0");
/* 6144 */     rowVal[15] = s2b("0");
/* 6145 */     rowVal[16] = s2b("0");
/* 6146 */     rowVal[17] = s2b("10");
/* 6147 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6152 */     rowVal = new byte[18][];
/* 6153 */     rowVal[0] = s2b("TEXT");
/* 6154 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 6157 */     rowVal[2] = s2b("65535");
/* 6158 */     rowVal[3] = s2b("'");
/* 6159 */     rowVal[4] = s2b("'");
/* 6160 */     rowVal[5] = s2b("");
/* 6161 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6165 */     rowVal[7] = s2b("false");
/* 6166 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6170 */     rowVal[9] = s2b("false");
/* 6171 */     rowVal[10] = s2b("false");
/* 6172 */     rowVal[11] = s2b("false");
/* 6173 */     rowVal[12] = s2b("TEXT");
/* 6174 */     rowVal[13] = s2b("0");
/* 6175 */     rowVal[14] = s2b("0");
/* 6176 */     rowVal[15] = s2b("0");
/* 6177 */     rowVal[16] = s2b("0");
/* 6178 */     rowVal[17] = s2b("10");
/* 6179 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6184 */     rowVal = new byte[18][];
/* 6185 */     rowVal[0] = s2b("TINYTEXT");
/* 6186 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 6189 */     rowVal[2] = s2b("255");
/* 6190 */     rowVal[3] = s2b("'");
/* 6191 */     rowVal[4] = s2b("'");
/* 6192 */     rowVal[5] = s2b("");
/* 6193 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6197 */     rowVal[7] = s2b("false");
/* 6198 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6202 */     rowVal[9] = s2b("false");
/* 6203 */     rowVal[10] = s2b("false");
/* 6204 */     rowVal[11] = s2b("false");
/* 6205 */     rowVal[12] = s2b("TINYTEXT");
/* 6206 */     rowVal[13] = s2b("0");
/* 6207 */     rowVal[14] = s2b("0");
/* 6208 */     rowVal[15] = s2b("0");
/* 6209 */     rowVal[16] = s2b("0");
/* 6210 */     rowVal[17] = s2b("10");
/* 6211 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6216 */     rowVal = new byte[18][];
/* 6217 */     rowVal[0] = s2b("CHAR");
/* 6218 */     rowVal[1] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/* 6221 */     rowVal[2] = s2b("255");
/* 6222 */     rowVal[3] = s2b("'");
/* 6223 */     rowVal[4] = s2b("'");
/* 6224 */     rowVal[5] = s2b("(M)");
/* 6225 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6229 */     rowVal[7] = s2b("false");
/* 6230 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6234 */     rowVal[9] = s2b("false");
/* 6235 */     rowVal[10] = s2b("false");
/* 6236 */     rowVal[11] = s2b("false");
/* 6237 */     rowVal[12] = s2b("CHAR");
/* 6238 */     rowVal[13] = s2b("0");
/* 6239 */     rowVal[14] = s2b("0");
/* 6240 */     rowVal[15] = s2b("0");
/* 6241 */     rowVal[16] = s2b("0");
/* 6242 */     rowVal[17] = s2b("10");
/* 6243 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/* 6247 */     int decimalPrecision = 254;
/*      */     
/* 6249 */     if (this.conn.versionMeetsMinimum(5, 0, 3)) {
/* 6250 */       if (this.conn.versionMeetsMinimum(5, 0, 6)) {
/* 6251 */         decimalPrecision = 65;
/*      */       } else {
/* 6253 */         decimalPrecision = 64;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6261 */     rowVal = new byte[18][];
/* 6262 */     rowVal[0] = s2b("NUMERIC");
/* 6263 */     rowVal[1] = Integer.toString(2).getBytes();
/*      */     
/*      */ 
/* 6266 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 6267 */     rowVal[3] = s2b("");
/* 6268 */     rowVal[4] = s2b("");
/* 6269 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 6270 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6274 */     rowVal[7] = s2b("false");
/* 6275 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6279 */     rowVal[9] = s2b("false");
/* 6280 */     rowVal[10] = s2b("false");
/* 6281 */     rowVal[11] = s2b("true");
/* 6282 */     rowVal[12] = s2b("NUMERIC");
/* 6283 */     rowVal[13] = s2b("-308");
/* 6284 */     rowVal[14] = s2b("308");
/* 6285 */     rowVal[15] = s2b("0");
/* 6286 */     rowVal[16] = s2b("0");
/* 6287 */     rowVal[17] = s2b("10");
/* 6288 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6293 */     rowVal = new byte[18][];
/* 6294 */     rowVal[0] = s2b("DECIMAL");
/* 6295 */     rowVal[1] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/* 6298 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 6299 */     rowVal[3] = s2b("");
/* 6300 */     rowVal[4] = s2b("");
/* 6301 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 6302 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6306 */     rowVal[7] = s2b("false");
/* 6307 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6311 */     rowVal[9] = s2b("false");
/* 6312 */     rowVal[10] = s2b("false");
/* 6313 */     rowVal[11] = s2b("true");
/* 6314 */     rowVal[12] = s2b("DECIMAL");
/* 6315 */     rowVal[13] = s2b("-308");
/* 6316 */     rowVal[14] = s2b("308");
/* 6317 */     rowVal[15] = s2b("0");
/* 6318 */     rowVal[16] = s2b("0");
/* 6319 */     rowVal[17] = s2b("10");
/* 6320 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6325 */     rowVal = new byte[18][];
/* 6326 */     rowVal[0] = s2b("INTEGER");
/* 6327 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6330 */     rowVal[2] = s2b("10");
/* 6331 */     rowVal[3] = s2b("");
/* 6332 */     rowVal[4] = s2b("");
/* 6333 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6334 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6338 */     rowVal[7] = s2b("false");
/* 6339 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6343 */     rowVal[9] = s2b("true");
/* 6344 */     rowVal[10] = s2b("false");
/* 6345 */     rowVal[11] = s2b("true");
/* 6346 */     rowVal[12] = s2b("INTEGER");
/* 6347 */     rowVal[13] = s2b("0");
/* 6348 */     rowVal[14] = s2b("0");
/* 6349 */     rowVal[15] = s2b("0");
/* 6350 */     rowVal[16] = s2b("0");
/* 6351 */     rowVal[17] = s2b("10");
/* 6352 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6354 */     rowVal = new byte[18][];
/* 6355 */     rowVal[0] = s2b("INTEGER UNSIGNED");
/* 6356 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6359 */     rowVal[2] = s2b("10");
/* 6360 */     rowVal[3] = s2b("");
/* 6361 */     rowVal[4] = s2b("");
/* 6362 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6363 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6367 */     rowVal[7] = s2b("false");
/* 6368 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6372 */     rowVal[9] = s2b("true");
/* 6373 */     rowVal[10] = s2b("false");
/* 6374 */     rowVal[11] = s2b("true");
/* 6375 */     rowVal[12] = s2b("INTEGER UNSIGNED");
/* 6376 */     rowVal[13] = s2b("0");
/* 6377 */     rowVal[14] = s2b("0");
/* 6378 */     rowVal[15] = s2b("0");
/* 6379 */     rowVal[16] = s2b("0");
/* 6380 */     rowVal[17] = s2b("10");
/* 6381 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6386 */     rowVal = new byte[18][];
/* 6387 */     rowVal[0] = s2b("INT");
/* 6388 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6391 */     rowVal[2] = s2b("10");
/* 6392 */     rowVal[3] = s2b("");
/* 6393 */     rowVal[4] = s2b("");
/* 6394 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6395 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6399 */     rowVal[7] = s2b("false");
/* 6400 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6404 */     rowVal[9] = s2b("true");
/* 6405 */     rowVal[10] = s2b("false");
/* 6406 */     rowVal[11] = s2b("true");
/* 6407 */     rowVal[12] = s2b("INT");
/* 6408 */     rowVal[13] = s2b("0");
/* 6409 */     rowVal[14] = s2b("0");
/* 6410 */     rowVal[15] = s2b("0");
/* 6411 */     rowVal[16] = s2b("0");
/* 6412 */     rowVal[17] = s2b("10");
/* 6413 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6415 */     rowVal = new byte[18][];
/* 6416 */     rowVal[0] = s2b("INT UNSIGNED");
/* 6417 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6420 */     rowVal[2] = s2b("10");
/* 6421 */     rowVal[3] = s2b("");
/* 6422 */     rowVal[4] = s2b("");
/* 6423 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6424 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6428 */     rowVal[7] = s2b("false");
/* 6429 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6433 */     rowVal[9] = s2b("true");
/* 6434 */     rowVal[10] = s2b("false");
/* 6435 */     rowVal[11] = s2b("true");
/* 6436 */     rowVal[12] = s2b("INT UNSIGNED");
/* 6437 */     rowVal[13] = s2b("0");
/* 6438 */     rowVal[14] = s2b("0");
/* 6439 */     rowVal[15] = s2b("0");
/* 6440 */     rowVal[16] = s2b("0");
/* 6441 */     rowVal[17] = s2b("10");
/* 6442 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6447 */     rowVal = new byte[18][];
/* 6448 */     rowVal[0] = s2b("MEDIUMINT");
/* 6449 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6452 */     rowVal[2] = s2b("7");
/* 6453 */     rowVal[3] = s2b("");
/* 6454 */     rowVal[4] = s2b("");
/* 6455 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6456 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6460 */     rowVal[7] = s2b("false");
/* 6461 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6465 */     rowVal[9] = s2b("true");
/* 6466 */     rowVal[10] = s2b("false");
/* 6467 */     rowVal[11] = s2b("true");
/* 6468 */     rowVal[12] = s2b("MEDIUMINT");
/* 6469 */     rowVal[13] = s2b("0");
/* 6470 */     rowVal[14] = s2b("0");
/* 6471 */     rowVal[15] = s2b("0");
/* 6472 */     rowVal[16] = s2b("0");
/* 6473 */     rowVal[17] = s2b("10");
/* 6474 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6476 */     rowVal = new byte[18][];
/* 6477 */     rowVal[0] = s2b("MEDIUMINT UNSIGNED");
/* 6478 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 6481 */     rowVal[2] = s2b("8");
/* 6482 */     rowVal[3] = s2b("");
/* 6483 */     rowVal[4] = s2b("");
/* 6484 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6485 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6489 */     rowVal[7] = s2b("false");
/* 6490 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6494 */     rowVal[9] = s2b("true");
/* 6495 */     rowVal[10] = s2b("false");
/* 6496 */     rowVal[11] = s2b("true");
/* 6497 */     rowVal[12] = s2b("MEDIUMINT UNSIGNED");
/* 6498 */     rowVal[13] = s2b("0");
/* 6499 */     rowVal[14] = s2b("0");
/* 6500 */     rowVal[15] = s2b("0");
/* 6501 */     rowVal[16] = s2b("0");
/* 6502 */     rowVal[17] = s2b("10");
/* 6503 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6508 */     rowVal = new byte[18][];
/* 6509 */     rowVal[0] = s2b("SMALLINT");
/* 6510 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */     
/*      */ 
/* 6513 */     rowVal[2] = s2b("5");
/* 6514 */     rowVal[3] = s2b("");
/* 6515 */     rowVal[4] = s2b("");
/* 6516 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 6517 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6521 */     rowVal[7] = s2b("false");
/* 6522 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6526 */     rowVal[9] = s2b("true");
/* 6527 */     rowVal[10] = s2b("false");
/* 6528 */     rowVal[11] = s2b("true");
/* 6529 */     rowVal[12] = s2b("SMALLINT");
/* 6530 */     rowVal[13] = s2b("0");
/* 6531 */     rowVal[14] = s2b("0");
/* 6532 */     rowVal[15] = s2b("0");
/* 6533 */     rowVal[16] = s2b("0");
/* 6534 */     rowVal[17] = s2b("10");
/* 6535 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6537 */     rowVal = new byte[18][];
/* 6538 */     rowVal[0] = s2b("SMALLINT UNSIGNED");
/* 6539 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */     
/*      */ 
/* 6542 */     rowVal[2] = s2b("5");
/* 6543 */     rowVal[3] = s2b("");
/* 6544 */     rowVal[4] = s2b("");
/* 6545 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 6546 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6550 */     rowVal[7] = s2b("false");
/* 6551 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6555 */     rowVal[9] = s2b("true");
/* 6556 */     rowVal[10] = s2b("false");
/* 6557 */     rowVal[11] = s2b("true");
/* 6558 */     rowVal[12] = s2b("SMALLINT UNSIGNED");
/* 6559 */     rowVal[13] = s2b("0");
/* 6560 */     rowVal[14] = s2b("0");
/* 6561 */     rowVal[15] = s2b("0");
/* 6562 */     rowVal[16] = s2b("0");
/* 6563 */     rowVal[17] = s2b("10");
/* 6564 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6570 */     rowVal = new byte[18][];
/* 6571 */     rowVal[0] = s2b("FLOAT");
/* 6572 */     rowVal[1] = Integer.toString(7).getBytes();
/*      */     
/*      */ 
/* 6575 */     rowVal[2] = s2b("10");
/* 6576 */     rowVal[3] = s2b("");
/* 6577 */     rowVal[4] = s2b("");
/* 6578 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6579 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6583 */     rowVal[7] = s2b("false");
/* 6584 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6588 */     rowVal[9] = s2b("false");
/* 6589 */     rowVal[10] = s2b("false");
/* 6590 */     rowVal[11] = s2b("true");
/* 6591 */     rowVal[12] = s2b("FLOAT");
/* 6592 */     rowVal[13] = s2b("-38");
/* 6593 */     rowVal[14] = s2b("38");
/* 6594 */     rowVal[15] = s2b("0");
/* 6595 */     rowVal[16] = s2b("0");
/* 6596 */     rowVal[17] = s2b("10");
/* 6597 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6602 */     rowVal = new byte[18][];
/* 6603 */     rowVal[0] = s2b("DOUBLE");
/* 6604 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6607 */     rowVal[2] = s2b("17");
/* 6608 */     rowVal[3] = s2b("");
/* 6609 */     rowVal[4] = s2b("");
/* 6610 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6611 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6615 */     rowVal[7] = s2b("false");
/* 6616 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6620 */     rowVal[9] = s2b("false");
/* 6621 */     rowVal[10] = s2b("false");
/* 6622 */     rowVal[11] = s2b("true");
/* 6623 */     rowVal[12] = s2b("DOUBLE");
/* 6624 */     rowVal[13] = s2b("-308");
/* 6625 */     rowVal[14] = s2b("308");
/* 6626 */     rowVal[15] = s2b("0");
/* 6627 */     rowVal[16] = s2b("0");
/* 6628 */     rowVal[17] = s2b("10");
/* 6629 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6634 */     rowVal = new byte[18][];
/* 6635 */     rowVal[0] = s2b("DOUBLE PRECISION");
/* 6636 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6639 */     rowVal[2] = s2b("17");
/* 6640 */     rowVal[3] = s2b("");
/* 6641 */     rowVal[4] = s2b("");
/* 6642 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6643 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6647 */     rowVal[7] = s2b("false");
/* 6648 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6652 */     rowVal[9] = s2b("false");
/* 6653 */     rowVal[10] = s2b("false");
/* 6654 */     rowVal[11] = s2b("true");
/* 6655 */     rowVal[12] = s2b("DOUBLE PRECISION");
/* 6656 */     rowVal[13] = s2b("-308");
/* 6657 */     rowVal[14] = s2b("308");
/* 6658 */     rowVal[15] = s2b("0");
/* 6659 */     rowVal[16] = s2b("0");
/* 6660 */     rowVal[17] = s2b("10");
/* 6661 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6666 */     rowVal = new byte[18][];
/* 6667 */     rowVal[0] = s2b("REAL");
/* 6668 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6671 */     rowVal[2] = s2b("17");
/* 6672 */     rowVal[3] = s2b("");
/* 6673 */     rowVal[4] = s2b("");
/* 6674 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6675 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6679 */     rowVal[7] = s2b("false");
/* 6680 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6684 */     rowVal[9] = s2b("false");
/* 6685 */     rowVal[10] = s2b("false");
/* 6686 */     rowVal[11] = s2b("true");
/* 6687 */     rowVal[12] = s2b("REAL");
/* 6688 */     rowVal[13] = s2b("-308");
/* 6689 */     rowVal[14] = s2b("308");
/* 6690 */     rowVal[15] = s2b("0");
/* 6691 */     rowVal[16] = s2b("0");
/* 6692 */     rowVal[17] = s2b("10");
/* 6693 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6698 */     rowVal = new byte[18][];
/* 6699 */     rowVal[0] = s2b("VARCHAR");
/* 6700 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6703 */     rowVal[2] = s2b("255");
/* 6704 */     rowVal[3] = s2b("'");
/* 6705 */     rowVal[4] = s2b("'");
/* 6706 */     rowVal[5] = s2b("(M)");
/* 6707 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6711 */     rowVal[7] = s2b("false");
/* 6712 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6716 */     rowVal[9] = s2b("false");
/* 6717 */     rowVal[10] = s2b("false");
/* 6718 */     rowVal[11] = s2b("false");
/* 6719 */     rowVal[12] = s2b("VARCHAR");
/* 6720 */     rowVal[13] = s2b("0");
/* 6721 */     rowVal[14] = s2b("0");
/* 6722 */     rowVal[15] = s2b("0");
/* 6723 */     rowVal[16] = s2b("0");
/* 6724 */     rowVal[17] = s2b("10");
/* 6725 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6730 */     rowVal = new byte[18][];
/* 6731 */     rowVal[0] = s2b("ENUM");
/* 6732 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6735 */     rowVal[2] = s2b("65535");
/* 6736 */     rowVal[3] = s2b("'");
/* 6737 */     rowVal[4] = s2b("'");
/* 6738 */     rowVal[5] = s2b("");
/* 6739 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6743 */     rowVal[7] = s2b("false");
/* 6744 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6748 */     rowVal[9] = s2b("false");
/* 6749 */     rowVal[10] = s2b("false");
/* 6750 */     rowVal[11] = s2b("false");
/* 6751 */     rowVal[12] = s2b("ENUM");
/* 6752 */     rowVal[13] = s2b("0");
/* 6753 */     rowVal[14] = s2b("0");
/* 6754 */     rowVal[15] = s2b("0");
/* 6755 */     rowVal[16] = s2b("0");
/* 6756 */     rowVal[17] = s2b("10");
/* 6757 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6762 */     rowVal = new byte[18][];
/* 6763 */     rowVal[0] = s2b("SET");
/* 6764 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6767 */     rowVal[2] = s2b("64");
/* 6768 */     rowVal[3] = s2b("'");
/* 6769 */     rowVal[4] = s2b("'");
/* 6770 */     rowVal[5] = s2b("");
/* 6771 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6775 */     rowVal[7] = s2b("false");
/* 6776 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6780 */     rowVal[9] = s2b("false");
/* 6781 */     rowVal[10] = s2b("false");
/* 6782 */     rowVal[11] = s2b("false");
/* 6783 */     rowVal[12] = s2b("SET");
/* 6784 */     rowVal[13] = s2b("0");
/* 6785 */     rowVal[14] = s2b("0");
/* 6786 */     rowVal[15] = s2b("0");
/* 6787 */     rowVal[16] = s2b("0");
/* 6788 */     rowVal[17] = s2b("10");
/* 6789 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6794 */     rowVal = new byte[18][];
/* 6795 */     rowVal[0] = s2b("DATE");
/* 6796 */     rowVal[1] = Integer.toString(91).getBytes();
/*      */     
/*      */ 
/* 6799 */     rowVal[2] = s2b("0");
/* 6800 */     rowVal[3] = s2b("'");
/* 6801 */     rowVal[4] = s2b("'");
/* 6802 */     rowVal[5] = s2b("");
/* 6803 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6807 */     rowVal[7] = s2b("false");
/* 6808 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6812 */     rowVal[9] = s2b("false");
/* 6813 */     rowVal[10] = s2b("false");
/* 6814 */     rowVal[11] = s2b("false");
/* 6815 */     rowVal[12] = s2b("DATE");
/* 6816 */     rowVal[13] = s2b("0");
/* 6817 */     rowVal[14] = s2b("0");
/* 6818 */     rowVal[15] = s2b("0");
/* 6819 */     rowVal[16] = s2b("0");
/* 6820 */     rowVal[17] = s2b("10");
/* 6821 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6826 */     rowVal = new byte[18][];
/* 6827 */     rowVal[0] = s2b("TIME");
/* 6828 */     rowVal[1] = Integer.toString(92).getBytes();
/*      */     
/*      */ 
/* 6831 */     rowVal[2] = s2b("0");
/* 6832 */     rowVal[3] = s2b("'");
/* 6833 */     rowVal[4] = s2b("'");
/* 6834 */     rowVal[5] = s2b("");
/* 6835 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6839 */     rowVal[7] = s2b("false");
/* 6840 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6844 */     rowVal[9] = s2b("false");
/* 6845 */     rowVal[10] = s2b("false");
/* 6846 */     rowVal[11] = s2b("false");
/* 6847 */     rowVal[12] = s2b("TIME");
/* 6848 */     rowVal[13] = s2b("0");
/* 6849 */     rowVal[14] = s2b("0");
/* 6850 */     rowVal[15] = s2b("0");
/* 6851 */     rowVal[16] = s2b("0");
/* 6852 */     rowVal[17] = s2b("10");
/* 6853 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6858 */     rowVal = new byte[18][];
/* 6859 */     rowVal[0] = s2b("DATETIME");
/* 6860 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */     
/*      */ 
/* 6863 */     rowVal[2] = s2b("0");
/* 6864 */     rowVal[3] = s2b("'");
/* 6865 */     rowVal[4] = s2b("'");
/* 6866 */     rowVal[5] = s2b("");
/* 6867 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6871 */     rowVal[7] = s2b("false");
/* 6872 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6876 */     rowVal[9] = s2b("false");
/* 6877 */     rowVal[10] = s2b("false");
/* 6878 */     rowVal[11] = s2b("false");
/* 6879 */     rowVal[12] = s2b("DATETIME");
/* 6880 */     rowVal[13] = s2b("0");
/* 6881 */     rowVal[14] = s2b("0");
/* 6882 */     rowVal[15] = s2b("0");
/* 6883 */     rowVal[16] = s2b("0");
/* 6884 */     rowVal[17] = s2b("10");
/* 6885 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6890 */     rowVal = new byte[18][];
/* 6891 */     rowVal[0] = s2b("TIMESTAMP");
/* 6892 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */     
/*      */ 
/* 6895 */     rowVal[2] = s2b("0");
/* 6896 */     rowVal[3] = s2b("'");
/* 6897 */     rowVal[4] = s2b("'");
/* 6898 */     rowVal[5] = s2b("[(M)]");
/* 6899 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6903 */     rowVal[7] = s2b("false");
/* 6904 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6908 */     rowVal[9] = s2b("false");
/* 6909 */     rowVal[10] = s2b("false");
/* 6910 */     rowVal[11] = s2b("false");
/* 6911 */     rowVal[12] = s2b("TIMESTAMP");
/* 6912 */     rowVal[13] = s2b("0");
/* 6913 */     rowVal[14] = s2b("0");
/* 6914 */     rowVal[15] = s2b("0");
/* 6915 */     rowVal[16] = s2b("0");
/* 6916 */     rowVal[17] = s2b("10");
/* 6917 */     tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
/*      */     
/* 6919 */     return buildResultSet(fields, tuples);
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
/*      */   public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
/*      */     throws SQLException
/*      */   {
/* 6965 */     Field[] fields = new Field[7];
/* 6966 */     fields[0] = new Field("", "TYPE_CAT", 12, 32);
/* 6967 */     fields[1] = new Field("", "TYPE_SCHEM", 12, 32);
/* 6968 */     fields[2] = new Field("", "TYPE_NAME", 12, 32);
/* 6969 */     fields[3] = new Field("", "CLASS_NAME", 12, 32);
/* 6970 */     fields[4] = new Field("", "DATA_TYPE", 4, 10);
/* 6971 */     fields[5] = new Field("", "REMARKS", 12, 32);
/* 6972 */     fields[6] = new Field("", "BASE_TYPE", 5, 10);
/*      */     
/* 6974 */     ArrayList<ResultSetRow> tuples = new ArrayList();
/*      */     
/* 6976 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getURL()
/*      */     throws SQLException
/*      */   {
/* 6987 */     return this.conn.getURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserName()
/*      */     throws SQLException
/*      */   {
/* 6998 */     if (this.conn.getUseHostsInPrivileges()) {
/* 6999 */       java.sql.Statement stmt = null;
/* 7000 */       ResultSet rs = null;
/*      */       try
/*      */       {
/* 7003 */         stmt = this.conn.createStatement();
/* 7004 */         stmt.setEscapeProcessing(false);
/*      */         
/* 7006 */         rs = stmt.executeQuery("SELECT USER()");
/* 7007 */         rs.next();
/*      */         
/* 7009 */         return rs.getString(1);
/*      */       } finally {
/* 7011 */         if (rs != null) {
/*      */           try {
/* 7013 */             rs.close();
/*      */           } catch (Exception ex) {
/* 7015 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           }
/*      */           
/* 7018 */           rs = null;
/*      */         }
/*      */         
/* 7021 */         if (stmt != null) {
/*      */           try {
/* 7023 */             stmt.close();
/*      */           } catch (Exception ex) {
/* 7025 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           }
/*      */           
/* 7028 */           stmt = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 7033 */     return this.conn.getUser();
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
/*      */   public ResultSet getVersionColumns(String catalog, String schema, final String table)
/*      */     throws SQLException
/*      */   {
/* 7073 */     if (table == null) {
/* 7074 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 7078 */     Field[] fields = new Field[8];
/* 7079 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 7080 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 7081 */     fields[2] = new Field("", "DATA_TYPE", 4, 5);
/* 7082 */     fields[3] = new Field("", "TYPE_NAME", 1, 16);
/* 7083 */     fields[4] = new Field("", "COLUMN_SIZE", 4, 16);
/* 7084 */     fields[5] = new Field("", "BUFFER_LENGTH", 4, 16);
/* 7085 */     fields[6] = new Field("", "DECIMAL_DIGITS", 5, 16);
/* 7086 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 7088 */     final ArrayList<ResultSetRow> rows = new ArrayList();
/*      */     
/* 7090 */     final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 7094 */       new IterateBlock(getCatalogIterator(catalog))
/*      */       {
/*      */         void forEach(String catalogStr) throws SQLException {
/* 7097 */           ResultSet results = null;
/* 7098 */           boolean with_where = DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 0);
/*      */           try
/*      */           {
/* 7101 */             StringBuffer whereBuf = new StringBuffer(" Extra LIKE '%on update CURRENT_TIMESTAMP%'");
/* 7102 */             List<String> rsFields = new ArrayList();
/*      */             
/*      */ 
/*      */ 
/* 7106 */             if (!DatabaseMetaData.this.conn.versionMeetsMinimum(5, 1, 23))
/*      */             {
/* 7108 */               whereBuf = new StringBuffer();
/* 7109 */               boolean firstTime = true;
/*      */               
/* 7111 */               String query = "SHOW CREATE TABLE " + StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()) + "." + StringUtils.quoteIdentifier(table, DatabaseMetaData.this.conn.getPedantic());
/*      */               
/*      */ 
/*      */ 
/* 7115 */               results = stmt.executeQuery(query);
/* 7116 */               while (results.next()) {
/* 7117 */                 String createTableString = results.getString(2);
/* 7118 */                 StringTokenizer lineTokenizer = new StringTokenizer(createTableString, "\n");
/*      */                 
/* 7120 */                 while (lineTokenizer.hasMoreTokens()) {
/* 7121 */                   String line = lineTokenizer.nextToken().trim();
/* 7122 */                   if (StringUtils.indexOfIgnoreCase(line, "on update CURRENT_TIMESTAMP") > -1) {
/* 7123 */                     boolean usingBackTicks = true;
/* 7124 */                     int beginPos = line.indexOf(DatabaseMetaData.this.quotedId);
/*      */                     
/* 7126 */                     if (beginPos == -1) {
/* 7127 */                       beginPos = line.indexOf("\"");
/* 7128 */                       usingBackTicks = false;
/*      */                     }
/*      */                     
/* 7131 */                     if (beginPos != -1) {
/* 7132 */                       int endPos = -1;
/*      */                       
/* 7134 */                       if (usingBackTicks) {
/* 7135 */                         endPos = line.indexOf(DatabaseMetaData.this.quotedId, beginPos + 1);
/*      */                       } else {
/* 7137 */                         endPos = line.indexOf("\"", beginPos + 1);
/*      */                       }
/*      */                       
/* 7140 */                       if (endPos != -1) {
/* 7141 */                         if (with_where) {
/* 7142 */                           if (!firstTime) {
/* 7143 */                             whereBuf.append(" or");
/*      */                           } else {
/* 7145 */                             firstTime = false;
/*      */                           }
/* 7147 */                           whereBuf.append(" Field='");
/* 7148 */                           whereBuf.append(line.substring(beginPos + 1, endPos));
/* 7149 */                           whereBuf.append("'");
/*      */                         } else {
/* 7151 */                           rsFields.add(line.substring(beginPos + 1, endPos));
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 7160 */             if ((whereBuf.length() > 0) || (rsFields.size() > 0)) {
/* 7161 */               StringBuffer queryBuf = new StringBuffer("SHOW ");
/* 7162 */               queryBuf.append("COLUMNS FROM ");
/* 7163 */               queryBuf.append(StringUtils.quoteIdentifier(table, DatabaseMetaData.this.conn.getPedantic()));
/* 7164 */               queryBuf.append(" FROM ");
/* 7165 */               queryBuf.append(StringUtils.quoteIdentifier(catalogStr, DatabaseMetaData.this.conn.getPedantic()));
/* 7166 */               if (with_where) {
/* 7167 */                 queryBuf.append(" WHERE");
/* 7168 */                 queryBuf.append(whereBuf.toString());
/*      */               }
/*      */               
/* 7171 */               results = stmt.executeQuery(queryBuf.toString());
/*      */               
/* 7173 */               while (results.next()) {
/* 7174 */                 if ((with_where) || (rsFields.contains(results.getString("Field")))) {
/* 7175 */                   DatabaseMetaData.TypeDescriptor typeDesc = new DatabaseMetaData.TypeDescriptor(DatabaseMetaData.this, results.getString("Type"), results.getString("Null"));
/* 7176 */                   byte[][] rowVal = new byte[8][];
/*      */                   
/* 7178 */                   rowVal[0] = null;
/*      */                   
/* 7180 */                   rowVal[1] = results.getBytes("Field");
/*      */                   
/* 7182 */                   rowVal[2] = Short.toString(typeDesc.dataType).getBytes();
/*      */                   
/* 7184 */                   rowVal[3] = DatabaseMetaData.this.s2b(typeDesc.typeName);
/*      */                   
/* 7186 */                   rowVal[4] = (typeDesc.columnSize == null ? null : DatabaseMetaData.this.s2b(typeDesc.columnSize.toString()));
/*      */                   
/* 7188 */                   rowVal[5] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.bufferLength));
/*      */                   
/* 7190 */                   rowVal[6] = (typeDesc.decimalDigits == null ? null : DatabaseMetaData.this.s2b(typeDesc.decimalDigits.toString()));
/*      */                   
/* 7192 */                   rowVal[7] = Integer.toString(1).getBytes();
/*      */                   
/* 7194 */                   rows.add(new ByteArrayRow(rowVal, DatabaseMetaData.this.getExceptionInterceptor()));
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (SQLException sqlEx) {
/* 7199 */             if (!"42S02".equals(sqlEx.getSQLState())) {
/* 7200 */               throw sqlEx;
/*      */             }
/*      */           } finally {
/* 7203 */             if (results != null) {
/*      */               try {
/* 7205 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 7210 */               results = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     }
/*      */     finally {
/* 7217 */       if (stmt != null) {
/* 7218 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 7222 */     return buildResultSet(fields, rows);
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
/*      */   public boolean insertsAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/* 7236 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCatalogAtStart()
/*      */     throws SQLException
/*      */   {
/* 7248 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/* 7259 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean locatorsUpdateCopy()
/*      */     throws SQLException
/*      */   {
/* 7266 */     return !this.conn.getEmulateLocators();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullPlusNonNullIsNull()
/*      */     throws SQLException
/*      */   {
/* 7278 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedAtEnd()
/*      */     throws SQLException
/*      */   {
/* 7289 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedAtStart()
/*      */     throws SQLException
/*      */   {
/* 7300 */     return (this.conn.versionMeetsMinimum(4, 0, 2)) && (!this.conn.versionMeetsMinimum(4, 0, 11));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedHigh()
/*      */     throws SQLException
/*      */   {
/* 7312 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedLow()
/*      */     throws SQLException
/*      */   {
/* 7323 */     return !nullsAreSortedHigh();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersDeletesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7336 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersInsertsAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7349 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersUpdatesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7362 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownDeletesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7375 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownInsertsAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7388 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownUpdatesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 7401 */     return false;
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
/*      */   protected LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String keysComment)
/*      */     throws SQLException
/*      */   {
/* 7422 */     String columnsDelimitter = ",";
/*      */     
/* 7424 */     char quoteChar = this.quotedId.length() == 0 ? '\000' : this.quotedId.charAt(0);
/*      */     
/*      */ 
/* 7427 */     int indexOfOpenParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysComment, "(", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 7431 */     if (indexOfOpenParenLocalColumns == -1) {
/* 7432 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7437 */     String constraintName = StringUtils.unQuoteIdentifier(keysComment.substring(0, indexOfOpenParenLocalColumns).trim(), this.conn.useAnsiQuotedIdentifiers());
/*      */     
/*      */ 
/* 7440 */     keysComment = keysComment.substring(indexOfOpenParenLocalColumns, keysComment.length());
/*      */     
/*      */ 
/* 7443 */     String keysCommentTrimmed = keysComment.trim();
/*      */     
/* 7445 */     int indexOfCloseParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, ")", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 7449 */     if (indexOfCloseParenLocalColumns == -1) {
/* 7450 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7455 */     String localColumnNamesString = keysCommentTrimmed.substring(1, indexOfCloseParenLocalColumns);
/*      */     
/*      */ 
/* 7458 */     int indexOfRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, "REFER ", this.quotedId.charAt(0), true);
/*      */     
/*      */ 
/* 7461 */     if (indexOfRefer == -1) {
/* 7462 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7467 */     int indexOfOpenParenReferCol = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfRefer, keysCommentTrimmed, "(", quoteChar, false);
/*      */     
/*      */ 
/*      */ 
/* 7471 */     if (indexOfOpenParenReferCol == -1) {
/* 7472 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7477 */     String referCatalogTableString = keysCommentTrimmed.substring(indexOfRefer + "REFER ".length(), indexOfOpenParenReferCol);
/*      */     
/*      */ 
/* 7480 */     int indexOfSlash = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referCatalogTableString, "/", this.quotedId.charAt(0), false);
/*      */     
/*      */ 
/* 7483 */     if (indexOfSlash == -1) {
/* 7484 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7489 */     String referCatalog = StringUtils.unQuoteIdentifier(referCatalogTableString.substring(0, indexOfSlash), this.conn.useAnsiQuotedIdentifiers());
/*      */     
/*      */ 
/* 7492 */     String referTable = StringUtils.unQuoteIdentifier(referCatalogTableString.substring(indexOfSlash + 1).trim(), this.conn.useAnsiQuotedIdentifiers());
/*      */     
/*      */ 
/*      */ 
/* 7496 */     int indexOfCloseParenRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfOpenParenReferCol, keysCommentTrimmed, ")", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 7500 */     if (indexOfCloseParenRefer == -1) {
/* 7501 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7506 */     String referColumnNamesString = keysCommentTrimmed.substring(indexOfOpenParenReferCol + 1, indexOfCloseParenRefer);
/*      */     
/*      */ 
/* 7509 */     List<String> referColumnsList = StringUtils.split(referColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */     
/* 7511 */     List<String> localColumnsList = StringUtils.split(localColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */     
/*      */ 
/* 7514 */     return new LocalAndReferencedColumns(localColumnsList, referColumnsList, constraintName, referCatalog, referTable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] s2b(String s)
/*      */     throws SQLException
/*      */   {
/* 7527 */     if (s == null) {
/* 7528 */       return null;
/*      */     }
/*      */     
/* 7531 */     return StringUtils.getBytes(s, this.conn.getCharacterSetMetadata(), this.conn.getServerCharacterEncoding(), this.conn.parserKnowsUnicode(), this.conn, getExceptionInterceptor());
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
/*      */   public boolean storesLowerCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7545 */     return this.conn.storesLowerCaseTableName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesLowerCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7557 */     return this.conn.storesLowerCaseTableName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7569 */     return !this.conn.storesLowerCaseTableName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7580 */     return !this.conn.storesLowerCaseTableName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesUpperCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7592 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesUpperCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7604 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsAlterTableWithAddColumn()
/*      */     throws SQLException
/*      */   {
/* 7615 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsAlterTableWithDropColumn()
/*      */     throws SQLException
/*      */   {
/* 7626 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92EntryLevelSQL()
/*      */     throws SQLException
/*      */   {
/* 7638 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92FullSQL()
/*      */     throws SQLException
/*      */   {
/* 7649 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92IntermediateSQL()
/*      */     throws SQLException
/*      */   {
/* 7660 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsBatchUpdates()
/*      */     throws SQLException
/*      */   {
/* 7672 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 7684 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7696 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7708 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 7720 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7732 */     return this.conn.versionMeetsMinimum(3, 22, 0);
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
/*      */   public boolean supportsColumnAliasing()
/*      */     throws SQLException
/*      */   {
/* 7748 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsConvert()
/*      */     throws SQLException
/*      */   {
/* 7759 */     return false;
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
/*      */   public boolean supportsConvert(int fromType, int toType)
/*      */     throws SQLException
/*      */   {
/* 7776 */     switch (fromType)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/*      */     case -1: 
/*      */     case 1: 
/*      */     case 12: 
/* 7787 */       switch (toType) {
/*      */       case -6: 
/*      */       case -5: 
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 3: 
/*      */       case 4: 
/*      */       case 5: 
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/*      */       case 12: 
/*      */       case 91: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 1111: 
/* 7807 */         return true;
/*      */       }
/*      */       
/* 7810 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case -7: 
/* 7817 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case -6: 
/*      */     case -5: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/* 7833 */       switch (toType) {
/*      */       case -6: 
/*      */       case -5: 
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 3: 
/*      */       case 4: 
/*      */       case 5: 
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/*      */       case 12: 
/* 7849 */         return true;
/*      */       }
/*      */       
/* 7852 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */     case 0: 
/* 7857 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1111: 
/* 7865 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7872 */         return true;
/*      */       }
/*      */       
/* 7875 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 91: 
/* 7881 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7888 */         return true;
/*      */       }
/*      */       
/* 7891 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 92: 
/* 7897 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7904 */         return true;
/*      */       }
/*      */       
/* 7907 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 93: 
/* 7916 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/*      */       case 91: 
/*      */       case 92: 
/* 7925 */         return true;
/*      */       }
/*      */       
/* 7928 */       return false;
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 7933 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCoreSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 7945 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCorrelatedSubqueries()
/*      */     throws SQLException
/*      */   {
/* 7957 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions()
/*      */     throws SQLException
/*      */   {
/* 7970 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDataManipulationTransactionsOnly()
/*      */     throws SQLException
/*      */   {
/* 7982 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDifferentTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/* 7995 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsExpressionsInOrderBy()
/*      */     throws SQLException
/*      */   {
/* 8006 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsExtendedSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 8017 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsFullOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 8028 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGetGeneratedKeys()
/*      */   {
/* 8037 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupBy()
/*      */     throws SQLException
/*      */   {
/* 8048 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupByBeyondSelect()
/*      */     throws SQLException
/*      */   {
/* 8060 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupByUnrelated()
/*      */     throws SQLException
/*      */   {
/* 8071 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsIntegrityEnhancementFacility()
/*      */     throws SQLException
/*      */   {
/* 8082 */     if (!this.conn.getOverrideSupportsIntegrityEnhancementFacility()) {
/* 8083 */       return false;
/*      */     }
/*      */     
/* 8086 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsLikeEscapeClause()
/*      */     throws SQLException
/*      */   {
/* 8098 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsLimitedOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 8110 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMinimumSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 8122 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 8133 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 8145 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsMultipleOpenResults()
/*      */     throws SQLException
/*      */   {
/* 8152 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMultipleResultSets()
/*      */     throws SQLException
/*      */   {
/* 8163 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMultipleTransactions()
/*      */     throws SQLException
/*      */   {
/* 8175 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsNamedParameters()
/*      */     throws SQLException
/*      */   {
/* 8182 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsNonNullableColumns()
/*      */     throws SQLException
/*      */   {
/* 8194 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 8206 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 8218 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 8230 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 8242 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOrderByUnrelated()
/*      */     throws SQLException
/*      */   {
/* 8253 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 8264 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsPositionedDelete()
/*      */     throws SQLException
/*      */   {
/* 8275 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsPositionedUpdate()
/*      */     throws SQLException
/*      */   {
/* 8286 */     return false;
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
/*      */   public boolean supportsResultSetConcurrency(int type, int concurrency)
/*      */     throws SQLException
/*      */   {
/* 8304 */     switch (type) {
/*      */     case 1004: 
/* 8306 */       if ((concurrency == 1007) || (concurrency == 1008))
/*      */       {
/* 8308 */         return true;
/*      */       }
/* 8310 */       throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
/*      */     
/*      */ 
/*      */ 
/*      */     case 1003: 
/* 8315 */       if ((concurrency == 1007) || (concurrency == 1008))
/*      */       {
/* 8317 */         return true;
/*      */       }
/* 8319 */       throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
/*      */     
/*      */ 
/*      */ 
/*      */     case 1005: 
/* 8324 */       return false;
/*      */     }
/* 8326 */     throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsResultSetHoldability(int holdability)
/*      */     throws SQLException
/*      */   {
/* 8338 */     return holdability == 1;
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
/*      */   public boolean supportsResultSetType(int type)
/*      */     throws SQLException
/*      */   {
/* 8352 */     return type == 1004;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean supportsSavepoints()
/*      */     throws SQLException
/*      */   {
/* 8360 */     return (this.conn.versionMeetsMinimum(4, 0, 14)) || (this.conn.versionMeetsMinimum(4, 1, 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 8372 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 8383 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 8394 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 8405 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 8416 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSelectForUpdate()
/*      */     throws SQLException
/*      */   {
/* 8427 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsStatementPooling()
/*      */     throws SQLException
/*      */   {
/* 8434 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsStoredProcedures()
/*      */     throws SQLException
/*      */   {
/* 8446 */     return this.conn.versionMeetsMinimum(5, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInComparisons()
/*      */     throws SQLException
/*      */   {
/* 8458 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInExists()
/*      */     throws SQLException
/*      */   {
/* 8470 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInIns()
/*      */     throws SQLException
/*      */   {
/* 8482 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInQuantifieds()
/*      */     throws SQLException
/*      */   {
/* 8494 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/* 8506 */     return true;
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
/*      */   public boolean supportsTransactionIsolationLevel(int level)
/*      */     throws SQLException
/*      */   {
/* 8521 */     if (this.conn.supportsIsolationLevel()) {
/* 8522 */       switch (level) {
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 4: 
/*      */       case 8: 
/* 8527 */         return true;
/*      */       }
/*      */       
/* 8530 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 8534 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTransactions()
/*      */     throws SQLException
/*      */   {
/* 8546 */     return this.conn.supportsTransactions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsUnion()
/*      */     throws SQLException
/*      */   {
/* 8557 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsUnionAll()
/*      */     throws SQLException
/*      */   {
/* 8568 */     return this.conn.versionMeetsMinimum(4, 0, 0);
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
/*      */   public boolean updatesAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/* 8582 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean usesLocalFilePerTable()
/*      */     throws SQLException
/*      */   {
/* 8593 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean usesLocalFiles()
/*      */     throws SQLException
/*      */   {
/* 8604 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getClientInfoProperties()
/*      */     throws SQLException
/*      */   {
/* 8637 */     Field[] fields = new Field[4];
/* 8638 */     fields[0] = new Field("", "NAME", 12, 255);
/* 8639 */     fields[1] = new Field("", "MAX_LEN", 4, 10);
/* 8640 */     fields[2] = new Field("", "DEFAULT_VALUE", 12, 255);
/* 8641 */     fields[3] = new Field("", "DESCRIPTION", 12, 255);
/*      */     
/* 8643 */     return buildResultSet(fields, new ArrayList(), this.conn);
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
/*      */   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 8657 */     Field[] fields = createFunctionColumnsFields();
/*      */     
/* 8659 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, functionNamePattern, columnNamePattern, false, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Field[] createFunctionColumnsFields()
/*      */   {
/* 8666 */     Field[] fields = { new Field("", "FUNCTION_CAT", 12, 512), new Field("", "FUNCTION_SCHEM", 12, 512), new Field("", "FUNCTION_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "COLUMN_TYPE", 12, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 12, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 32), new Field("", "ORDINAL_POSITION", 4, 32), new Field("", "IS_NULLABLE", 12, 12), new Field("", "SPECIFIC_NAME", 12, 64) };
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
/* 8684 */     return fields;
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
/*      */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
/*      */     throws SQLException
/*      */   {
/* 8737 */     Field[] fields = new Field[6];
/*      */     
/* 8739 */     fields[0] = new Field("", "FUNCTION_CAT", 1, 255);
/* 8740 */     fields[1] = new Field("", "FUNCTION_SCHEM", 1, 255);
/* 8741 */     fields[2] = new Field("", "FUNCTION_NAME", 1, 255);
/* 8742 */     fields[3] = new Field("", "REMARKS", 1, 255);
/* 8743 */     fields[4] = new Field("", "FUNCTION_TYPE", 5, 6);
/* 8744 */     fields[5] = new Field("", "SPECIFIC_NAME", 1, 255);
/*      */     
/* 8746 */     return getProceduresAndOrFunctions(fields, catalog, schemaPattern, functionNamePattern, false, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean providesQueryObjectGenerator()
/*      */     throws SQLException
/*      */   {
/* 8756 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getSchemas(String catalog, String schemaPattern)
/*      */     throws SQLException
/*      */   {
/* 8768 */     Field[] fields = { new Field("", "TABLE_SCHEM", 12, 255), new Field("", "TABLE_CATALOG", 12, 255) };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 8773 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 8777 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PreparedStatement prepareMetaDataSafeStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 8790 */     PreparedStatement pStmt = this.conn.clientPrepareStatement(sql);
/*      */     
/* 8792 */     if (pStmt.getMaxRows() != 0) {
/* 8793 */       pStmt.setMaxRows(0);
/*      */     }
/*      */     
/* 8796 */     ((Statement)pStmt).setHoldResultsOpenOverClose(true);
/*      */     
/* 8798 */     return pStmt;
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
/*      */   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 8814 */     Field[] fields = { new Field("", "TABLE_CAT", 12, 512), new Field("", "TABLE_SCHEM", 12, 512), new Field("", "TABLE_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "DATA_TYPE", 4, 12), new Field("", "COLUMN_SIZE", 4, 12), new Field("", "DECIMAL_DIGITS", 4, 12), new Field("", "NUM_PREC_RADIX", 4, 12), new Field("", "COLUMN_USAGE", 12, 512), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 12), new Field("", "IS_NULLABLE", 12, 512) };
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
/* 8827 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */   public boolean generatedKeyAlwaysReturned()
/*      */     throws SQLException
/*      */   {
/* 8833 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\DatabaseMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */