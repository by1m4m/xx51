/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ public class SequentialBalanceStrategy
/*     */   implements BalanceStrategy
/*     */ {
/*  39 */   private int currentHostIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   public void destroy() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void init(Connection conn, Properties props)
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */ 
/*     */   public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries)
/*     */     throws SQLException
/*     */   {
/*  55 */     int numHosts = configuredHosts.size();
/*     */     
/*  57 */     SQLException ex = null;
/*     */     
/*  59 */     Map<String, Long> blackList = proxy.getGlobalBlacklist();
/*     */     
/*  61 */     int attempts = 0; ConnectionImpl conn; for (;;) { if (attempts >= numRetries) break label417;
/*  62 */       if (numHosts == 1) {
/*  63 */         this.currentHostIndex = 0;
/*  64 */       } else if (this.currentHostIndex == -1) {
/*  65 */         int random = (int)Math.floor(Math.random() * numHosts);
/*     */         
/*  67 */         for (int i = random; i < numHosts; i++) {
/*  68 */           if (!blackList.containsKey(configuredHosts.get(i))) {
/*  69 */             this.currentHostIndex = i;
/*  70 */             break;
/*     */           }
/*     */         }
/*     */         
/*  74 */         if (this.currentHostIndex == -1) {
/*  75 */           for (int i = 0; i < random; i++) {
/*  76 */             if (!blackList.containsKey(configuredHosts.get(i))) {
/*  77 */               this.currentHostIndex = i;
/*  78 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  83 */         if (this.currentHostIndex == -1) {
/*  84 */           blackList = proxy.getGlobalBlacklist();
/*     */           
/*     */           try
/*     */           {
/*  88 */             Thread.sleep(250L);
/*     */           }
/*     */           catch (InterruptedException e) {}
/*     */           
/*  92 */           continue;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  97 */         int i = this.currentHostIndex + 1;
/*  98 */         boolean foundGoodHost = false;
/* 100 */         for (; 
/* 100 */             i < numHosts; i++) {
/* 101 */           if (!blackList.containsKey(configuredHosts.get(i))) {
/* 102 */             this.currentHostIndex = i;
/* 103 */             foundGoodHost = true;
/* 104 */             break;
/*     */           }
/*     */         }
/*     */         
/* 108 */         if (!foundGoodHost) {
/* 109 */           for (i = 0; i < this.currentHostIndex; i++) {
/* 110 */             if (!blackList.containsKey(configuredHosts.get(i))) {
/* 111 */               this.currentHostIndex = i;
/* 112 */               foundGoodHost = true;
/* 113 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 118 */         if (!foundGoodHost) {
/* 119 */           blackList = proxy.getGlobalBlacklist();
/*     */           
/*     */           try
/*     */           {
/* 123 */             Thread.sleep(250L);
/*     */           }
/*     */           catch (InterruptedException e) {}
/*     */           
/* 127 */           continue;
/*     */         }
/*     */       }
/*     */       
/* 131 */       String hostPortSpec = (String)configuredHosts.get(this.currentHostIndex);
/*     */       
/* 133 */       conn = (ConnectionImpl)liveConnections.get(hostPortSpec);
/*     */       
/* 135 */       if (conn == null)
/*     */         try {
/* 137 */           conn = proxy.createConnectionForHost(hostPortSpec);
/*     */         } catch (SQLException sqlEx) {
/* 139 */           ex = sqlEx;
/*     */           
/* 141 */           if (((sqlEx instanceof CommunicationsException)) || ("08S01".equals(sqlEx.getSQLState())))
/*     */           {
/*     */ 
/* 144 */             proxy.addToGlobalBlacklist(hostPortSpec);
/*     */             try
/*     */             {
/* 147 */               Thread.sleep(250L);
/*     */             }
/*     */             catch (InterruptedException e) {}
/*     */           }
/*     */           else
/*     */           {
/* 153 */             throw sqlEx;
/*     */           }
/*     */         }
/*     */     }
/* 157 */     return conn;
/*     */     
/*     */     label417:
/* 160 */     if (ex != null) {
/* 161 */       throw ex;
/*     */     }
/*     */     
/* 164 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\SequentialBalanceStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */