/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHtmlToXml
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkHtmlToXml(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHtmlToXml var0) {
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
/*  29 */         chilkatJNI.delete_CkHtmlToXml(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkHtmlToXml()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkHtmlToXml(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkHtmlToXml_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkHtmlToXml_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkHtmlToXml_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkHtmlToXml_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkHtmlToXml_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkHtmlToXml_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_DropCustomTags() {
/*  66 */     return chilkatJNI.CkHtmlToXml_get_DropCustomTags(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DropCustomTags(boolean var1) {
/*  70 */     chilkatJNI.CkHtmlToXml_put_DropCustomTags(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Html(CkString var1) {
/*  74 */     chilkatJNI.CkHtmlToXml_get_Html(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String html() {
/*  78 */     return chilkatJNI.CkHtmlToXml_html(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Html(String var1) {
/*  82 */     chilkatJNI.CkHtmlToXml_put_Html(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkHtmlToXml_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkHtmlToXml_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkHtmlToXml_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkHtmlToXml_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkHtmlToXml_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkHtmlToXml_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkHtmlToXml_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkHtmlToXml_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Nbsp() {
/* 118 */     return chilkatJNI.CkHtmlToXml_get_Nbsp(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Nbsp(int var1) {
/* 122 */     chilkatJNI.CkHtmlToXml_put_Nbsp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 126 */     return chilkatJNI.CkHtmlToXml_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 130 */     chilkatJNI.CkHtmlToXml_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 134 */     chilkatJNI.CkHtmlToXml_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 138 */     return chilkatJNI.CkHtmlToXml_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_XmlCharset(CkString var1) {
/* 142 */     chilkatJNI.CkHtmlToXml_get_XmlCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String xmlCharset() {
/* 146 */     return chilkatJNI.CkHtmlToXml_xmlCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_XmlCharset(String var1) {
/* 150 */     chilkatJNI.CkHtmlToXml_put_XmlCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ConvertFile(String var1, String var2) {
/* 154 */     return chilkatJNI.CkHtmlToXml_ConvertFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void DropTagType(String var1) {
/* 158 */     chilkatJNI.CkHtmlToXml_DropTagType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void DropTextFormattingTags() {
/* 162 */     chilkatJNI.CkHtmlToXml_DropTextFormattingTags(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean IsUnlocked() {
/* 166 */     return chilkatJNI.CkHtmlToXml_IsUnlocked(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ReadFile(String var1, CkByteData var2) {
/* 170 */     return chilkatJNI.CkHtmlToXml_ReadFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ReadFileToString(String var1, String var2, CkString var3) {
/* 174 */     return chilkatJNI.CkHtmlToXml_ReadFileToString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readFileToString(String var1, String var2) {
/* 178 */     return chilkatJNI.CkHtmlToXml_readFileToString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 182 */     return chilkatJNI.CkHtmlToXml_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetHtmlBytes(CkByteData var1) {
/* 186 */     chilkatJNI.CkHtmlToXml_SetHtmlBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetHtmlFromFile(String var1) {
/* 190 */     return chilkatJNI.CkHtmlToXml_SetHtmlFromFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToXml(CkString var1) {
/* 194 */     return chilkatJNI.CkHtmlToXml_ToXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toXml() {
/* 198 */     return chilkatJNI.CkHtmlToXml_toXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UndropTagType(String var1) {
/* 202 */     chilkatJNI.CkHtmlToXml_UndropTagType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void UndropTextFormattingTags() {
/* 206 */     chilkatJNI.CkHtmlToXml_UndropTextFormattingTags(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 210 */     return chilkatJNI.CkHtmlToXml_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean WriteFile(String var1, CkByteData var2) {
/* 214 */     return chilkatJNI.CkHtmlToXml_WriteFile(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean WriteStringToFile(String var1, String var2, String var3) {
/* 218 */     return chilkatJNI.CkHtmlToXml_WriteStringToFile(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean Xml(CkString var1) {
/* 222 */     return chilkatJNI.CkHtmlToXml_Xml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 226 */     return chilkatJNI.CkHtmlToXml_xml(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHtmlToXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */