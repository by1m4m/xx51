/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCertStore
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCertStore(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCertStore var0) {
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
/*  29 */         chilkatJNI.delete_CkCertStore(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCertStore()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCertStore(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCertStore_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCertStore_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCertStore_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AvoidWindowsPkAccess() {
/*  54 */     return chilkatJNI.CkCertStore_get_AvoidWindowsPkAccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AvoidWindowsPkAccess(boolean var1) {
/*  58 */     chilkatJNI.CkCertStore_put_AvoidWindowsPkAccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  62 */     chilkatJNI.CkCertStore_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  66 */     return chilkatJNI.CkCertStore_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  70 */     chilkatJNI.CkCertStore_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  74 */     chilkatJNI.CkCertStore_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  78 */     return chilkatJNI.CkCertStore_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  82 */     chilkatJNI.CkCertStore_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  86 */     return chilkatJNI.CkCertStore_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  90 */     chilkatJNI.CkCertStore_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  94 */     return chilkatJNI.CkCertStore_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  98 */     return chilkatJNI.CkCertStore_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 102 */     chilkatJNI.CkCertStore_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumCertificates() {
/* 106 */     return chilkatJNI.CkCertStore_get_NumCertificates(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumEmailCerts() {
/* 110 */     return chilkatJNI.CkCertStore_get_NumEmailCerts(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 114 */     return chilkatJNI.CkCertStore_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 118 */     chilkatJNI.CkCertStore_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 122 */     chilkatJNI.CkCertStore_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 126 */     return chilkatJNI.CkCertStore_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddCertificate(CkCert var1) {
/* 130 */     return chilkatJNI.CkCertStore_AddCertificate(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean CreateFileStore(String var1) {
/* 134 */     return chilkatJNI.CkCertStore_CreateFileStore(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CreateMemoryStore() {
/* 138 */     return chilkatJNI.CkCertStore_CreateMemoryStore(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CreateRegistryStore(String var1, String var2) {
/* 142 */     return chilkatJNI.CkCertStore_CreateRegistryStore(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkCert FindCertByRfc822Name(String var1) {
/* 146 */     long var2 = chilkatJNI.CkCertStore_FindCertByRfc822Name(this.swigCPtr, this, var1);
/* 147 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySerial(String var1) {
/* 151 */     long var2 = chilkatJNI.CkCertStore_FindCertBySerial(this.swigCPtr, this, var1);
/* 152 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySha1Thumbprint(String var1) {
/* 156 */     long var2 = chilkatJNI.CkCertStore_FindCertBySha1Thumbprint(this.swigCPtr, this, var1);
/* 157 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySubject(String var1) {
/* 161 */     long var2 = chilkatJNI.CkCertStore_FindCertBySubject(this.swigCPtr, this, var1);
/* 162 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySubjectCN(String var1) {
/* 166 */     long var2 = chilkatJNI.CkCertStore_FindCertBySubjectCN(this.swigCPtr, this, var1);
/* 167 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySubjectE(String var1) {
/* 171 */     long var2 = chilkatJNI.CkCertStore_FindCertBySubjectE(this.swigCPtr, this, var1);
/* 172 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertBySubjectO(String var1) {
/* 176 */     long var2 = chilkatJNI.CkCertStore_FindCertBySubjectO(this.swigCPtr, this, var1);
/* 177 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert FindCertForEmail(String var1) {
/* 181 */     long var2 = chilkatJNI.CkCertStore_FindCertForEmail(this.swigCPtr, this, var1);
/* 182 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert GetCertificate(int var1) {
/* 186 */     long var2 = chilkatJNI.CkCertStore_GetCertificate(this.swigCPtr, this, var1);
/* 187 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public CkCert GetEmailCert(int var1) {
/* 191 */     long var2 = chilkatJNI.CkCertStore_GetEmailCert(this.swigCPtr, this, var1);
/* 192 */     return var2 == 0L ? null : new CkCert(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadPemFile(String var1) {
/* 196 */     return chilkatJNI.CkCertStore_LoadPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPemStr(String var1) {
/* 200 */     return chilkatJNI.CkCertStore_LoadPemStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxData(CkByteData var1, String var2) {
/* 204 */     return chilkatJNI.CkCertStore_LoadPfxData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadPfxFile(String var1, String var2) {
/* 208 */     return chilkatJNI.CkCertStore_LoadPfxFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OpenCurrentUserStore(boolean var1) {
/* 212 */     return chilkatJNI.CkCertStore_OpenCurrentUserStore(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenFileStore(String var1, boolean var2) {
/* 216 */     return chilkatJNI.CkCertStore_OpenFileStore(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OpenLocalSystemStore(boolean var1) {
/* 220 */     return chilkatJNI.CkCertStore_OpenLocalSystemStore(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean OpenRegistryStore(String var1, String var2, boolean var3) {
/* 224 */     return chilkatJNI.CkCertStore_OpenRegistryStore(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean OpenWindowsStore(String var1, String var2, boolean var3) {
/* 228 */     return chilkatJNI.CkCertStore_OpenWindowsStore(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean RemoveCertificate(CkCert var1) {
/* 232 */     return chilkatJNI.CkCertStore_RemoveCertificate(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 236 */     return chilkatJNI.CkCertStore_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCertStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */