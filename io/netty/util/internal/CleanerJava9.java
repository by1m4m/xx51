/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ final class CleanerJava9
/*     */   implements Cleaner
/*     */ {
/*  31 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CleanerJava9.class);
/*     */   private static final Method INVOKE_CLEANER;
/*     */   
/*     */   static {
/*     */     Throwable error;
/*     */     Method method;
/*     */     Throwable error;
/*  38 */     if (PlatformDependent0.hasUnsafe()) {
/*  39 */       ByteBuffer buffer = ByteBuffer.allocateDirect(1);
/*  40 */       Object maybeInvokeMethod = AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/*     */           try {
/*  45 */             Method m = PlatformDependent0.UNSAFE.getClass().getDeclaredMethod("invokeCleaner", new Class[] { ByteBuffer.class });
/*     */             
/*  47 */             m.invoke(PlatformDependent0.UNSAFE, new Object[] { this.val$buffer });
/*  48 */             return m;
/*     */           } catch (NoSuchMethodException e) {
/*  50 */             return e;
/*     */           } catch (InvocationTargetException e) {
/*  52 */             return e;
/*     */           } catch (IllegalAccessException e) {
/*  54 */             return e;
/*     */           }
/*     */         }
/*     */       });
/*     */       Throwable error;
/*  59 */       if ((maybeInvokeMethod instanceof Throwable)) {
/*  60 */         Method method = null;
/*  61 */         error = (Throwable)maybeInvokeMethod;
/*     */       } else {
/*  63 */         Method method = (Method)maybeInvokeMethod;
/*  64 */         error = null;
/*     */       }
/*     */     } else {
/*  67 */       method = null;
/*  68 */       error = new UnsupportedOperationException("sun.misc.Unsafe unavailable");
/*     */     }
/*  70 */     if (error == null) {
/*  71 */       logger.debug("java.nio.ByteBuffer.cleaner(): available");
/*     */     } else {
/*  73 */       logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
/*     */     }
/*  75 */     INVOKE_CLEANER = method;
/*     */   }
/*     */   
/*     */   static boolean isSupported() {
/*  79 */     return INVOKE_CLEANER != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void freeDirectBuffer(ByteBuffer buffer)
/*     */   {
/*  86 */     if (System.getSecurityManager() == null) {
/*     */       try {
/*  88 */         INVOKE_CLEANER.invoke(PlatformDependent0.UNSAFE, new Object[] { buffer });
/*     */       } catch (Throwable cause) {
/*  90 */         PlatformDependent0.throwException(cause);
/*     */       }
/*     */     } else {
/*  93 */       freeDirectBufferPrivileged(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void freeDirectBufferPrivileged(ByteBuffer buffer) {
/*  98 */     Exception error = (Exception)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Exception run() {
/*     */         try {
/* 102 */           CleanerJava9.INVOKE_CLEANER.invoke(PlatformDependent0.UNSAFE, new Object[] { this.val$buffer });
/*     */         } catch (InvocationTargetException e) {
/* 104 */           return e;
/*     */         } catch (IllegalAccessException e) {
/* 106 */           return e;
/*     */         }
/* 108 */         return null;
/*     */       }
/*     */     });
/* 111 */     if (error != null) {
/* 112 */       PlatformDependent0.throwException(error);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\CleanerJava9.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */