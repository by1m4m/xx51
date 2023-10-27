/*      */ package com.fasterxml.jackson.databind.util;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonStreamContext;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*      */ import com.fasterxml.jackson.core.json.JsonReadContext;
/*      */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ public class TokenBuffer extends JsonGenerator
/*      */ {
/*   33 */   protected static final int DEFAULT_GENERATOR_FEATURES = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectCodec _objectCodec;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _generatorFeatures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _closed;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasNativeTypeIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasNativeObjectIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _mayHaveNativeIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Segment _first;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Segment _last;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _appendAt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _typeId;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _objectId;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  111 */   protected boolean _hasNativeId = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonWriteContext _writeContext;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public TokenBuffer(ObjectCodec codec)
/*      */   {
/*  136 */     this(codec, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer(ObjectCodec codec, boolean hasNativeIds)
/*      */   {
/*  148 */     this._objectCodec = codec;
/*  149 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  150 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  152 */     this._first = (this._last = new Segment());
/*  153 */     this._appendAt = 0;
/*  154 */     this._hasNativeTypeIds = hasNativeIds;
/*  155 */     this._hasNativeObjectIds = hasNativeIds;
/*      */     
/*  157 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer(JsonParser jp)
/*      */   {
/*  165 */     this._objectCodec = jp.getCodec();
/*  166 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  167 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  169 */     this._first = (this._last = new Segment());
/*  170 */     this._appendAt = 0;
/*  171 */     this._hasNativeTypeIds = jp.canReadTypeId();
/*  172 */     this._hasNativeObjectIds = jp.canReadObjectId();
/*  173 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*      */   }
/*      */   
/*      */   public Version version()
/*      */   {
/*  178 */     return PackageVersion.VERSION;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser asParser()
/*      */   {
/*  193 */     return asParser(this._objectCodec);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser asParser(ObjectCodec codec)
/*      */   {
/*  211 */     return new Parser(this._first, codec, this._hasNativeTypeIds, this._hasNativeObjectIds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser asParser(JsonParser src)
/*      */   {
/*  220 */     Parser p = new Parser(this._first, src.getCodec(), this._hasNativeTypeIds, this._hasNativeObjectIds);
/*  221 */     p.setLocation(src.getTokenLocation());
/*  222 */     return p;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonToken firstToken()
/*      */   {
/*  232 */     if (this._first != null) {
/*  233 */       return this._first.type(0);
/*      */     }
/*  235 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer append(TokenBuffer other)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  256 */     if (!this._hasNativeTypeIds) {
/*  257 */       this._hasNativeTypeIds = other.canWriteTypeId();
/*      */     }
/*  259 */     if (!this._hasNativeObjectIds) {
/*  260 */       this._hasNativeObjectIds = other.canWriteObjectId();
/*      */     }
/*  262 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*      */     
/*  264 */     JsonParser jp = other.asParser();
/*  265 */     while (jp.nextToken() != null) {
/*  266 */       copyCurrentStructure(jp);
/*      */     }
/*  268 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serialize(JsonGenerator jgen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  284 */     Segment segment = this._first;
/*  285 */     int ptr = -1;
/*      */     
/*  287 */     boolean checkIds = this._mayHaveNativeIds;
/*  288 */     boolean hasIds = (checkIds) && (segment.hasIds());
/*      */     for (;;)
/*      */     {
/*  291 */       ptr++; if (ptr >= 16) {
/*  292 */         ptr = 0;
/*  293 */         segment = segment.next();
/*  294 */         if (segment == null) break;
/*  295 */         hasIds = (checkIds) && (segment.hasIds());
/*      */       }
/*  297 */       JsonToken t = segment.type(ptr);
/*  298 */       if (t == null)
/*      */         break;
/*  300 */       if (hasIds) {
/*  301 */         Object id = segment.findObjectId(ptr);
/*  302 */         if (id != null) {
/*  303 */           jgen.writeObjectId(id);
/*      */         }
/*  305 */         id = segment.findTypeId(ptr);
/*  306 */         if (id != null) {
/*  307 */           jgen.writeTypeId(id);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  312 */       switch (t) {
/*      */       case START_OBJECT: 
/*  314 */         jgen.writeStartObject();
/*  315 */         break;
/*      */       case END_OBJECT: 
/*  317 */         jgen.writeEndObject();
/*  318 */         break;
/*      */       case START_ARRAY: 
/*  320 */         jgen.writeStartArray();
/*  321 */         break;
/*      */       case END_ARRAY: 
/*  323 */         jgen.writeEndArray();
/*  324 */         break;
/*      */       
/*      */ 
/*      */       case FIELD_NAME: 
/*  328 */         Object ob = segment.get(ptr);
/*  329 */         if ((ob instanceof SerializableString)) {
/*  330 */           jgen.writeFieldName((SerializableString)ob);
/*      */         } else {
/*  332 */           jgen.writeFieldName((String)ob);
/*      */         }
/*      */         
/*  335 */         break;
/*      */       
/*      */       case VALUE_STRING: 
/*  338 */         Object ob = segment.get(ptr);
/*  339 */         if ((ob instanceof SerializableString)) {
/*  340 */           jgen.writeString((SerializableString)ob);
/*      */         } else {
/*  342 */           jgen.writeString((String)ob);
/*      */         }
/*      */         
/*  345 */         break;
/*      */       
/*      */       case VALUE_NUMBER_INT: 
/*  348 */         Object n = segment.get(ptr);
/*  349 */         if ((n instanceof Integer)) {
/*  350 */           jgen.writeNumber(((Integer)n).intValue());
/*  351 */         } else if ((n instanceof BigInteger)) {
/*  352 */           jgen.writeNumber((BigInteger)n);
/*  353 */         } else if ((n instanceof Long)) {
/*  354 */           jgen.writeNumber(((Long)n).longValue());
/*  355 */         } else if ((n instanceof Short)) {
/*  356 */           jgen.writeNumber(((Short)n).shortValue());
/*      */         } else {
/*  358 */           jgen.writeNumber(((Number)n).intValue());
/*      */         }
/*      */         
/*  361 */         break;
/*      */       
/*      */       case VALUE_NUMBER_FLOAT: 
/*  364 */         Object n = segment.get(ptr);
/*  365 */         if ((n instanceof Double)) {
/*  366 */           jgen.writeNumber(((Double)n).doubleValue());
/*  367 */         } else if ((n instanceof BigDecimal)) {
/*  368 */           jgen.writeNumber((BigDecimal)n);
/*  369 */         } else if ((n instanceof Float)) {
/*  370 */           jgen.writeNumber(((Float)n).floatValue());
/*  371 */         } else if (n == null) {
/*  372 */           jgen.writeNull();
/*  373 */         } else if ((n instanceof String)) {
/*  374 */           jgen.writeNumber((String)n);
/*      */         } else {
/*  376 */           throw new JsonGenerationException("Unrecognized value type for VALUE_NUMBER_FLOAT: " + n.getClass().getName() + ", can not serialize");
/*      */         }
/*      */         
/*  379 */         break;
/*      */       case VALUE_TRUE: 
/*  381 */         jgen.writeBoolean(true);
/*  382 */         break;
/*      */       case VALUE_FALSE: 
/*  384 */         jgen.writeBoolean(false);
/*  385 */         break;
/*      */       case VALUE_NULL: 
/*  387 */         jgen.writeNull();
/*  388 */         break;
/*      */       case VALUE_EMBEDDED_OBJECT: 
/*  390 */         jgen.writeObject(segment.get(ptr));
/*  391 */         break;
/*      */       default: 
/*  393 */         throw new RuntimeException("Internal error: should never end up through this code path");
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  405 */     if (jp.getCurrentTokenId() != JsonToken.FIELD_NAME.id()) {
/*  406 */       copyCurrentStructure(jp);
/*  407 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  414 */     writeStartObject();
/*      */     JsonToken t;
/*  416 */     do { copyCurrentStructure(jp);
/*  417 */     } while ((t = jp.nextToken()) == JsonToken.FIELD_NAME);
/*  418 */     if (t != JsonToken.END_OBJECT) {
/*  419 */       throw ctxt.mappingException("Expected END_OBJECT after copying contents of a JsonParser into TokenBuffer, got " + t);
/*      */     }
/*  421 */     writeEndObject();
/*  422 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  430 */     int MAX_COUNT = 100;
/*      */     
/*  432 */     StringBuilder sb = new StringBuilder();
/*  433 */     sb.append("[TokenBuffer: ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */     JsonParser jp = asParser();
/*  441 */     int count = 0;
/*  442 */     boolean hasNativeIds = (this._hasNativeTypeIds) || (this._hasNativeObjectIds);
/*      */     for (;;)
/*      */     {
/*      */       try
/*      */       {
/*  447 */         JsonToken t = jp.nextToken();
/*  448 */         if (t == null)
/*      */           break;
/*  450 */         if (hasNativeIds) {
/*  451 */           _appendNativeIds(sb);
/*      */         }
/*      */         
/*  454 */         if (count < 100) {
/*  455 */           if (count > 0) {
/*  456 */             sb.append(", ");
/*      */           }
/*  458 */           sb.append(t.toString());
/*  459 */           if (t == JsonToken.FIELD_NAME) {
/*  460 */             sb.append('(');
/*  461 */             sb.append(jp.getCurrentName());
/*  462 */             sb.append(')');
/*      */           }
/*      */         }
/*      */       } catch (IOException ioe) {
/*  466 */         throw new IllegalStateException(ioe);
/*      */       }
/*  468 */       count++;
/*      */     }
/*      */     
/*  471 */     if (count >= 100) {
/*  472 */       sb.append(" ... (truncated ").append(count - 100).append(" entries)");
/*      */     }
/*  474 */     sb.append(']');
/*  475 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private final void _appendNativeIds(StringBuilder sb)
/*      */   {
/*  480 */     Object objectId = this._last.findObjectId(this._appendAt - 1);
/*  481 */     if (objectId != null) {
/*  482 */       sb.append("[objectId=").append(String.valueOf(objectId)).append(']');
/*      */     }
/*  484 */     Object typeId = this._last.findTypeId(this._appendAt - 1);
/*  485 */     if (typeId != null) {
/*  486 */       sb.append("[typeId=").append(String.valueOf(typeId)).append(']');
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator enable(JsonGenerator.Feature f)
/*      */   {
/*  498 */     this._generatorFeatures |= f.getMask();
/*  499 */     return this;
/*      */   }
/*      */   
/*      */   public JsonGenerator disable(JsonGenerator.Feature f)
/*      */   {
/*  504 */     this._generatorFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  505 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/*  512 */     return (this._generatorFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */   public int getFeatureMask()
/*      */   {
/*  517 */     return this._generatorFeatures;
/*      */   }
/*      */   
/*      */   public JsonGenerator setFeatureMask(int mask)
/*      */   {
/*  522 */     this._generatorFeatures = mask;
/*  523 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public JsonGenerator useDefaultPrettyPrinter()
/*      */   {
/*  529 */     return this;
/*      */   }
/*      */   
/*      */   public JsonGenerator setCodec(ObjectCodec oc)
/*      */   {
/*  534 */     this._objectCodec = oc;
/*  535 */     return this;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec() {
/*  539 */     return this._objectCodec;
/*      */   }
/*      */   
/*  542 */   public final JsonWriteContext getOutputContext() { return this._writeContext; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canWriteBinaryNatively()
/*      */   {
/*  555 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void flush()
/*      */     throws IOException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  569 */     this._closed = true;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*  573 */     return this._closed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  585 */     _append(JsonToken.START_ARRAY);
/*  586 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */   
/*      */ 
/*      */   public final void writeEndArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  593 */     _append(JsonToken.END_ARRAY);
/*      */     
/*  595 */     JsonWriteContext c = this._writeContext.getParent();
/*  596 */     if (c != null) {
/*  597 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public final void writeStartObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  605 */     _append(JsonToken.START_OBJECT);
/*  606 */     this._writeContext = this._writeContext.createChildObjectContext();
/*      */   }
/*      */   
/*      */ 
/*      */   public final void writeEndObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  613 */     _append(JsonToken.END_OBJECT);
/*      */     
/*  615 */     JsonWriteContext c = this._writeContext.getParent();
/*  616 */     if (c != null) {
/*  617 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public final void writeFieldName(String name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  625 */     _append(JsonToken.FIELD_NAME, name);
/*  626 */     this._writeContext.writeFieldName(name);
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeFieldName(SerializableString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  633 */     _append(JsonToken.FIELD_NAME, name);
/*  634 */     this._writeContext.writeFieldName(name.getValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  645 */     if (text == null) {
/*  646 */       writeNull();
/*      */     } else {
/*  648 */       _append(JsonToken.VALUE_STRING, text);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*      */   {
/*  654 */     writeString(new String(text, offset, len));
/*      */   }
/*      */   
/*      */   public void writeString(SerializableString text) throws IOException, JsonGenerationException
/*      */   {
/*  659 */     if (text == null) {
/*  660 */       writeNull();
/*      */     } else {
/*  662 */       _append(JsonToken.VALUE_STRING, text);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  671 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  679 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(String text) throws IOException, JsonGenerationException
/*      */   {
/*  684 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException
/*      */   {
/*  689 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(SerializableString text) throws IOException, JsonGenerationException
/*      */   {
/*  694 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*      */   {
/*  699 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(char c) throws IOException, JsonGenerationException
/*      */   {
/*  704 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRawValue(String text) throws IOException, JsonGenerationException
/*      */   {
/*  709 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException
/*      */   {
/*  714 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*      */   {
/*  719 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeNumber(short i)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  730 */     _append(JsonToken.VALUE_NUMBER_INT, Short.valueOf(i));
/*      */   }
/*      */   
/*      */   public void writeNumber(int i) throws IOException, JsonGenerationException
/*      */   {
/*  735 */     _append(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
/*      */   }
/*      */   
/*      */   public void writeNumber(long l) throws IOException, JsonGenerationException
/*      */   {
/*  740 */     _append(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
/*      */   }
/*      */   
/*      */   public void writeNumber(double d) throws IOException, JsonGenerationException
/*      */   {
/*  745 */     _append(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
/*      */   }
/*      */   
/*      */   public void writeNumber(float f) throws IOException, JsonGenerationException
/*      */   {
/*  750 */     _append(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
/*      */   }
/*      */   
/*      */   public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException
/*      */   {
/*  755 */     if (dec == null) {
/*  756 */       writeNull();
/*      */     } else {
/*  758 */       _append(JsonToken.VALUE_NUMBER_FLOAT, dec);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeNumber(BigInteger v) throws IOException, JsonGenerationException
/*      */   {
/*  764 */     if (v == null) {
/*  765 */       writeNull();
/*      */     } else {
/*  767 */       _append(JsonToken.VALUE_NUMBER_INT, v);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  776 */     _append(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
/*      */   }
/*      */   
/*      */   public void writeBoolean(boolean state) throws IOException, JsonGenerationException
/*      */   {
/*  781 */     _append(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
/*      */   }
/*      */   
/*      */   public void writeNull() throws IOException, JsonGenerationException
/*      */   {
/*  786 */     _append(JsonToken.VALUE_NULL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeObject(Object value)
/*      */     throws IOException
/*      */   {
/*  798 */     if (value == null) {
/*  799 */       writeNull();
/*  800 */       return;
/*      */     }
/*  802 */     Class<?> raw = value.getClass();
/*  803 */     if (raw == byte[].class) {
/*  804 */       _append(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*  805 */       return; }
/*  806 */     if (this._objectCodec == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  811 */       _append(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */     } else {
/*  813 */       this._objectCodec.writeValue(this, value);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeTree(TreeNode node)
/*      */     throws IOException
/*      */   {
/*  820 */     if (node == null) {
/*  821 */       writeNull();
/*  822 */       return;
/*      */     }
/*      */     
/*  825 */     if (this._objectCodec == null)
/*      */     {
/*  827 */       _append(JsonToken.VALUE_EMBEDDED_OBJECT, node);
/*      */     } else {
/*  829 */       this._objectCodec.writeTree(this, node);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  849 */     byte[] copy = new byte[len];
/*  850 */     System.arraycopy(data, offset, copy, 0, len);
/*  851 */     writeObject(copy);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*      */   {
/*  862 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canWriteTypeId()
/*      */   {
/*  873 */     return this._hasNativeTypeIds;
/*      */   }
/*      */   
/*      */   public boolean canWriteObjectId()
/*      */   {
/*  878 */     return this._hasNativeObjectIds;
/*      */   }
/*      */   
/*      */   public void writeTypeId(Object id)
/*      */   {
/*  883 */     this._typeId = id;
/*  884 */     this._hasNativeId = true;
/*      */   }
/*      */   
/*      */   public void writeObjectId(Object id)
/*      */   {
/*  889 */     this._objectId = id;
/*  890 */     this._hasNativeId = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyCurrentEvent(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  902 */     if (this._mayHaveNativeIds) {
/*  903 */       _checkNativeIds(jp);
/*      */     }
/*  905 */     switch (jp.getCurrentToken()) {
/*      */     case START_OBJECT: 
/*  907 */       writeStartObject();
/*  908 */       break;
/*      */     case END_OBJECT: 
/*  910 */       writeEndObject();
/*  911 */       break;
/*      */     case START_ARRAY: 
/*  913 */       writeStartArray();
/*  914 */       break;
/*      */     case END_ARRAY: 
/*  916 */       writeEndArray();
/*  917 */       break;
/*      */     case FIELD_NAME: 
/*  919 */       writeFieldName(jp.getCurrentName());
/*  920 */       break;
/*      */     case VALUE_STRING: 
/*  922 */       if (jp.hasTextCharacters()) {
/*  923 */         writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
/*      */       } else {
/*  925 */         writeString(jp.getText());
/*      */       }
/*  927 */       break;
/*      */     case VALUE_NUMBER_INT: 
/*  929 */       switch (jp.getNumberType()) {
/*      */       case INT: 
/*  931 */         writeNumber(jp.getIntValue());
/*  932 */         break;
/*      */       case BIG_INTEGER: 
/*  934 */         writeNumber(jp.getBigIntegerValue());
/*  935 */         break;
/*      */       default: 
/*  937 */         writeNumber(jp.getLongValue());
/*      */       }
/*  939 */       break;
/*      */     case VALUE_NUMBER_FLOAT: 
/*  941 */       switch (jp.getNumberType()) {
/*      */       case BIG_DECIMAL: 
/*  943 */         writeNumber(jp.getDecimalValue());
/*  944 */         break;
/*      */       case FLOAT: 
/*  946 */         writeNumber(jp.getFloatValue());
/*  947 */         break;
/*      */       default: 
/*  949 */         writeNumber(jp.getDoubleValue());
/*      */       }
/*  951 */       break;
/*      */     case VALUE_TRUE: 
/*  953 */       writeBoolean(true);
/*  954 */       break;
/*      */     case VALUE_FALSE: 
/*  956 */       writeBoolean(false);
/*  957 */       break;
/*      */     case VALUE_NULL: 
/*  959 */       writeNull();
/*  960 */       break;
/*      */     case VALUE_EMBEDDED_OBJECT: 
/*  962 */       writeObject(jp.getEmbeddedObject());
/*  963 */       break;
/*      */     default: 
/*  965 */       throw new RuntimeException("Internal error: should never end up through this code path");
/*      */     }
/*      */   }
/*      */   
/*      */   public void copyCurrentStructure(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  972 */     JsonToken t = jp.getCurrentToken();
/*      */     
/*      */ 
/*  975 */     if (t == JsonToken.FIELD_NAME) {
/*  976 */       if (this._mayHaveNativeIds) {
/*  977 */         _checkNativeIds(jp);
/*      */       }
/*  979 */       writeFieldName(jp.getCurrentName());
/*  980 */       t = jp.nextToken();
/*      */     }
/*      */     
/*      */ 
/*  984 */     if (this._mayHaveNativeIds) {
/*  985 */       _checkNativeIds(jp);
/*      */     }
/*      */     
/*  988 */     switch (t) {
/*      */     case START_ARRAY: 
/*  990 */       writeStartArray();
/*  991 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/*  992 */         copyCurrentStructure(jp);
/*      */       }
/*  994 */       writeEndArray();
/*  995 */       break;
/*      */     case START_OBJECT: 
/*  997 */       writeStartObject();
/*  998 */       while (jp.nextToken() != JsonToken.END_OBJECT) {
/*  999 */         copyCurrentStructure(jp);
/*      */       }
/* 1001 */       writeEndObject();
/* 1002 */       break;
/*      */     default: 
/* 1004 */       copyCurrentEvent(jp);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _checkNativeIds(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1011 */     if ((this._typeId = jp.getTypeId()) != null) {
/* 1012 */       this._hasNativeId = true;
/*      */     }
/* 1014 */     if ((this._objectId = jp.getObjectId()) != null) {
/* 1015 */       this._hasNativeId = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _append(JsonToken type)
/*      */   {
/* 1027 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, this._objectId, this._typeId) : this._last.append(this._appendAt, type);
/*      */     
/*      */ 
/* 1030 */     if (next == null) {
/* 1031 */       this._appendAt += 1;
/*      */     } else {
/* 1033 */       this._last = next;
/* 1034 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void _append(JsonToken type, Object value)
/*      */   {
/* 1040 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, value, this._objectId, this._typeId) : this._last.append(this._appendAt, type, value);
/*      */     
/*      */ 
/* 1043 */     if (next == null) {
/* 1044 */       this._appendAt += 1;
/*      */     } else {
/* 1046 */       this._last = next;
/* 1047 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void _appendRaw(int rawType, Object value)
/*      */   {
/* 1053 */     Segment next = this._hasNativeId ? this._last.appendRaw(this._appendAt, rawType, value, this._objectId, this._typeId) : this._last.appendRaw(this._appendAt, rawType, value);
/*      */     
/*      */ 
/* 1056 */     if (next == null) {
/* 1057 */       this._appendAt += 1;
/*      */     } else {
/* 1059 */       this._last = next;
/* 1060 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _reportUnsupportedOperation()
/*      */   {
/* 1066 */     throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final class Parser
/*      */     extends ParserMinimalBase
/*      */   {
/*      */     protected ObjectCodec _codec;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeTypeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeObjectIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected TokenBuffer.Segment _segment;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int _segmentPtr;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected JsonReadContext _parsingContext;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean _closed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected transient ByteArrayBuilder _byteBuilder;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1124 */     protected JsonLocation _location = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds)
/*      */     {
/* 1136 */       super();
/* 1137 */       this._segment = firstSeg;
/* 1138 */       this._segmentPtr = -1;
/* 1139 */       this._codec = codec;
/* 1140 */       this._parsingContext = JsonReadContext.createRootContext(null);
/* 1141 */       this._hasNativeTypeIds = hasNativeTypeIds;
/* 1142 */       this._hasNativeObjectIds = hasNativeObjectIds;
/* 1143 */       this._hasNativeIds = (hasNativeTypeIds | hasNativeObjectIds);
/*      */     }
/*      */     
/*      */     public void setLocation(JsonLocation l) {
/* 1147 */       this._location = l;
/*      */     }
/*      */     
/*      */     public ObjectCodec getCodec() {
/* 1151 */       return this._codec;
/*      */     }
/*      */     
/* 1154 */     public void setCodec(ObjectCodec c) { this._codec = c; }
/*      */     
/*      */     public Version version()
/*      */     {
/* 1158 */       return PackageVersion.VERSION;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken peekNextToken()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1171 */       if (this._closed) return null;
/* 1172 */       TokenBuffer.Segment seg = this._segment;
/* 1173 */       int ptr = this._segmentPtr + 1;
/* 1174 */       if (ptr >= 16) {
/* 1175 */         ptr = 0;
/* 1176 */         seg = seg == null ? null : seg.next();
/*      */       }
/* 1178 */       return seg == null ? null : seg.type(ptr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/* 1189 */       if (!this._closed) {
/* 1190 */         this._closed = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken nextToken()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1204 */       if ((this._closed) || (this._segment == null)) { return null;
/*      */       }
/*      */       
/* 1207 */       if (++this._segmentPtr >= 16) {
/* 1208 */         this._segmentPtr = 0;
/* 1209 */         this._segment = this._segment.next();
/* 1210 */         if (this._segment == null) {
/* 1211 */           return null;
/*      */         }
/*      */       }
/* 1214 */       this._currToken = this._segment.type(this._segmentPtr);
/*      */       
/* 1216 */       if (this._currToken == JsonToken.FIELD_NAME) {
/* 1217 */         Object ob = _currentObject();
/* 1218 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1219 */         this._parsingContext.setCurrentName(name);
/* 1220 */       } else if (this._currToken == JsonToken.START_OBJECT) {
/* 1221 */         this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
/* 1222 */       } else if (this._currToken == JsonToken.START_ARRAY) {
/* 1223 */         this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
/* 1224 */       } else if ((this._currToken == JsonToken.END_OBJECT) || (this._currToken == JsonToken.END_ARRAY))
/*      */       {
/*      */ 
/* 1227 */         this._parsingContext = this._parsingContext.getParent();
/*      */         
/* 1229 */         if (this._parsingContext == null) {
/* 1230 */           this._parsingContext = JsonReadContext.createRootContext(null);
/*      */         }
/*      */       }
/* 1233 */       return this._currToken;
/*      */     }
/*      */     
/*      */     public boolean isClosed() {
/* 1237 */       return this._closed;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonStreamContext getParsingContext()
/*      */     {
/* 1246 */       return this._parsingContext;
/*      */     }
/*      */     
/* 1249 */     public JsonLocation getTokenLocation() { return getCurrentLocation(); }
/*      */     
/*      */     public JsonLocation getCurrentLocation()
/*      */     {
/* 1253 */       return this._location == null ? JsonLocation.NA : this._location;
/*      */     }
/*      */     
/*      */     public String getCurrentName() {
/* 1257 */       return this._parsingContext.getCurrentName();
/*      */     }
/*      */     
/*      */ 
/*      */     public void overrideCurrentName(String name)
/*      */     {
/* 1263 */       JsonReadContext ctxt = this._parsingContext;
/* 1264 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 1265 */         ctxt = ctxt.getParent();
/*      */       }
/*      */       try {
/* 1268 */         ctxt.setCurrentName(name);
/*      */       } catch (IOException e) {
/* 1270 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getText()
/*      */     {
/* 1284 */       if ((this._currToken == JsonToken.VALUE_STRING) || (this._currToken == JsonToken.FIELD_NAME))
/*      */       {
/* 1286 */         Object ob = _currentObject();
/* 1287 */         if ((ob instanceof String)) {
/* 1288 */           return (String)ob;
/*      */         }
/* 1290 */         return ob == null ? null : ob.toString();
/*      */       }
/* 1292 */       if (this._currToken == null) {
/* 1293 */         return null;
/*      */       }
/* 1295 */       switch (TokenBuffer.1.$SwitchMap$com$fasterxml$jackson$core$JsonToken[this._currToken.ordinal()]) {
/*      */       case 7: 
/*      */       case 8: 
/* 1298 */         Object ob = _currentObject();
/* 1299 */         return ob == null ? null : ob.toString();
/*      */       }
/* 1301 */       return this._currToken.asString();
/*      */     }
/*      */     
/*      */ 
/*      */     public char[] getTextCharacters()
/*      */     {
/* 1307 */       String str = getText();
/* 1308 */       return str == null ? null : str.toCharArray();
/*      */     }
/*      */     
/*      */     public int getTextLength()
/*      */     {
/* 1313 */       String str = getText();
/* 1314 */       return str == null ? 0 : str.length();
/*      */     }
/*      */     
/*      */     public int getTextOffset() {
/* 1318 */       return 0;
/*      */     }
/*      */     
/*      */     public boolean hasTextCharacters()
/*      */     {
/* 1323 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public BigInteger getBigIntegerValue()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1335 */       Number n = getNumberValue();
/* 1336 */       if ((n instanceof BigInteger)) {
/* 1337 */         return (BigInteger)n;
/*      */       }
/* 1339 */       if (getNumberType() == JsonParser.NumberType.BIG_DECIMAL) {
/* 1340 */         return ((BigDecimal)n).toBigInteger();
/*      */       }
/*      */       
/* 1343 */       return BigInteger.valueOf(n.longValue());
/*      */     }
/*      */     
/*      */     public BigDecimal getDecimalValue()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1349 */       Number n = getNumberValue();
/* 1350 */       if ((n instanceof BigDecimal)) {
/* 1351 */         return (BigDecimal)n;
/*      */       }
/* 1353 */       switch (TokenBuffer.1.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[getNumberType().ordinal()]) {
/*      */       case 1: 
/*      */       case 5: 
/* 1356 */         return BigDecimal.valueOf(n.longValue());
/*      */       case 2: 
/* 1358 */         return new BigDecimal((BigInteger)n);
/*      */       }
/*      */       
/*      */       
/* 1362 */       return BigDecimal.valueOf(n.doubleValue());
/*      */     }
/*      */     
/*      */     public double getDoubleValue() throws IOException, JsonParseException
/*      */     {
/* 1367 */       return getNumberValue().doubleValue();
/*      */     }
/*      */     
/*      */     public float getFloatValue() throws IOException, JsonParseException
/*      */     {
/* 1372 */       return getNumberValue().floatValue();
/*      */     }
/*      */     
/*      */ 
/*      */     public int getIntValue()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1379 */       if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/* 1380 */         return ((Number)_currentObject()).intValue();
/*      */       }
/* 1382 */       return getNumberValue().intValue();
/*      */     }
/*      */     
/*      */     public long getLongValue() throws IOException, JsonParseException
/*      */     {
/* 1387 */       return getNumberValue().longValue();
/*      */     }
/*      */     
/*      */     public JsonParser.NumberType getNumberType()
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1393 */       Number n = getNumberValue();
/* 1394 */       if ((n instanceof Integer)) return JsonParser.NumberType.INT;
/* 1395 */       if ((n instanceof Long)) return JsonParser.NumberType.LONG;
/* 1396 */       if ((n instanceof Double)) return JsonParser.NumberType.DOUBLE;
/* 1397 */       if ((n instanceof BigDecimal)) return JsonParser.NumberType.BIG_DECIMAL;
/* 1398 */       if ((n instanceof BigInteger)) return JsonParser.NumberType.BIG_INTEGER;
/* 1399 */       if ((n instanceof Float)) return JsonParser.NumberType.FLOAT;
/* 1400 */       if ((n instanceof Short)) return JsonParser.NumberType.INT;
/* 1401 */       return null;
/*      */     }
/*      */     
/*      */     public final Number getNumberValue() throws IOException, JsonParseException
/*      */     {
/* 1406 */       _checkIsNumber();
/* 1407 */       Object value = _currentObject();
/* 1408 */       if ((value instanceof Number)) {
/* 1409 */         return (Number)value;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1414 */       if ((value instanceof String)) {
/* 1415 */         String str = (String)value;
/* 1416 */         if (str.indexOf('.') >= 0) {
/* 1417 */           return Double.valueOf(Double.parseDouble(str));
/*      */         }
/* 1419 */         return Long.valueOf(Long.parseLong(str));
/*      */       }
/* 1421 */       if (value == null) {
/* 1422 */         return null;
/*      */       }
/* 1424 */       throw new IllegalStateException("Internal error: entry should be a Number, but is of type " + value.getClass().getName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getEmbeddedObject()
/*      */     {
/* 1437 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 1438 */         return _currentObject();
/*      */       }
/* 1440 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public byte[] getBinaryValue(Base64Variant b64variant)
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1448 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT)
/*      */       {
/* 1450 */         Object ob = _currentObject();
/* 1451 */         if ((ob instanceof byte[])) {
/* 1452 */           return (byte[])ob;
/*      */         }
/*      */       }
/*      */       
/* 1456 */       if (this._currToken != JsonToken.VALUE_STRING) {
/* 1457 */         throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), can not access as binary");
/*      */       }
/* 1459 */       String str = getText();
/* 1460 */       if (str == null) {
/* 1461 */         return null;
/*      */       }
/* 1463 */       ByteArrayBuilder builder = this._byteBuilder;
/* 1464 */       if (builder == null) {
/* 1465 */         this._byteBuilder = (builder = new ByteArrayBuilder(100));
/*      */       } else {
/* 1467 */         this._byteBuilder.reset();
/*      */       }
/* 1469 */       _decodeBase64(str, builder, b64variant);
/* 1470 */       return builder.toByteArray();
/*      */     }
/*      */     
/*      */ 
/*      */     public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1477 */       byte[] data = getBinaryValue(b64variant);
/* 1478 */       if (data != null) {
/* 1479 */         out.write(data, 0, data.length);
/* 1480 */         return data.length;
/*      */       }
/* 1482 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean canReadObjectId()
/*      */     {
/* 1493 */       return this._hasNativeObjectIds;
/*      */     }
/*      */     
/*      */     public boolean canReadTypeId()
/*      */     {
/* 1498 */       return this._hasNativeTypeIds;
/*      */     }
/*      */     
/*      */     public Object getTypeId()
/*      */     {
/* 1503 */       return this._segment.findTypeId(this._segmentPtr);
/*      */     }
/*      */     
/*      */     public Object getObjectId()
/*      */     {
/* 1508 */       return this._segment.findObjectId(this._segmentPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final Object _currentObject()
/*      */     {
/* 1518 */       return this._segment.get(this._segmentPtr);
/*      */     }
/*      */     
/*      */     protected final void _checkIsNumber() throws JsonParseException
/*      */     {
/* 1523 */       if ((this._currToken == null) || (!this._currToken.isNumeric())) {
/* 1524 */         throw _constructError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
/*      */       }
/*      */     }
/*      */     
/*      */     protected void _handleEOF() throws JsonParseException
/*      */     {
/* 1530 */       _throwInternal();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final class Segment
/*      */   {
/*      */     public static final int TOKENS_PER_SEGMENT = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1552 */     private static final JsonToken[] TOKEN_TYPES_BY_INDEX = new JsonToken[16];
/* 1553 */     static { JsonToken[] t = JsonToken.values();
/*      */       
/* 1555 */       System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, t.length - 1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Segment _next;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected long _tokenTypes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1573 */     protected final Object[] _tokens = new Object[16];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected TreeMap<Integer, Object> _nativeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken type(int index)
/*      */     {
/* 1586 */       long l = this._tokenTypes;
/* 1587 */       if (index > 0) {
/* 1588 */         l >>= index << 2;
/*      */       }
/* 1590 */       int ix = (int)l & 0xF;
/* 1591 */       return TOKEN_TYPES_BY_INDEX[ix];
/*      */     }
/*      */     
/*      */     public int rawType(int index)
/*      */     {
/* 1596 */       long l = this._tokenTypes;
/* 1597 */       if (index > 0) {
/* 1598 */         l >>= index << 2;
/*      */       }
/* 1600 */       int ix = (int)l & 0xF;
/* 1601 */       return ix;
/*      */     }
/*      */     
/*      */     public Object get(int index) {
/* 1605 */       return this._tokens[index];
/*      */     }
/*      */     
/* 1608 */     public Segment next() { return this._next; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasIds()
/*      */     {
/* 1615 */       return this._nativeIds != null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType)
/*      */     {
/* 1622 */       if (index < 16) {
/* 1623 */         set(index, tokenType);
/* 1624 */         return null;
/*      */       }
/* 1626 */       this._next = new Segment();
/* 1627 */       this._next.set(0, tokenType);
/* 1628 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType, Object objectId, Object typeId)
/*      */     {
/* 1634 */       if (index < 16) {
/* 1635 */         set(index, tokenType, objectId, typeId);
/* 1636 */         return null;
/*      */       }
/* 1638 */       this._next = new Segment();
/* 1639 */       this._next.set(0, tokenType, objectId, typeId);
/* 1640 */       return this._next;
/*      */     }
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value)
/*      */     {
/* 1645 */       if (index < 16) {
/* 1646 */         set(index, tokenType, value);
/* 1647 */         return null;
/*      */       }
/* 1649 */       this._next = new Segment();
/* 1650 */       this._next.set(0, tokenType, value);
/* 1651 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1657 */       if (index < 16) {
/* 1658 */         set(index, tokenType, value, objectId, typeId);
/* 1659 */         return null;
/*      */       }
/* 1661 */       this._next = new Segment();
/* 1662 */       this._next.set(0, tokenType, value, objectId, typeId);
/* 1663 */       return this._next;
/*      */     }
/*      */     
/*      */     public Segment appendRaw(int index, int rawTokenType, Object value)
/*      */     {
/* 1668 */       if (index < 16) {
/* 1669 */         set(index, rawTokenType, value);
/* 1670 */         return null;
/*      */       }
/* 1672 */       this._next = new Segment();
/* 1673 */       this._next.set(0, rawTokenType, value);
/* 1674 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment appendRaw(int index, int rawTokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1680 */       if (index < 16) {
/* 1681 */         set(index, rawTokenType, value, objectId, typeId);
/* 1682 */         return null;
/*      */       }
/* 1684 */       this._next = new Segment();
/* 1685 */       this._next.set(0, rawTokenType, value, objectId, typeId);
/* 1686 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void set(int index, JsonToken tokenType)
/*      */     {
/* 1694 */       long typeCode = tokenType.ordinal();
/* 1695 */       if (index > 0) {
/* 1696 */         typeCode <<= index << 2;
/*      */       }
/* 1698 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */ 
/*      */     private void set(int index, JsonToken tokenType, Object objectId, Object typeId)
/*      */     {
/* 1704 */       long typeCode = tokenType.ordinal();
/* 1705 */       if (index > 0) {
/* 1706 */         typeCode <<= index << 2;
/*      */       }
/* 1708 */       this._tokenTypes |= typeCode;
/* 1709 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value)
/*      */     {
/* 1714 */       this._tokens[index] = value;
/* 1715 */       long typeCode = tokenType.ordinal();
/* 1716 */       if (index > 0) {
/* 1717 */         typeCode <<= index << 2;
/*      */       }
/* 1719 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */ 
/*      */     private void set(int index, JsonToken tokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1725 */       this._tokens[index] = value;
/* 1726 */       long typeCode = tokenType.ordinal();
/* 1727 */       if (index > 0) {
/* 1728 */         typeCode <<= index << 2;
/*      */       }
/* 1730 */       this._tokenTypes |= typeCode;
/* 1731 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private void set(int index, int rawTokenType, Object value)
/*      */     {
/* 1736 */       this._tokens[index] = value;
/* 1737 */       long typeCode = rawTokenType;
/* 1738 */       if (index > 0) {
/* 1739 */         typeCode <<= index << 2;
/*      */       }
/* 1741 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */     private void set(int index, int rawTokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1746 */       this._tokens[index] = value;
/* 1747 */       long typeCode = rawTokenType;
/* 1748 */       if (index > 0) {
/* 1749 */         typeCode <<= index << 2;
/*      */       }
/* 1751 */       this._tokenTypes |= typeCode;
/* 1752 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private final void assignNativeIds(int index, Object objectId, Object typeId)
/*      */     {
/* 1757 */       if (this._nativeIds == null) {
/* 1758 */         this._nativeIds = new TreeMap();
/*      */       }
/* 1760 */       if (objectId != null) {
/* 1761 */         this._nativeIds.put(Integer.valueOf(_objectIdIndex(index)), objectId);
/*      */       }
/* 1763 */       if (typeId != null) {
/* 1764 */         this._nativeIds.put(Integer.valueOf(_typeIdIndex(index)), typeId);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Object findObjectId(int index)
/*      */     {
/* 1772 */       return this._nativeIds == null ? null : this._nativeIds.get(Integer.valueOf(_objectIdIndex(index)));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Object findTypeId(int index)
/*      */     {
/* 1779 */       return this._nativeIds == null ? null : this._nativeIds.get(Integer.valueOf(_typeIdIndex(index)));
/*      */     }
/*      */     
/* 1782 */     private final int _typeIdIndex(int i) { return i + i; }
/* 1783 */     private final int _objectIdIndex(int i) { return i + i + 1; }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\TokenBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */