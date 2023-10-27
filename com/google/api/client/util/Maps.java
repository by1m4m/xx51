/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.TreeMap;
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
/*    */ public final class Maps
/*    */ {
/*    */   public static <K, V> HashMap<K, V> newHashMap()
/*    */   {
/* 37 */     return new HashMap();
/*    */   }
/*    */   
/*    */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap()
/*    */   {
/* 42 */     return new LinkedHashMap();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <K extends Comparable<?>, V> TreeMap<K, V> newTreeMap()
/*    */   {
/* 50 */     return new TreeMap();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Maps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */