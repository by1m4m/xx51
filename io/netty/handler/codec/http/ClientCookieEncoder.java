/*    */ package io.netty.handler.codec.http;
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
/*    */ @Deprecated
/*    */ public final class ClientCookieEncoder
/*    */ {
/*    */   @Deprecated
/*    */   public static String encode(String name, String value)
/*    */   {
/* 49 */     return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(name, value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static String encode(Cookie cookie)
/*    */   {
/* 60 */     return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookie);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static String encode(Cookie... cookies)
/*    */   {
/* 71 */     return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookies);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static String encode(Iterable<Cookie> cookies)
/*    */   {
/* 82 */     return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookies);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\ClientCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */