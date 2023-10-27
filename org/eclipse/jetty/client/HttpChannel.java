/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.Result;
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
/*     */ public abstract class HttpChannel
/*     */ {
/*  27 */   protected static final Logger LOG = Log.getLogger(HttpChannel.class);
/*     */   
/*     */   private final HttpDestination _destination;
/*     */   private final TimeoutCompleteListener _totalTimeout;
/*     */   private HttpExchange _exchange;
/*     */   
/*     */   protected HttpChannel(HttpDestination destination)
/*     */   {
/*  35 */     this._destination = destination;
/*  36 */     this._totalTimeout = new TimeoutCompleteListener(destination.getHttpClient().getScheduler());
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */   {
/*  41 */     this._totalTimeout.destroy();
/*     */   }
/*     */   
/*     */   public HttpDestination getHttpDestination()
/*     */   {
/*  46 */     return this._destination;
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
/*     */   public boolean associate(HttpExchange exchange)
/*     */   {
/*  59 */     boolean result = false;
/*  60 */     boolean abort = true;
/*  61 */     synchronized (this)
/*     */     {
/*  63 */       if (this._exchange == null)
/*     */       {
/*  65 */         abort = false;
/*  66 */         result = exchange.associate(this);
/*  67 */         if (result) {
/*  68 */           this._exchange = exchange;
/*     */         }
/*     */       }
/*     */     }
/*  72 */     if (abort) {
/*  73 */       exchange.getRequest().abort(new UnsupportedOperationException("Pipelined requests not supported"));
/*     */     }
/*  75 */     if (LOG.isDebugEnabled()) {
/*  76 */       LOG.debug("{} associated {} to {}", new Object[] { exchange, Boolean.valueOf(result), this });
/*     */     }
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   public boolean disassociate(HttpExchange exchange)
/*     */   {
/*  83 */     boolean result = false;
/*  84 */     synchronized (this)
/*     */     {
/*  86 */       HttpExchange existing = this._exchange;
/*  87 */       this._exchange = null;
/*  88 */       if (existing == exchange)
/*     */       {
/*  90 */         existing.disassociate(this);
/*  91 */         result = true;
/*     */       }
/*     */     }
/*     */     
/*  95 */     if (LOG.isDebugEnabled())
/*  96 */       LOG.debug("{} disassociated {} from {}", new Object[] { exchange, Boolean.valueOf(result), this });
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public HttpExchange getHttpExchange()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 9	org/eclipse/jetty/client/HttpChannel:_exchange	Lorg/eclipse/jetty/client/HttpExchange;
/*     */     //   8: aload_1
/*     */     //   9: monitorexit
/*     */     //   10: areturn
/*     */     //   11: astore_2
/*     */     //   12: aload_1
/*     */     //   13: monitorexit
/*     */     //   14: aload_2
/*     */     //   15: athrow
/*     */     // Line number table:
/*     */     //   Java source line #102	-> byte code offset #0
/*     */     //   Java source line #104	-> byte code offset #4
/*     */     //   Java source line #105	-> byte code offset #11
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	16	0	this	HttpChannel
/*     */     //   2	11	1	Ljava/lang/Object;	Object
/*     */     //   11	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	10	11	finally
/*     */     //   11	14	11	finally
/*     */   }
/*     */   
/*     */   protected abstract HttpSender getHttpSender();
/*     */   
/*     */   protected abstract HttpReceiver getHttpReceiver();
/*     */   
/*     */   public void send()
/*     */   {
/* 114 */     HttpExchange exchange = getHttpExchange();
/* 115 */     if (exchange != null)
/*     */     {
/* 117 */       HttpRequest request = exchange.getRequest();
/* 118 */       long timeoutAt = request.getTimeoutAt();
/* 119 */       if (timeoutAt != -1L)
/*     */       {
/* 121 */         exchange.getResponseListeners().add(this._totalTimeout);
/* 122 */         this._totalTimeout.schedule(request, timeoutAt);
/*     */       }
/* 124 */       send(exchange);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void send(HttpExchange paramHttpExchange);
/*     */   
/*     */   public abstract void release();
/*     */   
/*     */   public void proceed(HttpExchange exchange, Throwable failure)
/*     */   {
/* 134 */     getHttpSender().proceed(exchange, failure);
/*     */   }
/*     */   
/*     */   public boolean abort(HttpExchange exchange, Throwable requestFailure, Throwable responseFailure)
/*     */   {
/* 139 */     boolean requestAborted = false;
/* 140 */     if (requestFailure != null) {
/* 141 */       requestAborted = getHttpSender().abort(exchange, requestFailure);
/*     */     }
/* 143 */     boolean responseAborted = false;
/* 144 */     if (responseFailure != null) {
/* 145 */       responseAborted = abortResponse(exchange, responseFailure);
/*     */     }
/* 147 */     return (requestAborted) || (responseAborted);
/*     */   }
/*     */   
/*     */   public boolean abortResponse(HttpExchange exchange, Throwable failure)
/*     */   {
/* 152 */     return getHttpReceiver().abort(exchange, failure);
/*     */   }
/*     */   
/*     */   public Result exchangeTerminating(HttpExchange exchange, Result result)
/*     */   {
/* 157 */     return result;
/*     */   }
/*     */   
/*     */   public void exchangeTerminated(HttpExchange exchange, Result result)
/*     */   {
/* 162 */     disassociate(exchange);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     return String.format("%s@%x(exchange=%s)", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), getHttpExchange() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */