/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SendFailure
/*    */ {
/*    */   public final Throwable failure;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final boolean retry;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SendFailure(Throwable failure, boolean retry)
/*    */   {
/* 28 */     this.failure = failure;
/* 29 */     this.retry = retry;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 35 */     return String.format("%s[failure=%s,retry=%b]", new Object[] { super.toString(), this.failure, Boolean.valueOf(this.retry) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\SendFailure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */