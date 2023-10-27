/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class JsonParserDelegate extends JsonParser
/*     */ {
/*     */   protected JsonParser delegate;
/*     */   
/*     */   public JsonParserDelegate(JsonParser d)
/*     */   {
/*  25 */     this.delegate = d;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  30 */     return this.delegate.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  35 */     this.delegate.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public void setCodec(ObjectCodec c) { this.delegate.setCodec(c); }
/*  45 */   public ObjectCodec getCodec() { return this.delegate.getCodec(); }
/*     */   
/*     */   public JsonParser enable(JsonParser.Feature f)
/*     */   {
/*  49 */     this.delegate.enable(f);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public JsonParser disable(JsonParser.Feature f)
/*     */   {
/*  55 */     this.delegate.disable(f);
/*  56 */     return this;
/*     */   }
/*     */   
/*  59 */   public boolean isEnabled(JsonParser.Feature f) { return this.delegate.isEnabled(f); }
/*  60 */   public int getFeatureMask() { return this.delegate.getFeatureMask(); }
/*     */   
/*     */   public JsonParser setFeatureMask(int mask)
/*     */   {
/*  64 */     this.delegate.setFeatureMask(mask);
/*  65 */     return this;
/*     */   }
/*     */   
/*  68 */   public FormatSchema getSchema() { return this.delegate.getSchema(); }
/*  69 */   public void setSchema(FormatSchema schema) { this.delegate.setSchema(schema); }
/*  70 */   public boolean canUseSchema(FormatSchema schema) { return this.delegate.canUseSchema(schema); }
/*  71 */   public Version version() { return this.delegate.version(); }
/*  72 */   public Object getInputSource() { return this.delegate.getInputSource(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean requiresCustomCodec()
/*     */   {
/*  80 */     return this.delegate.requiresCustomCodec();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   public void close()
/*  88 */     throws IOException { this.delegate.close(); }
/*  89 */   public boolean isClosed() { return this.delegate.isClosed(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   public JsonToken getCurrentToken() { return this.delegate.getCurrentToken(); }
/*  98 */   public int getCurrentTokenId() { return this.delegate.getCurrentTokenId(); }
/*  99 */   public boolean hasCurrentToken() { return this.delegate.hasCurrentToken(); }
/* 100 */   public boolean hasTokenId(int id) { return this.delegate.hasTokenId(id); }
/*     */   
/* 102 */   public String getCurrentName() throws IOException, JsonParseException { return this.delegate.getCurrentName(); }
/* 103 */   public JsonLocation getCurrentLocation() { return this.delegate.getCurrentLocation(); }
/* 104 */   public JsonStreamContext getParsingContext() { return this.delegate.getParsingContext(); }
/* 105 */   public boolean isExpectedStartArrayToken() { return this.delegate.isExpectedStartArrayToken(); }
/* 106 */   public boolean isExpectedStartObjectToken() { return this.delegate.isExpectedStartObjectToken(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   public void clearCurrentToken() { this.delegate.clearCurrentToken(); }
/* 115 */   public JsonToken getLastClearedToken() { return this.delegate.getLastClearedToken(); }
/* 116 */   public void overrideCurrentName(String name) { this.delegate.overrideCurrentName(name); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   public String getText()
/* 124 */     throws IOException, JsonParseException { return this.delegate.getText(); }
/* 125 */   public boolean hasTextCharacters() { return this.delegate.hasTextCharacters(); }
/* 126 */   public char[] getTextCharacters() throws IOException, JsonParseException { return this.delegate.getTextCharacters(); }
/* 127 */   public int getTextLength() throws IOException, JsonParseException { return this.delegate.getTextLength(); }
/* 128 */   public int getTextOffset() throws IOException, JsonParseException { return this.delegate.getTextOffset(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 137 */     return this.delegate.getBigIntegerValue();
/*     */   }
/*     */   
/* 140 */   public boolean getBooleanValue() throws IOException, JsonParseException { return this.delegate.getBooleanValue(); }
/*     */   
/*     */   public byte getByteValue() throws IOException, JsonParseException {
/* 143 */     return this.delegate.getByteValue();
/*     */   }
/*     */   
/* 146 */   public short getShortValue() throws IOException, JsonParseException { return this.delegate.getShortValue(); }
/*     */   
/*     */   public java.math.BigDecimal getDecimalValue() throws IOException, JsonParseException {
/* 149 */     return this.delegate.getDecimalValue();
/*     */   }
/*     */   
/* 152 */   public double getDoubleValue() throws IOException, JsonParseException { return this.delegate.getDoubleValue(); }
/*     */   
/*     */   public float getFloatValue() throws IOException, JsonParseException {
/* 155 */     return this.delegate.getFloatValue();
/*     */   }
/*     */   
/* 158 */   public int getIntValue() throws IOException, JsonParseException { return this.delegate.getIntValue(); }
/*     */   
/*     */   public long getLongValue() throws IOException, JsonParseException {
/* 161 */     return this.delegate.getLongValue();
/*     */   }
/*     */   
/* 164 */   public JsonParser.NumberType getNumberType() throws IOException, JsonParseException { return this.delegate.getNumberType(); }
/*     */   
/*     */   public Number getNumberValue() throws IOException, JsonParseException {
/* 167 */     return this.delegate.getNumberValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */   public int getValueAsInt()
/* 175 */     throws IOException, JsonParseException { return this.delegate.getValueAsInt(); }
/* 176 */   public int getValueAsInt(int defaultValue) throws IOException, JsonParseException { return this.delegate.getValueAsInt(defaultValue); }
/* 177 */   public long getValueAsLong() throws IOException, JsonParseException { return this.delegate.getValueAsLong(); }
/* 178 */   public long getValueAsLong(long defaultValue) throws IOException, JsonParseException { return this.delegate.getValueAsLong(defaultValue); }
/* 179 */   public double getValueAsDouble() throws IOException, JsonParseException { return this.delegate.getValueAsDouble(); }
/* 180 */   public double getValueAsDouble(double defaultValue) throws IOException, JsonParseException { return this.delegate.getValueAsDouble(defaultValue); }
/* 181 */   public boolean getValueAsBoolean() throws IOException, JsonParseException { return this.delegate.getValueAsBoolean(); }
/* 182 */   public boolean getValueAsBoolean(boolean defaultValue) throws IOException, JsonParseException { return this.delegate.getValueAsBoolean(defaultValue); }
/* 183 */   public String getValueAsString() throws IOException, JsonParseException { return this.delegate.getValueAsString(); }
/* 184 */   public String getValueAsString(String defaultValue) throws IOException, JsonParseException { return this.delegate.getValueAsString(defaultValue); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */   public Object getEmbeddedObject()
/* 192 */     throws IOException, JsonParseException { return this.delegate.getEmbeddedObject(); }
/* 193 */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException { return this.delegate.getBinaryValue(b64variant); }
/* 194 */   public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException, JsonParseException { return this.delegate.readBinaryValue(b64variant, out); }
/* 195 */   public JsonLocation getTokenLocation() { return this.delegate.getTokenLocation(); }
/* 196 */   public JsonToken nextToken() throws IOException, JsonParseException { return this.delegate.nextToken(); }
/* 197 */   public JsonToken nextValue() throws IOException, JsonParseException { return this.delegate.nextValue(); }
/*     */   
/*     */   public JsonParser skipChildren() throws IOException, JsonParseException
/*     */   {
/* 201 */     this.delegate.skipChildren();
/*     */     
/* 203 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */   public boolean canReadObjectId() { return this.delegate.canReadObjectId(); }
/* 213 */   public boolean canReadTypeId() { return this.delegate.canReadTypeId(); }
/* 214 */   public Object getObjectId() throws IOException, JsonGenerationException { return this.delegate.getObjectId(); }
/* 215 */   public Object getTypeId() throws IOException, JsonGenerationException { return this.delegate.getTypeId(); }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\JsonParserDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */