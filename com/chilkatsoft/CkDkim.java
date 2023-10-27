/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkDkim
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkDkim(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkDkim var0) {
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
/*  29 */         chilkatJNI.delete_CkDkim(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkDkim()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkDkim(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkDkim_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkDkim_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkDkim_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkDkim_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkDkim_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkDkim_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkDkim_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkDkim_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkDkim_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DkimAlg(CkString var1) {
/*  78 */     chilkatJNI.CkDkim_get_DkimAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dkimAlg() {
/*  82 */     return chilkatJNI.CkDkim_dkimAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimAlg(String var1) {
/*  86 */     chilkatJNI.CkDkim_put_DkimAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DkimBodyLengthCount() {
/*  90 */     return chilkatJNI.CkDkim_get_DkimBodyLengthCount(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimBodyLengthCount(int var1) {
/*  94 */     chilkatJNI.CkDkim_put_DkimBodyLengthCount(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DkimCanon(CkString var1) {
/*  98 */     chilkatJNI.CkDkim_get_DkimCanon(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dkimCanon() {
/* 102 */     return chilkatJNI.CkDkim_dkimCanon(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimCanon(String var1) {
/* 106 */     chilkatJNI.CkDkim_put_DkimCanon(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DkimDomain(CkString var1) {
/* 110 */     chilkatJNI.CkDkim_get_DkimDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dkimDomain() {
/* 114 */     return chilkatJNI.CkDkim_dkimDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimDomain(String var1) {
/* 118 */     chilkatJNI.CkDkim_put_DkimDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DkimHeaders(CkString var1) {
/* 122 */     chilkatJNI.CkDkim_get_DkimHeaders(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dkimHeaders() {
/* 126 */     return chilkatJNI.CkDkim_dkimHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimHeaders(String var1) {
/* 130 */     chilkatJNI.CkDkim_put_DkimHeaders(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DkimSelector(CkString var1) {
/* 134 */     chilkatJNI.CkDkim_get_DkimSelector(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dkimSelector() {
/* 138 */     return chilkatJNI.CkDkim_dkimSelector(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DkimSelector(String var1) {
/* 142 */     chilkatJNI.CkDkim_put_DkimSelector(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DomainKeyAlg(CkString var1) {
/* 146 */     chilkatJNI.CkDkim_get_DomainKeyAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domainKeyAlg() {
/* 150 */     return chilkatJNI.CkDkim_domainKeyAlg(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DomainKeyAlg(String var1) {
/* 154 */     chilkatJNI.CkDkim_put_DomainKeyAlg(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DomainKeyCanon(CkString var1) {
/* 158 */     chilkatJNI.CkDkim_get_DomainKeyCanon(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domainKeyCanon() {
/* 162 */     return chilkatJNI.CkDkim_domainKeyCanon(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DomainKeyCanon(String var1) {
/* 166 */     chilkatJNI.CkDkim_put_DomainKeyCanon(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DomainKeyDomain(CkString var1) {
/* 170 */     chilkatJNI.CkDkim_get_DomainKeyDomain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domainKeyDomain() {
/* 174 */     return chilkatJNI.CkDkim_domainKeyDomain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DomainKeyDomain(String var1) {
/* 178 */     chilkatJNI.CkDkim_put_DomainKeyDomain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DomainKeyHeaders(CkString var1) {
/* 182 */     chilkatJNI.CkDkim_get_DomainKeyHeaders(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domainKeyHeaders() {
/* 186 */     return chilkatJNI.CkDkim_domainKeyHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DomainKeyHeaders(String var1) {
/* 190 */     chilkatJNI.CkDkim_put_DomainKeyHeaders(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DomainKeySelector(CkString var1) {
/* 194 */     chilkatJNI.CkDkim_get_DomainKeySelector(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domainKeySelector() {
/* 198 */     return chilkatJNI.CkDkim_domainKeySelector(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DomainKeySelector(String var1) {
/* 202 */     chilkatJNI.CkDkim_put_DomainKeySelector(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 206 */     return chilkatJNI.CkDkim_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 210 */     chilkatJNI.CkDkim_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 214 */     chilkatJNI.CkDkim_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 218 */     return chilkatJNI.CkDkim_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 222 */     chilkatJNI.CkDkim_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 226 */     return chilkatJNI.CkDkim_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 230 */     chilkatJNI.CkDkim_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 234 */     return chilkatJNI.CkDkim_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 238 */     return chilkatJNI.CkDkim_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 242 */     chilkatJNI.CkDkim_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 246 */     return chilkatJNI.CkDkim_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 250 */     chilkatJNI.CkDkim_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 254 */     chilkatJNI.CkDkim_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 258 */     return chilkatJNI.CkDkim_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddDkimSignature(CkByteData var1, CkByteData var2) {
/* 262 */     return chilkatJNI.CkDkim_AddDkimSignature(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean AddDomainKeySignature(CkByteData var1, CkByteData var2) {
/* 266 */     return chilkatJNI.CkDkim_AddDomainKeySignature(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean DkimSign(CkBinData var1) {
/* 270 */     return chilkatJNI.CkDkim_DkimSign(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DkimVerify(int var1, CkBinData var2) {
/* 274 */     return chilkatJNI.CkDkim_DkimVerify(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean DomainKeySign(CkBinData var1) {
/* 278 */     return chilkatJNI.CkDkim_DomainKeySign(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DomainKeyVerify(int var1, CkBinData var2) {
/* 282 */     return chilkatJNI.CkDkim_DomainKeyVerify(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDkimPk(String var1, String var2) {
/* 286 */     return chilkatJNI.CkDkim_LoadDkimPk(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDkimPkBytes(CkByteData var1, String var2) {
/* 290 */     return chilkatJNI.CkDkim_LoadDkimPkBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDkimPkFile(String var1, String var2) {
/* 294 */     return chilkatJNI.CkDkim_LoadDkimPkFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDomainKeyPk(String var1, String var2) {
/* 298 */     return chilkatJNI.CkDkim_LoadDomainKeyPk(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDomainKeyPkBytes(CkByteData var1, String var2) {
/* 302 */     return chilkatJNI.CkDkim_LoadDomainKeyPkBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadDomainKeyPkFile(String var1, String var2) {
/* 306 */     return chilkatJNI.CkDkim_LoadDomainKeyPkFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPublicKey(String var1, String var2, String var3) {
/* 310 */     return chilkatJNI.CkDkim_LoadPublicKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadPublicKeyFile(String var1, String var2, String var3) {
/* 314 */     return chilkatJNI.CkDkim_LoadPublicKeyFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public int NumDkimSignatures(CkByteData var1) {
/* 318 */     return chilkatJNI.CkDkim_NumDkimSignatures(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int NumDomainKeySignatures(CkByteData var1) {
/* 322 */     return chilkatJNI.CkDkim_NumDomainKeySignatures(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean PrefetchPublicKey(String var1, String var2) {
/* 326 */     return chilkatJNI.CkDkim_PrefetchPublicKey(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask PrefetchPublicKeyAsync(String var1, String var2) {
/* 330 */     long var3 = chilkatJNI.CkDkim_PrefetchPublicKeyAsync(this.swigCPtr, this, var1, var2);
/* 331 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 335 */     return chilkatJNI.CkDkim_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetDkimPrivateKey(CkPrivateKey var1) {
/* 339 */     return chilkatJNI.CkDkim_SetDkimPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetDomainKeyPrivateKey(CkPrivateKey var1) {
/* 343 */     return chilkatJNI.CkDkim_SetDomainKeyPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 347 */     return chilkatJNI.CkDkim_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyDkimSignature(int var1, CkByteData var2) {
/* 351 */     return chilkatJNI.CkDkim_VerifyDkimSignature(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask VerifyDkimSignatureAsync(int var1, CkByteData var2) {
/* 355 */     long var3 = chilkatJNI.CkDkim_VerifyDkimSignatureAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 356 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean VerifyDomainKeySignature(int var1, CkByteData var2) {
/* 360 */     return chilkatJNI.CkDkim_VerifyDomainKeySignature(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask VerifyDomainKeySignatureAsync(int var1, CkByteData var2) {
/* 364 */     long var3 = chilkatJNI.CkDkim_VerifyDomainKeySignatureAsync(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/* 365 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkDkim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */