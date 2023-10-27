/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
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
/*     */ public final class MethodProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _annotated;
/*     */   protected final transient Method _setter;
/*     */   
/*     */   public MethodProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */   {
/*  36 */     super(propDef, type, typeDeser, contextAnnotations);
/*  37 */     this._annotated = method;
/*  38 */     this._setter = method.getAnnotated();
/*     */   }
/*     */   
/*     */   protected MethodProperty(MethodProperty src, JsonDeserializer<?> deser) {
/*  42 */     super(src, deser);
/*  43 */     this._annotated = src._annotated;
/*  44 */     this._setter = src._setter;
/*     */   }
/*     */   
/*     */   protected MethodProperty(MethodProperty src, PropertyName newName) {
/*  48 */     super(src, newName);
/*  49 */     this._annotated = src._annotated;
/*  50 */     this._setter = src._setter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MethodProperty(MethodProperty src, Method m)
/*     */   {
/*  57 */     super(src);
/*  58 */     this._annotated = src._annotated;
/*  59 */     this._setter = m;
/*     */   }
/*     */   
/*     */   public MethodProperty withName(PropertyName newName)
/*     */   {
/*  64 */     return new MethodProperty(this, newName);
/*     */   }
/*     */   
/*     */   public MethodProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  69 */     return new MethodProperty(this, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  80 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  83 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*  95 */     Object value = deserialize(jp, ctxt);
/*     */     try {
/*  97 */       this._setter.invoke(instance, new Object[] { value });
/*     */     } catch (Exception e) {
/*  99 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 107 */     Object value = deserialize(jp, ctxt);
/*     */     try {
/* 109 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 110 */       return result == null ? instance : result;
/*     */     } catch (Exception e) {
/* 112 */       _throwAsIOE(e, value); }
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 121 */       this._setter.invoke(instance, new Object[] { value });
/*     */     } catch (Exception e) {
/* 123 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 131 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 132 */       return result == null ? instance : result;
/*     */     } catch (Exception e) {
/* 134 */       _throwAsIOE(e, value); }
/* 135 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object readResolve()
/*     */   {
/* 146 */     return new MethodProperty(this, this._annotated.getAnnotated());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\MethodProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */