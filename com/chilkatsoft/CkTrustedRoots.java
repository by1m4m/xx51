/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkTrustedRoots
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkTrustedRoots(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkTrustedRoots var0) {
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
/*  29 */         chilkatJNI.delete_CkTrustedRoots(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkTrustedRoots()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkTrustedRoots(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkTrustedRoots_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkTrustedRoots_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkTrustedRoots_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkTrustedRoots_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  58 */     chilkatJNI.CkTrustedRoots_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  62 */     return chilkatJNI.CkTrustedRoots_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  66 */     chilkatJNI.CkTrustedRoots_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  70 */     chilkatJNI.CkTrustedRoots_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  74 */     return chilkatJNI.CkTrustedRoots_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  78 */     chilkatJNI.CkTrustedRoots_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  82 */     return chilkatJNI.CkTrustedRoots_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  86 */     chilkatJNI.CkTrustedRoots_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  90 */     return chilkatJNI.CkTrustedRoots_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  94 */     return chilkatJNI.CkTrustedRoots_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  98 */     chilkatJNI.CkTrustedRoots_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCerts() {
/* 102 */     return chilkatJNI.CkTrustedRoots_get_NumCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_TrustSystemCaRoots() {
/* 106 */     return chilkatJNI.CkTrustedRoots_get_TrustSystemCaRoots(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TrustSystemCaRoots(boolean var1) {
/* 110 */     chilkatJNI.CkTrustedRoots_put_TrustSystemCaRoots(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 114 */     return chilkatJNI.CkTrustedRoots_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 118 */     chilkatJNI.CkTrustedRoots_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 122 */     chilkatJNI.CkTrustedRoots_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 126 */     return chilkatJNI.CkTrustedRoots_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Activate() {
/* 130 */     return chilkatJNI.CkTrustedRoots_Activate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddCert(CkCert var1) {
/* 134 */     return chilkatJNI.CkTrustedRoots_AddCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddJavaKeyStore(CkJavaKeyStore var1) {
/* 138 */     return chilkatJNI.CkTrustedRoots_AddJavaKeyStore(this.swigCPtr, this, CkJavaKeyStore.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask AddJavaKeyStoreAsync(CkJavaKeyStore var1) {
/* 142 */     long var2 = chilkatJNI.CkTrustedRoots_AddJavaKeyStoreAsync(this.swigCPtr, this, CkJavaKeyStore.getCPtr(var1), var1);
/* 143 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean Deactivate() {
/* 147 */     return chilkatJNI.CkTrustedRoots_Deactivate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkCert GetCert(int var1) {
/* 151 */     long var2 = chilkatJNI.CkTrustedRoots_GetCert(this.swigCPtr, this, var1);
/* 152 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadCaCertsPem(String var1) {
/* 156 */     return chilkatJNI.CkTrustedRoots_LoadCaCertsPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask LoadCaCertsPemAsync(String var1) {
/* 160 */     long var2 = chilkatJNI.CkTrustedRoots_LoadCaCertsPemAsync(this.swigCPtr, this, var1);
/* 161 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 165 */     return chilkatJNI.CkTrustedRoots_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkTrustedRoots.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */