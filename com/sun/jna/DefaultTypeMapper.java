/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTypeMapper
/*     */   implements TypeMapper
/*     */ {
/*     */   private static class Entry
/*     */   {
/*     */     public Class type;
/*     */     public Object converter;
/*     */     
/*     */     public Entry(Class type, Object converter)
/*     */     {
/*  39 */       this.type = type;
/*  40 */       this.converter = converter;
/*     */     } }
/*     */   
/*  43 */   private List toNativeConverters = new ArrayList();
/*  44 */   private List fromNativeConverters = new ArrayList();
/*     */   
/*  46 */   private Class getAltClass(Class cls) { if (cls == Boolean.class) {
/*  47 */       return Boolean.TYPE;
/*     */     }
/*  49 */     if (cls == Boolean.TYPE) {
/*  50 */       return Boolean.class;
/*     */     }
/*  52 */     if (cls == Byte.class) {
/*  53 */       return Byte.TYPE;
/*     */     }
/*  55 */     if (cls == Byte.TYPE) {
/*  56 */       return Byte.class;
/*     */     }
/*  58 */     if (cls == Character.class) {
/*  59 */       return Character.TYPE;
/*     */     }
/*  61 */     if (cls == Character.TYPE) {
/*  62 */       return Character.class;
/*     */     }
/*  64 */     if (cls == Short.class) {
/*  65 */       return Short.TYPE;
/*     */     }
/*  67 */     if (cls == Short.TYPE) {
/*  68 */       return Short.class;
/*     */     }
/*  70 */     if (cls == Integer.class) {
/*  71 */       return Integer.TYPE;
/*     */     }
/*  73 */     if (cls == Integer.TYPE) {
/*  74 */       return Integer.class;
/*     */     }
/*  76 */     if (cls == Long.class) {
/*  77 */       return Long.TYPE;
/*     */     }
/*  79 */     if (cls == Long.TYPE) {
/*  80 */       return Long.class;
/*     */     }
/*  82 */     if (cls == Float.class) {
/*  83 */       return Float.TYPE;
/*     */     }
/*  85 */     if (cls == Float.TYPE) {
/*  86 */       return Float.class;
/*     */     }
/*  88 */     if (cls == Double.class) {
/*  89 */       return Double.TYPE;
/*     */     }
/*  91 */     if (cls == Double.TYPE) {
/*  92 */       return Double.class;
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addToNativeConverter(Class cls, ToNativeConverter converter)
/*     */   {
/* 104 */     this.toNativeConverters.add(new Entry(cls, converter));
/* 105 */     Class alt = getAltClass(cls);
/* 106 */     if (alt != null) {
/* 107 */       this.toNativeConverters.add(new Entry(alt, converter));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addFromNativeConverter(Class cls, FromNativeConverter converter)
/*     */   {
/* 117 */     this.fromNativeConverters.add(new Entry(cls, converter));
/* 118 */     Class alt = getAltClass(cls);
/* 119 */     if (alt != null) {
/* 120 */       this.fromNativeConverters.add(new Entry(alt, converter));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTypeConverter(Class cls, TypeConverter converter)
/*     */   {
/* 130 */     addFromNativeConverter(cls, converter);
/* 131 */     addToNativeConverter(cls, converter);
/*     */   }
/*     */   
/*     */   private Object lookupConverter(Class javaClass, List converters) {
/* 135 */     for (Iterator i = converters.iterator(); i.hasNext();) {
/* 136 */       Entry entry = (Entry)i.next();
/* 137 */       if (entry.type.isAssignableFrom(javaClass)) {
/* 138 */         return entry.converter;
/*     */       }
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public FromNativeConverter getFromNativeConverter(Class javaType)
/*     */   {
/* 147 */     return (FromNativeConverter)lookupConverter(javaType, this.fromNativeConverters);
/*     */   }
/*     */   
/*     */ 
/*     */   public ToNativeConverter getToNativeConverter(Class javaType)
/*     */   {
/* 153 */     return (ToNativeConverter)lookupConverter(javaType, this.toNativeConverters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\DefaultTypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */