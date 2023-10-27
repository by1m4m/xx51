/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkOAuth2
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkOAuth2(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkOAuth2 var0) {
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
/*  29 */         chilkatJNI.delete_CkOAuth2(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkOAuth2()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkOAuth2(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkOAuth2_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkOAuth2_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkOAuth2_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkOAuth2_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessToken(CkString var1) {
/*  58 */     chilkatJNI.CkOAuth2_get_AccessToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessToken() {
/*  62 */     return chilkatJNI.CkOAuth2_accessToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessToken(String var1) {
/*  66 */     chilkatJNI.CkOAuth2_put_AccessToken(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AccessTokenResponse(CkString var1) {
/*  70 */     chilkatJNI.CkOAuth2_get_AccessTokenResponse(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessTokenResponse() {
/*  74 */     return chilkatJNI.CkOAuth2_accessTokenResponse(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_AppCallbackUrl(CkString var1) {
/*  78 */     chilkatJNI.CkOAuth2_get_AppCallbackUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String appCallbackUrl() {
/*  82 */     return chilkatJNI.CkOAuth2_appCallbackUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AppCallbackUrl(String var1) {
/*  86 */     chilkatJNI.CkOAuth2_put_AppCallbackUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_AuthFlowState() {
/*  90 */     return chilkatJNI.CkOAuth2_get_AuthFlowState(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_AuthorizationEndpoint(CkString var1) {
/*  94 */     chilkatJNI.CkOAuth2_get_AuthorizationEndpoint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String authorizationEndpoint() {
/*  98 */     return chilkatJNI.CkOAuth2_authorizationEndpoint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AuthorizationEndpoint(String var1) {
/* 102 */     chilkatJNI.CkOAuth2_put_AuthorizationEndpoint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientId(CkString var1) {
/* 106 */     chilkatJNI.CkOAuth2_get_ClientId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientId() {
/* 110 */     return chilkatJNI.CkOAuth2_clientId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientId(String var1) {
/* 114 */     chilkatJNI.CkOAuth2_put_ClientId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ClientSecret(CkString var1) {
/* 118 */     chilkatJNI.CkOAuth2_get_ClientSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientSecret() {
/* 122 */     return chilkatJNI.CkOAuth2_clientSecret(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientSecret(String var1) {
/* 126 */     chilkatJNI.CkOAuth2_put_ClientSecret(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_CodeChallenge() {
/* 130 */     return chilkatJNI.CkOAuth2_get_CodeChallenge(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CodeChallenge(boolean var1) {
/* 134 */     chilkatJNI.CkOAuth2_put_CodeChallenge(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CodeChallengeMethod(CkString var1) {
/* 138 */     chilkatJNI.CkOAuth2_get_CodeChallengeMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String codeChallengeMethod() {
/* 142 */     return chilkatJNI.CkOAuth2_codeChallengeMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CodeChallengeMethod(String var1) {
/* 146 */     chilkatJNI.CkOAuth2_put_CodeChallengeMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 150 */     chilkatJNI.CkOAuth2_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 154 */     return chilkatJNI.CkOAuth2_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 158 */     chilkatJNI.CkOAuth2_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_FailureInfo(CkString var1) {
/* 162 */     chilkatJNI.CkOAuth2_get_FailureInfo(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String failureInfo() {
/* 166 */     return chilkatJNI.CkOAuth2_failureInfo(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 170 */     chilkatJNI.CkOAuth2_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 174 */     return chilkatJNI.CkOAuth2_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 178 */     chilkatJNI.CkOAuth2_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 182 */     return chilkatJNI.CkOAuth2_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 186 */     chilkatJNI.CkOAuth2_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 190 */     return chilkatJNI.CkOAuth2_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 194 */     return chilkatJNI.CkOAuth2_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 198 */     chilkatJNI.CkOAuth2_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ListenPort() {
/* 202 */     return chilkatJNI.CkOAuth2_get_ListenPort(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ListenPort(int var1) {
/* 206 */     chilkatJNI.CkOAuth2_put_ListenPort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ListenPortRangeEnd() {
/* 210 */     return chilkatJNI.CkOAuth2_get_ListenPortRangeEnd(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ListenPortRangeEnd(int var1) {
/* 214 */     chilkatJNI.CkOAuth2_put_ListenPortRangeEnd(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LocalHost(CkString var1) {
/* 218 */     chilkatJNI.CkOAuth2_get_LocalHost(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String localHost() {
/* 222 */     return chilkatJNI.CkOAuth2_localHost(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LocalHost(String var1) {
/* 226 */     chilkatJNI.CkOAuth2_put_LocalHost(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_RedirectAllowHtml(CkString var1) {
/* 230 */     chilkatJNI.CkOAuth2_get_RedirectAllowHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String redirectAllowHtml() {
/* 234 */     return chilkatJNI.CkOAuth2_redirectAllowHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RedirectAllowHtml(String var1) {
/* 238 */     chilkatJNI.CkOAuth2_put_RedirectAllowHtml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_RedirectDenyHtml(CkString var1) {
/* 242 */     chilkatJNI.CkOAuth2_get_RedirectDenyHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String redirectDenyHtml() {
/* 246 */     return chilkatJNI.CkOAuth2_redirectDenyHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RedirectDenyHtml(String var1) {
/* 250 */     chilkatJNI.CkOAuth2_put_RedirectDenyHtml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_RefreshToken(CkString var1) {
/* 254 */     chilkatJNI.CkOAuth2_get_RefreshToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String refreshToken() {
/* 258 */     return chilkatJNI.CkOAuth2_refreshToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RefreshToken(String var1) {
/* 262 */     chilkatJNI.CkOAuth2_put_RefreshToken(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Resource(CkString var1) {
/* 266 */     chilkatJNI.CkOAuth2_get_Resource(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String resource() {
/* 270 */     return chilkatJNI.CkOAuth2_resource(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Resource(String var1) {
/* 274 */     chilkatJNI.CkOAuth2_put_Resource(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Scope(CkString var1) {
/* 278 */     chilkatJNI.CkOAuth2_get_Scope(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String scope() {
/* 282 */     return chilkatJNI.CkOAuth2_scope(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Scope(String var1) {
/* 286 */     chilkatJNI.CkOAuth2_put_Scope(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TokenEndpoint(CkString var1) {
/* 290 */     chilkatJNI.CkOAuth2_get_TokenEndpoint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tokenEndpoint() {
/* 294 */     return chilkatJNI.CkOAuth2_tokenEndpoint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TokenEndpoint(String var1) {
/* 298 */     chilkatJNI.CkOAuth2_put_TokenEndpoint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TokenType(CkString var1) {
/* 302 */     chilkatJNI.CkOAuth2_get_TokenType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tokenType() {
/* 306 */     return chilkatJNI.CkOAuth2_tokenType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TokenType(String var1) {
/* 310 */     chilkatJNI.CkOAuth2_put_TokenType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseBasicAuth() {
/* 314 */     return chilkatJNI.CkOAuth2_get_UseBasicAuth(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseBasicAuth(boolean var1) {
/* 318 */     chilkatJNI.CkOAuth2_put_UseBasicAuth(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 322 */     return chilkatJNI.CkOAuth2_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 326 */     chilkatJNI.CkOAuth2_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 330 */     chilkatJNI.CkOAuth2_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 334 */     return chilkatJNI.CkOAuth2_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Cancel() {
/* 338 */     return chilkatJNI.CkOAuth2_Cancel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetRedirectRequestParam(String var1, CkString var2) {
/* 342 */     return chilkatJNI.CkOAuth2_GetRedirectRequestParam(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getRedirectRequestParam(String var1) {
/* 346 */     return chilkatJNI.CkOAuth2_getRedirectRequestParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String redirectRequestParam(String var1) {
/* 350 */     return chilkatJNI.CkOAuth2_redirectRequestParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Monitor() {
/* 354 */     return chilkatJNI.CkOAuth2_Monitor(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask MonitorAsync() {
/* 358 */     long var1 = chilkatJNI.CkOAuth2_MonitorAsync(this.swigCPtr, this);
/* 359 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean RefreshAccessToken() {
/* 363 */     return chilkatJNI.CkOAuth2_RefreshAccessToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask RefreshAccessTokenAsync() {
/* 367 */     long var1 = chilkatJNI.CkOAuth2_RefreshAccessTokenAsync(this.swigCPtr, this);
/* 368 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 372 */     return chilkatJNI.CkOAuth2_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SleepMs(int var1) {
/* 376 */     chilkatJNI.CkOAuth2_SleepMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StartAuth(CkString var1) {
/* 380 */     return chilkatJNI.CkOAuth2_StartAuth(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String startAuth() {
/* 384 */     return chilkatJNI.CkOAuth2_startAuth(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean UseConnection(CkSocket var1) {
/* 388 */     return chilkatJNI.CkOAuth2_UseConnection(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkOAuth2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */