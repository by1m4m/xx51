/*      */ package com.fasterxml.jackson.core.base;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.core.json.DupDetector;
/*      */ import com.fasterxml.jackson.core.json.JsonReadContext;
/*      */ import com.fasterxml.jackson.core.json.PackageVersion;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ParserBase
/*      */   extends ParserMinimalBase
/*      */ {
/*      */   protected final IOContext _ioContext;
/*      */   protected boolean _closed;
/*   53 */   protected int _inputPtr = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   58 */   protected int _inputEnd = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   70 */   protected long _currInputProcessed = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   76 */   protected int _currInputRow = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   84 */   protected int _currInputRowStart = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  100 */   protected long _tokenInputTotal = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  105 */   protected int _tokenInputRow = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  111 */   protected int _tokenInputCol = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonReadContext _parsingContext;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _nextToken;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TextBuffer _textBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */   protected char[] _nameCopyBuffer = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  157 */   protected boolean _nameCopied = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  163 */   protected ByteArrayBuilder _byteArrayBuilder = null;
/*      */   
/*      */ 
/*      */ 
/*      */   protected byte[] _binaryValue;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_UNKNOWN = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_INT = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_LONG = 2;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_BIGINT = 4;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_DOUBLE = 8;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final int NR_BIGDECIMAL = 16;
/*      */   
/*      */ 
/*  194 */   static final BigInteger BI_MIN_INT = BigInteger.valueOf(-2147483648L);
/*  195 */   static final BigInteger BI_MAX_INT = BigInteger.valueOf(2147483647L);
/*      */   
/*  197 */   static final BigInteger BI_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/*  198 */   static final BigInteger BI_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*      */   
/*  200 */   static final BigDecimal BD_MIN_LONG = new BigDecimal(BI_MIN_LONG);
/*  201 */   static final BigDecimal BD_MAX_LONG = new BigDecimal(BI_MAX_LONG);
/*      */   
/*  203 */   static final BigDecimal BD_MIN_INT = new BigDecimal(BI_MIN_INT);
/*  204 */   static final BigDecimal BD_MAX_INT = new BigDecimal(BI_MAX_INT);
/*      */   
/*      */ 
/*      */   static final long MIN_INT_L = -2147483648L;
/*      */   
/*      */ 
/*      */   static final long MAX_INT_L = 2147483647L;
/*      */   
/*      */ 
/*      */   static final double MIN_LONG_D = -9.223372036854776E18D;
/*      */   
/*      */ 
/*      */   static final double MAX_LONG_D = 9.223372036854776E18D;
/*      */   
/*      */ 
/*      */   static final double MIN_INT_D = -2.147483648E9D;
/*      */   
/*      */   static final double MAX_INT_D = 2.147483647E9D;
/*      */   
/*      */   protected static final int INT_0 = 48;
/*      */   
/*      */   protected static final int INT_9 = 57;
/*      */   
/*      */   protected static final int INT_MINUS = 45;
/*      */   
/*      */   protected static final int INT_PLUS = 43;
/*      */   
/*      */   protected static final char CHAR_NULL = '\000';
/*      */   
/*  233 */   protected int _numTypesValid = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _numberInt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long _numberLong;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected double _numberDouble;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BigInteger _numberBigInt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BigDecimal _numberBigDecimal;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _numberNegative;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _intLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _fractLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _expLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ParserBase(IOContext ctxt, int features)
/*      */   {
/*  285 */     this._features = features;
/*  286 */     this._ioContext = ctxt;
/*  287 */     this._textBuffer = ctxt.constructTextBuffer();
/*  288 */     DupDetector dups = JsonParser.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(features) ? DupDetector.rootDetector(this) : null;
/*      */     
/*  290 */     this._parsingContext = JsonReadContext.createRootContext(dups);
/*      */   }
/*      */   
/*  293 */   public Version version() { return PackageVersion.VERSION; }
/*      */   
/*      */   public Object getCurrentValue()
/*      */   {
/*  297 */     return this._parsingContext.getCurrentValue();
/*      */   }
/*      */   
/*      */   public void setCurrentValue(Object v)
/*      */   {
/*  302 */     this._parsingContext.setCurrentValue(v);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser enable(JsonParser.Feature f)
/*      */   {
/*  313 */     this._features |= f.getMask();
/*  314 */     if ((f == JsonParser.Feature.STRICT_DUPLICATE_DETECTION) && 
/*  315 */       (this._parsingContext.getDupDetector() == null)) {
/*  316 */       this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
/*      */     }
/*      */     
/*  319 */     return this;
/*      */   }
/*      */   
/*      */   public JsonParser disable(JsonParser.Feature f)
/*      */   {
/*  324 */     this._features &= (f.getMask() ^ 0xFFFFFFFF);
/*  325 */     if (f == JsonParser.Feature.STRICT_DUPLICATE_DETECTION) {
/*  326 */       this._parsingContext = this._parsingContext.withDupDetector(null);
/*      */     }
/*  328 */     return this;
/*      */   }
/*      */   
/*      */   public JsonParser setFeatureMask(int newMask)
/*      */   {
/*  333 */     int changes = this._features ^ newMask;
/*  334 */     if (changes != 0) {
/*  335 */       this._features = newMask;
/*  336 */       if (JsonParser.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(newMask)) {
/*  337 */         if (this._parsingContext.getDupDetector() == null) {
/*  338 */           this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
/*      */         }
/*      */       } else {
/*  341 */         this._parsingContext = this._parsingContext.withDupDetector(null);
/*      */       }
/*      */     }
/*  344 */     return this;
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
/*      */   public String getCurrentName()
/*      */     throws IOException
/*      */   {
/*  359 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/*  360 */       JsonReadContext parent = this._parsingContext.getParent();
/*  361 */       return parent.getCurrentName();
/*      */     }
/*  363 */     return this._parsingContext.getCurrentName();
/*      */   }
/*      */   
/*      */   public void overrideCurrentName(String name)
/*      */   {
/*  368 */     JsonReadContext ctxt = this._parsingContext;
/*  369 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/*  370 */       ctxt = ctxt.getParent();
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  376 */       ctxt.setCurrentName(name);
/*      */     } catch (IOException e) {
/*  378 */       throw new IllegalStateException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void close() throws IOException {
/*  383 */     if (!this._closed) {
/*  384 */       this._closed = true;
/*      */       try {
/*  386 */         _closeInput();
/*      */       }
/*      */       finally
/*      */       {
/*  390 */         _releaseBuffers();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  395 */   public boolean isClosed() { return this._closed; }
/*  396 */   public JsonReadContext getParsingContext() { return this._parsingContext; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getTokenLocation()
/*      */   {
/*  405 */     return new JsonLocation(this._ioContext.getSourceReference(), -1L, getTokenCharacterOffset(), getTokenLineNr(), getTokenColumnNr());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getCurrentLocation()
/*      */   {
/*  417 */     int col = this._inputPtr - this._currInputRowStart + 1;
/*  418 */     return new JsonLocation(this._ioContext.getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, col);
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
/*      */   public boolean hasTextCharacters()
/*      */   {
/*  431 */     if (this._currToken == JsonToken.VALUE_STRING) return true;
/*  432 */     if (this._currToken == JsonToken.FIELD_NAME) return this._nameCopied;
/*  433 */     return false;
/*      */   }
/*      */   
/*      */   public Object getEmbeddedObject() throws IOException {
/*  437 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */   public long getTokenCharacterOffset() { return this._tokenInputTotal; }
/*  446 */   public int getTokenLineNr() { return this._tokenInputRow; }
/*      */   
/*      */   public int getTokenColumnNr() {
/*  449 */     int col = this._tokenInputCol;
/*  450 */     return col < 0 ? col : col + 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void loadMoreGuaranteed()
/*      */     throws IOException
/*      */   {
/*  460 */     if (!loadMore()) { _reportInvalidEOF();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract boolean loadMore()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void _finishString()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   protected abstract void _closeInput()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */     throws IOException
/*      */   {
/*  486 */     this._textBuffer.releaseBuffers();
/*  487 */     char[] buf = this._nameCopyBuffer;
/*  488 */     if (buf != null) {
/*  489 */       this._nameCopyBuffer = null;
/*  490 */       this._ioContext.releaseNameCopyBuffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _handleEOF()
/*      */     throws JsonParseException
/*      */   {
/*  501 */     if (!this._parsingContext.inRoot()) {
/*  502 */       _reportInvalidEOF(": expected close marker for " + this._parsingContext.getTypeDesc() + " (from " + this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) + ")");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected final int _eofAsNextChar()
/*      */     throws JsonParseException
/*      */   {
/*  510 */     _handleEOF();
/*  511 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportMismatchedEndMarker(int actCh, char expCh)
/*      */     throws JsonParseException
/*      */   {
/*  521 */     String startDesc = "" + this._parsingContext.getStartLocation(this._ioContext.getSourceReference());
/*  522 */     _reportError("Unexpected close marker '" + (char)actCh + "': expected '" + expCh + "' (for " + this._parsingContext.getTypeDesc() + " starting at " + startDesc + ")");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteArrayBuilder _getByteArrayBuilder()
/*      */   {
/*  533 */     if (this._byteArrayBuilder == null) {
/*  534 */       this._byteArrayBuilder = new ByteArrayBuilder();
/*      */     } else {
/*  536 */       this._byteArrayBuilder.reset();
/*      */     }
/*  538 */     return this._byteArrayBuilder;
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
/*      */   protected final JsonToken reset(boolean negative, int intLen, int fractLen, int expLen)
/*      */   {
/*  551 */     if ((fractLen < 1) && (expLen < 1)) {
/*  552 */       return resetInt(negative, intLen);
/*      */     }
/*  554 */     return resetFloat(negative, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */   protected final JsonToken resetInt(boolean negative, int intLen)
/*      */   {
/*  559 */     this._numberNegative = negative;
/*  560 */     this._intLength = intLen;
/*  561 */     this._fractLength = 0;
/*  562 */     this._expLength = 0;
/*  563 */     this._numTypesValid = 0;
/*  564 */     return JsonToken.VALUE_NUMBER_INT;
/*      */   }
/*      */   
/*      */   protected final JsonToken resetFloat(boolean negative, int intLen, int fractLen, int expLen)
/*      */   {
/*  569 */     this._numberNegative = negative;
/*  570 */     this._intLength = intLen;
/*  571 */     this._fractLength = fractLen;
/*  572 */     this._expLength = expLen;
/*  573 */     this._numTypesValid = 0;
/*  574 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*      */   }
/*      */   
/*      */   protected final JsonToken resetAsNaN(String valueStr, double value)
/*      */   {
/*  579 */     this._textBuffer.resetWithString(valueStr);
/*  580 */     this._numberDouble = value;
/*  581 */     this._numTypesValid = 8;
/*  582 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number getNumberValue()
/*      */     throws IOException
/*      */   {
/*  594 */     if (this._numTypesValid == 0) {
/*  595 */       _parseNumericValue(0);
/*      */     }
/*      */     
/*  598 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  599 */       if ((this._numTypesValid & 0x1) != 0) {
/*  600 */         return Integer.valueOf(this._numberInt);
/*      */       }
/*  602 */       if ((this._numTypesValid & 0x2) != 0) {
/*  603 */         return Long.valueOf(this._numberLong);
/*      */       }
/*  605 */       if ((this._numTypesValid & 0x4) != 0) {
/*  606 */         return this._numberBigInt;
/*      */       }
/*      */       
/*  609 */       return this._numberBigDecimal;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  615 */     if ((this._numTypesValid & 0x10) != 0) {
/*  616 */       return this._numberBigDecimal;
/*      */     }
/*  618 */     if ((this._numTypesValid & 0x8) == 0) {
/*  619 */       _throwInternal();
/*      */     }
/*  621 */     return Double.valueOf(this._numberDouble);
/*      */   }
/*      */   
/*      */   public JsonParser.NumberType getNumberType()
/*      */     throws IOException
/*      */   {
/*  627 */     if (this._numTypesValid == 0) {
/*  628 */       _parseNumericValue(0);
/*      */     }
/*  630 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  631 */       if ((this._numTypesValid & 0x1) != 0) {
/*  632 */         return JsonParser.NumberType.INT;
/*      */       }
/*  634 */       if ((this._numTypesValid & 0x2) != 0) {
/*  635 */         return JsonParser.NumberType.LONG;
/*      */       }
/*  637 */       return JsonParser.NumberType.BIG_INTEGER;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  646 */     if ((this._numTypesValid & 0x10) != 0) {
/*  647 */       return JsonParser.NumberType.BIG_DECIMAL;
/*      */     }
/*  649 */     return JsonParser.NumberType.DOUBLE;
/*      */   }
/*      */   
/*      */   public int getIntValue()
/*      */     throws IOException
/*      */   {
/*  655 */     if ((this._numTypesValid & 0x1) == 0) {
/*  656 */       if (this._numTypesValid == 0) {
/*  657 */         _parseNumericValue(1);
/*      */       }
/*  659 */       if ((this._numTypesValid & 0x1) == 0) {
/*  660 */         convertNumberToInt();
/*      */       }
/*      */     }
/*  663 */     return this._numberInt;
/*      */   }
/*      */   
/*      */   public long getLongValue()
/*      */     throws IOException
/*      */   {
/*  669 */     if ((this._numTypesValid & 0x2) == 0) {
/*  670 */       if (this._numTypesValid == 0) {
/*  671 */         _parseNumericValue(2);
/*      */       }
/*  673 */       if ((this._numTypesValid & 0x2) == 0) {
/*  674 */         convertNumberToLong();
/*      */       }
/*      */     }
/*  677 */     return this._numberLong;
/*      */   }
/*      */   
/*      */   public BigInteger getBigIntegerValue()
/*      */     throws IOException
/*      */   {
/*  683 */     if ((this._numTypesValid & 0x4) == 0) {
/*  684 */       if (this._numTypesValid == 0) {
/*  685 */         _parseNumericValue(4);
/*      */       }
/*  687 */       if ((this._numTypesValid & 0x4) == 0) {
/*  688 */         convertNumberToBigInteger();
/*      */       }
/*      */     }
/*  691 */     return this._numberBigInt;
/*      */   }
/*      */   
/*      */   public float getFloatValue()
/*      */     throws IOException
/*      */   {
/*  697 */     double value = getDoubleValue();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  706 */     return (float)value;
/*      */   }
/*      */   
/*      */   public double getDoubleValue()
/*      */     throws IOException
/*      */   {
/*  712 */     if ((this._numTypesValid & 0x8) == 0) {
/*  713 */       if (this._numTypesValid == 0) {
/*  714 */         _parseNumericValue(8);
/*      */       }
/*  716 */       if ((this._numTypesValid & 0x8) == 0) {
/*  717 */         convertNumberToDouble();
/*      */       }
/*      */     }
/*  720 */     return this._numberDouble;
/*      */   }
/*      */   
/*      */   public BigDecimal getDecimalValue()
/*      */     throws IOException
/*      */   {
/*  726 */     if ((this._numTypesValid & 0x10) == 0) {
/*  727 */       if (this._numTypesValid == 0) {
/*  728 */         _parseNumericValue(16);
/*      */       }
/*  730 */       if ((this._numTypesValid & 0x10) == 0) {
/*  731 */         convertNumberToBigDecimal();
/*      */       }
/*      */     }
/*  734 */     return this._numberBigDecimal;
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
/*      */   protected void _parseNumericValue(int expType)
/*      */     throws IOException
/*      */   {
/*  755 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  756 */       char[] buf = this._textBuffer.getTextBuffer();
/*  757 */       int offset = this._textBuffer.getTextOffset();
/*  758 */       int len = this._intLength;
/*  759 */       if (this._numberNegative) {
/*  760 */         offset++;
/*      */       }
/*  762 */       if (len <= 9) {
/*  763 */         int i = NumberInput.parseInt(buf, offset, len);
/*  764 */         this._numberInt = (this._numberNegative ? -i : i);
/*  765 */         this._numTypesValid = 1;
/*  766 */         return;
/*      */       }
/*  768 */       if (len <= 18) {
/*  769 */         long l = NumberInput.parseLong(buf, offset, len);
/*  770 */         if (this._numberNegative) {
/*  771 */           l = -l;
/*      */         }
/*      */         
/*  774 */         if (len == 10) {
/*  775 */           if (this._numberNegative) {
/*  776 */             if (l >= -2147483648L) {
/*  777 */               this._numberInt = ((int)l);
/*  778 */               this._numTypesValid = 1;
/*      */             }
/*      */             
/*      */           }
/*  782 */           else if (l <= 2147483647L) {
/*  783 */             this._numberInt = ((int)l);
/*  784 */             this._numTypesValid = 1;
/*  785 */             return;
/*      */           }
/*      */         }
/*      */         
/*  789 */         this._numberLong = l;
/*  790 */         this._numTypesValid = 2;
/*  791 */         return;
/*      */       }
/*  793 */       _parseSlowInt(expType, buf, offset, len);
/*  794 */       return;
/*      */     }
/*  796 */     if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
/*  797 */       _parseSlowFloat(expType);
/*  798 */       return;
/*      */     }
/*  800 */     _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _parseSlowFloat(int expType)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  813 */       if (expType == 16) {
/*  814 */         this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
/*  815 */         this._numTypesValid = 16;
/*      */       }
/*      */       else {
/*  818 */         this._numberDouble = this._textBuffer.contentsAsDouble();
/*  819 */         this._numTypesValid = 8;
/*      */       }
/*      */     }
/*      */     catch (NumberFormatException nex) {
/*  823 */       _wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", nex);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _parseSlowInt(int expType, char[] buf, int offset, int len) throws IOException
/*      */   {
/*  829 */     String numStr = this._textBuffer.contentsAsString();
/*      */     try
/*      */     {
/*  832 */       if (NumberInput.inLongRange(buf, offset, len, this._numberNegative))
/*      */       {
/*  834 */         this._numberLong = Long.parseLong(numStr);
/*  835 */         this._numTypesValid = 2;
/*      */       }
/*      */       else {
/*  838 */         this._numberBigInt = new BigInteger(numStr);
/*  839 */         this._numTypesValid = 4;
/*      */       }
/*      */     }
/*      */     catch (NumberFormatException nex) {
/*  843 */       _wrapError("Malformed numeric value '" + numStr + "'", nex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToInt()
/*      */     throws IOException
/*      */   {
/*  856 */     if ((this._numTypesValid & 0x2) != 0)
/*      */     {
/*  858 */       int result = (int)this._numberLong;
/*  859 */       if (result != this._numberLong) {
/*  860 */         _reportError("Numeric value (" + getText() + ") out of range of int");
/*      */       }
/*  862 */       this._numberInt = result;
/*  863 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  864 */       if ((BI_MIN_INT.compareTo(this._numberBigInt) > 0) || (BI_MAX_INT.compareTo(this._numberBigInt) < 0))
/*      */       {
/*  866 */         reportOverflowInt();
/*      */       }
/*  868 */       this._numberInt = this._numberBigInt.intValue();
/*  869 */     } else if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*  871 */       if ((this._numberDouble < -2.147483648E9D) || (this._numberDouble > 2.147483647E9D)) {
/*  872 */         reportOverflowInt();
/*      */       }
/*  874 */       this._numberInt = ((int)this._numberDouble);
/*  875 */     } else if ((this._numTypesValid & 0x10) != 0) {
/*  876 */       if ((BD_MIN_INT.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_INT.compareTo(this._numberBigDecimal) < 0))
/*      */       {
/*  878 */         reportOverflowInt();
/*      */       }
/*  880 */       this._numberInt = this._numberBigDecimal.intValue();
/*      */     } else {
/*  882 */       _throwInternal();
/*      */     }
/*  884 */     this._numTypesValid |= 0x1;
/*      */   }
/*      */   
/*      */   protected void convertNumberToLong() throws IOException
/*      */   {
/*  889 */     if ((this._numTypesValid & 0x1) != 0) {
/*  890 */       this._numberLong = this._numberInt;
/*  891 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  892 */       if ((BI_MIN_LONG.compareTo(this._numberBigInt) > 0) || (BI_MAX_LONG.compareTo(this._numberBigInt) < 0))
/*      */       {
/*  894 */         reportOverflowLong();
/*      */       }
/*  896 */       this._numberLong = this._numberBigInt.longValue();
/*  897 */     } else if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*  899 */       if ((this._numberDouble < -9.223372036854776E18D) || (this._numberDouble > 9.223372036854776E18D)) {
/*  900 */         reportOverflowLong();
/*      */       }
/*  902 */       this._numberLong = (this._numberDouble);
/*  903 */     } else if ((this._numTypesValid & 0x10) != 0) {
/*  904 */       if ((BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0))
/*      */       {
/*  906 */         reportOverflowLong();
/*      */       }
/*  908 */       this._numberLong = this._numberBigDecimal.longValue();
/*      */     } else {
/*  910 */       _throwInternal();
/*      */     }
/*  912 */     this._numTypesValid |= 0x2;
/*      */   }
/*      */   
/*      */   protected void convertNumberToBigInteger() throws IOException
/*      */   {
/*  917 */     if ((this._numTypesValid & 0x10) != 0)
/*      */     {
/*  919 */       this._numberBigInt = this._numberBigDecimal.toBigInteger();
/*  920 */     } else if ((this._numTypesValid & 0x2) != 0) {
/*  921 */       this._numberBigInt = BigInteger.valueOf(this._numberLong);
/*  922 */     } else if ((this._numTypesValid & 0x1) != 0) {
/*  923 */       this._numberBigInt = BigInteger.valueOf(this._numberInt);
/*  924 */     } else if ((this._numTypesValid & 0x8) != 0) {
/*  925 */       this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
/*      */     } else {
/*  927 */       _throwInternal();
/*      */     }
/*  929 */     this._numTypesValid |= 0x4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToDouble()
/*      */     throws IOException
/*      */   {
/*  940 */     if ((this._numTypesValid & 0x10) != 0) {
/*  941 */       this._numberDouble = this._numberBigDecimal.doubleValue();
/*  942 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  943 */       this._numberDouble = this._numberBigInt.doubleValue();
/*  944 */     } else if ((this._numTypesValid & 0x2) != 0) {
/*  945 */       this._numberDouble = this._numberLong;
/*  946 */     } else if ((this._numTypesValid & 0x1) != 0) {
/*  947 */       this._numberDouble = this._numberInt;
/*      */     } else {
/*  949 */       _throwInternal();
/*      */     }
/*  951 */     this._numTypesValid |= 0x8;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToBigDecimal()
/*      */     throws IOException
/*      */   {
/*  962 */     if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  967 */       this._numberBigDecimal = NumberInput.parseBigDecimal(getText());
/*  968 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  969 */       this._numberBigDecimal = new BigDecimal(this._numberBigInt);
/*  970 */     } else if ((this._numTypesValid & 0x2) != 0) {
/*  971 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
/*  972 */     } else if ((this._numTypesValid & 0x1) != 0) {
/*  973 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
/*      */     } else {
/*  975 */       _throwInternal();
/*      */     }
/*  977 */     this._numTypesValid |= 0x10;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reportUnexpectedNumberChar(int ch, String comment)
/*      */     throws JsonParseException
/*      */   {
/*  987 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ") in numeric value";
/*  988 */     if (comment != null) {
/*  989 */       msg = msg + ": " + comment;
/*      */     }
/*  991 */     _reportError(msg);
/*      */   }
/*      */   
/*      */   protected void reportInvalidNumber(String msg) throws JsonParseException {
/*  995 */     _reportError("Invalid numeric value: " + msg);
/*      */   }
/*      */   
/*      */   protected void reportOverflowInt() throws IOException {
/*  999 */     _reportError("Numeric value (" + getText() + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
/*      */   }
/*      */   
/*      */   protected void reportOverflowLong() throws IOException {
/* 1003 */     _reportError("Numeric value (" + getText() + ") out of range of long (" + Long.MIN_VALUE + " - " + Long.MAX_VALUE + ")");
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
/*      */   protected char _decodeEscaped()
/*      */     throws IOException
/*      */   {
/* 1018 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   protected final int _decodeBase64Escape(Base64Variant b64variant, int ch, int index)
/*      */     throws IOException
/*      */   {
/* 1024 */     if (ch != 92) {
/* 1025 */       throw reportInvalidBase64Char(b64variant, ch, index);
/*      */     }
/* 1027 */     int unescaped = _decodeEscaped();
/*      */     
/* 1029 */     if ((unescaped <= 32) && 
/* 1030 */       (index == 0)) {
/* 1031 */       return -1;
/*      */     }
/*      */     
/*      */ 
/* 1035 */     int bits = b64variant.decodeBase64Char(unescaped);
/* 1036 */     if (bits < 0) {
/* 1037 */       throw reportInvalidBase64Char(b64variant, unescaped, index);
/*      */     }
/* 1039 */     return bits;
/*      */   }
/*      */   
/*      */   protected final int _decodeBase64Escape(Base64Variant b64variant, char ch, int index)
/*      */     throws IOException
/*      */   {
/* 1045 */     if (ch != '\\') {
/* 1046 */       throw reportInvalidBase64Char(b64variant, ch, index);
/*      */     }
/* 1048 */     char unescaped = _decodeEscaped();
/*      */     
/* 1050 */     if ((unescaped <= ' ') && 
/* 1051 */       (index == 0)) {
/* 1052 */       return -1;
/*      */     }
/*      */     
/*      */ 
/* 1056 */     int bits = b64variant.decodeBase64Char(unescaped);
/* 1057 */     if (bits < 0) {
/* 1058 */       throw reportInvalidBase64Char(b64variant, unescaped, index);
/*      */     }
/* 1060 */     return bits;
/*      */   }
/*      */   
/*      */   protected IllegalArgumentException reportInvalidBase64Char(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException {
/* 1064 */     return reportInvalidBase64Char(b64variant, ch, bindex, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidBase64Char(Base64Variant b64variant, int ch, int bindex, String msg)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     String base;
/*      */     String base;
/* 1073 */     if (ch <= 32) {
/* 1074 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units"; } else { String base;
/* 1075 */       if (b64variant.usesPaddingChar(ch)) {
/* 1076 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character"; } else { String base;
/* 1077 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*      */         {
/* 1079 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */         } else
/* 1081 */           base = "Illegal character '" + (char)ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */       } }
/* 1083 */     if (msg != null) {
/* 1084 */       base = base + ": " + msg;
/*      */     }
/* 1086 */     return new IllegalArgumentException(base);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\base\ParserBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */