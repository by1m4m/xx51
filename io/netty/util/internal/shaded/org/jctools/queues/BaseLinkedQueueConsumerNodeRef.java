/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*     */ import java.lang.reflect.Field;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class BaseLinkedQueueConsumerNodeRef<E>
/*     */   extends BaseLinkedQueuePad1<E>
/*     */ {
/*     */   protected static final long C_NODE_OFFSET;
/*     */   protected LinkedQueueNode<E> consumerNode;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  86 */       Field cNodeField = BaseLinkedQueueConsumerNodeRef.class.getDeclaredField("consumerNode");
/*  87 */       C_NODE_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(cNodeField);
/*     */     }
/*     */     catch (NoSuchFieldException e)
/*     */     {
/*  91 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void spConsumerNode(LinkedQueueNode<E> newValue)
/*     */   {
/*  99 */     this.consumerNode = newValue;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final LinkedQueueNode<E> lvConsumerNode()
/*     */   {
/* 105 */     return (LinkedQueueNode)UnsafeAccess.UNSAFE.getObjectVolatile(this, C_NODE_OFFSET);
/*     */   }
/*     */   
/*     */   protected final LinkedQueueNode<E> lpConsumerNode()
/*     */   {
/* 110 */     return this.consumerNode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\queues\BaseLinkedQueueConsumerNodeRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */