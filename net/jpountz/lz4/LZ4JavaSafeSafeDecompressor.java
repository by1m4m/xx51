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
/*     */ final class LZ4JavaSafeSafeDecompressor
/*     */   extends LZ4SafeDecompressor
/*     */ {
/*  17 */   public static final LZ4SafeDecompressor INSTANCE = new LZ4JavaSafeSafeDecompressor();
/*     */   
/*     */ 
/*     */ 
/*     */   public int decompress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int destLen)
/*     */   {
/*  23 */     SafeUtils.checkRange(src, srcOff, srcLen);
/*  24 */     SafeUtils.checkRange(dest, destOff, destLen);
/*     */     
/*  26 */     if (destLen == 0) {
/*  27 */       if ((srcLen != 1) || (SafeUtils.readByte(src, srcOff) != 0)) {
/*  28 */         throw new LZ4Exception("Output buffer too small");
/*     */       }
/*  30 */       return 0;
/*     */     }
/*     */     
/*  33 */     int srcEnd = srcOff + srcLen;
/*     */     
/*     */ 
/*  36 */     int destEnd = destOff + destLen;
/*     */     
/*  38 */     int sOff = srcOff;
/*  39 */     int dOff = destOff;
/*     */     for (;;)
/*     */     {
/*  42 */       int token = SafeUtils.readByte(src, sOff) & 0xFF;
/*  43 */       sOff++;
/*     */       
/*     */ 
/*  46 */       int literalLen = token >>> 4;
/*  47 */       if (literalLen == 15) {
/*  48 */         byte len = -1;
/*  49 */         while ((sOff < srcEnd) && ((len = SafeUtils.readByte(src, sOff++)) == -1)) {
/*  50 */           literalLen += 255;
/*     */         }
/*  52 */         literalLen += (len & 0xFF);
/*     */       }
/*     */       
/*  55 */       int literalCopyEnd = dOff + literalLen;
/*     */       
/*  57 */       if ((literalCopyEnd > destEnd - 8) || (sOff + literalLen > srcEnd - 8)) {
/*  58 */         if (literalCopyEnd > destEnd)
/*  59 */           throw new LZ4Exception();
/*  60 */         if (sOff + literalLen != srcEnd) {
/*  61 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/*     */         
/*  64 */         LZ4SafeUtils.safeArraycopy(src, sOff, dest, dOff, literalLen);
/*  65 */         sOff += literalLen;
/*  66 */         dOff = literalCopyEnd;
/*  67 */         break;
/*     */       }
/*     */       
/*     */ 
/*  71 */       LZ4SafeUtils.wildArraycopy(src, sOff, dest, dOff, literalLen);
/*  72 */       sOff += literalLen;
/*  73 */       dOff = literalCopyEnd;
/*     */       
/*     */ 
/*  76 */       int matchDec = SafeUtils.readShortLE(src, sOff);
/*  77 */       sOff += 2;
/*  78 */       int matchOff = dOff - matchDec;
/*     */       
/*  80 */       if (matchOff < destOff) {
/*  81 */         throw new LZ4Exception("Malformed input at " + sOff);
/*     */       }
/*     */       
/*  84 */       int matchLen = token & 0xF;
/*  85 */       if (matchLen == 15) {
/*  86 */         byte len = -1;
/*  87 */         while ((sOff < srcEnd) && ((len = SafeUtils.readByte(src, sOff++)) == -1)) {
/*  88 */           matchLen += 255;
/*     */         }
/*  90 */         matchLen += (len & 0xFF);
/*     */       }
/*  92 */       matchLen += 4;
/*     */       
/*  94 */       int matchCopyEnd = dOff + matchLen;
/*     */       
/*  96 */       if (matchCopyEnd > destEnd - 8) {
/*  97 */         if (matchCopyEnd > destEnd) {
/*  98 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/* 100 */         LZ4SafeUtils.safeIncrementalCopy(dest, matchOff, dOff, matchLen);
/*     */       } else {
/* 102 */         LZ4SafeUtils.wildIncrementalCopy(dest, matchOff, dOff, matchCopyEnd);
/*     */       }
/* 104 */       dOff = matchCopyEnd;
/*     */     }
/*     */     
/*     */ 
/* 108 */     return dOff - destOff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int decompress(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int destLen)
/*     */   {
/* 115 */     if ((src.hasArray()) && (dest.hasArray())) {
/* 116 */       return decompress(src.array(), srcOff + src.arrayOffset(), srcLen, dest.array(), destOff + dest.arrayOffset(), destLen);
/*     */     }
/* 118 */     src = ByteBufferUtils.inNativeByteOrder(src);
/* 119 */     dest = ByteBufferUtils.inNativeByteOrder(dest);
/*     */     
/*     */ 
/* 122 */     ByteBufferUtils.checkRange(src, srcOff, srcLen);
/* 123 */     ByteBufferUtils.checkRange(dest, destOff, destLen);
/*     */     
/* 125 */     if (destLen == 0) {
/* 126 */       if ((srcLen != 1) || (ByteBufferUtils.readByte(src, srcOff) != 0)) {
/* 127 */         throw new LZ4Exception("Output buffer too small");
/*     */       }
/* 129 */       return 0;
/*     */     }
/*     */     
/* 132 */     int srcEnd = srcOff + srcLen;
/*     */     
/*     */ 
/* 135 */     int destEnd = destOff + destLen;
/*     */     
/* 137 */     int sOff = srcOff;
/* 138 */     int dOff = destOff;
/*     */     for (;;)
/*     */     {
/* 141 */       int token = ByteBufferUtils.readByte(src, sOff) & 0xFF;
/* 142 */       sOff++;
/*     */       
/*     */ 
/* 145 */       int literalLen = token >>> 4;
/* 146 */       if (literalLen == 15) {
/* 147 */         byte len = -1;
/* 148 */         while ((sOff < srcEnd) && ((len = ByteBufferUtils.readByte(src, sOff++)) == -1)) {
/* 149 */           literalLen += 255;
/*     */         }
/* 151 */         literalLen += (len & 0xFF);
/*     */       }
/*     */       
/* 154 */       int literalCopyEnd = dOff + literalLen;
/*     */       
/* 156 */       if ((literalCopyEnd > destEnd - 8) || (sOff + literalLen > srcEnd - 8)) {
/* 157 */         if (literalCopyEnd > destEnd)
/* 158 */           throw new LZ4Exception();
/* 159 */         if (sOff + literalLen != srcEnd) {
/* 160 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/*     */         
/* 163 */         LZ4ByteBufferUtils.safeArraycopy(src, sOff, dest, dOff, literalLen);
/* 164 */         sOff += literalLen;
/* 165 */         dOff = literalCopyEnd;
/* 166 */         break;
/*     */       }
/*     */       
/*     */ 
/* 170 */       LZ4ByteBufferUtils.wildArraycopy(src, sOff, dest, dOff, literalLen);
/* 171 */       sOff += literalLen;
/* 172 */       dOff = literalCopyEnd;
/*     */       
/*     */ 
/* 175 */       int matchDec = ByteBufferUtils.readShortLE(src, sOff);
/* 176 */       sOff += 2;
/* 177 */       int matchOff = dOff - matchDec;
/*     */       
/* 179 */       if (matchOff < destOff) {
/* 180 */         throw new LZ4Exception("Malformed input at " + sOff);
/*     */       }
/*     */       
/* 183 */       int matchLen = token & 0xF;
/* 184 */       if (matchLen == 15) {
/* 185 */         byte len = -1;
/* 186 */         while ((sOff < srcEnd) && ((len = ByteBufferUtils.readByte(src, sOff++)) == -1)) {
/* 187 */           matchLen += 255;
/*     */         }
/* 189 */         matchLen += (len & 0xFF);
/*     */       }
/* 191 */       matchLen += 4;
/*     */       
/* 193 */       int matchCopyEnd = dOff + matchLen;
/*     */       
/* 195 */       if (matchCopyEnd > destEnd - 8) {
/* 196 */         if (matchCopyEnd > destEnd) {
/* 197 */           throw new LZ4Exception("Malformed input at " + sOff);
/*     */         }
/* 199 */         LZ4ByteBufferUtils.safeIncrementalCopy(dest, matchOff, dOff, matchLen);
/*     */       } else {
/* 201 */         LZ4ByteBufferUtils.wildIncrementalCopy(dest, matchOff, dOff, matchCopyEnd);
/*     */       }
/* 203 */       dOff = matchCopyEnd;
/*     */     }
/*     */     
/*     */ 
/* 207 */     return dOff - destOff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JavaSafeSafeDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */