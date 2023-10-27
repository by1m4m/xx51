/*    */ package org.eclipse.jetty.io;
/*    */ 
/*    */ import java.io.EOFException;
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
/*    */ public class EofException
/*    */   extends EOFException
/*    */   implements QuietException
/*    */ {
/*    */   public EofException() {}
/*    */   
/*    */   public EofException(String reason)
/*    */   {
/* 38 */     super(reason);
/*    */   }
/*    */   
/*    */   public EofException(Throwable th)
/*    */   {
/* 43 */     if (th != null) {
/* 44 */       initCause(th);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\EofException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */