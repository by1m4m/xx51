/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*     */ import com.fasterxml.jackson.databind.DatabindContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassNameIdResolver
/*     */   extends TypeIdResolverBase
/*     */ {
/*     */   public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory)
/*     */   {
/*  20 */     super(baseType, typeFactory);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.Id getMechanism() {
/*  24 */     return JsonTypeInfo.Id.CLASS;
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerSubtype(Class<?> type, String name) {}
/*     */   
/*     */   public String idFromValue(Object value)
/*     */   {
/*  32 */     return _idFrom(value, value.getClass());
/*     */   }
/*     */   
/*     */   public String idFromValueAndType(Object value, Class<?> type)
/*     */   {
/*  37 */     return _idFrom(value, type);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JavaType typeFromId(String id)
/*     */   {
/*  43 */     return _typeFromId(id, this._typeFactory);
/*     */   }
/*     */   
/*     */   public JavaType typeFromId(DatabindContext context, String id)
/*     */   {
/*  48 */     return _typeFromId(id, context.getTypeFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _typeFromId(String id, TypeFactory typeFactory)
/*     */   {
/*  57 */     if (id.indexOf('<') > 0) {
/*  58 */       JavaType t = typeFactory.constructFromCanonical(id);
/*     */       
/*  60 */       return t;
/*     */     }
/*     */     try {
/*  63 */       Class<?> cls = ClassUtil.findClass(id);
/*  64 */       return typeFactory.constructSpecializedType(this._baseType, cls);
/*     */     } catch (ClassNotFoundException e) {
/*  66 */       throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found");
/*     */     } catch (Exception e) {
/*  68 */       throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e.getMessage(), e);
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
/*     */   protected final String _idFrom(Object value, Class<?> cls)
/*     */   {
/*  81 */     if ((Enum.class.isAssignableFrom(cls)) && 
/*  82 */       (!cls.isEnum())) {
/*  83 */       cls = cls.getSuperclass();
/*     */     }
/*     */     
/*  86 */     String str = cls.getName();
/*  87 */     if (str.startsWith("java.util"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */       if ((value instanceof EnumSet)) {
/*  97 */         Class<?> enumClass = ClassUtil.findEnumType((EnumSet)value);
/*     */         
/*  99 */         str = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, enumClass).toCanonical();
/* 100 */       } else if ((value instanceof EnumMap)) {
/* 101 */         Class<?> enumClass = ClassUtil.findEnumType((EnumMap)value);
/* 102 */         Class<?> valueClass = Object.class;
/*     */         
/* 104 */         str = TypeFactory.defaultInstance().constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
/*     */       } else {
/* 106 */         String end = str.substring(9);
/* 107 */         if (((end.startsWith(".Arrays$")) || (end.startsWith(".Collections$"))) && (str.indexOf("List") >= 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */           str = "java.util.ArrayList";
/*     */         }
/*     */       }
/* 118 */     } else if (str.indexOf('$') >= 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */       Class<?> outer = ClassUtil.getOuterClass(cls);
/* 127 */       if (outer != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 132 */         Class<?> staticType = this._baseType.getRawClass();
/* 133 */         if (ClassUtil.getOuterClass(staticType) == null)
/*     */         {
/* 135 */           cls = this._baseType.getRawClass();
/* 136 */           str = cls.getName();
/*     */         }
/*     */       }
/*     */     }
/* 140 */     return str;
/*     */   }
/*     */   
/*     */   public String getDescForKnownTypeIds()
/*     */   {
/* 145 */     return "class name used as type id";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\ClassNameIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */