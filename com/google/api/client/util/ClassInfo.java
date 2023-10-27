/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeSet;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassInfo
/*     */ {
/*  41 */   private static final Map<Class<?>, ClassInfo> CACHE = new WeakHashMap();
/*     */   
/*     */ 
/*  44 */   private static final Map<Class<?>, ClassInfo> CACHE_IGNORE_CASE = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private final Class<?> clazz;
/*     */   
/*     */ 
/*     */   private final boolean ignoreCase;
/*     */   
/*     */ 
/*  54 */   private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap = new IdentityHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final List<String> names;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ClassInfo of(Class<?> underlyingClass)
/*     */   {
/*  70 */     return of(underlyingClass, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ClassInfo of(Class<?> underlyingClass, boolean ignoreCase)
/*     */   {
/*  82 */     if (underlyingClass == null) {
/*  83 */       return null;
/*     */     }
/*  85 */     Map<Class<?>, ClassInfo> cache = ignoreCase ? CACHE_IGNORE_CASE : CACHE;
/*     */     ClassInfo classInfo;
/*  87 */     synchronized (cache) {
/*  88 */       classInfo = (ClassInfo)cache.get(underlyingClass);
/*  89 */       if (classInfo == null) {
/*  90 */         classInfo = new ClassInfo(underlyingClass, ignoreCase);
/*  91 */         cache.put(underlyingClass, classInfo);
/*     */       }
/*     */     }
/*  94 */     return classInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getUnderlyingClass()
/*     */   {
/* 103 */     return this.clazz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean getIgnoreCase()
/*     */   {
/* 112 */     return this.ignoreCase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FieldInfo getFieldInfo(String name)
/*     */   {
/* 122 */     if (name != null) {
/* 123 */       if (this.ignoreCase) {
/* 124 */         name = name.toLowerCase();
/*     */       }
/* 126 */       name = name.intern();
/*     */     }
/* 128 */     return (FieldInfo)this.nameToFieldInfoMap.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getField(String name)
/*     */   {
/* 138 */     FieldInfo fieldInfo = getFieldInfo(name);
/* 139 */     return fieldInfo == null ? null : fieldInfo.getField();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEnum()
/*     */   {
/* 148 */     return this.clazz.isEnum();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> getNames()
/*     */   {
/* 156 */     return this.names;
/*     */   }
/*     */   
/*     */   private ClassInfo(Class<?> srcClass, boolean ignoreCase) {
/* 160 */     this.clazz = srcClass;
/* 161 */     this.ignoreCase = ignoreCase;
/* 162 */     String str1 = String.valueOf(String.valueOf(srcClass));Preconditions.checkArgument((!ignoreCase) || (!srcClass.isEnum()), 31 + str1.length() + "cannot ignore case on an enum: " + str1);
/*     */     
/*     */ 
/* 165 */     TreeSet<String> nameSet = new TreeSet(new Comparator() {
/*     */       public int compare(String s0, String s1) {
/* 167 */         return s1 == null ? 1 : s0 == null ? -1 : s0 == s1 ? 0 : s0.compareTo(s1);
/*     */       }
/*     */     });
/*     */     
/* 171 */     for (Field field : srcClass.getDeclaredFields()) {
/* 172 */       FieldInfo fieldInfo = FieldInfo.of(field);
/* 173 */       if (fieldInfo != null)
/*     */       {
/*     */ 
/* 176 */         String fieldName = fieldInfo.getName();
/* 177 */         if (ignoreCase) {
/* 178 */           fieldName = fieldName.toLowerCase().intern();
/*     */         }
/* 180 */         FieldInfo conflictingFieldInfo = (FieldInfo)this.nameToFieldInfoMap.get(fieldName);
/* 181 */         Preconditions.checkArgument(conflictingFieldInfo == null, "two fields have the same %sname <%s>: %s and %s", new Object[] { ignoreCase ? "case-insensitive " : "", fieldName, field, conflictingFieldInfo == null ? null : conflictingFieldInfo.getField() });
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */         this.nameToFieldInfoMap.put(fieldName, fieldInfo);
/* 188 */         nameSet.add(fieldName);
/*     */       }
/*     */     }
/* 191 */     Class<?> superClass = srcClass.getSuperclass();
/* 192 */     if (superClass != null) {
/* 193 */       ClassInfo superClassInfo = of(superClass, ignoreCase);
/* 194 */       nameSet.addAll(superClassInfo.names);
/* 195 */       for (Map.Entry<String, FieldInfo> e : superClassInfo.nameToFieldInfoMap.entrySet()) {
/* 196 */         String name = (String)e.getKey();
/* 197 */         if (!this.nameToFieldInfoMap.containsKey(name)) {
/* 198 */           this.nameToFieldInfoMap.put(name, e.getValue());
/*     */         }
/*     */       }
/*     */     }
/* 202 */     this.names = (nameSet.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(nameSet)));
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
/*     */   public Collection<FieldInfo> getFieldInfos()
/*     */   {
/* 217 */     return Collections.unmodifiableCollection(this.nameToFieldInfoMap.values());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ClassInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */