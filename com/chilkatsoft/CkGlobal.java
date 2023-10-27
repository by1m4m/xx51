/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkGlobal
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkGlobal(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkGlobal var0) {
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
/*  29 */         chilkatJNI.delete_CkGlobal(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkGlobal()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkGlobal(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkGlobal_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkGlobal_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkGlobal_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int get_AnsiCodePage() {
/*  54 */     return chilkatJNI.CkGlobal_get_AnsiCodePage(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AnsiCodePage(int var1) {
/*  58 */     chilkatJNI.CkGlobal_put_AnsiCodePage(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  62 */     chilkatJNI.CkGlobal_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  66 */     return chilkatJNI.CkGlobal_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  70 */     chilkatJNI.CkGlobal_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DefaultNtlmVersion() {
/*  74 */     return chilkatJNI.CkGlobal_get_DefaultNtlmVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DefaultNtlmVersion(int var1) {
/*  78 */     chilkatJNI.CkGlobal_put_DefaultNtlmVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DefaultUtf8() {
/*  82 */     return chilkatJNI.CkGlobal_get_DefaultUtf8(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DefaultUtf8(boolean var1) {
/*  86 */     chilkatJNI.CkGlobal_put_DefaultUtf8(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DnsTimeToLive() {
/*  90 */     return chilkatJNI.CkGlobal_get_DnsTimeToLive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DnsTimeToLive(int var1) {
/*  94 */     chilkatJNI.CkGlobal_put_DnsTimeToLive(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EnableDnsCaching() {
/*  98 */     return chilkatJNI.CkGlobal_get_EnableDnsCaching(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EnableDnsCaching(boolean var1) {
/* 102 */     chilkatJNI.CkGlobal_put_EnableDnsCaching(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 106 */     chilkatJNI.CkGlobal_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 110 */     return chilkatJNI.CkGlobal_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 114 */     chilkatJNI.CkGlobal_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 118 */     return chilkatJNI.CkGlobal_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 122 */     chilkatJNI.CkGlobal_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 126 */     return chilkatJNI.CkGlobal_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 130 */     return chilkatJNI.CkGlobal_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 134 */     chilkatJNI.CkGlobal_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_MaxThreads() {
/* 138 */     return chilkatJNI.CkGlobal_get_MaxThreads(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MaxThreads(int var1) {
/* 142 */     chilkatJNI.CkGlobal_put_MaxThreads(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferIpv6() {
/* 146 */     return chilkatJNI.CkGlobal_get_PreferIpv6(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferIpv6(boolean var1) {
/* 150 */     chilkatJNI.CkGlobal_put_PreferIpv6(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ThreadPoolLogPath(CkString var1) {
/* 154 */     chilkatJNI.CkGlobal_get_ThreadPoolLogPath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String threadPoolLogPath() {
/* 158 */     return chilkatJNI.CkGlobal_threadPoolLogPath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ThreadPoolLogPath(String var1) {
/* 162 */     chilkatJNI.CkGlobal_put_ThreadPoolLogPath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_UnlockStatus() {
/* 166 */     return chilkatJNI.CkGlobal_get_UnlockStatus(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_UsePkcsConstructedEncoding() {
/* 170 */     return chilkatJNI.CkGlobal_get_UsePkcsConstructedEncoding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UsePkcsConstructedEncoding(boolean var1) {
/* 174 */     chilkatJNI.CkGlobal_put_UsePkcsConstructedEncoding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 178 */     return chilkatJNI.CkGlobal_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 182 */     chilkatJNI.CkGlobal_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 186 */     chilkatJNI.CkGlobal_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 190 */     return chilkatJNI.CkGlobal_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DnsClearCache() {
/* 194 */     return chilkatJNI.CkGlobal_DnsClearCache(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean FinalizeThreadPool() {
/* 198 */     return chilkatJNI.CkGlobal_FinalizeThreadPool(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 202 */     return chilkatJNI.CkGlobal_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockBundle(String var1) {
/* 206 */     return chilkatJNI.CkGlobal_UnlockBundle(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */