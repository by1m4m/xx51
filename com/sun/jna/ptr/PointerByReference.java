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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PointerByReference
/*    */   extends ByReference
/*    */ {
/*    */   public PointerByReference()
/*    */   {
/* 24 */     this(null);
/*    */   }
/*    */   
/*    */   public PointerByReference(Pointer value) {
/* 28 */     super(Pointer.SIZE);
/* 29 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(Pointer value) {
/* 33 */     getPointer().setPointer(0L, value);
/*    */   }
/*    */   
/*    */   public Pointer getValue() {
/* 37 */     return getPointer().getPointer(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\PointerByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */