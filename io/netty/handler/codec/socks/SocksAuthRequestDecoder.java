/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
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
/*    */ public class SocksAuthRequestDecoder
/*    */   extends ReplayingDecoder<State>
/*    */ {
/*    */   private String username;
/*    */   
/*    */   public SocksAuthRequestDecoder()
/*    */   {
/* 34 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception
/*    */   {
/* 39 */     switch ((State)state()) {
/*    */     case CHECK_PROTOCOL_VERSION: 
/* 41 */       if (byteBuf.readByte() != SocksSubnegotiationVersion.AUTH_PASSWORD.byteValue()) {
/* 42 */         out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
/*    */       }
/*    */       else
/* 45 */         checkpoint(State.READ_USERNAME);
/*    */       break;
/*    */     case READ_USERNAME: 
/* 48 */       int fieldLength = byteBuf.readByte();
/* 49 */       this.username = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
/* 50 */       checkpoint(State.READ_PASSWORD);
/*    */     
/*    */     case READ_PASSWORD: 
/* 53 */       int fieldLength = byteBuf.readByte();
/* 54 */       String password = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
/* 55 */       out.add(new SocksAuthRequest(this.username, password));
/* 56 */       break;
/*    */     
/*    */     default: 
/* 59 */       throw new Error();
/*    */     }
/*    */     
/* 62 */     ctx.pipeline().remove(this);
/*    */   }
/*    */   
/*    */   static enum State {
/* 66 */     CHECK_PROTOCOL_VERSION, 
/* 67 */     READ_USERNAME, 
/* 68 */     READ_PASSWORD;
/*    */     
/*    */     private State() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksAuthRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */