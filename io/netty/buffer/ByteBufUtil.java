/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.AsciiString;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ByteProcessor.IndexOfProcessor;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.Recycler;
/*      */ import io.netty.util.Recycler.Handle;
/*      */ import io.netty.util.concurrent.FastThreadLocal;
/*      */ import io.netty.util.internal.MathUtil;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.SystemPropertyUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.CoderResult;
/*      */ import java.nio.charset.CodingErrorAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ByteBufUtil
/*      */ {
/*   55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ByteBufUtil.class);
/*   56 */   private static final FastThreadLocal<CharBuffer> CHAR_BUFFERS = new FastThreadLocal()
/*      */   {
/*      */     protected CharBuffer initialValue() throws Exception {
/*   59 */       return CharBuffer.allocate(1024);
/*      */     }
/*      */   };
/*      */   
/*      */   private static final byte WRITE_UTF_UNKNOWN = 63;
/*      */   
/*      */   private static final int MAX_CHAR_BUFFER_SIZE;
/*      */   private static final int THREAD_LOCAL_BUFFER_SIZE;
/*   67 */   private static final int MAX_BYTES_PER_CHAR_UTF8 = (int)CharsetUtil.encoder(CharsetUtil.UTF_8).maxBytesPerChar();
/*      */   static final int WRITE_CHUNK_SIZE = 8192;
/*      */   static final ByteBufAllocator DEFAULT_ALLOCATOR;
/*      */   
/*      */   static
/*      */   {
/*   73 */     String allocType = SystemPropertyUtil.get("io.netty.allocator.type", 
/*   74 */       PlatformDependent.isAndroid() ? "unpooled" : "pooled");
/*   75 */     allocType = allocType.toLowerCase(Locale.US).trim();
/*      */     
/*      */     ByteBufAllocator alloc;
/*   78 */     if ("unpooled".equals(allocType)) {
/*   79 */       ByteBufAllocator alloc = UnpooledByteBufAllocator.DEFAULT;
/*   80 */       logger.debug("-Dio.netty.allocator.type: {}", allocType);
/*   81 */     } else if ("pooled".equals(allocType)) {
/*   82 */       ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;
/*   83 */       logger.debug("-Dio.netty.allocator.type: {}", allocType);
/*      */     } else {
/*   85 */       alloc = PooledByteBufAllocator.DEFAULT;
/*   86 */       logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
/*      */     }
/*      */     
/*   89 */     DEFAULT_ALLOCATOR = alloc;
/*      */     
/*   91 */     THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 0);
/*   92 */     logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", Integer.valueOf(THREAD_LOCAL_BUFFER_SIZE));
/*      */     
/*   94 */     MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16384);
/*   95 */     logger.debug("-Dio.netty.maxThreadLocalCharBufferSize: {}", Integer.valueOf(MAX_CHAR_BUFFER_SIZE));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hexDump(ByteBuf buffer)
/*      */   {
/*  103 */     return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hexDump(ByteBuf buffer, int fromIndex, int length)
/*      */   {
/*  111 */     return HexUtil.hexDump(buffer, fromIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hexDump(byte[] array)
/*      */   {
/*  119 */     return hexDump(array, 0, array.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hexDump(byte[] array, int fromIndex, int length)
/*      */   {
/*  127 */     return HexUtil.hexDump(array, fromIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static byte decodeHexByte(CharSequence s, int pos)
/*      */   {
/*  134 */     return StringUtil.decodeHexByte(s, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static byte[] decodeHexDump(CharSequence hexDump)
/*      */   {
/*  141 */     return StringUtil.decodeHexDump(hexDump, 0, hexDump.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static byte[] decodeHexDump(CharSequence hexDump, int fromIndex, int length)
/*      */   {
/*  148 */     return StringUtil.decodeHexDump(hexDump, fromIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean ensureWritableSuccess(int ensureWritableResult)
/*      */   {
/*  159 */     return (ensureWritableResult == 0) || (ensureWritableResult == 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int hashCode(ByteBuf buffer)
/*      */   {
/*  167 */     int aLen = buffer.readableBytes();
/*  168 */     int intCount = aLen >>> 2;
/*  169 */     int byteCount = aLen & 0x3;
/*      */     
/*  171 */     int hashCode = 1;
/*  172 */     int arrayIndex = buffer.readerIndex();
/*  173 */     if (buffer.order() == ByteOrder.BIG_ENDIAN) {
/*  174 */       for (int i = intCount; i > 0; i--) {
/*  175 */         hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
/*  176 */         arrayIndex += 4;
/*      */       }
/*      */     } else {
/*  179 */       for (int i = intCount; i > 0; i--) {
/*  180 */         hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
/*  181 */         arrayIndex += 4;
/*      */       }
/*      */     }
/*      */     
/*  185 */     for (int i = byteCount; i > 0; i--) {
/*  186 */       hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
/*      */     }
/*      */     
/*  189 */     if (hashCode == 0) {
/*  190 */       hashCode = 1;
/*      */     }
/*      */     
/*  193 */     return hashCode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int indexOf(ByteBuf needle, ByteBuf haystack)
/*      */   {
/*  201 */     int attempts = haystack.readableBytes() - needle.readableBytes() + 1;
/*  202 */     for (int i = 0; i < attempts; i++) {
/*  203 */       if (equals(needle, needle.readerIndex(), haystack, haystack
/*  204 */         .readerIndex() + i, needle
/*  205 */         .readableBytes())) {
/*  206 */         return haystack.readerIndex() + i;
/*      */       }
/*      */     }
/*  209 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length)
/*      */   {
/*  221 */     if ((aStartIndex < 0) || (bStartIndex < 0) || (length < 0)) {
/*  222 */       throw new IllegalArgumentException("All indexes and lengths must be non-negative");
/*      */     }
/*  224 */     if ((a.writerIndex() - length < aStartIndex) || (b.writerIndex() - length < bStartIndex)) {
/*  225 */       return false;
/*      */     }
/*      */     
/*  228 */     int longCount = length >>> 3;
/*  229 */     int byteCount = length & 0x7;
/*      */     
/*  231 */     if (a.order() == b.order()) {
/*  232 */       for (int i = longCount; i > 0; i--) {
/*  233 */         if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
/*  234 */           return false;
/*      */         }
/*  236 */         aStartIndex += 8;
/*  237 */         bStartIndex += 8;
/*      */       }
/*      */     } else {
/*  240 */       for (int i = longCount; i > 0; i--) {
/*  241 */         if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
/*  242 */           return false;
/*      */         }
/*  244 */         aStartIndex += 8;
/*  245 */         bStartIndex += 8;
/*      */       }
/*      */     }
/*      */     
/*  249 */     for (int i = byteCount; i > 0; i--) {
/*  250 */       if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
/*  251 */         return false;
/*      */       }
/*  253 */       aStartIndex++;
/*  254 */       bStartIndex++;
/*      */     }
/*      */     
/*  257 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean equals(ByteBuf bufferA, ByteBuf bufferB)
/*      */   {
/*  266 */     int aLen = bufferA.readableBytes();
/*  267 */     if (aLen != bufferB.readableBytes()) {
/*  268 */       return false;
/*      */     }
/*  270 */     return equals(bufferA, bufferA.readerIndex(), bufferB, bufferB.readerIndex(), aLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(ByteBuf bufferA, ByteBuf bufferB)
/*      */   {
/*  278 */     int aLen = bufferA.readableBytes();
/*  279 */     int bLen = bufferB.readableBytes();
/*  280 */     int minLength = Math.min(aLen, bLen);
/*  281 */     int uintCount = minLength >>> 2;
/*  282 */     int byteCount = minLength & 0x3;
/*  283 */     int aIndex = bufferA.readerIndex();
/*  284 */     int bIndex = bufferB.readerIndex();
/*      */     
/*  286 */     if (uintCount > 0) {
/*  287 */       boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
/*      */       
/*  289 */       int uintCountIncrement = uintCount << 2;
/*      */       long res;
/*  291 */       long res; if (bufferA.order() == bufferB.order())
/*      */       {
/*  293 */         res = bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
/*      */       }
/*      */       else {
/*  296 */         res = bufferAIsBigEndian ? compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
/*      */       }
/*  298 */       if (res != 0L)
/*      */       {
/*  300 */         return (int)Math.min(2147483647L, Math.max(-2147483648L, res));
/*      */       }
/*  302 */       aIndex += uintCountIncrement;
/*  303 */       bIndex += uintCountIncrement;
/*      */     }
/*      */     
/*  306 */     for (int aEnd = aIndex + byteCount; aIndex < aEnd; bIndex++) {
/*  307 */       int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
/*  308 */       if (comp != 0) {
/*  309 */         return comp;
/*      */       }
/*  306 */       aIndex++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  313 */     return aLen - bLen;
/*      */   }
/*      */   
/*      */   private static long compareUintBigEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement)
/*      */   {
/*  318 */     for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; bIndex += 4) {
/*  319 */       long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
/*  320 */       if (comp != 0L) {
/*  321 */         return comp;
/*      */       }
/*  318 */       aIndex += 4;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  324 */     return 0L;
/*      */   }
/*      */   
/*      */   private static long compareUintLittleEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement)
/*      */   {
/*  329 */     for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; bIndex += 4) {
/*  330 */       long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedIntLE(bIndex);
/*  331 */       if (comp != 0L) {
/*  332 */         return comp;
/*      */       }
/*  329 */       aIndex += 4;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  335 */     return 0L;
/*      */   }
/*      */   
/*      */   private static long compareUintBigEndianA(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement)
/*      */   {
/*  340 */     for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; bIndex += 4) {
/*  341 */       long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedIntLE(bIndex);
/*  342 */       if (comp != 0L) {
/*  343 */         return comp;
/*      */       }
/*  340 */       aIndex += 4;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  346 */     return 0L;
/*      */   }
/*      */   
/*      */   private static long compareUintBigEndianB(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement)
/*      */   {
/*  351 */     for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; bIndex += 4) {
/*  352 */       long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedInt(bIndex);
/*  353 */       if (comp != 0L) {
/*  354 */         return comp;
/*      */       }
/*  351 */       aIndex += 4;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  357 */     return 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int indexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value)
/*      */   {
/*  365 */     if (fromIndex <= toIndex) {
/*  366 */       return firstIndexOf(buffer, fromIndex, toIndex, value);
/*      */     }
/*  368 */     return lastIndexOf(buffer, fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static short swapShort(short value)
/*      */   {
/*  376 */     return Short.reverseBytes(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int swapMedium(int value)
/*      */   {
/*  383 */     int swapped = value << 16 & 0xFF0000 | value & 0xFF00 | value >>> 16 & 0xFF;
/*  384 */     if ((swapped & 0x800000) != 0) {
/*  385 */       swapped |= 0xFF000000;
/*      */     }
/*  387 */     return swapped;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int swapInt(int value)
/*      */   {
/*  394 */     return Integer.reverseBytes(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static long swapLong(long value)
/*      */   {
/*  401 */     return Long.reverseBytes(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuf writeShortBE(ByteBuf buf, int shortValue)
/*      */   {
/*  409 */     return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeShort(shortValue) : buf.writeShortLE(shortValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuf setShortBE(ByteBuf buf, int index, int shortValue)
/*      */   {
/*  417 */     return buf.order() == ByteOrder.BIG_ENDIAN ? buf.setShort(index, shortValue) : buf.setShortLE(index, shortValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuf writeMediumBE(ByteBuf buf, int mediumValue)
/*      */   {
/*  425 */     return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeMedium(mediumValue) : buf.writeMediumLE(mediumValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static ByteBuf readBytes(ByteBufAllocator alloc, ByteBuf buffer, int length)
/*      */   {
/*  432 */     boolean release = true;
/*  433 */     ByteBuf dst = alloc.buffer(length);
/*      */     try {
/*  435 */       buffer.readBytes(dst);
/*  436 */       release = false;
/*  437 */       return dst;
/*      */     } finally {
/*  439 */       if (release) {
/*  440 */         dst.release();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static int firstIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
/*  446 */     fromIndex = Math.max(fromIndex, 0);
/*  447 */     if ((fromIndex >= toIndex) || (buffer.capacity() == 0)) {
/*  448 */       return -1;
/*      */     }
/*      */     
/*  451 */     return buffer.forEachByte(fromIndex, toIndex - fromIndex, new ByteProcessor.IndexOfProcessor(value));
/*      */   }
/*      */   
/*      */   private static int lastIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
/*  455 */     fromIndex = Math.min(fromIndex, buffer.capacity());
/*  456 */     if ((fromIndex < 0) || (buffer.capacity() == 0)) {
/*  457 */       return -1;
/*      */     }
/*      */     
/*  460 */     return buffer.forEachByteDesc(toIndex, fromIndex - toIndex, new ByteProcessor.IndexOfProcessor(value));
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
/*      */   public static ByteBuf writeUtf8(ByteBufAllocator alloc, CharSequence seq)
/*      */   {
/*  473 */     ByteBuf buf = alloc.buffer(utf8MaxBytes(seq));
/*  474 */     writeUtf8(buf, seq);
/*  475 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int writeUtf8(ByteBuf buf, CharSequence seq)
/*      */   {
/*  487 */     return reserveAndWriteUtf8(buf, seq, utf8MaxBytes(seq));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int reserveAndWriteUtf8(ByteBuf buf, CharSequence seq, int reserveBytes)
/*      */   {
/*      */     for (;;)
/*      */     {
/*  501 */       if ((buf instanceof WrappedCompositeByteBuf))
/*      */       {
/*  503 */         buf = buf.unwrap();
/*  504 */       } else { if ((buf instanceof AbstractByteBuf)) {
/*  505 */           AbstractByteBuf byteBuf = (AbstractByteBuf)buf;
/*  506 */           byteBuf.ensureWritable0(reserveBytes);
/*  507 */           int written = writeUtf8(byteBuf, byteBuf.writerIndex, seq, seq.length());
/*  508 */           byteBuf.writerIndex += written;
/*  509 */           return written; }
/*  510 */         if (!(buf instanceof WrappedByteBuf))
/*      */           break;
/*  512 */         buf = buf.unwrap();
/*      */       } }
/*  514 */     byte[] bytes = seq.toString().getBytes(CharsetUtil.UTF_8);
/*  515 */     buf.writeBytes(bytes);
/*  516 */     return bytes.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static int writeUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len)
/*      */   {
/*  523 */     int oldWriterIndex = writerIndex;
/*      */     
/*      */ 
/*      */ 
/*  527 */     for (int i = 0; i < len; i++) {
/*  528 */       char c = seq.charAt(i);
/*  529 */       if (c < '') {
/*  530 */         buffer._setByte(writerIndex++, (byte)c);
/*  531 */       } else if (c < 'ࠀ') {
/*  532 */         buffer._setByte(writerIndex++, (byte)(0xC0 | c >> '\006'));
/*  533 */         buffer._setByte(writerIndex++, (byte)(0x80 | c & 0x3F));
/*  534 */       } else if (StringUtil.isSurrogate(c)) {
/*  535 */         if (!Character.isHighSurrogate(c)) {
/*  536 */           buffer._setByte(writerIndex++, 63);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*  544 */             c2 = seq.charAt(++i);
/*      */           } catch (IndexOutOfBoundsException ignored) { char c2;
/*  546 */             buffer._setByte(writerIndex++, 63);
/*  547 */             break; }
/*      */           char c2;
/*  549 */           if (!Character.isLowSurrogate(c2)) {
/*  550 */             buffer._setByte(writerIndex++, 63);
/*  551 */             buffer._setByte(writerIndex++, Character.isHighSurrogate(c2) ? '?' : c2);
/*      */           }
/*      */           else {
/*  554 */             int codePoint = Character.toCodePoint(c, c2);
/*      */             
/*  556 */             buffer._setByte(writerIndex++, (byte)(0xF0 | codePoint >> 18));
/*  557 */             buffer._setByte(writerIndex++, (byte)(0x80 | codePoint >> 12 & 0x3F));
/*  558 */             buffer._setByte(writerIndex++, (byte)(0x80 | codePoint >> 6 & 0x3F));
/*  559 */             buffer._setByte(writerIndex++, (byte)(0x80 | codePoint & 0x3F));
/*      */           }
/*  561 */         } } else { buffer._setByte(writerIndex++, (byte)(0xE0 | c >> '\f'));
/*  562 */         buffer._setByte(writerIndex++, (byte)(0x80 | c >> '\006' & 0x3F));
/*  563 */         buffer._setByte(writerIndex++, (byte)(0x80 | c & 0x3F));
/*      */       }
/*      */     }
/*  566 */     return writerIndex - oldWriterIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int utf8MaxBytes(int seqLength)
/*      */   {
/*  573 */     return seqLength * MAX_BYTES_PER_CHAR_UTF8;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int utf8MaxBytes(CharSequence seq)
/*      */   {
/*  582 */     return utf8MaxBytes(seq.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int utf8Bytes(CharSequence seq)
/*      */   {
/*  591 */     if ((seq instanceof AsciiString)) {
/*  592 */       return seq.length();
/*      */     }
/*  594 */     int seqLength = seq.length();
/*  595 */     int i = 0;
/*      */     
/*  597 */     while ((i < seqLength) && (seq.charAt(i) < '')) {
/*  598 */       i++;
/*      */     }
/*      */     
/*  601 */     return i < seqLength ? i + utf8Bytes(seq, i, seqLength) : i;
/*      */   }
/*      */   
/*      */   private static int utf8Bytes(CharSequence seq, int start, int length) {
/*  605 */     int encodedLength = 0;
/*  606 */     for (int i = start; i < length; i++) {
/*  607 */       char c = seq.charAt(i);
/*      */       
/*  609 */       if (c < 'ࠀ')
/*      */       {
/*  611 */         encodedLength += ('' - c >>> 31) + 1;
/*  612 */       } else if (StringUtil.isSurrogate(c)) {
/*  613 */         if (!Character.isHighSurrogate(c)) {
/*  614 */           encodedLength++;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*  622 */             c2 = seq.charAt(++i);
/*      */           } catch (IndexOutOfBoundsException ignored) { char c2;
/*  624 */             encodedLength++;
/*      */             
/*  626 */             break; }
/*      */           char c2;
/*  628 */           if (!Character.isLowSurrogate(c2))
/*      */           {
/*  630 */             encodedLength += 2;
/*      */           }
/*      */           else
/*      */           {
/*  634 */             encodedLength += 4; }
/*      */         }
/*  636 */       } else { encodedLength += 3;
/*      */       }
/*      */     }
/*  639 */     return encodedLength;
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
/*      */   public static ByteBuf writeAscii(ByteBufAllocator alloc, CharSequence seq)
/*      */   {
/*  652 */     ByteBuf buf = alloc.buffer(seq.length());
/*  653 */     writeAscii(buf, seq);
/*  654 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int writeAscii(ByteBuf buf, CharSequence seq)
/*      */   {
/*  665 */     int len = seq.length();
/*  666 */     if ((seq instanceof AsciiString)) {
/*  667 */       AsciiString asciiString = (AsciiString)seq;
/*  668 */       buf.writeBytes(asciiString.array(), asciiString.arrayOffset(), len);
/*      */     } else {
/*      */       for (;;) {
/*  671 */         if ((buf instanceof WrappedCompositeByteBuf))
/*      */         {
/*  673 */           buf = buf.unwrap();
/*  674 */         } else { if ((buf instanceof AbstractByteBuf)) {
/*  675 */             AbstractByteBuf byteBuf = (AbstractByteBuf)buf;
/*  676 */             byteBuf.ensureWritable0(len);
/*  677 */             int written = writeAscii(byteBuf, byteBuf.writerIndex, seq, len);
/*  678 */             byteBuf.writerIndex += written;
/*  679 */             return written; }
/*  680 */           if (!(buf instanceof WrappedByteBuf))
/*      */             break;
/*  682 */           buf = buf.unwrap();
/*      */         } }
/*  684 */       byte[] bytes = seq.toString().getBytes(CharsetUtil.US_ASCII);
/*  685 */       buf.writeBytes(bytes);
/*  686 */       return bytes.length;
/*      */     }
/*      */     
/*      */ 
/*  690 */     return len;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static int writeAscii(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len)
/*      */   {
/*  698 */     for (int i = 0; i < len; i++) {
/*  699 */       buffer._setByte(writerIndex++, AsciiString.c2b(seq.charAt(i)));
/*      */     }
/*  701 */     return len;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset)
/*      */   {
/*  709 */     return encodeString0(alloc, false, src, charset, 0);
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
/*      */   public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset, int extraCapacity)
/*      */   {
/*  722 */     return encodeString0(alloc, false, src, charset, extraCapacity);
/*      */   }
/*      */   
/*      */   static ByteBuf encodeString0(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset, int extraCapacity)
/*      */   {
/*  727 */     CharsetEncoder encoder = CharsetUtil.encoder(charset);
/*  728 */     int length = (int)(src.remaining() * encoder.maxBytesPerChar()) + extraCapacity;
/*  729 */     boolean release = true;
/*      */     ByteBuf dst;
/*  731 */     ByteBuf dst; if (enforceHeap) {
/*  732 */       dst = alloc.heapBuffer(length);
/*      */     } else {
/*  734 */       dst = alloc.buffer(length);
/*      */     }
/*      */     try {
/*  737 */       ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
/*  738 */       int pos = dstBuf.position();
/*  739 */       CoderResult cr = encoder.encode(src, dstBuf, true);
/*  740 */       if (!cr.isUnderflow()) {
/*  741 */         cr.throwException();
/*      */       }
/*  743 */       cr = encoder.flush(dstBuf);
/*  744 */       if (!cr.isUnderflow()) {
/*  745 */         cr.throwException();
/*      */       }
/*  747 */       dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
/*  748 */       release = false;
/*  749 */       return dst;
/*      */     } catch (CharacterCodingException x) {
/*  751 */       throw new IllegalStateException(x);
/*      */     } finally {
/*  753 */       if (release) {
/*  754 */         dst.release();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static String decodeString(ByteBuf src, int readerIndex, int len, Charset charset) {
/*  760 */     if (len == 0) {
/*  761 */       return "";
/*      */     }
/*  763 */     CharsetDecoder decoder = CharsetUtil.decoder(charset);
/*  764 */     int maxLength = (int)(len * decoder.maxCharsPerByte());
/*  765 */     CharBuffer dst = (CharBuffer)CHAR_BUFFERS.get();
/*  766 */     if (dst.length() < maxLength) {
/*  767 */       dst = CharBuffer.allocate(maxLength);
/*  768 */       if (maxLength <= MAX_CHAR_BUFFER_SIZE) {
/*  769 */         CHAR_BUFFERS.set(dst);
/*      */       }
/*      */     } else {
/*  772 */       dst.clear();
/*      */     }
/*  774 */     if (src.nioBufferCount() == 1) {
/*  775 */       decodeString(decoder, src.nioBuffer(readerIndex, len), dst);
/*      */     }
/*      */     else
/*      */     {
/*  779 */       ByteBuf buffer = src.alloc().heapBuffer(len);
/*      */       try {
/*  781 */         buffer.writeBytes(src, readerIndex, len);
/*      */         
/*  783 */         decodeString(decoder, buffer.internalNioBuffer(buffer.readerIndex(), len), dst);
/*      */       }
/*      */       finally {
/*  786 */         buffer.release();
/*      */       }
/*      */     }
/*  789 */     return dst.flip().toString();
/*      */   }
/*      */   
/*      */   private static void decodeString(CharsetDecoder decoder, ByteBuffer src, CharBuffer dst) {
/*      */     try {
/*  794 */       CoderResult cr = decoder.decode(src, dst, true);
/*  795 */       if (!cr.isUnderflow()) {
/*  796 */         cr.throwException();
/*      */       }
/*  798 */       cr = decoder.flush(dst);
/*  799 */       if (!cr.isUnderflow()) {
/*  800 */         cr.throwException();
/*      */       }
/*      */     } catch (CharacterCodingException x) {
/*  803 */       throw new IllegalStateException(x);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuf threadLocalDirectBuffer()
/*      */   {
/*  813 */     if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
/*  814 */       return null;
/*      */     }
/*      */     
/*  817 */     if (PlatformDependent.hasUnsafe()) {
/*  818 */       return ThreadLocalUnsafeDirectByteBuf.newInstance();
/*      */     }
/*  820 */     return ThreadLocalDirectByteBuf.newInstance();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] getBytes(ByteBuf buf)
/*      */   {
/*  829 */     return getBytes(buf, buf.readerIndex(), buf.readableBytes());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] getBytes(ByteBuf buf, int start, int length)
/*      */   {
/*  837 */     return getBytes(buf, start, length, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy)
/*      */   {
/*  847 */     if (MathUtil.isOutOfBounds(start, length, buf.capacity()))
/*      */     {
/*  849 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
/*      */     }
/*      */     
/*  852 */     if (buf.hasArray()) {
/*  853 */       if ((copy) || (start != 0) || (length != buf.capacity())) {
/*  854 */         int baseOffset = buf.arrayOffset() + start;
/*  855 */         return Arrays.copyOfRange(buf.array(), baseOffset, baseOffset + length);
/*      */       }
/*  857 */       return buf.array();
/*      */     }
/*      */     
/*      */ 
/*  861 */     byte[] v = new byte[length];
/*  862 */     buf.getBytes(start, v);
/*  863 */     return v;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void copy(AsciiString src, ByteBuf dst)
/*      */   {
/*  873 */     copy(src, 0, dst, src.length());
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
/*      */   public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int dstIdx, int length)
/*      */   {
/*  888 */     if (MathUtil.isOutOfBounds(srcIdx, length, src.length()))
/*      */     {
/*  890 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
/*      */     }
/*      */     
/*  893 */     ((ByteBuf)ObjectUtil.checkNotNull(dst, "dst")).setBytes(dstIdx, src.array(), srcIdx + src.arrayOffset(), length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int length)
/*      */   {
/*  905 */     if (MathUtil.isOutOfBounds(srcIdx, length, src.length()))
/*      */     {
/*  907 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
/*      */     }
/*      */     
/*  910 */     ((ByteBuf)ObjectUtil.checkNotNull(dst, "dst")).writeBytes(src.array(), srcIdx + src.arrayOffset(), length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String prettyHexDump(ByteBuf buffer)
/*      */   {
/*  917 */     return prettyHexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String prettyHexDump(ByteBuf buffer, int offset, int length)
/*      */   {
/*  925 */     return HexUtil.prettyHexDump(buffer, offset, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf)
/*      */   {
/*  933 */     appendPrettyHexDump(dump, buf, buf.readerIndex(), buf.readableBytes());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length)
/*      */   {
/*  942 */     HexUtil.appendPrettyHexDump(dump, buf, offset, length);
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class HexUtil
/*      */   {
/*  948 */     private static final char[] BYTE2CHAR = new char['Ā'];
/*  949 */     private static final char[] HEXDUMP_TABLE = new char['Ѐ'];
/*  950 */     private static final String[] HEXPADDING = new String[16];
/*  951 */     private static final String[] HEXDUMP_ROWPREFIXES = new String['က'];
/*  952 */     private static final String[] BYTE2HEX = new String['Ā'];
/*  953 */     private static final String[] BYTEPADDING = new String[16];
/*      */     
/*      */     static {
/*  956 */       char[] DIGITS = "0123456789abcdef".toCharArray();
/*  957 */       for (int i = 0; i < 256; i++) {
/*  958 */         HEXDUMP_TABLE[(i << 1)] = DIGITS[(i >>> 4 & 0xF)];
/*  959 */         HEXDUMP_TABLE[((i << 1) + 1)] = DIGITS[(i & 0xF)];
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  965 */       for (int i = 0; i < HEXPADDING.length; i++) {
/*  966 */         int padding = HEXPADDING.length - i;
/*  967 */         StringBuilder buf = new StringBuilder(padding * 3);
/*  968 */         for (int j = 0; j < padding; j++) {
/*  969 */           buf.append("   ");
/*      */         }
/*  971 */         HEXPADDING[i] = buf.toString();
/*      */       }
/*      */       
/*      */ 
/*  975 */       for (i = 0; i < HEXDUMP_ROWPREFIXES.length; i++) {
/*  976 */         StringBuilder buf = new StringBuilder(12);
/*  977 */         buf.append(StringUtil.NEWLINE);
/*  978 */         buf.append(Long.toHexString(i << 4 & 0xFFFFFFFF | 0x100000000));
/*  979 */         buf.setCharAt(buf.length() - 9, '|');
/*  980 */         buf.append('|');
/*  981 */         HEXDUMP_ROWPREFIXES[i] = buf.toString();
/*      */       }
/*      */       
/*      */ 
/*  985 */       for (i = 0; i < BYTE2HEX.length; i++) {
/*  986 */         BYTE2HEX[i] = (' ' + StringUtil.byteToHexStringPadded(i));
/*      */       }
/*      */       
/*      */ 
/*  990 */       for (i = 0; i < BYTEPADDING.length; i++) {
/*  991 */         int padding = BYTEPADDING.length - i;
/*  992 */         StringBuilder buf = new StringBuilder(padding);
/*  993 */         for (int j = 0; j < padding; j++) {
/*  994 */           buf.append(' ');
/*      */         }
/*  996 */         BYTEPADDING[i] = buf.toString();
/*      */       }
/*      */       
/*      */ 
/* 1000 */       for (i = 0; i < BYTE2CHAR.length; i++) {
/* 1001 */         if ((i <= 31) || (i >= 127)) {
/* 1002 */           BYTE2CHAR[i] = '.';
/*      */         } else {
/* 1004 */           BYTE2CHAR[i] = ((char)i);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private static String hexDump(ByteBuf buffer, int fromIndex, int length) {
/* 1010 */       if (length < 0) {
/* 1011 */         throw new IllegalArgumentException("length: " + length);
/*      */       }
/* 1013 */       if (length == 0) {
/* 1014 */         return "";
/*      */       }
/*      */       
/* 1017 */       int endIndex = fromIndex + length;
/* 1018 */       char[] buf = new char[length << 1];
/*      */       
/* 1020 */       int srcIdx = fromIndex;
/* 1021 */       for (int dstIdx = 0; 
/* 1022 */           srcIdx < endIndex; dstIdx += 2) {
/* 1023 */         System.arraycopy(HEXDUMP_TABLE, buffer
/* 1024 */           .getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);srcIdx++;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1028 */       return new String(buf);
/*      */     }
/*      */     
/*      */     private static String hexDump(byte[] array, int fromIndex, int length) {
/* 1032 */       if (length < 0) {
/* 1033 */         throw new IllegalArgumentException("length: " + length);
/*      */       }
/* 1035 */       if (length == 0) {
/* 1036 */         return "";
/*      */       }
/*      */       
/* 1039 */       int endIndex = fromIndex + length;
/* 1040 */       char[] buf = new char[length << 1];
/*      */       
/* 1042 */       int srcIdx = fromIndex;
/* 1043 */       for (int dstIdx = 0; 
/* 1044 */           srcIdx < endIndex; dstIdx += 2) {
/* 1045 */         System.arraycopy(HEXDUMP_TABLE, (array[srcIdx] & 0xFF) << 1, buf, dstIdx, 2);srcIdx++;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1050 */       return new String(buf);
/*      */     }
/*      */     
/*      */     private static String prettyHexDump(ByteBuf buffer, int offset, int length) {
/* 1054 */       if (length == 0) {
/* 1055 */         return "";
/*      */       }
/* 1057 */       int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
/* 1058 */       StringBuilder buf = new StringBuilder(rows * 80);
/* 1059 */       appendPrettyHexDump(buf, buffer, offset, length);
/* 1060 */       return buf.toString();
/*      */     }
/*      */     
/*      */     private static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length)
/*      */     {
/* 1065 */       if (MathUtil.isOutOfBounds(offset, length, buf.capacity()))
/*      */       {
/*      */ 
/* 1068 */         throw new IndexOutOfBoundsException("expected: 0 <= offset(" + offset + ") <= offset + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
/*      */       }
/* 1070 */       if (length == 0) {
/* 1071 */         return;
/*      */       }
/* 1073 */       dump.append("         +-------------------------------------------------+" + StringUtil.NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1078 */       int startIndex = offset;
/* 1079 */       int fullRows = length >>> 4;
/* 1080 */       int remainder = length & 0xF;
/*      */       
/*      */ 
/* 1083 */       for (int row = 0; row < fullRows; row++) {
/* 1084 */         int rowStartIndex = (row << 4) + startIndex;
/*      */         
/*      */ 
/* 1087 */         appendHexDumpRowPrefix(dump, row, rowStartIndex);
/*      */         
/*      */ 
/* 1090 */         int rowEndIndex = rowStartIndex + 16;
/* 1091 */         for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 1092 */           dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
/*      */         }
/* 1094 */         dump.append(" |");
/*      */         
/*      */ 
/* 1097 */         for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 1098 */           dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
/*      */         }
/* 1100 */         dump.append('|');
/*      */       }
/*      */       
/*      */ 
/* 1104 */       if (remainder != 0) {
/* 1105 */         int rowStartIndex = (fullRows << 4) + startIndex;
/* 1106 */         appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);
/*      */         
/*      */ 
/* 1109 */         int rowEndIndex = rowStartIndex + remainder;
/* 1110 */         for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 1111 */           dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
/*      */         }
/* 1113 */         dump.append(HEXPADDING[remainder]);
/* 1114 */         dump.append(" |");
/*      */         
/*      */ 
/* 1117 */         for (int j = rowStartIndex; j < rowEndIndex; j++) {
/* 1118 */           dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
/*      */         }
/* 1120 */         dump.append(BYTEPADDING[remainder]);
/* 1121 */         dump.append('|');
/*      */       }
/*      */       
/* 1124 */       dump.append(StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
/*      */     }
/*      */     
/*      */     private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex)
/*      */     {
/* 1129 */       if (row < HEXDUMP_ROWPREFIXES.length) {
/* 1130 */         dump.append(HEXDUMP_ROWPREFIXES[row]);
/*      */       } else {
/* 1132 */         dump.append(StringUtil.NEWLINE);
/* 1133 */         dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFF | 0x100000000));
/* 1134 */         dump.setCharAt(dump.length() - 9, '|');
/* 1135 */         dump.append('|');
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf
/*      */   {
/* 1142 */     private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER = new Recycler()
/*      */     {
/*      */ 
/*      */       protected ByteBufUtil.ThreadLocalUnsafeDirectByteBuf newObject(Recycler.Handle<ByteBufUtil.ThreadLocalUnsafeDirectByteBuf> handle) {
/* 1146 */         return new ByteBufUtil.ThreadLocalUnsafeDirectByteBuf(handle, null); }
/*      */     };
/*      */     private final Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle;
/*      */     
/*      */     static ThreadLocalUnsafeDirectByteBuf newInstance() {
/* 1151 */       ThreadLocalUnsafeDirectByteBuf buf = (ThreadLocalUnsafeDirectByteBuf)RECYCLER.get();
/* 1152 */       buf.setRefCnt(1);
/* 1153 */       return buf;
/*      */     }
/*      */     
/*      */ 
/*      */     private ThreadLocalUnsafeDirectByteBuf(Recycler.Handle<ThreadLocalUnsafeDirectByteBuf> handle)
/*      */     {
/* 1159 */       super(256, Integer.MAX_VALUE);
/* 1160 */       this.handle = handle;
/*      */     }
/*      */     
/*      */     protected void deallocate()
/*      */     {
/* 1165 */       if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
/* 1166 */         super.deallocate();
/*      */       } else {
/* 1168 */         clear();
/* 1169 */         this.handle.recycle(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf
/*      */   {
/* 1176 */     private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER = new Recycler()
/*      */     {
/*      */ 
/* 1179 */       protected ByteBufUtil.ThreadLocalDirectByteBuf newObject(Recycler.Handle<ByteBufUtil.ThreadLocalDirectByteBuf> handle) { return new ByteBufUtil.ThreadLocalDirectByteBuf(handle, null); }
/*      */     };
/*      */     private final Recycler.Handle<ThreadLocalDirectByteBuf> handle;
/*      */     
/*      */     static ThreadLocalDirectByteBuf newInstance() {
/* 1184 */       ThreadLocalDirectByteBuf buf = (ThreadLocalDirectByteBuf)RECYCLER.get();
/* 1185 */       buf.setRefCnt(1);
/* 1186 */       return buf;
/*      */     }
/*      */     
/*      */ 
/*      */     private ThreadLocalDirectByteBuf(Recycler.Handle<ThreadLocalDirectByteBuf> handle)
/*      */     {
/* 1192 */       super(256, Integer.MAX_VALUE);
/* 1193 */       this.handle = handle;
/*      */     }
/*      */     
/*      */     protected void deallocate()
/*      */     {
/* 1198 */       if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
/* 1199 */         super.deallocate();
/*      */       } else {
/* 1201 */         clear();
/* 1202 */         this.handle.recycle(this);
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
/*      */   public static boolean isText(ByteBuf buf, Charset charset)
/*      */   {
/* 1215 */     return isText(buf, buf.readerIndex(), buf.readableBytes(), charset);
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
/*      */   public static boolean isText(ByteBuf buf, int index, int length, Charset charset)
/*      */   {
/* 1230 */     ObjectUtil.checkNotNull(buf, "buf");
/* 1231 */     ObjectUtil.checkNotNull(charset, "charset");
/* 1232 */     int maxIndex = buf.readerIndex() + buf.readableBytes();
/* 1233 */     if ((index < 0) || (length < 0) || (index > maxIndex - length)) {
/* 1234 */       throw new IndexOutOfBoundsException("index: " + index + " length: " + length);
/*      */     }
/* 1236 */     if (charset.equals(CharsetUtil.UTF_8))
/* 1237 */       return isUtf8(buf, index, length);
/* 1238 */     if (charset.equals(CharsetUtil.US_ASCII)) {
/* 1239 */       return isAscii(buf, index, length);
/*      */     }
/* 1241 */     CharsetDecoder decoder = CharsetUtil.decoder(charset, CodingErrorAction.REPORT, CodingErrorAction.REPORT);
/*      */     try {
/* 1243 */       if (buf.nioBufferCount() == 1) {
/* 1244 */         decoder.decode(buf.nioBuffer(index, length));
/*      */       } else {
/* 1246 */         ByteBuf heapBuffer = buf.alloc().heapBuffer(length);
/*      */         try {
/* 1248 */           heapBuffer.writeBytes(buf, index, length);
/* 1249 */           decoder.decode(heapBuffer.internalNioBuffer(heapBuffer.readerIndex(), length));
/*      */         } finally {
/* 1251 */           heapBuffer.release();
/*      */         }
/*      */       }
/* 1254 */       return true;
/*      */     } catch (CharacterCodingException ignore) {}
/* 1256 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1264 */   private static final ByteProcessor FIND_NON_ASCII = new ByteProcessor()
/*      */   {
/*      */     public boolean process(byte value) {
/* 1267 */       return value >= 0;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isAscii(ByteBuf buf, int index, int length)
/*      */   {
/* 1280 */     return buf.forEachByte(index, length, FIND_NON_ASCII) == -1;
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
/*      */   private static boolean isUtf8(ByteBuf buf, int index, int length)
/*      */   {
/* 1327 */     int endIndex = index + length;
/* 1328 */     while (index < endIndex) {
/* 1329 */       byte b1 = buf.getByte(index++);
/*      */       
/* 1331 */       if ((b1 & 0x80) != 0)
/*      */       {
/*      */ 
/*      */ 
/* 1335 */         if ((b1 & 0xE0) == 192)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1341 */           if (index >= endIndex) {
/* 1342 */             return false;
/*      */           }
/* 1344 */           byte b2 = buf.getByte(index++);
/* 1345 */           if ((b2 & 0xC0) != 128) {
/* 1346 */             return false;
/*      */           }
/* 1348 */           if ((b1 & 0xFF) < 194) {
/* 1349 */             return false;
/*      */           }
/* 1351 */         } else if ((b1 & 0xF0) == 224)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1360 */           if (index > endIndex - 2) {
/* 1361 */             return false;
/*      */           }
/* 1363 */           byte b2 = buf.getByte(index++);
/* 1364 */           byte b3 = buf.getByte(index++);
/* 1365 */           if (((b2 & 0xC0) != 128) || ((b3 & 0xC0) != 128)) {
/* 1366 */             return false;
/*      */           }
/* 1368 */           if (((b1 & 0xF) == 0) && ((b2 & 0xFF) < 160)) {
/* 1369 */             return false;
/*      */           }
/* 1371 */           if (((b1 & 0xF) == 13) && ((b2 & 0xFF) > 159)) {
/* 1372 */             return false;
/*      */           }
/* 1374 */         } else if ((b1 & 0xF8) == 240)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1382 */           if (index > endIndex - 3) {
/* 1383 */             return false;
/*      */           }
/* 1385 */           byte b2 = buf.getByte(index++);
/* 1386 */           byte b3 = buf.getByte(index++);
/* 1387 */           byte b4 = buf.getByte(index++);
/* 1388 */           if (((b2 & 0xC0) != 128) || ((b3 & 0xC0) != 128) || ((b4 & 0xC0) != 128))
/*      */           {
/* 1390 */             return false;
/*      */           }
/* 1392 */           if (((b1 & 0xFF) > 244) || (((b1 & 0xFF) == 240) && ((b2 & 0xFF) < 144)) || (((b1 & 0xFF) == 244) && ((b2 & 0xFF) > 143)))
/*      */           {
/*      */ 
/* 1395 */             return false;
/*      */           }
/*      */         } else {
/* 1398 */           return false;
/*      */         } }
/*      */     }
/* 1401 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void readBytes(ByteBufAllocator allocator, ByteBuffer buffer, int position, int length, OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1410 */     if (buffer.hasArray()) {
/* 1411 */       out.write(buffer.array(), position + buffer.arrayOffset(), length);
/*      */     } else {
/* 1413 */       int chunkLen = Math.min(length, 8192);
/* 1414 */       buffer.clear().position(position);
/*      */       
/* 1416 */       if (allocator.isDirectBufferPooled())
/*      */       {
/* 1418 */         ByteBuf tmpBuf = allocator.heapBuffer(chunkLen);
/*      */         try {
/* 1420 */           byte[] tmp = tmpBuf.array();
/* 1421 */           int offset = tmpBuf.arrayOffset();
/* 1422 */           getBytes(buffer, tmp, offset, chunkLen, out, length);
/*      */         } finally {
/* 1424 */           tmpBuf.release();
/*      */         }
/*      */       } else {
/* 1427 */         getBytes(buffer, new byte[chunkLen], 0, chunkLen, out, length);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void getBytes(ByteBuffer inBuffer, byte[] in, int inOffset, int inLen, OutputStream out, int outLen) throws IOException
/*      */   {
/*      */     do {
/* 1435 */       int len = Math.min(inLen, outLen);
/* 1436 */       inBuffer.get(in, inOffset, len);
/* 1437 */       out.write(in, inOffset, len);
/* 1438 */       outLen -= len;
/* 1439 */     } while (outLen > 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\ByteBufUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */