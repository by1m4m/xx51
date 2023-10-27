/*    */ package org.eclipse.jetty.io;
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
/*    */ public class RuntimeIOException
/*    */   extends RuntimeException
/*    */ {
/*    */   public RuntimeIOException() {}
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
/*    */   public RuntimeIOException(String message)
/*    */   {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public RuntimeIOException(Throwable cause)
/*    */   {
/* 41 */     super(cause);
/*    */   }
/*    */   
/*    */   public RuntimeIOException(String message, Throwable cause)
/*    */   {
/* 46 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\RuntimeIOException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */