/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteOrder;
/*     */ import net.jpountz.util.UnsafeUtils;
/*     */ import net.jpountz.util.Utils;
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
/*     */  enum LZ4UnsafeUtils
/*     */ {
/*     */   private LZ4UnsafeUtils() {}
/*     */   
/*     */   static void safeArraycopy(byte[] src, int srcOff, byte[] dest, int destOff, int len)
/*     */   {
/*  38 */     int fastLen = len & 0xFFFFFFF8;
/*  39 */     wildArraycopy(src, srcOff, dest, destOff, fastLen);
/*  40 */     int i = 0; for (int slowLen = len & 0x7; i < slowLen; i++) {
/*  41 */       UnsafeUtils.writeByte(dest, destOff + fastLen + i, UnsafeUtils.readByte(src, srcOff + fastLen + i));
/*     */     }
/*     */   }
/*     */   
/*     */   static void wildArraycopy(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
/*  46 */     for (int i = 0; i < len; i += 8) {
/*  47 */       UnsafeUtils.writeLong(dest, destOff + i, UnsafeUtils.readLong(src, srcOff + i));
/*     */     }
/*     */   }
/*     */   
/*     */   static void wildIncrementalCopy(byte[] dest, int matchOff, int dOff, int matchCopyEnd) {
/*  52 */     if (dOff - matchOff < 4) {
/*  53 */       for (int i = 0; i < 4; i++) {
/*  54 */         UnsafeUtils.writeByte(dest, dOff + i, UnsafeUtils.readByte(dest, matchOff + i));
/*     */       }
/*  56 */       dOff += 4;
/*  57 */       matchOff += 4;
/*  58 */       int dec = 0;
/*  59 */       assert ((dOff >= matchOff) && (dOff - matchOff < 8));
/*  60 */       switch (dOff - matchOff) {
/*     */       case 1: 
/*  62 */         matchOff -= 3;
/*  63 */         break;
/*     */       case 2: 
/*  65 */         matchOff -= 2;
/*  66 */         break;
/*     */       case 3: 
/*  68 */         matchOff -= 3;
/*  69 */         dec = -1;
/*  70 */         break;
/*     */       case 5: 
/*  72 */         dec = 1;
/*  73 */         break;
/*     */       case 6: 
/*  75 */         dec = 2;
/*  76 */         break;
/*     */       case 7: 
/*  78 */         dec = 3;
/*  79 */         break;
/*     */       }
/*     */       
/*     */       
/*  83 */       UnsafeUtils.writeInt(dest, dOff, UnsafeUtils.readInt(dest, matchOff));
/*  84 */       dOff += 4;
/*  85 */       matchOff -= dec;
/*  86 */     } else if (dOff - matchOff < 8) {
/*  87 */       UnsafeUtils.writeLong(dest, dOff, UnsafeUtils.readLong(dest, matchOff));
/*  88 */       dOff += dOff - matchOff;
/*     */     }
/*  90 */     while (dOff < matchCopyEnd) {
/*  91 */       UnsafeUtils.writeLong(dest, dOff, UnsafeUtils.readLong(dest, matchOff));
/*  92 */       dOff += 8;
/*  93 */       matchOff += 8;
/*     */     }
/*     */   }
/*     */   
/*     */   static void safeIncrementalCopy(byte[] dest, int matchOff, int dOff, int matchLen) {
/*  98 */     for (int i = 0; i < matchLen; i++) {
/*  99 */       dest[(dOff + i)] = dest[(matchOff + i)];
/* 100 */       UnsafeUtils.writeByte(dest, dOff + i, UnsafeUtils.readByte(dest, matchOff + i));
/*     */     }
/*     */   }
/*     */   
/*     */   static int readShortLittleEndian(byte[] src, int srcOff) {
/* 105 */     short s = UnsafeUtils.readShort(src, srcOff);
/* 106 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 107 */       s = Short.reverseBytes(s);
/*     */     }
/* 109 */     return s & 0xFFFF;
/*     */   }
/*     */   
/*     */   static void writeShortLittleEndian(byte[] dest, int destOff, int value) {
/* 113 */     short s = (short)value;
/* 114 */     if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 115 */       s = Short.reverseBytes(s);
/*     */     }
/* 117 */     UnsafeUtils.writeShort(dest, destOff, s);
/*     */   }
/*     */   
/*     */   static boolean readIntEquals(byte[] src, int ref, int sOff) {
/* 121 */     return UnsafeUtils.readInt(src, ref) == UnsafeUtils.readInt(src, sOff);
/*     */   }
/*     */   
/*     */   static int commonBytes(byte[] src, int ref, int sOff, int srcLimit) {
/* 125 */     int matchLen = 0;
/* 126 */     while (sOff <= srcLimit - 8) {
/* 127 */       if (UnsafeUtils.readLong(src, sOff) == UnsafeUtils.readLong(src, ref)) {
/* 128 */         matchLen += 8;
/* 129 */         ref += 8;
/* 130 */         sOff += 8;
/*     */       } else { int zeroBits;
/*     */         int zeroBits;
/* 133 */         if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
/* 134 */           zeroBits = Long.numberOfLeadingZeros(UnsafeUtils.readLong(src, sOff) ^ UnsafeUtils.readLong(src, ref));
/*     */         } else {
/* 136 */           zeroBits = Long.numberOfTrailingZeros(UnsafeUtils.readLong(src, sOff) ^ UnsafeUtils.readLong(src, ref));
/*     */         }
/* 138 */         return matchLen + (zeroBits >>> 3);
/*     */       }
/*     */     }
/* 141 */     while ((sOff < srcLimit) && (UnsafeUtils.readByte(src, ref++) == UnsafeUtils.readByte(src, sOff++))) {
/* 142 */       matchLen++;
/*     */     }
/* 144 */     return matchLen;
/*     */   }
/*     */   
/*     */   static int writeLen(int len, byte[] dest, int dOff) {
/* 148 */     while (len >= 255) {
/* 149 */       UnsafeUtils.writeByte(dest, dOff++, 255);
/* 150 */       len -= 255;
/*     */     }
/* 152 */     UnsafeUtils.writeByte(dest, dOff++, len);
/* 153 */     return dOff;
/*     */   }
/*     */   
/*     */   static int encodeSequence(byte[] src, int anchor, int matchOff, int matchRef, int matchLen, byte[] dest, int dOff, int destEnd) {
/* 157 */     int runLen = matchOff - anchor;
/* 158 */     int tokenOff = dOff++;
/*     */     
/*     */     int token;
/* 161 */     if (runLen >= 15) {
/* 162 */       int token = -16;
/* 163 */       dOff = writeLen(runLen - 15, dest, dOff);
/*     */     } else {
/* 165 */       token = runLen << 4;
/*     */     }
/*     */     
/*     */ 
/* 169 */     wildArraycopy(src, anchor, dest, dOff, runLen);
/* 170 */     dOff += runLen;
/*     */     
/*     */ 
/* 173 */     int matchDec = matchOff - matchRef;
/* 174 */     dest[(dOff++)] = ((byte)matchDec);
/* 175 */     dest[(dOff++)] = ((byte)(matchDec >>> 8));
/*     */     
/*     */ 
/* 178 */     matchLen -= 4;
/* 179 */     if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 180 */       throw new LZ4Exception("maxDestLen is too small");
/*     */     }
/* 182 */     if (matchLen >= 15) {
/* 183 */       token |= 0xF;
/* 184 */       dOff = writeLen(matchLen - 15, dest, dOff);
/*     */     } else {
/* 186 */       token |= matchLen;
/*     */     }
/*     */     
/* 189 */     dest[tokenOff] = ((byte)token);
/*     */     
/* 191 */     return dOff;
/*     */   }
/*     */   
/*     */   static int commonBytesBackward(byte[] b, int o1, int o2, int l1, int l2) {
/* 195 */     int count = 0;
/* 196 */     while ((o1 > l1) && (o2 > l2) && (UnsafeUtils.readByte(b, --o1) == UnsafeUtils.readByte(b, --o2))) {
/* 197 */       count++;
/*     */     }
/* 199 */     return count;
/*     */   }
/*     */   
/*     */   static int lastLiterals(byte[] src, int sOff, int srcLen, byte[] dest, int dOff, int destEnd) {
/* 203 */     return LZ4SafeUtils.lastLiterals(src, sOff, srcLen, dest, dOff, destEnd);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4UnsafeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */