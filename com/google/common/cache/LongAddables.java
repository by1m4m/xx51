/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Supplier;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ @GwtCompatible(emulated=true)
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static
/*    */   {
/*    */     Supplier<LongAddable> supplier;
/*    */     try
/*    */     {
/* 33 */       new LongAdder();
/* 34 */       supplier = new Supplier()
/*    */       {
/*    */ 
/*    */         public LongAddable get() {
/* 38 */           return new LongAdder(); }
/*    */       };
/*    */     } catch (Throwable t) {
/*    */       Supplier<LongAddable> supplier;
/* 42 */       supplier = new Supplier()
/*    */       {
/*    */         public LongAddable get()
/*    */         {
/* 46 */           return new LongAddables.PureJavaLongAddable(null);
/*    */         }
/*    */       };
/*    */     }
/* 50 */     SUPPLIER = supplier;
/*    */   }
/*    */   
/*    */   public static LongAddable create() {
/* 54 */     return (LongAddable)SUPPLIER.get();
/*    */   }
/*    */   
/*    */   private static final class PureJavaLongAddable extends AtomicLong implements LongAddable
/*    */   {
/*    */     public void increment() {
/* 60 */       getAndIncrement();
/*    */     }
/*    */     
/*    */     public void add(long x)
/*    */     {
/* 65 */       getAndAdd(x);
/*    */     }
/*    */     
/*    */     public long sum()
/*    */     {
/* 70 */       return get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\LongAddables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */