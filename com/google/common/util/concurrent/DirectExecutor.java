/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.concurrent.Executor;
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
/*    */  enum DirectExecutor
/*    */   implements Executor
/*    */ {
/* 26 */   INSTANCE;
/*    */   
/*    */   private DirectExecutor() {}
/*    */   
/* 30 */   public void execute(Runnable command) { command.run(); }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 35 */     return "MoreExecutors.directExecutor()";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\DirectExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */