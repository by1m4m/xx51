/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSshKey
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSshKey(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSshKey var0) {
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
/*  29 */         chilkatJNI.delete_CkSshKey(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSshKey()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSshKey(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkSshKey_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkSshKey_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkSshKey_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Comment(CkString var1) {
/*  54 */     chilkatJNI.CkSshKey_get_Comment(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String comment() {
/*  58 */     return chilkatJNI.CkSshKey_comment(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Comment(String var1) {
/*  62 */     chilkatJNI.CkSshKey_put_Comment(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkSshKey_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkSshKey_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkSshKey_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsDsaKey() {
/*  78 */     return chilkatJNI.CkSshKey_get_IsDsaKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsPrivateKey() {
/*  82 */     return chilkatJNI.CkSshKey_get_IsPrivateKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsRsaKey() {
/*  86 */     return chilkatJNI.CkSshKey_get_IsRsaKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkSshKey_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkSshKey_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkSshKey_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkSshKey_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkSshKey_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkSshKey_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkSshKey_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkSshKey_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Password(CkString var1) {
/* 122 */     chilkatJNI.CkSshKey_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String password() {
/* 126 */     return chilkatJNI.CkSshKey_password(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Password(String var1) {
/* 130 */     chilkatJNI.CkSshKey_put_Password(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 134 */     return chilkatJNI.CkSshKey_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 138 */     chilkatJNI.CkSshKey_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 142 */     chilkatJNI.CkSshKey_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 146 */     return chilkatJNI.CkSshKey_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean FromOpenSshPrivateKey(String var1) {
/* 150 */     return chilkatJNI.CkSshKey_FromOpenSshPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromOpenSshPublicKey(String var1) {
/* 154 */     return chilkatJNI.CkSshKey_FromOpenSshPublicKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromPuttyPrivateKey(String var1) {
/* 158 */     return chilkatJNI.CkSshKey_FromPuttyPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromRfc4716PublicKey(String var1) {
/* 162 */     return chilkatJNI.CkSshKey_FromRfc4716PublicKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromXml(String var1) {
/* 166 */     return chilkatJNI.CkSshKey_FromXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenerateDsaKey(int var1) {
/* 170 */     return chilkatJNI.CkSshKey_GenerateDsaKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenerateRsaKey(int var1, int var2) {
/* 174 */     return chilkatJNI.CkSshKey_GenerateRsaKey(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GenFingerprint(CkString var1) {
/* 178 */     return chilkatJNI.CkSshKey_GenFingerprint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String genFingerprint() {
/* 182 */     return chilkatJNI.CkSshKey_genFingerprint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadText(String var1, CkString var2) {
/* 186 */     return chilkatJNI.CkSshKey_LoadText(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String loadText(String var1) {
/* 190 */     return chilkatJNI.CkSshKey_loadText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 194 */     return chilkatJNI.CkSshKey_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveText(String var1, String var2) {
/* 198 */     return chilkatJNI.CkSshKey_SaveText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToOpenSshPrivateKey(boolean var1, CkString var2) {
/* 202 */     return chilkatJNI.CkSshKey_ToOpenSshPrivateKey(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toOpenSshPrivateKey(boolean var1) {
/* 206 */     return chilkatJNI.CkSshKey_toOpenSshPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToOpenSshPublicKey(CkString var1) {
/* 210 */     return chilkatJNI.CkSshKey_ToOpenSshPublicKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toOpenSshPublicKey() {
/* 214 */     return chilkatJNI.CkSshKey_toOpenSshPublicKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToPuttyPrivateKey(boolean var1, CkString var2) {
/* 218 */     return chilkatJNI.CkSshKey_ToPuttyPrivateKey(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toPuttyPrivateKey(boolean var1) {
/* 222 */     return chilkatJNI.CkSshKey_toPuttyPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToRfc4716PublicKey(CkString var1) {
/* 226 */     return chilkatJNI.CkSshKey_ToRfc4716PublicKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toRfc4716PublicKey() {
/* 230 */     return chilkatJNI.CkSshKey_toRfc4716PublicKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToXml(CkString var1) {
/* 234 */     return chilkatJNI.CkSshKey_ToXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toXml() {
/* 238 */     return chilkatJNI.CkSshKey_toXml(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSshKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */