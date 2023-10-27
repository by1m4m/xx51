/*     */ package org.eclipse.jetty.util.thread.strategy;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy;
/*     */ import org.eclipse.jetty.util.thread.ExecutionStrategy.Producer;
/*     */ import org.eclipse.jetty.util.thread.Invocable;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecuteProduceConsume
/*     */   implements ExecutionStrategy, Runnable
/*     */ {
/*  46 */   private static final Logger LOG = Log.getLogger(ExecuteProduceConsume.class);
/*     */   
/*  48 */   private final Locker _locker = new Locker();
/*  49 */   private final Runnable _runProduce = new RunProduce(null);
/*     */   private final ExecutionStrategy.Producer _producer;
/*     */   private final Executor _executor;
/*  52 */   private boolean _idle = true;
/*     */   
/*     */   private boolean _execute;
/*     */   private boolean _producing;
/*     */   private boolean _pending;
/*     */   
/*     */   public ExecuteProduceConsume(ExecutionStrategy.Producer producer, Executor executor)
/*     */   {
/*  60 */     this._producer = producer;
/*  61 */     this._executor = executor;
/*     */   }
/*     */   
/*     */ 
/*     */   public void produce()
/*     */   {
/*  67 */     if (LOG.isDebugEnabled()) {
/*  68 */       LOG.debug("{} execute", new Object[] { this });
/*     */     }
/*  70 */     boolean produce = false;
/*  71 */     Locker.Lock locked = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try
/*     */     {
/*  74 */       if (this._idle)
/*     */       {
/*  76 */         if (this._producing) {
/*  77 */           throw new IllegalStateException();
/*     */         }
/*     */         
/*  80 */         produce = this._producing = 1;
/*     */         
/*  82 */         this._idle = false;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  88 */         this._execute = true;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*  71 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*  90 */       if (locked != null) $closeResource(localThrowable1, locked);
/*     */     }
/*  92 */     if (produce) {
/*  93 */       produceConsume();
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispatch()
/*     */   {
/*  99 */     if (LOG.isDebugEnabled())
/* 100 */       LOG.debug("{} spawning", new Object[] { this });
/* 101 */     boolean dispatch = false;
/* 102 */     Locker.Lock locked = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 104 */       if (this._idle) {
/* 105 */         dispatch = true;
/*     */       } else {
/* 107 */         this._execute = true;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 102 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/* 108 */       if (locked != null) $closeResource(localThrowable1, locked); }
/* 109 */     if (dispatch) {
/* 110 */       this._executor.execute(this._runProduce);
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/* 116 */     if (LOG.isDebugEnabled())
/* 117 */       LOG.debug("{} run", new Object[] { this });
/* 118 */     boolean produce = false;
/* 119 */     Locker.Lock locked = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 121 */       this._pending = false;
/* 122 */       if ((!this._idle) && (!this._producing))
/*     */       {
/* 124 */         produce = this._producing = 1;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 119 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/* 126 */       if (locked != null) $closeResource(localThrowable1, locked);
/*     */     }
/* 128 */     if (produce) {
/* 129 */       produceConsume();
/*     */     }
/*     */   }
/*     */   
/*     */   private void produceConsume() {
/* 134 */     if (LOG.isDebugEnabled()) {
/* 135 */       LOG.debug("{} produce enter", new Object[] { this });
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/* 140 */       if (LOG.isDebugEnabled()) {
/* 141 */         LOG.debug("{} producing", new Object[] { this });
/*     */       }
/* 143 */       Runnable task = this._producer.produce();
/*     */       
/* 145 */       if (LOG.isDebugEnabled()) {
/* 146 */         LOG.debug("{} produced {}", new Object[] { this, task });
/*     */       }
/* 148 */       boolean dispatch = false;
/* 149 */       Locker.Lock locked = this._locker.lock();Throwable localThrowable2 = null;
/*     */       try
/*     */       {
/* 152 */         this._producing = false;
/*     */         
/*     */ 
/* 155 */         if (task == null)
/*     */         {
/*     */ 
/*     */ 
/* 159 */           if (this._execute)
/*     */           {
/* 161 */             this._idle = false;
/* 162 */             this._producing = true;
/* 163 */             this._execute = false;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */             if (locked == null) continue; $closeResource(localThrowable2, locked); continue;
/*     */           }
/* 168 */           this._idle = true;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */           if (locked == null) break; $closeResource(localThrowable2, locked); break;
/*     */         }
/* 174 */         if (!this._pending)
/*     */         {
/*     */ 
/* 177 */           dispatch = this._pending = Invocable.getInvocationType(task) != Invocable.InvocationType.NON_BLOCKING ? 1 : 0;
/*     */         }
/*     */         
/* 180 */         this._execute = false;
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 149 */         localThrowable2 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */         if (locked != null) { $closeResource(localThrowable2, locked);
/*     */         }
/*     */       }
/* 184 */       if (dispatch)
/*     */       {
/*     */ 
/* 187 */         if (LOG.isDebugEnabled())
/* 188 */           LOG.debug("{} dispatch", new Object[] { this });
/* 189 */         this._executor.execute(this);
/*     */       }
/*     */       
/*     */ 
/* 193 */       if (LOG.isDebugEnabled())
/* 194 */         LOG.debug("{} run {}", new Object[] { this, task });
/* 195 */       if (task != null)
/* 196 */         task.run();
/* 197 */       if (LOG.isDebugEnabled()) {
/* 198 */         LOG.debug("{} ran {}", new Object[] { this, task });
/*     */       }
/*     */       
/* 201 */       Locker.Lock locked = this._locker.lock();localThrowable2 = null;
/*     */       try
/*     */       {
/* 204 */         if ((this._producing) || (this._idle))
/*     */         {
/*     */ 
/* 207 */           if (locked == null) break; $closeResource(localThrowable2, locked); break;
/*     */         }
/* 206 */         this._producing = true;
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 201 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 207 */         if (locked != null) $closeResource(localThrowable2, locked);
/*     */       }
/*     */     }
/* 210 */     if (LOG.isDebugEnabled()) {
/* 211 */       LOG.debug("{} produce exit", new Object[] { this });
/*     */     }
/*     */   }
/*     */   
/*     */   public Boolean isIdle() {
/* 216 */     Locker.Lock locked = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 218 */       return Boolean.valueOf(this._idle);
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 216 */       localThrowable1 = localThrowable2;throw localThrowable2;
/*     */     }
/*     */     finally {
/* 219 */       if (locked != null) $closeResource(localThrowable1, locked);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 225 */     StringBuilder builder = new StringBuilder();
/* 226 */     builder.append("EPC ");
/* 227 */     Locker.Lock locked = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 229 */       builder.append(this._idle ? "Idle/" : "");
/* 230 */       builder.append(this._producing ? "Prod/" : "");
/* 231 */       builder.append(this._pending ? "Pend/" : "");
/* 232 */       builder.append(this._execute ? "Exec/" : "");
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 227 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/* 233 */       if (locked != null) $closeResource(localThrowable1, locked); }
/* 234 */     builder.append(this._producer);
/* 235 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private class RunProduce implements Runnable
/*     */   {
/*     */     private RunProduce() {}
/*     */     
/*     */     public void run() {
/* 243 */       ExecuteProduceConsume.this.produce();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\strategy\ExecuteProduceConsume.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */