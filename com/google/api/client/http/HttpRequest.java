/*      */ package com.google.api.client.http;
/*      */ 
/*      */ import com.google.api.client.util.Beta;
/*      */ import com.google.api.client.util.ObjectParser;
/*      */ import com.google.api.client.util.Preconditions;
/*      */ import com.google.api.client.util.Sleeper;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.FutureTask;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class HttpRequest
/*      */ {
/*      */   public static final String VERSION = "1.20.0";
/*      */   public static final String USER_AGENT_SUFFIX = "Google-HTTP-Java-Client/1.20.0 (gzip)";
/*      */   private HttpExecuteInterceptor executeInterceptor;
/*   74 */   private HttpHeaders headers = new HttpHeaders();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   93 */   private HttpHeaders responseHeaders = new HttpHeaders();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */   private int numRetries = 10;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  123 */   private int contentLoggingLimit = 16384;
/*      */   
/*      */ 
/*  126 */   private boolean loggingEnabled = true;
/*      */   
/*      */ 
/*  129 */   private boolean curlLoggingEnabled = true;
/*      */   
/*      */ 
/*      */   private HttpContent content;
/*      */   
/*      */ 
/*      */   private final HttpTransport transport;
/*      */   
/*      */ 
/*      */   private String requestMethod;
/*      */   
/*      */ 
/*      */   private GenericUrl url;
/*      */   
/*      */ 
/*  144 */   private int connectTimeout = 20000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */   private int readTimeout = 20000;
/*      */   
/*      */ 
/*      */ 
/*      */   private HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler;
/*      */   
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   private HttpIOExceptionHandler ioExceptionHandler;
/*      */   
/*      */ 
/*      */   private HttpResponseInterceptor responseInterceptor;
/*      */   
/*      */ 
/*      */   private ObjectParser objectParser;
/*      */   
/*      */ 
/*      */   private HttpEncoding encoding;
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*      */   private BackOffPolicy backOffPolicy;
/*      */   
/*      */ 
/*  176 */   private boolean followRedirects = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  182 */   private boolean throwExceptionOnExecuteError = true;
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*  188 */   private boolean retryOnExecuteIOException = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean suppressUserAgentSuffix;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  202 */   private Sleeper sleeper = Sleeper.DEFAULT;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   HttpRequest(HttpTransport transport, String requestMethod)
/*      */   {
/*  209 */     this.transport = transport;
/*  210 */     setRequestMethod(requestMethod);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpTransport getTransport()
/*      */   {
/*  219 */     return this.transport;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRequestMethod()
/*      */   {
/*  228 */     return this.requestMethod;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setRequestMethod(String requestMethod)
/*      */   {
/*  237 */     Preconditions.checkArgument((requestMethod == null) || (HttpMediaType.matchesToken(requestMethod)));
/*  238 */     this.requestMethod = requestMethod;
/*  239 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GenericUrl getUrl()
/*      */   {
/*  248 */     return this.url;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setUrl(GenericUrl url)
/*      */   {
/*  257 */     this.url = ((GenericUrl)Preconditions.checkNotNull(url));
/*  258 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpContent getContent()
/*      */   {
/*  267 */     return this.content;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setContent(HttpContent content)
/*      */   {
/*  276 */     this.content = content;
/*  277 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpEncoding getEncoding()
/*      */   {
/*  286 */     return this.encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setEncoding(HttpEncoding encoding)
/*      */   {
/*  295 */     this.encoding = encoding;
/*  296 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*      */   public BackOffPolicy getBackOffPolicy()
/*      */   {
/*  311 */     return this.backOffPolicy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*      */   public HttpRequest setBackOffPolicy(BackOffPolicy backOffPolicy)
/*      */   {
/*  326 */     this.backOffPolicy = backOffPolicy;
/*  327 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getContentLoggingLimit()
/*      */   {
/*  353 */     return this.contentLoggingLimit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setContentLoggingLimit(int contentLoggingLimit)
/*      */   {
/*  379 */     Preconditions.checkArgument(contentLoggingLimit >= 0, "The content logging limit must be non-negative.");
/*      */     
/*  381 */     this.contentLoggingLimit = contentLoggingLimit;
/*  382 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLoggingEnabled()
/*      */   {
/*  395 */     return this.loggingEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setLoggingEnabled(boolean loggingEnabled)
/*      */   {
/*  408 */     this.loggingEnabled = loggingEnabled;
/*  409 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCurlLoggingEnabled()
/*      */   {
/*  418 */     return this.curlLoggingEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setCurlLoggingEnabled(boolean curlLoggingEnabled)
/*      */   {
/*  431 */     this.curlLoggingEnabled = curlLoggingEnabled;
/*  432 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getConnectTimeout()
/*      */   {
/*  442 */     return this.connectTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setConnectTimeout(int connectTimeout)
/*      */   {
/*  456 */     Preconditions.checkArgument(connectTimeout >= 0);
/*  457 */     this.connectTimeout = connectTimeout;
/*  458 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getReadTimeout()
/*      */   {
/*  472 */     return this.readTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setReadTimeout(int readTimeout)
/*      */   {
/*  482 */     Preconditions.checkArgument(readTimeout >= 0);
/*  483 */     this.readTimeout = readTimeout;
/*  484 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders getHeaders()
/*      */   {
/*  493 */     return this.headers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setHeaders(HttpHeaders headers)
/*      */   {
/*  506 */     this.headers = ((HttpHeaders)Preconditions.checkNotNull(headers));
/*  507 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders getResponseHeaders()
/*      */   {
/*  516 */     return this.responseHeaders;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setResponseHeaders(HttpHeaders responseHeaders)
/*      */   {
/*  543 */     this.responseHeaders = ((HttpHeaders)Preconditions.checkNotNull(responseHeaders));
/*  544 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpExecuteInterceptor getInterceptor()
/*      */   {
/*  554 */     return this.executeInterceptor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setInterceptor(HttpExecuteInterceptor interceptor)
/*      */   {
/*  564 */     this.executeInterceptor = interceptor;
/*  565 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpUnsuccessfulResponseHandler getUnsuccessfulResponseHandler()
/*      */   {
/*  574 */     return this.unsuccessfulResponseHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler)
/*      */   {
/*  584 */     this.unsuccessfulResponseHandler = unsuccessfulResponseHandler;
/*  585 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public HttpIOExceptionHandler getIOExceptionHandler()
/*      */   {
/*  596 */     return this.ioExceptionHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public HttpRequest setIOExceptionHandler(HttpIOExceptionHandler ioExceptionHandler)
/*      */   {
/*  607 */     this.ioExceptionHandler = ioExceptionHandler;
/*  608 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpResponseInterceptor getResponseInterceptor()
/*      */   {
/*  617 */     return this.responseInterceptor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setResponseInterceptor(HttpResponseInterceptor responseInterceptor)
/*      */   {
/*  626 */     this.responseInterceptor = responseInterceptor;
/*  627 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNumberOfRetries()
/*      */   {
/*  640 */     return this.numRetries;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setNumberOfRetries(int numRetries)
/*      */   {
/*  656 */     Preconditions.checkArgument(numRetries >= 0);
/*  657 */     this.numRetries = numRetries;
/*  658 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setParser(ObjectParser parser)
/*      */   {
/*  672 */     this.objectParser = parser;
/*  673 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ObjectParser getParser()
/*      */   {
/*  682 */     return this.objectParser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getFollowRedirects()
/*      */   {
/*  691 */     return this.followRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setFollowRedirects(boolean followRedirects)
/*      */   {
/*  704 */     this.followRedirects = followRedirects;
/*  705 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getThrowExceptionOnExecuteError()
/*      */   {
/*  715 */     return this.throwExceptionOnExecuteError;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setThrowExceptionOnExecuteError(boolean throwExceptionOnExecuteError)
/*      */   {
/*  729 */     this.throwExceptionOnExecuteError = throwExceptionOnExecuteError;
/*  730 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*      */   public boolean getRetryOnExecuteIOException()
/*      */   {
/*  745 */     return this.retryOnExecuteIOException;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   @Beta
/*      */   public HttpRequest setRetryOnExecuteIOException(boolean retryOnExecuteIOException)
/*      */   {
/*  764 */     this.retryOnExecuteIOException = retryOnExecuteIOException;
/*  765 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSuppressUserAgentSuffix()
/*      */   {
/*  774 */     return this.suppressUserAgentSuffix;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setSuppressUserAgentSuffix(boolean suppressUserAgentSuffix)
/*      */   {
/*  787 */     this.suppressUserAgentSuffix = suppressUserAgentSuffix;
/*  788 */     return this;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public HttpResponse execute()
/*      */     throws java.io.IOException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore_1
/*      */     //   2: aload_0
/*      */     //   3: getfield 68	com/google/api/client/http/HttpRequest:numRetries	I
/*      */     //   6: iflt +7 -> 13
/*      */     //   9: iconst_1
/*      */     //   10: goto +4 -> 14
/*      */     //   13: iconst_0
/*      */     //   14: invokestatic 117	com/google/api/client/util/Preconditions:checkArgument	(Z)V
/*      */     //   17: aload_0
/*      */     //   18: getfield 68	com/google/api/client/http/HttpRequest:numRetries	I
/*      */     //   21: istore_2
/*      */     //   22: aload_0
/*      */     //   23: getfield 145	com/google/api/client/http/HttpRequest:backOffPolicy	Lcom/google/api/client/http/BackOffPolicy;
/*      */     //   26: ifnull +12 -> 38
/*      */     //   29: aload_0
/*      */     //   30: getfield 145	com/google/api/client/http/HttpRequest:backOffPolicy	Lcom/google/api/client/http/BackOffPolicy;
/*      */     //   33: invokeinterface 227 1 0
/*      */     //   38: aconst_null
/*      */     //   39: astore_3
/*      */     //   40: aload_0
/*      */     //   41: getfield 105	com/google/api/client/http/HttpRequest:requestMethod	Ljava/lang/String;
/*      */     //   44: invokestatic 127	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   47: pop
/*      */     //   48: aload_0
/*      */     //   49: getfield 121	com/google/api/client/http/HttpRequest:url	Lcom/google/api/client/http/GenericUrl;
/*      */     //   52: invokestatic 127	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   55: pop
/*      */     //   56: aload_3
/*      */     //   57: ifnull +7 -> 64
/*      */     //   60: aload_3
/*      */     //   61: invokevirtual 232	com/google/api/client/http/HttpResponse:ignore	()V
/*      */     //   64: aconst_null
/*      */     //   65: astore_3
/*      */     //   66: aconst_null
/*      */     //   67: astore 4
/*      */     //   69: aload_0
/*      */     //   70: getfield 176	com/google/api/client/http/HttpRequest:executeInterceptor	Lcom/google/api/client/http/HttpExecuteInterceptor;
/*      */     //   73: ifnull +13 -> 86
/*      */     //   76: aload_0
/*      */     //   77: getfield 176	com/google/api/client/http/HttpRequest:executeInterceptor	Lcom/google/api/client/http/HttpExecuteInterceptor;
/*      */     //   80: aload_0
/*      */     //   81: invokeinterface 238 2 0
/*      */     //   86: aload_0
/*      */     //   87: getfield 121	com/google/api/client/http/HttpRequest:url	Lcom/google/api/client/http/GenericUrl;
/*      */     //   90: invokevirtual 241	com/google/api/client/http/GenericUrl:build	()Ljava/lang/String;
/*      */     //   93: astore 5
/*      */     //   95: aload_0
/*      */     //   96: getfield 93	com/google/api/client/http/HttpRequest:transport	Lcom/google/api/client/http/HttpTransport;
/*      */     //   99: aload_0
/*      */     //   100: getfield 105	com/google/api/client/http/HttpRequest:requestMethod	Ljava/lang/String;
/*      */     //   103: aload 5
/*      */     //   105: invokevirtual 247	com/google/api/client/http/HttpTransport:buildRequest	(Ljava/lang/String;Ljava/lang/String;)Lcom/google/api/client/http/LowLevelHttpRequest;
/*      */     //   108: astore 6
/*      */     //   110: getstatic 251	com/google/api/client/http/HttpTransport:LOGGER	Ljava/util/logging/Logger;
/*      */     //   113: astore 7
/*      */     //   115: aload_0
/*      */     //   116: getfield 72	com/google/api/client/http/HttpRequest:loggingEnabled	Z
/*      */     //   119: ifeq +18 -> 137
/*      */     //   122: aload 7
/*      */     //   124: getstatic 257	java/util/logging/Level:CONFIG	Ljava/util/logging/Level;
/*      */     //   127: invokevirtual 263	java/util/logging/Logger:isLoggable	(Ljava/util/logging/Level;)Z
/*      */     //   130: ifeq +7 -> 137
/*      */     //   133: iconst_1
/*      */     //   134: goto +4 -> 138
/*      */     //   137: iconst_0
/*      */     //   138: istore 8
/*      */     //   140: aconst_null
/*      */     //   141: astore 9
/*      */     //   143: aconst_null
/*      */     //   144: astore 10
/*      */     //   146: iload 8
/*      */     //   148: ifeq +101 -> 249
/*      */     //   151: new 265	java/lang/StringBuilder
/*      */     //   154: dup
/*      */     //   155: invokespecial 266	java/lang/StringBuilder:<init>	()V
/*      */     //   158: astore 9
/*      */     //   160: aload 9
/*      */     //   162: ldc_w 268
/*      */     //   165: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   168: getstatic 277	com/google/api/client/util/StringUtils:LINE_SEPARATOR	Ljava/lang/String;
/*      */     //   171: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   174: pop
/*      */     //   175: aload 9
/*      */     //   177: aload_0
/*      */     //   178: getfield 105	com/google/api/client/http/HttpRequest:requestMethod	Ljava/lang/String;
/*      */     //   181: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   184: bipush 32
/*      */     //   186: invokevirtual 280	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*      */     //   189: aload 5
/*      */     //   191: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   194: getstatic 277	com/google/api/client/util/StringUtils:LINE_SEPARATOR	Ljava/lang/String;
/*      */     //   197: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   200: pop
/*      */     //   201: aload_0
/*      */     //   202: getfield 74	com/google/api/client/http/HttpRequest:curlLoggingEnabled	Z
/*      */     //   205: ifeq +44 -> 249
/*      */     //   208: new 265	java/lang/StringBuilder
/*      */     //   211: dup
/*      */     //   212: ldc_w 282
/*      */     //   215: invokespecial 285	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   218: astore 10
/*      */     //   220: aload_0
/*      */     //   221: getfield 105	com/google/api/client/http/HttpRequest:requestMethod	Ljava/lang/String;
/*      */     //   224: ldc_w 287
/*      */     //   227: invokevirtual 293	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   230: ifne +19 -> 249
/*      */     //   233: aload 10
/*      */     //   235: ldc_w 295
/*      */     //   238: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   241: aload_0
/*      */     //   242: getfield 105	com/google/api/client/http/HttpRequest:requestMethod	Ljava/lang/String;
/*      */     //   245: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   248: pop
/*      */     //   249: aload_0
/*      */     //   250: getfield 64	com/google/api/client/http/HttpRequest:headers	Lcom/google/api/client/http/HttpHeaders;
/*      */     //   253: invokevirtual 298	com/google/api/client/http/HttpHeaders:getUserAgent	()Ljava/lang/String;
/*      */     //   256: astore 11
/*      */     //   258: aload_0
/*      */     //   259: getfield 215	com/google/api/client/http/HttpRequest:suppressUserAgentSuffix	Z
/*      */     //   262: ifne +88 -> 350
/*      */     //   265: aload 11
/*      */     //   267: ifnonnull +16 -> 283
/*      */     //   270: aload_0
/*      */     //   271: getfield 64	com/google/api/client/http/HttpRequest:headers	Lcom/google/api/client/http/HttpHeaders;
/*      */     //   274: ldc 14
/*      */     //   276: invokevirtual 302	com/google/api/client/http/HttpHeaders:setUserAgent	(Ljava/lang/String;)Lcom/google/api/client/http/HttpHeaders;
/*      */     //   279: pop
/*      */     //   280: goto +70 -> 350
/*      */     //   283: aload_0
/*      */     //   284: getfield 64	com/google/api/client/http/HttpRequest:headers	Lcom/google/api/client/http/HttpHeaders;
/*      */     //   287: aload 11
/*      */     //   289: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   292: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   295: astore 12
/*      */     //   297: ldc 14
/*      */     //   299: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   302: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   305: astore 13
/*      */     //   307: new 265	java/lang/StringBuilder
/*      */     //   310: dup
/*      */     //   311: iconst_1
/*      */     //   312: aload 12
/*      */     //   314: invokevirtual 309	java/lang/String:length	()I
/*      */     //   317: iadd
/*      */     //   318: aload 13
/*      */     //   320: invokevirtual 309	java/lang/String:length	()I
/*      */     //   323: iadd
/*      */     //   324: invokespecial 312	java/lang/StringBuilder:<init>	(I)V
/*      */     //   327: aload 12
/*      */     //   329: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   332: ldc_w 314
/*      */     //   335: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   338: aload 13
/*      */     //   340: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   343: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   346: invokevirtual 302	com/google/api/client/http/HttpHeaders:setUserAgent	(Ljava/lang/String;)Lcom/google/api/client/http/HttpHeaders;
/*      */     //   349: pop
/*      */     //   350: aload_0
/*      */     //   351: getfield 64	com/google/api/client/http/HttpRequest:headers	Lcom/google/api/client/http/HttpHeaders;
/*      */     //   354: aload 9
/*      */     //   356: aload 10
/*      */     //   358: aload 7
/*      */     //   360: aload 6
/*      */     //   362: invokestatic 321	com/google/api/client/http/HttpHeaders:serializeHeaders	(Lcom/google/api/client/http/HttpHeaders;Ljava/lang/StringBuilder;Ljava/lang/StringBuilder;Ljava/util/logging/Logger;Lcom/google/api/client/http/LowLevelHttpRequest;)V
/*      */     //   365: aload_0
/*      */     //   366: getfield 215	com/google/api/client/http/HttpRequest:suppressUserAgentSuffix	Z
/*      */     //   369: ifne +13 -> 382
/*      */     //   372: aload_0
/*      */     //   373: getfield 64	com/google/api/client/http/HttpRequest:headers	Lcom/google/api/client/http/HttpHeaders;
/*      */     //   376: aload 11
/*      */     //   378: invokevirtual 302	com/google/api/client/http/HttpHeaders:setUserAgent	(Ljava/lang/String;)Lcom/google/api/client/http/HttpHeaders;
/*      */     //   381: pop
/*      */     //   382: aload_0
/*      */     //   383: getfield 133	com/google/api/client/http/HttpRequest:content	Lcom/google/api/client/http/HttpContent;
/*      */     //   386: astore 12
/*      */     //   388: aload 12
/*      */     //   390: ifnull +15 -> 405
/*      */     //   393: aload_0
/*      */     //   394: getfield 133	com/google/api/client/http/HttpRequest:content	Lcom/google/api/client/http/HttpContent;
/*      */     //   397: invokeinterface 326 1 0
/*      */     //   402: ifeq +7 -> 409
/*      */     //   405: iconst_1
/*      */     //   406: goto +4 -> 410
/*      */     //   409: iconst_0
/*      */     //   410: istore 13
/*      */     //   412: aload 12
/*      */     //   414: ifnull +419 -> 833
/*      */     //   417: aload_0
/*      */     //   418: getfield 133	com/google/api/client/http/HttpRequest:content	Lcom/google/api/client/http/HttpContent;
/*      */     //   421: invokeinterface 329 1 0
/*      */     //   426: astore 14
/*      */     //   428: iload 8
/*      */     //   430: ifeq +24 -> 454
/*      */     //   433: new 331	com/google/api/client/util/LoggingStreamingContent
/*      */     //   436: dup
/*      */     //   437: aload 12
/*      */     //   439: getstatic 251	com/google/api/client/http/HttpTransport:LOGGER	Ljava/util/logging/Logger;
/*      */     //   442: getstatic 257	java/util/logging/Level:CONFIG	Ljava/util/logging/Level;
/*      */     //   445: aload_0
/*      */     //   446: getfield 70	com/google/api/client/http/HttpRequest:contentLoggingLimit	I
/*      */     //   449: invokespecial 334	com/google/api/client/util/LoggingStreamingContent:<init>	(Lcom/google/api/client/util/StreamingContent;Ljava/util/logging/Logger;Ljava/util/logging/Level;I)V
/*      */     //   452: astore 12
/*      */     //   454: aload_0
/*      */     //   455: getfield 139	com/google/api/client/http/HttpRequest:encoding	Lcom/google/api/client/http/HttpEncoding;
/*      */     //   458: ifnonnull +20 -> 478
/*      */     //   461: aconst_null
/*      */     //   462: astore 15
/*      */     //   464: aload_0
/*      */     //   465: getfield 133	com/google/api/client/http/HttpRequest:content	Lcom/google/api/client/http/HttpContent;
/*      */     //   468: invokeinterface 338 1 0
/*      */     //   473: lstore 16
/*      */     //   475: goto +47 -> 522
/*      */     //   478: aload_0
/*      */     //   479: getfield 139	com/google/api/client/http/HttpRequest:encoding	Lcom/google/api/client/http/HttpEncoding;
/*      */     //   482: invokeinterface 343 1 0
/*      */     //   487: astore 15
/*      */     //   489: new 345	com/google/api/client/http/HttpEncodingStreamingContent
/*      */     //   492: dup
/*      */     //   493: aload 12
/*      */     //   495: aload_0
/*      */     //   496: getfield 139	com/google/api/client/http/HttpRequest:encoding	Lcom/google/api/client/http/HttpEncoding;
/*      */     //   499: invokespecial 348	com/google/api/client/http/HttpEncodingStreamingContent:<init>	(Lcom/google/api/client/util/StreamingContent;Lcom/google/api/client/http/HttpEncoding;)V
/*      */     //   502: astore 12
/*      */     //   504: iload 13
/*      */     //   506: ifeq +11 -> 517
/*      */     //   509: aload 12
/*      */     //   511: invokestatic 354	com/google/api/client/util/IOUtils:computeLength	(Lcom/google/api/client/util/StreamingContent;)J
/*      */     //   514: goto +6 -> 520
/*      */     //   517: ldc2_w 355
/*      */     //   520: lstore 16
/*      */     //   522: iload 8
/*      */     //   524: ifeq +267 -> 791
/*      */     //   527: aload 14
/*      */     //   529: ifnull +105 -> 634
/*      */     //   532: ldc_w 358
/*      */     //   535: aload 14
/*      */     //   537: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   540: dup
/*      */     //   541: invokevirtual 309	java/lang/String:length	()I
/*      */     //   544: ifeq +9 -> 553
/*      */     //   547: invokevirtual 362	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   550: goto +12 -> 562
/*      */     //   553: pop
/*      */     //   554: new 289	java/lang/String
/*      */     //   557: dup_x1
/*      */     //   558: swap
/*      */     //   559: invokespecial 363	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */     //   562: astore 18
/*      */     //   564: aload 9
/*      */     //   566: aload 18
/*      */     //   568: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   571: getstatic 277	com/google/api/client/util/StringUtils:LINE_SEPARATOR	Ljava/lang/String;
/*      */     //   574: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   577: pop
/*      */     //   578: aload 10
/*      */     //   580: ifnull +54 -> 634
/*      */     //   583: aload 10
/*      */     //   585: aload 18
/*      */     //   587: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   590: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   593: astore 19
/*      */     //   595: new 265	java/lang/StringBuilder
/*      */     //   598: dup
/*      */     //   599: bipush 6
/*      */     //   601: aload 19
/*      */     //   603: invokevirtual 309	java/lang/String:length	()I
/*      */     //   606: iadd
/*      */     //   607: invokespecial 312	java/lang/StringBuilder:<init>	(I)V
/*      */     //   610: ldc_w 365
/*      */     //   613: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   616: aload 19
/*      */     //   618: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   621: ldc_w 367
/*      */     //   624: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   627: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   630: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   633: pop
/*      */     //   634: aload 15
/*      */     //   636: ifnull +105 -> 741
/*      */     //   639: ldc_w 369
/*      */     //   642: aload 15
/*      */     //   644: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   647: dup
/*      */     //   648: invokevirtual 309	java/lang/String:length	()I
/*      */     //   651: ifeq +9 -> 660
/*      */     //   654: invokevirtual 362	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   657: goto +12 -> 669
/*      */     //   660: pop
/*      */     //   661: new 289	java/lang/String
/*      */     //   664: dup_x1
/*      */     //   665: swap
/*      */     //   666: invokespecial 363	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */     //   669: astore 18
/*      */     //   671: aload 9
/*      */     //   673: aload 18
/*      */     //   675: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   678: getstatic 277	com/google/api/client/util/StringUtils:LINE_SEPARATOR	Ljava/lang/String;
/*      */     //   681: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   684: pop
/*      */     //   685: aload 10
/*      */     //   687: ifnull +54 -> 741
/*      */     //   690: aload 10
/*      */     //   692: aload 18
/*      */     //   694: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   697: invokestatic 306	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   700: astore 19
/*      */     //   702: new 265	java/lang/StringBuilder
/*      */     //   705: dup
/*      */     //   706: bipush 6
/*      */     //   708: aload 19
/*      */     //   710: invokevirtual 309	java/lang/String:length	()I
/*      */     //   713: iadd
/*      */     //   714: invokespecial 312	java/lang/StringBuilder:<init>	(I)V
/*      */     //   717: ldc_w 365
/*      */     //   720: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   723: aload 19
/*      */     //   725: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   728: ldc_w 367
/*      */     //   731: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   734: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   737: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   740: pop
/*      */     //   741: lload 16
/*      */     //   743: lconst_0
/*      */     //   744: lcmp
/*      */     //   745: iflt +46 -> 791
/*      */     //   748: lload 16
/*      */     //   750: lstore 20
/*      */     //   752: new 265	java/lang/StringBuilder
/*      */     //   755: dup
/*      */     //   756: bipush 36
/*      */     //   758: invokespecial 312	java/lang/StringBuilder:<init>	(I)V
/*      */     //   761: ldc_w 371
/*      */     //   764: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   767: lload 20
/*      */     //   769: invokevirtual 374	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
/*      */     //   772: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   775: astore 18
/*      */     //   777: aload 9
/*      */     //   779: aload 18
/*      */     //   781: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   784: getstatic 277	com/google/api/client/util/StringUtils:LINE_SEPARATOR	Ljava/lang/String;
/*      */     //   787: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   790: pop
/*      */     //   791: aload 10
/*      */     //   793: ifnull +12 -> 805
/*      */     //   796: aload 10
/*      */     //   798: ldc_w 376
/*      */     //   801: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   804: pop
/*      */     //   805: aload 6
/*      */     //   807: aload 14
/*      */     //   809: invokevirtual 381	com/google/api/client/http/LowLevelHttpRequest:setContentType	(Ljava/lang/String;)V
/*      */     //   812: aload 6
/*      */     //   814: aload 15
/*      */     //   816: invokevirtual 384	com/google/api/client/http/LowLevelHttpRequest:setContentEncoding	(Ljava/lang/String;)V
/*      */     //   819: aload 6
/*      */     //   821: lload 16
/*      */     //   823: invokevirtual 388	com/google/api/client/http/LowLevelHttpRequest:setContentLength	(J)V
/*      */     //   826: aload 6
/*      */     //   828: aload 12
/*      */     //   830: invokevirtual 392	com/google/api/client/http/LowLevelHttpRequest:setStreamingContent	(Lcom/google/api/client/util/StreamingContent;)V
/*      */     //   833: iload 8
/*      */     //   835: ifeq +77 -> 912
/*      */     //   838: aload 7
/*      */     //   840: aload 9
/*      */     //   842: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   845: invokevirtual 395	java/util/logging/Logger:config	(Ljava/lang/String;)V
/*      */     //   848: aload 10
/*      */     //   850: ifnull +62 -> 912
/*      */     //   853: aload 10
/*      */     //   855: ldc_w 397
/*      */     //   858: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   861: pop
/*      */     //   862: aload 10
/*      */     //   864: aload 5
/*      */     //   866: ldc_w 367
/*      */     //   869: ldc_w 399
/*      */     //   872: invokevirtual 403	java/lang/String:replaceAll	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*      */     //   875: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   878: pop
/*      */     //   879: aload 10
/*      */     //   881: ldc_w 367
/*      */     //   884: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   887: pop
/*      */     //   888: aload 12
/*      */     //   890: ifnull +12 -> 902
/*      */     //   893: aload 10
/*      */     //   895: ldc_w 405
/*      */     //   898: invokevirtual 272	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   901: pop
/*      */     //   902: aload 7
/*      */     //   904: aload 10
/*      */     //   906: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   909: invokevirtual 395	java/util/logging/Logger:config	(Ljava/lang/String;)V
/*      */     //   912: iload 13
/*      */     //   914: ifeq +11 -> 925
/*      */     //   917: iload_2
/*      */     //   918: ifle +7 -> 925
/*      */     //   921: iconst_1
/*      */     //   922: goto +4 -> 926
/*      */     //   925: iconst_0
/*      */     //   926: istore_1
/*      */     //   927: aload 6
/*      */     //   929: aload_0
/*      */     //   930: getfield 76	com/google/api/client/http/HttpRequest:connectTimeout	I
/*      */     //   933: aload_0
/*      */     //   934: getfield 78	com/google/api/client/http/HttpRequest:readTimeout	I
/*      */     //   937: invokevirtual 409	com/google/api/client/http/LowLevelHttpRequest:setTimeout	(II)V
/*      */     //   940: aload 6
/*      */     //   942: invokevirtual 412	com/google/api/client/http/LowLevelHttpRequest:execute	()Lcom/google/api/client/http/LowLevelHttpResponse;
/*      */     //   945: astore 15
/*      */     //   947: iconst_0
/*      */     //   948: istore 22
/*      */     //   950: new 229	com/google/api/client/http/HttpResponse
/*      */     //   953: dup
/*      */     //   954: aload_0
/*      */     //   955: aload 15
/*      */     //   957: invokespecial 415	com/google/api/client/http/HttpResponse:<init>	(Lcom/google/api/client/http/HttpRequest;Lcom/google/api/client/http/LowLevelHttpResponse;)V
/*      */     //   960: astore_3
/*      */     //   961: iconst_1
/*      */     //   962: istore 22
/*      */     //   964: iload 22
/*      */     //   966: ifne +50 -> 1016
/*      */     //   969: aload 15
/*      */     //   971: invokevirtual 420	com/google/api/client/http/LowLevelHttpResponse:getContent	()Ljava/io/InputStream;
/*      */     //   974: astore 23
/*      */     //   976: aload 23
/*      */     //   978: ifnull +8 -> 986
/*      */     //   981: aload 23
/*      */     //   983: invokevirtual 425	java/io/InputStream:close	()V
/*      */     //   986: goto +30 -> 1016
/*      */     //   989: astore 24
/*      */     //   991: iload 22
/*      */     //   993: ifne +20 -> 1013
/*      */     //   996: aload 15
/*      */     //   998: invokevirtual 420	com/google/api/client/http/LowLevelHttpResponse:getContent	()Ljava/io/InputStream;
/*      */     //   1001: astore 25
/*      */     //   1003: aload 25
/*      */     //   1005: ifnull +8 -> 1013
/*      */     //   1008: aload 25
/*      */     //   1010: invokevirtual 425	java/io/InputStream:close	()V
/*      */     //   1013: aload 24
/*      */     //   1015: athrow
/*      */     //   1016: goto +53 -> 1069
/*      */     //   1019: astore 15
/*      */     //   1021: aload_0
/*      */     //   1022: getfield 84	com/google/api/client/http/HttpRequest:retryOnExecuteIOException	Z
/*      */     //   1025: ifne +27 -> 1052
/*      */     //   1028: aload_0
/*      */     //   1029: getfield 189	com/google/api/client/http/HttpRequest:ioExceptionHandler	Lcom/google/api/client/http/HttpIOExceptionHandler;
/*      */     //   1032: ifnull +17 -> 1049
/*      */     //   1035: aload_0
/*      */     //   1036: getfield 189	com/google/api/client/http/HttpRequest:ioExceptionHandler	Lcom/google/api/client/http/HttpIOExceptionHandler;
/*      */     //   1039: aload_0
/*      */     //   1040: iload_1
/*      */     //   1041: invokeinterface 431 3 0
/*      */     //   1046: ifne +6 -> 1052
/*      */     //   1049: aload 15
/*      */     //   1051: athrow
/*      */     //   1052: aload 15
/*      */     //   1054: astore 4
/*      */     //   1056: aload 7
/*      */     //   1058: getstatic 434	java/util/logging/Level:WARNING	Ljava/util/logging/Level;
/*      */     //   1061: ldc_w 436
/*      */     //   1064: aload 15
/*      */     //   1066: invokevirtual 440	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
/*      */     //   1069: iconst_0
/*      */     //   1070: istore 15
/*      */     //   1072: aload_3
/*      */     //   1073: ifnull +142 -> 1215
/*      */     //   1076: aload_3
/*      */     //   1077: invokevirtual 443	com/google/api/client/http/HttpResponse:isSuccessStatusCode	()Z
/*      */     //   1080: ifne +135 -> 1215
/*      */     //   1083: iconst_0
/*      */     //   1084: istore 22
/*      */     //   1086: aload_0
/*      */     //   1087: getfield 183	com/google/api/client/http/HttpRequest:unsuccessfulResponseHandler	Lcom/google/api/client/http/HttpUnsuccessfulResponseHandler;
/*      */     //   1090: ifnull +17 -> 1107
/*      */     //   1093: aload_0
/*      */     //   1094: getfield 183	com/google/api/client/http/HttpRequest:unsuccessfulResponseHandler	Lcom/google/api/client/http/HttpUnsuccessfulResponseHandler;
/*      */     //   1097: aload_0
/*      */     //   1098: aload_3
/*      */     //   1099: iload_1
/*      */     //   1100: invokeinterface 449 4 0
/*      */     //   1105: istore 22
/*      */     //   1107: iload 22
/*      */     //   1109: ifne +90 -> 1199
/*      */     //   1112: aload_0
/*      */     //   1113: aload_3
/*      */     //   1114: invokevirtual 452	com/google/api/client/http/HttpResponse:getStatusCode	()I
/*      */     //   1117: aload_3
/*      */     //   1118: invokevirtual 454	com/google/api/client/http/HttpResponse:getHeaders	()Lcom/google/api/client/http/HttpHeaders;
/*      */     //   1121: invokevirtual 458	com/google/api/client/http/HttpRequest:handleRedirect	(ILcom/google/api/client/http/HttpHeaders;)Z
/*      */     //   1124: ifeq +9 -> 1133
/*      */     //   1127: iconst_1
/*      */     //   1128: istore 22
/*      */     //   1130: goto +69 -> 1199
/*      */     //   1133: iload_1
/*      */     //   1134: ifeq +65 -> 1199
/*      */     //   1137: aload_0
/*      */     //   1138: getfield 145	com/google/api/client/http/HttpRequest:backOffPolicy	Lcom/google/api/client/http/BackOffPolicy;
/*      */     //   1141: ifnull +58 -> 1199
/*      */     //   1144: aload_0
/*      */     //   1145: getfield 145	com/google/api/client/http/HttpRequest:backOffPolicy	Lcom/google/api/client/http/BackOffPolicy;
/*      */     //   1148: aload_3
/*      */     //   1149: invokevirtual 452	com/google/api/client/http/HttpResponse:getStatusCode	()I
/*      */     //   1152: invokeinterface 462 2 0
/*      */     //   1157: ifeq +42 -> 1199
/*      */     //   1160: aload_0
/*      */     //   1161: getfield 145	com/google/api/client/http/HttpRequest:backOffPolicy	Lcom/google/api/client/http/BackOffPolicy;
/*      */     //   1164: invokeinterface 465 1 0
/*      */     //   1169: lstore 26
/*      */     //   1171: lload 26
/*      */     //   1173: ldc2_w 355
/*      */     //   1176: lcmp
/*      */     //   1177: ifeq +22 -> 1199
/*      */     //   1180: aload_0
/*      */     //   1181: getfield 91	com/google/api/client/http/HttpRequest:sleeper	Lcom/google/api/client/util/Sleeper;
/*      */     //   1184: lload 26
/*      */     //   1186: invokeinterface 468 3 0
/*      */     //   1191: goto +5 -> 1196
/*      */     //   1194: astore 18
/*      */     //   1196: iconst_1
/*      */     //   1197: istore 22
/*      */     //   1199: iload_1
/*      */     //   1200: iload 22
/*      */     //   1202: iand
/*      */     //   1203: istore_1
/*      */     //   1204: iload_1
/*      */     //   1205: ifeq +7 -> 1212
/*      */     //   1208: aload_3
/*      */     //   1209: invokevirtual 232	com/google/api/client/http/HttpResponse:ignore	()V
/*      */     //   1212: goto +15 -> 1227
/*      */     //   1215: iload_1
/*      */     //   1216: aload_3
/*      */     //   1217: ifnonnull +7 -> 1224
/*      */     //   1220: iconst_1
/*      */     //   1221: goto +4 -> 1225
/*      */     //   1224: iconst_0
/*      */     //   1225: iand
/*      */     //   1226: istore_1
/*      */     //   1227: iinc 2 -1
/*      */     //   1230: iconst_1
/*      */     //   1231: istore 15
/*      */     //   1233: aload_3
/*      */     //   1234: ifnull +33 -> 1267
/*      */     //   1237: iload 15
/*      */     //   1239: ifne +28 -> 1267
/*      */     //   1242: aload_3
/*      */     //   1243: invokevirtual 471	com/google/api/client/http/HttpResponse:disconnect	()V
/*      */     //   1246: goto +21 -> 1267
/*      */     //   1249: astore 28
/*      */     //   1251: aload_3
/*      */     //   1252: ifnull +12 -> 1264
/*      */     //   1255: iload 15
/*      */     //   1257: ifne +7 -> 1264
/*      */     //   1260: aload_3
/*      */     //   1261: invokevirtual 471	com/google/api/client/http/HttpResponse:disconnect	()V
/*      */     //   1264: aload 28
/*      */     //   1266: athrow
/*      */     //   1267: iload_1
/*      */     //   1268: ifne -1212 -> 56
/*      */     //   1271: aload_3
/*      */     //   1272: ifnonnull +6 -> 1278
/*      */     //   1275: aload 4
/*      */     //   1277: athrow
/*      */     //   1278: aload_0
/*      */     //   1279: getfield 195	com/google/api/client/http/HttpRequest:responseInterceptor	Lcom/google/api/client/http/HttpResponseInterceptor;
/*      */     //   1282: ifnull +13 -> 1295
/*      */     //   1285: aload_0
/*      */     //   1286: getfield 195	com/google/api/client/http/HttpRequest:responseInterceptor	Lcom/google/api/client/http/HttpResponseInterceptor;
/*      */     //   1289: aload_3
/*      */     //   1290: invokeinterface 477 2 0
/*      */     //   1295: aload_0
/*      */     //   1296: getfield 82	com/google/api/client/http/HttpRequest:throwExceptionOnExecuteError	Z
/*      */     //   1299: ifeq +28 -> 1327
/*      */     //   1302: aload_3
/*      */     //   1303: invokevirtual 443	com/google/api/client/http/HttpResponse:isSuccessStatusCode	()Z
/*      */     //   1306: ifne +21 -> 1327
/*      */     //   1309: new 479	com/google/api/client/http/HttpResponseException
/*      */     //   1312: dup
/*      */     //   1313: aload_3
/*      */     //   1314: invokespecial 481	com/google/api/client/http/HttpResponseException:<init>	(Lcom/google/api/client/http/HttpResponse;)V
/*      */     //   1317: athrow
/*      */     //   1318: astore 29
/*      */     //   1320: aload_3
/*      */     //   1321: invokevirtual 471	com/google/api/client/http/HttpResponse:disconnect	()V
/*      */     //   1324: aload 29
/*      */     //   1326: athrow
/*      */     //   1327: aload_3
/*      */     //   1328: areturn
/*      */     // Line number table:
/*      */     //   Java source line #835	-> byte code offset #0
/*      */     //   Java source line #836	-> byte code offset #2
/*      */     //   Java source line #837	-> byte code offset #17
/*      */     //   Java source line #838	-> byte code offset #22
/*      */     //   Java source line #840	-> byte code offset #29
/*      */     //   Java source line #842	-> byte code offset #38
/*      */     //   Java source line #845	-> byte code offset #40
/*      */     //   Java source line #846	-> byte code offset #48
/*      */     //   Java source line #850	-> byte code offset #56
/*      */     //   Java source line #851	-> byte code offset #60
/*      */     //   Java source line #854	-> byte code offset #64
/*      */     //   Java source line #855	-> byte code offset #66
/*      */     //   Java source line #858	-> byte code offset #69
/*      */     //   Java source line #859	-> byte code offset #76
/*      */     //   Java source line #862	-> byte code offset #86
/*      */     //   Java source line #863	-> byte code offset #95
/*      */     //   Java source line #864	-> byte code offset #110
/*      */     //   Java source line #865	-> byte code offset #115
/*      */     //   Java source line #866	-> byte code offset #140
/*      */     //   Java source line #867	-> byte code offset #143
/*      */     //   Java source line #869	-> byte code offset #146
/*      */     //   Java source line #870	-> byte code offset #151
/*      */     //   Java source line #871	-> byte code offset #160
/*      */     //   Java source line #872	-> byte code offset #175
/*      */     //   Java source line #876	-> byte code offset #201
/*      */     //   Java source line #877	-> byte code offset #208
/*      */     //   Java source line #878	-> byte code offset #220
/*      */     //   Java source line #879	-> byte code offset #233
/*      */     //   Java source line #884	-> byte code offset #249
/*      */     //   Java source line #885	-> byte code offset #258
/*      */     //   Java source line #886	-> byte code offset #265
/*      */     //   Java source line #887	-> byte code offset #270
/*      */     //   Java source line #889	-> byte code offset #283
/*      */     //   Java source line #893	-> byte code offset #350
/*      */     //   Java source line #894	-> byte code offset #365
/*      */     //   Java source line #896	-> byte code offset #372
/*      */     //   Java source line #900	-> byte code offset #382
/*      */     //   Java source line #901	-> byte code offset #388
/*      */     //   Java source line #902	-> byte code offset #412
/*      */     //   Java source line #905	-> byte code offset #417
/*      */     //   Java source line #907	-> byte code offset #428
/*      */     //   Java source line #908	-> byte code offset #433
/*      */     //   Java source line #912	-> byte code offset #454
/*      */     //   Java source line #913	-> byte code offset #461
/*      */     //   Java source line #914	-> byte code offset #464
/*      */     //   Java source line #916	-> byte code offset #478
/*      */     //   Java source line #917	-> byte code offset #489
/*      */     //   Java source line #918	-> byte code offset #504
/*      */     //   Java source line #921	-> byte code offset #522
/*      */     //   Java source line #922	-> byte code offset #527
/*      */     //   Java source line #923	-> byte code offset #532
/*      */     //   Java source line #924	-> byte code offset #564
/*      */     //   Java source line #925	-> byte code offset #578
/*      */     //   Java source line #926	-> byte code offset #583
/*      */     //   Java source line #929	-> byte code offset #634
/*      */     //   Java source line #930	-> byte code offset #639
/*      */     //   Java source line #931	-> byte code offset #671
/*      */     //   Java source line #932	-> byte code offset #685
/*      */     //   Java source line #933	-> byte code offset #690
/*      */     //   Java source line #936	-> byte code offset #741
/*      */     //   Java source line #937	-> byte code offset #748
/*      */     //   Java source line #938	-> byte code offset #777
/*      */     //   Java source line #942	-> byte code offset #791
/*      */     //   Java source line #943	-> byte code offset #796
/*      */     //   Java source line #946	-> byte code offset #805
/*      */     //   Java source line #947	-> byte code offset #812
/*      */     //   Java source line #948	-> byte code offset #819
/*      */     //   Java source line #949	-> byte code offset #826
/*      */     //   Java source line #952	-> byte code offset #833
/*      */     //   Java source line #953	-> byte code offset #838
/*      */     //   Java source line #954	-> byte code offset #848
/*      */     //   Java source line #955	-> byte code offset #853
/*      */     //   Java source line #956	-> byte code offset #862
/*      */     //   Java source line #957	-> byte code offset #879
/*      */     //   Java source line #958	-> byte code offset #888
/*      */     //   Java source line #959	-> byte code offset #893
/*      */     //   Java source line #961	-> byte code offset #902
/*      */     //   Java source line #967	-> byte code offset #912
/*      */     //   Java source line #970	-> byte code offset #927
/*      */     //   Java source line #972	-> byte code offset #940
/*      */     //   Java source line #974	-> byte code offset #947
/*      */     //   Java source line #976	-> byte code offset #950
/*      */     //   Java source line #977	-> byte code offset #961
/*      */     //   Java source line #979	-> byte code offset #964
/*      */     //   Java source line #980	-> byte code offset #969
/*      */     //   Java source line #981	-> byte code offset #976
/*      */     //   Java source line #982	-> byte code offset #981
/*      */     //   Java source line #984	-> byte code offset #986
/*      */     //   Java source line #979	-> byte code offset #989
/*      */     //   Java source line #980	-> byte code offset #996
/*      */     //   Java source line #981	-> byte code offset #1003
/*      */     //   Java source line #982	-> byte code offset #1008
/*      */     //   Java source line #984	-> byte code offset #1013
/*      */     //   Java source line #994	-> byte code offset #1016
/*      */     //   Java source line #986	-> byte code offset #1019
/*      */     //   Java source line #987	-> byte code offset #1021
/*      */     //   Java source line #989	-> byte code offset #1049
/*      */     //   Java source line #992	-> byte code offset #1052
/*      */     //   Java source line #993	-> byte code offset #1056
/*      */     //   Java source line #998	-> byte code offset #1069
/*      */     //   Java source line #1000	-> byte code offset #1072
/*      */     //   Java source line #1001	-> byte code offset #1083
/*      */     //   Java source line #1002	-> byte code offset #1086
/*      */     //   Java source line #1006	-> byte code offset #1093
/*      */     //   Java source line #1008	-> byte code offset #1107
/*      */     //   Java source line #1009	-> byte code offset #1112
/*      */     //   Java source line #1011	-> byte code offset #1127
/*      */     //   Java source line #1012	-> byte code offset #1133
/*      */     //   Java source line #1016	-> byte code offset #1160
/*      */     //   Java source line #1017	-> byte code offset #1171
/*      */     //   Java source line #1019	-> byte code offset #1180
/*      */     //   Java source line #1022	-> byte code offset #1191
/*      */     //   Java source line #1020	-> byte code offset #1194
/*      */     //   Java source line #1023	-> byte code offset #1196
/*      */     //   Java source line #1029	-> byte code offset #1199
/*      */     //   Java source line #1031	-> byte code offset #1204
/*      */     //   Java source line #1032	-> byte code offset #1208
/*      */     //   Java source line #1034	-> byte code offset #1212
/*      */     //   Java source line #1036	-> byte code offset #1215
/*      */     //   Java source line #1040	-> byte code offset #1227
/*      */     //   Java source line #1042	-> byte code offset #1230
/*      */     //   Java source line #1044	-> byte code offset #1233
/*      */     //   Java source line #1045	-> byte code offset #1242
/*      */     //   Java source line #1044	-> byte code offset #1249
/*      */     //   Java source line #1045	-> byte code offset #1260
/*      */     //   Java source line #1048	-> byte code offset #1267
/*      */     //   Java source line #1050	-> byte code offset #1271
/*      */     //   Java source line #1052	-> byte code offset #1275
/*      */     //   Java source line #1055	-> byte code offset #1278
/*      */     //   Java source line #1056	-> byte code offset #1285
/*      */     //   Java source line #1059	-> byte code offset #1295
/*      */     //   Java source line #1061	-> byte code offset #1309
/*      */     //   Java source line #1063	-> byte code offset #1318
/*      */     //   Java source line #1066	-> byte code offset #1327
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1329	0	this	HttpRequest
/*      */     //   2	1327	1	retryRequest	boolean
/*      */     //   22	1307	2	retriesRemaining	int
/*      */     //   40	1289	3	response	HttpResponse
/*      */     //   69	1260	4	executeException	java.io.IOException
/*      */     //   95	1172	5	urlString	String
/*      */     //   110	1157	6	lowLevelHttpRequest	LowLevelHttpRequest
/*      */     //   115	1152	7	logger	java.util.logging.Logger
/*      */     //   140	1127	8	loggable	boolean
/*      */     //   143	1124	9	logbuf	StringBuilder
/*      */     //   146	1121	10	curlbuf	StringBuilder
/*      */     //   258	1009	11	originalUserAgent	String
/*      */     //   388	879	12	streamingContent	com.google.api.client.util.StreamingContent
/*      */     //   412	855	13	contentRetrySupported	boolean
/*      */     //   428	405	14	contentType	String
/*      */     //   464	14	15	contentEncoding	String
/*      */     //   489	344	15	contentEncoding	String
/*      */     //   947	69	15	lowLevelHttpResponse	LowLevelHttpResponse
/*      */     //   1021	48	15	e	java.io.IOException
/*      */     //   1072	195	15	responseProcessed	boolean
/*      */     //   475	3	16	contentLength	long
/*      */     //   522	311	16	contentLength	long
/*      */     //   564	70	18	header	String
/*      */     //   671	70	18	header	String
/*      */     //   777	14	18	header	String
/*      */     //   1196	0	18	exception	InterruptedException
/*      */     //   950	66	22	responseConstructed	boolean
/*      */     //   1086	126	22	errorHandled	boolean
/*      */     //   976	10	23	lowLevelContent	java.io.InputStream
/*      */     //   1003	10	25	lowLevelContent	java.io.InputStream
/*      */     //   1171	28	26	backOffTime	long
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   950	964	989	finally
/*      */     //   989	991	989	finally
/*      */     //   940	1016	1019	java/io/IOException
/*      */     //   1180	1191	1194	java/lang/InterruptedException
/*      */     //   1072	1233	1249	finally
/*      */     //   1249	1251	1249	finally
/*      */     //   1309	1320	1318	finally
/*      */   }
/*      */   
/*      */   @Beta
/*      */   public Future<HttpResponse> executeAsync(Executor executor)
/*      */   {
/* 1079 */     FutureTask<HttpResponse> future = new FutureTask(new Callable()
/*      */     {
/*      */       public HttpResponse call() throws Exception {
/* 1082 */         return HttpRequest.this.execute();
/*      */       }
/* 1084 */     });
/* 1085 */     executor.execute(future);
/* 1086 */     return future;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public Future<HttpResponse> executeAsync()
/*      */   {
/* 1099 */     return executeAsync(Executors.newSingleThreadExecutor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean handleRedirect(int statusCode, HttpHeaders responseHeaders)
/*      */   {
/* 1122 */     String redirectLocation = responseHeaders.getLocation();
/* 1123 */     if ((getFollowRedirects()) && (HttpStatusCodes.isRedirect(statusCode)) && (redirectLocation != null))
/*      */     {
/*      */ 
/* 1126 */       setUrl(new GenericUrl(this.url.toURL(redirectLocation)));
/*      */       
/* 1128 */       if (statusCode == 303) {
/* 1129 */         setRequestMethod("GET");
/*      */         
/* 1131 */         setContent(null);
/*      */       }
/*      */       
/* 1134 */       this.headers.setAuthorization((String)null);
/* 1135 */       this.headers.setIfMatch((String)null);
/* 1136 */       this.headers.setIfNoneMatch((String)null);
/* 1137 */       this.headers.setIfModifiedSince((String)null);
/* 1138 */       this.headers.setIfUnmodifiedSince((String)null);
/* 1139 */       this.headers.setIfRange((String)null);
/* 1140 */       return true;
/*      */     }
/* 1142 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Sleeper getSleeper()
/*      */   {
/* 1151 */     return this.sleeper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest setSleeper(Sleeper sleeper)
/*      */   {
/* 1160 */     this.sleeper = ((Sleeper)Preconditions.checkNotNull(sleeper));
/* 1161 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */