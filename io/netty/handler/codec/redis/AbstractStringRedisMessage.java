/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public abstract class AbstractStringRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final String content;
/*    */   
/*    */   AbstractStringRedisMessage(String content)
/*    */   {
/* 31 */     this.content = ((String)ObjectUtil.checkNotNull(content, "content"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String content()
/*    */   {
/* 40 */     return this.content;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return 
/*    */     
/*    */ 
/*    */ 
/* 49 */       StringUtil.simpleClassName(this) + '[' + "content=" + this.content + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\AbstractStringRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */