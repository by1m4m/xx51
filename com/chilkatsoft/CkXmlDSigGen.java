/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkXmlDSigGen
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkXmlDSigGen(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkXmlDSigGen var0) {
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
/*  29 */         chilkatJNI.delete_CkXmlDSigGen(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkXmlDSigGen()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkXmlDSigGen(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkXmlDSigGen_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkXmlDSigGen_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkXmlDSigGen_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Behaviors(CkString var1) {
/*  54 */     chilkatJNI.CkXmlDSigGen_get_Behaviors(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String behaviors() {
/*  58 */     return chilkatJNI.CkXmlDSigGen_behaviors(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Behaviors(String var1) {
/*  62 */     chilkatJNI.CkXmlDSigGen_put_Behaviors(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CustomKeyInfoXml(CkString var1) {
/*  66 */     chilkatJNI.CkXmlDSigGen_get_CustomKeyInfoXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String customKeyInfoXml() {
/*  70 */     return chilkatJNI.CkXmlDSigGen_customKeyInfoXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CustomKeyInfoXml(String var1) {
/*  74 */     chilkatJNI.CkXmlDSigGen_put_CustomKeyInfoXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkXmlDSigGen_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkXmlDSigGen_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkXmlDSigGen_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_IncNamespacePrefix(CkString var1) {
/*  90 */     chilkatJNI.CkXmlDSigGen_get_IncNamespacePrefix(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String incNamespacePrefix() {
/*  94 */     return chilkatJNI.CkXmlDSigGen_incNamespacePrefix(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IncNamespacePrefix(String var1) {
/*  98 */     chilkatJNI.CkXmlDSigGen_put_IncNamespacePrefix(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_IncNamespaceUri(CkString var1) {
/* 102 */     chilkatJNI.CkXmlDSigGen_get_IncNamespaceUri(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String incNamespaceUri() {
/* 106 */     return chilkatJNI.CkXmlDSigGen_incNamespaceUri(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IncNamespaceUri(String var1) {
/* 110 */     chilkatJNI.CkXmlDSigGen_put_IncNamespaceUri(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_KeyInfoId(CkString var1) {
/* 114 */     chilkatJNI.CkXmlDSigGen_get_KeyInfoId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyInfoId() {
/* 118 */     return chilkatJNI.CkXmlDSigGen_keyInfoId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeyInfoId(String var1) {
/* 122 */     chilkatJNI.CkXmlDSigGen_put_KeyInfoId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_KeyInfoKeyName(CkString var1) {
/* 126 */     chilkatJNI.CkXmlDSigGen_get_KeyInfoKeyName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyInfoKeyName() {
/* 130 */     return chilkatJNI.CkXmlDSigGen_keyInfoKeyName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeyInfoKeyName(String var1) {
/* 134 */     chilkatJNI.CkXmlDSigGen_put_KeyInfoKeyName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_KeyInfoType(CkString var1) {
/* 138 */     chilkatJNI.CkXmlDSigGen_get_KeyInfoType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyInfoType() {
/* 142 */     return chilkatJNI.CkXmlDSigGen_keyInfoType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeyInfoType(String var1) {
/* 146 */     chilkatJNI.CkXmlDSigGen_put_KeyInfoType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 150 */     chilkatJNI.CkXmlDSigGen_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 154 */     return chilkatJNI.CkXmlDSigGen_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 158 */     chilkatJNI.CkXmlDSigGen_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 162 */     return chilkatJNI.CkXmlDSigGen_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 166 */     chilkatJNI.CkXmlDSigGen_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 170 */     return chilkatJNI.CkXmlDSigGen_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 174 */     return chilkatJNI.CkXmlDSigGen_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 178 */     chilkatJNI.CkXmlDSigGen_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigId(CkString var1) {
/* 182 */     chilkatJNI.CkXmlDSigGen_get_SigId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sigId() {
/* 186 */     return chilkatJNI.CkXmlDSigGen_sigId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigId(String var1) {
/* 190 */     chilkatJNI.CkXmlDSigGen_put_SigId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigLocation(CkString var1) {
/* 194 */     chilkatJNI.CkXmlDSigGen_get_SigLocation(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sigLocation() {
/* 198 */     return chilkatJNI.CkXmlDSigGen_sigLocation(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigLocation(String var1) {
/* 202 */     chilkatJNI.CkXmlDSigGen_put_SigLocation(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigNamespacePrefix(CkString var1) {
/* 206 */     chilkatJNI.CkXmlDSigGen_get_SigNamespacePrefix(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sigNamespacePrefix() {
/* 210 */     return chilkatJNI.CkXmlDSigGen_sigNamespacePrefix(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigNamespacePrefix(String var1) {
/* 214 */     chilkatJNI.CkXmlDSigGen_put_SigNamespacePrefix(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigNamespaceUri(CkString var1) {
/* 218 */     chilkatJNI.CkXmlDSigGen_get_SigNamespaceUri(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sigNamespaceUri() {
/* 222 */     return chilkatJNI.CkXmlDSigGen_sigNamespaceUri(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigNamespaceUri(String var1) {
/* 226 */     chilkatJNI.CkXmlDSigGen_put_SigNamespaceUri(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SignedInfoCanonAlg(CkString var1) {
/* 230 */     chilkatJNI.CkXmlDSigGen_get_SignedInfoCanonAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signedInfoCanonAlg() {
/* 234 */     return chilkatJNI.CkXmlDSigGen_signedInfoCanonAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignedInfoCanonAlg(String var1) {
/* 238 */     chilkatJNI.CkXmlDSigGen_put_SignedInfoCanonAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SignedInfoDigestMethod(CkString var1) {
/* 242 */     chilkatJNI.CkXmlDSigGen_get_SignedInfoDigestMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signedInfoDigestMethod() {
/* 246 */     return chilkatJNI.CkXmlDSigGen_signedInfoDigestMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignedInfoDigestMethod(String var1) {
/* 250 */     chilkatJNI.CkXmlDSigGen_put_SignedInfoDigestMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SignedInfoId(CkString var1) {
/* 254 */     chilkatJNI.CkXmlDSigGen_get_SignedInfoId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signedInfoId() {
/* 258 */     return chilkatJNI.CkXmlDSigGen_signedInfoId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignedInfoId(String var1) {
/* 262 */     chilkatJNI.CkXmlDSigGen_put_SignedInfoId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SignedInfoPrefixList(CkString var1) {
/* 266 */     chilkatJNI.CkXmlDSigGen_get_SignedInfoPrefixList(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signedInfoPrefixList() {
/* 270 */     return chilkatJNI.CkXmlDSigGen_signedInfoPrefixList(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignedInfoPrefixList(String var1) {
/* 274 */     chilkatJNI.CkXmlDSigGen_put_SignedInfoPrefixList(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigningAlg(CkString var1) {
/* 278 */     chilkatJNI.CkXmlDSigGen_get_SigningAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signingAlg() {
/* 282 */     return chilkatJNI.CkXmlDSigGen_signingAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigningAlg(String var1) {
/* 286 */     chilkatJNI.CkXmlDSigGen_put_SigningAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigValueId(CkString var1) {
/* 290 */     chilkatJNI.CkXmlDSigGen_get_SigValueId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sigValueId() {
/* 294 */     return chilkatJNI.CkXmlDSigGen_sigValueId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigValueId(String var1) {
/* 298 */     chilkatJNI.CkXmlDSigGen_put_SigValueId(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 302 */     return chilkatJNI.CkXmlDSigGen_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 306 */     chilkatJNI.CkXmlDSigGen_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 310 */     chilkatJNI.CkXmlDSigGen_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 314 */     return chilkatJNI.CkXmlDSigGen_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_X509Type(CkString var1) {
/* 318 */     chilkatJNI.CkXmlDSigGen_get_X509Type(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String x509Type() {
/* 322 */     return chilkatJNI.CkXmlDSigGen_x509Type(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_X509Type(String var1) {
/* 326 */     chilkatJNI.CkXmlDSigGen_put_X509Type(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddEnvelopedRef(String var1, CkStringBuilder var2, String var3, String var4, String var5) {
/* 330 */     return chilkatJNI.CkXmlDSigGen_AddEnvelopedRef(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddExternalBinaryRef(String var1, CkBinData var2, String var3, String var4) {
/* 334 */     return chilkatJNI.CkXmlDSigGen_AddExternalBinaryRef(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddExternalFileRef(String var1, String var2, String var3, String var4) {
/* 338 */     return chilkatJNI.CkXmlDSigGen_AddExternalFileRef(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddExternalTextRef(String var1, CkStringBuilder var2, String var3, boolean var4, String var5, String var6) {
/* 342 */     return chilkatJNI.CkXmlDSigGen_AddExternalTextRef(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3, var4, var5, var6);
/*     */   }
/*     */   
/*     */   public boolean AddExternalXmlRef(String var1, CkStringBuilder var2, String var3, String var4, String var5) {
/* 346 */     return chilkatJNI.CkXmlDSigGen_AddExternalXmlRef(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddObject(String var1, String var2, String var3, String var4) {
/* 350 */     return chilkatJNI.CkXmlDSigGen_AddObject(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AddObjectRef(String var1, String var2, String var3, String var4, String var5) {
/* 354 */     return chilkatJNI.CkXmlDSigGen_AddObjectRef(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddSameDocRef(String var1, String var2, String var3, String var4, String var5) {
/* 358 */     return chilkatJNI.CkXmlDSigGen_AddSameDocRef(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddSignatureNamespace(String var1, String var2) {
/* 362 */     return chilkatJNI.CkXmlDSigGen_AddSignatureNamespace(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ConstructSignedInfo(CkStringBuilder var1, CkString var2) {
/* 366 */     return chilkatJNI.CkXmlDSigGen_ConstructSignedInfo(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String constructSignedInfo(CkStringBuilder var1) {
/* 370 */     return chilkatJNI.CkXmlDSigGen_constructSignedInfo(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean CreateXmlDSig(String var1, CkString var2) {
/* 374 */     return chilkatJNI.CkXmlDSigGen_CreateXmlDSig(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String createXmlDSig(String var1) {
/* 378 */     return chilkatJNI.CkXmlDSigGen_createXmlDSig(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CreateXmlDSigSb(CkStringBuilder var1) {
/* 382 */     return chilkatJNI.CkXmlDSigGen_CreateXmlDSigSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 386 */     return chilkatJNI.CkXmlDSigGen_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetHmacKey(String var1, String var2) {
/* 390 */     return chilkatJNI.CkXmlDSigGen_SetHmacKey(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetPrivateKey(CkPrivateKey var1) {
/* 394 */     return chilkatJNI.CkXmlDSigGen_SetPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetRefIdAttr(String var1, String var2) {
/* 398 */     return chilkatJNI.CkXmlDSigGen_SetRefIdAttr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetX509Cert(CkCert var1, boolean var2) {
/* 402 */     return chilkatJNI.CkXmlDSigGen_SetX509Cert(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkXmlDSigGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */