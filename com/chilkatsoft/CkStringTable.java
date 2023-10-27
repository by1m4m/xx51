/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkStringTable
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkStringTable(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkStringTable var0) {
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
/*  29 */         chilkatJNI.delete_CkStringTable(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkStringTable()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkStringTable(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkStringTable_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkStringTable_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkStringTable_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int get_Count() {
/*  54 */     return chilkatJNI.CkStringTable_get_Count(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  58 */     chilkatJNI.CkStringTable_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  62 */     return chilkatJNI.CkStringTable_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  66 */     chilkatJNI.CkStringTable_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  70 */     chilkatJNI.CkStringTable_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  74 */     return chilkatJNI.CkStringTable_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  78 */     chilkatJNI.CkStringTable_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  82 */     return chilkatJNI.CkStringTable_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  86 */     chilkatJNI.CkStringTable_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  90 */     return chilkatJNI.CkStringTable_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  94 */     return chilkatJNI.CkStringTable_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  98 */     chilkatJNI.CkStringTable_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 102 */     return chilkatJNI.CkStringTable_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 106 */     chilkatJNI.CkStringTable_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 110 */     chilkatJNI.CkStringTable_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 114 */     return chilkatJNI.CkStringTable_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Append(String var1) {
/* 118 */     return chilkatJNI.CkStringTable_Append(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendFromFile(int var1, String var2, String var3) {
/* 122 */     return chilkatJNI.CkStringTable_AppendFromFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AppendFromSb(CkStringBuilder var1) {
/* 126 */     return chilkatJNI.CkStringTable_AppendFromSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void Clear() {
/* 130 */     chilkatJNI.CkStringTable_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IntAt(int var1) {
/* 134 */     return chilkatJNI.CkStringTable_IntAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 138 */     return chilkatJNI.CkStringTable_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveToFile(String var1, boolean var2, String var3) {
/* 142 */     return chilkatJNI.CkStringTable_SaveToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SplitAndAppend(String var1, String var2, boolean var3, boolean var4) {
/* 146 */     return chilkatJNI.CkStringTable_SplitAndAppend(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean StringAt(int var1, CkString var2) {
/* 150 */     return chilkatJNI.CkStringTable_StringAt(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String stringAt(int var1) {
/* 154 */     return chilkatJNI.CkStringTable_stringAt(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkStringTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */