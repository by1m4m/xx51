/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public final class ManagedReferenceProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _referenceName;
/*     */   protected final boolean _isContainer;
/*     */   protected final SettableBeanProperty _managedProperty;
/*     */   protected final SettableBeanProperty _backProperty;
/*     */   
/*     */   public ManagedReferenceProperty(SettableBeanProperty forward, String refName, SettableBeanProperty backward, Annotations contextAnnotations, boolean isContainer)
/*     */   {
/*  42 */     super(forward.getFullName(), forward.getType(), forward.getWrapperName(), forward.getValueTypeDeserializer(), contextAnnotations, forward.getMetadata());
/*     */     
/*     */ 
/*  45 */     this._referenceName = refName;
/*  46 */     this._managedProperty = forward;
/*  47 */     this._backProperty = backward;
/*  48 */     this._isContainer = isContainer;
/*     */   }
/*     */   
/*     */   protected ManagedReferenceProperty(ManagedReferenceProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  53 */     super(src, deser);
/*  54 */     this._referenceName = src._referenceName;
/*  55 */     this._isContainer = src._isContainer;
/*  56 */     this._managedProperty = src._managedProperty;
/*  57 */     this._backProperty = src._backProperty;
/*     */   }
/*     */   
/*     */   protected ManagedReferenceProperty(ManagedReferenceProperty src, PropertyName newName) {
/*  61 */     super(src, newName);
/*  62 */     this._referenceName = src._referenceName;
/*  63 */     this._isContainer = src._isContainer;
/*  64 */     this._managedProperty = src._managedProperty;
/*  65 */     this._backProperty = src._backProperty;
/*     */   }
/*     */   
/*     */   public ManagedReferenceProperty withName(PropertyName newName)
/*     */   {
/*  70 */     return new ManagedReferenceProperty(this, newName);
/*     */   }
/*     */   
/*     */   public ManagedReferenceProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  75 */     return new ManagedReferenceProperty(this, deser);
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
/*  86 */     return this._managedProperty.getAnnotation(acls);
/*     */   }
/*     */   
/*  89 */   public AnnotatedMember getMember() { return this._managedProperty.getMember(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 100 */     set(instance, this._managedProperty.deserialize(jp, ctxt));
/*     */   }
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 106 */     return setAndReturn(instance, deserialize(jp, ctxt));
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/* 111 */     setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 120 */     if (value != null) {
/* 121 */       if (this._isContainer) {
/* 122 */         if ((value instanceof Object[])) {
/* 123 */           for (Object ob : (Object[])value) {
/* 124 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/* 126 */         } else if ((value instanceof Collection)) {
/* 127 */           for (Object ob : (Collection)value) {
/* 128 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/* 130 */         } else if ((value instanceof Map)) {
/* 131 */           for (Object ob : ((Map)value).values()) {
/* 132 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/*     */         } else {
/* 135 */           throw new IllegalStateException("Unsupported container type (" + value.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
/*     */         }
/*     */       }
/*     */       else {
/* 139 */         this._backProperty.set(value, instance);
/*     */       }
/*     */     }
/*     */     
/* 143 */     return this._managedProperty.setAndReturn(instance, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\ManagedReferenceProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */