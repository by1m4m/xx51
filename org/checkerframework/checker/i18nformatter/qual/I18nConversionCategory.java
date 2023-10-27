/*     */ package org.checkerframework.checker.i18nformatter.qual;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public enum I18nConversionCategory
/*     */ {
/*  37 */   UNUSED(null, null), 
/*     */   
/*     */ 
/*  40 */   GENERAL(null, null), 
/*     */   
/*     */ 
/*  43 */   DATE(new Class[] { Date.class, Number.class }, new String[] { "date", "time" }), 
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
/*  54 */   NUMBER(new Class[] { Number.class }, new String[] { "number", "choice" });
/*     */   
/*     */ 
/*     */   public final Class<? extends Object>[] types;
/*     */   
/*     */   public final String[] strings;
/*     */   
/*     */   private I18nConversionCategory(Class<? extends Object>[] types, String[] strings)
/*     */   {
/*  63 */     this.types = types;
/*  64 */     this.strings = strings;
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
/*     */   public static I18nConversionCategory stringToI18nConversionCategory(String string)
/*     */   {
/*  77 */     string = string.toLowerCase();
/*  78 */     for (I18nConversionCategory v : new I18nConversionCategory[] { DATE, NUMBER }) {
/*  79 */       for (String s : v.strings) {
/*  80 */         if (s.equals(string)) {
/*  81 */           return v;
/*     */         }
/*     */       }
/*     */     }
/*  85 */     throw new IllegalArgumentException("Invalid format type.");
/*     */   }
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/*  89 */     return new HashSet(Arrays.asList(a));
/*     */   }
/*     */   
/*     */   public static boolean isSubsetOf(I18nConversionCategory a, I18nConversionCategory b)
/*     */   {
/*  94 */     return intersect(a, b) == a;
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
/*     */   public static I18nConversionCategory intersect(I18nConversionCategory a, I18nConversionCategory b)
/*     */   {
/* 110 */     if (a == UNUSED) {
/* 111 */       return b;
/*     */     }
/* 113 */     if (b == UNUSED) {
/* 114 */       return a;
/*     */     }
/* 116 */     if (a == GENERAL) {
/* 117 */       return b;
/*     */     }
/* 119 */     if (b == GENERAL) {
/* 120 */       return a;
/*     */     }
/*     */     
/* 123 */     Set<Class<? extends Object>> as = arrayToSet(a.types);
/* 124 */     Set<Class<? extends Object>> bs = arrayToSet(b.types);
/* 125 */     as.retainAll(bs);
/* 126 */     for (I18nConversionCategory v : new I18nConversionCategory[] { DATE, NUMBER }) {
/* 127 */       Set<Class<? extends Object>> vs = arrayToSet(v.types);
/* 128 */       if (vs.equals(as)) {
/* 129 */         return v;
/*     */       }
/*     */     }
/*     */     
/* 133 */     throw new RuntimeException();
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
/*     */   public static I18nConversionCategory union(I18nConversionCategory a, I18nConversionCategory b)
/*     */   {
/* 148 */     if ((a == UNUSED) || (b == UNUSED)) {
/* 149 */       return UNUSED;
/*     */     }
/* 151 */     if ((a == GENERAL) || (b == GENERAL)) {
/* 152 */       return GENERAL;
/*     */     }
/* 154 */     if ((a == DATE) || (b == DATE)) {
/* 155 */       return DATE;
/*     */     }
/* 157 */     return NUMBER;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 163 */     StringBuilder sb = new StringBuilder(name());
/* 164 */     if (this.types == null) {
/* 165 */       sb.append(" conversion category (all types)");
/*     */     } else {
/* 167 */       sb.append(" conversion category (one of: ");
/* 168 */       boolean first = true;
/* 169 */       for (Class<? extends Object> cls : this.types) {
/* 170 */         if (!first) {
/* 171 */           sb.append(", ");
/*     */         }
/* 173 */         sb.append(cls.getCanonicalName());
/* 174 */         first = false;
/*     */       }
/* 176 */       sb.append(")");
/*     */     }
/* 178 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\i18nformatter\qual\I18nConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */