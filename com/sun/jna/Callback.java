/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
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
/*    */ public abstract interface Callback
/*    */ {
/*    */   public static final String METHOD_NAME = "callback";
/* 47 */   public static final Collection FORBIDDEN_NAMES = Arrays.asList(new String[] { "hashCode", "equals", "toString" });
/*    */   
/*    */   public static abstract interface UncaughtExceptionHandler
/*    */   {
/*    */     public abstract void uncaughtException(Callback paramCallback, Throwable paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */