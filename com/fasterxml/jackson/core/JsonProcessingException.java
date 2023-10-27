/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class JsonProcessingException
/*     */   extends IOException
/*     */ {
/*     */   static final long serialVersionUID = 123L;
/*     */   protected JsonLocation _location;
/*     */   
/*     */   protected JsonProcessingException(String msg, JsonLocation loc, Throwable rootCause)
/*     */   {
/*  25 */     super(msg);
/*  26 */     if (rootCause != null) {
/*  27 */       initCause(rootCause);
/*     */     }
/*  29 */     this._location = loc;
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg) {
/*  33 */     super(msg);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg, JsonLocation loc) {
/*  37 */     this(msg, loc, null);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(String msg, Throwable rootCause) {
/*  41 */     this(msg, null, rootCause);
/*     */   }
/*     */   
/*     */   protected JsonProcessingException(Throwable rootCause) {
/*  45 */     this(null, null, rootCause);
/*     */   }
/*     */   
/*  48 */   public JsonLocation getLocation() { return this._location; }
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
/*     */   public String getOriginalMessage()
/*     */   {
/*  63 */     return super.getMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getMessageSuffix()
/*     */   {
/*  76 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  88 */     String msg = super.getMessage();
/*  89 */     if (msg == null) {
/*  90 */       msg = "N/A";
/*     */     }
/*  92 */     JsonLocation loc = getLocation();
/*  93 */     String suffix = getMessageSuffix();
/*     */     
/*  95 */     if ((loc != null) || (suffix != null)) {
/*  96 */       StringBuilder sb = new StringBuilder(100);
/*  97 */       sb.append(msg);
/*  98 */       if (suffix != null) {
/*  99 */         sb.append(suffix);
/*     */       }
/* 101 */       if (loc != null) {
/* 102 */         sb.append('\n');
/* 103 */         sb.append(" at ");
/* 104 */         sb.append(loc.toString());
/*     */       }
/* 106 */       msg = sb.toString();
/*     */     }
/* 108 */     return msg;
/*     */   }
/*     */   
/* 111 */   public String toString() { return getClass().getName() + ": " + getMessage(); }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonProcessingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */