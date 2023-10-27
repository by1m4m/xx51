/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkZipCrc
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkZipCrc(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkZipCrc var0) {
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
/*  29 */         chilkatJNI.delete_CkZipCrc(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkZipCrc()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkZipCrc(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkZipCrc_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkZipCrc_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkZipCrc_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkZipCrc_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  58 */     chilkatJNI.CkZipCrc_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  62 */     return chilkatJNI.CkZipCrc_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  66 */     chilkatJNI.CkZipCrc_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  70 */     chilkatJNI.CkZipCrc_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  74 */     return chilkatJNI.CkZipCrc_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  78 */     chilkatJNI.CkZipCrc_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  82 */     return chilkatJNI.CkZipCrc_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  86 */     chilkatJNI.CkZipCrc_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  90 */     return chilkatJNI.CkZipCrc_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  94 */     return chilkatJNI.CkZipCrc_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  98 */     chilkatJNI.CkZipCrc_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 102 */     return chilkatJNI.CkZipCrc_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 106 */     chilkatJNI.CkZipCrc_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 110 */     chilkatJNI.CkZipCrc_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 114 */     return chilkatJNI.CkZipCrc_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void BeginStream() {
/* 118 */     chilkatJNI.CkZipCrc_BeginStream(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public long CalculateCrc(CkByteData var1) {
/* 122 */     return chilkatJNI.CkZipCrc_CalculateCrc(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public long CrcBd(CkBinData var1) {
/* 126 */     return chilkatJNI.CkZipCrc_CrcBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public long CrcSb(CkStringBuilder var1, String var2) {
/* 130 */     return chilkatJNI.CkZipCrc_CrcSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public long CrcString(String var1, String var2) {
/* 134 */     return chilkatJNI.CkZipCrc_CrcString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public long EndStream() {
/* 138 */     return chilkatJNI.CkZipCrc_EndStream(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public long FileCrc(String var1) {
/* 142 */     return chilkatJNI.CkZipCrc_FileCrc(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask FileCrcAsync(String var1) {
/* 146 */     long var2 = chilkatJNI.CkZipCrc_FileCrcAsync(this.swigCPtr, this, var1);
/* 147 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public void MoreData(CkByteData var1) {
/* 151 */     chilkatJNI.CkZipCrc_MoreData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 155 */     return chilkatJNI.CkZipCrc_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToHex(int var1, CkString var2) {
/* 159 */     return chilkatJNI.CkZipCrc_ToHex(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toHex(int var1) {
/* 163 */     return chilkatJNI.CkZipCrc_toHex(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkZipCrc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */