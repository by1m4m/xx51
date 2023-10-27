/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public final class Lists
/*    */ {
/*    */   public static <E> ArrayList<E> newArrayList()
/*    */   {
/* 37 */     return new ArrayList();
/*    */   }
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
/*    */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize)
/*    */   {
/* 51 */     return new ArrayList(initialArraySize);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements)
/*    */   {
/* 61 */     return (elements instanceof Collection) ? new ArrayList(Collections2.cast(elements)) : newArrayList(elements.iterator());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements)
/*    */   {
/* 72 */     ArrayList<E> list = newArrayList();
/* 73 */     while (elements.hasNext()) {
/* 74 */       list.add(elements.next());
/*    */     }
/* 76 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Lists.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */