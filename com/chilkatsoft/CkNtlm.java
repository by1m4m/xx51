/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkNtlm
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkNtlm(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkNtlm var0) {
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
/*  29 */         chilkatJNI.delete_CkNtlm(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkNtlm()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkNtlm(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkNtlm_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkNtlm_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkNtlm_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_ClientChallenge(CkString var1) {
/*  54 */     chilkatJNI.CkNtlm_get_ClientChallenge(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String clientChallenge() {
/*  58 */     return chilkatJNI.CkNtlm_clientChallenge(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ClientChallenge(String var1) {
/*  62 */     chilkatJNI.CkNtlm_put_ClientChallenge(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkNtlm_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkNtlm_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkNtlm_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DnsComputerName(CkString var1) {
/*  78 */     chilkatJNI.CkNtlm_get_DnsComputerName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dnsComputerName() {
/*  82 */     return chilkatJNI.CkNtlm_dnsComputerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DnsComputerName(String var1) {
/*  86 */     chilkatJNI.CkNtlm_put_DnsComputerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DnsDomainName(CkString var1) {
/*  90 */     chilkatJNI.CkNtlm_get_DnsDomainName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String dnsDomainName() {
/*  94 */     return chilkatJNI.CkNtlm_dnsDomainName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DnsDomainName(String var1) {
/*  98 */     chilkatJNI.CkNtlm_put_DnsDomainName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Domain(CkString var1) {
/* 102 */     chilkatJNI.CkNtlm_get_Domain(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String domain() {
/* 106 */     return chilkatJNI.CkNtlm_domain(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Domain(String var1) {
/* 110 */     chilkatJNI.CkNtlm_put_Domain(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EncodingMode(CkString var1) {
/* 114 */     chilkatJNI.CkNtlm_get_EncodingMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encodingMode() {
/* 118 */     return chilkatJNI.CkNtlm_encodingMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EncodingMode(String var1) {
/* 122 */     chilkatJNI.CkNtlm_put_EncodingMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Flags(CkString var1) {
/* 126 */     chilkatJNI.CkNtlm_get_Flags(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String flags() {
/* 130 */     return chilkatJNI.CkNtlm_flags(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Flags(String var1) {
/* 134 */     chilkatJNI.CkNtlm_put_Flags(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 138 */     chilkatJNI.CkNtlm_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 142 */     return chilkatJNI.CkNtlm_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 146 */     chilkatJNI.CkNtlm_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 150 */     return chilkatJNI.CkNtlm_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 154 */     chilkatJNI.CkNtlm_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 158 */     return chilkatJNI.CkNtlm_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 162 */     return chilkatJNI.CkNtlm_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 166 */     chilkatJNI.CkNtlm_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_NetBiosComputerName(CkString var1) {
/* 170 */     chilkatJNI.CkNtlm_get_NetBiosComputerName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String netBiosComputerName() {
/* 174 */     return chilkatJNI.CkNtlm_netBiosComputerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NetBiosComputerName(String var1) {
/* 178 */     chilkatJNI.CkNtlm_put_NetBiosComputerName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_NetBiosDomainName(CkString var1) {
/* 182 */     chilkatJNI.CkNtlm_get_NetBiosDomainName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String netBiosDomainName() {
/* 186 */     return chilkatJNI.CkNtlm_netBiosDomainName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NetBiosDomainName(String var1) {
/* 190 */     chilkatJNI.CkNtlm_put_NetBiosDomainName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NtlmVersion() {
/* 194 */     return chilkatJNI.CkNtlm_get_NtlmVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_NtlmVersion(int var1) {
/* 198 */     chilkatJNI.CkNtlm_put_NtlmVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_OemCodePage() {
/* 202 */     return chilkatJNI.CkNtlm_get_OemCodePage(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OemCodePage(int var1) {
/* 206 */     chilkatJNI.CkNtlm_put_OemCodePage(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Password(CkString var1) {
/* 210 */     chilkatJNI.CkNtlm_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String password() {
/* 214 */     return chilkatJNI.CkNtlm_password(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Password(String var1) {
/* 218 */     chilkatJNI.CkNtlm_put_Password(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ServerChallenge(CkString var1) {
/* 222 */     chilkatJNI.CkNtlm_get_ServerChallenge(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serverChallenge() {
/* 226 */     return chilkatJNI.CkNtlm_serverChallenge(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ServerChallenge(String var1) {
/* 230 */     chilkatJNI.CkNtlm_put_ServerChallenge(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TargetName(CkString var1) {
/* 234 */     chilkatJNI.CkNtlm_get_TargetName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String targetName() {
/* 238 */     return chilkatJNI.CkNtlm_targetName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TargetName(String var1) {
/* 242 */     chilkatJNI.CkNtlm_put_TargetName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_UserName(CkString var1) {
/* 246 */     chilkatJNI.CkNtlm_get_UserName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String userName() {
/* 250 */     return chilkatJNI.CkNtlm_userName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_UserName(String var1) {
/* 254 */     chilkatJNI.CkNtlm_put_UserName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 258 */     return chilkatJNI.CkNtlm_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 262 */     chilkatJNI.CkNtlm_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 266 */     chilkatJNI.CkNtlm_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 270 */     return chilkatJNI.CkNtlm_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Workstation(CkString var1) {
/* 274 */     chilkatJNI.CkNtlm_get_Workstation(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String workstation() {
/* 278 */     return chilkatJNI.CkNtlm_workstation(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Workstation(String var1) {
/* 282 */     chilkatJNI.CkNtlm_put_Workstation(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean CompareType3(String var1, String var2) {
/* 286 */     return chilkatJNI.CkNtlm_CompareType3(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GenType1(CkString var1) {
/* 290 */     return chilkatJNI.CkNtlm_GenType1(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String genType1() {
/* 294 */     return chilkatJNI.CkNtlm_genType1(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenType2(String var1, CkString var2) {
/* 298 */     return chilkatJNI.CkNtlm_GenType2(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String genType2(String var1) {
/* 302 */     return chilkatJNI.CkNtlm_genType2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenType3(String var1, CkString var2) {
/* 306 */     return chilkatJNI.CkNtlm_GenType3(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String genType3(String var1) {
/* 310 */     return chilkatJNI.CkNtlm_genType3(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadType3(String var1) {
/* 314 */     return chilkatJNI.CkNtlm_LoadType3(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ParseType1(String var1, CkString var2) {
/* 318 */     return chilkatJNI.CkNtlm_ParseType1(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String parseType1(String var1) {
/* 322 */     return chilkatJNI.CkNtlm_parseType1(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ParseType2(String var1, CkString var2) {
/* 326 */     return chilkatJNI.CkNtlm_ParseType2(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String parseType2(String var1) {
/* 330 */     return chilkatJNI.CkNtlm_parseType2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ParseType3(String var1, CkString var2) {
/* 334 */     return chilkatJNI.CkNtlm_ParseType3(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String parseType3(String var1) {
/* 338 */     return chilkatJNI.CkNtlm_parseType3(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 342 */     return chilkatJNI.CkNtlm_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetFlag(String var1, boolean var2) {
/* 346 */     return chilkatJNI.CkNtlm_SetFlag(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 350 */     return chilkatJNI.CkNtlm_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkNtlm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */