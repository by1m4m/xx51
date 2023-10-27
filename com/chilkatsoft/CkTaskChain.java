/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkTaskChain
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkTaskChain(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkTaskChain var0) {
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
/*  29 */         chilkatJNI.delete_CkTaskChain(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkTaskChain()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkTaskChain(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkTaskChain_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkTaskChain_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkTaskChain_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkTaskChain_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkTaskChain_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkTaskChain_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Finished() {
/*  66 */     return chilkatJNI.CkTaskChain_get_Finished(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  70 */     return chilkatJNI.CkTaskChain_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  74 */     chilkatJNI.CkTaskChain_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Inert() {
/*  78 */     return chilkatJNI.CkTaskChain_get_Inert(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  82 */     chilkatJNI.CkTaskChain_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  86 */     return chilkatJNI.CkTaskChain_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  90 */     chilkatJNI.CkTaskChain_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  94 */     return chilkatJNI.CkTaskChain_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  98 */     chilkatJNI.CkTaskChain_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 102 */     return chilkatJNI.CkTaskChain_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 106 */     return chilkatJNI.CkTaskChain_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 110 */     chilkatJNI.CkTaskChain_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Live() {
/* 114 */     return chilkatJNI.CkTaskChain_get_Live(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumTasks() {
/* 118 */     return chilkatJNI.CkTaskChain_get_NumTasks(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Status(CkString var1) {
/* 122 */     chilkatJNI.CkTaskChain_get_Status(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String status() {
/* 126 */     return chilkatJNI.CkTaskChain_status(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_StatusInt() {
/* 130 */     return chilkatJNI.CkTaskChain_get_StatusInt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_StopOnFailedTask() {
/* 134 */     return chilkatJNI.CkTaskChain_get_StopOnFailedTask(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StopOnFailedTask(boolean var1) {
/* 138 */     chilkatJNI.CkTaskChain_put_StopOnFailedTask(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 142 */     return chilkatJNI.CkTaskChain_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 146 */     chilkatJNI.CkTaskChain_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 150 */     chilkatJNI.CkTaskChain_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 154 */     return chilkatJNI.CkTaskChain_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Append(CkTask var1) {
/* 158 */     return chilkatJNI.CkTaskChain_Append(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean Cancel() {
/* 162 */     return chilkatJNI.CkTaskChain_Cancel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask GetTask(int var1) {
/* 166 */     long var2 = chilkatJNI.CkTaskChain_GetTask(this.swigCPtr, this, var1);
/* 167 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean Run() {
/* 171 */     return chilkatJNI.CkTaskChain_Run(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RunSynchronously() {
/* 175 */     return chilkatJNI.CkTaskChain_RunSynchronously(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 179 */     return chilkatJNI.CkTaskChain_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SleepMs(int var1) {
/* 183 */     chilkatJNI.CkTaskChain_SleepMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Wait(int var1) {
/* 187 */     return chilkatJNI.CkTaskChain_Wait(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkTaskChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */