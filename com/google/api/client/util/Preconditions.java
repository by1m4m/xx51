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
/*     */ public final class Preconditions
/*     */ {
/*     */   public static void checkArgument(boolean expression)
/*     */   {
/*  37 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(expression);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void checkArgument(boolean expression, Object errorMessage)
/*     */   {
/*  49 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(expression, errorMessage);
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
/*     */   public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
/*     */   {
/*  69 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkArgument(expression, errorMessageTemplate, errorMessageArgs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void checkState(boolean expression)
/*     */   {
/*  81 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(expression);
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
/*     */   public static void checkState(boolean expression, Object errorMessage)
/*     */   {
/*  94 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(expression, errorMessage);
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
/*     */   public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
/*     */   {
/* 115 */     com.google.api.client.repackaged.com.google.common.base.Preconditions.checkState(expression, errorMessageTemplate, errorMessageArgs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T checkNotNull(T reference)
/*     */   {
/* 127 */     return (T)com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull(reference);
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
/*     */   public static <T> T checkNotNull(T reference, Object errorMessage)
/*     */   {
/* 140 */     return (T)com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull(reference, errorMessage);
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
/*     */   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs)
/*     */   {
/* 159 */     return (T)com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull(reference, errorMessageTemplate, errorMessageArgs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Preconditions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */