/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHtmlToText
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkHtmlToText(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHtmlToText var0) {
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
/*  29 */         chilkatJNI.delete_CkHtmlToText(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkHtmlToText()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkHtmlToText(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkHtmlToText_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkHtmlToText_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkHtmlToText_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkHtmlToText_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkHtmlToText_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkHtmlToText_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DecodeHtmlEntities() {
/*  66 */     return chilkatJNI.CkHtmlToText_get_DecodeHtmlEntities(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DecodeHtmlEntities(boolean var1) {
/*  70 */     chilkatJNI.CkHtmlToText_put_DecodeHtmlEntities(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  74 */     chilkatJNI.CkHtmlToText_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  78 */     return chilkatJNI.CkHtmlToText_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  82 */     chilkatJNI.CkHtmlToText_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  86 */     return chilkatJNI.CkHtmlToText_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  90 */     chilkatJNI.CkHtmlToText_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  94 */     return chilkatJNI.CkHtmlToText_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  98 */     return chilkatJNI.CkHtmlToText_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 102 */     chilkatJNI.CkHtmlToText_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_RightMargin() {
/* 106 */     return chilkatJNI.CkHtmlToText_get_RightMargin(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_RightMargin(int var1) {
/* 110 */     chilkatJNI.CkHtmlToText_put_RightMargin(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_SuppressLinks() {
/* 114 */     return chilkatJNI.CkHtmlToText_get_SuppressLinks(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SuppressLinks(boolean var1) {
/* 118 */     chilkatJNI.CkHtmlToText_put_SuppressLinks(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 122 */     return chilkatJNI.CkHtmlToText_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 126 */     chilkatJNI.CkHtmlToText_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 130 */     chilkatJNI.CkHtmlToText_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 134 */     return chilkatJNI.CkHtmlToText_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 138 */     return chilkatJNI.CkHtmlToText_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ReadFileToString(String var1, String var2, CkString var3) {
/* 142 */     return chilkatJNI.CkHtmlToText_ReadFileToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readFileToString(String var1, String var2) {
/* 146 */     return chilkatJNI.CkHtmlToText_readFileToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 150 */     return chilkatJNI.CkHtmlToText_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToText(String var1, CkString var2) {
/* 154 */     return chilkatJNI.CkHtmlToText_ToText(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String toText(String var1) {
/* 158 */     return chilkatJNI.CkHtmlToText_toText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 162 */     return chilkatJNI.CkHtmlToText_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean WriteStringToFile(String var1, String var2, String var3) {
/* 166 */     return chilkatJNI.CkHtmlToText_WriteStringToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHtmlToText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */