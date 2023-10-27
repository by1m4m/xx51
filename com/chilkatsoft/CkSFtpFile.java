/*     */ package com.chilkatsoft;
/*     */ 
/*     */ 
/*     */ public class CkSFtpFile
/*     */ {
/*     */   private transient long swigCPtr;
/*     */   
/*     */   protected transient boolean swigCMemOwn;
/*     */   
/*     */ 
/*     */   protected CkSFtpFile(long var1, boolean var3)
/*     */   {
/*  13 */     this.swigCMemOwn = var3;
/*  14 */     this.swigCPtr = var1;
/*     */   }
/*     */   
/*     */   protected static long getCPtr(CkSFtpFile var0) {
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
/*  29 */         chilkatJNI.delete_CkSFtpFile(this.swigCPtr);
/*     */       }
/*     */       
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */   
/*     */   public CkSFtpFile()
/*     */   {
/*  38 */     this(chilkatJNI.new_CkSFtpFile(), true);
/*     */   }
/*     */   
/*     */   public void get_CreateTime(SYSTEMTIME var1) {
/*  42 */     chilkatJNI.CkSFtpFile_get_CreateTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_CreateTimeStr(CkString var1) {
/*  46 */     chilkatJNI.CkSFtpFile_get_CreateTimeStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String createTimeStr() {
/*  50 */     return chilkatJNI.CkSFtpFile_createTimeStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Filename(CkString var1) {
/*  54 */     chilkatJNI.CkSFtpFile_get_Filename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String filename() {
/*  58 */     return chilkatJNI.CkSFtpFile_filename(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_FileType(CkString var1) {
/*  62 */     chilkatJNI.CkSFtpFile_get_FileType(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String fileType() {
/*  66 */     return chilkatJNI.CkSFtpFile_fileType(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Gid() {
/*  70 */     return chilkatJNI.CkSFtpFile_get_Gid(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Group(CkString var1) {
/*  74 */     chilkatJNI.CkSFtpFile_get_Group(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String group() {
/*  78 */     return chilkatJNI.CkSFtpFile_group(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsAppendOnly() {
/*  82 */     return chilkatJNI.CkSFtpFile_get_IsAppendOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsArchive() {
/*  86 */     return chilkatJNI.CkSFtpFile_get_IsArchive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsCaseInsensitive() {
/*  90 */     return chilkatJNI.CkSFtpFile_get_IsCaseInsensitive(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsCompressed() {
/*  94 */     return chilkatJNI.CkSFtpFile_get_IsCompressed(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsDirectory() {
/*  98 */     return chilkatJNI.CkSFtpFile_get_IsDirectory(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsEncrypted() {
/* 102 */     return chilkatJNI.CkSFtpFile_get_IsEncrypted(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsHidden() {
/* 106 */     return chilkatJNI.CkSFtpFile_get_IsHidden(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsImmutable() {
/* 110 */     return chilkatJNI.CkSFtpFile_get_IsImmutable(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsReadOnly() {
/* 114 */     return chilkatJNI.CkSFtpFile_get_IsReadOnly(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsRegular() {
/* 118 */     return chilkatJNI.CkSFtpFile_get_IsRegular(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsSparse() {
/* 122 */     return chilkatJNI.CkSFtpFile_get_IsSparse(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsSymLink() {
/* 126 */     return chilkatJNI.CkSFtpFile_get_IsSymLink(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsSync() {
/* 130 */     return chilkatJNI.CkSFtpFile_get_IsSync(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_IsSystem() {
/* 134 */     return chilkatJNI.CkSFtpFile_get_IsSystem(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_LastAccessTime(SYSTEMTIME var1) {
/* 138 */     chilkatJNI.CkSFtpFile_get_LastAccessTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_LastAccessTimeStr(CkString var1) {
/* 142 */     chilkatJNI.CkSFtpFile_get_LastAccessTimeStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastAccessTimeStr() {
/* 146 */     return chilkatJNI.CkSFtpFile_lastAccessTimeStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public boolean get_LastMethodSuccess() {
/* 150 */     return chilkatJNI.CkSFtpFile_get_LastMethodSuccess(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void put_LastMethodSuccess(boolean var1) {
/* 154 */     chilkatJNI.CkSFtpFile_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*     */   }
/*     */   
/*     */   public void get_LastModifiedTime(SYSTEMTIME var1) {
/* 158 */     chilkatJNI.CkSFtpFile_get_LastModifiedTime(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public void get_LastModifiedTimeStr(CkString var1) {
/* 162 */     chilkatJNI.CkSFtpFile_get_LastModifiedTimeStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String lastModifiedTimeStr() {
/* 166 */     return chilkatJNI.CkSFtpFile_lastModifiedTimeStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_Owner(CkString var1) {
/* 170 */     chilkatJNI.CkSFtpFile_get_Owner(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String owner() {
/* 174 */     return chilkatJNI.CkSFtpFile_owner(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Permissions() {
/* 178 */     return chilkatJNI.CkSFtpFile_get_Permissions(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Size32() {
/* 182 */     return chilkatJNI.CkSFtpFile_get_Size32(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public void get_SizeStr(CkString var1) {
/* 186 */     chilkatJNI.CkSFtpFile_get_SizeStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*     */   }
/*     */   
/*     */   public String sizeStr() {
/* 190 */     return chilkatJNI.CkSFtpFile_sizeStr(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public int get_Uid() {
/* 194 */     return chilkatJNI.CkSFtpFile_get_Uid(this.swigCPtr, this);
/*     */   }
/*     */   
/*     */   public CkDateTime GetCreateDt() {
/* 198 */     long var1 = chilkatJNI.CkSFtpFile_GetCreateDt(this.swigCPtr, this);
/* 199 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public CkDateTime GetLastAccessDt() {
/* 203 */     long var1 = chilkatJNI.CkSFtpFile_GetLastAccessDt(this.swigCPtr, this);
/* 204 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */   
/*     */   public CkDateTime GetLastModifiedDt() {
/* 208 */     long var1 = chilkatJNI.CkSFtpFile_GetLastModifiedDt(this.swigCPtr, this);
/* 209 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkSFtpFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */