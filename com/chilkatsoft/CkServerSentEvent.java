/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkServerSentEvent
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */   protected transient boolean swigCMemOwn;
/*    */   
/*    */ 
/*    */   protected CkServerSentEvent(long var1, boolean var3)
/*    */   {
/* 13 */     this.swigCMemOwn = var3;
/* 14 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkServerSentEvent var0) {
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
/* 29 */         chilkatJNI.delete_CkServerSentEvent(this.swigCPtr);
/*    */       }
/*    */       
/* 32 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */   
/*    */   public CkServerSentEvent()
/*    */   {
/* 38 */     this(chilkatJNI.new_CkServerSentEvent(), true);
/*    */   }
/*    */   
/*    */   public void get_Data(CkString var1) {
/* 42 */     chilkatJNI.CkServerSentEvent_get_Data(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String data() {
/* 46 */     return chilkatJNI.CkServerSentEvent_data(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void get_EventName(CkString var1) {
/* 50 */     chilkatJNI.CkServerSentEvent_get_EventName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String eventName() {
/* 54 */     return chilkatJNI.CkServerSentEvent_eventName(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void get_LastEventId(CkString var1) {
/* 58 */     chilkatJNI.CkServerSentEvent_get_LastEventId(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String lastEventId() {
/* 62 */     return chilkatJNI.CkServerSentEvent_lastEventId(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean get_LastMethodSuccess() {
/* 66 */     return chilkatJNI.CkServerSentEvent_get_LastMethodSuccess(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public void put_LastMethodSuccess(boolean var1) {
/* 70 */     chilkatJNI.CkServerSentEvent_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public int get_Retry() {
/* 74 */     return chilkatJNI.CkServerSentEvent_get_Retry(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public boolean LoadEvent(String var1) {
/* 78 */     return chilkatJNI.CkServerSentEvent_LoadEvent(this.swigCPtr, this, var1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkServerSentEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */