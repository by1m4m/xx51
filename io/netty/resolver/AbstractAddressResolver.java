/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.UnsupportedAddressTypeException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractAddressResolver<T extends SocketAddress>
/*     */   implements AddressResolver<T>
/*     */ {
/*     */   private final EventExecutor executor;
/*     */   private final TypeParameterMatcher matcher;
/*     */   
/*     */   protected AbstractAddressResolver(EventExecutor executor)
/*     */   {
/*  46 */     this.executor = ((EventExecutor)ObjectUtil.checkNotNull(executor, "executor"));
/*  47 */     this.matcher = TypeParameterMatcher.find(this, AbstractAddressResolver.class, "T");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractAddressResolver(EventExecutor executor, Class<? extends T> addressType)
/*     */   {
/*  56 */     this.executor = ((EventExecutor)ObjectUtil.checkNotNull(executor, "executor"));
/*  57 */     this.matcher = TypeParameterMatcher.get(addressType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EventExecutor executor()
/*     */   {
/*  65 */     return this.executor;
/*     */   }
/*     */   
/*     */   public boolean isSupported(SocketAddress address)
/*     */   {
/*  70 */     return this.matcher.match(address);
/*     */   }
/*     */   
/*     */   public final boolean isResolved(SocketAddress address)
/*     */   {
/*  75 */     if (!isSupported(address)) {
/*  76 */       throw new UnsupportedAddressTypeException();
/*     */     }
/*     */     
/*     */ 
/*  80 */     T castAddress = address;
/*  81 */     return doIsResolved(castAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract boolean doIsResolved(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */   public final Future<T> resolve(SocketAddress address)
/*     */   {
/*  92 */     if (!isSupported((SocketAddress)ObjectUtil.checkNotNull(address, "address")))
/*     */     {
/*  94 */       return executor().newFailedFuture(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/*  97 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 100 */       T cast = address;
/* 101 */       return this.executor.newSucceededFuture(cast);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 106 */       T cast = address;
/* 107 */       Promise<T> promise = executor().newPromise();
/* 108 */       doResolve(cast, promise);
/* 109 */       return promise;
/*     */     } catch (Exception e) {
/* 111 */       return executor().newFailedFuture(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Future<T> resolve(SocketAddress address, Promise<T> promise)
/*     */   {
/* 117 */     ObjectUtil.checkNotNull(address, "address");
/* 118 */     ObjectUtil.checkNotNull(promise, "promise");
/*     */     
/* 120 */     if (!isSupported(address))
/*     */     {
/* 122 */       return promise.setFailure(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/* 125 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 128 */       T cast = address;
/* 129 */       return promise.setSuccess(cast);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 134 */       T cast = address;
/* 135 */       doResolve(cast, promise);
/* 136 */       return promise;
/*     */     } catch (Exception e) {
/* 138 */       return promise.setFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Future<List<T>> resolveAll(SocketAddress address)
/*     */   {
/* 144 */     if (!isSupported((SocketAddress)ObjectUtil.checkNotNull(address, "address")))
/*     */     {
/* 146 */       return executor().newFailedFuture(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/* 149 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 152 */       T cast = address;
/* 153 */       return this.executor.newSucceededFuture(Collections.singletonList(cast));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 158 */       T cast = address;
/* 159 */       Promise<List<T>> promise = executor().newPromise();
/* 160 */       doResolveAll(cast, promise);
/* 161 */       return promise;
/*     */     } catch (Exception e) {
/* 163 */       return executor().newFailedFuture(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Future<List<T>> resolveAll(SocketAddress address, Promise<List<T>> promise)
/*     */   {
/* 169 */     ObjectUtil.checkNotNull(address, "address");
/* 170 */     ObjectUtil.checkNotNull(promise, "promise");
/*     */     
/* 172 */     if (!isSupported(address))
/*     */     {
/* 174 */       return promise.setFailure(new UnsupportedAddressTypeException());
/*     */     }
/*     */     
/* 177 */     if (isResolved(address))
/*     */     {
/*     */ 
/* 180 */       T cast = address;
/* 181 */       return promise.setSuccess(Collections.singletonList(cast));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 186 */       T cast = address;
/* 187 */       doResolveAll(cast, promise);
/* 188 */       return promise;
/*     */     } catch (Exception e) {
/* 190 */       return promise.setFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void doResolve(T paramT, Promise<T> paramPromise)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract void doResolveAll(T paramT, Promise<List<T>> paramPromise)
/*     */     throws Exception;
/*     */   
/*     */   public void close() {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\AbstractAddressResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */