/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
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
/*    */ public final class IndexedQueueSizeUtil
/*    */ {
/*    */   public static int size(IndexedQueue iq)
/*    */   {
/* 29 */     long after = iq.lvConsumerIndex();
/*    */     
/*    */     for (;;)
/*    */     {
/* 33 */       long before = after;
/* 34 */       long currentProducerIndex = iq.lvProducerIndex();
/* 35 */       after = iq.lvConsumerIndex();
/* 36 */       if (before == after)
/*    */       {
/* 38 */         long size = currentProducerIndex - after;
/* 39 */         break;
/*    */       }
/*    */     }
/*    */     
/*    */     long size;
/* 44 */     if (size > 2147483647L)
/*    */     {
/* 46 */       return Integer.MAX_VALUE;
/*    */     }
/*    */     
/*    */ 
/* 50 */     return (int)size;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isEmpty(IndexedQueue iq)
/*    */   {
/* 60 */     return iq.lvConsumerIndex() == iq.lvProducerIndex();
/*    */   }
/*    */   
/*    */   public static abstract interface IndexedQueue
/*    */   {
/*    */     public abstract long lvConsumerIndex();
/*    */     
/*    */     public abstract long lvProducerIndex();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\queues\IndexedQueueSizeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */