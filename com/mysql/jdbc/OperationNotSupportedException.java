/*    */ package com.mysql.jdbc;
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
/*    */ class OperationNotSupportedException
/*    */   extends SQLException
/*    */ {
/*    */   static final long serialVersionUID = 474918612056813430L;
/*    */   
/*    */   OperationNotSupportedException()
/*    */   {
/* 31 */     super(Messages.getString("RowDataDynamic.10"), "S1009");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\OperationNotSupportedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */