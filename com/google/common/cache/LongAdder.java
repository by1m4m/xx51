/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x)
/*     */   {
/*  48 */     return v + x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(long x)
/*     */   {
/*     */     Striped64.Cell[] as;
/*     */     
/*     */ 
/*     */ 
/*     */     long b;
/*     */     
/*     */ 
/*     */ 
/*  65 */     if (((as = this.cells) != null) || (!casBase(b = this.base, b + x))) {
/*  66 */       boolean uncontended = true;
/*  67 */       int[] hc; int n; Striped64.Cell a; long v; if (((hc = (int[])threadHashCode.get()) == null) || (as == null) || ((n = as.length) < 1) || ((a = as[(n - 1 & hc[0])]) == null) || 
/*     */       
/*     */ 
/*     */ 
/*  71 */         (!(uncontended = a.cas(v = a.value, v + x)))) retryUpdate(x, hc, uncontended);
/*     */     }
/*     */   }
/*     */   
/*     */   public void increment()
/*     */   {
/*  77 */     add(1L);
/*     */   }
/*     */   
/*     */   public void decrement()
/*     */   {
/*  82 */     add(-1L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long sum()
/*     */   {
/*  93 */     long sum = this.base;
/*  94 */     Striped64.Cell[] as = this.cells;
/*  95 */     if (as != null) {
/*  96 */       int n = as.length;
/*  97 */       for (int i = 0; i < n; i++) {
/*  98 */         Striped64.Cell a = as[i];
/*  99 */         if (a != null) sum += a.value;
/*     */       }
/*     */     }
/* 102 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 112 */     internalReset(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long sumThenReset()
/*     */   {
/* 124 */     long sum = this.base;
/* 125 */     Striped64.Cell[] as = this.cells;
/* 126 */     this.base = 0L;
/* 127 */     if (as != null) {
/* 128 */       int n = as.length;
/* 129 */       for (int i = 0; i < n; i++) {
/* 130 */         Striped64.Cell a = as[i];
/* 131 */         if (a != null) {
/* 132 */           sum += a.value;
/* 133 */           a.value = 0L;
/*     */         }
/*     */       }
/*     */     }
/* 137 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 146 */     return Long.toString(sum());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 155 */     return sum();
/*     */   }
/*     */   
/*     */   public int intValue()
/*     */   {
/* 160 */     return (int)sum();
/*     */   }
/*     */   
/*     */   public float floatValue()
/*     */   {
/* 165 */     return (float)sum();
/*     */   }
/*     */   
/*     */   public double doubleValue()
/*     */   {
/* 170 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 174 */     s.defaultWriteObject();
/* 175 */     s.writeLong(sum());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 179 */     s.defaultReadObject();
/* 180 */     this.busy = 0;
/* 181 */     this.cells = null;
/* 182 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */