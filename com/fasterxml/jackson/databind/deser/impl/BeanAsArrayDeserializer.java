/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
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
/*     */ public class BeanAsArrayDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanDeserializerBase _delegate;
/*     */   protected final SettableBeanProperty[] _orderedProperties;
/*     */   
/*     */   public BeanAsArrayDeserializer(BeanDeserializerBase delegate, SettableBeanProperty[] ordered)
/*     */   {
/*  48 */     super(delegate);
/*  49 */     this._delegate = delegate;
/*  50 */     this._orderedProperties = ordered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  60 */     return this._delegate.unwrappingDeserializer(unwrapper);
/*     */   }
/*     */   
/*     */   public BeanAsArrayDeserializer withObjectIdReader(ObjectIdReader oir)
/*     */   {
/*  65 */     return new BeanAsArrayDeserializer(this._delegate.withObjectIdReader(oir), this._orderedProperties);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanAsArrayDeserializer withIgnorableProperties(HashSet<String> ignorableProps)
/*     */   {
/*  71 */     return new BeanAsArrayDeserializer(this._delegate.withIgnorableProperties(ignorableProps), this._orderedProperties);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanDeserializerBase asArrayDeserializer()
/*     */   {
/*  77 */     return this;
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  91 */     if (!p.isExpectedStartArrayToken()) {
/*  92 */       return _deserializeFromNonArray(p, ctxt);
/*     */     }
/*  94 */     if (!this._vanillaProcessing) {
/*  95 */       return _deserializeNonVanilla(p, ctxt);
/*     */     }
/*  97 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/*  99 */     p.setCurrentValue(bean);
/*     */     
/* 101 */     SettableBeanProperty[] props = this._orderedProperties;
/* 102 */     int i = 0;
/* 103 */     int propCount = props.length;
/*     */     for (;;) {
/* 105 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 106 */         return bean;
/*     */       }
/* 108 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 111 */       SettableBeanProperty prop = props[i];
/* 112 */       if (prop != null) {
/*     */         try {
/* 114 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 116 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 119 */         p.skipChildren();
/*     */       }
/* 121 */       i++;
/*     */     }
/*     */     
/* 124 */     if (!this._ignoreAllUnknown) {
/* 125 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 128 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 129 */       p.skipChildren();
/*     */     }
/* 131 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 139 */     p.setCurrentValue(bean);
/*     */     
/*     */ 
/*     */ 
/* 143 */     if (this._injectables != null) {
/* 144 */       injectValues(ctxt, bean);
/*     */     }
/* 146 */     SettableBeanProperty[] props = this._orderedProperties;
/* 147 */     int i = 0;
/* 148 */     int propCount = props.length;
/*     */     for (;;) {
/* 150 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 151 */         return bean;
/*     */       }
/* 153 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 156 */       SettableBeanProperty prop = props[i];
/* 157 */       if (prop != null) {
/*     */         try {
/* 159 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 161 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 164 */         p.skipChildren();
/*     */       }
/* 166 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 170 */     if (!this._ignoreAllUnknown) {
/* 171 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 174 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 175 */       p.skipChildren();
/*     */     }
/* 177 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 186 */     return _deserializeFromNonArray(p, ctxt);
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
/*     */   protected Object _deserializeNonVanilla(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 202 */     if (this._nonStandardCreation) {
/* 203 */       return _deserializeWithCreator(p, ctxt);
/*     */     }
/* 205 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 207 */     p.setCurrentValue(bean);
/* 208 */     if (this._injectables != null) {
/* 209 */       injectValues(ctxt, bean);
/*     */     }
/* 211 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 212 */     SettableBeanProperty[] props = this._orderedProperties;
/* 213 */     int i = 0;
/* 214 */     int propCount = props.length;
/*     */     for (;;) {
/* 216 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 217 */         return bean;
/*     */       }
/* 219 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 222 */       SettableBeanProperty prop = props[i];
/* 223 */       i++;
/* 224 */       if ((prop != null) && (
/* 225 */         (activeView == null) || (prop.visibleInView(activeView)))) {
/*     */         try {
/* 227 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 229 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 235 */         p.skipChildren();
/*     */       }
/*     */     }
/* 238 */     if (!this._ignoreAllUnknown) {
/* 239 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 242 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 243 */       p.skipChildren();
/*     */     }
/* 245 */     return bean;
/*     */   }
/*     */   
/*     */   protected Object _deserializeWithCreator(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 251 */     if (this._delegateDeserializer != null) {
/* 252 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/* 254 */     if (this._propertyBasedCreator != null) {
/* 255 */       return _deserializeUsingPropertyBased(p, ctxt);
/*     */     }
/*     */     
/* 258 */     if (this._beanType.isAbstract()) {
/* 259 */       throw JsonMappingException.from(p, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*     */     }
/*     */     
/* 262 */     throw JsonMappingException.from(p, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 278 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 279 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 281 */     SettableBeanProperty[] props = this._orderedProperties;
/* 282 */     int propCount = props.length;
/* 283 */     int i = 0;
/* 284 */     Object bean = null;
/* 286 */     for (; 
/* 286 */         p.nextToken() != JsonToken.END_ARRAY; i++) {
/* 287 */       SettableBeanProperty prop = i < propCount ? props[i] : null;
/* 288 */       if (prop == null) {
/* 289 */         p.skipChildren();
/*     */ 
/*     */ 
/*     */       }
/* 293 */       else if (bean != null) {
/*     */         try {
/* 295 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 297 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       }
/*     */       else {
/* 301 */         String propName = prop.getName();
/*     */         
/* 303 */         SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 304 */         if (creatorProp != null)
/*     */         {
/* 306 */           Object value = creatorProp.deserialize(p, ctxt);
/* 307 */           if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/*     */             try {
/* 309 */               bean = creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 311 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 312 */               continue;
/*     */             }
/*     */             
/* 315 */             p.setCurrentValue(bean);
/*     */             
/*     */ 
/* 318 */             if (bean.getClass() != this._beanType.getRawClass())
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 323 */               throw ctxt.mappingException("Can not support implicit polymorphic deserialization for POJOs-as-Arrays style: nominal type " + this._beanType.getRawClass().getName() + ", actual type " + bean.getClass().getName());
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 330 */         else if (!buffer.readIdProperty(propName))
/*     */         {
/*     */ 
/*     */ 
/* 334 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */         }
/*     */       }
/*     */     }
/* 338 */     if (bean == null) {
/*     */       try {
/* 340 */         bean = creator.build(ctxt, buffer);
/*     */       } catch (Exception e) {
/* 342 */         wrapInstantiationProblem(e, ctxt);
/* 343 */         return null;
/*     */       }
/*     */     }
/* 346 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeFromNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 359 */     throw ctxt.mappingException("Can not deserialize a POJO (of type " + this._beanType.getRawClass().getName() + ") from non-Array representation (token: " + p.getCurrentToken() + "): type/property designed to be serialized as JSON Array");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\BeanAsArrayDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */