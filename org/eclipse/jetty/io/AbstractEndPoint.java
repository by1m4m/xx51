/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Scheduler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEndPoint
/*     */   extends IdleTimeout
/*     */   implements EndPoint
/*     */ {
/*  34 */   private static final Logger LOG = Log.getLogger(AbstractEndPoint.class);
/*     */   
/*  36 */   private final AtomicReference<State> _state = new AtomicReference(State.OPEN);
/*  37 */   private final long _created = System.currentTimeMillis();
/*     */   
/*     */   private volatile Connection _connection;
/*  40 */   private final FillInterest _fillInterest = new FillInterest()
/*     */   {
/*     */     protected void needsFillInterest()
/*     */       throws IOException
/*     */     {
/*  45 */       AbstractEndPoint.this.needsFillInterest();
/*     */     }
/*     */   };
/*     */   
/*  49 */   private final WriteFlusher _writeFlusher = new WriteFlusher(this)
/*     */   {
/*     */ 
/*     */     protected void onIncompleteFlush()
/*     */     {
/*  54 */       AbstractEndPoint.this.onIncompleteFlush();
/*     */     }
/*     */   };
/*     */   
/*     */   protected AbstractEndPoint(Scheduler scheduler)
/*     */   {
/*  60 */     super(scheduler);
/*     */   }
/*     */   
/*     */   protected final void shutdownInput()
/*     */   {
/*  65 */     if (LOG.isDebugEnabled()) {
/*  66 */       LOG.debug("shutdownInput {}", new Object[] { this });
/*     */     }
/*     */     for (;;) {
/*  69 */       State s = (State)this._state.get();
/*  70 */       switch (s)
/*     */       {
/*     */       case OPEN: 
/*  73 */         if (this._state.compareAndSet(s, State.ISHUTTING))
/*     */         {
/*     */           try
/*     */           {
/*  77 */             doShutdownInput();
/*     */             
/*     */ 
/*     */ 
/*  81 */             if (!this._state.compareAndSet(State.ISHUTTING, State.ISHUT))
/*     */             {
/*     */ 
/*     */ 
/*  85 */               if (this._state.get() == State.CLOSED) {
/*  86 */                 doOnClose(null);
/*     */               } else {
/*  88 */                 throw new IllegalStateException();
/*     */               }
/*     */             }
/*     */           }
/*     */           finally
/*     */           {
/*  81 */             if (!this._state.compareAndSet(State.ISHUTTING, State.ISHUT))
/*     */             {
/*     */ 
/*     */ 
/*  85 */               if (this._state.get() == State.CLOSED) {
/*  86 */                 doOnClose(null);
/*     */               } else
/*  88 */                 throw new IllegalStateException();
/*     */             }
/*     */           }
/*  91 */           return;
/*     */         }
/*     */         break;
/*     */       case ISHUTTING: case ISHUT: 
/*  95 */         return;
/*     */       
/*     */       case OSHUTTING: 
/*  98 */         if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/*     */ 
/* 101 */           return; }
/*     */         break;
/*     */       case OSHUT: 
/* 104 */         if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/*     */ 
/* 107 */           doOnClose(null);
/* 108 */           return;
/*     */         }
/*     */         break;
/* 111 */       case CLOSED:  return;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public final void shutdownOutput()
/*     */   {
/* 119 */     if (LOG.isDebugEnabled()) {
/* 120 */       LOG.debug("shutdownOutput {}", new Object[] { this });
/*     */     }
/*     */     for (;;) {
/* 123 */       State s = (State)this._state.get();
/* 124 */       switch (s)
/*     */       {
/*     */       case OPEN: 
/* 127 */         if (this._state.compareAndSet(s, State.OSHUTTING))
/*     */         {
/*     */           try
/*     */           {
/* 131 */             doShutdownOutput();
/*     */             
/*     */ 
/*     */ 
/* 135 */             if (!this._state.compareAndSet(State.OSHUTTING, State.OSHUT))
/*     */             {
/*     */ 
/*     */ 
/* 139 */               if (this._state.get() == State.CLOSED) {
/* 140 */                 doOnClose(null);
/*     */               } else {
/* 142 */                 throw new IllegalStateException();
/*     */               }
/*     */             }
/*     */           }
/*     */           finally
/*     */           {
/* 135 */             if (!this._state.compareAndSet(State.OSHUTTING, State.OSHUT))
/*     */             {
/*     */ 
/*     */ 
/* 139 */               if (this._state.get() == State.CLOSED) {
/* 140 */                 doOnClose(null);
/*     */               } else
/* 142 */                 throw new IllegalStateException();
/*     */             }
/*     */           }
/* 145 */           return;
/*     */         }
/*     */         break;
/* 148 */       case ISHUTTING:  if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/*     */ 
/* 151 */           return; }
/*     */         break;
/*     */       case ISHUT: 
/* 154 */         if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/*     */ 
/* 157 */           doOnClose(null);
/* 158 */           return;
/*     */         }
/*     */         break;
/*     */       case OSHUTTING: case OSHUT: 
/* 162 */         return;
/*     */       
/*     */       case CLOSED: 
/* 165 */         return;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public final void close()
/*     */   {
/* 173 */     if (LOG.isDebugEnabled())
/* 174 */       LOG.debug("close {}", new Object[] { this });
/* 175 */     close(null);
/*     */   }
/*     */   
/*     */   protected final void close(Throwable failure)
/*     */   {
/* 180 */     if (LOG.isDebugEnabled()) {
/* 181 */       LOG.debug("close({}) {}", new Object[] { failure, this });
/*     */     }
/*     */     for (;;) {
/* 184 */       State s = (State)this._state.get();
/* 185 */       switch (s)
/*     */       {
/*     */       case OPEN: 
/*     */       case ISHUT: 
/*     */       case OSHUT: 
/* 190 */         if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/* 192 */           doOnClose(failure);
/* 193 */           return;
/*     */         }
/*     */         break;
/*     */       case ISHUTTING: case OSHUTTING: 
/* 197 */         if (this._state.compareAndSet(s, State.CLOSED))
/*     */         {
/*     */ 
/* 200 */           return; }
/*     */         break;
/*     */       case CLOSED: 
/* 203 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doShutdownInput() {}
/*     */   
/*     */   protected void doShutdownOutput() {}
/*     */   
/*     */   /* Error */
/*     */   private void doOnClose(Throwable failure)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 38	org/eclipse/jetty/io/AbstractEndPoint:doClose	()V
/*     */     //   4: aload_1
/*     */     //   5: ifnonnull +10 -> 15
/*     */     //   8: aload_0
/*     */     //   9: invokevirtual 39	org/eclipse/jetty/io/AbstractEndPoint:onClose	()V
/*     */     //   12: goto +30 -> 42
/*     */     //   15: aload_0
/*     */     //   16: aload_1
/*     */     //   17: invokevirtual 40	org/eclipse/jetty/io/AbstractEndPoint:onClose	(Ljava/lang/Throwable;)V
/*     */     //   20: goto +22 -> 42
/*     */     //   23: astore_2
/*     */     //   24: aload_1
/*     */     //   25: ifnonnull +10 -> 35
/*     */     //   28: aload_0
/*     */     //   29: invokevirtual 39	org/eclipse/jetty/io/AbstractEndPoint:onClose	()V
/*     */     //   32: goto +8 -> 40
/*     */     //   35: aload_0
/*     */     //   36: aload_1
/*     */     //   37: invokevirtual 40	org/eclipse/jetty/io/AbstractEndPoint:onClose	(Ljava/lang/Throwable;)V
/*     */     //   40: aload_2
/*     */     //   41: athrow
/*     */     //   42: return
/*     */     // Line number table:
/*     */     //   Java source line #220	-> byte code offset #0
/*     */     //   Java source line #224	-> byte code offset #4
/*     */     //   Java source line #225	-> byte code offset #8
/*     */     //   Java source line #227	-> byte code offset #15
/*     */     //   Java source line #228	-> byte code offset #20
/*     */     //   Java source line #224	-> byte code offset #23
/*     */     //   Java source line #225	-> byte code offset #28
/*     */     //   Java source line #227	-> byte code offset #35
/*     */     //   Java source line #229	-> byte code offset #42
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	43	0	this	AbstractEndPoint
/*     */     //   0	43	1	failure	Throwable
/*     */     //   23	18	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	23	finally
/*     */   }
/*     */   
/*     */   protected void doClose() {}
/*     */   
/*     */   protected void onClose(Throwable failure)
/*     */   {
/* 237 */     super.onClose();
/* 238 */     this._writeFlusher.onFail(failure);
/* 239 */     this._fillInterest.onFail(failure);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOutputShutdown()
/*     */   {
/* 245 */     switch ((State)this._state.get())
/*     */     {
/*     */     case OSHUTTING: 
/*     */     case OSHUT: 
/*     */     case CLOSED: 
/* 250 */       return true;
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isInputShutdown()
/*     */   {
/* 258 */     switch ((State)this._state.get())
/*     */     {
/*     */     case ISHUTTING: 
/*     */     case ISHUT: 
/*     */     case CLOSED: 
/* 263 */       return true;
/*     */     }
/* 265 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 272 */     switch ((State)this._state.get())
/*     */     {
/*     */     case CLOSED: 
/* 275 */       return false;
/*     */     }
/* 277 */     return true;
/*     */   }
/*     */   
/*     */   public void checkFlush()
/*     */     throws IOException
/*     */   {
/* 283 */     State s = (State)this._state.get();
/* 284 */     switch (s)
/*     */     {
/*     */     case OSHUTTING: 
/*     */     case OSHUT: 
/*     */     case CLOSED: 
/* 289 */       throw new IOException(s.toString());
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void checkFill()
/*     */     throws IOException
/*     */   {
/* 297 */     State s = (State)this._state.get();
/* 298 */     switch (s)
/*     */     {
/*     */     case ISHUTTING: 
/*     */     case ISHUT: 
/*     */     case CLOSED: 
/* 303 */       throw new IOException(s.toString());
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getCreatedTimeStamp()
/*     */   {
/* 312 */     return this._created;
/*     */   }
/*     */   
/*     */ 
/*     */   public Connection getConnection()
/*     */   {
/* 318 */     return this._connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setConnection(Connection connection)
/*     */   {
/* 324 */     this._connection = connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOptimizedForDirectBuffers()
/*     */   {
/* 330 */     return false;
/*     */   }
/*     */   
/*     */   protected void reset()
/*     */   {
/* 335 */     this._state.set(State.OPEN);
/* 336 */     this._writeFlusher.onClose();
/* 337 */     this._fillInterest.onClose();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onOpen()
/*     */   {
/* 343 */     if (LOG.isDebugEnabled())
/* 344 */       LOG.debug("onOpen {}", new Object[] { this });
/* 345 */     if (this._state.get() != State.OPEN) {
/* 346 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onClose()
/*     */   {
/* 352 */     super.onClose();
/* 353 */     this._writeFlusher.onClose();
/* 354 */     this._fillInterest.onClose();
/*     */   }
/*     */   
/*     */ 
/*     */   public void fillInterested(Callback callback)
/*     */   {
/* 360 */     notIdle();
/* 361 */     this._fillInterest.register(callback);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean tryFillInterested(Callback callback)
/*     */   {
/* 367 */     notIdle();
/* 368 */     return this._fillInterest.tryRegister(callback);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFillInterested()
/*     */   {
/* 374 */     return this._fillInterest.isInterested();
/*     */   }
/*     */   
/*     */   public void write(Callback callback, ByteBuffer... buffers)
/*     */     throws IllegalStateException
/*     */   {
/* 380 */     this._writeFlusher.write(callback, buffers);
/*     */   }
/*     */   
/*     */   protected abstract void onIncompleteFlush();
/*     */   
/*     */   protected abstract void needsFillInterest() throws IOException;
/*     */   
/*     */   public FillInterest getFillInterest()
/*     */   {
/* 389 */     return this._fillInterest;
/*     */   }
/*     */   
/*     */   protected WriteFlusher getWriteFlusher()
/*     */   {
/* 394 */     return this._writeFlusher;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onIdleExpired(TimeoutException timeout)
/*     */   {
/* 400 */     Connection connection = this._connection;
/* 401 */     if ((connection != null) && (!connection.onIdleExpired())) {
/* 402 */       return;
/*     */     }
/* 404 */     boolean output_shutdown = isOutputShutdown();
/* 405 */     boolean input_shutdown = isInputShutdown();
/* 406 */     boolean fillFailed = this._fillInterest.onFail(timeout);
/* 407 */     boolean writeFailed = this._writeFlusher.onFail(timeout);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */     if ((isOpen()) && ((output_shutdown) || (input_shutdown)) && (!fillFailed) && (!writeFailed)) {
/* 417 */       close();
/*     */     } else {
/* 419 */       LOG.debug("Ignored idle endpoint {}", new Object[] { this });
/*     */     }
/*     */   }
/*     */   
/*     */   public void upgrade(Connection newConnection)
/*     */   {
/* 425 */     Connection old_connection = getConnection();
/*     */     
/* 427 */     if (LOG.isDebugEnabled()) {
/* 428 */       LOG.debug("{} upgrading from {} to {}", new Object[] { this, old_connection, newConnection });
/*     */     }
/*     */     
/* 431 */     ByteBuffer prefilled = (old_connection instanceof Connection.UpgradeFrom) ? ((Connection.UpgradeFrom)old_connection).onUpgradeFrom() : null;
/* 432 */     old_connection.onClose();
/* 433 */     old_connection.getEndPoint().setConnection(newConnection);
/*     */     
/* 435 */     if ((newConnection instanceof Connection.UpgradeTo)) {
/* 436 */       ((Connection.UpgradeTo)newConnection).onUpgradeTo(prefilled);
/* 437 */     } else if (BufferUtil.hasContent(prefilled)) {
/* 438 */       throw new IllegalStateException();
/*     */     }
/* 440 */     newConnection.onOpen();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 446 */     return String.format("%s->%s", new Object[] { toEndPointString(), toConnectionString() });
/*     */   }
/*     */   
/*     */   public String toEndPointString()
/*     */   {
/* 451 */     Class<?> c = getClass();
/* 452 */     String name = c.getSimpleName();
/* 453 */     while ((name.length() == 0) && (c.getSuperclass() != null))
/*     */     {
/* 455 */       c = c.getSuperclass();
/* 456 */       name = c.getSimpleName();
/*     */     }
/*     */     
/* 459 */     return String.format("%s@%h{%s<->%s,%s,fill=%s,flush=%s,to=%d/%d}", new Object[] { name, this, 
/*     */     
/*     */ 
/* 462 */       getRemoteAddress(), 
/* 463 */       getLocalAddress(), this._state
/* 464 */       .get(), this._fillInterest
/* 465 */       .toStateString(), this._writeFlusher
/* 466 */       .toStateString(), 
/* 467 */       Long.valueOf(getIdleFor()), 
/* 468 */       Long.valueOf(getIdleTimeout()) });
/*     */   }
/*     */   
/*     */   public String toConnectionString()
/*     */   {
/* 473 */     Connection connection = getConnection();
/* 474 */     if (connection == null)
/* 475 */       return "<null>";
/* 476 */     if ((connection instanceof AbstractConnection))
/* 477 */       return ((AbstractConnection)connection).toConnectionString();
/* 478 */     return String.format("%s@%x", new Object[] { connection.getClass().getSimpleName(), Integer.valueOf(connection.hashCode()) });
/*     */   }
/*     */   
/*     */ 
/*     */   private static enum State
/*     */   {
/* 484 */     OPEN,  ISHUTTING,  ISHUT,  OSHUTTING,  OSHUT,  CLOSED;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\AbstractEndPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */