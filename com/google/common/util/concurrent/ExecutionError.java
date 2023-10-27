/*    */ package com.google.common.util.concurrent;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ public class ExecutionError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected ExecutionError() {}
/*    */   
/*    */   protected ExecutionError(String message)
/*    */   {
/* 37 */     super(message);
/*    */   }
/*    */   
/*    */   public ExecutionError(String message, Error cause)
/*    */   {
/* 42 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ExecutionError(Error cause)
/*    */   {
/* 47 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ExecutionError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */