/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.resolver.NameResolver;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ final class InflightNameResolver<T>
/*     */   implements NameResolver<T>
/*     */ {
/*     */   private final EventExecutor executor;
/*     */   private final NameResolver<T> delegate;
/*     */   private final ConcurrentMap<String, Promise<T>> resolvesInProgress;
/*     */   private final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress;
/*     */   
/*     */   InflightNameResolver(EventExecutor executor, NameResolver<T> delegate, ConcurrentMap<String, Promise<T>> resolvesInProgress, ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress)
/*     */   {
/*  43 */     this.executor = ((EventExecutor)ObjectUtil.checkNotNull(executor, "executor"));
/*  44 */     this.delegate = ((NameResolver)ObjectUtil.checkNotNull(delegate, "delegate"));
/*  45 */     this.resolvesInProgress = ((ConcurrentMap)ObjectUtil.checkNotNull(resolvesInProgress, "resolvesInProgress"));
/*  46 */     this.resolveAllsInProgress = ((ConcurrentMap)ObjectUtil.checkNotNull(resolveAllsInProgress, "resolveAllsInProgress"));
/*     */   }
/*     */   
/*     */   public Future<T> resolve(String inetHost)
/*     */   {
/*  51 */     return resolve(inetHost, this.executor.newPromise());
/*     */   }
/*     */   
/*     */   public Future<List<T>> resolveAll(String inetHost)
/*     */   {
/*  56 */     return resolveAll(inetHost, this.executor.newPromise());
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*  61 */     this.delegate.close();
/*     */   }
/*     */   
/*     */   public Promise<T> resolve(String inetHost, Promise<T> promise)
/*     */   {
/*  66 */     return resolve(this.resolvesInProgress, inetHost, promise, false);
/*     */   }
/*     */   
/*     */   public Promise<List<T>> resolveAll(String inetHost, Promise<List<T>> promise)
/*     */   {
/*  71 */     return resolve(this.resolveAllsInProgress, inetHost, promise, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private <U> Promise<U> resolve(final ConcurrentMap<String, Promise<U>> resolveMap, final String inetHost, final Promise<U> promise, boolean resolveAll)
/*     */   {
/*  78 */     Promise<U> earlyPromise = (Promise)resolveMap.putIfAbsent(inetHost, promise);
/*  79 */     if (earlyPromise != null)
/*     */     {
/*  81 */       if (earlyPromise.isDone()) {
/*  82 */         transferResult(earlyPromise, promise);
/*     */       } else {
/*  84 */         earlyPromise.addListener(new FutureListener()
/*     */         {
/*     */           public void operationComplete(Future<U> f) throws Exception {
/*  87 */             InflightNameResolver.transferResult(f, promise);
/*     */           }
/*     */         });
/*     */       }
/*     */     } else {
/*     */       try {
/*  93 */         if (resolveAll)
/*     */         {
/*  95 */           Promise<List<T>> castPromise = promise;
/*  96 */           this.delegate.resolveAll(inetHost, castPromise);
/*     */         }
/*     */         else {
/*  99 */           Promise<T> castPromise = promise;
/* 100 */           this.delegate.resolve(inetHost, castPromise);
/*     */         }
/*     */       } finally {
/* 103 */         if (promise.isDone()) {
/* 104 */           resolveMap.remove(inetHost);
/*     */         } else {
/* 106 */           promise.addListener(new FutureListener()
/*     */           {
/*     */             public void operationComplete(Future<U> f) throws Exception {
/* 109 */               resolveMap.remove(inetHost);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 116 */     return promise;
/*     */   }
/*     */   
/*     */   private static <T> void transferResult(Future<T> src, Promise<T> dst) {
/* 120 */     if (src.isSuccess()) {
/* 121 */       dst.trySuccess(src.getNow());
/*     */     } else {
/* 123 */       dst.tryFailure(src.cause());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 129 */     return StringUtil.simpleClassName(this) + '(' + this.delegate + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\InflightNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */