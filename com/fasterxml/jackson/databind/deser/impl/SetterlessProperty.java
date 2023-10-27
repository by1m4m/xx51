/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
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
/*     */ 
/*     */ 
/*     */ public final class SetterlessProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _annotated;
/*     */   protected final Method _getter;
/*     */   
/*     */   public SetterlessProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */   {
/*  41 */     super(propDef, type, typeDeser, contextAnnotations);
/*  42 */     this._annotated = method;
/*  43 */     this._getter = method.getAnnotated();
/*     */   }
/*     */   
/*     */   protected SetterlessProperty(SetterlessProperty src, JsonDeserializer<?> deser) {
/*  47 */     super(src, deser);
/*  48 */     this._annotated = src._annotated;
/*  49 */     this._getter = src._getter;
/*     */   }
/*     */   
/*     */   protected SetterlessProperty(SetterlessProperty src, PropertyName newName) {
/*  53 */     super(src, newName);
/*  54 */     this._annotated = src._annotated;
/*  55 */     this._getter = src._getter;
/*     */   }
/*     */   
/*     */   public SetterlessProperty withName(PropertyName newName)
/*     */   {
/*  60 */     return new SetterlessProperty(this, newName);
/*     */   }
/*     */   
/*     */   public SetterlessProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  65 */     return new SetterlessProperty(this, deser);
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
/*  76 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  79 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  92 */     JsonToken t = jp.getCurrentToken();
/*  93 */     if (t == JsonToken.VALUE_NULL)
/*     */     {
/*     */ 
/*     */ 
/*  97 */       return;
/*     */     }
/*     */     
/*     */ 
/* 101 */     if (this._valueTypeDeserializer != null) {
/* 102 */       throw new JsonMappingException("Problem deserializing 'setterless' property (\"" + getName() + "\"): no way to handle typed deser with setterless yet");
/*     */     }
/*     */     
/*     */     Object toModify;
/*     */     
/*     */     try
/*     */     {
/* 109 */       toModify = this._getter.invoke(instance, new Object[0]);
/*     */     } catch (Exception e) {
/* 111 */       _throwAsIOE(e);
/* 112 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */     if (toModify == null) {
/* 120 */       throw new JsonMappingException("Problem deserializing 'setterless' property '" + getName() + "': get method returned null");
/*     */     }
/* 122 */     this._valueDeserializer.deserialize(jp, ctxt, toModify);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 129 */     deserializeAndSet(jp, ctxt, instance);
/* 130 */     return instance;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/* 135 */     throw new UnsupportedOperationException("Should never call 'set' on setterless property");
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 141 */     set(instance, value);
/* 142 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\SetterlessProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */