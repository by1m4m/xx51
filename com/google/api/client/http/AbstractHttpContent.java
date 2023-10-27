/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Charsets;
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class AbstractHttpContent
/*     */   implements HttpContent
/*     */ {
/*     */   private HttpMediaType mediaType;
/*  40 */   private long computedLength = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpContent(String mediaType)
/*     */   {
/*  48 */     this(mediaType == null ? null : new HttpMediaType(mediaType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpContent(HttpMediaType mediaType)
/*     */   {
/*  56 */     this.mediaType = mediaType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getLength()
/*     */     throws IOException
/*     */   {
/*  64 */     if (this.computedLength == -1L) {
/*  65 */       this.computedLength = computeLength();
/*     */     }
/*  67 */     return this.computedLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final HttpMediaType getMediaType()
/*     */   {
/*  76 */     return this.mediaType;
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
/*     */   public AbstractHttpContent setMediaType(HttpMediaType mediaType)
/*     */   {
/*  90 */     this.mediaType = mediaType;
/*  91 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Charset getCharset()
/*     */   {
/* 100 */     return (this.mediaType == null) || (this.mediaType.getCharsetParameter() == null) ? Charsets.UTF_8 : this.mediaType.getCharsetParameter();
/*     */   }
/*     */   
/*     */   public String getType()
/*     */   {
/* 105 */     return this.mediaType == null ? null : this.mediaType.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long computeLength()
/*     */     throws IOException
/*     */   {
/* 117 */     return computeLength(this);
/*     */   }
/*     */   
/*     */   public boolean retrySupported()
/*     */   {
/* 122 */     return true;
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
/*     */   public static long computeLength(HttpContent content)
/*     */     throws IOException
/*     */   {
/* 136 */     if (!content.retrySupported()) {
/* 137 */       return -1L;
/*     */     }
/* 139 */     return IOUtils.computeLength(content);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\AbstractHttpContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */