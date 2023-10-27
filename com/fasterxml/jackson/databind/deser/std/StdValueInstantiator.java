/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class StdValueInstantiator
/*     */   extends ValueInstantiator
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _valueTypeDesc;
/*     */   protected AnnotatedWithParams _defaultCreator;
/*     */   protected AnnotatedWithParams _withArgsCreator;
/*     */   protected CreatorProperty[] _constructorArguments;
/*     */   protected JavaType _delegateType;
/*     */   protected AnnotatedWithParams _delegateCreator;
/*     */   protected CreatorProperty[] _delegateArguments;
/*     */   protected AnnotatedWithParams _fromStringCreator;
/*     */   protected AnnotatedWithParams _fromIntCreator;
/*     */   protected AnnotatedWithParams _fromLongCreator;
/*     */   protected AnnotatedWithParams _fromDoubleCreator;
/*     */   protected AnnotatedWithParams _fromBooleanCreator;
/*     */   protected AnnotatedParameter _incompleteParameter;
/*     */   
/*     */   public StdValueInstantiator(DeserializationConfig config, Class<?> valueType)
/*     */   {
/*  66 */     this._valueTypeDesc = (valueType == null ? "UNKNOWN TYPE" : valueType.getName());
/*     */   }
/*     */   
/*     */   public StdValueInstantiator(DeserializationConfig config, JavaType valueType) {
/*  70 */     this._valueTypeDesc = (valueType == null ? "UNKNOWN TYPE" : valueType.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdValueInstantiator(StdValueInstantiator src)
/*     */   {
/*  79 */     this._valueTypeDesc = src._valueTypeDesc;
/*     */     
/*  81 */     this._defaultCreator = src._defaultCreator;
/*     */     
/*  83 */     this._constructorArguments = src._constructorArguments;
/*  84 */     this._withArgsCreator = src._withArgsCreator;
/*     */     
/*  86 */     this._delegateType = src._delegateType;
/*  87 */     this._delegateCreator = src._delegateCreator;
/*  88 */     this._delegateArguments = src._delegateArguments;
/*     */     
/*  90 */     this._fromStringCreator = src._fromStringCreator;
/*  91 */     this._fromIntCreator = src._fromIntCreator;
/*  92 */     this._fromLongCreator = src._fromLongCreator;
/*  93 */     this._fromDoubleCreator = src._fromDoubleCreator;
/*  94 */     this._fromBooleanCreator = src._fromBooleanCreator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureFromObjectSettings(AnnotatedWithParams defaultCreator, AnnotatedWithParams delegateCreator, JavaType delegateType, CreatorProperty[] delegateArgs, AnnotatedWithParams withArgsCreator, CreatorProperty[] constructorArgs)
/*     */   {
/* 106 */     this._defaultCreator = defaultCreator;
/* 107 */     this._delegateCreator = delegateCreator;
/* 108 */     this._delegateType = delegateType;
/* 109 */     this._delegateArguments = delegateArgs;
/* 110 */     this._withArgsCreator = withArgsCreator;
/* 111 */     this._constructorArguments = constructorArgs;
/*     */   }
/*     */   
/*     */   public void configureFromStringCreator(AnnotatedWithParams creator) {
/* 115 */     this._fromStringCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromIntCreator(AnnotatedWithParams creator) {
/* 119 */     this._fromIntCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromLongCreator(AnnotatedWithParams creator) {
/* 123 */     this._fromLongCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromDoubleCreator(AnnotatedWithParams creator) {
/* 127 */     this._fromDoubleCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureFromBooleanCreator(AnnotatedWithParams creator) {
/* 131 */     this._fromBooleanCreator = creator;
/*     */   }
/*     */   
/*     */   public void configureIncompleteParameter(AnnotatedParameter parameter) {
/* 135 */     this._incompleteParameter = parameter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getValueTypeDesc()
/*     */   {
/* 146 */     return this._valueTypeDesc;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromString()
/*     */   {
/* 151 */     return this._fromStringCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromInt()
/*     */   {
/* 156 */     return this._fromIntCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromLong()
/*     */   {
/* 161 */     return this._fromLongCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromDouble()
/*     */   {
/* 166 */     return this._fromDoubleCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromBoolean()
/*     */   {
/* 171 */     return this._fromBooleanCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateUsingDefault()
/*     */   {
/* 176 */     return this._defaultCreator != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateUsingDelegate()
/*     */   {
/* 181 */     return this._delegateType != null;
/*     */   }
/*     */   
/*     */   public boolean canCreateFromObjectWith()
/*     */   {
/* 186 */     return this._withArgsCreator != null;
/*     */   }
/*     */   
/*     */   public JavaType getDelegateType(DeserializationConfig config)
/*     */   {
/* 191 */     return this._delegateType;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config)
/*     */   {
/* 196 */     return this._constructorArguments;
/*     */   }
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
/* 208 */     if (this._defaultCreator == null) {
/* 209 */       throw new IllegalStateException("No default constructor for " + getValueTypeDesc());
/*     */     }
/*     */     try {
/* 212 */       return this._defaultCreator.call();
/*     */     } catch (ExceptionInInitializerError e) {
/* 214 */       throw wrapException(e);
/*     */     } catch (Exception e) {
/* 216 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
/*     */     throws IOException
/*     */   {
/* 223 */     if (this._withArgsCreator == null) {
/* 224 */       throw new IllegalStateException("No with-args constructor for " + getValueTypeDesc());
/*     */     }
/*     */     try {
/* 227 */       return this._withArgsCreator.call(args);
/*     */     } catch (ExceptionInInitializerError e) {
/* 229 */       throw wrapException(e);
/*     */     } catch (Exception e) {
/* 231 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object createUsingDelegate(DeserializationContext ctxt, Object delegate)
/*     */     throws IOException
/*     */   {
/* 238 */     if (this._delegateCreator == null) {
/* 239 */       throw new IllegalStateException("No delegate constructor for " + getValueTypeDesc());
/*     */     }
/*     */     try
/*     */     {
/* 243 */       if (this._delegateArguments == null) {
/* 244 */         return this._delegateCreator.call1(delegate);
/*     */       }
/*     */       
/* 247 */       int len = this._delegateArguments.length;
/* 248 */       Object[] args = new Object[len];
/* 249 */       for (int i = 0; i < len; i++) {
/* 250 */         CreatorProperty prop = this._delegateArguments[i];
/* 251 */         if (prop == null) {
/* 252 */           args[i] = delegate;
/*     */         } else {
/* 254 */           args[i] = ctxt.findInjectableValue(prop.getInjectableValueId(), prop, null);
/*     */         }
/*     */       }
/*     */       
/* 258 */       return this._delegateCreator.call(args);
/*     */     } catch (ExceptionInInitializerError e) {
/* 260 */       throw wrapException(e);
/*     */     } catch (Exception e) {
/* 262 */       throw wrapException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object createFromString(DeserializationContext ctxt, String value)
/*     */     throws IOException
/*     */   {
/* 275 */     if (this._fromStringCreator != null) {
/*     */       try {
/* 277 */         return this._fromStringCreator.call1(value);
/*     */       } catch (Exception e) {
/* 279 */         throw wrapException(e);
/*     */       } catch (ExceptionInInitializerError e) {
/* 281 */         throw wrapException(e);
/*     */       }
/*     */     }
/* 284 */     return _createFromStringFallbacks(ctxt, value);
/*     */   }
/*     */   
/*     */   public Object createFromInt(DeserializationContext ctxt, int value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 292 */       if (this._fromIntCreator != null) {
/* 293 */         return this._fromIntCreator.call1(Integer.valueOf(value));
/*     */       }
/*     */       
/* 296 */       if (this._fromLongCreator != null) {
/* 297 */         return this._fromLongCreator.call1(Long.valueOf(value));
/*     */       }
/*     */     } catch (Exception e) {
/* 300 */       throw wrapException(e);
/*     */     } catch (ExceptionInInitializerError e) {
/* 302 */       throw wrapException(e);
/*     */     }
/* 304 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Integral number (" + value + "); no single-int-arg constructor/factory method");
/*     */   }
/*     */   
/*     */   public Object createFromLong(DeserializationContext ctxt, long value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 312 */       if (this._fromLongCreator != null) {
/* 313 */         return this._fromLongCreator.call1(Long.valueOf(value));
/*     */       }
/*     */     } catch (Exception e) {
/* 316 */       throw wrapException(e);
/*     */     } catch (ExceptionInInitializerError e) {
/* 318 */       throw wrapException(e);
/*     */     }
/* 320 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Long integral number (" + value + "); no single-long-arg constructor/factory method");
/*     */   }
/*     */   
/*     */   public Object createFromDouble(DeserializationContext ctxt, double value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 328 */       if (this._fromDoubleCreator != null) {
/* 329 */         return this._fromDoubleCreator.call1(Double.valueOf(value));
/*     */       }
/*     */     } catch (Exception e) {
/* 332 */       throw wrapException(e);
/*     */     } catch (ExceptionInInitializerError e) {
/* 334 */       throw wrapException(e);
/*     */     }
/* 336 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Floating-point number (" + value + "); no one-double/Double-arg constructor/factory method");
/*     */   }
/*     */   
/*     */   public Object createFromBoolean(DeserializationContext ctxt, boolean value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 344 */       if (this._fromBooleanCreator != null) {
/* 345 */         return this._fromBooleanCreator.call1(Boolean.valueOf(value));
/*     */       }
/*     */     } catch (Exception e) {
/* 348 */       throw wrapException(e);
/*     */     } catch (ExceptionInInitializerError e) {
/* 350 */       throw wrapException(e);
/*     */     }
/* 352 */     throw ctxt.mappingException("Can not instantiate value of type " + getValueTypeDesc() + " from Boolean value (" + value + "); no single-boolean/Boolean-arg constructor/factory method");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedWithParams getDelegateCreator()
/*     */   {
/* 364 */     return this._delegateCreator;
/*     */   }
/*     */   
/*     */   public AnnotatedWithParams getDefaultCreator()
/*     */   {
/* 369 */     return this._defaultCreator;
/*     */   }
/*     */   
/*     */   public AnnotatedWithParams getWithArgsCreator()
/*     */   {
/* 374 */     return this._withArgsCreator;
/*     */   }
/*     */   
/*     */   public AnnotatedParameter getIncompleteParameter()
/*     */   {
/* 379 */     return this._incompleteParameter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonMappingException wrapException(Throwable t)
/*     */   {
/* 390 */     while (t.getCause() != null) {
/* 391 */       t = t.getCause();
/*     */     }
/* 393 */     if ((t instanceof JsonMappingException)) {
/* 394 */       return (JsonMappingException)t;
/*     */     }
/* 396 */     return new JsonMappingException("Instantiation of " + getValueTypeDesc() + " value failed: " + t.getMessage(), t);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StdValueInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */