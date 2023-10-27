/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkSFtpDir
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */   protected transient boolean swigCMemOwn;
/*    */   
/*    */ 
/*    */   protected CkSFtpDir(long var1, boolean var3)
/*    */   {
/* 13 */     this.swigCMemOwn = var3;
/* 14 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkSFtpDir var0) {
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
/* 29 */         chilkatJNI.delete_CkSFtpDir(this.swigCPtr);
/*    */       }
/*    */       
/* 32 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */   
/*    */   public CkSFtpDir()
/*    */   {
/* 38 */     this(chilkatJNI.new_CkSFtpDir(), true);
/*    */   }
/*    */   
/*    */   public boolean get_LastMethodSuccess() {
/* 42 */     return chilkatJNI.CkSFtpDir_get_LastMethodSuccess(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void put_LastMethodSuccess(boolean var1) {
/* 46 */     chilkatJNI.CkSFtpDir_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public int get_NumFilesAndDirs() {
/* 50 */     return chilkatJNI.CkSFtpDir_get_NumFilesAndDirs(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void get_OriginalPath(CkString var1) {
/* 54 */     chilkatJNI.CkSFtpDir_get_OriginalPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String originalPath() {
/* 58 */     return chilkatJNI.CkSFtpDir_originalPath(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean GetFilename(int var1, CkString var2) {
/* 62 */     return chilkatJNI.CkSFtpDir_GetFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*    */   }
/*    */   
/*    */   public String getFilename(int var1) {
/* 66 */     return chilkatJNI.CkSFtpDir_getFilename(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public String filename(int var1) {
/* 70 */     return chilkatJNI.CkSFtpDir_filename(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public CkSFtpFile GetFileObject(int var1) {
/* 74 */     long var2 = chilkatJNI.CkSFtpDir_GetFileObject(this.swigCPtr, this, var1);
/* 75 */     return var2 == 0L ? null : new CkSFtpFile(var2, true);
/*    */   }
/*    */   
/*    */   public boolean LoadTaskResult(CkTask var1) {
/* 79 */     return chilkatJNI.CkSFtpDir_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSFtpDir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */