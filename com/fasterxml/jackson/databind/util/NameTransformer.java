/*     */ package com.fasterxml.jackson.databind.util;
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
/*     */ public abstract class NameTransformer
/*     */ {
/*  14 */   public static final NameTransformer NOP = new NameTransformer()
/*     */   {
/*     */     public String transform(String name) {
/*  17 */       return name;
/*     */     }
/*     */     
/*     */     public String reverse(String transformed)
/*     */     {
/*  22 */       return transformed;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NameTransformer simpleTransformer(String prefix, final String suffix)
/*     */   {
/*  34 */     boolean hasPrefix = (prefix != null) && (prefix.length() > 0);
/*  35 */     boolean hasSuffix = (suffix != null) && (suffix.length() > 0);
/*     */     
/*  37 */     if (hasPrefix) {
/*  38 */       if (hasSuffix) {
/*  39 */         new NameTransformer()
/*     */         {
/*  41 */           public String transform(String name) { return this.val$prefix + name + suffix; }
/*     */           
/*     */           public String reverse(String transformed) {
/*  44 */             if (transformed.startsWith(this.val$prefix)) {
/*  45 */               String str = transformed.substring(this.val$prefix.length());
/*  46 */               if (str.endsWith(suffix)) {
/*  47 */                 return str.substring(0, str.length() - suffix.length());
/*     */               }
/*     */             }
/*  50 */             return null;
/*     */           }
/*     */           
/*  53 */           public String toString() { return "[PreAndSuffixTransformer('" + this.val$prefix + "','" + suffix + "')]"; }
/*     */         };
/*     */       }
/*  56 */       new NameTransformer()
/*     */       {
/*  58 */         public String transform(String name) { return this.val$prefix + name; }
/*     */         
/*     */         public String reverse(String transformed) {
/*  61 */           if (transformed.startsWith(this.val$prefix)) {
/*  62 */             return transformed.substring(this.val$prefix.length());
/*     */           }
/*  64 */           return null;
/*     */         }
/*     */         
/*  67 */         public String toString() { return "[PrefixTransformer('" + this.val$prefix + "')]"; }
/*     */       };
/*     */     }
/*  70 */     if (hasSuffix) {
/*  71 */       new NameTransformer()
/*     */       {
/*  73 */         public String transform(String name) { return name + this.val$suffix; }
/*     */         
/*     */         public String reverse(String transformed) {
/*  76 */           if (transformed.endsWith(this.val$suffix)) {
/*  77 */             return transformed.substring(0, transformed.length() - this.val$suffix.length());
/*     */           }
/*  79 */           return null;
/*     */         }
/*     */         
/*  82 */         public String toString() { return "[SuffixTransformer('" + this.val$suffix + "')]"; }
/*     */       };
/*     */     }
/*  85 */     return NOP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NameTransformer chainedTransformer(NameTransformer t1, NameTransformer t2)
/*     */   {
/*  94 */     return new Chained(t1, t2);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract String transform(String paramString);
/*     */   
/*     */ 
/*     */   public abstract String reverse(String paramString);
/*     */   
/*     */ 
/*     */   public static class Chained
/*     */     extends NameTransformer
/*     */   {
/*     */     protected final NameTransformer _t1;
/*     */     
/*     */     protected final NameTransformer _t2;
/*     */     
/*     */ 
/*     */     public Chained(NameTransformer t1, NameTransformer t2)
/*     */     {
/* 114 */       this._t1 = t1;
/* 115 */       this._t2 = t2;
/*     */     }
/*     */     
/*     */     public String transform(String name)
/*     */     {
/* 120 */       return this._t1.transform(this._t2.transform(name));
/*     */     }
/*     */     
/*     */     public String reverse(String transformed)
/*     */     {
/* 125 */       transformed = this._t1.reverse(transformed);
/* 126 */       if (transformed != null) {
/* 127 */         transformed = this._t2.reverse(transformed);
/*     */       }
/* 129 */       return transformed;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 133 */       return "[ChainedTransformer(" + this._t1 + ", " + this._t2 + ")]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\NameTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */