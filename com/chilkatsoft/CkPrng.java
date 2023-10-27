/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkPrng
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkPrng(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkPrng var0) {
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
/*  29 */         chilkatJNI.delete_CkPrng(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkPrng()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkPrng(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkPrng_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkPrng_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkPrng_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkPrng_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkPrng_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkPrng_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkPrng_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkPrng_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkPrng_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkPrng_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkPrng_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkPrng_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkPrng_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkPrng_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PrngName(CkString var1) {
/*  98 */     chilkatJNI.CkPrng_get_PrngName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String prngName() {
/* 102 */     return chilkatJNI.CkPrng_prngName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PrngName(String var1) {
/* 106 */     chilkatJNI.CkPrng_put_PrngName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkPrng_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkPrng_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkPrng_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkPrng_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddEntropy(String var1, String var2) {
/* 126 */     return chilkatJNI.CkPrng_AddEntropy(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddEntropyBytes(CkByteData var1) {
/* 130 */     return chilkatJNI.CkPrng_AddEntropyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ExportEntropy(CkString var1) {
/* 134 */     return chilkatJNI.CkPrng_ExportEntropy(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exportEntropy() {
/* 138 */     return chilkatJNI.CkPrng_exportEntropy(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean FirebasePushId(CkString var1) {
/* 142 */     return chilkatJNI.CkPrng_FirebasePushId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String firebasePushId() {
/* 146 */     return chilkatJNI.CkPrng_firebasePushId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenRandom(int var1, String var2, CkString var3) {
/* 150 */     return chilkatJNI.CkPrng_GenRandom(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String genRandom(int var1, String var2) {
/* 154 */     return chilkatJNI.CkPrng_genRandom(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GenRandomBd(int var1, CkBinData var2) {
/* 158 */     return chilkatJNI.CkPrng_GenRandomBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GenRandomBytes(int var1, CkByteData var2) {
/* 162 */     return chilkatJNI.CkPrng_GenRandomBytes(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetEntropy(int var1, String var2, CkString var3) {
/* 166 */     return chilkatJNI.CkPrng_GetEntropy(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getEntropy(int var1, String var2) {
/* 170 */     return chilkatJNI.CkPrng_getEntropy(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String entropy(int var1, String var2) {
/* 174 */     return chilkatJNI.CkPrng_entropy(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetEntropyBytes(int var1, CkByteData var2) {
/* 178 */     return chilkatJNI.CkPrng_GetEntropyBytes(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ImportEntropy(String var1) {
/* 182 */     return chilkatJNI.CkPrng_ImportEntropy(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int RandomInt(int var1, int var2) {
/* 186 */     return chilkatJNI.CkPrng_RandomInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RandomPassword(int var1, boolean var2, boolean var3, String var4, String var5, CkString var6) {
/* 190 */     return chilkatJNI.CkPrng_RandomPassword(this.swigCPtr, this, var1, var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*     */   }
/*     */   
/*     */   public String randomPassword(int var1, boolean var2, boolean var3, String var4, String var5) {
/* 194 */     return chilkatJNI.CkPrng_randomPassword(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean RandomString(int var1, boolean var2, boolean var3, boolean var4, CkString var5) {
/* 198 */     return chilkatJNI.CkPrng_RandomString(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*     */   }
/*     */   
/*     */   public String randomString(int var1, boolean var2, boolean var3, boolean var4) {
/* 202 */     return chilkatJNI.CkPrng_randomString(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 206 */     return chilkatJNI.CkPrng_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkPrng.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */