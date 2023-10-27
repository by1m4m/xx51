/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import io.netty.handler.codec.MessageToMessageCodec;
/*     */ import io.netty.handler.codec.http.DefaultHttpContent;
/*     */ import io.netty.handler.codec.http.DefaultLastHttpContent;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpObject;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpScheme;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import io.netty.handler.ssl.SslHandler;
/*     */ import io.netty.util.AsciiString;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class Http2StreamFrameToHttpObjectCodec
/*     */   extends MessageToMessageCodec<Http2StreamFrame, HttpObject>
/*     */ {
/*     */   private final boolean isServer;
/*     */   private final boolean validateHeaders;
/*     */   private HttpScheme scheme;
/*     */   
/*     */   public Http2StreamFrameToHttpObjectCodec(boolean isServer, boolean validateHeaders)
/*     */   {
/*  67 */     this.isServer = isServer;
/*  68 */     this.validateHeaders = validateHeaders;
/*  69 */     this.scheme = HttpScheme.HTTP;
/*     */   }
/*     */   
/*     */   public Http2StreamFrameToHttpObjectCodec(boolean isServer) {
/*  73 */     this(isServer, true);
/*     */   }
/*     */   
/*     */   public boolean acceptInboundMessage(Object msg) throws Exception
/*     */   {
/*  78 */     return ((msg instanceof Http2HeadersFrame)) || ((msg instanceof Http2DataFrame));
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, Http2StreamFrame frame, List<Object> out) throws Exception
/*     */   {
/*  83 */     if ((frame instanceof Http2HeadersFrame)) {
/*  84 */       Http2HeadersFrame headersFrame = (Http2HeadersFrame)frame;
/*  85 */       Http2Headers headers = headersFrame.headers();
/*  86 */       Http2FrameStream stream = headersFrame.stream();
/*  87 */       int id = stream == null ? 0 : stream.id();
/*     */       
/*  89 */       CharSequence status = headers.status();
/*     */       
/*     */ 
/*     */ 
/*  93 */       if ((null != status) && (HttpResponseStatus.CONTINUE.codeAsText().contentEquals(status))) {
/*  94 */         FullHttpMessage fullMsg = newFullMessage(id, headers, ctx.alloc());
/*  95 */         out.add(fullMsg);
/*  96 */         return;
/*     */       }
/*     */       
/*  99 */       if (headersFrame.isEndStream()) {
/* 100 */         if ((headers.method() == null) && (status == null)) {
/* 101 */           LastHttpContent last = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
/* 102 */           HttpConversionUtil.addHttp2ToHttpHeaders(id, headers, last.trailingHeaders(), HttpVersion.HTTP_1_1, true, true);
/*     */           
/* 104 */           out.add(last);
/*     */         } else {
/* 106 */           FullHttpMessage full = newFullMessage(id, headers, ctx.alloc());
/* 107 */           out.add(full);
/*     */         }
/*     */       } else {
/* 110 */         HttpMessage req = newMessage(id, headers);
/* 111 */         if (!HttpUtil.isContentLengthSet(req)) {
/* 112 */           req.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/*     */         }
/* 114 */         out.add(req);
/*     */       }
/* 116 */     } else if ((frame instanceof Http2DataFrame)) {
/* 117 */       Http2DataFrame dataFrame = (Http2DataFrame)frame;
/* 118 */       if (dataFrame.isEndStream()) {
/* 119 */         out.add(new DefaultLastHttpContent(dataFrame.content().retain(), this.validateHeaders));
/*     */       } else {
/* 121 */         out.add(new DefaultHttpContent(dataFrame.content().retain()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void encodeLastContent(LastHttpContent last, List<Object> out) {
/* 127 */     boolean needFiller = (!(last instanceof FullHttpMessage)) && (last.trailingHeaders().isEmpty());
/* 128 */     if ((last.content().isReadable()) || (needFiller)) {
/* 129 */       out.add(new DefaultHttp2DataFrame(last.content().retain(), last.trailingHeaders().isEmpty()));
/*     */     }
/* 131 */     if (!last.trailingHeaders().isEmpty()) {
/* 132 */       Http2Headers headers = HttpConversionUtil.toHttp2Headers(last.trailingHeaders(), this.validateHeaders);
/* 133 */       out.add(new DefaultHttp2HeadersFrame(headers, true));
/*     */     }
/*     */   }
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
/*     */   protected void encode(ChannelHandlerContext ctx, HttpObject obj, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 153 */     if ((obj instanceof HttpResponse)) {
/* 154 */       HttpResponse res = (HttpResponse)obj;
/* 155 */       if (res.status().equals(HttpResponseStatus.CONTINUE)) {
/* 156 */         if ((res instanceof FullHttpResponse)) {
/* 157 */           Http2Headers headers = toHttp2Headers(res);
/* 158 */           out.add(new DefaultHttp2HeadersFrame(headers, false));
/* 159 */           return;
/*     */         }
/*     */         
/* 162 */         throw new EncoderException(HttpResponseStatus.CONTINUE.toString() + " must be a FullHttpResponse");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 167 */     if ((obj instanceof HttpMessage)) {
/* 168 */       Http2Headers headers = toHttp2Headers((HttpMessage)obj);
/* 169 */       boolean noMoreFrames = false;
/* 170 */       if ((obj instanceof FullHttpMessage)) {
/* 171 */         FullHttpMessage full = (FullHttpMessage)obj;
/* 172 */         noMoreFrames = (!full.content().isReadable()) && (full.trailingHeaders().isEmpty());
/*     */       }
/*     */       
/* 175 */       out.add(new DefaultHttp2HeadersFrame(headers, noMoreFrames));
/*     */     }
/*     */     
/* 178 */     if ((obj instanceof LastHttpContent)) {
/* 179 */       LastHttpContent last = (LastHttpContent)obj;
/* 180 */       encodeLastContent(last, out);
/* 181 */     } else if ((obj instanceof HttpContent)) {
/* 182 */       HttpContent cont = (HttpContent)obj;
/* 183 */       out.add(new DefaultHttp2DataFrame(cont.content().retain(), false));
/*     */     }
/*     */   }
/*     */   
/*     */   private Http2Headers toHttp2Headers(HttpMessage msg) {
/* 188 */     if ((msg instanceof HttpRequest)) {
/* 189 */       msg.headers().set(HttpConversionUtil.ExtensionHeaderNames.SCHEME
/* 190 */         .text(), this.scheme
/* 191 */         .name());
/*     */     }
/*     */     
/* 194 */     return HttpConversionUtil.toHttp2Headers(msg, this.validateHeaders);
/*     */   }
/*     */   
/*     */   private HttpMessage newMessage(int id, Http2Headers headers) throws Http2Exception
/*     */   {
/* 199 */     return this.isServer ? 
/* 200 */       HttpConversionUtil.toHttpRequest(id, headers, this.validateHeaders) : 
/* 201 */       HttpConversionUtil.toHttpResponse(id, headers, this.validateHeaders);
/*     */   }
/*     */   
/*     */   private FullHttpMessage newFullMessage(int id, Http2Headers headers, ByteBufAllocator alloc)
/*     */     throws Http2Exception
/*     */   {
/* 207 */     return this.isServer ? 
/* 208 */       HttpConversionUtil.toFullHttpRequest(id, headers, alloc, this.validateHeaders) : 
/* 209 */       HttpConversionUtil.toFullHttpResponse(id, headers, alloc, this.validateHeaders);
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 214 */     super.handlerAdded(ctx);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */     this.scheme = (isSsl(ctx) ? HttpScheme.HTTPS : HttpScheme.HTTP);
/*     */   }
/*     */   
/*     */   protected boolean isSsl(ChannelHandlerContext ctx) {
/* 225 */     Channel ch = ctx.channel();
/* 226 */     Channel connChannel = (ch instanceof Http2StreamChannel) ? ch.parent() : ch;
/* 227 */     return null != connChannel.pipeline().get(SslHandler.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2StreamFrameToHttpObjectCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */