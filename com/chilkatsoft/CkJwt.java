/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJwt
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJwt(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJwt var0) {
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
/*  29 */         chilkatJNI.delete_CkJwt(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJwt()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJwt(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJwt_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJwt_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJwt_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AutoCompact() {
/*  54 */     return chilkatJNI.CkJwt_get_AutoCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoCompact(boolean var1) {
/*  58 */     chilkatJNI.CkJwt_put_AutoCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  62 */     chilkatJNI.CkJwt_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  66 */     return chilkatJNI.CkJwt_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  70 */     chilkatJNI.CkJwt_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  74 */     chilkatJNI.CkJwt_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  78 */     return chilkatJNI.CkJwt_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  82 */     chilkatJNI.CkJwt_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  86 */     return chilkatJNI.CkJwt_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  90 */     chilkatJNI.CkJwt_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  94 */     return chilkatJNI.CkJwt_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  98 */     return chilkatJNI.CkJwt_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 102 */     chilkatJNI.CkJwt_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 106 */     return chilkatJNI.CkJwt_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 110 */     chilkatJNI.CkJwt_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 114 */     chilkatJNI.CkJwt_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 118 */     return chilkatJNI.CkJwt_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CreateJwt(String var1, String var2, String var3, CkString var4) {
/* 122 */     return chilkatJNI.CkJwt_CreateJwt(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String createJwt(String var1, String var2, String var3) {
/* 126 */     return chilkatJNI.CkJwt_createJwt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean CreateJwtPk(String var1, String var2, CkPrivateKey var3, CkString var4) {
/* 130 */     return chilkatJNI.CkJwt_CreateJwtPk(this.swigCPtr, this, var1, var2, CkPrivateKey.getCPtr(var3), var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String createJwtPk(String var1, String var2, CkPrivateKey var3) {
/* 134 */     return chilkatJNI.CkJwt_createJwtPk(this.swigCPtr, this, var1, var2, CkPrivateKey.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public int GenNumericDate(int var1) {
/* 138 */     return chilkatJNI.CkJwt_GenNumericDate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeader(String var1, CkString var2) {
/* 142 */     return chilkatJNI.CkJwt_GetHeader(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeader(String var1) {
/* 146 */     return chilkatJNI.CkJwt_getHeader(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String header(String var1) {
/* 150 */     return chilkatJNI.CkJwt_header(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPayload(String var1, CkString var2) {
/* 154 */     return chilkatJNI.CkJwt_GetPayload(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPayload(String var1) {
/* 158 */     return chilkatJNI.CkJwt_getPayload(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String payload(String var1) {
/* 162 */     return chilkatJNI.CkJwt_payload(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsTimeValid(String var1, int var2) {
/* 166 */     return chilkatJNI.CkJwt_IsTimeValid(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 170 */     return chilkatJNI.CkJwt_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyJwt(String var1, String var2) {
/* 174 */     return chilkatJNI.CkJwt_VerifyJwt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean VerifyJwtPk(String var1, CkPublicKey var2) {
/* 178 */     return chilkatJNI.CkJwt_VerifyJwtPk(this.swigCPtr, this, var1, CkPublicKey.getCPtr(var2), var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJwt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */