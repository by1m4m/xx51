/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Trie;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHttpContent
/*     */   implements HttpContent
/*     */ {
/*     */   final Resource _resource;
/*     */   final String _contentType;
/*     */   final int _maxBuffer;
/*     */   Map<CompressedContentFormat, HttpContent> _precompressedContents;
/*     */   String _etag;
/*     */   
/*     */   public ResourceHttpContent(Resource resource, String contentType)
/*     */   {
/*  50 */     this(resource, contentType, -1, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public ResourceHttpContent(Resource resource, String contentType, int maxBuffer)
/*     */   {
/*  56 */     this(resource, contentType, maxBuffer, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public ResourceHttpContent(Resource resource, String contentType, int maxBuffer, Map<CompressedContentFormat, HttpContent> precompressedContents)
/*     */   {
/*  62 */     this._resource = resource;
/*  63 */     this._contentType = contentType;
/*  64 */     this._maxBuffer = maxBuffer;
/*  65 */     if (precompressedContents == null)
/*     */     {
/*  67 */       this._precompressedContents = null;
/*     */     }
/*     */     else
/*     */     {
/*  71 */       this._precompressedContents = new HashMap(precompressedContents.size());
/*  72 */       for (Map.Entry<CompressedContentFormat, HttpContent> entry : precompressedContents.entrySet())
/*     */       {
/*  74 */         this._precompressedContents.put((CompressedContentFormat)entry.getKey(), new PrecompressedHttpContent(this, (HttpContent)entry.getValue(), (CompressedContentFormat)entry.getKey()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getContentTypeValue()
/*     */   {
/*  83 */     return this._contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpField getContentType()
/*     */   {
/*  90 */     return this._contentType == null ? null : new HttpField(HttpHeader.CONTENT_TYPE, this._contentType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpField getContentEncoding()
/*     */   {
/*  97 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getContentEncodingValue()
/*     */   {
/* 104 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 111 */     return this._contentType == null ? null : MimeTypes.getCharsetFromContentType(this._contentType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MimeTypes.Type getMimeType()
/*     */   {
/* 118 */     return this._contentType == null ? null : (MimeTypes.Type)MimeTypes.CACHE.get(MimeTypes.getContentTypeWithoutCharset(this._contentType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpField getLastModified()
/*     */   {
/* 125 */     long lm = this._resource.lastModified();
/* 126 */     return lm >= 0L ? new HttpField(HttpHeader.LAST_MODIFIED, DateGenerator.formatDate(lm)) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getLastModifiedValue()
/*     */   {
/* 133 */     long lm = this._resource.lastModified();
/* 134 */     return lm >= 0L ? DateGenerator.formatDate(lm) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ByteBuffer getDirectBuffer()
/*     */   {
/* 141 */     if ((this._resource.length() <= 0L) || ((this._maxBuffer > 0) && (this._maxBuffer < this._resource.length()))) {
/* 142 */       return null;
/*     */     }
/*     */     try {
/* 145 */       return BufferUtil.toBuffer(this._resource, true);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 149 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpField getETag()
/*     */   {
/* 157 */     return new HttpField(HttpHeader.ETAG, getETagValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getETagValue()
/*     */   {
/* 164 */     return this._resource.getWeakETag();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ByteBuffer getIndirectBuffer()
/*     */   {
/* 171 */     if ((this._resource.length() <= 0L) || ((this._maxBuffer > 0) && (this._maxBuffer < this._resource.length()))) {
/* 172 */       return null;
/*     */     }
/*     */     try {
/* 175 */       return BufferUtil.toBuffer(this._resource, false);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 179 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpField getContentLength()
/*     */   {
/* 187 */     long l = this._resource.length();
/* 188 */     return l == -1L ? null : new HttpField.LongValueHttpField(HttpHeader.CONTENT_LENGTH, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getContentLengthValue()
/*     */   {
/* 195 */     return this._resource.length();
/*     */   }
/*     */   
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 202 */     return this._resource.getInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */   public ReadableByteChannel getReadableByteChannel()
/*     */     throws IOException
/*     */   {
/* 209 */     return this._resource.getReadableByteChannel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Resource getResource()
/*     */   {
/* 216 */     return this._resource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/* 223 */     this._resource.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 230 */     return String.format("%s@%x{r=%s,ct=%s,c=%b}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), this._resource, this._contentType, Boolean.valueOf(this._precompressedContents != null ? 1 : false) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<CompressedContentFormat, HttpContent> getPrecompressedContents()
/*     */   {
/* 237 */     return this._precompressedContents;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\ResourceHttpContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */