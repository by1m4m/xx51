/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
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
/*    */ 
/*    */ @Deprecated
/*    */ public final class ServerCookieEncoder
/*    */ {
/*    */   @Deprecated
/*    */   public static String encode(String name, String value)
/*    */   {
/* 53 */     return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(name, value);
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
/* 64 */     return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static List<String> encode(Cookie... cookies)
/*    */   {
/* 75 */     return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static List<String> encode(Collection<Cookie> cookies)
/*    */   {
/* 86 */     return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static List<String> encode(Iterable<Cookie> cookies)
/*    */   {
/* 97 */     return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\ServerCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */