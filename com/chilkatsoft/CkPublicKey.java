/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkPublicKey
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkPublicKey(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkPublicKey var0) {
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
/*  29 */         chilkatJNI.delete_CkPublicKey(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkPublicKey()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkPublicKey(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkPublicKey_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkPublicKey_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkPublicKey_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkPublicKey_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkPublicKey_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkPublicKey_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_KeySize() {
/*  66 */     return chilkatJNI.CkPublicKey_get_KeySize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_KeyType(CkString var1) {
/*  70 */     chilkatJNI.CkPublicKey_get_KeyType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyType() {
/*  74 */     return chilkatJNI.CkPublicKey_keyType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  78 */     chilkatJNI.CkPublicKey_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  82 */     return chilkatJNI.CkPublicKey_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  86 */     chilkatJNI.CkPublicKey_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  90 */     return chilkatJNI.CkPublicKey_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  94 */     chilkatJNI.CkPublicKey_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  98 */     return chilkatJNI.CkPublicKey_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 102 */     return chilkatJNI.CkPublicKey_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 106 */     chilkatJNI.CkPublicKey_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkPublicKey_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkPublicKey_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkPublicKey_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkPublicKey_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetDer(boolean var1, CkByteData var2) {
/* 126 */     return chilkatJNI.CkPublicKey_GetDer(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetEncoded(boolean var1, String var2, CkString var3) {
/* 130 */     return chilkatJNI.CkPublicKey_GetEncoded(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getEncoded(boolean var1, String var2) {
/* 134 */     return chilkatJNI.CkPublicKey_getEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String encoded(boolean var1, String var2) {
/* 138 */     return chilkatJNI.CkPublicKey_encoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetJwk(CkString var1) {
/* 142 */     return chilkatJNI.CkPublicKey_GetJwk(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getJwk() {
/* 146 */     return chilkatJNI.CkPublicKey_getJwk(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String jwk() {
/* 150 */     return chilkatJNI.CkPublicKey_jwk(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetJwkThumbprint(String var1, CkString var2) {
/* 154 */     return chilkatJNI.CkPublicKey_GetJwkThumbprint(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getJwkThumbprint(String var1) {
/* 158 */     return chilkatJNI.CkPublicKey_getJwkThumbprint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String jwkThumbprint(String var1) {
/* 162 */     return chilkatJNI.CkPublicKey_jwkThumbprint(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetOpenSslDer(CkByteData var1) {
/* 166 */     return chilkatJNI.CkPublicKey_GetOpenSslDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetOpenSslPem(CkString var1) {
/* 170 */     return chilkatJNI.CkPublicKey_GetOpenSslPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getOpenSslPem() {
/* 174 */     return chilkatJNI.CkPublicKey_getOpenSslPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String openSslPem() {
/* 178 */     return chilkatJNI.CkPublicKey_openSslPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetPem(boolean var1, CkString var2) {
/* 182 */     return chilkatJNI.CkPublicKey_GetPem(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPem(boolean var1) {
/* 186 */     return chilkatJNI.CkPublicKey_getPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pem(boolean var1) {
/* 190 */     return chilkatJNI.CkPublicKey_pem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs1ENC(String var1, CkString var2) {
/* 194 */     return chilkatJNI.CkPublicKey_GetPkcs1ENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPkcs1ENC(String var1) {
/* 198 */     return chilkatJNI.CkPublicKey_getPkcs1ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pkcs1ENC(String var1) {
/* 202 */     return chilkatJNI.CkPublicKey_pkcs1ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPkcs8ENC(String var1, CkString var2) {
/* 206 */     return chilkatJNI.CkPublicKey_GetPkcs8ENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPkcs8ENC(String var1) {
/* 210 */     return chilkatJNI.CkPublicKey_getPkcs8ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String pkcs8ENC(String var1) {
/* 214 */     return chilkatJNI.CkPublicKey_pkcs8ENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetRsaDer(CkByteData var1) {
/* 218 */     return chilkatJNI.CkPublicKey_GetRsaDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 222 */     return chilkatJNI.CkPublicKey_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 226 */     return chilkatJNI.CkPublicKey_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 230 */     return chilkatJNI.CkPublicKey_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadBase64(String var1) {
/* 234 */     return chilkatJNI.CkPublicKey_LoadBase64(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromBinary(CkByteData var1) {
/* 238 */     return chilkatJNI.CkPublicKey_LoadFromBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromFile(String var1) {
/* 242 */     return chilkatJNI.CkPublicKey_LoadFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromString(String var1) {
/* 246 */     return chilkatJNI.CkPublicKey_LoadFromString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadOpenSslDer(CkByteData var1) {
/* 250 */     return chilkatJNI.CkPublicKey_LoadOpenSslDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadOpenSslDerFile(String var1) {
/* 254 */     return chilkatJNI.CkPublicKey_LoadOpenSslDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadOpenSslPem(String var1) {
/* 258 */     return chilkatJNI.CkPublicKey_LoadOpenSslPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadOpenSslPemFile(String var1) {
/* 262 */     return chilkatJNI.CkPublicKey_LoadOpenSslPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPkcs1Pem(String var1) {
/* 266 */     return chilkatJNI.CkPublicKey_LoadPkcs1Pem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadRsaDer(CkByteData var1) {
/* 270 */     return chilkatJNI.CkPublicKey_LoadRsaDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadRsaDerFile(String var1) {
/* 274 */     return chilkatJNI.CkPublicKey_LoadRsaDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 278 */     return chilkatJNI.CkPublicKey_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile(String var1) {
/* 282 */     return chilkatJNI.CkPublicKey_LoadXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveDerFile(boolean var1, String var2) {
/* 286 */     return chilkatJNI.CkPublicKey_SaveDerFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 290 */     return chilkatJNI.CkPublicKey_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveOpenSslDerFile(String var1) {
/* 294 */     return chilkatJNI.CkPublicKey_SaveOpenSslDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveOpenSslPemFile(String var1) {
/* 298 */     return chilkatJNI.CkPublicKey_SaveOpenSslPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SavePemFile(boolean var1, String var2) {
/* 302 */     return chilkatJNI.CkPublicKey_SavePemFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveRsaDerFile(String var1) {
/* 306 */     return chilkatJNI.CkPublicKey_SaveRsaDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXmlFile(String var1) {
/* 310 */     return chilkatJNI.CkPublicKey_SaveXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkPublicKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */