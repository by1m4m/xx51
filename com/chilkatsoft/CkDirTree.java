/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkDirTree
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkDirTree(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkDirTree var0) {
/*  18 */     return var0 == null ? 0L : var0.swigCPtr;
/*     */   }
/*     */   
/*     */   protected void finalize() {
/*  22 */     delete();
/*     */   }
/*     */   
/*     */   public synchronized void delete() {
/*  26 */     if (this.swigCPtr != 0L) {
/*  27 */       if (this.swigCMemOwn) {
/*  28 */         this.swigCMemOwn = false;
/*  29 */         chilkatJNI.delete_CkDirTree(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkDirTree()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkDirTree(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkDirTree_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkDirTree_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkDirTree_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_BaseDir(CkString var1) {
/*  54 */     chilkatJNI.CkDirTree_get_BaseDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String baseDir() {
/*  58 */     return chilkatJNI.CkDirTree_baseDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_BaseDir(String var1) {
/*  62 */     chilkatJNI.CkDirTree_put_BaseDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkDirTree_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkDirTree_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkDirTree_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DoneIterating() {
/*  78 */     return chilkatJNI.CkDirTree_get_DoneIterating(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_FileSize32() {
/*  82 */     return chilkatJNI.CkDirTree_get_FileSize32(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FullPath(CkString var1) {
/*  86 */     chilkatJNI.CkDirTree_get_FullPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fullPath() {
/*  90 */     return chilkatJNI.CkDirTree_fullPath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FullUncPath(CkString var1) {
/*  94 */     chilkatJNI.CkDirTree_get_FullUncPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fullUncPath() {
/*  98 */     return chilkatJNI.CkDirTree_fullUncPath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsDirectory() {
/* 102 */     return chilkatJNI.CkDirTree_get_IsDirectory(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 106 */     chilkatJNI.CkDirTree_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 110 */     return chilkatJNI.CkDirTree_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 114 */     chilkatJNI.CkDirTree_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 118 */     return chilkatJNI.CkDirTree_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 122 */     chilkatJNI.CkDirTree_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 126 */     return chilkatJNI.CkDirTree_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 130 */     return chilkatJNI.CkDirTree_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 134 */     chilkatJNI.CkDirTree_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Recurse() {
/* 138 */     return chilkatJNI.CkDirTree_get_Recurse(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Recurse(boolean var1) {
/* 142 */     chilkatJNI.CkDirTree_put_Recurse(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_RelativePath(CkString var1) {
/* 146 */     chilkatJNI.CkDirTree_get_RelativePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String relativePath() {
/* 150 */     return chilkatJNI.CkDirTree_relativePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 154 */     return chilkatJNI.CkDirTree_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 158 */     chilkatJNI.CkDirTree_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 162 */     chilkatJNI.CkDirTree_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 166 */     return chilkatJNI.CkDirTree_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AdvancePosition() {
/* 170 */     return chilkatJNI.CkDirTree_AdvancePosition(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean BeginIterate() {
/* 174 */     return chilkatJNI.CkDirTree_BeginIterate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 178 */     return chilkatJNI.CkDirTree_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkDirTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */