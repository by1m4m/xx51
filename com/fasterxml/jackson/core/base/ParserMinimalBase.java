/*     */ package com.fasterxml.jackson.core.base;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
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
/*     */ public abstract class ParserMinimalBase
/*     */   extends JsonParser
/*     */ {
/*     */   protected static final int INT_TAB = 9;
/*     */   protected static final int INT_LF = 10;
/*     */   protected static final int INT_CR = 13;
/*     */   protected static final int INT_SPACE = 32;
/*     */   protected static final int INT_LBRACKET = 91;
/*     */   protected static final int INT_RBRACKET = 93;
/*     */   protected static final int INT_LCURLY = 123;
/*     */   protected static final int INT_RCURLY = 125;
/*     */   protected static final int INT_QUOTE = 34;
/*     */   protected static final int INT_BACKSLASH = 92;
/*     */   protected static final int INT_SLASH = 47;
/*     */   protected static final int INT_COLON = 58;
/*     */   protected static final int INT_COMMA = 44;
/*     */   protected static final int INT_HASH = 35;
/*     */   protected static final int INT_PERIOD = 46;
/*     */   protected static final int INT_e = 101;
/*     */   protected static final int INT_E = 69;
/*     */   protected JsonToken _currToken;
/*     */   protected JsonToken _lastClearedToken;
/*     */   
/*     */   protected ParserMinimalBase() {}
/*     */   
/*     */   protected ParserMinimalBase(int features)
/*     */   {
/*  73 */     super(features);
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
/*     */   public abstract JsonToken nextToken()
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
/*     */ 
/*  98 */   public JsonToken getCurrentToken() { return this._currToken; }
/*     */   
/*     */   public final int getCurrentTokenId() {
/* 101 */     JsonToken t = this._currToken;
/* 102 */     return t == null ? 0 : t.id();
/*     */   }
/*     */   
/* 105 */   public boolean hasCurrentToken() { return this._currToken != null; }
/*     */   
/* 107 */   public boolean hasTokenId(int id) { JsonToken t = this._currToken;
/* 108 */     if (t == null) {
/* 109 */       return 0 == id;
/*     */     }
/* 111 */     return t.id() == id;
/*     */   }
/*     */   
/* 114 */   public boolean isExpectedStartArrayToken() { return this._currToken == JsonToken.START_ARRAY; }
/* 115 */   public boolean isExpectedStartObjectToken() { return this._currToken == JsonToken.START_OBJECT; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken nextValue()
/*     */     throws IOException
/*     */   {
/* 123 */     JsonToken t = nextToken();
/* 124 */     if (t == JsonToken.FIELD_NAME) {
/* 125 */       t = nextToken();
/*     */     }
/* 127 */     return t;
/*     */   }
/*     */   
/*     */   public JsonParser skipChildren()
/*     */     throws IOException
/*     */   {
/* 133 */     if ((this._currToken != JsonToken.START_OBJECT) && (this._currToken != JsonToken.START_ARRAY))
/*     */     {
/* 135 */       return this;
/*     */     }
/* 137 */     int open = 1;
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 143 */       JsonToken t = nextToken();
/* 144 */       if (t == null) {
/* 145 */         _handleEOF();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 150 */         return this;
/*     */       }
/* 152 */       if (t.isStructStart()) {
/* 153 */         open++;
/* 154 */       } else if (t.isStructEnd()) {
/* 155 */         open--; if (open == 0) {
/* 156 */           return this;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void _handleEOF()
/*     */     throws JsonParseException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getCurrentName()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean isClosed();
/*     */   
/*     */ 
/*     */   public abstract JsonStreamContext getParsingContext();
/*     */   
/*     */ 
/*     */   public void clearCurrentToken()
/*     */   {
/* 187 */     if (this._currToken != null) {
/* 188 */       this._lastClearedToken = this._currToken;
/* 189 */       this._currToken = null;
/*     */     }
/*     */   }
/*     */   
/* 193 */   public JsonToken getLastClearedToken() { return this._lastClearedToken; }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void overrideCurrentName(String paramString);
/*     */   
/*     */ 
/*     */   public abstract String getText()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract char[] getTextCharacters()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract boolean hasTextCharacters();
/*     */   
/*     */ 
/*     */   public abstract int getTextLength()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract int getTextOffset()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract byte[] getBinaryValue(Base64Variant paramBase64Variant)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */     throws IOException
/*     */   {
/* 226 */     JsonToken t = this._currToken;
/* 227 */     if (t != null) {
/* 228 */       switch (t.id()) {
/*     */       case 6: 
/* 230 */         String str = getText().trim();
/* 231 */         if ("true".equals(str)) {
/* 232 */           return true;
/*     */         }
/* 234 */         if ("false".equals(str)) {
/* 235 */           return false;
/*     */         }
/* 237 */         if (_hasTextualNull(str)) {
/* 238 */           return false;
/*     */         }
/*     */         break;
/*     */       case 7: 
/* 242 */         return getIntValue() != 0;
/*     */       case 9: 
/* 244 */         return true;
/*     */       case 10: 
/*     */       case 11: 
/* 247 */         return false;
/*     */       case 12: 
/* 249 */         Object value = getEmbeddedObject();
/* 250 */         if ((value instanceof Boolean)) {
/* 251 */           return ((Boolean)value).booleanValue();
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/* 257 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int getValueAsInt(int defaultValue)
/*     */     throws IOException
/*     */   {
/* 263 */     JsonToken t = this._currToken;
/* 264 */     if (t != null) {
/* 265 */       switch (t.id()) {
/*     */       case 6: 
/* 267 */         String str = getText();
/* 268 */         if (_hasTextualNull(str)) {
/* 269 */           return 0;
/*     */         }
/* 271 */         return NumberInput.parseAsInt(str, defaultValue);
/*     */       case 7: 
/*     */       case 8: 
/* 274 */         return getIntValue();
/*     */       case 9: 
/* 276 */         return 1;
/*     */       case 10: 
/* 278 */         return 0;
/*     */       case 11: 
/* 280 */         return 0;
/*     */       case 12: 
/* 282 */         Object value = getEmbeddedObject();
/* 283 */         if ((value instanceof Number))
/* 284 */           return ((Number)value).intValue();
/*     */         break;
/*     */       }
/*     */     }
/* 288 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public long getValueAsLong(long defaultValue)
/*     */     throws IOException
/*     */   {
/* 294 */     JsonToken t = this._currToken;
/* 295 */     if (t != null) {
/* 296 */       switch (t.id()) {
/*     */       case 6: 
/* 298 */         String str = getText();
/* 299 */         if (_hasTextualNull(str)) {
/* 300 */           return 0L;
/*     */         }
/* 302 */         return NumberInput.parseAsLong(str, defaultValue);
/*     */       case 7: 
/*     */       case 8: 
/* 305 */         return getLongValue();
/*     */       case 9: 
/* 307 */         return 1L;
/*     */       case 10: 
/*     */       case 11: 
/* 310 */         return 0L;
/*     */       case 12: 
/* 312 */         Object value = getEmbeddedObject();
/* 313 */         if ((value instanceof Number))
/* 314 */           return ((Number)value).longValue();
/*     */         break;
/*     */       }
/*     */     }
/* 318 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public double getValueAsDouble(double defaultValue)
/*     */     throws IOException
/*     */   {
/* 324 */     JsonToken t = this._currToken;
/* 325 */     if (t != null) {
/* 326 */       switch (t.id()) {
/*     */       case 6: 
/* 328 */         String str = getText();
/* 329 */         if (_hasTextualNull(str)) {
/* 330 */           return 0.0D;
/*     */         }
/* 332 */         return NumberInput.parseAsDouble(str, defaultValue);
/*     */       case 7: 
/*     */       case 8: 
/* 335 */         return getDoubleValue();
/*     */       case 9: 
/* 337 */         return 1.0D;
/*     */       case 10: 
/*     */       case 11: 
/* 340 */         return 0.0D;
/*     */       case 12: 
/* 342 */         Object value = getEmbeddedObject();
/* 343 */         if ((value instanceof Number))
/* 344 */           return ((Number)value).doubleValue();
/*     */         break;
/*     */       }
/*     */     }
/* 348 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public String getValueAsString(String defaultValue) throws IOException
/*     */   {
/* 353 */     if ((this._currToken != JsonToken.VALUE_STRING) && (
/* 354 */       (this._currToken == null) || (this._currToken == JsonToken.VALUE_NULL) || (!this._currToken.isScalarValue()))) {
/* 355 */       return defaultValue;
/*     */     }
/*     */     
/* 358 */     return getText();
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
/*     */   protected void _decodeBase64(String str, ByteArrayBuilder builder, Base64Variant b64variant)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 375 */       b64variant.decode(str, builder);
/*     */     } catch (IllegalArgumentException e) {
/* 377 */       _reportError(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg)
/*     */     throws JsonParseException
/*     */   {
/*     */     String base;
/*     */     
/*     */ 
/*     */     String base;
/*     */     
/* 392 */     if (ch <= ' ') {
/* 393 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units"; } else { String base;
/* 394 */       if (b64variant.usesPaddingChar(ch)) {
/* 395 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character"; } else { String base;
/* 396 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*     */         {
/* 398 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*     */         } else
/* 400 */           base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*     */       } }
/* 402 */     if (msg != null) {
/* 403 */       base = base + ": " + msg;
/*     */     }
/* 405 */     throw _constructError(base);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void _reportBase64EOF()
/*     */     throws JsonParseException
/*     */   {
/* 414 */     throw _constructError("Unexpected end-of-String in base64 content");
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
/*     */   protected boolean _hasTextualNull(String value)
/*     */   {
/* 430 */     return "null".equals(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _reportUnexpectedChar(int ch, String comment)
/*     */     throws JsonParseException
/*     */   {
/* 440 */     if (ch < 0) {
/* 441 */       _reportInvalidEOF();
/*     */     }
/* 443 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
/* 444 */     if (comment != null) {
/* 445 */       msg = msg + ": " + comment;
/*     */     }
/* 447 */     _reportError(msg);
/*     */   }
/*     */   
/*     */   protected void _reportInvalidEOF() throws JsonParseException {
/* 451 */     _reportInvalidEOF(" in " + this._currToken);
/*     */   }
/*     */   
/*     */   protected void _reportInvalidEOF(String msg) throws JsonParseException {
/* 455 */     _reportError("Unexpected end-of-input" + msg);
/*     */   }
/*     */   
/*     */   protected void _reportInvalidEOFInValue() throws JsonParseException {
/* 459 */     _reportInvalidEOF(" in a value");
/*     */   }
/*     */   
/*     */   protected void _reportMissingRootWS(int ch) throws JsonParseException {
/* 463 */     _reportUnexpectedChar(ch, "Expected space separating root-level values");
/*     */   }
/*     */   
/*     */   protected void _throwInvalidSpace(int i) throws JsonParseException {
/* 467 */     char c = (char)i;
/* 468 */     String msg = "Illegal character (" + _getCharDesc(c) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
/* 469 */     _reportError(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _throwUnquotedSpace(int i, String ctxtDesc)
/*     */     throws JsonParseException
/*     */   {
/* 479 */     if ((!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)) || (i > 32)) {
/* 480 */       char c = (char)i;
/* 481 */       String msg = "Illegal unquoted character (" + _getCharDesc(c) + "): has to be escaped using backslash to be included in " + ctxtDesc;
/* 482 */       _reportError(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   protected char _handleUnrecognizedCharacterEscape(char ch) throws JsonProcessingException
/*     */   {
/* 488 */     if (isEnabled(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
/* 489 */       return ch;
/*     */     }
/*     */     
/* 492 */     if ((ch == '\'') && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 493 */       return ch;
/*     */     }
/* 495 */     _reportError("Unrecognized character escape " + _getCharDesc(ch));
/* 496 */     return ch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String _getCharDesc(int ch)
/*     */   {
/* 507 */     char c = (char)ch;
/* 508 */     if (Character.isISOControl(c)) {
/* 509 */       return "(CTRL-CHAR, code " + ch + ")";
/*     */     }
/* 511 */     if (ch > 255) {
/* 512 */       return "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")";
/*     */     }
/* 514 */     return "'" + c + "' (code " + ch + ")";
/*     */   }
/*     */   
/*     */   protected final void _reportError(String msg) throws JsonParseException {
/* 518 */     throw _constructError(msg);
/*     */   }
/*     */   
/*     */   protected final void _wrapError(String msg, Throwable t) throws JsonParseException {
/* 522 */     throw _constructError(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void _throwInternal() {}
/*     */   
/*     */   protected final JsonParseException _constructError(String msg, Throwable t)
/*     */   {
/* 530 */     return new JsonParseException(msg, getCurrentLocation(), t);
/*     */   }
/*     */   
/*     */   protected static byte[] _asciiBytes(String str) {
/* 534 */     byte[] b = new byte[str.length()];
/* 535 */     int i = 0; for (int len = str.length(); i < len; i++) {
/* 536 */       b[i] = ((byte)str.charAt(i));
/*     */     }
/* 538 */     return b;
/*     */   }
/*     */   
/*     */   protected static String _ascii(byte[] b) {
/*     */     try {
/* 543 */       return new String(b, "US-ASCII");
/*     */     } catch (IOException e) {
/* 545 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\base\ParserMinimalBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */