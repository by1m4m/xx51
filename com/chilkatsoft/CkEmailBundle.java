/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkEmailBundle
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkEmailBundle(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkEmailBundle var0) {
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
/*  29 */         chilkatJNI.delete_CkEmailBundle(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkEmailBundle()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkEmailBundle(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkEmailBundle_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkEmailBundle_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkEmailBundle_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkEmailBundle_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkEmailBundle_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkEmailBundle_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkEmailBundle_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkEmailBundle_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkEmailBundle_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkEmailBundle_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkEmailBundle_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkEmailBundle_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  90 */     return chilkatJNI.CkEmailBundle_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  94 */     chilkatJNI.CkEmailBundle_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_MessageCount() {
/*  98 */     return chilkatJNI.CkEmailBundle_get_MessageCount(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 102 */     return chilkatJNI.CkEmailBundle_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 106 */     chilkatJNI.CkEmailBundle_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 110 */     chilkatJNI.CkEmailBundle_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 114 */     return chilkatJNI.CkEmailBundle_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddEmail(CkEmail var1) {
/* 118 */     return chilkatJNI.CkEmailBundle_AddEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkEmail FindByHeader(String var1, String var2) {
/* 122 */     long var3 = chilkatJNI.CkEmailBundle_FindByHeader(this.swigCPtr, this, var1, var2);
/* 123 */     return var3 == 0L ? null : new CkEmail(var3, true);
/*     */   }
/*     */   
/*     */   public CkEmail GetEmail(int var1) {
/* 127 */     long var2 = chilkatJNI.CkEmailBundle_GetEmail(this.swigCPtr, this, var1);
/* 128 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*     */   }
/*     */   
/*     */   public CkStringArray GetUidls() {
/* 132 */     long var1 = chilkatJNI.CkEmailBundle_GetUidls(this.swigCPtr, this);
/* 133 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 137 */     return chilkatJNI.CkEmailBundle_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 141 */     return chilkatJNI.CkEmailBundle_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 145 */     return chilkatJNI.CkEmailBundle_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 149 */     return chilkatJNI.CkEmailBundle_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 153 */     return chilkatJNI.CkEmailBundle_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlString(String var1) {
/* 157 */     return chilkatJNI.CkEmailBundle_LoadXmlString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveEmail(CkEmail var1) {
/* 161 */     return chilkatJNI.CkEmailBundle_RemoveEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveEmailByIndex(int var1) {
/* 165 */     return chilkatJNI.CkEmailBundle_RemoveEmailByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 169 */     return chilkatJNI.CkEmailBundle_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXml(String var1) {
/* 173 */     return chilkatJNI.CkEmailBundle_SaveXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortByDate(boolean var1) {
/* 177 */     chilkatJNI.CkEmailBundle_SortByDate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortByRecipient(boolean var1) {
/* 181 */     chilkatJNI.CkEmailBundle_SortByRecipient(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortBySender(boolean var1) {
/* 185 */     chilkatJNI.CkEmailBundle_SortBySender(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortBySubject(boolean var1) {
/* 189 */     chilkatJNI.CkEmailBundle_SortBySubject(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkEmailBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */