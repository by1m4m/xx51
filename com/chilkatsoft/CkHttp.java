/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkHttp
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkHttp(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkHttp var0) {
/*   18 */     return var0 == null ? 0L : var0.swigCPtr;
/*      */   }
/*      */   
/*      */   protected void finalize() {
/*   22 */     delete();
/*      */   }
/*      */   
/*      */   public synchronized void delete() {
/*   26 */     if (this.swigCPtr != 0L) {
/*   27 */       if (this.swigCMemOwn) {
/*   28 */         this.swigCMemOwn = false;
/*   29 */         chilkatJNI.delete_CkHttp(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkHttp()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkHttp(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkHttp_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkHttp_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkHttp_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkHttpProgress var1) {
/*   54 */     chilkatJNI.CkHttp_put_EventCallbackObject(this.swigCPtr, this, CkHttpProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkHttp_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkHttp_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Accept(CkString var1) {
/*   66 */     chilkatJNI.CkHttp_get_Accept(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String ck_accept() {
/*   70 */     return chilkatJNI.CkHttp_ck_accept(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Accept(String var1) {
/*   74 */     chilkatJNI.CkHttp_put_Accept(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AcceptCharset(CkString var1) {
/*   78 */     chilkatJNI.CkHttp_get_AcceptCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String acceptCharset() {
/*   82 */     return chilkatJNI.CkHttp_acceptCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AcceptCharset(String var1) {
/*   86 */     chilkatJNI.CkHttp_put_AcceptCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AcceptLanguage(CkString var1) {
/*   90 */     chilkatJNI.CkHttp_get_AcceptLanguage(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String acceptLanguage() {
/*   94 */     return chilkatJNI.CkHttp_acceptLanguage(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AcceptLanguage(String var1) {
/*   98 */     chilkatJNI.CkHttp_put_AcceptLanguage(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AllowGzip() {
/*  102 */     return chilkatJNI.CkHttp_get_AllowGzip(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AllowGzip(boolean var1) {
/*  106 */     chilkatJNI.CkHttp_put_AllowGzip(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AllowHeaderFolding() {
/*  110 */     return chilkatJNI.CkHttp_get_AllowHeaderFolding(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AllowHeaderFolding(boolean var1) {
/*  114 */     chilkatJNI.CkHttp_put_AllowHeaderFolding(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AuthToken(CkString var1) {
/*  118 */     chilkatJNI.CkHttp_get_AuthToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String authToken() {
/*  122 */     return chilkatJNI.CkHttp_authToken(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AuthToken(String var1) {
/*  126 */     chilkatJNI.CkHttp_put_AuthToken(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoAddHostHeader() {
/*  130 */     return chilkatJNI.CkHttp_get_AutoAddHostHeader(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoAddHostHeader(boolean var1) {
/*  134 */     chilkatJNI.CkHttp_put_AutoAddHostHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AwsAccessKey(CkString var1) {
/*  138 */     chilkatJNI.CkHttp_get_AwsAccessKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String awsAccessKey() {
/*  142 */     return chilkatJNI.CkHttp_awsAccessKey(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsAccessKey(String var1) {
/*  146 */     chilkatJNI.CkHttp_put_AwsAccessKey(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AwsEndpoint(CkString var1) {
/*  150 */     chilkatJNI.CkHttp_get_AwsEndpoint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String awsEndpoint() {
/*  154 */     return chilkatJNI.CkHttp_awsEndpoint(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsEndpoint(String var1) {
/*  158 */     chilkatJNI.CkHttp_put_AwsEndpoint(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AwsRegion(CkString var1) {
/*  162 */     chilkatJNI.CkHttp_get_AwsRegion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String awsRegion() {
/*  166 */     return chilkatJNI.CkHttp_awsRegion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsRegion(String var1) {
/*  170 */     chilkatJNI.CkHttp_put_AwsRegion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AwsSecretKey(CkString var1) {
/*  174 */     chilkatJNI.CkHttp_get_AwsSecretKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String awsSecretKey() {
/*  178 */     return chilkatJNI.CkHttp_awsSecretKey(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsSecretKey(String var1) {
/*  182 */     chilkatJNI.CkHttp_put_AwsSecretKey(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_AwsSignatureVersion() {
/*  186 */     return chilkatJNI.CkHttp_get_AwsSignatureVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsSignatureVersion(int var1) {
/*  190 */     chilkatJNI.CkHttp_put_AwsSignatureVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AwsSubResources(CkString var1) {
/*  194 */     chilkatJNI.CkHttp_get_AwsSubResources(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String awsSubResources() {
/*  198 */     return chilkatJNI.CkHttp_awsSubResources(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AwsSubResources(String var1) {
/*  202 */     chilkatJNI.CkHttp_put_AwsSubResources(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleDown() {
/*  206 */     return chilkatJNI.CkHttp_get_BandwidthThrottleDown(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleDown(int var1) {
/*  210 */     chilkatJNI.CkHttp_put_BandwidthThrottleDown(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleUp() {
/*  214 */     return chilkatJNI.CkHttp_get_BandwidthThrottleUp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleUp(int var1) {
/*  218 */     chilkatJNI.CkHttp_put_BandwidthThrottleUp(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_BasicAuth() {
/*  222 */     return chilkatJNI.CkHttp_get_BasicAuth(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BasicAuth(boolean var1) {
/*  226 */     chilkatJNI.CkHttp_put_BasicAuth(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_BgLastErrorText(CkString var1) {
/*  230 */     chilkatJNI.CkHttp_get_BgLastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String bgLastErrorText() {
/*  234 */     return chilkatJNI.CkHttp_bgLastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_BgPercentDone() {
/*  238 */     return chilkatJNI.CkHttp_get_BgPercentDone(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_BgResultData(CkByteData var1) {
/*  242 */     chilkatJNI.CkHttp_get_BgResultData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public int get_BgResultInt() {
/*  246 */     return chilkatJNI.CkHttp_get_BgResultInt(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_BgResultString(CkString var1) {
/*  250 */     chilkatJNI.CkHttp_get_BgResultString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String bgResultString() {
/*  254 */     return chilkatJNI.CkHttp_bgResultString(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_BgTaskFinished() {
/*  258 */     return chilkatJNI.CkHttp_get_BgTaskFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_BgTaskRunning() {
/*  262 */     return chilkatJNI.CkHttp_get_BgTaskRunning(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_BgTaskSuccess() {
/*  266 */     return chilkatJNI.CkHttp_get_BgTaskSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  270 */     chilkatJNI.CkHttp_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  274 */     return chilkatJNI.CkHttp_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  278 */     chilkatJNI.CkHttp_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectFailReason() {
/*  282 */     return chilkatJNI.CkHttp_get_ConnectFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_Connection(CkString var1) {
/*  286 */     chilkatJNI.CkHttp_get_Connection(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String connection() {
/*  290 */     return chilkatJNI.CkHttp_connection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Connection(String var1) {
/*  294 */     chilkatJNI.CkHttp_put_Connection(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectTimeout() {
/*  298 */     return chilkatJNI.CkHttp_get_ConnectTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ConnectTimeout(int var1) {
/*  302 */     chilkatJNI.CkHttp_put_ConnectTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CookieDir(CkString var1) {
/*  306 */     chilkatJNI.CkHttp_get_CookieDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cookieDir() {
/*  310 */     return chilkatJNI.CkHttp_cookieDir(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CookieDir(String var1) {
/*  314 */     chilkatJNI.CkHttp_put_CookieDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  318 */     chilkatJNI.CkHttp_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  322 */     return chilkatJNI.CkHttp_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  326 */     chilkatJNI.CkHttp_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_DefaultFreshPeriod() {
/*  330 */     return chilkatJNI.CkHttp_get_DefaultFreshPeriod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DefaultFreshPeriod(int var1) {
/*  334 */     chilkatJNI.CkHttp_put_DefaultFreshPeriod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_DigestAuth() {
/*  338 */     return chilkatJNI.CkHttp_get_DigestAuth(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DigestAuth(boolean var1) {
/*  342 */     chilkatJNI.CkHttp_put_DigestAuth(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_EventLogCount() {
/*  346 */     return chilkatJNI.CkHttp_get_EventLogCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_FetchFromCache() {
/*  350 */     return chilkatJNI.CkHttp_get_FetchFromCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FetchFromCache(boolean var1) {
/*  354 */     chilkatJNI.CkHttp_put_FetchFromCache(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_FinalRedirectUrl(CkString var1) {
/*  358 */     chilkatJNI.CkHttp_get_FinalRedirectUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String finalRedirectUrl() {
/*  362 */     return chilkatJNI.CkHttp_finalRedirectUrl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_FollowRedirects() {
/*  366 */     return chilkatJNI.CkHttp_get_FollowRedirects(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FollowRedirects(boolean var1) {
/*  370 */     chilkatJNI.CkHttp_put_FollowRedirects(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_FreshnessAlgorithm() {
/*  374 */     return chilkatJNI.CkHttp_get_FreshnessAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FreshnessAlgorithm(int var1) {
/*  378 */     chilkatJNI.CkHttp_put_FreshnessAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  382 */     return chilkatJNI.CkHttp_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  386 */     chilkatJNI.CkHttp_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IgnoreMustRevalidate() {
/*  390 */     return chilkatJNI.CkHttp_get_IgnoreMustRevalidate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IgnoreMustRevalidate(boolean var1) {
/*  394 */     chilkatJNI.CkHttp_put_IgnoreMustRevalidate(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IgnoreNoCache() {
/*  398 */     return chilkatJNI.CkHttp_get_IgnoreNoCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IgnoreNoCache(boolean var1) {
/*  402 */     chilkatJNI.CkHttp_put_IgnoreNoCache(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_KeepEventLog() {
/*  406 */     return chilkatJNI.CkHttp_get_KeepEventLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepEventLog(boolean var1) {
/*  410 */     chilkatJNI.CkHttp_put_KeepEventLog(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_KeepResponseBody() {
/*  414 */     return chilkatJNI.CkHttp_get_KeepResponseBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepResponseBody(boolean var1) {
/*  418 */     chilkatJNI.CkHttp_put_KeepResponseBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastContentType(CkString var1) {
/*  422 */     chilkatJNI.CkHttp_get_LastContentType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastContentType() {
/*  426 */     return chilkatJNI.CkHttp_lastContentType(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  430 */     chilkatJNI.CkHttp_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  434 */     return chilkatJNI.CkHttp_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  438 */     chilkatJNI.CkHttp_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  442 */     return chilkatJNI.CkHttp_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  446 */     chilkatJNI.CkHttp_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  450 */     return chilkatJNI.CkHttp_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastHeader(CkString var1) {
/*  454 */     chilkatJNI.CkHttp_get_LastHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastHeader() {
/*  458 */     return chilkatJNI.CkHttp_lastHeader(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  462 */     return chilkatJNI.CkHttp_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  466 */     chilkatJNI.CkHttp_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastModDate(CkString var1) {
/*  470 */     chilkatJNI.CkHttp_get_LastModDate(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastModDate() {
/*  474 */     return chilkatJNI.CkHttp_lastModDate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastResponseBody(CkString var1) {
/*  478 */     chilkatJNI.CkHttp_get_LastResponseBody(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastResponseBody() {
/*  482 */     return chilkatJNI.CkHttp_lastResponseBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastResponseHeader(CkString var1) {
/*  486 */     chilkatJNI.CkHttp_get_LastResponseHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastResponseHeader() {
/*  490 */     return chilkatJNI.CkHttp_lastResponseHeader(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_LastStatus() {
/*  494 */     return chilkatJNI.CkHttp_get_LastStatus(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastStatusText(CkString var1) {
/*  498 */     chilkatJNI.CkHttp_get_LastStatusText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastStatusText() {
/*  502 */     return chilkatJNI.CkHttp_lastStatusText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_LMFactor() {
/*  506 */     return chilkatJNI.CkHttp_get_LMFactor(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LMFactor(int var1) {
/*  510 */     chilkatJNI.CkHttp_put_LMFactor(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Login(CkString var1) {
/*  514 */     chilkatJNI.CkHttp_get_Login(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String login() {
/*  518 */     return chilkatJNI.CkHttp_login(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Login(String var1) {
/*  522 */     chilkatJNI.CkHttp_put_Login(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LoginDomain(CkString var1) {
/*  526 */     chilkatJNI.CkHttp_get_LoginDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String loginDomain() {
/*  530 */     return chilkatJNI.CkHttp_loginDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LoginDomain(String var1) {
/*  534 */     chilkatJNI.CkHttp_put_LoginDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxConnections() {
/*  538 */     return chilkatJNI.CkHttp_get_MaxConnections(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxConnections(int var1) {
/*  542 */     chilkatJNI.CkHttp_put_MaxConnections(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxFreshPeriod() {
/*  546 */     return chilkatJNI.CkHttp_get_MaxFreshPeriod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxFreshPeriod(int var1) {
/*  550 */     chilkatJNI.CkHttp_put_MaxFreshPeriod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public long get_MaxResponseSize() {
/*  554 */     return chilkatJNI.CkHttp_get_MaxResponseSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxResponseSize(long var1) {
/*  558 */     chilkatJNI.CkHttp_put_MaxResponseSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxUrlLen() {
/*  562 */     return chilkatJNI.CkHttp_get_MaxUrlLen(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxUrlLen(int var1) {
/*  566 */     chilkatJNI.CkHttp_put_MaxUrlLen(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_MimicFireFox() {
/*  570 */     return chilkatJNI.CkHttp_get_MimicFireFox(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MimicFireFox(boolean var1) {
/*  574 */     chilkatJNI.CkHttp_put_MimicFireFox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_MimicIE() {
/*  578 */     return chilkatJNI.CkHttp_get_MimicIE(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MimicIE(boolean var1) {
/*  582 */     chilkatJNI.CkHttp_put_MimicIE(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MinFreshPeriod() {
/*  586 */     return chilkatJNI.CkHttp_get_MinFreshPeriod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MinFreshPeriod(int var1) {
/*  590 */     chilkatJNI.CkHttp_put_MinFreshPeriod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_NegotiateAuth() {
/*  594 */     return chilkatJNI.CkHttp_get_NegotiateAuth(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_NegotiateAuth(boolean var1) {
/*  598 */     chilkatJNI.CkHttp_put_NegotiateAuth(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_NtlmAuth() {
/*  602 */     return chilkatJNI.CkHttp_get_NtlmAuth(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_NtlmAuth(boolean var1) {
/*  606 */     chilkatJNI.CkHttp_put_NtlmAuth(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_NumCacheLevels() {
/*  610 */     return chilkatJNI.CkHttp_get_NumCacheLevels(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_NumCacheLevels(int var1) {
/*  614 */     chilkatJNI.CkHttp_put_NumCacheLevels(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_NumCacheRoots() {
/*  618 */     return chilkatJNI.CkHttp_get_NumCacheRoots(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_OAuth1() {
/*  622 */     return chilkatJNI.CkHttp_get_OAuth1(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuth1(boolean var1) {
/*  626 */     chilkatJNI.CkHttp_put_OAuth1(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthCallback(CkString var1) {
/*  630 */     chilkatJNI.CkHttp_get_OAuthCallback(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthCallback() {
/*  634 */     return chilkatJNI.CkHttp_oAuthCallback(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthCallback(String var1) {
/*  638 */     chilkatJNI.CkHttp_put_OAuthCallback(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthConsumerKey(CkString var1) {
/*  642 */     chilkatJNI.CkHttp_get_OAuthConsumerKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthConsumerKey() {
/*  646 */     return chilkatJNI.CkHttp_oAuthConsumerKey(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthConsumerKey(String var1) {
/*  650 */     chilkatJNI.CkHttp_put_OAuthConsumerKey(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthConsumerSecret(CkString var1) {
/*  654 */     chilkatJNI.CkHttp_get_OAuthConsumerSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthConsumerSecret() {
/*  658 */     return chilkatJNI.CkHttp_oAuthConsumerSecret(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthConsumerSecret(String var1) {
/*  662 */     chilkatJNI.CkHttp_put_OAuthConsumerSecret(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthRealm(CkString var1) {
/*  666 */     chilkatJNI.CkHttp_get_OAuthRealm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthRealm() {
/*  670 */     return chilkatJNI.CkHttp_oAuthRealm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthRealm(String var1) {
/*  674 */     chilkatJNI.CkHttp_put_OAuthRealm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthSigMethod(CkString var1) {
/*  678 */     chilkatJNI.CkHttp_get_OAuthSigMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthSigMethod() {
/*  682 */     return chilkatJNI.CkHttp_oAuthSigMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthSigMethod(String var1) {
/*  686 */     chilkatJNI.CkHttp_put_OAuthSigMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthToken(CkString var1) {
/*  690 */     chilkatJNI.CkHttp_get_OAuthToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthToken() {
/*  694 */     return chilkatJNI.CkHttp_oAuthToken(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthToken(String var1) {
/*  698 */     chilkatJNI.CkHttp_put_OAuthToken(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthTokenSecret(CkString var1) {
/*  702 */     chilkatJNI.CkHttp_get_OAuthTokenSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthTokenSecret() {
/*  706 */     return chilkatJNI.CkHttp_oAuthTokenSecret(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthTokenSecret(String var1) {
/*  710 */     chilkatJNI.CkHttp_put_OAuthTokenSecret(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuthVerifier(CkString var1) {
/*  714 */     chilkatJNI.CkHttp_get_OAuthVerifier(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuthVerifier() {
/*  718 */     return chilkatJNI.CkHttp_oAuthVerifier(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuthVerifier(String var1) {
/*  722 */     chilkatJNI.CkHttp_put_OAuthVerifier(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Password(CkString var1) {
/*  726 */     chilkatJNI.CkHttp_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String password() {
/*  730 */     return chilkatJNI.CkHttp_password(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Password(String var1) {
/*  734 */     chilkatJNI.CkHttp_put_Password(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  738 */     return chilkatJNI.CkHttp_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  742 */     chilkatJNI.CkHttp_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  746 */     return chilkatJNI.CkHttp_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  750 */     chilkatJNI.CkHttp_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyAuthMethod(CkString var1) {
/*  754 */     chilkatJNI.CkHttp_get_ProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyAuthMethod() {
/*  758 */     return chilkatJNI.CkHttp_proxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyAuthMethod(String var1) {
/*  762 */     chilkatJNI.CkHttp_put_ProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyDomain(CkString var1) {
/*  766 */     chilkatJNI.CkHttp_get_ProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyDomain() {
/*  770 */     return chilkatJNI.CkHttp_proxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyDomain(String var1) {
/*  774 */     chilkatJNI.CkHttp_put_ProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyLogin(CkString var1) {
/*  778 */     chilkatJNI.CkHttp_get_ProxyLogin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyLogin() {
/*  782 */     return chilkatJNI.CkHttp_proxyLogin(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyLogin(String var1) {
/*  786 */     chilkatJNI.CkHttp_put_ProxyLogin(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyLoginDomain(CkString var1) {
/*  790 */     chilkatJNI.CkHttp_get_ProxyLoginDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyLoginDomain() {
/*  794 */     return chilkatJNI.CkHttp_proxyLoginDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyLoginDomain(String var1) {
/*  798 */     chilkatJNI.CkHttp_put_ProxyLoginDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyPassword(CkString var1) {
/*  802 */     chilkatJNI.CkHttp_get_ProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyPassword() {
/*  806 */     return chilkatJNI.CkHttp_proxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyPassword(String var1) {
/*  810 */     chilkatJNI.CkHttp_put_ProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ProxyPort() {
/*  814 */     return chilkatJNI.CkHttp_get_ProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyPort(int var1) {
/*  818 */     chilkatJNI.CkHttp_put_ProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReadTimeout() {
/*  822 */     return chilkatJNI.CkHttp_get_ReadTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadTimeout(int var1) {
/*  826 */     chilkatJNI.CkHttp_put_ReadTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_RedirectVerb(CkString var1) {
/*  830 */     chilkatJNI.CkHttp_get_RedirectVerb(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String redirectVerb() {
/*  834 */     return chilkatJNI.CkHttp_redirectVerb(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RedirectVerb(String var1) {
/*  838 */     chilkatJNI.CkHttp_put_RedirectVerb(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Referer(CkString var1) {
/*  842 */     chilkatJNI.CkHttp_get_Referer(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String referer() {
/*  846 */     return chilkatJNI.CkHttp_referer(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Referer(String var1) {
/*  850 */     chilkatJNI.CkHttp_put_Referer(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_RequiredContentType(CkString var1) {
/*  854 */     chilkatJNI.CkHttp_get_RequiredContentType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String requiredContentType() {
/*  858 */     return chilkatJNI.CkHttp_requiredContentType(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequiredContentType(String var1) {
/*  862 */     chilkatJNI.CkHttp_put_RequiredContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_RequireSslCertVerify() {
/*  866 */     return chilkatJNI.CkHttp_get_RequireSslCertVerify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequireSslCertVerify(boolean var1) {
/*  870 */     chilkatJNI.CkHttp_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_S3Ssl() {
/*  874 */     return chilkatJNI.CkHttp_get_S3Ssl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_S3Ssl(boolean var1) {
/*  878 */     chilkatJNI.CkHttp_put_S3Ssl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SaveCookies() {
/*  882 */     return chilkatJNI.CkHttp_get_SaveCookies(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SaveCookies(boolean var1) {
/*  886 */     chilkatJNI.CkHttp_put_SaveCookies(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SendBufferSize() {
/*  890 */     return chilkatJNI.CkHttp_get_SendBufferSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendBufferSize(int var1) {
/*  894 */     chilkatJNI.CkHttp_put_SendBufferSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SendCookies() {
/*  898 */     return chilkatJNI.CkHttp_get_SendCookies(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendCookies(boolean var1) {
/*  902 */     chilkatJNI.CkHttp_put_SendCookies(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SessionLogFilename(CkString var1) {
/*  906 */     chilkatJNI.CkHttp_get_SessionLogFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLogFilename() {
/*  910 */     return chilkatJNI.CkHttp_sessionLogFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SessionLogFilename(String var1) {
/*  914 */     chilkatJNI.CkHttp_put_SessionLogFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  918 */     chilkatJNI.CkHttp_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  922 */     return chilkatJNI.CkHttp_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  926 */     chilkatJNI.CkHttp_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  930 */     chilkatJNI.CkHttp_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  934 */     return chilkatJNI.CkHttp_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  938 */     chilkatJNI.CkHttp_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  942 */     return chilkatJNI.CkHttp_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  946 */     chilkatJNI.CkHttp_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  950 */     chilkatJNI.CkHttp_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  954 */     return chilkatJNI.CkHttp_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  958 */     chilkatJNI.CkHttp_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  962 */     return chilkatJNI.CkHttp_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  966 */     chilkatJNI.CkHttp_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  970 */     return chilkatJNI.CkHttp_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  974 */     chilkatJNI.CkHttp_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  978 */     return chilkatJNI.CkHttp_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  982 */     chilkatJNI.CkHttp_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslAllowedCiphers(CkString var1) {
/*  986 */     chilkatJNI.CkHttp_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslAllowedCiphers() {
/*  990 */     return chilkatJNI.CkHttp_sslAllowedCiphers(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslAllowedCiphers(String var1) {
/*  994 */     chilkatJNI.CkHttp_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslProtocol(CkString var1) {
/*  998 */     chilkatJNI.CkHttp_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslProtocol() {
/* 1002 */     return chilkatJNI.CkHttp_sslProtocol(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslProtocol(String var1) {
/* 1006 */     chilkatJNI.CkHttp_put_SslProtocol(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_StreamResponseBodyPath(CkString var1) {
/* 1010 */     chilkatJNI.CkHttp_get_StreamResponseBodyPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String streamResponseBodyPath() {
/* 1014 */     return chilkatJNI.CkHttp_streamResponseBodyPath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_StreamResponseBodyPath(String var1) {
/* 1018 */     chilkatJNI.CkHttp_put_StreamResponseBodyPath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsCipherSuite(CkString var1) {
/* 1022 */     chilkatJNI.CkHttp_get_TlsCipherSuite(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsCipherSuite() {
/* 1026 */     return chilkatJNI.CkHttp_tlsCipherSuite(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsPinSet(CkString var1) {
/* 1030 */     chilkatJNI.CkHttp_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsPinSet() {
/* 1034 */     return chilkatJNI.CkHttp_tlsPinSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TlsPinSet(String var1) {
/* 1038 */     chilkatJNI.CkHttp_put_TlsPinSet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsVersion(CkString var1) {
/* 1042 */     chilkatJNI.CkHttp_get_TlsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsVersion() {
/* 1046 */     return chilkatJNI.CkHttp_tlsVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_UpdateCache() {
/* 1050 */     return chilkatJNI.CkHttp_get_UpdateCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UpdateCache(boolean var1) {
/* 1054 */     chilkatJNI.CkHttp_put_UpdateCache(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_UseBgThread() {
/* 1058 */     return chilkatJNI.CkHttp_get_UseBgThread(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UseBgThread(boolean var1) {
/* 1062 */     chilkatJNI.CkHttp_put_UseBgThread(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_UseIEProxy() {
/* 1066 */     return chilkatJNI.CkHttp_get_UseIEProxy(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UseIEProxy(boolean var1) {
/* 1070 */     chilkatJNI.CkHttp_put_UseIEProxy(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_UserAgent(CkString var1) {
/* 1074 */     chilkatJNI.CkHttp_get_UserAgent(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String userAgent() {
/* 1078 */     return chilkatJNI.CkHttp_userAgent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UserAgent(String var1) {
/* 1082 */     chilkatJNI.CkHttp_put_UserAgent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/* 1086 */     return chilkatJNI.CkHttp_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/* 1090 */     chilkatJNI.CkHttp_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/* 1094 */     chilkatJNI.CkHttp_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/* 1098 */     return chilkatJNI.CkHttp_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_WasRedirected() {
/* 1102 */     return chilkatJNI.CkHttp_get_WasRedirected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void AddCacheRoot(String var1) {
/* 1106 */     chilkatJNI.CkHttp_AddCacheRoot(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddQuickHeader(String var1, String var2) {
/* 1110 */     return chilkatJNI.CkHttp_AddQuickHeader(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkHttpResponse BgResponseObject() {
/* 1114 */     long var1 = chilkatJNI.CkHttp_BgResponseObject(this.swigCPtr, this);
/* 1115 */     return var1 == 0L ? null : new CkHttpResponse(var1, true);
/*      */   }
/*      */   
/*      */   public void BgTaskAbort() {
/* 1119 */     chilkatJNI.CkHttp_BgTaskAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearBgEventLog() {
/* 1123 */     chilkatJNI.CkHttp_ClearBgEventLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearInMemoryCookies() {
/* 1127 */     chilkatJNI.CkHttp_ClearInMemoryCookies(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearUrlVars() {
/* 1131 */     chilkatJNI.CkHttp_ClearUrlVars(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean CloseAllConnections() {
/* 1135 */     return chilkatJNI.CkHttp_CloseAllConnections(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask CloseAllConnectionsAsync() {
/* 1139 */     long var1 = chilkatJNI.CkHttp_CloseAllConnectionsAsync(this.swigCPtr, this);
/* 1140 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean CreateOcspRequest(CkJsonObject var1, CkBinData var2) {
/* 1144 */     return chilkatJNI.CkHttp_CreateOcspRequest(this.swigCPtr, this, CkJsonObject.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean CreateTimestampRequest(String var1, String var2, String var3, boolean var4, boolean var5, CkBinData var6) {
/* 1148 */     return chilkatJNI.CkHttp_CreateTimestampRequest(this.swigCPtr, this, var1, var2, var3, var4, var5, CkBinData.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public void DnsCacheClear() {
/* 1152 */     chilkatJNI.CkHttp_DnsCacheClear(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean Download(String var1, String var2) {
/* 1156 */     return chilkatJNI.CkHttp_Download(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadAsync(String var1, String var2) {
/* 1160 */     long var3 = chilkatJNI.CkHttp_DownloadAsync(this.swigCPtr, this, var1, var2);
/* 1161 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadAppend(String var1, String var2) {
/* 1165 */     return chilkatJNI.CkHttp_DownloadAppend(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadAppendAsync(String var1, String var2) {
/* 1169 */     long var3 = chilkatJNI.CkHttp_DownloadAppendAsync(this.swigCPtr, this, var1, var2);
/* 1170 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadBd(String var1, CkBinData var2) {
/* 1174 */     return chilkatJNI.CkHttp_DownloadBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadBdAsync(String var1, CkBinData var2) {
/* 1178 */     long var3 = chilkatJNI.CkHttp_DownloadBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/* 1179 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadHash(String var1, String var2, String var3, CkString var4) {
/* 1183 */     return chilkatJNI.CkHttp_DownloadHash(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String downloadHash(String var1, String var2, String var3) {
/* 1187 */     return chilkatJNI.CkHttp_downloadHash(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask DownloadHashAsync(String var1, String var2, String var3) {
/* 1191 */     long var4 = chilkatJNI.CkHttp_DownloadHashAsync(this.swigCPtr, this, var1, var2, var3);
/* 1192 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadSb(String var1, String var2, CkStringBuilder var3) {
/* 1196 */     return chilkatJNI.CkHttp_DownloadSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask DownloadSbAsync(String var1, String var2, CkStringBuilder var3) {
/* 1200 */     long var4 = chilkatJNI.CkHttp_DownloadSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/* 1201 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean EventLogName(int var1, CkString var2) {
/* 1205 */     return chilkatJNI.CkHttp_EventLogName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String eventLogName(int var1) {
/* 1209 */     return chilkatJNI.CkHttp_eventLogName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean EventLogValue(int var1, CkString var2) {
/* 1213 */     return chilkatJNI.CkHttp_EventLogValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String eventLogValue(int var1) {
/* 1217 */     return chilkatJNI.CkHttp_eventLogValue(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean ExtractMetaRefreshUrl(String var1, CkString var2) {
/* 1221 */     return chilkatJNI.CkHttp_ExtractMetaRefreshUrl(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String extractMetaRefreshUrl(String var1) {
/* 1225 */     return chilkatJNI.CkHttp_extractMetaRefreshUrl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean G_SvcOauthAccessToken(String var1, String var2, String var3, int var4, CkCert var5, CkString var6) {
/* 1229 */     return chilkatJNI.CkHttp_G_SvcOauthAccessToken(this.swigCPtr, this, var1, var2, var3, var4, CkCert.getCPtr(var5), var5, CkString.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public String g_SvcOauthAccessToken(String var1, String var2, String var3, int var4, CkCert var5) {
/* 1233 */     return chilkatJNI.CkHttp_g_SvcOauthAccessToken(this.swigCPtr, this, var1, var2, var3, var4, CkCert.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public CkTask G_SvcOauthAccessTokenAsync(String var1, String var2, String var3, int var4, CkCert var5) {
/* 1237 */     long var6 = chilkatJNI.CkHttp_G_SvcOauthAccessTokenAsync(this.swigCPtr, this, var1, var2, var3, var4, CkCert.getCPtr(var5), var5);
/* 1238 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public boolean G_SvcOauthAccessToken2(CkHashtable var1, int var2, CkCert var3, CkString var4) {
/* 1242 */     return chilkatJNI.CkHttp_G_SvcOauthAccessToken2(this.swigCPtr, this, CkHashtable.getCPtr(var1), var1, var2, CkCert.getCPtr(var3), var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String g_SvcOauthAccessToken2(CkHashtable var1, int var2, CkCert var3) {
/* 1246 */     return chilkatJNI.CkHttp_g_SvcOauthAccessToken2(this.swigCPtr, this, CkHashtable.getCPtr(var1), var1, var2, CkCert.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask G_SvcOauthAccessToken2Async(CkHashtable var1, int var2, CkCert var3) {
/* 1250 */     long var4 = chilkatJNI.CkHttp_G_SvcOauthAccessToken2Async(this.swigCPtr, this, CkHashtable.getCPtr(var1), var1, var2, CkCert.getCPtr(var3), var3);
/* 1251 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GenTimeStamp(CkString var1) {
/* 1255 */     return chilkatJNI.CkHttp_GenTimeStamp(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String genTimeStamp() {
/* 1259 */     return chilkatJNI.CkHttp_genTimeStamp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean GetCacheRoot(int var1, CkString var2) {
/* 1263 */     return chilkatJNI.CkHttp_GetCacheRoot(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCacheRoot(int var1) {
/* 1267 */     return chilkatJNI.CkHttp_getCacheRoot(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String cacheRoot(int var1) {
/* 1271 */     return chilkatJNI.CkHttp_cacheRoot(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetCookieXml(String var1, CkString var2) {
/* 1275 */     return chilkatJNI.CkHttp_GetCookieXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCookieXml(String var1) {
/* 1279 */     return chilkatJNI.CkHttp_getCookieXml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String cookieXml(String var1) {
/* 1283 */     return chilkatJNI.CkHttp_cookieXml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetDomain(String var1, CkString var2) {
/* 1287 */     return chilkatJNI.CkHttp_GetDomain(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getDomain(String var1) {
/* 1291 */     return chilkatJNI.CkHttp_getDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String domain(String var1) {
/* 1295 */     return chilkatJNI.CkHttp_domain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkHttpResponse GetHead(String var1) {
/* 1299 */     long var2 = chilkatJNI.CkHttp_GetHead(this.swigCPtr, this, var1);
/* 1300 */     return var2 == 0L ? null : new CkHttpResponse(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetHeadAsync(String var1) {
/* 1304 */     long var2 = chilkatJNI.CkHttp_GetHeadAsync(this.swigCPtr, this, var1);
/* 1305 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetRequestHeader(String var1, CkString var2) {
/* 1309 */     return chilkatJNI.CkHttp_GetRequestHeader(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRequestHeader(String var1) {
/* 1313 */     return chilkatJNI.CkHttp_getRequestHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String requestHeader(String var1) {
/* 1317 */     return chilkatJNI.CkHttp_requestHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert GetServerSslCert(String var1, int var2) {
/* 1321 */     long var3 = chilkatJNI.CkHttp_GetServerSslCert(this.swigCPtr, this, var1, var2);
/* 1322 */     return var3 == 0L ? null : new CkCert(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask GetServerSslCertAsync(String var1, int var2) {
/* 1326 */     long var3 = chilkatJNI.CkHttp_GetServerSslCertAsync(this.swigCPtr, this, var1, var2);
/* 1327 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean GetUrlPath(String var1, CkString var2) {
/* 1331 */     return chilkatJNI.CkHttp_GetUrlPath(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getUrlPath(String var1) {
/* 1335 */     return chilkatJNI.CkHttp_getUrlPath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String urlPath(String var1) {
/* 1339 */     return chilkatJNI.CkHttp_urlPath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean HasRequestHeader(String var1) {
/* 1343 */     return chilkatJNI.CkHttp_HasRequestHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/* 1347 */     return chilkatJNI.CkHttp_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int ParseOcspReply(CkBinData var1, CkJsonObject var2) {
/* 1351 */     return chilkatJNI.CkHttp_ParseOcspReply(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkJsonObject.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PBinary(String var1, String var2, CkByteData var3, String var4, boolean var5, boolean var6) {
/* 1355 */     long var7 = chilkatJNI.CkHttp_PBinary(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3, var4, var5, var6);
/* 1356 */     return var7 == 0L ? null : new CkHttpResponse(var7, true);
/*      */   }
/*      */   
/*      */   public CkTask PBinaryAsync(String var1, String var2, CkByteData var3, String var4, boolean var5, boolean var6) {
/* 1360 */     long var7 = chilkatJNI.CkHttp_PBinaryAsync(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3, var4, var5, var6);
/* 1361 */     return var7 == 0L ? null : new CkTask(var7, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PBinaryBd(String var1, String var2, CkBinData var3, String var4, boolean var5, boolean var6) {
/* 1365 */     long var7 = chilkatJNI.CkHttp_PBinaryBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3, var4, var5, var6);
/* 1366 */     return var7 == 0L ? null : new CkHttpResponse(var7, true);
/*      */   }
/*      */   
/*      */   public CkTask PBinaryBdAsync(String var1, String var2, CkBinData var3, String var4, boolean var5, boolean var6) {
/* 1370 */     long var7 = chilkatJNI.CkHttp_PBinaryBdAsync(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3, var4, var5, var6);
/* 1371 */     return var7 == 0L ? null : new CkTask(var7, true);
/*      */   }
/*      */   
/*      */   public boolean PostBinary(String var1, CkByteData var2, String var3, boolean var4, boolean var5, CkString var6) {
/* 1375 */     return chilkatJNI.CkHttp_PostBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public String postBinary(String var1, CkByteData var2, String var3, boolean var4, boolean var5) {
/* 1379 */     return chilkatJNI.CkHttp_postBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5);
/*      */   }
/*      */   
/*      */   public CkTask PostBinaryAsync(String var1, CkByteData var2, String var3, boolean var4, boolean var5) {
/* 1383 */     long var6 = chilkatJNI.CkHttp_PostBinaryAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5);
/* 1384 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PostJson(String var1, String var2) {
/* 1388 */     long var3 = chilkatJNI.CkHttp_PostJson(this.swigCPtr, this, var1, var2);
/* 1389 */     return var3 == 0L ? null : new CkHttpResponse(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask PostJsonAsync(String var1, String var2) {
/* 1393 */     long var3 = chilkatJNI.CkHttp_PostJsonAsync(this.swigCPtr, this, var1, var2);
/* 1394 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PostJson2(String var1, String var2, String var3) {
/* 1398 */     long var4 = chilkatJNI.CkHttp_PostJson2(this.swigCPtr, this, var1, var2, var3);
/* 1399 */     return var4 == 0L ? null : new CkHttpResponse(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask PostJson2Async(String var1, String var2, String var3) {
/* 1403 */     long var4 = chilkatJNI.CkHttp_PostJson2Async(this.swigCPtr, this, var1, var2, var3);
/* 1404 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PostJson3(String var1, String var2, CkJsonObject var3) {
/* 1408 */     long var4 = chilkatJNI.CkHttp_PostJson3(this.swigCPtr, this, var1, var2, CkJsonObject.getCPtr(var3), var3);
/* 1409 */     return var4 == 0L ? null : new CkHttpResponse(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask PostJson3Async(String var1, String var2, CkJsonObject var3) {
/* 1413 */     long var4 = chilkatJNI.CkHttp_PostJson3Async(this.swigCPtr, this, var1, var2, CkJsonObject.getCPtr(var3), var3);
/* 1414 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PostUrlEncoded(String var1, CkHttpRequest var2) {
/* 1418 */     long var3 = chilkatJNI.CkHttp_PostUrlEncoded(this.swigCPtr, this, var1, CkHttpRequest.getCPtr(var2), var2);
/* 1419 */     return var3 == 0L ? null : new CkHttpResponse(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask PostUrlEncodedAsync(String var1, CkHttpRequest var2) {
/* 1423 */     long var3 = chilkatJNI.CkHttp_PostUrlEncodedAsync(this.swigCPtr, this, var1, CkHttpRequest.getCPtr(var2), var2);
/* 1424 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PostXml(String var1, String var2, String var3) {
/* 1428 */     long var4 = chilkatJNI.CkHttp_PostXml(this.swigCPtr, this, var1, var2, var3);
/* 1429 */     return var4 == 0L ? null : new CkHttpResponse(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask PostXmlAsync(String var1, String var2, String var3) {
/* 1433 */     long var4 = chilkatJNI.CkHttp_PostXmlAsync(this.swigCPtr, this, var1, var2, var3);
/* 1434 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PText(String var1, String var2, String var3, String var4, String var5, boolean var6, boolean var7) {
/* 1438 */     long var8 = chilkatJNI.CkHttp_PText(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/* 1439 */     return var8 == 0L ? null : new CkHttpResponse(var8, true);
/*      */   }
/*      */   
/*      */   public CkTask PTextAsync(String var1, String var2, String var3, String var4, String var5, boolean var6, boolean var7) {
/* 1443 */     long var8 = chilkatJNI.CkHttp_PTextAsync(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/* 1444 */     return var8 == 0L ? null : new CkTask(var8, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse PTextSb(String var1, String var2, CkStringBuilder var3, String var4, String var5, boolean var6, boolean var7) {
/* 1448 */     long var8 = chilkatJNI.CkHttp_PTextSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3, var4, var5, var6, var7);
/* 1449 */     return var8 == 0L ? null : new CkHttpResponse(var8, true);
/*      */   }
/*      */   
/*      */   public CkTask PTextSbAsync(String var1, String var2, CkStringBuilder var3, String var4, String var5, boolean var6, boolean var7) {
/* 1453 */     long var8 = chilkatJNI.CkHttp_PTextSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3, var4, var5, var6, var7);
/* 1454 */     return var8 == 0L ? null : new CkTask(var8, true);
/*      */   }
/*      */   
/*      */   public boolean PutBinary(String var1, CkByteData var2, String var3, boolean var4, boolean var5, CkString var6) {
/* 1458 */     return chilkatJNI.CkHttp_PutBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public String putBinary(String var1, CkByteData var2, String var3, boolean var4, boolean var5) {
/* 1462 */     return chilkatJNI.CkHttp_putBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5);
/*      */   }
/*      */   
/*      */   public CkTask PutBinaryAsync(String var1, CkByteData var2, String var3, boolean var4, boolean var5) {
/* 1466 */     long var6 = chilkatJNI.CkHttp_PutBinaryAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3, var4, var5);
/* 1467 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public boolean PutText(String var1, String var2, String var3, String var4, boolean var5, boolean var6, CkString var7) {
/* 1471 */     return chilkatJNI.CkHttp_PutText(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, CkString.getCPtr(var7), var7);
/*      */   }
/*      */   
/*      */   public String putText(String var1, String var2, String var3, String var4, boolean var5, boolean var6) {
/* 1475 */     return chilkatJNI.CkHttp_putText(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*      */   }
/*      */   
/*      */   public CkTask PutTextAsync(String var1, String var2, String var3, String var4, boolean var5, boolean var6) {
/* 1479 */     long var7 = chilkatJNI.CkHttp_PutTextAsync(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/* 1480 */     return var7 == 0L ? null : new CkTask(var7, true);
/*      */   }
/*      */   
/*      */   public boolean QuickDeleteStr(String var1, CkString var2) {
/* 1484 */     return chilkatJNI.CkHttp_QuickDeleteStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String quickDeleteStr(String var1) {
/* 1488 */     return chilkatJNI.CkHttp_quickDeleteStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask QuickDeleteStrAsync(String var1) {
/* 1492 */     long var2 = chilkatJNI.CkHttp_QuickDeleteStrAsync(this.swigCPtr, this, var1);
/* 1493 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean QuickGet(String var1, CkByteData var2) {
/* 1497 */     return chilkatJNI.CkHttp_QuickGet(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask QuickGetAsync(String var1) {
/* 1501 */     long var2 = chilkatJNI.CkHttp_QuickGetAsync(this.swigCPtr, this, var1);
/* 1502 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean QuickGetBd(String var1, CkBinData var2) {
/* 1506 */     return chilkatJNI.CkHttp_QuickGetBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask QuickGetBdAsync(String var1, CkBinData var2) {
/* 1510 */     long var3 = chilkatJNI.CkHttp_QuickGetBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/* 1511 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse QuickGetObj(String var1) {
/* 1515 */     long var2 = chilkatJNI.CkHttp_QuickGetObj(this.swigCPtr, this, var1);
/* 1516 */     return var2 == 0L ? null : new CkHttpResponse(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask QuickGetObjAsync(String var1) {
/* 1520 */     long var2 = chilkatJNI.CkHttp_QuickGetObjAsync(this.swigCPtr, this, var1);
/* 1521 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean QuickGetSb(String var1, CkStringBuilder var2) {
/* 1525 */     return chilkatJNI.CkHttp_QuickGetSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask QuickGetSbAsync(String var1, CkStringBuilder var2) {
/* 1529 */     long var3 = chilkatJNI.CkHttp_QuickGetSbAsync(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/* 1530 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean QuickGetStr(String var1, CkString var2) {
/* 1534 */     return chilkatJNI.CkHttp_QuickGetStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String quickGetStr(String var1) {
/* 1538 */     return chilkatJNI.CkHttp_quickGetStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask QuickGetStrAsync(String var1) {
/* 1542 */     long var2 = chilkatJNI.CkHttp_QuickGetStrAsync(this.swigCPtr, this, var1);
/* 1543 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean QuickPutStr(String var1, CkString var2) {
/* 1547 */     return chilkatJNI.CkHttp_QuickPutStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String quickPutStr(String var1) {
/* 1551 */     return chilkatJNI.CkHttp_quickPutStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask QuickPutStrAsync(String var1) {
/* 1555 */     long var2 = chilkatJNI.CkHttp_QuickPutStrAsync(this.swigCPtr, this, var1);
/* 1556 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RemoveQuickHeader(String var1) {
/* 1560 */     return chilkatJNI.CkHttp_RemoveQuickHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void RemoveRequestHeader(String var1) {
/* 1564 */     chilkatJNI.CkHttp_RemoveRequestHeader(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean RenderGet(String var1, CkString var2) {
/* 1568 */     return chilkatJNI.CkHttp_RenderGet(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String renderGet(String var1) {
/* 1572 */     return chilkatJNI.CkHttp_renderGet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean ResumeDownload(String var1, String var2) {
/* 1576 */     return chilkatJNI.CkHttp_ResumeDownload(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask ResumeDownloadAsync(String var1, String var2) {
/* 1580 */     long var3 = chilkatJNI.CkHttp_ResumeDownloadAsync(this.swigCPtr, this, var1, var2);
/* 1581 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ResumeDownloadBd(String var1, CkBinData var2) {
/* 1585 */     return chilkatJNI.CkHttp_ResumeDownloadBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask ResumeDownloadBdAsync(String var1, CkBinData var2) {
/* 1589 */     long var3 = chilkatJNI.CkHttp_ResumeDownloadBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/* 1590 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean S3_CreateBucket(String var1) {
/* 1594 */     return chilkatJNI.CkHttp_S3_CreateBucket(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask S3_CreateBucketAsync(String var1) {
/* 1598 */     long var2 = chilkatJNI.CkHttp_S3_CreateBucketAsync(this.swigCPtr, this, var1);
/* 1599 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean S3_DeleteBucket(String var1) {
/* 1603 */     return chilkatJNI.CkHttp_S3_DeleteBucket(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask S3_DeleteBucketAsync(String var1) {
/* 1607 */     long var2 = chilkatJNI.CkHttp_S3_DeleteBucketAsync(this.swigCPtr, this, var1);
/* 1608 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkHttpResponse S3_DeleteMultipleObjects(String var1, CkStringArray var2) {
/* 1612 */     long var3 = chilkatJNI.CkHttp_S3_DeleteMultipleObjects(this.swigCPtr, this, var1, CkStringArray.getCPtr(var2), var2);
/* 1613 */     return var3 == 0L ? null : new CkHttpResponse(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask S3_DeleteMultipleObjectsAsync(String var1, CkStringArray var2) {
/* 1617 */     long var3 = chilkatJNI.CkHttp_S3_DeleteMultipleObjectsAsync(this.swigCPtr, this, var1, CkStringArray.getCPtr(var2), var2);
/* 1618 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean S3_DeleteObject(String var1, String var2) {
/* 1622 */     return chilkatJNI.CkHttp_S3_DeleteObject(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask S3_DeleteObjectAsync(String var1, String var2) {
/* 1626 */     long var3 = chilkatJNI.CkHttp_S3_DeleteObjectAsync(this.swigCPtr, this, var1, var2);
/* 1627 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean S3_DownloadBytes(String var1, String var2, CkByteData var3) {
/* 1631 */     return chilkatJNI.CkHttp_S3_DownloadBytes(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask S3_DownloadBytesAsync(String var1, String var2) {
/* 1635 */     long var3 = chilkatJNI.CkHttp_S3_DownloadBytesAsync(this.swigCPtr, this, var1, var2);
/* 1636 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean S3_DownloadFile(String var1, String var2, String var3) {
/* 1640 */     return chilkatJNI.CkHttp_S3_DownloadFile(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask S3_DownloadFileAsync(String var1, String var2, String var3) {
/* 1644 */     long var4 = chilkatJNI.CkHttp_S3_DownloadFileAsync(this.swigCPtr, this, var1, var2, var3);
/* 1645 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean S3_DownloadString(String var1, String var2, String var3, CkString var4) {
/* 1649 */     return chilkatJNI.CkHttp_S3_DownloadString(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String s3_DownloadString(String var1, String var2, String var3) {
/* 1653 */     return chilkatJNI.CkHttp_s3_DownloadString(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask S3_DownloadStringAsync(String var1, String var2, String var3) {
/* 1657 */     long var4 = chilkatJNI.CkHttp_S3_DownloadStringAsync(this.swigCPtr, this, var1, var2, var3);
/* 1658 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public int S3_FileExists(String var1, String var2) {
/* 1662 */     return chilkatJNI.CkHttp_S3_FileExists(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask S3_FileExistsAsync(String var1, String var2) {
/* 1666 */     long var3 = chilkatJNI.CkHttp_S3_FileExistsAsync(this.swigCPtr, this, var1, var2);
/* 1667 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean S3_GenerateUrl(String var1, String var2, CkDateTime var3, CkString var4) {
/* 1671 */     return chilkatJNI.CkHttp_S3_GenerateUrl(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String s3_GenerateUrl(String var1, String var2, CkDateTime var3) {
/* 1675 */     return chilkatJNI.CkHttp_s3_GenerateUrl(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean S3_GenerateUrlV4(boolean var1, String var2, String var3, int var4, String var5, CkString var6) {
/* 1679 */     return chilkatJNI.CkHttp_S3_GenerateUrlV4(this.swigCPtr, this, var1, var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public String s3_GenerateUrlV4(boolean var1, String var2, String var3, int var4, String var5) {
/* 1683 */     return chilkatJNI.CkHttp_s3_GenerateUrlV4(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*      */   }
/*      */   
/*      */   public boolean S3_ListBucketObjects(String var1, CkString var2) {
/* 1687 */     return chilkatJNI.CkHttp_S3_ListBucketObjects(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String s3_ListBucketObjects(String var1) {
/* 1691 */     return chilkatJNI.CkHttp_s3_ListBucketObjects(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask S3_ListBucketObjectsAsync(String var1) {
/* 1695 */     long var2 = chilkatJNI.CkHttp_S3_ListBucketObjectsAsync(this.swigCPtr, this, var1);
/* 1696 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean S3_ListBuckets(CkString var1) {
/* 1700 */     return chilkatJNI.CkHttp_S3_ListBuckets(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String s3_ListBuckets() {
/* 1704 */     return chilkatJNI.CkHttp_s3_ListBuckets(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask S3_ListBucketsAsync() {
/* 1708 */     long var1 = chilkatJNI.CkHttp_S3_ListBucketsAsync(this.swigCPtr, this);
/* 1709 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean S3_UploadBytes(CkByteData var1, String var2, String var3, String var4) {
/* 1713 */     return chilkatJNI.CkHttp_S3_UploadBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask S3_UploadBytesAsync(CkByteData var1, String var2, String var3, String var4) {
/* 1717 */     long var5 = chilkatJNI.CkHttp_S3_UploadBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, var3, var4);
/* 1718 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean S3_UploadFile(String var1, String var2, String var3, String var4) {
/* 1722 */     return chilkatJNI.CkHttp_S3_UploadFile(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask S3_UploadFileAsync(String var1, String var2, String var3, String var4) {
/* 1726 */     long var5 = chilkatJNI.CkHttp_S3_UploadFileAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1727 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean S3_UploadString(String var1, String var2, String var3, String var4, String var5) {
/* 1731 */     return chilkatJNI.CkHttp_S3_UploadString(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*      */   }
/*      */   
/*      */   public CkTask S3_UploadStringAsync(String var1, String var2, String var3, String var4, String var5) {
/* 1735 */     long var6 = chilkatJNI.CkHttp_S3_UploadStringAsync(this.swigCPtr, this, var1, var2, var3, var4, var5);
/* 1736 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1740 */     return chilkatJNI.CkHttp_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetCookieXml(String var1, String var2) {
/* 1744 */     return chilkatJNI.CkHttp_SetCookieXml(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetOAuthRsaKey(CkPrivateKey var1) {
/* 1748 */     return chilkatJNI.CkHttp_SetOAuthRsaKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetPassword(CkSecureString var1) {
/* 1752 */     return chilkatJNI.CkHttp_SetPassword(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void SetRequestHeader(String var1, String var2) {
/* 1756 */     chilkatJNI.CkHttp_SetRequestHeader(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCert(CkCert var1) {
/* 1760 */     return chilkatJNI.CkHttp_SetSslClientCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPem(String var1, String var2) {
/* 1764 */     return chilkatJNI.CkHttp_SetSslClientCertPem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPfx(String var1, String var2) {
/* 1768 */     return chilkatJNI.CkHttp_SetSslClientCertPfx(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetUrlVar(String var1, String var2) {
/* 1772 */     return chilkatJNI.CkHttp_SetUrlVar(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SharePointOnlineAuth(String var1, String var2, CkSecureString var3, CkJsonObject var4) {
/* 1776 */     return chilkatJNI.CkHttp_SharePointOnlineAuth(this.swigCPtr, this, var1, var2, CkSecureString.getCPtr(var3), var3, CkJsonObject.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public CkTask SharePointOnlineAuthAsync(String var1, String var2, CkSecureString var3, CkJsonObject var4) {
/* 1780 */     long var5 = chilkatJNI.CkHttp_SharePointOnlineAuthAsync(this.swigCPtr, this, var1, var2, CkSecureString.getCPtr(var3), var3, CkJsonObject.getCPtr(var4), var4);
/* 1781 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public void SleepMs(int var1) {
/* 1785 */     chilkatJNI.CkHttp_SleepMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkHttpResponse SynchronousRequest(String var1, int var2, boolean var3, CkHttpRequest var4) {
/* 1789 */     long var5 = chilkatJNI.CkHttp_SynchronousRequest(this.swigCPtr, this, var1, var2, var3, CkHttpRequest.getCPtr(var4), var4);
/* 1790 */     return var5 == 0L ? null : new CkHttpResponse(var5, true);
/*      */   }
/*      */   
/*      */   public CkTask SynchronousRequestAsync(String var1, int var2, boolean var3, CkHttpRequest var4) {
/* 1794 */     long var5 = chilkatJNI.CkHttp_SynchronousRequestAsync(this.swigCPtr, this, var1, var2, var3, CkHttpRequest.getCPtr(var4), var4);
/* 1795 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1799 */     return chilkatJNI.CkHttp_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UrlDecode(String var1, CkString var2) {
/* 1803 */     return chilkatJNI.CkHttp_UrlDecode(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String urlDecode(String var1) {
/* 1807 */     return chilkatJNI.CkHttp_urlDecode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UrlEncode(String var1, CkString var2) {
/* 1811 */     return chilkatJNI.CkHttp_UrlEncode(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String urlEncode(String var1) {
/* 1815 */     return chilkatJNI.CkHttp_urlEncode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int VerifyTimestampReply(CkBinData var1, CkCert var2) {
/* 1819 */     return chilkatJNI.CkHttp_VerifyTimestampReply(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkCert.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean XmlRpc(String var1, String var2, CkString var3) {
/* 1823 */     return chilkatJNI.CkHttp_XmlRpc(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String xmlRpc(String var1, String var2) {
/* 1827 */     return chilkatJNI.CkHttp_xmlRpc(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask XmlRpcAsync(String var1, String var2) {
/* 1831 */     long var3 = chilkatJNI.CkHttp_XmlRpcAsync(this.swigCPtr, this, var1, var2);
/* 1832 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean XmlRpcPut(String var1, String var2, CkString var3) {
/* 1836 */     return chilkatJNI.CkHttp_XmlRpcPut(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String xmlRpcPut(String var1, String var2) {
/* 1840 */     return chilkatJNI.CkHttp_xmlRpcPut(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask XmlRpcPutAsync(String var1, String var2) {
/* 1844 */     long var3 = chilkatJNI.CkHttp_XmlRpcPutAsync(this.swigCPtr, this, var1, var2);
/* 1845 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHttp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */