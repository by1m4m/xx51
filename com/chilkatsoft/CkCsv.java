/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCsv
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCsv(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCsv var0) {
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
/*  29 */         chilkatJNI.delete_CkCsv(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCsv()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCsv(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCsv_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCsv_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCsv_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AutoTrim() {
/*  54 */     return chilkatJNI.CkCsv_get_AutoTrim(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoTrim(boolean var1) {
/*  58 */     chilkatJNI.CkCsv_put_AutoTrim(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Crlf() {
/*  62 */     return chilkatJNI.CkCsv_get_Crlf(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Crlf(boolean var1) {
/*  66 */     chilkatJNI.CkCsv_put_Crlf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  70 */     chilkatJNI.CkCsv_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  74 */     return chilkatJNI.CkCsv_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  78 */     chilkatJNI.CkCsv_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Delimiter(CkString var1) {
/*  82 */     chilkatJNI.CkCsv_get_Delimiter(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String delimiter() {
/*  86 */     return chilkatJNI.CkCsv_delimiter(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Delimiter(String var1) {
/*  90 */     chilkatJNI.CkCsv_put_Delimiter(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EnableQuotes() {
/*  94 */     return chilkatJNI.CkCsv_get_EnableQuotes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EnableQuotes(boolean var1) {
/*  98 */     chilkatJNI.CkCsv_put_EnableQuotes(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EscapeBackslash() {
/* 102 */     return chilkatJNI.CkCsv_get_EscapeBackslash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EscapeBackslash(boolean var1) {
/* 106 */     chilkatJNI.CkCsv_put_EscapeBackslash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_HasColumnNames() {
/* 110 */     return chilkatJNI.CkCsv_get_HasColumnNames(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HasColumnNames(boolean var1) {
/* 114 */     chilkatJNI.CkCsv_put_HasColumnNames(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 118 */     chilkatJNI.CkCsv_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 122 */     return chilkatJNI.CkCsv_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 126 */     chilkatJNI.CkCsv_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 130 */     return chilkatJNI.CkCsv_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 134 */     chilkatJNI.CkCsv_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 138 */     return chilkatJNI.CkCsv_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 142 */     return chilkatJNI.CkCsv_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 146 */     chilkatJNI.CkCsv_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumColumns() {
/* 150 */     return chilkatJNI.CkCsv_get_NumColumns(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumRows() {
/* 154 */     return chilkatJNI.CkCsv_get_NumRows(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 158 */     return chilkatJNI.CkCsv_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 162 */     chilkatJNI.CkCsv_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 166 */     chilkatJNI.CkCsv_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 170 */     return chilkatJNI.CkCsv_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DeleteColumn(int var1) {
/* 174 */     return chilkatJNI.CkCsv_DeleteColumn(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DeleteColumnByName(String var1) {
/* 178 */     return chilkatJNI.CkCsv_DeleteColumnByName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DeleteRow(int var1) {
/* 182 */     return chilkatJNI.CkCsv_DeleteRow(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetCell(int var1, int var2, CkString var3) {
/* 186 */     return chilkatJNI.CkCsv_GetCell(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getCell(int var1, int var2) {
/* 190 */     return chilkatJNI.CkCsv_getCell(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String cell(int var1, int var2) {
/* 194 */     return chilkatJNI.CkCsv_cell(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetCellByName(int var1, String var2, CkString var3) {
/* 198 */     return chilkatJNI.CkCsv_GetCellByName(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getCellByName(int var1, String var2) {
/* 202 */     return chilkatJNI.CkCsv_getCellByName(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String cellByName(int var1, String var2) {
/* 206 */     return chilkatJNI.CkCsv_cellByName(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetColumnName(int var1, CkString var2) {
/* 210 */     return chilkatJNI.CkCsv_GetColumnName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getColumnName(int var1) {
/* 214 */     return chilkatJNI.CkCsv_getColumnName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String columnName(int var1) {
/* 218 */     return chilkatJNI.CkCsv_columnName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetIndex(String var1) {
/* 222 */     return chilkatJNI.CkCsv_GetIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetNumCols(int var1) {
/* 226 */     return chilkatJNI.CkCsv_GetNumCols(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1) {
/* 230 */     return chilkatJNI.CkCsv_LoadFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFile2(String var1, String var2) {
/* 234 */     return chilkatJNI.CkCsv_LoadFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadFromString(String var1) {
/* 238 */     return chilkatJNI.CkCsv_LoadFromString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RowMatches(int var1, String var2, boolean var3) {
/* 242 */     return chilkatJNI.CkCsv_RowMatches(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SaveFile(String var1) {
/* 246 */     return chilkatJNI.CkCsv_SaveFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveFile2(String var1, String var2) {
/* 250 */     return chilkatJNI.CkCsv_SaveFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 254 */     return chilkatJNI.CkCsv_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveToString(CkString var1) {
/* 258 */     return chilkatJNI.CkCsv_SaveToString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String saveToString() {
/* 262 */     return chilkatJNI.CkCsv_saveToString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetCell(int var1, int var2, String var3) {
/* 266 */     return chilkatJNI.CkCsv_SetCell(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetCellByName(int var1, String var2, String var3) {
/* 270 */     return chilkatJNI.CkCsv_SetCellByName(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SetColumnName(int var1, String var2) {
/* 274 */     return chilkatJNI.CkCsv_SetColumnName(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SortByColumn(String var1, boolean var2, boolean var3) {
/* 278 */     return chilkatJNI.CkCsv_SortByColumn(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCsv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */