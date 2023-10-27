/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer.None;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public abstract class DefaultSerializerProvider
/*     */   extends SerializerProvider
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected transient Map<Object, WritableObjectId> _seenObjectIds;
/*     */   protected transient ArrayList<ObjectIdGenerator<?>> _objectIdGenerators;
/*     */   
/*     */   protected DefaultSerializerProvider() {}
/*     */   
/*     */   protected DefaultSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f)
/*     */   {
/*  61 */     super(src, config, f);
/*     */   }
/*     */   
/*     */   protected DefaultSerializerProvider(DefaultSerializerProvider src) {
/*  65 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultSerializerProvider copy()
/*     */   {
/*  77 */     throw new IllegalStateException("DefaultSerializerProvider sub-class not overriding copy()");
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
/*     */   public abstract DefaultSerializerProvider createInstance(SerializationConfig paramSerializationConfig, SerializerFactory paramSerializerFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeValue(JsonGenerator gen, Object value)
/*     */     throws IOException
/*     */   {
/* 101 */     if (value == null) {
/* 102 */       _serializeNull(gen);
/* 103 */       return;
/*     */     }
/* 105 */     Class<?> cls = value.getClass();
/*     */     
/* 107 */     JsonSerializer<Object> ser = findTypedValueSerializer(cls, true, null);
/*     */     
/*     */ 
/*     */ 
/* 111 */     String rootName = this._config.getRootName();
/* 112 */     boolean wrap; if (rootName == null)
/*     */     {
/* 114 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 115 */       if (wrap) {
/* 116 */         PropertyName pname = this._rootNames.findRootName(value.getClass(), this._config);
/* 117 */         gen.writeStartObject();
/* 118 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 120 */       if (rootName.length() == 0) {
/* 121 */         wrap = false;
/*     */       }
/*     */       else {
/* 124 */         wrap = true;
/* 125 */         gen.writeStartObject();
/* 126 */         gen.writeFieldName(rootName);
/*     */       }
/*     */     }
/* 129 */     try { ser.serialize(value, gen, this);
/* 130 */       if (wrap) {
/* 131 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 134 */       throw ioe;
/*     */     } catch (Exception e) {
/* 136 */       String msg = e.getMessage();
/* 137 */       if (msg == null) {
/* 138 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 140 */       throw new JsonMappingException(msg, e);
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
/*     */ 
/*     */ 
/*     */   public void serializeValue(JsonGenerator gen, Object value, JavaType rootType)
/*     */     throws IOException
/*     */   {
/* 157 */     if (value == null) {
/* 158 */       _serializeNull(gen);
/* 159 */       return;
/*     */     }
/*     */     
/* 162 */     if (!rootType.getRawClass().isAssignableFrom(value.getClass())) {
/* 163 */       _reportIncompatibleRootType(value, rootType);
/*     */     }
/*     */     
/* 166 */     JsonSerializer<Object> ser = findTypedValueSerializer(rootType, true, null);
/*     */     
/*     */ 
/*     */ 
/* 170 */     String rootName = this._config.getRootName();
/* 171 */     boolean wrap; if (rootName == null)
/*     */     {
/* 173 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 174 */       if (wrap) {
/* 175 */         gen.writeStartObject();
/* 176 */         PropertyName pname = this._rootNames.findRootName(value.getClass(), this._config);
/* 177 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 179 */       if (rootName.length() == 0) {
/* 180 */         wrap = false;
/*     */       }
/*     */       else {
/* 183 */         wrap = true;
/* 184 */         gen.writeStartObject();
/* 185 */         gen.writeFieldName(rootName);
/*     */       }
/*     */     }
/* 188 */     try { ser.serialize(value, gen, this);
/* 189 */       if (wrap) {
/* 190 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 193 */       throw ioe;
/*     */     } catch (Exception e) {
/* 195 */       String msg = e.getMessage();
/* 196 */       if (msg == null) {
/* 197 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 199 */       throw new JsonMappingException(msg, e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 217 */     if (value == null) {
/* 218 */       _serializeNull(gen);
/* 219 */       return;
/*     */     }
/*     */     
/* 222 */     if ((rootType != null) && (!rootType.getRawClass().isAssignableFrom(value.getClass()))) {
/* 223 */       _reportIncompatibleRootType(value, rootType);
/*     */     }
/*     */     
/* 226 */     if (ser == null) {
/* 227 */       ser = findTypedValueSerializer(rootType, true, null);
/*     */     }
/*     */     
/*     */ 
/* 231 */     String rootName = this._config.getRootName();
/* 232 */     boolean wrap; if (rootName == null)
/*     */     {
/* 234 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 235 */       if (wrap) {
/* 236 */         gen.writeStartObject();
/* 237 */         PropertyName pname = rootType == null ? this._rootNames.findRootName(value.getClass(), this._config) : this._rootNames.findRootName(rootType, this._config);
/*     */         
/*     */ 
/* 240 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 242 */       if (rootName.length() == 0) {
/* 243 */         wrap = false;
/*     */       }
/*     */       else {
/* 246 */         wrap = true;
/* 247 */         gen.writeStartObject();
/* 248 */         gen.writeFieldName(rootName);
/*     */       }
/*     */     }
/* 251 */     try { ser.serialize(value, gen, this);
/* 252 */       if (wrap) {
/* 253 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 256 */       throw ioe;
/*     */     } catch (Exception e) {
/* 258 */       String msg = e.getMessage();
/* 259 */       if (msg == null) {
/* 260 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 262 */       throw new JsonMappingException(msg, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializePolymorphic(JsonGenerator gen, Object value, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 275 */     if (value == null) {
/* 276 */       _serializeNull(gen);
/* 277 */       return;
/*     */     }
/* 279 */     Class<?> type = value.getClass();
/* 280 */     JsonSerializer<Object> ser = findValueSerializer(type, null);
/*     */     
/*     */ 
/* 283 */     String rootName = this._config.getRootName();
/* 284 */     boolean wrap; if (rootName == null) {
/* 285 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 286 */       if (wrap) {
/* 287 */         gen.writeStartObject();
/* 288 */         PropertyName pname = this._rootNames.findRootName(type, this._config);
/* 289 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 291 */       if (rootName.length() == 0) {
/* 292 */         wrap = false;
/*     */       } else {
/* 294 */         wrap = true;
/* 295 */         gen.writeStartObject();
/* 296 */         gen.writeFieldName(rootName);
/*     */       }
/*     */     }
/* 299 */     try { ser.serializeWithType(value, gen, this, typeSer);
/* 300 */       if (wrap) {
/* 301 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 304 */       throw ioe;
/*     */     } catch (Exception e) {
/* 306 */       String msg = e.getMessage();
/* 307 */       if (msg == null) {
/* 308 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 310 */       throw new JsonMappingException(msg, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _serializeNull(JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 321 */     JsonSerializer<Object> ser = getDefaultNullValueSerializer();
/*     */     try {
/* 323 */       ser.serialize(null, gen, this);
/*     */     } catch (IOException ioe) {
/* 325 */       throw ioe;
/*     */     } catch (Exception e) {
/* 327 */       String msg = e.getMessage();
/* 328 */       if (msg == null) {
/* 329 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 331 */       throw new JsonMappingException(msg, e);
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
/*     */   public JsonSchema generateJsonSchema(Class<?> type)
/*     */     throws JsonMappingException
/*     */   {
/* 346 */     if (type == null) {
/* 347 */       throw new IllegalArgumentException("A class must be provided");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 352 */     JsonSerializer<Object> ser = findValueSerializer(type, null);
/* 353 */     JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(this, null) : JsonSchema.getDefaultSchemaNode();
/*     */     
/* 355 */     if (!(schemaNode instanceof ObjectNode)) {
/* 356 */       throw new IllegalArgumentException("Class " + type.getName() + " would not be serialized as a JSON object and therefore has no schema");
/*     */     }
/*     */     
/* 359 */     return new JsonSchema((ObjectNode)schemaNode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JavaType javaType, JsonFormatVisitorWrapper visitor)
/*     */     throws JsonMappingException
/*     */   {
/* 372 */     if (javaType == null) {
/* 373 */       throw new IllegalArgumentException("A class must be provided");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 378 */     visitor.setProvider(this);
/* 379 */     findValueSerializer(javaType, null).acceptJsonFormatVisitor(visitor, javaType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasSerializerFor(Class<?> cls, AtomicReference<Throwable> cause)
/*     */   {
/*     */     try
/*     */     {
/* 392 */       JsonSerializer<?> ser = _findExplicitUntypedSerializer(cls);
/* 393 */       return ser != null;
/*     */     } catch (JsonMappingException e) {
/* 395 */       if (cause != null) {
/* 396 */         cause.set(e);
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 399 */       if (cause == null) {
/* 400 */         throw e;
/*     */       }
/* 402 */       cause.set(e);
/*     */     }
/* 404 */     return false;
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
/*     */   public int cachedSerializersCount()
/*     */   {
/* 425 */     return this._serializerCache.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCachedSerializers()
/*     */   {
/* 435 */     this._serializerCache.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WritableObjectId findObjectId(Object forPojo, ObjectIdGenerator<?> generatorType)
/*     */   {
/* 447 */     if (this._seenObjectIds == null) {
/* 448 */       this._seenObjectIds = _createObjectIdMap();
/*     */     } else {
/* 450 */       WritableObjectId oid = (WritableObjectId)this._seenObjectIds.get(forPojo);
/* 451 */       if (oid != null) {
/* 452 */         return oid;
/*     */       }
/*     */     }
/*     */     
/* 456 */     ObjectIdGenerator<?> generator = null;
/*     */     
/* 458 */     if (this._objectIdGenerators == null) {
/* 459 */       this._objectIdGenerators = new ArrayList(8);
/*     */     } else {
/* 461 */       int i = 0; for (int len = this._objectIdGenerators.size(); i < len; i++) {
/* 462 */         ObjectIdGenerator<?> gen = (ObjectIdGenerator)this._objectIdGenerators.get(i);
/* 463 */         if (gen.canUseFor(generatorType)) {
/* 464 */           generator = gen;
/* 465 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 469 */     if (generator == null) {
/* 470 */       generator = generatorType.newForSerialization(this);
/* 471 */       this._objectIdGenerators.add(generator);
/*     */     }
/* 473 */     WritableObjectId oid = new WritableObjectId(generator);
/* 474 */     this._seenObjectIds.put(forPojo, oid);
/* 475 */     return oid;
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
/*     */   protected Map<Object, WritableObjectId> _createObjectIdMap()
/*     */   {
/* 490 */     if (isEnabled(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID)) {
/* 491 */       return new HashMap();
/*     */     }
/* 493 */     return new IdentityHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<Object> serializerInstance(Annotated annotated, Object serDef)
/*     */     throws JsonMappingException
/*     */   {
/* 505 */     if (serDef == null) {
/* 506 */       return null;
/*     */     }
/*     */     JsonSerializer<?> ser;
/*     */     JsonSerializer<?> ser;
/* 510 */     if ((serDef instanceof JsonSerializer)) {
/* 511 */       ser = (JsonSerializer)serDef;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 516 */       if (!(serDef instanceof Class)) {
/* 517 */         throw new IllegalStateException("AnnotationIntrospector returned serializer definition of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */       }
/*     */       
/* 520 */       Class<?> serClass = (Class)serDef;
/*     */       
/* 522 */       if ((serClass == JsonSerializer.None.class) || (ClassUtil.isBogusClass(serClass))) {
/* 523 */         return null;
/*     */       }
/* 525 */       if (!JsonSerializer.class.isAssignableFrom(serClass)) {
/* 526 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + serClass.getName() + "; expected Class<JsonSerializer>");
/*     */       }
/*     */       
/* 529 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 530 */       ser = hi == null ? null : hi.serializerInstance(this._config, annotated, serClass);
/* 531 */       if (ser == null) {
/* 532 */         ser = (JsonSerializer)ClassUtil.createInstance(serClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/* 536 */     return _handleResolvable(ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class Impl
/*     */     extends DefaultSerializerProvider
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     public Impl() {}
/*     */     
/*     */ 
/*     */     public Impl(Impl src)
/*     */     {
/* 553 */       super();
/*     */     }
/*     */     
/* 556 */     protected Impl(SerializerProvider src, SerializationConfig config, SerializerFactory f) { super(config, f); }
/*     */     
/*     */ 
/*     */ 
/*     */     public DefaultSerializerProvider copy()
/*     */     {
/* 562 */       if (getClass() != Impl.class) {
/* 563 */         return super.copy();
/*     */       }
/* 565 */       return new Impl(this);
/*     */     }
/*     */     
/*     */     public Impl createInstance(SerializationConfig config, SerializerFactory jsf)
/*     */     {
/* 570 */       return new Impl(this, config, jsf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\DefaultSerializerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */