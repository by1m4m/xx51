/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.CookieManager;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.client.api.Response.Listener;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.CountingCallback;
/*     */ import org.eclipse.jetty.util.component.Destroyable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpReceiver
/*     */ {
/*  71 */   protected static final Logger LOG = Log.getLogger(HttpReceiver.class);
/*     */   
/*  73 */   private final AtomicReference<ResponseState> responseState = new AtomicReference(ResponseState.IDLE);
/*     */   private final HttpChannel channel;
/*     */   private ContentDecoder decoder;
/*     */   private Throwable failure;
/*     */   
/*     */   protected HttpReceiver(HttpChannel channel)
/*     */   {
/*  80 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   protected HttpChannel getHttpChannel()
/*     */   {
/*  85 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected HttpExchange getHttpExchange()
/*     */   {
/*  90 */     return this.channel.getHttpExchange();
/*     */   }
/*     */   
/*     */   protected HttpDestination getHttpDestination()
/*     */   {
/*  95 */     return this.channel.getHttpDestination();
/*     */   }
/*     */   
/*     */   public boolean isFailed()
/*     */   {
/* 100 */     return this.responseState.get() == ResponseState.FAILURE;
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
/*     */   protected boolean responseBegin(HttpExchange exchange)
/*     */   {
/* 116 */     if (!updateResponseState(ResponseState.IDLE, ResponseState.TRANSIENT)) {
/* 117 */       return false;
/*     */     }
/* 119 */     HttpConversation conversation = exchange.getConversation();
/* 120 */     HttpResponse response = exchange.getResponse();
/*     */     
/* 122 */     HttpDestination destination = getHttpDestination();
/* 123 */     HttpClient client = destination.getHttpClient();
/* 124 */     ProtocolHandler protocolHandler = client.findProtocolHandler(exchange.getRequest(), response);
/* 125 */     Response.Listener handlerListener = null;
/* 126 */     if (protocolHandler != null)
/*     */     {
/* 128 */       handlerListener = protocolHandler.getResponseListener();
/* 129 */       if (LOG.isDebugEnabled())
/* 130 */         LOG.debug("Found protocol handler {}", new Object[] { protocolHandler });
/*     */     }
/* 132 */     exchange.getConversation().updateResponseListeners(handlerListener);
/*     */     
/* 134 */     if (LOG.isDebugEnabled())
/* 135 */       LOG.debug("Response begin {}", new Object[] { response });
/* 136 */     ResponseNotifier notifier = destination.getResponseNotifier();
/* 137 */     notifier.notifyBegin(conversation.getResponseListeners(), response);
/*     */     
/* 139 */     if (updateResponseState(ResponseState.TRANSIENT, ResponseState.BEGIN)) {
/* 140 */       return true;
/*     */     }
/* 142 */     terminateResponse(exchange);
/* 143 */     return false;
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
/*     */   protected boolean responseHeader(HttpExchange exchange, HttpField field)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 162 */       ResponseState current = (ResponseState)this.responseState.get();
/* 163 */       switch (current)
/*     */       {
/*     */ 
/*     */       case BEGIN: 
/*     */       case HEADER: 
/* 168 */         if (!updateResponseState(current, ResponseState.TRANSIENT)) break;
/* 169 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 174 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 179 */     HttpResponse response = exchange.getResponse();
/* 180 */     ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 181 */     boolean process = notifier.notifyHeader(exchange.getConversation().getResponseListeners(), response, field);
/* 182 */     if (process)
/*     */     {
/* 184 */       response.getHeaders().add(field);
/* 185 */       HttpHeader fieldHeader = field.getHeader();
/* 186 */       if (fieldHeader != null)
/*     */       {
/* 188 */         switch (fieldHeader)
/*     */         {
/*     */ 
/*     */         case SET_COOKIE: 
/*     */         case SET_COOKIE2: 
/* 193 */           URI uri = exchange.getRequest().getURI();
/* 194 */           if (uri != null) {
/* 195 */             storeCookie(uri, field);
/*     */           }
/*     */           
/*     */ 
/*     */           break;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 206 */     if (updateResponseState(ResponseState.TRANSIENT, ResponseState.HEADER)) {
/* 207 */       return true;
/*     */     }
/* 209 */     terminateResponse(exchange);
/* 210 */     return false;
/*     */   }
/*     */   
/*     */   protected void storeCookie(URI uri, HttpField field)
/*     */   {
/*     */     try
/*     */     {
/* 217 */       String value = field.getValue();
/* 218 */       if (value != null)
/*     */       {
/* 220 */         Map<String, List<String>> header = new HashMap(1);
/* 221 */         header.put(field.getHeader().asString(), Collections.singletonList(value));
/* 222 */         getHttpDestination().getHttpClient().getCookieManager().put(uri, header);
/*     */       }
/*     */     }
/*     */     catch (IOException x)
/*     */     {
/* 227 */       if (LOG.isDebugEnabled()) {
/* 228 */         LOG.debug(x);
/*     */       }
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
/*     */   protected boolean responseHeaders(HttpExchange exchange)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 244 */       ResponseState current = (ResponseState)this.responseState.get();
/* 245 */       switch (current)
/*     */       {
/*     */ 
/*     */       case BEGIN: 
/*     */       case HEADER: 
/* 250 */         if (!updateResponseState(current, ResponseState.TRANSIENT)) break;
/* 251 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 256 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 261 */     HttpResponse response = exchange.getResponse();
/* 262 */     if (LOG.isDebugEnabled())
/* 263 */       LOG.debug("Response headers {}{}{}", new Object[] { response, System.lineSeparator(), response.getHeaders().toString().trim() });
/* 264 */     ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 265 */     notifier.notifyHeaders(exchange.getConversation().getResponseListeners(), response);
/*     */     
/* 267 */     Enumeration<String> contentEncodings = response.getHeaders().getValues(HttpHeader.CONTENT_ENCODING.asString(), ",");
/* 268 */     if (contentEncodings != null)
/*     */     {
/* 270 */       for (ContentDecoder.Factory factory : getHttpDestination().getHttpClient().getContentDecoderFactories())
/*     */       {
/* 272 */         while (contentEncodings.hasMoreElements())
/*     */         {
/* 274 */           if (factory.getEncoding().equalsIgnoreCase((String)contentEncodings.nextElement()))
/*     */           {
/* 276 */             this.decoder = factory.newContentDecoder();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 283 */     if (updateResponseState(ResponseState.TRANSIENT, ResponseState.HEADERS)) {
/* 284 */       return true;
/*     */     }
/* 286 */     terminateResponse(exchange);
/* 287 */     return false;
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
/*     */   protected boolean responseContent(HttpExchange exchange, ByteBuffer buffer, Callback callback)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 304 */       ResponseState current = (ResponseState)this.responseState.get();
/* 305 */       switch (current)
/*     */       {
/*     */ 
/*     */       case HEADERS: 
/*     */       case CONTENT: 
/* 310 */         if (!updateResponseState(current, ResponseState.TRANSIENT)) break;
/* 311 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 316 */         callback.failed(new IllegalStateException("Invalid response state " + current));
/* 317 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 322 */     HttpResponse response = exchange.getResponse();
/* 323 */     if (LOG.isDebugEnabled()) {
/* 324 */       LOG.debug("Response content {}{}{}", new Object[] { response, System.lineSeparator(), BufferUtil.toDetailString(buffer) });
/*     */     }
/* 326 */     ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 327 */     List<Response.ResponseListener> listeners = exchange.getConversation().getResponseListeners();
/*     */     
/* 329 */     ContentDecoder decoder = this.decoder;
/* 330 */     if (decoder == null)
/*     */     {
/* 332 */       notifier.notifyContent(listeners, response, buffer, callback);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 338 */         List<ByteBuffer> decodeds = new ArrayList(2);
/* 339 */         while (buffer.hasRemaining())
/*     */         {
/* 341 */           ByteBuffer decoded = decoder.decode(buffer);
/* 342 */           if (decoded.hasRemaining())
/*     */           {
/* 344 */             decodeds.add(decoded);
/* 345 */             if (LOG.isDebugEnabled())
/* 346 */               LOG.debug("Response content decoded ({}) {}{}{}", new Object[] { decoder, response, System.lineSeparator(), BufferUtil.toDetailString(decoded) });
/*     */           }
/*     */         }
/* 349 */         if (decodeds.isEmpty())
/*     */         {
/* 351 */           callback.succeeded();
/*     */         }
/*     */         else
/*     */         {
/* 355 */           int size = decodeds.size();
/* 356 */           CountingCallback counter = new CountingCallback(callback, size);
/* 357 */           for (int i = 0; i < size; i++) {
/* 358 */             notifier.notifyContent(listeners, response, (ByteBuffer)decodeds.get(i), counter);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 363 */         callback.failed(x);
/*     */       }
/*     */     }
/*     */     
/* 367 */     if (updateResponseState(ResponseState.TRANSIENT, ResponseState.CONTENT)) {
/* 368 */       return true;
/*     */     }
/* 370 */     terminateResponse(exchange);
/* 371 */     return false;
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
/*     */   protected boolean responseSuccess(HttpExchange exchange)
/*     */   {
/* 387 */     if (!exchange.responseComplete(null)) {
/* 388 */       return false;
/*     */     }
/* 390 */     this.responseState.set(ResponseState.IDLE);
/*     */     
/*     */ 
/* 393 */     reset();
/*     */     
/* 395 */     HttpResponse response = exchange.getResponse();
/* 396 */     if (LOG.isDebugEnabled())
/* 397 */       LOG.debug("Response success {}", new Object[] { response });
/* 398 */     List<Response.ResponseListener> listeners = exchange.getConversation().getResponseListeners();
/* 399 */     ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 400 */     notifier.notifySuccess(listeners, response);
/*     */     
/*     */ 
/*     */ 
/* 404 */     if (exchange.getResponse().getStatus() == 100) {
/* 405 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 409 */     Result result = exchange.terminateResponse();
/* 410 */     terminateResponse(exchange, result);
/*     */     
/* 412 */     return true;
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
/*     */   protected boolean responseFailure(Throwable failure)
/*     */   {
/* 425 */     HttpExchange exchange = getHttpExchange();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 430 */     if (exchange == null) {
/* 431 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 435 */     if (exchange.responseComplete(failure)) {
/* 436 */       return abort(exchange, failure);
/*     */     }
/* 438 */     return false;
/*     */   }
/*     */   
/*     */   private void terminateResponse(HttpExchange exchange)
/*     */   {
/* 443 */     Result result = exchange.terminateResponse();
/* 444 */     terminateResponse(exchange, result);
/*     */   }
/*     */   
/*     */   private void terminateResponse(HttpExchange exchange, Result result)
/*     */   {
/* 449 */     HttpResponse response = exchange.getResponse();
/*     */     
/* 451 */     if (LOG.isDebugEnabled()) {
/* 452 */       LOG.debug("Response complete {}", new Object[] { response });
/*     */     }
/* 454 */     if (result != null)
/*     */     {
/* 456 */       result = this.channel.exchangeTerminating(exchange, result);
/* 457 */       boolean ordered = getHttpDestination().getHttpClient().isStrictEventOrdering();
/* 458 */       if (!ordered)
/* 459 */         this.channel.exchangeTerminated(exchange, result);
/* 460 */       if (LOG.isDebugEnabled())
/* 461 */         LOG.debug("Request/Response {}: {}", new Object[] { this.failure == null ? "succeeded" : "failed", result });
/* 462 */       List<Response.ResponseListener> listeners = exchange.getConversation().getResponseListeners();
/* 463 */       ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 464 */       notifier.notifyComplete(listeners, result);
/* 465 */       if (ordered) {
/* 466 */         this.channel.exchangeTerminated(exchange, result);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 479 */     destroyDecoder(this.decoder);
/* 480 */     this.decoder = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dispose()
/*     */   {
/* 492 */     destroyDecoder(this.decoder);
/* 493 */     this.decoder = null;
/*     */   }
/*     */   
/*     */   private static void destroyDecoder(ContentDecoder decoder)
/*     */   {
/* 498 */     if ((decoder instanceof Destroyable))
/*     */     {
/* 500 */       ((Destroyable)decoder).destroy();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean abort(HttpExchange exchange, Throwable failure)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 510 */       ResponseState current = (ResponseState)this.responseState.get();
/* 511 */       switch (current)
/*     */       {
/*     */ 
/*     */       case FAILURE: 
/* 515 */         return false;
/*     */       }
/*     */       
/*     */       
/* 519 */       if (updateResponseState(current, ResponseState.FAILURE))
/*     */       {
/* 521 */         boolean terminate = current != ResponseState.TRANSIENT;
/* 522 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     boolean terminate;
/*     */     
/* 529 */     this.failure = failure;
/*     */     
/* 531 */     dispose();
/*     */     
/* 533 */     HttpResponse response = exchange.getResponse();
/* 534 */     if (LOG.isDebugEnabled())
/* 535 */       LOG.debug("Response failure {} {} on {}: {}", new Object[] { response, exchange, getHttpChannel(), failure });
/* 536 */     List<Response.ResponseListener> listeners = exchange.getConversation().getResponseListeners();
/* 537 */     ResponseNotifier notifier = getHttpDestination().getResponseNotifier();
/* 538 */     notifier.notifyFailure(listeners, response, failure);
/*     */     
/* 540 */     if (terminate)
/*     */     {
/*     */ 
/*     */ 
/* 544 */       Result result = exchange.terminateResponse();
/* 545 */       terminateResponse(exchange, result);
/*     */ 
/*     */ 
/*     */     }
/* 549 */     else if (LOG.isDebugEnabled()) {
/* 550 */       LOG.debug("Concurrent failure: response termination skipped, performed by helpers", new Object[0]);
/*     */     }
/*     */     
/* 553 */     return true;
/*     */   }
/*     */   
/*     */   private boolean updateResponseState(ResponseState from, ResponseState to)
/*     */   {
/* 558 */     boolean updated = this.responseState.compareAndSet(from, to);
/* 559 */     if (!updated)
/*     */     {
/* 561 */       if (LOG.isDebugEnabled())
/* 562 */         LOG.debug("State update failed: {} -> {}: {}", new Object[] { from, to, this.responseState.get() });
/*     */     }
/* 564 */     return updated;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 570 */     return String.format("%s@%x(rsp=%s,failure=%s)", new Object[] {
/* 571 */       getClass().getSimpleName(), 
/* 572 */       Integer.valueOf(hashCode()), this.responseState, this.failure });
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
/*     */   private static enum ResponseState
/*     */   {
/* 585 */     TRANSIENT, 
/*     */     
/*     */ 
/*     */ 
/* 589 */     IDLE, 
/*     */     
/*     */ 
/*     */ 
/* 593 */     BEGIN, 
/*     */     
/*     */ 
/*     */ 
/* 597 */     HEADER, 
/*     */     
/*     */ 
/*     */ 
/* 601 */     HEADERS, 
/*     */     
/*     */ 
/*     */ 
/* 605 */     CONTENT, 
/*     */     
/*     */ 
/*     */ 
/* 609 */     FAILURE;
/*     */     
/*     */     private ResponseState() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */