/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import com.google.j2objc.annotations.ReflectionSupport.Level;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class AggregateFutureState
/*     */ {
/*  43 */   private volatile Set<Throwable> seenExceptions = null;
/*     */   
/*     */   private volatile int remaining;
/*     */   
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*     */   
/*  49 */   private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
/*     */   
/*     */   static
/*     */   {
/*  53 */     Throwable thrownReflectionFailure = null;
/*     */     
/*     */     AtomicHelper helper;
/*     */     try
/*     */     {
/*  58 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
/*     */     }
/*     */     catch (Throwable reflectionFailure)
/*     */     {
/*     */       AtomicHelper helper;
/*     */       
/*  64 */       thrownReflectionFailure = reflectionFailure;
/*  65 */       helper = new SynchronizedAtomicHelper(null);
/*     */     }
/*  67 */     ATOMIC_HELPER = helper;
/*     */     
/*     */ 
/*  70 */     if (thrownReflectionFailure != null) {
/*  71 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownReflectionFailure);
/*     */     }
/*     */   }
/*     */   
/*     */   AggregateFutureState(int remainingFutures) {
/*  76 */     this.remaining = remainingFutures;
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
/*     */   final Set<Throwable> getOrInitSeenExceptions()
/*     */   {
/*  96 */     Set<Throwable> seenExceptionsLocal = this.seenExceptions;
/*  97 */     if (seenExceptionsLocal == null) {
/*  98 */       seenExceptionsLocal = Sets.newConcurrentHashSet();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */       addInitialException(seenExceptionsLocal);
/*     */       
/* 106 */       ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */       seenExceptionsLocal = this.seenExceptions;
/*     */     }
/* 115 */     return seenExceptionsLocal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final int decrementRemainingAndGet()
/*     */   {
/* 122 */     return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
/*     */   }
/*     */   
/*     */   abstract void addInitialException(Set<Throwable> paramSet);
/*     */   
/*     */   private static abstract class AtomicHelper
/*     */   {
/*     */     abstract void compareAndSetSeenExceptions(AggregateFutureState paramAggregateFutureState, Set<Throwable> paramSet1, Set<Throwable> paramSet2);
/*     */     
/*     */     abstract int decrementAndGetRemainingCount(AggregateFutureState paramAggregateFutureState);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper extends AggregateFutureState.AtomicHelper
/*     */   {
/*     */     final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
/*     */     final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
/*     */     
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater)
/*     */     {
/* 141 */       super();
/* 142 */       this.seenExceptionsUpdater = seenExceptionsUpdater;
/* 143 */       this.remainingCountUpdater = remainingCountUpdater;
/*     */     }
/*     */     
/*     */ 
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
/*     */     {
/* 149 */       this.seenExceptionsUpdater.compareAndSet(state, expect, update);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     int decrementAndGetRemainingCount(AggregateFutureState state) { return this.remainingCountUpdater.decrementAndGet(state); }
/*     */   }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends AggregateFutureState.AtomicHelper {
/* 158 */     private SynchronizedAtomicHelper() { super(); }
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
/*     */     {
/* 162 */       synchronized (state) {
/* 163 */         if (state.seenExceptions == expect) {
/* 164 */           state.seenExceptions = update;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState state)
/*     */     {
/* 171 */       synchronized (state) {
/* 172 */         AggregateFutureState.access$310(state);
/* 173 */         return state.remaining;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AggregateFutureState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */