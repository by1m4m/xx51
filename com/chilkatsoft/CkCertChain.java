/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCertChain
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCertChain(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCertChain var0) {
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
/*  29 */         chilkatJNI.delete_CkCertChain(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCertChain()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCertChain(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCertChain_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCertChain_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCertChain_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkCertChain_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkCertChain_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkCertChain_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkCertChain_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkCertChain_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkCertChain_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkCertChain_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkCertChain_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkCertChain_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkCertChain_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkCertChain_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCerts() {
/*  98 */     return chilkatJNI.CkCertChain_get_NumCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumExpiredCerts() {
/* 102 */     return chilkatJNI.CkCertChain_get_NumExpiredCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ReachesRoot() {
/* 106 */     return chilkatJNI.CkCertChain_get_ReachesRoot(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkCertChain_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkCertChain_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkCertChain_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkCertChain_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkCert GetCert(int var1) {
/* 126 */     long var2 = chilkatJNI.CkCertChain_GetCert(this.swigCPtr, this, var1);
/* 127 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean IsRootTrusted(CkTrustedRoots var1) {
/* 131 */     return chilkatJNI.CkCertChain_IsRootTrusted(this.swigCPtr, this, CkTrustedRoots.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadX5C(CkJsonObject var1) {
/* 135 */     return chilkatJNI.CkCertChain_LoadX5C(this.swigCPtr, this, CkJsonObject.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 139 */     return chilkatJNI.CkCertChain_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyCertSignatures() {
/* 143 */     return chilkatJNI.CkCertChain_VerifyCertSignatures(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCertChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */