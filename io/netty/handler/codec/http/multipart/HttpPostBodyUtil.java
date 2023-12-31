/*     */ package io.netty.handler.codec.http.multipart;
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
/*     */ final class HttpPostBodyUtil
/*     */ {
/*     */   public static final int chunkSize = 8096;
/*     */   public static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
/*     */   public static final String DEFAULT_TEXT_CONTENT_TYPE = "text/plain";
/*     */   
/*     */   public static enum TransferEncodingMechanism
/*     */   {
/*  49 */     BIT7("7bit"), 
/*     */     
/*     */ 
/*     */ 
/*  53 */     BIT8("8bit"), 
/*     */     
/*     */ 
/*     */ 
/*  57 */     BINARY("binary");
/*     */     
/*     */     private final String value;
/*     */     
/*     */     private TransferEncodingMechanism(String value) {
/*  62 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String value() {
/*  66 */       return this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  71 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class SeekAheadOptimize
/*     */   {
/*     */     byte[] bytes;
/*     */     
/*     */     int readerIndex;
/*     */     
/*     */     int pos;
/*     */     
/*     */     int origPos;
/*     */     
/*     */     int limit;
/*     */     
/*     */     ByteBuf buffer;
/*     */     
/*     */ 
/*     */     SeekAheadOptimize(ByteBuf buffer)
/*     */     {
/*  94 */       if (!buffer.hasArray()) {
/*  95 */         throw new IllegalArgumentException("buffer hasn't backing byte array");
/*     */       }
/*  97 */       this.buffer = buffer;
/*  98 */       this.bytes = buffer.array();
/*  99 */       this.readerIndex = buffer.readerIndex();
/* 100 */       this.origPos = (this.pos = buffer.arrayOffset() + this.readerIndex);
/* 101 */       this.limit = (buffer.arrayOffset() + buffer.writerIndex());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void setReadPosition(int minus)
/*     */     {
/* 110 */       this.pos -= minus;
/* 111 */       this.readerIndex = getReadPosition(this.pos);
/* 112 */       this.buffer.readerIndex(this.readerIndex);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int getReadPosition(int index)
/*     */     {
/* 121 */       return index - this.origPos + this.readerIndex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findNonWhitespace(String sb, int offset)
/*     */   {
/* 131 */     for (int result = offset; result < sb.length(); result++) {
/* 132 */       if (!Character.isWhitespace(sb.charAt(result))) {
/*     */         break;
/*     */       }
/*     */     }
/* 136 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int findEndOfString(String sb)
/*     */   {
/* 145 */     for (int result = sb.length(); result > 0; result--) {
/* 146 */       if (!Character.isWhitespace(sb.charAt(result - 1))) {
/*     */         break;
/*     */       }
/*     */     }
/* 150 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\multipart\HttpPostBodyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */