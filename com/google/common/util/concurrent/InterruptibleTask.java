/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import com.google.j2objc.annotations.ReflectionSupport.Level;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class InterruptibleTask<T>
/*     */   extends AtomicReference<Runnable>
/*     */   implements Runnable
/*     */ {
/*  36 */   private static final Runnable DONE = new DoNothingRunnable(null);
/*  37 */   private static final Runnable INTERRUPTING = new DoNothingRunnable(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void run()
/*     */   {
/*  47 */     Thread currentThread = Thread.currentThread();
/*  48 */     if (!compareAndSet(null, currentThread)) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     boolean run = !isDone();
/*  53 */     T result = null;
/*  54 */     Throwable error = null;
/*     */     try {
/*  56 */       if (run) {
/*  57 */         result = runInterruptibly();
/*     */       }
/*     */     } catch (Throwable t) {
/*  60 */       error = t;
/*     */     }
/*     */     finally {
/*  63 */       if (!compareAndSet(currentThread, DONE))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */         while (get() == INTERRUPTING) {
/*  71 */           Thread.yield();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */       if (run) {
/*  80 */         afterRanInterruptibly(result, error);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract boolean isDone();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract T runInterruptibly()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract void afterRanInterruptibly(T paramT, Throwable paramThrowable);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final void interruptTask()
/*     */   {
/* 107 */     Runnable currentRunner = (Runnable)get();
/* 108 */     if (((currentRunner instanceof Thread)) && (compareAndSet(currentRunner, INTERRUPTING))) {
/* 109 */       ((Thread)currentRunner).interrupt();
/* 110 */       set(DONE);
/*     */     }
/*     */   }
/*     */   
/*     */   public final String toString()
/*     */   {
/* 116 */     Runnable state = (Runnable)get();
/*     */     String result;
/* 118 */     String result; if (state == DONE) {
/* 119 */       result = "running=[DONE]"; } else { String result;
/* 120 */       if (state == INTERRUPTING) {
/* 121 */         result = "running=[INTERRUPTED]"; } else { String result;
/* 122 */         if ((state instanceof Thread))
/*     */         {
/* 124 */           result = "running=[RUNNING ON " + ((Thread)state).getName() + "]";
/*     */         } else
/* 126 */           result = "running=[NOT STARTED YET]";
/*     */       } }
/* 128 */     return result + ", " + toPendingString();
/*     */   }
/*     */   
/*     */   abstract String toPendingString();
/*     */   
/*     */   private static final class DoNothingRunnable
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\InterruptibleTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */