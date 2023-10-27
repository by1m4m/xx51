/*    */ package com.mysql.jdbc.jdbc2.optional;
/*    */ 
/*    */ import com.mysql.jdbc.ConnectionImpl;
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.XAConnection;
/*    */ import javax.sql.XADataSource;
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
/*    */ public class MysqlXADataSource
/*    */   extends MysqlDataSource
/*    */   implements XADataSource
/*    */ {
/*    */   static final long serialVersionUID = 7911390333152247455L;
/*    */   
/*    */   public XAConnection getXAConnection()
/*    */     throws SQLException
/*    */   {
/* 50 */     java.sql.Connection conn = getConnection();
/*    */     
/* 52 */     return wrapConnection(conn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public XAConnection getXAConnection(String u, String p)
/*    */     throws SQLException
/*    */   {
/* 61 */     java.sql.Connection conn = getConnection(u, p);
/*    */     
/* 63 */     return wrapConnection(conn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private XAConnection wrapConnection(java.sql.Connection conn)
/*    */     throws SQLException
/*    */   {
/* 71 */     if ((getPinGlobalTxToPhysicalConnection()) || (((com.mysql.jdbc.Connection)conn).getPinGlobalTxToPhysicalConnection()))
/*    */     {
/* 73 */       return SuspendableXAConnection.getInstance((ConnectionImpl)conn);
/*    */     }
/*    */     
/* 76 */     return MysqlXAConnection.getInstance((ConnectionImpl)conn, getLogXaCommands());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXADataSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */