/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.Listener;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.client.util.BufferingResponseListener;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.http.HttpHeaderValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContinueProtocolHandler
/*     */   implements ProtocolHandler
/*     */ {
/*     */   public static final String NAME = "continue";
/*  37 */   private static final String ATTRIBUTE = ContinueProtocolHandler.class.getName() + ".100continue";
/*     */   
/*     */   private final ResponseNotifier notifier;
/*     */   
/*     */   public ContinueProtocolHandler()
/*     */   {
/*  43 */     this.notifier = new ResponseNotifier();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/*  49 */     return "continue";
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean accept(Request request, Response response)
/*     */   {
/*  55 */     boolean is100 = response.getStatus() == 100;
/*  56 */     boolean expect100 = request.getHeaders().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
/*  57 */     HttpConversation conversation = ((HttpRequest)request).getConversation();
/*  58 */     boolean handled100 = conversation.getAttribute(ATTRIBUTE) != null;
/*  59 */     return ((is100) || (expect100)) && (!handled100);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Response.Listener getResponseListener()
/*     */   {
/*  66 */     return new ContinueListener();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onContinue(Request request) {}
/*     */   
/*     */ 
/*     */   protected class ContinueListener
/*     */     extends BufferingResponseListener
/*     */   {
/*     */     protected ContinueListener() {}
/*     */     
/*     */ 
/*     */     public void onSuccess(Response response)
/*     */     {
/*  81 */       Request request = response.getRequest();
/*  82 */       HttpConversation conversation = ((HttpRequest)request).getConversation();
/*     */       
/*  84 */       conversation.setAttribute(ContinueProtocolHandler.ATTRIBUTE, Boolean.TRUE);
/*     */       
/*     */ 
/*  87 */       conversation.updateResponseListeners(null);
/*     */       
/*  89 */       HttpExchange exchange = (HttpExchange)conversation.getExchanges().peekLast();
/*  90 */       assert (exchange.getResponse() == response);
/*  91 */       switch (response.getStatus())
/*     */       {
/*     */ 
/*     */ 
/*     */       case 100: 
/*  96 */         exchange.resetResponse();
/*  97 */         exchange.proceed(null);
/*  98 */         ContinueProtocolHandler.this.onContinue(request);
/*  99 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       default: 
/* 106 */         List<Response.ResponseListener> listeners = exchange.getResponseListeners();
/* 107 */         HttpContentResponse contentResponse = new HttpContentResponse(response, getContent(), getMediaType(), getEncoding());
/* 108 */         ContinueProtocolHandler.this.notifier.forwardSuccess(listeners, contentResponse);
/* 109 */         exchange.proceed(new HttpRequestException("Expectation failed", request));
/* 110 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */     public void onFailure(Response response, Throwable failure)
/*     */     {
/* 118 */       HttpConversation conversation = ((HttpRequest)response.getRequest()).getConversation();
/*     */       
/* 120 */       conversation.setAttribute(ContinueProtocolHandler.ATTRIBUTE, Boolean.TRUE);
/*     */       
/* 122 */       conversation.updateResponseListeners(null);
/*     */       
/* 124 */       HttpExchange exchange = (HttpExchange)conversation.getExchanges().peekLast();
/* 125 */       assert (exchange.getResponse() == response);
/* 126 */       List<Response.ResponseListener> listeners = exchange.getResponseListeners();
/* 127 */       HttpContentResponse contentResponse = new HttpContentResponse(response, getContent(), getMediaType(), getEncoding());
/* 128 */       ContinueProtocolHandler.this.notifier.forwardFailureComplete(listeners, exchange.getRequest(), exchange.getRequestFailure(), contentResponse, failure);
/*     */     }
/*     */     
/*     */     public void onComplete(Result result) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ContinueProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */