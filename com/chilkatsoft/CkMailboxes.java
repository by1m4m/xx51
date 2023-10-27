/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkMailboxes
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkMailboxes(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkMailboxes var0) {
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
/*  29 */         chilkatJNI.delete_CkMailboxes(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkMailboxes()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkMailboxes(), true);
/*     */   }
/*     */   
/*     */   public int get_Count() {
/*  42 */     return chilkatJNI.CkMailboxes_get_Count(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  46 */     return chilkatJNI.CkMailboxes_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  50 */     chilkatJNI.CkMailboxes_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetFlags(int var1, CkString var2) {
/*  54 */     return chilkatJNI.CkMailboxes_GetFlags(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getFlags(int var1) {
/*  58 */     return chilkatJNI.CkMailboxes_getFlags(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String flags(int var1) {
/*  62 */     return chilkatJNI.CkMailboxes_flags(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetMailboxIndex(String var1) {
/*  66 */     return chilkatJNI.CkMailboxes_GetMailboxIndex(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetName(int var1, CkString var2) {
/*  70 */     return chilkatJNI.CkMailboxes_GetName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getName(int var1) {
/*  74 */     return chilkatJNI.CkMailboxes_getName(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String name(int var1) {
/*  78 */     return chilkatJNI.CkMailboxes_name(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetNthFlag(int var1, int var2, CkString var3) {
/*  82 */     return chilkatJNI.CkMailboxes_GetNthFlag(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public String getNthFlag(int var1, int var2) {
/*  86 */     return chilkatJNI.CkMailboxes_getNthFlag(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String nthFlag(int var1, int var2) {
/*  90 */     return chilkatJNI.CkMailboxes_nthFlag(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public int GetNumFlags(int var1) {
/*  94 */     return chilkatJNI.CkMailboxes_GetNumFlags(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean HasFlag(int var1, String var2) {
/*  98 */     return chilkatJNI.CkMailboxes_HasFlag(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean HasInferiors(int var1) {
/* 102 */     return chilkatJNI.CkMailboxes_HasInferiors(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsMarked(int var1) {
/* 106 */     return chilkatJNI.CkMailboxes_IsMarked(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean IsSelectable(int var1) {
/* 110 */     return chilkatJNI.CkMailboxes_IsSelectable(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 114 */     return chilkatJNI.CkMailboxes_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMailboxes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */