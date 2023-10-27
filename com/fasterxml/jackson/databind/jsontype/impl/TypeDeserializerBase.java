/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeDeserializerBase
/*     */   extends TypeDeserializer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final TypeIdResolver _idResolver;
/*     */   protected final JavaType _baseType;
/*     */   protected final BeanProperty _property;
/*     */   protected final JavaType _defaultImpl;
/*     */   protected final String _typePropertyName;
/*     */   protected final boolean _typeIdVisible;
/*     */   protected final HashMap<String, JsonDeserializer<Object>> _deserializers;
/*     */   protected JsonDeserializer<Object> _defaultImplDeserializer;
/*     */   
/*     */   protected TypeDeserializerBase(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl)
/*     */   {
/*  71 */     this._baseType = baseType;
/*  72 */     this._idResolver = idRes;
/*  73 */     this._typePropertyName = typePropertyName;
/*  74 */     this._typeIdVisible = typeIdVisible;
/*  75 */     this._deserializers = new HashMap();
/*  76 */     if (defaultImpl == null) {
/*  77 */       this._defaultImpl = null;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  83 */       this._defaultImpl = baseType.forcedNarrowBy(defaultImpl);
/*     */     }
/*     */     
/*  86 */     this._property = null;
/*     */   }
/*     */   
/*     */   protected TypeDeserializerBase(TypeDeserializerBase src, BeanProperty property)
/*     */   {
/*  91 */     this._baseType = src._baseType;
/*  92 */     this._idResolver = src._idResolver;
/*  93 */     this._typePropertyName = src._typePropertyName;
/*  94 */     this._typeIdVisible = src._typeIdVisible;
/*  95 */     this._deserializers = src._deserializers;
/*  96 */     this._defaultImpl = src._defaultImpl;
/*  97 */     this._defaultImplDeserializer = src._defaultImplDeserializer;
/*     */     
/*  99 */     this._property = property;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract TypeDeserializer forProperty(BeanProperty paramBeanProperty);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonTypeInfo.As getTypeInclusion();
/*     */   
/*     */ 
/*     */ 
/*     */   public String baseTypeName()
/*     */   {
/* 114 */     return this._baseType.getRawClass().getName();
/*     */   }
/*     */   
/* 117 */   public final String getPropertyName() { return this._typePropertyName; }
/*     */   
/*     */   public TypeIdResolver getTypeIdResolver() {
/* 120 */     return this._idResolver;
/*     */   }
/*     */   
/*     */   public Class<?> getDefaultImpl() {
/* 124 */     return this._defaultImpl == null ? null : this._defaultImpl.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 130 */     StringBuilder sb = new StringBuilder();
/* 131 */     sb.append('[').append(getClass().getName());
/* 132 */     sb.append("; base-type:").append(this._baseType);
/* 133 */     sb.append("; id-resolver: ").append(this._idResolver);
/* 134 */     sb.append(']');
/* 135 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
/*     */     throws IOException
/*     */   {
/*     */     JsonDeserializer<Object> deser;
/*     */     
/*     */ 
/*     */ 
/* 149 */     synchronized (this._deserializers) {
/* 150 */       deser = (JsonDeserializer)this._deserializers.get(typeId);
/* 151 */       if (deser == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */         JavaType type = this._idResolver.typeFromId(ctxt, typeId);
/* 158 */         if (type == null)
/*     */         {
/* 160 */           deser = _findDefaultImplDeserializer(ctxt);
/* 161 */           if (deser == null) {
/* 162 */             deser = _handleUnknownTypeId(ctxt, typeId, this._idResolver, this._baseType);
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 173 */           if ((this._baseType != null) && (this._baseType.getClass() == type.getClass())) {
/* 174 */             type = this._baseType.narrowBy(type.getRawClass());
/*     */           }
/* 176 */           deser = ctxt.findContextualValueDeserializer(type, this._property);
/*     */         }
/* 178 */         this._deserializers.put(typeId, deser);
/*     */       }
/*     */     }
/* 181 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 190 */     if (this._defaultImpl == null) {
/* 191 */       if (!ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)) {
/* 192 */         return NullifyingDeserializer.instance;
/*     */       }
/* 194 */       return null;
/*     */     }
/* 196 */     Class<?> raw = this._defaultImpl.getRawClass();
/* 197 */     if (ClassUtil.isBogusClass(raw)) {
/* 198 */       return NullifyingDeserializer.instance;
/*     */     }
/*     */     
/* 201 */     synchronized (this._defaultImpl) {
/* 202 */       if (this._defaultImplDeserializer == null) {
/* 203 */         this._defaultImplDeserializer = ctxt.findContextualValueDeserializer(this._defaultImpl, this._property);
/*     */       }
/*     */       
/* 206 */       return this._defaultImplDeserializer;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 219 */     return _deserializeWithNativeTypeId(jp, ctxt, jp.getTypeId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
/*     */     throws IOException
/*     */   {
/*     */     JsonDeserializer<Object> deser;
/*     */     
/*     */ 
/*     */ 
/* 232 */     if (typeId == null)
/*     */     {
/*     */ 
/*     */ 
/* 236 */       JsonDeserializer<Object> deser = _findDefaultImplDeserializer(ctxt);
/* 237 */       if (deser == null) {
/* 238 */         throw ctxt.mappingException("No (native) type id found when one was expected for polymorphic type handling");
/*     */       }
/*     */     } else {
/* 241 */       String typeIdStr = (typeId instanceof String) ? (String)typeId : String.valueOf(typeId);
/* 242 */       deser = _findDeserializer(ctxt, typeIdStr);
/*     */     }
/* 244 */     return deser.deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
/*     */     throws IOException
/*     */   {
/*     */     String extraDesc;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */     if ((idResolver instanceof TypeIdResolverBase)) {
/* 266 */       String extraDesc = ((TypeIdResolverBase)idResolver).getDescForKnownTypeIds();
/* 267 */       if (extraDesc == null) {
/* 268 */         extraDesc = "known type ids are not statically known";
/*     */       } else {
/* 270 */         extraDesc = "known type ids = " + extraDesc;
/*     */       }
/*     */     } else {
/* 273 */       extraDesc = null;
/*     */     }
/* 275 */     throw ctxt.unknownTypeException(this._baseType, typeId, extraDesc);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeDeserializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */