/*     */ package org.eclipse.jetty.client.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.LongAdder;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.client.HttpConnection;
/*     */ import org.eclipse.jetty.client.HttpDestination;
/*     */ import org.eclipse.jetty.client.HttpExchange;
/*     */ import org.eclipse.jetty.client.HttpRequest;
/*     */ import org.eclipse.jetty.client.SendFailure;
/*     */ import org.eclipse.jetty.client.api.Connection;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response.CompleteListener;
/*     */ import org.eclipse.jetty.io.AbstractConnection;
/*     */ import org.eclipse.jetty.io.Connection.UpgradeFrom;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.Promise;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Sweeper.Sweepable;
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
/*     */ public class HttpConnectionOverHTTP
/*     */   extends AbstractConnection
/*     */   implements Connection, Connection.UpgradeFrom, Sweeper.Sweepable
/*     */ {
/*  44 */   private static final Logger LOG = Log.getLogger(HttpConnectionOverHTTP.class);
/*     */   
/*  46 */   private final AtomicBoolean closed = new AtomicBoolean();
/*  47 */   private final AtomicInteger sweeps = new AtomicInteger();
/*     */   
/*     */   private final Promise<Connection> promise;
/*     */   private final Delegate delegate;
/*     */   private final HttpChannelOverHTTP channel;
/*     */   private long idleTimeout;
/*  53 */   private final LongAdder bytesIn = new LongAdder();
/*  54 */   private final LongAdder bytesOut = new LongAdder();
/*     */   
/*     */   public HttpConnectionOverHTTP(EndPoint endPoint, HttpDestination destination, Promise<Connection> promise)
/*     */   {
/*  58 */     super(endPoint, destination.getHttpClient().getExecutor());
/*  59 */     this.promise = promise;
/*  60 */     this.delegate = new Delegate(destination, null);
/*  61 */     this.channel = newHttpChannel();
/*     */   }
/*     */   
/*     */   protected HttpChannelOverHTTP newHttpChannel()
/*     */   {
/*  66 */     return new HttpChannelOverHTTP(this);
/*     */   }
/*     */   
/*     */   public HttpChannelOverHTTP getHttpChannel()
/*     */   {
/*  71 */     return this.channel;
/*     */   }
/*     */   
/*     */   public HttpDestinationOverHTTP getHttpDestination()
/*     */   {
/*  76 */     return (HttpDestinationOverHTTP)this.delegate.getHttpDestination();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getBytesIn()
/*     */   {
/*  82 */     return this.bytesIn.longValue();
/*     */   }
/*     */   
/*     */   protected void addBytesIn(long bytesIn)
/*     */   {
/*  87 */     this.bytesIn.add(bytesIn);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getBytesOut()
/*     */   {
/*  93 */     return this.bytesOut.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addBytesOut(long bytesOut)
/*     */   {
/*  99 */     this.bytesOut.add(bytesOut);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getMessagesIn()
/*     */   {
/* 105 */     return getHttpChannel().getMessagesIn();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getMessagesOut()
/*     */   {
/* 111 */     return getHttpChannel().getMessagesOut();
/*     */   }
/*     */   
/*     */ 
/*     */   public void send(Request request, Response.CompleteListener listener)
/*     */   {
/* 117 */     this.delegate.send(request, listener);
/*     */   }
/*     */   
/*     */   protected SendFailure send(HttpExchange exchange)
/*     */   {
/* 122 */     return this.delegate.send(exchange);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onOpen()
/*     */   {
/* 128 */     super.onOpen();
/* 129 */     fillInterested();
/* 130 */     this.promise.succeeded(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 136 */     return this.closed.get();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean onIdleExpired()
/*     */   {
/* 142 */     long idleTimeout = getEndPoint().getIdleTimeout();
/* 143 */     boolean close = this.delegate.onIdleTimeout(idleTimeout);
/* 144 */     if (close)
/* 145 */       close(new TimeoutException("Idle timeout " + idleTimeout + " ms"));
/* 146 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onFillable()
/*     */   {
/* 152 */     HttpExchange exchange = this.channel.getHttpExchange();
/* 153 */     if (exchange != null)
/*     */     {
/* 155 */       this.channel.receive();
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 161 */       close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer onUpgradeFrom()
/*     */   {
/* 168 */     HttpReceiverOverHTTP receiver = this.channel.getHttpReceiver();
/* 169 */     return receiver.onUpgradeFrom();
/*     */   }
/*     */   
/*     */ 
/*     */   public void release()
/*     */   {
/* 175 */     getEndPoint().setIdleTimeout(this.idleTimeout);
/* 176 */     getHttpDestination().release(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/* 182 */     close(new AsynchronousCloseException());
/*     */   }
/*     */   
/*     */   protected void close(Throwable failure)
/*     */   {
/* 187 */     if (this.closed.compareAndSet(false, true))
/*     */     {
/* 189 */       getHttpDestination().close(this);
/* 190 */       abort(failure);
/* 191 */       this.channel.destroy();
/* 192 */       getEndPoint().shutdownOutput();
/* 193 */       if (LOG.isDebugEnabled())
/* 194 */         LOG.debug("Shutdown {}", new Object[] { this });
/* 195 */       getEndPoint().close();
/* 196 */       if (LOG.isDebugEnabled()) {
/* 197 */         LOG.debug("Closed {}", new Object[] { this });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean abort(Throwable failure) {
/* 203 */     HttpExchange exchange = this.channel.getHttpExchange();
/* 204 */     return (exchange != null) && (exchange.getRequest().abort(failure));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean sweep()
/*     */   {
/* 210 */     if (!this.closed.get())
/* 211 */       return false;
/* 212 */     if (this.sweeps.incrementAndGet() < 4)
/* 213 */       return false;
/* 214 */     return true;
/*     */   }
/*     */   
/*     */   public void remove()
/*     */   {
/* 219 */     getHttpDestination().remove(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toConnectionString()
/*     */   {
/* 225 */     return String.format("%s@%x(l:%s <-> r:%s,closed=%b)=>%s", new Object[] {
/* 226 */       getClass().getSimpleName(), 
/* 227 */       Integer.valueOf(hashCode()), 
/* 228 */       getEndPoint().getLocalAddress(), 
/* 229 */       getEndPoint().getRemoteAddress(), 
/* 230 */       Boolean.valueOf(this.closed.get()), this.channel });
/*     */   }
/*     */   
/*     */   private class Delegate
/*     */     extends HttpConnection
/*     */   {
/*     */     private Delegate(HttpDestination destination)
/*     */     {
/* 238 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     protected SendFailure send(HttpExchange exchange)
/*     */     {
/* 244 */       Request request = exchange.getRequest();
/* 245 */       normalizeRequest(request);
/*     */       
/*     */ 
/* 248 */       EndPoint endPoint = HttpConnectionOverHTTP.this.getEndPoint();
/* 249 */       HttpConnectionOverHTTP.this.idleTimeout = endPoint.getIdleTimeout();
/* 250 */       endPoint.setIdleTimeout(request.getIdleTimeout());
/*     */       
/*     */ 
/* 253 */       return send(HttpConnectionOverHTTP.this.channel, exchange);
/*     */     }
/*     */     
/*     */ 
/*     */     public void close()
/*     */     {
/* 259 */       HttpConnectionOverHTTP.this.close();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isClosed()
/*     */     {
/* 265 */       return HttpConnectionOverHTTP.this.isClosed();
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 271 */       return HttpConnectionOverHTTP.this.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpConnectionOverHTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */