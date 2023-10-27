/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
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
/*     */  enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  45 */   MURMUR128_MITZ_32, 
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
/*  93 */   MURMUR128_MITZ_64;
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
/*     */   private BloomFilterStrategies() {}
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
/*     */   static final class LockFreeBitArray
/*     */   {
/*     */     private static final int LONG_ADDRESSABLE_BITS = 6;
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
/*     */     final AtomicLongArray data;
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
/*     */     private final LongAddable bitCount;
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
/*     */     LockFreeBitArray(long bits)
/*     */     {
/* 154 */       this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
/*     */     }
/*     */     
/*     */     LockFreeBitArray(long[] data)
/*     */     {
/* 159 */       Preconditions.checkArgument(data.length > 0, "data length is zero!");
/* 160 */       this.data = new AtomicLongArray(data);
/* 161 */       this.bitCount = LongAddables.create();
/* 162 */       long bitCount = 0L;
/* 163 */       for (long value : data) {
/* 164 */         bitCount += Long.bitCount(value);
/*     */       }
/* 166 */       this.bitCount.add(bitCount);
/*     */     }
/*     */     
/*     */     boolean set(long bitIndex)
/*     */     {
/* 171 */       if (get(bitIndex)) {
/* 172 */         return false;
/*     */       }
/*     */       
/* 175 */       int longIndex = (int)(bitIndex >>> 6);
/* 176 */       long mask = 1L << (int)bitIndex;
/*     */       long oldValue;
/*     */       long newValue;
/*     */       do
/*     */       {
/* 181 */         oldValue = this.data.get(longIndex);
/* 182 */         newValue = oldValue | mask;
/* 183 */         if (oldValue == newValue) {
/* 184 */           return false;
/*     */         }
/* 186 */       } while (!this.data.compareAndSet(longIndex, oldValue, newValue));
/*     */       
/*     */ 
/* 189 */       this.bitCount.increment();
/* 190 */       return true;
/*     */     }
/*     */     
/*     */     boolean get(long bitIndex) {
/* 194 */       return (this.data.get((int)(bitIndex >>> 6)) & 1L << (int)bitIndex) != 0L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static long[] toPlainArray(AtomicLongArray atomicLongArray)
/*     */     {
/* 203 */       long[] array = new long[atomicLongArray.length()];
/* 204 */       for (int i = 0; i < array.length; i++) {
/* 205 */         array[i] = atomicLongArray.get(i);
/*     */       }
/* 207 */       return array;
/*     */     }
/*     */     
/*     */     long bitSize()
/*     */     {
/* 212 */       return this.data.length() * 64L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     long bitCount()
/*     */     {
/* 224 */       return this.bitCount.sum();
/*     */     }
/*     */     
/*     */     LockFreeBitArray copy() {
/* 228 */       return new LockFreeBitArray(toPlainArray(this.data));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void putAll(LockFreeBitArray other)
/*     */     {
/* 241 */       Preconditions.checkArgument(
/* 242 */         this.data.length() == other.data.length(), "BitArrays must be of equal length (%s != %s)", this.data
/*     */         
/* 244 */         .length(), other.data
/* 245 */         .length());
/* 246 */       for (int i = 0; i < this.data.length(); i++) {
/* 247 */         long otherLong = other.data.get(i);
/*     */         
/*     */ 
/*     */ 
/* 251 */         boolean changedAnyBits = true;
/*     */         long ourLongOld;
/* 253 */         long ourLongNew; do { ourLongOld = this.data.get(i);
/* 254 */           ourLongNew = ourLongOld | otherLong;
/* 255 */           if (ourLongOld == ourLongNew) {
/* 256 */             changedAnyBits = false;
/* 257 */             break;
/*     */           }
/* 259 */         } while (!this.data.compareAndSet(i, ourLongOld, ourLongNew));
/*     */         
/* 261 */         if (changedAnyBits) {
/* 262 */           int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
/* 263 */           this.bitCount.add(bitsAdded);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 270 */       if ((o instanceof LockFreeBitArray)) {
/* 271 */         LockFreeBitArray lockFreeBitArray = (LockFreeBitArray)o;
/*     */         
/* 273 */         return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
/*     */       }
/* 275 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 281 */       return Arrays.hashCode(toPlainArray(this.data));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */