/*     */ package org.eclipse.jetty.util.thread.strategy;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy.Producer;
/*     */ import org.eclipse.jetty.util.thread.Locker;
/*     */ import org.eclipse.jetty.util.thread.Locker.Lock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProduceConsume
/*     */   implements ExecutionStrategy, Runnable
/*     */ {
/*  35 */   private static final Logger LOG = Log.getLogger(ExecuteProduceConsume.class);
/*     */   
/*  37 */   private final Locker _locker = new Locker();
/*     */   private final ExecutionStrategy.Producer _producer;
/*     */   private final Executor _executor;
/*  40 */   private State _state = State.IDLE;
/*     */   
/*     */   public ProduceConsume(ExecutionStrategy.Producer producer, Executor executor)
/*     */   {
/*  44 */     this._producer = producer;
/*  45 */     this._executor = executor;
/*     */   }
/*     */   
/*     */ 
/*     */   public void produce()
/*     */   {
/*  51 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable2 = null;
/*     */     try {
/*  53 */       switch (this._state)
/*     */       {
/*     */       case IDLE: 
/*  56 */         this._state = State.PRODUCE;
/*  57 */         break;
/*     */       
/*     */       case PRODUCE: 
/*     */       case EXECUTE: 
/*  61 */         this._state = State.EXECUTE;
/*  62 */         return;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*  51 */       localThrowable2 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */       if (lock != null) { $closeResource(localThrowable2, lock);
/*     */       }
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/*  70 */       Runnable task = this._producer.produce();
/*  71 */       if (LOG.isDebugEnabled()) {
/*  72 */         LOG.debug("{} produced {}", new Object[] { this._producer, task });
/*     */       }
/*  74 */       if (task == null)
/*     */       {
/*  76 */         Locker.Lock lock = this._locker.lock();localThrowable = null;
/*     */         try {
/*  78 */           switch (this._state)
/*     */           {
/*     */           case IDLE: 
/*  81 */             throw new IllegalStateException();
/*     */           case PRODUCE: 
/*  83 */             this._state = State.IDLE;
/*  84 */             return;
/*     */           case EXECUTE: 
/*  86 */             this._state = State.PRODUCE;
/*     */             
/*     */ 
/*  89 */             if (lock == null) continue; $closeResource(localThrowable, lock); continue;
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable3)
/*     */         {
/*  76 */           localThrowable = localThrowable3;throw localThrowable3;
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
/*  89 */           if (lock != null) $closeResource(localThrowable, lock);
/*     */         }
/*     */       }
/*     */       else {
/*  93 */         task.run();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispatch()
/*     */   {
/* 100 */     this._executor.execute(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 106 */     produce();
/*     */   }
/*     */   
/*     */   private static enum State
/*     */   {
/* 111 */     IDLE,  PRODUCE,  EXECUTE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\strategy\ProduceConsume.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */