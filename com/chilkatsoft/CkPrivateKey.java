/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkPrivateKey
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkPrivateKey(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkPrivateKey var0) {
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
/*  29 */         chilkatJNI.delete_CkPrivateKey(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkPrivateKey()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkPrivateKey(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkPrivateKey_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkPrivateKey_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkPrivateKey_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int get_BitLength() {
/*  54 */     return chilkatJNI.CkPrivateKey_get_BitLength(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  58 */     chilkatJNI.CkPrivateKey_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  62 */     return chilkatJNI.CkPrivateKey_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  66 */     chilkatJNI.CkPrivateKey_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_KeyType(CkString var1) {
/*  70 */     chilkatJNI.CkPrivateKey_get_KeyType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyType() {
/*  74 */     return chilkatJNI.CkPrivateKey_keyType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  78 */     chilkatJNI.CkPrivateKey_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  82 */     return chilkatJNI.CkPrivateKey_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  86 */     chilkatJNI.CkPrivateKey_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  90 */     return chilkatJNI.CkPrivateKey_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  94 */     chilkatJNI.CkPrivateKey_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  98 */     return chilkatJNI.CkPrivateKey_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 102 */     return chilkatJNI.CkPrivateKey_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 106 */     chilkatJNI.CkPrivateKey_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Pkcs8EncryptAlg(CkString var1) {
/* 110 */     chilkatJNI.CkPrivateKey_get_Pkcs8EncryptAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pkcs8EncryptAlg() {
/* 114 */     return chilkatJNI.CkPrivateKey_pkcs8EncryptAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Pkcs8EncryptAlg(String var1) {
/* 118 */     chilkatJNI.CkPrivateKey_put_Pkcs8EncryptAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 122 */     return chilkatJNI.CkPrivateKey_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 126 */     chilkatJNI.CkPrivateKey_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 130 */     chilkatJNI.CkPrivateKey_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 134 */     return chilkatJNI.CkPrivateKey_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetJwk(CkString var1) {
/* 138 */     return chilkatJNI.CkPrivateKey_GetJwk(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getJwk() {
/* 142 */     return chilkatJNI.CkPrivateKey_getJwk(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String jwk() {
/* 146 */     return chilkatJNI.CkPrivateKey_jwk(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetJwkThumbprint(String var1, CkString var2) {
/* 150 */     return chilkatJNI.CkPrivateKey_GetJwkThumbprint(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getJwkThumbprint(String var1) {
/* 154 */     return chilkatJNI.CkPrivateKey_getJwkThumbprint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String jwkThumbprint(String var1) {
/* 158 */     return chilkatJNI.CkPrivateKey_jwkThumbprint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs1(CkByteData var1) {
/* 162 */     return chilkatJNI.CkPrivateKey_GetPkcs1(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs1ENC(String var1, CkString var2) {
/* 166 */     return chilkatJNI.CkPrivateKey_GetPkcs1ENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPkcs1ENC(String var1) {
/* 170 */     return chilkatJNI.CkPrivateKey_getPkcs1ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pkcs1ENC(String var1) {
/* 174 */     return chilkatJNI.CkPrivateKey_pkcs1ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs1Pem(CkString var1) {
/* 178 */     return chilkatJNI.CkPrivateKey_GetPkcs1Pem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getPkcs1Pem() {
/* 182 */     return chilkatJNI.CkPrivateKey_getPkcs1Pem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String pkcs1Pem() {
/* 186 */     return chilkatJNI.CkPrivateKey_pkcs1Pem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8(CkByteData var1) {
/* 190 */     return chilkatJNI.CkPrivateKey_GetPkcs8(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8ENC(String var1, CkString var2) {
/* 194 */     return chilkatJNI.CkPrivateKey_GetPkcs8ENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPkcs8ENC(String var1) {
/* 198 */     return chilkatJNI.CkPrivateKey_getPkcs8ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pkcs8ENC(String var1) {
/* 202 */     return chilkatJNI.CkPrivateKey_pkcs8ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8Encrypted(String var1, CkByteData var2) {
/* 206 */     return chilkatJNI.CkPrivateKey_GetPkcs8Encrypted(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8EncryptedENC(String var1, String var2, CkString var3) {
/* 210 */     return chilkatJNI.CkPrivateKey_GetPkcs8EncryptedENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getPkcs8EncryptedENC(String var1, String var2) {
/* 214 */     return chilkatJNI.CkPrivateKey_getPkcs8EncryptedENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String pkcs8EncryptedENC(String var1, String var2) {
/* 218 */     return chilkatJNI.CkPrivateKey_pkcs8EncryptedENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8EncryptedPem(String var1, CkString var2) {
/* 222 */     return chilkatJNI.CkPrivateKey_GetPkcs8EncryptedPem(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPkcs8EncryptedPem(String var1) {
/* 226 */     return chilkatJNI.CkPrivateKey_getPkcs8EncryptedPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pkcs8EncryptedPem(String var1) {
/* 230 */     return chilkatJNI.CkPrivateKey_pkcs8EncryptedPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8Pem(CkString var1) {
/* 234 */     return chilkatJNI.CkPrivateKey_GetPkcs8Pem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getPkcs8Pem() {
/* 238 */     return chilkatJNI.CkPrivateKey_getPkcs8Pem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String pkcs8Pem() {
/* 242 */     return chilkatJNI.CkPrivateKey_pkcs8Pem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPublicKey GetPublicKey() {
/* 246 */     long var1 = chilkatJNI.CkPrivateKey_GetPublicKey(this.swigCPtr, this);
/* 247 */     return var1 == 0L ? null : new CkPublicKey(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetRsaDer(CkByteData var1) {
/* 251 */     return chilkatJNI.CkPrivateKey_GetRsaDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetRsaPem(CkString var1) {
/* 255 */     return chilkatJNI.CkPrivateKey_GetRsaPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getRsaPem() {
/* 259 */     return chilkatJNI.CkPrivateKey_getRsaPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String rsaPem() {
/* 263 */     return chilkatJNI.CkPrivateKey_rsaPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 267 */     return chilkatJNI.CkPrivateKey_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 271 */     return chilkatJNI.CkPrivateKey_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 275 */     return chilkatJNI.CkPrivateKey_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadAnyFormat(CkBinData var1, String var2) {
/* 279 */     return chilkatJNI.CkPrivateKey_LoadAnyFormat(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadEncryptedPem(String var1, String var2) {
/* 283 */     return chilkatJNI.CkPrivateKey_LoadEncryptedPem(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadEncryptedPemFile(String var1, String var2) {
/* 287 */     return chilkatJNI.CkPrivateKey_LoadEncryptedPemFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadJwk(String var1) {
/* 291 */     return chilkatJNI.CkPrivateKey_LoadJwk(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPem(String var1) {
/* 295 */     return chilkatJNI.CkPrivateKey_LoadPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPemFile(String var1) {
/* 299 */     return chilkatJNI.CkPrivateKey_LoadPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs1(CkByteData var1) {
/* 303 */     return chilkatJNI.CkPrivateKey_LoadPkcs1(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs1File(String var1) {
/* 307 */     return chilkatJNI.CkPrivateKey_LoadPkcs1File(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs8(CkByteData var1) {
/* 311 */     return chilkatJNI.CkPrivateKey_LoadPkcs8(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs8Encrypted(CkByteData var1, String var2) {
/* 315 */     return chilkatJNI.CkPrivateKey_LoadPkcs8Encrypted(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs8EncryptedFile(String var1, String var2) {
/* 319 */     return chilkatJNI.CkPrivateKey_LoadPkcs8EncryptedFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs8File(String var1) {
/* 323 */     return chilkatJNI.CkPrivateKey_LoadPkcs8File(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPvk(CkByteData var1, String var2) {
/* 327 */     return chilkatJNI.CkPrivateKey_LoadPvk(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPvkFile(String var1, String var2) {
/* 331 */     return chilkatJNI.CkPrivateKey_LoadPvkFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadRsaDer(CkByteData var1) {
/* 335 */     return chilkatJNI.CkPrivateKey_LoadRsaDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadRsaDerFile(String var1) {
/* 339 */     return chilkatJNI.CkPrivateKey_LoadRsaDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 343 */     return chilkatJNI.CkPrivateKey_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile(String var1) {
/* 347 */     return chilkatJNI.CkPrivateKey_LoadXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 351 */     return chilkatJNI.CkPrivateKey_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SavePemFile(String var1) {
/* 355 */     return chilkatJNI.CkPrivateKey_SavePemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SavePkcs1File(String var1) {
/* 359 */     return chilkatJNI.CkPrivateKey_SavePkcs1File(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SavePkcs8EncryptedFile(String var1, String var2) {
/* 363 */     return chilkatJNI.CkPrivateKey_SavePkcs8EncryptedFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SavePkcs8EncryptedPemFile(String var1, String var2) {
/* 367 */     return chilkatJNI.CkPrivateKey_SavePkcs8EncryptedPemFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SavePkcs8File(String var1) {
/* 371 */     return chilkatJNI.CkPrivateKey_SavePkcs8File(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SavePkcs8PemFile(String var1) {
/* 375 */     return chilkatJNI.CkPrivateKey_SavePkcs8PemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveRsaDerFile(String var1) {
/* 379 */     return chilkatJNI.CkPrivateKey_SaveRsaDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveRsaPemFile(String var1) {
/* 383 */     return chilkatJNI.CkPrivateKey_SaveRsaPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXmlFile(String var1) {
/* 387 */     return chilkatJNI.CkPrivateKey_SaveXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkPrivateKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */