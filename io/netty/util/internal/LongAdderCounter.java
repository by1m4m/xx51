/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.util.concurrent.atomic.LongAdder;
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
/*    */ final class LongAdderCounter
/*    */   extends LongAdder
/*    */   implements LongCounter
/*    */ {
/*    */   public long value()
/*    */   {
/* 24 */     return longValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\LongAdderCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */