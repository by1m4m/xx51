/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer.MessageProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IovArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  50 */   private static final int ADDRESS_SIZE = Buffer.addressSize();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   private static final int IOV_SIZE = 2 * ADDRESS_SIZE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private static final int CAPACITY = Limits.IOV_MAX * IOV_SIZE;
/*     */   
/*     */   private final ByteBuffer memory;
/*     */   private final long memoryAddress;
/*     */   private int count;
/*     */   private long size;
/*  68 */   private long maxBytes = Limits.SSIZE_MAX;
/*     */   
/*     */   public IovArray() {
/*  71 */     this.memory = Buffer.allocateDirectWithNativeOrder(CAPACITY);
/*  72 */     this.memoryAddress = Buffer.memoryAddress(this.memory);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  76 */     this.count = 0;
/*  77 */     this.size = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(ByteBuf buf)
/*     */   {
/*  88 */     if (this.count == Limits.IOV_MAX)
/*     */     {
/*  90 */       return false; }
/*  91 */     ByteBuffer nioBuffer; if (buf.nioBufferCount() == 1) {
/*  92 */       int len = buf.readableBytes();
/*  93 */       if (len == 0) {
/*  94 */         return true;
/*     */       }
/*  96 */       if (buf.hasMemoryAddress()) {
/*  97 */         return add(buf.memoryAddress(), buf.readerIndex(), len);
/*     */       }
/*  99 */       nioBuffer = buf.internalNioBuffer(buf.readerIndex(), len);
/* 100 */       return add(Buffer.memoryAddress(nioBuffer), nioBuffer.position(), len);
/*     */     }
/*     */     
/* 103 */     ByteBuffer[] buffers = buf.nioBuffers();
/* 104 */     for (ByteBuffer nioBuffer : buffers) {
/* 105 */       int len = nioBuffer.remaining();
/* 106 */       if ((len != 0) && (
/* 107 */         (!add(Buffer.memoryAddress(nioBuffer), nioBuffer.position(), len)) || (this.count == Limits.IOV_MAX))) {
/* 108 */         return false;
/*     */       }
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   private boolean add(long addr, int offset, int len)
/*     */   {
/* 116 */     assert (addr != 0L);
/*     */     
/*     */ 
/*     */ 
/* 120 */     if ((this.maxBytes - len < this.size) && (this.count > 0))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */       return false;
/*     */     }
/* 129 */     int baseOffset = idx(this.count);
/* 130 */     int lengthOffset = baseOffset + ADDRESS_SIZE;
/*     */     
/* 132 */     this.size += len;
/* 133 */     this.count += 1;
/*     */     
/* 135 */     if (ADDRESS_SIZE == 8)
/*     */     {
/* 137 */       if (PlatformDependent.hasUnsafe()) {
/* 138 */         PlatformDependent.putLong(baseOffset + this.memoryAddress, addr + offset);
/* 139 */         PlatformDependent.putLong(lengthOffset + this.memoryAddress, len);
/*     */       } else {
/* 141 */         this.memory.putLong(baseOffset, addr + offset);
/* 142 */         this.memory.putLong(lengthOffset, len);
/*     */       }
/*     */     } else {
/* 145 */       assert (ADDRESS_SIZE == 4);
/* 146 */       if (PlatformDependent.hasUnsafe()) {
/* 147 */         PlatformDependent.putInt(baseOffset + this.memoryAddress, (int)addr + offset);
/* 148 */         PlatformDependent.putInt(lengthOffset + this.memoryAddress, len);
/*     */       } else {
/* 150 */         this.memory.putInt(baseOffset, (int)addr + offset);
/* 151 */         this.memory.putInt(lengthOffset, len);
/*     */       }
/*     */     }
/* 154 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int count()
/*     */   {
/* 161 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long size()
/*     */   {
/* 168 */     return this.size;
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
/*     */   public void maxBytes(long maxBytes)
/*     */   {
/* 182 */     this.maxBytes = Math.min(Limits.SSIZE_MAX, ObjectUtil.checkPositive(maxBytes, "maxBytes"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long maxBytes()
/*     */   {
/* 190 */     return this.maxBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long memoryAddress(int offset)
/*     */   {
/* 197 */     return this.memoryAddress + idx(offset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/* 204 */     Buffer.free(this.memory);
/*     */   }
/*     */   
/*     */   public boolean processMessage(Object msg) throws Exception
/*     */   {
/* 209 */     return ((msg instanceof ByteBuf)) && (add((ByteBuf)msg));
/*     */   }
/*     */   
/*     */   private static int idx(int index) {
/* 213 */     return IOV_SIZE * index;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\IovArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */