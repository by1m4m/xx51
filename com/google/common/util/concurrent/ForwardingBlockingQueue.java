/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.collect.ForwardingQueue;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ @CanIgnoreReturnValue
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingBlockingQueue<E>
/*    */   extends ForwardingQueue<E>
/*    */   implements BlockingQueue<E>
/*    */ {
/*    */   protected abstract BlockingQueue<E> delegate();
/*    */   
/*    */   public int drainTo(Collection<? super E> c, int maxElements)
/*    */   {
/* 51 */     return delegate().drainTo(c, maxElements);
/*    */   }
/*    */   
/*    */   public int drainTo(Collection<? super E> c)
/*    */   {
/* 56 */     return delegate().drainTo(c);
/*    */   }
/*    */   
/*    */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException
/*    */   {
/* 61 */     return delegate().offer(e, timeout, unit);
/*    */   }
/*    */   
/*    */   public E poll(long timeout, TimeUnit unit) throws InterruptedException
/*    */   {
/* 66 */     return (E)delegate().poll(timeout, unit);
/*    */   }
/*    */   
/*    */   public void put(E e) throws InterruptedException
/*    */   {
/* 71 */     delegate().put(e);
/*    */   }
/*    */   
/*    */   public int remainingCapacity()
/*    */   {
/* 76 */     return delegate().remainingCapacity();
/*    */   }
/*    */   
/*    */   public E take() throws InterruptedException
/*    */   {
/* 81 */     return (E)delegate().take();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ForwardingBlockingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */