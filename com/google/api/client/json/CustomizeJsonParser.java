/*    */ package com.google.api.client.json;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ @Beta
/*    */ public class CustomizeJsonParser
/*    */ {
/*    */   public boolean stopAt(Object context, String key)
/*    */   {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void handleUnrecognizedKey(Object context, String key) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Collection<Object> newInstanceForArray(Object context, Field field)
/*    */   {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object newInstanceForObject(Object context, Class<?> fieldClass)
/*    */   {
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\CustomizeJsonParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */