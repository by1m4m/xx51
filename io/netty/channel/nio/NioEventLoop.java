/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopException;
/*     */ import io.netty.channel.SelectStrategy;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.util.IntSupplier;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ReflectionUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NioEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*  57 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
/*     */   
/*     */ 
/*     */   private static final int CLEANUP_INTERVAL = 256;
/*     */   
/*  62 */   private static final boolean DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
/*     */   
/*     */   private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
/*     */   
/*     */   private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
/*  67 */   private final IntSupplier selectNowSupplier = new IntSupplier()
/*     */   {
/*     */     public int get() throws Exception {
/*  70 */       return NioEventLoop.this.selectNow();
/*     */     }
/*     */   };
/*     */   private Selector selector;
/*     */   private Selector unwrappedSelector;
/*     */   private SelectedSelectionKeySet selectedKeys;
/*     */   private final SelectorProvider provider;
/*     */   
/*     */   static
/*     */   {
/*  80 */     String key = "sun.nio.ch.bugLevel";
/*  81 */     String buglevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
/*  82 */     if (buglevel == null) {
/*     */       try {
/*  84 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/*  87 */             System.setProperty("sun.nio.ch.bugLevel", "");
/*  88 */             return null;
/*     */           }
/*     */         });
/*     */       } catch (SecurityException e) {
/*  92 */         logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", e);
/*     */       }
/*     */     }
/*     */     
/*  96 */     int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
/*  97 */     if (selectorAutoRebuildThreshold < 3) {
/*  98 */       selectorAutoRebuildThreshold = 0;
/*     */     }
/*     */     
/* 101 */     SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
/*     */     
/* 103 */     if (logger.isDebugEnabled()) {
/* 104 */       logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEYSET_OPTIMIZATION));
/* 105 */       logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
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
/* 124 */   private final AtomicBoolean wakenUp = new AtomicBoolean();
/*     */   
/*     */   private final SelectStrategy selectStrategy;
/*     */   
/* 128 */   private volatile int ioRatio = 50;
/*     */   private int cancelledKeys;
/*     */   private boolean needsToSelectAgain;
/*     */   
/*     */   NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler)
/*     */   {
/* 134 */     super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
/* 135 */     if (selectorProvider == null) {
/* 136 */       throw new NullPointerException("selectorProvider");
/*     */     }
/* 138 */     if (strategy == null) {
/* 139 */       throw new NullPointerException("selectStrategy");
/*     */     }
/* 141 */     this.provider = selectorProvider;
/* 142 */     SelectorTuple selectorTuple = openSelector();
/* 143 */     this.selector = selectorTuple.selector;
/* 144 */     this.unwrappedSelector = selectorTuple.unwrappedSelector;
/* 145 */     this.selectStrategy = strategy;
/*     */   }
/*     */   
/*     */   private static final class SelectorTuple {
/*     */     final Selector unwrappedSelector;
/*     */     final Selector selector;
/*     */     
/*     */     SelectorTuple(Selector unwrappedSelector) {
/* 153 */       this.unwrappedSelector = unwrappedSelector;
/* 154 */       this.selector = unwrappedSelector;
/*     */     }
/*     */     
/*     */     SelectorTuple(Selector unwrappedSelector, Selector selector) {
/* 158 */       this.unwrappedSelector = unwrappedSelector;
/* 159 */       this.selector = selector;
/*     */     }
/*     */   }
/*     */   
/*     */   private SelectorTuple openSelector()
/*     */   {
/*     */     try {
/* 166 */       unwrappedSelector = this.provider.openSelector();
/*     */     } catch (IOException e) { Selector unwrappedSelector;
/* 168 */       throw new ChannelException("failed to open a new selector", e);
/*     */     }
/*     */     final Selector unwrappedSelector;
/* 171 */     if (DISABLE_KEYSET_OPTIMIZATION) {
/* 172 */       return new SelectorTuple(unwrappedSelector);
/*     */     }
/*     */     
/* 175 */     Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 179 */           return Class.forName("sun.nio.ch.SelectorImpl", false, 
/*     */           
/*     */ 
/* 182 */             PlatformDependent.getSystemClassLoader());
/*     */         } catch (Throwable cause) {
/* 184 */           return cause;
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 189 */     if ((!(maybeSelectorImplClass instanceof Class)) || 
/*     */     
/* 191 */       (!((Class)maybeSelectorImplClass).isAssignableFrom(unwrappedSelector.getClass()))) {
/* 192 */       if ((maybeSelectorImplClass instanceof Throwable)) {
/* 193 */         Throwable t = (Throwable)maybeSelectorImplClass;
/* 194 */         logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, t);
/*     */       }
/* 196 */       return new SelectorTuple(unwrappedSelector);
/*     */     }
/*     */     
/* 199 */     final Class<?> selectorImplClass = (Class)maybeSelectorImplClass;
/* 200 */     final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
/*     */     
/* 202 */     Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*     */         try {
/* 206 */           Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
/* 207 */           Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
/*     */           
/* 209 */           if ((PlatformDependent.javaVersion() >= 9) && (PlatformDependent.hasUnsafe()))
/*     */           {
/*     */ 
/* 212 */             long selectedKeysFieldOffset = PlatformDependent.objectFieldOffset(selectedKeysField);
/*     */             
/* 214 */             long publicSelectedKeysFieldOffset = PlatformDependent.objectFieldOffset(publicSelectedKeysField);
/*     */             
/* 216 */             if ((selectedKeysFieldOffset != -1L) && (publicSelectedKeysFieldOffset != -1L)) {
/* 217 */               PlatformDependent.putObject(unwrappedSelector, selectedKeysFieldOffset, selectedKeySet);
/*     */               
/* 219 */               PlatformDependent.putObject(unwrappedSelector, publicSelectedKeysFieldOffset, selectedKeySet);
/*     */               
/* 221 */               return null;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 226 */           Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
/* 227 */           if (cause != null) {
/* 228 */             return cause;
/*     */           }
/* 230 */           cause = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
/* 231 */           if (cause != null) {
/* 232 */             return cause;
/*     */           }
/*     */           
/* 235 */           selectedKeysField.set(unwrappedSelector, selectedKeySet);
/* 236 */           publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
/* 237 */           return null;
/*     */         } catch (NoSuchFieldException e) {
/* 239 */           return e;
/*     */         } catch (IllegalAccessException e) {
/* 241 */           return e;
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 246 */     if ((maybeException instanceof Exception)) {
/* 247 */       this.selectedKeys = null;
/* 248 */       Exception e = (Exception)maybeException;
/* 249 */       logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, e);
/* 250 */       return new SelectorTuple(unwrappedSelector);
/*     */     }
/* 252 */     this.selectedKeys = selectedKeySet;
/* 253 */     logger.trace("instrumented a special java.util.Set into: {}", unwrappedSelector);
/* 254 */     return new SelectorTuple(unwrappedSelector, new SelectedSelectionKeySetSelector(unwrappedSelector, selectedKeySet));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelectorProvider selectorProvider()
/*     */   {
/* 262 */     return this.provider;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks)
/*     */   {
/* 268 */     return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : 
/* 269 */       PlatformDependent.newMpscQueue(maxPendingTasks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(SelectableChannel ch, int interestOps, NioTask<?> task)
/*     */   {
/* 278 */     if (ch == null) {
/* 279 */       throw new NullPointerException("ch");
/*     */     }
/* 281 */     if (interestOps == 0) {
/* 282 */       throw new IllegalArgumentException("interestOps must be non-zero.");
/*     */     }
/* 284 */     if ((interestOps & (ch.validOps() ^ 0xFFFFFFFF)) != 0)
/*     */     {
/* 286 */       throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')');
/*     */     }
/* 288 */     if (task == null) {
/* 289 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 292 */     if (isShutdown()) {
/* 293 */       throw new IllegalStateException("event loop shut down");
/*     */     }
/*     */     try
/*     */     {
/* 297 */       ch.register(this.selector, interestOps, task);
/*     */     } catch (Exception e) {
/* 299 */       throw new EventLoopException("failed to register a channel", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIoRatio()
/*     */   {
/* 307 */     return this.ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 315 */     if ((ioRatio <= 0) || (ioRatio > 100)) {
/* 316 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 318 */     this.ioRatio = ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void rebuildSelector()
/*     */   {
/* 326 */     if (!inEventLoop()) {
/* 327 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 330 */           NioEventLoop.this.rebuildSelector0();
/*     */         }
/* 332 */       });
/* 333 */       return;
/*     */     }
/* 335 */     rebuildSelector0();
/*     */   }
/*     */   
/*     */   private void rebuildSelector0() {
/* 339 */     Selector oldSelector = this.selector;
/*     */     
/*     */ 
/* 342 */     if (oldSelector == null) {
/* 343 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 347 */       newSelectorTuple = openSelector();
/*     */     } catch (Exception e) { SelectorTuple newSelectorTuple;
/* 349 */       logger.warn("Failed to create a new Selector.", e); return;
/*     */     }
/*     */     
/*     */     SelectorTuple newSelectorTuple;
/*     */     
/* 354 */     int nChannels = 0;
/* 355 */     for (SelectionKey key : oldSelector.keys()) {
/* 356 */       Object a = key.attachment();
/*     */       try {
/* 358 */         if ((!key.isValid()) || (key.channel().keyFor(newSelectorTuple.unwrappedSelector) == null))
/*     */         {
/*     */ 
/*     */ 
/* 362 */           int interestOps = key.interestOps();
/* 363 */           key.cancel();
/* 364 */           SelectionKey newKey = key.channel().register(newSelectorTuple.unwrappedSelector, interestOps, a);
/* 365 */           if ((a instanceof AbstractNioChannel))
/*     */           {
/* 367 */             ((AbstractNioChannel)a).selectionKey = newKey;
/*     */           }
/* 369 */           nChannels++;
/*     */         }
/* 371 */       } catch (Exception e) { logger.warn("Failed to re-register a Channel to the new Selector.", e);
/* 372 */         if ((a instanceof AbstractNioChannel)) {
/* 373 */           AbstractNioChannel ch = (AbstractNioChannel)a;
/* 374 */           ch.unsafe().close(ch.unsafe().voidPromise());
/*     */         }
/*     */         else {
/* 377 */           NioTask<SelectableChannel> task = (NioTask)a;
/* 378 */           invokeChannelUnregistered(task, key, e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 383 */     this.selector = newSelectorTuple.selector;
/* 384 */     this.unwrappedSelector = newSelectorTuple.unwrappedSelector;
/*     */     
/*     */     try
/*     */     {
/* 388 */       oldSelector.close();
/*     */     } catch (Throwable t) {
/* 390 */       if (logger.isWarnEnabled()) {
/* 391 */         logger.warn("Failed to close the old Selector.", t);
/*     */       }
/*     */     }
/*     */     
/* 395 */     if (logger.isInfoEnabled()) {
/* 396 */       logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
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
/*     */   private static void handleLoopException(Throwable t)
/*     */   {
/* 483 */     logger.warn("Unexpected exception in the selector loop.", t);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 488 */       Thread.sleep(1000L);
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */   private void processSelectedKeys()
/*     */   {
/* 495 */     if (this.selectedKeys != null) {
/* 496 */       processSelectedKeysOptimized();
/*     */     } else {
/* 498 */       processSelectedKeysPlain(this.selector.selectedKeys());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void cleanup()
/*     */   {
/*     */     try {
/* 505 */       this.selector.close();
/*     */     } catch (IOException e) {
/* 507 */       logger.warn("Failed to close a selector.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   void cancel(SelectionKey key) {
/* 512 */     key.cancel();
/* 513 */     this.cancelledKeys += 1;
/* 514 */     if (this.cancelledKeys >= 256) {
/* 515 */       this.cancelledKeys = 0;
/* 516 */       this.needsToSelectAgain = true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected Runnable pollTask()
/*     */   {
/* 522 */     Runnable task = super.pollTask();
/* 523 */     if (this.needsToSelectAgain) {
/* 524 */       selectAgain();
/*     */     }
/* 526 */     return task;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys)
/*     */   {
/* 533 */     if (selectedKeys.isEmpty()) {
/* 534 */       return;
/*     */     }
/*     */     
/* 537 */     Iterator<SelectionKey> i = selectedKeys.iterator();
/*     */     for (;;) {
/* 539 */       SelectionKey k = (SelectionKey)i.next();
/* 540 */       Object a = k.attachment();
/* 541 */       i.remove();
/*     */       
/* 543 */       if ((a instanceof AbstractNioChannel)) {
/* 544 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       }
/*     */       else {
/* 547 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 548 */         processSelectedKey(k, task);
/*     */       }
/*     */       
/* 551 */       if (!i.hasNext()) {
/*     */         break;
/*     */       }
/*     */       
/* 555 */       if (this.needsToSelectAgain) {
/* 556 */         selectAgain();
/* 557 */         selectedKeys = this.selector.selectedKeys();
/*     */         
/*     */ 
/* 560 */         if (selectedKeys.isEmpty()) {
/*     */           break;
/*     */         }
/* 563 */         i = selectedKeys.iterator();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processSelectedKeysOptimized()
/*     */   {
/* 570 */     for (int i = 0; i < this.selectedKeys.size; i++) {
/* 571 */       SelectionKey k = this.selectedKeys.keys[i];
/*     */       
/*     */ 
/* 574 */       this.selectedKeys.keys[i] = null;
/*     */       
/* 576 */       Object a = k.attachment();
/*     */       
/* 578 */       if ((a instanceof AbstractNioChannel)) {
/* 579 */         processSelectedKey(k, (AbstractNioChannel)a);
/*     */       }
/*     */       else {
/* 582 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 583 */         processSelectedKey(k, task);
/*     */       }
/*     */       
/* 586 */       if (this.needsToSelectAgain)
/*     */       {
/*     */ 
/* 589 */         this.selectedKeys.reset(i + 1);
/*     */         
/* 591 */         selectAgain();
/* 592 */         i = -1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
/* 598 */     AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
/* 599 */     if (!k.isValid())
/*     */     {
/*     */       try {
/* 602 */         eventLoop = ch.eventLoop();
/*     */       }
/*     */       catch (Throwable ignored)
/*     */       {
/*     */         EventLoop eventLoop;
/*     */         
/*     */         return;
/*     */       }
/*     */       
/*     */       EventLoop eventLoop;
/*     */       
/* 613 */       if ((eventLoop != this) || (eventLoop == null)) {
/* 614 */         return;
/*     */       }
/*     */       
/* 617 */       unsafe.close(unsafe.voidPromise());
/* 618 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 622 */       int readyOps = k.readyOps();
/*     */       
/*     */ 
/* 625 */       if ((readyOps & 0x8) != 0)
/*     */       {
/*     */ 
/* 628 */         int ops = k.interestOps();
/* 629 */         ops &= 0xFFFFFFF7;
/* 630 */         k.interestOps(ops);
/*     */         
/* 632 */         unsafe.finishConnect();
/*     */       }
/*     */       
/*     */ 
/* 636 */       if ((readyOps & 0x4) != 0)
/*     */       {
/* 638 */         ch.unsafe().forceFlush();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 643 */       if (((readyOps & 0x11) != 0) || (readyOps == 0)) {
/* 644 */         unsafe.read();
/*     */       }
/*     */     } catch (CancelledKeyException ignored) {
/* 647 */       unsafe.close(unsafe.voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
/* 652 */     int state = 0;
/*     */     try {
/* 654 */       task.channelReady(k.channel(), k);
/* 655 */       state = 1;
/*     */     } catch (Exception e) {
/* 657 */       k.cancel();
/* 658 */       invokeChannelUnregistered(task, k, e);
/* 659 */       state = 2;
/*     */     } finally {
/* 661 */       switch (state) {
/*     */       case 0: 
/* 663 */         k.cancel();
/* 664 */         invokeChannelUnregistered(task, k, null);
/* 665 */         break;
/*     */       case 1: 
/* 667 */         if (!k.isValid()) {
/* 668 */           invokeChannelUnregistered(task, k, null);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeAll() {
/* 676 */     selectAgain();
/* 677 */     Set<SelectionKey> keys = this.selector.keys();
/* 678 */     Collection<AbstractNioChannel> channels = new ArrayList(keys.size());
/* 679 */     for (SelectionKey k : keys) {
/* 680 */       Object a = k.attachment();
/* 681 */       if ((a instanceof AbstractNioChannel)) {
/* 682 */         channels.add((AbstractNioChannel)a);
/*     */       } else {
/* 684 */         k.cancel();
/*     */         
/* 686 */         NioTask<SelectableChannel> task = (NioTask)a;
/* 687 */         invokeChannelUnregistered(task, k, null);
/*     */       }
/*     */     }
/*     */     
/* 691 */     for (AbstractNioChannel ch : channels) {
/* 692 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
/*     */     try {
/* 698 */       task.channelUnregistered(k.channel(), cause);
/*     */     } catch (Exception e) {
/* 700 */       logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 706 */     if ((!inEventLoop) && (this.wakenUp.compareAndSet(false, true))) {
/* 707 */       this.selector.wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   Selector unwrappedSelector() {
/* 712 */     return this.unwrappedSelector;
/*     */   }
/*     */   
/*     */   int selectNow() throws IOException {
/*     */     try {
/* 717 */       return this.selector.selectNow();
/*     */     }
/*     */     finally {
/* 720 */       if (this.wakenUp.get()) {
/* 721 */         this.selector.wakeup();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void select(boolean oldWakenUp) throws IOException {
/* 727 */     Selector selector = this.selector;
/*     */     try {
/* 729 */       int selectCnt = 0;
/* 730 */       long currentTimeNanos = System.nanoTime();
/* 731 */       long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
/*     */       for (;;)
/*     */       {
/* 734 */         long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
/* 735 */         if (timeoutMillis <= 0L) {
/* 736 */           if (selectCnt != 0) break;
/* 737 */           selector.selectNow();
/* 738 */           selectCnt = 1; break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 747 */         if ((hasTasks()) && (this.wakenUp.compareAndSet(false, true))) {
/* 748 */           selector.selectNow();
/* 749 */           selectCnt = 1;
/* 750 */           break;
/*     */         }
/*     */         
/* 753 */         int selectedKeys = selector.select(timeoutMillis);
/* 754 */         selectCnt++;
/*     */         
/* 756 */         if ((selectedKeys != 0) || (oldWakenUp) || (this.wakenUp.get()) || (hasTasks()) || (hasScheduledTasks())) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 763 */         if (Thread.interrupted())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 769 */           if (logger.isDebugEnabled()) {
/* 770 */             logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
/*     */           }
/*     */           
/*     */ 
/* 774 */           selectCnt = 1;
/* 775 */           break;
/*     */         }
/*     */         
/* 778 */         long time = System.nanoTime();
/* 779 */         if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos)
/*     */         {
/* 781 */           selectCnt = 1;
/* 782 */         } else if ((SELECTOR_AUTO_REBUILD_THRESHOLD > 0) && (selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD))
/*     */         {
/*     */ 
/*     */ 
/* 786 */           logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", 
/*     */           
/* 788 */             Integer.valueOf(selectCnt), selector);
/*     */           
/* 790 */           rebuildSelector();
/* 791 */           selector = this.selector;
/*     */           
/*     */ 
/* 794 */           selector.selectNow();
/* 795 */           selectCnt = 1;
/* 796 */           break;
/*     */         }
/*     */         
/* 799 */         currentTimeNanos = time;
/*     */       }
/*     */       
/* 802 */       if ((selectCnt > 3) && 
/* 803 */         (logger.isDebugEnabled())) {
/* 804 */         logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.", 
/* 805 */           Integer.valueOf(selectCnt - 1), selector);
/*     */       }
/*     */     }
/*     */     catch (CancelledKeyException e) {
/* 809 */       if (logger.isDebugEnabled()) {
/* 810 */         logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?", selector, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void selectAgain()
/*     */   {
/* 818 */     this.needsToSelectAgain = false;
/*     */     try {
/* 820 */       this.selector.selectNow();
/*     */     } catch (Throwable t) {
/* 822 */       logger.warn("Failed to update SelectionKeys.", t);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 21	io/netty/channel/nio/NioEventLoop:selectStrategy	Lio/netty/channel/SelectStrategy;
/*     */     //   4: aload_0
/*     */     //   5: getfield 6	io/netty/channel/nio/NioEventLoop:selectNowSupplier	Lio/netty/util/IntSupplier;
/*     */     //   8: aload_0
/*     */     //   9: invokevirtual 111	io/netty/channel/nio/NioEventLoop:hasTasks	()Z
/*     */     //   12: invokeinterface 112 3 0
/*     */     //   17: lookupswitch	default:+60->77, -2:+27->44, -1:+30->47
/*     */     //   44: goto -44 -> 0
/*     */     //   47: aload_0
/*     */     //   48: aload_0
/*     */     //   49: getfield 9	io/netty/channel/nio/NioEventLoop:wakenUp	Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */     //   52: iconst_0
/*     */     //   53: invokevirtual 113	java/util/concurrent/atomic/AtomicBoolean:getAndSet	(Z)Z
/*     */     //   56: invokespecial 114	io/netty/channel/nio/NioEventLoop:select	(Z)V
/*     */     //   59: aload_0
/*     */     //   60: getfield 9	io/netty/channel/nio/NioEventLoop:wakenUp	Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */     //   63: invokevirtual 115	java/util/concurrent/atomic/AtomicBoolean:get	()Z
/*     */     //   66: ifeq +11 -> 77
/*     */     //   69: aload_0
/*     */     //   70: getfield 18	io/netty/channel/nio/NioEventLoop:selector	Ljava/nio/channels/Selector;
/*     */     //   73: invokevirtual 116	java/nio/channels/Selector:wakeup	()Ljava/nio/channels/Selector;
/*     */     //   76: pop
/*     */     //   77: aload_0
/*     */     //   78: iconst_0
/*     */     //   79: putfield 117	io/netty/channel/nio/NioEventLoop:cancelledKeys	I
/*     */     //   82: aload_0
/*     */     //   83: iconst_0
/*     */     //   84: putfield 118	io/netty/channel/nio/NioEventLoop:needsToSelectAgain	Z
/*     */     //   87: aload_0
/*     */     //   88: getfield 10	io/netty/channel/nio/NioEventLoop:ioRatio	I
/*     */     //   91: istore_1
/*     */     //   92: iload_1
/*     */     //   93: bipush 100
/*     */     //   95: if_icmpne +26 -> 121
/*     */     //   98: aload_0
/*     */     //   99: invokespecial 119	io/netty/channel/nio/NioEventLoop:processSelectedKeys	()V
/*     */     //   102: aload_0
/*     */     //   103: invokevirtual 120	io/netty/channel/nio/NioEventLoop:runAllTasks	()Z
/*     */     //   106: pop
/*     */     //   107: goto +11 -> 118
/*     */     //   110: astore_2
/*     */     //   111: aload_0
/*     */     //   112: invokevirtual 120	io/netty/channel/nio/NioEventLoop:runAllTasks	()Z
/*     */     //   115: pop
/*     */     //   116: aload_2
/*     */     //   117: athrow
/*     */     //   118: goto +65 -> 183
/*     */     //   121: invokestatic 121	java/lang/System:nanoTime	()J
/*     */     //   124: lstore_2
/*     */     //   125: aload_0
/*     */     //   126: invokespecial 119	io/netty/channel/nio/NioEventLoop:processSelectedKeys	()V
/*     */     //   129: invokestatic 121	java/lang/System:nanoTime	()J
/*     */     //   132: lload_2
/*     */     //   133: lsub
/*     */     //   134: lstore 4
/*     */     //   136: aload_0
/*     */     //   137: lload 4
/*     */     //   139: bipush 100
/*     */     //   141: iload_1
/*     */     //   142: isub
/*     */     //   143: i2l
/*     */     //   144: lmul
/*     */     //   145: iload_1
/*     */     //   146: i2l
/*     */     //   147: ldiv
/*     */     //   148: invokevirtual 122	io/netty/channel/nio/NioEventLoop:runAllTasks	(J)Z
/*     */     //   151: pop
/*     */     //   152: goto +31 -> 183
/*     */     //   155: astore 6
/*     */     //   157: invokestatic 121	java/lang/System:nanoTime	()J
/*     */     //   160: lload_2
/*     */     //   161: lsub
/*     */     //   162: lstore 7
/*     */     //   164: aload_0
/*     */     //   165: lload 7
/*     */     //   167: bipush 100
/*     */     //   169: iload_1
/*     */     //   170: isub
/*     */     //   171: i2l
/*     */     //   172: lmul
/*     */     //   173: iload_1
/*     */     //   174: i2l
/*     */     //   175: ldiv
/*     */     //   176: invokevirtual 122	io/netty/channel/nio/NioEventLoop:runAllTasks	(J)Z
/*     */     //   179: pop
/*     */     //   180: aload 6
/*     */     //   182: athrow
/*     */     //   183: goto +8 -> 191
/*     */     //   186: astore_1
/*     */     //   187: aload_1
/*     */     //   188: invokestatic 123	io/netty/channel/nio/NioEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   191: aload_0
/*     */     //   192: invokevirtual 124	io/netty/channel/nio/NioEventLoop:isShuttingDown	()Z
/*     */     //   195: ifeq +15 -> 210
/*     */     //   198: aload_0
/*     */     //   199: invokespecial 125	io/netty/channel/nio/NioEventLoop:closeAll	()V
/*     */     //   202: aload_0
/*     */     //   203: invokevirtual 126	io/netty/channel/nio/NioEventLoop:confirmShutdown	()Z
/*     */     //   206: ifeq +4 -> 210
/*     */     //   209: return
/*     */     //   210: goto -210 -> 0
/*     */     //   213: astore_1
/*     */     //   214: aload_1
/*     */     //   215: invokestatic 123	io/netty/channel/nio/NioEventLoop:handleLoopException	(Ljava/lang/Throwable;)V
/*     */     //   218: goto -218 -> 0
/*     */     // Line number table:
/*     */     //   Java source line #404	-> byte code offset #0
/*     */     //   Java source line #406	-> byte code offset #44
/*     */     //   Java source line #408	-> byte code offset #47
/*     */     //   Java source line #438	-> byte code offset #59
/*     */     //   Java source line #439	-> byte code offset #69
/*     */     //   Java source line #445	-> byte code offset #77
/*     */     //   Java source line #446	-> byte code offset #82
/*     */     //   Java source line #447	-> byte code offset #87
/*     */     //   Java source line #448	-> byte code offset #92
/*     */     //   Java source line #450	-> byte code offset #98
/*     */     //   Java source line #453	-> byte code offset #102
/*     */     //   Java source line #454	-> byte code offset #107
/*     */     //   Java source line #453	-> byte code offset #110
/*     */     //   Java source line #454	-> byte code offset #116
/*     */     //   Java source line #456	-> byte code offset #121
/*     */     //   Java source line #458	-> byte code offset #125
/*     */     //   Java source line #461	-> byte code offset #129
/*     */     //   Java source line #462	-> byte code offset #136
/*     */     //   Java source line #463	-> byte code offset #152
/*     */     //   Java source line #461	-> byte code offset #155
/*     */     //   Java source line #462	-> byte code offset #164
/*     */     //   Java source line #463	-> byte code offset #180
/*     */     //   Java source line #467	-> byte code offset #183
/*     */     //   Java source line #465	-> byte code offset #186
/*     */     //   Java source line #466	-> byte code offset #187
/*     */     //   Java source line #470	-> byte code offset #191
/*     */     //   Java source line #471	-> byte code offset #198
/*     */     //   Java source line #472	-> byte code offset #202
/*     */     //   Java source line #473	-> byte code offset #209
/*     */     //   Java source line #478	-> byte code offset #210
/*     */     //   Java source line #476	-> byte code offset #213
/*     */     //   Java source line #477	-> byte code offset #214
/*     */     //   Java source line #478	-> byte code offset #218
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	221	0	this	NioEventLoop
/*     */     //   91	83	1	ioRatio	int
/*     */     //   186	2	1	t	Throwable
/*     */     //   213	2	1	t	Throwable
/*     */     //   110	7	2	localObject1	Object
/*     */     //   124	37	2	ioStartTime	long
/*     */     //   134	4	4	ioTime	long
/*     */     //   155	26	6	localObject2	Object
/*     */     //   162	4	7	ioTime	long
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   98	102	110	finally
/*     */     //   125	129	155	finally
/*     */     //   155	157	155	finally
/*     */     //   0	44	186	java/lang/Throwable
/*     */     //   47	183	186	java/lang/Throwable
/*     */     //   191	209	213	java/lang/Throwable
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\nio\NioEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */