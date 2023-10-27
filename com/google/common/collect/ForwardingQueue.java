/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   protected abstract Queue<E> delegate();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E o)
/*     */   {
/*  58 */     return delegate().offer(o);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll()
/*     */   {
/*  64 */     return (E)delegate().poll();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove()
/*     */   {
/*  70 */     return (E)delegate().remove();
/*     */   }
/*     */   
/*     */   public E peek()
/*     */   {
/*  75 */     return (E)delegate().peek();
/*     */   }
/*     */   
/*     */   public E element()
/*     */   {
/*  80 */     return (E)delegate().element();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean standardOffer(E e)
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return add(e);
/*     */     } catch (IllegalStateException caught) {}
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPeek()
/*     */   {
/*     */     try
/*     */     {
/* 105 */       return (E)element();
/*     */     } catch (NoSuchElementException caught) {}
/* 107 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPoll()
/*     */   {
/*     */     try
/*     */     {
/* 119 */       return (E)remove();
/*     */     } catch (NoSuchElementException caught) {}
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ForwardingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */