/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkStringBuilder
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkStringBuilder(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkStringBuilder var0) {
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
/*  29 */         chilkatJNI.delete_CkStringBuilder(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkStringBuilder()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkStringBuilder(), true);
/*     */   }
/*     */   
/*     */   public int get_IntValue() {
/*  42 */     return chilkatJNI.CkStringBuilder_get_IntValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IntValue(int var1) {
/*  46 */     chilkatJNI.CkStringBuilder_put_IntValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  50 */     return chilkatJNI.CkStringBuilder_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  54 */     chilkatJNI.CkStringBuilder_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Length() {
/*  58 */     return chilkatJNI.CkStringBuilder_get_Length(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Append(String var1) {
/*  62 */     return chilkatJNI.CkStringBuilder_Append(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendBd(CkBinData var1, String var2, int var3, int var4) {
/*  66 */     return chilkatJNI.CkStringBuilder_AppendBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean AppendEncoded(CkByteData var1, String var2) {
/*  70 */     return chilkatJNI.CkStringBuilder_AppendEncoded(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendInt(int var1) {
/*  74 */     return chilkatJNI.CkStringBuilder_AppendInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendInt64(long var1) {
/*  78 */     return chilkatJNI.CkStringBuilder_AppendInt64(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendLine(String var1, boolean var2) {
/*  82 */     return chilkatJNI.CkStringBuilder_AppendLine(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendSb(CkStringBuilder var1) {
/*  86 */     return chilkatJNI.CkStringBuilder_AppendSb(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void Clear() {
/*  90 */     chilkatJNI.CkStringBuilder_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Contains(String var1, boolean var2) {
/*  94 */     return chilkatJNI.CkStringBuilder_Contains(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ContainsWord(String var1, boolean var2) {
/*  98 */     return chilkatJNI.CkStringBuilder_ContainsWord(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ContentsEqual(String var1, boolean var2) {
/* 102 */     return chilkatJNI.CkStringBuilder_ContentsEqual(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ContentsEqualSb(CkStringBuilder var1, boolean var2) {
/* 106 */     return chilkatJNI.CkStringBuilder_ContentsEqualSb(this.swigCPtr, this, getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Decode(String var1, String var2) {
/* 110 */     return chilkatJNI.CkStringBuilder_Decode(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Encode(String var1, String var2) {
/* 114 */     return chilkatJNI.CkStringBuilder_Encode(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean EndsWith(String var1, boolean var2) {
/* 118 */     return chilkatJNI.CkStringBuilder_EndsWith(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean EntityDecode() {
/* 122 */     return chilkatJNI.CkStringBuilder_EntityDecode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetAfterBetween(String var1, String var2, String var3, CkString var4) {
/* 126 */     return chilkatJNI.CkStringBuilder_GetAfterBetween(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getAfterBetween(String var1, String var2, String var3) {
/* 130 */     return chilkatJNI.CkStringBuilder_getAfterBetween(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String afterBetween(String var1, String var2, String var3) {
/* 134 */     return chilkatJNI.CkStringBuilder_afterBetween(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetAsString(CkString var1) {
/* 138 */     return chilkatJNI.CkStringBuilder_GetAsString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getAsString() {
/* 142 */     return chilkatJNI.CkStringBuilder_getAsString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String asString() {
/* 146 */     return chilkatJNI.CkStringBuilder_asString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetBetween(String var1, String var2, CkString var3) {
/* 150 */     return chilkatJNI.CkStringBuilder_GetBetween(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getBetween(String var1, String var2) {
/* 154 */     return chilkatJNI.CkStringBuilder_getBetween(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String between(String var1, String var2) {
/* 158 */     return chilkatJNI.CkStringBuilder_between(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetDecoded(String var1, CkByteData var2) {
/* 162 */     return chilkatJNI.CkStringBuilder_GetDecoded(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetEncoded(String var1, String var2, CkString var3) {
/* 166 */     return chilkatJNI.CkStringBuilder_GetEncoded(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getEncoded(String var1, String var2) {
/* 170 */     return chilkatJNI.CkStringBuilder_getEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String encoded(String var1, String var2) {
/* 174 */     return chilkatJNI.CkStringBuilder_encoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetNth(int var1, String var2, boolean var3, boolean var4, CkString var5) {
/* 178 */     return chilkatJNI.CkStringBuilder_GetNth(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*     */   }
/*     */   
/*     */   public String getNth(int var1, String var2, boolean var3, boolean var4) {
/* 182 */     return chilkatJNI.CkStringBuilder_getNth(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public String nth(int var1, String var2, boolean var3, boolean var4) {
/* 186 */     return chilkatJNI.CkStringBuilder_nth(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean LastNLines(int var1, boolean var2, CkString var3) {
/* 190 */     return chilkatJNI.CkStringBuilder_LastNLines(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String lastNLines(int var1, boolean var2) {
/* 194 */     return chilkatJNI.CkStringBuilder_lastNLines(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1, String var2) {
/* 198 */     return chilkatJNI.CkStringBuilder_LoadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Prepend(String var1) {
/* 202 */     return chilkatJNI.CkStringBuilder_Prepend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean PunyDecode() {
/* 206 */     return chilkatJNI.CkStringBuilder_PunyDecode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean PunyEncode() {
/* 210 */     return chilkatJNI.CkStringBuilder_PunyEncode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int Replace(String var1, String var2) {
/* 214 */     return chilkatJNI.CkStringBuilder_Replace(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ReplaceAfterFinal(String var1, String var2) {
/* 218 */     return chilkatJNI.CkStringBuilder_ReplaceAfterFinal(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ReplaceAllBetween(String var1, String var2, String var3, boolean var4) {
/* 222 */     return chilkatJNI.CkStringBuilder_ReplaceAllBetween(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public int ReplaceBetween(String var1, String var2, String var3, String var4) {
/* 226 */     return chilkatJNI.CkStringBuilder_ReplaceBetween(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public int ReplaceI(String var1, int var2) {
/* 230 */     return chilkatJNI.CkStringBuilder_ReplaceI(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int ReplaceWord(String var1, String var2) {
/* 234 */     return chilkatJNI.CkStringBuilder_ReplaceWord(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SecureClear() {
/* 238 */     chilkatJNI.CkStringBuilder_SecureClear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetNth(int var1, String var2, String var3, boolean var4, boolean var5) {
/* 242 */     return chilkatJNI.CkStringBuilder_SetNth(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean SetString(String var1) {
/* 246 */     return chilkatJNI.CkStringBuilder_SetString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StartsWith(String var1, boolean var2) {
/* 250 */     return chilkatJNI.CkStringBuilder_StartsWith(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToCRLF() {
/* 254 */     return chilkatJNI.CkStringBuilder_ToCRLF(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToLF() {
/* 258 */     return chilkatJNI.CkStringBuilder_ToLF(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToLowercase() {
/* 262 */     return chilkatJNI.CkStringBuilder_ToLowercase(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ToUppercase() {
/* 266 */     return chilkatJNI.CkStringBuilder_ToUppercase(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Trim() {
/* 270 */     return chilkatJNI.CkStringBuilder_Trim(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean TrimInsideSpaces() {
/* 274 */     return chilkatJNI.CkStringBuilder_TrimInsideSpaces(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean WriteFile(String var1, String var2, boolean var3) {
/* 278 */     return chilkatJNI.CkStringBuilder_WriteFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean WriteFileIfModified(String var1, String var2, boolean var3) {
/* 282 */     return chilkatJNI.CkStringBuilder_WriteFileIfModified(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkStringBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */