/*    */ package net.jpountz.util;
/*    */ 
/*    */ import java.nio.ByteOrder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SafeUtils
/*    */ {
/*    */   private SafeUtils() {}
/*    */   
/*    */   public static void checkRange(byte[] buf, int off)
/*    */   {
/* 23 */     if ((off < 0) || (off >= buf.length)) {
/* 24 */       throw new ArrayIndexOutOfBoundsException(off);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void checkRange(byte[] buf, int off, int len) {
/* 29 */     checkLength(len);
/* 30 */     if (len > 0) {
/* 31 */       checkRange(buf, off);
/* 32 */       checkRange(buf, off + len - 1);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void checkLength(int len) {
/* 37 */     if (len < 0) {
/* 38 */       throw new IllegalArgumentException("lengths must be >= 0");
/*    */     }
/*    */   }
/*    */   
/*    */   public static byte readByte(byte[] buf, int i) {
/* 43 */     return buf[i];
/*    */   }
/*    */   
/*    */   public static int readIntBE(byte[] buf, int i) {
/* 47 */     return (buf[i] & 0xFF) << 24 | (buf[(i + 1)] & 0xFF) << 16 | (buf[(i + 2)] & 0xFF) << 8 | buf[(i + 3)] & 0xFF;
/*    */   }
/*    */   
/*    */   public static int readIntLE(byte[] buf, int i) {
/* 51 */     return buf[i] & 0xFF | (buf[(i + 1)] & 0xFF) << 8 | (buf[(i + 2)] & 0xFF) << 16 | (buf[(i + 3)] & 0xFF) << 24;
/*    */   }
/*    */   
/*    */   public static int readInt(byte[] buf, int i) {
/* 55 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 56 */       return readIntBE(buf, i);
/*    */     }
/* 58 */     return readIntLE(buf, i);
/*    */   }
/*    */   
/*    */   public static long readLongLE(byte[] buf, int i)
/*    */   {
/* 63 */     return buf[i] & 0xFF | (buf[(i + 1)] & 0xFF) << 8 | (buf[(i + 2)] & 0xFF) << 16 | (buf[(i + 3)] & 0xFF) << 24 | (buf[(i + 4)] & 0xFF) << 32 | (buf[(i + 5)] & 0xFF) << 40 | (buf[(i + 6)] & 0xFF) << 48 | (buf[(i + 7)] & 0xFF) << 56;
/*    */   }
/*    */   
/*    */   public static void writeShortLE(byte[] buf, int off, int v)
/*    */   {
/* 68 */     buf[(off++)] = ((byte)v);
/* 69 */     buf[(off++)] = ((byte)(v >>> 8));
/*    */   }
/*    */   
/*    */   public static void writeInt(int[] buf, int off, int v) {
/* 73 */     buf[off] = v;
/*    */   }
/*    */   
/*    */   public static int readInt(int[] buf, int off) {
/* 77 */     return buf[off];
/*    */   }
/*    */   
/*    */   public static void writeByte(byte[] dest, int off, int i) {
/* 81 */     dest[off] = ((byte)i);
/*    */   }
/*    */   
/*    */   public static void writeShort(short[] buf, int off, int v) {
/* 85 */     buf[off] = ((short)v);
/*    */   }
/*    */   
/*    */   public static int readShortLE(byte[] buf, int i) {
/* 89 */     return buf[i] & 0xFF | (buf[(i + 1)] & 0xFF) << 8;
/*    */   }
/*    */   
/*    */   public static int readShort(short[] buf, int off) {
/* 93 */     return buf[off] & 0xFFFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\util\SafeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */