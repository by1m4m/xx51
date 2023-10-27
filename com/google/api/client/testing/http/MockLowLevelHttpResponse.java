/*     */ package com.google.api.client.testing.http;
/*     */ 
/*     */ import com.google.api.client.http.LowLevelHttpResponse;
/*     */ import com.google.api.client.testing.util.TestableByteArrayInputStream;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public class MockLowLevelHttpResponse
/*     */   extends LowLevelHttpResponse
/*     */ {
/*     */   private InputStream content;
/*     */   private String contentType;
/*  49 */   private int statusCode = 200;
/*     */   
/*     */ 
/*     */   private String reasonPhrase;
/*     */   
/*     */ 
/*  55 */   private List<String> headerNames = new ArrayList();
/*     */   
/*     */ 
/*  58 */   private List<String> headerValues = new ArrayList();
/*     */   
/*     */ 
/*     */   private String contentEncoding;
/*     */   
/*     */ 
/*  64 */   private long contentLength = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isDisconnected;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse addHeader(String name, String value)
/*     */   {
/*  76 */     this.headerNames.add(Preconditions.checkNotNull(name));
/*  77 */     this.headerValues.add(Preconditions.checkNotNull(value));
/*  78 */     return this;
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
/*     */   public MockLowLevelHttpResponse setContent(String stringContent)
/*     */   {
/*  92 */     return stringContent == null ? setZeroContent() : setContent(StringUtils.getBytesUtf8(stringContent));
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
/*     */   public MockLowLevelHttpResponse setContent(byte[] byteContent)
/*     */   {
/* 111 */     if (byteContent == null) {
/* 112 */       return setZeroContent();
/*     */     }
/* 114 */     this.content = new TestableByteArrayInputStream(byteContent);
/* 115 */     setContentLength(byteContent.length);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setZeroContent()
/*     */   {
/* 126 */     this.content = null;
/* 127 */     setContentLength(0L);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException
/*     */   {
/* 133 */     return this.content;
/*     */   }
/*     */   
/*     */   public String getContentEncoding()
/*     */   {
/* 138 */     return this.contentEncoding;
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/* 143 */     return this.contentLength;
/*     */   }
/*     */   
/*     */   public final String getContentType()
/*     */   {
/* 148 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public int getHeaderCount()
/*     */   {
/* 153 */     return this.headerNames.size();
/*     */   }
/*     */   
/*     */   public String getHeaderName(int index)
/*     */   {
/* 158 */     return (String)this.headerNames.get(index);
/*     */   }
/*     */   
/*     */   public String getHeaderValue(int index)
/*     */   {
/* 163 */     return (String)this.headerValues.get(index);
/*     */   }
/*     */   
/*     */   public String getReasonPhrase()
/*     */   {
/* 168 */     return this.reasonPhrase;
/*     */   }
/*     */   
/*     */   public int getStatusCode()
/*     */   {
/* 173 */     return this.statusCode;
/*     */   }
/*     */   
/*     */   public String getStatusLine()
/*     */   {
/* 178 */     StringBuilder buf = new StringBuilder();
/* 179 */     buf.append(this.statusCode);
/* 180 */     if (this.reasonPhrase != null) {
/* 181 */       buf.append(this.reasonPhrase);
/*     */     }
/* 183 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final List<String> getHeaderNames()
/*     */   {
/* 192 */     return this.headerNames;
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
/*     */   public MockLowLevelHttpResponse setHeaderNames(List<String> headerNames)
/*     */   {
/* 205 */     this.headerNames = ((List)Preconditions.checkNotNull(headerNames));
/* 206 */     return this;
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
/*     */   public final List<String> getHeaderValues()
/*     */   {
/* 219 */     return this.headerValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setHeaderValues(List<String> headerValues)
/*     */   {
/* 228 */     this.headerValues = ((List)Preconditions.checkNotNull(headerValues));
/* 229 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setContent(InputStream content)
/*     */   {
/* 238 */     this.content = content;
/* 239 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setContentType(String contentType)
/*     */   {
/* 248 */     this.contentType = contentType;
/* 249 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setContentEncoding(String contentEncoding)
/*     */   {
/* 258 */     this.contentEncoding = contentEncoding;
/* 259 */     return this;
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
/*     */   public MockLowLevelHttpResponse setContentLength(long contentLength)
/*     */   {
/* 272 */     this.contentLength = contentLength;
/* 273 */     Preconditions.checkArgument(contentLength >= -1L);
/* 274 */     return this;
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
/*     */   public MockLowLevelHttpResponse setStatusCode(int statusCode)
/*     */   {
/* 287 */     this.statusCode = statusCode;
/* 288 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse setReasonPhrase(String reasonPhrase)
/*     */   {
/* 297 */     this.reasonPhrase = reasonPhrase;
/* 298 */     return this;
/*     */   }
/*     */   
/*     */   public void disconnect() throws IOException
/*     */   {
/* 303 */     this.isDisconnected = true;
/* 304 */     super.disconnect();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDisconnected()
/*     */   {
/* 313 */     return this.isDisconnected;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\MockLowLevelHttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */