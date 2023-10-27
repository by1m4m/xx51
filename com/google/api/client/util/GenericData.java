/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericData
/*     */   extends AbstractMap<String, Object>
/*     */   implements Cloneable
/*     */ {
/*  50 */   Map<String, Object> unknownFields = ArrayMap.create();
/*     */   
/*     */ 
/*     */ 
/*     */   final ClassInfo classInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericData()
/*     */   {
/*  61 */     this(EnumSet.noneOf(Flags.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Flags
/*     */   {
/*  71 */     IGNORE_CASE;
/*     */     
/*     */ 
/*     */     private Flags() {}
/*     */   }
/*     */   
/*     */   public GenericData(EnumSet<Flags> flags)
/*     */   {
/*  79 */     this.classInfo = ClassInfo.of(getClass(), flags.contains(Flags.IGNORE_CASE));
/*     */   }
/*     */   
/*     */   public final Object get(Object name)
/*     */   {
/*  84 */     if (!(name instanceof String)) {
/*  85 */       return null;
/*     */     }
/*  87 */     String fieldName = (String)name;
/*  88 */     FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
/*  89 */     if (fieldInfo != null) {
/*  90 */       return fieldInfo.getValue(this);
/*     */     }
/*  92 */     if (this.classInfo.getIgnoreCase()) {
/*  93 */       fieldName = fieldName.toLowerCase();
/*     */     }
/*  95 */     return this.unknownFields.get(fieldName);
/*     */   }
/*     */   
/*     */   public final Object put(String fieldName, Object value)
/*     */   {
/* 100 */     FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
/* 101 */     if (fieldInfo != null) {
/* 102 */       Object oldValue = fieldInfo.getValue(this);
/* 103 */       fieldInfo.setValue(this, value);
/* 104 */       return oldValue;
/*     */     }
/* 106 */     if (this.classInfo.getIgnoreCase()) {
/* 107 */       fieldName = fieldName.toLowerCase();
/*     */     }
/* 109 */     return this.unknownFields.put(fieldName, value);
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
/*     */   public GenericData set(String fieldName, Object value)
/*     */   {
/* 123 */     FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
/* 124 */     if (fieldInfo != null) {
/* 125 */       fieldInfo.setValue(this, value);
/*     */     } else {
/* 127 */       if (this.classInfo.getIgnoreCase()) {
/* 128 */         fieldName = fieldName.toLowerCase();
/*     */       }
/* 130 */       this.unknownFields.put(fieldName, value);
/*     */     }
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public final void putAll(Map<? extends String, ?> map)
/*     */   {
/* 137 */     for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
/* 138 */       set((String)entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public final Object remove(Object name)
/*     */   {
/* 144 */     if (!(name instanceof String)) {
/* 145 */       return null;
/*     */     }
/* 147 */     String fieldName = (String)name;
/* 148 */     FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
/* 149 */     if (fieldInfo != null) {
/* 150 */       throw new UnsupportedOperationException();
/*     */     }
/* 152 */     if (this.classInfo.getIgnoreCase()) {
/* 153 */       fieldName = fieldName.toLowerCase();
/*     */     }
/* 155 */     return this.unknownFields.remove(fieldName);
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<String, Object>> entrySet()
/*     */   {
/* 160 */     return new EntrySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericData clone()
/*     */   {
/*     */     try
/*     */     {
/* 171 */       GenericData result = (GenericData)super.clone();
/* 172 */       Data.deepCopy(this, result);
/* 173 */       result.unknownFields = ((Map)Data.clone(this.unknownFields));
/* 174 */       return result;
/*     */     } catch (CloneNotSupportedException e) {
/* 176 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Map<String, Object> getUnknownKeys()
/*     */   {
/* 186 */     return this.unknownFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setUnknownKeys(Map<String, Object> unknownFields)
/*     */   {
/* 195 */     this.unknownFields = unknownFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ClassInfo getClassInfo()
/*     */   {
/* 204 */     return this.classInfo;
/*     */   }
/*     */   
/*     */   final class EntrySet extends AbstractSet<Map.Entry<String, Object>>
/*     */   {
/*     */     private final DataMap.EntrySet dataEntrySet;
/*     */     
/*     */     EntrySet()
/*     */     {
/* 213 */       this.dataEntrySet = new DataMap(GenericData.this, GenericData.this.classInfo.getIgnoreCase()).entrySet();
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<String, Object>> iterator()
/*     */     {
/* 218 */       return new GenericData.EntryIterator(GenericData.this, this.dataEntrySet);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 223 */       return GenericData.this.unknownFields.size() + this.dataEntrySet.size();
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 228 */       GenericData.this.unknownFields.clear();
/* 229 */       this.dataEntrySet.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final class EntryIterator
/*     */     implements Iterator<Map.Entry<String, Object>>
/*     */   {
/*     */     private boolean startedUnknown;
/*     */     
/*     */ 
/*     */     private final Iterator<Map.Entry<String, Object>> fieldIterator;
/*     */     
/*     */ 
/*     */     private final Iterator<Map.Entry<String, Object>> unknownIterator;
/*     */     
/*     */ 
/*     */     EntryIterator(DataMap.EntrySet dataEntrySet)
/*     */     {
/* 249 */       this.fieldIterator = dataEntrySet.iterator();
/* 250 */       this.unknownIterator = GenericData.this.unknownFields.entrySet().iterator();
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 254 */       return (this.fieldIterator.hasNext()) || (this.unknownIterator.hasNext());
/*     */     }
/*     */     
/*     */     public Map.Entry<String, Object> next() {
/* 258 */       if (!this.startedUnknown) {
/* 259 */         if (this.fieldIterator.hasNext()) {
/* 260 */           return (Map.Entry)this.fieldIterator.next();
/*     */         }
/* 262 */         this.startedUnknown = true;
/*     */       }
/* 264 */       return (Map.Entry)this.unknownIterator.next();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 268 */       if (this.startedUnknown) {
/* 269 */         this.unknownIterator.remove();
/*     */       }
/* 271 */       this.fieldIterator.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\GenericData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */