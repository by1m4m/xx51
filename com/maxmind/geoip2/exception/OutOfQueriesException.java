/*    */ package com.maxmind.geoip2.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OutOfQueriesException
/*    */   extends GeoIp2Exception
/*    */ {
/*    */   private static final long serialVersionUID = 3843736987256336967L;
/*    */   
/*    */ 
/*    */ 
/*    */   public OutOfQueriesException(String message)
/*    */   {
/* 14 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public OutOfQueriesException(String message, Throwable e)
/*    */   {
/* 22 */     super(message, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\exception\OutOfQueriesException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */