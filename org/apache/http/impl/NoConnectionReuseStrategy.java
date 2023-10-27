/*    */ package org.apache.http.impl;
/*    */ 
/*    */ import org.apache.http.ConnectionReuseStrategy;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ 
/*    */ public class NoConnectionReuseStrategy
/*    */   implements ConnectionReuseStrategy
/*    */ {
/*    */   public boolean keepAlive(HttpResponse response, HttpContext context)
/*    */   {
/* 54 */     if (response == null) {
/* 55 */       throw new IllegalArgumentException("HTTP response may not be null");
/*    */     }
/* 57 */     if (context == null) {
/* 58 */       throw new IllegalArgumentException("HTTP context may not be null");
/*    */     }
/*    */     
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\NoConnectionReuseStrategy.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */