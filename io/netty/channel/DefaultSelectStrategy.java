/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.IntSupplier;
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
/*    */ final class DefaultSelectStrategy
/*    */   implements SelectStrategy
/*    */ {
/* 24 */   static final SelectStrategy INSTANCE = new DefaultSelectStrategy();
/*    */   
/*    */ 
/*    */   public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks)
/*    */     throws Exception
/*    */   {
/* 30 */     return hasTasks ? selectSupplier.get() : -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\DefaultSelectStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */