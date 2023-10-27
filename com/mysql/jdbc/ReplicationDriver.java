/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.Driver;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.SQLException;
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
/*    */ public class ReplicationDriver
/*    */   extends NonRegisteringReplicationDriver
/*    */   implements Driver
/*    */ {
/*    */   public ReplicationDriver()
/*    */     throws SQLException
/*    */   {}
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 65 */       DriverManager.registerDriver(new NonRegisteringReplicationDriver());
/*    */     }
/*    */     catch (SQLException E) {
/* 68 */       throw new RuntimeException("Can't register driver!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ReplicationDriver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */