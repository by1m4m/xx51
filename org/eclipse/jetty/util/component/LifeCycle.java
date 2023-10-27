/*     */ package org.eclipse.jetty.util.component;
/*     */ 
/*     */ import java.util.EventListener;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.annotation.ManagedOperation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ManagedObject("Lifecycle Interface for startable components")
/*     */ public abstract interface LifeCycle
/*     */ {
/*     */   @ManagedOperation(value="Starts the instance", impact="ACTION")
/*     */   public abstract void start()
/*     */     throws Exception;
/*     */   
/*     */   @ManagedOperation(value="Stops the instance", impact="ACTION")
/*     */   public abstract void stop()
/*     */     throws Exception;
/*     */   
/*     */   public abstract boolean isRunning();
/*     */   
/*     */   public abstract boolean isStarted();
/*     */   
/*     */   public abstract boolean isStarting();
/*     */   
/*     */   public abstract boolean isStopping();
/*     */   
/*     */   public abstract boolean isStopped();
/*     */   
/*     */   public abstract boolean isFailed();
/*     */   
/*     */   public abstract void addLifeCycleListener(Listener paramListener);
/*     */   
/*     */   public abstract void removeLifeCycleListener(Listener paramListener);
/*     */   
/*     */   public static void start(Object object)
/*     */   {
/* 135 */     if ((object instanceof LifeCycle))
/*     */     {
/*     */       try
/*     */       {
/* 139 */         ((LifeCycle)object).start();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 143 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void stop(Object object)
/*     */   {
/* 156 */     if ((object instanceof LifeCycle))
/*     */     {
/*     */       try
/*     */       {
/* 160 */         ((LifeCycle)object).stop();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 164 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface Listener
/*     */     extends EventListener
/*     */   {
/*     */     public abstract void lifeCycleStarting(LifeCycle paramLifeCycle);
/*     */     
/*     */     public abstract void lifeCycleStarted(LifeCycle paramLifeCycle);
/*     */     
/*     */     public abstract void lifeCycleFailure(LifeCycle paramLifeCycle, Throwable paramThrowable);
/*     */     
/*     */     public abstract void lifeCycleStopping(LifeCycle paramLifeCycle);
/*     */     
/*     */     public abstract void lifeCycleStopped(LifeCycle paramLifeCycle);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\LifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */