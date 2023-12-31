/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class DeflateEncoder
/*     */   extends WebSocketExtensionEncoder
/*     */ {
/*     */   private final int compressionLevel;
/*     */   private final int windowSize;
/*     */   private final boolean noContext;
/*     */   private EmbeddedChannel encoder;
/*     */   
/*     */   public DeflateEncoder(int compressionLevel, int windowSize, boolean noContext)
/*     */   {
/*  53 */     this.compressionLevel = compressionLevel;
/*  54 */     this.windowSize = windowSize;
/*  55 */     this.noContext = noContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int rsv(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean removeFrameTail(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */ 
/*     */ 
/*     */   protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  73 */     if (this.encoder == null) {
/*  74 */       this.encoder = new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(ZlibWrapper.NONE, this.compressionLevel, this.windowSize, 8) });
/*     */     }
/*     */     
/*     */ 
/*  78 */     this.encoder.writeOutbound(new Object[] { msg.content().retain() });
/*     */     
/*  80 */     CompositeByteBuf fullCompressedContent = ctx.alloc().compositeBuffer();
/*     */     for (;;) {
/*  82 */       ByteBuf partCompressedContent = (ByteBuf)this.encoder.readOutbound();
/*  83 */       if (partCompressedContent == null) {
/*     */         break;
/*     */       }
/*  86 */       if (!partCompressedContent.isReadable()) {
/*  87 */         partCompressedContent.release();
/*     */       }
/*     */       else
/*  90 */         fullCompressedContent.addComponent(true, partCompressedContent);
/*     */     }
/*  92 */     if (fullCompressedContent.numComponents() <= 0) {
/*  93 */       fullCompressedContent.release();
/*  94 */       throw new CodecException("cannot read compressed buffer");
/*     */     }
/*     */     
/*  97 */     if ((msg.isFinalFragment()) && (this.noContext)) {
/*  98 */       cleanup();
/*     */     }
/*     */     ByteBuf compressedContent;
/*     */     ByteBuf compressedContent;
/* 102 */     if (removeFrameTail(msg)) {
/* 103 */       int realLength = fullCompressedContent.readableBytes() - PerMessageDeflateDecoder.FRAME_TAIL.length;
/* 104 */       compressedContent = fullCompressedContent.slice(0, realLength);
/*     */     } else {
/* 106 */       compressedContent = fullCompressedContent;
/*     */     }
/*     */     
/*     */     WebSocketFrame outMsg;
/* 110 */     if ((msg instanceof TextWebSocketFrame)) {
/* 111 */       outMsg = new TextWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent); } else { WebSocketFrame outMsg;
/* 112 */       if ((msg instanceof BinaryWebSocketFrame)) {
/* 113 */         outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent); } else { WebSocketFrame outMsg;
/* 114 */         if ((msg instanceof ContinuationWebSocketFrame)) {
/* 115 */           outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent);
/*     */         } else
/* 117 */           throw new CodecException("unexpected frame type: " + msg.getClass().getName()); } }
/*     */     WebSocketFrame outMsg;
/* 119 */     out.add(outMsg);
/*     */   }
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 124 */     cleanup();
/* 125 */     super.handlerRemoved(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 129 */     if (this.encoder != null)
/*     */     {
/* 131 */       if (this.encoder.finish()) {
/*     */         for (;;) {
/* 133 */           ByteBuf buf = (ByteBuf)this.encoder.readOutbound();
/* 134 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 138 */           buf.release();
/*     */         }
/*     */       }
/* 141 */       this.encoder = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */