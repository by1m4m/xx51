/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*     */ public class SharedBlockingCallback
/*     */ {
/*  51 */   static final Logger LOG = Log.getLogger(SharedBlockingCallback.class);
/*     */   
/*  53 */   private static Throwable IDLE = new ConstantThrowable("IDLE");
/*  54 */   private static Throwable SUCCEEDED = new ConstantThrowable("SUCCEEDED");
/*     */   
/*  56 */   private static Throwable FAILED = new ConstantThrowable("FAILED");
/*     */   
/*  58 */   private final ReentrantLock _lock = new ReentrantLock();
/*  59 */   private final Condition _idle = this._lock.newCondition();
/*  60 */   private final Condition _complete = this._lock.newCondition();
/*  61 */   private Blocker _blocker = new Blocker();
/*     */   
/*     */   protected long getIdleTimeout()
/*     */   {
/*  65 */     return -1L;
/*     */   }
/*     */   
/*     */   public Blocker acquire() throws IOException
/*     */   {
/*  70 */     long idle = getIdleTimeout();
/*  71 */     this._lock.lock();
/*     */     try
/*     */     {
/*  74 */       while (this._blocker._state != IDLE)
/*     */       {
/*  76 */         if ((idle > 0L) && (idle < 4611686018427387903L))
/*     */         {
/*     */ 
/*  79 */           if (!this._idle.await(idle * 2L, TimeUnit.MILLISECONDS)) {
/*  80 */             throw new IOException(new TimeoutException());
/*     */           }
/*     */         } else
/*  83 */           this._idle.await();
/*     */       }
/*  85 */       this._blocker._state = null;
/*  86 */       return this._blocker;
/*     */     }
/*     */     catch (InterruptedException x)
/*     */     {
/*  90 */       throw new InterruptedIOException();
/*     */     }
/*     */     finally
/*     */     {
/*  94 */       this._lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void notComplete(Blocker blocker)
/*     */   {
/* 100 */     LOG.warn("Blocker not complete {}", new Object[] { blocker });
/* 101 */     if (LOG.isDebugEnabled()) {
/* 102 */       LOG.debug(new Throwable());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class BlockerTimeoutException
/*     */     extends TimeoutException
/*     */   {}
/*     */   
/*     */   public class Blocker
/*     */     implements Callback, Closeable
/*     */   {
/* 114 */     private Throwable _state = SharedBlockingCallback.IDLE;
/*     */     
/*     */ 
/*     */ 
/*     */     protected Blocker() {}
/*     */     
/*     */ 
/*     */     public Invocable.InvocationType getInvocationType()
/*     */     {
/* 123 */       return Invocable.InvocationType.NON_BLOCKING;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void succeeded()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   4: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   7: invokevirtual 7	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */       //   10: aload_0
/*     */       //   11: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   14: ifnonnull +25 -> 39
/*     */       //   17: aload_0
/*     */       //   18: invokestatic 8	org/eclipse/jetty/util/SharedBlockingCallback:access$300	()Ljava/lang/Throwable;
/*     */       //   21: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   24: aload_0
/*     */       //   25: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   28: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   31: invokeinterface 10 1 0
/*     */       //   36: goto +15 -> 51
/*     */       //   39: new 11	java/lang/IllegalStateException
/*     */       //   42: dup
/*     */       //   43: aload_0
/*     */       //   44: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   47: invokespecial 12	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
/*     */       //   50: athrow
/*     */       //   51: aload_0
/*     */       //   52: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   55: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   58: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   61: goto +16 -> 77
/*     */       //   64: astore_1
/*     */       //   65: aload_0
/*     */       //   66: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   69: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   72: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   75: aload_1
/*     */       //   76: athrow
/*     */       //   77: return
/*     */       // Line number table:
/*     */       //   Java source line #129	-> byte code offset #0
/*     */       //   Java source line #132	-> byte code offset #10
/*     */       //   Java source line #134	-> byte code offset #17
/*     */       //   Java source line #135	-> byte code offset #24
/*     */       //   Java source line #138	-> byte code offset #39
/*     */       //   Java source line #142	-> byte code offset #51
/*     */       //   Java source line #143	-> byte code offset #61
/*     */       //   Java source line #142	-> byte code offset #64
/*     */       //   Java source line #144	-> byte code offset #77
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	78	0	this	Blocker
/*     */       //   64	12	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	51	64	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void failed(Throwable cause)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   4: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   7: invokevirtual 7	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */       //   10: aload_0
/*     */       //   11: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   14: ifnonnull +59 -> 73
/*     */       //   17: aload_1
/*     */       //   18: ifnonnull +13 -> 31
/*     */       //   21: aload_0
/*     */       //   22: invokestatic 14	org/eclipse/jetty/util/SharedBlockingCallback:access$500	()Ljava/lang/Throwable;
/*     */       //   25: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   28: goto +30 -> 58
/*     */       //   31: aload_1
/*     */       //   32: instanceof 15
/*     */       //   35: ifeq +18 -> 53
/*     */       //   38: aload_0
/*     */       //   39: new 16	java/io/IOException
/*     */       //   42: dup
/*     */       //   43: aload_1
/*     */       //   44: invokespecial 17	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
/*     */       //   47: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   50: goto +8 -> 58
/*     */       //   53: aload_0
/*     */       //   54: aload_1
/*     */       //   55: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   58: aload_0
/*     */       //   59: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   62: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   65: invokeinterface 10 1 0
/*     */       //   70: goto +35 -> 105
/*     */       //   73: aload_0
/*     */       //   74: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   77: instanceof 15
/*     */       //   80: ifeq +6 -> 86
/*     */       //   83: goto +22 -> 105
/*     */       //   86: aload_1
/*     */       //   87: getstatic 18	java/lang/System:err	Ljava/io/PrintStream;
/*     */       //   90: invokevirtual 19	java/lang/Throwable:printStackTrace	(Ljava/io/PrintStream;)V
/*     */       //   93: new 11	java/lang/IllegalStateException
/*     */       //   96: dup
/*     */       //   97: aload_0
/*     */       //   98: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   101: invokespecial 12	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
/*     */       //   104: athrow
/*     */       //   105: aload_0
/*     */       //   106: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   109: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   112: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   115: goto +16 -> 131
/*     */       //   118: astore_2
/*     */       //   119: aload_0
/*     */       //   120: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   123: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   126: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   129: aload_2
/*     */       //   130: athrow
/*     */       //   131: return
/*     */       // Line number table:
/*     */       //   Java source line #149	-> byte code offset #0
/*     */       //   Java source line #152	-> byte code offset #10
/*     */       //   Java source line #154	-> byte code offset #17
/*     */       //   Java source line #155	-> byte code offset #21
/*     */       //   Java source line #156	-> byte code offset #31
/*     */       //   Java source line #158	-> byte code offset #38
/*     */       //   Java source line #160	-> byte code offset #53
/*     */       //   Java source line #161	-> byte code offset #58
/*     */       //   Java source line #163	-> byte code offset #73
/*     */       //   Java source line #170	-> byte code offset #86
/*     */       //   Java source line #171	-> byte code offset #93
/*     */       //   Java source line #176	-> byte code offset #105
/*     */       //   Java source line #177	-> byte code offset #115
/*     */       //   Java source line #176	-> byte code offset #118
/*     */       //   Java source line #178	-> byte code offset #131
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	132	0	this	Blocker
/*     */       //   0	132	1	cause	Throwable
/*     */       //   118	12	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   10	105	118	finally
/*     */     }
/*     */     
/*     */     public void block()
/*     */       throws IOException
/*     */     {
/* 189 */       long idle = SharedBlockingCallback.this.getIdleTimeout();
/* 190 */       SharedBlockingCallback.this._lock.lock();
/*     */       try
/*     */       {
/* 193 */         while (this._state == null)
/*     */         {
/* 195 */           if (idle > 0L)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 200 */             long excess = Math.min(idle / 2L, 1000L);
/* 201 */             if (!SharedBlockingCallback.this._complete.await(idle + excess, TimeUnit.MILLISECONDS))
/*     */             {
/*     */ 
/*     */ 
/* 205 */               this._state = new SharedBlockingCallback.BlockerTimeoutException(null);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 210 */             SharedBlockingCallback.this._complete.await();
/*     */           }
/*     */         }
/*     */         
/* 214 */         if (this._state == SharedBlockingCallback.SUCCEEDED)
/* 215 */           return;
/* 216 */         if (this._state == SharedBlockingCallback.IDLE)
/* 217 */           throw new IllegalStateException("IDLE");
/* 218 */         if ((this._state instanceof IOException))
/* 219 */           throw ((IOException)this._state);
/* 220 */         if ((this._state instanceof CancellationException))
/* 221 */           throw ((CancellationException)this._state);
/* 222 */         if ((this._state instanceof RuntimeException))
/* 223 */           throw ((RuntimeException)this._state);
/* 224 */         if ((this._state instanceof Error))
/* 225 */           throw ((Error)this._state);
/* 226 */         throw new IOException(this._state);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/* 230 */         throw new InterruptedIOException();
/*     */       }
/*     */       finally
/*     */       {
/* 234 */         SharedBlockingCallback.this._lock.unlock();
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void close()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   4: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   7: invokevirtual 7	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */       //   10: aload_0
/*     */       //   11: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   14: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
/*     */       //   17: if_acmpne +13 -> 30
/*     */       //   20: new 11	java/lang/IllegalStateException
/*     */       //   23: dup
/*     */       //   24: ldc 30
/*     */       //   26: invokespecial 31	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */       //   29: athrow
/*     */       //   30: aload_0
/*     */       //   31: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   34: ifnonnull +11 -> 45
/*     */       //   37: aload_0
/*     */       //   38: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   41: aload_0
/*     */       //   42: invokevirtual 38	org/eclipse/jetty/util/SharedBlockingCallback:notComplete	(Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)V
/*     */       //   45: aload_0
/*     */       //   46: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   49: instanceof 15
/*     */       //   52: ifeq +25 -> 77
/*     */       //   55: aload_0
/*     */       //   56: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   59: new 39	org/eclipse/jetty/util/SharedBlockingCallback$Blocker
/*     */       //   62: dup
/*     */       //   63: aload_0
/*     */       //   64: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   67: invokespecial 40	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:<init>	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)V
/*     */       //   70: invokestatic 41	org/eclipse/jetty/util/SharedBlockingCallback:access$702	(Lorg/eclipse/jetty/util/SharedBlockingCallback;Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;
/*     */       //   73: pop
/*     */       //   74: goto +10 -> 84
/*     */       //   77: aload_0
/*     */       //   78: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
/*     */       //   81: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   84: aload_0
/*     */       //   85: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   88: invokestatic 42	org/eclipse/jetty/util/SharedBlockingCallback:access$800	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   91: invokeinterface 10 1 0
/*     */       //   96: aload_0
/*     */       //   97: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   100: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   103: invokeinterface 10 1 0
/*     */       //   108: aload_0
/*     */       //   109: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   112: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   115: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   118: goto +16 -> 134
/*     */       //   121: astore_1
/*     */       //   122: aload_0
/*     */       //   123: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   126: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   129: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   132: aload_1
/*     */       //   133: athrow
/*     */       //   134: goto +95 -> 229
/*     */       //   137: astore_2
/*     */       //   138: aload_0
/*     */       //   139: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   142: instanceof 15
/*     */       //   145: ifeq +25 -> 170
/*     */       //   148: aload_0
/*     */       //   149: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   152: new 39	org/eclipse/jetty/util/SharedBlockingCallback$Blocker
/*     */       //   155: dup
/*     */       //   156: aload_0
/*     */       //   157: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   160: invokespecial 40	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:<init>	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)V
/*     */       //   163: invokestatic 41	org/eclipse/jetty/util/SharedBlockingCallback:access$702	(Lorg/eclipse/jetty/util/SharedBlockingCallback;Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;
/*     */       //   166: pop
/*     */       //   167: goto +10 -> 177
/*     */       //   170: aload_0
/*     */       //   171: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
/*     */       //   174: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
/*     */       //   177: aload_0
/*     */       //   178: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   181: invokestatic 42	org/eclipse/jetty/util/SharedBlockingCallback:access$800	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   184: invokeinterface 10 1 0
/*     */       //   189: aload_0
/*     */       //   190: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   193: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
/*     */       //   196: invokeinterface 10 1 0
/*     */       //   201: aload_0
/*     */       //   202: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   205: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   208: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   211: goto +16 -> 227
/*     */       //   214: astore_3
/*     */       //   215: aload_0
/*     */       //   216: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
/*     */       //   219: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
/*     */       //   222: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   225: aload_3
/*     */       //   226: athrow
/*     */       //   227: aload_2
/*     */       //   228: athrow
/*     */       //   229: return
/*     */       // Line number table:
/*     */       //   Java source line #244	-> byte code offset #0
/*     */       //   Java source line #247	-> byte code offset #10
/*     */       //   Java source line #248	-> byte code offset #20
/*     */       //   Java source line #249	-> byte code offset #30
/*     */       //   Java source line #250	-> byte code offset #37
/*     */       //   Java source line #257	-> byte code offset #45
/*     */       //   Java source line #259	-> byte code offset #55
/*     */       //   Java source line #262	-> byte code offset #77
/*     */       //   Java source line #263	-> byte code offset #84
/*     */       //   Java source line #264	-> byte code offset #96
/*     */       //   Java source line #268	-> byte code offset #108
/*     */       //   Java source line #269	-> byte code offset #118
/*     */       //   Java source line #268	-> byte code offset #121
/*     */       //   Java source line #270	-> byte code offset #134
/*     */       //   Java source line #254	-> byte code offset #137
/*     */       //   Java source line #257	-> byte code offset #138
/*     */       //   Java source line #259	-> byte code offset #148
/*     */       //   Java source line #262	-> byte code offset #170
/*     */       //   Java source line #263	-> byte code offset #177
/*     */       //   Java source line #264	-> byte code offset #189
/*     */       //   Java source line #268	-> byte code offset #201
/*     */       //   Java source line #269	-> byte code offset #211
/*     */       //   Java source line #268	-> byte code offset #214
/*     */       //   Java source line #271	-> byte code offset #229
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	230	0	this	Blocker
/*     */       //   121	12	1	localObject1	Object
/*     */       //   137	91	2	localObject2	Object
/*     */       //   214	12	3	localObject3	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   45	108	121	finally
/*     */       //   10	45	137	finally
/*     */       //   138	201	214	finally
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 276 */       SharedBlockingCallback.this._lock.lock();
/*     */       try
/*     */       {
/* 279 */         return String.format("%s@%x{%s}", new Object[] { Blocker.class.getSimpleName(), Integer.valueOf(hashCode()), this._state });
/*     */       }
/*     */       finally
/*     */       {
/* 283 */         SharedBlockingCallback.this._lock.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\SharedBlockingCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */