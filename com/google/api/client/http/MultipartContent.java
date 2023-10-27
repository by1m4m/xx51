/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.StreamingContent;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipartContent
/*     */   extends AbstractHttpContent
/*     */ {
/*     */   static final String NEWLINE = "\r\n";
/*     */   private static final String TWO_DASHES = "--";
/*  55 */   private ArrayList<Part> parts = new ArrayList();
/*     */   
/*     */   public MultipartContent() {
/*  58 */     super(new HttpMediaType("multipart/related").setParameter("boundary", "__END_OF_PART__"));
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  62 */     Writer writer = new OutputStreamWriter(out, getCharset());
/*  63 */     String boundary = getBoundary();
/*  64 */     for (Part part : this.parts) {
/*  65 */       HttpHeaders headers = new HttpHeaders().setAcceptEncoding(null);
/*  66 */       if (part.headers != null) {
/*  67 */         headers.fromHttpHeaders(part.headers);
/*     */       }
/*  69 */       headers.setContentEncoding(null).setUserAgent(null).setContentType(null).setContentLength(null).set("Content-Transfer-Encoding", null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */       HttpContent content = part.content;
/*  76 */       StreamingContent streamingContent = null;
/*  77 */       if (content != null) {
/*  78 */         headers.set("Content-Transfer-Encoding", Arrays.asList(new String[] { "binary" }));
/*  79 */         headers.setContentType(content.getType());
/*  80 */         HttpEncoding encoding = part.encoding;
/*     */         long contentLength;
/*  82 */         if (encoding == null) {
/*  83 */           long contentLength = content.getLength();
/*  84 */           streamingContent = content;
/*     */         } else {
/*  86 */           headers.setContentEncoding(encoding.getName());
/*  87 */           streamingContent = new HttpEncodingStreamingContent(content, encoding);
/*  88 */           contentLength = AbstractHttpContent.computeLength(content);
/*     */         }
/*  90 */         if (contentLength != -1L) {
/*  91 */           headers.setContentLength(Long.valueOf(contentLength));
/*     */         }
/*     */       }
/*     */       
/*  95 */       writer.write("--");
/*  96 */       writer.write(boundary);
/*  97 */       writer.write("\r\n");
/*     */       
/*  99 */       HttpHeaders.serializeHeadersForMultipartRequests(headers, null, null, writer);
/*     */       
/* 101 */       if (streamingContent != null) {
/* 102 */         writer.write("\r\n");
/* 103 */         writer.flush();
/* 104 */         streamingContent.writeTo(out);
/* 105 */         writer.write("\r\n");
/*     */       }
/*     */     }
/*     */     
/* 109 */     writer.write("--");
/* 110 */     writer.write(boundary);
/* 111 */     writer.write("--");
/* 112 */     writer.write("\r\n");
/* 113 */     writer.flush();
/*     */   }
/*     */   
/*     */   public boolean retrySupported()
/*     */   {
/* 118 */     for (Part part : this.parts) {
/* 119 */       if (!part.content.retrySupported()) {
/* 120 */         return false;
/*     */       }
/*     */     }
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   public MultipartContent setMediaType(HttpMediaType mediaType)
/*     */   {
/* 128 */     super.setMediaType(mediaType);
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public final Collection<Part> getParts()
/*     */   {
/* 134 */     return Collections.unmodifiableCollection(this.parts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipartContent addPart(Part part)
/*     */   {
/* 146 */     this.parts.add(Preconditions.checkNotNull(part));
/* 147 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipartContent setParts(Collection<Part> parts)
/*     */   {
/* 159 */     this.parts = new ArrayList(parts);
/* 160 */     return this;
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
/*     */   public MultipartContent setContentParts(Collection<? extends HttpContent> contentParts)
/*     */   {
/* 173 */     this.parts = new ArrayList(contentParts.size());
/* 174 */     for (HttpContent contentPart : contentParts) {
/* 175 */       addPart(new Part(contentPart));
/*     */     }
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public final String getBoundary()
/*     */   {
/* 182 */     return getMediaType().getParameter("boundary");
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
/*     */   public MultipartContent setBoundary(String boundary)
/*     */   {
/* 198 */     getMediaType().setParameter("boundary", (String)Preconditions.checkNotNull(boundary));
/* 199 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Part
/*     */   {
/*     */     HttpContent content;
/*     */     
/*     */ 
/*     */ 
/*     */     HttpHeaders headers;
/*     */     
/*     */ 
/*     */ 
/*     */     HttpEncoding encoding;
/*     */     
/*     */ 
/*     */ 
/*     */     public Part()
/*     */     {
/* 221 */       this(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Part(HttpContent content)
/*     */     {
/* 228 */       this(null, content);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Part(HttpHeaders headers, HttpContent content)
/*     */     {
/* 236 */       setHeaders(headers);
/* 237 */       setContent(content);
/*     */     }
/*     */     
/*     */     public Part setContent(HttpContent content)
/*     */     {
/* 242 */       this.content = content;
/* 243 */       return this;
/*     */     }
/*     */     
/*     */     public HttpContent getContent()
/*     */     {
/* 248 */       return this.content;
/*     */     }
/*     */     
/*     */     public Part setHeaders(HttpHeaders headers)
/*     */     {
/* 253 */       this.headers = headers;
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public HttpHeaders getHeaders()
/*     */     {
/* 259 */       return this.headers;
/*     */     }
/*     */     
/*     */     public Part setEncoding(HttpEncoding encoding)
/*     */     {
/* 264 */       this.encoding = encoding;
/* 265 */       return this;
/*     */     }
/*     */     
/*     */     public HttpEncoding getEncoding()
/*     */     {
/* 270 */       return this.encoding;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\MultipartContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */