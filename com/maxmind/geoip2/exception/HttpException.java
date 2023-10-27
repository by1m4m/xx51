/*    */ package com.maxmind.geoip2.exception;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -8301101841509056974L;
/*    */   private final int httpStatus;
/*    */   private final URL url;
/*    */   
/*    */   public HttpException(String message, int httpStatus, URL url)
/*    */   {
/* 22 */     super(message);
/* 23 */     this.httpStatus = httpStatus;
/* 24 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpException(String message, int httpStatus, URL url, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/* 36 */     this.httpStatus = httpStatus;
/* 37 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getHttpStatus()
/*    */   {
/* 44 */     return this.httpStatus;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public URL getUrl()
/*    */   {
/* 51 */     return this.url;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\exception\HttpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */