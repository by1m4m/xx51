/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LinkedNode<T>
/*    */ {
/*    */   private final T value;
/*    */   
/*    */ 
/*    */ 
/*    */   private LinkedNode<T> next;
/*    */   
/*    */ 
/*    */ 
/*    */   public LinkedNode(T value, LinkedNode<T> next)
/*    */   {
/* 18 */     this.value = value;
/* 19 */     this.next = next;
/*    */   }
/*    */   
/*    */   public void linkNext(LinkedNode<T> n)
/*    */   {
/* 24 */     if (this.next != null) {
/* 25 */       throw new IllegalStateException();
/*    */     }
/* 27 */     this.next = n;
/*    */   }
/*    */   
/* 30 */   public LinkedNode<T> next() { return this.next; }
/*    */   
/* 32 */   public T value() { return (T)this.value; }
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
/*    */   public static <ST> boolean contains(LinkedNode<ST> node, ST value)
/*    */   {
/* 46 */     while (node != null) {
/* 47 */       if (node.value() == value) {
/* 48 */         return true;
/*    */       }
/* 50 */       node = node.next();
/*    */     }
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\LinkedNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */