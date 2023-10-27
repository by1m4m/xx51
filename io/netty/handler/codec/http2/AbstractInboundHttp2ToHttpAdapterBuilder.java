/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ 
/*     */ public abstract class AbstractInboundHttp2ToHttpAdapterBuilder<T extends InboundHttp2ToHttpAdapter, B extends AbstractInboundHttp2ToHttpAdapterBuilder<T, B>>
/*     */ {
/*     */   private final Http2Connection connection;
/*     */   private int maxContentLength;
/*     */   private boolean validateHttpHeaders;
/*     */   private boolean propagateSettings;
/*     */   
/*     */   protected AbstractInboundHttp2ToHttpAdapterBuilder(Http2Connection connection)
/*     */   {
/*  41 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(connection, "connection"));
/*     */   }
/*     */   
/*     */   protected final B self()
/*     */   {
/*  46 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Http2Connection connection()
/*     */   {
/*  53 */     return this.connection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int maxContentLength()
/*     */   {
/*  60 */     return this.maxContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected B maxContentLength(int maxContentLength)
/*     */   {
/*  71 */     this.maxContentLength = maxContentLength;
/*  72 */     return self();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isValidateHttpHeaders()
/*     */   {
/*  79 */     return this.validateHttpHeaders;
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
/*     */   protected B validateHttpHeaders(boolean validate)
/*     */   {
/*  93 */     this.validateHttpHeaders = validate;
/*  94 */     return self();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isPropagateSettings()
/*     */   {
/* 101 */     return this.propagateSettings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected B propagateSettings(boolean propagate)
/*     */   {
/* 112 */     this.propagateSettings = propagate;
/* 113 */     return self();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected T build()
/*     */   {
/*     */     try
/*     */     {
/* 122 */       instance = build(connection(), maxContentLength(), 
/* 123 */         isValidateHttpHeaders(), isPropagateSettings());
/*     */     } catch (Throwable t) { T instance;
/* 125 */       throw new IllegalStateException("failed to create a new InboundHttp2ToHttpAdapter", t); }
/*     */     T instance;
/* 127 */     this.connection.addListener(instance);
/* 128 */     return instance;
/*     */   }
/*     */   
/*     */   protected abstract T build(Http2Connection paramHttp2Connection, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\AbstractInboundHttp2ToHttpAdapterBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */