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
/*    */ public class LongByReference
/*    */   extends ByReference
/*    */ {
/*    */   public LongByReference()
/*    */   {
/* 17 */     this(0L);
/*    */   }
/*    */   
/*    */   public LongByReference(long value) {
/* 21 */     super(8);
/* 22 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(long value) {
/* 26 */     getPointer().setLong(0L, value);
/*    */   }
/*    */   
/*    */   public long getValue() {
/* 30 */     return getPointer().getLong(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\LongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */