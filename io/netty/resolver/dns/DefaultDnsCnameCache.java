/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.util.AsciiString;
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public final class DefaultDnsCnameCache
/*    */   implements DnsCnameCache
/*    */ {
/*    */   private final int minTtl;
/*    */   private final int maxTtl;
/* 34 */   private final Cache<String> cache = new Cache()
/*    */   {
/*    */     protected boolean shouldReplaceAll(String entry)
/*    */     {
/* 38 */       return true;
/*    */     }
/*    */     
/*    */     protected boolean equals(String entry, String otherEntry)
/*    */     {
/* 43 */       return AsciiString.contentEqualsIgnoreCase(entry, otherEntry);
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */   public DefaultDnsCnameCache()
/*    */   {
/* 51 */     this(0, Cache.MAX_SUPPORTED_TTL_SECS);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultDnsCnameCache(int minTtl, int maxTtl)
/*    */   {
/* 61 */     this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
/* 62 */     this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositive(maxTtl, "maxTtl"));
/* 63 */     if (minTtl > maxTtl) {
/* 64 */       throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String get(String hostname)
/*    */   {
/* 72 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 73 */     List<? extends String> cached = this.cache.get(hostname);
/* 74 */     if ((cached == null) || (cached.isEmpty())) {
/* 75 */       return null;
/*    */     }
/*    */     
/* 78 */     return (String)cached.get(0);
/*    */   }
/*    */   
/*    */   public void cache(String hostname, String cname, long originalTtl, EventLoop loop)
/*    */   {
/* 83 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 84 */     ObjectUtil.checkNotNull(cname, "cname");
/* 85 */     ObjectUtil.checkNotNull(loop, "loop");
/* 86 */     this.cache.cache(hostname, cname, Math.max(this.minTtl, (int)Math.min(this.maxTtl, originalTtl)), loop);
/*    */   }
/*    */   
/*    */   public void clear()
/*    */   {
/* 91 */     this.cache.clear();
/*    */   }
/*    */   
/*    */   public boolean clear(String hostname)
/*    */   {
/* 96 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 97 */     return this.cache.clear(hostname);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DefaultDnsCnameCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */