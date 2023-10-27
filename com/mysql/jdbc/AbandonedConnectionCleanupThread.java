/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public class AbandonedConnectionCleanupThread
/*    */   extends Thread
/*    */ {
/* 29 */   private static boolean running = true;
/* 30 */   private static Thread threadRef = null;
/*    */   
/*    */   public AbandonedConnectionCleanupThread() {
/* 33 */     super("Abandoned connection cleanup thread");
/*    */   }
/*    */   
/*    */   public void run() {
/* 37 */     threadRef = this;
/* 38 */     while (running) {
/*    */       try {
/* 40 */         Reference<? extends ConnectionImpl> ref = NonRegisteringDriver.refQueue.remove(100L);
/* 41 */         if (ref != null) {
/*    */           try {
/* 43 */             ((NonRegisteringDriver.ConnectionPhantomReference)ref).cleanup();
/*    */           } finally {
/* 45 */             NonRegisteringDriver.connectionPhantomRefs.remove(ref);
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (Exception ex) {}
/*    */     }
/*    */   }
/*    */   
/*    */   public static void shutdown()
/*    */     throws InterruptedException
/*    */   {
/* 56 */     running = false;
/* 57 */     if (threadRef != null) {
/* 58 */       threadRef.interrupt();
/* 59 */       threadRef.join();
/* 60 */       threadRef = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\AbandonedConnectionCleanupThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */