/*    */ package org.eclipse.jetty.util.thread;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface Invocable
/*    */ {
/*    */   public static enum InvocationType
/*    */   {
/* 42 */     BLOCKING,  NON_BLOCKING,  EITHER;
/*    */     
/*    */     private InvocationType() {} }
/* 45 */   public static final ThreadLocal<Boolean> __nonBlocking = new ThreadLocal();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isNonBlockingInvocation()
/*    */   {
/* 54 */     return Boolean.TRUE.equals(__nonBlocking.get());
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public static void invokeNonBlocking(Runnable task)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 2	org/eclipse/jetty/util/thread/Invocable:__nonBlocking	Ljava/lang/ThreadLocal;
/*    */     //   3: invokevirtual 3	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/*    */     //   6: checkcast 5	java/lang/Boolean
/*    */     //   9: astore_1
/*    */     //   10: getstatic 2	org/eclipse/jetty/util/thread/Invocable:__nonBlocking	Ljava/lang/ThreadLocal;
/*    */     //   13: getstatic 1	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
/*    */     //   16: invokevirtual 6	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*    */     //   19: aload_0
/*    */     //   20: invokeinterface 7 1 0
/*    */     //   25: getstatic 2	org/eclipse/jetty/util/thread/Invocable:__nonBlocking	Ljava/lang/ThreadLocal;
/*    */     //   28: aload_1
/*    */     //   29: invokevirtual 6	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*    */     //   32: goto +13 -> 45
/*    */     //   35: astore_2
/*    */     //   36: getstatic 2	org/eclipse/jetty/util/thread/Invocable:__nonBlocking	Ljava/lang/ThreadLocal;
/*    */     //   39: aload_1
/*    */     //   40: invokevirtual 6	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*    */     //   43: aload_2
/*    */     //   44: athrow
/*    */     //   45: return
/*    */     // Line number table:
/*    */     //   Java source line #64	-> byte code offset #0
/*    */     //   Java source line #67	-> byte code offset #10
/*    */     //   Java source line #68	-> byte code offset #19
/*    */     //   Java source line #72	-> byte code offset #25
/*    */     //   Java source line #73	-> byte code offset #32
/*    */     //   Java source line #72	-> byte code offset #35
/*    */     //   Java source line #74	-> byte code offset #45
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	46	0	task	Runnable
/*    */     //   9	31	1	was_non_blocking	Boolean
/*    */     //   35	9	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   10	25	35	finally
/*    */   }
/*    */   
/*    */   public static InvocationType getInvocationType(Object o)
/*    */   {
/* 84 */     if ((o instanceof Invocable))
/* 85 */       return ((Invocable)o).getInvocationType();
/* 86 */     return InvocationType.BLOCKING;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public InvocationType getInvocationType()
/*    */   {
/* 94 */     return InvocationType.BLOCKING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\Invocable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */