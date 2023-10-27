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
/*     */ public class JDBC4DatabaseMetaDataUsingInfoSchema
/*     */   extends DatabaseMetaDataUsingInfoSchema
/*     */ {
/*     */   public JDBC4DatabaseMetaDataUsingInfoSchema(MySQLConnection connToSet, String databaseToSet)
/*     */     throws SQLException
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResultSet getProcedureColumnsNoISParametersView(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*     */     throws SQLException
/*     */   {
/* 100 */     Field[] fields = createProcedureColumnsFields();
/*     */     
/* 102 */     return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, this.conn.getGetProceduresReturnsFunctions());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getRoutineTypeConditionForGetProcedures()
/*     */   {
/* 113 */     return this.conn.getGetProceduresReturnsFunctions() ? "" : "ROUTINE_TYPE = 'PROCEDURE' AND ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getRoutineTypeConditionForGetProcedureColumns()
/*     */   {
/* 124 */     return this.conn.getGetProceduresReturnsFunctions() ? "" : "ROUTINE_TYPE = 'PROCEDURE' AND ";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getJDBC4FunctionConstant(DatabaseMetaDataUsingInfoSchema.JDBC4FunctionConstant constant)
/*     */   {
/* 136 */     switch (constant) {
/*     */     case FUNCTION_COLUMN_IN: 
/* 138 */       return 1;
/*     */     case FUNCTION_COLUMN_INOUT: 
/* 140 */       return 2;
/*     */     case FUNCTION_COLUMN_OUT: 
/* 142 */       return 3;
/*     */     case FUNCTION_COLUMN_RETURN: 
/* 144 */       return 4;
/*     */     case FUNCTION_COLUMN_RESULT: 
/* 146 */       return 5;
/*     */     case FUNCTION_COLUMN_UNKNOWN: 
/* 148 */       return 0;
/*     */     case FUNCTION_NO_NULLS: 
/* 150 */       return 0;
/*     */     case FUNCTION_NULLABLE: 
/* 152 */       return 1;
/*     */     case FUNCTION_NULLABLE_UNKNOWN: 
/* 154 */       return 2;
/*     */     }
/* 156 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getJDBC4FunctionNoTableConstant()
/*     */   {
/* 166 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getColumnType(boolean isOutParam, boolean isInParam, boolean isReturnParam, boolean forGetFunctionColumns)
/*     */   {
/* 176 */     return JDBC4DatabaseMetaData.getProcedureOrFunctionColumnType(isOutParam, isInParam, isReturnParam, forGetFunctionColumns);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4DatabaseMetaDataUsingInfoSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */