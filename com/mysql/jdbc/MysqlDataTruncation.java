/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.DataTruncation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MysqlDataTruncation
/*    */   extends DataTruncation
/*    */ {
/*    */   static final long serialVersionUID = 3263928195256986226L;
/*    */   private String message;
/*    */   private int vendorErrorCode;
/*    */   
/*    */   public MysqlDataTruncation(String message, int index, boolean parameter, boolean read, int dataSize, int transferSize, int vendorErrorCode)
/*    */   {
/* 65 */     super(index, parameter, read, dataSize, transferSize);
/*    */     
/* 67 */     this.message = message;
/* 68 */     this.vendorErrorCode = vendorErrorCode;
/*    */   }
/*    */   
/*    */   public int getErrorCode() {
/* 72 */     return this.vendorErrorCode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 81 */     return super.getMessage() + ": " + this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\MysqlDataTruncation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */