/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ public abstract class ContextAttributes
/*     */ {
/*     */   public static ContextAttributes getEmpty()
/*     */   {
/*  24 */     return Impl.getEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContextAttributes withSharedAttribute(Object paramObject1, Object paramObject2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContextAttributes withSharedAttributes(Map<Object, Object> paramMap);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContextAttributes withoutSharedAttribute(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Object getAttribute(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContextAttributes withPerCallAttribute(Object paramObject1, Object paramObject2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Impl
/*     */     extends ContextAttributes
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*  67 */     protected static final Impl EMPTY = new Impl(Collections.emptyMap());
/*     */     
/*  69 */     protected static final Object NULL_SURROGATE = new Object();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final Map<Object, Object> _shared;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected transient Map<Object, Object> _nonShared;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Impl(Map<Object, Object> shared)
/*     */     {
/*  89 */       this._shared = shared;
/*  90 */       this._nonShared = null;
/*     */     }
/*     */     
/*     */     protected Impl(Map<Object, Object> shared, Map<Object, Object> nonShared) {
/*  94 */       this._shared = shared;
/*  95 */       this._nonShared = nonShared;
/*     */     }
/*     */     
/*     */     public static ContextAttributes getEmpty() {
/*  99 */       return EMPTY;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContextAttributes withSharedAttribute(Object key, Object value)
/*     */     {
/*     */       Map<Object, Object> m;
/*     */       
/*     */ 
/*     */       Map<Object, Object> m;
/*     */       
/*     */ 
/* 113 */       if (this == EMPTY) {
/* 114 */         m = new HashMap(8);
/*     */       } else {
/* 116 */         m = _copy(this._shared);
/*     */       }
/* 118 */       m.put(key, value);
/* 119 */       return new Impl(m);
/*     */     }
/*     */     
/*     */     public ContextAttributes withSharedAttributes(Map<Object, Object> shared)
/*     */     {
/* 124 */       return new Impl(shared);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ContextAttributes withoutSharedAttribute(Object key)
/*     */     {
/* 131 */       if (this._shared.isEmpty()) {
/* 132 */         return this;
/*     */       }
/* 134 */       if (this._shared.containsKey(key)) {
/* 135 */         if (this._shared.size() == 1) {
/* 136 */           return EMPTY;
/*     */         }
/*     */       } else {
/* 139 */         return this;
/*     */       }
/*     */       
/* 142 */       Map<Object, Object> m = _copy(this._shared);
/* 143 */       m.remove(key);
/* 144 */       return new Impl(m);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object getAttribute(Object key)
/*     */     {
/* 156 */       if (this._nonShared != null) {
/* 157 */         Object ob = this._nonShared.get(key);
/* 158 */         if (ob != null) {
/* 159 */           if (ob == NULL_SURROGATE) {
/* 160 */             return null;
/*     */           }
/* 162 */           return ob;
/*     */         }
/*     */       }
/* 165 */       return this._shared.get(key);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ContextAttributes withPerCallAttribute(Object key, Object value)
/*     */     {
/* 172 */       if (value == null)
/*     */       {
/* 174 */         if (this._shared.containsKey(key)) {
/* 175 */           value = NULL_SURROGATE;
/*     */         }
/*     */         else {
/* 178 */           return this;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 183 */       if (this._nonShared == null) {
/* 184 */         return nonSharedInstance(key, value);
/*     */       }
/* 186 */       this._nonShared.put(key, value);
/* 187 */       return this;
/*     */     }
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
/*     */     protected ContextAttributes nonSharedInstance(Object key, Object value)
/*     */     {
/* 202 */       Map<Object, Object> m = new HashMap();
/* 203 */       if (value == null) {
/* 204 */         value = NULL_SURROGATE;
/*     */       }
/* 206 */       m.put(key, value);
/* 207 */       return new Impl(this._shared, m);
/*     */     }
/*     */     
/*     */     private Map<Object, Object> _copy(Map<Object, Object> src)
/*     */     {
/* 212 */       return new HashMap(src);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\cfg\ContextAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */