/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import net.jpountz.util.SafeUtils;
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
/*     */  enum LZ4SafeUtils
/*     */ {
/*     */   private LZ4SafeUtils() {}
/*     */   
/*     */   static int hash(byte[] buf, int i)
/*     */   {
/*  27 */     return LZ4Utils.hash(SafeUtils.readInt(buf, i));
/*     */   }
/*     */   
/*     */   static int hash64k(byte[] buf, int i) {
/*  31 */     return LZ4Utils.hash64k(SafeUtils.readInt(buf, i));
/*     */   }
/*     */   
/*     */   static boolean readIntEquals(byte[] buf, int i, int j) {
/*  35 */     return (buf[i] == buf[j]) && (buf[(i + 1)] == buf[(j + 1)]) && (buf[(i + 2)] == buf[(j + 2)]) && (buf[(i + 3)] == buf[(j + 3)]);
/*     */   }
/*     */   
/*     */   static void safeIncrementalCopy(byte[] dest, int matchOff, int dOff, int matchLen) {
/*  39 */     for (int i = 0; i < matchLen; i++) {
/*  40 */       dest[(dOff + i)] = dest[(matchOff + i)];
/*     */     }
/*     */   }
/*     */   
/*     */   static void wildIncrementalCopy(byte[] dest, int matchOff, int dOff, int matchCopyEnd) {
/*     */     do {
/*  46 */       copy8Bytes(dest, matchOff, dest, dOff);
/*  47 */       matchOff += 8;
/*  48 */       dOff += 8;
/*  49 */     } while (dOff < matchCopyEnd);
/*     */   }
/*     */   
/*     */   static void copy8Bytes(byte[] src, int sOff, byte[] dest, int dOff) {
/*  53 */     for (int i = 0; i < 8; i++) {
/*  54 */       dest[(dOff + i)] = src[(sOff + i)];
/*     */     }
/*     */   }
/*     */   
/*     */   static int commonBytes(byte[] b, int o1, int o2, int limit) {
/*  59 */     int count = 0;
/*  60 */     while ((o2 < limit) && (b[(o1++)] == b[(o2++)])) {
/*  61 */       count++;
/*     */     }
/*  63 */     return count;
/*     */   }
/*     */   
/*     */   static int commonBytesBackward(byte[] b, int o1, int o2, int l1, int l2) {
/*  67 */     int count = 0;
/*  68 */     while ((o1 > l1) && (o2 > l2) && (b[(--o1)] == b[(--o2)])) {
/*  69 */       count++;
/*     */     }
/*  71 */     return count;
/*     */   }
/*     */   
/*     */   static void safeArraycopy(byte[] src, int sOff, byte[] dest, int dOff, int len) {
/*  75 */     System.arraycopy(src, sOff, dest, dOff, len);
/*     */   }
/*     */   
/*     */   static void wildArraycopy(byte[] src, int sOff, byte[] dest, int dOff, int len) {
/*     */     try {
/*  80 */       for (int i = 0; i < len; i += 8) {
/*  81 */         copy8Bytes(src, sOff + i, dest, dOff + i);
/*     */       }
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/*  84 */       throw new LZ4Exception("Malformed input at offset " + sOff);
/*     */     }
/*     */   }
/*     */   
/*     */   static int encodeSequence(byte[] src, int anchor, int matchOff, int matchRef, int matchLen, byte[] dest, int dOff, int destEnd) {
/*  89 */     int runLen = matchOff - anchor;
/*  90 */     int tokenOff = dOff++;
/*     */     
/*  92 */     if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/*  93 */       throw new LZ4Exception("maxDestLen is too small");
/*     */     }
/*     */     
/*     */     int token;
/*  97 */     if (runLen >= 15) {
/*  98 */       int token = -16;
/*  99 */       dOff = writeLen(runLen - 15, dest, dOff);
/*     */     } else {
/* 101 */       token = runLen << 4;
/*     */     }
/*     */     
/*     */ 
/* 105 */     wildArraycopy(src, anchor, dest, dOff, runLen);
/* 106 */     dOff += runLen;
/*     */     
/*     */ 
/* 109 */     int matchDec = matchOff - matchRef;
/* 110 */     dest[(dOff++)] = ((byte)matchDec);
/* 111 */     dest[(dOff++)] = ((byte)(matchDec >>> 8));
/*     */     
/*     */ 
/* 114 */     matchLen -= 4;
/* 115 */     if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 116 */       throw new LZ4Exception("maxDestLen is too small");
/*     */     }
/* 118 */     if (matchLen >= 15) {
/* 119 */       token |= 0xF;
/* 120 */       dOff = writeLen(matchLen - 15, dest, dOff);
/*     */     } else {
/* 122 */       token |= matchLen;
/*     */     }
/*     */     
/* 125 */     dest[tokenOff] = ((byte)token);
/*     */     
/* 127 */     return dOff;
/*     */   }
/*     */   
/*     */   static int lastLiterals(byte[] src, int sOff, int srcLen, byte[] dest, int dOff, int destEnd) {
/* 131 */     int runLen = srcLen;
/*     */     
/* 133 */     if (dOff + runLen + 1 + (runLen + 255 - 15) / 255 > destEnd) {
/* 134 */       throw new LZ4Exception();
/*     */     }
/*     */     
/* 137 */     if (runLen >= 15) {
/* 138 */       dest[(dOff++)] = -16;
/* 139 */       dOff = writeLen(runLen - 15, dest, dOff);
/*     */     } else {
/* 141 */       dest[(dOff++)] = ((byte)(runLen << 4));
/*     */     }
/*     */     
/* 144 */     System.arraycopy(src, sOff, dest, dOff, runLen);
/* 145 */     dOff += runLen;
/*     */     
/* 147 */     return dOff;
/*     */   }
/*     */   
/*     */   static int writeLen(int len, byte[] dest, int dOff) {
/* 151 */     while (len >= 255) {
/* 152 */       dest[(dOff++)] = -1;
/* 153 */       len -= 255;
/*     */     }
/* 155 */     dest[(dOff++)] = ((byte)len);
/* 156 */     return dOff;
/*     */   }
/*     */   
/*     */   static class Match { int start;
/*     */     int ref;
/*     */     int len;
/*     */     
/* 163 */     void fix(int correction) { this.start += correction;
/* 164 */       this.ref += correction;
/* 165 */       this.len -= correction;
/*     */     }
/*     */     
/*     */     int end() {
/* 169 */       return this.start + this.len;
/*     */     }
/*     */   }
/*     */   
/*     */   static void copyTo(Match m1, Match m2) {
/* 174 */     m2.len = m1.len;
/* 175 */     m2.start = m1.start;
/* 176 */     m2.ref = m1.ref;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4SafeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */