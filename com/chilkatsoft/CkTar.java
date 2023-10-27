/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkTar
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkTar(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkTar var0) {
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
/*  29 */         chilkatJNI.delete_CkTar(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkTar()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkTar(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkTar_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkTar_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkTar_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkTarProgress var1) {
/*  54 */     chilkatJNI.CkTar_put_EventCallbackObject(this.swigCPtr, this, CkTarProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_CaptureXmlListing() {
/*  58 */     return chilkatJNI.CkTar_get_CaptureXmlListing(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CaptureXmlListing(boolean var1) {
/*  62 */     chilkatJNI.CkTar_put_CaptureXmlListing(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  66 */     chilkatJNI.CkTar_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  70 */     return chilkatJNI.CkTar_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Charset(String var1) {
/*  74 */     chilkatJNI.CkTar_put_Charset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkTar_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkTar_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkTar_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DirMode() {
/*  90 */     return chilkatJNI.CkTar_get_DirMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DirMode(int var1) {
/*  94 */     chilkatJNI.CkTar_put_DirMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DirPrefix(CkString var1) {
/*  98 */     chilkatJNI.CkTar_get_DirPrefix(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dirPrefix() {
/* 102 */     return chilkatJNI.CkTar_dirPrefix(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DirPrefix(String var1) {
/* 106 */     chilkatJNI.CkTar_put_DirPrefix(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_FileMode() {
/* 110 */     return chilkatJNI.CkTar_get_FileMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FileMode(int var1) {
/* 114 */     chilkatJNI.CkTar_put_FileMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_GroupId() {
/* 118 */     return chilkatJNI.CkTar_get_GroupId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_GroupId(int var1) {
/* 122 */     chilkatJNI.CkTar_put_GroupId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_GroupName(CkString var1) {
/* 126 */     chilkatJNI.CkTar_get_GroupName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String groupName() {
/* 130 */     return chilkatJNI.CkTar_groupName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_GroupName(String var1) {
/* 134 */     chilkatJNI.CkTar_put_GroupName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 138 */     return chilkatJNI.CkTar_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 142 */     chilkatJNI.CkTar_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 146 */     chilkatJNI.CkTar_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 150 */     return chilkatJNI.CkTar_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 154 */     chilkatJNI.CkTar_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 158 */     return chilkatJNI.CkTar_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 162 */     chilkatJNI.CkTar_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 166 */     return chilkatJNI.CkTar_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 170 */     return chilkatJNI.CkTar_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 174 */     chilkatJNI.CkTar_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_MatchCaseSensitive() {
/* 178 */     return chilkatJNI.CkTar_get_MatchCaseSensitive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MatchCaseSensitive(boolean var1) {
/* 182 */     chilkatJNI.CkTar_put_MatchCaseSensitive(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_MustMatch(CkString var1) {
/* 186 */     chilkatJNI.CkTar_get_MustMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String mustMatch() {
/* 190 */     return chilkatJNI.CkTar_mustMatch(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MustMatch(String var1) {
/* 194 */     chilkatJNI.CkTar_put_MustMatch(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_MustNotMatch(CkString var1) {
/* 198 */     chilkatJNI.CkTar_get_MustNotMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String mustNotMatch() {
/* 202 */     return chilkatJNI.CkTar_mustNotMatch(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MustNotMatch(String var1) {
/* 206 */     chilkatJNI.CkTar_put_MustNotMatch(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_NoAbsolutePaths() {
/* 210 */     return chilkatJNI.CkTar_get_NoAbsolutePaths(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NoAbsolutePaths(boolean var1) {
/* 214 */     chilkatJNI.CkTar_put_NoAbsolutePaths(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumDirRoots() {
/* 218 */     return chilkatJNI.CkTar_get_NumDirRoots(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_PercentDoneScale() {
/* 222 */     return chilkatJNI.CkTar_get_PercentDoneScale(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PercentDoneScale(int var1) {
/* 226 */     chilkatJNI.CkTar_put_PercentDoneScale(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ScriptFileMode() {
/* 230 */     return chilkatJNI.CkTar_get_ScriptFileMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ScriptFileMode(int var1) {
/* 234 */     chilkatJNI.CkTar_put_ScriptFileMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_SuppressOutput() {
/* 238 */     return chilkatJNI.CkTar_get_SuppressOutput(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SuppressOutput(boolean var1) {
/* 242 */     chilkatJNI.CkTar_put_SuppressOutput(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UntarCaseSensitive() {
/* 246 */     return chilkatJNI.CkTar_get_UntarCaseSensitive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarCaseSensitive(boolean var1) {
/* 250 */     chilkatJNI.CkTar_put_UntarCaseSensitive(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UntarDebugLog() {
/* 254 */     return chilkatJNI.CkTar_get_UntarDebugLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarDebugLog(boolean var1) {
/* 258 */     chilkatJNI.CkTar_put_UntarDebugLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UntarDiscardPaths() {
/* 262 */     return chilkatJNI.CkTar_get_UntarDiscardPaths(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarDiscardPaths(boolean var1) {
/* 266 */     chilkatJNI.CkTar_put_UntarDiscardPaths(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UntarFromDir(CkString var1) {
/* 270 */     chilkatJNI.CkTar_get_UntarFromDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String untarFromDir() {
/* 274 */     return chilkatJNI.CkTar_untarFromDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarFromDir(String var1) {
/* 278 */     chilkatJNI.CkTar_put_UntarFromDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UntarMatchPattern(CkString var1) {
/* 282 */     chilkatJNI.CkTar_get_UntarMatchPattern(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String untarMatchPattern() {
/* 286 */     return chilkatJNI.CkTar_untarMatchPattern(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarMatchPattern(String var1) {
/* 290 */     chilkatJNI.CkTar_put_UntarMatchPattern(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_UntarMaxCount() {
/* 294 */     return chilkatJNI.CkTar_get_UntarMaxCount(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UntarMaxCount(int var1) {
/* 298 */     chilkatJNI.CkTar_put_UntarMaxCount(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_UserId() {
/* 302 */     return chilkatJNI.CkTar_get_UserId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserId(int var1) {
/* 306 */     chilkatJNI.CkTar_put_UserId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UserName(CkString var1) {
/* 310 */     chilkatJNI.CkTar_get_UserName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String userName() {
/* 314 */     return chilkatJNI.CkTar_userName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserName(String var1) {
/* 318 */     chilkatJNI.CkTar_put_UserName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 322 */     return chilkatJNI.CkTar_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 326 */     chilkatJNI.CkTar_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 330 */     chilkatJNI.CkTar_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 334 */     return chilkatJNI.CkTar_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_WriteFormat(CkString var1) {
/* 338 */     chilkatJNI.CkTar_get_WriteFormat(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String writeFormat() {
/* 342 */     return chilkatJNI.CkTar_writeFormat(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WriteFormat(String var1) {
/* 346 */     chilkatJNI.CkTar_put_WriteFormat(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_XmlListing(CkString var1) {
/* 350 */     chilkatJNI.CkTar_get_XmlListing(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String xmlListing() {
/* 354 */     return chilkatJNI.CkTar_xmlListing(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_XmlListing(String var1) {
/* 358 */     chilkatJNI.CkTar_put_XmlListing(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddDirRoot(String var1) {
/* 362 */     return chilkatJNI.CkTar_AddDirRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddDirRoot2(String var1, String var2) {
/* 366 */     return chilkatJNI.CkTar_AddDirRoot2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddFile(String var1) {
/* 370 */     return chilkatJNI.CkTar_AddFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddFile2(String var1, String var2) {
/* 374 */     return chilkatJNI.CkTar_AddFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean CreateDeb(String var1, String var2, String var3) {
/* 378 */     return chilkatJNI.CkTar_CreateDeb(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetDirRoot(int var1, CkString var2) {
/* 382 */     return chilkatJNI.CkTar_GetDirRoot(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getDirRoot(int var1) {
/* 386 */     return chilkatJNI.CkTar_getDirRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String dirRoot(int var1) {
/* 390 */     return chilkatJNI.CkTar_dirRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ListXml(String var1, CkString var2) {
/* 394 */     return chilkatJNI.CkTar_ListXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String listXml(String var1) {
/* 398 */     return chilkatJNI.CkTar_listXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ListXmlAsync(String var1) {
/* 402 */     long var2 = chilkatJNI.CkTar_ListXmlAsync(this.swigCPtr, this, var1);
/* 403 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 407 */     return chilkatJNI.CkTar_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 411 */     return chilkatJNI.CkTar_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int Untar(String var1) {
/* 415 */     return chilkatJNI.CkTar_Untar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UntarAsync(String var1) {
/* 419 */     long var2 = chilkatJNI.CkTar_UntarAsync(this.swigCPtr, this, var1);
/* 420 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UntarBz2(String var1) {
/* 424 */     return chilkatJNI.CkTar_UntarBz2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UntarBz2Async(String var1) {
/* 428 */     long var2 = chilkatJNI.CkTar_UntarBz2Async(this.swigCPtr, this, var1);
/* 429 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UntarFirstMatchingToMemory(CkByteData var1, String var2, CkByteData var3) {
/* 433 */     return chilkatJNI.CkTar_UntarFirstMatchingToMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public int UntarFromMemory(CkByteData var1) {
/* 437 */     return chilkatJNI.CkTar_UntarFromMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask UntarFromMemoryAsync(CkByteData var1) {
/* 441 */     long var2 = chilkatJNI.CkTar_UntarFromMemoryAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 442 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UntarGz(String var1) {
/* 446 */     return chilkatJNI.CkTar_UntarGz(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UntarGzAsync(String var1) {
/* 450 */     long var2 = chilkatJNI.CkTar_UntarGzAsync(this.swigCPtr, this, var1);
/* 451 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UntarZ(String var1) {
/* 455 */     return chilkatJNI.CkTar_UntarZ(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask UntarZAsync(String var1) {
/* 459 */     long var2 = chilkatJNI.CkTar_UntarZAsync(this.swigCPtr, this, var1);
/* 460 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean VerifyTar(String var1) {
/* 464 */     return chilkatJNI.CkTar_VerifyTar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask VerifyTarAsync(String var1) {
/* 468 */     long var2 = chilkatJNI.CkTar_VerifyTarAsync(this.swigCPtr, this, var1);
/* 469 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteTar(String var1) {
/* 473 */     return chilkatJNI.CkTar_WriteTar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteTarAsync(String var1) {
/* 477 */     long var2 = chilkatJNI.CkTar_WriteTarAsync(this.swigCPtr, this, var1);
/* 478 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteTarBz2(String var1) {
/* 482 */     return chilkatJNI.CkTar_WriteTarBz2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteTarBz2Async(String var1) {
/* 486 */     long var2 = chilkatJNI.CkTar_WriteTarBz2Async(this.swigCPtr, this, var1);
/* 487 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteTarGz(String var1) {
/* 491 */     return chilkatJNI.CkTar_WriteTarGz(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteTarGzAsync(String var1) {
/* 495 */     long var2 = chilkatJNI.CkTar_WriteTarGzAsync(this.swigCPtr, this, var1);
/* 496 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkTar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */