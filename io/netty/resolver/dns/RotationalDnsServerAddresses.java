/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*    */ final class RotationalDnsServerAddresses
/*    */   extends DefaultDnsServerAddresses
/*    */ {
/* 26 */   private static final AtomicIntegerFieldUpdater<RotationalDnsServerAddresses> startIdxUpdater = AtomicIntegerFieldUpdater.newUpdater(RotationalDnsServerAddresses.class, "startIdx");
/*    */   
/*    */   private volatile int startIdx;
/*    */   
/*    */   RotationalDnsServerAddresses(List<InetSocketAddress> addresses)
/*    */   {
/* 32 */     super("rotational", addresses);
/*    */   }
/*    */   
/*    */   public DnsServerAddressStream stream()
/*    */   {
/*    */     for (;;) {
/* 38 */       int curStartIdx = this.startIdx;
/* 39 */       int nextStartIdx = curStartIdx + 1;
/* 40 */       if (nextStartIdx >= this.addresses.size()) {
/* 41 */         nextStartIdx = 0;
/*    */       }
/* 43 */       if (startIdxUpdater.compareAndSet(this, curStartIdx, nextStartIdx)) {
/* 44 */         return new SequentialDnsServerAddressStream(this.addresses, curStartIdx);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\RotationalDnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */