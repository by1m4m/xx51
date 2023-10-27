/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJwe
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJwe(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJwe var0) {
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
/*  29 */         chilkatJNI.delete_CkJwe(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJwe()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJwe(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJwe_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJwe_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJwe_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkJwe_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkJwe_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkJwe_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkJwe_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkJwe_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkJwe_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkJwe_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkJwe_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkJwe_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkJwe_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkJwe_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumRecipients() {
/*  98 */     return chilkatJNI.CkJwe_get_NumRecipients(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PreferCompact() {
/* 102 */     return chilkatJNI.CkJwe_get_PreferCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferCompact(boolean var1) {
/* 106 */     chilkatJNI.CkJwe_put_PreferCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferFlattened() {
/* 110 */     return chilkatJNI.CkJwe_get_PreferFlattened(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferFlattened(boolean var1) {
/* 114 */     chilkatJNI.CkJwe_put_PreferFlattened(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkJwe_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkJwe_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkJwe_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkJwe_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Decrypt(int var1, String var2, CkString var3) {
/* 134 */     return chilkatJNI.CkJwe_Decrypt(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String decrypt(int var1, String var2) {
/* 138 */     return chilkatJNI.CkJwe_decrypt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean DecryptBd(int var1, CkBinData var2) {
/* 142 */     return chilkatJNI.CkJwe_DecryptBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean DecryptSb(int var1, String var2, CkStringBuilder var3) {
/* 146 */     return chilkatJNI.CkJwe_DecryptSb(this.swigCPtr, this, var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean Encrypt(String var1, String var2, CkString var3) {
/* 150 */     return chilkatJNI.CkJwe_Encrypt(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String encrypt(String var1, String var2) {
/* 154 */     return chilkatJNI.CkJwe_encrypt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean EncryptBd(CkBinData var1, CkStringBuilder var2) {
/* 158 */     return chilkatJNI.CkJwe_EncryptBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean EncryptSb(CkStringBuilder var1, String var2, CkStringBuilder var3) {
/* 162 */     return chilkatJNI.CkJwe_EncryptSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, CkStringBuilder.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public int FindRecipient(String var1, String var2, boolean var3) {
/* 166 */     return chilkatJNI.CkJwe_FindRecipient(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadJwe(String var1) {
/* 170 */     return chilkatJNI.CkJwe_LoadJwe(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadJweSb(CkStringBuilder var1) {
/* 174 */     return chilkatJNI.CkJwe_LoadJweSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 178 */     return chilkatJNI.CkJwe_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetAad(String var1, String var2) {
/* 182 */     return chilkatJNI.CkJwe_SetAad(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetAadBd(CkBinData var1) {
/* 186 */     return chilkatJNI.CkJwe_SetAadBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetPassword(int var1, String var2) {
/* 190 */     return chilkatJNI.CkJwe_SetPassword(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetPrivateKey(int var1, CkPrivateKey var2) {
/* 194 */     return chilkatJNI.CkJwe_SetPrivateKey(this.swigCPtr, this, var1, CkPrivateKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetProtectedHeader(CkJsonObject var1) {
/* 198 */     return chilkatJNI.CkJwe_SetProtectedHeader(this.swigCPtr, this, CkJsonObject.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetPublicKey(int var1, CkPublicKey var2) {
/* 202 */     return chilkatJNI.CkJwe_SetPublicKey(this.swigCPtr, this, var1, CkPublicKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetRecipientHeader(int var1, CkJsonObject var2) {
/* 206 */     return chilkatJNI.CkJwe_SetRecipientHeader(this.swigCPtr, this, var1, CkJsonObject.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetUnprotectedHeader(CkJsonObject var1) {
/* 210 */     return chilkatJNI.CkJwe_SetUnprotectedHeader(this.swigCPtr, this, CkJsonObject.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetWrappingKey(int var1, String var2, String var3) {
/* 214 */     return chilkatJNI.CkJwe_SetWrappingKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJwe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */