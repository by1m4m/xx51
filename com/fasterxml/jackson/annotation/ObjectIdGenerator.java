/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public abstract class ObjectIdGenerator<T>
/*     */   implements Serializable
/*     */ {
/*     */   public abstract Class<?> getScope();
/*     */   
/*     */   public abstract boolean canUseFor(ObjectIdGenerator<?> paramObjectIdGenerator);
/*     */   
/*     */   public boolean maySerializeAsObject()
/*     */   {
/*  49 */     return false;
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
/*     */   public boolean isValidReferencePropertyName(String name, Object parser)
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ObjectIdGenerator<T> forScope(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ObjectIdGenerator<T> newForSerialization(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract IdKey key(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T generateId(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class IdKey
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Class<?> type;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Class<?> scope;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Object key;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final int hashCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public IdKey(Class<?> type, Class<?> scope, Object key)
/*     */     {
/* 154 */       this.type = type;
/* 155 */       this.scope = scope;
/* 156 */       this.key = key;
/*     */       
/* 158 */       int h = key.hashCode() + type.getName().hashCode();
/* 159 */       if (scope != null) {
/* 160 */         h ^= scope.getName().hashCode();
/*     */       }
/* 162 */       this.hashCode = h;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 166 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 171 */       if (o == this) return true;
/* 172 */       if (o == null) return false;
/* 173 */       if (o.getClass() != getClass()) return false;
/* 174 */       IdKey other = (IdKey)o;
/* 175 */       return (other.key.equals(this.key)) && (other.type == this.type) && (other.scope == this.scope);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\ObjectIdGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */