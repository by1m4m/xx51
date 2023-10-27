/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.RecvByteBufAllocator.ExtendedHandle;
/*     */ import io.netty.channel.unix.PreferredDirectByteBufAllocator;
/*     */ import io.netty.util.UncheckedBooleanSupplier;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class KQueueRecvByteAllocatorHandle
/*     */   implements RecvByteBufAllocator.ExtendedHandle
/*     */ {
/*  30 */   private final PreferredDirectByteBufAllocator preferredDirectByteBufAllocator = new PreferredDirectByteBufAllocator();
/*     */   
/*     */   private final RecvByteBufAllocator.ExtendedHandle delegate;
/*     */   
/*  34 */   private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier()
/*     */   {
/*     */     public boolean get() {
/*  37 */       return KQueueRecvByteAllocatorHandle.this.maybeMoreDataToRead();
/*     */     }
/*     */   };
/*     */   private boolean overrideGuess;
/*     */   private boolean readEOF;
/*     */   private long numberBytesPending;
/*     */   
/*     */   KQueueRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/*  45 */     this.delegate = ((RecvByteBufAllocator.ExtendedHandle)ObjectUtil.checkNotNull(handle, "handle"));
/*     */   }
/*     */   
/*     */   public int guess()
/*     */   {
/*  50 */     return this.overrideGuess ? guess0() : this.delegate.guess();
/*     */   }
/*     */   
/*     */   public void reset(ChannelConfig config)
/*     */   {
/*  55 */     this.overrideGuess = ((KQueueChannelConfig)config).getRcvAllocTransportProvidesGuess();
/*  56 */     this.delegate.reset(config);
/*     */   }
/*     */   
/*     */   public void incMessagesRead(int numMessages)
/*     */   {
/*  61 */     this.delegate.incMessagesRead(numMessages);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuf allocate(ByteBufAllocator alloc)
/*     */   {
/*  67 */     this.preferredDirectByteBufAllocator.updateAllocator(alloc);
/*  68 */     return this.overrideGuess ? this.preferredDirectByteBufAllocator.ioBuffer(guess0()) : this.delegate
/*  69 */       .allocate(this.preferredDirectByteBufAllocator);
/*     */   }
/*     */   
/*     */   public void lastBytesRead(int bytes)
/*     */   {
/*  74 */     this.numberBytesPending = (bytes < 0 ? 0L : Math.max(0L, this.numberBytesPending - bytes));
/*  75 */     this.delegate.lastBytesRead(bytes);
/*     */   }
/*     */   
/*     */   public int lastBytesRead()
/*     */   {
/*  80 */     return this.delegate.lastBytesRead();
/*     */   }
/*     */   
/*     */   public void attemptedBytesRead(int bytes)
/*     */   {
/*  85 */     this.delegate.attemptedBytesRead(bytes);
/*     */   }
/*     */   
/*     */   public int attemptedBytesRead()
/*     */   {
/*  90 */     return this.delegate.attemptedBytesRead();
/*     */   }
/*     */   
/*     */   public void readComplete()
/*     */   {
/*  95 */     this.delegate.readComplete();
/*     */   }
/*     */   
/*     */   public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier)
/*     */   {
/* 100 */     return this.delegate.continueReading(maybeMoreDataSupplier);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean continueReading()
/*     */   {
/* 106 */     return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
/*     */   }
/*     */   
/*     */   void readEOF() {
/* 110 */     this.readEOF = true;
/*     */   }
/*     */   
/*     */   boolean isReadEOF() {
/* 114 */     return this.readEOF;
/*     */   }
/*     */   
/*     */   void numberBytesPending(long numberBytesPending) {
/* 118 */     this.numberBytesPending = numberBytesPending;
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
/*     */   boolean maybeMoreDataToRead()
/*     */   {
/* 132 */     return this.numberBytesPending != 0L;
/*     */   }
/*     */   
/*     */   private int guess0() {
/* 136 */     return (int)Math.min(this.numberBytesPending, 2147483647L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\KQueueRecvByteAllocatorHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */