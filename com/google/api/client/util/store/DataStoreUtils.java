/*    */ package com.google.api.client.util.store;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public final class DataStoreUtils
/*    */ {
/*    */   public static String toString(DataStore<?> dataStore)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       StringBuilder sb = new StringBuilder();
/* 43 */       sb.append('{');
/* 44 */       boolean first = true;
/* 45 */       for (String key : dataStore.keySet()) {
/* 46 */         if (first) {
/* 47 */           first = false;
/*    */         } else {
/* 49 */           sb.append(", ");
/*    */         }
/* 51 */         sb.append(key).append('=').append(dataStore.get(key));
/*    */       }
/* 53 */       return '}';
/*    */     } catch (IOException e) {
/* 55 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\DataStoreUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */