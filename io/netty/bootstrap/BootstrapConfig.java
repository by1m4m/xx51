/*    */ package io.netty.bootstrap;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.resolver.AddressResolverGroup;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class BootstrapConfig
/*    */   extends AbstractBootstrapConfig<Bootstrap, Channel>
/*    */ {
/*    */   BootstrapConfig(Bootstrap bootstrap)
/*    */   {
/* 29 */     super(bootstrap);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SocketAddress remoteAddress()
/*    */   {
/* 36 */     return ((Bootstrap)this.bootstrap).remoteAddress();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public AddressResolverGroup<?> resolver()
/*    */   {
/* 43 */     return ((Bootstrap)this.bootstrap).resolver();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     StringBuilder buf = new StringBuilder(super.toString());
/* 49 */     buf.setLength(buf.length() - 1);
/* 50 */     buf.append(", resolver: ").append(resolver());
/* 51 */     SocketAddress remoteAddress = remoteAddress();
/* 52 */     if (remoteAddress != null)
/*    */     {
/* 54 */       buf.append(", remoteAddress: ").append(remoteAddress);
/*    */     }
/* 56 */     return ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\bootstrap\BootstrapConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */