/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCompression
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCompression(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCompression var0) {
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
/*  29 */         chilkatJNI.delete_CkCompression(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCompression()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCompression(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkCompression_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkCompression_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkCompression_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkCompression_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_Algorithm(CkString var1) {
/*  58 */     chilkatJNI.CkCompression_get_Algorithm(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String algorithm() {
/*  62 */     return chilkatJNI.CkCompression_algorithm(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Algorithm(String var1) {
/*  66 */     chilkatJNI.CkCompression_put_Algorithm(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Charset(CkString var1) {
/*  70 */     chilkatJNI.CkCompression_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String charset() {
/*  74 */     return chilkatJNI.CkCompression_charset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Charset(String var1) {
/*  78 */     chilkatJNI.CkCompression_put_Charset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  82 */     chilkatJNI.CkCompression_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  86 */     return chilkatJNI.CkCompression_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  90 */     chilkatJNI.CkCompression_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DeflateLevel() {
/*  94 */     return chilkatJNI.CkCompression_get_DeflateLevel(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DeflateLevel(int var1) {
/*  98 */     chilkatJNI.CkCompression_put_DeflateLevel(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_EncodingMode(CkString var1) {
/* 102 */     chilkatJNI.CkCompression_get_EncodingMode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encodingMode() {
/* 106 */     return chilkatJNI.CkCompression_encodingMode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EncodingMode(String var1) {
/* 110 */     chilkatJNI.CkCompression_put_EncodingMode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_HeartbeatMs() {
/* 114 */     return chilkatJNI.CkCompression_get_HeartbeatMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_HeartbeatMs(int var1) {
/* 118 */     chilkatJNI.CkCompression_put_HeartbeatMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 122 */     chilkatJNI.CkCompression_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 126 */     return chilkatJNI.CkCompression_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 130 */     chilkatJNI.CkCompression_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 134 */     return chilkatJNI.CkCompression_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 138 */     chilkatJNI.CkCompression_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 142 */     return chilkatJNI.CkCompression_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 146 */     return chilkatJNI.CkCompression_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 150 */     chilkatJNI.CkCompression_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 154 */     return chilkatJNI.CkCompression_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 158 */     chilkatJNI.CkCompression_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 162 */     chilkatJNI.CkCompression_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 166 */     return chilkatJNI.CkCompression_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean BeginCompressBytes(CkByteData var1, CkByteData var2) {
/* 170 */     return chilkatJNI.CkCompression_BeginCompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask BeginCompressBytesAsync(CkByteData var1) {
/* 174 */     long var2 = chilkatJNI.CkCompression_BeginCompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 175 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginCompressBytesENC(CkByteData var1, CkString var2) {
/* 179 */     return chilkatJNI.CkCompression_BeginCompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String beginCompressBytesENC(CkByteData var1) {
/* 183 */     return chilkatJNI.CkCompression_beginCompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask BeginCompressBytesENCAsync(CkByteData var1) {
/* 187 */     long var2 = chilkatJNI.CkCompression_BeginCompressBytesENCAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 188 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginCompressString(String var1, CkByteData var2) {
/* 192 */     return chilkatJNI.CkCompression_BeginCompressString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask BeginCompressStringAsync(String var1) {
/* 196 */     long var2 = chilkatJNI.CkCompression_BeginCompressStringAsync(this.swigCPtr, this, var1);
/* 197 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginCompressStringENC(String var1, CkString var2) {
/* 201 */     return chilkatJNI.CkCompression_BeginCompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String beginCompressStringENC(String var1) {
/* 205 */     return chilkatJNI.CkCompression_beginCompressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask BeginCompressStringENCAsync(String var1) {
/* 209 */     long var2 = chilkatJNI.CkCompression_BeginCompressStringENCAsync(this.swigCPtr, this, var1);
/* 210 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginDecompressBytes(CkByteData var1, CkByteData var2) {
/* 214 */     return chilkatJNI.CkCompression_BeginDecompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask BeginDecompressBytesAsync(CkByteData var1) {
/* 218 */     long var2 = chilkatJNI.CkCompression_BeginDecompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 219 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginDecompressBytesENC(String var1, CkByteData var2) {
/* 223 */     return chilkatJNI.CkCompression_BeginDecompressBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask BeginDecompressBytesENCAsync(String var1) {
/* 227 */     long var2 = chilkatJNI.CkCompression_BeginDecompressBytesENCAsync(this.swigCPtr, this, var1);
/* 228 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginDecompressString(CkByteData var1, CkString var2) {
/* 232 */     return chilkatJNI.CkCompression_BeginDecompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String beginDecompressString(CkByteData var1) {
/* 236 */     return chilkatJNI.CkCompression_beginDecompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask BeginDecompressStringAsync(CkByteData var1) {
/* 240 */     long var2 = chilkatJNI.CkCompression_BeginDecompressStringAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 241 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean BeginDecompressStringENC(String var1, CkString var2) {
/* 245 */     return chilkatJNI.CkCompression_BeginDecompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String beginDecompressStringENC(String var1) {
/* 249 */     return chilkatJNI.CkCompression_beginDecompressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask BeginDecompressStringENCAsync(String var1) {
/* 253 */     long var2 = chilkatJNI.CkCompression_BeginDecompressStringENCAsync(this.swigCPtr, this, var1);
/* 254 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressBd(CkBinData var1) {
/* 258 */     return chilkatJNI.CkCompression_CompressBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask CompressBdAsync(CkBinData var1) {
/* 262 */     long var2 = chilkatJNI.CkCompression_CompressBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 263 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressBytes(CkByteData var1, CkByteData var2) {
/* 267 */     return chilkatJNI.CkCompression_CompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressBytesAsync(CkByteData var1) {
/* 271 */     long var2 = chilkatJNI.CkCompression_CompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 272 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressBytesENC(CkByteData var1, CkString var2) {
/* 276 */     return chilkatJNI.CkCompression_CompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String compressBytesENC(CkByteData var1) {
/* 280 */     return chilkatJNI.CkCompression_compressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask CompressBytesENCAsync(CkByteData var1) {
/* 284 */     long var2 = chilkatJNI.CkCompression_CompressBytesENCAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 285 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressFile(String var1, String var2) {
/* 289 */     return chilkatJNI.CkCompression_CompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressFileAsync(String var1, String var2) {
/* 293 */     long var3 = chilkatJNI.CkCompression_CompressFileAsync(this.swigCPtr, this, var1, var2);
/* 294 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressSb(CkStringBuilder var1, CkBinData var2) {
/* 298 */     return chilkatJNI.CkCompression_CompressSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressSbAsync(CkStringBuilder var1, CkBinData var2) {
/* 302 */     long var3 = chilkatJNI.CkCompression_CompressSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, CkBinData.getCPtr(var2), var2);
/* 303 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean CompressStream(CkStream var1) {
/* 307 */     return chilkatJNI.CkCompression_CompressStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask CompressStreamAsync(CkStream var1) {
/* 311 */     long var2 = chilkatJNI.CkCompression_CompressStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/* 312 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressString(String var1, CkByteData var2) {
/* 316 */     return chilkatJNI.CkCompression_CompressString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask CompressStringAsync(String var1) {
/* 320 */     long var2 = chilkatJNI.CkCompression_CompressStringAsync(this.swigCPtr, this, var1);
/* 321 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean CompressStringENC(String var1, CkString var2) {
/* 325 */     return chilkatJNI.CkCompression_CompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String compressStringENC(String var1) {
/* 329 */     return chilkatJNI.CkCompression_compressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask CompressStringENCAsync(String var1) {
/* 333 */     long var2 = chilkatJNI.CkCompression_CompressStringENCAsync(this.swigCPtr, this, var1);
/* 334 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressBd(CkBinData var1) {
/* 338 */     return chilkatJNI.CkCompression_DecompressBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask DecompressBdAsync(CkBinData var1) {
/* 342 */     long var2 = chilkatJNI.CkCompression_DecompressBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 343 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressBytes(CkByteData var1, CkByteData var2) {
/* 347 */     return chilkatJNI.CkCompression_DecompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask DecompressBytesAsync(CkByteData var1) {
/* 351 */     long var2 = chilkatJNI.CkCompression_DecompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 352 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressBytesENC(String var1, CkByteData var2) {
/* 356 */     return chilkatJNI.CkCompression_DecompressBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask DecompressBytesENCAsync(String var1) {
/* 360 */     long var2 = chilkatJNI.CkCompression_DecompressBytesENCAsync(this.swigCPtr, this, var1);
/* 361 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressFile(String var1, String var2) {
/* 365 */     return chilkatJNI.CkCompression_DecompressFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask DecompressFileAsync(String var1, String var2) {
/* 369 */     long var3 = chilkatJNI.CkCompression_DecompressFileAsync(this.swigCPtr, this, var1, var2);
/* 370 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressSb(CkBinData var1, CkStringBuilder var2) {
/* 374 */     return chilkatJNI.CkCompression_DecompressSb(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask DecompressSbAsync(CkBinData var1, CkStringBuilder var2) {
/* 378 */     long var3 = chilkatJNI.CkCompression_DecompressSbAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, CkStringBuilder.getCPtr(var2), var2);
/* 379 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressStream(CkStream var1) {
/* 383 */     return chilkatJNI.CkCompression_DecompressStream(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask DecompressStreamAsync(CkStream var1) {
/* 387 */     long var2 = chilkatJNI.CkCompression_DecompressStreamAsync(this.swigCPtr, this, CkStream.getCPtr(var1), var1);
/* 388 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressString(CkByteData var1, CkString var2) {
/* 392 */     return chilkatJNI.CkCompression_DecompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String decompressString(CkByteData var1) {
/* 396 */     return chilkatJNI.CkCompression_decompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask DecompressStringAsync(CkByteData var1) {
/* 400 */     long var2 = chilkatJNI.CkCompression_DecompressStringAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 401 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean DecompressStringENC(String var1, CkString var2) {
/* 405 */     return chilkatJNI.CkCompression_DecompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String decompressStringENC(String var1) {
/* 409 */     return chilkatJNI.CkCompression_decompressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask DecompressStringENCAsync(String var1) {
/* 413 */     long var2 = chilkatJNI.CkCompression_DecompressStringENCAsync(this.swigCPtr, this, var1);
/* 414 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean EndCompressBytes(CkByteData var1) {
/* 418 */     return chilkatJNI.CkCompression_EndCompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask EndCompressBytesAsync() {
/* 422 */     long var1 = chilkatJNI.CkCompression_EndCompressBytesAsync(this.swigCPtr, this);
/* 423 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndCompressBytesENC(CkString var1) {
/* 427 */     return chilkatJNI.CkCompression_EndCompressBytesENC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String endCompressBytesENC() {
/* 431 */     return chilkatJNI.CkCompression_endCompressBytesENC(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask EndCompressBytesENCAsync() {
/* 435 */     long var1 = chilkatJNI.CkCompression_EndCompressBytesENCAsync(this.swigCPtr, this);
/* 436 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndCompressString(CkByteData var1) {
/* 440 */     return chilkatJNI.CkCompression_EndCompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask EndCompressStringAsync() {
/* 444 */     long var1 = chilkatJNI.CkCompression_EndCompressStringAsync(this.swigCPtr, this);
/* 445 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndCompressStringENC(CkString var1) {
/* 449 */     return chilkatJNI.CkCompression_EndCompressStringENC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String endCompressStringENC() {
/* 453 */     return chilkatJNI.CkCompression_endCompressStringENC(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask EndCompressStringENCAsync() {
/* 457 */     long var1 = chilkatJNI.CkCompression_EndCompressStringENCAsync(this.swigCPtr, this);
/* 458 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndDecompressBytes(CkByteData var1) {
/* 462 */     return chilkatJNI.CkCompression_EndDecompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask EndDecompressBytesAsync() {
/* 466 */     long var1 = chilkatJNI.CkCompression_EndDecompressBytesAsync(this.swigCPtr, this);
/* 467 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndDecompressBytesENC(CkByteData var1) {
/* 471 */     return chilkatJNI.CkCompression_EndDecompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask EndDecompressBytesENCAsync() {
/* 475 */     long var1 = chilkatJNI.CkCompression_EndDecompressBytesENCAsync(this.swigCPtr, this);
/* 476 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndDecompressString(CkString var1) {
/* 480 */     return chilkatJNI.CkCompression_EndDecompressString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String endDecompressString() {
/* 484 */     return chilkatJNI.CkCompression_endDecompressString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask EndDecompressStringAsync() {
/* 488 */     long var1 = chilkatJNI.CkCompression_EndDecompressStringAsync(this.swigCPtr, this);
/* 489 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean EndDecompressStringENC(CkString var1) {
/* 493 */     return chilkatJNI.CkCompression_EndDecompressStringENC(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String endDecompressStringENC() {
/* 497 */     return chilkatJNI.CkCompression_endDecompressStringENC(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask EndDecompressStringENCAsync() {
/* 501 */     long var1 = chilkatJNI.CkCompression_EndDecompressStringENCAsync(this.swigCPtr, this);
/* 502 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean MoreCompressBytes(CkByteData var1, CkByteData var2) {
/* 506 */     return chilkatJNI.CkCompression_MoreCompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask MoreCompressBytesAsync(CkByteData var1) {
/* 510 */     long var2 = chilkatJNI.CkCompression_MoreCompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 511 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreCompressBytesENC(CkByteData var1, CkString var2) {
/* 515 */     return chilkatJNI.CkCompression_MoreCompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String moreCompressBytesENC(CkByteData var1) {
/* 519 */     return chilkatJNI.CkCompression_moreCompressBytesENC(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask MoreCompressBytesENCAsync(CkByteData var1) {
/* 523 */     long var2 = chilkatJNI.CkCompression_MoreCompressBytesENCAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 524 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreCompressString(String var1, CkByteData var2) {
/* 528 */     return chilkatJNI.CkCompression_MoreCompressString(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask MoreCompressStringAsync(String var1) {
/* 532 */     long var2 = chilkatJNI.CkCompression_MoreCompressStringAsync(this.swigCPtr, this, var1);
/* 533 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreCompressStringENC(String var1, CkString var2) {
/* 537 */     return chilkatJNI.CkCompression_MoreCompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String moreCompressStringENC(String var1) {
/* 541 */     return chilkatJNI.CkCompression_moreCompressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask MoreCompressStringENCAsync(String var1) {
/* 545 */     long var2 = chilkatJNI.CkCompression_MoreCompressStringENCAsync(this.swigCPtr, this, var1);
/* 546 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreDecompressBytes(CkByteData var1, CkByteData var2) {
/* 550 */     return chilkatJNI.CkCompression_MoreDecompressBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask MoreDecompressBytesAsync(CkByteData var1) {
/* 554 */     long var2 = chilkatJNI.CkCompression_MoreDecompressBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 555 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreDecompressBytesENC(String var1, CkByteData var2) {
/* 559 */     return chilkatJNI.CkCompression_MoreDecompressBytesENC(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask MoreDecompressBytesENCAsync(String var1) {
/* 563 */     long var2 = chilkatJNI.CkCompression_MoreDecompressBytesENCAsync(this.swigCPtr, this, var1);
/* 564 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreDecompressString(CkByteData var1, CkString var2) {
/* 568 */     return chilkatJNI.CkCompression_MoreDecompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String moreDecompressString(CkByteData var1) {
/* 572 */     return chilkatJNI.CkCompression_moreDecompressString(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask MoreDecompressStringAsync(CkByteData var1) {
/* 576 */     long var2 = chilkatJNI.CkCompression_MoreDecompressStringAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 577 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean MoreDecompressStringENC(String var1, CkString var2) {
/* 581 */     return chilkatJNI.CkCompression_MoreDecompressStringENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String moreDecompressStringENC(String var1) {
/* 585 */     return chilkatJNI.CkCompression_moreDecompressStringENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask MoreDecompressStringENCAsync(String var1) {
/* 589 */     long var2 = chilkatJNI.CkCompression_MoreDecompressStringENCAsync(this.swigCPtr, this, var1);
/* 590 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 594 */     return chilkatJNI.CkCompression_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 598 */     return chilkatJNI.CkCompression_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCompression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */