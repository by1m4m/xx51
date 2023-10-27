/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public final class DefaultLastBulkStringRedisContent
/*    */   extends DefaultBulkStringRedisContent
/*    */   implements LastBulkStringRedisContent
/*    */ {
/*    */   public DefaultLastBulkStringRedisContent(ByteBuf content)
/*    */   {
/* 33 */     super(content);
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent copy()
/*    */   {
/* 38 */     return (LastBulkStringRedisContent)super.copy();
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent duplicate()
/*    */   {
/* 43 */     return (LastBulkStringRedisContent)super.duplicate();
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent retainedDuplicate()
/*    */   {
/* 48 */     return (LastBulkStringRedisContent)super.retainedDuplicate();
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent replace(ByteBuf content)
/*    */   {
/* 53 */     return new DefaultLastBulkStringRedisContent(content);
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent retain()
/*    */   {
/* 58 */     super.retain();
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent retain(int increment)
/*    */   {
/* 64 */     super.retain(increment);
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent touch()
/*    */   {
/* 70 */     super.touch();
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public LastBulkStringRedisContent touch(Object hint)
/*    */   {
/* 76 */     super.touch(hint);
/* 77 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\DefaultLastBulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */