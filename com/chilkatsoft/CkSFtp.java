/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkSFtp
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkSFtp(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkSFtp var0) {
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
/*   29 */         chilkatJNI.delete_CkSFtp(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkSFtp()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkSFtp(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkSFtp_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkSFtp_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkSFtp_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkSFtpProgress var1) {
/*   54 */     chilkatJNI.CkSFtp_put_EventCallbackObject(this.swigCPtr, this, CkSFtpProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkSFtp_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkSFtp_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AccumulateBuffer(CkByteData var1) {
/*   66 */     chilkatJNI.CkSFtp_get_AccumulateBuffer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public int get_AuthFailReason() {
/*   70 */     return chilkatJNI.CkSFtp_get_AuthFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleDown() {
/*   74 */     return chilkatJNI.CkSFtp_get_BandwidthThrottleDown(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleDown(int var1) {
/*   78 */     chilkatJNI.CkSFtp_put_BandwidthThrottleDown(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleUp() {
/*   82 */     return chilkatJNI.CkSFtp_get_BandwidthThrottleUp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleUp(int var1) {
/*   86 */     chilkatJNI.CkSFtp_put_BandwidthThrottleUp(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIdentifier(CkString var1) {
/*   90 */     chilkatJNI.CkSFtp_get_ClientIdentifier(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIdentifier() {
/*   94 */     return chilkatJNI.CkSFtp_clientIdentifier(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIdentifier(String var1) {
/*   98 */     chilkatJNI.CkSFtp_put_ClientIdentifier(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  102 */     chilkatJNI.CkSFtp_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  106 */     return chilkatJNI.CkSFtp_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  110 */     chilkatJNI.CkSFtp_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectTimeoutMs() {
/*  114 */     return chilkatJNI.CkSFtp_get_ConnectTimeoutMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ConnectTimeoutMs(int var1) {
/*  118 */     chilkatJNI.CkSFtp_put_ConnectTimeoutMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  122 */     chilkatJNI.CkSFtp_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  126 */     return chilkatJNI.CkSFtp_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  130 */     chilkatJNI.CkSFtp_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_DisconnectCode() {
/*  134 */     return chilkatJNI.CkSFtp_get_DisconnectCode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_DisconnectReason(CkString var1) {
/*  138 */     chilkatJNI.CkSFtp_get_DisconnectReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String disconnectReason() {
/*  142 */     return chilkatJNI.CkSFtp_disconnectReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_EnableCache() {
/*  146 */     return chilkatJNI.CkSFtp_get_EnableCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_EnableCache(boolean var1) {
/*  150 */     chilkatJNI.CkSFtp_put_EnableCache(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_EnableCompression() {
/*  154 */     return chilkatJNI.CkSFtp_get_EnableCompression(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_EnableCompression(boolean var1) {
/*  158 */     chilkatJNI.CkSFtp_put_EnableCompression(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_FilenameCharset(CkString var1) {
/*  162 */     chilkatJNI.CkSFtp_get_FilenameCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String filenameCharset() {
/*  166 */     return chilkatJNI.CkSFtp_filenameCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FilenameCharset(String var1) {
/*  170 */     chilkatJNI.CkSFtp_put_FilenameCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ForceCipher(CkString var1) {
/*  174 */     chilkatJNI.CkSFtp_get_ForceCipher(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String forceCipher() {
/*  178 */     return chilkatJNI.CkSFtp_forceCipher(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ForceCipher(String var1) {
/*  182 */     chilkatJNI.CkSFtp_put_ForceCipher(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ForceV3() {
/*  186 */     return chilkatJNI.CkSFtp_get_ForceV3(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ForceV3(boolean var1) {
/*  190 */     chilkatJNI.CkSFtp_put_ForceV3(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  194 */     return chilkatJNI.CkSFtp_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  198 */     chilkatJNI.CkSFtp_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HostKeyAlg(CkString var1) {
/*  202 */     chilkatJNI.CkSFtp_get_HostKeyAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String hostKeyAlg() {
/*  206 */     return chilkatJNI.CkSFtp_hostKeyAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HostKeyAlg(String var1) {
/*  210 */     chilkatJNI.CkSFtp_put_HostKeyAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HostKeyFingerprint(CkString var1) {
/*  214 */     chilkatJNI.CkSFtp_get_HostKeyFingerprint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String hostKeyFingerprint() {
/*  218 */     return chilkatJNI.CkSFtp_hostKeyFingerprint(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyAuthMethod(CkString var1) {
/*  222 */     chilkatJNI.CkSFtp_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyAuthMethod() {
/*  226 */     return chilkatJNI.CkSFtp_httpProxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyAuthMethod(String var1) {
/*  230 */     chilkatJNI.CkSFtp_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyDomain(CkString var1) {
/*  234 */     chilkatJNI.CkSFtp_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyDomain() {
/*  238 */     return chilkatJNI.CkSFtp_httpProxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyDomain(String var1) {
/*  242 */     chilkatJNI.CkSFtp_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyHostname(CkString var1) {
/*  246 */     chilkatJNI.CkSFtp_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyHostname() {
/*  250 */     return chilkatJNI.CkSFtp_httpProxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyHostname(String var1) {
/*  254 */     chilkatJNI.CkSFtp_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyPassword(CkString var1) {
/*  258 */     chilkatJNI.CkSFtp_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyPassword() {
/*  262 */     return chilkatJNI.CkSFtp_httpProxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPassword(String var1) {
/*  266 */     chilkatJNI.CkSFtp_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HttpProxyPort() {
/*  270 */     return chilkatJNI.CkSFtp_get_HttpProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPort(int var1) {
/*  274 */     chilkatJNI.CkSFtp_put_HttpProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyUsername(CkString var1) {
/*  278 */     chilkatJNI.CkSFtp_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyUsername() {
/*  282 */     return chilkatJNI.CkSFtp_httpProxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyUsername(String var1) {
/*  286 */     chilkatJNI.CkSFtp_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_IdleTimeoutMs() {
/*  290 */     return chilkatJNI.CkSFtp_get_IdleTimeoutMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IdleTimeoutMs(int var1) {
/*  294 */     chilkatJNI.CkSFtp_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IncludeDotDirs() {
/*  298 */     return chilkatJNI.CkSFtp_get_IncludeDotDirs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IncludeDotDirs(boolean var1) {
/*  302 */     chilkatJNI.CkSFtp_put_IncludeDotDirs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_InitializeFailCode() {
/*  306 */     return chilkatJNI.CkSFtp_get_InitializeFailCode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_InitializeFailReason(CkString var1) {
/*  310 */     chilkatJNI.CkSFtp_get_InitializeFailReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String initializeFailReason() {
/*  314 */     return chilkatJNI.CkSFtp_initializeFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_IsConnected() {
/*  318 */     return chilkatJNI.CkSFtp_get_IsConnected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_KeepSessionLog() {
/*  322 */     return chilkatJNI.CkSFtp_get_KeepSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepSessionLog(boolean var1) {
/*  326 */     chilkatJNI.CkSFtp_put_KeepSessionLog(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  330 */     chilkatJNI.CkSFtp_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  334 */     return chilkatJNI.CkSFtp_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  338 */     chilkatJNI.CkSFtp_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  342 */     return chilkatJNI.CkSFtp_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  346 */     chilkatJNI.CkSFtp_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  350 */     return chilkatJNI.CkSFtp_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  354 */     return chilkatJNI.CkSFtp_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  358 */     chilkatJNI.CkSFtp_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxPacketSize() {
/*  362 */     return chilkatJNI.CkSFtp_get_MaxPacketSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxPacketSize(int var1) {
/*  366 */     chilkatJNI.CkSFtp_put_MaxPacketSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PasswordChangeRequested() {
/*  370 */     return chilkatJNI.CkSFtp_get_PasswordChangeRequested(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  374 */     return chilkatJNI.CkSFtp_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  378 */     chilkatJNI.CkSFtp_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  382 */     return chilkatJNI.CkSFtp_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  386 */     chilkatJNI.CkSFtp_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreserveDate() {
/*  390 */     return chilkatJNI.CkSFtp_get_PreserveDate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreserveDate(boolean var1) {
/*  394 */     chilkatJNI.CkSFtp_put_PreserveDate(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ProtocolVersion() {
/*  398 */     return chilkatJNI.CkSFtp_get_ProtocolVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_ReadDirMustMatch(CkString var1) {
/*  402 */     chilkatJNI.CkSFtp_get_ReadDirMustMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String readDirMustMatch() {
/*  406 */     return chilkatJNI.CkSFtp_readDirMustMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadDirMustMatch(String var1) {
/*  410 */     chilkatJNI.CkSFtp_put_ReadDirMustMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ReadDirMustNotMatch(CkString var1) {
/*  414 */     chilkatJNI.CkSFtp_get_ReadDirMustNotMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String readDirMustNotMatch() {
/*  418 */     return chilkatJNI.CkSFtp_readDirMustNotMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadDirMustNotMatch(String var1) {
/*  422 */     chilkatJNI.CkSFtp_put_ReadDirMustNotMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ServerIdentifier(CkString var1) {
/*  426 */     chilkatJNI.CkSFtp_get_ServerIdentifier(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String serverIdentifier() {
/*  430 */     return chilkatJNI.CkSFtp_serverIdentifier(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SessionLog(CkString var1) {
/*  434 */     chilkatJNI.CkSFtp_get_SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLog() {
/*  438 */     return chilkatJNI.CkSFtp_sessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  442 */     chilkatJNI.CkSFtp_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  446 */     return chilkatJNI.CkSFtp_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  450 */     chilkatJNI.CkSFtp_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  454 */     chilkatJNI.CkSFtp_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  458 */     return chilkatJNI.CkSFtp_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  462 */     chilkatJNI.CkSFtp_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  466 */     return chilkatJNI.CkSFtp_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  470 */     chilkatJNI.CkSFtp_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  474 */     chilkatJNI.CkSFtp_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  478 */     return chilkatJNI.CkSFtp_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  482 */     chilkatJNI.CkSFtp_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  486 */     return chilkatJNI.CkSFtp_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  490 */     chilkatJNI.CkSFtp_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  494 */     return chilkatJNI.CkSFtp_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  498 */     chilkatJNI.CkSFtp_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  502 */     return chilkatJNI.CkSFtp_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  506 */     chilkatJNI.CkSFtp_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncDirectives(CkString var1) {
/*  510 */     chilkatJNI.CkSFtp_get_SyncDirectives(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncDirectives() {
/*  514 */     return chilkatJNI.CkSFtp_syncDirectives(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncDirectives(String var1) {
/*  518 */     chilkatJNI.CkSFtp_put_SyncDirectives(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncedFiles(CkString var1) {
/*  522 */     chilkatJNI.CkSFtp_get_SyncedFiles(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncedFiles() {
/*  526 */     return chilkatJNI.CkSFtp_syncedFiles(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncedFiles(String var1) {
/*  530 */     chilkatJNI.CkSFtp_put_SyncedFiles(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncMustMatch(CkString var1) {
/*  534 */     chilkatJNI.CkSFtp_get_SyncMustMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncMustMatch() {
/*  538 */     return chilkatJNI.CkSFtp_syncMustMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncMustMatch(String var1) {
/*  542 */     chilkatJNI.CkSFtp_put_SyncMustMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SyncMustNotMatch(CkString var1) {
/*  546 */     chilkatJNI.CkSFtp_get_SyncMustNotMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String syncMustNotMatch() {
/*  550 */     return chilkatJNI.CkSFtp_syncMustNotMatch(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SyncMustNotMatch(String var1) {
/*  554 */     chilkatJNI.CkSFtp_put_SyncMustNotMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_TcpNoDelay() {
/*  558 */     return chilkatJNI.CkSFtp_get_TcpNoDelay(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TcpNoDelay(boolean var1) {
/*  562 */     chilkatJNI.CkSFtp_put_TcpNoDelay(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_UncommonOptions(CkString var1) {
/*  566 */     chilkatJNI.CkSFtp_get_UncommonOptions(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String uncommonOptions() {
/*  570 */     return chilkatJNI.CkSFtp_uncommonOptions(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UncommonOptions(String var1) {
/*  574 */     chilkatJNI.CkSFtp_put_UncommonOptions(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_UploadChunkSize() {
/*  578 */     return chilkatJNI.CkSFtp_get_UploadChunkSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UploadChunkSize(int var1) {
/*  582 */     chilkatJNI.CkSFtp_put_UploadChunkSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_UtcMode() {
/*  586 */     return chilkatJNI.CkSFtp_get_UtcMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UtcMode(boolean var1) {
/*  590 */     chilkatJNI.CkSFtp_put_UtcMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  594 */     return chilkatJNI.CkSFtp_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  598 */     chilkatJNI.CkSFtp_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  602 */     chilkatJNI.CkSFtp_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  606 */     return chilkatJNI.CkSFtp_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public long get_XferByteCount() {
/*  610 */     return chilkatJNI.CkSFtp_get_XferByteCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int AccumulateBytes(String var1, int var2) {
/*  614 */     return chilkatJNI.CkSFtp_AccumulateBytes(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask AccumulateBytesAsync(String var1, int var2) {
/*  618 */     long var3 = chilkatJNI.CkSFtp_AccumulateBytesAsync(this.swigCPtr, this, var1, var2);
/*  619 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean Add64(String var1, String var2, CkString var3) {
/*  623 */     return chilkatJNI.CkSFtp_Add64(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String add64(String var1, String var2) {
/*  627 */     return chilkatJNI.CkSFtp_add64(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AuthenticatePk(String var1, CkSshKey var2) {
/*  631 */     return chilkatJNI.CkSFtp_AuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask AuthenticatePkAsync(String var1, CkSshKey var2) {
/*  635 */     long var3 = chilkatJNI.CkSFtp_AuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*  636 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AuthenticatePw(String var1, String var2) {
/*  640 */     return chilkatJNI.CkSFtp_AuthenticatePw(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask AuthenticatePwAsync(String var1, String var2) {
/*  644 */     long var3 = chilkatJNI.CkSFtp_AuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/*  645 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AuthenticatePwPk(String var1, String var2, CkSshKey var3) {
/*  649 */     return chilkatJNI.CkSFtp_AuthenticatePwPk(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask AuthenticatePwPkAsync(String var1, String var2, CkSshKey var3) {
/*  653 */     long var4 = chilkatJNI.CkSFtp_AuthenticatePwPkAsync(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/*  654 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean AuthenticateSecPw(CkSecureString var1, CkSecureString var2) {
/*  658 */     return chilkatJNI.CkSFtp_AuthenticateSecPw(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask AuthenticateSecPwAsync(CkSecureString var1, CkSecureString var2) {
/*  662 */     long var3 = chilkatJNI.CkSFtp_AuthenticateSecPwAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*  663 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AuthenticateSecPwPk(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/*  667 */     return chilkatJNI.CkSFtp_AuthenticateSecPwPk(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask AuthenticateSecPwPkAsync(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/*  671 */     long var4 = chilkatJNI.CkSFtp_AuthenticateSecPwPkAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/*  672 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public void ClearAccumulateBuffer() {
/*  676 */     chilkatJNI.CkSFtp_ClearAccumulateBuffer(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearCache() {
/*  680 */     chilkatJNI.CkSFtp_ClearCache(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearSessionLog() {
/*  684 */     chilkatJNI.CkSFtp_ClearSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean CloseHandle(String var1) {
/*  688 */     return chilkatJNI.CkSFtp_CloseHandle(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CloseHandleAsync(String var1) {
/*  692 */     long var2 = chilkatJNI.CkSFtp_CloseHandleAsync(this.swigCPtr, this, var1);
/*  693 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Connect(String var1, int var2) {
/*  697 */     return chilkatJNI.CkSFtp_Connect(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask ConnectAsync(String var1, int var2) {
/*  701 */     long var3 = chilkatJNI.CkSFtp_ConnectAsync(this.swigCPtr, this, var1, var2);
/*  702 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ConnectThroughSsh(CkSsh var1, String var2, int var3) {
/*  706 */     return chilkatJNI.CkSFtp_ConnectThroughSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask ConnectThroughSshAsync(CkSsh var1, String var2, int var3) {
/*  710 */     long var4 = chilkatJNI.CkSFtp_ConnectThroughSshAsync(this.swigCPtr, this, CkSsh.getCPtr(var1), var1, var2, var3);
/*  711 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean CopyFileAttr(String var1, String var2, boolean var3) {
/*  715 */     return chilkatJNI.CkSFtp_CopyFileAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask CopyFileAttrAsync(String var1, String var2, boolean var3) {
/*  719 */     long var4 = chilkatJNI.CkSFtp_CopyFileAttrAsync(this.swigCPtr, this, var1, var2, var3);
/*  720 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean CreateDir(String var1) {
/*  724 */     return chilkatJNI.CkSFtp_CreateDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CreateDirAsync(String var1) {
/*  728 */     long var2 = chilkatJNI.CkSFtp_CreateDirAsync(this.swigCPtr, this, var1);
/*  729 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public void Disconnect() {
/*  733 */     chilkatJNI.CkSFtp_Disconnect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean DownloadBd(String var1, CkBinData var2) {
/*  737 */     return chilkatJNI.CkSFtp_DownloadBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadBdAsync(String var1, CkBinData var2) {
/*  741 */     long var3 = chilkatJNI.CkSFtp_DownloadBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*  742 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadFile(String var1, String var2) {
/*  746 */     return chilkatJNI.CkSFtp_DownloadFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadFileAsync(String var1, String var2) {
/*  750 */     long var3 = chilkatJNI.CkSFtp_DownloadFileAsync(this.swigCPtr, this, var1, var2);
/*  751 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadFileByName(String var1, String var2) {
/*  755 */     return chilkatJNI.CkSFtp_DownloadFileByName(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask DownloadFileByNameAsync(String var1, String var2) {
/*  759 */     long var3 = chilkatJNI.CkSFtp_DownloadFileByNameAsync(this.swigCPtr, this, var1, var2);
/*  760 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean DownloadSb(String var1, String var2, CkStringBuilder var3) {
/*  764 */     return chilkatJNI.CkSFtp_DownloadSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask DownloadSbAsync(String var1, String var2, CkStringBuilder var3) {
/*  768 */     long var4 = chilkatJNI.CkSFtp_DownloadSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*  769 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean Eof(String var1) {
/*  773 */     return chilkatJNI.CkSFtp_Eof(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int FileExists(String var1, boolean var2) {
/*  777 */     return chilkatJNI.CkSFtp_FileExists(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask FileExistsAsync(String var1, boolean var2) {
/*  781 */     long var3 = chilkatJNI.CkSFtp_FileExistsAsync(this.swigCPtr, this, var1, var2);
/*  782 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean Fsync(String var1) {
/*  786 */     return chilkatJNI.CkSFtp_Fsync(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask FsyncAsync(String var1) {
/*  790 */     long var2 = chilkatJNI.CkSFtp_FsyncAsync(this.swigCPtr, this, var1);
/*  791 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetFileCreateDt(String var1, boolean var2, boolean var3) {
/*  795 */     long var4 = chilkatJNI.CkSFtp_GetFileCreateDt(this.swigCPtr, this, var1, var2, var3);
/*  796 */     return var4 == 0L ? null : new CkDateTime(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask GetFileCreateDtAsync(String var1, boolean var2, boolean var3) {
/*  800 */     long var4 = chilkatJNI.CkSFtp_GetFileCreateDtAsync(this.swigCPtr, this, var1, var2, var3);
/*  801 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileCreateTime(String var1, boolean var2, boolean var3, SYSTEMTIME var4) {
/*  805 */     return chilkatJNI.CkSFtp_GetFileCreateTime(this.swigCPtr, this, var1, var2, var3, SYSTEMTIME.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public boolean GetFileCreateTimeStr(String var1, boolean var2, boolean var3, CkString var4) {
/*  809 */     return chilkatJNI.CkSFtp_GetFileCreateTimeStr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileCreateTimeStr(String var1, boolean var2, boolean var3) {
/*  813 */     return chilkatJNI.CkSFtp_getFileCreateTimeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileCreateTimeStr(String var1, boolean var2, boolean var3) {
/*  817 */     return chilkatJNI.CkSFtp_fileCreateTimeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileCreateTimeStrAsync(String var1, boolean var2, boolean var3) {
/*  821 */     long var4 = chilkatJNI.CkSFtp_GetFileCreateTimeStrAsync(this.swigCPtr, this, var1, var2, var3);
/*  822 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileGroup(String var1, boolean var2, boolean var3, CkString var4) {
/*  826 */     return chilkatJNI.CkSFtp_GetFileGroup(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileGroup(String var1, boolean var2, boolean var3) {
/*  830 */     return chilkatJNI.CkSFtp_getFileGroup(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileGroup(String var1, boolean var2, boolean var3) {
/*  834 */     return chilkatJNI.CkSFtp_fileGroup(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileGroupAsync(String var1, boolean var2, boolean var3) {
/*  838 */     long var4 = chilkatJNI.CkSFtp_GetFileGroupAsync(this.swigCPtr, this, var1, var2, var3);
/*  839 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileLastAccess(String var1, boolean var2, boolean var3, SYSTEMTIME var4) {
/*  843 */     return chilkatJNI.CkSFtp_GetFileLastAccess(this.swigCPtr, this, var1, var2, var3, SYSTEMTIME.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public CkDateTime GetFileLastAccessDt(String var1, boolean var2, boolean var3) {
/*  847 */     long var4 = chilkatJNI.CkSFtp_GetFileLastAccessDt(this.swigCPtr, this, var1, var2, var3);
/*  848 */     return var4 == 0L ? null : new CkDateTime(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask GetFileLastAccessDtAsync(String var1, boolean var2, boolean var3) {
/*  852 */     long var4 = chilkatJNI.CkSFtp_GetFileLastAccessDtAsync(this.swigCPtr, this, var1, var2, var3);
/*  853 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileLastAccessStr(String var1, boolean var2, boolean var3, CkString var4) {
/*  857 */     return chilkatJNI.CkSFtp_GetFileLastAccessStr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileLastAccessStr(String var1, boolean var2, boolean var3) {
/*  861 */     return chilkatJNI.CkSFtp_getFileLastAccessStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileLastAccessStr(String var1, boolean var2, boolean var3) {
/*  865 */     return chilkatJNI.CkSFtp_fileLastAccessStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileLastAccessStrAsync(String var1, boolean var2, boolean var3) {
/*  869 */     long var4 = chilkatJNI.CkSFtp_GetFileLastAccessStrAsync(this.swigCPtr, this, var1, var2, var3);
/*  870 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileLastModified(String var1, boolean var2, boolean var3, SYSTEMTIME var4) {
/*  874 */     return chilkatJNI.CkSFtp_GetFileLastModified(this.swigCPtr, this, var1, var2, var3, SYSTEMTIME.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public CkDateTime GetFileLastModifiedDt(String var1, boolean var2, boolean var3) {
/*  878 */     long var4 = chilkatJNI.CkSFtp_GetFileLastModifiedDt(this.swigCPtr, this, var1, var2, var3);
/*  879 */     return var4 == 0L ? null : new CkDateTime(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask GetFileLastModifiedDtAsync(String var1, boolean var2, boolean var3) {
/*  883 */     long var4 = chilkatJNI.CkSFtp_GetFileLastModifiedDtAsync(this.swigCPtr, this, var1, var2, var3);
/*  884 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileLastModifiedStr(String var1, boolean var2, boolean var3, CkString var4) {
/*  888 */     return chilkatJNI.CkSFtp_GetFileLastModifiedStr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileLastModifiedStr(String var1, boolean var2, boolean var3) {
/*  892 */     return chilkatJNI.CkSFtp_getFileLastModifiedStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileLastModifiedStr(String var1, boolean var2, boolean var3) {
/*  896 */     return chilkatJNI.CkSFtp_fileLastModifiedStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileLastModifiedStrAsync(String var1, boolean var2, boolean var3) {
/*  900 */     long var4 = chilkatJNI.CkSFtp_GetFileLastModifiedStrAsync(this.swigCPtr, this, var1, var2, var3);
/*  901 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileOwner(String var1, boolean var2, boolean var3, CkString var4) {
/*  905 */     return chilkatJNI.CkSFtp_GetFileOwner(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileOwner(String var1, boolean var2, boolean var3) {
/*  909 */     return chilkatJNI.CkSFtp_getFileOwner(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileOwner(String var1, boolean var2, boolean var3) {
/*  913 */     return chilkatJNI.CkSFtp_fileOwner(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFileOwnerAsync(String var1, boolean var2, boolean var3) {
/*  917 */     long var4 = chilkatJNI.CkSFtp_GetFileOwnerAsync(this.swigCPtr, this, var1, var2, var3);
/*  918 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public int GetFilePermissions(String var1, boolean var2, boolean var3) {
/*  922 */     return chilkatJNI.CkSFtp_GetFilePermissions(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask GetFilePermissionsAsync(String var1, boolean var2, boolean var3) {
/*  926 */     long var4 = chilkatJNI.CkSFtp_GetFilePermissionsAsync(this.swigCPtr, this, var1, var2, var3);
/*  927 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public int GetFileSize32(String var1, boolean var2, boolean var3) {
/*  931 */     return chilkatJNI.CkSFtp_GetFileSize32(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public long GetFileSize64(String var1, boolean var2, boolean var3) {
/*  935 */     return chilkatJNI.CkSFtp_GetFileSize64(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean GetFileSizeStr(String var1, boolean var2, boolean var3, CkString var4) {
/*  939 */     return chilkatJNI.CkSFtp_GetFileSizeStr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getFileSizeStr(String var1, boolean var2, boolean var3) {
/*  943 */     return chilkatJNI.CkSFtp_getFileSizeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String fileSizeStr(String var1, boolean var2, boolean var3) {
/*  947 */     return chilkatJNI.CkSFtp_fileSizeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean HardLink(String var1, String var2) {
/*  951 */     return chilkatJNI.CkSFtp_HardLink(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask HardLinkAsync(String var1, String var2) {
/*  955 */     long var3 = chilkatJNI.CkSFtp_HardLinkAsync(this.swigCPtr, this, var1, var2);
/*  956 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean InitializeSftp() {
/*  960 */     return chilkatJNI.CkSFtp_InitializeSftp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask InitializeSftpAsync() {
/*  964 */     long var1 = chilkatJNI.CkSFtp_InitializeSftpAsync(this.swigCPtr, this);
/*  965 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean LastReadFailed(String var1) {
/*  969 */     return chilkatJNI.CkSFtp_LastReadFailed(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int LastReadNumBytes(String var1) {
/*  973 */     return chilkatJNI.CkSFtp_LastReadNumBytes(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean OpenDir(String var1, CkString var2) {
/*  977 */     return chilkatJNI.CkSFtp_OpenDir(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String openDir(String var1) {
/*  981 */     return chilkatJNI.CkSFtp_openDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask OpenDirAsync(String var1) {
/*  985 */     long var2 = chilkatJNI.CkSFtp_OpenDirAsync(this.swigCPtr, this, var1);
/*  986 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean OpenFile(String var1, String var2, String var3, CkString var4) {
/*  990 */     return chilkatJNI.CkSFtp_OpenFile(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String openFile(String var1, String var2, String var3) {
/*  994 */     return chilkatJNI.CkSFtp_openFile(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask OpenFileAsync(String var1, String var2, String var3) {
/*  998 */     long var4 = chilkatJNI.CkSFtp_OpenFileAsync(this.swigCPtr, this, var1, var2, var3);
/*  999 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkSFtpDir ReadDir(String var1) {
/* 1003 */     long var2 = chilkatJNI.CkSFtp_ReadDir(this.swigCPtr, this, var1);
/* 1004 */     return var2 == 0L ? null : new CkSFtpDir(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask ReadDirAsync(String var1) {
/* 1008 */     long var2 = chilkatJNI.CkSFtp_ReadDirAsync(this.swigCPtr, this, var1);
/* 1009 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReadFileBytes(String var1, int var2, CkByteData var3) {
/* 1013 */     return chilkatJNI.CkSFtp_ReadFileBytes(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask ReadFileBytesAsync(String var1, int var2) {
/* 1017 */     long var3 = chilkatJNI.CkSFtp_ReadFileBytesAsync(this.swigCPtr, this, var1, var2);
/* 1018 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ReadFileBytes32(String var1, int var2, int var3, CkByteData var4) {
/* 1022 */     return chilkatJNI.CkSFtp_ReadFileBytes32(this.swigCPtr, this, var1, var2, var3, CkByteData.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public boolean ReadFileBytes64(String var1, long var2, int var4, CkByteData var5) {
/* 1026 */     return chilkatJNI.CkSFtp_ReadFileBytes64(this.swigCPtr, this, var1, var2, var4, CkByteData.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public boolean ReadFileBytes64s(String var1, String var2, int var3, CkByteData var4) {
/* 1030 */     return chilkatJNI.CkSFtp_ReadFileBytes64s(this.swigCPtr, this, var1, var2, var3, CkByteData.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public boolean ReadFileText(String var1, int var2, String var3, CkString var4) {
/* 1034 */     return chilkatJNI.CkSFtp_ReadFileText(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String readFileText(String var1, int var2, String var3) {
/* 1038 */     return chilkatJNI.CkSFtp_readFileText(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask ReadFileTextAsync(String var1, int var2, String var3) {
/* 1042 */     long var4 = chilkatJNI.CkSFtp_ReadFileTextAsync(this.swigCPtr, this, var1, var2, var3);
/* 1043 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean ReadFileText32(String var1, int var2, int var3, String var4, CkString var5) {
/* 1047 */     return chilkatJNI.CkSFtp_ReadFileText32(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public String readFileText32(String var1, int var2, int var3, String var4) {
/* 1051 */     return chilkatJNI.CkSFtp_readFileText32(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean ReadFileText64(String var1, long var2, int var4, String var5, CkString var6) {
/* 1055 */     return chilkatJNI.CkSFtp_ReadFileText64(this.swigCPtr, this, var1, var2, var4, var5, CkString.getCPtr(var6), var6);
/*      */   }
/*      */   
/*      */   public String readFileText64(String var1, long var2, int var4, String var5) {
/* 1059 */     return chilkatJNI.CkSFtp_readFileText64(this.swigCPtr, this, var1, var2, var4, var5);
/*      */   }
/*      */   
/*      */   public boolean ReadFileText64s(String var1, String var2, int var3, String var4, CkString var5) {
/* 1063 */     return chilkatJNI.CkSFtp_ReadFileText64s(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public String readFileText64s(String var1, String var2, int var3, String var4) {
/* 1067 */     return chilkatJNI.CkSFtp_readFileText64s(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean ReadLink(String var1, CkString var2) {
/* 1071 */     return chilkatJNI.CkSFtp_ReadLink(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String readLink(String var1) {
/* 1075 */     return chilkatJNI.CkSFtp_readLink(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReadLinkAsync(String var1) {
/* 1079 */     long var2 = chilkatJNI.CkSFtp_ReadLinkAsync(this.swigCPtr, this, var1);
/* 1080 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RealPath(String var1, String var2, CkString var3) {
/* 1084 */     return chilkatJNI.CkSFtp_RealPath(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String realPath(String var1, String var2) {
/* 1088 */     return chilkatJNI.CkSFtp_realPath(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask RealPathAsync(String var1, String var2) {
/* 1092 */     long var3 = chilkatJNI.CkSFtp_RealPathAsync(this.swigCPtr, this, var1, var2);
/* 1093 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean RemoveDir(String var1) {
/* 1097 */     return chilkatJNI.CkSFtp_RemoveDir(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask RemoveDirAsync(String var1) {
/* 1101 */     long var2 = chilkatJNI.CkSFtp_RemoveDirAsync(this.swigCPtr, this, var1);
/* 1102 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RemoveFile(String var1) {
/* 1106 */     return chilkatJNI.CkSFtp_RemoveFile(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask RemoveFileAsync(String var1) {
/* 1110 */     long var2 = chilkatJNI.CkSFtp_RemoveFileAsync(this.swigCPtr, this, var1);
/* 1111 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RenameFileOrDir(String var1, String var2) {
/* 1115 */     return chilkatJNI.CkSFtp_RenameFileOrDir(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask RenameFileOrDirAsync(String var1, String var2) {
/* 1119 */     long var3 = chilkatJNI.CkSFtp_RenameFileOrDirAsync(this.swigCPtr, this, var1, var2);
/* 1120 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ResumeDownloadFileByName(String var1, String var2) {
/* 1124 */     return chilkatJNI.CkSFtp_ResumeDownloadFileByName(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask ResumeDownloadFileByNameAsync(String var1, String var2) {
/* 1128 */     long var3 = chilkatJNI.CkSFtp_ResumeDownloadFileByNameAsync(this.swigCPtr, this, var1, var2);
/* 1129 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ResumeUploadFileByName(String var1, String var2) {
/* 1133 */     return chilkatJNI.CkSFtp_ResumeUploadFileByName(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask ResumeUploadFileByNameAsync(String var1, String var2) {
/* 1137 */     long var3 = chilkatJNI.CkSFtp_ResumeUploadFileByNameAsync(this.swigCPtr, this, var1, var2);
/* 1138 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1142 */     return chilkatJNI.CkSFtp_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SendIgnore() {
/* 1146 */     return chilkatJNI.CkSFtp_SendIgnore(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SendIgnoreAsync() {
/* 1150 */     long var1 = chilkatJNI.CkSFtp_SendIgnoreAsync(this.swigCPtr, this);
/* 1151 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SetCreateDt(String var1, boolean var2, CkDateTime var3) {
/* 1155 */     return chilkatJNI.CkSFtp_SetCreateDt(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask SetCreateDtAsync(String var1, boolean var2, CkDateTime var3) {
/* 1159 */     long var4 = chilkatJNI.CkSFtp_SetCreateDtAsync(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/* 1160 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetCreateTime(String var1, boolean var2, SYSTEMTIME var3) {
/* 1164 */     return chilkatJNI.CkSFtp_SetCreateTime(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean SetCreateTimeStr(String var1, boolean var2, String var3) {
/* 1168 */     return chilkatJNI.CkSFtp_SetCreateTimeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetCreateTimeStrAsync(String var1, boolean var2, String var3) {
/* 1172 */     long var4 = chilkatJNI.CkSFtp_SetCreateTimeStrAsync(this.swigCPtr, this, var1, var2, var3);
/* 1173 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetLastAccessDt(String var1, boolean var2, CkDateTime var3) {
/* 1177 */     return chilkatJNI.CkSFtp_SetLastAccessDt(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask SetLastAccessDtAsync(String var1, boolean var2, CkDateTime var3) {
/* 1181 */     long var4 = chilkatJNI.CkSFtp_SetLastAccessDtAsync(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/* 1182 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetLastAccessTime(String var1, boolean var2, SYSTEMTIME var3) {
/* 1186 */     return chilkatJNI.CkSFtp_SetLastAccessTime(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean SetLastAccessTimeStr(String var1, boolean var2, String var3) {
/* 1190 */     return chilkatJNI.CkSFtp_SetLastAccessTimeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetLastAccessTimeStrAsync(String var1, boolean var2, String var3) {
/* 1194 */     long var4 = chilkatJNI.CkSFtp_SetLastAccessTimeStrAsync(this.swigCPtr, this, var1, var2, var3);
/* 1195 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetLastModifiedDt(String var1, boolean var2, CkDateTime var3) {
/* 1199 */     return chilkatJNI.CkSFtp_SetLastModifiedDt(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask SetLastModifiedDtAsync(String var1, boolean var2, CkDateTime var3) {
/* 1203 */     long var4 = chilkatJNI.CkSFtp_SetLastModifiedDtAsync(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/* 1204 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetLastModifiedTime(String var1, boolean var2, SYSTEMTIME var3) {
/* 1208 */     return chilkatJNI.CkSFtp_SetLastModifiedTime(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean SetLastModifiedTimeStr(String var1, boolean var2, String var3) {
/* 1212 */     return chilkatJNI.CkSFtp_SetLastModifiedTimeStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetLastModifiedTimeStrAsync(String var1, boolean var2, String var3) {
/* 1216 */     long var4 = chilkatJNI.CkSFtp_SetLastModifiedTimeStrAsync(this.swigCPtr, this, var1, var2, var3);
/* 1217 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetOwnerAndGroup(String var1, boolean var2, String var3, String var4) {
/* 1221 */     return chilkatJNI.CkSFtp_SetOwnerAndGroup(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask SetOwnerAndGroupAsync(String var1, boolean var2, String var3, String var4) {
/* 1225 */     long var5 = chilkatJNI.CkSFtp_SetOwnerAndGroupAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1226 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean SetPermissions(String var1, boolean var2, int var3) {
/* 1230 */     return chilkatJNI.CkSFtp_SetPermissions(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetPermissionsAsync(String var1, boolean var2, int var3) {
/* 1234 */     long var4 = chilkatJNI.CkSFtp_SetPermissionsAsync(this.swigCPtr, this, var1, var2, var3);
/* 1235 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SymLink(String var1, String var2) {
/* 1239 */     return chilkatJNI.CkSFtp_SymLink(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SymLinkAsync(String var1, String var2) {
/* 1243 */     long var3 = chilkatJNI.CkSFtp_SymLinkAsync(this.swigCPtr, this, var1, var2);
/* 1244 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SyncTreeDownload(String var1, String var2, int var3, boolean var4) {
/* 1248 */     return chilkatJNI.CkSFtp_SyncTreeDownload(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask SyncTreeDownloadAsync(String var1, String var2, int var3, boolean var4) {
/* 1252 */     long var5 = chilkatJNI.CkSFtp_SyncTreeDownloadAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1253 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean SyncTreeUpload(String var1, String var2, int var3, boolean var4) {
/* 1257 */     return chilkatJNI.CkSFtp_SyncTreeUpload(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask SyncTreeUploadAsync(String var1, String var2, int var3, boolean var4) {
/* 1261 */     long var5 = chilkatJNI.CkSFtp_SyncTreeUploadAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1262 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1266 */     return chilkatJNI.CkSFtp_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UploadBd(CkBinData var1, String var2) {
/* 1270 */     return chilkatJNI.CkSFtp_UploadBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask UploadBdAsync(CkBinData var1, String var2) {
/* 1274 */     long var3 = chilkatJNI.CkSFtp_UploadBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/* 1275 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean UploadFile(String var1, String var2) {
/* 1279 */     return chilkatJNI.CkSFtp_UploadFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask UploadFileAsync(String var1, String var2) {
/* 1283 */     long var3 = chilkatJNI.CkSFtp_UploadFileAsync(this.swigCPtr, this, var1, var2);
/* 1284 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean UploadFileByName(String var1, String var2) {
/* 1288 */     return chilkatJNI.CkSFtp_UploadFileByName(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask UploadFileByNameAsync(String var1, String var2) {
/* 1292 */     long var3 = chilkatJNI.CkSFtp_UploadFileByNameAsync(this.swigCPtr, this, var1, var2);
/* 1293 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean UploadSb(CkStringBuilder var1, String var2, String var3, boolean var4) {
/* 1297 */     return chilkatJNI.CkSFtp_UploadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask UploadSbAsync(CkStringBuilder var1, String var2, String var3, boolean var4) {
/* 1301 */     long var5 = chilkatJNI.CkSFtp_UploadSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, var3, var4);
/* 1302 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean WriteFileBytes(String var1, CkByteData var2) {
/* 1306 */     return chilkatJNI.CkSFtp_WriteFileBytes(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask WriteFileBytesAsync(String var1, CkByteData var2) {
/* 1310 */     long var3 = chilkatJNI.CkSFtp_WriteFileBytesAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 1311 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean WriteFileBytes32(String var1, int var2, CkByteData var3) {
/* 1315 */     return chilkatJNI.CkSFtp_WriteFileBytes32(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean WriteFileBytes64(String var1, long var2, CkByteData var4) {
/* 1319 */     return chilkatJNI.CkSFtp_WriteFileBytes64(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public boolean WriteFileBytes64s(String var1, String var2, CkByteData var3) {
/* 1323 */     return chilkatJNI.CkSFtp_WriteFileBytes64s(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean WriteFileText(String var1, String var2, String var3) {
/* 1327 */     return chilkatJNI.CkSFtp_WriteFileText(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask WriteFileTextAsync(String var1, String var2, String var3) {
/* 1331 */     long var4 = chilkatJNI.CkSFtp_WriteFileTextAsync(this.swigCPtr, this, var1, var2, var3);
/* 1332 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean WriteFileText32(String var1, int var2, String var3, String var4) {
/* 1336 */     return chilkatJNI.CkSFtp_WriteFileText32(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean WriteFileText64(String var1, long var2, String var4, String var5) {
/* 1340 */     return chilkatJNI.CkSFtp_WriteFileText64(this.swigCPtr, this, var1, var2, var4, var5);
/*      */   }
/*      */   
/*      */   public boolean WriteFileText64s(String var1, String var2, String var3, String var4) {
/* 1344 */     return chilkatJNI.CkSFtp_WriteFileText64s(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSFtp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */