/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DnsServerAddresses
/*     */ {
/*     */   @Deprecated
/*     */   public static List<InetSocketAddress> defaultAddressList()
/*     */   {
/*  41 */     return DefaultDnsServerAddressStreamProvider.defaultAddressList();
/*     */   }
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
/*     */   @Deprecated
/*     */   public static DnsServerAddresses defaultAddresses()
/*     */   {
/*  59 */     return DefaultDnsServerAddressStreamProvider.defaultAddresses();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses sequential(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/*  67 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses sequential(InetSocketAddress... addresses)
/*     */   {
/*  75 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses sequential0(List<InetSocketAddress> addresses) {
/*  79 */     if (addresses.size() == 1) {
/*  80 */       return singleton((InetSocketAddress)addresses.get(0));
/*     */     }
/*     */     
/*  83 */     new DefaultDnsServerAddresses("sequential", addresses)
/*     */     {
/*     */       public DnsServerAddressStream stream() {
/*  86 */         return new SequentialDnsServerAddressStream(this.addresses, 0);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses shuffled(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/*  96 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses shuffled(InetSocketAddress... addresses)
/*     */   {
/* 104 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses shuffled0(List<InetSocketAddress> addresses) {
/* 108 */     if (addresses.size() == 1) {
/* 109 */       return singleton((InetSocketAddress)addresses.get(0));
/*     */     }
/*     */     
/* 112 */     new DefaultDnsServerAddresses("shuffled", addresses)
/*     */     {
/*     */       public DnsServerAddressStream stream() {
/* 115 */         return new ShuffledDnsServerAddressStream(this.addresses);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses rotational(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/* 127 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses rotational(InetSocketAddress... addresses)
/*     */   {
/* 137 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static DnsServerAddresses rotational0(List<InetSocketAddress> addresses) {
/* 141 */     if (addresses.size() == 1) {
/* 142 */       return singleton((InetSocketAddress)addresses.get(0));
/*     */     }
/*     */     
/* 145 */     return new RotationalDnsServerAddresses(addresses);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static DnsServerAddresses singleton(InetSocketAddress address)
/*     */   {
/* 152 */     if (address == null) {
/* 153 */       throw new NullPointerException("address");
/*     */     }
/* 155 */     if (address.isUnresolved()) {
/* 156 */       throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + address);
/*     */     }
/*     */     
/* 159 */     return new SingletonDnsServerAddresses(address);
/*     */   }
/*     */   
/*     */   private static List<InetSocketAddress> sanitize(Iterable<? extends InetSocketAddress> addresses) {
/* 163 */     if (addresses == null) {
/* 164 */       throw new NullPointerException("addresses");
/*     */     }
/*     */     List<InetSocketAddress> list;
/*     */     List<InetSocketAddress> list;
/* 168 */     if ((addresses instanceof Collection)) {
/* 169 */       list = new ArrayList(((Collection)addresses).size());
/*     */     } else {
/* 171 */       list = new ArrayList(4);
/*     */     }
/*     */     
/* 174 */     for (InetSocketAddress a : addresses) {
/* 175 */       if (a == null) {
/*     */         break;
/*     */       }
/* 178 */       if (a.isUnresolved()) {
/* 179 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 181 */       list.add(a);
/*     */     }
/*     */     
/* 184 */     if (list.isEmpty()) {
/* 185 */       throw new IllegalArgumentException("empty addresses");
/*     */     }
/*     */     
/* 188 */     return list;
/*     */   }
/*     */   
/*     */   private static List<InetSocketAddress> sanitize(InetSocketAddress[] addresses) {
/* 192 */     if (addresses == null) {
/* 193 */       throw new NullPointerException("addresses");
/*     */     }
/*     */     
/* 196 */     List<InetSocketAddress> list = new ArrayList(addresses.length);
/* 197 */     for (InetSocketAddress a : addresses) {
/* 198 */       if (a == null) {
/*     */         break;
/*     */       }
/* 201 */       if (a.isUnresolved()) {
/* 202 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 204 */       list.add(a);
/*     */     }
/*     */     
/* 207 */     if (list.isEmpty()) {
/* 208 */       return DefaultDnsServerAddressStreamProvider.defaultAddressList();
/*     */     }
/*     */     
/* 211 */     return list;
/*     */   }
/*     */   
/*     */   public abstract DnsServerAddressStream stream();
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */