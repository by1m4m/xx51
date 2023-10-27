/*     */ package com.google.api.client.util.store;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDataStore<V extends Serializable>
/*     */   implements DataStore<V>
/*     */ {
/*     */   private final DataStoreFactory dataStoreFactory;
/*     */   private final String id;
/*     */   
/*     */   protected AbstractDataStore(DataStoreFactory dataStoreFactory, String id)
/*     */   {
/*  45 */     this.dataStoreFactory = ((DataStoreFactory)Preconditions.checkNotNull(dataStoreFactory));
/*  46 */     this.id = ((String)Preconditions.checkNotNull(id));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataStoreFactory getDataStoreFactory()
/*     */   {
/*  58 */     return this.dataStoreFactory;
/*     */   }
/*     */   
/*     */   public final String getId() {
/*  62 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsKey(String key)
/*     */     throws IOException
/*     */   {
/*  73 */     return get(key) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsValue(V value)
/*     */     throws IOException
/*     */   {
/*  84 */     return values().contains(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */     throws IOException
/*     */   {
/*  95 */     return size() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */     throws IOException
/*     */   {
/* 106 */     return keySet().size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\AbstractDataStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */