/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public final class IntegerRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final long value;
/*    */   
/*    */   public IntegerRedisMessage(long value)
/*    */   {
/* 35 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public long value()
/*    */   {
/* 44 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 49 */     return 
/*    */     
/*    */ 
/*    */ 
/* 53 */       StringUtil.simpleClassName(this) + '[' + "value=" + this.value + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\IntegerRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */