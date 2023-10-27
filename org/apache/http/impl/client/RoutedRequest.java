/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.conn.routing.HttpRoute;
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
/*    */ @NotThreadSafe
/*    */ public class RoutedRequest
/*    */ {
/*    */   protected final RequestWrapper request;
/*    */   protected final HttpRoute route;
/*    */   
/*    */   public RoutedRequest(RequestWrapper req, HttpRoute route)
/*    */   {
/* 53 */     this.request = req;
/* 54 */     this.route = route;
/*    */   }
/*    */   
/*    */   public final RequestWrapper getRequest() {
/* 58 */     return this.request;
/*    */   }
/*    */   
/*    */   public final HttpRoute getRoute() {
/* 62 */     return this.route;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\client\RoutedRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */