/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface RecvByteBufAllocator
/*     */ {
/*     */   public abstract Handle newHandle();
/*     */   
/*     */   public static class DelegatingHandle
/*     */     implements RecvByteBufAllocator.Handle
/*     */   {
/*     */     private final RecvByteBufAllocator.Handle delegate;
/*     */     
/*     */     public DelegatingHandle(RecvByteBufAllocator.Handle delegate)
/*     */     {
/* 127 */       this.delegate = ((RecvByteBufAllocator.Handle)ObjectUtil.checkNotNull(delegate, "delegate"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final RecvByteBufAllocator.Handle delegate()
/*     */     {
/* 135 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public ByteBuf allocate(ByteBufAllocator alloc)
/*     */     {
/* 140 */       return this.delegate.allocate(alloc);
/*     */     }
/*     */     
/*     */     public int guess()
/*     */     {
/* 145 */       return this.delegate.guess();
/*     */     }
/*     */     
/*     */     public void reset(ChannelConfig config)
/*     */     {
/* 150 */       this.delegate.reset(config);
/*     */     }
/*     */     
/*     */     public void incMessagesRead(int numMessages)
/*     */     {
/* 155 */       this.delegate.incMessagesRead(numMessages);
/*     */     }
/*     */     
/*     */     public void lastBytesRead(int bytes)
/*     */     {
/* 160 */       this.delegate.lastBytesRead(bytes);
/*     */     }
/*     */     
/*     */     public int lastBytesRead()
/*     */     {
/* 165 */       return this.delegate.lastBytesRead();
/*     */     }
/*     */     
/*     */     public boolean continueReading()
/*     */     {
/* 170 */       return this.delegate.continueReading();
/*     */     }
/*     */     
/*     */     public int attemptedBytesRead()
/*     */     {
/* 175 */       return this.delegate.attemptedBytesRead();
/*     */     }
/*     */     
/*     */     public void attemptedBytesRead(int bytes)
/*     */     {
/* 180 */       this.delegate.attemptedBytesRead(bytes);
/*     */     }
/*     */     
/*     */     public void readComplete()
/*     */     {
/* 185 */       this.delegate.readComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ExtendedHandle
/*     */     extends RecvByteBufAllocator.Handle
/*     */   {
/*     */     public abstract boolean continueReading(UncheckedBooleanSupplier paramUncheckedBooleanSupplier);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static abstract interface Handle
/*     */   {
/*     */     public abstract ByteBuf allocate(ByteBufAllocator paramByteBufAllocator);
/*     */     
/*     */     public abstract int guess();
/*     */     
/*     */     public abstract void reset(ChannelConfig paramChannelConfig);
/*     */     
/*     */     public abstract void incMessagesRead(int paramInt);
/*     */     
/*     */     public abstract void lastBytesRead(int paramInt);
/*     */     
/*     */     public abstract int lastBytesRead();
/*     */     
/*     */     public abstract void attemptedBytesRead(int paramInt);
/*     */     
/*     */     public abstract int attemptedBytesRead();
/*     */     
/*     */     public abstract boolean continueReading();
/*     */     
/*     */     public abstract void readComplete();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\RecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */