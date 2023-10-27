/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkImap
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkImap(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkImap var0) {
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
/*   29 */         chilkatJNI.delete_CkImap(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkImap()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkImap(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkImap_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkImap_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkImap_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*   54 */     chilkatJNI.CkImap_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkImap_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkImap_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AppendSeen() {
/*   66 */     return chilkatJNI.CkImap_get_AppendSeen(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AppendSeen(boolean var1) {
/*   70 */     chilkatJNI.CkImap_put_AppendSeen(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_AppendUid() {
/*   74 */     return chilkatJNI.CkImap_get_AppendUid(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_AuthMethod(CkString var1) {
/*   78 */     chilkatJNI.CkImap_get_AuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String authMethod() {
/*   82 */     return chilkatJNI.CkImap_authMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AuthMethod(String var1) {
/*   86 */     chilkatJNI.CkImap_put_AuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_AuthzId(CkString var1) {
/*   90 */     chilkatJNI.CkImap_get_AuthzId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String authzId() {
/*   94 */     return chilkatJNI.CkImap_authzId(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AuthzId(String var1) {
/*   98 */     chilkatJNI.CkImap_put_AuthzId(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoDownloadAttachments() {
/*  102 */     return chilkatJNI.CkImap_get_AutoDownloadAttachments(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoDownloadAttachments(boolean var1) {
/*  106 */     chilkatJNI.CkImap_put_AutoDownloadAttachments(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoFix() {
/*  110 */     return chilkatJNI.CkImap_get_AutoFix(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoFix(boolean var1) {
/*  114 */     chilkatJNI.CkImap_put_AutoFix(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  118 */     chilkatJNI.CkImap_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  122 */     return chilkatJNI.CkImap_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  126 */     chilkatJNI.CkImap_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ConnectedToHost(CkString var1) {
/*  130 */     chilkatJNI.CkImap_get_ConnectedToHost(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String connectedToHost() {
/*  134 */     return chilkatJNI.CkImap_connectedToHost(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_ConnectTimeout() {
/*  138 */     return chilkatJNI.CkImap_get_ConnectTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ConnectTimeout(int var1) {
/*  142 */     chilkatJNI.CkImap_put_ConnectTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  146 */     chilkatJNI.CkImap_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  150 */     return chilkatJNI.CkImap_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  154 */     chilkatJNI.CkImap_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Domain(CkString var1) {
/*  158 */     chilkatJNI.CkImap_get_Domain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String domain() {
/*  162 */     return chilkatJNI.CkImap_domain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Domain(String var1) {
/*  166 */     chilkatJNI.CkImap_put_Domain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  170 */     return chilkatJNI.CkImap_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  174 */     chilkatJNI.CkImap_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyAuthMethod(CkString var1) {
/*  178 */     chilkatJNI.CkImap_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyAuthMethod() {
/*  182 */     return chilkatJNI.CkImap_httpProxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyAuthMethod(String var1) {
/*  186 */     chilkatJNI.CkImap_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyDomain(CkString var1) {
/*  190 */     chilkatJNI.CkImap_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyDomain() {
/*  194 */     return chilkatJNI.CkImap_httpProxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyDomain(String var1) {
/*  198 */     chilkatJNI.CkImap_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyHostname(CkString var1) {
/*  202 */     chilkatJNI.CkImap_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyHostname() {
/*  206 */     return chilkatJNI.CkImap_httpProxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyHostname(String var1) {
/*  210 */     chilkatJNI.CkImap_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyPassword(CkString var1) {
/*  214 */     chilkatJNI.CkImap_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyPassword() {
/*  218 */     return chilkatJNI.CkImap_httpProxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPassword(String var1) {
/*  222 */     chilkatJNI.CkImap_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HttpProxyPort() {
/*  226 */     return chilkatJNI.CkImap_get_HttpProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPort(int var1) {
/*  230 */     chilkatJNI.CkImap_put_HttpProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyUsername(CkString var1) {
/*  234 */     chilkatJNI.CkImap_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyUsername() {
/*  238 */     return chilkatJNI.CkImap_httpProxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyUsername(String var1) {
/*  242 */     chilkatJNI.CkImap_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_KeepSessionLog() {
/*  246 */     return chilkatJNI.CkImap_get_KeepSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeepSessionLog(boolean var1) {
/*  250 */     chilkatJNI.CkImap_put_KeepSessionLog(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastAppendedMime(CkString var1) {
/*  254 */     chilkatJNI.CkImap_get_LastAppendedMime(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastAppendedMime() {
/*  258 */     return chilkatJNI.CkImap_lastAppendedMime(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastCommand(CkString var1) {
/*  262 */     chilkatJNI.CkImap_get_LastCommand(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastCommand() {
/*  266 */     return chilkatJNI.CkImap_lastCommand(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  270 */     chilkatJNI.CkImap_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  274 */     return chilkatJNI.CkImap_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  278 */     chilkatJNI.CkImap_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  282 */     return chilkatJNI.CkImap_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  286 */     chilkatJNI.CkImap_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  290 */     return chilkatJNI.CkImap_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastIntermediateResponse(CkString var1) {
/*  294 */     chilkatJNI.CkImap_get_LastIntermediateResponse(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastIntermediateResponse() {
/*  298 */     return chilkatJNI.CkImap_lastIntermediateResponse(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  302 */     return chilkatJNI.CkImap_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  306 */     chilkatJNI.CkImap_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastResponse(CkString var1) {
/*  310 */     chilkatJNI.CkImap_get_LastResponse(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastResponse() {
/*  314 */     return chilkatJNI.CkImap_lastResponse(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastResponseCode(CkString var1) {
/*  318 */     chilkatJNI.CkImap_get_LastResponseCode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastResponseCode() {
/*  322 */     return chilkatJNI.CkImap_lastResponseCode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LoggedInUser(CkString var1) {
/*  326 */     chilkatJNI.CkImap_get_LoggedInUser(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String loggedInUser() {
/*  330 */     return chilkatJNI.CkImap_loggedInUser(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumMessages() {
/*  334 */     return chilkatJNI.CkImap_get_NumMessages(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_PeekMode() {
/*  338 */     return chilkatJNI.CkImap_get_PeekMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PeekMode(boolean var1) {
/*  342 */     chilkatJNI.CkImap_put_PeekMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  346 */     return chilkatJNI.CkImap_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  350 */     chilkatJNI.CkImap_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Port() {
/*  354 */     return chilkatJNI.CkImap_get_Port(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Port(int var1) {
/*  358 */     chilkatJNI.CkImap_put_Port(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  362 */     return chilkatJNI.CkImap_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  366 */     chilkatJNI.CkImap_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReadTimeout() {
/*  370 */     return chilkatJNI.CkImap_get_ReadTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadTimeout(int var1) {
/*  374 */     chilkatJNI.CkImap_put_ReadTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_RequireSslCertVerify() {
/*  378 */     return chilkatJNI.CkImap_get_RequireSslCertVerify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequireSslCertVerify(boolean var1) {
/*  382 */     chilkatJNI.CkImap_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SearchCharset(CkString var1) {
/*  386 */     chilkatJNI.CkImap_get_SearchCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String searchCharset() {
/*  390 */     return chilkatJNI.CkImap_searchCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SearchCharset(String var1) {
/*  394 */     chilkatJNI.CkImap_put_SearchCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SelectedMailbox(CkString var1) {
/*  398 */     chilkatJNI.CkImap_get_SelectedMailbox(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String selectedMailbox() {
/*  402 */     return chilkatJNI.CkImap_selectedMailbox(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_SendBufferSize() {
/*  406 */     return chilkatJNI.CkImap_get_SendBufferSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendBufferSize(int var1) {
/*  410 */     chilkatJNI.CkImap_put_SendBufferSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SeparatorChar(CkString var1) {
/*  414 */     chilkatJNI.CkImap_get_SeparatorChar(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String separatorChar() {
/*  418 */     return chilkatJNI.CkImap_separatorChar(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SeparatorChar(String var1) {
/*  422 */     chilkatJNI.CkImap_put_SeparatorChar(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SessionLog(CkString var1) {
/*  426 */     chilkatJNI.CkImap_get_SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sessionLog() {
/*  430 */     return chilkatJNI.CkImap_sessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  434 */     chilkatJNI.CkImap_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  438 */     return chilkatJNI.CkImap_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  442 */     chilkatJNI.CkImap_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  446 */     chilkatJNI.CkImap_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  450 */     return chilkatJNI.CkImap_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  454 */     chilkatJNI.CkImap_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  458 */     return chilkatJNI.CkImap_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  462 */     chilkatJNI.CkImap_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  466 */     chilkatJNI.CkImap_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  470 */     return chilkatJNI.CkImap_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  474 */     chilkatJNI.CkImap_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  478 */     return chilkatJNI.CkImap_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  482 */     chilkatJNI.CkImap_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  486 */     return chilkatJNI.CkImap_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  490 */     chilkatJNI.CkImap_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  494 */     return chilkatJNI.CkImap_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  498 */     chilkatJNI.CkImap_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_Ssl() {
/*  502 */     return chilkatJNI.CkImap_get_Ssl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Ssl(boolean var1) {
/*  506 */     chilkatJNI.CkImap_put_Ssl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslAllowedCiphers(CkString var1) {
/*  510 */     chilkatJNI.CkImap_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslAllowedCiphers() {
/*  514 */     return chilkatJNI.CkImap_sslAllowedCiphers(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslAllowedCiphers(String var1) {
/*  518 */     chilkatJNI.CkImap_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslProtocol(CkString var1) {
/*  522 */     chilkatJNI.CkImap_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslProtocol() {
/*  526 */     return chilkatJNI.CkImap_sslProtocol(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslProtocol(String var1) {
/*  530 */     chilkatJNI.CkImap_put_SslProtocol(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SslServerCertVerified() {
/*  534 */     return chilkatJNI.CkImap_get_SslServerCertVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_StartTls() {
/*  538 */     return chilkatJNI.CkImap_get_StartTls(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_StartTls(boolean var1) {
/*  542 */     chilkatJNI.CkImap_put_StartTls(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsCipherSuite(CkString var1) {
/*  546 */     chilkatJNI.CkImap_get_TlsCipherSuite(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsCipherSuite() {
/*  550 */     return chilkatJNI.CkImap_tlsCipherSuite(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsPinSet(CkString var1) {
/*  554 */     chilkatJNI.CkImap_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsPinSet() {
/*  558 */     return chilkatJNI.CkImap_tlsPinSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TlsPinSet(String var1) {
/*  562 */     chilkatJNI.CkImap_put_TlsPinSet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsVersion(CkString var1) {
/*  566 */     chilkatJNI.CkImap_get_TlsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsVersion() {
/*  570 */     return chilkatJNI.CkImap_tlsVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_UidNext() {
/*  574 */     return chilkatJNI.CkImap_get_UidNext(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_UidValidity() {
/*  578 */     return chilkatJNI.CkImap_get_UidValidity(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  582 */     return chilkatJNI.CkImap_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  586 */     chilkatJNI.CkImap_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  590 */     chilkatJNI.CkImap_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  594 */     return chilkatJNI.CkImap_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceData(CkByteData var1, String var2) {
/*  598 */     return chilkatJNI.CkImap_AddPfxSourceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceFile(String var1, String var2) {
/*  602 */     return chilkatJNI.CkImap_AddPfxSourceFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AppendMail(String var1, CkEmail var2) {
/*  606 */     return chilkatJNI.CkImap_AppendMail(this.swigCPtr, this, var1, CkEmail.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask AppendMailAsync(String var1, CkEmail var2) {
/*  610 */     long var3 = chilkatJNI.CkImap_AppendMailAsync(this.swigCPtr, this, var1, CkEmail.getCPtr(var2), var2);
/*  611 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AppendMime(String var1, String var2) {
/*  615 */     return chilkatJNI.CkImap_AppendMime(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask AppendMimeAsync(String var1, String var2) {
/*  619 */     long var3 = chilkatJNI.CkImap_AppendMimeAsync(this.swigCPtr, this, var1, var2);
/*  620 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean AppendMimeWithDate(String var1, String var2, SYSTEMTIME var3) {
/*  624 */     return chilkatJNI.CkImap_AppendMimeWithDate(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean AppendMimeWithDateStr(String var1, String var2, String var3) {
/*  628 */     return chilkatJNI.CkImap_AppendMimeWithDateStr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask AppendMimeWithDateStrAsync(String var1, String var2, String var3) {
/*  632 */     long var4 = chilkatJNI.CkImap_AppendMimeWithDateStrAsync(this.swigCPtr, this, var1, var2, var3);
/*  633 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean AppendMimeWithFlags(String var1, String var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/*  637 */     return chilkatJNI.CkImap_AppendMimeWithFlags(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*      */   }
/*      */   
/*      */   public CkTask AppendMimeWithFlagsAsync(String var1, String var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/*  641 */     long var7 = chilkatJNI.CkImap_AppendMimeWithFlagsAsync(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*  642 */     return var7 == 0L ? null : new CkTask(var7, true);
/*      */   }
/*      */   
/*      */   public boolean AppendMimeWithFlagsSb(String var1, CkStringBuilder var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/*  646 */     return chilkatJNI.CkImap_AppendMimeWithFlagsSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3, var4, var5, var6);
/*      */   }
/*      */   
/*      */   public CkTask AppendMimeWithFlagsSbAsync(String var1, CkStringBuilder var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/*  650 */     long var7 = chilkatJNI.CkImap_AppendMimeWithFlagsSbAsync(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3, var4, var5, var6);
/*  651 */     return var7 == 0L ? null : new CkTask(var7, true);
/*      */   }
/*      */   
/*      */   public boolean Capability(CkString var1) {
/*  655 */     return chilkatJNI.CkImap_Capability(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String capability() {
/*  659 */     return chilkatJNI.CkImap_capability(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask CapabilityAsync() {
/*  663 */     long var1 = chilkatJNI.CkImap_CapabilityAsync(this.swigCPtr, this);
/*  664 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean CheckConnection() {
/*  668 */     return chilkatJNI.CkImap_CheckConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkMessageSet CheckForNewEmail() {
/*  672 */     long var1 = chilkatJNI.CkImap_CheckForNewEmail(this.swigCPtr, this);
/*  673 */     return var1 == 0L ? null : new CkMessageSet(var1, true);
/*      */   }
/*      */   
/*      */   public CkTask CheckForNewEmailAsync() {
/*  677 */     long var1 = chilkatJNI.CkImap_CheckForNewEmailAsync(this.swigCPtr, this);
/*  678 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public void ClearSessionLog() {
/*  682 */     chilkatJNI.CkImap_ClearSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean CloseMailbox(String var1) {
/*  686 */     return chilkatJNI.CkImap_CloseMailbox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CloseMailboxAsync(String var1) {
/*  690 */     long var2 = chilkatJNI.CkImap_CloseMailboxAsync(this.swigCPtr, this, var1);
/*  691 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Connect(String var1) {
/*  695 */     return chilkatJNI.CkImap_Connect(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ConnectAsync(String var1) {
/*  699 */     long var2 = chilkatJNI.CkImap_ConnectAsync(this.swigCPtr, this, var1);
/*  700 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Copy(int var1, boolean var2, String var3) {
/*  704 */     return chilkatJNI.CkImap_Copy(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask CopyAsync(int var1, boolean var2, String var3) {
/*  708 */     long var4 = chilkatJNI.CkImap_CopyAsync(this.swigCPtr, this, var1, var2, var3);
/*  709 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean CopyMultiple(CkMessageSet var1, String var2) {
/*  713 */     return chilkatJNI.CkImap_CopyMultiple(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask CopyMultipleAsync(CkMessageSet var1, String var2) {
/*  717 */     long var3 = chilkatJNI.CkImap_CopyMultipleAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2);
/*  718 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean CopySequence(int var1, int var2, String var3) {
/*  722 */     return chilkatJNI.CkImap_CopySequence(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask CopySequenceAsync(int var1, int var2, String var3) {
/*  726 */     long var4 = chilkatJNI.CkImap_CopySequenceAsync(this.swigCPtr, this, var1, var2, var3);
/*  727 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean CreateMailbox(String var1) {
/*  731 */     return chilkatJNI.CkImap_CreateMailbox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask CreateMailboxAsync(String var1) {
/*  735 */     long var2 = chilkatJNI.CkImap_CreateMailboxAsync(this.swigCPtr, this, var1);
/*  736 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteMailbox(String var1) {
/*  740 */     return chilkatJNI.CkImap_DeleteMailbox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteMailboxAsync(String var1) {
/*  744 */     long var2 = chilkatJNI.CkImap_DeleteMailboxAsync(this.swigCPtr, this, var1);
/*  745 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Disconnect() {
/*  749 */     return chilkatJNI.CkImap_Disconnect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask DisconnectAsync() {
/*  753 */     long var1 = chilkatJNI.CkImap_DisconnectAsync(this.swigCPtr, this);
/*  754 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ExamineMailbox(String var1) {
/*  758 */     return chilkatJNI.CkImap_ExamineMailbox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask ExamineMailboxAsync(String var1) {
/*  762 */     long var2 = chilkatJNI.CkImap_ExamineMailboxAsync(this.swigCPtr, this, var1);
/*  763 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean Expunge() {
/*  767 */     return chilkatJNI.CkImap_Expunge(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ExpungeAsync() {
/*  771 */     long var1 = chilkatJNI.CkImap_ExpungeAsync(this.swigCPtr, this);
/*  772 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ExpungeAndClose() {
/*  776 */     return chilkatJNI.CkImap_ExpungeAndClose(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask ExpungeAndCloseAsync() {
/*  780 */     long var1 = chilkatJNI.CkImap_ExpungeAndCloseAsync(this.swigCPtr, this);
/*  781 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean FetchAttachment(CkEmail var1, int var2, String var3) {
/*  785 */     return chilkatJNI.CkImap_FetchAttachment(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask FetchAttachmentAsync(CkEmail var1, int var2, String var3) {
/*  789 */     long var4 = chilkatJNI.CkImap_FetchAttachmentAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/*  790 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean FetchAttachmentBd(CkEmail var1, int var2, CkBinData var3) {
/*  794 */     return chilkatJNI.CkImap_FetchAttachmentBd(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, CkBinData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask FetchAttachmentBdAsync(CkEmail var1, int var2, CkBinData var3) {
/*  798 */     long var4 = chilkatJNI.CkImap_FetchAttachmentBdAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, CkBinData.getCPtr(var3), var3);
/*  799 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean FetchAttachmentBytes(CkEmail var1, int var2, CkByteData var3) {
/*  803 */     return chilkatJNI.CkImap_FetchAttachmentBytes(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask FetchAttachmentBytesAsync(CkEmail var1, int var2) {
/*  807 */     long var3 = chilkatJNI.CkImap_FetchAttachmentBytesAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*  808 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean FetchAttachmentSb(CkEmail var1, int var2, String var3, CkStringBuilder var4) {
/*  812 */     return chilkatJNI.CkImap_FetchAttachmentSb(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3, CkStringBuilder.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public CkTask FetchAttachmentSbAsync(CkEmail var1, int var2, String var3, CkStringBuilder var4) {
/*  816 */     long var5 = chilkatJNI.CkImap_FetchAttachmentSbAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3, CkStringBuilder.getCPtr(var4), var4);
/*  817 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean FetchAttachmentString(CkEmail var1, int var2, String var3, CkString var4) {
/*  821 */     return chilkatJNI.CkImap_FetchAttachmentString(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String fetchAttachmentString(CkEmail var1, int var2, String var3) {
/*  825 */     return chilkatJNI.CkImap_fetchAttachmentString(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask FetchAttachmentStringAsync(CkEmail var1, int var2, String var3) {
/*  829 */     long var4 = chilkatJNI.CkImap_FetchAttachmentStringAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/*  830 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchBundle(CkMessageSet var1) {
/*  834 */     long var2 = chilkatJNI.CkImap_FetchBundle(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  835 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchBundleAsync(CkMessageSet var1) {
/*  839 */     long var2 = chilkatJNI.CkImap_FetchBundleAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  840 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkStringArray FetchBundleAsMime(CkMessageSet var1) {
/*  844 */     long var2 = chilkatJNI.CkImap_FetchBundleAsMime(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  845 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchBundleAsMimeAsync(CkMessageSet var1) {
/*  849 */     long var2 = chilkatJNI.CkImap_FetchBundleAsMimeAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  850 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchChunk(int var1, int var2, CkMessageSet var3, CkMessageSet var4) {
/*  854 */     long var5 = chilkatJNI.CkImap_FetchChunk(this.swigCPtr, this, var1, var2, CkMessageSet.getCPtr(var3), var3, CkMessageSet.getCPtr(var4), var4);
/*  855 */     return var5 == 0L ? null : new CkEmailBundle(var5, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchChunkAsync(int var1, int var2, CkMessageSet var3, CkMessageSet var4) {
/*  859 */     long var5 = chilkatJNI.CkImap_FetchChunkAsync(this.swigCPtr, this, var1, var2, CkMessageSet.getCPtr(var3), var3, CkMessageSet.getCPtr(var4), var4);
/*  860 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean FetchFlags(int var1, boolean var2, CkString var3) {
/*  864 */     return chilkatJNI.CkImap_FetchFlags(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String fetchFlags(int var1, boolean var2) {
/*  868 */     return chilkatJNI.CkImap_fetchFlags(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchFlagsAsync(int var1, boolean var2) {
/*  872 */     long var3 = chilkatJNI.CkImap_FetchFlagsAsync(this.swigCPtr, this, var1, var2);
/*  873 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchHeaders(CkMessageSet var1) {
/*  877 */     long var2 = chilkatJNI.CkImap_FetchHeaders(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  878 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchHeadersAsync(CkMessageSet var1) {
/*  882 */     long var2 = chilkatJNI.CkImap_FetchHeadersAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1);
/*  883 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchSequence(int var1, int var2) {
/*  887 */     long var3 = chilkatJNI.CkImap_FetchSequence(this.swigCPtr, this, var1, var2);
/*  888 */     return var3 == 0L ? null : new CkEmailBundle(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSequenceAsync(int var1, int var2) {
/*  892 */     long var3 = chilkatJNI.CkImap_FetchSequenceAsync(this.swigCPtr, this, var1, var2);
/*  893 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkStringArray FetchSequenceAsMime(int var1, int var2) {
/*  897 */     long var3 = chilkatJNI.CkImap_FetchSequenceAsMime(this.swigCPtr, this, var1, var2);
/*  898 */     return var3 == 0L ? null : new CkStringArray(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSequenceAsMimeAsync(int var1, int var2) {
/*  902 */     long var3 = chilkatJNI.CkImap_FetchSequenceAsMimeAsync(this.swigCPtr, this, var1, var2);
/*  903 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchSequenceHeaders(int var1, int var2) {
/*  907 */     long var3 = chilkatJNI.CkImap_FetchSequenceHeaders(this.swigCPtr, this, var1, var2);
/*  908 */     return var3 == 0L ? null : new CkEmailBundle(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSequenceHeadersAsync(int var1, int var2) {
/*  912 */     long var3 = chilkatJNI.CkImap_FetchSequenceHeadersAsync(this.swigCPtr, this, var1, var2);
/*  913 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchSingle(int var1, boolean var2) {
/*  917 */     long var3 = chilkatJNI.CkImap_FetchSingle(this.swigCPtr, this, var1, var2);
/*  918 */     return var3 == 0L ? null : new CkEmail(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleAsync(int var1, boolean var2) {
/*  922 */     long var3 = chilkatJNI.CkImap_FetchSingleAsync(this.swigCPtr, this, var1, var2);
/*  923 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean FetchSingleAsMime(int var1, boolean var2, CkString var3) {
/*  927 */     return chilkatJNI.CkImap_FetchSingleAsMime(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String fetchSingleAsMime(int var1, boolean var2) {
/*  931 */     return chilkatJNI.CkImap_fetchSingleAsMime(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleAsMimeAsync(int var1, boolean var2) {
/*  935 */     long var3 = chilkatJNI.CkImap_FetchSingleAsMimeAsync(this.swigCPtr, this, var1, var2);
/*  936 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean FetchSingleAsMimeSb(int var1, boolean var2, CkStringBuilder var3) {
/*  940 */     return chilkatJNI.CkImap_FetchSingleAsMimeSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleAsMimeSbAsync(int var1, boolean var2, CkStringBuilder var3) {
/*  944 */     long var4 = chilkatJNI.CkImap_FetchSingleAsMimeSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*  945 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchSingleHeader(int var1, boolean var2) {
/*  949 */     long var3 = chilkatJNI.CkImap_FetchSingleHeader(this.swigCPtr, this, var1, var2);
/*  950 */     return var3 == 0L ? null : new CkEmail(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleHeaderAsync(int var1, boolean var2) {
/*  954 */     long var3 = chilkatJNI.CkImap_FetchSingleHeaderAsync(this.swigCPtr, this, var1, var2);
/*  955 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean FetchSingleHeaderAsMime(int var1, boolean var2, CkString var3) {
/*  959 */     return chilkatJNI.CkImap_FetchSingleHeaderAsMime(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String fetchSingleHeaderAsMime(int var1, boolean var2) {
/*  963 */     return chilkatJNI.CkImap_fetchSingleHeaderAsMime(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleHeaderAsMimeAsync(int var1, boolean var2) {
/*  967 */     long var3 = chilkatJNI.CkImap_FetchSingleHeaderAsMimeAsync(this.swigCPtr, this, var1, var2);
/*  968 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkMessageSet GetAllUids() {
/*  972 */     long var1 = chilkatJNI.CkImap_GetAllUids(this.swigCPtr, this);
/*  973 */     return var1 == 0L ? null : new CkMessageSet(var1, true);
/*      */   }
/*      */   
/*      */   public CkTask GetAllUidsAsync() {
/*  977 */     long var1 = chilkatJNI.CkImap_GetAllUidsAsync(this.swigCPtr, this);
/*  978 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetMailAttachFilename(CkEmail var1, int var2, CkString var3) {
/*  982 */     return chilkatJNI.CkImap_GetMailAttachFilename(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getMailAttachFilename(CkEmail var1, int var2) {
/*  986 */     return chilkatJNI.CkImap_getMailAttachFilename(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public String mailAttachFilename(CkEmail var1, int var2) {
/*  990 */     return chilkatJNI.CkImap_mailAttachFilename(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public int GetMailAttachSize(CkEmail var1, int var2) {
/*  994 */     return chilkatJNI.CkImap_GetMailAttachSize(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GetMailboxStatus(String var1, CkString var2) {
/*  998 */     return chilkatJNI.CkImap_GetMailboxStatus(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getMailboxStatus(String var1) {
/* 1002 */     return chilkatJNI.CkImap_getMailboxStatus(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String mailboxStatus(String var1) {
/* 1006 */     return chilkatJNI.CkImap_mailboxStatus(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetMailboxStatusAsync(String var1) {
/* 1010 */     long var2 = chilkatJNI.CkImap_GetMailboxStatusAsync(this.swigCPtr, this, var1);
/* 1011 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public int GetMailFlag(CkEmail var1, String var2) {
/* 1015 */     return chilkatJNI.CkImap_GetMailFlag(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public int GetMailNumAttach(CkEmail var1) {
/* 1019 */     return chilkatJNI.CkImap_GetMailNumAttach(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public int GetMailSize(CkEmail var1) {
/* 1023 */     return chilkatJNI.CkImap_GetMailSize(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean GetQuota(String var1, CkString var2) {
/* 1027 */     return chilkatJNI.CkImap_GetQuota(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getQuota(String var1) {
/* 1031 */     return chilkatJNI.CkImap_getQuota(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String quota(String var1) {
/* 1035 */     return chilkatJNI.CkImap_quota(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetQuotaAsync(String var1) {
/* 1039 */     long var2 = chilkatJNI.CkImap_GetQuotaAsync(this.swigCPtr, this, var1);
/* 1040 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetQuotaRoot(String var1, CkString var2) {
/* 1044 */     return chilkatJNI.CkImap_GetQuotaRoot(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getQuotaRoot(String var1) {
/* 1048 */     return chilkatJNI.CkImap_getQuotaRoot(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String quotaRoot(String var1) {
/* 1052 */     return chilkatJNI.CkImap_quotaRoot(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetQuotaRootAsync(String var1) {
/* 1056 */     long var2 = chilkatJNI.CkImap_GetQuotaRootAsync(this.swigCPtr, this, var1);
/* 1057 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkCert GetSslServerCert() {
/* 1061 */     long var1 = chilkatJNI.CkImap_GetSslServerCert(this.swigCPtr, this);
/* 1062 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean HasCapability(String var1, String var2) {
/* 1066 */     return chilkatJNI.CkImap_HasCapability(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean IdleCheck(int var1, CkString var2) {
/* 1070 */     return chilkatJNI.CkImap_IdleCheck(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String idleCheck(int var1) {
/* 1074 */     return chilkatJNI.CkImap_idleCheck(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask IdleCheckAsync(int var1) {
/* 1078 */     long var2 = chilkatJNI.CkImap_IdleCheckAsync(this.swigCPtr, this, var1);
/* 1079 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean IdleDone() {
/* 1083 */     return chilkatJNI.CkImap_IdleDone(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask IdleDoneAsync() {
/* 1087 */     long var1 = chilkatJNI.CkImap_IdleDoneAsync(this.swigCPtr, this);
/* 1088 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean IdleStart() {
/* 1092 */     return chilkatJNI.CkImap_IdleStart(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask IdleStartAsync() {
/* 1096 */     long var1 = chilkatJNI.CkImap_IdleStartAsync(this.swigCPtr, this);
/* 1097 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean IsConnected() {
/* 1101 */     return chilkatJNI.CkImap_IsConnected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean IsLoggedIn() {
/* 1105 */     return chilkatJNI.CkImap_IsLoggedIn(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/* 1109 */     return chilkatJNI.CkImap_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkMailboxes ListMailboxes(String var1, String var2) {
/* 1113 */     long var3 = chilkatJNI.CkImap_ListMailboxes(this.swigCPtr, this, var1, var2);
/* 1114 */     return var3 == 0L ? null : new CkMailboxes(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask ListMailboxesAsync(String var1, String var2) {
/* 1118 */     long var3 = chilkatJNI.CkImap_ListMailboxesAsync(this.swigCPtr, this, var1, var2);
/* 1119 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkMailboxes ListSubscribed(String var1, String var2) {
/* 1123 */     long var3 = chilkatJNI.CkImap_ListSubscribed(this.swigCPtr, this, var1, var2);
/* 1124 */     return var3 == 0L ? null : new CkMailboxes(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask ListSubscribedAsync(String var1, String var2) {
/* 1128 */     long var3 = chilkatJNI.CkImap_ListSubscribedAsync(this.swigCPtr, this, var1, var2);
/* 1129 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean Login(String var1, String var2) {
/* 1133 */     return chilkatJNI.CkImap_Login(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask LoginAsync(String var1, String var2) {
/* 1137 */     long var3 = chilkatJNI.CkImap_LoginAsync(this.swigCPtr, this, var1, var2);
/* 1138 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean LoginSecure(CkSecureString var1, CkSecureString var2) {
/* 1142 */     return chilkatJNI.CkImap_LoginSecure(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask LoginSecureAsync(CkSecureString var1, CkSecureString var2) {
/* 1146 */     long var3 = chilkatJNI.CkImap_LoginSecureAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/* 1147 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean Logout() {
/* 1151 */     return chilkatJNI.CkImap_Logout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask LogoutAsync() {
/* 1155 */     long var1 = chilkatJNI.CkImap_LogoutAsync(this.swigCPtr, this);
/* 1156 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean MoveMessages(CkMessageSet var1, String var2) {
/* 1160 */     return chilkatJNI.CkImap_MoveMessages(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask MoveMessagesAsync(CkMessageSet var1, String var2) {
/* 1164 */     long var3 = chilkatJNI.CkImap_MoveMessagesAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2);
/* 1165 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean Noop() {
/* 1169 */     return chilkatJNI.CkImap_Noop(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask NoopAsync() {
/* 1173 */     long var1 = chilkatJNI.CkImap_NoopAsync(this.swigCPtr, this);
/* 1174 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean RefetchMailFlags(CkEmail var1) {
/* 1178 */     return chilkatJNI.CkImap_RefetchMailFlags(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask RefetchMailFlagsAsync(CkEmail var1) {
/* 1182 */     long var2 = chilkatJNI.CkImap_RefetchMailFlagsAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/* 1183 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean RenameMailbox(String var1, String var2) {
/* 1187 */     return chilkatJNI.CkImap_RenameMailbox(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask RenameMailboxAsync(String var1, String var2) {
/* 1191 */     long var3 = chilkatJNI.CkImap_RenameMailboxAsync(this.swigCPtr, this, var1, var2);
/* 1192 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1196 */     return chilkatJNI.CkImap_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkMessageSet Search(String var1, boolean var2) {
/* 1200 */     long var3 = chilkatJNI.CkImap_Search(this.swigCPtr, this, var1, var2);
/* 1201 */     return var3 == 0L ? null : new CkMessageSet(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask SearchAsync(String var1, boolean var2) {
/* 1205 */     long var3 = chilkatJNI.CkImap_SearchAsync(this.swigCPtr, this, var1, var2);
/* 1206 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SelectMailbox(String var1) {
/* 1210 */     return chilkatJNI.CkImap_SelectMailbox(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SelectMailboxAsync(String var1) {
/* 1214 */     long var2 = chilkatJNI.CkImap_SelectMailboxAsync(this.swigCPtr, this, var1);
/* 1215 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendRawCommand(String var1, CkString var2) {
/* 1219 */     return chilkatJNI.CkImap_SendRawCommand(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String sendRawCommand(String var1) {
/* 1223 */     return chilkatJNI.CkImap_sendRawCommand(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SendRawCommandAsync(String var1) {
/* 1227 */     long var2 = chilkatJNI.CkImap_SendRawCommandAsync(this.swigCPtr, this, var1);
/* 1228 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendRawCommandB(String var1, CkByteData var2) {
/* 1232 */     return chilkatJNI.CkImap_SendRawCommandB(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SendRawCommandBAsync(String var1) {
/* 1236 */     long var2 = chilkatJNI.CkImap_SendRawCommandBAsync(this.swigCPtr, this, var1);
/* 1237 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendRawCommandC(CkByteData var1, CkByteData var2) {
/* 1241 */     return chilkatJNI.CkImap_SendRawCommandC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SendRawCommandCAsync(CkByteData var1) {
/* 1245 */     long var2 = chilkatJNI.CkImap_SendRawCommandCAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 1246 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SetCSP(CkCsp var1) {
/* 1250 */     return chilkatJNI.CkImap_SetCSP(this.swigCPtr, this, CkCsp.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert(CkCert var1) {
/* 1254 */     return chilkatJNI.CkImap_SetDecryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert2(CkCert var1, CkPrivateKey var2) {
/* 1258 */     return chilkatJNI.CkImap_SetDecryptCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetFlag(int var1, boolean var2, String var3, int var4) {
/* 1262 */     return chilkatJNI.CkImap_SetFlag(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask SetFlagAsync(int var1, boolean var2, String var3, int var4) {
/* 1266 */     long var5 = chilkatJNI.CkImap_SetFlagAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1267 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean SetFlags(CkMessageSet var1, String var2, int var3) {
/* 1271 */     return chilkatJNI.CkImap_SetFlags(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetFlagsAsync(CkMessageSet var1, String var2, int var3) {
/* 1275 */     long var4 = chilkatJNI.CkImap_SetFlagsAsync(this.swigCPtr, this, CkMessageSet.getCPtr(var1), var1, var2, var3);
/* 1276 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetMailFlag(CkEmail var1, String var2, int var3) {
/* 1280 */     return chilkatJNI.CkImap_SetMailFlag(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetMailFlagAsync(CkEmail var1, String var2, int var3) {
/* 1284 */     long var4 = chilkatJNI.CkImap_SetMailFlagAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2, var3);
/* 1285 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetQuota(String var1, String var2, int var3) {
/* 1289 */     return chilkatJNI.CkImap_SetQuota(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SetQuotaAsync(String var1, String var2, int var3) {
/* 1293 */     long var4 = chilkatJNI.CkImap_SetQuotaAsync(this.swigCPtr, this, var1, var2, var3);
/* 1294 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCert(CkCert var1) {
/* 1298 */     return chilkatJNI.CkImap_SetSslClientCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPem(String var1, String var2) {
/* 1302 */     return chilkatJNI.CkImap_SetSslClientCertPem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPfx(String var1, String var2) {
/* 1306 */     return chilkatJNI.CkImap_SetSslClientCertPfx(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePk(String var1, CkSshKey var2) {
/* 1310 */     return chilkatJNI.CkImap_SshAuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePkAsync(String var1, CkSshKey var2) {
/* 1314 */     long var3 = chilkatJNI.CkImap_SshAuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/* 1315 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePw(String var1, String var2) {
/* 1319 */     return chilkatJNI.CkImap_SshAuthenticatePw(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePwAsync(String var1, String var2) {
/* 1323 */     long var3 = chilkatJNI.CkImap_SshAuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/* 1324 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshCloseTunnel() {
/* 1328 */     return chilkatJNI.CkImap_SshCloseTunnel(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SshCloseTunnelAsync() {
/* 1332 */     long var1 = chilkatJNI.CkImap_SshCloseTunnelAsync(this.swigCPtr, this);
/* 1333 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SshOpenTunnel(String var1, int var2) {
/* 1337 */     return chilkatJNI.CkImap_SshOpenTunnel(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshOpenTunnelAsync(String var1, int var2) {
/* 1341 */     long var3 = chilkatJNI.CkImap_SshOpenTunnelAsync(this.swigCPtr, this, var1, var2);
/* 1342 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean StoreFlags(int var1, boolean var2, String var3, int var4) {
/* 1346 */     return chilkatJNI.CkImap_StoreFlags(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public CkTask StoreFlagsAsync(int var1, boolean var2, String var3, int var4) {
/* 1350 */     long var5 = chilkatJNI.CkImap_StoreFlagsAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 1351 */     return var5 == 0L ? null : new CkTask(var5, true);
/*      */   }
/*      */   
/*      */   public boolean Subscribe(String var1) {
/* 1355 */     return chilkatJNI.CkImap_Subscribe(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask SubscribeAsync(String var1) {
/* 1359 */     long var2 = chilkatJNI.CkImap_SubscribeAsync(this.swigCPtr, this, var1);
/* 1360 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1364 */     return chilkatJNI.CkImap_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean Unsubscribe(String var1) {
/* 1368 */     return chilkatJNI.CkImap_Unsubscribe(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask UnsubscribeAsync(String var1) {
/* 1372 */     long var2 = chilkatJNI.CkImap_UnsubscribeAsync(this.swigCPtr, this, var1);
/* 1373 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 1377 */     return chilkatJNI.CkImap_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean UseSsh(CkSsh var1) {
/* 1381 */     return chilkatJNI.CkImap_UseSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean UseSshTunnel(CkSocket var1) {
/* 1385 */     return chilkatJNI.CkImap_UseSshTunnel(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkImap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */