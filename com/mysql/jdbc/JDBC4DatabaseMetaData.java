/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.RowIdLifetime;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBC4DatabaseMetaData
/*     */   extends DatabaseMetaData
/*     */ {
/*     */   public JDBC4DatabaseMetaData(MySQLConnection connToSet, String databaseToSet)
/*     */   {
/*  38 */     super(connToSet, databaseToSet);
/*     */   }
/*     */   
/*     */   public RowIdLifetime getRowIdLifetime() throws SQLException {
/*  42 */     return RowIdLifetime.ROWID_UNSUPPORTED;
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
/*     */   public boolean isWrapperFor(Class<?> iface)
/*     */     throws SQLException
/*     */   {
/*  63 */     return iface.isInstance(this);
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
/*  84 */       return (T)iface.cast(this);
/*     */     } catch (ClassCastException cce) {
/*  86 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.conn.getExceptionInterceptor());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean autoCommitFailureClosesAllResultSets() throws SQLException
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*     */     throws SQLException
/*     */   {
/* 104 */     Field[] fields = createProcedureColumnsFields();
/*     */     
/* 106 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
/*     */     throws SQLException
/*     */   {
/* 119 */     Field[] fields = createFieldMetadataForGetProcedures();
/*     */     
/* 121 */     return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getJDBC4FunctionNoTableConstant()
/*     */   {
/* 131 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns)
/*     */   {
/* 141 */     return getProcedureOrFunctionColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns);
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
/*     */   protected static int getProcedureOrFunctionColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns)
/*     */   {
/* 162 */     if ((isInParam) && (isOutParam))
/* 163 */       return forGetFunctionColumns ? 2 : 2;
/* 164 */     if (isInParam)
/* 165 */       return forGetFunctionColumns ? 1 : 1;
/* 166 */     if (isOutParam)
/* 167 */       return forGetFunctionColumns ? 3 : 4;
/* 168 */     if (isReturnParam) {
/* 169 */       return forGetFunctionColumns ? 4 : 5;
/*     */     }
/* 171 */     return forGetFunctionColumns ? 0 : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4DatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */