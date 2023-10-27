/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
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
/*     */ public final class PropertyBasedCreator
/*     */ {
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final HashMap<String, SettableBeanProperty> _properties;
/*     */   protected final int _propertyCount;
/*     */   protected final Object[] _defaultValues;
/*     */   protected final SettableBeanProperty[] _propertiesWithInjectables;
/*     */   
/*     */   protected PropertyBasedCreator(ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps, Object[] defaultValues)
/*     */   {
/*  62 */     this._valueInstantiator = valueInstantiator;
/*  63 */     this._properties = new HashMap();
/*  64 */     SettableBeanProperty[] propertiesWithInjectables = null;
/*  65 */     int len = creatorProps.length;
/*  66 */     this._propertyCount = len;
/*  67 */     for (int i = 0; i < len; i++) {
/*  68 */       SettableBeanProperty prop = creatorProps[i];
/*  69 */       this._properties.put(prop.getName(), prop);
/*  70 */       Object injectableValueId = prop.getInjectableValueId();
/*  71 */       if (injectableValueId != null) {
/*  72 */         if (propertiesWithInjectables == null) {
/*  73 */           propertiesWithInjectables = new SettableBeanProperty[len];
/*     */         }
/*  75 */         propertiesWithInjectables[i] = prop;
/*     */       }
/*     */     }
/*  78 */     this._defaultValues = defaultValues;
/*  79 */     this._propertiesWithInjectables = propertiesWithInjectables;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyBasedCreator construct(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] srcProps)
/*     */     throws JsonMappingException
/*     */   {
/*  90 */     int len = srcProps.length;
/*  91 */     SettableBeanProperty[] creatorProps = new SettableBeanProperty[len];
/*  92 */     Object[] defaultValues = null;
/*  93 */     for (int i = 0; i < len; i++) {
/*  94 */       SettableBeanProperty prop = srcProps[i];
/*  95 */       if (!prop.hasValueDeserializer()) {
/*  96 */         prop = prop.withValueDeserializer(ctxt.findContextualValueDeserializer(prop.getType(), prop));
/*     */       }
/*  98 */       creatorProps[i] = prop;
/*     */       
/*     */ 
/* 101 */       JsonDeserializer<?> deser = prop.getValueDeserializer();
/* 102 */       Object nullValue = deser == null ? null : deser.getNullValue();
/* 103 */       if ((nullValue == null) && (prop.getType().isPrimitive())) {
/* 104 */         nullValue = ClassUtil.defaultValue(prop.getType().getRawClass());
/*     */       }
/* 106 */       if (nullValue != null) {
/* 107 */         if (defaultValues == null) {
/* 108 */           defaultValues = new Object[len];
/*     */         }
/* 110 */         defaultValues[i] = nullValue;
/*     */       }
/*     */     }
/* 113 */     return new PropertyBasedCreator(valueInstantiator, creatorProps, defaultValues);
/*     */   }
/*     */   
/*     */   public void assignDeserializer(SettableBeanProperty prop, JsonDeserializer<Object> deser) {
/* 117 */     prop = prop.withValueDeserializer(deser);
/* 118 */     this._properties.put(prop.getName(), prop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<SettableBeanProperty> properties()
/*     */   {
/* 128 */     return this._properties.values();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(String name) {
/* 132 */     return (SettableBeanProperty)this._properties.get(name);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(int propertyIndex) {
/* 136 */     for (SettableBeanProperty prop : this._properties.values()) {
/* 137 */       if (prop.getPropertyIndex() == propertyIndex) {
/* 138 */         return prop;
/*     */       }
/*     */     }
/* 141 */     return null;
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
/*     */   public PropertyValueBuffer startBuilding(JsonParser jp, DeserializationContext ctxt, ObjectIdReader oir)
/*     */   {
/* 158 */     PropertyValueBuffer buffer = new PropertyValueBuffer(jp, ctxt, this._propertyCount, oir);
/* 159 */     if (this._propertiesWithInjectables != null) {
/* 160 */       buffer.inject(this._propertiesWithInjectables);
/*     */     }
/* 162 */     return buffer;
/*     */   }
/*     */   
/*     */   public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException
/*     */   {
/* 167 */     Object bean = this._valueInstantiator.createFromObjectWith(ctxt, buffer.getParameters(this._defaultValues));
/*     */     
/* 169 */     bean = buffer.handleIdValue(ctxt, bean);
/*     */     
/*     */ 
/* 172 */     for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
/* 173 */       pv.assign(bean);
/*     */     }
/* 175 */     return bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyBasedCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */