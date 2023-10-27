/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkMime
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkMime(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkMime var0) {
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
/*  29 */         chilkatJNI.delete_CkMime(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkMime()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkMime(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkMime_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkMime_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkMime_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Boundary(CkString var1) {
/*  54 */     chilkatJNI.CkMime_get_Boundary(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String boundary() {
/*  58 */     return chilkatJNI.CkMime_boundary(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Boundary(String var1) {
/*  62 */     chilkatJNI.CkMime_put_Boundary(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  66 */     chilkatJNI.CkMime_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  70 */     return chilkatJNI.CkMime_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Charset(String var1) {
/*  74 */     chilkatJNI.CkMime_put_Charset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ContentType(CkString var1) {
/*  78 */     chilkatJNI.CkMime_get_ContentType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String contentType() {
/*  82 */     return chilkatJNI.CkMime_contentType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ContentType(String var1) {
/*  86 */     chilkatJNI.CkMime_put_ContentType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CurrentDateTime(CkString var1) {
/*  90 */     chilkatJNI.CkMime_get_CurrentDateTime(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String currentDateTime() {
/*  94 */     return chilkatJNI.CkMime_currentDateTime(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  98 */     chilkatJNI.CkMime_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 102 */     return chilkatJNI.CkMime_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 106 */     chilkatJNI.CkMime_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Disposition(CkString var1) {
/* 110 */     chilkatJNI.CkMime_get_Disposition(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String disposition() {
/* 114 */     return chilkatJNI.CkMime_disposition(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Disposition(String var1) {
/* 118 */     chilkatJNI.CkMime_put_Disposition(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Encoding(CkString var1) {
/* 122 */     chilkatJNI.CkMime_get_Encoding(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encoding() {
/* 126 */     return chilkatJNI.CkMime_encoding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Encoding(String var1) {
/* 130 */     chilkatJNI.CkMime_put_Encoding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Filename(CkString var1) {
/* 134 */     chilkatJNI.CkMime_get_Filename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String filename() {
/* 138 */     return chilkatJNI.CkMime_filename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Filename(String var1) {
/* 142 */     chilkatJNI.CkMime_put_Filename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 146 */     chilkatJNI.CkMime_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 150 */     return chilkatJNI.CkMime_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 154 */     chilkatJNI.CkMime_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 158 */     return chilkatJNI.CkMime_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 162 */     chilkatJNI.CkMime_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 166 */     return chilkatJNI.CkMime_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 170 */     return chilkatJNI.CkMime_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 174 */     chilkatJNI.CkMime_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Micalg(CkString var1) {
/* 178 */     chilkatJNI.CkMime_get_Micalg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String micalg() {
/* 182 */     return chilkatJNI.CkMime_micalg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Micalg(String var1) {
/* 186 */     chilkatJNI.CkMime_put_Micalg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Name(CkString var1) {
/* 190 */     chilkatJNI.CkMime_get_Name(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String name() {
/* 194 */     return chilkatJNI.CkMime_name(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Name(String var1) {
/* 198 */     chilkatJNI.CkMime_put_Name(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumEncryptCerts() {
/* 202 */     return chilkatJNI.CkMime_get_NumEncryptCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumHeaderFields() {
/* 206 */     return chilkatJNI.CkMime_get_NumHeaderFields(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumParts() {
/* 210 */     return chilkatJNI.CkMime_get_NumParts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumSignerCerts() {
/* 214 */     return chilkatJNI.CkMime_get_NumSignerCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_OaepHash(CkString var1) {
/* 218 */     chilkatJNI.CkMime_get_OaepHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oaepHash() {
/* 222 */     return chilkatJNI.CkMime_oaepHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepHash(String var1) {
/* 226 */     chilkatJNI.CkMime_put_OaepHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_OaepMgfHash(CkString var1) {
/* 230 */     chilkatJNI.CkMime_get_OaepMgfHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oaepMgfHash() {
/* 234 */     return chilkatJNI.CkMime_oaepMgfHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepMgfHash(String var1) {
/* 238 */     chilkatJNI.CkMime_put_OaepMgfHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_OaepPadding() {
/* 242 */     return chilkatJNI.CkMime_get_OaepPadding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepPadding(boolean var1) {
/* 246 */     chilkatJNI.CkMime_put_OaepPadding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Pkcs7CryptAlg(CkString var1) {
/* 250 */     chilkatJNI.CkMime_get_Pkcs7CryptAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pkcs7CryptAlg() {
/* 254 */     return chilkatJNI.CkMime_pkcs7CryptAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Pkcs7CryptAlg(String var1) {
/* 258 */     chilkatJNI.CkMime_put_Pkcs7CryptAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Pkcs7KeyLength() {
/* 262 */     return chilkatJNI.CkMime_get_Pkcs7KeyLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Pkcs7KeyLength(int var1) {
/* 266 */     chilkatJNI.CkMime_put_Pkcs7KeyLength(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Protocol(CkString var1) {
/* 270 */     chilkatJNI.CkMime_get_Protocol(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String protocol() {
/* 274 */     return chilkatJNI.CkMime_protocol(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Protocol(String var1) {
/* 278 */     chilkatJNI.CkMime_put_Protocol(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigningAlg(CkString var1) {
/* 282 */     chilkatJNI.CkMime_get_SigningAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signingAlg() {
/* 286 */     return chilkatJNI.CkMime_signingAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigningAlg(String var1) {
/* 290 */     chilkatJNI.CkMime_put_SigningAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SigningHashAlg(CkString var1) {
/* 294 */     chilkatJNI.CkMime_get_SigningHashAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signingHashAlg() {
/* 298 */     return chilkatJNI.CkMime_signingHashAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SigningHashAlg(String var1) {
/* 302 */     chilkatJNI.CkMime_put_SigningHashAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UnwrapExtras() {
/* 306 */     return chilkatJNI.CkMime_get_UnwrapExtras(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UnwrapExtras(boolean var1) {
/* 310 */     chilkatJNI.CkMime_put_UnwrapExtras(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseMmDescription() {
/* 314 */     return chilkatJNI.CkMime_get_UseMmDescription(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseMmDescription(boolean var1) {
/* 318 */     chilkatJNI.CkMime_put_UseMmDescription(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_UseXPkcs7() {
/* 322 */     return chilkatJNI.CkMime_get_UseXPkcs7(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UseXPkcs7(boolean var1) {
/* 326 */     chilkatJNI.CkMime_put_UseXPkcs7(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 330 */     return chilkatJNI.CkMime_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 334 */     chilkatJNI.CkMime_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 338 */     chilkatJNI.CkMime_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 342 */     return chilkatJNI.CkMime_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void AddContentLength() {
/* 346 */     chilkatJNI.CkMime_AddContentLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddDecryptCert(CkCert var1) {
/* 350 */     return chilkatJNI.CkMime_AddDecryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddDetachedSignature(CkCert var1) {
/* 354 */     return chilkatJNI.CkMime_AddDetachedSignature(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddDetachedSignature2(CkCert var1, boolean var2) {
/* 358 */     return chilkatJNI.CkMime_AddDetachedSignature2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddDetachedSignaturePk(CkCert var1, CkPrivateKey var2) {
/* 362 */     return chilkatJNI.CkMime_AddDetachedSignaturePk(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean AddDetachedSignaturePk2(CkCert var1, CkPrivateKey var2, boolean var3) {
/* 366 */     return chilkatJNI.CkMime_AddDetachedSignaturePk2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddEncryptCert(CkCert var1) {
/* 370 */     return chilkatJNI.CkMime_AddEncryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddHeaderField(String var1, String var2) {
/* 374 */     return chilkatJNI.CkMime_AddHeaderField(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddPfxSourceData(CkByteData var1, String var2) {
/* 378 */     return chilkatJNI.CkMime_AddPfxSourceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddPfxSourceFile(String var1, String var2) {
/* 382 */     return chilkatJNI.CkMime_AddPfxSourceFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendPart(CkMime var1) {
/* 386 */     return chilkatJNI.CkMime_AppendPart(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AppendPartFromFile(String var1) {
/* 390 */     return chilkatJNI.CkMime_AppendPartFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AsnBodyToXml(CkString var1) {
/* 394 */     return chilkatJNI.CkMime_AsnBodyToXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String asnBodyToXml() {
/* 398 */     return chilkatJNI.CkMime_asnBodyToXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void ClearEncryptCerts() {
/* 402 */     chilkatJNI.CkMime_ClearEncryptCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ContainsEncryptedParts() {
/* 406 */     return chilkatJNI.CkMime_ContainsEncryptedParts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ContainsSignedParts() {
/* 410 */     return chilkatJNI.CkMime_ContainsSignedParts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Convert8Bit() {
/* 414 */     chilkatJNI.CkMime_Convert8Bit(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ConvertToMultipartAlt() {
/* 418 */     return chilkatJNI.CkMime_ConvertToMultipartAlt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ConvertToMultipartMixed() {
/* 422 */     return chilkatJNI.CkMime_ConvertToMultipartMixed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ConvertToSigned(CkCert var1) {
/* 426 */     return chilkatJNI.CkMime_ConvertToSigned(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ConvertToSignedPk(CkCert var1, CkPrivateKey var2) {
/* 430 */     return chilkatJNI.CkMime_ConvertToSignedPk(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean Decrypt() {
/* 434 */     return chilkatJNI.CkMime_Decrypt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Decrypt2(CkCert var1, CkPrivateKey var2) {
/* 438 */     return chilkatJNI.CkMime_Decrypt2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean DecryptUsingCert(CkCert var1) {
/* 442 */     return chilkatJNI.CkMime_DecryptUsingCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DecryptUsingPfxData(CkByteData var1, String var2) {
/* 446 */     return chilkatJNI.CkMime_DecryptUsingPfxData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean DecryptUsingPfxFile(String var1, String var2) {
/* 450 */     return chilkatJNI.CkMime_DecryptUsingPfxFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Encrypt(CkCert var1) {
/* 454 */     return chilkatJNI.CkMime_Encrypt(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean EncryptN() {
/* 458 */     return chilkatJNI.CkMime_EncryptN(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkStringArray ExtractPartsToFiles(String var1) {
/* 462 */     long var2 = chilkatJNI.CkMime_ExtractPartsToFiles(this.swigCPtr, this, var1);
/* 463 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindIssuer(CkCert var1) {
/* 467 */     long var2 = chilkatJNI.CkMime_FindIssuer(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/* 468 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetBodyBd(CkBinData var1) {
/* 472 */     return chilkatJNI.CkMime_GetBodyBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetBodyBinary(CkByteData var1) {
/* 476 */     return chilkatJNI.CkMime_GetBodyBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetBodyDecoded(CkString var1) {
/* 480 */     return chilkatJNI.CkMime_GetBodyDecoded(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getBodyDecoded() {
/* 484 */     return chilkatJNI.CkMime_getBodyDecoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String bodyDecoded() {
/* 488 */     return chilkatJNI.CkMime_bodyDecoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetBodyEncoded(CkString var1) {
/* 492 */     return chilkatJNI.CkMime_GetBodyEncoded(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getBodyEncoded() {
/* 496 */     return chilkatJNI.CkMime_getBodyEncoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String bodyEncoded() {
/* 500 */     return chilkatJNI.CkMime_bodyEncoded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkCert GetEncryptCert(int var1) {
/* 504 */     long var2 = chilkatJNI.CkMime_GetEncryptCert(this.swigCPtr, this, var1);
/* 505 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetEntireBody(CkString var1) {
/* 509 */     return chilkatJNI.CkMime_GetEntireBody(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getEntireBody() {
/* 513 */     return chilkatJNI.CkMime_getEntireBody(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String entireBody() {
/* 517 */     return chilkatJNI.CkMime_entireBody(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetEntireHead(CkString var1) {
/* 521 */     return chilkatJNI.CkMime_GetEntireHead(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getEntireHead() {
/* 525 */     return chilkatJNI.CkMime_getEntireHead(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String entireHead() {
/* 529 */     return chilkatJNI.CkMime_entireHead(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderField(String var1, CkString var2) {
/* 533 */     return chilkatJNI.CkMime_GetHeaderField(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderField(String var1) {
/* 537 */     return chilkatJNI.CkMime_getHeaderField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerField(String var1) {
/* 541 */     return chilkatJNI.CkMime_headerField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderFieldAttribute(String var1, String var2, CkString var3) {
/* 545 */     return chilkatJNI.CkMime_GetHeaderFieldAttribute(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getHeaderFieldAttribute(String var1, String var2) {
/* 549 */     return chilkatJNI.CkMime_getHeaderFieldAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String headerFieldAttribute(String var1, String var2) {
/* 553 */     return chilkatJNI.CkMime_headerFieldAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderFieldName(int var1, CkString var2) {
/* 557 */     return chilkatJNI.CkMime_GetHeaderFieldName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderFieldName(int var1) {
/* 561 */     return chilkatJNI.CkMime_getHeaderFieldName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerFieldName(int var1) {
/* 565 */     return chilkatJNI.CkMime_headerFieldName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetHeaderFieldValue(int var1, CkString var2) {
/* 569 */     return chilkatJNI.CkMime_GetHeaderFieldValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getHeaderFieldValue(int var1) {
/* 573 */     return chilkatJNI.CkMime_getHeaderFieldValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String headerFieldValue(int var1) {
/* 577 */     return chilkatJNI.CkMime_headerFieldValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetMime(CkString var1) {
/* 581 */     return chilkatJNI.CkMime_GetMime(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getMime() {
/* 585 */     return chilkatJNI.CkMime_getMime(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String mime() {
/* 589 */     return chilkatJNI.CkMime_mime(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetMimeBd(CkBinData var1) {
/* 593 */     return chilkatJNI.CkMime_GetMimeBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetMimeBytes(CkByteData var1) {
/* 597 */     return chilkatJNI.CkMime_GetMimeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetMimeSb(CkStringBuilder var1) {
/* 601 */     return chilkatJNI.CkMime_GetMimeSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkMime GetPart(int var1) {
/* 605 */     long var2 = chilkatJNI.CkMime_GetPart(this.swigCPtr, this, var1);
/* 606 */     return var2 == 0L ? null : new CkMime(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetSignatureSigningTime(int var1, SYSTEMTIME var2) {
/* 610 */     return chilkatJNI.CkMime_GetSignatureSigningTime(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetSignatureSigningTimeStr(int var1, CkString var2) {
/* 614 */     return chilkatJNI.CkMime_GetSignatureSigningTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getSignatureSigningTimeStr(int var1) {
/* 618 */     return chilkatJNI.CkMime_getSignatureSigningTimeStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String signatureSigningTimeStr(int var1) {
/* 622 */     return chilkatJNI.CkMime_signatureSigningTimeStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkCert GetSignerCert(int var1) {
/* 626 */     long var2 = chilkatJNI.CkMime_GetSignerCert(this.swigCPtr, this, var1);
/* 627 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCertChain GetSignerCertChain(int var1) {
/* 631 */     long var2 = chilkatJNI.CkMime_GetSignerCertChain(this.swigCPtr, this, var1);
/* 632 */     return var2 == 0L ? null : new CkCertChain(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetStructure(String var1, CkString var2) {
/* 636 */     return chilkatJNI.CkMime_GetStructure(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getStructure(String var1) {
/* 640 */     return chilkatJNI.CkMime_getStructure(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String structure(String var1) {
/* 644 */     return chilkatJNI.CkMime_structure(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 648 */     return chilkatJNI.CkMime_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 652 */     return chilkatJNI.CkMime_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 656 */     return chilkatJNI.CkMime_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean HasSignatureSigningTime(int var1) {
/* 660 */     return chilkatJNI.CkMime_HasSignatureSigningTime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsApplicationData() {
/* 664 */     return chilkatJNI.CkMime_IsApplicationData(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsAttachment() {
/* 668 */     return chilkatJNI.CkMime_IsAttachment(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsAudio() {
/* 672 */     return chilkatJNI.CkMime_IsAudio(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsEncrypted() {
/* 676 */     return chilkatJNI.CkMime_IsEncrypted(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsHtml() {
/* 680 */     return chilkatJNI.CkMime_IsHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsImage() {
/* 684 */     return chilkatJNI.CkMime_IsImage(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsMultipart() {
/* 688 */     return chilkatJNI.CkMime_IsMultipart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsMultipartAlternative() {
/* 692 */     return chilkatJNI.CkMime_IsMultipartAlternative(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsMultipartMixed() {
/* 696 */     return chilkatJNI.CkMime_IsMultipartMixed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsMultipartRelated() {
/* 700 */     return chilkatJNI.CkMime_IsMultipartRelated(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsPlainText() {
/* 704 */     return chilkatJNI.CkMime_IsPlainText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsSigned() {
/* 708 */     return chilkatJNI.CkMime_IsSigned(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsText() {
/* 712 */     return chilkatJNI.CkMime_IsText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 716 */     return chilkatJNI.CkMime_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsVideo() {
/* 720 */     return chilkatJNI.CkMime_IsVideo(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsXml() {
/* 724 */     return chilkatJNI.CkMime_IsXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkJsonObject LastJsonData() {
/* 728 */     long var1 = chilkatJNI.CkMime_LastJsonData(this.swigCPtr, this);
/* 729 */     return var1 == 0L ? null : new CkJsonObject(var1, true);
/*     */   }
/*     */   
/*     */   public boolean LoadMime(String var1) {
/* 733 */     return chilkatJNI.CkMime_LoadMime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadMimeBd(CkBinData var1) {
/* 737 */     return chilkatJNI.CkMime_LoadMimeBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadMimeBytes(CkByteData var1) {
/* 741 */     return chilkatJNI.CkMime_LoadMimeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadMimeFile(String var1) {
/* 745 */     return chilkatJNI.CkMime_LoadMimeFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadMimeSb(CkStringBuilder var1) {
/* 749 */     return chilkatJNI.CkMime_LoadMimeSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 753 */     return chilkatJNI.CkMime_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile(String var1) {
/* 757 */     return chilkatJNI.CkMime_LoadXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean NewMessageRfc822(CkMime var1) {
/* 761 */     return chilkatJNI.CkMime_NewMessageRfc822(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean NewMultipartAlternative() {
/* 765 */     return chilkatJNI.CkMime_NewMultipartAlternative(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean NewMultipartMixed() {
/* 769 */     return chilkatJNI.CkMime_NewMultipartMixed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean NewMultipartRelated() {
/* 773 */     return chilkatJNI.CkMime_NewMultipartRelated(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void RemoveHeaderField(String var1, boolean var2) {
/* 777 */     chilkatJNI.CkMime_RemoveHeaderField(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RemovePart(int var1) {
/* 781 */     return chilkatJNI.CkMime_RemovePart(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveBody(String var1) {
/* 785 */     return chilkatJNI.CkMime_SaveBody(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 789 */     return chilkatJNI.CkMime_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveMime(String var1) {
/* 793 */     return chilkatJNI.CkMime_SaveMime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXml(String var1) {
/* 797 */     return chilkatJNI.CkMime_SaveXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetBody(String var1) {
/* 801 */     chilkatJNI.CkMime_SetBody(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromBinary(CkByteData var1) {
/* 805 */     return chilkatJNI.CkMime_SetBodyFromBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromEncoded(String var1, String var2) {
/* 809 */     return chilkatJNI.CkMime_SetBodyFromEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromFile(String var1) {
/* 813 */     return chilkatJNI.CkMime_SetBodyFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromHtml(String var1) {
/* 817 */     return chilkatJNI.CkMime_SetBodyFromHtml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromPlainText(String var1) {
/* 821 */     return chilkatJNI.CkMime_SetBodyFromPlainText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBodyFromXml(String var1) {
/* 825 */     return chilkatJNI.CkMime_SetBodyFromXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetCSP(CkCsp var1) {
/* 829 */     return chilkatJNI.CkMime_SetCSP(this.swigCPtr, this, CkCsp.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetHeaderField(String var1, String var2) {
/* 833 */     return chilkatJNI.CkMime_SetHeaderField(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetVerifyCert(CkCert var1) {
/* 837 */     return chilkatJNI.CkMime_SetVerifyCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 841 */     return chilkatJNI.CkMime_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnwrapSecurity() {
/* 845 */     return chilkatJNI.CkMime_UnwrapSecurity(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UrlEncodeBody(String var1) {
/* 849 */     chilkatJNI.CkMime_UrlEncodeBody(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 853 */     return chilkatJNI.CkMime_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean Verify() {
/* 857 */     return chilkatJNI.CkMime_Verify(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */