/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import java.util.ArrayList;
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
/*    */ public class SocksInitRequestDecoder
/*    */   extends ReplayingDecoder<State>
/*    */ {
/*    */   public SocksInitRequestDecoder()
/*    */   {
/* 34 */     super(State.CHECK_PROTOCOL_VERSION);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception
/*    */   {
/* 39 */     switch ((State)state()) {
/*    */     case CHECK_PROTOCOL_VERSION: 
/* 41 */       if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
/* 42 */         out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
/*    */       }
/*    */       else
/* 45 */         checkpoint(State.READ_AUTH_SCHEMES);
/*    */       break;
/*    */     case READ_AUTH_SCHEMES: 
/* 48 */       byte authSchemeNum = byteBuf.readByte();
/*    */       List<SocksAuthScheme> authSchemes;
/* 50 */       if (authSchemeNum > 0) {
/* 51 */         List<SocksAuthScheme> authSchemes = new ArrayList(authSchemeNum);
/* 52 */         for (int i = 0; i < authSchemeNum; i++) {
/* 53 */           authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte()));
/*    */         }
/*    */       } else {
/* 56 */         authSchemes = Collections.emptyList();
/*    */       }
/* 58 */       out.add(new SocksInitRequest(authSchemes));
/* 59 */       break;
/*    */     
/*    */     default: 
/* 62 */       throw new Error();
/*    */     }
/*    */     
/* 65 */     ctx.pipeline().remove(this);
/*    */   }
/*    */   
/*    */   static enum State {
/* 69 */     CHECK_PROTOCOL_VERSION, 
/* 70 */     READ_AUTH_SCHEMES;
/*    */     
/*    */     private State() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksInitRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */