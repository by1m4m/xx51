/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public abstract class SslCompletionEvent
/*    */ {
/*    */   private final Throwable cause;
/*    */   
/*    */   SslCompletionEvent()
/*    */   {
/* 25 */     this.cause = null;
/*    */   }
/*    */   
/*    */   SslCompletionEvent(Throwable cause) {
/* 29 */     this.cause = ((Throwable)ObjectUtil.checkNotNull(cause, "cause"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean isSuccess()
/*    */   {
/* 36 */     return this.cause == null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Throwable cause()
/*    */   {
/* 44 */     return this.cause;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 49 */     Throwable cause = cause();
/* 50 */     return 
/* 51 */       getClass().getSimpleName() + '(' + cause + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\SslCompletionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */