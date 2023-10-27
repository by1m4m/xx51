/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.EnumValues;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public class EnumSerializer
/*     */   extends StdScalarSerializer<Enum<?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final EnumValues _values;
/*     */   protected final Boolean _serializeAsIndex;
/*     */   
/*     */   @Deprecated
/*     */   public EnumSerializer(EnumValues v)
/*     */   {
/*  63 */     this(v, null);
/*     */   }
/*     */   
/*     */   public EnumSerializer(EnumValues v, Boolean serializeAsIndex)
/*     */   {
/*  68 */     super(v.getEnumClass(), false);
/*  69 */     this._values = v;
/*  70 */     this._serializeAsIndex = serializeAsIndex;
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
/*     */   public static EnumSerializer construct(Class<?> enumClass, SerializationConfig config, BeanDescription beanDesc, JsonFormat.Value format)
/*     */   {
/*  87 */     EnumValues v = EnumValues.constructFromName(config, enumClass);
/*  88 */     Boolean serializeAsIndex = _isShapeWrittenUsingIndex(enumClass, format, true);
/*  89 */     return new EnumSerializer(v, serializeAsIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 100 */     if (property != null) {
/* 101 */       JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat(property.getMember());
/* 102 */       if (format != null) {
/* 103 */         Boolean serializeAsIndex = _isShapeWrittenUsingIndex(property.getType().getRawClass(), format, false);
/* 104 */         if (serializeAsIndex != this._serializeAsIndex) {
/* 105 */           return new EnumSerializer(this._values, serializeAsIndex);
/*     */         }
/*     */       }
/*     */     }
/* 109 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumValues getEnumValues()
/*     */   {
/* 118 */     return this._values;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Enum<?> en, JsonGenerator gen, SerializerProvider serializers)
/*     */     throws IOException
/*     */   {
/* 131 */     if (_serializeAsIndex(serializers)) {
/* 132 */       gen.writeNumber(en.ordinal());
/* 133 */       return;
/*     */     }
/*     */     
/* 136 */     if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 137 */       gen.writeString(en.toString());
/* 138 */       return;
/*     */     }
/* 140 */     gen.writeString(this._values.serializedValueFor(en));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 147 */     if (_serializeAsIndex(provider)) {
/* 148 */       return createSchemaNode("integer", true);
/*     */     }
/* 150 */     ObjectNode objectNode = createSchemaNode("string", true);
/* 151 */     ArrayNode enumNode; if (typeHint != null) {
/* 152 */       JavaType type = provider.constructType(typeHint);
/* 153 */       if (type.isEnumType()) {
/* 154 */         enumNode = objectNode.putArray("enum");
/* 155 */         for (SerializableString value : this._values.values()) {
/* 156 */           enumNode.add(value.getValue());
/*     */         }
/*     */       }
/*     */     }
/* 160 */     return objectNode;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 167 */     SerializerProvider serializers = visitor.getProvider();
/* 168 */     if (_serializeAsIndex(serializers)) {
/* 169 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 170 */       if (v2 != null) {
/* 171 */         v2.numberType(JsonParser.NumberType.INT);
/*     */       }
/* 173 */       return;
/*     */     }
/* 175 */     boolean usingToString = (serializers != null) && (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
/*     */     
/*     */ 
/* 178 */     JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(typeHint);
/* 179 */     if ((typeHint != null) && (stringVisitor != null)) {
/* 180 */       Set<String> enums = new LinkedHashSet();
/* 181 */       for (SerializableString value : this._values.values()) {
/* 182 */         if (usingToString) {
/* 183 */           enums.add(value.toString());
/*     */         } else {
/* 185 */           enums.add(value.getValue());
/*     */         }
/*     */       }
/* 188 */       stringVisitor.enumTypes(enums);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _serializeAsIndex(SerializerProvider serializers)
/*     */   {
/* 200 */     if (this._serializeAsIndex != null) {
/* 201 */       return this._serializeAsIndex.booleanValue();
/*     */     }
/* 203 */     return serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass)
/*     */   {
/* 212 */     JsonFormat.Shape shape = format == null ? null : format.getShape();
/* 213 */     if (shape == null) {
/* 214 */       return null;
/*     */     }
/* 216 */     if ((shape == JsonFormat.Shape.ANY) || (shape == JsonFormat.Shape.SCALAR)) {
/* 217 */       return null;
/*     */     }
/* 219 */     if (shape == JsonFormat.Shape.STRING) {
/* 220 */       return Boolean.FALSE;
/*     */     }
/*     */     
/* 223 */     if ((shape.isNumeric()) || (shape == JsonFormat.Shape.ARRAY)) {
/* 224 */       return Boolean.TRUE;
/*     */     }
/* 226 */     throw new IllegalArgumentException("Unsupported serialization shape (" + shape + ") for Enum " + enumClass.getName() + ", not supported as " + (fromClass ? "class" : "property") + " annotation");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\EnumSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */