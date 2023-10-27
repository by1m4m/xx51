/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer.MessageProcessor;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.Limits;
/*     */ import io.netty.channel.unix.NativeInetAddress;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NativeDatagramPacketArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  35 */   private final NativeDatagramPacket[] packets = new NativeDatagramPacket[Limits.UIO_MAX_IOV];
/*     */   private int count;
/*     */   
/*     */   NativeDatagramPacketArray() {
/*  39 */     for (int i = 0; i < this.packets.length; i++) {
/*  40 */       this.packets[i] = new NativeDatagramPacket();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(DatagramPacket packet)
/*     */   {
/*  49 */     if (this.count == this.packets.length) {
/*  50 */       return false;
/*     */     }
/*  52 */     ByteBuf content = (ByteBuf)packet.content();
/*  53 */     int len = content.readableBytes();
/*  54 */     if (len == 0) {
/*  55 */       return true;
/*     */     }
/*  57 */     NativeDatagramPacket p = this.packets[this.count];
/*  58 */     InetSocketAddress recipient = (InetSocketAddress)packet.recipient();
/*  59 */     if (!p.init(content, recipient)) {
/*  60 */       return false;
/*     */     }
/*     */     
/*  63 */     this.count += 1;
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   public boolean processMessage(Object msg)
/*     */   {
/*  69 */     return ((msg instanceof DatagramPacket)) && (add((DatagramPacket)msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int count()
/*     */   {
/*  76 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   NativeDatagramPacket[] packets()
/*     */   {
/*  83 */     return this.packets;
/*     */   }
/*     */   
/*     */   void clear() {
/*  87 */     this.count = 0;
/*     */   }
/*     */   
/*     */   void release()
/*     */   {
/*  92 */     for (NativeDatagramPacket datagramPacket : this.packets) {
/*  93 */       datagramPacket.release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class NativeDatagramPacket
/*     */   {
/* 105 */     private final IovArray array = new IovArray();
/*     */     
/*     */     private long memoryAddress;
/*     */     
/*     */     private int count;
/*     */     private byte[] addr;
/*     */     private int scopeId;
/*     */     private int port;
/*     */     
/*     */     private void release()
/*     */     {
/* 116 */       this.array.release();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean init(ByteBuf buf, InetSocketAddress recipient)
/*     */     {
/* 123 */       this.array.clear();
/* 124 */       if (!this.array.add(buf)) {
/* 125 */         return false;
/*     */       }
/*     */       
/* 128 */       this.memoryAddress = this.array.memoryAddress(0);
/* 129 */       this.count = this.array.count();
/*     */       
/* 131 */       InetAddress address = recipient.getAddress();
/* 132 */       if ((address instanceof Inet6Address)) {
/* 133 */         this.addr = address.getAddress();
/* 134 */         this.scopeId = ((Inet6Address)address).getScopeId();
/*     */       } else {
/* 136 */         this.addr = NativeInetAddress.ipv4MappedIpv6Address(address.getAddress());
/* 137 */         this.scopeId = 0;
/*     */       }
/* 139 */       this.port = recipient.getPort();
/* 140 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\NativeDatagramPacketArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */