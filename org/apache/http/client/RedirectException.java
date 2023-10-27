/*    */ package org.apache.http.client;
/*    */ 
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class RedirectException
/*    */   extends ProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = 4418824536372559326L;
/*    */   
/*    */   public RedirectException() {}
/*    */   
/*    */   public RedirectException(String message)
/*    */   {
/* 57 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RedirectException(String message, Throwable cause)
/*    */   {
/* 68 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\RedirectException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */