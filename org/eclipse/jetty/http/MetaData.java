/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaData
/*     */   implements Iterable<HttpField>
/*     */ {
/*     */   private HttpVersion _httpVersion;
/*     */   private final HttpFields _fields;
/*     */   private long _contentLength;
/*     */   private Supplier<HttpFields> _trailers;
/*     */   
/*     */   public MetaData(HttpVersion version, HttpFields fields)
/*     */   {
/*  34 */     this(version, fields, Long.MIN_VALUE);
/*     */   }
/*     */   
/*     */   public MetaData(HttpVersion version, HttpFields fields, long contentLength)
/*     */   {
/*  39 */     this._httpVersion = version;
/*  40 */     this._fields = fields;
/*  41 */     this._contentLength = contentLength;
/*     */   }
/*     */   
/*     */   protected void recycle()
/*     */   {
/*  46 */     this._httpVersion = null;
/*  47 */     if (this._fields != null)
/*  48 */       this._fields.clear();
/*  49 */     this._contentLength = Long.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public boolean isRequest()
/*     */   {
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isResponse()
/*     */   {
/*  59 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public HttpVersion getVersion()
/*     */   {
/*  69 */     return getHttpVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpVersion getHttpVersion()
/*     */   {
/*  77 */     return this._httpVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHttpVersion(HttpVersion httpVersion)
/*     */   {
/*  85 */     this._httpVersion = httpVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpFields getFields()
/*     */   {
/*  93 */     return this._fields;
/*     */   }
/*     */   
/*     */   public Supplier<HttpFields> getTrailerSupplier()
/*     */   {
/*  98 */     return this._trailers;
/*     */   }
/*     */   
/*     */   public void setTrailerSupplier(Supplier<HttpFields> trailers)
/*     */   {
/* 103 */     this._trailers = trailers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getContentLength()
/*     */   {
/* 111 */     if (this._contentLength == Long.MIN_VALUE)
/*     */     {
/* 113 */       if (this._fields != null)
/*     */       {
/* 115 */         HttpField field = this._fields.getField(HttpHeader.CONTENT_LENGTH);
/* 116 */         this._contentLength = (field == null ? -1L : field.getLongValue());
/*     */       }
/*     */     }
/* 119 */     return this._contentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<HttpField> iterator()
/*     */   {
/* 129 */     HttpFields fields = getFields();
/* 130 */     return fields == null ? Collections.emptyIterator() : fields.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 136 */     StringBuilder out = new StringBuilder();
/* 137 */     for (HttpField field : this)
/* 138 */       out.append(field).append(System.lineSeparator());
/* 139 */     return out.toString();
/*     */   }
/*     */   
/*     */   public static class Request extends MetaData
/*     */   {
/*     */     private String _method;
/*     */     private HttpURI _uri;
/*     */     
/*     */     public Request(HttpFields fields)
/*     */     {
/* 149 */       this(null, null, null, fields);
/*     */     }
/*     */     
/*     */     public Request(String method, HttpURI uri, HttpVersion version, HttpFields fields)
/*     */     {
/* 154 */       this(method, uri, version, fields, Long.MIN_VALUE);
/*     */     }
/*     */     
/*     */     public Request(String method, HttpURI uri, HttpVersion version, HttpFields fields, long contentLength)
/*     */     {
/* 159 */       super(fields, contentLength);
/* 160 */       this._method = method;
/* 161 */       this._uri = uri;
/*     */     }
/*     */     
/*     */     public Request(String method, HttpScheme scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields)
/*     */     {
/* 166 */       this(method, new HttpURI(scheme == null ? null : scheme.asString(), hostPort.getHost(), hostPort.getPort(), uri), version, fields);
/*     */     }
/*     */     
/*     */     public Request(String method, HttpScheme scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields, long contentLength)
/*     */     {
/* 171 */       this(method, new HttpURI(scheme == null ? null : scheme.asString(), hostPort.getHost(), hostPort.getPort(), uri), version, fields, contentLength);
/*     */     }
/*     */     
/*     */     public Request(String method, String scheme, HostPortHttpField hostPort, String uri, HttpVersion version, HttpFields fields, long contentLength)
/*     */     {
/* 176 */       this(method, new HttpURI(scheme, hostPort.getHost(), hostPort.getPort(), uri), version, fields, contentLength);
/*     */     }
/*     */     
/*     */     public Request(Request request)
/*     */     {
/* 181 */       this(request.getMethod(), new HttpURI(request.getURI()), request.getHttpVersion(), new HttpFields(request.getFields()), request.getContentLength());
/*     */     }
/*     */     
/*     */ 
/*     */     public void recycle()
/*     */     {
/* 187 */       super.recycle();
/* 188 */       this._method = null;
/* 189 */       if (this._uri != null) {
/* 190 */         this._uri.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isRequest()
/*     */     {
/* 196 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMethod()
/*     */     {
/* 204 */       return this._method;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setMethod(String method)
/*     */     {
/* 212 */       this._method = method;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public HttpURI getURI()
/*     */     {
/* 220 */       return this._uri;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getURIString()
/*     */     {
/* 228 */       return this._uri == null ? null : this._uri.toString();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setURI(HttpURI uri)
/*     */     {
/* 236 */       this._uri = uri;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 242 */       HttpFields fields = getFields();
/* 243 */       return String.format("%s{u=%s,%s,h=%d,cl=%d}", new Object[] {
/* 244 */         getMethod(), getURI(), getHttpVersion(), Integer.valueOf(fields == null ? -1 : fields.size()), Long.valueOf(getContentLength()) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Response extends MetaData
/*     */   {
/*     */     private int _status;
/*     */     private String _reason;
/*     */     
/*     */     public Response()
/*     */     {
/* 255 */       this(null, 0, null);
/*     */     }
/*     */     
/*     */     public Response(HttpVersion version, int status, HttpFields fields)
/*     */     {
/* 260 */       this(version, status, fields, Long.MIN_VALUE);
/*     */     }
/*     */     
/*     */     public Response(HttpVersion version, int status, HttpFields fields, long contentLength)
/*     */     {
/* 265 */       super(fields, contentLength);
/* 266 */       this._status = status;
/*     */     }
/*     */     
/*     */     public Response(HttpVersion version, int status, String reason, HttpFields fields, long contentLength)
/*     */     {
/* 271 */       super(fields, contentLength);
/* 272 */       this._reason = reason;
/* 273 */       this._status = status;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isResponse()
/*     */     {
/* 279 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getStatus()
/*     */     {
/* 287 */       return this._status;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getReason()
/*     */     {
/* 295 */       return this._reason;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setStatus(int status)
/*     */     {
/* 303 */       this._status = status;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setReason(String reason)
/*     */     {
/* 311 */       this._reason = reason;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 317 */       HttpFields fields = getFields();
/* 318 */       return String.format("%s{s=%d,h=%d,cl=%d}", new Object[] { getHttpVersion(), Integer.valueOf(getStatus()), Integer.valueOf(fields == null ? -1 : fields.size()), Long.valueOf(getContentLength()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\MetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */