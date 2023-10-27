/*    */ package io.netty.util.internal;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MathUtil
/*    */ {
/*    */   public static int findNextPositivePowerOfTwo(int value)
/*    */   {
/* 35 */     assert ((value > Integer.MIN_VALUE) && (value < 1073741824));
/* 36 */     return 1 << 32 - Integer.numberOfLeadingZeros(value - 1);
/*    */   }
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
/*    */   public static int safeFindNextPositivePowerOfTwo(int value)
/*    */   {
/* 52 */     return value >= 1073741824 ? 1073741824 : value <= 0 ? 1 : findNextPositivePowerOfTwo(value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isOutOfBounds(int index, int length, int capacity)
/*    */   {
/* 64 */     return (index | length | index + length | capacity - (index + length)) < 0;
/*    */   }
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
/*    */   public static int compare(int x, int y)
/*    */   {
/* 78 */     return x > y ? 1 : x < y ? -1 : 0;
/*    */   }
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
/*    */   public static int compare(long x, long y)
/*    */   {
/* 93 */     return x > y ? 1 : x < y ? -1 : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\MathUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */