/*      */ package com.chilkatsoft;
/*      */ 
/*      */ 
/*      */ public class CkEmail
/*      */ {
/*      */   private transient long swigCPtr;
/*      */   
/*      */   protected transient boolean swigCMemOwn;
/*      */   
/*      */ 
/*      */   protected CkEmail(long var1, boolean var3)
/*      */   {
/*   13 */     this.swigCMemOwn = var3;
/*   14 */     this.swigCPtr = var1;
/*      */   }
/*      */   
/*      */   protected static long getCPtr(CkEmail var0) {
/*   18 */     return var0 == null ? 0L : var0.swigCPtr;
/*      */   }
/*      */   
/*      */   protected void finalize() {
/*   22 */     delete();
/*      */   }
/*      */   
/*      */   public synchronized void delete() {
/*   26 */     if (this.swigCPtr != 0L) {
/*   27 */       if (this.swigCMemOwn) {
/*   28 */         this.swigCMemOwn = false;
/*   29 */         chilkatJNI.delete_CkEmail(this.swigCPtr);
/*      */       }
/*      */       
/*   32 */       this.swigCPtr = 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   public CkEmail()
/*      */   {
/*   38 */     this(chilkatJNI.new_CkEmail(), true);
/*      */   }
/*      */   
/*      */   public void LastErrorXml(CkString var1) {
/*   42 */     chilkatJNI.CkEmail_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorHtml(CkString var1) {
/*   46 */     chilkatJNI.CkEmail_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void LastErrorText(CkString var1) {
/*   50 */     chilkatJNI.CkEmail_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_Body(CkString var1) {
/*   54 */     chilkatJNI.CkEmail_get_Body(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String body() {
/*   58 */     return chilkatJNI.CkEmail_body(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Body(String var1) {
/*   62 */     chilkatJNI.CkEmail_put_Body(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_BounceAddress(CkString var1) {
/*   66 */     chilkatJNI.CkEmail_get_BounceAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String bounceAddress() {
/*   70 */     return chilkatJNI.CkEmail_bounceAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_BounceAddress(String var1) {
/*   74 */     chilkatJNI.CkEmail_put_BounceAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Charset(CkString var1) {
/*   78 */     chilkatJNI.CkEmail_get_Charset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String charset() {
/*   82 */     return chilkatJNI.CkEmail_charset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Charset(String var1) {
/*   86 */     chilkatJNI.CkEmail_put_Charset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_DebugLogFilePath(CkString var1) {
/*   90 */     chilkatJNI.CkEmail_get_DebugLogFilePath(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String debugLogFilePath() {
/*   94 */     return chilkatJNI.CkEmail_debugLogFilePath(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_DebugLogFilePath(String var1) {
/*   98 */     chilkatJNI.CkEmail_put_DebugLogFilePath(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_Decrypted() {
/*  102 */     return chilkatJNI.CkEmail_get_Decrypted(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_EmailDate(SYSTEMTIME var1) {
/*  106 */     chilkatJNI.CkEmail_get_EmailDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_EmailDate(SYSTEMTIME var1) {
/*  110 */     chilkatJNI.CkEmail_put_EmailDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_EmailDateStr(CkString var1) {
/*  114 */     chilkatJNI.CkEmail_get_EmailDateStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String emailDateStr() {
/*  118 */     return chilkatJNI.CkEmail_emailDateStr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_EmailDateStr(String var1) {
/*  122 */     chilkatJNI.CkEmail_put_EmailDateStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_EncryptedBy(CkString var1) {
/*  126 */     chilkatJNI.CkEmail_get_EncryptedBy(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String encryptedBy() {
/*  130 */     return chilkatJNI.CkEmail_encryptedBy(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_FileDistList(CkString var1) {
/*  134 */     chilkatJNI.CkEmail_get_FileDistList(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String fileDistList() {
/*  138 */     return chilkatJNI.CkEmail_fileDistList(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FileDistList(String var1) {
/*  142 */     chilkatJNI.CkEmail_put_FileDistList(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_From(CkString var1) {
/*  146 */     chilkatJNI.CkEmail_get_From(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String ck_from() {
/*  150 */     return chilkatJNI.CkEmail_ck_from(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_From(String var1) {
/*  154 */     chilkatJNI.CkEmail_put_From(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_FromAddress(CkString var1) {
/*  158 */     chilkatJNI.CkEmail_get_FromAddress(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String fromAddress() {
/*  162 */     return chilkatJNI.CkEmail_fromAddress(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FromAddress(String var1) {
/*  166 */     chilkatJNI.CkEmail_put_FromAddress(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_FromName(CkString var1) {
/*  170 */     chilkatJNI.CkEmail_get_FromName(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String fromName() {
/*  174 */     return chilkatJNI.CkEmail_fromName(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_FromName(String var1) {
/*  178 */     chilkatJNI.CkEmail_put_FromName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Header(CkString var1) {
/*  182 */     chilkatJNI.CkEmail_get_Header(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String header() {
/*  186 */     return chilkatJNI.CkEmail_header(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_Language(CkString var1) {
/*  190 */     chilkatJNI.CkEmail_get_Language(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String language() {
/*  194 */     return chilkatJNI.CkEmail_language(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorHtml(CkString var1) {
/*  198 */     chilkatJNI.CkEmail_get_LastErrorHtml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorHtml() {
/*  202 */     return chilkatJNI.CkEmail_lastErrorHtml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorText(CkString var1) {
/*  206 */     chilkatJNI.CkEmail_get_LastErrorText(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorText() {
/*  210 */     return chilkatJNI.CkEmail_lastErrorText(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_LastErrorXml(CkString var1) {
/*  214 */     chilkatJNI.CkEmail_get_LastErrorXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String lastErrorXml() {
/*  218 */     return chilkatJNI.CkEmail_lastErrorXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_LastMethodSuccess() {
/*  222 */     return chilkatJNI.CkEmail_get_LastMethodSuccess(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LastMethodSuccess(boolean var1) {
/*  226 */     chilkatJNI.CkEmail_put_LastMethodSuccess(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_LocalDate(SYSTEMTIME var1) {
/*  230 */     chilkatJNI.CkEmail_get_LocalDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void put_LocalDate(SYSTEMTIME var1) {
/*  234 */     chilkatJNI.CkEmail_put_LocalDate(this.swigCPtr, this, SYSTEMTIME.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void get_LocalDateStr(CkString var1) {
/*  238 */     chilkatJNI.CkEmail_get_LocalDateStr(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String localDateStr() {
/*  242 */     return chilkatJNI.CkEmail_localDateStr(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_LocalDateStr(String var1) {
/*  246 */     chilkatJNI.CkEmail_put_LocalDateStr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Mailer(CkString var1) {
/*  250 */     chilkatJNI.CkEmail_get_Mailer(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String mailer() {
/*  254 */     return chilkatJNI.CkEmail_mailer(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Mailer(String var1) {
/*  258 */     chilkatJNI.CkEmail_put_Mailer(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_NumAlternatives() {
/*  262 */     return chilkatJNI.CkEmail_get_NumAlternatives(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumAttachedMessages() {
/*  266 */     return chilkatJNI.CkEmail_get_NumAttachedMessages(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumAttachments() {
/*  270 */     return chilkatJNI.CkEmail_get_NumAttachments(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumBcc() {
/*  274 */     return chilkatJNI.CkEmail_get_NumBcc(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumCC() {
/*  278 */     return chilkatJNI.CkEmail_get_NumCC(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumDaysOld() {
/*  282 */     return chilkatJNI.CkEmail_get_NumDaysOld(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumDigests() {
/*  286 */     return chilkatJNI.CkEmail_get_NumDigests(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumHeaderFields() {
/*  290 */     return chilkatJNI.CkEmail_get_NumHeaderFields(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumRelatedItems() {
/*  294 */     return chilkatJNI.CkEmail_get_NumRelatedItems(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumReplacePatterns() {
/*  298 */     return chilkatJNI.CkEmail_get_NumReplacePatterns(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumReports() {
/*  302 */     return chilkatJNI.CkEmail_get_NumReports(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int get_NumTo() {
/*  306 */     return chilkatJNI.CkEmail_get_NumTo(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_OaepHash(CkString var1) {
/*  310 */     chilkatJNI.CkEmail_get_OaepHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oaepHash() {
/*  314 */     return chilkatJNI.CkEmail_oaepHash(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepHash(String var1) {
/*  318 */     chilkatJNI.CkEmail_put_OaepHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_OaepMgfHash(CkString var1) {
/*  322 */     chilkatJNI.CkEmail_get_OaepMgfHash(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String oaepMgfHash() {
/*  326 */     return chilkatJNI.CkEmail_oaepMgfHash(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepMgfHash(String var1) {
/*  330 */     chilkatJNI.CkEmail_put_OaepMgfHash(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_OaepPadding() {
/*  334 */     return chilkatJNI.CkEmail_get_OaepPadding(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OaepPadding(boolean var1) {
/*  338 */     chilkatJNI.CkEmail_put_OaepPadding(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_OverwriteExisting() {
/*  342 */     return chilkatJNI.CkEmail_get_OverwriteExisting(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_OverwriteExisting(boolean var1) {
/*  346 */     chilkatJNI.CkEmail_put_OverwriteExisting(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Pkcs7CryptAlg(CkString var1) {
/*  350 */     chilkatJNI.CkEmail_get_Pkcs7CryptAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String pkcs7CryptAlg() {
/*  354 */     return chilkatJNI.CkEmail_pkcs7CryptAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Pkcs7CryptAlg(String var1) {
/*  358 */     chilkatJNI.CkEmail_put_Pkcs7CryptAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Pkcs7KeyLength() {
/*  362 */     return chilkatJNI.CkEmail_get_Pkcs7KeyLength(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Pkcs7KeyLength(int var1) {
/*  366 */     chilkatJNI.CkEmail_put_Pkcs7KeyLength(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_PreferredCharset(CkString var1) {
/*  370 */     chilkatJNI.CkEmail_get_PreferredCharset(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String preferredCharset() {
/*  374 */     return chilkatJNI.CkEmail_preferredCharset(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PreferredCharset(String var1) {
/*  378 */     chilkatJNI.CkEmail_put_PreferredCharset(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_PrependHeaders() {
/*  382 */     return chilkatJNI.CkEmail_get_PrependHeaders(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_PrependHeaders(boolean var1) {
/*  386 */     chilkatJNI.CkEmail_put_PrependHeaders(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ReceivedEncrypted() {
/*  390 */     return chilkatJNI.CkEmail_get_ReceivedEncrypted(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_ReceivedSigned() {
/*  394 */     return chilkatJNI.CkEmail_get_ReceivedSigned(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_ReplyTo(CkString var1) {
/*  398 */     chilkatJNI.CkEmail_get_ReplyTo(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String replyTo() {
/*  402 */     return chilkatJNI.CkEmail_replyTo(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReplyTo(String var1) {
/*  406 */     chilkatJNI.CkEmail_put_ReplyTo(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_ReturnReceipt() {
/*  410 */     return chilkatJNI.CkEmail_get_ReturnReceipt(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_ReturnReceipt(boolean var1) {
/*  414 */     chilkatJNI.CkEmail_put_ReturnReceipt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SendEncrypted() {
/*  418 */     return chilkatJNI.CkEmail_get_SendEncrypted(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendEncrypted(boolean var1) {
/*  422 */     chilkatJNI.CkEmail_put_SendEncrypted(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Sender(CkString var1) {
/*  426 */     chilkatJNI.CkEmail_get_Sender(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String sender() {
/*  430 */     return chilkatJNI.CkEmail_sender(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Sender(String var1) {
/*  434 */     chilkatJNI.CkEmail_put_Sender(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SendSigned() {
/*  438 */     return chilkatJNI.CkEmail_get_SendSigned(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SendSigned(boolean var1) {
/*  442 */     chilkatJNI.CkEmail_put_SendSigned(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_SignaturesValid() {
/*  446 */     return chilkatJNI.CkEmail_get_SignaturesValid(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SignedBy(CkString var1) {
/*  450 */     chilkatJNI.CkEmail_get_SignedBy(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String signedBy() {
/*  454 */     return chilkatJNI.CkEmail_signedBy(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_SigningAlg(CkString var1) {
/*  458 */     chilkatJNI.CkEmail_get_SigningAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String signingAlg() {
/*  462 */     return chilkatJNI.CkEmail_signingAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SigningAlg(String var1) {
/*  466 */     chilkatJNI.CkEmail_put_SigningAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_SigningHashAlg(CkString var1) {
/*  470 */     chilkatJNI.CkEmail_get_SigningHashAlg(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String signingHashAlg() {
/*  474 */     return chilkatJNI.CkEmail_signingHashAlg(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_SigningHashAlg(String var1) {
/*  478 */     chilkatJNI.CkEmail_put_SigningHashAlg(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public int get_Size() {
/*  482 */     return chilkatJNI.CkEmail_get_Size(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void get_Subject(CkString var1) {
/*  486 */     chilkatJNI.CkEmail_get_Subject(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String subject() {
/*  490 */     return chilkatJNI.CkEmail_subject(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_Subject(String var1) {
/*  494 */     chilkatJNI.CkEmail_put_Subject(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Uidl(CkString var1) {
/*  498 */     chilkatJNI.CkEmail_get_Uidl(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String uidl() {
/*  502 */     return chilkatJNI.CkEmail_uidl(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean get_UnpackUseRelPaths() {
/*  506 */     return chilkatJNI.CkEmail_get_UnpackUseRelPaths(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_UnpackUseRelPaths(boolean var1) {
/*  510 */     chilkatJNI.CkEmail_put_UnpackUseRelPaths(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean get_VerboseLogging() {
/*  514 */     return chilkatJNI.CkEmail_get_VerboseLogging(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void put_VerboseLogging(boolean var1) {
/*  518 */     chilkatJNI.CkEmail_put_VerboseLogging(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void get_Version(CkString var1) {
/*  522 */     chilkatJNI.CkEmail_get_Version(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String version() {
/*  526 */     return chilkatJNI.CkEmail_version(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean AddAttachmentBd(String var1, CkBinData var2, String var3) {
/*  530 */     return chilkatJNI.CkEmail_AddAttachmentBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2, var3);
/*      */   }
/*      */   
/*      */   public void AddAttachmentHeader(int var1, String var2, String var3) {
/*  534 */     chilkatJNI.CkEmail_AddAttachmentHeader(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AddBcc(String var1, String var2) {
/*  538 */     return chilkatJNI.CkEmail_AddBcc(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddCC(String var1, String var2) {
/*  542 */     return chilkatJNI.CkEmail_AddCC(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddDataAttachment(String var1, CkByteData var2) {
/*  546 */     return chilkatJNI.CkEmail_AddDataAttachment(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean AddDataAttachment2(String var1, CkByteData var2, String var3) {
/*  550 */     return chilkatJNI.CkEmail_AddDataAttachment2(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AddEncryptCert(CkCert var1) {
/*  554 */     return chilkatJNI.CkEmail_AddEncryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean AddFileAttachment(String var1, CkString var2) {
/*  558 */     return chilkatJNI.CkEmail_AddFileAttachment(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String addFileAttachment(String var1) {
/*  562 */     return chilkatJNI.CkEmail_addFileAttachment(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddFileAttachment2(String var1, String var2) {
/*  566 */     return chilkatJNI.CkEmail_AddFileAttachment2(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void AddHeaderField(String var1, String var2) {
/*  570 */     chilkatJNI.CkEmail_AddHeaderField(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void AddHeaderField2(String var1, String var2) {
/*  574 */     chilkatJNI.CkEmail_AddHeaderField2(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddHtmlAlternativeBody(String var1) {
/*  578 */     return chilkatJNI.CkEmail_AddHtmlAlternativeBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddiCalendarAlternativeBody(String var1, String var2) {
/*  582 */     return chilkatJNI.CkEmail_AddiCalendarAlternativeBody(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddMultipleBcc(String var1) {
/*  586 */     return chilkatJNI.CkEmail_AddMultipleBcc(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddMultipleCC(String var1) {
/*  590 */     return chilkatJNI.CkEmail_AddMultipleCC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddMultipleTo(String var1) {
/*  594 */     return chilkatJNI.CkEmail_AddMultipleTo(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceData(CkByteData var1, String var2) {
/*  598 */     return chilkatJNI.CkEmail_AddPfxSourceData(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddPfxSourceFile(String var1, String var2) {
/*  602 */     return chilkatJNI.CkEmail_AddPfxSourceFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddPlainTextAlternativeBody(String var1) {
/*  606 */     return chilkatJNI.CkEmail_AddPlainTextAlternativeBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedBd(String var1, CkBinData var2, CkString var3) {
/*  610 */     return chilkatJNI.CkEmail_AddRelatedBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String addRelatedBd(String var1, CkBinData var2) {
/*  614 */     return chilkatJNI.CkEmail_addRelatedBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedBd2(CkBinData var1, String var2) {
/*  618 */     return chilkatJNI.CkEmail_AddRelatedBd2(this.swigCPtr, this, CkBinData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedData(String var1, CkByteData var2, CkString var3) {
/*  622 */     return chilkatJNI.CkEmail_AddRelatedData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String addRelatedData(String var1, CkByteData var2) {
/*  626 */     return chilkatJNI.CkEmail_addRelatedData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public void AddRelatedData2(CkByteData var1, String var2) {
/*  630 */     chilkatJNI.CkEmail_AddRelatedData2(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedFile(String var1, CkString var2) {
/*  634 */     return chilkatJNI.CkEmail_AddRelatedFile(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String addRelatedFile(String var1) {
/*  638 */     return chilkatJNI.CkEmail_addRelatedFile(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedFile2(String var1, String var2) {
/*  642 */     return chilkatJNI.CkEmail_AddRelatedFile2(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void AddRelatedHeader(int var1, String var2, String var3) {
/*  646 */     chilkatJNI.CkEmail_AddRelatedHeader(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AddRelatedString(String var1, String var2, String var3, CkString var4) {
/*  650 */     return chilkatJNI.CkEmail_AddRelatedString(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String addRelatedString(String var1, String var2, String var3) {
/*  654 */     return chilkatJNI.CkEmail_addRelatedString(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public void AddRelatedString2(String var1, String var2, String var3) {
/*  658 */     chilkatJNI.CkEmail_AddRelatedString2(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AddStringAttachment(String var1, String var2) {
/*  662 */     return chilkatJNI.CkEmail_AddStringAttachment(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AddStringAttachment2(String var1, String var2, String var3) {
/*  666 */     return chilkatJNI.CkEmail_AddStringAttachment2(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean AddTo(String var1, String var2) {
/*  670 */     return chilkatJNI.CkEmail_AddTo(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean AesDecrypt(String var1) {
/*  674 */     return chilkatJNI.CkEmail_AesDecrypt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AesEncrypt(String var1) {
/*  678 */     return chilkatJNI.CkEmail_AesEncrypt(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void AppendToBody(String var1) {
/*  682 */     chilkatJNI.CkEmail_AppendToBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean ApplyFixups(String var1) {
/*  686 */     return chilkatJNI.CkEmail_ApplyFixups(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean AspUnpack(String var1, String var2, String var3, boolean var4) {
/*  690 */     return chilkatJNI.CkEmail_AspUnpack(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean AspUnpack2(String var1, String var2, String var3, boolean var4, CkByteData var5) {
/*  694 */     return chilkatJNI.CkEmail_AspUnpack2(this.swigCPtr, this, var1, var2, var3, var4, CkByteData.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public boolean AttachMessage(CkByteData var1) {
/*  698 */     return chilkatJNI.CkEmail_AttachMessage(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean BEncodeBytes(CkByteData var1, String var2, CkString var3) {
/*  702 */     return chilkatJNI.CkEmail_BEncodeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String bEncodeBytes(CkByteData var1, String var2) {
/*  706 */     return chilkatJNI.CkEmail_bEncodeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean BEncodeString(String var1, String var2, CkString var3) {
/*  710 */     return chilkatJNI.CkEmail_BEncodeString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String bEncodeString(String var1, String var2) {
/*  714 */     return chilkatJNI.CkEmail_bEncodeString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void ClearBcc() {
/*  718 */     chilkatJNI.CkEmail_ClearBcc(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearCC() {
/*  722 */     chilkatJNI.CkEmail_ClearCC(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearEncryptCerts() {
/*  726 */     chilkatJNI.CkEmail_ClearEncryptCerts(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void ClearTo() {
/*  730 */     chilkatJNI.CkEmail_ClearTo(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkEmail Clone() {
/*  734 */     long var1 = chilkatJNI.CkEmail_Clone(this.swigCPtr, this);
/*  735 */     return var1 == 0L ? null : new CkEmail(var1, true);
/*      */   }
/*      */   
/*      */   public boolean ComputeGlobalKey(String var1, boolean var2, CkString var3) {
/*  739 */     return chilkatJNI.CkEmail_ComputeGlobalKey(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String computeGlobalKey(String var1, boolean var2) {
/*  743 */     return chilkatJNI.CkEmail_computeGlobalKey(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean ComputeGlobalKey2(String var1, boolean var2, CkString var3) {
/*  747 */     return chilkatJNI.CkEmail_ComputeGlobalKey2(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String computeGlobalKey2(String var1, boolean var2) {
/*  751 */     return chilkatJNI.CkEmail_computeGlobalKey2(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkEmail CreateDsn(String var1, String var2, boolean var3) {
/*  755 */     long var4 = chilkatJNI.CkEmail_CreateDsn(this.swigCPtr, this, var1, var2, var3);
/*  756 */     return var4 == 0L ? null : new CkEmail(var4, true);
/*      */   }
/*      */   
/*      */   public CkEmail CreateForward() {
/*  760 */     long var1 = chilkatJNI.CkEmail_CreateForward(this.swigCPtr, this);
/*  761 */     return var1 == 0L ? null : new CkEmail(var1, true);
/*      */   }
/*      */   
/*      */   public CkEmail CreateMdn(String var1, String var2, boolean var3) {
/*  765 */     long var4 = chilkatJNI.CkEmail_CreateMdn(this.swigCPtr, this, var1, var2, var3);
/*  766 */     return var4 == 0L ? null : new CkEmail(var4, true);
/*      */   }
/*      */   
/*      */   public CkEmail CreateReply() {
/*  770 */     long var1 = chilkatJNI.CkEmail_CreateReply(this.swigCPtr, this);
/*  771 */     return var1 == 0L ? null : new CkEmail(var1, true);
/*      */   }
/*      */   
/*      */   public boolean CreateTempMht(String var1, CkString var2) {
/*  775 */     return chilkatJNI.CkEmail_CreateTempMht(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String createTempMht(String var1) {
/*  779 */     return chilkatJNI.CkEmail_createTempMht(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void DropAttachments() {
/*  783 */     chilkatJNI.CkEmail_DropAttachments(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void DropRelatedItem(int var1) {
/*  787 */     chilkatJNI.CkEmail_DropRelatedItem(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void DropRelatedItems() {
/*  791 */     chilkatJNI.CkEmail_DropRelatedItems(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean DropSingleAttachment(int var1) {
/*  795 */     return chilkatJNI.CkEmail_DropSingleAttachment(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert FindIssuer(CkCert var1) {
/*  799 */     long var2 = chilkatJNI.CkEmail_FindIssuer(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*  800 */     return var2 == 0L ? null : new CkCert(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GenerateFilename(CkString var1) {
/*  804 */     return chilkatJNI.CkEmail_GenerateFilename(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String generateFilename() {
/*  808 */     return chilkatJNI.CkEmail_generateFilename(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean GetAlternativeBody(int var1, CkString var2) {
/*  812 */     return chilkatJNI.CkEmail_GetAlternativeBody(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAlternativeBody(int var1) {
/*  816 */     return chilkatJNI.CkEmail_getAlternativeBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String alternativeBody(int var1) {
/*  820 */     return chilkatJNI.CkEmail_alternativeBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAlternativeBodyBd(int var1, CkBinData var2) {
/*  824 */     return chilkatJNI.CkEmail_GetAlternativeBodyBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetAlternativeBodyByContentType(String var1, CkString var2) {
/*  828 */     return chilkatJNI.CkEmail_GetAlternativeBodyByContentType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAlternativeBodyByContentType(String var1) {
/*  832 */     return chilkatJNI.CkEmail_getAlternativeBodyByContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String alternativeBodyByContentType(String var1) {
/*  836 */     return chilkatJNI.CkEmail_alternativeBodyByContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAlternativeContentType(int var1, CkString var2) {
/*  840 */     return chilkatJNI.CkEmail_GetAlternativeContentType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAlternativeContentType(int var1) {
/*  844 */     return chilkatJNI.CkEmail_getAlternativeContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String alternativeContentType(int var1) {
/*  848 */     return chilkatJNI.CkEmail_alternativeContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAltHeaderField(int var1, String var2, CkString var3) {
/*  852 */     return chilkatJNI.CkEmail_GetAltHeaderField(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getAltHeaderField(int var1, String var2) {
/*  856 */     return chilkatJNI.CkEmail_getAltHeaderField(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String altHeaderField(int var1, String var2) {
/*  860 */     return chilkatJNI.CkEmail_altHeaderField(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public CkEmail GetAttachedMessage(int var1) {
/*  864 */     long var2 = chilkatJNI.CkEmail_GetAttachedMessage(this.swigCPtr, this, var1);
/*  865 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public boolean GetAttachedMessageAttr(int var1, String var2, String var3, CkString var4) {
/*  869 */     return chilkatJNI.CkEmail_GetAttachedMessageAttr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getAttachedMessageAttr(int var1, String var2, String var3) {
/*  873 */     return chilkatJNI.CkEmail_getAttachedMessageAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String attachedMessageAttr(int var1, String var2, String var3) {
/*  877 */     return chilkatJNI.CkEmail_attachedMessageAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean GetAttachedMessageFilename(int var1, CkString var2) {
/*  881 */     return chilkatJNI.CkEmail_GetAttachedMessageFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAttachedMessageFilename(int var1) {
/*  885 */     return chilkatJNI.CkEmail_getAttachedMessageFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String attachedMessageFilename(int var1) {
/*  889 */     return chilkatJNI.CkEmail_attachedMessageFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentAttr(int var1, String var2, String var3, CkString var4) {
/*  893 */     return chilkatJNI.CkEmail_GetAttachmentAttr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getAttachmentAttr(int var1, String var2, String var3) {
/*  897 */     return chilkatJNI.CkEmail_getAttachmentAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String attachmentAttr(int var1, String var2, String var3) {
/*  901 */     return chilkatJNI.CkEmail_attachmentAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentBd(int var1, CkBinData var2) {
/*  905 */     return chilkatJNI.CkEmail_GetAttachmentBd(this.swigCPtr, this, var1, CkBinData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentContentID(int var1, CkString var2) {
/*  909 */     return chilkatJNI.CkEmail_GetAttachmentContentID(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAttachmentContentID(int var1) {
/*  913 */     return chilkatJNI.CkEmail_getAttachmentContentID(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String attachmentContentID(int var1) {
/*  917 */     return chilkatJNI.CkEmail_attachmentContentID(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentContentType(int var1, CkString var2) {
/*  921 */     return chilkatJNI.CkEmail_GetAttachmentContentType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAttachmentContentType(int var1) {
/*  925 */     return chilkatJNI.CkEmail_getAttachmentContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String attachmentContentType(int var1) {
/*  929 */     return chilkatJNI.CkEmail_attachmentContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentData(int var1, CkByteData var2) {
/*  933 */     return chilkatJNI.CkEmail_GetAttachmentData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentFilename(int var1, CkString var2) {
/*  937 */     return chilkatJNI.CkEmail_GetAttachmentFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getAttachmentFilename(int var1) {
/*  941 */     return chilkatJNI.CkEmail_getAttachmentFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String attachmentFilename(int var1) {
/*  945 */     return chilkatJNI.CkEmail_attachmentFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentHeader(int var1, String var2, CkString var3) {
/*  949 */     return chilkatJNI.CkEmail_GetAttachmentHeader(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getAttachmentHeader(int var1, String var2) {
/*  953 */     return chilkatJNI.CkEmail_getAttachmentHeader(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String attachmentHeader(int var1, String var2) {
/*  957 */     return chilkatJNI.CkEmail_attachmentHeader(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public int GetAttachmentSize(int var1) {
/*  961 */     return chilkatJNI.CkEmail_GetAttachmentSize(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentString(int var1, String var2, CkString var3) {
/*  965 */     return chilkatJNI.CkEmail_GetAttachmentString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getAttachmentString(int var1, String var2) {
/*  969 */     return chilkatJNI.CkEmail_getAttachmentString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String attachmentString(int var1, String var2) {
/*  973 */     return chilkatJNI.CkEmail_attachmentString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GetAttachmentStringCrLf(int var1, String var2, CkString var3) {
/*  977 */     return chilkatJNI.CkEmail_GetAttachmentStringCrLf(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getAttachmentStringCrLf(int var1, String var2) {
/*  981 */     return chilkatJNI.CkEmail_getAttachmentStringCrLf(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String attachmentStringCrLf(int var1, String var2) {
/*  985 */     return chilkatJNI.CkEmail_attachmentStringCrLf(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GetBcc(int var1, CkString var2) {
/*  989 */     return chilkatJNI.CkEmail_GetBcc(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getBcc(int var1) {
/*  993 */     return chilkatJNI.CkEmail_getBcc(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String bcc(int var1) {
/*  997 */     return chilkatJNI.CkEmail_bcc(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetBccAddr(int var1, CkString var2) {
/* 1001 */     return chilkatJNI.CkEmail_GetBccAddr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getBccAddr(int var1) {
/* 1005 */     return chilkatJNI.CkEmail_getBccAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String bccAddr(int var1) {
/* 1009 */     return chilkatJNI.CkEmail_bccAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetBccName(int var1, CkString var2) {
/* 1013 */     return chilkatJNI.CkEmail_GetBccName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getBccName(int var1) {
/* 1017 */     return chilkatJNI.CkEmail_getBccName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String bccName(int var1) {
/* 1021 */     return chilkatJNI.CkEmail_bccName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetCC(int var1, CkString var2) {
/* 1025 */     return chilkatJNI.CkEmail_GetCC(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCC(int var1) {
/* 1029 */     return chilkatJNI.CkEmail_getCC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String cC(int var1) {
/* 1033 */     return chilkatJNI.CkEmail_cC(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetCcAddr(int var1, CkString var2) {
/* 1037 */     return chilkatJNI.CkEmail_GetCcAddr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCcAddr(int var1) {
/* 1041 */     return chilkatJNI.CkEmail_getCcAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String ccAddr(int var1) {
/* 1045 */     return chilkatJNI.CkEmail_ccAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetCcName(int var1, CkString var2) {
/* 1049 */     return chilkatJNI.CkEmail_GetCcName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getCcName(int var1) {
/* 1053 */     return chilkatJNI.CkEmail_getCcName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String ccName(int var1) {
/* 1057 */     return chilkatJNI.CkEmail_ccName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetDeliveryStatusInfo(String var1, CkString var2) {
/* 1061 */     return chilkatJNI.CkEmail_GetDeliveryStatusInfo(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getDeliveryStatusInfo(String var1) {
/* 1065 */     return chilkatJNI.CkEmail_getDeliveryStatusInfo(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String deliveryStatusInfo(String var1) {
/* 1069 */     return chilkatJNI.CkEmail_deliveryStatusInfo(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkEmail GetDigest(int var1) {
/* 1073 */     long var2 = chilkatJNI.CkEmail_GetDigest(this.swigCPtr, this, var1);
/* 1074 */     return var2 == 0L ? null : new CkEmail(var2, true);
/*      */   }
/*      */   
/*      */   public CkStringArray GetDsnFinalRecipients() {
/* 1078 */     long var1 = chilkatJNI.CkEmail_GetDsnFinalRecipients(this.swigCPtr, this);
/* 1079 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*      */   }
/*      */   
/*      */   public CkDateTime GetDt() {
/* 1083 */     long var1 = chilkatJNI.CkEmail_GetDt(this.swigCPtr, this);
/* 1084 */     return var1 == 0L ? null : new CkDateTime(var1, true);
/*      */   }
/*      */   
/*      */   public CkCert GetEncryptCert() {
/* 1088 */     long var1 = chilkatJNI.CkEmail_GetEncryptCert(this.swigCPtr, this);
/* 1089 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public CkCert GetEncryptedByCert() {
/* 1093 */     long var1 = chilkatJNI.CkEmail_GetEncryptedByCert(this.swigCPtr, this);
/* 1094 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetFileContent(String var1, CkByteData var2) {
/* 1098 */     return chilkatJNI.CkEmail_GetFileContent(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetHeaderField(String var1, CkString var2) {
/* 1102 */     return chilkatJNI.CkEmail_GetHeaderField(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getHeaderField(String var1) {
/* 1106 */     return chilkatJNI.CkEmail_getHeaderField(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String headerField(String var1) {
/* 1110 */     return chilkatJNI.CkEmail_headerField(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetHeaderFieldName(int var1, CkString var2) {
/* 1114 */     return chilkatJNI.CkEmail_GetHeaderFieldName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getHeaderFieldName(int var1) {
/* 1118 */     return chilkatJNI.CkEmail_getHeaderFieldName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String headerFieldName(int var1) {
/* 1122 */     return chilkatJNI.CkEmail_headerFieldName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetHeaderFieldValue(int var1, CkString var2) {
/* 1126 */     return chilkatJNI.CkEmail_GetHeaderFieldValue(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getHeaderFieldValue(int var1) {
/* 1130 */     return chilkatJNI.CkEmail_getHeaderFieldValue(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String headerFieldValue(int var1) {
/* 1134 */     return chilkatJNI.CkEmail_headerFieldValue(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetHtmlBody(CkString var1) {
/* 1138 */     return chilkatJNI.CkEmail_GetHtmlBody(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getHtmlBody() {
/* 1142 */     return chilkatJNI.CkEmail_getHtmlBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String htmlBody() {
/* 1146 */     return chilkatJNI.CkEmail_htmlBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public int GetImapUid() {
/* 1150 */     return chilkatJNI.CkEmail_GetImapUid(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public CkStringArray GetLinkedDomains() {
/* 1154 */     long var1 = chilkatJNI.CkEmail_GetLinkedDomains(this.swigCPtr, this);
/* 1155 */     return var1 == 0L ? null : new CkStringArray(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetMbHeaderField(String var1, String var2, CkByteData var3) {
/* 1159 */     return chilkatJNI.CkEmail_GetMbHeaderField(this.swigCPtr, this, var1, var2, CkByteData.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public boolean GetMbHtmlBody(String var1, CkByteData var2) {
/* 1163 */     return chilkatJNI.CkEmail_GetMbHtmlBody(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetMbPlainTextBody(String var1, CkByteData var2) {
/* 1167 */     return chilkatJNI.CkEmail_GetMbPlainTextBody(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetMime(CkString var1) {
/* 1171 */     return chilkatJNI.CkEmail_GetMime(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getMime() {
/* 1175 */     return chilkatJNI.CkEmail_getMime(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String mime() {
/* 1179 */     return chilkatJNI.CkEmail_mime(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean GetMimeBd(CkBinData var1) {
/* 1183 */     return chilkatJNI.CkEmail_GetMimeBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean GetMimeBinary(CkByteData var1) {
/* 1187 */     return chilkatJNI.CkEmail_GetMimeBinary(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean GetMimeSb(CkStringBuilder var1) {
/* 1191 */     return chilkatJNI.CkEmail_GetMimeSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean GetNthBinaryPartOfType(int var1, String var2, boolean var3, boolean var4, CkByteData var5) {
/* 1195 */     return chilkatJNI.CkEmail_GetNthBinaryPartOfType(this.swigCPtr, this, var1, var2, var3, var4, CkByteData.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public boolean GetNthTextPartOfType(int var1, String var2, boolean var3, boolean var4, CkString var5) {
/* 1199 */     return chilkatJNI.CkEmail_GetNthTextPartOfType(this.swigCPtr, this, var1, var2, var3, var4, CkString.getCPtr(var5), var5);
/*      */   }
/*      */   
/*      */   public String getNthTextPartOfType(int var1, String var2, boolean var3, boolean var4) {
/* 1203 */     return chilkatJNI.CkEmail_getNthTextPartOfType(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public String nthTextPartOfType(int var1, String var2, boolean var3, boolean var4) {
/* 1207 */     return chilkatJNI.CkEmail_nthTextPartOfType(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public int GetNumPartsOfType(String var1, boolean var2, boolean var3) {
/* 1211 */     return chilkatJNI.CkEmail_GetNumPartsOfType(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean GetPlainTextBody(CkString var1) {
/* 1215 */     return chilkatJNI.CkEmail_GetPlainTextBody(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getPlainTextBody() {
/* 1219 */     return chilkatJNI.CkEmail_getPlainTextBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String plainTextBody() {
/* 1223 */     return chilkatJNI.CkEmail_plainTextBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedAttr(int var1, String var2, String var3, CkString var4) {
/* 1227 */     return chilkatJNI.CkEmail_GetRelatedAttr(this.swigCPtr, this, var1, var2, var3, CkString.getCPtr(var4), var4);
/*      */   }
/*      */   
/*      */   public String getRelatedAttr(int var1, String var2, String var3) {
/* 1231 */     return chilkatJNI.CkEmail_getRelatedAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public String relatedAttr(int var1, String var2, String var3) {
/* 1235 */     return chilkatJNI.CkEmail_relatedAttr(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedContentID(int var1, CkString var2) {
/* 1239 */     return chilkatJNI.CkEmail_GetRelatedContentID(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRelatedContentID(int var1) {
/* 1243 */     return chilkatJNI.CkEmail_getRelatedContentID(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String relatedContentID(int var1) {
/* 1247 */     return chilkatJNI.CkEmail_relatedContentID(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedContentLocation(int var1, CkString var2) {
/* 1251 */     return chilkatJNI.CkEmail_GetRelatedContentLocation(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRelatedContentLocation(int var1) {
/* 1255 */     return chilkatJNI.CkEmail_getRelatedContentLocation(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String relatedContentLocation(int var1) {
/* 1259 */     return chilkatJNI.CkEmail_relatedContentLocation(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedContentType(int var1, CkString var2) {
/* 1263 */     return chilkatJNI.CkEmail_GetRelatedContentType(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRelatedContentType(int var1) {
/* 1267 */     return chilkatJNI.CkEmail_getRelatedContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String relatedContentType(int var1) {
/* 1271 */     return chilkatJNI.CkEmail_relatedContentType(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedData(int var1, CkByteData var2) {
/* 1275 */     return chilkatJNI.CkEmail_GetRelatedData(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedFilename(int var1, CkString var2) {
/* 1279 */     return chilkatJNI.CkEmail_GetRelatedFilename(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getRelatedFilename(int var1) {
/* 1283 */     return chilkatJNI.CkEmail_getRelatedFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String relatedFilename(int var1) {
/* 1287 */     return chilkatJNI.CkEmail_relatedFilename(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedString(int var1, String var2, CkString var3) {
/* 1291 */     return chilkatJNI.CkEmail_GetRelatedString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getRelatedString(int var1, String var2) {
/* 1295 */     return chilkatJNI.CkEmail_getRelatedString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String relatedString(int var1, String var2) {
/* 1299 */     return chilkatJNI.CkEmail_relatedString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GetRelatedStringCrLf(int var1, String var2, CkString var3) {
/* 1303 */     return chilkatJNI.CkEmail_GetRelatedStringCrLf(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String getRelatedStringCrLf(int var1, String var2) {
/* 1307 */     return chilkatJNI.CkEmail_getRelatedStringCrLf(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public String relatedStringCrLf(int var1, String var2) {
/* 1311 */     return chilkatJNI.CkEmail_relatedStringCrLf(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean GetReplacePattern(int var1, CkString var2) {
/* 1315 */     return chilkatJNI.CkEmail_GetReplacePattern(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getReplacePattern(int var1) {
/* 1319 */     return chilkatJNI.CkEmail_getReplacePattern(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String replacePattern(int var1) {
/* 1323 */     return chilkatJNI.CkEmail_replacePattern(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetReplaceString(int var1, CkString var2) {
/* 1327 */     return chilkatJNI.CkEmail_GetReplaceString(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getReplaceString(int var1) {
/* 1331 */     return chilkatJNI.CkEmail_getReplaceString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String replaceString(int var1) {
/* 1335 */     return chilkatJNI.CkEmail_replaceString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetReplaceString2(String var1, CkString var2) {
/* 1339 */     return chilkatJNI.CkEmail_GetReplaceString2(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getReplaceString2(String var1) {
/* 1343 */     return chilkatJNI.CkEmail_getReplaceString2(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String replaceString2(String var1) {
/* 1347 */     return chilkatJNI.CkEmail_replaceString2(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetReport(int var1, CkString var2) {
/* 1351 */     return chilkatJNI.CkEmail_GetReport(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getReport(int var1) {
/* 1355 */     return chilkatJNI.CkEmail_getReport(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String report(int var1) {
/* 1359 */     return chilkatJNI.CkEmail_report(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public CkCert GetSignedByCert() {
/* 1363 */     long var1 = chilkatJNI.CkEmail_GetSignedByCert(this.swigCPtr, this);
/* 1364 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public CkCertChain GetSignedByCertChain() {
/* 1368 */     long var1 = chilkatJNI.CkEmail_GetSignedByCertChain(this.swigCPtr, this);
/* 1369 */     return var1 == 0L ? null : new CkCertChain(var1, true);
/*      */   }
/*      */   
/*      */   public CkCert GetSigningCert() {
/* 1373 */     long var1 = chilkatJNI.CkEmail_GetSigningCert(this.swigCPtr, this);
/* 1374 */     return var1 == 0L ? null : new CkCert(var1, true);
/*      */   }
/*      */   
/*      */   public boolean GetTo(int var1, CkString var2) {
/* 1378 */     return chilkatJNI.CkEmail_GetTo(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getTo(int var1) {
/* 1382 */     return chilkatJNI.CkEmail_getTo(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String to(int var1) {
/* 1386 */     return chilkatJNI.CkEmail_to(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetToAddr(int var1, CkString var2) {
/* 1390 */     return chilkatJNI.CkEmail_GetToAddr(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getToAddr(int var1) {
/* 1394 */     return chilkatJNI.CkEmail_getToAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String toAddr(int var1) {
/* 1398 */     return chilkatJNI.CkEmail_toAddr(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetToName(int var1, CkString var2) {
/* 1402 */     return chilkatJNI.CkEmail_GetToName(this.swigCPtr, this, var1, CkString.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public String getToName(int var1) {
/* 1406 */     return chilkatJNI.CkEmail_getToName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public String toName(int var1) {
/* 1410 */     return chilkatJNI.CkEmail_toName(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean GetXml(CkString var1) {
/* 1414 */     return chilkatJNI.CkEmail_GetXml(this.swigCPtr, this, CkString.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public String getXml() {
/* 1418 */     return chilkatJNI.CkEmail_getXml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public String xml() {
/* 1422 */     return chilkatJNI.CkEmail_xml(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean HasHeaderMatching(String var1, String var2, boolean var3) {
/* 1426 */     return chilkatJNI.CkEmail_HasHeaderMatching(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public boolean HasHtmlBody() {
/* 1430 */     return chilkatJNI.CkEmail_HasHtmlBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean HasPlainTextBody() {
/* 1434 */     return chilkatJNI.CkEmail_HasPlainTextBody(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean IsMultipartReport() {
/* 1438 */     return chilkatJNI.CkEmail_IsMultipartReport(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean LoadEml(String var1) {
/* 1442 */     return chilkatJNI.CkEmail_LoadEml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean LoadTaskResult(CkTask var1) {
/* 1446 */     return chilkatJNI.CkEmail_LoadTaskResult(this.swigCPtr, this, CkTask.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean LoadXml(String var1) {
/* 1450 */     return chilkatJNI.CkEmail_LoadXml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean LoadXmlString(String var1) {
/* 1454 */     return chilkatJNI.CkEmail_LoadXmlString(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean QEncodeBytes(CkByteData var1, String var2, CkString var3) {
/* 1458 */     return chilkatJNI.CkEmail_QEncodeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String qEncodeBytes(CkByteData var1, String var2) {
/* 1462 */     return chilkatJNI.CkEmail_qEncodeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean QEncodeString(String var1, String var2, CkString var3) {
/* 1466 */     return chilkatJNI.CkEmail_QEncodeString(this.swigCPtr, this, var1, var2, CkString.getCPtr(var3), var3);
/*      */   }
/*      */   
/*      */   public String qEncodeString(String var1, String var2) {
/* 1470 */     return chilkatJNI.CkEmail_qEncodeString(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public void RemoveAttachedMessage(int var1) {
/* 1474 */     chilkatJNI.CkEmail_RemoveAttachedMessage(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void RemoveAttachedMessages() {
/* 1478 */     chilkatJNI.CkEmail_RemoveAttachedMessages(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void RemoveAttachmentPaths() {
/* 1482 */     chilkatJNI.CkEmail_RemoveAttachmentPaths(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void RemoveHeaderField(String var1) {
/* 1486 */     chilkatJNI.CkEmail_RemoveHeaderField(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void RemoveHtmlAlternative() {
/* 1490 */     chilkatJNI.CkEmail_RemoveHtmlAlternative(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public void RemovePlainTextAlternative() {
/* 1494 */     chilkatJNI.CkEmail_RemovePlainTextAlternative(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean SaveAllAttachments(String var1) {
/* 1498 */     return chilkatJNI.CkEmail_SaveAllAttachments(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SaveAttachedFile(int var1, String var2) {
/* 1502 */     return chilkatJNI.CkEmail_SaveAttachedFile(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SaveEml(String var1) {
/* 1506 */     return chilkatJNI.CkEmail_SaveEml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SaveLastError(String var1) {
/* 1510 */     return chilkatJNI.CkEmail_SaveLastError(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SaveRelatedItem(int var1, String var2) {
/* 1514 */     return chilkatJNI.CkEmail_SaveRelatedItem(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SaveXml(String var1) {
/* 1518 */     return chilkatJNI.CkEmail_SaveXml(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetAttachmentCharset(int var1, String var2) {
/* 1522 */     return chilkatJNI.CkEmail_SetAttachmentCharset(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetAttachmentDisposition(int var1, String var2) {
/* 1526 */     return chilkatJNI.CkEmail_SetAttachmentDisposition(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetAttachmentFilename(int var1, String var2) {
/* 1530 */     return chilkatJNI.CkEmail_SetAttachmentFilename(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetBinaryBody(CkByteData var1, String var2, String var3, String var4) {
/* 1534 */     return chilkatJNI.CkEmail_SetBinaryBody(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean SetCSP(CkCsp var1) {
/* 1538 */     return chilkatJNI.CkEmail_SetCSP(this.swigCPtr, this, CkCsp.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert(CkCert var1) {
/* 1542 */     return chilkatJNI.CkEmail_SetDecryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetDecryptCert2(CkCert var1, CkPrivateKey var2) {
/* 1546 */     return chilkatJNI.CkEmail_SetDecryptCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetDt(CkDateTime var1) {
/* 1550 */     return chilkatJNI.CkEmail_SetDt(this.swigCPtr, this, CkDateTime.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public void SetEdifactBody(String var1, String var2, String var3, String var4) {
/* 1554 */     chilkatJNI.CkEmail_SetEdifactBody(this.swigCPtr, this, var1, var2, var3, var4);
/*      */   }
/*      */   
/*      */   public boolean SetEncryptCert(CkCert var1) {
/* 1558 */     return chilkatJNI.CkEmail_SetEncryptCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetFromMimeBd(CkBinData var1) {
/* 1562 */     return chilkatJNI.CkEmail_SetFromMimeBd(this.swigCPtr, this, CkBinData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetFromMimeBytes(CkByteData var1) {
/* 1566 */     return chilkatJNI.CkEmail_SetFromMimeBytes(this.swigCPtr, this, CkByteData.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetFromMimeBytes2(CkByteData var1, String var2) {
/* 1570 */     return chilkatJNI.CkEmail_SetFromMimeBytes2(this.swigCPtr, this, CkByteData.getCPtr(var1), var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetFromMimeSb(CkStringBuilder var1) {
/* 1574 */     return chilkatJNI.CkEmail_SetFromMimeSb(this.swigCPtr, this, CkStringBuilder.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetFromMimeText(String var1) {
/* 1578 */     return chilkatJNI.CkEmail_SetFromMimeText(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetFromXmlText(String var1) {
/* 1582 */     return chilkatJNI.CkEmail_SetFromXmlText(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public void SetHtmlBody(String var1) {
/* 1586 */     chilkatJNI.CkEmail_SetHtmlBody(this.swigCPtr, this, var1);
/*      */   }
/*      */   
/*      */   public boolean SetMbHtmlBody(String var1, CkByteData var2) {
/* 1590 */     return chilkatJNI.CkEmail_SetMbHtmlBody(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetMbPlainTextBody(String var1, CkByteData var2) {
/* 1594 */     return chilkatJNI.CkEmail_SetMbPlainTextBody(this.swigCPtr, this, var1, CkByteData.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public boolean SetRelatedFilename(int var1, String var2) {
/* 1598 */     return chilkatJNI.CkEmail_SetRelatedFilename(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetReplacePattern(String var1, String var2) {
/* 1602 */     return chilkatJNI.CkEmail_SetReplacePattern(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean SetSigningCert(CkCert var1) {
/* 1606 */     return chilkatJNI.CkEmail_SetSigningCert(this.swigCPtr, this, CkCert.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean SetSigningCert2(CkCert var1, CkPrivateKey var2) {
/* 1610 */     return chilkatJNI.CkEmail_SetSigningCert2(this.swigCPtr, this, CkCert.getCPtr(var1), var1, CkPrivateKey.getCPtr(var2), var2);
/*      */   }
/*      */   
/*      */   public void SetTextBody(String var1, String var2) {
/* 1614 */     chilkatJNI.CkEmail_SetTextBody(this.swigCPtr, this, var1, var2);
/*      */   }
/*      */   
/*      */   public boolean UidlEquals(CkEmail var1) {
/* 1618 */     return chilkatJNI.CkEmail_UidlEquals(this.swigCPtr, this, getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean UnpackHtml(String var1, String var2, String var3) {
/* 1622 */     return chilkatJNI.CkEmail_UnpackHtml(this.swigCPtr, this, var1, var2, var3);
/*      */   }
/*      */   
/*      */   public void UnSpamify() {
/* 1626 */     chilkatJNI.CkEmail_UnSpamify(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean UnzipAttachments() {
/* 1630 */     return chilkatJNI.CkEmail_UnzipAttachments(this.swigCPtr, this);
/*      */   }
/*      */   
/*      */   public boolean UseCertVault(CkXmlCertVault var1) {
/* 1634 */     return chilkatJNI.CkEmail_UseCertVault(this.swigCPtr, this, CkXmlCertVault.getCPtr(var1), var1);
/*      */   }
/*      */   
/*      */   public boolean ZipAttachments(String var1) {
/* 1638 */     return chilkatJNI.CkEmail_ZipAttachments(this.swigCPtr, this, var1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\chilkatsoft\CkEmail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */