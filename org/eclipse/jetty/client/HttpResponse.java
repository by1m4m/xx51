/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpVersion;
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
/*     */ public class HttpResponse
/*     */   implements Response
/*     */ {
/*  32 */   private final HttpFields headers = new HttpFields();
/*     */   private final Request request;
/*     */   private final List<Response.ResponseListener> listeners;
/*     */   private HttpVersion version;
/*     */   private int status;
/*     */   private String reason;
/*     */   private HttpFields trailers;
/*     */   
/*     */   public HttpResponse(Request request, List<Response.ResponseListener> listeners)
/*     */   {
/*  42 */     this.request = request;
/*  43 */     this.listeners = listeners;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request getRequest()
/*     */   {
/*  49 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpVersion getVersion()
/*     */   {
/*  55 */     return this.version;
/*     */   }
/*     */   
/*     */   public HttpResponse version(HttpVersion version)
/*     */   {
/*  60 */     this.version = version;
/*  61 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStatus()
/*     */   {
/*  67 */     return this.status;
/*     */   }
/*     */   
/*     */   public HttpResponse status(int status)
/*     */   {
/*  72 */     this.status = status;
/*  73 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getReason()
/*     */   {
/*  79 */     return this.reason;
/*     */   }
/*     */   
/*     */   public HttpResponse reason(String reason)
/*     */   {
/*  84 */     this.reason = reason;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpFields getHeaders()
/*     */   {
/*  91 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T extends Response.ResponseListener> List<T> getListeners(Class<T> type)
/*     */   {
/*  97 */     ArrayList<T> result = new ArrayList();
/*  98 */     for (Response.ResponseListener listener : this.listeners)
/*  99 */       if ((type == null) || (type.isInstance(listener)))
/* 100 */         result.add(listener);
/* 101 */     return result;
/*     */   }
/*     */   
/*     */   public HttpFields getTrailers()
/*     */   {
/* 106 */     return this.trailers;
/*     */   }
/*     */   
/*     */   public HttpResponse trailer(HttpField trailer)
/*     */   {
/* 111 */     if (this.trailers == null)
/* 112 */       this.trailers = new HttpFields();
/* 113 */     this.trailers.add(trailer);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean abort(Throwable cause)
/*     */   {
/* 120 */     return this.request.abort(cause);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     return String.format("%s[%s %d %s]@%x", new Object[] { HttpResponse.class.getSimpleName(), getVersion(), Integer.valueOf(getStatus()), getReason(), Integer.valueOf(hashCode()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */