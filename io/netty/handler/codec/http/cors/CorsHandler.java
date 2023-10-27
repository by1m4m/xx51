/*     */ package io.netty.handler.codec.http.cors;
/*     */ 
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class CorsHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CorsHandler.class);
/*     */   
/*     */   private static final String ANY_ORIGIN = "*";
/*     */   
/*     */   private static final String NULL_ORIGIN = "null";
/*     */   
/*     */   private CorsConfig config;
/*     */   private HttpRequest request;
/*     */   private final List<CorsConfig> configList;
/*     */   private boolean isShortCircuit;
/*     */   
/*     */   public CorsHandler(CorsConfig config)
/*     */   {
/*  64 */     this(Collections.singletonList(ObjectUtil.checkNotNull(config, "config")), config.isShortCircuit());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsHandler(List<CorsConfig> configList, boolean isShortCircuit)
/*     */   {
/*  75 */     ObjectUtil.checkNonEmpty(configList, "configList");
/*  76 */     this.configList = configList;
/*  77 */     this.isShortCircuit = isShortCircuit;
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*  82 */     if ((msg instanceof HttpRequest)) {
/*  83 */       this.request = ((HttpRequest)msg);
/*  84 */       String origin = this.request.headers().get(HttpHeaderNames.ORIGIN);
/*  85 */       this.config = getForOrigin(origin);
/*  86 */       if (isPreflightRequest(this.request)) {
/*  87 */         handlePreflight(ctx, this.request);
/*  88 */         return;
/*     */       }
/*  90 */       if ((this.isShortCircuit) && (origin != null) && (this.config == null)) {
/*  91 */         forbidden(ctx, this.request);
/*  92 */         return;
/*     */       }
/*     */     }
/*  95 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   private void handlePreflight(ChannelHandlerContext ctx, HttpRequest request) {
/*  99 */     HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK, true, true);
/* 100 */     if (setOrigin(response)) {
/* 101 */       setAllowMethods(response);
/* 102 */       setAllowHeaders(response);
/* 103 */       setAllowCredentials(response);
/* 104 */       setMaxAge(response);
/* 105 */       setPreflightHeaders(response);
/*     */     }
/* 107 */     if (!response.headers().contains(HttpHeaderNames.CONTENT_LENGTH)) {
/* 108 */       response.headers().set(HttpHeaderNames.CONTENT_LENGTH, HttpHeaderValues.ZERO);
/*     */     }
/* 110 */     ReferenceCountUtil.release(request);
/* 111 */     respond(ctx, request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setPreflightHeaders(HttpResponse response)
/*     */   {
/* 121 */     response.headers().add(this.config.preflightResponseHeaders());
/*     */   }
/*     */   
/*     */   private CorsConfig getForOrigin(String requestOrigin) {
/* 125 */     for (CorsConfig corsConfig : this.configList) {
/* 126 */       if (corsConfig.isAnyOriginSupported()) {
/* 127 */         return corsConfig;
/*     */       }
/* 129 */       if (corsConfig.origins().contains(requestOrigin)) {
/* 130 */         return corsConfig;
/*     */       }
/* 132 */       if ((corsConfig.isNullOriginAllowed()) || ("null".equals(requestOrigin))) {
/* 133 */         return corsConfig;
/*     */       }
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   private boolean setOrigin(HttpResponse response) {
/* 140 */     String origin = this.request.headers().get(HttpHeaderNames.ORIGIN);
/* 141 */     if ((origin != null) && (this.config != null)) {
/* 142 */       if (("null".equals(origin)) && (this.config.isNullOriginAllowed())) {
/* 143 */         setNullOrigin(response);
/* 144 */         return true;
/*     */       }
/* 146 */       if (this.config.isAnyOriginSupported()) {
/* 147 */         if (this.config.isCredentialsAllowed()) {
/* 148 */           echoRequestOrigin(response);
/* 149 */           setVaryHeader(response);
/*     */         } else {
/* 151 */           setAnyOrigin(response);
/*     */         }
/* 153 */         return true;
/*     */       }
/* 155 */       if (this.config.origins().contains(origin)) {
/* 156 */         setOrigin(response, origin);
/* 157 */         setVaryHeader(response);
/* 158 */         return true;
/*     */       }
/* 160 */       logger.debug("Request origin [{}]] was not among the configured origins [{}]", origin, this.config.origins());
/*     */     }
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   private void echoRequestOrigin(HttpResponse response) {
/* 166 */     setOrigin(response, this.request.headers().get(HttpHeaderNames.ORIGIN));
/*     */   }
/*     */   
/*     */   private static void setVaryHeader(HttpResponse response) {
/* 170 */     response.headers().set(HttpHeaderNames.VARY, HttpHeaderNames.ORIGIN);
/*     */   }
/*     */   
/*     */   private static void setAnyOrigin(HttpResponse response) {
/* 174 */     setOrigin(response, "*");
/*     */   }
/*     */   
/*     */   private static void setNullOrigin(HttpResponse response) {
/* 178 */     setOrigin(response, "null");
/*     */   }
/*     */   
/*     */   private static void setOrigin(HttpResponse response, String origin) {
/* 182 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
/*     */   }
/*     */   
/*     */   private void setAllowCredentials(HttpResponse response) {
/* 186 */     if ((this.config.isCredentialsAllowed()) && 
/* 187 */       (!response.headers().get(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"))) {
/* 188 */       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isPreflightRequest(HttpRequest request) {
/* 193 */     HttpHeaders headers = request.headers();
/* 194 */     return (request.method().equals(HttpMethod.OPTIONS)) && 
/* 195 */       (headers.contains(HttpHeaderNames.ORIGIN)) && 
/* 196 */       (headers.contains(HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD));
/*     */   }
/*     */   
/*     */   private void setExposeHeaders(HttpResponse response) {
/* 200 */     if (!this.config.exposedHeaders().isEmpty()) {
/* 201 */       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, this.config.exposedHeaders());
/*     */     }
/*     */   }
/*     */   
/*     */   private void setAllowMethods(HttpResponse response) {
/* 206 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, this.config.allowedRequestMethods());
/*     */   }
/*     */   
/*     */   private void setAllowHeaders(HttpResponse response) {
/* 210 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, this.config.allowedRequestHeaders());
/*     */   }
/*     */   
/*     */   private void setMaxAge(HttpResponse response) {
/* 214 */     response.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, Long.valueOf(this.config.maxAge()));
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 220 */     if ((this.config != null) && (this.config.isCorsSupportEnabled()) && ((msg instanceof HttpResponse))) {
/* 221 */       HttpResponse response = (HttpResponse)msg;
/* 222 */       if (setOrigin(response)) {
/* 223 */         setAllowCredentials(response);
/* 224 */         setExposeHeaders(response);
/*     */       }
/*     */     }
/* 227 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */   private static void forbidden(ChannelHandlerContext ctx, HttpRequest request) {
/* 231 */     HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.FORBIDDEN);
/* 232 */     response.headers().set(HttpHeaderNames.CONTENT_LENGTH, HttpHeaderValues.ZERO);
/* 233 */     ReferenceCountUtil.release(request);
/* 234 */     respond(ctx, request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void respond(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
/*     */   {
/* 242 */     boolean keepAlive = HttpUtil.isKeepAlive(request);
/*     */     
/* 244 */     HttpUtil.setKeepAlive(response, keepAlive);
/*     */     
/* 246 */     ChannelFuture future = ctx.writeAndFlush(response);
/* 247 */     if (!keepAlive) {
/* 248 */       future.addListener(ChannelFutureListener.CLOSE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\cors\CorsHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */