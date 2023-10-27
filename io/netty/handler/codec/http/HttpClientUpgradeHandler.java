/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpClientUpgradeHandler
/*     */   extends HttpObjectAggregator
/*     */   implements ChannelOutboundHandler
/*     */ {
/*     */   private final SourceCodec sourceCodec;
/*     */   private final UpgradeCodec upgradeCodec;
/*     */   private boolean upgradeRequested;
/*     */   
/*     */   public static abstract interface UpgradeCodec
/*     */   {
/*     */     public abstract CharSequence protocol();
/*     */     
/*     */     public abstract Collection<CharSequence> setUpgradeHeaders(ChannelHandlerContext paramChannelHandlerContext, HttpRequest paramHttpRequest);
/*     */     
/*     */     public abstract void upgradeTo(ChannelHandlerContext paramChannelHandlerContext, FullHttpResponse paramFullHttpResponse)
/*     */       throws Exception;
/*     */   }
/*     */   
/*     */   public static abstract interface SourceCodec
/*     */   {
/*     */     public abstract void prepareUpgradeFrom(ChannelHandlerContext paramChannelHandlerContext);
/*     */     
/*     */     public abstract void upgradeFrom(ChannelHandlerContext paramChannelHandlerContext);
/*     */   }
/*     */   
/*     */   public static enum UpgradeEvent
/*     */   {
/*  47 */     UPGRADE_ISSUED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  52 */     UPGRADE_SUCCESSFUL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */     UPGRADE_REJECTED;
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
/*     */ 
/*     */ 
/*     */     private UpgradeEvent() {}
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
/*     */   public HttpClientUpgradeHandler(SourceCodec sourceCodec, UpgradeCodec upgradeCodec, int maxContentLength)
/*     */   {
/* 117 */     super(maxContentLength);
/* 118 */     if (sourceCodec == null) {
/* 119 */       throw new NullPointerException("sourceCodec");
/*     */     }
/* 121 */     if (upgradeCodec == null) {
/* 122 */       throw new NullPointerException("upgradeCodec");
/*     */     }
/* 124 */     this.sourceCodec = sourceCodec;
/* 125 */     this.upgradeCodec = upgradeCodec;
/*     */   }
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*     */   {
/* 130 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 136 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 141 */     ctx.disconnect(promise);
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 146 */     ctx.close(promise);
/*     */   }
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 151 */     ctx.deregister(promise);
/*     */   }
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 156 */     ctx.read();
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 162 */     if (!(msg instanceof HttpRequest)) {
/* 163 */       ctx.write(msg, promise);
/* 164 */       return;
/*     */     }
/*     */     
/* 167 */     if (this.upgradeRequested) {
/* 168 */       promise.setFailure(new IllegalStateException("Attempting to write HTTP request with upgrade in progress"));
/*     */       
/* 170 */       return;
/*     */     }
/*     */     
/* 173 */     this.upgradeRequested = true;
/* 174 */     setUpgradeRequestHeaders(ctx, (HttpRequest)msg);
/*     */     
/*     */ 
/* 177 */     ctx.write(msg, promise);
/*     */     
/*     */ 
/* 180 */     ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_ISSUED);
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 186 */     ctx.flush();
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 192 */     FullHttpResponse response = null;
/*     */     try {
/* 194 */       if (!this.upgradeRequested) {
/* 195 */         throw new IllegalStateException("Read HTTP response without requesting protocol switch");
/*     */       }
/*     */       
/* 198 */       if ((msg instanceof HttpResponse)) {
/* 199 */         HttpResponse rep = (HttpResponse)msg;
/* 200 */         if (!HttpResponseStatus.SWITCHING_PROTOCOLS.equals(rep.status()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 205 */           ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_REJECTED);
/* 206 */           removeThisHandler(ctx);
/* 207 */           ctx.fireChannelRead(msg);
/* 208 */           return;
/*     */         }
/*     */       }
/*     */       
/* 212 */       if ((msg instanceof FullHttpResponse)) {
/* 213 */         response = (FullHttpResponse)msg;
/*     */         
/* 215 */         response.retain();
/* 216 */         out.add(response);
/*     */       }
/*     */       else {
/* 219 */         super.decode(ctx, msg, out);
/* 220 */         if (out.isEmpty())
/*     */         {
/* 222 */           return;
/*     */         }
/*     */         
/* 225 */         assert (out.size() == 1);
/* 226 */         response = (FullHttpResponse)out.get(0);
/*     */       }
/*     */       
/* 229 */       CharSequence upgradeHeader = response.headers().get(HttpHeaderNames.UPGRADE);
/* 230 */       if ((upgradeHeader != null) && (!AsciiString.contentEqualsIgnoreCase(this.upgradeCodec.protocol(), upgradeHeader))) {
/* 231 */         throw new IllegalStateException("Switching Protocols response with unexpected UPGRADE protocol: " + upgradeHeader);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 236 */       this.sourceCodec.prepareUpgradeFrom(ctx);
/* 237 */       this.upgradeCodec.upgradeTo(ctx, response);
/*     */       
/*     */ 
/* 240 */       ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_SUCCESSFUL);
/*     */       
/*     */ 
/*     */ 
/* 244 */       this.sourceCodec.upgradeFrom(ctx);
/*     */       
/*     */ 
/*     */ 
/* 248 */       response.release();
/* 249 */       out.clear();
/* 250 */       removeThisHandler(ctx);
/*     */     } catch (Throwable t) {
/* 252 */       ReferenceCountUtil.release(response);
/* 253 */       ctx.fireExceptionCaught(t);
/* 254 */       removeThisHandler(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void removeThisHandler(ChannelHandlerContext ctx) {
/* 259 */     ctx.pipeline().remove(ctx.name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setUpgradeRequestHeaders(ChannelHandlerContext ctx, HttpRequest request)
/*     */   {
/* 267 */     request.headers().set(HttpHeaderNames.UPGRADE, this.upgradeCodec.protocol());
/*     */     
/*     */ 
/* 270 */     Set<CharSequence> connectionParts = new LinkedHashSet(2);
/* 271 */     connectionParts.addAll(this.upgradeCodec.setUpgradeHeaders(ctx, request));
/*     */     
/*     */ 
/* 274 */     StringBuilder builder = new StringBuilder();
/* 275 */     for (CharSequence part : connectionParts) {
/* 276 */       builder.append(part);
/* 277 */       builder.append(',');
/*     */     }
/* 279 */     builder.append(HttpHeaderValues.UPGRADE);
/* 280 */     request.headers().add(HttpHeaderNames.CONNECTION, builder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpClientUpgradeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */