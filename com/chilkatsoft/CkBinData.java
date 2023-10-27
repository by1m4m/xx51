/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkBinData
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkBinData(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkBinData var0) {
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
/*  29 */         chilkatJNI.delete_CkBinData(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkBinData()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkBinData(), true);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  42 */     return chilkatJNI.CkBinData_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  46 */     chilkatJNI.CkBinData_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_NumBytes() {
/*  50 */     return chilkatJNI.CkBinData_get_NumBytes(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean AppendBd(CkBinData var1) {
/*  54 */     return chilkatJNI.CkBinData_AppendBd(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AppendBinary(CkByteData var1) {
/*  58 */     return chilkatJNI.CkBinData_AppendBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AppendBom(String var1) {
/*  62 */     return chilkatJNI.CkBinData_AppendBom(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendEncoded(String var1, String var2) {
/*  66 */     return chilkatJNI.CkBinData_AppendEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendEncodedSb(CkStringBuilder var1, String var2) {
/*  70 */     return chilkatJNI.CkBinData_AppendEncodedSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendSb(CkStringBuilder var1, String var2) {
/*  74 */     return chilkatJNI.CkBinData_AppendSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AppendString(String var1, String var2) {
/*  78 */     return chilkatJNI.CkBinData_AppendString(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Clear() {
/*  82 */     return chilkatJNI.CkBinData_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ContentsEqual(CkBinData var1) {
/*  86 */     return chilkatJNI.CkBinData_ContentsEqual(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetBinary(CkByteData var1) {
/*  90 */     return chilkatJNI.CkBinData_GetBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetBinaryChunk(int var1, int var2, CkByteData var3) {
/*  94 */     return chilkatJNI.CkBinData_GetBinaryChunk(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean GetEncoded(String var1, CkString var2) {
/*  98 */     return chilkatJNI.CkBinData_GetEncoded(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getEncoded(String var1) {
/* 102 */     return chilkatJNI.CkBinData_getEncoded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String encoded(String var1) {
/* 106 */     return chilkatJNI.CkBinData_encoded(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedChunk(int var1, int var2, String var3, CkString var4) {
/* 110 */     return chilkatJNI.CkBinData_GetEncodedChunk(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*     */   }
/*     */   
/*     */   public String getEncodedChunk(int var1, int var2, String var3) {
/* 114 */     return chilkatJNI.CkBinData_getEncodedChunk(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public String encodedChunk(int var1, int var2, String var3) {
/* 118 */     return chilkatJNI.CkBinData_encodedChunk(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean GetEncodedSb(String var1, CkStringBuilder var2) {
/* 122 */     return chilkatJNI.CkBinData_GetEncodedSb(this.swigCPtr, this, var1, CkStringBuilder.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetString(String var1, CkString var2) {
/* 126 */     return chilkatJNI.CkBinData_GetString(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getString(String var1) {
/* 130 */     return chilkatJNI.CkBinData_getString(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String string(String var1) {
/* 134 */     return chilkatJNI.CkBinData_string(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadBinary(CkByteData var1) {
/* 138 */     return chilkatJNI.CkBinData_LoadBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadEncoded(String var1, String var2) {
/* 142 */     return chilkatJNI.CkBinData_LoadEncoded(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1) {
/* 146 */     return chilkatJNI.CkBinData_LoadFile(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean RemoveChunk(int var1, int var2) {
/* 150 */     return chilkatJNI.CkBinData_RemoveChunk(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SecureClear() {
/* 154 */     return chilkatJNI.CkBinData_SecureClear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean WriteFile(String var1) {
/* 158 */     return chilkatJNI.CkBinData_WriteFile(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkBinData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */