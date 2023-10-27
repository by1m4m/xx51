/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkDsa
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkDsa(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkDsa var0) {
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
/*  29 */         chilkatJNI.delete_CkDsa(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkDsa()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkDsa(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkDsa_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkDsa_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkDsa_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkDsa_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkDsa_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkDsa_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_GroupSize() {
/*  66 */     return chilkatJNI.CkDsa_get_GroupSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_GroupSize(int var1) {
/*  70 */     chilkatJNI.CkDsa_put_GroupSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Hash(CkByteData var1) {
/*  74 */     chilkatJNI.CkDsa_get_Hash(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_Hash(CkByteData var1) {
/*  78 */     chilkatJNI.CkDsa_put_Hash(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_HexG(CkString var1) {
/*  82 */     chilkatJNI.CkDsa_get_HexG(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hexG() {
/*  86 */     return chilkatJNI.CkDsa_hexG(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HexP(CkString var1) {
/*  90 */     chilkatJNI.CkDsa_get_HexP(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hexP() {
/*  94 */     return chilkatJNI.CkDsa_hexP(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HexQ(CkString var1) {
/*  98 */     chilkatJNI.CkDsa_get_HexQ(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hexQ() {
/* 102 */     return chilkatJNI.CkDsa_hexQ(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HexX(CkString var1) {
/* 106 */     chilkatJNI.CkDsa_get_HexX(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hexX() {
/* 110 */     return chilkatJNI.CkDsa_hexX(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HexY(CkString var1) {
/* 114 */     chilkatJNI.CkDsa_get_HexY(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hexY() {
/* 118 */     return chilkatJNI.CkDsa_hexY(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 122 */     chilkatJNI.CkDsa_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 126 */     return chilkatJNI.CkDsa_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 130 */     chilkatJNI.CkDsa_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 134 */     return chilkatJNI.CkDsa_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 138 */     chilkatJNI.CkDsa_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 142 */     return chilkatJNI.CkDsa_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 146 */     return chilkatJNI.CkDsa_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 150 */     chilkatJNI.CkDsa_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Signature(CkByteData var1) {
/* 154 */     chilkatJNI.CkDsa_get_Signature(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_Signature(CkByteData var1) {
/* 158 */     chilkatJNI.CkDsa_put_Signature(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 162 */     return chilkatJNI.CkDsa_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 166 */     chilkatJNI.CkDsa_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 170 */     chilkatJNI.CkDsa_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 174 */     return chilkatJNI.CkDsa_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean FromDer(CkByteData var1) {
/* 178 */     return chilkatJNI.CkDsa_FromDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean FromDerFile(String var1) {
/* 182 */     return chilkatJNI.CkDsa_FromDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromEncryptedPem(String var1, String var2) {
/* 186 */     return chilkatJNI.CkDsa_FromEncryptedPem(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FromPem(String var1) {
/* 190 */     return chilkatJNI.CkDsa_FromPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromPublicDer(CkByteData var1) {
/* 194 */     return chilkatJNI.CkDsa_FromPublicDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean FromPublicDerFile(String var1) {
/* 198 */     return chilkatJNI.CkDsa_FromPublicDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromPublicPem(String var1) {
/* 202 */     return chilkatJNI.CkDsa_FromPublicPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FromXml(String var1) {
/* 206 */     return chilkatJNI.CkDsa_FromXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenKey(int var1) {
/* 210 */     return chilkatJNI.CkDsa_GenKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenKeyFromParamsDer(CkByteData var1) {
/* 214 */     return chilkatJNI.CkDsa_GenKeyFromParamsDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GenKeyFromParamsDerFile(String var1) {
/* 218 */     return chilkatJNI.CkDsa_GenKeyFromParamsDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenKeyFromParamsPem(String var1) {
/* 222 */     return chilkatJNI.CkDsa_GenKeyFromParamsPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GenKeyFromParamsPemFile(String var1) {
/* 226 */     return chilkatJNI.CkDsa_GenKeyFromParamsPemFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedHash(String var1, CkString var2) {
/* 230 */     return chilkatJNI.CkDsa_GetEncodedHash(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEncodedHash(String var1) {
/* 234 */     return chilkatJNI.CkDsa_getEncodedHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String encodedHash(String var1) {
/* 238 */     return chilkatJNI.CkDsa_encodedHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedSignature(String var1, CkString var2) {
/* 242 */     return chilkatJNI.CkDsa_GetEncodedSignature(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEncodedSignature(String var1) {
/* 246 */     return chilkatJNI.CkDsa_getEncodedSignature(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String encodedSignature(String var1) {
/* 250 */     return chilkatJNI.CkDsa_encodedSignature(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadText(String var1, CkString var2) {
/* 254 */     return chilkatJNI.CkDsa_LoadText(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String loadText(String var1) {
/* 258 */     return chilkatJNI.CkDsa_loadText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 262 */     return chilkatJNI.CkDsa_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveText(String var1, String var2) {
/* 266 */     return chilkatJNI.CkDsa_SaveText(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetEncodedHash(String var1, String var2) {
/* 270 */     return chilkatJNI.CkDsa_SetEncodedHash(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetEncodedSignature(String var1, String var2) {
/* 274 */     return chilkatJNI.CkDsa_SetEncodedSignature(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetEncodedSignatureRS(String var1, String var2, String var3) {
/* 278 */     return chilkatJNI.CkDsa_SetEncodedSignatureRS(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetKeyExplicit(int var1, String var2, String var3, String var4, String var5) {
/* 282 */     return chilkatJNI.CkDsa_SetKeyExplicit(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean SetPubKeyExplicit(int var1, String var2, String var3, String var4, String var5) {
/* 286 */     return chilkatJNI.CkDsa_SetPubKeyExplicit(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean SignHash() {
/* 290 */     return chilkatJNI.CkDsa_SignHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToDer(CkByteData var1) {
/* 294 */     return chilkatJNI.CkDsa_ToDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ToDerFile(String var1) {
/* 298 */     return chilkatJNI.CkDsa_ToDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToEncryptedPem(String var1, CkString var2) {
/* 302 */     return chilkatJNI.CkDsa_ToEncryptedPem(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toEncryptedPem(String var1) {
/* 306 */     return chilkatJNI.CkDsa_toEncryptedPem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToPem(CkString var1) {
/* 310 */     return chilkatJNI.CkDsa_ToPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toPem() {
/* 314 */     return chilkatJNI.CkDsa_toPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToPublicDer(CkByteData var1) {
/* 318 */     return chilkatJNI.CkDsa_ToPublicDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ToPublicDerFile(String var1) {
/* 322 */     return chilkatJNI.CkDsa_ToPublicDerFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToPublicPem(CkString var1) {
/* 326 */     return chilkatJNI.CkDsa_ToPublicPem(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toPublicPem() {
/* 330 */     return chilkatJNI.CkDsa_toPublicPem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToXml(boolean var1, CkString var2) {
/* 334 */     return chilkatJNI.CkDsa_ToXml(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toXml(boolean var1) {
/* 338 */     return chilkatJNI.CkDsa_toXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 342 */     return chilkatJNI.CkDsa_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Verify() {
/* 346 */     return chilkatJNI.CkDsa_Verify(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean VerifyKey() {
/* 350 */     return chilkatJNI.CkDsa_VerifyKey(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkDsa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */