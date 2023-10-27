/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public abstract interface LastHttpContent
/*    */   extends HttpContent
/*    */ {
/* 30 */   public static final LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent()
/*    */   {
/*    */     public ByteBuf content()
/*    */     {
/* 34 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/*    */     public LastHttpContent copy()
/*    */     {
/* 39 */       return EMPTY_LAST_CONTENT;
/*    */     }
/*    */     
/*    */     public LastHttpContent duplicate()
/*    */     {
/* 44 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent replace(ByteBuf content)
/*    */     {
/* 49 */       return new DefaultLastHttpContent(content);
/*    */     }
/*    */     
/*    */     public LastHttpContent retainedDuplicate()
/*    */     {
/* 54 */       return this;
/*    */     }
/*    */     
/*    */     public HttpHeaders trailingHeaders()
/*    */     {
/* 59 */       return EmptyHttpHeaders.INSTANCE;
/*    */     }
/*    */     
/*    */     public DecoderResult decoderResult()
/*    */     {
/* 64 */       return DecoderResult.SUCCESS;
/*    */     }
/*    */     
/*    */     @Deprecated
/*    */     public DecoderResult getDecoderResult()
/*    */     {
/* 70 */       return decoderResult();
/*    */     }
/*    */     
/*    */     public void setDecoderResult(DecoderResult result)
/*    */     {
/* 75 */       throw new UnsupportedOperationException("read only");
/*    */     }
/*    */     
/*    */     public int refCnt()
/*    */     {
/* 80 */       return 1;
/*    */     }
/*    */     
/*    */     public LastHttpContent retain()
/*    */     {
/* 85 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent retain(int increment)
/*    */     {
/* 90 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent touch()
/*    */     {
/* 95 */       return this;
/*    */     }
/*    */     
/*    */     public LastHttpContent touch(Object hint)
/*    */     {
/* :0 */       return this;
/*    */     }
/*    */     
/*    */     public boolean release()
/*    */     {
/* :5 */       return false;
/*    */     }
/*    */     
/*    */     public boolean release(int decrement)
/*    */     {
/* ;0 */       return false;
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* ;5 */       return "EmptyLastHttpContent";
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract HttpHeaders trailingHeaders();
/*    */   
/*    */   public abstract LastHttpContent copy();
/*    */   
/*    */   public abstract LastHttpContent duplicate();
/*    */   
/*    */   public abstract LastHttpContent retainedDuplicate();
/*    */   
/*    */   public abstract LastHttpContent replace(ByteBuf paramByteBuf);
/*    */   
/*    */   public abstract LastHttpContent retain(int paramInt);
/*    */   
/*    */   public abstract LastHttpContent retain();
/*    */   
/*    */   public abstract LastHttpContent touch();
/*    */   
/*    */   public abstract LastHttpContent touch(Object paramObject);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\LastHttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */