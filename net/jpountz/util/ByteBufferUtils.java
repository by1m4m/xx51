/*    */ package net.jpountz.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public enum ByteBufferUtils
/*    */ {
/*    */   private ByteBufferUtils() {}
/*    */   
/*    */   public static void checkRange(ByteBuffer buf, int off, int len)
/*    */   {
/* 11 */     SafeUtils.checkLength(len);
/* 12 */     if (len > 0) {
/* 13 */       checkRange(buf, off);
/* 14 */       checkRange(buf, off + len - 1);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void checkRange(ByteBuffer buf, int off) {
/* 19 */     if ((off < 0) || (off >= buf.capacity())) {
/* 20 */       throw new ArrayIndexOutOfBoundsException(off);
/*    */     }
/*    */   }
/*    */   
/*    */   public static ByteBuffer inLittleEndianOrder(ByteBuffer buf) {
/* 25 */     if (buf.order().equals(java.nio.ByteOrder.LITTLE_ENDIAN)) {
/* 26 */       return buf;
/*    */     }
/* 28 */     return buf.duplicate().order(java.nio.ByteOrder.LITTLE_ENDIAN);
/*    */   }
/*    */   
/*    */   public static ByteBuffer inNativeByteOrder(ByteBuffer buf)
/*    */   {
/* 33 */     if (buf.order().equals(Utils.NATIVE_BYTE_ORDER)) {
/* 34 */       return buf;
/*    */     }
/* 36 */     return buf.duplicate().order(Utils.NATIVE_BYTE_ORDER);
/*    */   }
/*    */   
/*    */   public static byte readByte(ByteBuffer buf, int i)
/*    */   {
/* 41 */     return buf.get(i);
/*    */   }
/*    */   
/*    */   public static void writeInt(ByteBuffer buf, int i, int v) {
/* 45 */     assert (buf.order() == Utils.NATIVE_BYTE_ORDER);
/* 46 */     buf.putInt(i, v);
/*    */   }
/*    */   
/*    */   public static int readInt(ByteBuffer buf, int i) {
/* 50 */     assert (buf.order() == Utils.NATIVE_BYTE_ORDER);
/* 51 */     return buf.getInt(i);
/*    */   }
/*    */   
/*    */   public static int readIntLE(ByteBuffer buf, int i) {
/* 55 */     assert (buf.order() == java.nio.ByteOrder.LITTLE_ENDIAN);
/* 56 */     return buf.getInt(i);
/*    */   }
/*    */   
/*    */   public static void writeLong(ByteBuffer buf, int i, long v) {
/* 60 */     assert (buf.order() == Utils.NATIVE_BYTE_ORDER);
/* 61 */     buf.putLong(i, v);
/*    */   }
/*    */   
/*    */   public static long readLong(ByteBuffer buf, int i) {
/* 65 */     assert (buf.order() == Utils.NATIVE_BYTE_ORDER);
/* 66 */     return buf.getLong(i);
/*    */   }
/*    */   
/*    */   public static long readLongLE(ByteBuffer buf, int i) {
/* 70 */     assert (buf.order() == java.nio.ByteOrder.LITTLE_ENDIAN);
/* 71 */     return buf.getLong(i);
/*    */   }
/*    */   
/*    */   public static void writeByte(ByteBuffer dest, int off, int i) {
/* 75 */     dest.put(off, (byte)i);
/*    */   }
/*    */   
/*    */   public static void writeShortLE(ByteBuffer dest, int off, int i) {
/* 79 */     dest.put(off, (byte)i);
/* 80 */     dest.put(off + 1, (byte)(i >>> 8));
/*    */   }
/*    */   
/*    */   public static void checkNotReadOnly(ByteBuffer buffer) {
/* 84 */     if (buffer.isReadOnly()) {
/* 85 */       throw new java.nio.ReadOnlyBufferException();
/*    */     }
/*    */   }
/*    */   
/*    */   public static int readShortLE(ByteBuffer buf, int i) {
/* 90 */     return buf.get(i) & 0xFF | (buf.get(i + 1) & 0xFF) << 8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\util\ByteBufferUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */