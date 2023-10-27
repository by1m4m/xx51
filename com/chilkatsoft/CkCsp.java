/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCsp
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCsp(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCsp var0) {
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
/*  29 */         chilkatJNI.delete_CkCsp(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCsp()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCsp(), true);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  42 */     return chilkatJNI.CkCsp_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  46 */     return chilkatJNI.CkCsp_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  50 */     return chilkatJNI.CkCsp_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_KeyContainerName(CkString var1) {
/*  54 */     chilkatJNI.CkCsp_get_KeyContainerName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String keyContainerName() {
/*  58 */     return chilkatJNI.CkCsp_keyContainerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_KeyContainerName(String var1) {
/*  62 */     chilkatJNI.CkCsp_put_KeyContainerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkCsp_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkCsp_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkCsp_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/*  78 */     return chilkatJNI.CkCsp_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/*  82 */     chilkatJNI.CkCsp_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Initialize() {
/*  86 */     return chilkatJNI.CkCsp_Initialize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_MachineKeyset() {
/*  90 */     return chilkatJNI.CkCsp_get_MachineKeyset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MachineKeyset(boolean var1) {
/*  94 */     chilkatJNI.CkCsp_put_MachineKeyset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String hashAlgorithm() {
/*  98 */     return chilkatJNI.CkCsp_hashAlgorithm(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String encryptAlgorithm() {
/* 102 */     return chilkatJNI.CkCsp_encryptAlgorithm(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String providerName() {
/* 106 */     return chilkatJNI.CkCsp_providerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String nthKeyContainerName(int var1) {
/* 110 */     return chilkatJNI.CkCsp_nthKeyContainerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String nthSignatureAlgorithm(int var1) {
/* 114 */     return chilkatJNI.CkCsp_nthSignatureAlgorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String nthKeyExchangeAlgorithm(int var1) {
/* 118 */     return chilkatJNI.CkCsp_nthKeyExchangeAlgorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String nthHashAlgorithmName(int var1) {
/* 122 */     return chilkatJNI.CkCsp_nthHashAlgorithmName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String nthEncryptionAlgorithm(int var1) {
/* 126 */     return chilkatJNI.CkCsp_nthEncryptionAlgorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetProviderMicrosoftBase() {
/* 130 */     return chilkatJNI.CkCsp_SetProviderMicrosoftBase(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetProviderMicrosoftStrong() {
/* 134 */     return chilkatJNI.CkCsp_SetProviderMicrosoftStrong(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetProviderMicrosoftEnhanced() {
/* 138 */     return chilkatJNI.CkCsp_SetProviderMicrosoftEnhanced(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetProviderMicrosoftRsaAes() {
/* 142 */     return chilkatJNI.CkCsp_SetProviderMicrosoftRsaAes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ProviderType() {
/* 146 */     return chilkatJNI.CkCsp_get_ProviderType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkStringArray GetKeyContainerNames() {
/* 150 */     long var1 = chilkatJNI.CkCsp_GetKeyContainerNames(this.swigCPtr, this);
/* 151 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*     */   }
/*     */   
/*     */   public int SetHashAlgorithm(String var1) {
/* 155 */     return chilkatJNI.CkCsp_SetHashAlgorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasHashAlgorithm(String var1, int var2) {
/* 159 */     return chilkatJNI.CkCsp_HasHashAlgorithm(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int SetEncryptAlgorithm(String var1) {
/* 163 */     return chilkatJNI.CkCsp_SetEncryptAlgorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasEncryptAlgorithm(String var1, int var2) {
/* 167 */     return chilkatJNI.CkCsp_HasEncryptAlgorithm(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int get_HashAlgorithmID() {
/* 171 */     return chilkatJNI.CkCsp_get_HashAlgorithmID(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_EncryptAlgorithmID() {
/* 175 */     return chilkatJNI.CkCsp_get_EncryptAlgorithmID(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_HashNumBits() {
/* 179 */     return chilkatJNI.CkCsp_get_HashNumBits(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_EncryptNumBits() {
/* 183 */     return chilkatJNI.CkCsp_get_EncryptNumBits(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HashAlgorithm(CkString var1) {
/* 187 */     chilkatJNI.CkCsp_get_HashAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_EncryptAlgorithm(CkString var1) {
/* 191 */     chilkatJNI.CkCsp_get_EncryptAlgorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean NthKeyContainerName(int var1, CkString var2) {
/* 195 */     return chilkatJNI.CkCsp_NthKeyContainerName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean NthSignatureAlgorithm(int var1, CkString var2) {
/* 199 */     return chilkatJNI.CkCsp_NthSignatureAlgorithm(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean NthKeyExchangeAlgorithm(int var1, CkString var2) {
/* 203 */     return chilkatJNI.CkCsp_NthKeyExchangeAlgorithm(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean NthHashAlgorithmName(int var1, CkString var2) {
/* 207 */     return chilkatJNI.CkCsp_NthHashAlgorithmName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean NthEncryptionAlgorithm(int var1, CkString var2) {
/* 211 */     return chilkatJNI.CkCsp_NthEncryptionAlgorithm(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public int get_NumKeyContainers() {
/* 215 */     return chilkatJNI.CkCsp_get_NumKeyContainers(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int NthSignatureNumBits(int var1) {
/* 219 */     return chilkatJNI.CkCsp_NthSignatureNumBits(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int NthKeyExchangeNumBits(int var1) {
/* 223 */     return chilkatJNI.CkCsp_NthKeyExchangeNumBits(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int NthHashNumBits(int var1) {
/* 227 */     return chilkatJNI.CkCsp_NthHashNumBits(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int NthEncryptionNumBits(int var1) {
/* 231 */     return chilkatJNI.CkCsp_NthEncryptionNumBits(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ProviderName(CkString var1) {
/* 235 */     chilkatJNI.CkCsp_get_ProviderName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_ProviderName(String var1) {
/* 239 */     chilkatJNI.CkCsp_put_ProviderName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumKeyExchangeAlgorithms() {
/* 243 */     return chilkatJNI.CkCsp_get_NumKeyExchangeAlgorithms(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumSignatureAlgorithms() {
/* 247 */     return chilkatJNI.CkCsp_get_NumSignatureAlgorithms(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumEncryptAlgorithms() {
/* 251 */     return chilkatJNI.CkCsp_get_NumEncryptAlgorithms(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumHashAlgorithms() {
/* 255 */     return chilkatJNI.CkCsp_get_NumHashAlgorithms(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 259 */     return chilkatJNI.CkCsp_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/* 263 */     chilkatJNI.CkCsp_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/* 267 */     chilkatJNI.CkCsp_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/* 271 */     chilkatJNI.CkCsp_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCsp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */