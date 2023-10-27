/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import com.mysql.jdbc.util.LRUCache;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
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
/*    */ public class PerConnectionLRUFactory
/*    */   implements CacheAdapterFactory<String, PreparedStatement.ParseInfo>
/*    */ {
/*    */   public CacheAdapter<String, PreparedStatement.ParseInfo> getInstance(Connection forConnection, String url, int cacheMaxSize, int maxKeySize, Properties connectionProperties)
/*    */     throws SQLException
/*    */   {
/* 39 */     return new PerConnectionLRU(forConnection, cacheMaxSize, maxKeySize);
/*    */   }
/*    */   
/*    */   class PerConnectionLRU implements CacheAdapter<String, PreparedStatement.ParseInfo>
/*    */   {
/*    */     private final int cacheSqlLimit;
/*    */     private final LRUCache cache;
/*    */     private final Connection conn;
/*    */     
/*    */     protected PerConnectionLRU(Connection forConnection, int cacheMaxSize, int maxKeySize) {
/* 49 */       int cacheSize = cacheMaxSize;
/* 50 */       this.cacheSqlLimit = maxKeySize;
/* 51 */       this.cache = new LRUCache(cacheSize);
/* 52 */       this.conn = forConnection;
/*    */     }
/*    */     
/*    */     /* Error */
/*    */     public PreparedStatement.ParseInfo get(String key)
/*    */     {
/*    */       // Byte code:
/*    */       //   0: aload_1
/*    */       //   1: ifnull +14 -> 15
/*    */       //   4: aload_1
/*    */       //   5: invokevirtual 8	java/lang/String:length	()I
/*    */       //   8: aload_0
/*    */       //   9: getfield 3	com/mysql/jdbc/PerConnectionLRUFactory$PerConnectionLRU:cacheSqlLimit	I
/*    */       //   12: if_icmple +5 -> 17
/*    */       //   15: aconst_null
/*    */       //   16: areturn
/*    */       //   17: aload_0
/*    */       //   18: getfield 7	com/mysql/jdbc/PerConnectionLRUFactory$PerConnectionLRU:conn	Lcom/mysql/jdbc/Connection;
/*    */       //   21: invokeinterface 9 1 0
/*    */       //   26: dup
/*    */       //   27: astore_2
/*    */       //   28: monitorenter
/*    */       //   29: aload_0
/*    */       //   30: getfield 6	com/mysql/jdbc/PerConnectionLRUFactory$PerConnectionLRU:cache	Lcom/mysql/jdbc/util/LRUCache;
/*    */       //   33: aload_1
/*    */       //   34: invokevirtual 10	com/mysql/jdbc/util/LRUCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*    */       //   37: checkcast 11	com/mysql/jdbc/PreparedStatement$ParseInfo
/*    */       //   40: aload_2
/*    */       //   41: monitorexit
/*    */       //   42: areturn
/*    */       //   43: astore_3
/*    */       //   44: aload_2
/*    */       //   45: monitorexit
/*    */       //   46: aload_3
/*    */       //   47: athrow
/*    */       // Line number table:
/*    */       //   Java source line #56	-> byte code offset #0
/*    */       //   Java source line #57	-> byte code offset #15
/*    */       //   Java source line #60	-> byte code offset #17
/*    */       //   Java source line #61	-> byte code offset #29
/*    */       //   Java source line #62	-> byte code offset #43
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	48	0	this	PerConnectionLRU
/*    */       //   0	48	1	key	String
/*    */       //   27	18	2	Ljava/lang/Object;	Object
/*    */       //   43	4	3	localObject1	Object
/*    */       // Exception table:
/*    */       //   from	to	target	type
/*    */       //   29	42	43	finally
/*    */       //   43	46	43	finally
/*    */     }
/*    */     
/*    */     public void put(String key, PreparedStatement.ParseInfo value)
/*    */     {
/* 66 */       if ((key == null) || (key.length() > this.cacheSqlLimit)) {
/* 67 */         return;
/*    */       }
/*    */       
/* 70 */       synchronized (this.conn.getConnectionMutex()) {
/* 71 */         this.cache.put(key, value);
/*    */       }
/*    */     }
/*    */     
/*    */     public void invalidate(String key) {
/* 76 */       synchronized (this.conn.getConnectionMutex()) {
/* 77 */         this.cache.remove(key);
/*    */       }
/*    */     }
/*    */     
/*    */     public void invalidateAll(Set<String> keys) {
/* 82 */       synchronized (this.conn.getConnectionMutex()) {
/* 83 */         for (String key : keys) {
/* 84 */           this.cache.remove(key);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */     public void invalidateAll()
/*    */     {
/* 91 */       synchronized (this.conn.getConnectionMutex()) {
/* 92 */         this.cache.clear();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\PerConnectionLRUFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */