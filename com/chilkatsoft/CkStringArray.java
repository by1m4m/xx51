/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkStringArray
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkStringArray(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkStringArray var0) {
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
/*  29 */         chilkatJNI.delete_CkStringArray(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkStringArray()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkStringArray(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkStringArray_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkStringArray_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkStringArray_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int get_Count() {
/*  54 */     return chilkatJNI.CkStringArray_get_Count(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Crlf() {
/*  58 */     return chilkatJNI.CkStringArray_get_Crlf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Crlf(boolean var1) {
/*  62 */     chilkatJNI.CkStringArray_put_Crlf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  66 */     chilkatJNI.CkStringArray_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  70 */     return chilkatJNI.CkStringArray_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  74 */     chilkatJNI.CkStringArray_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  78 */     chilkatJNI.CkStringArray_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  82 */     return chilkatJNI.CkStringArray_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  86 */     chilkatJNI.CkStringArray_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  90 */     return chilkatJNI.CkStringArray_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  94 */     chilkatJNI.CkStringArray_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  98 */     return chilkatJNI.CkStringArray_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 102 */     return chilkatJNI.CkStringArray_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 106 */     chilkatJNI.CkStringArray_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Length() {
/* 110 */     return chilkatJNI.CkStringArray_get_Length(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Trim() {
/* 114 */     return chilkatJNI.CkStringArray_get_Trim(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Trim(boolean var1) {
/* 118 */     chilkatJNI.CkStringArray_put_Trim(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Unique() {
/* 122 */     return chilkatJNI.CkStringArray_get_Unique(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Unique(boolean var1) {
/* 126 */     chilkatJNI.CkStringArray_put_Unique(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 130 */     return chilkatJNI.CkStringArray_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 134 */     chilkatJNI.CkStringArray_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 138 */     chilkatJNI.CkStringArray_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 142 */     return chilkatJNI.CkStringArray_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Append(String var1) {
/* 146 */     return chilkatJNI.CkStringArray_Append(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendSerialized(String var1) {
/* 150 */     return chilkatJNI.CkStringArray_AppendSerialized(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void Clear() {
/* 154 */     chilkatJNI.CkStringArray_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Contains(String var1) {
/* 158 */     return chilkatJNI.CkStringArray_Contains(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int Find(String var1, int var2) {
/* 162 */     return chilkatJNI.CkStringArray_Find(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int FindFirstMatch(String var1, int var2) {
/* 166 */     return chilkatJNI.CkStringArray_FindFirstMatch(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetString(int var1, CkString var2) {
/* 170 */     return chilkatJNI.CkStringArray_GetString(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getString(int var1) {
/* 174 */     return chilkatJNI.CkStringArray_getString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String string(int var1) {
/* 178 */     return chilkatJNI.CkStringArray_string(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetStringLen(int var1) {
/* 182 */     return chilkatJNI.CkStringArray_GetStringLen(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void InsertAt(int var1, String var2) {
/* 186 */     chilkatJNI.CkStringArray_InsertAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LastString(CkString var1) {
/* 190 */     return chilkatJNI.CkStringArray_LastString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastString() {
/* 194 */     return chilkatJNI.CkStringArray_lastString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadFromFile(String var1) {
/* 198 */     return chilkatJNI.CkStringArray_LoadFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromFile2(String var1, String var2) {
/* 202 */     return chilkatJNI.CkStringArray_LoadFromFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void LoadFromText(String var1) {
/* 206 */     chilkatJNI.CkStringArray_LoadFromText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 210 */     return chilkatJNI.CkStringArray_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean Pop(CkString var1) {
/* 214 */     return chilkatJNI.CkStringArray_Pop(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pop() {
/* 218 */     return chilkatJNI.CkStringArray_pop(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Prepend(String var1) {
/* 222 */     chilkatJNI.CkStringArray_Prepend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void Remove(String var1) {
/* 226 */     chilkatJNI.CkStringArray_Remove(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveAt(int var1) {
/* 230 */     return chilkatJNI.CkStringArray_RemoveAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void ReplaceAt(int var1, String var2) {
/* 234 */     chilkatJNI.CkStringArray_ReplaceAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 238 */     return chilkatJNI.CkStringArray_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveNthToFile(int var1, String var2) {
/* 242 */     return chilkatJNI.CkStringArray_SaveNthToFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveToFile(String var1) {
/* 246 */     return chilkatJNI.CkStringArray_SaveToFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveToFile2(String var1, String var2) {
/* 250 */     return chilkatJNI.CkStringArray_SaveToFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveToText(CkString var1) {
/* 254 */     return chilkatJNI.CkStringArray_SaveToText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String saveToText() {
/* 258 */     return chilkatJNI.CkStringArray_saveToText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Serialize(CkString var1) {
/* 262 */     return chilkatJNI.CkStringArray_Serialize(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serialize() {
/* 266 */     return chilkatJNI.CkStringArray_serialize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Sort(boolean var1) {
/* 270 */     chilkatJNI.CkStringArray_Sort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SplitAndAppend(String var1, String var2) {
/* 274 */     chilkatJNI.CkStringArray_SplitAndAppend(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean StrAt(int var1, CkString var2) {
/* 278 */     return chilkatJNI.CkStringArray_StrAt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String strAt(int var1) {
/* 282 */     return chilkatJNI.CkStringArray_strAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void Subtract(CkStringArray var1) {
/* 286 */     chilkatJNI.CkStringArray_Subtract(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void Union(CkStringArray var1) {
/* 290 */     chilkatJNI.CkStringArray_Union(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkStringArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */