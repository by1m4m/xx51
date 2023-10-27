/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHttpRequest
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkHttpRequest(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHttpRequest var0) {
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
/*  29 */         chilkatJNI.delete_CkHttpRequest(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkHttpRequest()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkHttpRequest(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkHttpRequest_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkHttpRequest_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkHttpRequest_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Boundary(CkString var1) {
/*  54 */     chilkatJNI.CkHttpRequest_get_Boundary(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String boundary() {
/*  58 */     return chilkatJNI.CkHttpRequest_boundary(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Boundary(String var1) {
/*  62 */     chilkatJNI.CkHttpRequest_put_Boundary(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  66 */     chilkatJNI.CkHttpRequest_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  70 */     return chilkatJNI.CkHttpRequest_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Charset(String var1) {
/*  74 */     chilkatJNI.CkHttpRequest_put_Charset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ContentType(CkString var1) {
/*  78 */     chilkatJNI.CkHttpRequest_get_ContentType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String contentType() {
/*  82 */     return chilkatJNI.CkHttpRequest_contentType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ContentType(String var1) {
/*  86 */     chilkatJNI.CkHttpRequest_put_ContentType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  90 */     chilkatJNI.CkHttpRequest_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  94 */     return chilkatJNI.CkHttpRequest_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  98 */     chilkatJNI.CkHttpRequest_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EntireHeader(CkString var1) {
/* 102 */     chilkatJNI.CkHttpRequest_get_EntireHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String entireHeader() {
/* 106 */     return chilkatJNI.CkHttpRequest_entireHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EntireHeader(String var1) {
/* 110 */     chilkatJNI.CkHttpRequest_put_EntireHeader(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpVerb(CkString var1) {
/* 114 */     chilkatJNI.CkHttpRequest_get_HttpVerb(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpVerb() {
/* 118 */     return chilkatJNI.CkHttpRequest_httpVerb(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpVerb(String var1) {
/* 122 */     chilkatJNI.CkHttpRequest_put_HttpVerb(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_HttpVersion(CkString var1) {
/* 126 */     chilkatJNI.CkHttpRequest_get_HttpVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String httpVersion() {
/* 130 */     return chilkatJNI.CkHttpRequest_httpVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HttpVersion(String var1) {
/* 134 */     chilkatJNI.CkHttpRequest_put_HttpVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 138 */     chilkatJNI.CkHttpRequest_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 142 */     return chilkatJNI.CkHttpRequest_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 146 */     chilkatJNI.CkHttpRequest_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 150 */     return chilkatJNI.CkHttpRequest_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 154 */     chilkatJNI.CkHttpRequest_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 158 */     return chilkatJNI.CkHttpRequest_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 162 */     return chilkatJNI.CkHttpRequest_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 166 */     chilkatJNI.CkHttpRequest_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumHeaderFields() {
/* 170 */     return chilkatJNI.CkHttpRequest_get_NumHeaderFields(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumParams() {
/* 174 */     return chilkatJNI.CkHttpRequest_get_NumParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Path(CkString var1) {
/* 178 */     chilkatJNI.CkHttpRequest_get_Path(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String path() {
/* 182 */     return chilkatJNI.CkHttpRequest_path(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Path(String var1) {
/* 186 */     chilkatJNI.CkHttpRequest_put_Path(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_SendCharset() {
/* 190 */     return chilkatJNI.CkHttpRequest_get_SendCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SendCharset(boolean var1) {
/* 194 */     chilkatJNI.CkHttpRequest_put_SendCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 198 */     return chilkatJNI.CkHttpRequest_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 202 */     chilkatJNI.CkHttpRequest_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 206 */     chilkatJNI.CkHttpRequest_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 210 */     return chilkatJNI.CkHttpRequest_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddBytesForUpload(String var1, String var2, CkByteData var3) {
/* 214 */     return chilkatJNI.CkHttpRequest_AddBytesForUpload(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean AddBytesForUpload2(String var1, String var2, CkByteData var3, String var4) {
/* 218 */     return chilkatJNI.CkHttpRequest_AddBytesForUpload2(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddFileForUpload(String var1, String var2) {
/* 222 */     return chilkatJNI.CkHttpRequest_AddFileForUpload(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddFileForUpload2(String var1, String var2, String var3) {
/* 226 */     return chilkatJNI.CkHttpRequest_AddFileForUpload2(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void AddHeader(String var1, String var2) {
/* 230 */     chilkatJNI.CkHttpRequest_AddHeader(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddMwsSignature(String var1, String var2) {
/* 234 */     return chilkatJNI.CkHttpRequest_AddMwsSignature(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddParam(String var1, String var2) {
/* 238 */     chilkatJNI.CkHttpRequest_AddParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddStringForUpload(String var1, String var2, String var3, String var4) {
/* 242 */     return chilkatJNI.CkHttpRequest_AddStringForUpload(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddStringForUpload2(String var1, String var2, String var3, String var4, String var5) {
/* 246 */     return chilkatJNI.CkHttpRequest_AddStringForUpload2(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddSubHeader(int var1, String var2, String var3) {
/* 250 */     return chilkatJNI.CkHttpRequest_AddSubHeader(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GenerateRequestFile(String var1) {
/* 254 */     return chilkatJNI.CkHttpRequest_GenerateRequestFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenerateRequestText(CkString var1) {
/* 258 */     return chilkatJNI.CkHttpRequest_GenerateRequestText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String generateRequestText() {
/* 262 */     return chilkatJNI.CkHttpRequest_generateRequestText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderField(String var1, CkString var2) {
/* 266 */     return chilkatJNI.CkHttpRequest_GetHeaderField(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderField(String var1) {
/* 270 */     return chilkatJNI.CkHttpRequest_getHeaderField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerField(String var1) {
/* 274 */     return chilkatJNI.CkHttpRequest_headerField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderName(int var1, CkString var2) {
/* 278 */     return chilkatJNI.CkHttpRequest_GetHeaderName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderName(int var1) {
/* 282 */     return chilkatJNI.CkHttpRequest_getHeaderName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerName(int var1) {
/* 286 */     return chilkatJNI.CkHttpRequest_headerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderValue(int var1, CkString var2) {
/* 290 */     return chilkatJNI.CkHttpRequest_GetHeaderValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderValue(int var1) {
/* 294 */     return chilkatJNI.CkHttpRequest_getHeaderValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerValue(int var1) {
/* 298 */     return chilkatJNI.CkHttpRequest_headerValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetParam(String var1, CkString var2) {
/* 302 */     return chilkatJNI.CkHttpRequest_GetParam(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getParam(String var1) {
/* 306 */     return chilkatJNI.CkHttpRequest_getParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String param(String var1) {
/* 310 */     return chilkatJNI.CkHttpRequest_param(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetParamName(int var1, CkString var2) {
/* 314 */     return chilkatJNI.CkHttpRequest_GetParamName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getParamName(int var1) {
/* 318 */     return chilkatJNI.CkHttpRequest_getParamName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String paramName(int var1) {
/* 322 */     return chilkatJNI.CkHttpRequest_paramName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetParamValue(int var1, CkString var2) {
/* 326 */     return chilkatJNI.CkHttpRequest_GetParamValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getParamValue(int var1) {
/* 330 */     return chilkatJNI.CkHttpRequest_getParamValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String paramValue(int var1) {
/* 334 */     return chilkatJNI.CkHttpRequest_paramValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetUrlEncodedParams(CkString var1) {
/* 338 */     return chilkatJNI.CkHttpRequest_GetUrlEncodedParams(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getUrlEncodedParams() {
/* 342 */     return chilkatJNI.CkHttpRequest_getUrlEncodedParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String urlEncodedParams() {
/* 346 */     return chilkatJNI.CkHttpRequest_urlEncodedParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadBodyFromBd(CkBinData var1) {
/* 350 */     return chilkatJNI.CkHttpRequest_LoadBodyFromBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBodyFromBytes(CkByteData var1) {
/* 354 */     return chilkatJNI.CkHttpRequest_LoadBodyFromBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBodyFromFile(String var1) {
/* 358 */     return chilkatJNI.CkHttpRequest_LoadBodyFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBodyFromSb(CkStringBuilder var1, String var2) {
/* 362 */     return chilkatJNI.CkHttpRequest_LoadBodyFromSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadBodyFromString(String var1, String var2) {
/* 366 */     return chilkatJNI.CkHttpRequest_LoadBodyFromString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void RemoveAllParams() {
/* 370 */     chilkatJNI.CkHttpRequest_RemoveAllParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RemoveHeader(String var1) {
/* 374 */     return chilkatJNI.CkHttpRequest_RemoveHeader(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveParam(String var1) {
/* 378 */     chilkatJNI.CkHttpRequest_RemoveParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 382 */     return chilkatJNI.CkHttpRequest_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetFromUrl(String var1) {
/* 386 */     chilkatJNI.CkHttpRequest_SetFromUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StreamBodyFromFile(String var1) {
/* 390 */     return chilkatJNI.CkHttpRequest_StreamBodyFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StreamChunkFromFile(String var1, String var2, String var3) {
/* 394 */     return chilkatJNI.CkHttpRequest_StreamChunkFromFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UseGet() {
/* 398 */     chilkatJNI.CkHttpRequest_UseGet(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UseHead() {
/* 402 */     chilkatJNI.CkHttpRequest_UseHead(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UsePost() {
/* 406 */     chilkatJNI.CkHttpRequest_UsePost(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UsePostMultipartForm() {
/* 410 */     chilkatJNI.CkHttpRequest_UsePostMultipartForm(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UsePut() {
/* 414 */     chilkatJNI.CkHttpRequest_UsePut(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UseUpload() {
/* 418 */     chilkatJNI.CkHttpRequest_UseUpload(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UseUploadPut() {
/* 422 */     chilkatJNI.CkHttpRequest_UseUploadPut(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UseXmlHttp(String var1) {
/* 426 */     chilkatJNI.CkHttpRequest_UseXmlHttp(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */