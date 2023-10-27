/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.channel.CombinedChannelDuplexHandler;
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
/*    */ public final class SpdyHttpCodec
/*    */   extends CombinedChannelDuplexHandler<SpdyHttpDecoder, SpdyHttpEncoder>
/*    */ {
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength)
/*    */   {
/* 28 */     super(new SpdyHttpDecoder(version, maxContentLength), new SpdyHttpEncoder(version));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength, boolean validateHttpHeaders)
/*    */   {
/* 35 */     super(new SpdyHttpDecoder(version, maxContentLength, validateHttpHeaders), new SpdyHttpEncoder(version));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\spdy\SpdyHttpCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */