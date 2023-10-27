/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import com.fasterxml.jackson.core.SerializableString;
/*    */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*    */ import com.fasterxml.jackson.databind.SerializationConfig;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import java.util.Collection;
/*    */ import java.util.EnumMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EnumValues
/*    */ {
/*    */   private final Class<Enum<?>> _enumClass;
/*    */   private final EnumMap<?, SerializableString> _values;
/*    */   
/*    */   private EnumValues(Class<Enum<?>> enumClass, Map<Enum<?>, SerializableString> v)
/*    */   {
/* 25 */     this._enumClass = enumClass;
/* 26 */     this._values = new EnumMap(v);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static EnumValues construct(SerializationConfig config, Class<Enum<?>> enumClass)
/*    */   {
/* 34 */     if (config.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 35 */       return constructFromToString(config, enumClass);
/*    */     }
/* 37 */     return constructFromName(config, enumClass);
/*    */   }
/*    */   
/*    */ 
/*    */   public static EnumValues constructFromName(MapperConfig<?> config, Class<Enum<?>> enumClass)
/*    */   {
/* 43 */     Class<? extends Enum<?>> cls = ClassUtil.findEnumType(enumClass);
/* 44 */     Enum<?>[] values = (Enum[])cls.getEnumConstants();
/* 45 */     if (values != null)
/*    */     {
/* 47 */       Map<Enum<?>, SerializableString> map = new HashMap();
/* 48 */       for (Enum<?> en : values) {
/* 49 */         String value = config.getAnnotationIntrospector().findEnumValue(en);
/* 50 */         map.put(en, config.compileString(value));
/*    */       }
/* 52 */       return new EnumValues(enumClass, map);
/*    */     }
/* 54 */     throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
/*    */   }
/*    */   
/*    */   public static EnumValues constructFromToString(MapperConfig<?> config, Class<Enum<?>> enumClass)
/*    */   {
/* 59 */     Class<? extends Enum<?>> cls = ClassUtil.findEnumType(enumClass);
/* 60 */     Enum<?>[] values = (Enum[])cls.getEnumConstants();
/* 61 */     if (values != null)
/*    */     {
/* 63 */       Map<Enum<?>, SerializableString> map = new HashMap();
/* 64 */       for (Enum<?> en : values) {
/* 65 */         map.put(en, config.compileString(en.toString()));
/*    */       }
/* 67 */       return new EnumValues(enumClass, map);
/*    */     }
/* 69 */     throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
/*    */   }
/*    */   
/* 72 */   public SerializableString serializedValueFor(Enum<?> key) { return (SerializableString)this._values.get(key); }
/* 73 */   public Collection<SerializableString> values() { return this._values.values(); }
/*    */   
/*    */ 
/*    */   public EnumMap<?, SerializableString> internalMap()
/*    */   {
/* 78 */     return this._values;
/*    */   }
/*    */   
/*    */   public Class<Enum<?>> getEnumClass()
/*    */   {
/* 83 */     return this._enumClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\EnumValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */