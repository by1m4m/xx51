/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.PromiseNotificationUtil;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*    */ public class PromiseNotifier<V, F extends Future<V>>
/*    */   implements GenericFutureListener<F>
/*    */ {
/* 33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PromiseNotifier.class);
/*    */   
/*    */ 
/*    */   private final Promise<? super V>[] promises;
/*    */   
/*    */   private final boolean logNotifyFailure;
/*    */   
/*    */ 
/*    */   @SafeVarargs
/*    */   public PromiseNotifier(Promise<? super V>... promises)
/*    */   {
/* 44 */     this(true, promises);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @SafeVarargs
/*    */   public PromiseNotifier(boolean logNotifyFailure, Promise<? super V>... promises)
/*    */   {
/* 55 */     ObjectUtil.checkNotNull(promises, "promises");
/* 56 */     for (Promise<? super V> promise : promises) {
/* 57 */       if (promise == null) {
/* 58 */         throw new IllegalArgumentException("promises contains null Promise");
/*    */       }
/*    */     }
/* 61 */     this.promises = ((Promise[])promises.clone());
/* 62 */     this.logNotifyFailure = logNotifyFailure;
/*    */   }
/*    */   
/*    */   public void operationComplete(F future) throws Exception
/*    */   {
/* 67 */     InternalLogger internalLogger = this.logNotifyFailure ? logger : null;
/* 68 */     V result; Promise<? super V> localPromise1; if (future.isSuccess()) {
/* 69 */       result = future.get();
/* 70 */       Promise[] arrayOfPromise1 = this.promises;localPromise1 = arrayOfPromise1.length; for (Promise<? super V> localPromise2 = 0; localPromise2 < localPromise1; localPromise2++) { Promise<? super V> p = arrayOfPromise1[localPromise2];
/* 71 */         PromiseNotificationUtil.trySuccess(p, result, internalLogger);
/*    */       } } else { Promise<? super V> p;
/* 73 */       if (future.isCancelled()) {
/* 74 */         result = this.promises;int i = result.length; for (localPromise1 = 0; localPromise1 < i; localPromise1++) { p = result[localPromise1];
/* 75 */           PromiseNotificationUtil.tryCancel(p, internalLogger);
/*    */         }
/*    */       } else {
/* 78 */         Throwable cause = future.cause();
/* 79 */         Promise[] arrayOfPromise2 = this.promises;localPromise1 = arrayOfPromise2.length; for (Promise<? super V> localPromise3 = 0; localPromise3 < localPromise1; localPromise3++) { Promise<? super V> p = arrayOfPromise2[localPromise3];
/* 80 */           PromiseNotificationUtil.tryFailure(p, cause, internalLogger);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\PromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */