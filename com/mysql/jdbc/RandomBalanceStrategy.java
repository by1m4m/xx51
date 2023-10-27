/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public class RandomBalanceStrategy
/*     */   implements BalanceStrategy
/*     */ {
/*     */   public void destroy() {}
/*     */   
/*     */   public void init(Connection conn, Properties props)
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */   public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries)
/*     */     throws SQLException
/*     */   {
/*  48 */     int numHosts = configuredHosts.size();
/*     */     
/*  50 */     SQLException ex = null;
/*     */     
/*  52 */     List<String> whiteList = new ArrayList(numHosts);
/*  53 */     whiteList.addAll(configuredHosts);
/*     */     
/*  55 */     Map<String, Long> blackList = proxy.getGlobalBlacklist();
/*     */     
/*  57 */     whiteList.removeAll(blackList.keySet());
/*     */     
/*  59 */     Map<String, Integer> whiteListMap = getArrayIndexMap(whiteList);
/*     */     
/*     */ 
/*  62 */     int attempts = 0; ConnectionImpl conn; for (;;) { if (attempts >= numRetries) break label291;
/*  63 */       int random = (int)Math.floor(Math.random() * whiteList.size());
/*  64 */       if (whiteList.size() == 0) {
/*  65 */         throw SQLError.createSQLException("No hosts configured", null);
/*     */       }
/*     */       
/*  68 */       String hostPortSpec = (String)whiteList.get(random);
/*     */       
/*  70 */       conn = (ConnectionImpl)liveConnections.get(hostPortSpec);
/*     */       
/*  72 */       if (conn == null)
/*     */         try {
/*  74 */           conn = proxy.createConnectionForHost(hostPortSpec);
/*     */         } catch (SQLException sqlEx) {
/*  76 */           ex = sqlEx;
/*     */           
/*  78 */           if (proxy.shouldExceptionTriggerFailover(sqlEx))
/*     */           {
/*  80 */             Integer whiteListIndex = (Integer)whiteListMap.get(hostPortSpec);
/*     */             
/*     */ 
/*  83 */             if (whiteListIndex != null) {
/*  84 */               whiteList.remove(whiteListIndex.intValue());
/*  85 */               whiteListMap = getArrayIndexMap(whiteList);
/*     */             }
/*  87 */             proxy.addToGlobalBlacklist(hostPortSpec);
/*     */             
/*  89 */             if (whiteList.size() == 0) {
/*  90 */               attempts++;
/*     */               try {
/*  92 */                 Thread.sleep(250L);
/*     */               }
/*     */               catch (InterruptedException e) {}
/*     */               
/*     */ 
/*  97 */               whiteListMap = new HashMap(numHosts);
/*  98 */               whiteList.addAll(configuredHosts);
/*  99 */               blackList = proxy.getGlobalBlacklist();
/*     */               
/* 101 */               whiteList.removeAll(blackList.keySet());
/* 102 */               whiteListMap = getArrayIndexMap(whiteList);
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 108 */             throw sqlEx;
/*     */           }
/*     */         }
/*     */     }
/* 112 */     return conn;
/*     */     
/*     */     label291:
/* 115 */     if (ex != null) {
/* 116 */       throw ex;
/*     */     }
/*     */     
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   private Map<String, Integer> getArrayIndexMap(List<String> l) {
/* 123 */     Map<String, Integer> m = new HashMap(l.size());
/* 124 */     for (int i = 0; i < l.size(); i++) {
/* 125 */       m.put(l.get(i), Integer.valueOf(i));
/*     */     }
/* 127 */     return m;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\RandomBalanceStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */