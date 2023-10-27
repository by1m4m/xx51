/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.channel.ChannelDuplexHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public abstract class Http2ChannelDuplexHandler
/*    */   extends ChannelDuplexHandler
/*    */ {
/*    */   private volatile Http2FrameCodec frameCodec;
/*    */   
/*    */   public final void handlerAdded(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {
/* 42 */     this.frameCodec = requireHttp2FrameCodec(ctx);
/* 43 */     handlerAdded0(ctx);
/*    */   }
/*    */   
/*    */   protected void handlerAdded0(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   /* Error */
/*    */   public final void handlerRemoved(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: invokevirtual 5	io/netty/handler/codec/http2/Http2ChannelDuplexHandler:handlerRemoved0	(Lio/netty/channel/ChannelHandlerContext;)V
/*    */     //   5: aload_0
/*    */     //   6: aconst_null
/*    */     //   7: putfield 3	io/netty/handler/codec/http2/Http2ChannelDuplexHandler:frameCodec	Lio/netty/handler/codec/http2/Http2FrameCodec;
/*    */     //   10: goto +11 -> 21
/*    */     //   13: astore_2
/*    */     //   14: aload_0
/*    */     //   15: aconst_null
/*    */     //   16: putfield 3	io/netty/handler/codec/http2/Http2ChannelDuplexHandler:frameCodec	Lio/netty/handler/codec/http2/Http2FrameCodec;
/*    */     //   19: aload_2
/*    */     //   20: athrow
/*    */     //   21: return
/*    */     // Line number table:
/*    */     //   Java source line #53	-> byte code offset #0
/*    */     //   Java source line #55	-> byte code offset #5
/*    */     //   Java source line #56	-> byte code offset #10
/*    */     //   Java source line #55	-> byte code offset #13
/*    */     //   Java source line #56	-> byte code offset #19
/*    */     //   Java source line #57	-> byte code offset #21
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	22	0	this	Http2ChannelDuplexHandler
/*    */     //   0	22	1	ctx	ChannelHandlerContext
/*    */     //   13	7	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	5	13	finally
/*    */   }
/*    */   
/*    */   protected void handlerRemoved0(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public final Http2FrameStream newStream()
/*    */   {
/* 69 */     Http2FrameCodec codec = this.frameCodec;
/* 70 */     if (codec == null) {
/* 71 */       throw new IllegalStateException(StringUtil.simpleClassName(Http2FrameCodec.class) + " not found. Has the handler been added to a pipeline?");
/*    */     }
/*    */     
/* 74 */     return codec.newStream();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final void forEachActiveStream(Http2FrameStreamVisitor streamVisitor)
/*    */     throws Http2Exception
/*    */   {
/* 83 */     this.frameCodec.forEachActiveStream(streamVisitor);
/*    */   }
/*    */   
/*    */   private static Http2FrameCodec requireHttp2FrameCodec(ChannelHandlerContext ctx) {
/* 87 */     ChannelHandlerContext frameCodecCtx = ctx.pipeline().context(Http2FrameCodec.class);
/* 88 */     if (frameCodecCtx == null) {
/* 89 */       throw new IllegalArgumentException(Http2FrameCodec.class.getSimpleName() + " was not found in the channel pipeline.");
/*    */     }
/*    */     
/* 92 */     return (Http2FrameCodec)frameCodecCtx.handler();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2ChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */