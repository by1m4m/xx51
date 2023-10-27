/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSubInterceptorWrapper
/*    */   implements StatementInterceptorV2
/*    */ {
/*    */   private final StatementInterceptorV2 underlyingInterceptor;
/*    */   
/*    */   public NoSubInterceptorWrapper(StatementInterceptorV2 underlyingInterceptor)
/*    */   {
/* 38 */     if (underlyingInterceptor == null) {
/* 39 */       throw new RuntimeException("Interceptor to be wrapped can not be NULL");
/*    */     }
/*    */     
/* 42 */     this.underlyingInterceptor = underlyingInterceptor;
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 46 */     this.underlyingInterceptor.destroy();
/*    */   }
/*    */   
/*    */   public boolean executeTopLevelOnly() {
/* 50 */     return this.underlyingInterceptor.executeTopLevelOnly();
/*    */   }
/*    */   
/*    */   public void init(Connection conn, Properties props) throws SQLException {
/* 54 */     this.underlyingInterceptor.init(conn, props);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
/*    */     throws SQLException
/*    */   {
/* 62 */     this.underlyingInterceptor.postProcess(sql, interceptedStatement, originalResultSet, connection, warningCount, noIndexUsed, noGoodIndexUsed, statementException);
/*    */     
/*    */ 
/* 65 */     return null;
/*    */   }
/*    */   
/*    */   public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
/*    */     throws SQLException
/*    */   {
/* 71 */     this.underlyingInterceptor.preProcess(sql, interceptedStatement, connection);
/*    */     
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public StatementInterceptorV2 getUnderlyingInterceptor() {
/* 77 */     return this.underlyingInterceptor;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\NoSubInterceptorWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */