/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Deque;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
/*     */   private static final int MAX_ARRAY_LEN = 2147483639;
/*     */   private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
/*     */   
/*     */   static byte[] createBuffer()
/*     */   {
/*  59 */     return new byte[' '];
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(InputStream from, OutputStream to)
/*     */     throws IOException
/*     */   {
/* 104 */     Preconditions.checkNotNull(from);
/* 105 */     Preconditions.checkNotNull(to);
/* 106 */     byte[] buf = createBuffer();
/* 107 */     long total = 0L;
/*     */     for (;;) {
/* 109 */       int r = from.read(buf);
/* 110 */       if (r == -1) {
/*     */         break;
/*     */       }
/* 113 */       to.write(buf, 0, r);
/* 114 */       total += r;
/*     */     }
/* 116 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(ReadableByteChannel from, WritableByteChannel to)
/*     */     throws IOException
/*     */   {
/* 130 */     Preconditions.checkNotNull(from);
/* 131 */     Preconditions.checkNotNull(to);
/* 132 */     if ((from instanceof FileChannel)) {
/* 133 */       FileChannel sourceChannel = (FileChannel)from;
/* 134 */       long oldPosition = sourceChannel.position();
/* 135 */       long position = oldPosition;
/*     */       long copied;
/*     */       do {
/* 138 */         copied = sourceChannel.transferTo(position, 524288L, to);
/* 139 */         position += copied;
/* 140 */         sourceChannel.position(position);
/* 141 */       } while ((copied > 0L) || (position < sourceChannel.size()));
/* 142 */       return position - oldPosition;
/*     */     }
/*     */     
/* 145 */     ByteBuffer buf = ByteBuffer.wrap(createBuffer());
/* 146 */     long total = 0L;
/* 147 */     while (from.read(buf) != -1) {
/* 148 */       buf.flip();
/* 149 */       while (buf.hasRemaining()) {
/* 150 */         total += to.write(buf);
/*     */       }
/* 152 */       buf.clear();
/*     */     }
/* 154 */     return total;
/*     */   }
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
/*     */   private static byte[] toByteArrayInternal(InputStream in, Deque<byte[]> bufs, int totalLen)
/*     */     throws IOException
/*     */   {
/* 173 */     for (int bufSize = 8192; 
/* 174 */         totalLen < 2147483639; 
/* 175 */         bufSize = IntMath.saturatedMultiply(bufSize, 2)) {
/* 176 */       byte[] buf = new byte[Math.min(bufSize, 2147483639 - totalLen)];
/* 177 */       bufs.add(buf);
/* 178 */       int off = 0;
/* 179 */       while (off < buf.length)
/*     */       {
/* 181 */         int r = in.read(buf, off, buf.length - off);
/* 182 */         if (r == -1) {
/* 183 */           return combineBuffers(bufs, totalLen);
/*     */         }
/* 185 */         off += r;
/* 186 */         totalLen += r;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (in.read() == -1)
/*     */     {
/* 193 */       return combineBuffers(bufs, 2147483639);
/*     */     }
/* 195 */     throw new OutOfMemoryError("input is too large to fit in a byte array");
/*     */   }
/*     */   
/*     */   private static byte[] combineBuffers(Deque<byte[]> bufs, int totalLen)
/*     */   {
/* 200 */     byte[] result = new byte[totalLen];
/* 201 */     int remaining = totalLen;
/* 202 */     while (remaining > 0) {
/* 203 */       byte[] buf = (byte[])bufs.removeFirst();
/* 204 */       int bytesToCopy = Math.min(remaining, buf.length);
/* 205 */       int resultOffset = totalLen - remaining;
/* 206 */       System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
/* 207 */       remaining -= bytesToCopy;
/*     */     }
/* 209 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] toByteArray(InputStream in)
/*     */     throws IOException
/*     */   {
/* 220 */     Preconditions.checkNotNull(in);
/* 221 */     return toByteArrayInternal(in, new ArrayDeque(20), 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static byte[] toByteArray(InputStream in, long expectedSize)
/*     */     throws IOException
/*     */   {
/* 230 */     Preconditions.checkArgument(expectedSize >= 0L, "expectedSize (%s) must be non-negative", expectedSize);
/* 231 */     if (expectedSize > 2147483639L) {
/* 232 */       throw new OutOfMemoryError(expectedSize + " bytes is too large to fit in a byte array");
/*     */     }
/*     */     
/* 235 */     byte[] bytes = new byte[(int)expectedSize];
/* 236 */     int remaining = (int)expectedSize;
/*     */     
/* 238 */     while (remaining > 0) {
/* 239 */       int off = (int)expectedSize - remaining;
/* 240 */       int read = in.read(bytes, off, remaining);
/* 241 */       if (read == -1)
/*     */       {
/*     */ 
/* 244 */         return Arrays.copyOf(bytes, off);
/*     */       }
/* 246 */       remaining -= read;
/*     */     }
/*     */     
/*     */ 
/* 250 */     int b = in.read();
/* 251 */     if (b == -1) {
/* 252 */       return bytes;
/*     */     }
/*     */     
/*     */ 
/* 256 */     Deque<byte[]> bufs = new ArrayDeque(22);
/* 257 */     bufs.add(bytes);
/* 258 */     bufs.add(new byte[] { (byte)b });
/* 259 */     return toByteArrayInternal(in, bufs, bytes.length + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(InputStream in)
/*     */     throws IOException
/*     */   {
/* 270 */     long total = 0L;
/*     */     
/* 272 */     byte[] buf = createBuffer();
/* 273 */     long read; while ((read = in.read(buf)) != -1L) {
/* 274 */       total += read;
/*     */     }
/* 276 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes)
/*     */   {
/* 284 */     return newDataInput(new ByteArrayInputStream(bytes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes, int start)
/*     */   {
/* 295 */     Preconditions.checkPositionIndex(start, bytes.length);
/* 296 */     return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream)
/*     */   {
/* 307 */     return new ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
/*     */     final DataInput input;
/*     */     
/*     */     ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
/* 314 */       this.input = new DataInputStream(byteArrayInputStream);
/*     */     }
/*     */     
/*     */     public void readFully(byte[] b)
/*     */     {
/*     */       try {
/* 320 */         this.input.readFully(b);
/*     */       } catch (IOException e) {
/* 322 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public void readFully(byte[] b, int off, int len)
/*     */     {
/*     */       try {
/* 329 */         this.input.readFully(b, off, len);
/*     */       } catch (IOException e) {
/* 331 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public int skipBytes(int n)
/*     */     {
/*     */       try {
/* 338 */         return this.input.skipBytes(n);
/*     */       } catch (IOException e) {
/* 340 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean readBoolean()
/*     */     {
/*     */       try {
/* 347 */         return this.input.readBoolean();
/*     */       } catch (IOException e) {
/* 349 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public byte readByte()
/*     */     {
/*     */       try {
/* 356 */         return this.input.readByte();
/*     */       } catch (EOFException e) {
/* 358 */         throw new IllegalStateException(e);
/*     */       } catch (IOException impossible) {
/* 360 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public int readUnsignedByte()
/*     */     {
/*     */       try {
/* 367 */         return this.input.readUnsignedByte();
/*     */       } catch (IOException e) {
/* 369 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public short readShort()
/*     */     {
/*     */       try {
/* 376 */         return this.input.readShort();
/*     */       } catch (IOException e) {
/* 378 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public int readUnsignedShort()
/*     */     {
/*     */       try {
/* 385 */         return this.input.readUnsignedShort();
/*     */       } catch (IOException e) {
/* 387 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public char readChar()
/*     */     {
/*     */       try {
/* 394 */         return this.input.readChar();
/*     */       } catch (IOException e) {
/* 396 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public int readInt()
/*     */     {
/*     */       try {
/* 403 */         return this.input.readInt();
/*     */       } catch (IOException e) {
/* 405 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public long readLong()
/*     */     {
/*     */       try {
/* 412 */         return this.input.readLong();
/*     */       } catch (IOException e) {
/* 414 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public float readFloat()
/*     */     {
/*     */       try {
/* 421 */         return this.input.readFloat();
/*     */       } catch (IOException e) {
/* 423 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public double readDouble()
/*     */     {
/*     */       try {
/* 430 */         return this.input.readDouble();
/*     */       } catch (IOException e) {
/* 432 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public String readLine()
/*     */     {
/*     */       try {
/* 439 */         return this.input.readLine();
/*     */       } catch (IOException e) {
/* 441 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public String readUTF()
/*     */     {
/*     */       try {
/* 448 */         return this.input.readUTF();
/*     */       } catch (IOException e) {
/* 450 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static ByteArrayDataOutput newDataOutput()
/*     */   {
/* 457 */     return newDataOutput(new ByteArrayOutputStream());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteArrayDataOutput newDataOutput(int size)
/*     */   {
/* 469 */     if (size < 0) {
/* 470 */       throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[] { Integer.valueOf(size) }));
/*     */     }
/* 472 */     return newDataOutput(new ByteArrayOutputStream(size));
/*     */   }
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
/*     */   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputSteam)
/*     */   {
/* 488 */     return new ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputSteam));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataOutputStream implements ByteArrayDataOutput
/*     */   {
/*     */     final DataOutput output;
/*     */     final ByteArrayOutputStream byteArrayOutputSteam;
/*     */     
/*     */     ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam)
/*     */     {
/* 498 */       this.byteArrayOutputSteam = byteArrayOutputSteam;
/* 499 */       this.output = new DataOutputStream(byteArrayOutputSteam);
/*     */     }
/*     */     
/*     */     public void write(int b)
/*     */     {
/*     */       try {
/* 505 */         this.output.write(b);
/*     */       } catch (IOException impossible) {
/* 507 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void write(byte[] b)
/*     */     {
/*     */       try {
/* 514 */         this.output.write(b);
/*     */       } catch (IOException impossible) {
/* 516 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len)
/*     */     {
/*     */       try {
/* 523 */         this.output.write(b, off, len);
/*     */       } catch (IOException impossible) {
/* 525 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeBoolean(boolean v)
/*     */     {
/*     */       try {
/* 532 */         this.output.writeBoolean(v);
/*     */       } catch (IOException impossible) {
/* 534 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeByte(int v)
/*     */     {
/*     */       try {
/* 541 */         this.output.writeByte(v);
/*     */       } catch (IOException impossible) {
/* 543 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeBytes(String s)
/*     */     {
/*     */       try {
/* 550 */         this.output.writeBytes(s);
/*     */       } catch (IOException impossible) {
/* 552 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeChar(int v)
/*     */     {
/*     */       try {
/* 559 */         this.output.writeChar(v);
/*     */       } catch (IOException impossible) {
/* 561 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeChars(String s)
/*     */     {
/*     */       try {
/* 568 */         this.output.writeChars(s);
/*     */       } catch (IOException impossible) {
/* 570 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeDouble(double v)
/*     */     {
/*     */       try {
/* 577 */         this.output.writeDouble(v);
/*     */       } catch (IOException impossible) {
/* 579 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeFloat(float v)
/*     */     {
/*     */       try {
/* 586 */         this.output.writeFloat(v);
/*     */       } catch (IOException impossible) {
/* 588 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeInt(int v)
/*     */     {
/*     */       try {
/* 595 */         this.output.writeInt(v);
/*     */       } catch (IOException impossible) {
/* 597 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeLong(long v)
/*     */     {
/*     */       try {
/* 604 */         this.output.writeLong(v);
/*     */       } catch (IOException impossible) {
/* 606 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeShort(int v)
/*     */     {
/*     */       try {
/* 613 */         this.output.writeShort(v);
/*     */       } catch (IOException impossible) {
/* 615 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeUTF(String s)
/*     */     {
/*     */       try {
/* 622 */         this.output.writeUTF(s);
/*     */       } catch (IOException impossible) {
/* 624 */         throw new AssertionError(impossible);
/*     */       }
/*     */     }
/*     */     
/*     */     public byte[] toByteArray()
/*     */     {
/* 630 */       return this.byteArrayOutputSteam.toByteArray();
/*     */     }
/*     */   }
/*     */   
/* 634 */   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream()
/*     */   {
/*     */     public void write(int b) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void write(byte[] b)
/*     */     {
/* 643 */       Preconditions.checkNotNull(b);
/*     */     }
/*     */     
/*     */ 
/*     */     public void write(byte[] b, int off, int len)
/*     */     {
/* 649 */       Preconditions.checkNotNull(b);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 654 */       return "ByteStreams.nullOutputStream()";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OutputStream nullOutputStream()
/*     */   {
/* 664 */     return NULL_OUTPUT_STREAM;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static InputStream limit(InputStream in, long limit)
/*     */   {
/* 676 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream extends FilterInputStream
/*     */   {
/*     */     private long left;
/* 682 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/* 685 */       super();
/* 686 */       Preconditions.checkNotNull(in);
/* 687 */       Preconditions.checkArgument(limit >= 0L, "limit must be non-negative");
/* 688 */       this.left = limit;
/*     */     }
/*     */     
/*     */     public int available() throws IOException
/*     */     {
/* 693 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void mark(int readLimit)
/*     */     {
/* 699 */       this.in.mark(readLimit);
/* 700 */       this.mark = this.left;
/*     */     }
/*     */     
/*     */     public int read() throws IOException
/*     */     {
/* 705 */       if (this.left == 0L) {
/* 706 */         return -1;
/*     */       }
/*     */       
/* 709 */       int result = this.in.read();
/* 710 */       if (result != -1) {
/* 711 */         this.left -= 1L;
/*     */       }
/* 713 */       return result;
/*     */     }
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException
/*     */     {
/* 718 */       if (this.left == 0L) {
/* 719 */         return -1;
/*     */       }
/*     */       
/* 722 */       len = (int)Math.min(len, this.left);
/* 723 */       int result = this.in.read(b, off, len);
/* 724 */       if (result != -1) {
/* 725 */         this.left -= result;
/*     */       }
/* 727 */       return result;
/*     */     }
/*     */     
/*     */     public synchronized void reset() throws IOException
/*     */     {
/* 732 */       if (!this.in.markSupported()) {
/* 733 */         throw new IOException("Mark not supported");
/*     */       }
/* 735 */       if (this.mark == -1L) {
/* 736 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 739 */       this.in.reset();
/* 740 */       this.left = this.mark;
/*     */     }
/*     */     
/*     */     public long skip(long n) throws IOException
/*     */     {
/* 745 */       n = Math.min(n, this.left);
/* 746 */       long skipped = this.in.skip(n);
/* 747 */       this.left -= skipped;
/* 748 */       return skipped;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void readFully(InputStream in, byte[] b)
/*     */     throws IOException
/*     */   {
/* 762 */     readFully(in, b, 0, b.length);
/*     */   }
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
/*     */   public static void readFully(InputStream in, byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 778 */     int read = read(in, b, off, len);
/* 779 */     if (read != len) {
/* 780 */       throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void skipFully(InputStream in, long n)
/*     */     throws IOException
/*     */   {
/* 795 */     long skipped = skipUpTo(in, n);
/* 796 */     if (skipped < n) {
/* 797 */       throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static long skipUpTo(InputStream in, long n)
/*     */     throws IOException
/*     */   {
/* 808 */     long totalSkipped = 0L;
/* 809 */     byte[] buf = createBuffer();
/*     */     
/* 811 */     while (totalSkipped < n) {
/* 812 */       long remaining = n - totalSkipped;
/* 813 */       long skipped = skipSafely(in, remaining);
/*     */       
/* 815 */       if (skipped == 0L)
/*     */       {
/*     */ 
/* 818 */         int skip = (int)Math.min(remaining, buf.length);
/* 819 */         if ((skipped = in.read(buf, 0, skip)) == -1L) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 825 */       totalSkipped += skipped;
/*     */     }
/*     */     
/* 828 */     return totalSkipped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long skipSafely(InputStream in, long n)
/*     */     throws IOException
/*     */   {
/* 839 */     int available = in.available();
/* 840 */     return available == 0 ? 0L : in.skip(Math.min(available, n));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 854 */     Preconditions.checkNotNull(input);
/* 855 */     Preconditions.checkNotNull(processor);
/*     */     
/* 857 */     byte[] buf = createBuffer();
/*     */     int read;
/*     */     do {
/* 860 */       read = input.read(buf);
/* 861 */     } while ((read != -1) && (processor.processBytes(buf, 0, read)));
/* 862 */     return (T)processor.getResult();
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int read(InputStream in, byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 891 */     Preconditions.checkNotNull(in);
/* 892 */     Preconditions.checkNotNull(b);
/* 893 */     if (len < 0) {
/* 894 */       throw new IndexOutOfBoundsException("len is negative");
/*     */     }
/* 896 */     int total = 0;
/* 897 */     while (total < len) {
/* 898 */       int result = in.read(b, off + total, len - total);
/* 899 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 902 */       total += result;
/*     */     }
/* 904 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\ByteStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */