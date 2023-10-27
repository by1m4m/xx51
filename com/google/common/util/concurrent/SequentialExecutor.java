/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtIncompatible
/*     */ final class SequentialExecutor
/*     */   implements Executor
/*     */ {
/*  50 */   private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
/*     */   private final Executor executor;
/*     */   
/*     */   static enum WorkerRunningState {
/*  54 */     IDLE, 
/*     */     
/*  56 */     QUEUING, 
/*     */     
/*  58 */     QUEUED, 
/*  59 */     RUNNING;
/*     */     
/*     */     private WorkerRunningState() {}
/*     */   }
/*     */   
/*     */   @GuardedBy("queue")
/*  65 */   private final Deque<Runnable> queue = new ArrayDeque();
/*     */   
/*     */ 
/*     */   @GuardedBy("queue")
/*  69 */   private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GuardedBy("queue")
/*  79 */   private long workerRunCount = 0L;
/*     */   
/*     */ 
/*  82 */   private final QueueWorker worker = new QueueWorker(null);
/*     */   
/*     */   SequentialExecutor(Executor executor)
/*     */   {
/*  86 */     this.executor = ((Executor)Preconditions.checkNotNull(executor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute(final Runnable task)
/*     */   {
/*  97 */     Preconditions.checkNotNull(task);
/*     */     
/*     */ 
/* 100 */     synchronized (this.queue)
/*     */     {
/*     */ 
/* 103 */       if ((this.workerRunningState == WorkerRunningState.RUNNING) || (this.workerRunningState == WorkerRunningState.QUEUED)) {
/* 104 */         this.queue.add(task);
/* 105 */         return;
/*     */       }
/*     */       
/* 108 */       long oldRunCount = this.workerRunCount;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */       Runnable submittedTask = new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 120 */           task.run();
/*     */         }
/* 122 */       };
/* 123 */       this.queue.add(submittedTask);
/* 124 */       this.workerRunningState = WorkerRunningState.QUEUING;
/*     */     }
/*     */     long oldRunCount;
/*     */     try {
/* 128 */       this.executor.execute(this.worker);
/*     */     } catch (RuntimeException|Error t) {
/* 130 */       synchronized (this.queue) { Runnable submittedTask;
/* 131 */         if ((this.workerRunningState == WorkerRunningState.IDLE) || (this.workerRunningState == WorkerRunningState.QUEUING)) {}
/*     */         
/* 133 */         boolean removed = this.queue.removeLastOccurrence(submittedTask);
/*     */         
/*     */ 
/* 136 */         if ((!(t instanceof RejectedExecutionException)) || (removed)) {
/* 137 */           throw t;
/*     */         }
/*     */       }
/* 140 */       return;
/*     */     }
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
/* 154 */     boolean alreadyMarkedQueued = this.workerRunningState != WorkerRunningState.QUEUING;
/* 155 */     if (alreadyMarkedQueued) {
/* 156 */       return;
/*     */     }
/* 158 */     synchronized (this.queue) {
/* 159 */       if ((this.workerRunCount == oldRunCount) && (this.workerRunningState == WorkerRunningState.QUEUING)) {
/* 160 */         this.workerRunningState = WorkerRunningState.QUEUED;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class QueueWorker implements Runnable
/*     */   {
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 171 */         workOnQueue();
/*     */       } catch (Error e) {
/* 173 */         synchronized (SequentialExecutor.this.queue) {
/* 174 */           SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */         }
/* 176 */         throw e;
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     private void workOnQueue()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: iconst_0
/*     */       //   1: istore_1
/*     */       //   2: iconst_0
/*     */       //   3: istore_2
/*     */       //   4: aload_0
/*     */       //   5: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   8: invokestatic 6	com/google/common/util/concurrent/SequentialExecutor:access$100	(Lcom/google/common/util/concurrent/SequentialExecutor;)Ljava/util/Deque;
/*     */       //   11: dup
/*     */       //   12: astore 4
/*     */       //   14: monitorenter
/*     */       //   15: iload_2
/*     */       //   16: ifne +51 -> 67
/*     */       //   19: aload_0
/*     */       //   20: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   23: invokestatic 9	com/google/common/util/concurrent/SequentialExecutor:access$200	(Lcom/google/common/util/concurrent/SequentialExecutor;)Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   26: getstatic 10	com/google/common/util/concurrent/SequentialExecutor$WorkerRunningState:RUNNING	Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   29: if_acmpne +17 -> 46
/*     */       //   32: aload 4
/*     */       //   34: monitorexit
/*     */       //   35: iload_1
/*     */       //   36: ifeq +9 -> 45
/*     */       //   39: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */       //   42: invokevirtual 12	java/lang/Thread:interrupt	()V
/*     */       //   45: return
/*     */       //   46: aload_0
/*     */       //   47: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   50: invokestatic 13	com/google/common/util/concurrent/SequentialExecutor:access$308	(Lcom/google/common/util/concurrent/SequentialExecutor;)J
/*     */       //   53: pop2
/*     */       //   54: aload_0
/*     */       //   55: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   58: getstatic 10	com/google/common/util/concurrent/SequentialExecutor$WorkerRunningState:RUNNING	Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   61: invokestatic 8	com/google/common/util/concurrent/SequentialExecutor:access$202	(Lcom/google/common/util/concurrent/SequentialExecutor;Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;)Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   64: pop
/*     */       //   65: iconst_1
/*     */       //   66: istore_2
/*     */       //   67: aload_0
/*     */       //   68: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   71: invokestatic 6	com/google/common/util/concurrent/SequentialExecutor:access$100	(Lcom/google/common/util/concurrent/SequentialExecutor;)Ljava/util/Deque;
/*     */       //   74: invokeinterface 14 1 0
/*     */       //   79: checkcast 15	java/lang/Runnable
/*     */       //   82: astore_3
/*     */       //   83: aload_3
/*     */       //   84: ifnonnull +28 -> 112
/*     */       //   87: aload_0
/*     */       //   88: getfield 2	com/google/common/util/concurrent/SequentialExecutor$QueueWorker:this$0	Lcom/google/common/util/concurrent/SequentialExecutor;
/*     */       //   91: getstatic 7	com/google/common/util/concurrent/SequentialExecutor$WorkerRunningState:IDLE	Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   94: invokestatic 8	com/google/common/util/concurrent/SequentialExecutor:access$202	(Lcom/google/common/util/concurrent/SequentialExecutor;Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;)Lcom/google/common/util/concurrent/SequentialExecutor$WorkerRunningState;
/*     */       //   97: pop
/*     */       //   98: aload 4
/*     */       //   100: monitorexit
/*     */       //   101: iload_1
/*     */       //   102: ifeq +9 -> 111
/*     */       //   105: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */       //   108: invokevirtual 12	java/lang/Thread:interrupt	()V
/*     */       //   111: return
/*     */       //   112: aload 4
/*     */       //   114: monitorexit
/*     */       //   115: goto +11 -> 126
/*     */       //   118: astore 5
/*     */       //   120: aload 4
/*     */       //   122: monitorexit
/*     */       //   123: aload 5
/*     */       //   125: athrow
/*     */       //   126: iload_1
/*     */       //   127: invokestatic 16	java/lang/Thread:interrupted	()Z
/*     */       //   130: ior
/*     */       //   131: istore_1
/*     */       //   132: aload_3
/*     */       //   133: invokeinterface 17 1 0
/*     */       //   138: goto +35 -> 173
/*     */       //   141: astore 4
/*     */       //   143: invokestatic 19	com/google/common/util/concurrent/SequentialExecutor:access$400	()Ljava/util/logging/Logger;
/*     */       //   146: getstatic 20	java/util/logging/Level:SEVERE	Ljava/util/logging/Level;
/*     */       //   149: new 21	java/lang/StringBuilder
/*     */       //   152: dup
/*     */       //   153: invokespecial 22	java/lang/StringBuilder:<init>	()V
/*     */       //   156: ldc 23
/*     */       //   158: invokevirtual 24	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   161: aload_3
/*     */       //   162: invokevirtual 25	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   165: invokevirtual 26	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */       //   168: aload 4
/*     */       //   170: invokevirtual 27	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   173: goto -169 -> 4
/*     */       //   176: astore 6
/*     */       //   178: iload_1
/*     */       //   179: ifeq +9 -> 188
/*     */       //   182: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */       //   185: invokevirtual 12	java/lang/Thread:interrupt	()V
/*     */       //   188: aload 6
/*     */       //   190: athrow
/*     */       // Line number table:
/*     */       //   Java source line #195	-> byte code offset #0
/*     */       //   Java source line #196	-> byte code offset #2
/*     */       //   Java source line #200	-> byte code offset #4
/*     */       //   Java source line #203	-> byte code offset #15
/*     */       //   Java source line #204	-> byte code offset #19
/*     */       //   Java source line #206	-> byte code offset #32
/*     */       //   Java source line #236	-> byte code offset #35
/*     */       //   Java source line #237	-> byte code offset #39
/*     */       //   Java source line #206	-> byte code offset #45
/*     */       //   Java source line #211	-> byte code offset #46
/*     */       //   Java source line #212	-> byte code offset #54
/*     */       //   Java source line #213	-> byte code offset #65
/*     */       //   Java source line #216	-> byte code offset #67
/*     */       //   Java source line #217	-> byte code offset #83
/*     */       //   Java source line #218	-> byte code offset #87
/*     */       //   Java source line #219	-> byte code offset #98
/*     */       //   Java source line #236	-> byte code offset #101
/*     */       //   Java source line #237	-> byte code offset #105
/*     */       //   Java source line #219	-> byte code offset #111
/*     */       //   Java source line #221	-> byte code offset #112
/*     */       //   Java source line #225	-> byte code offset #126
/*     */       //   Java source line #227	-> byte code offset #132
/*     */       //   Java source line #230	-> byte code offset #138
/*     */       //   Java source line #228	-> byte code offset #141
/*     */       //   Java source line #229	-> byte code offset #143
/*     */       //   Java source line #231	-> byte code offset #173
/*     */       //   Java source line #236	-> byte code offset #176
/*     */       //   Java source line #237	-> byte code offset #182
/*     */       //   Java source line #239	-> byte code offset #188
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	191	0	this	QueueWorker
/*     */       //   1	178	1	interruptedDuringTask	boolean
/*     */       //   3	64	2	hasSetRunning	boolean
/*     */       //   82	2	3	task	Runnable
/*     */       //   126	36	3	task	Runnable
/*     */       //   141	28	4	e	RuntimeException
/*     */       //   118	6	5	localObject1	Object
/*     */       //   176	13	6	localObject2	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   15	35	118	finally
/*     */       //   46	101	118	finally
/*     */       //   112	115	118	finally
/*     */       //   118	123	118	finally
/*     */       //   132	138	141	java/lang/RuntimeException
/*     */       //   4	35	176	finally
/*     */       //   46	101	176	finally
/*     */       //   112	178	176	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\SequentialExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */