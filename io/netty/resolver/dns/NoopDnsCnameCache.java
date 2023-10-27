/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
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
/*    */ public final class NoopDnsCnameCache
/*    */   implements DnsCnameCache
/*    */ {
/* 24 */   public static final NoopDnsCnameCache INSTANCE = new NoopDnsCnameCache();
/*    */   
/*    */ 
/*    */ 
/*    */   public String get(String hostname)
/*    */   {
/* 30 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void cache(String hostname, String cname, long originalTtl, EventLoop loop) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void clear() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean clear(String hostname)
/*    */   {
/* 45 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\NoopDnsCnameCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */