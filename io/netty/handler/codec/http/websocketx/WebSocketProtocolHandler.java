/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
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
/*    */ abstract class WebSocketProtocolHandler
/*    */   extends MessageToMessageDecoder<WebSocketFrame>
/*    */ {
/*    */   private final boolean dropPongFrames;
/*    */   
/*    */   WebSocketProtocolHandler()
/*    */   {
/* 32 */     this(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   WebSocketProtocolHandler(boolean dropPongFrames)
/*    */   {
/* 43 */     this.dropPongFrames = dropPongFrames;
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception
/*    */   {
/* 48 */     if ((frame instanceof PingWebSocketFrame)) {
/* 49 */       frame.content().retain();
/* 50 */       ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content()));
/* 51 */       return;
/*    */     }
/* 53 */     if (((frame instanceof PongWebSocketFrame)) && (this.dropPongFrames)) {
/* 54 */       return;
/*    */     }
/*    */     
/* 57 */     out.add(frame.retain());
/*    */   }
/*    */   
/*    */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*    */   {
/* 62 */     ctx.fireExceptionCaught(cause);
/* 63 */     ctx.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketProtocolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */