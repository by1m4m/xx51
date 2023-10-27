/*     */ package org.eclipse.jetty.util.thread.strategy;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.atomic.LongAdder;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.annotation.ManagedOperation;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy.Producer;
/*     */ import org.eclipse.jetty.util.thread.Invocable;
/*     */ import org.eclipse.jetty.util.thread.TryExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ManagedObject("eat what you kill execution strategy")
/*     */ public class EatWhatYouKill
/*     */   extends ContainerLifeCycle
/*     */   implements ExecutionStrategy, Runnable
/*     */ {
/*  66 */   private static final Logger LOG = Log.getLogger(EatWhatYouKill.class);
/*     */   
/*  68 */   private static enum State { IDLE,  PRODUCING,  REPRODUCING;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*  73 */   private static enum Mode { PRODUCE_CONSUME, 
/*  74 */     PRODUCE_INVOKE_CONSUME, 
/*  75 */     PRODUCE_EXECUTE_CONSUME, 
/*  76 */     EXECUTE_PRODUCE_CONSUME;
/*     */     
/*     */     private Mode() {} }
/*  79 */   private final LongAdder _pcMode = new LongAdder();
/*  80 */   private final LongAdder _picMode = new LongAdder();
/*  81 */   private final LongAdder _pecMode = new LongAdder();
/*  82 */   private final LongAdder _epcMode = new LongAdder();
/*     */   private final ExecutionStrategy.Producer _producer;
/*     */   private final Executor _executor;
/*     */   private final TryExecutor _tryExecutor;
/*  86 */   private State _state = State.IDLE;
/*     */   private boolean _pending;
/*     */   
/*     */   public EatWhatYouKill(ExecutionStrategy.Producer producer, Executor executor)
/*     */   {
/*  91 */     this._producer = producer;
/*  92 */     this._executor = executor;
/*  93 */     this._tryExecutor = TryExecutor.asTryExecutor(executor);
/*  94 */     addBean(this._producer);
/*  95 */     addBean(this._tryExecutor);
/*  96 */     if (LOG.isDebugEnabled()) {
/*  97 */       LOG.debug("{} created", new Object[] { this });
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispatch()
/*     */   {
/* 103 */     boolean execute = false;
/* 104 */     synchronized (this)
/*     */     {
/* 106 */       switch (this._state)
/*     */       {
/*     */       case IDLE: 
/* 109 */         if (!this._pending)
/*     */         {
/* 111 */           this._pending = true;
/* 112 */           execute = true;
/*     */         }
/*     */         
/*     */         break;
/*     */       case PRODUCING: 
/* 117 */         this._state = State.REPRODUCING;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 124 */     if (LOG.isDebugEnabled())
/* 125 */       LOG.debug("{} dispatch {}", new Object[] { this, Boolean.valueOf(execute) });
/* 126 */     if (execute) {
/* 127 */       this._executor.execute(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/* 133 */     tryProduce(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public void produce()
/*     */   {
/* 139 */     tryProduce(false);
/*     */   }
/*     */   
/*     */   private void tryProduce(boolean wasPending)
/*     */   {
/* 144 */     if (LOG.isDebugEnabled()) {
/* 145 */       LOG.debug("{} tryProduce {}", new Object[] { this, Boolean.valueOf(wasPending) });
/*     */     }
/* 147 */     synchronized (this)
/*     */     {
/* 149 */       if (wasPending) {
/* 150 */         this._pending = false;
/*     */       }
/* 152 */       switch (this._state)
/*     */       {
/*     */ 
/*     */       case IDLE: 
/* 156 */         this._state = State.PRODUCING;
/* 157 */         break;
/*     */       
/*     */ 
/*     */       case PRODUCING: 
/* 161 */         this._state = State.REPRODUCING;
/* 162 */         return;
/*     */       
/*     */       default: 
/* 165 */         return;
/*     */       }
/*     */       
/*     */     }
/* 169 */     boolean non_blocking = Invocable.isNonBlockingInvocation();
/*     */     
/* 171 */     while (isRunning())
/*     */     {
/*     */       try
/*     */       {
/* 175 */         if (!doProduce(non_blocking))
/*     */         {
/* 177 */           return;
/*     */         }
/*     */       }
/*     */       catch (Throwable th) {
/* 181 */         LOG.warn(th);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean doProduce(boolean non_blocking)
/*     */   {
/* 189 */     Runnable task = produceTask();
/*     */     
/* 191 */     if (task == null)
/*     */     {
/* 193 */       synchronized (this)
/*     */       {
/*     */ 
/* 196 */         switch (this._state)
/*     */         {
/*     */         case PRODUCING: 
/* 199 */           this._state = State.IDLE;
/* 200 */           return false;
/*     */         
/*     */         case REPRODUCING: 
/* 203 */           this._state = State.PRODUCING;
/* 204 */           return true;
/*     */         }
/*     */         
/* 207 */         throw new IllegalStateException(toStringLocked());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 213 */     if (non_blocking) {
/*     */       Mode mode;
/*     */       Mode mode;
/*     */       Mode mode;
/* 217 */       switch (Invocable.getInvocationType(task))
/*     */       {
/*     */       case NON_BLOCKING: 
/* 220 */         mode = Mode.PRODUCE_CONSUME;
/* 221 */         break;
/*     */       
/*     */       case EITHER: 
/* 224 */         mode = Mode.PRODUCE_INVOKE_CONSUME;
/* 225 */         break;
/*     */       
/*     */       default: 
/* 228 */         mode = Mode.PRODUCE_EXECUTE_CONSUME;break;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       Mode mode;
/*     */       Mode mode;
/* 235 */       switch (Invocable.getInvocationType(task))
/*     */       {
/*     */       case NON_BLOCKING: 
/* 238 */         mode = Mode.PRODUCE_CONSUME;
/* 239 */         break;
/*     */       case BLOCKING: 
/*     */         Mode mode;
/*     */         
/*     */ 
/* 244 */         synchronized (this) {
/*     */           Mode mode;
/* 246 */           if (this._pending)
/*     */           {
/* 248 */             this._state = State.IDLE;
/* 249 */             mode = Mode.EXECUTE_PRODUCE_CONSUME;
/*     */           } else { Mode mode;
/* 251 */             if (this._tryExecutor.tryExecute(this))
/*     */             {
/* 253 */               this._pending = true;
/* 254 */               this._state = State.IDLE;
/* 255 */               mode = Mode.EXECUTE_PRODUCE_CONSUME;
/*     */             }
/*     */             else
/*     */             {
/* 259 */               mode = Mode.PRODUCE_EXECUTE_CONSUME;
/*     */             }
/*     */           }
/*     */         }
/*     */       case EITHER: 
/*     */         Mode mode;
/*     */         
/*     */ 
/*     */ 
/* 268 */         synchronized (this) {
/*     */           Mode mode;
/* 270 */           if (this._pending)
/*     */           {
/* 272 */             this._state = State.IDLE;
/* 273 */             mode = Mode.EXECUTE_PRODUCE_CONSUME;
/*     */           } else { Mode mode;
/* 275 */             if (this._tryExecutor.tryExecute(this))
/*     */             {
/* 277 */               this._pending = true;
/* 278 */               this._state = State.IDLE;
/* 279 */               mode = Mode.EXECUTE_PRODUCE_CONSUME;
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/* 285 */               mode = Mode.PRODUCE_INVOKE_CONSUME;
/*     */             }
/*     */           }
/*     */         }
/* 289 */         break;
/*     */       
/*     */       default: 
/* 292 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/*     */     Mode mode;
/* 296 */     if (LOG.isDebugEnabled()) {
/* 297 */       LOG.debug("{} m={} t={}/{}", new Object[] { this, mode, task, Invocable.getInvocationType(task) });
/*     */     }
/*     */     
/* 300 */     switch (mode)
/*     */     {
/*     */     case PRODUCE_CONSUME: 
/* 303 */       this._pcMode.increment();
/* 304 */       task.run();
/* 305 */       return true;
/*     */     
/*     */     case PRODUCE_INVOKE_CONSUME: 
/* 308 */       this._picMode.increment();
/* 309 */       Invocable.invokeNonBlocking(task);
/* 310 */       return true;
/*     */     
/*     */     case PRODUCE_EXECUTE_CONSUME: 
/* 313 */       this._pecMode.increment();
/* 314 */       execute(task);
/* 315 */       return true;
/*     */     
/*     */     case EXECUTE_PRODUCE_CONSUME: 
/* 318 */       this._epcMode.increment();
/* 319 */       task.run();
/*     */       
/*     */ 
/* 322 */       synchronized (this)
/*     */       {
/* 324 */         if (this._state == State.IDLE)
/*     */         {
/*     */ 
/* 327 */           this._state = State.PRODUCING;
/* 328 */           return true;
/*     */         }
/*     */       }
/* 331 */       return false;
/*     */     }
/*     */     
/* 334 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */   private Runnable produceTask()
/*     */   {
/*     */     try
/*     */     {
/* 342 */       return this._producer.produce();
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 346 */       LOG.warn(e);
/*     */     }
/* 348 */     return null;
/*     */   }
/*     */   
/*     */   private void execute(Runnable task)
/*     */   {
/*     */     try
/*     */     {
/* 355 */       this._executor.execute(task);
/*     */     }
/*     */     catch (RejectedExecutionException e)
/*     */     {
/* 359 */       if (isRunning()) {
/* 360 */         LOG.warn(e);
/*     */       } else {
/* 362 */         LOG.ignore(e);
/*     */       }
/* 364 */       if ((task instanceof Closeable))
/*     */       {
/*     */         try
/*     */         {
/* 368 */           ((Closeable)task).close();
/*     */         }
/*     */         catch (Throwable e2)
/*     */         {
/* 372 */           LOG.ignore(e2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="number of tasks consumed with PC mode", readonly=true)
/*     */   public long getPCTasksConsumed()
/*     */   {
/* 381 */     return this._pcMode.longValue();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="number of tasks executed with PIC mode", readonly=true)
/*     */   public long getPICTasksExecuted()
/*     */   {
/* 387 */     return this._picMode.longValue();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="number of tasks executed with PEC mode", readonly=true)
/*     */   public long getPECTasksExecuted()
/*     */   {
/* 393 */     return this._pecMode.longValue();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="number of tasks consumed with EPC mode", readonly=true)
/*     */   public long getEPCTasksConsumed()
/*     */   {
/* 399 */     return this._epcMode.longValue();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="whether this execution strategy is idle", readonly=true)
/*     */   public boolean isIdle()
/*     */   {
/* 405 */     synchronized (this)
/*     */     {
/* 407 */       return this._state == State.IDLE;
/*     */     }
/*     */   }
/*     */   
/*     */   @ManagedOperation(value="resets the task counts", impact="ACTION")
/*     */   public void reset()
/*     */   {
/* 414 */     this._pcMode.reset();
/* 415 */     this._epcMode.reset();
/* 416 */     this._pecMode.reset();
/* 417 */     this._picMode.reset();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String toString()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual 37	org/eclipse/jetty/util/thread/strategy/EatWhatYouKill:toStringLocked	()Ljava/lang/String;
/*     */     //   8: aload_1
/*     */     //   9: monitorexit
/*     */     //   10: areturn
/*     */     //   11: astore_2
/*     */     //   12: aload_1
/*     */     //   13: monitorexit
/*     */     //   14: aload_2
/*     */     //   15: athrow
/*     */     // Line number table:
/*     */     //   Java source line #423	-> byte code offset #0
/*     */     //   Java source line #425	-> byte code offset #4
/*     */     //   Java source line #426	-> byte code offset #11
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	16	0	this	EatWhatYouKill
/*     */     //   2	11	1	Ljava/lang/Object;	Object
/*     */     //   11	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	10	11	finally
/*     */     //   11	14	11	finally
/*     */   }
/*     */   
/*     */   public String toStringLocked()
/*     */   {
/* 431 */     StringBuilder builder = new StringBuilder();
/* 432 */     getString(builder);
/* 433 */     getState(builder);
/* 434 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private void getString(StringBuilder builder)
/*     */   {
/* 439 */     builder.append(getClass().getSimpleName());
/* 440 */     builder.append('@');
/* 441 */     builder.append(Integer.toHexString(hashCode()));
/* 442 */     builder.append('/');
/* 443 */     builder.append(this._producer);
/* 444 */     builder.append('/');
/*     */   }
/*     */   
/*     */   private void getState(StringBuilder builder)
/*     */   {
/* 449 */     builder.append(this._state);
/* 450 */     builder.append("/p=");
/* 451 */     builder.append(this._pending);
/* 452 */     builder.append('/');
/* 453 */     builder.append(this._tryExecutor);
/* 454 */     builder.append("[pc=");
/* 455 */     builder.append(getPCTasksConsumed());
/* 456 */     builder.append(",pic=");
/* 457 */     builder.append(getPICTasksExecuted());
/* 458 */     builder.append(",pec=");
/* 459 */     builder.append(getPECTasksExecuted());
/* 460 */     builder.append(",epc=");
/* 461 */     builder.append(getEPCTasksConsumed());
/* 462 */     builder.append("]");
/* 463 */     builder.append("@");
/* 464 */     builder.append(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\strategy\EatWhatYouKill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */