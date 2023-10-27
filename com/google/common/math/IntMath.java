/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class IntMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int MAX_SIGNED_POWER_OF_TWO = 1073741824;
/*     */   @VisibleForTesting
/*     */   static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
/*     */   
/*     */   @Beta
/*     */   public static int ceilingPowerOfTwo(int x)
/*     */   {
/*  67 */     MathPreconditions.checkPositive("x", x);
/*  68 */     if (x > 1073741824) {
/*  69 */       throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") not representable as an int");
/*     */     }
/*  71 */     return 1 << -Integer.numberOfLeadingZeros(x - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int floorPowerOfTwo(int x)
/*     */   {
/*  83 */     MathPreconditions.checkPositive("x", x);
/*  84 */     return Integer.highestOneBit(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPowerOfTwo(int x)
/*     */   {
/*  94 */     return (x > 0 ? 1 : 0) & ((x & x - 1) == 0 ? 1 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static int lessThanBranchFree(int x, int y)
/*     */   {
/* 106 */     return (x - y ^ 0xFFFFFFFF ^ 0xFFFFFFFF) >>> 31;
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
/*     */   public static int log2(int x, RoundingMode mode)
/*     */   {
/* 119 */     MathPreconditions.checkPositive("x", x);
/* 120 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 122 */       MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */     
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 126 */       return 31 - Integer.numberOfLeadingZeros(x);
/*     */     
/*     */     case UP: 
/*     */     case CEILING: 
/* 130 */       return 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */     
/*     */ 
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 136 */       int leadingZeros = Integer.numberOfLeadingZeros(x);
/* 137 */       int cmp = -1257966797 >>> leadingZeros;
/*     */       
/* 139 */       int logFloor = 31 - leadingZeros;
/* 140 */       return logFloor + lessThanBranchFree(cmp, x);
/*     */     }
/*     */     
/* 143 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   public static int log10(int x, RoundingMode mode)
/*     */   {
/* 160 */     MathPreconditions.checkPositive("x", x);
/* 161 */     int logFloor = log10Floor(x);
/* 162 */     int floorPow = powersOf10[logFloor];
/* 163 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 165 */       MathPreconditions.checkRoundingUnnecessary(x == floorPow);
/*     */     
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 169 */       return logFloor;
/*     */     case UP: 
/*     */     case CEILING: 
/* 172 */       return logFloor + lessThanBranchFree(floorPow, x);
/*     */     
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 177 */       return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*     */     }
/* 179 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int log10Floor(int x)
/*     */   {
/* 191 */     int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 196 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/* 201 */   static final byte[] maxLog10ForLeadingZeros = { 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 207 */   static final int[] powersOf10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 213 */   static final int[] halfPowersOf10 = { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final int FLOOR_SQRT_MAX_INT = 46340;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static int pow(int b, int k)
/*     */   {
/* 228 */     MathPreconditions.checkNonNegative("exponent", k);
/* 229 */     switch (b) {
/*     */     case 0: 
/* 231 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 233 */       return 1;
/*     */     case -1: 
/* 235 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 237 */       return k < 32 ? 1 << k : 0;
/*     */     case -2: 
/* 239 */       if (k < 32) {
/* 240 */         return (k & 0x1) == 0 ? 1 << k : -(1 << k);
/*     */       }
/* 242 */       return 0;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 247 */     for (int accum = 1;; k >>= 1) {
/* 248 */       switch (k) {
/*     */       case 0: 
/* 250 */         return accum;
/*     */       case 1: 
/* 252 */         return b * accum;
/*     */       }
/* 254 */       accum *= ((k & 0x1) == 0 ? 1 : b);
/* 255 */       b *= b;
/*     */     }
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
/*     */   @GwtIncompatible
/*     */   public static int sqrt(int x, RoundingMode mode)
/*     */   {
/* 270 */     MathPreconditions.checkNonNegative("x", x);
/* 271 */     int sqrtFloor = sqrtFloor(x);
/* 272 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 274 */       MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x);
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 277 */       return sqrtFloor;
/*     */     case UP: 
/*     */     case CEILING: 
/* 280 */       return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 284 */       int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
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
/* 296 */       return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*     */     }
/* 298 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int sqrtFloor(int x)
/*     */   {
/* 305 */     return (int)Math.sqrt(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int divide(int p, int q, RoundingMode mode)
/*     */   {
/* 317 */     Preconditions.checkNotNull(mode);
/* 318 */     if (q == 0) {
/* 319 */       throw new ArithmeticException("/ by zero");
/*     */     }
/* 321 */     int div = p / q;
/* 322 */     int rem = p - q * div;
/*     */     
/* 324 */     if (rem == 0) {
/* 325 */       return div;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 335 */     int signum = 0x1 | (p ^ q) >> 31;
/*     */     boolean increment;
/* 337 */     boolean increment; boolean increment; boolean increment; boolean increment; switch (mode) {
/*     */     case UNNECESSARY: 
/* 339 */       MathPreconditions.checkRoundingUnnecessary(rem == 0);
/*     */     
/*     */     case DOWN: 
/* 342 */       increment = false;
/* 343 */       break;
/*     */     case UP: 
/* 345 */       increment = true;
/* 346 */       break;
/*     */     case CEILING: 
/* 348 */       increment = signum > 0;
/* 349 */       break;
/*     */     case FLOOR: 
/* 351 */       increment = signum < 0;
/* 352 */       break;
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 356 */       int absRem = Math.abs(rem);
/* 357 */       int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
/*     */       
/*     */       boolean increment;
/* 360 */       if (cmpRemToHalfDivisor == 0) {
/* 361 */         if (mode != RoundingMode.HALF_UP) {} increment = ((mode == RoundingMode.HALF_EVEN ? 1 : 0) & ((div & 0x1) != 0 ? 1 : 0)) != 0;
/*     */       } else {
/* 363 */         increment = cmpRemToHalfDivisor > 0;
/*     */       }
/* 365 */       break;
/*     */     default: 
/* 367 */       throw new AssertionError(); }
/*     */     boolean increment;
/* 369 */     return increment ? div + signum : div;
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
/*     */   public static int mod(int x, int m)
/*     */   {
/* 391 */     if (m <= 0) {
/* 392 */       throw new ArithmeticException("Modulus " + m + " must be > 0");
/*     */     }
/* 394 */     int result = x % m;
/* 395 */     return result >= 0 ? result : result + m;
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
/*     */   public static int gcd(int a, int b)
/*     */   {
/* 410 */     MathPreconditions.checkNonNegative("a", a);
/* 411 */     MathPreconditions.checkNonNegative("b", b);
/* 412 */     if (a == 0)
/*     */     {
/*     */ 
/* 415 */       return b; }
/* 416 */     if (b == 0) {
/* 417 */       return a;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 423 */     int aTwos = Integer.numberOfTrailingZeros(a);
/* 424 */     a >>= aTwos;
/* 425 */     int bTwos = Integer.numberOfTrailingZeros(b);
/* 426 */     b >>= bTwos;
/* 427 */     while (a != b)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 435 */       int delta = a - b;
/*     */       
/* 437 */       int minDeltaOrZero = delta & delta >> 31;
/*     */       
/*     */ 
/* 440 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*     */       
/*     */ 
/* 443 */       b += minDeltaOrZero;
/* 444 */       a >>= Integer.numberOfTrailingZeros(a);
/*     */     }
/* 446 */     return a << Math.min(aTwos, bTwos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedAdd(int a, int b)
/*     */   {
/* 455 */     long result = a + b;
/* 456 */     MathPreconditions.checkNoOverflow(result == (int)result, "checkedAdd", a, b);
/* 457 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedSubtract(int a, int b)
/*     */   {
/* 466 */     long result = a - b;
/* 467 */     MathPreconditions.checkNoOverflow(result == (int)result, "checkedSubtract", a, b);
/* 468 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedMultiply(int a, int b)
/*     */   {
/* 477 */     long result = a * b;
/* 478 */     MathPreconditions.checkNoOverflow(result == (int)result, "checkedMultiply", a, b);
/* 479 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedPow(int b, int k)
/*     */   {
/* 491 */     MathPreconditions.checkNonNegative("exponent", k);
/* 492 */     switch (b) {
/*     */     case 0: 
/* 494 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 496 */       return 1;
/*     */     case -1: 
/* 498 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 500 */       MathPreconditions.checkNoOverflow(k < 31, "checkedPow", b, k);
/* 501 */       return 1 << k;
/*     */     case -2: 
/* 503 */       MathPreconditions.checkNoOverflow(k < 32, "checkedPow", b, k);
/* 504 */       return (k & 0x1) == 0 ? 1 << k : -1 << k;
/*     */     }
/*     */     
/*     */     
/* 508 */     int accum = 1;
/*     */     for (;;) {
/* 510 */       switch (k) {
/*     */       case 0: 
/* 512 */         return accum;
/*     */       case 1: 
/* 514 */         return checkedMultiply(accum, b);
/*     */       }
/* 516 */       if ((k & 0x1) != 0) {
/* 517 */         accum = checkedMultiply(accum, b);
/*     */       }
/* 519 */       k >>= 1;
/* 520 */       if (k > 0) {
/* 521 */         MathPreconditions.checkNoOverflow((-46340 <= b ? 1 : 0) & (b <= 46340 ? 1 : 0), "checkedPow", b, k);
/* 522 */         b *= b;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedAdd(int a, int b)
/*     */   {
/* 536 */     return Ints.saturatedCast(a + b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedSubtract(int a, int b)
/*     */   {
/* 547 */     return Ints.saturatedCast(a - b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedMultiply(int a, int b)
/*     */   {
/* 558 */     return Ints.saturatedCast(a * b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedPow(int b, int k)
/*     */   {
/* 569 */     MathPreconditions.checkNonNegative("exponent", k);
/* 570 */     switch (b) {
/*     */     case 0: 
/* 572 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 574 */       return 1;
/*     */     case -1: 
/* 576 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 578 */       if (k >= 31) {
/* 579 */         return Integer.MAX_VALUE;
/*     */       }
/* 581 */       return 1 << k;
/*     */     case -2: 
/* 583 */       if (k >= 32) {
/* 584 */         return Integer.MAX_VALUE + (k & 0x1);
/*     */       }
/* 586 */       return (k & 0x1) == 0 ? 1 << k : -1 << k;
/*     */     }
/*     */     
/*     */     
/* 590 */     int accum = 1;
/*     */     
/* 592 */     int limit = Integer.MAX_VALUE + (b >>> 31 & k & 0x1);
/*     */     for (;;) {
/* 594 */       switch (k) {
/*     */       case 0: 
/* 596 */         return accum;
/*     */       case 1: 
/* 598 */         return saturatedMultiply(accum, b);
/*     */       }
/* 600 */       if ((k & 0x1) != 0) {
/* 601 */         accum = saturatedMultiply(accum, b);
/*     */       }
/* 603 */       k >>= 1;
/* 604 */       if (k > 0) {
/* 605 */         if (((-46340 > b ? 1 : 0) | (b > 46340 ? 1 : 0)) != 0) {
/* 606 */           return limit;
/*     */         }
/* 608 */         b *= b;
/*     */       }
/*     */     }
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
/*     */   public static int factorial(int n)
/*     */   {
/* 623 */     MathPreconditions.checkNonNegative("n", n);
/* 624 */     return n < factorials.length ? factorials[n] : Integer.MAX_VALUE;
/*     */   }
/*     */   
/* 627 */   private static final int[] factorials = { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600 };
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
/*     */   public static int binomial(int n, int k)
/*     */   {
/* 650 */     MathPreconditions.checkNonNegative("n", n);
/* 651 */     MathPreconditions.checkNonNegative("k", k);
/* 652 */     Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
/* 653 */     if (k > n >> 1) {
/* 654 */       k = n - k;
/*     */     }
/* 656 */     if ((k >= biggestBinomials.length) || (n > biggestBinomials[k])) {
/* 657 */       return Integer.MAX_VALUE;
/*     */     }
/* 659 */     switch (k) {
/*     */     case 0: 
/* 661 */       return 1;
/*     */     case 1: 
/* 663 */       return n;
/*     */     }
/* 665 */     long result = 1L;
/* 666 */     for (int i = 0; i < k; i++) {
/* 667 */       result *= (n - i);
/* 668 */       result /= (i + 1);
/*     */     }
/* 670 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 676 */   static int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33 };
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
/*     */   public static int mean(int x, int y)
/*     */   {
/* 706 */     return (x & y) + ((x ^ y) >> 1);
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
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean isPrime(int n)
/*     */   {
/* 724 */     return LongMath.isPrime(n);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\math\IntMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */