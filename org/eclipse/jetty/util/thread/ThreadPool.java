/*    */ package org.eclipse.jetty.util.thread;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*    */ import org.eclipse.jetty.util.annotation.ManagedObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ManagedObject("Pool of Threads")
/*    */ public abstract interface ThreadPool
/*    */   extends Executor
/*    */ {
/*    */   public abstract void join()
/*    */     throws InterruptedException;
/*    */   
/*    */   @ManagedAttribute("number of threads in pool")
/*    */   public abstract int getThreads();
/*    */   
/*    */   @ManagedAttribute("number of idle threads in pool")
/*    */   public abstract int getIdleThreads();
/*    */   
/*    */   @ManagedAttribute("indicates the pool is low on available threads")
/*    */   public abstract boolean isLowOnThreads();
/*    */   
/*    */   public static abstract interface SizedThreadPool
/*    */     extends ThreadPool
/*    */   {
/*    */     public abstract int getMinThreads();
/*    */     
/*    */     public abstract int getMaxThreads();
/*    */     
/*    */     public abstract void setMinThreads(int paramInt);
/*    */     
/*    */     public abstract void setMaxThreads(int paramInt);
/*    */     
/*    */     public ThreadPoolBudget getThreadPoolBudget()
/*    */     {
/* 92 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\ThreadPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */