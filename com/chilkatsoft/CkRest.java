/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkRest
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkRest(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkRest var0) {
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
/*  29 */         chilkatJNI.delete_CkRest(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkRest()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkRest(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkRest_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkRest_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkRest_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkRest_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AllowHeaderFolding() {
/*  58 */     return chilkatJNI.CkRest_get_AllowHeaderFolding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AllowHeaderFolding(boolean var1) {
/*  62 */     chilkatJNI.CkRest_put_AllowHeaderFolding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_AllowHeaderQB() {
/*  66 */     return chilkatJNI.CkRest_get_AllowHeaderQB(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AllowHeaderQB(boolean var1) {
/*  70 */     chilkatJNI.CkRest_put_AllowHeaderQB(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Authorization(CkString var1) {
/*  74 */     chilkatJNI.CkRest_get_Authorization(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String authorization() {
/*  78 */     return chilkatJNI.CkRest_authorization(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Authorization(String var1) {
/*  82 */     chilkatJNI.CkRest_put_Authorization(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ConnectFailReason() {
/*  86 */     return chilkatJNI.CkRest_get_ConnectFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ConnectTimeoutMs() {
/*  90 */     return chilkatJNI.CkRest_get_ConnectTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConnectTimeoutMs(int var1) {
/*  94 */     chilkatJNI.CkRest_put_ConnectTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  98 */     chilkatJNI.CkRest_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 102 */     return chilkatJNI.CkRest_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 106 */     chilkatJNI.CkRest_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 110 */     return chilkatJNI.CkRest_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 114 */     chilkatJNI.CkRest_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Host(CkString var1) {
/* 118 */     chilkatJNI.CkRest_get_Host(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String host() {
/* 122 */     return chilkatJNI.CkRest_host(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Host(String var1) {
/* 126 */     chilkatJNI.CkRest_put_Host(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_IdleTimeoutMs() {
/* 130 */     return chilkatJNI.CkRest_get_IdleTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IdleTimeoutMs(int var1) {
/* 134 */     chilkatJNI.CkRest_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 138 */     chilkatJNI.CkRest_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 142 */     return chilkatJNI.CkRest_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 146 */     chilkatJNI.CkRest_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 150 */     return chilkatJNI.CkRest_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 154 */     chilkatJNI.CkRest_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 158 */     return chilkatJNI.CkRest_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 162 */     return chilkatJNI.CkRest_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 166 */     chilkatJNI.CkRest_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastRequestHeader(CkString var1) {
/* 170 */     chilkatJNI.CkRest_get_LastRequestHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastRequestHeader() {
/* 174 */     return chilkatJNI.CkRest_lastRequestHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastRequestStartLine(CkString var1) {
/* 178 */     chilkatJNI.CkRest_get_LastRequestStartLine(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastRequestStartLine() {
/* 182 */     return chilkatJNI.CkRest_lastRequestStartLine(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumResponseHeaders() {
/* 186 */     return chilkatJNI.CkRest_get_NumResponseHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_PartSelector(CkString var1) {
/* 190 */     chilkatJNI.CkRest_get_PartSelector(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String partSelector() {
/* 194 */     return chilkatJNI.CkRest_partSelector(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PartSelector(String var1) {
/* 198 */     chilkatJNI.CkRest_put_PartSelector(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PercentDoneOnSend() {
/* 202 */     return chilkatJNI.CkRest_get_PercentDoneOnSend(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PercentDoneOnSend(boolean var1) {
/* 206 */     chilkatJNI.CkRest_put_PercentDoneOnSend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ResponseHeader(CkString var1) {
/* 210 */     chilkatJNI.CkRest_get_ResponseHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String responseHeader() {
/* 214 */     return chilkatJNI.CkRest_responseHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ResponseStatusCode() {
/* 218 */     return chilkatJNI.CkRest_get_ResponseStatusCode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ResponseStatusText(CkString var1) {
/* 222 */     chilkatJNI.CkRest_get_ResponseStatusText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String responseStatusText() {
/* 226 */     return chilkatJNI.CkRest_responseStatusText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_StreamNonChunked() {
/* 230 */     return chilkatJNI.CkRest_get_StreamNonChunked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StreamNonChunked(boolean var1) {
/* 234 */     chilkatJNI.CkRest_put_StreamNonChunked(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 238 */     return chilkatJNI.CkRest_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 242 */     chilkatJNI.CkRest_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 246 */     chilkatJNI.CkRest_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 250 */     return chilkatJNI.CkRest_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddHeader(String var1, String var2) {
/* 254 */     return chilkatJNI.CkRest_AddHeader(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddMwsSignature(String var1, String var2, String var3, String var4) {
/* 258 */     return chilkatJNI.CkRest_AddMwsSignature(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddPathParam(String var1, String var2) {
/* 262 */     return chilkatJNI.CkRest_AddPathParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddQueryParam(String var1, String var2) {
/* 266 */     return chilkatJNI.CkRest_AddQueryParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddQueryParams(String var1) {
/* 270 */     return chilkatJNI.CkRest_AddQueryParams(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddQueryParamSb(String var1, CkStringBuilder var2) {
/* 274 */     return chilkatJNI.CkRest_AddQueryParamSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ClearAllHeaders() {
/* 278 */     return chilkatJNI.CkRest_ClearAllHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ClearAllParts() {
/* 282 */     return chilkatJNI.CkRest_ClearAllParts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ClearAllPathParams() {
/* 286 */     return chilkatJNI.CkRest_ClearAllPathParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ClearAllQueryParams() {
/* 290 */     return chilkatJNI.CkRest_ClearAllQueryParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ClearAuth() {
/* 294 */     return chilkatJNI.CkRest_ClearAuth(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearResponseBodyStream() {
/* 298 */     chilkatJNI.CkRest_ClearResponseBodyStream(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Connect(String var1, int var2, boolean var3, boolean var4) {
/* 302 */     return chilkatJNI.CkRest_Connect(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkTask ConnectAsync(String var1, int var2, boolean var3, boolean var4) {
/* 306 */     long var5 = chilkatJNI.CkRest_ConnectAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 307 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean Disconnect(int var1) {
/* 311 */     return chilkatJNI.CkRest_Disconnect(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask DisconnectAsync(int var1) {
/* 315 */     long var2 = chilkatJNI.CkRest_DisconnectAsync(this.swigCPtr, this, var1);
/* 316 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestBd(String var1, String var2, CkBinData var3, CkStringBuilder var4) {
/* 320 */     return chilkatJNI.CkRest_FullRequestBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3, CkStringBuilder.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestBdAsync(String var1, String var2, CkBinData var3, CkStringBuilder var4) {
/* 324 */     long var5 = chilkatJNI.CkRest_FullRequestBdAsync(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3, CkStringBuilder.getCPtr(var4), var4);
/* 325 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestBinary(String var1, String var2, CkByteData var3, CkString var4) {
/* 329 */     return chilkatJNI.CkRest_FullRequestBinary(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String fullRequestBinary(String var1, String var2, CkByteData var3) {
/* 333 */     return chilkatJNI.CkRest_fullRequestBinary(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestBinaryAsync(String var1, String var2, CkByteData var3) {
/* 337 */     long var4 = chilkatJNI.CkRest_FullRequestBinaryAsync(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/* 338 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestFormUrlEncoded(String var1, String var2, CkString var3) {
/* 342 */     return chilkatJNI.CkRest_FullRequestFormUrlEncoded(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String fullRequestFormUrlEncoded(String var1, String var2) {
/* 346 */     return chilkatJNI.CkRest_fullRequestFormUrlEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestFormUrlEncodedAsync(String var1, String var2) {
/* 350 */     long var3 = chilkatJNI.CkRest_FullRequestFormUrlEncodedAsync(this.swigCPtr, this, var1, var2);
/* 351 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestMultipart(String var1, String var2, CkString var3) {
/* 355 */     return chilkatJNI.CkRest_FullRequestMultipart(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String fullRequestMultipart(String var1, String var2) {
/* 359 */     return chilkatJNI.CkRest_fullRequestMultipart(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestMultipartAsync(String var1, String var2) {
/* 363 */     long var3 = chilkatJNI.CkRest_FullRequestMultipartAsync(this.swigCPtr, this, var1, var2);
/* 364 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestNoBody(String var1, String var2, CkString var3) {
/* 368 */     return chilkatJNI.CkRest_FullRequestNoBody(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String fullRequestNoBody(String var1, String var2) {
/* 372 */     return chilkatJNI.CkRest_fullRequestNoBody(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestNoBodyAsync(String var1, String var2) {
/* 376 */     long var3 = chilkatJNI.CkRest_FullRequestNoBodyAsync(this.swigCPtr, this, var1, var2);
/* 377 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestNoBodyBd(String var1, String var2, CkBinData var3) {
/* 381 */     return chilkatJNI.CkRest_FullRequestNoBodyBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestNoBodyBdAsync(String var1, String var2, CkBinData var3) {
/* 385 */     long var4 = chilkatJNI.CkRest_FullRequestNoBodyBdAsync(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/* 386 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestNoBodySb(String var1, String var2, CkStringBuilder var3) {
/* 390 */     return chilkatJNI.CkRest_FullRequestNoBodySb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestNoBodySbAsync(String var1, String var2, CkStringBuilder var3) {
/* 394 */     long var4 = chilkatJNI.CkRest_FullRequestNoBodySbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/* 395 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestSb(String var1, String var2, CkStringBuilder var3, CkStringBuilder var4) {
/* 399 */     return chilkatJNI.CkRest_FullRequestSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3, CkStringBuilder.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestSbAsync(String var1, String var2, CkStringBuilder var3, CkStringBuilder var4) {
/* 403 */     long var5 = chilkatJNI.CkRest_FullRequestSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3, CkStringBuilder.getCPtr(var4), var4);
/* 404 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestStream(String var1, String var2, CkStream var3, CkString var4) {
/* 408 */     return chilkatJNI.CkRest_FullRequestStream(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String fullRequestStream(String var1, String var2, CkStream var3) {
/* 412 */     return chilkatJNI.CkRest_fullRequestStream(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestStreamAsync(String var1, String var2, CkStream var3) {
/* 416 */     long var4 = chilkatJNI.CkRest_FullRequestStreamAsync(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3);
/* 417 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean FullRequestString(String var1, String var2, String var3, CkString var4) {
/* 421 */     return chilkatJNI.CkRest_FullRequestString(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String fullRequestString(String var1, String var2, String var3) {
/* 425 */     return chilkatJNI.CkRest_fullRequestString(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask FullRequestStringAsync(String var1, String var2, String var3) {
/* 429 */     long var4 = chilkatJNI.CkRest_FullRequestStringAsync(this.swigCPtr, this, var1, var2, var3);
/* 430 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean ReadRespBd(CkBinData var1) {
/* 434 */     return chilkatJNI.CkRest_ReadRespBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadRespBdAsync(CkBinData var1) {
/* 438 */     long var2 = chilkatJNI.CkRest_ReadRespBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 439 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReadRespBodyBinary(CkByteData var1) {
/* 443 */     return chilkatJNI.CkRest_ReadRespBodyBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadRespBodyBinaryAsync() {
/* 447 */     long var1 = chilkatJNI.CkRest_ReadRespBodyBinaryAsync(this.swigCPtr, this);
/* 448 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReadRespBodyStream(CkStream var1, boolean var2) {
/* 452 */     return chilkatJNI.CkRest_ReadRespBodyStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ReadRespBodyStreamAsync(CkStream var1, boolean var2) {
/* 456 */     long var3 = chilkatJNI.CkRest_ReadRespBodyStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1, var2);
/* 457 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ReadRespBodyString(CkString var1) {
/* 461 */     return chilkatJNI.CkRest_ReadRespBodyString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String readRespBodyString() {
/* 465 */     return chilkatJNI.CkRest_readRespBodyString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReadRespBodyStringAsync() {
/* 469 */     long var1 = chilkatJNI.CkRest_ReadRespBodyStringAsync(this.swigCPtr, this);
/* 470 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public int ReadResponseHeader() {
/* 474 */     return chilkatJNI.CkRest_ReadResponseHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReadResponseHeaderAsync() {
/* 478 */     long var1 = chilkatJNI.CkRest_ReadResponseHeaderAsync(this.swigCPtr, this);
/* 479 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReadRespSb(CkStringBuilder var1) {
/* 483 */     return chilkatJNI.CkRest_ReadRespSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadRespSbAsync(CkStringBuilder var1) {
/* 487 */     long var2 = chilkatJNI.CkRest_ReadRespSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/* 488 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public CkUrl RedirectUrl() {
/* 492 */     long var1 = chilkatJNI.CkRest_RedirectUrl(this.swigCPtr, this);
/* 493 */     return var1 == 0L ? null : new CkUrl(var1, true);
/*     */   }
/*     */   
/*     */   public boolean RemoveHeader(String var1) {
/* 497 */     return chilkatJNI.CkRest_RemoveHeader(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveQueryParam(String var1) {
/* 501 */     return chilkatJNI.CkRest_RemoveQueryParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ResponseHdrByName(String var1, CkString var2) {
/* 505 */     return chilkatJNI.CkRest_ResponseHdrByName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String responseHdrByName(String var1) {
/* 509 */     return chilkatJNI.CkRest_responseHdrByName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ResponseHdrName(int var1, CkString var2) {
/* 513 */     return chilkatJNI.CkRest_ResponseHdrName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String responseHdrName(int var1) {
/* 517 */     return chilkatJNI.CkRest_responseHdrName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ResponseHdrValue(int var1, CkString var2) {
/* 521 */     return chilkatJNI.CkRest_ResponseHdrValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String responseHdrValue(int var1) {
/* 525 */     return chilkatJNI.CkRest_responseHdrValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 529 */     return chilkatJNI.CkRest_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SendReqBd(String var1, String var2, CkBinData var3) {
/* 533 */     return chilkatJNI.CkRest_SendReqBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqBdAsync(String var1, String var2, CkBinData var3) {
/* 537 */     long var4 = chilkatJNI.CkRest_SendReqBdAsync(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/* 538 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqBinaryBody(String var1, String var2, CkByteData var3) {
/* 542 */     return chilkatJNI.CkRest_SendReqBinaryBody(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqBinaryBodyAsync(String var1, String var2, CkByteData var3) {
/* 546 */     long var4 = chilkatJNI.CkRest_SendReqBinaryBodyAsync(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/* 547 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqFormUrlEncoded(String var1, String var2) {
/* 551 */     return chilkatJNI.CkRest_SendReqFormUrlEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqFormUrlEncodedAsync(String var1, String var2) {
/* 555 */     long var3 = chilkatJNI.CkRest_SendReqFormUrlEncodedAsync(this.swigCPtr, this, var1, var2);
/* 556 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqMultipart(String var1, String var2) {
/* 560 */     return chilkatJNI.CkRest_SendReqMultipart(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqMultipartAsync(String var1, String var2) {
/* 564 */     long var3 = chilkatJNI.CkRest_SendReqMultipartAsync(this.swigCPtr, this, var1, var2);
/* 565 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqNoBody(String var1, String var2) {
/* 569 */     return chilkatJNI.CkRest_SendReqNoBody(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendReqNoBodyAsync(String var1, String var2) {
/* 573 */     long var3 = chilkatJNI.CkRest_SendReqNoBodyAsync(this.swigCPtr, this, var1, var2);
/* 574 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqSb(String var1, String var2, CkStringBuilder var3) {
/* 578 */     return chilkatJNI.CkRest_SendReqSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqSbAsync(String var1, String var2, CkStringBuilder var3) {
/* 582 */     long var4 = chilkatJNI.CkRest_SendReqSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/* 583 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqStreamBody(String var1, String var2, CkStream var3) {
/* 587 */     return chilkatJNI.CkRest_SendReqStreamBody(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqStreamBodyAsync(String var1, String var2, CkStream var3) {
/* 591 */     long var4 = chilkatJNI.CkRest_SendReqStreamBodyAsync(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3);
/* 592 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendReqStringBody(String var1, String var2, String var3) {
/* 596 */     return chilkatJNI.CkRest_SendReqStringBody(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask SendReqStringBodyAsync(String var1, String var2, String var3) {
/* 600 */     long var4 = chilkatJNI.CkRest_SendReqStringBodyAsync(this.swigCPtr, this, var1, var2, var3);
/* 601 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SetAuthAws(CkAuthAws var1) {
/* 605 */     return chilkatJNI.CkRest_SetAuthAws(this.swigCPtr, this, CkAuthAws.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetAuthAzureAD(CkAuthAzureAD var1) {
/* 609 */     return chilkatJNI.CkRest_SetAuthAzureAD(this.swigCPtr, this, CkAuthAzureAD.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetAuthAzureSas(CkAuthAzureSAS var1) {
/* 613 */     return chilkatJNI.CkRest_SetAuthAzureSas(this.swigCPtr, this, CkAuthAzureSAS.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetAuthAzureStorage(CkAuthAzureStorage var1) {
/* 617 */     return chilkatJNI.CkRest_SetAuthAzureStorage(this.swigCPtr, this, CkAuthAzureStorage.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetAuthBasic(String var1, String var2) {
/* 621 */     return chilkatJNI.CkRest_SetAuthBasic(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetAuthBasicSecure(CkSecureString var1, CkSecureString var2) {
/* 625 */     return chilkatJNI.CkRest_SetAuthBasicSecure(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkSecureString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetAuthGoogle(CkAuthGoogle var1) {
/* 629 */     return chilkatJNI.CkRest_SetAuthGoogle(this.swigCPtr, this, CkAuthGoogle.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetAuthOAuth1(CkOAuth1 var1, boolean var2) {
/* 633 */     return chilkatJNI.CkRest_SetAuthOAuth1(this.swigCPtr, this, CkOAuth1.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetAuthOAuth2(CkOAuth2 var1) {
/* 637 */     return chilkatJNI.CkRest_SetAuthOAuth2(this.swigCPtr, this, CkOAuth2.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetMultipartBodyBd(CkBinData var1) {
/* 641 */     return chilkatJNI.CkRest_SetMultipartBodyBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetMultipartBodyBinary(CkByteData var1) {
/* 645 */     return chilkatJNI.CkRest_SetMultipartBodyBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetMultipartBodySb(CkStringBuilder var1) {
/* 649 */     return chilkatJNI.CkRest_SetMultipartBodySb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetMultipartBodyStream(CkStream var1) {
/* 653 */     return chilkatJNI.CkRest_SetMultipartBodyStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetMultipartBodyString(String var1) {
/* 657 */     return chilkatJNI.CkRest_SetMultipartBodyString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetResponseBodyStream(int var1, boolean var2, CkStream var3) {
/* 661 */     return chilkatJNI.CkRest_SetResponseBodyStream(this.swigCPtr, this, var1, var2, CkStream.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean UseConnection(CkSocket var1, boolean var2) {
/* 665 */     return chilkatJNI.CkRest_UseConnection(this.swigCPtr, this, CkSocket.getCPtr(var1), var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkRest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */