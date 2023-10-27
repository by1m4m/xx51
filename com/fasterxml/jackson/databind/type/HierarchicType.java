/*    */ package com.fasterxml.jackson.databind.type;
/*    */ 
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HierarchicType
/*    */ {
/*    */   protected final Type _actualType;
/*    */   protected final Class<?> _rawClass;
/*    */   protected final ParameterizedType _genericType;
/*    */   protected HierarchicType _superType;
/*    */   protected HierarchicType _subType;
/*    */   
/*    */   public HierarchicType(Type type)
/*    */   {
/* 30 */     this._actualType = type;
/* 31 */     if ((type instanceof Class)) {
/* 32 */       this._rawClass = ((Class)type);
/* 33 */       this._genericType = null;
/* 34 */     } else if ((type instanceof ParameterizedType)) {
/* 35 */       this._genericType = ((ParameterizedType)type);
/* 36 */       this._rawClass = ((Class)this._genericType.getRawType());
/*    */     } else {
/* 38 */       throw new IllegalArgumentException("Type " + type.getClass().getName() + " can not be used to construct HierarchicType");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private HierarchicType(Type actualType, Class<?> rawClass, ParameterizedType genericType, HierarchicType superType, HierarchicType subType)
/*    */   {
/* 45 */     this._actualType = actualType;
/* 46 */     this._rawClass = rawClass;
/* 47 */     this._genericType = genericType;
/* 48 */     this._superType = superType;
/* 49 */     this._subType = subType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HierarchicType deepCloneWithoutSubtype()
/*    */   {
/* 58 */     HierarchicType sup = this._superType == null ? null : this._superType.deepCloneWithoutSubtype();
/* 59 */     HierarchicType result = new HierarchicType(this._actualType, this._rawClass, this._genericType, sup, null);
/* 60 */     if (sup != null) {
/* 61 */       sup.setSubType(result);
/*    */     }
/* 63 */     return result;
/*    */   }
/*    */   
/* 66 */   public void setSuperType(HierarchicType sup) { this._superType = sup; }
/* 67 */   public final HierarchicType getSuperType() { return this._superType; }
/* 68 */   public void setSubType(HierarchicType sub) { this._subType = sub; }
/* 69 */   public final HierarchicType getSubType() { return this._subType; }
/*    */   
/* 71 */   public final boolean isGeneric() { return this._genericType != null; }
/* 72 */   public final ParameterizedType asGeneric() { return this._genericType; }
/*    */   
/* 74 */   public final Class<?> getRawClass() { return this._rawClass; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 78 */     if (this._genericType != null) {
/* 79 */       return this._genericType.toString();
/*    */     }
/* 81 */     return this._rawClass.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\HierarchicType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */