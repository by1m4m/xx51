/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.eclipse.jetty.client.api.Request;
/*    */ import org.eclipse.jetty.client.api.Response.CompleteListener;
/*    */ import org.eclipse.jetty.client.api.Result;
/*    */ import org.eclipse.jetty.io.CyclicTimeout;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
/*    */ import org.eclipse.jetty.util.thread.Scheduler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeoutCompleteListener
/*    */   extends CyclicTimeout
/*    */   implements Response.CompleteListener
/*    */ {
/* 36 */   private static final Logger LOG = Log.getLogger(TimeoutCompleteListener.class);
/*    */   
/* 38 */   private final AtomicReference<Request> request = new AtomicReference();
/*    */   
/*    */   public TimeoutCompleteListener(Scheduler scheduler)
/*    */   {
/* 42 */     super(scheduler);
/*    */   }
/*    */   
/*    */ 
/*    */   public void onTimeoutExpired()
/*    */   {
/* 48 */     Request request = (Request)this.request.getAndSet(null);
/* 49 */     if (LOG.isDebugEnabled())
/* 50 */       LOG.debug("Total timeout {} ms elapsed for {}", new Object[] { Long.valueOf(request.getTimeout()), request });
/* 51 */     if (request != null) {
/* 52 */       request.abort(new TimeoutException("Total timeout " + request.getTimeout() + " ms elapsed"));
/*    */     }
/*    */   }
/*    */   
/*    */   public void onComplete(Result result)
/*    */   {
/* 58 */     Request request = (Request)this.request.getAndSet(null);
/* 59 */     if (request != null)
/*    */     {
/* 61 */       boolean cancelled = cancel();
/* 62 */       if (LOG.isDebugEnabled()) {
/* 63 */         LOG.debug("Cancelled ({}) timeout for {}", new Object[] { Boolean.valueOf(cancelled), request });
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   void schedule(HttpRequest request, long timeoutAt) {
/* 69 */     if (this.request.compareAndSet(null, request))
/*    */     {
/* 71 */       long delay = timeoutAt - System.nanoTime();
/* 72 */       if (LOG.isDebugEnabled())
/* 73 */         LOG.debug("Scheduled timeout in {} ms for {}", new Object[] { Long.valueOf(TimeUnit.NANOSECONDS.toMillis(delay)), request });
/* 74 */       if (delay <= 0L) {
/* 75 */         onTimeoutExpired();
/*    */       } else {
/* 77 */         schedule(delay, TimeUnit.NANOSECONDS);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\TimeoutCompleteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */