/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ public class DefaultAuthoritativeDnsServerCache
/*     */   implements AuthoritativeDnsServerCache
/*     */ {
/*     */   private final int minTtl;
/*     */   private final int maxTtl;
/*     */   private final Comparator<InetSocketAddress> comparator;
/*  39 */   private final Cache<InetSocketAddress> resolveCache = new Cache()
/*     */   {
/*     */     protected boolean shouldReplaceAll(InetSocketAddress entry) {
/*  42 */       return false;
/*     */     }
/*     */     
/*     */     protected boolean equals(InetSocketAddress entry, InetSocketAddress otherEntry)
/*     */     {
/*  47 */       if (PlatformDependent.javaVersion() >= 7) {
/*  48 */         return entry.getHostString().equalsIgnoreCase(otherEntry.getHostString());
/*     */       }
/*  50 */       return entry.getHostName().equalsIgnoreCase(otherEntry.getHostName());
/*     */     }
/*     */     
/*     */     protected void sortEntries(String hostname, List<InetSocketAddress> entries)
/*     */     {
/*  55 */       if (DefaultAuthoritativeDnsServerCache.this.comparator != null) {
/*  56 */         Collections.sort(entries, DefaultAuthoritativeDnsServerCache.this.comparator);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultAuthoritativeDnsServerCache()
/*     */   {
/*  65 */     this(0, Cache.MAX_SUPPORTED_TTL_SECS, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultAuthoritativeDnsServerCache(int minTtl, int maxTtl, Comparator<InetSocketAddress> comparator)
/*     */   {
/*  77 */     this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
/*  78 */     this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositive(maxTtl, "maxTtl"));
/*  79 */     if (minTtl > maxTtl) {
/*  80 */       throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
/*     */     }
/*     */     
/*  83 */     this.comparator = comparator;
/*     */   }
/*     */   
/*     */ 
/*     */   public DnsServerAddressStream get(String hostname)
/*     */   {
/*  89 */     ObjectUtil.checkNotNull(hostname, "hostname");
/*     */     
/*  91 */     List<? extends InetSocketAddress> addresses = this.resolveCache.get(hostname);
/*  92 */     if ((addresses == null) || (addresses.isEmpty())) {
/*  93 */       return null;
/*     */     }
/*  95 */     return new SequentialDnsServerAddressStream(addresses, 0);
/*     */   }
/*     */   
/*     */   public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop)
/*     */   {
/* 100 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 101 */     ObjectUtil.checkNotNull(address, "address");
/* 102 */     ObjectUtil.checkNotNull(loop, "loop");
/*     */     
/* 104 */     if ((PlatformDependent.javaVersion() >= 7) && (address.getHostString() == null))
/*     */     {
/*     */ 
/* 107 */       return;
/*     */     }
/*     */     
/* 110 */     this.resolveCache.cache(hostname, address, Math.max(this.minTtl, (int)Math.min(this.maxTtl, originalTtl)), loop);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 115 */     this.resolveCache.clear();
/*     */   }
/*     */   
/*     */   public boolean clear(String hostname)
/*     */   {
/* 120 */     ObjectUtil.checkNotNull(hostname, "hostname");
/*     */     
/* 122 */     return this.resolveCache.clear(hostname);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 127 */     return 
/* 128 */       "DefaultAuthoritativeDnsServerCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", cached nameservers=" + this.resolveCache.size() + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DefaultAuthoritativeDnsServerCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */