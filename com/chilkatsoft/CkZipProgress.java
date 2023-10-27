/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkZipProgress
/*     */   extends CkBaseProgress
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */ 
/*     */   protected CkZipProgress(long var1, boolean var3)
/*     */   {
/*  12 */     super(chilkatJNI.CkZipProgress_SWIGUpcast(var1), var3);
/*  13 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkZipProgress var0) {
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
/*  28 */         chilkatJNI.delete_CkZipProgress(this.swigCPtr);
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
/*  44 */     chilkatJNI.CkZipProgress_change_ownership(this, this.swigCPtr, false);
/*     */   }
/*     */   
/*     */   public void swigTakeOwnership() {
/*  48 */     this.swigCMemOwn = true;
/*  49 */     chilkatJNI.CkZipProgress_change_ownership(this, this.swigCPtr, true);
/*     */   }
/*     */   
/*     */   public CkZipProgress() {
/*  53 */     this(chilkatJNI.new_CkZipProgress(), true);
/*  54 */     chilkatJNI.CkZipProgress_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
/*     */   }
/*     */   
/*     */   public boolean DirToBeAdded(String var1) {
/*  58 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_DirToBeAdded(this.swigCPtr, this, var1) : chilkatJNI.CkZipProgress_DirToBeAddedSwigExplicitCkZipProgress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToBeAdded(String var1, long var2) {
/*  62 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_ToBeAdded(this.swigCPtr, this, var1, var2) : chilkatJNI.CkZipProgress_ToBeAddedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FileAdded(String var1, long var2) {
/*  66 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_FileAdded(this.swigCPtr, this, var1, var2) : chilkatJNI.CkZipProgress_FileAddedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToBeZipped(String var1, long var2) {
/*  70 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_ToBeZipped(this.swigCPtr, this, var1, var2) : chilkatJNI.CkZipProgress_ToBeZippedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FileZipped(String var1, long var2, long var4) {
/*  74 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_FileZipped(this.swigCPtr, this, var1, var2, var4) : chilkatJNI.CkZipProgress_FileZippedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2, var4);
/*     */   }
/*     */   
/*     */   public boolean ToBeUnzipped(String var1, long var2, long var4, boolean var6) {
/*  78 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_ToBeUnzipped(this.swigCPtr, this, var1, var2, var4, var6) : chilkatJNI.CkZipProgress_ToBeUnzippedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2, var4, var6);
/*     */   }
/*     */   
/*     */   public boolean FileUnzipped(String var1, long var2, long var4, boolean var6) {
/*  82 */     return getClass() == CkZipProgress.class ? chilkatJNI.CkZipProgress_FileUnzipped(this.swigCPtr, this, var1, var2, var4, var6) : chilkatJNI.CkZipProgress_FileUnzippedSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2, var4, var6);
/*     */   }
/*     */   
/*     */   public void SkippedForUnzip(String var1, long var2, long var4, boolean var6) {
/*  86 */     if (getClass() == CkZipProgress.class) {
/*  87 */       chilkatJNI.CkZipProgress_SkippedForUnzip(this.swigCPtr, this, var1, var2, var4, var6);
/*     */     } else {
/*  89 */       chilkatJNI.CkZipProgress_SkippedForUnzipSwigExplicitCkZipProgress(this.swigCPtr, this, var1, var2, var4, var6);
/*     */     }
/*     */   }
/*     */   
/*     */   public void AddFilesBegin()
/*     */   {
/*  95 */     if (getClass() == CkZipProgress.class) {
/*  96 */       chilkatJNI.CkZipProgress_AddFilesBegin(this.swigCPtr, this);
/*     */     } else {
/*  98 */       chilkatJNI.CkZipProgress_AddFilesBeginSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void AddFilesEnd()
/*     */   {
/* 104 */     if (getClass() == CkZipProgress.class) {
/* 105 */       chilkatJNI.CkZipProgress_AddFilesEnd(this.swigCPtr, this);
/*     */     } else {
/* 107 */       chilkatJNI.CkZipProgress_AddFilesEndSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void WriteZipBegin()
/*     */   {
/* 113 */     if (getClass() == CkZipProgress.class) {
/* 114 */       chilkatJNI.CkZipProgress_WriteZipBegin(this.swigCPtr, this);
/*     */     } else {
/* 116 */       chilkatJNI.CkZipProgress_WriteZipBeginSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void WriteZipEnd()
/*     */   {
/* 122 */     if (getClass() == CkZipProgress.class) {
/* 123 */       chilkatJNI.CkZipProgress_WriteZipEnd(this.swigCPtr, this);
/*     */     } else {
/* 125 */       chilkatJNI.CkZipProgress_WriteZipEndSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void UnzipBegin()
/*     */   {
/* 131 */     if (getClass() == CkZipProgress.class) {
/* 132 */       chilkatJNI.CkZipProgress_UnzipBegin(this.swigCPtr, this);
/*     */     } else {
/* 134 */       chilkatJNI.CkZipProgress_UnzipBeginSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void UnzipEnd()
/*     */   {
/* 140 */     if (getClass() == CkZipProgress.class) {
/* 141 */       chilkatJNI.CkZipProgress_UnzipEnd(this.swigCPtr, this);
/*     */     } else {
/* 143 */       chilkatJNI.CkZipProgress_UnzipEndSwigExplicitCkZipProgress(this.swigCPtr, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkZipProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */