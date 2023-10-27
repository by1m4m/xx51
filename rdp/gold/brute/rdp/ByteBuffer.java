/*      */ package rdp.gold.brute.rdp;
/*      */ 
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class ByteBuffer
/*      */ {
/*      */   public static final String SEQUENCE_NUMBER = "seq";
/*      */   public byte[] data;
/*   14 */   public int offset = 0;
/*   15 */   public int length = 0;
/*   16 */   public int cursor = 0;
/*      */   
/*   18 */   private int refCount = 1;
/*      */   
/*      */   private Order order;
/*   21 */   private Map<String, Object> metadata = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuffer(int minLength)
/*      */   {
/*   30 */     this.data = BufferPool.allocateNewBuffer(minLength);
/*   31 */     this.offset = 0;
/*   32 */     this.length = minLength;
/*      */   }
/*      */   
/*      */   public ByteBuffer(byte[] data) {
/*   36 */     if (data == null) {
/*   37 */       throw new NullPointerException("Data must be non-null.");
/*      */     }
/*   39 */     this.data = data;
/*   40 */     this.offset = 0;
/*   41 */     this.length = data.length;
/*      */   }
/*      */   
/*      */   public ByteBuffer(byte[] data, int offset, int length) {
/*   45 */     if (data == null) {
/*   46 */       throw new NullPointerException("Data must be non-null.");
/*      */     }
/*   48 */     this.data = data;
/*   49 */     this.offset = offset;
/*   50 */     this.length = length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuffer(int minLength, boolean reserveSpaceForHeader)
/*      */   {
/*   59 */     this.data = BufferPool.allocateNewBuffer(128 + minLength);
/*   60 */     this.offset = 128;
/*   61 */     this.length = minLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuffer(Order order)
/*      */   {
/*   68 */     this.order = order;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer[] convertByteArraysToByteBuffers(byte[]... bas)
/*      */   {
/*   76 */     ByteBuffer[] bufs = new ByteBuffer[bas.length];
/*      */     
/*   78 */     int i = 0;
/*   79 */     for (byte[] ba : bas) {
/*   80 */       bufs[(i++)] = new ByteBuffer(ba);
/*      */     }
/*   82 */     return bufs;
/*      */   }
/*      */   
/*      */   protected static long calculateUnsignedInt(byte value1, byte value2, byte value3, byte value4) {
/*   86 */     return (calculateUnsignedByte(value1) << 24) + (calculateUnsignedByte(value2) << 16) + (calculateUnsignedByte(value3) << 8) + calculateUnsignedByte(value4);
/*      */   }
/*      */   
/*      */   protected static int calculateUnsignedByte(byte value) {
/*   90 */     return value & 0xFF;
/*      */   }
/*      */   
/*      */   protected static int calculateUnsignedShort(byte value1, byte value2) {
/*   94 */     return calculateUnsignedByte(value1) << 8 | calculateUnsignedByte(value2);
/*      */   }
/*      */   
/*      */   protected static short calculateSignedShort(byte value1, byte value2) {
/*   98 */     return (short)calculateUnsignedShort(value1, value2);
/*      */   }
/*      */   
/*      */   public Order getOrder() {
/*  102 */     return this.order;
/*      */   }
/*      */   
/*      */   public void setOrder(Order order) {
/*  106 */     this.order = order;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/*  111 */     return toString(this.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString(int maxLength)
/*      */   {
/*  120 */     return 
/*  121 */       "ByteRange(){offset=" + this.offset + ", length=" + this.length + ", cursor=" + this.cursor + ", data=" + (this.data == null ? "null" : toHexString(maxLength)) + ((this.metadata == null) || (this.metadata.size() == 0) ? "" : new StringBuilder().append(", metadata=").append(this.metadata).toString()) + "}";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toHexString(int maxLength)
/*      */   {
/*  131 */     StringBuilder builder = new StringBuilder(maxLength * 6);
/*  132 */     builder.append('[');
/*  133 */     for (int i = 0; (i < maxLength) && (i < this.length); i++) {
/*  134 */       if (i > 0)
/*  135 */         builder.append(", ");
/*  136 */       int b = this.data[(this.offset + i)] & 0xFF;
/*  137 */       builder.append("0x" + (b < 16 ? "0" : "") + Integer.toString(b, 16));
/*      */     }
/*  139 */     builder.append(']');
/*  140 */     return builder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toPlainHexString(int maxLength)
/*      */   {
/*  150 */     StringBuilder builder = new StringBuilder(maxLength * 3);
/*  151 */     for (int i = 0; (i < maxLength) && (i < this.length); i++) {
/*  152 */       if (i > 0)
/*  153 */         builder.append(" ");
/*  154 */       int b = this.data[(this.offset + i)] & 0xFF;
/*  155 */       builder.append(String.format("%02x", new Object[] { Integer.valueOf(b) }));
/*      */     }
/*  157 */     return builder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String dump()
/*      */   {
/*  167 */     StringBuilder builder = new StringBuilder(this.length * 4);
/*  168 */     int i = addBytesToBuilder(builder);
/*  169 */     int end = i - 1;
/*  170 */     if (end % 16 != 15) {
/*  171 */       int begin = end & 0xFFFFFFF0;
/*  172 */       for (int j = 0; j < 15 - end % 16; j++) {
/*  173 */         builder.append("   ");
/*      */       }
/*  175 */       builder.append(' ');
/*  176 */       builder.append(toASCIIString(begin, end));
/*  177 */       builder.append('\n');
/*      */     }
/*  179 */     return builder.toString();
/*      */   }
/*      */   
/*      */   protected int addBytesToBuilder(StringBuilder builder) {
/*  183 */     for (int i = 0; 
/*  184 */         i < this.length; i++) {
/*  185 */       if (i % 16 == 0) {
/*  186 */         builder.append(String.format("%04x", new Object[] { Integer.valueOf(i) }));
/*      */       }
/*      */       
/*  189 */       builder.append(' ');
/*  190 */       int b = this.data[(this.offset + i)] & 0xFF;
/*  191 */       builder.append(String.format("%02x", new Object[] { Integer.valueOf(b) }));
/*      */       
/*  193 */       if (i % 16 == 15) {
/*  194 */         builder.append(' ');
/*  195 */         builder.append(toASCIIString(i - 15, i));
/*  196 */         builder.append('\n');
/*      */       }
/*      */     }
/*  199 */     return i;
/*      */   }
/*      */   
/*      */   private String toASCIIString(int start, int finish) {
/*  203 */     StringBuffer sb = new StringBuffer(16);
/*  204 */     for (int i = start; i <= finish; i++) {
/*  205 */       char ch = (char)this.data[(this.offset + i)];
/*  206 */       if ((ch < ' ') || (ch >= '')) {
/*  207 */         sb.append('.');
/*      */       } else {
/*  209 */         sb.append(ch);
/*      */       }
/*      */     }
/*  212 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toPlainHexString()
/*      */   {
/*  220 */     return toPlainHexString(this.length);
/*      */   }
/*      */   
/*      */   public void extend(int newLength) {
/*  224 */     if (this.data.length < newLength)
/*  225 */       Arrays.copyOf(this.data, newLength);
/*      */   }
/*      */   
/*      */   public void ref() {
/*  229 */     this.refCount += 1;
/*      */   }
/*      */   
/*      */   public void unref() {
/*  233 */     this.refCount -= 1;
/*      */     
/*  235 */     if (this.refCount == 0)
/*      */     {
/*  237 */       BufferPool.recycleBuffer(this.data);
/*      */       
/*  239 */       this.data = null;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isSoleOwner()
/*      */   {
/*  245 */     return this.refCount == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuffer slice(int offset, int length, boolean copyMetadata)
/*      */   {
/*  252 */     ref();
/*      */     
/*  254 */     if (this.length < offset + length) {
/*  255 */       throw new RuntimeException("Length of region is larger that length of this buffer. Buffer length: " + this.length + ", offset: " + offset + ", new region length: " + length + ".");
/*      */     }
/*      */     
/*  258 */     ByteBuffer slice = new ByteBuffer(this.data, this.offset + offset, length);
/*      */     
/*  260 */     if ((copyMetadata) && (this.metadata != null)) {
/*  261 */       slice.metadata = new HashMap(this.metadata);
/*      */     }
/*  263 */     return slice;
/*      */   }
/*      */   
/*      */   public Object putMetadata(String key, Object value) {
/*  267 */     if (this.metadata == null)
/*  268 */       this.metadata = new HashMap();
/*  269 */     return this.metadata.put(key, value);
/*      */   }
/*      */   
/*      */   public Object getMetadata(String key) {
/*  273 */     return this.metadata != null ? this.metadata.get(key) : null;
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
/*      */   public ByteBuffer join(ByteBuffer buf)
/*      */   {
/*  286 */     int newLength = this.length + buf.length;
/*  287 */     byte[] newData = new byte[newLength];
/*      */     
/*      */ 
/*  290 */     System.arraycopy(this.data, this.offset, newData, 0, this.length);
/*      */     
/*      */ 
/*  293 */     System.arraycopy(buf.data, buf.offset, newData, this.length, buf.length);
/*      */     
/*  295 */     ByteBuffer newBuf = new ByteBuffer(newData);
/*      */     
/*      */ 
/*      */ 
/*  299 */     if (this.metadata != null) {
/*  300 */       newBuf.metadata = new HashMap(this.metadata);
/*      */     }
/*  302 */     return newBuf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte[] toByteArray()
/*      */   {
/*  309 */     return Arrays.copyOfRange(this.data, this.offset, this.offset + this.length);
/*      */   }
/*      */   
/*      */   public short[] toShortArray() {
/*  313 */     if (this.length % 2 != 0) {
/*  314 */       throw new ArrayIndexOutOfBoundsException("Length of byte array must be dividable by 2 without remainder. Array length: " + this.length + ", remainder: " + this.length % 2 + ".");
/*      */     }
/*      */     
/*  317 */     short[] buf = new short[this.length / 2];
/*      */     
/*  319 */     int i = 0; for (int j = this.offset; i < buf.length; j += 2) {
/*  320 */       buf[i] = ((short)(this.data[(j + 0)] & 0xFF | (this.data[(j + 1)] & 0xFF) << 8));i++;
/*      */     }
/*  322 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int[] toIntLEArray()
/*      */   {
/*  329 */     if (this.length % 4 != 0) {
/*  330 */       throw new ArrayIndexOutOfBoundsException("Length of byte array must be dividable by 4 without remainder. Array length: " + this.length + ", remainder: " + this.length % 4 + ".");
/*      */     }
/*      */     
/*  333 */     int[] buf = new int[this.length / 4];
/*      */     
/*  335 */     int i = 0; for (int j = this.offset; i < buf.length; j += 4) {
/*  336 */       buf[i] = (this.data[(j + 0)] & 0xFF | (this.data[(j + 1)] & 0xFF) << 8 | (this.data[(j + 2)] & 0xFF) << 16 | (this.data[(j + 3)] & 0xFF) << 24);i++;
/*      */     }
/*  338 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] toInt3LEArray()
/*      */   {
/*  346 */     if (this.length % 3 != 0) {
/*  347 */       throw new ArrayIndexOutOfBoundsException("Length of byte array must be dividable by 3 without remainder. Array length: " + this.length + ", remainder: " + this.length % 3 + ".");
/*      */     }
/*      */     
/*  350 */     int[] buf = new int[this.length / 3];
/*      */     
/*  352 */     int i = 0; for (int j = this.offset; i < buf.length; j += 3) {
/*  353 */       buf[i] = (this.data[(j + 0)] & 0xFF | (this.data[(j + 1)] & 0xFF) << 8 | (this.data[(j + 2)] & 0xFF) << 16);i++;
/*      */     }
/*  355 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readSignedInt()
/*      */   {
/*  362 */     if (this.cursor + 4 > this.length) {
/*  363 */       throw new ArrayIndexOutOfBoundsException("Cannot read 4 bytes from this buffer: " + this + ".");
/*      */     }
/*  365 */     int result = ((this.data[(this.offset + this.cursor)] & 0xFF) << 24) + ((this.data[(this.offset + this.cursor + 1)] & 0xFF) << 16) + ((this.data[(this.offset + this.cursor + 2)] & 0xFF) << 8) + (this.data[(this.offset + this.cursor + 3)] & 0xFF);
/*  366 */     this.cursor += 4;
/*  367 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readSignedIntLE()
/*      */   {
/*  374 */     if (this.cursor + 4 > this.length) {
/*  375 */       throw new ArrayIndexOutOfBoundsException("Cannot read 4 bytes from this buffer: " + this + ".");
/*      */     }
/*  377 */     int result = ((this.data[(this.offset + this.cursor + 3)] & 0xFF) << 24) + ((this.data[(this.offset + this.cursor + 2)] & 0xFF) << 16) + ((this.data[(this.offset + this.cursor + 1)] & 0xFF) << 8) + (this.data[(this.offset + this.cursor)] & 0xFF);
/*  378 */     this.cursor += 4;
/*  379 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long readUnsignedIntLE()
/*      */   {
/*  386 */     if (this.cursor + 4 > this.length) {
/*  387 */       throw new ArrayIndexOutOfBoundsException("Cannot read 4 bytes from this buffer: " + this + ".");
/*      */     }
/*  389 */     long result = ((this.data[(this.offset + this.cursor + 3)] & 0xFF) << 24) + ((this.data[(this.offset + this.cursor + 2)] & 0xFF) << 16) + ((this.data[(this.offset + this.cursor + 1)] & 0xFF) << 8) + (this.data[(this.offset + this.cursor + 0)] & 0xFF);
/*      */     
/*  391 */     this.cursor += 4;
/*  392 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long readUnsignedInt()
/*      */   {
/*  399 */     if (this.cursor + 4 > this.length) {
/*  400 */       throw new ArrayIndexOutOfBoundsException("Cannot read 4 bytes from this buffer: " + this + ".");
/*      */     }
/*  402 */     byte value1 = this.data[(this.offset + this.cursor + 0)];
/*  403 */     byte value2 = this.data[(this.offset + this.cursor + 1)];
/*  404 */     byte value3 = this.data[(this.offset + this.cursor + 2)];
/*  405 */     byte value4 = this.data[(this.offset + this.cursor + 3)];
/*  406 */     long result = calculateUnsignedInt(value1, value2, value3, value4);
/*  407 */     this.cursor += 4;
/*  408 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readVariableSignedIntLE()
/*      */   {
/*  417 */     int result = 0;
/*      */     
/*  419 */     for (int shift = 0; shift < 32; shift += 7) {
/*  420 */       int b = readUnsignedByte();
/*  421 */       result |= (b & 0x7F) << shift;
/*  422 */       if ((b & 0x80) == 0) {
/*      */         break;
/*      */       }
/*      */     }
/*  426 */     return result;
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
/*      */   public int readEncodedUnsignedInt()
/*      */   {
/*  439 */     int firstByte = readUnsignedByte();
/*      */     int result;
/*  441 */     int result; int result; int result; switch (firstByte & 0xC0) {
/*      */     case 0: 
/*      */     default: 
/*  444 */       result = firstByte & 0x3F;
/*  445 */       break;
/*      */     case 64: 
/*  447 */       result = firstByte & 0x3F00 | readUnsignedByte();
/*  448 */       break;
/*      */     case 128: 
/*  450 */       result = (firstByte & 0x3F00 | readUnsignedByte()) << 8 | readUnsignedByte();
/*  451 */       break;
/*      */     case 192: 
/*  453 */       result = (firstByte & 0x3F00 | readUnsignedByte()) << 8 | readUnsignedByte() << 8 | readUnsignedByte();
/*      */     }
/*      */     
/*      */     
/*  457 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readUnsignedByte()
/*      */   {
/*  464 */     if (this.cursor + 1 > this.length) {
/*  465 */       throw new ArrayIndexOutOfBoundsException("Cannot read 1 byte from this buffer: " + this + ".");
/*      */     }
/*  467 */     byte value = this.data[(this.offset + this.cursor)];
/*  468 */     int b = calculateUnsignedByte(value);
/*  469 */     this.cursor += 1;
/*  470 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte readSignedByte()
/*      */   {
/*  477 */     if (this.cursor + 1 > this.length) {
/*  478 */       throw new ArrayIndexOutOfBoundsException("Cannot read 1 byte from this buffer: " + this + ".");
/*      */     }
/*  480 */     byte b = this.data[(this.offset + this.cursor)];
/*  481 */     this.cursor += 1;
/*  482 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readUnsignedShort()
/*      */   {
/*  489 */     if (this.cursor + 2 > this.length) {
/*  490 */       throw new ArrayIndexOutOfBoundsException("Cannot read 2 bytes from this buffer: " + this + ".");
/*      */     }
/*  492 */     byte value1 = this.data[(this.offset + this.cursor)];
/*  493 */     byte value2 = this.data[(this.offset + this.cursor + 1)];
/*  494 */     int result = calculateUnsignedShort(value1, value2);
/*  495 */     this.cursor += 2;
/*  496 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short readSignedShortLE()
/*      */   {
/*  503 */     if (this.cursor + 2 > this.length) {
/*  504 */       throw new ArrayIndexOutOfBoundsException("Cannot read 2 bytes from this buffer: " + this + ".");
/*      */     }
/*  506 */     short result = (short)((this.data[(this.offset + this.cursor + 1)] & 0xFF) << 8 | this.data[(this.offset + this.cursor)] & 0xFF);
/*  507 */     this.cursor += 2;
/*  508 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short readSignedShort()
/*      */   {
/*  515 */     if (this.cursor + 2 > this.length) {
/*  516 */       throw new ArrayIndexOutOfBoundsException("Cannot read 2 bytes from this buffer: " + this + ".");
/*      */     }
/*  518 */     byte value1 = this.data[(this.offset + this.cursor + 0)];
/*  519 */     byte value2 = this.data[(this.offset + this.cursor + 1)];
/*  520 */     short result = calculateSignedShort(value1, value2);
/*  521 */     this.cursor += 2;
/*  522 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readVariableUnsignedShort()
/*      */   {
/*  533 */     int firstByte = readUnsignedByte();
/*      */     int result;
/*      */     int result;
/*  536 */     if ((firstByte & 0x80) == 0) {
/*  537 */       result = firstByte & 0x7F;
/*      */     } else {
/*  539 */       int secondByte = readUnsignedByte();
/*  540 */       result = (firstByte & 0x7F) << 8 | secondByte;
/*      */     }
/*      */     
/*  543 */     return result;
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
/*      */   public long readBerLength()
/*      */   {
/*  561 */     int firstByte = readUnsignedByte();
/*      */     
/*      */     long result;
/*  564 */     if ((firstByte & 0x80) == 0) {
/*  565 */       result = firstByte & 0x7F;
/*      */     } else {
/*  567 */       int intLength = firstByte & 0x7F;
/*  568 */       long result; if (intLength != 0) {
/*  569 */         result = readUnsignedVarInt(intLength);
/*      */       } else
/*  571 */         return -1L;
/*      */     }
/*      */     long result;
/*  574 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeBerLength(long length)
/*      */   {
/*  585 */     if (length < 0L) {
/*  586 */       throw new RuntimeException("Length cannot be less than zero: " + length + ". Data: " + this + ".");
/*      */     }
/*  588 */     if (length < 128L) {
/*  589 */       writeByte((int)length);
/*      */     }
/*  591 */     else if (length < 255L) {
/*  592 */       writeByte(129);
/*  593 */       writeByte((int)length);
/*  594 */     } else if (length <= 65535L) {
/*  595 */       writeByte(130);
/*  596 */       writeShort((int)length);
/*  597 */     } else if (length <= 16777215L) {
/*  598 */       writeByte(131);
/*  599 */       writeByte((int)(length >> 16));
/*  600 */       writeShort((int)length);
/*  601 */     } else if (length <= 4294967295L) {
/*  602 */       writeByte(132);
/*  603 */       writeInt((int)length);
/*  604 */     } else if (length <= 1099511627775L) {
/*  605 */       writeByte(133);
/*  606 */       writeByte((int)(length >> 32));
/*  607 */       writeInt((int)length);
/*  608 */     } else if (length <= 281474976710655L) {
/*  609 */       writeByte(134);
/*  610 */       writeShort((int)(length >> 32));
/*  611 */       writeInt((int)length);
/*  612 */     } else if (length <= 72057594037927935L) {
/*  613 */       writeByte(135);
/*  614 */       writeByte((int)(length >> 48));
/*  615 */       writeShort((int)(length >> 32));
/*  616 */       writeInt((int)length);
/*      */     } else {
/*  618 */       writeByte(136);
/*  619 */       writeInt((int)(length >> 32));
/*  620 */       writeInt((int)length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long readSignedVarInt(int len)
/*      */   {
/*  632 */     long value = 0L;
/*  633 */     switch (len) {
/*      */     case 0: 
/*  635 */       value = 0L;
/*  636 */       break;
/*      */     case 1: 
/*  638 */       value = readSignedByte();
/*  639 */       break;
/*      */     case 2: 
/*  641 */       value = readSignedShort();
/*  642 */       break;
/*      */     case 3: 
/*  644 */       value = readSignedByte() << 16 | readUnsignedShort();
/*  645 */       break;
/*      */     case 4: 
/*  647 */       value = readSignedInt();
/*  648 */       break;
/*      */     case 5: 
/*  650 */       value = readSignedByte() | readUnsignedInt();
/*  651 */       break;
/*      */     case 6: 
/*  653 */       value = readSignedShort() | readUnsignedInt();
/*  654 */       break;
/*      */     case 7: 
/*  656 */       value = readSignedByte() << 24 | readUnsignedShort() | readUnsignedInt();
/*  657 */       break;
/*      */     case 8: 
/*  659 */       value = readSignedLong();
/*  660 */       break;
/*      */     default: 
/*  662 */       throw new RuntimeException("Cannot read integers which are more than 8 bytes long. Length: " + len + ". Data: " + this + ".");
/*      */     }
/*      */     
/*  665 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public long readUnsignedVarInt(int len)
/*      */   {
/*  673 */     long value = 0L;
/*  674 */     switch (len) {
/*      */     case 0: 
/*  676 */       value = 0L;
/*  677 */       break;
/*      */     case 1: 
/*  679 */       value = readUnsignedByte();
/*  680 */       break;
/*      */     case 2: 
/*  682 */       value = readUnsignedShort();
/*  683 */       break;
/*      */     case 3: 
/*  685 */       value = readUnsignedByte() << 16 | readUnsignedShort();
/*  686 */       break;
/*      */     case 4: 
/*  688 */       value = readUnsignedInt();
/*  689 */       break;
/*      */     case 5: 
/*  691 */       value = readUnsignedByte() | readUnsignedInt();
/*  692 */       break;
/*      */     case 6: 
/*  694 */       value = readUnsignedShort() | readUnsignedInt();
/*  695 */       break;
/*      */     case 7: 
/*  697 */       value = readUnsignedByte() << 16 | readUnsignedShort() | readUnsignedInt();
/*  698 */       break;
/*      */     case 8: 
/*  700 */       value = readSignedLong();
/*  701 */       if (value < 0L) {
/*  702 */         throw new RuntimeException("Cannot read 64 bit integers which are larger than 0x7FffFFffFFffFFff, because of lack of unsinged long type in Java. Value: " + value + ". Data: " + this + ".");
/*      */       }
/*      */       break;
/*      */     default: 
/*  706 */       throw new RuntimeException("Cannot read integers which are more than 8 bytes long. Length: " + len + ". Data: " + this + ".");
/*      */     }
/*      */     
/*  709 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int readUnsignedShortLE()
/*      */   {
/*  716 */     if (this.cursor + 2 > this.length) {
/*  717 */       throw new ArrayIndexOutOfBoundsException("Cannot read 2 bytes from this buffer: " + this + ".");
/*      */     }
/*  719 */     int result = (this.data[(this.offset + this.cursor + 1)] & 0xFF) << 8 | this.data[(this.offset + this.cursor)] & 0xFF;
/*  720 */     this.cursor += 2;
/*  721 */     return result;
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
/*      */   public int readEncodedUnsignedShort()
/*      */   {
/*  734 */     int firstByte = readUnsignedByte();
/*      */     int result;
/*      */     int result;
/*  737 */     if ((firstByte & 0x80) == 0) {
/*  738 */       result = firstByte & 0x7F;
/*      */     } else {
/*  740 */       int secondByte = readUnsignedByte();
/*  741 */       result = (firstByte & 0x7F) << 8 | secondByte;
/*      */     }
/*      */     
/*  744 */     return result;
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
/*      */   public int readEncodedSignedShort()
/*      */   {
/*  758 */     int firstByte = readUnsignedByte();
/*      */     int result;
/*      */     int result;
/*  761 */     if ((firstByte & 0x80) == 0) {
/*  762 */       result = firstByte & 0x3F;
/*      */     } else {
/*  764 */       int secondByte = readUnsignedByte();
/*  765 */       result = (firstByte & 0x3F) << 8 | secondByte;
/*      */     }
/*      */     
/*  768 */     if ((firstByte & 0x40) > 0) {
/*  769 */       return -result;
/*      */     }
/*  771 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long readSignedLongLE()
/*      */   {
/*  778 */     return readSignedIntLE() & 0xFFFFFFFF | readSignedIntLE() << 32;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long readSignedLong()
/*      */   {
/*  785 */     return readSignedInt() << 32 | readSignedInt() & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String readString(int length, Charset charset)
/*      */   {
/*  792 */     if (this.cursor + length > this.length) {
/*  793 */       throw new ArrayIndexOutOfBoundsException("Cannot read " + length + " bytes from this buffer: " + this + ".");
/*      */     }
/*  795 */     String string = new String(this.data, this.offset + this.cursor, length, charset);
/*  796 */     this.cursor += length;
/*  797 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readVariableString(Charset charset)
/*      */   {
/*  805 */     int start = this.cursor;
/*      */     
/*      */ 
/*  808 */     while (readUnsignedByte() != 0) {}
/*      */     
/*      */ 
/*  811 */     String string = new String(this.data, this.offset + start, this.cursor - start - 1, charset);
/*      */     
/*  813 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readVariableWideString(Charset charset)
/*      */   {
/*  821 */     int start = this.cursor;
/*      */     
/*      */ 
/*  824 */     while (readUnsignedShortLE() != 0) {}
/*      */     
/*      */ 
/*  827 */     String string = new String(this.data, this.offset + start, this.cursor - start - 2, charset);
/*      */     
/*  829 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ByteBuffer readBytes(int dataLength)
/*      */   {
/*  836 */     if (this.cursor + dataLength > this.length) {
/*  837 */       throw new ArrayIndexOutOfBoundsException("Cannot read " + dataLength + " bytes from this buffer: " + this + ".");
/*      */     }
/*  839 */     ByteBuffer slice = slice(this.cursor, dataLength, false);
/*  840 */     this.cursor += dataLength;
/*  841 */     return slice;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void skipBytes(int numOfBytes)
/*      */   {
/*  848 */     if (this.cursor + numOfBytes > this.length) {
/*  849 */       throw new ArrayIndexOutOfBoundsException("Cannot read " + numOfBytes + " bytes from this buffer: " + this + ".");
/*      */     }
/*  851 */     this.cursor += numOfBytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeByte(int b)
/*      */   {
/*  858 */     if (this.cursor + 1 > this.length) {
/*  859 */       throw new ArrayIndexOutOfBoundsException("Cannot write 1 byte to this buffer: " + this + ".");
/*      */     }
/*  861 */     this.data[(this.offset + this.cursor)] = ((byte)b);
/*  862 */     this.cursor += 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeShort(int x)
/*      */   {
/*  869 */     if (this.cursor + 2 > this.length) {
/*  870 */       throw new ArrayIndexOutOfBoundsException("Cannot write 2 bytes to this buffer: " + this + ".");
/*      */     }
/*  872 */     this.data[(this.offset + this.cursor)] = ((byte)(x >> 8));
/*  873 */     this.data[(this.offset + this.cursor + 1)] = ((byte)x);
/*  874 */     this.cursor += 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeShortLE(int x)
/*      */   {
/*  881 */     if (this.cursor + 2 > this.length) {
/*  882 */       throw new ArrayIndexOutOfBoundsException("Cannot write 2 bytes to this buffer: " + this + ".");
/*      */     }
/*  884 */     this.data[(this.offset + this.cursor + 1)] = ((byte)(x >> 8));
/*  885 */     this.data[(this.offset + this.cursor)] = ((byte)x);
/*  886 */     this.cursor += 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeInt(int i)
/*      */   {
/*  893 */     if (this.cursor + 4 > this.length) {
/*  894 */       throw new ArrayIndexOutOfBoundsException("Cannot write 4 bytes to this buffer: " + this + ".");
/*      */     }
/*  896 */     this.data[(this.offset + this.cursor)] = ((byte)(i >> 24));
/*  897 */     this.data[(this.offset + this.cursor + 1)] = ((byte)(i >> 16));
/*  898 */     this.data[(this.offset + this.cursor + 2)] = ((byte)(i >> 8));
/*  899 */     this.data[(this.offset + this.cursor + 3)] = ((byte)i);
/*  900 */     this.cursor += 4;
/*      */   }
/*      */   
/*      */   public void writeIntLE(int i) {
/*  904 */     if (this.cursor + 4 > this.length) {
/*  905 */       throw new ArrayIndexOutOfBoundsException("Cannot write 4 bytes to this buffer: " + this + ".");
/*      */     }
/*  907 */     this.data[(this.offset + this.cursor)] = ((byte)i);
/*  908 */     this.data[(this.offset + this.cursor + 1)] = ((byte)(i >> 8));
/*  909 */     this.data[(this.offset + this.cursor + 2)] = ((byte)(i >> 16));
/*  910 */     this.data[(this.offset + this.cursor + 3)] = ((byte)(i >> 24));
/*  911 */     this.cursor += 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeVariableIntLE(int i)
/*      */   {
/*  921 */     while (i != 0)
/*      */     {
/*  923 */       int b = i & 0x7F;
/*  924 */       i >>= 7;
/*      */       
/*  926 */       if (i > 0)
/*      */       {
/*      */ 
/*  929 */         b |= 0x80;
/*      */       }
/*  931 */       writeByte(b);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeVariableShort(int length)
/*      */   {
/*  942 */     if (((length > 127 ? 1 : 0) | (length < 0 ? 1 : 0)) != 0) {
/*  943 */       writeShort(length | 0x8000);
/*      */     } else {
/*  945 */       writeByte(length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void prepend(ByteBuffer buf)
/*      */   {
/*  952 */     prepend(buf.data, buf.offset, buf.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void prepend(byte[] data)
/*      */   {
/*  959 */     prepend(data, 0, data.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void prepend(byte[] data, int offset, int length)
/*      */   {
/*  966 */     if (!isSoleOwner()) {
/*  967 */       throw new RuntimeException("Create full copy of this byte buffer data for modification. refCount: " + this.refCount + ".");
/*      */     }
/*      */     
/*      */ 
/*  971 */     if (this.offset < length) {
/*  972 */       throw new RuntimeException("Reserve data to have enough space for header.");
/*      */     }
/*      */     
/*      */ 
/*  976 */     System.arraycopy(data, offset, this.data, this.offset - length, length);
/*      */     
/*      */ 
/*  979 */     this.offset -= length;
/*  980 */     this.length += length;
/*  981 */     this.cursor += length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeString(String str, Charset charset)
/*      */   {
/*  989 */     writeBytes(str.getBytes(charset));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFixedString(int length, String str, Charset charset)
/*      */   {
/*  997 */     byte[] bytes = str.getBytes(charset);
/*  998 */     writeBytes(bytes, 0, Math.min(bytes.length, length));
/*      */     
/* 1000 */     for (int i = bytes.length; i < length; i++)
/* 1001 */       writeByte(0);
/*      */   }
/*      */   
/*      */   public void writeBytes(ByteBuffer buf) {
/* 1005 */     writeBytes(buf.data, buf.offset, buf.length);
/*      */   }
/*      */   
/*      */   public void writeBytes(byte[] bytes) {
/* 1009 */     writeBytes(bytes, 0, bytes.length);
/*      */   }
/*      */   
/*      */   public void writeBytes(byte[] bytes, int offset, int length) {
/* 1013 */     System.arraycopy(bytes, offset, this.data, this.offset + this.cursor, length);
/* 1014 */     this.cursor += length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void trimAtCursor()
/*      */   {
/* 1021 */     this.length = this.cursor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void rewindCursor()
/*      */   {
/* 1028 */     this.cursor = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readRGBColor()
/*      */   {
/* 1037 */     return readUnsignedByte() | readUnsignedByte() << 8 | readUnsignedByte() << 16;
/*      */   }
/*      */   
/*      */   public void assertThatBufferIsFullyRead() {
/* 1041 */     if (this.cursor != this.length) {
/* 1042 */       throw new RuntimeException("Data in buffer is not read fully. Buf: " + this + ".");
/*      */     }
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1047 */     int prime = 31;
/* 1048 */     int result = 1;
/*      */     
/* 1050 */     int end = this.offset + this.length;
/* 1051 */     for (int i = this.offset; i < end; i++) {
/* 1052 */       result = 31 * result + this.data[i];
/*      */     }
/* 1054 */     result = 31 * result + this.length;
/* 1055 */     return result;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1060 */     if (this == obj) {
/* 1061 */       return true;
/*      */     }
/* 1063 */     if (obj == null) {
/* 1064 */       return false;
/*      */     }
/*      */     
/* 1067 */     if (getClass() != obj.getClass()) {
/* 1068 */       return false;
/*      */     }
/* 1070 */     ByteBuffer other = (ByteBuffer)obj;
/* 1071 */     if (this.length != other.length) {
/* 1072 */       return false;
/*      */     }
/* 1074 */     for (int i = 0; i < this.length; i++) {
/* 1075 */       if (this.data[(this.offset + i)] != other.data[(other.offset + i)])
/* 1076 */         return false;
/*      */     }
/* 1078 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int remainderLength()
/*      */   {
/* 1085 */     if (this.length >= this.cursor) {
/* 1086 */       return this.length - this.cursor;
/*      */     }
/* 1088 */     throw new RuntimeException("Inconsistent state of buffer: cursor is after end of buffer: " + this + ".");
/*      */   }
/*      */   
/*      */   public Set<String> getMetadataKeys() {
/* 1092 */     if (this.metadata != null) {
/* 1093 */       return this.metadata.keySet();
/*      */     }
/* 1095 */     return new HashSet(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int peekUnsignedByte(int i)
/*      */   {
/* 1103 */     return this.data[(this.offset + this.cursor + i)] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void trimHeader(int length)
/*      */   {
/* 1110 */     this.offset += length;
/* 1111 */     this.length -= length;
/* 1112 */     rewindCursor();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\ByteBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */