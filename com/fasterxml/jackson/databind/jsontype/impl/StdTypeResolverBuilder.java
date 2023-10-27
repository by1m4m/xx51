/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
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
/*     */ public class StdTypeResolverBuilder
/*     */   implements TypeResolverBuilder<StdTypeResolverBuilder>
/*     */ {
/*     */   protected JsonTypeInfo.Id _idType;
/*     */   protected JsonTypeInfo.As _includeAs;
/*     */   protected String _typeProperty;
/*  31 */   protected boolean _typeIdVisible = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> _defaultImpl;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeIdResolver _customIdResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StdTypeResolverBuilder noTypeInfoBuilder()
/*     */   {
/*  52 */     return new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder init(JsonTypeInfo.Id idType, TypeIdResolver idRes)
/*     */   {
/*  59 */     if (idType == null) {
/*  60 */       throw new IllegalArgumentException("idType can not be null");
/*     */     }
/*  62 */     this._idType = idType;
/*  63 */     this._customIdResolver = idRes;
/*     */     
/*  65 */     this._typeProperty = idType.getDefaultPropertyName();
/*  66 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*     */   {
/*  73 */     if (this._idType == JsonTypeInfo.Id.NONE) return null;
/*  74 */     TypeIdResolver idRes = idResolver(config, baseType, subtypes, true, false);
/*  75 */     switch (this._includeAs) {
/*     */     case WRAPPER_ARRAY: 
/*  77 */       return new AsArrayTypeSerializer(idRes, null);
/*     */     case PROPERTY: 
/*  79 */       return new AsPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */     
/*     */     case WRAPPER_OBJECT: 
/*  82 */       return new AsWrapperTypeSerializer(idRes, null);
/*     */     case EXTERNAL_PROPERTY: 
/*  84 */       return new AsExternalTypeSerializer(idRes, null, this._typeProperty);
/*     */     
/*     */ 
/*     */     case EXISTING_PROPERTY: 
/*  88 */       return new AsExistingPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */     }
/*  90 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
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
/*     */   public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*     */   {
/* 103 */     if (this._idType == JsonTypeInfo.Id.NONE) { return null;
/*     */     }
/* 105 */     TypeIdResolver idRes = idResolver(config, baseType, subtypes, false, true);
/*     */     
/*     */ 
/* 108 */     switch (this._includeAs) {
/*     */     case WRAPPER_ARRAY: 
/* 110 */       return new AsArrayTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, this._defaultImpl);
/*     */     
/*     */     case PROPERTY: 
/*     */     case EXISTING_PROPERTY: 
/* 114 */       return new AsPropertyTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, this._defaultImpl, this._includeAs);
/*     */     
/*     */     case WRAPPER_OBJECT: 
/* 117 */       return new AsWrapperTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, this._defaultImpl);
/*     */     
/*     */     case EXTERNAL_PROPERTY: 
/* 120 */       return new AsExternalTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, this._defaultImpl);
/*     */     }
/*     */     
/* 123 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder inclusion(JsonTypeInfo.As includeAs)
/*     */   {
/* 134 */     if (includeAs == null) {
/* 135 */       throw new IllegalArgumentException("includeAs can not be null");
/*     */     }
/* 137 */     this._includeAs = includeAs;
/* 138 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder typeProperty(String typeIdPropName)
/*     */   {
/* 148 */     if ((typeIdPropName == null) || (typeIdPropName.length() == 0)) {
/* 149 */       typeIdPropName = this._idType.getDefaultPropertyName();
/*     */     }
/* 151 */     this._typeProperty = typeIdPropName;
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public StdTypeResolverBuilder defaultImpl(Class<?> defaultImpl)
/*     */   {
/* 157 */     this._defaultImpl = defaultImpl;
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public StdTypeResolverBuilder typeIdVisibility(boolean isVisible)
/*     */   {
/* 163 */     this._typeIdVisible = isVisible;
/* 164 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */   public Class<?> getDefaultImpl() { return this._defaultImpl; }
/*     */   
/* 175 */   public String getTypeProperty() { return this._typeProperty; }
/* 176 */   public boolean isTypeIdVisible() { return this._typeIdVisible; }
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
/*     */   protected TypeIdResolver idResolver(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser)
/*     */   {
/* 193 */     if (this._customIdResolver != null) return this._customIdResolver;
/* 194 */     if (this._idType == null) throw new IllegalStateException("Can not build, 'init()' not yet called");
/* 195 */     switch (this._idType) {
/*     */     case CLASS: 
/* 197 */       return new ClassNameIdResolver(baseType, config.getTypeFactory());
/*     */     case MINIMAL_CLASS: 
/* 199 */       return new MinimalClassNameIdResolver(baseType, config.getTypeFactory());
/*     */     case NAME: 
/* 201 */       return TypeNameIdResolver.construct(config, baseType, subtypes, forSer, forDeser);
/*     */     case NONE: 
/* 203 */       return null;
/*     */     }
/*     */     
/* 206 */     throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\StdTypeResolverBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */