/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectIdGenerators
/*     */ {
/*     */   private static abstract class Base<T>
/*     */     extends ObjectIdGenerator<T>
/*     */   {
/*     */     protected final Class<?> _scope;
/*     */     
/*     */     protected Base(Class<?> scope)
/*     */     {
/*  19 */       this._scope = scope;
/*     */     }
/*     */     
/*     */     public final Class<?> getScope()
/*     */     {
/*  24 */       return this._scope;
/*     */     }
/*     */     
/*     */     public boolean canUseFor(ObjectIdGenerator<?> gen)
/*     */     {
/*  29 */       return (gen.getClass() == getClass()) && (gen.getScope() == this._scope);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract T generateId(Object paramObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class None
/*     */     extends ObjectIdGenerator<Object>
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class PropertyGenerator
/*     */     extends ObjectIdGenerators.Base<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected PropertyGenerator(Class<?> scope)
/*     */     {
/*  62 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class IntSequenceGenerator
/*     */     extends ObjectIdGenerators.Base<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected transient int _nextValue;
/*     */     
/*  75 */     public IntSequenceGenerator() { this(Object.class, -1); }
/*     */     
/*  77 */     public IntSequenceGenerator(Class<?> scope, int fv) { super();
/*  78 */       this._nextValue = fv;
/*     */     }
/*     */     
/*  81 */     protected int initialValue() { return 1; }
/*     */     
/*     */     public ObjectIdGenerator<Integer> forScope(Class<?> scope)
/*     */     {
/*  85 */       return this._scope == scope ? this : new IntSequenceGenerator(scope, this._nextValue);
/*     */     }
/*     */     
/*     */     public ObjectIdGenerator<Integer> newForSerialization(Object context)
/*     */     {
/*  90 */       return new IntSequenceGenerator(this._scope, initialValue());
/*     */     }
/*     */     
/*     */     public ObjectIdGenerator.IdKey key(Object key)
/*     */     {
/*  95 */       return new ObjectIdGenerator.IdKey(getClass(), this._scope, key);
/*     */     }
/*     */     
/*     */     public Integer generateId(Object forPojo)
/*     */     {
/* 100 */       int id = this._nextValue;
/* 101 */       this._nextValue += 1;
/* 102 */       return Integer.valueOf(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class UUIDGenerator
/*     */     extends ObjectIdGenerators.Base<UUID>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */     public UUIDGenerator() { this(Object.class); }
/*     */     
/* 121 */     private UUIDGenerator(Class<?> scope) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ObjectIdGenerator<UUID> forScope(Class<?> scope)
/*     */     {
/* 129 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ObjectIdGenerator<UUID> newForSerialization(Object context)
/*     */     {
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public UUID generateId(Object forPojo)
/*     */     {
/* 142 */       return UUID.randomUUID();
/*     */     }
/*     */     
/*     */     public ObjectIdGenerator.IdKey key(Object key)
/*     */     {
/* 147 */       return new ObjectIdGenerator.IdKey(getClass(), null, key);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean canUseFor(ObjectIdGenerator<?> gen)
/*     */     {
/* 155 */       return gen.getClass() == getClass();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\ObjectIdGenerators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */