/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkRarEntry
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkRarEntry(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkRarEntry var0) {
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
/*  29 */         chilkatJNI.delete_CkRarEntry(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_Filename(CkString var1)
/*     */   {
/*  38 */     chilkatJNI.CkRarEntry_get_Filename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int get_UncompressedSize() {
/*  42 */     return chilkatJNI.CkRarEntry_get_UncompressedSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_CompressedSize() {
/*  46 */     return chilkatJNI.CkRarEntry_get_CompressedSize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastModified(SYSTEMTIME var1) {
/*  50 */     chilkatJNI.CkRarEntry_get_LastModified(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean get_IsDirectory() {
/*  54 */     return chilkatJNI.CkRarEntry_get_IsDirectory(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsReadOnly() {
/*  58 */     return chilkatJNI.CkRarEntry_get_IsReadOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Unrar(String var1) {
/*  62 */     return chilkatJNI.CkRarEntry_Unrar(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Crc() {
/*  66 */     return chilkatJNI.CkRarEntry_get_Crc(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/*  70 */     return chilkatJNI.CkRarEntry_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  74 */     chilkatJNI.CkRarEntry_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  78 */     chilkatJNI.CkRarEntry_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  82 */     chilkatJNI.CkRarEntry_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  86 */     return chilkatJNI.CkRarEntry_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  90 */     return chilkatJNI.CkRarEntry_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  94 */     return chilkatJNI.CkRarEntry_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String filename() {
/*  98 */     return chilkatJNI.CkRarEntry_filename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkRarEntry() {
/* 102 */     this(chilkatJNI.new_CkRarEntry(), true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkRarEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */