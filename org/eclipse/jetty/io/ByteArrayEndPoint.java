/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Locker;
/*     */ import org.eclipse.jetty.util.thread.Locker.Lock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayEndPoint
/*     */   extends AbstractEndPoint
/*     */ {
/*  48 */   static final Logger LOG = Log.getLogger(ByteArrayEndPoint.class);
/*     */   static final InetAddress NOIP;
/*     */   static final InetSocketAddress NOIPPORT;
/*     */   
/*     */   static
/*     */   {
/*  54 */     InetAddress noip = null;
/*     */     try
/*     */     {
/*  57 */       noip = Inet4Address.getByName("0.0.0.0");
/*     */     }
/*     */     catch (UnknownHostException e)
/*     */     {
/*  61 */       LOG.warn(e);
/*     */     }
/*     */     finally
/*     */     {
/*  65 */       NOIP = noip;
/*  66 */       NOIPPORT = new InetSocketAddress(NOIP, 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  71 */   private static final ByteBuffer EOF = BufferUtil.allocate(0);
/*     */   
/*  73 */   private final Runnable _runFillable = new Runnable()
/*     */   {
/*     */ 
/*     */     public void run()
/*     */     {
/*  78 */       ByteArrayEndPoint.this.getFillInterest().fillable();
/*     */     }
/*     */   };
/*     */   
/*  82 */   private final Locker _locker = new Locker();
/*  83 */   private final Condition _hasOutput = this._locker.newCondition();
/*  84 */   private final Queue<ByteBuffer> _inQ = new ArrayDeque();
/*     */   
/*     */ 
/*     */   private ByteBuffer _out;
/*     */   
/*     */   private boolean _growOutput;
/*     */   
/*     */ 
/*     */   public ByteArrayEndPoint()
/*     */   {
/*  94 */     this(null, 0L, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteArrayEndPoint(byte[] input, int outputSize)
/*     */   {
/* 104 */     this(null, 0L, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteArrayEndPoint(String input, int outputSize)
/*     */   {
/* 114 */     this(null, 0L, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteArrayEndPoint(Scheduler scheduler, long idleTimeoutMs)
/*     */   {
/* 120 */     this(scheduler, idleTimeoutMs, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, byte[] input, int outputSize)
/*     */   {
/* 126 */     this(timer, idleTimeoutMs, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, String input, int outputSize)
/*     */   {
/* 132 */     this(timer, idleTimeoutMs, input != null ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteArrayEndPoint(Scheduler timer, long idleTimeoutMs, ByteBuffer input, ByteBuffer output)
/*     */   {
/* 138 */     super(timer);
/* 139 */     if (BufferUtil.hasContent(input))
/* 140 */       addInput(input);
/* 141 */     this._out = (output == null ? BufferUtil.allocate(1024) : output);
/* 142 */     setIdleTimeout(idleTimeoutMs);
/* 143 */     onOpen();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void doShutdownOutput()
/*     */   {
/* 150 */     super.doShutdownOutput();
/* 151 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 153 */       this._hasOutput.signalAll();
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 151 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */     }
/*     */     finally {
/* 154 */       if (lock != null) { $closeResource(localThrowable1, lock);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void doClose()
/*     */   {
/* 161 */     super.doClose();
/* 162 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 164 */       this._hasOutput.signalAll();
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 162 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */     }
/*     */     finally {
/* 165 */       if (lock != null) { $closeResource(localThrowable1, lock);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public InetSocketAddress getLocalAddress()
/*     */   {
/* 172 */     return NOIPPORT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InetSocketAddress getRemoteAddress()
/*     */   {
/* 179 */     return NOIPPORT;
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
/*     */   protected void execute(Runnable task)
/*     */   {
/* 192 */     new Thread(task, "BAEPoint-" + Integer.toHexString(hashCode())).start();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void needsFillInterest()
/*     */     throws IOException
/*     */   {
/* 199 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 201 */       if (!isOpen()) {
/* 202 */         throw new ClosedChannelException();
/*     */       }
/* 204 */       ByteBuffer in = (ByteBuffer)this._inQ.peek();
/* 205 */       if ((BufferUtil.hasContent(in)) || (isEOF(in))) {
/* 206 */         execute(this._runFillable);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 199 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/* 207 */       if (lock != null) { $closeResource(localThrowable1, lock);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addInputEOF()
/*     */   {
/* 215 */     addInput((ByteBuffer)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInput(ByteBuffer in)
/*     */   {
/* 224 */     boolean fillable = false;
/* 225 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 227 */       if (isEOF((ByteBuffer)this._inQ.peek()))
/* 228 */         throw new RuntimeIOException(new EOFException());
/* 229 */       boolean was_empty = this._inQ.isEmpty();
/* 230 */       if (in == null)
/*     */       {
/* 232 */         this._inQ.add(EOF);
/* 233 */         fillable = true;
/*     */       }
/* 235 */       if (BufferUtil.hasContent(in))
/*     */       {
/* 237 */         this._inQ.add(in);
/* 238 */         fillable = was_empty;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 225 */       localThrowable1 = localThrowable;throw localThrowable;
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
/* 240 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 241 */     if (fillable) {
/* 242 */       this._runFillable.run();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addInputAndExecute(ByteBuffer in)
/*     */   {
/* 248 */     boolean fillable = false;
/* 249 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 251 */       if (isEOF((ByteBuffer)this._inQ.peek()))
/* 252 */         throw new RuntimeIOException(new EOFException());
/* 253 */       boolean was_empty = this._inQ.isEmpty();
/* 254 */       if (in == null)
/*     */       {
/* 256 */         this._inQ.add(EOF);
/* 257 */         fillable = true;
/*     */       }
/* 259 */       if (BufferUtil.hasContent(in))
/*     */       {
/* 261 */         this._inQ.add(in);
/* 262 */         fillable = was_empty;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 249 */       localThrowable1 = localThrowable;throw localThrowable;
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
/* 264 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 265 */     if (fillable) {
/* 266 */       execute(this._runFillable);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addInput(String s)
/*     */   {
/* 272 */     addInput(BufferUtil.toBuffer(s, StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */ 
/*     */   public void addInput(String s, Charset charset)
/*     */   {
/* 278 */     addInput(BufferUtil.toBuffer(s, charset));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer getOutput()
/*     */   {
/* 287 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 289 */       return this._out;
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 287 */       localThrowable1 = localThrowable2;throw localThrowable2;
/*     */     }
/*     */     finally {
/* 290 */       if (lock != null) { $closeResource(localThrowable1, lock);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getOutputString()
/*     */   {
/* 299 */     return getOutputString(StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOutputString(Charset charset)
/*     */   {
/* 309 */     return BufferUtil.toString(this._out, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer takeOutput()
/*     */   {
/* 320 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 322 */       ByteBuffer b = this._out;
/* 323 */       this._out = BufferUtil.allocate(b.capacity());
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 320 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */     }
/*     */     finally
/*     */     {
/* 324 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 325 */     ByteBuffer b; getWriteFlusher().completeWrite();
/* 326 */     return b;
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
/*     */   public ByteBuffer waitForOutput(long time, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 340 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 342 */       while ((BufferUtil.isEmpty(this._out)) && (!isOutputShutdown()))
/*     */       {
/* 344 */         if (!this._hasOutput.await(time, unit))
/* 345 */           return null;
/*     */       }
/* 347 */       ByteBuffer b = this._out;
/* 348 */       this._out = BufferUtil.allocate(b.capacity());
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 340 */       localThrowable1 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/* 349 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 350 */     ByteBuffer b; getWriteFlusher().completeWrite();
/* 351 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String takeOutputString()
/*     */   {
/* 360 */     return takeOutputString(StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String takeOutputString(Charset charset)
/*     */   {
/* 370 */     ByteBuffer buffer = takeOutput();
/* 371 */     return BufferUtil.toString(buffer, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOutput(ByteBuffer out)
/*     */   {
/* 380 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 382 */       this._out = out;
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 380 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */     }
/*     */     finally {
/* 383 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 384 */     getWriteFlusher().completeWrite();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMore()
/*     */   {
/* 393 */     return getOutput().position() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int fill(ByteBuffer buffer)
/*     */     throws IOException
/*     */   {
/* 403 */     int filled = 0;
/* 404 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try
/*     */     {
/*     */       for (;;) {
/* 408 */         if (!isOpen()) {
/* 409 */           throw new EofException("CLOSED");
/*     */         }
/* 411 */         if (isInputShutdown()) {
/* 412 */           return -1;
/*     */         }
/* 414 */         if (this._inQ.isEmpty()) {
/*     */           break;
/*     */         }
/* 417 */         ByteBuffer in = (ByteBuffer)this._inQ.peek();
/* 418 */         if (isEOF(in))
/*     */         {
/* 420 */           filled = -1;
/* 421 */           break;
/*     */         }
/*     */         
/* 424 */         if (BufferUtil.hasContent(in))
/*     */         {
/* 426 */           filled = BufferUtil.append(buffer, in);
/* 427 */           if (!BufferUtil.isEmpty(in)) break;
/* 428 */           this._inQ.poll(); break;
/*     */         }
/*     */         
/* 431 */         this._inQ.poll();
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 404 */       localThrowable1 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 433 */       if (lock != null) $closeResource(localThrowable1, lock);
/*     */     }
/* 435 */     if (filled > 0) {
/* 436 */       notIdle();
/* 437 */     } else if (filled < 0)
/* 438 */       shutdownInput();
/* 439 */     return filled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean flush(ByteBuffer... buffers)
/*     */     throws IOException
/*     */   {
/* 449 */     boolean flushed = true;
/* 450 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 452 */       if (!isOpen())
/* 453 */         throw new IOException("CLOSED");
/* 454 */       if (isOutputShutdown()) {
/* 455 */         throw new IOException("OSHUT");
/*     */       }
/* 457 */       boolean idle = true;
/*     */       
/* 459 */       for (ByteBuffer b : buffers)
/*     */       {
/* 461 */         if (BufferUtil.hasContent(b))
/*     */         {
/* 463 */           if ((this._growOutput) && (b.remaining() > BufferUtil.space(this._out)))
/*     */           {
/* 465 */             BufferUtil.compact(this._out);
/* 466 */             if (b.remaining() > BufferUtil.space(this._out))
/*     */             {
/* 468 */               ByteBuffer n = BufferUtil.allocate(this._out.capacity() + b.remaining() * 2);
/* 469 */               BufferUtil.append(n, this._out);
/* 470 */               this._out = n;
/*     */             }
/*     */           }
/*     */           
/* 474 */           if (BufferUtil.append(this._out, b) > 0) {
/* 475 */             idle = false;
/*     */           }
/* 477 */           if (BufferUtil.hasContent(b))
/*     */           {
/* 479 */             flushed = false;
/* 480 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 484 */       if (!idle)
/*     */       {
/* 486 */         notIdle();
/* 487 */         this._hasOutput.signalAll();
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 450 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 489 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 490 */     return flushed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 500 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 502 */       this._inQ.clear();
/* 503 */       this._hasOutput.signalAll();
/* 504 */       BufferUtil.clear(this._out);
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 500 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 505 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 506 */     super.reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getTransport()
/*     */   {
/* 516 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isGrowOutput()
/*     */   {
/* 525 */     return this._growOutput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGrowOutput(boolean growOutput)
/*     */   {
/* 534 */     this._growOutput = growOutput;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 544 */     Locker.Lock lock = this._locker.lock();Throwable localThrowable1 = null;
/*     */     try {
/* 546 */       int q = this._inQ.size();
/* 547 */       ByteBuffer b = (ByteBuffer)this._inQ.peek();
/* 548 */       o = BufferUtil.toDetailString(this._out);
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */       String o;
/* 544 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 549 */       if (lock != null) $closeResource(localThrowable1, lock); }
/* 550 */     String o; ByteBuffer b; int q; return String.format("%s[q=%d,q[0]=%s,o=%s]", new Object[] { super.toString(), Integer.valueOf(q), b, o });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isEOF(ByteBuffer buffer)
/*     */   {
/* 562 */     boolean is_EOF = buffer == EOF;
/* 563 */     return is_EOF;
/*     */   }
/*     */   
/*     */   protected void onIncompleteFlush() {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ByteArrayEndPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */