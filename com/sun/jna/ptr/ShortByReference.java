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
/*    */ public class ShortByReference
/*    */   extends ByReference
/*    */ {
/*    */   public ShortByReference()
/*    */   {
/* 18 */     this((short)0);
/*    */   }
/*    */   
/*    */   public ShortByReference(short value) {
/* 22 */     super(2);
/* 23 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(short value) {
/* 27 */     getPointer().setShort(0L, value);
/*    */   }
/*    */   
/*    */   public short getValue() {
/* 31 */     return getPointer().getShort(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\ShortByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */