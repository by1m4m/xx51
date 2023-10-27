/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSpider
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSpider(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSpider var0) {
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
/*  29 */         chilkatJNI.delete_CkSpider(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSpider()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSpider(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkSpider_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkSpider_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkSpider_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkSpider_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkSpider_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkSpider_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_AvoidHttps() {
/*  66 */     return chilkatJNI.CkSpider_get_AvoidHttps(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AvoidHttps(boolean var1) {
/*  70 */     chilkatJNI.CkSpider_put_AvoidHttps(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CacheDir(CkString var1) {
/*  74 */     chilkatJNI.CkSpider_get_CacheDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String cacheDir() {
/*  78 */     return chilkatJNI.CkSpider_cacheDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CacheDir(String var1) {
/*  82 */     chilkatJNI.CkSpider_put_CacheDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ChopAtQuery() {
/*  86 */     return chilkatJNI.CkSpider_get_ChopAtQuery(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ChopAtQuery(boolean var1) {
/*  90 */     chilkatJNI.CkSpider_put_ChopAtQuery(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ConnectTimeout() {
/*  94 */     return chilkatJNI.CkSpider_get_ConnectTimeout(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConnectTimeout(int var1) {
/*  98 */     chilkatJNI.CkSpider_put_ConnectTimeout(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 102 */     chilkatJNI.CkSpider_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 106 */     return chilkatJNI.CkSpider_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 110 */     chilkatJNI.CkSpider_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Domain(CkString var1) {
/* 114 */     chilkatJNI.CkSpider_get_Domain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domain() {
/* 118 */     return chilkatJNI.CkSpider_domain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_FetchFromCache() {
/* 122 */     return chilkatJNI.CkSpider_get_FetchFromCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FetchFromCache(boolean var1) {
/* 126 */     chilkatJNI.CkSpider_put_FetchFromCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 130 */     return chilkatJNI.CkSpider_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 134 */     chilkatJNI.CkSpider_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 138 */     chilkatJNI.CkSpider_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 142 */     return chilkatJNI.CkSpider_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 146 */     chilkatJNI.CkSpider_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 150 */     return chilkatJNI.CkSpider_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 154 */     chilkatJNI.CkSpider_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 158 */     return chilkatJNI.CkSpider_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastFromCache() {
/* 162 */     return chilkatJNI.CkSpider_get_LastFromCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastHtml(CkString var1) {
/* 166 */     chilkatJNI.CkSpider_get_LastHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastHtml() {
/* 170 */     return chilkatJNI.CkSpider_lastHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastHtmlDescription(CkString var1) {
/* 174 */     chilkatJNI.CkSpider_get_LastHtmlDescription(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastHtmlDescription() {
/* 178 */     return chilkatJNI.CkSpider_lastHtmlDescription(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastHtmlKeywords(CkString var1) {
/* 182 */     chilkatJNI.CkSpider_get_LastHtmlKeywords(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastHtmlKeywords() {
/* 186 */     return chilkatJNI.CkSpider_lastHtmlKeywords(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastHtmlTitle(CkString var1) {
/* 190 */     chilkatJNI.CkSpider_get_LastHtmlTitle(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastHtmlTitle() {
/* 194 */     return chilkatJNI.CkSpider_lastHtmlTitle(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 198 */     return chilkatJNI.CkSpider_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 202 */     chilkatJNI.CkSpider_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastModDate(SYSTEMTIME var1) {
/* 206 */     chilkatJNI.CkSpider_get_LastModDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_LastModDateStr(CkString var1) {
/* 210 */     chilkatJNI.CkSpider_get_LastModDateStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastModDateStr() {
/* 214 */     return chilkatJNI.CkSpider_lastModDateStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastUrl(CkString var1) {
/* 218 */     chilkatJNI.CkSpider_get_LastUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastUrl() {
/* 222 */     return chilkatJNI.CkSpider_lastUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_MaxResponseSize() {
/* 226 */     return chilkatJNI.CkSpider_get_MaxResponseSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MaxResponseSize(int var1) {
/* 230 */     chilkatJNI.CkSpider_put_MaxResponseSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_MaxUrlLen() {
/* 234 */     return chilkatJNI.CkSpider_get_MaxUrlLen(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MaxUrlLen(int var1) {
/* 238 */     chilkatJNI.CkSpider_put_MaxUrlLen(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumAvoidPatterns() {
/* 242 */     return chilkatJNI.CkSpider_get_NumAvoidPatterns(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumFailed() {
/* 246 */     return chilkatJNI.CkSpider_get_NumFailed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumOutboundLinks() {
/* 250 */     return chilkatJNI.CkSpider_get_NumOutboundLinks(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumSpidered() {
/* 254 */     return chilkatJNI.CkSpider_get_NumSpidered(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumUnspidered() {
/* 258 */     return chilkatJNI.CkSpider_get_NumUnspidered(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 262 */     return chilkatJNI.CkSpider_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 266 */     chilkatJNI.CkSpider_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyDomain(CkString var1) {
/* 270 */     chilkatJNI.CkSpider_get_ProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyDomain() {
/* 274 */     return chilkatJNI.CkSpider_proxyDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyDomain(String var1) {
/* 278 */     chilkatJNI.CkSpider_put_ProxyDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyLogin(CkString var1) {
/* 282 */     chilkatJNI.CkSpider_get_ProxyLogin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyLogin() {
/* 286 */     return chilkatJNI.CkSpider_proxyLogin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyLogin(String var1) {
/* 290 */     chilkatJNI.CkSpider_put_ProxyLogin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProxyPassword(CkString var1) {
/* 294 */     chilkatJNI.CkSpider_get_ProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String proxyPassword() {
/* 298 */     return chilkatJNI.CkSpider_proxyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyPassword(String var1) {
/* 302 */     chilkatJNI.CkSpider_put_ProxyPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ProxyPort() {
/* 306 */     return chilkatJNI.CkSpider_get_ProxyPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ProxyPort(int var1) {
/* 310 */     chilkatJNI.CkSpider_put_ProxyPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ReadTimeout() {
/* 314 */     return chilkatJNI.CkSpider_get_ReadTimeout(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadTimeout(int var1) {
/* 318 */     chilkatJNI.CkSpider_put_ReadTimeout(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UpdateCache() {
/* 322 */     return chilkatJNI.CkSpider_get_UpdateCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UpdateCache(boolean var1) {
/* 326 */     chilkatJNI.CkSpider_put_UpdateCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UserAgent(CkString var1) {
/* 330 */     chilkatJNI.CkSpider_get_UserAgent(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String userAgent() {
/* 334 */     return chilkatJNI.CkSpider_userAgent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserAgent(String var1) {
/* 338 */     chilkatJNI.CkSpider_put_UserAgent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 342 */     return chilkatJNI.CkSpider_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 346 */     chilkatJNI.CkSpider_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 350 */     chilkatJNI.CkSpider_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 354 */     return chilkatJNI.CkSpider_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_WindDownCount() {
/* 358 */     return chilkatJNI.CkSpider_get_WindDownCount(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WindDownCount(int var1) {
/* 362 */     chilkatJNI.CkSpider_put_WindDownCount(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddAvoidOutboundLinkPattern(String var1) {
/* 366 */     chilkatJNI.CkSpider_AddAvoidOutboundLinkPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddAvoidPattern(String var1) {
/* 370 */     chilkatJNI.CkSpider_AddAvoidPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddMustMatchPattern(String var1) {
/* 374 */     chilkatJNI.CkSpider_AddMustMatchPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddUnspidered(String var1) {
/* 378 */     chilkatJNI.CkSpider_AddUnspidered(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CanonicalizeUrl(String var1, CkString var2) {
/* 382 */     return chilkatJNI.CkSpider_CanonicalizeUrl(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String canonicalizeUrl(String var1) {
/* 386 */     return chilkatJNI.CkSpider_canonicalizeUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void ClearFailedUrls() {
/* 390 */     chilkatJNI.CkSpider_ClearFailedUrls(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearOutboundLinks() {
/* 394 */     chilkatJNI.CkSpider_ClearOutboundLinks(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearSpideredUrls() {
/* 398 */     chilkatJNI.CkSpider_ClearSpideredUrls(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CrawlNext() {
/* 402 */     return chilkatJNI.CkSpider_CrawlNext(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask CrawlNextAsync() {
/* 406 */     long var1 = chilkatJNI.CkSpider_CrawlNextAsync(this.swigCPtr, this);
/* 407 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean FetchRobotsText(CkString var1) {
/* 411 */     return chilkatJNI.CkSpider_FetchRobotsText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fetchRobotsText() {
/* 415 */     return chilkatJNI.CkSpider_fetchRobotsText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask FetchRobotsTextAsync() {
/* 419 */     long var1 = chilkatJNI.CkSpider_FetchRobotsTextAsync(this.swigCPtr, this);
/* 420 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetAvoidPattern(int var1, CkString var2) {
/* 424 */     return chilkatJNI.CkSpider_GetAvoidPattern(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAvoidPattern(int var1) {
/* 428 */     return chilkatJNI.CkSpider_getAvoidPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String avoidPattern(int var1) {
/* 432 */     return chilkatJNI.CkSpider_avoidPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetBaseDomain(String var1, CkString var2) {
/* 436 */     return chilkatJNI.CkSpider_GetBaseDomain(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getBaseDomain(String var1) {
/* 440 */     return chilkatJNI.CkSpider_getBaseDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String baseDomain(String var1) {
/* 444 */     return chilkatJNI.CkSpider_baseDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetFailedUrl(int var1, CkString var2) {
/* 448 */     return chilkatJNI.CkSpider_GetFailedUrl(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getFailedUrl(int var1) {
/* 452 */     return chilkatJNI.CkSpider_getFailedUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String failedUrl(int var1) {
/* 456 */     return chilkatJNI.CkSpider_failedUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetOutboundLink(int var1, CkString var2) {
/* 460 */     return chilkatJNI.CkSpider_GetOutboundLink(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getOutboundLink(int var1) {
/* 464 */     return chilkatJNI.CkSpider_getOutboundLink(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String outboundLink(int var1) {
/* 468 */     return chilkatJNI.CkSpider_outboundLink(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetSpideredUrl(int var1, CkString var2) {
/* 472 */     return chilkatJNI.CkSpider_GetSpideredUrl(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getSpideredUrl(int var1) {
/* 476 */     return chilkatJNI.CkSpider_getSpideredUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String spideredUrl(int var1) {
/* 480 */     return chilkatJNI.CkSpider_spideredUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetUnspideredUrl(int var1, CkString var2) {
/* 484 */     return chilkatJNI.CkSpider_GetUnspideredUrl(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getUnspideredUrl(int var1) {
/* 488 */     return chilkatJNI.CkSpider_getUnspideredUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String unspideredUrl(int var1) {
/* 492 */     return chilkatJNI.CkSpider_unspideredUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetUrlDomain(String var1, CkString var2) {
/* 496 */     return chilkatJNI.CkSpider_GetUrlDomain(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getUrlDomain(String var1) {
/* 500 */     return chilkatJNI.CkSpider_getUrlDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String urlDomain(String var1) {
/* 504 */     return chilkatJNI.CkSpider_urlDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void Initialize(String var1) {
/* 508 */     chilkatJNI.CkSpider_Initialize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RecrawlLast() {
/* 512 */     return chilkatJNI.CkSpider_RecrawlLast(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask RecrawlLastAsync() {
/* 516 */     long var1 = chilkatJNI.CkSpider_RecrawlLastAsync(this.swigCPtr, this);
/* 517 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 521 */     return chilkatJNI.CkSpider_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SkipUnspidered(int var1) {
/* 525 */     chilkatJNI.CkSpider_SkipUnspidered(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SleepMs(int var1) {
/* 529 */     chilkatJNI.CkSpider_SleepMs(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */