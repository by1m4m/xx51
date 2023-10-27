/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ final class LZ4JavaSafeCompressor
/*     */   extends LZ4Compressor
/*     */ {
/*  19 */   public static final LZ4Compressor INSTANCE = new LZ4JavaSafeCompressor();
/*     */   
/*     */   static int compress64k(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int destEnd) {
/*  22 */     int srcEnd = srcOff + srcLen;
/*  23 */     int srcLimit = srcEnd - 5;
/*  24 */     int mflimit = srcEnd - 12;
/*     */     
/*  26 */     int sOff = srcOff;int dOff = destOff;
/*     */     
/*  28 */     int anchor = sOff;
/*     */     
/*  30 */     if (srcLen >= 13)
/*     */     {
/*  32 */       short[] hashTable = new short[' '];
/*     */       
/*  34 */       sOff++;
/*     */       
/*     */ 
/*     */ 
/*     */       for (;;)
/*     */       {
/*  40 */         int forwardOff = sOff;
/*     */         
/*     */ 
/*  43 */         int step = 1;
/*  44 */         int searchMatchNb = 1 << LZ4Constants.SKIP_STRENGTH;
/*     */         int ref;
/*  46 */         do { sOff = forwardOff;
/*  47 */           forwardOff += step;
/*  48 */           step = searchMatchNb++ >>> LZ4Constants.SKIP_STRENGTH;
/*     */           
/*  50 */           if (forwardOff > mflimit) {
/*     */             break;
/*     */           }
/*     */           
/*  54 */           int h = LZ4Utils.hash64k(SafeUtils.readInt(src, sOff));
/*  55 */           ref = srcOff + SafeUtils.readShort(hashTable, h);
/*  56 */           SafeUtils.writeShort(hashTable, h, sOff - srcOff);
/*  57 */         } while (!LZ4SafeUtils.readIntEquals(src, ref, sOff));
/*     */         
/*     */ 
/*  60 */         int excess = LZ4SafeUtils.commonBytesBackward(src, ref, sOff, srcOff, anchor);
/*  61 */         sOff -= excess;
/*  62 */         ref -= excess;
/*     */         
/*     */ 
/*  65 */         int runLen = sOff - anchor;
/*     */         
/*     */ 
/*  68 */         int tokenOff = dOff++;
/*     */         
/*  70 */         if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/*  71 */           throw new LZ4Exception("maxDestLen is too small");
/*     */         }
/*     */         
/*  74 */         if (runLen >= 15) {
/*  75 */           SafeUtils.writeByte(dest, tokenOff, 240);
/*  76 */           dOff = LZ4SafeUtils.writeLen(runLen - 15, dest, dOff);
/*     */         } else {
/*  78 */           SafeUtils.writeByte(dest, tokenOff, runLen << 4);
/*     */         }
/*     */         
/*     */ 
/*  82 */         LZ4SafeUtils.wildArraycopy(src, anchor, dest, dOff, runLen);
/*  83 */         dOff += runLen;
/*     */         
/*     */         for (;;)
/*     */         {
/*  87 */           SafeUtils.writeShortLE(dest, dOff, (short)(sOff - ref));
/*  88 */           dOff += 2;
/*     */           
/*     */ 
/*  91 */           sOff += 4;
/*  92 */           ref += 4;
/*  93 */           int matchLen = LZ4SafeUtils.commonBytes(src, ref, sOff, srcLimit);
/*  94 */           if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/*  95 */             throw new LZ4Exception("maxDestLen is too small");
/*     */           }
/*  97 */           sOff += matchLen;
/*     */           
/*     */ 
/* 100 */           if (matchLen >= 15) {
/* 101 */             SafeUtils.writeByte(dest, tokenOff, SafeUtils.readByte(dest, tokenOff) | 0xF);
/* 102 */             dOff = LZ4SafeUtils.writeLen(matchLen - 15, dest, dOff);
/*     */           } else {
/* 104 */             SafeUtils.writeByte(dest, tokenOff, SafeUtils.readByte(dest, tokenOff) | matchLen);
/*     */           }
/*     */           
/*     */ 
/* 108 */           if (sOff > mflimit) {
/* 109 */             anchor = sOff;
/*     */             
/*     */             break label494;
/*     */           }
/*     */           
/* 114 */           SafeUtils.writeShort(hashTable, LZ4Utils.hash64k(SafeUtils.readInt(src, sOff - 2)), sOff - 2 - srcOff);
/*     */           
/*     */ 
/* 117 */           int h = LZ4Utils.hash64k(SafeUtils.readInt(src, sOff));
/* 118 */           ref = srcOff + SafeUtils.readShort(hashTable, h);
/* 119 */           SafeUtils.writeShort(hashTable, h, sOff - srcOff);
/*     */           
/* 121 */           if (!LZ4SafeUtils.readIntEquals(src, sOff, ref)) {
/*     */             break;
/*     */           }
/*     */           
/* 125 */           tokenOff = dOff++;
/* 126 */           SafeUtils.writeByte(dest, tokenOff, 0);
/*     */         }
/*     */         
/*     */ 
/* 130 */         anchor = sOff++;
/*     */       }
/*     */     }
/*     */     label494:
/* 134 */     dOff = LZ4SafeUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 135 */     return dOff - destOff;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int maxDestLen)
/*     */   {
/* 141 */     SafeUtils.checkRange(src, srcOff, srcLen);
/* 142 */     SafeUtils.checkRange(dest, destOff, maxDestLen);
/* 143 */     int destEnd = destOff + maxDestLen;
/*     */     
/* 145 */     if (srcLen < 65547) {
/* 146 */       return compress64k(src, srcOff, srcLen, dest, destOff, destEnd);
/*     */     }
/*     */     
/* 149 */     int srcEnd = srcOff + srcLen;
/* 150 */     int srcLimit = srcEnd - 5;
/* 151 */     int mflimit = srcEnd - 12;
/*     */     
/* 153 */     int sOff = srcOff;int dOff = destOff;
/* 154 */     int anchor = sOff++;
/*     */     
/* 156 */     int[] hashTable = new int['က'];
/* 157 */     Arrays.fill(hashTable, anchor);
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 163 */       int forwardOff = sOff;
/*     */       
/*     */ 
/* 166 */       int step = 1;
/* 167 */       int searchMatchNb = 1 << LZ4Constants.SKIP_STRENGTH;
/*     */       int ref;
/*     */       int back;
/* 170 */       do { sOff = forwardOff;
/* 171 */         forwardOff += step;
/* 172 */         step = searchMatchNb++ >>> LZ4Constants.SKIP_STRENGTH;
/*     */         
/* 174 */         if (forwardOff > mflimit) {
/*     */           break;
/*     */         }
/*     */         
/* 178 */         int h = LZ4Utils.hash(SafeUtils.readInt(src, sOff));
/* 179 */         ref = SafeUtils.readInt(hashTable, h);
/* 180 */         back = sOff - ref;
/* 181 */         SafeUtils.writeInt(hashTable, h, sOff);
/* 182 */       } while ((back >= 65536) || (!LZ4SafeUtils.readIntEquals(src, ref, sOff)));
/*     */       
/*     */ 
/* 185 */       int excess = LZ4SafeUtils.commonBytesBackward(src, ref, sOff, srcOff, anchor);
/* 186 */       sOff -= excess;
/* 187 */       ref -= excess;
/*     */       
/*     */ 
/* 190 */       int runLen = sOff - anchor;
/*     */       
/*     */ 
/* 193 */       int tokenOff = dOff++;
/*     */       
/* 195 */       if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/* 196 */         throw new LZ4Exception("maxDestLen is too small");
/*     */       }
/*     */       
/* 199 */       if (runLen >= 15) {
/* 200 */         SafeUtils.writeByte(dest, tokenOff, 240);
/* 201 */         dOff = LZ4SafeUtils.writeLen(runLen - 15, dest, dOff);
/*     */       } else {
/* 203 */         SafeUtils.writeByte(dest, tokenOff, runLen << 4);
/*     */       }
/*     */       
/*     */ 
/* 207 */       LZ4SafeUtils.wildArraycopy(src, anchor, dest, dOff, runLen);
/* 208 */       dOff += runLen;
/*     */       
/*     */       for (;;)
/*     */       {
/* 212 */         SafeUtils.writeShortLE(dest, dOff, back);
/* 213 */         dOff += 2;
/*     */         
/*     */ 
/* 216 */         sOff += 4;
/* 217 */         int matchLen = LZ4SafeUtils.commonBytes(src, ref + 4, sOff, srcLimit);
/* 218 */         if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 219 */           throw new LZ4Exception("maxDestLen is too small");
/*     */         }
/* 221 */         sOff += matchLen;
/*     */         
/*     */ 
/* 224 */         if (matchLen >= 15) {
/* 225 */           SafeUtils.writeByte(dest, tokenOff, SafeUtils.readByte(dest, tokenOff) | 0xF);
/* 226 */           dOff = LZ4SafeUtils.writeLen(matchLen - 15, dest, dOff);
/*     */         } else {
/* 228 */           SafeUtils.writeByte(dest, tokenOff, SafeUtils.readByte(dest, tokenOff) | matchLen);
/*     */         }
/*     */         
/*     */ 
/* 232 */         if (sOff > mflimit) {
/* 233 */           anchor = sOff;
/*     */           
/*     */           break label560;
/*     */         }
/*     */         
/* 238 */         SafeUtils.writeInt(hashTable, LZ4Utils.hash(SafeUtils.readInt(src, sOff - 2)), sOff - 2);
/*     */         
/*     */ 
/* 241 */         int h = LZ4Utils.hash(SafeUtils.readInt(src, sOff));
/* 242 */         ref = SafeUtils.readInt(hashTable, h);
/* 243 */         SafeUtils.writeInt(hashTable, h, sOff);
/* 244 */         back = sOff - ref;
/*     */         
/* 246 */         if ((back >= 65536) || (!LZ4SafeUtils.readIntEquals(src, ref, sOff))) {
/*     */           break;
/*     */         }
/*     */         
/* 250 */         tokenOff = dOff++;
/* 251 */         SafeUtils.writeByte(dest, tokenOff, 0);
/*     */       }
/*     */       
/*     */ 
/* 255 */       anchor = sOff++;
/*     */     }
/*     */     label560:
/* 258 */     dOff = LZ4SafeUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 259 */     return dOff - destOff;
/*     */   }
/*     */   
/*     */   static int compress64k(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int destEnd)
/*     */   {
/* 264 */     int srcEnd = srcOff + srcLen;
/* 265 */     int srcLimit = srcEnd - 5;
/* 266 */     int mflimit = srcEnd - 12;
/*     */     
/* 268 */     int sOff = srcOff;int dOff = destOff;
/*     */     
/* 270 */     int anchor = sOff;
/*     */     
/* 272 */     if (srcLen >= 13)
/*     */     {
/* 274 */       short[] hashTable = new short[' '];
/*     */       
/* 276 */       sOff++;
/*     */       
/*     */ 
/*     */ 
/*     */       for (;;)
/*     */       {
/* 282 */         int forwardOff = sOff;
/*     */         
/*     */ 
/* 285 */         int step = 1;
/* 286 */         int searchMatchNb = 1 << LZ4Constants.SKIP_STRENGTH;
/*     */         int ref;
/* 288 */         do { sOff = forwardOff;
/* 289 */           forwardOff += step;
/* 290 */           step = searchMatchNb++ >>> LZ4Constants.SKIP_STRENGTH;
/*     */           
/* 292 */           if (forwardOff > mflimit) {
/*     */             break;
/*     */           }
/*     */           
/* 296 */           int h = LZ4Utils.hash64k(ByteBufferUtils.readInt(src, sOff));
/* 297 */           ref = srcOff + SafeUtils.readShort(hashTable, h);
/* 298 */           SafeUtils.writeShort(hashTable, h, sOff - srcOff);
/* 299 */         } while (!LZ4ByteBufferUtils.readIntEquals(src, ref, sOff));
/*     */         
/*     */ 
/* 302 */         int excess = LZ4ByteBufferUtils.commonBytesBackward(src, ref, sOff, srcOff, anchor);
/* 303 */         sOff -= excess;
/* 304 */         ref -= excess;
/*     */         
/*     */ 
/* 307 */         int runLen = sOff - anchor;
/*     */         
/*     */ 
/* 310 */         int tokenOff = dOff++;
/*     */         
/* 312 */         if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/* 313 */           throw new LZ4Exception("maxDestLen is too small");
/*     */         }
/*     */         
/* 316 */         if (runLen >= 15) {
/* 317 */           ByteBufferUtils.writeByte(dest, tokenOff, 240);
/* 318 */           dOff = LZ4ByteBufferUtils.writeLen(runLen - 15, dest, dOff);
/*     */         } else {
/* 320 */           ByteBufferUtils.writeByte(dest, tokenOff, runLen << 4);
/*     */         }
/*     */         
/*     */ 
/* 324 */         LZ4ByteBufferUtils.wildArraycopy(src, anchor, dest, dOff, runLen);
/* 325 */         dOff += runLen;
/*     */         
/*     */         for (;;)
/*     */         {
/* 329 */           ByteBufferUtils.writeShortLE(dest, dOff, (short)(sOff - ref));
/* 330 */           dOff += 2;
/*     */           
/*     */ 
/* 333 */           sOff += 4;
/* 334 */           ref += 4;
/* 335 */           int matchLen = LZ4ByteBufferUtils.commonBytes(src, ref, sOff, srcLimit);
/* 336 */           if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 337 */             throw new LZ4Exception("maxDestLen is too small");
/*     */           }
/* 339 */           sOff += matchLen;
/*     */           
/*     */ 
/* 342 */           if (matchLen >= 15) {
/* 343 */             ByteBufferUtils.writeByte(dest, tokenOff, ByteBufferUtils.readByte(dest, tokenOff) | 0xF);
/* 344 */             dOff = LZ4ByteBufferUtils.writeLen(matchLen - 15, dest, dOff);
/*     */           } else {
/* 346 */             ByteBufferUtils.writeByte(dest, tokenOff, ByteBufferUtils.readByte(dest, tokenOff) | matchLen);
/*     */           }
/*     */           
/*     */ 
/* 350 */           if (sOff > mflimit) {
/* 351 */             anchor = sOff;
/*     */             
/*     */             break label494;
/*     */           }
/*     */           
/* 356 */           SafeUtils.writeShort(hashTable, LZ4Utils.hash64k(ByteBufferUtils.readInt(src, sOff - 2)), sOff - 2 - srcOff);
/*     */           
/*     */ 
/* 359 */           int h = LZ4Utils.hash64k(ByteBufferUtils.readInt(src, sOff));
/* 360 */           ref = srcOff + SafeUtils.readShort(hashTable, h);
/* 361 */           SafeUtils.writeShort(hashTable, h, sOff - srcOff);
/*     */           
/* 363 */           if (!LZ4ByteBufferUtils.readIntEquals(src, sOff, ref)) {
/*     */             break;
/*     */           }
/*     */           
/* 367 */           tokenOff = dOff++;
/* 368 */           ByteBufferUtils.writeByte(dest, tokenOff, 0);
/*     */         }
/*     */         
/*     */ 
/* 372 */         anchor = sOff++;
/*     */       }
/*     */     }
/*     */     label494:
/* 376 */     dOff = LZ4ByteBufferUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 377 */     return dOff - destOff;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compress(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int maxDestLen)
/*     */   {
/* 383 */     if ((src.hasArray()) && (dest.hasArray())) {
/* 384 */       return compress(src.array(), srcOff + src.arrayOffset(), srcLen, dest.array(), destOff + dest.arrayOffset(), maxDestLen);
/*     */     }
/* 386 */     src = ByteBufferUtils.inNativeByteOrder(src);
/* 387 */     dest = ByteBufferUtils.inNativeByteOrder(dest);
/*     */     
/* 389 */     ByteBufferUtils.checkRange(src, srcOff, srcLen);
/* 390 */     ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
/* 391 */     int destEnd = destOff + maxDestLen;
/*     */     
/* 393 */     if (srcLen < 65547) {
/* 394 */       return compress64k(src, srcOff, srcLen, dest, destOff, destEnd);
/*     */     }
/*     */     
/* 397 */     int srcEnd = srcOff + srcLen;
/* 398 */     int srcLimit = srcEnd - 5;
/* 399 */     int mflimit = srcEnd - 12;
/*     */     
/* 401 */     int sOff = srcOff;int dOff = destOff;
/* 402 */     int anchor = sOff++;
/*     */     
/* 404 */     int[] hashTable = new int['က'];
/* 405 */     Arrays.fill(hashTable, anchor);
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 411 */       int forwardOff = sOff;
/*     */       
/*     */ 
/* 414 */       int step = 1;
/* 415 */       int searchMatchNb = 1 << LZ4Constants.SKIP_STRENGTH;
/*     */       int ref;
/*     */       int back;
/* 418 */       do { sOff = forwardOff;
/* 419 */         forwardOff += step;
/* 420 */         step = searchMatchNb++ >>> LZ4Constants.SKIP_STRENGTH;
/*     */         
/* 422 */         if (forwardOff > mflimit) {
/*     */           break;
/*     */         }
/*     */         
/* 426 */         int h = LZ4Utils.hash(ByteBufferUtils.readInt(src, sOff));
/* 427 */         ref = SafeUtils.readInt(hashTable, h);
/* 428 */         back = sOff - ref;
/* 429 */         SafeUtils.writeInt(hashTable, h, sOff);
/* 430 */       } while ((back >= 65536) || (!LZ4ByteBufferUtils.readIntEquals(src, ref, sOff)));
/*     */       
/*     */ 
/* 433 */       int excess = LZ4ByteBufferUtils.commonBytesBackward(src, ref, sOff, srcOff, anchor);
/* 434 */       sOff -= excess;
/* 435 */       ref -= excess;
/*     */       
/*     */ 
/* 438 */       int runLen = sOff - anchor;
/*     */       
/*     */ 
/* 441 */       int tokenOff = dOff++;
/*     */       
/* 443 */       if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
/* 444 */         throw new LZ4Exception("maxDestLen is too small");
/*     */       }
/*     */       
/* 447 */       if (runLen >= 15) {
/* 448 */         ByteBufferUtils.writeByte(dest, tokenOff, 240);
/* 449 */         dOff = LZ4ByteBufferUtils.writeLen(runLen - 15, dest, dOff);
/*     */       } else {
/* 451 */         ByteBufferUtils.writeByte(dest, tokenOff, runLen << 4);
/*     */       }
/*     */       
/*     */ 
/* 455 */       LZ4ByteBufferUtils.wildArraycopy(src, anchor, dest, dOff, runLen);
/* 456 */       dOff += runLen;
/*     */       
/*     */       for (;;)
/*     */       {
/* 460 */         ByteBufferUtils.writeShortLE(dest, dOff, back);
/* 461 */         dOff += 2;
/*     */         
/*     */ 
/* 464 */         sOff += 4;
/* 465 */         int matchLen = LZ4ByteBufferUtils.commonBytes(src, ref + 4, sOff, srcLimit);
/* 466 */         if (dOff + 6 + (matchLen >>> 8) > destEnd) {
/* 467 */           throw new LZ4Exception("maxDestLen is too small");
/*     */         }
/* 469 */         sOff += matchLen;
/*     */         
/*     */ 
/* 472 */         if (matchLen >= 15) {
/* 473 */           ByteBufferUtils.writeByte(dest, tokenOff, ByteBufferUtils.readByte(dest, tokenOff) | 0xF);
/* 474 */           dOff = LZ4ByteBufferUtils.writeLen(matchLen - 15, dest, dOff);
/*     */         } else {
/* 476 */           ByteBufferUtils.writeByte(dest, tokenOff, ByteBufferUtils.readByte(dest, tokenOff) | matchLen);
/*     */         }
/*     */         
/*     */ 
/* 480 */         if (sOff > mflimit) {
/* 481 */           anchor = sOff;
/*     */           
/*     */           break label618;
/*     */         }
/*     */         
/* 486 */         SafeUtils.writeInt(hashTable, LZ4Utils.hash(ByteBufferUtils.readInt(src, sOff - 2)), sOff - 2);
/*     */         
/*     */ 
/* 489 */         int h = LZ4Utils.hash(ByteBufferUtils.readInt(src, sOff));
/* 490 */         ref = SafeUtils.readInt(hashTable, h);
/* 491 */         SafeUtils.writeInt(hashTable, h, sOff);
/* 492 */         back = sOff - ref;
/*     */         
/* 494 */         if ((back >= 65536) || (!LZ4ByteBufferUtils.readIntEquals(src, ref, sOff))) {
/*     */           break;
/*     */         }
/*     */         
/* 498 */         tokenOff = dOff++;
/* 499 */         ByteBufferUtils.writeByte(dest, tokenOff, 0);
/*     */       }
/*     */       
/*     */ 
/* 503 */       anchor = sOff++;
/*     */     }
/*     */     label618:
/* 506 */     dOff = LZ4ByteBufferUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 507 */     return dOff - destOff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4JavaSafeCompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */