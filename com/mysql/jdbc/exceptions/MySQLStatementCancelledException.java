/*    */ package com.mysql.jdbc.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MySQLStatementCancelledException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   static final long serialVersionUID = -8762717748377197378L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MySQLStatementCancelledException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 31 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException(String reason, String SQLState) {
/* 35 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException(String reason) {
/* 39 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLStatementCancelledException() {
/* 43 */     super("Statement cancelled due to client request");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\exceptions\MySQLStatementCancelledException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */