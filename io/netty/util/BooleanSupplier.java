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
/*    */ 
/*    */ public abstract interface BooleanSupplier
/*    */ {
/* 32 */   public static final BooleanSupplier FALSE_SUPPLIER = new BooleanSupplier()
/*    */   {
/*    */     public boolean get() {
/* 35 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public static final BooleanSupplier TRUE_SUPPLIER = new BooleanSupplier()
/*    */   {
/*    */     public boolean get() {
/* 45 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean get()
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\BooleanSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */