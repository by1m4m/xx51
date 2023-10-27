/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider.Listener;
/*     */ import org.eclipse.jetty.client.Synchronizable;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.client.api.ContentProvider.Typed;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.io.RuntimeIOException;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiPartContentProvider
/*     */   extends AbstractTypedContentProvider
/*     */   implements AsyncContentProvider, Closeable
/*     */ {
/*  68 */   private static final Logger LOG = Log.getLogger(MultiPartContentProvider.class);
/*  69 */   private static final byte[] COLON_SPACE_BYTES = { 58, 32 };
/*  70 */   private static final byte[] CR_LF_BYTES = { 13, 10 };
/*     */   
/*  72 */   private final List<Part> parts = new ArrayList();
/*     */   private final ByteBuffer firstBoundary;
/*     */   private final ByteBuffer middleBoundary;
/*     */   private final ByteBuffer onlyBoundary;
/*     */   private final ByteBuffer lastBoundary;
/*  77 */   private final AtomicBoolean closed = new AtomicBoolean();
/*     */   private AsyncContentProvider.Listener listener;
/*  79 */   private long length = -1L;
/*     */   
/*     */   public MultiPartContentProvider()
/*     */   {
/*  83 */     this(makeBoundary());
/*     */   }
/*     */   
/*     */   public MultiPartContentProvider(String boundary)
/*     */   {
/*  88 */     super("multipart/form-data; boundary=" + boundary);
/*  89 */     String firstBoundaryLine = "--" + boundary + "\r\n";
/*  90 */     this.firstBoundary = ByteBuffer.wrap(firstBoundaryLine.getBytes(StandardCharsets.US_ASCII));
/*  91 */     String middleBoundaryLine = "\r\n" + firstBoundaryLine;
/*  92 */     this.middleBoundary = ByteBuffer.wrap(middleBoundaryLine.getBytes(StandardCharsets.US_ASCII));
/*  93 */     String onlyBoundaryLine = "--" + boundary + "--\r\n";
/*  94 */     this.onlyBoundary = ByteBuffer.wrap(onlyBoundaryLine.getBytes(StandardCharsets.US_ASCII));
/*  95 */     String lastBoundaryLine = "\r\n" + onlyBoundaryLine;
/*  96 */     this.lastBoundary = ByteBuffer.wrap(lastBoundaryLine.getBytes(StandardCharsets.US_ASCII));
/*     */   }
/*     */   
/*     */   private static String makeBoundary()
/*     */   {
/* 101 */     Random random = new Random();
/* 102 */     StringBuilder builder = new StringBuilder("JettyHttpClientBoundary");
/* 103 */     int length = builder.length();
/* 104 */     while (builder.length() < length + 16)
/*     */     {
/* 106 */       long rnd = random.nextLong();
/* 107 */       builder.append(Long.toString(rnd < 0L ? -rnd : rnd, 36));
/*     */     }
/* 109 */     builder.setLength(length + 16);
/* 110 */     return builder.toString();
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
/*     */   public void addFieldPart(String name, ContentProvider content, HttpFields fields)
/*     */   {
/* 130 */     addPart(new Part(name, null, "text/plain", content, fields, null));
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
/*     */   public void addFilePart(String name, String fileName, ContentProvider content, HttpFields fields)
/*     */   {
/* 151 */     addPart(new Part(name, fileName, "application/octet-stream", content, fields, null));
/*     */   }
/*     */   
/*     */   private void addPart(Part part)
/*     */   {
/* 156 */     this.parts.add(part);
/* 157 */     if (LOG.isDebugEnabled()) {
/* 158 */       LOG.debug("Added {}", new Object[] { part });
/*     */     }
/*     */   }
/*     */   
/*     */   public void setListener(AsyncContentProvider.Listener listener)
/*     */   {
/* 164 */     this.listener = listener;
/* 165 */     if (this.closed.get()) {
/* 166 */       this.length = calculateLength();
/*     */     }
/*     */   }
/*     */   
/*     */   private long calculateLength()
/*     */   {
/* 172 */     if (this.parts.isEmpty())
/*     */     {
/* 174 */       return this.onlyBoundary.remaining();
/*     */     }
/*     */     
/*     */ 
/* 178 */     long result = 0L;
/* 179 */     for (int i = 0; i < this.parts.size(); i++)
/*     */     {
/* 181 */       result += (i == 0 ? this.firstBoundary.remaining() : this.middleBoundary.remaining());
/* 182 */       Part part = (Part)this.parts.get(i);
/* 183 */       long partLength = part.length;
/* 184 */       result += partLength;
/* 185 */       if (partLength < 0L)
/*     */       {
/* 187 */         result = -1L;
/* 188 */         break;
/*     */       }
/*     */     }
/* 191 */     if (result > 0L)
/* 192 */       result += this.lastBoundary.remaining();
/* 193 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getLength()
/*     */   {
/* 200 */     return this.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<ByteBuffer> iterator()
/*     */   {
/* 206 */     return new MultiPartIterator(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/* 212 */     this.closed.compareAndSet(false, true);
/*     */   }
/*     */   
/*     */   private static class Part
/*     */   {
/*     */     private final String name;
/*     */     private final String fileName;
/*     */     private final String contentType;
/*     */     private final ContentProvider content;
/*     */     private final HttpFields fields;
/*     */     private final ByteBuffer headers;
/*     */     private final long length;
/*     */     
/*     */     private Part(String name, String fileName, String contentType, ContentProvider content, HttpFields fields)
/*     */     {
/* 227 */       this.name = name;
/* 228 */       this.fileName = fileName;
/* 229 */       this.contentType = contentType;
/* 230 */       this.content = content;
/* 231 */       this.fields = fields;
/* 232 */       this.headers = headers();
/* 233 */       this.length = (content.getLength() < 0L ? -1L : this.headers.remaining() + content.getLength());
/*     */     }
/*     */     
/*     */ 
/*     */     private ByteBuffer headers()
/*     */     {
/*     */       try
/*     */       {
/* 241 */         String contentDisposition = "Content-Disposition: form-data; name=\"" + this.name + "\"";
/* 242 */         if (this.fileName != null)
/* 243 */           contentDisposition = contentDisposition + "; filename=\"" + this.fileName + "\"";
/* 244 */         contentDisposition = contentDisposition + "\r\n";
/*     */         
/*     */ 
/* 247 */         String contentType = this.fields == null ? null : this.fields.get(HttpHeader.CONTENT_TYPE);
/* 248 */         if (contentType == null)
/*     */         {
/* 250 */           if ((this.content instanceof ContentProvider.Typed)) {
/* 251 */             contentType = ((ContentProvider.Typed)this.content).getContentType();
/*     */           } else
/* 253 */             contentType = this.contentType;
/*     */         }
/* 255 */         contentType = "Content-Type: " + contentType + "\r\n";
/*     */         
/* 257 */         if ((this.fields == null) || (this.fields.size() == 0))
/*     */         {
/* 259 */           String headers = contentDisposition;
/* 260 */           headers = headers + contentType;
/* 261 */           headers = headers + "\r\n";
/* 262 */           return ByteBuffer.wrap(headers.getBytes(StandardCharsets.UTF_8));
/*     */         }
/*     */         
/* 265 */         ByteArrayOutputStream buffer = new ByteArrayOutputStream((this.fields.size() + 1) * contentDisposition.length());
/* 266 */         buffer.write(contentDisposition.getBytes(StandardCharsets.UTF_8));
/* 267 */         buffer.write(contentType.getBytes(StandardCharsets.UTF_8));
/* 268 */         for (HttpField field : this.fields)
/*     */         {
/* 270 */           if (!HttpHeader.CONTENT_TYPE.equals(field.getHeader()))
/*     */           {
/* 272 */             buffer.write(field.getName().getBytes(StandardCharsets.US_ASCII));
/* 273 */             buffer.write(MultiPartContentProvider.COLON_SPACE_BYTES);
/* 274 */             String value = field.getValue();
/* 275 */             if (value != null)
/* 276 */               buffer.write(value.getBytes(StandardCharsets.UTF_8));
/* 277 */             buffer.write(MultiPartContentProvider.CR_LF_BYTES);
/*     */           } }
/* 279 */         buffer.write(MultiPartContentProvider.CR_LF_BYTES);
/* 280 */         return ByteBuffer.wrap(buffer.toByteArray());
/*     */       }
/*     */       catch (IOException x)
/*     */       {
/* 284 */         throw new RuntimeIOException(x);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 291 */       return String.format("%s@%x[name=%s,fileName=%s,length=%d,headers=%s]", new Object[] {
/* 292 */         getClass().getSimpleName(), 
/* 293 */         Integer.valueOf(hashCode()), this.name, this.fileName, 
/*     */         
/*     */ 
/* 296 */         Long.valueOf(this.content.getLength()), this.fields });
/*     */     }
/*     */   }
/*     */   
/*     */   private class MultiPartIterator
/*     */     implements Iterator<ByteBuffer>, Synchronizable, Callback, Closeable
/*     */   {
/*     */     private Iterator<ByteBuffer> iterator;
/*     */     private int index;
/* 305 */     private MultiPartContentProvider.State state = MultiPartContentProvider.State.FIRST_BOUNDARY;
/*     */     
/*     */     private MultiPartIterator() {}
/*     */     
/*     */     public boolean hasNext() {
/* 310 */       return this.state != MultiPartContentProvider.State.COMPLETE;
/*     */     }
/*     */     
/*     */ 
/*     */     public ByteBuffer next()
/*     */     {
/*     */       for (;;)
/*     */       {
/* 318 */         switch (MultiPartContentProvider.1.$SwitchMap$org$eclipse$jetty$client$util$MultiPartContentProvider$State[this.state.ordinal()])
/*     */         {
/*     */ 
/*     */         case 1: 
/* 322 */           if (MultiPartContentProvider.this.parts.isEmpty())
/*     */           {
/* 324 */             this.state = MultiPartContentProvider.State.COMPLETE;
/* 325 */             return MultiPartContentProvider.this.onlyBoundary.slice();
/*     */           }
/*     */           
/*     */ 
/* 329 */           this.state = MultiPartContentProvider.State.HEADERS;
/* 330 */           return MultiPartContentProvider.this.firstBoundary.slice();
/*     */         
/*     */ 
/*     */ 
/*     */         case 2: 
/* 335 */           MultiPartContentProvider.Part part = (MultiPartContentProvider.Part)MultiPartContentProvider.this.parts.get(this.index);
/* 336 */           ContentProvider content = MultiPartContentProvider.Part.access$800(part);
/* 337 */           if ((content instanceof AsyncContentProvider))
/* 338 */             ((AsyncContentProvider)content).setListener(MultiPartContentProvider.this.listener);
/* 339 */           this.iterator = content.iterator();
/* 340 */           this.state = MultiPartContentProvider.State.CONTENT;
/* 341 */           return MultiPartContentProvider.Part.access$1000(part).slice();
/*     */         
/*     */ 
/*     */         case 3: 
/* 345 */           if (this.iterator.hasNext())
/* 346 */             return (ByteBuffer)this.iterator.next();
/* 347 */           this.index += 1;
/* 348 */           if (this.index == MultiPartContentProvider.this.parts.size()) {
/* 349 */             this.state = MultiPartContentProvider.State.LAST_BOUNDARY;
/*     */           } else
/* 351 */             this.state = MultiPartContentProvider.State.MIDDLE_BOUNDARY;
/* 352 */           break;
/*     */         
/*     */ 
/*     */         case 4: 
/* 356 */           this.state = MultiPartContentProvider.State.HEADERS;
/* 357 */           return MultiPartContentProvider.this.middleBoundary.slice();
/*     */         
/*     */ 
/*     */         case 5: 
/* 361 */           this.state = MultiPartContentProvider.State.COMPLETE;
/* 362 */           return MultiPartContentProvider.this.lastBoundary.slice();
/*     */         
/*     */ 
/*     */         case 6: 
/* 366 */           throw new NoSuchElementException();
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getLock()
/*     */     {
/* 375 */       if ((this.iterator instanceof Synchronizable))
/* 376 */         return ((Synchronizable)this.iterator).getLock();
/* 377 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 383 */       if ((this.iterator instanceof Callback)) {
/* 384 */         ((Callback)this.iterator).succeeded();
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Throwable x)
/*     */     {
/* 390 */       if ((this.iterator instanceof Callback)) {
/* 391 */         ((Callback)this.iterator).failed(x);
/*     */       }
/*     */     }
/*     */     
/*     */     public void close() throws IOException
/*     */     {
/* 397 */       if ((this.iterator instanceof Closeable)) {
/* 398 */         ((Closeable)this.iterator).close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum State {
/* 404 */     FIRST_BOUNDARY,  HEADERS,  CONTENT,  MIDDLE_BOUNDARY,  LAST_BOUNDARY,  COMPLETE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\MultiPartContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */