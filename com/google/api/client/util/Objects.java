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
/*     */ public final class Objects
/*     */ {
/*     */   public static boolean equal(Object a, Object b)
/*     */   {
/*  45 */     return com.google.api.client.repackaged.com.google.common.base.Objects.equal(a, b);
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
/*     */   public static ToStringHelper toStringHelper(Object self)
/*     */   {
/*  88 */     return new ToStringHelper(com.google.api.client.repackaged.com.google.common.base.Objects.toStringHelper(self));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final com.google.api.client.repackaged.com.google.common.base.Objects.ToStringHelper wrapped;
/*     */     
/*     */ 
/*     */ 
/*     */     ToStringHelper(com.google.api.client.repackaged.com.google.common.base.Objects.ToStringHelper wrapped)
/*     */     {
/* 101 */       this.wrapped = wrapped;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper omitNullValues()
/*     */     {
/* 110 */       this.wrapped.omitNullValues();
/* 111 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, Object value)
/*     */     {
/* 120 */       this.wrapped.add(name, value);
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 126 */       return this.wrapped.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Objects.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */