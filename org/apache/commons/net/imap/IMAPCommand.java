/*     */ package org.apache.commons.net.imap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum IMAPCommand
/*     */ {
/*  30 */   CAPABILITY(0), 
/*  31 */   NOOP(0), 
/*  32 */   LOGOUT(0), 
/*     */   
/*     */ 
/*  35 */   STARTTLS(0), 
/*  36 */   AUTHENTICATE(1), 
/*  37 */   LOGIN(2), 
/*     */   
/*  39 */   XOAUTH(1), 
/*     */   
/*     */ 
/*  42 */   SELECT(1), 
/*  43 */   EXAMINE(1), 
/*  44 */   CREATE(1), 
/*  45 */   DELETE(1), 
/*  46 */   RENAME(2), 
/*  47 */   SUBSCRIBE(1), 
/*  48 */   UNSUBSCRIBE(1), 
/*  49 */   LIST(2), 
/*  50 */   LSUB(2), 
/*  51 */   STATUS(2), 
/*  52 */   APPEND(2, 4), 
/*     */   
/*     */ 
/*  55 */   CHECK(0), 
/*  56 */   CLOSE(0), 
/*  57 */   EXPUNGE(0), 
/*  58 */   SEARCH(1, Integer.MAX_VALUE), 
/*  59 */   FETCH(2), 
/*  60 */   STORE(3), 
/*  61 */   COPY(2), 
/*  62 */   UID(2, Integer.MAX_VALUE);
/*     */   
/*     */ 
/*     */   private final String imapCommand;
/*     */   
/*     */   private final int minParamCount;
/*     */   
/*     */   private final int maxParamCount;
/*     */   
/*     */   private IMAPCommand()
/*     */   {
/*  73 */     this(null);
/*     */   }
/*     */   
/*     */   private IMAPCommand(String name) {
/*  77 */     this(name, 0);
/*     */   }
/*     */   
/*     */   private IMAPCommand(int paramCount) {
/*  81 */     this(null, paramCount, paramCount);
/*     */   }
/*     */   
/*     */   private IMAPCommand(int minCount, int maxCount) {
/*  85 */     this(null, minCount, maxCount);
/*     */   }
/*     */   
/*     */   private IMAPCommand(String name, int paramCount) {
/*  89 */     this(name, paramCount, paramCount);
/*     */   }
/*     */   
/*     */   private IMAPCommand(String name, int minCount, int maxCount) {
/*  93 */     this.imapCommand = name;
/*  94 */     this.minParamCount = minCount;
/*  95 */     this.maxParamCount = maxCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String getCommand(IMAPCommand command)
/*     */   {
/* 105 */     return command.getIMAPCommand();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIMAPCommand()
/*     */   {
/* 114 */     return this.imapCommand != null ? this.imapCommand : name();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\imap\IMAPCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */