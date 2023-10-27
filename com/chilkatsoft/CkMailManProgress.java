/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkMailManProgress
/*    */   extends CkBaseProgress
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */ 
/*    */   protected CkMailManProgress(long var1, boolean var3)
/*    */   {
/* 12 */     super(chilkatJNI.CkMailManProgress_SWIGUpcast(var1), var3);
/* 13 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkMailManProgress var0) {
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
/* 28 */         chilkatJNI.delete_CkMailManProgress(this.swigCPtr);
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
/* 44 */     chilkatJNI.CkMailManProgress_change_ownership(this, this.swigCPtr, false);
/*    */   }
/*    */   
/*    */   public void swigTakeOwnership() {
/* 48 */     this.swigCMemOwn = true;
/* 49 */     chilkatJNI.CkMailManProgress_change_ownership(this, this.swigCPtr, true);
/*    */   }
/*    */   
/*    */   public CkMailManProgress() {
/* 53 */     this(chilkatJNI.new_CkMailManProgress(), true);
/* 54 */     chilkatJNI.CkMailManProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*    */   }
/*    */   
/*    */   public void EmailReceived(String var1, String var2, String var3, String var4, String var5, String var6, int var7) {
/* 58 */     if (getClass() == CkMailManProgress.class) {
/* 59 */       chilkatJNI.CkMailManProgress_EmailReceived(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/*    */     } else {
/* 61 */       chilkatJNI.CkMailManProgress_EmailReceivedSwigExplicitCkMailManProgress(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMailManProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */