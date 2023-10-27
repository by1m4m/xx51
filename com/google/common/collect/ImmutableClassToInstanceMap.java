/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ @GwtIncompatible
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*  39 */   private static final ImmutableClassToInstanceMap<Object> EMPTY = new ImmutableClassToInstanceMap(
/*  40 */     ImmutableMap.of());
/*     */   
/*     */ 
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */   
/*     */ 
/*     */ 
/*     */   public static <B> ImmutableClassToInstanceMap<B> of()
/*     */   {
/*  49 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <B, T extends B> ImmutableClassToInstanceMap<B> of(Class<T> type, T value)
/*     */   {
/*  58 */     ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, value);
/*  59 */     return new ImmutableClassToInstanceMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <B> Builder<B> builder()
/*     */   {
/*  67 */     return new Builder();
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
/*     */   public static final class Builder<B>
/*     */   {
/*  88 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value)
/*     */     {
/*  96 */       this.mapBuilder.put(key, value);
/*  97 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map)
/*     */     {
/* 109 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/* 110 */         Class<? extends T> type = (Class)entry.getKey();
/* 111 */         T value = entry.getValue();
/* 112 */         this.mapBuilder.put(type, cast(type, value));
/*     */       }
/* 114 */       return this;
/*     */     }
/*     */     
/*     */     private static <B, T extends B> T cast(Class<T> type, B value) {
/* 118 */       return (T)Primitives.wrap(type).cast(value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableClassToInstanceMap<B> build()
/*     */     {
/* 128 */       ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.build();
/* 129 */       if (map.isEmpty()) {
/* 130 */         return ImmutableClassToInstanceMap.of();
/*     */       }
/* 132 */       return new ImmutableClassToInstanceMap(map, null);
/*     */     }
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
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map)
/*     */   {
/* 150 */     if ((map instanceof ImmutableClassToInstanceMap))
/*     */     {
/*     */ 
/* 153 */       ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap)map;
/* 154 */       return cast;
/*     */     }
/* 156 */     return new Builder().putAll(map).build();
/*     */   }
/*     */   
/*     */ 
/*     */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate)
/*     */   {
/* 162 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate()
/*     */   {
/* 167 */     return this.delegate;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T extends B> T getInstance(Class<T> type)
/*     */   {
/* 173 */     return (T)this.delegate.get(Preconditions.checkNotNull(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value)
/*     */   {
/* 186 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 190 */     return isEmpty() ? of() : this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */