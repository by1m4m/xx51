/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkJsonObject
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkJsonObject(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkJsonObject var0) {
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
/*  29 */         chilkatJNI.delete_CkJsonObject(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkJsonObject()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkJsonObject(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkJsonObject_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkJsonObject_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkJsonObject_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkJsonObject_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkJsonObject_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkJsonObject_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DelimiterChar(CkString var1) {
/*  66 */     chilkatJNI.CkJsonObject_get_DelimiterChar(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String delimiterChar() {
/*  70 */     return chilkatJNI.CkJsonObject_delimiterChar(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DelimiterChar(String var1) {
/*  74 */     chilkatJNI.CkJsonObject_put_DelimiterChar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitCompact() {
/*  78 */     return chilkatJNI.CkJsonObject_get_EmitCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitCompact(boolean var1) {
/*  82 */     chilkatJNI.CkJsonObject_put_EmitCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitCrLf() {
/*  86 */     return chilkatJNI.CkJsonObject_get_EmitCrLf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitCrLf(boolean var1) {
/*  90 */     chilkatJNI.CkJsonObject_put_EmitCrLf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_I() {
/*  94 */     return chilkatJNI.CkJsonObject_get_I(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_I(int var1) {
/*  98 */     chilkatJNI.CkJsonObject_put_I(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_J() {
/* 102 */     return chilkatJNI.CkJsonObject_get_J(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_J(int var1) {
/* 106 */     chilkatJNI.CkJsonObject_put_J(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_K() {
/* 110 */     return chilkatJNI.CkJsonObject_get_K(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_K(int var1) {
/* 114 */     chilkatJNI.CkJsonObject_put_K(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 118 */     chilkatJNI.CkJsonObject_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 122 */     return chilkatJNI.CkJsonObject_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 126 */     chilkatJNI.CkJsonObject_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 130 */     return chilkatJNI.CkJsonObject_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 134 */     chilkatJNI.CkJsonObject_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 138 */     return chilkatJNI.CkJsonObject_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 142 */     return chilkatJNI.CkJsonObject_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 146 */     chilkatJNI.CkJsonObject_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Size() {
/* 150 */     return chilkatJNI.CkJsonObject_get_Size(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 154 */     return chilkatJNI.CkJsonObject_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 158 */     chilkatJNI.CkJsonObject_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 162 */     chilkatJNI.CkJsonObject_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 166 */     return chilkatJNI.CkJsonObject_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddArrayAt(int var1, String var2) {
/* 170 */     return chilkatJNI.CkJsonObject_AddArrayAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddBoolAt(int var1, String var2, boolean var3) {
/* 174 */     return chilkatJNI.CkJsonObject_AddBoolAt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddIntAt(int var1, String var2, int var3) {
/* 178 */     return chilkatJNI.CkJsonObject_AddIntAt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddNullAt(int var1, String var2) {
/* 182 */     return chilkatJNI.CkJsonObject_AddNullAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddNumberAt(int var1, String var2, String var3) {
/* 186 */     return chilkatJNI.CkJsonObject_AddNumberAt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddObjectAt(int var1, String var2) {
/* 190 */     return chilkatJNI.CkJsonObject_AddObjectAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddStringAt(int var1, String var2, String var3) {
/* 194 */     return chilkatJNI.CkJsonObject_AddStringAt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkJsonArray AppendArray(String var1) {
/* 198 */     long var2 = chilkatJNI.CkJsonObject_AppendArray(this.swigCPtr, this, var1);
/* 199 */     return var2 == 0L ? null : new CkJsonArray(var2, true);
/*     */   }
/*     */   
/*     */   public boolean AppendBool(String var1, boolean var2) {
/* 203 */     return chilkatJNI.CkJsonObject_AppendBool(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendInt(String var1, int var2) {
/* 207 */     return chilkatJNI.CkJsonObject_AppendInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkJsonObject AppendObject(String var1) {
/* 211 */     long var2 = chilkatJNI.CkJsonObject_AppendObject(this.swigCPtr, this, var1);
/* 212 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public boolean AppendString(String var1, String var2) {
/* 216 */     return chilkatJNI.CkJsonObject_AppendString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendStringArray(String var1, CkStringTable var2) {
/* 220 */     return chilkatJNI.CkJsonObject_AppendStringArray(this.swigCPtr, this, var1, CkStringTable.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkJsonArray ArrayAt(int var1) {
/* 224 */     long var2 = chilkatJNI.CkJsonObject_ArrayAt(this.swigCPtr, this, var1);
/* 225 */     return var2 == 0L ? null : new CkJsonArray(var2, true);
/*     */   }
/*     */   
/*     */   public CkJsonArray ArrayOf(String var1) {
/* 229 */     long var2 = chilkatJNI.CkJsonObject_ArrayOf(this.swigCPtr, this, var1);
/* 230 */     return var2 == 0L ? null : new CkJsonArray(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BoolAt(int var1) {
/* 234 */     return chilkatJNI.CkJsonObject_BoolAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean BoolOf(String var1) {
/* 238 */     return chilkatJNI.CkJsonObject_BoolOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean BytesOf(String var1, String var2, CkBinData var3) {
/* 242 */     return chilkatJNI.CkJsonObject_BytesOf(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public CkJsonObject Clone() {
/* 246 */     long var1 = chilkatJNI.CkJsonObject_Clone(this.swigCPtr, this);
/* 247 */     return var1 == 0L ? null : new CkJsonObject(var1, true);
/*     */   }
/*     */   
/*     */   public boolean DateOf(String var1, CkDateTime var2) {
/* 251 */     return chilkatJNI.CkJsonObject_DateOf(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean Delete(String var1) {
/* 255 */     return chilkatJNI.CkJsonObject_Delete(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DeleteAt(int var1) {
/* 259 */     return chilkatJNI.CkJsonObject_DeleteAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DtOf(String var1, boolean var2, CkDtObj var3) {
/* 263 */     return chilkatJNI.CkJsonObject_DtOf(this.swigCPtr, this, var1, var2, CkDtObj.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean Emit(CkString var1) {
/* 267 */     return chilkatJNI.CkJsonObject_Emit(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String emit() {
/* 271 */     return chilkatJNI.CkJsonObject_emit(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean EmitSb(CkStringBuilder var1) {
/* 275 */     return chilkatJNI.CkJsonObject_EmitSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean EmitWithSubs(CkHashtable var1, boolean var2, CkString var3) {
/* 279 */     return chilkatJNI.CkJsonObject_EmitWithSubs(this.swigCPtr, this, CkHashtable.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String emitWithSubs(CkHashtable var1, boolean var2) {
/* 283 */     return chilkatJNI.CkJsonObject_emitWithSubs(this.swigCPtr, this, CkHashtable.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkJsonObject FindObjectWithMember(String var1) {
/* 287 */     long var2 = chilkatJNI.CkJsonObject_FindObjectWithMember(this.swigCPtr, this, var1);
/* 288 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public CkJsonObject FindRecord(String var1, String var2, String var3, boolean var4) {
/* 292 */     long var5 = chilkatJNI.CkJsonObject_FindRecord(this.swigCPtr, this, var1, var2, var3, var4);
/* 293 */     return var5 == 0L ? null : new CkJsonObject(var5, true);
/*     */   }
/*     */   
/*     */   public boolean FindRecordString(String var1, String var2, String var3, boolean var4, String var5, CkString var6) {
/* 297 */     return chilkatJNI.CkJsonObject_FindRecordString(this.swigCPtr, this, var1, var2, var3, var4, var5, CkString.getCPtr(var6), var6);
/*     */   }
/*     */   
/*     */   public String findRecordString(String var1, String var2, String var3, boolean var4, String var5) {
/* 301 */     return chilkatJNI.CkJsonObject_findRecordString(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */   
/*     */   public boolean FirebaseApplyEvent(String var1, String var2) {
/* 305 */     return chilkatJNI.CkJsonObject_FirebaseApplyEvent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FirebasePatch(String var1, String var2) {
/* 309 */     return chilkatJNI.CkJsonObject_FirebasePatch(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FirebasePut(String var1, String var2) {
/* 313 */     return chilkatJNI.CkJsonObject_FirebasePut(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkJsonObject GetDocRoot() {
/* 317 */     long var1 = chilkatJNI.CkJsonObject_GetDocRoot(this.swigCPtr, this);
/* 318 */     return var1 == 0L ? null : new CkJsonObject(var1, true);
/*     */   }
/*     */   
/*     */   public boolean HasMember(String var1) {
/* 322 */     return chilkatJNI.CkJsonObject_HasMember(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int IndexOf(String var1) {
/* 326 */     return chilkatJNI.CkJsonObject_IndexOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int IntAt(int var1) {
/* 330 */     return chilkatJNI.CkJsonObject_IntAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int IntOf(String var1) {
/* 334 */     return chilkatJNI.CkJsonObject_IntOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsNullAt(int var1) {
/* 338 */     return chilkatJNI.CkJsonObject_IsNullAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsNullOf(String var1) {
/* 342 */     return chilkatJNI.CkJsonObject_IsNullOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int JsonTypeOf(String var1) {
/* 346 */     return chilkatJNI.CkJsonObject_JsonTypeOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Load(String var1) {
/* 350 */     return chilkatJNI.CkJsonObject_Load(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1) {
/* 354 */     return chilkatJNI.CkJsonObject_LoadFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadPredefined(String var1) {
/* 358 */     return chilkatJNI.CkJsonObject_LoadPredefined(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadSb(CkStringBuilder var1) {
/* 362 */     return chilkatJNI.CkJsonObject_LoadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean NameAt(int var1, CkString var2) {
/* 366 */     return chilkatJNI.CkJsonObject_NameAt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String nameAt(int var1) {
/* 370 */     return chilkatJNI.CkJsonObject_nameAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkJsonObject ObjectAt(int var1) {
/* 374 */     long var2 = chilkatJNI.CkJsonObject_ObjectAt(this.swigCPtr, this, var1);
/* 375 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public CkJsonObject ObjectOf(String var1) {
/* 379 */     long var2 = chilkatJNI.CkJsonObject_ObjectOf(this.swigCPtr, this, var1);
/* 380 */     return var2 == 0L ? null : new CkJsonObject(var2, true);
/*     */   }
/*     */   
/*     */   public boolean Predefine(String var1) {
/* 384 */     return chilkatJNI.CkJsonObject_Predefine(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Rename(String var1, String var2) {
/* 388 */     return chilkatJNI.CkJsonObject_Rename(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RenameAt(int var1, String var2) {
/* 392 */     return chilkatJNI.CkJsonObject_RenameAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 396 */     return chilkatJNI.CkJsonObject_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetBoolAt(int var1, boolean var2) {
/* 400 */     return chilkatJNI.CkJsonObject_SetBoolAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetBoolOf(String var1, boolean var2) {
/* 404 */     return chilkatJNI.CkJsonObject_SetBoolOf(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetIntAt(int var1, int var2) {
/* 408 */     return chilkatJNI.CkJsonObject_SetIntAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetIntOf(String var1, int var2) {
/* 412 */     return chilkatJNI.CkJsonObject_SetIntOf(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetNullAt(int var1) {
/* 416 */     return chilkatJNI.CkJsonObject_SetNullAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetNullOf(String var1) {
/* 420 */     return chilkatJNI.CkJsonObject_SetNullOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetNumberAt(int var1, String var2) {
/* 424 */     return chilkatJNI.CkJsonObject_SetNumberAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetNumberOf(String var1, String var2) {
/* 428 */     return chilkatJNI.CkJsonObject_SetNumberOf(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetStringAt(int var1, String var2) {
/* 432 */     return chilkatJNI.CkJsonObject_SetStringAt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetStringOf(String var1, String var2) {
/* 436 */     return chilkatJNI.CkJsonObject_SetStringOf(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int SizeOfArray(String var1) {
/* 440 */     return chilkatJNI.CkJsonObject_SizeOfArray(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StringAt(int var1, CkString var2) {
/* 444 */     return chilkatJNI.CkJsonObject_StringAt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String stringAt(int var1) {
/* 448 */     return chilkatJNI.CkJsonObject_stringAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StringOf(String var1, CkString var2) {
/* 452 */     return chilkatJNI.CkJsonObject_StringOf(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String stringOf(String var1) {
/* 456 */     return chilkatJNI.CkJsonObject_stringOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean StringOfSb(String var1, CkStringBuilder var2) {
/* 460 */     return chilkatJNI.CkJsonObject_StringOfSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public int TypeAt(int var1) {
/* 464 */     return chilkatJNI.CkJsonObject_TypeAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UpdateBd(String var1, String var2, CkBinData var3) {
/* 468 */     return chilkatJNI.CkJsonObject_UpdateBd(this.swigCPtr, this, var1, var2, CkBinData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean UpdateBool(String var1, boolean var2) {
/* 472 */     return chilkatJNI.CkJsonObject_UpdateBool(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateInt(String var1, int var2) {
/* 476 */     return chilkatJNI.CkJsonObject_UpdateInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateNewArray(String var1) {
/* 480 */     return chilkatJNI.CkJsonObject_UpdateNewArray(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UpdateNewObject(String var1) {
/* 484 */     return chilkatJNI.CkJsonObject_UpdateNewObject(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UpdateNull(String var1) {
/* 488 */     return chilkatJNI.CkJsonObject_UpdateNull(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UpdateNumber(String var1, String var2) {
/* 492 */     return chilkatJNI.CkJsonObject_UpdateNumber(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateSb(String var1, CkStringBuilder var2) {
/* 496 */     return chilkatJNI.CkJsonObject_UpdateSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateString(String var1, String var2) {
/* 500 */     return chilkatJNI.CkJsonObject_UpdateString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkJsonObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */