/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCert
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCert(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCert var0) {
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
/*  29 */         chilkatJNI.delete_CkCert(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCert()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCert(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCert_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCert_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCert_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AuthorityKeyId(CkString var1) {
/*  54 */     chilkatJNI.CkCert_get_AuthorityKeyId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String authorityKeyId() {
/*  58 */     return chilkatJNI.CkCert_authorityKeyId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_AvoidWindowsPkAccess() {
/*  62 */     return chilkatJNI.CkCert_get_AvoidWindowsPkAccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AvoidWindowsPkAccess(boolean var1) {
/*  66 */     chilkatJNI.CkCert_put_AvoidWindowsPkAccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_CertVersion() {
/*  70 */     return chilkatJNI.CkCert_get_CertVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_CspName(CkString var1) {
/*  74 */     chilkatJNI.CkCert_get_CspName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String cspName() {
/*  78 */     return chilkatJNI.CkCert_cspName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  82 */     chilkatJNI.CkCert_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  86 */     return chilkatJNI.CkCert_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  90 */     chilkatJNI.CkCert_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Expired() {
/*  94 */     return chilkatJNI.CkCert_get_Expired(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ForClientAuthentication() {
/*  98 */     return chilkatJNI.CkCert_get_ForClientAuthentication(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ForCodeSigning() {
/* 102 */     return chilkatJNI.CkCert_get_ForCodeSigning(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ForSecureEmail() {
/* 106 */     return chilkatJNI.CkCert_get_ForSecureEmail(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ForServerAuthentication() {
/* 110 */     return chilkatJNI.CkCert_get_ForServerAuthentication(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ForTimeStamping() {
/* 114 */     return chilkatJNI.CkCert_get_ForTimeStamping(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_HasKeyContainer() {
/* 118 */     return chilkatJNI.CkCert_get_HasKeyContainer(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public long get_IntendedKeyUsage() {
/* 122 */     return chilkatJNI.CkCert_get_IntendedKeyUsage(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsRoot() {
/* 126 */     return chilkatJNI.CkCert_get_IsRoot(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerC(CkString var1) {
/* 130 */     chilkatJNI.CkCert_get_IssuerC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerC() {
/* 134 */     return chilkatJNI.CkCert_issuerC(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerCN(CkString var1) {
/* 138 */     chilkatJNI.CkCert_get_IssuerCN(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerCN() {
/* 142 */     return chilkatJNI.CkCert_issuerCN(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerDN(CkString var1) {
/* 146 */     chilkatJNI.CkCert_get_IssuerDN(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerDN() {
/* 150 */     return chilkatJNI.CkCert_issuerDN(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerE(CkString var1) {
/* 154 */     chilkatJNI.CkCert_get_IssuerE(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerE() {
/* 158 */     return chilkatJNI.CkCert_issuerE(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerL(CkString var1) {
/* 162 */     chilkatJNI.CkCert_get_IssuerL(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerL() {
/* 166 */     return chilkatJNI.CkCert_issuerL(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerO(CkString var1) {
/* 170 */     chilkatJNI.CkCert_get_IssuerO(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerO() {
/* 174 */     return chilkatJNI.CkCert_issuerO(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerOU(CkString var1) {
/* 178 */     chilkatJNI.CkCert_get_IssuerOU(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerOU() {
/* 182 */     return chilkatJNI.CkCert_issuerOU(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_IssuerS(CkString var1) {
/* 186 */     chilkatJNI.CkCert_get_IssuerS(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String issuerS() {
/* 190 */     return chilkatJNI.CkCert_issuerS(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_KeyContainerName(CkString var1) {
/* 194 */     chilkatJNI.CkCert_get_KeyContainerName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyContainerName() {
/* 198 */     return chilkatJNI.CkCert_keyContainerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 202 */     chilkatJNI.CkCert_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 206 */     return chilkatJNI.CkCert_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 210 */     chilkatJNI.CkCert_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 214 */     return chilkatJNI.CkCert_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 218 */     chilkatJNI.CkCert_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 222 */     return chilkatJNI.CkCert_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 226 */     return chilkatJNI.CkCert_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 230 */     chilkatJNI.CkCert_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_MachineKeyset() {
/* 234 */     return chilkatJNI.CkCert_get_MachineKeyset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_OcspUrl(CkString var1) {
/* 238 */     chilkatJNI.CkCert_get_OcspUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String ocspUrl() {
/* 242 */     return chilkatJNI.CkCert_ocspUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PrivateKeyExportable() {
/* 246 */     return chilkatJNI.CkCert_get_PrivateKeyExportable(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Revoked() {
/* 250 */     return chilkatJNI.CkCert_get_Revoked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Rfc822Name(CkString var1) {
/* 254 */     chilkatJNI.CkCert_get_Rfc822Name(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String rfc822Name() {
/* 258 */     return chilkatJNI.CkCert_rfc822Name(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_SelfSigned() {
/* 262 */     return chilkatJNI.CkCert_get_SelfSigned(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SerialDecimal(CkString var1) {
/* 266 */     chilkatJNI.CkCert_get_SerialDecimal(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serialDecimal() {
/* 270 */     return chilkatJNI.CkCert_serialDecimal(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SerialNumber(CkString var1) {
/* 274 */     chilkatJNI.CkCert_get_SerialNumber(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serialNumber() {
/* 278 */     return chilkatJNI.CkCert_serialNumber(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Sha1Thumbprint(CkString var1) {
/* 282 */     chilkatJNI.CkCert_get_Sha1Thumbprint(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sha1Thumbprint() {
/* 286 */     return chilkatJNI.CkCert_sha1Thumbprint(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_SignatureVerified() {
/* 290 */     return chilkatJNI.CkCert_get_SignatureVerified(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Silent() {
/* 294 */     return chilkatJNI.CkCert_get_Silent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SmartCardPin(CkString var1) {
/* 298 */     chilkatJNI.CkCert_get_SmartCardPin(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String smartCardPin() {
/* 302 */     return chilkatJNI.CkCert_smartCardPin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SmartCardPin(String var1) {
/* 306 */     chilkatJNI.CkCert_put_SmartCardPin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SubjectC(CkString var1) {
/* 310 */     chilkatJNI.CkCert_get_SubjectC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectC() {
/* 314 */     return chilkatJNI.CkCert_subjectC(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectCN(CkString var1) {
/* 318 */     chilkatJNI.CkCert_get_SubjectCN(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectCN() {
/* 322 */     return chilkatJNI.CkCert_subjectCN(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectDN(CkString var1) {
/* 326 */     chilkatJNI.CkCert_get_SubjectDN(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectDN() {
/* 330 */     return chilkatJNI.CkCert_subjectDN(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectE(CkString var1) {
/* 334 */     chilkatJNI.CkCert_get_SubjectE(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectE() {
/* 338 */     return chilkatJNI.CkCert_subjectE(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectKeyId(CkString var1) {
/* 342 */     chilkatJNI.CkCert_get_SubjectKeyId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectKeyId() {
/* 346 */     return chilkatJNI.CkCert_subjectKeyId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectL(CkString var1) {
/* 350 */     chilkatJNI.CkCert_get_SubjectL(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectL() {
/* 354 */     return chilkatJNI.CkCert_subjectL(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectO(CkString var1) {
/* 358 */     chilkatJNI.CkCert_get_SubjectO(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectO() {
/* 362 */     return chilkatJNI.CkCert_subjectO(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectOU(CkString var1) {
/* 366 */     chilkatJNI.CkCert_get_SubjectOU(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectOU() {
/* 370 */     return chilkatJNI.CkCert_subjectOU(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SubjectS(CkString var1) {
/* 374 */     chilkatJNI.CkCert_get_SubjectS(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subjectS() {
/* 378 */     return chilkatJNI.CkCert_subjectS(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_TrustedRoot() {
/* 382 */     return chilkatJNI.CkCert_get_TrustedRoot(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ValidFrom(SYSTEMTIME var1) {
/* 386 */     chilkatJNI.CkCert_get_ValidFrom(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_ValidFromStr(CkString var1) {
/* 390 */     chilkatJNI.CkCert_get_ValidFromStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String validFromStr() {
/* 394 */     return chilkatJNI.CkCert_validFromStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ValidTo(SYSTEMTIME var1) {
/* 398 */     chilkatJNI.CkCert_get_ValidTo(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_ValidToStr(CkString var1) {
/* 402 */     chilkatJNI.CkCert_get_ValidToStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String validToStr() {
/* 406 */     return chilkatJNI.CkCert_validToStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 410 */     return chilkatJNI.CkCert_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 414 */     chilkatJNI.CkCert_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 418 */     chilkatJNI.CkCert_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 422 */     return chilkatJNI.CkCert_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int CheckRevoked() {
/* 426 */     return chilkatJNI.CkCert_CheckRevoked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ExportCertDer(CkByteData var1) {
/* 430 */     return chilkatJNI.CkCert_ExportCertDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ExportCertDerBd(CkBinData var1) {
/* 434 */     return chilkatJNI.CkCert_ExportCertDerBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ExportCertDerFile(String var1) {
/* 438 */     return chilkatJNI.CkCert_ExportCertDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExportCertPem(CkString var1) {
/* 442 */     return chilkatJNI.CkCert_ExportCertPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exportCertPem() {
/* 446 */     return chilkatJNI.CkCert_exportCertPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ExportCertPemFile(String var1) {
/* 450 */     return chilkatJNI.CkCert_ExportCertPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExportCertXml(CkString var1) {
/* 454 */     return chilkatJNI.CkCert_ExportCertXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exportCertXml() {
/* 458 */     return chilkatJNI.CkCert_exportCertXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPrivateKey ExportPrivateKey() {
/* 462 */     long var1 = chilkatJNI.CkCert_ExportPrivateKey(this.swigCPtr, this);
/* 463 */     return var1 == 0L ? null : new CkPrivateKey(var1, true);
/*     */   }
/*     */   
/*     */   public CkPublicKey ExportPublicKey() {
/* 467 */     long var1 = chilkatJNI.CkCert_ExportPublicKey(this.swigCPtr, this);
/* 468 */     return var1 == 0L ? null : new CkPublicKey(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ExportToPfxBd(String var1, boolean var2, CkBinData var3) {
/* 472 */     return chilkatJNI.CkCert_ExportToPfxBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean ExportToPfxData(String var1, boolean var2, CkByteData var3) {
/* 476 */     return chilkatJNI.CkCert_ExportToPfxData(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean ExportToPfxFile(String var1, String var2, boolean var3) {
/* 480 */     return chilkatJNI.CkCert_ExportToPfxFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkCert FindIssuer() {
/* 484 */     long var1 = chilkatJNI.CkCert_FindIssuer(this.swigCPtr, this);
/* 485 */     return var1 == 0L ? null : new CkCert(var1, true);
/*     */   }
/*     */   
/*     */   public CkCertChain GetCertChain() {
/* 489 */     long var1 = chilkatJNI.CkCert_GetCertChain(this.swigCPtr, this);
/* 490 */     return var1 == 0L ? null : new CkCertChain(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetEncoded(CkString var1) {
/* 494 */     return chilkatJNI.CkCert_GetEncoded(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getEncoded() {
/* 498 */     return chilkatJNI.CkCert_getEncoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String encoded() {
/* 502 */     return chilkatJNI.CkCert_encoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetExtensionAsXml(String var1, CkString var2) {
/* 506 */     return chilkatJNI.CkCert_GetExtensionAsXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getExtensionAsXml(String var1) {
/* 510 */     return chilkatJNI.CkCert_getExtensionAsXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String extensionAsXml(String var1) {
/* 514 */     return chilkatJNI.CkCert_extensionAsXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPrivateKeyPem(CkString var1) {
/* 518 */     return chilkatJNI.CkCert_GetPrivateKeyPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getPrivateKeyPem() {
/* 522 */     return chilkatJNI.CkCert_getPrivateKeyPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String privateKeyPem() {
/* 526 */     return chilkatJNI.CkCert_privateKeyPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetSpkiFingerprint(String var1, String var2, CkString var3) {
/* 530 */     return chilkatJNI.CkCert_GetSpkiFingerprint(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getSpkiFingerprint(String var1, String var2) {
/* 534 */     return chilkatJNI.CkCert_getSpkiFingerprint(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String spkiFingerprint(String var1, String var2) {
/* 538 */     return chilkatJNI.CkCert_spkiFingerprint(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkDateTime GetValidFromDt() {
/* 542 */     long var1 = chilkatJNI.CkCert_GetValidFromDt(this.swigCPtr, this);
/* 543 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public CkDateTime GetValidToDt() {
/* 547 */     long var1 = chilkatJNI.CkCert_GetValidToDt(this.swigCPtr, this);
/* 548 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public boolean HashOf(String var1, String var2, String var3, CkString var4) {
/* 552 */     return chilkatJNI.CkCert_HashOf(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String hashOf(String var1, String var2, String var3) {
/* 556 */     return chilkatJNI.CkCert_hashOf(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean HasPrivateKey() {
/* 560 */     return chilkatJNI.CkCert_HasPrivateKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LinkPrivateKey(String var1, boolean var2, boolean var3) {
/* 564 */     return chilkatJNI.CkCert_LinkPrivateKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadByCommonName(String var1) {
/* 568 */     return chilkatJNI.CkCert_LoadByCommonName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadByEmailAddress(String var1) {
/* 572 */     return chilkatJNI.CkCert_LoadByEmailAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadByIssuerAndSerialNumber(String var1, String var2) {
/* 576 */     return chilkatJNI.CkCert_LoadByIssuerAndSerialNumber(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadFromBase64(String var1) {
/* 580 */     return chilkatJNI.CkCert_LoadFromBase64(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromBd(CkBinData var1) {
/* 584 */     return chilkatJNI.CkCert_LoadFromBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromBinary(CkByteData var1) {
/* 588 */     return chilkatJNI.CkCert_LoadFromBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromFile(String var1) {
/* 592 */     return chilkatJNI.CkCert_LoadFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPem(String var1) {
/* 596 */     return chilkatJNI.CkCert_LoadPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxBd(CkBinData var1, String var2) {
/* 600 */     return chilkatJNI.CkCert_LoadPfxBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxData(CkByteData var1, String var2) {
/* 604 */     return chilkatJNI.CkCert_LoadPfxData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxFile(String var1, String var2) {
/* 608 */     return chilkatJNI.CkCert_LoadPfxFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 612 */     return chilkatJNI.CkCert_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean PemFileToDerFile(String var1, String var2) {
/* 616 */     return chilkatJNI.CkCert_PemFileToDerFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 620 */     return chilkatJNI.CkCert_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveToFile(String var1) {
/* 624 */     return chilkatJNI.CkCert_SaveToFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetFromEncoded(String var1) {
/* 628 */     return chilkatJNI.CkCert_SetFromEncoded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetPrivateKey(CkPrivateKey var1) {
/* 632 */     return chilkatJNI.CkCert_SetPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetPrivateKeyPem(String var1) {
/* 636 */     return chilkatJNI.CkCert_SetPrivateKeyPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 640 */     return chilkatJNI.CkCert_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean VerifySignature() {
/* 644 */     return chilkatJNI.CkCert_VerifySignature(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */