/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkWebSocket
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkWebSocket(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkWebSocket var0) {
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
/*  29 */         chilkatJNI.delete_CkWebSocket(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkWebSocket()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkWebSocket(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkWebSocket_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkWebSocket_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkWebSocket_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void put_EventCallbackObject(CkBaseProgress var1) {
/*  54 */     chilkatJNI.CkWebSocket_put_EventCallbackObject(this.swigCPtr, this, CkBaseProgress.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_CloseAutoRespond() {
/*  58 */     return chilkatJNI.CkWebSocket_get_CloseAutoRespond(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_CloseAutoRespond(boolean var1) {
/*  62 */     chilkatJNI.CkWebSocket_put_CloseAutoRespond(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_CloseReason(CkString var1) {
/*  66 */     chilkatJNI.CkWebSocket_get_CloseReason(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String closeReason() {
/*  70 */     return chilkatJNI.CkWebSocket_closeReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_CloseReceived() {
/*  74 */     return chilkatJNI.CkWebSocket_get_CloseReceived(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_CloseStatusCode() {
/*  78 */     return chilkatJNI.CkWebSocket_get_CloseStatusCode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  82 */     chilkatJNI.CkWebSocket_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  86 */     return chilkatJNI.CkWebSocket_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  90 */     chilkatJNI.CkWebSocket_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_FinalFrame() {
/*  94 */     return chilkatJNI.CkWebSocket_get_FinalFrame(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_FrameDataLen() {
/*  98 */     return chilkatJNI.CkWebSocket_get_FrameDataLen(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FrameOpcode(CkString var1) {
/* 102 */     chilkatJNI.CkWebSocket_get_FrameOpcode(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String frameOpcode() {
/* 106 */     return chilkatJNI.CkWebSocket_frameOpcode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_FrameOpcodeInt() {
/* 110 */     return chilkatJNI.CkWebSocket_get_FrameOpcodeInt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_IdleTimeoutMs() {
/* 114 */     return chilkatJNI.CkWebSocket_get_IdleTimeoutMs(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IdleTimeoutMs(int var1) {
/* 118 */     chilkatJNI.CkWebSocket_put_IdleTimeoutMs(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsConnected() {
/* 122 */     return chilkatJNI.CkWebSocket_get_IsConnected(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 126 */     chilkatJNI.CkWebSocket_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 130 */     return chilkatJNI.CkWebSocket_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 134 */     chilkatJNI.CkWebSocket_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 138 */     return chilkatJNI.CkWebSocket_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 142 */     chilkatJNI.CkWebSocket_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 146 */     return chilkatJNI.CkWebSocket_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 150 */     return chilkatJNI.CkWebSocket_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 154 */     chilkatJNI.CkWebSocket_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_NeedSendPong() {
/* 158 */     return chilkatJNI.CkWebSocket_get_NeedSendPong(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_PingAutoRespond() {
/* 162 */     return chilkatJNI.CkWebSocket_get_PingAutoRespond(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PingAutoRespond(boolean var1) {
/* 166 */     chilkatJNI.CkWebSocket_put_PingAutoRespond(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PongAutoConsume() {
/* 170 */     return chilkatJNI.CkWebSocket_get_PongAutoConsume(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_PongAutoConsume(boolean var1) {
/* 174 */     chilkatJNI.CkWebSocket_put_PongAutoConsume(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_PongConsumed() {
/* 178 */     return chilkatJNI.CkWebSocket_get_PongConsumed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_ReadFrameFailReason() {
/* 182 */     return chilkatJNI.CkWebSocket_get_ReadFrameFailReason(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 186 */     return chilkatJNI.CkWebSocket_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 190 */     chilkatJNI.CkWebSocket_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 194 */     chilkatJNI.CkWebSocket_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 198 */     return chilkatJNI.CkWebSocket_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AddClientHeaders() {
/* 202 */     return chilkatJNI.CkWebSocket_AddClientHeaders(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean CloseConnection() {
/* 206 */     return chilkatJNI.CkWebSocket_CloseConnection(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetFrameData(CkString var1) {
/* 210 */     return chilkatJNI.CkWebSocket_GetFrameData(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getFrameData() {
/* 214 */     return chilkatJNI.CkWebSocket_getFrameData(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String frameData() {
/* 218 */     return chilkatJNI.CkWebSocket_frameData(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetFrameDataBd(CkBinData var1) {
/* 222 */     return chilkatJNI.CkWebSocket_GetFrameDataBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetFrameDataSb(CkStringBuilder var1) {
/* 226 */     return chilkatJNI.CkWebSocket_GetFrameDataSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean PollDataAvailable() {
/* 230 */     return chilkatJNI.CkWebSocket_PollDataAvailable(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ReadFrame() {
/* 234 */     return chilkatJNI.CkWebSocket_ReadFrame(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask ReadFrameAsync() {
/* 238 */     long var1 = chilkatJNI.CkWebSocket_ReadFrameAsync(this.swigCPtr, this);
/* 239 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 243 */     return chilkatJNI.CkWebSocket_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SendClose(boolean var1, int var2, String var3) {
/* 247 */     return chilkatJNI.CkWebSocket_SendClose(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkTask SendCloseAsync(boolean var1, int var2, String var3) {
/* 251 */     long var4 = chilkatJNI.CkWebSocket_SendCloseAsync(this.swigCPtr, this, var1, var2, var3);
/* 252 */     return var4 == 0L ? null : new CkTask(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SendFrame(String var1, boolean var2) {
/* 256 */     return chilkatJNI.CkWebSocket_SendFrame(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendFrameAsync(String var1, boolean var2) {
/* 260 */     long var3 = chilkatJNI.CkWebSocket_SendFrameAsync(this.swigCPtr, this, var1, var2);
/* 261 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendFrameBd(CkBinData var1, boolean var2) {
/* 265 */     return chilkatJNI.CkWebSocket_SendFrameBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendFrameBdAsync(CkBinData var1, boolean var2) {
/* 269 */     long var3 = chilkatJNI.CkWebSocket_SendFrameBdAsync(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/* 270 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendFrameSb(CkStringBuilder var1, boolean var2) {
/* 274 */     return chilkatJNI.CkWebSocket_SendFrameSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkTask SendFrameSbAsync(CkStringBuilder var1, boolean var2) {
/* 278 */     long var3 = chilkatJNI.CkWebSocket_SendFrameSbAsync(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/* 279 */     return var3 == 0L ? null : new CkTask(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SendPing(String var1) {
/* 283 */     return chilkatJNI.CkWebSocket_SendPing(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkTask SendPingAsync(String var1) {
/* 287 */     long var2 = chilkatJNI.CkWebSocket_SendPingAsync(this.swigCPtr, this, var1);
/* 288 */     return var2 == 0L ? null : new CkTask(var2, true);
/*     */   }
/*     */   
/*     */   public boolean SendPong() {
/* 292 */     return chilkatJNI.CkWebSocket_SendPong(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkTask SendPongAsync() {
/* 296 */     long var1 = chilkatJNI.CkWebSocket_SendPongAsync(this.swigCPtr, this);
/* 297 */     return var1 == 0L ? null : new CkTask(var1, true);
/*     */   }
/*     */   
/*     */   public boolean UseConnection(CkRest var1) {
/* 301 */     return chilkatJNI.CkWebSocket_UseConnection(this.swigCPtr, this, CkRest.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ValidateServerHandshake() {
/* 305 */     return chilkatJNI.CkWebSocket_ValidateServerHandshake(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkWebSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */