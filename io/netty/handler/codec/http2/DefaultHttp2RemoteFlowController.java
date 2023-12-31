/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2RemoteFlowController
/*     */   implements Http2RemoteFlowController
/*     */ {
/*  46 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2RemoteFlowController.class);
/*     */   private static final int MIN_WRITABLE_CHUNK = 32768;
/*     */   private final Http2Connection connection;
/*     */   private final Http2Connection.PropertyKey stateKey;
/*     */   private final StreamByteDistributor streamByteDistributor;
/*     */   private final FlowState connectionState;
/*  52 */   private int initialWindowSize = 65535;
/*     */   private WritabilityMonitor monitor;
/*     */   private ChannelHandlerContext ctx;
/*     */   
/*     */   public DefaultHttp2RemoteFlowController(Http2Connection connection) {
/*  57 */     this(connection, (Http2RemoteFlowController.Listener)null);
/*     */   }
/*     */   
/*     */   public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor)
/*     */   {
/*  62 */     this(connection, streamByteDistributor, null);
/*     */   }
/*     */   
/*     */   public DefaultHttp2RemoteFlowController(Http2Connection connection, Http2RemoteFlowController.Listener listener) {
/*  66 */     this(connection, new WeightedFairQueueByteDistributor(connection), listener);
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor, Http2RemoteFlowController.Listener listener)
/*     */   {
/*  72 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(connection, "connection"));
/*  73 */     this.streamByteDistributor = ((StreamByteDistributor)ObjectUtil.checkNotNull(streamByteDistributor, "streamWriteDistributor"));
/*     */     
/*     */ 
/*  76 */     this.stateKey = connection.newKey();
/*  77 */     this.connectionState = new FlowState(connection.connectionStream());
/*  78 */     connection.connectionStream().setProperty(this.stateKey, this.connectionState);
/*     */     
/*     */ 
/*  81 */     listener(listener);
/*  82 */     this.monitor.windowSize(this.connectionState, this.initialWindowSize);
/*     */     
/*     */ 
/*  85 */     connection.addListener(new Http2ConnectionAdapter()
/*     */     {
/*     */ 
/*     */       public void onStreamAdded(Http2Stream stream)
/*     */       {
/*  90 */         stream.setProperty(DefaultHttp2RemoteFlowController.this.stateKey, new DefaultHttp2RemoteFlowController.FlowState(DefaultHttp2RemoteFlowController.this, stream));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void onStreamActive(Http2Stream stream)
/*     */       {
/*  97 */         DefaultHttp2RemoteFlowController.this.monitor.windowSize(DefaultHttp2RemoteFlowController.this.state(stream), DefaultHttp2RemoteFlowController.this.initialWindowSize);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void onStreamClosed(Http2Stream stream)
/*     */       {
/* 104 */         DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
/*     */       }
/*     */       
/*     */       public void onStreamHalfClosed(Http2Stream stream)
/*     */       {
/* 109 */         if (Http2Stream.State.HALF_CLOSED_LOCAL == stream.state())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */           DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void channelHandlerContext(ChannelHandlerContext ctx)
/*     */     throws Http2Exception
/*     */   {
/* 134 */     this.ctx = ((ChannelHandlerContext)ObjectUtil.checkNotNull(ctx, "ctx"));
/*     */     
/*     */ 
/*     */ 
/* 138 */     channelWritabilityChanged();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     if (isChannelWritable()) {
/* 145 */       writePendingBytes();
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext channelHandlerContext()
/*     */   {
/* 151 */     return this.ctx;
/*     */   }
/*     */   
/*     */   public void initialWindowSize(int newWindowSize) throws Http2Exception
/*     */   {
/* 156 */     assert ((this.ctx == null) || (this.ctx.executor().inEventLoop()));
/* 157 */     this.monitor.initialWindowSize(newWindowSize);
/*     */   }
/*     */   
/*     */   public int initialWindowSize()
/*     */   {
/* 162 */     return this.initialWindowSize;
/*     */   }
/*     */   
/*     */   public int windowSize(Http2Stream stream)
/*     */   {
/* 167 */     return state(stream).windowSize();
/*     */   }
/*     */   
/*     */   public boolean isWritable(Http2Stream stream)
/*     */   {
/* 172 */     return this.monitor.isWritable(state(stream));
/*     */   }
/*     */   
/*     */   public void channelWritabilityChanged() throws Http2Exception
/*     */   {
/* 177 */     this.monitor.channelWritabilityChange();
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive)
/*     */   {
/* 183 */     assert ((weight >= 1) && (weight <= 256)) : "Invalid weight";
/* 184 */     assert (childStreamId != parentStreamId) : "A stream cannot depend on itself";
/* 185 */     assert ((childStreamId > 0) && (parentStreamId >= 0)) : "childStreamId must be > 0. parentStreamId must be >= 0.";
/*     */     
/* 187 */     this.streamByteDistributor.updateDependencyTree(childStreamId, parentStreamId, weight, exclusive);
/*     */   }
/*     */   
/*     */   private boolean isChannelWritable() {
/* 191 */     return (this.ctx != null) && (isChannelWritable0());
/*     */   }
/*     */   
/*     */   private boolean isChannelWritable0() {
/* 195 */     return this.ctx.channel().isWritable();
/*     */   }
/*     */   
/*     */   public void listener(Http2RemoteFlowController.Listener listener)
/*     */   {
/* 200 */     this.monitor = (listener == null ? new WritabilityMonitor(null) : new ListenerWritabilityMonitor(listener));
/*     */   }
/*     */   
/*     */   public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception
/*     */   {
/* 205 */     assert ((this.ctx == null) || (this.ctx.executor().inEventLoop()));
/* 206 */     this.monitor.incrementWindowSize(state(stream), delta);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addFlowControlled(Http2Stream stream, Http2RemoteFlowController.FlowControlled frame)
/*     */   {
/* 212 */     assert ((this.ctx == null) || (this.ctx.executor().inEventLoop()));
/* 213 */     ObjectUtil.checkNotNull(frame, "frame");
/*     */     try {
/* 215 */       this.monitor.enqueueFrame(state(stream), frame);
/*     */     } catch (Throwable t) {
/* 217 */       frame.error(this.ctx, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasFlowControlled(Http2Stream stream)
/*     */   {
/* 223 */     return state(stream).hasFrame();
/*     */   }
/*     */   
/*     */   private FlowState state(Http2Stream stream) {
/* 227 */     return (FlowState)stream.getProperty(this.stateKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int connectionWindowSize()
/*     */   {
/* 234 */     return this.connectionState.windowSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int minUsableChannelBytes()
/*     */   {
/* 244 */     return Math.max(this.ctx.channel().config().getWriteBufferLowWaterMark(), 32768);
/*     */   }
/*     */   
/*     */   private int maxUsableChannelBytes()
/*     */   {
/* 249 */     int channelWritableBytes = (int)Math.min(2147483647L, this.ctx.channel().bytesBeforeUnwritable());
/* 250 */     int usableBytes = channelWritableBytes > 0 ? Math.max(channelWritableBytes, minUsableChannelBytes()) : 0;
/*     */     
/*     */ 
/* 253 */     return Math.min(this.connectionState.windowSize(), usableBytes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writableBytes()
/*     */   {
/* 261 */     return Math.min(connectionWindowSize(), maxUsableChannelBytes());
/*     */   }
/*     */   
/*     */   public void writePendingBytes() throws Http2Exception
/*     */   {
/* 266 */     this.monitor.writePendingBytes();
/*     */   }
/*     */   
/*     */ 
/*     */   private final class FlowState
/*     */     implements StreamByteDistributor.StreamState
/*     */   {
/*     */     private final Http2Stream stream;
/*     */     
/*     */     private final Deque<Http2RemoteFlowController.FlowControlled> pendingWriteQueue;
/*     */     
/*     */     private int window;
/*     */     
/*     */     private long pendingBytes;
/*     */     
/*     */     private boolean markedWritable;
/*     */     
/*     */     private boolean writing;
/*     */     
/*     */     private boolean cancelled;
/*     */     
/*     */     FlowState(Http2Stream stream)
/*     */     {
/* 289 */       this.stream = stream;
/* 290 */       this.pendingWriteQueue = new ArrayDeque(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean isWritable()
/*     */     {
/* 298 */       return (windowSize() > pendingBytes()) && (!this.cancelled);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Http2Stream stream()
/*     */     {
/* 306 */       return this.stream;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     boolean markedWritability()
/*     */     {
/* 313 */       return this.markedWritable;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void markedWritability(boolean isWritable)
/*     */     {
/* 320 */       this.markedWritable = isWritable;
/*     */     }
/*     */     
/*     */     public int windowSize()
/*     */     {
/* 325 */       return this.window;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void windowSize(int initialWindowSize)
/*     */     {
/* 332 */       this.window = initialWindowSize;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     int writeAllocatedBytes(int allocated)
/*     */     {
/* 340 */       int initialAllocated = allocated;
/*     */       
/*     */ 
/* 343 */       Throwable cause = null;
/*     */       int writtenBytes;
/*     */       try {
/* 346 */         assert (!this.writing);
/* 347 */         this.writing = true;
/*     */         
/*     */ 
/* 350 */         boolean writeOccurred = false;
/* 351 */         Http2RemoteFlowController.FlowControlled frame; int maxBytes; while ((!this.cancelled) && ((frame = peek()) != null)) {
/* 352 */           maxBytes = Math.min(allocated, writableWindow());
/* 353 */           if ((maxBytes <= 0) && (frame.size() > 0)) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 358 */           writeOccurred = true;
/* 359 */           int initialFrameSize = frame.size();
/*     */           try {
/* 361 */             frame.write(DefaultHttp2RemoteFlowController.this.ctx, Math.max(0, maxBytes));
/* 362 */             if (frame.size() == 0)
/*     */             {
/*     */ 
/*     */ 
/* 366 */               this.pendingWriteQueue.remove();
/* 367 */               frame.writeComplete();
/*     */             }
/*     */           }
/*     */           finally {
/* 371 */             allocated -= initialFrameSize - frame.size();
/*     */           }
/*     */         }
/*     */         
/* 375 */         if (!writeOccurred) {
/*     */           int writtenBytes;
/* 377 */           return -1;
/*     */         }
/*     */       }
/*     */       catch (Throwable t) {
/*     */         int writtenBytes;
/* 382 */         this.cancelled = true;
/* 383 */         cause = t;
/*     */       } finally { int writtenBytes;
/* 385 */         this.writing = false;
/*     */         
/*     */ 
/* 388 */         writtenBytes = initialAllocated - allocated;
/*     */         
/* 390 */         decrementPendingBytes(writtenBytes, false);
/* 391 */         decrementFlowControlWindow(writtenBytes);
/*     */         
/*     */ 
/*     */ 
/* 395 */         if (this.cancelled) {
/* 396 */           cancel(Http2Error.INTERNAL_ERROR, cause);
/*     */         }
/*     */       }
/* 399 */       return writtenBytes;
/*     */     }
/*     */     
/*     */ 
/*     */     int incrementStreamWindow(int delta)
/*     */       throws Http2Exception
/*     */     {
/* 406 */       if ((delta > 0) && (Integer.MAX_VALUE - delta < this.window)) {
/* 407 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Window size overflow for stream: %d", new Object[] {
/* 408 */           Integer.valueOf(this.stream.id()) });
/*     */       }
/* 410 */       this.window += delta;
/*     */       
/* 412 */       DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
/* 413 */       return this.window;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private int writableWindow()
/*     */     {
/* 420 */       return Math.min(this.window, DefaultHttp2RemoteFlowController.this.connectionWindowSize());
/*     */     }
/*     */     
/*     */     public long pendingBytes()
/*     */     {
/* 425 */       return this.pendingBytes;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void enqueueFrame(Http2RemoteFlowController.FlowControlled frame)
/*     */     {
/* 432 */       Http2RemoteFlowController.FlowControlled last = (Http2RemoteFlowController.FlowControlled)this.pendingWriteQueue.peekLast();
/* 433 */       if (last == null) {
/* 434 */         enqueueFrameWithoutMerge(frame);
/* 435 */         return;
/*     */       }
/*     */       
/* 438 */       int lastSize = last.size();
/* 439 */       if (last.merge(DefaultHttp2RemoteFlowController.this.ctx, frame)) {
/* 440 */         incrementPendingBytes(last.size() - lastSize, true);
/* 441 */         return;
/*     */       }
/* 443 */       enqueueFrameWithoutMerge(frame);
/*     */     }
/*     */     
/*     */     private void enqueueFrameWithoutMerge(Http2RemoteFlowController.FlowControlled frame) {
/* 447 */       this.pendingWriteQueue.offer(frame);
/*     */       
/*     */ 
/* 450 */       incrementPendingBytes(frame.size(), true);
/*     */     }
/*     */     
/*     */     public boolean hasFrame()
/*     */     {
/* 455 */       return !this.pendingWriteQueue.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private Http2RemoteFlowController.FlowControlled peek()
/*     */     {
/* 462 */       return (Http2RemoteFlowController.FlowControlled)this.pendingWriteQueue.peek();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void cancel(Http2Error error, Throwable cause)
/*     */     {
/* 471 */       this.cancelled = true;
/*     */       
/* 473 */       if (this.writing) {
/* 474 */         return;
/*     */       }
/*     */       
/* 477 */       Http2RemoteFlowController.FlowControlled frame = (Http2RemoteFlowController.FlowControlled)this.pendingWriteQueue.poll();
/* 478 */       if (frame != null)
/*     */       {
/* 480 */         Http2Exception exception = Http2Exception.streamError(this.stream.id(), error, cause, "Stream closed before write could take place", new Object[0]);
/*     */         do
/*     */         {
/* 483 */           writeError(frame, exception);
/* 484 */           frame = (Http2RemoteFlowController.FlowControlled)this.pendingWriteQueue.poll();
/* 485 */         } while (frame != null);
/*     */       }
/*     */       
/* 488 */       DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
/*     */       
/* 490 */       DefaultHttp2RemoteFlowController.this.monitor.stateCancelled(this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void incrementPendingBytes(int numBytes, boolean updateStreamableBytes)
/*     */     {
/* 498 */       this.pendingBytes += numBytes;
/* 499 */       DefaultHttp2RemoteFlowController.this.monitor.incrementPendingBytes(numBytes);
/* 500 */       if (updateStreamableBytes) {
/* 501 */         DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void decrementPendingBytes(int bytes, boolean updateStreamableBytes)
/*     */     {
/* 509 */       incrementPendingBytes(-bytes, updateStreamableBytes);
/*     */     }
/*     */     
/*     */ 
/*     */     private void decrementFlowControlWindow(int bytes)
/*     */     {
/*     */       try
/*     */       {
/* 517 */         int negativeBytes = -bytes;
/* 518 */         DefaultHttp2RemoteFlowController.this.connectionState.incrementStreamWindow(negativeBytes);
/* 519 */         incrementStreamWindow(negativeBytes);
/*     */       }
/*     */       catch (Http2Exception e) {
/* 522 */         throw new IllegalStateException("Invalid window state when writing frame: " + e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void writeError(Http2RemoteFlowController.FlowControlled frame, Http2Exception cause)
/*     */     {
/* 531 */       assert (DefaultHttp2RemoteFlowController.this.ctx != null);
/* 532 */       decrementPendingBytes(frame.size(), true);
/* 533 */       frame.error(DefaultHttp2RemoteFlowController.this.ctx, cause);
/*     */     }
/*     */   }
/*     */   
/*     */   private class WritabilityMonitor implements StreamByteDistributor.Writer
/*     */   {
/*     */     private boolean inWritePendingBytes;
/*     */     private long totalPendingBytes;
/*     */     
/*     */     private WritabilityMonitor() {}
/*     */     
/*     */     public final void write(Http2Stream stream, int numBytes)
/*     */     {
/* 546 */       DefaultHttp2RemoteFlowController.this.state(stream).writeAllocatedBytes(numBytes);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void channelWritabilityChange()
/*     */       throws Http2Exception
/*     */     {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void stateCancelled(DefaultHttp2RemoteFlowController.FlowState state) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void windowSize(DefaultHttp2RemoteFlowController.FlowState state, int initialWindowSize)
/*     */     {
/* 567 */       state.windowSize(initialWindowSize);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void incrementWindowSize(DefaultHttp2RemoteFlowController.FlowState state, int delta)
/*     */       throws Http2Exception
/*     */     {
/* 577 */       state.incrementStreamWindow(delta);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void enqueueFrame(DefaultHttp2RemoteFlowController.FlowState state, Http2RemoteFlowController.FlowControlled frame)
/*     */       throws Http2Exception
/*     */     {
/* 587 */       state.enqueueFrame(frame);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final void incrementPendingBytes(int delta)
/*     */     {
/* 596 */       this.totalPendingBytes += delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final boolean isWritable(DefaultHttp2RemoteFlowController.FlowState state)
/*     */     {
/* 608 */       return (isWritableConnection()) && (state.isWritable());
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     final void writePendingBytes()
/*     */       throws Http2Exception
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 12	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:inWritePendingBytes	Z
/*     */       //   4: ifeq +4 -> 8
/*     */       //   7: return
/*     */       //   8: aload_0
/*     */       //   9: iconst_1
/*     */       //   10: putfield 12	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:inWritePendingBytes	Z
/*     */       //   13: aload_0
/*     */       //   14: getfield 2	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:this$0	Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;
/*     */       //   17: invokestatic 13	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController:access$900	(Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;)I
/*     */       //   20: istore_1
/*     */       //   21: aload_0
/*     */       //   22: getfield 2	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:this$0	Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;
/*     */       //   25: invokestatic 14	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController:access$600	(Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;)Lio/netty/handler/codec/http2/StreamByteDistributor;
/*     */       //   28: iload_1
/*     */       //   29: aload_0
/*     */       //   30: invokeinterface 15 3 0
/*     */       //   35: ifeq +28 -> 63
/*     */       //   38: aload_0
/*     */       //   39: getfield 2	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:this$0	Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;
/*     */       //   42: invokestatic 13	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController:access$900	(Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;)I
/*     */       //   45: dup
/*     */       //   46: istore_1
/*     */       //   47: ifle +16 -> 63
/*     */       //   50: aload_0
/*     */       //   51: getfield 2	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:this$0	Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;
/*     */       //   54: invokestatic 16	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController:access$1000	(Lio/netty/handler/codec/http2/DefaultHttp2RemoteFlowController;)Z
/*     */       //   57: ifne -36 -> 21
/*     */       //   60: goto +3 -> 63
/*     */       //   63: aload_0
/*     */       //   64: iconst_0
/*     */       //   65: putfield 12	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:inWritePendingBytes	Z
/*     */       //   68: goto +11 -> 79
/*     */       //   71: astore_2
/*     */       //   72: aload_0
/*     */       //   73: iconst_0
/*     */       //   74: putfield 12	io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor:inWritePendingBytes	Z
/*     */       //   77: aload_2
/*     */       //   78: athrow
/*     */       //   79: return
/*     */       // Line number table:
/*     */       //   Java source line #617	-> byte code offset #0
/*     */       //   Java source line #618	-> byte code offset #7
/*     */       //   Java source line #620	-> byte code offset #8
/*     */       //   Java source line #622	-> byte code offset #13
/*     */       //   Java source line #626	-> byte code offset #21
/*     */       //   Java source line #627	-> byte code offset #42
/*     */       //   Java source line #628	-> byte code offset #54
/*     */       //   Java source line #629	-> byte code offset #60
/*     */       //   Java source line #633	-> byte code offset #63
/*     */       //   Java source line #634	-> byte code offset #68
/*     */       //   Java source line #633	-> byte code offset #71
/*     */       //   Java source line #634	-> byte code offset #77
/*     */       //   Java source line #635	-> byte code offset #79
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	80	0	this	WritabilityMonitor
/*     */       //   20	27	1	bytesToWrite	int
/*     */       //   71	7	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   13	63	71	finally
/*     */     }
/*     */     
/*     */     void initialWindowSize(int newWindowSize)
/*     */       throws Http2Exception
/*     */     {
/* 638 */       if (newWindowSize < 0) {
/* 639 */         throw new IllegalArgumentException("Invalid initial window size: " + newWindowSize);
/*     */       }
/*     */       
/* 642 */       final int delta = newWindowSize - DefaultHttp2RemoteFlowController.this.initialWindowSize;
/* 643 */       DefaultHttp2RemoteFlowController.this.initialWindowSize = newWindowSize;
/* 644 */       DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(new Http2StreamVisitor()
/*     */       {
/*     */         public boolean visit(Http2Stream stream) throws Http2Exception {
/* 647 */           DefaultHttp2RemoteFlowController.this.state(stream).incrementStreamWindow(delta);
/* 648 */           return true;
/*     */         }
/*     */       });
/*     */       
/* 652 */       if ((delta > 0) && (DefaultHttp2RemoteFlowController.this.isChannelWritable()))
/*     */       {
/* 654 */         writePendingBytes();
/*     */       }
/*     */     }
/*     */     
/*     */     final boolean isWritableConnection() {
/* 659 */       return (DefaultHttp2RemoteFlowController.this.connectionState.windowSize() - this.totalPendingBytes > 0L) && (DefaultHttp2RemoteFlowController.this.isChannelWritable());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final class ListenerWritabilityMonitor
/*     */     extends DefaultHttp2RemoteFlowController.WritabilityMonitor
/*     */     implements Http2StreamVisitor
/*     */   {
/*     */     private final Http2RemoteFlowController.Listener listener;
/*     */     
/*     */ 
/*     */     ListenerWritabilityMonitor(Http2RemoteFlowController.Listener listener)
/*     */     {
/* 674 */       super(null);
/* 675 */       this.listener = listener;
/*     */     }
/*     */     
/*     */     public boolean visit(Http2Stream stream) throws Http2Exception
/*     */     {
/* 680 */       DefaultHttp2RemoteFlowController.FlowState state = DefaultHttp2RemoteFlowController.this.state(stream);
/* 681 */       if (isWritable(state) != state.markedWritability()) {
/* 682 */         notifyWritabilityChanged(state);
/*     */       }
/* 684 */       return true;
/*     */     }
/*     */     
/*     */     void windowSize(DefaultHttp2RemoteFlowController.FlowState state, int initialWindowSize)
/*     */     {
/* 689 */       super.windowSize(state, initialWindowSize);
/*     */       try {
/* 691 */         checkStateWritability(state);
/*     */       } catch (Http2Exception e) {
/* 693 */         throw new RuntimeException("Caught unexpected exception from window", e);
/*     */       }
/*     */     }
/*     */     
/*     */     void incrementWindowSize(DefaultHttp2RemoteFlowController.FlowState state, int delta) throws Http2Exception
/*     */     {
/* 699 */       super.incrementWindowSize(state, delta);
/* 700 */       checkStateWritability(state);
/*     */     }
/*     */     
/*     */     void initialWindowSize(int newWindowSize) throws Http2Exception
/*     */     {
/* 705 */       super.initialWindowSize(newWindowSize);
/* 706 */       if (isWritableConnection())
/*     */       {
/*     */ 
/* 709 */         checkAllWritabilityChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     void enqueueFrame(DefaultHttp2RemoteFlowController.FlowState state, Http2RemoteFlowController.FlowControlled frame) throws Http2Exception
/*     */     {
/* 715 */       super.enqueueFrame(state, frame);
/* 716 */       checkConnectionThenStreamWritabilityChanged(state);
/*     */     }
/*     */     
/*     */     void stateCancelled(DefaultHttp2RemoteFlowController.FlowState state)
/*     */     {
/*     */       try {
/* 722 */         checkConnectionThenStreamWritabilityChanged(state);
/*     */       } catch (Http2Exception e) {
/* 724 */         throw new RuntimeException("Caught unexpected exception from checkAllWritabilityChanged", e);
/*     */       }
/*     */     }
/*     */     
/*     */     void channelWritabilityChange() throws Http2Exception
/*     */     {
/* 730 */       if (DefaultHttp2RemoteFlowController.this.connectionState.markedWritability() != DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
/* 731 */         checkAllWritabilityChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     private void checkStateWritability(DefaultHttp2RemoteFlowController.FlowState state) throws Http2Exception {
/* 736 */       if (isWritable(state) != state.markedWritability()) {
/* 737 */         if (state == DefaultHttp2RemoteFlowController.this.connectionState) {
/* 738 */           checkAllWritabilityChanged();
/*     */         } else {
/* 740 */           notifyWritabilityChanged(state);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifyWritabilityChanged(DefaultHttp2RemoteFlowController.FlowState state) {
/* 746 */       state.markedWritability(!state.markedWritability());
/*     */       try {
/* 748 */         this.listener.writabilityChanged(DefaultHttp2RemoteFlowController.FlowState.access$1300(state));
/*     */       } catch (Throwable cause) {
/* 750 */         DefaultHttp2RemoteFlowController.logger.error("Caught Throwable from listener.writabilityChanged", cause);
/*     */       }
/*     */     }
/*     */     
/*     */     private void checkConnectionThenStreamWritabilityChanged(DefaultHttp2RemoteFlowController.FlowState state) throws Http2Exception
/*     */     {
/* 756 */       if (isWritableConnection() != DefaultHttp2RemoteFlowController.this.connectionState.markedWritability()) {
/* 757 */         checkAllWritabilityChanged();
/* 758 */       } else if (isWritable(state) != state.markedWritability()) {
/* 759 */         notifyWritabilityChanged(state);
/*     */       }
/*     */     }
/*     */     
/*     */     private void checkAllWritabilityChanged() throws Http2Exception
/*     */     {
/* 765 */       DefaultHttp2RemoteFlowController.this.connectionState.markedWritability(isWritableConnection());
/* 766 */       DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\DefaultHttp2RemoteFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */