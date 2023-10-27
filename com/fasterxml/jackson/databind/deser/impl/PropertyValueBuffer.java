/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import java.io.IOException;
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
/*     */ public final class PropertyValueBuffer
/*     */ {
/*     */   protected final JsonParser _parser;
/*     */   protected final DeserializationContext _context;
/*     */   protected final Object[] _creatorParameters;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   private int _paramsNeeded;
/*     */   private PropertyValue _buffered;
/*     */   private Object _idValue;
/*     */   
/*     */   public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount, ObjectIdReader oir)
/*     */   {
/*  53 */     this._parser = jp;
/*  54 */     this._context = ctxt;
/*  55 */     this._paramsNeeded = paramCount;
/*  56 */     this._objectIdReader = oir;
/*  57 */     this._creatorParameters = new Object[paramCount];
/*     */   }
/*     */   
/*     */   public void inject(SettableBeanProperty[] injectableProperties)
/*     */   {
/*  62 */     int i = 0; for (int len = injectableProperties.length; i < len; i++) {
/*  63 */       SettableBeanProperty prop = injectableProperties[i];
/*  64 */       if (prop != null)
/*     */       {
/*  66 */         this._creatorParameters[i] = this._context.findInjectableValue(prop.getInjectableValueId(), prop, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object[] getParameters(Object[] defaults)
/*     */   {
/*  79 */     if (defaults != null) {
/*  80 */       int i = 0; for (int len = this._creatorParameters.length; i < len; i++) {
/*  81 */         if (this._creatorParameters[i] == null) {
/*  82 */           Object value = defaults[i];
/*  83 */           if (value != null) {
/*  84 */             this._creatorParameters[i] = value;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  89 */     return this._creatorParameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean readIdProperty(String propName)
/*     */     throws IOException
/*     */   {
/* 101 */     if ((this._objectIdReader != null) && (propName.equals(this._objectIdReader.propertyName.getSimpleName()))) {
/* 102 */       this._idValue = this._objectIdReader.readObjectReference(this._parser, this._context);
/* 103 */       return true;
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object handleIdValue(DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 113 */     if (this._objectIdReader != null) {
/* 114 */       if (this._idValue != null) {
/* 115 */         ReadableObjectId roid = ctxt.findObjectId(this._idValue, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 116 */         roid.bindItem(bean);
/*     */         
/* 118 */         SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 119 */         if (idProp != null) {
/* 120 */           return idProp.setAndReturn(bean, this._idValue);
/*     */         }
/*     */       }
/*     */       else {
/* 124 */         throw ctxt.mappingException("No _idValue when handleIdValue called, on instance of " + bean.getClass().getName());
/*     */       }
/*     */     }
/*     */     
/* 128 */     return bean;
/*     */   }
/*     */   
/* 131 */   protected PropertyValue buffered() { return this._buffered; }
/*     */   
/* 133 */   public boolean isComplete() { return this._paramsNeeded <= 0; }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean assignParameter(int index, Object value)
/*     */   {
/* 139 */     this._creatorParameters[index] = value;
/* 140 */     return --this._paramsNeeded <= 0;
/*     */   }
/*     */   
/*     */   public void bufferProperty(SettableBeanProperty prop, Object value) {
/* 144 */     this._buffered = new PropertyValue.Regular(this._buffered, value, prop);
/*     */   }
/*     */   
/*     */   public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
/* 148 */     this._buffered = new PropertyValue.Any(this._buffered, value, prop, propName);
/*     */   }
/*     */   
/*     */   public void bufferMapProperty(Object key, Object value) {
/* 152 */     this._buffered = new PropertyValue.Map(this._buffered, value, key);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyValueBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */