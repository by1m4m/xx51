/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ContainerBuilder
/*     */ {
/*     */   private static final int MAX_BUF = 1000;
/*     */   private Object[] b;
/*     */   private int tail;
/*     */   private int start;
/*     */   private List<Object> list;
/*     */   private Map<String, Object> map;
/*     */   
/*     */   public ContainerBuilder(int bufSize)
/*     */   {
/*  47 */     this.b = new Object[bufSize & 0xFFFFFFFE];
/*     */   }
/*     */   
/*     */   public boolean canReuse() {
/*  51 */     return (this.list == null) && (this.map == null);
/*     */   }
/*     */   
/*     */   public int bufferLength() {
/*  55 */     return this.b.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int start()
/*     */   {
/*  65 */     if ((this.list != null) || (this.map != null)) {
/*  66 */       throw new IllegalStateException();
/*     */     }
/*  68 */     int prevStart = this.start;
/*  69 */     this.start = this.tail;
/*  70 */     return prevStart;
/*     */   }
/*     */   
/*     */   public int startList(Object value) {
/*  74 */     if ((this.list != null) || (this.map != null)) {
/*  75 */       throw new IllegalStateException();
/*     */     }
/*  77 */     int prevStart = this.start;
/*  78 */     this.start = this.tail;
/*  79 */     add(value);
/*  80 */     return prevStart;
/*     */   }
/*     */   
/*     */   public int startMap(String key, Object value) {
/*  84 */     if ((this.list != null) || (this.map != null)) {
/*  85 */       throw new IllegalStateException();
/*     */     }
/*  87 */     int prevStart = this.start;
/*  88 */     this.start = this.tail;
/*  89 */     put(key, value);
/*  90 */     return prevStart;
/*     */   }
/*     */   
/*     */   public void add(Object value) {
/*  94 */     if (this.list != null) {
/*  95 */       this.list.add(value);
/*  96 */     } else if (this.tail >= this.b.length) {
/*  97 */       _expandList(value);
/*     */     } else {
/*  99 */       this.b[(this.tail++)] = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void put(String key, Object value) {
/* 104 */     if (this.map != null) {
/* 105 */       this.map.put(key, value);
/* 106 */     } else if (this.tail + 2 > this.b.length) {
/* 107 */       _expandMap(key, value);
/*     */     } else {
/* 109 */       this.b[(this.tail++)] = key;
/* 110 */       this.b[(this.tail++)] = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Object> finishList(int prevStart)
/*     */   {
/* 116 */     List<Object> l = this.list;
/* 117 */     if (l == null) {
/* 118 */       l = _buildList(true);
/*     */     } else {
/* 120 */       this.list = null;
/*     */     }
/* 122 */     this.start = prevStart;
/* 123 */     return l;
/*     */   }
/*     */   
/*     */   public Object[] finishArray(int prevStart) {
/*     */     Object[] result;
/*     */     Object[] result;
/* 129 */     if (this.list == null) {
/* 130 */       result = Arrays.copyOfRange(this.b, this.start, this.tail);
/*     */     } else {
/* 132 */       result = this.list.toArray(new Object[this.tail - this.start]);
/* 133 */       this.list = null;
/*     */     }
/* 135 */     this.start = prevStart;
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   public <T> Object[] finishArray(int prevStart, Class<T> elemType)
/*     */   {
/* 141 */     int size = this.tail - this.start;
/*     */     
/* 143 */     T[] result = (Object[])Array.newInstance(elemType, size);
/*     */     
/* 145 */     if (this.list == null) {
/* 146 */       System.arraycopy(this.b, this.start, result, 0, size);
/*     */     } else {
/* 148 */       result = this.list.toArray(result);
/* 149 */       this.list = null;
/*     */     }
/* 151 */     this.start = prevStart;
/* 152 */     return result;
/*     */   }
/*     */   
/*     */   public Map<String, Object> finishMap(int prevStart)
/*     */   {
/* 157 */     Map<String, Object> m = this.map;
/*     */     
/* 159 */     if (m == null) {
/* 160 */       m = _buildMap(true);
/*     */     } else {
/* 162 */       this.map = null;
/*     */     }
/* 164 */     this.start = prevStart;
/* 165 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void _expandList(Object value)
/*     */   {
/* 175 */     if (this.b.length < 1000) {
/* 176 */       this.b = Arrays.copyOf(this.b, this.b.length << 1);
/* 177 */       this.b[(this.tail++)] = value;
/*     */     } else {
/* 179 */       this.list = _buildList(false);
/* 180 */       this.list.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Object> _buildList(boolean isComplete)
/*     */   {
/* 186 */     int currLen = this.tail - this.start;
/* 187 */     if (isComplete) {
/* 188 */       if (currLen < 2) {
/* 189 */         currLen = 2;
/*     */       }
/*     */     }
/* 192 */     else if (currLen < 20) {
/* 193 */       currLen = 20;
/* 194 */     } else if (currLen < 1000) {
/* 195 */       currLen += (currLen >> 1);
/*     */     } else {
/* 197 */       currLen += (currLen >> 2);
/*     */     }
/*     */     
/* 200 */     List<Object> l = new ArrayList(currLen);
/* 201 */     for (int i = this.start; i < this.tail; i++) {
/* 202 */       l.add(this.b[i]);
/*     */     }
/* 204 */     this.tail = this.start;
/* 205 */     return l;
/*     */   }
/*     */   
/*     */   private void _expandMap(String key, Object value) {
/* 209 */     if (this.b.length < 1000) {
/* 210 */       this.b = Arrays.copyOf(this.b, this.b.length << 1);
/* 211 */       this.b[(this.tail++)] = key;
/* 212 */       this.b[(this.tail++)] = value;
/*     */     } else {
/* 214 */       this.map = _buildMap(false);
/* 215 */       this.map.put(key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<String, Object> _buildMap(boolean isComplete)
/*     */   {
/* 221 */     int size = this.tail - this.start >> 1;
/* 222 */     if (isComplete) {
/* 223 */       if (size <= 3) {
/* 224 */         size = 4;
/* 225 */       } else if (size <= 40) {
/* 226 */         size += (size >> 1);
/*     */       } else {
/* 228 */         size += (size >> 2) + (size >> 4);
/*     */       }
/*     */     }
/* 231 */     else if (size < 10) {
/* 232 */       size = 16;
/* 233 */     } else if (size < 1000) {
/* 234 */       size += (size >> 1);
/*     */     } else {
/* 236 */       size += size / 3;
/*     */     }
/*     */     
/* 239 */     Map<String, Object> m = new LinkedHashMap(size, 0.8F);
/* 240 */     for (int i = this.start; i < this.tail; i += 2) {
/* 241 */       m.put((String)this.b[i], this.b[(i + 1)]);
/*     */     }
/* 243 */     this.tail = this.start;
/* 244 */     return m;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\ContainerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */