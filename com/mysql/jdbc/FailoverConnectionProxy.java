/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
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
/*     */ public class FailoverConnectionProxy
/*     */   extends LoadBalancingConnectionProxy
/*     */ {
/*     */   boolean failedOver;
/*     */   boolean hasTriedMaster;
/*     */   private long masterFailTimeMillis;
/*     */   boolean preferSlaveDuringFailover;
/*     */   private String primaryHostPortSpec;
/*     */   private long queriesBeforeRetryMaster;
/*     */   long queriesIssuedFailedOver;
/*     */   private int secondsBeforeRetryMaster;
/*     */   
/*     */   class FailoverInvocationHandler
/*     */     extends LoadBalancingConnectionProxy.ConnectionErrorFiringInvocationHandler
/*     */   {
/*     */     public FailoverInvocationHandler(Object toInvokeOn)
/*     */     {
/*  41 */       super(toInvokeOn);
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */     {
/*  46 */       String methodName = method.getName();
/*     */       
/*  48 */       if ((FailoverConnectionProxy.this.failedOver) && (methodName.indexOf("execute") != -1)) {
/*  49 */         FailoverConnectionProxy.this.queriesIssuedFailedOver += 1L;
/*     */       }
/*     */       
/*  52 */       return super.invoke(proxy, method, args);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   FailoverConnectionProxy(List<String> hosts, Properties props)
/*     */     throws SQLException
/*     */   {
/*  67 */     super(hosts, props);
/*  68 */     ConnectionPropertiesImpl connectionProps = new ConnectionPropertiesImpl();
/*  69 */     connectionProps.initializeProperties(props);
/*     */     
/*  71 */     this.queriesBeforeRetryMaster = connectionProps.getQueriesBeforeRetryMaster();
/*  72 */     this.secondsBeforeRetryMaster = connectionProps.getSecondsBeforeRetryMaster();
/*  73 */     this.preferSlaveDuringFailover = false;
/*     */   }
/*     */   
/*     */   protected LoadBalancingConnectionProxy.ConnectionErrorFiringInvocationHandler createConnectionProxy(Object toProxy)
/*     */   {
/*  78 */     return new FailoverInvocationHandler(toProxy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   synchronized void dealWithInvocationException(InvocationTargetException e)
/*     */     throws SQLException, Throwable, InvocationTargetException
/*     */   {
/*  86 */     Throwable t = e.getTargetException();
/*     */     
/*  88 */     if (t != null) {
/*  89 */       if (this.failedOver) {
/*  90 */         createPrimaryConnection();
/*     */         
/*  92 */         if (this.currentConn != null) {
/*  93 */           throw t;
/*     */         }
/*     */       }
/*     */       
/*  97 */       failOver();
/*     */       
/*  99 */       throw t;
/*     */     }
/*     */     
/* 102 */     throw e;
/*     */   }
/*     */   
/*     */   public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */   {
/* 107 */     String methodName = method.getName();
/*     */     
/* 109 */     if ("setPreferSlaveDuringFailover".equals(methodName)) {
/* 110 */       this.preferSlaveDuringFailover = ((Boolean)args[0]).booleanValue();
/* 111 */     } else if ("clearHasTriedMaster".equals(methodName)) {
/* 112 */       this.hasTriedMaster = false;
/* 113 */     } else { if ("hasTriedMaster".equals(methodName))
/* 114 */         return Boolean.valueOf(this.hasTriedMaster);
/* 115 */       if ("isMasterConnection".equals(methodName))
/* 116 */         return Boolean.valueOf(!this.failedOver);
/* 117 */       if ("isSlaveConnection".equals(methodName))
/* 118 */         return Boolean.valueOf(this.failedOver);
/* 119 */       if ("setReadOnly".equals(methodName)) {
/* 120 */         if (this.failedOver)
/* 121 */           return null;
/*     */       } else {
/* 123 */         if (("setAutoCommit".equals(methodName)) && (this.failedOver) && (shouldFallBack()) && (Boolean.TRUE.equals(args[0])) && (this.failedOver))
/*     */         {
/* 125 */           createPrimaryConnection();
/*     */           
/* 127 */           return super.invoke(proxy, method, args, this.failedOver); }
/* 128 */         if ("hashCode".equals(methodName))
/* 129 */           return Integer.valueOf(hashCode());
/* 130 */         if ("equals".equals(methodName)) {
/* 131 */           if ((args[0] instanceof Proxy)) {
/* 132 */             return Boolean.valueOf(((Proxy)args[0]).equals(this));
/*     */           }
/* 134 */           return Boolean.valueOf(equals(args[0]));
/*     */         } } }
/* 136 */     return super.invoke(proxy, method, args, this.failedOver);
/*     */   }
/*     */   
/*     */   private synchronized void createPrimaryConnection() throws SQLException {
/*     */     try {
/* 141 */       this.currentConn = createConnectionForHost(this.primaryHostPortSpec);
/* 142 */       this.failedOver = false;
/* 143 */       this.hasTriedMaster = true;
/*     */       
/*     */ 
/* 146 */       this.queriesIssuedFailedOver = 0L;
/*     */     } catch (SQLException sqlEx) {
/* 148 */       this.failedOver = true;
/*     */       
/* 150 */       if (this.currentConn != null) {
/* 151 */         this.currentConn.getLog().logWarn("Connection to primary host failed", sqlEx);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   synchronized void invalidateCurrentConnection() throws SQLException {
/* 157 */     if (!this.failedOver) {
/* 158 */       this.failedOver = true;
/* 159 */       this.queriesIssuedFailedOver = 0L;
/* 160 */       this.masterFailTimeMillis = System.currentTimeMillis();
/*     */     }
/* 162 */     super.invalidateCurrentConnection();
/*     */   }
/*     */   
/*     */   protected synchronized void pickNewConnection() throws SQLException {
/* 166 */     if ((this.isClosed) && (this.closedExplicitly)) {
/* 167 */       return;
/*     */     }
/*     */     
/* 170 */     if (this.primaryHostPortSpec == null) {
/* 171 */       this.primaryHostPortSpec = ((String)this.hostList.remove(0));
/*     */     }
/*     */     
/* 174 */     if ((this.currentConn == null) || ((this.failedOver) && (shouldFallBack()))) {
/* 175 */       createPrimaryConnection();
/*     */       
/* 177 */       if (this.currentConn != null) {
/* 178 */         return;
/*     */       }
/*     */     }
/*     */     
/* 182 */     failOver();
/*     */   }
/*     */   
/*     */   private synchronized void failOver() throws SQLException {
/* 186 */     if (this.failedOver) {
/* 187 */       Iterator<Map.Entry<String, ConnectionImpl>> iter = this.liveConnections.entrySet().iterator();
/*     */       
/* 189 */       while (iter.hasNext()) {
/* 190 */         Map.Entry<String, ConnectionImpl> entry = (Map.Entry)iter.next();
/* 191 */         ((ConnectionImpl)entry.getValue()).close();
/*     */       }
/*     */       
/* 194 */       this.liveConnections.clear();
/*     */     }
/*     */     
/* 197 */     super.pickNewConnection();
/*     */     
/* 199 */     if (this.currentConn.getFailOverReadOnly()) {
/* 200 */       this.currentConn.setReadOnly(true);
/*     */     } else {
/* 202 */       this.currentConn.setReadOnly(false);
/*     */     }
/*     */     
/* 205 */     this.failedOver = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean shouldFallBack()
/*     */   {
/* 216 */     long secondsSinceFailedOver = (System.currentTimeMillis() - this.masterFailTimeMillis) / 1000L;
/*     */     
/* 218 */     if (secondsSinceFailedOver >= this.secondsBeforeRetryMaster)
/*     */     {
/* 220 */       this.masterFailTimeMillis = System.currentTimeMillis();
/*     */       
/* 222 */       return true; }
/* 223 */     if ((this.queriesBeforeRetryMaster != 0L) && (this.queriesIssuedFailedOver >= this.queriesBeforeRetryMaster)) {
/* 224 */       return true;
/*     */     }
/*     */     
/* 227 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\FailoverConnectionProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */