/*    */ package io.netty.handler.ssl;
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
/*    */ public final class SslCloseCompletionEvent
/*    */   extends SslCompletionEvent
/*    */ {
/* 23 */   public static final SslCloseCompletionEvent SUCCESS = new SslCloseCompletionEvent();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private SslCloseCompletionEvent() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SslCloseCompletionEvent(Throwable cause)
/*    */   {
/* 35 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\SslCloseCompletionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */