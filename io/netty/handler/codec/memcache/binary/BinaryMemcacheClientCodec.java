/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.CombinedChannelDuplexHandler;
/*     */ import io.netty.handler.codec.PrematureChannelClosureException;
/*     */ import io.netty.handler.codec.memcache.LastMemcacheContent;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ 
/*     */ 
/*     */ public final class BinaryMemcacheClientCodec
/*     */   extends CombinedChannelDuplexHandler<BinaryMemcacheResponseDecoder, BinaryMemcacheRequestEncoder>
/*     */ {
/*     */   private final boolean failOnMissingResponse;
/*  44 */   private final AtomicLong requestResponseCounter = new AtomicLong();
/*     */   
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec()
/*     */   {
/*  50 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec(int decodeChunkSize)
/*     */   {
/*  59 */     this(decodeChunkSize, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryMemcacheClientCodec(int decodeChunkSize, boolean failOnMissingResponse)
/*     */   {
/*  69 */     this.failOnMissingResponse = failOnMissingResponse;
/*  70 */     init(new Decoder(decodeChunkSize), new Encoder(null));
/*     */   }
/*     */   
/*     */   private final class Encoder extends BinaryMemcacheRequestEncoder {
/*     */     private Encoder() {}
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/*  77 */       super.encode(ctx, msg, out);
/*     */       
/*  79 */       if ((BinaryMemcacheClientCodec.this.failOnMissingResponse) && ((msg instanceof LastMemcacheContent))) {
/*  80 */         BinaryMemcacheClientCodec.this.requestResponseCounter.incrementAndGet();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Decoder extends BinaryMemcacheResponseDecoder
/*     */   {
/*     */     Decoder(int chunkSize) {
/*  88 */       super();
/*     */     }
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*  93 */       int oldSize = out.size();
/*  94 */       super.decode(ctx, in, out);
/*     */       
/*  96 */       if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
/*  97 */         int size = out.size();
/*  98 */         for (int i = oldSize; i < size; i++) {
/*  99 */           Object msg = out.get(i);
/* 100 */           if ((msg instanceof LastMemcacheContent)) {
/* 101 */             BinaryMemcacheClientCodec.this.requestResponseCounter.decrementAndGet();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */     {
/* 109 */       super.channelInactive(ctx);
/*     */       
/* 111 */       if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
/* 112 */         long missingResponses = BinaryMemcacheClientCodec.this.requestResponseCounter.get();
/* 113 */         if (missingResponses > 0L) {
/* 114 */           ctx.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheClientCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */