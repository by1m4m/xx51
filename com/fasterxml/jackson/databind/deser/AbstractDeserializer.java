/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class AbstractDeserializer
/*     */   extends JsonDeserializer<Object>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _baseType;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   protected final Map<String, SettableBeanProperty> _backRefProperties;
/*     */   protected final boolean _acceptString;
/*     */   protected final boolean _acceptBoolean;
/*     */   protected final boolean _acceptInt;
/*     */   protected final boolean _acceptDouble;
/*     */   
/*     */   public AbstractDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, Map<String, SettableBeanProperty> backRefProps)
/*     */   {
/*  40 */     this._baseType = beanDesc.getType();
/*  41 */     this._objectIdReader = builder.getObjectIdReader();
/*  42 */     this._backRefProperties = backRefProps;
/*  43 */     Class<?> cls = this._baseType.getRawClass();
/*  44 */     this._acceptString = cls.isAssignableFrom(String.class);
/*  45 */     this._acceptBoolean = ((cls == Boolean.TYPE) || (cls.isAssignableFrom(Boolean.class)));
/*  46 */     this._acceptInt = ((cls == Integer.TYPE) || (cls.isAssignableFrom(Integer.class)));
/*  47 */     this._acceptDouble = ((cls == Double.TYPE) || (cls.isAssignableFrom(Double.class)));
/*     */   }
/*     */   
/*     */   protected AbstractDeserializer(BeanDescription beanDesc)
/*     */   {
/*  52 */     this._baseType = beanDesc.getType();
/*  53 */     this._objectIdReader = null;
/*  54 */     this._backRefProperties = null;
/*  55 */     Class<?> cls = this._baseType.getRawClass();
/*  56 */     this._acceptString = cls.isAssignableFrom(String.class);
/*  57 */     this._acceptBoolean = ((cls == Boolean.TYPE) || (cls.isAssignableFrom(Boolean.class)));
/*  58 */     this._acceptInt = ((cls == Integer.TYPE) || (cls.isAssignableFrom(Integer.class)));
/*  59 */     this._acceptDouble = ((cls == Double.TYPE) || (cls.isAssignableFrom(Double.class)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AbstractDeserializer constructForNonPOJO(BeanDescription beanDesc)
/*     */   {
/*  69 */     return new AbstractDeserializer(beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> handledType()
/*     */   {
/*  80 */     return this._baseType.getRawClass();
/*     */   }
/*     */   
/*     */   public boolean isCachable() {
/*  84 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdReader getObjectIdReader()
/*     */   {
/*  93 */     return this._objectIdReader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty findBackReference(String logicalName)
/*     */   {
/* 102 */     return this._backRefProperties == null ? null : (SettableBeanProperty)this._backRefProperties.get(logicalName);
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
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 118 */     if (this._objectIdReader != null) {
/* 119 */       JsonToken t = jp.getCurrentToken();
/*     */       
/* 121 */       if ((t != null) && (t.isScalarValue())) {
/* 122 */         return _deserializeFromObjectId(jp, ctxt);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 127 */     Object result = _deserializeIfNatural(jp, ctxt);
/* 128 */     if (result != null) {
/* 129 */       return result;
/*     */     }
/* 131 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 139 */     throw ctxt.instantiationException(this._baseType.getRawClass(), "abstract types either need to be mapped to concrete types, have custom deserializer, or be instantiated with additional type information");
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
/*     */   protected Object _deserializeIfNatural(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 157 */     switch (jp.getCurrentTokenId()) {
/*     */     case 6: 
/* 159 */       if (this._acceptString) {
/* 160 */         return jp.getText();
/*     */       }
/*     */       break;
/*     */     case 7: 
/* 164 */       if (this._acceptInt) {
/* 165 */         return Integer.valueOf(jp.getIntValue());
/*     */       }
/*     */       break;
/*     */     case 8: 
/* 169 */       if (this._acceptDouble) {
/* 170 */         return Double.valueOf(jp.getDoubleValue());
/*     */       }
/*     */       break;
/*     */     case 9: 
/* 174 */       if (this._acceptBoolean) {
/* 175 */         return Boolean.TRUE;
/*     */       }
/*     */       break;
/*     */     case 10: 
/* 179 */       if (this._acceptBoolean) {
/* 180 */         return Boolean.FALSE;
/*     */       }
/*     */       break;
/*     */     }
/* 184 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeFromObjectId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 193 */     Object id = this._objectIdReader.readObjectReference(jp, ctxt);
/* 194 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*     */     
/* 196 */     Object pojo = roid.resolve();
/* 197 */     if (pojo == null) {
/* 198 */       throw new UnresolvedForwardReference("Could not resolve Object Id [" + id + "] -- unresolved forward-reference?", jp.getCurrentLocation(), roid);
/*     */     }
/* 200 */     return pojo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\AbstractDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */