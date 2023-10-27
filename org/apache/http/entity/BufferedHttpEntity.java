/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BufferedHttpEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private final byte[] buffer;
/*     */   
/*     */   public BufferedHttpEntity(HttpEntity entity)
/*     */     throws IOException
/*     */   {
/*  59 */     super(entity);
/*  60 */     if ((!entity.isRepeatable()) || (entity.getContentLength() < 0L)) {
/*  61 */       this.buffer = EntityUtils.toByteArray(entity);
/*     */     } else {
/*  63 */       this.buffer = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/*  68 */     if (this.buffer != null) {
/*  69 */       return this.buffer.length;
/*     */     }
/*  71 */     return this.wrappedEntity.getContentLength();
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException
/*     */   {
/*  76 */     if (this.buffer != null) {
/*  77 */       return new ByteArrayInputStream(this.buffer);
/*     */     }
/*  79 */     return this.wrappedEntity.getContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isChunked()
/*     */   {
/*  89 */     return (this.buffer == null) && (this.wrappedEntity.isChunked());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRepeatable()
/*     */   {
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException
/*     */   {
/* 103 */     if (outstream == null) {
/* 104 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 106 */     if (this.buffer != null) {
/* 107 */       outstream.write(this.buffer);
/*     */     } else {
/* 109 */       this.wrappedEntity.writeTo(outstream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStreaming()
/*     */   {
/* 116 */     return (this.buffer == null) && (this.wrappedEntity.isStreaming());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\entity\BufferedHttpEntity.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */