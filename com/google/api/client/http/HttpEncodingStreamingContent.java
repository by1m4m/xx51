/*    */ package com.google.api.client.http;
/*    */ 
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import com.google.api.client.util.StreamingContent;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpEncodingStreamingContent
/*    */   implements StreamingContent
/*    */ {
/*    */   private final StreamingContent content;
/*    */   private final HttpEncoding encoding;
/*    */   
/*    */   public HttpEncodingStreamingContent(StreamingContent content, HttpEncoding encoding)
/*    */   {
/* 46 */     this.content = ((StreamingContent)Preconditions.checkNotNull(content));
/* 47 */     this.encoding = ((HttpEncoding)Preconditions.checkNotNull(encoding));
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 51 */     this.encoding.encode(this.content, out);
/*    */   }
/*    */   
/*    */   public StreamingContent getContent()
/*    */   {
/* 56 */     return this.content;
/*    */   }
/*    */   
/*    */   public HttpEncoding getEncoding()
/*    */   {
/* 61 */     return this.encoding;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpEncodingStreamingContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */