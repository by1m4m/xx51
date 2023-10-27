/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.ByteProcessor;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ class WrappedCompositeByteBuf
/*      */   extends CompositeByteBuf
/*      */ {
/*      */   private final CompositeByteBuf wrapped;
/*      */   
/*      */   WrappedCompositeByteBuf(CompositeByteBuf wrapped)
/*      */   {
/*   37 */     super(wrapped.alloc());
/*   38 */     this.wrapped = wrapped;
/*      */   }
/*      */   
/*      */   public boolean release()
/*      */   {
/*   43 */     return this.wrapped.release();
/*      */   }
/*      */   
/*      */   public boolean release(int decrement)
/*      */   {
/*   48 */     return this.wrapped.release(decrement);
/*      */   }
/*      */   
/*      */   public final int maxCapacity()
/*      */   {
/*   53 */     return this.wrapped.maxCapacity();
/*      */   }
/*      */   
/*      */   public final int readerIndex()
/*      */   {
/*   58 */     return this.wrapped.readerIndex();
/*      */   }
/*      */   
/*      */   public final int writerIndex()
/*      */   {
/*   63 */     return this.wrapped.writerIndex();
/*      */   }
/*      */   
/*      */   public final boolean isReadable()
/*      */   {
/*   68 */     return this.wrapped.isReadable();
/*      */   }
/*      */   
/*      */   public final boolean isReadable(int numBytes)
/*      */   {
/*   73 */     return this.wrapped.isReadable(numBytes);
/*      */   }
/*      */   
/*      */   public final boolean isWritable()
/*      */   {
/*   78 */     return this.wrapped.isWritable();
/*      */   }
/*      */   
/*      */   public final boolean isWritable(int numBytes)
/*      */   {
/*   83 */     return this.wrapped.isWritable(numBytes);
/*      */   }
/*      */   
/*      */   public final int readableBytes()
/*      */   {
/*   88 */     return this.wrapped.readableBytes();
/*      */   }
/*      */   
/*      */   public final int writableBytes()
/*      */   {
/*   93 */     return this.wrapped.writableBytes();
/*      */   }
/*      */   
/*      */   public final int maxWritableBytes()
/*      */   {
/*   98 */     return this.wrapped.maxWritableBytes();
/*      */   }
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force)
/*      */   {
/*  103 */     return this.wrapped.ensureWritable(minWritableBytes, force);
/*      */   }
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness)
/*      */   {
/*  108 */     return this.wrapped.order(endianness);
/*      */   }
/*      */   
/*      */   public boolean getBoolean(int index)
/*      */   {
/*  113 */     return this.wrapped.getBoolean(index);
/*      */   }
/*      */   
/*      */   public short getUnsignedByte(int index)
/*      */   {
/*  118 */     return this.wrapped.getUnsignedByte(index);
/*      */   }
/*      */   
/*      */   public short getShort(int index)
/*      */   {
/*  123 */     return this.wrapped.getShort(index);
/*      */   }
/*      */   
/*      */   public short getShortLE(int index)
/*      */   {
/*  128 */     return this.wrapped.getShortLE(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedShort(int index)
/*      */   {
/*  133 */     return this.wrapped.getUnsignedShort(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedShortLE(int index)
/*      */   {
/*  138 */     return this.wrapped.getUnsignedShortLE(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedMedium(int index)
/*      */   {
/*  143 */     return this.wrapped.getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */   public int getUnsignedMediumLE(int index)
/*      */   {
/*  148 */     return this.wrapped.getUnsignedMediumLE(index);
/*      */   }
/*      */   
/*      */   public int getMedium(int index)
/*      */   {
/*  153 */     return this.wrapped.getMedium(index);
/*      */   }
/*      */   
/*      */   public int getMediumLE(int index)
/*      */   {
/*  158 */     return this.wrapped.getMediumLE(index);
/*      */   }
/*      */   
/*      */   public int getInt(int index)
/*      */   {
/*  163 */     return this.wrapped.getInt(index);
/*      */   }
/*      */   
/*      */   public int getIntLE(int index)
/*      */   {
/*  168 */     return this.wrapped.getIntLE(index);
/*      */   }
/*      */   
/*      */   public long getUnsignedInt(int index)
/*      */   {
/*  173 */     return this.wrapped.getUnsignedInt(index);
/*      */   }
/*      */   
/*      */   public long getUnsignedIntLE(int index)
/*      */   {
/*  178 */     return this.wrapped.getUnsignedIntLE(index);
/*      */   }
/*      */   
/*      */   public long getLong(int index)
/*      */   {
/*  183 */     return this.wrapped.getLong(index);
/*      */   }
/*      */   
/*      */   public long getLongLE(int index)
/*      */   {
/*  188 */     return this.wrapped.getLongLE(index);
/*      */   }
/*      */   
/*      */   public char getChar(int index)
/*      */   {
/*  193 */     return this.wrapped.getChar(index);
/*      */   }
/*      */   
/*      */   public float getFloat(int index)
/*      */   {
/*  198 */     return this.wrapped.getFloat(index);
/*      */   }
/*      */   
/*      */   public double getDouble(int index)
/*      */   {
/*  203 */     return this.wrapped.getDouble(index);
/*      */   }
/*      */   
/*      */   public ByteBuf setShortLE(int index, int value)
/*      */   {
/*  208 */     return this.wrapped.setShortLE(index, value);
/*      */   }
/*      */   
/*      */   public ByteBuf setMediumLE(int index, int value)
/*      */   {
/*  213 */     return this.wrapped.setMediumLE(index, value);
/*      */   }
/*      */   
/*      */   public ByteBuf setIntLE(int index, int value)
/*      */   {
/*  218 */     return this.wrapped.setIntLE(index, value);
/*      */   }
/*      */   
/*      */   public ByteBuf setLongLE(int index, long value)
/*      */   {
/*  223 */     return this.wrapped.setLongLE(index, value);
/*      */   }
/*      */   
/*      */   public byte readByte()
/*      */   {
/*  228 */     return this.wrapped.readByte();
/*      */   }
/*      */   
/*      */   public boolean readBoolean()
/*      */   {
/*  233 */     return this.wrapped.readBoolean();
/*      */   }
/*      */   
/*      */   public short readUnsignedByte()
/*      */   {
/*  238 */     return this.wrapped.readUnsignedByte();
/*      */   }
/*      */   
/*      */   public short readShort()
/*      */   {
/*  243 */     return this.wrapped.readShort();
/*      */   }
/*      */   
/*      */   public short readShortLE()
/*      */   {
/*  248 */     return this.wrapped.readShortLE();
/*      */   }
/*      */   
/*      */   public int readUnsignedShort()
/*      */   {
/*  253 */     return this.wrapped.readUnsignedShort();
/*      */   }
/*      */   
/*      */   public int readUnsignedShortLE()
/*      */   {
/*  258 */     return this.wrapped.readUnsignedShortLE();
/*      */   }
/*      */   
/*      */   public int readMedium()
/*      */   {
/*  263 */     return this.wrapped.readMedium();
/*      */   }
/*      */   
/*      */   public int readMediumLE()
/*      */   {
/*  268 */     return this.wrapped.readMediumLE();
/*      */   }
/*      */   
/*      */   public int readUnsignedMedium()
/*      */   {
/*  273 */     return this.wrapped.readUnsignedMedium();
/*      */   }
/*      */   
/*      */   public int readUnsignedMediumLE()
/*      */   {
/*  278 */     return this.wrapped.readUnsignedMediumLE();
/*      */   }
/*      */   
/*      */   public int readInt()
/*      */   {
/*  283 */     return this.wrapped.readInt();
/*      */   }
/*      */   
/*      */   public int readIntLE()
/*      */   {
/*  288 */     return this.wrapped.readIntLE();
/*      */   }
/*      */   
/*      */   public long readUnsignedInt()
/*      */   {
/*  293 */     return this.wrapped.readUnsignedInt();
/*      */   }
/*      */   
/*      */   public long readUnsignedIntLE()
/*      */   {
/*  298 */     return this.wrapped.readUnsignedIntLE();
/*      */   }
/*      */   
/*      */   public long readLong()
/*      */   {
/*  303 */     return this.wrapped.readLong();
/*      */   }
/*      */   
/*      */   public long readLongLE()
/*      */   {
/*  308 */     return this.wrapped.readLongLE();
/*      */   }
/*      */   
/*      */   public char readChar()
/*      */   {
/*  313 */     return this.wrapped.readChar();
/*      */   }
/*      */   
/*      */   public float readFloat()
/*      */   {
/*  318 */     return this.wrapped.readFloat();
/*      */   }
/*      */   
/*      */   public double readDouble()
/*      */   {
/*  323 */     return this.wrapped.readDouble();
/*      */   }
/*      */   
/*      */   public ByteBuf readBytes(int length)
/*      */   {
/*  328 */     return this.wrapped.readBytes(length);
/*      */   }
/*      */   
/*      */   public ByteBuf slice()
/*      */   {
/*  333 */     return this.wrapped.slice();
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice()
/*      */   {
/*  338 */     return this.wrapped.retainedSlice();
/*      */   }
/*      */   
/*      */   public ByteBuf slice(int index, int length)
/*      */   {
/*  343 */     return this.wrapped.slice(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length)
/*      */   {
/*  348 */     return this.wrapped.retainedSlice(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer()
/*      */   {
/*  353 */     return this.wrapped.nioBuffer();
/*      */   }
/*      */   
/*      */   public String toString(Charset charset)
/*      */   {
/*  358 */     return this.wrapped.toString(charset);
/*      */   }
/*      */   
/*      */   public String toString(int index, int length, Charset charset)
/*      */   {
/*  363 */     return this.wrapped.toString(index, length, charset);
/*      */   }
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value)
/*      */   {
/*  368 */     return this.wrapped.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(byte value)
/*      */   {
/*  373 */     return this.wrapped.bytesBefore(value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int length, byte value)
/*      */   {
/*  378 */     return this.wrapped.bytesBefore(length, value);
/*      */   }
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value)
/*      */   {
/*  383 */     return this.wrapped.bytesBefore(index, length, value);
/*      */   }
/*      */   
/*      */   public int forEachByte(ByteProcessor processor)
/*      */   {
/*  388 */     return this.wrapped.forEachByte(processor);
/*      */   }
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor)
/*      */   {
/*  393 */     return this.wrapped.forEachByte(index, length, processor);
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor)
/*      */   {
/*  398 */     return this.wrapped.forEachByteDesc(processor);
/*      */   }
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor)
/*      */   {
/*  403 */     return this.wrapped.forEachByteDesc(index, length, processor);
/*      */   }
/*      */   
/*      */   public final int hashCode()
/*      */   {
/*  408 */     return this.wrapped.hashCode();
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o)
/*      */   {
/*  413 */     return this.wrapped.equals(o);
/*      */   }
/*      */   
/*      */   public final int compareTo(ByteBuf that)
/*      */   {
/*  418 */     return this.wrapped.compareTo(that);
/*      */   }
/*      */   
/*      */   public final int refCnt()
/*      */   {
/*  423 */     return this.wrapped.refCnt();
/*      */   }
/*      */   
/*      */   int internalRefCnt()
/*      */   {
/*  428 */     return this.wrapped.internalRefCnt();
/*      */   }
/*      */   
/*      */   public ByteBuf duplicate()
/*      */   {
/*  433 */     return this.wrapped.duplicate();
/*      */   }
/*      */   
/*      */   public ByteBuf retainedDuplicate()
/*      */   {
/*  438 */     return this.wrapped.retainedDuplicate();
/*      */   }
/*      */   
/*      */   public ByteBuf readSlice(int length)
/*      */   {
/*  443 */     return this.wrapped.readSlice(length);
/*      */   }
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length)
/*      */   {
/*  448 */     return this.wrapped.readRetainedSlice(length);
/*      */   }
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException
/*      */   {
/*  453 */     return this.wrapped.readBytes(out, length);
/*      */   }
/*      */   
/*      */   public ByteBuf writeShortLE(int value)
/*      */   {
/*  458 */     return this.wrapped.writeShortLE(value);
/*      */   }
/*      */   
/*      */   public ByteBuf writeMediumLE(int value)
/*      */   {
/*  463 */     return this.wrapped.writeMediumLE(value);
/*      */   }
/*      */   
/*      */   public ByteBuf writeIntLE(int value)
/*      */   {
/*  468 */     return this.wrapped.writeIntLE(value);
/*      */   }
/*      */   
/*      */   public ByteBuf writeLongLE(long value)
/*      */   {
/*  473 */     return this.wrapped.writeLongLE(value);
/*      */   }
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException
/*      */   {
/*  478 */     return this.wrapped.writeBytes(in, length);
/*      */   }
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException
/*      */   {
/*  483 */     return this.wrapped.writeBytes(in, length);
/*      */   }
/*      */   
/*      */   public ByteBuf copy()
/*      */   {
/*  488 */     return this.wrapped.copy();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponent(ByteBuf buffer)
/*      */   {
/*  493 */     this.wrapped.addComponent(buffer);
/*  494 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(ByteBuf... buffers)
/*      */   {
/*  499 */     this.wrapped.addComponents(buffers);
/*  500 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers)
/*      */   {
/*  505 */     this.wrapped.addComponents(buffers);
/*  506 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer)
/*      */   {
/*  511 */     this.wrapped.addComponent(cIndex, buffer);
/*  512 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers)
/*      */   {
/*  517 */     this.wrapped.addComponents(cIndex, buffers);
/*  518 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers)
/*      */   {
/*  523 */     this.wrapped.addComponents(cIndex, buffers);
/*  524 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer)
/*      */   {
/*  529 */     this.wrapped.addComponent(increaseWriterIndex, buffer);
/*  530 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, ByteBuf... buffers)
/*      */   {
/*  535 */     this.wrapped.addComponents(increaseWriterIndex, buffers);
/*  536 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, Iterable<ByteBuf> buffers)
/*      */   {
/*  541 */     this.wrapped.addComponents(increaseWriterIndex, buffers);
/*  542 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, int cIndex, ByteBuf buffer)
/*      */   {
/*  547 */     this.wrapped.addComponent(increaseWriterIndex, cIndex, buffer);
/*  548 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf removeComponent(int cIndex)
/*      */   {
/*  553 */     this.wrapped.removeComponent(cIndex);
/*  554 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf removeComponents(int cIndex, int numComponents)
/*      */   {
/*  559 */     this.wrapped.removeComponents(cIndex, numComponents);
/*  560 */     return this;
/*      */   }
/*      */   
/*      */   public Iterator<ByteBuf> iterator()
/*      */   {
/*  565 */     return this.wrapped.iterator();
/*      */   }
/*      */   
/*      */   public List<ByteBuf> decompose(int offset, int length)
/*      */   {
/*  570 */     return this.wrapped.decompose(offset, length);
/*      */   }
/*      */   
/*      */   public final boolean isDirect()
/*      */   {
/*  575 */     return this.wrapped.isDirect();
/*      */   }
/*      */   
/*      */   public final boolean hasArray()
/*      */   {
/*  580 */     return this.wrapped.hasArray();
/*      */   }
/*      */   
/*      */   public final byte[] array()
/*      */   {
/*  585 */     return this.wrapped.array();
/*      */   }
/*      */   
/*      */   public final int arrayOffset()
/*      */   {
/*  590 */     return this.wrapped.arrayOffset();
/*      */   }
/*      */   
/*      */   public final boolean hasMemoryAddress()
/*      */   {
/*  595 */     return this.wrapped.hasMemoryAddress();
/*      */   }
/*      */   
/*      */   public final long memoryAddress()
/*      */   {
/*  600 */     return this.wrapped.memoryAddress();
/*      */   }
/*      */   
/*      */   public final int capacity()
/*      */   {
/*  605 */     return this.wrapped.capacity();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf capacity(int newCapacity)
/*      */   {
/*  610 */     this.wrapped.capacity(newCapacity);
/*  611 */     return this;
/*      */   }
/*      */   
/*      */   public final ByteBufAllocator alloc()
/*      */   {
/*  616 */     return this.wrapped.alloc();
/*      */   }
/*      */   
/*      */   public final ByteOrder order()
/*      */   {
/*  621 */     return this.wrapped.order();
/*      */   }
/*      */   
/*      */   public final int numComponents()
/*      */   {
/*  626 */     return this.wrapped.numComponents();
/*      */   }
/*      */   
/*      */   public final int maxNumComponents()
/*      */   {
/*  631 */     return this.wrapped.maxNumComponents();
/*      */   }
/*      */   
/*      */   public final int toComponentIndex(int offset)
/*      */   {
/*  636 */     return this.wrapped.toComponentIndex(offset);
/*      */   }
/*      */   
/*      */   public final int toByteIndex(int cIndex)
/*      */   {
/*  641 */     return this.wrapped.toByteIndex(cIndex);
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  646 */     return this.wrapped.getByte(index);
/*      */   }
/*      */   
/*      */   protected final byte _getByte(int index)
/*      */   {
/*  651 */     return this.wrapped._getByte(index);
/*      */   }
/*      */   
/*      */   protected final short _getShort(int index)
/*      */   {
/*  656 */     return this.wrapped._getShort(index);
/*      */   }
/*      */   
/*      */   protected final short _getShortLE(int index)
/*      */   {
/*  661 */     return this.wrapped._getShortLE(index);
/*      */   }
/*      */   
/*      */   protected final int _getUnsignedMedium(int index)
/*      */   {
/*  666 */     return this.wrapped._getUnsignedMedium(index);
/*      */   }
/*      */   
/*      */   protected final int _getUnsignedMediumLE(int index)
/*      */   {
/*  671 */     return this.wrapped._getUnsignedMediumLE(index);
/*      */   }
/*      */   
/*      */   protected final int _getInt(int index)
/*      */   {
/*  676 */     return this.wrapped._getInt(index);
/*      */   }
/*      */   
/*      */   protected final int _getIntLE(int index)
/*      */   {
/*  681 */     return this.wrapped._getIntLE(index);
/*      */   }
/*      */   
/*      */   protected final long _getLong(int index)
/*      */   {
/*  686 */     return this.wrapped._getLong(index);
/*      */   }
/*      */   
/*      */   protected final long _getLongLE(int index)
/*      */   {
/*  691 */     return this.wrapped._getLongLE(index);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*      */   {
/*  696 */     this.wrapped.getBytes(index, dst, dstIndex, length);
/*  697 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuffer dst)
/*      */   {
/*  702 */     this.wrapped.getBytes(index, dst);
/*  703 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  708 */     this.wrapped.getBytes(index, dst, dstIndex, length);
/*  709 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException
/*      */   {
/*  714 */     return this.wrapped.getBytes(index, out, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, OutputStream out, int length) throws IOException
/*      */   {
/*  719 */     this.wrapped.getBytes(index, out, length);
/*  720 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setByte(int index, int value)
/*      */   {
/*  725 */     this.wrapped.setByte(index, value);
/*  726 */     return this;
/*      */   }
/*      */   
/*      */   protected final void _setByte(int index, int value)
/*      */   {
/*  731 */     this.wrapped._setByte(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setShort(int index, int value)
/*      */   {
/*  736 */     this.wrapped.setShort(index, value);
/*  737 */     return this;
/*      */   }
/*      */   
/*      */   protected final void _setShort(int index, int value)
/*      */   {
/*  742 */     this.wrapped._setShort(index, value);
/*      */   }
/*      */   
/*      */   protected final void _setShortLE(int index, int value)
/*      */   {
/*  747 */     this.wrapped._setShortLE(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setMedium(int index, int value)
/*      */   {
/*  752 */     this.wrapped.setMedium(index, value);
/*  753 */     return this;
/*      */   }
/*      */   
/*      */   protected final void _setMedium(int index, int value)
/*      */   {
/*  758 */     this.wrapped._setMedium(index, value);
/*      */   }
/*      */   
/*      */   protected final void _setMediumLE(int index, int value)
/*      */   {
/*  763 */     this.wrapped._setMediumLE(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setInt(int index, int value)
/*      */   {
/*  768 */     this.wrapped.setInt(index, value);
/*  769 */     return this;
/*      */   }
/*      */   
/*      */   protected final void _setInt(int index, int value)
/*      */   {
/*  774 */     this.wrapped._setInt(index, value);
/*      */   }
/*      */   
/*      */   protected final void _setIntLE(int index, int value)
/*      */   {
/*  779 */     this.wrapped._setIntLE(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setLong(int index, long value)
/*      */   {
/*  784 */     this.wrapped.setLong(index, value);
/*  785 */     return this;
/*      */   }
/*      */   
/*      */   protected final void _setLong(int index, long value)
/*      */   {
/*  790 */     this.wrapped._setLong(index, value);
/*      */   }
/*      */   
/*      */   protected final void _setLongLE(int index, long value)
/*      */   {
/*  795 */     this.wrapped._setLongLE(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*      */   {
/*  800 */     this.wrapped.setBytes(index, src, srcIndex, length);
/*  801 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuffer src)
/*      */   {
/*  806 */     this.wrapped.setBytes(index, src);
/*  807 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*      */   {
/*  812 */     this.wrapped.setBytes(index, src, srcIndex, length);
/*  813 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException
/*      */   {
/*  818 */     return this.wrapped.setBytes(index, in, length);
/*      */   }
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*      */   {
/*  823 */     return this.wrapped.setBytes(index, in, length);
/*      */   }
/*      */   
/*      */   public ByteBuf copy(int index, int length)
/*      */   {
/*  828 */     return this.wrapped.copy(index, length);
/*      */   }
/*      */   
/*      */   public final ByteBuf component(int cIndex)
/*      */   {
/*  833 */     return this.wrapped.component(cIndex);
/*      */   }
/*      */   
/*      */   public final ByteBuf componentAtOffset(int offset)
/*      */   {
/*  838 */     return this.wrapped.componentAtOffset(offset);
/*      */   }
/*      */   
/*      */   public final ByteBuf internalComponent(int cIndex)
/*      */   {
/*  843 */     return this.wrapped.internalComponent(cIndex);
/*      */   }
/*      */   
/*      */   public final ByteBuf internalComponentAtOffset(int offset)
/*      */   {
/*  848 */     return this.wrapped.internalComponentAtOffset(offset);
/*      */   }
/*      */   
/*      */   public int nioBufferCount()
/*      */   {
/*  853 */     return this.wrapped.nioBufferCount();
/*      */   }
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length)
/*      */   {
/*  858 */     return this.wrapped.internalNioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length)
/*      */   {
/*  863 */     return this.wrapped.nioBuffer(index, length);
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length)
/*      */   {
/*  868 */     return this.wrapped.nioBuffers(index, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf consolidate()
/*      */   {
/*  873 */     this.wrapped.consolidate();
/*  874 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf consolidate(int cIndex, int numComponents)
/*      */   {
/*  879 */     this.wrapped.consolidate(cIndex, numComponents);
/*  880 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardReadComponents()
/*      */   {
/*  885 */     this.wrapped.discardReadComponents();
/*  886 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardReadBytes()
/*      */   {
/*  891 */     this.wrapped.discardReadBytes();
/*  892 */     return this;
/*      */   }
/*      */   
/*      */   public final String toString()
/*      */   {
/*  897 */     return this.wrapped.toString();
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf readerIndex(int readerIndex)
/*      */   {
/*  902 */     this.wrapped.readerIndex(readerIndex);
/*  903 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf writerIndex(int writerIndex)
/*      */   {
/*  908 */     this.wrapped.writerIndex(writerIndex);
/*  909 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/*  914 */     this.wrapped.setIndex(readerIndex, writerIndex);
/*  915 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf clear()
/*      */   {
/*  920 */     this.wrapped.clear();
/*  921 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf markReaderIndex()
/*      */   {
/*  926 */     this.wrapped.markReaderIndex();
/*  927 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf resetReaderIndex()
/*      */   {
/*  932 */     this.wrapped.resetReaderIndex();
/*  933 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf markWriterIndex()
/*      */   {
/*  938 */     this.wrapped.markWriterIndex();
/*  939 */     return this;
/*      */   }
/*      */   
/*      */   public final CompositeByteBuf resetWriterIndex()
/*      */   {
/*  944 */     this.wrapped.resetWriterIndex();
/*  945 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf ensureWritable(int minWritableBytes)
/*      */   {
/*  950 */     this.wrapped.ensureWritable(minWritableBytes);
/*  951 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/*  956 */     this.wrapped.getBytes(index, dst);
/*  957 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/*  962 */     this.wrapped.getBytes(index, dst, length);
/*  963 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst)
/*      */   {
/*  968 */     this.wrapped.getBytes(index, dst);
/*  969 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBoolean(int index, boolean value)
/*      */   {
/*  974 */     this.wrapped.setBoolean(index, value);
/*  975 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setChar(int index, int value)
/*      */   {
/*  980 */     this.wrapped.setChar(index, value);
/*  981 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setFloat(int index, float value)
/*      */   {
/*  986 */     this.wrapped.setFloat(index, value);
/*  987 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setDouble(int index, double value)
/*      */   {
/*  992 */     this.wrapped.setDouble(index, value);
/*  993 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/*  998 */     this.wrapped.setBytes(index, src);
/*  999 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/* 1004 */     this.wrapped.setBytes(index, src, length);
/* 1005 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src)
/*      */   {
/* 1010 */     this.wrapped.setBytes(index, src);
/* 1011 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setZero(int index, int length)
/*      */   {
/* 1016 */     this.wrapped.setZero(index, length);
/* 1017 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst)
/*      */   {
/* 1022 */     this.wrapped.readBytes(dst);
/* 1023 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/* 1028 */     this.wrapped.readBytes(dst, length);
/* 1029 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/* 1034 */     this.wrapped.readBytes(dst, dstIndex, length);
/* 1035 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst)
/*      */   {
/* 1040 */     this.wrapped.readBytes(dst);
/* 1041 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/* 1046 */     this.wrapped.readBytes(dst, dstIndex, length);
/* 1047 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuffer dst)
/*      */   {
/* 1052 */     this.wrapped.readBytes(dst);
/* 1053 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException
/*      */   {
/* 1058 */     this.wrapped.readBytes(out, length);
/* 1059 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException
/*      */   {
/* 1064 */     return this.wrapped.getBytes(index, out, position, length);
/*      */   }
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException
/*      */   {
/* 1069 */     return this.wrapped.setBytes(index, in, position, length);
/*      */   }
/*      */   
/*      */   public boolean isReadOnly()
/*      */   {
/* 1074 */     return this.wrapped.isReadOnly();
/*      */   }
/*      */   
/*      */   public ByteBuf asReadOnly()
/*      */   {
/* 1079 */     return this.wrapped.asReadOnly();
/*      */   }
/*      */   
/*      */   protected SwappedByteBuf newSwappedByteBuf()
/*      */   {
/* 1084 */     return this.wrapped.newSwappedByteBuf();
/*      */   }
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset)
/*      */   {
/* 1089 */     return this.wrapped.getCharSequence(index, length, charset);
/*      */   }
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset)
/*      */   {
/* 1094 */     return this.wrapped.readCharSequence(length, charset);
/*      */   }
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset)
/*      */   {
/* 1099 */     return this.wrapped.setCharSequence(index, sequence, charset);
/*      */   }
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException
/*      */   {
/* 1104 */     return this.wrapped.readBytes(out, position, length);
/*      */   }
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException
/*      */   {
/* 1109 */     return this.wrapped.writeBytes(in, position, length);
/*      */   }
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset)
/*      */   {
/* 1114 */     return this.wrapped.writeCharSequence(sequence, charset);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf skipBytes(int length)
/*      */   {
/* 1119 */     this.wrapped.skipBytes(length);
/* 1120 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBoolean(boolean value)
/*      */   {
/* 1125 */     this.wrapped.writeBoolean(value);
/* 1126 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeByte(int value)
/*      */   {
/* 1131 */     this.wrapped.writeByte(value);
/* 1132 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeShort(int value)
/*      */   {
/* 1137 */     this.wrapped.writeShort(value);
/* 1138 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeMedium(int value)
/*      */   {
/* 1143 */     this.wrapped.writeMedium(value);
/* 1144 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeInt(int value)
/*      */   {
/* 1149 */     this.wrapped.writeInt(value);
/* 1150 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeLong(long value)
/*      */   {
/* 1155 */     this.wrapped.writeLong(value);
/* 1156 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeChar(int value)
/*      */   {
/* 1161 */     this.wrapped.writeChar(value);
/* 1162 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeFloat(float value)
/*      */   {
/* 1167 */     this.wrapped.writeFloat(value);
/* 1168 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeDouble(double value)
/*      */   {
/* 1173 */     this.wrapped.writeDouble(value);
/* 1174 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src)
/*      */   {
/* 1179 */     this.wrapped.writeBytes(src);
/* 1180 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/* 1185 */     this.wrapped.writeBytes(src, length);
/* 1186 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/* 1191 */     this.wrapped.writeBytes(src, srcIndex, length);
/* 1192 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src)
/*      */   {
/* 1197 */     this.wrapped.writeBytes(src);
/* 1198 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/* 1203 */     this.wrapped.writeBytes(src, srcIndex, length);
/* 1204 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuffer src)
/*      */   {
/* 1209 */     this.wrapped.writeBytes(src);
/* 1210 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeZero(int length)
/*      */   {
/* 1215 */     this.wrapped.writeZero(length);
/* 1216 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain(int increment)
/*      */   {
/* 1221 */     this.wrapped.retain(increment);
/* 1222 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain()
/*      */   {
/* 1227 */     this.wrapped.retain();
/* 1228 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch()
/*      */   {
/* 1233 */     this.wrapped.touch();
/* 1234 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch(Object hint)
/*      */   {
/* 1239 */     this.wrapped.touch(hint);
/* 1240 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/* 1245 */     return this.wrapped.nioBuffers();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardSomeReadBytes()
/*      */   {
/* 1250 */     this.wrapped.discardSomeReadBytes();
/* 1251 */     return this;
/*      */   }
/*      */   
/*      */   public final void deallocate()
/*      */   {
/* 1256 */     this.wrapped.deallocate();
/*      */   }
/*      */   
/*      */   public final ByteBuf unwrap()
/*      */   {
/* 1261 */     return this.wrapped;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\WrappedCompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */