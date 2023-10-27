/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkTask
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkTask(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkTask var0) {
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
/*  29 */         chilkatJNI.delete_CkTask(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkTask()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkTask(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkTask_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkTask_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkTask_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkTask_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkTask_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkTask_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Finished() {
/*  66 */     return chilkatJNI.CkTask_get_Finished(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  70 */     return chilkatJNI.CkTask_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  74 */     chilkatJNI.CkTask_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Inert() {
/*  78 */     return chilkatJNI.CkTask_get_Inert(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_KeepProgressLog() {
/*  82 */     return chilkatJNI.CkTask_get_KeepProgressLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeepProgressLog(boolean var1) {
/*  86 */     chilkatJNI.CkTask_put_KeepProgressLog(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkTask_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkTask_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkTask_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkTask_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkTask_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkTask_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkTask_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkTask_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Live() {
/* 122 */     return chilkatJNI.CkTask_get_Live(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_PercentDone() {
/* 126 */     return chilkatJNI.CkTask_get_PercentDone(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ProgressLogSize() {
/* 130 */     return chilkatJNI.CkTask_get_ProgressLogSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ResultErrorText(CkString var1) {
/* 134 */     chilkatJNI.CkTask_get_ResultErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String resultErrorText() {
/* 138 */     return chilkatJNI.CkTask_resultErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ResultType(CkString var1) {
/* 142 */     chilkatJNI.CkTask_get_ResultType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String resultType() {
/* 146 */     return chilkatJNI.CkTask_resultType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Status(CkString var1) {
/* 150 */     chilkatJNI.CkTask_get_Status(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String status() {
/* 154 */     return chilkatJNI.CkTask_status(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_StatusInt() {
/* 158 */     return chilkatJNI.CkTask_get_StatusInt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_TaskId() {
/* 162 */     return chilkatJNI.CkTask_get_TaskId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_TaskSuccess() {
/* 166 */     return chilkatJNI.CkTask_get_TaskSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_UserData(CkString var1) {
/* 170 */     chilkatJNI.CkTask_get_UserData(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String userData() {
/* 174 */     return chilkatJNI.CkTask_userData(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserData(String var1) {
/* 178 */     chilkatJNI.CkTask_put_UserData(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 182 */     return chilkatJNI.CkTask_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 186 */     chilkatJNI.CkTask_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 190 */     chilkatJNI.CkTask_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 194 */     return chilkatJNI.CkTask_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Cancel() {
/* 198 */     return chilkatJNI.CkTask_Cancel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearProgressLog() {
/* 202 */     chilkatJNI.CkTask_ClearProgressLog(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CopyResultBytes(CkByteData var1) {
/* 206 */     return chilkatJNI.CkTask_CopyResultBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetResultBool() {
/* 210 */     return chilkatJNI.CkTask_GetResultBool(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetResultBytes(CkByteData var1) {
/* 214 */     return chilkatJNI.CkTask_GetResultBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int GetResultInt() {
/* 218 */     return chilkatJNI.CkTask_GetResultInt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetResultString(CkString var1) {
/* 222 */     return chilkatJNI.CkTask_GetResultString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getResultString() {
/* 226 */     return chilkatJNI.CkTask_getResultString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String resultString() {
/* 230 */     return chilkatJNI.CkTask_resultString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ProgressInfoName(int var1, CkString var2) {
/* 234 */     return chilkatJNI.CkTask_ProgressInfoName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String progressInfoName(int var1) {
/* 238 */     return chilkatJNI.CkTask_progressInfoName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ProgressInfoValue(int var1, CkString var2) {
/* 242 */     return chilkatJNI.CkTask_ProgressInfoValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String progressInfoValue(int var1) {
/* 246 */     return chilkatJNI.CkTask_progressInfoValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveProgressInfo(int var1) {
/* 250 */     chilkatJNI.CkTask_RemoveProgressInfo(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Run() {
/* 254 */     return chilkatJNI.CkTask_Run(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RunSynchronously() {
/* 258 */     return chilkatJNI.CkTask_RunSynchronously(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 262 */     return chilkatJNI.CkTask_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SleepMs(int var1) {
/* 266 */     chilkatJNI.CkTask_SleepMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Wait(int var1) {
/* 270 */     return chilkatJNI.CkTask_Wait(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */