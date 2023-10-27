/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkXmlDSig
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkXmlDSig(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkXmlDSig var0) {
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
/*  29 */         chilkatJNI.delete_CkXmlDSig(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkXmlDSig()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkXmlDSig(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkXmlDSig_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkXmlDSig_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkXmlDSig_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkXmlDSig_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkXmlDSig_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkXmlDSig_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkXmlDSig_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkXmlDSig_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkXmlDSig_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkXmlDSig_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkXmlDSig_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkXmlDSig_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkXmlDSig_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkXmlDSig_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumReferences() {
/*  98 */     return chilkatJNI.CkXmlDSig_get_NumReferences(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumSignatures() {
/* 102 */     return chilkatJNI.CkXmlDSig_get_NumSignatures(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Selector() {
/* 106 */     return chilkatJNI.CkXmlDSig_get_Selector(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Selector(int var1) {
/* 110 */     chilkatJNI.CkXmlDSig_put_Selector(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 114 */     return chilkatJNI.CkXmlDSig_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 118 */     chilkatJNI.CkXmlDSig_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 122 */     chilkatJNI.CkXmlDSig_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 126 */     return chilkatJNI.CkXmlDSig_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_WithComments() {
/* 130 */     return chilkatJNI.CkXmlDSig_get_WithComments(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WithComments(boolean var1) {
/* 134 */     chilkatJNI.CkXmlDSig_put_WithComments(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CanonicalizeFragment(String var1, String var2, String var3, String var4, boolean var5, CkString var6) {
/* 138 */     return chilkatJNI.CkXmlDSig_CanonicalizeFragment(this.swigCPtr, this, var1, var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*     */   }
/*     */   
/*     */   public String canonicalizeFragment(String var1, String var2, String var3, String var4, boolean var5) {
/* 142 */     return chilkatJNI.CkXmlDSig_canonicalizeFragment(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean CanonicalizeXml(String var1, String var2, boolean var3, CkString var4) {
/* 146 */     return chilkatJNI.CkXmlDSig_CanonicalizeXml(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String canonicalizeXml(String var1, String var2, boolean var3) {
/* 150 */     return chilkatJNI.CkXmlDSig_canonicalizeXml(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkXml GetKeyInfo() {
/* 154 */     long var1 = chilkatJNI.CkXmlDSig_GetKeyInfo(this.swigCPtr, this);
/* 155 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public CkPublicKey GetPublicKey() {
/* 159 */     long var1 = chilkatJNI.CkXmlDSig_GetPublicKey(this.swigCPtr, this);
/* 160 */     return var1 == 0L ? null : new CkPublicKey(var1, true);
/*     */   }
/*     */   
/*     */   public boolean IsReferenceExternal(int var1) {
/* 164 */     return chilkatJNI.CkXmlDSig_IsReferenceExternal(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadSignature(String var1) {
/* 168 */     return chilkatJNI.CkXmlDSig_LoadSignature(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadSignatureBd(CkBinData var1) {
/* 172 */     return chilkatJNI.CkXmlDSig_LoadSignatureBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadSignatureSb(CkStringBuilder var1) {
/* 176 */     return chilkatJNI.CkXmlDSig_LoadSignatureSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ReferenceUri(int var1, CkString var2) {
/* 180 */     return chilkatJNI.CkXmlDSig_ReferenceUri(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String referenceUri(int var1) {
/* 184 */     return chilkatJNI.CkXmlDSig_referenceUri(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 188 */     return chilkatJNI.CkXmlDSig_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetHmacKey(String var1, String var2) {
/* 192 */     return chilkatJNI.CkXmlDSig_SetHmacKey(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetPublicKey(CkPublicKey var1) {
/* 196 */     return chilkatJNI.CkXmlDSig_SetPublicKey(this.swigCPtr, this, CkPublicKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetRefDataBd(int var1, CkBinData var2) {
/* 200 */     return chilkatJNI.CkXmlDSig_SetRefDataBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetRefDataFile(int var1, String var2) {
/* 204 */     return chilkatJNI.CkXmlDSig_SetRefDataFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetRefDataSb(int var1, CkStringBuilder var2, String var3) {
/* 208 */     return chilkatJNI.CkXmlDSig_SetRefDataSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3);
/*     */   }
/*     */   
/*     */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 212 */     return chilkatJNI.CkXmlDSig_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyReferenceDigest(int var1) {
/* 216 */     return chilkatJNI.CkXmlDSig_VerifyReferenceDigest(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifySignature(boolean var1) {
/* 220 */     return chilkatJNI.CkXmlDSig_VerifySignature(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkXmlDSig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */