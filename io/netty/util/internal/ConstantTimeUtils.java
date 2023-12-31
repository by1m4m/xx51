/*     */ package io.netty.util.internal;
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
/*     */ public final class ConstantTimeUtils
/*     */ {
/*     */   public static int equalsConstantTime(int x, int y)
/*     */   {
/*  37 */     int z = 0xFFFFFFFF ^ x ^ y;
/*  38 */     z &= z >> 16;
/*  39 */     z &= z >> 8;
/*  40 */     z &= z >> 4;
/*  41 */     z &= z >> 2;
/*  42 */     z &= z >> 1;
/*  43 */     return z & 0x1;
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
/*     */   public static int equalsConstantTime(long x, long y)
/*     */   {
/*  62 */     long z = 0xFFFFFFFFFFFFFFFF ^ x ^ y;
/*  63 */     z &= z >> 32;
/*  64 */     z &= z >> 16;
/*  65 */     z &= z >> 8;
/*  66 */     z &= z >> 4;
/*  67 */     z &= z >> 2;
/*  68 */     z &= z >> 1;
/*  69 */     return (int)(z & 1L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length)
/*     */   {
/*  96 */     int b = 0;
/*  97 */     int end = startPos1 + length;
/*  98 */     for (; startPos1 < end; startPos2++) {
/*  99 */       b |= bytes1[startPos1] ^ bytes2[startPos2];startPos1++;
/*     */     }
/* 101 */     return equalsConstantTime(b, 0);
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
/*     */   public static int equalsConstantTime(CharSequence s1, CharSequence s2)
/*     */   {
/* 120 */     if (s1.length() != s2.length()) {
/* 121 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 125 */     int c = 0;
/* 126 */     for (int i = 0; i < s1.length(); i++) {
/* 127 */       c |= s1.charAt(i) ^ s2.charAt(i);
/*     */     }
/* 129 */     return equalsConstantTime(c, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\ConstantTimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */