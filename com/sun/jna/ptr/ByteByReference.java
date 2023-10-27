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
/*    */ public class ByteByReference
/*    */   extends ByReference
/*    */ {
/*    */   public ByteByReference()
/*    */   {
/* 18 */     this((byte)0);
/*    */   }
/*    */   
/*    */   public ByteByReference(byte value) {
/* 22 */     super(1);
/* 23 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(byte value) {
/* 27 */     getPointer().setByte(0L, value);
/*    */   }
/*    */   
/*    */   public byte getValue() {
/* 31 */     return getPointer().getByte(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\ByteByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */