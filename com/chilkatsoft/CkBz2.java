/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkBz2
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkBz2(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkBz2 var0) {
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
/*  29 */         chilkatJNI.delete_CkBz2(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkBz2()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkBz2(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkBz2_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkBz2_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkBz2_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkBz2_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkBz2_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkBz2_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkBz2_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkBz2_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkBz2_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  78 */     return chilkatJNI.CkBz2_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  82 */     chilkatJNI.CkBz2_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkBz2_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkBz2_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkBz2_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkBz2_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkBz2_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkBz2_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkBz2_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkBz2_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkBz2_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkBz2_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkBz2_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkBz2_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CompressFile(String var1, String var2) {
/* 134 */     return chilkatJNI.CkBz2_CompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileAsync(String var1, String var2) {
/* 138 */     long var3 = chilkatJNI.CkBz2_CompressFileAsync(this.swigCPtr, this, var1, var2);
/* 139 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFileToMem(String var1, CkByteData var2) {
/* 143 */     return chilkatJNI.CkBz2_CompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileToMemAsync(String var1) {
/* 147 */     long var2 = chilkatJNI.CkBz2_CompressFileToMemAsync(this.swigCPtr, this, var1);
/* 148 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressMemory(CkByteData var1, CkByteData var2) {
/* 152 */     return chilkatJNI.CkBz2_CompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressMemoryAsync(CkByteData var1) {
/* 156 */     long var2 = chilkatJNI.CkBz2_CompressMemoryAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 157 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressMemToFile(CkByteData var1, String var2) {
/* 161 */     return chilkatJNI.CkBz2_CompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressMemToFileAsync(CkByteData var1, String var2) {
/* 165 */     long var3 = chilkatJNI.CkBz2_CompressMemToFileAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/* 166 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 170 */     return chilkatJNI.CkBz2_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UncompressFile(String var1, String var2) {
/* 174 */     return chilkatJNI.CkBz2_UncompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileAsync(String var1, String var2) {
/* 178 */     long var3 = chilkatJNI.CkBz2_UncompressFileAsync(this.swigCPtr, this, var1, var2);
/* 179 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressFileToMem(String var1, CkByteData var2) {
/* 183 */     return chilkatJNI.CkBz2_UncompressFileToMem(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressFileToMemAsync(String var1) {
/* 187 */     long var2 = chilkatJNI.CkBz2_UncompressFileToMemAsync(this.swigCPtr, this, var1);
/* 188 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemory(CkByteData var1, CkByteData var2) {
/* 192 */     return chilkatJNI.CkBz2_UncompressMemory(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressMemoryAsync(CkByteData var1) {
/* 196 */     long var2 = chilkatJNI.CkBz2_UncompressMemoryAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 197 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UncompressMemToFile(CkByteData var1, String var2) {
/* 201 */     return chilkatJNI.CkBz2_UncompressMemToFile(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask UncompressMemToFileAsync(CkByteData var1, String var2) {
/* 205 */     long var3 = chilkatJNI.CkBz2_UncompressMemToFileAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/* 206 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 210 */     return chilkatJNI.CkBz2_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkBz2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */