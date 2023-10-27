/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.StreamingContent;
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
/*     */ public abstract class LowLevelHttpRequest
/*     */ {
/*  40 */   private long contentLength = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String contentEncoding;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String contentType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StreamingContent streamingContent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void addHeader(String paramString1, String paramString2)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setContentLength(long contentLength)
/*     */     throws IOException
/*     */   {
/*  75 */     this.contentLength = contentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long getContentLength()
/*     */   {
/*  84 */     return this.contentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setContentEncoding(String contentEncoding)
/*     */     throws IOException
/*     */   {
/*  94 */     this.contentEncoding = contentEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getContentEncoding()
/*     */   {
/* 103 */     return this.contentEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setContentType(String contentType)
/*     */     throws IOException
/*     */   {
/* 113 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getContentType()
/*     */   {
/* 122 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setStreamingContent(StreamingContent streamingContent)
/*     */     throws IOException
/*     */   {
/* 133 */     this.streamingContent = streamingContent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final StreamingContent getStreamingContent()
/*     */   {
/* 142 */     return this.streamingContent;
/*     */   }
/*     */   
/*     */   public void setTimeout(int connectTimeout, int readTimeout)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public abstract LowLevelHttpResponse execute()
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\LowLevelHttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */