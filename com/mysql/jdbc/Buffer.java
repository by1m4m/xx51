/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.sql.SQLException;
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
/*     */ public class Buffer
/*     */ {
/*     */   static final int MAX_BYTES_TO_DUMP = 512;
/*     */   static final int NO_LENGTH_LIMIT = -1;
/*     */   static final long NULL_LENGTH = -1L;
/*  45 */   private int bufLength = 0;
/*     */   
/*     */   private byte[] byteBuffer;
/*     */   
/*  49 */   private int position = 0;
/*     */   
/*  51 */   protected boolean wasMultiPacket = false;
/*     */   
/*     */   public Buffer(byte[] buf) {
/*  54 */     this.byteBuffer = buf;
/*  55 */     setBufLength(buf.length);
/*     */   }
/*     */   
/*     */   Buffer(int size) {
/*  59 */     this.byteBuffer = new byte[size];
/*  60 */     setBufLength(this.byteBuffer.length);
/*  61 */     this.position = 4;
/*     */   }
/*     */   
/*     */   final void clear() {
/*  65 */     this.position = 4;
/*     */   }
/*     */   
/*     */   final void dump() {
/*  69 */     dump(getBufLength());
/*     */   }
/*     */   
/*     */   final String dump(int numBytes) {
/*  73 */     return StringUtils.dumpAsHex(getBytes(0, numBytes > getBufLength() ? getBufLength() : numBytes), numBytes > getBufLength() ? getBufLength() : numBytes);
/*     */   }
/*     */   
/*     */ 
/*     */   final String dumpClampedBytes(int numBytes)
/*     */   {
/*  79 */     int numBytesToDump = numBytes < 512 ? numBytes : 512;
/*     */     
/*     */ 
/*  82 */     String dumped = StringUtils.dumpAsHex(getBytes(0, numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump), numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     if (numBytesToDump < numBytes) {
/*  89 */       return dumped + " ....(packet exceeds max. dump length)";
/*     */     }
/*     */     
/*  92 */     return dumped;
/*     */   }
/*     */   
/*     */   final void dumpHeader() {
/*  96 */     for (int i = 0; i < 4; i++) {
/*  97 */       String hexVal = Integer.toHexString(readByte(i) & 0xFF);
/*     */       
/*  99 */       if (hexVal.length() == 1) {
/* 100 */         hexVal = "0" + hexVal;
/*     */       }
/*     */       
/* 103 */       System.out.print(hexVal + " ");
/*     */     }
/*     */   }
/*     */   
/*     */   final void dumpNBytes(int start, int nBytes) {
/* 108 */     StringBuffer asciiBuf = new StringBuffer();
/*     */     
/* 110 */     for (int i = start; (i < start + nBytes) && (i < getBufLength()); i++) {
/* 111 */       String hexVal = Integer.toHexString(readByte(i) & 0xFF);
/*     */       
/* 113 */       if (hexVal.length() == 1) {
/* 114 */         hexVal = "0" + hexVal;
/*     */       }
/*     */       
/* 117 */       System.out.print(hexVal + " ");
/*     */       
/* 119 */       if ((readByte(i) > 32) && (readByte(i) < Byte.MAX_VALUE)) {
/* 120 */         asciiBuf.append((char)readByte(i));
/*     */       } else {
/* 122 */         asciiBuf.append(".");
/*     */       }
/*     */       
/* 125 */       asciiBuf.append(" ");
/*     */     }
/*     */     
/* 128 */     System.out.println("    " + asciiBuf.toString());
/*     */   }
/*     */   
/*     */   final void ensureCapacity(int additionalData) throws SQLException {
/* 132 */     if (this.position + additionalData > getBufLength()) {
/* 133 */       if (this.position + additionalData < this.byteBuffer.length)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */         setBufLength(this.byteBuffer.length);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 145 */         int newLength = (int)(this.byteBuffer.length * 1.25D);
/*     */         
/* 147 */         if (newLength < this.byteBuffer.length + additionalData) {
/* 148 */           newLength = this.byteBuffer.length + (int)(additionalData * 1.25D);
/*     */         }
/*     */         
/*     */ 
/* 152 */         if (newLength < this.byteBuffer.length) {
/* 153 */           newLength = this.byteBuffer.length + additionalData;
/*     */         }
/*     */         
/* 156 */         byte[] newBytes = new byte[newLength];
/*     */         
/* 158 */         System.arraycopy(this.byteBuffer, 0, newBytes, 0, this.byteBuffer.length);
/*     */         
/* 160 */         this.byteBuffer = newBytes;
/* 161 */         setBufLength(this.byteBuffer.length);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int fastSkipLenString()
/*     */   {
/* 172 */     long len = readFieldLength();
/*     */     
/* 174 */     this.position = ((int)(this.position + len));
/*     */     
/* 176 */     return (int)len;
/*     */   }
/*     */   
/*     */   public void fastSkipLenByteArray() {
/* 180 */     long len = readFieldLength();
/*     */     
/* 182 */     if ((len == -1L) || (len == 0L)) {
/* 183 */       return;
/*     */     }
/*     */     
/* 186 */     this.position = ((int)(this.position + len));
/*     */   }
/*     */   
/*     */   protected final byte[] getBufferSource() {
/* 190 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   public int getBufLength() {
/* 194 */     return this.bufLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getByteBuffer()
/*     */   {
/* 203 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   final byte[] getBytes(int len) {
/* 207 */     byte[] b = new byte[len];
/* 208 */     System.arraycopy(this.byteBuffer, this.position, b, 0, len);
/* 209 */     this.position += len;
/*     */     
/* 211 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   byte[] getBytes(int offset, int len)
/*     */   {
/* 220 */     byte[] dest = new byte[len];
/* 221 */     System.arraycopy(this.byteBuffer, offset, dest, 0, len);
/*     */     
/* 223 */     return dest;
/*     */   }
/*     */   
/*     */   int getCapacity() {
/* 227 */     return this.byteBuffer.length;
/*     */   }
/*     */   
/*     */   public ByteBuffer getNioBuffer() {
/* 231 */     throw new IllegalArgumentException(Messages.getString("ByteArrayBuffer.0"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 241 */     return this.position;
/*     */   }
/*     */   
/*     */   final boolean isLastDataPacket()
/*     */   {
/* 246 */     return (getBufLength() < 9) && ((this.byteBuffer[0] & 0xFF) == 254);
/*     */   }
/*     */   
/*     */   final boolean isAuthMethodSwitchRequestPacket() {
/* 250 */     return (this.byteBuffer[0] & 0xFF) == 254;
/*     */   }
/*     */   
/*     */   final boolean isOKPacket() {
/* 254 */     return (this.byteBuffer[0] & 0xFF) == 0;
/*     */   }
/*     */   
/*     */   final boolean isRawPacket() {
/* 258 */     return (this.byteBuffer[0] & 0xFF) == 1;
/*     */   }
/*     */   
/*     */   final long newReadLength() {
/* 262 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 264 */     switch (sw) {
/*     */     case 251: 
/* 266 */       return 0L;
/*     */     
/*     */     case 252: 
/* 269 */       return readInt();
/*     */     
/*     */     case 253: 
/* 272 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 275 */       return readLongLong();
/*     */     }
/*     */     
/* 278 */     return sw;
/*     */   }
/*     */   
/*     */   final byte readByte()
/*     */   {
/* 283 */     return this.byteBuffer[(this.position++)];
/*     */   }
/*     */   
/*     */   final byte readByte(int readAt) {
/* 287 */     return this.byteBuffer[readAt];
/*     */   }
/*     */   
/*     */   final long readFieldLength() {
/* 291 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 293 */     switch (sw) {
/*     */     case 251: 
/* 295 */       return -1L;
/*     */     
/*     */     case 252: 
/* 298 */       return readInt();
/*     */     
/*     */     case 253: 
/* 301 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 304 */       return readLongLong();
/*     */     }
/*     */     
/* 307 */     return sw;
/*     */   }
/*     */   
/*     */ 
/*     */   final int readInt()
/*     */   {
/* 313 */     byte[] b = this.byteBuffer;
/*     */     
/* 315 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8;
/*     */   }
/*     */   
/*     */   final int readIntAsLong() {
/* 319 */     byte[] b = this.byteBuffer;
/*     */     
/* 321 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */   final byte[] readLenByteArray(int offset)
/*     */   {
/* 327 */     long len = readFieldLength();
/*     */     
/* 329 */     if (len == -1L) {
/* 330 */       return null;
/*     */     }
/*     */     
/* 333 */     if (len == 0L) {
/* 334 */       return Constants.EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 337 */     this.position += offset;
/*     */     
/* 339 */     return getBytes((int)len);
/*     */   }
/*     */   
/*     */   final long readLength() {
/* 343 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 345 */     switch (sw) {
/*     */     case 251: 
/* 347 */       return 0L;
/*     */     
/*     */     case 252: 
/* 350 */       return readInt();
/*     */     
/*     */     case 253: 
/* 353 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 356 */       return readLong();
/*     */     }
/*     */     
/* 359 */     return sw;
/*     */   }
/*     */   
/*     */ 
/*     */   final long readLong()
/*     */   {
/* 365 */     byte[] b = this.byteBuffer;
/*     */     
/* 367 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int readLongInt()
/*     */   {
/* 375 */     byte[] b = this.byteBuffer;
/*     */     
/* 377 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16;
/*     */   }
/*     */   
/*     */ 
/*     */   final long readLongLong()
/*     */   {
/* 383 */     byte[] b = this.byteBuffer;
/*     */     
/* 385 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24 | (b[(this.position++)] & 0xFF) << 32 | (b[(this.position++)] & 0xFF) << 40 | (b[(this.position++)] & 0xFF) << 48 | (b[(this.position++)] & 0xFF) << 56;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int readnBytes()
/*     */   {
/* 396 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 398 */     switch (sw) {
/*     */     case 1: 
/* 400 */       return this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/*     */     case 2: 
/* 403 */       return readInt();
/*     */     
/*     */     case 3: 
/* 406 */       return readLongInt();
/*     */     
/*     */     case 4: 
/* 409 */       return (int)readLong();
/*     */     }
/*     */     
/* 412 */     return 255;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String readString()
/*     */   {
/* 423 */     int i = this.position;
/* 424 */     int len = 0;
/* 425 */     int maxLen = getBufLength();
/*     */     
/* 427 */     while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
/* 428 */       len++;
/* 429 */       i++;
/*     */     }
/*     */     
/* 432 */     String s = StringUtils.toString(this.byteBuffer, this.position, len);
/* 433 */     this.position += len + 1;
/*     */     
/* 435 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final String readString(String encoding, ExceptionInterceptor exceptionInterceptor)
/*     */     throws SQLException
/*     */   {
/* 446 */     int i = this.position;
/* 447 */     int len = 0;
/* 448 */     int maxLen = getBufLength();
/*     */     
/* 450 */     while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
/* 451 */       len++;
/* 452 */       i++;
/*     */     }
/*     */     try
/*     */     {
/* 456 */       return StringUtils.toString(this.byteBuffer, this.position, len, encoding);
/*     */     } catch (UnsupportedEncodingException uEE) {
/* 458 */       throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", "S1009", exceptionInterceptor);
/*     */     }
/*     */     finally {
/* 461 */       this.position += len + 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   final String readString(String encoding, ExceptionInterceptor exceptionInterceptor, int expectedLength)
/*     */     throws SQLException
/*     */   {
/* 469 */     if (this.position + expectedLength > getBufLength()) {
/* 470 */       throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.2"), "S1009", exceptionInterceptor);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 475 */       return StringUtils.toString(this.byteBuffer, this.position, expectedLength, encoding);
/*     */     } catch (UnsupportedEncodingException uEE) {
/* 477 */       throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", "S1009", exceptionInterceptor);
/*     */     }
/*     */     finally {
/* 480 */       this.position += expectedLength;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBufLength(int bufLengthToSet) {
/* 485 */     this.bufLength = bufLengthToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteBuffer(byte[] byteBufferToSet)
/*     */   {
/* 495 */     this.byteBuffer = byteBufferToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPosition(int positionToSet)
/*     */   {
/* 505 */     this.position = positionToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWasMultiPacket(boolean flag)
/*     */   {
/* 515 */     this.wasMultiPacket = flag;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 519 */     return dumpClampedBytes(getPosition());
/*     */   }
/*     */   
/*     */   public String toSuperString() {
/* 523 */     return super.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean wasMultiPacket()
/*     */   {
/* 532 */     return this.wasMultiPacket;
/*     */   }
/*     */   
/*     */   public final void writeByte(byte b) throws SQLException {
/* 536 */     ensureCapacity(1);
/*     */     
/* 538 */     this.byteBuffer[(this.position++)] = b;
/*     */   }
/*     */   
/*     */   public final void writeBytesNoNull(byte[] bytes) throws SQLException
/*     */   {
/* 543 */     int len = bytes.length;
/* 544 */     ensureCapacity(len);
/* 545 */     System.arraycopy(bytes, 0, this.byteBuffer, this.position, len);
/* 546 */     this.position += len;
/*     */   }
/*     */   
/*     */   final void writeBytesNoNull(byte[] bytes, int offset, int length)
/*     */     throws SQLException
/*     */   {
/* 552 */     ensureCapacity(length);
/* 553 */     System.arraycopy(bytes, offset, this.byteBuffer, this.position, length);
/* 554 */     this.position += length;
/*     */   }
/*     */   
/*     */   final void writeDouble(double d) throws SQLException {
/* 558 */     long l = Double.doubleToLongBits(d);
/* 559 */     writeLongLong(l);
/*     */   }
/*     */   
/*     */   final void writeFieldLength(long length) throws SQLException {
/* 563 */     if (length < 251L) {
/* 564 */       writeByte((byte)(int)length);
/* 565 */     } else if (length < 65536L) {
/* 566 */       ensureCapacity(3);
/* 567 */       writeByte((byte)-4);
/* 568 */       writeInt((int)length);
/* 569 */     } else if (length < 16777216L) {
/* 570 */       ensureCapacity(4);
/* 571 */       writeByte((byte)-3);
/* 572 */       writeLongInt((int)length);
/*     */     } else {
/* 574 */       ensureCapacity(9);
/* 575 */       writeByte((byte)-2);
/* 576 */       writeLongLong(length);
/*     */     }
/*     */   }
/*     */   
/*     */   final void writeFloat(float f) throws SQLException {
/* 581 */     ensureCapacity(4);
/*     */     
/* 583 */     int i = Float.floatToIntBits(f);
/* 584 */     byte[] b = this.byteBuffer;
/* 585 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 586 */     b[(this.position++)] = ((byte)(i >>> 8));
/* 587 */     b[(this.position++)] = ((byte)(i >>> 16));
/* 588 */     b[(this.position++)] = ((byte)(i >>> 24));
/*     */   }
/*     */   
/*     */   final void writeInt(int i) throws SQLException
/*     */   {
/* 593 */     ensureCapacity(2);
/*     */     
/* 595 */     byte[] b = this.byteBuffer;
/* 596 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 597 */     b[(this.position++)] = ((byte)(i >>> 8));
/*     */   }
/*     */   
/*     */   final void writeLenBytes(byte[] b)
/*     */     throws SQLException
/*     */   {
/* 603 */     int len = b.length;
/* 604 */     ensureCapacity(len + 9);
/* 605 */     writeFieldLength(len);
/* 606 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 607 */     this.position += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final void writeLenString(String s, String encoding, String serverEncoding, SingleByteCharsetConverter converter, boolean parserKnowsUnicode, MySQLConnection conn)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/* 616 */     byte[] b = null;
/*     */     
/* 618 */     if (converter != null) {
/* 619 */       b = converter.toBytes(s);
/*     */     } else {
/* 621 */       b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn, conn.getExceptionInterceptor());
/*     */     }
/*     */     
/*     */ 
/* 625 */     int len = b.length;
/* 626 */     ensureCapacity(len + 9);
/* 627 */     writeFieldLength(len);
/* 628 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 629 */     this.position += len;
/*     */   }
/*     */   
/*     */   final void writeLong(long i) throws SQLException
/*     */   {
/* 634 */     ensureCapacity(4);
/*     */     
/* 636 */     byte[] b = this.byteBuffer;
/* 637 */     b[(this.position++)] = ((byte)(int)(i & 0xFF));
/* 638 */     b[(this.position++)] = ((byte)(int)(i >>> 8));
/* 639 */     b[(this.position++)] = ((byte)(int)(i >>> 16));
/* 640 */     b[(this.position++)] = ((byte)(int)(i >>> 24));
/*     */   }
/*     */   
/*     */   final void writeLongInt(int i) throws SQLException
/*     */   {
/* 645 */     ensureCapacity(3);
/* 646 */     byte[] b = this.byteBuffer;
/* 647 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 648 */     b[(this.position++)] = ((byte)(i >>> 8));
/* 649 */     b[(this.position++)] = ((byte)(i >>> 16));
/*     */   }
/*     */   
/*     */   final void writeLongLong(long i) throws SQLException {
/* 653 */     ensureCapacity(8);
/* 654 */     byte[] b = this.byteBuffer;
/* 655 */     b[(this.position++)] = ((byte)(int)(i & 0xFF));
/* 656 */     b[(this.position++)] = ((byte)(int)(i >>> 8));
/* 657 */     b[(this.position++)] = ((byte)(int)(i >>> 16));
/* 658 */     b[(this.position++)] = ((byte)(int)(i >>> 24));
/* 659 */     b[(this.position++)] = ((byte)(int)(i >>> 32));
/* 660 */     b[(this.position++)] = ((byte)(int)(i >>> 40));
/* 661 */     b[(this.position++)] = ((byte)(int)(i >>> 48));
/* 662 */     b[(this.position++)] = ((byte)(int)(i >>> 56));
/*     */   }
/*     */   
/*     */   final void writeString(String s) throws SQLException
/*     */   {
/* 667 */     ensureCapacity(s.length() * 3 + 1);
/* 668 */     writeStringNoNull(s);
/* 669 */     this.byteBuffer[(this.position++)] = 0;
/*     */   }
/*     */   
/*     */   final void writeString(String s, String encoding, MySQLConnection conn) throws SQLException
/*     */   {
/* 674 */     ensureCapacity(s.length() * 3 + 1);
/*     */     try {
/* 676 */       writeStringNoNull(s, encoding, encoding, false, conn);
/*     */     } catch (UnsupportedEncodingException ue) {
/* 678 */       throw new SQLException(ue.toString(), "S1000");
/*     */     }
/*     */     
/* 681 */     this.byteBuffer[(this.position++)] = 0;
/*     */   }
/*     */   
/*     */   final void writeStringNoNull(String s) throws SQLException
/*     */   {
/* 686 */     int len = s.length();
/* 687 */     ensureCapacity(len * 3);
/* 688 */     System.arraycopy(StringUtils.getBytes(s), 0, this.byteBuffer, this.position, len);
/* 689 */     this.position += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void writeStringNoNull(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/* 702 */     byte[] b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn, conn.getExceptionInterceptor());
/*     */     
/*     */ 
/* 705 */     int len = b.length;
/* 706 */     ensureCapacity(len);
/* 707 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 708 */     this.position += len;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\Buffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */