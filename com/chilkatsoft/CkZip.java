/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkZip
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkZip(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkZip var0) {
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
/*  29 */         chilkatJNI.delete_CkZip(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkZip()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkZip(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkZip_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkZip_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkZip_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkZipProgress var1) {
/*  54 */     chilkatJNI.CkZip_put_EventCallbackObject(this.swigCPtr, this, CkZipProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkZip_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkZip_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AppendFromDir(CkString var1) {
/*  66 */     chilkatJNI.CkZip_get_AppendFromDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String appendFromDir() {
/*  70 */     return chilkatJNI.CkZip_appendFromDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AppendFromDir(String var1) {
/*  74 */     chilkatJNI.CkZip_put_AppendFromDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AutoRun(CkString var1) {
/*  78 */     chilkatJNI.CkZip_get_AutoRun(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String autoRun() {
/*  82 */     return chilkatJNI.CkZip_autoRun(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoRun(String var1) {
/*  86 */     chilkatJNI.CkZip_put_AutoRun(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_AutoRunParams(CkString var1) {
/*  90 */     chilkatJNI.CkZip_get_AutoRunParams(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String autoRunParams() {
/*  94 */     return chilkatJNI.CkZip_autoRunParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoRunParams(String var1) {
/*  98 */     chilkatJNI.CkZip_put_AutoRunParams(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_AutoTemp() {
/* 102 */     return chilkatJNI.CkZip_get_AutoTemp(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoTemp(boolean var1) {
/* 106 */     chilkatJNI.CkZip_put_AutoTemp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_CaseSensitive() {
/* 110 */     return chilkatJNI.CkZip_get_CaseSensitive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CaseSensitive(boolean var1) {
/* 114 */     chilkatJNI.CkZip_put_CaseSensitive(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ClearArchiveAttribute() {
/* 118 */     return chilkatJNI.CkZip_get_ClearArchiveAttribute(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClearArchiveAttribute(boolean var1) {
/* 122 */     chilkatJNI.CkZip_put_ClearArchiveAttribute(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ClearReadOnlyAttr() {
/* 126 */     return chilkatJNI.CkZip_get_ClearReadOnlyAttr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClearReadOnlyAttr(boolean var1) {
/* 130 */     chilkatJNI.CkZip_put_ClearReadOnlyAttr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Comment(CkString var1) {
/* 134 */     chilkatJNI.CkZip_get_Comment(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String comment() {
/* 138 */     return chilkatJNI.CkZip_comment(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Comment(String var1) {
/* 142 */     chilkatJNI.CkZip_put_Comment(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 146 */     chilkatJNI.CkZip_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 150 */     return chilkatJNI.CkZip_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 154 */     chilkatJNI.CkZip_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DecryptPassword(CkString var1) {
/* 158 */     chilkatJNI.CkZip_get_DecryptPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String decryptPassword() {
/* 162 */     return chilkatJNI.CkZip_decryptPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DecryptPassword(String var1) {
/* 166 */     chilkatJNI.CkZip_put_DecryptPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DiscardPaths() {
/* 170 */     return chilkatJNI.CkZip_get_DiscardPaths(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DiscardPaths(boolean var1) {
/* 174 */     chilkatJNI.CkZip_put_DiscardPaths(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Encryption() {
/* 178 */     return chilkatJNI.CkZip_get_Encryption(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Encryption(int var1) {
/* 182 */     chilkatJNI.CkZip_put_Encryption(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_EncryptKeyLength() {
/* 186 */     return chilkatJNI.CkZip_get_EncryptKeyLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EncryptKeyLength(int var1) {
/* 190 */     chilkatJNI.CkZip_put_EncryptKeyLength(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EncryptPassword(CkString var1) {
/* 194 */     chilkatJNI.CkZip_get_EncryptPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encryptPassword() {
/* 198 */     return chilkatJNI.CkZip_encryptPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EncryptPassword(String var1) {
/* 202 */     chilkatJNI.CkZip_put_EncryptPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeDefaultDir(CkString var1) {
/* 206 */     chilkatJNI.CkZip_get_ExeDefaultDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeDefaultDir() {
/* 210 */     return chilkatJNI.CkZip_exeDefaultDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeDefaultDir(String var1) {
/* 214 */     chilkatJNI.CkZip_put_ExeDefaultDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ExeFinishNotifier() {
/* 218 */     return chilkatJNI.CkZip_get_ExeFinishNotifier(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeFinishNotifier(boolean var1) {
/* 222 */     chilkatJNI.CkZip_put_ExeFinishNotifier(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeIconFile(CkString var1) {
/* 226 */     chilkatJNI.CkZip_get_ExeIconFile(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeIconFile() {
/* 230 */     return chilkatJNI.CkZip_exeIconFile(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeIconFile(String var1) {
/* 234 */     chilkatJNI.CkZip_put_ExeIconFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ExeNoInterface() {
/* 238 */     return chilkatJNI.CkZip_get_ExeNoInterface(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeNoInterface(boolean var1) {
/* 242 */     chilkatJNI.CkZip_put_ExeNoInterface(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ExeSilentProgress() {
/* 246 */     return chilkatJNI.CkZip_get_ExeSilentProgress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeSilentProgress(boolean var1) {
/* 250 */     chilkatJNI.CkZip_put_ExeSilentProgress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeTitle(CkString var1) {
/* 254 */     chilkatJNI.CkZip_get_ExeTitle(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeTitle() {
/* 258 */     return chilkatJNI.CkZip_exeTitle(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeTitle(String var1) {
/* 262 */     chilkatJNI.CkZip_put_ExeTitle(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeUnzipCaption(CkString var1) {
/* 266 */     chilkatJNI.CkZip_get_ExeUnzipCaption(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeUnzipCaption() {
/* 270 */     return chilkatJNI.CkZip_exeUnzipCaption(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeUnzipCaption(String var1) {
/* 274 */     chilkatJNI.CkZip_put_ExeUnzipCaption(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeUnzipDir(CkString var1) {
/* 278 */     chilkatJNI.CkZip_get_ExeUnzipDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeUnzipDir() {
/* 282 */     return chilkatJNI.CkZip_exeUnzipDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeUnzipDir(String var1) {
/* 286 */     chilkatJNI.CkZip_put_ExeUnzipDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ExeWaitForSetup() {
/* 290 */     return chilkatJNI.CkZip_get_ExeWaitForSetup(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeWaitForSetup(boolean var1) {
/* 294 */     chilkatJNI.CkZip_put_ExeWaitForSetup(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeXmlConfig(CkString var1) {
/* 298 */     chilkatJNI.CkZip_get_ExeXmlConfig(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeXmlConfig() {
/* 302 */     return chilkatJNI.CkZip_exeXmlConfig(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeXmlConfig(String var1) {
/* 306 */     chilkatJNI.CkZip_put_ExeXmlConfig(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_FileCount() {
/* 310 */     return chilkatJNI.CkZip_get_FileCount(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FileName(CkString var1) {
/* 314 */     chilkatJNI.CkZip_get_FileName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileName() {
/* 318 */     return chilkatJNI.CkZip_fileName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FileName(String var1) {
/* 322 */     chilkatJNI.CkZip_put_FileName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_HasZipFormatErrors() {
/* 326 */     return chilkatJNI.CkZip_get_HasZipFormatErrors(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 330 */     return chilkatJNI.CkZip_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 334 */     chilkatJNI.CkZip_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IgnoreAccessDenied() {
/* 338 */     return chilkatJNI.CkZip_get_IgnoreAccessDenied(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IgnoreAccessDenied(boolean var1) {
/* 342 */     chilkatJNI.CkZip_put_IgnoreAccessDenied(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 346 */     chilkatJNI.CkZip_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 350 */     return chilkatJNI.CkZip_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 354 */     chilkatJNI.CkZip_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 358 */     return chilkatJNI.CkZip_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 362 */     chilkatJNI.CkZip_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 366 */     return chilkatJNI.CkZip_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 370 */     return chilkatJNI.CkZip_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 374 */     chilkatJNI.CkZip_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumEntries() {
/* 378 */     return chilkatJNI.CkZip_get_NumEntries(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_OemCodePage() {
/* 382 */     return chilkatJNI.CkZip_get_OemCodePage(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OemCodePage(int var1) {
/* 386 */     chilkatJNI.CkZip_put_OemCodePage(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_OverwriteExisting() {
/* 390 */     return chilkatJNI.CkZip_get_OverwriteExisting(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OverwriteExisting(boolean var1) {
/* 394 */     chilkatJNI.CkZip_put_OverwriteExisting(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PasswordProtect() {
/* 398 */     return chilkatJNI.CkZip_get_PasswordProtect(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PasswordProtect(boolean var1) {
/* 402 */     chilkatJNI.CkZip_put_PasswordProtect(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PathPrefix(CkString var1) {
/* 406 */     chilkatJNI.CkZip_get_PathPrefix(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pathPrefix() {
/* 410 */     return chilkatJNI.CkZip_pathPrefix(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PathPrefix(String var1) {
/* 414 */     chilkatJNI.CkZip_put_PathPrefix(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_PercentDoneScale() {
/* 418 */     return chilkatJNI.CkZip_get_PercentDoneScale(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PercentDoneScale(int var1) {
/* 422 */     chilkatJNI.CkZip_put_PercentDoneScale(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PwdProtCharset(CkString var1) {
/* 426 */     chilkatJNI.CkZip_get_PwdProtCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pwdProtCharset() {
/* 430 */     return chilkatJNI.CkZip_pwdProtCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PwdProtCharset(String var1) {
/* 434 */     chilkatJNI.CkZip_put_PwdProtCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TempDir(CkString var1) {
/* 438 */     chilkatJNI.CkZip_get_TempDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tempDir() {
/* 442 */     return chilkatJNI.CkZip_tempDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TempDir(String var1) {
/* 446 */     chilkatJNI.CkZip_put_TempDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_TextFlag() {
/* 450 */     return chilkatJNI.CkZip_get_TextFlag(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TextFlag(boolean var1) {
/* 454 */     chilkatJNI.CkZip_put_TextFlag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 458 */     return chilkatJNI.CkZip_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 462 */     chilkatJNI.CkZip_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 466 */     chilkatJNI.CkZip_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 470 */     return chilkatJNI.CkZip_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Zipx() {
/* 474 */     return chilkatJNI.CkZip_get_Zipx(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Zipx(boolean var1) {
/* 478 */     chilkatJNI.CkZip_put_Zipx(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ZipxDefaultAlg(CkString var1) {
/* 482 */     chilkatJNI.CkZip_get_ZipxDefaultAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String zipxDefaultAlg() {
/* 486 */     return chilkatJNI.CkZip_zipxDefaultAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ZipxDefaultAlg(String var1) {
/* 490 */     chilkatJNI.CkZip_put_ZipxDefaultAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddEmbedded(String var1, String var2, String var3) {
/* 494 */     return chilkatJNI.CkZip_AddEmbedded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void AddNoCompressExtension(String var1) {
/* 498 */     chilkatJNI.CkZip_AddNoCompressExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendBase64(String var1, String var2) {
/* 502 */     long var3 = chilkatJNI.CkZip_AppendBase64(this.swigCPtr, this, var1, var2);
/* 503 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendBd(String var1, CkBinData var2) {
/* 507 */     long var3 = chilkatJNI.CkZip_AppendBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/* 508 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendCompressed(String var1, CkByteData var2) {
/* 512 */     long var3 = chilkatJNI.CkZip_AppendCompressed(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 513 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendData(String var1, CkByteData var2) {
/* 517 */     long var3 = chilkatJNI.CkZip_AppendData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 518 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendDataEncoded(String var1, String var2, String var3) {
/* 522 */     long var4 = chilkatJNI.CkZip_AppendDataEncoded(this.swigCPtr, this, var1, var2, var3);
/* 523 */     return var4 == 0L ? null : new CkZipEntry(var4, true);
/*     */   }
/*     */   
/*     */   public boolean AppendFiles(String var1, boolean var2) {
/* 527 */     return chilkatJNI.CkZip_AppendFiles(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AppendFilesAsync(String var1, boolean var2) {
/* 531 */     long var3 = chilkatJNI.CkZip_AppendFilesAsync(this.swigCPtr, this, var1, var2);
/* 532 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AppendFilesEx(String var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/* 536 */     return chilkatJNI.CkZip_AppendFilesEx(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*     */   }
/*     */   
/*     */   public CkTask AppendFilesExAsync(String var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
/* 540 */     long var7 = chilkatJNI.CkZip_AppendFilesExAsync(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/* 541 */     return var7 == 0L ? null : new CkTask(var7, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendHex(String var1, String var2) {
/* 545 */     long var3 = chilkatJNI.CkZip_AppendHex(this.swigCPtr, this, var1, var2);
/* 546 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public boolean AppendMultiple(CkStringArray var1, boolean var2) {
/* 550 */     return chilkatJNI.CkZip_AppendMultiple(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AppendMultipleAsync(CkStringArray var1, boolean var2) {
/* 554 */     long var3 = chilkatJNI.CkZip_AppendMultipleAsync(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1, var2);
/* 555 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendNew(String var1) {
/* 559 */     long var2 = chilkatJNI.CkZip_AppendNew(this.swigCPtr, this, var1);
/* 560 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendNewDir(String var1) {
/* 564 */     long var2 = chilkatJNI.CkZip_AppendNewDir(this.swigCPtr, this, var1);
/* 565 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public boolean AppendOneFileOrDir(String var1, boolean var2) {
/* 569 */     return chilkatJNI.CkZip_AppendOneFileOrDir(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AppendOneFileOrDirAsync(String var1, boolean var2) {
/* 573 */     long var3 = chilkatJNI.CkZip_AppendOneFileOrDirAsync(this.swigCPtr, this, var1, var2);
/* 574 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendString(String var1, String var2) {
/* 578 */     long var3 = chilkatJNI.CkZip_AppendString(this.swigCPtr, this, var1, var2);
/* 579 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry AppendString2(String var1, String var2, String var3) {
/* 583 */     long var4 = chilkatJNI.CkZip_AppendString2(this.swigCPtr, this, var1, var2, var3);
/* 584 */     return var4 == 0L ? null : new CkZipEntry(var4, true);
/*     */   }
/*     */   
/*     */   public boolean AppendZip(String var1) {
/* 588 */     return chilkatJNI.CkZip_AppendZip(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void CloseZip() {
/* 592 */     chilkatJNI.CkZip_CloseZip(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DeleteEntry(CkZipEntry var1) {
/* 596 */     return chilkatJNI.CkZip_DeleteEntry(this.swigCPtr, this, CkZipEntry.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void ExcludeDir(String var1) {
/* 600 */     chilkatJNI.CkZip_ExcludeDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Extract(String var1) {
/* 604 */     return chilkatJNI.CkZip_Extract(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ExtractAsync(String var1) {
/* 608 */     long var2 = chilkatJNI.CkZip_ExtractAsync(this.swigCPtr, this, var1);
/* 609 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ExtractExe(String var1, String var2) {
/* 613 */     return chilkatJNI.CkZip_ExtractExe(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ExtractExeAsync(String var1, String var2) {
/* 617 */     long var3 = chilkatJNI.CkZip_ExtractExeAsync(this.swigCPtr, this, var1, var2);
/* 618 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ExtractInto(String var1) {
/* 622 */     return chilkatJNI.CkZip_ExtractInto(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExtractMatching(String var1, String var2) {
/* 626 */     return chilkatJNI.CkZip_ExtractMatching(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ExtractNewer(String var1) {
/* 630 */     return chilkatJNI.CkZip_ExtractNewer(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExtractOne(CkZipEntry var1, String var2) {
/* 634 */     return chilkatJNI.CkZip_ExtractOne(this.swigCPtr, this, CkZipEntry.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkZipEntry FirstEntry() {
/* 638 */     long var1 = chilkatJNI.CkZip_FirstEntry(this.swigCPtr, this);
/* 639 */     return var1 == 0L ? null : new CkZipEntry(var1, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry FirstMatchingEntry(String var1) {
/* 643 */     long var2 = chilkatJNI.CkZip_FirstMatchingEntry(this.swigCPtr, this, var1);
/* 644 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetDirectoryAsXML(CkString var1) {
/* 648 */     return chilkatJNI.CkZip_GetDirectoryAsXML(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getDirectoryAsXML() {
/* 652 */     return chilkatJNI.CkZip_getDirectoryAsXML(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String directoryAsXML() {
/* 656 */     return chilkatJNI.CkZip_directoryAsXML(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkZipEntry GetEntryByID(int var1) {
/* 660 */     long var2 = chilkatJNI.CkZip_GetEntryByID(this.swigCPtr, this, var1);
/* 661 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry GetEntryByIndex(int var1) {
/* 665 */     long var2 = chilkatJNI.CkZip_GetEntryByIndex(this.swigCPtr, this, var1);
/* 666 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry GetEntryByName(String var1) {
/* 670 */     long var2 = chilkatJNI.CkZip_GetEntryByName(this.swigCPtr, this, var1);
/* 671 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public CkStringArray GetExclusions() {
/* 675 */     long var1 = chilkatJNI.CkZip_GetExclusions(this.swigCPtr, this);
/* 676 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetExeConfigParam(String var1, CkString var2) {
/* 680 */     return chilkatJNI.CkZip_GetExeConfigParam(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getExeConfigParam(String var1) {
/* 684 */     return chilkatJNI.CkZip_getExeConfigParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String exeConfigParam(String var1) {
/* 688 */     return chilkatJNI.CkZip_exeConfigParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkZipEntry InsertNew(String var1, int var2) {
/* 692 */     long var3 = chilkatJNI.CkZip_InsertNew(this.swigCPtr, this, var1, var2);
/* 693 */     return var3 == 0L ? null : new CkZipEntry(var3, true);
/*     */   }
/*     */   
/*     */   public boolean IsNoCompressExtension(String var1) {
/* 697 */     return chilkatJNI.CkZip_IsNoCompressExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsPasswordProtected(String var1) {
/* 701 */     return chilkatJNI.CkZip_IsPasswordProtected(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 705 */     return chilkatJNI.CkZip_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean NewZip(String var1) {
/* 709 */     return chilkatJNI.CkZip_NewZip(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenBd(CkBinData var1) {
/* 713 */     return chilkatJNI.CkZip_OpenBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenEmbedded(String var1, String var2) {
/* 717 */     return chilkatJNI.CkZip_OpenEmbedded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OpenFromByteData(CkByteData var1) {
/* 721 */     return chilkatJNI.CkZip_OpenFromByteData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenFromMemory(CkByteData var1) {
/* 725 */     return chilkatJNI.CkZip_OpenFromMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenMyEmbedded(String var1) {
/* 729 */     return chilkatJNI.CkZip_OpenMyEmbedded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenZip(String var1) {
/* 733 */     return chilkatJNI.CkZip_OpenZip(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask OpenZipAsync(String var1) {
/* 737 */     long var2 = chilkatJNI.CkZip_OpenZipAsync(this.swigCPtr, this, var1);
/* 738 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean QuickAppend(String var1) {
/* 742 */     return chilkatJNI.CkZip_QuickAppend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask QuickAppendAsync(String var1) {
/* 746 */     long var2 = chilkatJNI.CkZip_QuickAppendAsync(this.swigCPtr, this, var1);
/* 747 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean RemoveEmbedded(String var1, String var2) {
/* 751 */     return chilkatJNI.CkZip_RemoveEmbedded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void RemoveNoCompressExtension(String var1) {
/* 755 */     chilkatJNI.CkZip_RemoveNoCompressExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ReplaceEmbedded(String var1, String var2, String var3) {
/* 759 */     return chilkatJNI.CkZip_ReplaceEmbedded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 763 */     return chilkatJNI.CkZip_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetCompressionLevel(int var1) {
/* 767 */     chilkatJNI.CkZip_SetCompressionLevel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetExclusions(CkStringArray var1) {
/* 771 */     chilkatJNI.CkZip_SetExclusions(this.swigCPtr, this, CkStringArray.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void SetExeConfigParam(String var1, String var2) {
/* 775 */     chilkatJNI.CkZip_SetExeConfigParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetPassword(String var1) {
/* 779 */     chilkatJNI.CkZip_SetPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 783 */     return chilkatJNI.CkZip_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int Unzip(String var1) {
/* 787 */     return chilkatJNI.CkZip_Unzip(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UnzipAsync(String var1) {
/* 791 */     long var2 = chilkatJNI.CkZip_UnzipAsync(this.swigCPtr, this, var1);
/* 792 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public int UnzipInto(String var1) {
/* 796 */     return chilkatJNI.CkZip_UnzipInto(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UnzipIntoAsync(String var1) {
/* 800 */     long var2 = chilkatJNI.CkZip_UnzipIntoAsync(this.swigCPtr, this, var1);
/* 801 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public int UnzipMatching(String var1, String var2, boolean var3) {
/* 805 */     return chilkatJNI.CkZip_UnzipMatching(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UnzipMatchingAsync(String var1, String var2, boolean var3) {
/* 809 */     long var4 = chilkatJNI.CkZip_UnzipMatchingAsync(this.swigCPtr, this, var1, var2, var3);
/* 810 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public int UnzipMatchingInto(String var1, String var2, boolean var3) {
/* 814 */     return chilkatJNI.CkZip_UnzipMatchingInto(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UnzipMatchingIntoAsync(String var1, String var2, boolean var3) {
/* 818 */     long var4 = chilkatJNI.CkZip_UnzipMatchingIntoAsync(this.swigCPtr, this, var1, var2, var3);
/* 819 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public int UnzipNewer(String var1) {
/* 823 */     return chilkatJNI.CkZip_UnzipNewer(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UnzipNewerAsync(String var1) {
/* 827 */     long var2 = chilkatJNI.CkZip_UnzipNewerAsync(this.swigCPtr, this, var1);
/* 828 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean VerifyPassword() {
/* 832 */     return chilkatJNI.CkZip_VerifyPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean WriteBd(CkBinData var1) {
/* 836 */     return chilkatJNI.CkZip_WriteBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteBdAsync(CkBinData var1) {
/* 840 */     long var2 = chilkatJNI.CkZip_WriteBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 841 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteExe(String var1) {
/* 845 */     return chilkatJNI.CkZip_WriteExe(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean WriteExe2(String var1, String var2, boolean var3, int var4, String var5) {
/* 849 */     return chilkatJNI.CkZip_WriteExe2(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean WriteExeToMemory(CkByteData var1) {
/* 853 */     return chilkatJNI.CkZip_WriteExeToMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean WriteToMemory(CkByteData var1) {
/* 857 */     return chilkatJNI.CkZip_WriteToMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteToMemoryAsync() {
/* 861 */     long var1 = chilkatJNI.CkZip_WriteToMemoryAsync(this.swigCPtr, this);
/* 862 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean WriteZip() {
/* 866 */     return chilkatJNI.CkZip_WriteZip(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask WriteZipAsync() {
/* 870 */     long var1 = chilkatJNI.CkZip_WriteZipAsync(this.swigCPtr, this);
/* 871 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean WriteZipAndClose() {
/* 875 */     return chilkatJNI.CkZip_WriteZipAndClose(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask WriteZipAndCloseAsync() {
/* 879 */     long var1 = chilkatJNI.CkZip_WriteZipAndCloseAsync(this.swigCPtr, this);
/* 880 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkZip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */