/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
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
/*     */ public final class FieldProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedField _annotated;
/*     */   protected final transient Field _field;
/*     */   
/*     */   public FieldProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field)
/*     */   {
/*  37 */     super(propDef, type, typeDeser, contextAnnotations);
/*  38 */     this._annotated = field;
/*  39 */     this._field = field.getAnnotated();
/*     */   }
/*     */   
/*     */   protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser) {
/*  43 */     super(src, deser);
/*  44 */     this._annotated = src._annotated;
/*  45 */     this._field = src._field;
/*     */   }
/*     */   
/*     */   protected FieldProperty(FieldProperty src, PropertyName newName) {
/*  49 */     super(src, newName);
/*  50 */     this._annotated = src._annotated;
/*  51 */     this._field = src._field;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FieldProperty(FieldProperty src)
/*     */   {
/*  59 */     super(src);
/*  60 */     this._annotated = src._annotated;
/*  61 */     Field f = this._annotated.getAnnotated();
/*  62 */     if (f == null) {
/*  63 */       throw new IllegalArgumentException("Missing field (broken JDK (de)serialization?)");
/*     */     }
/*  65 */     this._field = f;
/*     */   }
/*     */   
/*     */   public FieldProperty withName(PropertyName newName)
/*     */   {
/*  70 */     return new FieldProperty(this, newName);
/*     */   }
/*     */   
/*     */   public FieldProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  75 */     return new FieldProperty(this, deser);
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
/*  86 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  89 */   public AnnotatedMember getMember() { return this._annotated; }
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
/* 101 */     Object value = deserialize(jp, ctxt);
/*     */     try {
/* 103 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 105 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 113 */     Object value = deserialize(jp, ctxt);
/*     */     try {
/* 115 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 117 */       _throwAsIOE(e, value);
/*     */     }
/* 119 */     return instance;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 126 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 128 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 136 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 138 */       _throwAsIOE(e, value);
/*     */     }
/* 140 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object readResolve()
/*     */   {
/* 150 */     return new FieldProperty(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\FieldProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */