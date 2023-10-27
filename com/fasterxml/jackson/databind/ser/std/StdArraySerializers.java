/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class StdArraySerializers
/*     */ {
/*  25 */   protected static final HashMap<String, JsonSerializer<?>> _arraySerializers = new HashMap();
/*     */   
/*     */   static
/*     */   {
/*  29 */     _arraySerializers.put(boolean[].class.getName(), new BooleanArraySerializer());
/*  30 */     _arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
/*  31 */     _arraySerializers.put(char[].class.getName(), new CharArraySerializer());
/*  32 */     _arraySerializers.put(short[].class.getName(), new ShortArraySerializer());
/*  33 */     _arraySerializers.put(int[].class.getName(), new IntArraySerializer());
/*  34 */     _arraySerializers.put(long[].class.getName(), new LongArraySerializer());
/*  35 */     _arraySerializers.put(float[].class.getName(), new FloatArraySerializer());
/*  36 */     _arraySerializers.put(double[].class.getName(), new DoubleArraySerializer());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonSerializer<?> findStandardImpl(Class<?> cls)
/*     */   {
/*  46 */     return (JsonSerializer)_arraySerializers.get(cls.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract class TypedPrimitiveArraySerializer<T>
/*     */     extends ArraySerializerBase<T>
/*     */   {
/*     */     protected final TypeSerializer _valueTypeSerializer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected TypedPrimitiveArraySerializer(Class<T> cls)
/*     */     {
/*  67 */       super();
/*  68 */       this._valueTypeSerializer = null;
/*     */     }
/*     */     
/*     */     protected TypedPrimitiveArraySerializer(TypedPrimitiveArraySerializer<T> src, BeanProperty prop, TypeSerializer vts)
/*     */     {
/*  73 */       super(prop);
/*  74 */       this._valueTypeSerializer = vts;
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
/*     */   public static class BooleanArraySerializer
/*     */     extends ArraySerializerBase<boolean[]>
/*     */   {
/*  88 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Boolean.class);
/*     */     
/*  90 */     public BooleanArraySerializer() { super(null); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 103 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 109 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, boolean[] value)
/*     */     {
/* 114 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(boolean[] value)
/*     */     {
/* 119 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(boolean[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 125 */       int len = value.length;
/* 126 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 127 */         serializeContents(value, jgen, provider);
/* 128 */         return;
/*     */       }
/* 130 */       jgen.writeStartArray(len);
/* 131 */       serializeContents(value, jgen, provider);
/* 132 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(boolean[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 139 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 140 */         jgen.writeBoolean(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 147 */       ObjectNode o = createSchemaNode("array", true);
/* 148 */       o.set("items", createSchemaNode("boolean"));
/* 149 */       return o;
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 156 */       if (visitor != null) {
/* 157 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 158 */         if (v2 != null) {
/* 159 */           v2.itemsFormat(JsonFormatTypes.BOOLEAN);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ByteArraySerializer
/*     */     extends StdSerializer<byte[]>
/*     */   {
/*     */     public ByteArraySerializer()
/*     */     {
/* 176 */       super();
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, byte[] value)
/*     */     {
/* 181 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 188 */       jgen.writeBinary(provider.getConfig().getBase64Variant(), value, 0, value.length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void serializeWithType(byte[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException
/*     */     {
/* 197 */       typeSer.writeTypePrefixForScalar(value, jgen);
/* 198 */       jgen.writeBinary(provider.getConfig().getBase64Variant(), value, 0, value.length);
/*     */       
/* 200 */       typeSer.writeTypeSuffixForScalar(value, jgen);
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 206 */       ObjectNode o = createSchemaNode("array", true);
/* 207 */       ObjectNode itemSchema = createSchemaNode("string");
/* 208 */       return o.set("items", itemSchema);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 215 */       if (visitor != null) {
/* 216 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 217 */         if (v2 != null) {
/* 218 */           v2.itemsFormat(JsonFormatTypes.STRING);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class ShortArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<short[]>
/*     */   {
/* 228 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Short.TYPE);
/*     */     
/* 230 */     public ShortArraySerializer() { super(); }
/*     */     
/* 232 */     public ShortArraySerializer(ShortArraySerializer src, BeanProperty prop, TypeSerializer vts) { super(prop, vts); }
/*     */     
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 237 */       return new ShortArraySerializer(this, this._property, vts);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 242 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 248 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, short[] value)
/*     */     {
/* 253 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(short[] value)
/*     */     {
/* 258 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(short[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 264 */       int len = value.length;
/* 265 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 266 */         serializeContents(value, jgen, provider);
/* 267 */         return;
/*     */       }
/* 269 */       jgen.writeStartArray(len);
/* 270 */       serializeContents(value, jgen, provider);
/* 271 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serializeContents(short[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 279 */       if (this._valueTypeSerializer != null) {
/* 280 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 281 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, jgen, Short.TYPE);
/* 282 */           jgen.writeNumber(value[i]);
/* 283 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, jgen);
/*     */         }
/* 285 */         return;
/*     */       }
/* 287 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 288 */         jgen.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 296 */       ObjectNode o = createSchemaNode("array", true);
/* 297 */       return o.set("items", createSchemaNode("integer"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 304 */       if (visitor != null) {
/* 305 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 306 */         if (v2 != null) {
/* 307 */           v2.itemsFormat(JsonFormatTypes.INTEGER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CharArraySerializer
/*     */     extends StdSerializer<char[]>
/*     */   {
/*     */     public CharArraySerializer()
/*     */     {
/* 323 */       super();
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, char[] value) {
/* 327 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serialize(char[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 335 */       if (provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 336 */         jgen.writeStartArray(value.length);
/* 337 */         _writeArrayContents(jgen, value);
/* 338 */         jgen.writeEndArray();
/*     */       } else {
/* 340 */         jgen.writeString(value, 0, value.length);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void serializeWithType(char[] value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 350 */       if (provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 351 */         typeSer.writeTypePrefixForArray(value, jgen);
/* 352 */         _writeArrayContents(jgen, value);
/* 353 */         typeSer.writeTypeSuffixForArray(value, jgen);
/*     */       } else {
/* 355 */         typeSer.writeTypePrefixForScalar(value, jgen);
/* 356 */         jgen.writeString(value, 0, value.length);
/* 357 */         typeSer.writeTypeSuffixForScalar(value, jgen);
/*     */       }
/*     */     }
/*     */     
/*     */     private final void _writeArrayContents(JsonGenerator jgen, char[] value)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 364 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 365 */         jgen.writeString(value, i, 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 372 */       ObjectNode o = createSchemaNode("array", true);
/* 373 */       ObjectNode itemSchema = createSchemaNode("string");
/* 374 */       itemSchema.put("type", "string");
/* 375 */       return o.set("items", itemSchema);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 382 */       if (visitor != null) {
/* 383 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 384 */         if (v2 != null) {
/* 385 */           v2.itemsFormat(JsonFormatTypes.STRING);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class IntArraySerializer
/*     */     extends ArraySerializerBase<int[]>
/*     */   {
/* 395 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Integer.TYPE);
/*     */     
/* 397 */     public IntArraySerializer() { super(null); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 405 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 410 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 416 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, int[] value)
/*     */     {
/* 421 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(int[] value)
/*     */     {
/* 426 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(int[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 432 */       int len = value.length;
/* 433 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 434 */         serializeContents(value, jgen, provider);
/* 435 */         return;
/*     */       }
/* 437 */       jgen.writeStartArray(len);
/* 438 */       serializeContents(value, jgen, provider);
/* 439 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(int[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 446 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 447 */         jgen.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 453 */       return createSchemaNode("array", true).set("items", createSchemaNode("integer"));
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 459 */       if (visitor != null) {
/* 460 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 461 */         if (v2 != null) {
/* 462 */           v2.itemsFormat(JsonFormatTypes.INTEGER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class LongArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<long[]>
/*     */   {
/* 472 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Long.TYPE);
/*     */     
/* 474 */     public LongArraySerializer() { super(); }
/*     */     
/*     */     public LongArraySerializer(LongArraySerializer src, BeanProperty prop, TypeSerializer vts) {
/* 477 */       super(prop, vts);
/*     */     }
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 482 */       return new LongArraySerializer(this, this._property, vts);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 487 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 493 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, long[] value)
/*     */     {
/* 498 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(long[] value)
/*     */     {
/* 503 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(long[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 509 */       int len = value.length;
/* 510 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 511 */         serializeContents(value, jgen, provider);
/* 512 */         return;
/*     */       }
/* 514 */       jgen.writeStartArray(len);
/* 515 */       serializeContents(value, jgen, provider);
/* 516 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(long[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 523 */       if (this._valueTypeSerializer != null) {
/* 524 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 525 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, jgen, Long.TYPE);
/* 526 */           jgen.writeNumber(value[i]);
/* 527 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, jgen);
/*     */         }
/* 529 */         return;
/*     */       }
/*     */       
/* 532 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 533 */         jgen.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 540 */       return createSchemaNode("array", true).set("items", createSchemaNode("number", true));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 548 */       if (visitor != null) {
/* 549 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 550 */         if (v2 != null) {
/* 551 */           v2.itemsFormat(JsonFormatTypes.NUMBER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class FloatArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<float[]>
/*     */   {
/* 561 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Float.TYPE);
/*     */     
/*     */     public FloatArraySerializer() {
/* 564 */       super();
/*     */     }
/*     */     
/*     */     public FloatArraySerializer(FloatArraySerializer src, BeanProperty prop, TypeSerializer vts) {
/* 568 */       super(prop, vts);
/*     */     }
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 573 */       return new FloatArraySerializer(this, this._property, vts);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 578 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 584 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, float[] value)
/*     */     {
/* 589 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(float[] value)
/*     */     {
/* 594 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(float[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 600 */       int len = value.length;
/* 601 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 602 */         serializeContents(value, jgen, provider);
/* 603 */         return;
/*     */       }
/* 605 */       jgen.writeStartArray(len);
/* 606 */       serializeContents(value, jgen, provider);
/* 607 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(float[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 614 */       if (this._valueTypeSerializer != null) {
/* 615 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 616 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, jgen, Float.TYPE);
/* 617 */           jgen.writeNumber(value[i]);
/* 618 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, jgen);
/*     */         }
/* 620 */         return;
/*     */       }
/* 622 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 623 */         jgen.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 629 */       return createSchemaNode("array", true).set("items", createSchemaNode("number"));
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 635 */       if (visitor != null) {
/* 636 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 637 */         if (v2 != null) {
/* 638 */           v2.itemsFormat(JsonFormatTypes.NUMBER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class DoubleArraySerializer
/*     */     extends ArraySerializerBase<double[]>
/*     */   {
/* 648 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Double.TYPE);
/*     */     
/* 650 */     public DoubleArraySerializer() { super(null); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 658 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 663 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 669 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, double[] value)
/*     */     {
/* 674 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(double[] value)
/*     */     {
/* 679 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(double[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 685 */       int len = value.length;
/* 686 */       if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 687 */         serializeContents(value, jgen, provider);
/* 688 */         return;
/*     */       }
/* 690 */       jgen.writeStartArray(len);
/* 691 */       serializeContents(value, jgen, provider);
/* 692 */       jgen.writeEndArray();
/*     */     }
/*     */     
/*     */     public void serializeContents(double[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 698 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 699 */         jgen.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 705 */       return createSchemaNode("array", true).set("items", createSchemaNode("number"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 712 */       if (visitor != null) {
/* 713 */         JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 714 */         if (v2 != null) {
/* 715 */           v2.itemsFormat(JsonFormatTypes.NUMBER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdArraySerializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */