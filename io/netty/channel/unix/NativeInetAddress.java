/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeInetAddress
/*     */ {
/*  27 */   private static final byte[] IPV4_MAPPED_IPV6_PREFIX = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 };
/*     */   final byte[] address;
/*     */   final int scopeId;
/*     */   
/*     */   public static NativeInetAddress newInstance(InetAddress addr)
/*     */   {
/*  33 */     byte[] bytes = addr.getAddress();
/*  34 */     if ((addr instanceof Inet6Address)) {
/*  35 */       return new NativeInetAddress(bytes, ((Inet6Address)addr).getScopeId());
/*     */     }
/*     */     
/*  38 */     return new NativeInetAddress(ipv4MappedIpv6Address(bytes));
/*     */   }
/*     */   
/*     */   public NativeInetAddress(byte[] address, int scopeId)
/*     */   {
/*  43 */     this.address = address;
/*  44 */     this.scopeId = scopeId;
/*     */   }
/*     */   
/*     */   public NativeInetAddress(byte[] address) {
/*  48 */     this(address, 0);
/*     */   }
/*     */   
/*     */   public byte[] address() {
/*  52 */     return this.address;
/*     */   }
/*     */   
/*     */   public int scopeId() {
/*  56 */     return this.scopeId;
/*     */   }
/*     */   
/*     */   public static byte[] ipv4MappedIpv6Address(byte[] ipv4) {
/*  60 */     byte[] address = new byte[16];
/*  61 */     System.arraycopy(IPV4_MAPPED_IPV6_PREFIX, 0, address, 0, IPV4_MAPPED_IPV6_PREFIX.length);
/*  62 */     System.arraycopy(ipv4, 0, address, 12, ipv4.length);
/*  63 */     return address;
/*     */   }
/*     */   
/*     */   public static InetSocketAddress address(byte[] addr, int offset, int len)
/*     */   {
/*  68 */     int port = decodeInt(addr, offset + len - 4);
/*     */     try {
/*     */       InetAddress address;
/*     */       InetAddress address;
/*  72 */       switch (len)
/*     */       {
/*     */ 
/*     */ 
/*     */       case 8: 
/*  77 */         byte[] ipv4 = new byte[4];
/*  78 */         System.arraycopy(addr, offset, ipv4, 0, 4);
/*  79 */         address = InetAddress.getByAddress(ipv4);
/*  80 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 24: 
/*  87 */         byte[] ipv6 = new byte[16];
/*  88 */         System.arraycopy(addr, offset, ipv6, 0, 16);
/*  89 */         int scopeId = decodeInt(addr, offset + len - 8);
/*  90 */         address = Inet6Address.getByAddress(null, ipv6, scopeId);
/*  91 */         break;
/*     */       default: 
/*  93 */         throw new Error(); }
/*     */       InetAddress address;
/*  95 */       return new InetSocketAddress(address, port);
/*     */     } catch (UnknownHostException e) {
/*  97 */       throw new Error("Should never happen", e);
/*     */     }
/*     */   }
/*     */   
/*     */   static int decodeInt(byte[] addr, int index) {
/* 102 */     return (addr[index] & 0xFF) << 24 | (addr[(index + 1)] & 0xFF) << 16 | (addr[(index + 2)] & 0xFF) << 8 | addr[(index + 3)] & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\NativeInetAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */