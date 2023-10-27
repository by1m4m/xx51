/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.base.ParserBase;
/*      */ import com.fasterxml.jackson.core.io.CharTypes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.sym.Name;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ 
/*      */ public class UTF8StreamJsonParser
/*      */   extends ParserBase
/*      */ {
/*      */   static final byte BYTE_LF = 10;
/*   27 */   private static final int[] _icUTF8 = ;
/*      */   
/*      */ 
/*      */ 
/*   31 */   protected static final int[] _icLatin1 = CharTypes.getInputCodeLatin1();
/*      */   
/*      */ 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final BytesToNameCanonicalizer _symbols;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   60 */   protected int[] _quadBuffer = new int[16];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   67 */   protected boolean _tokenIncomplete = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int _quad1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputStream _inputStream;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _inputBuffer;
/*      */   
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
/*      */ 
/*      */ 
/*      */   public UTF8StreamJsonParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, BytesToNameCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*      */   {
/*  115 */     super(ctxt, features);
/*  116 */     this._inputStream = in;
/*  117 */     this._objectCodec = codec;
/*  118 */     this._symbols = sym;
/*  119 */     this._inputBuffer = inputBuffer;
/*  120 */     this._inputPtr = start;
/*  121 */     this._inputEnd = end;
/*  122 */     this._currInputRowStart = start;
/*      */     
/*  124 */     this._currInputProcessed = (-start);
/*  125 */     this._bufferRecyclable = bufferRecyclable;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec()
/*      */   {
/*  130 */     return this._objectCodec;
/*      */   }
/*      */   
/*      */   public void setCodec(ObjectCodec c)
/*      */   {
/*  135 */     this._objectCodec = c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int releaseBuffered(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  147 */     int count = this._inputEnd - this._inputPtr;
/*  148 */     if (count < 1) {
/*  149 */       return 0;
/*      */     }
/*      */     
/*  152 */     int origPtr = this._inputPtr;
/*  153 */     out.write(this._inputBuffer, origPtr, count);
/*  154 */     return count;
/*      */   }
/*      */   
/*      */   public Object getInputSource()
/*      */   {
/*  159 */     return this._inputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean loadMore()
/*      */     throws IOException
/*      */   {
/*  171 */     this._currInputProcessed += this._inputEnd;
/*  172 */     this._currInputRowStart -= this._inputEnd;
/*      */     
/*  174 */     if (this._inputStream != null) {
/*  175 */       int space = this._inputBuffer.length;
/*  176 */       if (space == 0) {
/*  177 */         return false;
/*      */       }
/*      */       
/*  180 */       int count = this._inputStream.read(this._inputBuffer, 0, space);
/*  181 */       if (count > 0) {
/*  182 */         this._inputPtr = 0;
/*  183 */         this._inputEnd = count;
/*  184 */         return true;
/*      */       }
/*      */       
/*  187 */       _closeInput();
/*      */       
/*  189 */       if (count == 0) {
/*  190 */         throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
/*      */       }
/*      */     }
/*  193 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _loadToHaveAtLeast(int minAvailable)
/*      */     throws IOException
/*      */   {
/*  203 */     if (this._inputStream == null) {
/*  204 */       return false;
/*      */     }
/*      */     
/*  207 */     int amount = this._inputEnd - this._inputPtr;
/*  208 */     if ((amount > 0) && (this._inputPtr > 0)) {
/*  209 */       this._currInputProcessed += this._inputPtr;
/*  210 */       this._currInputRowStart -= this._inputPtr;
/*  211 */       System.arraycopy(this._inputBuffer, this._inputPtr, this._inputBuffer, 0, amount);
/*  212 */       this._inputEnd = amount;
/*      */     } else {
/*  214 */       this._inputEnd = 0;
/*      */     }
/*  216 */     this._inputPtr = 0;
/*  217 */     while (this._inputEnd < minAvailable) {
/*  218 */       int count = this._inputStream.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
/*  219 */       if (count < 1)
/*      */       {
/*  221 */         _closeInput();
/*      */         
/*  223 */         if (count == 0) {
/*  224 */           throw new IOException("InputStream.read() returned 0 characters when trying to read " + amount + " bytes");
/*      */         }
/*  226 */         return false;
/*      */       }
/*  228 */       this._inputEnd += count;
/*      */     }
/*  230 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _closeInput()
/*      */     throws IOException
/*      */   {
/*  240 */     if (this._inputStream != null) {
/*  241 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/*  242 */         this._inputStream.close();
/*      */       }
/*  244 */       this._inputStream = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */     throws IOException
/*      */   {
/*  257 */     super._releaseBuffers();
/*      */     
/*  259 */     this._symbols.release();
/*  260 */     if (this._bufferRecyclable) {
/*  261 */       byte[] buf = this._inputBuffer;
/*  262 */       if (buf != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  267 */         this._inputBuffer = ByteArrayBuilder.NO_BYTES;
/*  268 */         this._ioContext.releaseReadIOBuffer(buf);
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
/*      */   public String getText()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  283 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  284 */       if (this._tokenIncomplete) {
/*  285 */         this._tokenIncomplete = false;
/*  286 */         _finishString();
/*      */       }
/*  288 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  290 */     return _getText2(this._currToken);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getValueAsString()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  299 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  300 */       if (this._tokenIncomplete) {
/*  301 */         this._tokenIncomplete = false;
/*  302 */         _finishString();
/*      */       }
/*  304 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  306 */     return super.getValueAsString(null);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getValueAsString(String defValue)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  313 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  314 */       if (this._tokenIncomplete) {
/*  315 */         this._tokenIncomplete = false;
/*  316 */         _finishString();
/*      */       }
/*  318 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  320 */     return super.getValueAsString(defValue);
/*      */   }
/*      */   
/*      */   protected final String _getText2(JsonToken t)
/*      */   {
/*  325 */     if (t == null) {
/*  326 */       return null;
/*      */     }
/*  328 */     switch (t.id()) {
/*      */     case 5: 
/*  330 */       return this._parsingContext.getCurrentName();
/*      */     
/*      */ 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*  336 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  338 */     return t.asString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public char[] getTextCharacters()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  346 */     if (this._currToken != null) {
/*  347 */       switch (this._currToken.id())
/*      */       {
/*      */       case 5: 
/*  350 */         if (!this._nameCopied) {
/*  351 */           String name = this._parsingContext.getCurrentName();
/*  352 */           int nameLen = name.length();
/*  353 */           if (this._nameCopyBuffer == null) {
/*  354 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  355 */           } else if (this._nameCopyBuffer.length < nameLen) {
/*  356 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  358 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  359 */           this._nameCopied = true;
/*      */         }
/*  361 */         return this._nameCopyBuffer;
/*      */       
/*      */       case 6: 
/*  364 */         if (this._tokenIncomplete) {
/*  365 */           this._tokenIncomplete = false;
/*  366 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  371 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*      */       
/*  374 */       return this._currToken.asCharArray();
/*      */     }
/*      */     
/*  377 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getTextLength()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  384 */     if (this._currToken != null) {
/*  385 */       switch (this._currToken.id())
/*      */       {
/*      */       case 5: 
/*  388 */         return this._parsingContext.getCurrentName().length();
/*      */       case 6: 
/*  390 */         if (this._tokenIncomplete) {
/*  391 */           this._tokenIncomplete = false;
/*  392 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  397 */         return this._textBuffer.size();
/*      */       }
/*      */       
/*  400 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */     
/*  403 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getTextOffset()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  410 */     if (this._currToken != null) {
/*  411 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  413 */         return 0;
/*      */       case 6: 
/*  415 */         if (this._tokenIncomplete) {
/*  416 */           this._tokenIncomplete = false;
/*  417 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  422 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */       
/*      */     }
/*  426 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  433 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  435 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  440 */     if (this._tokenIncomplete) {
/*      */       try {
/*  442 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  444 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  449 */       this._tokenIncomplete = false;
/*      */     }
/*  451 */     else if (this._binaryValue == null)
/*      */     {
/*  453 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  454 */       _decodeBase64(getText(), builder, b64variant);
/*  455 */       this._binaryValue = builder.toByteArray();
/*      */     }
/*      */     
/*  458 */     return this._binaryValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  466 */     if ((!this._tokenIncomplete) || (this._currToken != JsonToken.VALUE_STRING)) {
/*  467 */       byte[] b = getBinaryValue(b64variant);
/*  468 */       out.write(b);
/*  469 */       return b.length;
/*      */     }
/*      */     
/*  472 */     byte[] buf = this._ioContext.allocBase64Buffer();
/*      */     try {
/*  474 */       return _readBinary(b64variant, out, buf);
/*      */     } finally {
/*  476 */       this._ioContext.releaseBase64Buffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _readBinary(Base64Variant b64variant, OutputStream out, byte[] buffer)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  484 */     int outputPtr = 0;
/*  485 */     int outputEnd = buffer.length - 3;
/*  486 */     int outputCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  492 */       if (this._inputPtr >= this._inputEnd) {
/*  493 */         loadMoreGuaranteed();
/*      */       }
/*  495 */       int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  496 */       if (ch > 32) {
/*  497 */         int bits = b64variant.decodeBase64Char(ch);
/*  498 */         if (bits < 0) {
/*  499 */           if (ch == 34) {
/*      */             break;
/*      */           }
/*  502 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/*  503 */           if (bits < 0) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  509 */           if (outputPtr > outputEnd) {
/*  510 */             outputCount += outputPtr;
/*  511 */             out.write(buffer, 0, outputPtr);
/*  512 */             outputPtr = 0;
/*      */           }
/*      */           
/*  515 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/*  519 */           if (this._inputPtr >= this._inputEnd) {
/*  520 */             loadMoreGuaranteed();
/*      */           }
/*  522 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  523 */           bits = b64variant.decodeBase64Char(ch);
/*  524 */           if (bits < 0) {
/*  525 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/*  527 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/*  530 */           if (this._inputPtr >= this._inputEnd) {
/*  531 */             loadMoreGuaranteed();
/*      */           }
/*  533 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  534 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/*  537 */           if (bits < 0) {
/*  538 */             if (bits != -2)
/*      */             {
/*  540 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/*  541 */                 decodedData >>= 4;
/*  542 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  543 */                 break;
/*      */               }
/*  545 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/*  547 */             if (bits == -2)
/*      */             {
/*  549 */               if (this._inputPtr >= this._inputEnd) {
/*  550 */                 loadMoreGuaranteed();
/*      */               }
/*  552 */               ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  553 */               if (!b64variant.usesPaddingChar(ch)) {
/*  554 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/*  557 */               decodedData >>= 4;
/*  558 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  559 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  563 */           decodedData = decodedData << 6 | bits;
/*      */           
/*  565 */           if (this._inputPtr >= this._inputEnd) {
/*  566 */             loadMoreGuaranteed();
/*      */           }
/*  568 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  569 */           bits = b64variant.decodeBase64Char(ch);
/*  570 */           if (bits < 0) {
/*  571 */             if (bits != -2)
/*      */             {
/*  573 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/*  574 */                 decodedData >>= 2;
/*  575 */                 buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  576 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  577 */                 break;
/*      */               }
/*  579 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/*  581 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  588 */               decodedData >>= 2;
/*  589 */               buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  590 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  591 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  595 */           decodedData = decodedData << 6 | bits;
/*  596 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 16));
/*  597 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  598 */           buffer[(outputPtr++)] = ((byte)decodedData);
/*      */         } } }
/*  600 */     this._tokenIncomplete = false;
/*  601 */     if (outputPtr > 0) {
/*  602 */       outputCount += outputPtr;
/*  603 */       out.write(buffer, 0, outputPtr);
/*      */     }
/*  605 */     return outputCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonLocation getTokenLocation()
/*      */   {
/*  612 */     return new JsonLocation(this._ioContext.getSourceReference(), getTokenCharacterOffset(), -1L, getTokenLineNr(), getTokenColumnNr());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getCurrentLocation()
/*      */   {
/*  622 */     int col = this._inputPtr - this._currInputRowStart + 1;
/*  623 */     return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + this._inputPtr, -1L, this._currInputRow, col);
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
/*      */   public JsonToken nextToken()
/*      */     throws IOException
/*      */   {
/*  641 */     this._numTypesValid = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  646 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  647 */       return _nextAfterName();
/*      */     }
/*  649 */     if (this._tokenIncomplete) {
/*  650 */       _skipString();
/*      */     }
/*  652 */     int i = _skipWSOrEnd();
/*  653 */     if (i < 0)
/*      */     {
/*  655 */       close();
/*  656 */       return this._currToken = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  661 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  662 */     this._tokenInputRow = this._currInputRow;
/*  663 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */     
/*      */ 
/*  666 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  669 */     if (i == 93) {
/*  670 */       if (!this._parsingContext.inArray()) {
/*  671 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  673 */       this._parsingContext = this._parsingContext.getParent();
/*  674 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  676 */     if (i == 125) {
/*  677 */       if (!this._parsingContext.inObject()) {
/*  678 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  680 */       this._parsingContext = this._parsingContext.getParent();
/*  681 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */     
/*      */ 
/*  685 */     if (this._parsingContext.expectComma()) {
/*  686 */       if (i != 44) {
/*  687 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */       }
/*  689 */       i = _skipWS();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  696 */     if (!this._parsingContext.inObject()) {
/*  697 */       return _nextTokenNotInObject(i);
/*      */     }
/*      */     
/*  700 */     Name n = _parseName(i);
/*  701 */     this._parsingContext.setCurrentName(n.getName());
/*  702 */     this._currToken = JsonToken.FIELD_NAME;
/*      */     
/*  704 */     i = _skipColon();
/*      */     
/*      */ 
/*  707 */     if (i == 34) {
/*  708 */       this._tokenIncomplete = true;
/*  709 */       this._nextToken = JsonToken.VALUE_STRING;
/*  710 */       return this._currToken;
/*      */     }
/*      */     
/*      */     JsonToken t;
/*  714 */     switch (i) {
/*      */     case 45: 
/*  716 */       t = _parseNegNumber();
/*  717 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  733 */       t = _parsePosNumber(i);
/*  734 */       break;
/*      */     case 102: 
/*  736 */       _matchToken("false", 1);
/*  737 */       t = JsonToken.VALUE_FALSE;
/*  738 */       break;
/*      */     case 110: 
/*  740 */       _matchToken("null", 1);
/*  741 */       t = JsonToken.VALUE_NULL;
/*  742 */       break;
/*      */     case 116: 
/*  744 */       _matchToken("true", 1);
/*  745 */       t = JsonToken.VALUE_TRUE;
/*  746 */       break;
/*      */     case 91: 
/*  748 */       t = JsonToken.START_ARRAY;
/*  749 */       break;
/*      */     case 123: 
/*  751 */       t = JsonToken.START_OBJECT;
/*  752 */       break;
/*      */     
/*      */     default: 
/*  755 */       t = _handleUnexpectedValue(i);
/*      */     }
/*  757 */     this._nextToken = t;
/*  758 */     return this._currToken;
/*      */   }
/*      */   
/*      */   private final JsonToken _nextTokenNotInObject(int i) throws IOException
/*      */   {
/*  763 */     if (i == 34) {
/*  764 */       this._tokenIncomplete = true;
/*  765 */       return this._currToken = JsonToken.VALUE_STRING;
/*      */     }
/*  767 */     switch (i) {
/*      */     case 91: 
/*  769 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  770 */       return this._currToken = JsonToken.START_ARRAY;
/*      */     case 123: 
/*  772 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*  773 */       return this._currToken = JsonToken.START_OBJECT;
/*      */     case 116: 
/*  775 */       _matchToken("true", 1);
/*  776 */       return this._currToken = JsonToken.VALUE_TRUE;
/*      */     case 102: 
/*  778 */       _matchToken("false", 1);
/*  779 */       return this._currToken = JsonToken.VALUE_FALSE;
/*      */     case 110: 
/*  781 */       _matchToken("null", 1);
/*  782 */       return this._currToken = JsonToken.VALUE_NULL;
/*      */     case 45: 
/*  784 */       return this._currToken = _parseNegNumber();
/*      */     
/*      */ 
/*      */ 
/*      */ 
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
/*  799 */       return this._currToken = _parsePosNumber(i);
/*      */     }
/*  801 */     return this._currToken = _handleUnexpectedValue(i);
/*      */   }
/*      */   
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  806 */     this._nameCopied = false;
/*  807 */     JsonToken t = this._nextToken;
/*  808 */     this._nextToken = null;
/*      */     
/*  810 */     if (t == JsonToken.START_ARRAY) {
/*  811 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  812 */     } else if (t == JsonToken.START_OBJECT) {
/*  813 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  815 */     return this._currToken = t;
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
/*      */   public boolean nextFieldName(SerializableString str)
/*      */     throws IOException
/*      */   {
/*  829 */     this._numTypesValid = 0;
/*  830 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  831 */       _nextAfterName();
/*  832 */       return false;
/*      */     }
/*  834 */     if (this._tokenIncomplete) {
/*  835 */       _skipString();
/*      */     }
/*  837 */     int i = _skipWSOrEnd();
/*  838 */     if (i < 0) {
/*  839 */       close();
/*  840 */       this._currToken = null;
/*  841 */       return false;
/*      */     }
/*  843 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  844 */     this._tokenInputRow = this._currInputRow;
/*  845 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */     
/*      */ 
/*  848 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  851 */     if (i == 93) {
/*  852 */       if (!this._parsingContext.inArray()) {
/*  853 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  855 */       this._parsingContext = this._parsingContext.getParent();
/*  856 */       this._currToken = JsonToken.END_ARRAY;
/*  857 */       return false;
/*      */     }
/*  859 */     if (i == 125) {
/*  860 */       if (!this._parsingContext.inObject()) {
/*  861 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  863 */       this._parsingContext = this._parsingContext.getParent();
/*  864 */       this._currToken = JsonToken.END_OBJECT;
/*  865 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  869 */     if (this._parsingContext.expectComma()) {
/*  870 */       if (i != 44) {
/*  871 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */       }
/*  873 */       i = _skipWS();
/*      */     }
/*      */     
/*  876 */     if (!this._parsingContext.inObject()) {
/*  877 */       _nextTokenNotInObject(i);
/*  878 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  882 */     if (i == 34)
/*      */     {
/*  884 */       byte[] nameBytes = str.asQuotedUTF8();
/*  885 */       int len = nameBytes.length;
/*      */       
/*      */ 
/*  888 */       if (this._inputPtr + len + 4 < this._inputEnd)
/*      */       {
/*  890 */         int end = this._inputPtr + len;
/*  891 */         if (this._inputBuffer[end] == 34) {
/*  892 */           int offset = 0;
/*  893 */           int ptr = this._inputPtr;
/*      */           for (;;) {
/*  895 */             if (ptr == end) {
/*  896 */               this._parsingContext.setCurrentName(str.getValue());
/*  897 */               _isNextTokenNameYes(_skipColonFast(ptr + 1));
/*  898 */               return true;
/*      */             }
/*  900 */             if (nameBytes[offset] != this._inputBuffer[ptr]) {
/*      */               break;
/*      */             }
/*  903 */             offset++;
/*  904 */             ptr++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  909 */     return _isNextTokenNameMaybe(i, str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String nextFieldName()
/*      */     throws IOException
/*      */   {
/*  917 */     this._numTypesValid = 0;
/*  918 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  919 */       _nextAfterName();
/*  920 */       return null;
/*      */     }
/*  922 */     if (this._tokenIncomplete) {
/*  923 */       _skipString();
/*      */     }
/*  925 */     int i = _skipWSOrEnd();
/*  926 */     if (i < 0) {
/*  927 */       close();
/*  928 */       this._currToken = null;
/*  929 */       return null;
/*      */     }
/*  931 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  932 */     this._tokenInputRow = this._currInputRow;
/*  933 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */     
/*  935 */     this._binaryValue = null;
/*      */     
/*  937 */     if (i == 93) {
/*  938 */       if (!this._parsingContext.inArray()) {
/*  939 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  941 */       this._parsingContext = this._parsingContext.getParent();
/*  942 */       this._currToken = JsonToken.END_ARRAY;
/*  943 */       return null;
/*      */     }
/*  945 */     if (i == 125) {
/*  946 */       if (!this._parsingContext.inObject()) {
/*  947 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  949 */       this._parsingContext = this._parsingContext.getParent();
/*  950 */       this._currToken = JsonToken.END_OBJECT;
/*  951 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  955 */     if (this._parsingContext.expectComma()) {
/*  956 */       if (i != 44) {
/*  957 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */       }
/*  959 */       i = _skipWS();
/*      */     }
/*      */     
/*  962 */     if (!this._parsingContext.inObject()) {
/*  963 */       _nextTokenNotInObject(i);
/*  964 */       return null;
/*      */     }
/*      */     
/*  967 */     Name n = _parseName(i);
/*  968 */     String nameStr = n.getName();
/*  969 */     this._parsingContext.setCurrentName(nameStr);
/*  970 */     this._currToken = JsonToken.FIELD_NAME;
/*      */     
/*  972 */     i = _skipColon();
/*  973 */     if (i == 34) {
/*  974 */       this._tokenIncomplete = true;
/*  975 */       this._nextToken = JsonToken.VALUE_STRING;
/*  976 */       return nameStr;
/*      */     }
/*      */     JsonToken t;
/*  979 */     switch (i) {
/*      */     case 45: 
/*  981 */       t = _parseNegNumber();
/*  982 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  998 */       t = _parsePosNumber(i);
/*  999 */       break;
/*      */     case 102: 
/* 1001 */       _matchToken("false", 1);
/* 1002 */       t = JsonToken.VALUE_FALSE;
/* 1003 */       break;
/*      */     case 110: 
/* 1005 */       _matchToken("null", 1);
/* 1006 */       t = JsonToken.VALUE_NULL;
/* 1007 */       break;
/*      */     case 116: 
/* 1009 */       _matchToken("true", 1);
/* 1010 */       t = JsonToken.VALUE_TRUE;
/* 1011 */       break;
/*      */     case 91: 
/* 1013 */       t = JsonToken.START_ARRAY;
/* 1014 */       break;
/*      */     case 123: 
/* 1016 */       t = JsonToken.START_OBJECT;
/* 1017 */       break;
/*      */     
/*      */     default: 
/* 1020 */       t = _handleUnexpectedValue(i);
/*      */     }
/* 1022 */     this._nextToken = t;
/* 1023 */     return nameStr;
/*      */   }
/*      */   
/*      */   private final int _skipColonFast(int ptr)
/*      */     throws IOException
/*      */   {
/* 1029 */     int i = this._inputBuffer[(ptr++)];
/* 1030 */     if (i == 58) {
/* 1031 */       i = this._inputBuffer[(ptr++)];
/* 1032 */       if (i > 32) {
/* 1033 */         if ((i != 47) && (i != 35)) {
/* 1034 */           this._inputPtr = ptr;
/* 1035 */           return i;
/*      */         }
/* 1037 */       } else if ((i == 32) || (i == 9)) {
/* 1038 */         i = this._inputBuffer[(ptr++)];
/* 1039 */         if ((i > 32) && 
/* 1040 */           (i != 47) && (i != 35)) {
/* 1041 */           this._inputPtr = ptr;
/* 1042 */           return i;
/*      */         }
/*      */       }
/*      */       
/* 1046 */       this._inputPtr = (ptr - 1);
/* 1047 */       return _skipColon2(true);
/*      */     }
/* 1049 */     if ((i == 32) || (i == 9)) {
/* 1050 */       i = this._inputBuffer[(ptr++)];
/*      */     }
/* 1052 */     if (i == 58) {
/* 1053 */       i = this._inputBuffer[(ptr++)];
/* 1054 */       if (i > 32) {
/* 1055 */         if ((i != 47) && (i != 35)) {
/* 1056 */           this._inputPtr = ptr;
/* 1057 */           return i;
/*      */         }
/* 1059 */       } else if ((i == 32) || (i == 9)) {
/* 1060 */         i = this._inputBuffer[(ptr++)];
/* 1061 */         if ((i > 32) && 
/* 1062 */           (i != 47) && (i != 35)) {
/* 1063 */           this._inputPtr = ptr;
/* 1064 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1069 */     this._inputPtr = (ptr - 1);
/* 1070 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final void _isNextTokenNameYes(int i) throws IOException
/*      */   {
/* 1075 */     this._currToken = JsonToken.FIELD_NAME;
/*      */     
/* 1077 */     switch (i) {
/*      */     case 34: 
/* 1079 */       this._tokenIncomplete = true;
/* 1080 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1081 */       return;
/*      */     case 91: 
/* 1083 */       this._nextToken = JsonToken.START_ARRAY;
/* 1084 */       return;
/*      */     case 123: 
/* 1086 */       this._nextToken = JsonToken.START_OBJECT;
/* 1087 */       return;
/*      */     case 116: 
/* 1089 */       _matchToken("true", 1);
/* 1090 */       this._nextToken = JsonToken.VALUE_TRUE;
/* 1091 */       return;
/*      */     case 102: 
/* 1093 */       _matchToken("false", 1);
/* 1094 */       this._nextToken = JsonToken.VALUE_FALSE;
/* 1095 */       return;
/*      */     case 110: 
/* 1097 */       _matchToken("null", 1);
/* 1098 */       this._nextToken = JsonToken.VALUE_NULL;
/* 1099 */       return;
/*      */     case 45: 
/* 1101 */       this._nextToken = _parseNegNumber();
/* 1102 */       return;
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
/* 1113 */       this._nextToken = _parsePosNumber(i);
/* 1114 */       return;
/*      */     }
/* 1116 */     this._nextToken = _handleUnexpectedValue(i);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean _isNextTokenNameMaybe(int i, SerializableString str)
/*      */     throws IOException
/*      */   {
/* 1124 */     Name n = _parseName(i);
/*      */     
/*      */ 
/* 1127 */     String nameStr = n.getName();
/* 1128 */     this._parsingContext.setCurrentName(nameStr);
/* 1129 */     boolean match = nameStr.equals(str.getValue());
/*      */     
/* 1131 */     this._currToken = JsonToken.FIELD_NAME;
/* 1132 */     i = _skipColon();
/*      */     
/*      */ 
/* 1135 */     if (i == 34) {
/* 1136 */       this._tokenIncomplete = true;
/* 1137 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1138 */       return match;
/*      */     }
/*      */     
/*      */     JsonToken t;
/* 1142 */     switch (i) {
/*      */     case 91: 
/* 1144 */       t = JsonToken.START_ARRAY;
/* 1145 */       break;
/*      */     case 123: 
/* 1147 */       t = JsonToken.START_OBJECT;
/* 1148 */       break;
/*      */     case 116: 
/* 1150 */       _matchToken("true", 1);
/* 1151 */       t = JsonToken.VALUE_TRUE;
/* 1152 */       break;
/*      */     case 102: 
/* 1154 */       _matchToken("false", 1);
/* 1155 */       t = JsonToken.VALUE_FALSE;
/* 1156 */       break;
/*      */     case 110: 
/* 1158 */       _matchToken("null", 1);
/* 1159 */       t = JsonToken.VALUE_NULL;
/* 1160 */       break;
/*      */     case 45: 
/* 1162 */       t = _parseNegNumber();
/* 1163 */       break;
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
/* 1174 */       t = _parsePosNumber(i);
/* 1175 */       break;
/*      */     default: 
/* 1177 */       t = _handleUnexpectedValue(i);
/*      */     }
/* 1179 */     this._nextToken = t;
/* 1180 */     return match;
/*      */   }
/*      */   
/*      */ 
/*      */   public String nextTextValue()
/*      */     throws IOException
/*      */   {
/* 1187 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1188 */       this._nameCopied = false;
/* 1189 */       JsonToken t = this._nextToken;
/* 1190 */       this._nextToken = null;
/* 1191 */       this._currToken = t;
/* 1192 */       if (t == JsonToken.VALUE_STRING) {
/* 1193 */         if (this._tokenIncomplete) {
/* 1194 */           this._tokenIncomplete = false;
/* 1195 */           _finishString();
/*      */         }
/* 1197 */         return this._textBuffer.contentsAsString();
/*      */       }
/* 1199 */       if (t == JsonToken.START_ARRAY) {
/* 1200 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1201 */       } else if (t == JsonToken.START_OBJECT) {
/* 1202 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1204 */       return null;
/*      */     }
/*      */     
/* 1207 */     return nextToken() == JsonToken.VALUE_STRING ? getText() : null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int nextIntValue(int defaultValue)
/*      */     throws IOException
/*      */   {
/* 1214 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1215 */       this._nameCopied = false;
/* 1216 */       JsonToken t = this._nextToken;
/* 1217 */       this._nextToken = null;
/* 1218 */       this._currToken = t;
/* 1219 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1220 */         return getIntValue();
/*      */       }
/* 1222 */       if (t == JsonToken.START_ARRAY) {
/* 1223 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1224 */       } else if (t == JsonToken.START_OBJECT) {
/* 1225 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1227 */       return defaultValue;
/*      */     }
/*      */     
/* 1230 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public long nextLongValue(long defaultValue)
/*      */     throws IOException
/*      */   {
/* 1237 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1238 */       this._nameCopied = false;
/* 1239 */       JsonToken t = this._nextToken;
/* 1240 */       this._nextToken = null;
/* 1241 */       this._currToken = t;
/* 1242 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1243 */         return getLongValue();
/*      */       }
/* 1245 */       if (t == JsonToken.START_ARRAY) {
/* 1246 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1247 */       } else if (t == JsonToken.START_OBJECT) {
/* 1248 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1250 */       return defaultValue;
/*      */     }
/*      */     
/* 1253 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public Boolean nextBooleanValue()
/*      */     throws IOException
/*      */   {
/* 1260 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1261 */       this._nameCopied = false;
/* 1262 */       JsonToken t = this._nextToken;
/* 1263 */       this._nextToken = null;
/* 1264 */       this._currToken = t;
/* 1265 */       if (t == JsonToken.VALUE_TRUE) {
/* 1266 */         return Boolean.TRUE;
/*      */       }
/* 1268 */       if (t == JsonToken.VALUE_FALSE) {
/* 1269 */         return Boolean.FALSE;
/*      */       }
/* 1271 */       if (t == JsonToken.START_ARRAY) {
/* 1272 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1273 */       } else if (t == JsonToken.START_OBJECT) {
/* 1274 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1276 */       return null;
/*      */     }
/*      */     
/* 1279 */     switch (nextToken().id()) {
/*      */     case 9: 
/* 1281 */       return Boolean.TRUE;
/*      */     case 10: 
/* 1283 */       return Boolean.FALSE;
/*      */     }
/* 1285 */     return null;
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
/*      */   protected JsonToken _parsePosNumber(int c)
/*      */     throws IOException
/*      */   {
/* 1312 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*      */     
/* 1314 */     if (c == 48) {
/* 1315 */       c = _verifyNoLeadingZeroes();
/*      */     }
/*      */     
/* 1318 */     outBuf[0] = ((char)c);
/* 1319 */     int intLen = 1;
/* 1320 */     int outPtr = 1;
/*      */     
/*      */ 
/* 1323 */     int end = this._inputPtr + outBuf.length - 1;
/* 1324 */     if (end > this._inputEnd) {
/* 1325 */       end = this._inputEnd;
/*      */     }
/*      */     for (;;)
/*      */     {
/* 1329 */       if (this._inputPtr >= end) {
/* 1330 */         return _parseNumber2(outBuf, outPtr, false, intLen);
/*      */       }
/* 1332 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1333 */       if ((c < 48) || (c > 57)) {
/*      */         break;
/*      */       }
/* 1336 */       intLen++;
/* 1337 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 1339 */     if ((c == 46) || (c == 101) || (c == 69)) {
/* 1340 */       return _parseFloat(outBuf, outPtr, c, false, intLen);
/*      */     }
/* 1342 */     this._inputPtr -= 1;
/* 1343 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1345 */     if (this._parsingContext.inRoot()) {
/* 1346 */       _verifyRootSpace(c);
/*      */     }
/*      */     
/* 1349 */     return resetInt(false, intLen);
/*      */   }
/*      */   
/*      */   protected JsonToken _parseNegNumber() throws IOException
/*      */   {
/* 1354 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1355 */     int outPtr = 0;
/*      */     
/*      */ 
/* 1358 */     outBuf[(outPtr++)] = '-';
/*      */     
/* 1360 */     if (this._inputPtr >= this._inputEnd) {
/* 1361 */       loadMoreGuaranteed();
/*      */     }
/* 1363 */     int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     
/* 1365 */     if ((c < 48) || (c > 57)) {
/* 1366 */       return _handleInvalidNumberStart(c, true);
/*      */     }
/*      */     
/*      */ 
/* 1370 */     if (c == 48) {
/* 1371 */       c = _verifyNoLeadingZeroes();
/*      */     }
/*      */     
/*      */ 
/* 1375 */     outBuf[(outPtr++)] = ((char)c);
/* 1376 */     int intLen = 1;
/*      */     
/*      */ 
/*      */ 
/* 1380 */     int end = this._inputPtr + outBuf.length - outPtr;
/* 1381 */     if (end > this._inputEnd) {
/* 1382 */       end = this._inputEnd;
/*      */     }
/*      */     
/*      */     for (;;)
/*      */     {
/* 1387 */       if (this._inputPtr >= end)
/*      */       {
/* 1389 */         return _parseNumber2(outBuf, outPtr, true, intLen);
/*      */       }
/* 1391 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1392 */       if ((c < 48) || (c > 57)) {
/*      */         break;
/*      */       }
/* 1395 */       intLen++;
/* 1396 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 1398 */     if ((c == 46) || (c == 101) || (c == 69)) {
/* 1399 */       return _parseFloat(outBuf, outPtr, c, true, intLen);
/*      */     }
/*      */     
/* 1402 */     this._inputPtr -= 1;
/* 1403 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1405 */     if (this._parsingContext.inRoot()) {
/* 1406 */       _verifyRootSpace(c);
/*      */     }
/*      */     
/*      */ 
/* 1410 */     return resetInt(true, intLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final JsonToken _parseNumber2(char[] outBuf, int outPtr, boolean negative, int intPartLength)
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1422 */       if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1423 */         this._textBuffer.setCurrentLength(outPtr);
/* 1424 */         return resetInt(negative, intPartLength);
/*      */       }
/* 1426 */       int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1427 */       if ((c > 57) || (c < 48)) {
/* 1428 */         if ((c != 46) && (c != 101) && (c != 69)) break;
/* 1429 */         return _parseFloat(outBuf, outPtr, c, negative, intPartLength);
/*      */       }
/*      */       
/*      */ 
/* 1433 */       if (outPtr >= outBuf.length) {
/* 1434 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1435 */         outPtr = 0;
/*      */       }
/* 1437 */       outBuf[(outPtr++)] = ((char)c);
/* 1438 */       intPartLength++;
/*      */     }
/* 1440 */     this._inputPtr -= 1;
/* 1441 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1443 */     if (this._parsingContext.inRoot()) {
/* 1444 */       _verifyRootSpace(this._inputBuffer[(this._inputPtr++)] & 0xFF);
/*      */     }
/*      */     
/*      */ 
/* 1448 */     return resetInt(negative, intPartLength);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _verifyNoLeadingZeroes()
/*      */     throws IOException
/*      */   {
/* 1459 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1460 */       return 48;
/*      */     }
/* 1462 */     int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/*      */     
/* 1464 */     if ((ch < 48) || (ch > 57)) {
/* 1465 */       return 48;
/*      */     }
/*      */     
/* 1468 */     if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
/* 1469 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */     }
/*      */     
/* 1472 */     this._inputPtr += 1;
/* 1473 */     if (ch == 48) {
/* 1474 */       while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1475 */         ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 1476 */         if ((ch < 48) || (ch > 57)) {
/* 1477 */           return 48;
/*      */         }
/* 1479 */         this._inputPtr += 1;
/* 1480 */         if (ch != 48) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1485 */     return ch;
/*      */   }
/*      */   
/*      */   private final JsonToken _parseFloat(char[] outBuf, int outPtr, int c, boolean negative, int integerPartLength)
/*      */     throws IOException
/*      */   {
/* 1491 */     int fractLen = 0;
/* 1492 */     boolean eof = false;
/*      */     
/*      */ 
/* 1495 */     if (c == 46) {
/* 1496 */       outBuf[(outPtr++)] = ((char)c);
/*      */       
/*      */       for (;;)
/*      */       {
/* 1500 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1501 */           eof = true;
/* 1502 */           break;
/*      */         }
/* 1504 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1505 */         if ((c < 48) || (c > 57)) {
/*      */           break;
/*      */         }
/* 1508 */         fractLen++;
/* 1509 */         if (outPtr >= outBuf.length) {
/* 1510 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1511 */           outPtr = 0;
/*      */         }
/* 1513 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/*      */       
/* 1516 */       if (fractLen == 0) {
/* 1517 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/* 1521 */     int expLen = 0;
/* 1522 */     if ((c == 101) || (c == 69)) {
/* 1523 */       if (outPtr >= outBuf.length) {
/* 1524 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1525 */         outPtr = 0;
/*      */       }
/* 1527 */       outBuf[(outPtr++)] = ((char)c);
/*      */       
/* 1529 */       if (this._inputPtr >= this._inputEnd) {
/* 1530 */         loadMoreGuaranteed();
/*      */       }
/* 1532 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       
/* 1534 */       if ((c == 45) || (c == 43)) {
/* 1535 */         if (outPtr >= outBuf.length) {
/* 1536 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1537 */           outPtr = 0;
/*      */         }
/* 1539 */         outBuf[(outPtr++)] = ((char)c);
/*      */         
/* 1541 */         if (this._inputPtr >= this._inputEnd) {
/* 1542 */           loadMoreGuaranteed();
/*      */         }
/* 1544 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */       
/*      */ 
/* 1548 */       while ((c <= 57) && (c >= 48)) {
/* 1549 */         expLen++;
/* 1550 */         if (outPtr >= outBuf.length) {
/* 1551 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1552 */           outPtr = 0;
/*      */         }
/* 1554 */         outBuf[(outPtr++)] = ((char)c);
/* 1555 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1556 */           eof = true;
/* 1557 */           break;
/*      */         }
/* 1559 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */       
/* 1562 */       if (expLen == 0) {
/* 1563 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1568 */     if (!eof) {
/* 1569 */       this._inputPtr -= 1;
/*      */       
/* 1571 */       if (this._parsingContext.inRoot()) {
/* 1572 */         _verifyRootSpace(c);
/*      */       }
/*      */     }
/* 1575 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/*      */ 
/* 1578 */     return resetFloat(negative, integerPartLength, fractLen, expLen);
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
/* 1591 */     this._inputPtr += 1;
/*      */     
/* 1593 */     switch (ch) {
/*      */     case 9: 
/*      */     case 32: 
/* 1596 */       return;
/*      */     case 13: 
/* 1598 */       _skipCR();
/* 1599 */       return;
/*      */     case 10: 
/* 1601 */       this._currInputRow += 1;
/* 1602 */       this._currInputRowStart = this._inputPtr;
/* 1603 */       return;
/*      */     }
/* 1605 */     _reportMissingRootWS(ch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Name _parseName(int i)
/*      */     throws IOException
/*      */   {
/* 1616 */     if (i != 34) {
/* 1617 */       return _handleOddName(i);
/*      */     }
/*      */     
/* 1620 */     if (this._inputPtr + 9 > this._inputEnd) {
/* 1621 */       return slowParseName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1630 */     byte[] input = this._inputBuffer;
/* 1631 */     int[] codes = _icLatin1;
/*      */     
/* 1633 */     int q = input[(this._inputPtr++)] & 0xFF;
/*      */     
/* 1635 */     if (codes[q] == 0) {
/* 1636 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1637 */       if (codes[i] == 0) {
/* 1638 */         q = q << 8 | i;
/* 1639 */         i = input[(this._inputPtr++)] & 0xFF;
/* 1640 */         if (codes[i] == 0) {
/* 1641 */           q = q << 8 | i;
/* 1642 */           i = input[(this._inputPtr++)] & 0xFF;
/* 1643 */           if (codes[i] == 0) {
/* 1644 */             q = q << 8 | i;
/* 1645 */             i = input[(this._inputPtr++)] & 0xFF;
/* 1646 */             if (codes[i] == 0) {
/* 1647 */               this._quad1 = q;
/* 1648 */               return parseMediumName(i);
/*      */             }
/* 1650 */             if (i == 34) {
/* 1651 */               return findName(q, 4);
/*      */             }
/* 1653 */             return parseName(q, i, 4);
/*      */           }
/* 1655 */           if (i == 34) {
/* 1656 */             return findName(q, 3);
/*      */           }
/* 1658 */           return parseName(q, i, 3);
/*      */         }
/* 1660 */         if (i == 34) {
/* 1661 */           return findName(q, 2);
/*      */         }
/* 1663 */         return parseName(q, i, 2);
/*      */       }
/* 1665 */       if (i == 34) {
/* 1666 */         return findName(q, 1);
/*      */       }
/* 1668 */       return parseName(q, i, 1);
/*      */     }
/* 1670 */     if (q == 34) {
/* 1671 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/* 1673 */     return parseName(0, q, 0);
/*      */   }
/*      */   
/*      */   protected final Name parseMediumName(int q2) throws IOException
/*      */   {
/* 1678 */     byte[] input = this._inputBuffer;
/* 1679 */     int[] codes = _icLatin1;
/*      */     
/*      */ 
/* 1682 */     int i = input[(this._inputPtr++)] & 0xFF;
/* 1683 */     if (codes[i] != 0) {
/* 1684 */       if (i == 34) {
/* 1685 */         return findName(this._quad1, q2, 1);
/*      */       }
/* 1687 */       return parseName(this._quad1, q2, i, 1);
/*      */     }
/* 1689 */     q2 = q2 << 8 | i;
/* 1690 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1691 */     if (codes[i] != 0) {
/* 1692 */       if (i == 34) {
/* 1693 */         return findName(this._quad1, q2, 2);
/*      */       }
/* 1695 */       return parseName(this._quad1, q2, i, 2);
/*      */     }
/* 1697 */     q2 = q2 << 8 | i;
/* 1698 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1699 */     if (codes[i] != 0) {
/* 1700 */       if (i == 34) {
/* 1701 */         return findName(this._quad1, q2, 3);
/*      */       }
/* 1703 */       return parseName(this._quad1, q2, i, 3);
/*      */     }
/* 1705 */     q2 = q2 << 8 | i;
/* 1706 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1707 */     if (codes[i] != 0) {
/* 1708 */       if (i == 34) {
/* 1709 */         return findName(this._quad1, q2, 4);
/*      */       }
/* 1711 */       return parseName(this._quad1, q2, i, 4);
/*      */     }
/* 1713 */     return parseLongName(i, q2);
/*      */   }
/*      */   
/*      */   protected final Name parseLongName(int q, int q2) throws IOException
/*      */   {
/* 1718 */     this._quadBuffer[0] = this._quad1;
/* 1719 */     this._quadBuffer[1] = q2;
/*      */     
/*      */ 
/* 1722 */     byte[] input = this._inputBuffer;
/* 1723 */     int[] codes = _icLatin1;
/* 1724 */     int qlen = 2;
/*      */     
/* 1726 */     while (this._inputPtr + 4 <= this._inputEnd) {
/* 1727 */       int i = input[(this._inputPtr++)] & 0xFF;
/* 1728 */       if (codes[i] != 0) {
/* 1729 */         if (i == 34) {
/* 1730 */           return findName(this._quadBuffer, qlen, q, 1);
/*      */         }
/* 1732 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 1);
/*      */       }
/*      */       
/* 1735 */       q = q << 8 | i;
/* 1736 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1737 */       if (codes[i] != 0) {
/* 1738 */         if (i == 34) {
/* 1739 */           return findName(this._quadBuffer, qlen, q, 2);
/*      */         }
/* 1741 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 2);
/*      */       }
/*      */       
/* 1744 */       q = q << 8 | i;
/* 1745 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1746 */       if (codes[i] != 0) {
/* 1747 */         if (i == 34) {
/* 1748 */           return findName(this._quadBuffer, qlen, q, 3);
/*      */         }
/* 1750 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 3);
/*      */       }
/*      */       
/* 1753 */       q = q << 8 | i;
/* 1754 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1755 */       if (codes[i] != 0) {
/* 1756 */         if (i == 34) {
/* 1757 */           return findName(this._quadBuffer, qlen, q, 4);
/*      */         }
/* 1759 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 4);
/*      */       }
/*      */       
/*      */ 
/* 1763 */       if (qlen >= this._quadBuffer.length) {
/* 1764 */         this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
/*      */       }
/* 1766 */       this._quadBuffer[(qlen++)] = q;
/* 1767 */       q = i;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1774 */     return parseEscapedName(this._quadBuffer, qlen, 0, q, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Name slowParseName()
/*      */     throws IOException
/*      */   {
/* 1784 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1785 */       (!loadMore())) {
/* 1786 */       _reportInvalidEOF(": was expecting closing '\"' for name");
/*      */     }
/*      */     
/* 1789 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1790 */     if (i == 34) {
/* 1791 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/* 1793 */     return parseEscapedName(this._quadBuffer, 0, 0, i, 0);
/*      */   }
/*      */   
/*      */   private final Name parseName(int q1, int ch, int lastQuadBytes) throws IOException {
/* 1797 */     return parseEscapedName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final Name parseName(int q1, int q2, int ch, int lastQuadBytes) throws IOException {
/* 1801 */     this._quadBuffer[0] = q1;
/* 1802 */     return parseEscapedName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
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
/*      */   protected final Name parseEscapedName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes)
/*      */     throws IOException
/*      */   {
/* 1819 */     int[] codes = _icLatin1;
/*      */     for (;;)
/*      */     {
/* 1822 */       if (codes[ch] != 0) {
/* 1823 */         if (ch == 34) {
/*      */           break;
/*      */         }
/*      */         
/* 1827 */         if (ch != 92)
/*      */         {
/* 1829 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/* 1832 */           ch = _decodeEscaped();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1839 */         if (ch > 127)
/*      */         {
/* 1841 */           if (currQuadBytes >= 4) {
/* 1842 */             if (qlen >= quads.length) {
/* 1843 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/* 1845 */             quads[(qlen++)] = currQuad;
/* 1846 */             currQuad = 0;
/* 1847 */             currQuadBytes = 0;
/*      */           }
/* 1849 */           if (ch < 2048) {
/* 1850 */             currQuad = currQuad << 8 | 0xC0 | ch >> 6;
/* 1851 */             currQuadBytes++;
/*      */           }
/*      */           else {
/* 1854 */             currQuad = currQuad << 8 | 0xE0 | ch >> 12;
/* 1855 */             currQuadBytes++;
/*      */             
/* 1857 */             if (currQuadBytes >= 4) {
/* 1858 */               if (qlen >= quads.length) {
/* 1859 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/* 1861 */               quads[(qlen++)] = currQuad;
/* 1862 */               currQuad = 0;
/* 1863 */               currQuadBytes = 0;
/*      */             }
/* 1865 */             currQuad = currQuad << 8 | 0x80 | ch >> 6 & 0x3F;
/* 1866 */             currQuadBytes++;
/*      */           }
/*      */           
/* 1869 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 1873 */       if (currQuadBytes < 4) {
/* 1874 */         currQuadBytes++;
/* 1875 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 1877 */         if (qlen >= quads.length) {
/* 1878 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 1880 */         quads[(qlen++)] = currQuad;
/* 1881 */         currQuad = ch;
/* 1882 */         currQuadBytes = 1;
/*      */       }
/* 1884 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1885 */         (!loadMore())) {
/* 1886 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */       
/* 1889 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/* 1891 */     if (currQuadBytes > 0) {
/* 1892 */       if (qlen >= quads.length) {
/* 1893 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 1895 */       quads[(qlen++)] = pad(currQuad, currQuadBytes);
/*      */     }
/* 1897 */     Name name = this._symbols.findName(quads, qlen);
/* 1898 */     if (name == null) {
/* 1899 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 1901 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Name _handleOddName(int ch)
/*      */     throws IOException
/*      */   {
/* 1913 */     if ((ch == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 1914 */       return _parseAposName();
/*      */     }
/*      */     
/* 1917 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/* 1918 */       char c = (char)_decodeCharForError(ch);
/* 1919 */       _reportUnexpectedChar(c, "was expecting double-quote to start field name");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1925 */     int[] codes = CharTypes.getInputCodeUtf8JsNames();
/*      */     
/* 1927 */     if (codes[ch] != 0) {
/* 1928 */       _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1935 */     int[] quads = this._quadBuffer;
/* 1936 */     int qlen = 0;
/* 1937 */     int currQuad = 0;
/* 1938 */     int currQuadBytes = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1942 */       if (currQuadBytes < 4) {
/* 1943 */         currQuadBytes++;
/* 1944 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 1946 */         if (qlen >= quads.length) {
/* 1947 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 1949 */         quads[(qlen++)] = currQuad;
/* 1950 */         currQuad = ch;
/* 1951 */         currQuadBytes = 1;
/*      */       }
/* 1953 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1954 */         (!loadMore())) {
/* 1955 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */       
/* 1958 */       ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 1959 */       if (codes[ch] != 0) {
/*      */         break;
/*      */       }
/* 1962 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 1965 */     if (currQuadBytes > 0) {
/* 1966 */       if (qlen >= quads.length) {
/* 1967 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 1969 */       quads[(qlen++)] = currQuad;
/*      */     }
/* 1971 */     Name name = this._symbols.findName(quads, qlen);
/* 1972 */     if (name == null) {
/* 1973 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 1975 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Name _parseAposName()
/*      */     throws IOException
/*      */   {
/* 1985 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1986 */       (!loadMore())) {
/* 1987 */       _reportInvalidEOF(": was expecting closing ''' for name");
/*      */     }
/*      */     
/* 1990 */     int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1991 */     if (ch == 39) {
/* 1992 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/* 1994 */     int[] quads = this._quadBuffer;
/* 1995 */     int qlen = 0;
/* 1996 */     int currQuad = 0;
/* 1997 */     int currQuadBytes = 0;
/*      */     
/*      */ 
/*      */ 
/* 2001 */     int[] codes = _icLatin1;
/*      */     
/*      */ 
/* 2004 */     while (ch != 39)
/*      */     {
/*      */ 
/*      */ 
/* 2008 */       if ((ch != 34) && (codes[ch] != 0)) {
/* 2009 */         if (ch != 92)
/*      */         {
/*      */ 
/* 2012 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/* 2015 */           ch = _decodeEscaped();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2022 */         if (ch > 127)
/*      */         {
/* 2024 */           if (currQuadBytes >= 4) {
/* 2025 */             if (qlen >= quads.length) {
/* 2026 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/* 2028 */             quads[(qlen++)] = currQuad;
/* 2029 */             currQuad = 0;
/* 2030 */             currQuadBytes = 0;
/*      */           }
/* 2032 */           if (ch < 2048) {
/* 2033 */             currQuad = currQuad << 8 | 0xC0 | ch >> 6;
/* 2034 */             currQuadBytes++;
/*      */           }
/*      */           else {
/* 2037 */             currQuad = currQuad << 8 | 0xE0 | ch >> 12;
/* 2038 */             currQuadBytes++;
/*      */             
/* 2040 */             if (currQuadBytes >= 4) {
/* 2041 */               if (qlen >= quads.length) {
/* 2042 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/* 2044 */               quads[(qlen++)] = currQuad;
/* 2045 */               currQuad = 0;
/* 2046 */               currQuadBytes = 0;
/*      */             }
/* 2048 */             currQuad = currQuad << 8 | 0x80 | ch >> 6 & 0x3F;
/* 2049 */             currQuadBytes++;
/*      */           }
/*      */           
/* 2052 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 2056 */       if (currQuadBytes < 4) {
/* 2057 */         currQuadBytes++;
/* 2058 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 2060 */         if (qlen >= quads.length) {
/* 2061 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 2063 */         quads[(qlen++)] = currQuad;
/* 2064 */         currQuad = ch;
/* 2065 */         currQuadBytes = 1;
/*      */       }
/* 2067 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2068 */         (!loadMore())) {
/* 2069 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */       
/* 2072 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/*      */     
/* 2075 */     if (currQuadBytes > 0) {
/* 2076 */       if (qlen >= quads.length) {
/* 2077 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 2079 */       quads[(qlen++)] = pad(currQuad, currQuadBytes);
/*      */     }
/* 2081 */     Name name = this._symbols.findName(quads, qlen);
/* 2082 */     if (name == null) {
/* 2083 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 2085 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Name findName(int q1, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2097 */     q1 = pad(q1, lastQuadBytes);
/*      */     
/* 2099 */     Name name = this._symbols.findName(q1);
/* 2100 */     if (name != null) {
/* 2101 */       return name;
/*      */     }
/*      */     
/* 2104 */     this._quadBuffer[0] = q1;
/* 2105 */     return addName(this._quadBuffer, 1, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final Name findName(int q1, int q2, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2111 */     q2 = pad(q2, lastQuadBytes);
/*      */     
/* 2113 */     Name name = this._symbols.findName(q1, q2);
/* 2114 */     if (name != null) {
/* 2115 */       return name;
/*      */     }
/*      */     
/* 2118 */     this._quadBuffer[0] = q1;
/* 2119 */     this._quadBuffer[1] = q2;
/* 2120 */     return addName(this._quadBuffer, 2, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final Name findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2126 */     if (qlen >= quads.length) {
/* 2127 */       this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */     }
/* 2129 */     quads[(qlen++)] = pad(lastQuad, lastQuadBytes);
/* 2130 */     Name name = this._symbols.findName(quads, qlen);
/* 2131 */     if (name == null) {
/* 2132 */       return addName(quads, qlen, lastQuadBytes);
/*      */     }
/* 2134 */     return name;
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
/*      */   private final Name addName(int[] quads, int qlen, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2151 */     int byteLen = (qlen << 2) - 4 + lastQuadBytes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int lastQuad;
/*      */     
/*      */ 
/*      */ 
/* 2160 */     if (lastQuadBytes < 4) {
/* 2161 */       int lastQuad = quads[(qlen - 1)];
/*      */       
/* 2163 */       quads[(qlen - 1)] = (lastQuad << (4 - lastQuadBytes << 3));
/*      */     } else {
/* 2165 */       lastQuad = 0;
/*      */     }
/*      */     
/*      */ 
/* 2169 */     char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 2170 */     int cix = 0;
/*      */     
/* 2172 */     for (int ix = 0; ix < byteLen;) {
/* 2173 */       int ch = quads[(ix >> 2)];
/* 2174 */       int byteIx = ix & 0x3;
/* 2175 */       ch = ch >> (3 - byteIx << 3) & 0xFF;
/* 2176 */       ix++;
/*      */       
/* 2178 */       if (ch > 127) { int needed;
/*      */         int needed;
/* 2180 */         if ((ch & 0xE0) == 192) {
/* 2181 */           ch &= 0x1F;
/* 2182 */           needed = 1; } else { int needed;
/* 2183 */           if ((ch & 0xF0) == 224) {
/* 2184 */             ch &= 0xF;
/* 2185 */             needed = 2; } else { int needed;
/* 2186 */             if ((ch & 0xF8) == 240) {
/* 2187 */               ch &= 0x7;
/* 2188 */               needed = 3;
/*      */             } else {
/* 2190 */               _reportInvalidInitial(ch);
/* 2191 */               needed = ch = 1;
/*      */             } } }
/* 2193 */         if (ix + needed > byteLen) {
/* 2194 */           _reportInvalidEOF(" in field name");
/*      */         }
/*      */         
/*      */ 
/* 2198 */         int ch2 = quads[(ix >> 2)];
/* 2199 */         byteIx = ix & 0x3;
/* 2200 */         ch2 >>= 3 - byteIx << 3;
/* 2201 */         ix++;
/*      */         
/* 2203 */         if ((ch2 & 0xC0) != 128) {
/* 2204 */           _reportInvalidOther(ch2);
/*      */         }
/* 2206 */         ch = ch << 6 | ch2 & 0x3F;
/* 2207 */         if (needed > 1) {
/* 2208 */           ch2 = quads[(ix >> 2)];
/* 2209 */           byteIx = ix & 0x3;
/* 2210 */           ch2 >>= 3 - byteIx << 3;
/* 2211 */           ix++;
/*      */           
/* 2213 */           if ((ch2 & 0xC0) != 128) {
/* 2214 */             _reportInvalidOther(ch2);
/*      */           }
/* 2216 */           ch = ch << 6 | ch2 & 0x3F;
/* 2217 */           if (needed > 2) {
/* 2218 */             ch2 = quads[(ix >> 2)];
/* 2219 */             byteIx = ix & 0x3;
/* 2220 */             ch2 >>= 3 - byteIx << 3;
/* 2221 */             ix++;
/* 2222 */             if ((ch2 & 0xC0) != 128) {
/* 2223 */               _reportInvalidOther(ch2 & 0xFF);
/*      */             }
/* 2225 */             ch = ch << 6 | ch2 & 0x3F;
/*      */           }
/*      */         }
/* 2228 */         if (needed > 2) {
/* 2229 */           ch -= 65536;
/* 2230 */           if (cix >= cbuf.length) {
/* 2231 */             cbuf = this._textBuffer.expandCurrentSegment();
/*      */           }
/* 2233 */           cbuf[(cix++)] = ((char)(55296 + (ch >> 10)));
/* 2234 */           ch = 0xDC00 | ch & 0x3FF;
/*      */         }
/*      */       }
/* 2237 */       if (cix >= cbuf.length) {
/* 2238 */         cbuf = this._textBuffer.expandCurrentSegment();
/*      */       }
/* 2240 */       cbuf[(cix++)] = ((char)ch);
/*      */     }
/*      */     
/*      */ 
/* 2244 */     String baseName = new String(cbuf, 0, cix);
/*      */     
/* 2246 */     if (lastQuadBytes < 4) {
/* 2247 */       quads[(qlen - 1)] = lastQuad;
/*      */     }
/* 2249 */     return this._symbols.addName(baseName, quads, qlen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _finishString()
/*      */     throws IOException
/*      */   {
/* 2262 */     int ptr = this._inputPtr;
/* 2263 */     if (ptr >= this._inputEnd) {
/* 2264 */       loadMoreGuaranteed();
/* 2265 */       ptr = this._inputPtr;
/*      */     }
/* 2267 */     int outPtr = 0;
/* 2268 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 2269 */     int[] codes = _icUTF8;
/*      */     
/* 2271 */     int max = Math.min(this._inputEnd, ptr + outBuf.length);
/* 2272 */     byte[] inputBuffer = this._inputBuffer;
/* 2273 */     while (ptr < max) {
/* 2274 */       int c = inputBuffer[ptr] & 0xFF;
/* 2275 */       if (codes[c] != 0) {
/* 2276 */         if (c != 34) break;
/* 2277 */         this._inputPtr = (ptr + 1);
/* 2278 */         this._textBuffer.setCurrentLength(outPtr);
/* 2279 */         return;
/*      */       }
/*      */       
/*      */ 
/* 2283 */       ptr++;
/* 2284 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2286 */     this._inputPtr = ptr;
/* 2287 */     _finishString2(outBuf, outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _finishString2(char[] outBuf, int outPtr)
/*      */     throws IOException
/*      */   {
/* 2296 */     int[] codes = _icUTF8;
/* 2297 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2304 */       int ptr = this._inputPtr;
/* 2305 */       if (ptr >= this._inputEnd) {
/* 2306 */         loadMoreGuaranteed();
/* 2307 */         ptr = this._inputPtr;
/*      */       }
/* 2309 */       if (outPtr >= outBuf.length) {
/* 2310 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2311 */         outPtr = 0;
/*      */       }
/* 2313 */       int max = Math.min(this._inputEnd, ptr + (outBuf.length - outPtr));
/* 2314 */       while (ptr < max) {
/* 2315 */         int c = inputBuffer[(ptr++)] & 0xFF;
/* 2316 */         if (codes[c] != 0) {
/* 2317 */           this._inputPtr = ptr;
/*      */           break label124;
/*      */         }
/* 2320 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/* 2322 */       this._inputPtr = ptr;
/* 2323 */       continue;
/*      */       label124:
/* 2325 */       int c; if (c == 34) {
/*      */         break;
/*      */       }
/*      */       
/* 2329 */       switch (codes[c]) {
/*      */       case 1: 
/* 2331 */         c = _decodeEscaped();
/* 2332 */         break;
/*      */       case 2: 
/* 2334 */         c = _decodeUtf8_2(c);
/* 2335 */         break;
/*      */       case 3: 
/* 2337 */         if (this._inputEnd - this._inputPtr >= 2) {
/* 2338 */           c = _decodeUtf8_3fast(c);
/*      */         } else {
/* 2340 */           c = _decodeUtf8_3(c);
/*      */         }
/* 2342 */         break;
/*      */       case 4: 
/* 2344 */         c = _decodeUtf8_4(c);
/*      */         
/* 2346 */         outBuf[(outPtr++)] = ((char)(0xD800 | c >> 10));
/* 2347 */         if (outPtr >= outBuf.length) {
/* 2348 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 2349 */           outPtr = 0;
/*      */         }
/* 2351 */         c = 0xDC00 | c & 0x3FF;
/*      */         
/* 2353 */         break;
/*      */       default: 
/* 2355 */         if (c < 32)
/*      */         {
/* 2357 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else {
/* 2360 */           _reportInvalidChar(c);
/*      */         }
/*      */         break;
/*      */       }
/* 2364 */       if (outPtr >= outBuf.length) {
/* 2365 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2366 */         outPtr = 0;
/*      */       }
/*      */       
/* 2369 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2371 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _skipString()
/*      */     throws IOException
/*      */   {
/* 2381 */     this._tokenIncomplete = false;
/*      */     
/*      */ 
/* 2384 */     int[] codes = _icUTF8;
/* 2385 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2393 */       int ptr = this._inputPtr;
/* 2394 */       int max = this._inputEnd;
/* 2395 */       if (ptr >= max) {
/* 2396 */         loadMoreGuaranteed();
/* 2397 */         ptr = this._inputPtr;
/* 2398 */         max = this._inputEnd;
/*      */       }
/* 2400 */       while (ptr < max) {
/* 2401 */         int c = inputBuffer[(ptr++)] & 0xFF;
/* 2402 */         if (codes[c] != 0) {
/* 2403 */           this._inputPtr = ptr;
/*      */           break label92;
/*      */         }
/*      */       }
/* 2407 */       this._inputPtr = ptr;
/* 2408 */       continue;
/*      */       label92:
/* 2410 */       int c; if (c == 34) {
/*      */         break;
/*      */       }
/*      */       
/* 2414 */       switch (codes[c]) {
/*      */       case 1: 
/* 2416 */         _decodeEscaped();
/* 2417 */         break;
/*      */       case 2: 
/* 2419 */         _skipUtf8_2(c);
/* 2420 */         break;
/*      */       case 3: 
/* 2422 */         _skipUtf8_3(c);
/* 2423 */         break;
/*      */       case 4: 
/* 2425 */         _skipUtf8_4(c);
/* 2426 */         break;
/*      */       default: 
/* 2428 */         if (c < 32)
/*      */         {
/* 2430 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else {
/* 2433 */           _reportInvalidChar(c);
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonToken _handleUnexpectedValue(int c)
/*      */     throws IOException
/*      */   {
/* 2447 */     switch (c)
/*      */     {
/*      */ 
/*      */     case 93: 
/*      */     case 125: 
/* 2452 */       _reportUnexpectedChar(c, "expected a value");
/*      */     case 39: 
/* 2454 */       if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
/* 2455 */         return _handleApos();
/*      */       }
/*      */       break;
/*      */     case 78: 
/* 2459 */       _matchToken("NaN", 1);
/* 2460 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2461 */         return resetAsNaN("NaN", NaN.0D);
/*      */       }
/* 2463 */       _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 2464 */       break;
/*      */     case 73: 
/* 2466 */       _matchToken("Infinity", 1);
/* 2467 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2468 */         return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
/*      */       }
/* 2470 */       _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 2471 */       break;
/*      */     case 43: 
/* 2473 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2474 */         (!loadMore())) {
/* 2475 */         _reportInvalidEOFInValue();
/*      */       }
/*      */       
/* 2478 */       return _handleInvalidNumberStart(this._inputBuffer[(this._inputPtr++)] & 0xFF, false);
/*      */     }
/*      */     
/* 2481 */     if (Character.isJavaIdentifierStart(c)) {
/* 2482 */       _reportInvalidToken("" + (char)c, "('true', 'false' or 'null')");
/*      */     }
/*      */     
/* 2485 */     _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/* 2486 */     return null;
/*      */   }
/*      */   
/*      */   protected JsonToken _handleApos()
/*      */     throws IOException
/*      */   {
/* 2492 */     int c = 0;
/*      */     
/* 2494 */     int outPtr = 0;
/* 2495 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*      */     
/*      */ 
/* 2498 */     int[] codes = _icUTF8;
/* 2499 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2506 */       if (this._inputPtr >= this._inputEnd) {
/* 2507 */         loadMoreGuaranteed();
/*      */       }
/* 2509 */       if (outPtr >= outBuf.length) {
/* 2510 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2511 */         outPtr = 0;
/*      */       }
/* 2513 */       int max = this._inputEnd;
/*      */       
/* 2515 */       int max2 = this._inputPtr + (outBuf.length - outPtr);
/* 2516 */       if (max2 < max) {
/* 2517 */         max = max2;
/*      */       }
/*      */       
/* 2520 */       while (this._inputPtr < max) {
/* 2521 */         c = inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2522 */         if ((c == 39) || (codes[c] != 0)) {
/*      */           break label140;
/*      */         }
/* 2525 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/* 2527 */       continue;
/*      */       
/*      */       label140:
/* 2530 */       if (c == 39) {
/*      */         break;
/*      */       }
/*      */       
/* 2534 */       switch (codes[c]) {
/*      */       case 1: 
/* 2536 */         if (c != 39) {
/* 2537 */           c = _decodeEscaped();
/*      */         }
/*      */         break;
/*      */       case 2: 
/* 2541 */         c = _decodeUtf8_2(c);
/* 2542 */         break;
/*      */       case 3: 
/* 2544 */         if (this._inputEnd - this._inputPtr >= 2) {
/* 2545 */           c = _decodeUtf8_3fast(c);
/*      */         } else {
/* 2547 */           c = _decodeUtf8_3(c);
/*      */         }
/* 2549 */         break;
/*      */       case 4: 
/* 2551 */         c = _decodeUtf8_4(c);
/*      */         
/* 2553 */         outBuf[(outPtr++)] = ((char)(0xD800 | c >> 10));
/* 2554 */         if (outPtr >= outBuf.length) {
/* 2555 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 2556 */           outPtr = 0;
/*      */         }
/* 2558 */         c = 0xDC00 | c & 0x3FF;
/*      */         
/* 2560 */         break;
/*      */       default: 
/* 2562 */         if (c < 32) {
/* 2563 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         
/* 2566 */         _reportInvalidChar(c);
/*      */       }
/*      */       
/* 2569 */       if (outPtr >= outBuf.length) {
/* 2570 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2571 */         outPtr = 0;
/*      */       }
/*      */       
/* 2574 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2576 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 2578 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleInvalidNumberStart(int ch, boolean neg)
/*      */     throws IOException
/*      */   {
/* 2588 */     while (ch == 73) {
/* 2589 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2590 */         (!loadMore())) {
/* 2591 */         _reportInvalidEOFInValue();
/*      */       }
/*      */       
/* 2594 */       ch = this._inputBuffer[(this._inputPtr++)];
/*      */       String match;
/* 2596 */       String match; if (ch == 78) {
/* 2597 */         match = neg ? "-INF" : "+INF";
/* 2598 */       } else { if (ch != 110) break;
/* 2599 */         match = neg ? "-Infinity" : "+Infinity";
/*      */       }
/*      */       
/*      */ 
/* 2603 */       _matchToken(match, 3);
/* 2604 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2605 */         return resetAsNaN(match, neg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */       }
/* 2607 */       _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */     }
/* 2609 */     reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/* 2610 */     return null;
/*      */   }
/*      */   
/*      */   protected final void _matchToken(String matchStr, int i) throws IOException
/*      */   {
/* 2615 */     int len = matchStr.length();
/* 2616 */     if (this._inputPtr + len >= this._inputEnd) {
/* 2617 */       _matchToken2(matchStr, i);
/*      */     }
/*      */     else {
/*      */       do {
/* 2621 */         if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
/* 2622 */           _reportInvalidToken(matchStr.substring(0, i));
/*      */         }
/* 2624 */         this._inputPtr += 1;
/* 2625 */         i++; } while (i < len);
/*      */       
/* 2627 */       int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 2628 */       if ((ch >= 48) && (ch != 93) && (ch != 125)) {
/* 2629 */         _checkMatchEnd(matchStr, i, ch);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _matchToken2(String matchStr, int i) throws IOException {
/* 2635 */     int len = matchStr.length();
/*      */     do {
/* 2637 */       if (((this._inputPtr >= this._inputEnd) && (!loadMore())) || (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)))
/*      */       {
/* 2639 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2641 */       this._inputPtr += 1;
/* 2642 */       i++; } while (i < len);
/*      */     
/*      */ 
/* 2645 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 2646 */       return;
/*      */     }
/* 2648 */     int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 2649 */     if ((ch >= 48) && (ch != 93) && (ch != 125)) {
/* 2650 */       _checkMatchEnd(matchStr, i, ch);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _checkMatchEnd(String matchStr, int i, int ch) throws IOException
/*      */   {
/* 2656 */     char c = (char)_decodeCharForError(ch);
/* 2657 */     if (Character.isJavaIdentifierPart(c)) {
/* 2658 */       _reportInvalidToken(matchStr.substring(0, i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _skipWS()
/*      */     throws IOException
/*      */   {
/* 2670 */     while (this._inputPtr < this._inputEnd) {
/* 2671 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2672 */       if (i > 32) {
/* 2673 */         if ((i == 47) || (i == 35)) {
/* 2674 */           this._inputPtr -= 1;
/* 2675 */           return _skipWS2();
/*      */         }
/* 2677 */         return i;
/*      */       }
/* 2679 */       if (i != 32) {
/* 2680 */         if (i == 10) {
/* 2681 */           this._currInputRow += 1;
/* 2682 */           this._currInputRowStart = this._inputPtr;
/* 2683 */         } else if (i == 13) {
/* 2684 */           _skipCR();
/* 2685 */         } else if (i != 9) {
/* 2686 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2690 */     return _skipWS2();
/*      */   }
/*      */   
/*      */   private final int _skipWS2() throws IOException
/*      */   {
/* 2695 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 2696 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2697 */       if (i > 32) {
/* 2698 */         if (i == 47) {
/* 2699 */           _skipComment();
/*      */ 
/*      */         }
/* 2702 */         else if ((i != 35) || 
/* 2703 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2707 */           return i;
/*      */         }
/* 2709 */       } else if (i != 32) {
/* 2710 */         if (i == 10) {
/* 2711 */           this._currInputRow += 1;
/* 2712 */           this._currInputRowStart = this._inputPtr;
/* 2713 */         } else if (i == 13) {
/* 2714 */           _skipCR();
/* 2715 */         } else if (i != 9) {
/* 2716 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2720 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException
/*      */   {
/* 2727 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2728 */       (!loadMore())) {
/* 2729 */       return _eofAsNextChar();
/*      */     }
/*      */     
/* 2732 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2733 */     if (i > 32) {
/* 2734 */       if ((i == 47) || (i == 35)) {
/* 2735 */         this._inputPtr -= 1;
/* 2736 */         return _skipWSOrEnd2();
/*      */       }
/* 2738 */       return i;
/*      */     }
/* 2740 */     if (i != 32) {
/* 2741 */       if (i == 10) {
/* 2742 */         this._currInputRow += 1;
/* 2743 */         this._currInputRowStart = this._inputPtr;
/* 2744 */       } else if (i == 13) {
/* 2745 */         _skipCR();
/* 2746 */       } else if (i != 9) {
/* 2747 */         _throwInvalidSpace(i);
/*      */       }
/*      */     }
/*      */     
/* 2751 */     while (this._inputPtr < this._inputEnd) {
/* 2752 */       i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2753 */       if (i > 32) {
/* 2754 */         if ((i == 47) || (i == 35)) {
/* 2755 */           this._inputPtr -= 1;
/* 2756 */           return _skipWSOrEnd2();
/*      */         }
/* 2758 */         return i;
/*      */       }
/* 2760 */       if (i != 32) {
/* 2761 */         if (i == 10) {
/* 2762 */           this._currInputRow += 1;
/* 2763 */           this._currInputRowStart = this._inputPtr;
/* 2764 */         } else if (i == 13) {
/* 2765 */           _skipCR();
/* 2766 */         } else if (i != 9) {
/* 2767 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2771 */     return _skipWSOrEnd2();
/*      */   }
/*      */   
/*      */   private final int _skipWSOrEnd2() throws IOException
/*      */   {
/* 2776 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 2777 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2778 */       if (i > 32) {
/* 2779 */         if (i == 47) {
/* 2780 */           _skipComment();
/*      */ 
/*      */         }
/* 2783 */         else if ((i != 35) || 
/* 2784 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2788 */           return i; }
/* 2789 */       } else if (i != 32) {
/* 2790 */         if (i == 10) {
/* 2791 */           this._currInputRow += 1;
/* 2792 */           this._currInputRowStart = this._inputPtr;
/* 2793 */         } else if (i == 13) {
/* 2794 */           _skipCR();
/* 2795 */         } else if (i != 9) {
/* 2796 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2801 */     return _eofAsNextChar();
/*      */   }
/*      */   
/*      */   private final int _skipColon() throws IOException
/*      */   {
/* 2806 */     if (this._inputPtr + 4 >= this._inputEnd) {
/* 2807 */       return _skipColon2(false);
/*      */     }
/*      */     
/* 2810 */     int i = this._inputBuffer[this._inputPtr];
/* 2811 */     if (i == 58) {
/* 2812 */       i = this._inputBuffer[(++this._inputPtr)];
/* 2813 */       if (i > 32) {
/* 2814 */         if ((i == 47) || (i == 35)) {
/* 2815 */           return _skipColon2(true);
/*      */         }
/* 2817 */         this._inputPtr += 1;
/* 2818 */         return i;
/*      */       }
/* 2820 */       if ((i == 32) || (i == 9)) {
/* 2821 */         i = this._inputBuffer[(++this._inputPtr)];
/* 2822 */         if (i > 32) {
/* 2823 */           if ((i == 47) || (i == 35)) {
/* 2824 */             return _skipColon2(true);
/*      */           }
/* 2826 */           this._inputPtr += 1;
/* 2827 */           return i;
/*      */         }
/*      */       }
/* 2830 */       return _skipColon2(true);
/*      */     }
/* 2832 */     if ((i == 32) || (i == 9)) {
/* 2833 */       i = this._inputBuffer[(++this._inputPtr)];
/*      */     }
/* 2835 */     if (i == 58) {
/* 2836 */       i = this._inputBuffer[(++this._inputPtr)];
/* 2837 */       if (i > 32) {
/* 2838 */         if ((i == 47) || (i == 35)) {
/* 2839 */           return _skipColon2(true);
/*      */         }
/* 2841 */         this._inputPtr += 1;
/* 2842 */         return i;
/*      */       }
/* 2844 */       if ((i == 32) || (i == 9)) {
/* 2845 */         i = this._inputBuffer[(++this._inputPtr)];
/* 2846 */         if (i > 32) {
/* 2847 */           if ((i == 47) || (i == 35)) {
/* 2848 */             return _skipColon2(true);
/*      */           }
/* 2850 */           this._inputPtr += 1;
/* 2851 */           return i;
/*      */         }
/*      */       }
/* 2854 */       return _skipColon2(true);
/*      */     }
/* 2856 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final int _skipColon2(boolean gotColon) throws IOException
/*      */   {
/* 2861 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 2862 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       
/* 2864 */       if (i > 32) {
/* 2865 */         if (i == 47) {
/* 2866 */           _skipComment();
/*      */ 
/*      */         }
/* 2869 */         else if ((i != 35) || 
/* 2870 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2874 */           if (gotColon) {
/* 2875 */             return i;
/*      */           }
/* 2877 */           if (i != 58) {
/* 2878 */             if (i < 32) {
/* 2879 */               _throwInvalidSpace(i);
/*      */             }
/* 2881 */             _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */           }
/* 2883 */           gotColon = true;
/* 2884 */         } } else if (i != 32) {
/* 2885 */         if (i == 10) {
/* 2886 */           this._currInputRow += 1;
/* 2887 */           this._currInputRowStart = this._inputPtr;
/* 2888 */         } else if (i == 13) {
/* 2889 */           _skipCR();
/* 2890 */         } else if (i != 9) {
/* 2891 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2895 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
/*      */   }
/*      */   
/*      */   private final void _skipComment() throws IOException
/*      */   {
/* 2900 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/* 2901 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */     
/* 2904 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 2905 */       _reportInvalidEOF(" in a comment");
/*      */     }
/* 2907 */     int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2908 */     if (c == 47) {
/* 2909 */       _skipLine();
/* 2910 */     } else if (c == 42) {
/* 2911 */       _skipCComment();
/*      */     } else {
/* 2913 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _skipCComment()
/*      */     throws IOException
/*      */   {
/* 2920 */     int[] codes = CharTypes.getInputCodeComment();
/*      */     
/*      */ 
/*      */ 
/* 2924 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 2925 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2926 */       int code = codes[i];
/* 2927 */       if (code != 0)
/* 2928 */         switch (code) {
/*      */         case 42: 
/* 2930 */           if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*      */             break label218;
/*      */           }
/* 2933 */           if (this._inputBuffer[this._inputPtr] == 47) {
/* 2934 */             this._inputPtr += 1; return;
/*      */           }
/*      */           
/*      */           break;
/*      */         case 10: 
/* 2939 */           this._currInputRow += 1;
/* 2940 */           this._currInputRowStart = this._inputPtr;
/* 2941 */           break;
/*      */         case 13: 
/* 2943 */           _skipCR();
/* 2944 */           break;
/*      */         case 2: 
/* 2946 */           _skipUtf8_2(i);
/* 2947 */           break;
/*      */         case 3: 
/* 2949 */           _skipUtf8_3(i);
/* 2950 */           break;
/*      */         case 4: 
/* 2952 */           _skipUtf8_4(i);
/* 2953 */           break;
/*      */         
/*      */         default: 
/* 2956 */           _reportInvalidChar(i);
/*      */         }
/*      */     }
/*      */     label218:
/* 2960 */     _reportInvalidEOF(" in a comment");
/*      */   }
/*      */   
/*      */   private final boolean _skipYAMLComment() throws IOException
/*      */   {
/* 2965 */     if (!isEnabled(JsonParser.Feature.ALLOW_YAML_COMMENTS)) {
/* 2966 */       return false;
/*      */     }
/* 2968 */     _skipLine();
/* 2969 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _skipLine()
/*      */     throws IOException
/*      */   {
/* 2979 */     int[] codes = CharTypes.getInputCodeComment();
/* 2980 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 2981 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2982 */       int code = codes[i];
/* 2983 */       if (code != 0) {
/* 2984 */         switch (code) {
/*      */         case 10: 
/* 2986 */           this._currInputRow += 1;
/* 2987 */           this._currInputRowStart = this._inputPtr;
/* 2988 */           return;
/*      */         case 13: 
/* 2990 */           _skipCR(); return;
/*      */         case 42: 
/*      */           break;
/*      */         
/*      */         case 2: 
/* 2995 */           _skipUtf8_2(i);
/* 2996 */           break;
/*      */         case 3: 
/* 2998 */           _skipUtf8_3(i);
/* 2999 */           break;
/*      */         case 4: 
/* 3001 */           _skipUtf8_4(i);
/* 3002 */           break;
/*      */         default: 
/* 3004 */           if (code < 0)
/*      */           {
/* 3006 */             _reportInvalidChar(i);
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected char _decodeEscaped() throws IOException
/*      */   {
/* 3016 */     if ((this._inputPtr >= this._inputEnd) && 
/* 3017 */       (!loadMore())) {
/* 3018 */       _reportInvalidEOF(" in character escape sequence");
/*      */     }
/*      */     
/* 3021 */     int c = this._inputBuffer[(this._inputPtr++)];
/*      */     
/* 3023 */     switch (c)
/*      */     {
/*      */     case 98: 
/* 3026 */       return '\b';
/*      */     case 116: 
/* 3028 */       return '\t';
/*      */     case 110: 
/* 3030 */       return '\n';
/*      */     case 102: 
/* 3032 */       return '\f';
/*      */     case 114: 
/* 3034 */       return '\r';
/*      */     
/*      */ 
/*      */     case 34: 
/*      */     case 47: 
/*      */     case 92: 
/* 3040 */       return (char)c;
/*      */     
/*      */     case 117: 
/*      */       break;
/*      */     
/*      */     default: 
/* 3046 */       return _handleUnrecognizedCharacterEscape((char)_decodeCharForError(c));
/*      */     }
/*      */     
/*      */     
/* 3050 */     int value = 0;
/* 3051 */     for (int i = 0; i < 4; i++) {
/* 3052 */       if ((this._inputPtr >= this._inputEnd) && 
/* 3053 */         (!loadMore())) {
/* 3054 */         _reportInvalidEOF(" in character escape sequence");
/*      */       }
/*      */       
/* 3057 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 3058 */       int digit = CharTypes.charToHex(ch);
/* 3059 */       if (digit < 0) {
/* 3060 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 3062 */       value = value << 4 | digit;
/*      */     }
/* 3064 */     return (char)value;
/*      */   }
/*      */   
/*      */   protected int _decodeCharForError(int firstByte) throws IOException
/*      */   {
/* 3069 */     int c = firstByte & 0xFF;
/* 3070 */     if (c > 127)
/*      */     {
/*      */       int needed;
/*      */       int needed;
/* 3074 */       if ((c & 0xE0) == 192) {
/* 3075 */         c &= 0x1F;
/* 3076 */         needed = 1; } else { int needed;
/* 3077 */         if ((c & 0xF0) == 224) {
/* 3078 */           c &= 0xF;
/* 3079 */           needed = 2; } else { int needed;
/* 3080 */           if ((c & 0xF8) == 240)
/*      */           {
/* 3082 */             c &= 0x7;
/* 3083 */             needed = 3;
/*      */           } else {
/* 3085 */             _reportInvalidInitial(c & 0xFF);
/* 3086 */             needed = 1;
/*      */           }
/*      */         } }
/* 3089 */       int d = nextByte();
/* 3090 */       if ((d & 0xC0) != 128) {
/* 3091 */         _reportInvalidOther(d & 0xFF);
/*      */       }
/* 3093 */       c = c << 6 | d & 0x3F;
/*      */       
/* 3095 */       if (needed > 1) {
/* 3096 */         d = nextByte();
/* 3097 */         if ((d & 0xC0) != 128) {
/* 3098 */           _reportInvalidOther(d & 0xFF);
/*      */         }
/* 3100 */         c = c << 6 | d & 0x3F;
/* 3101 */         if (needed > 2) {
/* 3102 */           d = nextByte();
/* 3103 */           if ((d & 0xC0) != 128) {
/* 3104 */             _reportInvalidOther(d & 0xFF);
/*      */           }
/* 3106 */           c = c << 6 | d & 0x3F;
/*      */         }
/*      */       }
/*      */     }
/* 3110 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _decodeUtf8_2(int c)
/*      */     throws IOException
/*      */   {
/* 3121 */     if (this._inputPtr >= this._inputEnd) {
/* 3122 */       loadMoreGuaranteed();
/*      */     }
/* 3124 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3125 */     if ((d & 0xC0) != 128) {
/* 3126 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3128 */     return (c & 0x1F) << 6 | d & 0x3F;
/*      */   }
/*      */   
/*      */   private final int _decodeUtf8_3(int c1) throws IOException
/*      */   {
/* 3133 */     if (this._inputPtr >= this._inputEnd) {
/* 3134 */       loadMoreGuaranteed();
/*      */     }
/* 3136 */     c1 &= 0xF;
/* 3137 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3138 */     if ((d & 0xC0) != 128) {
/* 3139 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3141 */     int c = c1 << 6 | d & 0x3F;
/* 3142 */     if (this._inputPtr >= this._inputEnd) {
/* 3143 */       loadMoreGuaranteed();
/*      */     }
/* 3145 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3146 */     if ((d & 0xC0) != 128) {
/* 3147 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3149 */     c = c << 6 | d & 0x3F;
/* 3150 */     return c;
/*      */   }
/*      */   
/*      */   private final int _decodeUtf8_3fast(int c1) throws IOException
/*      */   {
/* 3155 */     c1 &= 0xF;
/* 3156 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3157 */     if ((d & 0xC0) != 128) {
/* 3158 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3160 */     int c = c1 << 6 | d & 0x3F;
/* 3161 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3162 */     if ((d & 0xC0) != 128) {
/* 3163 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3165 */     c = c << 6 | d & 0x3F;
/* 3166 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _decodeUtf8_4(int c)
/*      */     throws IOException
/*      */   {
/* 3175 */     if (this._inputPtr >= this._inputEnd) {
/* 3176 */       loadMoreGuaranteed();
/*      */     }
/* 3178 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3179 */     if ((d & 0xC0) != 128) {
/* 3180 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3182 */     c = (c & 0x7) << 6 | d & 0x3F;
/*      */     
/* 3184 */     if (this._inputPtr >= this._inputEnd) {
/* 3185 */       loadMoreGuaranteed();
/*      */     }
/* 3187 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3188 */     if ((d & 0xC0) != 128) {
/* 3189 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3191 */     c = c << 6 | d & 0x3F;
/* 3192 */     if (this._inputPtr >= this._inputEnd) {
/* 3193 */       loadMoreGuaranteed();
/*      */     }
/* 3195 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3196 */     if ((d & 0xC0) != 128) {
/* 3197 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3203 */     return (c << 6 | d & 0x3F) - 65536;
/*      */   }
/*      */   
/*      */   private final void _skipUtf8_2(int c) throws IOException
/*      */   {
/* 3208 */     if (this._inputPtr >= this._inputEnd) {
/* 3209 */       loadMoreGuaranteed();
/*      */     }
/* 3211 */     c = this._inputBuffer[(this._inputPtr++)];
/* 3212 */     if ((c & 0xC0) != 128) {
/* 3213 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final void _skipUtf8_3(int c)
/*      */     throws IOException
/*      */   {
/* 3222 */     if (this._inputPtr >= this._inputEnd) {
/* 3223 */       loadMoreGuaranteed();
/*      */     }
/*      */     
/* 3226 */     c = this._inputBuffer[(this._inputPtr++)];
/* 3227 */     if ((c & 0xC0) != 128) {
/* 3228 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/* 3230 */     if (this._inputPtr >= this._inputEnd) {
/* 3231 */       loadMoreGuaranteed();
/*      */     }
/* 3233 */     c = this._inputBuffer[(this._inputPtr++)];
/* 3234 */     if ((c & 0xC0) != 128) {
/* 3235 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _skipUtf8_4(int c) throws IOException
/*      */   {
/* 3241 */     if (this._inputPtr >= this._inputEnd) {
/* 3242 */       loadMoreGuaranteed();
/*      */     }
/* 3244 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3245 */     if ((d & 0xC0) != 128) {
/* 3246 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3248 */     if (this._inputPtr >= this._inputEnd) {
/* 3249 */       loadMoreGuaranteed();
/*      */     }
/* 3251 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3252 */     if ((d & 0xC0) != 128) {
/* 3253 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3255 */     if (this._inputPtr >= this._inputEnd) {
/* 3256 */       loadMoreGuaranteed();
/*      */     }
/* 3258 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3259 */     if ((d & 0xC0) != 128) {
/* 3260 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
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
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/* 3276 */     if (((this._inputPtr < this._inputEnd) || (loadMore())) && 
/* 3277 */       (this._inputBuffer[this._inputPtr] == 10)) {
/* 3278 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 3281 */     this._currInputRow += 1;
/* 3282 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */   
/*      */   private int nextByte() throws IOException
/*      */   {
/* 3287 */     if (this._inputPtr >= this._inputEnd) {
/* 3288 */       loadMoreGuaranteed();
/*      */     }
/* 3290 */     return this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportInvalidToken(String matchedPart)
/*      */     throws IOException
/*      */   {
/* 3301 */     _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
/*      */   }
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart, String msg) throws IOException
/*      */   {
/* 3306 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3313 */     while ((this._inputPtr < this._inputEnd) || (loadMore()))
/*      */     {
/*      */ 
/* 3316 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 3317 */       char c = (char)_decodeCharForError(i);
/* 3318 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 3321 */       sb.append(c);
/*      */     }
/* 3323 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _reportInvalidChar(int c)
/*      */     throws JsonParseException
/*      */   {
/* 3330 */     if (c < 32) {
/* 3331 */       _throwInvalidSpace(c);
/*      */     }
/* 3333 */     _reportInvalidInitial(c);
/*      */   }
/*      */   
/*      */   protected void _reportInvalidInitial(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 3339 */     _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */   
/*      */   protected void _reportInvalidOther(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 3345 */     _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */   
/*      */   protected void _reportInvalidOther(int mask, int ptr)
/*      */     throws JsonParseException
/*      */   {
/* 3351 */     this._inputPtr = ptr;
/* 3352 */     _reportInvalidOther(mask);
/*      */   }
/*      */   
/*      */   public static int[] growArrayBy(int[] arr, int more)
/*      */   {
/* 3357 */     if (arr == null) {
/* 3358 */       return new int[more];
/*      */     }
/* 3360 */     return Arrays.copyOf(arr, arr.length + more);
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
/*      */   protected final byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/* 3376 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 3383 */       if (this._inputPtr >= this._inputEnd) {
/* 3384 */         loadMoreGuaranteed();
/*      */       }
/* 3386 */       int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3387 */       if (ch > 32) {
/* 3388 */         int bits = b64variant.decodeBase64Char(ch);
/* 3389 */         if (bits < 0) {
/* 3390 */           if (ch == 34) {
/* 3391 */             return builder.toByteArray();
/*      */           }
/* 3393 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/* 3394 */           if (bits < 0) {}
/*      */         }
/*      */         else
/*      */         {
/* 3398 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/* 3402 */           if (this._inputPtr >= this._inputEnd) {
/* 3403 */             loadMoreGuaranteed();
/*      */           }
/* 3405 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3406 */           bits = b64variant.decodeBase64Char(ch);
/* 3407 */           if (bits < 0) {
/* 3408 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/* 3410 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/* 3413 */           if (this._inputPtr >= this._inputEnd) {
/* 3414 */             loadMoreGuaranteed();
/*      */           }
/* 3416 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3417 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/* 3420 */           if (bits < 0) {
/* 3421 */             if (bits != -2)
/*      */             {
/* 3423 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/* 3424 */                 decodedData >>= 4;
/* 3425 */                 builder.append(decodedData);
/* 3426 */                 return builder.toByteArray();
/*      */               }
/* 3428 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/* 3430 */             if (bits == -2)
/*      */             {
/* 3432 */               if (this._inputPtr >= this._inputEnd) {
/* 3433 */                 loadMoreGuaranteed();
/*      */               }
/* 3435 */               ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3436 */               if (!b64variant.usesPaddingChar(ch)) {
/* 3437 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/* 3440 */               decodedData >>= 4;
/* 3441 */               builder.append(decodedData);
/* 3442 */               continue;
/*      */             }
/*      */           }
/*      */           
/* 3446 */           decodedData = decodedData << 6 | bits;
/*      */           
/* 3448 */           if (this._inputPtr >= this._inputEnd) {
/* 3449 */             loadMoreGuaranteed();
/*      */           }
/* 3451 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3452 */           bits = b64variant.decodeBase64Char(ch);
/* 3453 */           if (bits < 0) {
/* 3454 */             if (bits != -2)
/*      */             {
/* 3456 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/* 3457 */                 decodedData >>= 2;
/* 3458 */                 builder.appendTwoBytes(decodedData);
/* 3459 */                 return builder.toByteArray();
/*      */               }
/* 3461 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/* 3463 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3470 */               decodedData >>= 2;
/* 3471 */               builder.appendTwoBytes(decodedData);
/* 3472 */               continue;
/*      */             }
/*      */           }
/*      */           
/* 3476 */           decodedData = decodedData << 6 | bits;
/* 3477 */           builder.appendThreeBytes(decodedData);
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
/*      */   private static final int pad(int q, int bytes)
/*      */   {
/* 3491 */     return bytes == 4 ? q : q | -1 << (bytes << 3);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\UTF8StreamJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */