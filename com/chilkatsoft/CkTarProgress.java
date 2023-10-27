/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkTarProgress
/*    */   extends CkBaseProgress
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */ 
/*    */   protected CkTarProgress(long var1, boolean var3)
/*    */   {
/* 12 */     super(chilkatJNI.CkTarProgress_SWIGUpcast(var1), var3);
/* 13 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkTarProgress var0) {
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
/* 28 */         chilkatJNI.delete_CkTarProgress(this.swigCPtr);
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
/* 44 */     chilkatJNI.CkTarProgress_change_ownership(this, this.swigCPtr, false);
/*    */   }
/*    */   
/*    */   public void swigTakeOwnership() {
/* 48 */     this.swigCMemOwn = true;
/* 49 */     chilkatJNI.CkTarProgress_change_ownership(this, this.swigCPtr, true);
/*    */   }
/*    */   
/*    */   public CkTarProgress() {
/* 53 */     this(chilkatJNI.new_CkTarProgress(), true);
/* 54 */     chilkatJNI.CkTarProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*    */   }
/*    */   
/*    */   public boolean NextTarFile(String var1, long var2, boolean var4) {
/* 58 */     return getClass() == CkTarProgress.class ? chilkatJNI.CkTarProgress_NextTarFile(this.swigCPtr, this, var1, var2, var4) : chilkatJNI.CkTarProgress_NextTarFileSwigExplicitCkTarProgress(this.swigCPtr, this, var1, var2, var4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkTarProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */