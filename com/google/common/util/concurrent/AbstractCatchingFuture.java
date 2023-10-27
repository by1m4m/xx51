/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractCatchingFuture<V, X extends Throwable, F, T>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */   implements Runnable
/*     */ {
/*     */   ListenableFuture<? extends V> inputFuture;
/*     */   Class<X> exceptionType;
/*     */   F fallback;
/*     */   
/*     */   static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
/*     */   {
/*  38 */     CatchingFuture<V, X> future = new CatchingFuture(input, exceptionType, fallback);
/*  39 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  40 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
/*     */   {
/*  48 */     AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture(input, exceptionType, fallback);
/*  49 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  50 */     return future;
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
/*     */   AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback)
/*     */   {
/*  63 */     this.inputFuture = ((ListenableFuture)Preconditions.checkNotNull(inputFuture));
/*  64 */     this.exceptionType = ((Class)Preconditions.checkNotNull(exceptionType));
/*  65 */     this.fallback = Preconditions.checkNotNull(fallback);
/*     */   }
/*     */   
/*     */   public final void run()
/*     */   {
/*  70 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/*  71 */     Class<X> localExceptionType = this.exceptionType;
/*  72 */     F localFallback = this.fallback;
/*     */     
/*     */ 
/*     */ 
/*  76 */     if (((localInputFuture == null ? 1 : 0) | (localExceptionType == null ? 1 : 0) | (localFallback == null ? 1 : 0) | isCancelled()) != 0) {
/*  77 */       return;
/*     */     }
/*  79 */     this.inputFuture = null;
/*     */     
/*     */ 
/*  82 */     V sourceResult = null;
/*  83 */     Throwable throwable = null;
/*     */     try {
/*  85 */       sourceResult = Futures.getDone(localInputFuture);
/*     */     } catch (ExecutionException e) {
/*  87 */       throwable = (Throwable)Preconditions.checkNotNull(e.getCause());
/*     */     } catch (Throwable e) {
/*  89 */       throwable = e;
/*     */     }
/*     */     
/*  92 */     if (throwable == null) {
/*  93 */       set(sourceResult);
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
/*  98 */       setFuture(localInputFuture);
/*     */       
/* 100 */       return;
/*     */     }
/*     */     
/*     */ 
/* 104 */     X castThrowable = throwable;
/*     */     try
/*     */     {
/* 107 */       fallbackResult = doFallback(localFallback, castThrowable);
/*     */     } catch (Throwable t) { T fallbackResult;
/* 109 */       setException(t);
/* 110 */       return;
/*     */     } finally {
/* 112 */       this.exceptionType = null;
/* 113 */       this.fallback = null;
/*     */     }
/*     */     T fallbackResult;
/* 116 */     setResult(fallbackResult);
/*     */   }
/*     */   
/*     */   protected String pendingToString()
/*     */   {
/* 121 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/* 122 */     Class<X> localExceptionType = this.exceptionType;
/* 123 */     F localFallback = this.fallback;
/* 124 */     String superString = super.pendingToString();
/* 125 */     String resultString = "";
/* 126 */     if (localInputFuture != null) {
/* 127 */       resultString = "inputFuture=[" + localInputFuture + "], ";
/*     */     }
/* 129 */     if ((localExceptionType != null) && (localFallback != null)) {
/* 130 */       return resultString + "exceptionType=[" + localExceptionType + "], fallback=[" + localFallback + "]";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 136 */     if (superString != null) {
/* 137 */       return resultString + superString;
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   @ForOverride
/*     */   abstract T doFallback(F paramF, X paramX)
/*     */     throws Exception;
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(T paramT);
/*     */   
/*     */   protected final void afterDone()
/*     */   {
/* 152 */     maybePropagateCancellationTo(this.inputFuture);
/* 153 */     this.inputFuture = null;
/* 154 */     this.exceptionType = null;
/* 155 */     this.fallback = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class AsyncCatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
/*     */   {
/*     */     AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
/*     */     {
/* 169 */       super(exceptionType, fallback);
/*     */     }
/*     */     
/*     */     ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause)
/*     */       throws Exception
/*     */     {
/* 175 */       ListenableFuture<? extends V> replacement = fallback.apply(cause);
/* 176 */       Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", fallback);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 181 */       return replacement;
/*     */     }
/*     */     
/*     */     void setResult(ListenableFuture<? extends V> result)
/*     */     {
/* 186 */       setFuture(result);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class CatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
/*     */   {
/*     */     CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
/*     */     {
/* 200 */       super(exceptionType, fallback);
/*     */     }
/*     */     
/*     */     V doFallback(Function<? super X, ? extends V> fallback, X cause)
/*     */       throws Exception
/*     */     {
/* 206 */       return (V)fallback.apply(cause);
/*     */     }
/*     */     
/*     */     void setResult(V result)
/*     */     {
/* 211 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AbstractCatchingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */