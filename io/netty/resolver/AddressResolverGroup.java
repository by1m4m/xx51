/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.Closeable;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class AddressResolverGroup<T extends SocketAddress>
/*     */   implements Closeable
/*     */ {
/*  38 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AddressResolverGroup.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private final Map<EventExecutor, AddressResolver<T>> resolvers = new IdentityHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AddressResolver<T> getResolver(final EventExecutor executor)
/*     */   {
/*  55 */     if (executor == null) {
/*  56 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  59 */     if (executor.isShuttingDown()) {
/*  60 */       throw new IllegalStateException("executor not accepting a task");
/*     */     }
/*     */     
/*     */ 
/*  64 */     synchronized (this.resolvers) {
/*  65 */       AddressResolver<T> r = (AddressResolver)this.resolvers.get(executor);
/*  66 */       if (r == null)
/*     */       {
/*     */         try {
/*  69 */           newResolver = newResolver(executor);
/*     */         } catch (Exception e) { AddressResolver<T> newResolver;
/*  71 */           throw new IllegalStateException("failed to create a new resolver", e);
/*     */         }
/*     */         final AddressResolver<T> newResolver;
/*  74 */         this.resolvers.put(executor, newResolver);
/*  75 */         executor.terminationFuture().addListener(new FutureListener()
/*     */         {
/*     */           public void operationComplete(Future<Object> future) throws Exception {
/*  78 */             synchronized (AddressResolverGroup.this.resolvers) {
/*  79 */               AddressResolverGroup.this.resolvers.remove(executor);
/*     */             }
/*  81 */             newResolver.close();
/*     */           }
/*     */           
/*  84 */         });
/*  85 */         r = newResolver;
/*     */       }
/*     */     }
/*     */     AddressResolver<T> r;
/*  89 */     return r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract AddressResolver<T> newResolver(EventExecutor paramEventExecutor)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 104 */     synchronized (this.resolvers) {
/* 105 */       AddressResolver<T>[] rArray = (AddressResolver[])this.resolvers.values().toArray(new AddressResolver[0]);
/* 106 */       this.resolvers.clear();
/*     */     }
/*     */     AddressResolver<T>[] rArray;
/* 109 */     for (AddressResolver<T> r : rArray) {
/*     */       try {
/* 111 */         r.close();
/*     */       } catch (Throwable t) {
/* 113 */         logger.warn("Failed to close a resolver:", t);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\AddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */