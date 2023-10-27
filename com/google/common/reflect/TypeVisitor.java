/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import java.lang.reflect.WildcardType;
/*    */ import java.util.Set;
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
/*    */ abstract class TypeVisitor
/*    */ {
/* 58 */   private final Set<Type> visited = Sets.newHashSet();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void visit(Type... types)
/*    */   {
/* 65 */     for (Type type : types) {
/* 66 */       if ((type != null) && (this.visited.add(type)))
/*    */       {
/*    */ 
/*    */ 
/* 70 */         boolean succeeded = false;
/*    */         try {
/* 72 */           if ((type instanceof TypeVariable)) {
/* 73 */             visitTypeVariable((TypeVariable)type);
/* 74 */           } else if ((type instanceof WildcardType)) {
/* 75 */             visitWildcardType((WildcardType)type);
/* 76 */           } else if ((type instanceof ParameterizedType)) {
/* 77 */             visitParameterizedType((ParameterizedType)type);
/* 78 */           } else if ((type instanceof Class)) {
/* 79 */             visitClass((Class)type);
/* 80 */           } else if ((type instanceof GenericArrayType)) {
/* 81 */             visitGenericArrayType((GenericArrayType)type);
/*    */           } else {
/* 83 */             throw new AssertionError("Unknown type: " + type);
/*    */           }
/* 85 */           succeeded = true;
/*    */         } finally {
/* 87 */           if (!succeeded) {
/* 88 */             this.visited.remove(type);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   void visitClass(Class<?> t) {}
/*    */   
/*    */   void visitGenericArrayType(GenericArrayType t) {}
/*    */   
/*    */   void visitParameterizedType(ParameterizedType t) {}
/*    */   
/*    */   void visitTypeVariable(TypeVariable<?> t) {}
/*    */   
/*    */   void visitWildcardType(WildcardType t) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\reflect\TypeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */