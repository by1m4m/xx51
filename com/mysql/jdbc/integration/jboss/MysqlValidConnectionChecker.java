/*    */ package com.mysql.jdbc.integration.jboss;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.sql.Connection;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
/*    */ import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;
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
/*    */ public final class MysqlValidConnectionChecker
/*    */   implements ValidConnectionChecker, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8909421133577519177L;
/*    */   
/*    */   public SQLException isValidConnection(Connection conn)
/*    */   {
/* 58 */     Statement pingStatement = null;
/*    */     try
/*    */     {
/* 61 */       pingStatement = conn.createStatement();
/*    */       
/* 63 */       pingStatement.executeQuery("/* ping */ SELECT 1").close();
/*    */       
/* 65 */       return null;
/*    */     } catch (SQLException sqlEx) {
/* 67 */       return sqlEx;
/*    */     } finally {
/* 69 */       if (pingStatement != null) {
/*    */         try {
/* 71 */           pingStatement.close();
/*    */         }
/*    */         catch (SQLException sqlEx) {}
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\integration\jboss\MysqlValidConnectionChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */