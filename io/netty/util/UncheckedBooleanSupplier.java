/*    */ package io.netty.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface UncheckedBooleanSupplier
/*    */   extends BooleanSupplier
/*    */ {
/* 32 */   public static final UncheckedBooleanSupplier FALSE_SUPPLIER = new UncheckedBooleanSupplier()
/*    */   {
/*    */     public boolean get() {
/* 35 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public static final UncheckedBooleanSupplier TRUE_SUPPLIER = new UncheckedBooleanSupplier()
/*    */   {
/*    */     public boolean get() {
/* 45 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean get();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\UncheckedBooleanSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */