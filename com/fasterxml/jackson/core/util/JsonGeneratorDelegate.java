/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonGeneratorDelegate
/*     */   extends JsonGenerator
/*     */ {
/*     */   protected JsonGenerator delegate;
/*     */   protected boolean delegateCopyMethods;
/*     */   
/*     */   public JsonGeneratorDelegate(JsonGenerator d)
/*     */   {
/*  32 */     this(d, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGeneratorDelegate(JsonGenerator d, boolean delegateCopyMethods)
/*     */   {
/*  41 */     this.delegate = d;
/*  42 */     this.delegateCopyMethods = delegateCopyMethods;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  47 */     return this.delegate.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  52 */     this.delegate.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator getDelegate()
/*     */   {
/*  61 */     return this.delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public ObjectCodec getCodec() { return this.delegate.getCodec(); }
/*     */   
/*     */   public JsonGenerator setCodec(ObjectCodec oc) {
/*  72 */     this.delegate.setCodec(oc);
/*  73 */     return this;
/*     */   }
/*     */   
/*  76 */   public void setSchema(FormatSchema schema) { this.delegate.setSchema(schema); }
/*  77 */   public FormatSchema getSchema() { return this.delegate.getSchema(); }
/*  78 */   public Version version() { return this.delegate.version(); }
/*  79 */   public Object getOutputTarget() { return this.delegate.getOutputTarget(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canUseSchema(FormatSchema schema)
/*     */   {
/*  88 */     return this.delegate.canUseSchema(schema);
/*     */   }
/*     */   
/*  91 */   public boolean canWriteTypeId() { return this.delegate.canWriteTypeId(); }
/*     */   
/*     */   public boolean canWriteObjectId() {
/*  94 */     return this.delegate.canWriteObjectId();
/*     */   }
/*     */   
/*  97 */   public boolean canWriteBinaryNatively() { return this.delegate.canWriteBinaryNatively(); }
/*     */   
/*     */   public boolean canOmitFields() {
/* 100 */     return this.delegate.canOmitFields();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/* 110 */     this.delegate.enable(f);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/* 116 */     this.delegate.disable(f);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isEnabled(JsonGenerator.Feature f) {
/* 121 */     return this.delegate.isEnabled(f);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFeatureMask()
/*     */   {
/* 127 */     return this.delegate.getFeatureMask();
/*     */   }
/*     */   
/*     */   public JsonGenerator setFeatureMask(int mask) {
/* 131 */     this.delegate.setFeatureMask(mask);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator setPrettyPrinter(PrettyPrinter pp)
/*     */   {
/* 143 */     this.delegate.setPrettyPrinter(pp);
/* 144 */     return this;
/*     */   }
/*     */   
/*     */ 
/* 148 */   public PrettyPrinter getPrettyPrinter() { return this.delegate.getPrettyPrinter(); }
/*     */   
/*     */   public JsonGenerator useDefaultPrettyPrinter() {
/* 151 */     this.delegate.useDefaultPrettyPrinter();
/* 152 */     return this;
/*     */   }
/*     */   
/* 155 */   public JsonGenerator setHighestNonEscapedChar(int charCode) { this.delegate.setHighestNonEscapedChar(charCode);
/* 156 */     return this;
/*     */   }
/*     */   
/* 159 */   public int getHighestEscapedChar() { return this.delegate.getHighestEscapedChar(); }
/*     */   
/*     */ 
/* 162 */   public CharacterEscapes getCharacterEscapes() { return this.delegate.getCharacterEscapes(); }
/*     */   
/*     */   public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
/* 165 */     this.delegate.setCharacterEscapes(esc);
/* 166 */     return this;
/*     */   }
/*     */   
/* 169 */   public JsonGenerator setRootValueSeparator(SerializableString sep) { this.delegate.setRootValueSeparator(sep);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeStartArray()
/*     */     throws IOException
/*     */   {
/* 179 */     this.delegate.writeStartArray();
/*     */   }
/*     */   
/* 182 */   public void writeStartArray(int size) throws IOException { this.delegate.writeStartArray(size); }
/*     */   
/*     */   public void writeEndArray() throws IOException {
/* 185 */     this.delegate.writeEndArray();
/*     */   }
/*     */   
/* 188 */   public void writeStartObject() throws IOException { this.delegate.writeStartObject(); }
/*     */   
/*     */   public void writeEndObject() throws IOException {
/* 191 */     this.delegate.writeEndObject();
/*     */   }
/*     */   
/* 194 */   public void writeFieldName(String name) throws IOException { this.delegate.writeFieldName(name); }
/*     */   
/*     */   public void writeFieldName(SerializableString name) throws IOException {
/* 197 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeString(String text)
/*     */     throws IOException
/*     */   {
/* 206 */     this.delegate.writeString(text);
/*     */   }
/*     */   
/* 209 */   public void writeString(char[] text, int offset, int len) throws IOException { this.delegate.writeString(text, offset, len); }
/*     */   
/*     */   public void writeString(SerializableString text) throws IOException {
/* 212 */     this.delegate.writeString(text);
/*     */   }
/*     */   
/* 215 */   public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException { this.delegate.writeRawUTF8String(text, offset, length); }
/*     */   
/*     */   public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
/* 218 */     this.delegate.writeUTF8String(text, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeRaw(String text)
/*     */     throws IOException
/*     */   {
/* 227 */     this.delegate.writeRaw(text);
/*     */   }
/*     */   
/* 230 */   public void writeRaw(String text, int offset, int len) throws IOException { this.delegate.writeRaw(text, offset, len); }
/*     */   
/*     */   public void writeRaw(SerializableString raw) throws IOException {
/* 233 */     this.delegate.writeRaw(raw);
/*     */   }
/*     */   
/* 236 */   public void writeRaw(char[] text, int offset, int len) throws IOException { this.delegate.writeRaw(text, offset, len); }
/*     */   
/*     */   public void writeRaw(char c) throws IOException {
/* 239 */     this.delegate.writeRaw(c);
/*     */   }
/*     */   
/* 242 */   public void writeRawValue(String text) throws IOException { this.delegate.writeRawValue(text); }
/*     */   
/*     */   public void writeRawValue(String text, int offset, int len) throws IOException {
/* 245 */     this.delegate.writeRawValue(text, offset, len);
/*     */   }
/*     */   
/* 248 */   public void writeRawValue(char[] text, int offset, int len) throws IOException { this.delegate.writeRawValue(text, offset, len); }
/*     */   
/*     */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
/* 251 */     this.delegate.writeBinary(b64variant, data, offset, len);
/*     */   }
/*     */   
/* 254 */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException { return this.delegate.writeBinary(b64variant, data, dataLength); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeNumber(short v)
/*     */     throws IOException
/*     */   {
/* 263 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 266 */   public void writeNumber(int v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(long v) throws IOException {
/* 269 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 272 */   public void writeNumber(BigInteger v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(double v) throws IOException {
/* 275 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 278 */   public void writeNumber(float v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(BigDecimal v) throws IOException {
/* 281 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 284 */   public void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException { this.delegate.writeNumber(encodedValue); }
/*     */   
/*     */   public void writeBoolean(boolean state) throws IOException {
/* 287 */     this.delegate.writeBoolean(state);
/*     */   }
/*     */   
/* 290 */   public void writeNull() throws IOException { this.delegate.writeNull(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeOmittedField(String fieldName)
/*     */     throws IOException
/*     */   {
/* 299 */     this.delegate.writeOmittedField(fieldName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObjectId(Object id)
/*     */     throws IOException
/*     */   {
/* 308 */     this.delegate.writeObjectId(id);
/*     */   }
/*     */   
/* 311 */   public void writeObjectRef(Object id) throws IOException { this.delegate.writeObjectRef(id); }
/*     */   
/*     */   public void writeTypeId(Object id) throws IOException {
/* 314 */     this.delegate.writeTypeId(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObject(Object pojo)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 324 */     if (this.delegateCopyMethods) {
/* 325 */       this.delegate.writeObject(pojo);
/* 326 */       return;
/*     */     }
/*     */     
/* 329 */     if (pojo == null) {
/* 330 */       writeNull();
/*     */     } else {
/* 332 */       if (getCodec() != null) {
/* 333 */         getCodec().writeValue(this, pojo);
/* 334 */         return;
/*     */       }
/* 336 */       _writeSimpleObject(pojo);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTree(TreeNode rootNode) throws IOException
/*     */   {
/* 342 */     if (this.delegateCopyMethods) {
/* 343 */       this.delegate.writeTree(rootNode);
/* 344 */       return;
/*     */     }
/*     */     
/* 347 */     if (rootNode == null) {
/* 348 */       writeNull();
/*     */     } else {
/* 350 */       if (getCodec() == null) {
/* 351 */         throw new IllegalStateException("No ObjectCodec defined");
/*     */       }
/* 353 */       getCodec().writeValue(this, rootNode);
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
/*     */ 
/*     */ 
/*     */   public void copyCurrentEvent(JsonParser jp)
/*     */     throws IOException
/*     */   {
/* 373 */     if (this.delegateCopyMethods) this.delegate.copyCurrentEvent(jp); else {
/* 374 */       super.copyCurrentEvent(jp);
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyCurrentStructure(JsonParser jp) throws IOException {
/* 379 */     if (this.delegateCopyMethods) this.delegate.copyCurrentStructure(jp); else {
/* 380 */       super.copyCurrentStructure(jp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonStreamContext getOutputContext()
/*     */   {
/* 389 */     return this.delegate.getOutputContext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */   public void flush()
/* 397 */     throws IOException { this.delegate.flush(); }
/* 398 */   public void close() throws IOException { this.delegate.close(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 406 */     return this.delegate.isClosed();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\JsonGeneratorDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */