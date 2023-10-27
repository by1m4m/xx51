/*    */ package io.netty.channel;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractServerChannel
/*    */   extends AbstractChannel
/*    */   implements ServerChannel
/*    */ {
/* 32 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*    */   
/*    */ 
/*    */ 
/*    */   protected AbstractServerChannel()
/*    */   {
/* 38 */     super(null);
/*    */   }
/*    */   
/*    */   public ChannelMetadata metadata()
/*    */   {
/* 43 */     return METADATA;
/*    */   }
/*    */   
/*    */   public SocketAddress remoteAddress()
/*    */   {
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   protected SocketAddress remoteAddress0()
/*    */   {
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   protected void doDisconnect() throws Exception
/*    */   {
/* 58 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   protected AbstractChannel.AbstractUnsafe newUnsafe()
/*    */   {
/* 63 */     return new DefaultServerUnsafe(null);
/*    */   }
/*    */   
/*    */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*    */   {
/* 68 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 73 */   protected final Object filterOutboundMessage(Object msg) { throw new UnsupportedOperationException(); }
/*    */   
/*    */   private final class DefaultServerUnsafe extends AbstractChannel.AbstractUnsafe {
/* 76 */     private DefaultServerUnsafe() { super(); }
/*    */     
/*    */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 79 */       safeSetFailure(promise, new UnsupportedOperationException());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\AbstractServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */