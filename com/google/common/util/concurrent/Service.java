/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract interface Service
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public abstract Service startAsync();
/*     */   
/*     */   public abstract boolean isRunning();
/*     */   
/*     */   public abstract State state();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public abstract Service stopAsync();
/*     */   
/*     */   public abstract void awaitRunning();
/*     */   
/*     */   public abstract void awaitRunning(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws TimeoutException;
/*     */   
/*     */   public abstract void awaitTerminated();
/*     */   
/*     */   public abstract void awaitTerminated(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws TimeoutException;
/*     */   
/*     */   public abstract Throwable failureCause();
/*     */   
/*     */   public abstract void addListener(Listener paramListener, Executor paramExecutor);
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void starting() {}
/*     */     
/*     */     public void running() {}
/*     */     
/*     */     public void stopping(Service.State from) {}
/*     */     
/*     */     public void terminated(Service.State from) {}
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {}
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract enum State
/*     */   {
/* 179 */     NEW, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */     STARTING, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     RUNNING, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */     STOPPING, 
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
/* 214 */     TERMINATED, 
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
/* 225 */     FAILED;
/*     */     
/*     */     private State() {}
/*     */     
/*     */     abstract boolean isTerminal();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */