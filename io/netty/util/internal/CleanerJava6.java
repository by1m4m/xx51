/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Field;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CleanerJava6
/*     */   implements Cleaner
/*     */ {
/*     */   private static final long CLEANER_FIELD_OFFSET;
/*     */   private static final Method CLEAN_METHOD;
/*     */   private static final Field CLEANER_FIELD;
/*  39 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CleanerJava6.class);
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  45 */     Throwable error = null;
/*  46 */     ByteBuffer direct = ByteBuffer.allocateDirect(1);
/*     */     long fieldOffset;
/*  48 */     Method clean; Field cleanerField; try { Object mayBeCleanerField = AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/*     */           try {
/*  52 */             Field cleanerField = this.val$direct.getClass().getDeclaredField("cleaner");
/*  53 */             if (!PlatformDependent.hasUnsafe())
/*     */             {
/*     */ 
/*  56 */               cleanerField.setAccessible(true);
/*     */             }
/*  58 */             return cleanerField;
/*     */           } catch (Throwable cause) {
/*  60 */             return cause;
/*     */           }
/*     */         }
/*     */       });
/*  64 */       if ((mayBeCleanerField instanceof Throwable)) {
/*  65 */         throw ((Throwable)mayBeCleanerField);
/*     */       }
/*     */       
/*  68 */       Field cleanerField = (Field)mayBeCleanerField;
/*     */       
/*     */       Object cleaner;
/*     */       
/*     */       Object cleaner;
/*     */       
/*  74 */       if (PlatformDependent.hasUnsafe()) {
/*  75 */         long fieldOffset = PlatformDependent0.objectFieldOffset(cleanerField);
/*  76 */         cleaner = PlatformDependent0.getObject(direct, fieldOffset);
/*     */       } else {
/*  78 */         long fieldOffset = -1L;
/*  79 */         cleaner = cleanerField.get(direct);
/*     */       }
/*  81 */       Method clean = cleaner.getClass().getDeclaredMethod("clean", new Class[0]);
/*  82 */       clean.invoke(cleaner, new Object[0]);
/*     */     }
/*     */     catch (Throwable t) {
/*  85 */       fieldOffset = -1L;
/*  86 */       clean = null;
/*  87 */       error = t;
/*  88 */       cleanerField = null;
/*     */     }
/*     */     
/*  91 */     if (error == null) {
/*  92 */       logger.debug("java.nio.ByteBuffer.cleaner(): available");
/*     */     } else {
/*  94 */       logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
/*     */     }
/*  96 */     CLEANER_FIELD = cleanerField;
/*  97 */     CLEANER_FIELD_OFFSET = fieldOffset;
/*  98 */     CLEAN_METHOD = clean;
/*     */   }
/*     */   
/*     */   static boolean isSupported() {
/* 102 */     return (CLEANER_FIELD_OFFSET != -1L) || (CLEANER_FIELD != null);
/*     */   }
/*     */   
/*     */   public void freeDirectBuffer(ByteBuffer buffer)
/*     */   {
/* 107 */     if (!buffer.isDirect()) {
/* 108 */       return;
/*     */     }
/* 110 */     if (System.getSecurityManager() == null) {
/*     */       try {
/* 112 */         freeDirectBuffer0(buffer);
/*     */       } catch (Throwable cause) {
/* 114 */         PlatformDependent0.throwException(cause);
/*     */       }
/*     */     } else {
/* 117 */       freeDirectBufferPrivileged(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void freeDirectBufferPrivileged(ByteBuffer buffer) {
/* 122 */     Throwable cause = (Throwable)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Throwable run() {
/*     */         try {
/* 126 */           CleanerJava6.freeDirectBuffer0(this.val$buffer);
/* 127 */           return null;
/*     */         } catch (Throwable cause) {
/* 129 */           return cause;
/*     */         }
/*     */       }
/*     */     });
/* 133 */     if (cause != null) {
/* 134 */       PlatformDependent0.throwException(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void freeDirectBuffer0(ByteBuffer buffer) throws Exception
/*     */   {
/*     */     Object cleaner;
/*     */     Object cleaner;
/* 142 */     if (CLEANER_FIELD_OFFSET == -1L) {
/* 143 */       cleaner = CLEANER_FIELD.get(buffer);
/*     */     } else {
/* 145 */       cleaner = PlatformDependent0.getObject(buffer, CLEANER_FIELD_OFFSET);
/*     */     }
/* 147 */     if (cleaner != null) {
/* 148 */       CLEAN_METHOD.invoke(cleaner, new Object[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\CleanerJava6.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */