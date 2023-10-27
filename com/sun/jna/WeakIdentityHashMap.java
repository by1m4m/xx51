/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class WeakIdentityHashMap
/*     */   implements Map
/*     */ {
/*  50 */   private final ReferenceQueue queue = new ReferenceQueue();
/*  51 */   private Map backingStore = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/*  59 */     this.backingStore.clear();
/*  60 */     reap();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  64 */     reap();
/*  65 */     return this.backingStore.containsKey(new IdentityWeakReference(key));
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  69 */     reap();
/*  70 */     return this.backingStore.containsValue(value);
/*     */   }
/*     */   
/*     */   public Set entrySet() {
/*  74 */     reap();
/*  75 */     Set ret = new HashSet();
/*  76 */     for (Iterator i = this.backingStore.entrySet().iterator(); i.hasNext();) {
/*  77 */       Map.Entry ref = (Map.Entry)i.next();
/*  78 */       final Object key = ((IdentityWeakReference)ref.getKey()).get();
/*  79 */       final Object value = ref.getValue();
/*  80 */       Map.Entry entry = new Map.Entry() {
/*     */         public Object getKey() {
/*  82 */           return key;
/*     */         }
/*     */         
/*  85 */         public Object getValue() { return value; }
/*     */         
/*     */         public Object setValue(Object value) {
/*  88 */           throw new UnsupportedOperationException();
/*     */         }
/*  90 */       };
/*  91 */       ret.add(entry);
/*     */     }
/*  93 */     return Collections.unmodifiableSet(ret);
/*     */   }
/*     */   
/*  96 */   public Set keySet() { reap();
/*  97 */     Set ret = new HashSet();
/*  98 */     for (Iterator i = this.backingStore.keySet().iterator(); i.hasNext();) {
/*  99 */       IdentityWeakReference ref = (IdentityWeakReference)i.next();
/* 100 */       ret.add(ref.get());
/*     */     }
/* 102 */     return Collections.unmodifiableSet(ret);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 106 */     return this.backingStore.equals(((WeakIdentityHashMap)o).backingStore);
/*     */   }
/*     */   
/*     */   public Object get(Object key) {
/* 110 */     reap();
/* 111 */     return this.backingStore.get(new IdentityWeakReference(key));
/*     */   }
/*     */   
/* 114 */   public Object put(Object key, Object value) { reap();
/* 115 */     return this.backingStore.put(new IdentityWeakReference(key), value);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 119 */     reap();
/* 120 */     return this.backingStore.hashCode();
/*     */   }
/*     */   
/* 123 */   public boolean isEmpty() { reap();
/* 124 */     return this.backingStore.isEmpty();
/*     */   }
/*     */   
/* 127 */   public void putAll(Map t) { throw new UnsupportedOperationException(); }
/*     */   
/*     */   public Object remove(Object key) {
/* 130 */     reap();
/* 131 */     return this.backingStore.remove(new IdentityWeakReference(key));
/*     */   }
/*     */   
/* 134 */   public int size() { reap();
/* 135 */     return this.backingStore.size();
/*     */   }
/*     */   
/* 138 */   public Collection values() { reap();
/* 139 */     return this.backingStore.values();
/*     */   }
/*     */   
/*     */   private synchronized void reap() {
/* 143 */     Object zombie = this.queue.poll();
/*     */     
/* 145 */     while (zombie != null) {
/* 146 */       IdentityWeakReference victim = (IdentityWeakReference)zombie;
/* 147 */       this.backingStore.remove(victim);
/* 148 */       zombie = this.queue.poll();
/*     */     }
/*     */   }
/*     */   
/*     */   class IdentityWeakReference extends WeakReference
/*     */   {
/*     */     int hash;
/*     */     
/*     */     IdentityWeakReference(Object obj) {
/* 157 */       super(WeakIdentityHashMap.this.queue);
/* 158 */       this.hash = System.identityHashCode(obj);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 162 */       return this.hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 166 */       if (this == o) {
/* 167 */         return true;
/*     */       }
/* 169 */       IdentityWeakReference ref = (IdentityWeakReference)o;
/* 170 */       if (get() == ref.get()) {
/* 171 */         return true;
/*     */       }
/* 173 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\WeakIdentityHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */