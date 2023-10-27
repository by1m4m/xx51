/*     */ package io.netty.channel.kqueue;
/*     */ 
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
/*     */ final class KQueueEventArray
/*     */ {
/*  36 */   private static final int KQUEUE_EVENT_SIZE = ;
/*  37 */   private static final int KQUEUE_IDENT_OFFSET = Native.offsetofKEventIdent();
/*  38 */   private static final int KQUEUE_FILTER_OFFSET = Native.offsetofKEventFilter();
/*  39 */   private static final int KQUEUE_FFLAGS_OFFSET = Native.offsetofKEventFFlags();
/*  40 */   private static final int KQUEUE_FLAGS_OFFSET = Native.offsetofKEventFlags();
/*  41 */   private static final int KQUEUE_DATA_OFFSET = Native.offsetofKeventData();
/*     */   private ByteBuffer memory;
/*     */   private long memoryAddress;
/*     */   private int size;
/*     */   private int capacity;
/*     */   
/*     */   KQueueEventArray(int capacity)
/*     */   {
/*  49 */     if (capacity < 1) {
/*  50 */       throw new IllegalArgumentException("capacity must be >= 1 but was " + capacity);
/*     */     }
/*  52 */     this.memory = io.netty.channel.unix.Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(capacity));
/*  53 */     this.memoryAddress = io.netty.channel.unix.Buffer.memoryAddress(this.memory);
/*  54 */     this.capacity = capacity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long memoryAddress()
/*     */   {
/*  61 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int capacity()
/*     */   {
/*  69 */     return this.capacity;
/*     */   }
/*     */   
/*     */   int size() {
/*  73 */     return this.size;
/*     */   }
/*     */   
/*     */   void clear() {
/*  77 */     this.size = 0;
/*     */   }
/*     */   
/*     */   void evSet(AbstractKQueueChannel ch, short filter, short flags, int fflags) {
/*  81 */     reallocIfNeeded();
/*  82 */     evSet(getKEventOffset(this.size++) + this.memoryAddress, ch, ch.socket.intValue(), filter, flags, fflags);
/*     */   }
/*     */   
/*     */   private void reallocIfNeeded() {
/*  86 */     if (this.size == this.capacity) {
/*  87 */       realloc(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void realloc(boolean throwIfFail)
/*     */   {
/*  96 */     int newLength = this.capacity <= 65536 ? this.capacity << 1 : this.capacity + this.capacity >> 1;
/*     */     try
/*     */     {
/*  99 */       ByteBuffer buffer = io.netty.channel.unix.Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(newLength));
/*     */       
/*     */ 
/* 102 */       this.memory.position(0).limit(this.size);
/* 103 */       buffer.put(this.memory);
/* 104 */       buffer.position(0);
/*     */       
/* 106 */       io.netty.channel.unix.Buffer.free(this.memory);
/* 107 */       this.memory = buffer;
/* 108 */       this.memoryAddress = io.netty.channel.unix.Buffer.memoryAddress(buffer);
/*     */     } catch (OutOfMemoryError e) {
/* 110 */       if (throwIfFail) {
/* 111 */         OutOfMemoryError error = new OutOfMemoryError("unable to allocate " + newLength + " new bytes! Existing capacity is: " + this.capacity);
/*     */         
/* 113 */         error.initCause(e);
/* 114 */         throw error;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void free()
/*     */   {
/* 123 */     io.netty.channel.unix.Buffer.free(this.memory);
/* 124 */     this.memoryAddress = (this.size = this.capacity = 0);
/*     */   }
/*     */   
/*     */   private static int getKEventOffset(int index) {
/* 128 */     return index * KQUEUE_EVENT_SIZE;
/*     */   }
/*     */   
/*     */   private long getKEventOffsetAddress(int index) {
/* 132 */     return getKEventOffset(index) + this.memoryAddress;
/*     */   }
/*     */   
/*     */   private short getShort(int index, int offset) {
/* 136 */     if (PlatformDependent.hasUnsafe()) {
/* 137 */       return PlatformDependent.getShort(getKEventOffsetAddress(index) + offset);
/*     */     }
/* 139 */     return this.memory.getShort(getKEventOffset(index) + offset);
/*     */   }
/*     */   
/*     */   short flags(int index) {
/* 143 */     return getShort(index, KQUEUE_FLAGS_OFFSET);
/*     */   }
/*     */   
/*     */   short filter(int index) {
/* 147 */     return getShort(index, KQUEUE_FILTER_OFFSET);
/*     */   }
/*     */   
/*     */   short fflags(int index) {
/* 151 */     return getShort(index, KQUEUE_FFLAGS_OFFSET);
/*     */   }
/*     */   
/*     */   int fd(int index) {
/* 155 */     if (PlatformDependent.hasUnsafe()) {
/* 156 */       return PlatformDependent.getInt(getKEventOffsetAddress(index) + KQUEUE_IDENT_OFFSET);
/*     */     }
/* 158 */     return this.memory.getInt(getKEventOffset(index) + KQUEUE_IDENT_OFFSET);
/*     */   }
/*     */   
/*     */   long data(int index) {
/* 162 */     if (PlatformDependent.hasUnsafe()) {
/* 163 */       return PlatformDependent.getLong(getKEventOffsetAddress(index) + KQUEUE_DATA_OFFSET);
/*     */     }
/* 165 */     return this.memory.getLong(getKEventOffset(index) + KQUEUE_DATA_OFFSET);
/*     */   }
/*     */   
/*     */   AbstractKQueueChannel channel(int index) {
/* 169 */     return getChannel(getKEventOffsetAddress(index));
/*     */   }
/*     */   
/*     */   private static int calculateBufferCapacity(int capacity) {
/* 173 */     return capacity * KQUEUE_EVENT_SIZE;
/*     */   }
/*     */   
/*     */   private static native void evSet(long paramLong, AbstractKQueueChannel paramAbstractKQueueChannel, int paramInt1, short paramShort1, short paramShort2, int paramInt2);
/*     */   
/*     */   private static native AbstractKQueueChannel getChannel(long paramLong);
/*     */   
/*     */   static native void deleteGlobalRefs(long paramLong1, long paramLong2);
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\KQueueEventArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */