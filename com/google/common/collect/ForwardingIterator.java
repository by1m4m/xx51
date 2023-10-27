/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingIterator<T>
/*    */   extends ForwardingObject
/*    */   implements Iterator<T>
/*    */ {
/*    */   protected abstract Iterator<T> delegate();
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 49 */     return delegate().hasNext();
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public T next()
/*    */   {
/* 55 */     return (T)delegate().next();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 60 */     delegate().remove();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ForwardingIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */