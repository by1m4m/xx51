/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.Collections;
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
/*    */ final class ShuffledDnsServerAddressStream
/*    */   implements DnsServerAddressStream
/*    */ {
/*    */   private final List<InetSocketAddress> addresses;
/*    */   private int i;
/*    */   
/*    */   ShuffledDnsServerAddressStream(List<InetSocketAddress> addresses)
/*    */   {
/* 37 */     this.addresses = addresses;
/*    */     
/* 39 */     shuffle();
/*    */   }
/*    */   
/*    */   private ShuffledDnsServerAddressStream(List<InetSocketAddress> addresses, int startIdx) {
/* 43 */     this.addresses = addresses;
/* 44 */     this.i = startIdx;
/*    */   }
/*    */   
/*    */   private void shuffle() {
/* 48 */     Collections.shuffle(this.addresses, PlatformDependent.threadLocalRandom());
/*    */   }
/*    */   
/*    */   public InetSocketAddress next()
/*    */   {
/* 53 */     int i = this.i;
/* 54 */     InetSocketAddress next = (InetSocketAddress)this.addresses.get(i);
/* 55 */     i++; if (i < this.addresses.size()) {
/* 56 */       this.i = i;
/*    */     } else {
/* 58 */       this.i = 0;
/* 59 */       shuffle();
/*    */     }
/* 61 */     return next;
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 66 */     return this.addresses.size();
/*    */   }
/*    */   
/*    */   public ShuffledDnsServerAddressStream duplicate()
/*    */   {
/* 71 */     return new ShuffledDnsServerAddressStream(this.addresses, this.i);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 76 */     return SequentialDnsServerAddressStream.toString("shuffled", this.i, this.addresses);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\ShuffledDnsServerAddressStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */