/*    */ package com.mysql.jdbc.jdbc2.optional;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.ConnectionPoolDataSource;
/*    */ import javax.sql.PooledConnection;
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
/*    */ public class MysqlConnectionPoolDataSource
/*    */   extends MysqlDataSource
/*    */   implements ConnectionPoolDataSource
/*    */ {
/*    */   static final long serialVersionUID = -7767325445592304961L;
/*    */   
/*    */   public synchronized PooledConnection getPooledConnection()
/*    */     throws SQLException
/*    */   {
/* 62 */     java.sql.Connection connection = getConnection();
/* 63 */     MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((com.mysql.jdbc.Connection)connection);
/*    */     
/*    */ 
/* 66 */     return mysqlPooledConnection;
/*    */   }
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
/*    */   public synchronized PooledConnection getPooledConnection(String s, String s1)
/*    */     throws SQLException
/*    */   {
/* 83 */     java.sql.Connection connection = getConnection(s, s1);
/* 84 */     MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((com.mysql.jdbc.Connection)connection);
/*    */     
/*    */ 
/* 87 */     return mysqlPooledConnection;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\MysqlConnectionPoolDataSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */