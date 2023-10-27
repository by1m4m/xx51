/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.internal.tcnative.SessionTicketKey;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSessionContext;
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
/*     */ public abstract class OpenSslSessionContext
/*     */   implements SSLSessionContext
/*     */ {
/*  34 */   private static final Enumeration<byte[]> EMPTY = new EmptyEnumeration(null);
/*     */   
/*     */ 
/*     */ 
/*     */   private final OpenSslSessionStats stats;
/*     */   
/*     */ 
/*     */   private final OpenSslKeyMaterialProvider provider;
/*     */   
/*     */ 
/*     */   final ReferenceCountedOpenSslContext context;
/*     */   
/*     */ 
/*     */ 
/*     */   OpenSslSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider)
/*     */   {
/*  50 */     this.context = context;
/*  51 */     this.provider = provider;
/*  52 */     this.stats = new OpenSslSessionStats(context);
/*     */   }
/*     */   
/*     */   public SSLSession getSession(byte[] bytes)
/*     */   {
/*  57 */     if (bytes == null) {
/*  58 */       throw new NullPointerException("bytes");
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public Enumeration<byte[]> getIds()
/*     */   {
/*  65 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setTicketKeys(byte[] keys)
/*     */   {
/*  74 */     if (keys.length % 48 != 0) {
/*  75 */       throw new IllegalArgumentException("keys.length % 48 != 0");
/*     */     }
/*  77 */     SessionTicketKey[] tickets = new SessionTicketKey[keys.length / 48];
/*  78 */     int i = 0; for (int a = 0; i < tickets.length; i++) {
/*  79 */       byte[] name = Arrays.copyOfRange(keys, a, 16);
/*  80 */       a += 16;
/*  81 */       byte[] hmacKey = Arrays.copyOfRange(keys, a, 16);
/*  82 */       i += 16;
/*  83 */       byte[] aesKey = Arrays.copyOfRange(keys, a, 16);
/*  84 */       a += 16;
/*  85 */       tickets[i] = new SessionTicketKey(name, hmacKey, aesKey);
/*     */     }
/*  87 */     Lock writerLock = this.context.ctxLock.writeLock();
/*  88 */     writerLock.lock();
/*     */     try {
/*  90 */       SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
/*  91 */       SSLContext.setSessionTicketKeys(this.context.ctx, tickets);
/*     */     } finally {
/*  93 */       writerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTicketKeys(OpenSslSessionTicketKey... keys)
/*     */   {
/* 101 */     ObjectUtil.checkNotNull(keys, "keys");
/* 102 */     SessionTicketKey[] ticketKeys = new SessionTicketKey[keys.length];
/* 103 */     for (int i = 0; i < ticketKeys.length; i++) {
/* 104 */       ticketKeys[i] = keys[i].key;
/*     */     }
/* 106 */     Lock writerLock = this.context.ctxLock.writeLock();
/* 107 */     writerLock.lock();
/*     */     try {
/* 109 */       SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
/* 110 */       SSLContext.setSessionTicketKeys(this.context.ctx, ticketKeys);
/*     */     } finally {
/* 112 */       writerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void setSessionCacheEnabled(boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isSessionCacheEnabled();
/*     */   
/*     */ 
/*     */ 
/*     */   public OpenSslSessionStats stats()
/*     */   {
/* 130 */     return this.stats;
/*     */   }
/*     */   
/*     */   final void destroy() {
/* 134 */     if (this.provider != null) {
/* 135 */       this.provider.destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyEnumeration implements Enumeration<byte[]>
/*     */   {
/*     */     public boolean hasMoreElements() {
/* 142 */       return false;
/*     */     }
/*     */     
/*     */     public byte[] nextElement()
/*     */     {
/* 147 */       throw new NoSuchElementException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslSessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */