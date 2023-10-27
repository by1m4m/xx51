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
/*    */ final class LZ4JNIFastDecompressor
/*    */   extends LZ4FastDecompressor
/*    */ {
/* 30 */   public static final LZ4JNIFastDecompressor INSTANCE = new LZ4JNIFastDecompressor();
/*    */   private static LZ4FastDecompressor SAFE_INSTANCE;
/*    */   
/*    */   public final int decompress(byte[] src, int srcOff, byte[] dest, int destOff, int destLen)
/*    */   {
/* 35 */     SafeUtils.checkRange(src, srcOff);
/* 36 */     SafeUtils.checkRange(dest, destOff, destLen);
/* 37 */     int result = LZ4JNI.LZ4_decompress_fast(src, null, srcOff, dest, null, destOff, destLen);
/* 38 */     if (result < 0) {
/* 39 */       throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */   
/*    */   public int decompress(ByteBuffer src, int srcOff, ByteBuffer dest, int destOff, int destLen)
/*    */   {
/* 46 */     ByteBufferUtils.checkNotReadOnly(dest);
/* 47 */     ByteBufferUtils.checkRange(src, srcOff);
/* 48 */     ByteBufferUtils.checkRange(dest, destOff, destLen);
/*    */     
/* 50 */     if (((src.hasArray()) || (src.isDirect())) && ((dest.hasArray()) || (dest.isDirect()))) {
/* 51 */       byte[] srcArr = null;byte[] destArr = null;
/* 52 */       ByteBuffer srcBuf = null;ByteBuffer destBuf = null;
/* 53 */       if (src.hasArray()) {
/* 54 */         srcArr = src.array();
/* 55 */         srcOff += src.arrayOffset();
/*    */       } else {
/* 57 */         assert (src.isDirect());
/* 58 */         srcBuf = src;
/*    */       }
/* 60 */       if (dest.hasArray()) {
/* 61 */         destArr = dest.array();
/* 62 */         destOff += dest.arrayOffset();
/*    */       } else {
/* 64 */         assert (dest.isDirect());
/* 65 */         destBuf = dest;
/*    */       }
/*    */       
/* 68 */       int result = LZ4JNI.LZ4_decompress_fast(srcArr, srcBuf, srcOff, destArr, destBuf, destOff, destLen);
/* 69 */       if (result < 0) {
/* 70 */         throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
/*    */       }
/* 72 */       return result;
/*    */     }
/* 74 */     LZ4FastDecompressor safeInstance = SAFE_INSTANCE;
/* 75 */     if (safeInstance == null) {
/* 76 */       safeInstance = SAFE_INSTANCE = LZ4Factory.safeInstance().fastDecompressor();
/*    */     }
/* 78 */     return safeInstance.decompress(src, srcOff, dest, destOff, destLen);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JNIFastDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */