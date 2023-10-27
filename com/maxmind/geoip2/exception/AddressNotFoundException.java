/*    */ package com.maxmind.geoip2.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AddressNotFoundException
/*    */   extends GeoIp2Exception
/*    */ {
/*    */   private static final long serialVersionUID = -639962574626980783L;
/*    */   
/*    */ 
/*    */ 
/*    */   public AddressNotFoundException(String message)
/*    */   {
/* 15 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AddressNotFoundException(String message, Throwable e)
/*    */   {
/* 23 */     super(message, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\exception\AddressNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */