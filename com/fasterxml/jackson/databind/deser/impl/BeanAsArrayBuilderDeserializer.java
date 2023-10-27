/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class BeanAsArrayBuilderDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanDeserializerBase _delegate;
/*     */   protected final SettableBeanProperty[] _orderedProperties;
/*     */   protected final AnnotatedMethod _buildMethod;
/*     */   
/*     */   public BeanAsArrayBuilderDeserializer(BeanDeserializerBase delegate, SettableBeanProperty[] ordered, AnnotatedMethod buildMethod)
/*     */   {
/*  45 */     super(delegate);
/*  46 */     this._delegate = delegate;
/*  47 */     this._orderedProperties = ordered;
/*  48 */     this._buildMethod = buildMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  58 */     return this._delegate.unwrappingDeserializer(unwrapper);
/*     */   }
/*     */   
/*     */   public BeanAsArrayBuilderDeserializer withObjectIdReader(ObjectIdReader oir)
/*     */   {
/*  63 */     return new BeanAsArrayBuilderDeserializer(this._delegate.withObjectIdReader(oir), this._orderedProperties, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanAsArrayBuilderDeserializer withIgnorableProperties(HashSet<String> ignorableProps)
/*     */   {
/*  69 */     return new BeanAsArrayBuilderDeserializer(this._delegate.withIgnorableProperties(ignorableProps), this._orderedProperties, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanAsArrayBuilderDeserializer asArrayDeserializer()
/*     */   {
/*  75 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object finishBuild(DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  88 */       return this._buildMethod.getMember().invoke(builder, new Object[0]);
/*     */     } catch (Exception e) {
/*  90 */       wrapInstantiationProblem(e, ctxt); }
/*  91 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 100 */     if (!p.isExpectedStartArrayToken()) {
/* 101 */       return finishBuild(ctxt, _deserializeFromNonArray(p, ctxt));
/*     */     }
/* 103 */     if (!this._vanillaProcessing) {
/* 104 */       return finishBuild(ctxt, _deserializeNonVanilla(p, ctxt));
/*     */     }
/* 106 */     Object builder = this._valueInstantiator.createUsingDefault(ctxt);
/* 107 */     SettableBeanProperty[] props = this._orderedProperties;
/* 108 */     int i = 0;
/* 109 */     int propCount = props.length;
/*     */     for (;;) {
/* 111 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 112 */         return finishBuild(ctxt, builder);
/*     */       }
/* 114 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 117 */       SettableBeanProperty prop = props[i];
/* 118 */       if (prop != null) {
/*     */         try {
/* 120 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 122 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 125 */         p.skipChildren();
/*     */       }
/* 127 */       i++;
/*     */     }
/*     */     
/* 130 */     if (!this._ignoreAllUnknown) {
/* 131 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 134 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 135 */       p.skipChildren();
/*     */     }
/* 137 */     return finishBuild(ctxt, builder);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/* 147 */     if (this._injectables != null) {
/* 148 */       injectValues(ctxt, builder);
/*     */     }
/* 150 */     SettableBeanProperty[] props = this._orderedProperties;
/* 151 */     int i = 0;
/* 152 */     int propCount = props.length;
/*     */     for (;;) {
/* 154 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 155 */         return finishBuild(ctxt, builder);
/*     */       }
/* 157 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 160 */       SettableBeanProperty prop = props[i];
/* 161 */       if (prop != null) {
/*     */         try {
/* 163 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 165 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 168 */         p.skipChildren();
/*     */       }
/* 170 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 174 */     if (!this._ignoreAllUnknown) {
/* 175 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 178 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 179 */       p.skipChildren();
/*     */     }
/* 181 */     return finishBuild(ctxt, builder);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 188 */     return _deserializeFromNonArray(p, ctxt);
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
/*     */   protected Object _deserializeNonVanilla(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 207 */     if (this._nonStandardCreation) {
/* 208 */       return _deserializeWithCreator(p, ctxt);
/*     */     }
/* 210 */     Object builder = this._valueInstantiator.createUsingDefault(ctxt);
/* 211 */     if (this._injectables != null) {
/* 212 */       injectValues(ctxt, builder);
/*     */     }
/* 214 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 215 */     SettableBeanProperty[] props = this._orderedProperties;
/* 216 */     int i = 0;
/* 217 */     int propCount = props.length;
/*     */     for (;;) {
/* 219 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 220 */         return builder;
/*     */       }
/* 222 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 225 */       SettableBeanProperty prop = props[i];
/* 226 */       i++;
/* 227 */       if ((prop != null) && (
/* 228 */         (activeView == null) || (prop.visibleInView(activeView)))) {
/*     */         try {
/* 230 */           prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 232 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 238 */         p.skipChildren();
/*     */       }
/*     */     }
/* 241 */     if (!this._ignoreAllUnknown) {
/* 242 */       throw ctxt.mappingException("Unexpected JSON values; expected at most " + propCount + " properties (in JSON Array)");
/*     */     }
/*     */     
/* 245 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 246 */       p.skipChildren();
/*     */     }
/* 248 */     return builder;
/*     */   }
/*     */   
/*     */   protected Object _deserializeWithCreator(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 254 */     if (this._delegateDeserializer != null) {
/* 255 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/*     */     
/* 258 */     if (this._propertyBasedCreator != null) {
/* 259 */       return _deserializeUsingPropertyBased(p, ctxt);
/*     */     }
/*     */     
/* 262 */     if (this._beanType.isAbstract()) {
/* 263 */       throw JsonMappingException.from(p, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*     */     }
/*     */     
/* 266 */     throw JsonMappingException.from(p, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 283 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 284 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 286 */     SettableBeanProperty[] props = this._orderedProperties;
/* 287 */     int propCount = props.length;
/* 288 */     int i = 0;
/* 289 */     Object builder = null;
/* 291 */     for (; 
/* 291 */         p.nextToken() != JsonToken.END_ARRAY; i++) {
/* 292 */       SettableBeanProperty prop = i < propCount ? props[i] : null;
/* 293 */       if (prop == null) {
/* 294 */         p.skipChildren();
/*     */ 
/*     */ 
/*     */       }
/* 298 */       else if (builder != null) {
/*     */         try {
/* 300 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 302 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       }
/*     */       else {
/* 306 */         String propName = prop.getName();
/*     */         
/* 308 */         SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 309 */         if (creatorProp != null)
/*     */         {
/* 311 */           Object value = creatorProp.deserialize(p, ctxt);
/* 312 */           if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/*     */             try {
/* 314 */               builder = creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 316 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 317 */               continue;
/*     */             }
/*     */             
/* 320 */             if (builder.getClass() != this._beanType.getRawClass())
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 325 */               throw ctxt.mappingException("Can not support implicit polymorphic deserialization for POJOs-as-Arrays style: nominal type " + this._beanType.getRawClass().getName() + ", actual type " + builder.getClass().getName());
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 332 */         else if (!buffer.readIdProperty(propName))
/*     */         {
/*     */ 
/*     */ 
/* 336 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */         }
/*     */       }
/*     */     }
/* 340 */     if (builder == null) {
/*     */       try {
/* 342 */         builder = creator.build(ctxt, buffer);
/*     */       } catch (Exception e) {
/* 344 */         wrapInstantiationProblem(e, ctxt);
/* 345 */         return null;
/*     */       }
/*     */     }
/* 348 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeFromNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 361 */     throw ctxt.mappingException("Can not deserialize a POJO (of type " + this._beanType.getRawClass().getName() + ") from non-Array representation (token: " + jp.getCurrentToken() + "): type/property designed to be serialized as JSON Array");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\BeanAsArrayBuilderDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */