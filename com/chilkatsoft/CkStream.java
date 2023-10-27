/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkStream
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkStream(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkStream var0) {
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
/*  29 */         chilkatJNI.delete_CkStream(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkStream()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkStream(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkStream_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkStream_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkStream_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkStream_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_AbortCurrent() {
/*  58 */     return chilkatJNI.CkStream_get_AbortCurrent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AbortCurrent(boolean var1) {
/*  62 */     chilkatJNI.CkStream_put_AbortCurrent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_CanRead() {
/*  66 */     return chilkatJNI.CkStream_get_CanRead(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_CanWrite() {
/*  70 */     return chilkatJNI.CkStream_get_CanWrite(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_DataAvailable() {
/*  74 */     return chilkatJNI.CkStream_get_DataAvailable(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkStream_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkStream_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkStream_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_DefaultChunkSize() {
/*  90 */     return chilkatJNI.CkStream_get_DefaultChunkSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DefaultChunkSize(int var1) {
/*  94 */     chilkatJNI.CkStream_put_DefaultChunkSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EndOfStream() {
/*  98 */     return chilkatJNI.CkStream_get_EndOfStream(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsWriteClosed() {
/* 102 */     return chilkatJNI.CkStream_get_IsWriteClosed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 106 */     chilkatJNI.CkStream_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 110 */     return chilkatJNI.CkStream_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 114 */     chilkatJNI.CkStream_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 118 */     return chilkatJNI.CkStream_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 122 */     chilkatJNI.CkStream_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 126 */     return chilkatJNI.CkStream_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 130 */     return chilkatJNI.CkStream_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 134 */     chilkatJNI.CkStream_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Length32() {
/* 138 */     return chilkatJNI.CkStream_get_Length32(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Length32(int var1) {
/* 142 */     chilkatJNI.CkStream_put_Length32(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ReadFailReason() {
/* 146 */     return chilkatJNI.CkStream_get_ReadFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ReadTimeoutMs() {
/* 150 */     return chilkatJNI.CkStream_get_ReadTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadTimeoutMs(int var1) {
/* 154 */     chilkatJNI.CkStream_put_ReadTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SinkFile(CkString var1) {
/* 158 */     chilkatJNI.CkStream_get_SinkFile(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sinkFile() {
/* 162 */     return chilkatJNI.CkStream_sinkFile(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SinkFile(String var1) {
/* 166 */     chilkatJNI.CkStream_put_SinkFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_SourceFile(CkString var1) {
/* 170 */     chilkatJNI.CkStream_get_SourceFile(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sourceFile() {
/* 174 */     return chilkatJNI.CkStream_sourceFile(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SourceFile(String var1) {
/* 178 */     chilkatJNI.CkStream_put_SourceFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SourceFilePart() {
/* 182 */     return chilkatJNI.CkStream_get_SourceFilePart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SourceFilePart(int var1) {
/* 186 */     chilkatJNI.CkStream_put_SourceFilePart(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_SourceFilePartSize() {
/* 190 */     return chilkatJNI.CkStream_get_SourceFilePartSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SourceFilePartSize(int var1) {
/* 194 */     chilkatJNI.CkStream_put_SourceFilePartSize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_StringBom() {
/* 198 */     return chilkatJNI.CkStream_get_StringBom(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StringBom(boolean var1) {
/* 202 */     chilkatJNI.CkStream_put_StringBom(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_StringCharset(CkString var1) {
/* 206 */     chilkatJNI.CkStream_get_StringCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String stringCharset() {
/* 210 */     return chilkatJNI.CkStream_stringCharset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StringCharset(String var1) {
/* 214 */     chilkatJNI.CkStream_put_StringCharset(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 218 */     return chilkatJNI.CkStream_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 222 */     chilkatJNI.CkStream_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 226 */     chilkatJNI.CkStream_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 230 */     return chilkatJNI.CkStream_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_WriteFailReason() {
/* 234 */     return chilkatJNI.CkStream_get_WriteFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_WriteTimeoutMs() {
/* 238 */     return chilkatJNI.CkStream_get_WriteTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_WriteTimeoutMs(int var1) {
/* 242 */     chilkatJNI.CkStream_put_WriteTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ReadBd(CkBinData var1) {
/* 246 */     return chilkatJNI.CkStream_ReadBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadBdAsync(CkBinData var1) {
/* 250 */     long var2 = chilkatJNI.CkStream_ReadBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 251 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReadBytes(CkByteData var1) {
/* 255 */     return chilkatJNI.CkStream_ReadBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadBytesAsync() {
/* 259 */     long var1 = chilkatJNI.CkStream_ReadBytesAsync(this.swigCPtr, this);
/* 260 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReadBytesENC(String var1, CkString var2) {
/* 264 */     return chilkatJNI.CkStream_ReadBytesENC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String readBytesENC(String var1) {
/* 268 */     return chilkatJNI.CkStream_readBytesENC(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadBytesENCAsync(String var1) {
/* 272 */     long var2 = chilkatJNI.CkStream_ReadBytesENCAsync(this.swigCPtr, this, var1);
/* 273 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReadNBytes(int var1, CkByteData var2) {
/* 277 */     return chilkatJNI.CkStream_ReadNBytes(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkTask ReadNBytesAsync(int var1) {
/* 281 */     long var2 = chilkatJNI.CkStream_ReadNBytesAsync(this.swigCPtr, this, var1);
/* 282 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReadNBytesENC(int var1, String var2, CkString var3) {
/* 286 */     return chilkatJNI.CkStream_ReadNBytesENC(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String readNBytesENC(int var1, String var2) {
/* 290 */     return chilkatJNI.CkStream_readNBytesENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask ReadNBytesENCAsync(int var1, String var2) {
/* 294 */     long var3 = chilkatJNI.CkStream_ReadNBytesENCAsync(this.swigCPtr, this, var1, var2);
/* 295 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean ReadSb(CkStringBuilder var1) {
/* 299 */     return chilkatJNI.CkStream_ReadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadSbAsync(CkStringBuilder var1) {
/* 303 */     long var2 = chilkatJNI.CkStream_ReadSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/* 304 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ReadString(CkString var1) {
/* 308 */     return chilkatJNI.CkStream_ReadString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String readString() {
/* 312 */     return chilkatJNI.CkStream_readString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReadStringAsync() {
/* 316 */     long var1 = chilkatJNI.CkStream_ReadStringAsync(this.swigCPtr, this);
/* 317 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReadToCRLF(CkString var1) {
/* 321 */     return chilkatJNI.CkStream_ReadToCRLF(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String readToCRLF() {
/* 325 */     return chilkatJNI.CkStream_readToCRLF(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReadToCRLFAsync() {
/* 329 */     long var1 = chilkatJNI.CkStream_ReadToCRLFAsync(this.swigCPtr, this);
/* 330 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean ReadUntilMatch(String var1, CkString var2) {
/* 334 */     return chilkatJNI.CkStream_ReadUntilMatch(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String readUntilMatch(String var1) {
/* 338 */     return chilkatJNI.CkStream_readUntilMatch(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask ReadUntilMatchAsync(String var1) {
/* 342 */     long var2 = chilkatJNI.CkStream_ReadUntilMatchAsync(this.swigCPtr, this, var1);
/* 343 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public void Reset() {
/* 347 */     chilkatJNI.CkStream_Reset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RunStream() {
/* 351 */     return chilkatJNI.CkStream_RunStream(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask RunStreamAsync() {
/* 355 */     long var1 = chilkatJNI.CkStream_RunStreamAsync(this.swigCPtr, this);
/* 356 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 360 */     return chilkatJNI.CkStream_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetSinkStream(CkStream var1) {
/* 364 */     return chilkatJNI.CkStream_SetSinkStream(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetSourceBytes(CkByteData var1) {
/* 368 */     return chilkatJNI.CkStream_SetSourceBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetSourceStream(CkStream var1) {
/* 372 */     return chilkatJNI.CkStream_SetSourceStream(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetSourceString(String var1, String var2) {
/* 376 */     return chilkatJNI.CkStream_SetSourceString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean WriteBd(CkBinData var1) {
/* 380 */     return chilkatJNI.CkStream_WriteBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteBdAsync(CkBinData var1) {
/* 384 */     long var2 = chilkatJNI.CkStream_WriteBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/* 385 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteByte(int var1) {
/* 389 */     return chilkatJNI.CkStream_WriteByte(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteByteAsync(int var1) {
/* 393 */     long var2 = chilkatJNI.CkStream_WriteByteAsync(this.swigCPtr, this, var1);
/* 394 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteBytes(CkByteData var1) {
/* 398 */     return chilkatJNI.CkStream_WriteBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteBytesAsync(CkByteData var1) {
/* 402 */     long var2 = chilkatJNI.CkStream_WriteBytesAsync(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/* 403 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteBytesENC(String var1, String var2) {
/* 407 */     return chilkatJNI.CkStream_WriteBytesENC(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask WriteBytesENCAsync(String var1, String var2) {
/* 411 */     long var3 = chilkatJNI.CkStream_WriteBytesENCAsync(this.swigCPtr, this, var1, var2);
/* 412 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean WriteClose() {
/* 416 */     return chilkatJNI.CkStream_WriteClose(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean WriteSb(CkStringBuilder var1) {
/* 420 */     return chilkatJNI.CkStream_WriteSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteSbAsync(CkStringBuilder var1) {
/* 424 */     long var2 = chilkatJNI.CkStream_WriteSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/* 425 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean WriteString(String var1) {
/* 429 */     return chilkatJNI.CkStream_WriteString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask WriteStringAsync(String var1) {
/* 433 */     long var2 = chilkatJNI.CkStream_WriteStringAsync(this.swigCPtr, this, var1);
/* 434 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */