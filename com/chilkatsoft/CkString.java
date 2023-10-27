/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkString
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkString(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkString var0) {
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
/*  29 */         chilkatJNI.delete_CkString(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkString()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkString(), true);
/*     */   }
/*     */   
/*     */   public boolean loadFile(String var1, String var2) {
/*  42 */     return chilkatJNI.CkString_loadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public char charAt(int var1) {
/*  46 */     return chilkatJNI.CkString_charAt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int intValue() {
/*  50 */     return chilkatJNI.CkString_intValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/*  54 */     return chilkatJNI.CkString_doubleValue(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int countCharOccurances(char var1) {
/*  58 */     return chilkatJNI.CkString_countCharOccurances(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendCurrentDateRfc822() {
/*  62 */     chilkatJNI.CkString_appendCurrentDateRfc822(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void removeDelimited(String var1, String var2, boolean var3) {
/*  66 */     chilkatJNI.CkString_removeDelimited(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void setStr(CkString var1) {
/*  70 */     chilkatJNI.CkString_setStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean endsWith(String var1) {
/*  74 */     return chilkatJNI.CkString_endsWith(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean endsWithStr(CkString var1) {
/*  78 */     return chilkatJNI.CkString_endsWithStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean beginsWithStr(CkString var1) {
/*  82 */     return chilkatJNI.CkString_beginsWithStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int indexOf(String var1) {
/*  86 */     return chilkatJNI.CkString_indexOf(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int indexOfStr(CkString var1) {
/*  90 */     return chilkatJNI.CkString_indexOfStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int replaceAll(CkString var1, CkString var2) {
/*  94 */     return chilkatJNI.CkString_replaceAll(this.swigCPtr, this, getCPtr(var1), var1, getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean replaceFirst(CkString var1, CkString var2) {
/*  98 */     return chilkatJNI.CkString_replaceFirst(this.swigCPtr, this, getCPtr(var1), var1, getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public CkString substring(int var1, int var2) {
/* 102 */     long var3 = chilkatJNI.CkString_substring(this.swigCPtr, this, var1, var2);
/* 103 */     return var3 == 0L ? null : new CkString(var3, false);
/*     */   }
/*     */   
/*     */   public boolean matchesStr(CkString var1) {
/* 107 */     return chilkatJNI.CkString_matchesStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean matches(String var1) {
/* 111 */     return chilkatJNI.CkString_matches(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public CkString getChar(int var1) {
/* 115 */     long var2 = chilkatJNI.CkString_getChar(this.swigCPtr, this, var1);
/* 116 */     return var2 == 0L ? null : new CkString(var2, false);
/*     */   }
/*     */   
/*     */   public int removeAll(CkString var1) {
/* 120 */     return chilkatJNI.CkString_removeAll(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean removeFirst(CkString var1) {
/* 124 */     return chilkatJNI.CkString_removeFirst(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void chopAtStr(CkString var1) {
/* 128 */     chilkatJNI.CkString_chopAtStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void urlDecode(String var1) {
/* 132 */     chilkatJNI.CkString_urlDecode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void urlEncode(String var1) {
/* 136 */     chilkatJNI.CkString_urlEncode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void base64Decode(String var1) {
/* 140 */     chilkatJNI.CkString_base64Decode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void base64Encode(String var1) {
/* 144 */     chilkatJNI.CkString_base64Encode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void qpDecode(String var1) {
/* 148 */     chilkatJNI.CkString_qpDecode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void qpEncode(String var1) {
/* 152 */     chilkatJNI.CkString_qpEncode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void hexDecode(String var1) {
/* 156 */     chilkatJNI.CkString_hexDecode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void hexEncode(String var1) {
/* 160 */     chilkatJNI.CkString_hexEncode(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void entityDecode() {
/* 164 */     chilkatJNI.CkString_entityDecode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void entityEncode() {
/* 168 */     chilkatJNI.CkString_entityEncode(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void appendUtf8(String var1) {
/* 172 */     chilkatJNI.CkString_appendUtf8(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendAnsi(String var1) {
/* 176 */     chilkatJNI.CkString_appendAnsi(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 180 */     chilkatJNI.CkString_clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void prepend(String var1) {
/* 184 */     chilkatJNI.CkString_prepend(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendInt(int var1) {
/* 188 */     chilkatJNI.CkString_appendInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void append(String var1) {
/* 192 */     chilkatJNI.CkString_append(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendChar(char var1) {
/* 196 */     chilkatJNI.CkString_appendChar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendN(String var1, int var2) {
/* 200 */     chilkatJNI.CkString_appendN(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void appendStr(CkString var1) {
/* 204 */     chilkatJNI.CkString_appendStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void appendEnc(String var1, String var2) {
/* 208 */     chilkatJNI.CkString_appendEnc(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String getEnc(String var1) {
/* 212 */     return chilkatJNI.CkString_getEnc(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void setString(String var1) {
/* 216 */     chilkatJNI.CkString_setString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void setStringAnsi(String var1) {
/* 220 */     chilkatJNI.CkString_setStringAnsi(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void setStringUtf8(String var1) {
/* 224 */     chilkatJNI.CkString_setStringUtf8(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String getAnsi() {
/* 228 */     return chilkatJNI.CkString_getAnsi(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String getUtf8() {
/* 232 */     return chilkatJNI.CkString_getUtf8(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int compareStr(CkString var1) {
/* 236 */     return chilkatJNI.CkString_compareStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String getString() {
/* 240 */     return chilkatJNI.CkString_getString(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int getSizeUtf8() {
/* 244 */     return chilkatJNI.CkString_getSizeUtf8(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int getSizeAnsi() {
/* 248 */     return chilkatJNI.CkString_getSizeAnsi(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int getNumChars() {
/* 252 */     return chilkatJNI.CkString_getNumChars(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void trim() {
/* 256 */     chilkatJNI.CkString_trim(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void trim2() {
/* 260 */     chilkatJNI.CkString_trim2(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void trimInsideSpaces() {
/* 264 */     chilkatJNI.CkString_trimInsideSpaces(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int replaceAllOccurances(String var1, String var2) {
/* 268 */     return chilkatJNI.CkString_replaceAllOccurances(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean replaceFirstOccurance(String var1, String var2) {
/* 272 */     return chilkatJNI.CkString_replaceFirstOccurance(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void toCRLF() {
/* 276 */     chilkatJNI.CkString_toCRLF(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void toLF() {
/* 280 */     chilkatJNI.CkString_toLF(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void eliminateChar(char var1, int var2) {
/* 284 */     chilkatJNI.CkString_eliminateChar(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public char lastChar() {
/* 288 */     return chilkatJNI.CkString_lastChar(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void shorten(int var1) {
/* 292 */     chilkatJNI.CkString_shorten(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void toLowerCase() {
/* 296 */     chilkatJNI.CkString_toLowerCase(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void toUpperCase() {
/* 300 */     chilkatJNI.CkString_toUpperCase(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void encodeXMLSpecial() {
/* 304 */     chilkatJNI.CkString_encodeXMLSpecial(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void decodeXMLSpecial() {
/* 308 */     chilkatJNI.CkString_decodeXMLSpecial(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean containsSubstring(String var1) {
/* 312 */     return chilkatJNI.CkString_containsSubstring(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean containsSubstringNoCase(String var1) {
/* 316 */     return chilkatJNI.CkString_containsSubstringNoCase(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean equals(String var1) {
/* 320 */     return chilkatJNI.CkString_equals(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean equalsStr(CkString var1) {
/* 324 */     return chilkatJNI.CkString_equalsStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean equalsIgnoreCase(String var1) {
/* 328 */     return chilkatJNI.CkString_equalsIgnoreCase(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean equalsIgnoreCaseStr(CkString var1) {
/* 332 */     return chilkatJNI.CkString_equalsIgnoreCaseStr(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void removeChunk(int var1, int var2) {
/* 336 */     chilkatJNI.CkString_removeChunk(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void removeCharOccurances(char var1) {
/* 340 */     chilkatJNI.CkString_removeCharOccurances(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void replaceChar(char var1, char var2) {
/* 344 */     chilkatJNI.CkString_replaceChar(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void chopAtFirstChar(char var1) {
/* 348 */     chilkatJNI.CkString_chopAtFirstChar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean saveToFile(String var1, String var2) {
/* 352 */     return chilkatJNI.CkString_saveToFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkStringArray split(char var1, boolean var2, boolean var3, boolean var4) {
/* 356 */     long var5 = chilkatJNI.CkString_split(this.swigCPtr, this, var1, var2, var3, var4);
/* 357 */     return var5 == 0L ? null : new CkStringArray(var5, true);
/*     */   }
/*     */   
/*     */   public CkStringArray split2(String var1, boolean var2, boolean var3, boolean var4) {
/* 361 */     long var5 = chilkatJNI.CkString_split2(this.swigCPtr, this, var1, var2, var3, var4);
/* 362 */     return var5 == 0L ? null : new CkStringArray(var5, true);
/*     */   }
/*     */   
/*     */   public CkStringArray tokenize(String var1) {
/* 366 */     long var2 = chilkatJNI.CkString_tokenize(this.swigCPtr, this, var1);
/* 367 */     return var2 == 0L ? null : new CkStringArray(var2, true);
/*     */   }
/*     */   
/*     */   public CkStringArray splitAtWS() {
/* 371 */     long var1 = chilkatJNI.CkString_splitAtWS(this.swigCPtr, this);
/* 372 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*     */   }
/*     */   
/*     */   public boolean beginsWith(String var1) {
/* 376 */     return chilkatJNI.CkString_beginsWith(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */