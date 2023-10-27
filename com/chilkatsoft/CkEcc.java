/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkEcc
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkEcc(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkEcc var0) {
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
/*  29 */         chilkatJNI.delete_CkEcc(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkEcc()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkEcc(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkEcc_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkEcc_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkEcc_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkEcc_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkEcc_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkEcc_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkEcc_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkEcc_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkEcc_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkEcc_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkEcc_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkEcc_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkEcc_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkEcc_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/*  98 */     return chilkatJNI.CkEcc_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 102 */     chilkatJNI.CkEcc_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 106 */     chilkatJNI.CkEcc_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 110 */     return chilkatJNI.CkEcc_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GenEccKey(String var1, CkPrng var2) {
/* 114 */     long var3 = chilkatJNI.CkEcc_GenEccKey(this.swigCPtr, this, var1, CkPrng.getCPtr(var2), var2);
/* 115 */     return var3 == 0L ? null : new CkPrivateKey(var3, true);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GenEccKey2(String var1, String var2, String var3) {
/* 119 */     long var4 = chilkatJNI.CkEcc_GenEccKey2(this.swigCPtr, this, var1, var2, var3);
/* 120 */     return var4 == 0L ? null : new CkPrivateKey(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 124 */     return chilkatJNI.CkEcc_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SharedSecretENC(CkPrivateKey var1, CkPublicKey var2, String var3, CkString var4) {
/* 128 */     return chilkatJNI.CkEcc_SharedSecretENC(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkPublicKey.getCPtr(var2), var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String sharedSecretENC(CkPrivateKey var1, CkPublicKey var2, String var3) {
/* 132 */     return chilkatJNI.CkEcc_sharedSecretENC(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkPublicKey.getCPtr(var2), var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SignHashENC(String var1, String var2, CkPrivateKey var3, CkPrng var4, CkString var5) {
/* 136 */     return chilkatJNI.CkEcc_SignHashENC(this.swigCPtr, this, var1, var2, CkPrivateKey.getCPtr(var3), var3, CkPrng.getCPtr(var4), var4, CkString.getCPtr(var5), var5);
/*     */   }
/*     */   
/*     */   public String signHashENC(String var1, String var2, CkPrivateKey var3, CkPrng var4) {
/* 140 */     return chilkatJNI.CkEcc_signHashENC(this.swigCPtr, this, var1, var2, CkPrivateKey.getCPtr(var3), var3, CkPrng.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public int VerifyHashENC(String var1, String var2, String var3, CkPublicKey var4) {
/* 144 */     return chilkatJNI.CkEcc_VerifyHashENC(this.swigCPtr, this, var1, var2, var3, CkPublicKey.getCPtr(var4), var4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkEcc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */