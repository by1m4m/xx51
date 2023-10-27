/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponseException
/*     */   extends IOException
/*     */ {
/*     */   private static final long serialVersionUID = -1875819453475890043L;
/*     */   private final int statusCode;
/*     */   private final String statusMessage;
/*     */   private final transient HttpHeaders headers;
/*     */   private final String content;
/*     */   
/*     */   public HttpResponseException(HttpResponse response)
/*     */   {
/*  68 */     this(new Builder(response));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpResponseException(Builder builder)
/*     */   {
/*  77 */     super(builder.message);
/*  78 */     this.statusCode = builder.statusCode;
/*  79 */     this.statusMessage = builder.statusMessage;
/*  80 */     this.headers = builder.headers;
/*  81 */     this.content = builder.content;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isSuccessStatusCode()
/*     */   {
/*  91 */     return HttpStatusCodes.isSuccess(this.statusCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getStatusCode()
/*     */   {
/* 100 */     return this.statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getStatusMessage()
/*     */   {
/* 109 */     return this.statusMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpHeaders getHeaders()
/*     */   {
/* 118 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getContent()
/*     */   {
/* 127 */     return this.content;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/*     */     int statusCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     String statusMessage;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     HttpHeaders headers;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     String content;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     String message;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder(int statusCode, String statusMessage, HttpHeaders headers)
/*     */     {
/* 163 */       setStatusCode(statusCode);
/* 164 */       setStatusMessage(statusMessage);
/* 165 */       setHeaders(headers);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder(HttpResponse response)
/*     */     {
/* 172 */       this(response.getStatusCode(), response.getStatusMessage(), response.getHeaders());
/*     */       try
/*     */       {
/* 175 */         this.content = response.parseAsString();
/* 176 */         if (this.content.length() == 0) {
/* 177 */           this.content = null;
/*     */         }
/*     */       }
/*     */       catch (IOException exception) {
/* 181 */         exception.printStackTrace();
/*     */       }
/*     */       
/* 184 */       StringBuilder builder = HttpResponseException.computeMessageBuffer(response);
/* 185 */       if (this.content != null) {
/* 186 */         builder.append(StringUtils.LINE_SEPARATOR).append(this.content);
/*     */       }
/* 188 */       this.message = builder.toString();
/*     */     }
/*     */     
/*     */     public final String getMessage()
/*     */     {
/* 193 */       return this.message;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setMessage(String message)
/*     */     {
/* 205 */       this.message = message;
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public final int getStatusCode()
/*     */     {
/* 211 */       return this.statusCode;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setStatusCode(int statusCode)
/*     */     {
/* 223 */       Preconditions.checkArgument(statusCode >= 0);
/* 224 */       this.statusCode = statusCode;
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public final String getStatusMessage()
/*     */     {
/* 230 */       return this.statusMessage;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setStatusMessage(String statusMessage)
/*     */     {
/* 242 */       this.statusMessage = statusMessage;
/* 243 */       return this;
/*     */     }
/*     */     
/*     */     public HttpHeaders getHeaders()
/*     */     {
/* 248 */       return this.headers;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setHeaders(HttpHeaders headers)
/*     */     {
/* 260 */       this.headers = ((HttpHeaders)Preconditions.checkNotNull(headers));
/* 261 */       return this;
/*     */     }
/*     */     
/*     */     public final String getContent()
/*     */     {
/* 266 */       return this.content;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setContent(String content)
/*     */     {
/* 278 */       this.content = content;
/* 279 */       return this;
/*     */     }
/*     */     
/*     */     public HttpResponseException build()
/*     */     {
/* 284 */       return new HttpResponseException(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringBuilder computeMessageBuffer(HttpResponse response)
/*     */   {
/* 294 */     StringBuilder builder = new StringBuilder();
/* 295 */     int statusCode = response.getStatusCode();
/* 296 */     if (statusCode != 0) {
/* 297 */       builder.append(statusCode);
/*     */     }
/* 299 */     String statusMessage = response.getStatusMessage();
/* 300 */     if (statusMessage != null) {
/* 301 */       if (statusCode != 0) {
/* 302 */         builder.append(' ');
/*     */       }
/* 304 */       builder.append(statusMessage);
/*     */     }
/* 306 */     return builder;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpResponseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */