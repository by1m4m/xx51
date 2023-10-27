/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkXmp
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkXmp(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkXmp var0) {
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
/*  29 */         chilkatJNI.delete_CkXmp(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkXmp()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkXmp(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkXmp_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkXmp_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkXmp_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkXmp_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkXmp_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkXmp_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkXmp_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkXmp_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkXmp_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkXmp_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkXmp_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkXmp_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkXmp_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkXmp_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumEmbedded() {
/*  98 */     return chilkatJNI.CkXmp_get_NumEmbedded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_StructInnerDescrip() {
/* 102 */     return chilkatJNI.CkXmp_get_StructInnerDescrip(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StructInnerDescrip(boolean var1) {
/* 106 */     chilkatJNI.CkXmp_put_StructInnerDescrip(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkXmp_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkXmp_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkXmp_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkXmp_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddArray(CkXml var1, String var2, String var3, CkStringArray var4) {
/* 126 */     return chilkatJNI.CkXmp_AddArray(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3, CkStringArray.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public void AddNsMapping(String var1, String var2) {
/* 130 */     chilkatJNI.CkXmp_AddNsMapping(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddSimpleDate(CkXml var1, String var2, SYSTEMTIME var3) {
/* 134 */     return chilkatJNI.CkXmp_AddSimpleDate(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean AddSimpleInt(CkXml var1, String var2, int var3) {
/* 138 */     return chilkatJNI.CkXmp_AddSimpleInt(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddSimpleStr(CkXml var1, String var2, String var3) {
/* 142 */     return chilkatJNI.CkXmp_AddSimpleStr(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AddStructProp(CkXml var1, String var2, String var3, String var4) {
/* 146 */     return chilkatJNI.CkXmp_AddStructProp(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean Append(CkXml var1) {
/* 150 */     return chilkatJNI.CkXmp_Append(this.swigCPtr, this, CkXml.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DateToString(SYSTEMTIME var1, CkString var2) {
/* 154 */     return chilkatJNI.CkXmp_DateToString(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String dateToString(SYSTEMTIME var1) {
/* 158 */     return chilkatJNI.CkXmp_dateToString(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkStringArray GetArray(CkXml var1, String var2) {
/* 162 */     long var3 = chilkatJNI.CkXmp_GetArray(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/* 163 */     return var3 == 0L ? null : new CkStringArray(var3, true);
/*     */   }
/*     */   
/*     */   public CkXml GetEmbedded(int var1) {
/* 167 */     long var2 = chilkatJNI.CkXmp_GetEmbedded(this.swigCPtr, this, var1);
/* 168 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public CkXml GetProperty(CkXml var1, String var2) {
/* 172 */     long var3 = chilkatJNI.CkXmp_GetProperty(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/* 173 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetSimpleDate(CkXml var1, String var2, SYSTEMTIME var3) {
/* 177 */     return chilkatJNI.CkXmp_GetSimpleDate(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public int GetSimpleInt(CkXml var1, String var2) {
/* 181 */     return chilkatJNI.CkXmp_GetSimpleInt(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetSimpleStr(CkXml var1, String var2, CkString var3) {
/* 185 */     return chilkatJNI.CkXmp_GetSimpleStr(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getSimpleStr(CkXml var1, String var2) {
/* 189 */     return chilkatJNI.CkXmp_getSimpleStr(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public String simpleStr(CkXml var1, String var2) {
/* 193 */     return chilkatJNI.CkXmp_simpleStr(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkStringArray GetStructPropNames(CkXml var1, String var2) {
/* 197 */     long var3 = chilkatJNI.CkXmp_GetStructPropNames(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/* 198 */     return var3 == 0L ? null : new CkStringArray(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetStructValue(CkXml var1, String var2, String var3, CkString var4) {
/* 202 */     return chilkatJNI.CkXmp_GetStructValue(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getStructValue(CkXml var1, String var2, String var3) {
/* 206 */     return chilkatJNI.CkXmp_getStructValue(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String structValue(CkXml var1, String var2, String var3) {
/* 210 */     return chilkatJNI.CkXmp_structValue(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean LoadAppFile(String var1) {
/* 214 */     return chilkatJNI.CkXmp_LoadAppFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFromBuffer(CkByteData var1, String var2) {
/* 218 */     return chilkatJNI.CkXmp_LoadFromBuffer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml NewXmp() {
/* 222 */     long var1 = chilkatJNI.CkXmp_NewXmp(this.swigCPtr, this);
/* 223 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean RemoveAllEmbedded() {
/* 227 */     return chilkatJNI.CkXmp_RemoveAllEmbedded(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RemoveArray(CkXml var1, String var2) {
/* 231 */     return chilkatJNI.CkXmp_RemoveArray(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RemoveEmbedded(int var1) {
/* 235 */     return chilkatJNI.CkXmp_RemoveEmbedded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveNsMapping(String var1) {
/* 239 */     chilkatJNI.CkXmp_RemoveNsMapping(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveSimple(CkXml var1, String var2) {
/* 243 */     return chilkatJNI.CkXmp_RemoveSimple(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RemoveStruct(CkXml var1, String var2) {
/* 247 */     return chilkatJNI.CkXmp_RemoveStruct(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean RemoveStructProp(CkXml var1, String var2, String var3) {
/* 251 */     return chilkatJNI.CkXmp_RemoveStructProp(this.swigCPtr, this, CkXml.getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SaveAppFile(String var1) {
/* 255 */     return chilkatJNI.CkXmp_SaveAppFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 259 */     return chilkatJNI.CkXmp_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveToBuffer(CkByteData var1) {
/* 263 */     return chilkatJNI.CkXmp_SaveToBuffer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean StringToDate(String var1, SYSTEMTIME var2) {
/* 267 */     return chilkatJNI.CkXmp_StringToDate(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 271 */     return chilkatJNI.CkXmp_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkXmp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */