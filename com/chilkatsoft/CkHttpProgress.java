/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHttpProgress
/*     */   extends CkBaseProgress
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */ 
/*     */   protected CkHttpProgress(long var1, boolean var3)
/*     */   {
/*  12 */     super(chilkatJNI.CkHttpProgress_SWIGUpcast(var1), var3);
/*  13 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHttpProgress var0) {
/*  17 */     return var0 == null ? 0L : var0.swigCPtr;
/*     */   }
/*     */   
/*     */   protected void finalize() {
/*  21 */     delete();
/*     */   }
/*     */   
/*     */   public synchronized void delete() {
/*  25 */     if (this.swigCPtr != 0L) {
/*  26 */       if (this.swigCMemOwn) {
/*  27 */         this.swigCMemOwn = false;
/*  28 */         chilkatJNI.delete_CkHttpProgress(this.swigCPtr);
/*     */       }
/*     */       
/*  31 */       this.swigCPtr = 0L;
/*     */     }
/*     */     
/*  34 */     super.delete();
/*     */   }
/*     */   
/*     */   protected void swigDirectorDisconnect() {
/*  38 */     this.swigCMemOwn = false;
/*  39 */     delete();
/*     */   }
/*     */   
/*     */   public void swigReleaseOwnership() {
/*  43 */     this.swigCMemOwn = false;
/*  44 */     chilkatJNI.CkHttpProgress_change_ownership(this, this.swigCPtr, false);
/*     */   }
/*     */   
/*     */   public void swigTakeOwnership() {
/*  48 */     this.swigCMemOwn = true;
/*  49 */     chilkatJNI.CkHttpProgress_change_ownership(this, this.swigCPtr, true);
/*     */   }
/*     */   
/*     */   public CkHttpProgress() {
/*  53 */     this(chilkatJNI.new_CkHttpProgress(), true);
/*  54 */     chilkatJNI.CkHttpProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*     */   }
/*     */   
/*     */   public boolean HttpRedirect(String var1, String var2) {
/*  58 */     return getClass() == CkHttpProgress.class ? chilkatJNI.CkHttpProgress_HttpRedirect(this.swigCPtr, this, var1, var2) : chilkatJNI.CkHttpProgress_HttpRedirectSwigExplicitCkHttpProgress(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void HttpChunked() {
/*  62 */     if (getClass() == CkHttpProgress.class) {
/*  63 */       chilkatJNI.CkHttpProgress_HttpChunked(this.swigCPtr, this);
/*     */     } else {
/*  65 */       chilkatJNI.CkHttpProgress_HttpChunkedSwigExplicitCkHttpProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void HttpBeginReceive()
/*     */   {
/*  71 */     if (getClass() == CkHttpProgress.class) {
/*  72 */       chilkatJNI.CkHttpProgress_HttpBeginReceive(this.swigCPtr, this);
/*     */     } else {
/*  74 */       chilkatJNI.CkHttpProgress_HttpBeginReceiveSwigExplicitCkHttpProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void HttpEndReceive(boolean var1)
/*     */   {
/*  80 */     if (getClass() == CkHttpProgress.class) {
/*  81 */       chilkatJNI.CkHttpProgress_HttpEndReceive(this.swigCPtr, this, var1);
/*     */     } else {
/*  83 */       chilkatJNI.CkHttpProgress_HttpEndReceiveSwigExplicitCkHttpProgress(this.swigCPtr, this, var1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void HttpBeginSend()
/*     */   {
/*  89 */     if (getClass() == CkHttpProgress.class) {
/*  90 */       chilkatJNI.CkHttpProgress_HttpBeginSend(this.swigCPtr, this);
/*     */     } else {
/*  92 */       chilkatJNI.CkHttpProgress_HttpBeginSendSwigExplicitCkHttpProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void HttpEndSend(boolean var1)
/*     */   {
/*  98 */     if (getClass() == CkHttpProgress.class) {
/*  99 */       chilkatJNI.CkHttpProgress_HttpEndSend(this.swigCPtr, this, var1);
/*     */     } else {
/* 101 */       chilkatJNI.CkHttpProgress_HttpEndSendSwigExplicitCkHttpProgress(this.swigCPtr, this, var1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ReceiveRate(long var1, long var3)
/*     */   {
/* 107 */     if (getClass() == CkHttpProgress.class) {
/* 108 */       chilkatJNI.CkHttpProgress_ReceiveRate(this.swigCPtr, this, var1, var3);
/*     */     } else {
/* 110 */       chilkatJNI.CkHttpProgress_ReceiveRateSwigExplicitCkHttpProgress(this.swigCPtr, this, var1, var3);
/*     */     }
/*     */   }
/*     */   
/*     */   public void SendRate(long var1, long var3)
/*     */   {
/* 116 */     if (getClass() == CkHttpProgress.class) {
/* 117 */       chilkatJNI.CkHttpProgress_SendRate(this.swigCPtr, this, var1, var3);
/*     */     } else {
/* 119 */       chilkatJNI.CkHttpProgress_SendRateSwigExplicitCkHttpProgress(this.swigCPtr, this, var1, var3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHttpProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */