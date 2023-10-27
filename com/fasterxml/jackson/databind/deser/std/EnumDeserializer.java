/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class EnumDeserializer
/*     */   extends StdScalarDeserializer<Enum<?>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final EnumResolver<?> _resolver;
/*     */   
/*     */   public EnumDeserializer(EnumResolver<?> res)
/*     */   {
/*  27 */     super(Enum.class);
/*  28 */     this._resolver = res;
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
/*     */   public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory)
/*     */   {
/*  42 */     Class<?> paramClass = factory.getRawParameterType(0);
/*  43 */     if (config.canOverrideAccessModifiers()) {
/*  44 */       ClassUtil.checkAndFixAccess(factory.getMember());
/*     */     }
/*  46 */     return new FactoryBasedDeserializer(enumClass, factory, paramClass);
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
/*     */   public boolean isCachable()
/*     */   {
/*  60 */     return true;
/*     */   }
/*     */   
/*     */   public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  65 */     JsonToken curr = jp.getCurrentToken();
/*     */     
/*     */ 
/*  68 */     if ((curr == JsonToken.VALUE_STRING) || (curr == JsonToken.FIELD_NAME)) {
/*  69 */       String name = jp.getText();
/*  70 */       Enum<?> result = this._resolver.findEnum(name);
/*  71 */       if (result == null) {
/*  72 */         return _deserializeAltString(jp, ctxt, name);
/*     */       }
/*  74 */       return result;
/*     */     }
/*     */     
/*  77 */     if (curr == JsonToken.VALUE_NUMBER_INT)
/*     */     {
/*  79 */       _checkFailOnNumber(ctxt);
/*     */       
/*  81 */       int index = jp.getIntValue();
/*  82 */       Enum<?> result = this._resolver.getEnum(index);
/*  83 */       if ((result == null) && (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL))) {
/*  84 */         throw ctxt.weirdNumberException(Integer.valueOf(index), this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
/*     */       }
/*     */       
/*  87 */       return result;
/*     */     }
/*  89 */     return _deserializeOther(jp, ctxt);
/*     */   }
/*     */   
/*     */   private final Enum<?> _deserializeAltString(JsonParser jp, DeserializationContext ctxt, String name)
/*     */     throws IOException
/*     */   {
/*  95 */     name = name.trim();
/*  96 */     if (name.length() == 0) {
/*  97 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
/*  98 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/* 102 */       char c = name.charAt(0);
/* 103 */       if ((c >= '0') && (c <= '9')) {
/*     */         try {
/* 105 */           int ix = Integer.parseInt(name);
/* 106 */           _checkFailOnNumber(ctxt);
/* 107 */           Enum<?> result = this._resolver.getEnum(ix);
/* 108 */           if (result != null) {
/* 109 */             return result;
/*     */           }
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 117 */       throw ctxt.weirdStringException(name, this._resolver.getEnumClass(), "value not one of declared Enum instance names: " + this._resolver.getEnums());
/*     */     }
/*     */     
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   protected Enum<?> _deserializeOther(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/* 125 */     JsonToken curr = jp.getCurrentToken();
/*     */     
/* 127 */     if ((curr == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 128 */       jp.nextToken();
/* 129 */       Enum<?> parsed = deserialize(jp, ctxt);
/* 130 */       curr = jp.nextToken();
/* 131 */       if (curr != JsonToken.END_ARRAY) {
/* 132 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._resolver.getEnumClass().getName() + "' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 135 */       return parsed;
/*     */     }
/* 137 */     throw ctxt.mappingException(this._resolver.getEnumClass());
/*     */   }
/*     */   
/*     */   protected void _checkFailOnNumber(DeserializationContext ctxt) throws IOException
/*     */   {
/* 142 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
/* 143 */       throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class FactoryBasedDeserializer
/*     */     extends StdDeserializer<Object>
/*     */     implements ContextualDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final Class<?> _inputType;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final Method _factory;
/*     */     
/*     */ 
/*     */     protected final JsonDeserializer<?> _deser;
/*     */     
/*     */ 
/*     */ 
/*     */     public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f, Class<?> inputType)
/*     */     {
/* 171 */       super();
/* 172 */       this._factory = f.getAnnotated();
/* 173 */       this._inputType = inputType;
/* 174 */       this._deser = null;
/*     */     }
/*     */     
/*     */     protected FactoryBasedDeserializer(FactoryBasedDeserializer base, JsonDeserializer<?> deser)
/*     */     {
/* 179 */       super();
/* 180 */       this._inputType = base._inputType;
/* 181 */       this._factory = base._factory;
/* 182 */       this._deser = deser;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/* 190 */       if ((this._deser == null) && (this._inputType != String.class)) {
/* 191 */         return new FactoryBasedDeserializer(this, ctxt.findContextualValueDeserializer(ctxt.constructType(this._inputType), property));
/*     */       }
/*     */       
/* 194 */       return this;
/*     */     }
/*     */     
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/*     */       Object value;
/*     */       Object value;
/* 201 */       if (this._deser != null) {
/* 202 */         value = this._deser.deserialize(jp, ctxt);
/*     */       } else {
/* 204 */         JsonToken curr = jp.getCurrentToken();
/* 205 */         Object value; if ((curr == JsonToken.VALUE_STRING) || (curr == JsonToken.FIELD_NAME)) {
/* 206 */           value = jp.getText();
/*     */         } else {
/* 208 */           value = jp.getValueAsString();
/*     */         }
/*     */       }
/*     */       try {
/* 212 */         return this._factory.invoke(this._valueClass, new Object[] { value });
/*     */       } catch (Exception e) {
/* 214 */         Throwable t = ClassUtil.getRootCause(e);
/* 215 */         if ((t instanceof IOException)) {
/* 216 */           throw ((IOException)t);
/*     */         }
/* 218 */         throw ctxt.instantiationException(this._valueClass, t);
/*     */       }
/*     */     }
/*     */     
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException
/*     */     {
/* 224 */       if (this._deser == null) {
/* 225 */         return deserialize(jp, ctxt);
/*     */       }
/* 227 */       return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\EnumDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */