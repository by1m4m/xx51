/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord;
/*     */ import io.netty.handler.codec.dns.DatagramDnsQuery;
/*     */ import io.netty.handler.codec.dns.DnsQuery;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsSection;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ final class DnsQueryContext
/*     */   implements FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DnsQueryContext.class);
/*     */   
/*     */   private final DnsNameResolver parent;
/*     */   
/*     */   private final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final DnsQuestion question;
/*     */   
/*     */   private final DnsRecord[] additionals;
/*     */   
/*     */   private final DnsRecord optResource;
/*     */   private final InetSocketAddress nameServerAddr;
/*     */   private final boolean recursionDesired;
/*     */   private volatile ScheduledFuture<?> timeoutFuture;
/*     */   
/*     */   DnsQueryContext(DnsNameResolver parent, InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise)
/*     */   {
/*  64 */     this.parent = ((DnsNameResolver)ObjectUtil.checkNotNull(parent, "parent"));
/*  65 */     this.nameServerAddr = ((InetSocketAddress)ObjectUtil.checkNotNull(nameServerAddr, "nameServerAddr"));
/*  66 */     this.question = ((DnsQuestion)ObjectUtil.checkNotNull(question, "question"));
/*  67 */     this.additionals = ((DnsRecord[])ObjectUtil.checkNotNull(additionals, "additionals"));
/*  68 */     this.promise = ((Promise)ObjectUtil.checkNotNull(promise, "promise"));
/*  69 */     this.recursionDesired = parent.isRecursionDesired();
/*  70 */     this.id = parent.queryContextManager.add(this);
/*     */     
/*     */ 
/*  73 */     promise.addListener(this);
/*     */     
/*  75 */     if (parent.isOptResourceEnabled()) {
/*  76 */       this.optResource = new AbstractDnsOptPseudoRrRecord(parent.maxPayloadSize(), 0, 0) {};
/*     */     }
/*     */     else
/*     */     {
/*  80 */       this.optResource = null;
/*     */     }
/*     */   }
/*     */   
/*     */   InetSocketAddress nameServerAddr() {
/*  85 */     return this.nameServerAddr;
/*     */   }
/*     */   
/*     */   DnsQuestion question() {
/*  89 */     return this.question;
/*     */   }
/*     */   
/*     */   void query(ChannelPromise writePromise) {
/*  93 */     DnsQuestion question = question();
/*  94 */     InetSocketAddress nameServerAddr = nameServerAddr();
/*  95 */     DatagramDnsQuery query = new DatagramDnsQuery(null, nameServerAddr, this.id);
/*     */     
/*  97 */     query.setRecursionDesired(this.recursionDesired);
/*     */     
/*  99 */     query.addRecord(DnsSection.QUESTION, question);
/*     */     
/* 101 */     for (DnsRecord record : this.additionals) {
/* 102 */       query.addRecord(DnsSection.ADDITIONAL, record);
/*     */     }
/*     */     
/* 105 */     if (this.optResource != null) {
/* 106 */       query.addRecord(DnsSection.ADDITIONAL, this.optResource);
/*     */     }
/*     */     
/* 109 */     if (logger.isDebugEnabled()) {
/* 110 */       logger.debug("{} WRITE: [{}: {}], {}", new Object[] { this.parent.ch, Integer.valueOf(this.id), nameServerAddr, question });
/*     */     }
/*     */     
/* 113 */     sendQuery(query, writePromise);
/*     */   }
/*     */   
/*     */   private void sendQuery(final DnsQuery query, final ChannelPromise writePromise) {
/* 117 */     if (this.parent.channelFuture.isDone()) {
/* 118 */       writeQuery(query, writePromise);
/*     */     } else {
/* 120 */       this.parent.channelFuture.addListener(new GenericFutureListener()
/*     */       {
/*     */         public void operationComplete(Future<? super Channel> future) {
/* 123 */           if (future.isSuccess()) {
/* 124 */             DnsQueryContext.this.writeQuery(query, writePromise);
/*     */           } else {
/* 126 */             Throwable cause = future.cause();
/* 127 */             DnsQueryContext.this.promise.tryFailure(cause);
/* 128 */             writePromise.setFailure(cause);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeQuery(DnsQuery query, ChannelPromise writePromise) {
/* 136 */     final ChannelFuture writeFuture = this.parent.ch.writeAndFlush(query, writePromise);
/* 137 */     if (writeFuture.isDone()) {
/* 138 */       onQueryWriteCompletion(writeFuture);
/*     */     } else {
/* 140 */       writeFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) {
/* 143 */           DnsQueryContext.this.onQueryWriteCompletion(writeFuture);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void onQueryWriteCompletion(ChannelFuture writeFuture) {
/* 150 */     if (!writeFuture.isSuccess()) {
/* 151 */       setFailure("failed to send a query", writeFuture.cause());
/* 152 */       return;
/*     */     }
/*     */     
/*     */ 
/* 156 */     final long queryTimeoutMillis = this.parent.queryTimeoutMillis();
/* 157 */     if (queryTimeoutMillis > 0L) {
/* 158 */       this.timeoutFuture = this.parent.ch.eventLoop().schedule(new Runnable()
/*     */       {
/*     */         public void run() {
/* 161 */           if (DnsQueryContext.this.promise.isDone())
/*     */           {
/* 163 */             return;
/*     */           }
/*     */           
/* 166 */           DnsQueryContext.this.setFailure("query timed out after " + queryTimeoutMillis + " milliseconds", null); } }, queryTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void finish(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope)
/*     */   {
/* 173 */     DnsResponse res = (DnsResponse)envelope.content();
/* 174 */     if (res.count(DnsSection.QUESTION) != 1) {
/* 175 */       logger.warn("Received a DNS response with invalid number of questions: {}", envelope);
/* 176 */       return;
/*     */     }
/*     */     
/* 179 */     if (!question().equals(res.recordAt(DnsSection.QUESTION))) {
/* 180 */       logger.warn("Received a mismatching DNS response: {}", envelope);
/* 181 */       return;
/*     */     }
/*     */     
/* 184 */     setSuccess(envelope);
/*     */   }
/*     */   
/*     */   private void setSuccess(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
/* 188 */     Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise = this.promise;
/*     */     
/*     */ 
/* 191 */     AddressedEnvelope<DnsResponse, InetSocketAddress> castResponse = envelope.retain();
/* 192 */     if (!promise.trySuccess(castResponse))
/*     */     {
/* 194 */       envelope.release();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setFailure(String message, Throwable cause) {
/* 199 */     InetSocketAddress nameServerAddr = nameServerAddr();
/*     */     
/* 201 */     StringBuilder buf = new StringBuilder(message.length() + 64);
/* 202 */     buf.append('[')
/* 203 */       .append(nameServerAddr)
/* 204 */       .append("] ")
/* 205 */       .append(message)
/* 206 */       .append(" (no stack trace available)");
/*     */     DnsNameResolverException e;
/*     */     DnsNameResolverException e;
/* 209 */     if (cause == null)
/*     */     {
/*     */ 
/* 212 */       e = new DnsNameResolverTimeoutException(nameServerAddr, question(), buf.toString());
/*     */     } else {
/* 214 */       e = new DnsNameResolverException(nameServerAddr, question(), buf.toString(), cause);
/*     */     }
/* 216 */     this.promise.tryFailure(e);
/*     */   }
/*     */   
/*     */ 
/*     */   public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future)
/*     */   {
/* 222 */     ScheduledFuture<?> timeoutFuture = this.timeoutFuture;
/* 223 */     if (timeoutFuture != null) {
/* 224 */       this.timeoutFuture = null;
/* 225 */       timeoutFuture.cancel(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 230 */     this.parent.queryContextManager.remove(this.nameServerAddr, this.id);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsQueryContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */