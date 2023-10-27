/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import com.google.j2objc.annotations.ReflectionSupport.Level;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ public class AtomicDouble
/*     */   extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private volatile transient long value;
/*  64 */   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AtomicDouble(double initialValue)
/*     */   {
/*  72 */     this.value = Double.doubleToRawLongBits(initialValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AtomicDouble() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double get()
/*     */   {
/*  86 */     return Double.longBitsToDouble(this.value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void set(double newValue)
/*     */   {
/*  95 */     long next = Double.doubleToRawLongBits(newValue);
/*  96 */     this.value = next;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void lazySet(double newValue)
/*     */   {
/* 105 */     long next = Double.doubleToRawLongBits(newValue);
/* 106 */     updater.lazySet(this, next);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double getAndSet(double newValue)
/*     */   {
/* 116 */     long next = Double.doubleToRawLongBits(newValue);
/* 117 */     return Double.longBitsToDouble(updater.getAndSet(this, next));
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
/*     */   public final boolean compareAndSet(double expect, double update)
/*     */   {
/* 130 */     return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(double expect, double update)
/*     */   {
/* 147 */     return updater.weakCompareAndSet(this, 
/* 148 */       Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(double delta)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 160 */       long current = this.value;
/* 161 */       double currentVal = Double.longBitsToDouble(current);
/* 162 */       double nextVal = currentVal + delta;
/* 163 */       long next = Double.doubleToRawLongBits(nextVal);
/* 164 */       if (updater.compareAndSet(this, current, next)) {
/* 165 */         return currentVal;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final double addAndGet(double delta)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 179 */       long current = this.value;
/* 180 */       double currentVal = Double.longBitsToDouble(current);
/* 181 */       double nextVal = currentVal + delta;
/* 182 */       long next = Double.doubleToRawLongBits(nextVal);
/* 183 */       if (updater.compareAndSet(this, current, next)) {
/* 184 */         return nextVal;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 195 */     return Double.toString(get());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 203 */     return (int)get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 211 */     return get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float floatValue()
/*     */   {
/* 219 */     return (float)get();
/*     */   }
/*     */   
/*     */   public double doubleValue()
/*     */   {
/* 224 */     return get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream s)
/*     */     throws IOException
/*     */   {
/* 233 */     s.defaultWriteObject();
/*     */     
/* 235 */     s.writeDouble(get());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 241 */     s.defaultReadObject();
/*     */     
/* 243 */     set(s.readDouble());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AtomicDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */