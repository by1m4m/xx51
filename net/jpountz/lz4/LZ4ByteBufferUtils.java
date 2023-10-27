/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import net.jpountz.util.ByteBufferUtils;
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
/*     */  enum LZ4ByteBufferUtils
/*     */ {
/*     */   private LZ4ByteBufferUtils() {}
/*     */   
/*     */   static int hash(ByteBuffer buf, int i)
/*     */   {
/*  35 */     return LZ4Utils.hash(ByteBufferUtils.readInt(buf, i));
/*     */   }
/*     */   
/*     */   static int hash64k(ByteBuffer buf, int i) {
/*  39 */     return LZ4Utils.hash64k(ByteBufferUtils.readInt(buf, i));
/*     */   }
/*     */   
/*     */   static boolean readIntEquals(ByteBuffer buf, int i, int j) {
/*  43 */     return buf.getInt(i) == buf.getInt(j);
/*     */   }
/*     */   
/*     */   static void safeIncrementalCopy(ByteBuffer dest, int matchOff, int dOff, int matchLen) {
/*  47 */     for (int i = 0; i < matchLen; i++) {
/*  48 */       dest.put(dOff + i, dest.get(matchOff + i));
/*     */     }
/*     */   }
/*     */   
/*     */   static void wildIncrementalCopy(ByteBuffer dest, int matchOff, int dOff, int matchCopyEnd) {
/*  53 */     if (dOff - matchOff < 4) {
/*  54 */       for (int i = 0; i < 4; i++) {
/*  55 */         ByteBufferUtils.writeByte(dest, dOff + i, ByteBufferUtils.readByte(dest, matchOff + i));
/*     */       }
/*  57 */       dOff += 4;
/*  58 */       matchOff += 4;
/*  59 */       int dec = 0;
/*  60 */       assert ((dOff >= matchOff) && (dOff - matchOff < 8));
/*  61 */       switch (dOff - matchOff) {
/*     */       case 1: 
/*  63 */         matchOff -= 3;
/*  64 */         break;
/*     */       case 2: 
/*  66 */         matchOff -= 2;
/*  67 */         break;
/*     */       case 3: 
/*  69 */         matchOff -= 3;
/*  70 */         dec = -1;
/*  71 */         break;
/*     */       case 5: 
/*  73 */         dec = 1;
/*  74 */         break;
/*     */       case 6: 
/*  76 */         dec = 2;
/*  77 */         break;
/*     */       case 7: 
/*  79 */         dec = 3;
/*  80 */         break;
/*     */       }
/*     */       
/*     */       
/*  84 */       ByteBufferUtils.writeInt(dest, dOff, ByteBufferUtils.readInt(dest, matchOff));
/*  85 */       dOff += 4;
/*  86 */       matchOff -= dec;
/*  87 */     } else if (dOff - matchOff < 8) {
/*  88 */       ByteBufferUtils.writeLong(dest, dOff, ByteBufferUtils.readLong(dest, matchOff));
/*  89 */       dOff += dOff - matchOff;
/*     */     }
/*  91 */     while (dOff < matchCopyEnd) {
/*  92 */       ByteBufferUtils.writeLong(dest, dOff, ByteBufferUtils.readLong(dest, matchOff));
/*  93 */       dOff += 8;
/*  94 */       matchOff += 8;
/*     */     }
/*     */   }
/*     */   
/*     */   static int commonBytes(ByteBuffer src, int ref, int sOff, int srcLimit) {
/*  99 */     int matchLen = 0;
/* 100 */     while (sOff <= srcLimit - 8) {
/* 101 */       if (ByteBufferUtils.readLong(src, sOff) == ByteBufferUtils.readLong(src, ref)) {
/* 102 */         matchLen += 8;
/* 103 */         ref += 8;
/* 104 */         sOff += 8;
/*     */       } else { int zeroBits;
/*     */         int zeroBits;
/* 107 */         if (src.order() == ByteOrder.BIG_ENDIAN) {
/* 108 */           zeroBits = Long.numberOfLeadingZeros(ByteBufferUtils.readLong(src, sOff) ^ ByteBufferUtils.readLong(src, ref));
/*     */         } else {
/* 110 */           zeroBits = Long.numberOfTrailingZeros(ByteBufferUtils.readLong(src, sOff) ^ ByteBufferUtils.readLong(src, ref));
/*     */         }
/* 112 */         return matchLen + (zeroBits >>> 3);
/*     */       }
/*     */     }
/* 115 */     while ((sOff < srcLimit) && (ByteBufferUtils.readByte(src, ref++) == ByteBufferUtils.readByte(src, sOff++))) {
/* 116 */       matchLen++;
/*     */     }
/* 118 */     return matchLen;
/*     */   }
/*     */   
/*     */   static int commonBytesBackward(ByteBuffer b, int o1, int o2, int l1, int l2) {
/* 122 */     int count = 0;
/* 123 */     while ((o1 > l1) && (o2 > l2) && (b.get(--o1) == b.get(--o2))) {
/* 124 */       count++;
/*     */     }
/* 126 */     return count;
/*     */   }
/*     */   
/*     */   static void safeArraycopy(ByteBuffer src, int sOff, ByteBuffer dest, int dOff, int len) {
/* 130 */     for (int i = 0; i < len; i++) {
/* 131 */       dest.put(dOff + i, src.get(sOff + i));
/*     */     }
/*     */   }
/*     */   
/*     */   static void wildArraycopy(ByteBuffer src, int sOff, ByteBuffer dest, int dOff, int len) {
/* 136 */     assert (src.order().equals(dest.order()));
/*     */     try {
/* 138 */       for (int i = 0; i < len; i += 8) {
/* 139 */         dest.putLong(dOff + i, src.getLong(sOff + i));
/*     */       }
/*     */     } catch (IndexOutOfBoundsException e) {
/* 142 */       throw new LZ4Exception("Malformed input at offset " + sOff);
/*     */     }
/*     */   }
/*     */   
/*     */   static int encodeSequence(ByteBuffer src, int anchor, int matchOff, int matchRef, int matchLen, ByteBuffer dest, int dOff, int destEnd) {
/* 147 */     int runLen = matchOff - anchor;
/* 148 */     int tokenOff = dOff++;
/*     */     
/* 150 */     if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/* 151 */       throw new LZ4Exception("maxDestLen is too small");
/*     */     }
/*     */     
/*     */     int token;
/* 155 */     if (runLen >= 15) {
/* 156 */       int token = -16;
/* 157 */       dOff = writeLen(runLen - 15, dest, dOff);
/*     */     } else {
/* 159 */       token = runLen << 4;
/*     */     }
/*     */     
/*     */ 
/* 163 */     wildArraycopy(src, anchor, dest, dOff, runLen);
/* 164 */     dOff += runLen;
/*     */     
/*     */ 
/* 167 */     int matchDec = matchOff - matchRef;
/* 168 */     dest.put(dOff++, (byte)matchDec);
/* 169 */     dest.put(dOff++, (byte)(matchDec >>> 8));
/*     */     
/*     */ 
/* 172 */     matchLen -= 4;
/* 173 */     if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 174 */       throw new LZ4Exception("maxDestLen is too small");
/*     */     }
/* 176 */     if (matchLen >= 15) {
/* 177 */       token |= 0xF;
/* 178 */       dOff = writeLen(matchLen - 15, dest, dOff);
/*     */     } else {
/* 180 */       token |= matchLen;
/*     */     }
/*     */     
/* 183 */     dest.put(tokenOff, (byte)token);
/*     */     
/* 185 */     return dOff;
/*     */   }
/*     */   
/*     */   static int lastLiterals(ByteBuffer src, int sOff, int srcLen, ByteBuffer dest, int dOff, int destEnd) {
/* 189 */     int runLen = srcLen;
/*     */     
/* 191 */     if (dOff + runLen + 1 + (runLen + 255 - 15) / 255 > destEnd) {
/* 192 */       throw new LZ4Exception();
/*     */     }
/*     */     
/* 195 */     if (runLen >= 15) {
/* 196 */       dest.put(dOff++, (byte)-16);
/* 197 */       dOff = writeLen(runLen - 15, dest, dOff);
/*     */     } else {
/* 199 */       dest.put(dOff++, (byte)(runLen << 4));
/*     */     }
/*     */     
/* 202 */     safeArraycopy(src, sOff, dest, dOff, runLen);
/* 203 */     dOff += runLen;
/*     */     
/* 205 */     return dOff;
/*     */   }
/*     */   
/*     */   static int writeLen(int len, ByteBuffer dest, int dOff) {
/* 209 */     while (len >= 255) {
/* 210 */       dest.put(dOff++, (byte)-1);
/* 211 */       len -= 255;
/*     */     }
/* 213 */     dest.put(dOff++, (byte)len);
/* 214 */     return dOff;
/*     */   }
/*     */   
/*     */   static class Match { int start;
/*     */     int ref;
/*     */     int len;
/*     */     
/* 221 */     void fix(int correction) { this.start += correction;
/* 222 */       this.ref += correction;
/* 223 */       this.len -= correction;
/*     */     }
/*     */     
/*     */     int end() {
/* 227 */       return this.start + this.len;
/*     */     }
/*     */   }
/*     */   
/*     */   static void copyTo(Match m1, Match m2) {
/* 232 */     m2.len = m1.len;
/* 233 */     m2.start = m1.start;
/* 234 */     m2.ref = m1.ref;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4ByteBufferUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */