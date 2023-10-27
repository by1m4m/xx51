/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkMht
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkMht(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkMht var0) {
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
/*  29 */         chilkatJNI.delete_CkMht(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkMht()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkMht(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkMht_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkMht_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkMht_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkMht_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkMht_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkMht_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_BaseUrl(CkString var1) {
/*  66 */     chilkatJNI.CkMht_get_BaseUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String baseUrl() {
/*  70 */     return chilkatJNI.CkMht_baseUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_BaseUrl(String var1) {
/*  74 */     chilkatJNI.CkMht_put_BaseUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ConnectTimeout() {
/*  78 */     return chilkatJNI.CkMht_get_ConnectTimeout(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConnectTimeout(int var1) {
/*  82 */     chilkatJNI.CkMht_put_ConnectTimeout(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugHtmlAfter(CkString var1) {
/*  86 */     chilkatJNI.CkMht_get_DebugHtmlAfter(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugHtmlAfter() {
/*  90 */     return chilkatJNI.CkMht_debugHtmlAfter(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugHtmlAfter(String var1) {
/*  94 */     chilkatJNI.CkMht_put_DebugHtmlAfter(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugHtmlBefore(CkString var1) {
/*  98 */     chilkatJNI.CkMht_get_DebugHtmlBefore(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugHtmlBefore() {
/* 102 */     return chilkatJNI.CkMht_debugHtmlBefore(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugHtmlBefore(String var1) {
/* 106 */     chilkatJNI.CkMht_put_DebugHtmlBefore(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 110 */     chilkatJNI.CkMht_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 114 */     return chilkatJNI.CkMht_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 118 */     chilkatJNI.CkMht_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DebugTagCleaning() {
/* 122 */     return chilkatJNI.CkMht_get_DebugTagCleaning(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugTagCleaning(boolean var1) {
/* 126 */     chilkatJNI.CkMht_put_DebugTagCleaning(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmbedImages() {
/* 130 */     return chilkatJNI.CkMht_get_EmbedImages(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmbedImages(boolean var1) {
/* 134 */     chilkatJNI.CkMht_put_EmbedImages(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmbedLocalOnly() {
/* 138 */     return chilkatJNI.CkMht_get_EmbedLocalOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmbedLocalOnly(boolean var1) {
/* 142 */     chilkatJNI.CkMht_put_EmbedLocalOnly(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_FetchFromCache() {
/* 146 */     return chilkatJNI.CkMht_get_FetchFromCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FetchFromCache(boolean var1) {
/* 150 */     chilkatJNI.CkMht_put_FetchFromCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 154 */     return chilkatJNI.CkMht_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 158 */     chilkatJNI.CkMht_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IgnoreMustRevalidate() {
/* 162 */     return chilkatJNI.CkMht_get_IgnoreMustRevalidate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IgnoreMustRevalidate(boolean var1) {
/* 166 */     chilkatJNI.CkMht_put_IgnoreMustRevalidate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IgnoreNoCache() {
/* 170 */     return chilkatJNI.CkMht_get_IgnoreNoCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IgnoreNoCache(boolean var1) {
/* 174 */     chilkatJNI.CkMht_put_IgnoreNoCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 178 */     chilkatJNI.CkMht_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 182 */     return chilkatJNI.CkMht_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 186 */     chilkatJNI.CkMht_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 190 */     return chilkatJNI.CkMht_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 194 */     chilkatJNI.CkMht_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 198 */     return chilkatJNI.CkMht_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 202 */     return chilkatJNI.CkMht_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 206 */     chilkatJNI.CkMht_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_NoScripts() {
/* 210 */     return chilkatJNI.CkMht_get_NoScripts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NoScripts(boolean var1) {
/* 214 */     chilkatJNI.CkMht_put_NoScripts(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_NtlmAuth() {
/* 218 */     return chilkatJNI.CkMht_get_NtlmAuth(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NtlmAuth(boolean var1) {
/* 222 */     chilkatJNI.CkMht_put_NtlmAuth(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCacheLevels() {
/* 226 */     return chilkatJNI.CkMht_get_NumCacheLevels(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NumCacheLevels(int var1) {
/* 230 */     chilkatJNI.CkMht_put_NumCacheLevels(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCacheRoots() {
/* 234 */     return chilkatJNI.CkMht_get_NumCacheRoots(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 238 */     return chilkatJNI.CkMht_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 242 */     chilkatJNI.CkMht_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferMHTScripts() {
/* 246 */     return chilkatJNI.CkMht_get_PreferMHTScripts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferMHTScripts(boolean var1) {
/* 250 */     chilkatJNI.CkMht_put_PreferMHTScripts(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Proxy(CkString var1) {
/* 254 */     chilkatJNI.CkMht_get_Proxy(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxy() {
/* 258 */     return chilkatJNI.CkMht_proxy(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Proxy(String var1) {
/* 262 */     chilkatJNI.CkMht_put_Proxy(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyLogin(CkString var1) {
/* 266 */     chilkatJNI.CkMht_get_ProxyLogin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyLogin() {
/* 270 */     return chilkatJNI.CkMht_proxyLogin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyLogin(String var1) {
/* 274 */     chilkatJNI.CkMht_put_ProxyLogin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyPassword(CkString var1) {
/* 278 */     chilkatJNI.CkMht_get_ProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyPassword() {
/* 282 */     return chilkatJNI.CkMht_proxyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyPassword(String var1) {
/* 286 */     chilkatJNI.CkMht_put_ProxyPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ReadTimeout() {
/* 290 */     return chilkatJNI.CkMht_get_ReadTimeout(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadTimeout(int var1) {
/* 294 */     chilkatJNI.CkMht_put_ReadTimeout(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_RequireSslCertVerify() {
/* 298 */     return chilkatJNI.CkMht_get_RequireSslCertVerify(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RequireSslCertVerify(boolean var1) {
/* 302 */     chilkatJNI.CkMht_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksHostname(CkString var1) {
/* 306 */     chilkatJNI.CkMht_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksHostname() {
/* 310 */     return chilkatJNI.CkMht_socksHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksHostname(String var1) {
/* 314 */     chilkatJNI.CkMht_put_SocksHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksPassword(CkString var1) {
/* 318 */     chilkatJNI.CkMht_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksPassword() {
/* 322 */     return chilkatJNI.CkMht_socksPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPassword(String var1) {
/* 326 */     chilkatJNI.CkMht_put_SocksPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksPort() {
/* 330 */     return chilkatJNI.CkMht_get_SocksPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPort(int var1) {
/* 334 */     chilkatJNI.CkMht_put_SocksPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksUsername(CkString var1) {
/* 338 */     chilkatJNI.CkMht_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksUsername() {
/* 342 */     return chilkatJNI.CkMht_socksUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksUsername(String var1) {
/* 346 */     chilkatJNI.CkMht_put_SocksUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksVersion() {
/* 350 */     return chilkatJNI.CkMht_get_SocksVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksVersion(int var1) {
/* 354 */     chilkatJNI.CkMht_put_SocksVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UnpackDirect() {
/* 358 */     return chilkatJNI.CkMht_get_UnpackDirect(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UnpackDirect(boolean var1) {
/* 362 */     chilkatJNI.CkMht_put_UnpackDirect(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UnpackUseRelPaths() {
/* 366 */     return chilkatJNI.CkMht_get_UnpackUseRelPaths(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UnpackUseRelPaths(boolean var1) {
/* 370 */     chilkatJNI.CkMht_put_UnpackUseRelPaths(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UpdateCache() {
/* 374 */     return chilkatJNI.CkMht_get_UpdateCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UpdateCache(boolean var1) {
/* 378 */     chilkatJNI.CkMht_put_UpdateCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseCids() {
/* 382 */     return chilkatJNI.CkMht_get_UseCids(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseCids(boolean var1) {
/* 386 */     chilkatJNI.CkMht_put_UseCids(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseFilename() {
/* 390 */     return chilkatJNI.CkMht_get_UseFilename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseFilename(boolean var1) {
/* 394 */     chilkatJNI.CkMht_put_UseFilename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseIEProxy() {
/* 398 */     return chilkatJNI.CkMht_get_UseIEProxy(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseIEProxy(boolean var1) {
/* 402 */     chilkatJNI.CkMht_put_UseIEProxy(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseInline() {
/* 406 */     return chilkatJNI.CkMht_get_UseInline(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseInline(boolean var1) {
/* 410 */     chilkatJNI.CkMht_put_UseInline(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 414 */     return chilkatJNI.CkMht_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 418 */     chilkatJNI.CkMht_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 422 */     chilkatJNI.CkMht_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 426 */     return chilkatJNI.CkMht_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_WebSiteLogin(CkString var1) {
/* 430 */     chilkatJNI.CkMht_get_WebSiteLogin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String webSiteLogin() {
/* 434 */     return chilkatJNI.CkMht_webSiteLogin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WebSiteLogin(String var1) {
/* 438 */     chilkatJNI.CkMht_put_WebSiteLogin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_WebSiteLoginDomain(CkString var1) {
/* 442 */     chilkatJNI.CkMht_get_WebSiteLoginDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String webSiteLoginDomain() {
/* 446 */     return chilkatJNI.CkMht_webSiteLoginDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WebSiteLoginDomain(String var1) {
/* 450 */     chilkatJNI.CkMht_put_WebSiteLoginDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_WebSitePassword(CkString var1) {
/* 454 */     chilkatJNI.CkMht_get_WebSitePassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String webSitePassword() {
/* 458 */     return chilkatJNI.CkMht_webSitePassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WebSitePassword(String var1) {
/* 462 */     chilkatJNI.CkMht_put_WebSitePassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddCacheRoot(String var1) {
/* 466 */     chilkatJNI.CkMht_AddCacheRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddCustomHeader(String var1, String var2) {
/* 470 */     chilkatJNI.CkMht_AddCustomHeader(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddExternalStyleSheet(String var1) {
/* 474 */     chilkatJNI.CkMht_AddExternalStyleSheet(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void ClearCustomHeaders() {
/* 478 */     chilkatJNI.CkMht_ClearCustomHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ExcludeImagesMatching(String var1) {
/* 482 */     chilkatJNI.CkMht_ExcludeImagesMatching(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetAndSaveEML(String var1, String var2) {
/* 486 */     return chilkatJNI.CkMht_GetAndSaveEML(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask GetAndSaveEMLAsync(String var1, String var2) {
/* 490 */     long var3 = chilkatJNI.CkMht_GetAndSaveEMLAsync(this.swigCPtr, this, var1, var2);
/* 491 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetAndSaveMHT(String var1, String var2) {
/* 495 */     return chilkatJNI.CkMht_GetAndSaveMHT(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask GetAndSaveMHTAsync(String var1, String var2) {
/* 499 */     long var3 = chilkatJNI.CkMht_GetAndSaveMHTAsync(this.swigCPtr, this, var1, var2);
/* 500 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetAndZipEML(String var1, String var2, String var3) {
/* 504 */     return chilkatJNI.CkMht_GetAndZipEML(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask GetAndZipEMLAsync(String var1, String var2, String var3) {
/* 508 */     long var4 = chilkatJNI.CkMht_GetAndZipEMLAsync(this.swigCPtr, this, var1, var2, var3);
/* 509 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean GetAndZipMHT(String var1, String var2, String var3) {
/* 513 */     return chilkatJNI.CkMht_GetAndZipMHT(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask GetAndZipMHTAsync(String var1, String var2, String var3) {
/* 517 */     long var4 = chilkatJNI.CkMht_GetAndZipMHTAsync(this.swigCPtr, this, var1, var2, var3);
/* 518 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean GetCacheRoot(int var1, CkString var2) {
/* 522 */     return chilkatJNI.CkMht_GetCacheRoot(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCacheRoot(int var1) {
/* 526 */     return chilkatJNI.CkMht_getCacheRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cacheRoot(int var1) {
/* 530 */     return chilkatJNI.CkMht_cacheRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEML(String var1, CkString var2) {
/* 534 */     return chilkatJNI.CkMht_GetEML(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEML(String var1) {
/* 538 */     return chilkatJNI.CkMht_getEML(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String eML(String var1) {
/* 542 */     return chilkatJNI.CkMht_eML(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask GetEMLAsync(String var1) {
/* 546 */     long var2 = chilkatJNI.CkMht_GetEMLAsync(this.swigCPtr, this, var1);
/* 547 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetMHT(String var1, CkString var2) {
/* 551 */     return chilkatJNI.CkMht_GetMHT(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getMHT(String var1) {
/* 555 */     return chilkatJNI.CkMht_getMHT(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String mHT(String var1) {
/* 559 */     return chilkatJNI.CkMht_mHT(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask GetMHTAsync(String var1) {
/* 563 */     long var2 = chilkatJNI.CkMht_GetMHTAsync(this.swigCPtr, this, var1);
/* 564 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean HtmlToEML(String var1, CkString var2) {
/* 568 */     return chilkatJNI.CkMht_HtmlToEML(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String htmlToEML(String var1) {
/* 572 */     return chilkatJNI.CkMht_htmlToEML(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask HtmlToEMLAsync(String var1) {
/* 576 */     long var2 = chilkatJNI.CkMht_HtmlToEMLAsync(this.swigCPtr, this, var1);
/* 577 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean HtmlToEMLFile(String var1, String var2) {
/* 581 */     return chilkatJNI.CkMht_HtmlToEMLFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask HtmlToEMLFileAsync(String var1, String var2) {
/* 585 */     long var3 = chilkatJNI.CkMht_HtmlToEMLFileAsync(this.swigCPtr, this, var1, var2);
/* 586 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean HtmlToMHT(String var1, CkString var2) {
/* 590 */     return chilkatJNI.CkMht_HtmlToMHT(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String htmlToMHT(String var1) {
/* 594 */     return chilkatJNI.CkMht_htmlToMHT(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask HtmlToMHTAsync(String var1) {
/* 598 */     long var2 = chilkatJNI.CkMht_HtmlToMHTAsync(this.swigCPtr, this, var1);
/* 599 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean HtmlToMHTFile(String var1, String var2) {
/* 603 */     return chilkatJNI.CkMht_HtmlToMHTFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask HtmlToMHTFileAsync(String var1, String var2) {
/* 607 */     long var3 = chilkatJNI.CkMht_HtmlToMHTFileAsync(this.swigCPtr, this, var1, var2);
/* 608 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 612 */     return chilkatJNI.CkMht_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void RemoveCustomHeader(String var1) {
/* 616 */     chilkatJNI.CkMht_RemoveCustomHeader(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RestoreDefaults() {
/* 620 */     chilkatJNI.CkMht_RestoreDefaults(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 624 */     return chilkatJNI.CkMht_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 628 */     return chilkatJNI.CkMht_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnpackMHT(String var1, String var2, String var3, String var4) {
/* 632 */     return chilkatJNI.CkMht_UnpackMHT(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean UnpackMHTString(String var1, String var2, String var3, String var4) {
/* 636 */     return chilkatJNI.CkMht_UnpackMHTString(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMht.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */