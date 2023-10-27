/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkByteData
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkByteData(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkByteData var0) {
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
/*  29 */         chilkatJNI.delete_CkByteData(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkByteData()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkByteData(), true);
/*     */   }
/*     */   
/*     */   public byte[] toByteArray() {
/*  42 */     return chilkatJNI.CkByteData_toByteArray(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void appendByteArray(byte[] var1) {
/*  46 */     chilkatJNI.CkByteData_appendByteArray(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String to_s() {
/*  50 */     return chilkatJNI.CkByteData_to_s(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void appendRandom(int var1) {
/*  54 */     chilkatJNI.CkByteData_appendRandom(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendInt(int var1, boolean var2) {
/*  58 */     chilkatJNI.CkByteData_appendInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void appendShort(short var1, boolean var2) {
/*  62 */     chilkatJNI.CkByteData_appendShort(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String getEncodedRange(String var1, int var2, int var3) {
/*  66 */     return chilkatJNI.CkByteData_getEncodedRange(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void appendRange(CkByteData var1, int var2, int var3) {
/*  70 */     chilkatJNI.CkByteData_appendRange(this.swigCPtr, this, getCPtr(var1), var1, var2, var3);
/*     */   }
/*     */   
/*     */   public void ensureBuffer(int var1) {
/*  74 */     chilkatJNI.CkByteData_ensureBuffer(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int findBytes2(String var1, int var2) {
/*  78 */     return chilkatJNI.CkByteData_findBytes2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int findBytes(CkByteData var1) {
/*  82 */     return chilkatJNI.CkByteData_findBytes(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean beginsWith2(String var1, int var2) {
/*  86 */     return chilkatJNI.CkByteData_beginsWith2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean beginsWith(CkByteData var1) {
/*  90 */     return chilkatJNI.CkByteData_beginsWith(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void removeChunk(int var1, int var2) {
/*  94 */     chilkatJNI.CkByteData_removeChunk(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void byteSwap4321() {
/*  98 */     chilkatJNI.CkByteData_byteSwap4321(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void pad(int var1, int var2) {
/* 102 */     chilkatJNI.CkByteData_pad(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void unpad(int var1, int var2) {
/* 106 */     chilkatJNI.CkByteData_unpad(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean is7bit() {
/* 110 */     return chilkatJNI.CkByteData_is7bit(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 114 */     chilkatJNI.CkByteData_clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 118 */     return chilkatJNI.CkByteData_getSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void appendEncoded(String var1, String var2) {
/* 122 */     chilkatJNI.CkByteData_appendEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void encode(String var1, CkString var2) {
/* 126 */     chilkatJNI.CkByteData_encode(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public SWIGTYPE_p_unsigned_char getData() {
/* 130 */     long var1 = chilkatJNI.CkByteData_getData(this.swigCPtr, this);
/* 131 */     return var1 == 0L ? null : new SWIGTYPE_p_unsigned_char(var1, false);
/*     */   }
/*     */   
/*     */   public SWIGTYPE_p_unsigned_char getBytes() {
/* 135 */     long var1 = chilkatJNI.CkByteData_getBytes(this.swigCPtr, this);
/* 136 */     return var1 == 0L ? null : new SWIGTYPE_p_unsigned_char(var1, false);
/*     */   }
/*     */   
/*     */   public String getEncoded(String var1) {
/* 140 */     return chilkatJNI.CkByteData_getEncoded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public SWIGTYPE_p_unsigned_char getRange(int var1, int var2) {
/* 144 */     long var3 = chilkatJNI.CkByteData_getRange(this.swigCPtr, this, var1, var2);
/* 145 */     return var3 == 0L ? null : new SWIGTYPE_p_unsigned_char(var3, false);
/*     */   }
/*     */   
/*     */   public String getRangeStr(int var1, int var2) {
/* 149 */     return chilkatJNI.CkByteData_getRangeStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void append2(String var1, int var2) {
/* 153 */     chilkatJNI.CkByteData_append2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean equals2(String var1, int var2) {
/* 157 */     return chilkatJNI.CkByteData_equals2(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean equals(CkByteData var1) {
/* 161 */     return chilkatJNI.CkByteData_equals(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void appendStr(String var1) {
/* 165 */     chilkatJNI.CkByteData_appendStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void appendChar(char var1) {
/* 169 */     chilkatJNI.CkByteData_appendChar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public short getByte(int var1) {
/* 173 */     return chilkatJNI.CkByteData_getByte(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public char getChar(int var1) {
/* 177 */     return chilkatJNI.CkByteData_getChar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long getUInt(int var1) {
/* 181 */     return chilkatJNI.CkByteData_getUInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getInt(int var1) {
/* 185 */     return chilkatJNI.CkByteData_getInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getUShort(int var1) {
/* 189 */     return chilkatJNI.CkByteData_getUShort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public short getShort(int var1) {
/* 193 */     return chilkatJNI.CkByteData_getShort(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean loadFile(String var1) {
/* 197 */     return chilkatJNI.CkByteData_loadFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean saveFile(String var1) {
/* 201 */     return chilkatJNI.CkByteData_saveFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean appendFile(String var1) {
/* 205 */     return chilkatJNI.CkByteData_appendFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void shorten(int var1) {
/* 209 */     chilkatJNI.CkByteData_shorten(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkByteData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */