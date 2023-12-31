/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Future;
/*    */ import io.netty.util.concurrent.FutureListener;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.ArrayList;
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
/*    */ public class InetSocketAddressResolver
/*    */   extends AbstractAddressResolver<InetSocketAddress>
/*    */ {
/*    */   final NameResolver<InetAddress> nameResolver;
/*    */   
/*    */   public InetSocketAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver)
/*    */   {
/* 43 */     super(executor, InetSocketAddress.class);
/* 44 */     this.nameResolver = nameResolver;
/*    */   }
/*    */   
/*    */   protected boolean doIsResolved(InetSocketAddress address)
/*    */   {
/* 49 */     return !address.isUnresolved();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void doResolve(final InetSocketAddress unresolvedAddress, final Promise<InetSocketAddress> promise)
/*    */     throws Exception
/*    */   {
/* 58 */     this.nameResolver.resolve(unresolvedAddress.getHostName()).addListener(new FutureListener()
/*    */     {
/*    */       public void operationComplete(Future<InetAddress> future) throws Exception {
/* 61 */         if (future.isSuccess()) {
/* 62 */           promise.setSuccess(new InetSocketAddress((InetAddress)future.getNow(), unresolvedAddress.getPort()));
/*    */         } else {
/* 64 */           promise.setFailure(future.cause());
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void doResolveAll(final InetSocketAddress unresolvedAddress, final Promise<List<InetSocketAddress>> promise)
/*    */     throws Exception
/*    */   {
/* 76 */     this.nameResolver.resolveAll(unresolvedAddress.getHostName()).addListener(new FutureListener()
/*    */     {
/*    */       public void operationComplete(Future<List<InetAddress>> future) throws Exception {
/* 79 */         if (future.isSuccess()) {
/* 80 */           List<InetAddress> inetAddresses = (List)future.getNow();
/*    */           
/* 82 */           List<InetSocketAddress> socketAddresses = new ArrayList(inetAddresses.size());
/* 83 */           for (InetAddress inetAddress : inetAddresses) {
/* 84 */             socketAddresses.add(new InetSocketAddress(inetAddress, unresolvedAddress.getPort()));
/*    */           }
/* 86 */           promise.setSuccess(socketAddresses);
/*    */         } else {
/* 88 */           promise.setFailure(future.cause());
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 96 */     this.nameResolver.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\InetSocketAddressResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */