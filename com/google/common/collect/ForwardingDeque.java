/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*     */   protected abstract Deque<E> delegate();
/*     */   
/*     */   public void addFirst(E e)
/*     */   {
/*  52 */     delegate().addFirst(e);
/*     */   }
/*     */   
/*     */   public void addLast(E e)
/*     */   {
/*  57 */     delegate().addLast(e);
/*     */   }
/*     */   
/*     */   public Iterator<E> descendingIterator()
/*     */   {
/*  62 */     return delegate().descendingIterator();
/*     */   }
/*     */   
/*     */   public E getFirst()
/*     */   {
/*  67 */     return (E)delegate().getFirst();
/*     */   }
/*     */   
/*     */   public E getLast()
/*     */   {
/*  72 */     return (E)delegate().getLast();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerFirst(E e)
/*     */   {
/*  78 */     return delegate().offerFirst(e);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerLast(E e)
/*     */   {
/*  84 */     return delegate().offerLast(e);
/*     */   }
/*     */   
/*     */   public E peekFirst()
/*     */   {
/*  89 */     return (E)delegate().peekFirst();
/*     */   }
/*     */   
/*     */   public E peekLast()
/*     */   {
/*  94 */     return (E)delegate().peekLast();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst()
/*     */   {
/* 100 */     return (E)delegate().pollFirst();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast()
/*     */   {
/* 106 */     return (E)delegate().pollLast();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pop()
/*     */   {
/* 112 */     return (E)delegate().pop();
/*     */   }
/*     */   
/*     */   public void push(E e)
/*     */   {
/* 117 */     delegate().push(e);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst()
/*     */   {
/* 123 */     return (E)delegate().removeFirst();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast()
/*     */   {
/* 129 */     return (E)delegate().removeLast();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeFirstOccurrence(Object o)
/*     */   {
/* 135 */     return delegate().removeFirstOccurrence(o);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeLastOccurrence(Object o)
/*     */   {
/* 141 */     return delegate().removeLastOccurrence(o);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ForwardingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */