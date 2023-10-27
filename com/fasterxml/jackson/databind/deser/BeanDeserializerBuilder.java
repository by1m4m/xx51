/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ public class BeanDeserializerBuilder
/*     */ {
/*     */   protected final BeanDescription _beanDesc;
/*     */   protected final boolean _defaultViewInclusion;
/*     */   protected final boolean _caseInsensitivePropertyComparison;
/*  43 */   protected final Map<String, SettableBeanProperty> _properties = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<ValueInjector> _injectables;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashMap<String, SettableBeanProperty> _backRefProperties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashSet<String> _ignorableProps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ValueInstantiator _valueInstantiator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectIdReader _objectIdReader;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableAnyProperty _anySetter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _ignoreAllUnknown;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMethod _buildMethod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonPOJOBuilder.Value _builderConfig;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDeserializerBuilder(BeanDescription beanDesc, DeserializationConfig config)
/*     */   {
/* 102 */     this._beanDesc = beanDesc;
/* 103 */     this._defaultViewInclusion = config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 104 */     this._caseInsensitivePropertyComparison = config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanDeserializerBuilder(BeanDeserializerBuilder src)
/*     */   {
/* 113 */     this._beanDesc = src._beanDesc;
/* 114 */     this._defaultViewInclusion = src._defaultViewInclusion;
/* 115 */     this._caseInsensitivePropertyComparison = src._caseInsensitivePropertyComparison;
/*     */     
/*     */ 
/* 118 */     this._properties.putAll(src._properties);
/* 119 */     this._injectables = _copy(src._injectables);
/* 120 */     this._backRefProperties = _copy(src._backRefProperties);
/*     */     
/* 122 */     this._ignorableProps = src._ignorableProps;
/* 123 */     this._valueInstantiator = src._valueInstantiator;
/* 124 */     this._objectIdReader = src._objectIdReader;
/*     */     
/* 126 */     this._anySetter = src._anySetter;
/* 127 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*     */     
/* 129 */     this._buildMethod = src._buildMethod;
/* 130 */     this._builderConfig = src._builderConfig;
/*     */   }
/*     */   
/*     */   private static HashMap<String, SettableBeanProperty> _copy(HashMap<String, SettableBeanProperty> src) {
/* 134 */     return src == null ? null : new HashMap(src);
/*     */   }
/*     */   
/*     */   private static <T> List<T> _copy(List<T> src)
/*     */   {
/* 139 */     return src == null ? null : new ArrayList(src);
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
/*     */   public void addOrReplaceProperty(SettableBeanProperty prop, boolean allowOverride)
/*     */   {
/* 152 */     this._properties.put(prop.getName(), prop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addProperty(SettableBeanProperty prop)
/*     */   {
/* 162 */     SettableBeanProperty old = (SettableBeanProperty)this._properties.put(prop.getName(), prop);
/* 163 */     if ((old != null) && (old != prop)) {
/* 164 */       throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + this._beanDesc.getType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBackReferenceProperty(String referenceName, SettableBeanProperty prop)
/*     */   {
/* 175 */     if (this._backRefProperties == null) {
/* 176 */       this._backRefProperties = new HashMap(4);
/*     */     }
/* 178 */     this._backRefProperties.put(referenceName, prop);
/*     */     
/* 180 */     if (this._properties != null) {
/* 181 */       this._properties.remove(prop.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInjectable(PropertyName propName, JavaType propType, Annotations contextAnnotations, AnnotatedMember member, Object valueId)
/*     */   {
/* 191 */     if (this._injectables == null) {
/* 192 */       this._injectables = new ArrayList();
/*     */     }
/* 194 */     this._injectables.add(new ValueInjector(propName, propType, contextAnnotations, member, valueId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addIgnorable(String propName)
/*     */   {
/* 204 */     if (this._ignorableProps == null) {
/* 205 */       this._ignorableProps = new HashSet();
/*     */     }
/* 207 */     this._ignorableProps.add(propName);
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
/*     */   public void addCreatorProperty(SettableBeanProperty prop)
/*     */   {
/* 222 */     addProperty(prop);
/*     */   }
/*     */   
/*     */   public void setAnySetter(SettableAnyProperty s)
/*     */   {
/* 227 */     if ((this._anySetter != null) && (s != null)) {
/* 228 */       throw new IllegalStateException("_anySetter already set to non-null");
/*     */     }
/* 230 */     this._anySetter = s;
/*     */   }
/*     */   
/*     */   public void setIgnoreUnknownProperties(boolean ignore) {
/* 234 */     this._ignoreAllUnknown = ignore;
/*     */   }
/*     */   
/*     */   public void setValueInstantiator(ValueInstantiator inst) {
/* 238 */     this._valueInstantiator = inst;
/*     */   }
/*     */   
/*     */   public void setObjectIdReader(ObjectIdReader r) {
/* 242 */     this._objectIdReader = r;
/*     */   }
/*     */   
/*     */   public void setPOJOBuilder(AnnotatedMethod buildMethod, JsonPOJOBuilder.Value config) {
/* 246 */     this._buildMethod = buildMethod;
/* 247 */     this._builderConfig = config;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<SettableBeanProperty> getProperties()
/*     */   {
/* 265 */     return this._properties.values().iterator();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findProperty(PropertyName propertyName) {
/* 269 */     return (SettableBeanProperty)this._properties.get(propertyName.getSimpleName());
/*     */   }
/*     */   
/*     */   public boolean hasProperty(PropertyName propertyName) {
/* 273 */     return findProperty(propertyName) != null;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty removeProperty(PropertyName name) {
/* 277 */     return (SettableBeanProperty)this._properties.remove(name.getSimpleName());
/*     */   }
/*     */   
/*     */   public SettableAnyProperty getAnySetter() {
/* 281 */     return this._anySetter;
/*     */   }
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 285 */     return this._valueInstantiator;
/*     */   }
/*     */   
/*     */   public List<ValueInjector> getInjectables() {
/* 289 */     return this._injectables;
/*     */   }
/*     */   
/*     */   public ObjectIdReader getObjectIdReader() {
/* 293 */     return this._objectIdReader;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getBuildMethod() {
/* 297 */     return this._buildMethod;
/*     */   }
/*     */   
/*     */   public JsonPOJOBuilder.Value getBuilderConfig() {
/* 301 */     return this._builderConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> build()
/*     */   {
/* 319 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 320 */     BeanPropertyMap propertyMap = new BeanPropertyMap(props, this._caseInsensitivePropertyComparison);
/* 321 */     propertyMap.assignIndexes();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 326 */     boolean anyViews = !this._defaultViewInclusion;
/*     */     
/* 328 */     if (!anyViews) {
/* 329 */       for (SettableBeanProperty prop : props) {
/* 330 */         if (prop.hasViews()) {
/* 331 */           anyViews = true;
/* 332 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 338 */     if (this._objectIdReader != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 343 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/* 344 */       propertyMap = propertyMap.withProperty(prop);
/*     */     }
/*     */     
/* 347 */     return new BeanDeserializer(this, this._beanDesc, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
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
/*     */   public AbstractDeserializer buildAbstract()
/*     */   {
/* 360 */     return new AbstractDeserializer(this, this._beanDesc, this._backRefProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> buildBuilderBased(JavaType valueType, String expBuildMethodName)
/*     */   {
/* 371 */     if (this._buildMethod == null) {
/* 372 */       throw new IllegalArgumentException("Builder class " + this._beanDesc.getBeanClass().getName() + " does not have build method '" + expBuildMethodName + "()'");
/*     */     }
/*     */     
/*     */ 
/* 376 */     Class<?> rawBuildType = this._buildMethod.getRawReturnType();
/* 377 */     Class<?> rawValueType = valueType.getRawClass();
/* 378 */     if ((rawBuildType != rawValueType) && (!rawBuildType.isAssignableFrom(rawValueType)) && (!rawValueType.isAssignableFrom(rawBuildType)))
/*     */     {
/*     */ 
/* 381 */       throw new IllegalArgumentException("Build method '" + this._buildMethod.getFullName() + " has bad return type (" + rawBuildType.getName() + "), not compatible with POJO type (" + valueType.getRawClass().getName() + ")");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 386 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 387 */     BeanPropertyMap propertyMap = new BeanPropertyMap(props, this._caseInsensitivePropertyComparison);
/* 388 */     propertyMap.assignIndexes();
/*     */     
/* 390 */     boolean anyViews = !this._defaultViewInclusion;
/*     */     
/* 392 */     if (!anyViews) {
/* 393 */       for (SettableBeanProperty prop : props) {
/* 394 */         if (prop.hasViews()) {
/* 395 */           anyViews = true;
/* 396 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 401 */     if (this._objectIdReader != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 406 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/*     */       
/* 408 */       propertyMap = propertyMap.withProperty(prop);
/*     */     }
/*     */     
/* 411 */     return new BuilderBasedDeserializer(this, this._beanDesc, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */