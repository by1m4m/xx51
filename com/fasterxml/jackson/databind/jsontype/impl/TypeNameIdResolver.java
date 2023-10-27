/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DatabindContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeNameIdResolver
/*     */   extends TypeIdResolverBase
/*     */ {
/*     */   protected final MapperConfig<?> _config;
/*     */   protected final HashMap<String, String> _typeToId;
/*     */   protected final HashMap<String, JavaType> _idToType;
/*     */   
/*     */   protected TypeNameIdResolver(MapperConfig<?> config, JavaType baseType, HashMap<String, String> typeToId, HashMap<String, JavaType> idToType)
/*     */   {
/*  29 */     super(baseType, config.getTypeFactory());
/*  30 */     this._config = config;
/*  31 */     this._typeToId = typeToId;
/*  32 */     this._idToType = idToType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static TypeNameIdResolver construct(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser)
/*     */   {
/*  39 */     if (forSer == forDeser) throw new IllegalArgumentException();
/*  40 */     HashMap<String, String> typeToId = null;
/*  41 */     HashMap<String, JavaType> idToType = null;
/*     */     
/*  43 */     if (forSer) {
/*  44 */       typeToId = new HashMap();
/*     */     }
/*  46 */     if (forDeser) {
/*  47 */       idToType = new HashMap();
/*     */     }
/*  49 */     if (subtypes != null) {
/*  50 */       for (NamedType t : subtypes)
/*     */       {
/*     */ 
/*     */ 
/*  54 */         Class<?> cls = t.getType();
/*  55 */         String id = t.hasName() ? t.getName() : _defaultTypeId(cls);
/*  56 */         if (forSer) {
/*  57 */           typeToId.put(cls.getName(), id);
/*     */         }
/*  59 */         if (forDeser)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  64 */           JavaType prev = (JavaType)idToType.get(id);
/*  65 */           if ((prev == null) || 
/*  66 */             (!cls.isAssignableFrom(prev.getRawClass())))
/*     */           {
/*     */ 
/*     */ 
/*  70 */             idToType.put(id, config.constructType(cls)); }
/*     */         }
/*     */       }
/*     */     }
/*  74 */     return new TypeNameIdResolver(config, baseType, typeToId, idToType);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.Id getMechanism() {
/*  78 */     return JsonTypeInfo.Id.NAME;
/*     */   }
/*     */   
/*     */   public String idFromValue(Object value)
/*     */   {
/*  83 */     Class<?> cls = this._typeFactory.constructType(value.getClass()).getRawClass();
/*  84 */     String key = cls.getName();
/*     */     String name;
/*  86 */     synchronized (this._typeToId) {
/*  87 */       name = (String)this._typeToId.get(key);
/*  88 */       if (name == null)
/*     */       {
/*     */ 
/*  91 */         if (this._config.isAnnotationProcessingEnabled()) {
/*  92 */           BeanDescription beanDesc = this._config.introspectClassAnnotations(cls);
/*  93 */           name = this._config.getAnnotationIntrospector().findTypeName(beanDesc.getClassInfo());
/*     */         }
/*  95 */         if (name == null)
/*     */         {
/*  97 */           name = _defaultTypeId(cls);
/*     */         }
/*  99 */         this._typeToId.put(key, name);
/*     */       }
/*     */     }
/* 102 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String idFromValueAndType(Object value, Class<?> type)
/*     */   {
/* 110 */     if (value == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     return idFromValue(value);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JavaType typeFromId(String id)
/*     */   {
/* 119 */     return _typeFromId(id);
/*     */   }
/*     */   
/*     */   public JavaType typeFromId(DatabindContext context, String id)
/*     */   {
/* 124 */     return _typeFromId(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _typeFromId(String id)
/*     */   {
/* 133 */     return (JavaType)this._idToType.get(id);
/*     */   }
/*     */   
/*     */   public String getDescForKnownTypeIds()
/*     */   {
/* 138 */     return new TreeSet(this._idToType.keySet()).toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 143 */     StringBuilder sb = new StringBuilder();
/* 144 */     sb.append('[').append(getClass().getName());
/* 145 */     sb.append("; id-to-type=").append(this._idToType);
/* 146 */     sb.append(']');
/* 147 */     return sb.toString();
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
/*     */   protected static String _defaultTypeId(Class<?> cls)
/*     */   {
/* 162 */     String n = cls.getName();
/* 163 */     int ix = n.lastIndexOf('.');
/* 164 */     return ix < 0 ? n : n.substring(ix + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeNameIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */