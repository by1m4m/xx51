/*     */ package com.fasterxml.jackson.core.base;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.json.DupDetector;
/*     */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GeneratorBase
/*     */   extends JsonGenerator
/*     */ {
/*     */   public static final int SURR1_FIRST = 55296;
/*     */   public static final int SURR1_LAST = 56319;
/*     */   public static final int SURR2_FIRST = 56320;
/*     */   public static final int SURR2_LAST = 57343;
/*  29 */   protected static final int DERIVED_FEATURES_MASK = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask() | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectCodec _objectCodec;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _features;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _cfgNumbersAsStrings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonWriteContext _writeContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _closed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected GeneratorBase(int features, ObjectCodec codec)
/*     */   {
/*  83 */     this._features = features;
/*  84 */     this._objectCodec = codec;
/*  85 */     DupDetector dups = JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(features) ? DupDetector.rootDetector(this) : null;
/*     */     
/*  87 */     this._writeContext = JsonWriteContext.createRootContext(dups);
/*  88 */     this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected GeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt)
/*     */   {
/*  96 */     this._features = features;
/*  97 */     this._objectCodec = codec;
/*  98 */     this._writeContext = ctxt;
/*  99 */     this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Version version()
/*     */   {
/* 106 */     return VersionUtil.versionFor(getClass());
/*     */   }
/*     */   
/*     */   public Object getCurrentValue() {
/* 110 */     return this._writeContext.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/* 115 */     this._writeContext.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   public final boolean isEnabled(JsonGenerator.Feature f) { return (this._features & f.getMask()) != 0; }
/* 126 */   public int getFeatureMask() { return this._features; }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/* 132 */     int mask = f.getMask();
/* 133 */     this._features |= mask;
/* 134 */     if ((mask & DERIVED_FEATURES_MASK) != 0) {
/* 135 */       if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/* 136 */         this._cfgNumbersAsStrings = true;
/* 137 */       } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
/* 138 */         setHighestNonEscapedChar(127);
/* 139 */       } else if ((f == JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION) && 
/* 140 */         (this._writeContext.getDupDetector() == null)) {
/* 141 */         this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
/*     */       }
/*     */     }
/*     */     
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/* 150 */     int mask = f.getMask();
/* 151 */     this._features &= (mask ^ 0xFFFFFFFF);
/* 152 */     if ((mask & DERIVED_FEATURES_MASK) != 0) {
/* 153 */       if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/* 154 */         this._cfgNumbersAsStrings = false;
/* 155 */       } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
/* 156 */         setHighestNonEscapedChar(0);
/* 157 */       } else if (f == JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION) {
/* 158 */         this._writeContext = this._writeContext.withDupDetector(null);
/*     */       }
/*     */     }
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator setFeatureMask(int newMask) {
/* 165 */     int changed = newMask ^ this._features;
/* 166 */     this._features = newMask;
/* 167 */     if ((changed & DERIVED_FEATURES_MASK) != 0) {
/* 168 */       this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(newMask);
/* 169 */       if (JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(changed)) {
/* 170 */         if (JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(newMask)) {
/* 171 */           setHighestNonEscapedChar(127);
/*     */         } else {
/* 173 */           setHighestNonEscapedChar(0);
/*     */         }
/*     */       }
/* 176 */       if (JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(changed)) {
/* 177 */         if (JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(newMask)) {
/* 178 */           if (this._writeContext.getDupDetector() == null) {
/* 179 */             this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
/*     */           }
/*     */         } else {
/* 182 */           this._writeContext = this._writeContext.withDupDetector(null);
/*     */         }
/*     */       }
/*     */     }
/* 186 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonGenerator useDefaultPrettyPrinter()
/*     */   {
/* 193 */     if (getPrettyPrinter() != null) {
/* 194 */       return this;
/*     */     }
/* 196 */     return setPrettyPrinter(new DefaultPrettyPrinter());
/*     */   }
/*     */   
/*     */   public JsonGenerator setCodec(ObjectCodec oc) {
/* 200 */     this._objectCodec = oc;
/* 201 */     return this;
/*     */   }
/*     */   
/* 204 */   public final ObjectCodec getCodec() { return this._objectCodec; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JsonWriteContext getOutputContext()
/*     */   {
/* 215 */     return this._writeContext;
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
/*     */   public void writeFieldName(SerializableString name)
/*     */     throws IOException
/*     */   {
/* 235 */     writeFieldName(name.getValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeString(SerializableString text)
/*     */     throws IOException
/*     */   {
/* 248 */     writeString(text.getValue());
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text) throws IOException {
/* 252 */     _verifyValueWrite("write raw value");
/* 253 */     writeRaw(text);
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text, int offset, int len) throws IOException {
/* 257 */     _verifyValueWrite("write raw value");
/* 258 */     writeRaw(text, offset, len);
/*     */   }
/*     */   
/*     */   public void writeRawValue(char[] text, int offset, int len) throws IOException {
/* 262 */     _verifyValueWrite("write raw value");
/* 263 */     writeRaw(text, offset, len);
/*     */   }
/*     */   
/*     */   public void writeRawValue(SerializableString text) throws IOException {
/* 267 */     _verifyValueWrite("write raw value");
/* 268 */     writeRaw(text);
/*     */   }
/*     */   
/*     */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*     */     throws IOException
/*     */   {
/* 274 */     _reportUnsupportedOperation();
/* 275 */     return 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObject(Object value)
/*     */     throws IOException
/*     */   {
/* 304 */     if (value == null)
/*     */     {
/* 306 */       writeNull();
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 313 */       if (this._objectCodec != null) {
/* 314 */         this._objectCodec.writeValue(this, value);
/* 315 */         return;
/*     */       }
/* 317 */       _writeSimpleObject(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTree(TreeNode rootNode)
/*     */     throws IOException
/*     */   {
/* 324 */     if (rootNode == null) {
/* 325 */       writeNull();
/*     */     } else {
/* 327 */       if (this._objectCodec == null) {
/* 328 */         throw new IllegalStateException("No ObjectCodec defined");
/*     */       }
/* 330 */       this._objectCodec.writeValue(this, rootNode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */   
/*     */ 
/* 341 */   public void close()
/* 341 */     throws IOException { this._closed = true; }
/* 342 */   public boolean isClosed() { return this._closed; }
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
/*     */   protected abstract void _releaseBuffers();
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
/*     */   protected abstract void _verifyValueWrite(String paramString)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _decodeSurrogate(int surr1, int surr2)
/*     */     throws IOException
/*     */   {
/* 378 */     if ((surr2 < 56320) || (surr2 > 57343)) {
/* 379 */       String msg = "Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2);
/* 380 */       _reportError(msg);
/*     */     }
/* 382 */     int c = 65536 + (surr1 - 55296 << 10) + (surr2 - 56320);
/* 383 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\base\GeneratorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */