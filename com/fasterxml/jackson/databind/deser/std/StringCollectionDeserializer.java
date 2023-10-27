/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ @JacksonStdImpl
/*     */ public final class StringCollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<String>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _collectionType;
/*     */   protected final JsonDeserializer<String> _valueDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator)
/*     */   {
/*  60 */     this(collectionType, valueInstantiator, null, valueDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringCollectionDeserializer(JavaType collectionType, ValueInstantiator valueInstantiator, JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser)
/*     */   {
/*  68 */     super(collectionType);
/*  69 */     this._collectionType = collectionType;
/*  70 */     this._valueDeserializer = valueDeser;
/*  71 */     this._valueInstantiator = valueInstantiator;
/*  72 */     this._delegateDeserializer = delegateDeser;
/*     */   }
/*     */   
/*     */ 
/*     */   protected StringCollectionDeserializer withResolved(JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser)
/*     */   {
/*  78 */     if ((this._valueDeserializer == valueDeser) && (this._delegateDeserializer == delegateDeser)) {
/*  79 */       return this;
/*     */     }
/*  81 */     return new StringCollectionDeserializer(this._collectionType, this._valueInstantiator, delegateDeser, valueDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/*  88 */     return (this._valueDeserializer == null) && (this._delegateDeserializer == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 101 */     JsonDeserializer<Object> delegate = null;
/* 102 */     if (this._valueInstantiator != null) {
/* 103 */       AnnotatedWithParams delegateCreator = this._valueInstantiator.getDelegateCreator();
/* 104 */       if (delegateCreator != null) {
/* 105 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 106 */         delegate = findDeserializer(ctxt, delegateType, property);
/*     */       }
/*     */     }
/* 109 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/* 110 */     JavaType valueType = this._collectionType.getContentType();
/* 111 */     if (valueDeser == null)
/*     */     {
/* 113 */       valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 114 */       if (valueDeser == null)
/*     */       {
/* 116 */         valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
/*     */       }
/*     */     } else {
/* 119 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
/*     */     }
/* 121 */     if (isDefaultDeserializer(valueDeser)) {
/* 122 */       valueDeser = null;
/*     */     }
/* 124 */     return withResolved(delegate, valueDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 135 */     return this._collectionType.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 141 */     JsonDeserializer<?> deser = this._valueDeserializer;
/* 142 */     return deser;
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
/*     */   public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 156 */     if (this._delegateDeserializer != null) {
/* 157 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/*     */     
/* 160 */     Collection<String> result = (Collection)this._valueInstantiator.createUsingDefault(ctxt);
/* 161 */     return deserialize(jp, ctxt, result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException
/*     */   {
/* 170 */     if (!jp.isExpectedStartArrayToken()) {
/* 171 */       return handleNonArray(jp, ctxt, result);
/*     */     }
/*     */     
/* 174 */     if (this._valueDeserializer != null) {
/* 175 */       return deserializeUsingCustom(jp, ctxt, result, this._valueDeserializer);
/*     */     }
/*     */     try
/*     */     {
/*     */       for (;;) {
/* 180 */         String value = jp.nextTextValue();
/* 181 */         if (value != null) {
/* 182 */           result.add(value);
/*     */         }
/*     */         else {
/* 185 */           JsonToken t = jp.getCurrentToken();
/* 186 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/* 189 */           if (t != JsonToken.VALUE_NULL) {
/* 190 */             value = _parseString(jp, ctxt);
/*     */           }
/* 192 */           result.add(value);
/*     */         }
/*     */       }
/* 195 */     } catch (Exception e) { throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     }
/* 197 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private Collection<String> deserializeUsingCustom(JsonParser jp, DeserializationContext ctxt, Collection<String> result, JsonDeserializer<String> deser)
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/*     */       String value;
/*     */       
/*     */       String value;
/*     */       
/* 210 */       if (jp.nextTextValue() == null) {
/* 211 */         JsonToken t = jp.getCurrentToken();
/* 212 */         if (t == JsonToken.END_ARRAY) {
/*     */           break;
/*     */         }
/*     */         
/* 216 */         value = t == JsonToken.VALUE_NULL ? (String)deser.getNullValue() : (String)deser.deserialize(jp, ctxt);
/*     */       } else {
/* 218 */         value = (String)deser.deserialize(jp, ctxt);
/*     */       }
/* 220 */       result.add(value);
/*     */     }
/* 222 */     return result;
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 228 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Collection<String> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException
/*     */   {
/* 239 */     if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 240 */       throw ctxt.mappingException(this._collectionType.getRawClass());
/*     */     }
/*     */     
/* 243 */     JsonDeserializer<String> valueDes = this._valueDeserializer;
/* 244 */     JsonToken t = jp.getCurrentToken();
/*     */     
/*     */     String value;
/*     */     String value;
/* 248 */     if (t == JsonToken.VALUE_NULL) {
/* 249 */       value = valueDes == null ? null : (String)valueDes.getNullValue();
/*     */     } else {
/* 251 */       value = valueDes == null ? _parseString(jp, ctxt) : (String)valueDes.deserialize(jp, ctxt);
/*     */     }
/* 253 */     result.add(value);
/* 254 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StringCollectionDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */