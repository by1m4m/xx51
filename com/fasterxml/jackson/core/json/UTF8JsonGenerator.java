/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.NumberOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ public class UTF8JsonGenerator extends JsonGeneratorImpl
/*      */ {
/*      */   private static final byte BYTE_u = 117;
/*      */   private static final byte BYTE_0 = 48;
/*      */   private static final byte BYTE_LBRACKET = 91;
/*      */   private static final byte BYTE_RBRACKET = 93;
/*      */   private static final byte BYTE_LCURLY = 123;
/*      */   private static final byte BYTE_RCURLY = 125;
/*      */   private static final byte BYTE_BACKSLASH = 92;
/*      */   private static final byte BYTE_COMMA = 44;
/*      */   private static final byte BYTE_COLON = 58;
/*      */   private static final byte BYTE_QUOTE = 34;
/*      */   private static final int MAX_BYTES_TO_BUFFER = 512;
/*   31 */   static final byte[] HEX_CHARS = ;
/*      */   
/*   33 */   private static final byte[] NULL_BYTES = { 110, 117, 108, 108 };
/*   34 */   private static final byte[] TRUE_BYTES = { 116, 114, 117, 101 };
/*   35 */   private static final byte[] FALSE_BYTES = { 102, 97, 108, 115, 101 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final OutputStream _outputStream;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _outputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   58 */   protected int _outputTail = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _outputEnd;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _outputMaxContiguous;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _charBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _charBufferLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _entityBuffer;
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
/*      */   protected boolean _cfgUnqNames;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out)
/*      */   {
/*  116 */     super(ctxt, features, codec);
/*  117 */     this._outputStream = out;
/*  118 */     this._bufferRecyclable = true;
/*  119 */     this._outputBuffer = ctxt.allocWriteEncodingBuffer();
/*  120 */     this._outputEnd = this._outputBuffer.length;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  127 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  128 */     this._charBufferLength = this._charBuffer.length;
/*      */     
/*      */ 
/*  131 */     if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII)) {
/*  132 */       setHighestNonEscapedChar(127);
/*      */     }
/*  134 */     this._cfgUnqNames = (!JsonGenerator.Feature.QUOTE_FIELD_NAMES.enabledIn(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable)
/*      */   {
/*  142 */     super(ctxt, features, codec);
/*  143 */     this._outputStream = out;
/*  144 */     this._bufferRecyclable = bufferRecyclable;
/*  145 */     this._outputTail = outputOffset;
/*  146 */     this._outputBuffer = outputBuffer;
/*  147 */     this._outputEnd = this._outputBuffer.length;
/*      */     
/*  149 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  150 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  151 */     this._charBufferLength = this._charBuffer.length;
/*  152 */     this._cfgUnqNames = (!JsonGenerator.Feature.QUOTE_FIELD_NAMES.enabledIn(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getOutputTarget()
/*      */   {
/*  163 */     return this._outputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  175 */     if (this._cfgPrettyPrinter != null) {
/*  176 */       _writePPFieldName(name);
/*  177 */       return;
/*      */     }
/*  179 */     int status = this._writeContext.writeFieldName(name);
/*  180 */     if (status == 4) {
/*  181 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  183 */     if (status == 1) {
/*  184 */       if (this._outputTail >= this._outputEnd) {
/*  185 */         _flushBuffer();
/*      */       }
/*  187 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  192 */     if (this._cfgUnqNames) {
/*  193 */       _writeStringSegments(name, false);
/*  194 */       return;
/*      */     }
/*  196 */     int len = name.length();
/*      */     
/*  198 */     if (len > this._charBufferLength) {
/*  199 */       _writeStringSegments(name, true);
/*  200 */       return;
/*      */     }
/*  202 */     if (this._outputTail >= this._outputEnd) {
/*  203 */       _flushBuffer();
/*      */     }
/*  205 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  206 */     name.getChars(0, len, this._charBuffer, 0);
/*      */     
/*  208 */     if (len <= this._outputMaxContiguous) {
/*  209 */       if (this._outputTail + len > this._outputEnd) {
/*  210 */         _flushBuffer();
/*      */       }
/*  212 */       _writeStringSegment(this._charBuffer, 0, len);
/*      */     } else {
/*  214 */       _writeStringSegments(this._charBuffer, 0, len);
/*      */     }
/*      */     
/*  217 */     if (this._outputTail >= this._outputEnd) {
/*  218 */       _flushBuffer();
/*      */     }
/*  220 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   public void writeFieldName(SerializableString name)
/*      */     throws IOException
/*      */   {
/*  226 */     if (this._cfgPrettyPrinter != null) {
/*  227 */       _writePPFieldName(name);
/*  228 */       return;
/*      */     }
/*  230 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  231 */     if (status == 4) {
/*  232 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  234 */     if (status == 1) {
/*  235 */       if (this._outputTail >= this._outputEnd) {
/*  236 */         _flushBuffer();
/*      */       }
/*  238 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*  240 */     if (this._cfgUnqNames) {
/*  241 */       _writeUnq(name);
/*  242 */       return;
/*      */     }
/*  244 */     if (this._outputTail >= this._outputEnd) {
/*  245 */       _flushBuffer();
/*      */     }
/*  247 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  248 */     int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  249 */     if (len < 0) {
/*  250 */       _writeBytes(name.asQuotedUTF8());
/*      */     } else {
/*  252 */       this._outputTail += len;
/*      */     }
/*  254 */     if (this._outputTail >= this._outputEnd) {
/*  255 */       _flushBuffer();
/*      */     }
/*  257 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   private final void _writeUnq(SerializableString name) throws IOException {
/*  261 */     int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  262 */     if (len < 0) {
/*  263 */       _writeBytes(name.asQuotedUTF8());
/*      */     } else {
/*  265 */       this._outputTail += len;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException
/*      */   {
/*  278 */     _verifyValueWrite("start an array");
/*  279 */     this._writeContext = this._writeContext.createChildArrayContext();
/*  280 */     if (this._cfgPrettyPrinter != null) {
/*  281 */       this._cfgPrettyPrinter.writeStartArray(this);
/*      */     } else {
/*  283 */       if (this._outputTail >= this._outputEnd) {
/*  284 */         _flushBuffer();
/*      */       }
/*  286 */       this._outputBuffer[(this._outputTail++)] = 91;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeEndArray()
/*      */     throws IOException
/*      */   {
/*  293 */     if (!this._writeContext.inArray()) {
/*  294 */       _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
/*      */     }
/*  296 */     if (this._cfgPrettyPrinter != null) {
/*  297 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  299 */       if (this._outputTail >= this._outputEnd) {
/*  300 */         _flushBuffer();
/*      */       }
/*  302 */       this._outputBuffer[(this._outputTail++)] = 93;
/*      */     }
/*  304 */     this._writeContext = this._writeContext.getParent();
/*      */   }
/*      */   
/*      */   public final void writeStartObject()
/*      */     throws IOException
/*      */   {
/*  310 */     _verifyValueWrite("start an object");
/*  311 */     this._writeContext = this._writeContext.createChildObjectContext();
/*  312 */     if (this._cfgPrettyPrinter != null) {
/*  313 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  315 */       if (this._outputTail >= this._outputEnd) {
/*  316 */         _flushBuffer();
/*      */       }
/*  318 */       this._outputBuffer[(this._outputTail++)] = 123;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeEndObject()
/*      */     throws IOException
/*      */   {
/*  325 */     if (!this._writeContext.inObject()) {
/*  326 */       _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
/*      */     }
/*  328 */     if (this._cfgPrettyPrinter != null) {
/*  329 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  331 */       if (this._outputTail >= this._outputEnd) {
/*  332 */         _flushBuffer();
/*      */       }
/*  334 */       this._outputBuffer[(this._outputTail++)] = 125;
/*      */     }
/*  336 */     this._writeContext = this._writeContext.getParent();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _writePPFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  345 */     int status = this._writeContext.writeFieldName(name);
/*  346 */     if (status == 4) {
/*  347 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  349 */     if (status == 1) {
/*  350 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  352 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*  354 */     if (this._cfgUnqNames) {
/*  355 */       _writeStringSegments(name, false);
/*  356 */       return;
/*      */     }
/*  358 */     int len = name.length();
/*  359 */     if (len > this._charBufferLength) {
/*  360 */       _writeStringSegments(name, true);
/*  361 */       return;
/*      */     }
/*  363 */     if (this._outputTail >= this._outputEnd) {
/*  364 */       _flushBuffer();
/*      */     }
/*  366 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  367 */     name.getChars(0, len, this._charBuffer, 0);
/*      */     
/*  369 */     if (len <= this._outputMaxContiguous) {
/*  370 */       if (this._outputTail + len > this._outputEnd) {
/*  371 */         _flushBuffer();
/*      */       }
/*  373 */       _writeStringSegment(this._charBuffer, 0, len);
/*      */     } else {
/*  375 */       _writeStringSegments(this._charBuffer, 0, len);
/*      */     }
/*  377 */     if (this._outputTail >= this._outputEnd) {
/*  378 */       _flushBuffer();
/*      */     }
/*  380 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   protected final void _writePPFieldName(SerializableString name) throws IOException
/*      */   {
/*  385 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  386 */     if (status == 4) {
/*  387 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  389 */     if (status == 1) {
/*  390 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  392 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */     
/*  395 */     boolean addQuotes = !this._cfgUnqNames;
/*  396 */     if (addQuotes) {
/*  397 */       if (this._outputTail >= this._outputEnd) {
/*  398 */         _flushBuffer();
/*      */       }
/*  400 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*  402 */     _writeBytes(name.asQuotedUTF8());
/*  403 */     if (addQuotes) {
/*  404 */       if (this._outputTail >= this._outputEnd) {
/*  405 */         _flushBuffer();
/*      */       }
/*  407 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException
/*      */   {
/*  420 */     _verifyValueWrite("write text value");
/*  421 */     if (text == null) {
/*  422 */       _writeNull();
/*  423 */       return;
/*      */     }
/*      */     
/*  426 */     int len = text.length();
/*  427 */     if (len > this._charBufferLength) {
/*  428 */       _writeStringSegments(text, true);
/*  429 */       return;
/*      */     }
/*      */     
/*  432 */     text.getChars(0, len, this._charBuffer, 0);
/*      */     
/*  434 */     if (len > this._outputMaxContiguous) {
/*  435 */       _writeLongString(this._charBuffer, 0, len);
/*  436 */       return;
/*      */     }
/*  438 */     if (this._outputTail + len >= this._outputEnd) {
/*  439 */       _flushBuffer();
/*      */     }
/*  441 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  442 */     _writeStringSegment(this._charBuffer, 0, len);
/*      */     
/*      */ 
/*      */ 
/*  446 */     if (this._outputTail >= this._outputEnd) {
/*  447 */       _flushBuffer();
/*      */     }
/*  449 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   private void _writeLongString(char[] text, int offset, int len) throws IOException
/*      */   {
/*  454 */     if (this._outputTail >= this._outputEnd) {
/*  455 */       _flushBuffer();
/*      */     }
/*  457 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  458 */     _writeStringSegments(text, 0, len);
/*  459 */     if (this._outputTail >= this._outputEnd) {
/*  460 */       _flushBuffer();
/*      */     }
/*  462 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   public void writeString(char[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  468 */     _verifyValueWrite("write text value");
/*  469 */     if (this._outputTail >= this._outputEnd) {
/*  470 */       _flushBuffer();
/*      */     }
/*  472 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */     
/*  474 */     if (len <= this._outputMaxContiguous) {
/*  475 */       if (this._outputTail + len > this._outputEnd) {
/*  476 */         _flushBuffer();
/*      */       }
/*  478 */       _writeStringSegment(text, offset, len);
/*      */     } else {
/*  480 */       _writeStringSegments(text, offset, len);
/*      */     }
/*      */     
/*  483 */     if (this._outputTail >= this._outputEnd) {
/*  484 */       _flushBuffer();
/*      */     }
/*  486 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   public final void writeString(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  492 */     _verifyValueWrite("write text value");
/*  493 */     if (this._outputTail >= this._outputEnd) {
/*  494 */       _flushBuffer();
/*      */     }
/*  496 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  497 */     int len = text.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  498 */     if (len < 0) {
/*  499 */       _writeBytes(text.asQuotedUTF8());
/*      */     } else {
/*  501 */       this._outputTail += len;
/*      */     }
/*  503 */     if (this._outputTail >= this._outputEnd) {
/*  504 */       _flushBuffer();
/*      */     }
/*  506 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  512 */     _verifyValueWrite("write text value");
/*  513 */     if (this._outputTail >= this._outputEnd) {
/*  514 */       _flushBuffer();
/*      */     }
/*  516 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  517 */     _writeBytes(text, offset, length);
/*  518 */     if (this._outputTail >= this._outputEnd) {
/*  519 */       _flushBuffer();
/*      */     }
/*  521 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */   public void writeUTF8String(byte[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  527 */     _verifyValueWrite("write text value");
/*  528 */     if (this._outputTail >= this._outputEnd) {
/*  529 */       _flushBuffer();
/*      */     }
/*  531 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */     
/*  533 */     if (len <= this._outputMaxContiguous) {
/*  534 */       _writeUTF8Segment(text, offset, len);
/*      */     } else {
/*  536 */       _writeUTF8Segments(text, offset, len);
/*      */     }
/*  538 */     if (this._outputTail >= this._outputEnd) {
/*  539 */       _flushBuffer();
/*      */     }
/*  541 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeRaw(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  554 */     int start = 0;
/*  555 */     int len = text.length();
/*  556 */     while (len > 0) {
/*  557 */       char[] buf = this._charBuffer;
/*  558 */       int blen = buf.length;
/*  559 */       int len2 = len < blen ? len : blen;
/*  560 */       text.getChars(start, start + len2, buf, 0);
/*  561 */       writeRaw(buf, 0, len2);
/*  562 */       start += len2;
/*  563 */       len -= len2;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeRaw(String text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  571 */     while (len > 0) {
/*  572 */       char[] buf = this._charBuffer;
/*  573 */       int blen = buf.length;
/*  574 */       int len2 = len < blen ? len : blen;
/*  575 */       text.getChars(offset, offset + len2, buf, 0);
/*  576 */       writeRaw(buf, 0, len2);
/*  577 */       offset += len2;
/*  578 */       len -= len2;
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRaw(SerializableString text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  585 */     byte[] raw = text.asUnquotedUTF8();
/*  586 */     if (raw.length > 0) {
/*  587 */       _writeBytes(raw);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRawValue(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  594 */     _verifyValueWrite("write raw value");
/*  595 */     byte[] raw = text.asUnquotedUTF8();
/*  596 */     if (raw.length > 0) {
/*  597 */       _writeBytes(raw);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  608 */     int len3 = len + len + len;
/*  609 */     if (this._outputTail + len3 > this._outputEnd)
/*      */     {
/*  611 */       if (this._outputEnd < len3) {
/*  612 */         _writeSegmentedRaw(cbuf, offset, len);
/*  613 */         return;
/*      */       }
/*      */       
/*  616 */       _flushBuffer();
/*      */     }
/*      */     
/*  619 */     len += offset;
/*      */     
/*      */ 
/*      */ 
/*  623 */     while (offset < len)
/*      */     {
/*      */       for (;;) {
/*  626 */         int ch = cbuf[offset];
/*  627 */         if (ch > 127) {
/*      */           break;
/*      */         }
/*  630 */         this._outputBuffer[(this._outputTail++)] = ((byte)ch);
/*  631 */         offset++; if (offset >= len) {
/*      */           return;
/*      */         }
/*      */       }
/*  635 */       char ch = cbuf[(offset++)];
/*  636 */       if (ch < 'ࠀ') {
/*  637 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  638 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/*  640 */         offset = _outputRawMultiByteChar(ch, cbuf, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeRaw(char ch)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  649 */     if (this._outputTail + 3 >= this._outputEnd) {
/*  650 */       _flushBuffer();
/*      */     }
/*  652 */     byte[] bbuf = this._outputBuffer;
/*  653 */     if (ch <= '') {
/*  654 */       bbuf[(this._outputTail++)] = ((byte)ch);
/*  655 */     } else if (ch < 'ࠀ') {
/*  656 */       bbuf[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  657 */       bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */     } else {
/*  659 */       _outputRawMultiByteChar(ch, null, 0, 0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeSegmentedRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  670 */     int end = this._outputEnd;
/*  671 */     byte[] bbuf = this._outputBuffer;
/*      */     
/*      */ 
/*  674 */     while (offset < len)
/*      */     {
/*      */       for (;;) {
/*  677 */         int ch = cbuf[offset];
/*  678 */         if (ch >= 128) {
/*      */           break;
/*      */         }
/*      */         
/*  682 */         if (this._outputTail >= end) {
/*  683 */           _flushBuffer();
/*      */         }
/*  685 */         bbuf[(this._outputTail++)] = ((byte)ch);
/*  686 */         offset++; if (offset >= len) {
/*      */           return;
/*      */         }
/*      */       }
/*  690 */       if (this._outputTail + 3 >= this._outputEnd) {
/*  691 */         _flushBuffer();
/*      */       }
/*  693 */       char ch = cbuf[(offset++)];
/*  694 */       if (ch < 'ࠀ') {
/*  695 */         bbuf[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  696 */         bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/*  698 */         offset = _outputRawMultiByteChar(ch, cbuf, offset, len);
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
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  714 */     _verifyValueWrite("write binary value");
/*      */     
/*  716 */     if (this._outputTail >= this._outputEnd) {
/*  717 */       _flushBuffer();
/*      */     }
/*  719 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  720 */     _writeBinary(b64variant, data, offset, offset + len);
/*      */     
/*  722 */     if (this._outputTail >= this._outputEnd) {
/*  723 */       _flushBuffer();
/*      */     }
/*  725 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  733 */     _verifyValueWrite("write binary value");
/*      */     
/*  735 */     if (this._outputTail >= this._outputEnd) {
/*  736 */       _flushBuffer();
/*      */     }
/*  738 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  739 */     byte[] encodingBuffer = this._ioContext.allocBase64Buffer();
/*      */     int bytes;
/*      */     try { int bytes;
/*  742 */       if (dataLength < 0) {
/*  743 */         bytes = _writeBinary(b64variant, data, encodingBuffer);
/*      */       } else {
/*  745 */         int missing = _writeBinary(b64variant, data, encodingBuffer, dataLength);
/*  746 */         if (missing > 0) {
/*  747 */           _reportError("Too few bytes available: missing " + missing + " bytes (out of " + dataLength + ")");
/*      */         }
/*  749 */         bytes = dataLength;
/*      */       }
/*      */     } finally {
/*  752 */       this._ioContext.releaseBase64Buffer(encodingBuffer);
/*      */     }
/*      */     
/*  755 */     if (this._outputTail >= this._outputEnd) {
/*  756 */       _flushBuffer();
/*      */     }
/*  758 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  759 */     return bytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeNumber(short s)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  772 */     _verifyValueWrite("write number");
/*      */     
/*  774 */     if (this._outputTail + 6 >= this._outputEnd) {
/*  775 */       _flushBuffer();
/*      */     }
/*  777 */     if (this._cfgNumbersAsStrings) {
/*  778 */       _writeQuotedShort(s);
/*  779 */       return;
/*      */     }
/*  781 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedShort(short s) throws IOException {
/*  785 */     if (this._outputTail + 8 >= this._outputEnd) {
/*  786 */       _flushBuffer();
/*      */     }
/*  788 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  789 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*  790 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(int i)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  797 */     _verifyValueWrite("write number");
/*      */     
/*  799 */     if (this._outputTail + 11 >= this._outputEnd) {
/*  800 */       _flushBuffer();
/*      */     }
/*  802 */     if (this._cfgNumbersAsStrings) {
/*  803 */       _writeQuotedInt(i);
/*  804 */       return;
/*      */     }
/*  806 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedInt(int i) throws IOException
/*      */   {
/*  811 */     if (this._outputTail + 13 >= this._outputEnd) {
/*  812 */       _flushBuffer();
/*      */     }
/*  814 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  815 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*  816 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(long l)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  823 */     _verifyValueWrite("write number");
/*  824 */     if (this._cfgNumbersAsStrings) {
/*  825 */       _writeQuotedLong(l);
/*  826 */       return;
/*      */     }
/*  828 */     if (this._outputTail + 21 >= this._outputEnd)
/*      */     {
/*  830 */       _flushBuffer();
/*      */     }
/*  832 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedLong(long l) throws IOException
/*      */   {
/*  837 */     if (this._outputTail + 23 >= this._outputEnd) {
/*  838 */       _flushBuffer();
/*      */     }
/*  840 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  841 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*  842 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(BigInteger value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  849 */     _verifyValueWrite("write number");
/*  850 */     if (value == null) {
/*  851 */       _writeNull();
/*  852 */     } else if (this._cfgNumbersAsStrings) {
/*  853 */       _writeQuotedRaw(value.toString());
/*      */     } else {
/*  855 */       writeRaw(value.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeNumber(double d)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  864 */     if ((this._cfgNumbersAsStrings) || (((Double.isNaN(d)) || (Double.isInfinite(d))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*      */ 
/*      */ 
/*  868 */       writeString(String.valueOf(d));
/*  869 */       return;
/*      */     }
/*      */     
/*  872 */     _verifyValueWrite("write number");
/*  873 */     writeRaw(String.valueOf(d));
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(float f)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  880 */     if ((this._cfgNumbersAsStrings) || (((Float.isNaN(f)) || (Float.isInfinite(f))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*      */ 
/*      */ 
/*  884 */       writeString(String.valueOf(f));
/*  885 */       return;
/*      */     }
/*      */     
/*  888 */     _verifyValueWrite("write number");
/*  889 */     writeRaw(String.valueOf(f));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeNumber(BigDecimal value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  897 */     _verifyValueWrite("write number");
/*  898 */     if (value == null) {
/*  899 */       _writeNull();
/*  900 */     } else if (this._cfgNumbersAsStrings) {
/*  901 */       String raw = isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN) ? value.toPlainString() : value.toString();
/*  902 */       _writeQuotedRaw(raw);
/*  903 */     } else if (isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)) {
/*  904 */       writeRaw(value.toPlainString());
/*      */     } else {
/*  906 */       writeRaw(value.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  914 */     _verifyValueWrite("write number");
/*  915 */     if (this._cfgNumbersAsStrings) {
/*  916 */       _writeQuotedRaw(encodedValue);
/*      */     } else {
/*  918 */       writeRaw(encodedValue);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _writeQuotedRaw(String value) throws IOException
/*      */   {
/*  924 */     if (this._outputTail >= this._outputEnd) {
/*  925 */       _flushBuffer();
/*      */     }
/*  927 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  928 */     writeRaw(value);
/*  929 */     if (this._outputTail >= this._outputEnd) {
/*  930 */       _flushBuffer();
/*      */     }
/*  932 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeBoolean(boolean state)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  939 */     _verifyValueWrite("write boolean value");
/*  940 */     if (this._outputTail + 5 >= this._outputEnd) {
/*  941 */       _flushBuffer();
/*      */     }
/*  943 */     byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
/*  944 */     int len = keyword.length;
/*  945 */     System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
/*  946 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNull()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  953 */     _verifyValueWrite("write null value");
/*  954 */     _writeNull();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _verifyValueWrite(String typeMsg)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  967 */     int status = this._writeContext.writeValue();
/*  968 */     if (status == 5) {
/*  969 */       _reportError("Can not " + typeMsg + ", expecting field name");
/*      */     }
/*  971 */     if (this._cfgPrettyPrinter == null) {
/*      */       byte b;
/*  973 */       switch (status) {
/*      */       case 1: 
/*  975 */         b = 44;
/*  976 */         break;
/*      */       case 2: 
/*  978 */         b = 58;
/*  979 */         break;
/*      */       case 3: 
/*  981 */         if (this._rootValueSeparator != null) {
/*  982 */           byte[] raw = this._rootValueSeparator.asUnquotedUTF8();
/*  983 */           if (raw.length > 0) {
/*  984 */             _writeBytes(raw);
/*      */           }
/*      */         }
/*  987 */         return;
/*      */       case 0: 
/*      */       default: 
/*  990 */         return;
/*      */       }
/*  992 */       if (this._outputTail >= this._outputEnd) {
/*  993 */         _flushBuffer();
/*      */       }
/*  995 */       this._outputBuffer[this._outputTail] = b;
/*  996 */       this._outputTail += 1;
/*  997 */       return;
/*      */     }
/*      */     
/* 1000 */     _verifyPrettyValueWrite(typeMsg, status);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void _verifyPrettyValueWrite(String typeMsg, int status)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1007 */     switch (status) {
/*      */     case 1: 
/* 1009 */       this._cfgPrettyPrinter.writeArrayValueSeparator(this);
/* 1010 */       break;
/*      */     case 2: 
/* 1012 */       this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
/* 1013 */       break;
/*      */     case 3: 
/* 1015 */       this._cfgPrettyPrinter.writeRootValueSeparator(this);
/* 1016 */       break;
/*      */     
/*      */     case 0: 
/* 1019 */       if (this._writeContext.inArray()) {
/* 1020 */         this._cfgPrettyPrinter.beforeArrayValues(this);
/* 1021 */       } else if (this._writeContext.inObject()) {
/* 1022 */         this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */       }
/*      */       break;
/*      */     default: 
/* 1026 */       _throwInternal();
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
/*      */   public void flush()
/*      */     throws IOException
/*      */   {
/* 1041 */     _flushBuffer();
/* 1042 */     if ((this._outputStream != null) && 
/* 1043 */       (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))) {
/* 1044 */       this._outputStream.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/* 1053 */     super.close();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1059 */     if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT))) {
/*      */       for (;;)
/*      */       {
/* 1062 */         com.fasterxml.jackson.core.JsonStreamContext ctxt = getOutputContext();
/* 1063 */         if (ctxt.inArray()) {
/* 1064 */           writeEndArray();
/* 1065 */         } else { if (!ctxt.inObject()) break;
/* 1066 */           writeEndObject();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1072 */     _flushBuffer();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1080 */     if (this._outputStream != null) {
/* 1081 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET))) {
/* 1082 */         this._outputStream.close();
/* 1083 */       } else if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))
/*      */       {
/* 1085 */         this._outputStream.flush();
/*      */       }
/*      */     }
/*      */     
/* 1089 */     _releaseBuffers();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */   {
/* 1095 */     byte[] buf = this._outputBuffer;
/* 1096 */     if ((buf != null) && (this._bufferRecyclable)) {
/* 1097 */       this._outputBuffer = null;
/* 1098 */       this._ioContext.releaseWriteEncodingBuffer(buf);
/*      */     }
/* 1100 */     char[] cbuf = this._charBuffer;
/* 1101 */     if (cbuf != null) {
/* 1102 */       this._charBuffer = null;
/* 1103 */       this._ioContext.releaseConcatBuffer(cbuf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeBytes(byte[] bytes)
/*      */     throws IOException
/*      */   {
/* 1115 */     int len = bytes.length;
/* 1116 */     if (this._outputTail + len > this._outputEnd) {
/* 1117 */       _flushBuffer();
/*      */       
/* 1119 */       if (len > 512) {
/* 1120 */         this._outputStream.write(bytes, 0, len);
/* 1121 */         return;
/*      */       }
/*      */     }
/* 1124 */     System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
/* 1125 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException
/*      */   {
/* 1130 */     if (this._outputTail + len > this._outputEnd) {
/* 1131 */       _flushBuffer();
/*      */       
/* 1133 */       if (len > 512) {
/* 1134 */         this._outputStream.write(bytes, offset, len);
/* 1135 */         return;
/*      */       }
/*      */     }
/* 1138 */     System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
/* 1139 */     this._outputTail += len;
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
/*      */   private final void _writeStringSegments(String text, boolean addQuotes)
/*      */     throws IOException
/*      */   {
/* 1157 */     if (addQuotes) {
/* 1158 */       if (this._outputTail >= this._outputEnd) {
/* 1159 */         _flushBuffer();
/*      */       }
/* 1161 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*      */     
/* 1164 */     int left = text.length();
/* 1165 */     int offset = 0;
/* 1166 */     char[] cbuf = this._charBuffer;
/*      */     
/* 1168 */     while (left > 0) {
/* 1169 */       int len = Math.min(this._outputMaxContiguous, left);
/* 1170 */       text.getChars(offset, offset + len, cbuf, 0);
/* 1171 */       if (this._outputTail + len > this._outputEnd) {
/* 1172 */         _flushBuffer();
/*      */       }
/* 1174 */       _writeStringSegment(cbuf, 0, len);
/* 1175 */       offset += len;
/* 1176 */       left -= len;
/*      */     }
/*      */     
/* 1179 */     if (addQuotes) {
/* 1180 */       if (this._outputTail >= this._outputEnd) {
/* 1181 */         _flushBuffer();
/*      */       }
/* 1183 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeStringSegments(char[] cbuf, int offset, int totalLen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*      */     do
/*      */     {
/* 1197 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1198 */       if (this._outputTail + len > this._outputEnd) {
/* 1199 */         _flushBuffer();
/*      */       }
/* 1201 */       _writeStringSegment(cbuf, offset, len);
/* 1202 */       offset += len;
/* 1203 */       totalLen -= len;
/* 1204 */     } while (totalLen > 0);
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
/*      */   private final void _writeStringSegment(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1227 */     len += offset;
/*      */     
/* 1229 */     int outputPtr = this._outputTail;
/* 1230 */     byte[] outputBuffer = this._outputBuffer;
/* 1231 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1233 */     while (offset < len) {
/* 1234 */       int ch = cbuf[offset];
/*      */       
/* 1236 */       if ((ch > 127) || (escCodes[ch] != 0)) {
/*      */         break;
/*      */       }
/* 1239 */       outputBuffer[(outputPtr++)] = ((byte)ch);
/* 1240 */       offset++;
/*      */     }
/* 1242 */     this._outputTail = outputPtr;
/* 1243 */     if (offset < len)
/*      */     {
/* 1245 */       if (this._characterEscapes != null) {
/* 1246 */         _writeCustomStringSegment2(cbuf, offset, len);
/*      */       }
/* 1248 */       else if (this._maximumNonEscapedChar == 0) {
/* 1249 */         _writeStringSegment2(cbuf, offset, len);
/*      */       } else {
/* 1251 */         _writeStringSegmentASCII2(cbuf, offset, len);
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
/*      */   private final void _writeStringSegment2(char[] cbuf, int offset, int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1265 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1266 */       _flushBuffer();
/*      */     }
/*      */     
/* 1269 */     int outputPtr = this._outputTail;
/*      */     
/* 1271 */     byte[] outputBuffer = this._outputBuffer;
/* 1272 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1274 */     while (offset < end) {
/* 1275 */       int ch = cbuf[(offset++)];
/* 1276 */       if (ch <= 127) {
/* 1277 */         if (escCodes[ch] == 0) {
/* 1278 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1281 */           int escape = escCodes[ch];
/* 1282 */           if (escape > 0) {
/* 1283 */             outputBuffer[(outputPtr++)] = 92;
/* 1284 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1287 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1291 */       else if (ch <= 2047) {
/* 1292 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1293 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1295 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1298 */     this._outputTail = outputPtr;
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
/*      */   private final void _writeStringSegmentASCII2(char[] cbuf, int offset, int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1316 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1317 */       _flushBuffer();
/*      */     }
/*      */     
/* 1320 */     int outputPtr = this._outputTail;
/*      */     
/* 1322 */     byte[] outputBuffer = this._outputBuffer;
/* 1323 */     int[] escCodes = this._outputEscapes;
/* 1324 */     int maxUnescaped = this._maximumNonEscapedChar;
/*      */     
/* 1326 */     while (offset < end) {
/* 1327 */       int ch = cbuf[(offset++)];
/* 1328 */       if (ch <= 127) {
/* 1329 */         if (escCodes[ch] == 0) {
/* 1330 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1333 */           int escape = escCodes[ch];
/* 1334 */           if (escape > 0) {
/* 1335 */             outputBuffer[(outputPtr++)] = 92;
/* 1336 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1339 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1343 */       else if (ch > maxUnescaped) {
/* 1344 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */ 
/*      */       }
/* 1347 */       else if (ch <= 2047) {
/* 1348 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1349 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1351 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1354 */     this._outputTail = outputPtr;
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
/*      */   private final void _writeCustomStringSegment2(char[] cbuf, int offset, int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1372 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1373 */       _flushBuffer();
/*      */     }
/* 1375 */     int outputPtr = this._outputTail;
/*      */     
/* 1377 */     byte[] outputBuffer = this._outputBuffer;
/* 1378 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1380 */     int maxUnescaped = this._maximumNonEscapedChar <= 0 ? 65535 : this._maximumNonEscapedChar;
/* 1381 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/* 1383 */     while (offset < end) {
/* 1384 */       int ch = cbuf[(offset++)];
/* 1385 */       if (ch <= 127) {
/* 1386 */         if (escCodes[ch] == 0) {
/* 1387 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1390 */           int escape = escCodes[ch];
/* 1391 */           if (escape > 0) {
/* 1392 */             outputBuffer[(outputPtr++)] = 92;
/* 1393 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/* 1394 */           } else if (escape == -2) {
/* 1395 */             SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1396 */             if (esc == null) {
/* 1397 */               _reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
/*      */             }
/*      */             
/* 1400 */             outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */           }
/*      */           else {
/* 1403 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1407 */       else if (ch > maxUnescaped) {
/* 1408 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */       }
/*      */       else {
/* 1411 */         SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1412 */         if (esc != null) {
/* 1413 */           outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */ 
/*      */         }
/* 1416 */         else if (ch <= 2047) {
/* 1417 */           outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1418 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */         } else {
/* 1420 */           outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */         }
/*      */       } }
/* 1423 */     this._outputTail = outputPtr;
/*      */   }
/*      */   
/*      */   private final int _writeCustomEscape(byte[] outputBuffer, int outputPtr, SerializableString esc, int remainingChars)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1429 */     byte[] raw = esc.asUnquotedUTF8();
/* 1430 */     int len = raw.length;
/* 1431 */     if (len > 6) {
/* 1432 */       return _handleLongCustomEscape(outputBuffer, outputPtr, this._outputEnd, raw, remainingChars);
/*      */     }
/*      */     
/* 1435 */     System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
/* 1436 */     return outputPtr + len;
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _handleLongCustomEscape(byte[] outputBuffer, int outputPtr, int outputEnd, byte[] raw, int remainingChars)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1443 */     int len = raw.length;
/* 1444 */     if (outputPtr + len > outputEnd) {
/* 1445 */       this._outputTail = outputPtr;
/* 1446 */       _flushBuffer();
/* 1447 */       outputPtr = this._outputTail;
/* 1448 */       if (len > outputBuffer.length) {
/* 1449 */         this._outputStream.write(raw, 0, len);
/* 1450 */         return outputPtr;
/*      */       }
/* 1452 */       System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
/* 1453 */       outputPtr += len;
/*      */     }
/*      */     
/* 1456 */     if (outputPtr + 6 * remainingChars > outputEnd) {
/* 1457 */       _flushBuffer();
/* 1458 */       return this._outputTail;
/*      */     }
/* 1460 */     return outputPtr;
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
/*      */   private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*      */     do
/*      */     {
/* 1478 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1479 */       _writeUTF8Segment(utf8, offset, len);
/* 1480 */       offset += len;
/* 1481 */       totalLen -= len;
/* 1482 */     } while (totalLen > 0);
/*      */   }
/*      */   
/*      */ 
/*      */   private final void _writeUTF8Segment(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1489 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1491 */     int ptr = offset; for (int end = offset + len; ptr < end;)
/*      */     {
/* 1493 */       int ch = utf8[(ptr++)];
/* 1494 */       if ((ch >= 0) && (escCodes[ch] != 0)) {
/* 1495 */         _writeUTF8Segment2(utf8, offset, len);
/* 1496 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1501 */     if (this._outputTail + len > this._outputEnd) {
/* 1502 */       _flushBuffer();
/*      */     }
/* 1504 */     System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
/* 1505 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   private final void _writeUTF8Segment2(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1511 */     int outputPtr = this._outputTail;
/*      */     
/*      */ 
/* 1514 */     if (outputPtr + len * 6 > this._outputEnd) {
/* 1515 */       _flushBuffer();
/* 1516 */       outputPtr = this._outputTail;
/*      */     }
/*      */     
/* 1519 */     byte[] outputBuffer = this._outputBuffer;
/* 1520 */     int[] escCodes = this._outputEscapes;
/* 1521 */     len += offset;
/*      */     
/* 1523 */     while (offset < len) {
/* 1524 */       byte b = utf8[(offset++)];
/* 1525 */       int ch = b;
/* 1526 */       if ((ch < 0) || (escCodes[ch] == 0)) {
/* 1527 */         outputBuffer[(outputPtr++)] = b;
/*      */       }
/*      */       else {
/* 1530 */         int escape = escCodes[ch];
/* 1531 */         if (escape > 0) {
/* 1532 */           outputBuffer[(outputPtr++)] = 92;
/* 1533 */           outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */         }
/*      */         else {
/* 1536 */           outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */         }
/*      */       } }
/* 1539 */     this._outputTail = outputPtr;
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
/*      */   protected final void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1553 */     int safeInputEnd = inputEnd - 3;
/*      */     
/* 1555 */     int safeOutputEnd = this._outputEnd - 6;
/* 1556 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */ 
/* 1559 */     while (inputPtr <= safeInputEnd) {
/* 1560 */       if (this._outputTail > safeOutputEnd) {
/* 1561 */         _flushBuffer();
/*      */       }
/*      */       
/* 1564 */       int b24 = input[(inputPtr++)] << 8;
/* 1565 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 1566 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 1567 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1568 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*      */       {
/* 1570 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1571 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1572 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1577 */     int inputLeft = inputEnd - inputPtr;
/* 1578 */     if (inputLeft > 0) {
/* 1579 */       if (this._outputTail > safeOutputEnd) {
/* 1580 */         _flushBuffer();
/*      */       }
/* 1582 */       int b24 = input[(inputPtr++)] << 16;
/* 1583 */       if (inputLeft == 2) {
/* 1584 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*      */       }
/* 1586 */       this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer, int bytesLeft)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1595 */     int inputPtr = 0;
/* 1596 */     int inputEnd = 0;
/* 1597 */     int lastFullOffset = -3;
/*      */     
/*      */ 
/* 1600 */     int safeOutputEnd = this._outputEnd - 6;
/* 1601 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/* 1603 */     while (bytesLeft > 2) {
/* 1604 */       if (inputPtr > lastFullOffset) {
/* 1605 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1606 */         inputPtr = 0;
/* 1607 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1610 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1612 */       if (this._outputTail > safeOutputEnd) {
/* 1613 */         _flushBuffer();
/*      */       }
/* 1615 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1616 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1617 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1618 */       bytesLeft -= 3;
/* 1619 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1620 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1621 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1622 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1623 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1628 */     if (bytesLeft > 0) {
/* 1629 */       inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1630 */       inputPtr = 0;
/* 1631 */       if (inputEnd > 0) {
/* 1632 */         if (this._outputTail > safeOutputEnd) {
/* 1633 */           _flushBuffer();
/*      */         }
/* 1635 */         int b24 = readBuffer[(inputPtr++)] << 16;
/*      */         int amount;
/* 1637 */         int amount; if (inputPtr < inputEnd) {
/* 1638 */           b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1639 */           amount = 2;
/*      */         } else {
/* 1641 */           amount = 1;
/*      */         }
/* 1643 */         this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/* 1644 */         bytesLeft -= amount;
/*      */       }
/*      */     }
/* 1647 */     return bytesLeft;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1655 */     int inputPtr = 0;
/* 1656 */     int inputEnd = 0;
/* 1657 */     int lastFullOffset = -3;
/* 1658 */     int bytesDone = 0;
/*      */     
/*      */ 
/* 1661 */     int safeOutputEnd = this._outputEnd - 6;
/* 1662 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1666 */       if (inputPtr > lastFullOffset) {
/* 1667 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, readBuffer.length);
/* 1668 */         inputPtr = 0;
/* 1669 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1672 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1674 */       if (this._outputTail > safeOutputEnd) {
/* 1675 */         _flushBuffer();
/*      */       }
/*      */       
/* 1678 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1679 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1680 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1681 */       bytesDone += 3;
/* 1682 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1683 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1684 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1685 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1686 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1691 */     if (inputPtr < inputEnd) {
/* 1692 */       if (this._outputTail > safeOutputEnd) {
/* 1693 */         _flushBuffer();
/*      */       }
/* 1695 */       int b24 = readBuffer[(inputPtr++)] << 16;
/* 1696 */       int amount = 1;
/* 1697 */       if (inputPtr < inputEnd) {
/* 1698 */         b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1699 */         amount = 2;
/*      */       }
/* 1701 */       bytesDone += amount;
/* 1702 */       this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/*      */     }
/* 1704 */     return bytesDone;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final int _readMore(InputStream in, byte[] readBuffer, int inputPtr, int inputEnd, int maxRead)
/*      */     throws IOException
/*      */   {
/* 1712 */     int i = 0;
/* 1713 */     while (inputPtr < inputEnd) {
/* 1714 */       readBuffer[(i++)] = readBuffer[(inputPtr++)];
/*      */     }
/* 1716 */     inputPtr = 0;
/* 1717 */     inputEnd = i;
/* 1718 */     maxRead = Math.min(maxRead, readBuffer.length);
/*      */     do
/*      */     {
/* 1721 */       int length = maxRead - inputEnd;
/* 1722 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 1725 */       int count = in.read(readBuffer, inputEnd, length);
/* 1726 */       if (count < 0) {
/* 1727 */         return inputEnd;
/*      */       }
/* 1729 */       inputEnd += count;
/* 1730 */     } while (inputEnd < 3);
/* 1731 */     return inputEnd;
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
/*      */   private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen)
/*      */     throws IOException
/*      */   {
/* 1749 */     if ((ch >= 55296) && 
/* 1750 */       (ch <= 57343))
/*      */     {
/* 1752 */       if ((inputOffset >= inputLen) || (cbuf == null)) {
/* 1753 */         _reportError("Split surrogate on writeRaw() input (last character)");
/*      */       }
/* 1755 */       _outputSurrogates(ch, cbuf[inputOffset]);
/* 1756 */       return inputOffset + 1;
/*      */     }
/*      */     
/* 1759 */     byte[] bbuf = this._outputBuffer;
/* 1760 */     bbuf[(this._outputTail++)] = ((byte)(0xE0 | ch >> 12));
/* 1761 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 1762 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/* 1763 */     return inputOffset;
/*      */   }
/*      */   
/*      */   protected final void _outputSurrogates(int surr1, int surr2)
/*      */     throws IOException
/*      */   {
/* 1769 */     int c = _decodeSurrogate(surr1, surr2);
/* 1770 */     if (this._outputTail + 4 > this._outputEnd) {
/* 1771 */       _flushBuffer();
/*      */     }
/* 1773 */     byte[] bbuf = this._outputBuffer;
/* 1774 */     bbuf[(this._outputTail++)] = ((byte)(0xF0 | c >> 18));
/* 1775 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c >> 12 & 0x3F));
/* 1776 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c >> 6 & 0x3F));
/* 1777 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c & 0x3F));
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
/*      */   private final int _outputMultiByteChar(int ch, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 1791 */     byte[] bbuf = this._outputBuffer;
/* 1792 */     if ((ch >= 55296) && (ch <= 57343)) {
/* 1793 */       bbuf[(outputPtr++)] = 92;
/* 1794 */       bbuf[(outputPtr++)] = 117;
/*      */       
/* 1796 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 12 & 0xF)];
/* 1797 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 8 & 0xF)];
/* 1798 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 4 & 0xF)];
/* 1799 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch & 0xF)];
/*      */     } else {
/* 1801 */       bbuf[(outputPtr++)] = ((byte)(0xE0 | ch >> 12));
/* 1802 */       bbuf[(outputPtr++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 1803 */       bbuf[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */     }
/* 1805 */     return outputPtr;
/*      */   }
/*      */   
/*      */   private final void _writeNull() throws IOException
/*      */   {
/* 1810 */     if (this._outputTail + 4 >= this._outputEnd) {
/* 1811 */       _flushBuffer();
/*      */     }
/* 1813 */     System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
/* 1814 */     this._outputTail += 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int _writeGenericEscape(int charToEscape, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 1825 */     byte[] bbuf = this._outputBuffer;
/* 1826 */     bbuf[(outputPtr++)] = 92;
/* 1827 */     bbuf[(outputPtr++)] = 117;
/* 1828 */     if (charToEscape > 255) {
/* 1829 */       int hi = charToEscape >> 8 & 0xFF;
/* 1830 */       bbuf[(outputPtr++)] = HEX_CHARS[(hi >> 4)];
/* 1831 */       bbuf[(outputPtr++)] = HEX_CHARS[(hi & 0xF)];
/* 1832 */       charToEscape &= 0xFF;
/*      */     } else {
/* 1834 */       bbuf[(outputPtr++)] = 48;
/* 1835 */       bbuf[(outputPtr++)] = 48;
/*      */     }
/*      */     
/* 1838 */     bbuf[(outputPtr++)] = HEX_CHARS[(charToEscape >> 4)];
/* 1839 */     bbuf[(outputPtr++)] = HEX_CHARS[(charToEscape & 0xF)];
/* 1840 */     return outputPtr;
/*      */   }
/*      */   
/*      */   protected final void _flushBuffer() throws IOException
/*      */   {
/* 1845 */     int len = this._outputTail;
/* 1846 */     if (len > 0) {
/* 1847 */       this._outputTail = 0;
/* 1848 */       this._outputStream.write(this._outputBuffer, 0, len);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\UTF8JsonGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */