/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */  enum XXHashJNI { private XXHashJNI() {}
/*    */   
/*    */   private static native void init();
/*    */   
/*    */   static native int XXH32(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   static native int XXH32BB(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   static native long XXH32_init(int paramInt);
/*    */   
/*    */   static native void XXH32_update(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*    */   
/*    */   static native int XXH32_digest(long paramLong);
/*    */   static native void XXH32_free(long paramLong);
/*    */   static native long XXH64(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong);
/*    */   static native long XXH64BB(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong);
/*    */   static native long XXH64_init(long paramLong);
/*    */   static native void XXH64_update(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*    */   static native long XXH64_digest(long paramLong);
/*    */   static native void XXH64_free(long paramLong);
/* 25 */   static { net.jpountz.util.Native.load();
/* 26 */     init();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHashJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */