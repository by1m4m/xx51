/*    */ package com.google.api.client.util.store;
/*    */ 
/*    */ import com.google.api.client.util.Maps;
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public abstract class AbstractDataStoreFactory
/*    */   implements DataStoreFactory
/*    */ {
/* 36 */   private final Lock lock = new ReentrantLock();
/*    */   
/*    */ 
/* 39 */   private final Map<String, DataStore<? extends Serializable>> dataStoreMap = Maps.newHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   private static final Pattern ID_PATTERN = Pattern.compile("\\w{1,30}");
/*    */   
/*    */   public final <V extends Serializable> DataStore<V> getDataStore(String id) throws IOException {
/* 48 */     Preconditions.checkArgument(ID_PATTERN.matcher(id).matches(), "%s does not match pattern %s", new Object[] { id, ID_PATTERN });
/*    */     
/* 50 */     this.lock.lock();
/*    */     try
/*    */     {
/* 53 */       DataStore<V> dataStore = (DataStore)this.dataStoreMap.get(id);
/* 54 */       if (dataStore == null) {
/* 55 */         dataStore = createDataStore(id);
/* 56 */         this.dataStoreMap.put(id, dataStore);
/*    */       }
/* 58 */       return dataStore;
/*    */     } finally {
/* 60 */       this.lock.unlock();
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract <V extends Serializable> DataStore<V> createDataStore(String paramString)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\AbstractDataStoreFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */