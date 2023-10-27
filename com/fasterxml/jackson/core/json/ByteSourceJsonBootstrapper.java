/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.format.InputAccessor;
/*     */ import com.fasterxml.jackson.core.format.MatchStrength;
/*     */ import com.fasterxml.jackson.core.io.IOContext;
/*     */ import com.fasterxml.jackson.core.io.MergedStream;
/*     */ import com.fasterxml.jackson.core.io.UTF32Reader;
/*     */ import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
/*     */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
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
/*     */ public final class ByteSourceJsonBootstrapper
/*     */ {
/*     */   static final byte UTF8_BOM_1 = -17;
/*     */   static final byte UTF8_BOM_2 = -69;
/*     */   static final byte UTF8_BOM_3 = -65;
/*     */   protected final IOContext _context;
/*     */   protected final InputStream _in;
/*     */   protected final byte[] _inputBuffer;
/*     */   private int _inputPtr;
/*     */   private int _inputEnd;
/*     */   private final boolean _bufferRecyclable;
/*     */   protected int _inputProcessed;
/*  74 */   protected boolean _bigEndian = true;
/*     */   
/*  76 */   protected int _bytesPerChar = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteSourceJsonBootstrapper(IOContext ctxt, InputStream in)
/*     */   {
/*  85 */     this._context = ctxt;
/*  86 */     this._in = in;
/*  87 */     this._inputBuffer = ctxt.allocReadIOBuffer();
/*  88 */     this._inputEnd = (this._inputPtr = 0);
/*  89 */     this._inputProcessed = 0;
/*  90 */     this._bufferRecyclable = true;
/*     */   }
/*     */   
/*     */   public ByteSourceJsonBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
/*  94 */     this._context = ctxt;
/*  95 */     this._in = null;
/*  96 */     this._inputBuffer = inputBuffer;
/*  97 */     this._inputPtr = inputStart;
/*  98 */     this._inputEnd = (inputStart + inputLen);
/*     */     
/* 100 */     this._inputProcessed = (-inputStart);
/* 101 */     this._bufferRecyclable = false;
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
/*     */   public JsonEncoding detectEncoding()
/*     */     throws IOException
/*     */   {
/* 117 */     boolean foundEncoding = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */     if (ensureLoaded(4)) {
/* 127 */       int quad = this._inputBuffer[this._inputPtr] << 24 | (this._inputBuffer[(this._inputPtr + 1)] & 0xFF) << 16 | (this._inputBuffer[(this._inputPtr + 2)] & 0xFF) << 8 | this._inputBuffer[(this._inputPtr + 3)] & 0xFF;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 132 */       if (handleBOM(quad)) {
/* 133 */         foundEncoding = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 141 */       else if (checkUTF32(quad)) {
/* 142 */         foundEncoding = true;
/* 143 */       } else if (checkUTF16(quad >>> 16)) {
/* 144 */         foundEncoding = true;
/*     */       }
/*     */     }
/* 147 */     else if (ensureLoaded(2)) {
/* 148 */       int i16 = (this._inputBuffer[this._inputPtr] & 0xFF) << 8 | this._inputBuffer[(this._inputPtr + 1)] & 0xFF;
/*     */       
/* 150 */       if (checkUTF16(i16)) {
/* 151 */         foundEncoding = true;
/*     */       }
/*     */     }
/*     */     
/*     */     JsonEncoding enc;
/*     */     
/*     */     JsonEncoding enc;
/* 158 */     if (!foundEncoding) {
/* 159 */       enc = JsonEncoding.UTF8;
/*     */     } else {
/* 161 */       switch (this._bytesPerChar) {
/* 162 */       case 1:  enc = JsonEncoding.UTF8;
/* 163 */         break;
/* 164 */       case 2:  enc = this._bigEndian ? JsonEncoding.UTF16_BE : JsonEncoding.UTF16_LE;
/* 165 */         break;
/* 166 */       case 4:  enc = this._bigEndian ? JsonEncoding.UTF32_BE : JsonEncoding.UTF32_LE;
/* 167 */         break;
/* 168 */       case 3: default:  throw new RuntimeException("Internal error");
/*     */       }
/*     */     }
/* 171 */     this._context.setEncoding(enc);
/* 172 */     return enc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Reader constructReader()
/*     */     throws IOException
/*     */   {
/* 184 */     JsonEncoding enc = this._context.getEncoding();
/* 185 */     switch (enc.bits())
/*     */     {
/*     */ 
/*     */     case 8: 
/*     */     case 16: 
/* 190 */       InputStream in = this._in;
/*     */       
/* 192 */       if (in == null) {
/* 193 */         in = new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 198 */       else if (this._inputPtr < this._inputEnd) {
/* 199 */         in = new MergedStream(this._context, in, this._inputBuffer, this._inputPtr, this._inputEnd);
/*     */       }
/*     */       
/* 202 */       return new InputStreamReader(in, enc.getJavaName());
/*     */     
/*     */     case 32: 
/* 205 */       return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
/*     */     }
/*     */     
/* 208 */     throw new RuntimeException("Internal error");
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonParser constructParser(int parserFeatures, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols, CharsToNameCanonicalizer rootCharSymbols, int factoryFeatures)
/*     */     throws IOException
/*     */   {
/* 215 */     JsonEncoding enc = detectEncoding();
/*     */     
/* 217 */     if (enc == JsonEncoding.UTF8)
/*     */     {
/*     */ 
/*     */ 
/* 221 */       if (JsonFactory.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(factoryFeatures)) {
/* 222 */         BytesToNameCanonicalizer can = rootByteSymbols.makeChild(factoryFeatures);
/* 223 */         return new UTF8StreamJsonParser(this._context, parserFeatures, this._in, codec, can, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
/*     */       }
/*     */     }
/*     */     
/* 227 */     return new ReaderBasedJsonParser(this._context, parserFeatures, constructReader(), codec, rootCharSymbols.makeChild(factoryFeatures));
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
/*     */   public static MatchStrength hasJSONFormat(InputAccessor acc)
/*     */     throws IOException
/*     */   {
/* 248 */     if (!acc.hasMoreBytes()) {
/* 249 */       return MatchStrength.INCONCLUSIVE;
/*     */     }
/* 251 */     byte b = acc.nextByte();
/*     */     
/* 253 */     if (b == -17) {
/* 254 */       if (!acc.hasMoreBytes()) {
/* 255 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 257 */       if (acc.nextByte() != -69) {
/* 258 */         return MatchStrength.NO_MATCH;
/*     */       }
/* 260 */       if (!acc.hasMoreBytes()) {
/* 261 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 263 */       if (acc.nextByte() != -65) {
/* 264 */         return MatchStrength.NO_MATCH;
/*     */       }
/* 266 */       if (!acc.hasMoreBytes()) {
/* 267 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 269 */       b = acc.nextByte();
/*     */     }
/*     */     
/* 272 */     int ch = skipSpace(acc, b);
/* 273 */     if (ch < 0) {
/* 274 */       return MatchStrength.INCONCLUSIVE;
/*     */     }
/*     */     
/* 277 */     if (ch == 123)
/*     */     {
/* 279 */       ch = skipSpace(acc);
/* 280 */       if (ch < 0) {
/* 281 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 283 */       if ((ch == 34) || (ch == 125)) {
/* 284 */         return MatchStrength.SOLID_MATCH;
/*     */       }
/*     */       
/* 287 */       return MatchStrength.NO_MATCH;
/*     */     }
/*     */     
/*     */ 
/* 291 */     if (ch == 91) {
/* 292 */       ch = skipSpace(acc);
/* 293 */       if (ch < 0) {
/* 294 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/*     */       
/* 297 */       if ((ch == 93) || (ch == 91)) {
/* 298 */         return MatchStrength.SOLID_MATCH;
/*     */       }
/* 300 */       return MatchStrength.SOLID_MATCH;
/*     */     }
/*     */     
/* 303 */     MatchStrength strength = MatchStrength.WEAK_MATCH;
/*     */     
/*     */ 
/* 306 */     if (ch == 34) {
/* 307 */       return strength;
/*     */     }
/* 309 */     if ((ch <= 57) && (ch >= 48)) {
/* 310 */       return strength;
/*     */     }
/* 312 */     if (ch == 45) {
/* 313 */       ch = skipSpace(acc);
/* 314 */       if (ch < 0) {
/* 315 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 317 */       return (ch <= 57) && (ch >= 48) ? strength : MatchStrength.NO_MATCH;
/*     */     }
/*     */     
/* 320 */     if (ch == 110) {
/* 321 */       return tryMatch(acc, "ull", strength);
/*     */     }
/* 323 */     if (ch == 116) {
/* 324 */       return tryMatch(acc, "rue", strength);
/*     */     }
/* 326 */     if (ch == 102) {
/* 327 */       return tryMatch(acc, "alse", strength);
/*     */     }
/* 329 */     return MatchStrength.NO_MATCH;
/*     */   }
/*     */   
/*     */   private static MatchStrength tryMatch(InputAccessor acc, String matchStr, MatchStrength fullMatchStrength)
/*     */     throws IOException
/*     */   {
/* 335 */     int i = 0; for (int len = matchStr.length(); i < len; i++) {
/* 336 */       if (!acc.hasMoreBytes()) {
/* 337 */         return MatchStrength.INCONCLUSIVE;
/*     */       }
/* 339 */       if (acc.nextByte() != matchStr.charAt(i)) {
/* 340 */         return MatchStrength.NO_MATCH;
/*     */       }
/*     */     }
/* 343 */     return fullMatchStrength;
/*     */   }
/*     */   
/*     */   private static int skipSpace(InputAccessor acc) throws IOException
/*     */   {
/* 348 */     if (!acc.hasMoreBytes()) {
/* 349 */       return -1;
/*     */     }
/* 351 */     return skipSpace(acc, acc.nextByte());
/*     */   }
/*     */   
/*     */   private static int skipSpace(InputAccessor acc, byte b) throws IOException
/*     */   {
/*     */     for (;;) {
/* 357 */       int ch = b & 0xFF;
/* 358 */       if ((ch != 32) && (ch != 13) && (ch != 10) && (ch != 9)) {
/* 359 */         return ch;
/*     */       }
/* 361 */       if (!acc.hasMoreBytes()) {
/* 362 */         return -1;
/*     */       }
/* 364 */       b = acc.nextByte();
/* 365 */       ch = b & 0xFF;
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
/*     */   private boolean handleBOM(int quad)
/*     */     throws IOException
/*     */   {
/* 384 */     switch (quad) {
/*     */     case 65279: 
/* 386 */       this._bigEndian = true;
/* 387 */       this._inputPtr += 4;
/* 388 */       this._bytesPerChar = 4;
/* 389 */       return true;
/*     */     case -131072: 
/* 391 */       this._inputPtr += 4;
/* 392 */       this._bytesPerChar = 4;
/* 393 */       this._bigEndian = false;
/* 394 */       return true;
/*     */     case 65534: 
/* 396 */       reportWeirdUCS4("2143");
/*     */     case -16842752: 
/* 398 */       reportWeirdUCS4("3412");
/*     */     }
/*     */     
/* 401 */     int msw = quad >>> 16;
/* 402 */     if (msw == 65279) {
/* 403 */       this._inputPtr += 2;
/* 404 */       this._bytesPerChar = 2;
/* 405 */       this._bigEndian = true;
/* 406 */       return true;
/*     */     }
/* 408 */     if (msw == 65534) {
/* 409 */       this._inputPtr += 2;
/* 410 */       this._bytesPerChar = 2;
/* 411 */       this._bigEndian = false;
/* 412 */       return true;
/*     */     }
/*     */     
/* 415 */     if (quad >>> 8 == 15711167) {
/* 416 */       this._inputPtr += 3;
/* 417 */       this._bytesPerChar = 1;
/* 418 */       this._bigEndian = true;
/* 419 */       return true;
/*     */     }
/* 421 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean checkUTF32(int quad)
/*     */     throws IOException
/*     */   {
/* 429 */     if (quad >> 8 == 0) {
/* 430 */       this._bigEndian = true;
/* 431 */     } else if ((quad & 0xFFFFFF) == 0) {
/* 432 */       this._bigEndian = false;
/* 433 */     } else if ((quad & 0xFF00FFFF) == 0) {
/* 434 */       reportWeirdUCS4("3412");
/* 435 */     } else if ((quad & 0xFFFF00FF) == 0) {
/* 436 */       reportWeirdUCS4("2143");
/*     */     }
/*     */     else {
/* 439 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 443 */     this._bytesPerChar = 4;
/* 444 */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkUTF16(int i16)
/*     */   {
/* 449 */     if ((i16 & 0xFF00) == 0) {
/* 450 */       this._bigEndian = true;
/* 451 */     } else if ((i16 & 0xFF) == 0) {
/* 452 */       this._bigEndian = false;
/*     */     } else {
/* 454 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 458 */     this._bytesPerChar = 2;
/* 459 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void reportWeirdUCS4(String type)
/*     */     throws IOException
/*     */   {
/* 469 */     throw new CharConversionException("Unsupported UCS-4 endianness (" + type + ") detected");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean ensureLoaded(int minimum)
/*     */     throws IOException
/*     */   {
/* 482 */     int gotten = this._inputEnd - this._inputPtr;
/* 483 */     while (gotten < minimum) {
/*     */       int count;
/*     */       int count;
/* 486 */       if (this._in == null) {
/* 487 */         count = -1;
/*     */       } else {
/* 489 */         count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
/*     */       }
/* 491 */       if (count < 1) {
/* 492 */         return false;
/*     */       }
/* 494 */       this._inputEnd += count;
/* 495 */       gotten += count;
/*     */     }
/* 497 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\ByteSourceJsonBootstrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */