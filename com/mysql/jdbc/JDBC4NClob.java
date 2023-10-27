/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.NClob;
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
/*    */ public class JDBC4NClob
/*    */   extends Clob
/*    */   implements NClob
/*    */ {
/*    */   JDBC4NClob(ExceptionInterceptor exceptionInterceptor)
/*    */   {
/* 38 */     super(exceptionInterceptor);
/*    */   }
/*    */   
/*    */   JDBC4NClob(String charDataInit, ExceptionInterceptor exceptionInterceptor) {
/* 42 */     super(charDataInit, exceptionInterceptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4NClob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */