/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Future;
/*    */ import io.netty.util.concurrent.FutureListener;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CompositeNameResolver<T>
/*    */   extends SimpleNameResolver<T>
/*    */ {
/*    */   private final NameResolver<T>[] resolvers;
/*    */   
/*    */   public CompositeNameResolver(EventExecutor executor, NameResolver<T>... resolvers)
/*    */   {
/* 45 */     super(executor);
/* 46 */     ObjectUtil.checkNotNull(resolvers, "resolvers");
/* 47 */     for (int i = 0; i < resolvers.length; i++) {
/* 48 */       if (resolvers[i] == null) {
/* 49 */         throw new NullPointerException("resolvers[" + i + ']');
/*    */       }
/*    */     }
/* 52 */     if (resolvers.length < 2) {
/* 53 */       throw new IllegalArgumentException("resolvers: " + Arrays.asList(resolvers) + " (expected: at least 2 resolvers)");
/*    */     }
/*    */     
/* 56 */     this.resolvers = ((NameResolver[])resolvers.clone());
/*    */   }
/*    */   
/*    */   protected void doResolve(String inetHost, Promise<T> promise) throws Exception
/*    */   {
/* 61 */     doResolveRec(inetHost, promise, 0, null);
/*    */   }
/*    */   
/*    */ 
/*    */   private void doResolveRec(final String inetHost, final Promise<T> promise, final int resolverIndex, Throwable lastFailure)
/*    */     throws Exception
/*    */   {
/* 68 */     if (resolverIndex >= this.resolvers.length) {
/* 69 */       promise.setFailure(lastFailure);
/*    */     } else {
/* 71 */       NameResolver<T> resolver = this.resolvers[resolverIndex];
/* 72 */       resolver.resolve(inetHost).addListener(new FutureListener()
/*    */       {
/*    */         public void operationComplete(Future<T> future) throws Exception {
/* 75 */           if (future.isSuccess()) {
/* 76 */             promise.setSuccess(future.getNow());
/*    */           } else {
/* 78 */             CompositeNameResolver.this.doResolveRec(inetHost, promise, resolverIndex + 1, future.cause());
/*    */           }
/*    */         }
/*    */       });
/*    */     }
/*    */   }
/*    */   
/*    */   protected void doResolveAll(String inetHost, Promise<List<T>> promise) throws Exception
/*    */   {
/* 87 */     doResolveAllRec(inetHost, promise, 0, null);
/*    */   }
/*    */   
/*    */ 
/*    */   private void doResolveAllRec(final String inetHost, final Promise<List<T>> promise, final int resolverIndex, Throwable lastFailure)
/*    */     throws Exception
/*    */   {
/* 94 */     if (resolverIndex >= this.resolvers.length) {
/* 95 */       promise.setFailure(lastFailure);
/*    */     } else {
/* 97 */       NameResolver<T> resolver = this.resolvers[resolverIndex];
/* 98 */       resolver.resolveAll(inetHost).addListener(new FutureListener()
/*    */       {
/*    */         public void operationComplete(Future<List<T>> future) throws Exception {
/* :1 */           if (future.isSuccess()) {
/* :2 */             promise.setSuccess(future.getNow());
/*    */           } else {
/* :4 */             CompositeNameResolver.this.doResolveAllRec(inetHost, promise, resolverIndex + 1, future.cause());
/*    */           }
/*    */         }
/*    */       });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\CompositeNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */