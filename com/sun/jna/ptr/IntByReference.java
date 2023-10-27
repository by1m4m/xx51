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
/*    */ 
/*    */ public class IntByReference
/*    */   extends ByReference
/*    */ {
/*    */   public IntByReference()
/*    */   {
/* 18 */     this(0);
/*    */   }
/*    */   
/*    */   public IntByReference(int value) {
/* 22 */     super(4);
/* 23 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(int value) {
/* 27 */     getPointer().setInt(0L, value);
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 31 */     return getPointer().getInt(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\IntByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */