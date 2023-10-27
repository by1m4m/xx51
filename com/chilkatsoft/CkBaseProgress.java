/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkBaseProgress
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */   protected transient boolean swigCMemOwn;
/*    */   
/*    */ 
/*    */   protected CkBaseProgress(long var1, boolean var3)
/*    */   {
/* 13 */     this.swigCMemOwn = var3;
/* 14 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkBaseProgress var0) {
/* 18 */     return var0 == null ? 0L : var0.swigCPtr;
/*    */   }
/*    */   
/*    */   protected void finalize() {
/* 22 */     delete();
/*    */   }
/*    */   
/*    */   public synchronized void delete() {
/* 26 */     if (this.swigCPtr != 0L) {
/* 27 */       if (this.swigCMemOwn) {
/* 28 */         this.swigCMemOwn = false;
/* 29 */         chilkatJNI.delete_CkBaseProgress(this.swigCPtr);
/*    */       }
/*    */       
/* 32 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */   
/*    */   protected void swigDirectorDisconnect()
/*    */   {
/* 38 */     this.swigCMemOwn = false;
/* 39 */     delete();
/*    */   }
/*    */   
/*    */   public void swigReleaseOwnership() {
/* 43 */     this.swigCMemOwn = false;
/* 44 */     chilkatJNI.CkBaseProgress_change_ownership(this, this.swigCPtr, false);
/*    */   }
/*    */   
/*    */   public void swigTakeOwnership() {
/* 48 */     this.swigCMemOwn = true;
/* 49 */     chilkatJNI.CkBaseProgress_change_ownership(this, this.swigCPtr, true);
/*    */   }
/*    */   
/*    */   public CkBaseProgress() {
/* 53 */     this(chilkatJNI.new_CkBaseProgress(), true);
/* 54 */     chilkatJNI.CkBaseProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*    */   }
/*    */   
/*    */   public boolean AbortCheck() {
/* 58 */     return getClass() == CkBaseProgress.class ? chilkatJNI.CkBaseProgress_AbortCheck(this.swigCPtr, this) : chilkatJNI.CkBaseProgress_AbortCheckSwigExplicitCkBaseProgress(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean PercentDone(int var1) {
/* 62 */     return getClass() == CkBaseProgress.class ? chilkatJNI.CkBaseProgress_PercentDone(this.swigCPtr, this, var1) : chilkatJNI.CkBaseProgress_PercentDoneSwigExplicitCkBaseProgress(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public void ProgressInfo(String var1, String var2) {
/* 66 */     if (getClass() == CkBaseProgress.class) {
/* 67 */       chilkatJNI.CkBaseProgress_ProgressInfo(this.swigCPtr, this, var1, var2);
/*    */     } else {
/* 69 */       chilkatJNI.CkBaseProgress_ProgressInfoSwigExplicitCkBaseProgress(this.swigCPtr, this, var1, var2);
/*    */     }
/*    */   }
/*    */   
/*    */   public void TaskCompleted(CkTask var1)
/*    */   {
/* 75 */     if (getClass() == CkBaseProgress.class) {
/* 76 */       chilkatJNI.CkBaseProgress_TaskCompleted(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*    */     } else {
/* 78 */       chilkatJNI.CkBaseProgress_TaskCompletedSwigExplicitCkBaseProgress(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*    */     }
/*    */   }
/*    */   
/*    */   public void TextData(String var1)
/*    */   {
/* 84 */     if (getClass() == CkBaseProgress.class) {
/* 85 */       chilkatJNI.CkBaseProgress_TextData(this.swigCPtr, this, var1);
/*    */     } else {
/* 87 */       chilkatJNI.CkBaseProgress_TextDataSwigExplicitCkBaseProgress(this.swigCPtr, this, var1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkBaseProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */