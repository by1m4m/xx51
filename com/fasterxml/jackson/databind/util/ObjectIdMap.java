/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectIdMap
/*    */   extends IdentityHashMap<Object, Object>
/*    */ {
/*    */   public ObjectIdMap()
/*    */   {
/* 14 */     super(16);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object findId(Object pojo)
/*    */   {
/* 31 */     return get(pojo);
/*    */   }
/*    */   
/*    */   public void insertId(Object pojo, Object id)
/*    */   {
/* 36 */     put(pojo, id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\ObjectIdMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */