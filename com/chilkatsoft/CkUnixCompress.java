/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkUnixCompress
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkUnixCompress(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkUnixCompress var0) {
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
/*  29 */         chilkatJNI.delete_CkUnixCompress(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkUnixCompress()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkUnixCompress(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkUnixCompress_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkUnixCompress_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkUnixCompress_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkUnixCompress_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkUnixCompress_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkUnixCompress_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkUnixCompress_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkUnixCompress_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkUnixCompress_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  78 */     return chilkatJNI.CkUnixCompress_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  82 */     chilkatJNI.CkUnixCompress_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkUnixCompress_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkUnixCompress_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkUnixCompress_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkUnixCompress_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkUnixCompress_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkUnixCompress_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkUnixCompress_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkUnixCompress_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkUnixCompress_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkUnixCompress_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkUnixCompress_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkUnixCompress_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CompressFile(String var1, String var2) {
/* 134 */     return chilkatJNI.CkUnixCompress_CompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileAsync(String var1, String var2) {
/* 138 */     long var3 = chilkatJNI.CkUnixCompress_CompressFileAsync(this.swigCPtr, this, var1, var2);
/* 139 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFileToMem(String var1, CkByteData var2) {
/* 143 */     return chilkatJNI.CkUnixCompress_CompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileToMemAsync(String var1) {
/* 147 */     long var2 = chilkatJNI.CkUnixCompress_CompressFileToMemAsync(this.swigCPtr, this, var1);
/* 148 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressMemory(CkByteData var1, CkByteData var2) {
/* 152 */     return chilkatJNI.CkUnixCompress_CompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean CompressMemToFile(CkByteData var1, String var2) {
/* 156 */     return chilkatJNI.CkUnixCompress_CompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean CompressString(String var1, String var2, CkByteData var3) {
/* 160 */     return chilkatJNI.CkUnixCompress_CompressString(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean CompressStringToFile(String var1, String var2, String var3) {
/* 164 */     return chilkatJNI.CkUnixCompress_CompressStringToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 168 */     return chilkatJNI.CkUnixCompress_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 172 */     return chilkatJNI.CkUnixCompress_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UncompressFile(String var1, String var2) {
/* 176 */     return chilkatJNI.CkUnixCompress_UncompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileAsync(String var1, String var2) {
/* 180 */     long var3 = chilkatJNI.CkUnixCompress_UncompressFileAsync(this.swigCPtr, this, var1, var2);
/* 181 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFileToMem(String var1, CkByteData var2) {
/* 185 */     return chilkatJNI.CkUnixCompress_UncompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileToMemAsync(String var1) {
/* 189 */     long var2 = chilkatJNI.CkUnixCompress_UncompressFileToMemAsync(this.swigCPtr, this, var1);
/* 190 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFileToString(String var1, String var2, CkString var3) {
/* 194 */     return chilkatJNI.CkUnixCompress_UncompressFileToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String uncompressFileToString(String var1, String var2) {
/* 198 */     return chilkatJNI.CkUnixCompress_uncompressFileToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileToStringAsync(String var1, String var2) {
/* 202 */     long var3 = chilkatJNI.CkUnixCompress_UncompressFileToStringAsync(this.swigCPtr, this, var1, var2);
/* 203 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemory(CkByteData var1, CkByteData var2) {
/* 207 */     return chilkatJNI.CkUnixCompress_UncompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemToFile(CkByteData var1, String var2) {
/* 211 */     return chilkatJNI.CkUnixCompress_UncompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UncompressString(CkByteData var1, String var2, CkString var3) {
/* 215 */     return chilkatJNI.CkUnixCompress_UncompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String uncompressString(CkByteData var1, String var2) {
/* 219 */     return chilkatJNI.CkUnixCompress_uncompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 223 */     return chilkatJNI.CkUnixCompress_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnTarZ(String var1, String var2, boolean var3) {
/* 227 */     return chilkatJNI.CkUnixCompress_UnTarZ(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask UnTarZAsync(String var1, String var2, boolean var3) {
/* 231 */     long var4 = chilkatJNI.CkUnixCompress_UnTarZAsync(this.swigCPtr, this, var1, var2, var3);
/* 232 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkUnixCompress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */