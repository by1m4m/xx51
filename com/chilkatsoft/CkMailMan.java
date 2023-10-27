/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkMailMan
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkMailMan(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkMailMan var0) {
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
/*   29 */         chilkatJNI.delete_CkMailMan(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkMailMan()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkMailMan(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkMailMan_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkMailMan_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkMailMan_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkMailManProgress var1) {
/*   54 */     chilkatJNI.CkMailMan_put_EventCallbackObject(this.swigCPtr, this, CkMailManProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkMailMan_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkMailMan_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AllOrNone() {
/*   66 */     return chilkatJNI.CkMailMan_get_AllOrNone(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AllOrNone(boolean var1) {
/*   70 */     chilkatJNI.CkMailMan_put_AllOrNone(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoFix() {
/*   74 */     return chilkatJNI.CkMailMan_get_AutoFix(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoFix(boolean var1) {
/*   78 */     chilkatJNI.CkMailMan_put_AutoFix(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoGenMessageId() {
/*   82 */     return chilkatJNI.CkMailMan_get_AutoGenMessageId(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoGenMessageId(boolean var1) {
/*   86 */     chilkatJNI.CkMailMan_put_AutoGenMessageId(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoSmtpRset() {
/*   90 */     return chilkatJNI.CkMailMan_get_AutoSmtpRset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoSmtpRset(boolean var1) {
/*   94 */     chilkatJNI.CkMailMan_put_AutoSmtpRset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_AutoUnwrapSecurity() {
/*   98 */     return chilkatJNI.CkMailMan_get_AutoUnwrapSecurity(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AutoUnwrapSecurity(boolean var1) {
/*  102 */     chilkatJNI.CkMailMan_put_AutoUnwrapSecurity(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_ClientIpAddress(CkString var1) {
/*  106 */     chilkatJNI.CkMailMan_get_ClientIpAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String clientIpAddress() {
/*  110 */     return chilkatJNI.CkMailMan_clientIpAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ClientIpAddress(String var1) {
/*  114 */     chilkatJNI.CkMailMan_put_ClientIpAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ConnectFailReason() {
/*  118 */     return chilkatJNI.CkMailMan_get_ConnectFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_ConnectTimeout() {
/*  122 */     return chilkatJNI.CkMailMan_get_ConnectTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ConnectTimeout(int var1) {
/*  126 */     chilkatJNI.CkMailMan_put_ConnectTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  130 */     chilkatJNI.CkMailMan_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  134 */     return chilkatJNI.CkMailMan_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  138 */     chilkatJNI.CkMailMan_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DsnEnvid(CkString var1) {
/*  142 */     chilkatJNI.CkMailMan_get_DsnEnvid(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dsnEnvid() {
/*  146 */     return chilkatJNI.CkMailMan_dsnEnvid(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DsnEnvid(String var1) {
/*  150 */     chilkatJNI.CkMailMan_put_DsnEnvid(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DsnNotify(CkString var1) {
/*  154 */     chilkatJNI.CkMailMan_get_DsnNotify(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dsnNotify() {
/*  158 */     return chilkatJNI.CkMailMan_dsnNotify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DsnNotify(String var1) {
/*  162 */     chilkatJNI.CkMailMan_put_DsnNotify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DsnRet(CkString var1) {
/*  166 */     chilkatJNI.CkMailMan_get_DsnRet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String dsnRet() {
/*  170 */     return chilkatJNI.CkMailMan_dsnRet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DsnRet(String var1) {
/*  174 */     chilkatJNI.CkMailMan_put_DsnRet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_EmbedCertChain() {
/*  178 */     return chilkatJNI.CkMailMan_get_EmbedCertChain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_EmbedCertChain(boolean var1) {
/*  182 */     chilkatJNI.CkMailMan_put_EmbedCertChain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Filter(CkString var1) {
/*  186 */     chilkatJNI.CkMailMan_get_Filter(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String filter() {
/*  190 */     return chilkatJNI.CkMailMan_filter(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Filter(String var1) {
/*  194 */     chilkatJNI.CkMailMan_put_Filter(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  198 */     return chilkatJNI.CkMailMan_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  202 */     chilkatJNI.CkMailMan_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HeloHostname(CkString var1) {
/*  206 */     chilkatJNI.CkMailMan_get_HeloHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String heloHostname() {
/*  210 */     return chilkatJNI.CkMailMan_heloHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeloHostname(String var1) {
/*  214 */     chilkatJNI.CkMailMan_put_HeloHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyAuthMethod(CkString var1) {
/*  218 */     chilkatJNI.CkMailMan_get_HttpProxyAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyAuthMethod() {
/*  222 */     return chilkatJNI.CkMailMan_httpProxyAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyAuthMethod(String var1) {
/*  226 */     chilkatJNI.CkMailMan_put_HttpProxyAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyDomain(CkString var1) {
/*  230 */     chilkatJNI.CkMailMan_get_HttpProxyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyDomain() {
/*  234 */     return chilkatJNI.CkMailMan_httpProxyDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyDomain(String var1) {
/*  238 */     chilkatJNI.CkMailMan_put_HttpProxyDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyHostname(CkString var1) {
/*  242 */     chilkatJNI.CkMailMan_get_HttpProxyHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyHostname() {
/*  246 */     return chilkatJNI.CkMailMan_httpProxyHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyHostname(String var1) {
/*  250 */     chilkatJNI.CkMailMan_put_HttpProxyHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyPassword(CkString var1) {
/*  254 */     chilkatJNI.CkMailMan_get_HttpProxyPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyPassword() {
/*  258 */     return chilkatJNI.CkMailMan_httpProxyPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPassword(String var1) {
/*  262 */     chilkatJNI.CkMailMan_put_HttpProxyPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HttpProxyPort() {
/*  266 */     return chilkatJNI.CkMailMan_get_HttpProxyPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyPort(int var1) {
/*  270 */     chilkatJNI.CkMailMan_put_HttpProxyPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HttpProxyUsername(CkString var1) {
/*  274 */     chilkatJNI.CkMailMan_get_HttpProxyUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String httpProxyUsername() {
/*  278 */     return chilkatJNI.CkMailMan_httpProxyUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HttpProxyUsername(String var1) {
/*  282 */     chilkatJNI.CkMailMan_put_HttpProxyUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ImmediateDelete() {
/*  286 */     return chilkatJNI.CkMailMan_get_ImmediateDelete(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ImmediateDelete(boolean var1) {
/*  290 */     chilkatJNI.CkMailMan_put_ImmediateDelete(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IncludeRootCert() {
/*  294 */     return chilkatJNI.CkMailMan_get_IncludeRootCert(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IncludeRootCert(boolean var1) {
/*  298 */     chilkatJNI.CkMailMan_put_IncludeRootCert(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IsPop3Connected() {
/*  302 */     return chilkatJNI.CkMailMan_get_IsPop3Connected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_IsSmtpConnected() {
/*  306 */     return chilkatJNI.CkMailMan_get_IsSmtpConnected(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  310 */     chilkatJNI.CkMailMan_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  314 */     return chilkatJNI.CkMailMan_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  318 */     chilkatJNI.CkMailMan_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  322 */     return chilkatJNI.CkMailMan_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  326 */     chilkatJNI.CkMailMan_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  330 */     return chilkatJNI.CkMailMan_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  334 */     return chilkatJNI.CkMailMan_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  338 */     chilkatJNI.CkMailMan_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastSendQFilename(CkString var1) {
/*  342 */     chilkatJNI.CkMailMan_get_LastSendQFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastSendQFilename() {
/*  346 */     return chilkatJNI.CkMailMan_lastSendQFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_LastSmtpStatus() {
/*  350 */     return chilkatJNI.CkMailMan_get_LastSmtpStatus(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LogMailReceivedFilename(CkString var1) {
/*  354 */     chilkatJNI.CkMailMan_get_LogMailReceivedFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String logMailReceivedFilename() {
/*  358 */     return chilkatJNI.CkMailMan_logMailReceivedFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LogMailReceivedFilename(String var1) {
/*  362 */     chilkatJNI.CkMailMan_put_LogMailReceivedFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LogMailSentFilename(CkString var1) {
/*  366 */     chilkatJNI.CkMailMan_get_LogMailSentFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String logMailSentFilename() {
/*  370 */     return chilkatJNI.CkMailMan_logMailSentFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LogMailSentFilename(String var1) {
/*  374 */     chilkatJNI.CkMailMan_put_LogMailSentFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_MailHost(CkString var1) {
/*  378 */     chilkatJNI.CkMailMan_get_MailHost(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String mailHost() {
/*  382 */     return chilkatJNI.CkMailMan_mailHost(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MailHost(String var1) {
/*  386 */     chilkatJNI.CkMailMan_put_MailHost(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MailPort() {
/*  390 */     return chilkatJNI.CkMailMan_get_MailPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MailPort(int var1) {
/*  394 */     chilkatJNI.CkMailMan_put_MailPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_MaxCount() {
/*  398 */     return chilkatJNI.CkMailMan_get_MaxCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MaxCount(int var1) {
/*  402 */     chilkatJNI.CkMailMan_put_MaxCount(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OAuth2AccessToken(CkString var1) {
/*  406 */     chilkatJNI.CkMailMan_get_OAuth2AccessToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oAuth2AccessToken() {
/*  410 */     return chilkatJNI.CkMailMan_oAuth2AccessToken(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OAuth2AccessToken(String var1) {
/*  414 */     chilkatJNI.CkMailMan_put_OAuth2AccessToken(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_OpaqueSigning() {
/*  418 */     return chilkatJNI.CkMailMan_get_OpaqueSigning(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OpaqueSigning(boolean var1) {
/*  422 */     chilkatJNI.CkMailMan_put_OpaqueSigning(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_P7mEncryptAttachFilename(CkString var1) {
/*  426 */     chilkatJNI.CkMailMan_get_P7mEncryptAttachFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String p7mEncryptAttachFilename() {
/*  430 */     return chilkatJNI.CkMailMan_p7mEncryptAttachFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_P7mEncryptAttachFilename(String var1) {
/*  434 */     chilkatJNI.CkMailMan_put_P7mEncryptAttachFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_P7mSigAttachFilename(CkString var1) {
/*  438 */     chilkatJNI.CkMailMan_get_P7mSigAttachFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String p7mSigAttachFilename() {
/*  442 */     return chilkatJNI.CkMailMan_p7mSigAttachFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_P7mSigAttachFilename(String var1) {
/*  446 */     chilkatJNI.CkMailMan_put_P7mSigAttachFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_P7sSigAttachFilename(CkString var1) {
/*  450 */     chilkatJNI.CkMailMan_get_P7sSigAttachFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String p7sSigAttachFilename() {
/*  454 */     return chilkatJNI.CkMailMan_p7sSigAttachFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_P7sSigAttachFilename(String var1) {
/*  458 */     chilkatJNI.CkMailMan_put_P7sSigAttachFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_PercentDoneScale() {
/*  462 */     return chilkatJNI.CkMailMan_get_PercentDoneScale(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PercentDoneScale(int var1) {
/*  466 */     chilkatJNI.CkMailMan_put_PercentDoneScale(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Pop3SessionId() {
/*  470 */     return chilkatJNI.CkMailMan_get_Pop3SessionId(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_Pop3SessionLog(CkString var1) {
/*  474 */     chilkatJNI.CkMailMan_get_Pop3SessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String pop3SessionLog() {
/*  478 */     return chilkatJNI.CkMailMan_pop3SessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_Pop3SPA() {
/*  482 */     return chilkatJNI.CkMailMan_get_Pop3SPA(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Pop3SPA(boolean var1) {
/*  486 */     chilkatJNI.CkMailMan_put_Pop3SPA(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_Pop3SslServerCertVerified() {
/*  490 */     return chilkatJNI.CkMailMan_get_Pop3SslServerCertVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_Pop3Stls() {
/*  494 */     return chilkatJNI.CkMailMan_get_Pop3Stls(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Pop3Stls(boolean var1) {
/*  498 */     chilkatJNI.CkMailMan_put_Pop3Stls(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PopPassword(CkString var1) {
/*  502 */     chilkatJNI.CkMailMan_get_PopPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String popPassword() {
/*  506 */     return chilkatJNI.CkMailMan_popPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PopPassword(String var1) {
/*  510 */     chilkatJNI.CkMailMan_put_PopPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PopPasswordBase64(CkString var1) {
/*  514 */     chilkatJNI.CkMailMan_get_PopPasswordBase64(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String popPasswordBase64() {
/*  518 */     return chilkatJNI.CkMailMan_popPasswordBase64(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PopPasswordBase64(String var1) {
/*  522 */     chilkatJNI.CkMailMan_put_PopPasswordBase64(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PopSsl() {
/*  526 */     return chilkatJNI.CkMailMan_get_PopSsl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PopSsl(boolean var1) {
/*  530 */     chilkatJNI.CkMailMan_put_PopSsl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PopUsername(CkString var1) {
/*  534 */     chilkatJNI.CkMailMan_get_PopUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String popUsername() {
/*  538 */     return chilkatJNI.CkMailMan_popUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PopUsername(String var1) {
/*  542 */     chilkatJNI.CkMailMan_put_PopUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PreferIpv6() {
/*  546 */     return chilkatJNI.CkMailMan_get_PreferIpv6(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferIpv6(boolean var1) {
/*  550 */     chilkatJNI.CkMailMan_put_PreferIpv6(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_ReadTimeout() {
/*  554 */     return chilkatJNI.CkMailMan_get_ReadTimeout(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReadTimeout(int var1) {
/*  558 */     chilkatJNI.CkMailMan_put_ReadTimeout(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_RequireSslCertVerify() {
/*  562 */     return chilkatJNI.CkMailMan_get_RequireSslCertVerify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_RequireSslCertVerify(boolean var1) {
/*  566 */     chilkatJNI.CkMailMan_put_RequireSslCertVerify(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ResetDateOnLoad() {
/*  570 */     return chilkatJNI.CkMailMan_get_ResetDateOnLoad(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ResetDateOnLoad(boolean var1) {
/*  574 */     chilkatJNI.CkMailMan_put_ResetDateOnLoad(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SendBufferSize() {
/*  578 */     return chilkatJNI.CkMailMan_get_SendBufferSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendBufferSize(int var1) {
/*  582 */     chilkatJNI.CkMailMan_put_SendBufferSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SendIndividual() {
/*  586 */     return chilkatJNI.CkMailMan_get_SendIndividual(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendIndividual(boolean var1) {
/*  590 */     chilkatJNI.CkMailMan_put_SendIndividual(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SizeLimit() {
/*  594 */     return chilkatJNI.CkMailMan_get_SizeLimit(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SizeLimit(int var1) {
/*  598 */     chilkatJNI.CkMailMan_put_SizeLimit(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SmtpAuthMethod(CkString var1) {
/*  602 */     chilkatJNI.CkMailMan_get_SmtpAuthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpAuthMethod() {
/*  606 */     return chilkatJNI.CkMailMan_smtpAuthMethod(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpAuthMethod(String var1) {
/*  610 */     chilkatJNI.CkMailMan_put_SmtpAuthMethod(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SmtpFailReason(CkString var1) {
/*  614 */     chilkatJNI.CkMailMan_get_SmtpFailReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpFailReason() {
/*  618 */     return chilkatJNI.CkMailMan_smtpFailReason(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SmtpHost(CkString var1) {
/*  622 */     chilkatJNI.CkMailMan_get_SmtpHost(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpHost() {
/*  626 */     return chilkatJNI.CkMailMan_smtpHost(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpHost(String var1) {
/*  630 */     chilkatJNI.CkMailMan_put_SmtpHost(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SmtpLoginDomain(CkString var1) {
/*  634 */     chilkatJNI.CkMailMan_get_SmtpLoginDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpLoginDomain() {
/*  638 */     return chilkatJNI.CkMailMan_smtpLoginDomain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpLoginDomain(String var1) {
/*  642 */     chilkatJNI.CkMailMan_put_SmtpLoginDomain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SmtpPassword(CkString var1) {
/*  646 */     chilkatJNI.CkMailMan_get_SmtpPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpPassword() {
/*  650 */     return chilkatJNI.CkMailMan_smtpPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpPassword(String var1) {
/*  654 */     chilkatJNI.CkMailMan_put_SmtpPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SmtpPipelining() {
/*  658 */     return chilkatJNI.CkMailMan_get_SmtpPipelining(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpPipelining(boolean var1) {
/*  662 */     chilkatJNI.CkMailMan_put_SmtpPipelining(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SmtpPort() {
/*  666 */     return chilkatJNI.CkMailMan_get_SmtpPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpPort(int var1) {
/*  670 */     chilkatJNI.CkMailMan_put_SmtpPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SmtpSessionLog(CkString var1) {
/*  674 */     chilkatJNI.CkMailMan_get_SmtpSessionLog(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpSessionLog() {
/*  678 */     return chilkatJNI.CkMailMan_smtpSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_SmtpSsl() {
/*  682 */     return chilkatJNI.CkMailMan_get_SmtpSsl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpSsl(boolean var1) {
/*  686 */     chilkatJNI.CkMailMan_put_SmtpSsl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SmtpSslServerCertVerified() {
/*  690 */     return chilkatJNI.CkMailMan_get_SmtpSslServerCertVerified(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SmtpUsername(CkString var1) {
/*  694 */     chilkatJNI.CkMailMan_get_SmtpUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String smtpUsername() {
/*  698 */     return chilkatJNI.CkMailMan_smtpUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SmtpUsername(String var1) {
/*  702 */     chilkatJNI.CkMailMan_put_SmtpUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksHostname(CkString var1) {
/*  706 */     chilkatJNI.CkMailMan_get_SocksHostname(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksHostname() {
/*  710 */     return chilkatJNI.CkMailMan_socksHostname(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksHostname(String var1) {
/*  714 */     chilkatJNI.CkMailMan_put_SocksHostname(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksPassword(CkString var1) {
/*  718 */     chilkatJNI.CkMailMan_get_SocksPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksPassword() {
/*  722 */     return chilkatJNI.CkMailMan_socksPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPassword(String var1) {
/*  726 */     chilkatJNI.CkMailMan_put_SocksPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksPort() {
/*  730 */     return chilkatJNI.CkMailMan_get_SocksPort(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksPort(int var1) {
/*  734 */     chilkatJNI.CkMailMan_put_SocksPort(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SocksUsername(CkString var1) {
/*  738 */     chilkatJNI.CkMailMan_get_SocksUsername(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String socksUsername() {
/*  742 */     return chilkatJNI.CkMailMan_socksUsername(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksUsername(String var1) {
/*  746 */     chilkatJNI.CkMailMan_put_SocksUsername(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SocksVersion() {
/*  750 */     return chilkatJNI.CkMailMan_get_SocksVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SocksVersion(int var1) {
/*  754 */     chilkatJNI.CkMailMan_put_SocksVersion(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoRcvBuf() {
/*  758 */     return chilkatJNI.CkMailMan_get_SoRcvBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoRcvBuf(int var1) {
/*  762 */     chilkatJNI.CkMailMan_put_SoRcvBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_SoSndBuf() {
/*  766 */     return chilkatJNI.CkMailMan_get_SoSndBuf(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SoSndBuf(int var1) {
/*  770 */     chilkatJNI.CkMailMan_put_SoSndBuf(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslAllowedCiphers(CkString var1) {
/*  774 */     chilkatJNI.CkMailMan_get_SslAllowedCiphers(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslAllowedCiphers() {
/*  778 */     return chilkatJNI.CkMailMan_sslAllowedCiphers(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslAllowedCiphers(String var1) {
/*  782 */     chilkatJNI.CkMailMan_put_SslAllowedCiphers(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SslProtocol(CkString var1) {
/*  786 */     chilkatJNI.CkMailMan_get_SslProtocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sslProtocol() {
/*  790 */     return chilkatJNI.CkMailMan_sslProtocol(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SslProtocol(String var1) {
/*  794 */     chilkatJNI.CkMailMan_put_SslProtocol(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_StartTLS() {
/*  798 */     return chilkatJNI.CkMailMan_get_StartTLS(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_StartTLS(boolean var1) {
/*  802 */     chilkatJNI.CkMailMan_put_StartTLS(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_StartTLSifPossible() {
/*  806 */     return chilkatJNI.CkMailMan_get_StartTLSifPossible(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_StartTLSifPossible(boolean var1) {
/*  810 */     chilkatJNI.CkMailMan_put_StartTLSifPossible(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsCipherSuite(CkString var1) {
/*  814 */     chilkatJNI.CkMailMan_get_TlsCipherSuite(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsCipherSuite() {
/*  818 */     return chilkatJNI.CkMailMan_tlsCipherSuite(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_TlsPinSet(CkString var1) {
/*  822 */     chilkatJNI.CkMailMan_get_TlsPinSet(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsPinSet() {
/*  826 */     return chilkatJNI.CkMailMan_tlsPinSet(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_TlsPinSet(String var1) {
/*  830 */     chilkatJNI.CkMailMan_put_TlsPinSet(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_TlsVersion(CkString var1) {
/*  834 */     chilkatJNI.CkMailMan_get_TlsVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String tlsVersion() {
/*  838 */     return chilkatJNI.CkMailMan_tlsVersion(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_UseApop() {
/*  842 */     return chilkatJNI.CkMailMan_get_UseApop(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UseApop(boolean var1) {
/*  846 */     chilkatJNI.CkMailMan_put_UseApop(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  850 */     return chilkatJNI.CkMailMan_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  854 */     chilkatJNI.CkMailMan_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  858 */     chilkatJNI.CkMailMan_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  862 */     return chilkatJNI.CkMailMan_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceData(CkByteData var1, String var2) {
/*  866 */     return chilkatJNI.CkMailMan_AddPfxSourceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceFile(String var1, String var2) {
/*  870 */     return chilkatJNI.CkMailMan_AddPfxSourceFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public int CheckMail() {
/*  874 */     return chilkatJNI.CkMailMan_CheckMail(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask CheckMailAsync() {
/*  878 */     long var1 = chilkatJNI.CkMailMan_CheckMailAsync(this.swigCPtr, this);
/*  879 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public void ClearBadEmailAddresses() {
/*  883 */     chilkatJNI.CkMailMan_ClearBadEmailAddresses(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearPop3SessionLog() {
/*  887 */     chilkatJNI.CkMailMan_ClearPop3SessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearSmtpSessionLog() {
/*  891 */     chilkatJNI.CkMailMan_ClearSmtpSessionLog(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean CloseSmtpConnection() {
/*  895 */     return chilkatJNI.CkMailMan_CloseSmtpConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask CloseSmtpConnectionAsync() {
/*  899 */     long var1 = chilkatJNI.CkMailMan_CloseSmtpConnectionAsync(this.swigCPtr, this);
/*  900 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle CopyMail() {
/*  904 */     long var1 = chilkatJNI.CkMailMan_CopyMail(this.swigCPtr, this);
/*  905 */     return var1 == 0L ? null : new CkEmailBundle(var1, true);
/*      */   }
/*      */   
/*      */   public CkTask CopyMailAsync() {
/*  909 */     long var1 = chilkatJNI.CkMailMan_CopyMailAsync(this.swigCPtr, this);
/*  910 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteBundle(CkEmailBundle var1) {
/*  914 */     return chilkatJNI.CkMailMan_DeleteBundle(this.swigCPtr, this, CkEmailBundle.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteBundleAsync(CkEmailBundle var1) {
/*  918 */     long var2 = chilkatJNI.CkMailMan_DeleteBundleAsync(this.swigCPtr, this, CkEmailBundle.getCPtr(var1), var1);
/*  919 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteByMsgnum(int var1) {
/*  923 */     return chilkatJNI.CkMailMan_DeleteByMsgnum(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteByMsgnumAsync(int var1) {
/*  927 */     long var2 = chilkatJNI.CkMailMan_DeleteByMsgnumAsync(this.swigCPtr, this, var1);
/*  928 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteByUidl(String var1) {
/*  932 */     return chilkatJNI.CkMailMan_DeleteByUidl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteByUidlAsync(String var1) {
/*  936 */     long var2 = chilkatJNI.CkMailMan_DeleteByUidlAsync(this.swigCPtr, this, var1);
/*  937 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteEmail(CkEmail var1) {
/*  941 */     return chilkatJNI.CkMailMan_DeleteEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteEmailAsync(CkEmail var1) {
/*  945 */     long var2 = chilkatJNI.CkMailMan_DeleteEmailAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*  946 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DeleteMultiple(CkStringArray var1) {
/*  950 */     return chilkatJNI.CkMailMan_DeleteMultiple(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask DeleteMultipleAsync(CkStringArray var1) {
/*  954 */     long var2 = chilkatJNI.CkMailMan_DeleteMultipleAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/*  955 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchByMsgnum(int var1) {
/*  959 */     long var2 = chilkatJNI.CkMailMan_FetchByMsgnum(this.swigCPtr, this, var1);
/*  960 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchByMsgnumAsync(int var1) {
/*  964 */     long var2 = chilkatJNI.CkMailMan_FetchByMsgnumAsync(this.swigCPtr, this, var1);
/*  965 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchEmail(String var1) {
/*  969 */     long var2 = chilkatJNI.CkMailMan_FetchEmail(this.swigCPtr, this, var1);
/*  970 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchEmailAsync(String var1) {
/*  974 */     long var2 = chilkatJNI.CkMailMan_FetchEmailAsync(this.swigCPtr, this, var1);
/*  975 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean FetchMime(String var1, CkByteData var2) {
/*  979 */     return chilkatJNI.CkMailMan_FetchMime(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchMimeAsync(String var1) {
/*  983 */     long var2 = chilkatJNI.CkMailMan_FetchMimeAsync(this.swigCPtr, this, var1);
/*  984 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean FetchMimeBd(String var1, CkBinData var2) {
/*  988 */     return chilkatJNI.CkMailMan_FetchMimeBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchMimeBdAsync(String var1, CkBinData var2) {
/*  992 */     long var3 = chilkatJNI.CkMailMan_FetchMimeBdAsync(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*  993 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean FetchMimeByMsgnum(int var1, CkByteData var2) {
/*  997 */     return chilkatJNI.CkMailMan_FetchMimeByMsgnum(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask FetchMimeByMsgnumAsync(int var1) {
/* 1001 */     long var2 = chilkatJNI.CkMailMan_FetchMimeByMsgnumAsync(this.swigCPtr, this, var1);
/* 1002 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchMultiple(CkStringArray var1) {
/* 1006 */     long var2 = chilkatJNI.CkMailMan_FetchMultiple(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1007 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchMultipleAsync(CkStringArray var1) {
/* 1011 */     long var2 = chilkatJNI.CkMailMan_FetchMultipleAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1012 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle FetchMultipleHeaders(CkStringArray var1, int var2) {
/* 1016 */     long var3 = chilkatJNI.CkMailMan_FetchMultipleHeaders(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1, var2);
/* 1017 */     return var3 == 0L ? null : new CkEmailBundle(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchMultipleHeadersAsync(CkStringArray var1, int var2) {
/* 1021 */     long var3 = chilkatJNI.CkMailMan_FetchMultipleHeadersAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1, var2);
/* 1022 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkStringArray FetchMultipleMime(CkStringArray var1) {
/* 1026 */     long var2 = chilkatJNI.CkMailMan_FetchMultipleMime(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1027 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchMultipleMimeAsync(CkStringArray var1) {
/* 1031 */     long var2 = chilkatJNI.CkMailMan_FetchMultipleMimeAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1032 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchSingleHeader(int var1, int var2) {
/* 1036 */     long var3 = chilkatJNI.CkMailMan_FetchSingleHeader(this.swigCPtr, this, var1, var2);
/* 1037 */     return var3 == 0L ? null : new CkEmail(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleHeaderAsync(int var1, int var2) {
/* 1041 */     long var3 = chilkatJNI.CkMailMan_FetchSingleHeaderAsync(this.swigCPtr, this, var1, var2);
/* 1042 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmail FetchSingleHeaderByUidl(int var1, String var2) {
/* 1046 */     long var3 = chilkatJNI.CkMailMan_FetchSingleHeaderByUidl(this.swigCPtr, this, var1, var2);
/* 1047 */     return var3 == 0L ? null : new CkEmail(var3, true);
/*      */   }
/*      */   
/*      */   public CkTask FetchSingleHeaderByUidlAsync(int var1, String var2) {
/* 1051 */     long var3 = chilkatJNI.CkMailMan_FetchSingleHeaderByUidlAsync(this.swigCPtr, this, var1, var2);
/* 1052 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle GetAllHeaders(int var1) {
/* 1056 */     long var2 = chilkatJNI.CkMailMan_GetAllHeaders(this.swigCPtr, this, var1);
/* 1057 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetAllHeadersAsync(int var1) {
/* 1061 */     long var2 = chilkatJNI.CkMailMan_GetAllHeadersAsync(this.swigCPtr, this, var1);
/* 1062 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkStringArray GetBadEmailAddrs() {
/* 1066 */     long var1 = chilkatJNI.CkMailMan_GetBadEmailAddrs(this.swigCPtr, this);
/* 1067 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*      */   }
/*      */   
/*      */   public CkEmail GetFullEmail(CkEmail var1) {
/* 1071 */     long var2 = chilkatJNI.CkMailMan_GetFullEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/* 1072 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask GetFullEmailAsync(CkEmail var1) {
/* 1076 */     long var2 = chilkatJNI.CkMailMan_GetFullEmailAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/* 1077 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle GetHeaders(int var1, int var2, int var3) {
/* 1081 */     long var4 = chilkatJNI.CkMailMan_GetHeaders(this.swigCPtr, this, var1, var2, var3);
/* 1082 */     return var4 == 0L ? null : new CkEmailBundle(var4, true);
/*      */   }
/*      */   
/*      */   public CkTask GetHeadersAsync(int var1, int var2, int var3) {
/* 1086 */     long var4 = chilkatJNI.CkMailMan_GetHeadersAsync(this.swigCPtr, this, var1, var2, var3);
/* 1087 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public int GetMailboxCount() {
/* 1091 */     return chilkatJNI.CkMailMan_GetMailboxCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask GetMailboxCountAsync() {
/* 1095 */     long var1 = chilkatJNI.CkMailMan_GetMailboxCountAsync(this.swigCPtr, this);
/* 1096 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetMailboxInfoXml(CkString var1) {
/* 1100 */     return chilkatJNI.CkMailMan_GetMailboxInfoXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getMailboxInfoXml() {
/* 1104 */     return chilkatJNI.CkMailMan_getMailboxInfoXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String mailboxInfoXml() {
/* 1108 */     return chilkatJNI.CkMailMan_mailboxInfoXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask GetMailboxInfoXmlAsync() {
/* 1112 */     long var1 = chilkatJNI.CkMailMan_GetMailboxInfoXmlAsync(this.swigCPtr, this);
/* 1113 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public long GetMailboxSize() {
/* 1117 */     return chilkatJNI.CkMailMan_GetMailboxSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask GetMailboxSizeAsync() {
/* 1121 */     long var1 = chilkatJNI.CkMailMan_GetMailboxSizeAsync(this.swigCPtr, this);
/* 1122 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public CkCert GetPop3SslServerCert() {
/* 1126 */     long var1 = chilkatJNI.CkMailMan_GetPop3SslServerCert(this.swigCPtr, this);
/* 1127 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public CkStringArray GetSentToEmailAddrs() {
/* 1131 */     long var1 = chilkatJNI.CkMailMan_GetSentToEmailAddrs(this.swigCPtr, this);
/* 1132 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*      */   }
/*      */   
/*      */   public int GetSizeByUidl(String var1) {
/* 1136 */     return chilkatJNI.CkMailMan_GetSizeByUidl(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask GetSizeByUidlAsync(String var1) {
/* 1140 */     long var2 = chilkatJNI.CkMailMan_GetSizeByUidlAsync(this.swigCPtr, this, var1);
/* 1141 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public CkCert GetSmtpSslServerCert() {
/* 1145 */     long var1 = chilkatJNI.CkMailMan_GetSmtpSslServerCert(this.swigCPtr, this);
/* 1146 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public CkStringArray GetUidls() {
/* 1150 */     long var1 = chilkatJNI.CkMailMan_GetUidls(this.swigCPtr, this);
/* 1151 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*      */   }
/*      */   
/*      */   public CkTask GetUidlsAsync() {
/* 1155 */     long var1 = chilkatJNI.CkMailMan_GetUidlsAsync(this.swigCPtr, this);
/* 1156 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean IsSmtpDsnCapable() {
/* 1160 */     return chilkatJNI.CkMailMan_IsSmtpDsnCapable(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask IsSmtpDsnCapableAsync() {
/* 1164 */     long var1 = chilkatJNI.CkMailMan_IsSmtpDsnCapableAsync(this.swigCPtr, this);
/* 1165 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/* 1169 */     return chilkatJNI.CkMailMan_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkJsonObject LastJsonData() {
/* 1173 */     long var1 = chilkatJNI.CkMailMan_LastJsonData(this.swigCPtr, this);
/* 1174 */     return var1 == 0L ? null : new CkJsonObject(var1, true);
/*      */   }
/*      */   
/*      */   public CkEmail LoadEml(String var1) {
/* 1178 */     long var2 = chilkatJNI.CkMailMan_LoadEml(this.swigCPtr, this, var1);
/* 1179 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle LoadMbx(String var1) {
/* 1183 */     long var2 = chilkatJNI.CkMailMan_LoadMbx(this.swigCPtr, this, var1);
/* 1184 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail LoadMime(String var1) {
/* 1188 */     long var2 = chilkatJNI.CkMailMan_LoadMime(this.swigCPtr, this, var1);
/* 1189 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail LoadQueuedEmail(String var1) {
/* 1193 */     long var2 = chilkatJNI.CkMailMan_LoadQueuedEmail(this.swigCPtr, this, var1);
/* 1194 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail LoadXmlEmail(String var1) {
/* 1198 */     long var2 = chilkatJNI.CkMailMan_LoadXmlEmail(this.swigCPtr, this, var1);
/* 1199 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmail LoadXmlEmailString(String var1) {
/* 1203 */     long var2 = chilkatJNI.CkMailMan_LoadXmlEmailString(this.swigCPtr, this, var1);
/* 1204 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle LoadXmlFile(String var1) {
/* 1208 */     long var2 = chilkatJNI.CkMailMan_LoadXmlFile(this.swigCPtr, this, var1);
/* 1209 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle LoadXmlString(String var1) {
/* 1213 */     long var2 = chilkatJNI.CkMailMan_LoadXmlString(this.swigCPtr, this, var1);
/* 1214 */     return var2 == 0L ? null : new CkEmailBundle(var2, true);
/*      */   }
/*      */   
/*      */   public boolean MxLookup(String var1, CkString var2) {
/* 1218 */     return chilkatJNI.CkMailMan_MxLookup(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String mxLookup(String var1) {
/* 1222 */     return chilkatJNI.CkMailMan_mxLookup(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkStringArray MxLookupAll(String var1) {
/* 1226 */     long var2 = chilkatJNI.CkMailMan_MxLookupAll(this.swigCPtr, this, var1);
/* 1227 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*      */   }
/*      */   
/*      */   public boolean OpenSmtpConnection() {
/* 1231 */     return chilkatJNI.CkMailMan_OpenSmtpConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask OpenSmtpConnectionAsync() {
/* 1235 */     long var1 = chilkatJNI.CkMailMan_OpenSmtpConnectionAsync(this.swigCPtr, this);
/* 1236 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3Authenticate() {
/* 1240 */     return chilkatJNI.CkMailMan_Pop3Authenticate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3AuthenticateAsync() {
/* 1244 */     long var1 = chilkatJNI.CkMailMan_Pop3AuthenticateAsync(this.swigCPtr, this);
/* 1245 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3BeginSession() {
/* 1249 */     return chilkatJNI.CkMailMan_Pop3BeginSession(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3BeginSessionAsync() {
/* 1253 */     long var1 = chilkatJNI.CkMailMan_Pop3BeginSessionAsync(this.swigCPtr, this);
/* 1254 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3Connect() {
/* 1258 */     return chilkatJNI.CkMailMan_Pop3Connect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3ConnectAsync() {
/* 1262 */     long var1 = chilkatJNI.CkMailMan_Pop3ConnectAsync(this.swigCPtr, this);
/* 1263 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3EndSession() {
/* 1267 */     return chilkatJNI.CkMailMan_Pop3EndSession(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3EndSessionAsync() {
/* 1271 */     long var1 = chilkatJNI.CkMailMan_Pop3EndSessionAsync(this.swigCPtr, this);
/* 1272 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3EndSessionNoQuit() {
/* 1276 */     return chilkatJNI.CkMailMan_Pop3EndSessionNoQuit(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3EndSessionNoQuitAsync() {
/* 1280 */     long var1 = chilkatJNI.CkMailMan_Pop3EndSessionNoQuitAsync(this.swigCPtr, this);
/* 1281 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3Noop() {
/* 1285 */     return chilkatJNI.CkMailMan_Pop3Noop(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3NoopAsync() {
/* 1289 */     long var1 = chilkatJNI.CkMailMan_Pop3NoopAsync(this.swigCPtr, this);
/* 1290 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3Reset() {
/* 1294 */     return chilkatJNI.CkMailMan_Pop3Reset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask Pop3ResetAsync() {
/* 1298 */     long var1 = chilkatJNI.CkMailMan_Pop3ResetAsync(this.swigCPtr, this);
/* 1299 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean Pop3SendRawCommand(String var1, String var2, CkString var3) {
/* 1303 */     return chilkatJNI.CkMailMan_Pop3SendRawCommand(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String pop3SendRawCommand(String var1, String var2) {
/* 1307 */     return chilkatJNI.CkMailMan_pop3SendRawCommand(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask Pop3SendRawCommandAsync(String var1, String var2) {
/* 1311 */     long var3 = chilkatJNI.CkMailMan_Pop3SendRawCommandAsync(this.swigCPtr, this, var1, var2);
/* 1312 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean QuickSend(String var1, String var2, String var3, String var4, String var5) {
/* 1316 */     return chilkatJNI.CkMailMan_QuickSend(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*      */   }
/*      */   
/*      */   public CkTask QuickSendAsync(String var1, String var2, String var3, String var4, String var5) {
/* 1320 */     long var6 = chilkatJNI.CkMailMan_QuickSendAsync(this.swigCPtr, this, var1, var2, var3, var4, var5);
/* 1321 */     return var6 == 0L ? null : new CkTask(var6, true);
/*      */   }
/*      */   
/*      */   public boolean RenderToMime(CkEmail var1, CkString var2) {
/* 1325 */     return chilkatJNI.CkMailMan_RenderToMime(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String renderToMime(CkEmail var1) {
/* 1329 */     return chilkatJNI.CkMailMan_renderToMime(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean RenderToMimeBd(CkEmail var1, CkBinData var2) {
/* 1333 */     return chilkatJNI.CkMailMan_RenderToMimeBd(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean RenderToMimeBytes(CkEmail var1, CkByteData var2) {
/* 1337 */     return chilkatJNI.CkMailMan_RenderToMimeBytes(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean RenderToMimeSb(CkEmail var1, CkStringBuilder var2) {
/* 1341 */     return chilkatJNI.CkMailMan_RenderToMimeSb(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkStringBuilder.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1345 */     return chilkatJNI.CkMailMan_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SendBundle(CkEmailBundle var1) {
/* 1349 */     return chilkatJNI.CkMailMan_SendBundle(this.swigCPtr, this, CkEmailBundle.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask SendBundleAsync(CkEmailBundle var1) {
/* 1353 */     long var2 = chilkatJNI.CkMailMan_SendBundleAsync(this.swigCPtr, this, CkEmailBundle.getCPtr(var1), var1);
/* 1354 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendEmail(CkEmail var1) {
/* 1358 */     return chilkatJNI.CkMailMan_SendEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask SendEmailAsync(CkEmail var1) {
/* 1362 */     long var2 = chilkatJNI.CkMailMan_SendEmailAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/* 1363 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean SendMime(String var1, String var2, String var3) {
/* 1367 */     return chilkatJNI.CkMailMan_SendMime(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SendMimeAsync(String var1, String var2, String var3) {
/* 1371 */     long var4 = chilkatJNI.CkMailMan_SendMimeAsync(this.swigCPtr, this, var1, var2, var3);
/* 1372 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SendMimeBd(String var1, String var2, CkBinData var3) {
/* 1376 */     return chilkatJNI.CkMailMan_SendMimeBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask SendMimeBdAsync(String var1, String var2, CkBinData var3) {
/* 1380 */     long var4 = chilkatJNI.CkMailMan_SendMimeBdAsync(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/* 1381 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SendMimeBytes(String var1, String var2, CkByteData var3) {
/* 1385 */     return chilkatJNI.CkMailMan_SendMimeBytes(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkTask SendMimeBytesAsync(String var1, String var2, CkByteData var3) {
/* 1389 */     long var4 = chilkatJNI.CkMailMan_SendMimeBytesAsync(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/* 1390 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SendMimeBytesQ(String var1, String var2, CkByteData var3) {
/* 1394 */     return chilkatJNI.CkMailMan_SendMimeBytesQ(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean SendMimeQ(String var1, String var2, String var3) {
/* 1398 */     return chilkatJNI.CkMailMan_SendMimeQ(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean SendMimeToList(String var1, String var2, String var3) {
/* 1402 */     return chilkatJNI.CkMailMan_SendMimeToList(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SendMimeToListAsync(String var1, String var2, String var3) {
/* 1406 */     long var4 = chilkatJNI.CkMailMan_SendMimeToListAsync(this.swigCPtr, this, var1, var2, var3);
/* 1407 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SendQ(CkEmail var1) {
/* 1411 */     return chilkatJNI.CkMailMan_SendQ(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SendQ2(CkEmail var1, String var2) {
/* 1415 */     return chilkatJNI.CkMailMan_SendQ2(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SendToDistributionList(CkEmail var1, CkStringArray var2) {
/* 1419 */     return chilkatJNI.CkMailMan_SendToDistributionList(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkStringArray.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SendToDistributionListAsync(CkEmail var1, CkStringArray var2) {
/* 1423 */     long var3 = chilkatJNI.CkMailMan_SendToDistributionListAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkStringArray.getCPtr(var2), var2);
/* 1424 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SetCSP(CkCsp var1) {
/* 1428 */     return chilkatJNI.CkMailMan_SetCSP(this.swigCPtr, this, CkCsp.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert(CkCert var1) {
/* 1432 */     return chilkatJNI.CkMailMan_SetDecryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert2(CkCert var1, CkPrivateKey var2) {
/* 1436 */     return chilkatJNI.CkMailMan_SetDecryptCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetPassword(String var1, CkSecureString var2) {
/* 1440 */     return chilkatJNI.CkMailMan_SetPassword(this.swigCPtr, this, var1, CkSecureString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCert(CkCert var1) {
/* 1444 */     return chilkatJNI.CkMailMan_SetSslClientCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPem(String var1, String var2) {
/* 1448 */     return chilkatJNI.CkMailMan_SetSslClientCertPem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSslClientCertPfx(String var1, String var2) {
/* 1452 */     return chilkatJNI.CkMailMan_SetSslClientCertPfx(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SmtpAuthenticate() {
/* 1456 */     return chilkatJNI.CkMailMan_SmtpAuthenticate(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SmtpAuthenticateAsync() {
/* 1460 */     long var1 = chilkatJNI.CkMailMan_SmtpAuthenticateAsync(this.swigCPtr, this);
/* 1461 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SmtpConnect() {
/* 1465 */     return chilkatJNI.CkMailMan_SmtpConnect(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SmtpConnectAsync() {
/* 1469 */     long var1 = chilkatJNI.CkMailMan_SmtpConnectAsync(this.swigCPtr, this);
/* 1470 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SmtpNoop() {
/* 1474 */     return chilkatJNI.CkMailMan_SmtpNoop(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SmtpNoopAsync() {
/* 1478 */     long var1 = chilkatJNI.CkMailMan_SmtpNoopAsync(this.swigCPtr, this);
/* 1479 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SmtpReset() {
/* 1483 */     return chilkatJNI.CkMailMan_SmtpReset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SmtpResetAsync() {
/* 1487 */     long var1 = chilkatJNI.CkMailMan_SmtpResetAsync(this.swigCPtr, this);
/* 1488 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SmtpSendRawCommand(String var1, String var2, boolean var3, CkString var4) {
/* 1492 */     return chilkatJNI.CkMailMan_SmtpSendRawCommand(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String smtpSendRawCommand(String var1, String var2, boolean var3) {
/* 1496 */     return chilkatJNI.CkMailMan_smtpSendRawCommand(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public CkTask SmtpSendRawCommandAsync(String var1, String var2, boolean var3) {
/* 1500 */     long var4 = chilkatJNI.CkMailMan_SmtpSendRawCommandAsync(this.swigCPtr, this, var1, var2, var3);
/* 1501 */     return var4 == 0L ? null : new CkTask(var4, true);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePk(String var1, CkSshKey var2) {
/* 1505 */     return chilkatJNI.CkMailMan_SshAuthenticatePk(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePkAsync(String var1, CkSshKey var2) {
/* 1509 */     long var3 = chilkatJNI.CkMailMan_SshAuthenticatePkAsync(this.swigCPtr, this, var1, CkSshKey.getCPtr(var2), var2);
/* 1510 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshAuthenticatePw(String var1, String var2) {
/* 1514 */     return chilkatJNI.CkMailMan_SshAuthenticatePw(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshAuthenticatePwAsync(String var1, String var2) {
/* 1518 */     long var3 = chilkatJNI.CkMailMan_SshAuthenticatePwAsync(this.swigCPtr, this, var1, var2);
/* 1519 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean SshCloseTunnel() {
/* 1523 */     return chilkatJNI.CkMailMan_SshCloseTunnel(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask SshCloseTunnelAsync() {
/* 1527 */     long var1 = chilkatJNI.CkMailMan_SshCloseTunnelAsync(this.swigCPtr, this);
/* 1528 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean SshOpenTunnel(String var1, int var2) {
/* 1532 */     return chilkatJNI.CkMailMan_SshOpenTunnel(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask SshOpenTunnelAsync(String var1, int var2) {
/* 1536 */     long var3 = chilkatJNI.CkMailMan_SshOpenTunnelAsync(this.swigCPtr, this, var1, var2);
/* 1537 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public CkEmailBundle TransferMail() {
/* 1541 */     long var1 = chilkatJNI.CkMailMan_TransferMail(this.swigCPtr, this);
/* 1542 */     return var1 == 0L ? null : new CkEmailBundle(var1, true);
/*      */   }
/*      */   
/*      */   public CkTask TransferMailAsync() {
/* 1546 */     long var1 = chilkatJNI.CkMailMan_TransferMailAsync(this.swigCPtr, this);
/* 1547 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public CkStringArray TransferMultipleMime(CkStringArray var1) {
/* 1551 */     long var2 = chilkatJNI.CkMailMan_TransferMultipleMime(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1552 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*      */   }
/*      */   
/*      */   public CkTask TransferMultipleMimeAsync(CkStringArray var1) {
/* 1556 */     long var2 = chilkatJNI.CkMailMan_TransferMultipleMimeAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/* 1557 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1561 */     return chilkatJNI.CkMailMan_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 1565 */     return chilkatJNI.CkMailMan_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean UseSsh(CkSsh var1) {
/* 1569 */     return chilkatJNI.CkMailMan_UseSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean UseSshTunnel(CkSocket var1) {
/* 1573 */     return chilkatJNI.CkMailMan_UseSshTunnel(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean VerifyPopConnection() {
/* 1577 */     return chilkatJNI.CkMailMan_VerifyPopConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask VerifyPopConnectionAsync() {
/* 1581 */     long var1 = chilkatJNI.CkMailMan_VerifyPopConnectionAsync(this.swigCPtr, this);
/* 1582 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean VerifyPopLogin() {
/* 1586 */     return chilkatJNI.CkMailMan_VerifyPopLogin(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask VerifyPopLoginAsync() {
/* 1590 */     long var1 = chilkatJNI.CkMailMan_VerifyPopLoginAsync(this.swigCPtr, this);
/* 1591 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean VerifyRecips(CkEmail var1, CkStringArray var2) {
/* 1595 */     return chilkatJNI.CkMailMan_VerifyRecips(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkStringArray.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask VerifyRecipsAsync(CkEmail var1, CkStringArray var2) {
/* 1599 */     long var3 = chilkatJNI.CkMailMan_VerifyRecipsAsync(this.swigCPtr, this, CkEmail.getCPtr(var1), var1, CkStringArray.getCPtr(var2), var2);
/* 1600 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean VerifySmtpConnection() {
/* 1604 */     return chilkatJNI.CkMailMan_VerifySmtpConnection(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask VerifySmtpConnectionAsync() {
/* 1608 */     long var1 = chilkatJNI.CkMailMan_VerifySmtpConnectionAsync(this.swigCPtr, this);
/* 1609 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */   
/*      */   public boolean VerifySmtpLogin() {
/* 1613 */     return chilkatJNI.CkMailMan_VerifySmtpLogin(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkTask VerifySmtpLoginAsync() {
/* 1617 */     long var1 = chilkatJNI.CkMailMan_VerifySmtpLoginAsync(this.swigCPtr, this);
/* 1618 */     return var1 == 0L ? null : new CkTask(var1, true);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMailMan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */