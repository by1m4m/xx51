/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.net.CookieStore;
/*     */ import java.net.HttpCookie;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.eclipse.jetty.client.api.Authentication.Result;
/*     */ import org.eclipse.jetty.client.api.AuthenticationStore;
/*     */ import org.eclipse.jetty.client.api.Connection;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.client.api.ContentProvider.Typed;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response.CompleteListener;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ import org.eclipse.jetty.util.HttpCookieStore;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public abstract class HttpConnection
/*     */   implements Connection
/*     */ {
/*  43 */   private static final Logger LOG = Log.getLogger(HttpConnection.class);
/*     */   
/*     */   private final HttpDestination destination;
/*     */   private int idleTimeoutGuard;
/*     */   private long idleTimeoutStamp;
/*     */   
/*     */   protected HttpConnection(HttpDestination destination)
/*     */   {
/*  51 */     this.destination = destination;
/*  52 */     this.idleTimeoutStamp = System.nanoTime();
/*     */   }
/*     */   
/*     */   public HttpClient getHttpClient()
/*     */   {
/*  57 */     return this.destination.getHttpClient();
/*     */   }
/*     */   
/*     */   public HttpDestination getHttpDestination()
/*     */   {
/*  62 */     return this.destination;
/*     */   }
/*     */   
/*     */ 
/*     */   public void send(Request request, Response.CompleteListener listener)
/*     */   {
/*  68 */     HttpRequest httpRequest = (HttpRequest)request;
/*     */     
/*  70 */     ArrayList<Response.ResponseListener> listeners = new ArrayList(httpRequest.getResponseListeners());
/*     */     
/*  72 */     httpRequest.sent();
/*  73 */     if (listener != null) {
/*  74 */       listeners.add(listener);
/*     */     }
/*  76 */     HttpExchange exchange = new HttpExchange(getHttpDestination(), httpRequest, listeners);
/*     */     
/*  78 */     SendFailure result = send(exchange);
/*  79 */     if (result != null) {
/*  80 */       httpRequest.abort(result.failure);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract SendFailure send(HttpExchange paramHttpExchange);
/*     */   
/*     */   protected void normalizeRequest(Request request) {
/*  87 */     HttpVersion version = request.getVersion();
/*  88 */     HttpFields headers = request.getHeaders();
/*  89 */     ContentProvider content = request.getContent();
/*  90 */     ProxyConfiguration.Proxy proxy = this.destination.getProxy();
/*     */     
/*     */ 
/*  93 */     String path = request.getPath();
/*  94 */     if (path.trim().length() == 0)
/*     */     {
/*  96 */       path = "/";
/*  97 */       request.path(path);
/*     */     }
/*     */     
/* 100 */     URI uri = request.getURI();
/*     */     
/* 102 */     if (((proxy instanceof HttpProxy)) && (!HttpClient.isSchemeSecure(request.getScheme())) && (uri != null))
/*     */     {
/* 104 */       path = uri.toString();
/* 105 */       request.path(path);
/*     */     }
/*     */     
/*     */ 
/* 109 */     if (version.getVersion() <= 11)
/*     */     {
/* 111 */       if (!headers.containsKey(HttpHeader.HOST.asString())) {
/* 112 */         headers.put(getHttpDestination().getHostField());
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (content != null)
/*     */     {
/* 118 */       if (!headers.containsKey(HttpHeader.CONTENT_TYPE.asString()))
/*     */       {
/* 120 */         String contentType = null;
/* 121 */         if ((content instanceof ContentProvider.Typed))
/* 122 */           contentType = ((ContentProvider.Typed)content).getContentType();
/* 123 */         if (contentType != null)
/*     */         {
/* 125 */           headers.put(HttpHeader.CONTENT_TYPE, contentType);
/*     */         }
/*     */         else
/*     */         {
/* 129 */           contentType = getHttpClient().getDefaultRequestContentType();
/* 130 */           if (contentType != null)
/* 131 */             headers.put(HttpHeader.CONTENT_TYPE, contentType);
/*     */         }
/*     */       }
/* 134 */       long contentLength = content.getLength();
/* 135 */       if (contentLength >= 0L)
/*     */       {
/* 137 */         if (!headers.containsKey(HttpHeader.CONTENT_LENGTH.asString())) {
/* 138 */           headers.put(HttpHeader.CONTENT_LENGTH, String.valueOf(contentLength));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 143 */     CookieStore cookieStore = getHttpClient().getCookieStore();
/* 144 */     if (cookieStore != null)
/*     */     {
/* 146 */       StringBuilder cookies = null;
/* 147 */       if (uri != null)
/* 148 */         cookies = convertCookies(HttpCookieStore.matchPath(uri, cookieStore.get(uri)), null);
/* 149 */       cookies = convertCookies(request.getCookies(), cookies);
/* 150 */       if (cookies != null) {
/* 151 */         request.header(HttpHeader.COOKIE.asString(), cookies.toString());
/*     */       }
/*     */     }
/*     */     
/* 155 */     applyAuthentication(request, proxy != null ? proxy.getURI() : null);
/* 156 */     applyAuthentication(request, uri);
/*     */   }
/*     */   
/*     */   private StringBuilder convertCookies(List<HttpCookie> cookies, StringBuilder builder)
/*     */   {
/* 161 */     for (HttpCookie cookie : cookies)
/*     */     {
/* 163 */       if (builder == null)
/* 164 */         builder = new StringBuilder();
/* 165 */       if (builder.length() > 0)
/* 166 */         builder.append("; ");
/* 167 */       builder.append(cookie.getName()).append("=").append(cookie.getValue());
/*     */     }
/* 169 */     return builder;
/*     */   }
/*     */   
/*     */   private void applyAuthentication(Request request, URI uri)
/*     */   {
/* 174 */     if (uri != null)
/*     */     {
/* 176 */       Authentication.Result result = getHttpClient().getAuthenticationStore().findAuthenticationResult(uri);
/* 177 */       if (result != null) {
/* 178 */         result.apply(request);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SendFailure send(HttpChannel channel, HttpExchange exchange)
/*     */   {
/* 188 */     synchronized (this)
/*     */     {
/* 190 */       boolean send = this.idleTimeoutGuard >= 0;
/* 191 */       if (send)
/* 192 */         this.idleTimeoutGuard += 1;
/*     */     }
/*     */     boolean send;
/* 195 */     if (send)
/*     */     {
/* 197 */       HttpRequest request = exchange.getRequest();
/*     */       SendFailure result;
/* 199 */       SendFailure result; if (channel.associate(exchange))
/*     */       {
/* 201 */         channel.send();
/* 202 */         result = null;
/*     */       }
/*     */       else
/*     */       {
/* 206 */         channel.release();
/* 207 */         result = new SendFailure(new HttpRequestException("Could not associate request to connection", request), false);
/*     */       }
/*     */       
/* 210 */       synchronized (this)
/*     */       {
/* 212 */         this.idleTimeoutGuard -= 1;
/* 213 */         this.idleTimeoutStamp = System.nanoTime();
/*     */       }
/*     */       
/* 216 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 220 */     return new SendFailure(new TimeoutException(), true);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean onIdleTimeout(long idleTimeout)
/*     */   {
/* 226 */     synchronized (this)
/*     */     {
/* 228 */       if (this.idleTimeoutGuard == 0)
/*     */       {
/* 230 */         long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.idleTimeoutStamp);
/* 231 */         boolean idle = elapsed > idleTimeout / 2L;
/* 232 */         if (idle)
/* 233 */           this.idleTimeoutGuard = -1;
/* 234 */         if (LOG.isDebugEnabled())
/* 235 */           LOG.debug("Idle timeout {}/{}ms - {}", new Object[] { Long.valueOf(elapsed), Long.valueOf(idleTimeout), this });
/* 236 */         return idle;
/*     */       }
/*     */       
/*     */ 
/* 240 */       if (LOG.isDebugEnabled())
/* 241 */         LOG.debug("Idle timeout skipped - {}", new Object[] { this });
/* 242 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 250 */     return String.format("%s@%h", new Object[] { getClass().getSimpleName(), this });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */