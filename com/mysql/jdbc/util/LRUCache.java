/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map.Entry;
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
/*    */ public class LRUCache
/*    */   extends LinkedHashMap<Object, Object>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int maxElements;
/*    */   
/*    */   public LRUCache(int maxSize)
/*    */   {
/* 40 */     super(maxSize, 0.75F, true);
/* 41 */     this.maxElements = maxSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest)
/*    */   {
/* 51 */     return size() > this.maxElements;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\util\LRUCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */