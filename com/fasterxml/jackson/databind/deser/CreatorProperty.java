/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreatorProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedParameter _annotated;
/*     */   protected final Object _injectableValueId;
/*     */   protected final int _creatorIndex;
/*     */   protected final SettableBeanProperty _fallbackSetter;
/*     */   
/*     */   public CreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, PropertyMetadata metadata)
/*     */   {
/*  82 */     super(name, type, wrapperName, typeDeser, contextAnnotations, metadata);
/*  83 */     this._annotated = param;
/*  84 */     this._creatorIndex = index;
/*  85 */     this._injectableValueId = injectableValueId;
/*  86 */     this._fallbackSetter = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public CreatorProperty(String name, JavaType type, PropertyName wrapperName, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, boolean isRequired)
/*     */   {
/*  96 */     this(new PropertyName(name), type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, PropertyMetadata.construct(isRequired, null, null, null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CreatorProperty(CreatorProperty src, PropertyName newName)
/*     */   {
/* 105 */     super(src, newName);
/* 106 */     this._annotated = src._annotated;
/* 107 */     this._creatorIndex = src._creatorIndex;
/* 108 */     this._injectableValueId = src._injectableValueId;
/* 109 */     this._fallbackSetter = src._fallbackSetter;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected CreatorProperty(CreatorProperty src, String newName) {
/* 114 */     this(src, new PropertyName(newName));
/*     */   }
/*     */   
/*     */   protected CreatorProperty(CreatorProperty src, JsonDeserializer<?> deser) {
/* 118 */     super(src, deser);
/* 119 */     this._annotated = src._annotated;
/* 120 */     this._creatorIndex = src._creatorIndex;
/* 121 */     this._injectableValueId = src._injectableValueId;
/* 122 */     this._fallbackSetter = src._fallbackSetter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected CreatorProperty(CreatorProperty src, SettableBeanProperty fallbackSetter)
/*     */   {
/* 129 */     super(src);
/* 130 */     this._annotated = src._annotated;
/* 131 */     this._creatorIndex = src._creatorIndex;
/* 132 */     this._injectableValueId = src._injectableValueId;
/* 133 */     this._fallbackSetter = fallbackSetter;
/*     */   }
/*     */   
/*     */   public CreatorProperty withName(PropertyName newName)
/*     */   {
/* 138 */     return new CreatorProperty(this, newName);
/*     */   }
/*     */   
/*     */   public CreatorProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/* 143 */     return new CreatorProperty(this, deser);
/*     */   }
/*     */   
/*     */   public CreatorProperty withFallbackSetter(SettableBeanProperty fallbackSetter) {
/* 147 */     return new CreatorProperty(this, fallbackSetter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object findInjectableValue(DeserializationContext context, Object beanInstance)
/*     */   {
/* 156 */     if (this._injectableValueId == null) {
/* 157 */       throw new IllegalStateException("Property '" + getName() + "' (type " + getClass().getName() + ") has no injectable value id configured");
/*     */     }
/*     */     
/* 160 */     return context.findInjectableValue(this._injectableValueId, this, beanInstance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void inject(DeserializationContext context, Object beanInstance)
/*     */     throws IOException
/*     */   {
/* 169 */     set(beanInstance, findInjectableValue(context, beanInstance));
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
/* 180 */     if (this._annotated == null) {
/* 181 */       return null;
/*     */     }
/* 183 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/* 186 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */   public int getCreatorIndex() {
/* 189 */     return this._creatorIndex;
/*     */   }
/*     */   
/*     */ 
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
/* 203 */     set(instance, deserialize(jp, ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 211 */     return setAndReturn(instance, deserialize(jp, ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 220 */     if (this._fallbackSetter == null) {
/* 221 */       throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
/*     */     }
/*     */     
/* 224 */     this._fallbackSetter.set(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 230 */     if (this._fallbackSetter == null) {
/* 231 */       throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
/*     */     }
/*     */     
/* 234 */     return this._fallbackSetter.setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public Object getInjectableValueId()
/*     */   {
/* 239 */     return this._injectableValueId;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 243 */     return "[creator property, name '" + getName() + "'; inject id '" + this._injectableValueId + "']";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\CreatorProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */