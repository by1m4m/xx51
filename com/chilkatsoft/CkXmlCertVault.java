/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkXmlCertVault
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkXmlCertVault(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkXmlCertVault var0) {
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
/*  29 */         chilkatJNI.delete_CkXmlCertVault(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkXmlCertVault()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkXmlCertVault(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkXmlCertVault_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkXmlCertVault_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkXmlCertVault_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkXmlCertVault_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkXmlCertVault_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkXmlCertVault_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkXmlCertVault_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkXmlCertVault_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkXmlCertVault_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkXmlCertVault_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkXmlCertVault_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkXmlCertVault_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkXmlCertVault_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkXmlCertVault_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_MasterPassword(CkString var1) {
/*  98 */     chilkatJNI.CkXmlCertVault_get_MasterPassword(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String masterPassword() {
/* 102 */     return chilkatJNI.CkXmlCertVault_masterPassword(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MasterPassword(String var1) {
/* 106 */     chilkatJNI.CkXmlCertVault_put_MasterPassword(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkXmlCertVault_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkXmlCertVault_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkXmlCertVault_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkXmlCertVault_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddCert(CkCert var1) {
/* 126 */     return chilkatJNI.CkXmlCertVault_AddCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddCertBinary(CkByteData var1) {
/* 130 */     return chilkatJNI.CkXmlCertVault_AddCertBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddCertChain(CkCertChain var1) {
/* 134 */     return chilkatJNI.CkXmlCertVault_AddCertChain(this.swigCPtr, this, CkCertChain.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddCertEncoded(String var1, String var2) {
/* 138 */     return chilkatJNI.CkXmlCertVault_AddCertEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddCertFile(String var1) {
/* 142 */     return chilkatJNI.CkXmlCertVault_AddCertFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddCertString(String var1) {
/* 146 */     return chilkatJNI.CkXmlCertVault_AddCertString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddPemFile(String var1, String var2) {
/* 150 */     return chilkatJNI.CkXmlCertVault_AddPemFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddPfx(CkPfx var1) {
/* 154 */     return chilkatJNI.CkXmlCertVault_AddPfx(this.swigCPtr, this, CkPfx.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddPfxBinary(CkByteData var1, String var2) {
/* 158 */     return chilkatJNI.CkXmlCertVault_AddPfxBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddPfxEncoded(String var1, String var2, String var3) {
/* 162 */     return chilkatJNI.CkXmlCertVault_AddPfxEncoded(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddPfxFile(String var1, String var2) {
/* 166 */     return chilkatJNI.CkXmlCertVault_AddPfxFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 170 */     return chilkatJNI.CkXmlCertVault_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 174 */     return chilkatJNI.CkXmlCertVault_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 178 */     return chilkatJNI.CkXmlCertVault_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 182 */     return chilkatJNI.CkXmlCertVault_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile(String var1) {
/* 186 */     return chilkatJNI.CkXmlCertVault_LoadXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 190 */     return chilkatJNI.CkXmlCertVault_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXml(String var1) {
/* 194 */     return chilkatJNI.CkXmlCertVault_SaveXml(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkXmlCertVault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */