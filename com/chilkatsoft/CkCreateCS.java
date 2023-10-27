/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkCreateCS
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkCreateCS(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkCreateCS var0) {
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
/*  29 */         chilkatJNI.delete_CkCreateCS(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkCreateCS()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkCreateCS(), true);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  42 */     return chilkatJNI.CkCreateCS_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  46 */     return chilkatJNI.CkCreateCS_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  50 */     return chilkatJNI.CkCreateCS_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_ReadOnly() {
/*  54 */     return chilkatJNI.CkCreateCS_get_ReadOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadOnly(boolean var1) {
/*  58 */     chilkatJNI.CkCreateCS_put_ReadOnly(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/*  62 */     chilkatJNI.CkCreateCS_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public CkCertStore CreateRegistryStore(String var1, String var2) {
/*  66 */     long var3 = chilkatJNI.CkCreateCS_CreateRegistryStore(this.swigCPtr, this, var1, var2);
/*  67 */     return var3 == 0L ? null : new CkCertStore(var3, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenRegistryStore(String var1, String var2) {
/*  71 */     long var3 = chilkatJNI.CkCreateCS_OpenRegistryStore(this.swigCPtr, this, var1, var2);
/*  72 */     return var3 == 0L ? null : new CkCertStore(var3, true);
/*     */   }
/*     */   
/*     */   public CkCertStore CreateFileStore(String var1) {
/*  76 */     long var2 = chilkatJNI.CkCreateCS_CreateFileStore(this.swigCPtr, this, var1);
/*  77 */     return var2 == 0L ? null : new CkCertStore(var2, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenFileStore(String var1) {
/*  81 */     long var2 = chilkatJNI.CkCreateCS_OpenFileStore(this.swigCPtr, this, var1);
/*  82 */     return var2 == 0L ? null : new CkCertStore(var2, true);
/*     */   }
/*     */   
/*     */   public CkCertStore CreateMemoryStore() {
/*  86 */     long var1 = chilkatJNI.CkCreateCS_CreateMemoryStore(this.swigCPtr, this);
/*  87 */     return var1 == 0L ? null : new CkCertStore(var1, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenChilkatStore() {
/*  91 */     long var1 = chilkatJNI.CkCreateCS_OpenChilkatStore(this.swigCPtr, this);
/*  92 */     return var1 == 0L ? null : new CkCertStore(var1, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenOutlookStore() {
/*  96 */     long var1 = chilkatJNI.CkCreateCS_OpenOutlookStore(this.swigCPtr, this);
/*  97 */     return var1 == 0L ? null : new CkCertStore(var1, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenLocalSystemStore() {
/* 101 */     long var1 = chilkatJNI.CkCreateCS_OpenLocalSystemStore(this.swigCPtr, this);
/* 102 */     return var1 == 0L ? null : new CkCertStore(var1, true);
/*     */   }
/*     */   
/*     */   public CkCertStore OpenCurrentUserStore() {
/* 106 */     long var1 = chilkatJNI.CkCreateCS_OpenCurrentUserStore(this.swigCPtr, this);
/* 107 */     return var1 == 0L ? null : new CkCertStore(var1, true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 111 */     return chilkatJNI.CkCreateCS_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/* 115 */     chilkatJNI.CkCreateCS_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/* 119 */     chilkatJNI.CkCreateCS_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/* 123 */     chilkatJNI.CkCreateCS_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkCreateCS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */