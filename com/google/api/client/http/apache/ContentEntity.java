/*    */ package com.google.api.client.http.apache;
/*    */ 
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import com.google.api.client.util.StreamingContent;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.http.entity.AbstractHttpEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ContentEntity
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final long contentLength;
/*    */   private final StreamingContent streamingContent;
/*    */   
/*    */   ContentEntity(long contentLength, StreamingContent streamingContent)
/*    */   {
/* 42 */     this.contentLength = contentLength;
/* 43 */     this.streamingContent = ((StreamingContent)Preconditions.checkNotNull(streamingContent));
/*    */   }
/*    */   
/*    */   public InputStream getContent() {
/* 47 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public long getContentLength() {
/* 51 */     return this.contentLength;
/*    */   }
/*    */   
/*    */   public boolean isRepeatable() {
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isStreaming() {
/* 59 */     return true;
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 63 */     if (this.contentLength != 0L) {
/* 64 */       this.streamingContent.writeTo(out);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\ContentEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */