/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.DefaultPriorityQueue;
/*     */ import io.netty.util.internal.PriorityQueueNode;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ final class ScheduledFutureTask<V>
/*     */   extends PromiseTask<V>
/*     */   implements ScheduledFuture<V>, PriorityQueueNode
/*     */ {
/*  30 */   private static final AtomicLong nextTaskId = new AtomicLong();
/*  31 */   private static final long START_TIME = System.nanoTime();
/*     */   
/*     */   static long nanoTime() {
/*  34 */     return System.nanoTime() - START_TIME;
/*     */   }
/*     */   
/*     */   static long deadlineNanos(long delay) {
/*  38 */     long deadlineNanos = nanoTime() + delay;
/*     */     
/*  40 */     return deadlineNanos < 0L ? Long.MAX_VALUE : deadlineNanos;
/*     */   }
/*     */   
/*  43 */   private final long id = nextTaskId.getAndIncrement();
/*     */   
/*     */   private long deadlineNanos;
/*     */   
/*     */   private final long periodNanos;
/*  48 */   private int queueIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Runnable runnable, V result, long nanoTime)
/*     */   {
/*  54 */     this(executor, toCallable(runnable, result), nanoTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime, long period)
/*     */   {
/*  61 */     super(executor, callable);
/*  62 */     if (period == 0L) {
/*  63 */       throw new IllegalArgumentException("period: 0 (expected: != 0)");
/*     */     }
/*  65 */     this.deadlineNanos = nanoTime;
/*  66 */     this.periodNanos = period;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime)
/*     */   {
/*  73 */     super(executor, callable);
/*  74 */     this.deadlineNanos = nanoTime;
/*  75 */     this.periodNanos = 0L;
/*     */   }
/*     */   
/*     */   protected EventExecutor executor()
/*     */   {
/*  80 */     return super.executor();
/*     */   }
/*     */   
/*     */   public long deadlineNanos() {
/*  84 */     return this.deadlineNanos;
/*     */   }
/*     */   
/*     */   public long delayNanos() {
/*  88 */     return Math.max(0L, deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   public long delayNanos(long currentTimeNanos) {
/*  92 */     return Math.max(0L, deadlineNanos() - (currentTimeNanos - START_TIME));
/*     */   }
/*     */   
/*     */   public long getDelay(TimeUnit unit)
/*     */   {
/*  97 */     return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */   public int compareTo(Delayed o)
/*     */   {
/* 102 */     if (this == o) {
/* 103 */       return 0;
/*     */     }
/*     */     
/* 106 */     ScheduledFutureTask<?> that = (ScheduledFutureTask)o;
/* 107 */     long d = deadlineNanos() - that.deadlineNanos();
/* 108 */     if (d < 0L)
/* 109 */       return -1;
/* 110 */     if (d > 0L)
/* 111 */       return 1;
/* 112 */     if (this.id < that.id)
/* 113 */       return -1;
/* 114 */     if (this.id == that.id) {
/* 115 */       throw new Error();
/*     */     }
/* 117 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 123 */     assert (executor().inEventLoop());
/*     */     try {
/* 125 */       if (this.periodNanos == 0L) {
/* 126 */         if (setUncancellableInternal()) {
/* 127 */           V result = this.task.call();
/* 128 */           setSuccessInternal(result);
/*     */         }
/*     */         
/*     */       }
/* 132 */       else if (!isCancelled()) {
/* 133 */         this.task.call();
/* 134 */         if (!executor().isShutdown()) {
/* 135 */           long p = this.periodNanos;
/* 136 */           if (p > 0L) {
/* 137 */             this.deadlineNanos += p;
/*     */           } else {
/* 139 */             this.deadlineNanos = (nanoTime() - p);
/*     */           }
/* 141 */           if (!isCancelled())
/*     */           {
/*     */ 
/* 144 */             Queue<ScheduledFutureTask<?>> scheduledTaskQueue = ((AbstractScheduledEventExecutor)executor()).scheduledTaskQueue;
/* 145 */             assert (scheduledTaskQueue != null);
/* 146 */             scheduledTaskQueue.add(this);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable cause) {
/* 152 */       setFailureInternal(cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/* 163 */     boolean canceled = super.cancel(mayInterruptIfRunning);
/* 164 */     if (canceled) {
/* 165 */       ((AbstractScheduledEventExecutor)executor()).removeScheduled(this);
/*     */     }
/* 167 */     return canceled;
/*     */   }
/*     */   
/*     */   boolean cancelWithoutRemove(boolean mayInterruptIfRunning) {
/* 171 */     return super.cancel(mayInterruptIfRunning);
/*     */   }
/*     */   
/*     */   protected StringBuilder toStringBuilder()
/*     */   {
/* 176 */     StringBuilder buf = super.toStringBuilder();
/* 177 */     buf.setCharAt(buf.length() - 1, ',');
/*     */     
/* 179 */     return buf.append(" id: ")
/* 180 */       .append(this.id)
/* 181 */       .append(", deadline: ")
/* 182 */       .append(this.deadlineNanos)
/* 183 */       .append(", period: ")
/* 184 */       .append(this.periodNanos)
/* 185 */       .append(')');
/*     */   }
/*     */   
/*     */   public int priorityQueueIndex(DefaultPriorityQueue<?> queue)
/*     */   {
/* 190 */     return this.queueIndex;
/*     */   }
/*     */   
/*     */   public void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i)
/*     */   {
/* 195 */     this.queueIndex = i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\ScheduledFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */