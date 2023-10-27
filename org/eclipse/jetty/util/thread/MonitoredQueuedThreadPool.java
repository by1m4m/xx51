/*     */ package org.eclipse.jetty.util.thread;
/*     */ 
/*     */ import org.eclipse.jetty.util.BlockingArrayQueue;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.annotation.ManagedOperation;
/*     */ import org.eclipse.jetty.util.statistic.CounterStatistic;
/*     */ import org.eclipse.jetty.util.statistic.SampleStatistic;
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
/*     */ @ManagedObject
/*     */ public class MonitoredQueuedThreadPool
/*     */   extends QueuedThreadPool
/*     */ {
/*  34 */   private final CounterStatistic queueStats = new CounterStatistic();
/*  35 */   private final SampleStatistic queueLatencyStats = new SampleStatistic();
/*  36 */   private final SampleStatistic taskLatencyStats = new SampleStatistic();
/*  37 */   private final CounterStatistic threadStats = new CounterStatistic();
/*     */   
/*     */   public MonitoredQueuedThreadPool()
/*     */   {
/*  41 */     this(256);
/*     */   }
/*     */   
/*     */   public MonitoredQueuedThreadPool(int maxThreads)
/*     */   {
/*  46 */     super(maxThreads, maxThreads, 86400000, new BlockingArrayQueue(maxThreads, 256));
/*  47 */     addBean(this.queueStats);
/*  48 */     addBean(this.queueLatencyStats);
/*  49 */     addBean(this.taskLatencyStats);
/*  50 */     addBean(this.threadStats);
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(Runnable job)
/*     */   {
/*  56 */     this.queueStats.increment();
/*  57 */     final long begin = System.nanoTime();
/*  58 */     super.execute(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/*  63 */         long queueLatency = System.nanoTime() - begin;
/*  64 */         MonitoredQueuedThreadPool.this.queueStats.decrement();
/*  65 */         MonitoredQueuedThreadPool.this.threadStats.increment();
/*  66 */         MonitoredQueuedThreadPool.this.queueLatencyStats.set(queueLatency);
/*  67 */         long start = System.nanoTime();
/*     */         try
/*     */         {
/*  70 */           this.val$job.run();
/*     */         }
/*     */         finally {
/*     */           long taskLatency;
/*  74 */           long taskLatency = System.nanoTime() - start;
/*  75 */           MonitoredQueuedThreadPool.this.threadStats.decrement();
/*  76 */           MonitoredQueuedThreadPool.this.taskLatencyStats.set(taskLatency);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       public String toString()
/*     */       {
/*  83 */         return this.val$job.toString();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedOperation(value="resets the statistics", impact="ACTION")
/*     */   public void reset()
/*     */   {
/*  94 */     this.queueStats.reset();
/*  95 */     this.queueLatencyStats.reset();
/*  96 */     this.taskLatencyStats.reset();
/*  97 */     this.threadStats.reset(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the number of tasks executed")
/*     */   public long getTasks()
/*     */   {
/* 106 */     return this.taskLatencyStats.getTotal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the maximum number of busy threads")
/*     */   public int getMaxBusyThreads()
/*     */   {
/* 115 */     return (int)this.threadStats.getMax();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the maximum task queue size")
/*     */   public int getMaxQueueSize()
/*     */   {
/* 124 */     return (int)this.queueStats.getMax();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the average time a task remains in the queue, in nanoseconds")
/*     */   public long getAverageQueueLatency()
/*     */   {
/* 133 */     return this.queueLatencyStats.getMean();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the maximum time a task remains in the queue, in nanoseconds")
/*     */   public long getMaxQueueLatency()
/*     */   {
/* 142 */     return this.queueLatencyStats.getMax();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the average task execution time, in nanoseconds")
/*     */   public long getAverageTaskLatency()
/*     */   {
/* 151 */     return this.taskLatencyStats.getMean();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedAttribute("the maximum task execution time, in nanoseconds")
/*     */   public long getMaxTaskLatency()
/*     */   {
/* 160 */     return this.taskLatencyStats.getMax();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\MonitoredQueuedThreadPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */