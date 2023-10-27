/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ abstract class Striped64
/*     */   extends Number
/*     */ {
/*     */   static final class Cell
/*     */   {
/*     */     volatile long p0;
/*     */     volatile long p1;
/*     */     volatile long p2;
/*     */     volatile long p3;
/*     */     volatile long p4;
/*     */     volatile long p5;
/*     */     volatile long p6;
/*     */     volatile long value;
/*     */     volatile long q0;
/*     */     volatile long q1;
/*     */     volatile long q2;
/*     */     volatile long q3;
/*     */     volatile long q4;
/*     */     volatile long q5;
/*     */     volatile long q6;
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long valueOffset;
/*     */     
/*     */     Cell(long x)
/*     */     {
/* 101 */       this.value = x;
/*     */     }
/*     */     
/*     */     final boolean cas(long cmp, long val) {
/* 105 */       return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 114 */         UNSAFE = Striped64.access$000();
/* 115 */         Class<?> ak = Cell.class;
/* 116 */         valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
/*     */       } catch (Exception e) {
/* 118 */         throw new Error(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   static final ThreadLocal<int[]> threadHashCode = new ThreadLocal();
/*     */   
/*     */ 
/* 131 */   static final Random rng = new Random();
/*     */   
/*     */ 
/* 134 */   static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */   
/*     */ 
/*     */   volatile transient Cell[] cells;
/*     */   
/*     */ 
/*     */   volatile transient long base;
/*     */   
/*     */   volatile transient int busy;
/*     */   
/*     */   private static final Unsafe UNSAFE;
/*     */   
/*     */   private static final long baseOffset;
/*     */   
/*     */   private static final long busyOffset;
/*     */   
/*     */ 
/*     */   final boolean casBase(long cmp, long val)
/*     */   {
/* 153 */     return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
/*     */   }
/*     */   
/*     */   final boolean casBusy()
/*     */   {
/* 158 */     return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void retryUpdate(long x, int[] hc, boolean wasUncontended)
/*     */   {
/*     */     int h;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int h;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */     if (hc == null) {
/* 183 */       threadHashCode.set(hc = new int[1]);
/* 184 */       int r = rng.nextInt();
/* 185 */       h = hc[0] = r == 0 ? 1 : r;
/* 186 */     } else { h = hc[0]; }
/* 187 */     boolean collide = false;
/*     */     
/*     */     for (;;)
/*     */     {
/*     */       Cell[] as;
/*     */       int n;
/* 193 */       if (((as = this.cells) != null) && ((n = as.length) > 0)) { Cell a;
/* 194 */         if ((a = as[(n - 1 & h)]) == null) {
/* 195 */           if (this.busy == 0) {
/* 196 */             Cell r = new Cell(x);
/* 197 */             if ((this.busy == 0) && (casBusy())) {
/* 198 */               boolean created = false;
/*     */               try { Cell[] rs;
/*     */                 int m;
/*     */                 int j;
/* 202 */                 if (((rs = this.cells) != null) && ((m = rs.length) > 0) && (rs[(j = m - 1 & h)] == null)) {
/* 203 */                   rs[j] = r;
/* 204 */                   created = true;
/*     */                 }
/*     */               } finally {
/* 207 */                 this.busy = 0;
/*     */               }
/* 209 */               if (!created) continue;
/*     */               break;
/*     */             }
/*     */           }
/* 213 */           collide = false;
/* 214 */         } else if (!wasUncontended) {
/* 215 */           wasUncontended = true; } else { long v;
/* 216 */           if (a.cas(v = a.value, fn(v, x))) break;
/* 217 */           if ((n >= NCPU) || (this.cells != as)) { collide = false;
/* 218 */           } else if (!collide) { collide = true;
/* 219 */           } else if ((this.busy == 0) && (casBusy())) {
/*     */             try {
/* 221 */               if (this.cells == as) {
/* 222 */                 Cell[] rs = new Cell[n << 1];
/* 223 */                 for (int i = 0; i < n; i++) rs[i] = as[i];
/* 224 */                 this.cells = rs;
/*     */               }
/*     */             } finally {
/* 227 */               this.busy = 0;
/*     */             }
/* 229 */             collide = false;
/* 230 */             continue;
/*     */           } }
/* 232 */         h ^= h << 13;
/* 233 */         h ^= h >>> 17;
/* 234 */         h ^= h << 5;
/* 235 */         hc[0] = h;
/* 236 */       } else if ((this.busy == 0) && (this.cells == as) && (casBusy())) {
/* 237 */         boolean init = false;
/*     */         try {
/* 239 */           if (this.cells == as) {
/* 240 */             Cell[] rs = new Cell[2];
/* 241 */             rs[(h & 0x1)] = new Cell(x);
/* 242 */             this.cells = rs;
/* 243 */             init = true;
/*     */           }
/*     */         } finally {
/* 246 */           this.busy = 0;
/*     */         }
/* 248 */         if (init) break; } else { long v;
/* 249 */         if (casBase(v = this.base, fn(v, x)))
/*     */           break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 255 */   final void internalReset(long initialValue) { Cell[] as = this.cells;
/* 256 */     this.base = initialValue;
/* 257 */     if (as != null) {
/* 258 */       int n = as.length;
/* 259 */       for (int i = 0; i < n; i++) {
/* 260 */         Cell a = as[i];
/* 261 */         if (a != null) { a.value = initialValue;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 273 */       UNSAFE = getUnsafe();
/* 274 */       Class<?> sk = Striped64.class;
/* 275 */       baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
/* 276 */       busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
/*     */     } catch (Exception e) {
/* 278 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Unsafe getUnsafe()
/*     */   {
/*     */     try
/*     */     {
/* 290 */       return Unsafe.getUnsafe();
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/*     */       try {
/* 294 */         (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Unsafe run() throws Exception {
/* 297 */             Class<Unsafe> k = Unsafe.class;
/* 298 */             for (Field f : k.getDeclaredFields()) {
/* 299 */               f.setAccessible(true);
/* 300 */               Object x = f.get(null);
/* 301 */               if (k.isInstance(x)) return (Unsafe)k.cast(x);
/*     */             }
/* 303 */             throw new NoSuchFieldError("the Unsafe");
/*     */           }
/*     */         });
/*     */       } catch (PrivilegedActionException e) {
/* 307 */         throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   abstract long fn(long paramLong1, long paramLong2);
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\Striped64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */