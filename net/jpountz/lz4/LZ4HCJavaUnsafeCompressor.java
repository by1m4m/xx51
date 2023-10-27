/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import net.jpountz.util.ByteBufferUtils;
/*     */ import net.jpountz.util.UnsafeUtils;
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
/*     */ final class LZ4HCJavaUnsafeCompressor
/*     */   extends LZ4Compressor
/*     */ {
/*  20 */   public static final LZ4Compressor INSTANCE = new LZ4HCJavaUnsafeCompressor();
/*     */   
/*     */   private final int maxAttempts;
/*     */   final int compressionLevel;
/*     */   
/*  25 */   LZ4HCJavaUnsafeCompressor() { this(9); }
/*     */   
/*  27 */   LZ4HCJavaUnsafeCompressor(int compressionLevel) { this.maxAttempts = (1 << compressionLevel - 1);
/*  28 */     this.compressionLevel = compressionLevel;
/*     */   }
/*     */   
/*     */   private class HashTable {
/*     */     static final int MASK = 65535;
/*     */     int nextToUpdate;
/*     */     private final int base;
/*     */     private final int[] hashTable;
/*     */     private final short[] chainTable;
/*     */     
/*     */     HashTable(int base) {
/*  39 */       this.base = base;
/*  40 */       this.nextToUpdate = base;
/*  41 */       this.hashTable = new int[32768];
/*  42 */       Arrays.fill(this.hashTable, -1);
/*  43 */       this.chainTable = new short[65536];
/*     */     }
/*     */     
/*     */     private int hashPointer(byte[] bytes, int off) {
/*  47 */       int v = UnsafeUtils.readInt(bytes, off);
/*  48 */       return hashPointer(v);
/*     */     }
/*     */     
/*     */     private int hashPointer(ByteBuffer bytes, int off) {
/*  52 */       int v = ByteBufferUtils.readInt(bytes, off);
/*  53 */       return hashPointer(v);
/*     */     }
/*     */     
/*     */     private int hashPointer(int v) {
/*  57 */       int h = LZ4Utils.hashHC(v);
/*  58 */       return this.hashTable[h];
/*     */     }
/*     */     
/*     */     private int next(int off) {
/*  62 */       return off - (this.chainTable[(off & 0xFFFF)] & 0xFFFF);
/*     */     }
/*     */     
/*     */     private void addHash(byte[] bytes, int off) {
/*  66 */       int v = UnsafeUtils.readInt(bytes, off);
/*  67 */       addHash(v, off);
/*     */     }
/*     */     
/*     */     private void addHash(ByteBuffer bytes, int off) {
/*  71 */       int v = ByteBufferUtils.readInt(bytes, off);
/*  72 */       addHash(v, off);
/*     */     }
/*     */     
/*     */     private void addHash(int v, int off) {
/*  76 */       int h = LZ4Utils.hashHC(v);
/*  77 */       int delta = off - this.hashTable[h];
/*  78 */       assert (delta > 0) : delta;
/*  79 */       if (delta >= 65536) {
/*  80 */         delta = 65535;
/*     */       }
/*  82 */       this.chainTable[(off & 0xFFFF)] = ((short)delta);
/*  83 */       this.hashTable[h] = off;
/*     */     }
/*     */     
/*     */     void insert(int off, byte[] bytes) {
/*  87 */       for (; this.nextToUpdate < off; this.nextToUpdate += 1) {
/*  88 */         addHash(bytes, this.nextToUpdate);
/*     */       }
/*     */     }
/*     */     
/*     */     void insert(int off, ByteBuffer bytes) {
/*  93 */       for (; this.nextToUpdate < off; this.nextToUpdate += 1) {
/*  94 */         addHash(bytes, this.nextToUpdate);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     boolean insertAndFindBestMatch(byte[] buf, int off, int matchLimit, LZ4Utils.Match match)
/*     */     {
/* 101 */       match.start = off;
/* 102 */       match.len = 0;
/* 103 */       int delta = 0;
/* 104 */       int repl = 0;
/*     */       
/* 106 */       insert(off, buf);
/*     */       
/* 108 */       int ref = hashPointer(buf, off);
/*     */       
/* 110 */       if ((ref >= off - 4) && (ref <= off) && (ref >= this.base)) {
/* 111 */         if (LZ4UnsafeUtils.readIntEquals(buf, ref, off)) {
/* 112 */           delta = off - ref;
/* 113 */           repl = match.len = 4 + LZ4UnsafeUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 114 */           match.ref = ref;
/*     */         }
/* 116 */         ref = next(ref);
/*     */       }
/*     */       
/* 119 */       for (int i = 0; i < LZ4HCJavaUnsafeCompressor.this.maxAttempts; i++) {
/* 120 */         if ((ref < Math.max(this.base, off - 65536 + 1)) || (ref > off)) {
/*     */           break;
/*     */         }
/* 123 */         if (LZ4UnsafeUtils.readIntEquals(buf, ref, off)) {
/* 124 */           int matchLen = 4 + LZ4UnsafeUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 125 */           if (matchLen > match.len) {
/* 126 */             match.ref = ref;
/* 127 */             match.len = matchLen;
/*     */           }
/*     */         }
/* 130 */         ref = next(ref);
/*     */       }
/*     */       
/* 133 */       if (repl != 0) {
/* 134 */         int ptr = off;
/* 135 */         int end = off + repl - 3;
/* 136 */         while (ptr < end - delta) {
/* 137 */           this.chainTable[(ptr & 0xFFFF)] = ((short)delta);
/* 138 */           ptr++;
/*     */         }
/*     */         do {
/* 141 */           this.chainTable[(ptr & 0xFFFF)] = ((short)delta);
/* 142 */           this.hashTable[LZ4Utils.hashHC(UnsafeUtils.readInt(buf, ptr))] = ptr;
/* 143 */           ptr++;
/* 144 */         } while (ptr < end);
/* 145 */         this.nextToUpdate = end;
/*     */       }
/*     */       
/* 148 */       return match.len != 0;
/*     */     }
/*     */     
/*     */     boolean insertAndFindWiderMatch(byte[] buf, int off, int startLimit, int matchLimit, int minLen, LZ4Utils.Match match) {
/* 152 */       match.len = minLen;
/*     */       
/* 154 */       insert(off, buf);
/*     */       
/* 156 */       int delta = off - startLimit;
/* 157 */       int ref = hashPointer(buf, off);
/* 158 */       for (int i = 0; i < LZ4HCJavaUnsafeCompressor.this.maxAttempts; i++) {
/* 159 */         if ((ref < Math.max(this.base, off - 65536 + 1)) || (ref > off)) {
/*     */           break;
/*     */         }
/* 162 */         if (LZ4UnsafeUtils.readIntEquals(buf, ref, off)) {
/* 163 */           int matchLenForward = 4 + LZ4UnsafeUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 164 */           int matchLenBackward = LZ4UnsafeUtils.commonBytesBackward(buf, ref, off, this.base, startLimit);
/* 165 */           int matchLen = matchLenBackward + matchLenForward;
/* 166 */           if (matchLen > match.len) {
/* 167 */             match.len = matchLen;
/* 168 */             match.ref = (ref - matchLenBackward);
/* 169 */             match.start = (off - matchLenBackward);
/*     */           }
/*     */         }
/* 172 */         ref = next(ref);
/*     */       }
/*     */       
/* 175 */       return match.len > minLen;
/*     */     }
/*     */     
/*     */     boolean insertAndFindBestMatch(ByteBuffer buf, int off, int matchLimit, LZ4Utils.Match match)
/*     */     {
/* 180 */       match.start = off;
/* 181 */       match.len = 0;
/* 182 */       int delta = 0;
/* 183 */       int repl = 0;
/*     */       
/* 185 */       insert(off, buf);
/*     */       
/* 187 */       int ref = hashPointer(buf, off);
/*     */       
/* 189 */       if ((ref >= off - 4) && (ref <= off) && (ref >= this.base)) {
/* 190 */         if (LZ4ByteBufferUtils.readIntEquals(buf, ref, off)) {
/* 191 */           delta = off - ref;
/* 192 */           repl = match.len = 4 + LZ4ByteBufferUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 193 */           match.ref = ref;
/*     */         }
/* 195 */         ref = next(ref);
/*     */       }
/*     */       
/* 198 */       for (int i = 0; i < LZ4HCJavaUnsafeCompressor.this.maxAttempts; i++) {
/* 199 */         if ((ref < Math.max(this.base, off - 65536 + 1)) || (ref > off)) {
/*     */           break;
/*     */         }
/* 202 */         if (LZ4ByteBufferUtils.readIntEquals(buf, ref, off)) {
/* 203 */           int matchLen = 4 + LZ4ByteBufferUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 204 */           if (matchLen > match.len) {
/* 205 */             match.ref = ref;
/* 206 */             match.len = matchLen;
/*     */           }
/*     */         }
/* 209 */         ref = next(ref);
/*     */       }
/*     */       
/* 212 */       if (repl != 0) {
/* 213 */         int ptr = off;
/* 214 */         int end = off + repl - 3;
/* 215 */         while (ptr < end - delta) {
/* 216 */           this.chainTable[(ptr & 0xFFFF)] = ((short)delta);
/* 217 */           ptr++;
/*     */         }
/*     */         do {
/* 220 */           this.chainTable[(ptr & 0xFFFF)] = ((short)delta);
/* 221 */           this.hashTable[LZ4Utils.hashHC(ByteBufferUtils.readInt(buf, ptr))] = ptr;
/* 222 */           ptr++;
/* 223 */         } while (ptr < end);
/* 224 */         this.nextToUpdate = end;
/*     */       }
/*     */       
/* 227 */       return match.len != 0;
/*     */     }
/*     */     
/*     */     boolean insertAndFindWiderMatch(ByteBuffer buf, int off, int startLimit, int matchLimit, int minLen, LZ4Utils.Match match) {
/* 231 */       match.len = minLen;
/*     */       
/* 233 */       insert(off, buf);
/*     */       
/* 235 */       int delta = off - startLimit;
/* 236 */       int ref = hashPointer(buf, off);
/* 237 */       for (int i = 0; i < LZ4HCJavaUnsafeCompressor.this.maxAttempts; i++) {
/* 238 */         if ((ref < Math.max(this.base, off - 65536 + 1)) || (ref > off)) {
/*     */           break;
/*     */         }
/* 241 */         if (LZ4ByteBufferUtils.readIntEquals(buf, ref, off)) {
/* 242 */           int matchLenForward = 4 + LZ4ByteBufferUtils.commonBytes(buf, ref + 4, off + 4, matchLimit);
/* 243 */           int matchLenBackward = LZ4ByteBufferUtils.commonBytesBackward(buf, ref, off, this.base, startLimit);
/* 244 */           int matchLen = matchLenBackward + matchLenForward;
/* 245 */           if (matchLen > match.len) {
/* 246 */             match.len = matchLen;
/* 247 */             match.ref = (ref - matchLenBackward);
/* 248 */             match.start = (off - matchLenBackward);
/*     */           }
/*     */         }
/* 251 */         ref = next(ref);
/*     */       }
/*     */       
/* 254 */       return match.len > minLen;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compress(byte[] src, int srcOff, int srcLen, byte[] dest, int destOff, int maxDestLen)
/*     */   {
/* 263 */     UnsafeUtils.checkRange(src, srcOff, srcLen);
/* 264 */     UnsafeUtils.checkRange(dest, destOff, maxDestLen);
/*     */     
/* 266 */     int srcEnd = srcOff + srcLen;
/* 267 */     int destEnd = destOff + maxDestLen;
/* 268 */     int mfLimit = srcEnd - 12;
/* 269 */     int matchLimit = srcEnd - 5;
/*     */     
/* 271 */     int sOff = srcOff;
/* 272 */     int dOff = destOff;
/* 273 */     int anchor = sOff++;
/*     */     
/* 275 */     HashTable ht = new HashTable(srcOff);
/* 276 */     LZ4Utils.Match match0 = new LZ4Utils.Match();
/* 277 */     LZ4Utils.Match match1 = new LZ4Utils.Match();
/* 278 */     LZ4Utils.Match match2 = new LZ4Utils.Match();
/* 279 */     LZ4Utils.Match match3 = new LZ4Utils.Match();
/*     */     
/*     */ 
/* 282 */     while (sOff < mfLimit) {
/* 283 */       if (!ht.insertAndFindBestMatch(src, sOff, matchLimit, match1)) {
/* 284 */         sOff++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 289 */         LZ4Utils.copyTo(match1, match0);
/*     */         for (;;)
/*     */         {
/*     */           label136:
/* 293 */           assert (match1.start >= anchor);
/* 294 */           if ((match1.end() >= mfLimit) || (!ht.insertAndFindWiderMatch(src, match1.end() - 2, match1.start + 1, matchLimit, match1.len, match2)))
/*     */           {
/*     */ 
/* 297 */             dOff = LZ4UnsafeUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 298 */             anchor = sOff = match1.end();
/* 299 */             break;
/*     */           }
/*     */           
/* 302 */           if ((match0.start < match1.start) && 
/* 303 */             (match2.start < match1.start + match0.len)) {
/* 304 */             LZ4Utils.copyTo(match0, match1);
/*     */           }
/*     */           
/* 307 */           assert (match2.start > match1.start);
/*     */           
/* 309 */           if (match2.start - match1.start >= 3) break label335;
/* 310 */           LZ4Utils.copyTo(match2, match1);
/*     */         }
/*     */         
/*     */         for (;;)
/*     */         {
/*     */           label335:
/* 316 */           if (match2.start - match1.start < 18) {
/* 317 */             int newMatchLen = match1.len;
/* 318 */             if (newMatchLen > 18) {
/* 319 */               newMatchLen = 18;
/*     */             }
/* 321 */             if (match1.start + newMatchLen > match2.end() - 4) {
/* 322 */               newMatchLen = match2.start - match1.start + match2.len - 4;
/*     */             }
/* 324 */             int correction = newMatchLen - (match2.start - match1.start);
/* 325 */             if (correction > 0) {
/* 326 */               match2.fix(correction);
/*     */             }
/*     */           }
/*     */           
/* 330 */           if ((match2.start + match2.len >= mfLimit) || (!ht.insertAndFindWiderMatch(src, match2.end() - 3, match2.start, matchLimit, match2.len, match3)))
/*     */           {
/*     */ 
/* 333 */             if (match2.start < match1.end()) {
/* 334 */               match1.len = (match2.start - match1.start);
/*     */             }
/*     */             
/* 337 */             dOff = LZ4UnsafeUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 338 */             anchor = sOff = match1.end();
/*     */             
/* 340 */             dOff = LZ4UnsafeUtils.encodeSequence(src, anchor, match2.start, match2.ref, match2.len, dest, dOff, destEnd);
/* 341 */             anchor = sOff = match2.end();
/* 342 */             break;
/*     */           }
/*     */           
/* 345 */           if (match3.start < match1.end() + 3) {
/* 346 */             if (match3.start >= match1.end()) {
/* 347 */               if (match2.start < match1.end()) {
/* 348 */                 int correction = match1.end() - match2.start;
/* 349 */                 match2.fix(correction);
/* 350 */                 if (match2.len < 4) {
/* 351 */                   LZ4Utils.copyTo(match3, match2);
/*     */                 }
/*     */               }
/*     */               
/* 355 */               dOff = LZ4UnsafeUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 356 */               anchor = sOff = match1.end();
/*     */               
/* 358 */               LZ4Utils.copyTo(match3, match1);
/* 359 */               LZ4Utils.copyTo(match2, match0);
/*     */               
/*     */               break label136;
/*     */             }
/*     */             
/* 364 */             LZ4Utils.copyTo(match3, match2);
/* 365 */             continue;
/*     */           }
/*     */           
/*     */ 
/* 369 */           if (match2.start < match1.end()) {
/* 370 */             if (match2.start - match1.start < 15) {
/* 371 */               if (match1.len > 18) {
/* 372 */                 match1.len = 18;
/*     */               }
/* 374 */               if (match1.end() > match2.end() - 4) {
/* 375 */                 match1.len = (match2.end() - match1.start - 4);
/*     */               }
/* 377 */               int correction = match1.end() - match2.start;
/* 378 */               match2.fix(correction);
/*     */             } else {
/* 380 */               match1.len = (match2.start - match1.start);
/*     */             }
/*     */           }
/*     */           
/* 384 */           dOff = LZ4UnsafeUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 385 */           anchor = sOff = match1.end();
/*     */           
/* 387 */           LZ4Utils.copyTo(match2, match1);
/* 388 */           LZ4Utils.copyTo(match3, match2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */     dOff = LZ4UnsafeUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 398 */     return dOff - destOff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int compress(ByteBuffer src, int srcOff, int srcLen, ByteBuffer dest, int destOff, int maxDestLen)
/*     */   {
/* 405 */     if ((src.hasArray()) && (dest.hasArray())) {
/* 406 */       return compress(src.array(), srcOff + src.arrayOffset(), srcLen, dest.array(), destOff + dest.arrayOffset(), maxDestLen);
/*     */     }
/* 408 */     src = ByteBufferUtils.inNativeByteOrder(src);
/* 409 */     dest = ByteBufferUtils.inNativeByteOrder(dest);
/*     */     
/* 411 */     ByteBufferUtils.checkRange(src, srcOff, srcLen);
/* 412 */     ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
/*     */     
/* 414 */     int srcEnd = srcOff + srcLen;
/* 415 */     int destEnd = destOff + maxDestLen;
/* 416 */     int mfLimit = srcEnd - 12;
/* 417 */     int matchLimit = srcEnd - 5;
/*     */     
/* 419 */     int sOff = srcOff;
/* 420 */     int dOff = destOff;
/* 421 */     int anchor = sOff++;
/*     */     
/* 423 */     HashTable ht = new HashTable(srcOff);
/* 424 */     LZ4Utils.Match match0 = new LZ4Utils.Match();
/* 425 */     LZ4Utils.Match match1 = new LZ4Utils.Match();
/* 426 */     LZ4Utils.Match match2 = new LZ4Utils.Match();
/* 427 */     LZ4Utils.Match match3 = new LZ4Utils.Match();
/*     */     
/*     */ 
/* 430 */     while (sOff < mfLimit) {
/* 431 */       if (!ht.insertAndFindBestMatch(src, sOff, matchLimit, match1)) {
/* 432 */         sOff++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 437 */         LZ4Utils.copyTo(match1, match0);
/*     */         for (;;)
/*     */         {
/*     */           label194:
/* 441 */           assert (match1.start >= anchor);
/* 442 */           if ((match1.end() >= mfLimit) || (!ht.insertAndFindWiderMatch(src, match1.end() - 2, match1.start + 1, matchLimit, match1.len, match2)))
/*     */           {
/*     */ 
/* 445 */             dOff = LZ4ByteBufferUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 446 */             anchor = sOff = match1.end();
/* 447 */             break;
/*     */           }
/*     */           
/* 450 */           if ((match0.start < match1.start) && 
/* 451 */             (match2.start < match1.start + match0.len)) {
/* 452 */             LZ4Utils.copyTo(match0, match1);
/*     */           }
/*     */           
/* 455 */           assert (match2.start > match1.start);
/*     */           
/* 457 */           if (match2.start - match1.start >= 3) break label393;
/* 458 */           LZ4Utils.copyTo(match2, match1);
/*     */         }
/*     */         
/*     */         for (;;)
/*     */         {
/*     */           label393:
/* 464 */           if (match2.start - match1.start < 18) {
/* 465 */             int newMatchLen = match1.len;
/* 466 */             if (newMatchLen > 18) {
/* 467 */               newMatchLen = 18;
/*     */             }
/* 469 */             if (match1.start + newMatchLen > match2.end() - 4) {
/* 470 */               newMatchLen = match2.start - match1.start + match2.len - 4;
/*     */             }
/* 472 */             int correction = newMatchLen - (match2.start - match1.start);
/* 473 */             if (correction > 0) {
/* 474 */               match2.fix(correction);
/*     */             }
/*     */           }
/*     */           
/* 478 */           if ((match2.start + match2.len >= mfLimit) || (!ht.insertAndFindWiderMatch(src, match2.end() - 3, match2.start, matchLimit, match2.len, match3)))
/*     */           {
/*     */ 
/* 481 */             if (match2.start < match1.end()) {
/* 482 */               match1.len = (match2.start - match1.start);
/*     */             }
/*     */             
/* 485 */             dOff = LZ4ByteBufferUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 486 */             anchor = sOff = match1.end();
/*     */             
/* 488 */             dOff = LZ4ByteBufferUtils.encodeSequence(src, anchor, match2.start, match2.ref, match2.len, dest, dOff, destEnd);
/* 489 */             anchor = sOff = match2.end();
/* 490 */             break;
/*     */           }
/*     */           
/* 493 */           if (match3.start < match1.end() + 3) {
/* 494 */             if (match3.start >= match1.end()) {
/* 495 */               if (match2.start < match1.end()) {
/* 496 */                 int correction = match1.end() - match2.start;
/* 497 */                 match2.fix(correction);
/* 498 */                 if (match2.len < 4) {
/* 499 */                   LZ4Utils.copyTo(match3, match2);
/*     */                 }
/*     */               }
/*     */               
/* 503 */               dOff = LZ4ByteBufferUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 504 */               anchor = sOff = match1.end();
/*     */               
/* 506 */               LZ4Utils.copyTo(match3, match1);
/* 507 */               LZ4Utils.copyTo(match2, match0);
/*     */               
/*     */               break label194;
/*     */             }
/*     */             
/* 512 */             LZ4Utils.copyTo(match3, match2);
/* 513 */             continue;
/*     */           }
/*     */           
/*     */ 
/* 517 */           if (match2.start < match1.end()) {
/* 518 */             if (match2.start - match1.start < 15) {
/* 519 */               if (match1.len > 18) {
/* 520 */                 match1.len = 18;
/*     */               }
/* 522 */               if (match1.end() > match2.end() - 4) {
/* 523 */                 match1.len = (match2.end() - match1.start - 4);
/*     */               }
/* 525 */               int correction = match1.end() - match2.start;
/* 526 */               match2.fix(correction);
/*     */             } else {
/* 528 */               match1.len = (match2.start - match1.start);
/*     */             }
/*     */           }
/*     */           
/* 532 */           dOff = LZ4ByteBufferUtils.encodeSequence(src, anchor, match1.start, match1.ref, match1.len, dest, dOff, destEnd);
/* 533 */           anchor = sOff = match1.end();
/*     */           
/* 535 */           LZ4Utils.copyTo(match2, match1);
/* 536 */           LZ4Utils.copyTo(match3, match2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 545 */     dOff = LZ4ByteBufferUtils.lastLiterals(src, anchor, srcEnd - anchor, dest, dOff, destEnd);
/* 546 */     return dOff - destOff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4HCJavaUnsafeCompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */