/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ public class VerifyException
/*    */   extends RuntimeException
/*    */ {
/*    */   public VerifyException() {}
/*    */   
/*    */   public VerifyException(String message)
/*    */   {
/* 34 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public VerifyException(Throwable cause)
/*    */   {
/* 44 */     super(cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public VerifyException(String message, Throwable cause)
/*    */   {
/* 54 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\VerifyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */