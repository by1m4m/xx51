/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkAsn
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkAsn(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkAsn var0) {
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
/*  29 */         chilkatJNI.delete_CkAsn(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkAsn()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkAsn(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkAsn_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkAsn_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkAsn_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_BoolValue() {
/*  54 */     return chilkatJNI.CkAsn_get_BoolValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_BoolValue(boolean var1) {
/*  58 */     chilkatJNI.CkAsn_put_BoolValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Constructed() {
/*  62 */     return chilkatJNI.CkAsn_get_Constructed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ContentStr(CkString var1) {
/*  66 */     chilkatJNI.CkAsn_get_ContentStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String contentStr() {
/*  70 */     return chilkatJNI.CkAsn_contentStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ContentStr(String var1) {
/*  74 */     chilkatJNI.CkAsn_put_ContentStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  78 */     chilkatJNI.CkAsn_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  82 */     return chilkatJNI.CkAsn_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  86 */     chilkatJNI.CkAsn_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_IntValue() {
/*  90 */     return chilkatJNI.CkAsn_get_IntValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_IntValue(int var1) {
/*  94 */     chilkatJNI.CkAsn_put_IntValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  98 */     chilkatJNI.CkAsn_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 102 */     return chilkatJNI.CkAsn_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 106 */     chilkatJNI.CkAsn_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 110 */     return chilkatJNI.CkAsn_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 114 */     chilkatJNI.CkAsn_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 118 */     return chilkatJNI.CkAsn_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 122 */     return chilkatJNI.CkAsn_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 126 */     chilkatJNI.CkAsn_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumSubItems() {
/* 130 */     return chilkatJNI.CkAsn_get_NumSubItems(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Tag(CkString var1) {
/* 134 */     chilkatJNI.CkAsn_get_Tag(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tag() {
/* 138 */     return chilkatJNI.CkAsn_tag(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_TagValue() {
/* 142 */     return chilkatJNI.CkAsn_get_TagValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 146 */     return chilkatJNI.CkAsn_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 150 */     chilkatJNI.CkAsn_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 154 */     chilkatJNI.CkAsn_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 158 */     return chilkatJNI.CkAsn_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendBigInt(String var1, String var2) {
/* 162 */     return chilkatJNI.CkAsn_AppendBigInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendBits(String var1, String var2) {
/* 166 */     return chilkatJNI.CkAsn_AppendBits(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendBool(boolean var1) {
/* 170 */     return chilkatJNI.CkAsn_AppendBool(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendContextConstructed(int var1) {
/* 174 */     return chilkatJNI.CkAsn_AppendContextConstructed(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendContextPrimitive(int var1, String var2, String var3) {
/* 178 */     return chilkatJNI.CkAsn_AppendContextPrimitive(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean AppendInt(int var1) {
/* 182 */     return chilkatJNI.CkAsn_AppendInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendNull() {
/* 186 */     return chilkatJNI.CkAsn_AppendNull(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendOctets(String var1, String var2) {
/* 190 */     return chilkatJNI.CkAsn_AppendOctets(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendOid(String var1) {
/* 194 */     return chilkatJNI.CkAsn_AppendOid(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendSequence() {
/* 198 */     return chilkatJNI.CkAsn_AppendSequence(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendSequence2() {
/* 202 */     return chilkatJNI.CkAsn_AppendSequence2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkAsn AppendSequenceR() {
/* 206 */     long var1 = chilkatJNI.CkAsn_AppendSequenceR(this.swigCPtr, this);
/* 207 */     return var1 == 0L ? null : new CkAsn(var1, true);
/*     */   }
/*     */   
/*     */   public boolean AppendSet() {
/* 211 */     return chilkatJNI.CkAsn_AppendSet(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendSet2() {
/* 215 */     return chilkatJNI.CkAsn_AppendSet2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkAsn AppendSetR() {
/* 219 */     long var1 = chilkatJNI.CkAsn_AppendSetR(this.swigCPtr, this);
/* 220 */     return var1 == 0L ? null : new CkAsn(var1, true);
/*     */   }
/*     */   
/*     */   public boolean AppendString(String var1, String var2) {
/* 224 */     return chilkatJNI.CkAsn_AppendString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendTime(String var1, String var2) {
/* 228 */     return chilkatJNI.CkAsn_AppendTime(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AsnToXml(CkString var1) {
/* 232 */     return chilkatJNI.CkAsn_AsnToXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String asnToXml() {
/* 236 */     return chilkatJNI.CkAsn_asnToXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean DeleteSubItem(int var1) {
/* 240 */     return chilkatJNI.CkAsn_DeleteSubItem(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetBinaryDer(CkByteData var1) {
/* 244 */     return chilkatJNI.CkAsn_GetBinaryDer(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedContent(String var1, CkString var2) {
/* 248 */     return chilkatJNI.CkAsn_GetEncodedContent(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEncodedContent(String var1) {
/* 252 */     return chilkatJNI.CkAsn_getEncodedContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String encodedContent(String var1) {
/* 256 */     return chilkatJNI.CkAsn_encodedContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedDer(String var1, CkString var2) {
/* 260 */     return chilkatJNI.CkAsn_GetEncodedDer(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEncodedDer(String var1) {
/* 264 */     return chilkatJNI.CkAsn_getEncodedDer(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String encodedDer(String var1) {
/* 268 */     return chilkatJNI.CkAsn_encodedDer(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkAsn GetLastSubItem() {
/* 272 */     long var1 = chilkatJNI.CkAsn_GetLastSubItem(this.swigCPtr, this);
/* 273 */     return var1 == 0L ? null : new CkAsn(var1, true);
/*     */   }
/*     */   
/*     */   public CkAsn GetSubItem(int var1) {
/* 277 */     long var2 = chilkatJNI.CkAsn_GetSubItem(this.swigCPtr, this, var1);
/* 278 */     return var2 == 0L ? null : new CkAsn(var2, true);
/*     */   }
/*     */   
/*     */   public boolean LoadAsnXml(String var1) {
/* 282 */     return chilkatJNI.CkAsn_LoadAsnXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBinary(CkByteData var1) {
/* 286 */     return chilkatJNI.CkAsn_LoadBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBinaryFile(String var1) {
/* 290 */     return chilkatJNI.CkAsn_LoadBinaryFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadEncoded(String var1, String var2) {
/* 294 */     return chilkatJNI.CkAsn_LoadEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 298 */     return chilkatJNI.CkAsn_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetEncodedContent(String var1, String var2) {
/* 302 */     return chilkatJNI.CkAsn_SetEncodedContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean WriteBinaryDer(String var1) {
/* 306 */     return chilkatJNI.CkAsn_WriteBinaryDer(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkAsn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */