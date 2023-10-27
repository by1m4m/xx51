/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.unix.Buffer;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class EpollEventArray
/*     */ {
/*  43 */   private static final int EPOLL_EVENT_SIZE = ;
/*     */   
/*  45 */   private static final int EPOLL_DATA_OFFSET = Native.offsetofEpollData();
/*     */   private ByteBuffer memory;
/*     */   private long memoryAddress;
/*     */   private int length;
/*     */   
/*     */   EpollEventArray(int length)
/*     */   {
/*  52 */     if (length < 1) {
/*  53 */       throw new IllegalArgumentException("length must be >= 1 but was " + length);
/*     */     }
/*  55 */     this.length = length;
/*  56 */     this.memory = Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(length));
/*  57 */     this.memoryAddress = Buffer.memoryAddress(this.memory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long memoryAddress()
/*     */   {
/*  64 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int length()
/*     */   {
/*  72 */     return this.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void increase()
/*     */   {
/*  80 */     this.length <<= 1;
/*     */     
/*  82 */     ByteBuffer buffer = Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(this.length));
/*  83 */     Buffer.free(this.memory);
/*  84 */     this.memory = buffer;
/*  85 */     this.memoryAddress = Buffer.memoryAddress(buffer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void free()
/*     */   {
/*  92 */     Buffer.free(this.memory);
/*  93 */     this.memoryAddress = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int events(int index)
/*     */   {
/* 100 */     return getInt(index, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int fd(int index)
/*     */   {
/* 107 */     return getInt(index, EPOLL_DATA_OFFSET);
/*     */   }
/*     */   
/*     */   private int getInt(int index, int offset) {
/* 111 */     if (PlatformDependent.hasUnsafe()) {
/* 112 */       return PlatformDependent.getInt(this.memoryAddress + index * EPOLL_EVENT_SIZE + offset);
/*     */     }
/* 114 */     return this.memory.getInt(index * EPOLL_EVENT_SIZE + offset);
/*     */   }
/*     */   
/*     */   private static int calculateBufferCapacity(int capacity) {
/* 118 */     return capacity * EPOLL_EVENT_SIZE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollEventArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */