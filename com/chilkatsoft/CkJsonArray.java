/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJsonArray
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJsonArray(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJsonArray var0) {
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
/*  29 */         chilkatJNI.delete_CkJsonArray(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJsonArray()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJsonArray(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJsonArray_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJsonArray_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJsonArray_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkJsonArray_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkJsonArray_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkJsonArray_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitCompact() {
/*  66 */     return chilkatJNI.CkJsonArray_get_EmitCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitCompact(boolean var1) {
/*  70 */     chilkatJNI.CkJsonArray_put_EmitCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitCrlf() {
/*  74 */     return chilkatJNI.CkJsonArray_get_EmitCrlf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitCrlf(boolean var1) {
/*  78 */     chilkatJNI.CkJsonArray_put_EmitCrlf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  82 */     chilkatJNI.CkJsonArray_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  86 */     return chilkatJNI.CkJsonArray_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  90 */     chilkatJNI.CkJsonArray_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  94 */     return chilkatJNI.CkJsonArray_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  98 */     chilkatJNI.CkJsonArray_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 102 */     return chilkatJNI.CkJsonArray_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 106 */     return chilkatJNI.CkJsonArray_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 110 */     chilkatJNI.CkJsonArray_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Size() {
/* 114 */     return chilkatJNI.CkJsonArray_get_Size(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkJsonArray_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkJsonArray_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkJsonArray_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkJsonArray_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddArrayAt(int var1) {
/* 134 */     return chilkatJNI.CkJsonArray_AddArrayAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddBoolAt(int var1, boolean var2) {
/* 138 */     return chilkatJNI.CkJsonArray_AddBoolAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddIntAt(int var1, int var2) {
/* 142 */     return chilkatJNI.CkJsonArray_AddIntAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddNullAt(int var1) {
/* 146 */     return chilkatJNI.CkJsonArray_AddNullAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddNumberAt(int var1, String var2) {
/* 150 */     return chilkatJNI.CkJsonArray_AddNumberAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddObjectAt(int var1) {
/* 154 */     return chilkatJNI.CkJsonArray_AddObjectAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddStringAt(int var1, String var2) {
/* 158 */     return chilkatJNI.CkJsonArray_AddStringAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkJsonArray ArrayAt(int var1) {
/* 162 */     long var2 = chilkatJNI.CkJsonArray_ArrayAt(this.swigCPtr, this, var1);
/* 163 */     return var2 == 0L ? null : new CkJsonArray(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BoolAt(int var1) {
/* 167 */     return chilkatJNI.CkJsonArray_BoolAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DateAt(int var1, CkDateTime var2) {
/* 171 */     return chilkatJNI.CkJsonArray_DateAt(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean DeleteAt(int var1) {
/* 175 */     return chilkatJNI.CkJsonArray_DeleteAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DtAt(int var1, boolean var2, CkDtObj var3) {
/* 179 */     return chilkatJNI.CkJsonArray_DtAt(this.swigCPtr, this, var1, var2, CkDtObj.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean Emit(CkString var1) {
/* 183 */     return chilkatJNI.CkJsonArray_Emit(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String emit() {
/* 187 */     return chilkatJNI.CkJsonArray_emit(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean EmitSb(CkStringBuilder var1) {
/* 191 */     return chilkatJNI.CkJsonArray_EmitSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int FindObject(String var1, String var2, boolean var3) {
/* 195 */     return chilkatJNI.CkJsonArray_FindObject(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public int FindString(String var1, boolean var2) {
/* 199 */     return chilkatJNI.CkJsonArray_FindString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int IntAt(int var1) {
/* 203 */     return chilkatJNI.CkJsonArray_IntAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsNullAt(int var1) {
/* 207 */     return chilkatJNI.CkJsonArray_IsNullAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Load(String var1) {
/* 211 */     return chilkatJNI.CkJsonArray_Load(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadSb(CkStringBuilder var1) {
/* 215 */     return chilkatJNI.CkJsonArray_LoadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkJsonObject ObjectAt(int var1) {
/* 219 */     long var2 = chilkatJNI.CkJsonArray_ObjectAt(this.swigCPtr, this, var1);
/* 220 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 224 */     return chilkatJNI.CkJsonArray_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBoolAt(int var1, boolean var2) {
/* 228 */     return chilkatJNI.CkJsonArray_SetBoolAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetIntAt(int var1, int var2) {
/* 232 */     return chilkatJNI.CkJsonArray_SetIntAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetNullAt(int var1) {
/* 236 */     return chilkatJNI.CkJsonArray_SetNullAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetNumberAt(int var1, String var2) {
/* 240 */     return chilkatJNI.CkJsonArray_SetNumberAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetStringAt(int var1, String var2) {
/* 244 */     return chilkatJNI.CkJsonArray_SetStringAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean StringAt(int var1, CkString var2) {
/* 248 */     return chilkatJNI.CkJsonArray_StringAt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String stringAt(int var1) {
/* 252 */     return chilkatJNI.CkJsonArray_stringAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int TypeAt(int var1) {
/* 256 */     return chilkatJNI.CkJsonArray_TypeAt(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJsonArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */