/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAuthAws
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAuthAws(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAuthAws var0) {
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
/*  29 */         chilkatJNI.delete_CkAuthAws(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAuthAws()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAuthAws(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAuthAws_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAuthAws_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAuthAws_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessKey(CkString var1) {
/*  54 */     chilkatJNI.CkAuthAws_get_AccessKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessKey() {
/*  58 */     return chilkatJNI.CkAuthAws_accessKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessKey(String var1) {
/*  62 */     chilkatJNI.CkAuthAws_put_AccessKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CanonicalizedResourceV2(CkString var1) {
/*  66 */     chilkatJNI.CkAuthAws_get_CanonicalizedResourceV2(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String canonicalizedResourceV2() {
/*  70 */     return chilkatJNI.CkAuthAws_canonicalizedResourceV2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CanonicalizedResourceV2(String var1) {
/*  74 */     chilkatJNI.CkAuthAws_put_CanonicalizedResourceV2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkAuthAws_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkAuthAws_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkAuthAws_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkAuthAws_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkAuthAws_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkAuthAws_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkAuthAws_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkAuthAws_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkAuthAws_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkAuthAws_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkAuthAws_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PrecomputedMd5(CkString var1) {
/* 122 */     chilkatJNI.CkAuthAws_get_PrecomputedMd5(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String precomputedMd5() {
/* 126 */     return chilkatJNI.CkAuthAws_precomputedMd5(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PrecomputedMd5(String var1) {
/* 130 */     chilkatJNI.CkAuthAws_put_PrecomputedMd5(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PrecomputedSha256(CkString var1) {
/* 134 */     chilkatJNI.CkAuthAws_get_PrecomputedSha256(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String precomputedSha256() {
/* 138 */     return chilkatJNI.CkAuthAws_precomputedSha256(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PrecomputedSha256(String var1) {
/* 142 */     chilkatJNI.CkAuthAws_put_PrecomputedSha256(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Region(CkString var1) {
/* 146 */     chilkatJNI.CkAuthAws_get_Region(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String region() {
/* 150 */     return chilkatJNI.CkAuthAws_region(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Region(String var1) {
/* 154 */     chilkatJNI.CkAuthAws_put_Region(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SecretKey(CkString var1) {
/* 158 */     chilkatJNI.CkAuthAws_get_SecretKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String secretKey() {
/* 162 */     return chilkatJNI.CkAuthAws_secretKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SecretKey(String var1) {
/* 166 */     chilkatJNI.CkAuthAws_put_SecretKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ServiceName(CkString var1) {
/* 170 */     chilkatJNI.CkAuthAws_get_ServiceName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serviceName() {
/* 174 */     return chilkatJNI.CkAuthAws_serviceName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ServiceName(String var1) {
/* 178 */     chilkatJNI.CkAuthAws_put_ServiceName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SignatureVersion() {
/* 182 */     return chilkatJNI.CkAuthAws_get_SignatureVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignatureVersion(int var1) {
/* 186 */     chilkatJNI.CkAuthAws_put_SignatureVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 190 */     return chilkatJNI.CkAuthAws_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 194 */     chilkatJNI.CkAuthAws_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 198 */     chilkatJNI.CkAuthAws_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 202 */     return chilkatJNI.CkAuthAws_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 206 */     return chilkatJNI.CkAuthAws_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAuthAws.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */