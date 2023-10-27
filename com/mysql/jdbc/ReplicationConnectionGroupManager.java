/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.jmx.ReplicationGroupManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class ReplicationConnectionGroupManager
/*     */ {
/*  32 */   private static HashMap<String, ReplicationConnectionGroup> GROUP_MAP = new HashMap();
/*     */   
/*  34 */   private static ReplicationGroupManager mbean = new ReplicationGroupManager();
/*     */   
/*  36 */   private static boolean hasRegisteredJmx = false;
/*     */   
/*     */   public static synchronized ReplicationConnectionGroup getConnectionGroupInstance(String groupName)
/*     */   {
/*  40 */     if (GROUP_MAP.containsKey(groupName)) {
/*  41 */       return (ReplicationConnectionGroup)GROUP_MAP.get(groupName);
/*     */     }
/*  43 */     ReplicationConnectionGroup group = new ReplicationConnectionGroup(groupName);
/*     */     
/*  45 */     GROUP_MAP.put(groupName, group);
/*  46 */     return group;
/*     */   }
/*     */   
/*     */   public static void registerJmx() throws SQLException
/*     */   {
/*  51 */     if (hasRegisteredJmx) {
/*  52 */       return;
/*     */     }
/*     */     
/*  55 */     mbean.registerJmx();
/*  56 */     hasRegisteredJmx = true;
/*     */   }
/*     */   
/*     */   public static ReplicationConnectionGroup getConnectionGroup(String groupName) {
/*  60 */     return (ReplicationConnectionGroup)GROUP_MAP.get(groupName);
/*     */   }
/*     */   
/*     */   public static Collection<ReplicationConnectionGroup> getGroupsMatching(String group)
/*     */   {
/*  65 */     if ((group == null) || (group.equals(""))) {
/*  66 */       Set<ReplicationConnectionGroup> s = new HashSet();
/*     */       
/*  68 */       s.addAll(GROUP_MAP.values());
/*  69 */       return s;
/*     */     }
/*  71 */     Set<ReplicationConnectionGroup> s = new HashSet();
/*  72 */     ReplicationConnectionGroup o = (ReplicationConnectionGroup)GROUP_MAP.get(group);
/*  73 */     if (o != null) {
/*  74 */       s.add(o);
/*     */     }
/*  76 */     return s;
/*     */   }
/*     */   
/*     */   public static void addSlaveHost(String group, String host) throws SQLException
/*     */   {
/*  81 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/*  82 */     for (ReplicationConnectionGroup cg : s) {
/*  83 */       cg.addSlaveHost(host);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void removeSlaveHost(String group, String host) throws SQLException
/*     */   {
/*  89 */     removeSlaveHost(group, host, true);
/*     */   }
/*     */   
/*     */   public static void removeSlaveHost(String group, String host, boolean closeGently) throws SQLException
/*     */   {
/*  94 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/*  95 */     for (ReplicationConnectionGroup cg : s) {
/*  96 */       cg.removeSlaveHost(host, closeGently);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void promoteSlaveToMaster(String group, String newMasterHost) throws SQLException {
/* 101 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/* 102 */     for (ReplicationConnectionGroup cg : s) {
/* 103 */       cg.promoteSlaveToMaster(newMasterHost);
/*     */     }
/*     */   }
/*     */   
/*     */   public static long getSlavePromotionCount(String group) throws SQLException
/*     */   {
/* 109 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/* 110 */     long promoted = 0L;
/* 111 */     for (ReplicationConnectionGroup cg : s) {
/* 112 */       long tmp = cg.getNumberOfSlavePromotions();
/* 113 */       if (tmp > promoted) {
/* 114 */         promoted = tmp;
/*     */       }
/*     */     }
/* 117 */     return promoted;
/*     */   }
/*     */   
/*     */   public static void removeMasterHost(String group, String host)
/*     */     throws SQLException
/*     */   {
/* 123 */     removeMasterHost(group, host, true);
/*     */   }
/*     */   
/*     */   public static void removeMasterHost(String group, String host, boolean closeGently) throws SQLException {
/* 127 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/* 128 */     for (ReplicationConnectionGroup cg : s) {
/* 129 */       cg.removeMasterHost(host, closeGently);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getRegisteredReplicationConnectionGroups() {
/* 134 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(null);
/* 135 */     StringBuffer sb = new StringBuffer();
/* 136 */     String sep = "";
/* 137 */     for (ReplicationConnectionGroup cg : s) {
/* 138 */       String group = cg.getGroupName();
/* 139 */       sb.append(sep);
/* 140 */       sb.append(group);
/* 141 */       sep = ",";
/*     */     }
/* 143 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getNumberOfMasterPromotion(String groupFilter)
/*     */   {
/* 149 */     int total = 0;
/* 150 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(groupFilter);
/* 151 */     for (ReplicationConnectionGroup cg : s) {
/* 152 */       total = (int)(total + cg.getNumberOfSlavePromotions());
/*     */     }
/* 154 */     return total;
/*     */   }
/*     */   
/*     */   public static int getConnectionCountWithHostAsSlave(String groupFilter, String host)
/*     */   {
/* 159 */     int total = 0;
/* 160 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(groupFilter);
/* 161 */     for (ReplicationConnectionGroup cg : s) {
/* 162 */       total += cg.getConnectionCountWithHostAsSlave(host);
/*     */     }
/* 164 */     return total;
/*     */   }
/*     */   
/*     */   public static int getConnectionCountWithHostAsMaster(String groupFilter, String host) {
/* 168 */     int total = 0;
/* 169 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(groupFilter);
/* 170 */     for (ReplicationConnectionGroup cg : s) {
/* 171 */       total += cg.getConnectionCountWithHostAsMaster(host);
/*     */     }
/* 173 */     return total;
/*     */   }
/*     */   
/*     */   public static Collection<String> getSlaveHosts(String groupFilter) {
/* 177 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(groupFilter);
/* 178 */     Collection<String> hosts = new ArrayList();
/* 179 */     for (ReplicationConnectionGroup cg : s) {
/* 180 */       hosts.addAll(cg.getSlaveHosts());
/*     */     }
/* 182 */     return hosts;
/*     */   }
/*     */   
/*     */   public static Collection<String> getMasterHosts(String groupFilter) {
/* 186 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(groupFilter);
/* 187 */     Collection<String> hosts = new ArrayList();
/* 188 */     for (ReplicationConnectionGroup cg : s) {
/* 189 */       hosts.addAll(cg.getMasterHosts());
/*     */     }
/* 191 */     return hosts;
/*     */   }
/*     */   
/*     */   public static long getTotalConnectionCount(String group) {
/* 195 */     long connections = 0L;
/* 196 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/* 197 */     for (ReplicationConnectionGroup cg : s) {
/* 198 */       connections += cg.getTotalConnectionCount();
/*     */     }
/* 200 */     return connections;
/*     */   }
/*     */   
/*     */   public static long getActiveConnectionCount(String group) {
/* 204 */     long connections = 0L;
/* 205 */     Collection<ReplicationConnectionGroup> s = getGroupsMatching(group);
/* 206 */     for (ReplicationConnectionGroup cg : s) {
/* 207 */       connections += cg.getActiveConnectionCount();
/*     */     }
/* 209 */     return connections;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ReplicationConnectionGroupManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */