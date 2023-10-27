/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.Listener.Adapter;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BufferingResponseListener
/*     */   extends Response.Listener.Adapter
/*     */ {
/*     */   private final int maxLength;
/*     */   private volatile ByteBuffer buffer;
/*     */   private volatile String mediaType;
/*     */   private volatile String encoding;
/*     */   
/*     */   public BufferingResponseListener()
/*     */   {
/*  53 */     this(2097152);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferingResponseListener(int maxLength)
/*     */   {
/*  63 */     this.maxLength = maxLength;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onHeaders(Response response)
/*     */   {
/*  69 */     super.onHeaders(response);
/*     */     
/*  71 */     HttpFields headers = response.getHeaders();
/*  72 */     long length = headers.getLongField(HttpHeader.CONTENT_LENGTH.asString());
/*  73 */     if (length > this.maxLength)
/*     */     {
/*  75 */       response.abort(new IllegalArgumentException("Buffering capacity exceeded"));
/*  76 */       return;
/*     */     }
/*     */     
/*  79 */     this.buffer = BufferUtil.allocate(length > 0L ? (int)length : 1024);
/*     */     
/*  81 */     String contentType = headers.get(HttpHeader.CONTENT_TYPE);
/*  82 */     if (contentType != null)
/*     */     {
/*  84 */       String media = contentType;
/*     */       
/*  86 */       String charset = "charset=";
/*  87 */       int index = contentType.toLowerCase(Locale.ENGLISH).indexOf(charset);
/*  88 */       if (index > 0)
/*     */       {
/*  90 */         media = contentType.substring(0, index);
/*  91 */         String encoding = contentType.substring(index + charset.length());
/*     */         
/*  93 */         int semicolon = encoding.indexOf(';');
/*  94 */         if (semicolon > 0) {
/*  95 */           encoding = encoding.substring(0, semicolon).trim();
/*     */         }
/*  97 */         int lastIndex = encoding.length() - 1;
/*  98 */         if ((encoding.charAt(0) == '"') && (encoding.charAt(lastIndex) == '"'))
/*  99 */           encoding = encoding.substring(1, lastIndex).trim();
/* 100 */         this.encoding = encoding;
/*     */       }
/*     */       
/* 103 */       int semicolon = media.indexOf(';');
/* 104 */       if (semicolon > 0)
/* 105 */         media = media.substring(0, semicolon).trim();
/* 106 */       this.mediaType = media;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onContent(Response response, ByteBuffer content)
/*     */   {
/* 113 */     int length = content.remaining();
/* 114 */     if (length > BufferUtil.space(this.buffer))
/*     */     {
/* 116 */       int requiredCapacity = this.buffer == null ? 0 : this.buffer.capacity() + length;
/* 117 */       if (requiredCapacity > this.maxLength) {
/* 118 */         response.abort(new IllegalArgumentException("Buffering capacity exceeded"));
/*     */       }
/* 120 */       int newCapacity = Math.min(Integer.highestOneBit(requiredCapacity) << 1, this.maxLength);
/* 121 */       this.buffer = BufferUtil.ensureCapacity(this.buffer, newCapacity);
/*     */     }
/* 123 */     BufferUtil.append(this.buffer, content);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void onComplete(Result paramResult);
/*     */   
/*     */   public String getMediaType()
/*     */   {
/* 131 */     return this.mediaType;
/*     */   }
/*     */   
/*     */   public String getEncoding()
/*     */   {
/* 136 */     return this.encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getContent()
/*     */   {
/* 145 */     if (this.buffer == null)
/* 146 */       return new byte[0];
/* 147 */     return BufferUtil.toArray(this.buffer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentAsString()
/*     */   {
/* 157 */     String encoding = this.encoding;
/* 158 */     if (encoding == null)
/* 159 */       return getContentAsString(StandardCharsets.UTF_8);
/* 160 */     return getContentAsString(encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentAsString(String encoding)
/*     */   {
/* 170 */     if (this.buffer == null)
/* 171 */       return null;
/* 172 */     return BufferUtil.toString(this.buffer, Charset.forName(encoding));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentAsString(Charset encoding)
/*     */   {
/* 182 */     if (this.buffer == null)
/* 183 */       return null;
/* 184 */     return BufferUtil.toString(this.buffer, encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getContentAsInputStream()
/*     */   {
/* 192 */     if (this.buffer == null)
/* 193 */       return new ByteArrayInputStream(new byte[0]);
/* 194 */     return new ByteArrayInputStream(this.buffer.array(), this.buffer.arrayOffset(), this.buffer.remaining());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\BufferingResponseListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */