/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkFileAccess
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkFileAccess(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkFileAccess var0) {
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
/*  29 */         chilkatJNI.delete_CkFileAccess(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkFileAccess()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkFileAccess(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkFileAccess_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkFileAccess_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkFileAccess_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_CurrentDir(CkString var1) {
/*  54 */     chilkatJNI.CkFileAccess_get_CurrentDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String currentDir() {
/*  58 */     return chilkatJNI.CkFileAccess_currentDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  62 */     chilkatJNI.CkFileAccess_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  66 */     return chilkatJNI.CkFileAccess_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  70 */     chilkatJNI.CkFileAccess_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EndOfFile() {
/*  74 */     return chilkatJNI.CkFileAccess_get_EndOfFile(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_FileOpenError() {
/*  78 */     return chilkatJNI.CkFileAccess_get_FileOpenError(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FileOpenErrorMsg(CkString var1) {
/*  82 */     chilkatJNI.CkFileAccess_get_FileOpenErrorMsg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileOpenErrorMsg() {
/*  86 */     return chilkatJNI.CkFileAccess_fileOpenErrorMsg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkFileAccess_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkFileAccess_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkFileAccess_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkFileAccess_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkFileAccess_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkFileAccess_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkFileAccess_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkFileAccess_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 122 */     return chilkatJNI.CkFileAccess_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 126 */     chilkatJNI.CkFileAccess_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 130 */     chilkatJNI.CkFileAccess_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 134 */     return chilkatJNI.CkFileAccess_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendAnsi(String var1) {
/* 138 */     return chilkatJNI.CkFileAccess_AppendAnsi(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendText(String var1, String var2) {
/* 142 */     return chilkatJNI.CkFileAccess_AppendText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendUnicodeBOM() {
/* 146 */     return chilkatJNI.CkFileAccess_AppendUnicodeBOM(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendUtf8BOM() {
/* 150 */     return chilkatJNI.CkFileAccess_AppendUtf8BOM(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DirAutoCreate(String var1) {
/* 154 */     return chilkatJNI.CkFileAccess_DirAutoCreate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DirCreate(String var1) {
/* 158 */     return chilkatJNI.CkFileAccess_DirCreate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DirDelete(String var1) {
/* 162 */     return chilkatJNI.CkFileAccess_DirDelete(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DirEnsureExists(String var1) {
/* 166 */     return chilkatJNI.CkFileAccess_DirEnsureExists(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void FileClose() {
/* 170 */     chilkatJNI.CkFileAccess_FileClose(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean FileContentsEqual(String var1, String var2) {
/* 174 */     return chilkatJNI.CkFileAccess_FileContentsEqual(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FileCopy(String var1, String var2, boolean var3) {
/* 178 */     return chilkatJNI.CkFileAccess_FileCopy(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean FileDelete(String var1) {
/* 182 */     return chilkatJNI.CkFileAccess_FileDelete(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FileExists(String var1) {
/* 186 */     return chilkatJNI.CkFileAccess_FileExists(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int FileExists3(String var1) {
/* 190 */     return chilkatJNI.CkFileAccess_FileExists3(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FileOpen(String var1, long var2, long var4, long var6, long var8) {
/* 194 */     return chilkatJNI.CkFileAccess_FileOpen(this.swigCPtr, this, var1, var2, var4, var6, var8);
/*     */   }
/*     */   
/*     */   public boolean FileRead(int var1, CkByteData var2) {
/* 198 */     return chilkatJNI.CkFileAccess_FileRead(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean FileReadBd(int var1, CkBinData var2) {
/* 202 */     return chilkatJNI.CkFileAccess_FileReadBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean FileRename(String var1, String var2) {
/* 206 */     return chilkatJNI.CkFileAccess_FileRename(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FileSeek(int var1, int var2) {
/* 210 */     return chilkatJNI.CkFileAccess_FileSeek(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int FileSize(String var1) {
/* 214 */     return chilkatJNI.CkFileAccess_FileSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FileWrite(CkByteData var1) {
/* 218 */     return chilkatJNI.CkFileAccess_FileWrite(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean FileWriteBd(CkBinData var1, int var2, int var3) {
/* 222 */     return chilkatJNI.CkFileAccess_FileWriteBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GenBlockId(int var1, int var2, String var3, CkString var4) {
/* 226 */     return chilkatJNI.CkFileAccess_GenBlockId(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String genBlockId(int var1, int var2, String var3) {
/* 230 */     return chilkatJNI.CkFileAccess_genBlockId(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetDirectoryName(String var1, CkString var2) {
/* 234 */     return chilkatJNI.CkFileAccess_GetDirectoryName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getDirectoryName(String var1) {
/* 238 */     return chilkatJNI.CkFileAccess_getDirectoryName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String directoryName(String var1) {
/* 242 */     return chilkatJNI.CkFileAccess_directoryName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetExtension(String var1, CkString var2) {
/* 246 */     return chilkatJNI.CkFileAccess_GetExtension(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getExtension(String var1) {
/* 250 */     return chilkatJNI.CkFileAccess_getExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String extension(String var1) {
/* 254 */     return chilkatJNI.CkFileAccess_extension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetFileName(String var1, CkString var2) {
/* 258 */     return chilkatJNI.CkFileAccess_GetFileName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getFileName(String var1) {
/* 262 */     return chilkatJNI.CkFileAccess_getFileName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String fileName(String var1) {
/* 266 */     return chilkatJNI.CkFileAccess_fileName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetFileNameWithoutExtension(String var1, CkString var2) {
/* 270 */     return chilkatJNI.CkFileAccess_GetFileNameWithoutExtension(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getFileNameWithoutExtension(String var1) {
/* 274 */     return chilkatJNI.CkFileAccess_getFileNameWithoutExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String fileNameWithoutExtension(String var1) {
/* 278 */     return chilkatJNI.CkFileAccess_fileNameWithoutExtension(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkDateTime GetFileTime(String var1, int var2) {
/* 282 */     long var3 = chilkatJNI.CkFileAccess_GetFileTime(this.swigCPtr, this, var1, var2);
/* 283 */     return var3 == 0L ? null : new CkDateTime(var3, true);
/*     */   }
/*     */   
/*     */   public CkDateTime GetLastModified(String var1) {
/* 287 */     long var2 = chilkatJNI.CkFileAccess_GetLastModified(this.swigCPtr, this, var1);
/* 288 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*     */   }
/*     */   
/*     */   public int GetNumBlocks(int var1) {
/* 292 */     return chilkatJNI.CkFileAccess_GetNumBlocks(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetTempFilename(String var1, String var2, CkString var3) {
/* 296 */     return chilkatJNI.CkFileAccess_GetTempFilename(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getTempFilename(String var1, String var2) {
/* 300 */     return chilkatJNI.CkFileAccess_getTempFilename(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String tempFilename(String var1, String var2) {
/* 304 */     return chilkatJNI.CkFileAccess_tempFilename(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OpenForAppend(String var1) {
/* 308 */     return chilkatJNI.CkFileAccess_OpenForAppend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenForRead(String var1) {
/* 312 */     return chilkatJNI.CkFileAccess_OpenForRead(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenForReadWrite(String var1) {
/* 316 */     return chilkatJNI.CkFileAccess_OpenForReadWrite(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenForWrite(String var1) {
/* 320 */     return chilkatJNI.CkFileAccess_OpenForWrite(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ReadBinaryToEncoded(String var1, String var2, CkString var3) {
/* 324 */     return chilkatJNI.CkFileAccess_ReadBinaryToEncoded(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readBinaryToEncoded(String var1, String var2) {
/* 328 */     return chilkatJNI.CkFileAccess_readBinaryToEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ReadBlock(int var1, int var2, CkByteData var3) {
/* 332 */     return chilkatJNI.CkFileAccess_ReadBlock(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean ReadEntireFile(String var1, CkByteData var2) {
/* 336 */     return chilkatJNI.CkFileAccess_ReadEntireFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ReadEntireTextFile(String var1, String var2, CkString var3) {
/* 340 */     return chilkatJNI.CkFileAccess_ReadEntireTextFile(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readEntireTextFile(String var1, String var2) {
/* 344 */     return chilkatJNI.CkFileAccess_readEntireTextFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ReassembleFile(String var1, String var2, String var3, String var4) {
/* 348 */     return chilkatJNI.CkFileAccess_ReassembleFile(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public int ReplaceStrings(String var1, String var2, String var3, String var4) {
/* 352 */     return chilkatJNI.CkFileAccess_ReplaceStrings(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 356 */     return chilkatJNI.CkFileAccess_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetCurrentDir(String var1) {
/* 360 */     return chilkatJNI.CkFileAccess_SetCurrentDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetFileTimes(String var1, CkDateTime var2, CkDateTime var3, CkDateTime var4) {
/* 364 */     return chilkatJNI.CkFileAccess_SetFileTimes(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2, CkDateTime.getCPtr(var3), var3, CkDateTime.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public boolean SetLastModified(String var1, CkDateTime var2) {
/* 368 */     return chilkatJNI.CkFileAccess_SetLastModified(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SplitFile(String var1, String var2, String var3, int var4, String var5) {
/* 372 */     return chilkatJNI.CkFileAccess_SplitFile(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean TreeDelete(String var1) {
/* 376 */     return chilkatJNI.CkFileAccess_TreeDelete(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean WriteEntireFile(String var1, CkByteData var2) {
/* 380 */     return chilkatJNI.CkFileAccess_WriteEntireFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean WriteEntireTextFile(String var1, String var2, String var3, boolean var4) {
/* 384 */     return chilkatJNI.CkFileAccess_WriteEntireTextFile(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkFileAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */