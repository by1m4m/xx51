/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Supplier;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.http.HttpHeaderValue;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
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
/*     */ public abstract class HttpSender
/*     */   implements AsyncContentProvider.Listener
/*     */ {
/*  62 */   protected static final Logger LOG = Log.getLogger(HttpSender.class);
/*     */   
/*  64 */   private final AtomicReference<RequestState> requestState = new AtomicReference(RequestState.QUEUED);
/*  65 */   private final AtomicReference<SenderState> senderState = new AtomicReference(SenderState.IDLE);
/*  66 */   private final Callback commitCallback = new CommitCallback(null);
/*  67 */   private final IteratingCallback contentCallback = new ContentCallback(null);
/*  68 */   private final Callback trailersCallback = new TrailersCallback(null);
/*  69 */   private final Callback lastCallback = new LastCallback(null);
/*     */   private final HttpChannel channel;
/*     */   private HttpContent content;
/*     */   private Throwable failure;
/*     */   
/*     */   protected HttpSender(HttpChannel channel)
/*     */   {
/*  76 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   protected HttpChannel getHttpChannel()
/*     */   {
/*  81 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected HttpExchange getHttpExchange()
/*     */   {
/*  86 */     return this.channel.getHttpExchange();
/*     */   }
/*     */   
/*     */   public boolean isFailed()
/*     */   {
/*  91 */     return this.requestState.get() == RequestState.FAILURE;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onContent()
/*     */   {
/*  97 */     HttpExchange exchange = getHttpExchange();
/*  98 */     if (exchange == null) {
/*  99 */       return;
/*     */     }
/*     */     for (;;)
/*     */     {
/* 103 */       SenderState current = (SenderState)this.senderState.get();
/* 104 */       switch (current)
/*     */       {
/*     */ 
/*     */       case IDLE: 
/* 108 */         SenderState newSenderState = SenderState.SENDING;
/* 109 */         if (updateSenderState(current, newSenderState))
/*     */         {
/* 111 */           if (LOG.isDebugEnabled())
/* 112 */             LOG.debug("Deferred content available, {} -> {}", new Object[] { current, newSenderState });
/* 113 */           this.contentCallback.iterate();
/* 114 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case SENDING: 
/* 120 */         SenderState newSenderState = SenderState.SENDING_WITH_CONTENT;
/* 121 */         if (updateSenderState(current, newSenderState))
/*     */         {
/* 123 */           if (LOG.isDebugEnabled())
/* 124 */             LOG.debug("Deferred content available, {} -> {}", new Object[] { current, newSenderState });
/* 125 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case EXPECTING: 
/* 131 */         SenderState newSenderState = SenderState.EXPECTING_WITH_CONTENT;
/* 132 */         if (updateSenderState(current, newSenderState))
/*     */         {
/* 134 */           if (LOG.isDebugEnabled())
/* 135 */             LOG.debug("Deferred content available, {} -> {}", new Object[] { current, newSenderState });
/* 136 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case PROCEEDING: 
/* 142 */         SenderState newSenderState = SenderState.PROCEEDING_WITH_CONTENT;
/* 143 */         if (updateSenderState(current, newSenderState))
/*     */         {
/* 145 */           if (LOG.isDebugEnabled())
/* 146 */             LOG.debug("Deferred content available, {} -> {}", new Object[] { current, newSenderState });
/* 147 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case SENDING_WITH_CONTENT: 
/*     */       case EXPECTING_WITH_CONTENT: 
/*     */       case PROCEEDING_WITH_CONTENT: 
/*     */       case WAITING: 
/*     */       case COMPLETED: 
/*     */       case FAILED: 
/* 158 */         if (LOG.isDebugEnabled())
/* 159 */           LOG.debug("Deferred content available, {}", new Object[] { current });
/* 160 */         return;
/*     */       
/*     */ 
/*     */       default: 
/* 164 */         illegalSenderState(current);
/* 165 */         return;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public void send(HttpExchange exchange)
/*     */   {
/* 173 */     if (!queuedToBegin(exchange)) {
/* 174 */       return;
/*     */     }
/* 176 */     Request request = exchange.getRequest();
/* 177 */     ContentProvider contentProvider = request.getContent();
/* 178 */     HttpContent content = this.content = new HttpContent(contentProvider);
/*     */     
/* 180 */     SenderState newSenderState = SenderState.SENDING;
/* 181 */     if (expects100Continue(request)) {
/* 182 */       newSenderState = content.hasContent() ? SenderState.EXPECTING_WITH_CONTENT : SenderState.EXPECTING;
/*     */     }
/*     */     for (;;)
/*     */     {
/* 186 */       SenderState current = (SenderState)this.senderState.get();
/* 187 */       switch (current)
/*     */       {
/*     */ 
/*     */       case IDLE: 
/*     */       case COMPLETED: 
/* 192 */         if (!updateSenderState(current, newSenderState)) break;
/* 193 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 198 */         illegalSenderState(current);
/* 199 */         return;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 206 */     if ((contentProvider instanceof AsyncContentProvider)) {
/* 207 */       ((AsyncContentProvider)contentProvider).setListener(this);
/*     */     }
/* 209 */     if (!beginToHeaders(exchange)) {
/* 210 */       return;
/*     */     }
/* 212 */     sendHeaders(exchange, content, this.commitCallback);
/*     */   }
/*     */   
/*     */   protected boolean expects100Continue(Request request)
/*     */   {
/* 217 */     return request.getHeaders().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
/*     */   }
/*     */   
/*     */   protected boolean queuedToBegin(HttpExchange exchange)
/*     */   {
/* 222 */     if (!updateRequestState(RequestState.QUEUED, RequestState.TRANSIENT)) {
/* 223 */       return false;
/*     */     }
/* 225 */     Request request = exchange.getRequest();
/* 226 */     if (LOG.isDebugEnabled())
/* 227 */       LOG.debug("Request begin {}", new Object[] { request });
/* 228 */     RequestNotifier notifier = getHttpChannel().getHttpDestination().getRequestNotifier();
/* 229 */     notifier.notifyBegin(request);
/*     */     
/* 231 */     if (updateRequestState(RequestState.TRANSIENT, RequestState.BEGIN)) {
/* 232 */       return true;
/*     */     }
/* 234 */     terminateRequest(exchange);
/* 235 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean beginToHeaders(HttpExchange exchange)
/*     */   {
/* 240 */     if (!updateRequestState(RequestState.BEGIN, RequestState.TRANSIENT)) {
/* 241 */       return false;
/*     */     }
/* 243 */     Request request = exchange.getRequest();
/* 244 */     if (LOG.isDebugEnabled())
/* 245 */       LOG.debug("Request headers {}{}{}", new Object[] { request, System.lineSeparator(), request.getHeaders().toString().trim() });
/* 246 */     RequestNotifier notifier = getHttpChannel().getHttpDestination().getRequestNotifier();
/* 247 */     notifier.notifyHeaders(request);
/*     */     
/* 249 */     if (updateRequestState(RequestState.TRANSIENT, RequestState.HEADERS)) {
/* 250 */       return true;
/*     */     }
/* 252 */     terminateRequest(exchange);
/* 253 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean headersToCommit(HttpExchange exchange)
/*     */   {
/* 258 */     if (!updateRequestState(RequestState.HEADERS, RequestState.TRANSIENT)) {
/* 259 */       return false;
/*     */     }
/* 261 */     Request request = exchange.getRequest();
/* 262 */     if (LOG.isDebugEnabled())
/* 263 */       LOG.debug("Request committed {}", new Object[] { request });
/* 264 */     RequestNotifier notifier = getHttpChannel().getHttpDestination().getRequestNotifier();
/* 265 */     notifier.notifyCommit(request);
/*     */     
/* 267 */     if (updateRequestState(RequestState.TRANSIENT, RequestState.COMMIT)) {
/* 268 */       return true;
/*     */     }
/* 270 */     terminateRequest(exchange);
/* 271 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean someToContent(HttpExchange exchange, ByteBuffer content)
/*     */   {
/* 276 */     RequestState current = (RequestState)this.requestState.get();
/* 277 */     switch (current)
/*     */     {
/*     */ 
/*     */     case COMMIT: 
/*     */     case CONTENT: 
/* 282 */       if (!updateRequestState(current, RequestState.TRANSIENT)) {
/* 283 */         return false;
/*     */       }
/* 285 */       Request request = exchange.getRequest();
/* 286 */       if (LOG.isDebugEnabled())
/* 287 */         LOG.debug("Request content {}{}{}", new Object[] { request, System.lineSeparator(), BufferUtil.toDetailString(content) });
/* 288 */       RequestNotifier notifier = getHttpChannel().getHttpDestination().getRequestNotifier();
/* 289 */       notifier.notifyContent(request, content);
/*     */       
/* 291 */       if (updateRequestState(RequestState.TRANSIENT, RequestState.CONTENT)) {
/* 292 */         return true;
/*     */       }
/* 294 */       terminateRequest(exchange);
/* 295 */       return false;
/*     */     }
/*     */     
/*     */     
/* 299 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean someToSuccess(HttpExchange exchange)
/*     */   {
/* 306 */     RequestState current = (RequestState)this.requestState.get();
/* 307 */     switch (current)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */     case COMMIT: 
/*     */     case CONTENT: 
/* 314 */       if (!exchange.requestComplete(null)) {
/* 315 */         return false;
/*     */       }
/* 317 */       this.requestState.set(RequestState.QUEUED);
/*     */       
/*     */ 
/* 320 */       reset();
/*     */       
/* 322 */       Request request = exchange.getRequest();
/* 323 */       if (LOG.isDebugEnabled())
/* 324 */         LOG.debug("Request success {}", new Object[] { request });
/* 325 */       HttpDestination destination = getHttpChannel().getHttpDestination();
/* 326 */       destination.getRequestNotifier().notifySuccess(exchange.getRequest());
/*     */       
/*     */ 
/*     */ 
/* 330 */       Result result = exchange.terminateRequest();
/* 331 */       terminateRequest(exchange, null, result);
/* 332 */       return true;
/*     */     }
/*     */     
/*     */     
/* 336 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean anyToFailure(Throwable failure)
/*     */   {
/* 343 */     HttpExchange exchange = getHttpExchange();
/* 344 */     if (exchange == null) {
/* 345 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 349 */     if (exchange.requestComplete(failure)) {
/* 350 */       return abort(exchange, failure);
/*     */     }
/* 352 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void terminateRequest(HttpExchange exchange)
/*     */   {
/* 359 */     Throwable failure = this.failure;
/* 360 */     if (failure == null)
/* 361 */       failure = new HttpRequestException("Concurrent failure", exchange.getRequest());
/* 362 */     Result result = exchange.terminateRequest();
/* 363 */     terminateRequest(exchange, failure, result);
/*     */   }
/*     */   
/*     */   private void terminateRequest(HttpExchange exchange, Throwable failure, Result result)
/*     */   {
/* 368 */     Request request = exchange.getRequest();
/*     */     
/* 370 */     if (LOG.isDebugEnabled()) {
/* 371 */       LOG.debug("Terminating request {}", new Object[] { request });
/*     */     }
/* 373 */     if (result == null)
/*     */     {
/* 375 */       if (failure != null)
/*     */       {
/* 377 */         if (exchange.responseComplete(failure))
/*     */         {
/* 379 */           if (LOG.isDebugEnabled())
/* 380 */             LOG.debug("Response failure from request {} {}", new Object[] { request, exchange });
/* 381 */           getHttpChannel().abortResponse(exchange, failure);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 387 */       result = this.channel.exchangeTerminating(exchange, result);
/* 388 */       HttpDestination destination = getHttpChannel().getHttpDestination();
/* 389 */       boolean ordered = destination.getHttpClient().isStrictEventOrdering();
/* 390 */       if (!ordered)
/* 391 */         this.channel.exchangeTerminated(exchange, result);
/* 392 */       if (LOG.isDebugEnabled())
/* 393 */         LOG.debug("Request/Response {}: {}", new Object[] { failure == null ? "succeeded" : "failed", result });
/* 394 */       HttpConversation conversation = exchange.getConversation();
/* 395 */       destination.getResponseNotifier().notifyComplete(conversation.getResponseListeners(), result);
/* 396 */       if (ordered) {
/* 397 */         this.channel.exchangeTerminated(exchange, result);
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
/*     */ 
/*     */ 
/*     */   protected abstract void sendHeaders(HttpExchange paramHttpExchange, HttpContent paramHttpContent, Callback paramCallback);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void sendContent(HttpExchange paramHttpExchange, HttpContent paramHttpContent, Callback paramCallback);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void sendTrailers(HttpExchange paramHttpExchange, Callback paramCallback);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 443 */     HttpContent content = this.content;
/* 444 */     this.content = null;
/* 445 */     content.close();
/* 446 */     this.senderState.set(SenderState.COMPLETED);
/*     */   }
/*     */   
/*     */   protected void dispose()
/*     */   {
/* 451 */     HttpContent content = this.content;
/* 452 */     this.content = null;
/* 453 */     if (content != null)
/* 454 */       content.close();
/* 455 */     this.senderState.set(SenderState.FAILED);
/*     */   }
/*     */   
/*     */   public void proceed(HttpExchange exchange, Throwable failure)
/*     */   {
/* 460 */     if (!expects100Continue(exchange.getRequest())) {
/* 461 */       return;
/*     */     }
/* 463 */     if (failure != null)
/*     */     {
/* 465 */       anyToFailure(failure);
/*     */     }
/*     */     else
/*     */     {
/*     */       for (;;)
/*     */       {
/* 471 */         SenderState current = (SenderState)this.senderState.get();
/* 472 */         switch (current)
/*     */         {
/*     */ 
/*     */ 
/*     */         case EXPECTING: 
/* 477 */           if (updateSenderState(current, SenderState.PROCEEDING))
/*     */           {
/* 479 */             if (LOG.isDebugEnabled()) {
/* 480 */               LOG.debug("Proceeding while expecting", new Object[0]);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */             return;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */           break;
/*     */         case EXPECTING_WITH_CONTENT: 
/* 493 */           if (updateSenderState(current, SenderState.PROCEEDING_WITH_CONTENT))
/*     */           {
/* 495 */             if (LOG.isDebugEnabled()) {
/* 496 */               LOG.debug("Proceeding while scheduled", new Object[0]);
/*     */             }
/*     */             
/*     */             return;
/*     */           }
/*     */           
/*     */           break;
/*     */         case WAITING: 
/* 504 */           if (updateSenderState(current, SenderState.SENDING))
/*     */           {
/* 506 */             if (LOG.isDebugEnabled())
/* 507 */               LOG.debug("Proceeding while waiting", new Object[0]);
/* 508 */             this.contentCallback.iterate(); return;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */           break;
/*     */         case FAILED: 
/* 515 */           return;
/*     */         case PROCEEDING: case SENDING_WITH_CONTENT: 
/*     */         case PROCEEDING_WITH_CONTENT: case COMPLETED: 
/*     */         default: 
/* 519 */           illegalSenderState(current); return;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean abort(HttpExchange exchange, Throwable failure)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 532 */       RequestState current = (RequestState)this.requestState.get();
/* 533 */       switch (current)
/*     */       {
/*     */ 
/*     */       case FAILURE: 
/* 537 */         return false;
/*     */       }
/*     */       
/*     */       
/* 541 */       if (updateRequestState(current, RequestState.FAILURE))
/*     */       {
/* 543 */         boolean terminate = current != RequestState.TRANSIENT;
/* 544 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     boolean terminate;
/*     */     
/* 551 */     this.failure = failure;
/*     */     
/* 553 */     dispose();
/*     */     
/* 555 */     Request request = exchange.getRequest();
/* 556 */     if (LOG.isDebugEnabled())
/* 557 */       LOG.debug("Request failure {} {} on {}: {}", new Object[] { request, exchange, getHttpChannel(), failure });
/* 558 */     HttpDestination destination = getHttpChannel().getHttpDestination();
/* 559 */     destination.getRequestNotifier().notifyFailure(request, failure);
/*     */     
/* 561 */     if (terminate)
/*     */     {
/*     */ 
/*     */ 
/* 565 */       Result result = exchange.terminateRequest();
/* 566 */       terminateRequest(exchange, failure, result);
/*     */ 
/*     */ 
/*     */     }
/* 570 */     else if (LOG.isDebugEnabled()) {
/* 571 */       LOG.debug("Concurrent failure: request termination skipped, performed by helpers", new Object[0]);
/*     */     }
/*     */     
/* 574 */     return true;
/*     */   }
/*     */   
/*     */   private boolean updateRequestState(RequestState from, RequestState to)
/*     */   {
/* 579 */     boolean updated = this.requestState.compareAndSet(from, to);
/* 580 */     if ((!updated) && (LOG.isDebugEnabled()))
/* 581 */       LOG.debug("RequestState update failed: {} -> {}: {}", new Object[] { from, to, this.requestState.get() });
/* 582 */     return updated;
/*     */   }
/*     */   
/*     */   private boolean updateSenderState(SenderState from, SenderState to)
/*     */   {
/* 587 */     boolean updated = this.senderState.compareAndSet(from, to);
/* 588 */     if ((!updated) && (LOG.isDebugEnabled()))
/* 589 */       LOG.debug("SenderState update failed: {} -> {}: {}", new Object[] { from, to, this.senderState.get() });
/* 590 */     return updated;
/*     */   }
/*     */   
/*     */   private void illegalSenderState(SenderState current)
/*     */   {
/* 595 */     anyToFailure(new IllegalStateException("Expected " + current + " found " + this.senderState.get() + " instead"));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 601 */     return String.format("%s@%x(req=%s,snd=%s,failure=%s)", new Object[] {
/* 602 */       getClass().getSimpleName(), 
/* 603 */       Integer.valueOf(hashCode()), this.requestState, this.senderState, this.failure });
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
/*     */   private static enum RequestState
/*     */   {
/* 617 */     TRANSIENT, 
/*     */     
/*     */ 
/*     */ 
/* 621 */     QUEUED, 
/*     */     
/*     */ 
/*     */ 
/* 625 */     BEGIN, 
/*     */     
/*     */ 
/*     */ 
/* 629 */     HEADERS, 
/*     */     
/*     */ 
/*     */ 
/* 633 */     COMMIT, 
/*     */     
/*     */ 
/*     */ 
/* 637 */     CONTENT, 
/*     */     
/*     */ 
/*     */ 
/* 641 */     FAILURE;
/*     */     
/*     */ 
/*     */ 
/*     */     private RequestState() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static enum SenderState
/*     */   {
/* 652 */     IDLE, 
/*     */     
/*     */ 
/*     */ 
/* 656 */     SENDING, 
/*     */     
/*     */ 
/*     */ 
/* 660 */     SENDING_WITH_CONTENT, 
/*     */     
/*     */ 
/*     */ 
/* 664 */     EXPECTING, 
/*     */     
/*     */ 
/*     */ 
/* 668 */     EXPECTING_WITH_CONTENT, 
/*     */     
/*     */ 
/*     */ 
/* 672 */     WAITING, 
/*     */     
/*     */ 
/*     */ 
/* 676 */     PROCEEDING, 
/*     */     
/*     */ 
/*     */ 
/* 680 */     PROCEEDING_WITH_CONTENT, 
/*     */     
/*     */ 
/*     */ 
/* 684 */     COMPLETED, 
/*     */     
/*     */ 
/*     */ 
/* 688 */     FAILED;
/*     */     
/*     */     private SenderState() {}
/*     */   }
/*     */   
/*     */   private class CommitCallback implements Callback {
/*     */     private CommitCallback() {}
/*     */     
/*     */     public void succeeded() {
/*     */       try {
/* 698 */         HttpContent content = HttpSender.this.content;
/* 699 */         if (content == null)
/* 700 */           return;
/* 701 */         content.succeeded();
/* 702 */         process();
/*     */       }
/*     */       catch (Throwable x)
/*     */       {
/* 706 */         HttpSender.this.anyToFailure(x);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable failure)
/*     */     {
/* 713 */       HttpContent content = HttpSender.this.content;
/* 714 */       if (content == null)
/* 715 */         return;
/* 716 */       content.failed(failure);
/* 717 */       HttpSender.this.anyToFailure(failure);
/*     */     }
/*     */     
/*     */     private void process() throws Exception
/*     */     {
/* 722 */       HttpExchange exchange = HttpSender.this.getHttpExchange();
/* 723 */       if (exchange == null) {
/* 724 */         return;
/*     */       }
/* 726 */       if (!HttpSender.this.headersToCommit(exchange)) {
/* 727 */         return;
/*     */       }
/* 729 */       HttpContent content = HttpSender.this.content;
/* 730 */       if (content == null) {
/* 731 */         return;
/*     */       }
/* 733 */       HttpRequest request = exchange.getRequest();
/* 734 */       Supplier<HttpFields> trailers = request.getTrailers();
/* 735 */       boolean hasContent = content.hasContent();
/* 736 */       if (!hasContent)
/*     */       {
/* 738 */         if (trailers == null)
/*     */         {
/*     */ 
/* 741 */           HttpSender.this.someToSuccess(exchange);
/*     */         }
/*     */         else
/*     */         {
/* 745 */           HttpSender.this.sendTrailers(exchange, HttpSender.this.lastCallback);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 751 */         ByteBuffer contentBuffer = content.getContent();
/* 752 */         if (contentBuffer != null)
/*     */         {
/* 754 */           if (!HttpSender.this.someToContent(exchange, contentBuffer)) {
/*     */             return;
/*     */           }
/*     */         }
/*     */         for (;;)
/*     */         {
/* 760 */           HttpSender.SenderState current = (HttpSender.SenderState)HttpSender.this.senderState.get();
/* 761 */           switch (HttpSender.1.$SwitchMap$org$eclipse$jetty$client$HttpSender$SenderState[current.ordinal()])
/*     */           {
/*     */ 
/*     */           case 2: 
/* 765 */             HttpSender.this.contentCallback.iterate();
/* 766 */             return;
/*     */           
/*     */ 
/*     */ 
/*     */           case 5: 
/* 771 */             HttpSender.this.updateSenderState(current, HttpSender.SenderState.SENDING);
/* 772 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case 3: 
/* 777 */             if (HttpSender.this.updateSenderState(current, HttpSender.SenderState.WAITING)) {
/*     */               return;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case 6: 
/* 785 */             if (HttpSender.this.updateSenderState(current, HttpSender.SenderState.WAITING)) {
/*     */               return;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case 4: 
/* 793 */             if (HttpSender.this.updateSenderState(current, HttpSender.SenderState.IDLE)) {
/*     */               return;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case 7: 
/* 801 */             HttpSender.this.updateSenderState(current, HttpSender.SenderState.SENDING);
/* 802 */             break;
/*     */           
/*     */ 
/*     */           case 10: 
/* 806 */             return;
/*     */           case 8: 
/*     */           case 9: 
/*     */           default: 
/* 810 */             HttpSender.this.illegalSenderState(current);
/* 811 */             return;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class ContentCallback extends IteratingCallback
/*     */   {
/*     */     private ContentCallback() {}
/*     */     
/*     */     protected IteratingCallback.Action process() throws Exception {
/* 824 */       HttpExchange exchange = HttpSender.this.getHttpExchange();
/* 825 */       if (exchange == null) {
/* 826 */         return IteratingCallback.Action.IDLE;
/*     */       }
/* 828 */       HttpContent content = HttpSender.this.content;
/* 829 */       if (content == null) {
/* 830 */         return IteratingCallback.Action.IDLE;
/*     */       }
/*     */       for (;;)
/*     */       {
/* 834 */         boolean advanced = content.advance();
/* 835 */         boolean lastContent = content.isLast();
/* 836 */         if (HttpSender.LOG.isDebugEnabled()) {
/* 837 */           HttpSender.LOG.debug("Content present {}, last {}, consumed {} for {}", new Object[] { Boolean.valueOf(advanced), Boolean.valueOf(lastContent), Boolean.valueOf(content.isConsumed()), exchange.getRequest() });
/*     */         }
/* 839 */         if (advanced)
/*     */         {
/* 841 */           HttpSender.this.sendContent(exchange, content, this);
/* 842 */           return IteratingCallback.Action.SCHEDULED;
/*     */         }
/*     */         
/* 845 */         if (lastContent)
/*     */         {
/* 847 */           HttpRequest request = exchange.getRequest();
/* 848 */           Supplier<HttpFields> trailers = request.getTrailers();
/* 849 */           HttpSender.this.sendContent(exchange, content, trailers == null ? HttpSender.this.lastCallback : HttpSender.this.trailersCallback);
/* 850 */           return IteratingCallback.Action.IDLE;
/*     */         }
/*     */         
/* 853 */         HttpSender.SenderState current = (HttpSender.SenderState)HttpSender.this.senderState.get();
/* 854 */         switch (HttpSender.1.$SwitchMap$org$eclipse$jetty$client$HttpSender$SenderState[current.ordinal()])
/*     */         {
/*     */ 
/*     */         case 2: 
/* 858 */           if (HttpSender.this.updateSenderState(current, HttpSender.SenderState.IDLE))
/*     */           {
/* 860 */             if (HttpSender.LOG.isDebugEnabled())
/* 861 */               HttpSender.LOG.debug("Content is deferred for {}", new Object[] { exchange.getRequest() });
/* 862 */             return IteratingCallback.Action.IDLE;
/*     */           }
/*     */           
/*     */ 
/*     */           break;
/*     */         case 5: 
/* 868 */           HttpSender.this.updateSenderState(current, HttpSender.SenderState.SENDING);
/* 869 */           break;
/*     */         
/*     */ 
/*     */         default: 
/* 873 */           HttpSender.this.illegalSenderState(current);
/* 874 */           return IteratingCallback.Action.IDLE;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 883 */       HttpExchange exchange = HttpSender.this.getHttpExchange();
/* 884 */       if (exchange == null)
/* 885 */         return;
/* 886 */       HttpContent content = HttpSender.this.content;
/* 887 */       if (content == null)
/* 888 */         return;
/* 889 */       content.succeeded();
/* 890 */       ByteBuffer buffer = content.getContent();
/* 891 */       HttpSender.this.someToContent(exchange, buffer);
/* 892 */       super.succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void onCompleteFailure(Throwable failure)
/*     */     {
/* 898 */       HttpContent content = HttpSender.this.content;
/* 899 */       if (content == null)
/* 900 */         return;
/* 901 */       content.failed(failure);
/* 902 */       HttpSender.this.anyToFailure(failure);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void onCompleteSuccess() {}
/*     */   }
/*     */   
/*     */ 
/*     */   private class TrailersCallback
/*     */     implements Callback
/*     */   {
/*     */     private TrailersCallback() {}
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 918 */       HttpExchange exchange = HttpSender.this.getHttpExchange();
/* 919 */       if (exchange == null)
/* 920 */         return;
/* 921 */       HttpSender.this.sendTrailers(exchange, HttpSender.this.lastCallback);
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 927 */       HttpContent content = HttpSender.this.content;
/* 928 */       if (content == null)
/* 929 */         return;
/* 930 */       content.failed(x);
/* 931 */       HttpSender.this.anyToFailure(x);
/*     */     }
/*     */   }
/*     */   
/*     */   private class LastCallback implements Callback
/*     */   {
/*     */     private LastCallback() {}
/*     */     
/*     */     public void succeeded() {
/* 940 */       HttpExchange exchange = HttpSender.this.getHttpExchange();
/* 941 */       if (exchange == null)
/* 942 */         return;
/* 943 */       HttpContent content = HttpSender.this.content;
/* 944 */       if (content == null)
/* 945 */         return;
/* 946 */       content.succeeded();
/* 947 */       HttpSender.this.someToSuccess(exchange);
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable failure)
/*     */     {
/* 953 */       HttpContent content = HttpSender.this.content;
/* 954 */       if (content == null)
/* 955 */         return;
/* 956 */       content.failed(failure);
/* 957 */       HttpSender.this.anyToFailure(failure);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */