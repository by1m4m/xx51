/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelMetadata;
/*    */ import io.netty.channel.embedded.EmbeddedChannel;
/*    */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*    */ import io.netty.handler.codec.compression.ZlibWrapper;
/*    */ import io.netty.util.AsciiString;
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
/*    */ public class HttpContentDecompressor
/*    */   extends HttpContentDecoder
/*    */ {
/*    */   private final boolean strict;
/*    */   
/*    */   public HttpContentDecompressor()
/*    */   {
/* 40 */     this(false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpContentDecompressor(boolean strict)
/*    */   {
/* 50 */     this.strict = strict;
/*    */   }
/*    */   
/*    */   protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception
/*    */   {
/* 55 */     if ((HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding)) || 
/* 56 */       (HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding))) {
/* 57 */       return new EmbeddedChannel(this.ctx.channel().id(), this.ctx.channel().metadata().hasDisconnect(), this.ctx
/* 58 */         .channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
/*    */     }
/* 60 */     if ((HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding)) || 
/* 61 */       (HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding))) {
/* 62 */       ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
/*    */       
/* 64 */       return new EmbeddedChannel(this.ctx.channel().id(), this.ctx.channel().metadata().hasDisconnect(), this.ctx
/* 65 */         .channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(wrapper) });
/*    */     }
/*    */     
/*    */ 
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpContentDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */