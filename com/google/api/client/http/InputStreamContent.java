/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.InputStream;
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
/*     */ public final class InputStreamContent
/*     */   extends AbstractInputStreamContent
/*     */ {
/*  53 */   private long length = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean retrySupported;
/*     */   
/*     */ 
/*     */ 
/*     */   private final InputStream inputStream;
/*     */   
/*     */ 
/*     */ 
/*     */   public InputStreamContent(String type, InputStream inputStream)
/*     */   {
/*  67 */     super(type);
/*  68 */     this.inputStream = ((InputStream)Preconditions.checkNotNull(inputStream));
/*     */   }
/*     */   
/*     */   public long getLength() {
/*  72 */     return this.length;
/*     */   }
/*     */   
/*     */   public boolean retrySupported() {
/*  76 */     return this.retrySupported;
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
/*     */ 
/*     */   public InputStreamContent setRetrySupported(boolean retrySupported)
/*     */   {
/*  90 */     this.retrySupported = retrySupported;
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */   {
/*  96 */     return this.inputStream;
/*     */   }
/*     */   
/*     */   public InputStreamContent setType(String type)
/*     */   {
/* 101 */     return (InputStreamContent)super.setType(type);
/*     */   }
/*     */   
/*     */   public InputStreamContent setCloseInputStream(boolean closeInputStream)
/*     */   {
/* 106 */     return (InputStreamContent)super.setCloseInputStream(closeInputStream);
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
/*     */   public InputStreamContent setLength(long length)
/*     */   {
/* 119 */     this.length = length;
/* 120 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\InputStreamContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */