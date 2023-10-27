/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkDtObj
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkDtObj(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkDtObj var0) {
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
/*  29 */         chilkatJNI.delete_CkDtObj(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkDtObj()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkDtObj(), true);
/*     */   }
/*     */   
/*     */   public boolean get_Utc() {
/*  42 */     return chilkatJNI.CkDtObj_get_Utc(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Utc(boolean var1) {
/*  46 */     chilkatJNI.CkDtObj_put_Utc(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Year() {
/*  50 */     return chilkatJNI.CkDtObj_get_Year(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Year(int var1) {
/*  54 */     chilkatJNI.CkDtObj_put_Year(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Month() {
/*  58 */     return chilkatJNI.CkDtObj_get_Month(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Month(int var1) {
/*  62 */     chilkatJNI.CkDtObj_put_Month(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Day() {
/*  66 */     return chilkatJNI.CkDtObj_get_Day(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Day(int var1) {
/*  70 */     chilkatJNI.CkDtObj_put_Day(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Hour() {
/*  74 */     return chilkatJNI.CkDtObj_get_Hour(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Hour(int var1) {
/*  78 */     chilkatJNI.CkDtObj_put_Hour(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Minute() {
/*  82 */     return chilkatJNI.CkDtObj_get_Minute(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Minute(int var1) {
/*  86 */     chilkatJNI.CkDtObj_put_Minute(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_Second() {
/*  90 */     return chilkatJNI.CkDtObj_get_Second(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_Second(int var1) {
/*  94 */     chilkatJNI.CkDtObj_put_Second(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_StructTmYear() {
/*  98 */     return chilkatJNI.CkDtObj_get_StructTmYear(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StructTmYear(int var1) {
/* 102 */     chilkatJNI.CkDtObj_put_StructTmYear(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int get_StructTmMonth() {
/* 106 */     return chilkatJNI.CkDtObj_get_StructTmMonth(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_StructTmMonth(int var1) {
/* 110 */     chilkatJNI.CkDtObj_put_StructTmMonth(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean Serialize(CkString var1) {
/* 114 */     return chilkatJNI.CkDtObj_Serialize(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serialize() {
/* 118 */     return chilkatJNI.CkDtObj_serialize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void DeSerialize(String var1) {
/* 122 */     chilkatJNI.CkDtObj_DeSerialize(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkDtObj.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */