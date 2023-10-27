/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.util.NetUtil;
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
/*    */ public class SocksCmdResponseDecoder
/*    */   extends ReplayingDecoder<State>
/*    */ {
/*    */   private SocksCmdStatus cmdStatus;
/*    */   private SocksAddressType addressType;
/*    */   
/*    */   public SocksCmdResponseDecoder()
/*    */   {
/* 36 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception
/*    */   {
/* 41 */     switch ((State)state()) {
/*    */     case CHECK_PROTOCOL_VERSION: 
/* 43 */       if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
/* 44 */         out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/*    */       }
/*    */       else
/* 47 */         checkpoint(State.READ_CMD_HEADER);
/*    */       break;
/*    */     case READ_CMD_HEADER: 
/* 50 */       this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
/* 51 */       byteBuf.skipBytes(1);
/* 52 */       this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
/* 53 */       checkpoint(State.READ_CMD_ADDRESS);
/*    */     
/*    */     case READ_CMD_ADDRESS: 
/* 56 */       switch (this.addressType) {
/*    */       case IPv4: 
/* 58 */         String host = NetUtil.intToIpAddress(byteBuf.readInt());
/* 59 */         int port = byteBuf.readUnsignedShort();
/* 60 */         out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
/* 61 */         break;
/*    */       
/*    */       case DOMAIN: 
/* 64 */         int fieldLength = byteBuf.readByte();
/* 65 */         String host = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
/* 66 */         int port = byteBuf.readUnsignedShort();
/* 67 */         out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
/* 68 */         break;
/*    */       
/*    */       case IPv6: 
/* 71 */         byte[] bytes = new byte[16];
/* 72 */         byteBuf.readBytes(bytes);
/* 73 */         String host = SocksCommonUtils.ipv6toStr(bytes);
/* 74 */         int port = byteBuf.readUnsignedShort();
/* 75 */         out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
/* 76 */         break;
/*    */       
/*    */       case UNKNOWN: 
/* 79 */         out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
/* 80 */         break;
/*    */       
/*    */       default: 
/* 83 */         throw new Error();
/*    */       }
/*    */       
/*    */       
/*    */       break;
/*    */     default: 
/* 89 */       throw new Error();
/*    */     }
/*    */     
/* 92 */     ctx.pipeline().remove(this);
/*    */   }
/*    */   
/*    */   static enum State {
/* 96 */     CHECK_PROTOCOL_VERSION, 
/* 97 */     READ_CMD_HEADER, 
/* 98 */     READ_CMD_ADDRESS;
/*    */     
/*    */     private State() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksCmdResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */