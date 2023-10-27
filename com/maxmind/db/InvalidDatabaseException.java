/*    */ package com.maxmind.db;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidDatabaseException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 6161763462364823003L;
/*    */   
/*    */   public InvalidDatabaseException(String message)
/*    */   {
/* 18 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public InvalidDatabaseException(String message, Throwable cause)
/*    */   {
/* 26 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\InvalidDatabaseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */