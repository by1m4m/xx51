/*     */ package org.eclipse.jetty.client.http;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.atomic.LongAdder;
/*     */ import org.eclipse.jetty.client.HttpChannel;
/*     */ import org.eclipse.jetty.client.HttpExchange;
/*     */ import org.eclipse.jetty.client.HttpRequest;
/*     */ import org.eclipse.jetty.client.HttpResponse;
/*     */ import org.eclipse.jetty.client.HttpResponseException;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.http.HttpHeaderValue;
/*     */ import org.eclipse.jetty.http.HttpMethod;
/*     */ import org.eclipse.jetty.http.HttpVersion;
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
/*     */ public class HttpChannelOverHTTP
/*     */   extends HttpChannel
/*     */ {
/*     */   private final HttpConnectionOverHTTP connection;
/*     */   private final HttpSenderOverHTTP sender;
/*     */   private final HttpReceiverOverHTTP receiver;
/*  43 */   private final LongAdder inMessages = new LongAdder();
/*  44 */   private final LongAdder outMessages = new LongAdder();
/*     */   
/*     */   public HttpChannelOverHTTP(HttpConnectionOverHTTP connection)
/*     */   {
/*  48 */     super(connection.getHttpDestination());
/*  49 */     this.connection = connection;
/*  50 */     this.sender = newHttpSender();
/*  51 */     this.receiver = newHttpReceiver();
/*     */   }
/*     */   
/*     */   protected HttpSenderOverHTTP newHttpSender()
/*     */   {
/*  56 */     return new HttpSenderOverHTTP(this);
/*     */   }
/*     */   
/*     */   protected HttpReceiverOverHTTP newHttpReceiver()
/*     */   {
/*  61 */     return new HttpReceiverOverHTTP(this);
/*     */   }
/*     */   
/*     */ 
/*     */   protected HttpSenderOverHTTP getHttpSender()
/*     */   {
/*  67 */     return this.sender;
/*     */   }
/*     */   
/*     */ 
/*     */   protected HttpReceiverOverHTTP getHttpReceiver()
/*     */   {
/*  73 */     return this.receiver;
/*     */   }
/*     */   
/*     */   public HttpConnectionOverHTTP getHttpConnection()
/*     */   {
/*  78 */     return this.connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public void send(HttpExchange exchange)
/*     */   {
/*  84 */     this.outMessages.increment();
/*  85 */     this.sender.send(exchange);
/*     */   }
/*     */   
/*     */ 
/*     */   public void release()
/*     */   {
/*  91 */     this.connection.release();
/*     */   }
/*     */   
/*     */ 
/*     */   public Result exchangeTerminating(HttpExchange exchange, Result result)
/*     */   {
/*  97 */     if (result.isFailed()) {
/*  98 */       return result;
/*     */     }
/* 100 */     HttpResponse response = exchange.getResponse();
/*     */     
/* 102 */     if ((response.getVersion() == HttpVersion.HTTP_1_1) && 
/* 103 */       (response.getStatus() == 101))
/*     */     {
/* 105 */       String connection = response.getHeaders().get(HttpHeader.CONNECTION);
/* 106 */       if ((connection == null) || (!connection.toLowerCase(Locale.US).contains("upgrade")))
/*     */       {
/* 108 */         return new Result(result, new HttpResponseException("101 Switching Protocols without Connection: Upgrade not supported", response));
/*     */       }
/*     */       
/*     */ 
/* 112 */       HttpRequest request = exchange.getRequest();
/* 113 */       if ((request instanceof HttpConnectionUpgrader))
/*     */       {
/* 115 */         HttpConnectionUpgrader listener = (HttpConnectionUpgrader)request;
/*     */         try
/*     */         {
/* 118 */           listener.upgrade(response, getHttpConnection());
/*     */         }
/*     */         catch (Throwable x)
/*     */         {
/* 122 */           return new Result(result, x);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public void receive()
/*     */   {
/* 132 */     this.inMessages.increment();
/* 133 */     this.receiver.receive();
/*     */   }
/*     */   
/*     */ 
/*     */   public void exchangeTerminated(HttpExchange exchange, Result result)
/*     */   {
/* 139 */     super.exchangeTerminated(exchange, result);
/*     */     
/* 141 */     Response response = result.getResponse();
/* 142 */     HttpFields responseHeaders = response.getHeaders();
/*     */     
/* 144 */     String closeReason = null;
/* 145 */     if (result.isFailed()) {
/* 146 */       closeReason = "failure";
/* 147 */     } else if (this.receiver.isShutdown()) {
/* 148 */       closeReason = "server close";
/* 149 */     } else if (this.sender.isShutdown()) {
/* 150 */       closeReason = "client close";
/*     */     }
/* 152 */     if (closeReason == null)
/*     */     {
/* 154 */       if (response.getVersion().compareTo(HttpVersion.HTTP_1_1) < 0)
/*     */       {
/*     */ 
/*     */ 
/* 158 */         boolean keepAlive = responseHeaders.contains(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.asString());
/* 159 */         boolean connect = HttpMethod.CONNECT.is(exchange.getRequest().getMethod());
/* 160 */         if ((!keepAlive) && (!connect)) {
/* 161 */           closeReason = "http/1.0";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 166 */       else if (responseHeaders.contains(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString())) {
/* 167 */         closeReason = "http/1.1";
/*     */       }
/*     */     }
/*     */     
/* 171 */     if (closeReason != null)
/*     */     {
/* 173 */       if (LOG.isDebugEnabled())
/* 174 */         LOG.debug("Closing, reason: {} - {}", new Object[] { closeReason, this.connection });
/* 175 */       this.connection.close();
/*     */ 
/*     */ 
/*     */     }
/* 179 */     else if (response.getStatus() == 101) {
/* 180 */       this.connection.remove();
/*     */     } else {
/* 182 */       release();
/*     */     }
/*     */   }
/*     */   
/*     */   protected long getMessagesIn()
/*     */   {
/* 188 */     return this.inMessages.longValue();
/*     */   }
/*     */   
/*     */   protected long getMessagesOut()
/*     */   {
/* 193 */     return this.outMessages.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 199 */     return String.format("%s[send=%s,recv=%s]", new Object[] {
/* 200 */       super.toString(), this.sender, this.receiver });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpChannelOverHTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */