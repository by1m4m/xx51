/*    */ package com.mysql.jdbc.integration.jboss;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter;
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
/*    */ public final class ExtendedMysqlExceptionSorter
/*    */   extends MySQLExceptionSorter
/*    */ {
/*    */   static final long serialVersionUID = -2454582336945931069L;
/*    */   
/*    */   public boolean isExceptionFatal(SQLException ex)
/*    */   {
/* 47 */     String sqlState = ex.getSQLState();
/*    */     
/* 49 */     if ((sqlState != null) && (sqlState.startsWith("08"))) {
/* 50 */       return true;
/*    */     }
/*    */     
/* 53 */     return super.isExceptionFatal(ex);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\integration\jboss\ExtendedMysqlExceptionSorter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */