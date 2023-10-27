/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultDnsCache
/*     */   implements DnsCache
/*     */ {
/*  38 */   private final Cache<DefaultDnsCacheEntry> resolveCache = new Cache()
/*     */   {
/*     */     protected boolean shouldReplaceAll(DefaultDnsCache.DefaultDnsCacheEntry entry)
/*     */     {
/*  42 */       return entry.cause() != null;
/*     */     }
/*     */     
/*     */     protected boolean equals(DefaultDnsCache.DefaultDnsCacheEntry entry, DefaultDnsCache.DefaultDnsCacheEntry otherEntry)
/*     */     {
/*  47 */       if (entry.address() != null) {
/*  48 */         return entry.address().equals(otherEntry.address());
/*     */       }
/*  50 */       if (otherEntry.address() != null) {
/*  51 */         return false;
/*     */       }
/*  53 */       return entry.cause().equals(otherEntry.cause());
/*     */     }
/*     */   };
/*     */   
/*     */   private final int minTtl;
/*     */   
/*     */   private final int maxTtl;
/*     */   
/*     */   private final int negativeTtl;
/*     */   
/*     */ 
/*     */   public DefaultDnsCache()
/*     */   {
/*  66 */     this(0, Cache.MAX_SUPPORTED_TTL_SECS, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultDnsCache(int minTtl, int maxTtl, int negativeTtl)
/*     */   {
/*  76 */     this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
/*  77 */     this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl"));
/*  78 */     if (minTtl > maxTtl) {
/*  79 */       throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
/*     */     }
/*     */     
/*  82 */     this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int minTtl()
/*     */   {
/*  91 */     return this.minTtl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxTtl()
/*     */   {
/* 100 */     return this.maxTtl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int negativeTtl()
/*     */   {
/* 108 */     return this.negativeTtl;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 113 */     this.resolveCache.clear();
/*     */   }
/*     */   
/*     */   public boolean clear(String hostname)
/*     */   {
/* 118 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 119 */     return this.resolveCache.clear(appendDot(hostname));
/*     */   }
/*     */   
/*     */   private static boolean emptyAdditionals(DnsRecord[] additionals) {
/* 123 */     return (additionals == null) || (additionals.length == 0);
/*     */   }
/*     */   
/*     */   public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals)
/*     */   {
/* 128 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 129 */     if (!emptyAdditionals(additionals)) {
/* 130 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 133 */     return this.resolveCache.get(appendDot(hostname));
/*     */   }
/*     */   
/*     */ 
/*     */   public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, InetAddress address, long originalTtl, EventLoop loop)
/*     */   {
/* 139 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 140 */     ObjectUtil.checkNotNull(address, "address");
/* 141 */     ObjectUtil.checkNotNull(loop, "loop");
/* 142 */     DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, address);
/* 143 */     if ((this.maxTtl == 0) || (!emptyAdditionals(additionals))) {
/* 144 */       return e;
/*     */     }
/* 146 */     this.resolveCache.cache(appendDot(hostname), e, Math.max(this.minTtl, (int)Math.min(this.maxTtl, originalTtl)), loop);
/* 147 */     return e;
/*     */   }
/*     */   
/*     */   public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, Throwable cause, EventLoop loop)
/*     */   {
/* 152 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 153 */     ObjectUtil.checkNotNull(cause, "cause");
/* 154 */     ObjectUtil.checkNotNull(loop, "loop");
/*     */     
/* 156 */     DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, cause);
/* 157 */     if ((this.negativeTtl == 0) || (!emptyAdditionals(additionals))) {
/* 158 */       return e;
/*     */     }
/*     */     
/* 161 */     this.resolveCache.cache(appendDot(hostname), e, this.negativeTtl, loop);
/* 162 */     return e;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 167 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 172 */       "DefaultDnsCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", negativeTtl=" + this.negativeTtl + ", cached resolved hostname=" + this.resolveCache.size() + ')';
/*     */   }
/*     */   
/*     */   private static final class DefaultDnsCacheEntry implements DnsCacheEntry
/*     */   {
/*     */     private final String hostname;
/*     */     private final InetAddress address;
/*     */     private final Throwable cause;
/*     */     
/*     */     DefaultDnsCacheEntry(String hostname, InetAddress address) {
/* 182 */       this.hostname = hostname;
/* 183 */       this.address = address;
/* 184 */       this.cause = null;
/*     */     }
/*     */     
/*     */     DefaultDnsCacheEntry(String hostname, Throwable cause) {
/* 188 */       this.hostname = hostname;
/* 189 */       this.cause = cause;
/* 190 */       this.address = null;
/*     */     }
/*     */     
/*     */     public InetAddress address()
/*     */     {
/* 195 */       return this.address;
/*     */     }
/*     */     
/*     */     public Throwable cause()
/*     */     {
/* 200 */       return this.cause;
/*     */     }
/*     */     
/*     */     String hostname() {
/* 204 */       return this.hostname;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 209 */       if (this.cause != null) {
/* 210 */         return this.hostname + '/' + this.cause;
/*     */       }
/* 212 */       return this.address.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static String appendDot(String hostname)
/*     */   {
/* 218 */     return hostname + '.';
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DefaultDnsCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */