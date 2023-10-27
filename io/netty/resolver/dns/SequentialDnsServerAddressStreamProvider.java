/*    */ package io.netty.resolver.dns;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SequentialDnsServerAddressStreamProvider
/*    */   extends UniSequentialDnsServerAddressStreamProvider
/*    */ {
/*    */   public SequentialDnsServerAddressStreamProvider(InetSocketAddress... addresses)
/*    */   {
/* 35 */     super(DnsServerAddresses.sequential(addresses));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SequentialDnsServerAddressStreamProvider(Iterable<? extends InetSocketAddress> addresses)
/*    */   {
/* 44 */     super(DnsServerAddresses.sequential(addresses));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\SequentialDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */