/*     */ package org.eclipse.jetty.client.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.client.HttpContent;
/*     */ import org.eclipse.jetty.client.HttpDestination;
/*     */ import org.eclipse.jetty.client.HttpExchange;
/*     */ import org.eclipse.jetty.client.HttpRequest;
/*     */ import org.eclipse.jetty.client.HttpRequestException;
/*     */ import org.eclipse.jetty.client.HttpSender;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.http.HttpGenerator;
/*     */ import org.eclipse.jetty.http.HttpGenerator.Result;
/*     */ import org.eclipse.jetty.http.HttpURI;
/*     */ import org.eclipse.jetty.http.MetaData.Request;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.Callback.Nested;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
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
/*     */ public class HttpSenderOverHTTP
/*     */   extends HttpSender
/*     */ {
/*  41 */   private final HttpGenerator generator = new HttpGenerator();
/*     */   private final HttpClient httpClient;
/*     */   private boolean shutdown;
/*     */   
/*     */   public HttpSenderOverHTTP(HttpChannelOverHTTP channel)
/*     */   {
/*  47 */     super(channel);
/*  48 */     this.httpClient = channel.getHttpDestination().getHttpClient();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpChannelOverHTTP getHttpChannel()
/*     */   {
/*  54 */     return (HttpChannelOverHTTP)super.getHttpChannel();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void sendHeaders(HttpExchange exchange, HttpContent content, Callback callback)
/*     */   {
/*     */     try
/*     */     {
/*  62 */       new HeadersCallback(exchange, content, callback, getHttpChannel().getHttpConnection()).iterate();
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  66 */       if (LOG.isDebugEnabled())
/*  67 */         LOG.debug(x);
/*  68 */       callback.failed(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void sendContent(HttpExchange exchange, HttpContent content, Callback callback)
/*     */   {
/*     */     try
/*     */     {
/*  77 */       ByteBufferPool bufferPool = this.httpClient.getByteBufferPool();
/*  78 */       ByteBuffer chunk = null;
/*     */       for (;;)
/*     */       {
/*  81 */         ByteBuffer contentBuffer = content.getByteBuffer();
/*  82 */         boolean lastContent = content.isLast();
/*  83 */         HttpGenerator.Result result = this.generator.generateRequest(null, null, chunk, contentBuffer, lastContent);
/*  84 */         if (LOG.isDebugEnabled()) {
/*  85 */           LOG.debug("Generated content ({} bytes) - {}/{}", new Object[] {
/*  86 */             Integer.valueOf(contentBuffer == null ? -1 : contentBuffer.remaining()), result, this.generator });
/*     */         }
/*  88 */         switch (result)
/*     */         {
/*     */ 
/*     */         case NEED_CHUNK: 
/*  92 */           chunk = bufferPool.acquire(12, false);
/*  93 */           break;
/*     */         
/*     */ 
/*     */         case NEED_CHUNK_TRAILER: 
/*  97 */           callback.succeeded();
/*  98 */           return;
/*     */         
/*     */ 
/*     */         case FLUSH: 
/* 102 */           EndPoint endPoint = getHttpChannel().getHttpConnection().getEndPoint();
/* 103 */           if (chunk != null) {
/* 104 */             endPoint.write(new ByteBufferRecyclerCallback(callback, bufferPool, new ByteBuffer[] { chunk }, null), new ByteBuffer[] { chunk, contentBuffer });
/*     */           } else
/* 106 */             endPoint.write(callback, new ByteBuffer[] { contentBuffer });
/* 107 */           return;
/*     */         
/*     */ 
/*     */         case SHUTDOWN_OUT: 
/* 111 */           shutdownOutput();
/* 112 */           break;
/*     */         
/*     */ 
/*     */         case CONTINUE: 
/* 116 */           if (!lastContent)
/*     */           {
/* 118 */             callback.succeeded();
/* 119 */             return;
/*     */           }
/*     */           break;
/*     */         case DONE: 
/* 123 */           callback.succeeded();
/* 124 */           return;
/*     */         
/*     */ 
/*     */         default: 
/* 128 */           throw new IllegalStateException(result.toString());
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */       return;
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 135 */       if (LOG.isDebugEnabled())
/* 136 */         LOG.debug(x);
/* 137 */       callback.failed(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void sendTrailers(HttpExchange exchange, Callback callback)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       new TrailersCallback(callback).iterate();
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 150 */       if (LOG.isDebugEnabled())
/* 151 */         LOG.debug(x);
/* 152 */       callback.failed(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 159 */     this.generator.reset();
/* 160 */     super.reset();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void dispose()
/*     */   {
/* 166 */     this.generator.abort();
/* 167 */     super.dispose();
/* 168 */     shutdownOutput();
/*     */   }
/*     */   
/*     */   private void shutdownOutput()
/*     */   {
/* 173 */     if (LOG.isDebugEnabled())
/* 174 */       LOG.debug("Request shutdown output {}", new Object[] { getHttpExchange().getRequest() });
/* 175 */     this.shutdown = true;
/*     */   }
/*     */   
/*     */   protected boolean isShutdown()
/*     */   {
/* 180 */     return this.shutdown;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     return String.format("%s[%s]", new Object[] { super.toString(), this.generator });
/*     */   }
/*     */   
/*     */   private class HeadersCallback extends IteratingCallback
/*     */   {
/*     */     private final HttpExchange exchange;
/*     */     private final Callback callback;
/*     */     private final MetaData.Request metaData;
/*     */     private final HttpConnectionOverHTTP httpConnectionOverHTTP;
/*     */     private ByteBuffer headerBuffer;
/*     */     private ByteBuffer chunkBuffer;
/*     */     private ByteBuffer contentBuffer;
/*     */     private boolean lastContent;
/*     */     private boolean generated;
/*     */     
/*     */     public HeadersCallback(HttpExchange exchange, HttpContent content, Callback callback, HttpConnectionOverHTTP httpConnectionOverHTTP)
/*     */     {
/* 203 */       super();
/* 204 */       this.exchange = exchange;
/* 205 */       this.callback = callback;
/* 206 */       this.httpConnectionOverHTTP = httpConnectionOverHTTP;
/*     */       
/* 208 */       HttpRequest request = exchange.getRequest();
/* 209 */       ContentProvider requestContent = request.getContent();
/* 210 */       long contentLength = requestContent == null ? -1L : requestContent.getLength();
/* 211 */       String path = request.getPath();
/* 212 */       String query = request.getQuery();
/* 213 */       if (query != null)
/* 214 */         path = path + "?" + query;
/* 215 */       this.metaData = new MetaData.Request(request.getMethod(), new HttpURI(path), request.getVersion(), request.getHeaders(), contentLength);
/* 216 */       this.metaData.setTrailerSupplier(request.getTrailers());
/*     */       
/* 218 */       if (!HttpSenderOverHTTP.this.expects100Continue(request))
/*     */       {
/* 220 */         content.advance();
/* 221 */         this.contentBuffer = content.getByteBuffer();
/* 222 */         this.lastContent = content.isLast();
/*     */       }
/*     */     }
/*     */     
/*     */     protected IteratingCallback.Action process()
/*     */       throws Exception
/*     */     {
/*     */       for (;;)
/*     */       {
/* 231 */         HttpGenerator.Result result = HttpSenderOverHTTP.this.generator.generateRequest(this.metaData, this.headerBuffer, this.chunkBuffer, this.contentBuffer, this.lastContent);
/* 232 */         if (HttpSenderOverHTTP.LOG.isDebugEnabled())
/* 233 */           HttpSenderOverHTTP.LOG.debug("Generated headers ({} bytes), chunk ({} bytes), content ({} bytes) - {}/{}", new Object[] {
/* 234 */             Integer.valueOf(this.headerBuffer == null ? -1 : this.headerBuffer.remaining()), 
/* 235 */             Integer.valueOf(this.chunkBuffer == null ? -1 : this.chunkBuffer.remaining()), 
/* 236 */             Integer.valueOf(this.contentBuffer == null ? -1 : this.contentBuffer.remaining()), result, 
/* 237 */             HttpSenderOverHTTP.this.generator });
/* 238 */         switch (HttpSenderOverHTTP.1.$SwitchMap$org$eclipse$jetty$http$HttpGenerator$Result[result.ordinal()])
/*     */         {
/*     */ 
/*     */         case 7: 
/* 242 */           this.headerBuffer = HttpSenderOverHTTP.this.httpClient.getByteBufferPool().acquire(HttpSenderOverHTTP.this.httpClient.getRequestBufferSize(), false);
/* 243 */           break;
/*     */         
/*     */ 
/*     */         case 1: 
/* 247 */           this.chunkBuffer = HttpSenderOverHTTP.this.httpClient.getByteBufferPool().acquire(12, false);
/* 248 */           break;
/*     */         
/*     */ 
/*     */         case 2: 
/* 252 */           return IteratingCallback.Action.SUCCEEDED;
/*     */         
/*     */ 
/*     */         case 3: 
/* 256 */           EndPoint endPoint = HttpSenderOverHTTP.this.getHttpChannel().getHttpConnection().getEndPoint();
/* 257 */           if (this.headerBuffer == null)
/* 258 */             this.headerBuffer = BufferUtil.EMPTY_BUFFER;
/* 259 */           if (this.chunkBuffer == null)
/* 260 */             this.chunkBuffer = BufferUtil.EMPTY_BUFFER;
/* 261 */           if (this.contentBuffer == null) {
/* 262 */             this.contentBuffer = BufferUtil.EMPTY_BUFFER;
/*     */           }
/* 264 */           this.httpConnectionOverHTTP.addBytesOut(BufferUtil.length(this.headerBuffer) + 
/* 265 */             BufferUtil.length(this.chunkBuffer) + 
/* 266 */             BufferUtil.length(this.contentBuffer));
/*     */           
/* 268 */           endPoint.write(this, new ByteBuffer[] { this.headerBuffer, this.chunkBuffer, this.contentBuffer });
/* 269 */           this.generated = true;
/* 270 */           return IteratingCallback.Action.SCHEDULED;
/*     */         
/*     */ 
/*     */         case 4: 
/* 274 */           HttpSenderOverHTTP.this.shutdownOutput();
/* 275 */           return IteratingCallback.Action.SUCCEEDED;
/*     */         
/*     */ 
/*     */         case 5: 
/* 279 */           if (this.generated) {
/* 280 */             return IteratingCallback.Action.SUCCEEDED;
/*     */           }
/*     */           
/*     */           break;
/*     */         case 6: 
/* 285 */           if (this.generated) {
/* 286 */             return IteratingCallback.Action.SUCCEEDED;
/*     */           }
/*     */           
/* 289 */           throw new HttpRequestException("Could not generate headers", this.exchange.getRequest());
/*     */         
/*     */ 
/*     */         default: 
/* 293 */           throw new IllegalStateException(result.toString());
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 302 */       release();
/* 303 */       super.succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 309 */       release();
/* 310 */       this.callback.failed(x);
/* 311 */       super.failed(x);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void onCompleteSuccess()
/*     */     {
/* 317 */       super.onCompleteSuccess();
/* 318 */       this.callback.succeeded();
/*     */     }
/*     */     
/*     */     private void release()
/*     */     {
/* 323 */       ByteBufferPool bufferPool = HttpSenderOverHTTP.this.httpClient.getByteBufferPool();
/* 324 */       if (!BufferUtil.isTheEmptyBuffer(this.headerBuffer))
/* 325 */         bufferPool.release(this.headerBuffer);
/* 326 */       this.headerBuffer = null;
/* 327 */       if (!BufferUtil.isTheEmptyBuffer(this.chunkBuffer))
/* 328 */         bufferPool.release(this.chunkBuffer);
/* 329 */       this.chunkBuffer = null;
/* 330 */       this.contentBuffer = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private class TrailersCallback extends IteratingCallback
/*     */   {
/*     */     private final Callback callback;
/*     */     private ByteBuffer chunkBuffer;
/*     */     
/*     */     public TrailersCallback(Callback callback)
/*     */     {
/* 341 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     protected IteratingCallback.Action process()
/*     */       throws Throwable
/*     */     {
/*     */       for (;;)
/*     */       {
/* 349 */         HttpGenerator.Result result = HttpSenderOverHTTP.this.generator.generateRequest(null, null, this.chunkBuffer, null, true);
/* 350 */         if (HttpSenderOverHTTP.LOG.isDebugEnabled())
/* 351 */           HttpSenderOverHTTP.LOG.debug("Generated trailers {}/{}", new Object[] { result, HttpSenderOverHTTP.this.generator });
/* 352 */         switch (HttpSenderOverHTTP.1.$SwitchMap$org$eclipse$jetty$http$HttpGenerator$Result[result.ordinal()])
/*     */         {
/*     */ 
/*     */         case 2: 
/* 356 */           this.chunkBuffer = HttpSenderOverHTTP.this.httpClient.getByteBufferPool().acquire(HttpSenderOverHTTP.this.httpClient.getRequestBufferSize(), false);
/* 357 */           break;
/*     */         
/*     */ 
/*     */         case 3: 
/* 361 */           EndPoint endPoint = HttpSenderOverHTTP.this.getHttpChannel().getHttpConnection().getEndPoint();
/* 362 */           endPoint.write(this, new ByteBuffer[] { this.chunkBuffer });
/* 363 */           return IteratingCallback.Action.SCHEDULED;
/*     */         
/*     */ 
/*     */         case 4: 
/* 367 */           HttpSenderOverHTTP.this.shutdownOutput();
/* 368 */           return IteratingCallback.Action.SUCCEEDED;
/*     */         
/*     */ 
/*     */         case 6: 
/* 372 */           return IteratingCallback.Action.SUCCEEDED;
/*     */         
/*     */         case 5: 
/*     */         default: 
/* 376 */           throw new IllegalStateException(result.toString());
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 385 */       release();
/* 386 */       super.succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 392 */       release();
/* 393 */       this.callback.failed(x);
/* 394 */       super.failed(x);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void onCompleteSuccess()
/*     */     {
/* 400 */       super.onCompleteSuccess();
/* 401 */       this.callback.succeeded();
/*     */     }
/*     */     
/*     */     private void release()
/*     */     {
/* 406 */       HttpSenderOverHTTP.this.httpClient.getByteBufferPool().release(this.chunkBuffer);
/* 407 */       this.chunkBuffer = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ByteBufferRecyclerCallback extends Callback.Nested
/*     */   {
/*     */     private final ByteBufferPool pool;
/*     */     private final ByteBuffer[] buffers;
/*     */     
/*     */     private ByteBufferRecyclerCallback(Callback callback, ByteBufferPool pool, ByteBuffer... buffers)
/*     */     {
/* 418 */       super();
/* 419 */       this.pool = pool;
/* 420 */       this.buffers = buffers;
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 426 */       for (ByteBuffer buffer : this.buffers)
/*     */       {
/* 428 */         assert (!buffer.hasRemaining());
/* 429 */         this.pool.release(buffer);
/*     */       }
/* 431 */       super.succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 437 */       for (ByteBuffer buffer : this.buffers)
/* 438 */         this.pool.release(buffer);
/* 439 */       super.failed(x);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpSenderOverHTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */