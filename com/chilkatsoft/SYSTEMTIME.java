/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class SYSTEMTIME
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected SYSTEMTIME(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(SYSTEMTIME var0) {
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
/*  29 */         chilkatJNI.delete_SYSTEMTIME(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setWYear(int var1)
/*     */   {
/*  38 */     chilkatJNI.SYSTEMTIME_wYear_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWYear() {
/*  42 */     return chilkatJNI.SYSTEMTIME_wYear_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWMonth(int var1) {
/*  46 */     chilkatJNI.SYSTEMTIME_wMonth_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWMonth() {
/*  50 */     return chilkatJNI.SYSTEMTIME_wMonth_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWDayOfWeek(int var1) {
/*  54 */     chilkatJNI.SYSTEMTIME_wDayOfWeek_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWDayOfWeek() {
/*  58 */     return chilkatJNI.SYSTEMTIME_wDayOfWeek_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWDay(int var1) {
/*  62 */     chilkatJNI.SYSTEMTIME_wDay_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWDay() {
/*  66 */     return chilkatJNI.SYSTEMTIME_wDay_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWHour(int var1) {
/*  70 */     chilkatJNI.SYSTEMTIME_wHour_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWHour() {
/*  74 */     return chilkatJNI.SYSTEMTIME_wHour_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWMinute(int var1) {
/*  78 */     chilkatJNI.SYSTEMTIME_wMinute_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWMinute() {
/*  82 */     return chilkatJNI.SYSTEMTIME_wMinute_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWSecond(int var1) {
/*  86 */     chilkatJNI.SYSTEMTIME_wSecond_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWSecond() {
/*  90 */     return chilkatJNI.SYSTEMTIME_wSecond_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void setWMilliseconds(int var1) {
/*  94 */     chilkatJNI.SYSTEMTIME_wMilliseconds_set(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int getWMilliseconds() {
/*  98 */     return chilkatJNI.SYSTEMTIME_wMilliseconds_get(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public SYSTEMTIME() {
/* 102 */     this(chilkatJNI.new_SYSTEMTIME(), true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\SYSTEMTIME.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */