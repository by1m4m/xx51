/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
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
/*     */ public abstract class LZ4FastDecompressor
/*     */   implements LZ4Decompressor
/*     */ {
/*     */   public abstract int decompress(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract int decompress(ByteBuffer paramByteBuffer1, int paramInt1, ByteBuffer paramByteBuffer2, int paramInt2, int paramInt3);
/*     */   
/*     */   public final int decompress(byte[] src, byte[] dest, int destLen)
/*     */   {
/*  65 */     return decompress(src, 0, dest, 0, destLen);
/*     */   }
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
/*  77 */     return decompress(src, dest, dest.length);
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
/*     */   public final byte[] decompress(byte[] src, int srcOff, int destLen)
/*     */   {
/*  99 */     byte[] decompressed = new byte[destLen];
/* 100 */     decompress(src, srcOff, decompressed, 0, destLen);
/* 101 */     return decompressed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte[] decompress(byte[] src, int destLen)
/*     */   {
/* 113 */     return decompress(src, 0, destLen);
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
/* 125 */     int read = decompress(src, src.position(), dest, dest.position(), dest.remaining());
/* 126 */     dest.position(dest.limit());
/* 127 */     src.position(src.position() + read);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 132 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4FastDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */