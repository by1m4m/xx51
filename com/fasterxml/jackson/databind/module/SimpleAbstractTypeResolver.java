/*    */ package com.fasterxml.jackson.databind.module;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.AbstractTypeResolver;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.ClassKey;
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleAbstractTypeResolver
/*    */   extends AbstractTypeResolver
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8635483102371490919L;
/* 38 */   protected final HashMap<ClassKey, Class<?>> _mappings = new HashMap();
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
/*    */   public <T> SimpleAbstractTypeResolver addMapping(Class<T> superType, Class<? extends T> subType)
/*    */   {
/* 55 */     if (superType == subType) {
/* 56 */       throw new IllegalArgumentException("Can not add mapping from class to itself");
/*    */     }
/* 58 */     if (!superType.isAssignableFrom(subType)) {
/* 59 */       throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " to " + subType.getName() + ", as latter is not a subtype of former");
/*    */     }
/*    */     
/* 62 */     if (!Modifier.isAbstract(superType.getModifiers())) {
/* 63 */       throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " since it is not abstract");
/*    */     }
/*    */     
/* 66 */     this._mappings.put(new ClassKey(superType), subType);
/* 67 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public JavaType findTypeMapping(DeserializationConfig config, JavaType type)
/*    */   {
/* 74 */     Class<?> src = type.getRawClass();
/* 75 */     Class<?> dst = (Class)this._mappings.get(new ClassKey(src));
/* 76 */     if (dst == null) {
/* 77 */       return null;
/*    */     }
/* 79 */     return type.narrowBy(dst);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JavaType resolveAbstractType(DeserializationConfig config, JavaType type)
/*    */   {
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\module\SimpleAbstractTypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */