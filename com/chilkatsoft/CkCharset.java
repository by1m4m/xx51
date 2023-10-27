/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCharset
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCharset(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCharset var0) {
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
/*  29 */         chilkatJNI.delete_CkCharset(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCharset()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCharset(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCharset_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCharset_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCharset_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AltToCharset(CkString var1) {
/*  54 */     chilkatJNI.CkCharset_get_AltToCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String altToCharset() {
/*  58 */     return chilkatJNI.CkCharset_altToCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AltToCharset(String var1) {
/*  62 */     chilkatJNI.CkCharset_put_AltToCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkCharset_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkCharset_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkCharset_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ErrorAction() {
/*  78 */     return chilkatJNI.CkCharset_get_ErrorAction(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ErrorAction(int var1) {
/*  82 */     chilkatJNI.CkCharset_put_ErrorAction(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_FromCharset(CkString var1) {
/*  86 */     chilkatJNI.CkCharset_get_FromCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fromCharset() {
/*  90 */     return chilkatJNI.CkCharset_fromCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FromCharset(String var1) {
/*  94 */     chilkatJNI.CkCharset_put_FromCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  98 */     chilkatJNI.CkCharset_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 102 */     return chilkatJNI.CkCharset_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 106 */     chilkatJNI.CkCharset_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 110 */     return chilkatJNI.CkCharset_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 114 */     chilkatJNI.CkCharset_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 118 */     return chilkatJNI.CkCharset_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastInputAsHex(CkString var1) {
/* 122 */     chilkatJNI.CkCharset_get_LastInputAsHex(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastInputAsHex() {
/* 126 */     return chilkatJNI.CkCharset_lastInputAsHex(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastInputAsQP(CkString var1) {
/* 130 */     chilkatJNI.CkCharset_get_LastInputAsQP(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastInputAsQP() {
/* 134 */     return chilkatJNI.CkCharset_lastInputAsQP(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 138 */     return chilkatJNI.CkCharset_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 142 */     chilkatJNI.CkCharset_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastOutputAsHex(CkString var1) {
/* 146 */     chilkatJNI.CkCharset_get_LastOutputAsHex(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastOutputAsHex() {
/* 150 */     return chilkatJNI.CkCharset_lastOutputAsHex(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastOutputAsQP(CkString var1) {
/* 154 */     chilkatJNI.CkCharset_get_LastOutputAsQP(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastOutputAsQP() {
/* 158 */     return chilkatJNI.CkCharset_lastOutputAsQP(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_SaveLast() {
/* 162 */     return chilkatJNI.CkCharset_get_SaveLast(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SaveLast(boolean var1) {
/* 166 */     chilkatJNI.CkCharset_put_SaveLast(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ToCharset(CkString var1) {
/* 170 */     chilkatJNI.CkCharset_get_ToCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toCharset() {
/* 174 */     return chilkatJNI.CkCharset_toCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ToCharset(String var1) {
/* 178 */     chilkatJNI.CkCharset_put_ToCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 182 */     return chilkatJNI.CkCharset_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 186 */     chilkatJNI.CkCharset_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 190 */     chilkatJNI.CkCharset_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 194 */     return chilkatJNI.CkCharset_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int CharsetToCodePage(String var1) {
/* 198 */     return chilkatJNI.CkCharset_CharsetToCodePage(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CodePageToCharset(int var1, CkString var2) {
/* 202 */     return chilkatJNI.CkCharset_CodePageToCharset(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String codePageToCharset(int var1) {
/* 206 */     return chilkatJNI.CkCharset_codePageToCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ConvertData(CkByteData var1, CkByteData var2) {
/* 210 */     return chilkatJNI.CkCharset_ConvertData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertFile(String var1, String var2) {
/* 214 */     return chilkatJNI.CkCharset_ConvertFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertFileNoPreamble(String var1, String var2) {
/* 218 */     return chilkatJNI.CkCharset_ConvertFileNoPreamble(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertFromUnicode(String var1, CkByteData var2) {
/* 222 */     return chilkatJNI.CkCharset_ConvertFromUnicode(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertFromUtf16(CkByteData var1, CkByteData var2) {
/* 226 */     return chilkatJNI.CkCharset_ConvertFromUtf16(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertHtml(CkByteData var1, CkByteData var2) {
/* 230 */     return chilkatJNI.CkCharset_ConvertHtml(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertHtmlFile(String var1, String var2) {
/* 234 */     return chilkatJNI.CkCharset_ConvertHtmlFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ConvertToUnicode(CkByteData var1, CkString var2) {
/* 238 */     return chilkatJNI.CkCharset_ConvertToUnicode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String convertToUnicode(CkByteData var1) {
/* 242 */     return chilkatJNI.CkCharset_convertToUnicode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ConvertToUtf16(CkByteData var1, CkByteData var2) {
/* 246 */     return chilkatJNI.CkCharset_ConvertToUtf16(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean EntityEncodeDec(String var1, CkString var2) {
/* 250 */     return chilkatJNI.CkCharset_EntityEncodeDec(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String entityEncodeDec(String var1) {
/* 254 */     return chilkatJNI.CkCharset_entityEncodeDec(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean EntityEncodeHex(String var1, CkString var2) {
/* 258 */     return chilkatJNI.CkCharset_EntityEncodeHex(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String entityEncodeHex(String var1) {
/* 262 */     return chilkatJNI.CkCharset_entityEncodeHex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHtmlCharset(CkByteData var1, CkString var2) {
/* 266 */     return chilkatJNI.CkCharset_GetHtmlCharset(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHtmlCharset(CkByteData var1) {
/* 270 */     return chilkatJNI.CkCharset_getHtmlCharset(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String htmlCharset(CkByteData var1) {
/* 274 */     return chilkatJNI.CkCharset_htmlCharset(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetHtmlFileCharset(String var1, CkString var2) {
/* 278 */     return chilkatJNI.CkCharset_GetHtmlFileCharset(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHtmlFileCharset(String var1) {
/* 282 */     return chilkatJNI.CkCharset_getHtmlFileCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String htmlFileCharset(String var1) {
/* 286 */     return chilkatJNI.CkCharset_htmlFileCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HtmlDecodeToStr(String var1, CkString var2) {
/* 290 */     return chilkatJNI.CkCharset_HtmlDecodeToStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String htmlDecodeToStr(String var1) {
/* 294 */     return chilkatJNI.CkCharset_htmlDecodeToStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HtmlEntityDecode(CkByteData var1, CkByteData var2) {
/* 298 */     return chilkatJNI.CkCharset_HtmlEntityDecode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean HtmlEntityDecodeFile(String var1, String var2) {
/* 302 */     return chilkatJNI.CkCharset_HtmlEntityDecodeFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 306 */     return chilkatJNI.CkCharset_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LowerCase(String var1, CkString var2) {
/* 310 */     return chilkatJNI.CkCharset_LowerCase(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String lowerCase(String var1) {
/* 314 */     return chilkatJNI.CkCharset_lowerCase(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ReadFile(String var1, CkByteData var2) {
/* 318 */     return chilkatJNI.CkCharset_ReadFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ReadFileToString(String var1, String var2, CkString var3) {
/* 322 */     return chilkatJNI.CkCharset_ReadFileToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readFileToString(String var1, String var2) {
/* 326 */     return chilkatJNI.CkCharset_readFileToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 330 */     return chilkatJNI.CkCharset_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetErrorBytes(CkByteData var1) {
/* 334 */     chilkatJNI.CkCharset_SetErrorBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void SetErrorString(String var1, String var2) {
/* 338 */     chilkatJNI.CkCharset_SetErrorString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 342 */     return chilkatJNI.CkCharset_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UpperCase(String var1, CkString var2) {
/* 346 */     return chilkatJNI.CkCharset_UpperCase(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String upperCase(String var1) {
/* 350 */     return chilkatJNI.CkCharset_upperCase(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UrlDecodeStr(String var1, CkString var2) {
/* 354 */     return chilkatJNI.CkCharset_UrlDecodeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String urlDecodeStr(String var1) {
/* 358 */     return chilkatJNI.CkCharset_urlDecodeStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyData(String var1, CkByteData var2) {
/* 362 */     return chilkatJNI.CkCharset_VerifyData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean VerifyFile(String var1, String var2) {
/* 366 */     return chilkatJNI.CkCharset_VerifyFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean WriteFile(String var1, CkByteData var2) {
/* 370 */     return chilkatJNI.CkCharset_WriteFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean WriteStringToFile(String var1, String var2, String var3) {
/* 374 */     return chilkatJNI.CkCharset_WriteStringToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCharset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */