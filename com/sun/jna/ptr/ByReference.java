/*    */ package com.sun.jna.ptr;
/*    */ 
/*    */ import com.sun.jna.Memory;
/*    */ import com.sun.jna.PointerType;
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
/*    */ public abstract class ByReference
/*    */   extends PointerType
/*    */ {
/*    */   protected ByReference(int dataSize)
/*    */   {
/* 31 */     setPointer(new Memory(dataSize));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ptr\ByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */