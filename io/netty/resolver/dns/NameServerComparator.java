/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.io.Serializable;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.Comparator;
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
/*    */ public final class NameServerComparator
/*    */   implements Comparator<InetSocketAddress>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8372151874317596185L;
/*    */   private final Class<? extends InetAddress> preferredAddressType;
/*    */   
/*    */   public NameServerComparator(Class<? extends InetAddress> preferredAddressType)
/*    */   {
/* 42 */     this.preferredAddressType = ((Class)ObjectUtil.checkNotNull(preferredAddressType, "preferredAddressType"));
/*    */   }
/*    */   
/*    */   public int compare(InetSocketAddress addr1, InetSocketAddress addr2)
/*    */   {
/* 47 */     if (addr1.equals(addr2)) {
/* 48 */       return 0;
/*    */     }
/* 50 */     if ((!addr1.isUnresolved()) && (!addr2.isUnresolved())) {
/* 51 */       if (addr1.getAddress().getClass() == addr2.getAddress().getClass()) {
/* 52 */         return 0;
/*    */       }
/* 54 */       return this.preferredAddressType.isAssignableFrom(addr1.getAddress().getClass()) ? -1 : 1;
/*    */     }
/* 56 */     if ((addr1.isUnresolved()) && (addr2.isUnresolved())) {
/* 57 */       return 0;
/*    */     }
/* 59 */     return addr1.isUnresolved() ? 1 : -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\NameServerComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */