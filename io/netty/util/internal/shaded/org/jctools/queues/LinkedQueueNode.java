/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
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
/*    */ final class LinkedQueueNode<E>
/*    */ {
/*    */   private static final long NEXT_OFFSET;
/*    */   private E value;
/*    */   private volatile LinkedQueueNode<E> next;
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 26 */       NEXT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(LinkedQueueNode.class.getDeclaredField("next"));
/*    */     }
/*    */     catch (NoSuchFieldException e)
/*    */     {
/* 30 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   LinkedQueueNode()
/*    */   {
/* 39 */     this(null);
/*    */   }
/*    */   
/*    */   LinkedQueueNode(E val)
/*    */   {
/* 44 */     spValue(val);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public E getAndNullValue()
/*    */   {
/* 54 */     E temp = lpValue();
/* 55 */     spValue(null);
/* 56 */     return temp;
/*    */   }
/*    */   
/*    */   public E lpValue()
/*    */   {
/* 61 */     return (E)this.value;
/*    */   }
/*    */   
/*    */   public void spValue(E newValue)
/*    */   {
/* 66 */     this.value = newValue;
/*    */   }
/*    */   
/*    */   public void soNext(LinkedQueueNode<E> n)
/*    */   {
/* 71 */     UnsafeAccess.UNSAFE.putOrderedObject(this, NEXT_OFFSET, n);
/*    */   }
/*    */   
/*    */   public LinkedQueueNode<E> lvNext()
/*    */   {
/* 76 */     return this.next;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\queues\LinkedQueueNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */