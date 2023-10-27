/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public class AtomicBiInteger
/*     */   extends AtomicLong
/*     */ {
/*     */   public int getHi()
/*     */   {
/*  34 */     return getHi(get());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLo()
/*     */   {
/*  42 */     return getLo(get());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int setHi(int hi)
/*     */   {
/*     */     for (;;)
/*     */     {
/*  55 */       long encoded = get();
/*  56 */       long update = encodeHi(encoded, hi);
/*  57 */       if (compareAndSet(encoded, update)) {
/*  58 */         return getHi(encoded);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int setLo(int lo)
/*     */   {
/*     */     for (;;)
/*     */     {
/*  71 */       long encoded = get();
/*  72 */       long update = encodeLo(encoded, lo);
/*  73 */       if (compareAndSet(encoded, update)) {
/*  74 */         return getLo(encoded);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(int hi, int lo)
/*     */   {
/*  85 */     set(encode(hi, lo));
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
/*     */   public boolean compareAndSetHi(int expect, int hi)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 101 */       long encoded = get();
/* 102 */       if (getHi(encoded) != expect)
/* 103 */         return false;
/* 104 */       long update = encodeHi(encoded, hi);
/* 105 */       if (compareAndSet(encoded, update)) {
/* 106 */         return true;
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
/*     */   public boolean compareAndSetLo(int expect, int lo)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 123 */       long encoded = get();
/* 124 */       if (getLo(encoded) != expect)
/* 125 */         return false;
/* 126 */       long update = encodeLo(encoded, lo);
/* 127 */       if (compareAndSet(encoded, update)) {
/* 128 */         return true;
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
/*     */   public boolean compareAndSet(long expect, int hi, int lo)
/*     */   {
/* 143 */     long encoded = get();
/* 144 */     long update = encode(hi, lo);
/* 145 */     return compareAndSet(encoded, update);
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
/*     */   public boolean compareAndSet(int expectHi, int hi, int expectLo, int lo)
/*     */   {
/* 160 */     long encoded = encode(expectHi, expectLo);
/* 161 */     long update = encode(hi, lo);
/* 162 */     return compareAndSet(encoded, update);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int updateHi(int delta)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 176 */       long encoded = get();
/* 177 */       int hi = getHi(encoded) + delta;
/* 178 */       long update = encodeHi(encoded, hi);
/* 179 */       if (compareAndSet(encoded, update)) {
/* 180 */         return hi;
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
/*     */   public int updateLo(int delta)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 195 */       long encoded = get();
/* 196 */       int lo = getLo(encoded) + delta;
/* 197 */       long update = encodeLo(encoded, lo);
/* 198 */       if (compareAndSet(encoded, update)) {
/* 199 */         return lo;
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
/*     */   public void update(int deltaHi, int deltaLo)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 214 */       long encoded = get();
/* 215 */       long update = encode(getHi(encoded) + deltaHi, getLo(encoded) + deltaLo);
/* 216 */       if (compareAndSet(encoded, update)) {
/* 217 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getHi(long encoded)
/*     */   {
/* 228 */     return (int)(encoded >> 32 & 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getLo(long encoded)
/*     */   {
/* 238 */     return (int)(encoded & 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long encode(int hi, int lo)
/*     */   {
/* 250 */     long h = hi & 0xFFFFFFFF;
/* 251 */     long l = lo & 0xFFFFFFFF;
/* 252 */     long encoded = (h << 32) + l;
/* 253 */     return encoded;
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
/*     */   public static long encodeHi(long encoded, int hi)
/*     */   {
/* 266 */     long h = hi & 0xFFFFFFFF;
/* 267 */     long l = encoded & 0xFFFFFFFF;
/* 268 */     encoded = (h << 32) + l;
/* 269 */     return encoded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long encodeLo(long encoded, int lo)
/*     */   {
/* 281 */     long h = encoded >> 32 & 0xFFFFFFFF;
/* 282 */     long l = lo & 0xFFFFFFFF;
/* 283 */     encoded = (h << 32) + l;
/* 284 */     return encoded;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\AtomicBiInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */