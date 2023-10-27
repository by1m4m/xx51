/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*    */ 
/*    */ public abstract class IteratingNestedCallback
/*    */   extends IteratingCallback
/*    */ {
/*    */   final Callback _callback;
/*    */   
/*    */   public IteratingNestedCallback(Callback callback)
/*    */   {
/* 48 */     this._callback = callback;
/*    */   }
/*    */   
/*    */ 
/*    */   public Invocable.InvocationType getInvocationType()
/*    */   {
/* 54 */     return this._callback.getInvocationType();
/*    */   }
/*    */   
/*    */ 
/*    */   protected void onCompleteSuccess()
/*    */   {
/* 60 */     this._callback.succeeded();
/*    */   }
/*    */   
/*    */ 
/*    */   protected void onCompleteFailure(Throwable x)
/*    */   {
/* 66 */     this._callback.failed(x);
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\IteratingNestedCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */