/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import org.eclipse.jetty.client.api.Request;
/*    */ import org.eclipse.jetty.client.api.Response;
/*    */ import org.eclipse.jetty.client.api.Response.Listener;
/*    */ import org.eclipse.jetty.client.api.Response.Listener.Adapter;
/*    */ import org.eclipse.jetty.client.api.Result;
/*    */ import org.eclipse.jetty.http.HttpField;
/*    */ import org.eclipse.jetty.http.HttpHeader;
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
/*    */ public class RedirectProtocolHandler
/*    */   extends Response.Listener.Adapter
/*    */   implements ProtocolHandler
/*    */ {
/*    */   public static final String NAME = "redirect";
/*    */   private final HttpRedirector redirector;
/*    */   
/*    */   public RedirectProtocolHandler(HttpClient client)
/*    */   {
/* 38 */     this.redirector = new HttpRedirector(client);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 44 */     return "redirect";
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean accept(Request request, Response response)
/*    */   {
/* 50 */     return (this.redirector.isRedirect(response)) && (request.isFollowRedirects());
/*    */   }
/*    */   
/*    */ 
/*    */   public Response.Listener getResponseListener()
/*    */   {
/* 56 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onHeader(Response response, HttpField field)
/*    */   {
/* 64 */     return field.getHeader() != HttpHeader.CONTENT_ENCODING;
/*    */   }
/*    */   
/*    */ 
/*    */   public void onComplete(Result result)
/*    */   {
/* 70 */     Request request = result.getRequest();
/* 71 */     Response response = result.getResponse();
/* 72 */     if (result.isSucceeded()) {
/* 73 */       this.redirector.redirect(request, response, null);
/*    */     } else {
/* 75 */       this.redirector.fail(request, response, result.getFailure());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\RedirectProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */