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
/*     */ public abstract class LZ4Compressor
/*     */ {
/*     */   public final int maxCompressedLength(int length)
/*     */   {
/*  35 */     return LZ4Utils.maxCompressedLength(length);
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
/*     */   public abstract int compress(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4);
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
/*     */   public abstract int compress(ByteBuffer paramByteBuffer1, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer2, int paramInt3, int paramInt4);
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
/*     */   public final int compress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff)
/*     */   {
/*  95 */     return compress(src, srcOff, srcLen, dest, destOff, dest.length - destOff);
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
/*     */   public final int compress(byte[] src, byte[] dest)
/*     */   {
/* 108 */     return compress(src, 0, src.length, dest, 0);
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
/*     */   public final byte[] compress(byte[] src, int srcOff, int srcLen)
/*     */   {
/* 132 */     int maxCompressedLength = maxCompressedLength(srcLen);
/* 133 */     byte[] compressed = new byte[maxCompressedLength];
/* 134 */     int compressedLength = compress(src, srcOff, srcLen, compressed, 0);
/* 135 */     return Arrays.copyOf(compressed, compressedLength);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte[] compress(byte[] src)
/*     */   {
/* 146 */     return compress(src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void compress(ByteBuffer src, ByteBuffer dest)
/*     */   {
/* 158 */     int cpLen = compress(src, src.position(), src.remaining(), dest, dest.position(), dest.remaining());
/* 159 */     src.position(src.limit());
/* 160 */     dest.position(dest.position() + cpLen);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 165 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4Compressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */