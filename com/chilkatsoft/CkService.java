/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkService
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkService(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkService var0) {
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
/*  29 */         chilkatJNI.delete_CkService(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkService()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkService(), true);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/*  42 */     return chilkatJNI.CkService_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  46 */     chilkatJNI.CkService_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  50 */     chilkatJNI.CkService_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  54 */     chilkatJNI.CkService_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  58 */     return chilkatJNI.CkService_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/*  62 */     return chilkatJNI.CkService_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  66 */     return chilkatJNI.CkService_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Install() {
/*  70 */     return chilkatJNI.CkService_Install(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Uninstall() {
/*  74 */     return chilkatJNI.CkService_Uninstall(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Start() {
/*  78 */     return chilkatJNI.CkService_Start(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Stop() {
/*  82 */     return chilkatJNI.CkService_Stop(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ServiceName(CkString var1) {
/*  86 */     chilkatJNI.CkService_get_ServiceName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serviceName() {
/*  90 */     return chilkatJNI.CkService_serviceName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ServiceName(String var1) {
/*  94 */     chilkatJNI.CkService_put_ServiceName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_DisplayName(CkString var1) {
/*  98 */     chilkatJNI.CkService_get_DisplayName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String displayName() {
/* 102 */     return chilkatJNI.CkService_displayName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DisplayName(String var1) {
/* 106 */     chilkatJNI.CkService_put_DisplayName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_ExeFilename(CkString var1) {
/* 110 */     chilkatJNI.CkService_get_ExeFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String exeFilename() {
/* 114 */     return chilkatJNI.CkService_exeFilename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ExeFilename(String var1) {
/* 118 */     chilkatJNI.CkService_put_ExeFilename(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_AutoStart() {
/* 122 */     return chilkatJNI.CkService_get_AutoStart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_AutoStart(boolean var1) {
/* 126 */     chilkatJNI.CkService_put_AutoStart(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetAutoStart() {
/* 130 */     return chilkatJNI.CkService_SetAutoStart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetDemandStart() {
/* 134 */     return chilkatJNI.CkService_SetDemandStart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Disable() {
/* 138 */     return chilkatJNI.CkService_Disable(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IsAutoStart() {
/* 142 */     return chilkatJNI.CkService_IsAutoStart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IsDemandStart() {
/* 146 */     return chilkatJNI.CkService_IsDemandStart(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IsDisabled() {
/* 150 */     return chilkatJNI.CkService_IsDisabled(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IsInstalled() {
/* 154 */     return chilkatJNI.CkService_IsInstalled(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int IsRunning() {
/* 158 */     return chilkatJNI.CkService_IsRunning(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */