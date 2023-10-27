/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(T value)
/*     */   {
/*  38 */     new Callable()
/*     */     {
/*     */       public T call() {
/*  41 */         return (T)this.val$value;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, ListeningExecutorService listeningExecutorService)
/*     */   {
/*  58 */     Preconditions.checkNotNull(callable);
/*  59 */     Preconditions.checkNotNull(listeningExecutorService);
/*  60 */     new AsyncCallable()
/*     */     {
/*     */       public ListenableFuture<T> call() throws Exception {
/*  63 */         return this.val$listeningExecutorService.submit(callable);
/*     */       }
/*     */     };
/*     */   }
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
/*     */   @GwtIncompatible
/*     */   static <T> Callable<T> threadRenaming(final Callable<T> callable, Supplier<String> nameSupplier)
/*     */   {
/*  80 */     Preconditions.checkNotNull(nameSupplier);
/*  81 */     Preconditions.checkNotNull(callable);
/*  82 */     new Callable()
/*     */     {
/*     */       public T call() throws Exception {
/*  85 */         Thread currentThread = Thread.currentThread();
/*  86 */         String oldName = currentThread.getName();
/*  87 */         boolean restoreName = Callables.trySetName((String)this.val$nameSupplier.get(), currentThread);
/*     */         try { boolean bool1;
/*  89 */           return (T)callable.call();
/*     */         } finally {
/*  91 */           if (restoreName) {
/*  92 */             boolean bool2 = Callables.trySetName(oldName, currentThread);
/*     */           }
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   static Runnable threadRenaming(final Runnable task, Supplier<String> nameSupplier)
/*     */   {
/* 110 */     Preconditions.checkNotNull(nameSupplier);
/* 111 */     Preconditions.checkNotNull(task);
/* 112 */     new Runnable()
/*     */     {
/*     */       public void run() {
/* 115 */         Thread currentThread = Thread.currentThread();
/* 116 */         String oldName = currentThread.getName();
/* 117 */         boolean restoreName = Callables.trySetName((String)this.val$nameSupplier.get(), currentThread);
/*     */         try {
/* 119 */           task.run();
/*     */         } finally { boolean bool1;
/* 121 */           if (restoreName) {
/* 122 */             boolean bool2 = Callables.trySetName(oldName, currentThread);
/*     */           }
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   private static boolean trySetName(String threadName, Thread currentThread)
/*     */   {
/*     */     try
/*     */     {
/* 136 */       currentThread.setName(threadName);
/* 137 */       return true;
/*     */     } catch (SecurityException e) {}
/* 139 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\Callables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */