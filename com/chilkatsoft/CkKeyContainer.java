/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkKeyContainer
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkKeyContainer(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkKeyContainer var0) {
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
/*  29 */         chilkatJNI.delete_CkKeyContainer(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkKeyContainer()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkKeyContainer(), true);
/*     */   }
/*     */   
/*     */   public boolean get_IsOpen() {
/*  42 */     return chilkatJNI.CkKeyContainer_get_IsOpen(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsMachineKeyset() {
/*  46 */     return chilkatJNI.CkKeyContainer_get_IsMachineKeyset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_ContainerName(CkString var1) {
/*  50 */     chilkatJNI.CkKeyContainer_get_ContainerName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void GenerateUuid(CkString var1) {
/*  54 */     chilkatJNI.CkKeyContainer_GenerateUuid(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean CreateContainer(String var1, boolean var2) {
/*  58 */     return chilkatJNI.CkKeyContainer_CreateContainer(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OpenContainer(String var1, boolean var2, boolean var3) {
/*  62 */     return chilkatJNI.CkKeyContainer_OpenContainer(this.swigCPtr, this, var1, var2, var3);
/*     */   }
/*     */   
/*     */   public boolean DeleteContainer() {
/*  66 */     return chilkatJNI.CkKeyContainer_DeleteContainer(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void CloseContainer() {
/*  70 */     chilkatJNI.CkKeyContainer_CloseContainer(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean GenerateKeyPair(boolean var1, int var2) {
/*  74 */     return chilkatJNI.CkKeyContainer_GenerateKeyPair(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkPrivateKey GetPrivateKey(boolean var1) {
/*  78 */     long var2 = chilkatJNI.CkKeyContainer_GetPrivateKey(this.swigCPtr, this, var1);
/*  79 */     return var2 == 0L ? null : new CkPrivateKey(var2, true);
/*     */   }
/*     */   
/*     */   public CkPublicKey GetPublicKey(boolean var1) {
/*  83 */     long var2 = chilkatJNI.CkKeyContainer_GetPublicKey(this.swigCPtr, this, var1);
/*  84 */     return var2 == 0L ? null : new CkPublicKey(var2, true);
/*     */   }
/*     */   
/*     */   public boolean ImportPublicKey(CkPublicKey var1, boolean var2) {
/*  88 */     return chilkatJNI.CkKeyContainer_ImportPublicKey(this.swigCPtr, this, CkPublicKey.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean ImportPrivateKey(CkPrivateKey var1, boolean var2) {
/*  92 */     return chilkatJNI.CkKeyContainer_ImportPrivateKey(this.swigCPtr, this, CkPrivateKey.getCPtr(var1), var1, var2);
/*     */   }
/*     */   
/*     */   public boolean FetchContainerNames(boolean var1) {
/*  96 */     return chilkatJNI.CkKeyContainer_FetchContainerNames(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetNumContainers(boolean var1) {
/* 100 */     return chilkatJNI.CkKeyContainer_GetNumContainers(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void GetNthContainerName(boolean var1, int var2, CkString var3) {
/* 104 */     chilkatJNI.CkKeyContainer_GetNthContainerName(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getNthContainerName(boolean var1, int var2) {
/* 108 */     return chilkatJNI.CkKeyContainer_getNthContainerName(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String containerName() {
/* 112 */     return chilkatJNI.CkKeyContainer_containerName(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String generateUuid() {
/* 116 */     return chilkatJNI.CkKeyContainer_generateUuid(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 120 */     return chilkatJNI.CkKeyContainer_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/* 124 */     chilkatJNI.CkKeyContainer_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/* 128 */     chilkatJNI.CkKeyContainer_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/* 132 */     chilkatJNI.CkKeyContainer_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/* 136 */     return chilkatJNI.CkKeyContainer_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 140 */     return chilkatJNI.CkKeyContainer_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/* 144 */     return chilkatJNI.CkKeyContainer_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkKeyContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */