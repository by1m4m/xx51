/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrowableDeserializer
/*     */   extends BeanDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected static final String PROP_NAME_MESSAGE = "message";
/*     */   
/*     */   public ThrowableDeserializer(BeanDeserializer baseDeserializer)
/*     */   {
/*  30 */     super(baseDeserializer);
/*     */     
/*  32 */     this._vanillaProcessing = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ThrowableDeserializer(BeanDeserializer src, NameTransformer unwrapper)
/*     */   {
/*  39 */     super(src, unwrapper);
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  44 */     if (getClass() != ThrowableDeserializer.class) {
/*  45 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  51 */     return new ThrowableDeserializer(this, unwrapper);
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
/*     */   public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  65 */     if (this._propertyBasedCreator != null) {
/*  66 */       return _deserializeUsingPropertyBased(jp, ctxt);
/*     */     }
/*  68 */     if (this._delegateDeserializer != null) {
/*  69 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/*     */     
/*  72 */     if (this._beanType.isAbstract()) {
/*  73 */       throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*     */     }
/*     */     
/*  76 */     boolean hasStringCreator = this._valueInstantiator.canCreateFromString();
/*  77 */     boolean hasDefaultCtor = this._valueInstantiator.canCreateUsingDefault();
/*     */     
/*  79 */     if ((!hasStringCreator) && (!hasDefaultCtor)) {
/*  80 */       throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having a default contructor, a single-String-arg constructor; or explicit @JsonCreator");
/*     */     }
/*     */     
/*     */ 
/*  84 */     Object throwable = null;
/*  85 */     Object[] pending = null;
/*  86 */     int pendingIx = 0;
/*  88 */     for (; 
/*  88 */         jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/*  89 */       String propName = jp.getCurrentName();
/*  90 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  91 */       jp.nextToken();
/*     */       
/*  93 */       if (prop != null) {
/*  94 */         if (throwable != null) {
/*  95 */           prop.deserializeAndSet(jp, ctxt, throwable);
/*     */         }
/*     */         else
/*     */         {
/*  99 */           if (pending == null) {
/* 100 */             int len = this._beanProperties.size();
/* 101 */             pending = new Object[len + len];
/*     */           }
/* 103 */           pending[(pendingIx++)] = prop;
/* 104 */           pending[(pendingIx++)] = prop.deserialize(jp, ctxt);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 109 */       else if (("message".equals(propName)) && 
/* 110 */         (hasStringCreator)) {
/* 111 */         throwable = this._valueInstantiator.createFromString(ctxt, jp.getText());
/*     */         
/* 113 */         if (pending != null) {
/* 114 */           int i = 0; for (int len = pendingIx; i < len; i += 2) {
/* 115 */             prop = (SettableBeanProperty)pending[i];
/* 116 */             prop.set(throwable, pending[(i + 1)]);
/*     */           }
/* 118 */           pending = null;
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/* 126 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 127 */         jp.skipChildren();
/*     */ 
/*     */       }
/* 130 */       else if (this._anySetter != null) {
/* 131 */         this._anySetter.deserializeAndSet(jp, ctxt, throwable, propName);
/*     */       }
/*     */       else
/*     */       {
/* 135 */         handleUnknownProperty(jp, ctxt, throwable, propName);
/*     */       }
/*     */     }
/* 138 */     if (throwable == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */       if (hasStringCreator) {
/* 146 */         throwable = this._valueInstantiator.createFromString(ctxt, null);
/*     */       } else {
/* 148 */         throwable = this._valueInstantiator.createUsingDefault(ctxt);
/*     */       }
/*     */       
/* 151 */       if (pending != null) {
/* 152 */         int i = 0; for (int len = pendingIx; i < len; i += 2) {
/* 153 */           SettableBeanProperty prop = (SettableBeanProperty)pending[i];
/* 154 */           prop.set(throwable, pending[(i + 1)]);
/*     */         }
/*     */       }
/*     */     }
/* 158 */     return throwable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\ThrowableDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */