/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkFtp2Progress
/*     */   extends CkBaseProgress
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */ 
/*     */   protected CkFtp2Progress(long var1, boolean var3)
/*     */   {
/*  12 */     super(chilkatJNI.CkFtp2Progress_SWIGUpcast(var1), var3);
/*  13 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkFtp2Progress var0) {
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
/*  28 */         chilkatJNI.delete_CkFtp2Progress(this.swigCPtr);
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
/*  44 */     chilkatJNI.CkFtp2Progress_change_ownership(this, this.swigCPtr, false);
/*     */   }
/*     */   
/*     */   public void swigTakeOwnership() {
/*  48 */     this.swigCMemOwn = true;
/*  49 */     chilkatJNI.CkFtp2Progress_change_ownership(this, this.swigCPtr, true);
/*     */   }
/*     */   
/*     */   public CkFtp2Progress() {
/*  53 */     this(chilkatJNI.new_CkFtp2Progress(), true);
/*  54 */     chilkatJNI.CkFtp2Progress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*     */   }
/*     */   
/*     */   public boolean BeginDownloadFile(String var1) {
/*  58 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_BeginDownloadFile(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_BeginDownloadFileSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyDownloadDir(String var1) {
/*  62 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_VerifyDownloadDir(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_VerifyDownloadDirSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean BeginUploadFile(String var1) {
/*  66 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_BeginUploadFile(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_BeginUploadFileSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyUploadDir(String var1) {
/*  70 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_VerifyUploadDir(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_VerifyUploadDirSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyDeleteDir(String var1) {
/*  74 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_VerifyDeleteDir(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_VerifyDeleteDirSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyDeleteFile(String var1) {
/*  78 */     return getClass() == CkFtp2Progress.class ? chilkatJNI.CkFtp2Progress_VerifyDeleteFile(this.swigCPtr, this, var1) : chilkatJNI.CkFtp2Progress_VerifyDeleteFileSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void EndUploadFile(String var1, long var2) {
/*  82 */     if (getClass() == CkFtp2Progress.class) {
/*  83 */       chilkatJNI.CkFtp2Progress_EndUploadFile(this.swigCPtr, this, var1, var2);
/*     */     } else {
/*  85 */       chilkatJNI.CkFtp2Progress_EndUploadFileSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1, var2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void EndDownloadFile(String var1, long var2)
/*     */   {
/*  91 */     if (getClass() == CkFtp2Progress.class) {
/*  92 */       chilkatJNI.CkFtp2Progress_EndDownloadFile(this.swigCPtr, this, var1, var2);
/*     */     } else {
/*  94 */       chilkatJNI.CkFtp2Progress_EndDownloadFileSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1, var2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void UploadRate(long var1, long var3)
/*     */   {
/* 100 */     if (getClass() == CkFtp2Progress.class) {
/* 101 */       chilkatJNI.CkFtp2Progress_UploadRate(this.swigCPtr, this, var1, var3);
/*     */     } else {
/* 103 */       chilkatJNI.CkFtp2Progress_UploadRateSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1, var3);
/*     */     }
/*     */   }
/*     */   
/*     */   public void DownloadRate(long var1, long var3)
/*     */   {
/* 109 */     if (getClass() == CkFtp2Progress.class) {
/* 110 */       chilkatJNI.CkFtp2Progress_DownloadRate(this.swigCPtr, this, var1, var3);
/*     */     } else {
/* 112 */       chilkatJNI.CkFtp2Progress_DownloadRateSwigExplicitCkFtp2Progress(this.swigCPtr, this, var1, var3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkFtp2Progress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */