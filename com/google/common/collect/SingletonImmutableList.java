/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import java.util.Spliterator;
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
/*    */ @GwtCompatible(serializable=true, emulated=true)
/*    */ final class SingletonImmutableList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/*    */   final transient E element;
/*    */   
/*    */   SingletonImmutableList(E element)
/*    */   {
/* 38 */     this.element = Preconditions.checkNotNull(element);
/*    */   }
/*    */   
/*    */   public E get(int index)
/*    */   {
/* 43 */     Preconditions.checkElementIndex(index, 1);
/* 44 */     return (E)this.element;
/*    */   }
/*    */   
/*    */   public UnmodifiableIterator<E> iterator()
/*    */   {
/* 49 */     return Iterators.singletonIterator(this.element);
/*    */   }
/*    */   
/*    */   public Spliterator<E> spliterator()
/*    */   {
/* 54 */     return Collections.singleton(this.element).spliterator();
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 59 */     return 1;
/*    */   }
/*    */   
/*    */   public ImmutableList<E> subList(int fromIndex, int toIndex)
/*    */   {
/* 64 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
/* 65 */     return fromIndex == toIndex ? ImmutableList.of() : this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 70 */     return '[' + this.element.toString() + ']';
/*    */   }
/*    */   
/*    */   boolean isPartialView()
/*    */   {
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SingletonImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */