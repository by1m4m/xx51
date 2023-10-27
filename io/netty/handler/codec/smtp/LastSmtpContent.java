/*    */ package io.netty.handler.codec.smtp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
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
/*    */ public abstract interface LastSmtpContent
/*    */   extends SmtpContent
/*    */ {
/* 34 */   public static final LastSmtpContent EMPTY_LAST_CONTENT = new LastSmtpContent()
/*    */   {
/*    */     public LastSmtpContent copy() {
/* 37 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent duplicate()
/*    */     {
/* 42 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent retainedDuplicate()
/*    */     {
/* 47 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent replace(ByteBuf content)
/*    */     {
/* 52 */       return new DefaultLastSmtpContent(content);
/*    */     }
/*    */     
/*    */     public LastSmtpContent retain()
/*    */     {
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent retain(int increment)
/*    */     {
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent touch()
/*    */     {
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public LastSmtpContent touch(Object hint)
/*    */     {
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public ByteBuf content()
/*    */     {
/* 77 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/*    */     public int refCnt()
/*    */     {
/* 82 */       return 1;
/*    */     }
/*    */     
/*    */     public boolean release()
/*    */     {
/* 87 */       return false;
/*    */     }
/*    */     
/*    */     public boolean release(int decrement)
/*    */     {
/* 92 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract LastSmtpContent copy();
/*    */   
/*    */   public abstract LastSmtpContent duplicate();
/*    */   
/*    */   public abstract LastSmtpContent retainedDuplicate();
/*    */   
/*    */   public abstract LastSmtpContent replace(ByteBuf paramByteBuf);
/*    */   
/*    */   public abstract LastSmtpContent retain();
/*    */   
/*    */   public abstract LastSmtpContent retain(int paramInt);
/*    */   
/*    */   public abstract LastSmtpContent touch();
/*    */   
/*    */   public abstract LastSmtpContent touch(Object paramObject);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\smtp\LastSmtpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */