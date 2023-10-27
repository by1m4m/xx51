/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class ReplicationConnectionGroup
/*     */ {
/*     */   private String groupName;
/*  43 */   private long connections = 0L;
/*  44 */   private long slavesAdded = 0L;
/*  45 */   private long slavesRemoved = 0L;
/*  46 */   private long slavesPromoted = 0L;
/*  47 */   private long activeConnections = 0L;
/*  48 */   private HashMap<Long, ReplicationConnection> replicationConnections = new HashMap();
/*  49 */   private Set<String> slaveHostList = new HashSet();
/*  50 */   private boolean isInitialized = false;
/*  51 */   private Set<String> masterHostList = new HashSet();
/*     */   
/*     */   ReplicationConnectionGroup(String groupName) {
/*  54 */     this.groupName = groupName;
/*     */   }
/*     */   
/*     */   public long getConnectionCount() {
/*  58 */     return this.connections;
/*     */   }
/*     */   
/*     */ 
/*     */   public long registerReplicationConnection(ReplicationConnection conn, List<String> localMasterList, List<String> localSlaveList)
/*     */   {
/*     */     long currentConnectionId;
/*  65 */     synchronized (this) {
/*  66 */       if (!this.isInitialized) {
/*  67 */         if (localMasterList != null) {
/*  68 */           this.masterHostList.addAll(localMasterList);
/*     */         }
/*  70 */         if (localSlaveList != null) {
/*  71 */           this.slaveHostList.addAll(localSlaveList);
/*     */         }
/*  73 */         this.isInitialized = true;
/*     */       }
/*  75 */       currentConnectionId = ++this.connections;
/*  76 */       this.replicationConnections.put(Long.valueOf(currentConnectionId), conn);
/*     */     }
/*     */     
/*  79 */     this.activeConnections += 1L;
/*     */     
/*     */ 
/*  82 */     return currentConnectionId;
/*     */   }
/*     */   
/*     */   public String getGroupName()
/*     */   {
/*  87 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public Collection<String> getMasterHosts()
/*     */   {
/*  92 */     return this.masterHostList;
/*     */   }
/*     */   
/*     */   public Collection<String> getSlaveHosts() {
/*  96 */     return this.slaveHostList;
/*     */   }
/*     */   
/*     */   public void addSlaveHost(String host) throws SQLException
/*     */   {
/* 101 */     if (this.slaveHostList.add(host)) {
/* 102 */       this.slavesAdded += 1L;
/*     */     }
/*     */     
/* 105 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 106 */       c.addSlaveHost(host);
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleCloseConnection(ReplicationConnection conn)
/*     */   {
/* 112 */     this.replicationConnections.remove(Long.valueOf(conn.getConnectionGroupId()));
/* 113 */     this.activeConnections -= 1L;
/*     */   }
/*     */   
/*     */   public void removeSlaveHost(String host, boolean closeGently) throws SQLException {
/* 117 */     if (this.slaveHostList.remove(host)) {
/* 118 */       this.slavesRemoved += 1L;
/*     */     }
/* 120 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 121 */       c.removeSlave(host, closeGently);
/*     */     }
/*     */   }
/*     */   
/*     */   public void promoteSlaveToMaster(String host) throws SQLException {
/* 126 */     this.slaveHostList.remove(host);
/* 127 */     this.masterHostList.add(host);
/* 128 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 129 */       c.promoteSlaveToMaster(host);
/*     */     }
/*     */     
/* 132 */     this.slavesPromoted += 1L;
/*     */   }
/*     */   
/*     */   public void removeMasterHost(String host) throws SQLException {
/* 136 */     removeMasterHost(host, true);
/*     */   }
/*     */   
/*     */   public void removeMasterHost(String host, boolean closeGently) throws SQLException {
/* 140 */     if (this.masterHostList.remove(host)) {}
/*     */     
/*     */ 
/* 143 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 144 */       c.removeMasterHost(host, closeGently);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getConnectionCountWithHostAsSlave(String host)
/*     */   {
/* 150 */     int matched = 0;
/*     */     
/* 152 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 153 */       if (c.isHostSlave(host)) {
/* 154 */         matched++;
/*     */       }
/*     */     }
/* 157 */     return matched;
/*     */   }
/*     */   
/* 160 */   public int getConnectionCountWithHostAsMaster(String host) { int matched = 0;
/*     */     
/* 162 */     for (ReplicationConnection c : this.replicationConnections.values()) {
/* 163 */       if (c.isHostMaster(host)) {
/* 164 */         matched++;
/*     */       }
/*     */     }
/* 167 */     return matched;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getNumberOfSlavesAdded()
/*     */   {
/* 173 */     return this.slavesAdded;
/*     */   }
/*     */   
/*     */   public long getNumberOfSlavesRemoved() {
/* 177 */     return this.slavesRemoved;
/*     */   }
/*     */   
/*     */   public long getNumberOfSlavePromotions() {
/* 181 */     return this.slavesPromoted;
/*     */   }
/*     */   
/*     */   public long getTotalConnectionCount() {
/* 185 */     return this.connections;
/*     */   }
/*     */   
/*     */   public long getActiveConnectionCount() {
/* 189 */     return this.activeConnections;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ReplicationConnectionGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */