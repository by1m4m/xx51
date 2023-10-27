/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.base.GeneratorBase;
/*     */ import com.fasterxml.jackson.core.io.CharTypes;
/*     */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*     */ import com.fasterxml.jackson.core.io.IOContext;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
/*     */ import java.io.IOException;
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
/*     */ public abstract class JsonGeneratorImpl
/*     */   extends GeneratorBase
/*     */ {
/*  31 */   protected static final int[] sOutputEscapes = ;
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
/*     */   protected final IOContext _ioContext;
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
/*  53 */   protected int[] _outputEscapes = sOutputEscapes;
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
/*     */   protected int _maximumNonEscapedChar;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CharacterEscapes _characterEscapes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected SerializableString _rootValueSeparator = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGeneratorImpl(IOContext ctxt, int features, ObjectCodec codec)
/*     */   {
/*  95 */     super(features, codec);
/*  96 */     this._ioContext = ctxt;
/*  97 */     if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII)) {
/*  98 */       setHighestNonEscapedChar(127);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator setHighestNonEscapedChar(int charCode)
/*     */   {
/* 110 */     this._maximumNonEscapedChar = (charCode < 0 ? 0 : charCode);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public int getHighestEscapedChar()
/*     */   {
/* 116 */     return this._maximumNonEscapedChar;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonGenerator setCharacterEscapes(CharacterEscapes esc)
/*     */   {
/* 122 */     this._characterEscapes = esc;
/* 123 */     if (esc == null) {
/* 124 */       this._outputEscapes = sOutputEscapes;
/*     */     } else {
/* 126 */       this._outputEscapes = esc.getEscapeCodesForAscii();
/*     */     }
/* 128 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharacterEscapes getCharacterEscapes()
/*     */   {
/* 137 */     return this._characterEscapes;
/*     */   }
/*     */   
/*     */   public JsonGenerator setRootValueSeparator(SerializableString sep)
/*     */   {
/* 142 */     this._rootValueSeparator = sep;
/* 143 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Version version()
/*     */   {
/* 154 */     return VersionUtil.versionFor(getClass());
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
/*     */   public final void writeStringField(String fieldName, String value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 169 */     writeFieldName(fieldName);
/* 170 */     writeString(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\JsonGeneratorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */