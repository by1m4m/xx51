/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class ReflectiveStatementInterceptorAdapter
/*     */   implements StatementInterceptorV2
/*     */ {
/*     */   private final StatementInterceptor toProxy;
/*     */   final Method v2PostProcessMethod;
/*     */   
/*     */   public ReflectiveStatementInterceptorAdapter(StatementInterceptor toProxy)
/*     */   {
/*  39 */     this.toProxy = toProxy;
/*  40 */     this.v2PostProcessMethod = getV2PostProcessMethod(toProxy.getClass());
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  44 */     this.toProxy.destroy();
/*     */   }
/*     */   
/*     */   public boolean executeTopLevelOnly() {
/*  48 */     return this.toProxy.executeTopLevelOnly();
/*     */   }
/*     */   
/*     */   public void init(Connection conn, Properties props) throws SQLException {
/*  52 */     this.toProxy.init(conn, props);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  62 */       return (ResultSetInternalMethods)this.v2PostProcessMethod.invoke(this.toProxy, new Object[] { sql, interceptedStatement, originalResultSet, connection, Integer.valueOf(warningCount), noIndexUsed ? Boolean.TRUE : Boolean.FALSE, noGoodIndexUsed ? Boolean.TRUE : Boolean.FALSE, statementException });
/*     */ 
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/*     */ 
/*  68 */       SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
/*  69 */       sqlEx.initCause(e);
/*     */       
/*  71 */       throw sqlEx;
/*     */     } catch (IllegalAccessException e) {
/*  73 */       SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
/*  74 */       sqlEx.initCause(e);
/*     */       
/*  76 */       throw sqlEx;
/*     */     } catch (InvocationTargetException e) {
/*  78 */       SQLException sqlEx = new SQLException("Unable to reflectively invoke interceptor");
/*  79 */       sqlEx.initCause(e);
/*     */       
/*  81 */       throw sqlEx;
/*     */     }
/*     */   }
/*     */   
/*     */   public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
/*     */     throws SQLException
/*     */   {
/*  88 */     return this.toProxy.preProcess(sql, interceptedStatement, connection);
/*     */   }
/*     */   
/*     */   public static final Method getV2PostProcessMethod(Class<?> toProxyClass) {
/*     */     try {
/*  93 */       return toProxyClass.getMethod("postProcess", new Class[] { String.class, Statement.class, ResultSetInternalMethods.class, Connection.class, Integer.TYPE, Boolean.TYPE, Boolean.TYPE, SQLException.class });
/*     */ 
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/*     */ 
/*  99 */       return null;
/*     */     } catch (NoSuchMethodException e) {}
/* 101 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ReflectiveStatementInterceptorAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */