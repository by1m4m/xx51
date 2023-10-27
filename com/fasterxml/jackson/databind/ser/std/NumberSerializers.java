/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class NumberSerializers
/*     */ {
/*     */   public static void addAll(Map<String, JsonSerializer<?>> allDeserializers)
/*     */   {
/*  28 */     JsonSerializer<?> intS = new IntegerSerializer();
/*  29 */     allDeserializers.put(Integer.class.getName(), intS);
/*  30 */     allDeserializers.put(Integer.TYPE.getName(), intS);
/*  31 */     allDeserializers.put(Long.class.getName(), LongSerializer.instance);
/*  32 */     allDeserializers.put(Long.TYPE.getName(), LongSerializer.instance);
/*  33 */     allDeserializers.put(Byte.class.getName(), IntLikeSerializer.instance);
/*  34 */     allDeserializers.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
/*  35 */     allDeserializers.put(Short.class.getName(), ShortSerializer.instance);
/*  36 */     allDeserializers.put(Short.TYPE.getName(), ShortSerializer.instance);
/*     */     
/*     */ 
/*  39 */     allDeserializers.put(Float.class.getName(), FloatSerializer.instance);
/*  40 */     allDeserializers.put(Float.TYPE.getName(), FloatSerializer.instance);
/*  41 */     allDeserializers.put(Double.class.getName(), DoubleSerializer.instance);
/*  42 */     allDeserializers.put(Double.TYPE.getName(), DoubleSerializer.instance);
/*     */   }
/*     */   
/*     */ 
/*     */   protected static abstract class Base<T>
/*     */     extends StdScalarSerializer<T>
/*     */     implements ContextualSerializer
/*     */   {
/*     */     protected final JsonParser.NumberType _numberType;
/*     */     
/*     */     protected final String _schemaType;
/*     */     
/*     */     protected final boolean _isInt;
/*     */     
/*     */ 
/*     */     protected Base(Class<T> cls, JsonParser.NumberType numberType, String schemaType)
/*     */     {
/*  59 */       super();
/*  60 */       this._numberType = numberType;
/*  61 */       this._schemaType = schemaType;
/*  62 */       this._isInt = ((numberType == JsonParser.NumberType.INT) || (numberType == JsonParser.NumberType.LONG) || (numberType == JsonParser.NumberType.BIG_INTEGER));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  70 */       return createSchemaNode(this._schemaType, true);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/*  76 */       if (this._isInt) {
/*  77 */         JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/*  78 */         if (v2 != null) {
/*  79 */           v2.numberType(this._numberType);
/*     */         }
/*     */       } else {
/*  82 */         JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(typeHint);
/*  83 */         if (v2 != null) {
/*  84 */           v2.numberType(this._numberType);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/*  93 */       if (property != null) {
/*  94 */         AnnotatedMember m = property.getMember();
/*  95 */         if (m != null) {
/*  96 */           JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat(m);
/*  97 */           if (format != null) {
/*  98 */             switch (NumberSerializers.1.$SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[format.getShape().ordinal()]) {
/*     */             case 1: 
/* 100 */               return ToStringSerializer.instance;
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/* 106 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class ShortSerializer
/*     */     extends NumberSerializers.Base<Short>
/*     */   {
/* 119 */     static final ShortSerializer instance = new ShortSerializer();
/*     */     
/* 121 */     public ShortSerializer() { super(JsonParser.NumberType.INT, "number"); }
/*     */     
/*     */     public void serialize(Short value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */     {
/* 125 */       jgen.writeNumber(value.shortValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntegerSerializer
/*     */     extends NumberSerializers.Base<Integer>
/*     */   {
/*     */     public IntegerSerializer()
/*     */     {
/* 139 */       super(JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */     
/*     */     public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 143 */       jgen.writeNumber(value.intValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serializeWithType(Integer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException
/*     */     {
/* 151 */       serialize(value, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntLikeSerializer
/*     */     extends NumberSerializers.Base<Number>
/*     */   {
/* 163 */     static final IntLikeSerializer instance = new IntLikeSerializer();
/*     */     
/*     */     public IntLikeSerializer() {
/* 166 */       super(JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */     
/*     */     public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */     {
/* 171 */       jgen.writeNumber(value.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static final class LongSerializer extends NumberSerializers.Base<Long>
/*     */   {
/* 178 */     static final LongSerializer instance = new LongSerializer();
/*     */     
/* 180 */     public LongSerializer() { super(JsonParser.NumberType.LONG, "number"); }
/*     */     
/*     */     public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */     {
/* 184 */       jgen.writeNumber(value.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static final class FloatSerializer extends NumberSerializers.Base<Float>
/*     */   {
/* 191 */     static final FloatSerializer instance = new FloatSerializer();
/*     */     
/* 193 */     public FloatSerializer() { super(JsonParser.NumberType.FLOAT, "number"); }
/*     */     
/*     */     public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */     {
/* 197 */       jgen.writeNumber(value.floatValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class DoubleSerializer
/*     */     extends NumberSerializers.Base<Double>
/*     */   {
/* 211 */     static final DoubleSerializer instance = new DoubleSerializer();
/*     */     
/* 213 */     public DoubleSerializer() { super(JsonParser.NumberType.DOUBLE, "number"); }
/*     */     
/*     */     public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */     {
/* 217 */       jgen.writeNumber(value.doubleValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serializeWithType(Double value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException
/*     */     {
/* 225 */       serialize(value, jgen, provider);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */