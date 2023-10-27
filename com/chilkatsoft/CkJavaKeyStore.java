/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJavaKeyStore
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJavaKeyStore(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJavaKeyStore var0) {
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
/*  29 */         chilkatJNI.delete_CkJavaKeyStore(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJavaKeyStore()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJavaKeyStore(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJavaKeyStore_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJavaKeyStore_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJavaKeyStore_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkJavaKeyStore_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkJavaKeyStore_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkJavaKeyStore_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkJavaKeyStore_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkJavaKeyStore_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkJavaKeyStore_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkJavaKeyStore_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkJavaKeyStore_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkJavaKeyStore_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkJavaKeyStore_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkJavaKeyStore_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumPrivateKeys() {
/*  98 */     return chilkatJNI.CkJavaKeyStore_get_NumPrivateKeys(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumSecretKeys() {
/* 102 */     return chilkatJNI.CkJavaKeyStore_get_NumSecretKeys(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumTrustedCerts() {
/* 106 */     return chilkatJNI.CkJavaKeyStore_get_NumTrustedCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_RequireCompleteChain() {
/* 110 */     return chilkatJNI.CkJavaKeyStore_get_RequireCompleteChain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RequireCompleteChain(boolean var1) {
/* 114 */     chilkatJNI.CkJavaKeyStore_put_RequireCompleteChain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkJavaKeyStore_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkJavaKeyStore_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerifyKeyedDigest() {
/* 126 */     return chilkatJNI.CkJavaKeyStore_get_VerifyKeyedDigest(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerifyKeyedDigest(boolean var1) {
/* 130 */     chilkatJNI.CkJavaKeyStore_put_VerifyKeyedDigest(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 134 */     chilkatJNI.CkJavaKeyStore_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 138 */     return chilkatJNI.CkJavaKeyStore_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddPfx(CkPfx var1, String var2, String var3) {
/* 142 */     return chilkatJNI.CkJavaKeyStore_AddPfx(this.swigCPtr, this, CkPfx.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddPrivateKey(CkCert var1, String var2, String var3) {
/* 146 */     return chilkatJNI.CkJavaKeyStore_AddPrivateKey(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddSecretKey(String var1, String var2, String var3, String var4, String var5) {
/* 150 */     return chilkatJNI.CkJavaKeyStore_AddSecretKey(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean AddTrustedCert(CkCert var1, String var2) {
/* 154 */     return chilkatJNI.CkJavaKeyStore_AddTrustedCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ChangePassword(int var1, String var2, String var3) {
/* 158 */     return chilkatJNI.CkJavaKeyStore_ChangePassword(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkCertChain FindCertChain(String var1, boolean var2) {
/* 162 */     long var3 = chilkatJNI.CkJavaKeyStore_FindCertChain(this.swigCPtr, this, var1, var2);
/* 163 */     return var3 == 0L ? null : new CkCertChain(var3, true);
/*     */   }
/*     */   
/*     */   public CkPrivateKey FindPrivateKey(String var1, String var2, boolean var3) {
/* 167 */     long var4 = chilkatJNI.CkJavaKeyStore_FindPrivateKey(this.swigCPtr, this, var1, var2, var3);
/* 168 */     return var4 == 0L ? null : new CkPrivateKey(var4, true);
/*     */   }
/*     */   
/*     */   public CkCert FindTrustedCert(String var1, boolean var2) {
/* 172 */     long var3 = chilkatJNI.CkJavaKeyStore_FindTrustedCert(this.swigCPtr, this, var1, var2);
/* 173 */     return var3 == 0L ? null : new CkCert(var3, true);
/*     */   }
/*     */   
/*     */   public CkCertChain GetCertChain(int var1) {
/* 177 */     long var2 = chilkatJNI.CkJavaKeyStore_GetCertChain(this.swigCPtr, this, var1);
/* 178 */     return var2 == 0L ? null : new CkCertChain(var2, true);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GetPrivateKey(String var1, int var2) {
/* 182 */     long var3 = chilkatJNI.CkJavaKeyStore_GetPrivateKey(this.swigCPtr, this, var1, var2);
/* 183 */     return var3 == 0L ? null : new CkPrivateKey(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetPrivateKeyAlias(int var1, CkString var2) {
/* 187 */     return chilkatJNI.CkJavaKeyStore_GetPrivateKeyAlias(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPrivateKeyAlias(int var1) {
/* 191 */     return chilkatJNI.CkJavaKeyStore_getPrivateKeyAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String privateKeyAlias(int var1) {
/* 195 */     return chilkatJNI.CkJavaKeyStore_privateKeyAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetSecretKey(String var1, int var2, String var3, CkString var4) {
/* 199 */     return chilkatJNI.CkJavaKeyStore_GetSecretKey(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getSecretKey(String var1, int var2, String var3) {
/* 203 */     return chilkatJNI.CkJavaKeyStore_getSecretKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String secretKey(String var1, int var2, String var3) {
/* 207 */     return chilkatJNI.CkJavaKeyStore_secretKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetSecretKeyAlias(int var1, CkString var2) {
/* 211 */     return chilkatJNI.CkJavaKeyStore_GetSecretKeyAlias(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getSecretKeyAlias(int var1) {
/* 215 */     return chilkatJNI.CkJavaKeyStore_getSecretKeyAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String secretKeyAlias(int var1) {
/* 219 */     return chilkatJNI.CkJavaKeyStore_secretKeyAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkCert GetTrustedCert(int var1) {
/* 223 */     long var2 = chilkatJNI.CkJavaKeyStore_GetTrustedCert(this.swigCPtr, this, var1);
/* 224 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetTrustedCertAlias(int var1, CkString var2) {
/* 228 */     return chilkatJNI.CkJavaKeyStore_GetTrustedCertAlias(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getTrustedCertAlias(int var1) {
/* 232 */     return chilkatJNI.CkJavaKeyStore_getTrustedCertAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String trustedCertAlias(int var1) {
/* 236 */     return chilkatJNI.CkJavaKeyStore_trustedCertAlias(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBinary(String var1, CkByteData var2) {
/* 240 */     return chilkatJNI.CkJavaKeyStore_LoadBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean LoadEncoded(String var1, String var2, String var3) {
/* 244 */     return chilkatJNI.CkJavaKeyStore_LoadEncoded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1, String var2) {
/* 248 */     return chilkatJNI.CkJavaKeyStore_LoadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadJwkSet(String var1, CkJsonObject var2) {
/* 252 */     return chilkatJNI.CkJavaKeyStore_LoadJwkSet(this.swigCPtr, this, var1, CkJsonObject.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean RemoveEntry(int var1, int var2) {
/* 256 */     return chilkatJNI.CkJavaKeyStore_RemoveEntry(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 260 */     return chilkatJNI.CkJavaKeyStore_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetAlias(int var1, int var2, String var3) {
/* 264 */     return chilkatJNI.CkJavaKeyStore_SetAlias(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean ToBinary(String var1, CkByteData var2) {
/* 268 */     return chilkatJNI.CkJavaKeyStore_ToBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ToEncodedString(String var1, String var2, CkString var3) {
/* 272 */     return chilkatJNI.CkJavaKeyStore_ToEncodedString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String toEncodedString(String var1, String var2) {
/* 276 */     return chilkatJNI.CkJavaKeyStore_toEncodedString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToFile(String var1, String var2) {
/* 280 */     return chilkatJNI.CkJavaKeyStore_ToFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToJwkSet(String var1, CkStringBuilder var2) {
/* 284 */     return chilkatJNI.CkJavaKeyStore_ToJwkSet(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkPem ToPem(String var1) {
/* 288 */     long var2 = chilkatJNI.CkJavaKeyStore_ToPem(this.swigCPtr, this, var1);
/* 289 */     return var2 == 0L ? null : new CkPem(var2, true);
/*     */   }
/*     */   
/*     */   public CkPfx ToPfx(String var1) {
/* 293 */     long var2 = chilkatJNI.CkJavaKeyStore_ToPfx(this.swigCPtr, this, var1);
/* 294 */     return var2 == 0L ? null : new CkPfx(var2, true);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 298 */     return chilkatJNI.CkJavaKeyStore_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 302 */     return chilkatJNI.CkJavaKeyStore_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJavaKeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */