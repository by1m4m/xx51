/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkCrypt2
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkCrypt2(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkCrypt2 var0) {
/*   18 */     return var0 == null ? 0L : var0.swigCPtr;
/*      */   }
/*      */   
/*      */   protected void finalize() {
/*   22 */     delete();
/*      */   }
/*      */   
/*      */   public synchronized void delete() {
/*   26 */     if (this.swigCPtr != 0L) {
/*   27 */       if (this.swigCMemOwn) {
/*   28 */         this.swigCMemOwn = false;
/*   29 */         chilkatJNI.delete_CkCrypt2(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkCrypt2()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkCrypt2(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkCrypt2_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkCrypt2_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkCrypt2_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*   54 */     chilkatJNI.CkCrypt2_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean get_AbortCurrent() {
/*   58 */     return chilkatJNI.CkCrypt2_get_AbortCurrent(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_AbortCurrent(boolean var1) {
/*   62 */     chilkatJNI.CkCrypt2_put_AbortCurrent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BCryptWorkFactor() {
/*   66 */     return chilkatJNI.CkCrypt2_get_BCryptWorkFactor(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BCryptWorkFactor(int var1) {
/*   70 */     chilkatJNI.CkCrypt2_put_BCryptWorkFactor(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_BlockSize() {
/*   74 */     return chilkatJNI.CkCrypt2_get_BlockSize(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_CadesEnabled() {
/*   78 */     return chilkatJNI.CkCrypt2_get_CadesEnabled(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CadesEnabled(boolean var1) {
/*   82 */     chilkatJNI.CkCrypt2_put_CadesEnabled(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CadesSigPolicyHash(CkString var1) {
/*   86 */     chilkatJNI.CkCrypt2_get_CadesSigPolicyHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cadesSigPolicyHash() {
/*   90 */     return chilkatJNI.CkCrypt2_cadesSigPolicyHash(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CadesSigPolicyHash(String var1) {
/*   94 */     chilkatJNI.CkCrypt2_put_CadesSigPolicyHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CadesSigPolicyId(CkString var1) {
/*   98 */     chilkatJNI.CkCrypt2_get_CadesSigPolicyId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cadesSigPolicyId() {
/*  102 */     return chilkatJNI.CkCrypt2_cadesSigPolicyId(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CadesSigPolicyId(String var1) {
/*  106 */     chilkatJNI.CkCrypt2_put_CadesSigPolicyId(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CadesSigPolicyUri(CkString var1) {
/*  110 */     chilkatJNI.CkCrypt2_get_CadesSigPolicyUri(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cadesSigPolicyUri() {
/*  114 */     return chilkatJNI.CkCrypt2_cadesSigPolicyUri(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CadesSigPolicyUri(String var1) {
/*  118 */     chilkatJNI.CkCrypt2_put_CadesSigPolicyUri(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Charset(CkString var1) {
/*  122 */     chilkatJNI.CkCrypt2_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String charset() {
/*  126 */     return chilkatJNI.CkCrypt2_charset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Charset(String var1) {
/*  130 */     chilkatJNI.CkCrypt2_put_Charset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CipherMode(CkString var1) {
/*  134 */     chilkatJNI.CkCrypt2_get_CipherMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cipherMode() {
/*  138 */     return chilkatJNI.CkCrypt2_cipherMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CipherMode(String var1) {
/*  142 */     chilkatJNI.CkCrypt2_put_CipherMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CompressionAlgorithm(CkString var1) {
/*  146 */     chilkatJNI.CkCrypt2_get_CompressionAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String compressionAlgorithm() {
/*  150 */     return chilkatJNI.CkCrypt2_compressionAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CompressionAlgorithm(String var1) {
/*  154 */     chilkatJNI.CkCrypt2_put_CompressionAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_CryptAlgorithm(CkString var1) {
/*  158 */     chilkatJNI.CkCrypt2_get_CryptAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String cryptAlgorithm() {
/*  162 */     return chilkatJNI.CkCrypt2_cryptAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_CryptAlgorithm(String var1) {
/*  166 */     chilkatJNI.CkCrypt2_put_CryptAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*  170 */     chilkatJNI.CkCrypt2_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*  174 */     return chilkatJNI.CkCrypt2_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*  178 */     chilkatJNI.CkCrypt2_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_EncodingMode(CkString var1) {
/*  182 */     chilkatJNI.CkCrypt2_get_EncodingMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String encodingMode() {
/*  186 */     return chilkatJNI.CkCrypt2_encodingMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_EncodingMode(String var1) {
/*  190 */     chilkatJNI.CkCrypt2_put_EncodingMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_FirstChunk() {
/*  194 */     return chilkatJNI.CkCrypt2_get_FirstChunk(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FirstChunk(boolean var1) {
/*  198 */     chilkatJNI.CkCrypt2_put_FirstChunk(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_HashAlgorithm(CkString var1) {
/*  202 */     chilkatJNI.CkCrypt2_get_HashAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String hashAlgorithm() {
/*  206 */     return chilkatJNI.CkCrypt2_hashAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HashAlgorithm(String var1) {
/*  210 */     chilkatJNI.CkCrypt2_put_HashAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HavalRounds() {
/*  214 */     return chilkatJNI.CkCrypt2_get_HavalRounds(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HavalRounds(int var1) {
/*  218 */     chilkatJNI.CkCrypt2_put_HavalRounds(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_HeartbeatMs() {
/*  222 */     return chilkatJNI.CkCrypt2_get_HeartbeatMs(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_HeartbeatMs(int var1) {
/*  226 */     chilkatJNI.CkCrypt2_put_HeartbeatMs(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_IncludeCertChain() {
/*  230 */     return chilkatJNI.CkCrypt2_get_IncludeCertChain(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IncludeCertChain(boolean var1) {
/*  234 */     chilkatJNI.CkCrypt2_put_IncludeCertChain(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_InitialCount() {
/*  238 */     return chilkatJNI.CkCrypt2_get_InitialCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_InitialCount(int var1) {
/*  242 */     chilkatJNI.CkCrypt2_put_InitialCount(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_IterationCount() {
/*  246 */     return chilkatJNI.CkCrypt2_get_IterationCount(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_IterationCount(int var1) {
/*  250 */     chilkatJNI.CkCrypt2_put_IterationCount(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_IV(CkByteData var1) {
/*  254 */     chilkatJNI.CkCrypt2_get_IV(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_IV(CkByteData var1) {
/*  258 */     chilkatJNI.CkCrypt2_put_IV(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public int get_KeyLength() {
/*  262 */     return chilkatJNI.CkCrypt2_get_KeyLength(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_KeyLength(int var1) {
/*  266 */     chilkatJNI.CkCrypt2_put_KeyLength(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_LastChunk() {
/*  270 */     return chilkatJNI.CkCrypt2_get_LastChunk(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastChunk(boolean var1) {
/*  274 */     chilkatJNI.CkCrypt2_put_LastChunk(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  278 */     chilkatJNI.CkCrypt2_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  282 */     return chilkatJNI.CkCrypt2_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  286 */     chilkatJNI.CkCrypt2_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  290 */     return chilkatJNI.CkCrypt2_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  294 */     chilkatJNI.CkCrypt2_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  298 */     return chilkatJNI.CkCrypt2_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  302 */     return chilkatJNI.CkCrypt2_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  306 */     chilkatJNI.CkCrypt2_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_MacAlgorithm(CkString var1) {
/*  310 */     chilkatJNI.CkCrypt2_get_MacAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String macAlgorithm() {
/*  314 */     return chilkatJNI.CkCrypt2_macAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_MacAlgorithm(String var1) {
/*  318 */     chilkatJNI.CkCrypt2_put_MacAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_NumSignerCerts() {
/*  322 */     return chilkatJNI.CkCrypt2_get_NumSignerCerts(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_OaepHash(CkString var1) {
/*  326 */     chilkatJNI.CkCrypt2_get_OaepHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oaepHash() {
/*  330 */     return chilkatJNI.CkCrypt2_oaepHash(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepHash(String var1) {
/*  334 */     chilkatJNI.CkCrypt2_put_OaepHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OaepMgfHash(CkString var1) {
/*  338 */     chilkatJNI.CkCrypt2_get_OaepMgfHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oaepMgfHash() {
/*  342 */     return chilkatJNI.CkCrypt2_oaepMgfHash(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepMgfHash(String var1) {
/*  346 */     chilkatJNI.CkCrypt2_put_OaepMgfHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_OaepPadding() {
/*  350 */     return chilkatJNI.CkCrypt2_get_OaepPadding(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepPadding(boolean var1) {
/*  354 */     chilkatJNI.CkCrypt2_put_OaepPadding(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_PaddingScheme() {
/*  358 */     return chilkatJNI.CkCrypt2_get_PaddingScheme(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PaddingScheme(int var1) {
/*  362 */     chilkatJNI.CkCrypt2_put_PaddingScheme(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PbesAlgorithm(CkString var1) {
/*  366 */     chilkatJNI.CkCrypt2_get_PbesAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String pbesAlgorithm() {
/*  370 */     return chilkatJNI.CkCrypt2_pbesAlgorithm(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PbesAlgorithm(String var1) {
/*  374 */     chilkatJNI.CkCrypt2_put_PbesAlgorithm(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PbesPassword(CkString var1) {
/*  378 */     chilkatJNI.CkCrypt2_get_PbesPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String pbesPassword() {
/*  382 */     return chilkatJNI.CkCrypt2_pbesPassword(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PbesPassword(String var1) {
/*  386 */     chilkatJNI.CkCrypt2_put_PbesPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Pkcs7CryptAlg(CkString var1) {
/*  390 */     chilkatJNI.CkCrypt2_get_Pkcs7CryptAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String pkcs7CryptAlg() {
/*  394 */     return chilkatJNI.CkCrypt2_pkcs7CryptAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Pkcs7CryptAlg(String var1) {
/*  398 */     chilkatJNI.CkCrypt2_put_Pkcs7CryptAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Rc2EffectiveKeyLength() {
/*  402 */     return chilkatJNI.CkCrypt2_get_Rc2EffectiveKeyLength(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Rc2EffectiveKeyLength(int var1) {
/*  406 */     chilkatJNI.CkCrypt2_put_Rc2EffectiveKeyLength(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Salt(CkByteData var1) {
/*  410 */     chilkatJNI.CkCrypt2_get_Salt(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_Salt(CkByteData var1) {
/*  414 */     chilkatJNI.CkCrypt2_put_Salt(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_SecretKey(CkByteData var1) {
/*  418 */     chilkatJNI.CkCrypt2_get_SecretKey(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_SecretKey(CkByteData var1) {
/*  422 */     chilkatJNI.CkCrypt2_put_SecretKey(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_SigningAlg(CkString var1) {
/*  426 */     chilkatJNI.CkCrypt2_get_SigningAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String signingAlg() {
/*  430 */     return chilkatJNI.CkCrypt2_signingAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SigningAlg(String var1) {
/*  434 */     chilkatJNI.CkCrypt2_put_SigningAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SigningAttributes(CkString var1) {
/*  438 */     chilkatJNI.CkCrypt2_get_SigningAttributes(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String signingAttributes() {
/*  442 */     return chilkatJNI.CkCrypt2_signingAttributes(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SigningAttributes(String var1) {
/*  446 */     chilkatJNI.CkCrypt2_put_SigningAttributes(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_UuFilename(CkString var1) {
/*  450 */     chilkatJNI.CkCrypt2_get_UuFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String uuFilename() {
/*  454 */     return chilkatJNI.CkCrypt2_uuFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UuFilename(String var1) {
/*  458 */     chilkatJNI.CkCrypt2_put_UuFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_UuMode(CkString var1) {
/*  462 */     chilkatJNI.CkCrypt2_get_UuMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String uuMode() {
/*  466 */     return chilkatJNI.CkCrypt2_uuMode(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UuMode(String var1) {
/*  470 */     chilkatJNI.CkCrypt2_put_UuMode(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  474 */     return chilkatJNI.CkCrypt2_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  478 */     chilkatJNI.CkCrypt2_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  482 */     chilkatJNI.CkCrypt2_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  486 */     return chilkatJNI.CkCrypt2_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void AddEncryptCert(CkCert var1) {
/*  490 */     chilkatJNI.CkCrypt2_AddEncryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceData(CkByteData var1, String var2) {
/*  494 */     return chilkatJNI.CkCrypt2_AddPfxSourceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceFile(String var1, String var2) {
/*  498 */     return chilkatJNI.CkCrypt2_AddPfxSourceFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AesKeyUnwrap(String var1, String var2, String var3, CkString var4) {
/*  502 */     return chilkatJNI.CkCrypt2_AesKeyUnwrap(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String aesKeyUnwrap(String var1, String var2, String var3) {
/*  506 */     return chilkatJNI.CkCrypt2_aesKeyUnwrap(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AesKeyWrap(String var1, String var2, String var3, CkString var4) {
/*  510 */     return chilkatJNI.CkCrypt2_AesKeyWrap(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String aesKeyWrap(String var1, String var2, String var3) {
/*  514 */     return chilkatJNI.CkCrypt2_aesKeyWrap(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean BCryptHash(String var1, CkString var2) {
/*  518 */     return chilkatJNI.CkCrypt2_BCryptHash(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String bCryptHash(String var1) {
/*  522 */     return chilkatJNI.CkCrypt2_bCryptHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean BCryptVerify(String var1, String var2) {
/*  526 */     return chilkatJNI.CkCrypt2_BCryptVerify(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean BytesToString(CkByteData var1, String var2, CkString var3) {
/*  530 */     return chilkatJNI.CkCrypt2_BytesToString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String bytesToString(CkByteData var1, String var2) {
/*  534 */     return chilkatJNI.CkCrypt2_bytesToString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean ByteSwap4321(CkByteData var1, CkByteData var2) {
/*  538 */     return chilkatJNI.CkCrypt2_ByteSwap4321(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean CkDecryptFile(String var1, String var2) {
/*  542 */     return chilkatJNI.CkCrypt2_CkDecryptFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask CkDecryptFileAsync(String var1, String var2) {
/*  546 */     long var3 = chilkatJNI.CkCrypt2_CkDecryptFileAsync(this.swigCPtr, this, var1, var2);
/*  547 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean CkEncryptFile(String var1, String var2) {
/*  551 */     return chilkatJNI.CkCrypt2_CkEncryptFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask CkEncryptFileAsync(String var1, String var2) {
/*  555 */     long var3 = chilkatJNI.CkCrypt2_CkEncryptFileAsync(this.swigCPtr, this, var1, var2);
/*  556 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public void ClearEncryptCerts() {
/*  560 */     chilkatJNI.CkCrypt2_ClearEncryptCerts(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean CompressBytes(CkByteData var1, CkByteData var2) {
/*  564 */     return chilkatJNI.CkCrypt2_CompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean CompressBytesENC(CkByteData var1, CkString var2) {
/*  568 */     return chilkatJNI.CkCrypt2_CompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String compressBytesENC(CkByteData var1) {
/*  572 */     return chilkatJNI.CkCrypt2_compressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean CompressString(String var1, CkByteData var2) {
/*  576 */     return chilkatJNI.CkCrypt2_CompressString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean CompressStringENC(String var1, CkString var2) {
/*  580 */     return chilkatJNI.CkCrypt2_CompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String compressStringENC(String var1) {
/*  584 */     return chilkatJNI.CkCrypt2_compressStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public long CrcBytes(String var1, CkByteData var2) {
/*  588 */     return chilkatJNI.CkCrypt2_CrcBytes(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public long CrcFile(String var1, String var2) {
/*  592 */     return chilkatJNI.CkCrypt2_CrcFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkTask CrcFileAsync(String var1, String var2) {
/*  596 */     long var3 = chilkatJNI.CkCrypt2_CrcFileAsync(this.swigCPtr, this, var1, var2);
/*  597 */     return var3 == 0L ? null : new CkTask(var3, true);
/*      */   }
/*      */   
/*      */   public boolean CreateDetachedSignature(String var1, String var2) {
/*  601 */     return chilkatJNI.CkCrypt2_CreateDetachedSignature(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean CreateP7M(String var1, String var2) {
/*  605 */     return chilkatJNI.CkCrypt2_CreateP7M(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean CreateP7S(String var1, String var2) {
/*  609 */     return chilkatJNI.CkCrypt2_CreateP7S(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean Decode(String var1, String var2, CkByteData var3) {
/*  613 */     return chilkatJNI.CkCrypt2_Decode(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean DecodeString(String var1, String var2, String var3, CkString var4) {
/*  617 */     return chilkatJNI.CkCrypt2_DecodeString(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String decodeString(String var1, String var2, String var3) {
/*  621 */     return chilkatJNI.CkCrypt2_decodeString(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean DecryptBd(CkBinData var1) {
/*  625 */     return chilkatJNI.CkCrypt2_DecryptBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean DecryptBytes(CkByteData var1, CkByteData var2) {
/*  629 */     return chilkatJNI.CkCrypt2_DecryptBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean DecryptBytesENC(String var1, CkByteData var2) {
/*  633 */     return chilkatJNI.CkCrypt2_DecryptBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean DecryptEncoded(String var1, CkString var2) {
/*  637 */     return chilkatJNI.CkCrypt2_DecryptEncoded(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String decryptEncoded(String var1) {
/*  641 */     return chilkatJNI.CkCrypt2_decryptEncoded(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean DecryptSb(CkBinData var1, CkStringBuilder var2) {
/*  645 */     return chilkatJNI.CkCrypt2_DecryptSb(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkStringBuilder.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean DecryptSecureENC(String var1, CkSecureString var2) {
/*  649 */     return chilkatJNI.CkCrypt2_DecryptSecureENC(this.swigCPtr, this, var1, CkSecureString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean DecryptStream(CkStream var1) {
/*  653 */     return chilkatJNI.CkCrypt2_DecryptStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask DecryptStreamAsync(CkStream var1) {
/*  657 */     long var2 = chilkatJNI.CkCrypt2_DecryptStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*  658 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean DecryptString(CkByteData var1, CkString var2) {
/*  662 */     return chilkatJNI.CkCrypt2_DecryptString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String decryptString(CkByteData var1) {
/*  666 */     return chilkatJNI.CkCrypt2_decryptString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean DecryptStringENC(String var1, CkString var2) {
/*  670 */     return chilkatJNI.CkCrypt2_DecryptStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String decryptStringENC(String var1) {
/*  674 */     return chilkatJNI.CkCrypt2_decryptStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean Encode(CkByteData var1, String var2, CkString var3) {
/*  678 */     return chilkatJNI.CkCrypt2_Encode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String encode(CkByteData var1, String var2) {
/*  682 */     return chilkatJNI.CkCrypt2_encode(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean EncodeString(String var1, String var2, String var3, CkString var4) {
/*  686 */     return chilkatJNI.CkCrypt2_EncodeString(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String encodeString(String var1, String var2, String var3) {
/*  690 */     return chilkatJNI.CkCrypt2_encodeString(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean EncryptBd(CkBinData var1) {
/*  694 */     return chilkatJNI.CkCrypt2_EncryptBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean EncryptBytes(CkByteData var1, CkByteData var2) {
/*  698 */     return chilkatJNI.CkCrypt2_EncryptBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean EncryptBytesENC(CkByteData var1, CkString var2) {
/*  702 */     return chilkatJNI.CkCrypt2_EncryptBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String encryptBytesENC(CkByteData var1) {
/*  706 */     return chilkatJNI.CkCrypt2_encryptBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean EncryptEncoded(String var1, CkString var2) {
/*  710 */     return chilkatJNI.CkCrypt2_EncryptEncoded(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String encryptEncoded(String var1) {
/*  714 */     return chilkatJNI.CkCrypt2_encryptEncoded(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean EncryptSb(CkStringBuilder var1, CkBinData var2) {
/*  718 */     return chilkatJNI.CkCrypt2_EncryptSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean EncryptSecureENC(CkSecureString var1, CkString var2) {
/*  722 */     return chilkatJNI.CkCrypt2_EncryptSecureENC(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String encryptSecureENC(CkSecureString var1) {
/*  726 */     return chilkatJNI.CkCrypt2_encryptSecureENC(this.swigCPtr, this, CkSecureString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean EncryptStream(CkStream var1) {
/*  730 */     return chilkatJNI.CkCrypt2_EncryptStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public CkTask EncryptStreamAsync(CkStream var1) {
/*  734 */     long var2 = chilkatJNI.CkCrypt2_EncryptStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*  735 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean EncryptString(String var1, CkByteData var2) {
/*  739 */     return chilkatJNI.CkCrypt2_EncryptString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean EncryptStringENC(String var1, CkString var2) {
/*  743 */     return chilkatJNI.CkCrypt2_EncryptStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String encryptStringENC(String var1) {
/*  747 */     return chilkatJNI.CkCrypt2_encryptStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GenEncodedSecretKey(String var1, String var2, CkString var3) {
/*  751 */     return chilkatJNI.CkCrypt2_GenEncodedSecretKey(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String genEncodedSecretKey(String var1, String var2) {
/*  755 */     return chilkatJNI.CkCrypt2_genEncodedSecretKey(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GenerateSecretKey(String var1, CkByteData var2) {
/*  759 */     return chilkatJNI.CkCrypt2_GenerateSecretKey(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GenerateUuid(CkString var1) {
/*  763 */     return chilkatJNI.CkCrypt2_GenerateUuid(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String generateUuid() {
/*  767 */     return chilkatJNI.CkCrypt2_generateUuid(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean GenRandomBytesENC(int var1, CkString var2) {
/*  771 */     return chilkatJNI.CkCrypt2_GenRandomBytesENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String genRandomBytesENC(int var1) {
/*  775 */     return chilkatJNI.CkCrypt2_genRandomBytesENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert GetDecryptCert() {
/*  779 */     long var1 = chilkatJNI.CkCrypt2_GetDecryptCert(this.swigCPtr, this);
/*  780 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetEncodedAad(String var1, CkString var2) {
/*  784 */     return chilkatJNI.CkCrypt2_GetEncodedAad(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getEncodedAad(String var1) {
/*  788 */     return chilkatJNI.CkCrypt2_getEncodedAad(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String encodedAad(String var1) {
/*  792 */     return chilkatJNI.CkCrypt2_encodedAad(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetEncodedAuthTag(String var1, CkString var2) {
/*  796 */     return chilkatJNI.CkCrypt2_GetEncodedAuthTag(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getEncodedAuthTag(String var1) {
/*  800 */     return chilkatJNI.CkCrypt2_getEncodedAuthTag(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String encodedAuthTag(String var1) {
/*  804 */     return chilkatJNI.CkCrypt2_encodedAuthTag(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetEncodedIV(String var1, CkString var2) {
/*  808 */     return chilkatJNI.CkCrypt2_GetEncodedIV(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getEncodedIV(String var1) {
/*  812 */     return chilkatJNI.CkCrypt2_getEncodedIV(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String encodedIV(String var1) {
/*  816 */     return chilkatJNI.CkCrypt2_encodedIV(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetEncodedKey(String var1, CkString var2) {
/*  820 */     return chilkatJNI.CkCrypt2_GetEncodedKey(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getEncodedKey(String var1) {
/*  824 */     return chilkatJNI.CkCrypt2_getEncodedKey(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String encodedKey(String var1) {
/*  828 */     return chilkatJNI.CkCrypt2_encodedKey(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetEncodedSalt(String var1, CkString var2) {
/*  832 */     return chilkatJNI.CkCrypt2_GetEncodedSalt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getEncodedSalt(String var1) {
/*  836 */     return chilkatJNI.CkCrypt2_getEncodedSalt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String encodedSalt(String var1) {
/*  840 */     return chilkatJNI.CkCrypt2_encodedSalt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert GetLastCert() {
/*  844 */     long var1 = chilkatJNI.CkCrypt2_GetLastCert(this.swigCPtr, this);
/*  845 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetSignatureSigningTime(int var1, SYSTEMTIME var2) {
/*  849 */     return chilkatJNI.CkCrypt2_GetSignatureSigningTime(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetSignatureSigningTimeStr(int var1, CkString var2) {
/*  853 */     return chilkatJNI.CkCrypt2_GetSignatureSigningTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getSignatureSigningTimeStr(int var1) {
/*  857 */     return chilkatJNI.CkCrypt2_getSignatureSigningTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String signatureSigningTimeStr(int var1) {
/*  861 */     return chilkatJNI.CkCrypt2_signatureSigningTimeStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetSignedAttributes(int var1, CkBinData var2, CkStringBuilder var3) {
/*  865 */     return chilkatJNI.CkCrypt2_GetSignedAttributes(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2, CkStringBuilder.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public CkCert GetSignerCert(int var1) {
/*  869 */     long var2 = chilkatJNI.CkCrypt2_GetSignerCert(this.swigCPtr, this, var1);
/*  870 */     return var2 == 0L ? null : new CkCert(var2, true);
/*      */   }
/*      */   
/*      */   public CkCertChain GetSignerCertChain(int var1) {
/*  874 */     long var2 = chilkatJNI.CkCrypt2_GetSignerCertChain(this.swigCPtr, this, var1);
/*  875 */     return var2 == 0L ? null : new CkCertChain(var2, true);
/*      */   }
/*      */   
/*      */   public boolean HashBdENC(CkBinData var1, CkString var2) {
/*  879 */     return chilkatJNI.CkCrypt2_HashBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hashBdENC(CkBinData var1) {
/*  883 */     return chilkatJNI.CkCrypt2_hashBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HashBeginBytes(CkByteData var1) {
/*  887 */     return chilkatJNI.CkCrypt2_HashBeginBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HashBeginString(String var1) {
/*  891 */     return chilkatJNI.CkCrypt2_HashBeginString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean HashBytes(CkByteData var1, CkByteData var2) {
/*  895 */     return chilkatJNI.CkCrypt2_HashBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean HashBytesENC(CkByteData var1, CkString var2) {
/*  899 */     return chilkatJNI.CkCrypt2_HashBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hashBytesENC(CkByteData var1) {
/*  903 */     return chilkatJNI.CkCrypt2_hashBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HashFile(String var1, CkByteData var2) {
/*  907 */     return chilkatJNI.CkCrypt2_HashFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public CkTask HashFileAsync(String var1) {
/*  911 */     long var2 = chilkatJNI.CkCrypt2_HashFileAsync(this.swigCPtr, this, var1);
/*  912 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean HashFileENC(String var1, CkString var2) {
/*  916 */     return chilkatJNI.CkCrypt2_HashFileENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hashFileENC(String var1) {
/*  920 */     return chilkatJNI.CkCrypt2_hashFileENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkTask HashFileENCAsync(String var1) {
/*  924 */     long var2 = chilkatJNI.CkCrypt2_HashFileENCAsync(this.swigCPtr, this, var1);
/*  925 */     return var2 == 0L ? null : new CkTask(var2, true);
/*      */   }
/*      */   
/*      */   public boolean HashFinal(CkByteData var1) {
/*  929 */     return chilkatJNI.CkCrypt2_HashFinal(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HashFinalENC(CkString var1) {
/*  933 */     return chilkatJNI.CkCrypt2_HashFinalENC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String hashFinalENC() {
/*  937 */     return chilkatJNI.CkCrypt2_hashFinalENC(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean HashMoreBytes(CkByteData var1) {
/*  941 */     return chilkatJNI.CkCrypt2_HashMoreBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HashMoreString(String var1) {
/*  945 */     return chilkatJNI.CkCrypt2_HashMoreString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean HashString(String var1, CkByteData var2) {
/*  949 */     return chilkatJNI.CkCrypt2_HashString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean HashStringENC(String var1, CkString var2) {
/*  953 */     return chilkatJNI.CkCrypt2_HashStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hashStringENC(String var1) {
/*  957 */     return chilkatJNI.CkCrypt2_hashStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean HasSignatureSigningTime(int var1) {
/*  961 */     return chilkatJNI.CkCrypt2_HasSignatureSigningTime(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean HmacBytes(CkByteData var1, CkByteData var2) {
/*  965 */     return chilkatJNI.CkCrypt2_HmacBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean HmacBytesENC(CkByteData var1, CkString var2) {
/*  969 */     return chilkatJNI.CkCrypt2_HmacBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hmacBytesENC(CkByteData var1) {
/*  973 */     return chilkatJNI.CkCrypt2_hmacBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean HmacString(String var1, CkByteData var2) {
/*  977 */     return chilkatJNI.CkCrypt2_HmacString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean HmacStringENC(String var1, CkString var2) {
/*  981 */     return chilkatJNI.CkCrypt2_HmacStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String hmacStringENC(String var1) {
/*  985 */     return chilkatJNI.CkCrypt2_hmacStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean InflateBytes(CkByteData var1, CkByteData var2) {
/*  989 */     return chilkatJNI.CkCrypt2_InflateBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean InflateBytesENC(String var1, CkByteData var2) {
/*  993 */     return chilkatJNI.CkCrypt2_InflateBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean InflateString(CkByteData var1, CkString var2) {
/*  997 */     return chilkatJNI.CkCrypt2_InflateString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String inflateString(CkByteData var1) {
/* 1001 */     return chilkatJNI.CkCrypt2_inflateString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean InflateStringENC(String var1, CkString var2) {
/* 1005 */     return chilkatJNI.CkCrypt2_InflateStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String inflateStringENC(String var1) {
/* 1009 */     return chilkatJNI.CkCrypt2_inflateStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean IsUnlocked() {
/* 1013 */     return chilkatJNI.CkCrypt2_IsUnlocked(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkJsonObject LastJsonData() {
/* 1017 */     long var1 = chilkatJNI.CkCrypt2_LastJsonData(this.swigCPtr, this);
/* 1018 */     return var1 == 0L ? null : new CkJsonObject(var1, true);
/*      */   }
/*      */   
/*      */   public boolean MacBdENC(CkBinData var1, CkString var2) {
/* 1022 */     return chilkatJNI.CkCrypt2_MacBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String macBdENC(CkBinData var1) {
/* 1026 */     return chilkatJNI.CkCrypt2_macBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean MacBytes(CkByteData var1, CkByteData var2) {
/* 1030 */     return chilkatJNI.CkCrypt2_MacBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean MacBytesENC(CkByteData var1, CkString var2) {
/* 1034 */     return chilkatJNI.CkCrypt2_MacBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String macBytesENC(CkByteData var1) {
/* 1038 */     return chilkatJNI.CkCrypt2_macBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean MacString(String var1, CkByteData var2) {
/* 1042 */     return chilkatJNI.CkCrypt2_MacString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean MacStringENC(String var1, CkString var2) {
/* 1046 */     return chilkatJNI.CkCrypt2_MacStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String macStringENC(String var1) {
/* 1050 */     return chilkatJNI.CkCrypt2_macStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean MySqlAesDecrypt(String var1, String var2, CkString var3) {
/* 1054 */     return chilkatJNI.CkCrypt2_MySqlAesDecrypt(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String mySqlAesDecrypt(String var1, String var2) {
/* 1058 */     return chilkatJNI.CkCrypt2_mySqlAesDecrypt(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean MySqlAesEncrypt(String var1, String var2, CkString var3) {
/* 1062 */     return chilkatJNI.CkCrypt2_MySqlAesEncrypt(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String mySqlAesEncrypt(String var1, String var2) {
/* 1066 */     return chilkatJNI.CkCrypt2_mySqlAesEncrypt(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean OpaqueSignBd(CkBinData var1) {
/* 1070 */     return chilkatJNI.CkCrypt2_OpaqueSignBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean OpaqueSignBytes(CkByteData var1, CkByteData var2) {
/* 1074 */     return chilkatJNI.CkCrypt2_OpaqueSignBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean OpaqueSignBytesENC(CkByteData var1, CkString var2) {
/* 1078 */     return chilkatJNI.CkCrypt2_OpaqueSignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String opaqueSignBytesENC(CkByteData var1) {
/* 1082 */     return chilkatJNI.CkCrypt2_opaqueSignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean OpaqueSignString(String var1, CkByteData var2) {
/* 1086 */     return chilkatJNI.CkCrypt2_OpaqueSignString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean OpaqueSignStringENC(String var1, CkString var2) {
/* 1090 */     return chilkatJNI.CkCrypt2_OpaqueSignStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String opaqueSignStringENC(String var1) {
/* 1094 */     return chilkatJNI.CkCrypt2_opaqueSignStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean OpaqueVerifyBd(CkBinData var1) {
/* 1098 */     return chilkatJNI.CkCrypt2_OpaqueVerifyBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean OpaqueVerifyBytes(CkByteData var1, CkByteData var2) {
/* 1102 */     return chilkatJNI.CkCrypt2_OpaqueVerifyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean OpaqueVerifyBytesENC(String var1, CkByteData var2) {
/* 1106 */     return chilkatJNI.CkCrypt2_OpaqueVerifyBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean OpaqueVerifyString(CkByteData var1, CkString var2) {
/* 1110 */     return chilkatJNI.CkCrypt2_OpaqueVerifyString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String opaqueVerifyString(CkByteData var1) {
/* 1114 */     return chilkatJNI.CkCrypt2_opaqueVerifyString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean OpaqueVerifyStringENC(String var1, CkString var2) {
/* 1118 */     return chilkatJNI.CkCrypt2_OpaqueVerifyStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String opaqueVerifyStringENC(String var1) {
/* 1122 */     return chilkatJNI.CkCrypt2_opaqueVerifyStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean Pbkdf1(String var1, String var2, String var3, String var4, int var5, int var6, String var7, CkString var8) {
/* 1126 */     return chilkatJNI.CkCrypt2_Pbkdf1(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7, CkString.getCPtr(var8), var8);
/*      */   }
/*      */   
/*      */   public String pbkdf1(String var1, String var2, String var3, String var4, int var5, int var6, String var7) {
/* 1130 */     return chilkatJNI.CkCrypt2_pbkdf1(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/*      */   }
/*      */   
/*      */   public boolean Pbkdf2(String var1, String var2, String var3, String var4, int var5, int var6, String var7, CkString var8) {
/* 1134 */     return chilkatJNI.CkCrypt2_Pbkdf2(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7, CkString.getCPtr(var8), var8);
/*      */   }
/*      */   
/*      */   public String pbkdf2(String var1, String var2, String var3, String var4, int var5, int var6, String var7) {
/* 1138 */     return chilkatJNI.CkCrypt2_pbkdf2(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, var7);
/*      */   }
/*      */   
/*      */   public boolean Pkcs7ExtractDigest(int var1, String var2, CkString var3) {
/* 1142 */     return chilkatJNI.CkCrypt2_Pkcs7ExtractDigest(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String pkcs7ExtractDigest(int var1, String var2) {
/* 1146 */     return chilkatJNI.CkCrypt2_pkcs7ExtractDigest(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void RandomizeIV() {
/* 1150 */     chilkatJNI.CkCrypt2_RandomizeIV(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void RandomizeKey() {
/* 1154 */     chilkatJNI.CkCrypt2_RandomizeKey(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean ReadFile(String var1, CkByteData var2) {
/* 1158 */     return chilkatJNI.CkCrypt2_ReadFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean ReEncode(String var1, String var2, String var3, CkString var4) {
/* 1162 */     return chilkatJNI.CkCrypt2_ReEncode(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String reEncode(String var1, String var2, String var3) {
/* 1166 */     return chilkatJNI.CkCrypt2_reEncode(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1170 */     return chilkatJNI.CkCrypt2_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetCSP(CkCsp var1) {
/* 1174 */     return chilkatJNI.CkCrypt2_SetCSP(this.swigCPtr, this, CkCsp.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert(CkCert var1) {
/* 1178 */     return chilkatJNI.CkCrypt2_SetDecryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert2(CkCert var1, CkPrivateKey var2) {
/* 1182 */     return chilkatJNI.CkCrypt2_SetDecryptCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetEncodedAad(String var1, String var2) {
/* 1186 */     return chilkatJNI.CkCrypt2_SetEncodedAad(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetEncodedAuthTag(String var1, String var2) {
/* 1190 */     return chilkatJNI.CkCrypt2_SetEncodedAuthTag(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void SetEncodedIV(String var1, String var2) {
/* 1194 */     chilkatJNI.CkCrypt2_SetEncodedIV(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void SetEncodedKey(String var1, String var2) {
/* 1198 */     chilkatJNI.CkCrypt2_SetEncodedKey(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void SetEncodedSalt(String var1, String var2) {
/* 1202 */     chilkatJNI.CkCrypt2_SetEncodedSalt(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetEncryptCert(CkCert var1) {
/* 1206 */     return chilkatJNI.CkCrypt2_SetEncryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void SetHmacKeyBytes(CkByteData var1) {
/* 1210 */     chilkatJNI.CkCrypt2_SetHmacKeyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void SetHmacKeyEncoded(String var1, String var2) {
/* 1214 */     chilkatJNI.CkCrypt2_SetHmacKeyEncoded(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void SetHmacKeyString(String var1) {
/* 1218 */     chilkatJNI.CkCrypt2_SetHmacKeyString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetMacKeyBytes(CkByteData var1) {
/* 1222 */     return chilkatJNI.CkCrypt2_SetMacKeyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetMacKeyEncoded(String var1, String var2) {
/* 1226 */     return chilkatJNI.CkCrypt2_SetMacKeyEncoded(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetMacKeyString(String var1) {
/* 1230 */     return chilkatJNI.CkCrypt2_SetMacKeyString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void SetSecretKeyViaPassword(String var1) {
/* 1234 */     chilkatJNI.CkCrypt2_SetSecretKeyViaPassword(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetSigningCert(CkCert var1) {
/* 1238 */     return chilkatJNI.CkCrypt2_SetSigningCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSigningCert2(CkCert var1, CkPrivateKey var2) {
/* 1242 */     return chilkatJNI.CkCrypt2_SetSigningCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetVerifyCert(CkCert var1) {
/* 1246 */     return chilkatJNI.CkCrypt2_SetVerifyCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SignBdENC(CkBinData var1, CkString var2) {
/* 1250 */     return chilkatJNI.CkCrypt2_SignBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String signBdENC(CkBinData var1) {
/* 1254 */     return chilkatJNI.CkCrypt2_signBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SignBytes(CkByteData var1, CkByteData var2) {
/* 1258 */     return chilkatJNI.CkCrypt2_SignBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SignBytesENC(CkByteData var1, CkString var2) {
/* 1262 */     return chilkatJNI.CkCrypt2_SignBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String signBytesENC(CkByteData var1) {
/* 1266 */     return chilkatJNI.CkCrypt2_signBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SignSbENC(CkStringBuilder var1, CkString var2) {
/* 1270 */     return chilkatJNI.CkCrypt2_SignSbENC(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String signSbENC(CkStringBuilder var1) {
/* 1274 */     return chilkatJNI.CkCrypt2_signSbENC(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SignString(String var1, CkByteData var2) {
/* 1278 */     return chilkatJNI.CkCrypt2_SignString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SignStringENC(String var1, CkString var2) {
/* 1282 */     return chilkatJNI.CkCrypt2_SignStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String signStringENC(String var1) {
/* 1286 */     return chilkatJNI.CkCrypt2_signStringENC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean StringToBytes(String var1, String var2, CkByteData var3) {
/* 1290 */     return chilkatJNI.CkCrypt2_StringToBytes(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean TrimEndingWith(String var1, String var2, CkString var3) {
/* 1294 */     return chilkatJNI.CkCrypt2_TrimEndingWith(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String trimEndingWith(String var1, String var2) {
/* 1298 */     return chilkatJNI.CkCrypt2_trimEndingWith(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean UnlockComponent(String var1) {
/* 1302 */     return chilkatJNI.CkCrypt2_UnlockComponent(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 1306 */     return chilkatJNI.CkCrypt2_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean VerifyBdENC(CkBinData var1, String var2) {
/* 1310 */     return chilkatJNI.CkCrypt2_VerifyBdENC(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyBytes(CkByteData var1, CkByteData var2) {
/* 1314 */     return chilkatJNI.CkCrypt2_VerifyBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyBytesENC(CkByteData var1, String var2) {
/* 1318 */     return chilkatJNI.CkCrypt2_VerifyBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyDetachedSignature(String var1, String var2) {
/* 1322 */     return chilkatJNI.CkCrypt2_VerifyDetachedSignature(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyP7M(String var1, String var2) {
/* 1326 */     return chilkatJNI.CkCrypt2_VerifyP7M(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyP7S(String var1, String var2) {
/* 1330 */     return chilkatJNI.CkCrypt2_VerifyP7S(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifySbENC(CkStringBuilder var1, String var2) {
/* 1334 */     return chilkatJNI.CkCrypt2_VerifySbENC(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyString(String var1, CkByteData var2) {
/* 1338 */     return chilkatJNI.CkCrypt2_VerifyString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean VerifyStringENC(String var1, String var2) {
/* 1342 */     return chilkatJNI.CkCrypt2_VerifyStringENC(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean WriteFile(String var1, CkByteData var2) {
/* 1346 */     return chilkatJNI.CkCrypt2_WriteFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCrypt2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */