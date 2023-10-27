/*    */ package io.netty.channel.epoll;
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
/*    */ public final class Epoll
/*    */ {
/*    */   private static final Throwable UNAVAILABILITY_CAUSE;
/*    */   
/*    */   static
/*    */   {
/* 29 */     Throwable cause = null;
/*    */     
/* 31 */     if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
/* 32 */       cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
/*    */     }
/*    */     else {
/* 35 */       FileDescriptor epollFd = null;
/* 36 */       FileDescriptor eventFd = null;
/*    */       try {
/* 38 */         epollFd = Native.newEpollCreate();
/* 39 */         eventFd = Native.newEventFd();
/*    */         
/*    */ 
/*    */ 
/* 43 */         if (epollFd != null) {
/*    */           try {
/* 45 */             epollFd.close();
/*    */           }
/*    */           catch (Exception localException) {}
/*    */         }
/*    */         
/* 50 */         if (eventFd != null) {
/*    */           try {
/* 52 */             eventFd.close();
/*    */           }
/*    */           catch (Exception localException1) {}
/*    */         }
/*    */         
/*    */ 
/*    */ 
/*    */ 
/* 60 */         UNAVAILABILITY_CAUSE = cause;
/*    */       }
/*    */       catch (Throwable t)
/*    */       {
/* 41 */         cause = t;
/*    */       } finally {
/* 43 */         if (epollFd != null) {
/*    */           try {
/* 45 */             epollFd.close();
/*    */           }
/*    */           catch (Exception localException4) {}
/*    */         }
/*    */         
/* 50 */         if (eventFd != null) {
/*    */           try {
/* 52 */             eventFd.close();
/*    */           }
/*    */           catch (Exception localException5) {}
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
/* 68 */     return UNAVAILABILITY_CAUSE == null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void ensureAvailability()
/*    */   {
/* 78 */     if (UNAVAILABILITY_CAUSE != null)
/*    */     {
/* 80 */       throw ((Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
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
/* 91 */     return UNAVAILABILITY_CAUSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\Epoll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */