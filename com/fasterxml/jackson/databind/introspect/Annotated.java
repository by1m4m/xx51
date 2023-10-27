/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Annotated
/*    */ {
/*    */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*    */   
/*    */   public final <A extends Annotation> boolean hasAnnotation(Class<A> acls)
/*    */   {
/* 23 */     return getAnnotation(acls) != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract Annotated withAnnotations(AnnotationMap paramAnnotationMap);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Annotated withFallBackAnnotationsFrom(Annotated annotated)
/*    */   {
/* 37 */     return withAnnotations(AnnotationMap.merge(getAllAnnotations(), annotated.getAllAnnotations()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract AnnotatedElement getAnnotated();
/*    */   
/*    */ 
/*    */   protected abstract int getModifiers();
/*    */   
/*    */ 
/*    */   public final boolean isPublic()
/*    */   {
/* 50 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract String getName();
/*    */   
/*    */ 
/*    */   public JavaType getType(TypeBindings context)
/*    */   {
/* 60 */     return context.resolveType(getGenericType());
/*    */   }
/*    */   
/*    */   public abstract Type getGenericType();
/*    */   
/*    */   public abstract Class<?> getRawType();
/*    */   
/*    */   public abstract Iterable<Annotation> annotations();
/*    */   
/*    */   protected abstract AnnotationMap getAllAnnotations();
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   public abstract String toString();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\Annotated.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */