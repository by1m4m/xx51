/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public abstract interface Promise<C>
/*     */ {
/*     */   public void succeeded(C result) {}
/*     */   
/*     */   public void failed(Throwable x) {}
/*     */   
/*     */   public static class Adapter<U>
/*     */     implements Promise<U>
/*     */   {
/*     */     public void failed(Throwable x)
/*     */     {
/*  62 */       Log.getLogger(getClass()).warn(x);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Promise<T> from(CompletableFuture<? super T> completable)
/*     */   {
/*  79 */     if ((completable instanceof Promise)) {
/*  80 */       return (Promise)completable;
/*     */     }
/*  82 */     new Promise()
/*     */     {
/*     */ 
/*     */       public void succeeded(T result)
/*     */       {
/*  87 */         Promise.this.complete(result);
/*     */       }
/*     */       
/*     */ 
/*     */       public void failed(Throwable x)
/*     */       {
/*  93 */         Promise.this.completeExceptionally(x);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Completable<S>
/*     */     extends CompletableFuture<S>
/*     */     implements Promise<S>
/*     */   {
/*     */     public void succeeded(S result)
/*     */     {
/* 108 */       complete(result);
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 114 */       completeExceptionally(x);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Wrapper<W> implements Promise<W>
/*     */   {
/*     */     private final Promise<W> promise;
/*     */     
/*     */     public Wrapper(Promise<W> promise)
/*     */     {
/* 124 */       this.promise = ((Promise)Objects.requireNonNull(promise));
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded(W result)
/*     */     {
/* 130 */       this.promise.succeeded(result);
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 136 */       this.promise.failed(x);
/*     */     }
/*     */     
/*     */     public Promise<W> getPromise()
/*     */     {
/* 141 */       return this.promise;
/*     */     }
/*     */     
/*     */     public Promise<W> unwrap()
/*     */     {
/* 146 */       Promise<W> result = this.promise;
/*     */       
/*     */ 
/* 149 */       while ((result instanceof Wrapper)) {
/* 150 */         result = ((Wrapper)result).unwrap();
/*     */       }
/*     */       
/*     */ 
/* 154 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Promise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */