/*    */ package org.eclipse.jetty.client.http;
/*    */ 
/*    */ import org.eclipse.jetty.client.HttpClient;
/*    */ import org.eclipse.jetty.client.HttpExchange;
/*    */ import org.eclipse.jetty.client.Origin;
/*    */ import org.eclipse.jetty.client.PoolingHttpDestination;
/*    */ import org.eclipse.jetty.client.SendFailure;
/*    */ import org.eclipse.jetty.client.api.Connection;
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
/*    */ public class HttpDestinationOverHTTP
/*    */   extends PoolingHttpDestination
/*    */ {
/*    */   public HttpDestinationOverHTTP(HttpClient client, Origin origin)
/*    */   {
/* 32 */     super(client, origin);
/*    */   }
/*    */   
/*    */ 
/*    */   protected SendFailure send(Connection connection, HttpExchange exchange)
/*    */   {
/* 38 */     return ((HttpConnectionOverHTTP)connection).send(exchange);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpDestinationOverHTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */