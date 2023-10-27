/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
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
/*     */ public abstract class LZ4SafeDecompressor
/*     */   implements LZ4UnknownSizeDecompressor
/*     */ {
/*     */   public abstract int decompress(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4);
/*     */   
/*     */   public abstract int decompress(ByteBuffer paramByteBuffer1, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer2, int paramInt3, int paramInt4);
/*     */   
/*     */   public final int decompress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff)
/*     */   {
/*  74 */     return decompress(src, srcOff, srcLen, dest, destOff, dest.length - destOff);
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
/*     */   public final int decompress(byte[] src, byte[] dest)
/*     */   {
/*  87 */     return decompress(src, 0, src.length, dest, 0);
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
/*     */   public final byte[] decompress(byte[] src, int srcOff, int srcLen, int maxDestLen)
/*     */   {
/* 115 */     byte[] decompressed = new byte[maxDestLen];
/* 116 */     int decompressedLength = decompress(src, srcOff, srcLen, decompressed, 0, maxDestLen);
/* 117 */     if (decompressedLength != decompressed.length) {
/* 118 */       decompressed = Arrays.copyOf(decompressed, decompressedLength);
/*     */     }
/* 120 */     return decompressed;
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
/*     */   public final byte[] decompress(byte[] src, int maxDestLen)
/*     */   {
/* 133 */     return decompress(src, 0, src.length, maxDestLen);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void decompress(ByteBuffer src, ByteBuffer dest)
/*     */   {
/* 145 */     int decompressed = decompress(src, src.position(), src.remaining(), dest, dest.position(), dest.remaining());
/* 146 */     src.position(src.limit());
/* 147 */     dest.position(dest.position() + decompressed);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 152 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4SafeDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */