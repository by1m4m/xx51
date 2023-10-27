/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAuthAzureSAS
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAuthAzureSAS(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAuthAzureSAS var0) {
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
/*  29 */         chilkatJNI.delete_CkAuthAzureSAS(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAuthAzureSAS()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAuthAzureSAS(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAuthAzureSAS_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAuthAzureSAS_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAuthAzureSAS_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessKey(CkString var1) {
/*  54 */     chilkatJNI.CkAuthAzureSAS_get_AccessKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessKey() {
/*  58 */     return chilkatJNI.CkAuthAzureSAS_accessKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessKey(String var1) {
/*  62 */     chilkatJNI.CkAuthAzureSAS_put_AccessKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkAuthAzureSAS_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkAuthAzureSAS_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkAuthAzureSAS_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  78 */     chilkatJNI.CkAuthAzureSAS_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  82 */     return chilkatJNI.CkAuthAzureSAS_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  86 */     chilkatJNI.CkAuthAzureSAS_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  90 */     return chilkatJNI.CkAuthAzureSAS_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  94 */     chilkatJNI.CkAuthAzureSAS_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  98 */     return chilkatJNI.CkAuthAzureSAS_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 102 */     return chilkatJNI.CkAuthAzureSAS_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 106 */     chilkatJNI.CkAuthAzureSAS_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_StringToSign(CkString var1) {
/* 110 */     chilkatJNI.CkAuthAzureSAS_get_StringToSign(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String stringToSign() {
/* 114 */     return chilkatJNI.CkAuthAzureSAS_stringToSign(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StringToSign(String var1) {
/* 118 */     chilkatJNI.CkAuthAzureSAS_put_StringToSign(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 122 */     return chilkatJNI.CkAuthAzureSAS_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 126 */     chilkatJNI.CkAuthAzureSAS_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 130 */     chilkatJNI.CkAuthAzureSAS_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 134 */     return chilkatJNI.CkAuthAzureSAS_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Clear() {
/* 138 */     chilkatJNI.CkAuthAzureSAS_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenerateToken(CkString var1) {
/* 142 */     return chilkatJNI.CkAuthAzureSAS_GenerateToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String generateToken() {
/* 146 */     return chilkatJNI.CkAuthAzureSAS_generateToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 150 */     return chilkatJNI.CkAuthAzureSAS_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetNonTokenParam(String var1, String var2) {
/* 154 */     return chilkatJNI.CkAuthAzureSAS_SetNonTokenParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetTokenParam(String var1, String var2, String var3) {
/* 158 */     return chilkatJNI.CkAuthAzureSAS_SetTokenParam(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAuthAzureSAS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */