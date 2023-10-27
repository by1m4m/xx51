/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ final class FastThreadLocalRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final Runnable runnable;
/*    */   
/*    */   private FastThreadLocalRunnable(Runnable runnable)
/*    */   {
/* 24 */     this.runnable = ((Runnable)ObjectUtil.checkNotNull(runnable, "runnable"));
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void run()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 23	io/netty/util/concurrent/FastThreadLocalRunnable:runnable	Ljava/lang/Runnable;
/*    */     //   4: invokeinterface 28 1 0
/*    */     //   9: invokestatic 33	io/netty/util/concurrent/FastThreadLocal:removeAll	()V
/*    */     //   12: goto +9 -> 21
/*    */     //   15: astore_1
/*    */     //   16: invokestatic 33	io/netty/util/concurrent/FastThreadLocal:removeAll	()V
/*    */     //   19: aload_1
/*    */     //   20: athrow
/*    */     //   21: return
/*    */     // Line number table:
/*    */     //   Java source line #30	-> byte code offset #0
/*    */     //   Java source line #32	-> byte code offset #9
/*    */     //   Java source line #33	-> byte code offset #12
/*    */     //   Java source line #32	-> byte code offset #15
/*    */     //   Java source line #33	-> byte code offset #19
/*    */     //   Java source line #34	-> byte code offset #21
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	22	0	this	FastThreadLocalRunnable
/*    */     //   15	5	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	9	15	finally
/*    */   }
/*    */   
/*    */   static Runnable wrap(Runnable runnable)
/*    */   {
/* 37 */     return (runnable instanceof FastThreadLocalRunnable) ? runnable : new FastThreadLocalRunnable(runnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\FastThreadLocalRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */