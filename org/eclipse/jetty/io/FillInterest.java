/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ReadPendingException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Invocable;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*     */ public abstract class FillInterest
/*     */ {
/*  38 */   private static final Logger LOG = Log.getLogger(FillInterest.class);
/*  39 */   private final AtomicReference<Callback> _interested = new AtomicReference(null);
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
/*     */   public void register(Callback callback)
/*     */     throws ReadPendingException
/*     */   {
/*  55 */     if (!tryRegister(callback))
/*     */     {
/*  57 */       LOG.warn("Read pending for {} prevented {}", new Object[] { this._interested, callback });
/*  58 */       throw new ReadPendingException();
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
/*     */   public boolean tryRegister(Callback callback)
/*     */   {
/*  72 */     if (callback == null) {
/*  73 */       throw new IllegalArgumentException();
/*     */     }
/*  75 */     if (!this._interested.compareAndSet(null, callback)) {
/*  76 */       return false;
/*     */     }
/*  78 */     if (LOG.isDebugEnabled()) {
/*  79 */       LOG.debug("interested {}", new Object[] { this });
/*     */     }
/*     */     try
/*     */     {
/*  83 */       needsFillInterest();
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  87 */       onFail(e);
/*     */     }
/*     */     
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fillable()
/*     */   {
/*  98 */     if (LOG.isDebugEnabled())
/*  99 */       LOG.debug("fillable {}", new Object[] { this });
/* 100 */     Callback callback = (Callback)this._interested.get();
/* 101 */     if ((callback != null) && (this._interested.compareAndSet(callback, null))) {
/* 102 */       callback.succeeded();
/* 103 */     } else if (LOG.isDebugEnabled()) {
/* 104 */       LOG.debug("{} lost race {}", new Object[] { this, callback });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInterested()
/*     */   {
/* 112 */     return this._interested.get() != null;
/*     */   }
/*     */   
/*     */   public Invocable.InvocationType getCallbackInvocationType()
/*     */   {
/* 117 */     Callback callback = (Callback)this._interested.get();
/* 118 */     return Invocable.getInvocationType(callback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onFail(Throwable cause)
/*     */   {
/* 129 */     if (LOG.isDebugEnabled())
/* 130 */       LOG.debug("onFail " + this, cause);
/* 131 */     Callback callback = (Callback)this._interested.get();
/* 132 */     if ((callback != null) && (this._interested.compareAndSet(callback, null)))
/*     */     {
/* 134 */       callback.failed(cause);
/* 135 */       return true;
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public void onClose()
/*     */   {
/* 142 */     if (LOG.isDebugEnabled())
/* 143 */       LOG.debug("onClose {}", new Object[] { this });
/* 144 */     Callback callback = (Callback)this._interested.get();
/* 145 */     if ((callback != null) && (this._interested.compareAndSet(callback, null))) {
/* 146 */       callback.failed(new ClosedChannelException());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 152 */     return String.format("FillInterest@%x{%s}", new Object[] { Integer.valueOf(hashCode()), this._interested.get() });
/*     */   }
/*     */   
/*     */ 
/*     */   public String toStateString()
/*     */   {
/* 158 */     return this._interested.get() == null ? "-" : "FI";
/*     */   }
/*     */   
/*     */   protected abstract void needsFillInterest()
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\FillInterest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */