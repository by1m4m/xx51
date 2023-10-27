/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkLog
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkLog(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkLog var0) {
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
/*  29 */         chilkatJNI.delete_CkLog(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkLog()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkLog(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkLog_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkLog_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkLog_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkLog_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkLog_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkLog_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkLog_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkLog_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkLog_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkLog_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkLog_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkLog_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkLog_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkLog_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/*  98 */     return chilkatJNI.CkLog_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 102 */     chilkatJNI.CkLog_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 106 */     chilkatJNI.CkLog_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 110 */     return chilkatJNI.CkLog_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Clear(String var1) {
/* 114 */     chilkatJNI.CkLog_Clear(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void EnterContext(String var1) {
/* 118 */     chilkatJNI.CkLog_EnterContext(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LeaveContext() {
/* 122 */     chilkatJNI.CkLog_LeaveContext(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void LogData(String var1, String var2) {
/* 126 */     chilkatJNI.CkLog_LogData(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void LogDataBase64(String var1, CkByteData var2) {
/* 130 */     chilkatJNI.CkLog_LogDataBase64(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public void LogDataHex(String var1, CkByteData var2) {
/* 134 */     chilkatJNI.CkLog_LogDataHex(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public void LogDataMax(String var1, String var2, int var3) {
/* 138 */     chilkatJNI.CkLog_LogDataMax(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void LogDateTime(String var1, boolean var2) {
/* 142 */     chilkatJNI.CkLog_LogDateTime(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void LogError(String var1) {
/* 146 */     chilkatJNI.CkLog_LogError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LogInfo(String var1) {
/* 150 */     chilkatJNI.CkLog_LogInfo(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LogInt(String var1, int var2) {
/* 154 */     chilkatJNI.CkLog_LogInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void LogInt64(String var1, long var2) {
/* 158 */     chilkatJNI.CkLog_LogInt64(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void LogTimestamp(String var1) {
/* 162 */     chilkatJNI.CkLog_LogTimestamp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 166 */     return chilkatJNI.CkLog_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */