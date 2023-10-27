/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DatabaseMetaDataUsingInfoSchema
/*      */   extends DatabaseMetaData
/*      */ {
/*      */   private boolean hasReferentialConstraintsView;
/*      */   private final boolean hasParametersView;
/*      */   
/*      */   protected static enum JDBC4FunctionConstant
/*      */   {
/*   41 */     FUNCTION_COLUMN_UNKNOWN,  FUNCTION_COLUMN_IN,  FUNCTION_COLUMN_INOUT,  FUNCTION_COLUMN_OUT, 
/*   42 */     FUNCTION_COLUMN_RETURN,  FUNCTION_COLUMN_RESULT, 
/*      */     
/*   44 */     FUNCTION_NO_NULLS,  FUNCTION_NULLABLE,  FUNCTION_NULLABLE_UNKNOWN;
/*      */     
/*      */     private JDBC4FunctionConstant() {}
/*      */   }
/*      */   
/*      */   protected DatabaseMetaDataUsingInfoSchema(MySQLConnection connToSet, String databaseToSet)
/*      */     throws SQLException
/*      */   {
/*   52 */     super(connToSet, databaseToSet);
/*      */     
/*   54 */     this.hasReferentialConstraintsView = this.conn.versionMeetsMinimum(5, 1, 10);
/*      */     
/*      */ 
/*   57 */     ResultSet rs = null;
/*      */     try
/*      */     {
/*   60 */       rs = super.getTables("INFORMATION_SCHEMA", null, "PARAMETERS", new String[0]);
/*      */       
/*   62 */       this.hasParametersView = rs.next();
/*      */     } finally {
/*   64 */       if (rs != null) {
/*   65 */         rs.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected ResultSet executeMetadataQuery(PreparedStatement pStmt) throws SQLException
/*      */   {
/*   72 */     ResultSet rs = pStmt.executeQuery();
/*   73 */     ((ResultSetInternalMethods)rs).setOwningStatement(null);
/*      */     
/*   75 */     return rs;
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
/*  116 */     if (columnNamePattern == null) {
/*  117 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  118 */         columnNamePattern = "%";
/*      */       } else {
/*  120 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  126 */     if ((catalog == null) && 
/*  127 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  128 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  132 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME,COLUMN_NAME, NULL AS GRANTOR, GRANTEE, PRIVILEGE_TYPE AS PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME =? AND COLUMN_NAME LIKE ? ORDER BY COLUMN_NAME, PRIVILEGE_TYPE";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  142 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  144 */       if (catalog != null) {
/*  145 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  147 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  150 */       pStmt.setString(2, table);
/*  151 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  153 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  154 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 64), new Field("", "TABLE_SCHEM", 1, 1), new Field("", "TABLE_NAME", 1, 64), new Field("", "COLUMN_NAME", 1, 64), new Field("", "GRANTOR", 1, 77), new Field("", "GRANTEE", 1, 77), new Field("", "PRIVILEGE", 1, 64), new Field("", "IS_GRANTABLE", 1, 3) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  164 */       return rs;
/*      */     } finally {
/*  166 */       if (pStmt != null) {
/*  167 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getColumns(String catalog, String schemaPattern, String tableName, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/*  218 */     if (columnNamePattern == null) {
/*  219 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  220 */         columnNamePattern = "%";
/*      */       } else {
/*  222 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  228 */     if ((catalog == null) && 
/*  229 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  230 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  234 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,COLUMN_NAME,");
/*      */     
/*      */ 
/*  237 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/*  239 */     sqlBuf.append(" AS DATA_TYPE, ");
/*      */     
/*  241 */     if (this.conn.getCapitalizeTypeNames()) {
/*  242 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 AND LOCATE('set', DATA_TYPE) <> 1 AND LOCATE('enum', DATA_TYPE) <> 1 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS TYPE_NAME,");
/*      */     } else {
/*  244 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 AND LOCATE('set', DATA_TYPE) <> 1 AND LOCATE('enum', DATA_TYPE) <> 1 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS TYPE_NAME,");
/*      */     }
/*      */     
/*  247 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS COLUMN_SIZE, " + MysqlIO.getMaxBuf() + " AS BUFFER_LENGTH," + "NUMERIC_SCALE AS DECIMAL_DIGITS," + "10 AS NUM_PREC_RADIX," + "CASE WHEN IS_NULLABLE='NO' THEN " + 0 + " ELSE CASE WHEN IS_NULLABLE='YES' THEN " + 1 + " ELSE " + 2 + " END END AS NULLABLE," + "COLUMN_COMMENT AS REMARKS," + "COLUMN_DEFAULT AS COLUMN_DEF," + "0 AS SQL_DATA_TYPE," + "0 AS SQL_DATETIME_SUB," + "CASE WHEN CHARACTER_OCTET_LENGTH > " + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE + " ELSE CHARACTER_OCTET_LENGTH END AS CHAR_OCTET_LENGTH," + "ORDINAL_POSITION," + "IS_NULLABLE," + "NULL AS SCOPE_CATALOG," + "NULL AS SCOPE_SCHEMA," + "NULL AS SCOPE_TABLE," + "NULL AS SOURCE_DATA_TYPE," + "IF (EXTRA LIKE '%auto_increment%','YES','NO') AS IS_AUTOINCREMENT " + "FROM INFORMATION_SCHEMA.COLUMNS WHERE ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  268 */     boolean operatingOnInformationSchema = "information_schema".equalsIgnoreCase(catalog);
/*      */     
/*  270 */     if (catalog != null) {
/*  271 */       if ((operatingOnInformationSchema) || ((StringUtils.indexOfIgnoreCase(0, catalog, "%") == -1) && (StringUtils.indexOfIgnoreCase(0, catalog, "_") == -1)))
/*      */       {
/*  273 */         sqlBuf.append("TABLE_SCHEMA = ? AND ");
/*      */       } else {
/*  275 */         sqlBuf.append("TABLE_SCHEMA LIKE ? AND ");
/*      */       }
/*      */     }
/*      */     else {
/*  279 */       sqlBuf.append("TABLE_SCHEMA LIKE ? AND ");
/*      */     }
/*      */     
/*  282 */     if (tableName != null) {
/*  283 */       if ((StringUtils.indexOfIgnoreCase(0, tableName, "%") == -1) && (StringUtils.indexOfIgnoreCase(0, tableName, "_") == -1))
/*      */       {
/*  285 */         sqlBuf.append("TABLE_NAME = ? AND ");
/*      */       } else {
/*  287 */         sqlBuf.append("TABLE_NAME LIKE ? AND ");
/*      */       }
/*      */     }
/*      */     else {
/*  291 */       sqlBuf.append("TABLE_NAME LIKE ? AND ");
/*      */     }
/*      */     
/*  294 */     if ((StringUtils.indexOfIgnoreCase(0, columnNamePattern, "%") == -1) && (StringUtils.indexOfIgnoreCase(0, columnNamePattern, "_") == -1))
/*      */     {
/*  296 */       sqlBuf.append("COLUMN_NAME = ? ");
/*      */     } else {
/*  298 */       sqlBuf.append("COLUMN_NAME LIKE ? ");
/*      */     }
/*  300 */     sqlBuf.append("ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION");
/*      */     
/*  302 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  305 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  307 */       if (catalog != null) {
/*  308 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  310 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  313 */       pStmt.setString(2, tableName);
/*  314 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  316 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  318 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createColumnsFields());
/*  319 */       return rs;
/*      */     } finally {
/*  321 */       if (pStmt != null) {
/*  322 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable)
/*      */     throws SQLException
/*      */   {
/*  397 */     if (primaryTable == null) {
/*  398 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  402 */     if ((primaryCatalog == null) && 
/*  403 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  404 */       primaryCatalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  408 */     if ((foreignCatalog == null) && 
/*  409 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  410 */       foreignCatalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  414 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + 7 + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN " + "INFORMATION_SCHEMA.TABLE_CONSTRAINTS B " + "USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? " + "AND A.TABLE_SCHEMA LIKE ? AND A.TABLE_NAME=? " + "ORDER BY " + "A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  448 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  451 */       pStmt = prepareMetaDataSafeStatement(sql);
/*  452 */       if (primaryCatalog != null) {
/*  453 */         pStmt.setString(1, primaryCatalog);
/*      */       } else {
/*  455 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  458 */       pStmt.setString(2, primaryTable);
/*      */       
/*  460 */       if (foreignCatalog != null) {
/*  461 */         pStmt.setString(3, foreignCatalog);
/*      */       } else {
/*  463 */         pStmt.setString(3, "%");
/*      */       }
/*      */       
/*  466 */       pStmt.setString(4, foreignTable);
/*      */       
/*  468 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  469 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  471 */       return rs;
/*      */     } finally {
/*  473 */       if (pStmt != null) {
/*  474 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/*  542 */     if (table == null) {
/*  543 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  547 */     if ((catalog == null) && 
/*  548 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  549 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  555 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME,A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + 7 + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A JOIN " + "INFORMATION_SCHEMA.TABLE_CONSTRAINTS B " + "USING (TABLE_SCHEMA, TABLE_NAME, CONSTRAINT_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? " + "ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  588 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  591 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  593 */       if (catalog != null) {
/*  594 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  596 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  599 */       pStmt.setString(2, table);
/*      */       
/*  601 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  603 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  605 */       return rs;
/*      */     } finally {
/*  607 */       if (pStmt != null) {
/*  608 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String generateOptionalRefContraintsJoin()
/*      */   {
/*  615 */     return this.hasReferentialConstraintsView ? "JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON (R.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND R.TABLE_NAME = B.TABLE_NAME AND R.CONSTRAINT_SCHEMA = B.TABLE_SCHEMA) " : "";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String generateDeleteRuleClause()
/*      */   {
/*  623 */     return this.hasReferentialConstraintsView ? "CASE WHEN R.DELETE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.DELETE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.DELETE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.DELETE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.DELETE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END " : String.valueOf(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String generateUpdateRuleClause()
/*      */   {
/*  633 */     return this.hasReferentialConstraintsView ? "CASE WHEN R.UPDATE_RULE='CASCADE' THEN " + String.valueOf(0) + " WHEN R.UPDATE_RULE='SET NULL' THEN " + String.valueOf(2) + " WHEN R.UPDATE_RULE='SET DEFAULT' THEN " + String.valueOf(4) + " WHEN R.UPDATE_RULE='RESTRICT' THEN " + String.valueOf(1) + " WHEN R.UPDATE_RULE='NO ACTION' THEN " + String.valueOf(3) + " ELSE " + String.valueOf(3) + " END " : String.valueOf(1);
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
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/*  703 */     if (table == null) {
/*  704 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  708 */     if ((catalog == null) && 
/*  709 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  710 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  714 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ," + generateUpdateRuleClause() + " AS UPDATE_RULE," + generateDeleteRuleClause() + " AS DELETE_RULE," + "A.CONSTRAINT_NAME AS FK_NAME," + "(SELECT CONSTRAINT_NAME FROM" + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS" + " WHERE TABLE_SCHEMA = A.REFERENCED_TABLE_SCHEMA AND" + " TABLE_NAME = A.REFERENCED_TABLE_NAME AND" + " CONSTRAINT_TYPE IN ('UNIQUE','PRIMARY KEY') LIMIT 1)" + " AS PK_NAME," + 7 + " AS DEFERRABILITY " + "FROM " + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE A " + "JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS B USING " + "(CONSTRAINT_NAME, TABLE_NAME) " + generateOptionalRefContraintsJoin() + "WHERE " + "B.CONSTRAINT_TYPE = 'FOREIGN KEY' " + "AND A.TABLE_SCHEMA LIKE ? " + "AND A.TABLE_NAME=? " + "AND A.REFERENCED_TABLE_SCHEMA IS NOT NULL " + "ORDER BY " + "A.REFERENCED_TABLE_SCHEMA, A.REFERENCED_TABLE_NAME, " + "A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  751 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  754 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  756 */       if (catalog != null) {
/*  757 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  759 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  762 */       pStmt.setString(2, table);
/*      */       
/*  764 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  766 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFkMetadataFields());
/*      */       
/*  768 */       return rs;
/*      */     } finally {
/*  770 */       if (pStmt != null) {
/*  771 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
/*      */     throws SQLException
/*      */   {
/*  836 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,NON_UNIQUE,TABLE_SCHEMA AS INDEX_QUALIFIER,INDEX_NAME,3 AS TYPE,SEQ_IN_INDEX AS ORDINAL_POSITION,COLUMN_NAME,COLLATION AS ASC_OR_DESC,CARDINALITY,NULL AS PAGES,NULL AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  847 */     if (unique) {
/*  848 */       sqlBuf.append(" AND NON_UNIQUE=0 ");
/*      */     }
/*      */     
/*  851 */     sqlBuf.append("ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX");
/*      */     
/*  853 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  856 */       if ((catalog == null) && 
/*  857 */         (this.conn.getNullCatalogMeansCurrent())) {
/*  858 */         catalog = this.database;
/*      */       }
/*      */       
/*      */ 
/*  862 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  864 */       if (catalog != null) {
/*  865 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  867 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  870 */       pStmt.setString(2, table);
/*      */       
/*  872 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  874 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createIndexInfoFields());
/*      */       
/*  876 */       return rs;
/*      */     } finally {
/*  878 */       if (pStmt != null) {
/*  879 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResultSet getPrimaryKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/*  912 */     if ((catalog == null) && 
/*  913 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  914 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  918 */     if (table == null) {
/*  919 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*  923 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX AS KEY_SEQ, 'PRIMARY' AS PK_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND INDEX_NAME='PRIMARY' ORDER BY TABLE_SCHEMA, TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  928 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  931 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  933 */       if (catalog != null) {
/*  934 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  936 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  939 */       pStmt.setString(2, table);
/*      */       
/*  941 */       ResultSet rs = executeMetadataQuery(pStmt);
/*  942 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 5), new Field("", "PK_NAME", 1, 32) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  950 */       return rs;
/*      */     } finally {
/*  952 */       if (pStmt != null) {
/*  953 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1001 */     if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0))
/*      */     {
/* 1003 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1004 */         procedureNamePattern = "%";
/*      */       } else {
/* 1006 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1012 */     String db = null;
/*      */     
/* 1014 */     if (catalog == null) {
/* 1015 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1016 */         db = this.database;
/*      */       }
/*      */     } else {
/* 1019 */       db = catalog;
/*      */     }
/*      */     
/* 1022 */     String sql = "SELECT ROUTINE_SCHEMA AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, ROUTINE_NAME AS PROCEDURE_NAME, NULL AS RESERVED_1, NULL AS RESERVED_2, NULL AS RESERVED_3, ROUTINE_COMMENT AS REMARKS, CASE WHEN ROUTINE_TYPE = 'PROCEDURE' THEN 1 WHEN ROUTINE_TYPE='FUNCTION' THEN 2 ELSE 0 END AS PROCEDURE_TYPE, ROUTINE_NAME AS SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES WHERE " + getRoutineTypeConditionForGetProcedures() + "ROUTINE_SCHEMA LIKE ? AND ROUTINE_NAME LIKE ? " + "ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME, ROUTINE_TYPE";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1037 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1040 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1042 */       if (db != null) {
/* 1043 */         pStmt.setString(1, db);
/*      */       } else {
/* 1045 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1048 */       pStmt.setString(2, procedureNamePattern);
/*      */       
/* 1050 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1051 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFieldMetadataForGetProcedures());
/*      */       
/* 1053 */       return rs;
/*      */     } finally {
/* 1055 */       if (pStmt != null) {
/* 1056 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getRoutineTypeConditionForGetProcedures()
/*      */   {
/* 1068 */     return "";
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
/*      */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 1137 */     if (!this.hasParametersView) {
/* 1138 */       return getProcedureColumnsNoISParametersView(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
/*      */     }
/*      */     
/* 1141 */     if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0))
/*      */     {
/* 1143 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1144 */         procedureNamePattern = "%";
/*      */       } else {
/* 1146 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1152 */     String db = null;
/*      */     
/* 1154 */     if (catalog == null) {
/* 1155 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1156 */         db = this.database;
/*      */       }
/*      */     } else {
/* 1159 */       db = catalog;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1178 */     StringBuffer sqlBuf = new StringBuffer("SELECT SPECIFIC_SCHEMA AS PROCEDURE_CAT, NULL AS `PROCEDURE_SCHEM`, SPECIFIC_NAME AS `PROCEDURE_NAME`, IFNULL(PARAMETER_NAME, '') AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN 1 WHEN PARAMETER_MODE = 'OUT' THEN 4 WHEN PARAMETER_MODE = 'INOUT' THEN 2 WHEN ORDINAL_POSITION = 0 THEN 5 ELSE 0 END AS `COLUMN_TYPE`, ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1190 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/* 1192 */     sqlBuf.append(" AS `DATA_TYPE`, ");
/*      */     
/*      */ 
/* 1195 */     if (this.conn.getCapitalizeTypeNames()) {
/* 1196 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,");
/*      */     } else {
/* 1198 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,");
/*      */     }
/*      */     
/*      */ 
/* 1202 */     sqlBuf.append("NUMERIC_PRECISION AS `PRECISION`, ");
/*      */     
/* 1204 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1209 */     sqlBuf.append("NUMERIC_SCALE AS `SCALE`, ");
/*      */     
/* 1211 */     sqlBuf.append("10 AS RADIX,");
/* 1212 */     sqlBuf.append("1 AS `NULLABLE`, NULL AS `REMARKS`, NULL AS `COLUMN_DEF`, NULL AS `SQL_DATA_TYPE`, NULL AS `SQL_DATETIME_SUB`, CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`, ORDINAL_POSITION, 'YES' AS `IS_NULLABLE`, SPECIFIC_NAME FROM INFORMATION_SCHEMA.PARAMETERS WHERE " + getRoutineTypeConditionForGetProcedureColumns() + "SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) " + "ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ROUTINE_TYPE, ORDINAL_POSITION");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1226 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1229 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/* 1231 */       if (db != null) {
/* 1232 */         pStmt.setString(1, db);
/*      */       } else {
/* 1234 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1237 */       pStmt.setString(2, procedureNamePattern);
/* 1238 */       pStmt.setString(3, columnNamePattern);
/*      */       
/* 1240 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1241 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createProcedureColumnsFields());
/*      */       
/* 1243 */       return rs;
/*      */     } finally {
/* 1245 */       if (pStmt != null) {
/* 1246 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSet getProcedureColumnsNoISParametersView(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 1258 */     return super.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getRoutineTypeConditionForGetProcedureColumns()
/*      */   {
/* 1268 */     return "";
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
/*      */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
/*      */     throws SQLException
/*      */   {
/* 1309 */     if ((catalog == null) && 
/* 1310 */       (this.conn.getNullCatalogMeansCurrent())) {
/* 1311 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/* 1315 */     if (tableNamePattern == null) {
/* 1316 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1317 */         tableNamePattern = "%";
/*      */       } else {
/* 1319 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1326 */     String tmpCat = "";
/*      */     
/* 1328 */     if ((catalog == null) || (catalog.length() == 0)) {
/* 1329 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1330 */         tmpCat = this.database;
/*      */       }
/*      */     } else {
/* 1333 */       tmpCat = catalog;
/*      */     }
/*      */     
/* 1336 */     List<String> parseList = StringUtils.splitDBdotName(tableNamePattern, tmpCat, this.quotedId, this.conn.isNoBackslashEscapesSet());
/*      */     String tableNamePat;
/*      */     String tableNamePat;
/* 1339 */     if (parseList.size() == 2) {
/* 1340 */       tableNamePat = (String)parseList.get(1);
/*      */     } else {
/* 1342 */       tableNamePat = tableNamePattern;
/*      */     }
/*      */     
/* 1345 */     PreparedStatement pStmt = null;
/*      */     
/* 1347 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN CASE WHEN TABLE_SCHEMA = 'mysql' OR TABLE_SCHEMA = 'performance_schema' THEN 'SYSTEM TABLE' ELSE 'TABLE' END WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS, NULL AS TYPE_CAT, NULL AS TYPE_SCHEM, NULL AS TYPE_NAME, NULL AS SELF_REFERENCING_COL_NAME, NULL AS REF_GENERATION FROM INFORMATION_SCHEMA.TABLES WHERE ";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1354 */     boolean operatingOnInformationSchema = "information_schema".equalsIgnoreCase(catalog);
/* 1355 */     if (catalog != null) {
/* 1356 */       if ((operatingOnInformationSchema) || ((StringUtils.indexOfIgnoreCase(0, catalog, "%") == -1) && (StringUtils.indexOfIgnoreCase(0, catalog, "_") == -1)))
/*      */       {
/* 1358 */         sql = sql + "TABLE_SCHEMA = ? ";
/*      */       } else {
/* 1360 */         sql = sql + "TABLE_SCHEMA LIKE ? ";
/*      */       }
/*      */     }
/*      */     else {
/* 1364 */       sql = sql + "TABLE_SCHEMA LIKE ? ";
/*      */     }
/*      */     
/* 1367 */     if (tableNamePat != null) {
/* 1368 */       if ((StringUtils.indexOfIgnoreCase(0, tableNamePat, "%") == -1) && (StringUtils.indexOfIgnoreCase(0, tableNamePat, "_") == -1))
/*      */       {
/* 1370 */         sql = sql + "AND TABLE_NAME = ? ";
/*      */       } else {
/* 1372 */         sql = sql + "AND TABLE_NAME LIKE ? ";
/*      */       }
/*      */     }
/*      */     else {
/* 1376 */       sql = sql + "AND TABLE_NAME LIKE ? ";
/*      */     }
/* 1378 */     sql = sql + "HAVING TABLE_TYPE IN (?,?,?,?,?) ";
/* 1379 */     sql = sql + "ORDER BY TABLE_TYPE, TABLE_SCHEMA, TABLE_NAME";
/*      */     try {
/* 1381 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1383 */       if (catalog != null) {
/* 1384 */         pStmt.setString(1, catalog);
/*      */       } else {
/* 1386 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1389 */       pStmt.setString(2, tableNamePat);
/*      */       
/*      */       int i;
/*      */       
/* 1393 */       if ((types == null) || (types.length == 0)) {
/* 1394 */         DatabaseMetaData.TableType[] tableTypes = DatabaseMetaData.TableType.values();
/* 1395 */         for (int i = 0; i < 5; i++) {
/* 1396 */           pStmt.setString(3 + i, tableTypes[i].getName());
/*      */         }
/*      */       } else {
/* 1399 */         for (int i = 0; i < 5; i++) {
/* 1400 */           pStmt.setNull(3 + i, 12);
/*      */         }
/*      */         
/* 1403 */         int idx = 3;
/* 1404 */         for (i = 0; i < types.length; i++) {
/* 1405 */           DatabaseMetaData.TableType tableType = DatabaseMetaData.TableType.getTableTypeEqualTo(types[i]);
/* 1406 */           if (tableType != DatabaseMetaData.TableType.UNKNOWN) {
/* 1407 */             pStmt.setString(idx++, tableType.getName());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1412 */       ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/* 1414 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createTablesFields());
/*      */       
/* 1416 */       return rs;
/*      */     } finally {
/* 1418 */       if (pStmt != null) {
/* 1419 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean gethasParametersView() {
/* 1425 */     return this.hasParametersView;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ResultSet getVersionColumns(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/* 1433 */     if ((catalog == null) && 
/* 1434 */       (this.conn.getNullCatalogMeansCurrent())) {
/* 1435 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/* 1439 */     if (table == null) {
/* 1440 */       throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 1444 */     StringBuffer sqlBuf = new StringBuffer("SELECT NULL AS SCOPE, COLUMN_NAME, ");
/*      */     
/* 1446 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/* 1447 */     sqlBuf.append(" AS DATA_TYPE, ");
/*      */     
/* 1449 */     sqlBuf.append("COLUMN_TYPE AS TYPE_NAME, ");
/* 1450 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS COLUMN_SIZE, ");
/*      */     
/*      */ 
/*      */ 
/* 1454 */     sqlBuf.append(MysqlIO.getMaxBuf() + " AS BUFFER_LENGTH," + "NUMERIC_SCALE AS DECIMAL_DIGITS, " + Integer.toString(1) + " AS PSEUDO_COLUMN " + "FROM INFORMATION_SCHEMA.COLUMNS " + "WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?" + " AND EXTRA LIKE '%on update CURRENT_TIMESTAMP%'");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1462 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1465 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/* 1467 */       if (catalog != null) {
/* 1468 */         pStmt.setString(1, catalog);
/*      */       } else {
/* 1470 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1473 */       pStmt.setString(2, table);
/*      */       
/* 1475 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1476 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "SCOPE", 5, 5), new Field("", "COLUMN_NAME", 1, 32), new Field("", "DATA_TYPE", 4, 5), new Field("", "TYPE_NAME", 1, 16), new Field("", "COLUMN_SIZE", 4, 16), new Field("", "BUFFER_LENGTH", 4, 16), new Field("", "DECIMAL_DIGITS", 5, 16), new Field("", "PSEUDO_COLUMN", 5, 5) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1487 */       return rs;
/*      */     } finally {
/* 1489 */       if (pStmt != null) {
/* 1490 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1597 */     if (!this.hasParametersView) {
/* 1598 */       return super.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
/*      */     }
/*      */     
/* 1601 */     if ((functionNamePattern == null) || (functionNamePattern.length() == 0))
/*      */     {
/* 1603 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1604 */         functionNamePattern = "%";
/*      */       } else {
/* 1606 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1612 */     String db = null;
/*      */     
/* 1614 */     if (catalog == null) {
/* 1615 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1616 */         db = this.database;
/*      */       }
/*      */     } else {
/* 1619 */       db = catalog;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1627 */     StringBuffer sqlBuf = new StringBuffer("SELECT SPECIFIC_SCHEMA AS FUNCTION_CAT, NULL AS `FUNCTION_SCHEM`, SPECIFIC_NAME AS `FUNCTION_NAME`, IFNULL(PARAMETER_NAME, '') AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN " + getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_COLUMN_IN) + " WHEN PARAMETER_MODE = 'OUT' THEN " + getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_COLUMN_OUT) + " WHEN PARAMETER_MODE = 'INOUT' THEN " + getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_COLUMN_INOUT) + " WHEN ORDINAL_POSITION = 0 THEN " + getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_COLUMN_RETURN) + " ELSE " + getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_COLUMN_UNKNOWN) + " END AS `COLUMN_TYPE`, ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1639 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/* 1641 */     sqlBuf.append(" AS `DATA_TYPE`, ");
/*      */     
/*      */ 
/* 1644 */     if (this.conn.getCapitalizeTypeNames()) {
/* 1645 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS `TYPE_NAME`,");
/*      */     } else {
/* 1647 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', DATA_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS `TYPE_NAME`,");
/*      */     }
/*      */     
/*      */ 
/* 1651 */     sqlBuf.append("NUMERIC_PRECISION AS `PRECISION`, ");
/*      */     
/* 1653 */     sqlBuf.append("CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH, ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1658 */     sqlBuf.append("NUMERIC_SCALE AS `SCALE`, ");
/*      */     
/* 1660 */     sqlBuf.append("10 AS RADIX,");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1667 */     sqlBuf.append(getJDBC4FunctionConstant(JDBC4FunctionConstant.FUNCTION_NULLABLE) + " AS `NULLABLE`, " + " NULL AS `REMARKS`, " + "CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`, " + " ORDINAL_POSITION, " + "'YES' AS `IS_NULLABLE`, " + "SPECIFIC_NAME " + "FROM INFORMATION_SCHEMA.PARAMETERS WHERE " + "SPECIFIC_SCHEMA LIKE ? AND SPECIFIC_NAME LIKE ? AND (PARAMETER_NAME LIKE ? OR PARAMETER_NAME IS NULL) " + "AND ROUTINE_TYPE='FUNCTION' ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1677 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1680 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/* 1682 */       if (db != null) {
/* 1683 */         pStmt.setString(1, db);
/*      */       } else {
/* 1685 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1688 */       pStmt.setString(2, functionNamePattern);
/* 1689 */       pStmt.setString(3, columnNamePattern);
/*      */       
/* 1691 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1692 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(createFunctionColumnsFields());
/*      */       
/* 1694 */       return rs;
/*      */     } finally {
/* 1696 */       if (pStmt != null) {
/* 1697 */         pStmt.close();
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
/*      */   protected int getJDBC4FunctionConstant(JDBC4FunctionConstant constant)
/*      */   {
/* 1712 */     return 0;
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
/*      */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
/*      */     throws SQLException
/*      */   {
/* 1766 */     if ((functionNamePattern == null) || (functionNamePattern.length() == 0))
/*      */     {
/* 1768 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1769 */         functionNamePattern = "%";
/*      */       } else {
/* 1771 */         throw SQLError.createSQLException("Function name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1777 */     String db = null;
/*      */     
/* 1779 */     if (catalog == null) {
/* 1780 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1781 */         db = this.database;
/*      */       }
/*      */     } else {
/* 1784 */       db = catalog;
/*      */     }
/*      */     
/* 1787 */     String sql = "SELECT ROUTINE_SCHEMA AS FUNCTION_CAT, NULL AS FUNCTION_SCHEM, ROUTINE_NAME AS FUNCTION_NAME, ROUTINE_COMMENT AS REMARKS, " + getJDBC4FunctionNoTableConstant() + " AS FUNCTION_TYPE, ROUTINE_NAME AS SPECIFIC_NAME FROM INFORMATION_SCHEMA.ROUTINES " + "WHERE ROUTINE_TYPE LIKE 'FUNCTION' AND ROUTINE_SCHEMA LIKE ? AND " + "ROUTINE_NAME LIKE ? ORDER BY FUNCTION_CAT, FUNCTION_SCHEM, FUNCTION_NAME, SPECIFIC_NAME";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1793 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1796 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1798 */       pStmt.setString(1, db != null ? db : "%");
/* 1799 */       pStmt.setString(2, functionNamePattern);
/*      */       
/* 1801 */       ResultSet rs = executeMetadataQuery(pStmt);
/* 1802 */       ((ResultSetInternalMethods)rs).redefineFieldsForDBMD(new Field[] { new Field("", "FUNCTION_CAT", 1, 255), new Field("", "FUNCTION_SCHEM", 1, 255), new Field("", "FUNCTION_NAME", 1, 255), new Field("", "REMARKS", 1, 255), new Field("", "FUNCTION_TYPE", 5, 6), new Field("", "SPECIFIC_NAME", 1, 255) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1811 */       return rs;
/*      */     } finally {
/* 1813 */       if (pStmt != null) {
/* 1814 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getJDBC4FunctionNoTableConstant()
/*      */   {
/* 1826 */     return 0;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\DatabaseMetaDataUsingInfoSchema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */