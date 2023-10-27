/*    */ package com.sun.jna.ptr;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
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
/*    */ public class NativeLongByReference
/*    */   extends ByReference
/*    */ {
/*    */   public NativeLongByReference()
/*    */   {
/* 19 */     this(new NativeLong(0L));
/*    */   }
/*    */   
/*    */   public NativeLongByReference(NativeLong value) {
/* 23 */     super(NativeLong.SIZE);
/* 24 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(NativeLong value) {
/* 28 */     getPointer().setNativeLong(0L, value);
/*    */   }
/*    */   
/*    */   public NativeLong getValue() {
/* 32 */     return getPointer().getNativeLong(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\NativeLongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */