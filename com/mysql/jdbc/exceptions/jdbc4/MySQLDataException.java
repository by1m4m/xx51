/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLDataException;
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
/*    */ public class MySQLDataException
/*    */   extends SQLDataException
/*    */ {
/*    */   public MySQLDataException() {}
/*    */   
/*    */   public MySQLDataException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 36 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason, String SQLState) {
/* 40 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason) {
/* 44 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLDataException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */