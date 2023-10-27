/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkPem
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkPem(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkPem var0) {
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
/*  29 */         chilkatJNI.delete_CkPem(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkPem()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkPem(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkPem_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkPem_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkPem_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkPem_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AppendMode() {
/*  58 */     return chilkatJNI.CkPem_get_AppendMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AppendMode(boolean var1) {
/*  62 */     chilkatJNI.CkPem_put_AppendMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkPem_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkPem_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkPem_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/*  78 */     return chilkatJNI.CkPem_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/*  82 */     chilkatJNI.CkPem_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkPem_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkPem_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkPem_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkPem_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkPem_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkPem_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkPem_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkPem_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCerts() {
/* 118 */     return chilkatJNI.CkPem_get_NumCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumCsrs() {
/* 122 */     return chilkatJNI.CkPem_get_NumCsrs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumPrivateKeys() {
/* 126 */     return chilkatJNI.CkPem_get_NumPrivateKeys(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumPublicKeys() {
/* 130 */     return chilkatJNI.CkPem_get_NumPublicKeys(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_PrivateKeyFormat(CkString var1) {
/* 134 */     chilkatJNI.CkPem_get_PrivateKeyFormat(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String privateKeyFormat() {
/* 138 */     return chilkatJNI.CkPem_privateKeyFormat(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PrivateKeyFormat(String var1) {
/* 142 */     chilkatJNI.CkPem_put_PrivateKeyFormat(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_PublicKeyFormat(CkString var1) {
/* 146 */     chilkatJNI.CkPem_get_PublicKeyFormat(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String publicKeyFormat() {
/* 150 */     return chilkatJNI.CkPem_publicKeyFormat(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PublicKeyFormat(String var1) {
/* 154 */     chilkatJNI.CkPem_put_PublicKeyFormat(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 158 */     return chilkatJNI.CkPem_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 162 */     chilkatJNI.CkPem_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 166 */     chilkatJNI.CkPem_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 170 */     return chilkatJNI.CkPem_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddCert(CkCert var1, boolean var2) {
/* 174 */     return chilkatJNI.CkPem_AddCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddItem(String var1, String var2, String var3) {
/* 178 */     return chilkatJNI.CkPem_AddItem(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddPrivateKey(CkPrivateKey var1) {
/* 182 */     return chilkatJNI.CkPem_AddPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddPrivateKey2(CkPrivateKey var1, CkCertChain var2) {
/* 186 */     return chilkatJNI.CkPem_AddPrivateKey2(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkCertChain.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean AddPublicKey(CkPublicKey var1) {
/* 190 */     return chilkatJNI.CkPem_AddPublicKey(this.swigCPtr, this, CkPublicKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean Clear() {
/* 194 */     return chilkatJNI.CkPem_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkCert GetCert(int var1) {
/* 198 */     long var2 = chilkatJNI.CkPem_GetCert(this.swigCPtr, this, var1);
/* 199 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedItem(String var1, String var2, String var3, int var4, CkString var5) {
/* 203 */     return chilkatJNI.CkPem_GetEncodedItem(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*     */   }
/*     */   
/*     */   public String getEncodedItem(String var1, String var2, String var3, int var4) {
/* 207 */     return chilkatJNI.CkPem_getEncodedItem(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public String encodedItem(String var1, String var2, String var3, int var4) {
/* 211 */     return chilkatJNI.CkPem_encodedItem(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GetPrivateKey(int var1) {
/* 215 */     long var2 = chilkatJNI.CkPem_GetPrivateKey(this.swigCPtr, this, var1);
/* 216 */     return var2 == 0L ? null : new CkPrivateKey(var2, true);
/*     */   }
/*     */   
/*     */   public CkPublicKey GetPublicKey(int var1) {
/* 220 */     long var2 = chilkatJNI.CkPem_GetPublicKey(this.swigCPtr, this, var1);
/* 221 */     return var2 == 0L ? null : new CkPublicKey(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadP7b(CkByteData var1) {
/* 225 */     return chilkatJNI.CkPem_LoadP7b(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask LoadP7bAsync(CkByteData var1) {
/* 229 */     long var2 = chilkatJNI.CkPem_LoadP7bAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 230 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadP7bFile(String var1) {
/* 234 */     return chilkatJNI.CkPem_LoadP7bFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask LoadP7bFileAsync(String var1) {
/* 238 */     long var2 = chilkatJNI.CkPem_LoadP7bFileAsync(this.swigCPtr, this, var1);
/* 239 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadPem(String var1, String var2) {
/* 243 */     return chilkatJNI.CkPem_LoadPem(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask LoadPemAsync(String var1, String var2) {
/* 247 */     long var3 = chilkatJNI.CkPem_LoadPemAsync(this.swigCPtr, this, var1, var2);
/* 248 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean LoadPemFile(String var1, String var2) {
/* 252 */     return chilkatJNI.CkPem_LoadPemFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask LoadPemFileAsync(String var1, String var2) {
/* 256 */     long var3 = chilkatJNI.CkPem_LoadPemFileAsync(this.swigCPtr, this, var1, var2);
/* 257 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean RemoveCert(int var1) {
/* 261 */     return chilkatJNI.CkPem_RemoveCert(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemovePrivateKey(int var1) {
/* 265 */     return chilkatJNI.CkPem_RemovePrivateKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 269 */     return chilkatJNI.CkPem_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkJavaKeyStore ToJks(String var1, String var2) {
/* 273 */     long var3 = chilkatJNI.CkPem_ToJks(this.swigCPtr, this, var1, var2);
/* 274 */     return var3 == 0L ? null : new CkJavaKeyStore(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ToPem(CkString var1) {
/* 278 */     return chilkatJNI.CkPem_ToPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toPem() {
/* 282 */     return chilkatJNI.CkPem_toPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToPemEx(boolean var1, boolean var2, boolean var3, boolean var4, String var5, String var6, CkString var7) {
/* 286 */     return chilkatJNI.CkPem_ToPemEx(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, CkString.getCPtr(var7), var7);
/*     */   }
/*     */   
/*     */   public String toPemEx(boolean var1, boolean var2, boolean var3, boolean var4, String var5, String var6) {
/* 290 */     return chilkatJNI.CkPem_toPemEx(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*     */   }
/*     */   
/*     */   public CkPfx ToPfx() {
/* 294 */     long var1 = chilkatJNI.CkPem_ToPfx(this.swigCPtr, this);
/* 295 */     return var1 == 0L ? null : new CkPfx(var1, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkPem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */