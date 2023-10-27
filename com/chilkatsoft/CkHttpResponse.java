/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHttpResponse
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkHttpResponse(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHttpResponse var0) {
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
/*  29 */         chilkatJNI.delete_CkHttpResponse(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkHttpResponse()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkHttpResponse(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkHttpResponse_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkHttpResponse_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkHttpResponse_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Body(CkByteData var1) {
/*  54 */     chilkatJNI.CkHttpResponse_get_Body(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_BodyQP(CkString var1) {
/*  58 */     chilkatJNI.CkHttpResponse_get_BodyQP(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String bodyQP() {
/*  62 */     return chilkatJNI.CkHttpResponse_bodyQP(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_BodyStr(CkString var1) {
/*  66 */     chilkatJNI.CkHttpResponse_get_BodyStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String bodyStr() {
/*  70 */     return chilkatJNI.CkHttpResponse_bodyStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  74 */     chilkatJNI.CkHttpResponse_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  78 */     return chilkatJNI.CkHttpResponse_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public long get_ContentLength() {
/*  82 */     return chilkatJNI.CkHttpResponse_get_ContentLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Date(SYSTEMTIME var1) {
/*  86 */     chilkatJNI.CkHttpResponse_get_Date(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DateStr(CkString var1) {
/*  90 */     chilkatJNI.CkHttpResponse_get_DateStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dateStr() {
/*  94 */     return chilkatJNI.CkHttpResponse_dateStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  98 */     chilkatJNI.CkHttpResponse_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 102 */     return chilkatJNI.CkHttpResponse_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 106 */     chilkatJNI.CkHttpResponse_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Domain(CkString var1) {
/* 110 */     chilkatJNI.CkHttpResponse_get_Domain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domain() {
/* 114 */     return chilkatJNI.CkHttpResponse_domain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FinalRedirectUrl(CkString var1) {
/* 118 */     chilkatJNI.CkHttpResponse_get_FinalRedirectUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String finalRedirectUrl() {
/* 122 */     return chilkatJNI.CkHttpResponse_finalRedirectUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FullMime(CkString var1) {
/* 126 */     chilkatJNI.CkHttpResponse_get_FullMime(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fullMime() {
/* 130 */     return chilkatJNI.CkHttpResponse_fullMime(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Header(CkString var1) {
/* 134 */     chilkatJNI.CkHttpResponse_get_Header(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String header() {
/* 138 */     return chilkatJNI.CkHttpResponse_header(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 142 */     chilkatJNI.CkHttpResponse_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 146 */     return chilkatJNI.CkHttpResponse_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 150 */     chilkatJNI.CkHttpResponse_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 154 */     return chilkatJNI.CkHttpResponse_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 158 */     chilkatJNI.CkHttpResponse_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 162 */     return chilkatJNI.CkHttpResponse_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 166 */     return chilkatJNI.CkHttpResponse_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 170 */     chilkatJNI.CkHttpResponse_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCookies() {
/* 174 */     return chilkatJNI.CkHttpResponse_get_NumCookies(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumHeaderFields() {
/* 178 */     return chilkatJNI.CkHttpResponse_get_NumHeaderFields(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_StatusCode() {
/* 182 */     return chilkatJNI.CkHttpResponse_get_StatusCode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_StatusLine(CkString var1) {
/* 186 */     chilkatJNI.CkHttpResponse_get_StatusLine(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String statusLine() {
/* 190 */     return chilkatJNI.CkHttpResponse_statusLine(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_StatusText(CkString var1) {
/* 194 */     chilkatJNI.CkHttpResponse_get_StatusText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String statusText() {
/* 198 */     return chilkatJNI.CkHttpResponse_statusText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 202 */     return chilkatJNI.CkHttpResponse_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 206 */     chilkatJNI.CkHttpResponse_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 210 */     chilkatJNI.CkHttpResponse_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 214 */     return chilkatJNI.CkHttpResponse_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetBodyBd(CkBinData var1) {
/* 218 */     return chilkatJNI.CkHttpResponse_GetBodyBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetBodySb(CkStringBuilder var1) {
/* 222 */     return chilkatJNI.CkHttpResponse_GetBodySb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetCookieDomain(int var1, CkString var2) {
/* 226 */     return chilkatJNI.CkHttpResponse_GetCookieDomain(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCookieDomain(int var1) {
/* 230 */     return chilkatJNI.CkHttpResponse_getCookieDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cookieDomain(int var1) {
/* 234 */     return chilkatJNI.CkHttpResponse_cookieDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCookieExpires(int var1, SYSTEMTIME var2) {
/* 238 */     return chilkatJNI.CkHttpResponse_GetCookieExpires(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetCookieExpiresStr(int var1, CkString var2) {
/* 242 */     return chilkatJNI.CkHttpResponse_GetCookieExpiresStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCookieExpiresStr(int var1) {
/* 246 */     return chilkatJNI.CkHttpResponse_getCookieExpiresStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cookieExpiresStr(int var1) {
/* 250 */     return chilkatJNI.CkHttpResponse_cookieExpiresStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCookieName(int var1, CkString var2) {
/* 254 */     return chilkatJNI.CkHttpResponse_GetCookieName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCookieName(int var1) {
/* 258 */     return chilkatJNI.CkHttpResponse_getCookieName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cookieName(int var1) {
/* 262 */     return chilkatJNI.CkHttpResponse_cookieName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCookiePath(int var1, CkString var2) {
/* 266 */     return chilkatJNI.CkHttpResponse_GetCookiePath(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCookiePath(int var1) {
/* 270 */     return chilkatJNI.CkHttpResponse_getCookiePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cookiePath(int var1) {
/* 274 */     return chilkatJNI.CkHttpResponse_cookiePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCookieValue(int var1, CkString var2) {
/* 278 */     return chilkatJNI.CkHttpResponse_GetCookieValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getCookieValue(int var1) {
/* 282 */     return chilkatJNI.CkHttpResponse_getCookieValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String cookieValue(int var1) {
/* 286 */     return chilkatJNI.CkHttpResponse_cookieValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderField(String var1, CkString var2) {
/* 290 */     return chilkatJNI.CkHttpResponse_GetHeaderField(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderField(String var1) {
/* 294 */     return chilkatJNI.CkHttpResponse_getHeaderField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerField(String var1) {
/* 298 */     return chilkatJNI.CkHttpResponse_headerField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderFieldAttr(String var1, String var2, CkString var3) {
/* 302 */     return chilkatJNI.CkHttpResponse_GetHeaderFieldAttr(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getHeaderFieldAttr(String var1, String var2) {
/* 306 */     return chilkatJNI.CkHttpResponse_getHeaderFieldAttr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String headerFieldAttr(String var1, String var2) {
/* 310 */     return chilkatJNI.CkHttpResponse_headerFieldAttr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderName(int var1, CkString var2) {
/* 314 */     return chilkatJNI.CkHttpResponse_GetHeaderName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderName(int var1) {
/* 318 */     return chilkatJNI.CkHttpResponse_getHeaderName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerName(int var1) {
/* 322 */     return chilkatJNI.CkHttpResponse_headerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderValue(int var1, CkString var2) {
/* 326 */     return chilkatJNI.CkHttpResponse_GetHeaderValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderValue(int var1) {
/* 330 */     return chilkatJNI.CkHttpResponse_getHeaderValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerValue(int var1) {
/* 334 */     return chilkatJNI.CkHttpResponse_headerValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 338 */     return chilkatJNI.CkHttpResponse_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveBodyBinary(String var1) {
/* 342 */     return chilkatJNI.CkHttpResponse_SaveBodyBinary(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveBodyText(boolean var1, String var2) {
/* 346 */     return chilkatJNI.CkHttpResponse_SaveBodyText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 350 */     return chilkatJNI.CkHttpResponse_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UrlEncParamValue(String var1, String var2, CkString var3) {
/* 354 */     return chilkatJNI.CkHttpResponse_UrlEncParamValue(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String urlEncParamValue(String var1, String var2) {
/* 358 */     return chilkatJNI.CkHttpResponse_urlEncParamValue(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */