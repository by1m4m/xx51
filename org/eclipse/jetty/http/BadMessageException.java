/*    */ package org.eclipse.jetty.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BadMessageException
/*    */   extends RuntimeException
/*    */ {
/*    */   final int _code;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   final String _reason;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BadMessageException()
/*    */   {
/* 35 */     this(400, null);
/*    */   }
/*    */   
/*    */   public BadMessageException(int code)
/*    */   {
/* 40 */     this(code, null);
/*    */   }
/*    */   
/*    */   public BadMessageException(String reason)
/*    */   {
/* 45 */     this(400, reason);
/*    */   }
/*    */   
/*    */   public BadMessageException(String reason, Throwable cause)
/*    */   {
/* 50 */     this(400, reason, cause);
/*    */   }
/*    */   
/*    */   public BadMessageException(int code, String reason)
/*    */   {
/* 55 */     super(code + ": " + reason);
/* 56 */     this._code = code;
/* 57 */     this._reason = reason;
/*    */   }
/*    */   
/*    */   public BadMessageException(int code, String reason, Throwable cause)
/*    */   {
/* 62 */     super(code + ": " + reason, cause);
/* 63 */     this._code = code;
/* 64 */     this._reason = reason;
/*    */   }
/*    */   
/*    */   public int getCode()
/*    */   {
/* 69 */     return this._code;
/*    */   }
/*    */   
/*    */   public String getReason()
/*    */   {
/* 74 */     return this._reason;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\BadMessageException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */