/*     */ package com.google.api.client.util;
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
/*     */ public final class Throwables
/*     */ {
/*     */   public static RuntimeException propagate(Throwable throwable)
/*     */   {
/*  56 */     return com.google.api.client.repackaged.com.google.common.base.Throwables.propagate(throwable);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void propagateIfPossible(Throwable throwable)
/*     */   {
/*  77 */     com.google.api.client.repackaged.com.google.common.base.Throwables.propagateIfPossible(throwable);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <X extends Throwable> void propagateIfPossible(Throwable throwable, Class<X> declaredType)
/*     */     throws Throwable
/*     */   {
/* 100 */     com.google.api.client.repackaged.com.google.common.base.Throwables.propagateIfPossible(throwable, declaredType);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Throwables.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */