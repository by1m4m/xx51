/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkRsa
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkRsa(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkRsa var0) {
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
/*  29 */         chilkatJNI.delete_CkRsa(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkRsa()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkRsa(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkRsa_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkRsa_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkRsa_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  54 */     chilkatJNI.CkRsa_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  58 */     return chilkatJNI.CkRsa_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Charset(String var1) {
/*  62 */     chilkatJNI.CkRsa_put_Charset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkRsa_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkRsa_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkRsa_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EncodingMode(CkString var1) {
/*  78 */     chilkatJNI.CkRsa_get_EncodingMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encodingMode() {
/*  82 */     return chilkatJNI.CkRsa_encodingMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EncodingMode(String var1) {
/*  86 */     chilkatJNI.CkRsa_put_EncodingMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  90 */     chilkatJNI.CkRsa_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkRsa_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  98 */     chilkatJNI.CkRsa_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 102 */     return chilkatJNI.CkRsa_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 106 */     chilkatJNI.CkRsa_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 110 */     return chilkatJNI.CkRsa_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 114 */     return chilkatJNI.CkRsa_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 118 */     chilkatJNI.CkRsa_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_LittleEndian() {
/* 122 */     return chilkatJNI.CkRsa_get_LittleEndian(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LittleEndian(boolean var1) {
/* 126 */     chilkatJNI.CkRsa_put_LittleEndian(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_NoUnpad() {
/* 130 */     return chilkatJNI.CkRsa_get_NoUnpad(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NoUnpad(boolean var1) {
/* 134 */     chilkatJNI.CkRsa_put_NoUnpad(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumBits() {
/* 138 */     return chilkatJNI.CkRsa_get_NumBits(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_OaepHash(CkString var1) {
/* 142 */     chilkatJNI.CkRsa_get_OaepHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oaepHash() {
/* 146 */     return chilkatJNI.CkRsa_oaepHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepHash(String var1) {
/* 150 */     chilkatJNI.CkRsa_put_OaepHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_OaepMgfHash(CkString var1) {
/* 154 */     chilkatJNI.CkRsa_get_OaepMgfHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oaepMgfHash() {
/* 158 */     return chilkatJNI.CkRsa_oaepMgfHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepMgfHash(String var1) {
/* 162 */     chilkatJNI.CkRsa_put_OaepMgfHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_OaepPadding() {
/* 166 */     return chilkatJNI.CkRsa_get_OaepPadding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OaepPadding(boolean var1) {
/* 170 */     chilkatJNI.CkRsa_put_OaepPadding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 174 */     return chilkatJNI.CkRsa_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 178 */     chilkatJNI.CkRsa_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 182 */     chilkatJNI.CkRsa_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 186 */     return chilkatJNI.CkRsa_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DecryptBytes(CkByteData var1, boolean var2, CkByteData var3) {
/* 190 */     return chilkatJNI.CkRsa_DecryptBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean DecryptBytesENC(String var1, boolean var2, CkByteData var3) {
/* 194 */     return chilkatJNI.CkRsa_DecryptBytesENC(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean DecryptString(CkByteData var1, boolean var2, CkString var3) {
/* 198 */     return chilkatJNI.CkRsa_DecryptString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String decryptString(CkByteData var1, boolean var2) {
/* 202 */     return chilkatJNI.CkRsa_decryptString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean DecryptStringENC(String var1, boolean var2, CkString var3) {
/* 206 */     return chilkatJNI.CkRsa_DecryptStringENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String decryptStringENC(String var1, boolean var2) {
/* 210 */     return chilkatJNI.CkRsa_decryptStringENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean EncryptBytes(CkByteData var1, boolean var2, CkByteData var3) {
/* 214 */     return chilkatJNI.CkRsa_EncryptBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean EncryptBytesENC(CkByteData var1, boolean var2, CkString var3) {
/* 218 */     return chilkatJNI.CkRsa_EncryptBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String encryptBytesENC(CkByteData var1, boolean var2) {
/* 222 */     return chilkatJNI.CkRsa_encryptBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean EncryptString(String var1, boolean var2, CkByteData var3) {
/* 226 */     return chilkatJNI.CkRsa_EncryptString(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean EncryptStringENC(String var1, boolean var2, CkString var3) {
/* 230 */     return chilkatJNI.CkRsa_EncryptStringENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String encryptStringENC(String var1, boolean var2) {
/* 234 */     return chilkatJNI.CkRsa_encryptStringENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ExportPrivateKey(CkString var1) {
/* 238 */     return chilkatJNI.CkRsa_ExportPrivateKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exportPrivateKey() {
/* 242 */     return chilkatJNI.CkRsa_exportPrivateKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPrivateKey ExportPrivateKeyObj() {
/* 246 */     long var1 = chilkatJNI.CkRsa_ExportPrivateKeyObj(this.swigCPtr, this);
/* 247 */     return var1 == 0L ? null : new CkPrivateKey(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ExportPublicKey(CkString var1) {
/* 251 */     return chilkatJNI.CkRsa_ExportPublicKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exportPublicKey() {
/* 255 */     return chilkatJNI.CkRsa_exportPublicKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPublicKey ExportPublicKeyObj() {
/* 259 */     long var1 = chilkatJNI.CkRsa_ExportPublicKeyObj(this.swigCPtr, this);
/* 260 */     return var1 == 0L ? null : new CkPublicKey(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GenerateKey(int var1) {
/* 264 */     return chilkatJNI.CkRsa_GenerateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ImportPrivateKey(String var1) {
/* 268 */     return chilkatJNI.CkRsa_ImportPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ImportPrivateKeyObj(CkPrivateKey var1) {
/* 272 */     return chilkatJNI.CkRsa_ImportPrivateKeyObj(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ImportPublicKey(String var1) {
/* 276 */     return chilkatJNI.CkRsa_ImportPublicKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ImportPublicKeyObj(CkPublicKey var1) {
/* 280 */     return chilkatJNI.CkRsa_ImportPublicKeyObj(this.swigCPtr, this, CkPublicKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenSslSignBytes(CkByteData var1, CkByteData var2) {
/* 284 */     return chilkatJNI.CkRsa_OpenSslSignBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean OpenSslSignBytesENC(CkByteData var1, CkString var2) {
/* 288 */     return chilkatJNI.CkRsa_OpenSslSignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String openSslSignBytesENC(CkByteData var1) {
/* 292 */     return chilkatJNI.CkRsa_openSslSignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenSslSignString(String var1, CkByteData var2) {
/* 296 */     return chilkatJNI.CkRsa_OpenSslSignString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean OpenSslSignStringENC(String var1, CkString var2) {
/* 300 */     return chilkatJNI.CkRsa_OpenSslSignStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String openSslSignStringENC(String var1) {
/* 304 */     return chilkatJNI.CkRsa_openSslSignStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenSslVerifyBytes(CkByteData var1, CkByteData var2) {
/* 308 */     return chilkatJNI.CkRsa_OpenSslVerifyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean OpenSslVerifyBytesENC(String var1, CkByteData var2) {
/* 312 */     return chilkatJNI.CkRsa_OpenSslVerifyBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean OpenSslVerifyString(CkByteData var1, CkString var2) {
/* 316 */     return chilkatJNI.CkRsa_OpenSslVerifyString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String openSslVerifyString(CkByteData var1) {
/* 320 */     return chilkatJNI.CkRsa_openSslVerifyString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean OpenSslVerifyStringENC(String var1, CkString var2) {
/* 324 */     return chilkatJNI.CkRsa_OpenSslVerifyStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String openSslVerifyStringENC(String var1) {
/* 328 */     return chilkatJNI.CkRsa_openSslVerifyStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 332 */     return chilkatJNI.CkRsa_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetX509Cert(CkCert var1, boolean var2) {
/* 336 */     return chilkatJNI.CkRsa_SetX509Cert(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SignBytes(CkByteData var1, String var2, CkByteData var3) {
/* 340 */     return chilkatJNI.CkRsa_SignBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean SignBytesENC(CkByteData var1, String var2, CkString var3) {
/* 344 */     return chilkatJNI.CkRsa_SignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String signBytesENC(CkByteData var1, String var2) {
/* 348 */     return chilkatJNI.CkRsa_signBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SignHash(CkByteData var1, String var2, CkByteData var3) {
/* 352 */     return chilkatJNI.CkRsa_SignHash(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean SignHashENC(String var1, String var2, CkString var3) {
/* 356 */     return chilkatJNI.CkRsa_SignHashENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String signHashENC(String var1, String var2) {
/* 360 */     return chilkatJNI.CkRsa_signHashENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SignString(String var1, String var2, CkByteData var3) {
/* 364 */     return chilkatJNI.CkRsa_SignString(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean SignStringENC(String var1, String var2, CkString var3) {
/* 368 */     return chilkatJNI.CkRsa_SignStringENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String signStringENC(String var1, String var2) {
/* 372 */     return chilkatJNI.CkRsa_signStringENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SnkToXml(String var1, CkString var2) {
/* 376 */     return chilkatJNI.CkRsa_SnkToXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String snkToXml(String var1) {
/* 380 */     return chilkatJNI.CkRsa_snkToXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 384 */     return chilkatJNI.CkRsa_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyBytes(CkByteData var1, String var2, CkByteData var3) {
/* 388 */     return chilkatJNI.CkRsa_VerifyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean VerifyBytesENC(CkByteData var1, String var2, String var3) {
/* 392 */     return chilkatJNI.CkRsa_VerifyBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean VerifyHash(CkByteData var1, String var2, CkByteData var3) {
/* 396 */     return chilkatJNI.CkRsa_VerifyHash(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean VerifyHashENC(String var1, String var2, String var3) {
/* 400 */     return chilkatJNI.CkRsa_VerifyHashENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean VerifyPrivateKey(String var1) {
/* 404 */     return chilkatJNI.CkRsa_VerifyPrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyString(String var1, String var2, CkByteData var3) {
/* 408 */     return chilkatJNI.CkRsa_VerifyString(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean VerifyStringENC(String var1, String var2, String var3) {
/* 412 */     return chilkatJNI.CkRsa_VerifyStringENC(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkRsa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */