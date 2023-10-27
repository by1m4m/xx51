/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkRss
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkRss(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkRss var0) {
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
/*  29 */         chilkatJNI.delete_CkRss(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkRss()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkRss(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkRss_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkRss_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkRss_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkRss_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  58 */     chilkatJNI.CkRss_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  62 */     return chilkatJNI.CkRss_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  66 */     chilkatJNI.CkRss_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  70 */     chilkatJNI.CkRss_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  74 */     return chilkatJNI.CkRss_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  78 */     chilkatJNI.CkRss_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  82 */     return chilkatJNI.CkRss_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  86 */     chilkatJNI.CkRss_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  90 */     return chilkatJNI.CkRss_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  94 */     return chilkatJNI.CkRss_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  98 */     chilkatJNI.CkRss_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumChannels() {
/* 102 */     return chilkatJNI.CkRss_get_NumChannels(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumItems() {
/* 106 */     return chilkatJNI.CkRss_get_NumItems(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkRss_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkRss_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkRss_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkRss_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkRss AddNewChannel() {
/* 126 */     long var1 = chilkatJNI.CkRss_AddNewChannel(this.swigCPtr, this);
/* 127 */     return var1 == 0L ? null : new CkRss(var1, true);
/*     */   }
/*     */   
/*     */   public CkRss AddNewImage() {
/* 131 */     long var1 = chilkatJNI.CkRss_AddNewImage(this.swigCPtr, this);
/* 132 */     return var1 == 0L ? null : new CkRss(var1, true);
/*     */   }
/*     */   
/*     */   public CkRss AddNewItem() {
/* 136 */     long var1 = chilkatJNI.CkRss_AddNewItem(this.swigCPtr, this);
/* 137 */     return var1 == 0L ? null : new CkRss(var1, true);
/*     */   }
/*     */   
/*     */   public boolean DownloadRss(String var1) {
/* 141 */     return chilkatJNI.CkRss_DownloadRss(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask DownloadRssAsync(String var1) {
/* 145 */     long var2 = chilkatJNI.CkRss_DownloadRssAsync(this.swigCPtr, this, var1);
/* 146 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetAttr(String var1, String var2, CkString var3) {
/* 150 */     return chilkatJNI.CkRss_GetAttr(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getAttr(String var1, String var2) {
/* 154 */     return chilkatJNI.CkRss_getAttr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String attr(String var1, String var2) {
/* 158 */     return chilkatJNI.CkRss_attr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkRss GetChannel(int var1) {
/* 162 */     long var2 = chilkatJNI.CkRss_GetChannel(this.swigCPtr, this, var1);
/* 163 */     return var2 == 0L ? null : new CkRss(var2, true);
/*     */   }
/*     */   
/*     */   public int GetCount(String var1) {
/* 167 */     return chilkatJNI.CkRss_GetCount(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetDate(String var1, SYSTEMTIME var2) {
/* 171 */     return chilkatJNI.CkRss_GetDate(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetDateStr(String var1, CkString var2) {
/* 175 */     return chilkatJNI.CkRss_GetDateStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getDateStr(String var1) {
/* 179 */     return chilkatJNI.CkRss_getDateStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String dateStr(String var1) {
/* 183 */     return chilkatJNI.CkRss_dateStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkRss GetImage() {
/* 187 */     long var1 = chilkatJNI.CkRss_GetImage(this.swigCPtr, this);
/* 188 */     return var1 == 0L ? null : new CkRss(var1, true);
/*     */   }
/*     */   
/*     */   public int GetInt(String var1) {
/* 192 */     return chilkatJNI.CkRss_GetInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkRss GetItem(int var1) {
/* 196 */     long var2 = chilkatJNI.CkRss_GetItem(this.swigCPtr, this, var1);
/* 197 */     return var2 == 0L ? null : new CkRss(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetString(String var1, CkString var2) {
/* 201 */     return chilkatJNI.CkRss_GetString(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getString(String var1) {
/* 205 */     return chilkatJNI.CkRss_getString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String string(String var1) {
/* 209 */     return chilkatJNI.CkRss_string(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadRssFile(String var1) {
/* 213 */     return chilkatJNI.CkRss_LoadRssFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadRssString(String var1) {
/* 217 */     return chilkatJNI.CkRss_LoadRssString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean MGetAttr(String var1, int var2, String var3, CkString var4) {
/* 221 */     return chilkatJNI.CkRss_MGetAttr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String mGetAttr(String var1, int var2, String var3) {
/* 225 */     return chilkatJNI.CkRss_mGetAttr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean MGetString(String var1, int var2, CkString var3) {
/* 229 */     return chilkatJNI.CkRss_MGetString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String mGetString(String var1, int var2) {
/* 233 */     return chilkatJNI.CkRss_mGetString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean MSetAttr(String var1, int var2, String var3, String var4) {
/* 237 */     return chilkatJNI.CkRss_MSetAttr(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean MSetString(String var1, int var2, String var3) {
/* 241 */     return chilkatJNI.CkRss_MSetString(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void NewRss() {
/* 245 */     chilkatJNI.CkRss_NewRss(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void Remove(String var1) {
/* 249 */     chilkatJNI.CkRss_Remove(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 253 */     return chilkatJNI.CkRss_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetAttr(String var1, String var2, String var3) {
/* 257 */     chilkatJNI.CkRss_SetAttr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void SetDate(String var1, SYSTEMTIME var2) {
/* 261 */     chilkatJNI.CkRss_SetDate(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public void SetDateNow(String var1) {
/* 265 */     chilkatJNI.CkRss_SetDateNow(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetDateStr(String var1, String var2) {
/* 269 */     chilkatJNI.CkRss_SetDateStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetInt(String var1, int var2) {
/* 273 */     chilkatJNI.CkRss_SetInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetString(String var1, String var2) {
/* 277 */     chilkatJNI.CkRss_SetString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToXmlString(CkString var1) {
/* 281 */     return chilkatJNI.CkRss_ToXmlString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toXmlString() {
/* 285 */     return chilkatJNI.CkRss_toXmlString(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkRss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */