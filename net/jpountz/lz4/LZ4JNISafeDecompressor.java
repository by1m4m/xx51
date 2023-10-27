/*    */ package net.jpountz.lz4;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import net.jpountz.util.ByteBufferUtils;
/*    */ import net.jpountz.util.SafeUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LZ4JNISafeDecompressor
/*    */   extends LZ4SafeDecompressor
/*    */ {
/* 29 */   public static final LZ4JNISafeDecompressor INSTANCE = new LZ4JNISafeDecompressor();
/*    */   private static LZ4SafeDecompressor SAFE_INSTANCE;
/*    */   
/*    */   public final int decompress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int maxDestLen)
/*    */   {
/* 34 */     SafeUtils.checkRange(src, srcOff, srcLen);
/* 35 */     SafeUtils.checkRange(dest, destOff, maxDestLen);
/* 36 */     int result = LZ4JNI.LZ4_decompress_safe(src, null, srcOff, srcLen, dest, null, destOff, maxDestLen);
/* 37 */     if (result < 0) {
/* 38 */       throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
/*    */     }
/* 40 */     return result;
/*    */   }
/*    */   
/*    */   public int decompress(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int maxDestLen)
/*    */   {
/* 45 */     ByteBufferUtils.checkNotReadOnly(dest);
/* 46 */     ByteBufferUtils.checkRange(src, srcOff, srcLen);
/* 47 */     ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
/*    */     
/* 49 */     if (((src.hasArray()) || (src.isDirect())) && ((dest.hasArray()) || (dest.isDirect()))) {
/* 50 */       byte[] srcArr = null;byte[] destArr = null;
/* 51 */       ByteBuffer srcBuf = null;ByteBuffer destBuf = null;
/* 52 */       if (src.hasArray()) {
/* 53 */         srcArr = src.array();
/* 54 */         srcOff += src.arrayOffset();
/*    */       } else {
/* 56 */         assert (src.isDirect());
/* 57 */         srcBuf = src;
/*    */       }
/* 59 */       if (dest.hasArray()) {
/* 60 */         destArr = dest.array();
/* 61 */         destOff += dest.arrayOffset();
/*    */       } else {
/* 63 */         assert (dest.isDirect());
/* 64 */         destBuf = dest;
/*    */       }
/*    */       
/* 67 */       int result = LZ4JNI.LZ4_decompress_safe(srcArr, srcBuf, srcOff, srcLen, destArr, destBuf, destOff, maxDestLen);
/* 68 */       if (result < 0) {
/* 69 */         throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
/*    */       }
/* 71 */       return result;
/*    */     }
/* 73 */     LZ4SafeDecompressor safeInstance = SAFE_INSTANCE;
/* 74 */     if (safeInstance == null) {
/* 75 */       safeInstance = SAFE_INSTANCE = LZ4Factory.safeInstance().safeDecompressor();
/*    */     }
/* 77 */     return safeInstance.decompress(src, srcOff, srcLen, dest, destOff, maxDestLen);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JNISafeDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */