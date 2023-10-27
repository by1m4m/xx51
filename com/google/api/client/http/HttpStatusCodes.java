/*    */ package com.google.api.client.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpStatusCodes
/*    */ {
/*    */   public static final int STATUS_CODE_OK = 200;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_NO_CONTENT = 204;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_MULTIPLE_CHOICES = 300;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_MOVED_PERMANENTLY = 301;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_FOUND = 302;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_SEE_OTHER = 303;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_NOT_MODIFIED = 304;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_TEMPORARY_REDIRECT = 307;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_UNAUTHORIZED = 401;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_FORBIDDEN = 403;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_NOT_FOUND = 404;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_SERVER_ERROR = 500;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_BAD_GATEWAY = 502;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE_SERVICE_UNAVAILABLE = 503;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isSuccess(int statusCode)
/*    */   {
/* 80 */     return (statusCode >= 200) && (statusCode < 300);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isRedirect(int statusCode)
/*    */   {
/* 90 */     switch (statusCode) {
/*    */     case 301: 
/*    */     case 302: 
/*    */     case 303: 
/*    */     case 307: 
/* 95 */       return true;
/*    */     }
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpStatusCodes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */