/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultDeserializationContext
/*     */   extends DeserializationContext
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected transient LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds;
/*     */   private List<ObjectIdResolver> _objectIdResolvers;
/*     */   
/*     */   protected DefaultDeserializationContext(DeserializerFactory df, DeserializerCache cache)
/*     */   {
/*  43 */     super(df, cache);
/*     */   }
/*     */   
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */   {
/*  48 */     super(src, config, jp, values);
/*     */   }
/*     */   
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializerFactory factory)
/*     */   {
/*  53 */     super(src, factory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src)
/*     */   {
/*  60 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultDeserializationContext copy()
/*     */   {
/*  72 */     throw new IllegalStateException("DefaultDeserializationContext sub-class not overriding copy()");
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
/*     */   public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType)
/*     */   {
/*  87 */     if (id == null) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     ObjectIdGenerator.IdKey key = gen.key(id);
/*     */     
/*  93 */     if (this._objectIds == null) {
/*  94 */       this._objectIds = new LinkedHashMap();
/*     */     } else {
/*  96 */       ReadableObjectId entry = (ReadableObjectId)this._objectIds.get(key);
/*  97 */       if (entry != null) {
/*  98 */         return entry;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 103 */     ObjectIdResolver resolver = null;
/*     */     
/* 105 */     if (this._objectIdResolvers == null) {
/* 106 */       this._objectIdResolvers = new ArrayList(8);
/*     */     } else {
/* 108 */       for (ObjectIdResolver res : this._objectIdResolvers) {
/* 109 */         if (res.canUseFor(resolverType)) {
/* 110 */           resolver = res;
/* 111 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (resolver == null) {
/* 117 */       resolver = resolverType.newForDeserialization(this);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */       this._objectIdResolvers.add(resolver);
/*     */     }
/*     */     
/* 130 */     ReadableObjectId entry = new ReadableObjectId(key);
/* 131 */     entry.setResolver(resolver);
/* 132 */     this._objectIds.put(key, entry);
/* 133 */     return entry;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen)
/*     */   {
/* 139 */     return findObjectId(id, gen, new SimpleObjectIdResolver());
/*     */   }
/*     */   
/*     */   public void checkUnresolvedObjectId()
/*     */     throws UnresolvedForwardReference
/*     */   {
/* 145 */     if (this._objectIds == null) {
/* 146 */       return;
/*     */     }
/*     */     
/* 149 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)) {
/* 150 */       return;
/*     */     }
/* 152 */     UnresolvedForwardReference exception = null;
/* 153 */     for (Map.Entry<ObjectIdGenerator.IdKey, ReadableObjectId> entry : this._objectIds.entrySet()) {
/* 154 */       roid = (ReadableObjectId)entry.getValue();
/* 155 */       if (roid.hasReferringProperties()) {
/* 156 */         if (exception == null) {
/* 157 */           exception = new UnresolvedForwardReference("Unresolved forward references for: ");
/*     */         }
/* 159 */         for (iterator = roid.referringProperties(); iterator.hasNext();) {
/* 160 */           ReadableObjectId.Referring referring = (ReadableObjectId.Referring)iterator.next();
/* 161 */           exception.addUnresolvedId(roid.getKey().key, referring.getBeanType(), referring.getLocation());
/*     */         } } }
/*     */     ReadableObjectId roid;
/*     */     Iterator<ReadableObjectId.Referring> iterator;
/* 165 */     if (exception != null) {
/* 166 */       throw exception;
/*     */     }
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
/*     */   public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef)
/*     */     throws JsonMappingException
/*     */   {
/* 181 */     if (deserDef == null) {
/* 182 */       return null;
/*     */     }
/*     */     JsonDeserializer<?> deser;
/*     */     JsonDeserializer<?> deser;
/* 186 */     if ((deserDef instanceof JsonDeserializer)) {
/* 187 */       deser = (JsonDeserializer)deserDef;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 192 */       if (!(deserDef instanceof Class)) {
/* 193 */         throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
/*     */       }
/* 195 */       Class<?> deserClass = (Class)deserDef;
/*     */       
/* 197 */       if ((deserClass == JsonDeserializer.None.class) || (ClassUtil.isBogusClass(deserClass))) {
/* 198 */         return null;
/*     */       }
/* 200 */       if (!JsonDeserializer.class.isAssignableFrom(deserClass)) {
/* 201 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<JsonDeserializer>");
/*     */       }
/* 203 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 204 */       deser = hi == null ? null : hi.deserializerInstance(this._config, ann, deserClass);
/* 205 */       if (deser == null) {
/* 206 */         deser = (JsonDeserializer)ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 211 */     if ((deser instanceof ResolvableDeserializer)) {
/* 212 */       ((ResolvableDeserializer)deser).resolve(this);
/*     */     }
/* 214 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */   public final KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
/*     */     throws JsonMappingException
/*     */   {
/* 221 */     if (deserDef == null) {
/* 222 */       return null;
/*     */     }
/*     */     
/*     */     KeyDeserializer deser;
/*     */     KeyDeserializer deser;
/* 227 */     if ((deserDef instanceof KeyDeserializer)) {
/* 228 */       deser = (KeyDeserializer)deserDef;
/*     */     } else {
/* 230 */       if (!(deserDef instanceof Class)) {
/* 231 */         throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + deserDef.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
/*     */       }
/*     */       
/*     */ 
/* 235 */       Class<?> deserClass = (Class)deserDef;
/*     */       
/* 237 */       if ((deserClass == KeyDeserializer.None.class) || (ClassUtil.isBogusClass(deserClass))) {
/* 238 */         return null;
/*     */       }
/* 240 */       if (!KeyDeserializer.class.isAssignableFrom(deserClass)) {
/* 241 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<KeyDeserializer>");
/*     */       }
/*     */       
/* 244 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 245 */       deser = hi == null ? null : hi.keyDeserializerInstance(this._config, ann, deserClass);
/* 246 */       if (deser == null) {
/* 247 */         deser = (KeyDeserializer)ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 252 */     if ((deser instanceof ResolvableDeserializer)) {
/* 253 */       ((ResolvableDeserializer)deser).resolve(this);
/*     */     }
/* 255 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DefaultDeserializationContext with(DeserializerFactory paramDeserializerFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DefaultDeserializationContext createInstance(DeserializationConfig paramDeserializationConfig, JsonParser paramJsonParser, InjectableValues paramInjectableValues);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Impl
/*     */     extends DefaultDeserializationContext
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Impl(DeserializerFactory df)
/*     */     {
/* 295 */       super(null);
/*     */     }
/*     */     
/*     */     protected Impl(Impl src, DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */     {
/* 300 */       super(config, jp, values);
/*     */     }
/*     */     
/* 303 */     protected Impl(Impl src) { super(); }
/*     */     
/*     */     protected Impl(Impl src, DeserializerFactory factory) {
/* 306 */       super(factory);
/*     */     }
/*     */     
/*     */     public DefaultDeserializationContext copy()
/*     */     {
/* 311 */       if (getClass() != Impl.class) {
/* 312 */         return super.copy();
/*     */       }
/* 314 */       return new Impl(this);
/*     */     }
/*     */     
/*     */ 
/*     */     public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */     {
/* 320 */       return new Impl(this, config, jp, values);
/*     */     }
/*     */     
/*     */     public DefaultDeserializationContext with(DeserializerFactory factory)
/*     */     {
/* 325 */       return new Impl(this, factory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\DefaultDeserializationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */