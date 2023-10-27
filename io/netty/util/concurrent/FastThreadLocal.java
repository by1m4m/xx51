/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
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
/*     */ public class FastThreadLocal<V>
/*     */ {
/*  46 */   private static final int variablesToRemoveIndex = ;
/*     */   
/*     */ 
/*     */   private final int index;
/*     */   
/*     */ 
/*     */   public static void removeAll()
/*     */   {
/*  54 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  55 */     if (threadLocalMap == null) {
/*  56 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  60 */       Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*  61 */       if ((v != null) && (v != InternalThreadLocalMap.UNSET))
/*     */       {
/*  63 */         Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
/*     */         
/*  65 */         FastThreadLocal<?>[] variablesToRemoveArray = (FastThreadLocal[])variablesToRemove.toArray(new FastThreadLocal[0]);
/*  66 */         for (FastThreadLocal<?> tlv : variablesToRemoveArray) {
/*  67 */           tlv.remove(threadLocalMap);
/*     */         }
/*     */       }
/*     */     } finally {
/*  71 */       InternalThreadLocalMap.remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int size()
/*     */   {
/*  79 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  80 */     if (threadLocalMap == null) {
/*  81 */       return 0;
/*     */     }
/*  83 */     return threadLocalMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void destroy() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable)
/*     */   {
/*  99 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     Set<FastThreadLocal<?>> variablesToRemove;
/* 101 */     if ((v == InternalThreadLocalMap.UNSET) || (v == null)) {
/* 102 */       Set<FastThreadLocal<?>> variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
/* 103 */       threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
/*     */     } else {
/* 105 */       variablesToRemove = (Set)v;
/*     */     }
/*     */     
/* 108 */     variablesToRemove.add(variable);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable)
/*     */   {
/* 114 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     
/* 116 */     if ((v == InternalThreadLocalMap.UNSET) || (v == null)) {
/* 117 */       return;
/*     */     }
/*     */     
/*     */ 
/* 121 */     Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
/* 122 */     variablesToRemove.remove(variable);
/*     */   }
/*     */   
/*     */ 
/*     */   public FastThreadLocal()
/*     */   {
/* 128 */     this.index = InternalThreadLocalMap.nextVariableIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V get()
/*     */   {
/* 136 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 137 */     Object v = threadLocalMap.indexedVariable(this.index);
/* 138 */     if (v != InternalThreadLocalMap.UNSET) {
/* 139 */       return (V)v;
/*     */     }
/*     */     
/* 142 */     V value = initialize(threadLocalMap);
/* 143 */     registerCleaner(threadLocalMap);
/* 144 */     return value;
/*     */   }
/*     */   
/*     */   private void registerCleaner(InternalThreadLocalMap threadLocalMap) {
/* 148 */     Thread current = Thread.currentThread();
/* 149 */     if ((FastThreadLocalThread.willCleanupFastThreadLocals(current)) || (threadLocalMap.isCleanerFlagSet(this.index))) {
/* 150 */       return;
/*     */     }
/*     */     
/* 153 */     threadLocalMap.setCleanerFlag(this.index);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V get(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 177 */     Object v = threadLocalMap.indexedVariable(this.index);
/* 178 */     if (v != InternalThreadLocalMap.UNSET) {
/* 179 */       return (V)v;
/*     */     }
/*     */     
/* 182 */     return (V)initialize(threadLocalMap);
/*     */   }
/*     */   
/*     */   private V initialize(InternalThreadLocalMap threadLocalMap) {
/* 186 */     V v = null;
/*     */     try {
/* 188 */       v = initialValue();
/*     */     } catch (Exception e) {
/* 190 */       PlatformDependent.throwException(e);
/*     */     }
/*     */     
/* 193 */     threadLocalMap.setIndexedVariable(this.index, v);
/* 194 */     addToVariablesToRemove(threadLocalMap, this);
/* 195 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void set(V value)
/*     */   {
/* 202 */     if (value != InternalThreadLocalMap.UNSET) {
/* 203 */       InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 204 */       if (setKnownNotUnset(threadLocalMap, value)) {
/* 205 */         registerCleaner(threadLocalMap);
/*     */       }
/*     */     } else {
/* 208 */       remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void set(InternalThreadLocalMap threadLocalMap, V value)
/*     */   {
/* 216 */     if (value != InternalThreadLocalMap.UNSET) {
/* 217 */       setKnownNotUnset(threadLocalMap, value);
/*     */     } else {
/* 219 */       remove(threadLocalMap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean setKnownNotUnset(InternalThreadLocalMap threadLocalMap, V value)
/*     */   {
/* 227 */     if (threadLocalMap.setIndexedVariable(this.index, value)) {
/* 228 */       addToVariablesToRemove(threadLocalMap, this);
/* 229 */       return true;
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isSet()
/*     */   {
/* 238 */     return isSet(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isSet(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 246 */     return (threadLocalMap != null) && (threadLocalMap.isIndexedVariableSet(this.index));
/*     */   }
/*     */   
/*     */ 
/*     */   public final void remove()
/*     */   {
/* 252 */     remove(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void remove(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 262 */     if (threadLocalMap == null) {
/* 263 */       return;
/*     */     }
/*     */     
/* 266 */     Object v = threadLocalMap.removeIndexedVariable(this.index);
/* 267 */     removeFromVariablesToRemove(threadLocalMap, this);
/*     */     
/* 269 */     if (v != InternalThreadLocalMap.UNSET) {
/*     */       try {
/* 271 */         onRemoval(v);
/*     */       } catch (Exception e) {
/* 273 */         PlatformDependent.throwException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected V initialValue()
/*     */     throws Exception
/*     */   {
/* 282 */     return null;
/*     */   }
/*     */   
/*     */   protected void onRemoval(V value)
/*     */     throws Exception
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\FastThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */