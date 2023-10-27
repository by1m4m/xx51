/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkUrl
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkUrl(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkUrl var0) {
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
/*  29 */         chilkatJNI.delete_CkUrl(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkUrl()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkUrl(), true);
/*     */   }
/*     */   
/*     */   public void get_Frag(CkString var1) {
/*  42 */     chilkatJNI.CkUrl_get_Frag(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String frag() {
/*  46 */     return chilkatJNI.CkUrl_frag(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Host(CkString var1) {
/*  50 */     chilkatJNI.CkUrl_get_Host(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String host() {
/*  54 */     return chilkatJNI.CkUrl_host(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_HostType(CkString var1) {
/*  58 */     chilkatJNI.CkUrl_get_HostType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String hostType() {
/*  62 */     return chilkatJNI.CkUrl_hostType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/*  66 */     return chilkatJNI.CkUrl_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/*  70 */     chilkatJNI.CkUrl_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_Login(CkString var1) {
/*  74 */     chilkatJNI.CkUrl_get_Login(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String login() {
/*  78 */     return chilkatJNI.CkUrl_login(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Password(CkString var1) {
/*  82 */     chilkatJNI.CkUrl_get_Password(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String password() {
/*  86 */     return chilkatJNI.CkUrl_password(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Path(CkString var1) {
/*  90 */     chilkatJNI.CkUrl_get_Path(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String path() {
/*  94 */     return chilkatJNI.CkUrl_path(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_PathWithQueryParams(CkString var1) {
/*  98 */     chilkatJNI.CkUrl_get_PathWithQueryParams(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String pathWithQueryParams() {
/* 102 */     return chilkatJNI.CkUrl_pathWithQueryParams(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Port() {
/* 106 */     return chilkatJNI.CkUrl_get_Port(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Query(CkString var1) {
/* 110 */     chilkatJNI.CkUrl_get_Query(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String query() {
/* 114 */     return chilkatJNI.CkUrl_query(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_Ssl() {
/* 118 */     return chilkatJNI.CkUrl_get_Ssl(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean ParseUrl(String var1) {
/* 122 */     return chilkatJNI.CkUrl_ParseUrl(this.swigCPtr, this, var1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */