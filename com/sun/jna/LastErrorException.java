/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LastErrorException
/*    */   extends RuntimeException
/*    */ {
/*    */   private int errorCode;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static String formatMessage(int code)
/*    */   {
/* 25 */     return "errno was " + code;
/*    */   }
/*    */   
/*    */   private static String parseMessage(String m)
/*    */   {
/*    */     try
/*    */     {
/* 32 */       return formatMessage(Integer.parseInt(m));
/*    */     }
/*    */     catch (NumberFormatException e) {}
/* 35 */     return m;
/*    */   }
/*    */   
/*    */   public LastErrorException(String msg)
/*    */   {
/* 40 */     super(parseMessage(msg.trim()));
/*    */     try {
/* 42 */       if (msg.startsWith("[")) {
/* 43 */         msg = msg.substring(1, msg.indexOf("]"));
/*    */       }
/* 45 */       this.errorCode = Integer.parseInt(msg);
/*    */     }
/*    */     catch (NumberFormatException e) {
/* 48 */       this.errorCode = -1;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getErrorCode()
/*    */   {
/* 58 */     return this.errorCode;
/*    */   }
/*    */   
/*    */   public LastErrorException(int code) {
/* 62 */     super(formatMessage(code));
/* 63 */     this.errorCode = code;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\LastErrorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */