/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectIdValueProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   
/*     */   public ObjectIdValueProperty(ObjectIdReader objectIdReader, PropertyMetadata metadata)
/*     */   {
/*  26 */     super(objectIdReader.propertyName, objectIdReader.getIdType(), metadata, objectIdReader.getDeserializer());
/*     */     
/*  28 */     this._objectIdReader = objectIdReader;
/*     */   }
/*     */   
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  33 */     super(src, deser);
/*  34 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
/*  39 */     super(src, newName);
/*  40 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, String newName) {
/*  45 */     this(src, new PropertyName(newName));
/*     */   }
/*     */   
/*     */   public ObjectIdValueProperty withName(PropertyName newName)
/*     */   {
/*  50 */     return new ObjectIdValueProperty(this, newName);
/*     */   }
/*     */   
/*     */   public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  55 */     return new ObjectIdValueProperty(this, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  62 */     return null;
/*     */   }
/*     */   
/*  65 */   public AnnotatedMember getMember() { return null; }
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
/*  77 */     deserializeSetAndReturn(jp, ctxt, instance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*  85 */     Object id = this._valueDeserializer.deserialize(jp, ctxt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     if (id == null) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*  97 */     roid.bindItem(instance);
/*     */     
/*  99 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 100 */     if (idProp != null) {
/* 101 */       return idProp.setAndReturn(instance, id);
/*     */     }
/* 103 */     return instance;
/*     */   }
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException
/*     */   {
/* 108 */     setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 114 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 115 */     if (idProp == null) {
/* 116 */       throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
/*     */     }
/*     */     
/* 119 */     return idProp.setAndReturn(instance, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdValueProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */