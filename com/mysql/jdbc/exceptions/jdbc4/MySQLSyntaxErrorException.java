/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import java.sql.SQLSyntaxErrorException;
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
/*    */ public class MySQLSyntaxErrorException
/*    */   extends SQLSyntaxErrorException
/*    */ {
/*    */   public MySQLSyntaxErrorException() {}
/*    */   
/*    */   public MySQLSyntaxErrorException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 36 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLSyntaxErrorException(String reason, String SQLState) {
/* 40 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLSyntaxErrorException(String reason) {
/* 44 */     super(reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\exceptions\jdbc4\MySQLSyntaxErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */