/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSsh
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSsh(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSsh var0) {
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
/*  29 */         chilkatJNI.delete_CkSsh(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSsh()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSsh(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkSsh_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkSsh_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkSsh_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkSsh_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkSsh_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkSsh_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_AuthFailReason() {
/*  66 */     return chilkatJNI.CkSsh_get_AuthFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_CaretControl() {
/*  70 */     return chilkatJNI.CkSsh_get_CaretControl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CaretControl(boolean var1) {
/*  74 */     chilkatJNI.CkSsh_put_CaretControl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ChannelOpenFailCode() {
/*  78 */     return chilkatJNI.CkSsh_get_ChannelOpenFailCode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ChannelOpenFailReason(CkString var1) {
/*  82 */     chilkatJNI.CkSsh_get_ChannelOpenFailReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String channelOpenFailReason() {
/*  86 */     return chilkatJNI.CkSsh_channelOpenFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ClientIdentifier(CkString var1) {
/*  90 */     chilkatJNI.CkSsh_get_ClientIdentifier(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientIdentifier() {
/*  94 */     return chilkatJNI.CkSsh_clientIdentifier(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientIdentifier(String var1) {
/*  98 */     chilkatJNI.CkSsh_put_ClientIdentifier(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientIpAddress(CkString var1) {
/* 102 */     chilkatJNI.CkSsh_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientIpAddress() {
/* 106 */     return chilkatJNI.CkSsh_clientIpAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientIpAddress(String var1) {
/* 110 */     chilkatJNI.CkSsh_put_ClientIpAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ClientPort() {
/* 114 */     return chilkatJNI.CkSsh_get_ClientPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientPort(int var1) {
/* 118 */     chilkatJNI.CkSsh_put_ClientPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ConnectTimeoutMs() {
/* 122 */     return chilkatJNI.CkSsh_get_ConnectTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConnectTimeoutMs(int var1) {
/* 126 */     chilkatJNI.CkSsh_put_ConnectTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 130 */     chilkatJNI.CkSsh_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 134 */     return chilkatJNI.CkSsh_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 138 */     chilkatJNI.CkSsh_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DisconnectCode() {
/* 142 */     return chilkatJNI.CkSsh_get_DisconnectCode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DisconnectReason(CkString var1) {
/* 146 */     chilkatJNI.CkSsh_get_DisconnectReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String disconnectReason() {
/* 150 */     return chilkatJNI.CkSsh_disconnectReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_EnableCompression() {
/* 154 */     return chilkatJNI.CkSsh_get_EnableCompression(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EnableCompression(boolean var1) {
/* 158 */     chilkatJNI.CkSsh_put_EnableCompression(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ForceCipher(CkString var1) {
/* 162 */     chilkatJNI.CkSsh_get_ForceCipher(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String forceCipher() {
/* 166 */     return chilkatJNI.CkSsh_forceCipher(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ForceCipher(String var1) {
/* 170 */     chilkatJNI.CkSsh_put_ForceCipher(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 174 */     return chilkatJNI.CkSsh_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 178 */     chilkatJNI.CkSsh_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HostKeyAlg(CkString var1) {
/* 182 */     chilkatJNI.CkSsh_get_HostKeyAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hostKeyAlg() {
/* 186 */     return chilkatJNI.CkSsh_hostKeyAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HostKeyAlg(String var1) {
/* 190 */     chilkatJNI.CkSsh_put_HostKeyAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HostKeyFingerprint(CkString var1) {
/* 194 */     chilkatJNI.CkSsh_get_HostKeyFingerprint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hostKeyFingerprint() {
/* 198 */     return chilkatJNI.CkSsh_hostKeyFingerprint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyAuthMethod(CkString var1) {
/* 202 */     chilkatJNI.CkSsh_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyAuthMethod() {
/* 206 */     return chilkatJNI.CkSsh_httpProxyAuthMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyAuthMethod(String var1) {
/* 210 */     chilkatJNI.CkSsh_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyDomain(CkString var1) {
/* 214 */     chilkatJNI.CkSsh_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyDomain() {
/* 218 */     return chilkatJNI.CkSsh_httpProxyDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyDomain(String var1) {
/* 222 */     chilkatJNI.CkSsh_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyHostname(CkString var1) {
/* 226 */     chilkatJNI.CkSsh_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyHostname() {
/* 230 */     return chilkatJNI.CkSsh_httpProxyHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyHostname(String var1) {
/* 234 */     chilkatJNI.CkSsh_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyPassword(CkString var1) {
/* 238 */     chilkatJNI.CkSsh_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyPassword() {
/* 242 */     return chilkatJNI.CkSsh_httpProxyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyPassword(String var1) {
/* 246 */     chilkatJNI.CkSsh_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HttpProxyPort() {
/* 250 */     return chilkatJNI.CkSsh_get_HttpProxyPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyPort(int var1) {
/* 254 */     chilkatJNI.CkSsh_put_HttpProxyPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyUsername(CkString var1) {
/* 258 */     chilkatJNI.CkSsh_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyUsername() {
/* 262 */     return chilkatJNI.CkSsh_httpProxyUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyUsername(String var1) {
/* 266 */     chilkatJNI.CkSsh_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_IdleTimeoutMs() {
/* 270 */     return chilkatJNI.CkSsh_get_IdleTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IdleTimeoutMs(int var1) {
/* 274 */     chilkatJNI.CkSsh_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsConnected() {
/* 278 */     return chilkatJNI.CkSsh_get_IsConnected(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_KeepSessionLog() {
/* 282 */     return chilkatJNI.CkSsh_get_KeepSessionLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeepSessionLog(boolean var1) {
/* 286 */     chilkatJNI.CkSsh_put_KeepSessionLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 290 */     chilkatJNI.CkSsh_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 294 */     return chilkatJNI.CkSsh_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 298 */     chilkatJNI.CkSsh_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 302 */     return chilkatJNI.CkSsh_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 306 */     chilkatJNI.CkSsh_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 310 */     return chilkatJNI.CkSsh_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 314 */     return chilkatJNI.CkSsh_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 318 */     chilkatJNI.CkSsh_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_MaxPacketSize() {
/* 322 */     return chilkatJNI.CkSsh_get_MaxPacketSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MaxPacketSize(int var1) {
/* 326 */     chilkatJNI.CkSsh_put_MaxPacketSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumOpenChannels() {
/* 330 */     return chilkatJNI.CkSsh_get_NumOpenChannels(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PasswordChangeRequested() {
/* 334 */     return chilkatJNI.CkSsh_get_PasswordChangeRequested(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 338 */     return chilkatJNI.CkSsh_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 342 */     chilkatJNI.CkSsh_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ReadTimeoutMs() {
/* 346 */     return chilkatJNI.CkSsh_get_ReadTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadTimeoutMs(int var1) {
/* 350 */     chilkatJNI.CkSsh_put_ReadTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ReqExecCharset(CkString var1) {
/* 354 */     chilkatJNI.CkSsh_get_ReqExecCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String reqExecCharset() {
/* 358 */     return chilkatJNI.CkSsh_reqExecCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReqExecCharset(String var1) {
/* 362 */     chilkatJNI.CkSsh_put_ReqExecCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ServerIdentifier(CkString var1) {
/* 366 */     chilkatJNI.CkSsh_get_ServerIdentifier(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serverIdentifier() {
/* 370 */     return chilkatJNI.CkSsh_serverIdentifier(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SessionLog(CkString var1) {
/* 374 */     chilkatJNI.CkSsh_get_SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sessionLog() {
/* 378 */     return chilkatJNI.CkSsh_sessionLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SocksHostname(CkString var1) {
/* 382 */     chilkatJNI.CkSsh_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksHostname() {
/* 386 */     return chilkatJNI.CkSsh_socksHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksHostname(String var1) {
/* 390 */     chilkatJNI.CkSsh_put_SocksHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksPassword(CkString var1) {
/* 394 */     chilkatJNI.CkSsh_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksPassword() {
/* 398 */     return chilkatJNI.CkSsh_socksPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPassword(String var1) {
/* 402 */     chilkatJNI.CkSsh_put_SocksPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksPort() {
/* 406 */     return chilkatJNI.CkSsh_get_SocksPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPort(int var1) {
/* 410 */     chilkatJNI.CkSsh_put_SocksPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksUsername(CkString var1) {
/* 414 */     chilkatJNI.CkSsh_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksUsername() {
/* 418 */     return chilkatJNI.CkSsh_socksUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksUsername(String var1) {
/* 422 */     chilkatJNI.CkSsh_put_SocksUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksVersion() {
/* 426 */     return chilkatJNI.CkSsh_get_SocksVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksVersion(int var1) {
/* 430 */     chilkatJNI.CkSsh_put_SocksVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SoRcvBuf() {
/* 434 */     return chilkatJNI.CkSsh_get_SoRcvBuf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SoRcvBuf(int var1) {
/* 438 */     chilkatJNI.CkSsh_put_SoRcvBuf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SoSndBuf() {
/* 442 */     return chilkatJNI.CkSsh_get_SoSndBuf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SoSndBuf(int var1) {
/* 446 */     chilkatJNI.CkSsh_put_SoSndBuf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_StderrToStdout() {
/* 450 */     return chilkatJNI.CkSsh_get_StderrToStdout(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StderrToStdout(boolean var1) {
/* 454 */     chilkatJNI.CkSsh_put_StderrToStdout(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_StripColorCodes() {
/* 458 */     return chilkatJNI.CkSsh_get_StripColorCodes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StripColorCodes(boolean var1) {
/* 462 */     chilkatJNI.CkSsh_put_StripColorCodes(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_TcpNoDelay() {
/* 466 */     return chilkatJNI.CkSsh_get_TcpNoDelay(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TcpNoDelay(boolean var1) {
/* 470 */     chilkatJNI.CkSsh_put_TcpNoDelay(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UncommonOptions(CkString var1) {
/* 474 */     chilkatJNI.CkSsh_get_UncommonOptions(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String uncommonOptions() {
/* 478 */     return chilkatJNI.CkSsh_uncommonOptions(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UncommonOptions(String var1) {
/* 482 */     chilkatJNI.CkSsh_put_UncommonOptions(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UserAuthBanner(CkString var1) {
/* 486 */     chilkatJNI.CkSsh_get_UserAuthBanner(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String userAuthBanner() {
/* 490 */     return chilkatJNI.CkSsh_userAuthBanner(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserAuthBanner(String var1) {
/* 494 */     chilkatJNI.CkSsh_put_UserAuthBanner(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 498 */     return chilkatJNI.CkSsh_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 502 */     chilkatJNI.CkSsh_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 506 */     chilkatJNI.CkSsh_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 510 */     return chilkatJNI.CkSsh_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePk(String var1, CkSshKey var2) {
/* 514 */     return chilkatJNI.CkSsh_AuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePkAsync(String var1, CkSshKey var2) {
/* 518 */     long var3 = chilkatJNI.CkSsh_AuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/* 519 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePw(String var1, String var2) {
/* 523 */     return chilkatJNI.CkSsh_AuthenticatePw(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePwAsync(String var1, String var2) {
/* 527 */     long var3 = chilkatJNI.CkSsh_AuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/* 528 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePwPk(String var1, String var2, CkSshKey var3) {
/* 532 */     return chilkatJNI.CkSsh_AuthenticatePwPk(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePwPkAsync(String var1, String var2, CkSshKey var3) {
/* 536 */     long var4 = chilkatJNI.CkSsh_AuthenticatePwPkAsync(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/* 537 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticateSecPw(CkSecureString var1, CkSecureString var2) {
/* 541 */     return chilkatJNI.CkSsh_AuthenticateSecPw(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticateSecPwAsync(CkSecureString var1, CkSecureString var2) {
/* 545 */     long var3 = chilkatJNI.CkSsh_AuthenticateSecPwAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/* 546 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticateSecPwPk(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/* 550 */     return chilkatJNI.CkSsh_AuthenticateSecPwPk(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticateSecPwPkAsync(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/* 554 */     long var4 = chilkatJNI.CkSsh_AuthenticateSecPwPkAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/* 555 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelIsOpen(int var1) {
/* 559 */     return chilkatJNI.CkSsh_ChannelIsOpen(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int ChannelPoll(int var1, int var2) {
/* 563 */     return chilkatJNI.CkSsh_ChannelPoll(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ChannelPollAsync(int var1, int var2) {
/* 567 */     long var3 = chilkatJNI.CkSsh_ChannelPollAsync(this.swigCPtr, this, var1, var2);
/* 568 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public int ChannelRead(int var1) {
/* 572 */     return chilkatJNI.CkSsh_ChannelRead(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReadAsync(int var1) {
/* 576 */     long var2 = chilkatJNI.CkSsh_ChannelReadAsync(this.swigCPtr, this, var1);
/* 577 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public int ChannelReadAndPoll(int var1, int var2) {
/* 581 */     return chilkatJNI.CkSsh_ChannelReadAndPoll(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReadAndPollAsync(int var1, int var2) {
/* 585 */     long var3 = chilkatJNI.CkSsh_ChannelReadAndPollAsync(this.swigCPtr, this, var1, var2);
/* 586 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public int ChannelReadAndPoll2(int var1, int var2, int var3) {
/* 590 */     return chilkatJNI.CkSsh_ChannelReadAndPoll2(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReadAndPoll2Async(int var1, int var2, int var3) {
/* 594 */     long var4 = chilkatJNI.CkSsh_ChannelReadAndPoll2Async(this.swigCPtr, this, var1, var2, var3);
/* 595 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceivedClose(int var1) {
/* 599 */     return chilkatJNI.CkSsh_ChannelReceivedClose(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceivedEof(int var1) {
/* 603 */     return chilkatJNI.CkSsh_ChannelReceivedEof(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceivedExitStatus(int var1) {
/* 607 */     return chilkatJNI.CkSsh_ChannelReceivedExitStatus(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceiveToClose(int var1) {
/* 611 */     return chilkatJNI.CkSsh_ChannelReceiveToClose(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReceiveToCloseAsync(int var1) {
/* 615 */     long var2 = chilkatJNI.CkSsh_ChannelReceiveToCloseAsync(this.swigCPtr, this, var1);
/* 616 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceiveUntilMatch(int var1, String var2, String var3, boolean var4) {
/* 620 */     return chilkatJNI.CkSsh_ChannelReceiveUntilMatch(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReceiveUntilMatchAsync(int var1, String var2, String var3, boolean var4) {
/* 624 */     long var5 = chilkatJNI.CkSsh_ChannelReceiveUntilMatchAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 625 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelReceiveUntilMatchN(int var1, CkStringArray var2, String var3, boolean var4) {
/* 629 */     return chilkatJNI.CkSsh_ChannelReceiveUntilMatchN(this.swigCPtr, this, var1, CkStringArray.getCPtr(var2), var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkTask ChannelReceiveUntilMatchNAsync(int var1, CkStringArray var2, String var3, boolean var4) {
/* 633 */     long var5 = chilkatJNI.CkSsh_ChannelReceiveUntilMatchNAsync(this.swigCPtr, this, var1, CkStringArray.getCPtr(var2), var2, var3, var4);
/* 634 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public void ChannelRelease(int var1) {
/* 638 */     chilkatJNI.CkSsh_ChannelRelease(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ChannelSendClose(int var1) {
/* 642 */     return chilkatJNI.CkSsh_ChannelSendClose(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ChannelSendCloseAsync(int var1) {
/* 646 */     long var2 = chilkatJNI.CkSsh_ChannelSendCloseAsync(this.swigCPtr, this, var1);
/* 647 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelSendData(int var1, CkByteData var2) {
/* 651 */     return chilkatJNI.CkSsh_ChannelSendData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask ChannelSendDataAsync(int var1, CkByteData var2) {
/* 655 */     long var3 = chilkatJNI.CkSsh_ChannelSendDataAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 656 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelSendEof(int var1) {
/* 660 */     return chilkatJNI.CkSsh_ChannelSendEof(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ChannelSendEofAsync(int var1) {
/* 664 */     long var2 = chilkatJNI.CkSsh_ChannelSendEofAsync(this.swigCPtr, this, var1);
/* 665 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ChannelSendString(int var1, String var2, String var3) {
/* 669 */     return chilkatJNI.CkSsh_ChannelSendString(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask ChannelSendStringAsync(int var1, String var2, String var3) {
/* 673 */     long var4 = chilkatJNI.CkSsh_ChannelSendStringAsync(this.swigCPtr, this, var1, var2, var3);
/* 674 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean CheckConnection() {
/* 678 */     return chilkatJNI.CkSsh_CheckConnection(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearTtyModes() {
/* 682 */     chilkatJNI.CkSsh_ClearTtyModes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Connect(String var1, int var2) {
/* 686 */     return chilkatJNI.CkSsh_Connect(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ConnectAsync(String var1, int var2) {
/* 690 */     long var3 = chilkatJNI.CkSsh_ConnectAsync(this.swigCPtr, this, var1, var2);
/* 691 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ConnectThroughSsh(CkSsh var1, String var2, int var3) {
/* 695 */     return chilkatJNI.CkSsh_ConnectThroughSsh(this.swigCPtr, this, getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask ConnectThroughSshAsync(CkSsh var1, String var2, int var3) {
/* 699 */     long var4 = chilkatJNI.CkSsh_ConnectThroughSshAsync(this.swigCPtr, this, getCPtr(var1), var1, var2, var3);
/* 700 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean ContinueKeyboardAuth(String var1, CkString var2) {
/* 704 */     return chilkatJNI.CkSsh_ContinueKeyboardAuth(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String continueKeyboardAuth(String var1) {
/* 708 */     return chilkatJNI.CkSsh_continueKeyboardAuth(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ContinueKeyboardAuthAsync(String var1) {
/* 712 */     long var2 = chilkatJNI.CkSsh_ContinueKeyboardAuthAsync(this.swigCPtr, this, var1);
/* 713 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public void Disconnect() {
/* 717 */     chilkatJNI.CkSsh_Disconnect(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int GetChannelExitStatus(int var1) {
/* 721 */     return chilkatJNI.CkSsh_GetChannelExitStatus(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetChannelNumber(int var1) {
/* 725 */     return chilkatJNI.CkSsh_GetChannelNumber(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChannelType(int var1, CkString var2) {
/* 729 */     return chilkatJNI.CkSsh_GetChannelType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getChannelType(int var1) {
/* 733 */     return chilkatJNI.CkSsh_getChannelType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String channelType(int var1) {
/* 737 */     return chilkatJNI.CkSsh_channelType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedData(int var1, CkByteData var2) {
/* 741 */     return chilkatJNI.CkSsh_GetReceivedData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedDataN(int var1, int var2, CkByteData var3) {
/* 745 */     return chilkatJNI.CkSsh_GetReceivedDataN(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public int GetReceivedNumBytes(int var1) {
/* 749 */     return chilkatJNI.CkSsh_GetReceivedNumBytes(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedStderr(int var1, CkByteData var2) {
/* 753 */     return chilkatJNI.CkSsh_GetReceivedStderr(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedStderrText(int var1, String var2, CkString var3) {
/* 757 */     return chilkatJNI.CkSsh_GetReceivedStderrText(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getReceivedStderrText(int var1, String var2) {
/* 761 */     return chilkatJNI.CkSsh_getReceivedStderrText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String receivedStderrText(int var1, String var2) {
/* 765 */     return chilkatJNI.CkSsh_receivedStderrText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedText(int var1, String var2, CkString var3) {
/* 769 */     return chilkatJNI.CkSsh_GetReceivedText(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getReceivedText(int var1, String var2) {
/* 773 */     return chilkatJNI.CkSsh_getReceivedText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String receivedText(int var1, String var2) {
/* 777 */     return chilkatJNI.CkSsh_receivedText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetReceivedTextS(int var1, String var2, String var3, CkString var4) {
/* 781 */     return chilkatJNI.CkSsh_GetReceivedTextS(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getReceivedTextS(int var1, String var2, String var3) {
/* 785 */     return chilkatJNI.CkSsh_getReceivedTextS(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String receivedTextS(int var1, String var2, String var3) {
/* 789 */     return chilkatJNI.CkSsh_receivedTextS(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public int OpenCustomChannel(String var1) {
/* 793 */     return chilkatJNI.CkSsh_OpenCustomChannel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask OpenCustomChannelAsync(String var1) {
/* 797 */     long var2 = chilkatJNI.CkSsh_OpenCustomChannelAsync(this.swigCPtr, this, var1);
/* 798 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public int OpenDirectTcpIpChannel(String var1, int var2) {
/* 802 */     return chilkatJNI.CkSsh_OpenDirectTcpIpChannel(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask OpenDirectTcpIpChannelAsync(String var1, int var2) {
/* 806 */     long var3 = chilkatJNI.CkSsh_OpenDirectTcpIpChannelAsync(this.swigCPtr, this, var1, var2);
/* 807 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public int OpenSessionChannel() {
/* 811 */     return chilkatJNI.CkSsh_OpenSessionChannel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask OpenSessionChannelAsync() {
/* 815 */     long var1 = chilkatJNI.CkSsh_OpenSessionChannelAsync(this.swigCPtr, this);
/* 816 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean PeekReceivedText(int var1, String var2, CkString var3) {
/* 820 */     return chilkatJNI.CkSsh_PeekReceivedText(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String peekReceivedText(int var1, String var2) {
/* 824 */     return chilkatJNI.CkSsh_peekReceivedText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int QuickCmdCheck(int var1) {
/* 828 */     return chilkatJNI.CkSsh_QuickCmdCheck(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask QuickCmdCheckAsync(int var1) {
/* 832 */     long var2 = chilkatJNI.CkSsh_QuickCmdCheckAsync(this.swigCPtr, this, var1);
/* 833 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public int QuickCmdSend(String var1) {
/* 837 */     return chilkatJNI.CkSsh_QuickCmdSend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask QuickCmdSendAsync(String var1) {
/* 841 */     long var2 = chilkatJNI.CkSsh_QuickCmdSendAsync(this.swigCPtr, this, var1);
/* 842 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean QuickCommand(String var1, String var2, CkString var3) {
/* 846 */     return chilkatJNI.CkSsh_QuickCommand(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String quickCommand(String var1, String var2) {
/* 850 */     return chilkatJNI.CkSsh_quickCommand(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask QuickCommandAsync(String var1, String var2) {
/* 854 */     long var3 = chilkatJNI.CkSsh_QuickCommandAsync(this.swigCPtr, this, var1, var2);
/* 855 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public int QuickShell() {
/* 859 */     return chilkatJNI.CkSsh_QuickShell(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask QuickShellAsync() {
/* 863 */     long var1 = chilkatJNI.CkSsh_QuickShellAsync(this.swigCPtr, this);
/* 864 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReKey() {
/* 868 */     return chilkatJNI.CkSsh_ReKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReKeyAsync() {
/* 872 */     long var1 = chilkatJNI.CkSsh_ReKeyAsync(this.swigCPtr, this);
/* 873 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 877 */     return chilkatJNI.CkSsh_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SendIgnore() {
/* 881 */     return chilkatJNI.CkSsh_SendIgnore(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask SendIgnoreAsync() {
/* 885 */     long var1 = chilkatJNI.CkSsh_SendIgnoreAsync(this.swigCPtr, this);
/* 886 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqExec(int var1, String var2) {
/* 890 */     return chilkatJNI.CkSsh_SendReqExec(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqExecAsync(int var1, String var2) {
/* 894 */     long var3 = chilkatJNI.CkSsh_SendReqExecAsync(this.swigCPtr, this, var1, var2);
/* 895 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqPty(int var1, String var2, int var3, int var4, int var5, int var6) {
/* 899 */     return chilkatJNI.CkSsh_SendReqPty(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*     */   }
/*     */   
/*     */   public CkTask SendReqPtyAsync(int var1, String var2, int var3, int var4, int var5, int var6) {
/* 903 */     long var7 = chilkatJNI.CkSsh_SendReqPtyAsync(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/* 904 */     return var7 == 0L ? null : new CkTask(var7, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqSetEnv(int var1, String var2, String var3) {
/* 908 */     return chilkatJNI.CkSsh_SendReqSetEnv(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqSetEnvAsync(int var1, String var2, String var3) {
/* 912 */     long var4 = chilkatJNI.CkSsh_SendReqSetEnvAsync(this.swigCPtr, this, var1, var2, var3);
/* 913 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqShell(int var1) {
/* 917 */     return chilkatJNI.CkSsh_SendReqShell(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask SendReqShellAsync(int var1) {
/* 921 */     long var2 = chilkatJNI.CkSsh_SendReqShellAsync(this.swigCPtr, this, var1);
/* 922 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqSignal(int var1, String var2) {
/* 926 */     return chilkatJNI.CkSsh_SendReqSignal(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqSignalAsync(int var1, String var2) {
/* 930 */     long var3 = chilkatJNI.CkSsh_SendReqSignalAsync(this.swigCPtr, this, var1, var2);
/* 931 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqSubsystem(int var1, String var2) {
/* 935 */     return chilkatJNI.CkSsh_SendReqSubsystem(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqSubsystemAsync(int var1, String var2) {
/* 939 */     long var3 = chilkatJNI.CkSsh_SendReqSubsystemAsync(this.swigCPtr, this, var1, var2);
/* 940 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqWindowChange(int var1, int var2, int var3, int var4, int var5) {
/* 944 */     return chilkatJNI.CkSsh_SendReqWindowChange(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public CkTask SendReqWindowChangeAsync(int var1, int var2, int var3, int var4, int var5) {
/* 948 */     long var6 = chilkatJNI.CkSsh_SendReqWindowChangeAsync(this.swigCPtr, this, var1, var2, var3, var4, var5);
/* 949 */     return var6 == 0L ? null : new CkTask(var6, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqX11Forwarding(int var1, boolean var2, String var3, String var4, int var5) {
/* 953 */     return chilkatJNI.CkSsh_SendReqX11Forwarding(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public CkTask SendReqX11ForwardingAsync(int var1, boolean var2, String var3, String var4, int var5) {
/* 957 */     long var6 = chilkatJNI.CkSsh_SendReqX11ForwardingAsync(this.swigCPtr, this, var1, var2, var3, var4, var5);
/* 958 */     return var6 == 0L ? null : new CkTask(var6, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqXonXoff(int var1, boolean var2) {
/* 962 */     return chilkatJNI.CkSsh_SendReqXonXoff(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqXonXoffAsync(int var1, boolean var2) {
/* 966 */     long var3 = chilkatJNI.CkSsh_SendReqXonXoffAsync(this.swigCPtr, this, var1, var2);
/* 967 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SetTtyMode(String var1, int var2) {
/* 971 */     return chilkatJNI.CkSsh_SetTtyMode(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean StartKeyboardAuth(String var1, CkString var2) {
/* 975 */     return chilkatJNI.CkSsh_StartKeyboardAuth(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String startKeyboardAuth(String var1) {
/* 979 */     return chilkatJNI.CkSsh_startKeyboardAuth(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask StartKeyboardAuthAsync(String var1) {
/* 983 */     long var2 = chilkatJNI.CkSsh_StartKeyboardAuthAsync(this.swigCPtr, this, var1);
/* 984 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 988 */     return chilkatJNI.CkSsh_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int WaitForChannelMessage(int var1) {
/* 992 */     return chilkatJNI.CkSsh_WaitForChannelMessage(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WaitForChannelMessageAsync(int var1) {
/* 996 */     long var2 = chilkatJNI.CkSsh_WaitForChannelMessageAsync(this.swigCPtr, this, var1);
/* 997 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSsh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */