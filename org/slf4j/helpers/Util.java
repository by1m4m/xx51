/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public final class Util
/*     */ {
/*     */   private static ClassContextSecurityManager SECURITY_MANAGER;
/*     */   
/*     */   public static String safeGetSystemProperty(String key)
/*     */   {
/*  39 */     if (key == null) {
/*  40 */       throw new IllegalArgumentException("null input");
/*     */     }
/*  42 */     String result = null;
/*     */     try {
/*  44 */       result = System.getProperty(key);
/*     */     }
/*     */     catch (SecurityException sm) {}
/*     */     
/*  48 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean safeGetBooleanSystemProperty(String key) {
/*  52 */     String value = safeGetSystemProperty(key);
/*  53 */     if (value == null) {
/*  54 */       return false;
/*     */     }
/*  56 */     return value.equalsIgnoreCase("true");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ClassContextSecurityManager
/*     */     extends SecurityManager
/*     */   {
/*     */     protected Class<?>[] getClassContext()
/*     */     {
/*  66 */       return super.getClassContext();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  71 */   private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
/*     */   
/*     */   private static ClassContextSecurityManager getSecurityManager() {
/*  74 */     if (SECURITY_MANAGER != null)
/*  75 */       return SECURITY_MANAGER;
/*  76 */     if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED) {
/*  77 */       return null;
/*     */     }
/*  79 */     SECURITY_MANAGER = safeCreateSecurityManager();
/*  80 */     SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
/*  81 */     return SECURITY_MANAGER;
/*     */   }
/*     */   
/*     */   private static ClassContextSecurityManager safeCreateSecurityManager()
/*     */   {
/*     */     try {
/*  87 */       return new ClassContextSecurityManager(null);
/*     */     } catch (SecurityException sm) {}
/*  89 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getCallingClass()
/*     */   {
/*  99 */     ClassContextSecurityManager securityManager = getSecurityManager();
/* 100 */     if (securityManager == null)
/* 101 */       return null;
/* 102 */     Class<?>[] trace = securityManager.getClassContext();
/* 103 */     String thisClassName = Util.class.getName();
/*     */     
/*     */ 
/*     */ 
/* 107 */     for (int i = 0; i < trace.length; i++) {
/* 108 */       if (thisClassName.equals(trace[i].getName())) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 113 */     if ((i >= trace.length) || (i + 2 >= trace.length)) {
/* 114 */       throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
/*     */     }
/*     */     
/* 117 */     return trace[(i + 2)];
/*     */   }
/*     */   
/*     */   public static final void report(String msg, Throwable t) {
/* 121 */     System.err.println(msg);
/* 122 */     System.err.println("Reported exception:");
/* 123 */     t.printStackTrace();
/*     */   }
/*     */   
/*     */   public static final void report(String msg) {
/* 127 */     System.err.println("SLF4J: " + msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\slf4j\helpers\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */