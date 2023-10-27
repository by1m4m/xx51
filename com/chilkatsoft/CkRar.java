/*    */ package com.chilkatsoft;
/*    */ 
/*    */ 
/*    */ public class CkRar
/*    */ {
/*    */   private transient long swigCPtr;
/*    */   
/*    */   protected transient boolean swigCMemOwn;
/*    */   
/*    */ 
/*    */   protected CkRar(long var1, boolean var3)
/*    */   {
/* 13 */     this.swigCMemOwn = var3;
/* 14 */     this.swigCPtr = var1;
/*    */   }
/*    */   
/*    */   protected static long getCPtr(CkRar var0) {
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
/* 29 */         chilkatJNI.delete_CkRar(this.swigCPtr);
/*    */       }
/*    */       
/* 32 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */   
/*    */   public CkRar()
/*    */   {
/* 38 */     this(chilkatJNI.new_CkRar(), true);
/*    */   }
/*    */   
/*    */   public boolean Unrar(String var1) {
/* 42 */     return chilkatJNI.CkRar_Unrar(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean FastOpen(String var1) {
/* 46 */     return chilkatJNI.CkRar_FastOpen(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean Open(String var1) {
/* 50 */     return chilkatJNI.CkRar_Open(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public boolean Close() {
/* 54 */     return chilkatJNI.CkRar_Close(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public int get_NumEntries() {
/* 58 */     return chilkatJNI.CkRar_get_NumEntries(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public CkRarEntry GetEntryByName(String var1) {
/* 62 */     long var2 = chilkatJNI.CkRar_GetEntryByName(this.swigCPtr, this, var1);
/* 63 */     return var2 == 0L ? null : new CkRarEntry(var2, true);
/*    */   }
/*    */   
/*    */   public CkRarEntry GetEntryByIndex(int var1) {
/* 67 */     long var2 = chilkatJNI.CkRar_GetEntryByIndex(this.swigCPtr, this, var1);
/* 68 */     return var2 == 0L ? null : new CkRarEntry(var2, true);
/*    */   }
/*    */   
/*    */   public boolean SaveLastError(String var1) {
/* 72 */     return chilkatJNI.CkRar_SaveLastError(this.swigCPtr, this, var1);
/*    */   }
/*    */   
/*    */   public void LastErrorXml(CkString var1) {
/* 76 */     chilkatJNI.CkRar_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public void LastErrorHtml(CkString var1) {
/* 80 */     chilkatJNI.CkRar_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public void LastErrorText(CkString var1) {
/* 84 */     chilkatJNI.CkRar_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*    */   }
/*    */   
/*    */   public String lastErrorText() {
/* 88 */     return chilkatJNI.CkRar_lastErrorText(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public String lastErrorXml() {
/* 92 */     return chilkatJNI.CkRar_lastErrorXml(this.swigCPtr, this);
/*    */   }
/*    */   
/*    */   public String lastErrorHtml() {
/* 96 */     return chilkatJNI.CkRar_lastErrorHtml(this.swigCPtr, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkRar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */