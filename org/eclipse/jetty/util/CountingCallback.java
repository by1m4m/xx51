/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CountingCallback
/*    */   extends Callback.Nested
/*    */ {
/*    */   private final AtomicInteger count;
/*    */   
/*    */   public CountingCallback(Callback callback, int count)
/*    */   {
/* 47 */     super(callback);
/* 48 */     if (count < 1)
/* 49 */       throw new IllegalArgumentException();
/* 50 */     this.count = new AtomicInteger(count);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void succeeded()
/*    */   {
/*    */     for (;;)
/*    */     {
/* 59 */       int current = this.count.get();
/*    */       
/*    */ 
/* 62 */       if (current == 0) {
/* 63 */         return;
/*    */       }
/* 65 */       if (this.count.compareAndSet(current, current - 1))
/*    */       {
/* 67 */         if (current == 1)
/* 68 */           super.succeeded();
/* 69 */         return;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void failed(Throwable failure)
/*    */   {
/*    */     for (;;)
/*    */     {
/* 80 */       int current = this.count.get();
/*    */       
/*    */ 
/* 83 */       if (current == 0) {
/* 84 */         return;
/*    */       }
/* 86 */       if (this.count.compareAndSet(current, 0))
/*    */       {
/* 88 */         super.failed(failure);
/* 89 */         return;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 97 */     return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\CountingCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */