/*      */ package com.fasterxml.jackson.core;
/*      */ 
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.util.VersionUtil;
/*      */ import java.io.Closeable;
/*      */ import java.io.Flushable;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
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
/*      */ 
/*      */ public abstract class JsonGenerator
/*      */   implements Closeable, Flushable, Versioned
/*      */ {
/*      */   protected PrettyPrinter _cfgPrettyPrinter;
/*      */   public abstract JsonGenerator setCodec(ObjectCodec paramObjectCodec);
/*      */   
/*      */   public abstract ObjectCodec getCodec();
/*      */   
/*      */   public abstract Version version();
/*      */   
/*      */   public static enum Feature
/*      */   {
/*   59 */     AUTO_CLOSE_TARGET(true), 
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
/*   71 */     AUTO_CLOSE_JSON_CONTENT(true), 
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
/*   82 */     QUOTE_FIELD_NAMES(true), 
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
/*   96 */     QUOTE_NON_NUMERIC_NUMBERS(true), 
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
/*  113 */     WRITE_NUMBERS_AS_STRINGS(false), 
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
/*  125 */     WRITE_BIGDECIMAL_AS_PLAIN(false), 
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
/*  138 */     FLUSH_PASSED_TO_STREAM(true), 
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
/*  155 */     ESCAPE_NON_ASCII(false), 
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
/*  172 */     STRICT_DUPLICATE_DETECTION(false), 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  194 */     IGNORE_UNKNOWN(false);
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean _defaultState;
/*      */     
/*      */ 
/*      */     private final int _mask;
/*      */     
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  206 */       int flags = 0;
/*  207 */       for (Feature f : values()) {
/*  208 */         if (f.enabledByDefault()) {
/*  209 */           flags |= f.getMask();
/*      */         }
/*      */       }
/*  212 */       return flags;
/*      */     }
/*      */     
/*      */     private Feature(boolean defaultState) {
/*  216 */       this._defaultState = defaultState;
/*  217 */       this._mask = (1 << ordinal());
/*      */     }
/*      */     
/*  220 */     public boolean enabledByDefault() { return this._defaultState; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  225 */     public boolean enabledIn(int flags) { return (flags & this._mask) != 0; }
/*      */     
/*  227 */     public int getMask() { return this._mask; }
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
/*      */   public Object getOutputTarget()
/*      */   {
/*  289 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getCurrentValue()
/*      */   {
/*  301 */     JsonStreamContext ctxt = getOutputContext();
/*  302 */     return ctxt == null ? null : ctxt.getCurrentValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentValue(Object v)
/*      */   {
/*  314 */     JsonStreamContext ctxt = getOutputContext();
/*  315 */     if (ctxt != null) {
/*  316 */       ctxt.setCurrentValue(v);
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
/*      */   public abstract JsonGenerator enable(Feature paramFeature);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonGenerator disable(Feature paramFeature);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonGenerator configure(Feature f, boolean state)
/*      */   {
/*  349 */     if (state) enable(f); else disable(f);
/*  350 */     return this;
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
/*      */   public abstract boolean isEnabled(Feature paramFeature);
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
/*      */   public abstract int getFeatureMask();
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
/*      */   public abstract JsonGenerator setFeatureMask(int paramInt);
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
/*      */   public void setSchema(FormatSchema schema)
/*      */   {
/*  403 */     throw new UnsupportedOperationException("Generator of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FormatSchema getSchema()
/*      */   {
/*  413 */     return null;
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
/*      */   public JsonGenerator setPrettyPrinter(PrettyPrinter pp)
/*      */   {
/*  433 */     this._cfgPrettyPrinter = pp;
/*  434 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PrettyPrinter getPrettyPrinter()
/*      */   {
/*  444 */     return this._cfgPrettyPrinter;
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
/*      */   public abstract JsonGenerator useDefaultPrettyPrinter();
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
/*      */   public JsonGenerator setHighestNonEscapedChar(int charCode)
/*      */   {
/*  477 */     return this;
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
/*      */   public int getHighestEscapedChar()
/*      */   {
/*  491 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public CharacterEscapes getCharacterEscapes()
/*      */   {
/*  497 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator setCharacterEscapes(CharacterEscapes esc)
/*      */   {
/*  505 */     return this;
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
/*      */   public JsonGenerator setRootValueSeparator(SerializableString sep)
/*      */   {
/*  519 */     throw new UnsupportedOperationException();
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
/*      */   public boolean canUseSchema(FormatSchema schema)
/*      */   {
/*  536 */     return false;
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
/*      */   public boolean canWriteObjectId()
/*      */   {
/*  552 */     return false;
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
/*      */   public boolean canWriteTypeId()
/*      */   {
/*  568 */     return false;
/*      */   }
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
/*  580 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canOmitFields()
/*      */   {
/*  590 */     return true;
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
/*      */   public abstract void writeStartArray()
/*      */     throws IOException;
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
/*      */   public void writeStartArray(int size)
/*      */     throws IOException
/*      */   {
/*  625 */     writeStartArray();
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
/*      */   public abstract void writeEndArray()
/*      */     throws IOException;
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
/*      */   public abstract void writeStartObject()
/*      */     throws IOException;
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
/*      */   public abstract void writeEndObject()
/*      */     throws IOException;
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
/*      */   public abstract void writeFieldName(String paramString)
/*      */     throws IOException;
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
/*      */   public abstract void writeFieldName(SerializableString paramSerializableString)
/*      */     throws IOException;
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
/*      */   public abstract void writeString(String paramString)
/*      */     throws IOException;
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
/*      */   public abstract void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public abstract void writeString(SerializableString paramSerializableString)
/*      */     throws IOException;
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
/*      */   public abstract void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public abstract void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public abstract void writeRaw(String paramString)
/*      */     throws IOException;
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
/*      */   public abstract void writeRaw(String paramString, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public abstract void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public abstract void writeRaw(char paramChar)
/*      */     throws IOException;
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
/*      */   public void writeRaw(SerializableString raw)
/*      */     throws IOException
/*      */   {
/*  848 */     writeRaw(raw.getValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(String paramString)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(String paramString, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeRawValue(SerializableString raw)
/*      */     throws IOException
/*      */   {
/*  873 */     writeRawValue(raw.getValue());
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
/*      */   public abstract void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
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
/*      */   public void writeBinary(byte[] data, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  906 */     writeBinary(Base64Variants.getDefaultVariant(), data, offset, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeBinary(byte[] data)
/*      */     throws IOException
/*      */   {
/*  916 */     writeBinary(Base64Variants.getDefaultVariant(), data, 0, data.length);
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
/*      */   public int writeBinary(InputStream data, int dataLength)
/*      */     throws IOException
/*      */   {
/*  934 */     return writeBinary(Base64Variants.getDefaultVariant(), data, dataLength);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int writeBinary(Base64Variant paramBase64Variant, InputStream paramInputStream, int paramInt)
/*      */     throws IOException;
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
/*      */ 
/*      */ 
/*      */   public void writeNumber(short v)
/*      */     throws IOException
/*      */   {
/*  978 */     writeNumber(v);
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
/*      */   public abstract void writeNumber(int paramInt)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(long paramLong)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(BigInteger paramBigInteger)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(double paramDouble)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(float paramFloat)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(BigDecimal paramBigDecimal)
/*      */     throws IOException;
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
/*      */   public abstract void writeNumber(String paramString)
/*      */     throws IOException;
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
/*      */   public abstract void writeBoolean(boolean paramBoolean)
/*      */     throws IOException;
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
/*      */   public abstract void writeNull()
/*      */     throws IOException;
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
/*      */   public void writeObjectId(Object id)
/*      */     throws IOException
/*      */   {
/* 1106 */     throw new JsonGenerationException("No native support for writing Object Ids");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeObjectRef(Object id)
/*      */     throws IOException
/*      */   {
/* 1119 */     throw new JsonGenerationException("No native support for writing Object Ids");
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
/*      */   public void writeTypeId(Object id)
/*      */     throws IOException
/*      */   {
/* 1134 */     throw new JsonGenerationException("No native support for writing Type Ids");
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
/*      */   public abstract void writeObject(Object paramObject)
/*      */     throws IOException;
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
/*      */   public abstract void writeTree(TreeNode paramTreeNode)
/*      */     throws IOException;
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
/*      */   public void writeStringField(String fieldName, String value)
/*      */     throws IOException
/*      */   {
/* 1183 */     writeFieldName(fieldName);
/* 1184 */     writeString(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeBooleanField(String fieldName, boolean value)
/*      */     throws IOException
/*      */   {
/* 1196 */     writeFieldName(fieldName);
/* 1197 */     writeBoolean(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNullField(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1209 */     writeFieldName(fieldName);
/* 1210 */     writeNull();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, int value)
/*      */     throws IOException
/*      */   {
/* 1222 */     writeFieldName(fieldName);
/* 1223 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, long value)
/*      */     throws IOException
/*      */   {
/* 1235 */     writeFieldName(fieldName);
/* 1236 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, double value)
/*      */     throws IOException
/*      */   {
/* 1248 */     writeFieldName(fieldName);
/* 1249 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, float value)
/*      */     throws IOException
/*      */   {
/* 1261 */     writeFieldName(fieldName);
/* 1262 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, BigDecimal value)
/*      */     throws IOException
/*      */   {
/* 1275 */     writeFieldName(fieldName);
/* 1276 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeBinaryField(String fieldName, byte[] data)
/*      */     throws IOException
/*      */   {
/* 1289 */     writeFieldName(fieldName);
/* 1290 */     writeBinary(data);
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
/*      */   public final void writeArrayFieldStart(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1307 */     writeFieldName(fieldName);
/* 1308 */     writeStartArray();
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
/*      */   public final void writeObjectFieldStart(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1325 */     writeFieldName(fieldName);
/* 1326 */     writeStartObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeObjectField(String fieldName, Object pojo)
/*      */     throws IOException
/*      */   {
/* 1339 */     writeFieldName(fieldName);
/* 1340 */     writeObject(pojo);
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
/*      */   public void writeOmittedField(String fieldName)
/*      */     throws IOException
/*      */   {}
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
/*      */   public void copyCurrentEvent(JsonParser jp)
/*      */     throws IOException
/*      */   {
/* 1372 */     JsonToken t = jp.getCurrentToken();
/*      */     
/* 1374 */     if (t == null) {
/* 1375 */       _reportError("No current event to copy");
/*      */     }
/* 1377 */     switch (t.id()) {
/*      */     case -1: 
/* 1379 */       _reportError("No current event to copy");
/*      */     case 1: 
/* 1381 */       writeStartObject();
/* 1382 */       break;
/*      */     case 2: 
/* 1384 */       writeEndObject();
/* 1385 */       break;
/*      */     case 3: 
/* 1387 */       writeStartArray();
/* 1388 */       break;
/*      */     case 4: 
/* 1390 */       writeEndArray();
/* 1391 */       break;
/*      */     case 5: 
/* 1393 */       writeFieldName(jp.getCurrentName());
/* 1394 */       break;
/*      */     case 6: 
/* 1396 */       if (jp.hasTextCharacters()) {
/* 1397 */         writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
/*      */       } else {
/* 1399 */         writeString(jp.getText());
/*      */       }
/* 1401 */       break;
/*      */     
/*      */     case 7: 
/* 1404 */       JsonParser.NumberType n = jp.getNumberType();
/* 1405 */       if (n == JsonParser.NumberType.INT) {
/* 1406 */         writeNumber(jp.getIntValue());
/* 1407 */       } else if (n == JsonParser.NumberType.BIG_INTEGER) {
/* 1408 */         writeNumber(jp.getBigIntegerValue());
/*      */       } else {
/* 1410 */         writeNumber(jp.getLongValue());
/*      */       }
/* 1412 */       break;
/*      */     
/*      */ 
/*      */     case 8: 
/* 1416 */       JsonParser.NumberType n = jp.getNumberType();
/* 1417 */       if (n == JsonParser.NumberType.BIG_DECIMAL) {
/* 1418 */         writeNumber(jp.getDecimalValue());
/* 1419 */       } else if (n == JsonParser.NumberType.FLOAT) {
/* 1420 */         writeNumber(jp.getFloatValue());
/*      */       } else {
/* 1422 */         writeNumber(jp.getDoubleValue());
/*      */       }
/* 1424 */       break;
/*      */     
/*      */     case 9: 
/* 1427 */       writeBoolean(true);
/* 1428 */       break;
/*      */     case 10: 
/* 1430 */       writeBoolean(false);
/* 1431 */       break;
/*      */     case 11: 
/* 1433 */       writeNull();
/* 1434 */       break;
/*      */     case 12: 
/* 1436 */       writeObject(jp.getEmbeddedObject());
/* 1437 */       break;
/*      */     case 0: default: 
/* 1439 */       _throwInternal();
/*      */     }
/*      */     
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
/*      */   public void copyCurrentStructure(JsonParser jp)
/*      */     throws IOException
/*      */   {
/* 1475 */     JsonToken t = jp.getCurrentToken();
/* 1476 */     if (t == null) {
/* 1477 */       _reportError("No current event to copy");
/*      */     }
/*      */     
/* 1480 */     int id = t.id();
/* 1481 */     if (id == 5) {
/* 1482 */       writeFieldName(jp.getCurrentName());
/* 1483 */       t = jp.nextToken();
/* 1484 */       id = t.id();
/*      */     }
/*      */     
/* 1487 */     switch (id) {
/*      */     case 1: 
/* 1489 */       writeStartObject();
/* 1490 */       while (jp.nextToken() != JsonToken.END_OBJECT) {
/* 1491 */         copyCurrentStructure(jp);
/*      */       }
/* 1493 */       writeEndObject();
/* 1494 */       break;
/*      */     case 3: 
/* 1496 */       writeStartArray();
/* 1497 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 1498 */         copyCurrentStructure(jp);
/*      */       }
/* 1500 */       writeEndArray();
/* 1501 */       break;
/*      */     default: 
/* 1503 */       copyCurrentEvent(jp);
/*      */     }
/*      */     
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
/*      */   public abstract JsonStreamContext getOutputContext();
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
/*      */   public abstract void flush()
/*      */     throws IOException;
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
/*      */   public abstract boolean isClosed();
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
/*      */   public abstract void close()
/*      */     throws IOException;
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
/*      */   protected void _reportError(String msg)
/*      */     throws JsonGenerationException
/*      */   {
/* 1574 */     throw new JsonGenerationException(msg);
/*      */   }
/*      */   
/*      */   protected final void _throwInternal() {}
/*      */   
/*      */   protected void _reportUnsupportedOperation() {
/* 1580 */     throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
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
/*      */   protected void _writeSimpleObject(Object value)
/*      */     throws IOException
/*      */   {
/* 1596 */     if (value == null) {
/* 1597 */       writeNull();
/* 1598 */       return;
/*      */     }
/* 1600 */     if ((value instanceof String)) {
/* 1601 */       writeString((String)value);
/* 1602 */       return;
/*      */     }
/* 1604 */     if ((value instanceof Number)) {
/* 1605 */       Number n = (Number)value;
/* 1606 */       if ((n instanceof Integer)) {
/* 1607 */         writeNumber(n.intValue());
/* 1608 */         return; }
/* 1609 */       if ((n instanceof Long)) {
/* 1610 */         writeNumber(n.longValue());
/* 1611 */         return; }
/* 1612 */       if ((n instanceof Double)) {
/* 1613 */         writeNumber(n.doubleValue());
/* 1614 */         return; }
/* 1615 */       if ((n instanceof Float)) {
/* 1616 */         writeNumber(n.floatValue());
/* 1617 */         return; }
/* 1618 */       if ((n instanceof Short)) {
/* 1619 */         writeNumber(n.shortValue());
/* 1620 */         return; }
/* 1621 */       if ((n instanceof Byte)) {
/* 1622 */         writeNumber((short)n.byteValue());
/* 1623 */         return; }
/* 1624 */       if ((n instanceof BigInteger)) {
/* 1625 */         writeNumber((BigInteger)n);
/* 1626 */         return; }
/* 1627 */       if ((n instanceof BigDecimal)) {
/* 1628 */         writeNumber((BigDecimal)n);
/* 1629 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1633 */       if ((n instanceof AtomicInteger)) {
/* 1634 */         writeNumber(((AtomicInteger)n).get());
/* 1635 */         return; }
/* 1636 */       if ((n instanceof AtomicLong)) {
/* 1637 */         writeNumber(((AtomicLong)n).get());
/* 1638 */         return;
/*      */       }
/* 1640 */     } else { if ((value instanceof byte[])) {
/* 1641 */         writeBinary((byte[])value);
/* 1642 */         return; }
/* 1643 */       if ((value instanceof Boolean)) {
/* 1644 */         writeBoolean(((Boolean)value).booleanValue());
/* 1645 */         return; }
/* 1646 */       if ((value instanceof AtomicBoolean)) {
/* 1647 */         writeBoolean(((AtomicBoolean)value).get());
/* 1648 */         return;
/*      */       } }
/* 1650 */     throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */