/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.Collection;
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
/*    */ final class SequentialDnsServerAddressStream
/*    */   implements DnsServerAddressStream
/*    */ {
/*    */   private final List<? extends InetSocketAddress> addresses;
/*    */   private int i;
/*    */   
/*    */   SequentialDnsServerAddressStream(List<? extends InetSocketAddress> addresses, int startIdx)
/*    */   {
/* 29 */     this.addresses = addresses;
/* 30 */     this.i = startIdx;
/*    */   }
/*    */   
/*    */   public InetSocketAddress next()
/*    */   {
/* 35 */     int i = this.i;
/* 36 */     InetSocketAddress next = (InetSocketAddress)this.addresses.get(i);
/* 37 */     i++; if (i < this.addresses.size()) {
/* 38 */       this.i = i;
/*    */     } else {
/* 40 */       this.i = 0;
/*    */     }
/* 42 */     return next;
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 47 */     return this.addresses.size();
/*    */   }
/*    */   
/*    */   public SequentialDnsServerAddressStream duplicate()
/*    */   {
/* 52 */     return new SequentialDnsServerAddressStream(this.addresses, this.i);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 57 */     return toString("sequential", this.i, this.addresses);
/*    */   }
/*    */   
/*    */   static String toString(String type, int index, Collection<? extends InetSocketAddress> addresses) {
/* 61 */     StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.size() * 16);
/* 62 */     buf.append(type).append("(index: ").append(index);
/* 63 */     buf.append(", addrs: (");
/* 64 */     for (InetSocketAddress a : addresses) {
/* 65 */       buf.append(a).append(", ");
/*    */     }
/*    */     
/* 68 */     buf.setLength(buf.length() - 2);
/* 69 */     buf.append("))");
/*    */     
/* 71 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\SequentialDnsServerAddressStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */