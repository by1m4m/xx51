/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.RemoteEndpoint;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
/*     */ import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.DataFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PingFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PongFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.TextFrame;
/*     */ import org.eclipse.jetty.websocket.common.io.FrameFlusher;
/*     */ import org.eclipse.jetty.websocket.common.io.FutureWriteCallback;
/*     */ import org.eclipse.jetty.websocket.common.io.IOState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebSocketRemoteEndpoint
/*     */   implements RemoteEndpoint
/*     */ {
/*     */   private static enum MsgType
/*     */   {
/*  52 */     BLOCKING, 
/*  53 */     ASYNC, 
/*  54 */     STREAMING, 
/*  55 */     PARTIAL_TEXT, 
/*  56 */     PARTIAL_BINARY;
/*     */     
/*     */     private MsgType() {} }
/*  59 */   private static final WriteCallback NOOP_CALLBACK = new WriteCallback()
/*     */   {
/*     */     public void writeSuccess() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void writeFailed(Throwable x) {}
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  72 */   private static final Logger LOG = Log.getLogger(WebSocketRemoteEndpoint.class);
/*     */   
/*     */   private static final int ASYNC_MASK = 65535;
/*     */   
/*     */   private static final int BLOCK_MASK = 65536;
/*     */   private static final int STREAM_MASK = 131072;
/*     */   private static final int PARTIAL_TEXT_MASK = 262144;
/*     */   private static final int PARTIAL_BINARY_MASK = 524288;
/*     */   private final LogicalConnection connection;
/*     */   private final OutgoingFrames outgoing;
/*  82 */   private final AtomicInteger msgState = new AtomicInteger();
/*  83 */   private final BlockingWriteCallback blocker = new BlockingWriteCallback();
/*     */   private volatile BatchMode batchMode;
/*     */   
/*     */   public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing)
/*     */   {
/*  88 */     this(connection, outgoing, BatchMode.AUTO);
/*     */   }
/*     */   
/*     */   public WebSocketRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoing, BatchMode batchMode)
/*     */   {
/*  93 */     if (connection == null)
/*     */     {
/*  95 */       throw new IllegalArgumentException("LogicalConnection cannot be null");
/*     */     }
/*  97 */     this.connection = connection;
/*  98 */     this.outgoing = outgoing;
/*  99 */     this.batchMode = batchMode;
/*     */   }
/*     */   
/*     */   private void blockingWrite(WebSocketFrame frame) throws IOException
/*     */   {
/* 104 */     BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker();Throwable localThrowable1 = null;
/*     */     try {
/* 106 */       uncheckedSendFrame(frame, b);
/* 107 */       b.block();
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 104 */       localThrowable1 = localThrowable;throw localThrowable;
/*     */     }
/*     */     finally
/*     */     {
/* 108 */       if (b != null) { $closeResource(localThrowable1, b);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean lockMsg(MsgType type)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 122 */       int state = this.msgState.get();
/*     */       
/* 124 */       switch (type)
/*     */       {
/*     */       case BLOCKING: 
/* 127 */         if ((state & 0xC0000) != 0)
/* 128 */           throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
/* 129 */         if ((state & 0x10000) != 0)
/* 130 */           throw new IllegalStateException(String.format("Blocking message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
/* 131 */         if (this.msgState.compareAndSet(state, state | 0x10000)) {
/* 132 */           return state == 0;
/*     */         }
/*     */         break;
/*     */       case ASYNC: 
/* 136 */         if ((state & 0xC0000) != 0)
/* 137 */           throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
/* 138 */         if ((state & 0xFFFF) == 65535)
/* 139 */           throw new IllegalStateException(String.format("Too many async sends: %x", new Object[] { Integer.valueOf(state) }));
/* 140 */         if (this.msgState.compareAndSet(state, state + 1)) {
/* 141 */           return state == 0;
/*     */         }
/*     */         break;
/*     */       case STREAMING: 
/* 145 */         if ((state & 0xC0000) != 0)
/* 146 */           throw new IllegalStateException(String.format("Partial message pending %x for %s", new Object[] { Integer.valueOf(state), type }));
/* 147 */         if ((state & 0x20000) != 0)
/* 148 */           throw new IllegalStateException(String.format("Already streaming %x for %s", new Object[] { Integer.valueOf(state), type }));
/* 149 */         if (this.msgState.compareAndSet(state, state | 0x20000)) {
/* 150 */           return state == 0;
/*     */         }
/*     */         break;
/*     */       case PARTIAL_BINARY: 
/* 154 */         if (state == 524288)
/* 155 */           return false;
/* 156 */         if (state == 0)
/*     */         {
/* 158 */           if (this.msgState.compareAndSet(0, state | 0x80000))
/* 159 */             return true;
/*     */         }
/* 161 */         throw new IllegalStateException(String.format("Cannot send %s in state %x", new Object[] { type, Integer.valueOf(state) }));
/*     */       
/*     */       case PARTIAL_TEXT: 
/* 164 */         if (state == 262144)
/* 165 */           return false;
/* 166 */         if (state == 0)
/*     */         {
/* 168 */           if (this.msgState.compareAndSet(0, state | 0x40000))
/* 169 */             return true;
/*     */         }
/* 171 */         throw new IllegalStateException(String.format("Cannot send %s in state %x", new Object[] { type, Integer.valueOf(state) }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void unlockMsg(MsgType type)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 180 */       int state = this.msgState.get();
/*     */       
/* 182 */       switch (type)
/*     */       {
/*     */       case BLOCKING: 
/* 185 */         if ((state & 0x10000) == 0)
/* 186 */           throw new IllegalStateException(String.format("Not Blocking in state %x", new Object[] { Integer.valueOf(state) }));
/* 187 */         if (this.msgState.compareAndSet(state, state & 0xFFFEFFFF)) {
/* 188 */           return;
/*     */         }
/*     */         break;
/*     */       case ASYNC: 
/* 192 */         if ((state & 0xFFFF) == 0)
/* 193 */           throw new IllegalStateException(String.format("Not Async in %x", new Object[] { Integer.valueOf(state) }));
/* 194 */         if (this.msgState.compareAndSet(state, state - 1)) {
/* 195 */           return;
/*     */         }
/*     */         break;
/*     */       case STREAMING: 
/* 199 */         if ((state & 0x20000) == 0)
/* 200 */           throw new IllegalStateException(String.format("Not Streaming in state %x", new Object[] { Integer.valueOf(state) }));
/* 201 */         if (this.msgState.compareAndSet(state, state & 0xFFFDFFFF)) {
/* 202 */           return;
/*     */         }
/*     */         break;
/*     */       case PARTIAL_BINARY: 
/* 206 */         if (this.msgState.compareAndSet(524288, 0))
/* 207 */           return;
/* 208 */         throw new IllegalStateException(String.format("Not Partial Binary in state %x", new Object[] { Integer.valueOf(state) }));
/*     */       
/*     */       case PARTIAL_TEXT: 
/* 211 */         if (this.msgState.compareAndSet(262144, 0))
/* 212 */           return;
/* 213 */         throw new IllegalStateException(String.format("Not Partial Text in state %x", new Object[] { Integer.valueOf(state) }));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InetSocketAddress getInetSocketAddress()
/*     */   {
/* 227 */     if (this.connection == null)
/* 228 */       return null;
/* 229 */     return this.connection.getRemoteAddress();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Future<Void> sendAsyncFrame(WebSocketFrame frame)
/*     */   {
/* 240 */     FutureWriteCallback future = new FutureWriteCallback();
/* 241 */     uncheckedSendFrame(frame, future);
/* 242 */     return future;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void sendBytes(ByteBuffer data)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   4: invokespecial 55	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:lockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)Z
/*     */     //   7: pop
/*     */     //   8: aload_0
/*     */     //   9: getfield 16	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */     //   12: invokeinterface 56 1 0
/*     */     //   17: invokevirtual 57	org/eclipse/jetty/websocket/common/io/IOState:assertOutputOpen	()V
/*     */     //   20: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   23: invokeinterface 59 1 0
/*     */     //   28: ifeq +24 -> 52
/*     */     //   31: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   34: ldc 60
/*     */     //   36: iconst_1
/*     */     //   37: anewarray 30	java/lang/Object
/*     */     //   40: dup
/*     */     //   41: iconst_0
/*     */     //   42: aload_1
/*     */     //   43: invokestatic 61	org/eclipse/jetty/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
/*     */     //   46: aastore
/*     */     //   47: invokeinterface 62 3 0
/*     */     //   52: aload_0
/*     */     //   53: new 63	org/eclipse/jetty/websocket/common/frames/BinaryFrame
/*     */     //   56: dup
/*     */     //   57: invokespecial 64	org/eclipse/jetty/websocket/common/frames/BinaryFrame:<init>	()V
/*     */     //   60: aload_1
/*     */     //   61: invokevirtual 65	org/eclipse/jetty/websocket/common/frames/BinaryFrame:setPayload	(Ljava/nio/ByteBuffer;)Lorg/eclipse/jetty/websocket/common/frames/BinaryFrame;
/*     */     //   64: invokespecial 66	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:blockingWrite	(Lorg/eclipse/jetty/websocket/common/WebSocketFrame;)V
/*     */     //   67: aload_0
/*     */     //   68: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   71: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   74: goto +13 -> 87
/*     */     //   77: astore_2
/*     */     //   78: aload_0
/*     */     //   79: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   82: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   85: aload_2
/*     */     //   86: athrow
/*     */     //   87: return
/*     */     // Line number table:
/*     */     //   Java source line #251	-> byte code offset #0
/*     */     //   Java source line #254	-> byte code offset #8
/*     */     //   Java source line #255	-> byte code offset #20
/*     */     //   Java source line #257	-> byte code offset #31
/*     */     //   Java source line #259	-> byte code offset #52
/*     */     //   Java source line #263	-> byte code offset #67
/*     */     //   Java source line #264	-> byte code offset #74
/*     */     //   Java source line #263	-> byte code offset #77
/*     */     //   Java source line #265	-> byte code offset #87
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	88	0	this	WebSocketRemoteEndpoint
/*     */     //   0	88	1	data	ByteBuffer
/*     */     //   77	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	67	77	finally
/*     */   }
/*     */   
/*     */   public Future<Void> sendBytesByFuture(ByteBuffer data)
/*     */   {
/* 270 */     lockMsg(MsgType.ASYNC);
/*     */     try
/*     */     {
/* 273 */       if (LOG.isDebugEnabled())
/*     */       {
/* 275 */         LOG.debug("sendBytesByFuture with {}", new Object[] { BufferUtil.toDetailString(data) });
/*     */       }
/* 277 */       return sendAsyncFrame(new BinaryFrame().setPayload(data));
/*     */     }
/*     */     finally
/*     */     {
/* 281 */       unlockMsg(MsgType.ASYNC);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void sendBytes(ByteBuffer data, WriteCallback callback)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getstatic 68	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:ASYNC	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   4: invokespecial 55	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:lockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)Z
/*     */     //   7: pop
/*     */     //   8: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   11: invokeinterface 59 1 0
/*     */     //   16: ifeq +28 -> 44
/*     */     //   19: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   22: ldc 71
/*     */     //   24: iconst_2
/*     */     //   25: anewarray 30	java/lang/Object
/*     */     //   28: dup
/*     */     //   29: iconst_0
/*     */     //   30: aload_1
/*     */     //   31: invokestatic 61	org/eclipse/jetty/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
/*     */     //   34: aastore
/*     */     //   35: dup
/*     */     //   36: iconst_1
/*     */     //   37: aload_2
/*     */     //   38: aastore
/*     */     //   39: invokeinterface 62 3 0
/*     */     //   44: aload_0
/*     */     //   45: new 63	org/eclipse/jetty/websocket/common/frames/BinaryFrame
/*     */     //   48: dup
/*     */     //   49: invokespecial 64	org/eclipse/jetty/websocket/common/frames/BinaryFrame:<init>	()V
/*     */     //   52: aload_1
/*     */     //   53: invokevirtual 65	org/eclipse/jetty/websocket/common/frames/BinaryFrame:setPayload	(Ljava/nio/ByteBuffer;)Lorg/eclipse/jetty/websocket/common/frames/BinaryFrame;
/*     */     //   56: aload_2
/*     */     //   57: ifnonnull +9 -> 66
/*     */     //   60: getstatic 72	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:NOOP_CALLBACK	Lorg/eclipse/jetty/websocket/api/WriteCallback;
/*     */     //   63: goto +4 -> 67
/*     */     //   66: aload_2
/*     */     //   67: invokevirtual 20	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:uncheckedSendFrame	(Lorg/eclipse/jetty/websocket/common/WebSocketFrame;Lorg/eclipse/jetty/websocket/api/WriteCallback;)V
/*     */     //   70: aload_0
/*     */     //   71: getstatic 68	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:ASYNC	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   74: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   77: goto +13 -> 90
/*     */     //   80: astore_3
/*     */     //   81: aload_0
/*     */     //   82: getstatic 68	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:ASYNC	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   85: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   88: aload_3
/*     */     //   89: athrow
/*     */     //   90: return
/*     */     // Line number table:
/*     */     //   Java source line #288	-> byte code offset #0
/*     */     //   Java source line #291	-> byte code offset #8
/*     */     //   Java source line #293	-> byte code offset #19
/*     */     //   Java source line #295	-> byte code offset #44
/*     */     //   Java source line #299	-> byte code offset #70
/*     */     //   Java source line #300	-> byte code offset #77
/*     */     //   Java source line #299	-> byte code offset #80
/*     */     //   Java source line #301	-> byte code offset #90
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	91	0	this	WebSocketRemoteEndpoint
/*     */     //   0	91	1	data	ByteBuffer
/*     */     //   0	91	2	callback	WriteCallback
/*     */     //   80	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	70	80	finally
/*     */   }
/*     */   
/*     */   public void uncheckedSendFrame(WebSocketFrame frame, WriteCallback callback)
/*     */   {
/*     */     try
/*     */     {
/* 307 */       BatchMode batchMode = BatchMode.OFF;
/* 308 */       if (frame.isDataFrame())
/* 309 */         batchMode = getBatchMode();
/* 310 */       this.connection.getIOState().assertOutputOpen();
/* 311 */       this.outgoing.outgoingFrame(frame, callback, batchMode);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 315 */       callback.writeFailed(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendPartialBytes(ByteBuffer fragment, boolean isLast)
/*     */     throws IOException
/*     */   {
/* 322 */     boolean first = lockMsg(MsgType.PARTIAL_BINARY);
/*     */     try
/*     */     {
/* 325 */       if (LOG.isDebugEnabled())
/*     */       {
/* 327 */         LOG.debug("sendPartialBytes({}, {})", new Object[] { BufferUtil.toDetailString(fragment), Boolean.valueOf(isLast) });
/*     */       }
/* 329 */       DataFrame frame = first ? new BinaryFrame() : new ContinuationFrame();
/* 330 */       frame.setPayload(fragment);
/* 331 */       frame.setFin(isLast);
/* 332 */       blockingWrite(frame);
/*     */     }
/*     */     finally
/*     */     {
/* 336 */       if (isLast) {
/* 337 */         unlockMsg(MsgType.PARTIAL_BINARY);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendPartialString(String fragment, boolean isLast) throws IOException
/*     */   {
/* 344 */     boolean first = lockMsg(MsgType.PARTIAL_TEXT);
/*     */     try
/*     */     {
/* 347 */       if (LOG.isDebugEnabled())
/*     */       {
/* 349 */         LOG.debug("sendPartialString({}, {})", new Object[] { fragment, Boolean.valueOf(isLast) });
/*     */       }
/* 351 */       DataFrame frame = first ? new TextFrame() : new ContinuationFrame();
/* 352 */       frame.setPayload(BufferUtil.toBuffer(fragment, StandardCharsets.UTF_8));
/* 353 */       frame.setFin(isLast);
/* 354 */       blockingWrite(frame);
/*     */     }
/*     */     finally
/*     */     {
/* 358 */       if (isLast) {
/* 359 */         unlockMsg(MsgType.PARTIAL_TEXT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendPing(ByteBuffer applicationData) throws IOException
/*     */   {
/* 366 */     if (LOG.isDebugEnabled())
/*     */     {
/* 368 */       LOG.debug("sendPing with {}", new Object[] { BufferUtil.toDetailString(applicationData) });
/*     */     }
/* 370 */     sendAsyncFrame(new PingFrame().setPayload(applicationData));
/*     */   }
/*     */   
/*     */   public void sendPong(ByteBuffer applicationData)
/*     */     throws IOException
/*     */   {
/* 376 */     if (LOG.isDebugEnabled())
/*     */     {
/* 378 */       LOG.debug("sendPong with {}", new Object[] { BufferUtil.toDetailString(applicationData) });
/*     */     }
/* 380 */     sendAsyncFrame(new PongFrame().setPayload(applicationData));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void sendString(String text)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   4: invokespecial 55	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:lockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)Z
/*     */     //   7: pop
/*     */     //   8: new 88	org/eclipse/jetty/websocket/common/frames/TextFrame
/*     */     //   11: dup
/*     */     //   12: invokespecial 89	org/eclipse/jetty/websocket/common/frames/TextFrame:<init>	()V
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual 100	org/eclipse/jetty/websocket/common/frames/TextFrame:setPayload	(Ljava/lang/String;)Lorg/eclipse/jetty/websocket/common/frames/TextFrame;
/*     */     //   19: astore_2
/*     */     //   20: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   23: invokeinterface 59 1 0
/*     */     //   28: ifeq +27 -> 55
/*     */     //   31: getstatic 58	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   34: ldc 101
/*     */     //   36: iconst_1
/*     */     //   37: anewarray 30	java/lang/Object
/*     */     //   40: dup
/*     */     //   41: iconst_0
/*     */     //   42: aload_2
/*     */     //   43: invokevirtual 102	org/eclipse/jetty/websocket/common/WebSocketFrame:getPayload	()Ljava/nio/ByteBuffer;
/*     */     //   46: invokestatic 61	org/eclipse/jetty/util/BufferUtil:toDetailString	(Ljava/nio/ByteBuffer;)Ljava/lang/String;
/*     */     //   49: aastore
/*     */     //   50: invokeinterface 62 3 0
/*     */     //   55: aload_0
/*     */     //   56: aload_2
/*     */     //   57: invokespecial 66	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:blockingWrite	(Lorg/eclipse/jetty/websocket/common/WebSocketFrame;)V
/*     */     //   60: aload_0
/*     */     //   61: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   64: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   67: goto +13 -> 80
/*     */     //   70: astore_3
/*     */     //   71: aload_0
/*     */     //   72: getstatic 54	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType:BLOCKING	Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;
/*     */     //   75: invokespecial 67	org/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint:unlockMsg	(Lorg/eclipse/jetty/websocket/common/WebSocketRemoteEndpoint$MsgType;)V
/*     */     //   78: aload_3
/*     */     //   79: athrow
/*     */     //   80: return
/*     */     // Line number table:
/*     */     //   Java source line #386	-> byte code offset #0
/*     */     //   Java source line #389	-> byte code offset #8
/*     */     //   Java source line #390	-> byte code offset #20
/*     */     //   Java source line #392	-> byte code offset #31
/*     */     //   Java source line #394	-> byte code offset #55
/*     */     //   Java source line #398	-> byte code offset #60
/*     */     //   Java source line #399	-> byte code offset #67
/*     */     //   Java source line #398	-> byte code offset #70
/*     */     //   Java source line #400	-> byte code offset #80
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	81	0	this	WebSocketRemoteEndpoint
/*     */     //   0	81	1	text	String
/*     */     //   19	38	2	frame	WebSocketFrame
/*     */     //   70	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	60	70	finally
/*     */   }
/*     */   
/*     */   public Future<Void> sendStringByFuture(String text)
/*     */   {
/* 405 */     lockMsg(MsgType.ASYNC);
/*     */     try
/*     */     {
/* 408 */       TextFrame frame = new TextFrame().setPayload(text);
/* 409 */       if (LOG.isDebugEnabled())
/*     */       {
/* 411 */         LOG.debug("sendStringByFuture with {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
/*     */       }
/* 413 */       return sendAsyncFrame(frame);
/*     */     }
/*     */     finally
/*     */     {
/* 417 */       unlockMsg(MsgType.ASYNC);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void sendString(String text, WriteCallback callback)
/*     */   {
/* 424 */     lockMsg(MsgType.ASYNC);
/*     */     try
/*     */     {
/* 427 */       TextFrame frame = new TextFrame().setPayload(text);
/* 428 */       if (LOG.isDebugEnabled())
/*     */       {
/* 430 */         LOG.debug("sendString({},{})", new Object[] { BufferUtil.toDetailString(frame.getPayload()), callback });
/*     */       }
/* 432 */       uncheckedSendFrame(frame, callback == null ? NOOP_CALLBACK : callback);
/*     */     }
/*     */     finally
/*     */     {
/* 436 */       unlockMsg(MsgType.ASYNC);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public BatchMode getBatchMode()
/*     */   {
/* 443 */     return this.batchMode;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBatchMode(BatchMode batchMode)
/*     */   {
/* 449 */     this.batchMode = batchMode;
/*     */   }
/*     */   
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 455 */     lockMsg(MsgType.ASYNC);
/* 456 */     try { BlockingWriteCallback.WriteBlocker b = this.blocker.acquireWriteBlocker();Throwable localThrowable1 = null;
/*     */       try {
/* 458 */         uncheckedSendFrame(FrameFlusher.FLUSH_FRAME, b);
/* 459 */         b.block();
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 456 */         localThrowable1 = localThrowable;throw localThrowable;
/*     */       }
/*     */       finally
/*     */       {
/* 460 */         if (b != null) $closeResource(localThrowable1, b);
/*     */       }
/*     */     } finally {
/* 463 */       unlockMsg(MsgType.ASYNC);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 470 */     return String.format("%s@%x[batching=%b]", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), getBatchMode() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\WebSocketRemoteEndpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */