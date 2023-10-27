/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCache
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCache(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCache var0) {
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
/*  29 */         chilkatJNI.delete_CkCache(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCache()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCache(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCache_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCache_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCache_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  54 */     chilkatJNI.CkCache_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  58 */     return chilkatJNI.CkCache_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  62 */     chilkatJNI.CkCache_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  66 */     chilkatJNI.CkCache_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  70 */     return chilkatJNI.CkCache_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  74 */     chilkatJNI.CkCache_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  78 */     return chilkatJNI.CkCache_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/*  82 */     chilkatJNI.CkCache_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  86 */     return chilkatJNI.CkCache_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastEtagFetched(CkString var1) {
/*  90 */     chilkatJNI.CkCache_get_LastEtagFetched(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastEtagFetched() {
/*  94 */     return chilkatJNI.CkCache_lastEtagFetched(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastExpirationFetched(SYSTEMTIME var1) {
/*  98 */     chilkatJNI.CkCache_get_LastExpirationFetched(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_LastExpirationFetchedStr(CkString var1) {
/* 102 */     chilkatJNI.CkCache_get_LastExpirationFetchedStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastExpirationFetchedStr() {
/* 106 */     return chilkatJNI.CkCache_lastExpirationFetchedStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastHitExpired() {
/* 110 */     return chilkatJNI.CkCache_get_LastHitExpired(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastKeyFetched(CkString var1) {
/* 114 */     chilkatJNI.CkCache_get_LastKeyFetched(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastKeyFetched() {
/* 118 */     return chilkatJNI.CkCache_lastKeyFetched(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 122 */     return chilkatJNI.CkCache_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 126 */     chilkatJNI.CkCache_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Level() {
/* 130 */     return chilkatJNI.CkCache_get_Level(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Level(int var1) {
/* 134 */     chilkatJNI.CkCache_put_Level(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumRoots() {
/* 138 */     return chilkatJNI.CkCache_get_NumRoots(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 142 */     return chilkatJNI.CkCache_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 146 */     chilkatJNI.CkCache_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 150 */     chilkatJNI.CkCache_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 154 */     return chilkatJNI.CkCache_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void AddRoot(String var1) {
/* 158 */     chilkatJNI.CkCache_AddRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int DeleteAll() {
/* 162 */     return chilkatJNI.CkCache_DeleteAll(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int DeleteAllExpired() {
/* 166 */     return chilkatJNI.CkCache_DeleteAllExpired(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DeleteFromCache(String var1) {
/* 170 */     return chilkatJNI.CkCache_DeleteFromCache(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int DeleteOlder(SYSTEMTIME var1) {
/* 174 */     return chilkatJNI.CkCache_DeleteOlder(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int DeleteOlderDt(CkDateTime var1) {
/* 178 */     return chilkatJNI.CkCache_DeleteOlderDt(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int DeleteOlderStr(String var1) {
/* 182 */     return chilkatJNI.CkCache_DeleteOlderStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean FetchFromCache(String var1, CkByteData var2) {
/* 186 */     return chilkatJNI.CkCache_FetchFromCache(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean FetchText(String var1, CkString var2) {
/* 190 */     return chilkatJNI.CkCache_FetchText(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String fetchText(String var1) {
/* 194 */     return chilkatJNI.CkCache_fetchText(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEtag(String var1, CkString var2) {
/* 198 */     return chilkatJNI.CkCache_GetEtag(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEtag(String var1) {
/* 202 */     return chilkatJNI.CkCache_getEtag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String etag(String var1) {
/* 206 */     return chilkatJNI.CkCache_etag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetExpiration(String var1, SYSTEMTIME var2) {
/* 210 */     return chilkatJNI.CkCache_GetExpiration(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkDateTime GetExpirationDt(String var1) {
/* 214 */     long var2 = chilkatJNI.CkCache_GetExpirationDt(this.swigCPtr, this, var1);
/* 215 */     return var2 == 0L ? null : new CkDateTime(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetExpirationStr(String var1, CkString var2) {
/* 219 */     return chilkatJNI.CkCache_GetExpirationStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getExpirationStr(String var1) {
/* 223 */     return chilkatJNI.CkCache_getExpirationStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String expirationStr(String var1) {
/* 227 */     return chilkatJNI.CkCache_expirationStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetFilename(String var1, CkString var2) {
/* 231 */     return chilkatJNI.CkCache_GetFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getFilename(String var1) {
/* 235 */     return chilkatJNI.CkCache_getFilename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String filename(String var1) {
/* 239 */     return chilkatJNI.CkCache_filename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetRoot(int var1, CkString var2) {
/* 243 */     return chilkatJNI.CkCache_GetRoot(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getRoot(int var1) {
/* 247 */     return chilkatJNI.CkCache_getRoot(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String root(int var1) {
/* 251 */     return chilkatJNI.CkCache_root(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsCached(String var1) {
/* 255 */     return chilkatJNI.CkCache_IsCached(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 259 */     return chilkatJNI.CkCache_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveText(String var1, SYSTEMTIME var2, String var3, String var4) {
/* 263 */     return chilkatJNI.CkCache_SaveText(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveTextDt(String var1, CkDateTime var2, String var3, String var4) {
/* 267 */     return chilkatJNI.CkCache_SaveTextDt(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveTextNoExpire(String var1, String var2, String var3) {
/* 271 */     return chilkatJNI.CkCache_SaveTextNoExpire(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean SaveTextStr(String var1, String var2, String var3, String var4) {
/* 275 */     return chilkatJNI.CkCache_SaveTextStr(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveToCache(String var1, SYSTEMTIME var2, String var3, CkByteData var4) {
/* 279 */     return chilkatJNI.CkCache_SaveToCache(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2, var3, CkByteData.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public boolean SaveToCacheDt(String var1, CkDateTime var2, String var3, CkByteData var4) {
/* 283 */     return chilkatJNI.CkCache_SaveToCacheDt(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2, var3, CkByteData.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public boolean SaveToCacheNoExpire(String var1, String var2, CkByteData var3) {
/* 287 */     return chilkatJNI.CkCache_SaveToCacheNoExpire(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean SaveToCacheStr(String var1, String var2, String var3, CkByteData var4) {
/* 291 */     return chilkatJNI.CkCache_SaveToCacheStr(this.swigCPtr, this, var1, var2, var3, CkByteData.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public boolean UpdateExpiration(String var1, SYSTEMTIME var2) {
/* 295 */     return chilkatJNI.CkCache_UpdateExpiration(this.swigCPtr, this, var1, SYSTEMTIME.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateExpirationDt(String var1, CkDateTime var2) {
/* 299 */     return chilkatJNI.CkCache_UpdateExpirationDt(this.swigCPtr, this, var1, CkDateTime.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateExpirationStr(String var1, String var2) {
/* 303 */     return chilkatJNI.CkCache_UpdateExpirationStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */