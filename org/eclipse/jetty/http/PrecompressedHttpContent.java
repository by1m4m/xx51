/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jetty.util.resource.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrecompressedHttpContent
/*     */   implements HttpContent
/*     */ {
/*     */   private final HttpContent _content;
/*     */   private final HttpContent _precompressedContent;
/*     */   private final CompressedContentFormat _format;
/*     */   
/*     */   public PrecompressedHttpContent(HttpContent content, HttpContent precompressedContent, CompressedContentFormat format)
/*     */   {
/*  39 */     this._content = content;
/*  40 */     this._precompressedContent = precompressedContent;
/*  41 */     this._format = format;
/*  42 */     if ((this._precompressedContent == null) || (this._format == null))
/*     */     {
/*  44 */       throw new NullPointerException("Missing compressed content and/or format");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  51 */     return this._content.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  57 */     return this._content.equals(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/*  63 */     return this._content.getResource();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpField getETag()
/*     */   {
/*  69 */     return new HttpField(HttpHeader.ETAG, getETagValue());
/*     */   }
/*     */   
/*     */ 
/*     */   public String getETagValue()
/*     */   {
/*  75 */     return this._content.getResource().getWeakETag(this._format._etag);
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpField getLastModified()
/*     */   {
/*  81 */     return this._content.getLastModified();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getLastModifiedValue()
/*     */   {
/*  87 */     return this._content.getLastModifiedValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpField getContentType()
/*     */   {
/*  93 */     return this._content.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getContentTypeValue()
/*     */   {
/*  99 */     return this._content.getContentTypeValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpField getContentEncoding()
/*     */   {
/* 105 */     return this._format._contentEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getContentEncodingValue()
/*     */   {
/* 111 */     return this._format._contentEncoding.getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 117 */     return this._content.getCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */   public MimeTypes.Type getMimeType()
/*     */   {
/* 123 */     return this._content.getMimeType();
/*     */   }
/*     */   
/*     */ 
/*     */   public void release()
/*     */   {
/* 129 */     this._content.release();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getIndirectBuffer()
/*     */   {
/* 135 */     return this._precompressedContent.getIndirectBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getDirectBuffer()
/*     */   {
/* 141 */     return this._precompressedContent.getDirectBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpField getContentLength()
/*     */   {
/* 147 */     return this._precompressedContent.getContentLength();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getContentLengthValue()
/*     */   {
/* 153 */     return this._precompressedContent.getContentLengthValue();
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 159 */     return this._precompressedContent.getInputStream();
/*     */   }
/*     */   
/*     */   public ReadableByteChannel getReadableByteChannel()
/*     */     throws IOException
/*     */   {
/* 165 */     return this._precompressedContent.getReadableByteChannel();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 171 */     return String.format("PrecompressedHttpContent@%x{e=%s,r=%s|%s,lm=%s|%s,ct=%s}", new Object[] { Integer.valueOf(hashCode()), this._format._encoding, this._content
/* 172 */       .getResource(), this._precompressedContent.getResource(), 
/* 173 */       Long.valueOf(this._content.getResource().lastModified()), Long.valueOf(this._precompressedContent.getResource().lastModified()), 
/* 174 */       getContentType() });
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<CompressedContentFormat, HttpContent> getPrecompressedContents()
/*     */   {
/* 180 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\PrecompressedHttpContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */