/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ public abstract interface Http2PromisedRequestVerifier
/*    */ {
/* 58 */   public static final Http2PromisedRequestVerifier ALWAYS_VERIFY = new Http2PromisedRequestVerifier()
/*    */   {
/*    */     public boolean isAuthoritative(ChannelHandlerContext ctx, Http2Headers headers) {
/* 61 */       return true;
/*    */     }
/*    */     
/*    */     public boolean isCacheable(Http2Headers headers)
/*    */     {
/* 66 */       return true;
/*    */     }
/*    */     
/*    */     public boolean isSafe(Http2Headers headers)
/*    */     {
/* 71 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean isAuthoritative(ChannelHandlerContext paramChannelHandlerContext, Http2Headers paramHttp2Headers);
/*    */   
/*    */   public abstract boolean isCacheable(Http2Headers paramHttp2Headers);
/*    */   
/*    */   public abstract boolean isSafe(Http2Headers paramHttp2Headers);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2PromisedRequestVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */