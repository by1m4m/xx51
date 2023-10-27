/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.concurrent.RejectedExecutionException;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.LockSupport;
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
/*    */ public final class RejectedExecutionHandlers
/*    */ {
/* 28 */   private static final RejectedExecutionHandler REJECT = new RejectedExecutionHandler()
/*    */   {
/*    */     public void rejected(Runnable task, SingleThreadEventExecutor executor) {
/* 31 */       throw new RejectedExecutionException();
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RejectedExecutionHandler reject()
/*    */   {
/* 41 */     return REJECT;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RejectedExecutionHandler backoff(int retries, long backoffAmount, TimeUnit unit)
/*    */   {
/* 50 */     ObjectUtil.checkPositive(retries, "retries");
/* 51 */     final long backOffNanos = unit.toNanos(backoffAmount);
/* 52 */     new RejectedExecutionHandler()
/*    */     {
/*    */       public void rejected(Runnable task, SingleThreadEventExecutor executor) {
/* 55 */         if (!executor.inEventLoop()) {
/* 56 */           for (int i = 0; i < this.val$retries; i++)
/*    */           {
/* 58 */             executor.wakeup(false);
/*    */             
/* 60 */             LockSupport.parkNanos(backOffNanos);
/* 61 */             if (executor.offerTask(task)) {
/* 62 */               return;
/*    */             }
/*    */           }
/*    */         }
/*    */         
/*    */ 
/* 68 */         throw new RejectedExecutionException();
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\RejectedExecutionHandlers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */