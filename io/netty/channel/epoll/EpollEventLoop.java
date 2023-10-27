/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class EpollEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
/*     */   
/*     */   static
/*     */   {
/*  48 */     logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
/*     */     
/*  50 */     WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  55 */     Epoll.ensureAvailability();
/*     */   }
/*     */   
/*     */ 
/*  59 */   private long prevDeadlineNanos = nanoTime() - 1L;
/*     */   private final FileDescriptor epollFd;
/*     */   private final FileDescriptor eventFd;
/*     */   private final FileDescriptor timerFd;
/*  63 */   private final IntObjectMap<AbstractEpollChannel> channels = new IntObjectHashMap(4096);
/*     */   
/*     */   private final boolean allowGrowing;
/*     */   
/*     */   private final EpollEventArray events;
/*     */   
/*     */   private IovArray iovArray;
/*     */   private NativeDatagramPacketArray datagramPacketArray;
/*     */   private final SelectStrategy selectStrategy;
/*  72 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */   {
/*     */     public int get() throws Exception {
/*  75 */       return EpollEventLoop.this.epollWaitNow();
/*     */     }
/*     */   };
/*     */   private volatile int wakenUp;
/*  79 */   private volatile int ioRatio = 50;
/*     */   
/*     */   private static final long MAX_SCHEDULED_TIMERFD_NS = 999999999L;
/*     */   
/*     */ 
/*     */   EpollEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/*  86 */     super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/*  87 */     this.selectStrategy = ((SelectStrategy)ObjectUtil.checkNotNull(strategy, "strategy"));
/*  88 */     if (maxEvents == 0) {
/*  89 */       this.allowGrowing = true;
/*  90 */       this.events = new EpollEventArray(4096);
/*     */     } else {
/*  92 */       this.allowGrowing = false;
/*  93 */       this.events = new EpollEventArray(maxEvents);
/*     */     }
/*  95 */     boolean success = false;
/*  96 */     FileDescriptor epollFd = null;
/*  97 */     FileDescriptor eventFd = null;
/*  98 */     FileDescriptor timerFd = null;
/*     */     try {
/* 100 */       this.epollFd = (epollFd = Native.newEpollCreate());
/* 101 */       this.eventFd = (eventFd = Native.newEventFd());
/*     */       try {
/* 103 */         Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN);
/*     */       } catch (IOException e) {
/* 105 */         throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", e);
/*     */       }
/* 107 */       this.timerFd = (timerFd = Native.newTimerFd());
/*     */       try {
/* 109 */         Native.epollCtlAdd(epollFd.intValue(), timerFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
/*     */       } catch (IOException e) {
/* 111 */         throw new IllegalStateException("Unable to add timerFd filedescriptor to epoll", e);
/*     */       }
/* 113 */       success = true; return;
/*     */     } finally {
/* 115 */       if (!success) {
/* 116 */         if (epollFd != null) {
/*     */           try {
/* 118 */             epollFd.close();
/*     */           }
/*     */           catch (Exception localException3) {}
/*     */         }
/*     */         
/* 123 */         if (eventFd != null) {
/*     */           try {
/* 125 */             eventFd.close();
/*     */           }
/*     */           catch (Exception localException4) {}
/*     */         }
/*     */         
/* 130 */         if (timerFd != null) {
/*     */           try {
/* 132 */             timerFd.close();
/*     */           }
/*     */           catch (Exception localException5) {}
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   IovArray cleanIovArray()
/*     */   {
/* 145 */     if (this.iovArray == null) {
/* 146 */       this.iovArray = new IovArray();
/*     */     } else {
/* 148 */       this.iovArray.clear();
/*     */     }
/* 150 */     return this.iovArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   NativeDatagramPacketArray cleanDatagramPacketArray()
/*     */   {
/* 157 */     if (this.datagramPacketArray == null) {
/* 158 */       this.datagramPacketArray = new NativeDatagramPacketArray();
/*     */     } else {
/* 160 */       this.datagramPacketArray.clear();
/*     */     }
/* 162 */     return this.datagramPacketArray;
/*     */   }
/*     */   
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 167 */     if ((!inEventLoop) && (WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)))
/*     */     {
/* 169 */       Native.eventFdWrite(this.eventFd.intValue(), 1L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void add(AbstractEpollChannel ch)
/*     */     throws IOException
/*     */   {
/* 177 */     assert (inEventLoop());
/* 178 */     int fd = ch.socket.intValue();
/* 179 */     Native.epollCtlAdd(this.epollFd.intValue(), fd, ch.flags);
/* 180 */     this.channels.put(fd, ch);
/*     */   }
/*     */   
/*     */ 
/*     */   void modify(AbstractEpollChannel ch)
/*     */     throws IOException
/*     */   {
/* 187 */     assert (inEventLoop());
/* 188 */     Native.epollCtlMod(this.epollFd.intValue(), ch.socket.intValue(), ch.flags);
/*     */   }
/*     */   
/*     */ 
/*     */   void remove(AbstractEpollChannel ch)
/*     */     throws IOException
/*     */   {
/* 195 */     assert (inEventLoop());
/*     */     
/* 197 */     if (ch.isOpen()) {
/* 198 */       int fd = ch.socket.intValue();
/* 199 */       if (this.channels.remove(fd) != null)
/*     */       {
/*     */ 
/* 202 */         Native.epollCtlDel(this.epollFd.intValue(), ch.fd().intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks)
/*     */   {
/* 210 */     return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : 
/* 211 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIoRatio()
/*     */   {
/* 218 */     return this.ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 226 */     if ((ioRatio <= 0) || (ioRatio > 100)) {
/* 227 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 229 */     this.ioRatio = ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int epollWait(boolean oldWakeup)
/*     */     throws IOException
/*     */   {
/* 237 */     if ((oldWakeup) && (hasTasks())) {
/* 238 */       return epollWaitNow();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 243 */     long curDeadlineNanos = deadlineNanos();
/* 244 */     int delayNanos; int delaySeconds; int delayNanos; if (curDeadlineNanos == this.prevDeadlineNanos) {
/* 245 */       int delaySeconds = -1;
/* 246 */       delayNanos = -1;
/*     */     } else {
/* 248 */       long totalDelay = delayNanos(System.nanoTime());
/* 249 */       this.prevDeadlineNanos = curDeadlineNanos;
/* 250 */       delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
/* 251 */       delayNanos = (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L);
/*     */     }
/* 253 */     return Native.epollWait(this.epollFd, this.events, this.timerFd, delaySeconds, delayNanos);
/*     */   }
/*     */   
/*     */   private int epollWaitNow() throws IOException {
/* 257 */     return Native.epollWait(this.epollFd, this.events, this.timerFd, 0, 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void handleLoopException(Throwable t)
/*     */   {
/* 351 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 356 */       Thread.sleep(1000L);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */   private void closeAll()
/*     */   {
/*     */     try {
/* 364 */       epollWaitNow();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/*     */ 
/*     */ 
/* 370 */     Collection<AbstractEpollChannel> array = new ArrayList(this.channels.size());
/*     */     
/* 372 */     for (AbstractEpollChannel channel : this.channels.values()) {
/* 373 */       array.add(channel);
/*     */     }
/*     */     
/* 376 */     for (AbstractEpollChannel ch : array) {
/* 377 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private void processReady(EpollEventArray events, int ready) {
/* 382 */     for (int i = 0; i < ready; i++) {
/* 383 */       int fd = events.fd(i);
/* 384 */       if (fd == this.eventFd.intValue())
/*     */       {
/* 386 */         Native.eventFdRead(fd);
/* 387 */       } else if (fd == this.timerFd.intValue())
/*     */       {
/* 389 */         Native.timerFdRead(fd);
/*     */       } else {
/* 391 */         long ev = events.events(i);
/*     */         
/* 393 */         AbstractEpollChannel ch = (AbstractEpollChannel)this.channels.get(fd);
/* 394 */         if (ch != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 399 */           AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 409 */           if ((ev & (Native.EPOLLERR | Native.EPOLLOUT)) != 0L)
/*     */           {
/* 411 */             unsafe.epollOutReady();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 419 */           if ((ev & (Native.EPOLLERR | Native.EPOLLIN)) != 0L)
/*     */           {
/* 421 */             unsafe.epollInReady();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 427 */           if ((ev & Native.EPOLLRDHUP) != 0L) {
/* 428 */             unsafe.epollRdHupReady();
/*     */           }
/*     */         }
/*     */         else {
/*     */           try {
/* 433 */             Native.epollCtlDel(this.epollFd.intValue(), fd);
/*     */           }
/*     */           catch (IOException localIOException) {}
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 16	io/netty/channel/epoll/EpollEventLoop:selectStrategy	Lio/netty/channel/SelectStrategy;
/*     */     //   4: aload_0
/*     */     //   5: getfield 11	io/netty/channel/epoll/EpollEventLoop:selectNowSupplier	Lio/netty/util/IntSupplier;
/*     */     //   8: aload_0
/*     */     //   9: invokevirtual 75	io/netty/channel/epoll/EpollEventLoop:hasTasks	()Z
/*     */     //   12: invokeinterface 85 3 0
/*     */     //   17: istore_1
/*     */     //   18: iload_1
/*     */     //   19: lookupswitch	default:+69->88, -2:+25->44, -1:+28->47
/*     */     //   44: goto -44 -> 0
/*     */     //   47: aload_0
/*     */     //   48: getstatic 46	io/netty/channel/epoll/EpollEventLoop:WAKEN_UP_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   51: aload_0
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual 86	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:getAndSet	(Ljava/lang/Object;I)I
/*     */     //   56: iconst_1
/*     */     //   57: if_icmpne +7 -> 64
/*     */     //   60: iconst_1
/*     */     //   61: goto +4 -> 65
/*     */     //   64: iconst_0
/*     */     //   65: invokespecial 87	io/netty/channel/epoll/EpollEventLoop:epollWait	(Z)I
/*     */     //   68: istore_1
/*     */     //   69: aload_0
/*     */     //   70: getfield 88	io/netty/channel/epoll/EpollEventLoop:wakenUp	I
/*     */     //   73: iconst_1
/*     */     //   74: if_icmpne +14 -> 88
/*     */     //   77: aload_0
/*     */     //   78: getfield 24	io/netty/channel/epoll/EpollEventLoop:eventFd	Lio/netty/channel/unix/FileDescriptor;
/*     */     //   81: invokevirtual 25	io/netty/channel/unix/FileDescriptor:intValue	()I
/*     */     //   84: lconst_1
/*     */     //   85: invokestatic 48	io/netty/channel/epoll/Native:eventFdWrite	(IJ)V
/*     */     //   88: aload_0
/*     */     //   89: getfield 12	io/netty/channel/epoll/EpollEventLoop:ioRatio	I
/*     */     //   92: istore_2
/*     */     //   93: iload_2
/*     */     //   94: bipush 100
/*     */     //   96: if_icmpne +35 -> 131
/*     */     //   99: iload_1
/*     */     //   100: ifle +12 -> 112
/*     */     //   103: aload_0
/*     */     //   104: aload_0
/*     */     //   105: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   108: iload_1
/*     */     //   109: invokespecial 89	io/netty/channel/epoll/EpollEventLoop:processReady	(Lio/netty/channel/epoll/EpollEventArray;I)V
/*     */     //   112: aload_0
/*     */     //   113: invokevirtual 90	io/netty/channel/epoll/EpollEventLoop:runAllTasks	()Z
/*     */     //   116: pop
/*     */     //   117: goto +11 -> 128
/*     */     //   120: astore_3
/*     */     //   121: aload_0
/*     */     //   122: invokevirtual 90	io/netty/channel/epoll/EpollEventLoop:runAllTasks	()Z
/*     */     //   125: pop
/*     */     //   126: aload_3
/*     */     //   127: athrow
/*     */     //   128: goto +74 -> 202
/*     */     //   131: invokestatic 77	java/lang/System:nanoTime	()J
/*     */     //   134: lstore_3
/*     */     //   135: iload_1
/*     */     //   136: ifle +12 -> 148
/*     */     //   139: aload_0
/*     */     //   140: aload_0
/*     */     //   141: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   144: iload_1
/*     */     //   145: invokespecial 89	io/netty/channel/epoll/EpollEventLoop:processReady	(Lio/netty/channel/epoll/EpollEventArray;I)V
/*     */     //   148: invokestatic 77	java/lang/System:nanoTime	()J
/*     */     //   151: lload_3
/*     */     //   152: lsub
/*     */     //   153: lstore 5
/*     */     //   155: aload_0
/*     */     //   156: lload 5
/*     */     //   158: bipush 100
/*     */     //   160: iload_2
/*     */     //   161: isub
/*     */     //   162: i2l
/*     */     //   163: lmul
/*     */     //   164: iload_2
/*     */     //   165: i2l
/*     */     //   166: ldiv
/*     */     //   167: invokevirtual 91	io/netty/channel/epoll/EpollEventLoop:runAllTasks	(J)Z
/*     */     //   170: pop
/*     */     //   171: goto +31 -> 202
/*     */     //   174: astore 7
/*     */     //   176: invokestatic 77	java/lang/System:nanoTime	()J
/*     */     //   179: lload_3
/*     */     //   180: lsub
/*     */     //   181: lstore 8
/*     */     //   183: aload_0
/*     */     //   184: lload 8
/*     */     //   186: bipush 100
/*     */     //   188: iload_2
/*     */     //   189: isub
/*     */     //   190: i2l
/*     */     //   191: lmul
/*     */     //   192: iload_2
/*     */     //   193: i2l
/*     */     //   194: ldiv
/*     */     //   195: invokevirtual 91	io/netty/channel/epoll/EpollEventLoop:runAllTasks	(J)Z
/*     */     //   198: pop
/*     */     //   199: aload 7
/*     */     //   201: athrow
/*     */     //   202: aload_0
/*     */     //   203: getfield 17	io/netty/channel/epoll/EpollEventLoop:allowGrowing	Z
/*     */     //   206: ifeq +21 -> 227
/*     */     //   209: iload_1
/*     */     //   210: aload_0
/*     */     //   211: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   214: invokevirtual 92	io/netty/channel/epoll/EpollEventArray:length	()I
/*     */     //   217: if_icmpne +10 -> 227
/*     */     //   220: aload_0
/*     */     //   221: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   224: invokevirtual 93	io/netty/channel/epoll/EpollEventArray:increase	()V
/*     */     //   227: goto +8 -> 235
/*     */     //   230: astore_1
/*     */     //   231: aload_1
/*     */     //   232: invokestatic 95	io/netty/channel/epoll/EpollEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   235: aload_0
/*     */     //   236: invokevirtual 96	io/netty/channel/epoll/EpollEventLoop:isShuttingDown	()Z
/*     */     //   239: ifeq +17 -> 256
/*     */     //   242: aload_0
/*     */     //   243: invokespecial 97	io/netty/channel/epoll/EpollEventLoop:closeAll	()V
/*     */     //   246: aload_0
/*     */     //   247: invokevirtual 98	io/netty/channel/epoll/EpollEventLoop:confirmShutdown	()Z
/*     */     //   250: ifeq +6 -> 256
/*     */     //   253: goto +14 -> 267
/*     */     //   256: goto -256 -> 0
/*     */     //   259: astore_1
/*     */     //   260: aload_1
/*     */     //   261: invokestatic 95	io/netty/channel/epoll/EpollEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   264: goto -264 -> 0
/*     */     //   267: return
/*     */     // Line number table:
/*     */     //   Java source line #264	-> byte code offset #0
/*     */     //   Java source line #265	-> byte code offset #18
/*     */     //   Java source line #267	-> byte code offset #44
/*     */     //   Java source line #269	-> byte code offset #47
/*     */     //   Java source line #299	-> byte code offset #69
/*     */     //   Java source line #300	-> byte code offset #77
/*     */     //   Java source line #306	-> byte code offset #88
/*     */     //   Java source line #307	-> byte code offset #93
/*     */     //   Java source line #309	-> byte code offset #99
/*     */     //   Java source line #310	-> byte code offset #103
/*     */     //   Java source line #314	-> byte code offset #112
/*     */     //   Java source line #315	-> byte code offset #117
/*     */     //   Java source line #314	-> byte code offset #120
/*     */     //   Java source line #315	-> byte code offset #126
/*     */     //   Java source line #317	-> byte code offset #131
/*     */     //   Java source line #320	-> byte code offset #135
/*     */     //   Java source line #321	-> byte code offset #139
/*     */     //   Java source line #325	-> byte code offset #148
/*     */     //   Java source line #326	-> byte code offset #155
/*     */     //   Java source line #327	-> byte code offset #171
/*     */     //   Java source line #325	-> byte code offset #174
/*     */     //   Java source line #326	-> byte code offset #183
/*     */     //   Java source line #327	-> byte code offset #199
/*     */     //   Java source line #329	-> byte code offset #202
/*     */     //   Java source line #331	-> byte code offset #220
/*     */     //   Java source line #335	-> byte code offset #227
/*     */     //   Java source line #333	-> byte code offset #230
/*     */     //   Java source line #334	-> byte code offset #231
/*     */     //   Java source line #338	-> byte code offset #235
/*     */     //   Java source line #339	-> byte code offset #242
/*     */     //   Java source line #340	-> byte code offset #246
/*     */     //   Java source line #341	-> byte code offset #253
/*     */     //   Java source line #346	-> byte code offset #256
/*     */     //   Java source line #344	-> byte code offset #259
/*     */     //   Java source line #345	-> byte code offset #260
/*     */     //   Java source line #346	-> byte code offset #264
/*     */     //   Java source line #348	-> byte code offset #267
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	268	0	this	EpollEventLoop
/*     */     //   17	193	1	strategy	int
/*     */     //   230	2	1	t	Throwable
/*     */     //   259	2	1	t	Throwable
/*     */     //   92	101	2	ioRatio	int
/*     */     //   120	7	3	localObject1	Object
/*     */     //   134	46	3	ioStartTime	long
/*     */     //   153	4	5	ioTime	long
/*     */     //   174	26	7	localObject2	Object
/*     */     //   181	4	8	ioTime	long
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   99	112	120	finally
/*     */     //   135	148	174	finally
/*     */     //   174	176	174	finally
/*     */     //   0	44	230	java/lang/Throwable
/*     */     //   47	227	230	java/lang/Throwable
/*     */     //   235	253	259	java/lang/Throwable
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void cleanup()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 22	io/netty/channel/epoll/EpollEventLoop:epollFd	Lio/netty/channel/unix/FileDescriptor;
/*     */     //   4: invokevirtual 36	io/netty/channel/unix/FileDescriptor:close	()V
/*     */     //   7: goto +15 -> 22
/*     */     //   10: astore_1
/*     */     //   11: getstatic 99	io/netty/channel/epoll/EpollEventLoop:logger	Lio/netty/util/internal/logging/InternalLogger;
/*     */     //   14: ldc -126
/*     */     //   16: aload_1
/*     */     //   17: invokeinterface 101 3 0
/*     */     //   22: aload_0
/*     */     //   23: getfield 24	io/netty/channel/epoll/EpollEventLoop:eventFd	Lio/netty/channel/unix/FileDescriptor;
/*     */     //   26: invokevirtual 36	io/netty/channel/unix/FileDescriptor:close	()V
/*     */     //   29: goto +15 -> 44
/*     */     //   32: astore_1
/*     */     //   33: getstatic 99	io/netty/channel/epoll/EpollEventLoop:logger	Lio/netty/util/internal/logging/InternalLogger;
/*     */     //   36: ldc -125
/*     */     //   38: aload_1
/*     */     //   39: invokeinterface 101 3 0
/*     */     //   44: aload_0
/*     */     //   45: getfield 33	io/netty/channel/epoll/EpollEventLoop:timerFd	Lio/netty/channel/unix/FileDescriptor;
/*     */     //   48: invokevirtual 36	io/netty/channel/unix/FileDescriptor:close	()V
/*     */     //   51: goto +15 -> 66
/*     */     //   54: astore_1
/*     */     //   55: getstatic 99	io/netty/channel/epoll/EpollEventLoop:logger	Lio/netty/util/internal/logging/InternalLogger;
/*     */     //   58: ldc -124
/*     */     //   60: aload_1
/*     */     //   61: invokeinterface 101 3 0
/*     */     //   66: aload_0
/*     */     //   67: getfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   70: ifnull +15 -> 85
/*     */     //   73: aload_0
/*     */     //   74: getfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   77: invokevirtual 133	io/netty/channel/unix/IovArray:release	()V
/*     */     //   80: aload_0
/*     */     //   81: aconst_null
/*     */     //   82: putfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   85: aload_0
/*     */     //   86: getfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   89: ifnull +15 -> 104
/*     */     //   92: aload_0
/*     */     //   93: getfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   96: invokevirtual 134	io/netty/channel/epoll/NativeDatagramPacketArray:release	()V
/*     */     //   99: aload_0
/*     */     //   100: aconst_null
/*     */     //   101: putfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   104: aload_0
/*     */     //   105: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   108: invokevirtual 135	io/netty/channel/epoll/EpollEventArray:free	()V
/*     */     //   111: goto +51 -> 162
/*     */     //   114: astore_2
/*     */     //   115: aload_0
/*     */     //   116: getfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   119: ifnull +15 -> 134
/*     */     //   122: aload_0
/*     */     //   123: getfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   126: invokevirtual 133	io/netty/channel/unix/IovArray:release	()V
/*     */     //   129: aload_0
/*     */     //   130: aconst_null
/*     */     //   131: putfield 38	io/netty/channel/epoll/EpollEventLoop:iovArray	Lio/netty/channel/unix/IovArray;
/*     */     //   134: aload_0
/*     */     //   135: getfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   138: ifnull +15 -> 153
/*     */     //   141: aload_0
/*     */     //   142: getfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   145: invokevirtual 134	io/netty/channel/epoll/NativeDatagramPacketArray:release	()V
/*     */     //   148: aload_0
/*     */     //   149: aconst_null
/*     */     //   150: putfield 42	io/netty/channel/epoll/EpollEventLoop:datagramPacketArray	Lio/netty/channel/epoll/NativeDatagramPacketArray;
/*     */     //   153: aload_0
/*     */     //   154: getfield 20	io/netty/channel/epoll/EpollEventLoop:events	Lio/netty/channel/epoll/EpollEventArray;
/*     */     //   157: invokevirtual 135	io/netty/channel/epoll/EpollEventArray:free	()V
/*     */     //   160: aload_2
/*     */     //   161: athrow
/*     */     //   162: return
/*     */     // Line number table:
/*     */     //   Java source line #449	-> byte code offset #0
/*     */     //   Java source line #452	-> byte code offset #7
/*     */     //   Java source line #450	-> byte code offset #10
/*     */     //   Java source line #451	-> byte code offset #11
/*     */     //   Java source line #454	-> byte code offset #22
/*     */     //   Java source line #457	-> byte code offset #29
/*     */     //   Java source line #455	-> byte code offset #32
/*     */     //   Java source line #456	-> byte code offset #33
/*     */     //   Java source line #459	-> byte code offset #44
/*     */     //   Java source line #462	-> byte code offset #51
/*     */     //   Java source line #460	-> byte code offset #54
/*     */     //   Java source line #461	-> byte code offset #55
/*     */     //   Java source line #465	-> byte code offset #66
/*     */     //   Java source line #466	-> byte code offset #73
/*     */     //   Java source line #467	-> byte code offset #80
/*     */     //   Java source line #469	-> byte code offset #85
/*     */     //   Java source line #470	-> byte code offset #92
/*     */     //   Java source line #471	-> byte code offset #99
/*     */     //   Java source line #473	-> byte code offset #104
/*     */     //   Java source line #474	-> byte code offset #111
/*     */     //   Java source line #465	-> byte code offset #114
/*     */     //   Java source line #466	-> byte code offset #122
/*     */     //   Java source line #467	-> byte code offset #129
/*     */     //   Java source line #469	-> byte code offset #134
/*     */     //   Java source line #470	-> byte code offset #141
/*     */     //   Java source line #471	-> byte code offset #148
/*     */     //   Java source line #473	-> byte code offset #153
/*     */     //   Java source line #474	-> byte code offset #160
/*     */     //   Java source line #475	-> byte code offset #162
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	163	0	this	EpollEventLoop
/*     */     //   10	7	1	e	IOException
/*     */     //   32	7	1	e	IOException
/*     */     //   54	7	1	e	IOException
/*     */     //   114	47	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	7	10	java/io/IOException
/*     */     //   22	29	32	java/io/IOException
/*     */     //   44	51	54	java/io/IOException
/*     */     //   0	66	114	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */