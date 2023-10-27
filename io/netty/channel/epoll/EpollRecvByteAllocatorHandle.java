/*     */ package io.netty.channel.epoll;
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
/*     */ class EpollRecvByteAllocatorHandle
/*     */   implements RecvByteBufAllocator.ExtendedHandle
/*     */ {
/*  27 */   private final PreferredDirectByteBufAllocator preferredDirectByteBufAllocator = new PreferredDirectByteBufAllocator();
/*     */   
/*     */   private final RecvByteBufAllocator.ExtendedHandle delegate;
/*  30 */   private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier()
/*     */   {
/*     */     public boolean get() {
/*  33 */       return EpollRecvByteAllocatorHandle.this.maybeMoreDataToRead();
/*     */     }
/*     */   };
/*     */   private boolean isEdgeTriggered;
/*     */   private boolean receivedRdHup;
/*     */   
/*     */   EpollRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/*  40 */     this.delegate = ((RecvByteBufAllocator.ExtendedHandle)ObjectUtil.checkNotNull(handle, "handle"));
/*     */   }
/*     */   
/*     */   final void receivedRdHup() {
/*  44 */     this.receivedRdHup = true;
/*     */   }
/*     */   
/*     */   final boolean isReceivedRdHup() {
/*  48 */     return this.receivedRdHup;
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
/*     */   boolean maybeMoreDataToRead()
/*     */   {
/*  61 */     return ((this.isEdgeTriggered) && (lastBytesRead() > 0)) || ((!this.isEdgeTriggered) && 
/*  62 */       (lastBytesRead() == attemptedBytesRead()));
/*     */   }
/*     */   
/*     */   final void edgeTriggered(boolean edgeTriggered) {
/*  66 */     this.isEdgeTriggered = edgeTriggered;
/*     */   }
/*     */   
/*     */   final boolean isEdgeTriggered() {
/*  70 */     return this.isEdgeTriggered;
/*     */   }
/*     */   
/*     */ 
/*     */   public final ByteBuf allocate(ByteBufAllocator alloc)
/*     */   {
/*  76 */     this.preferredDirectByteBufAllocator.updateAllocator(alloc);
/*  77 */     return this.delegate.allocate(this.preferredDirectByteBufAllocator);
/*     */   }
/*     */   
/*     */   public final int guess()
/*     */   {
/*  82 */     return this.delegate.guess();
/*     */   }
/*     */   
/*     */   public final void reset(ChannelConfig config)
/*     */   {
/*  87 */     this.delegate.reset(config);
/*     */   }
/*     */   
/*     */   public final void incMessagesRead(int numMessages)
/*     */   {
/*  92 */     this.delegate.incMessagesRead(numMessages);
/*     */   }
/*     */   
/*     */   public final void lastBytesRead(int bytes)
/*     */   {
/*  97 */     this.delegate.lastBytesRead(bytes);
/*     */   }
/*     */   
/*     */   public final int lastBytesRead()
/*     */   {
/* 102 */     return this.delegate.lastBytesRead();
/*     */   }
/*     */   
/*     */   public final int attemptedBytesRead()
/*     */   {
/* 107 */     return this.delegate.attemptedBytesRead();
/*     */   }
/*     */   
/*     */   public final void attemptedBytesRead(int bytes)
/*     */   {
/* 112 */     this.delegate.attemptedBytesRead(bytes);
/*     */   }
/*     */   
/*     */   public final void readComplete()
/*     */   {
/* 117 */     this.delegate.readComplete();
/*     */   }
/*     */   
/*     */   public final boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier)
/*     */   {
/* 122 */     return this.delegate.continueReading(maybeMoreDataSupplier);
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean continueReading()
/*     */   {
/* 128 */     return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollRecvByteAllocatorHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */