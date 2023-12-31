/*    */ package io.netty.handler.codec.http2;
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
/*    */ public final class InboundHttp2ToHttpAdapterBuilder
/*    */   extends AbstractInboundHttp2ToHttpAdapterBuilder<InboundHttp2ToHttpAdapter, InboundHttp2ToHttpAdapterBuilder>
/*    */ {
/*    */   public InboundHttp2ToHttpAdapterBuilder(Http2Connection connection)
/*    */   {
/* 33 */     super(connection);
/*    */   }
/*    */   
/*    */   public InboundHttp2ToHttpAdapterBuilder maxContentLength(int maxContentLength)
/*    */   {
/* 38 */     return (InboundHttp2ToHttpAdapterBuilder)super.maxContentLength(maxContentLength);
/*    */   }
/*    */   
/*    */   public InboundHttp2ToHttpAdapterBuilder validateHttpHeaders(boolean validate)
/*    */   {
/* 43 */     return (InboundHttp2ToHttpAdapterBuilder)super.validateHttpHeaders(validate);
/*    */   }
/*    */   
/*    */   public InboundHttp2ToHttpAdapterBuilder propagateSettings(boolean propagate)
/*    */   {
/* 48 */     return (InboundHttp2ToHttpAdapterBuilder)super.propagateSettings(propagate);
/*    */   }
/*    */   
/*    */   public InboundHttp2ToHttpAdapter build()
/*    */   {
/* 53 */     return super.build();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected InboundHttp2ToHttpAdapter build(Http2Connection connection, int maxContentLength, boolean validateHttpHeaders, boolean propagateSettings)
/*    */     throws Exception
/*    */   {
/* 62 */     return new InboundHttp2ToHttpAdapter(connection, maxContentLength, validateHttpHeaders, propagateSettings);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\InboundHttp2ToHttpAdapterBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */