/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.impl.FailingDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.NullProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ViewMatcher;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class SettableBeanProperty
/*     */   implements BeanProperty, Serializable
/*     */ {
/*  36 */   protected static final JsonDeserializer<Object> MISSING_VALUE_DESERIALIZER = new FailingDeserializer("No _valueDeserializer assigned");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _propName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _type;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _wrapperName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final transient Annotations _contextAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final NullProvider _nullProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyMetadata _metadata;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _managedReferenceName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ViewMatcher _viewMatcher;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   protected int _propertyIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations)
/*     */   {
/* 144 */     this(propDef.getFullName(), type, propDef.getWrapperName(), typeDeser, contextAnnotations, propDef.getMetadata());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected SettableBeanProperty(String propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, boolean isRequired)
/*     */   {
/* 153 */     this(new PropertyName(propName), type, wrapper, typeDeser, contextAnnotations, PropertyMetadata.construct(isRequired, null, null, null));
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
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, PropertyMetadata metadata)
/*     */   {
/* 166 */     if (propName == null) {
/* 167 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 169 */       this._propName = propName.internSimpleName();
/*     */     }
/* 171 */     this._type = type;
/* 172 */     this._wrapperName = wrapper;
/* 173 */     this._metadata = metadata;
/* 174 */     this._contextAnnotations = contextAnnotations;
/* 175 */     this._viewMatcher = null;
/* 176 */     this._nullProvider = null;
/*     */     
/*     */ 
/* 179 */     if (typeDeser != null) {
/* 180 */       typeDeser = typeDeser.forProperty(this);
/*     */     }
/* 182 */     this._valueTypeDeserializer = typeDeser;
/* 183 */     this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyMetadata metadata, JsonDeserializer<Object> valueDeser)
/*     */   {
/* 195 */     if (propName == null) {
/* 196 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 198 */       this._propName = propName.internSimpleName();
/*     */     }
/* 200 */     this._type = type;
/* 201 */     this._wrapperName = null;
/* 202 */     this._metadata = metadata;
/* 203 */     this._contextAnnotations = null;
/* 204 */     this._viewMatcher = null;
/* 205 */     this._nullProvider = null;
/* 206 */     this._valueTypeDeserializer = null;
/* 207 */     this._valueDeserializer = valueDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src)
/*     */   {
/* 215 */     this._propName = src._propName;
/* 216 */     this._type = src._type;
/* 217 */     this._wrapperName = src._wrapperName;
/* 218 */     this._metadata = src._metadata;
/* 219 */     this._contextAnnotations = src._contextAnnotations;
/* 220 */     this._valueDeserializer = src._valueDeserializer;
/* 221 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 222 */     this._nullProvider = src._nullProvider;
/* 223 */     this._managedReferenceName = src._managedReferenceName;
/* 224 */     this._propertyIndex = src._propertyIndex;
/* 225 */     this._viewMatcher = src._viewMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src, JsonDeserializer<?> deser)
/*     */   {
/* 234 */     this._propName = src._propName;
/* 235 */     this._type = src._type;
/* 236 */     this._wrapperName = src._wrapperName;
/* 237 */     this._metadata = src._metadata;
/* 238 */     this._contextAnnotations = src._contextAnnotations;
/* 239 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 240 */     this._managedReferenceName = src._managedReferenceName;
/* 241 */     this._propertyIndex = src._propertyIndex;
/*     */     
/* 243 */     if (deser == null) {
/* 244 */       this._nullProvider = null;
/* 245 */       this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/*     */     } else {
/* 247 */       Object nvl = deser.getNullValue();
/* 248 */       this._nullProvider = (nvl == null ? null : new NullProvider(this._type, nvl));
/* 249 */       this._valueDeserializer = deser;
/*     */     }
/* 251 */     this._viewMatcher = src._viewMatcher;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected SettableBeanProperty(SettableBeanProperty src, String newName) {
/* 256 */     this(src, new PropertyName(newName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src, PropertyName newName)
/*     */   {
/* 264 */     this._propName = newName;
/* 265 */     this._type = src._type;
/* 266 */     this._wrapperName = src._wrapperName;
/* 267 */     this._metadata = src._metadata;
/* 268 */     this._contextAnnotations = src._contextAnnotations;
/* 269 */     this._valueDeserializer = src._valueDeserializer;
/* 270 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 271 */     this._nullProvider = src._nullProvider;
/* 272 */     this._managedReferenceName = src._managedReferenceName;
/* 273 */     this._propertyIndex = src._propertyIndex;
/* 274 */     this._viewMatcher = src._viewMatcher;
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
/*     */   public abstract SettableBeanProperty withValueDeserializer(JsonDeserializer<?> paramJsonDeserializer);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract SettableBeanProperty withName(PropertyName paramPropertyName);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty withSimpleName(String simpleName)
/*     */   {
/* 305 */     PropertyName n = this._propName == null ? new PropertyName(simpleName) : this._propName.withSimpleName(simpleName);
/*     */     
/* 307 */     return n == this._propName ? this : withName(n);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public SettableBeanProperty withName(String simpleName) {
/* 312 */     return withName(new PropertyName(simpleName));
/*     */   }
/*     */   
/*     */   public void setManagedReferenceName(String n) {
/* 316 */     this._managedReferenceName = n;
/*     */   }
/*     */   
/*     */   public void setObjectIdInfo(ObjectIdInfo objectIdInfo) {
/* 320 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */   
/*     */   public void setViews(Class<?>[] views) {
/* 324 */     if (views == null) {
/* 325 */       this._viewMatcher = null;
/*     */     } else {
/* 327 */       this._viewMatcher = ViewMatcher.construct(views);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void assignIndex(int index)
/*     */   {
/* 335 */     if (this._propertyIndex != -1) {
/* 336 */       throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + index);
/*     */     }
/* 338 */     this._propertyIndex = index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 349 */     return this._propName.getSimpleName();
/*     */   }
/*     */   
/*     */   public PropertyName getFullName()
/*     */   {
/* 354 */     return this._propName;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 358 */     return this._metadata.isRequired();
/*     */   }
/*     */   
/* 361 */   public PropertyMetadata getMetadata() { return this._metadata; }
/*     */   
/*     */   public JavaType getType() {
/* 364 */     return this._type;
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 368 */     return this._wrapperName;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */   
/*     */ 
/*     */   public abstract AnnotatedMember getMember();
/*     */   
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */   {
/* 379 */     return this._contextAnnotations.get(acls);
/*     */   }
/*     */   
/*     */ 
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor)
/*     */     throws JsonMappingException
/*     */   {
/* 386 */     if (isRequired()) {
/* 387 */       objectVisitor.property(this);
/*     */     } else {
/* 389 */       objectVisitor.optionalProperty(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> getDeclaringClass()
/*     */   {
/* 400 */     return getMember().getDeclaringClass();
/*     */   }
/*     */   
/* 403 */   public String getManagedReferenceName() { return this._managedReferenceName; }
/*     */   
/* 405 */   public ObjectIdInfo getObjectIdInfo() { return this._objectIdInfo; }
/*     */   
/*     */   public boolean hasValueDeserializer() {
/* 408 */     return (this._valueDeserializer != null) && (this._valueDeserializer != MISSING_VALUE_DESERIALIZER);
/*     */   }
/*     */   
/* 411 */   public boolean hasValueTypeDeserializer() { return this._valueTypeDeserializer != null; }
/*     */   
/*     */   public JsonDeserializer<Object> getValueDeserializer() {
/* 414 */     JsonDeserializer<Object> deser = this._valueDeserializer;
/* 415 */     if (deser == MISSING_VALUE_DESERIALIZER) {
/* 416 */       return null;
/*     */     }
/* 418 */     return deser;
/*     */   }
/*     */   
/* 421 */   public TypeDeserializer getValueTypeDeserializer() { return this._valueTypeDeserializer; }
/*     */   
/*     */   public boolean visibleInView(Class<?> activeView) {
/* 424 */     return (this._viewMatcher == null) || (this._viewMatcher.isVisibleForView(activeView));
/*     */   }
/*     */   
/* 427 */   public boolean hasViews() { return this._viewMatcher != null; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPropertyIndex()
/*     */   {
/* 436 */     return this._propertyIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCreatorIndex()
/*     */   {
/* 444 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getInjectableValueId()
/*     */   {
/* 450 */     return null;
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
/*     */ 
/*     */ 
/*     */   public abstract void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
/*     */     throws IOException;
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
/*     */   public abstract Object deserializeSetAndReturn(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
/*     */     throws IOException;
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
/*     */   public abstract void set(Object paramObject1, Object paramObject2)
/*     */     throws IOException;
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
/*     */   public abstract Object setAndReturn(Object paramObject1, Object paramObject2)
/*     */     throws IOException;
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
/*     */   public final Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 515 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 517 */     if (t == JsonToken.VALUE_NULL) {
/* 518 */       return this._nullProvider == null ? null : this._nullProvider.nullValue(ctxt);
/*     */     }
/* 520 */     if (this._valueTypeDeserializer != null) {
/* 521 */       return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 523 */     return this._valueDeserializer.deserialize(p, ctxt);
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
/*     */ 
/*     */   protected void _throwAsIOE(Exception e, Object value)
/*     */     throws IOException
/*     */   {
/* 538 */     if ((e instanceof IllegalArgumentException)) {
/* 539 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 540 */       StringBuilder msg = new StringBuilder("Problem deserializing property '").append(getName());
/* 541 */       msg.append("' (expected type: ").append(getType());
/* 542 */       msg.append("; actual type: ").append(actType).append(")");
/* 543 */       String origMsg = e.getMessage();
/* 544 */       if (origMsg != null) {
/* 545 */         msg.append(", problem: ").append(origMsg);
/*     */       } else {
/* 547 */         msg.append(" (no error message provided)");
/*     */       }
/* 549 */       throw new JsonMappingException(msg.toString(), null, e);
/*     */     }
/* 551 */     _throwAsIOE(e);
/*     */   }
/*     */   
/*     */   protected IOException _throwAsIOE(Exception e) throws IOException
/*     */   {
/* 556 */     if ((e instanceof IOException)) {
/* 557 */       throw ((IOException)e);
/*     */     }
/* 559 */     if ((e instanceof RuntimeException)) {
/* 560 */       throw ((RuntimeException)e);
/*     */     }
/*     */     
/* 563 */     Throwable th = e;
/* 564 */     while (th.getCause() != null) {
/* 565 */       th = th.getCause();
/*     */     }
/* 567 */     throw new JsonMappingException(th.getMessage(), null, th);
/*     */   }
/*     */   
/* 570 */   public String toString() { return "[property '" + getName() + "']"; }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\SettableBeanProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */