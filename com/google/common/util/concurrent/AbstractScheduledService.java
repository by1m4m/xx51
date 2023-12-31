/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractScheduledService
/*     */   implements Service
/*     */ {
/* 101 */   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
/*     */   
/*     */ 
/*     */   protected abstract void runOneIteration()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void startUp()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void shutDown()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract Scheduler scheduler();
/*     */   
/*     */ 
/*     */   public static abstract class Scheduler
/*     */   {
/*     */     public static Scheduler newFixedDelaySchedule(long initialDelay, long delay, final TimeUnit unit)
/*     */     {
/* 128 */       Preconditions.checkNotNull(unit);
/* 129 */       Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
/* 130 */       new Scheduler(initialDelay)
/*     */       {
/*     */         public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */         {
/* 134 */           return executor.scheduleWithFixedDelay(task, this.val$initialDelay, unit, this.val$unit);
/*     */         }
/*     */       };
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
/*     */     public static Scheduler newFixedRateSchedule(long initialDelay, long period, final TimeUnit unit)
/*     */     {
/* 150 */       Preconditions.checkNotNull(unit);
/* 151 */       Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
/* 152 */       new Scheduler(initialDelay)
/*     */       {
/*     */         public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */         {
/* 156 */           return executor.scheduleAtFixedRate(task, this.val$initialDelay, unit, this.val$unit);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract Future<?> schedule(AbstractService paramAbstractService, ScheduledExecutorService paramScheduledExecutorService, Runnable paramRunnable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 169 */   private final AbstractService delegate = new ServiceDelegate(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class ServiceDelegate
/*     */     extends AbstractService
/*     */   {
/*     */     private volatile Future<?> runningTask;
/*     */     
/*     */ 
/*     */     private volatile ScheduledExecutorService executorService;
/*     */     
/*     */ 
/* 183 */     private final ReentrantLock lock = new ReentrantLock();
/*     */     private ServiceDelegate() {}
/*     */     
/*     */     class Task implements Runnable {
/*     */       Task() {}
/*     */       
/* 189 */       public void run() { AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */         try {
/* 191 */           if (AbstractScheduledService.ServiceDelegate.this.runningTask.isCancelled())
/*     */           {
/* 193 */             return;
/*     */           }
/* 195 */           AbstractScheduledService.this.runOneIteration();
/*     */         } catch (Throwable t) {
/*     */           try {
/* 198 */             AbstractScheduledService.this.shutDown();
/*     */           } catch (Exception ignored) {
/* 200 */             AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 205 */           AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/* 206 */           AbstractScheduledService.ServiceDelegate.this.runningTask.cancel(false);
/*     */         } finally {
/* 208 */           AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 213 */     private final Runnable task = new Task();
/*     */     
/*     */ 
/*     */     protected final void doStart()
/*     */     {
/* 218 */       this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this
/* 219 */         .executor(), new Supplier()
/*     */         {
/*     */           public String get()
/*     */           {
/* 223 */             return AbstractScheduledService.this.serviceName() + " " + AbstractScheduledService.ServiceDelegate.this.state();
/*     */           }
/* 225 */         });
/* 226 */       this.executorService.execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 230 */           AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */           try {
/* 232 */             AbstractScheduledService.this.startUp();
/* 233 */             AbstractScheduledService.ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, AbstractScheduledService.ServiceDelegate.this.executorService, AbstractScheduledService.ServiceDelegate.this.task);
/* 234 */             AbstractScheduledService.ServiceDelegate.this.notifyStarted();
/*     */           } catch (Throwable t) {
/* 236 */             AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/* 237 */             if (AbstractScheduledService.ServiceDelegate.this.runningTask != null)
/*     */             {
/* 239 */               AbstractScheduledService.ServiceDelegate.this.runningTask.cancel(false);
/*     */             }
/*     */           } finally {
/* 242 */             AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     protected final void doStop()
/*     */     {
/* 250 */       this.runningTask.cancel(false);
/* 251 */       this.executorService.execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/* 256 */             AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */             try {
/* 258 */               if (AbstractScheduledService.ServiceDelegate.this.state() != Service.State.STOPPING)
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 263 */                 return;
/*     */               }
/* 265 */               AbstractScheduledService.this.shutDown();
/*     */             } finally {
/* 267 */               AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */             }
/* 269 */             AbstractScheduledService.ServiceDelegate.this.notifyStopped();
/*     */           } catch (Throwable t) {
/* 271 */             AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 279 */       return AbstractScheduledService.this.toString();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ScheduledExecutorService executor()
/*     */   {
/* 337 */     final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
/*     */     {
/*     */       public Thread newThread(Runnable runnable)
/*     */       {
/* 333 */         return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 342 */     });
/* 343 */     addListener(new Service.Listener()
/*     */     {
/*     */       public void terminated(Service.State from)
/*     */       {
/* 347 */         executor.shutdown();
/*     */       }
/*     */       
/*     */       public void failed(Service.State from, Throwable failure)
/*     */       {
/* 352 */         executor.shutdown();
/*     */       }
/*     */       
/* 355 */     }, MoreExecutors.directExecutor());
/* 356 */     return executor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String serviceName()
/*     */   {
/* 366 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 371 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */   
/*     */   public final boolean isRunning()
/*     */   {
/* 376 */     return this.delegate.isRunning();
/*     */   }
/*     */   
/*     */   public final Service.State state()
/*     */   {
/* 381 */     return this.delegate.state();
/*     */   }
/*     */   
/*     */ 
/*     */   public final void addListener(Service.Listener listener, Executor executor)
/*     */   {
/* 387 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */   
/*     */ 
/*     */   public final Throwable failureCause()
/*     */   {
/* 393 */     return this.delegate.failureCause();
/*     */   }
/*     */   
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync()
/*     */   {
/* 400 */     this.delegate.startAsync();
/* 401 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync()
/*     */   {
/* 408 */     this.delegate.stopAsync();
/* 409 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void awaitRunning()
/*     */   {
/* 415 */     this.delegate.awaitRunning();
/*     */   }
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 421 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */   public final void awaitTerminated()
/*     */   {
/* 427 */     this.delegate.awaitTerminated();
/*     */   }
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 433 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static abstract class CustomScheduler
/*     */     extends AbstractScheduledService.Scheduler
/*     */   {
/*     */     public CustomScheduler()
/*     */     {
/* 445 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private class ReschedulableCallable
/*     */       extends ForwardingFuture<Void>
/*     */       implements Callable<Void>
/*     */     {
/*     */       private final Runnable wrappedRunnable;
/*     */       
/*     */ 
/*     */ 
/*     */       private final ScheduledExecutorService executor;
/*     */       
/*     */ 
/*     */ 
/*     */       private final AbstractService service;
/*     */       
/*     */ 
/*     */ 
/* 467 */       private final ReentrantLock lock = new ReentrantLock();
/*     */       
/*     */       @GuardedBy("lock")
/*     */       private Future<Void> currentFuture;
/*     */       
/*     */ 
/*     */       ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
/*     */       {
/* 475 */         this.wrappedRunnable = runnable;
/* 476 */         this.executor = executor;
/* 477 */         this.service = service;
/*     */       }
/*     */       
/*     */       public Void call() throws Exception
/*     */       {
/* 482 */         this.wrappedRunnable.run();
/* 483 */         reschedule();
/* 484 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */       public void reschedule()
/*     */       {
/*     */         try
/*     */         {
/* 492 */           schedule = AbstractScheduledService.CustomScheduler.this.getNextSchedule();
/*     */         } catch (Throwable t) { AbstractScheduledService.CustomScheduler.Schedule schedule;
/* 494 */           this.service.notifyFailed(t); return;
/*     */         }
/*     */         
/*     */ 
/*     */         AbstractScheduledService.CustomScheduler.Schedule schedule;
/*     */         
/*     */ 
/* 501 */         Throwable scheduleFailure = null;
/* 502 */         this.lock.lock();
/*     */         try {
/* 504 */           if ((this.currentFuture == null) || (!this.currentFuture.isCancelled())) {
/* 505 */             this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (Throwable e)
/*     */         {
/*     */ 
/*     */ 
/* 516 */           scheduleFailure = e;
/*     */         } finally {
/* 518 */           this.lock.unlock();
/*     */         }
/*     */         
/* 521 */         if (scheduleFailure != null) {
/* 522 */           this.service.notifyFailed(scheduleFailure);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public boolean cancel(boolean mayInterruptIfRunning)
/*     */       {
/* 531 */         this.lock.lock();
/*     */         try {
/* 533 */           return this.currentFuture.cancel(mayInterruptIfRunning);
/*     */         } finally {
/* 535 */           this.lock.unlock();
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean isCancelled()
/*     */       {
/* 541 */         this.lock.lock();
/*     */         try {
/* 543 */           return this.currentFuture.isCancelled();
/*     */         } finally {
/* 545 */           this.lock.unlock();
/*     */         }
/*     */       }
/*     */       
/*     */       protected Future<Void> delegate()
/*     */       {
/* 551 */         throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
/*     */     {
/* 559 */       ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
/* 560 */       task.reschedule();
/* 561 */       return task;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected abstract Schedule getNextSchedule()
/*     */       throws Exception;
/*     */     
/*     */ 
/*     */ 
/*     */     @Beta
/*     */     protected static final class Schedule
/*     */     {
/*     */       private final long delay;
/*     */       
/*     */       private final TimeUnit unit;
/*     */       
/*     */ 
/*     */       public Schedule(long delay, TimeUnit unit)
/*     */       {
/* 581 */         this.delay = delay;
/* 582 */         this.unit = ((TimeUnit)Preconditions.checkNotNull(unit));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AbstractScheduledService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */