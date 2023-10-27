/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkUpload
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkUpload(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkUpload var0) {
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
/*  29 */         chilkatJNI.delete_CkUpload(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkUpload()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkUpload(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkUpload_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkUpload_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkUpload_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkUpload_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkUpload_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkUpload_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_BandwidthThrottleUp() {
/*  66 */     return chilkatJNI.CkUpload_get_BandwidthThrottleUp(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_BandwidthThrottleUp(int var1) {
/*  70 */     chilkatJNI.CkUpload_put_BandwidthThrottleUp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ChunkSize() {
/*  74 */     return chilkatJNI.CkUpload_get_ChunkSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ChunkSize(int var1) {
/*  78 */     chilkatJNI.CkUpload_put_ChunkSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientIpAddress(CkString var1) {
/*  82 */     chilkatJNI.CkUpload_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientIpAddress() {
/*  86 */     return chilkatJNI.CkUpload_clientIpAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientIpAddress(String var1) {
/*  90 */     chilkatJNI.CkUpload_put_ClientIpAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  94 */     chilkatJNI.CkUpload_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  98 */     return chilkatJNI.CkUpload_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 102 */     chilkatJNI.CkUpload_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Expect100Continue() {
/* 106 */     return chilkatJNI.CkUpload_get_Expect100Continue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Expect100Continue(boolean var1) {
/* 110 */     chilkatJNI.CkUpload_put_Expect100Continue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 114 */     return chilkatJNI.CkUpload_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 118 */     chilkatJNI.CkUpload_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Hostname(CkString var1) {
/* 122 */     chilkatJNI.CkUpload_get_Hostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hostname() {
/* 126 */     return chilkatJNI.CkUpload_hostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Hostname(String var1) {
/* 130 */     chilkatJNI.CkUpload_put_Hostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_IdleTimeoutMs() {
/* 134 */     return chilkatJNI.CkUpload_get_IdleTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IdleTimeoutMs(int var1) {
/* 138 */     chilkatJNI.CkUpload_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 142 */     chilkatJNI.CkUpload_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 146 */     return chilkatJNI.CkUpload_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 150 */     chilkatJNI.CkUpload_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 154 */     return chilkatJNI.CkUpload_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 158 */     chilkatJNI.CkUpload_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 162 */     return chilkatJNI.CkUpload_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 166 */     return chilkatJNI.CkUpload_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 170 */     chilkatJNI.CkUpload_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Login(CkString var1) {
/* 174 */     chilkatJNI.CkUpload_get_Login(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String login() {
/* 178 */     return chilkatJNI.CkUpload_login(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Login(String var1) {
/* 182 */     chilkatJNI.CkUpload_put_Login(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long get_NumBytesSent() {
/* 186 */     return chilkatJNI.CkUpload_get_NumBytesSent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Password(CkString var1) {
/* 190 */     chilkatJNI.CkUpload_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String password() {
/* 194 */     return chilkatJNI.CkUpload_password(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Password(String var1) {
/* 198 */     chilkatJNI.CkUpload_put_Password(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Path(CkString var1) {
/* 202 */     chilkatJNI.CkUpload_get_Path(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String path() {
/* 206 */     return chilkatJNI.CkUpload_path(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Path(String var1) {
/* 210 */     chilkatJNI.CkUpload_put_Path(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_PercentDoneScale() {
/* 214 */     return chilkatJNI.CkUpload_get_PercentDoneScale(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PercentDoneScale(int var1) {
/* 218 */     chilkatJNI.CkUpload_put_PercentDoneScale(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long get_PercentUploaded() {
/* 222 */     return chilkatJNI.CkUpload_get_PercentUploaded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Port() {
/* 226 */     return chilkatJNI.CkUpload_get_Port(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Port(int var1) {
/* 230 */     chilkatJNI.CkUpload_put_Port(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 234 */     return chilkatJNI.CkUpload_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 238 */     chilkatJNI.CkUpload_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyDomain(CkString var1) {
/* 242 */     chilkatJNI.CkUpload_get_ProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyDomain() {
/* 246 */     return chilkatJNI.CkUpload_proxyDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyDomain(String var1) {
/* 250 */     chilkatJNI.CkUpload_put_ProxyDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyLogin(CkString var1) {
/* 254 */     chilkatJNI.CkUpload_get_ProxyLogin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyLogin() {
/* 258 */     return chilkatJNI.CkUpload_proxyLogin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyLogin(String var1) {
/* 262 */     chilkatJNI.CkUpload_put_ProxyLogin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyPassword(CkString var1) {
/* 266 */     chilkatJNI.CkUpload_get_ProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyPassword() {
/* 270 */     return chilkatJNI.CkUpload_proxyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyPassword(String var1) {
/* 274 */     chilkatJNI.CkUpload_put_ProxyPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ProxyPort() {
/* 278 */     return chilkatJNI.CkUpload_get_ProxyPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyPort(int var1) {
/* 282 */     chilkatJNI.CkUpload_put_ProxyPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ResponseBody(CkByteData var1) {
/* 286 */     chilkatJNI.CkUpload_get_ResponseBody(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_ResponseHeader(CkString var1) {
/* 290 */     chilkatJNI.CkUpload_get_ResponseHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String responseHeader() {
/* 294 */     return chilkatJNI.CkUpload_responseHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ResponseStatus() {
/* 298 */     return chilkatJNI.CkUpload_get_ResponseStatus(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Ssl() {
/* 302 */     return chilkatJNI.CkUpload_get_Ssl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Ssl(boolean var1) {
/* 306 */     chilkatJNI.CkUpload_put_Ssl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SslAllowedCiphers(CkString var1) {
/* 310 */     chilkatJNI.CkUpload_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sslAllowedCiphers() {
/* 314 */     return chilkatJNI.CkUpload_sslAllowedCiphers(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SslAllowedCiphers(String var1) {
/* 318 */     chilkatJNI.CkUpload_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SslProtocol(CkString var1) {
/* 322 */     chilkatJNI.CkUpload_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sslProtocol() {
/* 326 */     return chilkatJNI.CkUpload_sslProtocol(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SslProtocol(String var1) {
/* 330 */     chilkatJNI.CkUpload_put_SslProtocol(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TlsPinSet(CkString var1) {
/* 334 */     chilkatJNI.CkUpload_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tlsPinSet() {
/* 338 */     return chilkatJNI.CkUpload_tlsPinSet(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TlsPinSet(String var1) {
/* 342 */     chilkatJNI.CkUpload_put_TlsPinSet(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long get_TotalUploadSize() {
/* 346 */     return chilkatJNI.CkUpload_get_TotalUploadSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_UploadInProgress() {
/* 350 */     return chilkatJNI.CkUpload_get_UploadInProgress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_UploadSuccess() {
/* 354 */     return chilkatJNI.CkUpload_get_UploadSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 358 */     return chilkatJNI.CkUpload_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 362 */     chilkatJNI.CkUpload_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 366 */     chilkatJNI.CkUpload_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 370 */     return chilkatJNI.CkUpload_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void AbortUpload() {
/* 374 */     chilkatJNI.CkUpload_AbortUpload(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void AddCustomHeader(String var1, String var2) {
/* 378 */     chilkatJNI.CkUpload_AddCustomHeader(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddFileReference(String var1, String var2) {
/* 382 */     chilkatJNI.CkUpload_AddFileReference(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddParam(String var1, String var2) {
/* 386 */     chilkatJNI.CkUpload_AddParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean BeginUpload() {
/* 390 */     return chilkatJNI.CkUpload_BeginUpload(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean BlockingUpload() {
/* 394 */     return chilkatJNI.CkUpload_BlockingUpload(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask BlockingUploadAsync() {
/* 398 */     long var1 = chilkatJNI.CkUpload_BlockingUploadAsync(this.swigCPtr, this);
/* 399 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public void ClearFileReferences() {
/* 403 */     chilkatJNI.CkUpload_ClearFileReferences(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearParams() {
/* 407 */     chilkatJNI.CkUpload_ClearParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 411 */     return chilkatJNI.CkUpload_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SleepMs(int var1) {
/* 415 */     chilkatJNI.CkUpload_SleepMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UploadToMemory(CkByteData var1) {
/* 419 */     return chilkatJNI.CkUpload_UploadToMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkUpload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */