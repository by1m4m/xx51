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
/*    */ 
/*    */ final class LZ4HCJNICompressor
/*    */   extends LZ4Compressor
/*    */ {
/* 30 */   public static final LZ4HCJNICompressor INSTANCE = new LZ4HCJNICompressor();
/*    */   
/*    */   private static LZ4Compressor SAFE_INSTANCE;
/*    */   private final int compressionLevel;
/*    */   
/* 35 */   LZ4HCJNICompressor() { this(9); }
/*    */   
/* 37 */   LZ4HCJNICompressor(int compressionLevel) { this.compressionLevel = compressionLevel; }
/*    */   
/*    */ 
/*    */   public int compress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int maxDestLen)
/*    */   {
/* 42 */     SafeUtils.checkRange(src, srcOff, srcLen);
/* 43 */     SafeUtils.checkRange(dest, destOff, maxDestLen);
/* 44 */     int result = LZ4JNI.LZ4_compressHC(src, null, srcOff, srcLen, dest, null, destOff, maxDestLen, this.compressionLevel);
/* 45 */     if (result <= 0) {
/* 46 */       throw new LZ4Exception();
/*    */     }
/* 48 */     return result;
/*    */   }
/*    */   
/*    */   public int compress(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int maxDestLen)
/*    */   {
/* 53 */     ByteBufferUtils.checkNotReadOnly(dest);
/* 54 */     ByteBufferUtils.checkRange(src, srcOff, srcLen);
/* 55 */     ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
/*    */     
/* 57 */     if (((src.hasArray()) || (src.isDirect())) && ((dest.hasArray()) || (dest.isDirect()))) {
/* 58 */       byte[] srcArr = null;byte[] destArr = null;
/* 59 */       ByteBuffer srcBuf = null;ByteBuffer destBuf = null;
/* 60 */       if (src.hasArray()) {
/* 61 */         srcArr = src.array();
/* 62 */         srcOff += src.arrayOffset();
/*    */       } else {
/* 64 */         assert (src.isDirect());
/* 65 */         srcBuf = src;
/*    */       }
/* 67 */       if (dest.hasArray()) {
/* 68 */         destArr = dest.array();
/* 69 */         destOff += dest.arrayOffset();
/*    */       } else {
/* 71 */         assert (dest.isDirect());
/* 72 */         destBuf = dest;
/*    */       }
/*    */       
/* 75 */       int result = LZ4JNI.LZ4_compressHC(srcArr, srcBuf, srcOff, srcLen, destArr, destBuf, destOff, maxDestLen, this.compressionLevel);
/* 76 */       if (result <= 0) {
/* 77 */         throw new LZ4Exception();
/*    */       }
/* 79 */       return result;
/*    */     }
/* 81 */     LZ4Compressor safeInstance = SAFE_INSTANCE;
/* 82 */     if (safeInstance == null) {
/* 83 */       safeInstance = SAFE_INSTANCE = LZ4Factory.safeInstance().highCompressor(this.compressionLevel);
/*    */     }
/* 85 */     return safeInstance.compress(src, srcOff, srcLen, dest, destOff, maxDestLen);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4HCJNICompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */