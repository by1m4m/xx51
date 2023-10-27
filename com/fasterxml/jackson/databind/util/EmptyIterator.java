/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmptyIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/* 14 */   private static final EmptyIterator<?> instance = new EmptyIterator();
/*    */   
/*    */ 
/* 17 */   public static <T> Iterator<T> instance() { return instance; }
/*    */   
/* 19 */   public boolean hasNext() { return false; }
/* 20 */   public T next() { throw new NoSuchElementException(); }
/* 21 */   public void remove() { throw new UnsupportedOperationException(); }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\EmptyIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */