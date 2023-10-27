/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Charsets;
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import com.google.api.client.util.LoggingInputStream;
/*     */ import com.google.api.client.util.ObjectParser;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.StringUtils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.GZIPInputStream;
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
/*     */ public final class HttpResponse
/*     */ {
/*     */   private InputStream content;
/*     */   private final String contentEncoding;
/*     */   private final String contentType;
/*     */   private final HttpMediaType mediaType;
/*     */   LowLevelHttpResponse response;
/*     */   private final int statusCode;
/*     */   private final String statusMessage;
/*     */   private final HttpRequest request;
/*     */   private int contentLoggingLimit;
/*     */   private boolean loggingEnabled;
/*     */   private boolean contentRead;
/*     */   
/*     */   HttpResponse(HttpRequest request, LowLevelHttpResponse response)
/*     */     throws IOException
/*     */   {
/* 120 */     this.request = request;
/* 121 */     this.contentLoggingLimit = request.getContentLoggingLimit();
/* 122 */     this.loggingEnabled = request.isLoggingEnabled();
/* 123 */     this.response = response;
/* 124 */     this.contentEncoding = response.getContentEncoding();
/* 125 */     int code = response.getStatusCode();
/* 126 */     this.statusCode = (code < 0 ? 0 : code);
/* 127 */     String message = response.getReasonPhrase();
/* 128 */     this.statusMessage = message;
/* 129 */     Logger logger = HttpTransport.LOGGER;
/* 130 */     boolean loggable = (this.loggingEnabled) && (logger.isLoggable(Level.CONFIG));
/* 131 */     StringBuilder logbuf = null;
/* 132 */     if (loggable) {
/* 133 */       logbuf = new StringBuilder();
/* 134 */       logbuf.append("-------------- RESPONSE --------------").append(StringUtils.LINE_SEPARATOR);
/* 135 */       String statusLine = response.getStatusLine();
/* 136 */       if (statusLine != null) {
/* 137 */         logbuf.append(statusLine);
/*     */       } else {
/* 139 */         logbuf.append(this.statusCode);
/* 140 */         if (message != null) {
/* 141 */           logbuf.append(' ').append(message);
/*     */         }
/*     */       }
/* 144 */       logbuf.append(StringUtils.LINE_SEPARATOR);
/*     */     }
/*     */     
/*     */ 
/* 148 */     request.getResponseHeaders().fromHttpResponse(response, loggable ? logbuf : null);
/*     */     
/*     */ 
/*     */ 
/* 152 */     String contentType = response.getContentType();
/* 153 */     if (contentType == null) {
/* 154 */       contentType = request.getResponseHeaders().getContentType();
/*     */     }
/* 156 */     this.contentType = contentType;
/* 157 */     this.mediaType = (contentType == null ? null : new HttpMediaType(contentType));
/*     */     
/*     */ 
/* 160 */     if (loggable) {
/* 161 */       logger.config(logbuf.toString());
/*     */     }
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
/*     */   public int getContentLoggingLimit()
/*     */   {
/* 188 */     return this.contentLoggingLimit;
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
/*     */   public HttpResponse setContentLoggingLimit(int contentLoggingLimit)
/*     */   {
/* 214 */     Preconditions.checkArgument(contentLoggingLimit >= 0, "The content logging limit must be non-negative.");
/*     */     
/* 216 */     this.contentLoggingLimit = contentLoggingLimit;
/* 217 */     return this;
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
/*     */   public boolean isLoggingEnabled()
/*     */   {
/* 230 */     return this.loggingEnabled;
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
/*     */   public HttpResponse setLoggingEnabled(boolean loggingEnabled)
/*     */   {
/* 243 */     this.loggingEnabled = loggingEnabled;
/* 244 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentEncoding()
/*     */   {
/* 253 */     return this.contentEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 262 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType getMediaType()
/*     */   {
/* 272 */     return this.mediaType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpHeaders getHeaders()
/*     */   {
/* 281 */     return this.request.getResponseHeaders();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSuccessStatusCode()
/*     */   {
/* 291 */     return HttpStatusCodes.isSuccess(this.statusCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatusCode()
/*     */   {
/* 300 */     return this.statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStatusMessage()
/*     */   {
/* 309 */     return this.statusMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpTransport getTransport()
/*     */   {
/* 318 */     return this.request.getTransport();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest getRequest()
/*     */   {
/* 327 */     return this.request;
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
/*     */   public InputStream getContent()
/*     */     throws IOException
/*     */   {
/* 353 */     if (!this.contentRead) {
/* 354 */       InputStream lowLevelResponseContent = this.response.getContent();
/* 355 */       if (lowLevelResponseContent != null)
/*     */       {
/*     */ 
/* 358 */         boolean contentProcessed = false;
/*     */         try
/*     */         {
/* 361 */           String contentEncoding = this.contentEncoding;
/* 362 */           if ((contentEncoding != null) && (contentEncoding.contains("gzip"))) {
/* 363 */             lowLevelResponseContent = new GZIPInputStream(lowLevelResponseContent);
/*     */           }
/*     */           
/* 366 */           Logger logger = HttpTransport.LOGGER;
/* 367 */           if ((this.loggingEnabled) && (logger.isLoggable(Level.CONFIG))) {
/* 368 */             lowLevelResponseContent = new LoggingInputStream(lowLevelResponseContent, logger, Level.CONFIG, this.contentLoggingLimit);
/*     */           }
/*     */           
/* 371 */           this.content = lowLevelResponseContent;
/* 372 */           contentProcessed = true;
/*     */ 
/*     */         }
/*     */         catch (EOFException e) {}finally
/*     */         {
/* 377 */           if (!contentProcessed) {
/* 378 */             lowLevelResponseContent.close();
/*     */           }
/*     */         }
/*     */       }
/* 382 */       this.contentRead = true;
/*     */     }
/* 384 */     return this.content;
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
/*     */   public void download(OutputStream outputStream)
/*     */     throws IOException
/*     */   {
/* 420 */     InputStream inputStream = getContent();
/* 421 */     IOUtils.copy(inputStream, outputStream);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ignore()
/*     */     throws IOException
/*     */   {
/* 428 */     InputStream content = getContent();
/* 429 */     if (content != null) {
/* 430 */       content.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */     throws IOException
/*     */   {
/* 441 */     ignore();
/* 442 */     this.response.disconnect();
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
/*     */   public <T> T parseAs(Class<T> dataClass)
/*     */     throws IOException
/*     */   {
/* 456 */     if (!hasMessageBody()) {
/* 457 */       return null;
/*     */     }
/* 459 */     return (T)this.request.getParser().parseAndClose(getContent(), getContentCharset(), dataClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasMessageBody()
/*     */     throws IOException
/*     */   {
/* 467 */     int statusCode = getStatusCode();
/* 468 */     if ((getRequest().getRequestMethod().equals("HEAD")) || (statusCode / 100 == 1) || (statusCode == 204) || (statusCode == 304))
/*     */     {
/*     */ 
/* 471 */       ignore();
/* 472 */       return false;
/*     */     }
/* 474 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object parseAs(Type dataType)
/*     */     throws IOException
/*     */   {
/* 485 */     if (!hasMessageBody()) {
/* 486 */       return null;
/*     */     }
/* 488 */     return this.request.getParser().parseAndClose(getContent(), getContentCharset(), dataType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String parseAsString()
/*     */     throws IOException
/*     */   {
/* 510 */     InputStream content = getContent();
/* 511 */     if (content == null) {
/* 512 */       return "";
/*     */     }
/* 514 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 515 */     IOUtils.copy(content, out);
/* 516 */     return out.toString(getContentCharset().name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getContentCharset()
/*     */   {
/* 526 */     return (this.mediaType == null) || (this.mediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : this.mediaType.getCharsetParameter();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */