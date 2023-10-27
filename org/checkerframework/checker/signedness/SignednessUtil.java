/*     */ package org.checkerframework.checker.signedness;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SignednessUtil
/*     */ {
/*     */   private SignednessUtil()
/*     */   {
/*  20 */     throw new Error("Do not instantiate");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer wrapUnsigned(byte[] array)
/*     */   {
/*  30 */     return ByteBuffer.wrap(array);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer wrapUnsigned(byte[] array, int offset, int length)
/*     */   {
/*  40 */     return ByteBuffer.wrap(array, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getUnsignedInt(ByteBuffer b)
/*     */   {
/*  50 */     return b.getInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short getUnsignedShort(ByteBuffer b)
/*     */   {
/*  60 */     return b.getShort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte getUnsigned(ByteBuffer b)
/*     */   {
/*  70 */     return b.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte getUnsigned(ByteBuffer b, int i)
/*     */   {
/*  80 */     return b.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer getUnsigned(ByteBuffer b, byte[] bs, int i, int l)
/*     */   {
/*  90 */     return b.get(bs, i, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsigned(ByteBuffer b, byte ubyte)
/*     */   {
/* 100 */     return b.put(ubyte);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsigned(ByteBuffer b, int i, byte ubyte)
/*     */   {
/* 110 */     return b.put(i, ubyte);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int uint)
/*     */   {
/* 120 */     return b.put(uint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int i, int uint)
/*     */   {
/* 130 */     return b.put(i, uint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int[] uints)
/*     */   {
/* 140 */     return b.put(uints);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntBuffer putUnsigned(IntBuffer b, int[] uints, int i, int l)
/*     */   {
/* 150 */     return b.put(uints, i, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getUnsigned(IntBuffer b, int i)
/*     */   {
/* 160 */     return b.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsignedShort(ByteBuffer b, short ushort)
/*     */   {
/* 170 */     return b.putShort(ushort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsignedShort(ByteBuffer b, int i, short ushort)
/*     */   {
/* 180 */     return b.putShort(i, ushort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsignedInt(ByteBuffer b, int uint)
/*     */   {
/* 190 */     return b.putInt(uint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsignedInt(ByteBuffer b, int i, int uint)
/*     */   {
/* 200 */     return b.putInt(i, uint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuffer putUnsignedLong(ByteBuffer b, int i, long ulong)
/*     */   {
/* 210 */     return b.putLong(i, ulong);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte readUnsignedByte(RandomAccessFile f)
/*     */     throws IOException
/*     */   {
/* 220 */     return f.readByte();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char readUnsignedChar(RandomAccessFile f)
/*     */     throws IOException
/*     */   {
/* 230 */     return f.readChar();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static short readUnsignedShort(RandomAccessFile f)
/*     */     throws IOException
/*     */   {
/* 240 */     return f.readShort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int readUnsignedInt(RandomAccessFile f)
/*     */     throws IOException
/*     */   {
/* 250 */     return f.readInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long readUnsignedLong(RandomAccessFile f)
/*     */     throws IOException
/*     */   {
/* 260 */     return f.readLong();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int readUnsigned(RandomAccessFile f, byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 271 */     return f.read(b, off, len);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void readFullyUnsigned(RandomAccessFile f, byte[] b)
/*     */     throws IOException
/*     */   {
/* 282 */     f.readFully(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsigned(RandomAccessFile f, byte[] bs, int off, int len)
/*     */     throws IOException
/*     */   {
/* 293 */     f.write(bs, off, len);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedByte(RandomAccessFile f, byte b)
/*     */     throws IOException
/*     */   {
/* 303 */     f.writeByte(toUnsignedInt(b));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedChar(RandomAccessFile f, char c)
/*     */     throws IOException
/*     */   {
/* 313 */     f.writeChar(toUnsignedInt(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedShort(RandomAccessFile f, short s)
/*     */     throws IOException
/*     */   {
/* 324 */     f.writeShort(toUnsignedInt(s));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedInt(RandomAccessFile f, int i)
/*     */     throws IOException
/*     */   {
/* 334 */     f.writeInt(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedLong(RandomAccessFile f, long l)
/*     */     throws IOException
/*     */   {
/* 344 */     f.writeLong(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void getUnsigned(ByteBuffer b, byte[] bs)
/*     */   {
/* 354 */     b.get(bs);
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
/*     */   public static int compareUnsigned(long x, long y)
/*     */   {
/* 368 */     return Long.compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
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
/*     */   public static int compareUnsigned(int x, int y)
/*     */   {
/* 382 */     return Integer.compare(x + Integer.MIN_VALUE, y + Integer.MIN_VALUE);
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
/*     */   public static int compareUnsigned(short x, short y)
/*     */   {
/* 395 */     return compareUnsigned(toUnsignedInt(x), toUnsignedInt(y));
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
/*     */   public static int compareUnsigned(byte x, byte y)
/*     */   {
/* 408 */     return compareUnsigned(toUnsignedInt(x), toUnsignedInt(y));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(long l)
/*     */   {
/* 419 */     return toUnsignedBigInteger(l).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(long l, int radix)
/*     */   {
/* 430 */     return toUnsignedBigInteger(l).toString(radix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(int i)
/*     */   {
/* 441 */     return Long.toString(toUnsignedLong(i));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(int i, int radix)
/*     */   {
/* 452 */     return Long.toString(toUnsignedLong(i), radix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(short s)
/*     */   {
/* 459 */     return Long.toString(toUnsignedLong(s));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(short s, int radix)
/*     */   {
/* 466 */     return Long.toString(toUnsignedLong(s), radix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(byte b)
/*     */   {
/* 473 */     return Long.toString(toUnsignedLong(b));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toUnsignedString(byte b, int radix)
/*     */   {
/* 480 */     return Long.toString(toUnsignedLong(b), radix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static BigInteger toUnsignedBigInteger(long l)
/*     */   {
/* 492 */     if (l >= 0L) {
/* 493 */       return BigInteger.valueOf(l);
/*     */     }
/* 495 */     int upper = (int)(l >>> 32);
/* 496 */     int lower = (int)l;
/*     */     
/*     */ 
/* 499 */     return BigInteger.valueOf(toUnsignedLong(upper))
/* 500 */       .shiftLeft(32)
/* 501 */       .add(BigInteger.valueOf(toUnsignedLong(lower)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long toUnsignedLong(int i)
/*     */   {
/* 512 */     return i & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public static long toUnsignedLong(short s)
/*     */   {
/* 517 */     return s & 0xFFFF;
/*     */   }
/*     */   
/*     */   public static int toUnsignedInt(short s)
/*     */   {
/* 522 */     return s & 0xFFFF;
/*     */   }
/*     */   
/*     */   public static long toUnsignedLong(byte b)
/*     */   {
/* 527 */     return b & 0xFF;
/*     */   }
/*     */   
/*     */   public static int toUnsignedInt(byte b)
/*     */   {
/* 532 */     return b & 0xFF;
/*     */   }
/*     */   
/*     */   public static short toUnsignedShort(byte b)
/*     */   {
/* 537 */     return (short)(b & 0xFF);
/*     */   }
/*     */   
/*     */   public static long toUnsignedLong(char c)
/*     */   {
/* 542 */     return c & 0xFF;
/*     */   }
/*     */   
/*     */   public static int toUnsignedInt(char c)
/*     */   {
/* 547 */     return c & 0xFF;
/*     */   }
/*     */   
/*     */   public static short toUnsignedShort(char c)
/*     */   {
/* 552 */     return (short)(c & 0xFF);
/*     */   }
/*     */   
/*     */   public static float toFloat(byte b)
/*     */   {
/* 557 */     return toUnsignedBigInteger(toUnsignedLong(b)).floatValue();
/*     */   }
/*     */   
/*     */   public static float toFloat(short s)
/*     */   {
/* 562 */     return toUnsignedBigInteger(toUnsignedLong(s)).floatValue();
/*     */   }
/*     */   
/*     */   public static float toFloat(int i)
/*     */   {
/* 567 */     return toUnsignedBigInteger(toUnsignedLong(i)).floatValue();
/*     */   }
/*     */   
/*     */   public static float toFloat(long l)
/*     */   {
/* 572 */     return toUnsignedBigInteger(l).floatValue();
/*     */   }
/*     */   
/*     */   public static double toDouble(byte b)
/*     */   {
/* 577 */     return toUnsignedBigInteger(toUnsignedLong(b)).doubleValue();
/*     */   }
/*     */   
/*     */   public static double toDouble(short s)
/*     */   {
/* 582 */     return toUnsignedBigInteger(toUnsignedLong(s)).doubleValue();
/*     */   }
/*     */   
/*     */   public static double toDouble(int i)
/*     */   {
/* 587 */     return toUnsignedBigInteger(toUnsignedLong(i)).doubleValue();
/*     */   }
/*     */   
/*     */   public static double toDouble(long l)
/*     */   {
/* 592 */     return toUnsignedBigInteger(l).doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte byteFromFloat(float f)
/*     */   {
/* 598 */     assert (f >= 0.0F);
/* 599 */     return (byte)(int)f;
/*     */   }
/*     */   
/*     */ 
/*     */   public static short shortFromFloat(float f)
/*     */   {
/* 605 */     assert (f >= 0.0F);
/* 606 */     return (short)(int)f;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int intFromFloat(float f)
/*     */   {
/* 612 */     assert (f >= 0.0F);
/* 613 */     return (int)f;
/*     */   }
/*     */   
/*     */ 
/*     */   public static long longFromFloat(float f)
/*     */   {
/* 619 */     assert (f >= 0.0F);
/* 620 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte byteFromDouble(double d)
/*     */   {
/* 626 */     assert (d >= 0.0D);
/* 627 */     return (byte)(int)d;
/*     */   }
/*     */   
/*     */ 
/*     */   public static short shortFromDouble(double d)
/*     */   {
/* 633 */     assert (d >= 0.0D);
/* 634 */     return (short)(int)d;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int intFromDouble(double d)
/*     */   {
/* 640 */     assert (d >= 0.0D);
/* 641 */     return (int)d;
/*     */   }
/*     */   
/*     */ 
/*     */   public static long longFromDouble(double d)
/*     */   {
/* 647 */     assert (d >= 0.0D);
/* 648 */     return d;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signedness\SignednessUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */