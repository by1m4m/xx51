/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkHashtable
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkHashtable(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkHashtable var0) {
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
/*  29 */         chilkatJNI.delete_CkHashtable(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkHashtable()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkHashtable(), true);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  42 */     return chilkatJNI.CkHashtable_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  46 */     chilkatJNI.CkHashtable_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddFromXmlSb(CkStringBuilder var1) {
/*  50 */     return chilkatJNI.CkHashtable_AddFromXmlSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean AddInt(String var1, int var2) {
/*  54 */     return chilkatJNI.CkHashtable_AddInt(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean AddQueryParams(String var1) {
/*  58 */     return chilkatJNI.CkHashtable_AddQueryParams(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddStr(String var1, String var2) {
/*  62 */     return chilkatJNI.CkHashtable_AddStr(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void Clear() {
/*  66 */     chilkatJNI.CkHashtable_Clear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ClearWithNewCapacity(int var1) {
/*  70 */     return chilkatJNI.CkHashtable_ClearWithNewCapacity(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Contains(String var1) {
/*  74 */     return chilkatJNI.CkHashtable_Contains(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ContainsIntKey(int var1) {
/*  78 */     return chilkatJNI.CkHashtable_ContainsIntKey(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetKeys(CkStringTable var1) {
/*  82 */     return chilkatJNI.CkHashtable_GetKeys(this.swigCPtr, this, CkStringTable.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public int LookupInt(String var1) {
/*  86 */     return chilkatJNI.CkHashtable_LookupInt(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean LookupStr(String var1, CkString var2) {
/*  90 */     return chilkatJNI.CkHashtable_LookupStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String lookupStr(String var1) {
/*  94 */     return chilkatJNI.CkHashtable_lookupStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Remove(String var1) {
/*  98 */     return chilkatJNI.CkHashtable_Remove(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ToXmlSb(CkStringBuilder var1) {
/* 102 */     return chilkatJNI.CkHashtable_ToXmlSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkHashtable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */