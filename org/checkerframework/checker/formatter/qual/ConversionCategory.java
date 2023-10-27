/*     */ package org.checkerframework.checker.formatter.qual;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.checkerframework.dataflow.qual.Pure;
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
/*     */ public enum ConversionCategory
/*     */ {
/*  33 */   GENERAL(null, "bBhHsS"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  40 */   CHAR(new Class[] { Character.class, Byte.class, Short.class, Integer.class }, "cC"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   INT(new Class[] { Byte.class, Short.class, Integer.class, Long.class, BigInteger.class }, "doxX"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   FLOAT(new Class[] { Float.class, Double.class, BigDecimal.class }, "eEfgGaA"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   TIME(new Class[] { Long.class, Calendar.class, Date.class }, "tT"), 
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
/*  80 */   CHAR_AND_INT(new Class[] { Byte.class, Short.class, Integer.class }, null), 
/*     */   
/*  82 */   INT_AND_TIME(new Class[] { Long.class }, null), 
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
/*  94 */   NULL(new Class[0], null), 
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
/* 107 */   UNUSED(null, null);
/*     */   
/*     */   private ConversionCategory(Class<? extends Object>[] types, String chars) {
/* 110 */     this.types = types;
/* 111 */     this.chars = chars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Class<? extends Object>[] types;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String chars;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConversionCategory fromConversionChar(char c)
/*     */   {
/* 131 */     for (ConversionCategory v : new ConversionCategory[] { GENERAL, CHAR, INT, FLOAT, TIME }) {
/* 132 */       if (v.chars.contains(String.valueOf(c))) {
/* 133 */         return v;
/*     */       }
/*     */     }
/* 136 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/* 140 */     return new HashSet(Arrays.asList(a));
/*     */   }
/*     */   
/*     */   public static boolean isSubsetOf(ConversionCategory a, ConversionCategory b) {
/* 144 */     return intersect(a, b) == a;
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
/*     */   public static ConversionCategory intersect(ConversionCategory a, ConversionCategory b)
/*     */   {
/* 159 */     if (a == UNUSED) {
/* 160 */       return b;
/*     */     }
/* 162 */     if (b == UNUSED) {
/* 163 */       return a;
/*     */     }
/* 165 */     if (a == GENERAL) {
/* 166 */       return b;
/*     */     }
/* 168 */     if (b == GENERAL) {
/* 169 */       return a;
/*     */     }
/*     */     
/* 172 */     Set<Class<? extends Object>> as = arrayToSet(a.types);
/* 173 */     Set<Class<? extends Object>> bs = arrayToSet(b.types);
/* 174 */     as.retainAll(bs);
/*     */     
/* 176 */     for (ConversionCategory v : new ConversionCategory[] { CHAR, INT, FLOAT, TIME, CHAR_AND_INT, INT_AND_TIME, NULL })
/*     */     {
/*     */ 
/* 179 */       Set<Class<? extends Object>> vs = arrayToSet(v.types);
/* 180 */       if (vs.equals(as)) {
/* 181 */         return v;
/*     */       }
/*     */     }
/*     */     
/* 185 */     throw new RuntimeException();
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
/*     */   public static ConversionCategory union(ConversionCategory a, ConversionCategory b)
/*     */   {
/* 200 */     if ((a == UNUSED) || (b == UNUSED)) {
/* 201 */       return UNUSED;
/*     */     }
/* 203 */     if ((a == GENERAL) || (b == GENERAL)) {
/* 204 */       return GENERAL;
/*     */     }
/* 206 */     if (((a == CHAR_AND_INT) && (b == INT_AND_TIME)) || ((a == INT_AND_TIME) && (b == CHAR_AND_INT)))
/*     */     {
/*     */ 
/*     */ 
/* 210 */       return INT;
/*     */     }
/*     */     
/* 213 */     Set<Class<? extends Object>> as = arrayToSet(a.types);
/* 214 */     Set<Class<? extends Object>> bs = arrayToSet(b.types);
/* 215 */     as.addAll(bs);
/*     */     
/* 217 */     for (ConversionCategory v : new ConversionCategory[] { NULL, CHAR_AND_INT, INT_AND_TIME, CHAR, INT, FLOAT, TIME })
/*     */     {
/*     */ 
/* 220 */       Set<Class<? extends Object>> vs = arrayToSet(v.types);
/* 221 */       if (vs.equals(as)) {
/* 222 */         return v;
/*     */       }
/*     */     }
/*     */     
/* 226 */     return GENERAL;
/*     */   }
/*     */   
/*     */   private String className(Class<?> cls) {
/* 230 */     if (cls == Boolean.class) {
/* 231 */       return "boolean";
/*     */     }
/* 233 */     if (cls == Character.class) {
/* 234 */       return "char";
/*     */     }
/* 236 */     if (cls == Byte.class) {
/* 237 */       return "byte";
/*     */     }
/* 239 */     if (cls == Short.class) {
/* 240 */       return "short";
/*     */     }
/* 242 */     if (cls == Integer.class) {
/* 243 */       return "int";
/*     */     }
/* 245 */     if (cls == Long.class) {
/* 246 */       return "long";
/*     */     }
/* 248 */     if (cls == Float.class) {
/* 249 */       return "float";
/*     */     }
/* 251 */     if (cls == Double.class) {
/* 252 */       return "double";
/*     */     }
/* 254 */     return cls.getSimpleName();
/*     */   }
/*     */   
/*     */ 
/*     */   @Pure
/*     */   public String toString()
/*     */   {
/* 261 */     StringBuilder sb = new StringBuilder(name());
/* 262 */     sb.append(" conversion category (one of: ");
/* 263 */     boolean first = true;
/* 264 */     for (Class<? extends Object> cls : this.types) {
/* 265 */       if (!first) {
/* 266 */         sb.append(", ");
/*     */       }
/* 268 */       sb.append(className(cls));
/* 269 */       first = false;
/*     */     }
/* 271 */     sb.append(")");
/* 272 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\formatter\qual\ConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */