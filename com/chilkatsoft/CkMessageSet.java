/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkMessageSet
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */   protected transient boolean swigCMemOwn;
/*    */   
/*    */ 
/*    */   protected CkMessageSet(long var1, boolean var3)
/*    */   {
/* 13 */     this.swigCMemOwn = var3;
/* 14 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkMessageSet var0) {
/* 18 */     return var0 == null ? 0L : var0.swigCPtr;
/*    */   }
/*    */   
/*    */   protected void finalize() {
/* 22 */     delete();
/*    */   }
/*    */   
/*    */   public synchronized void delete() {
/* 26 */     if (this.swigCPtr != 0L) {
/* 27 */       if (this.swigCMemOwn) {
/* 28 */         this.swigCMemOwn = false;
/* 29 */         chilkatJNI.delete_CkMessageSet(this.swigCPtr);
/*    */       }
/*    */       
/* 32 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */   
/*    */   public CkMessageSet()
/*    */   {
/* 38 */     this(chilkatJNI.new_CkMessageSet(), true);
/*    */   }
/*    */   
/*    */   public int get_Count() {
/* 42 */     return chilkatJNI.CkMessageSet_get_Count(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean get_HasUids() {
/* 46 */     return chilkatJNI.CkMessageSet_get_HasUids(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void put_HasUids(boolean var1) {
/* 50 */     chilkatJNI.CkMessageSet_put_HasUids(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean get_LastMethodSuccess() {
/* 54 */     return chilkatJNI.CkMessageSet_get_LastMethodSuccess(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void put_LastMethodSuccess(boolean var1) {
/* 58 */     chilkatJNI.CkMessageSet_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean ContainsId(int var1) {
/* 62 */     return chilkatJNI.CkMessageSet_ContainsId(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean FromCompactString(String var1) {
/* 66 */     return chilkatJNI.CkMessageSet_FromCompactString(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public int GetId(int var1) {
/* 70 */     return chilkatJNI.CkMessageSet_GetId(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public void InsertId(int var1) {
/* 74 */     chilkatJNI.CkMessageSet_InsertId(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean LoadTaskResult(CkTask var1) {
/* 78 */     return chilkatJNI.CkMessageSet_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public void RemoveId(int var1) {
/* 82 */     chilkatJNI.CkMessageSet_RemoveId(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean ToCommaSeparatedStr(CkString var1) {
/* 86 */     return chilkatJNI.CkMessageSet_ToCommaSeparatedStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String toCommaSeparatedStr() {
/* 90 */     return chilkatJNI.CkMessageSet_toCommaSeparatedStr(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean ToCompactString(CkString var1) {
/* 94 */     return chilkatJNI.CkMessageSet_ToCompactString(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String toCompactString() {
/* 98 */     return chilkatJNI.CkMessageSet_toCompactString(this.swigCPtr, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkMessageSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */