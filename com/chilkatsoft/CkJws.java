/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJws
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJws(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJws var0) {
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
/*  29 */         chilkatJNI.delete_CkJws(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJws()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJws(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJws_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJws_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJws_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkJws_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkJws_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkJws_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkJws_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkJws_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkJws_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkJws_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkJws_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkJws_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkJws_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkJws_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumSignatures() {
/*  98 */     return chilkatJNI.CkJws_get_NumSignatures(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PreferCompact() {
/* 102 */     return chilkatJNI.CkJws_get_PreferCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferCompact(boolean var1) {
/* 106 */     chilkatJNI.CkJws_put_PreferCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PreferFlattened() {
/* 110 */     return chilkatJNI.CkJws_get_PreferFlattened(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PreferFlattened(boolean var1) {
/* 114 */     chilkatJNI.CkJws_put_PreferFlattened(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkJws_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkJws_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkJws_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkJws_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CreateJws(CkString var1) {
/* 134 */     return chilkatJNI.CkJws_CreateJws(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String createJws() {
/* 138 */     return chilkatJNI.CkJws_createJws(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CreateJwsSb(CkStringBuilder var1) {
/* 142 */     return chilkatJNI.CkJws_CreateJwsSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetPayload(String var1, CkString var2) {
/* 146 */     return chilkatJNI.CkJws_GetPayload(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getPayload(String var1) {
/* 150 */     return chilkatJNI.CkJws_getPayload(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String payload(String var1) {
/* 154 */     return chilkatJNI.CkJws_payload(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPayloadBd(CkBinData var1) {
/* 158 */     return chilkatJNI.CkJws_GetPayloadBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetPayloadSb(String var1, CkStringBuilder var2) {
/* 162 */     return chilkatJNI.CkJws_GetPayloadSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkJsonObject GetProtectedHeader(int var1) {
/* 166 */     long var2 = chilkatJNI.CkJws_GetProtectedHeader(this.swigCPtr, this, var1);
/* 167 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public CkJsonObject GetUnprotectedHeader(int var1) {
/* 171 */     long var2 = chilkatJNI.CkJws_GetUnprotectedHeader(this.swigCPtr, this, var1);
/* 172 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadJws(String var1) {
/* 176 */     return chilkatJNI.CkJws_LoadJws(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadJwsSb(CkStringBuilder var1) {
/* 180 */     return chilkatJNI.CkJws_LoadJwsSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 184 */     return chilkatJNI.CkJws_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetMacKey(int var1, String var2, String var3) {
/* 188 */     return chilkatJNI.CkJws_SetMacKey(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetMacKeyBd(int var1, CkBinData var2) {
/* 192 */     return chilkatJNI.CkJws_SetMacKeyBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetPayload(String var1, String var2, boolean var3) {
/* 196 */     return chilkatJNI.CkJws_SetPayload(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetPayloadBd(CkBinData var1) {
/* 200 */     return chilkatJNI.CkJws_SetPayloadBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetPayloadSb(CkStringBuilder var1, String var2, boolean var3) {
/* 204 */     return chilkatJNI.CkJws_SetPayloadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetPrivateKey(int var1, CkPrivateKey var2) {
/* 208 */     return chilkatJNI.CkJws_SetPrivateKey(this.swigCPtr, this, var1, CkPrivateKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetProtectedHeader(int var1, CkJsonObject var2) {
/* 212 */     return chilkatJNI.CkJws_SetProtectedHeader(this.swigCPtr, this, var1, CkJsonObject.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetPublicKey(int var1, CkPublicKey var2) {
/* 216 */     return chilkatJNI.CkJws_SetPublicKey(this.swigCPtr, this, var1, CkPublicKey.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean SetUnprotectedHeader(int var1, CkJsonObject var2) {
/* 220 */     return chilkatJNI.CkJws_SetUnprotectedHeader(this.swigCPtr, this, var1, CkJsonObject.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public int Validate(int var1) {
/* 224 */     return chilkatJNI.CkJws_Validate(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJws.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */