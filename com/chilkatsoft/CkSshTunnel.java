/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSshTunnel
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSshTunnel(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSshTunnel var0) {
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
/*  29 */         chilkatJNI.delete_CkSshTunnel(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSshTunnel()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSshTunnel(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkSshTunnel_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkSshTunnel_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkSshTunnel_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkSshTunnel_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkSshTunnel_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkSshTunnel_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AcceptLog(CkString var1) {
/*  66 */     chilkatJNI.CkSshTunnel_get_AcceptLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String acceptLog() {
/*  70 */     return chilkatJNI.CkSshTunnel_acceptLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AcceptLog(String var1) {
/*  74 */     chilkatJNI.CkSshTunnel_put_AcceptLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AcceptLogPath(CkString var1) {
/*  78 */     chilkatJNI.CkSshTunnel_get_AcceptLogPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String acceptLogPath() {
/*  82 */     return chilkatJNI.CkSshTunnel_acceptLogPath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AcceptLogPath(String var1) {
/*  86 */     chilkatJNI.CkSshTunnel_put_AcceptLogPath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ConnectTimeoutMs() {
/*  90 */     return chilkatJNI.CkSshTunnel_get_ConnectTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConnectTimeoutMs(int var1) {
/*  94 */     chilkatJNI.CkSshTunnel_put_ConnectTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  98 */     chilkatJNI.CkSshTunnel_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 102 */     return chilkatJNI.CkSshTunnel_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 106 */     chilkatJNI.CkSshTunnel_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DestHostname(CkString var1) {
/* 110 */     chilkatJNI.CkSshTunnel_get_DestHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String destHostname() {
/* 114 */     return chilkatJNI.CkSshTunnel_destHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DestHostname(String var1) {
/* 118 */     chilkatJNI.CkSshTunnel_put_DestHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DestPort() {
/* 122 */     return chilkatJNI.CkSshTunnel_get_DestPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DestPort(int var1) {
/* 126 */     chilkatJNI.CkSshTunnel_put_DestPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DynamicPortForwarding() {
/* 130 */     return chilkatJNI.CkSshTunnel_get_DynamicPortForwarding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DynamicPortForwarding(boolean var1) {
/* 134 */     chilkatJNI.CkSshTunnel_put_DynamicPortForwarding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HostKeyFingerprint(CkString var1) {
/* 138 */     chilkatJNI.CkSshTunnel_get_HostKeyFingerprint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hostKeyFingerprint() {
/* 142 */     return chilkatJNI.CkSshTunnel_hostKeyFingerprint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyAuthMethod(CkString var1) {
/* 146 */     chilkatJNI.CkSshTunnel_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyAuthMethod() {
/* 150 */     return chilkatJNI.CkSshTunnel_httpProxyAuthMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyAuthMethod(String var1) {
/* 154 */     chilkatJNI.CkSshTunnel_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyDomain(CkString var1) {
/* 158 */     chilkatJNI.CkSshTunnel_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyDomain() {
/* 162 */     return chilkatJNI.CkSshTunnel_httpProxyDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyDomain(String var1) {
/* 166 */     chilkatJNI.CkSshTunnel_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyHostname(CkString var1) {
/* 170 */     chilkatJNI.CkSshTunnel_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyHostname() {
/* 174 */     return chilkatJNI.CkSshTunnel_httpProxyHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyHostname(String var1) {
/* 178 */     chilkatJNI.CkSshTunnel_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyPassword(CkString var1) {
/* 182 */     chilkatJNI.CkSshTunnel_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyPassword() {
/* 186 */     return chilkatJNI.CkSshTunnel_httpProxyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyPassword(String var1) {
/* 190 */     chilkatJNI.CkSshTunnel_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HttpProxyPort() {
/* 194 */     return chilkatJNI.CkSshTunnel_get_HttpProxyPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyPort(int var1) {
/* 198 */     chilkatJNI.CkSshTunnel_put_HttpProxyPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpProxyUsername(CkString var1) {
/* 202 */     chilkatJNI.CkSshTunnel_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpProxyUsername() {
/* 206 */     return chilkatJNI.CkSshTunnel_httpProxyUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpProxyUsername(String var1) {
/* 210 */     chilkatJNI.CkSshTunnel_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_IdleTimeoutMs() {
/* 214 */     return chilkatJNI.CkSshTunnel_get_IdleTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IdleTimeoutMs(int var1) {
/* 218 */     chilkatJNI.CkSshTunnel_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_InboundSocksPassword(CkString var1) {
/* 222 */     chilkatJNI.CkSshTunnel_get_InboundSocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String inboundSocksPassword() {
/* 226 */     return chilkatJNI.CkSshTunnel_inboundSocksPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_InboundSocksPassword(String var1) {
/* 230 */     chilkatJNI.CkSshTunnel_put_InboundSocksPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_InboundSocksUsername(CkString var1) {
/* 234 */     chilkatJNI.CkSshTunnel_get_InboundSocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String inboundSocksUsername() {
/* 238 */     return chilkatJNI.CkSshTunnel_inboundSocksUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_InboundSocksUsername(String var1) {
/* 242 */     chilkatJNI.CkSshTunnel_put_InboundSocksUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsAccepting() {
/* 246 */     return chilkatJNI.CkSshTunnel_get_IsAccepting(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_KeepAcceptLog() {
/* 250 */     return chilkatJNI.CkSshTunnel_get_KeepAcceptLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeepAcceptLog(boolean var1) {
/* 254 */     chilkatJNI.CkSshTunnel_put_KeepAcceptLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_KeepTunnelLog() {
/* 258 */     return chilkatJNI.CkSshTunnel_get_KeepTunnelLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeepTunnelLog(boolean var1) {
/* 262 */     chilkatJNI.CkSshTunnel_put_KeepTunnelLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 266 */     chilkatJNI.CkSshTunnel_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 270 */     return chilkatJNI.CkSshTunnel_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 274 */     chilkatJNI.CkSshTunnel_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 278 */     return chilkatJNI.CkSshTunnel_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 282 */     chilkatJNI.CkSshTunnel_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 286 */     return chilkatJNI.CkSshTunnel_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 290 */     return chilkatJNI.CkSshTunnel_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 294 */     chilkatJNI.CkSshTunnel_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ListenBindIpAddress(CkString var1) {
/* 298 */     chilkatJNI.CkSshTunnel_get_ListenBindIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String listenBindIpAddress() {
/* 302 */     return chilkatJNI.CkSshTunnel_listenBindIpAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ListenBindIpAddress(String var1) {
/* 306 */     chilkatJNI.CkSshTunnel_put_ListenBindIpAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ListenPort() {
/* 310 */     return chilkatJNI.CkSshTunnel_get_ListenPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_OutboundBindIpAddress(CkString var1) {
/* 314 */     chilkatJNI.CkSshTunnel_get_OutboundBindIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String outboundBindIpAddress() {
/* 318 */     return chilkatJNI.CkSshTunnel_outboundBindIpAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OutboundBindIpAddress(String var1) {
/* 322 */     chilkatJNI.CkSshTunnel_put_OutboundBindIpAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_OutboundBindPort() {
/* 326 */     return chilkatJNI.CkSshTunnel_get_OutboundBindPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OutboundBindPort(int var1) {
/* 330 */     chilkatJNI.CkSshTunnel_put_OutboundBindPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 334 */     return chilkatJNI.CkSshTunnel_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 338 */     chilkatJNI.CkSshTunnel_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksHostname(CkString var1) {
/* 342 */     chilkatJNI.CkSshTunnel_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksHostname() {
/* 346 */     return chilkatJNI.CkSshTunnel_socksHostname(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksHostname(String var1) {
/* 350 */     chilkatJNI.CkSshTunnel_put_SocksHostname(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksPassword(CkString var1) {
/* 354 */     chilkatJNI.CkSshTunnel_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksPassword() {
/* 358 */     return chilkatJNI.CkSshTunnel_socksPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPassword(String var1) {
/* 362 */     chilkatJNI.CkSshTunnel_put_SocksPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksPort() {
/* 366 */     return chilkatJNI.CkSshTunnel_get_SocksPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksPort(int var1) {
/* 370 */     chilkatJNI.CkSshTunnel_put_SocksPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SocksUsername(CkString var1) {
/* 374 */     chilkatJNI.CkSshTunnel_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String socksUsername() {
/* 378 */     return chilkatJNI.CkSshTunnel_socksUsername(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksUsername(String var1) {
/* 382 */     chilkatJNI.CkSshTunnel_put_SocksUsername(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SocksVersion() {
/* 386 */     return chilkatJNI.CkSshTunnel_get_SocksVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SocksVersion(int var1) {
/* 390 */     chilkatJNI.CkSshTunnel_put_SocksVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SoRcvBuf() {
/* 394 */     return chilkatJNI.CkSshTunnel_get_SoRcvBuf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SoRcvBuf(int var1) {
/* 398 */     chilkatJNI.CkSshTunnel_put_SoRcvBuf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SoSndBuf() {
/* 402 */     return chilkatJNI.CkSshTunnel_get_SoSndBuf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SoSndBuf(int var1) {
/* 406 */     chilkatJNI.CkSshTunnel_put_SoSndBuf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_TcpNoDelay() {
/* 410 */     return chilkatJNI.CkSshTunnel_get_TcpNoDelay(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TcpNoDelay(boolean var1) {
/* 414 */     chilkatJNI.CkSshTunnel_put_TcpNoDelay(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TunnelLog(CkString var1) {
/* 418 */     chilkatJNI.CkSshTunnel_get_TunnelLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tunnelLog() {
/* 422 */     return chilkatJNI.CkSshTunnel_tunnelLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TunnelLog(String var1) {
/* 426 */     chilkatJNI.CkSshTunnel_put_TunnelLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TunnelLogPath(CkString var1) {
/* 430 */     chilkatJNI.CkSshTunnel_get_TunnelLogPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tunnelLogPath() {
/* 434 */     return chilkatJNI.CkSshTunnel_tunnelLogPath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TunnelLogPath(String var1) {
/* 438 */     chilkatJNI.CkSshTunnel_put_TunnelLogPath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UncommonOptions(CkString var1) {
/* 442 */     chilkatJNI.CkSshTunnel_get_UncommonOptions(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String uncommonOptions() {
/* 446 */     return chilkatJNI.CkSshTunnel_uncommonOptions(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UncommonOptions(String var1) {
/* 450 */     chilkatJNI.CkSshTunnel_put_UncommonOptions(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 454 */     return chilkatJNI.CkSshTunnel_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 458 */     chilkatJNI.CkSshTunnel_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 462 */     chilkatJNI.CkSshTunnel_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 466 */     return chilkatJNI.CkSshTunnel_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePk(String var1, CkSshKey var2) {
/* 470 */     return chilkatJNI.CkSshTunnel_AuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePkAsync(String var1, CkSshKey var2) {
/* 474 */     long var3 = chilkatJNI.CkSshTunnel_AuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/* 475 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePw(String var1, String var2) {
/* 479 */     return chilkatJNI.CkSshTunnel_AuthenticatePw(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePwAsync(String var1, String var2) {
/* 483 */     long var3 = chilkatJNI.CkSshTunnel_AuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/* 484 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticatePwPk(String var1, String var2, CkSshKey var3) {
/* 488 */     return chilkatJNI.CkSshTunnel_AuthenticatePwPk(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticatePwPkAsync(String var1, String var2, CkSshKey var3) {
/* 492 */     long var4 = chilkatJNI.CkSshTunnel_AuthenticatePwPkAsync(this.swigCPtr, this, var1, var2, CkSshKey.getCPtr(var3), var3);
/* 493 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticateSecPw(CkSecureString var1, CkSecureString var2) {
/* 497 */     return chilkatJNI.CkSshTunnel_AuthenticateSecPw(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticateSecPwAsync(CkSecureString var1, CkSecureString var2) {
/* 501 */     long var3 = chilkatJNI.CkSshTunnel_AuthenticateSecPwAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/* 502 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AuthenticateSecPwPk(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/* 506 */     return chilkatJNI.CkSshTunnel_AuthenticateSecPwPk(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask AuthenticateSecPwPkAsync(CkSecureString var1, CkSecureString var2, CkSshKey var3) {
/* 510 */     long var4 = chilkatJNI.CkSshTunnel_AuthenticateSecPwPkAsync(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2, CkSshKey.getCPtr(var3), var3);
/* 511 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean BeginAccepting(int var1) {
/* 515 */     return chilkatJNI.CkSshTunnel_BeginAccepting(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask BeginAcceptingAsync(int var1) {
/* 519 */     long var2 = chilkatJNI.CkSshTunnel_BeginAcceptingAsync(this.swigCPtr, this, var1);
/* 520 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CloseTunnel(boolean var1) {
/* 524 */     return chilkatJNI.CkSshTunnel_CloseTunnel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Connect(String var1, int var2) {
/* 528 */     return chilkatJNI.CkSshTunnel_Connect(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ConnectAsync(String var1, int var2) {
/* 532 */     long var3 = chilkatJNI.CkSshTunnel_ConnectAsync(this.swigCPtr, this, var1, var2);
/* 533 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ConnectThroughSsh(CkSsh var1, String var2, int var3) {
/* 537 */     return chilkatJNI.CkSshTunnel_ConnectThroughSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask ConnectThroughSshAsync(CkSsh var1, String var2, int var3) {
/* 541 */     long var4 = chilkatJNI.CkSshTunnel_ConnectThroughSshAsync(this.swigCPtr, this, CkSsh.getCPtr(var1), var1, var2, var3);
/* 542 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean DisconnectAllClients(boolean var1) {
/* 546 */     return chilkatJNI.CkSshTunnel_DisconnectAllClients(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCurrentState(CkString var1) {
/* 550 */     return chilkatJNI.CkSshTunnel_GetCurrentState(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getCurrentState() {
/* 554 */     return chilkatJNI.CkSshTunnel_getCurrentState(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String currentState() {
/* 558 */     return chilkatJNI.CkSshTunnel_currentState(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsSshConnected() {
/* 562 */     return chilkatJNI.CkSshTunnel_IsSshConnected(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 566 */     return chilkatJNI.CkSshTunnel_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StopAccepting(boolean var1) {
/* 570 */     return chilkatJNI.CkSshTunnel_StopAccepting(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 574 */     return chilkatJNI.CkSshTunnel_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSshTunnel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */