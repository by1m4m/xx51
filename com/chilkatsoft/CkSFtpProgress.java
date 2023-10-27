/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkSFtpProgress
/*    */   extends CkBaseProgress
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */ 
/*    */   protected CkSFtpProgress(long var1, boolean var3)
/*    */   {
/* 12 */     super(chilkatJNI.CkSFtpProgress_SWIGUpcast(var1), var3);
/* 13 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkSFtpProgress var0) {
/* 17 */     return var0 == null ? 0L : var0.swigCPtr;
/*    */   }
/*    */   
/*    */   protected void finalize() {
/* 21 */     delete();
/*    */   }
/*    */   
/*    */   public synchronized void delete() {
/* 25 */     if (this.swigCPtr != 0L) {
/* 26 */       if (this.swigCMemOwn) {
/* 27 */         this.swigCMemOwn = false;
/* 28 */         chilkatJNI.delete_CkSFtpProgress(this.swigCPtr);
/*    */       }
/*    */       
/* 31 */       this.swigCPtr = 0L;
/*    */     }
/*    */     
/* 34 */     super.delete();
/*    */   }
/*    */   
/*    */   protected void swigDirectorDisconnect() {
/* 38 */     this.swigCMemOwn = false;
/* 39 */     delete();
/*    */   }
/*    */   
/*    */   public void swigReleaseOwnership() {
/* 43 */     this.swigCMemOwn = false;
/* 44 */     chilkatJNI.CkSFtpProgress_change_ownership(this, this.swigCPtr, false);
/*    */   }
/*    */   
/*    */   public void swigTakeOwnership() {
/* 48 */     this.swigCMemOwn = true;
/* 49 */     chilkatJNI.CkSFtpProgress_change_ownership(this, this.swigCPtr, true);
/*    */   }
/*    */   
/*    */   public CkSFtpProgress() {
/* 53 */     this(chilkatJNI.new_CkSFtpProgress(), true);
/* 54 */     chilkatJNI.CkSFtpProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*    */   }
/*    */   
/*    */   public void UploadRate(long var1, long var3) {
/* 58 */     if (getClass() == CkSFtpProgress.class) {
/* 59 */       chilkatJNI.CkSFtpProgress_UploadRate(this.swigCPtr, this, var1, var3);
/*    */     } else {
/* 61 */       chilkatJNI.CkSFtpProgress_UploadRateSwigExplicitCkSFtpProgress(this.swigCPtr, this, var1, var3);
/*    */     }
/*    */   }
/*    */   
/*    */   public void DownloadRate(long var1, long var3)
/*    */   {
/* 67 */     if (getClass() == CkSFtpProgress.class) {
/* 68 */       chilkatJNI.CkSFtpProgress_DownloadRate(this.swigCPtr, this, var1, var3);
/*    */     } else {
/* 70 */       chilkatJNI.CkSFtpProgress_DownloadRateSwigExplicitCkSFtpProgress(this.swigCPtr, this, var1, var3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSFtpProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */