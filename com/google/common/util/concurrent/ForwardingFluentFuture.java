/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ final class ForwardingFluentFuture<V>
/*    */   extends FluentFuture<V>
/*    */ {
/*    */   private final ListenableFuture<V> delegate;
/*    */   
/*    */   ForwardingFluentFuture(ListenableFuture<V> delegate)
/*    */   {
/* 40 */     this.delegate = ((ListenableFuture)Preconditions.checkNotNull(delegate));
/*    */   }
/*    */   
/*    */   public void addListener(Runnable listener, Executor executor)
/*    */   {
/* 45 */     this.delegate.addListener(listener, executor);
/*    */   }
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning)
/*    */   {
/* 50 */     return this.delegate.cancel(mayInterruptIfRunning);
/*    */   }
/*    */   
/*    */   public boolean isCancelled()
/*    */   {
/* 55 */     return this.delegate.isCancelled();
/*    */   }
/*    */   
/*    */   public boolean isDone()
/*    */   {
/* 60 */     return this.delegate.isDone();
/*    */   }
/*    */   
/*    */   public V get() throws InterruptedException, ExecutionException
/*    */   {
/* 65 */     return (V)this.delegate.get();
/*    */   }
/*    */   
/*    */   public V get(long timeout, TimeUnit unit)
/*    */     throws InterruptedException, ExecutionException, TimeoutException
/*    */   {
/* 71 */     return (V)this.delegate.get(timeout, unit);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ForwardingFluentFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */