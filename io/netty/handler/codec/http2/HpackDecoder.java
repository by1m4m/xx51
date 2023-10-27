/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HpackDecoder
/*     */ {
/*  55 */   private static final Http2Exception DECODE_ULE_128_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  56 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - decompression failure", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
/*     */   
/*  58 */   private static final Http2Exception DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  59 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - long overflow", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
/*  60 */   private static final Http2Exception DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  61 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - int overflow", new Object[0]), HpackDecoder.class, "decodeULE128ToInt(..)");
/*  62 */   private static final Http2Exception DECODE_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  63 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "decode(..)");
/*  64 */   private static final Http2Exception INDEX_HEADER_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  65 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "indexHeader(..)");
/*  66 */   private static final Http2Exception READ_NAME_ILLEGAL_INDEX_VALUE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  67 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "readName(..)");
/*  68 */   private static final Http2Exception INVALID_MAX_DYNAMIC_TABLE_SIZE = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  69 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - invalid max dynamic table size", new Object[0]), HpackDecoder.class, "setDynamicTableSize(..)");
/*     */   
/*  71 */   private static final Http2Exception MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  72 */     Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - max dynamic table size change required", new Object[0]), HpackDecoder.class, "decode(..)");
/*     */   
/*     */   private static final byte READ_HEADER_REPRESENTATION = 0;
/*     */   
/*     */   private static final byte READ_MAX_DYNAMIC_TABLE_SIZE = 1;
/*     */   
/*     */   private static final byte READ_INDEXED_HEADER = 2;
/*     */   
/*     */   private static final byte READ_INDEXED_HEADER_NAME = 3;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME_LENGTH_PREFIX = 4;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME_LENGTH = 5;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_NAME = 6;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_VALUE_LENGTH_PREFIX = 7;
/*     */   
/*     */   private static final byte READ_LITERAL_HEADER_VALUE_LENGTH = 8;
/*     */   private static final byte READ_LITERAL_HEADER_VALUE = 9;
/*     */   private final HpackDynamicTable hpackDynamicTable;
/*     */   private final HpackHuffmanDecoder hpackHuffmanDecoder;
/*     */   private long maxHeaderListSize;
/*     */   private long maxDynamicTableSize;
/*     */   private long encoderMaxDynamicTableSize;
/*     */   private boolean maxDynamicTableSizeChangeRequired;
/*     */   
/*     */   HpackDecoder(long maxHeaderListSize, int initialHuffmanDecodeCapacity)
/*     */   {
/* 101 */     this(maxHeaderListSize, initialHuffmanDecodeCapacity, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   HpackDecoder(long maxHeaderListSize, int initialHuffmanDecodeCapacity, int maxHeaderTableSize)
/*     */   {
/* 109 */     this.maxHeaderListSize = ObjectUtil.checkPositive(maxHeaderListSize, "maxHeaderListSize");
/*     */     
/* 111 */     this.maxDynamicTableSize = (this.encoderMaxDynamicTableSize = maxHeaderTableSize);
/* 112 */     this.maxDynamicTableSizeChangeRequired = false;
/* 113 */     this.hpackDynamicTable = new HpackDynamicTable(maxHeaderTableSize);
/* 114 */     this.hpackHuffmanDecoder = new HpackHuffmanDecoder(initialHuffmanDecodeCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decode(int streamId, ByteBuf in, Http2Headers headers, boolean validateHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 123 */     Http2HeadersSink sink = new Http2HeadersSink(streamId, headers, this.maxHeaderListSize, validateHeaders);
/* 124 */     decode(in, sink);
/*     */     
/*     */ 
/*     */ 
/* 128 */     sink.finish();
/*     */   }
/*     */   
/*     */   private void decode(ByteBuf in, Sink sink) throws Http2Exception {
/* 132 */     int index = 0;
/* 133 */     int nameLength = 0;
/* 134 */     int valueLength = 0;
/* 135 */     byte state = 0;
/* 136 */     boolean huffmanEncoded = false;
/* 137 */     CharSequence name = null;
/* 138 */     HpackUtil.IndexType indexType = HpackUtil.IndexType.NONE;
/* 139 */     while (in.isReadable()) {
/* 140 */       switch (state) {
/*     */       case 0: 
/* 142 */         byte b = in.readByte();
/* 143 */         if ((this.maxDynamicTableSizeChangeRequired) && ((b & 0xE0) != 32))
/*     */         {
/* 145 */           throw MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED;
/*     */         }
/* 147 */         if (b < 0)
/*     */         {
/* 149 */           index = b & 0x7F;
/* 150 */           switch (index) {
/*     */           case 0: 
/* 152 */             throw DECODE_ILLEGAL_INDEX_VALUE;
/*     */           case 127: 
/* 154 */             state = 2;
/* 155 */             break;
/*     */           default: 
/* 157 */             HpackHeaderField indexedHeader = getIndexedHeader(index);
/* 158 */             sink.appendToHeaderList(indexedHeader.name, indexedHeader.value);
/*     */           }
/* 160 */         } else if ((b & 0x40) == 64)
/*     */         {
/* 162 */           indexType = HpackUtil.IndexType.INCREMENTAL;
/* 163 */           index = b & 0x3F;
/* 164 */           switch (index) {
/*     */           case 0: 
/* 166 */             state = 4;
/* 167 */             break;
/*     */           case 63: 
/* 169 */             state = 3;
/* 170 */             break;
/*     */           
/*     */           default: 
/* 173 */             name = readName(index);
/* 174 */             nameLength = name.length();
/* 175 */             state = 7;break;
/*     */           }
/* 177 */         } else if ((b & 0x20) == 32)
/*     */         {
/* 179 */           index = b & 0x1F;
/* 180 */           if (index == 31) {
/* 181 */             state = 1;
/*     */           } else {
/* 183 */             setDynamicTableSize(index);
/* 184 */             state = 0;
/*     */           }
/*     */         }
/*     */         else {
/* 188 */           indexType = (b & 0x10) == 16 ? HpackUtil.IndexType.NEVER : HpackUtil.IndexType.NONE;
/* 189 */           index = b & 0xF;
/* 190 */           switch (index) {
/*     */           case 0: 
/* 192 */             state = 4;
/* 193 */             break;
/*     */           case 15: 
/* 195 */             state = 3;
/* 196 */             break;
/*     */           
/*     */           default: 
/* 199 */             name = readName(index);
/* 200 */             nameLength = name.length();
/* 201 */             state = 7;
/*     */           }
/*     */         }
/* 204 */         break;
/*     */       
/*     */       case 1: 
/* 207 */         setDynamicTableSize(decodeULE128(in, index));
/* 208 */         state = 0;
/* 209 */         break;
/*     */       
/*     */       case 2: 
/* 212 */         HpackHeaderField indexedHeader = getIndexedHeader(decodeULE128(in, index));
/* 213 */         sink.appendToHeaderList(indexedHeader.name, indexedHeader.value);
/* 214 */         state = 0;
/* 215 */         break;
/*     */       
/*     */ 
/*     */       case 3: 
/* 219 */         name = readName(decodeULE128(in, index));
/* 220 */         nameLength = name.length();
/* 221 */         state = 7;
/* 222 */         break;
/*     */       
/*     */       case 4: 
/* 225 */         byte b = in.readByte();
/* 226 */         huffmanEncoded = (b & 0x80) == 128;
/* 227 */         index = b & 0x7F;
/* 228 */         if (index == 127) {
/* 229 */           state = 5;
/*     */         } else {
/* 231 */           nameLength = index;
/* 232 */           state = 6;
/*     */         }
/* 234 */         break;
/*     */       
/*     */ 
/*     */       case 5: 
/* 238 */         nameLength = decodeULE128(in, index);
/*     */         
/* 240 */         state = 6;
/* 241 */         break;
/*     */       
/*     */ 
/*     */       case 6: 
/* 245 */         if (in.readableBytes() < nameLength) {
/* 246 */           throw notEnoughDataException(in);
/*     */         }
/*     */         
/* 249 */         name = readStringLiteral(in, nameLength, huffmanEncoded);
/*     */         
/* 251 */         state = 7;
/* 252 */         break;
/*     */       
/*     */       case 7: 
/* 255 */         byte b = in.readByte();
/* 256 */         huffmanEncoded = (b & 0x80) == 128;
/* 257 */         index = b & 0x7F;
/* 258 */         switch (index) {
/*     */         case 127: 
/* 260 */           state = 8;
/* 261 */           break;
/*     */         case 0: 
/* 263 */           insertHeader(sink, name, AsciiString.EMPTY_STRING, indexType);
/* 264 */           state = 0;
/* 265 */           break;
/*     */         default: 
/* 267 */           valueLength = index;
/* 268 */           state = 9;
/*     */         }
/*     */         
/* 271 */         break;
/*     */       
/*     */ 
/*     */       case 8: 
/* 275 */         valueLength = decodeULE128(in, index);
/*     */         
/* 277 */         state = 9;
/* 278 */         break;
/*     */       
/*     */ 
/*     */       case 9: 
/* 282 */         if (in.readableBytes() < valueLength) {
/* 283 */           throw notEnoughDataException(in);
/*     */         }
/*     */         
/* 286 */         CharSequence value = readStringLiteral(in, valueLength, huffmanEncoded);
/* 287 */         insertHeader(sink, name, value, indexType);
/* 288 */         state = 0;
/* 289 */         break;
/*     */       
/*     */       default: 
/* 292 */         throw new Error("should not reach here state: " + state);
/*     */       }
/*     */       
/*     */     }
/* 296 */     if (state != 0) {
/* 297 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "Incomplete header block fragment.", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaxHeaderTableSize(long maxHeaderTableSize)
/*     */     throws Http2Exception
/*     */   {
/* 306 */     if ((maxHeaderTableSize < 0L) || (maxHeaderTableSize > 4294967295L)) {
/* 307 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", new Object[] {
/* 308 */         Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderTableSize) });
/*     */     }
/* 310 */     this.maxDynamicTableSize = maxHeaderTableSize;
/* 311 */     if (this.maxDynamicTableSize < this.encoderMaxDynamicTableSize)
/*     */     {
/*     */ 
/* 314 */       this.maxDynamicTableSizeChangeRequired = true;
/* 315 */       this.hpackDynamicTable.setCapacity(this.maxDynamicTableSize);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setMaxHeaderListSize(long maxHeaderListSize, long maxHeaderListSizeGoAway)
/*     */     throws Http2Exception
/*     */   {
/* 325 */     setMaxHeaderListSize(maxHeaderListSize);
/*     */   }
/*     */   
/*     */   public void setMaxHeaderListSize(long maxHeaderListSize) throws Http2Exception {
/* 329 */     if ((maxHeaderListSize < 0L) || (maxHeaderListSize > 4294967295L)) {
/* 330 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", new Object[] {
/* 331 */         Long.valueOf(0L), Long.valueOf(4294967295L), Long.valueOf(maxHeaderListSize) });
/*     */     }
/* 333 */     this.maxHeaderListSize = maxHeaderListSize;
/*     */   }
/*     */   
/*     */   public long getMaxHeaderListSize() {
/* 337 */     return this.maxHeaderListSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMaxHeaderTableSize()
/*     */   {
/* 345 */     return this.hpackDynamicTable.capacity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int length()
/*     */   {
/* 352 */     return this.hpackDynamicTable.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long size()
/*     */   {
/* 359 */     return this.hpackDynamicTable.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   HpackHeaderField getHeaderField(int index)
/*     */   {
/* 366 */     return this.hpackDynamicTable.getEntry(index + 1);
/*     */   }
/*     */   
/*     */   private void setDynamicTableSize(long dynamicTableSize) throws Http2Exception {
/* 370 */     if (dynamicTableSize > this.maxDynamicTableSize) {
/* 371 */       throw INVALID_MAX_DYNAMIC_TABLE_SIZE;
/*     */     }
/* 373 */     this.encoderMaxDynamicTableSize = dynamicTableSize;
/* 374 */     this.maxDynamicTableSizeChangeRequired = false;
/* 375 */     this.hpackDynamicTable.setCapacity(dynamicTableSize);
/*     */   }
/*     */   
/*     */   private static HeaderType validate(int streamId, CharSequence name, HeaderType previousHeaderType) throws Http2Exception
/*     */   {
/* 380 */     if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
/* 381 */       if (previousHeaderType == HeaderType.REGULAR_HEADER) {
/* 382 */         throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Pseudo-header field '%s' found after regular header.", new Object[] { name });
/*     */       }
/*     */       
/*     */ 
/* 386 */       Http2Headers.PseudoHeaderName pseudoHeader = Http2Headers.PseudoHeaderName.getPseudoHeader(name);
/* 387 */       if (pseudoHeader == null) {
/* 388 */         throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 pseudo-header '%s' encountered.", new Object[] { name });
/*     */       }
/*     */       
/* 391 */       HeaderType currentHeaderType = pseudoHeader.isRequestOnly() ? HeaderType.REQUEST_PSEUDO_HEADER : HeaderType.RESPONSE_PSEUDO_HEADER;
/*     */       
/* 393 */       if ((previousHeaderType != null) && (currentHeaderType != previousHeaderType)) {
/* 394 */         throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Mix of request and response pseudo-headers.", new Object[0]);
/*     */       }
/*     */       
/* 397 */       return currentHeaderType;
/*     */     }
/*     */     
/* 400 */     return HeaderType.REGULAR_HEADER;
/*     */   }
/*     */   
/*     */   private CharSequence readName(int index) throws Http2Exception {
/* 404 */     if (index <= HpackStaticTable.length) {
/* 405 */       HpackHeaderField hpackHeaderField = HpackStaticTable.getEntry(index);
/* 406 */       return hpackHeaderField.name;
/*     */     }
/* 408 */     if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
/* 409 */       HpackHeaderField hpackHeaderField = this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
/* 410 */       return hpackHeaderField.name;
/*     */     }
/* 412 */     throw READ_NAME_ILLEGAL_INDEX_VALUE;
/*     */   }
/*     */   
/*     */   private HpackHeaderField getIndexedHeader(int index) throws Http2Exception {
/* 416 */     if (index <= HpackStaticTable.length) {
/* 417 */       return HpackStaticTable.getEntry(index);
/*     */     }
/* 419 */     if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
/* 420 */       return this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
/*     */     }
/* 422 */     throw INDEX_HEADER_ILLEGAL_INDEX_VALUE;
/*     */   }
/*     */   
/*     */   private void insertHeader(Sink sink, CharSequence name, CharSequence value, HpackUtil.IndexType indexType) {
/* 426 */     sink.appendToHeaderList(name, value);
/*     */     
/* 428 */     switch (indexType)
/*     */     {
/*     */     case NONE: 
/*     */     case NEVER: 
/*     */       break;
/*     */     case INCREMENTAL: 
/* 434 */       this.hpackDynamicTable.add(new HpackHeaderField(name, value));
/* 435 */       break;
/*     */     
/*     */     default: 
/* 438 */       throw new Error("should not reach here");
/*     */     }
/*     */   }
/*     */   
/*     */   private CharSequence readStringLiteral(ByteBuf in, int length, boolean huffmanEncoded) throws Http2Exception {
/* 443 */     if (huffmanEncoded) {
/* 444 */       return this.hpackHuffmanDecoder.decode(in, length);
/*     */     }
/* 446 */     byte[] buf = new byte[length];
/* 447 */     in.readBytes(buf);
/* 448 */     return new AsciiString(buf, false);
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException notEnoughDataException(ByteBuf in) {
/* 452 */     return new IllegalArgumentException("decode only works with an entire header block! " + in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static int decodeULE128(ByteBuf in, int result)
/*     */     throws Http2Exception
/*     */   {
/* 461 */     int readerIndex = in.readerIndex();
/* 462 */     long v = decodeULE128(in, result);
/* 463 */     if (v > 2147483647L)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 469 */       in.readerIndex(readerIndex);
/* 470 */       throw DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION;
/*     */     }
/* 472 */     return (int)v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static long decodeULE128(ByteBuf in, long result)
/*     */     throws Http2Exception
/*     */   {
/* 481 */     assert ((result <= 127L) && (result >= 0L));
/* 482 */     boolean resultStartedAtZero = result == 0L;
/* 483 */     int writerIndex = in.writerIndex();
/* 484 */     int readerIndex = in.readerIndex(); for (int shift = 0; readerIndex < writerIndex; shift += 7) {
/* 485 */       byte b = in.getByte(readerIndex);
/* 486 */       if ((shift == 56) && (((b & 0x80) != 0) || ((b == Byte.MAX_VALUE) && (!resultStartedAtZero))))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 494 */         throw DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
/*     */       }
/*     */       
/* 497 */       if ((b & 0x80) == 0) {
/* 498 */         in.readerIndex(readerIndex + 1);
/* 499 */         return result + ((b & 0x7F) << shift);
/*     */       }
/* 501 */       result += ((b & 0x7F) << shift);readerIndex++;
/*     */     }
/*     */     
/* 504 */     throw DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static enum HeaderType
/*     */   {
/* 511 */     REGULAR_HEADER, 
/* 512 */     REQUEST_PSEUDO_HEADER, 
/* 513 */     RESPONSE_PSEUDO_HEADER;
/*     */     
/*     */     private HeaderType() {}
/*     */   }
/*     */   
/*     */   private static abstract interface Sink { public abstract void appendToHeaderList(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*     */     
/*     */     public abstract void finish() throws Http2Exception;
/*     */   }
/*     */   
/*     */   private static final class Http2HeadersSink implements HpackDecoder.Sink { private final Http2Headers headers;
/*     */     private final long maxHeaderListSize;
/*     */     private final int streamId;
/*     */     private final boolean validate;
/*     */     private long headersLength;
/*     */     private boolean exceededMaxLength;
/*     */     private HpackDecoder.HeaderType previousType;
/*     */     private Http2Exception validationException;
/*     */     
/* 532 */     public Http2HeadersSink(int streamId, Http2Headers headers, long maxHeaderListSize, boolean validate) { this.headers = headers;
/* 533 */       this.maxHeaderListSize = maxHeaderListSize;
/* 534 */       this.streamId = streamId;
/* 535 */       this.validate = validate;
/*     */     }
/*     */     
/*     */     public void finish() throws Http2Exception
/*     */     {
/* 540 */       if (this.exceededMaxLength) {
/* 541 */         Http2CodecUtil.headerListSizeExceeded(this.streamId, this.maxHeaderListSize, true);
/* 542 */       } else if (this.validationException != null) {
/* 543 */         throw this.validationException;
/*     */       }
/*     */     }
/*     */     
/*     */     public void appendToHeaderList(CharSequence name, CharSequence value)
/*     */     {
/* 549 */       this.headersLength += HpackHeaderField.sizeOf(name, value);
/* 550 */       this.exceededMaxLength |= this.headersLength > this.maxHeaderListSize;
/*     */       
/* 552 */       if ((this.exceededMaxLength) || (this.validationException != null))
/*     */       {
/* 554 */         return;
/*     */       }
/*     */       
/* 557 */       if (this.validate) {
/*     */         try {
/* 559 */           this.previousType = HpackDecoder.validate(this.streamId, name, this.previousType);
/*     */         } catch (Http2Exception ex) {
/* 561 */           this.validationException = ex;
/* 562 */           return;
/*     */         }
/*     */       }
/*     */       
/* 566 */       this.headers.add(name, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\HpackDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */