/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashedWheelTimer
/*     */   implements Timer
/*     */ {
/*  82 */   static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
/*     */   
/*  84 */   private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();
/*  85 */   private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();
/*     */   private static final int INSTANCE_COUNT_LIMIT = 64;
/*  87 */   private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = ResourceLeakDetectorFactory.instance()
/*  88 */     .newResourceLeakDetector(HashedWheelTimer.class, 1);
/*     */   
/*     */ 
/*  91 */   private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
/*     */   
/*     */   private final ResourceLeakTracker<HashedWheelTimer> leak;
/*  94 */   private final Worker worker = new Worker(null);
/*     */   
/*     */   private final Thread workerThread;
/*     */   
/*     */   public static final int WORKER_STATE_INIT = 0;
/*     */   
/*     */   public static final int WORKER_STATE_STARTED = 1;
/*     */   public static final int WORKER_STATE_SHUTDOWN = 2;
/*     */   private volatile int workerState;
/*     */   private final long tickDuration;
/*     */   private final HashedWheelBucket[] wheel;
/*     */   private final int mask;
/* 106 */   private final CountDownLatch startTimeInitialized = new CountDownLatch(1);
/* 107 */   private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();
/* 108 */   private final Queue<HashedWheelTimeout> cancelledTimeouts = PlatformDependent.newMpscQueue();
/* 109 */   private final AtomicLong pendingTimeouts = new AtomicLong(0L);
/*     */   
/*     */ 
/*     */   private final long maxPendingTimeouts;
/*     */   
/*     */ 
/*     */   private volatile long startTime;
/*     */   
/*     */ 
/*     */   public HashedWheelTimer()
/*     */   {
/* 120 */     this(Executors.defaultThreadFactory());
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit)
/*     */   {
/* 134 */     this(Executors.defaultThreadFactory(), tickDuration, unit);
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel)
/*     */   {
/* 148 */     this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory)
/*     */   {
/* 161 */     this(threadFactory, 100L, TimeUnit.MILLISECONDS);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit)
/*     */   {
/* 177 */     this(threadFactory, tickDuration, unit, 512);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel)
/*     */   {
/* 195 */     this(threadFactory, tickDuration, unit, ticksPerWheel, true);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection)
/*     */   {
/* 216 */     this(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1L);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, long maxPendingTimeouts)
/*     */   {
/* 244 */     if (threadFactory == null) {
/* 245 */       throw new NullPointerException("threadFactory");
/*     */     }
/* 247 */     if (unit == null) {
/* 248 */       throw new NullPointerException("unit");
/*     */     }
/* 250 */     if (tickDuration <= 0L) {
/* 251 */       throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
/*     */     }
/* 253 */     if (ticksPerWheel <= 0) {
/* 254 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */     
/*     */ 
/* 258 */     this.wheel = createWheel(ticksPerWheel);
/* 259 */     this.mask = (this.wheel.length - 1);
/*     */     
/*     */ 
/* 262 */     this.tickDuration = unit.toNanos(tickDuration);
/*     */     
/*     */ 
/* 265 */     if (this.tickDuration >= Long.MAX_VALUE / this.wheel.length) {
/* 266 */       throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", new Object[] {
/*     */       
/* 268 */         Long.valueOf(tickDuration), Long.valueOf(Long.MAX_VALUE / this.wheel.length) }));
/*     */     }
/* 270 */     this.workerThread = threadFactory.newThread(this.worker);
/*     */     
/* 272 */     this.leak = ((leakDetection) || (!this.workerThread.isDaemon()) ? leakDetector.track(this) : null);
/*     */     
/* 274 */     this.maxPendingTimeouts = maxPendingTimeouts;
/*     */     
/* 276 */     if ((INSTANCE_COUNTER.incrementAndGet() > 64) && 
/* 277 */       (WARNED_TOO_MANY_INSTANCES.compareAndSet(false, true))) {
/* 278 */       reportTooManyInstances();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 250	java/lang/Object:finalize	()V
/*     */     //   4: getstatic 252	io/netty/util/HashedWheelTimer:WORKER_STATE_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   7: aload_0
/*     */     //   8: iconst_2
/*     */     //   9: invokevirtual 258	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:getAndSet	(Ljava/lang/Object;I)I
/*     */     //   12: iconst_2
/*     */     //   13: if_icmpeq +35 -> 48
/*     */     //   16: getstatic 228	io/netty/util/HashedWheelTimer:INSTANCE_COUNTER	Ljava/util/concurrent/atomic/AtomicInteger;
/*     */     //   19: invokevirtual 261	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
/*     */     //   22: pop
/*     */     //   23: goto +25 -> 48
/*     */     //   26: astore_1
/*     */     //   27: getstatic 252	io/netty/util/HashedWheelTimer:WORKER_STATE_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   30: aload_0
/*     */     //   31: iconst_2
/*     */     //   32: invokevirtual 258	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:getAndSet	(Ljava/lang/Object;I)I
/*     */     //   35: iconst_2
/*     */     //   36: if_icmpeq +10 -> 46
/*     */     //   39: getstatic 228	io/netty/util/HashedWheelTimer:INSTANCE_COUNTER	Ljava/util/concurrent/atomic/AtomicInteger;
/*     */     //   42: invokevirtual 261	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
/*     */     //   45: pop
/*     */     //   46: aload_1
/*     */     //   47: athrow
/*     */     //   48: return
/*     */     // Line number table:
/*     */     //   Java source line #285	-> byte code offset #0
/*     */     //   Java source line #289	-> byte code offset #4
/*     */     //   Java source line #290	-> byte code offset #16
/*     */     //   Java source line #289	-> byte code offset #26
/*     */     //   Java source line #290	-> byte code offset #39
/*     */     //   Java source line #292	-> byte code offset #46
/*     */     //   Java source line #293	-> byte code offset #48
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	49	0	this	HashedWheelTimer
/*     */     //   26	21	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	26	finally
/*     */   }
/*     */   
/*     */   private static HashedWheelBucket[] createWheel(int ticksPerWheel)
/*     */   {
/* 296 */     if (ticksPerWheel <= 0) {
/* 297 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */     
/* 300 */     if (ticksPerWheel > 1073741824) {
/* 301 */       throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
/*     */     }
/*     */     
/*     */ 
/* 305 */     ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
/* 306 */     HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
/* 307 */     for (int i = 0; i < wheel.length; i++) {
/* 308 */       wheel[i] = new HashedWheelBucket(null);
/*     */     }
/* 310 */     return wheel;
/*     */   }
/*     */   
/*     */   private static int normalizeTicksPerWheel(int ticksPerWheel) {
/* 314 */     int normalizedTicksPerWheel = 1;
/* 315 */     while (normalizedTicksPerWheel < ticksPerWheel) {
/* 316 */       normalizedTicksPerWheel <<= 1;
/*     */     }
/* 318 */     return normalizedTicksPerWheel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 329 */     switch (WORKER_STATE_UPDATER.get(this)) {
/*     */     case 0: 
/* 331 */       if (WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
/* 332 */         this.workerThread.start();
/*     */       }
/*     */       break;
/*     */     case 1: 
/*     */       break;
/*     */     case 2: 
/* 338 */       throw new IllegalStateException("cannot be started once stopped");
/*     */     default: 
/* 340 */       throw new Error("Invalid WorkerState");
/*     */     }
/*     */     
/*     */     
/* 344 */     while (this.startTime == 0L) {
/*     */       try {
/* 346 */         this.startTimeInitialized.await();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Set<Timeout> stop()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 307	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: aload_0
/*     */     //   4: getfield 206	io/netty/util/HashedWheelTimer:workerThread	Ljava/lang/Thread;
/*     */     //   7: if_acmpne +44 -> 51
/*     */     //   10: new 288	java/lang/IllegalStateException
/*     */     //   13: dup
/*     */     //   14: new 150	java/lang/StringBuilder
/*     */     //   17: dup
/*     */     //   18: invokespecial 151	java/lang/StringBuilder:<init>	()V
/*     */     //   21: ldc 2
/*     */     //   23: invokevirtual 312	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */     //   26: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   29: ldc_w 314
/*     */     //   32: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   35: ldc_w 316
/*     */     //   38: invokevirtual 312	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */     //   41: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   44: invokevirtual 164	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   47: invokespecial 291	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   50: athrow
/*     */     //   51: getstatic 252	io/netty/util/HashedWheelTimer:WORKER_STATE_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   54: aload_0
/*     */     //   55: iconst_1
/*     */     //   56: iconst_2
/*     */     //   57: invokevirtual 284	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:compareAndSet	(Ljava/lang/Object;II)Z
/*     */     //   60: ifne +62 -> 122
/*     */     //   63: getstatic 252	io/netty/util/HashedWheelTimer:WORKER_STATE_UPDATER	Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */     //   66: aload_0
/*     */     //   67: iconst_2
/*     */     //   68: invokevirtual 258	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:getAndSet	(Ljava/lang/Object;I)I
/*     */     //   71: iconst_2
/*     */     //   72: if_icmpeq +46 -> 118
/*     */     //   75: getstatic 228	io/netty/util/HashedWheelTimer:INSTANCE_COUNTER	Ljava/util/concurrent/atomic/AtomicInteger;
/*     */     //   78: invokevirtual 261	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
/*     */     //   81: pop
/*     */     //   82: aload_0
/*     */     //   83: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   86: ifnull +32 -> 118
/*     */     //   89: aload_0
/*     */     //   90: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   93: aload_0
/*     */     //   94: invokeinterface 320 2 0
/*     */     //   99: istore_1
/*     */     //   100: getstatic 322	io/netty/util/HashedWheelTimer:$assertionsDisabled	Z
/*     */     //   103: ifne +15 -> 118
/*     */     //   106: iload_1
/*     */     //   107: ifne +11 -> 118
/*     */     //   110: new 324	java/lang/AssertionError
/*     */     //   113: dup
/*     */     //   114: invokespecial 325	java/lang/AssertionError:<init>	()V
/*     */     //   117: athrow
/*     */     //   118: invokestatic 330	java/util/Collections:emptySet	()Ljava/util/Set;
/*     */     //   121: areturn
/*     */     //   122: iconst_0
/*     */     //   123: istore_1
/*     */     //   124: aload_0
/*     */     //   125: getfield 206	io/netty/util/HashedWheelTimer:workerThread	Ljava/lang/Thread;
/*     */     //   128: invokevirtual 333	java/lang/Thread:isAlive	()Z
/*     */     //   131: ifeq +29 -> 160
/*     */     //   134: aload_0
/*     */     //   135: getfield 206	io/netty/util/HashedWheelTimer:workerThread	Ljava/lang/Thread;
/*     */     //   138: invokevirtual 336	java/lang/Thread:interrupt	()V
/*     */     //   141: aload_0
/*     */     //   142: getfield 206	io/netty/util/HashedWheelTimer:workerThread	Ljava/lang/Thread;
/*     */     //   145: ldc2_w 89
/*     */     //   148: invokevirtual 339	java/lang/Thread:join	(J)V
/*     */     //   151: goto -27 -> 124
/*     */     //   154: astore_2
/*     */     //   155: iconst_1
/*     */     //   156: istore_1
/*     */     //   157: goto -33 -> 124
/*     */     //   160: iload_1
/*     */     //   161: ifeq +9 -> 170
/*     */     //   164: invokestatic 307	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   167: invokevirtual 336	java/lang/Thread:interrupt	()V
/*     */     //   170: getstatic 228	io/netty/util/HashedWheelTimer:INSTANCE_COUNTER	Ljava/util/concurrent/atomic/AtomicInteger;
/*     */     //   173: invokevirtual 261	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
/*     */     //   176: pop
/*     */     //   177: aload_0
/*     */     //   178: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   181: ifnull +83 -> 264
/*     */     //   184: aload_0
/*     */     //   185: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   188: aload_0
/*     */     //   189: invokeinterface 320 2 0
/*     */     //   194: istore_1
/*     */     //   195: getstatic 322	io/netty/util/HashedWheelTimer:$assertionsDisabled	Z
/*     */     //   198: ifne +15 -> 213
/*     */     //   201: iload_1
/*     */     //   202: ifne +11 -> 213
/*     */     //   205: new 324	java/lang/AssertionError
/*     */     //   208: dup
/*     */     //   209: invokespecial 325	java/lang/AssertionError:<init>	()V
/*     */     //   212: athrow
/*     */     //   213: goto +51 -> 264
/*     */     //   216: astore_3
/*     */     //   217: getstatic 228	io/netty/util/HashedWheelTimer:INSTANCE_COUNTER	Ljava/util/concurrent/atomic/AtomicInteger;
/*     */     //   220: invokevirtual 261	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
/*     */     //   223: pop
/*     */     //   224: aload_0
/*     */     //   225: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   228: ifnull +34 -> 262
/*     */     //   231: aload_0
/*     */     //   232: getfield 224	io/netty/util/HashedWheelTimer:leak	Lio/netty/util/ResourceLeakTracker;
/*     */     //   235: aload_0
/*     */     //   236: invokeinterface 320 2 0
/*     */     //   241: istore 4
/*     */     //   243: getstatic 322	io/netty/util/HashedWheelTimer:$assertionsDisabled	Z
/*     */     //   246: ifne +16 -> 262
/*     */     //   249: iload 4
/*     */     //   251: ifne +11 -> 262
/*     */     //   254: new 324	java/lang/AssertionError
/*     */     //   257: dup
/*     */     //   258: invokespecial 325	java/lang/AssertionError:<init>	()V
/*     */     //   261: athrow
/*     */     //   262: aload_3
/*     */     //   263: athrow
/*     */     //   264: aload_0
/*     */     //   265: getfield 113	io/netty/util/HashedWheelTimer:worker	Lio/netty/util/HashedWheelTimer$Worker;
/*     */     //   268: invokevirtual 342	io/netty/util/HashedWheelTimer$Worker:unprocessedTimeouts	()Ljava/util/Set;
/*     */     //   271: areturn
/*     */     // Line number table:
/*     */     //   Java source line #355	-> byte code offset #0
/*     */     //   Java source line #356	-> byte code offset #10
/*     */     //   Java source line #357	-> byte code offset #23
/*     */     //   Java source line #359	-> byte code offset #38
/*     */     //   Java source line #362	-> byte code offset #51
/*     */     //   Java source line #364	-> byte code offset #63
/*     */     //   Java source line #365	-> byte code offset #75
/*     */     //   Java source line #366	-> byte code offset #82
/*     */     //   Java source line #367	-> byte code offset #89
/*     */     //   Java source line #368	-> byte code offset #100
/*     */     //   Java source line #372	-> byte code offset #118
/*     */     //   Java source line #376	-> byte code offset #122
/*     */     //   Java source line #377	-> byte code offset #124
/*     */     //   Java source line #378	-> byte code offset #134
/*     */     //   Java source line #380	-> byte code offset #141
/*     */     //   Java source line #383	-> byte code offset #151
/*     */     //   Java source line #381	-> byte code offset #154
/*     */     //   Java source line #382	-> byte code offset #155
/*     */     //   Java source line #383	-> byte code offset #157
/*     */     //   Java source line #386	-> byte code offset #160
/*     */     //   Java source line #387	-> byte code offset #164
/*     */     //   Java source line #390	-> byte code offset #170
/*     */     //   Java source line #391	-> byte code offset #177
/*     */     //   Java source line #392	-> byte code offset #184
/*     */     //   Java source line #393	-> byte code offset #195
/*     */     //   Java source line #394	-> byte code offset #213
/*     */     //   Java source line #390	-> byte code offset #216
/*     */     //   Java source line #391	-> byte code offset #224
/*     */     //   Java source line #392	-> byte code offset #231
/*     */     //   Java source line #393	-> byte code offset #243
/*     */     //   Java source line #395	-> byte code offset #262
/*     */     //   Java source line #396	-> byte code offset #264
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	272	0	this	HashedWheelTimer
/*     */     //   99	8	1	closed	boolean
/*     */     //   123	38	1	interrupted	boolean
/*     */     //   194	8	1	closed	boolean
/*     */     //   154	2	2	ignored	InterruptedException
/*     */     //   216	47	3	localObject	Object
/*     */     //   241	9	4	closed	boolean
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   141	151	154	java/lang/InterruptedException
/*     */     //   122	170	216	finally
/*     */   }
/*     */   
/*     */   public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit)
/*     */   {
/* 401 */     if (task == null) {
/* 402 */       throw new NullPointerException("task");
/*     */     }
/* 404 */     if (unit == null) {
/* 405 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 408 */     long pendingTimeoutsCount = this.pendingTimeouts.incrementAndGet();
/*     */     
/* 410 */     if ((this.maxPendingTimeouts > 0L) && (pendingTimeoutsCount > this.maxPendingTimeouts)) {
/* 411 */       this.pendingTimeouts.decrementAndGet();
/* 412 */       throw new RejectedExecutionException("Number of pending timeouts (" + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending timeouts (" + this.maxPendingTimeouts + ")");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 417 */     start();
/*     */     
/*     */ 
/*     */ 
/* 421 */     long deadline = System.nanoTime() + unit.toNanos(delay) - this.startTime;
/*     */     
/*     */ 
/* 424 */     if ((delay > 0L) && (deadline < 0L)) {
/* 425 */       deadline = Long.MAX_VALUE;
/*     */     }
/* 427 */     HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
/* 428 */     this.timeouts.add(timeout);
/* 429 */     return timeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long pendingTimeouts()
/*     */   {
/* 436 */     return this.pendingTimeouts.get();
/*     */   }
/*     */   
/*     */   private static void reportTooManyInstances() {
/* 440 */     if (logger.isErrorEnabled()) {
/* 441 */       String resourceType = StringUtil.simpleClassName(HashedWheelTimer.class);
/* 442 */       logger.error("You are creating too many " + resourceType + " instances. " + resourceType + " is a shared resource that must be reused across the JVM,so that only a few instances are created.");
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Worker
/*     */     implements Runnable
/*     */   {
/* 449 */     private final Set<Timeout> unprocessedTimeouts = new HashSet();
/*     */     private long tick;
/*     */     
/*     */     private Worker() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 456 */       HashedWheelTimer.this.startTime = System.nanoTime();
/* 457 */       if (HashedWheelTimer.this.startTime == 0L)
/*     */       {
/* 459 */         HashedWheelTimer.this.startTime = 1L;
/*     */       }
/*     */       
/*     */ 
/* 463 */       HashedWheelTimer.this.startTimeInitialized.countDown();
/*     */       long deadline;
/*     */       int idx;
/* 466 */       do { deadline = waitForNextTick();
/* 467 */         if (deadline > 0L) {
/* 468 */           idx = (int)(this.tick & HashedWheelTimer.this.mask);
/* 469 */           processCancelledTasks();
/*     */           
/* 471 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
/* 472 */           transferTimeoutsToBuckets();
/* 473 */           bucket.expireTimeouts(deadline);
/* 474 */           this.tick += 1L;
/*     */         }
/* 476 */       } while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
/*     */       
/*     */ 
/* 479 */       for (HashedWheelTimer.HashedWheelBucket bucket : HashedWheelTimer.this.wheel) {
/* 480 */         bucket.clearTimeouts(this.unprocessedTimeouts);
/*     */       }
/*     */       for (;;) {
/* 483 */         HashedWheelTimer.HashedWheelTimeout timeout = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
/* 484 */         if (timeout == null) {
/*     */           break;
/*     */         }
/* 487 */         if (!timeout.isCancelled()) {
/* 488 */           this.unprocessedTimeouts.add(timeout);
/*     */         }
/*     */       }
/* 491 */       processCancelledTasks();
/*     */     }
/*     */     
/*     */ 
/*     */     private void transferTimeoutsToBuckets()
/*     */     {
/* 497 */       for (int i = 0; i < 100000; i++) {
/* 498 */         HashedWheelTimer.HashedWheelTimeout timeout = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
/* 499 */         if (timeout == null) {
/*     */           break;
/*     */         }
/*     */         
/* 503 */         if (timeout.state() != 1)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 508 */           long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
/* 509 */           timeout.remainingRounds = ((calculated - this.tick) / HashedWheelTimer.this.wheel.length);
/*     */           
/* 511 */           long ticks = Math.max(calculated, this.tick);
/* 512 */           int stopIndex = (int)(ticks & HashedWheelTimer.this.mask);
/*     */           
/* 514 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
/* 515 */           bucket.addTimeout(timeout);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void processCancelledTasks() {
/* 521 */       for (;;) { HashedWheelTimer.HashedWheelTimeout timeout = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.cancelledTimeouts.poll();
/* 522 */         if (timeout == null) {
/*     */           break;
/*     */         }
/*     */         try
/*     */         {
/* 527 */           timeout.remove();
/*     */         } catch (Throwable t) {
/* 529 */           if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 530 */             HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private long waitForNextTick()
/*     */     {
/* 543 */       long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1L);
/*     */       for (;;)
/*     */       {
/* 546 */         long currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
/* 547 */         long sleepTimeMs = (deadline - currentTime + 999999L) / 1000000L;
/*     */         
/* 549 */         if (sleepTimeMs <= 0L) {
/* 550 */           if (currentTime == Long.MIN_VALUE) {
/* 551 */             return -9223372036854775807L;
/*     */           }
/* 553 */           return currentTime;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 562 */         if (PlatformDependent.isWindows()) {
/* 563 */           sleepTimeMs = sleepTimeMs / 10L * 10L;
/*     */         }
/*     */         try
/*     */         {
/* 567 */           Thread.sleep(sleepTimeMs);
/*     */         } catch (InterruptedException ignored) {
/* 569 */           if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
/* 570 */             return Long.MIN_VALUE;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public Set<Timeout> unprocessedTimeouts() {
/* 577 */       return Collections.unmodifiableSet(this.unprocessedTimeouts);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashedWheelTimeout
/*     */     implements Timeout
/*     */   {
/*     */     private static final int ST_INIT = 0;
/*     */     private static final int ST_CANCELLED = 1;
/*     */     private static final int ST_EXPIRED = 2;
/* 587 */     private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
/*     */     
/*     */     private final HashedWheelTimer timer;
/*     */     
/*     */     private final TimerTask task;
/*     */     private final long deadline;
/* 593 */     private volatile int state = 0;
/*     */     
/*     */ 
/*     */     long remainingRounds;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout next;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout prev;
/*     */     
/*     */     HashedWheelTimer.HashedWheelBucket bucket;
/*     */     
/*     */ 
/*     */     HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline)
/*     */     {
/* 609 */       this.timer = timer;
/* 610 */       this.task = task;
/* 611 */       this.deadline = deadline;
/*     */     }
/*     */     
/*     */     public Timer timer()
/*     */     {
/* 616 */       return this.timer;
/*     */     }
/*     */     
/*     */     public TimerTask task()
/*     */     {
/* 621 */       return this.task;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean cancel()
/*     */     {
/* 627 */       if (!compareAndSetState(0, 1)) {
/* 628 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 633 */       this.timer.cancelledTimeouts.add(this);
/* 634 */       return true;
/*     */     }
/*     */     
/*     */     void remove() {
/* 638 */       HashedWheelTimer.HashedWheelBucket bucket = this.bucket;
/* 639 */       if (bucket != null) {
/* 640 */         bucket.remove(this);
/*     */       } else {
/* 642 */         this.timer.pendingTimeouts.decrementAndGet();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean compareAndSetState(int expected, int state) {
/* 647 */       return STATE_UPDATER.compareAndSet(this, expected, state);
/*     */     }
/*     */     
/*     */     public int state() {
/* 651 */       return this.state;
/*     */     }
/*     */     
/*     */     public boolean isCancelled()
/*     */     {
/* 656 */       return state() == 1;
/*     */     }
/*     */     
/*     */     public boolean isExpired()
/*     */     {
/* 661 */       return state() == 2;
/*     */     }
/*     */     
/*     */     public void expire() {
/* 665 */       if (!compareAndSetState(0, 2)) {
/* 666 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 670 */         this.task.run(this);
/*     */       } catch (Throwable t) {
/* 672 */         if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 673 */           HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 680 */       long currentTime = System.nanoTime();
/* 681 */       long remaining = this.deadline - currentTime + this.timer.startTime;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 686 */       StringBuilder buf = new StringBuilder(192).append(StringUtil.simpleClassName(this)).append('(').append("deadline: ");
/* 687 */       if (remaining > 0L)
/*     */       {
/* 689 */         buf.append(remaining).append(" ns later");
/* 690 */       } else if (remaining < 0L)
/*     */       {
/* 692 */         buf.append(-remaining).append(" ns ago");
/*     */       } else {
/* 694 */         buf.append("now");
/*     */       }
/*     */       
/* 697 */       if (isCancelled()) {
/* 698 */         buf.append(", cancelled");
/*     */       }
/*     */       
/* 701 */       return 
/*     */       
/* 703 */         ", task: " + task() + ')';
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class HashedWheelBucket
/*     */   {
/*     */     private HashedWheelTimer.HashedWheelTimeout head;
/*     */     
/*     */ 
/*     */ 
/*     */     private HashedWheelTimer.HashedWheelTimeout tail;
/*     */     
/*     */ 
/*     */ 
/*     */     public void addTimeout(HashedWheelTimer.HashedWheelTimeout timeout)
/*     */     {
/* 722 */       assert (timeout.bucket == null);
/* 723 */       timeout.bucket = this;
/* 724 */       if (this.head == null) {
/* 725 */         this.head = (this.tail = timeout);
/*     */       } else {
/* 727 */         this.tail.next = timeout;
/* 728 */         timeout.prev = this.tail;
/* 729 */         this.tail = timeout;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void expireTimeouts(long deadline)
/*     */     {
/* 737 */       HashedWheelTimer.HashedWheelTimeout timeout = this.head;
/*     */       
/*     */ 
/* 740 */       while (timeout != null) {
/* 741 */         HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/* 742 */         if (timeout.remainingRounds <= 0L) {
/* 743 */           next = remove(timeout);
/* 744 */           if (HashedWheelTimer.HashedWheelTimeout.access$800(timeout) <= deadline) {
/* 745 */             timeout.expire();
/*     */           }
/*     */           else {
/* 748 */             throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", new Object[] {
/* 749 */               Long.valueOf(HashedWheelTimer.HashedWheelTimeout.access$800(timeout)), Long.valueOf(deadline) }));
/*     */           }
/* 751 */         } else if (timeout.isCancelled()) {
/* 752 */           next = remove(timeout);
/*     */         } else {
/* 754 */           timeout.remainingRounds -= 1L;
/*     */         }
/* 756 */         timeout = next;
/*     */       }
/*     */     }
/*     */     
/*     */     public HashedWheelTimer.HashedWheelTimeout remove(HashedWheelTimer.HashedWheelTimeout timeout) {
/* 761 */       HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/*     */       
/* 763 */       if (timeout.prev != null) {
/* 764 */         timeout.prev.next = next;
/*     */       }
/* 766 */       if (timeout.next != null) {
/* 767 */         timeout.next.prev = timeout.prev;
/*     */       }
/*     */       
/* 770 */       if (timeout == this.head)
/*     */       {
/* 772 */         if (timeout == this.tail) {
/* 773 */           this.tail = null;
/* 774 */           this.head = null;
/*     */         } else {
/* 776 */           this.head = next;
/*     */         }
/* 778 */       } else if (timeout == this.tail)
/*     */       {
/* 780 */         this.tail = timeout.prev;
/*     */       }
/*     */       
/* 783 */       timeout.prev = null;
/* 784 */       timeout.next = null;
/* 785 */       timeout.bucket = null;
/* 786 */       HashedWheelTimer.HashedWheelTimeout.access$1200(timeout).pendingTimeouts.decrementAndGet();
/* 787 */       return next;
/*     */     }
/*     */     
/*     */ 
/*     */     public void clearTimeouts(Set<Timeout> set)
/*     */     {
/*     */       for (;;)
/*     */       {
/* 795 */         HashedWheelTimer.HashedWheelTimeout timeout = pollTimeout();
/* 796 */         if (timeout == null) {
/* 797 */           return;
/*     */         }
/* 799 */         if ((!timeout.isExpired()) && (!timeout.isCancelled()))
/*     */         {
/*     */ 
/* 802 */           set.add(timeout); }
/*     */       }
/*     */     }
/*     */     
/*     */     private HashedWheelTimer.HashedWheelTimeout pollTimeout() {
/* 807 */       HashedWheelTimer.HashedWheelTimeout head = this.head;
/* 808 */       if (head == null) {
/* 809 */         return null;
/*     */       }
/* 811 */       HashedWheelTimer.HashedWheelTimeout next = head.next;
/* 812 */       if (next == null) {
/* 813 */         this.tail = (this.head = null);
/*     */       } else {
/* 815 */         this.head = next;
/* 816 */         next.prev = null;
/*     */       }
/*     */       
/*     */ 
/* 820 */       head.next = null;
/* 821 */       head.prev = null;
/* 822 */       head.bucket = null;
/* 823 */       return head;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\HashedWheelTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */