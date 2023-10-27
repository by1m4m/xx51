/*    */ package io.netty.resolver.dns;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public final class MultiDnsServerAddressStreamProvider
/*    */   implements DnsServerAddressStreamProvider
/*    */ {
/*    */   private final DnsServerAddressStreamProvider[] providers;
/*    */   
/*    */   public MultiDnsServerAddressStreamProvider(List<DnsServerAddressStreamProvider> providers)
/*    */   {
/* 35 */     this.providers = ((DnsServerAddressStreamProvider[])providers.toArray(new DnsServerAddressStreamProvider[0]));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public MultiDnsServerAddressStreamProvider(DnsServerAddressStreamProvider... providers)
/*    */   {
/* 43 */     this.providers = ((DnsServerAddressStreamProvider[])providers.clone());
/*    */   }
/*    */   
/*    */   public DnsServerAddressStream nameServerAddressStream(String hostname)
/*    */   {
/* 48 */     for (DnsServerAddressStreamProvider provider : this.providers) {
/* 49 */       DnsServerAddressStream stream = provider.nameServerAddressStream(hostname);
/* 50 */       if (stream != null) {
/* 51 */         return stream;
/*    */       }
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\MultiDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */