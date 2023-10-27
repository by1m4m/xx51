/*    */ package net.jpountz.lz4;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import net.jpountz.util.Native;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */  enum LZ4JNI
/*    */ {
/*    */   private LZ4JNI() {}
/*    */   
/*    */   static native void init();
/*    */   
/*    */   static native int LZ4_compress_limitedOutput(byte[] paramArrayOfByte1, ByteBuffer paramByteBuffer1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, ByteBuffer paramByteBuffer2, int paramInt3, int paramInt4);
/*    */   
/*    */   static native int LZ4_compressHC(byte[] paramArrayOfByte1, ByteBuffer paramByteBuffer1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, ByteBuffer paramByteBuffer2, int paramInt3, int paramInt4, int paramInt5);
/*    */   
/*    */   static native int LZ4_decompress_fast(byte[] paramArrayOfByte1, ByteBuffer paramByteBuffer1, int paramInt1, byte[] paramArrayOfByte2, ByteBuffer paramByteBuffer2, int paramInt2, int paramInt3);
/*    */   
/*    */   static native int LZ4_decompress_safe(byte[] paramArrayOfByte1, ByteBuffer paramByteBuffer1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, ByteBuffer paramByteBuffer2, int paramInt3, int paramInt4);
/*    */   
/*    */   static native int LZ4_compressBound(int paramInt);
/*    */   
/*    */   static
/*    */   {
/* 29 */     Native.load();
/* 30 */     init();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */