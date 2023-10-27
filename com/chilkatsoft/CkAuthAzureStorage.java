/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAuthAzureStorage
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAuthAzureStorage(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAuthAzureStorage var0) {
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
/*  29 */         chilkatJNI.delete_CkAuthAzureStorage(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAuthAzureStorage()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAuthAzureStorage(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAuthAzureStorage_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAuthAzureStorage_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAuthAzureStorage_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessKey(CkString var1) {
/*  54 */     chilkatJNI.CkAuthAzureStorage_get_AccessKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessKey() {
/*  58 */     return chilkatJNI.CkAuthAzureStorage_accessKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessKey(String var1) {
/*  62 */     chilkatJNI.CkAuthAzureStorage_put_AccessKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Account(CkString var1) {
/*  66 */     chilkatJNI.CkAuthAzureStorage_get_Account(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String account() {
/*  70 */     return chilkatJNI.CkAuthAzureStorage_account(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Account(String var1) {
/*  74 */     chilkatJNI.CkAuthAzureStorage_put_Account(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkAuthAzureStorage_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkAuthAzureStorage_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkAuthAzureStorage_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkAuthAzureStorage_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkAuthAzureStorage_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkAuthAzureStorage_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkAuthAzureStorage_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkAuthAzureStorage_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkAuthAzureStorage_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkAuthAzureStorage_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkAuthAzureStorage_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Scheme(CkString var1) {
/* 122 */     chilkatJNI.CkAuthAzureStorage_get_Scheme(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String scheme() {
/* 126 */     return chilkatJNI.CkAuthAzureStorage_scheme(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Scheme(String var1) {
/* 130 */     chilkatJNI.CkAuthAzureStorage_put_Scheme(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Service(CkString var1) {
/* 134 */     chilkatJNI.CkAuthAzureStorage_get_Service(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String service() {
/* 138 */     return chilkatJNI.CkAuthAzureStorage_service(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Service(String var1) {
/* 142 */     chilkatJNI.CkAuthAzureStorage_put_Service(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 146 */     return chilkatJNI.CkAuthAzureStorage_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 150 */     chilkatJNI.CkAuthAzureStorage_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 154 */     chilkatJNI.CkAuthAzureStorage_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 158 */     return chilkatJNI.CkAuthAzureStorage_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_XMsVersion(CkString var1) {
/* 162 */     chilkatJNI.CkAuthAzureStorage_get_XMsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String xMsVersion() {
/* 166 */     return chilkatJNI.CkAuthAzureStorage_xMsVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_XMsVersion(String var1) {
/* 170 */     chilkatJNI.CkAuthAzureStorage_put_XMsVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 174 */     return chilkatJNI.CkAuthAzureStorage_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAuthAzureStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */