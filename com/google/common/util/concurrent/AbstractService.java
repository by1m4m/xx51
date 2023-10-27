/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  53 */   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(Service.Listener listener)
/*     */     {
/*  57 */       listener.starting();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  62 */       return "starting()";
/*     */     }
/*     */   };
/*  65 */   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(Service.Listener listener)
/*     */     {
/*  69 */       listener.running();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  74 */       return "running()";
/*     */     }
/*     */   };
/*     */   
/*  78 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
/*     */   
/*  80 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
/*     */   
/*     */ 
/*  83 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
/*     */   
/*  85 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
/*     */   
/*  87 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
/*     */   
/*  89 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(Service.State from) {
/*  92 */     new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener) {
/*  95 */         listener.terminated(this.val$from);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 100 */         return "terminated({from = " + this.val$from + "})";
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(Service.State from) {
/* 106 */     new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener) {
/* 109 */         listener.stopping(this.val$from);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 114 */         return "stopping({from = " + this.val$from + "})";
/*     */       }
/*     */     };
/*     */   }
/*     */   
/* 119 */   private final Monitor monitor = new Monitor();
/*     */   
/* 121 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */   @ForOverride
/*     */   protected abstract void doStart();
/*     */   
/*     */   private final class IsStartableGuard extends Monitor.Guard {
/* 126 */     IsStartableGuard() { super(); }
/*     */     
/*     */ 
/*     */     public boolean isSatisfied()
/*     */     {
/* 131 */       return AbstractService.this.state() == Service.State.NEW;
/*     */     }
/*     */   }
/*     */   
/* 135 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */   
/*     */   private final class IsStoppableGuard extends Monitor.Guard
/*     */   {
/*     */     IsStoppableGuard() {
/* 140 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 145 */       return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
/*     */     }
/*     */   }
/*     */   
/* 149 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */   
/*     */   private final class HasReachedRunningGuard extends Monitor.Guard
/*     */   {
/*     */     HasReachedRunningGuard() {
/* 154 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 159 */       return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
/*     */     }
/*     */   }
/*     */   
/* 163 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */   
/*     */   private final class IsStoppedGuard extends Monitor.Guard
/*     */   {
/*     */     IsStoppedGuard() {
/* 168 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 173 */       return AbstractService.this.state().isTerminal();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 178 */   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue();
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
/* 189 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
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
/*     */   @ForOverride
/*     */   protected abstract void doStop();
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
/*     */   @ForOverride
/*     */   protected void doCancelStart() {}
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
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync()
/*     */   {
/* 244 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 246 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 247 */         enqueueStartingEvent();
/* 248 */         doStart();
/*     */       } catch (Throwable startupFailure) {
/* 250 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 252 */         this.monitor.leave();
/* 253 */         dispatchListenerEvents();
/*     */       }
/*     */     } else {
/* 256 */       throw new IllegalStateException("Service " + this + " has already been started");
/*     */     }
/* 258 */     return this;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync()
/*     */   {
/* 264 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 266 */         Service.State previous = state();
/* 267 */         switch (previous) {
/*     */         case NEW: 
/* 269 */           this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 270 */           enqueueTerminatedEvent(Service.State.NEW);
/* 271 */           break;
/*     */         case STARTING: 
/* 273 */           this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 274 */           enqueueStoppingEvent(Service.State.STARTING);
/* 275 */           doCancelStart();
/* 276 */           break;
/*     */         case RUNNING: 
/* 278 */           this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 279 */           enqueueStoppingEvent(Service.State.RUNNING);
/* 280 */           doStop();
/* 281 */           break;
/*     */         
/*     */         case STOPPING: 
/*     */         case TERMINATED: 
/*     */         case FAILED: 
/* 286 */           throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
/*     */         }
/*     */       } catch (Throwable shutdownFailure) {
/* 289 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 291 */         this.monitor.leave();
/* 292 */         dispatchListenerEvents();
/*     */       }
/*     */     }
/* 295 */     return this;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public final void awaitRunning()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: aload_0
/*     */     //   5: getfield 17	com/google/common/util/concurrent/AbstractService:hasReachedRunning	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */     //   8: invokevirtual 59	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */     //   11: aload_0
/*     */     //   12: getstatic 54	com/google/common/util/concurrent/Service$State:RUNNING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   15: invokespecial 60	com/google/common/util/concurrent/AbstractService:checkCurrentState	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   18: aload_0
/*     */     //   19: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   22: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   25: goto +13 -> 38
/*     */     //   28: astore_1
/*     */     //   29: aload_0
/*     */     //   30: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   33: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   36: aload_1
/*     */     //   37: athrow
/*     */     //   38: return
/*     */     // Line number table:
/*     */     //   Java source line #300	-> byte code offset #0
/*     */     //   Java source line #302	-> byte code offset #11
/*     */     //   Java source line #304	-> byte code offset #18
/*     */     //   Java source line #305	-> byte code offset #25
/*     */     //   Java source line #304	-> byte code offset #28
/*     */     //   Java source line #305	-> byte code offset #36
/*     */     //   Java source line #306	-> byte code offset #38
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	AbstractService
/*     */     //   28	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	18	28	finally
/*     */   }
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 310 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 312 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 314 */         this.monitor.leave();
/*     */ 
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 321 */       throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public final void awaitTerminated()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: aload_0
/*     */     //   5: getfield 20	com/google/common/util/concurrent/AbstractService:isStopped	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */     //   8: invokevirtual 59	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */     //   11: aload_0
/*     */     //   12: getstatic 48	com/google/common/util/concurrent/Service$State:TERMINATED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   15: invokespecial 60	com/google/common/util/concurrent/AbstractService:checkCurrentState	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   18: aload_0
/*     */     //   19: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   22: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   25: goto +13 -> 38
/*     */     //   28: astore_1
/*     */     //   29: aload_0
/*     */     //   30: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   33: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   36: aload_1
/*     */     //   37: athrow
/*     */     //   38: return
/*     */     // Line number table:
/*     */     //   Java source line #327	-> byte code offset #0
/*     */     //   Java source line #329	-> byte code offset #11
/*     */     //   Java source line #331	-> byte code offset #18
/*     */     //   Java source line #332	-> byte code offset #25
/*     */     //   Java source line #331	-> byte code offset #28
/*     */     //   Java source line #332	-> byte code offset #36
/*     */     //   Java source line #333	-> byte code offset #38
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	AbstractService
/*     */     //   28	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	18	28	finally
/*     */   }
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 337 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 339 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 341 */         this.monitor.leave();
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 353 */       throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + state());
/*     */     }
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected)
/*     */   {
/* 360 */     Service.State actual = state();
/* 361 */     if (actual != expected) {
/* 362 */       if (actual == Service.State.FAILED)
/*     */       {
/*     */ 
/*     */ 
/* 366 */         throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", failureCause());
/*     */       }
/* 368 */       throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyStarted()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   11: getfield 75	com/google/common/util/concurrent/AbstractService$StateSnapshot:state	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   14: getstatic 29	com/google/common/util/concurrent/Service$State:STARTING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   17: if_acmpeq +43 -> 60
/*     */     //   20: new 36	java/lang/IllegalStateException
/*     */     //   23: dup
/*     */     //   24: new 37	java/lang/StringBuilder
/*     */     //   27: dup
/*     */     //   28: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   31: ldc 76
/*     */     //   33: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   36: aload_0
/*     */     //   37: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   40: getfield 75	com/google/common/util/concurrent/AbstractService$StateSnapshot:state	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   43: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   46: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   49: invokespecial 44	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   52: astore_1
/*     */     //   53: aload_0
/*     */     //   54: aload_1
/*     */     //   55: invokevirtual 35	com/google/common/util/concurrent/AbstractService:notifyFailed	(Ljava/lang/Throwable;)V
/*     */     //   58: aload_1
/*     */     //   59: athrow
/*     */     //   60: aload_0
/*     */     //   61: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   64: getfield 77	com/google/common/util/concurrent/AbstractService$StateSnapshot:shutdownWhenStartupFinishes	Z
/*     */     //   67: ifeq +24 -> 91
/*     */     //   70: aload_0
/*     */     //   71: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   74: dup
/*     */     //   75: getstatic 53	com/google/common/util/concurrent/Service$State:STOPPING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   78: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   81: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   84: aload_0
/*     */     //   85: invokevirtual 55	com/google/common/util/concurrent/AbstractService:doStop	()V
/*     */     //   88: goto +21 -> 109
/*     */     //   91: aload_0
/*     */     //   92: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   95: dup
/*     */     //   96: getstatic 54	com/google/common/util/concurrent/Service$State:RUNNING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   99: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   102: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   105: aload_0
/*     */     //   106: invokespecial 78	com/google/common/util/concurrent/AbstractService:enqueueRunningEvent	()V
/*     */     //   109: aload_0
/*     */     //   110: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   113: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   116: aload_0
/*     */     //   117: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   120: goto +17 -> 137
/*     */     //   123: astore_2
/*     */     //   124: aload_0
/*     */     //   125: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   128: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   131: aload_0
/*     */     //   132: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   135: aload_2
/*     */     //   136: athrow
/*     */     //   137: return
/*     */     // Line number table:
/*     */     //   Java source line #380	-> byte code offset #0
/*     */     //   Java source line #384	-> byte code offset #7
/*     */     //   Java source line #385	-> byte code offset #20
/*     */     //   Java source line #388	-> byte code offset #53
/*     */     //   Java source line #389	-> byte code offset #58
/*     */     //   Java source line #392	-> byte code offset #60
/*     */     //   Java source line #393	-> byte code offset #70
/*     */     //   Java source line #396	-> byte code offset #84
/*     */     //   Java source line #398	-> byte code offset #91
/*     */     //   Java source line #399	-> byte code offset #105
/*     */     //   Java source line #402	-> byte code offset #109
/*     */     //   Java source line #403	-> byte code offset #116
/*     */     //   Java source line #404	-> byte code offset #120
/*     */     //   Java source line #402	-> byte code offset #123
/*     */     //   Java source line #403	-> byte code offset #131
/*     */     //   Java source line #404	-> byte code offset #135
/*     */     //   Java source line #405	-> byte code offset #137
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	138	0	this	AbstractService
/*     */     //   52	7	1	failure	IllegalStateException
/*     */     //   123	13	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	109	123	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyStopped()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 45	com/google/common/util/concurrent/AbstractService:state	()Lcom/google/common/util/concurrent/Service$State;
/*     */     //   11: astore_1
/*     */     //   12: getstatic 46	com/google/common/util/concurrent/AbstractService$6:$SwitchMap$com$google$common$util$concurrent$Service$State	[I
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual 47	com/google/common/util/concurrent/Service$State:ordinal	()I
/*     */     //   19: iaload
/*     */     //   20: tableswitch	default:+86->106, 1:+40->60, 2:+67->87, 3:+67->87, 4:+67->87, 5:+40->60, 6:+40->60
/*     */     //   60: new 36	java/lang/IllegalStateException
/*     */     //   63: dup
/*     */     //   64: new 37	java/lang/StringBuilder
/*     */     //   67: dup
/*     */     //   68: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   71: ldc 79
/*     */     //   73: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   76: aload_1
/*     */     //   77: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   80: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   83: invokespecial 44	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   86: athrow
/*     */     //   87: aload_0
/*     */     //   88: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   91: dup
/*     */     //   92: getstatic 48	com/google/common/util/concurrent/Service$State:TERMINATED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   95: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   98: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   101: aload_0
/*     */     //   102: aload_1
/*     */     //   103: invokespecial 49	com/google/common/util/concurrent/AbstractService:enqueueTerminatedEvent	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   106: aload_0
/*     */     //   107: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   110: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   113: aload_0
/*     */     //   114: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   117: goto +17 -> 134
/*     */     //   120: astore_2
/*     */     //   121: aload_0
/*     */     //   122: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   125: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   128: aload_0
/*     */     //   129: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   132: aload_2
/*     */     //   133: athrow
/*     */     //   134: return
/*     */     // Line number table:
/*     */     //   Java source line #416	-> byte code offset #0
/*     */     //   Java source line #418	-> byte code offset #7
/*     */     //   Java source line #419	-> byte code offset #12
/*     */     //   Java source line #423	-> byte code offset #60
/*     */     //   Java source line #427	-> byte code offset #87
/*     */     //   Java source line #428	-> byte code offset #101
/*     */     //   Java source line #432	-> byte code offset #106
/*     */     //   Java source line #433	-> byte code offset #113
/*     */     //   Java source line #434	-> byte code offset #117
/*     */     //   Java source line #432	-> byte code offset #120
/*     */     //   Java source line #433	-> byte code offset #128
/*     */     //   Java source line #434	-> byte code offset #132
/*     */     //   Java source line #435	-> byte code offset #134
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	135	0	this	AbstractService
/*     */     //   11	92	1	previous	Service.State
/*     */     //   120	13	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	106	120	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyFailed(Throwable cause)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic 80	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: aload_0
/*     */     //   6: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   9: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   12: aload_0
/*     */     //   13: invokevirtual 45	com/google/common/util/concurrent/AbstractService:state	()Lcom/google/common/util/concurrent/Service$State;
/*     */     //   16: astore_2
/*     */     //   17: getstatic 46	com/google/common/util/concurrent/AbstractService$6:$SwitchMap$com$google$common$util$concurrent$Service$State	[I
/*     */     //   20: aload_2
/*     */     //   21: invokevirtual 47	com/google/common/util/concurrent/Service$State:ordinal	()I
/*     */     //   24: iaload
/*     */     //   25: tableswitch	default:+92->117, 1:+39->64, 2:+67->92, 3:+67->92, 4:+67->92, 5:+39->64, 6:+92->117
/*     */     //   64: new 36	java/lang/IllegalStateException
/*     */     //   67: dup
/*     */     //   68: new 37	java/lang/StringBuilder
/*     */     //   71: dup
/*     */     //   72: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   75: ldc 81
/*     */     //   77: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   80: aload_2
/*     */     //   81: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   84: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   87: aload_1
/*     */     //   88: invokespecial 72	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   91: athrow
/*     */     //   92: aload_0
/*     */     //   93: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   96: dup
/*     */     //   97: getstatic 67	com/google/common/util/concurrent/Service$State:FAILED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   100: iconst_0
/*     */     //   101: aload_1
/*     */     //   102: invokespecial 50	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;ZLjava/lang/Throwable;)V
/*     */     //   105: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   108: aload_0
/*     */     //   109: aload_2
/*     */     //   110: aload_1
/*     */     //   111: invokespecial 82	com/google/common/util/concurrent/AbstractService:enqueueFailedEvent	(Lcom/google/common/util/concurrent/Service$State;Ljava/lang/Throwable;)V
/*     */     //   114: goto +3 -> 117
/*     */     //   117: aload_0
/*     */     //   118: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   121: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   124: aload_0
/*     */     //   125: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   128: goto +17 -> 145
/*     */     //   131: astore_3
/*     */     //   132: aload_0
/*     */     //   133: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   136: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   139: aload_0
/*     */     //   140: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   143: aload_3
/*     */     //   144: athrow
/*     */     //   145: return
/*     */     // Line number table:
/*     */     //   Java source line #443	-> byte code offset #0
/*     */     //   Java source line #445	-> byte code offset #5
/*     */     //   Java source line #447	-> byte code offset #12
/*     */     //   Java source line #448	-> byte code offset #17
/*     */     //   Java source line #451	-> byte code offset #64
/*     */     //   Java source line #455	-> byte code offset #92
/*     */     //   Java source line #456	-> byte code offset #108
/*     */     //   Java source line #457	-> byte code offset #114
/*     */     //   Java source line #463	-> byte code offset #117
/*     */     //   Java source line #464	-> byte code offset #124
/*     */     //   Java source line #465	-> byte code offset #128
/*     */     //   Java source line #463	-> byte code offset #131
/*     */     //   Java source line #464	-> byte code offset #139
/*     */     //   Java source line #465	-> byte code offset #143
/*     */     //   Java source line #466	-> byte code offset #145
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	146	0	this	AbstractService
/*     */     //   0	146	1	cause	Throwable
/*     */     //   16	94	2	previous	Service.State
/*     */     //   131	13	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   12	117	131	finally
/*     */   }
/*     */   
/*     */   public final boolean isRunning()
/*     */   {
/* 470 */     return state() == Service.State.RUNNING;
/*     */   }
/*     */   
/*     */   public final Service.State state()
/*     */   {
/* 475 */     return this.snapshot.externalState();
/*     */   }
/*     */   
/*     */ 
/*     */   public final Throwable failureCause()
/*     */   {
/* 481 */     return this.snapshot.failureCause();
/*     */   }
/*     */   
/*     */ 
/*     */   public final void addListener(Service.Listener listener, Executor executor)
/*     */   {
/* 487 */     this.listeners.addListener(listener, executor);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 492 */     return getClass().getSimpleName() + " [" + state() + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void dispatchListenerEvents()
/*     */   {
/* 500 */     if (!this.monitor.isOccupiedByCurrentThread()) {
/* 501 */       this.listeners.dispatch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueStartingEvent() {
/* 506 */     this.listeners.enqueue(STARTING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueRunningEvent() {
/* 510 */     this.listeners.enqueue(RUNNING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueStoppingEvent(Service.State from) {
/* 514 */     if (from == Service.State.STARTING) {
/* 515 */       this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
/* 516 */     } else if (from == Service.State.RUNNING) {
/* 517 */       this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
/*     */     } else {
/* 519 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueTerminatedEvent(Service.State from) {
/* 524 */     switch (from) {
/*     */     case NEW: 
/* 526 */       this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
/* 527 */       break;
/*     */     case STARTING: 
/* 529 */       this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
/* 530 */       break;
/*     */     case RUNNING: 
/* 532 */       this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
/* 533 */       break;
/*     */     case STOPPING: 
/* 535 */       this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
/* 536 */       break;
/*     */     case TERMINATED: 
/*     */     case FAILED: 
/* 539 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueFailedEvent(final Service.State from, final Throwable cause)
/*     */   {
/* 545 */     this.listeners.enqueue(new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/* 549 */         listener.failed(from, cause);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 554 */         return "failed({from = " + from + ", cause = " + cause + "})";
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */     
/*     */ 
/*     */ 
/*     */     final Throwable failure;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     StateSnapshot(Service.State internalState)
/*     */     {
/* 581 */       this(internalState, false, null);
/*     */     }
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, Throwable failure)
/*     */     {
/* 586 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes) || (internalState == Service.State.STARTING), "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */       
/*     */ 
/*     */ 
/* 590 */       Preconditions.checkArgument(((failure != null ? 1 : 0) ^ (internalState == Service.State.FAILED ? 1 : 0)) == 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 596 */       this.state = internalState;
/* 597 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 598 */       this.failure = failure;
/*     */     }
/*     */     
/*     */     Service.State externalState()
/*     */     {
/* 603 */       if ((this.shutdownWhenStartupFinishes) && (this.state == Service.State.STARTING)) {
/* 604 */         return Service.State.STOPPING;
/*     */       }
/* 606 */       return this.state;
/*     */     }
/*     */     
/*     */ 
/*     */     Throwable failureCause()
/*     */     {
/* 612 */       Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */       
/*     */ 
/*     */ 
/* 616 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */