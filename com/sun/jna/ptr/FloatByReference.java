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
/*    */ public class FloatByReference
/*    */   extends ByReference
/*    */ {
/*    */   public FloatByReference()
/*    */   {
/* 17 */     this(0.0F);
/*    */   }
/*    */   
/*    */   public FloatByReference(float value) {
/* 21 */     super(4);
/* 22 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 26 */     getPointer().setFloat(0L, value);
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 30 */     return getPointer().getFloat(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\FloatByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */