/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAuthAzureAD
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAuthAzureAD(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAuthAzureAD var0) {
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
/*  29 */         chilkatJNI.delete_CkAuthAzureAD(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAuthAzureAD()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAuthAzureAD(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAuthAzureAD_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAuthAzureAD_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAuthAzureAD_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkAuthAzureAD_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessToken(CkString var1) {
/*  58 */     chilkatJNI.CkAuthAzureAD_get_AccessToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessToken() {
/*  62 */     return chilkatJNI.CkAuthAzureAD_accessToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessToken(String var1) {
/*  66 */     chilkatJNI.CkAuthAzureAD_put_AccessToken(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientId(CkString var1) {
/*  70 */     chilkatJNI.CkAuthAzureAD_get_ClientId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientId() {
/*  74 */     return chilkatJNI.CkAuthAzureAD_clientId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientId(String var1) {
/*  78 */     chilkatJNI.CkAuthAzureAD_put_ClientId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientSecret(CkString var1) {
/*  82 */     chilkatJNI.CkAuthAzureAD_get_ClientSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientSecret() {
/*  86 */     return chilkatJNI.CkAuthAzureAD_clientSecret(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientSecret(String var1) {
/*  90 */     chilkatJNI.CkAuthAzureAD_put_ClientSecret(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  94 */     chilkatJNI.CkAuthAzureAD_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  98 */     return chilkatJNI.CkAuthAzureAD_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 102 */     chilkatJNI.CkAuthAzureAD_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 106 */     chilkatJNI.CkAuthAzureAD_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 110 */     return chilkatJNI.CkAuthAzureAD_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 114 */     chilkatJNI.CkAuthAzureAD_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 118 */     return chilkatJNI.CkAuthAzureAD_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 122 */     chilkatJNI.CkAuthAzureAD_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 126 */     return chilkatJNI.CkAuthAzureAD_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 130 */     return chilkatJNI.CkAuthAzureAD_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 134 */     chilkatJNI.CkAuthAzureAD_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumSecondsRemaining() {
/* 138 */     return chilkatJNI.CkAuthAzureAD_get_NumSecondsRemaining(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Resource(CkString var1) {
/* 142 */     chilkatJNI.CkAuthAzureAD_get_Resource(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String resource() {
/* 146 */     return chilkatJNI.CkAuthAzureAD_resource(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Resource(String var1) {
/* 150 */     chilkatJNI.CkAuthAzureAD_put_Resource(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TenantId(CkString var1) {
/* 154 */     chilkatJNI.CkAuthAzureAD_get_TenantId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tenantId() {
/* 158 */     return chilkatJNI.CkAuthAzureAD_tenantId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TenantId(String var1) {
/* 162 */     chilkatJNI.CkAuthAzureAD_put_TenantId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Valid() {
/* 166 */     return chilkatJNI.CkAuthAzureAD_get_Valid(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 170 */     return chilkatJNI.CkAuthAzureAD_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 174 */     chilkatJNI.CkAuthAzureAD_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 178 */     chilkatJNI.CkAuthAzureAD_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 182 */     return chilkatJNI.CkAuthAzureAD_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ObtainAccessToken(CkSocket var1) {
/* 186 */     return chilkatJNI.CkAuthAzureAD_ObtainAccessToken(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ObtainAccessTokenAsync(CkSocket var1) {
/* 190 */     long var2 = chilkatJNI.CkAuthAzureAD_ObtainAccessTokenAsync(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/* 191 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 195 */     return chilkatJNI.CkAuthAzureAD_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAuthAzureAD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */