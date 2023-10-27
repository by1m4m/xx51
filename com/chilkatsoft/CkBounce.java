/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkBounce
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkBounce(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkBounce var0) {
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
/*  29 */         chilkatJNI.delete_CkBounce(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkBounce()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkBounce(), true);
/*     */   }
/*     */   
/*     */   public void LastErrorXml(CkString var1) {
/*  42 */     chilkatJNI.CkBounce_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorHtml(CkString var1) {
/*  46 */     chilkatJNI.CkBounce_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void LastErrorText(CkString var1) {
/*  50 */     chilkatJNI.CkBounce_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_BounceAddress(CkString var1) {
/*  54 */     chilkatJNI.CkBounce_get_BounceAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String bounceAddress() {
/*  58 */     return chilkatJNI.CkBounce_bounceAddress(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_BounceData(CkString var1) {
/*  62 */     chilkatJNI.CkBounce_get_BounceData(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String bounceData() {
/*  66 */     return chilkatJNI.CkBounce_bounceData(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_BounceType() {
/*  70 */     return chilkatJNI.CkBounce_get_BounceType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_DebugLogFilePath(CkString var1) {
/*  74 */     chilkatJNI.CkBounce_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String debugLogFilePath() {
/*  78 */     return chilkatJNI.CkBounce_debugLogFilePath(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_DebugLogFilePath(String var1) {
/*  82 */     chilkatJNI.CkBounce_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastErrorHtml(CkString var1) {
/*  86 */     chilkatJNI.CkBounce_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorHtml() {
/*  90 */     return chilkatJNI.CkBounce_lastErrorHtml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorText(CkString var1) {
/*  94 */     chilkatJNI.CkBounce_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorText() {
/*  98 */     return chilkatJNI.CkBounce_lastErrorText(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastErrorXml(CkString var1) {
/* 102 */     chilkatJNI.CkBounce_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastErrorXml() {
/* 106 */     return chilkatJNI.CkBounce_lastErrorXml(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 110 */     return chilkatJNI.CkBounce_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 114 */     chilkatJNI.CkBounce_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_VerboseLogging() {
/* 118 */     return chilkatJNI.CkBounce_get_VerboseLogging(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_VerboseLogging(boolean var1) {
/* 122 */     chilkatJNI.CkBounce_put_VerboseLogging(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Version(CkString var1) {
/* 126 */     chilkatJNI.CkBounce_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String version() {
/* 130 */     return chilkatJNI.CkBounce_version(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ExamineEmail(CkEmail var1) {
/* 134 */     return chilkatJNI.CkBounce_ExamineEmail(this.swigCPtr, this, CkEmail.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean ExamineEml(String var1) {
/* 138 */     return chilkatJNI.CkBounce_ExamineEml(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExamineMime(String var1) {
/* 142 */     return chilkatJNI.CkBounce_ExamineMime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SaveLastError(String var1) {
/* 146 */     return chilkatJNI.CkBounce_SaveLastError(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean UnlockComponent(String var1) {
/* 150 */     return chilkatJNI.CkBounce_UnlockComponent(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkBounce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */