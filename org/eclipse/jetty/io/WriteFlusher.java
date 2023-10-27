/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.WritePendingException;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Invocable;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WriteFlusher
/*     */ {
/*  47 */   private static final Logger LOG = Log.getLogger(WriteFlusher.class);
/*  48 */   private static final boolean DEBUG = LOG.isDebugEnabled();
/*  49 */   private static final ByteBuffer[] EMPTY_BUFFERS = { BufferUtil.EMPTY_BUFFER };
/*  50 */   private static final EnumMap<StateType, Set<StateType>> __stateTransitions = new EnumMap(StateType.class);
/*  51 */   private static final State __IDLE = new IdleState(null);
/*  52 */   private static final State __WRITING = new WritingState(null);
/*  53 */   private static final State __COMPLETING = new CompletingState(null);
/*     */   private final EndPoint _endPoint;
/*  55 */   private final AtomicReference<State> _state = new AtomicReference();
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  60 */     __stateTransitions.put(StateType.IDLE, EnumSet.of(StateType.WRITING));
/*  61 */     __stateTransitions.put(StateType.WRITING, EnumSet.of(StateType.IDLE, StateType.PENDING, StateType.FAILED));
/*  62 */     __stateTransitions.put(StateType.PENDING, EnumSet.of(StateType.COMPLETING, StateType.IDLE));
/*  63 */     __stateTransitions.put(StateType.COMPLETING, EnumSet.of(StateType.IDLE, StateType.PENDING, StateType.FAILED));
/*  64 */     __stateTransitions.put(StateType.FAILED, EnumSet.of(StateType.IDLE));
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
/*     */   protected WriteFlusher(EndPoint endPoint)
/*     */   {
/*  91 */     this._state.set(__IDLE);
/*  92 */     this._endPoint = endPoint;
/*     */   }
/*     */   
/*     */   private static enum StateType
/*     */   {
/*  97 */     IDLE, 
/*  98 */     WRITING, 
/*  99 */     PENDING, 
/* 100 */     COMPLETING, 
/* 101 */     FAILED;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private StateType() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean updateState(State previous, State next)
/*     */   {
/* 114 */     if (!isTransitionAllowed(previous, next)) {
/* 115 */       throw new IllegalStateException();
/*     */     }
/* 117 */     boolean updated = this._state.compareAndSet(previous, next);
/* 118 */     if (DEBUG)
/* 119 */       LOG.debug("update {}:{}{}{}", new Object[] { this, previous, updated ? "-->" : "!->", next });
/* 120 */     return updated;
/*     */   }
/*     */   
/*     */   private void fail(PendingState pending)
/*     */   {
/* 125 */     State current = (State)this._state.get();
/* 126 */     if (current.getType() == StateType.FAILED)
/*     */     {
/* 128 */       FailedState failed = (FailedState)current;
/* 129 */       if (updateState(failed, __IDLE))
/*     */       {
/* 131 */         pending.fail(failed.getCause());
/* 132 */         return;
/*     */       }
/*     */     }
/* 135 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   private void ignoreFail()
/*     */   {
/* 140 */     State current = (State)this._state.get();
/* 141 */     while (current.getType() == StateType.FAILED)
/*     */     {
/* 143 */       if (updateState(current, __IDLE))
/* 144 */         return;
/* 145 */       current = (State)this._state.get();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isTransitionAllowed(State currentState, State newState)
/*     */   {
/* 151 */     Set<StateType> allowedNewStateTypes = (Set)__stateTransitions.get(currentState.getType());
/* 152 */     if (!allowedNewStateTypes.contains(newState.getType()))
/*     */     {
/* 154 */       LOG.warn("{}: {} -> {} not allowed", new Object[] { this, currentState, newState });
/* 155 */       return false;
/*     */     }
/* 157 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class State
/*     */   {
/*     */     private final WriteFlusher.StateType _type;
/*     */     
/*     */ 
/*     */     private State(WriteFlusher.StateType stateType)
/*     */     {
/* 169 */       this._type = stateType;
/*     */     }
/*     */     
/*     */     public WriteFlusher.StateType getType()
/*     */     {
/* 174 */       return this._type;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 180 */       return String.format("%s", new Object[] { this._type });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class IdleState
/*     */     extends WriteFlusher.State
/*     */   {
/*     */     private IdleState()
/*     */     {
/* 191 */       super(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class WritingState
/*     */     extends WriteFlusher.State
/*     */   {
/*     */     private WritingState()
/*     */     {
/* 202 */       super(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class FailedState
/*     */     extends WriteFlusher.State
/*     */   {
/*     */     private final Throwable _cause;
/*     */     
/*     */ 
/*     */     private FailedState(Throwable cause)
/*     */     {
/* 215 */       super(null);
/* 216 */       this._cause = cause;
/*     */     }
/*     */     
/*     */     public Throwable getCause()
/*     */     {
/* 221 */       return this._cause;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class CompletingState
/*     */     extends WriteFlusher.State
/*     */   {
/*     */     private CompletingState()
/*     */     {
/* 234 */       super(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class PendingState
/*     */     extends WriteFlusher.State
/*     */   {
/*     */     private final Callback _callback;
/*     */     
/*     */     private final ByteBuffer[] _buffers;
/*     */     
/*     */ 
/*     */     private PendingState(ByteBuffer[] buffers, Callback callback)
/*     */     {
/* 249 */       super(null);
/* 250 */       this._buffers = buffers;
/* 251 */       this._callback = callback;
/*     */     }
/*     */     
/*     */     public ByteBuffer[] getBuffers()
/*     */     {
/* 256 */       return this._buffers;
/*     */     }
/*     */     
/*     */     protected boolean fail(Throwable cause)
/*     */     {
/* 261 */       if (this._callback != null)
/*     */       {
/* 263 */         this._callback.failed(cause);
/* 264 */         return true;
/*     */       }
/* 266 */       return false;
/*     */     }
/*     */     
/*     */     protected void complete()
/*     */     {
/* 271 */       if (this._callback != null) {
/* 272 */         this._callback.succeeded();
/*     */       }
/*     */     }
/*     */     
/*     */     Invocable.InvocationType getCallbackInvocationType() {
/* 277 */       return Invocable.getInvocationType(this._callback);
/*     */     }
/*     */     
/*     */     public Object getCallback()
/*     */     {
/* 282 */       return this._callback;
/*     */     }
/*     */   }
/*     */   
/*     */   public Invocable.InvocationType getCallbackInvocationType()
/*     */   {
/* 288 */     State s = (State)this._state.get();
/* 289 */     return (s instanceof PendingState) ? 
/* 290 */       ((PendingState)s).getCallbackInvocationType() : 
/* 291 */       Invocable.InvocationType.BLOCKING;
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
/*     */   public void write(Callback callback, ByteBuffer... buffers)
/*     */     throws WritePendingException
/*     */   {
/* 315 */     if (DEBUG) {
/* 316 */       LOG.debug("write: {} {}", new Object[] { this, BufferUtil.toDetailString(buffers) });
/*     */     }
/* 318 */     if (!updateState(__IDLE, __WRITING)) {
/* 319 */       throw new WritePendingException();
/*     */     }
/*     */     try
/*     */     {
/* 323 */       buffers = flush(buffers);
/*     */       
/*     */ 
/* 326 */       if (buffers != null)
/*     */       {
/* 328 */         if (DEBUG)
/* 329 */           LOG.debug("flushed incomplete", new Object[0]);
/* 330 */         PendingState pending = new PendingState(buffers, callback, null);
/* 331 */         if (updateState(__WRITING, pending)) {
/* 332 */           onIncompleteFlush();
/*     */         } else
/* 334 */           fail(pending);
/* 335 */         return;
/*     */       }
/*     */       
/*     */ 
/* 339 */       if (!updateState(__WRITING, __IDLE))
/* 340 */         ignoreFail();
/* 341 */       if (callback != null) {
/* 342 */         callback.succeeded();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 346 */       if (DEBUG)
/* 347 */         LOG.debug("write exception", e);
/* 348 */       if (updateState(__WRITING, __IDLE))
/*     */       {
/* 350 */         if (callback != null) {
/* 351 */           callback.failed(e);
/*     */         }
/*     */       } else {
/* 354 */         fail(new PendingState(buffers, callback, null));
/*     */       }
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
/*     */   public void completeWrite()
/*     */   {
/* 368 */     if (DEBUG) {
/* 369 */       LOG.debug("completeWrite: {}", new Object[] { this });
/*     */     }
/* 371 */     State previous = (State)this._state.get();
/*     */     
/* 373 */     if (previous.getType() != StateType.PENDING) {
/* 374 */       return;
/*     */     }
/* 376 */     PendingState pending = (PendingState)previous;
/* 377 */     if (!updateState(pending, __COMPLETING)) {
/* 378 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 382 */       ByteBuffer[] buffers = pending.getBuffers();
/*     */       
/* 384 */       buffers = flush(buffers);
/*     */       
/*     */ 
/* 387 */       if (buffers != null)
/*     */       {
/* 389 */         if (DEBUG)
/* 390 */           LOG.debug("flushed incomplete {}", new Object[] { BufferUtil.toDetailString(buffers) });
/* 391 */         if (buffers != pending.getBuffers())
/* 392 */           pending = new PendingState(buffers, pending._callback, null);
/* 393 */         if (updateState(__COMPLETING, pending)) {
/* 394 */           onIncompleteFlush();
/*     */         } else
/* 396 */           fail(pending);
/* 397 */         return;
/*     */       }
/*     */       
/*     */ 
/* 401 */       if (!updateState(__COMPLETING, __IDLE))
/* 402 */         ignoreFail();
/* 403 */       pending.complete();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 407 */       if (DEBUG)
/* 408 */         LOG.debug("completeWrite exception", e);
/* 409 */       if (updateState(__COMPLETING, __IDLE)) {
/* 410 */         pending.fail(e);
/*     */       } else {
/* 412 */         fail(pending);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuffer[] flush(ByteBuffer[] buffers)
/*     */     throws IOException
/*     */   {
/* 425 */     boolean progress = true;
/* 426 */     while ((progress) && (buffers != null))
/*     */     {
/* 428 */       long before = remaining(buffers);
/* 429 */       boolean flushed = this._endPoint.flush(buffers);
/* 430 */       long after = remaining(buffers);
/* 431 */       long written = before - after;
/*     */       
/* 433 */       if (LOG.isDebugEnabled()) {
/* 434 */         LOG.debug("Flushed={} written={} remaining={} {}", new Object[] { Boolean.valueOf(flushed), Long.valueOf(written), Long.valueOf(after), this });
/*     */       }
/* 436 */       if (written > 0L)
/*     */       {
/* 438 */         Connection connection = this._endPoint.getConnection();
/* 439 */         if ((connection instanceof Listener)) {
/* 440 */           ((Listener)connection).onFlushed(written);
/*     */         }
/*     */       }
/* 443 */       if (flushed) {
/* 444 */         return null;
/*     */       }
/* 446 */       progress = written > 0L;
/*     */       
/* 448 */       int index = 0;
/*     */       for (;;)
/*     */       {
/* 451 */         if (index == buffers.length)
/*     */         {
/*     */ 
/* 454 */           buffers = null;
/* 455 */           index = 0;
/* 456 */           break;
/*     */         }
/*     */         
/*     */ 
/* 460 */         int remaining = buffers[index].remaining();
/* 461 */         if (remaining > 0)
/*     */           break;
/* 463 */         index++;
/* 464 */         progress = true;
/*     */       }
/*     */       
/* 467 */       if (index > 0) {
/* 468 */         buffers = (ByteBuffer[])Arrays.copyOfRange(buffers, index, buffers.length);
/*     */       }
/*     */     }
/* 471 */     if (LOG.isDebugEnabled()) {
/* 472 */       LOG.debug("!fully flushed {}", new Object[] { this });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 477 */     return buffers == null ? EMPTY_BUFFERS : buffers;
/*     */   }
/*     */   
/*     */   private long remaining(ByteBuffer[] buffers)
/*     */   {
/* 482 */     if (buffers == null)
/* 483 */       return 0L;
/* 484 */     long result = 0L;
/* 485 */     for (ByteBuffer buffer : buffers)
/* 486 */       result += buffer.remaining();
/* 487 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onFail(Throwable cause)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 501 */       State current = (State)this._state.get();
/* 502 */       switch (current.getType())
/*     */       {
/*     */       case IDLE: 
/*     */       case FAILED: 
/* 506 */         if (DEBUG)
/* 507 */           LOG.debug("ignored: " + this, cause);
/* 508 */         return false;
/*     */       
/*     */       case PENDING: 
/* 511 */         if (DEBUG) {
/* 512 */           LOG.debug("failed: " + this, cause);
/*     */         }
/* 514 */         PendingState pending = (PendingState)current;
/* 515 */         if (updateState(pending, __IDLE)) {
/* 516 */           return pending.fail(cause);
/*     */         }
/*     */         break;
/*     */       default: 
/* 520 */         if (DEBUG) {
/* 521 */           LOG.debug("failed: " + this, cause);
/*     */         }
/* 523 */         if (updateState(current, new FailedState(cause, null))) {
/* 524 */           return false;
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 532 */     onFail(new ClosedChannelException());
/*     */   }
/*     */   
/*     */   boolean isIdle()
/*     */   {
/* 537 */     return ((State)this._state.get()).getType() == StateType.IDLE;
/*     */   }
/*     */   
/*     */   public String toStateString()
/*     */   {
/* 542 */     switch (((State)this._state.get()).getType())
/*     */     {
/*     */     case WRITING: 
/* 545 */       return "W";
/*     */     case PENDING: 
/* 547 */       return "P";
/*     */     case COMPLETING: 
/* 549 */       return "C";
/*     */     case IDLE: 
/* 551 */       return "-";
/*     */     case FAILED: 
/* 553 */       return "F";
/*     */     }
/* 555 */     return "?";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 562 */     State s = (State)this._state.get();
/* 563 */     return String.format("WriteFlusher@%x{%s}->%s", new Object[] { Integer.valueOf(hashCode()), s, (s instanceof PendingState) ? ((PendingState)s).getCallback() : null });
/*     */   }
/*     */   
/*     */   protected abstract void onIncompleteFlush();
/*     */   
/*     */   public static abstract interface Listener
/*     */   {
/*     */     public abstract void onFlushed(long paramLong)
/*     */       throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\WriteFlusher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */