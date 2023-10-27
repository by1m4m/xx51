/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkOAuth1
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkOAuth1(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkOAuth1 var0) {
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
/*  29 */         chilkatJNI.delete_CkOAuth1(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkOAuth1()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkOAuth1(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkOAuth1_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkOAuth1_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkOAuth1_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_AuthorizationHeader(CkString var1) {
/*  54 */     chilkatJNI.CkOAuth1_get_AuthorizationHeader(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String authorizationHeader() {
/*  58 */     return chilkatJNI.CkOAuth1_authorizationHeader(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_BaseString(CkString var1) {
/*  62 */     chilkatJNI.CkOAuth1_get_BaseString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String baseString() {
/*  66 */     return chilkatJNI.CkOAuth1_baseString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ConsumerKey(CkString var1) {
/*  70 */     chilkatJNI.CkOAuth1_get_ConsumerKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String consumerKey() {
/*  74 */     return chilkatJNI.CkOAuth1_consumerKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConsumerKey(String var1) {
/*  78 */     chilkatJNI.CkOAuth1_put_ConsumerKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ConsumerSecret(CkString var1) {
/*  82 */     chilkatJNI.CkOAuth1_get_ConsumerSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String consumerSecret() {
/*  86 */     return chilkatJNI.CkOAuth1_consumerSecret(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ConsumerSecret(String var1) {
/*  90 */     chilkatJNI.CkOAuth1_put_ConsumerSecret(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  94 */     chilkatJNI.CkOAuth1_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  98 */     return chilkatJNI.CkOAuth1_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/* 102 */     chilkatJNI.CkOAuth1_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EncodedSignature(CkString var1) {
/* 106 */     chilkatJNI.CkOAuth1_get_EncodedSignature(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encodedSignature() {
/* 110 */     return chilkatJNI.CkOAuth1_encodedSignature(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_GeneratedUrl(CkString var1) {
/* 114 */     chilkatJNI.CkOAuth1_get_GeneratedUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String generatedUrl() {
/* 118 */     return chilkatJNI.CkOAuth1_generatedUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HmacKey(CkString var1) {
/* 122 */     chilkatJNI.CkOAuth1_get_HmacKey(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hmacKey() {
/* 126 */     return chilkatJNI.CkOAuth1_hmacKey(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 130 */     chilkatJNI.CkOAuth1_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 134 */     return chilkatJNI.CkOAuth1_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 138 */     chilkatJNI.CkOAuth1_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 142 */     return chilkatJNI.CkOAuth1_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 146 */     chilkatJNI.CkOAuth1_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 150 */     return chilkatJNI.CkOAuth1_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 154 */     return chilkatJNI.CkOAuth1_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 158 */     chilkatJNI.CkOAuth1_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Nonce(CkString var1) {
/* 162 */     chilkatJNI.CkOAuth1_get_Nonce(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String nonce() {
/* 166 */     return chilkatJNI.CkOAuth1_nonce(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Nonce(String var1) {
/* 170 */     chilkatJNI.CkOAuth1_put_Nonce(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_OauthMethod(CkString var1) {
/* 174 */     chilkatJNI.CkOAuth1_get_OauthMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oauthMethod() {
/* 178 */     return chilkatJNI.CkOAuth1_oauthMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OauthMethod(String var1) {
/* 182 */     chilkatJNI.CkOAuth1_put_OauthMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_OauthUrl(CkString var1) {
/* 186 */     chilkatJNI.CkOAuth1_get_OauthUrl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oauthUrl() {
/* 190 */     return chilkatJNI.CkOAuth1_oauthUrl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OauthUrl(String var1) {
/* 194 */     chilkatJNI.CkOAuth1_put_OauthUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_OauthVersion(CkString var1) {
/* 198 */     chilkatJNI.CkOAuth1_get_OauthVersion(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String oauthVersion() {
/* 202 */     return chilkatJNI.CkOAuth1_oauthVersion(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_OauthVersion(String var1) {
/* 206 */     chilkatJNI.CkOAuth1_put_OauthVersion(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_QueryString(CkString var1) {
/* 210 */     chilkatJNI.CkOAuth1_get_QueryString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String queryString() {
/* 214 */     return chilkatJNI.CkOAuth1_queryString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Realm(CkString var1) {
/* 218 */     chilkatJNI.CkOAuth1_get_Realm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String realm() {
/* 222 */     return chilkatJNI.CkOAuth1_realm(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Realm(String var1) {
/* 226 */     chilkatJNI.CkOAuth1_put_Realm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Signature(CkString var1) {
/* 230 */     chilkatJNI.CkOAuth1_get_Signature(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signature() {
/* 234 */     return chilkatJNI.CkOAuth1_signature(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SignatureMethod(CkString var1) {
/* 238 */     chilkatJNI.CkOAuth1_get_SignatureMethod(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String signatureMethod() {
/* 242 */     return chilkatJNI.CkOAuth1_signatureMethod(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SignatureMethod(String var1) {
/* 246 */     chilkatJNI.CkOAuth1_put_SignatureMethod(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Timestamp(CkString var1) {
/* 250 */     chilkatJNI.CkOAuth1_get_Timestamp(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String timestamp() {
/* 254 */     return chilkatJNI.CkOAuth1_timestamp(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Timestamp(String var1) {
/* 258 */     chilkatJNI.CkOAuth1_put_Timestamp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Token(CkString var1) {
/* 262 */     chilkatJNI.CkOAuth1_get_Token(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String token() {
/* 266 */     return chilkatJNI.CkOAuth1_token(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Token(String var1) {
/* 270 */     chilkatJNI.CkOAuth1_put_Token(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_TokenSecret(CkString var1) {
/* 274 */     chilkatJNI.CkOAuth1_get_TokenSecret(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tokenSecret() {
/* 278 */     return chilkatJNI.CkOAuth1_tokenSecret(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_TokenSecret(String var1) {
/* 282 */     chilkatJNI.CkOAuth1_put_TokenSecret(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 286 */     return chilkatJNI.CkOAuth1_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 290 */     chilkatJNI.CkOAuth1_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 294 */     chilkatJNI.CkOAuth1_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 298 */     return chilkatJNI.CkOAuth1_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddParam(String var1, String var2) {
/* 302 */     return chilkatJNI.CkOAuth1_AddParam(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Generate() {
/* 306 */     return chilkatJNI.CkOAuth1_Generate(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenNonce(int var1) {
/* 310 */     return chilkatJNI.CkOAuth1_GenNonce(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenTimestamp() {
/* 314 */     return chilkatJNI.CkOAuth1_GenTimestamp(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RemoveParam(String var1) {
/* 318 */     return chilkatJNI.CkOAuth1_RemoveParam(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 322 */     return chilkatJNI.CkOAuth1_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetRsaKey(CkPrivateKey var1) {
/* 326 */     return chilkatJNI.CkOAuth1_SetRsaKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkOAuth1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */