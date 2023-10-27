/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.unix.Limits;
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
/*     */ final class NativeLongArray
/*     */ {
/*     */   private ByteBuffer memory;
/*     */   private long memoryAddress;
/*     */   private int capacity;
/*     */   private int size;
/*     */   
/*     */   NativeLongArray(int capacity)
/*     */   {
/*  32 */     if (capacity < 1) {
/*  33 */       throw new IllegalArgumentException("capacity must be >= 1 but was " + capacity);
/*     */     }
/*  35 */     this.memory = io.netty.channel.unix.Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(capacity));
/*  36 */     this.memoryAddress = io.netty.channel.unix.Buffer.memoryAddress(this.memory);
/*  37 */     this.capacity = capacity;
/*     */   }
/*     */   
/*     */   private static int idx(int index) {
/*  41 */     return index * Limits.SIZEOF_JLONG;
/*     */   }
/*     */   
/*     */   private static int calculateBufferCapacity(int capacity) {
/*  45 */     return capacity * Limits.SIZEOF_JLONG;
/*     */   }
/*     */   
/*     */   void add(long value) {
/*  49 */     reallocIfNeeded();
/*  50 */     if (PlatformDependent.hasUnsafe()) {
/*  51 */       PlatformDependent.putLong(memoryOffset(this.size), value);
/*     */     } else {
/*  53 */       this.memory.putLong(idx(this.size), value);
/*     */     }
/*  55 */     this.size += 1;
/*     */   }
/*     */   
/*     */   void clear() {
/*  59 */     this.size = 0;
/*     */   }
/*     */   
/*     */   boolean isEmpty() {
/*  63 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   void free() {
/*  67 */     io.netty.channel.unix.Buffer.free(this.memory);
/*  68 */     this.memoryAddress = 0L;
/*     */   }
/*     */   
/*     */   long memoryAddress() {
/*  72 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */   long memoryAddressEnd() {
/*  76 */     return memoryOffset(this.size);
/*     */   }
/*     */   
/*     */   private long memoryOffset(int index) {
/*  80 */     return this.memoryAddress + idx(index);
/*     */   }
/*     */   
/*     */   private void reallocIfNeeded() {
/*  84 */     if (this.size == this.capacity)
/*     */     {
/*  86 */       int newLength = this.capacity <= 65536 ? this.capacity << 1 : this.capacity + this.capacity >> 1;
/*  87 */       ByteBuffer buffer = io.netty.channel.unix.Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(newLength));
/*     */       
/*     */ 
/*  90 */       this.memory.position(0).limit(this.size);
/*  91 */       buffer.put(this.memory);
/*  92 */       buffer.position(0);
/*     */       
/*  94 */       io.netty.channel.unix.Buffer.free(this.memory);
/*  95 */       this.memory = buffer;
/*  96 */       this.memoryAddress = io.netty.channel.unix.Buffer.memoryAddress(buffer);
/*  97 */       this.capacity = newLength;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 103 */     return "memoryAddress: " + this.memoryAddress + " capacity: " + this.capacity + " size: " + this.size;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\NativeLongArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */