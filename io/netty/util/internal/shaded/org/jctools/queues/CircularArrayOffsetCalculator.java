/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CircularArrayOffsetCalculator
/*    */ {
/*    */   public static <E> E[] allocate(int capacity)
/*    */   {
/* 14 */     return new Object[capacity];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static long calcElementOffset(long index, long mask)
/*    */   {
/* 24 */     return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\queues\CircularArrayOffsetCalculator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */