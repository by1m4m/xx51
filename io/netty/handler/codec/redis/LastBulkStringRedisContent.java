/*    */ package io.netty.handler.codec.redis;
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
/*    */ public abstract interface LastBulkStringRedisContent
/*    */   extends BulkStringRedisContent
/*    */ {
/* 31 */   public static final LastBulkStringRedisContent EMPTY_LAST_CONTENT = new LastBulkStringRedisContent()
/*    */   {
/*    */     public ByteBuf content()
/*    */     {
/* 35 */       return Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent copy()
/*    */     {
/* 40 */       return this;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent duplicate()
/*    */     {
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent retainedDuplicate()
/*    */     {
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent replace(ByteBuf content)
/*    */     {
/* 55 */       return new DefaultLastBulkStringRedisContent(content);
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent retain(int increment)
/*    */     {
/* 60 */       return this;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent retain()
/*    */     {
/* 65 */       return this;
/*    */     }
/*    */     
/*    */     public int refCnt()
/*    */     {
/* 70 */       return 1;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent touch()
/*    */     {
/* 75 */       return this;
/*    */     }
/*    */     
/*    */     public LastBulkStringRedisContent touch(Object hint)
/*    */     {
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public boolean release()
/*    */     {
/* 85 */       return false;
/*    */     }
/*    */     
/*    */     public boolean release(int decrement)
/*    */     {
/* 90 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract LastBulkStringRedisContent copy();
/*    */   
/*    */   public abstract LastBulkStringRedisContent duplicate();
/*    */   
/*    */   public abstract LastBulkStringRedisContent retainedDuplicate();
/*    */   
/*    */   public abstract LastBulkStringRedisContent replace(ByteBuf paramByteBuf);
/*    */   
/*    */   public abstract LastBulkStringRedisContent retain();
/*    */   
/*    */   public abstract LastBulkStringRedisContent retain(int paramInt);
/*    */   
/*    */   public abstract LastBulkStringRedisContent touch();
/*    */   
/*    */   public abstract LastBulkStringRedisContent touch(Object paramObject);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\LastBulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */