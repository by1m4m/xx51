/*    */ package io.netty.handler.codec.socks;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public final class UnknownSocksResponse
/*    */   extends SocksResponse
/*    */ {
/*    */   public UnknownSocksResponse()
/*    */   {
/* 30 */     super(SocksResponseType.UNKNOWN);
/*    */   }
/*    */   
/*    */   public void encodeAsByteBuf(ByteBuf byteBuf) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\UnknownSocksResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */