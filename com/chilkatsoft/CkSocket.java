/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkSocket
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkSocket(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkSocket var0) {
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
/*   29 */         chilkatJNI.delete_CkSocket(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkSocket()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkSocket(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkSocket_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkSocket_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkSocket_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*   54 */     chilkatJNI.CkSocket_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkSocket_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkSocket_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_AcceptFailReason() {
/*   66 */     return chilkatJNI.CkSocket_get_AcceptFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncAcceptFinished() {
/*   70 */     return chilkatJNI.CkSocket_get_AsyncAcceptFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncAcceptLog(CkString var1) {
/*   74 */     chilkatJNI.CkSocket_get_AsyncAcceptLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncAcceptLog() {
/*   78 */     return chilkatJNI.CkSocket_asyncAcceptLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncAcceptSuccess() {
/*   82 */     return chilkatJNI.CkSocket_get_AsyncAcceptSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncConnectFinished() {
/*   86 */     return chilkatJNI.CkSocket_get_AsyncConnectFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncConnectLog(CkString var1) {
/*   90 */     chilkatJNI.CkSocket_get_AsyncConnectLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncConnectLog() {
/*   94 */     return chilkatJNI.CkSocket_asyncConnectLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncConnectSuccess() {
/*   98 */     return chilkatJNI.CkSocket_get_AsyncConnectSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncDnsFinished() {
/*  102 */     return chilkatJNI.CkSocket_get_AsyncDnsFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncDnsLog(CkString var1) {
/*  106 */     chilkatJNI.CkSocket_get_AsyncDnsLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncDnsLog() {
/*  110 */     return chilkatJNI.CkSocket_asyncDnsLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncDnsResult(CkString var1) {
/*  114 */     chilkatJNI.CkSocket_get_AsyncDnsResult(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncDnsResult() {
/*  118 */     return chilkatJNI.CkSocket_asyncDnsResult(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncDnsSuccess() {
/*  122 */     return chilkatJNI.CkSocket_get_AsyncDnsSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncReceivedBytes(CkByteData var1) {
/*  126 */     chilkatJNI.CkSocket_get_AsyncReceivedBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_AsyncReceivedString(CkString var1) {
/*  130 */     chilkatJNI.CkSocket_get_AsyncReceivedString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncReceivedString() {
/*  134 */     return chilkatJNI.CkSocket_asyncReceivedString(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncReceiveFinished() {
/*  138 */     return chilkatJNI.CkSocket_get_AsyncReceiveFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncReceiveLog(CkString var1) {
/*  142 */     chilkatJNI.CkSocket_get_AsyncReceiveLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncReceiveLog() {
/*  146 */     return chilkatJNI.CkSocket_asyncReceiveLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncReceiveSuccess() {
/*  150 */     return chilkatJNI.CkSocket_get_AsyncReceiveSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncSendFinished() {
/*  154 */     return chilkatJNI.CkSocket_get_AsyncSendFinished(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AsyncSendLog(CkString var1) {
/*  158 */     chilkatJNI.CkSocket_get_AsyncSendLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String asyncSendLog() {
/*  162 */     return chilkatJNI.CkSocket_asyncSendLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_AsyncSendSuccess() {
/*  166 */     return chilkatJNI.CkSocket_get_AsyncSendSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleDown() {
/*  170 */     return chilkatJNI.CkSocket_get_BandwidthThrottleDown(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleDown(int var1) {
/*  174 */     chilkatJNI.CkSocket_put_BandwidthThrottleDown(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BandwidthThrottleUp() {
/*  178 */     return chilkatJNI.CkSocket_get_BandwidthThrottleUp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BandwidthThrottleUp(int var1) {
/*  182 */     chilkatJNI.CkSocket_put_BandwidthThrottleUp(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_BigEndian() {
/*  186 */     return chilkatJNI.CkSocket_get_BigEndian(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BigEndian(boolean var1) {
/*  190 */     chilkatJNI.CkSocket_put_BigEndian(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  194 */     chilkatJNI.CkSocket_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  198 */     return chilkatJNI.CkSocket_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  202 */     chilkatJNI.CkSocket_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ClientPort() {
/*  206 */     return chilkatJNI.CkSocket_get_ClientPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientPort(int var1) {
/*  210 */     chilkatJNI.CkSocket_put_ClientPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectFailReason() {
/*  214 */     return chilkatJNI.CkSocket_get_ConnectFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_DebugConnectDelayMs() {
/*  218 */     return chilkatJNI.CkSocket_get_DebugConnectDelayMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugConnectDelayMs(int var1) {
/*  222 */     chilkatJNI.CkSocket_put_DebugConnectDelayMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_DebugDnsDelayMs() {
/*  226 */     return chilkatJNI.CkSocket_get_DebugDnsDelayMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugDnsDelayMs(int var1) {
/*  230 */     chilkatJNI.CkSocket_put_DebugDnsDelayMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  234 */     chilkatJNI.CkSocket_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  238 */     return chilkatJNI.CkSocket_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  242 */     chilkatJNI.CkSocket_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ElapsedSeconds() {
/*  246 */     return chilkatJNI.CkSocket_get_ElapsedSeconds(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  250 */     return chilkatJNI.CkSocket_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  254 */     chilkatJNI.CkSocket_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyAuthMethod(CkString var1) {
/*  258 */     chilkatJNI.CkSocket_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyAuthMethod() {
/*  262 */     return chilkatJNI.CkSocket_httpProxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyAuthMethod(String var1) {
/*  266 */     chilkatJNI.CkSocket_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyDomain(CkString var1) {
/*  270 */     chilkatJNI.CkSocket_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyDomain() {
/*  274 */     return chilkatJNI.CkSocket_httpProxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyDomain(String var1) {
/*  278 */     chilkatJNI.CkSocket_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_HttpProxyForHttp() {
/*  282 */     return chilkatJNI.CkSocket_get_HttpProxyForHttp(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyForHttp(boolean var1) {
/*  286 */     chilkatJNI.CkSocket_put_HttpProxyForHttp(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyHostname(CkString var1) {
/*  290 */     chilkatJNI.CkSocket_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyHostname() {
/*  294 */     return chilkatJNI.CkSocket_httpProxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyHostname(String var1) {
/*  298 */     chilkatJNI.CkSocket_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyPassword(CkString var1) {
/*  302 */     chilkatJNI.CkSocket_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyPassword() {
/*  306 */     return chilkatJNI.CkSocket_httpProxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPassword(String var1) {
/*  310 */     chilkatJNI.CkSocket_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HttpProxyPort() {
/*  314 */     return chilkatJNI.CkSocket_get_HttpProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPort(int var1) {
/*  318 */     chilkatJNI.CkSocket_put_HttpProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyUsername(CkString var1) {
/*  322 */     chilkatJNI.CkSocket_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyUsername() {
/*  326 */     return chilkatJNI.CkSocket_httpProxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyUsername(String var1) {
/*  330 */     chilkatJNI.CkSocket_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IsConnected() {
/*  334 */     return chilkatJNI.CkSocket_get_IsConnected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_KeepAlive() {
/*  338 */     return chilkatJNI.CkSocket_get_KeepAlive(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepAlive(boolean var1) {
/*  342 */     chilkatJNI.CkSocket_put_KeepAlive(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_KeepSessionLog() {
/*  346 */     return chilkatJNI.CkSocket_get_KeepSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepSessionLog(boolean var1) {
/*  350 */     chilkatJNI.CkSocket_put_KeepSessionLog(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  354 */     chilkatJNI.CkSocket_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  358 */     return chilkatJNI.CkSocket_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  362 */     chilkatJNI.CkSocket_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  366 */     return chilkatJNI.CkSocket_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  370 */     chilkatJNI.CkSocket_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  374 */     return chilkatJNI.CkSocket_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodFailed() {
/*  378 */     return chilkatJNI.CkSocket_get_LastMethodFailed(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  382 */     return chilkatJNI.CkSocket_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  386 */     chilkatJNI.CkSocket_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ListenIpv6() {
/*  390 */     return chilkatJNI.CkSocket_get_ListenIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ListenIpv6(boolean var1) {
/*  394 */     chilkatJNI.CkSocket_put_ListenIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ListenPort() {
/*  398 */     return chilkatJNI.CkSocket_get_ListenPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LocalIpAddress(CkString var1) {
/*  402 */     chilkatJNI.CkSocket_get_LocalIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String localIpAddress() {
/*  406 */     return chilkatJNI.CkSocket_localIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_LocalPort() {
/*  410 */     return chilkatJNI.CkSocket_get_LocalPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_MaxReadIdleMs() {
/*  414 */     return chilkatJNI.CkSocket_get_MaxReadIdleMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxReadIdleMs(int var1) {
/*  418 */     chilkatJNI.CkSocket_put_MaxReadIdleMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxSendIdleMs() {
/*  422 */     return chilkatJNI.CkSocket_get_MaxSendIdleMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxSendIdleMs(int var1) {
/*  426 */     chilkatJNI.CkSocket_put_MaxSendIdleMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_MyIpAddress(CkString var1) {
/*  430 */     chilkatJNI.CkSocket_get_MyIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String myIpAddress() {
/*  434 */     return chilkatJNI.CkSocket_myIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumReceivedClientCerts() {
/*  438 */     return chilkatJNI.CkSocket_get_NumReceivedClientCerts(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumSocketsInSet() {
/*  442 */     return chilkatJNI.CkSocket_get_NumSocketsInSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumSslAcceptableClientCAs() {
/*  446 */     return chilkatJNI.CkSocket_get_NumSslAcceptableClientCAs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_ObjectId() {
/*  450 */     return chilkatJNI.CkSocket_get_ObjectId(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  454 */     return chilkatJNI.CkSocket_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  458 */     chilkatJNI.CkSocket_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  462 */     return chilkatJNI.CkSocket_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  466 */     chilkatJNI.CkSocket_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReceivedCount() {
/*  470 */     return chilkatJNI.CkSocket_get_ReceivedCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReceivedCount(int var1) {
/*  474 */     chilkatJNI.CkSocket_put_ReceivedCount(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReceivedInt() {
/*  478 */     return chilkatJNI.CkSocket_get_ReceivedInt(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReceivedInt(int var1) {
/*  482 */     chilkatJNI.CkSocket_put_ReceivedInt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReceiveFailReason() {
/*  486 */     return chilkatJNI.CkSocket_get_ReceiveFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_ReceivePacketSize() {
/*  490 */     return chilkatJNI.CkSocket_get_ReceivePacketSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReceivePacketSize(int var1) {
/*  494 */     chilkatJNI.CkSocket_put_ReceivePacketSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_RemoteIpAddress(CkString var1) {
/*  498 */     chilkatJNI.CkSocket_get_RemoteIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String remoteIpAddress() {
/*  502 */     return chilkatJNI.CkSocket_remoteIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_RemotePort() {
/*  506 */     return chilkatJNI.CkSocket_get_RemotePort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_RequireSslCertVerify() {
/*  510 */     return chilkatJNI.CkSocket_get_RequireSslCertVerify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequireSslCertVerify(boolean var1) {
/*  514 */     chilkatJNI.CkSocket_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SelectorIndex() {
/*  518 */     return chilkatJNI.CkSocket_get_SelectorIndex(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SelectorIndex(int var1) {
/*  522 */     chilkatJNI.CkSocket_put_SelectorIndex(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SelectorReadIndex() {
/*  526 */     return chilkatJNI.CkSocket_get_SelectorReadIndex(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SelectorReadIndex(int var1) {
/*  530 */     chilkatJNI.CkSocket_put_SelectorReadIndex(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SelectorWriteIndex() {
/*  534 */     return chilkatJNI.CkSocket_get_SelectorWriteIndex(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SelectorWriteIndex(int var1) {
/*  538 */     chilkatJNI.CkSocket_put_SelectorWriteIndex(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SendFailReason() {
/*  542 */     return chilkatJNI.CkSocket_get_SendFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_SendPacketSize() {
/*  546 */     return chilkatJNI.CkSocket_get_SendPacketSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendPacketSize(int var1) {
/*  550 */     chilkatJNI.CkSocket_put_SendPacketSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SessionLog(CkString var1) {
/*  554 */     chilkatJNI.CkSocket_get_SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLog() {
/*  558 */     return chilkatJNI.CkSocket_sessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SessionLogEncoding(CkString var1) {
/*  562 */     chilkatJNI.CkSocket_get_SessionLogEncoding(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLogEncoding() {
/*  566 */     return chilkatJNI.CkSocket_sessionLogEncoding(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SessionLogEncoding(String var1) {
/*  570 */     chilkatJNI.CkSocket_put_SessionLogEncoding(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  574 */     chilkatJNI.CkSocket_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  578 */     return chilkatJNI.CkSocket_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  582 */     chilkatJNI.CkSocket_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  586 */     chilkatJNI.CkSocket_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  590 */     return chilkatJNI.CkSocket_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  594 */     chilkatJNI.CkSocket_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  598 */     return chilkatJNI.CkSocket_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  602 */     chilkatJNI.CkSocket_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  606 */     chilkatJNI.CkSocket_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  610 */     return chilkatJNI.CkSocket_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  614 */     chilkatJNI.CkSocket_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  618 */     return chilkatJNI.CkSocket_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  622 */     chilkatJNI.CkSocket_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  626 */     return chilkatJNI.CkSocket_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  630 */     chilkatJNI.CkSocket_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SoReuseAddr() {
/*  634 */     return chilkatJNI.CkSocket_get_SoReuseAddr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoReuseAddr(boolean var1) {
/*  638 */     chilkatJNI.CkSocket_put_SoReuseAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  642 */     return chilkatJNI.CkSocket_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  646 */     chilkatJNI.CkSocket_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_Ssl() {
/*  650 */     return chilkatJNI.CkSocket_get_Ssl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Ssl(boolean var1) {
/*  654 */     chilkatJNI.CkSocket_put_Ssl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslAllowedCiphers(CkString var1) {
/*  658 */     chilkatJNI.CkSocket_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslAllowedCiphers() {
/*  662 */     return chilkatJNI.CkSocket_sslAllowedCiphers(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslAllowedCiphers(String var1) {
/*  666 */     chilkatJNI.CkSocket_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslProtocol(CkString var1) {
/*  670 */     chilkatJNI.CkSocket_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslProtocol() {
/*  674 */     return chilkatJNI.CkSocket_sslProtocol(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslProtocol(String var1) {
/*  678 */     chilkatJNI.CkSocket_put_SslProtocol(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_StringCharset(CkString var1) {
/*  682 */     chilkatJNI.CkSocket_get_StringCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String stringCharset() {
/*  686 */     return chilkatJNI.CkSocket_stringCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_StringCharset(String var1) {
/*  690 */     chilkatJNI.CkSocket_put_StringCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_TcpNoDelay() {
/*  694 */     return chilkatJNI.CkSocket_get_TcpNoDelay(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TcpNoDelay(boolean var1) {
/*  698 */     chilkatJNI.CkSocket_put_TcpNoDelay(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsCipherSuite(CkString var1) {
/*  702 */     chilkatJNI.CkSocket_get_TlsCipherSuite(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsCipherSuite() {
/*  706 */     return chilkatJNI.CkSocket_tlsCipherSuite(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsPinSet(CkString var1) {
/*  710 */     chilkatJNI.CkSocket_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsPinSet() {
/*  714 */     return chilkatJNI.CkSocket_tlsPinSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TlsPinSet(String var1) {
/*  718 */     chilkatJNI.CkSocket_put_TlsPinSet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsVersion(CkString var1) {
/*  722 */     chilkatJNI.CkSocket_get_TlsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsVersion() {
/*  726 */     return chilkatJNI.CkSocket_tlsVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_UserData(CkString var1) {
/*  730 */     chilkatJNI.CkSocket_get_UserData(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String userData() {
/*  734 */     return chilkatJNI.CkSocket_userData(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UserData(String var1) {
/*  738 */     chilkatJNI.CkSocket_put_UserData(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  742 */     return chilkatJNI.CkSocket_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  746 */     chilkatJNI.CkSocket_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  750 */     chilkatJNI.CkSocket_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  754 */     return chilkatJNI.CkSocket_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkSocket AcceptNextConnection(int var1) {
/*  758 */     long var2 = chilkatJNI.CkSocket_AcceptNextConnection(this.swigCPtr, this, var1);
/*  759 */     return var2 == 0L ? null : new CkSocket(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask AcceptNextConnectionAsync(int var1) {
/*  763 */     long var2 = chilkatJNI.CkSocket_AcceptNextConnectionAsync(this.swigCPtr, this, var1);
/*  764 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean AddSslAcceptableClientCaDn(String var1) {
/*  768 */     return chilkatJNI.CkSocket_AddSslAcceptableClientCaDn(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void AsyncAcceptAbort() {
/*  772 */     chilkatJNI.CkSocket_AsyncAcceptAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkSocket AsyncAcceptSocket() {
/*  776 */     long var1 = chilkatJNI.CkSocket_AsyncAcceptSocket(this.swigCPtr, this);
/*  777 */     return var1 == 0L ? null : new CkSocket(var1, true);
/*      */   }
/*      */   
/*      */   public boolean AsyncAcceptStart(int var1) {
/*  781 */     return chilkatJNI.CkSocket_AsyncAcceptStart(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void AsyncConnectAbort() {
/*  785 */     chilkatJNI.CkSocket_AsyncConnectAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncConnectStart(String var1, int var2, boolean var3, int var4) {
/*  789 */     return chilkatJNI.CkSocket_AsyncConnectStart(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public void AsyncDnsAbort() {
/*  793 */     chilkatJNI.CkSocket_AsyncDnsAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncDnsStart(String var1, int var2) {
/*  797 */     return chilkatJNI.CkSocket_AsyncDnsStart(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void AsyncReceiveAbort() {
/*  801 */     chilkatJNI.CkSocket_AsyncReceiveAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncReceiveBytes() {
/*  805 */     return chilkatJNI.CkSocket_AsyncReceiveBytes(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncReceiveBytesN(long var1) {
/*  809 */     return chilkatJNI.CkSocket_AsyncReceiveBytesN(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AsyncReceiveString() {
/*  813 */     return chilkatJNI.CkSocket_AsyncReceiveString(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncReceiveToCRLF() {
/*  817 */     return chilkatJNI.CkSocket_AsyncReceiveToCRLF(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncReceiveUntilMatch(String var1) {
/*  821 */     return chilkatJNI.CkSocket_AsyncReceiveUntilMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void AsyncSendAbort() {
/*  825 */     chilkatJNI.CkSocket_AsyncSendAbort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AsyncSendByteData(CkByteData var1) {
/*  829 */     return chilkatJNI.CkSocket_AsyncSendByteData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean AsyncSendBytes(CkByteData var1) {
/*  833 */     return chilkatJNI.CkSocket_AsyncSendBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean AsyncSendString(String var1) {
/*  837 */     return chilkatJNI.CkSocket_AsyncSendString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean BindAndListen(int var1, int var2) {
/*  841 */     return chilkatJNI.CkSocket_BindAndListen(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask BindAndListenAsync(int var1, int var2) {
/*  845 */     long var3 = chilkatJNI.CkSocket_BindAndListenAsync(this.swigCPtr, this, var1, var2);
/*  846 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public int BindAndListenPortRange(int var1, int var2, int var3) {
/*  850 */     return chilkatJNI.CkSocket_BindAndListenPortRange(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask BindAndListenPortRangeAsync(int var1, int var2, int var3) {
/*  854 */     long var4 = chilkatJNI.CkSocket_BindAndListenPortRangeAsync(this.swigCPtr, this, var1, var2, var3);
/*  855 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean BuildHttpGetRequest(String var1, CkString var2) {
/*  859 */     return chilkatJNI.CkSocket_BuildHttpGetRequest(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String buildHttpGetRequest(String var1) {
/*  863 */     return chilkatJNI.CkSocket_buildHttpGetRequest(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int CheckWriteable(int var1) {
/*  867 */     return chilkatJNI.CkSocket_CheckWriteable(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CheckWriteableAsync(int var1) {
/*  871 */     long var2 = chilkatJNI.CkSocket_CheckWriteableAsync(this.swigCPtr, this, var1);
/*  872 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public void ClearSessionLog() {
/*  876 */     chilkatJNI.CkSocket_ClearSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkSocket CloneSocket() {
/*  880 */     long var1 = chilkatJNI.CkSocket_CloneSocket(this.swigCPtr, this);
/*  881 */     return var1 == 0L ? null : new CkSocket(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Close(int var1) {
/*  885 */     return chilkatJNI.CkSocket_Close(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CloseAsync(int var1) {
/*  889 */     long var2 = chilkatJNI.CkSocket_CloseAsync(this.swigCPtr, this, var1);
/*  890 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Connect(String var1, int var2, boolean var3, int var4) {
/*  894 */     return chilkatJNI.CkSocket_Connect(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask ConnectAsync(String var1, int var2, boolean var3, int var4) {
/*  898 */     long var5 = chilkatJNI.CkSocket_ConnectAsync(this.swigCPtr, this, var1, var2, var3, var4);
/*  899 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean ConvertFromSsl() {
/*  903 */     return chilkatJNI.CkSocket_ConvertFromSsl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ConvertFromSslAsync() {
/*  907 */     long var1 = chilkatJNI.CkSocket_ConvertFromSslAsync(this.swigCPtr, this);
/*  908 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ConvertToSsl() {
/*  912 */     return chilkatJNI.CkSocket_ConvertToSsl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ConvertToSslAsync() {
/*  916 */     long var1 = chilkatJNI.CkSocket_ConvertToSslAsync(this.swigCPtr, this);
/*  917 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public void DnsCacheClear() {
/*  921 */     chilkatJNI.CkSocket_DnsCacheClear(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean DnsLookup(String var1, int var2, CkString var3) {
/*  925 */     return chilkatJNI.CkSocket_DnsLookup(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String dnsLookup(String var1, int var2) {
/*  929 */     return chilkatJNI.CkSocket_dnsLookup(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask DnsLookupAsync(String var1, int var2) {
/*  933 */     long var3 = chilkatJNI.CkSocket_DnsLookupAsync(this.swigCPtr, this, var1, var2);
/*  934 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkCert GetMyCert() {
/*  938 */     long var1 = chilkatJNI.CkSocket_GetMyCert(this.swigCPtr, this);
/*  939 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public CkCert GetReceivedClientCert(int var1) {
/*  943 */     long var2 = chilkatJNI.CkSocket_GetReceivedClientCert(this.swigCPtr, this, var1);
/*  944 */     return var2 == 0L ? null : new CkCert(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetSslAcceptableClientCaDn(int var1, CkString var2) {
/*  948 */     return chilkatJNI.CkSocket_GetSslAcceptableClientCaDn(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getSslAcceptableClientCaDn(int var1) {
/*  952 */     return chilkatJNI.CkSocket_getSslAcceptableClientCaDn(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String sslAcceptableClientCaDn(int var1) {
/*  956 */     return chilkatJNI.CkSocket_sslAcceptableClientCaDn(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert GetSslServerCert() {
/*  960 */     long var1 = chilkatJNI.CkSocket_GetSslServerCert(this.swigCPtr, this);
/*  961 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean InitSslServer(CkCert var1) {
/*  965 */     return chilkatJNI.CkSocket_InitSslServer(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/*  969 */     return chilkatJNI.CkSocket_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean LoadTaskResult(CkTask var1) {
/*  973 */     return chilkatJNI.CkSocket_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean PollDataAvailable() {
/*  977 */     return chilkatJNI.CkSocket_PollDataAvailable(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask PollDataAvailableAsync() {
/*  981 */     long var1 = chilkatJNI.CkSocket_PollDataAvailableAsync(this.swigCPtr, this);
/*  982 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBd(CkBinData var1) {
/*  986 */     return chilkatJNI.CkSocket_ReceiveBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBdAsync(CkBinData var1) {
/*  990 */     long var2 = chilkatJNI.CkSocket_ReceiveBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*  991 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBdN(long var1, CkBinData var3) {
/*  995 */     return chilkatJNI.CkSocket_ReceiveBdN(this.swigCPtr, this, var1, CkBinData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBdNAsync(long var1, CkBinData var3) {
/*  999 */     long var4 = chilkatJNI.CkSocket_ReceiveBdNAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var3), var3);
/* 1000 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveByte(boolean var1) {
/* 1004 */     return chilkatJNI.CkSocket_ReceiveByte(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveByteAsync(boolean var1) {
/* 1008 */     long var2 = chilkatJNI.CkSocket_ReceiveByteAsync(this.swigCPtr, this, var1);
/* 1009 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBytes(CkByteData var1) {
/* 1013 */     return chilkatJNI.CkSocket_ReceiveBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBytesAsync() {
/* 1017 */     long var1 = chilkatJNI.CkSocket_ReceiveBytesAsync(this.swigCPtr, this);
/* 1018 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBytesENC(String var1, CkString var2) {
/* 1022 */     return chilkatJNI.CkSocket_ReceiveBytesENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String receiveBytesENC(String var1) {
/* 1026 */     return chilkatJNI.CkSocket_receiveBytesENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBytesENCAsync(String var1) {
/* 1030 */     long var2 = chilkatJNI.CkSocket_ReceiveBytesENCAsync(this.swigCPtr, this, var1);
/* 1031 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBytesN(long var1, CkByteData var3) {
/* 1035 */     return chilkatJNI.CkSocket_ReceiveBytesN(this.swigCPtr, this, var1, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBytesNAsync(long var1) {
/* 1039 */     long var3 = chilkatJNI.CkSocket_ReceiveBytesNAsync(this.swigCPtr, this, var1);
/* 1040 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveBytesToFile(String var1) {
/* 1044 */     return chilkatJNI.CkSocket_ReceiveBytesToFile(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveBytesToFileAsync(String var1) {
/* 1048 */     long var2 = chilkatJNI.CkSocket_ReceiveBytesToFileAsync(this.swigCPtr, this, var1);
/* 1049 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public int ReceiveCount() {
/* 1053 */     return chilkatJNI.CkSocket_ReceiveCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveCountAsync() {
/* 1057 */     long var1 = chilkatJNI.CkSocket_ReceiveCountAsync(this.swigCPtr, this);
/* 1058 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveInt16(boolean var1, boolean var2) {
/* 1062 */     return chilkatJNI.CkSocket_ReceiveInt16(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveInt16Async(boolean var1, boolean var2) {
/* 1066 */     long var3 = chilkatJNI.CkSocket_ReceiveInt16Async(this.swigCPtr, this, var1, var2);
/* 1067 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveInt32(boolean var1) {
/* 1071 */     return chilkatJNI.CkSocket_ReceiveInt32(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveInt32Async(boolean var1) {
/* 1075 */     long var2 = chilkatJNI.CkSocket_ReceiveInt32Async(this.swigCPtr, this, var1);
/* 1076 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveNBytesENC(long var1, String var3, CkString var4) {
/* 1080 */     return chilkatJNI.CkSocket_ReceiveNBytesENC(this.swigCPtr, this, var1, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String receiveNBytesENC(long var1, String var3) {
/* 1084 */     return chilkatJNI.CkSocket_receiveNBytesENC(this.swigCPtr, this, var1, var3);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveNBytesENCAsync(long var1, String var3) {
/* 1088 */     long var4 = chilkatJNI.CkSocket_ReceiveNBytesENCAsync(this.swigCPtr, this, var1, var3);
/* 1089 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveSb(CkStringBuilder var1) {
/* 1093 */     return chilkatJNI.CkSocket_ReceiveSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveSbAsync(CkStringBuilder var1) {
/* 1097 */     long var2 = chilkatJNI.CkSocket_ReceiveSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/* 1098 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveString(CkString var1) {
/* 1102 */     return chilkatJNI.CkSocket_ReceiveString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String receiveString() {
/* 1106 */     return chilkatJNI.CkSocket_receiveString(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveStringAsync() {
/* 1110 */     long var1 = chilkatJNI.CkSocket_ReceiveStringAsync(this.swigCPtr, this);
/* 1111 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveStringMaxN(int var1, CkString var2) {
/* 1115 */     return chilkatJNI.CkSocket_ReceiveStringMaxN(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String receiveStringMaxN(int var1) {
/* 1119 */     return chilkatJNI.CkSocket_receiveStringMaxN(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveStringMaxNAsync(int var1) {
/* 1123 */     long var2 = chilkatJNI.CkSocket_ReceiveStringMaxNAsync(this.swigCPtr, this, var1);
/* 1124 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveStringUntilByte(int var1, CkString var2) {
/* 1128 */     return chilkatJNI.CkSocket_ReceiveStringUntilByte(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String receiveStringUntilByte(int var1) {
/* 1132 */     return chilkatJNI.CkSocket_receiveStringUntilByte(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveStringUntilByteAsync(int var1) {
/* 1136 */     long var2 = chilkatJNI.CkSocket_ReceiveStringUntilByteAsync(this.swigCPtr, this, var1);
/* 1137 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveToCRLF(CkString var1) {
/* 1141 */     return chilkatJNI.CkSocket_ReceiveToCRLF(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String receiveToCRLF() {
/* 1145 */     return chilkatJNI.CkSocket_receiveToCRLF(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveToCRLFAsync() {
/* 1149 */     long var1 = chilkatJNI.CkSocket_ReceiveToCRLFAsync(this.swigCPtr, this);
/* 1150 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveUntilByte(int var1, CkByteData var2) {
/* 1154 */     return chilkatJNI.CkSocket_ReceiveUntilByte(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveUntilByteAsync(int var1) {
/* 1158 */     long var2 = chilkatJNI.CkSocket_ReceiveUntilByteAsync(this.swigCPtr, this, var1);
/* 1159 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean ReceiveUntilMatch(String var1, CkString var2) {
/* 1163 */     return chilkatJNI.CkSocket_ReceiveUntilMatch(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String receiveUntilMatch(String var1) {
/* 1167 */     return chilkatJNI.CkSocket_receiveUntilMatch(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ReceiveUntilMatchAsync(String var1) {
/* 1171 */     long var2 = chilkatJNI.CkSocket_ReceiveUntilMatchAsync(this.swigCPtr, this, var1);
/* 1172 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1176 */     return chilkatJNI.CkSocket_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int SelectForReading(int var1) {
/* 1180 */     return chilkatJNI.CkSocket_SelectForReading(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SelectForReadingAsync(int var1) {
/* 1184 */     long var2 = chilkatJNI.CkSocket_SelectForReadingAsync(this.swigCPtr, this, var1);
/* 1185 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public int SelectForWriting(int var1) {
/* 1189 */     return chilkatJNI.CkSocket_SelectForWriting(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SelectForWritingAsync(int var1) {
/* 1193 */     long var2 = chilkatJNI.CkSocket_SelectForWritingAsync(this.swigCPtr, this, var1);
/* 1194 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendBd(CkBinData var1, long var2, long var4) {
/* 1198 */     return chilkatJNI.CkSocket_SendBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2, var4);
/*      */   }
/*      */   
/*      */   public CkTask SendBdAsync(CkBinData var1, long var2, long var4) {
/* 1202 */     long var6 = chilkatJNI.CkSocket_SendBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2, var4);
/* 1203 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public boolean SendByte(int var1) {
/* 1207 */     return chilkatJNI.CkSocket_SendByte(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SendByteAsync(int var1) {
/* 1211 */     long var2 = chilkatJNI.CkSocket_SendByteAsync(this.swigCPtr, this, var1);
/* 1212 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendBytes(CkByteData var1) {
/* 1216 */     return chilkatJNI.CkSocket_SendBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask SendBytesAsync(CkByteData var1) {
/* 1220 */     long var2 = chilkatJNI.CkSocket_SendBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 1221 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendBytesENC(String var1, String var2) {
/* 1225 */     return chilkatJNI.CkSocket_SendBytesENC(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SendBytesENCAsync(String var1, String var2) {
/* 1229 */     long var3 = chilkatJNI.CkSocket_SendBytesENCAsync(this.swigCPtr, this, var1, var2);
/* 1230 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SendCount(int var1) {
/* 1234 */     return chilkatJNI.CkSocket_SendCount(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SendCountAsync(int var1) {
/* 1238 */     long var2 = chilkatJNI.CkSocket_SendCountAsync(this.swigCPtr, this, var1);
/* 1239 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendInt16(int var1, boolean var2) {
/* 1243 */     return chilkatJNI.CkSocket_SendInt16(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SendInt16Async(int var1, boolean var2) {
/* 1247 */     long var3 = chilkatJNI.CkSocket_SendInt16Async(this.swigCPtr, this, var1, var2);
/* 1248 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SendInt32(int var1, boolean var2) {
/* 1252 */     return chilkatJNI.CkSocket_SendInt32(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SendInt32Async(int var1, boolean var2) {
/* 1256 */     long var3 = chilkatJNI.CkSocket_SendInt32Async(this.swigCPtr, this, var1, var2);
/* 1257 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SendSb(CkStringBuilder var1) {
/* 1261 */     return chilkatJNI.CkSocket_SendSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask SendSbAsync(CkStringBuilder var1) {
/* 1265 */     long var2 = chilkatJNI.CkSocket_SendSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/* 1266 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendString(String var1) {
/* 1270 */     return chilkatJNI.CkSocket_SendString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SendStringAsync(String var1) {
/* 1274 */     long var2 = chilkatJNI.CkSocket_SendStringAsync(this.swigCPtr, this, var1);
/* 1275 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendWakeOnLan(String var1, int var2, String var3) {
/* 1279 */     return chilkatJNI.CkSocket_SendWakeOnLan(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCert(CkCert var1) {
/* 1283 */     return chilkatJNI.CkSocket_SetSslClientCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPem(String var1, String var2) {
/* 1287 */     return chilkatJNI.CkSocket_SetSslClientCertPem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPfx(String var1, String var2) {
/* 1291 */     return chilkatJNI.CkSocket_SetSslClientCertPfx(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void SleepMs(int var1) {
/* 1295 */     chilkatJNI.CkSocket_SleepMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePk(String var1, CkSshKey var2) {
/* 1299 */     return chilkatJNI.CkSocket_SshAuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePkAsync(String var1, CkSshKey var2) {
/* 1303 */     long var3 = chilkatJNI.CkSocket_SshAuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/* 1304 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePw(String var1, String var2) {
/* 1308 */     return chilkatJNI.CkSocket_SshAuthenticatePw(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePwAsync(String var1, String var2) {
/* 1312 */     long var3 = chilkatJNI.CkSocket_SshAuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/* 1313 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshCloseTunnel() {
/* 1317 */     return chilkatJNI.CkSocket_SshCloseTunnel(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SshCloseTunnelAsync() {
/* 1321 */     long var1 = chilkatJNI.CkSocket_SshCloseTunnelAsync(this.swigCPtr, this);
/* 1322 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public CkSocket SshOpenChannel(String var1, int var2, boolean var3, int var4) {
/* 1326 */     long var5 = chilkatJNI.CkSocket_SshOpenChannel(this.swigCPtr, this, var1, var2, var3, var4);
/* 1327 */     return var5 == 0L ? null : new CkSocket(var5, true);
/*      */   }
/*      */   
/*      */   public CkTask SshOpenChannelAsync(String var1, int var2, boolean var3, int var4) {
/* 1331 */     long var5 = chilkatJNI.CkSocket_SshOpenChannelAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1332 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean SshOpenTunnel(String var1, int var2) {
/* 1336 */     return chilkatJNI.CkSocket_SshOpenTunnel(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshOpenTunnelAsync(String var1, int var2) {
/* 1340 */     long var3 = chilkatJNI.CkSocket_SshOpenTunnelAsync(this.swigCPtr, this, var1, var2);
/* 1341 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public void StartTiming() {
/* 1345 */     chilkatJNI.CkSocket_StartTiming(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean TakeSocket(CkSocket var1) {
/* 1349 */     return chilkatJNI.CkSocket_TakeSocket(this.swigCPtr, this, getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean TlsRenegotiate() {
/* 1353 */     return chilkatJNI.CkSocket_TlsRenegotiate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask TlsRenegotiateAsync() {
/* 1357 */     long var1 = chilkatJNI.CkSocket_TlsRenegotiateAsync(this.swigCPtr, this);
/* 1358 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1362 */     return chilkatJNI.CkSocket_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UseSsh(CkSsh var1) {
/* 1366 */     return chilkatJNI.CkSocket_UseSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */