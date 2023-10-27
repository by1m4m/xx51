/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkXml
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkXml(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkXml var0) {
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
/*  29 */         chilkatJNI.delete_CkXml(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkXml()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkXml(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkXml_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkXml_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkXml_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_Cdata() {
/*  54 */     return chilkatJNI.CkXml_get_Cdata(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Cdata(boolean var1) {
/*  58 */     chilkatJNI.CkXml_put_Cdata(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Content(CkString var1) {
/*  62 */     chilkatJNI.CkXml_get_Content(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String content() {
/*  66 */     return chilkatJNI.CkXml_content(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Content(String var1) {
/*  70 */     chilkatJNI.CkXml_put_Content(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_ContentInt() {
/*  74 */     return chilkatJNI.CkXml_get_ContentInt(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ContentInt(int var1) {
/*  78 */     chilkatJNI.CkXml_put_ContentInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  82 */     chilkatJNI.CkXml_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  86 */     return chilkatJNI.CkXml_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  90 */     chilkatJNI.CkXml_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DocType(CkString var1) {
/*  94 */     chilkatJNI.CkXml_get_DocType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String docType() {
/*  98 */     return chilkatJNI.CkXml_docType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DocType(String var1) {
/* 102 */     chilkatJNI.CkXml_put_DocType(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitBom() {
/* 106 */     return chilkatJNI.CkXml_get_EmitBom(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitBom(boolean var1) {
/* 110 */     chilkatJNI.CkXml_put_EmitBom(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitCompact() {
/* 114 */     return chilkatJNI.CkXml_get_EmitCompact(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitCompact(boolean var1) {
/* 118 */     chilkatJNI.CkXml_put_EmitCompact(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_EmitXmlDecl() {
/* 122 */     return chilkatJNI.CkXml_get_EmitXmlDecl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_EmitXmlDecl(boolean var1) {
/* 126 */     chilkatJNI.CkXml_put_EmitXmlDecl(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Encoding(CkString var1) {
/* 130 */     chilkatJNI.CkXml_get_Encoding(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String encoding() {
/* 134 */     return chilkatJNI.CkXml_encoding(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Encoding(String var1) {
/* 138 */     chilkatJNI.CkXml_put_Encoding(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_I() {
/* 142 */     return chilkatJNI.CkXml_get_I(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_I(int var1) {
/* 146 */     chilkatJNI.CkXml_put_I(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_J() {
/* 150 */     return chilkatJNI.CkXml_get_J(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_J(int var1) {
/* 154 */     chilkatJNI.CkXml_put_J(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_K() {
/* 158 */     return chilkatJNI.CkXml_get_K(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_K(int var1) {
/* 162 */     chilkatJNI.CkXml_put_K(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/* 166 */     chilkatJNI.CkXml_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 170 */     return chilkatJNI.CkXml_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/* 174 */     chilkatJNI.CkXml_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 178 */     return chilkatJNI.CkXml_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 182 */     chilkatJNI.CkXml_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 186 */     return chilkatJNI.CkXml_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 190 */     return chilkatJNI.CkXml_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 194 */     chilkatJNI.CkXml_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumAttributes() {
/* 198 */     return chilkatJNI.CkXml_get_NumAttributes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_NumChildren() {
/* 202 */     return chilkatJNI.CkXml_get_NumChildren(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_SortCaseInsensitive() {
/* 206 */     return chilkatJNI.CkXml_get_SortCaseInsensitive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_SortCaseInsensitive(boolean var1) {
/* 210 */     chilkatJNI.CkXml_put_SortCaseInsensitive(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_Standalone() {
/* 214 */     return chilkatJNI.CkXml_get_Standalone(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Standalone(boolean var1) {
/* 218 */     chilkatJNI.CkXml_put_Standalone(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Tag(CkString var1) {
/* 222 */     chilkatJNI.CkXml_get_Tag(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String tag() {
/* 226 */     return chilkatJNI.CkXml_tag(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Tag(String var1) {
/* 230 */     chilkatJNI.CkXml_put_Tag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_TreeId() {
/* 234 */     return chilkatJNI.CkXml_get_TreeId(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 238 */     return chilkatJNI.CkXml_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 242 */     chilkatJNI.CkXml_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 246 */     chilkatJNI.CkXml_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 250 */     return chilkatJNI.CkXml_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AccumulateTagContent(String var1, String var2, CkString var3) {
/* 254 */     return chilkatJNI.CkXml_AccumulateTagContent(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String accumulateTagContent(String var1, String var2) {
/* 258 */     return chilkatJNI.CkXml_accumulateTagContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddAttribute(String var1, String var2) {
/* 262 */     return chilkatJNI.CkXml_AddAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddAttributeInt(String var1, int var2) {
/* 266 */     return chilkatJNI.CkXml_AddAttributeInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddChildTree(CkXml var1) {
/* 270 */     return chilkatJNI.CkXml_AddChildTree(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void AddOrUpdateAttribute(String var1, String var2) {
/* 274 */     chilkatJNI.CkXml_AddOrUpdateAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddOrUpdateAttributeI(String var1, int var2) {
/* 278 */     chilkatJNI.CkXml_AddOrUpdateAttributeI(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddStyleSheet(String var1) {
/* 282 */     chilkatJNI.CkXml_AddStyleSheet(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void AddToAttribute(String var1, int var2) {
/* 286 */     chilkatJNI.CkXml_AddToAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddToChildContent(String var1, int var2) {
/* 290 */     chilkatJNI.CkXml_AddToChildContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void AddToContent(int var1) {
/* 294 */     chilkatJNI.CkXml_AddToContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendToContent(String var1) {
/* 298 */     return chilkatJNI.CkXml_AppendToContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean BEncodeContent(String var1, CkByteData var2) {
/* 302 */     return chilkatJNI.CkXml_BEncodeContent(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean ChildContentMatches(String var1, String var2, boolean var3) {
/* 306 */     return chilkatJNI.CkXml_ChildContentMatches(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean ChilkatPath(String var1, CkString var2) {
/* 310 */     return chilkatJNI.CkXml_ChilkatPath(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String chilkatPath(String var1) {
/* 314 */     return chilkatJNI.CkXml_chilkatPath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void Clear() {
/* 318 */     chilkatJNI.CkXml_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ContentMatches(String var1, boolean var2) {
/* 322 */     return chilkatJNI.CkXml_ContentMatches(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void Copy(CkXml var1) {
/* 326 */     chilkatJNI.CkXml_Copy(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void CopyRef(CkXml var1) {
/* 330 */     chilkatJNI.CkXml_CopyRef(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DecodeContent(CkByteData var1) {
/* 334 */     return chilkatJNI.CkXml_DecodeContent(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean DecodeEntities(String var1, CkString var2) {
/* 338 */     return chilkatJNI.CkXml_DecodeEntities(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String decodeEntities(String var1) {
/* 342 */     return chilkatJNI.CkXml_decodeEntities(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean DecryptContent(String var1) {
/* 346 */     return chilkatJNI.CkXml_DecryptContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean EncryptContent(String var1) {
/* 350 */     return chilkatJNI.CkXml_EncryptContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml ExtractChildByIndex(int var1) {
/* 354 */     long var2 = chilkatJNI.CkXml_ExtractChildByIndex(this.swigCPtr, this, var1);
/* 355 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public CkXml ExtractChildByName(String var1, String var2, String var3) {
/* 359 */     long var4 = chilkatJNI.CkXml_ExtractChildByName(this.swigCPtr, this, var1, var2, var3);
/* 360 */     return var4 == 0L ? null : new CkXml(var4, true);
/*     */   }
/*     */   
/*     */   public CkXml FindChild(String var1) {
/* 364 */     long var2 = chilkatJNI.CkXml_FindChild(this.swigCPtr, this, var1);
/* 365 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public boolean FindChild2(String var1) {
/* 369 */     return chilkatJNI.CkXml_FindChild2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml FindNextRecord(String var1, String var2) {
/* 373 */     long var3 = chilkatJNI.CkXml_FindNextRecord(this.swigCPtr, this, var1, var2);
/* 374 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public CkXml FindOrAddNewChild(String var1) {
/* 378 */     long var2 = chilkatJNI.CkXml_FindOrAddNewChild(this.swigCPtr, this, var1);
/* 379 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public CkXml FirstChild() {
/* 383 */     long var1 = chilkatJNI.CkXml_FirstChild(this.swigCPtr, this);
/* 384 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean FirstChild2() {
/* 388 */     return chilkatJNI.CkXml_FirstChild2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetAttributeName(int var1, CkString var2) {
/* 392 */     return chilkatJNI.CkXml_GetAttributeName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAttributeName(int var1) {
/* 396 */     return chilkatJNI.CkXml_getAttributeName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String attributeName(int var1) {
/* 400 */     return chilkatJNI.CkXml_attributeName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetAttributeValue(int var1, CkString var2) {
/* 404 */     return chilkatJNI.CkXml_GetAttributeValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAttributeValue(int var1) {
/* 408 */     return chilkatJNI.CkXml_getAttributeValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String attributeValue(int var1) {
/* 412 */     return chilkatJNI.CkXml_attributeValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetAttributeValueInt(int var1) {
/* 416 */     return chilkatJNI.CkXml_GetAttributeValueInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetAttrValue(String var1, CkString var2) {
/* 420 */     return chilkatJNI.CkXml_GetAttrValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAttrValue(String var1) {
/* 424 */     return chilkatJNI.CkXml_getAttrValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String attrValue(String var1) {
/* 428 */     return chilkatJNI.CkXml_attrValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetAttrValueInt(String var1) {
/* 432 */     return chilkatJNI.CkXml_GetAttrValueInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetBinaryContent(boolean var1, boolean var2, String var3, CkByteData var4) {
/* 436 */     return chilkatJNI.CkXml_GetBinaryContent(this.swigCPtr, this, var1, var2, var3, CkByteData.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public CkXml GetChild(int var1) {
/* 440 */     long var2 = chilkatJNI.CkXml_GetChild(this.swigCPtr, this, var1);
/* 441 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public boolean GetChild2(int var1) {
/* 445 */     return chilkatJNI.CkXml_GetChild2(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChildBoolValue(String var1) {
/* 449 */     return chilkatJNI.CkXml_GetChildBoolValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChildContent(String var1, CkString var2) {
/* 453 */     return chilkatJNI.CkXml_GetChildContent(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getChildContent(String var1) {
/* 457 */     return chilkatJNI.CkXml_getChildContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String childContent(String var1) {
/* 461 */     return chilkatJNI.CkXml_childContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChildContentByIndex(int var1, CkString var2) {
/* 465 */     return chilkatJNI.CkXml_GetChildContentByIndex(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getChildContentByIndex(int var1) {
/* 469 */     return chilkatJNI.CkXml_getChildContentByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String childContentByIndex(int var1) {
/* 473 */     return chilkatJNI.CkXml_childContentByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml GetChildExact(String var1, String var2) {
/* 477 */     long var3 = chilkatJNI.CkXml_GetChildExact(this.swigCPtr, this, var1, var2);
/* 478 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public int GetChildIntValue(String var1) {
/* 482 */     return chilkatJNI.CkXml_GetChildIntValue(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChildTag(int var1, CkString var2) {
/* 486 */     return chilkatJNI.CkXml_GetChildTag(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getChildTag(int var1) {
/* 490 */     return chilkatJNI.CkXml_getChildTag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String childTag(int var1) {
/* 494 */     return chilkatJNI.CkXml_childTag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetChildTagByIndex(int var1, CkString var2) {
/* 498 */     return chilkatJNI.CkXml_GetChildTagByIndex(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getChildTagByIndex(int var1) {
/* 502 */     return chilkatJNI.CkXml_getChildTagByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String childTagByIndex(int var1) {
/* 506 */     return chilkatJNI.CkXml_childTagByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml GetChildWithAttr(String var1, String var2, String var3) {
/* 510 */     long var4 = chilkatJNI.CkXml_GetChildWithAttr(this.swigCPtr, this, var1, var2, var3);
/* 511 */     return var4 == 0L ? null : new CkXml(var4, true);
/*     */   }
/*     */   
/*     */   public CkXml GetChildWithContent(String var1) {
/* 515 */     long var2 = chilkatJNI.CkXml_GetChildWithContent(this.swigCPtr, this, var1);
/* 516 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public CkXml GetChildWithTag(String var1) {
/* 520 */     long var2 = chilkatJNI.CkXml_GetChildWithTag(this.swigCPtr, this, var1);
/* 521 */     return var2 == 0L ? null : new CkXml(var2, true);
/*     */   }
/*     */   
/*     */   public CkXml GetNthChildWithTag(String var1, int var2) {
/* 525 */     long var3 = chilkatJNI.CkXml_GetNthChildWithTag(this.swigCPtr, this, var1, var2);
/* 526 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public boolean GetNthChildWithTag2(String var1, int var2) {
/* 530 */     return chilkatJNI.CkXml_GetNthChildWithTag2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml GetParent() {
/* 534 */     long var1 = chilkatJNI.CkXml_GetParent(this.swigCPtr, this);
/* 535 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetParent2() {
/* 539 */     return chilkatJNI.CkXml_GetParent2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkXml GetRoot() {
/* 543 */     long var1 = chilkatJNI.CkXml_GetRoot(this.swigCPtr, this);
/* 544 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public void GetRoot2() {
/* 548 */     chilkatJNI.CkXml_GetRoot2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkXml GetSelf() {
/* 552 */     long var1 = chilkatJNI.CkXml_GetSelf(this.swigCPtr, this);
/* 553 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean GetXml(CkString var1) {
/* 557 */     return chilkatJNI.CkXml_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getXml() {
/* 561 */     return chilkatJNI.CkXml_getXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String xml() {
/* 565 */     return chilkatJNI.CkXml_xml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GetXmlSb(CkStringBuilder var1) {
/* 569 */     return chilkatJNI.CkXml_GetXmlSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean HasAttribute(String var1) {
/* 573 */     return chilkatJNI.CkXml_HasAttribute(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasAttrWithValue(String var1, String var2) {
/* 577 */     return chilkatJNI.CkXml_HasAttrWithValue(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean HasChildWithContent(String var1) {
/* 581 */     return chilkatJNI.CkXml_HasChildWithContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasChildWithTag(String var1) {
/* 585 */     return chilkatJNI.CkXml_HasChildWithTag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasChildWithTagAndContent(String var1, String var2) {
/* 589 */     return chilkatJNI.CkXml_HasChildWithTagAndContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void InsertChildTreeAfter(int var1, CkXml var2) {
/* 593 */     chilkatJNI.CkXml_InsertChildTreeAfter(this.swigCPtr, this, var1, getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public void InsertChildTreeBefore(int var1, CkXml var2) {
/* 597 */     chilkatJNI.CkXml_InsertChildTreeBefore(this.swigCPtr, this, var1, getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkXml LastChild() {
/* 601 */     long var1 = chilkatJNI.CkXml_LastChild(this.swigCPtr, this);
/* 602 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean LastChild2() {
/* 606 */     return chilkatJNI.CkXml_LastChild2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean LoadSb(CkStringBuilder var1, boolean var2) {
/* 610 */     return chilkatJNI.CkXml_LoadSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadXml(String var1) {
/* 614 */     return chilkatJNI.CkXml_LoadXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXml2(String var1, boolean var2) {
/* 618 */     return chilkatJNI.CkXml_LoadXml2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile(String var1) {
/* 622 */     return chilkatJNI.CkXml_LoadXmlFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadXmlFile2(String var1, boolean var2) {
/* 626 */     return chilkatJNI.CkXml_LoadXmlFile2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml NewChild(String var1, String var2) {
/* 630 */     long var3 = chilkatJNI.CkXml_NewChild(this.swigCPtr, this, var1, var2);
/* 631 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public void NewChild2(String var1, String var2) {
/* 635 */     chilkatJNI.CkXml_NewChild2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml NewChildAfter(int var1, String var2, String var3) {
/* 639 */     long var4 = chilkatJNI.CkXml_NewChildAfter(this.swigCPtr, this, var1, var2, var3);
/* 640 */     return var4 == 0L ? null : new CkXml(var4, true);
/*     */   }
/*     */   
/*     */   public CkXml NewChildBefore(int var1, String var2, String var3) {
/* 644 */     long var4 = chilkatJNI.CkXml_NewChildBefore(this.swigCPtr, this, var1, var2, var3);
/* 645 */     return var4 == 0L ? null : new CkXml(var4, true);
/*     */   }
/*     */   
/*     */   public void NewChildInt2(String var1, int var2) {
/* 649 */     chilkatJNI.CkXml_NewChildInt2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml NextSibling() {
/* 653 */     long var1 = chilkatJNI.CkXml_NextSibling(this.swigCPtr, this);
/* 654 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean NextSibling2() {
/* 658 */     return chilkatJNI.CkXml_NextSibling2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int NumChildrenAt(String var1) {
/* 662 */     return chilkatJNI.CkXml_NumChildrenAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int NumChildrenHavingTag(String var1) {
/* 666 */     return chilkatJNI.CkXml_NumChildrenHavingTag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml PreviousSibling() {
/* 670 */     long var1 = chilkatJNI.CkXml_PreviousSibling(this.swigCPtr, this);
/* 671 */     return var1 == 0L ? null : new CkXml(var1, true);
/*     */   }
/*     */   
/*     */   public boolean PreviousSibling2() {
/* 675 */     return chilkatJNI.CkXml_PreviousSibling2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean QEncodeContent(String var1, CkByteData var2) {
/* 679 */     return chilkatJNI.CkXml_QEncodeContent(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean RemoveAllAttributes() {
/* 683 */     return chilkatJNI.CkXml_RemoveAllAttributes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void RemoveAllChildren() {
/* 687 */     chilkatJNI.CkXml_RemoveAllChildren(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean RemoveAttribute(String var1) {
/* 691 */     return chilkatJNI.CkXml_RemoveAttribute(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveChild(String var1) {
/* 695 */     chilkatJNI.CkXml_RemoveChild(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveChildByIndex(int var1) {
/* 699 */     chilkatJNI.CkXml_RemoveChildByIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveChildWithContent(String var1) {
/* 703 */     chilkatJNI.CkXml_RemoveChildWithContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void RemoveFromTree() {
/* 707 */     chilkatJNI.CkXml_RemoveFromTree(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveBinaryContent(String var1, boolean var2, boolean var3, String var4) {
/* 711 */     return chilkatJNI.CkXml_SaveBinaryContent(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 715 */     return chilkatJNI.CkXml_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveXml(String var1) {
/* 719 */     return chilkatJNI.CkXml_SaveXml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkXml SearchAllForContent(CkXml var1, String var2) {
/* 723 */     long var3 = chilkatJNI.CkXml_SearchAllForContent(this.swigCPtr, this, getCPtr(var1), var1, var2);
/* 724 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SearchAllForContent2(CkXml var1, String var2) {
/* 728 */     return chilkatJNI.CkXml_SearchAllForContent2(this.swigCPtr, this, getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public CkXml SearchForAttribute(CkXml var1, String var2, String var3, String var4) {
/* 732 */     long var5 = chilkatJNI.CkXml_SearchForAttribute(this.swigCPtr, this, getCPtr(var1), var1, var2, var3, var4);
/* 733 */     return var5 == 0L ? null : new CkXml(var5, true);
/*     */   }
/*     */   
/*     */   public boolean SearchForAttribute2(CkXml var1, String var2, String var3, String var4) {
/* 737 */     return chilkatJNI.CkXml_SearchForAttribute2(this.swigCPtr, this, getCPtr(var1), var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public CkXml SearchForContent(CkXml var1, String var2, String var3) {
/* 741 */     long var4 = chilkatJNI.CkXml_SearchForContent(this.swigCPtr, this, getCPtr(var1), var1, var2, var3);
/* 742 */     return var4 == 0L ? null : new CkXml(var4, true);
/*     */   }
/*     */   
/*     */   public boolean SearchForContent2(CkXml var1, String var2, String var3) {
/* 746 */     return chilkatJNI.CkXml_SearchForContent2(this.swigCPtr, this, getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public CkXml SearchForTag(CkXml var1, String var2) {
/* 750 */     long var3 = chilkatJNI.CkXml_SearchForTag(this.swigCPtr, this, getCPtr(var1), var1, var2);
/* 751 */     return var3 == 0L ? null : new CkXml(var3, true);
/*     */   }
/*     */   
/*     */   public boolean SearchForTag2(CkXml var1, String var2) {
/* 755 */     return chilkatJNI.CkXml_SearchForTag2(this.swigCPtr, this, getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SetBinaryContent(CkByteData var1, boolean var2, boolean var3, String var4) {
/* 759 */     return chilkatJNI.CkXml_SetBinaryContent(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean SetBinaryContentFromFile(String var1, boolean var2, boolean var3, String var4) {
/* 763 */     return chilkatJNI.CkXml_SetBinaryContentFromFile(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public void SortByAttribute(String var1, boolean var2) {
/* 767 */     chilkatJNI.CkXml_SortByAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SortByAttributeInt(String var1, boolean var2) {
/* 771 */     chilkatJNI.CkXml_SortByAttributeInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SortByContent(boolean var1) {
/* 775 */     chilkatJNI.CkXml_SortByContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortByTag(boolean var1) {
/* 779 */     chilkatJNI.CkXml_SortByTag(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SortRecordsByAttribute(String var1, String var2, boolean var3) {
/* 783 */     chilkatJNI.CkXml_SortRecordsByAttribute(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void SortRecordsByContent(String var1, boolean var2) {
/* 787 */     chilkatJNI.CkXml_SortRecordsByContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SortRecordsByContentInt(String var1, boolean var2) {
/* 791 */     chilkatJNI.CkXml_SortRecordsByContentInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SwapNode(CkXml var1) {
/* 795 */     return chilkatJNI.CkXml_SwapNode(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SwapTree(CkXml var1) {
/* 799 */     return chilkatJNI.CkXml_SwapTree(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean TagContent(String var1, CkString var2) {
/* 803 */     return chilkatJNI.CkXml_TagContent(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String tagContent(String var1) {
/* 807 */     return chilkatJNI.CkXml_tagContent(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean TagEquals(String var1) {
/* 811 */     return chilkatJNI.CkXml_TagEquals(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnzipContent() {
/* 815 */     return chilkatJNI.CkXml_UnzipContent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean UnzipTree() {
/* 819 */     return chilkatJNI.CkXml_UnzipTree(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean UpdateAt(String var1, boolean var2, String var3) {
/* 823 */     return chilkatJNI.CkXml_UpdateAt(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean UpdateAttrAt(String var1, boolean var2, String var3, String var4) {
/* 827 */     return chilkatJNI.CkXml_UpdateAttrAt(this.swigCPtr, this, var1, var2, var3, var4);
/*     */   }
/*     */   
/*     */   public boolean UpdateAttribute(String var1, String var2) {
/* 831 */     return chilkatJNI.CkXml_UpdateAttribute(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean UpdateAttributeInt(String var1, int var2) {
/* 835 */     return chilkatJNI.CkXml_UpdateAttributeInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void UpdateChildContent(String var1, String var2) {
/* 839 */     chilkatJNI.CkXml_UpdateChildContent(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void UpdateChildContentInt(String var1, int var2) {
/* 843 */     chilkatJNI.CkXml_UpdateChildContentInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ZipContent() {
/* 847 */     return chilkatJNI.CkXml_ZipContent(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ZipTree() {
/* 851 */     return chilkatJNI.CkXml_ZipTree(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */