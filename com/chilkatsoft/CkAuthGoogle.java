/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAuthGoogle
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAuthGoogle(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAuthGoogle var0) {
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
/*  29 */         chilkatJNI.delete_CkAuthGoogle(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAuthGoogle()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAuthGoogle(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAuthGoogle_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAuthGoogle_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAuthGoogle_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkAuthGoogle_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AccessToken(CkString var1) {
/*  58 */     chilkatJNI.CkAuthGoogle_get_AccessToken(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String accessToken() {
/*  62 */     return chilkatJNI.CkAuthGoogle_accessToken(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AccessToken(String var1) {
/*  66 */     chilkatJNI.CkAuthGoogle_put_AccessToken(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  70 */     chilkatJNI.CkAuthGoogle_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  74 */     return chilkatJNI.CkAuthGoogle_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  78 */     chilkatJNI.CkAuthGoogle_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EmailAddress(CkString var1) {
/*  82 */     chilkatJNI.CkAuthGoogle_get_EmailAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String emailAddress() {
/*  86 */     return chilkatJNI.CkAuthGoogle_emailAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmailAddress(String var1) {
/*  90 */     chilkatJNI.CkAuthGoogle_put_EmailAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ExpireNumSeconds() {
/*  94 */     return chilkatJNI.CkAuthGoogle_get_ExpireNumSeconds(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExpireNumSeconds(int var1) {
/*  98 */     chilkatJNI.CkAuthGoogle_put_ExpireNumSeconds(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_JsonKey(CkString var1) {
/* 102 */     chilkatJNI.CkAuthGoogle_get_JsonKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String jsonKey() {
/* 106 */     return chilkatJNI.CkAuthGoogle_jsonKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_JsonKey(String var1) {
/* 110 */     chilkatJNI.CkAuthGoogle_put_JsonKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 114 */     chilkatJNI.CkAuthGoogle_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 118 */     return chilkatJNI.CkAuthGoogle_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 122 */     chilkatJNI.CkAuthGoogle_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 126 */     return chilkatJNI.CkAuthGoogle_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 130 */     chilkatJNI.CkAuthGoogle_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 134 */     return chilkatJNI.CkAuthGoogle_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 138 */     return chilkatJNI.CkAuthGoogle_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 142 */     chilkatJNI.CkAuthGoogle_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumSecondsRemaining() {
/* 146 */     return chilkatJNI.CkAuthGoogle_get_NumSecondsRemaining(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Scope(CkString var1) {
/* 150 */     chilkatJNI.CkAuthGoogle_get_Scope(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String scope() {
/* 154 */     return chilkatJNI.CkAuthGoogle_scope(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Scope(String var1) {
/* 158 */     chilkatJNI.CkAuthGoogle_put_Scope(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SubEmailAddress(CkString var1) {
/* 162 */     chilkatJNI.CkAuthGoogle_get_SubEmailAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String subEmailAddress() {
/* 166 */     return chilkatJNI.CkAuthGoogle_subEmailAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SubEmailAddress(String var1) {
/* 170 */     chilkatJNI.CkAuthGoogle_put_SubEmailAddress(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Valid() {
/* 174 */     return chilkatJNI.CkAuthGoogle_get_Valid(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 178 */     return chilkatJNI.CkAuthGoogle_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 182 */     chilkatJNI.CkAuthGoogle_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 186 */     chilkatJNI.CkAuthGoogle_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 190 */     return chilkatJNI.CkAuthGoogle_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkPfx GetP12() {
/* 194 */     long var1 = chilkatJNI.CkAuthGoogle_GetP12(this.swigCPtr, this);
/* 195 */     return var1 == 0L ? null : new CkPfx(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ObtainAccessToken(CkSocket var1) {
/* 199 */     return chilkatJNI.CkAuthGoogle_ObtainAccessToken(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ObtainAccessTokenAsync(CkSocket var1) {
/* 203 */     long var2 = chilkatJNI.CkAuthGoogle_ObtainAccessTokenAsync(this.swigCPtr, this, CkSocket.getCPtr(var1), var1);
/* 204 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 208 */     return chilkatJNI.CkAuthGoogle_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetP12(CkPfx var1) {
/* 212 */     return chilkatJNI.CkAuthGoogle_SetP12(this.swigCPtr, this, CkPfx.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAuthGoogle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */