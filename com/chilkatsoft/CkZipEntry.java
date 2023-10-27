/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkZipEntry
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkZipEntry(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkZipEntry var0) {
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
/*  29 */         chilkatJNI.delete_CkZipEntry(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkZipEntry()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkZipEntry(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkZipEntry_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkZipEntry_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkZipEntry_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkZipEntry_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Comment(CkString var1) {
/*  58 */     chilkatJNI.CkZipEntry_get_Comment(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String comment() {
/*  62 */     return chilkatJNI.CkZipEntry_comment(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Comment(String var1) {
/*  66 */     chilkatJNI.CkZipEntry_put_Comment(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long get_CompressedLength() {
/*  70 */     return chilkatJNI.CkZipEntry_get_CompressedLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_CompressedLengthStr(CkString var1) {
/*  74 */     chilkatJNI.CkZipEntry_get_CompressedLengthStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String compressedLengthStr() {
/*  78 */     return chilkatJNI.CkZipEntry_compressedLengthStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_CompressionLevel() {
/*  82 */     return chilkatJNI.CkZipEntry_get_CompressionLevel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CompressionLevel(int var1) {
/*  86 */     chilkatJNI.CkZipEntry_put_CompressionLevel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_CompressionMethod() {
/*  90 */     return chilkatJNI.CkZipEntry_get_CompressionMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CompressionMethod(int var1) {
/*  94 */     chilkatJNI.CkZipEntry_put_CompressionMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Crc() {
/*  98 */     return chilkatJNI.CkZipEntry_get_Crc(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 102 */     chilkatJNI.CkZipEntry_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 106 */     return chilkatJNI.CkZipEntry_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 110 */     chilkatJNI.CkZipEntry_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_EncryptionKeyLen() {
/* 114 */     return chilkatJNI.CkZipEntry_get_EncryptionKeyLen(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_EntryID() {
/* 118 */     return chilkatJNI.CkZipEntry_get_EntryID(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_EntryType() {
/* 122 */     return chilkatJNI.CkZipEntry_get_EntryType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FileDateTime(SYSTEMTIME var1) {
/* 126 */     chilkatJNI.CkZipEntry_get_FileDateTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_FileDateTime(SYSTEMTIME var1) {
/* 130 */     chilkatJNI.CkZipEntry_put_FileDateTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_FileDateTimeStr(CkString var1) {
/* 134 */     chilkatJNI.CkZipEntry_get_FileDateTimeStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileDateTimeStr() {
/* 138 */     return chilkatJNI.CkZipEntry_fileDateTimeStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FileDateTimeStr(String var1) {
/* 142 */     chilkatJNI.CkZipEntry_put_FileDateTimeStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_FileName(CkString var1) {
/* 146 */     chilkatJNI.CkZipEntry_get_FileName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileName() {
/* 150 */     return chilkatJNI.CkZipEntry_fileName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_FileName(String var1) {
/* 154 */     chilkatJNI.CkZipEntry_put_FileName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_FileNameHex(CkString var1) {
/* 158 */     chilkatJNI.CkZipEntry_get_FileNameHex(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileNameHex() {
/* 162 */     return chilkatJNI.CkZipEntry_fileNameHex(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 166 */     return chilkatJNI.CkZipEntry_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 170 */     chilkatJNI.CkZipEntry_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsAesEncrypted() {
/* 174 */     return chilkatJNI.CkZipEntry_get_IsAesEncrypted(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsDirectory() {
/* 178 */     return chilkatJNI.CkZipEntry_get_IsDirectory(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 182 */     chilkatJNI.CkZipEntry_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 186 */     return chilkatJNI.CkZipEntry_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 190 */     chilkatJNI.CkZipEntry_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 194 */     return chilkatJNI.CkZipEntry_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 198 */     chilkatJNI.CkZipEntry_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 202 */     return chilkatJNI.CkZipEntry_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 206 */     return chilkatJNI.CkZipEntry_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 210 */     chilkatJNI.CkZipEntry_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_TextFlag() {
/* 214 */     return chilkatJNI.CkZipEntry_get_TextFlag(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TextFlag(boolean var1) {
/* 218 */     chilkatJNI.CkZipEntry_put_TextFlag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long get_UncompressedLength() {
/* 222 */     return chilkatJNI.CkZipEntry_get_UncompressedLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_UncompressedLengthStr(CkString var1) {
/* 226 */     chilkatJNI.CkZipEntry_get_UncompressedLengthStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String uncompressedLengthStr() {
/* 230 */     return chilkatJNI.CkZipEntry_uncompressedLengthStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 234 */     return chilkatJNI.CkZipEntry_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 238 */     chilkatJNI.CkZipEntry_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 242 */     chilkatJNI.CkZipEntry_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 246 */     return chilkatJNI.CkZipEntry_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendData(CkByteData var1) {
/* 250 */     return chilkatJNI.CkZipEntry_AppendData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask AppendDataAsync(CkByteData var1) {
/* 254 */     long var2 = chilkatJNI.CkZipEntry_AppendDataAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 255 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean AppendString(String var1, String var2) {
/* 259 */     return chilkatJNI.CkZipEntry_AppendString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask AppendStringAsync(String var1, String var2) {
/* 263 */     long var3 = chilkatJNI.CkZipEntry_AppendStringAsync(this.swigCPtr, this, var1, var2);
/* 264 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean Copy(CkByteData var1) {
/* 268 */     return chilkatJNI.CkZipEntry_Copy(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean CopyToBase64(CkString var1) {
/* 272 */     return chilkatJNI.CkZipEntry_CopyToBase64(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String copyToBase64() {
/* 276 */     return chilkatJNI.CkZipEntry_copyToBase64(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CopyToHex(CkString var1) {
/* 280 */     return chilkatJNI.CkZipEntry_CopyToHex(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String copyToHex() {
/* 284 */     return chilkatJNI.CkZipEntry_copyToHex(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Extract(String var1) {
/* 288 */     return chilkatJNI.CkZipEntry_Extract(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ExtractAsync(String var1) {
/* 292 */     long var2 = chilkatJNI.CkZipEntry_ExtractAsync(this.swigCPtr, this, var1);
/* 293 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ExtractInto(String var1) {
/* 297 */     return chilkatJNI.CkZipEntry_ExtractInto(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ExtractIntoAsync(String var1) {
/* 301 */     long var2 = chilkatJNI.CkZipEntry_ExtractIntoAsync(this.swigCPtr, this, var1);
/* 302 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public CkDateTime GetDt() {
/* 306 */     long var1 = chilkatJNI.CkZipEntry_GetDt(this.swigCPtr, this);
/* 307 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public boolean Inflate(CkByteData var1) {
/* 311 */     return chilkatJNI.CkZipEntry_Inflate(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask InflateAsync() {
/* 315 */     long var1 = chilkatJNI.CkZipEntry_InflateAsync(this.swigCPtr, this);
/* 316 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry NextEntry() {
/* 320 */     long var1 = chilkatJNI.CkZipEntry_NextEntry(this.swigCPtr, this);
/* 321 */     return var1 == 0L ? null : new CkZipEntry(var1, true);
/*     */   }
/*     */   
/*     */   public CkZipEntry NextMatchingEntry(String var1) {
/* 325 */     long var2 = chilkatJNI.CkZipEntry_NextMatchingEntry(this.swigCPtr, this, var1);
/* 326 */     return var2 == 0L ? null : new CkZipEntry(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReplaceData(CkByteData var1) {
/* 330 */     return chilkatJNI.CkZipEntry_ReplaceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ReplaceString(String var1, String var2) {
/* 334 */     return chilkatJNI.CkZipEntry_ReplaceString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 338 */     return chilkatJNI.CkZipEntry_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetDt(CkDateTime var1) {
/* 342 */     chilkatJNI.CkZipEntry_SetDt(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean UnzipToBd(CkBinData var1) {
/* 346 */     return chilkatJNI.CkZipEntry_UnzipToBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask UnzipToBdAsync(CkBinData var1) {
/* 350 */     long var2 = chilkatJNI.CkZipEntry_UnzipToBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 351 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UnzipToSb(int var1, String var2, CkStringBuilder var3) {
/* 355 */     return chilkatJNI.CkZipEntry_UnzipToSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkTask UnzipToSbAsync(int var1, String var2, CkStringBuilder var3) {
/* 359 */     long var4 = chilkatJNI.CkZipEntry_UnzipToSbAsync(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/* 360 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean UnzipToStream(CkStream var1) {
/* 364 */     return chilkatJNI.CkZipEntry_UnzipToStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask UnzipToStreamAsync(CkStream var1) {
/* 368 */     long var2 = chilkatJNI.CkZipEntry_UnzipToStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/* 369 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UnzipToString(int var1, String var2, CkString var3) {
/* 373 */     return chilkatJNI.CkZipEntry_UnzipToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String unzipToString(int var1, String var2) {
/* 377 */     return chilkatJNI.CkZipEntry_unzipToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UnzipToStringAsync(int var1, String var2) {
/* 381 */     long var3 = chilkatJNI.CkZipEntry_UnzipToStringAsync(this.swigCPtr, this, var1, var2);
/* 382 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkZipEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */