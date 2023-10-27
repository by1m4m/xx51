/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAtom
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAtom(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAtom var0) {
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
/*  29 */         chilkatJNI.delete_CkAtom(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAtom()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAtom(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAtom_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAtom_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAtom_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkAtom_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkAtom_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  62 */     chilkatJNI.CkAtom_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  66 */     return chilkatJNI.CkAtom_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  70 */     chilkatJNI.CkAtom_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  74 */     chilkatJNI.CkAtom_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  78 */     return chilkatJNI.CkAtom_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  82 */     chilkatJNI.CkAtom_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  86 */     return chilkatJNI.CkAtom_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  90 */     chilkatJNI.CkAtom_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  94 */     return chilkatJNI.CkAtom_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  98 */     return chilkatJNI.CkAtom_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 102 */     chilkatJNI.CkAtom_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumEntries() {
/* 106 */     return chilkatJNI.CkAtom_get_NumEntries(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 110 */     return chilkatJNI.CkAtom_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 114 */     chilkatJNI.CkAtom_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 118 */     chilkatJNI.CkAtom_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 122 */     return chilkatJNI.CkAtom_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int AddElement(String var1, String var2) {
/* 126 */     return chilkatJNI.CkAtom_AddElement(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int AddElementDate(String var1, SYSTEMTIME var2) {
/* 130 */     return chilkatJNI.CkAtom_AddElementDate(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public int AddElementDateStr(String var1, String var2) {
/* 134 */     return chilkatJNI.CkAtom_AddElementDateStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int AddElementDt(String var1, CkDateTime var2) {
/* 138 */     return chilkatJNI.CkAtom_AddElementDt(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public int AddElementHtml(String var1, String var2) {
/* 142 */     return chilkatJNI.CkAtom_AddElementHtml(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int AddElementXHtml(String var1, String var2) {
/* 146 */     return chilkatJNI.CkAtom_AddElementXHtml(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int AddElementXml(String var1, String var2) {
/* 150 */     return chilkatJNI.CkAtom_AddElementXml(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddEntry(String var1) {
/* 154 */     chilkatJNI.CkAtom_AddEntry(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddLink(String var1, String var2, String var3, String var4) {
/* 158 */     chilkatJNI.CkAtom_AddLink(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public void AddPerson(String var1, String var2, String var3, String var4) {
/* 162 */     chilkatJNI.CkAtom_AddPerson(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public void DeleteElement(String var1, int var2) {
/* 166 */     chilkatJNI.CkAtom_DeleteElement(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void DeleteElementAttr(String var1, int var2, String var3) {
/* 170 */     chilkatJNI.CkAtom_DeleteElementAttr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void DeletePerson(String var1, int var2) {
/* 174 */     chilkatJNI.CkAtom_DeletePerson(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean DownloadAtom(String var1) {
/* 178 */     return chilkatJNI.CkAtom_DownloadAtom(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask DownloadAtomAsync(String var1) {
/* 182 */     long var2 = chilkatJNI.CkAtom_DownloadAtomAsync(this.swigCPtr, this, var1);
/* 183 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetElement(String var1, int var2, CkString var3) {
/* 187 */     return chilkatJNI.CkAtom_GetElement(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getElement(String var1, int var2) {
/* 191 */     return chilkatJNI.CkAtom_getElement(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String element(String var1, int var2) {
/* 195 */     return chilkatJNI.CkAtom_element(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean GetElementAttr(String var1, int var2, String var3, CkString var4) {
/* 199 */     return chilkatJNI.CkAtom_GetElementAttr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getElementAttr(String var1, int var2, String var3) {
/* 203 */     return chilkatJNI.CkAtom_getElementAttr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String elementAttr(String var1, int var2, String var3) {
/* 207 */     return chilkatJNI.CkAtom_elementAttr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public int GetElementCount(String var1) {
/* 211 */     return chilkatJNI.CkAtom_GetElementCount(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetElementDate(String var1, int var2, SYSTEMTIME var3) {
/* 215 */     return chilkatJNI.CkAtom_GetElementDate(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean GetElementDateStr(String var1, int var2, CkString var3) {
/* 219 */     return chilkatJNI.CkAtom_GetElementDateStr(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getElementDateStr(String var1, int var2) {
/* 223 */     return chilkatJNI.CkAtom_getElementDateStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String elementDateStr(String var1, int var2) {
/* 227 */     return chilkatJNI.CkAtom_elementDateStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkDateTime GetElementDt(String var1, int var2) {
/* 231 */     long var3 = chilkatJNI.CkAtom_GetElementDt(this.swigCPtr, this, var1, var2);
/* 232 */     return var3 == 0L ? null : new CkDateTime(var3, true);
/*     */   }
/*     */   
/*     */   public CkAtom GetEntry(int var1) {
/* 236 */     long var2 = chilkatJNI.CkAtom_GetEntry(this.swigCPtr, this, var1);
/* 237 */     return var2 == 0L ? null : new CkAtom(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetLinkHref(String var1, CkString var2) {
/* 241 */     return chilkatJNI.CkAtom_GetLinkHref(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getLinkHref(String var1) {
/* 245 */     return chilkatJNI.CkAtom_getLinkHref(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String linkHref(String var1) {
/* 249 */     return chilkatJNI.CkAtom_linkHref(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetPersonInfo(String var1, int var2, String var3, CkString var4) {
/* 253 */     return chilkatJNI.CkAtom_GetPersonInfo(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getPersonInfo(String var1, int var2, String var3) {
/* 257 */     return chilkatJNI.CkAtom_getPersonInfo(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String personInfo(String var1, int var2, String var3) {
/* 261 */     return chilkatJNI.CkAtom_personInfo(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetTopAttr(String var1, CkString var2) {
/* 265 */     return chilkatJNI.CkAtom_GetTopAttr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getTopAttr(String var1) {
/* 269 */     return chilkatJNI.CkAtom_getTopAttr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String topAttr(String var1) {
/* 273 */     return chilkatJNI.CkAtom_topAttr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasElement(String var1) {
/* 277 */     return chilkatJNI.CkAtom_HasElement(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 281 */     return chilkatJNI.CkAtom_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void NewEntry() {
/* 285 */     chilkatJNI.CkAtom_NewEntry(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void NewFeed() {
/* 289 */     chilkatJNI.CkAtom_NewFeed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 293 */     return chilkatJNI.CkAtom_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetElementAttr(String var1, int var2, String var3, String var4) {
/* 297 */     chilkatJNI.CkAtom_SetElementAttr(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public void SetTopAttr(String var1, String var2) {
/* 301 */     chilkatJNI.CkAtom_SetTopAttr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ToXmlString(CkString var1) {
/* 305 */     return chilkatJNI.CkAtom_ToXmlString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String toXmlString() {
/* 309 */     return chilkatJNI.CkAtom_toXmlString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void UpdateElement(String var1, int var2, String var3) {
/* 313 */     chilkatJNI.CkAtom_UpdateElement(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementDate(String var1, int var2, SYSTEMTIME var3) {
/* 317 */     chilkatJNI.CkAtom_UpdateElementDate(this.swigCPtr, this, var1, var2, SYSTEMTIME.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementDateStr(String var1, int var2, String var3) {
/* 321 */     chilkatJNI.CkAtom_UpdateElementDateStr(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementDt(String var1, int var2, CkDateTime var3) {
/* 325 */     chilkatJNI.CkAtom_UpdateElementDt(this.swigCPtr, this, var1, var2, CkDateTime.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementHtml(String var1, int var2, String var3) {
/* 329 */     chilkatJNI.CkAtom_UpdateElementHtml(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementXHtml(String var1, int var2, String var3) {
/* 333 */     chilkatJNI.CkAtom_UpdateElementXHtml(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UpdateElementXml(String var1, int var2, String var3) {
/* 337 */     chilkatJNI.CkAtom_UpdateElementXml(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void UpdatePerson(String var1, int var2, String var3, String var4, String var5) {
/* 341 */     chilkatJNI.CkAtom_UpdatePerson(this.swigCPtr, this, var1, var2, var3, var4, var5);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAtom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */