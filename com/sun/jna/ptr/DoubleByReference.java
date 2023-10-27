/*    */ package com.sun.jna.ptr;
/*    */ 
/*    */ import com.sun.jna.Pointer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleByReference
/*    */   extends ByReference
/*    */ {
/*    */   public DoubleByReference()
/*    */   {
/* 17 */     this(0.0D);
/*    */   }
/*    */   
/*    */   public DoubleByReference(double value) {
/* 21 */     super(8);
/* 22 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(double value) {
/* 26 */     getPointer().setDouble(0L, value);
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 30 */     return getPointer().getDouble(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\DoubleByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */