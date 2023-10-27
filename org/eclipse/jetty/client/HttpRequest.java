/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpCookie;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.client.api.ContentResponse;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Request.BeginListener;
/*     */ import org.eclipse.jetty.client.api.Request.CommitListener;
/*     */ import org.eclipse.jetty.client.api.Request.ContentListener;
/*     */ import org.eclipse.jetty.client.api.Request.FailureListener;
/*     */ import org.eclipse.jetty.client.api.Request.HeadersListener;
/*     */ import org.eclipse.jetty.client.api.Request.Listener;
/*     */ import org.eclipse.jetty.client.api.Request.QueuedListener;
/*     */ import org.eclipse.jetty.client.api.Request.RequestListener;
/*     */ import org.eclipse.jetty.client.api.Request.SuccessListener;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.AsyncContentListener;
/*     */ import org.eclipse.jetty.client.api.Response.BeginListener;
/*     */ import org.eclipse.jetty.client.api.Response.CompleteListener;
/*     */ import org.eclipse.jetty.client.api.Response.ContentListener;
/*     */ import org.eclipse.jetty.client.api.Response.FailureListener;
/*     */ import org.eclipse.jetty.client.api.Response.HeaderListener;
/*     */ import org.eclipse.jetty.client.api.Response.HeadersListener;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.client.api.Response.SuccessListener;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.client.util.FutureResponseListener;
/*     */ import org.eclipse.jetty.client.util.PathContentProvider;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.http.HttpMethod;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.Fields;
/*     */ import org.eclipse.jetty.util.Fields.Field;
/*     */ 
/*     */ public class HttpRequest implements Request
/*     */ {
/*  63 */   private static final URI NULL_URI = URI.create("null:0");
/*     */   
/*  65 */   private final HttpFields headers = new HttpFields();
/*  66 */   private final Fields params = new Fields(true);
/*  67 */   private final List<Response.ResponseListener> responseListeners = new ArrayList();
/*  68 */   private final AtomicReference<Throwable> aborted = new AtomicReference();
/*     */   private final HttpClient client;
/*     */   private final HttpConversation conversation;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private URI uri;
/*     */   private String scheme;
/*     */   private String path;
/*     */   private String query;
/*  77 */   private String method = HttpMethod.GET.asString();
/*  78 */   private HttpVersion version = HttpVersion.HTTP_1_1;
/*     */   private long idleTimeout;
/*     */   private long timeout;
/*     */   private long timeoutAt;
/*     */   private ContentProvider content;
/*     */   private boolean followRedirects;
/*     */   private List<HttpCookie> cookies;
/*     */   private Map<String, Object> attributes;
/*     */   private List<Request.RequestListener> requestListeners;
/*     */   private BiFunction<Request, Request, Response.CompleteListener> pushListener;
/*     */   private Supplier<HttpFields> trailers;
/*     */   
/*     */   protected HttpRequest(HttpClient client, HttpConversation conversation, URI uri)
/*     */   {
/*  92 */     this.client = client;
/*  93 */     this.conversation = conversation;
/*  94 */     this.scheme = uri.getScheme();
/*  95 */     this.host = client.normalizeHost(uri.getHost());
/*  96 */     this.port = HttpClient.normalizePort(this.scheme, uri.getPort());
/*  97 */     this.path = uri.getRawPath();
/*  98 */     this.query = uri.getRawQuery();
/*  99 */     extractParams(this.query);
/*     */     
/* 101 */     followRedirects(client.isFollowRedirects());
/* 102 */     this.idleTimeout = client.getIdleTimeout();
/* 103 */     HttpField acceptEncodingField = client.getAcceptEncodingField();
/* 104 */     if (acceptEncodingField != null)
/* 105 */       this.headers.put(acceptEncodingField);
/* 106 */     HttpField userAgentField = client.getUserAgentField();
/* 107 */     if (userAgentField != null) {
/* 108 */       this.headers.put(userAgentField);
/*     */     }
/*     */   }
/*     */   
/*     */   protected HttpConversation getConversation() {
/* 113 */     return this.conversation;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 119 */     return this.scheme;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request scheme(String scheme)
/*     */   {
/* 125 */     this.scheme = scheme;
/* 126 */     this.uri = null;
/* 127 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 133 */     return this.host;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 139 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 145 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request method(HttpMethod method)
/*     */   {
/* 151 */     return method(method.asString());
/*     */   }
/*     */   
/*     */ 
/*     */   public Request method(String method)
/*     */   {
/* 157 */     this.method = ((String)Objects.requireNonNull(method)).toUpperCase(Locale.ENGLISH);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 164 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request path(String path)
/*     */   {
/* 170 */     URI uri = newURI(path);
/* 171 */     if (uri == null)
/*     */     {
/* 173 */       this.path = path;
/* 174 */       this.query = null;
/*     */     }
/*     */     else
/*     */     {
/* 178 */       String rawPath = uri.getRawPath();
/* 179 */       if (rawPath == null)
/* 180 */         rawPath = "";
/* 181 */       this.path = rawPath;
/* 182 */       String query = uri.getRawQuery();
/* 183 */       if (query != null)
/*     */       {
/* 185 */         this.query = query;
/* 186 */         this.params.clear();
/* 187 */         extractParams(query);
/*     */       }
/* 189 */       if (uri.isAbsolute())
/* 190 */         this.path = buildURI(false).toString();
/*     */     }
/* 192 */     this.uri = null;
/* 193 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 199 */     return this.query;
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 205 */     if (this.uri == null) {
/* 206 */       this.uri = buildURI(true);
/*     */     }
/*     */     
/* 209 */     boolean isNullURI = this.uri == NULL_URI;
/* 210 */     return isNullURI ? null : this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpVersion getVersion()
/*     */   {
/* 216 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request version(HttpVersion version)
/*     */   {
/* 222 */     this.version = ((HttpVersion)Objects.requireNonNull(version));
/* 223 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request param(String name, String value)
/*     */   {
/* 229 */     return param(name, value, false);
/*     */   }
/*     */   
/*     */   private Request param(String name, String value, boolean fromQuery)
/*     */   {
/* 234 */     this.params.add(name, value);
/* 235 */     if (!fromQuery)
/*     */     {
/*     */ 
/* 238 */       if (this.query != null) {
/* 239 */         this.query = (this.query + "&" + urlEncode(name) + "=" + urlEncode(value));
/*     */       } else
/* 241 */         this.query = buildQuery();
/* 242 */       this.uri = null;
/*     */     }
/* 244 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Fields getParams()
/*     */   {
/* 250 */     return new Fields(this.params, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAgent()
/*     */   {
/* 256 */     return this.headers.get(HttpHeader.USER_AGENT);
/*     */   }
/*     */   
/*     */ 
/*     */   public Request agent(String agent)
/*     */   {
/* 262 */     this.headers.put(HttpHeader.USER_AGENT, agent);
/* 263 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request accept(String... accepts)
/*     */   {
/* 269 */     StringBuilder result = new StringBuilder();
/* 270 */     for (String accept : accepts)
/*     */     {
/* 272 */       if (result.length() > 0)
/* 273 */         result.append(", ");
/* 274 */       result.append(accept);
/*     */     }
/* 276 */     if (result.length() > 0)
/* 277 */       this.headers.put(HttpHeader.ACCEPT, result.toString());
/* 278 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request header(String name, String value)
/*     */   {
/* 284 */     if (value == null) {
/* 285 */       this.headers.remove(name);
/*     */     } else
/* 287 */       this.headers.add(name, value);
/* 288 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request header(HttpHeader header, String value)
/*     */   {
/* 294 */     if (value == null) {
/* 295 */       this.headers.remove(header);
/*     */     } else
/* 297 */       this.headers.add(header, value);
/* 298 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<HttpCookie> getCookies()
/*     */   {
/* 304 */     return this.cookies != null ? this.cookies : Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */   public Request cookie(HttpCookie cookie)
/*     */   {
/* 310 */     if (this.cookies == null)
/* 311 */       this.cookies = new ArrayList();
/* 312 */     this.cookies.add(cookie);
/* 313 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request attribute(String name, Object value)
/*     */   {
/* 319 */     if (this.attributes == null)
/* 320 */       this.attributes = new HashMap(4);
/* 321 */     this.attributes.put(name, value);
/* 322 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Object> getAttributes()
/*     */   {
/* 328 */     return this.attributes != null ? this.attributes : Collections.emptyMap();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpFields getHeaders()
/*     */   {
/* 334 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends Request.RequestListener> List<T> getRequestListeners(Class<T> type)
/*     */   {
/* 343 */     if ((type == null) || (this.requestListeners == null)) {
/* 344 */       return this.requestListeners != null ? this.requestListeners : Collections.emptyList();
/*     */     }
/* 346 */     ArrayList<T> result = new ArrayList();
/* 347 */     for (Request.RequestListener listener : this.requestListeners)
/* 348 */       if (type.isInstance(listener))
/* 349 */         result.add(listener);
/* 350 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request listener(Request.Listener listener)
/*     */   {
/* 356 */     return requestListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestQueued(final Request.QueuedListener listener)
/*     */   {
/* 362 */     requestListener(new Request.QueuedListener()
/*     */     {
/*     */ 
/*     */       public void onQueued(Request request)
/*     */       {
/* 367 */         listener.onQueued(request);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestBegin(final Request.BeginListener listener)
/*     */   {
/* 375 */     requestListener(new Request.BeginListener()
/*     */     {
/*     */ 
/*     */       public void onBegin(Request request)
/*     */       {
/* 380 */         listener.onBegin(request);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestHeaders(final Request.HeadersListener listener)
/*     */   {
/* 388 */     requestListener(new Request.HeadersListener()
/*     */     {
/*     */ 
/*     */       public void onHeaders(Request request)
/*     */       {
/* 393 */         listener.onHeaders(request);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestCommit(final Request.CommitListener listener)
/*     */   {
/* 401 */     requestListener(new Request.CommitListener()
/*     */     {
/*     */ 
/*     */       public void onCommit(Request request)
/*     */       {
/* 406 */         listener.onCommit(request);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestContent(final Request.ContentListener listener)
/*     */   {
/* 414 */     requestListener(new Request.ContentListener()
/*     */     {
/*     */ 
/*     */       public void onContent(Request request, ByteBuffer content)
/*     */       {
/* 419 */         listener.onContent(request, content);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestSuccess(final Request.SuccessListener listener)
/*     */   {
/* 427 */     requestListener(new Request.SuccessListener()
/*     */     {
/*     */ 
/*     */       public void onSuccess(Request request)
/*     */       {
/* 432 */         listener.onSuccess(request);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onRequestFailure(final Request.FailureListener listener)
/*     */   {
/* 440 */     requestListener(new Request.FailureListener()
/*     */     {
/*     */ 
/*     */       public void onFailure(Request request, Throwable failure)
/*     */       {
/* 445 */         listener.onFailure(request, failure);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private Request requestListener(Request.RequestListener listener)
/*     */   {
/* 452 */     if (this.requestListeners == null)
/* 453 */       this.requestListeners = new ArrayList();
/* 454 */     this.requestListeners.add(listener);
/* 455 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseBegin(final Response.BeginListener listener)
/*     */   {
/* 461 */     this.responseListeners.add(new Response.BeginListener()
/*     */     {
/*     */ 
/*     */       public void onBegin(Response response)
/*     */       {
/* 466 */         listener.onBegin(response);
/*     */       }
/* 468 */     });
/* 469 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseHeader(final Response.HeaderListener listener)
/*     */   {
/* 475 */     this.responseListeners.add(new Response.HeaderListener()
/*     */     {
/*     */ 
/*     */       public boolean onHeader(Response response, HttpField field)
/*     */       {
/* 480 */         return listener.onHeader(response, field);
/*     */       }
/* 482 */     });
/* 483 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseHeaders(final Response.HeadersListener listener)
/*     */   {
/* 489 */     this.responseListeners.add(new Response.HeadersListener()
/*     */     {
/*     */ 
/*     */       public void onHeaders(Response response)
/*     */       {
/* 494 */         listener.onHeaders(response);
/*     */       }
/* 496 */     });
/* 497 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseContent(final Response.ContentListener listener)
/*     */   {
/* 503 */     this.responseListeners.add(new Response.AsyncContentListener()
/*     */     {
/*     */ 
/*     */       public void onContent(Response response, ByteBuffer content, Callback callback)
/*     */       {
/*     */         try
/*     */         {
/* 510 */           listener.onContent(response, content);
/* 511 */           callback.succeeded();
/*     */         }
/*     */         catch (Throwable x)
/*     */         {
/* 515 */           callback.failed(x);
/*     */         }
/*     */       }
/* 518 */     });
/* 519 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseContentAsync(final Response.AsyncContentListener listener)
/*     */   {
/* 525 */     this.responseListeners.add(new Response.AsyncContentListener()
/*     */     {
/*     */ 
/*     */       public void onContent(Response response, ByteBuffer content, Callback callback)
/*     */       {
/* 530 */         listener.onContent(response, content, callback);
/*     */       }
/* 532 */     });
/* 533 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseSuccess(final Response.SuccessListener listener)
/*     */   {
/* 539 */     this.responseListeners.add(new Response.SuccessListener()
/*     */     {
/*     */ 
/*     */       public void onSuccess(Response response)
/*     */       {
/* 544 */         listener.onSuccess(response);
/*     */       }
/* 546 */     });
/* 547 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onResponseFailure(final Response.FailureListener listener)
/*     */   {
/* 553 */     this.responseListeners.add(new Response.FailureListener()
/*     */     {
/*     */ 
/*     */       public void onFailure(Response response, Throwable failure)
/*     */       {
/* 558 */         listener.onFailure(response, failure);
/*     */       }
/* 560 */     });
/* 561 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request onComplete(final Response.CompleteListener listener)
/*     */   {
/* 567 */     this.responseListeners.add(new Response.CompleteListener()
/*     */     {
/*     */ 
/*     */       public void onComplete(Result result)
/*     */       {
/* 572 */         listener.onComplete(result);
/*     */       }
/* 574 */     });
/* 575 */     return this;
/*     */   }
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
/*     */   public Request pushListener(BiFunction<Request, Request, Response.CompleteListener> listener)
/*     */   {
/* 594 */     this.pushListener = listener;
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public HttpRequest trailers(Supplier<HttpFields> trailers)
/*     */   {
/* 600 */     this.trailers = trailers;
/* 601 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public ContentProvider getContent()
/*     */   {
/* 607 */     return this.content;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request content(ContentProvider content)
/*     */   {
/* 613 */     return content(content, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Request content(ContentProvider content, String contentType)
/*     */   {
/* 619 */     if (contentType != null)
/* 620 */       header(HttpHeader.CONTENT_TYPE, contentType);
/* 621 */     this.content = content;
/* 622 */     return this;
/*     */   }
/*     */   
/*     */   public Request file(Path file)
/*     */     throws IOException
/*     */   {
/* 628 */     return file(file, "application/octet-stream");
/*     */   }
/*     */   
/*     */   public Request file(Path file, String contentType)
/*     */     throws IOException
/*     */   {
/* 634 */     return content(new PathContentProvider(contentType, file));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFollowRedirects()
/*     */   {
/* 640 */     return this.followRedirects;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request followRedirects(boolean follow)
/*     */   {
/* 646 */     this.followRedirects = follow;
/* 647 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getIdleTimeout()
/*     */   {
/* 653 */     return this.idleTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request idleTimeout(long timeout, TimeUnit unit)
/*     */   {
/* 659 */     this.idleTimeout = unit.toMillis(timeout);
/* 660 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getTimeout()
/*     */   {
/* 666 */     return this.timeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request timeout(long timeout, TimeUnit unit)
/*     */   {
/* 672 */     this.timeout = unit.toMillis(timeout);
/* 673 */     return this;
/*     */   }
/*     */   
/*     */   public ContentResponse send()
/*     */     throws InterruptedException, TimeoutException, ExecutionException
/*     */   {
/* 679 */     FutureResponseListener listener = new FutureResponseListener(this);
/* 680 */     send(this, listener);
/*     */     
/*     */     try
/*     */     {
/* 684 */       return listener.get();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (ExecutionException x)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 697 */       if ((x.getCause() instanceof TimeoutException))
/*     */       {
/* 699 */         TimeoutException t = (TimeoutException)x.getCause();
/* 700 */         abort(t);
/* 701 */         throw t;
/*     */       }
/*     */       
/* 704 */       abort(x);
/* 705 */       throw x;
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*     */ 
/* 711 */       abort(x);
/* 712 */       throw x;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void send(Response.CompleteListener listener)
/*     */   {
/* 719 */     send(this, listener);
/*     */   }
/*     */   
/*     */   private void send(HttpRequest request, Response.CompleteListener listener)
/*     */   {
/* 724 */     if (listener != null)
/* 725 */       this.responseListeners.add(listener);
/* 726 */     sent();
/* 727 */     this.client.send(request, this.responseListeners);
/*     */   }
/*     */   
/*     */   void sent()
/*     */   {
/* 732 */     long timeout = getTimeout();
/* 733 */     this.timeoutAt = (timeout > 0L ? System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeout) : -1L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   long getTimeoutAt()
/*     */   {
/* 742 */     return this.timeoutAt;
/*     */   }
/*     */   
/*     */   protected List<Response.ResponseListener> getResponseListeners()
/*     */   {
/* 747 */     return this.responseListeners;
/*     */   }
/*     */   
/*     */   public BiFunction<Request, Request, Response.CompleteListener> getPushListener()
/*     */   {
/* 752 */     return this.pushListener;
/*     */   }
/*     */   
/*     */   public Supplier<HttpFields> getTrailers()
/*     */   {
/* 757 */     return this.trailers;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean abort(Throwable cause)
/*     */   {
/* 763 */     if (this.aborted.compareAndSet(null, (Throwable)Objects.requireNonNull(cause)))
/*     */     {
/* 765 */       if ((this.content instanceof Callback))
/* 766 */         ((Callback)this.content).failed(cause);
/* 767 */       return this.conversation.abort(cause);
/*     */     }
/* 769 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Throwable getAbortCause()
/*     */   {
/* 775 */     return (Throwable)this.aborted.get();
/*     */   }
/*     */   
/*     */   private String buildQuery()
/*     */   {
/* 780 */     StringBuilder result = new StringBuilder();
/* 781 */     for (Iterator<Fields.Field> iterator = this.params.iterator(); iterator.hasNext();)
/*     */     {
/* 783 */       Fields.Field field = (Fields.Field)iterator.next();
/* 784 */       List<String> values = field.getValues();
/* 785 */       for (int i = 0; i < values.size(); i++)
/*     */       {
/* 787 */         if (i > 0)
/* 788 */           result.append("&");
/* 789 */         result.append(field.getName()).append("=");
/* 790 */         result.append(urlEncode((String)values.get(i)));
/*     */       }
/* 792 */       if (iterator.hasNext())
/* 793 */         result.append("&");
/*     */     }
/* 795 */     return result.toString();
/*     */   }
/*     */   
/*     */   private String urlEncode(String value)
/*     */   {
/* 800 */     if (value == null) {
/* 801 */       return "";
/*     */     }
/* 803 */     String encoding = "utf-8";
/*     */     try
/*     */     {
/* 806 */       return URLEncoder.encode(value, encoding);
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 810 */       throw new UnsupportedCharsetException(encoding);
/*     */     }
/*     */   }
/*     */   
/*     */   private void extractParams(String query)
/*     */   {
/* 816 */     if (query != null)
/*     */     {
/* 818 */       for (String nameValue : query.split("&"))
/*     */       {
/* 820 */         String[] parts = nameValue.split("=");
/* 821 */         if (parts.length > 0)
/*     */         {
/* 823 */           String name = urlDecode(parts[0]);
/* 824 */           if (name.trim().length() != 0)
/*     */           {
/* 826 */             param(name, parts.length < 2 ? "" : urlDecode(parts[1]), true);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String urlDecode(String value) {
/* 834 */     String charset = "utf-8";
/*     */     try
/*     */     {
/* 837 */       return URLDecoder.decode(value, charset);
/*     */     }
/*     */     catch (UnsupportedEncodingException x)
/*     */     {
/* 841 */       throw new UnsupportedCharsetException(charset);
/*     */     }
/*     */   }
/*     */   
/*     */   private URI buildURI(boolean withQuery)
/*     */   {
/* 847 */     String path = getPath();
/* 848 */     String query = getQuery();
/* 849 */     if ((query != null) && (withQuery))
/* 850 */       path = path + "?" + query;
/* 851 */     URI result = newURI(path);
/* 852 */     if (result == null)
/* 853 */       return NULL_URI;
/* 854 */     if (!result.isAbsolute())
/* 855 */       result = URI.create(new Origin(getScheme(), getHost(), getPort()).asString() + path);
/* 856 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private URI newURI(String uri)
/*     */   {
/*     */     try
/*     */     {
/* 864 */       if ("*".equals(uri))
/* 865 */         return null;
/* 866 */       URI result = new URI(uri);
/* 867 */       return result.isOpaque() ? null : result;
/*     */     }
/*     */     catch (URISyntaxException x) {}
/*     */     
/*     */ 
/*     */ 
/* 873 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 880 */     return String.format("%s[%s %s %s]@%x", new Object[] { HttpRequest.class.getSimpleName(), getMethod(), getPath(), getVersion(), Integer.valueOf(hashCode()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */