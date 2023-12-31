/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*    */ import java.lang.reflect.Field;
/*    */ import sun.misc.Unsafe;
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
/*    */ abstract class BaseLinkedQueueProducerNodeRef<E>
/*    */   extends BaseLinkedQueuePad0<E>
/*    */ {
/*    */   protected static final long P_NODE_OFFSET;
/*    */   protected LinkedQueueNode<E> producerNode;
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 37 */       Field pNodeField = BaseLinkedQueueProducerNodeRef.class.getDeclaredField("producerNode");
/* 38 */       P_NODE_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(pNodeField);
/*    */     }
/*    */     catch (NoSuchFieldException e)
/*    */     {
/* 42 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected final void spProducerNode(LinkedQueueNode<E> newValue)
/*    */   {
/* 50 */     this.producerNode = newValue;
/*    */   }
/*    */   
/*    */ 
/*    */   protected final LinkedQueueNode<E> lvProducerNode()
/*    */   {
/* 56 */     return (LinkedQueueNode)UnsafeAccess.UNSAFE.getObjectVolatile(this, P_NODE_OFFSET);
/*    */   }
/*    */   
/*    */ 
/*    */   protected final boolean casProducerNode(LinkedQueueNode<E> expect, LinkedQueueNode<E> newValue)
/*    */   {
/* 62 */     return UnsafeAccess.UNSAFE.compareAndSwapObject(this, P_NODE_OFFSET, expect, newValue);
/*    */   }
/*    */   
/*    */   protected final LinkedQueueNode<E> lpProducerNode()
/*    */   {
/* 67 */     return this.producerNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\queues\BaseLinkedQueueProducerNodeRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */