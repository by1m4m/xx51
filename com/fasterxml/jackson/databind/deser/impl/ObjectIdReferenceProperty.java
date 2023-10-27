/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ public class ObjectIdReferenceProperty extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final SettableBeanProperty _forward;
/*     */   
/*     */   public ObjectIdReferenceProperty(SettableBeanProperty forward, ObjectIdInfo objectIdInfo)
/*     */   {
/*  21 */     super(forward);
/*  22 */     this._forward = forward;
/*  23 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  28 */     super(src, deser);
/*  29 */     this._forward = src._forward;
/*  30 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, PropertyName newName)
/*     */   {
/*  35 */     super(src, newName);
/*  36 */     this._forward = src._forward;
/*  37 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  42 */     return new ObjectIdReferenceProperty(this, deser);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName)
/*     */   {
/*  47 */     return new ObjectIdReferenceProperty(this, newName);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  52 */     return this._forward.getAnnotation(acls);
/*     */   }
/*     */   
/*     */   public com.fasterxml.jackson.databind.introspect.AnnotatedMember getMember()
/*     */   {
/*  57 */     return this._forward.getMember();
/*     */   }
/*     */   
/*     */   public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException
/*     */   {
/*  62 */     deserializeSetAndReturn(jp, ctxt, instance);
/*     */   }
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  70 */       return setAndReturn(instance, deserialize(jp, ctxt));
/*     */     } catch (UnresolvedForwardReference reference) {
/*  72 */       boolean usingIdentityInfo = (this._objectIdInfo != null) || (this._valueDeserializer.getObjectIdReader() != null);
/*  73 */       if (!usingIdentityInfo) {
/*  74 */         throw JsonMappingException.from(jp, "Unresolved forward reference but no identity info.", reference);
/*     */       }
/*  76 */       reference.getRoid().appendReferring(new PropertyReferring(this, reference, this._type.getRawClass(), instance)); }
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   public void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/*  83 */     this._forward.set(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/*  88 */     return this._forward.setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public static final class PropertyReferring extends ReadableObjectId.Referring
/*     */   {
/*     */     private final ObjectIdReferenceProperty _parent;
/*     */     public final Object _pojo;
/*     */     
/*     */     public PropertyReferring(ObjectIdReferenceProperty parent, UnresolvedForwardReference ref, Class<?> type, Object ob)
/*     */     {
/*  98 */       super(type);
/*  99 */       this._parent = parent;
/* 100 */       this._pojo = ob;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value)
/*     */       throws IOException
/*     */     {
/* 106 */       if (!hasId(id)) {
/* 107 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */       }
/*     */       
/* 110 */       this._parent.set(this._pojo, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdReferenceProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */