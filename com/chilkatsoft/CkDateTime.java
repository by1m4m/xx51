/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkDateTime
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkDateTime(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkDateTime var0) {
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
/*  29 */         chilkatJNI.delete_CkDateTime(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkDateTime()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkDateTime(), true);
/*     */   }
/*     */   
/*     */   public int get_IsDst() {
/*  42 */     return chilkatJNI.CkDateTime_get_IsDst(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_UtcOffset() {
/*  46 */     return chilkatJNI.CkDateTime_get_UtcOffset(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void SetFromCurrentSystemTime() {
/*  50 */     chilkatJNI.CkDateTime_SetFromCurrentSystemTime(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean SetFromRfc822(String var1) {
/*  54 */     return chilkatJNI.CkDateTime_SetFromRfc822(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetAsUnixTime(boolean var1) {
/*  58 */     return chilkatJNI.CkDateTime_GetAsUnixTime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long GetAsUnixTime64(boolean var1) {
/*  62 */     return chilkatJNI.CkDateTime_GetAsUnixTime64(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public double GetAsOleDate(boolean var1) {
/*  66 */     return chilkatJNI.CkDateTime_GetAsOleDate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int GetAsDosDate(boolean var1) {
/*  70 */     return chilkatJNI.CkDateTime_GetAsDosDate(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public long GetAsDateTimeTicks(boolean var1) {
/*  74 */     return chilkatJNI.CkDateTime_GetAsDateTimeTicks(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void SetFromUnixTime(boolean var1, int var2) {
/*  78 */     chilkatJNI.CkDateTime_SetFromUnixTime(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetFromUnixTime64(boolean var1, long var2) {
/*  82 */     chilkatJNI.CkDateTime_SetFromUnixTime64(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetFromOleDate(boolean var1, double var2) {
/*  86 */     chilkatJNI.CkDateTime_SetFromOleDate(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetFromDosDate(boolean var1, int var2) {
/*  90 */     chilkatJNI.CkDateTime_SetFromDosDate(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public void SetFromDateTimeTicks(boolean var1, long var2) {
/*  94 */     chilkatJNI.CkDateTime_SetFromDateTimeTicks(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean Serialize(CkString var1) {
/*  98 */     return chilkatJNI.CkDateTime_Serialize(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String serialize() {
/* 102 */     return chilkatJNI.CkDateTime_serialize(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void DeSerialize(String var1) {
/* 106 */     chilkatJNI.CkDateTime_DeSerialize(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddDays(int var1) {
/* 110 */     return chilkatJNI.CkDateTime_AddDays(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean GetAsRfc822(boolean var1, CkString var2) {
/* 114 */     return chilkatJNI.CkDateTime_GetAsRfc822(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAsRfc822(boolean var1) {
/* 118 */     return chilkatJNI.CkDateTime_getAsRfc822(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 122 */     return chilkatJNI.CkDateTime_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 126 */     chilkatJNI.CkDateTime_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean AddSeconds(int var1) {
/* 130 */     return chilkatJNI.CkDateTime_AddSeconds(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public int DiffSeconds(CkDateTime var1) {
/* 134 */     return chilkatJNI.CkDateTime_DiffSeconds(this.swigCPtr, this, getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean GetAsUnixTimeStr(boolean var1, CkString var2) {
/* 138 */     return chilkatJNI.CkDateTime_GetAsUnixTimeStr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public boolean GetAsIso8601(String var1, boolean var2, CkString var3) {
/* 142 */     return chilkatJNI.CkDateTime_GetAsIso8601(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*     */   }
/*     */   
/*     */   public boolean GetAsTimestamp(boolean var1, CkString var2) {
/* 146 */     return chilkatJNI.CkDateTime_GetAsTimestamp(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*     */   }
/*     */   
/*     */   public String getAsUnixTimeStr(boolean var1) {
/* 150 */     return chilkatJNI.CkDateTime_getAsUnixTimeStr(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public String getAsIso8601(String var1, boolean var2) {
/* 154 */     return chilkatJNI.CkDateTime_getAsIso8601(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public String getAsTimestamp(boolean var1) {
/* 158 */     return chilkatJNI.CkDateTime_getAsTimestamp(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean ExpiresWithin(int var1, String var2) {
/* 162 */     return chilkatJNI.CkDateTime_ExpiresWithin(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public boolean OlderThan(int var1, String var2) {
/* 166 */     return chilkatJNI.CkDateTime_OlderThan(this.swigCPtr, this, var1, var2);
/*     */   }
/*     */   
/*     */   public CkDtObj GetDtObj(boolean var1) {
/* 170 */     long var2 = chilkatJNI.CkDateTime_GetDtObj(this.swigCPtr, this, var1);
/* 171 */     return var2 == 0L ? null : new CkDtObj(var2, false);
/*     */   }
/*     */   
/*     */   public boolean SetFromDtObj(CkDtObj var1) {
/* 175 */     return chilkatJNI.CkDateTime_SetFromDtObj(this.swigCPtr, this, CkDtObj.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean LoadTaskResult(CkTask var1) {
/* 179 */     return chilkatJNI.CkDateTime_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public boolean SetFromNtpTime(int var1) {
/* 183 */     return chilkatJNI.CkDateTime_SetFromNtpTime(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public boolean SetFromTimestamp(String var1) {
/* 187 */     return chilkatJNI.CkDateTime_SetFromTimestamp(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkDateTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */