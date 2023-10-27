/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkGzip
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkGzip(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkGzip var0) {
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
/*  29 */         chilkatJNI.delete_CkGzip(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkGzip()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkGzip(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkGzip_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkGzip_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkGzip_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkGzip_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkGzip_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkGzip_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Comment(CkString var1) {
/*  66 */     chilkatJNI.CkGzip_get_Comment(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String comment() {
/*  70 */     return chilkatJNI.CkGzip_comment(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Comment(String var1) {
/*  74 */     chilkatJNI.CkGzip_put_Comment(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_CompressionLevel() {
/*  78 */     return chilkatJNI.CkGzip_get_CompressionLevel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CompressionLevel(int var1) {
/*  82 */     chilkatJNI.CkGzip_put_CompressionLevel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  86 */     chilkatJNI.CkGzip_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  90 */     return chilkatJNI.CkGzip_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  94 */     chilkatJNI.CkGzip_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExtraData(CkByteData var1) {
/*  98 */     chilkatJNI.CkGzip_get_ExtraData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_ExtraData(CkByteData var1) {
/* 102 */     chilkatJNI.CkGzip_put_ExtraData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Filename(CkString var1) {
/* 106 */     chilkatJNI.CkGzip_get_Filename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String filename() {
/* 110 */     return chilkatJNI.CkGzip_filename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Filename(String var1) {
/* 114 */     chilkatJNI.CkGzip_put_Filename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 118 */     return chilkatJNI.CkGzip_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 122 */     chilkatJNI.CkGzip_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 126 */     chilkatJNI.CkGzip_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 130 */     return chilkatJNI.CkGzip_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 134 */     chilkatJNI.CkGzip_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 138 */     return chilkatJNI.CkGzip_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 142 */     chilkatJNI.CkGzip_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 146 */     return chilkatJNI.CkGzip_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 150 */     return chilkatJNI.CkGzip_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 154 */     chilkatJNI.CkGzip_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastMod(SYSTEMTIME var1) {
/* 158 */     chilkatJNI.CkGzip_get_LastMod(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_LastMod(SYSTEMTIME var1) {
/* 162 */     chilkatJNI.CkGzip_put_LastMod(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_LastModStr(CkString var1) {
/* 166 */     chilkatJNI.CkGzip_get_LastModStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastModStr() {
/* 170 */     return chilkatJNI.CkGzip_lastModStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastModStr(String var1) {
/* 174 */     chilkatJNI.CkGzip_put_LastModStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseCurrentDate() {
/* 178 */     return chilkatJNI.CkGzip_get_UseCurrentDate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseCurrentDate(boolean var1) {
/* 182 */     chilkatJNI.CkGzip_put_UseCurrentDate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 186 */     return chilkatJNI.CkGzip_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 190 */     chilkatJNI.CkGzip_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 194 */     chilkatJNI.CkGzip_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 198 */     return chilkatJNI.CkGzip_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CompressBd(CkBinData var1) {
/* 202 */     return chilkatJNI.CkGzip_CompressBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask CompressBdAsync(CkBinData var1) {
/* 206 */     long var2 = chilkatJNI.CkGzip_CompressBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 207 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFile(String var1, String var2) {
/* 211 */     return chilkatJNI.CkGzip_CompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileAsync(String var1, String var2) {
/* 215 */     long var3 = chilkatJNI.CkGzip_CompressFileAsync(this.swigCPtr, this, var1, var2);
/* 216 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFile2(String var1, String var2, String var3) {
/* 220 */     return chilkatJNI.CkGzip_CompressFile2(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask CompressFile2Async(String var1, String var2, String var3) {
/* 224 */     long var4 = chilkatJNI.CkGzip_CompressFile2Async(this.swigCPtr, this, var1, var2, var3);
/* 225 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFileToMem(String var1, CkByteData var2) {
/* 229 */     return chilkatJNI.CkGzip_CompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileToMemAsync(String var1) {
/* 233 */     long var2 = chilkatJNI.CkGzip_CompressFileToMemAsync(this.swigCPtr, this, var1);
/* 234 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressMemory(CkByteData var1, CkByteData var2) {
/* 238 */     return chilkatJNI.CkGzip_CompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressMemoryAsync(CkByteData var1) {
/* 242 */     long var2 = chilkatJNI.CkGzip_CompressMemoryAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 243 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressMemToFile(CkByteData var1, String var2) {
/* 247 */     return chilkatJNI.CkGzip_CompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressMemToFileAsync(CkByteData var1, String var2) {
/* 251 */     long var3 = chilkatJNI.CkGzip_CompressMemToFileAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/* 252 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressString(String var1, String var2, CkByteData var3) {
/* 256 */     return chilkatJNI.CkGzip_CompressString(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask CompressStringAsync(String var1, String var2) {
/* 260 */     long var3 = chilkatJNI.CkGzip_CompressStringAsync(this.swigCPtr, this, var1, var2);
/* 261 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressStringENC(String var1, String var2, String var3, CkString var4) {
/* 265 */     return chilkatJNI.CkGzip_CompressStringENC(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String compressStringENC(String var1, String var2, String var3) {
/* 269 */     return chilkatJNI.CkGzip_compressStringENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean CompressStringToFile(String var1, String var2, String var3) {
/* 273 */     return chilkatJNI.CkGzip_CompressStringToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask CompressStringToFileAsync(String var1, String var2, String var3) {
/* 277 */     long var4 = chilkatJNI.CkGzip_CompressStringToFileAsync(this.swigCPtr, this, var1, var2, var3);
/* 278 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean Decode(String var1, String var2, CkByteData var3) {
/* 282 */     return chilkatJNI.CkGzip_Decode(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean DeflateStringENC(String var1, String var2, String var3, CkString var4) {
/* 286 */     return chilkatJNI.CkGzip_DeflateStringENC(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String deflateStringENC(String var1, String var2, String var3) {
/* 290 */     return chilkatJNI.CkGzip_deflateStringENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean Encode(CkByteData var1, String var2, CkString var3) {
/* 294 */     return chilkatJNI.CkGzip_Encode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String encode(CkByteData var1, String var2) {
/* 298 */     return chilkatJNI.CkGzip_encode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ExamineFile(String var1) {
/* 302 */     return chilkatJNI.CkGzip_ExamineFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExamineMemory(CkByteData var1) {
/* 306 */     return chilkatJNI.CkGzip_ExamineMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkDateTime GetDt() {
/* 310 */     long var1 = chilkatJNI.CkGzip_GetDt(this.swigCPtr, this);
/* 311 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public boolean InflateStringENC(String var1, String var2, String var3, CkString var4) {
/* 315 */     return chilkatJNI.CkGzip_InflateStringENC(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String inflateStringENC(String var1, String var2, String var3) {
/* 319 */     return chilkatJNI.CkGzip_inflateStringENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 323 */     return chilkatJNI.CkGzip_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ReadFile(String var1, CkByteData var2) {
/* 327 */     return chilkatJNI.CkGzip_ReadFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 331 */     return chilkatJNI.CkGzip_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetDt(CkDateTime var1) {
/* 335 */     return chilkatJNI.CkGzip_SetDt(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean UncompressBd(CkBinData var1) {
/* 339 */     return chilkatJNI.CkGzip_UncompressBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask UncompressBdAsync(CkBinData var1) {
/* 343 */     long var2 = chilkatJNI.CkGzip_UncompressBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 344 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFile(String var1, String var2) {
/* 348 */     return chilkatJNI.CkGzip_UncompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileAsync(String var1, String var2) {
/* 352 */     long var3 = chilkatJNI.CkGzip_UncompressFileAsync(this.swigCPtr, this, var1, var2);
/* 353 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFileToMem(String var1, CkByteData var2) {
/* 357 */     return chilkatJNI.CkGzip_UncompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileToMemAsync(String var1) {
/* 361 */     long var2 = chilkatJNI.CkGzip_UncompressFileToMemAsync(this.swigCPtr, this, var1);
/* 362 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFileToString(String var1, String var2, CkString var3) {
/* 366 */     return chilkatJNI.CkGzip_UncompressFileToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String uncompressFileToString(String var1, String var2) {
/* 370 */     return chilkatJNI.CkGzip_uncompressFileToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileToStringAsync(String var1, String var2) {
/* 374 */     long var3 = chilkatJNI.CkGzip_UncompressFileToStringAsync(this.swigCPtr, this, var1, var2);
/* 375 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemory(CkByteData var1, CkByteData var2) {
/* 379 */     return chilkatJNI.CkGzip_UncompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressMemoryAsync(CkByteData var1) {
/* 383 */     long var2 = chilkatJNI.CkGzip_UncompressMemoryAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 384 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemToFile(CkByteData var1, String var2) {
/* 388 */     return chilkatJNI.CkGzip_UncompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressMemToFileAsync(CkByteData var1, String var2) {
/* 392 */     long var3 = chilkatJNI.CkGzip_UncompressMemToFileAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/* 393 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressString(CkByteData var1, String var2, CkString var3) {
/* 397 */     return chilkatJNI.CkGzip_UncompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String uncompressString(CkByteData var1, String var2) {
/* 401 */     return chilkatJNI.CkGzip_uncompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressStringAsync(CkByteData var1, String var2) {
/* 405 */     long var3 = chilkatJNI.CkGzip_UncompressStringAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/* 406 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressStringENC(String var1, String var2, String var3, CkString var4) {
/* 410 */     return chilkatJNI.CkGzip_UncompressStringENC(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String uncompressStringENC(String var1, String var2, String var3) {
/* 414 */     return chilkatJNI.CkGzip_uncompressStringENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 418 */     return chilkatJNI.CkGzip_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnTarGz(String var1, String var2, boolean var3) {
/* 422 */     return chilkatJNI.CkGzip_UnTarGz(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UnTarGzAsync(String var1, String var2, boolean var3) {
/* 426 */     long var4 = chilkatJNI.CkGzip_UnTarGzAsync(this.swigCPtr, this, var1, var2, var3);
/* 427 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean WriteFile(String var1, CkByteData var2) {
/* 431 */     return chilkatJNI.CkGzip_WriteFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean XfdlToXml(String var1, CkString var2) {
/* 435 */     return chilkatJNI.CkGzip_XfdlToXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String xfdlToXml(String var1) {
/* 439 */     return chilkatJNI.CkGzip_xfdlToXml(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkGzip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */