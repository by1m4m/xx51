/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.MoreObjects.ToStringHelper;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableList.Builder;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtCompatible(emulated=true)
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper)
/*      */   {
/*  154 */     return new MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(future), mapper);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateFuture(V value)
/*      */   {
/*  163 */     if (value == null)
/*      */     {
/*      */ 
/*  166 */       ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
/*  167 */       return typedNull;
/*      */     }
/*  169 */     return new ImmediateFuture.ImmediateSuccessfulFuture(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(V value)
/*      */   {
/*  193 */     return new ImmediateFuture.ImmediateSuccessfulCheckedFuture(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable)
/*      */   {
/*  204 */     Preconditions.checkNotNull(throwable);
/*  205 */     return new ImmediateFuture.ImmediateFailedFuture(throwable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture()
/*      */   {
/*  215 */     return new ImmediateFuture.ImmediateCancelledFuture();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception)
/*      */   {
/*  240 */     Preconditions.checkNotNull(exception);
/*  241 */     return new ImmediateFuture.ImmediateFailedCheckedFuture(exception);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor)
/*      */   {
/*  251 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  252 */     executor.execute(task);
/*  253 */     return task;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService)
/*      */   {
/*  269 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  270 */     Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
/*  271 */     task.addListener(new Runnable()
/*      */     {
/*      */ 
/*      */       public void run()
/*      */       {
/*  276 */         this.val$scheduled.cancel(false);
/*      */       }
/*      */       
/*  279 */     }, MoreExecutors.directExecutor());
/*  280 */     return task;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
/*      */   {
/*  327 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
/*      */   {
/*  396 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
/*      */   {
/*  418 */     if (delegate.isDone()) {
/*  419 */       return delegate;
/*      */     }
/*  421 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
/*      */   {
/*  464 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
/*      */   {
/*  500 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(Future<I> input, final Function<? super I, ? extends O> function)
/*      */   {
/*  526 */     Preconditions.checkNotNull(input);
/*  527 */     Preconditions.checkNotNull(function);
/*  528 */     new Future()
/*      */     {
/*      */       public boolean cancel(boolean mayInterruptIfRunning)
/*      */       {
/*  532 */         return this.val$input.cancel(mayInterruptIfRunning);
/*      */       }
/*      */       
/*      */       public boolean isCancelled()
/*      */       {
/*  537 */         return this.val$input.isCancelled();
/*      */       }
/*      */       
/*      */       public boolean isDone()
/*      */       {
/*  542 */         return this.val$input.isDone();
/*      */       }
/*      */       
/*      */       public O get() throws InterruptedException, ExecutionException
/*      */       {
/*  547 */         return (O)applyTransformation(this.val$input.get());
/*      */       }
/*      */       
/*      */       public O get(long timeout, TimeUnit unit)
/*      */         throws InterruptedException, ExecutionException, TimeoutException
/*      */       {
/*  553 */         return (O)applyTransformation(this.val$input.get(timeout, unit));
/*      */       }
/*      */       
/*      */       private O applyTransformation(I input) throws ExecutionException {
/*      */         try {
/*  558 */           return (O)function.apply(input);
/*      */         } catch (Throwable t) {
/*  560 */           throw new ExecutionException(t);
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures)
/*      */   {
/*  582 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  601 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures)
/*      */   {
/*  612 */     return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  623 */     return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures)
/*      */   {
/*  635 */     return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  647 */     return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
/*      */     private final boolean allMustSucceed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures)
/*      */     {
/*  685 */       this.allMustSucceed = allMustSucceed;
/*  686 */       this.futures = futures;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor)
/*      */     {
/*  704 */       return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor)
/*      */     {
/*  723 */       return new CombinedFuture(this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ListenableFuture<?> run(final Runnable combiner, Executor executor)
/*      */     {
/*  738 */       call(new Callable()
/*      */       {
/*      */         public Void call() throws Exception
/*      */         {
/*  742 */           combiner.run();
/*  743 */           return null; } }, executor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future)
/*      */   {
/*  758 */     if (future.isDone()) {
/*  759 */       return future;
/*      */     }
/*  761 */     NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture(future);
/*  762 */     future.addListener(output, MoreExecutors.directExecutor());
/*  763 */     return output;
/*      */   }
/*      */   
/*      */   private static final class NonCancellationPropagatingFuture<V> extends AbstractFuture.TrustedFuture<V> implements Runnable
/*      */   {
/*      */     private ListenableFuture<V> delegate;
/*      */     
/*      */     NonCancellationPropagatingFuture(ListenableFuture<V> delegate)
/*      */     {
/*  772 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/*  779 */       ListenableFuture<V> localDelegate = this.delegate;
/*  780 */       if (localDelegate != null) {
/*  781 */         setFuture(localDelegate);
/*      */       }
/*      */     }
/*      */     
/*      */     protected String pendingToString()
/*      */     {
/*  787 */       ListenableFuture<V> localDelegate = this.delegate;
/*  788 */       if (localDelegate != null) {
/*  789 */         return "delegate=[" + localDelegate + "]";
/*      */       }
/*  791 */       return null;
/*      */     }
/*      */     
/*      */     protected void afterDone()
/*      */     {
/*  796 */       this.delegate = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures)
/*      */   {
/*  817 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
/*      */   {
/*  836 */     return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures)
/*      */   {
/*      */     Collection<ListenableFuture<? extends T>> collection;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Collection<ListenableFuture<? extends T>> collection;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  865 */     if ((futures instanceof Collection)) {
/*  866 */       collection = (Collection)futures;
/*      */     } else {
/*  868 */       collection = ImmutableList.copyOf(futures);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  873 */     ListenableFuture<? extends T>[] copy = (ListenableFuture[])collection.toArray(new ListenableFuture[collection.size()]);
/*  874 */     InCompletionOrderState<T> state = new InCompletionOrderState(copy, null);
/*  875 */     ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builder();
/*  876 */     for (int i = 0; i < copy.length; i++) {
/*  877 */       delegatesBuilder.add(new InCompletionOrderFuture(state, null));
/*      */     }
/*      */     
/*  880 */     final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
/*  881 */     for (int i = 0; i < copy.length; i++) {
/*  882 */       final int localI = i;
/*  883 */       copy[i].addListener(new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*  887 */           this.val$state.recordInputCompletion(delegates, localI);
/*      */         }
/*      */         
/*  890 */       }, MoreExecutors.directExecutor());
/*      */     }
/*      */     
/*      */ 
/*  894 */     ImmutableList<ListenableFuture<T>> delegatesCast = delegates;
/*  895 */     return delegatesCast;
/*      */   }
/*      */   
/*      */   private static final class InCompletionOrderFuture<T>
/*      */     extends AbstractFuture<T>
/*      */   {
/*      */     private Futures.InCompletionOrderState<T> state;
/*      */     
/*      */     private InCompletionOrderFuture(Futures.InCompletionOrderState<T> state)
/*      */     {
/*  905 */       this.state = state;
/*      */     }
/*      */     
/*      */     public boolean cancel(boolean interruptIfRunning)
/*      */     {
/*  910 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  911 */       if (super.cancel(interruptIfRunning)) {
/*  912 */         localState.recordOutputCancellation(interruptIfRunning);
/*  913 */         return true;
/*      */       }
/*  915 */       return false;
/*      */     }
/*      */     
/*      */     protected void afterDone()
/*      */     {
/*  920 */       this.state = null;
/*      */     }
/*      */     
/*      */     protected String pendingToString()
/*      */     {
/*  925 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  926 */       if (localState != null)
/*      */       {
/*      */ 
/*  929 */         return 
/*      */         
/*      */ 
/*  932 */           "inputCount=[" + localState.inputFutures.length + "], remaining=[" + localState.incompleteOutputCount.get() + "]";
/*      */       }
/*      */       
/*  935 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class InCompletionOrderState<T>
/*      */   {
/*  943 */     private boolean wasCancelled = false;
/*  944 */     private boolean shouldInterrupt = true;
/*      */     private final AtomicInteger incompleteOutputCount;
/*      */     private final ListenableFuture<? extends T>[] inputFutures;
/*  947 */     private volatile int delegateIndex = 0;
/*      */     
/*      */     private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
/*  950 */       this.inputFutures = inputFutures;
/*  951 */       this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
/*      */     }
/*      */     
/*      */     private void recordOutputCancellation(boolean interruptIfRunning) {
/*  955 */       this.wasCancelled = true;
/*      */       
/*      */ 
/*  958 */       if (!interruptIfRunning) {
/*  959 */         this.shouldInterrupt = false;
/*      */       }
/*  961 */       recordCompletion();
/*      */     }
/*      */     
/*      */     private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex)
/*      */     {
/*  966 */       ListenableFuture<? extends T> inputFuture = this.inputFutures[inputFutureIndex];
/*      */       
/*  968 */       this.inputFutures[inputFutureIndex] = null;
/*  969 */       for (int i = this.delegateIndex; i < delegates.size(); i++) {
/*  970 */         if (((AbstractFuture)delegates.get(i)).setFuture(inputFuture)) {
/*  971 */           recordCompletion();
/*      */           
/*  973 */           this.delegateIndex = (i + 1);
/*  974 */           return;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  980 */       this.delegateIndex = delegates.size();
/*      */     }
/*      */     
/*      */     private void recordCompletion() {
/*  984 */       if ((this.incompleteOutputCount.decrementAndGet() == 0) && (this.wasCancelled)) {
/*  985 */         for (ListenableFuture<?> toCancel : this.inputFutures) {
/*  986 */           if (toCancel != null) {
/*  987 */             toCancel.cancel(this.shouldInterrupt);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor)
/*      */   {
/* 1036 */     Preconditions.checkNotNull(callback);
/* 1037 */     future.addListener(new CallbackListener(future, callback), executor);
/*      */   }
/*      */   
/*      */   private static final class CallbackListener<V> implements Runnable
/*      */   {
/*      */     final Future<V> future;
/*      */     final FutureCallback<? super V> callback;
/*      */     
/*      */     CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
/* 1046 */       this.future = future;
/* 1047 */       this.callback = callback;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*      */       try
/*      */       {
/* 1054 */         value = Futures.getDone(this.future);
/*      */       } catch (ExecutionException e) { V value;
/* 1056 */         this.callback.onFailure(e.getCause());
/* 1057 */         return;
/*      */       } catch (RuntimeException|Error e) {
/* 1059 */         this.callback.onFailure(e); return;
/*      */       }
/*      */       V value;
/* 1062 */       this.callback.onSuccess(value);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1067 */       return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future)
/*      */     throws ExecutionException
/*      */   {
/* 1104 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1105 */     return (V)Uninterruptibles.getUninterruptibly(future);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass)
/*      */     throws Exception
/*      */   {
/* 1154 */     return (V)FuturesGetChecked.getChecked(future, exceptionClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit)
/*      */     throws Exception
/*      */   {
/* 1205 */     return (V)FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getUnchecked(Future<V> future)
/*      */   {
/* 1244 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1246 */       return (V)Uninterruptibles.getUninterruptibly(future);
/*      */     } catch (ExecutionException e) {
/* 1248 */       wrapAndThrowUnchecked(e.getCause());
/* 1249 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1254 */     if ((cause instanceof Error)) {
/* 1255 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1262 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<? super Exception, X> mapper;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper)
/*      */     {
/* 1287 */       super();
/*      */       
/* 1289 */       this.mapper = ((Function)Preconditions.checkNotNull(mapper));
/*      */     }
/*      */     
/*      */     protected X mapException(Exception e)
/*      */     {
/* 1294 */       return (Exception)this.mapper.apply(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */