/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkPfx
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkPfx(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkPfx var0) {
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
/*  29 */         chilkatJNI.delete_CkPfx(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkPfx()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkPfx(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkPfx_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkPfx_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkPfx_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkPfx_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkPfx_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkPfx_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkPfx_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkPfx_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkPfx_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkPfx_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkPfx_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkPfx_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkPfx_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkPfx_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCerts() {
/*  98 */     return chilkatJNI.CkPfx_get_NumCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumPrivateKeys() {
/* 102 */     return chilkatJNI.CkPfx_get_NumPrivateKeys(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 106 */     return chilkatJNI.CkPfx_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 110 */     chilkatJNI.CkPfx_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 114 */     chilkatJNI.CkPfx_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 118 */     return chilkatJNI.CkPfx_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddCert(CkCert var1, boolean var2) {
/* 122 */     return chilkatJNI.CkPfx_AddCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddPrivateKey(CkPrivateKey var1, CkCertChain var2) {
/* 126 */     return chilkatJNI.CkPfx_AddPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkCertChain.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkCert GetCert(int var1) {
/* 130 */     long var2 = chilkatJNI.CkPfx_GetCert(this.swigCPtr, this, var1);
/* 131 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GetPrivateKey(int var1) {
/* 135 */     long var2 = chilkatJNI.CkPfx_GetPrivateKey(this.swigCPtr, this, var1);
/* 136 */     return var2 == 0L ? null : new CkPrivateKey(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadPem(String var1, String var2) {
/* 140 */     return chilkatJNI.CkPfx_LoadPem(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxBytes(CkByteData var1, String var2) {
/* 144 */     return chilkatJNI.CkPfx_LoadPfxBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxEncoded(String var1, String var2, String var3) {
/* 148 */     return chilkatJNI.CkPfx_LoadPfxEncoded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxFile(String var1, String var2) {
/* 152 */     return chilkatJNI.CkPfx_LoadPfxFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 156 */     return chilkatJNI.CkPfx_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToBinary(String var1, CkByteData var2) {
/* 160 */     return chilkatJNI.CkPfx_ToBinary(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ToEncodedString(String var1, String var2, CkString var3) {
/* 164 */     return chilkatJNI.CkPfx_ToEncodedString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String toEncodedString(String var1, String var2) {
/* 168 */     return chilkatJNI.CkPfx_toEncodedString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToFile(String var1, String var2) {
/* 172 */     return chilkatJNI.CkPfx_ToFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkJavaKeyStore ToJavaKeyStore(String var1, String var2) {
/* 176 */     long var3 = chilkatJNI.CkPfx_ToJavaKeyStore(this.swigCPtr, this, var1, var2);
/* 177 */     return var3 == 0L ? null : new CkJavaKeyStore(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ToPem(CkString var1) {
/* 181 */     return chilkatJNI.CkPfx_ToPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toPem() {
/* 185 */     return chilkatJNI.CkPfx_toPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToPemEx(boolean var1, boolean var2, boolean var3, boolean var4, String var5, String var6, CkString var7) {
/* 189 */     return chilkatJNI.CkPfx_ToPemEx(this.swigCPtr, this, var1, var2, var3, var4, var5, var6, CkString.getCPtr(var7), var7);
/*     */   }
/*     */   
/*     */   public String toPemEx(boolean var1, boolean var2, boolean var3, boolean var4, String var5, String var6) {
/* 193 */     return chilkatJNI.CkPfx_toPemEx(this.swigCPtr, this, var1, var2, var3, var4, var5, var6);
/*     */   }
/*     */   
/*     */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 197 */     return chilkatJNI.CkPfx_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkPfx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */