/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.TreeSet;
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
/*    */ public final class Sets
/*    */ {
/*    */   public static <E> HashSet<E> newHashSet()
/*    */   {
/* 36 */     return new HashSet();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <E extends Comparable<?>> TreeSet<E> newTreeSet()
/*    */   {
/* 44 */     return new TreeSet();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Sets.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */