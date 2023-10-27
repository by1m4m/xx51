/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCsr
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCsr(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCsr var0) {
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
/*  29 */         chilkatJNI.delete_CkCsr(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCsr()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCsr(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCsr_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCsr_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCsr_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_CommonName(CkString var1) {
/*  54 */     chilkatJNI.CkCsr_get_CommonName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String commonName() {
/*  58 */     return chilkatJNI.CkCsr_commonName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CommonName(String var1) {
/*  62 */     chilkatJNI.CkCsr_put_CommonName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Company(CkString var1) {
/*  66 */     chilkatJNI.CkCsr_get_Company(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String company() {
/*  70 */     return chilkatJNI.CkCsr_company(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Company(String var1) {
/*  74 */     chilkatJNI.CkCsr_put_Company(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CompanyDivision(CkString var1) {
/*  78 */     chilkatJNI.CkCsr_get_CompanyDivision(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String companyDivision() {
/*  82 */     return chilkatJNI.CkCsr_companyDivision(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CompanyDivision(String var1) {
/*  86 */     chilkatJNI.CkCsr_put_CompanyDivision(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Country(CkString var1) {
/*  90 */     chilkatJNI.CkCsr_get_Country(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String country() {
/*  94 */     return chilkatJNI.CkCsr_country(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Country(String var1) {
/*  98 */     chilkatJNI.CkCsr_put_Country(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/* 102 */     chilkatJNI.CkCsr_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/* 106 */     return chilkatJNI.CkCsr_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 110 */     chilkatJNI.CkCsr_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EmailAddress(CkString var1) {
/* 114 */     chilkatJNI.CkCsr_get_EmailAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String emailAddress() {
/* 118 */     return chilkatJNI.CkCsr_emailAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmailAddress(String var1) {
/* 122 */     chilkatJNI.CkCsr_put_EmailAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 126 */     chilkatJNI.CkCsr_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 130 */     return chilkatJNI.CkCsr_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 134 */     chilkatJNI.CkCsr_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 138 */     return chilkatJNI.CkCsr_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 142 */     chilkatJNI.CkCsr_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 146 */     return chilkatJNI.CkCsr_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 150 */     return chilkatJNI.CkCsr_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 154 */     chilkatJNI.CkCsr_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Locality(CkString var1) {
/* 158 */     chilkatJNI.CkCsr_get_Locality(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String locality() {
/* 162 */     return chilkatJNI.CkCsr_locality(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Locality(String var1) {
/* 166 */     chilkatJNI.CkCsr_put_Locality(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_State(CkString var1) {
/* 170 */     chilkatJNI.CkCsr_get_State(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String state() {
/* 174 */     return chilkatJNI.CkCsr_state(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_State(String var1) {
/* 178 */     chilkatJNI.CkCsr_put_State(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 182 */     return chilkatJNI.CkCsr_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 186 */     chilkatJNI.CkCsr_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 190 */     chilkatJNI.CkCsr_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 194 */     return chilkatJNI.CkCsr_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenCsrBd(CkPrivateKey var1, CkBinData var2) {
/* 198 */     return chilkatJNI.CkCsr_GenCsrBd(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GenCsrPem(CkPrivateKey var1, CkString var2) {
/* 202 */     return chilkatJNI.CkCsr_GenCsrPem(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String genCsrPem(CkPrivateKey var1) {
/* 206 */     return chilkatJNI.CkCsr_genCsrPem(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetSubjectField(String var1, CkString var2) {
/* 210 */     return chilkatJNI.CkCsr_GetSubjectField(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getSubjectField(String var1) {
/* 214 */     return chilkatJNI.CkCsr_getSubjectField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String subjectField(String var1) {
/* 218 */     return chilkatJNI.CkCsr_subjectField(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadCsrPem(String var1) {
/* 222 */     return chilkatJNI.CkCsr_LoadCsrPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 226 */     return chilkatJNI.CkCsr_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetSubjectField(String var1, String var2, String var3) {
/* 230 */     return chilkatJNI.CkCsr_SetSubjectField(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCsr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */