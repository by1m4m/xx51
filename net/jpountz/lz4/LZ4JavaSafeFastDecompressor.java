/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.jpountz.util.ByteBufferUtils;
/*     */ import net.jpountz.util.SafeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class LZ4JavaSafeFastDecompressor
/*     */   extends LZ4FastDecompressor
/*     */ {
/*  17 */   public static final LZ4FastDecompressor INSTANCE = new LZ4JavaSafeFastDecompressor();
/*     */   
/*     */ 
/*     */ 
/*     */   public int decompress(byte[] src, int srcOff, byte[] dest, int destOff, int destLen)
/*     */   {
/*  23 */     SafeUtils.checkRange(src, srcOff);
/*  24 */     SafeUtils.checkRange(dest, destOff, destLen);
/*     */     
/*  26 */     if (destLen == 0) {
/*  27 */       if (SafeUtils.readByte(src, srcOff) != 0) {
/*  28 */         throw new LZ4Exception("Malformed input at " + srcOff);
/*     */       }
/*  30 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*  34 */     int destEnd = destOff + destLen;
/*     */     
/*  36 */     int sOff = srcOff;
/*  37 */     int dOff = destOff;
/*     */     for (;;)
/*     */     {
/*  40 */       int token = SafeUtils.readByte(src, sOff) & 0xFF;
/*  41 */       sOff++;
/*     */       
/*     */ 
/*  44 */       int literalLen = token >>> 4;
/*  45 */       if (literalLen == 15) {
/*  46 */         byte len = -1;
/*  47 */         while ((len = SafeUtils.readByte(src, sOff++)) == -1) {
/*  48 */           literalLen += 255;
/*     */         }
/*  50 */         literalLen += (len & 0xFF);
/*     */       }
/*     */       
/*  53 */       int literalCopyEnd = dOff + literalLen;
/*     */       
/*  55 */       if (literalCopyEnd > destEnd - 8) {
/*  56 */         if (literalCopyEnd != destEnd) {
/*  57 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/*     */         
/*  60 */         LZ4SafeUtils.safeArraycopy(src, sOff, dest, dOff, literalLen);
/*  61 */         sOff += literalLen;
/*  62 */         dOff = literalCopyEnd;
/*  63 */         break;
/*     */       }
/*     */       
/*     */ 
/*  67 */       LZ4SafeUtils.wildArraycopy(src, sOff, dest, dOff, literalLen);
/*  68 */       sOff += literalLen;
/*  69 */       dOff = literalCopyEnd;
/*     */       
/*     */ 
/*  72 */       int matchDec = SafeUtils.readShortLE(src, sOff);
/*  73 */       sOff += 2;
/*  74 */       int matchOff = dOff - matchDec;
/*     */       
/*  76 */       if (matchOff < destOff) {
/*  77 */         throw new LZ4Exception("Malformed input at " + sOff);
/*     */       }
/*     */       
/*  80 */       int matchLen = token & 0xF;
/*  81 */       if (matchLen == 15) {
/*  82 */         byte len = -1;
/*  83 */         while ((len = SafeUtils.readByte(src, sOff++)) == -1) {
/*  84 */           matchLen += 255;
/*     */         }
/*  86 */         matchLen += (len & 0xFF);
/*     */       }
/*  88 */       matchLen += 4;
/*     */       
/*  90 */       int matchCopyEnd = dOff + matchLen;
/*     */       
/*  92 */       if (matchCopyEnd > destEnd - 8) {
/*  93 */         if (matchCopyEnd > destEnd) {
/*  94 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/*  96 */         LZ4SafeUtils.safeIncrementalCopy(dest, matchOff, dOff, matchLen);
/*     */       } else {
/*  98 */         LZ4SafeUtils.wildIncrementalCopy(dest, matchOff, dOff, matchCopyEnd);
/*     */       }
/* 100 */       dOff = matchCopyEnd;
/*     */     }
/*     */     
/*     */ 
/* 104 */     return sOff - srcOff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int decompress(ByteBuffer src, int srcOff, ByteBuffer dest, int destOff, int destLen)
/*     */   {
/* 111 */     if ((src.hasArray()) && (dest.hasArray())) {
/* 112 */       return decompress(src.array(), srcOff + src.arrayOffset(), dest.array(), destOff + dest.arrayOffset(), destLen);
/*     */     }
/* 114 */     src = ByteBufferUtils.inNativeByteOrder(src);
/* 115 */     dest = ByteBufferUtils.inNativeByteOrder(dest);
/*     */     
/*     */ 
/* 118 */     ByteBufferUtils.checkRange(src, srcOff);
/* 119 */     ByteBufferUtils.checkRange(dest, destOff, destLen);
/*     */     
/* 121 */     if (destLen == 0) {
/* 122 */       if (ByteBufferUtils.readByte(src, srcOff) != 0) {
/* 123 */         throw new LZ4Exception("Malformed input at " + srcOff);
/*     */       }
/* 125 */       return 1;
/*     */     }
/*     */     
/*     */ 
/* 129 */     int destEnd = destOff + destLen;
/*     */     
/* 131 */     int sOff = srcOff;
/* 132 */     int dOff = destOff;
/*     */     for (;;)
/*     */     {
/* 135 */       int token = ByteBufferUtils.readByte(src, sOff) & 0xFF;
/* 136 */       sOff++;
/*     */       
/*     */ 
/* 139 */       int literalLen = token >>> 4;
/* 140 */       if (literalLen == 15) {
/* 141 */         byte len = -1;
/* 142 */         while ((len = ByteBufferUtils.readByte(src, sOff++)) == -1) {
/* 143 */           literalLen += 255;
/*     */         }
/* 145 */         literalLen += (len & 0xFF);
/*     */       }
/*     */       
/* 148 */       int literalCopyEnd = dOff + literalLen;
/*     */       
/* 150 */       if (literalCopyEnd > destEnd - 8) {
/* 151 */         if (literalCopyEnd != destEnd) {
/* 152 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/*     */         
/* 155 */         LZ4ByteBufferUtils.safeArraycopy(src, sOff, dest, dOff, literalLen);
/* 156 */         sOff += literalLen;
/* 157 */         dOff = literalCopyEnd;
/* 158 */         break;
/*     */       }
/*     */       
/*     */ 
/* 162 */       LZ4ByteBufferUtils.wildArraycopy(src, sOff, dest, dOff, literalLen);
/* 163 */       sOff += literalLen;
/* 164 */       dOff = literalCopyEnd;
/*     */       
/*     */ 
/* 167 */       int matchDec = ByteBufferUtils.readShortLE(src, sOff);
/* 168 */       sOff += 2;
/* 169 */       int matchOff = dOff - matchDec;
/*     */       
/* 171 */       if (matchOff < destOff) {
/* 172 */         throw new LZ4Exception("Malformed input at " + sOff);
/*     */       }
/*     */       
/* 175 */       int matchLen = token & 0xF;
/* 176 */       if (matchLen == 15) {
/* 177 */         byte len = -1;
/* 178 */         while ((len = ByteBufferUtils.readByte(src, sOff++)) == -1) {
/* 179 */           matchLen += 255;
/*     */         }
/* 181 */         matchLen += (len & 0xFF);
/*     */       }
/* 183 */       matchLen += 4;
/*     */       
/* 185 */       int matchCopyEnd = dOff + matchLen;
/*     */       
/* 187 */       if (matchCopyEnd > destEnd - 8) {
/* 188 */         if (matchCopyEnd > destEnd) {
/* 189 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/* 191 */         LZ4ByteBufferUtils.safeIncrementalCopy(dest, matchOff, dOff, matchLen);
/*     */       } else {
/* 193 */         LZ4ByteBufferUtils.wildIncrementalCopy(dest, matchOff, dOff, matchCopyEnd);
/*     */       }
/* 195 */       dOff = matchCopyEnd;
/*     */     }
/*     */     
/*     */ 
/* 199 */     return sOff - srcOff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JavaSafeFastDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */