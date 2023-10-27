/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
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
/*     */ @GwtCompatible
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */   private AggregateFuture<InputT, OutputT>.RunningState runningState;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void afterDone()
/*     */   {
/*  51 */     super.afterDone();
/*  52 */     AggregateFuture<InputT, OutputT>.RunningState localRunningState = this.runningState;
/*  53 */     boolean wasInterrupted; UnmodifiableIterator localUnmodifiableIterator; if (localRunningState != null)
/*     */     {
/*  55 */       this.runningState = null;
/*     */       
/*  57 */       ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
/*  58 */       wasInterrupted = wasInterrupted();
/*     */       
/*  60 */       if (wasInterrupted) {
/*  61 */         localRunningState.interruptTask();
/*     */       }
/*     */       
/*  64 */       if ((isCancelled() & futures != null)) {
/*  65 */         for (localUnmodifiableIterator = futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<?> future = (ListenableFuture)localUnmodifiableIterator.next();
/*  66 */           future.cancel(wasInterrupted);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected String pendingToString()
/*     */   {
/*  74 */     AggregateFuture<InputT, OutputT>.RunningState localRunningState = this.runningState;
/*  75 */     if (localRunningState == null) {
/*  76 */       return null;
/*     */     }
/*     */     
/*  79 */     ImmutableCollection<? extends ListenableFuture<? extends InputT>> localFutures = localRunningState.futures;
/*  80 */     if (localFutures != null) {
/*  81 */       return "futures=[" + localFutures + "]";
/*     */     }
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   final void init(AggregateFuture<InputT, OutputT>.RunningState runningState)
/*     */   {
/*  88 */     this.runningState = runningState;
/*  89 */     runningState.init();
/*     */   }
/*     */   
/*     */   abstract class RunningState
/*     */     extends AggregateFutureState implements Runnable
/*     */   {
/*     */     private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */     private final boolean allMustSucceed;
/*     */     private final boolean collectsValues;
/*     */     
/*     */     RunningState(boolean futures, boolean allMustSucceed)
/*     */     {
/* 101 */       super();
/* 102 */       this.futures = ((ImmutableCollection)Preconditions.checkNotNull(futures));
/* 103 */       this.allMustSucceed = allMustSucceed;
/* 104 */       this.collectsValues = collectsValues;
/*     */     }
/*     */     
/*     */ 
/*     */     public final void run()
/*     */     {
/* 110 */       decrementCountAndMaybeComplete();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void init()
/*     */     {
/* 121 */       if (this.futures.isEmpty()) {
/* 122 */         handleAllCompleted(); return;
/*     */       }
/*     */       
/*     */       int i;
/*     */       
/*     */       UnmodifiableIterator localUnmodifiableIterator;
/*     */       
/* 129 */       if (this.allMustSucceed)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */         i = 0;
/* 140 */         for (localUnmodifiableIterator = this.futures.iterator(); localUnmodifiableIterator.hasNext();) { final ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
/* 141 */           final int index = i++;
/* 142 */           listenable.addListener(new Runnable()
/*     */           {
/*     */             /* Error */
/*     */             public void run()
/*     */             {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   4: aload_0
/*     */               //   5: getfield 2	com/google/common/util/concurrent/AggregateFuture$RunningState$1:val$index	I
/*     */               //   8: aload_0
/*     */               //   9: getfield 3	com/google/common/util/concurrent/AggregateFuture$RunningState$1:val$listenable	Lcom/google/common/util/concurrent/ListenableFuture;
/*     */               //   12: invokestatic 5	com/google/common/util/concurrent/AggregateFuture$RunningState:access$200	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;ILjava/util/concurrent/Future;)V
/*     */               //   15: aload_0
/*     */               //   16: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   19: invokestatic 6	com/google/common/util/concurrent/AggregateFuture$RunningState:access$300	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;)V
/*     */               //   22: goto +13 -> 35
/*     */               //   25: astore_1
/*     */               //   26: aload_0
/*     */               //   27: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   30: invokestatic 6	com/google/common/util/concurrent/AggregateFuture$RunningState:access$300	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;)V
/*     */               //   33: aload_1
/*     */               //   34: athrow
/*     */               //   35: return
/*     */               // Line number table:
/*     */               //   Java source line #147	-> byte code offset #0
/*     */               //   Java source line #149	-> byte code offset #15
/*     */               //   Java source line #150	-> byte code offset #22
/*     */               //   Java source line #149	-> byte code offset #25
/*     */               //   Java source line #150	-> byte code offset #33
/*     */               //   Java source line #151	-> byte code offset #35
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	signature
/*     */               //   0	36	0	this	1
/*     */               //   25	9	1	localObject	Object
/*     */               // Exception table:
/*     */               //   from	to	target	type
/*     */               //   0	15	25	finally
/*     */             }
/* 153 */           }, MoreExecutors.directExecutor());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 158 */         for (i = this.futures.iterator(); i.hasNext();) { Object listenable = (ListenableFuture)i.next();
/* 159 */           ((ListenableFuture)listenable).addListener(this, MoreExecutors.directExecutor());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void handleException(Throwable throwable)
/*     */     {
/* 171 */       Preconditions.checkNotNull(throwable);
/*     */       
/* 173 */       boolean completedWithFailure = false;
/* 174 */       boolean firstTimeSeeingThisException = true;
/* 175 */       if (this.allMustSucceed)
/*     */       {
/*     */ 
/* 178 */         completedWithFailure = AggregateFuture.this.setException(throwable);
/* 179 */         if (completedWithFailure) {
/* 180 */           releaseResourcesAfterFailure();
/*     */         }
/*     */         else
/*     */         {
/* 184 */           firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 189 */       if ((throwable instanceof Error | this.allMustSucceed & !completedWithFailure & firstTimeSeeingThisException))
/*     */       {
/* 191 */         String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/*     */         
/*     */ 
/*     */ 
/* 195 */         AggregateFuture.logger.log(Level.SEVERE, message, throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     final void addInitialException(Set<Throwable> seen)
/*     */     {
/* 201 */       if (!AggregateFuture.this.isCancelled())
/*     */       {
/* 203 */         boolean bool = AggregateFuture.addCausalChain(seen, AggregateFuture.this.tryInternalFastPathGetFailure());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void handleOneInputDone(int index, Future<? extends InputT> future)
/*     */     {
/* 211 */       Preconditions.checkState((this.allMustSucceed) || 
/* 212 */         (!AggregateFuture.this.isDone()) || (AggregateFuture.this.isCancelled()), "Future was done before all dependencies completed");
/*     */       
/*     */       try
/*     */       {
/* 216 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/* 217 */         if (this.allMustSucceed) {
/* 218 */           if (future.isCancelled())
/*     */           {
/*     */ 
/* 221 */             AggregateFuture.this.runningState = null;
/* 222 */             AggregateFuture.this.cancel(false);
/*     */           }
/*     */           else {
/* 225 */             InputT result = Futures.getDone(future);
/* 226 */             if (this.collectsValues) {
/* 227 */               collectOneValue(this.allMustSucceed, index, result);
/*     */             }
/*     */           }
/* 230 */         } else if ((this.collectsValues) && (!future.isCancelled())) {
/* 231 */           collectOneValue(this.allMustSucceed, index, Futures.getDone(future));
/*     */         }
/*     */       } catch (ExecutionException e) {
/* 234 */         handleException(e.getCause());
/*     */       } catch (Throwable t) {
/* 236 */         handleException(t);
/*     */       }
/*     */     }
/*     */     
/*     */     private void decrementCountAndMaybeComplete() {
/* 241 */       int newRemaining = decrementRemainingAndGet();
/* 242 */       Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
/* 243 */       if (newRemaining == 0) {
/* 244 */         processCompleted();
/*     */       }
/*     */     }
/*     */     
/*     */     private void processCompleted() {
/*     */       int i;
/*     */       UnmodifiableIterator localUnmodifiableIterator;
/* 251 */       if ((this.collectsValues & !this.allMustSucceed)) {
/* 252 */         i = 0;
/* 253 */         for (localUnmodifiableIterator = this.futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
/* 254 */           handleOneInputDone(i++, listenable);
/*     */         }
/*     */       }
/* 257 */       handleAllCompleted();
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
/*     */     @ForOverride
/*     */     @OverridingMethodsMustInvokeSuper
/*     */     void releaseResourcesAfterFailure()
/*     */     {
/* 272 */       this.futures = null;
/*     */     }
/*     */     
/*     */     abstract void collectOneValue(boolean paramBoolean, int paramInt, InputT paramInputT);
/*     */     
/*     */     abstract void handleAllCompleted();
/*     */     
/*     */     void interruptTask() {}
/*     */   }
/*     */   
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable t)
/*     */   {
/* 290 */     for (; 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 290 */         t != null; t = t.getCause()) {
/* 291 */       boolean firstTimeSeen = seen.add(t);
/* 292 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 299 */         return false;
/*     */       }
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */