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
/*     */ public class MiniAdmin
/*     */ {
/*     */   private Connection conn;
/*     */   
/*     */   public MiniAdmin(java.sql.Connection conn)
/*     */     throws SQLException
/*     */   {
/*  55 */     if (conn == null) {
/*  56 */       throw SQLError.createSQLException(Messages.getString("MiniAdmin.0"), "S1000", null);
/*     */     }
/*     */     
/*     */ 
/*  60 */     if (!(conn instanceof Connection)) {
/*  61 */       throw SQLError.createSQLException(Messages.getString("MiniAdmin.1"), "S1000", ((ConnectionImpl)conn).getExceptionInterceptor());
/*     */     }
/*     */     
/*     */ 
/*  65 */     this.conn = ((Connection)conn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MiniAdmin(String jdbcUrl)
/*     */     throws SQLException
/*     */   {
/*  78 */     this(jdbcUrl, new Properties());
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
/*     */   public MiniAdmin(String jdbcUrl, Properties props)
/*     */     throws SQLException
/*     */   {
/*  94 */     this.conn = ((Connection)new Driver().connect(jdbcUrl, props));
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
/*     */   public void shutdown()
/*     */     throws SQLException
/*     */   {
/* 108 */     this.conn.shutdownServer();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\MiniAdmin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */