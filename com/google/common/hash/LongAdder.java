/*     */ package com.google.common.hash;
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x)
/*     */   {
/*  46 */     return v + x;
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
/*  63 */     if (((as = this.cells) != null) || (!casBase(b = this.base, b + x))) {
/*  64 */       boolean uncontended = true;
/*  65 */       int[] hc; int n; Striped64.Cell a; long v; if (((hc = (int[])threadHashCode.get()) == null) || (as == null) || ((n = as.length) < 1) || ((a = as[(n - 1 & hc[0])]) == null) || 
/*     */       
/*     */ 
/*     */ 
/*  69 */         (!(uncontended = a.cas(v = a.value, v + x)))) retryUpdate(x, hc, uncontended);
/*     */     }
/*     */   }
/*     */   
/*     */   public void increment()
/*     */   {
/*  75 */     add(1L);
/*     */   }
/*     */   
/*     */   public void decrement()
/*     */   {
/*  80 */     add(-1L);
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
/*  91 */     long sum = this.base;
/*  92 */     Striped64.Cell[] as = this.cells;
/*  93 */     if (as != null) {
/*  94 */       int n = as.length;
/*  95 */       for (int i = 0; i < n; i++) {
/*  96 */         Striped64.Cell a = as[i];
/*  97 */         if (a != null) sum += a.value;
/*     */       }
/*     */     }
/* 100 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 110 */     internalReset(0L);
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
/* 122 */     long sum = this.base;
/* 123 */     Striped64.Cell[] as = this.cells;
/* 124 */     this.base = 0L;
/* 125 */     if (as != null) {
/* 126 */       int n = as.length;
/* 127 */       for (int i = 0; i < n; i++) {
/* 128 */         Striped64.Cell a = as[i];
/* 129 */         if (a != null) {
/* 130 */           sum += a.value;
/* 131 */           a.value = 0L;
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 144 */     return Long.toString(sum());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 153 */     return sum();
/*     */   }
/*     */   
/*     */   public int intValue()
/*     */   {
/* 158 */     return (int)sum();
/*     */   }
/*     */   
/*     */   public float floatValue()
/*     */   {
/* 163 */     return (float)sum();
/*     */   }
/*     */   
/*     */   public double doubleValue()
/*     */   {
/* 168 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 172 */     s.defaultWriteObject();
/* 173 */     s.writeLong(sum());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 177 */     s.defaultReadObject();
/* 178 */     this.busy = 0;
/* 179 */     this.cells = null;
/* 180 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */