/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ChannelHandler.Sharable
/*     */ public class GlobalTrafficShapingHandler
/*     */   extends AbstractTrafficShapingHandler
/*     */ {
/*  82 */   private final ConcurrentMap<Integer, PerChannel> channelQueues = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private final AtomicLong queuesSize = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   long maxGlobalWriteSize = 419430400L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void createGlobalTrafficCounter(ScheduledExecutorService executor)
/*     */   {
/* 106 */     if (executor == null) {
/* 107 */       throw new NullPointerException("executor");
/*     */     }
/* 109 */     TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
/* 110 */     setTrafficCounter(tc);
/* 111 */     tc.start();
/*     */   }
/*     */   
/*     */   protected int userDefinedWritabilityIndex()
/*     */   {
/* 116 */     return 2;
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval, long maxTime)
/*     */   {
/* 136 */     super(writeLimit, readLimit, checkInterval, maxTime);
/* 137 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval)
/*     */   {
/* 156 */     super(writeLimit, readLimit, checkInterval);
/* 157 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit)
/*     */   {
/* 173 */     super(writeLimit, readLimit);
/* 174 */     createGlobalTrafficCounter(executor);
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
/*     */   public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval)
/*     */   {
/* 188 */     super(checkInterval);
/* 189 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalTrafficShapingHandler(EventExecutor executor)
/*     */   {
/* 200 */     createGlobalTrafficCounter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxGlobalWriteSize()
/*     */   {
/* 207 */     return this.maxGlobalWriteSize;
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
/*     */   public void setMaxGlobalWriteSize(long maxGlobalWriteSize)
/*     */   {
/* 222 */     this.maxGlobalWriteSize = maxGlobalWriteSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long queuesSize()
/*     */   {
/* 229 */     return this.queuesSize.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void release()
/*     */   {
/* 236 */     this.trafficCounter.stop();
/*     */   }
/*     */   
/*     */   private PerChannel getOrSetPerChannel(ChannelHandlerContext ctx)
/*     */   {
/* 241 */     Channel channel = ctx.channel();
/* 242 */     Integer key = Integer.valueOf(channel.hashCode());
/* 243 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 244 */     if (perChannel == null) {
/* 245 */       perChannel = new PerChannel(null);
/* 246 */       perChannel.messagesQueue = new ArrayDeque();
/* 247 */       perChannel.queueSize = 0L;
/* 248 */       perChannel.lastReadTimestamp = TrafficCounter.milliSecondFromNano();
/* 249 */       perChannel.lastWriteTimestamp = perChannel.lastReadTimestamp;
/* 250 */       this.channelQueues.put(key, perChannel);
/*     */     }
/* 252 */     return perChannel;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 257 */     getOrSetPerChannel(ctx);
/* 258 */     super.handlerAdded(ctx);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 263 */     Channel channel = ctx.channel();
/* 264 */     Integer key = Integer.valueOf(channel.hashCode());
/* 265 */     PerChannel perChannel = (PerChannel)this.channelQueues.remove(key);
/* 266 */     if (perChannel != null)
/*     */     {
/* 268 */       synchronized (perChannel) {
/* 269 */         if (channel.isActive()) {
/* 270 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 271 */             long size = calculateSize(toSend.toSend);
/* 272 */             this.trafficCounter.bytesRealWriteFlowControl(size);
/* 273 */             perChannel.queueSize -= size;
/* 274 */             this.queuesSize.addAndGet(-size);
/* 275 */             ctx.write(toSend.toSend, toSend.promise);
/*     */           }
/*     */         } else {
/* 278 */           this.queuesSize.addAndGet(-perChannel.queueSize);
/* 279 */           for (ToSend toSend : perChannel.messagesQueue) {
/* 280 */             if ((toSend.toSend instanceof ByteBuf)) {
/* 281 */               ((ByteBuf)toSend.toSend).release();
/*     */             }
/*     */           }
/*     */         }
/* 285 */         perChannel.messagesQueue.clear();
/*     */       }
/*     */     }
/* 288 */     releaseWriteSuspended(ctx);
/* 289 */     releaseReadSuspended(ctx);
/* 290 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now)
/*     */   {
/* 295 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 296 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 297 */     if ((perChannel != null) && 
/* 298 */       (wait > this.maxTime) && (now + wait - perChannel.lastReadTimestamp > this.maxTime)) {
/* 299 */       wait = this.maxTime;
/*     */     }
/*     */     
/* 302 */     return wait;
/*     */   }
/*     */   
/*     */   void informReadOperation(ChannelHandlerContext ctx, long now)
/*     */   {
/* 307 */     Integer key = Integer.valueOf(ctx.channel().hashCode());
/* 308 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 309 */     if (perChannel != null) {
/* 310 */       perChannel.lastReadTimestamp = now;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ToSend {
/*     */     final long relativeTimeAction;
/*     */     final Object toSend;
/*     */     final long size;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     private ToSend(long delay, Object toSend, long size, ChannelPromise promise) {
/* 321 */       this.relativeTimeAction = delay;
/* 322 */       this.toSend = toSend;
/* 323 */       this.size = size;
/* 324 */       this.promise = promise;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void submitWrite(final ChannelHandlerContext ctx, Object msg, long size, long writedelay, long now, ChannelPromise promise)
/*     */   {
/* 332 */     Channel channel = ctx.channel();
/* 333 */     Integer key = Integer.valueOf(channel.hashCode());
/* 334 */     PerChannel perChannel = (PerChannel)this.channelQueues.get(key);
/* 335 */     if (perChannel == null)
/*     */     {
/*     */ 
/* 338 */       perChannel = getOrSetPerChannel(ctx);
/*     */     }
/*     */     
/* 341 */     long delay = writedelay;
/* 342 */     boolean globalSizeExceeded = false;
/*     */     
/* 344 */     synchronized (perChannel) {
/* 345 */       if ((writedelay == 0L) && (perChannel.messagesQueue.isEmpty())) {
/* 346 */         this.trafficCounter.bytesRealWriteFlowControl(size);
/* 347 */         ctx.write(msg, promise);
/* 348 */         perChannel.lastWriteTimestamp = now;
/* 349 */         return;
/*     */       }
/* 351 */       if ((delay > this.maxTime) && (now + delay - perChannel.lastWriteTimestamp > this.maxTime)) {
/* 352 */         delay = this.maxTime;
/*     */       }
/* 354 */       ToSend newToSend = new ToSend(delay + now, msg, size, promise, null);
/* 355 */       perChannel.messagesQueue.addLast(newToSend);
/* 356 */       perChannel.queueSize += size;
/* 357 */       this.queuesSize.addAndGet(size);
/* 358 */       checkWriteSuspend(ctx, delay, perChannel.queueSize);
/* 359 */       if (this.queuesSize.get() > this.maxGlobalWriteSize)
/* 360 */         globalSizeExceeded = true;
/*     */     }
/*     */     ToSend newToSend;
/* 363 */     if (globalSizeExceeded) {
/* 364 */       setUserDefinedWritability(ctx, false);
/*     */     }
/* 366 */     final long futureNow = newToSend.relativeTimeAction;
/* 367 */     final PerChannel forSchedule = perChannel;
/* 368 */     ctx.executor().schedule(new Runnable()
/*     */     {
/*     */ 
/* 371 */       public void run() { GlobalTrafficShapingHandler.this.sendAllValid(ctx, forSchedule, futureNow); } }, delay, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendAllValid(ChannelHandlerContext ctx, PerChannel perChannel, long now)
/*     */   {
/* 378 */     synchronized (perChannel) {
/* 379 */       for (ToSend newToSend = (ToSend)perChannel.messagesQueue.pollFirst(); 
/* 380 */           newToSend != null; newToSend = (ToSend)perChannel.messagesQueue.pollFirst()) {
/* 381 */         if (newToSend.relativeTimeAction <= now) {
/* 382 */           long size = newToSend.size;
/* 383 */           this.trafficCounter.bytesRealWriteFlowControl(size);
/* 384 */           perChannel.queueSize -= size;
/* 385 */           this.queuesSize.addAndGet(-size);
/* 386 */           ctx.write(newToSend.toSend, newToSend.promise);
/* 387 */           perChannel.lastWriteTimestamp = now;
/*     */         } else {
/* 389 */           perChannel.messagesQueue.addFirst(newToSend);
/* 390 */           break;
/*     */         }
/*     */       }
/* 393 */       if (perChannel.messagesQueue.isEmpty()) {
/* 394 */         releaseWriteSuspended(ctx);
/*     */       }
/*     */     }
/* 397 */     ctx.flush();
/*     */   }
/*     */   
/*     */   private static final class PerChannel
/*     */   {
/*     */     ArrayDeque<GlobalTrafficShapingHandler.ToSend> messagesQueue;
/*     */     long queueSize;
/*     */     long lastWriteTimestamp;
/*     */     long lastReadTimestamp;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\traffic\GlobalTrafficShapingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */