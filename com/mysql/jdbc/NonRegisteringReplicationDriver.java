/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NonRegisteringReplicationDriver
/*    */   extends NonRegisteringDriver
/*    */ {
/*    */   public NonRegisteringReplicationDriver()
/*    */     throws SQLException
/*    */   {}
/*    */   
/*    */   public Connection connect(String url, Properties info)
/*    */     throws SQLException
/*    */   {
/* 51 */     return connectReplicationConnection(url, info);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\NonRegisteringReplicationDriver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */