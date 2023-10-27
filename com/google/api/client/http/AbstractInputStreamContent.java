/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public abstract class AbstractInputStreamContent
/*     */   implements HttpContent
/*     */ {
/*     */   private String type;
/*  51 */   private boolean closeInputStream = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractInputStreamContent(String type)
/*     */   {
/*  58 */     setType(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract InputStream getInputStream()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/*  72 */     IOUtils.copy(getInputStream(), out, this.closeInputStream);
/*  73 */     out.flush();
/*     */   }
/*     */   
/*     */   public String getType() {
/*  77 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean getCloseInputStream()
/*     */   {
/*  87 */     return this.closeInputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractInputStreamContent setType(String type)
/*     */   {
/*  96 */     this.type = type;
/*  97 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractInputStreamContent setCloseInputStream(boolean closeInputStream)
/*     */   {
/* 107 */     this.closeInputStream = closeInputStream;
/* 108 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\AbstractInputStreamContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */