/*    */ package com.maxmind.geoip2.exception;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class InvalidRequestException
/*    */   extends GeoIp2Exception
/*    */ {
/*    */   private static final long serialVersionUID = 8662062420258379643L;
/*    */   private final String code;
/*    */   private final URL url;
/*    */   
/*    */   public InvalidRequestException(String message, String code, URL url)
/*    */   {
/* 21 */     super(message);
/* 22 */     this.url = url;
/* 23 */     this.code = code;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InvalidRequestException(String message, String code, int httpStatus, URL url, Throwable e)
/*    */   {
/* 35 */     super(message, e);
/* 36 */     this.code = code;
/* 37 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getCode()
/*    */   {
/* 44 */     return this.code;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public URL getUrl()
/*    */   {
/* 51 */     return this.url;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\exception\InvalidRequestException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */