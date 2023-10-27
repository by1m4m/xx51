/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
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
/*     */ public final class OpenSslServerSessionContext
/*     */   extends OpenSslSessionContext
/*     */ {
/*     */   OpenSslServerSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider)
/*     */   {
/*  29 */     super(context, provider);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setSessionTimeout(int seconds)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: ifge +11 -> 12
/*     */     //   4: new 2	java/lang/IllegalArgumentException
/*     */     //   7: dup
/*     */     //   8: invokespecial 3	java/lang/IllegalArgumentException:<init>	()V
/*     */     //   11: athrow
/*     */     //   12: aload_0
/*     */     //   13: getfield 4	io/netty/handler/ssl/OpenSslServerSessionContext:context	Lio/netty/handler/ssl/ReferenceCountedOpenSslContext;
/*     */     //   16: getfield 5	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctxLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   19: invokeinterface 6 1 0
/*     */     //   24: astore_2
/*     */     //   25: aload_2
/*     */     //   26: invokeinterface 7 1 0
/*     */     //   31: aload_0
/*     */     //   32: getfield 4	io/netty/handler/ssl/OpenSslServerSessionContext:context	Lio/netty/handler/ssl/ReferenceCountedOpenSslContext;
/*     */     //   35: getfield 8	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   38: iload_1
/*     */     //   39: i2l
/*     */     //   40: invokestatic 9	io/netty/internal/tcnative/SSLContext:setSessionCacheTimeout	(JJ)J
/*     */     //   43: pop2
/*     */     //   44: aload_2
/*     */     //   45: invokeinterface 10 1 0
/*     */     //   50: goto +12 -> 62
/*     */     //   53: astore_3
/*     */     //   54: aload_2
/*     */     //   55: invokeinterface 10 1 0
/*     */     //   60: aload_3
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #34	-> byte code offset #0
/*     */     //   Java source line #35	-> byte code offset #4
/*     */     //   Java source line #37	-> byte code offset #12
/*     */     //   Java source line #38	-> byte code offset #25
/*     */     //   Java source line #40	-> byte code offset #31
/*     */     //   Java source line #42	-> byte code offset #44
/*     */     //   Java source line #43	-> byte code offset #50
/*     */     //   Java source line #42	-> byte code offset #53
/*     */     //   Java source line #43	-> byte code offset #60
/*     */     //   Java source line #44	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	OpenSslServerSessionContext
/*     */     //   0	63	1	seconds	int
/*     */     //   24	31	2	writerLock	Lock
/*     */     //   53	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   31	44	53	finally
/*     */   }
/*     */   
/*     */   public int getSessionTimeout()
/*     */   {
/*  48 */     Lock readerLock = this.context.ctxLock.readLock();
/*  49 */     readerLock.lock();
/*     */     try {
/*  51 */       return (int)SSLContext.getSessionCacheTimeout(this.context.ctx);
/*     */     } finally {
/*  53 */       readerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setSessionCacheSize(int size)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: ifge +11 -> 12
/*     */     //   4: new 2	java/lang/IllegalArgumentException
/*     */     //   7: dup
/*     */     //   8: invokespecial 3	java/lang/IllegalArgumentException:<init>	()V
/*     */     //   11: athrow
/*     */     //   12: aload_0
/*     */     //   13: getfield 4	io/netty/handler/ssl/OpenSslServerSessionContext:context	Lio/netty/handler/ssl/ReferenceCountedOpenSslContext;
/*     */     //   16: getfield 5	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctxLock	Ljava/util/concurrent/locks/ReadWriteLock;
/*     */     //   19: invokeinterface 6 1 0
/*     */     //   24: astore_2
/*     */     //   25: aload_2
/*     */     //   26: invokeinterface 7 1 0
/*     */     //   31: aload_0
/*     */     //   32: getfield 4	io/netty/handler/ssl/OpenSslServerSessionContext:context	Lio/netty/handler/ssl/ReferenceCountedOpenSslContext;
/*     */     //   35: getfield 8	io/netty/handler/ssl/ReferenceCountedOpenSslContext:ctx	J
/*     */     //   38: iload_1
/*     */     //   39: i2l
/*     */     //   40: invokestatic 13	io/netty/internal/tcnative/SSLContext:setSessionCacheSize	(JJ)J
/*     */     //   43: pop2
/*     */     //   44: aload_2
/*     */     //   45: invokeinterface 10 1 0
/*     */     //   50: goto +12 -> 62
/*     */     //   53: astore_3
/*     */     //   54: aload_2
/*     */     //   55: invokeinterface 10 1 0
/*     */     //   60: aload_3
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #59	-> byte code offset #0
/*     */     //   Java source line #60	-> byte code offset #4
/*     */     //   Java source line #62	-> byte code offset #12
/*     */     //   Java source line #63	-> byte code offset #25
/*     */     //   Java source line #65	-> byte code offset #31
/*     */     //   Java source line #67	-> byte code offset #44
/*     */     //   Java source line #68	-> byte code offset #50
/*     */     //   Java source line #67	-> byte code offset #53
/*     */     //   Java source line #68	-> byte code offset #60
/*     */     //   Java source line #69	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	OpenSslServerSessionContext
/*     */     //   0	63	1	size	int
/*     */     //   24	31	2	writerLock	Lock
/*     */     //   53	8	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   31	44	53	finally
/*     */   }
/*     */   
/*     */   public int getSessionCacheSize()
/*     */   {
/*  73 */     Lock readerLock = this.context.ctxLock.readLock();
/*  74 */     readerLock.lock();
/*     */     try {
/*  76 */       return (int)SSLContext.getSessionCacheSize(this.context.ctx);
/*     */     } finally {
/*  78 */       readerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSessionCacheEnabled(boolean enabled)
/*     */   {
/*  84 */     long mode = enabled ? SSL.SSL_SESS_CACHE_SERVER : SSL.SSL_SESS_CACHE_OFF;
/*     */     
/*  86 */     Lock writerLock = this.context.ctxLock.writeLock();
/*  87 */     writerLock.lock();
/*     */     try {
/*  89 */       SSLContext.setSessionCacheMode(this.context.ctx, mode);
/*     */     } finally {
/*  91 */       writerLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSessionCacheEnabled()
/*     */   {
/*  97 */     Lock readerLock = this.context.ctxLock.readLock();
/*  98 */     readerLock.lock();
/*     */     try {
/* 100 */       return SSLContext.getSessionCacheMode(this.context.ctx) == SSL.SSL_SESS_CACHE_SERVER;
/*     */     } finally {
/* 102 */       readerLock.unlock();
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
/*     */   public boolean setSessionIdContext(byte[] sidCtx)
/*     */   {
/* 116 */     Lock writerLock = this.context.ctxLock.writeLock();
/* 117 */     writerLock.lock();
/*     */     try {
/* 119 */       return SSLContext.setSessionIdContext(this.context.ctx, sidCtx);
/*     */     } finally {
/* 121 */       writerLock.unlock();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslServerSessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */