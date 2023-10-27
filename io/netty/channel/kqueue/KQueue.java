/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.unix.FileDescriptor;
/*    */ import io.netty.util.internal.SystemPropertyUtil;
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
/*    */ public final class KQueue
/*    */ {
/*    */   private static final Throwable UNAVAILABILITY_CAUSE;
/*    */   
/*    */   static
/*    */   {
/* 29 */     Throwable cause = null;
/* 30 */     if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
/* 31 */       cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
/*    */     }
/*    */     else {
/* 34 */       FileDescriptor kqueueFd = null;
/*    */       try {
/* 36 */         kqueueFd = Native.newKQueue();
/*    */         
/*    */ 
/*    */ 
/* 40 */         if (kqueueFd != null) {
/*    */           try {
/* 42 */             kqueueFd.close();
/*    */           }
/*    */           catch (Exception localException) {}
/*    */         }
/*    */         
/*    */ 
/*    */ 
/*    */ 
/* 50 */         UNAVAILABILITY_CAUSE = cause;
/*    */       }
/*    */       catch (Throwable t)
/*    */       {
/* 38 */         cause = t;
/*    */       } finally {
/* 40 */         if (kqueueFd != null) {
/*    */           try {
/* 42 */             kqueueFd.close();
/*    */           }
/*    */           catch (Exception localException2) {}
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 58 */     return UNAVAILABILITY_CAUSE == null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void ensureAvailability()
/*    */   {
/* 68 */     if (UNAVAILABILITY_CAUSE != null)
/*    */     {
/* 70 */       throw ((Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Throwable unavailabilityCause()
/*    */   {
/* 81 */     return UNAVAILABILITY_CAUSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\KQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */