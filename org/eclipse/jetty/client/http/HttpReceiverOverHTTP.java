/*     */ package org.eclipse.jetty.client.http;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.client.HttpDestination;
/*     */ import org.eclipse.jetty.client.HttpExchange;
/*     */ import org.eclipse.jetty.client.HttpReceiver;
/*     */ import org.eclipse.jetty.client.HttpRequest;
/*     */ import org.eclipse.jetty.client.HttpResponse;
/*     */ import org.eclipse.jetty.client.HttpResponseException;
/*     */ import org.eclipse.jetty.http.BadMessageException;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpMethod;
/*     */ import org.eclipse.jetty.http.HttpParser;
/*     */ import org.eclipse.jetty.http.HttpParser.ResponseHandler;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.CompletableCallback;
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
/*     */ public class HttpReceiverOverHTTP
/*     */   extends HttpReceiver
/*     */   implements HttpParser.ResponseHandler
/*     */ {
/*     */   private final HttpParser parser;
/*     */   private ByteBuffer buffer;
/*     */   private boolean shutdown;
/*     */   
/*     */   public HttpReceiverOverHTTP(HttpChannelOverHTTP channel)
/*     */   {
/*  48 */     super(channel);
/*  49 */     this.parser = new HttpParser(this, -1, channel.getHttpDestination().getHttpClient().getHttpCompliance());
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpChannelOverHTTP getHttpChannel()
/*     */   {
/*  55 */     return (HttpChannelOverHTTP)super.getHttpChannel();
/*     */   }
/*     */   
/*     */   private HttpConnectionOverHTTP getHttpConnection()
/*     */   {
/*  60 */     return getHttpChannel().getHttpConnection();
/*     */   }
/*     */   
/*     */   protected ByteBuffer getResponseBuffer()
/*     */   {
/*  65 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public void receive()
/*     */   {
/*  70 */     if (this.buffer == null)
/*  71 */       acquireBuffer();
/*  72 */     process();
/*     */   }
/*     */   
/*     */   private void acquireBuffer()
/*     */   {
/*  77 */     HttpClient client = getHttpDestination().getHttpClient();
/*  78 */     ByteBufferPool bufferPool = client.getByteBufferPool();
/*  79 */     this.buffer = bufferPool.acquire(client.getResponseBufferSize(), true);
/*     */   }
/*     */   
/*     */   private void releaseBuffer()
/*     */   {
/*  84 */     if (this.buffer == null)
/*  85 */       throw new IllegalStateException();
/*  86 */     if (BufferUtil.hasContent(this.buffer))
/*  87 */       throw new IllegalStateException();
/*  88 */     HttpClient client = getHttpDestination().getHttpClient();
/*  89 */     ByteBufferPool bufferPool = client.getByteBufferPool();
/*  90 */     bufferPool.release(this.buffer);
/*  91 */     this.buffer = null;
/*     */   }
/*     */   
/*     */   protected ByteBuffer onUpgradeFrom()
/*     */   {
/*  96 */     if (BufferUtil.hasContent(this.buffer))
/*     */     {
/*  98 */       ByteBuffer upgradeBuffer = ByteBuffer.allocate(this.buffer.remaining());
/*  99 */       upgradeBuffer.put(this.buffer).flip();
/* 100 */       return upgradeBuffer;
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   private void process()
/*     */   {
/*     */     try
/*     */     {
/* 109 */       HttpConnectionOverHTTP connection = getHttpConnection();
/* 110 */       EndPoint endPoint = connection.getEndPoint();
/*     */       for (;;)
/*     */       {
/* 113 */         boolean upgraded = connection != endPoint.getConnection();
/*     */         
/*     */ 
/* 116 */         if ((connection.isClosed()) || (upgraded))
/*     */         {
/* 118 */           if (LOG.isDebugEnabled())
/* 119 */             LOG.debug("{} {}", new Object[] { connection, upgraded ? "upgraded" : "closed" });
/* 120 */           releaseBuffer();
/* 121 */           return;
/*     */         }
/*     */         
/* 124 */         if (parse()) {
/* 125 */           return;
/*     */         }
/* 127 */         int read = endPoint.fill(this.buffer);
/* 128 */         if (LOG.isDebugEnabled()) {
/* 129 */           LOG.debug("Read {} bytes {} from {}", new Object[] { Integer.valueOf(read), BufferUtil.toDetailString(this.buffer), endPoint });
/*     */         }
/* 131 */         if (read > 0)
/*     */         {
/* 133 */           connection.addBytesIn(read);
/* 134 */           if (!parse()) {}
/*     */         }
/*     */         else {
/* 137 */           if (read == 0)
/*     */           {
/* 139 */             releaseBuffer();
/* 140 */             fillInterested();
/* 141 */             return;
/*     */           }
/*     */           
/*     */ 
/* 145 */           releaseBuffer();
/* 146 */           shutdown();
/* 147 */           return;
/*     */         }
/*     */       }
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
/* 160 */       return;
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 153 */       if (LOG.isDebugEnabled())
/* 154 */         LOG.debug(x);
/* 155 */       BufferUtil.clear(this.buffer);
/* 156 */       if (this.buffer != null)
/* 157 */         releaseBuffer();
/* 158 */       failAndClose(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parse()
/*     */   {
/*     */     for (;;)
/*     */     {
/* 173 */       boolean handle = this.parser.parseNext(this.buffer);
/* 174 */       if (LOG.isDebugEnabled())
/* 175 */         LOG.debug("Parsed {}, remaining {} {}", new Object[] { Boolean.valueOf(handle), Integer.valueOf(this.buffer.remaining()), this.parser });
/* 176 */       if ((handle) || (!this.buffer.hasRemaining())) {
/* 177 */         return handle;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fillInterested() {
/* 183 */     getHttpConnection().fillInterested();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void shutdown()
/*     */   {
/* 192 */     this.shutdown = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */     this.parser.atEOF();
/* 199 */     this.parser.parseNext(BufferUtil.EMPTY_BUFFER);
/*     */   }
/*     */   
/*     */   protected boolean isShutdown()
/*     */   {
/* 204 */     return this.shutdown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getHeaderCacheSize()
/*     */   {
/* 211 */     return 256;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean startResponse(HttpVersion version, int status, String reason)
/*     */   {
/* 217 */     HttpExchange exchange = getHttpExchange();
/* 218 */     if (exchange == null) {
/* 219 */       return false;
/*     */     }
/* 221 */     String method = exchange.getRequest().getMethod();
/* 222 */     this.parser.setHeadResponse((HttpMethod.HEAD.is(method)) || (
/* 223 */       (HttpMethod.CONNECT.is(method)) && (status == 200)));
/* 224 */     exchange.getResponse().version(version).status(status).reason(reason);
/*     */     
/* 226 */     return !responseBegin(exchange);
/*     */   }
/*     */   
/*     */ 
/*     */   public void parsedHeader(HttpField field)
/*     */   {
/* 232 */     HttpExchange exchange = getHttpExchange();
/* 233 */     if (exchange == null) {
/* 234 */       return;
/*     */     }
/* 236 */     responseHeader(exchange, field);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean headerComplete()
/*     */   {
/* 242 */     HttpExchange exchange = getHttpExchange();
/* 243 */     if (exchange == null) {
/* 244 */       return false;
/*     */     }
/* 246 */     return !responseHeaders(exchange);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean content(ByteBuffer buffer)
/*     */   {
/* 252 */     HttpExchange exchange = getHttpExchange();
/* 253 */     if (exchange == null) {
/* 254 */       return false;
/*     */     }
/* 256 */     CompletableCallback callback = new CompletableCallback()
/*     */     {
/*     */ 
/*     */       public void resume()
/*     */       {
/* 261 */         if (HttpReceiverOverHTTP.LOG.isDebugEnabled())
/* 262 */           HttpReceiverOverHTTP.LOG.debug("Content consumed asynchronously, resuming processing", new Object[0]);
/* 263 */         HttpReceiverOverHTTP.this.process();
/*     */       }
/*     */       
/*     */ 
/*     */       public void abort(Throwable x)
/*     */       {
/* 269 */         HttpReceiverOverHTTP.this.failAndClose(x);
/*     */       }
/*     */       
/* 272 */     };
/* 273 */     boolean proceed = responseContent(exchange, buffer, callback);
/* 274 */     boolean async = callback.tryComplete();
/* 275 */     return (!proceed) || (async);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contentComplete()
/*     */   {
/* 281 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void parsedTrailer(HttpField trailer)
/*     */   {
/* 287 */     HttpExchange exchange = getHttpExchange();
/* 288 */     if (exchange == null) {
/* 289 */       return;
/*     */     }
/* 291 */     exchange.getResponse().trailer(trailer);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean messageComplete()
/*     */   {
/* 297 */     HttpExchange exchange = getHttpExchange();
/* 298 */     if (exchange == null) {
/* 299 */       return false;
/*     */     }
/* 301 */     boolean proceed = responseSuccess(exchange);
/* 302 */     if (!proceed) {
/* 303 */       return true;
/*     */     }
/* 305 */     int status = exchange.getResponse().getStatus();
/* 306 */     if (status == 101) {
/* 307 */       return true;
/*     */     }
/* 309 */     if ((HttpMethod.CONNECT.is(exchange.getRequest().getMethod())) && (status == 200))
/*     */     {
/* 311 */       return true;
/*     */     }
/* 313 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void earlyEOF()
/*     */   {
/* 319 */     HttpExchange exchange = getHttpExchange();
/* 320 */     HttpConnectionOverHTTP connection = getHttpConnection();
/* 321 */     if (exchange == null) {
/* 322 */       connection.close();
/*     */     } else {
/* 324 */       failAndClose(new EOFException(String.valueOf(connection)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void badMessage(BadMessageException failure)
/*     */   {
/* 330 */     HttpExchange exchange = getHttpExchange();
/* 331 */     if (exchange != null)
/*     */     {
/* 333 */       HttpResponse response = exchange.getResponse();
/* 334 */       response.status(failure.getCode()).reason(failure.getReason());
/* 335 */       failAndClose(new HttpResponseException("HTTP protocol violation: bad response on " + getHttpConnection(), response));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 342 */     super.reset();
/* 343 */     this.parser.reset();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void dispose()
/*     */   {
/* 349 */     super.dispose();
/* 350 */     this.parser.close();
/*     */   }
/*     */   
/*     */   private void failAndClose(Throwable failure)
/*     */   {
/* 355 */     if (responseFailure(failure)) {
/* 356 */       getHttpConnection().close(failure);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 362 */     return String.format("%s[%s]", new Object[] { super.toString(), this.parser });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpReceiverOverHTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */