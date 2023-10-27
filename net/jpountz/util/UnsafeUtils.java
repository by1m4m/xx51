/*     */ package net.jpountz.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import sun.misc.Unsafe;
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
/*     */ public enum UnsafeUtils
/*     */ {
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long BYTE_ARRAY_OFFSET;
/*     */   private static final int BYTE_ARRAY_SCALE;
/*     */   private static final long INT_ARRAY_OFFSET;
/*     */   private static final int INT_ARRAY_SCALE;
/*     */   private static final long SHORT_ARRAY_OFFSET;
/*     */   private static final int SHORT_ARRAY_SCALE;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  37 */       Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
/*  38 */       theUnsafe.setAccessible(true);
/*  39 */       UNSAFE = (Unsafe)theUnsafe.get(null);
/*  40 */       BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
/*  41 */       BYTE_ARRAY_SCALE = UNSAFE.arrayIndexScale(byte[].class);
/*  42 */       INT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
/*  43 */       INT_ARRAY_SCALE = UNSAFE.arrayIndexScale(int[].class);
/*  44 */       SHORT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(short[].class);
/*  45 */       SHORT_ARRAY_SCALE = UNSAFE.arrayIndexScale(short[].class);
/*     */     } catch (IllegalAccessException e) {
/*  47 */       throw new ExceptionInInitializerError("Cannot access Unsafe");
/*     */     } catch (NoSuchFieldException e) {
/*  49 */       throw new ExceptionInInitializerError("Cannot access Unsafe");
/*     */     } catch (SecurityException e) {
/*  51 */       throw new ExceptionInInitializerError("Cannot access Unsafe");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void checkRange(byte[] buf, int off) {
/*  56 */     SafeUtils.checkRange(buf, off);
/*     */   }
/*     */   
/*     */   public static void checkRange(byte[] buf, int off, int len) {
/*  60 */     SafeUtils.checkRange(buf, off, len);
/*     */   }
/*     */   
/*     */   public static void checkLength(int len) {
/*  64 */     SafeUtils.checkLength(len);
/*     */   }
/*     */   
/*     */   public static byte readByte(byte[] src, int srcOff) {
/*  68 */     return UNSAFE.getByte(src, BYTE_ARRAY_OFFSET + BYTE_ARRAY_SCALE * srcOff);
/*     */   }
/*     */   
/*     */   public static void writeByte(byte[] src, int srcOff, byte value) {
/*  72 */     UNSAFE.putByte(src, BYTE_ARRAY_OFFSET + BYTE_ARRAY_SCALE * srcOff, value);
/*     */   }
/*     */   
/*     */   public static void writeByte(byte[] src, int srcOff, int value) {
/*  76 */     writeByte(src, srcOff, (byte)value);
/*     */   }
/*     */   
/*     */   public static long readLong(byte[] src, int srcOff) {
/*  80 */     return UNSAFE.getLong(src, BYTE_ARRAY_OFFSET + srcOff);
/*     */   }
/*     */   
/*     */   public static long readLongLE(byte[] src, int srcOff) {
/*  84 */     long i = readLong(src, srcOff);
/*  85 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/*  86 */       i = Long.reverseBytes(i);
/*     */     }
/*  88 */     return i;
/*     */   }
/*     */   
/*     */   public static void writeLong(byte[] dest, int destOff, long value) {
/*  92 */     UNSAFE.putLong(dest, BYTE_ARRAY_OFFSET + destOff, value);
/*     */   }
/*     */   
/*     */   public static int readInt(byte[] src, int srcOff) {
/*  96 */     return UNSAFE.getInt(src, BYTE_ARRAY_OFFSET + srcOff);
/*     */   }
/*     */   
/*     */   public static int readIntLE(byte[] src, int srcOff) {
/* 100 */     int i = readInt(src, srcOff);
/* 101 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 102 */       i = Integer.reverseBytes(i);
/*     */     }
/* 104 */     return i;
/*     */   }
/*     */   
/*     */   public static void writeInt(byte[] dest, int destOff, int value) {
/* 108 */     UNSAFE.putInt(dest, BYTE_ARRAY_OFFSET + destOff, value);
/*     */   }
/*     */   
/*     */   public static short readShort(byte[] src, int srcOff) {
/* 112 */     return UNSAFE.getShort(src, BYTE_ARRAY_OFFSET + srcOff);
/*     */   }
/*     */   
/*     */   public static int readShortLE(byte[] src, int srcOff) {
/* 116 */     short s = readShort(src, srcOff);
/* 117 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 118 */       s = Short.reverseBytes(s);
/*     */     }
/* 120 */     return s & 0xFFFF;
/*     */   }
/*     */   
/*     */   public static void writeShort(byte[] dest, int destOff, short value) {
/* 124 */     UNSAFE.putShort(dest, BYTE_ARRAY_OFFSET + destOff, value);
/*     */   }
/*     */   
/*     */   public static void writeShortLE(byte[] buf, int off, int v) {
/* 128 */     writeByte(buf, off, (byte)v);
/* 129 */     writeByte(buf, off + 1, (byte)(v >>> 8));
/*     */   }
/*     */   
/*     */   public static int readInt(int[] src, int srcOff) {
/* 133 */     return UNSAFE.getInt(src, INT_ARRAY_OFFSET + INT_ARRAY_SCALE * srcOff);
/*     */   }
/*     */   
/*     */   public static void writeInt(int[] dest, int destOff, int value) {
/* 137 */     UNSAFE.putInt(dest, INT_ARRAY_OFFSET + INT_ARRAY_SCALE * destOff, value);
/*     */   }
/*     */   
/*     */   public static int readShort(short[] src, int srcOff) {
/* 141 */     return UNSAFE.getShort(src, SHORT_ARRAY_OFFSET + SHORT_ARRAY_SCALE * srcOff) & 0xFFFF;
/*     */   }
/*     */   
/*     */   public static void writeShort(short[] dest, int destOff, int value) {
/* 145 */     UNSAFE.putShort(dest, SHORT_ARRAY_OFFSET + SHORT_ARRAY_SCALE * destOff, (short)value);
/*     */   }
/*     */   
/*     */   private UnsafeUtils() {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\util\UnsafeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */