/*    */ package com.mysql.jdbc.exceptions;
/*    */ 
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
/*    */ public class MySQLTransientException
/*    */   extends SQLException
/*    */ {
/*    */   static final long serialVersionUID = -1885878228558607563L;
/*    */   
/*    */   public MySQLTransientException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransientException() {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\exceptions\MySQLTransientException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */