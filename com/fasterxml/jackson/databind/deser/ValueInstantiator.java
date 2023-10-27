/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
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
/*     */ public abstract class ValueInstantiator
/*     */ {
/*     */   public abstract String getValueTypeDesc();
/*     */   
/*     */   public boolean canInstantiate()
/*     */   {
/*  51 */     return (canCreateUsingDefault()) || (canCreateUsingDelegate()) || (canCreateFromObjectWith()) || (canCreateFromString()) || (canCreateFromInt()) || (canCreateFromLong()) || (canCreateFromDouble()) || (canCreateFromBoolean());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canCreateFromString()
/*     */   {
/*  61 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canCreateFromInt()
/*     */   {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canCreateFromLong()
/*     */   {
/*  73 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canCreateFromDouble()
/*     */   {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canCreateFromBoolean()
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canCreateUsingDefault()
/*     */   {
/*  92 */     return getDefaultCreator() != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canCreateUsingDelegate()
/*     */   {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canCreateFromObjectWith()
/*     */   {
/* 106 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config)
/*     */   {
/* 118 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getDelegateType(DeserializationConfig config)
/*     */   {
/* 127 */     return null;
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
/*     */   public Object createUsingDefault(DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 146 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + "; no default creator found");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
/*     */     throws IOException
/*     */   {
/* 159 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " with arguments");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object createUsingDelegate(DeserializationContext ctxt, Object delegate)
/*     */     throws IOException
/*     */   {
/* 167 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " using delegate");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object createFromString(DeserializationContext ctxt, String value)
/*     */     throws IOException
/*     */   {
/* 178 */     return _createFromStringFallbacks(ctxt, value);
/*     */   }
/*     */   
/*     */   public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
/* 182 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Integer number (" + value + ", int)");
/*     */   }
/*     */   
/*     */   public Object createFromLong(DeserializationContext ctxt, long value) throws IOException
/*     */   {
/* 187 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Integer number (" + value + ", long)");
/*     */   }
/*     */   
/*     */   public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException
/*     */   {
/* 192 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Floating-point number (" + value + ", double)");
/*     */   }
/*     */   
/*     */   public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException
/*     */   {
/* 197 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Boolean value (" + value + ")");
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
/*     */   public AnnotatedWithParams getDefaultCreator()
/*     */   {
/* 217 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedWithParams getDelegateCreator()
/*     */   {
/* 227 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedWithParams getWithArgsCreator()
/*     */   {
/* 238 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public AnnotatedParameter getIncompleteParameter()
/*     */   {
/* 244 */     return null;
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
/*     */   protected Object _createFromStringFallbacks(DeserializationContext ctxt, String value)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 262 */     if (canCreateFromBoolean()) {
/* 263 */       String str = value.trim();
/* 264 */       if ("true".equals(str)) {
/* 265 */         return createFromBoolean(ctxt, true);
/*     */       }
/* 267 */       if ("false".equals(str)) {
/* 268 */         return createFromBoolean(ctxt, false);
/*     */       }
/*     */     }
/*     */     
/* 272 */     if ((value.length() == 0) && 
/* 273 */       (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))) {
/* 274 */       return null;
/*     */     }
/*     */     
/* 277 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from String value ('" + value + "'); no single-String constructor/factory method");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\ValueInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */