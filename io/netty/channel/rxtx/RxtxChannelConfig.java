/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract interface RxtxChannelConfig extends io.netty.channel.ChannelConfig { public abstract RxtxChannelConfig setBaudrate(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setStopbits(Stopbits paramStopbits);
/*     */   
/*     */   public abstract RxtxChannelConfig setDatabits(Databits paramDatabits);
/*     */   
/*     */   public abstract RxtxChannelConfig setParitybit(Paritybit paramParitybit);
/*     */   
/*     */   public abstract int getBaudrate();
/*     */   
/*     */   public abstract Stopbits getStopbits();
/*     */   
/*     */   public abstract Databits getDatabits();
/*     */   
/*     */   public abstract Paritybit getParitybit();
/*     */   
/*     */   public abstract boolean isDtr();
/*     */   
/*     */   public abstract RxtxChannelConfig setDtr(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean isRts();
/*     */   
/*     */   public abstract RxtxChannelConfig setRts(boolean paramBoolean);
/*     */   
/*     */   public abstract int getWaitTimeMillis();
/*     */   
/*     */   public abstract RxtxChannelConfig setWaitTimeMillis(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setReadTimeout(int paramInt);
/*     */   
/*     */   public abstract int getReadTimeout();
/*     */   
/*     */   public abstract RxtxChannelConfig setConnectTimeoutMillis(int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   public abstract RxtxChannelConfig setMaxMessagesPerRead(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteSpinCount(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setAllocator(io.netty.buffer.ByteBufAllocator paramByteBufAllocator);
/*     */   
/*     */   public abstract RxtxChannelConfig setRecvByteBufAllocator(io.netty.channel.RecvByteBufAllocator paramRecvByteBufAllocator);
/*     */   
/*     */   public abstract RxtxChannelConfig setAutoRead(boolean paramBoolean);
/*     */   
/*     */   public abstract RxtxChannelConfig setAutoClose(boolean paramBoolean);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteBufferHighWaterMark(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteBufferLowWaterMark(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark paramWriteBufferWaterMark);
/*     */   
/*     */   public abstract RxtxChannelConfig setMessageSizeEstimator(io.netty.channel.MessageSizeEstimator paramMessageSizeEstimator);
/*     */   
/*  61 */   public static enum Stopbits { STOPBITS_1(1), 
/*     */     
/*     */ 
/*     */ 
/*  65 */     STOPBITS_2(2), 
/*     */     
/*     */ 
/*     */ 
/*  69 */     STOPBITS_1_5(3);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Stopbits(int value) {
/*  74 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/*  78 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Stopbits valueOf(int value) {
/*  82 */       for (Stopbits stopbit : ) {
/*  83 */         if (stopbit.value == value) {
/*  84 */           return stopbit;
/*     */         }
/*     */       }
/*  87 */       throw new IllegalArgumentException("unknown " + Stopbits.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Databits
/*     */   {
/*  95 */     DATABITS_5(5), 
/*     */     
/*     */ 
/*     */ 
/*  99 */     DATABITS_6(6), 
/*     */     
/*     */ 
/*     */ 
/* 103 */     DATABITS_7(7), 
/*     */     
/*     */ 
/*     */ 
/* 107 */     DATABITS_8(8);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Databits(int value) {
/* 112 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 116 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Databits valueOf(int value) {
/* 120 */       for (Databits databit : ) {
/* 121 */         if (databit.value == value) {
/* 122 */           return databit;
/*     */         }
/*     */       }
/* 125 */       throw new IllegalArgumentException("unknown " + Databits.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Paritybit
/*     */   {
/* 133 */     NONE(0), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 138 */     ODD(1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 143 */     EVEN(2), 
/*     */     
/*     */ 
/*     */ 
/* 147 */     MARK(3), 
/*     */     
/*     */ 
/*     */ 
/* 151 */     SPACE(4);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Paritybit(int value) {
/* 156 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 160 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Paritybit valueOf(int value) {
/* 164 */       for (Paritybit paritybit : ) {
/* 165 */         if (paritybit.value == value) {
/* 166 */           return paritybit;
/*     */         }
/*     */       }
/* 169 */       throw new IllegalArgumentException("unknown " + Paritybit.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\rxtx\RxtxChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */