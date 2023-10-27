/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import java.net.InetSocketAddress;
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
/*    */ abstract class DefaultDnsServerAddresses
/*    */   extends DnsServerAddresses
/*    */ {
/*    */   protected final List<InetSocketAddress> addresses;
/*    */   private final String strVal;
/*    */   
/*    */   DefaultDnsServerAddresses(String type, List<InetSocketAddress> addresses)
/*    */   {
/* 28 */     this.addresses = addresses;
/*    */     
/* 30 */     StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.size() * 16);
/* 31 */     buf.append(type).append('(');
/*    */     
/* 33 */     for (InetSocketAddress a : addresses) {
/* 34 */       buf.append(a).append(", ");
/*    */     }
/*    */     
/* 37 */     buf.setLength(buf.length() - 2);
/* 38 */     buf.append(')');
/*    */     
/* 40 */     this.strVal = buf.toString();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return this.strVal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DefaultDnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */