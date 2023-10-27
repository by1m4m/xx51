/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DataMap
/*     */   extends AbstractMap<String, Object>
/*     */ {
/*     */   final Object object;
/*     */   final ClassInfo classInfo;
/*     */   
/*     */   DataMap(Object object, boolean ignoreCase)
/*     */   {
/*  43 */     this.object = object;
/*  44 */     this.classInfo = ClassInfo.of(object.getClass(), ignoreCase);
/*  45 */     Preconditions.checkArgument(!this.classInfo.isEnum());
/*     */   }
/*     */   
/*     */   public EntrySet entrySet()
/*     */   {
/*  50 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key)
/*     */   {
/*  55 */     return get(key) != null;
/*     */   }
/*     */   
/*     */   public Object get(Object key)
/*     */   {
/*  60 */     if (!(key instanceof String)) {
/*  61 */       return null;
/*     */     }
/*  63 */     FieldInfo fieldInfo = this.classInfo.getFieldInfo((String)key);
/*  64 */     if (fieldInfo == null) {
/*  65 */       return null;
/*     */     }
/*  67 */     return fieldInfo.getValue(this.object);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Object put(String key, Object value)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 40	com/google/api/client/util/DataMap:classInfo	Lcom/google/api/client/util/ClassInfo;
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 72	com/google/api/client/util/ClassInfo:getFieldInfo	(Ljava/lang/String;)Lcom/google/api/client/util/FieldInfo;
/*     */     //   8: astore_3
/*     */     //   9: aload_3
/*     */     //   10: ldc 83
/*     */     //   12: aload_1
/*     */     //   13: invokestatic 87	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   16: dup
/*     */     //   17: invokevirtual 91	java/lang/String:length	()I
/*     */     //   20: ifeq +9 -> 29
/*     */     //   23: invokevirtual 95	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   26: goto +12 -> 38
/*     */     //   29: pop
/*     */     //   30: new 68	java/lang/String
/*     */     //   33: dup_x1
/*     */     //   34: swap
/*     */     //   35: invokespecial 98	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   38: invokestatic 102	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   41: pop
/*     */     //   42: aload_3
/*     */     //   43: aload_0
/*     */     //   44: getfield 26	com/google/api/client/util/DataMap:object	Ljava/lang/Object;
/*     */     //   47: invokevirtual 77	com/google/api/client/util/FieldInfo:getValue	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   50: astore 4
/*     */     //   52: aload_3
/*     */     //   53: aload_0
/*     */     //   54: getfield 26	com/google/api/client/util/DataMap:object	Ljava/lang/Object;
/*     */     //   57: aload_2
/*     */     //   58: invokestatic 104	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   61: invokevirtual 108	com/google/api/client/util/FieldInfo:setValue	(Ljava/lang/Object;Ljava/lang/Object;)V
/*     */     //   64: aload 4
/*     */     //   66: areturn
/*     */     // Line number table:
/*     */     //   Java source line #72	-> byte code offset #0
/*     */     //   Java source line #73	-> byte code offset #9
/*     */     //   Java source line #74	-> byte code offset #42
/*     */     //   Java source line #75	-> byte code offset #52
/*     */     //   Java source line #76	-> byte code offset #64
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	67	0	this	DataMap
/*     */     //   0	67	1	key	String
/*     */     //   0	67	2	value	Object
/*     */     //   9	58	3	fieldInfo	FieldInfo
/*     */     //   52	15	4	oldValue	Object
/*     */   }
/*     */   
/*     */   final class EntrySet
/*     */     extends AbstractSet<Map.Entry<String, Object>>
/*     */   {
/*     */     EntrySet() {}
/*     */     
/*     */     public DataMap.EntryIterator iterator()
/*     */     {
/*  84 */       return new DataMap.EntryIterator(DataMap.this);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/*  89 */       int result = 0;
/*  90 */       for (String name : DataMap.this.classInfo.names) {
/*  91 */         if (DataMap.this.classInfo.getFieldInfo(name).getValue(DataMap.this.object) != null) {
/*  92 */           result++;
/*     */         }
/*     */       }
/*  95 */       return result;
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 100 */       for (String name : DataMap.this.classInfo.names) {
/* 101 */         DataMap.this.classInfo.getFieldInfo(name).setValue(DataMap.this.object, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 107 */       for (String name : DataMap.this.classInfo.names) {
/* 108 */         if (DataMap.this.classInfo.getFieldInfo(name).getValue(DataMap.this.object) != null) {
/* 109 */           return false;
/*     */         }
/*     */       }
/* 112 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final class EntryIterator
/*     */     implements Iterator<Map.Entry<String, Object>>
/*     */   {
/* 123 */     private int nextKeyIndex = -1;
/*     */     
/*     */ 
/*     */ 
/*     */     private FieldInfo nextFieldInfo;
/*     */     
/*     */ 
/*     */ 
/*     */     private Object nextFieldValue;
/*     */     
/*     */ 
/*     */     private boolean isRemoved;
/*     */     
/*     */ 
/*     */     private boolean isComputed;
/*     */     
/*     */ 
/*     */     private FieldInfo currentFieldInfo;
/*     */     
/*     */ 
/*     */ 
/*     */     EntryIterator() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 150 */       if (!this.isComputed) {
/* 151 */         this.isComputed = true;
/* 152 */         this.nextFieldValue = null;
/* 153 */         while ((this.nextFieldValue == null) && (++this.nextKeyIndex < DataMap.this.classInfo.names.size())) {
/* 154 */           this.nextFieldInfo = DataMap.this.classInfo.getFieldInfo((String)DataMap.this.classInfo.names.get(this.nextKeyIndex));
/* 155 */           this.nextFieldValue = this.nextFieldInfo.getValue(DataMap.this.object);
/*     */         }
/*     */       }
/* 158 */       return this.nextFieldValue != null;
/*     */     }
/*     */     
/*     */     public Map.Entry<String, Object> next() {
/* 162 */       if (!hasNext()) {
/* 163 */         throw new NoSuchElementException();
/*     */       }
/* 165 */       this.currentFieldInfo = this.nextFieldInfo;
/* 166 */       Object currentFieldValue = this.nextFieldValue;
/* 167 */       this.isComputed = false;
/* 168 */       this.isRemoved = false;
/* 169 */       this.nextFieldInfo = null;
/* 170 */       this.nextFieldValue = null;
/* 171 */       return new DataMap.Entry(DataMap.this, this.currentFieldInfo, currentFieldValue);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 175 */       Preconditions.checkState((this.currentFieldInfo != null) && (!this.isRemoved));
/* 176 */       this.isRemoved = true;
/* 177 */       this.currentFieldInfo.setValue(DataMap.this.object, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final class Entry
/*     */     implements Map.Entry<String, Object>
/*     */   {
/*     */     private Object fieldValue;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final FieldInfo fieldInfo;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Entry(FieldInfo fieldInfo, Object fieldValue)
/*     */     {
/* 200 */       this.fieldInfo = fieldInfo;
/* 201 */       this.fieldValue = Preconditions.checkNotNull(fieldValue);
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 205 */       String result = this.fieldInfo.getName();
/* 206 */       if (DataMap.this.classInfo.getIgnoreCase()) {
/* 207 */         result = result.toLowerCase();
/*     */       }
/* 209 */       return result;
/*     */     }
/*     */     
/*     */     public Object getValue() {
/* 213 */       return this.fieldValue;
/*     */     }
/*     */     
/*     */     public Object setValue(Object value) {
/* 217 */       Object oldValue = this.fieldValue;
/* 218 */       this.fieldValue = Preconditions.checkNotNull(value);
/* 219 */       this.fieldInfo.setValue(DataMap.this.object, value);
/* 220 */       return oldValue;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 225 */       return getKey().hashCode() ^ getValue().hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 230 */       if (this == obj) {
/* 231 */         return true;
/*     */       }
/* 233 */       if (!(obj instanceof Map.Entry)) {
/* 234 */         return false;
/*     */       }
/* 236 */       Map.Entry<?, ?> other = (Map.Entry)obj;
/* 237 */       return (getKey().equals(other.getKey())) && (getValue().equals(other.getValue()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\DataMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */