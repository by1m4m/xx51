/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkFtp2
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkFtp2(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkFtp2 var0) {
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
/*   29 */         chilkatJNI.delete_CkFtp2(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkFtp2()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkFtp2(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkFtp2_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkFtp2_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkFtp2_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkFtp2Progress var1) {
/*   54 */     chilkatJNI.CkFtp2_put_EventCallbackObject(this.swigCPtr, this, CkFtp2Progress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkFtp2_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkFtp2_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Account(CkString var1) {
/*   66 */     chilkatJNI.CkFtp2_get_Account(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String account() {
/*   70 */     return chilkatJNI.CkFtp2_account(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Account(String var1) {
/*   74 */     chilkatJNI.CkFtp2_put_Account(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ActivePortRangeEnd() {
/*   78 */     return chilkatJNI.CkFtp2_get_ActivePortRangeEnd(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ActivePortRangeEnd(int var1) {
/*   82 */     chilkatJNI.CkFtp2_put_ActivePortRangeEnd(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ActivePortRangeStart() {
/*   86 */     return chilkatJNI.CkFtp2_get_ActivePortRangeStart(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ActivePortRangeStart(int var1) {
/*   90 */     chilkatJNI.CkFtp2_put_ActivePortRangeStart(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public long get_AllocateSize() {
/*   94 */     return chilkatJNI.CkFtp2_get_AllocateSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AllocateSize(long var1) {
/*   98 */     chilkatJNI.CkFtp2_put_AllocateSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AllowMlsd() {
/*  102 */     return chilkatJNI.CkFtp2_get_AllowMlsd(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AllowMlsd(boolean var1) {
/*  106 */     chilkatJNI.CkFtp2_put_AllowMlsd(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public long get_AsyncBytesReceived() {
/*  110 */     return chilkatJNI.CkFtp2_get_AsyncBytesReceived(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncBytesReceivedStr(CkString var1) {
/*  114 */     chilkatJNI.CkFtp2_get_AsyncBytesReceivedStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncBytesReceivedStr() {
/*  118 */     return chilkatJNI.CkFtp2_asyncBytesReceivedStr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public long get_AsyncBytesSent() {
/*  122 */     return chilkatJNI.CkFtp2_get_AsyncBytesSent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncBytesSentStr(CkString var1) {
/*  126 */     chilkatJNI.CkFtp2_get_AsyncBytesSentStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncBytesSentStr() {
/*  130 */     return chilkatJNI.CkFtp2_asyncBytesSentStr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncFinished() {
/*  134 */     return chilkatJNI.CkFtp2_get_AsyncFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncLog(CkString var1) {
/*  138 */     chilkatJNI.CkFtp2_get_AsyncLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncLog() {
/*  142 */     return chilkatJNI.CkFtp2_asyncLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public long get_AsyncPercentDone() {
/*  146 */     return chilkatJNI.CkFtp2_get_AsyncPercentDone(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncSuccess() {
/*  150 */     return chilkatJNI.CkFtp2_get_AsyncSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AuthSsl() {
/*  154 */     return chilkatJNI.CkFtp2_get_AuthSsl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AuthSsl(boolean var1) {
/*  158 */     chilkatJNI.CkFtp2_put_AuthSsl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AuthTls() {
/*  162 */     return chilkatJNI.CkFtp2_get_AuthTls(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AuthTls(boolean var1) {
/*  166 */     chilkatJNI.CkFtp2_put_AuthTls(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoFeat() {
/*  170 */     return chilkatJNI.CkFtp2_get_AutoFeat(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoFeat(boolean var1) {
/*  174 */     chilkatJNI.CkFtp2_put_AutoFeat(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoFix() {
/*  178 */     return chilkatJNI.CkFtp2_get_AutoFix(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoFix(boolean var1) {
/*  182 */     chilkatJNI.CkFtp2_put_AutoFix(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoGetSizeForProgress() {
/*  186 */     return chilkatJNI.CkFtp2_get_AutoGetSizeForProgress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoGetSizeForProgress(boolean var1) {
/*  190 */     chilkatJNI.CkFtp2_put_AutoGetSizeForProgress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoOptsUtf8() {
/*  194 */     return chilkatJNI.CkFtp2_get_AutoOptsUtf8(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoOptsUtf8(boolean var1) {
/*  198 */     chilkatJNI.CkFtp2_put_AutoOptsUtf8(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoSetUseEpsv() {
/*  202 */     return chilkatJNI.CkFtp2_get_AutoSetUseEpsv(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoSetUseEpsv(boolean var1) {
/*  206 */     chilkatJNI.CkFtp2_put_AutoSetUseEpsv(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoSyst() {
/*  210 */     return chilkatJNI.CkFtp2_get_AutoSyst(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoSyst(boolean var1) {
/*  214 */     chilkatJNI.CkFtp2_put_AutoSyst(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoXcrc() {
/*  218 */     return chilkatJNI.CkFtp2_get_AutoXcrc(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoXcrc(boolean var1) {
/*  222 */     chilkatJNI.CkFtp2_put_AutoXcrc(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleDown() {
/*  226 */     return chilkatJNI.CkFtp2_get_BandwidthThrottleDown(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleDown(int var1) {
/*  230 */     chilkatJNI.CkFtp2_put_BandwidthThrottleDown(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleUp() {
/*  234 */     return chilkatJNI.CkFtp2_get_BandwidthThrottleUp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleUp(int var1) {
/*  238 */     chilkatJNI.CkFtp2_put_BandwidthThrottleUp(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  242 */     chilkatJNI.CkFtp2_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  246 */     return chilkatJNI.CkFtp2_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  250 */     chilkatJNI.CkFtp2_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CommandCharset(CkString var1) {
/*  254 */     chilkatJNI.CkFtp2_get_CommandCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String commandCharset() {
/*  258 */     return chilkatJNI.CkFtp2_commandCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CommandCharset(String var1) {
/*  262 */     chilkatJNI.CkFtp2_put_CommandCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectFailReason() {
/*  266 */     return chilkatJNI.CkFtp2_get_ConnectFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_ConnectTimeout() {
/*  270 */     return chilkatJNI.CkFtp2_get_ConnectTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ConnectTimeout(int var1) {
/*  274 */     chilkatJNI.CkFtp2_put_ConnectTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ConnectVerified() {
/*  278 */     return chilkatJNI.CkFtp2_get_ConnectVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_CrlfMode() {
/*  282 */     return chilkatJNI.CkFtp2_get_CrlfMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CrlfMode(int var1) {
/*  286 */     chilkatJNI.CkFtp2_put_CrlfMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DataProtection(CkString var1) {
/*  290 */     chilkatJNI.CkFtp2_get_DataProtection(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dataProtection() {
/*  294 */     return chilkatJNI.CkFtp2_dataProtection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DataProtection(String var1) {
/*  298 */     chilkatJNI.CkFtp2_put_DataProtection(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  302 */     chilkatJNI.CkFtp2_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  306 */     return chilkatJNI.CkFtp2_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  310 */     chilkatJNI.CkFtp2_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DirListingCharset(CkString var1) {
/*  314 */     chilkatJNI.CkFtp2_get_DirListingCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dirListingCharset() {
/*  318 */     return chilkatJNI.CkFtp2_dirListingCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DirListingCharset(String var1) {
/*  322 */     chilkatJNI.CkFtp2_put_DirListingCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_DownloadTransferRate() {
/*  326 */     return chilkatJNI.CkFtp2_get_DownloadTransferRate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_ForcePortIpAddress(CkString var1) {
/*  330 */     chilkatJNI.CkFtp2_get_ForcePortIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String forcePortIpAddress() {
/*  334 */     return chilkatJNI.CkFtp2_forcePortIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ForcePortIpAddress(String var1) {
/*  338 */     chilkatJNI.CkFtp2_put_ForcePortIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Greeting(CkString var1) {
/*  342 */     chilkatJNI.CkFtp2_get_Greeting(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String greeting() {
/*  346 */     return chilkatJNI.CkFtp2_greeting(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_HasModeZ() {
/*  350 */     return chilkatJNI.CkFtp2_get_HasModeZ(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  354 */     return chilkatJNI.CkFtp2_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  358 */     chilkatJNI.CkFtp2_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Hostname(CkString var1) {
/*  362 */     chilkatJNI.CkFtp2_get_Hostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String hostname() {
/*  366 */     return chilkatJNI.CkFtp2_hostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Hostname(String var1) {
/*  370 */     chilkatJNI.CkFtp2_put_Hostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyAuthMethod(CkString var1) {
/*  374 */     chilkatJNI.CkFtp2_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyAuthMethod() {
/*  378 */     return chilkatJNI.CkFtp2_httpProxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyAuthMethod(String var1) {
/*  382 */     chilkatJNI.CkFtp2_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyDomain(CkString var1) {
/*  386 */     chilkatJNI.CkFtp2_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyDomain() {
/*  390 */     return chilkatJNI.CkFtp2_httpProxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyDomain(String var1) {
/*  394 */     chilkatJNI.CkFtp2_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyHostname(CkString var1) {
/*  398 */     chilkatJNI.CkFtp2_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyHostname() {
/*  402 */     return chilkatJNI.CkFtp2_httpProxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyHostname(String var1) {
/*  406 */     chilkatJNI.CkFtp2_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyPassword(CkString var1) {
/*  410 */     chilkatJNI.CkFtp2_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyPassword() {
/*  414 */     return chilkatJNI.CkFtp2_httpProxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPassword(String var1) {
/*  418 */     chilkatJNI.CkFtp2_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HttpProxyPort() {
/*  422 */     return chilkatJNI.CkFtp2_get_HttpProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPort(int var1) {
/*  426 */     chilkatJNI.CkFtp2_put_HttpProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyUsername(CkString var1) {
/*  430 */     chilkatJNI.CkFtp2_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyUsername() {
/*  434 */     return chilkatJNI.CkFtp2_httpProxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyUsername(String var1) {
/*  438 */     chilkatJNI.CkFtp2_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_IdleTimeoutMs() {
/*  442 */     return chilkatJNI.CkFtp2_get_IdleTimeoutMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IdleTimeoutMs(int var1) {
/*  446 */     chilkatJNI.CkFtp2_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IsConnected() {
/*  450 */     return chilkatJNI.CkFtp2_get_IsConnected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_KeepSessionLog() {
/*  454 */     return chilkatJNI.CkFtp2_get_KeepSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepSessionLog(boolean var1) {
/*  458 */     chilkatJNI.CkFtp2_put_KeepSessionLog(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_LargeFileMeasures() {
/*  462 */     return chilkatJNI.CkFtp2_get_LargeFileMeasures(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LargeFileMeasures(boolean var1) {
/*  466 */     chilkatJNI.CkFtp2_put_LargeFileMeasures(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  470 */     chilkatJNI.CkFtp2_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  474 */     return chilkatJNI.CkFtp2_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  478 */     chilkatJNI.CkFtp2_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  482 */     return chilkatJNI.CkFtp2_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  486 */     chilkatJNI.CkFtp2_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  490 */     return chilkatJNI.CkFtp2_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  494 */     return chilkatJNI.CkFtp2_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  498 */     chilkatJNI.CkFtp2_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastReply(CkString var1) {
/*  502 */     chilkatJNI.CkFtp2_get_LastReply(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastReply() {
/*  506 */     return chilkatJNI.CkFtp2_lastReply(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_ListPattern(CkString var1) {
/*  510 */     chilkatJNI.CkFtp2_get_ListPattern(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String listPattern() {
/*  514 */     return chilkatJNI.CkFtp2_listPattern(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ListPattern(String var1) {
/*  518 */     chilkatJNI.CkFtp2_put_ListPattern(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_LoginVerified() {
/*  522 */     return chilkatJNI.CkFtp2_get_LoginVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumFilesAndDirs() {
/*  526 */     return chilkatJNI.CkFtp2_get_NumFilesAndDirs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_PartialTransfer() {
/*  530 */     return chilkatJNI.CkFtp2_get_PartialTransfer(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_Passive() {
/*  534 */     return chilkatJNI.CkFtp2_get_Passive(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Passive(boolean var1) {
/*  538 */     chilkatJNI.CkFtp2_put_Passive(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PassiveUseHostAddr() {
/*  542 */     return chilkatJNI.CkFtp2_get_PassiveUseHostAddr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PassiveUseHostAddr(boolean var1) {
/*  546 */     chilkatJNI.CkFtp2_put_PassiveUseHostAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Password(CkString var1) {
/*  550 */     chilkatJNI.CkFtp2_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String password() {
/*  554 */     return chilkatJNI.CkFtp2_password(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Password(String var1) {
/*  558 */     chilkatJNI.CkFtp2_put_Password(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  562 */     return chilkatJNI.CkFtp2_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  566 */     chilkatJNI.CkFtp2_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Port() {
/*  570 */     return chilkatJNI.CkFtp2_get_Port(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Port(int var1) {
/*  574 */     chilkatJNI.CkFtp2_put_Port(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  578 */     return chilkatJNI.CkFtp2_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  582 */     chilkatJNI.CkFtp2_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferNlst() {
/*  586 */     return chilkatJNI.CkFtp2_get_PreferNlst(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferNlst(boolean var1) {
/*  590 */     chilkatJNI.CkFtp2_put_PreferNlst(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ProgressMonSize() {
/*  594 */     return chilkatJNI.CkFtp2_get_ProgressMonSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProgressMonSize(int var1) {
/*  598 */     chilkatJNI.CkFtp2_put_ProgressMonSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyHostname(CkString var1) {
/*  602 */     chilkatJNI.CkFtp2_get_ProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyHostname() {
/*  606 */     return chilkatJNI.CkFtp2_proxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyHostname(String var1) {
/*  610 */     chilkatJNI.CkFtp2_put_ProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ProxyMethod() {
/*  614 */     return chilkatJNI.CkFtp2_get_ProxyMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyMethod(int var1) {
/*  618 */     chilkatJNI.CkFtp2_put_ProxyMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyPassword(CkString var1) {
/*  622 */     chilkatJNI.CkFtp2_get_ProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyPassword() {
/*  626 */     return chilkatJNI.CkFtp2_proxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyPassword(String var1) {
/*  630 */     chilkatJNI.CkFtp2_put_ProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ProxyPort() {
/*  634 */     return chilkatJNI.CkFtp2_get_ProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyPort(int var1) {
/*  638 */     chilkatJNI.CkFtp2_put_ProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ProxyUsername(CkString var1) {
/*  642 */     chilkatJNI.CkFtp2_get_ProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String proxyUsername() {
/*  646 */     return chilkatJNI.CkFtp2_proxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ProxyUsername(String var1) {
/*  650 */     chilkatJNI.CkFtp2_put_ProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReadTimeout() {
/*  654 */     return chilkatJNI.CkFtp2_get_ReadTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadTimeout(int var1) {
/*  658 */     chilkatJNI.CkFtp2_put_ReadTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_RequireSslCertVerify() {
/*  662 */     return chilkatJNI.CkFtp2_get_RequireSslCertVerify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequireSslCertVerify(boolean var1) {
/*  666 */     chilkatJNI.CkFtp2_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_RestartNext() {
/*  670 */     return chilkatJNI.CkFtp2_get_RestartNext(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RestartNext(boolean var1) {
/*  674 */     chilkatJNI.CkFtp2_put_RestartNext(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SendBufferSize() {
/*  678 */     return chilkatJNI.CkFtp2_get_SendBufferSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendBufferSize(int var1) {
/*  682 */     chilkatJNI.CkFtp2_put_SendBufferSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SessionLog(CkString var1) {
/*  686 */     chilkatJNI.CkFtp2_get_SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLog() {
/*  690 */     return chilkatJNI.CkFtp2_sessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_SkipFinalReply() {
/*  694 */     return chilkatJNI.CkFtp2_get_SkipFinalReply(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SkipFinalReply(boolean var1) {
/*  698 */     chilkatJNI.CkFtp2_put_SkipFinalReply(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  702 */     chilkatJNI.CkFtp2_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  706 */     return chilkatJNI.CkFtp2_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  710 */     chilkatJNI.CkFtp2_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  714 */     chilkatJNI.CkFtp2_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  718 */     return chilkatJNI.CkFtp2_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  722 */     chilkatJNI.CkFtp2_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  726 */     return chilkatJNI.CkFtp2_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  730 */     chilkatJNI.CkFtp2_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  734 */     chilkatJNI.CkFtp2_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  738 */     return chilkatJNI.CkFtp2_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  742 */     chilkatJNI.CkFtp2_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  746 */     return chilkatJNI.CkFtp2_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  750 */     chilkatJNI.CkFtp2_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  754 */     return chilkatJNI.CkFtp2_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  758 */     chilkatJNI.CkFtp2_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  762 */     return chilkatJNI.CkFtp2_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  766 */     chilkatJNI.CkFtp2_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_Ssl() {
/*  770 */     return chilkatJNI.CkFtp2_get_Ssl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Ssl(boolean var1) {
/*  774 */     chilkatJNI.CkFtp2_put_Ssl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslAllowedCiphers(CkString var1) {
/*  778 */     chilkatJNI.CkFtp2_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslAllowedCiphers() {
/*  782 */     return chilkatJNI.CkFtp2_sslAllowedCiphers(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslAllowedCiphers(String var1) {
/*  786 */     chilkatJNI.CkFtp2_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslProtocol(CkString var1) {
/*  790 */     chilkatJNI.CkFtp2_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslProtocol() {
/*  794 */     return chilkatJNI.CkFtp2_sslProtocol(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslProtocol(String var1) {
/*  798 */     chilkatJNI.CkFtp2_put_SslProtocol(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SslServerCertVerified() {
/*  802 */     return chilkatJNI.CkFtp2_get_SslServerCertVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SyncedFiles(CkString var1) {
/*  806 */     chilkatJNI.CkFtp2_get_SyncedFiles(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncedFiles() {
/*  810 */     return chilkatJNI.CkFtp2_syncedFiles(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncedFiles(String var1) {
/*  814 */     chilkatJNI.CkFtp2_put_SyncedFiles(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncMustMatch(CkString var1) {
/*  818 */     chilkatJNI.CkFtp2_get_SyncMustMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncMustMatch() {
/*  822 */     return chilkatJNI.CkFtp2_syncMustMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncMustMatch(String var1) {
/*  826 */     chilkatJNI.CkFtp2_put_SyncMustMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncMustNotMatch(CkString var1) {
/*  830 */     chilkatJNI.CkFtp2_get_SyncMustNotMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncMustNotMatch() {
/*  834 */     return chilkatJNI.CkFtp2_syncMustNotMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncMustNotMatch(String var1) {
/*  838 */     chilkatJNI.CkFtp2_put_SyncMustNotMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncPreview(CkString var1) {
/*  842 */     chilkatJNI.CkFtp2_get_SyncPreview(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncPreview() {
/*  846 */     return chilkatJNI.CkFtp2_syncPreview(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsCipherSuite(CkString var1) {
/*  850 */     chilkatJNI.CkFtp2_get_TlsCipherSuite(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsCipherSuite() {
/*  854 */     return chilkatJNI.CkFtp2_tlsCipherSuite(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsPinSet(CkString var1) {
/*  858 */     chilkatJNI.CkFtp2_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsPinSet() {
/*  862 */     return chilkatJNI.CkFtp2_tlsPinSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TlsPinSet(String var1) {
/*  866 */     chilkatJNI.CkFtp2_put_TlsPinSet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsVersion(CkString var1) {
/*  870 */     chilkatJNI.CkFtp2_get_TlsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsVersion() {
/*  874 */     return chilkatJNI.CkFtp2_tlsVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_UploadTransferRate() {
/*  878 */     return chilkatJNI.CkFtp2_get_UploadTransferRate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_UseEpsv() {
/*  882 */     return chilkatJNI.CkFtp2_get_UseEpsv(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UseEpsv(boolean var1) {
/*  886 */     chilkatJNI.CkFtp2_put_UseEpsv(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Username(CkString var1) {
/*  890 */     chilkatJNI.CkFtp2_get_Username(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String username() {
/*  894 */     return chilkatJNI.CkFtp2_username(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Username(String var1) {
/*  898 */     chilkatJNI.CkFtp2_put_Username(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  902 */     return chilkatJNI.CkFtp2_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  906 */     chilkatJNI.CkFtp2_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  910 */     chilkatJNI.CkFtp2_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  914 */     return chilkatJNI.CkFtp2_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AppendFile(String var1, String var2) {
/*  918 */     return chilkatJNI.CkFtp2_AppendFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask AppendFileAsync(String var1, String var2) {
/*  922 */     long var3 = chilkatJNI.CkFtp2_AppendFileAsync(this.swigCPtr, this, var1, var2);
/*  923 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AppendFileFromBinaryData(String var1, CkByteData var2) {
/*  927 */     return chilkatJNI.CkFtp2_AppendFileFromBinaryData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask AppendFileFromBinaryDataAsync(String var1, CkByteData var2) {
/*  931 */     long var3 = chilkatJNI.CkFtp2_AppendFileFromBinaryDataAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*  932 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AppendFileFromTextData(String var1, String var2, String var3) {
/*  936 */     return chilkatJNI.CkFtp2_AppendFileFromTextData(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask AppendFileFromTextDataAsync(String var1, String var2, String var3) {
/*  940 */     long var4 = chilkatJNI.CkFtp2_AppendFileFromTextDataAsync(this.swigCPtr, this, var1, var2, var3);
/*  941 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public void AsyncAbort() {
/*  945 */     chilkatJNI.CkFtp2_AsyncAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncAppendFileStart(String var1, String var2) {
/*  949 */     return chilkatJNI.CkFtp2_AsyncAppendFileStart(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AsyncGetFileStart(String var1, String var2) {
/*  953 */     return chilkatJNI.CkFtp2_AsyncGetFileStart(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AsyncPutFileStart(String var1, String var2) {
/*  957 */     return chilkatJNI.CkFtp2_AsyncPutFileStart(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean ChangeRemoteDir(String var1) {
/*  961 */     return chilkatJNI.CkFtp2_ChangeRemoteDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ChangeRemoteDirAsync(String var1) {
/*  965 */     long var2 = chilkatJNI.CkFtp2_ChangeRemoteDirAsync(this.swigCPtr, this, var1);
/*  966 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean CheckConnection() {
/*  970 */     return chilkatJNI.CkFtp2_CheckConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask CheckConnectionAsync() {
/*  974 */     long var1 = chilkatJNI.CkFtp2_CheckConnectionAsync(this.swigCPtr, this);
/*  975 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ClearControlChannel() {
/*  979 */     return chilkatJNI.CkFtp2_ClearControlChannel(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ClearControlChannelAsync() {
/*  983 */     long var1 = chilkatJNI.CkFtp2_ClearControlChannelAsync(this.swigCPtr, this);
/*  984 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public void ClearDirCache() {
/*  988 */     chilkatJNI.CkFtp2_ClearDirCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearSessionLog() {
/*  992 */     chilkatJNI.CkFtp2_ClearSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean Connect() {
/*  996 */     return chilkatJNI.CkFtp2_Connect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ConnectAsync() {
/* 1000 */     long var1 = chilkatJNI.CkFtp2_ConnectAsync(this.swigCPtr, this);
/* 1001 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ConnectOnly() {
/* 1005 */     return chilkatJNI.CkFtp2_ConnectOnly(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ConnectOnlyAsync() {
/* 1009 */     long var1 = chilkatJNI.CkFtp2_ConnectOnlyAsync(this.swigCPtr, this);
/* 1010 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ConvertToTls() {
/* 1014 */     return chilkatJNI.CkFtp2_ConvertToTls(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ConvertToTlsAsync() {
/* 1018 */     long var1 = chilkatJNI.CkFtp2_ConvertToTlsAsync(this.swigCPtr, this);
/* 1019 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean CreatePlan(String var1, CkString var2) {
/* 1023 */     return chilkatJNI.CkFtp2_CreatePlan(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String createPlan(String var1) {
/* 1027 */     return chilkatJNI.CkFtp2_createPlan(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CreatePlanAsync(String var1) {
/* 1031 */     long var2 = chilkatJNI.CkFtp2_CreatePlanAsync(this.swigCPtr, this, var1);
/* 1032 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean CreateRemoteDir(String var1) {
/* 1036 */     return chilkatJNI.CkFtp2_CreateRemoteDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CreateRemoteDirAsync(String var1) {
/* 1040 */     long var2 = chilkatJNI.CkFtp2_CreateRemoteDirAsync(this.swigCPtr, this, var1);
/* 1041 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public int DeleteMatching(String var1) {
/* 1045 */     return chilkatJNI.CkFtp2_DeleteMatching(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteMatchingAsync(String var1) {
/* 1049 */     long var2 = chilkatJNI.CkFtp2_DeleteMatchingAsync(this.swigCPtr, this, var1);
/* 1050 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteRemoteFile(String var1) {
/* 1054 */     return chilkatJNI.CkFtp2_DeleteRemoteFile(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteRemoteFileAsync(String var1) {
/* 1058 */     long var2 = chilkatJNI.CkFtp2_DeleteRemoteFileAsync(this.swigCPtr, this, var1);
/* 1059 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteTree() {
/* 1063 */     return chilkatJNI.CkFtp2_DeleteTree(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DeleteTreeAsync() {
/* 1067 */     long var1 = chilkatJNI.CkFtp2_DeleteTreeAsync(this.swigCPtr, this);
/* 1068 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public int DetermineProxyMethod() {
/* 1072 */     return chilkatJNI.CkFtp2_DetermineProxyMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DetermineProxyMethodAsync() {
/* 1076 */     long var1 = chilkatJNI.CkFtp2_DetermineProxyMethodAsync(this.swigCPtr, this);
/* 1077 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean DetermineSettings(CkString var1) {
/* 1081 */     return chilkatJNI.CkFtp2_DetermineSettings(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String determineSettings() {
/* 1085 */     return chilkatJNI.CkFtp2_determineSettings(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DetermineSettingsAsync() {
/* 1089 */     long var1 = chilkatJNI.CkFtp2_DetermineSettingsAsync(this.swigCPtr, this);
/* 1090 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean DirTreeXml(CkString var1) {
/* 1094 */     return chilkatJNI.CkFtp2_DirTreeXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dirTreeXml() {
/* 1098 */     return chilkatJNI.CkFtp2_dirTreeXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DirTreeXmlAsync() {
/* 1102 */     long var1 = chilkatJNI.CkFtp2_DirTreeXmlAsync(this.swigCPtr, this);
/* 1103 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Disconnect() {
/* 1107 */     return chilkatJNI.CkFtp2_Disconnect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DisconnectAsync() {
/* 1111 */     long var1 = chilkatJNI.CkFtp2_DisconnectAsync(this.swigCPtr, this);
/* 1112 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadTree(String var1) {
/* 1116 */     return chilkatJNI.CkFtp2_DownloadTree(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DownloadTreeAsync(String var1) {
/* 1120 */     long var2 = chilkatJNI.CkFtp2_DownloadTreeAsync(this.swigCPtr, this, var1);
/* 1121 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Feat(CkString var1) {
/* 1125 */     return chilkatJNI.CkFtp2_Feat(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String feat() {
/* 1129 */     return chilkatJNI.CkFtp2_feat(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask FeatAsync() {
/* 1133 */     long var1 = chilkatJNI.CkFtp2_FeatAsync(this.swigCPtr, this);
/* 1134 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetCreateDt(int var1) {
/* 1138 */     long var2 = chilkatJNI.CkFtp2_GetCreateDt(this.swigCPtr, this, var1);
/* 1139 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetCreateDtAsync(int var1) {
/* 1143 */     long var2 = chilkatJNI.CkFtp2_GetCreateDtAsync(this.swigCPtr, this, var1);
/* 1144 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetCreateDtByName(String var1) {
/* 1148 */     long var2 = chilkatJNI.CkFtp2_GetCreateDtByName(this.swigCPtr, this, var1);
/* 1149 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetCreateDtByNameAsync(String var1) {
/* 1153 */     long var2 = chilkatJNI.CkFtp2_GetCreateDtByNameAsync(this.swigCPtr, this, var1);
/* 1154 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetCreateTime(int var1, SYSTEMTIME var2) {
/* 1158 */     return chilkatJNI.CkFtp2_GetCreateTime(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetCreateTimeByName(String var1, SYSTEMTIME var2) {
/* 1162 */     return chilkatJNI.CkFtp2_GetCreateTimeByName(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetCreateTimeByNameStr(String var1, CkString var2) {
/* 1166 */     return chilkatJNI.CkFtp2_GetCreateTimeByNameStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCreateTimeByNameStr(String var1) {
/* 1170 */     return chilkatJNI.CkFtp2_getCreateTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String createTimeByNameStr(String var1) {
/* 1174 */     return chilkatJNI.CkFtp2_createTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetCreateTimeByNameStrAsync(String var1) {
/* 1178 */     long var2 = chilkatJNI.CkFtp2_GetCreateTimeByNameStrAsync(this.swigCPtr, this, var1);
/* 1179 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetCreateTimeStr(int var1, CkString var2) {
/* 1183 */     return chilkatJNI.CkFtp2_GetCreateTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCreateTimeStr(int var1) {
/* 1187 */     return chilkatJNI.CkFtp2_getCreateTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String createTimeStr(int var1) {
/* 1191 */     return chilkatJNI.CkFtp2_createTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetCreateTimeStrAsync(int var1) {
/* 1195 */     long var2 = chilkatJNI.CkFtp2_GetCreateTimeStrAsync(this.swigCPtr, this, var1);
/* 1196 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetCurrentRemoteDir(CkString var1) {
/* 1200 */     return chilkatJNI.CkFtp2_GetCurrentRemoteDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getCurrentRemoteDir() {
/* 1204 */     return chilkatJNI.CkFtp2_getCurrentRemoteDir(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String currentRemoteDir() {
/* 1208 */     return chilkatJNI.CkFtp2_currentRemoteDir(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask GetCurrentRemoteDirAsync() {
/* 1212 */     long var1 = chilkatJNI.CkFtp2_GetCurrentRemoteDirAsync(this.swigCPtr, this);
/* 1213 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public int GetDirCount() {
/* 1217 */     return chilkatJNI.CkFtp2_GetDirCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask GetDirCountAsync() {
/* 1221 */     long var1 = chilkatJNI.CkFtp2_GetDirCountAsync(this.swigCPtr, this);
/* 1222 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetFile(String var1, String var2) {
/* 1226 */     return chilkatJNI.CkFtp2_GetFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask GetFileAsync(String var1, String var2) {
/* 1230 */     long var3 = chilkatJNI.CkFtp2_GetFileAsync(this.swigCPtr, this, var1, var2);
/* 1231 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileBd(String var1, CkBinData var2) {
/* 1235 */     return chilkatJNI.CkFtp2_GetFileBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask GetFileBdAsync(String var1, CkBinData var2) {
/* 1239 */     long var3 = chilkatJNI.CkFtp2_GetFileBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/* 1240 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean GetFilename(int var1, CkString var2) {
/* 1244 */     return chilkatJNI.CkFtp2_GetFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getFilename(int var1) {
/* 1248 */     return chilkatJNI.CkFtp2_getFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String filename(int var1) {
/* 1252 */     return chilkatJNI.CkFtp2_filename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetFilenameAsync(int var1) {
/* 1256 */     long var2 = chilkatJNI.CkFtp2_GetFilenameAsync(this.swigCPtr, this, var1);
/* 1257 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileSb(String var1, String var2, CkStringBuilder var3) {
/* 1261 */     return chilkatJNI.CkFtp2_GetFileSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileSbAsync(String var1, String var2, CkStringBuilder var3) {
/* 1265 */     long var4 = chilkatJNI.CkFtp2_GetFileSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/* 1266 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileToStream(String var1, CkStream var2) {
/* 1270 */     return chilkatJNI.CkFtp2_GetFileToStream(this.swigCPtr, this, var1, CkStream.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask GetFileToStreamAsync(String var1, CkStream var2) {
/* 1274 */     long var3 = chilkatJNI.CkFtp2_GetFileToStreamAsync(this.swigCPtr, this, var1, CkStream.getCPtr(var2), var2);
/* 1275 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean GetGroup(int var1, CkString var2) {
/* 1279 */     return chilkatJNI.CkFtp2_GetGroup(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getGroup(int var1) {
/* 1283 */     return chilkatJNI.CkFtp2_getGroup(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String group(int var1) {
/* 1287 */     return chilkatJNI.CkFtp2_group(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetGroupAsync(int var1) {
/* 1291 */     long var2 = chilkatJNI.CkFtp2_GetGroupAsync(this.swigCPtr, this, var1);
/* 1292 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetIsDirectory(int var1) {
/* 1296 */     return chilkatJNI.CkFtp2_GetIsDirectory(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetIsDirectoryAsync(int var1) {
/* 1300 */     long var2 = chilkatJNI.CkFtp2_GetIsDirectoryAsync(this.swigCPtr, this, var1);
/* 1301 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetIsSymbolicLink(int var1) {
/* 1305 */     return chilkatJNI.CkFtp2_GetIsSymbolicLink(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetIsSymbolicLinkAsync(int var1) {
/* 1309 */     long var2 = chilkatJNI.CkFtp2_GetIsSymbolicLinkAsync(this.swigCPtr, this, var1);
/* 1310 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetLastAccessDt(int var1) {
/* 1314 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessDt(this.swigCPtr, this, var1);
/* 1315 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetLastAccessDtAsync(int var1) {
/* 1319 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessDtAsync(this.swigCPtr, this, var1);
/* 1320 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetLastAccessDtByName(String var1) {
/* 1324 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessDtByName(this.swigCPtr, this, var1);
/* 1325 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetLastAccessDtByNameAsync(String var1) {
/* 1329 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessDtByNameAsync(this.swigCPtr, this, var1);
/* 1330 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetLastAccessTime(int var1, SYSTEMTIME var2) {
/* 1334 */     return chilkatJNI.CkFtp2_GetLastAccessTime(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetLastAccessTimeByName(String var1, SYSTEMTIME var2) {
/* 1338 */     return chilkatJNI.CkFtp2_GetLastAccessTimeByName(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetLastAccessTimeByNameStr(String var1, CkString var2) {
/* 1342 */     return chilkatJNI.CkFtp2_GetLastAccessTimeByNameStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getLastAccessTimeByNameStr(String var1) {
/* 1346 */     return chilkatJNI.CkFtp2_getLastAccessTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String lastAccessTimeByNameStr(String var1) {
/* 1350 */     return chilkatJNI.CkFtp2_lastAccessTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetLastAccessTimeByNameStrAsync(String var1) {
/* 1354 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessTimeByNameStrAsync(this.swigCPtr, this, var1);
/* 1355 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetLastAccessTimeStr(int var1, CkString var2) {
/* 1359 */     return chilkatJNI.CkFtp2_GetLastAccessTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getLastAccessTimeStr(int var1) {
/* 1363 */     return chilkatJNI.CkFtp2_getLastAccessTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String lastAccessTimeStr(int var1) {
/* 1367 */     return chilkatJNI.CkFtp2_lastAccessTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetLastAccessTimeStrAsync(int var1) {
/* 1371 */     long var2 = chilkatJNI.CkFtp2_GetLastAccessTimeStrAsync(this.swigCPtr, this, var1);
/* 1372 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetLastModDt(int var1) {
/* 1376 */     long var2 = chilkatJNI.CkFtp2_GetLastModDt(this.swigCPtr, this, var1);
/* 1377 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetLastModDtAsync(int var1) {
/* 1381 */     long var2 = chilkatJNI.CkFtp2_GetLastModDtAsync(this.swigCPtr, this, var1);
/* 1382 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetLastModDtByName(String var1) {
/* 1386 */     long var2 = chilkatJNI.CkFtp2_GetLastModDtByName(this.swigCPtr, this, var1);
/* 1387 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetLastModDtByNameAsync(String var1) {
/* 1391 */     long var2 = chilkatJNI.CkFtp2_GetLastModDtByNameAsync(this.swigCPtr, this, var1);
/* 1392 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetLastModifiedTime(int var1, SYSTEMTIME var2) {
/* 1396 */     return chilkatJNI.CkFtp2_GetLastModifiedTime(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetLastModifiedTimeByName(String var1, SYSTEMTIME var2) {
/* 1400 */     return chilkatJNI.CkFtp2_GetLastModifiedTimeByName(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetLastModifiedTimeByNameStr(String var1, CkString var2) {
/* 1404 */     return chilkatJNI.CkFtp2_GetLastModifiedTimeByNameStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getLastModifiedTimeByNameStr(String var1) {
/* 1408 */     return chilkatJNI.CkFtp2_getLastModifiedTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String lastModifiedTimeByNameStr(String var1) {
/* 1412 */     return chilkatJNI.CkFtp2_lastModifiedTimeByNameStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetLastModifiedTimeByNameStrAsync(String var1) {
/* 1416 */     long var2 = chilkatJNI.CkFtp2_GetLastModifiedTimeByNameStrAsync(this.swigCPtr, this, var1);
/* 1417 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetLastModifiedTimeStr(int var1, CkString var2) {
/* 1421 */     return chilkatJNI.CkFtp2_GetLastModifiedTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getLastModifiedTimeStr(int var1) {
/* 1425 */     return chilkatJNI.CkFtp2_getLastModifiedTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String lastModifiedTimeStr(int var1) {
/* 1429 */     return chilkatJNI.CkFtp2_lastModifiedTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetLastModifiedTimeStrAsync(int var1) {
/* 1433 */     long var2 = chilkatJNI.CkFtp2_GetLastModifiedTimeStrAsync(this.swigCPtr, this, var1);
/* 1434 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetOwner(int var1, CkString var2) {
/* 1438 */     return chilkatJNI.CkFtp2_GetOwner(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getOwner(int var1) {
/* 1442 */     return chilkatJNI.CkFtp2_getOwner(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String owner(int var1) {
/* 1446 */     return chilkatJNI.CkFtp2_owner(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetOwnerAsync(int var1) {
/* 1450 */     long var2 = chilkatJNI.CkFtp2_GetOwnerAsync(this.swigCPtr, this, var1);
/* 1451 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetPermissions(int var1, CkString var2) {
/* 1455 */     return chilkatJNI.CkFtp2_GetPermissions(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getPermissions(int var1) {
/* 1459 */     return chilkatJNI.CkFtp2_getPermissions(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String permissions(int var1) {
/* 1463 */     return chilkatJNI.CkFtp2_permissions(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetPermissionsAsync(int var1) {
/* 1467 */     long var2 = chilkatJNI.CkFtp2_GetPermissionsAsync(this.swigCPtr, this, var1);
/* 1468 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetPermType(int var1, CkString var2) {
/* 1472 */     return chilkatJNI.CkFtp2_GetPermType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getPermType(int var1) {
/* 1476 */     return chilkatJNI.CkFtp2_getPermType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String permType(int var1) {
/* 1480 */     return chilkatJNI.CkFtp2_permType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetPermTypeAsync(int var1) {
/* 1484 */     long var2 = chilkatJNI.CkFtp2_GetPermTypeAsync(this.swigCPtr, this, var1);
/* 1485 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetRemoteFileBinaryData(String var1, CkByteData var2) {
/* 1489 */     return chilkatJNI.CkFtp2_GetRemoteFileBinaryData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask GetRemoteFileBinaryDataAsync(String var1) {
/* 1493 */     long var2 = chilkatJNI.CkFtp2_GetRemoteFileBinaryDataAsync(this.swigCPtr, this, var1);
/* 1494 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetRemoteFileTextC(String var1, String var2, CkString var3) {
/* 1498 */     return chilkatJNI.CkFtp2_GetRemoteFileTextC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getRemoteFileTextC(String var1, String var2) {
/* 1502 */     return chilkatJNI.CkFtp2_getRemoteFileTextC(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String remoteFileTextC(String var1, String var2) {
/* 1506 */     return chilkatJNI.CkFtp2_remoteFileTextC(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask GetRemoteFileTextCAsync(String var1, String var2) {
/* 1510 */     long var3 = chilkatJNI.CkFtp2_GetRemoteFileTextCAsync(this.swigCPtr, this, var1, var2);
/* 1511 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean GetRemoteFileTextData(String var1, CkString var2) {
/* 1515 */     return chilkatJNI.CkFtp2_GetRemoteFileTextData(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRemoteFileTextData(String var1) {
/* 1519 */     return chilkatJNI.CkFtp2_getRemoteFileTextData(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String remoteFileTextData(String var1) {
/* 1523 */     return chilkatJNI.CkFtp2_remoteFileTextData(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetRemoteFileTextDataAsync(String var1) {
/* 1527 */     long var2 = chilkatJNI.CkFtp2_GetRemoteFileTextDataAsync(this.swigCPtr, this, var1);
/* 1528 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public int GetSize(int var1) {
/* 1532 */     return chilkatJNI.CkFtp2_GetSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetSizeAsync(int var1) {
/* 1536 */     long var2 = chilkatJNI.CkFtp2_GetSizeAsync(this.swigCPtr, this, var1);
/* 1537 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public long GetSize64(int var1) {
/* 1541 */     return chilkatJNI.CkFtp2_GetSize64(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int GetSizeByName(String var1) {
/* 1545 */     return chilkatJNI.CkFtp2_GetSizeByName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetSizeByNameAsync(String var1) {
/* 1549 */     long var2 = chilkatJNI.CkFtp2_GetSizeByNameAsync(this.swigCPtr, this, var1);
/* 1550 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public long GetSizeByName64(String var1) {
/* 1554 */     return chilkatJNI.CkFtp2_GetSizeByName64(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetSizeStr(int var1, CkString var2) {
/* 1558 */     return chilkatJNI.CkFtp2_GetSizeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getSizeStr(int var1) {
/* 1562 */     return chilkatJNI.CkFtp2_getSizeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String sizeStr(int var1) {
/* 1566 */     return chilkatJNI.CkFtp2_sizeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetSizeStrAsync(int var1) {
/* 1570 */     long var2 = chilkatJNI.CkFtp2_GetSizeStrAsync(this.swigCPtr, this, var1);
/* 1571 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetSizeStrByName(String var1, CkString var2) {
/* 1575 */     return chilkatJNI.CkFtp2_GetSizeStrByName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getSizeStrByName(String var1) {
/* 1579 */     return chilkatJNI.CkFtp2_getSizeStrByName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String sizeStrByName(String var1) {
/* 1583 */     return chilkatJNI.CkFtp2_sizeStrByName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetSizeStrByNameAsync(String var1) {
/* 1587 */     long var2 = chilkatJNI.CkFtp2_GetSizeStrByNameAsync(this.swigCPtr, this, var1);
/* 1588 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkCert GetSslServerCert() {
/* 1592 */     long var1 = chilkatJNI.CkFtp2_GetSslServerCert(this.swigCPtr, this);
/* 1593 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetTextDirListing(String var1, CkString var2) {
/* 1597 */     return chilkatJNI.CkFtp2_GetTextDirListing(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getTextDirListing(String var1) {
/* 1601 */     return chilkatJNI.CkFtp2_getTextDirListing(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String textDirListing(String var1) {
/* 1605 */     return chilkatJNI.CkFtp2_textDirListing(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetTextDirListingAsync(String var1) {
/* 1609 */     long var2 = chilkatJNI.CkFtp2_GetTextDirListingAsync(this.swigCPtr, this, var1);
/* 1610 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetXmlDirListing(String var1, CkString var2) {
/* 1614 */     return chilkatJNI.CkFtp2_GetXmlDirListing(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getXmlDirListing(String var1) {
/* 1618 */     return chilkatJNI.CkFtp2_getXmlDirListing(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String xmlDirListing(String var1) {
/* 1622 */     return chilkatJNI.CkFtp2_xmlDirListing(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetXmlDirListingAsync(String var1) {
/* 1626 */     long var2 = chilkatJNI.CkFtp2_GetXmlDirListingAsync(this.swigCPtr, this, var1);
/* 1627 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/* 1631 */     return chilkatJNI.CkFtp2_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean LargeFileUpload(String var1, String var2, int var3) {
/* 1635 */     return chilkatJNI.CkFtp2_LargeFileUpload(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask LargeFileUploadAsync(String var1, String var2, int var3) {
/* 1639 */     long var4 = chilkatJNI.CkFtp2_LargeFileUploadAsync(this.swigCPtr, this, var1, var2, var3);
/* 1640 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean LoginAfterConnectOnly() {
/* 1644 */     return chilkatJNI.CkFtp2_LoginAfterConnectOnly(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask LoginAfterConnectOnlyAsync() {
/* 1648 */     long var1 = chilkatJNI.CkFtp2_LoginAfterConnectOnlyAsync(this.swigCPtr, this);
/* 1649 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public int MGetFiles(String var1, String var2) {
/* 1653 */     return chilkatJNI.CkFtp2_MGetFiles(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask MGetFilesAsync(String var1, String var2) {
/* 1657 */     long var3 = chilkatJNI.CkFtp2_MGetFilesAsync(this.swigCPtr, this, var1, var2);
/* 1658 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public int MPutFiles(String var1) {
/* 1662 */     return chilkatJNI.CkFtp2_MPutFiles(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask MPutFilesAsync(String var1) {
/* 1666 */     long var2 = chilkatJNI.CkFtp2_MPutFilesAsync(this.swigCPtr, this, var1);
/* 1667 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean NlstXml(String var1, CkString var2) {
/* 1671 */     return chilkatJNI.CkFtp2_NlstXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String nlstXml(String var1) {
/* 1675 */     return chilkatJNI.CkFtp2_nlstXml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask NlstXmlAsync(String var1) {
/* 1679 */     long var2 = chilkatJNI.CkFtp2_NlstXmlAsync(this.swigCPtr, this, var1);
/* 1680 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Noop() {
/* 1684 */     return chilkatJNI.CkFtp2_Noop(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask NoopAsync() {
/* 1688 */     long var1 = chilkatJNI.CkFtp2_NoopAsync(this.swigCPtr, this);
/* 1689 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean PutFile(String var1, String var2) {
/* 1693 */     return chilkatJNI.CkFtp2_PutFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask PutFileAsync(String var1, String var2) {
/* 1697 */     long var3 = chilkatJNI.CkFtp2_PutFileAsync(this.swigCPtr, this, var1, var2);
/* 1698 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean PutFileBd(CkBinData var1, String var2) {
/* 1702 */     return chilkatJNI.CkFtp2_PutFileBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask PutFileBdAsync(CkBinData var1, String var2) {
/* 1706 */     long var3 = chilkatJNI.CkFtp2_PutFileBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/* 1707 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean PutFileFromBinaryData(String var1, CkByteData var2) {
/* 1711 */     return chilkatJNI.CkFtp2_PutFileFromBinaryData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask PutFileFromBinaryDataAsync(String var1, CkByteData var2) {
/* 1715 */     long var3 = chilkatJNI.CkFtp2_PutFileFromBinaryDataAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 1716 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean PutFileFromTextData(String var1, String var2, String var3) {
/* 1720 */     return chilkatJNI.CkFtp2_PutFileFromTextData(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask PutFileFromTextDataAsync(String var1, String var2, String var3) {
/* 1724 */     long var4 = chilkatJNI.CkFtp2_PutFileFromTextDataAsync(this.swigCPtr, this, var1, var2, var3);
/* 1725 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean PutFileSb(CkStringBuilder var1, String var2, boolean var3, String var4) {
/* 1729 */     return chilkatJNI.CkFtp2_PutFileSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask PutFileSbAsync(CkStringBuilder var1, String var2, boolean var3, String var4) {
/* 1733 */     long var5 = chilkatJNI.CkFtp2_PutFileSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, var3, var4);
/* 1734 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean PutPlan(String var1, String var2) {
/* 1738 */     return chilkatJNI.CkFtp2_PutPlan(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask PutPlanAsync(String var1, String var2) {
/* 1742 */     long var3 = chilkatJNI.CkFtp2_PutPlanAsync(this.swigCPtr, this, var1, var2);
/* 1743 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean PutTree(String var1) {
/* 1747 */     return chilkatJNI.CkFtp2_PutTree(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask PutTreeAsync(String var1) {
/* 1751 */     long var2 = chilkatJNI.CkFtp2_PutTreeAsync(this.swigCPtr, this, var1);
/* 1752 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Quote(String var1) {
/* 1756 */     return chilkatJNI.CkFtp2_Quote(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask QuoteAsync(String var1) {
/* 1760 */     long var2 = chilkatJNI.CkFtp2_QuoteAsync(this.swigCPtr, this, var1);
/* 1761 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RemoveRemoteDir(String var1) {
/* 1765 */     return chilkatJNI.CkFtp2_RemoveRemoteDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask RemoveRemoteDirAsync(String var1) {
/* 1769 */     long var2 = chilkatJNI.CkFtp2_RemoveRemoteDirAsync(this.swigCPtr, this, var1);
/* 1770 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RenameRemoteFile(String var1, String var2) {
/* 1774 */     return chilkatJNI.CkFtp2_RenameRemoteFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask RenameRemoteFileAsync(String var1, String var2) {
/* 1778 */     long var3 = chilkatJNI.CkFtp2_RenameRemoteFileAsync(this.swigCPtr, this, var1, var2);
/* 1779 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1783 */     return chilkatJNI.CkFtp2_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SendCommand(String var1, CkString var2) {
/* 1787 */     return chilkatJNI.CkFtp2_SendCommand(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String sendCommand(String var1) {
/* 1791 */     return chilkatJNI.CkFtp2_sendCommand(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SendCommandAsync(String var1) {
/* 1795 */     long var2 = chilkatJNI.CkFtp2_SendCommandAsync(this.swigCPtr, this, var1);
/* 1796 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SetModeZ() {
/* 1800 */     return chilkatJNI.CkFtp2_SetModeZ(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SetModeZAsync() {
/* 1804 */     long var1 = chilkatJNI.CkFtp2_SetModeZAsync(this.swigCPtr, this);
/* 1805 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public void SetOldestDate(SYSTEMTIME var1) {
/* 1809 */     chilkatJNI.CkFtp2_SetOldestDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void SetOldestDateStr(String var1) {
/* 1813 */     chilkatJNI.CkFtp2_SetOldestDateStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetOption(String var1) {
/* 1817 */     return chilkatJNI.CkFtp2_SetOption(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetPassword(CkSecureString var1) {
/* 1821 */     return chilkatJNI.CkFtp2_SetPassword(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetRemoteFileDateTime(SYSTEMTIME var1, String var2) {
/* 1825 */     return chilkatJNI.CkFtp2_SetRemoteFileDateTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetRemoteFileDateTimeStr(String var1, String var2) {
/* 1829 */     return chilkatJNI.CkFtp2_SetRemoteFileDateTimeStr(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SetRemoteFileDateTimeStrAsync(String var1, String var2) {
/* 1833 */     long var3 = chilkatJNI.CkFtp2_SetRemoteFileDateTimeStrAsync(this.swigCPtr, this, var1, var2);
/* 1834 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SetRemoteFileDt(CkDateTime var1, String var2) {
/* 1838 */     return chilkatJNI.CkFtp2_SetRemoteFileDt(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SetRemoteFileDtAsync(CkDateTime var1, String var2) {
/* 1842 */     long var3 = chilkatJNI.CkFtp2_SetRemoteFileDtAsync(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1, var2);
/* 1843 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public void SetSslCertRequirement(String var1, String var2) {
/* 1847 */     chilkatJNI.CkFtp2_SetSslCertRequirement(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCert(CkCert var1) {
/* 1851 */     return chilkatJNI.CkFtp2_SetSslClientCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPem(String var1, String var2) {
/* 1855 */     return chilkatJNI.CkFtp2_SetSslClientCertPem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPfx(String var1, String var2) {
/* 1859 */     return chilkatJNI.CkFtp2_SetSslClientCertPfx(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetTypeAscii() {
/* 1863 */     return chilkatJNI.CkFtp2_SetTypeAscii(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SetTypeAsciiAsync() {
/* 1867 */     long var1 = chilkatJNI.CkFtp2_SetTypeAsciiAsync(this.swigCPtr, this);
/* 1868 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SetTypeBinary() {
/* 1872 */     return chilkatJNI.CkFtp2_SetTypeBinary(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SetTypeBinaryAsync() {
/* 1876 */     long var1 = chilkatJNI.CkFtp2_SetTypeBinaryAsync(this.swigCPtr, this);
/* 1877 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Site(String var1) {
/* 1881 */     return chilkatJNI.CkFtp2_Site(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SiteAsync(String var1) {
/* 1885 */     long var2 = chilkatJNI.CkFtp2_SiteAsync(this.swigCPtr, this, var1);
/* 1886 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public void SleepMs(int var1) {
/* 1890 */     chilkatJNI.CkFtp2_SleepMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean Stat(CkString var1) {
/* 1894 */     return chilkatJNI.CkFtp2_Stat(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String ck_stat() {
/* 1898 */     return chilkatJNI.CkFtp2_ck_stat(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask StatAsync() {
/* 1902 */     long var1 = chilkatJNI.CkFtp2_StatAsync(this.swigCPtr, this);
/* 1903 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SyncDeleteRemote(String var1) {
/* 1907 */     return chilkatJNI.CkFtp2_SyncDeleteRemote(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SyncDeleteRemoteAsync(String var1) {
/* 1911 */     long var2 = chilkatJNI.CkFtp2_SyncDeleteRemoteAsync(this.swigCPtr, this, var1);
/* 1912 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SyncLocalDir(String var1, int var2) {
/* 1916 */     return chilkatJNI.CkFtp2_SyncLocalDir(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SyncLocalDirAsync(String var1, int var2) {
/* 1920 */     long var3 = chilkatJNI.CkFtp2_SyncLocalDirAsync(this.swigCPtr, this, var1, var2);
/* 1921 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SyncLocalTree(String var1, int var2) {
/* 1925 */     return chilkatJNI.CkFtp2_SyncLocalTree(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SyncLocalTreeAsync(String var1, int var2) {
/* 1929 */     long var3 = chilkatJNI.CkFtp2_SyncLocalTreeAsync(this.swigCPtr, this, var1, var2);
/* 1930 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SyncRemoteTree(String var1, int var2) {
/* 1934 */     return chilkatJNI.CkFtp2_SyncRemoteTree(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SyncRemoteTreeAsync(String var1, int var2) {
/* 1938 */     long var3 = chilkatJNI.CkFtp2_SyncRemoteTreeAsync(this.swigCPtr, this, var1, var2);
/* 1939 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SyncRemoteTree2(String var1, int var2, boolean var3, boolean var4) {
/* 1943 */     return chilkatJNI.CkFtp2_SyncRemoteTree2(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask SyncRemoteTree2Async(String var1, int var2, boolean var3, boolean var4) {
/* 1947 */     long var5 = chilkatJNI.CkFtp2_SyncRemoteTree2Async(this.swigCPtr, this, var1, var2, var3, var4);
/* 1948 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean Syst(CkString var1) {
/* 1952 */     return chilkatJNI.CkFtp2_Syst(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syst() {
/* 1956 */     return chilkatJNI.CkFtp2_syst(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SystAsync() {
/* 1960 */     long var1 = chilkatJNI.CkFtp2_SystAsync(this.swigCPtr, this);
/* 1961 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1965 */     return chilkatJNI.CkFtp2_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkFtp2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */