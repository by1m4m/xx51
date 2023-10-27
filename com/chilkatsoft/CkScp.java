/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkScp
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkScp(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkScp var0) {
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
/*  29 */         chilkatJNI.delete_CkScp(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkScp()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkScp(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkScp_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkScp_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkScp_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkScp_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkScp_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkScp_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkScp_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkScp_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkScp_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  78 */     return chilkatJNI.CkScp_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  82 */     chilkatJNI.CkScp_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkScp_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkScp_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkScp_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkScp_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkScp_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkScp_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkScp_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkScp_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_PercentDoneScale() {
/* 118 */     return chilkatJNI.CkScp_get_PercentDoneScale(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PercentDoneScale(int var1) {
/* 122 */     chilkatJNI.CkScp_put_PercentDoneScale(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SyncedFiles(CkString var1) {
/* 126 */     chilkatJNI.CkScp_get_SyncedFiles(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String syncedFiles() {
/* 130 */     return chilkatJNI.CkScp_syncedFiles(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SyncedFiles(String var1) {
/* 134 */     chilkatJNI.CkScp_put_SyncedFiles(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SyncMustMatch(CkString var1) {
/* 138 */     chilkatJNI.CkScp_get_SyncMustMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String syncMustMatch() {
/* 142 */     return chilkatJNI.CkScp_syncMustMatch(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SyncMustMatch(String var1) {
/* 146 */     chilkatJNI.CkScp_put_SyncMustMatch(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SyncMustMatchDir(CkString var1) {
/* 150 */     chilkatJNI.CkScp_get_SyncMustMatchDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String syncMustMatchDir() {
/* 154 */     return chilkatJNI.CkScp_syncMustMatchDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SyncMustMatchDir(String var1) {
/* 158 */     chilkatJNI.CkScp_put_SyncMustMatchDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SyncMustNotMatch(CkString var1) {
/* 162 */     chilkatJNI.CkScp_get_SyncMustNotMatch(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String syncMustNotMatch() {
/* 166 */     return chilkatJNI.CkScp_syncMustNotMatch(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SyncMustNotMatch(String var1) {
/* 170 */     chilkatJNI.CkScp_put_SyncMustNotMatch(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SyncMustNotMatchDir(CkString var1) {
/* 174 */     chilkatJNI.CkScp_get_SyncMustNotMatchDir(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String syncMustNotMatchDir() {
/* 178 */     return chilkatJNI.CkScp_syncMustNotMatchDir(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SyncMustNotMatchDir(String var1) {
/* 182 */     chilkatJNI.CkScp_put_SyncMustNotMatchDir(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 186 */     return chilkatJNI.CkScp_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 190 */     chilkatJNI.CkScp_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 194 */     chilkatJNI.CkScp_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 198 */     return chilkatJNI.CkScp_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DownloadBinary(String var1, CkByteData var2) {
/* 202 */     return chilkatJNI.CkScp_DownloadBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask DownloadBinaryAsync(String var1) {
/* 206 */     long var2 = chilkatJNI.CkScp_DownloadBinaryAsync(this.swigCPtr, this, var1);
/* 207 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DownloadBinaryEncoded(String var1, String var2, CkString var3) {
/* 211 */     return chilkatJNI.CkScp_DownloadBinaryEncoded(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String downloadBinaryEncoded(String var1, String var2) {
/* 215 */     return chilkatJNI.CkScp_downloadBinaryEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask DownloadBinaryEncodedAsync(String var1, String var2) {
/* 219 */     long var3 = chilkatJNI.CkScp_DownloadBinaryEncodedAsync(this.swigCPtr, this, var1, var2);
/* 220 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean DownloadFile(String var1, String var2) {
/* 224 */     return chilkatJNI.CkScp_DownloadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask DownloadFileAsync(String var1, String var2) {
/* 228 */     long var3 = chilkatJNI.CkScp_DownloadFileAsync(this.swigCPtr, this, var1, var2);
/* 229 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean DownloadString(String var1, String var2, CkString var3) {
/* 233 */     return chilkatJNI.CkScp_DownloadString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String downloadString(String var1, String var2) {
/* 237 */     return chilkatJNI.CkScp_downloadString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask DownloadStringAsync(String var1, String var2) {
/* 241 */     long var3 = chilkatJNI.CkScp_DownloadStringAsync(this.swigCPtr, this, var1, var2);
/* 242 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 246 */     return chilkatJNI.CkScp_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SyncTreeDownload(String var1, String var2, int var3, boolean var4) {
/* 250 */     return chilkatJNI.CkScp_SyncTreeDownload(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkTask SyncTreeDownloadAsync(String var1, String var2, int var3, boolean var4) {
/* 254 */     long var5 = chilkatJNI.CkScp_SyncTreeDownloadAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 255 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean SyncTreeUpload(String var1, String var2, int var3, boolean var4) {
/* 259 */     return chilkatJNI.CkScp_SyncTreeUpload(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkTask SyncTreeUploadAsync(String var1, String var2, int var3, boolean var4) {
/* 263 */     long var5 = chilkatJNI.CkScp_SyncTreeUploadAsync(this.swigCPtr, this, var1, var2, var3, var4);
/* 264 */     return var5 == 0L ? null : new CkTask(var5, true);
/*     */   }
/*     */   
/*     */   public boolean UploadBinary(String var1, CkByteData var2) {
/* 268 */     return chilkatJNI.CkScp_UploadBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UploadBinaryAsync(String var1, CkByteData var2) {
/* 272 */     long var3 = chilkatJNI.CkScp_UploadBinaryAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 273 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UploadBinaryEncoded(String var1, String var2, String var3) {
/* 277 */     return chilkatJNI.CkScp_UploadBinaryEncoded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UploadBinaryEncodedAsync(String var1, String var2, String var3) {
/* 281 */     long var4 = chilkatJNI.CkScp_UploadBinaryEncodedAsync(this.swigCPtr, this, var1, var2, var3);
/* 282 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean UploadFile(String var1, String var2) {
/* 286 */     return chilkatJNI.CkScp_UploadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UploadFileAsync(String var1, String var2) {
/* 290 */     long var3 = chilkatJNI.CkScp_UploadFileAsync(this.swigCPtr, this, var1, var2);
/* 291 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UploadString(String var1, String var2, String var3) {
/* 295 */     return chilkatJNI.CkScp_UploadString(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UploadStringAsync(String var1, String var2, String var3) {
/* 299 */     long var4 = chilkatJNI.CkScp_UploadStringAsync(this.swigCPtr, this, var1, var2, var3);
/* 300 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean UseSsh(CkSsh var1) {
/* 304 */     return chilkatJNI.CkScp_UseSsh(this.swigCPtr, this, CkSsh.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkScp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */