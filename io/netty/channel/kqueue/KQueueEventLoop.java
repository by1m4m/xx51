/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
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
/*     */ final class KQueueEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final AtomicIntegerFieldUpdater<KQueueEventLoop> WAKEN_UP_UPDATER;
/*     */   private static final int KQUEUE_WAKE_UP_IDENT = 0;
/*     */   private final NativeLongArray jniChannelPointers;
/*     */   private final boolean allowGrowing;
/*     */   private final FileDescriptor kqueueFd;
/*     */   private final KQueueEventArray changeList;
/*     */   private final KQueueEventArray eventList;
/*     */   private final SelectStrategy selectStrategy;
/*     */   
/*     */   static
/*     */   {
/*  45 */     logger = InternalLoggerFactory.getInstance(KQueueEventLoop.class);
/*     */     
/*  47 */     WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(KQueueEventLoop.class, "wakenUp");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */     KQueue.ensureAvailability();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private final IovArray iovArray = new IovArray();
/*  63 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */   {
/*     */     public int get() throws Exception {
/*  66 */       return KQueueEventLoop.this.kqueueWaitNow();
/*     */     }
/*     */   };
/*     */   
/*     */   private volatile int wakenUp;
/*  71 */   private volatile int ioRatio = 50;
/*     */   
/*     */   KQueueEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/*  75 */     super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/*  76 */     this.selectStrategy = ((SelectStrategy)ObjectUtil.checkNotNull(strategy, "strategy"));
/*  77 */     this.kqueueFd = Native.newKQueue();
/*  78 */     if (maxEvents == 0) {
/*  79 */       this.allowGrowing = true;
/*  80 */       maxEvents = 4096;
/*     */     } else {
/*  82 */       this.allowGrowing = false;
/*     */     }
/*  84 */     this.changeList = new KQueueEventArray(maxEvents);
/*  85 */     this.eventList = new KQueueEventArray(maxEvents);
/*  86 */     this.jniChannelPointers = new NativeLongArray(4096);
/*  87 */     int result = Native.keventAddUserEvent(this.kqueueFd.intValue(), 0);
/*  88 */     if (result < 0) {
/*  89 */       cleanup();
/*  90 */       throw new IllegalStateException("kevent failed to add user event with errno: " + -result);
/*     */     }
/*     */   }
/*     */   
/*     */   void evSet(AbstractKQueueChannel ch, short filter, short flags, int fflags) {
/*  95 */     this.changeList.evSet(ch, filter, flags, fflags);
/*     */   }
/*     */   
/*     */   void remove(AbstractKQueueChannel ch) throws IOException {
/*  99 */     assert (inEventLoop());
/* 100 */     if (ch.jniSelfPtr == 0L) {
/* 101 */       return;
/*     */     }
/*     */     
/* 104 */     this.jniChannelPointers.add(ch.jniSelfPtr);
/* 105 */     ch.jniSelfPtr = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   IovArray cleanArray()
/*     */   {
/* 112 */     this.iovArray.clear();
/* 113 */     return this.iovArray;
/*     */   }
/*     */   
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 118 */     if ((!inEventLoop) && (WAKEN_UP_UPDATER.compareAndSet(this, 0, 1))) {
/* 119 */       wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   private void wakeup() {
/* 124 */     Native.keventTriggerUserEvent(this.kqueueFd.intValue(), 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int kqueueWait(boolean oldWakeup)
/*     */     throws IOException
/*     */   {
/* 134 */     if ((oldWakeup) && (hasTasks())) {
/* 135 */       return kqueueWaitNow();
/*     */     }
/*     */     
/* 138 */     long totalDelay = delayNanos(System.nanoTime());
/* 139 */     int delaySeconds = (int)Math.min(totalDelay / 1000000000L, 2147483647L);
/* 140 */     return kqueueWait(delaySeconds, (int)Math.min(totalDelay - delaySeconds * 1000000000L, 2147483647L));
/*     */   }
/*     */   
/*     */   private int kqueueWaitNow() throws IOException {
/* 144 */     return kqueueWait(0, 0);
/*     */   }
/*     */   
/*     */   private int kqueueWait(int timeoutSec, int timeoutNs) throws IOException {
/* 148 */     deleteJniChannelPointers();
/* 149 */     int numEvents = Native.keventWait(this.kqueueFd.intValue(), this.changeList, this.eventList, timeoutSec, timeoutNs);
/* 150 */     this.changeList.clear();
/* 151 */     return numEvents;
/*     */   }
/*     */   
/*     */   private void deleteJniChannelPointers() {
/* 155 */     if (!this.jniChannelPointers.isEmpty()) {
/* 156 */       KQueueEventArray.deleteGlobalRefs(this.jniChannelPointers.memoryAddress(), this.jniChannelPointers.memoryAddressEnd());
/* 157 */       this.jniChannelPointers.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private void processReady(int ready) {
/* 162 */     for (int i = 0; i < ready; i++) {
/* 163 */       short filter = this.eventList.filter(i);
/* 164 */       short flags = this.eventList.flags(i);
/* 165 */       if ((filter == Native.EVFILT_USER) || ((flags & Native.EV_ERROR) != 0))
/*     */       {
/*     */ 
/* 168 */         if ((!$assertionsDisabled) && (filter == Native.EVFILT_USER) && ((filter != Native.EVFILT_USER) || 
/* 169 */           (this.eventList.fd(i) != 0))) {
/* 168 */           throw new AssertionError();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 173 */         AbstractKQueueChannel channel = this.eventList.channel(i);
/* 174 */         if (channel == null)
/*     */         {
/*     */ 
/*     */ 
/* 178 */           logger.warn("events[{}]=[{}, {}] had no channel!", new Object[] { Integer.valueOf(i), Integer.valueOf(this.eventList.fd(i)), Short.valueOf(filter) });
/*     */         }
/*     */         else
/*     */         {
/* 182 */           AbstractKQueueChannel.AbstractKQueueUnsafe unsafe = (AbstractKQueueChannel.AbstractKQueueUnsafe)channel.unsafe();
/*     */           
/*     */ 
/* 185 */           if (filter == Native.EVFILT_WRITE) {
/* 186 */             unsafe.writeReady();
/* 187 */           } else if (filter == Native.EVFILT_READ)
/*     */           {
/* 189 */             unsafe.readReady(this.eventList.data(i));
/* 190 */           } else if ((filter == Native.EVFILT_SOCK) && ((this.eventList.fflags(i) & Native.NOTE_RDHUP) != 0)) {
/* 191 */             unsafe.readEOF();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 197 */           if ((flags & Native.EV_EOF) != 0) {
/* 198 */             unsafe.readEOF();
/*     */           }
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks)
/*     */   {
/* 294 */     return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : 
/* 295 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIoRatio()
/*     */   {
/* 302 */     return this.ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 310 */     if ((ioRatio <= 0) || (ioRatio > 100)) {
/* 311 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 313 */     this.ioRatio = ioRatio;
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
/*     */   private void closeAll()
/*     */   {
/*     */     try
/*     */     {
/* 339 */       kqueueWaitNow();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */   private static void handleLoopException(Throwable t)
/*     */   {
/* 346 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 351 */       Thread.sleep(1000L);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 14	io/netty/channel/kqueue/KQueueEventLoop:selectStrategy	Lio/netty/channel/SelectStrategy;
/*     */     //   4: aload_0
/*     */     //   5: getfield 9	io/netty/channel/kqueue/KQueueEventLoop:selectNowSupplier	Lio/netty/util/IntSupplier;
/*     */     //   8: aload_0
/*     */     //   9: invokevirtual 49	io/netty/channel/kqueue/KQueueEventLoop:hasTasks	()Z
/*     */     //   12: invokeinterface 91 3 0
/*     */     //   17: istore_1
/*     */     //   18: iload_1
/*     */     //   19: lookupswitch	default:+62->81, -2:+25->44, -1:+28->47
/*     */     //   44: goto -44 -> 0
/*     */     //   47: aload_0
/*     */     //   48: getstatic 45	io/netty/channel/kqueue/KQueueEventLoop:WAKEN_UP_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   51: aload_0
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual 92	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:getAndSet	(Ljava/lang/Object;I)I
/*     */     //   56: iconst_1
/*     */     //   57: if_icmpne +7 -> 64
/*     */     //   60: iconst_1
/*     */     //   61: goto +4 -> 65
/*     */     //   64: iconst_0
/*     */     //   65: invokespecial 93	io/netty/channel/kqueue/KQueueEventLoop:kqueueWait	(Z)I
/*     */     //   68: istore_1
/*     */     //   69: aload_0
/*     */     //   70: getfield 94	io/netty/channel/kqueue/KQueueEventLoop:wakenUp	I
/*     */     //   73: iconst_1
/*     */     //   74: if_icmpne +7 -> 81
/*     */     //   77: aload_0
/*     */     //   78: invokespecial 47	io/netty/channel/kqueue/KQueueEventLoop:wakeup	()V
/*     */     //   81: aload_0
/*     */     //   82: getfield 10	io/netty/channel/kqueue/KQueueEventLoop:ioRatio	I
/*     */     //   85: istore_2
/*     */     //   86: iload_2
/*     */     //   87: bipush 100
/*     */     //   89: if_icmpne +31 -> 120
/*     */     //   92: iload_1
/*     */     //   93: ifle +8 -> 101
/*     */     //   96: aload_0
/*     */     //   97: iload_1
/*     */     //   98: invokespecial 95	io/netty/channel/kqueue/KQueueEventLoop:processReady	(I)V
/*     */     //   101: aload_0
/*     */     //   102: invokevirtual 96	io/netty/channel/kqueue/KQueueEventLoop:runAllTasks	()Z
/*     */     //   105: pop
/*     */     //   106: goto +11 -> 117
/*     */     //   109: astore_3
/*     */     //   110: aload_0
/*     */     //   111: invokevirtual 96	io/netty/channel/kqueue/KQueueEventLoop:runAllTasks	()Z
/*     */     //   114: pop
/*     */     //   115: aload_3
/*     */     //   116: athrow
/*     */     //   117: goto +70 -> 187
/*     */     //   120: invokestatic 50	java/lang/System:nanoTime	()J
/*     */     //   123: lstore_3
/*     */     //   124: iload_1
/*     */     //   125: ifle +8 -> 133
/*     */     //   128: aload_0
/*     */     //   129: iload_1
/*     */     //   130: invokespecial 95	io/netty/channel/kqueue/KQueueEventLoop:processReady	(I)V
/*     */     //   133: invokestatic 50	java/lang/System:nanoTime	()J
/*     */     //   136: lload_3
/*     */     //   137: lsub
/*     */     //   138: lstore 5
/*     */     //   140: aload_0
/*     */     //   141: lload 5
/*     */     //   143: bipush 100
/*     */     //   145: iload_2
/*     */     //   146: isub
/*     */     //   147: i2l
/*     */     //   148: lmul
/*     */     //   149: iload_2
/*     */     //   150: i2l
/*     */     //   151: ldiv
/*     */     //   152: invokevirtual 97	io/netty/channel/kqueue/KQueueEventLoop:runAllTasks	(J)Z
/*     */     //   155: pop
/*     */     //   156: goto +31 -> 187
/*     */     //   159: astore 7
/*     */     //   161: invokestatic 50	java/lang/System:nanoTime	()J
/*     */     //   164: lload_3
/*     */     //   165: lsub
/*     */     //   166: lstore 8
/*     */     //   168: aload_0
/*     */     //   169: lload 8
/*     */     //   171: bipush 100
/*     */     //   173: iload_2
/*     */     //   174: isub
/*     */     //   175: i2l
/*     */     //   176: lmul
/*     */     //   177: iload_2
/*     */     //   178: i2l
/*     */     //   179: ldiv
/*     */     //   180: invokevirtual 97	io/netty/channel/kqueue/KQueueEventLoop:runAllTasks	(J)Z
/*     */     //   183: pop
/*     */     //   184: aload 7
/*     */     //   186: athrow
/*     */     //   187: aload_0
/*     */     //   188: getfield 17	io/netty/channel/kqueue/KQueueEventLoop:allowGrowing	Z
/*     */     //   191: ifeq +22 -> 213
/*     */     //   194: iload_1
/*     */     //   195: aload_0
/*     */     //   196: getfield 21	io/netty/channel/kqueue/KQueueEventLoop:eventList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   199: invokevirtual 98	io/netty/channel/kqueue/KQueueEventArray:capacity	()I
/*     */     //   202: if_icmpne +11 -> 213
/*     */     //   205: aload_0
/*     */     //   206: getfield 21	io/netty/channel/kqueue/KQueueEventLoop:eventList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   209: iconst_0
/*     */     //   210: invokevirtual 99	io/netty/channel/kqueue/KQueueEventArray:realloc	(Z)V
/*     */     //   213: goto +8 -> 221
/*     */     //   216: astore_1
/*     */     //   217: aload_1
/*     */     //   218: invokestatic 101	io/netty/channel/kqueue/KQueueEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   221: aload_0
/*     */     //   222: invokevirtual 102	io/netty/channel/kqueue/KQueueEventLoop:isShuttingDown	()Z
/*     */     //   225: ifeq +17 -> 242
/*     */     //   228: aload_0
/*     */     //   229: invokespecial 103	io/netty/channel/kqueue/KQueueEventLoop:closeAll	()V
/*     */     //   232: aload_0
/*     */     //   233: invokevirtual 104	io/netty/channel/kqueue/KQueueEventLoop:confirmShutdown	()Z
/*     */     //   236: ifeq +6 -> 242
/*     */     //   239: goto +14 -> 253
/*     */     //   242: goto -242 -> 0
/*     */     //   245: astore_1
/*     */     //   246: aload_1
/*     */     //   247: invokestatic 101	io/netty/channel/kqueue/KQueueEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   250: goto -250 -> 0
/*     */     //   253: return
/*     */     // Line number table:
/*     */     //   Java source line #207	-> byte code offset #0
/*     */     //   Java source line #208	-> byte code offset #18
/*     */     //   Java source line #210	-> byte code offset #44
/*     */     //   Java source line #212	-> byte code offset #47
/*     */     //   Java source line #242	-> byte code offset #69
/*     */     //   Java source line #243	-> byte code offset #77
/*     */     //   Java source line #249	-> byte code offset #81
/*     */     //   Java source line #250	-> byte code offset #86
/*     */     //   Java source line #252	-> byte code offset #92
/*     */     //   Java source line #253	-> byte code offset #96
/*     */     //   Java source line #256	-> byte code offset #101
/*     */     //   Java source line #257	-> byte code offset #106
/*     */     //   Java source line #256	-> byte code offset #109
/*     */     //   Java source line #257	-> byte code offset #115
/*     */     //   Java source line #259	-> byte code offset #120
/*     */     //   Java source line #262	-> byte code offset #124
/*     */     //   Java source line #263	-> byte code offset #128
/*     */     //   Java source line #266	-> byte code offset #133
/*     */     //   Java source line #267	-> byte code offset #140
/*     */     //   Java source line #268	-> byte code offset #156
/*     */     //   Java source line #266	-> byte code offset #159
/*     */     //   Java source line #267	-> byte code offset #168
/*     */     //   Java source line #268	-> byte code offset #184
/*     */     //   Java source line #270	-> byte code offset #187
/*     */     //   Java source line #272	-> byte code offset #205
/*     */     //   Java source line #276	-> byte code offset #213
/*     */     //   Java source line #274	-> byte code offset #216
/*     */     //   Java source line #275	-> byte code offset #217
/*     */     //   Java source line #279	-> byte code offset #221
/*     */     //   Java source line #280	-> byte code offset #228
/*     */     //   Java source line #281	-> byte code offset #232
/*     */     //   Java source line #282	-> byte code offset #239
/*     */     //   Java source line #287	-> byte code offset #242
/*     */     //   Java source line #285	-> byte code offset #245
/*     */     //   Java source line #286	-> byte code offset #246
/*     */     //   Java source line #287	-> byte code offset #250
/*     */     //   Java source line #289	-> byte code offset #253
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	254	0	this	KQueueEventLoop
/*     */     //   17	178	1	strategy	int
/*     */     //   216	2	1	t	Throwable
/*     */     //   245	2	1	t	Throwable
/*     */     //   85	93	2	ioRatio	int
/*     */     //   109	7	3	localObject1	Object
/*     */     //   123	42	3	ioStartTime	long
/*     */     //   138	4	5	ioTime	long
/*     */     //   159	26	7	localObject2	Object
/*     */     //   166	4	8	ioTime	long
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   92	101	109	finally
/*     */     //   124	133	159	finally
/*     */     //   159	161	159	finally
/*     */     //   0	44	216	java/lang/Throwable
/*     */     //   47	213	216	java/lang/Throwable
/*     */     //   221	239	245	java/lang/Throwable
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void cleanup()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 16	io/netty/channel/kqueue/KQueueEventLoop:kqueueFd	Lio/netty/channel/unix/FileDescriptor;
/*     */     //   4: invokevirtual 112	io/netty/channel/unix/FileDescriptor:close	()V
/*     */     //   7: goto +15 -> 22
/*     */     //   10: astore_1
/*     */     //   11: getstatic 73	io/netty/channel/kqueue/KQueueEventLoop:logger	Lio/netty/util/internal/logging/InternalLogger;
/*     */     //   14: ldc 114
/*     */     //   16: aload_1
/*     */     //   17: invokeinterface 115 3 0
/*     */     //   22: aload_0
/*     */     //   23: invokespecial 59	io/netty/channel/kqueue/KQueueEventLoop:deleteJniChannelPointers	()V
/*     */     //   26: aload_0
/*     */     //   27: getfield 24	io/netty/channel/kqueue/KQueueEventLoop:jniChannelPointers	Lio/netty/channel/kqueue/NativeLongArray;
/*     */     //   30: invokevirtual 116	io/netty/channel/kqueue/NativeLongArray:free	()V
/*     */     //   33: aload_0
/*     */     //   34: getfield 20	io/netty/channel/kqueue/KQueueEventLoop:changeList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   37: invokevirtual 117	io/netty/channel/kqueue/KQueueEventArray:free	()V
/*     */     //   40: aload_0
/*     */     //   41: getfield 21	io/netty/channel/kqueue/KQueueEventLoop:eventList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   44: invokevirtual 117	io/netty/channel/kqueue/KQueueEventArray:free	()V
/*     */     //   47: goto +31 -> 78
/*     */     //   50: astore_2
/*     */     //   51: aload_0
/*     */     //   52: invokespecial 59	io/netty/channel/kqueue/KQueueEventLoop:deleteJniChannelPointers	()V
/*     */     //   55: aload_0
/*     */     //   56: getfield 24	io/netty/channel/kqueue/KQueueEventLoop:jniChannelPointers	Lio/netty/channel/kqueue/NativeLongArray;
/*     */     //   59: invokevirtual 116	io/netty/channel/kqueue/NativeLongArray:free	()V
/*     */     //   62: aload_0
/*     */     //   63: getfield 20	io/netty/channel/kqueue/KQueueEventLoop:changeList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   66: invokevirtual 117	io/netty/channel/kqueue/KQueueEventArray:free	()V
/*     */     //   69: aload_0
/*     */     //   70: getfield 21	io/netty/channel/kqueue/KQueueEventLoop:eventList	Lio/netty/channel/kqueue/KQueueEventArray;
/*     */     //   73: invokevirtual 117	io/netty/channel/kqueue/KQueueEventArray:free	()V
/*     */     //   76: aload_2
/*     */     //   77: athrow
/*     */     //   78: return
/*     */     // Line number table:
/*     */     //   Java source line #320	-> byte code offset #0
/*     */     //   Java source line #323	-> byte code offset #7
/*     */     //   Java source line #321	-> byte code offset #10
/*     */     //   Java source line #322	-> byte code offset #11
/*     */     //   Java source line #329	-> byte code offset #22
/*     */     //   Java source line #330	-> byte code offset #26
/*     */     //   Java source line #332	-> byte code offset #33
/*     */     //   Java source line #333	-> byte code offset #40
/*     */     //   Java source line #334	-> byte code offset #47
/*     */     //   Java source line #329	-> byte code offset #50
/*     */     //   Java source line #330	-> byte code offset #55
/*     */     //   Java source line #332	-> byte code offset #62
/*     */     //   Java source line #333	-> byte code offset #69
/*     */     //   Java source line #334	-> byte code offset #76
/*     */     //   Java source line #335	-> byte code offset #78
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	79	0	this	KQueueEventLoop
/*     */     //   10	7	1	e	IOException
/*     */     //   50	27	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	7	10	java/io/IOException
/*     */     //   0	22	50	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\KQueueEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */