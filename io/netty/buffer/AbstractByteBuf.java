/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.IllegalReferenceCountException;
/*      */ import io.netty.util.ResourceLeakDetector;
/*      */ import io.netty.util.ResourceLeakDetectorFactory;
/*      */ import io.netty.util.internal.MathUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.SystemPropertyUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractByteBuf
/*      */   extends ByteBuf
/*      */ {
/*   45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractByteBuf.class);
/*      */   private static final String LEGACY_PROP_CHECK_ACCESSIBLE = "io.netty.buffer.bytebuf.checkAccessible";
/*      */   private static final String PROP_CHECK_ACCESSIBLE = "io.netty.buffer.checkAccessible";
/*      */   private static final boolean checkAccessible;
/*      */   private static final String PROP_CHECK_BOUNDS = "io.netty.buffer.checkBounds";
/*      */   private static final boolean checkBounds;
/*      */   
/*      */   static {
/*   53 */     if (SystemPropertyUtil.contains("io.netty.buffer.checkAccessible")) {
/*   54 */       checkAccessible = SystemPropertyUtil.getBoolean("io.netty.buffer.checkAccessible", true);
/*      */     } else {
/*   56 */       checkAccessible = SystemPropertyUtil.getBoolean("io.netty.buffer.bytebuf.checkAccessible", true);
/*      */     }
/*   58 */     checkBounds = SystemPropertyUtil.getBoolean("io.netty.buffer.checkBounds", true);
/*   59 */     if (logger.isDebugEnabled()) {
/*   60 */       logger.debug("-D{}: {}", "io.netty.buffer.checkAccessible", Boolean.valueOf(checkAccessible));
/*   61 */       logger.debug("-D{}: {}", "io.netty.buffer.checkBounds", Boolean.valueOf(checkBounds));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*   66 */   static final ResourceLeakDetector<ByteBuf> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ByteBuf.class);
/*      */   int readerIndex;
/*      */   int writerIndex;
/*      */   private int markedReaderIndex;
/*      */   private int markedWriterIndex;
/*      */   private int maxCapacity;
/*      */   
/*      */   protected AbstractByteBuf(int maxCapacity)
/*      */   {
/*   75 */     if (maxCapacity < 0) {
/*   76 */       throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected: >= 0)");
/*      */     }
/*   78 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */   
/*      */   public boolean isReadOnly()
/*      */   {
/*   83 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuf asReadOnly()
/*      */   {
/*   89 */     if (isReadOnly()) {
/*   90 */       return this;
/*      */     }
/*   92 */     return Unpooled.unmodifiableBuffer(this);
/*      */   }
/*      */   
/*      */   public int maxCapacity()
/*      */   {
/*   97 */     return this.maxCapacity;
/*      */   }
/*      */   
/*      */   protected final void maxCapacity(int maxCapacity) {
/*  101 */     this.maxCapacity = maxCapacity;
/*      */   }
/*      */   
/*      */   public int readerIndex()
/*      */   {
/*  106 */     return this.readerIndex;
/*      */   }
/*      */   
/*      */   private static void checkIndexBounds(int readerIndex, int writerIndex, int capacity) {
/*  110 */     if ((readerIndex < 0) || (readerIndex > writerIndex) || (writerIndex > capacity)) {
/*  111 */       throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", new Object[] {
/*      */       
/*  113 */         Integer.valueOf(readerIndex), Integer.valueOf(writerIndex), Integer.valueOf(capacity) }));
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf readerIndex(int readerIndex)
/*      */   {
/*  119 */     if (checkBounds) {
/*  120 */       checkIndexBounds(readerIndex, this.writerIndex, capacity());
/*      */     }
/*  122 */     this.readerIndex = readerIndex;
/*  123 */     return this;
/*      */   }
/*      */   
/*      */   public int writerIndex()
/*      */   {
/*  128 */     return this.writerIndex;
/*      */   }
/*      */   
/*      */   public ByteBuf writerIndex(int writerIndex)
/*      */   {
/*  133 */     if (checkBounds) {
/*  134 */       checkIndexBounds(this.readerIndex, writerIndex, capacity());
/*      */     }
/*  136 */     this.writerIndex = writerIndex;
/*  137 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/*  142 */     if (checkBounds) {
/*  143 */       checkIndexBounds(readerIndex, writerIndex, capacity());
/*      */     }
/*  145 */     setIndex0(readerIndex, writerIndex);
/*  146 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf clear()
/*      */   {
/*  151 */     this.readerIndex = (this.writerIndex = 0);
/*  152 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isReadable()
/*      */   {
/*  157 */     return this.writerIndex > this.readerIndex;
/*      */   }
/*      */   
/*      */   public boolean isReadable(int numBytes)
/*      */   {
/*  162 */     return this.writerIndex - this.readerIndex >= numBytes;
/*      */   }
/*      */   
/*      */   public boolean isWritable()
/*      */   {
/*  167 */     return capacity() > this.writerIndex;
/*      */   }
/*      */   
/*      */   public boolean isWritable(int numBytes)
/*      */   {
/*  172 */     return capacity() - this.writerIndex >= numBytes;
/*      */   }
/*      */   
/*      */   public int readableBytes()
/*      */   {
/*  177 */     return this.writerIndex - this.readerIndex;
/*      */   }
/*      */   
/*      */   public int writableBytes()
/*      */   {
/*  182 */     return capacity() - this.writerIndex;
/*      */   }
/*      */   
/*      */   public int maxWritableBytes()
/*      */   {
/*  187 */     return maxCapacity() - this.writerIndex;
/*      */   }
/*      */   
/*      */   public ByteBuf markReaderIndex()
/*      */   {
/*  192 */     this.markedReaderIndex = this.readerIndex;
/*  193 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf resetReaderIndex()
/*      */   {
/*  198 */     readerIndex(this.markedReaderIndex);
/*  199 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf markWriterIndex()
/*      */   {
/*  204 */     this.markedWriterIndex = this.writerIndex;
/*  205 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf resetWriterIndex()
/*      */   {
/*  210 */     writerIndex(this.markedWriterIndex);
/*  211 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf discardReadBytes()
/*      */   {
/*  216 */     ensureAccessible();
/*  217 */     if (this.readerIndex == 0) {
/*  218 */       return this;
/*      */     }
/*      */     
/*  221 */     if (this.readerIndex != this.writerIndex) {
/*  222 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  223 */       this.writerIndex -= this.readerIndex;
/*  224 */       adjustMarkers(this.readerIndex);
/*  225 */       this.readerIndex = 0;
/*      */     } else {
/*  227 */       adjustMarkers(this.readerIndex);
/*  228 */       this.writerIndex = (this.readerIndex = 0);
/*      */     }
/*  230 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf discardSomeReadBytes()
/*      */   {
/*  235 */     ensureAccessible();
/*  236 */     if (this.readerIndex == 0) {
/*  237 */       return this;
/*      */     }
/*      */     
/*  240 */     if (this.readerIndex == this.writerIndex) {
/*  241 */       adjustMarkers(this.readerIndex);
/*  242 */       this.writerIndex = (this.readerIndex = 0);
/*  243 */       return this;
/*      */     }
/*      */     
/*  246 */     if (this.readerIndex >= capacity() >>> 1) {
/*  247 */       setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
/*  248 */       this.writerIndex -= this.readerIndex;
/*  249 */       adjustMarkers(this.readerIndex);
/*  250 */       this.readerIndex = 0;
/*      */     }
/*  252 */     return this;
/*      */   }
/*      */   
/*      */   protected final void adjustMarkers(int decrement) {
/*  256 */     int markedReaderIndex = this.markedReaderIndex;
/*  257 */     if (markedReaderIndex <= decrement) {
/*  258 */       this.markedReaderIndex = 0;
/*  259 */       int markedWriterIndex = this.markedWriterIndex;
/*  260 */       if (markedWriterIndex <= decrement) {
/*  261 */         this.markedWriterIndex = 0;
/*      */       } else {
/*  263 */         this.markedWriterIndex = (markedWriterIndex - decrement);
/*      */       }
/*      */     } else {
/*  266 */       this.markedReaderIndex = (markedReaderIndex - decrement);
/*  267 */       this.markedWriterIndex -= decrement;
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf ensureWritable(int minWritableBytes)
/*      */   {
/*  273 */     if (minWritableBytes < 0) {
/*  274 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] {
/*  275 */         Integer.valueOf(minWritableBytes) }));
/*      */     }
/*  277 */     ensureWritable0(minWritableBytes);
/*  278 */     return this;
/*      */   }
/*      */   
/*      */   final void ensureWritable0(int minWritableBytes) {
/*  282 */     ensureAccessible();
/*  283 */     if (minWritableBytes <= writableBytes()) {
/*  284 */       return;
/*      */     }
/*  286 */     if ((checkBounds) && 
/*  287 */       (minWritableBytes > this.maxCapacity - this.writerIndex)) {
/*  288 */       throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", new Object[] {
/*      */       
/*  290 */         Integer.valueOf(this.writerIndex), Integer.valueOf(minWritableBytes), Integer.valueOf(this.maxCapacity), this }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  295 */     int newCapacity = alloc().calculateNewCapacity(this.writerIndex + minWritableBytes, this.maxCapacity);
/*      */     
/*      */ 
/*  298 */     capacity(newCapacity);
/*      */   }
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force)
/*      */   {
/*  303 */     ensureAccessible();
/*  304 */     if (minWritableBytes < 0) {
/*  305 */       throw new IllegalArgumentException(String.format("minWritableBytes: %d (expected: >= 0)", new Object[] {
/*  306 */         Integer.valueOf(minWritableBytes) }));
/*      */     }
/*      */     
/*  309 */     if (minWritableBytes <= writableBytes()) {
/*  310 */       return 0;
/*      */     }
/*      */     
/*  313 */     int maxCapacity = maxCapacity();
/*  314 */     int writerIndex = writerIndex();
/*  315 */     if (minWritableBytes > maxCapacity - writerIndex) {
/*  316 */       if ((!force) || (capacity() == maxCapacity)) {
/*  317 */         return 1;
/*      */       }
/*      */       
/*  320 */       capacity(maxCapacity);
/*  321 */       return 3;
/*      */     }
/*      */     
/*      */ 
/*  325 */     int newCapacity = alloc().calculateNewCapacity(writerIndex + minWritableBytes, maxCapacity);
/*      */     
/*      */ 
/*  328 */     capacity(newCapacity);
/*  329 */     return 2;
/*      */   }
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness)
/*      */   {
/*  334 */     if (endianness == null) {
/*  335 */       throw new NullPointerException("endianness");
/*      */     }
/*  337 */     if (endianness == order()) {
/*  338 */       return this;
/*      */     }
/*  340 */     return newSwappedByteBuf();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SwappedByteBuf newSwappedByteBuf()
/*      */   {
/*  347 */     return new SwappedByteBuf(this);
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  352 */     checkIndex(index);
/*  353 */     return _getByte(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getBoolean(int index)
/*      */   {
/*  360 */     return getByte(index) != 0;
/*      */   }
/*      */   
/*      */   public short getUnsignedByte(int index)
/*      */   {
/*  365 */     return (short)(getByte(index) & 0xFF);
/*      */   }
/*      */   
/*      */   public short getShort(int index)
/*      */   {
/*  370 */     checkIndex(index, 2);
/*  371 */     return _getShort(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short getShortLE(int index)
/*      */   {
/*  378 */     checkIndex(index, 2);
/*  379 */     return _getShortLE(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getUnsignedShort(int index)
/*      */   {
/*  386 */     return getShort(index) & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int getUnsignedShortLE(int index)
/*      */   {
/*  391 */     return getShortLE(index) & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int getUnsignedMedium(int index)
/*      */   {
/*  396 */     checkIndex(index, 3);
/*  397 */     return _getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getUnsignedMediumLE(int index)
/*      */   {
/*  404 */     checkIndex(index, 3);
/*  405 */     return _getUnsignedMediumLE(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getMedium(int index)
/*      */   {
/*  412 */     int value = getUnsignedMedium(index);
/*  413 */     if ((value & 0x800000) != 0) {
/*  414 */       value |= 0xFF000000;
/*      */     }
/*  416 */     return value;
/*      */   }
/*      */   
/*      */   public int getMediumLE(int index)
/*      */   {
/*  421 */     int value = getUnsignedMediumLE(index);
/*  422 */     if ((value & 0x800000) != 0) {
/*  423 */       value |= 0xFF000000;
/*      */     }
/*  425 */     return value;
/*      */   }
/*      */   
/*      */   public int getInt(int index)
/*      */   {
/*  430 */     checkIndex(index, 4);
/*  431 */     return _getInt(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getIntLE(int index)
/*      */   {
/*  438 */     checkIndex(index, 4);
/*  439 */     return _getIntLE(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getUnsignedInt(int index)
/*      */   {
/*  446 */     return getInt(index) & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long getUnsignedIntLE(int index)
/*      */   {
/*  451 */     return getIntLE(index) & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long getLong(int index)
/*      */   {
/*  456 */     checkIndex(index, 8);
/*  457 */     return _getLong(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getLongLE(int index)
/*      */   {
/*  464 */     checkIndex(index, 8);
/*  465 */     return _getLongLE(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public char getChar(int index)
/*      */   {
/*  472 */     return (char)getShort(index);
/*      */   }
/*      */   
/*      */   public float getFloat(int index)
/*      */   {
/*  477 */     return Float.intBitsToFloat(getInt(index));
/*      */   }
/*      */   
/*      */   public double getDouble(int index)
/*      */   {
/*  482 */     return Double.longBitsToDouble(getLong(index));
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, byte[] dst)
/*      */   {
/*  487 */     getBytes(index, dst, 0, dst.length);
/*  488 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/*  493 */     getBytes(index, dst, dst.writableBytes());
/*  494 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/*  499 */     getBytes(index, dst, dst.writerIndex(), length);
/*  500 */     dst.writerIndex(dst.writerIndex() + length);
/*  501 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset)
/*      */   {
/*  507 */     return toString(index, length, charset);
/*      */   }
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset)
/*      */   {
/*  512 */     CharSequence sequence = getCharSequence(this.readerIndex, length, charset);
/*  513 */     this.readerIndex += length;
/*  514 */     return sequence;
/*      */   }
/*      */   
/*      */   public ByteBuf setByte(int index, int value)
/*      */   {
/*  519 */     checkIndex(index);
/*  520 */     _setByte(index, value);
/*  521 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setBoolean(int index, boolean value)
/*      */   {
/*  528 */     setByte(index, value ? 1 : 0);
/*  529 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setShort(int index, int value)
/*      */   {
/*  534 */     checkIndex(index, 2);
/*  535 */     _setShort(index, value);
/*  536 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setShortLE(int index, int value)
/*      */   {
/*  543 */     checkIndex(index, 2);
/*  544 */     _setShortLE(index, value);
/*  545 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setChar(int index, int value)
/*      */   {
/*  552 */     setShort(index, value);
/*  553 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setMedium(int index, int value)
/*      */   {
/*  558 */     checkIndex(index, 3);
/*  559 */     _setMedium(index, value);
/*  560 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setMediumLE(int index, int value)
/*      */   {
/*  567 */     checkIndex(index, 3);
/*  568 */     _setMediumLE(index, value);
/*  569 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setInt(int index, int value)
/*      */   {
/*  576 */     checkIndex(index, 4);
/*  577 */     _setInt(index, value);
/*  578 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setIntLE(int index, int value)
/*      */   {
/*  585 */     checkIndex(index, 4);
/*  586 */     _setIntLE(index, value);
/*  587 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setFloat(int index, float value)
/*      */   {
/*  594 */     setInt(index, Float.floatToRawIntBits(value));
/*  595 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setLong(int index, long value)
/*      */   {
/*  600 */     checkIndex(index, 8);
/*  601 */     _setLong(index, value);
/*  602 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setLongLE(int index, long value)
/*      */   {
/*  609 */     checkIndex(index, 8);
/*  610 */     _setLongLE(index, value);
/*  611 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuf setDouble(int index, double value)
/*      */   {
/*  618 */     setLong(index, Double.doubleToRawLongBits(value));
/*  619 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, byte[] src)
/*      */   {
/*  624 */     setBytes(index, src, 0, src.length);
/*  625 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/*  630 */     setBytes(index, src, src.readableBytes());
/*  631 */     return this;
/*      */   }
/*      */   
/*      */   private static void checkReadableBounds(ByteBuf src, int length) {
/*  635 */     if (length > src.readableBytes()) {
/*  636 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", new Object[] {
/*  637 */         Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src }));
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/*  643 */     checkIndex(index, length);
/*  644 */     if (src == null) {
/*  645 */       throw new NullPointerException("src");
/*      */     }
/*  647 */     if (checkBounds) {
/*  648 */       checkReadableBounds(src, length);
/*      */     }
/*      */     
/*  651 */     setBytes(index, src, src.readerIndex(), length);
/*  652 */     src.readerIndex(src.readerIndex() + length);
/*  653 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf setZero(int index, int length)
/*      */   {
/*  658 */     if (length == 0) {
/*  659 */       return this;
/*      */     }
/*      */     
/*  662 */     checkIndex(index, length);
/*      */     
/*  664 */     int nLong = length >>> 3;
/*  665 */     int nBytes = length & 0x7;
/*  666 */     for (int i = nLong; i > 0; i--) {
/*  667 */       _setLong(index, 0L);
/*  668 */       index += 8;
/*      */     }
/*  670 */     if (nBytes == 4) {
/*  671 */       _setInt(index, 0);
/*      */     }
/*  673 */     else if (nBytes < 4) {
/*  674 */       for (int i = nBytes; i > 0; i--) {
/*  675 */         _setByte(index, 0);
/*  676 */         index++;
/*      */       }
/*      */     } else {
/*  679 */       _setInt(index, 0);
/*  680 */       index += 4;
/*  681 */       for (int i = nBytes - 4; i > 0; i--) {
/*  682 */         _setByte(index, 0);
/*  683 */         index++;
/*      */       }
/*      */     }
/*  686 */     return this;
/*      */   }
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset)
/*      */   {
/*  691 */     return setCharSequence0(index, sequence, charset, false);
/*      */   }
/*      */   
/*      */   private int setCharSequence0(int index, CharSequence sequence, Charset charset, boolean expand) {
/*  695 */     if (charset.equals(CharsetUtil.UTF_8)) {
/*  696 */       int length = ByteBufUtil.utf8MaxBytes(sequence);
/*  697 */       if (expand) {
/*  698 */         ensureWritable0(length);
/*  699 */         checkIndex0(index, length);
/*      */       } else {
/*  701 */         checkIndex(index, length);
/*      */       }
/*  703 */       return ByteBufUtil.writeUtf8(this, index, sequence, sequence.length());
/*      */     }
/*  705 */     if ((charset.equals(CharsetUtil.US_ASCII)) || (charset.equals(CharsetUtil.ISO_8859_1))) {
/*  706 */       int length = sequence.length();
/*  707 */       if (expand) {
/*  708 */         ensureWritable0(length);
/*  709 */         checkIndex0(index, length);
/*      */       } else {
/*  711 */         checkIndex(index, length);
/*      */       }
/*  713 */       return ByteBufUtil.writeAscii(this, index, sequence, length);
/*      */     }
/*  715 */     byte[] bytes = sequence.toString().getBytes(charset);
/*  716 */     if (expand) {
/*  717 */       ensureWritable0(bytes.length);
/*      */     }
/*      */     
/*  720 */     setBytes(index, bytes);
/*  721 */     return bytes.length;
/*      */   }
/*      */   
/*      */   public byte readByte()
/*      */   {
/*  726 */     checkReadableBytes0(1);
/*  727 */     int i = this.readerIndex;
/*  728 */     byte b = _getByte(i);
/*  729 */     this.readerIndex = (i + 1);
/*  730 */     return b;
/*      */   }
/*      */   
/*      */   public boolean readBoolean()
/*      */   {
/*  735 */     return readByte() != 0;
/*      */   }
/*      */   
/*      */   public short readUnsignedByte()
/*      */   {
/*  740 */     return (short)(readByte() & 0xFF);
/*      */   }
/*      */   
/*      */   public short readShort()
/*      */   {
/*  745 */     checkReadableBytes0(2);
/*  746 */     short v = _getShort(this.readerIndex);
/*  747 */     this.readerIndex += 2;
/*  748 */     return v;
/*      */   }
/*      */   
/*      */   public short readShortLE()
/*      */   {
/*  753 */     checkReadableBytes0(2);
/*  754 */     short v = _getShortLE(this.readerIndex);
/*  755 */     this.readerIndex += 2;
/*  756 */     return v;
/*      */   }
/*      */   
/*      */   public int readUnsignedShort()
/*      */   {
/*  761 */     return readShort() & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int readUnsignedShortLE()
/*      */   {
/*  766 */     return readShortLE() & 0xFFFF;
/*      */   }
/*      */   
/*      */   public int readMedium()
/*      */   {
/*  771 */     int value = readUnsignedMedium();
/*  772 */     if ((value & 0x800000) != 0) {
/*  773 */       value |= 0xFF000000;
/*      */     }
/*  775 */     return value;
/*      */   }
/*      */   
/*      */   public int readMediumLE()
/*      */   {
/*  780 */     int value = readUnsignedMediumLE();
/*  781 */     if ((value & 0x800000) != 0) {
/*  782 */       value |= 0xFF000000;
/*      */     }
/*  784 */     return value;
/*      */   }
/*      */   
/*      */   public int readUnsignedMedium()
/*      */   {
/*  789 */     checkReadableBytes0(3);
/*  790 */     int v = _getUnsignedMedium(this.readerIndex);
/*  791 */     this.readerIndex += 3;
/*  792 */     return v;
/*      */   }
/*      */   
/*      */   public int readUnsignedMediumLE()
/*      */   {
/*  797 */     checkReadableBytes0(3);
/*  798 */     int v = _getUnsignedMediumLE(this.readerIndex);
/*  799 */     this.readerIndex += 3;
/*  800 */     return v;
/*      */   }
/*      */   
/*      */   public int readInt()
/*      */   {
/*  805 */     checkReadableBytes0(4);
/*  806 */     int v = _getInt(this.readerIndex);
/*  807 */     this.readerIndex += 4;
/*  808 */     return v;
/*      */   }
/*      */   
/*      */   public int readIntLE()
/*      */   {
/*  813 */     checkReadableBytes0(4);
/*  814 */     int v = _getIntLE(this.readerIndex);
/*  815 */     this.readerIndex += 4;
/*  816 */     return v;
/*      */   }
/*      */   
/*      */   public long readUnsignedInt()
/*      */   {
/*  821 */     return readInt() & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long readUnsignedIntLE()
/*      */   {
/*  826 */     return readIntLE() & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */   public long readLong()
/*      */   {
/*  831 */     checkReadableBytes0(8);
/*  832 */     long v = _getLong(this.readerIndex);
/*  833 */     this.readerIndex += 8;
/*  834 */     return v;
/*      */   }
/*      */   
/*      */   public long readLongLE()
/*      */   {
/*  839 */     checkReadableBytes0(8);
/*  840 */     long v = _getLongLE(this.readerIndex);
/*  841 */     this.readerIndex += 8;
/*  842 */     return v;
/*      */   }
/*      */   
/*      */   public char readChar()
/*      */   {
/*  847 */     return (char)readShort();
/*      */   }
/*      */   
/*      */   public float readFloat()
/*      */   {
/*  852 */     return Float.intBitsToFloat(readInt());
/*      */   }
/*      */   
/*      */   public double readDouble()
/*      */   {
/*  857 */     return Double.longBitsToDouble(readLong());
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(int length)
/*      */   {
/*  862 */     checkReadableBytes(length);
/*  863 */     if (length == 0) {
/*  864 */       return Unpooled.EMPTY_BUFFER;
/*      */     }
/*      */     
/*  867 */     ByteBuf buf = alloc().buffer(length, this.maxCapacity);
/*  868 */     buf.writeBytes(this, this.readerIndex, length);
/*  869 */     this.readerIndex += length;
/*  870 */     return buf;
/*      */   }
/*      */   
/*      */   public ByteBuf readSlice(int length)
/*      */   {
/*  875 */     checkReadableBytes(length);
/*  876 */     ByteBuf slice = slice(this.readerIndex, length);
/*  877 */     this.readerIndex += length;
/*  878 */     return slice;
/*      */   }
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length)
/*      */   {
/*  883 */     checkReadableBytes(length);
/*  884 */     ByteBuf slice = retainedSlice(this.readerIndex, length);
/*  885 */     this.readerIndex += length;
/*  886 */     return slice;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/*  891 */     checkReadableBytes(length);
/*  892 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  893 */     this.readerIndex += length;
/*  894 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(byte[] dst)
/*      */   {
/*  899 */     readBytes(dst, 0, dst.length);
/*  900 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst)
/*      */   {
/*  905 */     readBytes(dst, dst.writableBytes());
/*  906 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/*  911 */     if ((checkBounds) && 
/*  912 */       (length > dst.writableBytes())) {
/*  913 */       throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", new Object[] {
/*  914 */         Integer.valueOf(length), Integer.valueOf(dst.writableBytes()), dst }));
/*      */     }
/*      */     
/*  917 */     readBytes(dst, dst.writerIndex(), length);
/*  918 */     dst.writerIndex(dst.writerIndex() + length);
/*  919 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  924 */     checkReadableBytes(length);
/*  925 */     getBytes(this.readerIndex, dst, dstIndex, length);
/*  926 */     this.readerIndex += length;
/*  927 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer dst)
/*      */   {
/*  932 */     int length = dst.remaining();
/*  933 */     checkReadableBytes(length);
/*  934 */     getBytes(this.readerIndex, dst);
/*  935 */     this.readerIndex += length;
/*  936 */     return this;
/*      */   }
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length)
/*      */     throws IOException
/*      */   {
/*  942 */     checkReadableBytes(length);
/*  943 */     int readBytes = getBytes(this.readerIndex, out, length);
/*  944 */     this.readerIndex += readBytes;
/*  945 */     return readBytes;
/*      */   }
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length)
/*      */     throws IOException
/*      */   {
/*  951 */     checkReadableBytes(length);
/*  952 */     int readBytes = getBytes(this.readerIndex, out, position, length);
/*  953 */     this.readerIndex += readBytes;
/*  954 */     return readBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(OutputStream out, int length) throws IOException
/*      */   {
/*  959 */     checkReadableBytes(length);
/*  960 */     getBytes(this.readerIndex, out, length);
/*  961 */     this.readerIndex += length;
/*  962 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf skipBytes(int length)
/*      */   {
/*  967 */     checkReadableBytes(length);
/*  968 */     this.readerIndex += length;
/*  969 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBoolean(boolean value)
/*      */   {
/*  974 */     writeByte(value ? 1 : 0);
/*  975 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeByte(int value)
/*      */   {
/*  980 */     ensureWritable0(1);
/*  981 */     _setByte(this.writerIndex++, value);
/*  982 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeShort(int value)
/*      */   {
/*  987 */     ensureWritable0(2);
/*  988 */     _setShort(this.writerIndex, value);
/*  989 */     this.writerIndex += 2;
/*  990 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeShortLE(int value)
/*      */   {
/*  995 */     ensureWritable0(2);
/*  996 */     _setShortLE(this.writerIndex, value);
/*  997 */     this.writerIndex += 2;
/*  998 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeMedium(int value)
/*      */   {
/* 1003 */     ensureWritable0(3);
/* 1004 */     _setMedium(this.writerIndex, value);
/* 1005 */     this.writerIndex += 3;
/* 1006 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeMediumLE(int value)
/*      */   {
/* 1011 */     ensureWritable0(3);
/* 1012 */     _setMediumLE(this.writerIndex, value);
/* 1013 */     this.writerIndex += 3;
/* 1014 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeInt(int value)
/*      */   {
/* 1019 */     ensureWritable0(4);
/* 1020 */     _setInt(this.writerIndex, value);
/* 1021 */     this.writerIndex += 4;
/* 1022 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeIntLE(int value)
/*      */   {
/* 1027 */     ensureWritable0(4);
/* 1028 */     _setIntLE(this.writerIndex, value);
/* 1029 */     this.writerIndex += 4;
/* 1030 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeLong(long value)
/*      */   {
/* 1035 */     ensureWritable0(8);
/* 1036 */     _setLong(this.writerIndex, value);
/* 1037 */     this.writerIndex += 8;
/* 1038 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeLongLE(long value)
/*      */   {
/* 1043 */     ensureWritable0(8);
/* 1044 */     _setLongLE(this.writerIndex, value);
/* 1045 */     this.writerIndex += 8;
/* 1046 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeChar(int value)
/*      */   {
/* 1051 */     writeShort(value);
/* 1052 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeFloat(float value)
/*      */   {
/* 1057 */     writeInt(Float.floatToRawIntBits(value));
/* 1058 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeDouble(double value)
/*      */   {
/* 1063 */     writeLong(Double.doubleToRawLongBits(value));
/* 1064 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/* 1069 */     ensureWritable(length);
/* 1070 */     setBytes(this.writerIndex, src, srcIndex, length);
/* 1071 */     this.writerIndex += length;
/* 1072 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(byte[] src)
/*      */   {
/* 1077 */     writeBytes(src, 0, src.length);
/* 1078 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src)
/*      */   {
/* 1083 */     writeBytes(src, src.readableBytes());
/* 1084 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/* 1089 */     if (checkBounds) {
/* 1090 */       checkReadableBounds(src, length);
/*      */     }
/* 1092 */     writeBytes(src, src.readerIndex(), length);
/* 1093 */     src.readerIndex(src.readerIndex() + length);
/* 1094 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/* 1099 */     ensureWritable(length);
/* 1100 */     setBytes(this.writerIndex, src, srcIndex, length);
/* 1101 */     this.writerIndex += length;
/* 1102 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer src)
/*      */   {
/* 1107 */     int length = src.remaining();
/* 1108 */     ensureWritable0(length);
/* 1109 */     setBytes(this.writerIndex, src);
/* 1110 */     this.writerIndex += length;
/* 1111 */     return this;
/*      */   }
/*      */   
/*      */   public int writeBytes(InputStream in, int length)
/*      */     throws IOException
/*      */   {
/* 1117 */     ensureWritable(length);
/* 1118 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/* 1119 */     if (writtenBytes > 0) {
/* 1120 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1122 */     return writtenBytes;
/*      */   }
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*      */   {
/* 1127 */     ensureWritable(length);
/* 1128 */     int writtenBytes = setBytes(this.writerIndex, in, length);
/* 1129 */     if (writtenBytes > 0) {
/* 1130 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1132 */     return writtenBytes;
/*      */   }
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException
/*      */   {
/* 1137 */     ensureWritable(length);
/* 1138 */     int writtenBytes = setBytes(this.writerIndex, in, position, length);
/* 1139 */     if (writtenBytes > 0) {
/* 1140 */       this.writerIndex += writtenBytes;
/*      */     }
/* 1142 */     return writtenBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf writeZero(int length)
/*      */   {
/* 1147 */     if (length == 0) {
/* 1148 */       return this;
/*      */     }
/*      */     
/* 1151 */     ensureWritable(length);
/* 1152 */     int wIndex = this.writerIndex;
/* 1153 */     checkIndex0(wIndex, length);
/*      */     
/* 1155 */     int nLong = length >>> 3;
/* 1156 */     int nBytes = length & 0x7;
/* 1157 */     for (int i = nLong; i > 0; i--) {
/* 1158 */       _setLong(wIndex, 0L);
/* 1159 */       wIndex += 8;
/*      */     }
/* 1161 */     if (nBytes == 4) {
/* 1162 */       _setInt(wIndex, 0);
/* 1163 */       wIndex += 4;
/* 1164 */     } else if (nBytes < 4) {
/* 1165 */       for (int i = nBytes; i > 0; i--) {
/* 1166 */         _setByte(wIndex, 0);
/* 1167 */         wIndex++;
/*      */       }
/*      */     } else {
/* 1170 */       _setInt(wIndex, 0);
/* 1171 */       wIndex += 4;
/* 1172 */       for (int i = nBytes - 4; i > 0; i--) {
/* 1173 */         _setByte(wIndex, 0);
/* 1174 */         wIndex++;
/*      */       }
/*      */     }
/* 1177 */     this.writerIndex = wIndex;
/* 1178 */     return this;
/*      */   }
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset)
/*      */   {
/* 1183 */     int written = setCharSequence0(this.writerIndex, sequence, charset, true);
/* 1184 */     this.writerIndex += written;
/* 1185 */     return written;
/*      */   }
/*      */   
/*      */   public ByteBuf copy()
/*      */   {
/* 1190 */     return copy(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuf duplicate()
/*      */   {
/* 1195 */     ensureAccessible();
/* 1196 */     return new UnpooledDuplicatedByteBuf(this);
/*      */   }
/*      */   
/*      */   public ByteBuf retainedDuplicate()
/*      */   {
/* 1201 */     return duplicate().retain();
/*      */   }
/*      */   
/*      */   public ByteBuf slice()
/*      */   {
/* 1206 */     return slice(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice()
/*      */   {
/* 1211 */     return slice().retain();
/*      */   }
/*      */   
/*      */   public ByteBuf slice(int index, int length)
/*      */   {
/* 1216 */     ensureAccessible();
/* 1217 */     return new UnpooledSlicedByteBuf(this, index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length)
/*      */   {
/* 1222 */     return slice(index, length).retain();
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer()
/*      */   {
/* 1227 */     return nioBuffer(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/* 1232 */     return nioBuffers(this.readerIndex, readableBytes());
/*      */   }
/*      */   
/*      */   public String toString(Charset charset)
/*      */   {
/* 1237 */     return toString(this.readerIndex, readableBytes(), charset);
/*      */   }
/*      */   
/*      */   public String toString(int index, int length, Charset charset)
/*      */   {
/* 1242 */     return ByteBufUtil.decodeString(this, index, length, charset);
/*      */   }
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value)
/*      */   {
/* 1247 */     return ByteBufUtil.indexOf(this, fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(byte value)
/*      */   {
/* 1252 */     return bytesBefore(readerIndex(), readableBytes(), value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int length, byte value)
/*      */   {
/* 1257 */     checkReadableBytes(length);
/* 1258 */     return bytesBefore(readerIndex(), length, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value)
/*      */   {
/* 1263 */     int endIndex = indexOf(index, index + length, value);
/* 1264 */     if (endIndex < 0) {
/* 1265 */       return -1;
/*      */     }
/* 1267 */     return endIndex - index;
/*      */   }
/*      */   
/*      */   public int forEachByte(ByteProcessor processor)
/*      */   {
/* 1272 */     ensureAccessible();
/*      */     try {
/* 1274 */       return forEachByteAsc0(this.readerIndex, this.writerIndex, processor);
/*      */     } catch (Exception e) {
/* 1276 */       PlatformDependent.throwException(e); }
/* 1277 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByte(int index, int length, ByteProcessor processor)
/*      */   {
/* 1283 */     checkIndex(index, length);
/*      */     try {
/* 1285 */       return forEachByteAsc0(index, index + length, processor);
/*      */     } catch (Exception e) {
/* 1287 */       PlatformDependent.throwException(e); }
/* 1288 */     return -1;
/*      */   }
/*      */   
/*      */   private int forEachByteAsc0(int start, int end, ByteProcessor processor) throws Exception
/*      */   {
/* 1293 */     for (; start < end; start++) {
/* 1294 */       if (!processor.process(_getByte(start))) {
/* 1295 */         return start;
/*      */       }
/*      */     }
/*      */     
/* 1299 */     return -1;
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor)
/*      */   {
/* 1304 */     ensureAccessible();
/*      */     try {
/* 1306 */       return forEachByteDesc0(this.writerIndex - 1, this.readerIndex, processor);
/*      */     } catch (Exception e) {
/* 1308 */       PlatformDependent.throwException(e); }
/* 1309 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor)
/*      */   {
/* 1315 */     checkIndex(index, length);
/*      */     try {
/* 1317 */       return forEachByteDesc0(index + length - 1, index, processor);
/*      */     } catch (Exception e) {
/* 1319 */       PlatformDependent.throwException(e); }
/* 1320 */     return -1;
/*      */   }
/*      */   
/*      */   private int forEachByteDesc0(int rStart, int rEnd, ByteProcessor processor) throws Exception
/*      */   {
/* 1325 */     for (; rStart >= rEnd; rStart--) {
/* 1326 */       if (!processor.process(_getByte(rStart))) {
/* 1327 */         return rStart;
/*      */       }
/*      */     }
/* 1330 */     return -1;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1335 */     return ByteBufUtil.hashCode(this);
/*      */   }
/*      */   
/*      */   public boolean equals(Object o)
/*      */   {
/* 1340 */     return (this == o) || (((o instanceof ByteBuf)) && (ByteBufUtil.equals(this, (ByteBuf)o)));
/*      */   }
/*      */   
/*      */   public int compareTo(ByteBuf that)
/*      */   {
/* 1345 */     return ByteBufUtil.compare(this, that);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1350 */     if (refCnt() == 0) {
/* 1351 */       return StringUtil.simpleClassName(this) + "(freed)";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1358 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(ridx: ").append(this.readerIndex).append(", widx: ").append(this.writerIndex).append(", cap: ").append(capacity());
/* 1359 */     if (this.maxCapacity != Integer.MAX_VALUE) {
/* 1360 */       buf.append('/').append(this.maxCapacity);
/*      */     }
/*      */     
/* 1363 */     ByteBuf unwrapped = unwrap();
/* 1364 */     if (unwrapped != null) {
/* 1365 */       buf.append(", unwrapped: ").append(unwrapped);
/*      */     }
/* 1367 */     buf.append(')');
/* 1368 */     return buf.toString();
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index) {
/* 1372 */     checkIndex(index, 1);
/*      */   }
/*      */   
/*      */   protected final void checkIndex(int index, int fieldLength) {
/* 1376 */     ensureAccessible();
/* 1377 */     checkIndex0(index, fieldLength);
/*      */   }
/*      */   
/*      */   private static void checkRangeBounds(int index, int fieldLength, int capacity) {
/* 1381 */     if (MathUtil.isOutOfBounds(index, fieldLength, capacity)) {
/* 1382 */       throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", new Object[] {
/* 1383 */         Integer.valueOf(index), Integer.valueOf(fieldLength), Integer.valueOf(capacity) }));
/*      */     }
/*      */   }
/*      */   
/*      */   final void checkIndex0(int index, int fieldLength) {
/* 1388 */     if (checkBounds) {
/* 1389 */       checkRangeBounds(index, fieldLength, capacity());
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void checkSrcIndex(int index, int length, int srcIndex, int srcCapacity) {
/* 1394 */     checkIndex(index, length);
/* 1395 */     if (checkBounds) {
/* 1396 */       checkRangeBounds(srcIndex, length, srcCapacity);
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
/* 1401 */     checkIndex(index, length);
/* 1402 */     if (checkBounds) {
/* 1403 */       checkRangeBounds(dstIndex, length, dstCapacity);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void checkReadableBytes(int minimumReadableBytes)
/*      */   {
/* 1413 */     if (minimumReadableBytes < 0) {
/* 1414 */       throw new IllegalArgumentException("minimumReadableBytes: " + minimumReadableBytes + " (expected: >= 0)");
/*      */     }
/* 1416 */     checkReadableBytes0(minimumReadableBytes);
/*      */   }
/*      */   
/*      */   protected final void checkNewCapacity(int newCapacity) {
/* 1420 */     ensureAccessible();
/* 1421 */     if ((checkBounds) && (
/* 1422 */       (newCapacity < 0) || (newCapacity > maxCapacity())))
/*      */     {
/* 1424 */       throw new IllegalArgumentException("newCapacity: " + newCapacity + " (expected: 0-" + maxCapacity() + ')');
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkReadableBytes0(int minimumReadableBytes)
/*      */   {
/* 1430 */     ensureAccessible();
/* 1431 */     if ((checkBounds) && 
/* 1432 */       (this.readerIndex > this.writerIndex - minimumReadableBytes)) {
/* 1433 */       throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", new Object[] {
/*      */       
/* 1435 */         Integer.valueOf(this.readerIndex), Integer.valueOf(minimumReadableBytes), Integer.valueOf(this.writerIndex), this }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void ensureAccessible()
/*      */   {
/* 1445 */     if ((checkAccessible) && (internalRefCnt() == 0)) {
/* 1446 */       throw new IllegalReferenceCountException(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   int internalRefCnt()
/*      */   {
/* 1455 */     return refCnt();
/*      */   }
/*      */   
/*      */   final void setIndex0(int readerIndex, int writerIndex) {
/* 1459 */     this.readerIndex = readerIndex;
/* 1460 */     this.writerIndex = writerIndex;
/*      */   }
/*      */   
/*      */   final void discardMarks() {
/* 1464 */     this.markedReaderIndex = (this.markedWriterIndex = 0);
/*      */   }
/*      */   
/*      */   protected abstract byte _getByte(int paramInt);
/*      */   
/*      */   protected abstract short _getShort(int paramInt);
/*      */   
/*      */   protected abstract short _getShortLE(int paramInt);
/*      */   
/*      */   protected abstract int _getUnsignedMedium(int paramInt);
/*      */   
/*      */   protected abstract int _getUnsignedMediumLE(int paramInt);
/*      */   
/*      */   protected abstract int _getInt(int paramInt);
/*      */   
/*      */   protected abstract int _getIntLE(int paramInt);
/*      */   
/*      */   protected abstract long _getLong(int paramInt);
/*      */   
/*      */   protected abstract long _getLongLE(int paramInt);
/*      */   
/*      */   protected abstract void _setByte(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setShort(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setShortLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setMedium(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setMediumLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setInt(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setIntLE(int paramInt1, int paramInt2);
/*      */   
/*      */   protected abstract void _setLong(int paramInt, long paramLong);
/*      */   
/*      */   protected abstract void _setLongLE(int paramInt, long paramLong);
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\AbstractByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */