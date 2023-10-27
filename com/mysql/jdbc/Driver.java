/*    */ package com.mysql.jdbc;
/*    */ 
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
/*    */ public class Driver
/*    */   extends NonRegisteringDriver
/*    */   implements java.sql.Driver
/*    */ {
/*    */   public Driver()
/*    */     throws SQLException
/*    */   {}
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 63 */       DriverManager.registerDriver(new Driver());
/*    */     } catch (SQLException E) {
/* 65 */       throw new RuntimeException("Can't register driver!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\Driver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */