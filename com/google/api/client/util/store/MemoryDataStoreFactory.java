/*    */ package com.google.api.client.util.store;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
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
/*    */ public class MemoryDataStoreFactory
/*    */   extends AbstractDataStoreFactory
/*    */ {
/*    */   protected <V extends Serializable> DataStore<V> createDataStore(String id)
/*    */     throws IOException
/*    */   {
/* 36 */     return new MemoryDataStore(this, id);
/*    */   }
/*    */   
/*    */   public static MemoryDataStoreFactory getDefaultInstance()
/*    */   {
/* 41 */     return InstanceHolder.INSTANCE;
/*    */   }
/*    */   
/*    */   static class InstanceHolder
/*    */   {
/* 46 */     static final MemoryDataStoreFactory INSTANCE = new MemoryDataStoreFactory();
/*    */   }
/*    */   
/*    */   static class MemoryDataStore<V extends Serializable> extends AbstractMemoryDataStore<V>
/*    */   {
/*    */     MemoryDataStore(MemoryDataStoreFactory dataStore, String id) {
/* 52 */       super(id);
/*    */     }
/*    */     
/*    */     public MemoryDataStoreFactory getDataStoreFactory()
/*    */     {
/* 57 */       return (MemoryDataStoreFactory)super.getDataStoreFactory();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\MemoryDataStoreFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */