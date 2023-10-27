/*    */ package com.sun.jna;
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
/*    */ public class NativeLong
/*    */   extends IntegerType
/*    */ {
/* 23 */   public static final int SIZE = Native.LONG_SIZE;
/*    */   
/*    */   public NativeLong()
/*    */   {
/* 27 */     this(0L);
/*    */   }
/*    */   
/*    */   public NativeLong(long value)
/*    */   {
/* 32 */     this(value, false);
/*    */   }
/*    */   
/*    */   public NativeLong(long value, boolean unsigned)
/*    */   {
/* 37 */     super(SIZE, value, unsigned);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\NativeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */