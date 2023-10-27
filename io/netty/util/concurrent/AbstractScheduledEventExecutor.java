/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.DefaultPriorityQueue;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import java.util.Comparator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public abstract class AbstractScheduledEventExecutor
/*     */   extends AbstractEventExecutor
/*     */ {
/*  33 */   private static final Comparator<ScheduledFutureTask<?>> SCHEDULED_FUTURE_TASK_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(ScheduledFutureTask<?> o1, ScheduledFutureTask<?> o2)
/*     */     {
/*  37 */       return o1.compareTo(o2);
/*     */     }
/*     */   };
/*     */   
/*     */   PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue;
/*     */   
/*     */   protected AbstractScheduledEventExecutor() {}
/*     */   
/*     */   protected AbstractScheduledEventExecutor(EventExecutorGroup parent)
/*     */   {
/*  47 */     super(parent);
/*     */   }
/*     */   
/*     */   protected static long nanoTime() {
/*  51 */     return ScheduledFutureTask.nanoTime();
/*     */   }
/*     */   
/*     */   PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue() {
/*  55 */     if (this.scheduledTaskQueue == null) {
/*  56 */       this.scheduledTaskQueue = new DefaultPriorityQueue(SCHEDULED_FUTURE_TASK_COMPARATOR, 11);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  61 */     return this.scheduledTaskQueue;
/*     */   }
/*     */   
/*     */   private static boolean isNullOrEmpty(Queue<ScheduledFutureTask<?>> queue) {
/*  65 */     return (queue == null) || (queue.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void cancelScheduledTasks()
/*     */   {
/*  74 */     assert (inEventLoop());
/*  75 */     PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/*  76 */     if (isNullOrEmpty(scheduledTaskQueue)) {
/*  77 */       return;
/*     */     }
/*     */     
/*     */ 
/*  81 */     ScheduledFutureTask<?>[] scheduledTasks = (ScheduledFutureTask[])scheduledTaskQueue.toArray(new ScheduledFutureTask[0]);
/*     */     
/*  83 */     for (ScheduledFutureTask<?> task : scheduledTasks) {
/*  84 */       task.cancelWithoutRemove(false);
/*     */     }
/*     */     
/*  87 */     scheduledTaskQueue.clearIgnoringIndexes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Runnable pollScheduledTask()
/*     */   {
/*  94 */     return pollScheduledTask(nanoTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Runnable pollScheduledTask(long nanoTime)
/*     */   {
/* 102 */     assert (inEventLoop());
/*     */     
/* 104 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 105 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/* 106 */     if (scheduledTask == null) {
/* 107 */       return null;
/*     */     }
/*     */     
/* 110 */     if (scheduledTask.deadlineNanos() <= nanoTime) {
/* 111 */       scheduledTaskQueue.remove();
/* 112 */       return scheduledTask;
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final long nextScheduledTaskNano()
/*     */   {
/* 121 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 122 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/* 123 */     if (scheduledTask == null) {
/* 124 */       return -1L;
/*     */     }
/* 126 */     return Math.max(0L, scheduledTask.deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   final ScheduledFutureTask<?> peekScheduledTask() {
/* 130 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 131 */     if (scheduledTaskQueue == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     return (ScheduledFutureTask)scheduledTaskQueue.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final boolean hasScheduledTasks()
/*     */   {
/* 141 */     Queue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/* 142 */     ScheduledFutureTask<?> scheduledTask = scheduledTaskQueue == null ? null : (ScheduledFutureTask)scheduledTaskQueue.peek();
/* 143 */     return (scheduledTask != null) && (scheduledTask.deadlineNanos() <= nanoTime());
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */   {
/* 148 */     ObjectUtil.checkNotNull(command, "command");
/* 149 */     ObjectUtil.checkNotNull(unit, "unit");
/* 150 */     if (delay < 0L) {
/* 151 */       delay = 0L;
/*     */     }
/* 153 */     validateScheduled0(delay, unit);
/*     */     
/* 155 */     return schedule(new ScheduledFutureTask(this, command, null, 
/* 156 */       ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */   {
/* 161 */     ObjectUtil.checkNotNull(callable, "callable");
/* 162 */     ObjectUtil.checkNotNull(unit, "unit");
/* 163 */     if (delay < 0L) {
/* 164 */       delay = 0L;
/*     */     }
/* 166 */     validateScheduled0(delay, unit);
/*     */     
/* 168 */     return schedule(new ScheduledFutureTask(this, callable, 
/* 169 */       ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */   {
/* 174 */     ObjectUtil.checkNotNull(command, "command");
/* 175 */     ObjectUtil.checkNotNull(unit, "unit");
/* 176 */     if (initialDelay < 0L)
/*     */     {
/* 178 */       throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] {Long.valueOf(initialDelay) }));
/*     */     }
/* 180 */     if (period <= 0L)
/*     */     {
/* 182 */       throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[] {Long.valueOf(period) }));
/*     */     }
/* 184 */     validateScheduled0(initialDelay, unit);
/* 185 */     validateScheduled0(period, unit);
/*     */     
/* 187 */     return schedule(new ScheduledFutureTask(this, 
/* 188 */       Executors.callable(command, null), 
/* 189 */       ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */   {
/* 194 */     ObjectUtil.checkNotNull(command, "command");
/* 195 */     ObjectUtil.checkNotNull(unit, "unit");
/* 196 */     if (initialDelay < 0L)
/*     */     {
/* 198 */       throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] {Long.valueOf(initialDelay) }));
/*     */     }
/* 200 */     if (delay <= 0L)
/*     */     {
/* 202 */       throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[] {Long.valueOf(delay) }));
/*     */     }
/*     */     
/* 205 */     validateScheduled0(initialDelay, unit);
/* 206 */     validateScheduled0(delay, unit);
/*     */     
/* 208 */     return schedule(new ScheduledFutureTask(this, 
/* 209 */       Executors.callable(command, null), 
/* 210 */       ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
/*     */   }
/*     */   
/*     */   private void validateScheduled0(long amount, TimeUnit unit)
/*     */   {
/* 215 */     validateScheduled(amount, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void validateScheduled(long amount, TimeUnit unit) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task)
/*     */   {
/* 229 */     if (inEventLoop()) {
/* 230 */       scheduledTaskQueue().add(task);
/*     */     } else {
/* 232 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 235 */           AbstractScheduledEventExecutor.this.scheduledTaskQueue().add(task);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 240 */     return task;
/*     */   }
/*     */   
/*     */   final void removeScheduled(final ScheduledFutureTask<?> task) {
/* 244 */     if (inEventLoop()) {
/* 245 */       scheduledTaskQueue().removeTyped(task);
/*     */     } else {
/* 247 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 250 */           AbstractScheduledEventExecutor.this.removeScheduled(task);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\AbstractScheduledEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */