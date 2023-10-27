/*    */ package org.apache.commons.net.ftp.parser;
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
/*    */ public class ParserInitializationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 5563335279583210658L;
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
/*    */   public ParserInitializationException(String message)
/*    */   {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ParserInitializationException(String message, Throwable rootCause)
/*    */   {
/* 47 */     super(message, rootCause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public Throwable getRootCause()
/*    */   {
/* 59 */     return super.getCause();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\parser\ParserInitializationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */