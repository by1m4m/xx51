/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class NoopAuthoritativeDnsServerCache
/*    */   implements AuthoritativeDnsServerCache
/*    */ {
/* 30 */   public static final NoopAuthoritativeDnsServerCache INSTANCE = new NoopAuthoritativeDnsServerCache();
/*    */   
/*    */ 
/*    */ 
/*    */   public DnsServerAddressStream get(String hostname)
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void clear() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean clear(String hostname)
/*    */   {
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\NoopAuthoritativeDnsServerCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */