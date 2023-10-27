/*     */ package com.mysql.jdbc.jmx;
/*     */ 
/*     */ import com.mysql.jdbc.ReplicationConnectionGroup;
/*     */ import com.mysql.jdbc.ReplicationConnectionGroupManager;
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
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
/*     */ public class ReplicationGroupManager
/*     */   implements ReplicationGroupManagerMBean
/*     */ {
/*  35 */   private boolean isJmxRegistered = false;
/*     */   
/*     */   public synchronized void registerJmx() throws SQLException {
/*  38 */     if (this.isJmxRegistered) {
/*  39 */       return;
/*     */     }
/*  41 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/*     */     try {
/*  43 */       ObjectName name = new ObjectName("com.mysql.jdbc.jmx:type=ReplicationGroupManager");
/*  44 */       mbs.registerMBean(this, name);
/*  45 */       this.isJmxRegistered = true;
/*     */     } catch (Exception e) {
/*  47 */       throw SQLError.createSQLException("Unable to register replication host management bean with JMX", null, e, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addSlaveHost(String groupFilter, String host) throws SQLException
/*     */   {
/*  53 */     ReplicationConnectionGroupManager.addSlaveHost(groupFilter, host);
/*     */   }
/*     */   
/*     */   public void removeSlaveHost(String groupFilter, String host) throws SQLException {
/*  57 */     ReplicationConnectionGroupManager.removeSlaveHost(groupFilter, host);
/*     */   }
/*     */   
/*     */   public void promoteSlaveToMaster(String groupFilter, String host) throws SQLException {
/*  61 */     ReplicationConnectionGroupManager.promoteSlaveToMaster(groupFilter, host);
/*     */   }
/*     */   
/*     */   public void removeMasterHost(String groupFilter, String host)
/*     */     throws SQLException
/*     */   {
/*  67 */     ReplicationConnectionGroupManager.removeMasterHost(groupFilter, host);
/*     */   }
/*     */   
/*     */   public String getMasterHostsList(String group)
/*     */   {
/*  72 */     StringBuffer sb = new StringBuffer("");
/*  73 */     boolean found = false;
/*  74 */     for (String host : ReplicationConnectionGroupManager.getMasterHosts(group)) {
/*  75 */       if (found) {
/*  76 */         sb.append(",");
/*     */       }
/*  78 */       found = true;
/*  79 */       sb.append(host);
/*     */     }
/*  81 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getSlaveHostsList(String group) {
/*  85 */     StringBuffer sb = new StringBuffer("");
/*  86 */     boolean found = false;
/*  87 */     for (String host : ReplicationConnectionGroupManager.getSlaveHosts(group)) {
/*  88 */       if (found) {
/*  89 */         sb.append(",");
/*     */       }
/*  91 */       found = true;
/*  92 */       sb.append(host);
/*     */     }
/*  94 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getRegisteredConnectionGroups()
/*     */   {
/*  99 */     StringBuffer sb = new StringBuffer("");
/* 100 */     boolean found = false;
/* 101 */     for (ReplicationConnectionGroup group : ReplicationConnectionGroupManager.getGroupsMatching(null)) {
/* 102 */       if (found) {
/* 103 */         sb.append(",");
/*     */       }
/* 105 */       found = true;
/* 106 */       sb.append(group.getGroupName());
/*     */     }
/* 108 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int getActiveMasterHostCount(String group) {
/* 112 */     return ReplicationConnectionGroupManager.getMasterHosts(group).size();
/*     */   }
/*     */   
/*     */   public int getActiveSlaveHostCount(String group) {
/* 116 */     return ReplicationConnectionGroupManager.getSlaveHosts(group).size();
/*     */   }
/*     */   
/*     */   public int getSlavePromotionCount(String group)
/*     */   {
/* 121 */     return ReplicationConnectionGroupManager.getNumberOfMasterPromotion(group);
/*     */   }
/*     */   
/*     */   public long getTotalLogicalConnectionCount(String group) {
/* 125 */     return ReplicationConnectionGroupManager.getTotalConnectionCount(group);
/*     */   }
/*     */   
/*     */   public long getActiveLogicalConnectionCount(String group) {
/* 129 */     return ReplicationConnectionGroupManager.getActiveConnectionCount(group);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\jmx\ReplicationGroupManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */