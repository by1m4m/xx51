/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ public abstract interface Http2HeadersEncoder
/*     */ {
/*  93 */   public static final SensitivityDetector NEVER_SENSITIVE = new SensitivityDetector()
/*     */   {
/*     */     public boolean isSensitive(CharSequence name, CharSequence value) {
/*  96 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   public static final SensitivityDetector ALWAYS_SENSITIVE = new SensitivityDetector()
/*     */   {
/*     */     public boolean isSensitive(CharSequence name, CharSequence value) {
/* 106 */       return true;
/*     */     }
/*     */   };
/*     */   
/*     */   public abstract void encodeHeaders(int paramInt, Http2Headers paramHttp2Headers, ByteBuf paramByteBuf)
/*     */     throws Http2Exception;
/*     */   
/*     */   public abstract Configuration configuration();
/*     */   
/*     */   public static abstract interface SensitivityDetector
/*     */   {
/*     */     public abstract boolean isSensitive(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*     */   }
/*     */   
/*     */   public static abstract interface Configuration
/*     */   {
/*     */     public abstract void maxHeaderTableSize(long paramLong)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract long maxHeaderTableSize();
/*     */     
/*     */     public abstract void maxHeaderListSize(long paramLong)
/*     */       throws Http2Exception;
/*     */     
/*     */     public abstract long maxHeaderListSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2HeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */