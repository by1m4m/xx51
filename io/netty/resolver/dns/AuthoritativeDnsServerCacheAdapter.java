/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.handler.codec.dns.DnsRecord;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class AuthoritativeDnsServerCacheAdapter
/*    */   implements AuthoritativeDnsServerCache
/*    */ {
/* 36 */   private static final DnsRecord[] EMPTY = new DnsRecord[0];
/*    */   private final DnsCache cache;
/*    */   
/*    */   AuthoritativeDnsServerCacheAdapter(DnsCache cache) {
/* 40 */     this.cache = ((DnsCache)ObjectUtil.checkNotNull(cache, "cache"));
/*    */   }
/*    */   
/*    */   public DnsServerAddressStream get(String hostname)
/*    */   {
/* 45 */     List<? extends DnsCacheEntry> entries = this.cache.get(hostname, EMPTY);
/* 46 */     if ((entries == null) || (entries.isEmpty())) {
/* 47 */       return null;
/*    */     }
/* 49 */     if (((DnsCacheEntry)entries.get(0)).cause() != null) {
/* 50 */       return null;
/*    */     }
/*    */     
/* 53 */     List<InetSocketAddress> addresses = new ArrayList(entries.size());
/*    */     
/* 55 */     int i = 0;
/*    */     do {
/* 57 */       InetAddress addr = ((DnsCacheEntry)entries.get(i)).address();
/* 58 */       addresses.add(new InetSocketAddress(addr, 53));
/* 59 */       i++; } while (i < entries.size());
/* 60 */     return new SequentialDnsServerAddressStream(addresses, 0);
/*    */   }
/*    */   
/*    */ 
/*    */   public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop)
/*    */   {
/* 66 */     if (!address.isUnresolved()) {
/* 67 */       this.cache.cache(hostname, EMPTY, address.getAddress(), originalTtl, loop);
/*    */     }
/*    */   }
/*    */   
/*    */   public void clear()
/*    */   {
/* 73 */     this.cache.clear();
/*    */   }
/*    */   
/*    */   public boolean clear(String hostname)
/*    */   {
/* 78 */     return this.cache.clear(hostname);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\AuthoritativeDnsServerCacheAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */