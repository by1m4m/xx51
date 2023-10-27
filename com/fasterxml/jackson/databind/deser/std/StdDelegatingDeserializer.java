/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdDelegatingDeserializer<T>
/*     */   extends StdDeserializer<T>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Converter<Object, T> _converter;
/*     */   protected final JavaType _delegateType;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public StdDelegatingDeserializer(Converter<?, T> converter)
/*     */   {
/*  61 */     super(Object.class);
/*  62 */     this._converter = converter;
/*  63 */     this._delegateType = null;
/*  64 */     this._delegateDeserializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdDelegatingDeserializer(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer)
/*     */   {
/*  71 */     super(delegateType);
/*  72 */     this._converter = converter;
/*  73 */     this._delegateType = delegateType;
/*  74 */     this._delegateDeserializer = delegateDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingDeserializer(StdDelegatingDeserializer<T> src)
/*     */   {
/*  82 */     super(src);
/*  83 */     this._converter = src._converter;
/*  84 */     this._delegateType = src._delegateType;
/*  85 */     this._delegateDeserializer = src._delegateDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingDeserializer<T> withDelegate(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer)
/*     */   {
/*  95 */     if (getClass() != StdDelegatingDeserializer.class) {
/*  96 */       throw new IllegalStateException("Sub-class " + getClass().getName() + " must override 'withDelegate'");
/*     */     }
/*  98 */     return new StdDelegatingDeserializer(converter, delegateType, delegateDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/* 111 */     if ((this._delegateDeserializer != null) && ((this._delegateDeserializer instanceof ResolvableDeserializer))) {
/* 112 */       ((ResolvableDeserializer)this._delegateDeserializer).resolve(ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 121 */     if (this._delegateDeserializer != null) {
/* 122 */       JsonDeserializer<?> deser = ctxt.handleSecondaryContextualization(this._delegateDeserializer, property, this._delegateType);
/*     */       
/* 124 */       if (deser != this._delegateDeserializer) {
/* 125 */         return withDelegate(this._converter, this._delegateType, deser);
/*     */       }
/* 127 */       return this;
/*     */     }
/*     */     
/* 130 */     JavaType delegateType = this._converter.getInputType(ctxt.getTypeFactory());
/* 131 */     return withDelegate(this._converter, delegateType, ctxt.findContextualValueDeserializer(delegateType, property));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> getDelegatee()
/*     */   {
/* 143 */     return this._delegateDeserializer;
/*     */   }
/*     */   
/*     */   public Class<?> handledType()
/*     */   {
/* 148 */     return this._delegateDeserializer.handledType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 161 */     Object delegateValue = this._delegateDeserializer.deserialize(jp, ctxt);
/* 162 */     if (delegateValue == null) {
/* 163 */       return null;
/*     */     }
/* 165 */     return (T)convertValue(delegateValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 176 */     Object delegateValue = this._delegateDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
/*     */     
/* 178 */     if (delegateValue == null) {
/* 179 */       return null;
/*     */     }
/* 181 */     return convertValue(delegateValue);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected T convertValue(Object delegateValue)
/*     */   {
/* 203 */     return (T)this._converter.convert(delegateValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StdDelegatingDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */