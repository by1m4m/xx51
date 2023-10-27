/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSecureString
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSecureString(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSecureString var0) {
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
/*  29 */         chilkatJNI.delete_CkSecureString(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSecureString()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSecureString(), true);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  42 */     return chilkatJNI.CkSecureString_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  46 */     chilkatJNI.CkSecureString_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_MaintainHash(CkString var1) {
/*  50 */     chilkatJNI.CkSecureString_get_MaintainHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String maintainHash() {
/*  54 */     return chilkatJNI.CkSecureString_maintainHash(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_MaintainHash(String var1) {
/*  58 */     chilkatJNI.CkSecureString_put_MaintainHash(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_ReadOnly() {
/*  62 */     return chilkatJNI.CkSecureString_get_ReadOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_ReadOnly(boolean var1) {
/*  66 */     chilkatJNI.CkSecureString_put_ReadOnly(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Access(CkString var1) {
/*  70 */     return chilkatJNI.CkSecureString_Access(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String access() {
/*  74 */     return chilkatJNI.CkSecureString_access(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean Append(String var1) {
/*  78 */     return chilkatJNI.CkSecureString_Append(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AppendSb(CkStringBuilder var1) {
/*  82 */     return chilkatJNI.CkSecureString_AppendSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AppendSecure(CkSecureString var1) {
/*  86 */     return chilkatJNI.CkSecureString_AppendSecure(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean HashVal(String var1, CkString var2) {
/*  90 */     return chilkatJNI.CkSecureString_HashVal(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String hashVal(String var1) {
/*  94 */     return chilkatJNI.CkSecureString_hashVal(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LoadFile(String var1, String var2) {
/*  98 */     return chilkatJNI.CkSecureString_LoadFile(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean SecStrEquals(CkSecureString var1) {
/* 102 */     return chilkatJNI.CkSecureString_SecStrEquals(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean VerifyHash(String var1, String var2) {
/* 106 */     return chilkatJNI.CkSecureString_VerifyHash(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSecureString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */