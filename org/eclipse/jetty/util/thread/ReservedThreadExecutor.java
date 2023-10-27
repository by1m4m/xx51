/*     */ package org.eclipse.jetty.util.thread;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import org.eclipse.jetty.util.ProcessorUtils;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ @ManagedObject("A pool for reserved threads")
/*     */ public class ReservedThreadExecutor
/*     */   extends AbstractLifeCycle
/*     */   implements TryExecutor
/*     */ {
/*  48 */   private static final Logger LOG = Log.getLogger(ReservedThreadExecutor.class);
/*  49 */   private static final Runnable STOP = new Runnable()
/*     */   {
/*     */     public void run() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/*  59 */       return "STOP!";
/*     */     }
/*     */   };
/*     */   
/*     */   private final Executor _executor;
/*     */   private final int _capacity;
/*     */   private final ConcurrentLinkedDeque<ReservedThread> _stack;
/*  66 */   private final AtomicInteger _size = new AtomicInteger();
/*  67 */   private final AtomicInteger _pending = new AtomicInteger();
/*     */   
/*     */   private ThreadPoolBudget.Lease _lease;
/*  70 */   private long _idleTime = 1L;
/*  71 */   private TimeUnit _idleTimeUnit = TimeUnit.MINUTES;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReservedThreadExecutor(Executor executor, int capacity)
/*     */   {
/*  81 */     this._executor = executor;
/*  82 */     this._capacity = reservedThreads(executor, capacity);
/*  83 */     this._stack = new ConcurrentLinkedDeque();
/*     */     
/*  85 */     LOG.debug("{}", new Object[] { this });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int reservedThreads(Executor executor, int capacity)
/*     */   {
/*  98 */     if (capacity >= 0)
/*  99 */       return capacity;
/* 100 */     int cpus = ProcessorUtils.availableProcessors();
/* 101 */     if ((executor instanceof ThreadPool.SizedThreadPool))
/*     */     {
/* 103 */       int threads = ((ThreadPool.SizedThreadPool)executor).getMaxThreads();
/* 104 */       return Math.max(1, Math.min(cpus, threads / 10));
/*     */     }
/* 106 */     return cpus;
/*     */   }
/*     */   
/*     */   public Executor getExecutor()
/*     */   {
/* 111 */     return this._executor;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="max number of reserved threads", readonly=true)
/*     */   public int getCapacity()
/*     */   {
/* 117 */     return this._capacity;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="available reserved threads", readonly=true)
/*     */   public int getAvailable()
/*     */   {
/* 123 */     return this._size.get();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="pending reserved threads", readonly=true)
/*     */   public int getPending()
/*     */   {
/* 129 */     return this._pending.get();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="idletimeout in MS", readonly=true)
/*     */   public long getIdleTimeoutMs()
/*     */   {
/* 135 */     if (this._idleTimeUnit == null)
/* 136 */       return 0L;
/* 137 */     return this._idleTimeUnit.toMillis(this._idleTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdleTimeout(long idleTime, TimeUnit idleTimeUnit)
/*     */   {
/* 147 */     if (isRunning())
/* 148 */       throw new IllegalStateException();
/* 149 */     this._idleTime = idleTime;
/* 150 */     this._idleTimeUnit = idleTimeUnit;
/*     */   }
/*     */   
/*     */   public void doStart()
/*     */     throws Exception
/*     */   {
/* 156 */     this._lease = ThreadPoolBudget.leaseFrom(getExecutor(), this, this._capacity);
/* 157 */     super.doStart();
/*     */   }
/*     */   
/*     */   public void doStop()
/*     */     throws Exception
/*     */   {
/* 163 */     if (this._lease != null) {
/* 164 */       this._lease.close();
/*     */     }
/*     */     for (;;) {
/* 167 */       ReservedThread thread = (ReservedThread)this._stack.pollFirst();
/* 168 */       if (thread == null)
/*     */         break;
/* 170 */       this._size.decrementAndGet();
/* 171 */       thread.stop();
/*     */     }
/* 173 */     super.doStop();
/*     */   }
/*     */   
/*     */   public void execute(Runnable task)
/*     */     throws RejectedExecutionException
/*     */   {
/* 179 */     this._executor.execute(task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean tryExecute(Runnable task)
/*     */   {
/* 189 */     if (LOG.isDebugEnabled()) {
/* 190 */       LOG.debug("{} tryExecute {}", new Object[] { this, task });
/*     */     }
/* 192 */     if (task == null) {
/* 193 */       return false;
/*     */     }
/* 195 */     ReservedThread thread = (ReservedThread)this._stack.pollFirst();
/* 196 */     if (thread == null)
/*     */     {
/* 198 */       if (task != STOP)
/* 199 */         startReservedThread();
/* 200 */       return false;
/*     */     }
/*     */     
/* 203 */     int size = this._size.decrementAndGet();
/* 204 */     thread.offer(task);
/*     */     
/* 206 */     if ((size == 0) && (task != STOP)) {
/* 207 */       startReservedThread();
/*     */     }
/* 209 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void startReservedThread()
/*     */   {
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 219 */         int pending = this._pending.get();
/* 220 */         int size = this._size.get();
/* 221 */         if (pending + size >= this._capacity)
/* 222 */           return;
/* 223 */         if (this._pending.compareAndSet(pending, pending + 1))
/*     */         {
/* 225 */           if (LOG.isDebugEnabled())
/* 226 */             LOG.debug("{} startReservedThread p={}", new Object[] { this, Integer.valueOf(pending + 1) });
/* 227 */           this._executor.execute(new ReservedThread(null));
/* 228 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */       return;
/*     */     }
/*     */     catch (RejectedExecutionException e)
/*     */     {
/* 234 */       LOG.ignore(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 241 */     return String.format("%s@%x{s=%d/%d,p=%d}", new Object[] {
/* 242 */       getClass().getSimpleName(), 
/* 243 */       Integer.valueOf(hashCode()), 
/* 244 */       Integer.valueOf(this._size.get()), 
/* 245 */       Integer.valueOf(this._capacity), 
/* 246 */       Integer.valueOf(this._pending.get()) });
/*     */   }
/*     */   
/*     */   private class ReservedThread implements Runnable
/*     */   {
/* 251 */     private final Locker _locker = new Locker();
/* 252 */     private final Condition _wakeup = this._locker.newCondition();
/* 253 */     private boolean _starting = true;
/* 254 */     private Runnable _task = null;
/*     */     
/*     */     private ReservedThread() {}
/*     */     
/* 258 */     public void offer(Runnable task) { if (ReservedThreadExecutor.LOG.isDebugEnabled()) {
/* 259 */         ReservedThreadExecutor.LOG.debug("{} offer {}", new Object[] { this, task });
/*     */       }
/* 261 */       Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */       try {
/* 263 */         this._task = task;
/* 264 */         this._wakeup.signal();
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 261 */         localThrowable1 = localThrowable;throw localThrowable;
/*     */       }
/*     */       finally
/*     */       {
/* 265 */         if (lock != null) $closeResource(localThrowable1, lock);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stop() {
/* 270 */       offer(ReservedThreadExecutor.STOP);
/*     */     }
/*     */     
/*     */     private Runnable reservedWait()
/*     */     {
/* 275 */       if (ReservedThreadExecutor.LOG.isDebugEnabled()) {
/* 276 */         ReservedThreadExecutor.LOG.debug("{} waiting", new Object[] { this });
/*     */       }
/* 278 */       Runnable task = null;
/* 279 */       while (task == null)
/*     */       {
/* 281 */         boolean idle = false;
/*     */         
/* 283 */         Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */         try {
/* 285 */           if (this._task == null)
/*     */           {
/*     */             try
/*     */             {
/* 289 */               if (ReservedThreadExecutor.this._idleTime == 0L) {
/* 290 */                 this._wakeup.await();
/*     */               } else {
/* 292 */                 idle = !this._wakeup.await(ReservedThreadExecutor.this._idleTime, ReservedThreadExecutor.this._idleTimeUnit);
/*     */               }
/*     */             }
/*     */             catch (InterruptedException e) {
/* 296 */               ReservedThreadExecutor.LOG.ignore(e);
/*     */             }
/*     */           }
/* 299 */           task = this._task;
/* 300 */           this._task = null;
/*     */         }
/*     */         catch (Throwable localThrowable)
/*     */         {
/* 283 */           localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 301 */           if (lock != null) $closeResource(localThrowable1, lock);
/*     */         }
/* 303 */         if (idle)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */           if (ReservedThreadExecutor.LOG.isDebugEnabled())
/* 311 */             ReservedThreadExecutor.LOG.debug("{} IDLE", new Object[] { this });
/* 312 */           ReservedThreadExecutor.this.tryExecute(ReservedThreadExecutor.STOP);
/*     */         }
/*     */       }
/*     */       
/* 316 */       if (ReservedThreadExecutor.LOG.isDebugEnabled()) {
/* 317 */         ReservedThreadExecutor.LOG.debug("{} task={}", new Object[] { this, task });
/*     */       }
/* 319 */       return task;
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/* 325 */       while (ReservedThreadExecutor.this.isRunning())
/*     */       {
/*     */ 
/*     */         for (;;)
/*     */         {
/*     */ 
/* 331 */           int size = ReservedThreadExecutor.this._size.get();
/* 332 */           if (size >= ReservedThreadExecutor.this._capacity)
/*     */           {
/* 334 */             if (ReservedThreadExecutor.LOG.isDebugEnabled())
/* 335 */               ReservedThreadExecutor.LOG.debug("{} size {} > capacity", new Object[] { this, Integer.valueOf(size), Integer.valueOf(ReservedThreadExecutor.this._capacity) });
/* 336 */             if (this._starting)
/* 337 */               ReservedThreadExecutor.this._pending.decrementAndGet();
/* 338 */             return;
/*     */           }
/* 340 */           if (ReservedThreadExecutor.this._size.compareAndSet(size, size + 1)) {
/*     */             break;
/*     */           }
/*     */         }
/* 344 */         if (this._starting)
/*     */         {
/* 346 */           if (ReservedThreadExecutor.LOG.isDebugEnabled())
/* 347 */             ReservedThreadExecutor.LOG.debug("{} started", new Object[] { this });
/* 348 */           ReservedThreadExecutor.this._pending.decrementAndGet();
/* 349 */           this._starting = false;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 354 */         ReservedThreadExecutor.this._stack.offerFirst(this);
/*     */         
/*     */ 
/* 357 */         Runnable task = reservedWait();
/*     */         
/* 359 */         if (task == ReservedThreadExecutor.STOP) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 366 */           task.run();
/*     */         }
/*     */         catch (Throwable e)
/*     */         {
/* 370 */           ReservedThreadExecutor.LOG.warn(e);
/*     */         }
/*     */       }
/*     */       
/* 374 */       if (ReservedThreadExecutor.LOG.isDebugEnabled()) {
/* 375 */         ReservedThreadExecutor.LOG.debug("{} Exited", new Object[] { this });
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 381 */       return String.format("%s@%x", new Object[] { ReservedThreadExecutor.this, Integer.valueOf(hashCode()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\ReservedThreadExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */