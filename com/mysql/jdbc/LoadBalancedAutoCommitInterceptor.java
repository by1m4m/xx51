/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadBalancedAutoCommitInterceptor
/*     */   implements StatementInterceptorV2
/*     */ {
/*  37 */   private int matchingAfterStatementCount = 0;
/*  38 */   private int matchingAfterStatementThreshold = 0;
/*     */   private String matchingAfterStatementRegex;
/*     */   private ConnectionImpl conn;
/*  41 */   private LoadBalancingConnectionProxy proxy = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean executeTopLevelOnly()
/*     */   {
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   public void init(Connection connection, Properties props) throws SQLException {
/*  58 */     this.conn = ((ConnectionImpl)connection);
/*     */     
/*  60 */     String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
/*     */     try
/*     */     {
/*  63 */       this.matchingAfterStatementThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
/*     */     }
/*     */     catch (NumberFormatException nfe) {}
/*     */     
/*  67 */     String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
/*  68 */     if ("".equals(autoCommitSwapRegex)) {
/*  69 */       return;
/*     */     }
/*  71 */     this.matchingAfterStatementRegex = autoCommitSwapRegex;
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
/*     */   public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
/*     */     throws SQLException
/*     */   {
/*  85 */     if (!this.conn.getAutoCommit()) {
/*  86 */       this.matchingAfterStatementCount = 0;
/*     */     }
/*     */     else
/*     */     {
/*  90 */       if ((this.proxy == null) && (this.conn.isProxySet())) {
/*  91 */         MySQLConnection lcl_proxy = this.conn.getLoadBalanceSafeProxy();
/*  92 */         while ((lcl_proxy != null) && (!(lcl_proxy instanceof LoadBalancedMySQLConnection))) {
/*  93 */           lcl_proxy = lcl_proxy.getLoadBalanceSafeProxy();
/*     */         }
/*  95 */         if (lcl_proxy != null) {
/*  96 */           this.proxy = ((LoadBalancedMySQLConnection)lcl_proxy).getProxy();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 101 */       if (this.proxy != null)
/*     */       {
/* 103 */         if ((this.matchingAfterStatementRegex == null) || (sql.matches(this.matchingAfterStatementRegex)))
/*     */         {
/* 105 */           this.matchingAfterStatementCount += 1;
/*     */         }
/*     */       }
/*     */       
/* 109 */       if (this.matchingAfterStatementCount >= this.matchingAfterStatementThreshold) {
/* 110 */         this.matchingAfterStatementCount = 0;
/*     */         try {
/* 112 */           if (this.proxy != null) {
/* 113 */             this.proxy.pickNewConnection();
/*     */           }
/*     */         }
/*     */         catch (SQLException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     return originalResultSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
/*     */     throws SQLException
/*     */   {
/* 132 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\LoadBalancedAutoCommitInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */