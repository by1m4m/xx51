/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.base.ParserBase;
/*      */ import com.fasterxml.jackson.core.io.CharTypes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ReaderBasedJsonParser
/*      */   extends ParserBase
/*      */ {
/*   24 */   protected static final int[] _icLatin1 = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Reader _reader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _inputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _bufferRecyclable;
/*      */   
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
/*      */   protected final CharsToNameCanonicalizer _symbols;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _hashSeed;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   77 */   protected boolean _tokenIncomplete = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*      */   {
/*   96 */     super(ctxt, features);
/*   97 */     this._reader = r;
/*   98 */     this._inputBuffer = inputBuffer;
/*   99 */     this._inputPtr = start;
/*  100 */     this._inputEnd = end;
/*  101 */     this._objectCodec = codec;
/*  102 */     this._symbols = st;
/*  103 */     this._hashSeed = st.hashSeed();
/*  104 */     this._bufferRecyclable = bufferRecyclable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st)
/*      */   {
/*  114 */     super(ctxt, features);
/*  115 */     this._reader = r;
/*  116 */     this._inputBuffer = ctxt.allocTokenBuffer();
/*  117 */     this._inputPtr = 0;
/*  118 */     this._inputEnd = 0;
/*  119 */     this._objectCodec = codec;
/*  120 */     this._symbols = st;
/*  121 */     this._hashSeed = st.hashSeed();
/*  122 */     this._bufferRecyclable = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  131 */   public ObjectCodec getCodec() { return this._objectCodec; }
/*  132 */   public void setCodec(ObjectCodec c) { this._objectCodec = c; }
/*      */   
/*      */   public int releaseBuffered(Writer w) throws IOException
/*      */   {
/*  136 */     int count = this._inputEnd - this._inputPtr;
/*  137 */     if (count < 1) { return 0;
/*      */     }
/*  139 */     int origPtr = this._inputPtr;
/*  140 */     w.write(this._inputBuffer, origPtr, count);
/*  141 */     return count;
/*      */   }
/*      */   
/*  144 */   public Object getInputSource() { return this._reader; }
/*      */   
/*      */   protected boolean loadMore()
/*      */     throws IOException
/*      */   {
/*  149 */     this._currInputProcessed += this._inputEnd;
/*  150 */     this._currInputRowStart -= this._inputEnd;
/*      */     
/*  152 */     if (this._reader != null) {
/*  153 */       int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
/*  154 */       if (count > 0) {
/*  155 */         this._inputPtr = 0;
/*  156 */         this._inputEnd = count;
/*  157 */         return true;
/*      */       }
/*      */       
/*  160 */       _closeInput();
/*      */       
/*  162 */       if (count == 0) {
/*  163 */         throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
/*      */       }
/*      */     }
/*  166 */     return false;
/*      */   }
/*      */   
/*      */   protected char getNextChar(String eofMsg) throws IOException {
/*  170 */     if ((this._inputPtr >= this._inputEnd) && 
/*  171 */       (!loadMore())) { _reportInvalidEOF(eofMsg);
/*      */     }
/*  173 */     return this._inputBuffer[(this._inputPtr++)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _closeInput()
/*      */     throws IOException
/*      */   {
/*  185 */     if (this._reader != null) {
/*  186 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/*  187 */         this._reader.close();
/*      */       }
/*  189 */       this._reader = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */     throws IOException
/*      */   {
/*  201 */     super._releaseBuffers();
/*      */     
/*  203 */     this._symbols.release();
/*      */     
/*  205 */     if (this._bufferRecyclable) {
/*  206 */       char[] buf = this._inputBuffer;
/*  207 */       if (buf != null) {
/*  208 */         this._inputBuffer = null;
/*  209 */         this._ioContext.releaseTokenBuffer(buf);
/*      */       }
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
/*      */   public final String getText()
/*      */     throws IOException
/*      */   {
/*  229 */     JsonToken t = this._currToken;
/*  230 */     if (t == JsonToken.VALUE_STRING) {
/*  231 */       if (this._tokenIncomplete) {
/*  232 */         this._tokenIncomplete = false;
/*  233 */         _finishString();
/*      */       }
/*  235 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  237 */     return _getText2(t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getValueAsString()
/*      */     throws IOException
/*      */   {
/*  246 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  247 */       if (this._tokenIncomplete) {
/*  248 */         this._tokenIncomplete = false;
/*  249 */         _finishString();
/*      */       }
/*  251 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  253 */     return super.getValueAsString(null);
/*      */   }
/*      */   
/*      */   public final String getValueAsString(String defValue)
/*      */     throws IOException
/*      */   {
/*  259 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  260 */       if (this._tokenIncomplete) {
/*  261 */         this._tokenIncomplete = false;
/*  262 */         _finishString();
/*      */       }
/*  264 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  266 */     return super.getValueAsString(defValue);
/*      */   }
/*      */   
/*      */   protected final String _getText2(JsonToken t) {
/*  270 */     if (t == null) {
/*  271 */       return null;
/*      */     }
/*  273 */     switch (t.id()) {
/*      */     case 5: 
/*  275 */       return this._parsingContext.getCurrentName();
/*      */     
/*      */ 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*  281 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  283 */     return t.asString();
/*      */   }
/*      */   
/*      */ 
/*      */   public final char[] getTextCharacters()
/*      */     throws IOException
/*      */   {
/*  290 */     if (this._currToken != null) {
/*  291 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  293 */         if (!this._nameCopied) {
/*  294 */           String name = this._parsingContext.getCurrentName();
/*  295 */           int nameLen = name.length();
/*  296 */           if (this._nameCopyBuffer == null) {
/*  297 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  298 */           } else if (this._nameCopyBuffer.length < nameLen) {
/*  299 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  301 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  302 */           this._nameCopied = true;
/*      */         }
/*  304 */         return this._nameCopyBuffer;
/*      */       
/*      */       case 6: 
/*  307 */         if (this._tokenIncomplete) {
/*  308 */           this._tokenIncomplete = false;
/*  309 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  314 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*      */       
/*  317 */       return this._currToken.asCharArray();
/*      */     }
/*      */     
/*  320 */     return null;
/*      */   }
/*      */   
/*      */   public final int getTextLength()
/*      */     throws IOException
/*      */   {
/*  326 */     if (this._currToken != null) {
/*  327 */       switch (this._currToken.id())
/*      */       {
/*      */       case 5: 
/*  330 */         return this._parsingContext.getCurrentName().length();
/*      */       case 6: 
/*  332 */         if (this._tokenIncomplete) {
/*  333 */           this._tokenIncomplete = false;
/*  334 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  339 */         return this._textBuffer.size();
/*      */       }
/*      */       
/*  342 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */     
/*  345 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int getTextOffset()
/*      */     throws IOException
/*      */   {
/*  352 */     if (this._currToken != null) {
/*  353 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  355 */         return 0;
/*      */       case 6: 
/*  357 */         if (this._tokenIncomplete) {
/*  358 */           this._tokenIncomplete = false;
/*  359 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  364 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */       
/*      */     }
/*  368 */     return 0;
/*      */   }
/*      */   
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/*  374 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  376 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  381 */     if (this._tokenIncomplete) {
/*      */       try {
/*  383 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  385 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  390 */       this._tokenIncomplete = false;
/*      */     }
/*  392 */     else if (this._binaryValue == null)
/*      */     {
/*  394 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  395 */       _decodeBase64(getText(), builder, b64variant);
/*  396 */       this._binaryValue = builder.toByteArray();
/*      */     }
/*      */     
/*  399 */     return this._binaryValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */     throws IOException
/*      */   {
/*  406 */     if ((!this._tokenIncomplete) || (this._currToken != JsonToken.VALUE_STRING)) {
/*  407 */       byte[] b = getBinaryValue(b64variant);
/*  408 */       out.write(b);
/*  409 */       return b.length;
/*      */     }
/*      */     
/*  412 */     byte[] buf = this._ioContext.allocBase64Buffer();
/*      */     try {
/*  414 */       return _readBinary(b64variant, out, buf);
/*      */     } finally {
/*  416 */       this._ioContext.releaseBase64Buffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */   protected int _readBinary(Base64Variant b64variant, OutputStream out, byte[] buffer) throws IOException
/*      */   {
/*  422 */     int outputPtr = 0;
/*  423 */     int outputEnd = buffer.length - 3;
/*  424 */     int outputCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  430 */       if (this._inputPtr >= this._inputEnd) {
/*  431 */         loadMoreGuaranteed();
/*      */       }
/*  433 */       char ch = this._inputBuffer[(this._inputPtr++)];
/*  434 */       if (ch > ' ') {
/*  435 */         int bits = b64variant.decodeBase64Char(ch);
/*  436 */         if (bits < 0) {
/*  437 */           if (ch == '"') {
/*      */             break;
/*      */           }
/*  440 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/*  441 */           if (bits < 0) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  447 */           if (outputPtr > outputEnd) {
/*  448 */             outputCount += outputPtr;
/*  449 */             out.write(buffer, 0, outputPtr);
/*  450 */             outputPtr = 0;
/*      */           }
/*      */           
/*  453 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/*  457 */           if (this._inputPtr >= this._inputEnd) {
/*  458 */             loadMoreGuaranteed();
/*      */           }
/*  460 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  461 */           bits = b64variant.decodeBase64Char(ch);
/*  462 */           if (bits < 0) {
/*  463 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/*  465 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/*  468 */           if (this._inputPtr >= this._inputEnd) {
/*  469 */             loadMoreGuaranteed();
/*      */           }
/*  471 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  472 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/*  475 */           if (bits < 0) {
/*  476 */             if (bits != -2)
/*      */             {
/*  478 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/*  479 */                 decodedData >>= 4;
/*  480 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  481 */                 break;
/*      */               }
/*  483 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/*  485 */             if (bits == -2)
/*      */             {
/*  487 */               if (this._inputPtr >= this._inputEnd) {
/*  488 */                 loadMoreGuaranteed();
/*      */               }
/*  490 */               ch = this._inputBuffer[(this._inputPtr++)];
/*  491 */               if (!b64variant.usesPaddingChar(ch)) {
/*  492 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/*  495 */               decodedData >>= 4;
/*  496 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  497 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  501 */           decodedData = decodedData << 6 | bits;
/*      */           
/*  503 */           if (this._inputPtr >= this._inputEnd) {
/*  504 */             loadMoreGuaranteed();
/*      */           }
/*  506 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  507 */           bits = b64variant.decodeBase64Char(ch);
/*  508 */           if (bits < 0) {
/*  509 */             if (bits != -2)
/*      */             {
/*  511 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/*  512 */                 decodedData >>= 2;
/*  513 */                 buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  514 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  515 */                 break;
/*      */               }
/*  517 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/*  519 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  526 */               decodedData >>= 2;
/*  527 */               buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  528 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  529 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  533 */           decodedData = decodedData << 6 | bits;
/*  534 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 16));
/*  535 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  536 */           buffer[(outputPtr++)] = ((byte)decodedData);
/*      */         } } }
/*  538 */     this._tokenIncomplete = false;
/*  539 */     if (outputPtr > 0) {
/*  540 */       outputCount += outputPtr;
/*  541 */       out.write(buffer, 0, outputPtr);
/*      */     }
/*  543 */     return outputCount;
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
/*      */   public final JsonToken nextToken()
/*      */     throws IOException
/*      */   {
/*  559 */     this._numTypesValid = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  565 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  566 */       return _nextAfterName();
/*      */     }
/*  568 */     if (this._tokenIncomplete) {
/*  569 */       _skipString();
/*      */     }
/*  571 */     int i = _skipWSOrEnd();
/*  572 */     if (i < 0)
/*      */     {
/*      */ 
/*      */ 
/*  576 */       close();
/*  577 */       return this._currToken = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  583 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  584 */     this._tokenInputRow = this._currInputRow;
/*  585 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */     
/*      */ 
/*  588 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  591 */     if (i == 93) {
/*  592 */       if (!this._parsingContext.inArray()) {
/*  593 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  595 */       this._parsingContext = this._parsingContext.getParent();
/*  596 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  598 */     if (i == 125) {
/*  599 */       if (!this._parsingContext.inObject()) {
/*  600 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  602 */       this._parsingContext = this._parsingContext.getParent();
/*  603 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */     
/*      */ 
/*  607 */     if (this._parsingContext.expectComma()) {
/*  608 */       i = _skipComma(i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  615 */     boolean inObject = this._parsingContext.inObject();
/*  616 */     if (inObject)
/*      */     {
/*  618 */       String name = i == 34 ? _parseName() : _handleOddName(i);
/*  619 */       this._parsingContext.setCurrentName(name);
/*  620 */       this._currToken = JsonToken.FIELD_NAME;
/*  621 */       i = _skipColon();
/*      */     }
/*      */     
/*      */ 
/*      */     JsonToken t;
/*      */     
/*      */ 
/*  628 */     switch (i) {
/*      */     case 34: 
/*  630 */       this._tokenIncomplete = true;
/*  631 */       t = JsonToken.VALUE_STRING;
/*  632 */       break;
/*      */     case 91: 
/*  634 */       if (!inObject) {
/*  635 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  637 */       t = JsonToken.START_ARRAY;
/*  638 */       break;
/*      */     case 123: 
/*  640 */       if (!inObject) {
/*  641 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  643 */       t = JsonToken.START_OBJECT;
/*  644 */       break;
/*      */     
/*      */ 
/*      */     case 93: 
/*      */     case 125: 
/*  649 */       _reportUnexpectedChar(i, "expected a value");
/*      */     case 116: 
/*  651 */       _matchTrue();
/*  652 */       t = JsonToken.VALUE_TRUE;
/*  653 */       break;
/*      */     case 102: 
/*  655 */       _matchFalse();
/*  656 */       t = JsonToken.VALUE_FALSE;
/*  657 */       break;
/*      */     case 110: 
/*  659 */       _matchNull();
/*  660 */       t = JsonToken.VALUE_NULL;
/*  661 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 45: 
/*  668 */       t = _parseNegNumber();
/*  669 */       break;
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/*  680 */       t = _parsePosNumber(i);
/*  681 */       break;
/*      */     default: 
/*  683 */       t = _handleOddValue(i);
/*      */     }
/*      */     
/*      */     
/*  687 */     if (inObject) {
/*  688 */       this._nextToken = t;
/*  689 */       return this._currToken;
/*      */     }
/*  691 */     this._currToken = t;
/*  692 */     return t;
/*      */   }
/*      */   
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  697 */     this._nameCopied = false;
/*  698 */     JsonToken t = this._nextToken;
/*  699 */     this._nextToken = null;
/*      */     
/*  701 */     if (t == JsonToken.START_ARRAY) {
/*  702 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  703 */     } else if (t == JsonToken.START_OBJECT) {
/*  704 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  706 */     return this._currToken = t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String nextTextValue()
/*      */     throws IOException
/*      */   {
/*  719 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  720 */       this._nameCopied = false;
/*  721 */       JsonToken t = this._nextToken;
/*  722 */       this._nextToken = null;
/*  723 */       this._currToken = t;
/*  724 */       if (t == JsonToken.VALUE_STRING) {
/*  725 */         if (this._tokenIncomplete) {
/*  726 */           this._tokenIncomplete = false;
/*  727 */           _finishString();
/*      */         }
/*  729 */         return this._textBuffer.contentsAsString();
/*      */       }
/*  731 */       if (t == JsonToken.START_ARRAY) {
/*  732 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  733 */       } else if (t == JsonToken.START_OBJECT) {
/*  734 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  736 */       return null;
/*      */     }
/*      */     
/*  739 */     return nextToken() == JsonToken.VALUE_STRING ? getText() : null;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int nextIntValue(int defaultValue)
/*      */     throws IOException
/*      */   {
/*  746 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  747 */       this._nameCopied = false;
/*  748 */       JsonToken t = this._nextToken;
/*  749 */       this._nextToken = null;
/*  750 */       this._currToken = t;
/*  751 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  752 */         return getIntValue();
/*      */       }
/*  754 */       if (t == JsonToken.START_ARRAY) {
/*  755 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  756 */       } else if (t == JsonToken.START_OBJECT) {
/*  757 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  759 */       return defaultValue;
/*      */     }
/*      */     
/*  762 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public final long nextLongValue(long defaultValue)
/*      */     throws IOException
/*      */   {
/*  769 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  770 */       this._nameCopied = false;
/*  771 */       JsonToken t = this._nextToken;
/*  772 */       this._nextToken = null;
/*  773 */       this._currToken = t;
/*  774 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  775 */         return getLongValue();
/*      */       }
/*  777 */       if (t == JsonToken.START_ARRAY) {
/*  778 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  779 */       } else if (t == JsonToken.START_OBJECT) {
/*  780 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  782 */       return defaultValue;
/*      */     }
/*      */     
/*  785 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public final Boolean nextBooleanValue()
/*      */     throws IOException
/*      */   {
/*  792 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  793 */       this._nameCopied = false;
/*  794 */       JsonToken t = this._nextToken;
/*  795 */       this._nextToken = null;
/*  796 */       this._currToken = t;
/*  797 */       if (t == JsonToken.VALUE_TRUE) {
/*  798 */         return Boolean.TRUE;
/*      */       }
/*  800 */       if (t == JsonToken.VALUE_FALSE) {
/*  801 */         return Boolean.FALSE;
/*      */       }
/*  803 */       if (t == JsonToken.START_ARRAY) {
/*  804 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  805 */       } else if (t == JsonToken.START_OBJECT) {
/*  806 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  808 */       return null;
/*      */     }
/*  810 */     JsonToken t = nextToken();
/*  811 */     if (t != null) {
/*  812 */       int id = t.id();
/*  813 */       if (id == 9) return Boolean.TRUE;
/*  814 */       if (id == 10) return Boolean.FALSE;
/*      */     }
/*  816 */     return null;
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
/*      */   protected final JsonToken _parsePosNumber(int ch)
/*      */     throws IOException
/*      */   {
/*  847 */     int ptr = this._inputPtr;
/*  848 */     int startPtr = ptr - 1;
/*  849 */     int inputLen = this._inputEnd;
/*      */     
/*      */ 
/*  852 */     if (ch == 48) {
/*  853 */       return _parseNumber2(false, startPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  862 */     int intLen = 1;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/*  867 */       if (ptr >= inputLen) {
/*  868 */         this._inputPtr = startPtr;
/*  869 */         return _parseNumber2(false, startPtr);
/*      */       }
/*  871 */       ch = this._inputBuffer[(ptr++)];
/*  872 */       if ((ch < 48) || (ch > 57)) {
/*      */         break;
/*      */       }
/*  875 */       intLen++;
/*      */     }
/*  877 */     if ((ch == 46) || (ch == 101) || (ch == 69)) {
/*  878 */       this._inputPtr = ptr;
/*  879 */       return _parseFloat(ch, startPtr, ptr, false, intLen);
/*      */     }
/*      */     
/*  882 */     ptr--;
/*  883 */     this._inputPtr = ptr;
/*      */     
/*  885 */     if (this._parsingContext.inRoot()) {
/*  886 */       _verifyRootSpace(ch);
/*      */     }
/*  888 */     int len = ptr - startPtr;
/*  889 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/*  890 */     return resetInt(false, intLen);
/*      */   }
/*      */   
/*      */   private final JsonToken _parseFloat(int ch, int startPtr, int ptr, boolean neg, int intLen)
/*      */     throws IOException
/*      */   {
/*  896 */     int inputLen = this._inputEnd;
/*  897 */     int fractLen = 0;
/*      */     
/*      */ 
/*  900 */     if (ch == 46)
/*      */     {
/*      */       for (;;) {
/*  903 */         if (ptr >= inputLen) {
/*  904 */           return _parseNumber2(neg, startPtr);
/*      */         }
/*  906 */         ch = this._inputBuffer[(ptr++)];
/*  907 */         if ((ch < 48) || (ch > 57)) {
/*      */           break;
/*      */         }
/*  910 */         fractLen++;
/*      */       }
/*      */       
/*  913 */       if (fractLen == 0) {
/*  914 */         reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*  917 */     int expLen = 0;
/*  918 */     if ((ch == 101) || (ch == 69)) {
/*  919 */       if (ptr >= inputLen) {
/*  920 */         this._inputPtr = startPtr;
/*  921 */         return _parseNumber2(neg, startPtr);
/*      */       }
/*      */       
/*  924 */       ch = this._inputBuffer[(ptr++)];
/*  925 */       if ((ch == 45) || (ch == 43)) {
/*  926 */         if (ptr >= inputLen) {
/*  927 */           this._inputPtr = startPtr;
/*  928 */           return _parseNumber2(neg, startPtr);
/*      */         }
/*  930 */         ch = this._inputBuffer[(ptr++)];
/*      */       }
/*  932 */       while ((ch <= 57) && (ch >= 48)) {
/*  933 */         expLen++;
/*  934 */         if (ptr >= inputLen) {
/*  935 */           this._inputPtr = startPtr;
/*  936 */           return _parseNumber2(neg, startPtr);
/*      */         }
/*  938 */         ch = this._inputBuffer[(ptr++)];
/*      */       }
/*      */       
/*  941 */       if (expLen == 0) {
/*  942 */         reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/*  945 */     ptr--;
/*  946 */     this._inputPtr = ptr;
/*      */     
/*  948 */     if (this._parsingContext.inRoot()) {
/*  949 */       _verifyRootSpace(ch);
/*      */     }
/*  951 */     int len = ptr - startPtr;
/*  952 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/*      */     
/*  954 */     return resetFloat(neg, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */   protected final JsonToken _parseNegNumber() throws IOException
/*      */   {
/*  959 */     int ptr = this._inputPtr;
/*  960 */     int startPtr = ptr - 1;
/*  961 */     int inputLen = this._inputEnd;
/*      */     
/*  963 */     if (ptr >= inputLen) {
/*  964 */       return _parseNumber2(true, startPtr);
/*      */     }
/*  966 */     int ch = this._inputBuffer[(ptr++)];
/*      */     
/*  968 */     if ((ch > 57) || (ch < 48)) {
/*  969 */       this._inputPtr = ptr;
/*  970 */       return _handleInvalidNumberStart(ch, true);
/*      */     }
/*      */     
/*  973 */     if (ch == 48) {
/*  974 */       return _parseNumber2(true, startPtr);
/*      */     }
/*  976 */     int intLen = 1;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/*  981 */       if (ptr >= inputLen) {
/*  982 */         return _parseNumber2(true, startPtr);
/*      */       }
/*  984 */       ch = this._inputBuffer[(ptr++)];
/*  985 */       if ((ch < 48) || (ch > 57)) {
/*      */         break;
/*      */       }
/*  988 */       intLen++;
/*      */     }
/*      */     
/*  991 */     if ((ch == 46) || (ch == 101) || (ch == 69)) {
/*  992 */       this._inputPtr = ptr;
/*  993 */       return _parseFloat(ch, startPtr, ptr, true, intLen);
/*      */     }
/*  995 */     ptr--;
/*  996 */     this._inputPtr = ptr;
/*  997 */     if (this._parsingContext.inRoot()) {
/*  998 */       _verifyRootSpace(ch);
/*      */     }
/* 1000 */     int len = ptr - startPtr;
/* 1001 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 1002 */     return resetInt(true, intLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final JsonToken _parseNumber2(boolean neg, int startPtr)
/*      */     throws IOException
/*      */   {
/* 1014 */     this._inputPtr = (neg ? startPtr + 1 : startPtr);
/* 1015 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1016 */     int outPtr = 0;
/*      */     
/*      */ 
/* 1019 */     if (neg) {
/* 1020 */       outBuf[(outPtr++)] = '-';
/*      */     }
/*      */     
/*      */ 
/* 1024 */     int intLen = 0;
/* 1025 */     char c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("No digit following minus sign");
/* 1026 */     if (c == '0') {
/* 1027 */       c = _verifyNoLeadingZeroes();
/*      */     }
/* 1029 */     boolean eof = false;
/*      */     
/*      */ 
/*      */ 
/* 1033 */     while ((c >= '0') && (c <= '9')) {
/* 1034 */       intLen++;
/* 1035 */       if (outPtr >= outBuf.length) {
/* 1036 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1037 */         outPtr = 0;
/*      */       }
/* 1039 */       outBuf[(outPtr++)] = c;
/* 1040 */       if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
/*      */       {
/* 1042 */         c = '\000';
/* 1043 */         eof = true;
/* 1044 */         break;
/*      */       }
/* 1046 */       c = this._inputBuffer[(this._inputPtr++)];
/*      */     }
/*      */     
/* 1049 */     if (intLen == 0) {
/* 1050 */       return _handleInvalidNumberStart(c, neg);
/*      */     }
/*      */     
/* 1053 */     int fractLen = 0;
/*      */     
/* 1055 */     if (c == '.') {
/* 1056 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */       for (;;)
/*      */       {
/* 1060 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1061 */           eof = true;
/* 1062 */           break;
/*      */         }
/* 1064 */         c = this._inputBuffer[(this._inputPtr++)];
/* 1065 */         if ((c < '0') || (c > '9')) {
/*      */           break;
/*      */         }
/* 1068 */         fractLen++;
/* 1069 */         if (outPtr >= outBuf.length) {
/* 1070 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1071 */           outPtr = 0;
/*      */         }
/* 1073 */         outBuf[(outPtr++)] = c;
/*      */       }
/*      */       
/* 1076 */       if (fractLen == 0) {
/* 1077 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/* 1081 */     int expLen = 0;
/* 1082 */     if ((c == 'e') || (c == 'E')) {
/* 1083 */       if (outPtr >= outBuf.length) {
/* 1084 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1085 */         outPtr = 0;
/*      */       }
/* 1087 */       outBuf[(outPtr++)] = c;
/*      */       
/* 1089 */       c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*      */       
/*      */ 
/* 1092 */       if ((c == '-') || (c == '+')) {
/* 1093 */         if (outPtr >= outBuf.length) {
/* 1094 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1095 */           outPtr = 0;
/*      */         }
/* 1097 */         outBuf[(outPtr++)] = c;
/*      */         
/* 1099 */         c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1104 */       while ((c <= '9') && (c >= '0')) {
/* 1105 */         expLen++;
/* 1106 */         if (outPtr >= outBuf.length) {
/* 1107 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1108 */           outPtr = 0;
/*      */         }
/* 1110 */         outBuf[(outPtr++)] = c;
/* 1111 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1112 */           eof = true;
/* 1113 */           break;
/*      */         }
/* 1115 */         c = this._inputBuffer[(this._inputPtr++)];
/*      */       }
/*      */       
/* 1118 */       if (expLen == 0) {
/* 1119 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1124 */     if (!eof) {
/* 1125 */       this._inputPtr -= 1;
/* 1126 */       if (this._parsingContext.inRoot()) {
/* 1127 */         _verifyRootSpace(c);
/*      */       }
/*      */     }
/* 1130 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1132 */     return reset(neg, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final char _verifyNoLeadingZeroes()
/*      */     throws IOException
/*      */   {
/* 1142 */     if (this._inputPtr < this._inputEnd) {
/* 1143 */       char ch = this._inputBuffer[this._inputPtr];
/*      */       
/* 1145 */       if ((ch < '0') || (ch > '9')) {
/* 1146 */         return '0';
/*      */       }
/*      */     }
/*      */     
/* 1150 */     return _verifyNLZ2();
/*      */   }
/*      */   
/*      */   private char _verifyNLZ2() throws IOException
/*      */   {
/* 1155 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1156 */       return '0';
/*      */     }
/* 1158 */     char ch = this._inputBuffer[this._inputPtr];
/* 1159 */     if ((ch < '0') || (ch > '9')) {
/* 1160 */       return '0';
/*      */     }
/* 1162 */     if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
/* 1163 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */     }
/*      */     
/* 1166 */     this._inputPtr += 1;
/* 1167 */     if (ch == '0') {
/* 1168 */       while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1169 */         ch = this._inputBuffer[this._inputPtr];
/* 1170 */         if ((ch < '0') || (ch > '9')) {
/* 1171 */           return '0';
/*      */         }
/* 1173 */         this._inputPtr += 1;
/* 1174 */         if (ch != '0') {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1179 */     return ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleInvalidNumberStart(int ch, boolean negative)
/*      */     throws IOException
/*      */   {
/* 1188 */     if (ch == 73) {
/* 1189 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1190 */         (!loadMore())) { _reportInvalidEOFInValue();
/*      */       }
/* 1192 */       ch = this._inputBuffer[(this._inputPtr++)];
/* 1193 */       if (ch == 78) {
/* 1194 */         String match = negative ? "-INF" : "+INF";
/* 1195 */         _matchToken(match, 3);
/* 1196 */         if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1197 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1199 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1200 */       } else if (ch == 110) {
/* 1201 */         String match = negative ? "-Infinity" : "+Infinity";
/* 1202 */         _matchToken(match, 3);
/* 1203 */         if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1204 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1206 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */       }
/*      */     }
/* 1209 */     reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/* 1210 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _verifyRootSpace(int ch)
/*      */     throws IOException
/*      */   {
/* 1223 */     this._inputPtr += 1;
/* 1224 */     switch (ch) {
/*      */     case 9: 
/*      */     case 32: 
/* 1227 */       return;
/*      */     case 13: 
/* 1229 */       _skipCR();
/* 1230 */       return;
/*      */     case 10: 
/* 1232 */       this._currInputRow += 1;
/* 1233 */       this._currInputRowStart = this._inputPtr;
/* 1234 */       return;
/*      */     }
/* 1236 */     _reportMissingRootWS(ch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String _parseName()
/*      */     throws IOException
/*      */   {
/* 1249 */     int ptr = this._inputPtr;
/* 1250 */     int hash = this._hashSeed;
/* 1251 */     int[] codes = _icLatin1;
/*      */     
/* 1253 */     while (ptr < this._inputEnd) {
/* 1254 */       int ch = this._inputBuffer[ptr];
/* 1255 */       if ((ch < codes.length) && (codes[ch] != 0)) {
/* 1256 */         if (ch != 34) break;
/* 1257 */         int start = this._inputPtr;
/* 1258 */         this._inputPtr = (ptr + 1);
/* 1259 */         return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */       }
/*      */       
/*      */ 
/* 1263 */       hash = hash * 33 + ch;
/* 1264 */       ptr++;
/*      */     }
/* 1266 */     int start = this._inputPtr;
/* 1267 */     this._inputPtr = ptr;
/* 1268 */     return _parseName2(start, hash, 34);
/*      */   }
/*      */   
/*      */   private String _parseName2(int startPtr, int hash, int endChar) throws IOException
/*      */   {
/* 1273 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1278 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1279 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     for (;;)
/*      */     {
/* 1282 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1283 */         (!loadMore())) {
/* 1284 */         _reportInvalidEOF(": was expecting closing '" + (char)endChar + "' for name");
/*      */       }
/*      */       
/* 1287 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 1288 */       int i = c;
/* 1289 */       if (i <= 92) {
/* 1290 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1295 */           c = _decodeEscaped();
/* 1296 */         } else if (i <= endChar) {
/* 1297 */           if (i == endChar) {
/*      */             break;
/*      */           }
/* 1300 */           if (i < 32) {
/* 1301 */             _throwUnquotedSpace(i, "name");
/*      */           }
/*      */         }
/*      */       }
/* 1305 */       hash = hash * 33 + c;
/*      */       
/* 1307 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */ 
/* 1310 */       if (outPtr >= outBuf.length) {
/* 1311 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1312 */         outPtr = 0;
/*      */       }
/*      */     }
/* 1315 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1317 */     TextBuffer tb = this._textBuffer;
/* 1318 */     char[] buf = tb.getTextBuffer();
/* 1319 */     int start = tb.getTextOffset();
/* 1320 */     int len = tb.size();
/* 1321 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _handleOddName(int i)
/*      */     throws IOException
/*      */   {
/* 1334 */     if ((i == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 1335 */       return _parseAposName();
/*      */     }
/*      */     
/* 1338 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/* 1339 */       _reportUnexpectedChar(i, "was expecting double-quote to start field name");
/*      */     }
/* 1341 */     int[] codes = CharTypes.getInputCodeLatin1JsNames();
/* 1342 */     int maxCode = codes.length;
/*      */     
/*      */     boolean firstOk;
/*      */     
/*      */     boolean firstOk;
/* 1347 */     if (i < maxCode) {
/* 1348 */       firstOk = codes[i] == 0;
/*      */     } else {
/* 1350 */       firstOk = Character.isJavaIdentifierPart((char)i);
/*      */     }
/* 1352 */     if (!firstOk) {
/* 1353 */       _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/* 1355 */     int ptr = this._inputPtr;
/* 1356 */     int hash = this._hashSeed;
/* 1357 */     int inputLen = this._inputEnd;
/*      */     
/* 1359 */     if (ptr < inputLen) {
/*      */       do {
/* 1361 */         int ch = this._inputBuffer[ptr];
/* 1362 */         if (ch < maxCode) {
/* 1363 */           if (codes[ch] != 0) {
/* 1364 */             int start = this._inputPtr - 1;
/* 1365 */             this._inputPtr = ptr;
/* 1366 */             return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */           }
/* 1368 */         } else if (!Character.isJavaIdentifierPart((char)ch)) {
/* 1369 */           int start = this._inputPtr - 1;
/* 1370 */           this._inputPtr = ptr;
/* 1371 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/* 1373 */         hash = hash * 33 + ch;
/* 1374 */         ptr++;
/* 1375 */       } while (ptr < inputLen);
/*      */     }
/* 1377 */     int start = this._inputPtr - 1;
/* 1378 */     this._inputPtr = ptr;
/* 1379 */     return _handleOddName2(start, hash, codes);
/*      */   }
/*      */   
/*      */   protected String _parseAposName()
/*      */     throws IOException
/*      */   {
/* 1385 */     int ptr = this._inputPtr;
/* 1386 */     int hash = this._hashSeed;
/* 1387 */     int inputLen = this._inputEnd;
/*      */     
/* 1389 */     if (ptr < inputLen) {
/* 1390 */       int[] codes = _icLatin1;
/* 1391 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/* 1394 */         int ch = this._inputBuffer[ptr];
/* 1395 */         if (ch == 39) {
/* 1396 */           int start = this._inputPtr;
/* 1397 */           this._inputPtr = (ptr + 1);
/* 1398 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/* 1400 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/*      */           break;
/*      */         }
/* 1403 */         hash = hash * 33 + ch;
/* 1404 */         ptr++;
/* 1405 */       } while (ptr < inputLen);
/*      */     }
/*      */     
/* 1408 */     int start = this._inputPtr;
/* 1409 */     this._inputPtr = ptr;
/*      */     
/* 1411 */     return _parseName2(start, hash, 39);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleOddValue(int i)
/*      */     throws IOException
/*      */   {
/* 1421 */     switch (i)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 39: 
/* 1430 */       if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
/* 1431 */         return _handleApos();
/*      */       }
/*      */       break;
/*      */     case 78: 
/* 1435 */       _matchToken("NaN", 1);
/* 1436 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1437 */         return resetAsNaN("NaN", NaN.0D);
/*      */       }
/* 1439 */       _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1440 */       break;
/*      */     case 73: 
/* 1442 */       _matchToken("Infinity", 1);
/* 1443 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1444 */         return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
/*      */       }
/* 1446 */       _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1447 */       break;
/*      */     case 43: 
/* 1449 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1450 */         (!loadMore())) {
/* 1451 */         _reportInvalidEOFInValue();
/*      */       }
/*      */       
/* 1454 */       return _handleInvalidNumberStart(this._inputBuffer[(this._inputPtr++)], false);
/*      */     }
/*      */     
/* 1457 */     if (Character.isJavaIdentifierStart(i)) {
/* 1458 */       _reportInvalidToken("" + (char)i, "('true', 'false' or 'null')");
/*      */     }
/*      */     
/* 1461 */     _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/* 1462 */     return null;
/*      */   }
/*      */   
/*      */   protected JsonToken _handleApos() throws IOException
/*      */   {
/* 1467 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1468 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     for (;;)
/*      */     {
/* 1471 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1472 */         (!loadMore())) {
/* 1473 */         _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */       }
/*      */       
/* 1476 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 1477 */       int i = c;
/* 1478 */       if (i <= 92) {
/* 1479 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1484 */           c = _decodeEscaped();
/* 1485 */         } else if (i <= 39) {
/* 1486 */           if (i == 39) {
/*      */             break;
/*      */           }
/* 1489 */           if (i < 32) {
/* 1490 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1495 */       if (outPtr >= outBuf.length) {
/* 1496 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1497 */         outPtr = 0;
/*      */       }
/*      */       
/* 1500 */       outBuf[(outPtr++)] = c;
/*      */     }
/* 1502 */     this._textBuffer.setCurrentLength(outPtr);
/* 1503 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */   
/*      */   private String _handleOddName2(int startPtr, int hash, int[] codes) throws IOException
/*      */   {
/* 1508 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/* 1509 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1510 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 1511 */     int maxCode = codes.length;
/*      */     
/*      */ 
/* 1514 */     while ((this._inputPtr < this._inputEnd) || 
/* 1515 */       (loadMore()))
/*      */     {
/*      */ 
/*      */ 
/* 1519 */       char c = this._inputBuffer[this._inputPtr];
/* 1520 */       int i = c;
/* 1521 */       if (i <= maxCode ? 
/* 1522 */         codes[i] != 0 : 
/*      */         
/*      */ 
/* 1525 */         !Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 1528 */       this._inputPtr += 1;
/* 1529 */       hash = hash * 33 + i;
/*      */       
/* 1531 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */ 
/* 1534 */       if (outPtr >= outBuf.length) {
/* 1535 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1536 */         outPtr = 0;
/*      */       }
/*      */     }
/* 1539 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1541 */     TextBuffer tb = this._textBuffer;
/* 1542 */     char[] buf = tb.getTextBuffer();
/* 1543 */     int start = tb.getTextOffset();
/* 1544 */     int len = tb.size();
/*      */     
/* 1546 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _finishString()
/*      */     throws IOException
/*      */   {
/* 1557 */     int ptr = this._inputPtr;
/* 1558 */     int inputLen = this._inputEnd;
/*      */     
/* 1560 */     if (ptr < inputLen) {
/* 1561 */       int[] codes = _icLatin1;
/* 1562 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/* 1565 */         int ch = this._inputBuffer[ptr];
/* 1566 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/* 1567 */           if (ch != 34) break;
/* 1568 */           this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 1569 */           this._inputPtr = (ptr + 1);
/*      */           
/* 1571 */           return;
/*      */         }
/*      */         
/*      */ 
/* 1575 */         ptr++;
/* 1576 */       } while (ptr < inputLen);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1582 */     this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 1583 */     this._inputPtr = ptr;
/* 1584 */     _finishString2();
/*      */   }
/*      */   
/*      */   protected void _finishString2() throws IOException
/*      */   {
/* 1589 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1590 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 1591 */     int[] codes = _icLatin1;
/* 1592 */     int maxCode = codes.length;
/*      */     for (;;)
/*      */     {
/* 1595 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1596 */         (!loadMore())) {
/* 1597 */         _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */       }
/*      */       
/* 1600 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 1601 */       int i = c;
/* 1602 */       if ((i < maxCode) && (codes[i] != 0)) {
/* 1603 */         if (i == 34)
/*      */           break;
/* 1605 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1610 */           c = _decodeEscaped();
/* 1611 */         } else if (i < 32) {
/* 1612 */           _throwUnquotedSpace(i, "string value");
/*      */         }
/*      */       }
/*      */       
/* 1616 */       if (outPtr >= outBuf.length) {
/* 1617 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1618 */         outPtr = 0;
/*      */       }
/*      */       
/* 1621 */       outBuf[(outPtr++)] = c;
/*      */     }
/* 1623 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _skipString()
/*      */     throws IOException
/*      */   {
/* 1633 */     this._tokenIncomplete = false;
/*      */     
/* 1635 */     int inPtr = this._inputPtr;
/* 1636 */     int inLen = this._inputEnd;
/* 1637 */     char[] inBuf = this._inputBuffer;
/*      */     for (;;)
/*      */     {
/* 1640 */       if (inPtr >= inLen) {
/* 1641 */         this._inputPtr = inPtr;
/* 1642 */         if (!loadMore()) {
/* 1643 */           _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */         }
/* 1645 */         inPtr = this._inputPtr;
/* 1646 */         inLen = this._inputEnd;
/*      */       }
/* 1648 */       char c = inBuf[(inPtr++)];
/* 1649 */       int i = c;
/* 1650 */       if (i <= 92) {
/* 1651 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1656 */           this._inputPtr = inPtr;
/* 1657 */           c = _decodeEscaped();
/* 1658 */           inPtr = this._inputPtr;
/* 1659 */           inLen = this._inputEnd;
/* 1660 */         } else if (i <= 34) {
/* 1661 */           if (i == 34) {
/* 1662 */             this._inputPtr = inPtr;
/* 1663 */             break;
/*      */           }
/* 1665 */           if (i < 32) {
/* 1666 */             this._inputPtr = inPtr;
/* 1667 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */       }
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
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/* 1685 */     if (((this._inputPtr < this._inputEnd) || (loadMore())) && 
/* 1686 */       (this._inputBuffer[this._inputPtr] == '\n')) {
/* 1687 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 1690 */     this._currInputRow += 1;
/* 1691 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */   
/*      */   private final int _skipColon() throws IOException
/*      */   {
/* 1696 */     if (this._inputPtr + 4 >= this._inputEnd) {
/* 1697 */       return _skipColon2(false);
/*      */     }
/* 1699 */     char c = this._inputBuffer[this._inputPtr];
/* 1700 */     if (c == ':') {
/* 1701 */       int i = this._inputBuffer[(++this._inputPtr)];
/* 1702 */       if (i > 32) {
/* 1703 */         if ((i == 47) || (i == 35)) {
/* 1704 */           return _skipColon2(true);
/*      */         }
/* 1706 */         this._inputPtr += 1;
/* 1707 */         return i;
/*      */       }
/* 1709 */       if ((i == 32) || (i == 9)) {
/* 1710 */         i = this._inputBuffer[(++this._inputPtr)];
/* 1711 */         if (i > 32) {
/* 1712 */           if ((i == 47) || (i == 35)) {
/* 1713 */             return _skipColon2(true);
/*      */           }
/* 1715 */           this._inputPtr += 1;
/* 1716 */           return i;
/*      */         }
/*      */       }
/* 1719 */       return _skipColon2(true);
/*      */     }
/* 1721 */     if ((c == ' ') || (c == '\t')) {
/* 1722 */       c = this._inputBuffer[(++this._inputPtr)];
/*      */     }
/* 1724 */     if (c == ':') {
/* 1725 */       int i = this._inputBuffer[(++this._inputPtr)];
/* 1726 */       if (i > 32) {
/* 1727 */         if ((i == 47) || (i == 35)) {
/* 1728 */           return _skipColon2(true);
/*      */         }
/* 1730 */         this._inputPtr += 1;
/* 1731 */         return i;
/*      */       }
/* 1733 */       if ((i == 32) || (i == 9)) {
/* 1734 */         i = this._inputBuffer[(++this._inputPtr)];
/* 1735 */         if (i > 32) {
/* 1736 */           if ((i == 47) || (i == 35)) {
/* 1737 */             return _skipColon2(true);
/*      */           }
/* 1739 */           this._inputPtr += 1;
/* 1740 */           return i;
/*      */         }
/*      */       }
/* 1743 */       return _skipColon2(true);
/*      */     }
/* 1745 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final int _skipColon2(boolean gotColon) throws IOException
/*      */   {
/*      */     for (;;) {
/* 1751 */       if (this._inputPtr >= this._inputEnd) {
/* 1752 */         loadMoreGuaranteed();
/*      */       }
/* 1754 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1755 */       if (i > 32) {
/* 1756 */         if (i == 47) {
/* 1757 */           _skipComment();
/*      */ 
/*      */         }
/* 1760 */         else if ((i != 35) || 
/* 1761 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 1765 */           if (gotColon) {
/* 1766 */             return i;
/*      */           }
/* 1768 */           if (i != 58) {
/* 1769 */             if (i < 32) {
/* 1770 */               _throwInvalidSpace(i);
/*      */             }
/* 1772 */             _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */           }
/* 1774 */           gotColon = true;
/*      */         }
/*      */       }
/* 1777 */       else if (i < 32) {
/* 1778 */         if (i == 10) {
/* 1779 */           this._currInputRow += 1;
/* 1780 */           this._currInputRowStart = this._inputPtr;
/* 1781 */         } else if (i == 13) {
/* 1782 */           _skipCR();
/* 1783 */         } else if (i != 9) {
/* 1784 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final int _skipComma(int i)
/*      */     throws IOException
/*      */   {
/* 1793 */     if (i != 44) {
/* 1794 */       _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */     }
/* 1796 */     while (this._inputPtr < this._inputEnd) {
/* 1797 */       i = this._inputBuffer[(this._inputPtr++)];
/* 1798 */       if (i > 32) {
/* 1799 */         if ((i == 47) || (i == 35)) {
/* 1800 */           this._inputPtr -= 1;
/* 1801 */           return _skipAfterComma2();
/*      */         }
/* 1803 */         return i;
/*      */       }
/* 1805 */       if (i < 32) {
/* 1806 */         if (i == 10) {
/* 1807 */           this._currInputRow += 1;
/* 1808 */           this._currInputRowStart = this._inputPtr;
/* 1809 */         } else if (i == 13) {
/* 1810 */           _skipCR();
/* 1811 */         } else if (i != 9) {
/* 1812 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 1816 */     return _skipAfterComma2();
/*      */   }
/*      */   
/*      */   private final int _skipAfterComma2() throws IOException
/*      */   {
/* 1821 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1822 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1823 */       if (i > 32) {
/* 1824 */         if (i == 47) {
/* 1825 */           _skipComment();
/*      */ 
/*      */         }
/* 1828 */         else if ((i != 35) || 
/* 1829 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 1833 */           return i;
/*      */         }
/* 1835 */       } else if (i < 32) {
/* 1836 */         if (i == 10) {
/* 1837 */           this._currInputRow += 1;
/* 1838 */           this._currInputRowStart = this._inputPtr;
/* 1839 */         } else if (i == 13) {
/* 1840 */           _skipCR();
/* 1841 */         } else if (i != 9) {
/* 1842 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 1846 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException
/*      */   {
/* 1853 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1854 */       (!loadMore())) {
/* 1855 */       return _eofAsNextChar();
/*      */     }
/*      */     
/* 1858 */     int i = this._inputBuffer[(this._inputPtr++)];
/* 1859 */     if (i > 32) {
/* 1860 */       if ((i == 47) || (i == 35)) {
/* 1861 */         this._inputPtr -= 1;
/* 1862 */         return _skipWSOrEnd2();
/*      */       }
/* 1864 */       return i;
/*      */     }
/* 1866 */     if (i != 32) {
/* 1867 */       if (i == 10) {
/* 1868 */         this._currInputRow += 1;
/* 1869 */         this._currInputRowStart = this._inputPtr;
/* 1870 */       } else if (i == 13) {
/* 1871 */         _skipCR();
/* 1872 */       } else if (i != 9) {
/* 1873 */         _throwInvalidSpace(i);
/*      */       }
/*      */     }
/*      */     
/* 1877 */     while (this._inputPtr < this._inputEnd) {
/* 1878 */       i = this._inputBuffer[(this._inputPtr++)];
/* 1879 */       if (i > 32) {
/* 1880 */         if ((i == 47) || (i == 35)) {
/* 1881 */           this._inputPtr -= 1;
/* 1882 */           return _skipWSOrEnd2();
/*      */         }
/* 1884 */         return i;
/*      */       }
/* 1886 */       if (i != 32) {
/* 1887 */         if (i == 10) {
/* 1888 */           this._currInputRow += 1;
/* 1889 */           this._currInputRowStart = this._inputPtr;
/* 1890 */         } else if (i == 13) {
/* 1891 */           _skipCR();
/* 1892 */         } else if (i != 9) {
/* 1893 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 1897 */     return _skipWSOrEnd2();
/*      */   }
/*      */   
/*      */   private int _skipWSOrEnd2() throws IOException
/*      */   {
/*      */     for (;;) {
/* 1903 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1904 */         (!loadMore())) {
/* 1905 */         return _eofAsNextChar();
/*      */       }
/*      */       
/* 1908 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1909 */       if (i > 32) {
/* 1910 */         if (i == 47) {
/* 1911 */           _skipComment();
/*      */ 
/*      */         }
/* 1914 */         else if ((i != 35) || 
/* 1915 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 1919 */           return i; }
/* 1920 */       } else if (i != 32) {
/* 1921 */         if (i == 10) {
/* 1922 */           this._currInputRow += 1;
/* 1923 */           this._currInputRowStart = this._inputPtr;
/* 1924 */         } else if (i == 13) {
/* 1925 */           _skipCR();
/* 1926 */         } else if (i != 9) {
/* 1927 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _skipComment() throws IOException
/*      */   {
/* 1935 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/* 1936 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */     
/* 1939 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1940 */       _reportInvalidEOF(" in a comment");
/*      */     }
/* 1942 */     char c = this._inputBuffer[(this._inputPtr++)];
/* 1943 */     if (c == '/') {
/* 1944 */       _skipLine();
/* 1945 */     } else if (c == '*') {
/* 1946 */       _skipCComment();
/*      */     } else {
/* 1948 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */     }
/*      */   }
/*      */   
/*      */   private void _skipCComment()
/*      */     throws IOException
/*      */   {
/* 1955 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1956 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1957 */       if (i <= 42) {
/* 1958 */         if (i == 42) {
/* 1959 */           if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*      */             break;
/*      */           }
/* 1962 */           if (this._inputBuffer[this._inputPtr] == '/') {
/* 1963 */             this._inputPtr += 1;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/* 1968 */         else if (i < 32) {
/* 1969 */           if (i == 10) {
/* 1970 */             this._currInputRow += 1;
/* 1971 */             this._currInputRowStart = this._inputPtr;
/* 1972 */           } else if (i == 13) {
/* 1973 */             _skipCR();
/* 1974 */           } else if (i != 9) {
/* 1975 */             _throwInvalidSpace(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1980 */     _reportInvalidEOF(" in a comment");
/*      */   }
/*      */   
/*      */   private boolean _skipYAMLComment() throws IOException
/*      */   {
/* 1985 */     if (!isEnabled(JsonParser.Feature.ALLOW_YAML_COMMENTS)) {
/* 1986 */       return false;
/*      */     }
/* 1988 */     _skipLine();
/* 1989 */     return true;
/*      */   }
/*      */   
/*      */   private void _skipLine()
/*      */     throws IOException
/*      */   {
/* 1995 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1996 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1997 */       if (i < 32) {
/* 1998 */         if (i == 10) {
/* 1999 */           this._currInputRow += 1;
/* 2000 */           this._currInputRowStart = this._inputPtr;
/* 2001 */           break; }
/* 2002 */         if (i == 13) {
/* 2003 */           _skipCR();
/* 2004 */           break; }
/* 2005 */         if (i != 9) {
/* 2006 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected char _decodeEscaped()
/*      */     throws IOException
/*      */   {
/* 2015 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2016 */       (!loadMore())) {
/* 2017 */       _reportInvalidEOF(" in character escape sequence");
/*      */     }
/*      */     
/* 2020 */     char c = this._inputBuffer[(this._inputPtr++)];
/*      */     
/* 2022 */     switch (c)
/*      */     {
/*      */     case 'b': 
/* 2025 */       return '\b';
/*      */     case 't': 
/* 2027 */       return '\t';
/*      */     case 'n': 
/* 2029 */       return '\n';
/*      */     case 'f': 
/* 2031 */       return '\f';
/*      */     case 'r': 
/* 2033 */       return '\r';
/*      */     
/*      */ 
/*      */     case '"': 
/*      */     case '/': 
/*      */     case '\\': 
/* 2039 */       return c;
/*      */     
/*      */     case 'u': 
/*      */       break;
/*      */     
/*      */     default: 
/* 2045 */       return _handleUnrecognizedCharacterEscape(c);
/*      */     }
/*      */     
/*      */     
/* 2049 */     int value = 0;
/* 2050 */     for (int i = 0; i < 4; i++) {
/* 2051 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2052 */         (!loadMore())) {
/* 2053 */         _reportInvalidEOF(" in character escape sequence");
/*      */       }
/*      */       
/* 2056 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 2057 */       int digit = CharTypes.charToHex(ch);
/* 2058 */       if (digit < 0) {
/* 2059 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 2061 */       value = value << 4 | digit;
/*      */     }
/* 2063 */     return (char)value;
/*      */   }
/*      */   
/*      */   private final void _matchTrue() throws IOException {
/* 2067 */     int ptr = this._inputPtr;
/* 2068 */     if (ptr + 3 < this._inputEnd) {
/* 2069 */       char[] b = this._inputBuffer;
/* 2070 */       if ((b[ptr] == 'r') && (b[(++ptr)] == 'u') && (b[(++ptr)] == 'e')) {
/* 2071 */         char c = b[(++ptr)];
/* 2072 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2073 */           this._inputPtr = ptr;
/* 2074 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2079 */     _matchToken("true", 1);
/*      */   }
/*      */   
/*      */   private final void _matchFalse() throws IOException {
/* 2083 */     int ptr = this._inputPtr;
/* 2084 */     if (ptr + 4 < this._inputEnd) {
/* 2085 */       char[] b = this._inputBuffer;
/* 2086 */       if ((b[ptr] == 'a') && (b[(++ptr)] == 'l') && (b[(++ptr)] == 's') && (b[(++ptr)] == 'e')) {
/* 2087 */         char c = b[(++ptr)];
/* 2088 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2089 */           this._inputPtr = ptr;
/* 2090 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2095 */     _matchToken("false", 1);
/*      */   }
/*      */   
/*      */   private final void _matchNull() throws IOException {
/* 2099 */     int ptr = this._inputPtr;
/* 2100 */     if (ptr + 3 < this._inputEnd) {
/* 2101 */       char[] b = this._inputBuffer;
/* 2102 */       if ((b[ptr] == 'u') && (b[(++ptr)] == 'l') && (b[(++ptr)] == 'l')) {
/* 2103 */         char c = b[(++ptr)];
/* 2104 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2105 */           this._inputPtr = ptr;
/* 2106 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2111 */     _matchToken("null", 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final void _matchToken(String matchStr, int i)
/*      */     throws IOException
/*      */   {
/* 2119 */     int len = matchStr.length();
/*      */     do
/*      */     {
/* 2122 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2123 */         (!loadMore())) {
/* 2124 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/*      */       
/* 2127 */       if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
/* 2128 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2130 */       this._inputPtr += 1;
/* 2131 */       i++; } while (i < len);
/*      */     
/*      */ 
/* 2134 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2135 */       (!loadMore())) {
/* 2136 */       return;
/*      */     }
/*      */     
/* 2139 */     char c = this._inputBuffer[this._inputPtr];
/* 2140 */     if ((c < '0') || (c == ']') || (c == '}')) {
/* 2141 */       return;
/*      */     }
/*      */     
/* 2144 */     if (Character.isJavaIdentifierPart(c)) {
/* 2145 */       _reportInvalidToken(matchStr.substring(0, i));
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
/*      */   protected byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/* 2163 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2170 */       if (this._inputPtr >= this._inputEnd) {
/* 2171 */         loadMoreGuaranteed();
/*      */       }
/* 2173 */       char ch = this._inputBuffer[(this._inputPtr++)];
/* 2174 */       if (ch > ' ') {
/* 2175 */         int bits = b64variant.decodeBase64Char(ch);
/* 2176 */         if (bits < 0) {
/* 2177 */           if (ch == '"') {
/* 2178 */             return builder.toByteArray();
/*      */           }
/* 2180 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/* 2181 */           if (bits < 0) {}
/*      */         }
/*      */         else
/*      */         {
/* 2185 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/* 2189 */           if (this._inputPtr >= this._inputEnd) {
/* 2190 */             loadMoreGuaranteed();
/*      */           }
/* 2192 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2193 */           bits = b64variant.decodeBase64Char(ch);
/* 2194 */           if (bits < 0) {
/* 2195 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/* 2197 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/* 2200 */           if (this._inputPtr >= this._inputEnd) {
/* 2201 */             loadMoreGuaranteed();
/*      */           }
/* 2203 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2204 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/* 2207 */           if (bits < 0) {
/* 2208 */             if (bits != -2)
/*      */             {
/* 2210 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/* 2211 */                 decodedData >>= 4;
/* 2212 */                 builder.append(decodedData);
/* 2213 */                 return builder.toByteArray();
/*      */               }
/* 2215 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/* 2217 */             if (bits == -2)
/*      */             {
/* 2219 */               if (this._inputPtr >= this._inputEnd) {
/* 2220 */                 loadMoreGuaranteed();
/*      */               }
/* 2222 */               ch = this._inputBuffer[(this._inputPtr++)];
/* 2223 */               if (!b64variant.usesPaddingChar(ch)) {
/* 2224 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/* 2227 */               decodedData >>= 4;
/* 2228 */               builder.append(decodedData);
/* 2229 */               continue;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2234 */           decodedData = decodedData << 6 | bits;
/*      */           
/* 2236 */           if (this._inputPtr >= this._inputEnd) {
/* 2237 */             loadMoreGuaranteed();
/*      */           }
/* 2239 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2240 */           bits = b64variant.decodeBase64Char(ch);
/* 2241 */           if (bits < 0) {
/* 2242 */             if (bits != -2)
/*      */             {
/* 2244 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/* 2245 */                 decodedData >>= 2;
/* 2246 */                 builder.appendTwoBytes(decodedData);
/* 2247 */                 return builder.toByteArray();
/*      */               }
/* 2249 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/* 2251 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2257 */               decodedData >>= 2;
/* 2258 */               builder.appendTwoBytes(decodedData);
/* 2259 */               continue;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2264 */           decodedData = decodedData << 6 | bits;
/* 2265 */           builder.appendThreeBytes(decodedData);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void _reportInvalidToken(String matchedPart)
/*      */     throws IOException
/*      */   {
/* 2276 */     _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
/*      */   }
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart, String msg) throws IOException
/*      */   {
/* 2281 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2287 */     while ((this._inputPtr < this._inputEnd) || 
/* 2288 */       (loadMore()))
/*      */     {
/*      */ 
/*      */ 
/* 2292 */       char c = this._inputBuffer[this._inputPtr];
/* 2293 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 2296 */       this._inputPtr += 1;
/* 2297 */       sb.append(c);
/*      */     }
/* 2299 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\ReaderBasedJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */