/*     */ package com.google.api.client.testing.http;
/*     */ 
/*     */ import com.google.api.client.http.HttpMediaType;
/*     */ import com.google.api.client.http.LowLevelHttpRequest;
/*     */ import com.google.api.client.http.LowLevelHttpResponse;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Charsets;
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import com.google.api.client.util.StreamingContent;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ public class MockLowLevelHttpRequest
/*     */   extends LowLevelHttpRequest
/*     */ {
/*     */   private String url;
/*  54 */   private final Map<String, List<String>> headersMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
/*     */   
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpRequest() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpRequest(String url)
/*     */   {
/*  73 */     this.url = url;
/*     */   }
/*     */   
/*     */   public void addHeader(String name, String value) throws IOException
/*     */   {
/*  78 */     name = name.toLowerCase();
/*  79 */     List<String> values = (List)this.headersMap.get(name);
/*  80 */     if (values == null) {
/*  81 */       values = new ArrayList();
/*  82 */       this.headersMap.put(name, values);
/*     */     }
/*  84 */     values.add(value);
/*     */   }
/*     */   
/*     */   public LowLevelHttpResponse execute() throws IOException
/*     */   {
/*  89 */     return this.response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl()
/*     */   {
/*  98 */     return this.url;
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
/*     */   public Map<String, List<String>> getHeaders()
/*     */   {
/* 112 */     return Collections.unmodifiableMap(this.headersMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFirstHeaderValue(String name)
/*     */   {
/* 122 */     List<String> values = (List)this.headersMap.get(name.toLowerCase());
/* 123 */     return values == null ? null : (String)values.get(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getHeaderValues(String name)
/*     */   {
/* 133 */     List<String> values = (List)this.headersMap.get(name.toLowerCase());
/* 134 */     return values == null ? Collections.emptyList() : Collections.unmodifiableList(values);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpRequest setUrl(String url)
/*     */   {
/* 143 */     this.url = url;
/* 144 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentAsString()
/*     */     throws IOException
/*     */   {
/* 157 */     if (getStreamingContent() == null) {
/* 158 */       return "";
/*     */     }
/*     */     
/* 161 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 162 */     getStreamingContent().writeTo(out);
/*     */     
/* 164 */     String contentEncoding = getContentEncoding();
/* 165 */     if ((contentEncoding != null) && (contentEncoding.contains("gzip"))) {
/* 166 */       InputStream contentInputStream = new GZIPInputStream(new ByteArrayInputStream(out.toByteArray()));
/*     */       
/* 168 */       out = new ByteArrayOutputStream();
/* 169 */       IOUtils.copy(contentInputStream, out);
/*     */     }
/*     */     
/* 172 */     String contentType = getContentType();
/* 173 */     HttpMediaType mediaType = contentType != null ? new HttpMediaType(contentType) : null;
/* 174 */     Charset charset = (mediaType == null) || (mediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : mediaType.getCharsetParameter();
/*     */     
/* 176 */     return out.toString(charset.name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpResponse getResponse()
/*     */   {
/* 185 */     return this.response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockLowLevelHttpRequest setResponse(MockLowLevelHttpResponse response)
/*     */   {
/* 196 */     this.response = response;
/* 197 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\MockLowLevelHttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */